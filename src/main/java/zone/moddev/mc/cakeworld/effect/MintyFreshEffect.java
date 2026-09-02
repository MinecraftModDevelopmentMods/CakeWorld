package zone.moddev.mc.cakeworld.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * A cooling effect that extinguishes the affected entity while it lasts.
 */
public final class MintyFreshEffect extends MobEffect {
	public MintyFreshEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x9BE8D8);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		entity.clearFire();
	}
}
