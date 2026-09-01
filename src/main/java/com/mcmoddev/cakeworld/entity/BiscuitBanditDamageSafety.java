package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Turns Biscuit Bandit hits into a protected obscuring crumb puff below Hard.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BiscuitBanditDamageSafety {
	private static final int RESCUE_TICKS = 120;

	private BiscuitBanditDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity()
						instanceof BiscuitBandit bandit)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		LivingEntity target = event.getEntityLiving();
		event.setCanceled(true);
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.BLINDNESS, 40, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, 60, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		bandit.playSound(SoundEvents.GRAVEL_HIT,
				0.8F, 1.25F + bandit.getRandom()
						.nextFloat() * 0.2F);
	}
}
