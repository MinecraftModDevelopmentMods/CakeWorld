package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Cancels the Elder Guardian beam and thorns damage below Hard while leaving
 * their visible targeting and timing behavior intact.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GrandGumballGuardianDamageSafety {
	private GrandGumballGuardianDamageSafety() {
	}

	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		Entity source = event.getSource().getEntity();
		if (!(source instanceof GrandGumballGuardian)) {
			source = event.getSource().getDirectEntity();
		}
		if (!(source instanceof GrandGumballGuardian)
				|| event.getEntityLiving().level.getDifficulty()
						== Difficulty.HARD) {
			return;
		}

		event.setCanceled(true);
		GrandGumballGuardian.applyGumballImpact(
				source, event.getEntityLiving());
	}
}
