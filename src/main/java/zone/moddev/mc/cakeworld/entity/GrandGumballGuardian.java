package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's persistent Elder Guardian role and future Soda Palace boss.
 *
 * <p>The inherited beam, thorns and mining-fatigue pulse are kept intact.
 * {@link GrandGumballGuardianDamageSafety} changes only the health-damage
 * result of attacks below Hard difficulty.</p>
 */
public final class GrandGumballGuardian extends ElderGuardian {
	private static final int STICKY_TICKS = 160;

	public GrandGumballGuardian(
			EntityType<? extends ElderGuardian> type, Level level) {
		super(type, level);
	}

	public static boolean applyGumballImpact(
			Entity source, LivingEntity target) {
		Vec3 offset = target.position().subtract(source.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.3D);
		}
		target.push(horizontal.x, 0.12D, horizontal.z);
		target.fallDistance = 0.0F;
		target.clearFire();
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, STICKY_TICKS,
				1, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.DIG_SLOWDOWN, STICKY_TICKS,
				1, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, STICKY_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, STICKY_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, STICKY_TICKS,
				4, false, false));
		source.playSound(SoundEvents.SLIME_SQUISH, 0.9F, 0.75F);
		return true;
	}
}
