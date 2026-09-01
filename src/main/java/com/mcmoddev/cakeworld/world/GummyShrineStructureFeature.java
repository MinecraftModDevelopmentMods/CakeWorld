package com.mcmoddev.cakeworld.world;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

/**
 * Vanilla Jungle Temple placement contract for CakeWorld's Gummy Shrine.
 */
public final class GummyShrineStructureFeature extends JigsawFeature {
	public GummyShrineStructureFeature() {
		super(JigsawConfiguration.CODEC, 0, true, true,
				GummyShrineStructureFeature::checkLocation);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static boolean checkLocation(
			PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		return context.validBiomeOnTop(
						Heightmap.Types.WORLD_SURFACE_WG)
				&& context.getLowestY(12, 15)
						>= context.chunkGenerator()
								.getSeaLevel();
	}
}
