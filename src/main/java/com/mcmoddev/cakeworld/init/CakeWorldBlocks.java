package com.mcmoddev.cakeworld.init;

import java.util.function.Supplier;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.CakeOvenBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.CookbookKioskBlock;
import com.mcmoddev.cakeworld.block.GummyBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.block.MarshmallowBlock;
import com.mcmoddev.cakeworld.block.MixingBowlBlock;
import com.mcmoddev.cakeworld.item.ReusableBlockItem;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
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
			() -> new ChocolateSpongeBlock(BlockBehaviour.Properties.of(Material.DIRT)
					.strength(0.6F).sound(SoundType.ROOTED_DIRT)));
	// Revision-1 OreSpawn snapshots can contain these registry IDs as full rock
	// strata. Keep their physical contracts stable and use new IDs for layers
	// and falling terrain so existing worlds cannot become hollow or collapse.
	public static final RegistryObject<Block> ICING = block("icing",
			BlockBehaviour.Properties.of(Material.SNOW).strength(0.25F).sound(SoundType.SNOW));
	public static final RegistryObject<Block> ICING_LAYER = block("icing_layer",
			() -> new IcingLayerBlock(BlockBehaviour.Properties.of(Material.SNOW)
					.randomTicks().strength(0.25F).sound(SoundType.SNOW).noOcclusion()));
	public static final RegistryObject<Block> BISCUIT_STONE = block("biscuit_stone",
			BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(1.5F, 6.0F).sound(SoundType.STONE));
	public static final RegistryObject<Block> BISCUIT_SAND = block("biscuit_sand",
			BlockBehaviour.Properties.of(Material.SAND).strength(0.5F).sound(SoundType.SAND));
	public static final RegistryObject<Block> BISCUIT_CRUMBS = block("biscuit_crumbs",
			() -> new BiscuitCrumbsBlock(BlockBehaviour.Properties.of(Material.SAND)
					.strength(0.5F).sound(SoundType.SAND)));
	public static final RegistryObject<Block> FROZEN_LEMONADE = block("frozen_lemonade",
			BlockBehaviour.Properties.of(Material.ICE_SOLID).friction(0.96F)
					.strength(0.5F).sound(SoundType.GLASS).noOcclusion());
	public static final RegistryObject<Block> MARSHMALLOW = block("marshmallow",
			() -> new MarshmallowBlock(BlockBehaviour.Properties.of(Material.WOOL)
					.strength(0.4F).sound(SoundType.WOOL)));
	public static final RegistryObject<Block> GUMMY_BLOCK = block("gummy_block",
			() -> new GummyBlock(BlockBehaviour.Properties.of(Material.CLAY)
					.strength(0.5F).sound(SoundType.SLIME_BLOCK)
					.noOcclusion()));
	public static final RegistryObject<Block> CANDY_CANE_PILLAR =
			block("candy_cane_pillar",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.of(Material.WOOD)
									.strength(1.5F)
									.sound(SoundType.BAMBOO)));
	public static final RegistryObject<Block> GINGERBREAD_BRICKS =
			block("gingerbread_bricks",
					BlockBehaviour.Properties.of(Material.STONE)
							.requiresCorrectToolForDrops()
							.strength(1.5F, 6.0F)
							.sound(SoundType.STONE));
	public static final RegistryObject<Block> FUDGE_ROCK = block("fudge_rock",
			BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(2.0F, 6.0F).sound(SoundType.NETHERRACK));
	public static final RegistryObject<Block> WAFER_ROCK = stone("wafer_rock", 1.0F);
	public static final RegistryObject<Block> NOUGAT_ROCK = stone("nougat_rock", 2.5F);
	public static final RegistryObject<Block> PEPPERMINT_ROCK =
			stone("peppermint_rock", 1.8F);
	public static final RegistryObject<Block> ROCK_CANDY = stone("rock_candy", 2.2F);
	public static final RegistryObject<Block> CANDY_GLASS = block("candy_glass",
			() -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
					.requiresCorrectToolForDrops().strength(1.2F, 6.0F)
					.sound(SoundType.GLASS).noOcclusion()));
	public static final RegistryObject<Block> BURNT_SUGAR_ROCK =
			stone("burnt_sugar_rock", 2.8F);

	public static final RegistryObject<Block> ROCK_CANDY_DEPOSIT =
			stone("rock_candy_deposit", 2.5F);
	public static final RegistryObject<Block> LIQUORICE_VEIN =
			stone("liquorice_vein", 2.0F);
	public static final RegistryObject<Block> COCOA_CLOUD =
			stone("cocoa_cloud", 1.5F);
	public static final RegistryObject<Block> MINT_CRYSTAL =
			stone("mint_crystal", 2.5F);
	public static final RegistryObject<Block> SPRINKLE_CLUSTER =
			stone("sprinkle_cluster", 1.8F);
	public static final RegistryObject<Block> RICH_SPRINKLE_CLUSTER =
			stone("rich_sprinkle_cluster", 2.0F);
	public static final RegistryObject<Block> FIZZY_PEARL =
			stone("fizzy_pearl", 2.5F);
	public static final RegistryObject<Block> COCOA_COAL =
			ore("cocoa_coal", UniformInt.of(0, 2));
	public static final RegistryObject<Block> IRON_WAFER =
			ore("iron_wafer");
	public static final RegistryObject<Block> COPPER_CARAMEL =
			ore("copper_caramel");
	public static final RegistryObject<Block> HONEYCOMB_GOLD =
			ore("honeycomb_gold");
	public static final RegistryObject<Block> RASPBERRY_REDSTONE =
			block("raspberry_redstone", () -> new RedStoneOreBlock(
					oreProperties().randomTicks()
							.lightLevel(state -> state.getValue(
									RedStoneOreBlock.LIT) ? 9 : 0)));
	public static final RegistryObject<Block> BLUEBERRY_LAPIS =
			ore("blueberry_lapis", UniformInt.of(2, 5));
	public static final RegistryObject<Block> ROCK_CANDY_DIAMOND =
			ore("rock_candy_diamond", UniformInt.of(3, 7));
	public static final RegistryObject<Block> MINT_EMERALD =
			ore("mint_emerald", UniformInt.of(3, 7));
	public static final RegistryObject<Block> VANILLA_QUARTZ =
			ore("vanilla_quartz", UniformInt.of(2, 5));
	public static final RegistryObject<Block> FUDGE_GOLD =
			ore("fudge_gold", UniformInt.of(0, 1));
	public static final RegistryObject<Block> ANCIENT_NOUGAT =
			block("ancient_nougat", BlockBehaviour.Properties.of(Material.METAL)
					.requiresCorrectToolForDrops().strength(30.0F, 1200.0F)
					.sound(SoundType.ANCIENT_DEBRIS));
	public static final RegistryObject<Block> FROSTED_COLD_IRON =
			ore("frosted_cold_iron");
	public static final RegistryObject<Block> JAWBREAKER_ADAMANTINE =
			ore("jawbreaker_adamantine");
	public static final RegistryObject<Block> STARLIGHT_STARSTEEL =
			ore("starlight_starsteel");
	public static final RegistryObject<Block> SILVER_DRAGEE_TIN =
			ore("silver_dragee_tin");
	public static final RegistryObject<Block> LIQUORICE_LEAD =
			ore("liquorice_lead");
	public static final RegistryObject<Block> LEMON_DROP_ZINC =
			ore("lemon_drop_zinc");
	public static final RegistryObject<Block> SILVER_LEAF_SILVER =
			ore("silver_leaf_silver");
	public static final RegistryObject<Block> MIRROR_GLAZE_MERCURY =
			ore("mirror_glaze_mercury");
	public static final RegistryObject<Block> MINT_WAFER_NICKEL =
			ore("mint_wafer_nickel");
	public static final RegistryObject<Block> SUGAR_STAR_PLATINUM =
			ore("sugar_star_platinum");
	public static final RegistryObject<Block> ANISEED_ANTIMONY =
			ore("aniseed_antimony");
	public static final RegistryObject<Block> RAINBOW_ROCK_BISMUTH =
			ore("rainbow_rock_bismuth");
	public static final RegistryObject<Block> COOKBOOK_KIOSK = block("cookbook_kiosk",
			() -> new CookbookKioskBlock(BlockBehaviour.Properties.of(Material.WOOD)
					.strength(2.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIXING_BOWL = reusableBlock("mixing_bowl",
			() -> new MixingBowlBlock(BlockBehaviour.Properties.of(Material.WOOD)
					.strength(1.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> OVEN = block("oven",
			() -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE)
					.requiresCorrectToolForDrops().strength(3.0F, 6.0F)
					.sound(SoundType.STONE)));

	private CakeWorldBlocks() {
	}

	public static void register(IEventBus modBus) {
		BLOCKS.register(modBus);
		ITEMS.register(modBus);
	}

	private static RegistryObject<Block> block(String name, BlockBehaviour.Properties properties) {
		return block(name, () -> new Block(properties));
	}

	private static RegistryObject<Block> block(String name,
			Supplier<Block> supplier) {
		RegistryObject<Block> block = BLOCKS.register(name, supplier);
		ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
		return block;
	}

	private static RegistryObject<Block> reusableBlock(String name,
			Supplier<Block> supplier) {
		RegistryObject<Block> block = BLOCKS.register(name, supplier);
		ITEMS.register(name, () -> new ReusableBlockItem(block.get(),
				new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)
						.stacksTo(1)));
		return block;
	}

	private static RegistryObject<Block> stone(String name, float strength) {
		return block(name, BlockBehaviour.Properties.of(Material.STONE)
				.requiresCorrectToolForDrops().strength(strength, 6.0F)
				.sound(SoundType.STONE));
	}

	private static RegistryObject<Block> ore(String name) {
		return block(name, oreProperties());
	}

	private static RegistryObject<Block> ore(String name,
			UniformInt experience) {
		return block(name, () -> new OreBlock(oreProperties(), experience));
	}

	private static BlockBehaviour.Properties oreProperties() {
		return BlockBehaviour.Properties.of(Material.STONE)
				.requiresCorrectToolForDrops().strength(3.0F, 3.0F)
				.sound(SoundType.STONE);
	}
}
