package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Repairs Zombie's literal {@code EntityType.ZOMBIE} reinforcement factory.
 *
 * <p>The vanilla probability and short-circuit order are retained exactly.
 * The event result supplies the custom type only after that same roll passes.
 * An explicit custom aid from another event listener wins.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class StaleCrumblerReinforcements {
	private StaleCrumblerReinforcements() {
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onSummonAid(
			ZombieEvent.SummonAidEvent event) {
		if (!(event.getSummoner()
						instanceof StaleCrumbler)
				&& !(event.getSummoner()
						instanceof CrumbledGingerbreadFolk)
				|| event.getResult()
						!= Event.Result.DEFAULT) {
			return;
		}

		boolean shouldSummon =
				event.getAttacker() != null
						&& event.getWorld()
								.getDifficulty()
								== Difficulty.HARD
						&& event.getSummoner()
								.getRandom()
								.nextFloat()
								< event.getSummonChance()
						&& event.getWorld()
								.getGameRules()
								.getBoolean(
										GameRules
											.RULE_DOMOBSPAWNING);
		if (!shouldSummon) {
			event.setResult(Event.Result.DENY);
			return;
		}

		Zombie aid = event.getCustomSummonedAid();
		if (aid == null) {
			aid = CakeWorldEntities
					.STALE_CRUMBLER.get()
					.create(event.getWorld());
		}
		if (aid == null) {
			event.setResult(Event.Result.DENY);
			return;
		}

		event.setCustomSummonedAid(aid);
		event.setResult(Event.Result.ALLOW);
	}
}
