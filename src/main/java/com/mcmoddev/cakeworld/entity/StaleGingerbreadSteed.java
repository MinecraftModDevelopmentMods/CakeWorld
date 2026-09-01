package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * CakeWorld's genuine Zombie Horse compatibility mount.
 *
 * <p>Minecraft 1.18.2 provides this role through commands and its creative
 * spawn egg, not ordinary biome ecology. Taming, saddle inventory, rider
 * control, food, fall damage, undead state and NBT therefore remain inherited
 * without inventing an encounter. The inherited command-only offspring
 * factory is repaired to return the CakeWorld type.</p>
 */
public class StaleGingerbreadSteed extends ZombieHorse {
	public StaleGingerbreadSteed(
			EntityType<? extends ZombieHorse> type,
			Level level) {
		super(type, level);
	}

	@Override
	public AgeableMob getBreedOffspring(
			ServerLevel level, AgeableMob mate) {
		return CakeWorldEntities.STALE_GINGERBREAD_STEED
				.get().create(level);
	}

	public static boolean
			checkStaleGingerbreadSteedSpawnRules(
					EntityType<StaleGingerbreadSteed> type,
					ServerLevelAccessor level,
					MobSpawnType reason,
					BlockPos pos, Random random) {
		return Animal.checkAnimalSpawnRules(
				type, level, reason, pos, random);
	}
}
