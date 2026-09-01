package com.mcmoddev.cakeworld.entity;

import java.util.Random;
import java.util.EnumSet;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's Drowned-role swimmer, with soggy rather than harmful attacks
 * below Hard difficulty.
 */
public final class SoggyBiscuit extends Drowned {
	private static final int SOGGY_TICKS = 100;

	public SoggyBiscuit(EntityType<? extends Drowned> type, Level level) {
		super(type, level);
	}

	@Override
	protected void addBehaviourGoals() {
		super.addBehaviourGoals();
		goalSelector.addGoal(0,
				new TaggedWaterGoal(this, 1.0D));
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		return applySoggySplash(this, target);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (level.getDifficulty() == Difficulty.HARD) {
			super.performRangedAttack(target, distanceFactor);
			return;
		}

		SoggyTridentProjectile trident = new SoggyTridentProjectile(
				CakeWorldEntities.SOGGY_TRIDENT.get(), level, this);
		double x = target.getX() - getX();
		double y = target.getY(0.3333333333333333D) - trident.getY();
		double z = target.getZ() - getZ();
		double horizontal = Math.sqrt(x * x + z * z);
		trident.shoot(x, y + horizontal * 0.2D, z, 1.6F,
				(float) (14 - level.getDifficulty().getId() * 4));
		playSound(SoundEvents.DROWNED_SHOOT, 1.0F,
				1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
		level.addFreshEntity(trident);
	}

	public static boolean applySoggySplash(Entity source, Entity target) {
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		Vec3 offset = living.position().subtract(source.position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.2D);
		}
		living.push(horizontal.x, 0.1D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, SOGGY_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.DIG_SLOWDOWN, SOGGY_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.WATER_BREATHING, SOGGY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, SOGGY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, SOGGY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, SOGGY_TICKS,
				4, false, false));
		source.playSound(SoundEvents.GENERIC_SPLASH, 1.0F, 0.9F);
		return true;
	}

	public static boolean checkSoggyBiscuitSpawnRules(
			EntityType<SoggyBiscuit> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return Drowned.checkDrownedSpawnRules(EntityType.DROWNED,
				level, reason, pos, random);
	}

	public static boolean isTaggedWater(Level level, BlockPos pos) {
		return level.getFluidState(pos).is(FluidTags.WATER);
	}

	private static final class TaggedWaterGoal extends Goal {
		private final PathfinderMob mob;
		private final double speed;
		private BlockPos destination;

		private TaggedWaterGoal(PathfinderMob mob, double speed) {
			this.mob = mob;
			this.speed = speed;
			setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!mob.level.isDay() || mob.isInWater()) {
				return false;
			}
			BlockPos origin = mob.blockPosition();
			for (int attempt = 0; attempt < 10; attempt++) {
				BlockPos candidate = origin.offset(
						mob.getRandom().nextInt(20) - 10,
						2 - mob.getRandom().nextInt(8),
						mob.getRandom().nextInt(20) - 10);
				if (isTaggedWater(mob.level, candidate)) {
					destination = candidate;
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean canContinueToUse() {
			return destination != null
					&& !mob.isInWater()
					&& !mob.getNavigation().isDone();
		}

		@Override
		public void start() {
			mob.getNavigation().moveTo(
					destination.getX() + 0.5D,
					destination.getY(),
					destination.getZ() + 0.5D, speed);
		}

		@Override
		public void stop() {
			destination = null;
		}
	}
}
