package com.mcmoddev.cakeworld.entity;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's cane-loving Jungle panda.
 *
 * Remaining a genuine Panda preserves genes, moods, family alerting, rolling,
 * sneezing, sitting, eating, thunder fear and the vanilla renderer animation
 * contract. The two extra goals extend vanilla's literal Bamboo checks to
 * tagged CakeWorld vegetation without removing Bamboo compatibility.
 */
public class ChocolatePanda extends Panda {
	public static final TagKey<Item> FOODS = TagKey.create(
			Registry.ITEM_REGISTRY,
			new ResourceLocation(CakeWorld.MODID,
					"chocolate_panda_foods"));
	public static final TagKey<Block> BREEDING_PLANTS = TagKey.create(
			Registry.BLOCK_REGISTRY,
			new ResourceLocation(CakeWorld.MODID,
					"chocolate_panda_breeding_plants"));

	public ChocolatePanda(
			EntityType<? extends Panda> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(2,
				new CakeWorldPandaBreedGoal(this, 1.0D));
		goalSelector.addGoal(4,
				new TemptGoal(this, 1.0D,
						Ingredient.of(FOODS), false));
		goalSelector.addGoal(7,
				new CakeWorldPandaFoodGoal(this));
	}

	@Override
	@Nullable
	public ChocolatePanda getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		ChocolatePanda child =
				CakeWorldEntities.CHOCOLATE_PANDA.get()
						.create(level);
		if (child != null && partner instanceof Panda panda) {
			child.setGeneFromParents(this, panda);
			child.setAttributes();
		}
		return child;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return super.isFood(stack) || stack.is(FOODS);
	}

	@Override
	protected void pickUpItem(ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();
		if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()
				&& stack.is(FOODS)
				&& itemEntity.isAlive()
				&& !itemEntity.hasPickUpDelay()) {
			onItemPickup(itemEntity);
			setItemSlot(EquipmentSlot.MAINHAND, stack);
			handDropChances[
					EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
			take(itemEntity, stack.getCount());
			itemEntity.discard();
			return;
		}
		super.pickUpItem(itemEntity);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		playSound(SoundEvents.PANDA_BITE, 1.0F, 1.0F);
		if (!isAggressive()) {
			setTarget(null);
		}
		Vec3 offset = living.position().subtract(position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.16D);
		}
		living.push(horizontal.x, 0.18D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 100,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 100,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 100,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 100,
				4, false, false));
		return true;
	}

	private void sitWithFood() {
		if (!isInWater()) {
			setZza(0.0F);
			getNavigation().stop();
			sit(true);
			eat(true);
		}
	}

	private static final class CakeWorldPandaBreedGoal
			extends BreedGoal {
		private final ChocolatePanda panda;

		private CakeWorldPandaBreedGoal(
				ChocolatePanda panda, double speed) {
			super(panda, speed);
			this.panda = panda;
		}

		@Override
		public boolean canUse() {
			if (!super.canUse()
					|| !hasCakeWorldBreedingPlant()) {
				return false;
			}
			panda.setUnhappyCounter(0);
			return true;
		}

		private boolean hasCakeWorldBreedingPlant() {
			BlockPos origin = panda.blockPosition();
			for (int y = 0; y < 3; y++) {
				for (int x = -8; x <= 8; x++) {
					for (int z = -8; z <= 8; z++) {
						if (level.getBlockState(
								origin.offset(x, y, z))
								.is(BREEDING_PLANTS)) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	private static final class CakeWorldPandaFoodGoal
			extends Goal {
		private final ChocolatePanda panda;
		@Nullable
		private ItemEntity food;

		private CakeWorldPandaFoodGoal(
				ChocolatePanda panda) {
			this.panda = panda;
			setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (panda.isBaby() || panda.isInWater()
					|| !panda.canPerformAction()
					|| !panda.getItemBySlot(
							EquipmentSlot.MAINHAND)
							.isEmpty()) {
				return false;
			}
			List<ItemEntity> foods =
					panda.level.getEntitiesOfClass(
							ItemEntity.class,
							panda.getBoundingBox().inflate(
									6.0D),
							CakeWorldPandaFoodGoal
									::isAvailableFood);
			food = foods.stream()
					.min((left, right) -> Double.compare(
							panda.distanceToSqr(left),
							panda.distanceToSqr(right)))
					.orElse(null);
			return food != null;
		}

		@Override
		public boolean canContinueToUse() {
			return food != null && food.isAlive()
					&& !panda.isInWater()
					&& panda.canPerformAction()
					&& panda.getItemBySlot(
							EquipmentSlot.MAINHAND)
							.isEmpty()
					&& panda.distanceToSqr(food) <= 100.0D;
		}

		@Override
		public void start() {
			if (food != null) {
				panda.getNavigation().moveTo(food, 1.2D);
			}
		}

		@Override
		public void tick() {
			if (food == null) {
				return;
			}
			if (panda.distanceToSqr(food) <= 2.25D) {
				panda.pickUpItem(food);
				if (!panda.getItemBySlot(
						EquipmentSlot.MAINHAND)
						.isEmpty()) {
					panda.sitWithFood();
				}
			} else {
				panda.getNavigation().moveTo(food, 1.2D);
			}
		}

		@Override
		public void stop() {
			if (panda.getItemBySlot(
					EquipmentSlot.MAINHAND).isEmpty()) {
				panda.getNavigation().stop();
			}
			food = null;
		}

		private static boolean isAvailableFood(
				ItemEntity itemEntity) {
			return itemEntity.isAlive()
					&& !itemEntity.hasPickUpDelay()
					&& itemEntity.getItem().is(FOODS);
		}
	}
}
