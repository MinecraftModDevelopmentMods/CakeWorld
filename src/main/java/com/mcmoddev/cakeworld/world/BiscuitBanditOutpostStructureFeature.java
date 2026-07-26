package com.mcmoddev.cakeworld.world;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

/**
 * Vanilla Pillager-Outpost placement with CakeWorld villages included in the
 * ten-chunk exclusion rule.
 */
public final class BiscuitBanditOutpostStructureFeature
		extends JigsawFeature {
	public BiscuitBanditOutpostStructureFeature() {
		super(JigsawConfiguration.CODEC, 0, true, true,
				BiscuitBanditOutpostStructureFeature
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
		ChunkPos chunk = context.chunkPos();
		int regionX = chunk.x >> 4;
		int regionZ = chunk.z >> 4;
		WorldgenRandom random =
				new WorldgenRandom(
						new LegacyRandomSource(0L));
		random.setSeed((long)(regionX
				^ regionZ << 4) ^ context.seed());
		random.nextInt();
		if (random.nextInt(5) != 0) {
			return false;
		}
		if (context.chunkGenerator()
				.hasFeatureChunkInRange(
						BuiltinStructureSets.VILLAGES,
						context.seed(),
						chunk.x, chunk.z, 10)) {
			return false;
		}
		return GingerbreadVillageFeature.structureSet()
				== null
				|| !context.chunkGenerator()
						.hasFeatureChunkInRange(
								GingerbreadVillageFeature
										.STRUCTURE_SET_KEY,
								context.seed(),
								chunk.x, chunk.z, 10);
	}
}
