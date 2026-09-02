package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.Level;

/**
 * A mineshaft-role ambusher whose lower-difficulty bite is sticky, not toxic.
 */
public final class DeepLiquoriceWeaver extends CaveSpider {
	private static final int STICKY_TICKS = 100;

	public DeepLiquoriceWeaver(EntityType<? extends CaveSpider> type,
			Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity livingTarget)) {
			return false;
		}
		livingTarget.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, STICKY_TICKS, 1));
		livingTarget.addEffect(new MobEffectInstance(
				MobEffects.DIG_SLOWDOWN, STICKY_TICKS));
		playSound(SoundEvents.SLIME_SQUISH, 0.8F,
				0.75F + random.nextFloat() * 0.15F);
		return true;
	}
}
