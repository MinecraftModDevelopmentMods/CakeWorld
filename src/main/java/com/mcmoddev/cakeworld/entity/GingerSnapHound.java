package com.mcmoddev.cakeworld.entity;

import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's genuine Wolf-derived tameable companion and pack hunter.
 *
 * <p>The Wolf superclass remains authoritative for bone taming, ownership,
 * sitting, collars, meat healing and breeding, begging, pack anger, owner
 * defence, wet shaking and its complete sound and movement contract. One
 * parallel prey goal repairs the literal entity-type checks in vanilla's Wolf
 * predicate so CakeWorld's Sheep, Rabbit and Fox replacements remain valid
 * wild prey. A data-pack tag adds optional biscuit treats without removing any
 * vanilla meat.</p>
 */
public class GingerSnapHound extends Wolf {
	public static final TagKey<Item> FOODS = ItemTags.create(
			new ResourceLocation(CakeWorld.MODID,
					"ginger_snap_hound_foods"));
	private static final int INCONVENIENCE_TICKS = 80;
	private static final int RESCUE_TICKS = 160;

	public GingerSnapHound(
			EntityType<? extends Wolf> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		targetSelector.addGoal(5,
				new CakeWorldPreyGoal());
	}

	public static boolean isCakeWorldPrey(
			LivingEntity entity) {
		EntityType<?> type = entity.getType();
		return type == CakeWorldEntities
						.CANDYFLOSS_SHEEP.get()
				|| type == CakeWorldEntities
						.GUMMY_BUNNY.get()
				|| type == CakeWorldEntities
						.PEPPERMINT_FOX.get();
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(FOODS) || super.isFood(stack);
	}

	@Override
	@Nullable
	public GingerSnapHound getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		GingerSnapHound child =
				CakeWorldEntities.GINGER_SNAP_HOUND
						.get().create(level);
		if (child == null) {
			return null;
		}
		UUID owner = getOwnerUUID();
		if (owner != null) {
			child.setOwnerUUID(owner);
			child.setTame(true);
		}
		return child;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		return doHurtTargetForDifficulty(
				target, level.getDifficulty());
	}

	public boolean doHurtTargetForDifficulty(
			Entity target, Difficulty difficulty) {
		if (difficulty == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		Vec3 away = living.position()
				.subtract(position());
		Vec3 horizontal = new Vec3(
				away.x, 0.0D, away.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize()
					.scale(0.22D);
		}
		living.push(horizontal.x, 0.16D,
				horizontal.z);
		living.clearFire();
		living.fallDistance = 0.0F;
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				INCONVENIENCE_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				INCONVENIENCE_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		playSound(SoundEvents.WOLF_SHAKE,
				0.45F, 1.35F);
		return true;
	}

	public static boolean checkGingerSnapHoundSpawnRules(
			EntityType<GingerSnapHound> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return level.getBlockState(pos.below())
				.is(BlockTags.WOLVES_SPAWNABLE_ON)
				&& isBrightEnoughToSpawn(level, pos);
	}

	private final class CakeWorldPreyGoal
			extends NonTameRandomTargetGoal<Animal> {
		private CakeWorldPreyGoal() {
			super(GingerSnapHound.this,
					Animal.class, false,
					GingerSnapHound::isCakeWorldPrey);
		}
	}
}
