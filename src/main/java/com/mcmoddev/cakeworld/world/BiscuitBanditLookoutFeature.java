package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BiscuitBandit;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
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
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * The first Biscuit Bandit Lookout variant.
 *
 * <p>A correctly bounded procedural pool piece retains structure-wide
 * Outpost spawning while building a compact edible watchtower, supply camp,
 * cage and target range. Initial persistent bandits make the landmark
 * readable immediately; the structure spawn override remains the long-term
 * source of its inhabitants.</p>
 */
public final class BiscuitBanditLookoutFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation PIECE_ID =
			id("biscuit_bandit_lookout_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("biscuit_bandit_outpost");
	public static final ResourceLocation POOL_ID =
			id("biscuit_bandit_lookout/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("biscuit_bandit_lookout");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("biscuit_bandit_lookouts");
	public static final ResourceLocation LOOT_ID =
			id("chests/biscuit_bandit_lookout");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(24, 20, 24);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/biscuit_bandit_lookout"));
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
	public static final BiscuitBanditLookoutFeature FEATURE =
			new BiscuitBanditLookoutFeature();
	public static final BiscuitBanditOutpostStructureFeature
			STRUCTURE_FEATURE =
			new BiscuitBanditOutpostStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private BiscuitBanditLookoutFeature() {
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
								BiscuitBanditLookoutFeature>(
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
		Map<MobCategory, StructureSpawnOverride>
				spawnOverrides =
				Map.of(MobCategory.MONSTER,
						new StructureSpawnOverride(
								StructureSpawnOverride
										.BoundingBoxType
										.STRUCTURE,
								WeightedRandomList.create(
										new MobSpawnSettings
												.SpawnerData(
														CakeWorldEntities
																.BISCUIT_BANDIT
																.get(),
														1, 1, 1))));
		configuredStructure = BuiltinRegistries.register(
				BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
				STRUCTURE_KEY,
				STRUCTURE_FEATURE.configured(
						new JigsawConfiguration(pool, 1),
						GENERATES_IN, true,
						spawnOverrides));
		structureSet = BuiltinRegistries.register(
				BuiltinRegistries.STRUCTURE_SETS,
				STRUCTURE_SET_ID,
				new StructureSet(
						configuredStructure,
						new RandomSpreadStructurePlacement(
								32, 8,
								RandomSpreadType.LINEAR,
								1978002)));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	public static Holder<StructureTemplatePool> pool() {
		return pool;
	}

	public static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure() {
		return configuredStructure;
	}

	public static Holder<StructureSet> structureSet() {
		return structureSet;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos centreColumn =
				context.origin().offset(12, 0, 12);
		int surfaceY = world.getHeight(
				Heightmap.Types.WORLD_SURFACE_WG,
				centreColumn.getX(),
				centreColumn.getZ()) - 1;
		return buildAt(world, context.random(),
				new BlockPos(centreColumn.getX(),
						surfaceY, centreColumn.getZ()));
	}

	/**
	 * Builds one deterministic Cookie Forest Lookout around its ground-level
	 * centre.
	 */
	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		prepareClearing(world, centre);
		buildPaths(world, centre);
		buildTower(world, random, centre);
		buildCamp(world, centre.offset(8, 0, -6));
		buildCage(world, centre.offset(-8, 0, 7));
		buildTargets(world, centre.offset(8, 0, 7));

		spawnBandit(world,
				centre.offset(0, 17, -5),
				centre, true);
		spawnBandit(world,
				centre.offset(-2, 17, 1),
				centre, false);
		spawnBandit(world,
				centre.offset(2, 1, -6),
				centre, false);
		spawnBandit(world,
				// Keep authored residents in the structure's originating
				// chunk while WorldGenRegion is still persisting entities.
				centre.offset(3, 1, -6),
				centre, false);
		return true;
	}

	private static void prepareClearing(
			WorldGenLevel world, BlockPos centre) {
		BlockState foundation =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		for (int x = -12; x <= 12; x++) {
			for (int z = -12; z <= 12; z++) {
				BlockPos floor = centre.offset(x, 0, z);
				world.setBlock(floor, foundation, 2);
				for (int y = 1; y <= 20; y++) {
					world.setBlock(floor.above(y),
							Blocks.AIR.defaultBlockState(),
							2);
				}
				for (int y = 1; y <= 8; y++) {
					BlockPos support = floor.below(y);
					if (!world.isEmptyBlock(support)) {
						break;
					}
					world.setBlock(support,
							foundation, 2);
				}
			}
		}
	}

	private static void buildPaths(
			WorldGenLevel world, BlockPos centre) {
		BlockState crumbs =
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState();
		BlockState stripe =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Z);
		for (int offset = -12; offset <= 12;
				offset++) {
			for (int width = -1; width <= 1;
					width++) {
				world.setBlock(
						centre.offset(width, 0,
								offset),
						width == 0 ? stripe : crumbs,
						2);
				world.setBlock(
						centre.offset(offset, 0,
								width),
						crumbs, 2);
			}
		}
	}

	private static void buildTower(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		BlockState gingerbread =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		BlockState glass =
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState();
		BlockState pillar =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();

		for (int y = 0; y <= 16; y += 4) {
			for (int x = -4; x <= 4; x++) {
				for (int z = -4; z <= 4; z++) {
					world.setBlock(
							centre.offset(x, y, z),
							y == 0 ? gingerbread
									: wafer,
							2);
				}
			}
		}
		for (int y = 1; y <= 15; y++) {
			for (int cornerX : List.of(-4, 4)) {
				for (int cornerZ :
						List.of(-4, 4)) {
					world.setBlock(
							centre.offset(
									cornerX, y,
									cornerZ),
							pillar, 2);
				}
			}
			for (int edge = -3; edge <= 3;
					edge++) {
				if (y % 4 == 2
						&& Math.abs(edge) <= 1) {
					world.setBlock(
							centre.offset(edge, y,
									-4),
							glass, 2);
					world.setBlock(
							centre.offset(edge, y,
									4),
							glass, 2);
					world.setBlock(
							centre.offset(-4, y,
									edge),
							glass, 2);
					world.setBlock(
							centre.offset(4, y,
									edge),
							glass, 2);
				} else if (y % 4 != 0) {
					world.setBlock(
							centre.offset(edge, y,
									-4),
							wafer, 2);
					world.setBlock(
							centre.offset(edge, y,
									4),
							wafer, 2);
					world.setBlock(
							centre.offset(-4, y,
									edge),
							wafer, 2);
					world.setBlock(
							centre.offset(4, y,
									edge),
							wafer, 2);
				}
			}
		}
		for (int y = 1; y <= 15; y++) {
			world.setBlock(centre.offset(0, y, 3),
					Blocks.LADDER.defaultBlockState()
							.setValue(LadderBlock.FACING,
									Direction.NORTH),
					2);
		}
		world.setBlock(centre.offset(0, 1, -4),
				Blocks.AIR.defaultBlockState(), 2);
		world.setBlock(centre.offset(0, 2, -4),
				Blocks.AIR.defaultBlockState(), 2);

		for (int x = -6; x <= 6; x++) {
			for (int z = -6; z <= 6; z++) {
				world.setBlock(
						centre.offset(x, 16, z),
						wafer, 2);
				if (Math.abs(x) == 6
						|| Math.abs(z) == 6) {
					world.setBlock(
							centre.offset(x, 17, z),
							pillar, 2);
				}
			}
		}
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				world.setBlock(
						centre.offset(x, 19, z),
						CakeWorldBlocks
								.RASPBERRY_GUMMY_BLOCK
								.get().defaultBlockState(),
						2);
				world.setBlock(
						centre.offset(x, 20, z),
						CakeWorldBlocks.ICING_LAYER
								.get().defaultBlockState(),
						2);
			}
		}
		world.setBlock(centre.offset(0, 17, 0),
				Blocks.CHEST.defaultBlockState()
						.setValue(
								HorizontalDirectionalBlock
										.FACING,
								Direction.SOUTH),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random,
				centre.offset(0, 17, 0),
				LOOT_ID);
	}

	private static void buildCamp(
			WorldGenLevel world, BlockPos centre) {
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlock(centre.offset(x, 0, z),
						wafer, 2);
			}
		}
		for (int x : List.of(-3, 3)) {
			for (int z : List.of(-2, 2)) {
				for (int y = 1; y <= 3; y++) {
					world.setBlock(
							centre.offset(x, y, z),
							CakeWorldBlocks
									.CANDY_CANE_PILLAR
									.get()
									.defaultBlockState(),
							2);
				}
			}
		}
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlock(centre.offset(x, 4, z),
						wafer, 2);
				world.setBlock(centre.offset(x, 5, z),
						CakeWorldBlocks.ICING_LAYER
								.get().defaultBlockState(),
						2);
			}
		}
		world.setBlock(centre.offset(0, 1, 0),
				Blocks.CAMPFIRE.defaultBlockState(), 2);
		world.setBlock(centre.offset(2, 1, 0),
				Blocks.BARREL.defaultBlockState(), 2);
	}

	private static void buildCage(
			WorldGenLevel world, BlockPos centre) {
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks
								.GINGERBREAD_BRICKS
								.get().defaultBlockState(),
						2);
				if (Math.abs(x) == 2
						|| Math.abs(z) == 2) {
					for (int y = 1; y <= 3; y++) {
						world.setBlock(
								centre.offset(
										x, y, z),
								Blocks.IRON_BARS
										.defaultBlockState(),
								2);
					}
				}
			}
		}
		BlockState door =
				Blocks.IRON_DOOR.defaultBlockState()
						.setValue(DoorBlock.FACING,
								Direction.NORTH);
		BlockPos doorBottom = centre.offset(0, 1, -2);
		world.setBlock(doorBottom,
				door.setValue(DoorBlock.HALF,
						DoubleBlockHalf.LOWER),
				2);
		world.setBlock(doorBottom.above(),
				door.setValue(DoorBlock.HALF,
						DoubleBlockHalf.UPPER),
				2);
	}

	private static void buildTargets(
			WorldGenLevel world, BlockPos centre) {
		for (int z = -2; z <= 2; z += 2) {
			world.setBlock(centre.offset(0, 1, z),
					Blocks.TARGET.defaultBlockState(), 2);
			world.setBlock(centre.offset(1, 1, z),
					CakeWorldBlocks
							.CANDY_CANE_PILLAR
							.get().defaultBlockState(),
					2);
		}
	}

	private static void spawnBandit(
			WorldGenLevel world, BlockPos pos,
			BlockPos lookoutCentre, boolean captain) {
		BiscuitBandit bandit =
				CakeWorldEntities.BISCUIT_BANDIT.get()
						.create(world.getLevel());
		if (bandit == null) {
			return;
		}
		bandit.setPos(pos.getX() + 0.5D,
				pos.getY(),
				pos.getZ() + 0.5D);
		if (captain) {
			bandit.setPatrolLeader(true);
		}
		bandit.finalizeSpawn(world,
				world.getCurrentDifficultyAt(pos),
				MobSpawnType.STRUCTURE, null, null);
		if (captain) {
			bandit.setItemSlot(EquipmentSlot.HEAD,
					Raid.getLeaderBannerInstance());
			bandit.setPatrolTarget(lookoutCentre);
		}
		bandit.setPersistenceRequired();
		bandit.restrictTo(lookoutCentre, 32);
		world.addFreshEntity(bandit);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
