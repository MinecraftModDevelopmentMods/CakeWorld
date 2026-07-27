package com.mcmoddev.cakeworld.init;

import java.util.function.Supplier;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.item.ExplorersCookbookItem;
import com.mcmoddev.cakeworld.item.JellybeanFishBucketItem;
import com.mcmoddev.cakeworld.item.JellylotlBucketItem;
import com.mcmoddev.cakeworld.item.LemonadeBottleItem;
import com.mcmoddev.cakeworld.item.ReusableKitchenToolItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldItems {
	private static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, CakeWorld.MODID);

	public static final RegistryObject<Item> CHOCOLATE_SPONGE_SLICE =
			food("chocolate_sponge_slice", 3, 0.3F);
	public static final RegistryObject<Item> ICING_SPOONFUL =
			food("icing_spoonful", 1, 0.2F);
	public static final RegistryObject<Item> SIMPLE_BISCUIT =
			food("simple_biscuit", 4, 0.4F);
	public static final RegistryObject<Item> COCOA_TRUFFLE =
			food("cocoa_truffle", 4, 0.5F);
	public static final RegistryObject<Item> LEMONADE_BOTTLE = ITEMS.register("lemonade_bottle",
			() -> new LemonadeBottleItem(foodProperties(3, 0.3F)
					.craftRemainder(Items.GLASS_BOTTLE)));
	public static final RegistryObject<Item> CHOCOLATE_SPONGE_SUNDAE =
			ITEMS.register("chocolate_sponge_sundae",
					() -> new BowlFoodItem(foodProperties(8, 0.8F).stacksTo(1)));
	public static final RegistryObject<Item> SPONGE_BATTER =
			ITEMS.register("sponge_batter",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> WARM_SPONGE_CAKE =
			food("warm_sponge_cake", 6, 0.7F);
	public static final RegistryObject<Item> BOILED_SWEET =
			food("boiled_sweet", 2, 0.2F);
	public static final RegistryObject<Item> SHERBET_FIZZ =
			ITEMS.register("sherbet_fizz",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.food(new FoodProperties.Builder()
									.nutrition(3).saturationMod(0.3F)
									.effect(() -> new MobEffectInstance(
											CakeWorldEffects.SUGAR_RUSH.get(),
											200), 1.0F)
									.build())));
	public static final RegistryObject<Item> COMFORT_COCOA =
			ITEMS.register("comfort_cocoa",
					() -> new BowlFoodItem(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.stacksTo(1)
							.food(new FoodProperties.Builder()
									.nutrition(5).saturationMod(0.6F)
									.effect(() -> new MobEffectInstance(
											CakeWorldEffects.COCOA_COMFORT.get(),
											300), 1.0F)
									.build())));
	public static final RegistryObject<Item> MINT_WAFER =
			ITEMS.register("mint_wafer",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.food(new FoodProperties.Builder()
									.nutrition(4).saturationMod(0.4F)
									.effect(() -> new MobEffectInstance(
											CakeWorldEffects.MINTY_FRESH.get(),
											200), 1.0F)
									.build())));
	public static final RegistryObject<Item> SYRUP_WAFFLE =
			ITEMS.register("syrup_waffle",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.food(new FoodProperties.Builder()
									.nutrition(6).saturationMod(0.7F)
									.effect(() -> new MobEffectInstance(
											CakeWorldEffects.SUGAR_RUSH.get(),
											160), 1.0F)
									.build())));
	public static final RegistryObject<Item> HONEY_SPRINKLE_CUPCAKE =
			ITEMS.register("honey_sprinkle_cupcake",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.food(new FoodProperties.Builder()
									.nutrition(7).saturationMod(0.8F)
									.effect(() -> new MobEffectInstance(
											CakeWorldEffects.SUGAR_RUSH.get(),
											120), 1.0F)
									.build())));
	public static final RegistryObject<Item> LIQUORICE_TWIST =
			ITEMS.register("liquorice_twist",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.food(new FoodProperties.Builder()
									.nutrition(5).saturationMod(0.6F)
									.effect(() -> new MobEffectInstance(
											MobEffects.NIGHT_VISION,
											240), 1.0F)
									.build())));
	public static final RegistryObject<Item> FIZZY_POPPERS =
			ITEMS.register("fizzy_poppers",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_FOOD)
							.food(new FoodProperties.Builder()
									.nutrition(2).saturationMod(0.2F)
									.effect(() -> new MobEffectInstance(
											CakeWorldEffects.FIZZY_FEET.get(),
											200), 1.0F)
									.build())));
	public static final RegistryObject<Item> FUDGE_SQUARE =
			food("fudge_square", 5, 0.5F);
	public static final RegistryObject<Item> CARAMEL_CHEW =
			food("caramel_chew", 3, 0.3F);
	public static final RegistryObject<Item> SPRINKLE_SEEDS =
			ITEMS.register("sprinkle_seeds",
					() -> new ItemNameBlockItem(
							CakeWorldBlocks.CANDY_SPROUT.get(),
							new Item.Properties()
									.tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> FROSTING_MORTAR =
			ITEMS.register("frosting_mortar",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> ROLLING_PIN =
			ITEMS.register("rolling_pin",
					() -> new ReusableKitchenToolItem(
							new Item.Properties()
									.tab(CreativeModeTab.TAB_TOOLS)
									.stacksTo(1)));
	public static final RegistryObject<Item> PIPING_BAG =
			ITEMS.register("piping_bag",
					() -> new ReusableKitchenToolItem(
							new Item.Properties()
									.tab(CreativeModeTab.TAB_TOOLS)
									.stacksTo(1)));
	public static final RegistryObject<Item> ROLLED_BISCUIT_DOUGH =
			ITEMS.register("rolled_biscuit_dough",
					() -> new Item(new Item.Properties()
							.tab(CreativeModeTab.TAB_MISC)));
	public static final RegistryObject<Item> PIPED_CELEBRATION_CAKE =
			food("piped_celebration_cake", 9, 0.9F);
	public static final RegistryObject<Item> MOONCAKE =
			food("mooncake", 8, 0.8F);
	public static final RegistryObject<Item> EXPLORERS_COOKBOOK =
			ITEMS.register("explorers_cookbook",
					() -> new ExplorersCookbookItem(new Item.Properties()
							.tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
	public static final RegistryObject<Item> JELLYLOTL_BUCKET =
			ITEMS.register("jellylotl_bucket",
					() -> new JellylotlBucketItem(new Item.Properties()
							.tab(CreativeModeTab.TAB_MISC)
							.stacksTo(1)));
	public static final RegistryObject<Item> SODA_COD_BUCKET =
			ITEMS.register("soda_cod_bucket",
					() -> new MobBucketItem(CakeWorldEntities.SODA_COD,
							CakeWorldFluids.LEMONADE,
							() -> SoundEvents.BUCKET_EMPTY_FISH,
							new Item.Properties()
									.tab(CreativeModeTab.TAB_MISC)
									.stacksTo(1)));
	public static final RegistryObject<Item> SHERBET_SALMON_BUCKET =
			ITEMS.register("sherbet_salmon_bucket",
					() -> new MobBucketItem(
							CakeWorldEntities.SHERBET_SALMON,
							CakeWorldFluids.LEMONADE,
							() -> SoundEvents.BUCKET_EMPTY_FISH,
							new Item.Properties()
									.tab(CreativeModeTab.TAB_MISC)
									.stacksTo(1)));
	public static final RegistryObject<Item> FIZZBALL_FISH_BUCKET =
			ITEMS.register("fizzball_fish_bucket",
					() -> new MobBucketItem(
							CakeWorldEntities.FIZZBALL_FISH,
							CakeWorldFluids.LEMONADE,
							() -> SoundEvents.BUCKET_EMPTY_FISH,
							new Item.Properties()
									.tab(CreativeModeTab.TAB_MISC)
									.stacksTo(1)));
	public static final RegistryObject<Item> JELLYBEAN_FISH_BUCKET =
			ITEMS.register("jellybean_fish_bucket",
					() -> new JellybeanFishBucketItem(
							new Item.Properties()
									.tab(CreativeModeTab.TAB_MISC)
									.stacksTo(1)));
	public static final RegistryObject<Item> JELLYLOTL_SPAWN_EGG =
			spawnEgg("jellylotl_spawn_egg", CakeWorldEntities.JELLYLOTL,
					0xF29AB2, 0x8AD9E8);
	public static final RegistryObject<Item> BONBON_BAT_SPAWN_EGG =
			spawnEgg("bonbon_bat_spawn_egg", CakeWorldEntities.BONBON_BAT,
					0x6B3C30, 0xF2B5C8);
	public static final RegistryObject<Item> SUGAR_BEE_SPAWN_EGG =
			spawnEgg("sugar_bee_spawn_egg", CakeWorldEntities.SUGAR_BEE,
					0xFFF0A8, 0xF29AB2);
	public static final RegistryObject<Item> CUSTARD_CAT_SPAWN_EGG =
			spawnEgg("custard_cat_spawn_egg",
					CakeWorldEntities.CUSTARD_CAT,
					0xF2D27A, 0xFFF3B0);
	public static final RegistryObject<Item> SHERBET_OCELOT_SPAWN_EGG =
			spawnEgg("sherbet_ocelot_spawn_egg",
					CakeWorldEntities.SHERBET_OCELOT,
					0xF5A24A, 0x66D7C9);
	public static final RegistryObject<Item> CHOCOLATE_PANDA_SPAWN_EGG =
			spawnEgg("chocolate_panda_spawn_egg",
					CakeWorldEntities.CHOCOLATE_PANDA,
					0x4A2618, 0xF0D7B0);
	public static final RegistryObject<Item> LOLLIPOP_LORIKEET_SPAWN_EGG =
			spawnEgg("lollipop_lorikeet_spawn_egg",
					CakeWorldEntities.LOLLIPOP_LORIKEET,
					0xE64A62, 0x58C7D9);
	public static final RegistryObject<Item> WAFER_WRAITH_SPAWN_EGG =
			spawnEgg("wafer_wraith_spawn_egg",
					CakeWorldEntities.WAFER_WRAITH,
					0xD8B77B, 0x5B3524);
	public static final RegistryObject<Item> DEEP_LIQUORICE_WEAVER_SPAWN_EGG =
			spawnEgg("deep_liquorice_weaver_spawn_egg",
					CakeWorldEntities.DEEP_LIQUORICE_WEAVER,
					0x24152F, 0x8A4B9F);
	public static final RegistryObject<Item> SODA_COD_SPAWN_EGG =
			spawnEgg("soda_cod_spawn_egg",
					CakeWorldEntities.SODA_COD,
					0xF6D66D, 0xF7A8D8);
	public static final RegistryObject<Item>
			SHERBET_SALMON_SPAWN_EGG =
			spawnEgg("sherbet_salmon_spawn_egg",
					CakeWorldEntities.SHERBET_SALMON,
					0xF57BC5, 0xF7E36D);
	public static final RegistryObject<Item> FIZZBALL_FISH_SPAWN_EGG =
			spawnEgg("fizzball_fish_spawn_egg",
					CakeWorldEntities.FIZZBALL_FISH,
					0xF6D34A, 0x6ACBE8);
	public static final RegistryObject<Item> JELLYBEAN_FISH_SPAWN_EGG =
			spawnEgg("jellybean_fish_spawn_egg",
					CakeWorldEntities.JELLYBEAN_FISH,
					0xF06FB2, 0x67DCE5);
	public static final RegistryObject<Item> WAFER_TURTLE_SPAWN_EGG =
			spawnEgg("wafer_turtle_spawn_egg",
					CakeWorldEntities.WAFER_TURTLE,
					0xD8B77B, 0x78C9B5);
	public static final RegistryObject<Item> SODA_DOLPHIN_SPAWN_EGG =
			spawnEgg("soda_dolphin_spawn_egg",
					CakeWorldEntities.SODA_DOLPHIN,
					0x2E76A8, 0xFFF080);
	public static final RegistryObject<Item> DOUGH_DONKEY_SPAWN_EGG =
			spawnEgg("dough_donkey_spawn_egg",
					CakeWorldEntities.DOUGH_DONKEY,
					0xB47A43, 0xF1D39A);
	public static final RegistryObject<Item> MARZIPAN_MULE_SPAWN_EGG =
			spawnEgg("marzipan_mule_spawn_egg",
					CakeWorldEntities.MARZIPAN_MULE,
					0xE2B77E, 0x8B5A3C);
	public static final RegistryObject<Item> SOGGY_BISCUIT_SPAWN_EGG =
			spawnEgg("soggy_biscuit_spawn_egg",
					CakeWorldEntities.SOGGY_BISCUIT,
					0x9A7042, 0x5FA6A0);
	public static final RegistryObject<Item>
			GRAND_GUMBALL_GUARDIAN_SPAWN_EGG =
			spawnEgg("grand_gumball_guardian_spawn_egg",
					CakeWorldEntities.GRAND_GUMBALL_GUARDIAN,
					0xA563C7, 0xF4D35E);
	public static final RegistryObject<Item> GUMBALL_GUARDIAN_SPAWN_EGG =
			spawnEgg("gumball_guardian_spawn_egg",
					CakeWorldEntities.GUMBALL_GUARDIAN,
					0x52BFD5, 0xFF75B5);
	public static final RegistryObject<Item> FUDGE_BOAR_SPAWN_EGG =
			spawnEgg("fudge_boar_spawn_egg",
					CakeWorldEntities.FUDGE_BOAR,
					0x5B2A22, 0xD78A52);
	public static final RegistryObject<Item>
			STALE_FUDGE_BOAR_SPAWN_EGG =
			spawnEgg("stale_fudge_boar_spawn_egg",
					CakeWorldEntities.STALE_FUDGE_BOAR,
					0x4A211B, 0xA66C43);
	public static final RegistryObject<Item> FUDGE_FOLK_SPAWN_EGG =
			spawnEgg("fudge_folk_spawn_egg",
					CakeWorldEntities.FUDGE_FOLK,
					0x4A1F18, 0xE0A25B);
	public static final RegistryObject<Item> FUDGE_BRUTE_SPAWN_EGG =
			spawnEgg("fudge_brute_spawn_egg",
					CakeWorldEntities.FUDGE_BRUTE,
					0x35140F, 0xC8833A);
	public static final RegistryObject<Item>
			STALE_FUDGE_FOLK_SPAWN_EGG =
			spawnEgg("stale_fudge_folk_spawn_egg",
					CakeWorldEntities.STALE_FUDGE_FOLK,
					0xEA9393, 0x4C7129);
	public static final RegistryObject<Item> BISCUIT_BANDIT_SPAWN_EGG =
			spawnEgg("biscuit_bandit_spawn_egg",
					CakeWorldEntities.BISCUIT_BANDIT,
					0x7A5532, 0xE3C58F);
	public static final RegistryObject<Item>
			ROLLING_PIN_RAIDER_SPAWN_EGG =
			spawnEgg("rolling_pin_raider_spawn_egg",
					CakeWorldEntities.ROLLING_PIN_RAIDER,
					0x765A45, 0xD9B77A);
	public static final RegistryObject<Item>
			BITTER_BAKER_SPAWN_EGG =
			spawnEgg("bitter_baker_spawn_egg",
					CakeWorldEntities.BITTER_BAKER,
					0x4A3947, 0xC98B5D);
	public static final RegistryObject<Item>
			BURNT_SUGAR_TEMPEST_SPAWN_EGG =
			spawnEgg("burnt_sugar_tempest_spawn_egg",
					CakeWorldEntities.BURNT_SUGAR_TEMPEST,
					0x2B1210, 0xD16A32);
	public static final RegistryObject<Item>
			BURNT_CANDY_KNIGHT_SPAWN_EGG =
			spawnEgg("burnt_candy_knight_spawn_egg",
					CakeWorldEntities.BURNT_CANDY_KNIGHT,
					0x2B211F, 0xB85A3C);
	public static final RegistryObject<Item> VANILLA_ICE_BEAR_SPAWN_EGG =
			spawnEgg("vanilla_ice_bear_spawn_egg",
					CakeWorldEntities.VANILLA_ICE_BEAR,
					0xF4E8C8, 0xD8B778);
	public static final RegistryObject<Item> GUMMY_BUNNY_SPAWN_EGG =
			spawnEgg("gummy_bunny_spawn_egg",
					CakeWorldEntities.GUMMY_BUNNY,
					0xE85BB5, 0x6DDC7A);
	public static final RegistryObject<Item>
			GINGERBREAD_STOMPER_SPAWN_EGG =
			spawnEgg("gingerbread_stomper_spawn_egg",
					CakeWorldEntities.GINGERBREAD_STOMPER,
					0x9B5A2E, 0xF1E0B0);
	public static final RegistryObject<Item> GINGERBREAD_PONY_SPAWN_EGG =
			spawnEgg("gingerbread_pony_spawn_egg",
					CakeWorldEntities.GINGERBREAD_PONY,
					0xB06B3D, 0xFFF0D2);
	public static final RegistryObject<Item> MERINGUE_LLAMA_SPAWN_EGG =
			spawnEgg("meringue_llama_spawn_egg",
					CakeWorldEntities.MERINGUE_LLAMA,
					0xFFF5E6, 0xF4B6D2);
	public static final RegistryObject<Item> SPRINKLE_LLAMA_SPAWN_EGG =
			spawnEgg("sprinkle_llama_spawn_egg",
					CakeWorldEntities.SPRINKLE_LLAMA,
					0xD99B63, 0xF26BAA);
	public static final RegistryObject<Item> HOT_FUDGE_BLOB_SPAWN_EGG =
			spawnEgg("hot_fudge_blob_spawn_egg",
					CakeWorldEntities.HOT_FUDGE_BLOB,
					0x3B160F, 0xE06B37);
	public static final RegistryObject<Item> DRIED_CRUMBLER_SPAWN_EGG =
			spawnEgg("dried_crumbler_spawn_egg",
					CakeWorldEntities.DRIED_CRUMBLER,
					0xD9B57A, 0x6E5C3B);
	public static final RegistryObject<Item> TAFFY_TALLWALKER_SPAWN_EGG =
			spawnEgg("taffy_tallwalker_spawn_egg",
					CakeWorldEntities.TAFFY_TALLWALKER,
					0xB77ADA, 0xF7C6E0);
	public static final RegistryObject<Item> MACARON_CLAM_SPAWN_EGG =
			spawnEgg("macaron_clam_spawn_egg",
					CakeWorldEntities.MACARON_CLAM,
					0xF2A7C6, 0xFFF0D2);
	public static final RegistryObject<Item> CRUMB_MITE_SPAWN_EGG =
			spawnEgg("crumb_mite_spawn_egg",
					CakeWorldEntities.CRUMB_MITE,
					0xD8B77B, 0x6B4427);
	public static final RegistryObject<Item>
			CANDY_CANE_ARCHER_SPAWN_EGG =
			spawnEgg("candy_cane_archer_spawn_egg",
					CakeWorldEntities.CANDY_CANE_ARCHER,
					0xFFF4E5, 0xC8202F);
	public static final RegistryObject<Item>
			FROSTED_ARCHER_SPAWN_EGG =
			spawnEgg("frosted_archer_spawn_egg",
					CakeWorldEntities.FROSTED_ARCHER,
					0xD7ECF5, 0x7EA7C4);
	public static final RegistryObject<Item>
			FUDGE_SKATER_SPAWN_EGG =
			spawnEgg("fudge_skater_spawn_egg",
					CakeWorldEntities.FUDGE_SKATER,
					0x4A1E16, 0xE8B06A);
	public static final RegistryObject<Item>
			BRITTLE_BISCUIT_STEED_SPAWN_EGG =
			spawnEgg("brittle_biscuit_steed_spawn_egg",
					CakeWorldEntities.BRITTLE_BISCUIT_STEED,
					0xD9B57A, 0xF5E0B7);
	public static final RegistryObject<Item>
			STALE_GINGERBREAD_STEED_SPAWN_EGG =
			spawnEgg("stale_gingerbread_steed_spawn_egg",
					CakeWorldEntities.STALE_GINGERBREAD_STEED,
					0x315234, 0x97C284);
	public static final RegistryObject<Item>
			JELLY_BLOB_SPAWN_EGG =
			spawnEgg("jelly_blob_spawn_egg",
					CakeWorldEntities.JELLY_BLOB,
					0x51A03E, 0x7EBF6E);
	public static final RegistryObject<Item>
			ICE_CREAM_GOLEM_SPAWN_EGG =
			spawnEgg("ice_cream_golem_spawn_egg",
					CakeWorldEntities.ICE_CREAM_GOLEM,
					0xFFF4E6, 0xF3A7C8);
	public static final RegistryObject<Item>
			LIQUORICE_WEAVER_SPAWN_EGG =
			spawnEgg("liquorice_weaver_spawn_egg",
					CakeWorldEntities.LIQUORICE_WEAVER,
					0x1B1026, 0xB94A8C);
	public static final RegistryObject<Item>
			LIQUORICE_SQUID_SPAWN_EGG =
			spawnEgg("liquorice_squid_spawn_egg",
					CakeWorldEntities.LIQUORICE_SQUID,
					0x17101F, 0x8D5A9F);
	public static final RegistryObject<Item> SUGAR_MITE_SPAWN_EGG =
			spawnEgg("sugar_mite_spawn_egg",
					CakeWorldEntities.SUGAR_MITE,
					0xE8D7FF, 0xB86BDA);
	public static final RegistryObject<Item> SOUR_SORCERER_SPAWN_EGG =
			spawnEgg("sour_sorcerer_spawn_egg",
					CakeWorldEntities.SOUR_SORCERER,
					0x6B345F, 0xB7F04A);
	public static final RegistryObject<Item> SOUR_SPRITE_SPAWN_EGG =
			spawnEgg("sour_sprite_spawn_egg",
					CakeWorldEntities.SOUR_SPRITE,
					0xC9F06A, 0xA83B62);
	public static final RegistryObject<Item> GINGERBREAD_FOLK_SPAWN_EGG =
			spawnEgg("gingerbread_folk_spawn_egg",
					CakeWorldEntities.GINGERBREAD_FOLK,
					0xA86232, 0xF2E2B8);
	public static final RegistryObject<Item>
			TRAVELLING_CONFECTIONER_SPAWN_EGG =
			spawnEgg("travelling_confectioner_spawn_egg",
					CakeWorldEntities
							.TRAVELLING_CONFECTIONER,
					0x6E4C3A, 0xE9C86F);
	public static final RegistryObject<Item> PEPPERMINT_FOX_SPAWN_EGG =
			spawnEgg("peppermint_fox_spawn_egg",
					CakeWorldEntities.PEPPERMINT_FOX,
					0xF4F6F2, 0xC73D4E);
	public static final RegistryObject<Item>
			GINGER_SNAP_HOUND_SPAWN_EGG =
			spawnEgg("ginger_snap_hound_spawn_egg",
					CakeWorldEntities.GINGER_SNAP_HOUND,
					0xA86232, 0xF4E0B5);
	public static final RegistryObject<Item> MALLOW_FLOATER_SPAWN_EGG =
			spawnEgg("mallow_floater_spawn_egg",
					CakeWorldEntities.MALLOW_FLOATER,
					0xFFF6F2, 0xF3A9C4);
	public static final RegistryObject<Item> GLOW_JELLY_SPAWN_EGG =
			spawnEgg("glow_jelly_spawn_egg",
					CakeWorldEntities.GLOW_JELLY,
					0x124E78, 0x86F7E8);
	public static final RegistryObject<Item> NOUGAT_GOAT_SPAWN_EGG =
			spawnEgg("nougat_goat_spawn_egg",
					CakeWorldEntities.NOUGAT_GOAT,
					0xE8D2A2, 0x8A5C3A);
	public static final RegistryObject<Item> POP_ROCK_POPPER_SPAWN_EGG =
			spawnEgg("pop_rock_popper_spawn_egg",
					CakeWorldEntities.POP_ROCK_POPPER,
					0xF05A8A, 0xFFF16B);
	public static final RegistryObject<Item> CINNAMON_SPARK_SPAWN_EGG =
			spawnEgg("cinnamon_spark_spawn_egg",
					CakeWorldEntities.CINNAMON_SPARK,
					0xC85A24, 0xFFD36B);
	public static final RegistryObject<Item> COCOA_COW_SPAWN_EGG =
			spawnEgg("cocoa_cow_spawn_egg", CakeWorldEntities.COCOA_COW,
					0x5A2E1D, 0xE7C28E);
	public static final RegistryObject<Item> CUPCAKE_COW_SPAWN_EGG =
			spawnEgg("cupcake_cow_spawn_egg",
					CakeWorldEntities.CUPCAKE_COW,
					0xD84C76, 0xFFF1D6);
	public static final RegistryObject<Item> MALLOW_CHICK_SPAWN_EGG =
			spawnEgg("mallow_chick_spawn_egg", CakeWorldEntities.MALLOW_CHICK,
					0xFFF4E0, 0xF2A7C6);
	public static final RegistryObject<Item> TRUFFLE_PIG_SPAWN_EGG =
			spawnEgg("truffle_pig_spawn_egg", CakeWorldEntities.TRUFFLE_PIG,
					0x7A4935, 0xD9A07E);
	public static final RegistryObject<Item> CANDYFLOSS_SHEEP_SPAWN_EGG =
			spawnEgg("candyfloss_sheep_spawn_egg", CakeWorldEntities.CANDYFLOSS_SHEEP,
					0xF7B7D2, 0xFFF6FA);
	public static final RegistryObject<Item> STALE_CRUMBLER_SPAWN_EGG =
			spawnEgg("stale_crumbler_spawn_egg", CakeWorldEntities.STALE_CRUMBLER,
					0x8B6A45, 0x5A3A28);
	public static final RegistryObject<Item>
			CRUMBLED_GINGERBREAD_FOLK_SPAWN_EGG =
			spawnEgg("crumbled_gingerbread_folk_spawn_egg",
					CakeWorldEntities
							.CRUMBLED_GINGERBREAD_FOLK,
					0x563C33, 0x799C65);

	private CakeWorldItems() {
	}

	public static void register(IEventBus modBus) {
		ITEMS.register(modBus);
	}

	private static RegistryObject<Item> food(String name, int nutrition, float saturation) {
		return ITEMS.register(name, () -> new Item(foodProperties(nutrition, saturation)));
	}

	private static RegistryObject<Item> spawnEgg(String name,
			Supplier<? extends EntityType<? extends Mob>> type, int primary, int spots) {
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(type, primary, spots,
				new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
	}

	private static Item.Properties foodProperties(int nutrition, float saturation) {
		return new Item.Properties()
				.tab(CreativeModeTab.TAB_FOOD)
				.food(new FoodProperties.Builder()
						.nutrition(nutrition)
						.saturationMod(saturation)
						.build());
	}
}
