package com.mcmoddev.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Soft, slightly yielding shore sediment for Custard Coast.
 *
 * <p>The block never bounces or traps an entity. It simply halves ordinary
 * fall damage and uses its registered speed factor to make a pudding beach
 * feel softer underfoot.</p>
 */
public final class CustardPuddingBlock extends Block {
	public static final float FALL_DAMAGE_MULTIPLIER = 0.5F;

	public CustardPuddingBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos,
			Entity entity, float distance) {
		entity.causeFallDamage(distance,
				FALL_DAMAGE_MULTIPLIER, DamageSource.FALL);
	}
}
