package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import javax.annotation.Nullable;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's flavour-variety Rabbit role.
 *
 * <p>The genuine Rabbit superclass retains hopping, all six ordinary variants,
 * climate-aware group variants, carrot-garden raiding, panic and avoidance,
 * breeding inheritance, sounds and the command-only evil variant. Sprinkle
 * Seeds extend the food contract without replacing carrots, golden carrots or
 * dandelions.</p>
 */
public class GummyBunny extends Rabbit {
	public static final TagKey<Item> FOODS = ItemTags.create(
			new ResourceLocation(CakeWorld.MODID,
					"gummy_bunny_foods"));

	public GummyBunny(EntityType<? extends Rabbit> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(3, new TemptGoal(this, 1.0D,
				Ingredient.of(FOODS), false));
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return super.isFood(stack) || stack.is(FOODS);
	}

	@Override
	@Nullable
	public GummyBunny getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		Rabbit calculated = super.getBreedOffspring(level, partner);
		GummyBunny child =
				CakeWorldEntities.GUMMY_BUNNY.get().create(level);
		if (child != null && calculated != null) {
			child.setRabbitType(calculated.getRabbitType());
		}
		return child;
	}

	@Override
	public void setRabbitType(int type) {
		boolean alreadyNamed = hasCustomName();
		super.setRabbitType(type);
		if (type == TYPE_EVIL && !alreadyNamed) {
			setCustomName(new TranslatableComponent(
					"entity.cakeworld.ferocious_gummy_bunny"));
		}
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		return doHurtTargetForDifficulty(target,
				level.getDifficulty());
	}

	public boolean doHurtTargetForDifficulty(
			Entity target, Difficulty difficulty) {
		if (difficulty == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		Vec3 away = living.position().subtract(position())
				.multiply(1.0D, 0.0D, 1.0D);
		if (away.lengthSqr() < 0.0001D) {
			away = Vec3.directionFromRotation(0.0F, getYRot());
		}
		away = away.normalize();
		living.clearFire();
		living.fallDistance = 0.0F;
		living.addEffect(new MobEffectInstance(
				CakeWorldEffects.FIZZY_FEET.get(), 100,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 100,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 100,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60,
				4, false, false));
		living.push(away.x * 0.18D, 0.28D,
				away.z * 0.18D);
		playSound(SoundEvents.SLIME_JUMP, 0.7F, 1.45F);
		return true;
	}

	public static boolean checkGummyBunnySpawnRules(
			EntityType<GummyBunny> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return level.getBlockState(pos.below())
				.is(BlockTags.RABBITS_SPAWNABLE_ON)
				&& isBrightEnoughToSpawn(level, pos);
	}
}
