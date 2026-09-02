package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts Fudge Boar charges below Hard into a protected but still physical
 * Hoglin throw.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FudgeBoarDamageSafety {
	private static final int RESCUE_TICKS = 120;

	private FudgeBoarDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity()
						instanceof FudgeBoar boar)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		LivingEntity target = event.getEntityLiving();
		event.setCanceled(true);
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		HoglinBase.throwTarget(boar, target);
	}
}
