package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Makes Stale Fudge Boar contact harmless below Hard while retaining the
 * readable adult throw and baby bump.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class StaleFudgeBoarDamageSafety {
	private static final int INCONVENIENCE_TICKS = 80;
	private static final int RESCUE_TICKS = 160;

	private StaleFudgeBoarDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(
			LivingAttackEvent event) {
		if (!(event.getSource().getEntity()
						instanceof StaleFudgeBoar boar)
				|| event.getEntityLiving().level
						.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		protectAndThrow(boar,
				event.getEntityLiving());
	}

	@SubscribeEvent
	public static void onLivingHurt(
			LivingHurtEvent event) {
		if (!(event.getSource().getEntity()
						instanceof StaleFudgeBoar boar)
				|| event.getEntityLiving().level
						.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		protectAndThrow(boar,
				event.getEntityLiving());
	}

	public static void protectAndThrow(
			StaleFudgeBoar boar,
			LivingEntity target) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				INCONVENIENCE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				INCONVENIENCE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		if (!boar.isBaby()) {
			HoglinBase.throwTarget(boar, target);
		}
	}
}
