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
 * A raised, walkable Wafer bridge carried by Marshmallow cloud puffs.
 *
 * <p>The authored cells start above the finished surface so the landmark
 * remains independent from terrain replacement. Landing clouds at both ends
 * and cushions beneath the widest puffs make missed jumps forgiving.</p>
 */
public final class MarshmallowCloudBridgeFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("marshmallow_cloud_bridge");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 10;
	public static final int MAX_TERRAIN_RELIEF = 24;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 1978072;
	public static final int BRIDGE_Y = 3;
	public static final MarshmallowCloudBridgeFeature FEATURE =
			new MarshmallowCloudBridgeFeature();
	private static final ResourceKey<Biome> PEAKS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.MARSHMALLOW_PEAKS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[] CLOUD_CENTRES = {-4, 0, 4};
	private static Holder<PlacedFeature> placedFeature;

	private MarshmallowCloudBridgeFeature() {
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
								MarshmallowCloudBridgeFeature>(
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
		if (!world.getBiome(origin).is(PEAKS_KEY)) {
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
					BlockPos around = surfaceAt(world,
							origin.getX() + x,
							origin.getZ() + z);
					if (!world.getBiome(around).is(PEAKS_KEY)) {
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
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			Random random, BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearInheritedVegetation(world, centre, rotation);
		buildLandings(world, centre, rotation);
		buildBridge(world, centre, rotation);
		buildCloudPuffs(world, centre, rotation);
		buildChimePosts(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -6; x <= 6; x++) {
			for (int z = -2; z <= 2; z++) {
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
				BlockState ground = world.getBlockState(surface);
				if (!world.getFluidState(surface).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return false;
				}
				for (int y = surfaceY + 1;
					y <= centre.getY() + 5; y++) {
					BlockState state = world.getBlockState(
							new BlockPos(horizontal.getX(),
									y, horizontal.getZ()));
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
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-2, 2}) {
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

	public static int[] cloudCentres() {
		return CLOUD_CENTRES;
	}

	private static void buildLandings(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState marshmallow = CakeWorldBlocks.MARSHMALLOW
				.get().defaultBlockState();
		for (int x : new int[] {-6, 6}) {
			for (int z = -1; z <= 1; z++) {
				BlockPos landing = local(
						centre, rotation, x, 1, z);
				world.setBlock(landing, marshmallow, 2);
				supportDown(world, landing.below());
			}
		}
	}

	private static void buildBridge(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK
				.get().defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			set(world, centre, rotation,
					x, BRIDGE_Y, 0, wafer);
		}
		for (int x : new int[] {-5, 5}) {
			BlockPos step = local(centre, rotation,
					x, 2, 0);
			world.setBlock(step, wafer, 2);
			supportDown(world, step.below());
		}
	}

	private static void buildCloudPuffs(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState marshmallow = CakeWorldBlocks.MARSHMALLOW
				.get().defaultBlockState();
		BlockState icing = CakeWorldBlocks.ICING_LAYER
				.get().defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z : new int[] {-1, 1}) {
				set(world, centre, rotation,
						x, BRIDGE_Y, z,
						marshmallow);
			}
		}
		for (int x : CLOUD_CENTRES) {
			set(world, centre, rotation,
					x, BRIDGE_Y - 1, 0,
					marshmallow);
			for (int z : new int[] {-2, 2}) {
				set(world, centre, rotation,
						x, BRIDGE_Y, z,
						marshmallow);
			}
			for (int z : new int[] {-2, -1, 1, 2}) {
				set(world, centre, rotation,
						x, BRIDGE_Y + 1, z,
						icing);
			}
		}
	}

	private static void buildChimePosts(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState pillar = CakeWorldBlocks.CANDY_CANE_PILLAR
				.get().defaultBlockState();
		BlockState glass = CakeWorldBlocks.CANDY_GLASS
				.get().defaultBlockState();
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					set(world, centre, rotation,
							x, y, z, pillar);
				}
				set(world, centre, rotation,
						x, 4, z, glass);
			}
		}
	}

	private static void supportDown(WorldGenLevel world,
			BlockPos start) {
		BlockPos.MutableBlockPos cursor = start.mutable();
		while (cursor.getY() > world.getMinBuildHeight()
				&& world.isEmptyBlock(cursor)) {
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
		for (int x = -6; x <= 6; x++) {
			for (int z = -2; z <= 2; z++) {
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				for (int y = surfaceY + 1;
					y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					if (canClear(
							world.getBlockState(position))) {
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
		return state.is(CakeWorldBlocks.MARSHMALLOW.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(CakeWorldBlocks.PEPPERMINT_ROCK.get())
				|| state.is(CakeWorldBlocks.ROCK_CANDY.get())
				|| state.is(CakeWorldBlocks.WAFER_ROCK.get())
				|| state.is(CakeWorldBlocks.ICING.get())
				|| state.is(CakeWorldBlocks.ICING_LAYER.get())
				|| state.is(BlockTags.BASE_STONE_OVERWORLD)
				|| state.is(BlockTags.DIRT)
				|| state.is(Blocks.SNOW_BLOCK)
				|| state.is(Blocks.POWDER_SNOW)
				|| state.is(Blocks.GRAVEL);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS)
				|| state.is(Blocks.SNOW)
				|| state.is(Blocks.POWDER_SNOW);
	}

	private static int highestSurfaceY(WorldGenLevel world,
			BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -6; x <= 6; x++) {
			for (int z = -2; z <= 2; z++) {
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
		/*
		 * Icing is a thin layer and Marshmallow deliberately has gentle
		 * collision behaviour, so a heightmap is allowed to stop at the
		 * structural filler immediately beneath either one. Recognise the
		 * canonical Peaks surface explicitly before deciding that the soft
		 * layer is an obstruction.
		 */
		int heightmapSurface = y;
		for (int above = 1; above <= 2; above++) {
			cursor.set(x, heightmapSurface + above, z);
			BlockState state = world.getBlockState(cursor);
			if (state.is(CakeWorldBlocks.MARSHMALLOW.get())
					|| state.is(CakeWorldBlocks.ICING_LAYER.get())) {
				y = heightmapSurface + above;
			} else {
				break;
			}
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
