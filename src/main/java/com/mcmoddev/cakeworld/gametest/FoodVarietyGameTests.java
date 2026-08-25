package com.mcmoddev.cakeworld.gametest;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.cookbook.CookbookEvents;
import com.mcmoddev.cakeworld.cookbook.CookbookProgress;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

/** Regression proof for the positive, non-punitive FOOD-008 variety loop. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_food_variety")
public final class FoodVarietyGameTests {
	private static final String EMPTY = "empty";

	private FoodVarietyGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void tastingBreadthAddsPagesWithoutWeakeningRepeatFood(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(), "CakeWorldVarietyTest"));
		ItemStack biscuit = new ItemStack(CakeWorldItems.SIMPLE_BISCUIT.get());
		ItemStack cake = new ItemStack(CakeWorldItems.WARM_SPONGE_CAKE.get());
		ResourceLocation biscuitId = ForgeRegistries.ITEMS.getKey(
				biscuit.getItem());
		ResourceLocation cakeId = ForgeRegistries.ITEMS.getKey(cake.getItem());
		FoodProperties biscuitFood = biscuit.getItem().getFoodProperties();
		int foodLevel = player.getFoodData().getFoodLevel();
		float saturation = player.getFoodData().getSaturationLevel();

		require(helper, biscuitId != null && cakeId != null
					&& !biscuitId.equals(cakeId) && biscuitFood != null,
				"FOOD-008 fixture did not resolve two distinct edible items");
		Set<ResourceLocation> before = tastingPages(player);
		finishFood(player, biscuit);
		Set<ResourceLocation> afterBiscuit = tastingPages(player);
		require(helper, afterBiscuit.size() == before.size() + 1
					&& afterBiscuit.contains(biscuitId),
				"First distinct food did not add exactly one tasting page");

		finishFood(player, biscuit);
		Set<ResourceLocation> afterRepeat = tastingPages(player);
		require(helper, afterRepeat.equals(afterBiscuit),
				"Repeated food added duplicate progress or changed tasting pages");

		finishFood(player, cake);
		Set<ResourceLocation> afterCake = tastingPages(player);
		require(helper, afterCake.size() == before.size() + 2
					&& afterCake.contains(biscuitId)
					&& afterCake.contains(cakeId),
				"Tasting breadth did not produce two distinct Cookbook pages");

		FoodProperties biscuitFoodAfter = biscuit.getItem().getFoodProperties();
		require(helper, biscuitFoodAfter != null
					&& biscuitFoodAfter.getNutrition()
							== biscuitFood.getNutrition()
					&& Float.compare(biscuitFoodAfter.getSaturationModifier(),
							biscuitFood.getSaturationModifier()) == 0
					&& player.getFoodData().getFoodLevel() == foodLevel
					&& Float.compare(player.getFoodData().getSaturationLevel(),
							saturation) == 0,
				"Cookbook variety tracking weakened food or altered hunger state");
		helper.succeed();
	}

	private static void finishFood(ServerPlayer player, ItemStack food) {
		CookbookEvents.onFinishFood(new LivingEntityUseItemEvent.Finish(
				player, food.copy(), 0, ItemStack.EMPTY));
	}

	private static Set<ResourceLocation> tastingPages(ServerPlayer player) {
		Map<DiscoveryType, Set<ResourceLocation>> pages = CookbookProgress.read(
				CookbookProgress.snapshot(player));
		return Set.copyOf(pages.get(DiscoveryType.TASTING));
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
