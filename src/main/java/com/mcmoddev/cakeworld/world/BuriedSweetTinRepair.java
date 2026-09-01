package com.mcmoddev.cakeworld.world;

import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Completes native Buried Treasure starts against CakeWorld terrain.
 *
 * <p>Vanilla's piece recognises only five exact vanilla support states.
 * OreSpawn correctly replaces those states in a fresh CakeWorld, so a valid
 * saved start can otherwise descend to minimum build height without creating
 * its chest. This one-time repair retains the native saved start, placement,
 * locate/map identity and loot table while accepting the solid edible ocean
 * floor. It adds only a compact Biscuit-Sand ring and Crumb cap around the
 * ordinary progression-compatible chest.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class BuriedSweetTinRepair {
	private static final String PERSISTENT_KEY =
			"cakeworld_buried_sweet_tin";
	private static final String CONVERTED_STARTS_KEY =
			"converted_starts";
	private static final int STABILITY_TICKS = 20;
	private static final int MAX_REPAIR_ATTEMPTS = 120;
	private static final Queue<PendingChunk> PENDING =
			new ConcurrentLinkedQueue<>();
	private static final ConcurrentMap<Long, Set<Long>>
			CONVERTED_STARTS = new ConcurrentHashMap<>();
	private static final ConcurrentMap<Long, Long>
			STABILIZE_AFTER = new ConcurrentHashMap<>();

	private BuriedSweetTinRepair() {
	}

	@SubscribeEvent
	public static void onChunkDataLoad(ChunkDataEvent.Load event) {
		if (event.getStatus()
				!= ChunkStatus.ChunkType.LEVELCHUNK
				|| !event.getData().contains(PERSISTENT_KEY,
						Tag.TAG_COMPOUND)) {
			return;
		}
		CompoundTag persistent = event.getData()
				.getCompound(PERSISTENT_KEY);
		Set<Long> starts = convertedStarts(
				event.getChunk().getPos().toLong());
		for (long start : persistent.getLongArray(
				CONVERTED_STARTS_KEY)) {
			starts.add(start);
		}
	}

	@SubscribeEvent
	public static void onChunkDataSave(ChunkDataEvent.Save event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !level.dimension().equals(
						Level.OVERWORLD)) {
			return;
		}
		Set<Long> starts = CONVERTED_STARTS.get(
				event.getChunk().getPos().toLong());
		if (starts == null || starts.isEmpty()) {
			return;
		}
		CompoundTag persistent = event.getData()
				.getCompound(PERSISTENT_KEY);
		persistent.putLongArray(CONVERTED_STARTS_KEY,
				starts.stream().mapToLong(Long::longValue)
						.sorted().toArray());
		event.getData().put(PERSISTENT_KEY, persistent);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk() instanceof LevelChunk chunk)
				|| !level.dimension().equals(
						Level.OVERWORLD)) {
			return;
		}
		PENDING.add(new PendingChunk(
				level.dimension(), chunk.getPos(), 0));
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
		int pendingAtStart = Math.min(PENDING.size(), 256);
		for (int index = 0; index < pendingAtStart; index++) {
			PendingChunk pending = PENDING.poll();
			if (pending == null) {
				break;
			}
			ServerLevel level = server.getLevel(
					pending.dimension());
			if (level == null || !level.hasChunk(
					pending.chunk().x,
					pending.chunk().z)) {
				continue;
			}
			LevelChunk chunk = level.getChunk(
					pending.chunk().x,
					pending.chunk().z);
			ConfiguredStructureFeature<?, ?> treasure =
					level.registryAccess()
							.registryOrThrow(Registry
									.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
							.get(BuriedSweetTinFeature
									.STRUCTURE_ID);
			if (treasure == null) {
				continue;
			}
			boolean referenced = false;
			boolean repaired = false;
			StructureStart direct =
					chunk.getStartForFeature(treasure);
			if (direct != null) {
				referenced = true;
			}
			if (isBuriedSweetTin(level, direct)) {
				repaired |= repair(level, chunk, direct);
			}
			for (long reference
					: chunk.getReferencesForFeature(treasure)) {
				referenced = true;
				ChunkPos startChunk = new ChunkPos(reference);
				LevelChunk owner = level.getChunk(
						startChunk.x, startChunk.z);
				StructureStart start =
						owner.getStartForFeature(treasure);
				if (isBuriedSweetTin(level, start)) {
					repaired |= repair(level, owner, start);
				}
			}
			if (!repaired && referenced
					&& pending.attempts()
							< MAX_REPAIR_ATTEMPTS) {
				PENDING.add(new PendingChunk(
						pending.dimension(),
						pending.chunk(),
						pending.attempts() + 1));
			}
		}
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		PENDING.clear();
		CONVERTED_STARTS.clear();
		STABILIZE_AFTER.clear();
	}

	private static boolean isBuriedSweetTin(
			ServerLevel level, StructureStart start) {
		if (start == null || !start.isValid()) {
			return false;
		}
		ChunkPos startChunk = start.getChunkPos();
		BlockPos anchor = new BlockPos(
				startChunk.getBlockX(9), 64,
				startChunk.getBlockZ(9));
		return level.getBiome(anchor).is(
				BuriedSweetTinFeature.GENERATES_IN);
	}

	private static boolean repair(ServerLevel level,
			LevelChunk owner, StructureStart start) {
		Set<Long> converted = convertedStarts(
				owner.getPos().toLong());
		long startKey = start.getChunkPos().toLong();
		if (converted.contains(startKey)) {
			return true;
		}
		ChunkPos startChunk = start.getChunkPos();
		BlockPos anchor = new BlockPos(
				startChunk.getBlockX(9), 90,
				startChunk.getBlockZ(9));
		int surface = level.getHeight(
				Heightmap.Types.OCEAN_FLOOR_WG,
				anchor.getX(), anchor.getZ());
		BlockPos existing = findExistingChest(level,
				anchor.getX(), anchor.getZ(), surface);
		if (existing != null
				&& hasStableCache(level, existing)) {
			Long stableAfter =
					STABILIZE_AFTER.get(startKey);
			if (stableAfter == null) {
				STABILIZE_AFTER.put(startKey,
						level.getGameTime()
								+ STABILITY_TICKS);
			} else if (level.getGameTime()
					>= stableAfter.longValue()) {
				converted.add(startKey);
				STABILIZE_AFTER.remove(startKey);
				owner.setUnsaved(true);
				return true;
			}
			return false;
		}
		BlockPos chest = placeSweetTin(level, anchor,
				level.getSeed() ^ startKey);
		if (chest == null) {
			return false;
		}
		STABILIZE_AFTER.put(startKey,
				level.getGameTime()
						+ STABILITY_TICKS);
		/*
		 * ChunkEvent.Load fires during the LevelChunk handoff. Require the
		 * wrapper to survive a complete server tick before persisting the
		 * conversion marker; otherwise late fluid/terrain completion can
		 * overwrite the ring while the marker prevents a repair.
		 */
		return false;
	}

	/**
	 * Public idempotent placement seam used by direct and integrated proof.
	 *
	 * @return the native-compatible chest position, or {@code null} when the
	 *         column exposes no safe solid support.
	 */
	public static BlockPos placeSweetTin(
			WorldGenLevel world, BlockPos anchor,
			long lootSeed) {
		int x = anchor.getX();
		int z = anchor.getZ();
		int surface = world.getHeight(
				Heightmap.Types.OCEAN_FLOOR_WG, x, z);
		BlockPos existing = findExistingChest(
				world, x, z, surface);
		BlockPos chest = existing != null
				? existing
				: findSupportedChestPosition(
						world, x, z, surface);
		if (chest == null) {
			return null;
		}
		boolean created = existing == null;
		if (created) {
			world.setBlock(chest,
					Blocks.CHEST.defaultBlockState(), 3);
		}
		BlockEntity entity = world.getBlockEntity(chest);
		if (!(entity
				instanceof RandomizableContainerBlockEntity
						container)) {
			return null;
		}
		if (created) {
			container.setLootTable(
					BuiltInLootTables.BURIED_TREASURE,
					new Random(lootSeed).nextLong());
		}
		if (container.getCustomName() == null) {
			((BaseContainerBlockEntity)container)
					.setCustomName(
							new TranslatableComponent(
									"container.cakeworld.buried_sweet_tin"));
		}
		for (Direction direction
				: Direction.Plane.HORIZONTAL) {
			BlockPos side =
					chest.relative(direction);
			BlockPos support = side.below();
			if (!world.getBlockState(support)
					.isFaceSturdy(world, support,
							Direction.UP)) {
				fillIfOpen(world, support,
						CakeWorldBlocks
								.BISCUIT_STONE
								.get()
								.defaultBlockState());
			}
			fillCacheBlock(world,
					side,
					CakeWorldBlocks.BISCUIT_SAND.get()
							.defaultBlockState());
		}
		fillCacheBlock(world, chest.above(),
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState());
		return chest.immutable();
	}

	private static BlockPos findExistingChest(
			WorldGenLevel world, int x, int z,
			int surface) {
		int maximumY = Math.min(
				world.getMaxBuildHeight() - 1,
				surface + 2);
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos();
		/*
		 * The first LevelChunk handoff can report an ocean-floor height that
		 * later rises as neighbouring generation and fluid completion settle.
		 * The native start fixes this exact x/z column, so searching its full
		 * vertical range is both unambiguous and necessary to preserve a
		 * chest already placed at the earlier surface instead of creating a
		 * succession of partial caches at newer surface heights.
		 */
		for (int y = maximumY;
				y >= world.getMinBuildHeight(); y--) {
			cursor.set(x, y, z);
			if (world.getBlockState(cursor)
					.is(Blocks.CHEST)) {
				return cursor.immutable();
			}
		}
		return null;
	}

	private static BlockPos findSupportedChestPosition(
			WorldGenLevel world, int x, int z,
			int surface) {
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos(
						x, Math.min(surface,
								world.getMaxBuildHeight()
										- 1),
						z);
		while (cursor.getY()
				> world.getMinBuildHeight()) {
			BlockPos below = cursor.below();
			if (world.getBlockState(below)
					.isFaceSturdy(world, below,
							Direction.UP)
					&& world.getBlockEntity(below)
							== null) {
				return cursor.immutable();
			}
			cursor.move(Direction.DOWN);
		}
		return null;
	}

	private static void fillIfOpen(WorldGenLevel world,
			BlockPos position,
			net.minecraft.world.level.block.state.BlockState state) {
		if (world.getBlockState(position)
				.getMaterial().isReplaceable()) {
			world.setBlock(position, state, 3);
		}
	}

	private static void fillCacheBlock(
			WorldGenLevel world, BlockPos position,
			net.minecraft.world.level.block.state.BlockState state) {
		net.minecraft.world.level.block.state.BlockState
				existing = world.getBlockState(position);
		/*
		 * Native buried treasure deliberately replaces its immediate sand
		 * shell. CakeWorld's equivalent shell is Biscuit Crumbs, which is
		 * solid and therefore not generally replaceable; keep that exception
		 * local to the five decorative cache positions.
		 */
		if (existing.getMaterial().isReplaceable()
				|| existing.is(CakeWorldBlocks
						.BISCUIT_CRUMBS.get())) {
			world.setBlock(position, state, 3);
		}
	}

	private static boolean hasStableCache(
			WorldGenLevel world, BlockPos chest) {
		if (!world.getBlockState(chest).is(Blocks.CHEST)
				|| !world.getBlockState(chest.above())
						.is(CakeWorldBlocks
								.BISCUIT_CRUMBS.get())) {
			return false;
		}
		int biscuitSand = 0;
		for (Direction direction
				: Direction.Plane.HORIZONTAL) {
			BlockPos side =
					chest.relative(direction);
			BlockPos support = side.below();
			if (world.getBlockState(side)
					.is(CakeWorldBlocks
							.BISCUIT_SAND.get())
					&& world.getBlockState(support)
							.isFaceSturdy(world,
									support,
									Direction.UP)) {
				biscuitSand++;
			}
		}
		return biscuitSand >= 2;
	}

	private static Set<Long> convertedStarts(long chunkKey) {
		return CONVERTED_STARTS.computeIfAbsent(chunkKey,
				ignored -> ConcurrentHashMap.newKeySet());
	}

	/**
	 * Compact bounds used by evidence without changing the native saved piece.
	 */
	public static BoundingBox cacheBounds(BlockPos chest) {
		return new BoundingBox(
				chest.getX() - 1, chest.getY(),
				chest.getZ() - 1,
				chest.getX() + 1,
				chest.getY() + 1,
				chest.getZ() + 1);
	}

	private record PendingChunk(
			ResourceKey<Level> dimension,
			ChunkPos chunk, int attempts) {
	}
}
