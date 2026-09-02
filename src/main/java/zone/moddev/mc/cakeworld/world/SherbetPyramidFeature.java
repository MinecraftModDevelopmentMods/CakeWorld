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
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
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
 * First edible Desert Pyramid counterpart.
 *
 * <p>The fixed first pyramid keeps the recognisable buried pressure-plate,
 * TNT and four-cache discovery, but reduces nine TNT blocks to one clearly
 * marked environmental charge. Gummy cushioning, blast-separated treasure,
 * and a second recovery shaft make the peril legible and recoverable.</p>
 */
public final class SherbetPyramidFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("sherbet_pyramid_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("sherbet_pyramid_structure");
	public static final ResourceLocation POOL_ID =
			id("sherbet_pyramid/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("sherbet_pyramid");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("sherbet_pyramids");
	public static final ResourceLocation LOOT_ID =
			id("chests/sherbet_pyramid");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(20, 24, 20);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/sherbet_pyramid"));
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
	public static final SherbetPyramidFeature FEATURE =
			new SherbetPyramidFeature();
	public static final SherbetPyramidStructureFeature
			STRUCTURE_FEATURE =
			new SherbetPyramidStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private SherbetPyramidFeature() {
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
								SherbetPyramidFeature>(
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
								14357617)));
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
				context.origin().offset(
						10,
						SherbetPyramidStructureFeature
								.BURIED_DEPTH,
						10));
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		return buildAt(world, random,
				origin.offset(
						10,
						SherbetPyramidStructureFeature
								.BURIED_DEPTH,
						10),
				generationBounds);
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos surfaceCentre) {
		return buildAt(world, random, surfaceCentre,
				new BoundingBox(
						surfaceCentre.getX() - 10,
						surfaceCentre.getY()
								- SherbetPyramidStructureFeature
										.BURIED_DEPTH,
						surfaceCentre.getZ() - 10,
						surfaceCentre.getX() + 10,
						surfaceCentre.getY() + 10,
						surfaceCentre.getZ() + 10));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		buildPyramid(world, centre, generationBounds);
		buildEntrance(world, centre, generationBounds);
		buildTreasureChamber(world, centre,
				generationBounds);
		buildCentralShaft(world, centre,
				generationBounds);
		buildRecoveryShaft(world, centre,
				generationBounds);
		buildFizzyFossils(world, centre,
				generationBounds);
		buildWindowsAndCrown(world, centre,
				generationBounds);
		placeLoot(world, generationBounds, random,
				centre.offset(0, -11, -5),
				Direction.SOUTH);
		placeLoot(world, generationBounds, random,
				centre.offset(0, -11, 5),
				Direction.NORTH);
		placeLoot(world, generationBounds, random,
				centre.offset(-5, -11, 0),
				Direction.EAST);
		placeLoot(world, generationBounds, random,
				centre.offset(5, -11, 0),
				Direction.WEST);
		return true;
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos surfaceCentre,
			BoundingBox generationBounds) {
		return buildAt(world, random, surfaceCentre,
				generationBounds);
	}

	private static void buildPyramid(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState pressedSherbet =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		fill(world, bounds, centre,
				-10, 0, -10, 10, 0, 10,
				pressedSherbet);
		for (int y = 1; y <= 9; y++) {
			int radius = 10 - y;
			fill(world, bounds, centre,
					-radius, y, -radius,
					radius, y, radius,
					pressedSherbet);
			if (radius > 1) {
				fill(world, bounds, centre,
						-radius + 1, y,
						-radius + 1,
						radius - 1, y,
						radius - 1,
						Blocks.AIR.defaultBlockState());
			}
			BlockState stripe = switch (y % 3) {
			case 0 -> CakeWorldBlocks
					.RASPBERRY_GUMMY_BLOCK.get()
					.defaultBlockState();
			case 1 -> CakeWorldBlocks
					.BLUEBERRY_GUMMY_BLOCK.get()
					.defaultBlockState();
			default -> CakeWorldBlocks
					.GRAPE_GUMMY_BLOCK.get()
					.defaultBlockState();
			};
			set(world, bounds,
					centre.offset(0, y, radius),
					stripe, 2);
			set(world, bounds,
					centre.offset(radius, y, 0),
					stripe, 2);
			set(world, bounds,
					centre.offset(-radius, y, 0),
					stripe, 2);
			set(world, bounds,
					centre.offset(0, y, -radius),
					stripe, 2);
		}
	}

	private static void buildEntrance(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		fill(world, bounds, centre,
				-2, 1, -10, 2, 4, -3,
				Blocks.AIR.defaultBlockState());
		fill(world, bounds, centre,
				-2, 0, -10, 2, 0, -3,
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState());
		for (int x : new int[] {-3, 3}) {
			fill(world, bounds, centre,
					x, 1, -7, x, 5, -7,
					CakeWorldBlocks
							.CANDY_CANE_PILLAR.get()
							.defaultBlockState());
		}
	}

	private static void buildTreasureChamber(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState pressedSherbet =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		fill(world, bounds, centre,
				-6, -13, -6, 6, -7, 6,
				pressedSherbet);
		fill(world, bounds, centre,
				-5, -11, -5, 5, -8, 5,
				Blocks.AIR.defaultBlockState());

		BlockState[] warning = {
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK
						.get().defaultBlockState(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState()
		};
		int index = 0;
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (Math.max(Math.abs(x),
						Math.abs(z)) == 2) {
					set(world, bounds,
							centre.offset(
									x, -12, z),
							warning[index++
									% warning.length],
							2);
				}
			}
		}
		set(world, bounds, centre.offset(0, -13, 0),
				Blocks.TNT.defaultBlockState(), 2);
		set(world, bounds, centre.offset(0, -12, 0),
				pressedSherbet, 2);
		set(world, bounds, centre.offset(0, -11, 0),
				Blocks.STONE_PRESSURE_PLATE
						.defaultBlockState(),
				2);
		for (BlockPos shield : List.of(
				centre.offset(-3, -11, 0),
				centre.offset(3, -11, 0),
				centre.offset(0, -11, -3),
				centre.offset(0, -11, 3))) {
			set(world, bounds, shield,
					CakeWorldBlocks.ROCK_CANDY.get()
							.defaultBlockState(),
					2);
		}
	}

	private static void buildCentralShaft(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		fill(world, bounds, centre,
				-1, -10, -1, 1, 4, 1,
				Blocks.AIR.defaultBlockState());
		BlockState support =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		BlockState ladder = Blocks.LADDER
				.defaultBlockState()
				.setValue(LadderBlock.FACING,
						Direction.WEST);
		for (int y = -10; y <= 3; y++) {
			set(world, bounds,
					centre.offset(2, y, 0),
					y == 0
							? CakeWorldBlocks
									.MARSHMALLOW.get()
									.defaultBlockState()
							: support,
					2);
			set(world, bounds,
					centre.offset(1, y, 0),
					ladder, 2);
		}
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (Math.max(Math.abs(x),
						Math.abs(z)) == 2) {
					set(world, bounds,
							centre.offset(x, 0, z),
							CakeWorldBlocks
									.MARSHMALLOW
									.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static void buildRecoveryShaft(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState support =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		BlockState ladder = Blocks.LADDER
				.defaultBlockState()
				.setValue(LadderBlock.FACING,
						Direction.WEST);
		for (int y = -10; y <= 2; y++) {
			set(world, bounds,
					centre.offset(5, y, 5),
					Blocks.AIR.defaultBlockState(),
					2);
			set(world, bounds,
					centre.offset(6, y, 5),
					support, 2);
			set(world, bounds,
					centre.offset(5, y, 5),
					ladder, 2);
		}
		fill(world, bounds, centre,
				3, 0, -3, 4, 0, 5,
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState());
	}

	private static void buildFizzyFossils(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState bone = Blocks.BONE_BLOCK
				.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS,
						Direction.Axis.X);
		for (int x : new int[] {-6, -5, -4, -3,
				3, 4, 5, 6}) {
			set(world, bounds,
					centre.offset(x, 1, 3),
					bone, 2);
		}
		for (int x : new int[] {-6, -3, 3, 6}) {
			set(world, bounds,
					centre.offset(x, 2, 3),
					CakeWorldBlocks.FIZZY_PEARL.get()
							.defaultBlockState(),
					2);
		}
		for (int x : new int[] {-5, -4, -3, 3, 4, 5}) {
			set(world, bounds,
					centre.offset(x, 3, 3),
					CakeWorldBlocks
							.SPRINKLE_CLUSTER.get()
							.defaultBlockState(),
					2);
		}
	}

	private static void buildWindowsAndCrown(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState glass =
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState();
		for (int offset : new int[] {-2, 0, 2}) {
			set(world, bounds,
					centre.offset(offset, 3, 7),
					glass, 2);
			set(world, bounds,
					centre.offset(7, 3, offset),
					glass, 2);
			set(world, bounds,
					centre.offset(-7, 3, offset),
					glass, 2);
		}
		set(world, bounds, centre.offset(0, 10, 0),
				CakeWorldBlocks.FIZZY_PEARL.get()
						.defaultBlockState(),
				2);
	}

	private static void placeLoot(
			WorldGenLevel world, BoundingBox bounds,
			Random random, BlockPos position,
			Direction facing) {
		if (!bounds.isInside(position)) {
			return;
		}
		world.setBlock(position,
				Blocks.BARREL.defaultBlockState()
						.setValue(BarrelBlock.FACING,
								facing),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, position, LOOT_ID);
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
