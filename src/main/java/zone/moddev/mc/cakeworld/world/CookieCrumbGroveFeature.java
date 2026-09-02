package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
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
 * A small Cookie Forest ecology scene: four Wafer trees with broad biscuit
 * canopies shelter a shallow, player-accessible crumb burrow.
 *
 * <p>The grove is an ordinary non-locatable placed feature. Its one bounded
 * construction pass may clear plants and leaves inside the authored canopy,
 * but it never replays after chunk generation, spawns no entities and refuses
 * to overwrite a block entity. Later player edits therefore remain
 * authoritative.</p>
 */
public final class CookieCrumbGroveFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("cookie_crumb_grove");
	public static final ResourceLocation LOOT_ID =
			id("chests/cookie_crumb_burrow");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 24;
	public static final int SAFE_SITE_SEARCH_RADIUS = 6;
	public static final int PLACEMENT_SALT = 1978027;
	public static final CookieCrumbGroveFeature FEATURE =
			new CookieCrumbGroveFeature();
	private static final ResourceKey<Biome> COOKIE_FOREST_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.COOKIE_FOREST.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private CookieCrumbGroveFeature() {
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
								CookieCrumbGroveFeature>(
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
		BlockPos origin = surfaceAt(world,
				context.origin().getX(),
				context.origin().getZ());
		ChunkPos placementChunk =
				new ChunkPos(context.origin());
		if (!isCookieForest(world, origin)) {
			return false;
		}
		BlockPos centre = findSafeSite(world, origin,
				placementChunk);
		return centre != null
				&& buildAt(world, context.random(), centre,
						orientation(world.getSeed(), centre));
	}

	/**
	 * Deterministic construction seam used by GameTests. The supplied centre is
	 * the central surface block above the burrow cache.
	 */
	public static boolean buildAt(WorldGenLevel world, Random random,
			BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearPlantsAndLeaves(world, centre, rotation);
		buildBurrow(world, random, centre, rotation);
		buildCrumbPath(world, centre, rotation);

		int[][] trees = {
				{-3, -2, 5},
				{3, -2, 6},
				{-3, 3, 6},
				{3, 3, 5}
		};
		Block[] sprinkles = {
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
		};
		for (int index = 0; index < trees.length; index++) {
			BlockPos base = local(centre, rotation,
					trees[index][0], 0, trees[index][1]);
			buildWaferTree(world, base,
					trees[index][2],
					sprinkles[index]);
		}
		return true;
	}

	public static Rotation orientation(long worldSeed,
			BlockPos centre) {
		long mixed = worldSeed ^ centre.asLong()
				^ PLACEMENT_SALT;
		mixed = (mixed ^ mixed >>> 30)
				* -4658895280553007687L;
		mixed = (mixed ^ mixed >>> 27)
				* -7723592293110705685L;
		mixed ^= mixed >>> 31;
		return ROTATIONS[(int) (mixed & 3L)];
	}

	public static BlockPos cachePosition(BlockPos centre) {
		return centre.below(2);
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre) {
		return hasSafeFootprint(world, centre,
				orientation(world.getSeed(), centre));
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -4; z <= 10; z++) {
				BlockPos horizontal = local(centre, rotation,
						x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (Math.abs(surfaceY - centre.getY()) > 1) {
					return false;
				}
				BlockPos surface = new BlockPos(
						horizontal.getX(),
						surfaceY,
						horizontal.getZ());
				if (!world.getBlockState(surface)
						.isFaceSturdy(world, surface,
								Direction.UP)
						|| !world.getFluidState(surface)
								.isEmpty()) {
					return false;
				}
			}
		}
		for (int x = -5; x <= 5; x++) {
			for (int y = -3; y <= 9; y++) {
				for (int z = -4; z <= 10; z++) {
					if (world.getBlockState(
							local(centre, rotation,
									x, y, z))
							.hasBlockEntity()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		int[][] corners = {
				{-5, -4},
				{-5, 10},
				{5, -4},
				{5, 10}
		};
		for (int[] corner : corners) {
			BlockPos position = local(centre, rotation,
					corner[0], 0, corner[1]);
			if (Math.floorDiv(position.getX(), 16)
						!= chunk.x
					|| Math.floorDiv(position.getZ(), 16)
							!= chunk.z) {
				return false;
			}
		}
		return true;
	}

	private static BlockPos findSafeSite(WorldGenLevel world,
			BlockPos origin, ChunkPos placementChunk) {
		for (int radius = 0;
				radius <= SAFE_SITE_SEARCH_RADIUS; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (radius > 0
							&& Math.abs(x) != radius
							&& Math.abs(z) != radius) {
						continue;
					}
					BlockPos candidate = surfaceAt(world,
							origin.getX() + x,
							origin.getZ() + z);
					Rotation rotation = orientation(
							world.getSeed(), candidate);
					if (fitsWithinChunk(candidate,
							rotation,
							placementChunk)
							&& isCookieForest(
									world, candidate)
							&& hasSafeFootprint(
									world, candidate,
									rotation)) {
						return candidate;
					}
				}
			}
		}
		return null;
	}

	private static boolean isCookieForest(WorldGenLevel world,
			BlockPos position) {
		return world.getBiome(position)
				.is(COOKIE_FOREST_KEY);
	}

	private static void clearPlantsAndLeaves(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 9; y++) {
				for (int z = -4; z <= 10; z++) {
					BlockPos position =
							local(centre, rotation,
									x, y, z);
					BlockState state =
							world.getBlockState(position);
					if (state.getMaterial().isReplaceable()
							|| state.is(BlockTags.LEAVES)
							|| state.is(BlockTags.LOGS)) {
						world.setBlock(position,
								Blocks.AIR
										.defaultBlockState(),
								2);
					}
				}
			}
		}
	}

	private static void buildBurrow(WorldGenLevel world,
			Random random, BlockPos centre,
			Rotation rotation) {
		BlockState wall = CakeWorldBlocks.BISCUIT_STONE
				.get().defaultBlockState();
		BlockState floor = CakeWorldBlocks.WAFER_BLOCK
				.get().defaultBlockState();
		BlockState roof =
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState();
		for (int x = -2; x <= 2; x++) {
			for (int y = -3; y <= 0; y++) {
				for (int z = -2; z <= 2; z++) {
					BlockState state;
					if (y == -3) {
						state = floor;
					} else if (y == 0) {
						state = roof;
					} else if (Math.abs(x) == 2
							|| Math.abs(z) == 2) {
						state = wall;
					} else {
						state = Blocks.AIR
								.defaultBlockState();
					}
					set(world, centre, rotation,
							x, y, z, state);
				}
			}
		}
		for (int step = 0; step < 4; step++) {
			int z = 3 + step;
			int floorY = -3 + step;
			for (int x = -1; x <= 1; x++) {
				set(world, centre, rotation,
						x, floorY, z, floor);
				set(world, centre, rotation,
						x, floorY + 3, z, roof);
			}
			set(world, centre, rotation,
					-2, floorY + 1, z, wall);
			set(world, centre, rotation,
					2, floorY + 1, z, wall);
			set(world, centre, rotation,
					-2, floorY + 2, z, wall);
			set(world, centre, rotation,
					2, floorY + 2, z, wall);
			for (int x = -1; x <= 1; x++) {
				set(world, centre, rotation,
						x, floorY + 1, z,
						Blocks.AIR.defaultBlockState());
				set(world, centre, rotation,
						x, floorY + 2, z,
						Blocks.AIR.defaultBlockState());
			}
		}

		BlockPos cache = cachePosition(centre);
		world.setBlock(cache,
				Blocks.CHEST.defaultBlockState()
						.rotate(rotation),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, cache, LOOT_ID);
	}

	private static void buildCrumbPath(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState crumbs = CakeWorldBlocks.BISCUIT_CRUMBS
				.get().defaultBlockState();
		for (int z = 6; z <= 10; z++) {
			for (int x = -1; x <= 1; x++) {
				set(world, centre, rotation,
						x, 0, z, crumbs);
			}
		}
	}

	private static void buildWaferTree(WorldGenLevel world,
			BlockPos base, int height, Block sprinkle) {
		BlockState trunk = CakeWorldBlocks.WAFER_BLOCK
				.get().defaultBlockState();
		for (int y = -1; y <= height; y++) {
			world.setBlock(base.above(y), trunk, 2);
		}
		BlockState canopy = CakeWorldBlocks.BISCUIT_STONE
				.get().defaultBlockState();
		for (int y = height - 1; y <= height + 1; y++) {
			int radius = y == height + 1 ? 1 : 2;
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (x * x + z * z
							<= radius * radius + 1
							&& (x != 0 || z != 0
									|| y > height)) {
						world.setBlock(
								base.offset(x, y, z),
								canopy, 2);
					}
				}
			}
		}
		world.setBlock(base.offset(1, height + 1, 0),
				sprinkle.defaultBlockState(), 2);
		world.setBlock(base.offset(-1, height, 1),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(),
				2);
	}

	private static BlockPos surfaceAt(WorldGenLevel world,
			int x, int z) {
		return new BlockPos(x,
				terrainSurfaceY(world, x, z), z);
	}

	private static int terrainSurfaceY(WorldGenLevel world,
			int x, int z) {
		int y = world.getHeight(
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				x, z) - 1;
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos(x, y, z);
		while (y > world.getMinBuildHeight()) {
			BlockState state =
					world.getBlockState(cursor);
			if (!world.getFluidState(cursor).isEmpty()
					|| !state.getMaterial().isReplaceable()
					&& !state.is(BlockTags.LEAVES)
					&& !state.is(BlockTags.LOGS)) {
				break;
			}
			cursor.setY(--y);
		}
		return y;
	}

	private static void set(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int x, int y, int z, BlockState state) {
		world.setBlock(local(centre, rotation,
				x, y, z), state.rotate(rotation), 2);
	}

	private static BlockPos local(BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return switch (rotation) {
		case NONE -> centre.offset(x, y, z);
		case CLOCKWISE_90 -> centre.offset(-z, y, x);
		case CLOCKWISE_180 -> centre.offset(-x, y, -z);
		case COUNTERCLOCKWISE_90 ->
			centre.offset(z, y, -x);
		};
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
