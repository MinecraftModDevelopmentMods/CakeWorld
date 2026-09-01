package com.mcmoddev.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A low, placeable kitchen tool. Its block item is a reusable shapeless-recipe
 * container, so recipes can demonstrate ordinary data-driven mixing.
 */
public final class MixingBowlBlock extends Block {
	private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D,
			14.0D, 8.0D, 14.0D);

	public MixingBowlBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level,
			BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
