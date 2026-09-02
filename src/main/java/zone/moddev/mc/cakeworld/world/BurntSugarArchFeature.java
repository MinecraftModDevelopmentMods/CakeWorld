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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
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
 * First edible Ruined Portal counterpart.
 *
 * <p>The fixed first ruin keeps a recognisable incomplete Obsidian frame,
 * Crying Obsidian, a scorched spread and a repair chest. The only four missing
 * frame blocks are guaranteed in the chest with Flint and Steel, so the
 * vanilla portal can genuinely be completed without a disguised progression
 * dead end.</p>
 */
public final class BurntSugarArchFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("burnt_sugar_arch_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("burnt_sugar_arch_structure");
	public static final ResourceLocation POOL_ID =
			id("burnt_sugar_arch/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("burnt_sugar_arch");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("burnt_sugar_arches");
	public static final ResourceLocation LOOT_ID =
			id("chests/burnt_sugar_arch");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(16, 16, 16);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/burnt_sugar_arch"));
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
	public static final BurntSugarArchFeature FEATURE =
			new BurntSugarArchFeature();
	public static final BurntSugarArchStructureFeature
			STRUCTURE_FEATURE =
			new BurntSugarArchStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private BurntSugarArchFeature() {
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
								BurntSugarArchFeature>(
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
								40, 15,
								RandomSpreadType.LINEAR,
								34222645)));
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
				8,
				BurntSugarArchStructureFeature
						.SURFACE_OFFSET,
				8);
		return buildAt(context.level(), context.random(),
				centre, isNether(context.level()));
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		BlockPos centre = origin.offset(
				8,
				BurntSugarArchStructureFeature
						.SURFACE_OFFSET,
				8);
		return buildAt(world, random, centre,
				isNether(world), generationBounds);
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, boolean nether) {
		return buildAt(world, random, centre, nether,
				fullBounds(centre));
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				isNether(world), generationBounds);
	}

	public static List<BlockPos> portalGaps(
			BlockPos centre) {
		return List.of(
				centre.offset(-1, 1, 0),
				centre.offset(0, 7, 0),
				centre.offset(-3, 4, 0),
				centre.offset(2, 5, 0));
	}

	public static BlockPos portalInterior(
			BlockPos centre) {
		return centre.offset(-2, 2, 0);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, boolean nether,
			BoundingBox bounds) {
		clearApproach(world, centre, nether, bounds);
		buildScorchedGround(world, centre, nether,
				bounds);
		buildDecorativeArch(world, centre, bounds);
		buildPartialPortal(world, centre, bounds);
		placeRepairChest(world, random, centre,
				bounds);
		return true;
	}

	private static BoundingBox fullBounds(
			BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 8,
				centre.getY()
						- BurntSugarArchStructureFeature
								.SURFACE_OFFSET,
				centre.getZ() - 8,
				centre.getX() + 8,
				centre.getY() + 12,
				centre.getZ() + 8);
	}

	private static void clearApproach(
			WorldGenLevel world, BlockPos centre,
			boolean nether, BoundingBox bounds) {
		int horizontal = nether ? 7 : 6;
		fill(world, bounds, centre,
				-horizontal, 1, -4,
				horizontal, nether ? 12 : 10, 4,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildScorchedGround(
			WorldGenLevel world, BlockPos centre,
			boolean nether, BoundingBox bounds) {
		BlockState burnt =
				CakeWorldBlocks.BURNT_SUGAR_ROCK
						.get().defaultBlockState();
		BlockState edge = nether
				? CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState()
				: CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		fill(world, bounds, centre,
				-7, 0, -5, 7, 0, 5, burnt);
		for (int x = -7; x <= 7; x++) {
			set(world, bounds,
					centre.offset(x, 0, -5),
					edge);
			set(world, bounds,
					centre.offset(x, 0, 5),
					edge);
		}
		for (int z = -4; z <= 4; z++) {
			set(world, bounds,
					centre.offset(-7, 0, z),
					edge);
			set(world, bounds,
					centre.offset(7, 0, z),
					edge);
		}
		for (BlockPos magma : List.of(
				centre.offset(-5, 1, 4),
				centre.offset(4, 1, -4))) {
			set(world, bounds, magma,
					Blocks.MAGMA_BLOCK
							.defaultBlockState());
		}
		for (BlockPos rescue : List.of(
				centre.offset(-4, 1, 4),
				centre.offset(3, 1, -4))) {
			set(world, bounds, rescue,
					CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState());
		}
		for (BlockPos crying : List.of(
				centre.offset(-6, 1, -3),
				centre.offset(6, 1, 3))) {
			set(world, bounds, crying,
					Blocks.CRYING_OBSIDIAN
							.defaultBlockState());
		}
		BlockState treasure = nether
				? CakeWorldBlocks.FUDGE_GOLD.get()
						.defaultBlockState()
				: CakeWorldBlocks.HONEYCOMB_GOLD.get()
						.defaultBlockState();
		set(world, bounds,
				centre.offset(-4, 1, -3),
				treasure);
		set(world, bounds,
				centre.offset(4, 1, 3),
				treasure);
	}

	private static void buildDecorativeArch(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState burnt =
				CakeWorldBlocks.BURNT_SUGAR_ROCK
						.get().defaultBlockState();
		fill(world, bounds, centre,
				-5, 1, 0, -5, 9, 0, burnt);
		fill(world, bounds, centre,
				4, 1, 0, 4, 9, 0, burnt);
		fill(world, bounds, centre,
				-5, 9, 0, 4, 9, 0, burnt);
		set(world, bounds,
				centre.offset(-4, 10, 0), burnt);
		set(world, bounds,
				centre.offset(3, 10, 0), burnt);
	}

	private static void buildPartialPortal(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockState obsidian =
				Blocks.OBSIDIAN.defaultBlockState();
		fill(world, bounds, centre,
				-3, 1, 0, 2, 1, 0,
				obsidian);
		fill(world, bounds, centre,
				-3, 7, 0, 2, 7, 0,
				obsidian);
		fill(world, bounds, centre,
				-3, 2, 0, -3, 6, 0,
				obsidian);
		fill(world, bounds, centre,
				2, 2, 0, 2, 6, 0,
				obsidian);
		fill(world, bounds, centre,
				-2, 2, 0, 1, 6, 0,
				Blocks.AIR.defaultBlockState());
		for (BlockPos gap : portalGaps(centre)) {
			set(world, bounds, gap,
					Blocks.AIR.defaultBlockState());
		}
	}

	private static void placeRepairChest(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		BlockPos chest = centre.offset(5, 1, 3);
		if (!bounds.isInside(chest)) {
			return;
		}
		world.setBlock(chest,
				Blocks.CHEST.defaultBlockState()
						.setValue(ChestBlock.FACING,
								Direction.WEST),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, chest, LOOT_ID);
	}

	private static boolean isNether(
			WorldGenLevel world) {
		return world.getLevel().dimension()
				== Level.NETHER;
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
							state);
				}
			}
		}
	}

	private static void set(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos position, BlockState state) {
		if (bounds.isInside(position)) {
			world.setBlock(position, state, 2);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
