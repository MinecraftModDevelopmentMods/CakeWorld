package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Pillager patrol, Outpost and raid role.
 *
 * <p>The superclass retains crossbow combat, patrol/captain state, raid
 * membership, inventory, equipment, alliances and sounds. CakeWorld only
 * repairs the Villager hostile sensor's literal entity-type table.</p>
 */
public class BiscuitBandit extends Pillager {
	private static final double VILLAGER_ALERT_RANGE = 15.0D;
	private static final double VILLAGER_ALERT_RANGE_SQUARED =
			VILLAGER_ALERT_RANGE * VILLAGER_ALERT_RANGE;

	public BiscuitBandit(
			EntityType<? extends Pillager> type, Level level) {
		super(type, level);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		repairVillagerHostileAwareness();
	}

	protected void repairVillagerHostileAwareness() {
		if (tickCount % 20 != 0) {
			return;
		}
		level.getEntitiesOfClass(
				Villager.class,
				getBoundingBox().inflate(VILLAGER_ALERT_RANGE),
				villager -> villager.isAlive()
						&& isWithinVillagerAlertRange(
								villager))
				.forEach(this::alertVisibleVillager);
	}

	protected boolean isWithinVillagerAlertRange(
			LivingEntity entity) {
		return entity.distanceToSqr(this)
				<= VILLAGER_ALERT_RANGE_SQUARED;
	}

	private void alertVisibleVillager(Villager villager) {
		Brain<?> brain = villager.getBrain();
		boolean visible = brain.getMemory(
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
				.filter(nearby -> nearby.contains(this))
				.isPresent();
		if (!visible) {
			return;
		}

		LivingEntity current = brain.getMemory(
				MemoryModuleType.NEAREST_HOSTILE)
				.orElse(null);
		if (current == null
				|| !current.isAlive()
				|| villager.distanceToSqr(this)
						< villager.distanceToSqr(current)) {
			brain.setMemory(
					MemoryModuleType.NEAREST_HOSTILE, this);
		}
	}
}
