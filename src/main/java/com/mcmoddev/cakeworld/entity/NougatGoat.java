package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.schedule.Activity;

/**
 * Marshmallow Peaks' sure-footed Goat role.
 *
 * Vanilla 1.18.2's Goat brain hard-codes {@code EntityType.GOAT} in its mate
 * behavior. This class keeps that brain and adds the same behavior for the
 * CakeWorld type; the original behavior simply finds no compatible partner.
 */
public final class NougatGoat extends Goat {
	private static final UniformInt LONG_JUMP_COOLDOWN =
			UniformInt.of(600, 1200);
	private static final UniformInt RAM_COOLDOWN =
			UniformInt.of(600, 6000);

	public NougatGoat(EntityType<? extends Goat> type, Level level) {
		super(type, level);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		Brain<Goat> brain = (Brain<Goat>) super.makeBrain(dynamic);
		brain.addActivityWithConditions(Activity.IDLE,
				ImmutableList.of(Pair.of(0,
						new AnimalMakeLove(
								CakeWorldEntities.NOUGAT_GOAT.get(),
								1.0F))),
				ImmutableSet.of(
						Pair.of(MemoryModuleType.RAM_TARGET,
								MemoryStatus.VALUE_ABSENT),
						Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP,
								MemoryStatus.VALUE_ABSENT)));
		return brain;
	}

	@Override
	public NougatGoat getBreedOffspring(ServerLevel level,
			AgeableMob partner) {
		NougatGoat child = CakeWorldEntities.NOUGAT_GOAT.get()
				.create(level);
		if (child != null) {
			initializeCooldowns(child);
			boolean partnerScreams = partner instanceof Goat goat
					&& goat.isScreamingGoat();
			child.setScreamingGoat(partnerScreams
					|| level.getRandom().nextDouble()
							< GOAT_SCREAMING_CHANCE);
		}
		return child;
	}

	@Override
	public boolean canAttack(LivingEntity target) {
		return !(target instanceof NougatGoat)
				&& super.canAttack(target);
	}

	public static boolean checkNougatGoatSpawnRules(
			EntityType<? extends NougatGoat> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return level.getWorldBorder().isWithinBounds(pos)
				&& Goat.checkGoatSpawnRules(type, level, reason,
						pos, random)
				&& NaturalSpawner.isValidEmptySpawnBlock(
						level, pos, level.getBlockState(pos),
						level.getFluidState(pos), type)
				&& NaturalSpawner.isValidEmptySpawnBlock(
						level, pos.above(),
						level.getBlockState(pos.above()),
						level.getFluidState(pos.above()), type);
	}

	private static void initializeCooldowns(NougatGoat child) {
		child.getBrain().setMemory(
				MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS,
				LONG_JUMP_COOLDOWN.sample(child.level.random));
		child.getBrain().setMemory(
				MemoryModuleType.RAM_COOLDOWN_TICKS,
				RAM_COOLDOWN.sample(child.level.random));
	}
}
