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
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
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
 * A quiet ruined kitchen carved into a safe End-island crater.
 *
 * <p>Mooncake Barrens is the structure's mature home. The structure creates a
 * traversable stepped bowl, rather than placing a decorative building beside
 * a randomly dangerous void edge.</p>
 */
public final class CraterKitchenFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("crater_kitchen_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("crater_kitchen_structure");
	public static final ResourceLocation POOL_ID =
			id("crater_kitchen/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("crater_kitchen");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("crater_kitchens");
	public static final ResourceLocation LOOT_ID =
			id("chests/crater_kitchen");
	public static final int PLACEMENT_SALT = 1978022;
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(32, 15, 32);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/crater_kitchen"));
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
	public static final CraterKitchenFeature FEATURE =
			new CraterKitchenFeature();
	public static final CraterKitchenStructureFeature
			STRUCTURE_FEATURE =
			new CraterKitchenStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private CraterKitchenFeature() {
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
								CraterKitchenFeature>(
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
								72, 24,
								RandomSpreadType.LINEAR,
								PLACEMENT_SALT)));
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
				CraterKitchenStructureFeature
						.CENTRE_OFFSET,
				0,
				CraterKitchenStructureFeature
						.CENTRE_OFFSET);
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
				CraterKitchenStructureFeature
						.CENTRE_OFFSET,
				0,
				CraterKitchenStructureFeature
						.CENTRE_OFFSET);
		return buildAt(world, random, centre,
				generationBounds);
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				fullGenerationBounds(centre));
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				generationBounds);
	}

	public static boolean repairAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				fullGenerationBounds(centre));
	}

	public static Rotation orientation(
			long worldSeed, BlockPos centre) {
		Random random = new Random(
				worldSeed ^ centre.asLong()
						^ PLACEMENT_SALT);
		return Rotation.getRandom(random);
	}

	public static BlockPos cachePosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				3, 1, -1);
	}

	public static BlockPos craterFloorPosition(
			BlockPos centre) {
		return centre;
	}

	public static BlockPos entrancePosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0, 5, -12);
	}

	public static BlockPos reloadSentinelPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				12, 5, 0);
	}

	public static int craterFloorOffset(
			int x, int z) {
		double radius = Math.sqrt(
				x * (double) x + z * (double) z);
		return Math.min(5,
				(int) Math.floor(radius * 5.0D / 12.0D));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		buildCrater(world, centre, rotation, bounds);
		buildKitchen(world, centre, rotation, bounds);
		buildEntrance(world, centre, rotation, bounds);
		buildLunarDial(world, centre, rotation, bounds);
		buildSafetyPads(world, centre, rotation, bounds);
		set(world, bounds, centre, rotation,
				12, 5, 0,
				CakeWorldBlocks.MACARON_BRICKS.get()
						.defaultBlockState());
		return true;
	}

	private static void buildCrater(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState crust =
				CakeWorldBlocks.MOONCAKE_CRUST.get()
						.defaultBlockState();
		BlockState macaron =
				CakeWorldBlocks.MACARON_BRICKS.get()
						.defaultBlockState();
		BlockState crumbs =
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState();
		for (int x = -12; x <= 12; x++) {
			for (int z = -12; z <= 12; z++) {
				int distanceSquared = x * x + z * z;
				if (distanceSquared > 144) {
					continue;
				}
				int floorY = craterFloorOffset(x, z);
				fill(world, bounds, centre, rotation,
						x, 0, z,
						x, floorY, z,
						crust);
				fill(world, bounds, centre, rotation,
						x, floorY + 1, z,
						x, 10, z,
						Blocks.AIR
								.defaultBlockState());
				boolean ancientCrumb =
						floorY > 0
								&& distanceSquared < 100
								&& Math.floorMod(
										x * 31 + z * 17,
										11) == 0;
				set(world, bounds, centre, rotation,
						x, floorY, z,
						distanceSquared >= 110
								? macaron
								: ancientCrumb
										? crumbs
										: crust);
			}
		}
	}

	private static void buildKitchen(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState meringue =
				CakeWorldBlocks.MERINGUE_BRICKS.get()
						.defaultBlockState();
		BlockState macaron =
				CakeWorldBlocks.MACARON_BRICKS.get()
						.defaultBlockState();
		BlockState pillar =
				CakeWorldBlocks.MACARON_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		fill(world, bounds, centre, rotation,
				-4, 0, -4, 4, 0, 4,
				meringue);
		fill(world, bounds, centre, rotation,
				-4, 1, -4, 4, 9, 4,
				Blocks.AIR.defaultBlockState());

		fill(world, bounds, centre, rotation,
				-4, 1, 4, -4, 6, 4,
				pillar);
		fill(world, bounds, centre, rotation,
				4, 1, 4, 4, 4, 4,
				pillar);
		fill(world, bounds, centre, rotation,
				-4, 6, 4, 0, 6, 4,
				macaron);
		fill(world, bounds, centre, rotation,
				2, 5, 4, 4, 5, 4,
				macaron);
		fill(world, bounds, centre, rotation,
				-2, 2, 4, 2, 4, 4,
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				0, 3, 4,
				Blocks.AIR.defaultBlockState());

		set(world, bounds, centre, rotation,
				-3, 1, 2,
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				-1, 1, 2,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				1, 1, 2,
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				3, 1, 2,
				CakeWorldBlocks.CANDY_COOKER.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				-3, 1, -1,
				CakeWorldBlocks.COOKBOOK_LIBRARY.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				0, 1, 0,
				CakeWorldBlocks.WAFER_SLAB.get()
						.defaultBlockState());
		placeCache(world, bounds, centre, rotation);
	}

	private static void buildEntrance(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState support =
				CakeWorldBlocks.MERINGUE_BRICKS.get()
						.defaultBlockState();
		for (int index = 0; index < 6; index++) {
			int z = -12 + index;
			int y = 5 - index;
			for (int x = -1; x <= 1; x++) {
				fill(world, bounds, centre, rotation,
						x, 0, z, x, y - 1, z,
						support);
				set(world, bounds, centre, rotation,
						x, y, z,
						CakeWorldBlocks
								.WAFER_STAIRS.get()
								.defaultBlockState()
								.setValue(
										StairBlock.FACING,
										Direction.SOUTH)
								.setValue(
										StairBlock.HALF,
										Half.BOTTOM));
				fill(world, bounds, centre, rotation,
						x, y + 1, z,
						x, y + 3, z,
						Blocks.AIR
								.defaultBlockState());
			}
		}
		fill(world, bounds, centre, rotation,
				-1, 0, -6, 1, 0, -4,
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState());
		fill(world, bounds, centre, rotation,
				-1, 1, -6, 1, 3, -4,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildLunarDial(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		int[][] markers = {
				{0, 8}, {4, 7}, {7, 4}, {8, 0},
				{7, -4}, {4, -7}, {-4, -7},
				{-7, -4}, {-8, 0}, {-7, 4},
				{-4, 7}
		};
		for (int index = 0;
				index < markers.length; index++) {
			int x = markers[index][0];
			int z = markers[index][1];
			int y = craterFloorOffset(x, z);
			set(world, bounds, centre, rotation,
					x, y, z,
					index % 3 == 0
							? CakeWorldBlocks
									.CANDY_GLASS.get()
									.defaultBlockState()
							: CakeWorldBlocks
									.ROCK_CANDY.get()
									.defaultBlockState());
		}
		for (int[] marker : new int[][] {
				{8, 0}, {-8, 0}, {0, 8}}) {
			int x = marker[0];
			int z = marker[1];
			int y = craterFloorOffset(x, z);
			set(world, bounds, centre, rotation,
					x, y + 1, z,
					Blocks.END_ROD.defaultBlockState()
							.setValue(
									EndRodBlock.FACING,
									Direction.UP));
		}
	}

	private static void buildSafetyPads(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		for (int[] pad : new int[][] {
				{7, 0}, {-7, 0}, {0, 7}, {0, -7},
				{5, 5}, {5, -5}, {-5, 5}, {-5, -5}}) {
			int x = pad[0];
			int z = pad[1];
			set(world, bounds, centre, rotation,
					x, craterFloorOffset(x, z), z,
					CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState());
		}
	}

	private static void placeCache(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation) {
		BlockPos position = transform(
				centre, rotation, 3, 1, -1);
		if (!bounds.isInside(position)) {
			return;
		}
		world.setBlock(position,
				Blocks.CHEST.defaultBlockState(), 2);
		if (world.getBlockEntity(position)
				instanceof RandomizableContainerBlockEntity) {
			((RandomizableContainerBlockEntity)
					world.getBlockEntity(position))
							.setLootTable(
									LOOT_ID,
									position.asLong());
		}
	}

	private static BoundingBox fullGenerationBounds(
			BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 16,
				centre.getY(),
				centre.getZ() - 16,
				centre.getX() + 16,
				centre.getY() + 15,
				centre.getZ() + 16);
	}

	private static void fill(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int minimumX, int minimumY, int minimumZ,
			int maximumX, int maximumY, int maximumZ,
			BlockState state) {
		if (maximumY < minimumY) {
			return;
		}
		for (int x = minimumX; x <= maximumX; x++) {
			for (int y = minimumY;
					y <= maximumY; y++) {
				for (int z = minimumZ;
						z <= maximumZ; z++) {
					set(world, bounds, centre,
							rotation, x, y, z,
							state);
				}
			}
		}
	}

	private static void set(
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
		BlockPos offset =
				new BlockPos(x, y, z).rotate(rotation);
		return centre.offset(offset);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
