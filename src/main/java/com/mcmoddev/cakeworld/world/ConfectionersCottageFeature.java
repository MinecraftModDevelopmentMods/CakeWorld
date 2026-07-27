package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.TravellingConfectioner;
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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
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
import net.minecraft.world.phys.AABB;

/**
 * A small, friendly home-and-shop for Lollipop Orchards.
 *
 * <p>Candy Plains is the deliberately temporary prototype home until the
 * orchard biome exists. The cottage has its own structure identity instead of
 * borrowing a vanilla landmark already owned by another CakeWorld
 * conversion.</p>
 */
public final class ConfectionersCottageFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("confectioners_cottage_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("confectioners_cottage_structure");
	public static final ResourceLocation POOL_ID =
			id("confectioners_cottage/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("confectioners_cottage");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("confectioners_cottages");
	public static final ResourceLocation LOOT_ID =
			id("chests/confectioners_cottage");
	public static final int PLACEMENT_SALT = 1978019;
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(16, 12, 16);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/confectioners_cottage"));
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
	public static final ConfectionersCottageFeature FEATURE =
			new ConfectionersCottageFeature();
	public static final ConfectionersCottageStructureFeature
			STRUCTURE_FEATURE =
			new ConfectionersCottageStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private ConfectionersCottageFeature() {
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
								ConfectionersCottageFeature>(
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
								48, 16,
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
				ConfectionersCottageStructureFeature
						.CENTRE_OFFSET,
				0,
				ConfectionersCottageStructureFeature
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
				ConfectionersCottageStructureFeature
						.CENTRE_OFFSET,
				0,
				ConfectionersCottageStructureFeature
						.CENTRE_OFFSET);
		boolean built = buildAt(world, random, centre,
				generationBounds, false);
		Rotation rotation =
				orientation(world.getSeed(), centre);
		if (built && generationBounds.isInside(
				residentMarker(centre, rotation))) {
			ConfectionersCottageResidents.queue(
					world, centre, savedBounds(centre));
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
						^ PLACEMENT_SALT);
		return Rotation.getRandom(random);
	}

	public static BlockPos residentMarker(
			BlockPos centre, Rotation rotation) {
		return transform(centre, rotation,
				0, 0, 0);
	}

	public static BlockPos residentPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				0, 1, -1);
	}

	public static BlockPos stockChestPosition(
			long worldSeed, BlockPos centre) {
		return transform(centre,
				orientation(worldSeed, centre),
				4, 1, -2);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds,
			boolean spawnResident) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		BlockPos marker =
				residentMarker(centre, rotation);
		boolean residentActivated =
				!spawnResident
						&& world.getBlockState(marker)
								.is(CakeWorldBlocks
										.GINGERBREAD_BRICKS
										.get());
		clearSite(world, centre, rotation, bounds);
		buildShop(world, random, centre, rotation,
				bounds);
		buildIngredientGarden(world, centre, rotation,
				bounds);
		buildLollipopSign(world, centre, rotation,
				bounds);
		placeResidentMarker(world, centre, rotation,
				bounds, residentActivated);
		if (spawnResident) {
			spawnResident(world, centre,
					savedBounds(centre));
		}
		return true;
	}

	private static void clearSite(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		fill(world, bounds, centre, rotation,
				-7, 1, -7, 7, 12, 7,
				Blocks.AIR.defaultBlockState());
	}

	private static void buildShop(
			WorldGenLevel world, Random random,
			BlockPos centre, Rotation rotation,
			BoundingBox bounds) {
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
				-5, 0, -4, 5, 0, 4, wafer);
		for (int y = 1; y <= 5; y++) {
			for (int x = -5; x <= 5; x++) {
				set(world, bounds, centre, rotation,
						x, y, -4, gingerbread);
				set(world, bounds, centre, rotation,
						x, y, 4, gingerbread);
			}
			for (int z = -3; z <= 3; z++) {
				set(world, bounds, centre, rotation,
						-5, y, z, gingerbread);
				set(world, bounds, centre, rotation,
						5, y, z, gingerbread);
			}
		}
		fill(world, bounds, centre, rotation,
				-1, 1, 4, 1, 3, 4,
				Blocks.AIR.defaultBlockState());
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-4, 4}) {
				fill(world, bounds, centre, rotation,
						x, 0, z, x, 6, z,
						candyCane);
				fillSupportDown(world, bounds,
						centre, rotation, x, z,
						candyCane);
			}
		}

		for (int x : new int[] {-3, 0, 3}) {
			set(world, bounds, centre, rotation,
					x, 3, -4, glass);
		}
		for (int x : new int[] {-3, 3}) {
			set(world, bounds, centre, rotation,
					x, 3, 4, glass);
		}
		for (int z : new int[] {-2, 1}) {
			set(world, bounds, centre, rotation,
					-5, 3, z, glass);
			set(world, bounds, centre, rotation,
					5, 3, z, glass);
		}

		fill(world, bounds, centre, rotation,
				-6, 6, -5, 6, 6, 5, wafer);
		fill(world, bounds, centre, rotation,
				-5, 7, -4, 5, 7, 4, icing);
		fill(world, bounds, centre, rotation,
				-3, 8, -2, 3, 8, 2, wafer);
		fill(world, bounds, centre, rotation,
				-2, 9, -1, 2, 9, 1, icing);
		set(world, bounds, centre, rotation,
				0, 10, 0,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState());

		for (int z = 5; z <= 8; z++) {
			set(world, bounds, centre, rotation,
					0, 0, z,
					CakeWorldBlocks.BISCUIT_CRUMBS
							.get().defaultBlockState());
		}
		for (int x = -4; x <= 4; x++) {
			if (x != 0) {
				set(world, bounds, centre, rotation,
						x, 1, 1, wafer);
			}
		}
		set(world, bounds, centre, rotation,
				-3, 1, -3,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				-1, 1, -3,
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				1, 1, -3,
				CakeWorldBlocks.CANDY_COOKER.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				3, 1, -3,
				CakeWorldBlocks.SODA_FOUNTAIN.get()
						.defaultBlockState());
		set(world, bounds, centre, rotation,
				-4, 1, -1,
				CakeWorldBlocks.COOKBOOK_KIOSK.get()
						.defaultBlockState());
		BlockPos chest =
				transform(centre, rotation,
						4, 1, -2);
		if (bounds.isInside(chest)) {
			world.setBlock(chest,
					Blocks.CHEST.defaultBlockState()
							.rotate(rotation),
					2);
			RandomizableContainerBlockEntity
					.setLootTable(world, random,
							chest, LOOT_ID);
		}
		set(world, bounds, centre, rotation,
				0, 5, 0,
				Blocks.LANTERN.defaultBlockState()
						.setValue(
								LanternBlock.HANGING,
								true));
	}

	private static void buildIngredientGarden(
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
		for (int x = -7; x <= -3; x++) {
			for (int z = 5; z <= 7; z++) {
				boolean edge = x == -7 || x == -3
						|| z == 5 || z == 7;
				set(world, bounds, centre, rotation,
						x, 0, z,
						edge ? border : sponge);
				if (!edge) {
					set(world, bounds, centre,
							rotation, x, 1, z,
							matureSprout);
				}
			}
		}
	}

	private static void buildLollipopSign(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds) {
		BlockState pole =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		fill(world, bounds, centre, rotation,
				6, 0, 6, 6, 4, 6, pole);
		for (int x = 5; x <= 7; x++) {
			for (int y = 4; y <= 6; y++) {
				if (Math.abs(x - 6)
						+ Math.abs(y - 5) <= 2) {
					set(world, bounds, centre,
							rotation, x, y, 6,
							((x + y) & 1) == 0
									? CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get()
											.defaultBlockState()
									: CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get()
											.defaultBlockState());
				}
			}
		}
	}

	private static void placeResidentMarker(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, BoundingBox bounds,
			boolean residentActivated) {
		BlockPos marker =
				residentMarker(centre, rotation);
		if (!bounds.isInside(marker)) {
			return;
		}
		world.setBlock(marker,
				residentActivated
						? CakeWorldBlocks
								.GINGERBREAD_BRICKS
								.get()
								.defaultBlockState()
						: Blocks.STRUCTURE_VOID
								.defaultBlockState(),
				2);
	}

	static boolean spawnResident(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		Rotation rotation =
				orientation(world.getSeed(), centre);
		BlockPos position =
				residentPosition(world.getSeed(), centre);
		if (!bounds.isInside(position)) {
			return false;
		}
		world.getLevel().getEntitiesOfClass(
				TravellingConfectioner.class,
				new AABB(position).inflate(1.0D))
				.forEach(TravellingConfectioner::discard);
		TravellingConfectioner resident =
				CakeWorldEntities.TRAVELLING_CONFECTIONER
						.get().create(world.getLevel());
		if (resident == null) {
			return false;
		}
		resident.moveTo(position,
				rotation.rotate(Direction.SOUTH)
						.toYRot(),
				0.0F);
		resident.finalizeSpawn(
				world,
				world.getCurrentDifficultyAt(position),
				MobSpawnType.STRUCTURE,
				null, null);
		resident.installCottageOffers();
		resident.setDespawnDelay(0);
		resident.setWanderTarget(centre);
		resident.setPersistenceRequired();
		resident.restrictTo(centre, 7);
		resident.setCustomName(new TranslatableComponent(
				"entity.cakeworld.cottage_confectioner"));
		world.addFreshEntity(resident);

		boolean complete =
				world.getLevel().getEntitiesOfClass(
						TravellingConfectioner.class,
						new AABB(position).inflate(1.0D))
						.size() == 1;
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

	private static BoundingBox savedBounds(
			BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 8,
				centre.getY(),
				centre.getZ() - 8,
				centre.getX() + 8,
				centre.getY() + 12,
				centre.getZ() + 8);
	}

	private static BoundingBox fullGenerationBounds(
			WorldGenLevel world, BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 8,
				world.getMinBuildHeight(),
				centre.getZ() - 8,
				centre.getX() + 8,
				centre.getY() + 12,
				centre.getZ() + 8);
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
