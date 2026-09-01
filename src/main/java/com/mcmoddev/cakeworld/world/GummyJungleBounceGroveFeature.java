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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.VineBlock;
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
 * A bright Gummy Jungle micro-landmark with three striped lollipop trees,
 * stable elastic vines and three flavour-coded solid jelly pools.
 *
 * <p>The grove runs once after OreSpawn's surface pass. It may clear inherited
 * plants, logs and leaves, but rejects fluid, block entities and other solid
 * authored blocks. It creates no entities and has no repair or replay path, so
 * later player edits remain authoritative.</p>
 */
public final class GummyJungleBounceGroveFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("gummy_jungle_bounce_grove");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 12;
	public static final int SAFE_SITE_SEARCH_RADIUS = 6;
	public static final int MAX_TERRAIN_RELIEF = 4;
	public static final int PLACEMENT_SALT = 1978059;
	public static final GummyJungleBounceGroveFeature FEATURE =
			new GummyJungleBounceGroveFeature();
	private static final ResourceKey<Biome> GUMMY_JUNGLE_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.GUMMY_JUNGLE.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private GummyJungleBounceGroveFeature() {
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
								GummyJungleBounceGroveFeature>(
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
		if (!isGummyJungle(world, origin)) {
			return false;
		}
		BlockPos centre = findSafeSite(
				world, origin, placementChunk);
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
		buildLandingDisc(world, centre, rotation);
		buildJellyPools(world, centre, rotation);
		buildLollipopTree(world,
				local(centre, rotation, -3, 0, -3),
				7, CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get(),
				rotation);
		buildLollipopTree(world,
				local(centre, rotation, 3, 0, -3),
				8, CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get(),
				rotation);
		buildLollipopTree(world,
				local(centre, rotation, 0, 0, 3),
				7, CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get(),
				rotation);
		buildBubbleBeacon(world, centre, rotation);
		plantCandySprouts(world, centre, rotation);
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
				BlockPos surface = new BlockPos(
						horizontal.getX(), surfaceY,
						horizontal.getZ());
				BlockState state =
						world.getBlockState(surface);
				if (!state.isFaceSturdy(world, surface,
								Direction.UP)
						|| !world.getFluidState(surface)
								.isEmpty()
						|| !isAcceptedGround(state)) {
					return false;
				}
			}
		}
		for (int x = -5; x <= 5; x++) {
			for (int y = -2; y <= 11; y++) {
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
					BlockPos surfaceCandidate = surfaceAt(world,
							origin.getX() + x,
							origin.getZ() + z);
					if (!isGummyJungle(
							world, surfaceCandidate)) {
						continue;
					}
					BlockPos candidate = new BlockPos(
							surfaceCandidate.getX(),
							highestSurfaceY(
									world,
									surfaceCandidate),
							surfaceCandidate.getZ());
					Rotation rotation = orientation(
							world.getSeed(), candidate);
					if (fitsWithinChunk(candidate,
							rotation, placementChunk)
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

	private static boolean isGummyJungle(
			WorldGenLevel world, BlockPos position) {
		return world.getBiome(position).is(GUMMY_JUNGLE_KEY);
	}

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.CHOCOLATE_SPONGE.get())
				|| state.is(Blocks.GRASS_BLOCK)
				|| state.is(Blocks.DIRT)
				|| state.is(Blocks.COARSE_DIRT)
				|| state.is(Blocks.PODZOL)
				|| state.is(Blocks.ROOTED_DIRT)
				|| state.is(Blocks.MOSS_BLOCK)
				|| state.is(Blocks.STONE);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS);
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 11; y++) {
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

	private static void buildLandingDisc(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState support = CakeWorldBlocks.CHOCOLATE_SPONGE
				.get().defaultBlockState();
		BlockState landing = CakeWorldBlocks.GUMMY_BLOCK
				.get().defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				if (x * x + z * z <= 16) {
					BlockPos supportTop = local(
							centre, rotation,
							x, -1, z);
					buildSupportColumn(
							world, supportTop,
							support);
					set(world, centre, rotation,
							x, 0, z, landing);
				}
			}
		}
	}

	private static void buildJellyPools(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		buildJellyPool(world, centre, rotation,
				-2, 1, CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get());
		buildJellyPool(world, centre, rotation,
				2, 1, CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get());
		buildJellyPool(world, centre, rotation,
				0, -1, CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get());
	}

	private static void buildJellyPool(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int centreX, int centreZ, Block flavour) {
		int[][] pool = {
				{0, 0},
				{-1, 0},
				{1, 0},
				{0, -1},
				{0, 1}
		};
		for (int[] position : pool) {
			set(world, centre, rotation,
					centreX + position[0], 0,
					centreZ + position[1],
					flavour.defaultBlockState());
		}
	}

	private static void buildLollipopTree(
			WorldGenLevel world, BlockPos base,
			int height, Block canopyBlock,
			Rotation rotation) {
		buildSupportColumn(world, base.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState());
		BlockState trunk = CakeWorldBlocks
				.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		for (int y = 0; y < height; y++) {
			world.setBlock(base.above(y), trunk, 2);
		}
		BlockState canopy =
				canopyBlock.defaultBlockState();
		for (int y = height - 2; y <= height; y++) {
			int radius = y == height ? 1 : 2;
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (x * x + z * z
								> radius * radius + 1
							|| y < height
									&& x == 0
									&& z == 0) {
						continue;
					}
					world.setBlock(
							local(base, rotation,
									x, y, z),
							canopy, 2);
				}
			}
		}
		world.setBlock(base.above(height + 1),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(),
				2);
		buildVineStrand(world,
				local(base, rotation, -2,
						height - 3, 0),
				3);
		buildVineStrand(world,
				local(base, rotation, 2,
						height - 3, 0),
				4);
	}

	private static void buildVineStrand(
			WorldGenLevel world, BlockPos top,
			int length) {
		BlockState vine = CakeWorldBlocks.GUMMY_VINE
				.get().defaultBlockState()
						.setValue(VineBlock.UP, true);
		for (int step = 0; step < length; step++) {
			world.setBlock(top.below(step), vine, 2);
		}
	}

	private static void buildBubbleBeacon(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		set(world, centre, rotation, 0, 1, 0,
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState());
		set(world, centre, rotation, 0, 2, 0,
				CakeWorldBlocks.CANDY_GLASS
						.get().defaultBlockState());
		set(world, centre, rotation, 0, 3, 0,
				CakeWorldBlocks.GUMMY_BLOCK
						.get().defaultBlockState());
	}

	private static void plantCandySprouts(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		int[][] positions = {
				{-4, 0},
				{4, 0},
				{0, -4},
				{0, 4}
		};
		BlockState sprout = CakeWorldBlocks.CANDY_SPROUT
				.get().defaultBlockState()
						.setValue(CropBlock.AGE, 7);
		for (int[] position : positions) {
			set(world, centre, rotation,
					position[0], 1, position[1],
					sprout);
		}
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

	private static int highestSurfaceY(
			WorldGenLevel world, BlockPos centre) {
		int highest = world.getMinBuildHeight();
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				highest = Math.max(highest,
						terrainSurfaceY(world,
								centre.getX() + x,
								centre.getZ() + z));
			}
		}
		return highest;
	}

	private static void buildSupportColumn(
			WorldGenLevel world, BlockPos top,
			BlockState support) {
		int groundY = terrainSurfaceY(
				world, top.getX(), top.getZ());
		for (int y = top.getY();
				y >= groundY; y--) {
			world.setBlock(new BlockPos(
					top.getX(), y, top.getZ()),
					support, 2);
		}
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
