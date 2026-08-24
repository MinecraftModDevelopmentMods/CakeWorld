package com.mcmoddev.cakeworld.block;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;

/**
 * A very light, edible cloud surface for safe movement between End islands.
 * It keeps the visible nibble contract from Meringue Foam but returns a higher,
 * still bounded rebound and supplies a short jump-and-glide travel window.
 */
public final class CandyflossCloudBlock extends MeringueFoamBlock {
	public static final double BOUNCE_MULTIPLIER = 0.65D;
	public static final double MAXIMUM_BOUNCE = 1.0D;
	public static final int SLOW_FALLING_TICKS = 240;
	public static final int JUMP_BOOST_TICKS = 120;

	public CandyflossCloudBlock(BlockBehaviour.Properties properties) {
		super(properties);
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

	@Override
	protected void applyStepEffects(LivingEntity living) {
		living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
				SLOW_FALLING_TICKS, 0, true, false, true));
		living.addEffect(new MobEffectInstance(MobEffects.JUMP,
				JUMP_BOOST_TICKS, 0, true, false, true));
	}
}
