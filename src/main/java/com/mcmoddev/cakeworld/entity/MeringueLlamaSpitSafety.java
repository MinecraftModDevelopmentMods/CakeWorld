package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Turns Meringue Llama spit into a sticky, protected nudge below Hard.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MeringueLlamaSpitSafety {
	private MeringueLlamaSpitSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (!(event.getSource().getDirectEntity()
						instanceof LlamaSpit)
				|| !(event.getSource().getEntity()
						instanceof MeringueLlama llama)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyMeringueSplat(
				llama, event.getEntityLiving());
	}

	public static void applyMeringueSplat(
			MeringueLlama llama, LivingEntity target) {
		Vec3 away = target.position()
				.subtract(llama.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(
					0.0F, llama.getYRot());
		}
		away = away.normalize();
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, 60, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60, 4));
		target.push(away.x * 0.25D, 0.25D,
				away.z * 0.25D);
	}
}
