package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Prevents Crumb Mites from hiding in possessions or waking destructive nest
 * chains below Hard. Hard deliberately follows the world's mobGriefing rule.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CrumbMiteGriefSafety {
	private CrumbMiteGriefSafety() {
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
		if (event.getEntity() instanceof CrumbMite
				&& difficulty != Difficulty.HARD) {
			event.setResult(Event.Result.DENY);
		}
	}
}
