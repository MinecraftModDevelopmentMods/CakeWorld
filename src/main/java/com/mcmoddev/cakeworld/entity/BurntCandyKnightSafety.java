package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Turns Burnt-Candy Knight attacks into a sticky warning below Hard.
 *
 * <p>Cancellation at LivingAttack is essential: Wither Skeleton applies its
 * Wither effect only after {@code hurt} reports success, so stopping the
 * attack here prevents both health loss and the hidden follow-on effect.
 * Hard is never intercepted.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BurntCandyKnightSafety {
	private static final int INCONVENIENCE_TICKS = 80;
	private static final int RESCUE_TICKS = 160;

	private BurntCandyKnightSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(
			LivingAttackEvent event) {
		applyAttackPolicy(event,
				event.getEntityLiving().level
						.getDifficulty());
	}

	public static void applyAttackPolicy(
			LivingAttackEvent event,
			Difficulty difficulty) {
		BurntCandyKnight knight =
				owningKnight(
						event.getSource().getEntity(),
						event.getSource()
								.getDirectEntity());
		if (knight == null
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedSmear(knight,
				event.getEntityLiving());
	}

	/**
	 * Defense in depth for damage injected after LivingAttack by another mod.
	 */
	@SubscribeEvent
	public static void onLivingHurt(
			LivingHurtEvent event) {
		applyDamagePolicy(event,
				event.getEntityLiving().level
						.getDifficulty());
	}

	public static void applyDamagePolicy(
			LivingHurtEvent event,
			Difficulty difficulty) {
		BurntCandyKnight knight =
				owningKnight(
						event.getSource().getEntity(),
						event.getSource()
								.getDirectEntity());
		if (knight == null
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedSmear(knight,
				event.getEntityLiving());
	}

	public static boolean isOwnedByKnight(
			Entity source, Entity directSource) {
		return owningKnight(source,
				directSource) != null;
	}

	private static BurntCandyKnight owningKnight(
			Entity source, Entity directSource) {
		if (source instanceof BurntCandyKnight knight) {
			return knight;
		}
		if (directSource
					instanceof AbstractArrow arrow
				&& arrow.getOwner()
						instanceof BurntCandyKnight knight) {
			return knight;
		}
		if (source instanceof AbstractArrow arrow
				&& arrow.getOwner()
						instanceof BurntCandyKnight knight) {
			return knight;
		}
		return null;
	}

	private static void applyProtectedSmear(
			BurntCandyKnight knight,
			LivingEntity target) {
		Vec3 away = target.position()
				.subtract(knight.position());
		Vec3 horizontal = new Vec3(
				away.x, 0.0D, away.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize()
					.scale(0.28D);
		}
		target.push(horizontal.x, 0.16D,
				horizontal.z);
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
		knight.playSound(
				SoundEvents.WITHER_SKELETON_STEP,
				0.7F,
				1.25F + knight.getRandom()
						.nextFloat() * 0.2F);
	}
}
