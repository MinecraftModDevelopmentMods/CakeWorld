package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.GingerbreadFolk;
import com.mcmoddev.cakeworld.entity.JawbreakerGuardian;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * The first complete Gingerbread Village variant.
 *
 * <p>A vanilla Village structure start owns spacing, locating, persistence and
 * structure identity. Its single feature-pool piece builds a compact
 * confectionery settlement containing real village POIs and CakeWorld
 * residents. Later biome variants can replace the pool element without
 * changing the public structure ID or village hooks.</p>
 */
public final class GingerbreadVillageFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation PIECE_ID =
			id("gingerbread_village_piece");
	public static final ResourceLocation POOL_ID =
			id("gingerbread_village/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("gingerbread_village");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("gingerbread_villages");
	public static final ResourceLocation LIBRARY_LOOT =
			id("chests/gingerbread_village_library");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/gingerbread_village"));
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
	public static final GingerbreadVillageFeature FEATURE =
			new GingerbreadVillageFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	private GingerbreadVillageFeature() {
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
								GingerbreadVillageFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				PIECE_ID,
				new PlacedFeature(configuredPiece, List.of()));

		Holder<StructureTemplatePool> pool =
				BuiltinRegistries.register(
						BuiltinRegistries.TEMPLATE_POOL,
						POOL_ID,
						new StructureTemplatePool(
								POOL_ID,
								Pools.EMPTY.location(),
								List.of(Pair.of(
										StructurePoolElement
												.feature(
														placedFeature),
										1)),
								StructureTemplatePool
										.Projection.RIGID));
		configuredStructure =
				BuiltinRegistries.register(
						BuiltinRegistries
								.CONFIGURED_STRUCTURE_FEATURE,
						STRUCTURE_KEY,
						StructureFeature.VILLAGE.configured(
								new JigsawConfiguration(pool, 1),
								GENERATES_IN, true));
		structureSet = BuiltinRegistries.register(
				BuiltinRegistries.STRUCTURE_SETS,
				STRUCTURE_SET_ID,
				new StructureSet(
						configuredStructure,
						new RandomSpreadStructurePlacement(
								24, 8,
								RandomSpreadType.LINEAR,
								1978001)));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
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
		BlockPos chunkCentre = context.origin().offset(8, 0, 8);
		int surfaceY = world.getHeight(
				Heightmap.Types.WORLD_SURFACE_WG,
				chunkCentre.getX(), chunkCentre.getZ()) - 1;
		return buildAt(world, context.random(),
				new BlockPos(chunkCentre.getX(), surfaceY,
						chunkCentre.getZ()));
	}

	/**
	 * Deterministic construction seam shared by the structure piece and tests.
	 * The supplied position is the centre floor block.
	 */
	public static boolean buildAt(WorldGenLevel world, Random random,
			BlockPos centre) {
		prepareFoundation(world, centre);
		buildRoadsAndPlaza(world, centre);

		House library = buildHouse(world,
				centre.offset(-6, 0, -6),
				Direction.EAST,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
						.get().defaultBlockState());
		House bakery = buildHouse(world,
				centre.offset(6, 0, -6),
				Direction.WEST,
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK
						.get().defaultBlockState());
		House home = buildHouse(world,
				centre.offset(6, 0, 6),
				Direction.WEST,
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK
						.get().defaultBlockState());

		furnishLibrary(world, random, library);
		furnishBakery(world, bakery);
		furnishHome(world, home);
		buildFarm(world, centre.offset(-6, 0, 6));
		buildMeetingPoint(world, centre);

		BlockPos meeting = centre.above();
		spawnResident(world, centre.offset(-5, 1, -6),
				VillagerProfession.LIBRARIAN,
				PoiType.LIBRARIAN,
				centre.offset(-5, 1, -5),
				centre.offset(-5, 1, -7), meeting);
		spawnResident(world, centre.offset(5, 1, -6),
				VillagerProfession.BUTCHER,
				PoiType.BUTCHER,
				centre.offset(7, 1, -5),
				centre.offset(7, 1, -7), meeting);
		spawnResident(world, centre.offset(5, 1, 6),
				VillagerProfession.CARTOGRAPHER,
				PoiType.CARTOGRAPHER,
				centre.offset(5, 1, 7),
				centre.offset(5, 1, 5), meeting);
		spawnResident(world, centre.offset(-5, 1, 6),
				VillagerProfession.FARMER,
				PoiType.FARMER,
				centre.offset(7, 1, 7),
				centre.offset(-6, 1, 3), meeting);
		spawnGuardian(world, centre.offset(0, 1, 3));
		return true;
	}

	private static void prepareFoundation(WorldGenLevel world,
			BlockPos centre) {
		BlockState foundation =
				CakeWorldBlocks.GINGERBREAD_BRICKS
						.get().defaultBlockState();
		for (int x = -10; x <= 10; x++) {
			for (int z = -10; z <= 10; z++) {
				int surfaceY = world.getHeight(
						Heightmap.Types.WORLD_SURFACE_WG,
						centre.getX() + x,
						centre.getZ() + z) - 1;
				BlockPos floor = centre.offset(x, 0, z);
				if (surfaceY < centre.getY()) {
					for (int y = surfaceY + 1;
							y <= centre.getY(); y++) {
						world.setBlock(
								new BlockPos(
										floor.getX(), y,
										floor.getZ()),
								foundation, 2);
					}
				} else {
					for (int y = centre.getY() + 1;
							y <= surfaceY + 7; y++) {
						world.setBlock(
								new BlockPos(
										floor.getX(), y,
										floor.getZ()),
								Blocks.AIR
										.defaultBlockState(),
								2);
					}
				}
				world.setBlock(floor, foundation, 2);
				for (int y = 1; y <= 7; y++) {
					world.setBlock(floor.above(y),
							Blocks.AIR.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static void buildRoadsAndPlaza(WorldGenLevel world,
			BlockPos centre) {
		BlockState xRoad =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(RotatedPillarBlock.AXIS,
								Direction.Axis.X);
		BlockState zRoad =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(RotatedPillarBlock.AXIS,
								Direction.Axis.Z);
		BlockState crumbs =
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState();
		for (int distance = -10; distance <= 10;
				distance++) {
			for (int width = -1; width <= 1; width++) {
				world.setBlock(
						centre.offset(distance, 0, width),
						width == 0 ? xRoad : crumbs, 2);
				world.setBlock(
						centre.offset(width, 0, distance),
						width == 0 ? zRoad : crumbs, 2);
			}
		}
		BlockState plaza =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlock(centre.offset(x, 0, z),
						plaza, 2);
			}
		}
	}

	private static House buildHouse(WorldGenLevel world,
			BlockPos floorCentre, Direction doorFacing,
			BlockState roof) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState glass =
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				world.setBlock(floorCentre.offset(x, 0, z),
						CakeWorldBlocks.WAFER_BLOCK.get()
								.defaultBlockState(),
						2);
				boolean wall = Math.abs(x) == 3
						|| Math.abs(z) == 3;
				for (int y = 1; y <= 3; y++) {
					world.setBlock(
							floorCentre.offset(x, y, z),
							wall ? bricks
									: Blocks.AIR
											.defaultBlockState(),
							2);
				}
				world.setBlock(floorCentre.offset(x, 4, z),
						roof, 2);
				if (Math.abs(x) <= 2 && Math.abs(z) <= 2) {
					world.setBlock(
							floorCentre.offset(x, 5, z),
							CakeWorldBlocks.ICING_LAYER.get()
									.defaultBlockState(),
							2);
				}
			}
		}

		world.setBlock(floorCentre.offset(0, 2, -3),
				glass, 2);
		world.setBlock(floorCentre.offset(0, 2, 3),
				glass, 2);
		world.setBlock(floorCentre.offset(-3, 2, 0),
				glass, 2);
		world.setBlock(floorCentre.offset(3, 2, 0),
				glass, 2);

		BlockPos door = floorCentre.relative(doorFacing, 3)
				.above();
		BlockState lower = Blocks.OAK_DOOR.defaultBlockState()
				.setValue(DoorBlock.FACING, doorFacing)
				.setValue(DoorBlock.HALF,
						DoubleBlockHalf.LOWER);
		world.setBlock(door, lower, 2);
		world.setBlock(door.above(),
				lower.setValue(DoorBlock.HALF,
						DoubleBlockHalf.UPPER),
				2);
		return new House(floorCentre, doorFacing);
	}

	private static void furnishLibrary(WorldGenLevel world,
			Random random, House house) {
		BlockPos library = house.centre().offset(-1, 1, -1);
		world.setBlock(library,
				CakeWorldBlocks.COOKBOOK_LIBRARY.get()
						.defaultBlockState(),
				2);
		BlockPos lectern = house.centre().offset(1, 1, -1);
		world.setBlock(lectern,
				Blocks.LECTERN.defaultBlockState(), 2);
		registerPoi(world, lectern, PoiType.LIBRARIAN);
		BlockPos chest = house.centre().offset(-1, 1, 1);
		world.setBlock(chest,
				Blocks.CHEST.defaultBlockState(), 2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, chest, LIBRARY_LOOT);
		placeBed(world, house.centre().offset(1, 1, 1),
				Direction.NORTH);
	}

	private static void furnishBakery(WorldGenLevel world,
			House house) {
		world.setBlock(house.centre().offset(-1, 1, -1),
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState(),
				2);
		BlockPos smoker = house.centre().offset(1, 1, -1);
		world.setBlock(smoker,
				Blocks.SMOKER.defaultBlockState(), 2);
		registerPoi(world, smoker, PoiType.BUTCHER);
		world.setBlock(house.centre().offset(-1, 1, 1),
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState(),
				2);
		placeBed(world, house.centre().offset(1, 1, 1),
				Direction.NORTH);
	}

	private static void furnishHome(WorldGenLevel world,
			House house) {
		BlockPos table = house.centre().offset(-1, 1, -1);
		world.setBlock(table,
				Blocks.CARTOGRAPHY_TABLE.defaultBlockState(),
				2);
		registerPoi(world, table, PoiType.CARTOGRAPHER);
		world.setBlock(house.centre().offset(1, 1, -1),
				CakeWorldBlocks.COOKBOOK_KIOSK.get()
						.defaultBlockState(),
				2);
		placeBed(world, house.centre().offset(-1, 1, 1),
				Direction.NORTH);
		placeBed(world, house.centre().offset(1, 1, 1),
				Direction.NORTH);
	}

	private static void buildFarm(WorldGenLevel world,
			BlockPos centre) {
		BlockState border =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				BlockPos floor = centre.offset(x, 0, z);
				if (Math.abs(x) == 3 || Math.abs(z) == 3) {
					world.setBlock(floor, border, 2);
				} else if (x == 0) {
					world.setBlock(floor,
							Blocks.WATER
									.defaultBlockState(),
							2);
				} else {
					BlockState farmland =
							Blocks.FARMLAND
									.defaultBlockState();
					world.setBlock(floor, farmland, 2);
					world.setBlock(floor.above(),
							((x + z) & 1) == 0
									? Blocks.WHEAT
											.defaultBlockState()
									: Blocks.CARROTS
											.defaultBlockState(),
							2);
				}
			}
		}
		BlockPos composter = centre.offset(0, 1, -3);
		world.setBlock(composter,
				Blocks.COMPOSTER.defaultBlockState(), 2);
		registerPoi(world, composter, PoiType.FARMER);
		world.setBlock(centre.offset(2, 1, 3),
				CakeWorldBlocks.WAFER_WINDMILL.get()
						.defaultBlockState(),
				2);
	}

	private static void buildMeetingPoint(WorldGenLevel world,
			BlockPos centre) {
		BlockPos bell = centre.above();
		world.setBlock(bell, Blocks.BELL.defaultBlockState(), 2);
		registerPoi(world, bell, PoiType.MEETING);
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			world.setBlock(
					centre.relative(direction, 2).above(),
					CakeWorldBlocks.CHOCOLATE_SPONGE.get()
							.defaultBlockState(),
					2);
		}
	}

	private static void placeBed(WorldGenLevel world,
			BlockPos foot, Direction facing) {
		BlockState footState = Blocks.RED_BED.defaultBlockState()
				.setValue(BedBlock.FACING, facing)
				.setValue(BedBlock.PART, BedPart.FOOT);
		BlockPos head = foot.relative(facing);
		world.setBlock(foot, footState, 2);
		world.setBlock(head,
				footState.setValue(BedBlock.PART, BedPart.HEAD),
				2);
		registerPoi(world, foot, PoiType.HOME);
		registerPoi(world, head, PoiType.HOME);
	}

	private static void registerPoi(WorldGenLevel world,
			BlockPos pos, PoiType type) {
		world.getLevel().getPoiManager().add(pos, type);
	}

	private static void spawnResident(WorldGenLevel world,
			BlockPos pos, VillagerProfession profession,
			PoiType jobType, BlockPos home, BlockPos job,
			BlockPos meeting) {
		GingerbreadFolk resident =
				CakeWorldEntities.GINGERBREAD_FOLK.get()
						.create(world.getLevel());
		if (resident == null) {
			return;
		}
		resident.setPos(pos.getX() + 0.5D, pos.getY(),
				pos.getZ() + 0.5D);
		resident.finalizeSpawn(world,
				world.getCurrentDifficultyAt(pos),
				MobSpawnType.STRUCTURE, null, null);
		resident.setVillagerData(
				resident.getVillagerData()
						.setType(VillagerType.PLAINS)
						.setProfession(profession));
		resident.setPersistenceRequired();
		resident.restrictTo(pos, 24);
		claimPoi(world, home, PoiType.HOME);
		claimPoi(world, job, jobType);
		claimPoi(world, meeting, PoiType.MEETING);
		GlobalPos homeMemory = GlobalPos.of(
				world.getLevel().dimension(), home);
		GlobalPos jobMemory = GlobalPos.of(
				world.getLevel().dimension(), job);
		GlobalPos meetingMemory = GlobalPos.of(
				world.getLevel().dimension(), meeting);
		resident.getBrain().setMemory(
				MemoryModuleType.HOME, homeMemory);
		resident.getBrain().setMemory(
				MemoryModuleType.JOB_SITE, jobMemory);
		resident.getBrain().setMemory(
				MemoryModuleType.MEETING_POINT,
				meetingMemory);
		world.addFreshEntity(resident);
	}

	private static void claimPoi(WorldGenLevel world,
			BlockPos pos, PoiType type) {
		world.getLevel().getPoiManager().take(
				type::equals, pos::equals, pos, 1);
	}

	private static void spawnGuardian(WorldGenLevel world,
			BlockPos pos) {
		JawbreakerGuardian guardian =
				CakeWorldEntities.JAWBREAKER_GUARDIAN.get()
						.create(world.getLevel());
		if (guardian == null) {
			return;
		}
		guardian.setPos(pos.getX() + 0.5D, pos.getY(),
				pos.getZ() + 0.5D);
		guardian.setPersistenceRequired();
		guardian.restrictTo(pos, 24);
		world.addFreshEntity(guardian);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private static record House(BlockPos centre,
			Direction doorFacing) {
	}
}
