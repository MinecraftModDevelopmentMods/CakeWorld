package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.WaferWindmillBlock;
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
import net.minecraft.world.level.block.LanternBlock;
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
 * A tall rural mill that turns the small GADGET-008 prop into a landmark.
 *
 * <p>The hidden redstone source keeps the visible hub turning while the
 * syrup-pipe shaft and kitchen stations communicate a whimsical mechanical
 * story. They deliberately do not create an item, fluid, energy, or signal
 * network.</p>
 */
public final class WaferWindmillFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("wafer_windmill_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("wafer_windmill_structure");
	public static final ResourceLocation POOL_ID =
			id("wafer_windmill/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("wafer_windmill");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("wafer_windmills");
	public static final ResourceLocation LOOT_ID =
			id("chests/wafer_windmill");
	public static final int PLACEMENT_SALT = 1978020;
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(20, 19, 20);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/wafer_windmill"));
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
	public static final WaferWindmillFeature FEATURE =
			new WaferWindmillFeature();
	public static final WaferWindmillStructureFeature
			STRUCTURE_FEATURE =
			new WaferWindmillStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private WaferWindmillFeature() {
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
								WaferWindmillFeature>(
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
								56, 20,
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
				WaferWindmillStructureFeature.CENTRE_OFFSET,
				0,
				WaferWindmillStructureFeature.CENTRE_OFFSET);
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
				WaferWindmillStructureFeature.CENTRE_OFFSET,
				0,
				WaferWindmillStructureFeature.CENTRE_OFFSET);
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

	public static BlockPos poweredHubPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0, 10, 5);
	}

	public static BlockPos pantryPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				3, 1, 3);
	}

	public static BlockPos reloadSentinelPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				2, 14, 0);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		clearSite(world, centre, rotation, bounds);
		buildTower(world, centre, rotation, bounds);
		buildMillRoom(world, random, centre, rotation,
				bounds);
		buildSails(world, centre, rotation, bounds);
		buildGrounds(world, centre, rotation, bounds);
		return true;
	}

	private static void clearSite(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		fill(world, bounds, centre, rotation,
				-9, 1, -9, 9, 19, 9,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildTower(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState gingerbread =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		BlockState candyCane =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		BlockState glass =
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState();
		BlockState icing =
				CakeWorldBlocks.ICING.get()
						.defaultBlockState();

		fill(world, bounds, centre, rotation,
				-4, 0, -4, 4, 0, 4, wafer);
		for (int y = 1; y <= 12; y++) {
			for (int x = -4; x <= 4; x++) {
				set(world, bounds, centre, rotation,
						x, y, -4, gingerbread);
				set(world, bounds, centre, rotation,
						x, y, 4, gingerbread);
			}
			for (int z = -3; z <= 3; z++) {
				set(world, bounds, centre, rotation,
						-4, y, z, gingerbread);
				set(world, bounds, centre, rotation,
						4, y, z, gingerbread);
			}
		}
		fill(world, bounds, centre, rotation,
				-1, 1, 4, 1, 3, 4,
				Blocks.AIR.defaultBlockState());
		fill(world, bounds, centre, rotation,
				-1, 7, -4, 1, 9, -4,
				Blocks.AIR.defaultBlockState());
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				fill(world, bounds, centre, rotation,
						x, 0, z, x, 13, z,
						candyCane);
				fillSupportDown(world, bounds,
						centre, rotation, x, z,
						candyCane);
			}
		}
		for (int y : new int[] {4, 8}) {
			for (int x : new int[] {-2, 2}) {
				set(world, bounds, centre, rotation,
						x, y, -4, glass);
				set(world, bounds, centre, rotation,
						x, y, 4, glass);
			}
			for (int z : new int[] {-2, 2}) {
				set(world, bounds, centre, rotation,
						-4, y, z, glass);
				set(world, bounds, centre, rotation,
						4, y, z, glass);
			}
		}

		fill(world, bounds, centre, rotation,
				-3, 6, -3, 3, 6, 3, wafer);
		fill(world, bounds, centre, rotation,
				3, 6, 2, 3, 6, 2,
				Blocks.AIR.defaultBlockState());
		for (int y = 1; y <= 6; y++) {
			set(world, bounds, centre, rotation,
					3, y, 2,
					Blocks.LADDER.defaultBlockState()
							.setValue(
									LadderBlock.FACING,
									Direction.WEST));
		}

		fill(world, bounds, centre, rotation,
				-5, 13, -5, 5, 13, 5, wafer);
		fill(world, bounds, centre, rotation,
				-4, 14, -4, 4, 14, 4, icing);
		fill(world, bounds, centre, rotation,
				-3, 15, -3, 3, 15, 3, wafer);
		fill(world, bounds, centre, rotation,
				-2, 16, -2, 2, 16, 2, icing);
		fill(world, bounds, centre, rotation,
				0, 17, 0, 0, 19, 0,
				candyCane);

		fill(world, bounds, centre, rotation,
				-3, 6, -5, 3, 6, -5, wafer);
		for (int x = -3; x <= 3; x++) {
			if (Math.abs(x) != 0) {
				set(world, bounds, centre, rotation,
						x, 7, -5,
						candyCane);
			}
		}
	}

	private static void buildMillRoom(
			WorldGenLevel world, Random random,
			BlockPos centre, Rotation rotation,
			BoundingBox bounds) {
		BlockState pipeY =
				CakeWorldBlocks.SYRUP_PIPE.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		BlockState pipeX =
				CakeWorldBlocks.SYRUP_PIPE.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.X);
		BlockState pipeZ =
				CakeWorldBlocks.SYRUP_PIPE.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Z);
		for (int y = 2; y <= 9; y++) {
			set(world, bounds, centre, rotation,
					0, y, 3, pipeY);
		}
		for (int z = -2; z <= 3; z++) {
			set(world, bounds, centre, rotation,
					0, 2, z, pipeZ);
		}
		for (int x = -3; x <= 3; x++) {
			set(world, bounds, centre, rotation,
					x, 2, -2, pipeX);
		}
		set(world, bounds, centre, rotation,
				-3, 1, -3,
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				-1, 1, -3,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				1, 1, -3,
				CakeWorldBlocks.CANDY_COOKER.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				3, 1, -3,
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				-3, 1, 1,
				CakeWorldBlocks.COOKBOOK_KIOSK.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				3, 1, 1,
				CakeWorldBlocks.SODA_FOUNTAIN.get()
						.defaultBlockState());
		BlockPos pantry = transform(
				centre, rotation, 3, 1, 3);
		if (bounds.isInside(pantry)) {
			world.setBlock(pantry,
					Blocks.CHEST.defaultBlockState()
							.rotate(rotation),
					2);
			RandomizableContainerBlockEntity
					.setLootTable(world, random,
							pantry, LOOT_ID);
		}
		set(world, bounds, centre, rotation,
				0, 12, 0,
				Blocks.LANTERN.defaultBlockState()
						.setValue(
								LanternBlock.HANGING,
								true));
	}

	private static void buildSails(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		BlockState horizontalSpoke =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.X);
		BlockState verticalSpoke =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		for (int distance = 1;
				distance <= 7; distance++) {
			for (int sign : new int[] {-1, 1}) {
				int horizontal = sign * distance;
				set(world, bounds, centre, rotation,
						horizontal, 10, 5,
						horizontalSpoke);
				set(world, bounds, centre, rotation,
						horizontal, 9, 5, wafer);
				set(world, bounds, centre, rotation,
						horizontal, 11, 5, wafer);

				int vertical = 10
						+ sign * distance;
				set(world, bounds, centre, rotation,
						0, vertical, 5,
						verticalSpoke);
				set(world, bounds, centre, rotation,
						-1, vertical, 5, wafer);
				set(world, bounds, centre, rotation,
						1, vertical, 5, wafer);
			}
		}
		for (BlockPos tip : List.of(
				new BlockPos(-8, 10, 5),
				new BlockPos(8, 10, 5),
				new BlockPos(0, 2, 5),
				new BlockPos(0, 18, 5))) {
			set(world, bounds, centre, rotation,
					tip.getX(), tip.getY(),
					tip.getZ(),
					((tip.getX() + tip.getY())
							& 1) == 0
									? CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get()
											.defaultBlockState()
									: CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get()
											.defaultBlockState());
		}

		set(world, bounds, centre, rotation,
				0, 10, 4,
				Blocks.REDSTONE_BLOCK
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				0, 10, 5,
				CakeWorldBlocks.WAFER_WINDMILL.get()
						.defaultBlockState()
						.setValue(
								WaferWindmillBlock.FACING,
								Direction.SOUTH)
						.setValue(
								WaferWindmillBlock.POWERED,
								true));
	}

	private static void buildGrounds(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		for (int z = 5; z <= 9; z++) {
			set(world, bounds, centre, rotation,
					0, 0, z,
					CakeWorldBlocks.BISCUIT_CRUMBS
							.get().defaultBlockState());
		}
		fill(world, bounds, centre, rotation,
				-2, 0, -8, 2, 0, -6,
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState());
		BlockState sponge =
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState();
		BlockState matureSprout =
				CakeWorldBlocks.CANDY_SPROUT.get()
						.defaultBlockState()
						.setValue(
								net.minecraft.world.level
										.block.CropBlock.AGE,
								7);
		for (int x = 6; x <= 9; x++) {
			for (int z = -3; z <= 3; z++) {
				set(world, bounds, centre, rotation,
						x, 0, z, sponge);
				if (((x + z) & 1) == 0) {
					set(world, bounds, centre,
							rotation,
							x, 1, z,
							matureSprout);
				}
			}
		}
	}

	private static void fillSupportDown(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int x, int z, BlockState support) {
		BlockPos cursor = transform(
				centre, rotation, x, -1, z);
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
				centre.getX() - 10,
				world.getMinBuildHeight(),
				centre.getZ() - 10,
				centre.getX() + 10,
				centre.getY() + 19,
				centre.getZ() + 10);
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
