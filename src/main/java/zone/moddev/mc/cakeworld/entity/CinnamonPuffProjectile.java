package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A non-damaging lower-difficulty Cinnamon Spark projectile.
 *
 * Its short rescue effects cover the fall and fire risks introduced by the
 * small displacement, so the attack cannot disguise environmental damage.
 */
public final class CinnamonPuffProjectile extends ThrowableItemProjectile {
	public CinnamonPuffProjectile(
			EntityType<? extends CinnamonPuffProjectile> type, Level level) {
		super(type, level);
	}

	public CinnamonPuffProjectile(Level level, LivingEntity owner) {
		super(CakeWorldEntities.CINNAMON_PUFF.get(), owner, level);
	}

	@Override
	protected Item getDefaultItem() {
		return Items.COCOA_BEANS;
	}

	@Override
	protected void onHitEntity(EntityHitResult hit) {
		super.onHitEntity(hit);
		if (!level.isClientSide
				&& hit.getEntity() instanceof LivingEntity target) {
			applyHarmlessPuff(target);
		}
	}

	@Override
	protected void onHit(HitResult hit) {
		super.onHit(hit);
		if (!level.isClientSide) {
			discard();
		}
	}

	public void applyHarmlessPuff(LivingEntity target) {
		Vec3 away = target.position().subtract(position());
		double horizontal = Math.max(0.001D,
				Math.sqrt(away.x * away.x + away.z * away.z));
		target.push(away.x / horizontal * 0.35D, 0.2D,
				away.z / horizontal * 0.35D);
		target.fallDistance = 0.0F;
		target.clearFire();
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 120));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 120));
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 40));
	}
}
