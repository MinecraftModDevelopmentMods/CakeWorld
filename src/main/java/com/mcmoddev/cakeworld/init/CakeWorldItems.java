package com.mcmoddev.cakeworld.init;

import java.util.function.Supplier;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.item.ExplorersCookbookItem;
import com.mcmoddev.cakeworld.item.LemonadeBottleItem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
	public static final RegistryObject<Item> EXPLORERS_COOKBOOK =
			ITEMS.register("explorers_cookbook",
					() -> new ExplorersCookbookItem(new Item.Properties()
							.tab(CreativeModeTab.TAB_MISC).stacksTo(1)));
	public static final RegistryObject<Item> COCOA_COW_SPAWN_EGG =
			spawnEgg("cocoa_cow_spawn_egg", CakeWorldEntities.COCOA_COW,
					0x5A2E1D, 0xE7C28E);
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
