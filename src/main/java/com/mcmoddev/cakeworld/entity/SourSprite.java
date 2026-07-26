package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's summoned Vex role.
 *
 * <p>The inherited owner targeting, charging flight, bound-origin wandering,
 * limited lifetime, Iron Sword, sounds and decay remain authoritative. Family
 * difficulties turn charging contact into the same visible, protected sour
 * surprise used by its Sour Sorcerer owner; Hard retains vanilla damage.</p>
 */
public class SourSprite extends Vex {
	public SourSprite(EntityType<? extends Vex> type,
			Level level) {
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
		SourSorcerer.applySourSurprise(this, living);
		return true;
	}
}
