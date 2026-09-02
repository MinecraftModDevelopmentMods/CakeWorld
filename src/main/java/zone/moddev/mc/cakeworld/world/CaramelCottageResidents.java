package zone.moddev.mc.cakeworld.world;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Activates a Caramel Cottage's one-time Baker and Cat after worldgen hands
 * the owning chunk to the live server level.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class CaramelCottageResidents {
	private static final Logger LOGGER =
			LogManager.getLogger();
	private static final int MAX_WORLDGEN_MARKER_WAIT_TICKS = 1200;
	private static final int MAX_SPAWN_RETRY_TICKS = 100;
	private static final Queue<PendingCottage> PENDING =
			new ConcurrentLinkedQueue<>();

	private CaramelCottageResidents() {
	}

	static void queue(WorldGenLevel world,
			BlockPos centre, BoundingBox bounds) {
		PENDING.add(new PendingCottage(
				world.getLevel().dimension(),
				centre.immutable(), bounds, 0,
				true));
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk()
						instanceof LevelChunk chunk)) {
			return;
		}
		ConfiguredStructureFeature<?, ?> configured =
				level.registryAccess()
						.registryOrThrow(Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.get(CaramelCottageFeature
								.STRUCTURE_ID);
		if (configured == null) {
			return;
		}
		StructureStart start =
				chunk.getStartForFeature(configured);
		if (start == null || !start.isValid()) {
			return;
		}
		BoundingBox bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX()
						+ CaramelCottageStructureFeature
								.CENTRE_OFFSET,
				bounds.minY(),
				bounds.minZ()
						+ CaramelCottageStructureFeature
								.CENTRE_OFFSET);
		if (!new ChunkPos(centre).equals(
				chunk.getPos())) {
			return;
		}
		PENDING.add(new PendingCottage(
				level.dimension(),
				centre.immutable(), bounds, 0,
				false));
	}

	@SubscribeEvent
	public static void onServerTick(
			TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.START
				|| PENDING.isEmpty()) {
			return;
		}
		MinecraftServer server =
				ServerLifecycleHooks
						.getCurrentServer();
		if (server == null) {
			return;
		}
		int pendingAtStart = PENDING.size();
		for (int index = 0;
				index < pendingAtStart; index++) {
			PendingCottage pending =
					PENDING.poll();
			if (pending == null) {
				break;
			}
			ServerLevel level =
					server.getLevel(
							pending.dimension());
			if (level == null) {
				continue;
			}
			Rotation rotation =
					CaramelCottageFeature.orientation(
							level.getSeed(),
							pending.centre());
			BlockPos marker =
					CaramelCottageFeature
							.residentMarker(
									pending.centre(),
									rotation);
			if (!level.hasChunkAt(marker)) {
				retry(pending);
				continue;
			}
			if (!level.getBlockState(marker)
					.is(Blocks.STRUCTURE_VOID)) {
				if (pending.waitForMarker()) {
					retry(pending);
				}
				continue;
			}
			if (!CaramelCottageFeature
					.finalizeFreshWorldgen(
							level,
							pending.centre(),
							pending.bounds())) {
				retry(pending);
			} else {
				LOGGER.info("Activated Caramel Cottage residents at {} in {}",
						pending.centre(),
						pending.dimension()
								.location());
			}
		}
	}

	@SubscribeEvent
	public static void onServerStopped(
			ServerStoppedEvent event) {
		PENDING.clear();
	}

	private static void retry(
			PendingCottage pending) {
		int maximumAttempts = pending.waitForMarker()
				? MAX_WORLDGEN_MARKER_WAIT_TICKS
				: MAX_SPAWN_RETRY_TICKS;
		if (pending.attempt() < maximumAttempts) {
			PENDING.add(new PendingCottage(
					pending.dimension(),
					pending.centre(),
					pending.bounds(),
					pending.attempt() + 1,
					pending.waitForMarker()));
		} else {
			LOGGER.warn("Caramel Cottage residents at {} in {} were still unavailable after {} server ticks",
					pending.centre(),
					pending.dimension().location(),
					maximumAttempts);
		}
	}

	private record PendingCottage(
			ResourceKey<Level> dimension,
			BlockPos centre,
			BoundingBox bounds,
			int attempt,
			boolean waitForMarker) {
	}
}
