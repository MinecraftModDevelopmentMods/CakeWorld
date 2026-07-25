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
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

/**
 * Fudge Wastes' huntable Hoglin role.
 *
 * <p>Vanilla's Hoglin brain and offspring method hard-code the vanilla entity
 * type. The inherited brain is retained and receives a custom-family mate
 * behavior; offspring are created as Fudge Boars.</p>
 */
public final class FudgeBoar extends Hoglin {
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
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity)) {
			return false;
		}

		// Preserve attack animation, sound and Hoglin herd response. The Forge
		// safety boundary cancels the damage and supplies the protected throw.
		super.doHurtTarget(target);
		return true;
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
