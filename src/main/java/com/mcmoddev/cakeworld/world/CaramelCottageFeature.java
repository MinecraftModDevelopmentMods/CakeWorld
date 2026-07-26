package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BitterBaker;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
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
import net.minecraft.world.Container;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.phys.AABB;

/**
 * First edible Swamp Hut counterpart.
 *
 * <p>The cottage preserves the tiny isolated home, working-room furnishings,
 * downward supports, one-time residents and piece-bounded ongoing spawns. Its
 * wet Cookie Forest home is temporary until Caramel Bogs is registered.</p>
 */
public final class CaramelCottageFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("caramel_cottage_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("caramel_cottage_structure");
	public static final ResourceLocation POOL_ID =
			id("caramel_cottage/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("caramel_cottage");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("caramel_cottages");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(14, 11, 14);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/caramel_cottage"));
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
	public static final CaramelCottageFeature FEATURE =
			new CaramelCottageFeature();
	public static final CaramelCottageStructureFeature
			STRUCTURE_FEATURE =
			new CaramelCottageStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private CaramelCottageFeature() {
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
								CaramelCottageFeature>(
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
		Map<MobCategory, StructureSpawnOverride> spawns =
				Map.of(
						MobCategory.MONSTER,
						new StructureSpawnOverride(
								StructureSpawnOverride
										.BoundingBoxType.PIECE,
								WeightedRandomList.create(
										new MobSpawnSettings
												.SpawnerData(
														CakeWorldEntities
																.BITTER_BAKER
																.get(),
														1, 1, 1))),
						MobCategory.CREATURE,
						new StructureSpawnOverride(
								StructureSpawnOverride
										.BoundingBoxType.PIECE,
								WeightedRandomList.create(
										new MobSpawnSettings
												.SpawnerData(
														CakeWorldEntities
																.CUSTARD_CAT
																.get(),
														1, 1, 1))));
		configuredStructure = BuiltinRegistries.register(
				BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
				STRUCTURE_KEY,
				STRUCTURE_FEATURE.configured(
						new JigsawConfiguration(pool, 1),
						GENERATES_IN, spawns));
		structureSet = BuiltinRegistries.register(
				BuiltinRegistries.STRUCTURE_SETS,
				STRUCTURE_SET_ID,
				new StructureSet(
						configuredStructure,
						new RandomSpreadStructurePlacement(
								32, 8,
								RandomSpreadType.LINEAR,
								14357620)));
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
				CaramelCottageStructureFeature
						.CENTRE_OFFSET,
				0,
				CaramelCottageStructureFeature
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
				CaramelCottageStructureFeature
						.CENTRE_OFFSET,
				0,
				CaramelCottageStructureFeature
						.CENTRE_OFFSET);
		boolean built = buildAt(world, random, centre,
				generationBounds, false);
		Rotation rotation =
				orientation(world.getSeed(), centre);
		if (built && generationBounds.isInside(
				residentMarker(centre, rotation))) {
			CaramelCottageResidents.queue(
					world, centre,
					savedBounds(centre));
		}
		return built;
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				fullGenerationBounds(world, centre),
				true);
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		return buildAt(world, random, centre,
				generationBounds, false);
	}

	/**
	 * Rebuilds the complete block envelope without creating or replacing its
	 * one-time residents.
	 */
	public static boolean repairAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				fullGenerationBounds(world, centre),
				false);
	}

	public static Rotation orientation(
			long worldSeed, BlockPos centre) {
		Random random = new Random(
				worldSeed ^ centre.asLong()
						^ 14357620L);
		return Rotation.getRandom(random);
	}

	public static BlockPos residentMarker(
			BlockPos centre, Rotation rotation) {
		return transform(centre, rotation,
				0, 2, -2);
	}

	public static List<BlockPos> residentPositions(
			long worldSeed, BlockPos centre) {
		Rotation rotation =
				orientation(worldSeed, centre);
		return List.of(
				transform(centre, rotation,
						2, 4, -1),
				transform(centre, rotation,
						-2, 4, -1));
	}

	public static List<BlockPos> fluidStoragePositions(
			long worldSeed, BlockPos centre) {
		Rotation rotation =
				orientation(worldSeed, centre);
		return List.of(
				transform(centre, rotation,
						-4, 1, 5),
				transform(centre, rotation,
						3, 1, 5));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds,
			boolean spawnResidents) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		BlockPos marker =
				residentMarker(centre, rotation);
		boolean residentsActivated =
				!spawnResidents
						&& world.getBlockState(marker)
								.is(CakeWorldBlocks
										.GINGERBREAD_BRICKS
										.get());
		clearSite(world, centre, rotation, bounds);
		buildCottage(world, centre, rotation, bounds);
		buildGarden(world, centre, rotation, bounds);
		buildCaramelBasin(world, centre, rotation,
				bounds);
		placeResidentMarker(world, centre, rotation,
				bounds, residentsActivated);
		if (spawnResidents) {
			spawnResidentPair(world, centre,
					savedBounds(centre));
		}
		return true;
	}

	private static void clearSite(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		fill(world, bounds, centre, rotation,
				-6, 1, -6, 6, 11, 6,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildCottage(
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
				-4, 3, -4, 4, 3, 2, wafer);
		for (int y = 4; y <= 7; y++) {
			for (int x = -4; x <= 4; x++) {
				set(world, bounds, centre, rotation,
						x, y, -4, gingerbread);
				set(world, bounds, centre, rotation,
						x, y, 2, gingerbread);
			}
			for (int z = -3; z <= 1; z++) {
				set(world, bounds, centre, rotation,
						-4, y, z, gingerbread);
				set(world, bounds, centre, rotation,
						4, y, z, gingerbread);
			}
		}
		fill(world, bounds, centre, rotation,
				-1, 4, 2, 1, 5, 2,
				Blocks.AIR.defaultBlockState());
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 2}) {
				fill(world, bounds, centre, rotation,
						x, 3, z, x, 8, z,
						candyCane);
				fillSupportDown(world, bounds,
						centre, rotation, x, z,
						candyCane);
			}
		}

		for (int x : new int[] {-2, 2}) {
			set(world, bounds, centre, rotation,
					x, 5, -4, glass);
			set(world, bounds, centre, rotation,
					x, 5, 2, glass);
		}
		for (int z : new int[] {-2, 0}) {
			set(world, bounds, centre, rotation,
					-4, 5, z, glass);
			set(world, bounds, centre, rotation,
					4, 5, z, glass);
		}

		fill(world, bounds, centre, rotation,
				-5, 8, -5, 5, 8, 3, wafer);
		fill(world, bounds, centre, rotation,
				-5, 9, -5, 5, 9, 3, icing);
		fill(world, bounds, centre, rotation,
				-3, 10, -3, 3, 10, 1, icing);
		fill(world, bounds, centre, rotation,
				-1, 11, -1, 1, 11, -1,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState());

		set(world, bounds, centre, rotation,
				0, 2, 3, wafer);
		set(world, bounds, centre, rotation,
				0, 1, 4, wafer);
		set(world, bounds, centre, rotation,
				0, 0, 5, wafer);
		for (int z = 5; z <= 7; z++) {
			set(world, bounds, centre, rotation,
					0, 0, z,
					CakeWorldBlocks.BISCUIT_CRUMBS
							.get().defaultBlockState());
		}

		set(world, bounds, centre, rotation,
				-2, 4, -3,
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				0, 4, -3,
				CakeWorldBlocks.CANDY_COOKER.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				2, 4, -3,
				Blocks.CAULDRON.defaultBlockState());
		set(world, bounds, centre, rotation,
				-2, 4, 1,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				2, 4, 1,
				Blocks.CRAFTING_TABLE
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				3, 4, 1,
				Blocks.POTTED_RED_MUSHROOM
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				0, 7, -1,
				Blocks.LANTERN.defaultBlockState()
						.setValue(
								net.minecraft.world.level
										.block.LanternBlock
										.HANGING,
								true));
	}

	private static void buildGarden(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState border =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState sponge =
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState();
		BlockState matureSprout =
				CakeWorldBlocks.CANDY_SPROUT.get()
						.defaultBlockState()
						.setValue(CropBlock.AGE, 7);
		for (int x = -6; x <= -2; x++) {
			for (int z = 4; z <= 7; z++) {
				boolean edge = x == -6 || x == -2
						|| z == 4 || z == 7;
				set(world, bounds, centre, rotation,
						x, 0, z,
						edge ? border : sponge);
				if (!edge && z == 6) {
					set(world, bounds, centre,
							rotation, x, 1, z,
							matureSprout);
				}
			}
		}
		set(world, bounds, centre, rotation,
				-6, 1, 5,
				CakeWorldBlocks.SYRUP_PIPE.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.X));
		set(world, bounds, centre, rotation,
				-5, 1, 5,
				CakeWorldBlocks.SYRUP_PIPE.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.X));
		placeFilledBarrel(world, bounds, centre,
				rotation, -4, 1, 5,
				new ItemStack(
						CakeWorldFluids.SYRUP_BUCKET
								.get()));

	}

	private static void buildCaramelBasin(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState border =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		for (int x = 3; x <= 6; x++) {
			for (int z = 4; z <= 6; z++) {
				set(world, bounds, centre, rotation,
						x, 0, z, border);
			}
		}
		set(world, bounds, centre, rotation,
				4, 1, 5,
				Blocks.CAULDRON.defaultBlockState());
		set(world, bounds, centre, rotation,
				5, 1, 5,
				Blocks.CAULDRON.defaultBlockState());
		placeFilledBarrel(world, bounds, centre,
				rotation, 3, 1, 5,
				new ItemStack(
						CakeWorldFluids.CARAMEL_BUCKET
								.get()),
				new ItemStack(
						CakeWorldFluids.CARAMEL_BUCKET
								.get()));
	}

	private static void placeFilledBarrel(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int x, int y, int z,
			ItemStack... contents) {
		BlockPos position =
				transform(centre, rotation, x, y, z);
		if (!bounds.isInside(position)) {
			return;
		}
		world.setBlock(position,
				Blocks.BARREL.defaultBlockState()
						.rotate(rotation),
				2);
		if (world.getBlockEntity(position)
				instanceof Container container) {
			container.clearContent();
			for (int index = 0;
					index < contents.length; index++) {
				container.setItem(index,
						contents[index].copy());
			}
			container.setChanged();
		}
	}

	private static void placeResidentMarker(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds,
			boolean residentsActivated) {
		BlockPos marker =
				residentMarker(centre, rotation);
		if (!bounds.isInside(marker)) {
			return;
		}
		world.setBlock(marker,
				residentsActivated
						? CakeWorldBlocks
								.GINGERBREAD_BRICKS
								.get()
								.defaultBlockState()
						: Blocks.STRUCTURE_VOID
								.defaultBlockState(),
				2);
	}

	static boolean spawnResidentPair(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		List<BlockPos> positions =
				residentPositions(world.getSeed(), centre);
		BlockPos bakerPosition = positions.get(0);
		BlockPos catPosition = positions.get(1);

		if (bounds.isInside(bakerPosition)) {
			world.getLevel().getEntitiesOfClass(
					BitterBaker.class,
					new AABB(bakerPosition).inflate(1.0D))
					.forEach(BitterBaker::discard);
			BitterBaker baker =
					CakeWorldEntities.BITTER_BAKER.get()
							.create(world.getLevel());
			if (baker != null) {
				baker.moveTo(bakerPosition,
						rotation.rotate(
								Direction.SOUTH)
								.toYRot(),
						0.0F);
				baker.finalizeSpawn(
						world,
						world.getCurrentDifficultyAt(
								bakerPosition),
						MobSpawnType.STRUCTURE,
						null, null);
				baker.setPersistenceRequired();
				baker.restrictTo(centre, 10);
				world.addFreshEntity(baker);
			}
		}

		if (bounds.isInside(catPosition)) {
			world.getLevel().getEntitiesOfClass(
					CustardCat.class,
					new AABB(catPosition).inflate(1.0D))
					.forEach(CustardCat::discard);
			CustardCat cat =
					CakeWorldEntities.CUSTARD_CAT.get()
							.create(world.getLevel());
			if (cat != null) {
				cat.moveTo(catPosition,
						rotation.rotate(
								Direction.SOUTH)
								.toYRot(),
						0.0F);
				cat.finalizeSpawn(
						world,
						world.getCurrentDifficultyAt(
								catPosition),
						MobSpawnType.STRUCTURE,
						null, null);
				// Cat.finalizeSpawn only recognizes the literal vanilla
				// Swamp Hut feature, so preserve its deliberate black-cat
				// role explicitly for this custom structure.
				cat.setCatType(Cat.TYPE_ALL_BLACK);
				cat.setPersistenceRequired();
				cat.restrictTo(centre, 10);
				world.addFreshEntity(cat);
			}
		}

		AABB cottage = new AABB(
				centre.offset(-7, 0, -7),
				centre.offset(8, 12, 8));
		boolean complete =
				world.getLevel().getEntitiesOfClass(
						BitterBaker.class,
						cottage).size() == 1
						&& world.getLevel()
								.getEntitiesOfClass(
										CustardCat.class,
										cottage).size()
								== 1;
		if (complete) {
			BlockPos marker =
					residentMarker(centre, rotation);
			if (bounds.isInside(marker)) {
				world.setBlock(marker,
						CakeWorldBlocks
								.GINGERBREAD_BRICKS
								.get()
								.defaultBlockState(),
						2);
			}
		}
		return complete;
	}

	private static void fillSupportDown(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre, Rotation rotation,
			int x, int z, BlockState support) {
		BlockPos cursor = transform(
				centre, rotation, x, 2, z);
		while (cursor.getY()
				> world.getMinBuildHeight()) {
			if (!bounds.isInside(cursor)) {
				cursor = cursor.below();
				continue;
			}
			BlockState existing =
					world.getBlockState(cursor);
			if (cursor.getY() <= centre.getY()
					&& existing.isFaceSturdy(
							world, cursor,
							Direction.UP)
					&& world.getFluidState(cursor)
							.isEmpty()) {
				break;
			}
			world.setBlock(cursor, support, 2);
			cursor = cursor.below();
		}
	}

	private static BoundingBox savedBounds(
			BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 7,
				centre.getY(),
				centre.getZ() - 7,
				centre.getX() + 7,
				centre.getY() + 11,
				centre.getZ() + 7);
	}

	private static BoundingBox fullGenerationBounds(
			WorldGenLevel world, BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 7,
				world.getMinBuildHeight(),
				centre.getZ() - 7,
				centre.getX() + 7,
				centre.getY() + 11,
				centre.getZ() + 7);
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
