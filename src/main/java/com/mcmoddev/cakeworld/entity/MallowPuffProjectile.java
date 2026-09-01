package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A visible, reflectable Ghast-sized projectile which bursts into cushioning
 * marshmallow instead of invoking Minecraft's explosion system.
 */
public final class MallowPuffProjectile extends LargeFireball {
	private static final double SPLASH_RADIUS = 2.5D;
	private static final int CUSHION_TICKS = 120;

	public MallowPuffProjectile(
			EntityType<? extends LargeFireball> type, Level level) {
		super(type, level);
	}

	public MallowPuffProjectile(Level level, LivingEntity owner,
			double xPower, double yPower, double zPower) {
		this(CakeWorldEntities.MALLOW_PUFF.get(), level);
		setOwner(owner);
		setPos(owner.getX(), owner.getY(0.5D) + 0.5D,
				owner.getZ());
		double length = Math.sqrt(xPower * xPower
				+ yPower * yPower + zPower * zPower);
		if (length != 0.0D) {
			this.xPower = xPower / length * 0.1D;
			this.yPower = yPower / length * 0.1D;
			this.zPower = zPower / length * 0.1D;
		}
	}

	@Override
	protected boolean shouldBurn() {
		return false;
	}

	@Override
	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.CLOUD;
	}

	@Override
	protected void onHit(HitResult hit) {
		if (!level.isClientSide) {
			burst();
		}
	}

	public void burst() {
		if (level.isClientSide || isRemoved()) {
			return;
		}
		Entity owner = getOwner();
		for (LivingEntity target : level.getEntitiesOfClass(
				LivingEntity.class,
				getBoundingBox().inflate(SPLASH_RADIUS),
				entity -> entity != owner)) {
			applyMallowCushion(this, target);
		}
		if (level instanceof ServerLevel server) {
			server.sendParticles(ParticleTypes.CLOUD,
					getX(), getY(), getZ(),
					24, 0.8D, 0.8D, 0.8D, 0.04D);
		}
		level.playSound(null, blockPosition(),
				SoundEvents.SNOWBALL_THROW,
				SoundSource.HOSTILE, 1.0F, 0.65F);
		discard();
	}

	public static void applyMallowCushion(
			Entity source, LivingEntity target) {
		Vec3 offset = target.position().subtract(source.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.16D);
		}
		target.push(horizontal.x, 0.3D, horizontal.z);
		target.fallDistance = 0.0F;
		target.clearFire();
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, CUSHION_TICKS,
				0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, CUSHION_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, CUSHION_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, CUSHION_TICKS,
				4, false, false));
	}
}
