package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.LollipopLorikeet;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Extends vanilla's literal minecraft:parrot shoulder sound check without
 * changing the serialized CakeWorld entity identity.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LollipopLorikeetShoulderSounds {
	private LollipopLorikeetShoulderSounds() {
	}

	@SubscribeEvent
	public static void onPlayerTick(
			TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		LollipopLorikeet.playShoulderAmbient(
				event.player,
				event.player.getShoulderEntityLeft());
		LollipopLorikeet.playShoulderAmbient(
				event.player,
				event.player.getShoulderEntityRight());
	}
}
