package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's insomnia-spawned Phantom-role creature.
 *
 * The inherited flight, circling, swoop, size, sunlight, Cat deterrence and
 * undead contracts remain intact. Below Hard, a swoop is still an annoying
 * reason to find a bed, but its short obscuring shove is wrapped in enough
 * protection that it cannot disguise a lethal fall, fire or combat chain.
 */
public class WaferWraith extends Phantom {
	private static final int OBSCURED_TICKS = 40;
	private static final int SLOWED_TICKS = 100;
	private static final int RESCUE_TICKS = 140;

	public WaferWraith(
			EntityType<? extends Phantom> type, Level level) {
		super(type, level);
		// Vanilla's size-zero value is also its synced-data default, so a
		// freshly constructed Phantom does not run its size update callback.
		// Establish the intended six-point size-zero base explicitly.
		getAttribute(Attributes.ATTACK_DAMAGE)
				.setBaseValue(6.0D);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		// Lift in place rather than pushing toward a ledge. The inherited
		// swoop still supplies the movement and spectacle.
		living.push(0.0D, 0.12D, 0.0D);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.BLINDNESS, OBSCURED_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, SLOWED_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, RESCUE_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, RESCUE_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, RESCUE_TICKS,
				4, false, false));
		playSound(SoundEvents.PHANTOM_BITE, 0.8F, 1.35F);
		return true;
	}
}
