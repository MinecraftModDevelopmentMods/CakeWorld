package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Turns both Fudge Folk swords and crossbow arrows into a protected sticky
 * splat below Hard while leaving the visible attack cue intact.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FudgeFolkDamageSafety {
	private static final int RESCUE_TICKS = 120;

	private FudgeFolkDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (!(event.getSource().getEntity()
						instanceof FudgeFolk folk)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		LivingEntity target = event.getEntityLiving();
		event.setCanceled(true);
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING, 60, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		folk.playSound(SoundEvents.HONEY_BLOCK_SLIDE,
				0.8F, 0.8F + folk.getRandom()
						.nextFloat() * 0.2F);
	}
}
