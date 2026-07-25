package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * A visible, non-damaging boiled-sweet mirage used below Hard difficulty.
 */
public final class MirageSweetProjectile
		extends ThrowableItemProjectile {
	private static final int MIRAGE_TICKS = 100;
	private static final int RESCUE_TICKS = 120;

	public MirageSweetProjectile(
			EntityType<? extends MirageSweetProjectile> type,
			Level level) {
		super(type, level);
	}

	public MirageSweetProjectile(
			Level level, LivingEntity owner) {
		super(CakeWorldEntities.MIRAGE_SWEET.get(),
				owner, level);
	}

	@Override
	protected Item getDefaultItem() {
		return CakeWorldItems.BOILED_SWEET.get();
	}

	@Override
	protected void onHitEntity(EntityHitResult hit) {
		super.onHitEntity(hit);
		if (!level.isClientSide
				&& hit.getEntity() instanceof LivingEntity target) {
			applyHarmlessMirage(target);
		}
	}

	@Override
	protected void onHit(HitResult hit) {
		super.onHit(hit);
		if (!level.isClientSide) {
			discard();
		}
	}

	public void applyHarmlessMirage(LivingEntity target) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.BLINDNESS, MIRAGE_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.CONFUSION, MIRAGE_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, MIRAGE_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, RESCUE_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, RESCUE_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
	}
}
