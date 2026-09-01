package com.mcmoddev.cakeworld.entity;

import net.minecraft.world.Difficulty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * CakeWorld's genuine Zombified-Piglin-derived neutral group-anger role.
 *
 * <p>The superclass remains authoritative for neutral anger, group alerts,
 * universal anger, equipment, pickup, baby state, fire immunity, spawning,
 * NBT and sounds. CakeWorld changes only source/type seams and the
 * lower-difficulty contact policy.</p>
 */
public class StaleFudgeFolk extends ZombifiedPiglin {
	public StaleFudgeFolk(
			EntityType<? extends ZombifiedPiglin> type,
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

		StaleCrumblerSafety.applyProtectedContact(
				this, living);
		return true;
	}

	public static boolean checkStaleFudgeFolkSpawnRules(
			EntityType<StaleFudgeFolk> type,
			LevelAccessor level,
			MobSpawnType reason, BlockPos pos,
			java.util.Random random) {
		return ZombifiedPiglin
				.checkZombifiedPiglinSpawnRules(
						EntityType.ZOMBIFIED_PIGLIN,
						level, reason, pos, random);
	}
}
