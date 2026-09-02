package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps the inherited raid door-breaking role on Hard while protecting
 * possessions on the family-friendly difficulties.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RollingPinRaiderGriefSafety {
	private RollingPinRaiderGriefSafety() {
	}

	@SubscribeEvent
	public static void onMobGriefing(
			EntityMobGriefingEvent event) {
		applyForDifficulty(event,
				event.getEntity().level.getDifficulty());
	}

	public static void applyForDifficulty(
			EntityMobGriefingEvent event,
			Difficulty difficulty) {
		if (event.getEntity()
				instanceof RollingPinRaider
				&& difficulty != Difficulty.HARD) {
			event.setResult(Event.Result.DENY);
		}
	}
}
