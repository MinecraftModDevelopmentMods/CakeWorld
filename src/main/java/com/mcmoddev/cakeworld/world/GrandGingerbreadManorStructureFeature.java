package com.mcmoddev.cakeworld.world;

import java.util.Arrays;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

/**
 * Surface placement contract for CakeWorld's Grand Gingerbread Manor.
 *
 * <p>The structure retains the vanilla Woodland Mansion's minimum terrain
 * height and surface-structure phase. The Jigsaw start projects the complete
 * bounded procedural piece onto the world-surface heightmap.</p>
 */
public final class GrandGingerbreadManorStructureFeature
		extends JigsawFeature {
	public static final int MINIMUM_TERRAIN_Y = 60;

	public GrandGingerbreadManorStructureFeature() {
		super(JigsawConfiguration.CODEC, 0,
				true, true,
				GrandGingerbreadManorStructureFeature
						::checkLocation);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration
				.SURFACE_STRUCTURES;
	}

	private static boolean checkLocation(
			PieceGeneratorSupplier.Context<
					JigsawConfiguration> context) {
		int[] cornerHeights = context.getCornerHeights(
				context.chunkPos().getMinBlockX(),
				GrandGingerbreadManorFeature
						.MAXIMUM_OFFSET.getX(),
				context.chunkPos().getMinBlockZ(),
				GrandGingerbreadManorFeature
						.MAXIMUM_OFFSET.getZ());
		return Arrays.stream(cornerHeights).min()
				.orElse(Integer.MIN_VALUE)
				>= MINIMUM_TERRAIN_Y;
	}
}
