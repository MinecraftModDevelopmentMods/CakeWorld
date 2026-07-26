package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Wither-Skeleton role.
 *
 * <p>The subclass is deliberately behaviour-free. Vanilla remains the source
 * of truth for fortress combat, Piglin hostility, equipment, flaming arrows,
 * Wither attacks, charged-Creeper skulls and all ordinary loot. CakeWorld
 * supplies only registration, fresh-entity conversion, difficulty safety and
 * compatibility bridges around literal entity-type checks.</p>
 */
public class BurntCandyKnight extends WitherSkeleton {
	public BurntCandyKnight(
			EntityType<? extends WitherSkeleton> type,
			Level level) {
		super(type, level);
	}
}
