package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's raid and mansion caster, retaining the complete Evoker spell
 * repertoire while making its attacks playful below Hard.
 */
public final class SourSorcerer extends Evoker {
	private static final int SOUR_TICKS = 100;

	public SourSorcerer(
			EntityType<? extends Evoker> type, Level level) {
		super(type, level);
	}

	public static void applySourSurprise(
			Entity source, LivingEntity target) {
		Vec3 offset = target.position().subtract(source.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.18D);
		}
		target.push(horizontal.x, 0.22D, horizontal.z);
		target.fallDistance = 0.0F;
		target.clearFire();
		target.addEffect(new MobEffectInstance(
				MobEffects.CONFUSION, SOUR_TICKS,
				0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, SOUR_TICKS,
				0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, SOUR_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, SOUR_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, SOUR_TICKS,
				4, false, false));
		target.level.playSound(null, target.blockPosition(),
				SoundEvents.EVOKER_FANGS_ATTACK,
				target.getSoundSource(), 0.65F, 1.35F);
	}
}
