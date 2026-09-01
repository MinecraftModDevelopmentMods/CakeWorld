package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.Difficulty;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Prevents leaf, crop, farmland and other gamerule-mediated possession damage
 * below Hard. Hard deliberately falls back to the world's mobGriefing rule.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GingerbreadStomperGriefSafety {
	private GingerbreadStomperGriefSafety() {
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
		if (event.getEntity() instanceof GingerbreadStomper
				&& difficulty != Difficulty.HARD) {
			event.setResult(Event.Result.DENY);
		}
	}
}
