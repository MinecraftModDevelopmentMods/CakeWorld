package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Cancels Frosted Archer arrow damage below Hard before the ordinary Arrow
 * hit can hurt the target. Hard remains the exact inherited Stray attack.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FrostedArcherDamageSafety {
	private static final int CHILLED_TICKS = 100;
	private static final int RESCUE_TICKS = 140;

	private FrostedArcherDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event) {
		applyForDifficulty(event,
				event.getEntityLiving().level.getDifficulty());
	}

	public static void applyForDifficulty(
			LivingAttackEvent event, Difficulty difficulty) {
		if (!(event.getSource().getDirectEntity()
						instanceof AbstractArrow arrow)
				|| !(arrow.getOwner()
						instanceof FrostedArcher archer)
				|| difficulty == Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyChilledShot(archer,
				event.getEntityLiving());
	}

	public static void applyChilledShot(
			FrostedArcher archer,
			LivingEntity target) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				CHILLED_TICKS, 1, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				CHILLED_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4, false, false));
		double x = target.getX() - archer.getX();
		double z = target.getZ() - archer.getZ();
		double length = Math.max(1.0D,
				Math.sqrt(x * x + z * z));
		target.push(x / length * 0.12D,
				0.08D, z / length * 0.12D);
	}
}
