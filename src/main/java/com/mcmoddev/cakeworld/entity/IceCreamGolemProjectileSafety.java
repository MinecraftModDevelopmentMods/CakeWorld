package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps Ice-Cream Golem scoops playful below Hard while preserving the exact
 * vanilla Snowball path and Hard damage.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class IceCreamGolemProjectileSafety {
	private static final int SCOOP_TICKS = 60;

	private IceCreamGolemProjectileSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(
			LivingAttackEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level
						.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingAttackEvent event,
			Difficulty difficulty) {
		if (!(event.getSource().getDirectEntity()
						instanceof Snowball)
				|| !(event.getSource().getEntity()
						instanceof IceCreamGolem)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyHarmlessScoop(event.getEntityLiving());
	}

	public static void applyHarmlessScoop(
			LivingEntity target) {
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				SCOOP_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				SCOOP_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.WEAKNESS,
				SCOOP_TICKS, 0, false, true));
	}
}
