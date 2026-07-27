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
 * Four-corner surface placement for the Wafer Windmill landmark.
 */
public final class WaferWindmillStructureFeature
		extends StructureFeature<JigsawConfiguration> {
	public static final int CENTRE_OFFSET = 10;

	public WaferWindmillStructureFeature() {
		super(JigsawConfiguration.CODEC,
				WaferWindmillStructureFeature::createPieces);
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
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				totalHeight += context.chunkGenerator()
						.getBaseHeight(
								centreX + x,
								centreZ + z,
								Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
								context.heightAccessor());
			}
		}
		int floorY = totalHeight / 4;
		Pools.bootstrap();
		BlockPos start = new BlockPos(
				context.chunkPos().getMinBlockX(),
				floorY,
				context.chunkPos().getMinBlockZ());
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				start, true, false);
	}
}
