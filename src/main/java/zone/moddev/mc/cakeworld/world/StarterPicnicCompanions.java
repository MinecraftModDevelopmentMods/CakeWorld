package zone.moddev.mc.cakeworld.world;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.CustardCat;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Hands Picnic-Hamlet companions from chunk generation to the live server.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class StarterPicnicCompanions {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final int MAX_WORLDGEN_WAIT_TICKS = 1200;
	private static final Queue<PendingCompanion> PENDING =
			new ConcurrentLinkedQueue<>();

	private StarterPicnicCompanions() {
	}

	static void queue(WorldGenLevel world,
			BlockPos centre, int catType) {
		queue(world.getLevel().dimension(), centre, catType);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk() instanceof LevelChunk chunk)
				|| !level.dimension().equals(Level.OVERWORLD)) {
			return;
		}
		for (LevelChunkSection section : chunk.getSections()) {
			if (section == null || section.hasOnlyAir()
					|| !section.maybeHas(state -> state.is(
							CakeWorldBlocks.COOKBOOK_KIOSK.get()))) {
				continue;
			}
			for (int localX = 0; localX < 16; localX++) {
				for (int localY = 0; localY < 16; localY++) {
					for (int localZ = 0; localZ < 16; localZ++) {
						if (!section.getBlockState(
								localX, localY, localZ)
								.is(CakeWorldBlocks.COOKBOOK_KIOSK.get())) {
							continue;
						}
						BlockPos centre = new BlockPos(
								chunk.getPos().getMinBlockX() + localX,
								section.bottomBlockY() + localY - 1,
								chunk.getPos().getMinBlockZ() + localZ);
						if (StarterPicnicFeature.fitsWithinChunk(
								centre, chunk.getPos())
								&& isPicnicLayout(chunk, centre)) {
							int catType = Math.floorMod(
									(int) (level.getSeed()
											^ centre.asLong()), 10);
							queue(level.dimension(), centre, catType);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.START || PENDING.isEmpty()) {
			return;
		}
		MinecraftServer server =
				ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return;
		}
		int pendingAtStart = PENDING.size();
		for (int index = 0; index < pendingAtStart; index++) {
			PendingCompanion pending = PENDING.poll();
			if (pending == null) {
				break;
			}
			ServerLevel level = server.getLevel(pending.dimension());
			if (level == null || !level.hasChunkAt(pending.centre())) {
				retry(pending, true);
				continue;
			}
			if (!level.getBlockState(pending.centre().above())
					.is(CakeWorldBlocks.COOKBOOK_KIOSK.get())) {
				// Later decoration can legitimately replace a candidate feature.
				retry(pending, false);
				continue;
			}
			if (!spawn(level, pending.centre(), pending.catType())) {
				retry(pending, true);
				continue;
			}
			LOGGER.info("Ensured Picnic-Hamlet Custard Cat at {} in {}",
					pending.centre(), pending.dimension().location());
		}
	}

	private static void queue(ResourceKey<Level> dimension,
			BlockPos centre, int catType) {
		boolean alreadyPending = PENDING.stream()
				.anyMatch(pending -> pending.dimension().equals(dimension)
						&& pending.centre().equals(centre));
		if (!alreadyPending) {
			PENDING.add(new PendingCompanion(
					dimension, centre.immutable(), catType, 0));
		}
	}

	private static boolean isPicnicLayout(
			LevelChunk chunk, BlockPos centre) {
		if (!chunk.getBlockState(centre.above())
				.is(CakeWorldBlocks.COOKBOOK_KIOSK.get())) {
			return false;
		}
		for (int z = 0; z <= 4; z++) {
			if (!chunk.getBlockState(centre.offset(0, 0, z))
					.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())) {
				return false;
			}
		}
		for (int shelterX : new int[] {-2, 2}) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					if (!chunk.getBlockState(centre.offset(
							shelterX + x, 3, -2 + z))
							.is(CakeWorldBlocks.ICING.get())) {
						return false;
					}
				}
			}
		}
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos seat = centre.relative(direction, 2).above();
			if (!chunk.getBlockState(seat)
					.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
					|| !chunk.getBlockState(seat.above())
							.is(CakeWorldBlocks.ICING_LAYER.get())) {
				return false;
			}
		}
		return true;
	}

	static boolean spawn(ServerLevel level,
			BlockPos centre, int catType) {
		BlockPos shelter = centre.offset(-2, 1, -2);
		boolean alreadyPresent = level.getEntitiesOfClass(
				CustardCat.class,
				new AABB(shelter).inflate(2.0D))
				.stream()
				.anyMatch(cat -> cat.isTame()
						&& cat.isOrderedToSit()
						&& cat.isPersistenceRequired()
						&& cat.isInvulnerable()
						&& cat.hasRestriction()
						&& cat.getRestrictCenter().equals(centre));
		if (alreadyPresent) {
			return true;
		}
		CustardCat cat = CakeWorldEntities.CUSTARD_CAT.get().create(level);
		if (cat == null) {
			return false;
		}
		cat.moveTo(shelter.getX() + 0.5D, shelter.getY(),
				shelter.getZ() + 0.5D, 0.0F, 0.0F);
		cat.setCatType(catType);
		cat.setTame(true);
		cat.setOrderedToSit(true);
		cat.setPersistenceRequired();
		cat.setInvulnerable(true);
		cat.restrictTo(centre, 12);
		return level.addFreshEntity(cat);
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		PENDING.clear();
	}

	private static void retry(PendingCompanion pending,
			boolean warnOnExhaustion) {
		if (pending.attempt() < MAX_WORLDGEN_WAIT_TICKS) {
			PENDING.add(new PendingCompanion(
					pending.dimension(), pending.centre(),
					pending.catType(), pending.attempt() + 1));
		} else if (warnOnExhaustion) {
			LOGGER.warn("Picnic-Hamlet Custard Cat at {} in {} was still unavailable after {} server ticks",
					pending.centre(), pending.dimension().location(),
					MAX_WORLDGEN_WAIT_TICKS);
		} else {
			LOGGER.debug("Discarded overwritten Picnic-Hamlet companion candidate at {} in {}",
					pending.centre(), pending.dimension().location());
		}
	}

	private record PendingCompanion(
			ResourceKey<Level> dimension,
			BlockPos centre,
			int catType,
			int attempt) {
	}
}
