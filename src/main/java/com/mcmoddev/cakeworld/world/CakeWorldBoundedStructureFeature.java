package com.mcmoddev.cakeworld.world;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Opt-in contract for procedural pool elements larger than one generation
 * chunk.
 *
 * <p>Minecraft post-processes a saved structure piece once for every
 * intersecting chunk. Implementations must write only positions inside the
 * supplied generation bounds and must assign each entity or block-entity
 * anchor to the one bounds slice that contains it.</p>
 */
public interface CakeWorldBoundedStructureFeature {
	boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds);
}
