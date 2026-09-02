package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps the defender's visible swing below Hard while replacing health and
 * indirect fall/fire damage with a strong, protected jawbreaker bounce.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class JawbreakerGuardianDamageSafety {
	private static final int GUARD_TICKS = 120;

	private JawbreakerGuardianDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity()
						instanceof JawbreakerGuardian guardian)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedBounce(guardian,
				event.getEntityLiving());
	}

	public static void applyProtectedBounce(
			JawbreakerGuardian guardian,
			LivingEntity target) {
		Vec3 away = target.position()
				.subtract(guardian.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(
					0.0F, guardian.getYRot());
		}
		away = away.normalize();
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 80));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, 100));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, GUARD_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, GUARD_TICKS));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				GUARD_TICKS, 4));
		target.push(away.x * 0.85D, 0.55D,
				away.z * 0.85D);
	}
}
