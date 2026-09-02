package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps the inherited fangs and summoned Vexes visible and inconvenient on
 * family difficulties without allowing them to cause indirect health damage.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SourSorcererDamageSafety {
	private SourSorcererDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		if (event.getEntityLiving().level.getDifficulty()
				== Difficulty.HARD) {
			return;
		}

		Entity impact = protectedImpact(event.getSource());
		if (impact == null) {
			return;
		}

		event.setCanceled(true);
		SourSorcerer.applySourSurprise(
				impact, event.getEntityLiving());
	}

	private static Entity protectedImpact(DamageSource source) {
		Entity direct = source.getDirectEntity();
		Entity attacker = source.getEntity();
		if (attacker instanceof SourSorcerer) {
			return direct == null ? attacker : direct;
		}
		if (direct instanceof EvokerFangs fangs
				&& fangs.getOwner() instanceof SourSorcerer) {
			return direct;
		}
		if (attacker instanceof Vex vex
				&& vex.getOwner() instanceof SourSorcerer) {
			return attacker;
		}
		return null;
	}
}
