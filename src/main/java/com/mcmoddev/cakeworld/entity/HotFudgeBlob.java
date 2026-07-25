package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * CakeWorld's Magma Cube role. The inherited size, jump, collision, loot and
 * type-based split logic are deliberately retained.
 */
public class HotFudgeBlob extends MagmaCube {
	public HotFudgeBlob(EntityType<? extends MagmaCube> type,
			Level level) {
		super(type, level);
	}

	public static boolean checkHotFudgeBlobSpawnRules(
			EntityType<HotFudgeBlob> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return allowsNaturalSpawn(level.getDifficulty());
	}

	public static boolean allowsNaturalSpawn(
			Difficulty difficulty) {
		return difficulty != Difficulty.PEACEFUL;
	}
}
