package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's gentle pollinator.
 *
 * Easy and Normal replace the health-damaging sting with visible sticky
 * pollen and end the anger episode. Hard deliberately keeps the full vanilla
 * sting and poison role.
 */
public final class SugarBee extends Bee {
	public SugarBee(EntityType<? extends Bee> type, Level level) {
		super(type, level);
	}

	@Override
	public Bee getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.SUGAR_BEE.get().create(level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (target instanceof LivingEntity livingTarget) {
			livingTarget.addEffect(new MobEffectInstance(
					MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
			livingTarget.addEffect(new MobEffectInstance(
					MobEffects.GLOWING, 80));
			setTarget(null);
			stopBeingAngry();
			playSound(SoundEvents.BEE_STING, 0.7F, 1.3F);
			return true;
		}
		return false;
	}
}
