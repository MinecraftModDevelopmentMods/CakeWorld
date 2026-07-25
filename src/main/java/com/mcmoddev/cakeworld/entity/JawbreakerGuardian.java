package com.mcmoddev.cakeworld.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's settlement-defending Iron Golem role.
 *
 * <p>Vanilla's Villager sensor and Raider activity check compare the literal
 * Iron Golem entity type. Low-frequency refreshes below preserve those roles
 * without replacing the inherited village, flower, anger or combat AI.</p>
 */
public final class JawbreakerGuardian extends IronGolem {
	private static final int VILLAGER_SCAN_TICKS = 200;
	private static final int RAIDER_SCAN_TICKS = 20;
	private static final double VILLAGER_SCAN_RANGE = 16.0D;
	private static final double RAIDER_SCAN_RANGE = 64.0D;

	public JawbreakerGuardian(
			EntityType<? extends IronGolem> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity)) {
			return false;
		}

		// Retain the real swing animation and sound. The Forge safety boundary
		// cancels health damage and supplies the protected jawbreaker bounce.
		super.doHurtTarget(target);
		return true;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!(level instanceof ServerLevel)) {
			return;
		}
		if (tickCount % VILLAGER_SCAN_TICKS == 0) {
			refreshVillagerAwareness();
		}
		if (tickCount % RAIDER_SCAN_TICKS == 0) {
			refreshRaiderCombatActivity();
		}
	}

	/**
	 * Mirrors the vanilla GolemSensor cadence for the custom entity type.
	 */
	public void refreshVillagerAwareness() {
		level.getEntitiesOfClass(Villager.class,
				getBoundingBox().inflate(VILLAGER_SCAN_RANGE))
				.forEach(GolemSensor::golemDetected);
	}

	/**
	 * Preserves Raider anti-despawn activity while it targets this defender.
	 */
	public void refreshRaiderCombatActivity() {
		level.getEntitiesOfClass(Raider.class,
				getBoundingBox().inflate(RAIDER_SCAN_RANGE),
				raider -> raider.getTarget() == this)
				.forEach(raider -> raider.setNoActionTime(0));
	}
}
