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
 * Makes the inherited Ravager roar protective below Hard while leaving its
 * visible roar and strong knockback intact.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GingerbreadStomperDamageSafety {
	private static final int RESCUE_TICKS = 160;

	private GingerbreadStomperDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingHurtEvent event, Difficulty difficulty) {
		if (!(event.getSource().getEntity()
						instanceof GingerbreadStomper stomper)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		// At roarTick 10, Ravager.roar applies its genuine strong
		// knockback immediately after the canceled health event.
		applySafeImpact(stomper, event.getEntityLiving(),
				stomper.getRoarTick() != 10);
	}

	public static void applySafeImpact(
			GingerbreadStomper stomper,
			LivingEntity target, boolean addPush) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 80,
				0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, RESCUE_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, RESCUE_TICKS,
				0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4, false, false));
		if (!addPush) {
			return;
		}

		Vec3 away = target.position()
				.subtract(stomper.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(
					0.0F, stomper.getYRot());
		}
		away = away.normalize();
		target.push(away.x * 0.3D, 0.18D,
				away.z * 0.3D);
	}
}
