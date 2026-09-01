package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * CakeWorld's Skeleton-role ranged mischief creature.
 *
 * <p>The undead body, sunlight response, equipment, bow/melee goal switching,
 * aiming, freeze conversion, sounds and charged-Creeper skull path remain
 * inherited. Only lower-difficulty contact is made harmless.</p>
 */
public class CandyCaneArcher extends Skeleton {
	public CandyCaneArcher(
			EntityType<? extends Skeleton> type, Level level) {
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
		CandyCaneArcherDamageSafety.applyStickyShot(
				this, living);
		return true;
	}

	public static boolean checkCandyCaneArcherSpawnRules(
			EntityType<CandyCaneArcher> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return Monster.checkMonsterSpawnRules(
				type, level, reason, pos, random);
	}
}
