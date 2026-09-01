package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Wither boss role.
 *
 * <p>The vanilla superclass remains authoritative for the summoning charge,
 * three-headed combat, powered phase, regeneration, block breaking,
 * immunities, boss bar, sounds and extended-life Nether Star drop. Difficulty
 * safety is applied at Forge's damage, explosion and mob-griefing seams so the
 * inherited Hard fight remains completely real.</p>
 */
public class BurntSugarTempest extends WitherBoss {
	public BurntSugarTempest(
			EntityType<? extends WitherBoss> type,
			Level level) {
		super(type, level);
	}
}
