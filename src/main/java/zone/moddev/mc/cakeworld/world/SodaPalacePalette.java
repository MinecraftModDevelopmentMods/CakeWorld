package zone.moddev.mc.cakeworld.world;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Converts native Ocean Monument slices into Soda Palace architecture.
 *
 * <p>Loaded slices receive an idempotent, chunk-bounded pass restricted to
 * monuments whose centre lies in Soda Ocean, so ordinary and third-party
 * worlds are untouched. Native water is deliberately retained: replacing the
 * complete 58-by-23-by-58 water envelope through live-server block updates
 * caused an unacceptable 96.8-second server tick in fixed-world evidence.
 * Vanilla gold, wet sponge and sea-lantern blocks remain unchanged because
 * they are progression, reward and accessibility contracts.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class SodaPalacePalette {
	private static final Queue<PendingSlice> PENDING =
			new ConcurrentLinkedQueue<>();

	private SodaPalacePalette() {
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !level.dimension().equals(Level.OVERWORLD)) {
			return;
		}
		PENDING.add(new PendingSlice(level.dimension(),
				event.getChunk().getPos(), 0));
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
			ServerLevel level =
					server.getLevel(pending.dimension());
			if (level == null || !level.hasChunk(
					pending.chunk().x,
					pending.chunk().z)) {
				continue;
			}
			LevelChunk chunk = level.getChunk(
					pending.chunk().x,
					pending.chunk().z);
			ConfiguredStructureFeature<?, ?> monument =
					level.registryAccess()
							.registryOrThrow(Registry
									.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
							.get(SodaPalaceFeature.STRUCTURE_ID);
			if (monument == null) {
				continue;
			}
			boolean themed = false;
			StructureStart direct =
					chunk.getStartForFeature(monument);
			if (isSodaPalace(level, direct)) {
				themeLoadedChunks(level, direct);
				themed = true;
			}
			for (long reference
					: chunk.getReferencesForFeature(monument)) {
				ChunkPos startChunk = new ChunkPos(reference);
				LevelChunk owner = level.getChunk(
						startChunk.x, startChunk.z);
				StructureStart start =
						owner.getStartForFeature(monument);
				if (isSodaPalace(level, start)) {
					themeLoadedChunks(level, start);
					themed = true;
				}
			}
			if (!themed && pending.attempts() < 4
					&& (direct != null
							|| !chunk
									.getReferencesForFeature(monument)
									.isEmpty())) {
				PENDING.add(new PendingSlice(
						pending.dimension(), pending.chunk(),
						pending.attempts() + 1));
			}
		}
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		PENDING.clear();
	}

	private static boolean isSodaPalace(
			ServerLevel level, StructureStart start) {
		if (start == null || !start.isValid()) {
			return false;
		}
		BoundingBox bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				(bounds.minX() + bounds.maxX()) / 2,
				level.getSeaLevel(),
				(bounds.minZ() + bounds.maxZ()) / 2);
		return level.getBiome(centre).is(
				SodaPalaceFeature.GENERATES_IN);
	}

	private static void themeLoadedChunks(
			ServerLevel level, StructureStart start) {
		BoundingBox bounds = start.getBoundingBox();
		int minimumChunkX = Math.floorDiv(
				bounds.minX(), 16);
		int maximumChunkX = Math.floorDiv(
				bounds.maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				bounds.minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				bounds.maxZ(), 16);
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

	private static void themeChunk(ServerLevel level,
			LevelChunk chunk, StructureStart start) {
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
		if (current.is(Blocks.PRISMARINE)) {
			return CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK
					.get().defaultBlockState();
		}
		if (current.is(Blocks.PRISMARINE_BRICKS)) {
			return CakeWorldBlocks.CANDY_GLASS
					.get().defaultBlockState();
		}
		if (current.is(Blocks.DARK_PRISMARINE)) {
			return CakeWorldBlocks.GRAPE_GUMMY_BLOCK
					.get().defaultBlockState();
		}
		return current;
	}

	private record PendingSlice(
			net.minecraft.resources.ResourceKey<Level> dimension,
			ChunkPos chunk, int attempts) {
	}

}
