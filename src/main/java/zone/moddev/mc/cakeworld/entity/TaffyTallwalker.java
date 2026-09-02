package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * A long-limbed Enderman-role wanderer whose lower-difficulty attack tangles
 * and rescues rather than damages.
 */
public final class TaffyTallwalker extends EnderMan {
	private static final int TAFFY_TICKS = 200;

	public TaffyTallwalker(
			EntityType<? extends EnderMan> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		Vec3 offset = living.position().subtract(position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.15D);
		}
		living.push(horizontal.x, 0.1D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, TAFFY_TICKS,
				1, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.LEVITATION, 40,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, TAFFY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, TAFFY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, TAFFY_TICKS,
				4, false, false));
		playSound(SoundEvents.SLIME_SQUISH, 0.8F, 0.65F);
		return true;
	}
}
