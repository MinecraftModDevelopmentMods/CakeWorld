package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
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
import net.minecraft.world.phys.AABB;

/**
 * First edible Woodland Mansion counterpart.
 *
 * <p>The fixed first plan is a three-storey, correctly bounded manor with
 * ordinary rooms, two kitchens, a sealed secret kitchen, rare-recipe cache,
 * Cookbook library and persistent CakeWorld raiders. Later variants can
 * replace the plan without changing its public configured-structure or map
 * identity.</p>
 */
public final class GrandGingerbreadManorFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("grand_gingerbread_manor_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("grand_gingerbread_manor_structure");
	public static final ResourceLocation POOL_ID =
			id("grand_gingerbread_manor/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("grand_gingerbread_manor");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("grand_gingerbread_manors");
	public static final ResourceLocation LOOT_ID =
			id("chests/grand_gingerbread_manor");
	public static final ResourceLocation SECRET_LOOT_ID =
			id("chests/grand_gingerbread_manor_secret");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(48, 29, 48);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/grand_gingerbread_manor"));
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
	public static final GrandGingerbreadManorFeature FEATURE =
			new GrandGingerbreadManorFeature();
	public static final GrandGingerbreadManorStructureFeature
			STRUCTURE_FEATURE =
			new GrandGingerbreadManorStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private GrandGingerbreadManorFeature() {
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
								GrandGingerbreadManorFeature>(
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
								80, 20,
								RandomSpreadType.TRIANGULAR,
								10387319)));
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
		return buildAt(context.level(), context.random(),
				context.origin().offset(24, 0, 24));
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		return buildAt(world, random,
				origin.offset(24, 0, 24),
				generationBounds);
	}

	/**
	 * Builds the deterministic first manor around its ground-floor centre.
	 */
	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				new BoundingBox(
						centre.getX() - 24,
						world.getMinBuildHeight(),
						centre.getZ() - 24,
						centre.getX() + 24,
						centre.getY() + 30,
						centre.getZ() + 24));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				generationBounds, true);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds,
			boolean spawnInhabitants) {
		prepareSite(world, centre, generationBounds);
		buildShell(world, centre, generationBounds);
		buildInterior(world, centre, generationBounds);
		buildPorch(world, centre, generationBounds);
		buildGrandKitchen(world, centre, generationBounds);
		buildLibrary(world, centre, generationBounds);
		buildSecretKitchen(world, random, centre,
				generationBounds);
		buildDiningHall(world, centre, generationBounds);
		buildRoof(world, centre, generationBounds);
		placeLoot(world, generationBounds, random,
				centre.offset(14, 1, 11), LOOT_ID);
		if (spawnInhabitants) {
			spawnInhabitants(world, centre,
					generationBounds);
		}
		return true;
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				generationBounds, false);
	}

	private static void prepareSite(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		BlockState foundation =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState floor =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		for (int x = -22; x <= 22; x++) {
			for (int z = -20; z <= 20; z++) {
				BlockPos floorPosition =
						centre.offset(x, 0, z);
				if (!generationBounds.isInside(
						floorPosition)) {
					continue;
				}
				world.setBlock(floorPosition, floor, 2);
				for (int y = 1; y <= 29; y++) {
					world.setBlock(
							floorPosition.above(y),
							Blocks.AIR
									.defaultBlockState(),
							2);
				}
				BlockPos support = floorPosition.below();
				while (support.getY()
						> world.getMinBuildHeight()
						&& (world.isEmptyBlock(support)
								|| world.getBlockState(
										support)
										.getMaterial()
										.isLiquid())) {
					world.setBlock(support,
							foundation, 2);
					support = support.below();
				}
			}
		}
		for (int z = -24; z <= -19; z++) {
			for (int x = -2; x <= 2; x++) {
				setBlock(world, generationBounds,
						centre.offset(x, 0, z),
						x == 0
								? CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get()
										.defaultBlockState()
										.setValue(
												RotatedPillarBlock
														.AXIS,
												Direction.Axis.Z)
								: CakeWorldBlocks
										.BISCUIT_CRUMBS
										.get()
										.defaultBlockState());
				for (int y = 1; y <= 5; y++) {
					setBlock(world, generationBounds,
							centre.offset(x, y, z),
							Blocks.AIR
									.defaultBlockState());
				}
			}
		}
	}

	private static void buildShell(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int floorY : new int[] {0, 8, 16, 24}) {
			fill(world, generationBounds, centre,
					-20, floorY, -18,
					20, floorY, 18, wafer);
		}

		BlockState gingerbread =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState candyGlass =
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState();
		BlockState pillar =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		for (int y = 1; y <= 23; y++) {
			for (int x = -20; x <= 20; x++) {
				for (int z : new int[] {-18, 18}) {
					setBlock(world, generationBounds,
							centre.offset(x, y, z),
							exteriorState(x, y, z,
									gingerbread,
									candyGlass,
									pillar));
				}
			}
			for (int z = -17; z <= 17; z++) {
				for (int x : new int[] {-20, 20}) {
					setBlock(world, generationBounds,
							centre.offset(x, y, z),
							exteriorState(x, y, z,
									gingerbread,
									candyGlass,
									pillar));
				}
			}
		}

		for (int x = -1; x <= 0; x++) {
			for (int y = 1; y <= 3; y++) {
				setBlock(world, generationBounds,
						centre.offset(x, y, -18),
						Blocks.AIR
								.defaultBlockState());
			}
		}
		placeDoubleDoor(world, generationBounds,
				centre.offset(-1, 1, -18));
	}

	private static BlockState exteriorState(
			int x, int y, int z,
			BlockState gingerbread,
			BlockState candyGlass,
			BlockState pillar) {
		if (Math.abs(x) == 20
				&& Math.abs(z) == 18) {
			return pillar;
		}
		int floorY = Math.floorMod(y, 8);
		boolean windowHeight =
				floorY == 3 || floorY == 4;
		boolean windowColumn =
				(Math.abs(x) < 18
						&& Math.floorMod(x, 6) == 0)
				|| (Math.abs(z) < 16
						&& Math.floorMod(z, 6) == 0);
		return windowHeight && windowColumn
				? candyGlass : gingerbread;
	}

	private static void buildInterior(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		BlockState wall =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		for (int floorY : new int[] {0, 8, 16}) {
			for (int y = floorY + 1;
					y <= floorY + 7; y++) {
				for (int z = -17; z <= 17; z++) {
					if (!doorGap(z, y - floorY)) {
						setBlock(world,
								generationBounds,
								centre.offset(
										-5, y, z),
								wall);
						setBlock(world,
								generationBounds,
								centre.offset(
										5, y, z),
								wall);
					}
				}
				for (int x = -19; x <= 19; x++) {
					if (Math.abs(x) > 5
							&& !doorGap(
									x,
									y - floorY)) {
						setBlock(world,
								generationBounds,
								centre.offset(
										x, y, 0),
								wall);
					}
				}
			}
		}

		buildStaircase(world, generationBounds,
				centre, 0);
		buildStaircase(world, generationBounds,
				centre, 8);
	}

	private static boolean doorGap(
			int coordinate, int relativeY) {
		return relativeY <= 3
				&& (Math.abs(coordinate + 11) <= 1
						|| Math.abs(coordinate) <= 1
						|| Math.abs(coordinate - 11)
								<= 1);
	}

	private static void buildStaircase(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre, int floorY) {
		fill(world, generationBounds, centre,
				-2, floorY + 8, 4,
				2, floorY + 8, 13,
				Blocks.AIR.defaultBlockState());
		BlockState stair =
				Blocks.DARK_OAK_STAIRS
						.defaultBlockState()
						.setValue(StairBlock.FACING,
								Direction.NORTH);
		BlockState rail =
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState();
		for (int step = 0; step < 8; step++) {
			for (int x = -1; x <= 1; x++) {
				setBlock(world, generationBounds,
						centre.offset(x,
								floorY + step + 1,
								5 + step),
						stair);
			}
			for (int x : new int[] {-2, 2}) {
				setBlock(world, generationBounds,
						centre.offset(x,
								floorY + step + 1,
								5 + step),
						rail);
			}
		}
	}

	private static void buildPorch(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		BlockState pillar =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		fill(world, generationBounds, centre,
				-5, 0, -23, 5, 0, -18, wafer);
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-23, -18}) {
				fill(world, generationBounds, centre,
						x, 1, z, x, 6, z,
						pillar);
			}
		}
		fill(world, generationBounds, centre,
				-6, 6, -24, 6, 6, -17,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState());
		fill(world, generationBounds, centre,
				-6, 7, -24, 6, 7, -17,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState());
	}

	private static void buildGrandKitchen(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		for (int x = -17; x <= -8; x += 3) {
			setBlock(world, generationBounds,
					centre.offset(x, 1, 15),
					Blocks.CRAFTING_TABLE
							.defaultBlockState());
		}
		setBlock(world, generationBounds,
				centre.offset(-16, 1, 12),
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-13, 1, 12),
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-10, 1, 12),
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-8, 1, 12),
				CakeWorldBlocks.CANDY_COOKER.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-15, 1, 8),
				Blocks.CAKE.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-11, 1, 8),
				Blocks.BARREL.defaultBlockState());
	}

	private static void buildLibrary(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		for (int x = 8; x <= 18; x += 2) {
			for (int y = 1; y <= 3; y++) {
				setBlock(world, generationBounds,
						centre.offset(x, y, 15),
						Blocks.BOOKSHELF
								.defaultBlockState());
			}
		}
		for (int z = 6; z <= 14; z += 4) {
			setBlock(world, generationBounds,
					centre.offset(18, 1, z),
					CakeWorldBlocks
							.COOKBOOK_LIBRARY.get()
							.defaultBlockState());
		}
		setBlock(world, generationBounds,
				centre.offset(9, 1, 8),
				CakeWorldBlocks.COOKBOOK_KIOSK.get()
						.defaultBlockState());
	}

	private static void buildSecretKitchen(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		BlockState wall =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		for (int y = 17; y <= 23; y++) {
			fill(world, generationBounds, centre,
					-19, y, 4, -6, y, 4, wall);
			fill(world, generationBounds, centre,
					-6, y, 4, -6, y, 17, wall);
		}
		for (int z = 8; z <= 12; z += 2) {
			for (int y = 17; y <= 19; y++) {
				setBlock(world, generationBounds,
						centre.offset(-5, y, z),
						Blocks.BOOKSHELF
								.defaultBlockState());
			}
		}
		setBlock(world, generationBounds,
				centre.offset(-16, 17, 13),
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-13, 17, 13),
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-10, 17, 13),
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(-7, 17, 13),
				CakeWorldBlocks.CANDY_COOKER.get()
						.defaultBlockState());
		placeLoot(world, generationBounds, random,
				centre.offset(-13, 17, 9),
				SECRET_LOOT_ID);
	}

	private static void buildDiningHall(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		for (int z = -14; z <= -4; z += 2) {
			for (int x = 9; x <= 15; x += 3) {
				setBlock(world, generationBounds,
						centre.offset(x, 1, z),
						Blocks.DARK_OAK_FENCE
								.defaultBlockState());
				setBlock(world, generationBounds,
						centre.offset(x, 2, z),
						Blocks.DARK_OAK_PRESSURE_PLATE
								.defaultBlockState());
			}
		}
		setBlock(world, generationBounds,
				centre.offset(12, 2, -9),
				Blocks.CAKE.defaultBlockState());
	}

	private static void buildRoof(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		BlockState[] tiers = {
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get()
						.defaultBlockState()
		};
		for (int tier = 0; tier < tiers.length; tier++) {
			fill(world, generationBounds, centre,
					-22 + tier * 3,
					24 + tier,
					-20 + tier * 3,
					22 - tier * 3,
					24 + tier,
					20 - tier * 3,
					tiers[tier]);
		}
		fill(world, generationBounds, centre,
				-10, 29, -8, 10, 29, 8,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState());
		for (int y = 25; y <= 29; y++) {
			setBlock(world, generationBounds,
					centre.offset(14, y, 8),
					CakeWorldBlocks
							.CANDY_CANE_PILLAR
							.get().defaultBlockState());
		}
	}

	private static void placeDoubleDoor(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos leftBottom) {
		BlockState door =
				Blocks.DARK_OAK_DOOR.defaultBlockState()
						.setValue(DoorBlock.FACING,
								Direction.NORTH);
		placeDoorHalf(world, generationBounds,
				leftBottom,
				door.setValue(DoorBlock.HINGE,
						DoorHingeSide.LEFT));
		placeDoorHalf(world, generationBounds,
				leftBottom.east(),
				door.setValue(DoorBlock.HINGE,
						DoorHingeSide.RIGHT));
	}

	private static void placeDoorHalf(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos bottom, BlockState door) {
		setBlock(world, generationBounds, bottom,
				door.setValue(DoorBlock.HALF,
						DoubleBlockHalf.LOWER));
		setBlock(world, generationBounds, bottom.above(),
				door.setValue(DoorBlock.HALF,
						DoubleBlockHalf.UPPER));
	}

	private static void placeLoot(
			WorldGenLevel world,
			BoundingBox generationBounds,
			Random random, BlockPos position,
			ResourceLocation lootTable) {
		if (!generationBounds.isInside(position)) {
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

	private static void spawnInhabitants(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		spawnRaider(world, generationBounds,
				centre.offset(-14, 1, -10),
				centre,
				CakeWorldEntities
						.ROLLING_PIN_RAIDER.get());
		spawnRaider(world, generationBounds,
				centre.offset(14, 1, -10),
				centre,
				CakeWorldEntities
						.ROLLING_PIN_RAIDER.get());
		spawnRaider(world, generationBounds,
				centre.offset(-14, 9, 10),
				centre,
				CakeWorldEntities
						.ROLLING_PIN_RAIDER.get());
		spawnRaider(world, generationBounds,
				centre.offset(14, 9, 10),
				centre,
				CakeWorldEntities
						.ROLLING_PIN_RAIDER.get());
		spawnRaider(world, generationBounds,
				centre.offset(0, 17, 0),
				centre,
				CakeWorldEntities
						.ROLLING_PIN_RAIDER.get());
		spawnRaider(world, generationBounds,
				centre.offset(-9, 17, 9),
				centre,
				CakeWorldEntities.SOUR_SORCERER.get());
		spawnRaider(world, generationBounds,
				centre.offset(13, 17, 9),
				centre,
				CakeWorldEntities.SOUR_SORCERER.get());
		spawnRaider(world, generationBounds,
				centre.offset(-13, 1, 10),
				centre,
				CakeWorldEntities.BITTER_BAKER.get());
	}

	private static void spawnRaider(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos position, BlockPos centre,
			EntityType<? extends Raider> type) {
		if (!generationBounds.isInside(position)) {
			return;
		}
		world.getLevel().getEntitiesOfClass(
				Raider.class,
				new AABB(position).inflate(1.0D),
				raider -> raider.getType() == type)
				.forEach(Raider::discard);
		Raider raider = type.create(world.getLevel());
		if (raider == null) {
			return;
		}
		raider.moveTo(position, 0.0F, 0.0F);
		raider.finalizeSpawn(world,
				world.getCurrentDifficultyAt(position),
				MobSpawnType.STRUCTURE,
				null, null);
		raider.setPersistenceRequired();
		raider.restrictTo(centre, 48);
		world.addFreshEntity(raider);
	}

	private static void fill(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre,
			int minimumX, int minimumY, int minimumZ,
			int maximumX, int maximumY, int maximumZ,
			BlockState state) {
		for (int x = minimumX; x <= maximumX; x++) {
			for (int y = minimumY; y <= maximumY; y++) {
				for (int z = minimumZ;
						z <= maximumZ; z++) {
					setBlock(world, generationBounds,
							centre.offset(x, y, z),
							state);
				}
			}
		}
	}

	private static void setBlock(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos position, BlockState state) {
		if (generationBounds.isInside(position)) {
			world.setBlock(position, state, 2);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
