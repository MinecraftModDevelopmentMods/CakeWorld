package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Keeps the visible carried-block role while preventing block taking and
 * placement below Hard difficulty.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TaffyTallwalkerGriefSafety {
	private TaffyTallwalkerGriefSafety() {
	}

	@SubscribeEvent
	public static void onMobGriefing(EntityMobGriefingEvent event) {
		if (event.getEntity() instanceof TaffyTallwalker
				&& event.getEntity().level.getDifficulty()
						!= Difficulty.HARD) {
			event.setResult(Event.Result.DENY);
		}
	}
}
