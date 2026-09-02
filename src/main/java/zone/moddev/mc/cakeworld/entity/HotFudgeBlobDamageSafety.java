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
 * Converts Hot-Fudge Blob contact into a sticky, protected bounce below Hard.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class HotFudgeBlobDamageSafety {
	private HotFudgeBlobDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingHurtEvent event, Difficulty difficulty) {
		if (!(event.getSource().getEntity()
						instanceof HotFudgeBlob blob)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedBounce(blob, event.getEntityLiving());
	}

	public static void applyProtectedBounce(
			HotFudgeBlob blob, LivingEntity target) {
		Vec3 away = target.position()
				.subtract(blob.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(
					0.0F, blob.getYRot());
		}
		away = away.normalize();
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, 60, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60, 4));
		target.push(away.x * 0.35D, 0.3D,
				away.z * 0.35D);
	}
}
