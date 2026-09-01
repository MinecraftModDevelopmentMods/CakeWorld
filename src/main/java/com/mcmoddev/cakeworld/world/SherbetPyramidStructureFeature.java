package com.mcmoddev.cakeworld.world;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

/**
 * Vanilla Desert Pyramid placement contract for CakeWorld's Sherbet Pyramid.
 */
public final class SherbetPyramidStructureFeature extends JigsawFeature {
	public static final int BURIED_DEPTH = 14;

	public SherbetPyramidStructureFeature() {
		super(JigsawConfiguration.CODEC, -BURIED_DEPTH,
				true, true,
				SherbetPyramidStructureFeature::checkLocation);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static boolean checkLocation(
			PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		return context.validBiomeOnTop(
						Heightmap.Types.WORLD_SURFACE_WG)
				&& context.getLowestY(21, 21)
						>= context.chunkGenerator()
								.getSeaLevel();
	}
}
