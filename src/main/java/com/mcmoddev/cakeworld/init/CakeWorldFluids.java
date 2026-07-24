package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.CakeLiquidBlock;
import com.mcmoddev.cakeworld.item.CakeBucketItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldFluids {
	private static final DeferredRegister<Fluid> FLUIDS =
			DeferredRegister.create(ForgeRegistries.FLUIDS, CakeWorld.MODID);
	private static final DeferredRegister<net.minecraft.world.level.block.Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, CakeWorld.MODID);
	private static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, CakeWorld.MODID);

	private static final ResourceLocation WATER_STILL =
			new ResourceLocation("minecraft", "block/water_still");
	private static final ResourceLocation WATER_FLOW =
			new ResourceLocation("minecraft", "block/water_flow");
	private static final ResourceLocation LAVA_STILL =
			new ResourceLocation("minecraft", "block/lava_still");
	private static final ResourceLocation LAVA_FLOW =
			new ResourceLocation("minecraft", "block/lava_flow");

	private static final ForgeFlowingFluid.Properties LEMONADE_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::lemonade,
					CakeWorldFluids::flowingLemonade,
					FluidAttributes.builder(WATER_STILL, WATER_FLOW)
							.color(0xFFFFD84D)
							.density(1000)
							.viscosity(1000)
							.temperature(295)
							.sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
					.bucket(CakeWorldFluids::lemonadeBucket)
					.block(CakeWorldFluids::lemonadeBlock);

	private static final ForgeFlowingFluid.Properties HOT_FUDGE_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::hotFudge,
					CakeWorldFluids::flowingHotFudge,
					FluidAttributes.builder(LAVA_STILL, LAVA_FLOW)
							.color(0xFF5B2B18)
							.density(2200)
							.viscosity(6500)
							.temperature(390)
							.luminosity(8)
							.sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA))
					.bucket(CakeWorldFluids::hotFudgeBucket)
					.block(CakeWorldFluids::hotFudgeBlock)
					.slopeFindDistance(2)
					.levelDecreasePerBlock(2)
					.tickRate(20);

	public static final RegistryObject<ForgeFlowingFluid.Source> LEMONADE =
			FLUIDS.register("lemonade", () -> new ForgeFlowingFluid.Source(LEMONADE_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_LEMONADE =
			FLUIDS.register("flowing_lemonade", () -> new ForgeFlowingFluid.Flowing(LEMONADE_PROPERTIES));
	public static final RegistryObject<LiquidBlock> LEMONADE_BLOCK = BLOCKS.register("lemonade",
			() -> new CakeLiquidBlock(CakeWorldFluids::lemonadeFlowing,
					BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
	public static final RegistryObject<Item> LEMONADE_BUCKET = ITEMS.register("lemonade_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::lemonade,
					new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
							.tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<ForgeFlowingFluid.Source> HOT_FUDGE =
			FLUIDS.register("hot_fudge", () -> new ForgeFlowingFluid.Source(HOT_FUDGE_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_HOT_FUDGE =
			FLUIDS.register("flowing_hot_fudge",
					() -> new ForgeFlowingFluid.Flowing(HOT_FUDGE_PROPERTIES));
	public static final RegistryObject<LiquidBlock> HOT_FUDGE_BLOCK = BLOCKS.register("hot_fudge",
			() -> new CakeLiquidBlock(CakeWorldFluids::hotFudgeFlowing,
					BlockBehaviour.Properties.of(Material.LAVA).noCollission().strength(100.0F)
							.lightLevel(state -> 8).noDrops()));
	public static final RegistryObject<Item> HOT_FUDGE_BUCKET = ITEMS.register("hot_fudge_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::hotFudge,
					new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
							.tab(CreativeModeTab.TAB_MISC)));

	private CakeWorldFluids() {
	}

	public static void register(IEventBus modBus) {
		FLUIDS.register(modBus);
		BLOCKS.register(modBus);
		ITEMS.register(modBus);
	}

	public static Fluid lemonade() { return LEMONADE.get(); }
	public static Fluid flowingLemonade() { return FLOWING_LEMONADE.get(); }
	public static FlowingFluid lemonadeFlowing() { return LEMONADE.get(); }
	public static LiquidBlock lemonadeBlock() { return LEMONADE_BLOCK.get(); }
	public static Item lemonadeBucket() { return LEMONADE_BUCKET.get(); }

	public static Fluid hotFudge() { return HOT_FUDGE.get(); }
	public static Fluid flowingHotFudge() { return FLOWING_HOT_FUDGE.get(); }
	public static FlowingFluid hotFudgeFlowing() { return HOT_FUDGE.get(); }
	public static LiquidBlock hotFudgeBlock() { return HOT_FUDGE_BLOCK.get(); }
	public static Item hotFudgeBucket() { return HOT_FUDGE_BUCKET.get(); }
}
