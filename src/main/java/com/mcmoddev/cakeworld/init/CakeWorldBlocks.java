package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldBlocks {
	private static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, CakeWorld.MODID);
	private static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, CakeWorld.MODID);

	public static final RegistryObject<Block> CHOCOLATE_SPONGE = block("chocolate_sponge",
			BlockBehaviour.Properties.of(Material.DIRT).strength(0.6F).sound(SoundType.ROOTED_DIRT));
	public static final RegistryObject<Block> ICING = block("icing",
			BlockBehaviour.Properties.of(Material.SNOW).strength(0.25F).sound(SoundType.SNOW));
	public static final RegistryObject<Block> BISCUIT_STONE = block("biscuit_stone",
			BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(1.5F, 6.0F).sound(SoundType.STONE));
	public static final RegistryObject<Block> BISCUIT_SAND = block("biscuit_sand",
			BlockBehaviour.Properties.of(Material.SAND).strength(0.5F).sound(SoundType.SAND));
	public static final RegistryObject<Block> FROZEN_LEMONADE = block("frozen_lemonade",
			BlockBehaviour.Properties.of(Material.ICE_SOLID).friction(0.98F)
					.strength(0.5F).sound(SoundType.GLASS).noOcclusion());
	public static final RegistryObject<Block> FUDGE_ROCK = block("fudge_rock",
			BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(2.0F, 6.0F).sound(SoundType.NETHERRACK));

	private CakeWorldBlocks() {
	}

	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
		ITEMS.register(modBus);
	}

	private static RegistryObject<Block> block(String name, BlockBehaviour.Properties properties) {
		RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));
		ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
		return block;
	}
}
