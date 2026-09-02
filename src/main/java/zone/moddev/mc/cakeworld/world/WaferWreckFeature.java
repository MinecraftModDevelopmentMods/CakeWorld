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
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
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
 * First edible Shipwreck counterpart.
 *
 * <p>The fixed full wreck is deliberately damaged but retains the three
 * vanilla cargo roles, a real exploration map and Dolphin discovery through
 * the public Shipwreck configured-structure tag.</p>
 */
public final class WaferWreckFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("wafer_wreck_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("wafer_wreck_structure");
	public static final ResourceLocation POOL_ID =
			id("wafer_wreck/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("wafer_wreck");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("wafer_wrecks");
	public static final ResourceLocation SUPPLY_LOOT_ID =
			id("chests/wafer_wreck_supply");
	public static final ResourceLocation MAP_LOOT_ID =
			id("chests/wafer_wreck_map");
	public static final ResourceLocation TREASURE_LOOT_ID =
			id("chests/wafer_wreck_treasure");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(32, 16, 32);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/wafer_wreck"));
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
	public static final WaferWreckFeature FEATURE =
			new WaferWreckFeature();
	public static final WaferWreckStructureFeature
			STRUCTURE_FEATURE =
			new WaferWreckStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private WaferWreckFeature() {
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
								WaferWreckFeature>(
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
								24, 4,
								RandomSpreadType.LINEAR,
								165745295)));
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
		BlockPos centre = context.origin().offset(
				16,
				WaferWreckStructureFeature
						.FLOOR_OFFSET,
				16);
		return buildAt(context.level(), context.random(),
				centre);
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		BlockPos centre = origin.offset(
				16,
				WaferWreckStructureFeature
						.FLOOR_OFFSET,
				16);
		return buildAt(world, random, centre,
				generationBounds);
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				fullBounds(centre));
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				generationBounds);
	}

	public static Rotation orientation(
			long worldSeed, BlockPos centre) {
		Random random = new Random(
				worldSeed ^ centre.asLong()
						^ 165745295L);
		return Rotation.getRandom(random);
	}

	public static List<BlockPos> lootPositions(
			long worldSeed, BlockPos centre) {
		Rotation rotation =
				orientation(worldSeed, centre);
		return List.of(
				transform(centre, rotation,
						-2, 4, -8),
				transform(centre, rotation,
						-1, 4, 9),
				transform(centre, rotation,
						1, 0, 10));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		buildHull(world, centre, rotation, bounds);
		buildCabin(world, centre, rotation, bounds);
		buildMastAndSail(world, centre, rotation,
				bounds);
		placeCargo(world, random, centre, rotation,
				bounds);
		return true;
	}

	private static BoundingBox fullBounds(
			BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 16,
				centre.getY()
						- WaferWreckStructureFeature
								.FLOOR_OFFSET,
				centre.getZ() - 16,
				centre.getX() + 16,
				centre.getY() + 12,
				centre.getZ() + 16);
	}

	private static void buildHull(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		BlockState candyCane =
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState();
		BlockState crossbeam = candyCane.setValue(
				RotatedPillarBlock.AXIS,
				Direction.Axis.X);
		for (int z = -14; z <= 14; z++) {
			int width = hullWidth(z);
			int keelWidth = Math.max(0, width - 2);
			for (int x = -keelWidth;
					x <= keelWidth; x++) {
				setLocal(world, bounds, centre,
						rotation, x, -2, z,
						wafer);
			}
			for (int x = -width + 1;
					x <= width - 1; x++) {
				setLocal(world, bounds, centre,
						rotation, x, -1, z,
						wafer);
			}
			for (int y = 0; y <= 2; y++) {
				if (!isDamageGap(-width,
						y, z)) {
					setLocal(world, bounds,
							centre, rotation,
							-width, y, z,
							wafer);
				}
				if (!isDamageGap(width,
						y, z)) {
					setLocal(world, bounds,
							centre, rotation,
							width, y, z,
							wafer);
				}
			}
			for (int x = -width; x <= width;
					x++) {
				if (!isDeckGap(x, z)) {
					setLocal(world, bounds,
							centre, rotation,
							x, 3, z, wafer);
				}
			}
		}
		for (int z : new int[] {-9, -3, 3, 9}) {
			int width = hullWidth(z);
			for (int y = -1; y <= 3; y++) {
				setLocal(world, bounds, centre,
						rotation, -width, y, z,
						candyCane);
				setLocal(world, bounds, centre,
						rotation, width, y, z,
						candyCane);
			}
			for (int x = -width; x <= width;
					x++) {
				setLocal(world, bounds, centre,
						rotation, x, 3, z,
						crossbeam);
			}
		}
		for (int z : new int[] {-11, -6, 0, 6}) {
			int width = hullWidth(z);
			setLocal(world, bounds, centre,
					rotation, -width, 1, z,
					CakeWorldBlocks.CANDY_GLASS
							.get()
							.defaultBlockState());
			setLocal(world, bounds, centre,
					rotation, width, 1, z,
					CakeWorldBlocks.CANDY_GLASS
							.get()
							.defaultBlockState());
		}
		for (int z : new int[] {-6, 1, 8}) {
			setLocal(world, bounds, centre,
					rotation, 0, -1, z,
					CakeWorldBlocks.BISCUIT_STONE
							.get()
							.defaultBlockState());
		}
	}

	private static void buildCabin(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int z = 7; z <= 12; z++) {
			for (int y = 4; y <= 6; y++) {
				setLocal(world, bounds, centre,
						rotation, -3, y, z,
						wafer);
				setLocal(world, bounds, centre,
						rotation, 3, y, z,
						wafer);
			}
			for (int x = -3; x <= 3; x++) {
				setLocal(world, bounds, centre,
						rotation, x, 7, z,
						wafer);
			}
		}
		for (int x = -3; x <= 3; x++) {
			for (int y = 4; y <= 6; y++) {
				setLocal(world, bounds, centre,
						rotation, x, y, 12,
						wafer);
				if (x != 0 || y == 6) {
					setLocal(world, bounds,
							centre, rotation,
							x, y, 7, wafer);
				}
			}
		}
		for (int z : new int[] {9, 11}) {
			setLocal(world, bounds, centre,
					rotation, -3, 5, z,
					CakeWorldBlocks.CANDY_GLASS
							.get()
							.defaultBlockState());
			setLocal(world, bounds, centre,
					rotation, 3, 5, z,
					CakeWorldBlocks.CANDY_GLASS
							.get()
							.defaultBlockState());
		}
	}

	private static void buildMastAndSail(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState mast =
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState();
		for (int y = 4; y <= 12; y++) {
			setLocal(world, bounds, centre,
					rotation, 0, y, -2, mast);
		}
		BlockState yard = mast.setValue(
				RotatedPillarBlock.AXIS,
				Direction.Axis.X);
		for (int x = -5; x <= 5; x++) {
			setLocal(world, bounds, centre,
					rotation, x, 11, -2, yard);
		}
		for (int x = -4; x <= 4; x++) {
			setLocal(world, bounds, centre,
					rotation, x, 7, -2, yard);
		}
		BlockState icing =
				CakeWorldBlocks.ICING.get()
						.defaultBlockState();
		for (int y = 8; y <= 10; y++) {
			int halfWidth = y == 10 ? 3 : 4;
			for (int x = -halfWidth;
					x <= halfWidth; x++) {
				if (x != 0
						&& !(x == 4 && y == 8)) {
					setLocal(world, bounds,
							centre, rotation,
							x, y, -2,
							icing);
				}
			}
		}
		setLocal(world, bounds, centre, rotation,
				0, 12, -2,
				CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState());
	}

	private static void placeCargo(
			WorldGenLevel world, Random random,
			BlockPos centre, Rotation rotation,
			BoundingBox bounds) {
		placeChest(world, random, bounds,
				transform(centre, rotation,
						-2, 4, -8),
				Direction.NORTH, rotation,
				SUPPLY_LOOT_ID);
		placeChest(world, random, bounds,
				transform(centre, rotation,
						-1, 4, 9),
				Direction.SOUTH, rotation,
				MAP_LOOT_ID);
		placeChest(world, random, bounds,
				transform(centre, rotation,
						1, 0, 10),
				Direction.WEST, rotation,
				TREASURE_LOOT_ID);
	}

	private static void placeChest(
			WorldGenLevel world, Random random,
			BoundingBox bounds, BlockPos position,
			Direction facing, Rotation rotation,
			ResourceLocation loot) {
		if (!bounds.isInside(position)) {
			return;
		}
		BlockState chest =
				Blocks.CHEST.defaultBlockState()
						.setValue(ChestBlock.FACING,
								facing)
						.rotate(rotation);
		world.setBlock(position, chest, 2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, position, loot);
	}

	private static int hullWidth(int z) {
		int distance = Math.abs(z);
		if (distance >= 14) {
			return 1;
		}
		if (distance >= 12) {
			return 2;
		}
		if (distance >= 10) {
			return 3;
		}
		return 4;
	}

	private static boolean isDamageGap(
			int x, int y, int z) {
		return (z == -5 && x > 0 && y <= 1)
				|| (z == 5 && x < 0 && y >= 1)
				|| (z == 9 && x > 0 && y <= 1);
	}

	private static boolean isDeckGap(
			int x, int z) {
		return (z == -5 && x >= 2)
				|| (z == 5 && x <= -2)
				|| (z == 9 && x == 1);
	}

	private static void setLocal(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int x, int y, int z,
			BlockState state) {
		BlockPos position =
				transform(centre, rotation, x, y, z);
		if (bounds.isInside(position)) {
			world.setBlock(position,
					state.rotate(rotation), 2);
		}
	}

	private static BlockPos transform(
			BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return switch (rotation) {
		case CLOCKWISE_90 ->
			centre.offset(-z, y, x);
		case CLOCKWISE_180 ->
			centre.offset(-x, y, -z);
		case COUNTERCLOCKWISE_90 ->
			centre.offset(z, y, -x);
		default -> centre.offset(x, y, z);
		};
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID,
				path);
	}
}
