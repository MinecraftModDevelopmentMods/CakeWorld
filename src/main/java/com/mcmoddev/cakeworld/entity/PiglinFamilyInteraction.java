package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.schedule.Activity;

/**
 * Repairs the two vanilla Piglin idle behaviours which compare exact entity
 * types, without replacing or duplicating either Piglin brain.
 */
final class PiglinFamilyInteraction {
	private static final double INTERACTION_RANGE_SQUARED = 64.0D;

	private PiglinFamilyInteraction() {
	}

	static void repair(AbstractPiglin piglin) {
		Brain<?> brain = piglin.getBrain();
		if (piglin.tickCount % 20 != 0
				|| brain.getActiveNonCoreActivity()
						.filter(activity ->
								activity == Activity.IDLE)
						.isEmpty()
				|| brain.hasMemoryValue(
						MemoryModuleType.ATTACK_TARGET)
				|| brain.hasMemoryValue(
						MemoryModuleType.WALK_TARGET)
				|| brain.hasMemoryValue(
						MemoryModuleType.INTERACTION_TARGET)) {
			return;
		}

		LivingEntity family = brain.getMemory(
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
				.flatMap(nearby -> nearby.findClosest(
						candidate ->
								candidate != piglin
										&& candidate.isAlive()
										&& isCakeWorldPiglin(
												candidate)
										&& piglin
												.distanceToSqr(
														candidate)
												<= INTERACTION_RANGE_SQUARED))
				.orElse(null);
		if (family == null) {
			return;
		}

		brain.setMemory(MemoryModuleType.INTERACTION_TARGET,
				family);
		BehaviorUtils.setWalkAndLookTargetMemories(
				piglin, family, 0.6F, 2);
	}

	private static boolean isCakeWorldPiglin(
			LivingEntity entity) {
		return entity instanceof FudgeFolk
				|| entity instanceof FudgeBrute;
	}
}
