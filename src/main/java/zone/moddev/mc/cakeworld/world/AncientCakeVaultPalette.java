package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StrongholdPieces;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Converts native Stronghold slices into Ancient Cake Vault masonry.
 *
 * <p>The native configured structure and its sole concentric-ring placement
 * remain authoritative for serialization, Eye-of-Ender location, graph
 * generation, loot and progression. A delayed chunk-load pass themes only
 * Strongholds whose start lies in a CakeWorld land biome. It is idempotent and
 * leaves vanilla or third-party worlds untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class AncientCakeVaultPalette {
	private static final String PERSISTENT_KEY =
			"cakeworld_ancient_cake_vault_palette";
	private static final String CONVERTED_STARTS_KEY =
			"converted_starts";
	private static final Queue<PendingSlice> PENDING =
			new ConcurrentLinkedQueue<>();
	private static final ConcurrentMap<Long, Set<Long>>
			CONVERTED_STARTS = new ConcurrentHashMap<>();
	private static final int MAX_REFERENCE_ATTEMPTS = 4;
	private static final int MAX_START_DISCOVERY_ATTEMPTS = 1200;
	private static volatile Set<Long> strongholdStartCandidates;

	private AncientCakeVaultPalette() {
	}

	@SubscribeEvent
	public static void onChunkDataLoad(
			ChunkDataEvent.Load event) {
		if (event.getStatus()
				!= ChunkStatus.ChunkType.LEVELCHUNK
				|| !event.getData().contains(
						PERSISTENT_KEY,
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
	public static void onChunkDataSave(
			ChunkDataEvent.Save event) {
		if (!(event.getWorld()
				instanceof ServerLevel level)
				|| !level.dimension()
						.equals(Level.OVERWORLD)) {
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
				starts.stream()
						.mapToLong(Long::longValue)
						.sorted().toArray());
		event.getData().put(PERSISTENT_KEY,
				persistent);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk()
						instanceof LevelChunk chunk)
				|| !level.dimension()
						.equals(Level.OVERWORLD)) {
			return;
		}
		PENDING.add(new PendingSlice(
				level.dimension(),
				chunk.getPos(), 0));
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
		int pendingAtStart =
				Math.min(PENDING.size(), 256);
		for (int index = 0;
				index < pendingAtStart; index++) {
			PendingSlice pending =
					PENDING.poll();
			if (pending == null) {
				break;
			}
			ServerLevel level =
					server.getLevel(
							pending.dimension());
			if (level == null
					|| !level.hasChunk(
							pending.chunk().x,
							pending.chunk().z)) {
				continue;
			}
			LevelChunk chunk = level.getChunk(
					pending.chunk().x,
					pending.chunk().z);
			ConfiguredStructureFeature<?, ?>
					stronghold =
					level.registryAccess()
							.registryOrThrow(
									Registry
											.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
							.get(AncientCakeVaultFeature
									.STRUCTURE_ID);
			if (stronghold == null) {
				continue;
			}
			boolean themed = false;
			StructureStart direct =
					chunk.getStartForFeature(
							stronghold);
			if (isCakeWorldVault(
					level, direct)) {
				themeLoadedChunks(level, direct);
				themed = true;
			}
			for (long reference
					: chunk.getReferencesForFeature(
							stronghold)) {
				ChunkPos startChunk =
						new ChunkPos(reference);
				LevelChunk owner =
						level.getChunk(
								startChunk.x,
								startChunk.z);
				StructureStart start =
						owner.getStartForFeature(
								stronghold);
				if (isCakeWorldVault(
						level, start)) {
					themeLoadedChunks(level, start);
					themed = true;
				}
			}
			boolean hasReferences = !chunk
					.getReferencesForFeature(stronghold)
					.isEmpty();
			boolean awaitingStart = direct == null
					&& !hasReferences
					&& isStrongholdStartCandidate(
							level, pending.chunk());
			int maximumAttempts = awaitingStart
					? MAX_START_DISCOVERY_ATTEMPTS
					: MAX_REFERENCE_ATTEMPTS;
			if (!themed
					&& pending.attempts()
							< maximumAttempts
					&& (awaitingStart || direct != null
							|| hasReferences)) {
				PENDING.add(new PendingSlice(
						pending.dimension(),
						pending.chunk(),
						pending.attempts() + 1));
			}
		}
	}

	@SubscribeEvent
	public static void onServerStopped(
			ServerStoppedEvent event) {
		PENDING.clear();
		CONVERTED_STARTS.clear();
		strongholdStartCandidates = null;
	}

	/**
	 * Returns whether this is one of the generator's authoritative Stronghold
	 * start chunks.
	 *
	 * <p>A {@link LevelChunk} load event can arrive before Minecraft attaches
	 * its generated structure starts. Only the 128 concentric-ring candidates
	 * need to survive that lifecycle gap; caching their packed positions keeps
	 * ordinary chunk-load processing constant-time.</p>
	 */
	private static boolean isStrongholdStartCandidate(
			ServerLevel level, ChunkPos chunk) {
		Set<Long> candidates = strongholdStartCandidates;
		if (candidates == null) {
			StructureSet structureSet = level.registryAccess()
					.registryOrThrow(
							Registry.STRUCTURE_SET_REGISTRY)
					.get(AncientCakeVaultFeature
							.STRUCTURE_SET_ID);
			if (structureSet == null
					|| !(structureSet.placement()
							instanceof ConcentricRingsStructurePlacement
							placement)) {
				return false;
			}
			List<ChunkPos> ringPositions = level
					.getChunkSource().getGenerator()
					.getRingPositionsFor(placement);
			if (ringPositions == null) {
				return false;
			}
			candidates = ringPositions.stream()
					.map(ChunkPos::toLong)
					.collect(java.util.stream.Collectors
							.toUnmodifiableSet());
			strongholdStartCandidates = candidates;
		}
		return candidates.contains(chunk.toLong());
	}

	private static boolean isCakeWorldVault(
			ServerLevel level,
			StructureStart start) {
		if (start == null || !start.isValid()) {
			return false;
		}
		BoundingBox bounds =
				start.getBoundingBox();
		BlockPos centre = new BlockPos(
				(bounds.minX() + bounds.maxX()) / 2,
				level.getSeaLevel(),
				(bounds.minZ() + bounds.maxZ()) / 2);
		return level.getBiome(centre).is(
				AncientCakeVaultFeature
						.GENERATES_IN);
	}

	private static void themeLoadedChunks(
			ServerLevel level,
			StructureStart start) {
		BoundingBox bounds = start.getBoundingBox();
		int minimumChunkX =
				Math.floorDiv(bounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(bounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(bounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(bounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				if (level.hasChunk(chunkX, chunkZ)) {
					themeChunk(level,
							level.getChunk(
									chunkX, chunkZ),
							start);
				}
			}
		}
	}

	private static void themeChunk(
			ServerLevel level,
			LevelChunk chunk,
			StructureStart start) {
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
		applyEdiblePalette(
				level,
				level.structureFeatureManager(),
				level.getChunkSource()
						.getGenerator(),
				new Random(level.getSeed()
						^ chunkPos.toLong()),
				slice, chunkPos,
				new PiecesContainer(
						start.getPieces()));
		converted.add(startKey);
		chunk.setUnsaved(true);
	}

	private static Set<Long> convertedStarts(
			long chunkKey) {
		return CONVERTED_STARTS.computeIfAbsent(
				chunkKey,
				ignored -> ConcurrentHashMap
						.newKeySet());
	}

	/**
	 * Exposes vanilla's deterministic graph seam for regression evidence.
	 */
	public static void buildVanillaGraph(
			StructurePiecesBuilder builder,
			WorldgenRandom random, long seed,
			ChunkPos chunkPos, int seaLevel,
			int minimumY) {
		int attempt = 0;
		StrongholdPieces.StartPiece start;
		do {
			builder.clear();
			random.setLargeFeatureSeed(
					seed + attempt++, chunkPos.x,
					chunkPos.z);
			StrongholdPieces.resetPieces();
			start = new StrongholdPieces.StartPiece(
					random,
					chunkPos.getBlockX(2),
					chunkPos.getBlockZ(2));
			builder.addPiece(start);
			start.addChildren(start, builder, random);
			List<StructurePiece> pending =
					start.pendingChildren;
			while (!pending.isEmpty()) {
				StructurePiece piece = pending.remove(
						random.nextInt(
								pending.size()));
				piece.addChildren(start, builder,
						random);
			}
			builder.moveBelowSeaLevel(
					seaLevel, minimumY, random, 10);
		} while (builder.isEmpty()
				|| start.portalRoomPiece == null);
	}

	/**
	 * Public, idempotent palette seam used by direct and integrated evidence.
	 */
	public static void applyEdiblePalette(
			WorldGenLevel world,
			StructureFeatureManager structureManager,
			ChunkGenerator chunkGenerator,
			Random random, BoundingBox generationBounds,
			ChunkPos chunkPos,
			PiecesContainer pieces) {
		for (StructurePiece piece : pieces.pieces()) {
			BoundingBox pieceBounds =
					piece.getBoundingBox();
			if (!pieceBounds.intersects(
					generationBounds)) {
				continue;
			}
			int minimumX = Math.max(
					pieceBounds.minX(),
					generationBounds.minX());
			int minimumY = Math.max(
					pieceBounds.minY(),
					generationBounds.minY());
			int minimumZ = Math.max(
					pieceBounds.minZ(),
					generationBounds.minZ());
			int maximumX = Math.min(
					pieceBounds.maxX(),
					generationBounds.maxX());
			int maximumY = Math.min(
					pieceBounds.maxY(),
					generationBounds.maxY());
			int maximumZ = Math.min(
					pieceBounds.maxZ(),
					generationBounds.maxZ());
			BlockPos.MutableBlockPos cursor =
					new BlockPos.MutableBlockPos();
			for (int x = minimumX;
					x <= maximumX; x++) {
				for (int y = minimumY;
						y <= maximumY; y++) {
					for (int z = minimumZ;
							z <= maximumZ; z++) {
						cursor.set(x, y, z);
						BlockState current =
								world.getBlockState(
										cursor);
						BlockState themed =
								themedState(
										current);
						if (themed != current) {
							world.setBlock(cursor,
									themed, 2);
						}
					}
				}
			}
		}
	}

	private static BlockState themedState(
			BlockState current) {
		if (current.is(Blocks.STONE_BRICKS)) {
			return CakeWorldBlocks.GINGERBREAD_BRICKS
					.get().defaultBlockState();
		}
		if (current.is(
				Blocks.CRACKED_STONE_BRICKS)) {
			return CakeWorldBlocks.BISCUIT_STONE
					.get().defaultBlockState();
		}
		if (current.is(
				Blocks.MOSSY_STONE_BRICKS)) {
			return CakeWorldBlocks.ROCK_CANDY
					.get().defaultBlockState();
		}
		if (current.is(
				Blocks.INFESTED_STONE_BRICKS)) {
			return CakeWorldBlocks.CRUMB_MITE_NEST
					.get().defaultBlockState();
		}
		if (current.is(Blocks.STONE_BRICK_SLAB)
				|| current.is(Blocks.SMOOTH_STONE_SLAB)
				|| current.is(Blocks.OAK_PLANKS)) {
			return CakeWorldBlocks.WAFER_BLOCK
					.get().defaultBlockState();
		}
		if (current.is(Blocks.BOOKSHELF)) {
			return CakeWorldBlocks.COOKBOOK_LIBRARY
					.get().defaultBlockState();
		}
		if (current.is(
				Blocks.COBBLESTONE_STAIRS)) {
			return CakeWorldBlocks.GINGERBREAD_BRICKS
					.get().defaultBlockState();
		}
		if (current.is(
				Blocks.STONE_BRICK_STAIRS)) {
			return CakeWorldBlocks.MARSHMALLOW
					.get().defaultBlockState();
		}
		if (current.is(Blocks.LAVA)) {
			return CakeWorldFluids.HOT_FUDGE_BLOCK
					.get().defaultBlockState();
		}
		return current;
	}

	private record PendingSlice(
			net.minecraft.resources.ResourceKey<Level>
					dimension,
			ChunkPos chunk,
			int attempts) {
	}
}
