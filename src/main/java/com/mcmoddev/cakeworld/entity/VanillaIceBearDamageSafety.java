package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Preserves the telegraphed Polar Bear warning swipe below Hard while replacing
 * health and indirect fall/fire damage with a cushioned vanilla-cream shove.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VanillaIceBearDamageSafety {
	private VanillaIceBearDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingHurtEvent event, Difficulty difficulty) {
		if (!(event.getSource().getEntity()
						instanceof VanillaIceBear bear)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyVanillaCreamShove(bear, event.getEntityLiving());
	}

	public static void applyVanillaCreamShove(VanillaIceBear bear,
			LivingEntity target) {
		Vec3 away = target.position().subtract(bear.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(0.0F, bear.getYRot());
		}
		away = away.normalize();
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60, 4));
		target.push(away.x * 0.25D, 0.25D, away.z * 0.25D);
		bear.playSound(SoundEvents.SNOW_BREAK,
				0.8F, 0.9F + bear.getRandom().nextFloat() * 0.2F);
	}
}
