package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps ordinary Gumball Guardian beam and thorns feedback visible while
 * canceling their health damage below Hard.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GumballGuardianDamageSafety {
	private GumballGuardianDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		Entity source = event.getSource().getEntity();
		if (!(source instanceof GumballGuardian)) {
			source = event.getSource().getDirectEntity();
		}
		if (!(source instanceof GumballGuardian)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		GrandGumballGuardian.applyGumballImpact(
				source, event.getEntityLiving());
	}
}
