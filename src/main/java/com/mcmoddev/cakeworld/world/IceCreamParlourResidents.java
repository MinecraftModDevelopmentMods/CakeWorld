package com.mcmoddev.cakeworld.world;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
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
 * Moves Ice-Cream Parlour residents from worldgen's deferred entity sink into
 * the live server level exactly once at a safe server-tick boundary.
 *
 * <p>Procedural jigsaw pieces can save entities added while a distant chunk is
 * being generated without attaching them to the live entity manager until a
 * reload. The hidden structure-void marker is consumed only after both
 * residents join the completed level, so it is also a durable no-respawn
 * marker once players have met or removed them.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class IceCreamParlourResidents {
	private static final Logger LOGGER =
			LogManager.getLogger();
	private static final Queue<PendingCellar> PENDING =
			new ConcurrentLinkedQueue<>();

	private IceCreamParlourResidents() {
	}

	static void queue(WorldGenLevel world,
			BlockPos centre, BoundingBox bounds) {
		PENDING.add(new PendingCellar(
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
						.get(IceCreamParlourFeature
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
				bounds.minX() + 6,
				bounds.minY()
						+ IceCreamParlourStructureFeature
								.BURIED_DEPTH,
				bounds.minZ() + 6);
		if (!new ChunkPos(centre).equals(
					chunk.getPos())
				|| !IceCreamParlourFeature
						.hasBasement(
								level.getSeed(),
								centre)) {
			return;
		}

		// ChunkEvent.Load is emitted while the LevelChunk is still completing
		// its handoff. A same-thread executor can run immediately and recurse
		// into that half-loaded chunk, so a later server-tick queue is
		// required.
		PENDING.add(new PendingCellar(
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
			PendingCellar pending =
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
			BlockPos centre = pending.centre();
			BlockPos marker =
					IceCreamParlourFeature
							.residentMarker(centre);
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
			if (!IceCreamParlourFeature
					.spawnCuringPair(level, centre,
							pending.bounds())) {
				retry(pending);
			} else {
				LOGGER.info("Activated Ice-Cream Parlour curing pair at {} in {}",
						centre,
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
			PendingCellar pending) {
		if (pending.attempt() < 100) {
			PENDING.add(new PendingCellar(
					pending.dimension(),
					pending.centre(),
					pending.bounds(),
					pending.attempt() + 1,
					pending.waitForMarker()));
		} else {
			LOGGER.warn("Ice-Cream Parlour curing pair at {} in {} was still unavailable after {} server ticks",
					pending.centre(),
					pending.dimension().location(),
					pending.attempt());
		}
	}

	private record PendingCellar(
			ResourceKey<Level> dimension,
			BlockPos centre,
			BoundingBox bounds,
			int attempt,
			boolean waitForMarker) {
	}
}
