package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Zombie-derived common night creature.
 *
 * <p>The superclass remains authoritative for goals, targets, sunlight,
 * equipment, baby state, jockeys, item pickup, reinforcements, Villager
 * conversion, drowning, NBT and sounds. CakeWorld changes only source/type
 * seams and the lower-difficulty contact policy.</p>
 */
public class StaleCrumbler extends Zombie {
	public StaleCrumbler(EntityType<? extends Zombie> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		StaleCrumblerSafety.applyProtectedContact(
				this, living);
		return true;
	}
}
