package zone.moddev.mc.cakeworld.world;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

/**
 * Dimension-aware vanilla Ruined Portal placement contract for CakeWorld's
 * Burnt-Sugar Arch.
 */
public final class BurntSugarArchStructureFeature
		extends StructureFeature<JigsawConfiguration> {
	public static final int SURFACE_OFFSET = 4;
	private static final int NETHER_MINIMUM_Y = 32;
	private static final int NETHER_MAXIMUM_Y = 80;

	public BurntSugarArchStructureFeature() {
		super(JigsawConfiguration.CODEC,
				BurntSugarArchStructureFeature
						::createPieces);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static Optional<PieceGenerator<JigsawConfiguration>>
			createPieces(
					PieceGeneratorSupplier.Context<
							JigsawConfiguration> context) {
		int middleX = context.chunkPos()
				.getMiddleBlockX();
		int middleZ = context.chunkPos()
				.getMiddleBlockZ();
		boolean nether = context.chunkGenerator()
				.getSeaLevel() == 32
				&& context.heightAccessor()
						.getMinBuildHeight() == 0;
		boolean projectToSurface = !nether;
		int startY = -SURFACE_OFFSET;

		if (nether) {
			WorldgenRandom random =
					new WorldgenRandom(
							new LegacyRandomSource(0L));
			random.setLargeFeatureSeed(
					context.seed(),
					context.chunkPos().x,
					context.chunkPos().z);
			startY = Mth.randomBetweenInclusive(
					random, NETHER_MINIMUM_Y,
					NETHER_MAXIMUM_Y);
			projectToSurface = false;
		} else {
			int surfaceY = context.chunkGenerator()
					.getFirstOccupiedHeight(
							middleX, middleZ,
							Heightmap.Types
									.WORLD_SURFACE_WG,
							context.heightAccessor());
			Holder<Biome> surfaceBiome =
					context.chunkGenerator()
							.getNoiseBiome(
									QuartPos.fromBlock(
											middleX),
									QuartPos.fromBlock(
											surfaceY),
									QuartPos.fromBlock(
											middleZ));
			ResourceLocation biomeId =
					surfaceBiome.unwrapKey()
							.map(key -> key.location())
							.orElse(null);
			if (new ResourceLocation("cakeworld",
					"soda_ocean").equals(biomeId)) {
				startY = context.chunkGenerator()
						.getFirstFreeHeight(
								middleX, middleZ,
								Heightmap.Types
										.OCEAN_FLOOR_WG,
								context.heightAccessor())
						- SURFACE_OFFSET;
				projectToSurface = false;
			}
		}

		Pools.bootstrap();
		BlockPos start = new BlockPos(
				context.chunkPos().getMinBlockX(),
				startY,
				context.chunkPos().getMinBlockZ());
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				start, true, projectToSurface);
	}
}
