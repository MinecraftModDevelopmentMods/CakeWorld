package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
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
 * A bounded Caramel-Bogs scene: three squat toffee-mangrove silhouettes,
 * contained Caramel, stable Treacle Reeds and a Wafer recovery path. It runs
 * once after OreSpawn's surface pass, creates no entities or block entities,
 * and has no repair path that could overwrite later player work.
 */
public final class CaramelBogMangroveFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("caramel_bog_mangrove");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 14;
	public static final int MAX_TERRAIN_RELIEF = 3;
	public static final int PLACEMENT_SALT = 1978060;
	public static final CaramelBogMangroveFeature FEATURE =
			new CaramelBogMangroveFeature();
	private static final ResourceKey<Biome> CARAMEL_BOGS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.CARAMEL_BOGS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private CaramelBogMangroveFeature() {
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
								CaramelBogMangroveFeature>(
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
										.MOTION_BLOCKING_NO_LEAVES),
						BiomeFilter.biome())));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos surface = surfaceAt(world,
				context.origin().getX(),
				context.origin().getZ());
		if (!world.getBiome(surface).is(CARAMEL_BOGS_KEY)) {
			return false;
		}
		ChunkPos chunk = new ChunkPos(context.origin());
		for (int radius = 0; radius <= 6; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (radius > 0 && Math.abs(x) != radius
							&& Math.abs(z) != radius) {
						continue;
					}
					BlockPos candidateSurface = surfaceAt(world,
							surface.getX() + x,
							surface.getZ() + z);
					if (!world.getBiome(candidateSurface)
							.is(CARAMEL_BOGS_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							candidateSurface.getX(),
							highestSurfaceY(world,
									candidateSurface),
							candidateSurface.getZ());
					Rotation rotation = orientation(
							world.getSeed(), centre);
					if (fitsWithinChunk(centre, rotation, chunk)
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
		clearVegetation(world, centre, rotation);
		buildGround(world, centre, rotation);
		buildCaramelPool(world, centre, rotation);
		buildWaferPath(world, centre, rotation);
		buildMangrove(world, centre, rotation, -3, 3, 5);
		buildMangrove(world, centre, rotation, 3, 2, 6);
		buildMangrove(world, centre, rotation, 2, -3, 5);
		buildReeds(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (surfaceY > centre.getY()
						|| centre.getY() - surfaceY
								> MAX_TERRAIN_RELIEF) {
					return false;
				}
				BlockPos ground = new BlockPos(
						horizontal.getX(), surfaceY,
						horizontal.getZ());
				BlockState state = world.getBlockState(ground);
				if (!world.getFluidState(ground).isEmpty()
						|| !isAcceptedGround(state)) {
					return false;
				}
			}
		}
		for (int x = -4; x <= 4; x++) {
			for (int y = 1; y <= 9; y++) {
				for (int z = -4; z <= 4; z++) {
					BlockState state = world.getBlockState(
							local(centre, rotation,
									x, y, z));
					if (state.hasBlockEntity()
							|| !canClear(state)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				BlockPos corner = local(
						centre, rotation, x, 0, z);
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

	private static void buildGround(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos top = local(
						centre, rotation, x, 0, z);
				world.setBlock(top,
						CakeWorldBlocks.CARAMEL_CRUST.get()
								.defaultBlockState(), 2);
				for (int y = -1; y >= -MAX_TERRAIN_RELIEF; y--) {
					BlockPos support = top.offset(0, y, 0);
					if (!world.isEmptyBlock(support)) {
						break;
					}
					world.setBlock(support,
							CakeWorldBlocks.CHOCOLATE_SPONGE
									.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static void buildCaramelPool(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -3; x <= -1; x++) {
			for (int z = -2; z <= 0; z++) {
				BlockPos pool = local(
						centre, rotation, x, 0, z);
				world.setBlock(pool.below(),
						CakeWorldBlocks.CARAMEL_CRUST.get()
								.defaultBlockState(), 2);
				world.setBlock(pool,
						CakeWorldFluids.CARAMEL_BLOCK.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void buildWaferPath(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			world.setBlock(local(
					centre, rotation, x, 0, 2),
					CakeWorldBlocks.WAFER_BLOCK.get()
							.defaultBlockState(), 2);
		}
	}

	private static void buildMangrove(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int localX, int localZ, int height) {
		BlockPos root = local(
				centre, rotation, localX, 0, localZ);
		for (int y = 1; y <= height; y++) {
			world.setBlock(root.above(y),
					CakeWorldBlocks.GINGERBREAD_BRICKS.get()
							.defaultBlockState(), 2);
		}
		for (int[] offset : new int[][] {
				{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
			world.setBlock(root.offset(
					offset[0], 1, offset[1]),
					CakeWorldBlocks.GINGERBREAD_BRICKS.get()
							.defaultBlockState(), 2);
		}
		BlockPos crown = root.above(height);
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (Math.abs(x) + Math.abs(z) <= 3) {
					world.setBlock(crown.offset(x, 0, z),
							CakeWorldBlocks.CARAMEL_CRUST
									.get()
									.defaultBlockState(),
							2);
				}
			}
		}
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				world.setBlock(crown.offset(x, 1, z),
						CakeWorldBlocks.CARAMEL_CRUST.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void buildReeds(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		int[][] reeds = {
				{-4, -2, 2}, {-4, 0, 3},
				{0, -2, 2}, {0, 0, 3},
				{-3, 1, 2}, {-1, 1, 2}
		};
		for (int[] reed : reeds) {
			BlockPos base = local(centre, rotation,
					reed[0], 1, reed[1]);
			world.setBlock(base.below(),
					CakeWorldBlocks.CARAMEL_CRUST.get()
							.defaultBlockState(), 2);
			for (int y = 0; y < reed[2]; y++) {
				world.setBlock(base.above(y),
						CakeWorldBlocks.TREACLE_REED.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void clearVegetation(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int y = 1; y <= 9; y++) {
				for (int z = -4; z <= 4; z++) {
					BlockPos position = local(centre,
							rotation, x, y, z);
					if (!world.isEmptyBlock(position)) {
						world.setBlock(position,
								Blocks.AIR.defaultBlockState(),
								2);
					}
				}
			}
		}
	}

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.CARAMEL_CRUST.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())
				|| state.is(Blocks.GRASS_BLOCK)
				|| state.is(Blocks.DIRT)
				|| state.is(Blocks.COARSE_DIRT)
				|| state.is(Blocks.PODZOL)
				|| state.is(Blocks.CLAY)
				|| state.is(Blocks.STONE);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS);
	}

	private static int highestSurfaceY(WorldGenLevel world,
			BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				highest = Math.max(highest,
						terrainSurfaceY(world,
								around.getX() + x,
								around.getZ() + z));
			}
		}
		return highest;
	}

	private static BlockPos surfaceAt(WorldGenLevel world,
			int x, int z) {
		return new BlockPos(x,
				world.getHeight(
						Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES,
						x, z) - 1,
				z);
	}

	private static int terrainSurfaceY(WorldGenLevel world,
			int x, int z) {
		return world.getHeight(
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				x, z) - 1;
	}

	private static BlockPos local(BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		BlockPos rotated = new BlockPos(x, y, z).rotate(rotation);
		return centre.offset(rotated);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
