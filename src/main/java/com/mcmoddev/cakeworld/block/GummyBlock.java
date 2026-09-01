package com.mcmoddev.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * A translucent elastic building block. It rebounds much more strongly than
 * marshmallow, but never returns more energy than the landing supplied.
 */
public final class GummyBlock extends HalfTransparentBlock {
	public static final double BOUNCE_MULTIPLIER = 0.9D;
	public static final double MAXIMUM_BOUNCE = 1.25D;

	public GummyBlock(BlockBehaviour.Properties properties) {
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
