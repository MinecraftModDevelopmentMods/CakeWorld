package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps CakeWorld's Zombie family contact and world interaction safe below
 * Hard.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class StaleCrumblerSafety {
	private static final int INCONVENIENCE_TICKS = 80;
	private static final int RESCUE_TICKS = 160;

	private StaleCrumblerSafety() {
	}

	/**
	 * Defense in depth for damage introduced around the inherited attack by
	 * another mod. CakeWorld's own lower-difficulty attack does not call
	 * {@code hurt}, so this does not double-apply during ordinary contact.
	 */
	@SubscribeEvent
	public static void onLivingAttack(
			LivingAttackEvent event) {
		if (!(event.getSource().getEntity()
						instanceof Zombie crumbler)
				|| !isProtectedZombie(crumbler)
				|| event.getEntityLiving().level
						.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedContact(crumbler,
				event.getEntityLiving());
	}

	@SubscribeEvent
	public static void onLivingHurt(
			LivingHurtEvent event) {
		if (!(event.getSource().getEntity()
						instanceof Zombie crumbler)
				|| !isProtectedZombie(crumbler)
				|| event.getEntityLiving().level
						.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		applyProtectedContact(crumbler,
				event.getEntityLiving());
	}

	/**
	 * Turtle eggs and dropped possessions remain safe below Hard. Hard follows
	 * the world's ordinary mobGriefing rule and Zombie door rules.
	 */
	@SubscribeEvent
	public static void onMobGriefing(
			EntityMobGriefingEvent event) {
		applyGriefPolicy(event,
				event.getEntity().level
						.getDifficulty());
	}

	public static void applyGriefPolicy(
			EntityMobGriefingEvent event,
			Difficulty difficulty) {
		if (event.getEntity() instanceof Zombie zombie
				&& isProtectedZombie(zombie)
				&& difficulty != Difficulty.HARD) {
			event.setResult(Event.Result.DENY);
		}
	}

	public static void applyProtectedContact(
			Zombie crumbler,
			LivingEntity target) {
		Vec3 away = target.position()
				.subtract(crumbler.position());
		Vec3 horizontal = new Vec3(
				away.x, 0.0D, away.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize()
					.scale(0.2D);
		}
		target.push(horizontal.x, 0.12D,
				horizontal.z);
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				INCONVENIENCE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				INCONVENIENCE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		crumbler.playSound(
				SoundEvents.SLIME_SQUISH,
				0.8F,
				0.8F + crumbler.getRandom()
						.nextFloat() * 0.2F);
	}

	private static boolean isProtectedZombie(
			Zombie zombie) {
		return zombie instanceof StaleCrumbler
				|| zombie instanceof StaleFudgeFolk
				|| zombie
						instanceof CrumbledGingerbreadFolk;
	}
}
