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
 * Retains the visible Goat ram below Hard while replacing health and indirect
 * fall/fire damage with a cushioned nougat bounce.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class NougatGoatDamageSafety {
	private NougatGoatDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity() instanceof NougatGoat goat)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyNougatBounce(goat, event.getEntityLiving());
	}

	public static void applyNougatBounce(NougatGoat goat,
			LivingEntity target) {
		Vec3 away = target.position().subtract(goat.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(0.0F, goat.getYRot());
		}
		away = away.normalize();
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60, 4));
		target.push(away.x * 0.35D, 0.45D, away.z * 0.35D);
	}
}
