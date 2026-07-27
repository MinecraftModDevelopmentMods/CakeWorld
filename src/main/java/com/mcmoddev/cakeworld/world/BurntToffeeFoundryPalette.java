package com.mcmoddev.cakeworld.world;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Converts native Bastion Remnants in Fudge Wastes into Burnt-Toffee
 * Foundries.
 *
 * <p>The four start variants, complete Jigsaw graph, placement, chests and
 * native loot identities, Gold Blocks, Lodestones, Magma Cube spawner and
 * Piglin/Hoglin entity roles remain authoritative. Only the reusable
 * Blackstone and Basalt architecture receives a CakeWorld palette. Each
 * intersecting chunk records the converted start in persistent data so a
 * reload cannot reinterpret player changes.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class BurntToffeeFoundryPalette {
	private static final String PERSISTENT_KEY =
			"cakeworld_burnt_toffee_foundry_palette";
	private static final String CONVERTED_STARTS_KEY =
			"converted_starts";
	private static final Queue<PendingSlice> PENDING =
			new ConcurrentLinkedQueue<>();
	private static final ConcurrentMap<Long, Set<Long>>
			CONVERTED_STARTS = new ConcurrentHashMap<>();

	private BurntToffeeFoundryPalette() {
	}

	@SubscribeEvent
	public static void onChunkDataLoad(ChunkDataEvent.Load event) {
		if (event.getStatus() != ChunkStatus.ChunkType.LEVELCHUNK
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
				|| !level.dimension().equals(Level.NETHER)) {
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
				|| !level.dimension().equals(Level.NETHER)) {
			return;
		}
		PENDING.add(new PendingSlice(level.dimension(),
				chunk.getPos(), 0));
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
			PendingSlice pending = PENDING.poll();
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
			ConfiguredStructureFeature<?, ?> bastion =
					level.registryAccess()
							.registryOrThrow(Registry
									.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
							.get(BurntToffeeFoundryFeature
									.STRUCTURE_ID);
			if (bastion == null) {
				continue;
			}
			boolean themed = false;
			boolean referenced = false;
			StructureStart direct =
					chunk.getStartForFeature(bastion);
			if (direct != null) {
				referenced = true;
			}
			if (isBurntToffeeFoundry(level, direct)) {
				themeLoadedChunks(level, direct);
				themed = true;
			}
			for (long reference
					: chunk.getReferencesForFeature(bastion)) {
				referenced = true;
				ChunkPos startChunk = new ChunkPos(reference);
				LevelChunk owner = level.getChunk(
						startChunk.x, startChunk.z);
				StructureStart start =
						owner.getStartForFeature(bastion);
				if (isBurntToffeeFoundry(level, start)) {
					themeLoadedChunks(level, start);
					themed = true;
				}
			}
			if (!themed && referenced
					&& pending.attempts() < 4) {
				PENDING.add(new PendingSlice(
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
	}

	private static boolean isBurntToffeeFoundry(
			ServerLevel level, StructureStart start) {
		if (start == null || !start.isValid()) {
			return false;
		}
		BoundingBox bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				(bounds.minX() + bounds.maxX()) / 2,
				(bounds.minY() + bounds.maxY()) / 2,
				(bounds.minZ() + bounds.maxZ()) / 2);
		return level.getBiome(centre).is(
				BurntToffeeFoundryFeature.GENERATES_IN);
	}

	private static void themeLoadedChunks(
			ServerLevel level, StructureStart start) {
		BoundingBox bounds = start.getBoundingBox();
		int minimumChunkX = Math.floorDiv(bounds.minX(), 16);
		int maximumChunkX = Math.floorDiv(bounds.maxX(), 16);
		int minimumChunkZ = Math.floorDiv(bounds.minZ(), 16);
		int maximumChunkZ = Math.floorDiv(bounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				if (level.hasChunk(chunkX, chunkZ)) {
					themeChunk(level,
							level.getChunk(chunkX, chunkZ),
							start);
				}
			}
		}
	}

	private static void themeChunk(ServerLevel level,
			LevelChunk chunk, StructureStart start) {
		Set<Long> converted = convertedStarts(
				chunk.getPos().toLong());
		long startKey = start.getChunkPos().toLong();
		if (converted.contains(startKey)) {
			return;
		}
		ChunkPos chunkPos = chunk.getPos();
		BoundingBox slice = new BoundingBox(
				chunkPos.getMinBlockX(),
				level.getMinBuildHeight(),
				chunkPos.getMinBlockZ(),
				chunkPos.getMaxBlockX(),
				level.getMaxBuildHeight() - 1,
				chunkPos.getMaxBlockZ());
		applyEdiblePalette(level, slice,
				new PiecesContainer(start.getPieces()));
		converted.add(startKey);
		chunk.setUnsaved(true);
	}

	private static Set<Long> convertedStarts(long chunkKey) {
		return CONVERTED_STARTS.computeIfAbsent(chunkKey,
				ignored -> ConcurrentHashMap.newKeySet());
	}

	/**
	 * Public idempotent palette seam used by direct and integrated evidence.
	 */
	public static void applyEdiblePalette(
			WorldGenLevel world, BoundingBox generationBounds,
			PiecesContainer pieces) {
		for (StructurePiece piece : pieces.pieces()) {
			BoundingBox pieceBounds = piece.getBoundingBox();
			if (!pieceBounds.intersects(generationBounds)) {
				continue;
			}
			int minimumX = Math.max(pieceBounds.minX(),
					generationBounds.minX());
			int minimumY = Math.max(pieceBounds.minY(),
					generationBounds.minY());
			int minimumZ = Math.max(pieceBounds.minZ(),
					generationBounds.minZ());
			int maximumX = Math.min(pieceBounds.maxX(),
					generationBounds.maxX());
			int maximumY = Math.min(pieceBounds.maxY(),
					generationBounds.maxY());
			int maximumZ = Math.min(pieceBounds.maxZ(),
					generationBounds.maxZ());
			BlockPos.MutableBlockPos cursor =
					new BlockPos.MutableBlockPos();
			for (int x = minimumX; x <= maximumX; x++) {
				for (int y = minimumY; y <= maximumY; y++) {
					for (int z = minimumZ;
							z <= maximumZ; z++) {
						cursor.set(x, y, z);
						BlockState current =
								world.getBlockState(cursor);
						BlockState themed =
								themedState(current);
						if (themed != current) {
							world.setBlock(cursor, themed, 2);
						}
					}
				}
			}
		}
	}

	private static BlockState themedState(BlockState current) {
		if (current.is(Blocks.BLACKSTONE)) {
			return CakeWorldBlocks.BURNT_SUGAR_ROCK
					.get().defaultBlockState();
		}
		if (current.is(Blocks.POLISHED_BLACKSTONE_BRICKS)) {
			return CakeWorldBlocks.BURNT_TOFFEE_BRICKS
					.get().defaultBlockState();
		}
		if (current.is(
				Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)) {
			return CakeWorldBlocks.CRACKED_BURNT_TOFFEE_BRICKS
					.get().defaultBlockState();
		}
		if (current.is(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
				|| current.is(Blocks.BLACKSTONE_STAIRS)) {
			return copySharedProperties(current,
					CakeWorldBlocks.BURNT_TOFFEE_STAIRS
							.get().defaultBlockState());
		}
		if (current.is(Blocks.BLACKSTONE_SLAB)) {
			return copySharedProperties(current,
					CakeWorldBlocks.BURNT_TOFFEE_SLAB
							.get().defaultBlockState());
		}
		if (current.is(Blocks.BLACKSTONE_WALL)) {
			return copySharedProperties(current,
					CakeWorldBlocks.BURNT_TOFFEE_WALL
							.get().defaultBlockState());
		}
		if (current.is(Blocks.CHISELED_POLISHED_BLACKSTONE)) {
			return CakeWorldBlocks.STAMPED_BURNT_TOFFEE
					.get().defaultBlockState();
		}
		if (current.is(Blocks.GILDED_BLACKSTONE)) {
			return CakeWorldBlocks.GILDED_BURNT_TOFFEE
					.get().defaultBlockState();
		}
		if (current.is(Blocks.BASALT)
				|| current.is(Blocks.POLISHED_BASALT)) {
			return copySharedProperties(current,
					CakeWorldBlocks.BURNT_TOFFEE_PILLAR
							.get().defaultBlockState());
		}
		return current;
	}

	private static BlockState copySharedProperties(
			BlockState source, BlockState target) {
		BlockState copied = target;
		for (Property<?> property : source.getProperties()) {
			if (copied.hasProperty(property)) {
				copied = copyProperty(source, copied, property);
			}
		}
		return copied;
	}

	private static <T extends Comparable<T>>
			BlockState copyProperty(BlockState source,
					BlockState target, Property<T> property) {
		return target.setValue(property,
				source.getValue(property));
	}

	private record PendingSlice(
			net.minecraft.resources.ResourceKey<Level> dimension,
			ChunkPos chunk, int attempts) {
	}
}
