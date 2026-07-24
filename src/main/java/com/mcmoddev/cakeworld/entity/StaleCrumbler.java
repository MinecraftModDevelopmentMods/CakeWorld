package com.mcmoddev.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

/**
 * A Zombie-role creature whose lower-difficulty attack is sticky, not harmful.
 */
public final class StaleCrumbler extends Zombie {
	private static final int STICKY_TICKS = 50;

	public StaleCrumbler(EntityType<? extends Zombie> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
				STICKY_TICKS, 0, false, true));
		living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
				STICKY_TICKS, 0, false, false));
		double x = living.getX() - getX();
		double z = living.getZ() - getZ();
		living.push(x * 0.08D, 0.12D, z * 0.08D);
		playSound(SoundEvents.SLIME_SQUISH, 0.8F, 0.8F + random.nextFloat() * 0.2F);
		return true;
	}
}
