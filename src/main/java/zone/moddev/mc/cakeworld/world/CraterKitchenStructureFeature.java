package zone.moddev.mc.cakeworld.world;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

/**
 * Selects broad, well-supported End-island surfaces for Crater Kitchens.
 */
public final class CraterKitchenStructureFeature
		extends StructureFeature<JigsawConfiguration> {
	public static final int CENTRE_OFFSET = 16;
	public static final int CRATER_DEPTH = 5;

	public CraterKitchenStructureFeature() {
		super(JigsawConfiguration.CODEC,
				CraterKitchenStructureFeature::createPieces);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static Optional<PieceGenerator<JigsawConfiguration>>
			createPieces(
					PieceGeneratorSupplier.Context<
							JigsawConfiguration> context) {
		if (!context.validBiomeOnTop(
				Heightmap.Types.WORLD_SURFACE_WG)) {
			return Optional.empty();
		}
		int centreX = context.chunkPos().getMinBlockX()
				+ CENTRE_OFFSET;
		int centreZ = context.chunkPos().getMinBlockZ()
				+ CENTRE_OFFSET;
		int totalHeight = 0;
		int minimumHeight = Integer.MAX_VALUE;
		int maximumHeight = Integer.MIN_VALUE;
		int samples = 0;
		for (int x : new int[] {-10, 0, 10}) {
			for (int z : new int[] {-10, 0, 10}) {
				int height = context.chunkGenerator()
						.getBaseHeight(
								centreX + x,
								centreZ + z,
								Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
								context.heightAccessor());
				Holder<Biome> biome = context.chunkGenerator()
						.getNoiseBiome(
								QuartPos.fromBlock(centreX + x),
								QuartPos.fromBlock(height),
								QuartPos.fromBlock(centreZ + z));
				if (!context.validBiome().test(biome)) {
					return Optional.empty();
				}
				totalHeight += height;
				minimumHeight = Math.min(
						minimumHeight, height);
				maximumHeight = Math.max(
						maximumHeight, height);
				samples++;
			}
		}
		if (minimumHeight < 48
				|| maximumHeight - minimumHeight > 12) {
			return Optional.empty();
		}
		int craterFloorY =
				totalHeight / samples - CRATER_DEPTH;
		Pools.bootstrap();
		BlockPos start = new BlockPos(
				context.chunkPos().getMinBlockX(),
				craterFloorY,
				context.chunkPos().getMinBlockZ());
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				start, true, false);
	}
}
