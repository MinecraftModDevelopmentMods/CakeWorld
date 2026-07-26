package com.mcmoddev.cakeworld.entity;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's genuine Witch and raid-support role.
 *
 * <p>The superclass remains authoritative for self-drinking, magic
 * resistance, raid membership, targeting, sounds and Hard attacks. CakeWorld
 * repairs the literal-Witch allied-healing exclusion and replaces hostile
 * attacks below Hard with a visible but harmless sticky mixture.</p>
 */
public class BitterBaker extends Witch {
	private static final int INCONVENIENCE_TICKS = 100;
	private static final int RESCUE_TICKS = 140;
	private static final double SPLASH_RANGE = 4.0D;
	private static final double SPLASH_RANGE_SQUARED =
			SPLASH_RANGE * SPLASH_RANGE;

	public BitterBaker(
			EntityType<? extends Witch> type,
			Level level) {
		super(type, level);
	}

	public void resumeConvertedDrink() {
		if (!getMainHandItem().is(Items.POTION)) {
			return;
		}
		usingTime = getMainHandItem()
				.getUseDuration();
		setUsingItem(true);
		AttributeInstance movement = getAttribute(
				Attributes.MOVEMENT_SPEED);
		movement.removeModifier(
				SPEED_MODIFIER_DRINKING);
		movement.addTransientModifier(
				SPEED_MODIFIER_DRINKING);
	}

	@Override
	@Nullable
	public SpawnGroupData finalizeSpawn(
			ServerLevelAccessor level,
			DifficultyInstance difficulty,
			MobSpawnType spawnType,
			@Nullable SpawnGroupData spawnData,
			@Nullable CompoundTag dataTag) {
		SpawnGroupData result = super.finalizeSpawn(
				level, difficulty, spawnType,
				spawnData, dataTag);
		// Raider checks the literal EntityType.WITCH here. Preserve the
		// vanilla rule: natural Witches do not later join nearby raids.
		setCanJoinRaid(
				spawnType != MobSpawnType.NATURAL);
		return result;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

		// Witch's private healing goal excludes only EntityType.WITCH. A
		// registered Witch subclass would otherwise select another Baker.
		List<Goal> literalOnlyHealingGoals = targetSelector
				.getAvailableGoals().stream()
				.map(WrappedGoal::getGoal)
				.filter(NearestHealableRaiderTargetGoal.class
						::isInstance)
				.toList();
		literalOnlyHealingGoals.forEach(
				targetSelector::removeGoal);
		targetSelector.addGoal(2,
				new NearestHealableRaiderTargetGoal<>(
						this, Raider.class, true,
						candidate -> candidate != null
								&& hasActiveRaid()
								&& !(candidate
										instanceof Witch)));
	}

	@Override
	public void performRangedAttack(
			LivingEntity target, float distanceFactor) {
		if (target instanceof Witch) {
			setTarget(null);
			return;
		}
		if (target instanceof Raider
				|| level.getDifficulty()
						== Difficulty.HARD) {
			super.performRangedAttack(
					target, distanceFactor);
			return;
		}
		if (isDrinkingPotion()) {
			return;
		}

		protectFromMixtureHazards(target);
		Vec3 movement = target.getDeltaMovement();
		double x = target.getX() + movement.x - getX();
		double y = target.getEyeY() - 1.1F - getY();
		double z = target.getZ() + movement.z - getZ();
		double horizontalDistance =
				Math.sqrt(x * x + z * z);
		ThrownPotion mixture =
				new HarmlessKitchenMixture(level, this);
		mixture.setItem(PotionUtils.setPotion(
				new ItemStack(Items.SPLASH_POTION),
				Potions.SLOWNESS));
		mixture.setXRot(
				mixture.getXRot() + 20.0F);
		mixture.shoot(x,
				y + horizontalDistance * 0.2D,
				z, 0.75F, 8.0F);
		if (!isSilent()) {
			level.playSound((Player)null,
					getX(), getY(), getZ(),
					SoundEvents.WITCH_THROW,
					getSoundSource(), 1.0F,
					0.8F + random.nextFloat()
							* 0.4F);
		}
		level.addFreshEntity(mixture);
	}

	static void protectFromMixtureHazards(
			LivingEntity entity) {
		entity.fallDistance = 0.0F;
		entity.clearFire();
		entity.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				INCONVENIENCE_TICKS,
				0, false, true));
		entity.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				INCONVENIENCE_TICKS,
				0, false, true));
		entity.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS,
				0, false, false));
		entity.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS,
				0, false, false));
		entity.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS,
				4, false, false));
	}

	private static final class HarmlessKitchenMixture
			extends ThrownPotion {
		private HarmlessKitchenMixture(
				Level level, LivingEntity owner) {
			super(level, owner);
		}

		@Override
		protected void onHit(HitResult result) {
			if (!level.isClientSide) {
				Entity owner = getOwner();
				AABB affectedArea = getBoundingBox()
						.inflate(SPLASH_RANGE,
								SPLASH_RANGE / 2.0D,
								SPLASH_RANGE);
				for (LivingEntity affected :
						level.getEntitiesOfClass(
								LivingEntity.class,
								affectedArea,
								entity -> entity != owner
										&& distanceToSqr(
												entity)
												< SPLASH_RANGE_SQUARED)) {
					protectFromMixtureHazards(
							affected);
				}
			}
			super.onHit(result);
		}
	}
}
