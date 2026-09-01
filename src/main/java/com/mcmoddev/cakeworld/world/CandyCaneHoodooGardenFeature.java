package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

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
 * A bounded Candy-Cane Badlands landmark with four striped hoodoos, broad
 * wafer capstones and crystal markers around a walkable geology court.
 *
 * <p>The display is intentionally authored scenery. Natural OreSpawn rock and
 * geome surveys remain the evidence for real formation boundaries.</p>
 */
public final class CandyCaneHoodooGardenFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("candy_cane_hoodoo_garden");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 4;
	public static final int MAX_TERRAIN_RELIEF = 24;
	private static final int SAFE_SITE_MIN_OFFSET = 5;
	private static final int SAFE_SITE_WIDTH = 6;
	public static final int PLACEMENT_SALT = 1978071;
	public static final CandyCaneHoodooGardenFeature FEATURE =
			new CandyCaneHoodooGardenFeature();
	private static final ResourceKey<Biome> BADLANDS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.CANDY_CANE_BADLANDS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] HOODOOS = {
			{-3, -3, 4, 0},
			{3, -3, 5, 1},
			{-3, 3, 6, 1},
			{3, 3, 7, 0}
	};
	private static Holder<PlacedFeature> placedFeature;

	private CandyCaneHoodooGardenFeature() {
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
								CandyCaneHoodooGardenFeature>(
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
		if (!world.getBiome(origin).is(BADLANDS_KEY)) {
			return false;
		}
		ChunkPos placementChunk = new ChunkPos(context.origin());
		int startX = context.random().nextInt(SAFE_SITE_WIDTH);
		int startZ = context.random().nextInt(SAFE_SITE_WIDTH);
		for (int xIndex = 0;
				xIndex < SAFE_SITE_WIDTH; xIndex++) {
			for (int zIndex = 0;
					zIndex < SAFE_SITE_WIDTH; zIndex++) {
					int localX = SAFE_SITE_MIN_OFFSET
							+ Math.floorMod(startX + xIndex,
									SAFE_SITE_WIDTH);
					int localZ = SAFE_SITE_MIN_OFFSET
							+ Math.floorMod(startZ + zIndex,
									SAFE_SITE_WIDTH);
					BlockPos around = surfaceAt(world,
							placementChunk.getMinBlockX()
									+ localX,
							placementChunk.getMinBlockZ()
									+ localZ);
					if (!world.getBiome(around)
							.is(BADLANDS_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							around.getX(),
							highestSurfaceY(world, around),
							around.getZ());
					Rotation rotation = orientation(
							world.getSeed(), centre);
					if (fitsWithinChunk(centre,
							rotation, placementChunk)
							&& buildAt(world,
									context.random(),
									centre, rotation)) {
						return true;
					}
			}
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			Random random, BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearInheritedVegetation(world, centre, rotation);
		buildStripedCourt(world, centre, rotation);
		buildWaferPath(world, centre, rotation);
		for (int[] hoodoo : HOODOOS) {
			buildHoodoo(world, centre, rotation,
					hoodoo[0], hoodoo[1],
					hoodoo[2], hoodoo[3] == 0);
		}
		return true;
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
				BlockState ground =
						world.getBlockState(surface);
				if (!world.getFluidState(surface).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return false;
				}
				for (int y = surfaceY + 1;
					y <= centre.getY() + 9; y++) {
					BlockState state = world.getBlockState(
							new BlockPos(
									horizontal.getX(),
									y,
									horizontal.getZ()));
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
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-5, 5}) {
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

	public static BlockPos local(BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return centre.offset(new BlockPos(x, y, z)
				.rotate(rotation));
	}

	public static BlockState courtState(int x, int z) {
		return switch (Math.floorMod(x + z, 4)) {
			case 0 -> CakeWorldBlocks.CANDY_CANE_PILLAR
					.get().defaultBlockState();
			case 1 -> CakeWorldBlocks.WAFER_ROCK
					.get().defaultBlockState();
			case 2 -> CakeWorldBlocks.PEPPERMINT_ROCK
					.get().defaultBlockState();
			default -> CakeWorldBlocks.ROCK_CANDY
					.get().defaultBlockState();
		};
	}

	public static int[][] hoodoos() {
		return HOODOOS;
	}

	private static void buildStripedCourt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos top = local(
						centre, rotation, x, 0, z);
				world.setBlock(top, courtState(x, z), 2);
				for (int y = -1;
					y >= -MAX_TERRAIN_RELIEF; y--) {
					BlockPos support = top.offset(0, y, 0);
					if (!world.isEmptyBlock(support)) {
						break;
					}
					world.setBlock(support,
							CakeWorldBlocks.BISCUIT_STONE
									.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static void buildWaferPath(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int z = -5; z <= 5; z++) {
			set(world, centre, rotation, 0, 0, z,
					CakeWorldBlocks.WAFER_BLOCK.get()
							.defaultBlockState());
		}
		for (int x : new int[] {-1, 1}) {
			set(world, centre, rotation, x, 0, 0,
					CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState());
		}
	}

	private static void buildHoodoo(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int x, int z, int height,
			boolean rockCandyCrystal) {
		BlockState pillar = CakeWorldBlocks.CANDY_CANE_PILLAR
				.get().defaultBlockState();
		for (int y = 1; y <= height; y++) {
			set(world, centre, rotation, x, y, z, pillar);
		}
		BlockState cap = CakeWorldBlocks.WAFER_ROCK
				.get().defaultBlockState();
		for (int capX = -1; capX <= 1; capX++) {
			for (int capZ = -1; capZ <= 1; capZ++) {
				set(world, centre, rotation,
						x + capX, height + 1,
						z + capZ, cap);
			}
		}
		set(world, centre, rotation, x, height + 2, z,
				(rockCandyCrystal
						? CakeWorldBlocks.ROCK_CANDY_DEPOSIT
						: CakeWorldBlocks.MINT_CRYSTAL)
						.get().defaultBlockState());
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int y = 0; y <= 9; y++) {
				for (int z = -5; z <= 5; z++) {
					BlockPos position = local(centre,
							rotation, x, y, z);
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

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.WAFER_ROCK.get())
				|| state.is(CakeWorldBlocks.CANDY_CANE_PILLAR.get())
				|| state.is(CakeWorldBlocks.PEPPERMINT_ROCK.get())
				|| state.is(CakeWorldBlocks.ROCK_CANDY.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(BlockTags.TERRACOTTA)
				|| state.is(Blocks.RED_SAND)
				|| state.is(Blocks.SAND)
				|| state.is(Blocks.GRASS_BLOCK)
				|| state.is(Blocks.DIRT)
				|| state.is(Blocks.COARSE_DIRT)
				|| state.is(Blocks.STONE);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS)
				|| state.is(Blocks.CACTUS);
	}

	private static int highestSurfaceY(WorldGenLevel world,
			BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
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
			BlockState state = world.getBlockState(cursor);
			if (!world.getFluidState(cursor).isEmpty()
					|| !canClear(state)) {
				break;
			}
			y--;
			cursor.setY(y);
		}
		return y;
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
