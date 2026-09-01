package com.mcmoddev.cakeworld.entity;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * CakeWorld's Strider-role passive mount.
 *
 * <p>The complete vanilla Strider body, steering, saddle, food, temperature,
 * sound, water weakness and passenger behaviour remain inherited. The two
 * literal-lava navigation checks are generalized to the lava fluid tag so Hot
 * Fudge receives the same behaviour as vanilla Lava.</p>
 */
public class FudgeSkater extends Strider {
	public FudgeSkater(
			EntityType<? extends Strider> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		for (WrappedGoal wrapped :
				List.copyOf(goalSelector.getAvailableGoals())) {
			if ("StriderGoToLavaGoal".equals(
					wrapped.getGoal().getClass()
							.getSimpleName())) {
				goalSelector.removeGoal(wrapped.getGoal());
			}
		}
		goalSelector.addGoal(4,
				new FudgeSkaterGoToHotFluidGoal(
						this, 1.5D));
	}

	/**
	 * The exact Strider spawn-column rule, parameterized for the custom type and
	 * all fluids in the shared Lava role tag.
	 */
	public static boolean checkFudgeSkaterSpawnRules(
			EntityType<FudgeSkater> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		BlockPos.MutableBlockPos probe = pos.mutable();
		do {
			probe.move(Direction.UP);
		} while (level.getFluidState(probe)
				.is(FluidTags.LAVA));
		return level.getBlockState(probe).isAir();
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		return new FudgeSkaterPathNavigation(
				this, level);
	}

	@Override
	public FudgeSkater getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.FUDGE_SKATER
				.get().create(level);
	}

	static final class FudgeSkaterGoToHotFluidGoal
			extends MoveToBlockGoal {
		private final FudgeSkater skater;

		FudgeSkaterGoToHotFluidGoal(
				FudgeSkater skater, double speed) {
			super(skater, speed, 8, 2);
			this.skater = skater;
		}

		@Override
		public BlockPos getMoveToTarget() {
			return blockPos;
		}

		@Override
		public boolean canContinueToUse() {
			return !skater.isInLava()
					&& isValidTarget(
							skater.level, blockPos);
		}

		@Override
		public boolean canUse() {
			return !skater.isInLava()
					&& super.canUse();
		}

		@Override
		public boolean shouldRecalculatePath() {
			return tryTicks % 20 == 0;
		}

		@Override
		protected boolean isValidTarget(
				LevelReader level, BlockPos pos) {
			return level.getFluidState(pos)
						.is(FluidTags.LAVA)
					&& level.getBlockState(pos.above())
							.isPathfindable(level, pos,
									PathComputationType.LAND);
		}
	}

	static final class FudgeSkaterPathNavigation
			extends GroundPathNavigation {
		FudgeSkaterPathNavigation(
				FudgeSkater skater, Level level) {
			super(skater, level);
		}

		@Override
		protected PathFinder createPathFinder(
				int maximumVisitedNodes) {
			nodeEvaluator = new WalkNodeEvaluator();
			return new PathFinder(nodeEvaluator,
					maximumVisitedNodes);
		}

		@Override
		protected boolean hasValidPathType(
				BlockPathTypes type) {
			return type == BlockPathTypes.LAVA
					|| type == BlockPathTypes.DAMAGE_FIRE
					|| type == BlockPathTypes.DANGER_FIRE
					|| super.hasValidPathType(type);
		}

		@Override
		public boolean isStableDestination(BlockPos pos) {
			return level.getFluidState(pos)
						.is(FluidTags.LAVA)
					|| super.isStableDestination(pos);
		}
	}
}
