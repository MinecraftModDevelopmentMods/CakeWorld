package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * CakeWorld's genuine Skeleton Horse role.
 *
 * <p>The undead body, underwater riding, saddle interaction and complete
 * Skeleton Trap goal remain inherited. Literal horses emitted by vanilla
 * thunder and by that goal are converted at the world boundary, allowing the
 * private vanilla trap implementation to remain the source of truth.</p>
 */
public class BrittleBiscuitSteed extends SkeletonHorse {
	public BrittleBiscuitSteed(
			EntityType<? extends SkeletonHorse> type,
			Level level) {
		super(type, level);
	}

	@Override
	public AgeableMob getBreedOffspring(
			ServerLevel level, AgeableMob mate) {
		return CakeWorldEntities.BRITTLE_BISCUIT_STEED
				.get().create(level);
	}

	public static boolean checkBrittleBiscuitSteedSpawnRules(
			EntityType<BrittleBiscuitSteed> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return Animal.checkAnimalSpawnRules(
				type, level, reason, pos, random);
	}
}
