package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
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
 * A road bridge that always crosses a narrow, safe Lemonade cutting.
 *
 * <p>The structure creates the obstacle it solves. This avoids decorative
 * bridges scattered over flat ground while keeping the first prototype
 * deterministic and suitable for later reuse by settlement road pools.</p>
 */
public final class CandyCaneBridgeFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("candy_cane_bridge_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("candy_cane_bridge_structure");
	public static final ResourceLocation POOL_ID =
			id("candy_cane_bridge/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("candy_cane_bridge");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("candy_cane_bridges");
	public static final int PLACEMENT_SALT = 1978021;
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(32, 9, 32);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/candy_cane_bridge"));
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
	public static final CandyCaneBridgeFeature FEATURE =
			new CandyCaneBridgeFeature();
	public static final CandyCaneBridgeStructureFeature
			STRUCTURE_FEATURE =
			new CandyCaneBridgeStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private CandyCaneBridgeFeature() {
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
								CandyCaneBridgeFeature>(
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
								64, 24,
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
				CandyCaneBridgeStructureFeature
						.CENTRE_OFFSET,
				0,
				CandyCaneBridgeStructureFeature
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
				CandyCaneBridgeStructureFeature
						.CENTRE_OFFSET,
				0,
				CandyCaneBridgeStructureFeature
						.CENTRE_OFFSET);
		return buildAt(world, random, centre,
				generationBounds);
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				fullGenerationBounds(world, centre));
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
				fullGenerationBounds(world, centre));
	}

	public static Rotation orientation(
			long worldSeed, BlockPos centre) {
		Random random = new Random(
				worldSeed ^ centre.asLong()
						^ PLACEMENT_SALT);
		return Rotation.getRandom(random);
	}

	public static BlockPos channelPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0, -1, 0);
	}

	public static BlockPos deckPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0, 3, 0);
	}

	public static BlockPos reloadSentinelPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				3, 5, 1);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		clearCrossing(world, centre, rotation, bounds);
		buildChannel(world, centre, rotation, bounds);
		buildRoadAndDeck(world, centre, rotation,
				bounds);
		buildCandyCaneTruss(world, centre, rotation,
				bounds);
		return true;
	}

	private static void clearCrossing(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		fill(world, bounds, centre, rotation,
				-16, 0, -3, 16, 8, 3,
				Blocks.AIR.defaultBlockState());
		fill(world, bounds, centre, rotation,
				-3, 1, -16, 3, 8, 16,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildChannel(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState bed =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		BlockState lemonade =
				CakeWorldFluids.LEMONADE_BLOCK.get()
						.defaultBlockState();
		fill(world, bounds, centre, rotation,
				-16, -3, -3, 16, -3, 3, bed);
		fill(world, bounds, centre, rotation,
				-16, -2, -3, 16, -1, 3,
				lemonade);
		fill(world, bounds, centre, rotation,
				-16, -2, -4, 16, 0, -4, bed);
		fill(world, bounds, centre, rotation,
				-16, -2, 4, 16, 0, 4, bed);

		BlockState marshmallow =
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState();
		for (int x : new int[] {-10, -6, 6, 10}) {
			for (int z : new int[] {-2, 2}) {
				set(world, bounds, centre, rotation,
						x, 0, z, marshmallow);
			}
		}
	}

	private static void buildRoadAndDeck(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState crumbs =
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState();
		BlockState roadBed =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int z : new int[] {
				-16, -15, -14, -13, -12, -11,
				11, 12, 13, 14, 15, 16}) {
			for (int x = -2; x <= 2; x++) {
				set(world, bounds, centre, rotation,
						x, -1, z, roadBed);
				set(world, bounds, centre, rotation,
						x, 0, z, crumbs);
			}
		}

		for (int index = 0; index < 4; index++) {
			int northZ = -10 + index;
			int southZ = 10 - index;
			for (int x = -2; x <= 2; x++) {
				fill(world, bounds, centre,
						rotation,
						x, -2, northZ,
						x, index - 1, northZ,
						wafer);
				fill(world, bounds, centre,
						rotation,
						x, -2, southZ,
						x, index - 1, southZ,
						wafer);
				set(world, bounds, centre, rotation,
						x, index, northZ,
						CakeWorldBlocks
								.WAFER_STAIRS.get()
								.defaultBlockState()
								.setValue(
										StairBlock.FACING,
										Direction.SOUTH)
								.setValue(
										StairBlock.HALF,
										Half.BOTTOM));
				set(world, bounds, centre, rotation,
						x, index, southZ,
						CakeWorldBlocks
								.WAFER_STAIRS.get()
								.defaultBlockState()
								.setValue(
										StairBlock.FACING,
										Direction.NORTH)
								.setValue(
										StairBlock.HALF,
										Half.BOTTOM));
			}
		}
		fill(world, bounds, centre, rotation,
				-2, 3, -6, 2, 3, 6, wafer);
	}

	private static void buildCandyCaneTruss(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState vertical =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		BlockState alongRoad =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Z);
		BlockState crossRoad =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.X);

		for (int x : new int[] {-3, 3}) {
			fill(world, bounds, centre, rotation,
					x, 2, -6, x, 2, 6,
					alongRoad);
			fill(world, bounds, centre, rotation,
					x, 5, -6, x, 5, 6,
					alongRoad);
			for (int z : new int[] {-6, 0, 6}) {
				fill(world, bounds, centre,
						rotation,
						x, -2, z,
						x, 6, z,
						vertical);
				fillSupportDown(world, bounds,
						centre, rotation,
						x, z, vertical);
			}
		}
		for (int z : new int[] {-6, 6}) {
			fill(world, bounds, centre, rotation,
					-3, 2, z, 3, 2, z,
					crossRoad);
		}
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-6, 6}) {
				set(world, bounds, centre, rotation,
						x, 7, z,
						((x + z) & 1) == 0
								? CakeWorldBlocks
										.RASPBERRY_GUMMY_BLOCK
										.get()
										.defaultBlockState()
								: CakeWorldBlocks
										.BLUEBERRY_GUMMY_BLOCK
										.get()
										.defaultBlockState());
				set(world, bounds, centre, rotation,
						x, 6, z,
						Blocks.LANTERN
								.defaultBlockState());
			}
		}
	}

	private static void fillSupportDown(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int x, int z, BlockState support) {
		BlockPos cursor = transform(
				centre, rotation, x, -3, z);
		while (cursor.getY()
				> world.getMinBuildHeight()) {
			if (!bounds.isInside(cursor)) {
				cursor = cursor.below();
				continue;
			}
			BlockState existing =
					world.getBlockState(cursor);
			if (existing.isFaceSturdy(
					world, cursor, Direction.UP)
					&& world.getFluidState(cursor)
							.isEmpty()) {
				break;
			}
			world.setBlock(cursor, support, 2);
			cursor = cursor.below();
		}
	}

	private static BoundingBox fullGenerationBounds(
			WorldGenLevel world, BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 16,
				world.getMinBuildHeight(),
				centre.getZ() - 16,
				centre.getX() + 16,
				centre.getY() + 9,
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
