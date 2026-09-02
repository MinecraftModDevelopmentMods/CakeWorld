package zone.moddev.mc.cakeworld.init;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.CakeLiquidBlock;
import zone.moddev.mc.cakeworld.block.HotFudgeLiquidBlock;
import zone.moddev.mc.cakeworld.block.MoltenMallowLiquidBlock;
import zone.moddev.mc.cakeworld.item.CakeBucketItem;

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

	private static final ForgeFlowingFluid.Properties MOLTEN_MALLOW_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::moltenMallow,
					CakeWorldFluids::flowingMoltenMallow,
					FluidAttributes.builder(WATER_STILL, WATER_FLOW)
							.color(0xFFFFD9DF)
							.density(900)
							.viscosity(2400)
							.temperature(345)
							.luminosity(5)
							.sound(SoundEvents.BUCKET_FILL,
									SoundEvents.BUCKET_EMPTY))
					.bucket(CakeWorldFluids::moltenMallowBucket)
					.block(CakeWorldFluids::moltenMallowBlock)
					.slopeFindDistance(3)
					.levelDecreasePerBlock(2)
					.tickRate(12);

	private static final ForgeFlowingFluid.Properties JAM_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::jam,
					CakeWorldFluids::flowingJam,
					FluidAttributes.builder(WATER_STILL, WATER_FLOW)
							.color(0xFFC51D4A)
							.density(1350)
							.viscosity(3200)
							.temperature(295)
							.sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
					.bucket(CakeWorldFluids::jamBucket)
					.block(CakeWorldFluids::jamBlock)
					.slopeFindDistance(3)
					.levelDecreasePerBlock(2)
					.tickRate(12);

	private static final ForgeFlowingFluid.Properties CUSTARD_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::custard,
					CakeWorldFluids::flowingCustard,
					FluidAttributes.builder(WATER_STILL, WATER_FLOW)
							.color(0xFFFFD76A)
							.density(1250)
							.viscosity(2600)
							.temperature(300)
							.sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
					.bucket(CakeWorldFluids::custardBucket)
					.block(CakeWorldFluids::custardBlock)
					.slopeFindDistance(3)
					.levelDecreasePerBlock(2)
					.tickRate(10);

	private static final ForgeFlowingFluid.Properties CARAMEL_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::caramel,
					CakeWorldFluids::flowingCaramel,
					FluidAttributes.builder(WATER_STILL, WATER_FLOW)
							.color(0xFFC97A26)
							.density(1800)
							.viscosity(5200)
							.temperature(305)
							.sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
					.bucket(CakeWorldFluids::caramelBucket)
					.block(CakeWorldFluids::caramelBlock)
					.slopeFindDistance(2)
					.levelDecreasePerBlock(2)
					.tickRate(18);

	private static final ForgeFlowingFluid.Properties SYRUP_PROPERTIES =
			new ForgeFlowingFluid.Properties(CakeWorldFluids::syrup,
					CakeWorldFluids::flowingSyrup,
					FluidAttributes.builder(WATER_STILL, WATER_FLOW)
							.color(0xFFA85A24)
							.density(1650)
							.viscosity(4600)
							.temperature(300)
							.sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
					.bucket(CakeWorldFluids::syrupBucket)
					.block(CakeWorldFluids::syrupBlock)
					.slopeFindDistance(2)
					.levelDecreasePerBlock(2)
					.tickRate(16);

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
			() -> new HotFudgeLiquidBlock(CakeWorldFluids::hotFudgeFlowing,
					BlockBehaviour.Properties.of(Material.LAVA).noCollission().strength(100.0F)
							.lightLevel(state -> HotFudgeLiquidBlock.WARNING_LIGHT).noDrops()));
	public static final RegistryObject<Item> HOT_FUDGE_BUCKET = ITEMS.register("hot_fudge_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::hotFudge,
					new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
							.tab(CreativeModeTab.TAB_MISC)));

	public static final RegistryObject<ForgeFlowingFluid.Source> MOLTEN_MALLOW =
			FLUIDS.register("molten_mallow",
					() -> new ForgeFlowingFluid.Source(
							MOLTEN_MALLOW_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing>
			FLOWING_MOLTEN_MALLOW =
					FLUIDS.register("flowing_molten_mallow",
							() -> new ForgeFlowingFluid.Flowing(
									MOLTEN_MALLOW_PROPERTIES));
	public static final RegistryObject<LiquidBlock> MOLTEN_MALLOW_BLOCK =
			BLOCKS.register("molten_mallow",
					() -> new MoltenMallowLiquidBlock(
							CakeWorldFluids::moltenMallowFlowing,
							BlockBehaviour.Properties.of(Material.WATER)
									.noCollission().strength(100.0F)
									.lightLevel(state -> 5).noDrops()));
	public static final RegistryObject<Item> MOLTEN_MALLOW_BUCKET =
			ITEMS.register("molten_mallow_bucket",
					() -> new CakeBucketItem(CakeWorldFluids::moltenMallow,
							bucketProperties()));

	public static final RegistryObject<ForgeFlowingFluid.Source> JAM =
			FLUIDS.register("jam", () -> new ForgeFlowingFluid.Source(JAM_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_JAM =
			FLUIDS.register("flowing_jam",
					() -> new ForgeFlowingFluid.Flowing(JAM_PROPERTIES));
	public static final RegistryObject<LiquidBlock> JAM_BLOCK = BLOCKS.register("jam",
			() -> new CakeLiquidBlock(CakeWorldFluids::jamFlowing,
					edibleFluidProperties(), 0.4D, 0.7D));
	public static final RegistryObject<Item> JAM_BUCKET = ITEMS.register("jam_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::jam, bucketProperties()));

	public static final RegistryObject<ForgeFlowingFluid.Source> CUSTARD =
			FLUIDS.register("custard",
					() -> new ForgeFlowingFluid.Source(CUSTARD_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_CUSTARD =
			FLUIDS.register("flowing_custard",
					() -> new ForgeFlowingFluid.Flowing(CUSTARD_PROPERTIES));
	public static final RegistryObject<LiquidBlock> CUSTARD_BLOCK = BLOCKS.register("custard",
			() -> new CakeLiquidBlock(CakeWorldFluids::custardFlowing,
					edibleFluidProperties()));
	public static final RegistryObject<Item> CUSTARD_BUCKET = ITEMS.register("custard_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::custard, bucketProperties()));

	public static final RegistryObject<ForgeFlowingFluid.Source> CARAMEL =
			FLUIDS.register("caramel",
					() -> new ForgeFlowingFluid.Source(CARAMEL_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_CARAMEL =
			FLUIDS.register("flowing_caramel",
					() -> new ForgeFlowingFluid.Flowing(CARAMEL_PROPERTIES));
	public static final RegistryObject<LiquidBlock> CARAMEL_BLOCK = BLOCKS.register("caramel",
			() -> new CakeLiquidBlock(CakeWorldFluids::caramelFlowing,
					edibleFluidProperties(), 0.35D, 0.5D));
	public static final RegistryObject<Item> CARAMEL_BUCKET = ITEMS.register("caramel_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::caramel, bucketProperties()));

	public static final RegistryObject<ForgeFlowingFluid.Source> SYRUP =
			FLUIDS.register("syrup",
					() -> new ForgeFlowingFluid.Source(SYRUP_PROPERTIES));
	public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_SYRUP =
			FLUIDS.register("flowing_syrup",
					() -> new ForgeFlowingFluid.Flowing(SYRUP_PROPERTIES));
	public static final RegistryObject<LiquidBlock> SYRUP_BLOCK = BLOCKS.register("syrup",
			() -> new CakeLiquidBlock(CakeWorldFluids::syrupFlowing,
					edibleFluidProperties(), 0.55D, 0.7D));
	public static final RegistryObject<Item> SYRUP_BUCKET = ITEMS.register("syrup_bucket",
			() -> new CakeBucketItem(CakeWorldFluids::syrup, bucketProperties()));

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

	public static Fluid moltenMallow() { return MOLTEN_MALLOW.get(); }
	public static Fluid flowingMoltenMallow() {
		return FLOWING_MOLTEN_MALLOW.get();
	}
	public static FlowingFluid moltenMallowFlowing() {
		return MOLTEN_MALLOW.get();
	}
	public static LiquidBlock moltenMallowBlock() {
		return MOLTEN_MALLOW_BLOCK.get();
	}
	public static Item moltenMallowBucket() {
		return MOLTEN_MALLOW_BUCKET.get();
	}

	public static Fluid jam() { return JAM.get(); }
	public static Fluid flowingJam() { return FLOWING_JAM.get(); }
	public static FlowingFluid jamFlowing() { return JAM.get(); }
	public static LiquidBlock jamBlock() { return JAM_BLOCK.get(); }
	public static Item jamBucket() { return JAM_BUCKET.get(); }

	public static Fluid custard() { return CUSTARD.get(); }
	public static Fluid flowingCustard() { return FLOWING_CUSTARD.get(); }
	public static FlowingFluid custardFlowing() { return CUSTARD.get(); }
	public static LiquidBlock custardBlock() { return CUSTARD_BLOCK.get(); }
	public static Item custardBucket() { return CUSTARD_BUCKET.get(); }

	public static Fluid caramel() { return CARAMEL.get(); }
	public static Fluid flowingCaramel() { return FLOWING_CARAMEL.get(); }
	public static FlowingFluid caramelFlowing() { return CARAMEL.get(); }
	public static LiquidBlock caramelBlock() { return CARAMEL_BLOCK.get(); }
	public static Item caramelBucket() { return CARAMEL_BUCKET.get(); }

	public static Fluid syrup() { return SYRUP.get(); }
	public static Fluid flowingSyrup() { return FLOWING_SYRUP.get(); }
	public static FlowingFluid syrupFlowing() { return SYRUP.get(); }
	public static LiquidBlock syrupBlock() { return SYRUP_BLOCK.get(); }
	public static Item syrupBucket() { return SYRUP_BUCKET.get(); }

	private static BlockBehaviour.Properties edibleFluidProperties() {
		return BlockBehaviour.Properties.of(Material.WATER).noCollission()
				.strength(100.0F).noDrops();
	}

	private static Item.Properties bucketProperties() {
		return new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
				.tab(CreativeModeTab.TAB_MISC);
	}
}
