package com.mcmoddev.cakeworld.init;

import java.util.function.Supplier;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.CakeOvenBlock;
import com.mcmoddev.cakeworld.block.CandyCookerBlock;
import com.mcmoddev.cakeworld.block.CandySproutBlock;
import com.mcmoddev.cakeworld.block.CaramelCrustBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.CinnamonWartBlock;
import com.mcmoddev.cakeworld.block.CookbookKioskBlock;
import com.mcmoddev.cakeworld.block.CookbookLibraryBlock;
import com.mcmoddev.cakeworld.block.CoolingRackBlock;
import com.mcmoddev.cakeworld.block.GummyBlock;
import com.mcmoddev.cakeworld.block.GummyVineBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.block.LollipopFruitBlock;
import com.mcmoddev.cakeworld.block.MarshmallowBlock;
import com.mcmoddev.cakeworld.block.MixingBowlBlock;
import com.mcmoddev.cakeworld.block.SherbetPowderBlock;
import com.mcmoddev.cakeworld.block.SodaFountainBlock;
import com.mcmoddev.cakeworld.block.TreacleReedBlock;
import com.mcmoddev.cakeworld.block.WaferWindmillBlock;
import com.mcmoddev.cakeworld.item.ReusableBlockItem;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
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
	public static final RegistryObject<Block> CRUMB_MITE_NEST =
			block("crumb_mite_nest",
					() -> new InfestedBlock(
							BISCUIT_STONE.get(),
							BlockBehaviour.Properties
									.of(Material.CLAY)
									.sound(SoundType.STONE)));
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
	public static final RegistryObject<Block> GUMMY_BLOCK = gummy("gummy_block");
	public static final RegistryObject<Block> RASPBERRY_GUMMY_BLOCK =
			gummy("raspberry_gummy_block");
	public static final RegistryObject<Block> BLUEBERRY_GUMMY_BLOCK =
			gummy("blueberry_gummy_block");
	public static final RegistryObject<Block> GRAPE_GUMMY_BLOCK =
			gummy("grape_gummy_block");
	public static final RegistryObject<Block> GUMMY_VINE =
			block("gummy_vine",
					() -> new GummyVineBlock(
							BlockBehaviour.Properties
									.of(Material
											.REPLACEABLE_PLANT)
									.noCollission()
									.instabreak()
									.sound(SoundType
											.SLIME_BLOCK)));
	public static final RegistryObject<Block> CARAMEL_CRUST =
			block("caramel_crust",
					() -> new CaramelCrustBlock(
							BlockBehaviour.Properties
									.of(Material.CLAY)
									.strength(0.5F)
									.sound(SoundType.HONEY_BLOCK)));
	public static final RegistryObject<Block> TREACLE_REED =
			block("treacle_reed",
					() -> new TreacleReedBlock(
							BlockBehaviour.Properties
									.of(Material.PLANT)
									.noCollission()
									.instabreak()
									.sound(SoundType.CROP)));
	public static final RegistryObject<Block> RASPBERRY_SHERBET_POWDER =
			sherbetPowder("raspberry_sherbet_powder");
	public static final RegistryObject<Block> ORANGE_SHERBET_POWDER =
			sherbetPowder("orange_sherbet_powder");
	public static final RegistryObject<Block> LEMON_SHERBET_POWDER =
			sherbetPowder("lemon_sherbet_powder");
	public static final RegistryObject<Block> LIME_SHERBET_POWDER =
			sherbetPowder("lime_sherbet_powder");
	public static final RegistryObject<Block> WAFER_BLOCK = block("wafer_block",
			BlockBehaviour.Properties.of(Material.WOOD)
					.strength(0.2F, 0.2F)
					.sound(SoundType.BAMBOO));
	public static final RegistryObject<Block> CANDY_SPROUT =
			BLOCKS.register("candy_sprout",
					() -> new CandySproutBlock(
							BlockBehaviour.Properties.of(Material.PLANT)
									.noCollission().randomTicks().instabreak()
									.sound(SoundType.CROP)));
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
	public static final RegistryObject<Block> LIQUORICE_LOAM =
			block("liquorice_loam",
					BlockBehaviour.Properties.of(Material.DIRT)
							.strength(0.6F)
							.sound(SoundType.ROOTED_DIRT));
	public static final RegistryObject<Block> CANDIED_SOIL =
			block("candied_soil",
					BlockBehaviour.Properties.of(Material.DIRT)
							.strength(0.6F)
							.sound(SoundType.ROOTED_DIRT));
	public static final RegistryObject<Block> LOLLIPOP_FRUIT =
			block("lollipop_fruit",
					() -> new LollipopFruitBlock(
							BlockBehaviour.Properties
									.of(Material.LEAVES)
									.randomTicks()
									.strength(0.3F)
									.sound(SoundType
											.SLIME_BLOCK)));
	public static final RegistryObject<Block> LIQUORICE_ROOT =
			block("liquorice_root",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.of(Material.WOOD)
									.strength(1.2F)
									.sound(SoundType.WOOD)));
	public static final RegistryObject<Block> LIQUORICE_BRICKS =
			block("liquorice_bricks",
					BlockBehaviour.Properties
							.copy(Blocks.POLISHED_BLACKSTONE_BRICKS));
	public static final RegistryObject<Block> LIQUORICE_STAIRS =
			block("liquorice_stairs",
					() -> new StairBlock(
							() -> LIQUORICE_BRICKS.get()
									.defaultBlockState(),
							BlockBehaviour.Properties.copy(
									Blocks
											.POLISHED_BLACKSTONE_BRICK_STAIRS)));
	public static final RegistryObject<Block> LIQUORICE_FENCE =
			block("liquorice_fence",
					() -> new FenceBlock(
							BlockBehaviour.Properties.copy(
									Blocks.NETHER_BRICK_FENCE)));
	public static final RegistryObject<Block> CINNAMON_WART =
			BLOCKS.register("cinnamon_wart",
					() -> new CinnamonWartBlock(
							BlockBehaviour.Properties.copy(
									Blocks.NETHER_WART)));
	public static final RegistryObject<Block> MACARON_BRICKS =
			block("macaron_bricks",
					BlockBehaviour.Properties.copy(
							Blocks.PURPUR_BLOCK));
	public static final RegistryObject<Block> MACARON_PILLAR =
			block("macaron_pillar",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.copy(
									Blocks.PURPUR_PILLAR)));
	public static final RegistryObject<Block> MACARON_STAIRS =
			block("macaron_stairs",
					() -> new StairBlock(
							() -> MACARON_BRICKS.get()
									.defaultBlockState(),
							BlockBehaviour.Properties.copy(
									Blocks.PURPUR_STAIRS)));
	public static final RegistryObject<Block> MACARON_SLAB =
			block("macaron_slab",
					() -> new SlabBlock(
							BlockBehaviour.Properties.copy(
									Blocks.PURPUR_SLAB)));
	public static final RegistryObject<Block> MERINGUE_BRICKS =
			block("meringue_bricks",
					BlockBehaviour.Properties.copy(
							Blocks.END_STONE_BRICKS));
	public static final RegistryObject<Block> WAFER_PILLAR =
			block("wafer_pillar",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.copy(
									WAFER_BLOCK.get())));
	public static final RegistryObject<Block> WAFER_STAIRS =
			block("wafer_stairs",
					() -> new StairBlock(
							() -> WAFER_BLOCK.get()
									.defaultBlockState(),
							BlockBehaviour.Properties.copy(
									WAFER_BLOCK.get())));
	public static final RegistryObject<Block> WAFER_SLAB =
			block("wafer_slab",
					() -> new SlabBlock(
							BlockBehaviour.Properties.copy(
									WAFER_BLOCK.get())));
	public static final RegistryObject<Block> FUDGE_ROCK = block("fudge_rock",
			BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
					.strength(2.0F, 6.0F).sound(SoundType.NETHERRACK));
	public static final RegistryObject<Block> WAFER_ROCK = stone("wafer_rock", 1.0F);
	public static final RegistryObject<Block> NOUGAT_ROCK = stone("nougat_rock", 2.5F);
	public static final RegistryObject<Block> PEPPERMINT_ROCK =
			stone("peppermint_rock", 1.8F);
	public static final RegistryObject<Block> ROCK_CANDY = stone("rock_candy", 2.2F);
	public static final RegistryObject<Block> ROCK_CANDY_FOSSIL =
			block("rock_candy_fossil",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.copy(
									Blocks.BONE_BLOCK)));
	public static final RegistryObject<Block> CANDY_GLASS = block("candy_glass",
			() -> new GlassBlock(BlockBehaviour.Properties.of(Material.GLASS)
					.requiresCorrectToolForDrops().strength(1.2F, 6.0F)
					.sound(SoundType.GLASS).noOcclusion()));
	public static final RegistryObject<Block> BURNT_SUGAR_ROCK =
			stone("burnt_sugar_rock", 2.8F);
	public static final RegistryObject<Block> BURNT_TOFFEE_BRICKS =
			block("burnt_toffee_bricks",
					BlockBehaviour.Properties.copy(
							Blocks.POLISHED_BLACKSTONE_BRICKS));
	public static final RegistryObject<Block> CRACKED_BURNT_TOFFEE_BRICKS =
			block("cracked_burnt_toffee_bricks",
					BlockBehaviour.Properties.copy(
							Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS));
	public static final RegistryObject<Block> BURNT_TOFFEE_STAIRS =
			block("burnt_toffee_stairs",
					() -> new StairBlock(
							() -> BURNT_TOFFEE_BRICKS.get()
									.defaultBlockState(),
							BlockBehaviour.Properties.copy(
									Blocks
											.POLISHED_BLACKSTONE_BRICK_STAIRS)));
	public static final RegistryObject<Block> BURNT_TOFFEE_SLAB =
			block("burnt_toffee_slab",
					() -> new SlabBlock(
							BlockBehaviour.Properties.copy(
									Blocks.BLACKSTONE_SLAB)));
	public static final RegistryObject<Block> BURNT_TOFFEE_WALL =
			block("burnt_toffee_wall",
					() -> new WallBlock(
							BlockBehaviour.Properties.copy(
									Blocks.BLACKSTONE_WALL)));
	public static final RegistryObject<Block> STAMPED_BURNT_TOFFEE =
			block("stamped_burnt_toffee",
					BlockBehaviour.Properties.copy(
							Blocks.CHISELED_POLISHED_BLACKSTONE));
	public static final RegistryObject<Block> GILDED_BURNT_TOFFEE =
			block("gilded_burnt_toffee",
					BlockBehaviour.Properties.copy(
							Blocks.GILDED_BLACKSTONE));
	public static final RegistryObject<Block> BURNT_TOFFEE_PILLAR =
			block("burnt_toffee_pillar",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.copy(
									Blocks.BASALT)));

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
	public static final RegistryObject<Block> COOKBOOK_LIBRARY =
			block("cookbook_library",
					() -> new CookbookLibraryBlock(
							BlockBehaviour.Properties.of(Material.WOOD)
									.strength(2.0F)
									.sound(SoundType.WOOD)));
	public static final RegistryObject<Block> MIXING_BOWL = reusableBlock("mixing_bowl",
			() -> new MixingBowlBlock(BlockBehaviour.Properties.of(Material.WOOD)
					.strength(1.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> OVEN = block("oven",
			() -> new CakeOvenBlock(BlockBehaviour.Properties.of(Material.STONE)
					.requiresCorrectToolForDrops().strength(3.0F, 6.0F)
					.sound(SoundType.STONE)));
	public static final RegistryObject<Block> COOLING_RACK =
			block("cooling_rack",
					() -> new CoolingRackBlock(
							BlockBehaviour.Properties.of(Material.METAL)
									.strength(1.0F)
									.sound(SoundType.METAL)
									.noOcclusion()));
	public static final RegistryObject<Block> CANDY_COOKER =
			block("candy_cooker",
					() -> new CandyCookerBlock(
							BlockBehaviour.Properties.of(Material.METAL)
									.requiresCorrectToolForDrops()
									.strength(2.5F, 6.0F)
									.sound(SoundType.METAL)));
	public static final RegistryObject<Block> SODA_FOUNTAIN =
			block("soda_fountain",
					() -> new SodaFountainBlock(
							BlockBehaviour.Properties.of(Material.METAL)
									.requiresCorrectToolForDrops()
									.strength(2.0F, 6.0F)
									.sound(SoundType.METAL)));
	public static final RegistryObject<Block> WAFER_WINDMILL =
			block("wafer_windmill",
					() -> new WaferWindmillBlock(
							BlockBehaviour.Properties.of(Material.WOOD)
									.strength(1.5F, 3.0F)
									.sound(SoundType.WOOD)));
	public static final RegistryObject<Block> SYRUP_PIPE =
			block("syrup_pipe",
					() -> new RotatedPillarBlock(
							BlockBehaviour.Properties.of(Material.METAL)
									.requiresCorrectToolForDrops()
									.strength(2.0F, 6.0F)
									.sound(SoundType.COPPER)));

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

	private static RegistryObject<Block> gummy(String name) {
		return block(name,
				() -> new GummyBlock(BlockBehaviour.Properties.of(Material.CLAY)
						.strength(0.5F).sound(SoundType.SLIME_BLOCK)
						.noOcclusion()));
	}

	private static RegistryObject<Block> sherbetPowder(String name) {
		return block(name,
				() -> new SherbetPowderBlock(
						BlockBehaviour.Properties.of(Material.SAND)
								.strength(0.45F)
								.sound(SoundType.SAND)));
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
