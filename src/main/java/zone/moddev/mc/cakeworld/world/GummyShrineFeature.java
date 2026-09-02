package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * First edible Jungle Temple counterpart.
 *
 * <p>The fixed first shrine is a compact gummy-roofed ruin with two harmless
 * sticky-splash tripwires, an elastic approach, a three-flavour piston clue,
 * and ordinary plus hidden treasure. Gummy Jungle can become its primary
 * home later without changing the saved structure identity.</p>
 */
public final class GummyShrineFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("gummy_shrine_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("gummy_shrine_structure");
	public static final ResourceLocation POOL_ID =
			id("gummy_shrine/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("gummy_shrine");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("gummy_shrines");
	public static final ResourceLocation LOOT_ID =
			id("chests/gummy_shrine");
	public static final ResourceLocation HIDDEN_LOOT_ID =
			id("chests/gummy_shrine_hidden");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(14, 11, 14);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/gummy_shrine"));
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_KEY =
			ResourceKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					STRUCTURE_ID);
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					STRUCTURE_ID);
	public static final GummyShrineFeature FEATURE =
			new GummyShrineFeature();
	public static final GummyShrineStructureFeature
			STRUCTURE_FEATURE =
			new GummyShrineStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private GummyShrineFeature() {
		super(NoneFeatureConfiguration.CODEC);
		setRegistryName(PIECE_ID);
	}

	public static void registerWorldgen() {
		Holder<ConfiguredFeature<?, ?>> configuredPiece =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						PIECE_ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								GummyShrineFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				PIECE_ID,
				new PlacedFeature(configuredPiece, List.of()));
		pool = BuiltinRegistries.register(
				BuiltinRegistries.TEMPLATE_POOL,
				POOL_ID,
				new StructureTemplatePool(
						POOL_ID,
						Pools.EMPTY.location(),
						List.of(Pair.of(
								CakeWorldFeaturePoolElement
										.of(placedFeature,
												MAXIMUM_OFFSET),
								1)),
						StructureTemplatePool
								.Projection.RIGID));
		configuredStructure = BuiltinRegistries.register(
				BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
				STRUCTURE_KEY,
				STRUCTURE_FEATURE.configured(
						new JigsawConfiguration(pool, 1),
						GENERATES_IN));
		structureSet = BuiltinRegistries.register(
				BuiltinRegistries.STRUCTURE_SETS,
				STRUCTURE_SET_ID,
				new StructureSet(
						configuredStructure,
						new RandomSpreadStructurePlacement(
								32, 8,
								RandomSpreadType.LINEAR,
								14357619)));
	}

	public static Holder<StructureTemplatePool> pool() {
		return pool;
	}

	public static Holder<StructureSet> structureSet() {
		return structureSet;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		return buildAt(context.level(), context.random(),
				context.origin().offset(7, 0, 7));
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		return buildAt(world, random,
				origin.offset(7, 0, 7),
				generationBounds);
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				new BoundingBox(
						centre.getX() - 7,
						world.getMinBuildHeight(),
						centre.getZ() - 7,
						centre.getX() + 7,
						centre.getY() + 12,
						centre.getZ() + 7));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		prepareSite(world, centre, generationBounds);
		buildShell(world, centre, generationBounds);
		buildElasticApproach(world, centre,
				generationBounds);
		buildStickySplashTraps(world, centre,
				generationBounds);
		buildFlavourClue(world, centre,
				generationBounds);
		buildRoof(world, centre, generationBounds);
		placeLoot(world, generationBounds, random,
				centre.offset(3, 1, 3), LOOT_ID);
		placeLoot(world, generationBounds, random,
				centre.offset(0, 1, 5),
				HIDDEN_LOOT_ID);
		return true;
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				generationBounds);
	}

	private static void prepareSite(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		fill(world, bounds, centre,
				-7, 0, -7, 7, 0, 7,
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-6, 1, -6, 6, 6, 6,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildShell(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		for (int y = 1; y <= 5; y++) {
			for (int axis = -6; axis <= 6;
					axis++) {
				set(world, bounds,
						centre.offset(axis, y, -6),
						bricks, 2);
				set(world, bounds,
						centre.offset(axis, y, 6),
						bricks, 2);
				set(world, bounds,
						centre.offset(-6, y, axis),
						bricks, 2);
				set(world, bounds,
						centre.offset(6, y, axis),
						bricks, 2);
			}
		}
		fill(world, bounds, centre,
				-1, 1, -6, 1, 3, -6,
				Blocks.AIR.defaultBlockState());
		BlockState glass =
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState();
		for (int axis : new int[] {-3, 0, 3}) {
			set(world, bounds,
					centre.offset(axis, 3, -6),
					glass, 2);
			set(world, bounds,
					centre.offset(axis, 3, 6),
					glass, 2);
			set(world, bounds,
					centre.offset(-6, 3, axis),
					glass, 2);
			set(world, bounds,
					centre.offset(6, 3, axis),
					glass, 2);
		}
		BlockState candyCane =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-6, 6}) {
				fill(world, bounds, centre,
						x, 1, z, x, 7, z,
						candyCane);
			}
		}
	}

	private static void buildElasticApproach(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState[] flavours = {
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK
						.get().defaultBlockState()
		};
		for (int z = -7; z <= 3; z++) {
			for (int x = -2; x <= 2; x++) {
				set(world, bounds,
						centre.offset(x, 0, z),
						flavours[Math.floorMod(
								x + z, flavours.length)],
						2);
			}
		}
	}

	private static void buildStickySplashTraps(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		buildTripwireLine(world, centre, bounds,
				-2, -4, Direction.EAST);
		buildTripwireLine(world, centre, bounds,
				2, 4, Direction.WEST);
	}

	private static void buildTripwireLine(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds, int z,
			int dispenserX, Direction dispenserFacing) {
		BlockState westHook = Blocks.TRIPWIRE_HOOK
				.defaultBlockState()
				.setValue(TripWireHookBlock.FACING,
						Direction.EAST)
				.setValue(TripWireHookBlock.ATTACHED,
						true);
		BlockState eastHook = Blocks.TRIPWIRE_HOOK
				.defaultBlockState()
				.setValue(TripWireHookBlock.FACING,
						Direction.WEST)
				.setValue(TripWireHookBlock.ATTACHED,
						true);
		set(world, bounds, centre.offset(-3, 1, z),
				westHook, 2);
		set(world, bounds, centre.offset(3, 1, z),
				eastHook, 2);
		BlockState wire = Blocks.TRIPWIRE
				.defaultBlockState()
				.setValue(TripWireBlock.EAST, true)
				.setValue(TripWireBlock.WEST, true)
				.setValue(TripWireBlock.ATTACHED,
						true);
		for (int x = -2; x <= 2; x++) {
			set(world, bounds, centre.offset(x, 1, z),
					wire, 2);
		}
		int supportX = -dispenserX;
		set(world, bounds,
				centre.offset(supportX, 1, z),
				CakeWorldBlocks.GINGERBREAD_BRICKS
						.get().defaultBlockState(),
				2);
		placeDispenser(world, bounds,
				centre.offset(dispenserX, 1, z),
				dispenserFacing);
	}

	private static void placeDispenser(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos position, Direction facing) {
		if (!bounds.isInside(position)) {
			return;
		}
		world.setBlock(position,
				Blocks.DISPENSER.defaultBlockState()
						.setValue(DispenserBlock.FACING,
								facing),
				2);
		if (world.getBlockEntity(position)
				instanceof DispenserBlockEntity dispenser) {
			for (int slot = 0; slot < 3; slot++) {
				ItemStack splash =
						PotionUtils.setPotion(
								new ItemStack(
										Items.SPLASH_POTION),
								Potions.SLOWNESS);
				dispenser.setItem(slot, splash);
			}
		}
	}

	private static void buildFlavourClue(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		fill(world, bounds, centre,
				-3, 1, 4, 3, 4, 4, bricks);
		BlockState lever = Blocks.LEVER
				.defaultBlockState()
				.setValue(LeverBlock.FACE,
						AttachFace.WALL)
				.setValue(LeverBlock.FACING,
						Direction.SOUTH);
		BlockState[] clues = {
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK
						.get().defaultBlockState()
		};
		for (int index = 0; index < 3; index++) {
			int x = (index - 1) * 2;
			set(world, bounds,
					centre.offset(x, 2, 3),
					lever, 2);
			set(world, bounds,
					centre.offset(x, 4, 3),
					clues[index], 2);
		}
		for (int x = -1; x <= 1; x++) {
			set(world, bounds,
					centre.offset(x, 1, 4),
					CakeWorldBlocks.GRAPE_GUMMY_BLOCK
							.get().defaultBlockState(),
					2);
			set(world, bounds,
					centre.offset(x, 2, 5),
					Blocks.STICKY_PISTON
							.defaultBlockState()
							.setValue(
									PistonBaseBlock
											.FACING,
									Direction.SOUTH),
					2);
		}
		BlockState redstone = Blocks.REDSTONE_WIRE
				.defaultBlockState()
				.setValue(RedStoneWireBlock.EAST,
						RedstoneSide.SIDE)
				.setValue(RedStoneWireBlock.WEST,
						RedstoneSide.SIDE);
		for (int x = -1; x <= 1; x++) {
			set(world, bounds,
					centre.offset(x, 1, 5),
					redstone, 2);
		}
	}

	private static void buildRoof(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState[] layers = {
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState()
		};
		for (int y = 6; y <= 10; y++) {
			int radius = 12 - y;
			fill(world, bounds, centre,
					-radius, y, -radius,
					radius, y, radius,
					layers[(y - 6)
							% layers.length]);
		}
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-6, 6}) {
				set(world, bounds,
						centre.offset(x, 6, z),
						CakeWorldBlocks
								.CANDY_CANE_PILLAR
								.get()
								.defaultBlockState(),
						2);
			}
		}
		set(world, bounds, centre.offset(0, 11, 0),
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState(),
				2);
	}

	private static void placeLoot(
			WorldGenLevel world, BoundingBox bounds,
			Random random, BlockPos position,
			ResourceLocation lootTable) {
		if (!bounds.isInside(position)) {
			return;
		}
		world.setBlock(position,
				Blocks.CHEST.defaultBlockState()
						.setValue(
								HorizontalDirectionalBlock
										.FACING,
								Direction.SOUTH),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, position, lootTable);
	}

	private static void fill(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre,
			int minimumX, int minimumY, int minimumZ,
			int maximumX, int maximumY, int maximumZ,
			BlockState state) {
		for (int x = minimumX; x <= maximumX; x++) {
			for (int y = minimumY;
					y <= maximumY; y++) {
				for (int z = minimumZ;
						z <= maximumZ; z++) {
					set(world, bounds,
							centre.offset(x, y, z),
							state, 2);
				}
			}
		}
	}

	private static void set(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos position, BlockState state,
			int flags) {
		if (bounds.isInside(position)) {
			world.setBlock(position, state, flags);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
