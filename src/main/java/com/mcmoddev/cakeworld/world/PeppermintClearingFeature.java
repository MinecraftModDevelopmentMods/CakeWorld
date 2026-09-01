package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
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
 * A quiet Peppermint Pinewoods micro-landmark: three striped pines surround a
 * frosted clearing and a tiny crystal-topped chime post.
 *
 * <p>The scene is generated once, after OreSpawn applies the configured
 * surface palette. It may clear inherited plants, logs and leaves inside its
 * bounded footprint, but it never repairs or replays and it spawns no
 * entities. Player edits remain authoritative after chunk generation.</p>
 */
public final class PeppermintClearingFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("peppermint_clearing");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 18;
	public static final int SAFE_SITE_SEARCH_RADIUS = 6;
	public static final int PLACEMENT_SALT = 1978041;
	public static final PeppermintClearingFeature FEATURE =
			new PeppermintClearingFeature();
	private static final ResourceKey<Biome> PEPPERMINT_PINEWOODS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.PEPPERMINT_PINEWOODS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private PeppermintClearingFeature() {
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
								PeppermintClearingFeature>(
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
		if (!isPeppermintPinewoods(world, origin)) {
			return false;
		}
		BlockPos centre = findSafeSite(world, origin,
				placementChunk);
		return centre != null
				&& buildAt(world, centre,
						orientation(world.getSeed(), centre));
	}

	/**
	 * Deterministic construction seam used by GameTests.
	 */
	public static boolean buildAt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearInheritedVegetation(world, centre, rotation);
		buildFrostedClearing(world, centre, rotation);
		buildPeppermintPine(world,
				local(centre, rotation, -3, 0, -2), 6);
		buildPeppermintPine(world,
				local(centre, rotation, 3, 0, -2), 7);
		buildPeppermintPine(world,
				local(centre, rotation, 0, 0, 3), 6);
		buildChimePost(world, centre, rotation);
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

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				BlockPos horizontal = local(centre, rotation,
						x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (Math.abs(surfaceY - centre.getY()) > 1) {
					return false;
				}
				BlockPos surface = new BlockPos(
						horizontal.getX(), surfaceY,
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
			for (int y = -1; y <= 9; y++) {
				for (int z = -5; z <= 5; z++) {
					BlockState state = world.getBlockState(
							local(centre, rotation,
									x, y, z));
					if (state.hasBlockEntity()
							|| y > 0
									&& !canClear(state)) {
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
				{-5, -5},
				{-5, 5},
				{5, -5},
				{5, 5}
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
							&& isPeppermintPinewoods(
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

	private static boolean isPeppermintPinewoods(
			WorldGenLevel world, BlockPos position) {
		return world.getBiome(position)
				.is(PEPPERMINT_PINEWOODS_KEY);
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 9; y++) {
				for (int z = -5; z <= 5; z++) {
					BlockPos position =
							local(centre, rotation,
									x, y, z);
					BlockState state =
							world.getBlockState(position);
					if (canClear(state)) {
						world.setBlock(position,
								Blocks.AIR
										.defaultBlockState(),
								2);
					}
				}
			}
		}
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS);
	}

	private static void buildFrostedClearing(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		BlockState frosting = CakeWorldBlocks.ICING_LAYER
				.get().defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				if (x * x + z * z <= 16) {
					set(world, centre, rotation,
							x, 0, z, frosting);
				}
			}
		}
	}

	private static void buildPeppermintPine(
			WorldGenLevel world, BlockPos base,
			int height) {
		BlockState trunk = CakeWorldBlocks.CANDY_CANE_PILLAR
				.get().defaultBlockState();
		for (int y = 0; y < height; y++) {
			world.setBlock(base.above(y), trunk, 2);
		}
		BlockState needles = CakeWorldBlocks.ICING
				.get().defaultBlockState();
		for (int y = height - 3; y <= height; y++) {
			int radius;
			if (y <= height - 2) {
				radius = 2;
			} else if (y == height - 1) {
				radius = 1;
			} else {
				radius = 0;
			}
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (x * x + z * z
								> radius * radius + 1
							|| radius > 0
									&& x == 0
									&& z == 0) {
						continue;
					}
					world.setBlock(base.offset(x, y, z),
							needles, 2);
				}
			}
		}
	}

	private static void buildChimePost(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState post = CakeWorldBlocks.CANDY_CANE_PILLAR
				.get().defaultBlockState();
		for (int y = 0; y <= 2; y++) {
			set(world, centre, rotation,
					0, y, 0, post);
		}
		BlockState chime = CakeWorldBlocks.CANDY_GLASS
				.get().defaultBlockState();
		set(world, centre, rotation,
				1, 2, 0, chime);
		set(world, centre, rotation,
				-1, 2, 0, chime);
		set(world, centre, rotation,
				0, 2, 1, chime);
		set(world, centre, rotation,
				0, 2, -1, chime);
		set(world, centre, rotation,
				0, 3, 0,
				CakeWorldBlocks.MINT_CRYSTAL
						.get().defaultBlockState());
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
