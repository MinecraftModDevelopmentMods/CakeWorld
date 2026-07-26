package com.mcmoddev.cakeworld.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

/**
 * Underground placement contract for CakeWorld's Wafer Mine.
 *
 * <p>Vanilla's mineshaft probability is evaluated after its every-chunk
 * structure-set candidate. CakeWorld retains that 0.004 probability and the
 * same seed calculation, but owns the pieces so their palette can be edible.
 * The first bounded piece starts at y=-32 and remains an underground
 * structure across every current CakeWorld Overworld biome.</p>
 */
public final class WaferMineStructureFeature
		extends JigsawFeature {
	public static final float PROBABILITY = 0.004F;
	public static final int START_Y = -32;

	public WaferMineStructureFeature() {
		super(JigsawConfiguration.CODEC, START_Y,
				true, false,
				WaferMineStructureFeature::checkLocation);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration
				.UNDERGROUND_STRUCTURES;
	}

	private static boolean checkLocation(
			PieceGeneratorSupplier.Context<
					JigsawConfiguration> context) {
		WorldgenRandom random =
				new WorldgenRandom(
						new LegacyRandomSource(0L));
		random.setLargeFeatureSeed(
				context.seed(),
				context.chunkPos().x,
				context.chunkPos().z);
		if (random.nextDouble() >= PROBABILITY) {
			return false;
		}
		return context.validBiome().test(
				context.chunkGenerator().getNoiseBiome(
						QuartPos.fromBlock(
								context.chunkPos()
										.getMiddleBlockX()),
						QuartPos.fromBlock(50),
						QuartPos.fromBlock(
								context.chunkPos()
										.getMiddleBlockZ())));
	}
}
