package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.Level;

/**
 * Burnt-Toffee Foundry's elite Piglin Brute role.
 *
 * <p>The genuine Piglin Brute superclass retains its 50-health body, Golden
 * Axe equipment, home memory, always-hostile targeting, anger, melee,
 * alliance, pickup, sound and zombification contracts. CakeWorld only repairs
 * the vanilla idle behaviours which compare literal Piglin entity types.</p>
 */
public class FudgeBrute extends PiglinBrute {
	public FudgeBrute(
			EntityType<? extends PiglinBrute> type, Level level) {
		super(type, level);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		repairCakeWorldFamilyInteraction();
	}

	protected void repairCakeWorldFamilyInteraction() {
		PiglinFamilyInteraction.repair(this);
	}
}
