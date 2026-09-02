package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.FizzyKelpBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

/**
 * A small renewable Wafer reef that keeps every authored cell inside its
 * generating chunk and every planted frond inside Lemonade.
 *
 * <p>The reef creates no entities, block entities or repair pass. Its one
 * visible Fizzy Pearl is authored treasure and is not evidence that
 * OreSpawn's separately shelved under-fluid rule generated a pearl.</p>
 */
public final class WaferReefNurseryFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("wafer_reef_nursery");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MAX_FLOOR_RELIEF = 3;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 1978080;
	public static final WaferReefNurseryFeature FEATURE =
			new WaferReefNurseryFeature();
	private static final ResourceKey<Biome> SODA_OCEAN_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.SODA_OCEAN.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] KELP = {
			{-4, -2}, {-4, 0}, {-4, 2},
			{-2, -4}, {0, -4}, {2, -4},
			{4, -2}, {4, 0}, {4, 2},
			{-2, 4}, {0, 4}, {2, 4},
			{-2, -2}, {2, -2}, {-2, 2}, {2, 2}
	};
	private static final int[][] TOWERS = {
			{-3, -3}, {3, -3}, {-3, 3}, {3, 3}
	};
	private static Holder<PlacedFeature> placedFeature;

	private WaferReefNurseryFeature() {
		super(NoneFeatureConfiguration.CODEC);
		setRegistryName(ID);
	}

	public static void registerConfiguredFeature() {
		Holder<ConfiguredFeature<?, ?>> configured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								WaferReefNurseryFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						RarityFilter.onAverageOnceEvery(
								AVERAGE_CHUNKS_PER_ATTEMPT),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(
								Heightmap.Types
										.OCEAN_FLOOR_WG),
						BiomeFilter.biome())));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos origin = floorAt(world,
				context.origin().getX(),
				context.origin().getZ());
		if (!world.getBiome(origin).is(SODA_OCEAN_KEY)) {
			return false;
		}
		ChunkPos placementChunk = new ChunkPos(context.origin());
		for (int radius = 0;
				radius <= SAFE_SITE_SEARCH_RADIUS; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (radius > 0
							&& Math.abs(x) != radius
							&& Math.abs(z) != radius) {
						continue;
					}
					BlockPos around = floorAt(world,
							origin.getX() + x,
							origin.getZ() + z);
					if (!world.getBiome(around)
							.is(SODA_OCEAN_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							around.getX(),
							highestFloorY(world, around),
							around.getZ());
					Rotation rotation = orientation(
							world.getSeed(), centre);
					if (fitsWithinChunk(centre, rotation,
							placementChunk)
							&& buildAt(world, centre,
									rotation)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearInheritedVegetation(world, centre, rotation);
		buildFloor(world, centre, rotation);
		buildReefTowers(world, centre, rotation);
		buildKelp(world, centre, rotation);
		buildTreasureMarkers(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		return footprintProblem(world, centre, rotation) == null;
	}

	public static String footprintProblem(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int floorY = terrainFloorY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (floorY > centre.getY()
						|| centre.getY() - floorY
								> MAX_FLOOR_RELIEF) {
					return "relief at " + horizontal
							+ ": floorY=" + floorY
							+ ", centreY=" + centre.getY();
				}
				BlockPos floor = new BlockPos(
						horizontal.getX(), floorY,
						horizontal.getZ());
				BlockState ground = world.getBlockState(floor);
				if (!world.getFluidState(floor).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return "floor at " + floor
							+ ": state=" + ground
							+ ", fluid="
							+ world.getFluidState(floor)
							+ ", blockEntity="
							+ ground.hasBlockEntity();
				}
				for (int y = floorY + 1;
						y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					BlockState state =
							world.getBlockState(position);
					if (state.hasBlockEntity()
							|| !isLemonadeOrInheritedPlant(
									world, position,
									state)) {
						return "water column at " + position
								+ ": state=" + state
								+ ", fluid="
								+ world.getFluidState(
										position)
								+ ", blockEntity="
								+ state.hasBlockEntity();
					}
				}
			}
		}
		return null;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-5, 5}) {
				BlockPos corner =
						local(centre, rotation, x, 0, z);
				if (Math.floorDiv(corner.getX(), 16) != chunk.x
						|| Math.floorDiv(corner.getZ(), 16)
								!= chunk.z) {
					return false;
				}
			}
		}
		return true;
	}

	public static Rotation orientation(long seed,
			BlockPos centre) {
		long mixed = seed ^ centre.asLong() ^ PLACEMENT_SALT;
		mixed = (mixed ^ mixed >>> 30)
				* -4658895280553007687L;
		mixed = (mixed ^ mixed >>> 27)
				* -7723592293110705685L;
		mixed ^= mixed >>> 31;
		return ROTATIONS[(int) (mixed & 3L)];
	}

	public static BlockPos local(BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return centre.offset(new BlockPos(x, y, z)
				.rotate(rotation));
	}

	public static int[][] kelp() {
		return KELP;
	}

	private static void buildFloor(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState state = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
								.defaultBlockState()
						: CakeWorldBlocks.BISCUIT_CRUMBS.get()
								.defaultBlockState();
				BlockPos floor =
						local(centre, rotation, x, 1, z);
				supportDown(world, floor.below());
				world.setBlock(floor, state, 2);
			}
		}
	}

	private static void buildReefTowers(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState[] caps = {
			CakeWorldBlocks.GUMMY_BLOCK.get()
					.defaultBlockState(),
			CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
					.defaultBlockState(),
			CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get()
					.defaultBlockState(),
			CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
					.defaultBlockState()
		};
		BlockState wafer = CakeWorldBlocks.WAFER_ROCK.get()
				.defaultBlockState();
		for (int i = 0; i < TOWERS.length; i++) {
			int[] tower = TOWERS[i];
			set(world, centre, rotation,
					tower[0], 2, tower[1], wafer);
			set(world, centre, rotation,
					tower[0], 3, tower[1], wafer);
			set(world, centre, rotation,
					tower[0], 4, tower[1], caps[i]);
		}
	}

	private static void buildKelp(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState mature = CakeWorldBlocks.FIZZY_KELP.get()
				.defaultBlockState()
				.setValue(FizzyKelpBlock.AGE, 3);
		for (int[] kelp : KELP) {
			set(world, centre, rotation,
					kelp[0], 2, kelp[1], mature);
		}
	}

	private static void buildTreasureMarkers(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		for (int[] marker : new int[][] {
				{-2, 0}, {2, 0}, {0, -2}, {0, 2}
		}) {
			set(world, centre, rotation,
					marker[0], 2, marker[1], glass);
		}
		set(world, centre, rotation, 0, 2, 0,
				CakeWorldBlocks.FIZZY_PEARL.get()
						.defaultBlockState());
	}

	private static void supportDown(WorldGenLevel world,
			BlockPos start) {
		BlockPos.MutableBlockPos cursor = start.mutable();
		while (cursor.getY() > world.getMinBuildHeight()
				&& world.getFluidState(cursor)
						.is(CakeWorldFluids.LEMONADE.get())) {
			world.setBlock(cursor,
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState(),
					2);
			cursor.move(0, -1, 0);
		}
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int floorY = terrainFloorY(world,
						horizontal.getX(),
						horizontal.getZ());
				for (int y = floorY + 1;
						y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					if (isInheritedPlant(
							world.getBlockState(position))) {
						world.setBlock(position,
								CakeWorldFluids.LEMONADE_BLOCK
										.get()
										.defaultBlockState(),
								2);
					}
				}
			}
		}
	}

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(CakeWorldBlocks.WAFER_ROCK.get())
				|| state.is(CakeWorldBlocks.WAFER_BLOCK.get())
				|| state.is(BlockTags.BASE_STONE_OVERWORLD)
				|| state.is(BlockTags.DIRT)
				|| state.is(BlockTags.SAND)
				|| state.is(Blocks.GRAVEL);
	}

	private static boolean isLemonadeOrInheritedPlant(
			WorldGenLevel world, BlockPos pos,
			BlockState state) {
		return world.getFluidState(pos)
						.is(CakeWorldFluids.LEMONADE.get())
				|| isInheritedPlant(state);
	}

	private static boolean isInheritedPlant(BlockState state) {
		return state.is(Blocks.SEAGRASS)
				|| state.is(Blocks.TALL_SEAGRASS)
				|| state.is(Blocks.KELP)
				|| state.is(Blocks.KELP_PLANT)
				|| state.is(Blocks.SEA_PICKLE);
	}

	private static int highestFloorY(WorldGenLevel world,
			BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				highest = Math.max(highest,
						terrainFloorY(world,
								around.getX() + x,
								around.getZ() + z));
			}
		}
		return highest;
	}

	private static BlockPos floorAt(WorldGenLevel world,
			int x, int z) {
		return new BlockPos(x,
				terrainFloorY(world, x, z), z);
	}

	private static int terrainFloorY(WorldGenLevel world,
			int x, int z) {
		return world.getHeight(
				Heightmap.Types.OCEAN_FLOOR_WG,
				x, z) - 1;
	}

	private static void set(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int x, int y, int z, BlockState state) {
		world.setBlock(local(centre, rotation, x, y, z),
				state, 2);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
