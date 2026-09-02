package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Replaces Macaron Clam projectile damage and dangerous levitation below Hard
 * before the vanilla bullet can apply either effect.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MacaronClamProjectileSafety {
	private static final int DUST_TICKS = 80;
	private static final int RESCUE_TICKS = 160;

	private MacaronClamProjectileSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingAttackEvent event, Difficulty difficulty) {
		if (!(event.getSource().getDirectEntity()
						instanceof ShulkerBullet)
				|| !(event.getSource().getEntity()
						instanceof MacaronClam clam)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyMacaronDust(clam, event.getEntityLiving());
	}

	public static void applyMacaronDust(
			MacaronClam clam, LivingEntity target) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				DUST_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				DUST_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4, false, false));
		target.push(0.0D, 0.1D, 0.0D);
	}
}
