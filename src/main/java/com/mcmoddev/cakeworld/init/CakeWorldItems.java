package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.item.LemonadeBottleItem;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

	private CakeWorldItems() {
	}

	public static void register(IEventBus modBus) {
		ITEMS.register(modBus);
	}

	private static RegistryObject<Item> food(String name, int nutrition, float saturation) {
		return ITEMS.register(name, () -> new Item(foodProperties(nutrition, saturation)));
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
