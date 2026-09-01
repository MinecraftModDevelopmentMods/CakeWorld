package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Makes the Burnt-Sugar Tempest inconvenient but non-destructive below Hard.
 *
 * <p>All three seams matter: cancelling living damage prevents direct,
 * projectile and blast health loss (and therefore Wither effects); cancelling
 * explosions protects blocks, items and possessions; denying mob griefing
 * protects the boss's separate powered-phase block-break path. Hard is never
 * intercepted.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class BurntSugarTempestSafety {
	private static final int INCONVENIENCE_TICKS = 80;
	private static final int RESCUE_TICKS = 160;

	private BurntSugarTempestSafety() {
	}

	/**
	 * Cancels before {@code LivingEntity.hurt} returns. This early seam is
	 * essential for Wither Skull: cancelling only LivingHurt prevents health
	 * loss, but vanilla would still see a successful hit and apply Wither.
	 */
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
		BurntSugarTempest tempest =
				owningTempest(
						event.getSource().getEntity(),
						event.getSource()
								.getDirectEntity());
		if (tempest == null
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedGust(tempest,
				event.getEntityLiving());
	}

	/**
	 * Defense in depth for damage injected after LivingAttack by another mod.
	 * Ordinary Tempest and skull hits are already stopped at LivingAttack.
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
		BurntSugarTempest tempest =
				owningTempest(
						event.getSource().getEntity(),
						event.getSource()
								.getDirectEntity());
		if (tempest == null
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedGust(tempest,
				event.getEntityLiving());
	}

	@SubscribeEvent
	public static void onExplosionStart(
			ExplosionEvent.Start event) {
		applyExplosionPolicy(event,
				event.getWorld().getDifficulty());
	}

	public static void applyExplosionPolicy(
			ExplosionEvent.Start event,
			Difficulty difficulty) {
		Explosion explosion = event.getExplosion();
		Entity exploder = explosion.getExploder();
		if (difficulty != Difficulty.HARD
				&& owningTempest(exploder,
						exploder) != null) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onMobGriefing(
			EntityMobGriefingEvent event) {
		applyGriefingPolicy(event,
				event.getEntity().level
						.getDifficulty());
	}

	public static void applyGriefingPolicy(
			EntityMobGriefingEvent event,
			Difficulty difficulty) {
		if (difficulty != Difficulty.HARD
				&& owningTempest(
						event.getEntity(),
						event.getEntity()) != null) {
			event.setResult(Event.Result.DENY);
		}
	}

	public static boolean isOwnedByTempest(
			Entity source, Entity directSource) {
		return owningTempest(source,
				directSource) != null;
	}

	private static BurntSugarTempest owningTempest(
			Entity source, Entity directSource) {
		if (source instanceof BurntSugarTempest tempest) {
			return tempest;
		}
		if (directSource
					instanceof WitherSkull skull
				&& skull.getOwner()
						instanceof BurntSugarTempest tempest) {
			return tempest;
		}
		if (source instanceof WitherSkull skull
				&& skull.getOwner()
						instanceof BurntSugarTempest tempest) {
			return tempest;
		}
		return null;
	}

	private static void applyProtectedGust(
			WitherBoss tempest,
			LivingEntity target) {
		Vec3 away = target.position()
				.subtract(tempest.position());
		Vec3 horizontal = new Vec3(
				away.x, 0.0D, away.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize()
					.scale(0.45D);
		}
		target.push(horizontal.x, 0.25D,
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
		tempest.playSound(
				SoundEvents.WITHER_SHOOT,
				0.7F,
				1.35F + tempest.getRandom()
						.nextFloat() * 0.2F);
	}
}
