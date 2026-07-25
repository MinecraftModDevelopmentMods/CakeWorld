package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;

/**
 * Fudge Wastes' huntable Hoglin role.
 *
 * <p>Vanilla's Hoglin brain and offspring method hard-code the vanilla entity
 * type. The inherited brain is retained and receives a custom-family mate
 * behavior; offspring are created as Fudge Boars.</p>
 */
public final class FudgeBoar extends Hoglin {
	private static final UniformInt FUDGE_FOLK_RETREAT_DURATION =
			TimeUtil.rangeOfSeconds(5, 20);

	public FudgeBoar(
			EntityType<? extends Hoglin> type, Level level) {
		super(type, level);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		Brain<Hoglin> brain =
				(Brain<Hoglin>) super.makeBrain(dynamic);
		brain.addActivity(Activity.IDLE, 10,
				ImmutableList.of(new AnimalMakeLove(
						CakeWorldEntities.FUDGE_BOAR.get(),
						0.6F)));
		return brain;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			boolean hit = super.doHurtTarget(target);
			repairFudgeFolkContact(target);
			return hit;
		}
		if (!(target instanceof LivingEntity)) {
			return false;
		}

		// Preserve attack animation, sound and Hoglin herd response. The Forge
		// safety boundary cancels the damage and supplies the protected throw.
		super.doHurtTarget(target);
		repairFudgeFolkContact(target);
		return true;
	}

	@Override
	public boolean hurt(net.minecraft.world.damagesource.DamageSource source,
			float amount) {
		boolean hurt = super.hurt(source, amount);
		if (hurt && source.getEntity() instanceof FudgeFolk
				&& getBrain().isActive(Activity.AVOID)) {
			clearRetaliationAgainst(
					(LivingEntity) source.getEntity());
		}
		return hurt;
	}

	private void repairFudgeFolkContact(Entity target) {
		if (!(target instanceof FudgeFolk folk)
				|| !isAdult() || !piglinsOutnumberFudgeBoars()) {
			return;
		}
		retreatFrom(this, folk);
		getBrain().getMemory(
				MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS)
				.orElse(ImmutableList.of()).forEach(boar -> {
					LivingEntity nearest =
							BehaviorUtils.getNearestTarget(
									boar,
									boar.getBrain().getMemory(
											MemoryModuleType
													.AVOID_TARGET),
									folk);
					nearest = BehaviorUtils.getNearestTarget(
							boar,
							boar.getBrain().getMemory(
									MemoryModuleType
											.ATTACK_TARGET),
							nearest);
					retreatFrom(boar, nearest);
				});
	}

	private boolean piglinsOutnumberFudgeBoars() {
		int piglins = getBrain().getMemory(
				MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT)
				.orElse(0);
		int hoglins = getBrain().getMemory(
				MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT)
				.orElse(0) + 1;
		return piglins > hoglins;
	}

	private void clearRetaliationAgainst(LivingEntity folk) {
		clearMatchingAttack(this, folk);
		getBrain().getMemory(
				MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS)
				.orElse(ImmutableList.of())
				.forEach(boar ->
						clearMatchingAttack(boar, folk));
	}

	private static void clearMatchingAttack(
			Hoglin boar, LivingEntity folk) {
		if (boar.getBrain().isActive(Activity.AVOID)
				&& boar.getBrain().getMemory(
						MemoryModuleType.ATTACK_TARGET)
						.filter(target -> target == folk)
						.isPresent()) {
			boar.getBrain().eraseMemory(
					MemoryModuleType.ATTACK_TARGET);
		}
	}

	private static void retreatFrom(
			Hoglin boar, LivingEntity danger) {
		boar.getBrain().eraseMemory(
				MemoryModuleType.ATTACK_TARGET);
		boar.getBrain().eraseMemory(
				MemoryModuleType.WALK_TARGET);
		boar.getBrain().setMemoryWithExpiry(
				MemoryModuleType.AVOID_TARGET, danger,
				FUDGE_FOLK_RETREAT_DURATION.sample(
						boar.level.random));
	}

	@Override
	public FudgeBoar getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		FudgeBoar child =
				CakeWorldEntities.FUDGE_BOAR.get().create(level);
		if (child != null) {
			child.setPersistenceRequired();
		}
		return child;
	}

	public static boolean checkFudgeBoarSpawnRules(
			EntityType<? extends FudgeBoar> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return !level.getBlockState(pos.below())
				.is(Blocks.NETHER_WART_BLOCK);
	}
}
