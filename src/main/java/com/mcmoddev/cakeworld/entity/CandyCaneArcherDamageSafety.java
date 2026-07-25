package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Cancels Candy-Cane Archer arrow damage below Hard before the ordinary Arrow
 * hit can hurt the target. Hard remains the exact inherited Skeleton attack.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CandyCaneArcherDamageSafety {
	private static final int STICKY_TICKS = 80;
	private static final int RESCUE_TICKS = 140;

	private CandyCaneArcherDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingAttackEvent event, Difficulty difficulty) {
		if (!(event.getSource().getDirectEntity()
						instanceof AbstractArrow arrow)
				|| !(arrow.getOwner()
						instanceof CandyCaneArcher archer)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyStickyShot(archer,
				event.getEntityLiving());
	}

	public static void applyStickyShot(
			CandyCaneArcher archer,
			LivingEntity target) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				STICKY_TICKS, 1, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				STICKY_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4, false, false));
		double x = target.getX() - archer.getX();
		double z = target.getZ() - archer.getZ();
		double length = Math.max(1.0D,
				Math.sqrt(x * x + z * z));
		target.push(x / length * 0.16D,
				0.10D, z / length * 0.16D);
	}
}
