package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

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
import net.minecraft.world.level.block.CropBlock;
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
 * A little frosting garden with four flavour blooms, a glass nectar bell and
 * twelve harvest-ready Candy Sprouts.
 *
 * <p>The complete authored envelope stays inside its generating chunk. The
 * feature creates no entities or block entities and has no repair pass, so
 * later player gardening is never overwritten.</p>
 */
public final class CupcakeBloomCircleFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("cupcake_bloom_circle");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 6;
	public static final int MAX_TERRAIN_RELIEF = 4;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 1978075;
	public static final CupcakeBloomCircleFeature FEATURE =
			new CupcakeBloomCircleFeature();
	private static final ResourceKey<Biome> GARDENS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.CUPCAKE_GARDENS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] BLOOMS = {
			{-2, -2, 0},
			{2, -2, 1},
			{-2, 2, 2},
			{2, 2, 3}
	};
	private static final int[][] OUTER_SPROUTS = {
			{-3, -1}, {-3, 1}, {3, -1}, {3, 1},
			{-1, -3}, {1, -3}, {-1, 3}, {1, 3}
	};
	private static Holder<PlacedFeature> placedFeature;

	private CupcakeBloomCircleFeature() {
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
								CupcakeBloomCircleFeature>(
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
		if (!world.getBiome(origin).is(GARDENS_KEY)) {
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
					if (!world.getBiome(around).is(GARDENS_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							around.getX(),
							highestSurfaceY(world, around),
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
		buildFrostingGarden(world, centre, rotation);
		buildBlooms(world, centre, rotation);
		buildNectarBell(world, centre, rotation);
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
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (surfaceY > centre.getY()
						|| centre.getY() - surfaceY
								> MAX_TERRAIN_RELIEF) {
					return "relief at " + horizontal
							+ ": surfaceY=" + surfaceY
							+ ", centreY=" + centre.getY();
				}
				BlockPos surface = new BlockPos(
						horizontal.getX(), surfaceY,
						horizontal.getZ());
				BlockState ground = world.getBlockState(surface);
				if (!world.getFluidState(surface).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return "ground at " + surface
							+ ": state=" + ground
							+ ", fluid="
							+ world.getFluidState(surface)
							+ ", blockEntity="
							+ ground.hasBlockEntity();
				}
				for (int y = surfaceY + 1;
						y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					BlockState state =
							world.getBlockState(position);
					if (state.hasBlockEntity()
							|| !canClear(state)) {
						return "obstacle at " + position
								+ ": state=" + state
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

	public static int[][] blooms() {
		return BLOOMS;
	}

	public static int[][] outerSprouts() {
		return OUTER_SPROUTS;
	}

	private static void buildFrostingGarden(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState state;
				if (Math.abs(x) == 4 && Math.abs(z) == 4) {
					state = CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState();
				} else if (x == 0 || z == 0) {
					state = CakeWorldBlocks.ICING.get()
							.defaultBlockState();
				} else {
					state = CakeWorldBlocks.CHOCOLATE_SPONGE
							.get().defaultBlockState();
				}
				BlockPos platform =
						local(centre, rotation, x, 1, z);
				world.setBlock(platform, state, 2);
				supportDown(world, platform.below());
			}
		}
	}

	private static void buildBlooms(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState[] flavours = {
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get()
						.defaultBlockState(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
						.defaultBlockState()
		};
		BlockState icing = CakeWorldBlocks.ICING.get()
				.defaultBlockState();
		BlockState sprout = matureSprout();
		for (int[] bloom : BLOOMS) {
			int x = bloom[0];
			int z = bloom[1];
			set(world, centre, rotation, x, 2, z,
					flavours[bloom[2]]);
			set(world, centre, rotation, x - 1, 2, z, icing);
			set(world, centre, rotation, x + 1, 2, z, icing);
			set(world, centre, rotation, x, 2, z - 1, icing);
			set(world, centre, rotation, x, 2, z + 1, icing);
			set(world, centre, rotation, x, 3, z, sprout);
		}
		for (int[] outer : OUTER_SPROUTS) {
			set(world, centre, rotation,
					outer[0], 2, outer[1], sprout);
		}
	}

	private static void buildNectarBell(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		for (int y = 2; y <= 4; y++) {
			set(world, centre, rotation, 0, y, 0, glass);
		}
		set(world, centre, rotation, 0, 5, 0,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
						.defaultBlockState());
	}

	private static BlockState matureSprout() {
		return CakeWorldBlocks.CANDY_SPROUT.get()
				.defaultBlockState()
				.setValue(CropBlock.AGE, CropBlock.MAX_AGE);
	}

	private static void supportDown(WorldGenLevel world,
			BlockPos start) {
		BlockPos.MutableBlockPos cursor = start.mutable();
		while (cursor.getY() > world.getMinBuildHeight()
				&& canClear(world.getBlockState(cursor))) {
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
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				for (int y = surfaceY + 1;
						y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					if (canClear(world.getBlockState(position))) {
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
		return state.is(CakeWorldBlocks.ICING.get())
				|| state.is(CakeWorldBlocks.ICING_LAYER.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(BlockTags.BASE_STONE_OVERWORLD)
				|| state.is(BlockTags.DIRT)
				|| state.is(Blocks.GRAVEL);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(CakeWorldBlocks.ICING_LAYER.get())
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS);
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
		int solidSurface = y;
		cursor.set(x, solidSurface + 1, z);
		if (world.getBlockState(cursor)
				.is(CakeWorldBlocks.ICING_LAYER.get())) {
			y = solidSurface + 1;
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
