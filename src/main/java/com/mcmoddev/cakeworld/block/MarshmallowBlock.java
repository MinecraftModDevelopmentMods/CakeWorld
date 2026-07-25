package com.mcmoddev.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * A rescue surface that always cancels fall damage and turns downward speed
 * into a deliberately small, capped rebound.
 */
public final class MarshmallowBlock extends Block {
	public static final double BOUNCE_MULTIPLIER = 0.45D;
	public static final double MAXIMUM_BOUNCE = 0.8D;

	public MarshmallowBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos,
			Entity entity, float distance) {
		entity.causeFallDamage(distance, 0.0F, DamageSource.FALL);
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(level, entity);
			return;
		}
		Vec3 movement = entity.getDeltaMovement();
		if (movement.y < 0.0D) {
			double rebound = Math.min(-movement.y * BOUNCE_MULTIPLIER,
					MAXIMUM_BOUNCE);
			entity.setDeltaMovement(movement.x, rebound, movement.z);
		}
	}
}
