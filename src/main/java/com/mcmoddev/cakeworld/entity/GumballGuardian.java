package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's ordinary Soda Palace defender.
 *
 * <p>The inherited Guardian supplies the visible eighty-tick beam, thorns,
 * swimming, flop, targeting and loot roles. Damage is changed only below
 * Hard by {@link GumballGuardianDamageSafety}.</p>
 */
public final class GumballGuardian extends Guardian {
	public GumballGuardian(
			EntityType<? extends Guardian> type, Level level) {
		super(type, level);
	}
}
