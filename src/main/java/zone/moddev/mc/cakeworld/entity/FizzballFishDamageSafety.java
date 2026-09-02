package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Cancels a Fizzball sting before vanilla can add poison below Hard, replacing
 * it with a visible, landing-safe fizzy shove. Hard keeps the genuine
 * Pufferfish health damage and state-scaled poison.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FizzballFishDamageSafety {
	private FizzballFishDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingAttackEvent event, Difficulty difficulty) {
		if (!(event.getSource().getEntity()
						instanceof FizzballFish fish)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyFizzyShove(fish, event.getEntityLiving());
	}

	public static void applyFizzyShove(FizzballFish fish,
			LivingEntity target) {
		Vec3 away = target.position().subtract(fish.position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(0.0F, fish.getYRot());
		}
		away = away.normalize();
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SPEED, 60, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 120, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60, 4));
		target.push(away.x * 0.35D, 0.3D, away.z * 0.35D);
		fish.playSound(SoundEvents.PUFFER_FISH_STING,
				0.8F, 1.1F + fish.getRandom().nextFloat() * 0.2F);
	}
}
