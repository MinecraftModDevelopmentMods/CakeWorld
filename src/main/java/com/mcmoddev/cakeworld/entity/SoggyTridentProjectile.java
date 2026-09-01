package com.mcmoddev.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Visible Easy/Normal trident substitute which splashes instead of damaging.
 */
public final class SoggyTridentProjectile extends ThrownTrident {
	public SoggyTridentProjectile(
			EntityType<? extends ThrownTrident> type, Level level) {
		super(type, level);
	}

	public SoggyTridentProjectile(
			EntityType<? extends ThrownTrident> type, Level level,
			LivingEntity owner) {
		this(type, level);
		setPos(owner.getX(), owner.getEyeY() - 0.1D, owner.getZ());
		setOwner(owner);
	}

	@Override
	protected void onHitEntity(EntityHitResult hit) {
		splash(hit.getEntity());
		setDeltaMovement(getDeltaMovement()
				.multiply(-0.01D, -0.1D, -0.01D));
		discard();
	}

	public boolean splash(Entity target) {
		boolean applied = SoggyBiscuit.applySoggySplash(this, target);
		playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
		return applied;
	}
}
