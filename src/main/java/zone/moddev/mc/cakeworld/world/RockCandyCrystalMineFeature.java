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
import net.minecraft.world.level.block.LadderBlock;
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
 * A peaceful, recoverable underground gallery for CakeWorld geology.
 *
 * <p>The mine is deliberately not another mineshaft. Its authored exhibits
 * teach the visual vocabulary of rock families and ore patterns, while
 * fixed-world OreSpawn surveys remain the evidence for real generated
 * geology.</p>
 */
public final class RockCandyCrystalMineFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("rock_candy_crystal_mine_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("rock_candy_crystal_mine_structure");
	public static final ResourceLocation POOL_ID =
			id("rock_candy_crystal_mine/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("rock_candy_crystal_mine");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("rock_candy_crystal_mines");
	public static final ResourceLocation LOOT_ID =
			id("chests/rock_candy_crystal_mine");
	public static final int PLACEMENT_SALT = 1978023;
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(32, 48, 32);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/rock_candy_crystal_mine"));
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
	public static final RockCandyCrystalMineFeature FEATURE =
			new RockCandyCrystalMineFeature();
	public static final RockCandyCrystalMineStructureFeature
			STRUCTURE_FEATURE =
			new RockCandyCrystalMineStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private RockCandyCrystalMineFeature() {
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
								RockCandyCrystalMineFeature>(
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
								80, 32,
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
				RockCandyCrystalMineStructureFeature
						.CENTRE_OFFSET,
				0,
				RockCandyCrystalMineStructureFeature
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
				RockCandyCrystalMineStructureFeature
						.CENTRE_OFFSET,
				0,
				RockCandyCrystalMineStructureFeature
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

	public static BlockPos shaftBottomPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0, 0, -12);
	}

	public static BlockPos entrancePosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0,
				RockCandyCrystalMineStructureFeature
						.SURFACE_OFFSET,
				-7);
	}

	public static BlockPos cachePosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				5, 1, 5);
	}

	public static BlockPos reloadSentinelPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				4, 40, -11);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		buildCentralCavern(world, centre, rotation,
				bounds);
		buildFamilyWings(world, centre, rotation,
				bounds);
		buildPatternExhibits(world, centre, rotation,
				bounds);
		buildHeadframe(world, centre, rotation,
				bounds);
		// Cut and furnish the shaft after the headframe so its surface floor
		// cannot overwrite the transition rung or supporting wall.
		buildShaft(world, centre, rotation, bounds);
		placeCache(world, centre, rotation, bounds);
		return true;
	}

	private static void buildCentralCavern(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState floor =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int x = -8; x <= 8; x++) {
			for (int z = -8; z <= 8; z++) {
				int distance = x * x + z * z;
				if (distance > 64) {
					continue;
				}
				set(world, bounds, centre, rotation,
						x, 0, z, floor);
				for (int y = 1; y <= 7; y++) {
					set(world, bounds, centre,
							rotation, x, y, z,
							Blocks.CAVE_AIR
									.defaultBlockState());
				}
				if (distance >= 49) {
					for (int y = 1; y <= 5; y++) {
						set(world, bounds, centre,
								rotation, x, y,
								z,
								CakeWorldBlocks
										.ROCK_CANDY
										.get()
										.defaultBlockState());
					}
				}
			}
		}
		for (int[] pad : new int[][] {
				{0, 0}, {4, 0}, {-4, 0},
				{0, 4}, {0, -4}}) {
			set(world, bounds, centre, rotation,
					pad[0], 0, pad[1],
					CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState());
		}
		for (int[] light : new int[][] {
				{5, 1, 0}, {-5, 1, 0},
				{0, 1, 5}, {0, 1, -5}}) {
			set(world, bounds, centre, rotation,
					light[0], light[1], light[2],
					Blocks.LANTERN.defaultBlockState());
		}
		for (int y = 1; y <= 6; y++) {
			set(world, bounds, centre, rotation,
					0, y, 0,
					y % 2 == 0
							? CakeWorldBlocks
									.CANDY_GLASS.get()
									.defaultBlockState()
							: CakeWorldBlocks
									.ROCK_CANDY.get()
									.defaultBlockState());
		}
	}

	private static void buildFamilyWings(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		buildWing(world, centre, rotation, bounds,
				Direction.NORTH,
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState(),
				CakeWorldBlocks.WAFER_ROCK.get()
						.defaultBlockState());
		buildWing(world, centre, rotation, bounds,
				Direction.EAST,
				CakeWorldBlocks.PEPPERMINT_ROCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.PEPPERMINT_ROCK.get()
						.defaultBlockState());
		buildWing(world, centre, rotation, bounds,
				Direction.SOUTH,
				CakeWorldBlocks.ROCK_CANDY.get()
						.defaultBlockState(),
				CakeWorldBlocks.NOUGAT_ROCK.get()
						.defaultBlockState());
		buildWing(world, centre, rotation, bounds,
				Direction.WEST,
				CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.BURNT_SUGAR_ROCK.get()
						.defaultBlockState());
	}

	private static void buildWing(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds,
			Direction direction, BlockState leftHost,
			BlockState rightHost) {
		boolean alongZ =
				direction.getAxis() == Direction.Axis.Z;
		int sign = direction.getAxisDirection()
				== Direction.AxisDirection.POSITIVE
						? 1 : -1;
		for (int along = 7; along <= 15; along++) {
			for (int across = -4;
					across <= 4; across++) {
				int x = alongZ ? across
						: sign * along;
				int z = alongZ ? sign * along
						: across;
				set(world, bounds, centre, rotation,
						x, 0, z,
						CakeWorldBlocks.WAFER_BLOCK
								.get()
								.defaultBlockState());
				for (int y = 1; y <= 6; y++) {
					set(world, bounds, centre,
							rotation, x, y, z,
							Blocks.CAVE_AIR
									.defaultBlockState());
				}
			}
		}
		for (int across = -4;
				across <= 4; across++) {
			for (int y = 1; y <= 6; y++) {
				int x = alongZ ? across
						: sign * 16;
				int z = alongZ ? sign * 16
						: across;
				set(world, bounds, centre, rotation,
						x, y, z,
						across < 0
								? leftHost
								: rightHost);
			}
		}
		BlockState upright =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		for (int across : new int[] {-4, 4}) {
			for (int y = 1; y <= 5; y++) {
				int x = alongZ ? across
						: sign * 8;
				int z = alongZ ? sign * 8
						: across;
				set(world, bounds, centre, rotation,
						x, y, z, upright);
			}
		}
	}

	private static void buildPatternExhibits(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		// Sedimentary wing: a broad Cocoa Cloud and a thin Liquorice Vein.
		for (int[] offset : new int[][] {
				{-2, 2, -15}, {-1, 2, -15},
				{0, 2, -15}, {1, 2, -15},
				{2, 2, -15}, {-1, 3, -15},
				{0, 3, -15}, {1, 3, -15},
				{0, 4, -15}}) {
			set(world, bounds, centre, rotation,
					offset[0], offset[1], offset[2],
					CakeWorldBlocks.COCOA_CLOUD.get()
							.defaultBlockState());
		}
		for (int x = -3; x <= 3; x++) {
			set(world, bounds, centre, rotation,
					x, 1, -14,
					CakeWorldBlocks.LIQUORICE_VEIN
							.get()
							.defaultBlockState());
		}

		// Intrusive wing: one deliberately precise Mint Crystal.
		set(world, bounds, centre, rotation,
				15, 3, 0,
				CakeWorldBlocks.MINT_CRYSTAL.get()
						.defaultBlockState());

		// Metamorphic wing: a compact thirteen-block Rock-Candy deposit.
		for (int[] offset : new int[][] {
				{0, 3, 14},
				{-1, 3, 14}, {1, 3, 14},
				{0, 2, 14}, {0, 4, 14},
				{-1, 2, 14}, {1, 2, 14},
				{-1, 4, 14}, {1, 4, 14},
				{0, 3, 13}, {0, 3, 15},
				{-2, 3, 14}, {2, 3, 14}}) {
			set(world, bounds, centre, rotation,
					offset[0], offset[1], offset[2],
					CakeWorldBlocks
							.ROCK_CANDY_DEPOSIT.get()
							.defaultBlockState());
		}
		set(world, bounds, centre, rotation,
				0, 3, 13,
				CakeWorldBlocks.ROCK_CANDY_DIAMOND
						.get()
						.defaultBlockState());

		// Volcanic wing: a small weighted Sprinkle Cluster.
		for (int[] offset : new int[][] {
				{-15, 2, -1}, {-15, 2, 0},
				{-15, 2, 1}, {-15, 3, 0},
				{-15, 3, 1}}) {
			set(world, bounds, centre, rotation,
					offset[0], offset[1], offset[2],
					CakeWorldBlocks.SPRINKLE_CLUSTER
							.get()
							.defaultBlockState());
		}
		set(world, bounds, centre, rotation,
				-15, 4, 0,
				CakeWorldBlocks.RICH_SPRINKLE_CLUSTER
						.get()
						.defaultBlockState());
	}

	private static void buildShaft(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -13; z <= -11; z++) {
				for (int y = 1; y <= 35; y++) {
					set(world, bounds, centre,
							rotation, x, y, z,
							Blocks.CAVE_AIR
									.defaultBlockState());
				}
				set(world, bounds, centre, rotation,
						x, 0, z,
						CakeWorldBlocks.MARSHMALLOW
								.get()
								.defaultBlockState());
			}
		}
		Direction ladderFacing =
				rotation.rotate(Direction.SOUTH);
		for (int y = 1; y <= 35; y++) {
			set(world, bounds, centre, Rotation.NONE,
					transform(centre, rotation,
							0, y, -14),
					CakeWorldBlocks.CANDY_CANE_PILLAR
							.get()
							.defaultBlockState()
							.setValue(
									RotatedPillarBlock.AXIS,
									Direction.Axis.Y));
			set(world, bounds, centre, Rotation.NONE,
					transform(centre, rotation,
							0, y, -13),
					Blocks.LADDER.defaultBlockState()
							.setValue(
									LadderBlock.FACING,
									ladderFacing));
		}
		for (int y : new int[] {8, 16, 24, 32}) {
			for (int x : new int[] {-1, 1}) {
				set(world, bounds, centre, rotation,
						x, y, -12,
						CakeWorldBlocks.WAFER_BLOCK
								.get()
								.defaultBlockState());
			}
		}
	}

	private static void buildHeadframe(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		int surface =
				RockCandyCrystalMineStructureFeature
						.SURFACE_OFFSET;
		fill(world, bounds, centre, rotation,
				-4, surface, -15,
				4, surface, -7,
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState());
		fill(world, bounds, centre, rotation,
				-1, surface, -13,
				1, surface, -11,
				Blocks.CAVE_AIR.defaultBlockState());
		BlockState upright =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-15, -7}) {
				fill(world, bounds, centre, rotation,
						x, surface + 1, z,
						x, surface + 7, z,
						upright);
				set(world, bounds, centre, rotation,
						x, surface + 6, z,
						Blocks.LANTERN
								.defaultBlockState());
			}
		}
		fill(world, bounds, centre, rotation,
				-4, surface + 8, -15,
				4, surface + 8, -7,
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState());
		fill(world, bounds, centre, rotation,
				-3, surface + 9, -14,
				3, surface + 9, -8,
				CakeWorldBlocks.ICING.get()
						.defaultBlockState());
		for (int y = surface + 1;
				y <= surface + 5; y++) {
			for (int x : new int[] {-2, 2}) {
				set(world, bounds, centre, rotation,
						x, y, -11,
						CakeWorldBlocks.CANDY_GLASS
								.get()
								.defaultBlockState());
			}
		}
		for (int x = -2; x <= 2; x++) {
			set(world, bounds, centre, rotation,
					x, surface, -6,
					CakeWorldBlocks.WAFER_STAIRS.get()
							.defaultBlockState()
							.setValue(
									StairBlock.FACING,
									Direction.SOUTH)
							.setValue(
									StairBlock.HALF,
									Half.BOTTOM));
		}
		fill(world, bounds, centre, rotation,
				-1, surface, -5,
				1, surface, -3,
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState());
	}

	private static void placeCache(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockPos position = transform(
				centre, rotation, 5, 1, 5);
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
				centre.getY() + 48,
				centre.getZ() + 16);
	}

	private static void fill(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int minimumX, int minimumY, int minimumZ,
			int maximumX, int maximumY, int maximumZ,
			BlockState state) {
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
			int x, int y, int z, BlockState state) {
		set(world, bounds, centre, Rotation.NONE,
				transform(centre, rotation, x, y, z),
				state.rotate(rotation));
	}

	private static void set(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation ignored,
			BlockPos position, BlockState state) {
		if (bounds.isInside(position)) {
			world.setBlock(position, state, 2);
		}
	}

	private static BlockPos transform(
			BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return centre.offset(
				new BlockPos(x, y, z)
						.rotate(rotation));
	}

	private static ResourceLocation id(
			String path) {
		return new ResourceLocation(
				CakeWorld.MODID, path);
	}
}
