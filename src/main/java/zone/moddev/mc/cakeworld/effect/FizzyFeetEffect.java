package zone.moddev.mc.cakeworld.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Softens descent without ever forcing an upward launch, keeping the playful
 * movement legible and camera-friendly.
 */
public final class FizzyFeetEffect extends MobEffect {
	public static final double DESCENT_MULTIPLIER = 0.6D;

	public FizzyFeetEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xF4D35E);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		Vec3 movement = entity.getDeltaMovement();
		if (movement.y < 0.0D) {
			entity.setDeltaMovement(movement.x,
					movement.y * DESCENT_MULTIPLIER, movement.z);
		}
	}
}
