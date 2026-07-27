package com.mcmoddev.cakeworld.world;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

/**
 * Places Crystal Mines below broad Marshmallow-Peaks surfaces.
 */
public final class RockCandyCrystalMineStructureFeature
		extends StructureFeature<JigsawConfiguration> {
	public static final int CENTRE_OFFSET = 16;
	public static final int SURFACE_OFFSET = 32;

	public RockCandyCrystalMineStructureFeature() {
		super(JigsawConfiguration.CODEC,
				RockCandyCrystalMineStructureFeature::createPieces);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
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
		for (int x : new int[] {-8, 0, 8}) {
			for (int z : new int[] {-8, 0, 8}) {
				int height = context.chunkGenerator()
						.getBaseHeight(
								centreX + x,
								centreZ + z,
								Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
								context.heightAccessor());
				totalHeight += height;
				minimumHeight = Math.min(
						minimumHeight, height);
				maximumHeight = Math.max(
						maximumHeight, height);
				samples++;
			}
		}
		if (minimumHeight < 48
				|| maximumHeight - minimumHeight > 16) {
			return Optional.empty();
		}
		int galleryY = totalHeight / samples
				- SURFACE_OFFSET;
		if (galleryY < context.heightAccessor()
				.getMinBuildHeight() + 4) {
			return Optional.empty();
		}
		Pools.bootstrap();
		BlockPos start = new BlockPos(
				context.chunkPos().getMinBlockX(),
				galleryY,
				context.chunkPos().getMinBlockZ());
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				start, true, false);
	}
}
