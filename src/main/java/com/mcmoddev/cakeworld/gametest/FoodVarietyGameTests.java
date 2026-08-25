package com.mcmoddev.cakeworld.gametest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import net.minecraft.world.item.Item;
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

	@GameTest(template = EMPTY)
	public static void completeCatalogueKeepsRawSnacksSmallAndPreparedFoodWorthwhile(
			GameTestHelper helper) {
		Set<ResourceLocation> emergency = Set.of(
				id("icing_spoonful"),
				id("boiled_sweet"),
				id("popcorn_kernel"),
				id("fizzy_kelp_frond"),
				id("glowing_jam_berry"),
				id("star_sugar_crystals"),
				id("macaron_shell_piece"),
				id("star_jelly_droplet"));
		List<ResourceLocation> prepared = List.of(
				id("warm_sponge_cake"),
				id("chocolate_sponge_sundae"),
				id("comfort_cocoa"),
				id("mint_wafer"),
				id("syrup_waffle"),
				id("honey_sprinkle_cupcake"),
				id("liquorice_twist"),
				id("labyrinth_liquorice_coil"),
				id("wisp_light_toffee"),
				id("rainbow_orchard_lollipop"),
				id("caramel_popcorn"),
				id("bubble_reef_fizz"),
				id("custard_tart"),
				id("island_hop_fizz"),
				id("prismatic_rock_candy"),
				id("grotto_jam_toast"),
				id("miners_nougat"),
				id("smoky_toffee_snap"),
				id("fudge_fondue_dunk"),
				id("cragfire_truffle"),
				id("steam_puffed_mallow"),
				id("starlight_pavlova"),
				id("skyberry_candyfloss"),
				id("constellation_candy"),
				id("rainbow_sky_macaron"),
				id("nebula_jelly_cup"),
				id("garden_waybite"),
				id("ember_cinnamon_swirl"),
				id("piped_celebration_cake"),
				id("mooncake"));

		Set<ResourceLocation> allFoods = ForgeRegistries.ITEMS.getValues()
				.stream()
				.filter(item -> item.getFoodProperties() != null)
				.map(ForgeRegistries.ITEMS::getKey)
				.filter(key -> key != null
						&& CakeWorld.MODID.equals(key.getNamespace()))
				.collect(Collectors.toUnmodifiableSet());
		require(helper, allFoods.size() >= 44,
				"CakeWorld's broad edible catalogue shrank below 44 foods: "
						+ allFoods.size());
		require(helper, allFoods.containsAll(emergency)
						&& allFoods.containsAll(prepared),
				"The catalogue lost a declared emergency snack or prepared dish");

		for (ResourceLocation key : emergency) {
			FoodProperties food = food(helper, key);
			require(helper, food.getNutrition() <= 2
						&& food.getSaturationModifier() <= 0.2F,
					key + " is no longer a deliberately small emergency snack");
		}

		int effectful = 0;
		for (ResourceLocation key : prepared) {
			FoodProperties food = food(helper, key);
			require(helper, food.getNutrition() >= 4
						&& food.getSaturationModifier() >= 0.4F,
					key + " is no longer a worthwhile prepared dish");
			if (!food.getEffects().isEmpty()) {
				effectful++;
			}
			ResourceLocation recipe = "warm_sponge_cake".equals(key.getPath())
					? id("warm_sponge_cake_from_oven") : key;
			require(helper,
					helper.getLevel().getRecipeManager().byKey(recipe).isPresent(),
					key + " lost its data-driven preparation recipe");
		}
		require(helper, effectful >= 24,
				"Fewer than 24 substantial dishes retain playful effects: "
						+ effectful);

		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(), "CakeWorldCatalogueTest"));
		for (ResourceLocation key : allFoods) {
			Item item = ForgeRegistries.ITEMS.getValue(key);
			require(helper, item != null,
					"Registered catalogue food disappeared: " + key);
			finishFood(player, new ItemStack(item));
		}
		Set<ResourceLocation> pages = tastingPages(player);
		require(helper, pages.containsAll(allFoods),
				"Not every CakeWorld food produced a player-specific tasting page");
		int pageCount = pages.size();
		for (ResourceLocation key : allFoods) {
			finishFood(player,
					new ItemStack(ForgeRegistries.ITEMS.getValue(key)));
		}
		require(helper, tastingPages(player).size() == pageCount,
				"Repeated catalogue tasting created duplicate Cookbook pages");
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

	private static FoodProperties food(GameTestHelper helper,
			ResourceLocation key) {
		Item item = ForgeRegistries.ITEMS.getValue(key);
		require(helper, item != null && item.getFoodProperties() != null,
				"Missing declared catalogue food " + key);
		return item.getFoodProperties();
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
