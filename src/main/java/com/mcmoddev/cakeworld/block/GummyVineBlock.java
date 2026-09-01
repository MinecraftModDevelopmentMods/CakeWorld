package com.mcmoddev.cakeworld.block;

import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * A stable, climbable strand of gummy candy. A meaningful fall into a strand
 * is converted into a small upward wobble, while sneaking keeps vanilla's
 * ordinary no-bounce behaviour.
 */
public final class GummyVineBlock extends VineBlock {
	public static final float MINIMUM_REBOUND_FALL = 2.0F;
	public static final double REBOUND_MULTIPLIER = 0.35D;
	public static final double MAXIMUM_REBOUND = 0.4D;

	public GummyVineBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void entityInside(BlockState state, Level level,
			BlockPos position, Entity entity) {
		super.entityInside(state, level, position, entity);
		Vec3 movement = entity.getDeltaMovement();
		if (!level.isClientSide
				&& !entity.isSuppressingBounce()
				&& entity.fallDistance >= MINIMUM_REBOUND_FALL
				&& movement.y < -0.08D) {
			double rebound = Math.min(
					-movement.y * REBOUND_MULTIPLIER,
					MAXIMUM_REBOUND);
			entity.setDeltaMovement(
					movement.x, rebound, movement.z);
			entity.fallDistance = 0.0F;
		}
	}

	@Override
	public boolean canSurvive(BlockState state,
			LevelReader level, BlockPos position) {
		BlockState above = level.getBlockState(
				position.above());
		return above.is(this)
				|| above.is(CakeWorldBlocks.GUMMY_BLOCK.get())
				|| above.is(CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get())
				|| above.is(CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get())
				|| above.is(CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get())
				|| super.canSurvive(state, level, position);
	}
}
