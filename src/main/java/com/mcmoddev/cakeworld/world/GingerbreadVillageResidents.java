package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.GingerbreadFolk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Completes Gingerbread Village POI ownership at the live-server boundary.
 *
 * <p>Structure features may build on parallel worldgen workers. Mutating the
 * live {@link PoiManager} from those workers can corrupt its section maps, so
 * the feature only writes blocks, resident memories and chunk entity data.
 * This handoff runs after the completed chunk reaches the server thread,
 * restores any POI records not discovered from block states, and claims each
 * resident's home, job site and meeting point exactly once.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class GingerbreadVillageResidents {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String CLAIMED_KEY =
			"CakeWorldGingerbreadVillagePoisClaimed";
	private static final int MAX_ATTEMPTS = 200;
	private static final Queue<PendingVillage> PENDING =
			new ConcurrentLinkedQueue<>();

	private GingerbreadVillageResidents() {
	}

	static void queue(WorldGenLevel world, BlockPos centre) {
		if (!(world instanceof ServerLevel)) {
			PENDING.add(new PendingVillage(
					world.getLevel().dimension(),
					centre.immutable(), 0));
		}
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk() instanceof LevelChunk chunk)) {
			return;
		}
		ChunkPos chunkPos = chunk.getPos();
		// Structure starts are not guaranteed to be attached yet when Forge
		// emits ChunkEvent.Load. The feature's bell is deliberately fixed at
		// the start chunk's +8/+8 column, so that durable block marker is a
		// safer handoff signal than observing a half-loaded start map.
		BlockPos centre = findCentre(level, chunk,
				chunkPos.getMinBlockX() + 8,
				chunkPos.getMinBlockZ() + 8);
		if (centre != null) {
			PENDING.add(new PendingVillage(level.dimension(),
					centre, 0));
		}
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.START
				|| PENDING.isEmpty()) {
			return;
		}
		MinecraftServer server =
				ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return;
		}
		int pendingAtStart = PENDING.size();
		for (int index = 0; index < pendingAtStart; index++) {
			PendingVillage pending = PENDING.poll();
			if (pending == null) {
				break;
			}
			ServerLevel level = server.getLevel(
					pending.dimension());
			if (level == null
					|| !level.hasChunkAt(pending.centre())) {
				retry(pending);
				continue;
			}
			List<GingerbreadFolk> residents =
					level.getEntitiesOfClass(
							GingerbreadFolk.class,
							new AABB(pending.centre())
									.inflate(16.0D));
			if (residents.size() != 4) {
				retry(pending);
				continue;
			}
			boolean complete = true;
			for (GingerbreadFolk resident : residents) {
				complete &= repairResident(level, resident);
			}
			if (!complete) {
				retry(pending);
				continue;
			}
			LOGGER.info("Activated Gingerbread Village POIs at {} in {}",
					pending.centre(),
					pending.dimension().location());
		}
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		PENDING.clear();
	}

	private static boolean repairResident(ServerLevel level,
			GingerbreadFolk resident) {
		if (resident.getPersistentData().getBoolean(CLAIMED_KEY)) {
			return true;
		}
		Optional<GlobalPos> home = resident.getBrain()
				.getMemory(MemoryModuleType.HOME);
		Optional<GlobalPos> job = resident.getBrain()
				.getMemory(MemoryModuleType.JOB_SITE);
		Optional<GlobalPos> meeting = resident.getBrain()
				.getMemory(MemoryModuleType.MEETING_POINT);
		PoiType jobType = jobType(resident.getVillagerData()
				.getProfession());
		if (home.isEmpty() || job.isEmpty() || meeting.isEmpty()
				|| jobType == null
				|| !ensureHome(level, home.get())
				|| !claim(level, job.get(), jobType)
				|| !claim(level, meeting.get(), PoiType.MEETING)) {
			return false;
		}
		resident.getPersistentData().putBoolean(CLAIMED_KEY, true);
		return true;
	}

	private static boolean ensureHome(ServerLevel level,
			GlobalPos globalPos) {
		if (!sameLevel(level, globalPos)
				|| !level.hasChunkAt(globalPos.pos())) {
			return false;
		}
		BlockPos home = globalPos.pos();
		BlockState state = level.getBlockState(home);
		if (!state.is(Blocks.RED_BED)) {
			return false;
		}
		ensurePoi(level, home, PoiType.HOME);
		BlockPos otherHalf = state.getValue(BedBlock.PART)
				== BedPart.FOOT
						? home.relative(state.getValue(BedBlock.FACING))
						: home.relative(state.getValue(BedBlock.FACING)
								.getOpposite());
		if (level.getBlockState(otherHalf).is(Blocks.RED_BED)) {
			ensurePoi(level, otherHalf, PoiType.HOME);
		}
		return claim(level, globalPos, PoiType.HOME);
	}

	private static boolean claim(ServerLevel level,
			GlobalPos globalPos, PoiType type) {
		if (!sameLevel(level, globalPos)
				|| !level.hasChunkAt(globalPos.pos())) {
			return false;
		}
		BlockPos pos = globalPos.pos();
		ensurePoi(level, pos, type);
		PoiManager manager = level.getPoiManager();
		if (manager.take(type::equals, pos::equals, pos, 1)
				.isPresent()) {
			return true;
		}
		return manager.getCountInRange(type::equals, pos, 1,
				PoiManager.Occupancy.IS_OCCUPIED) > 0;
	}

	private static void ensurePoi(ServerLevel level,
			BlockPos pos, PoiType type) {
		if (!level.getPoiManager().existsAtPosition(type, pos)) {
			level.getPoiManager().add(pos, type);
		}
	}

	private static boolean sameLevel(ServerLevel level,
			GlobalPos globalPos) {
		return globalPos.dimension().equals(level.dimension());
	}

	private static PoiType jobType(VillagerProfession profession) {
		if (profession == VillagerProfession.LIBRARIAN) {
			return PoiType.LIBRARIAN;
		}
		if (profession == VillagerProfession.BUTCHER) {
			return PoiType.BUTCHER;
		}
		if (profession == VillagerProfession.CARTOGRAPHER) {
			return PoiType.CARTOGRAPHER;
		}
		if (profession == VillagerProfession.FARMER) {
			return PoiType.FARMER;
		}
		return null;
	}

	private static BlockPos findCentre(ServerLevel level,
			LevelChunk chunk, int x, int z) {
		for (int y = level.getMinBuildHeight();
				y < level.getMaxBuildHeight(); y++) {
			BlockPos bell = new BlockPos(x, y, z);
			if (chunk.getBlockState(bell).is(Blocks.BELL)) {
				return bell.below().immutable();
			}
		}
		return null;
	}

	private static void retry(PendingVillage pending) {
		if (pending.attempt() < MAX_ATTEMPTS) {
			PENDING.add(new PendingVillage(
					pending.dimension(), pending.centre(),
					pending.attempt() + 1));
		} else {
			LOGGER.warn("Gingerbread Village POIs at {} in {} were still unavailable after {} server ticks",
					pending.centre(),
					pending.dimension().location(),
					pending.attempt());
		}
	}

	private record PendingVillage(
			ResourceKey<Level> dimension,
			BlockPos centre, int attempt) {
	}
}
