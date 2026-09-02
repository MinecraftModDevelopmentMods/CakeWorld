package zone.moddev.mc.cakeworld.world;

import java.util.List;

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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

/**
 * A compact, route-readable Nether labyrinth with two optional soft gates.
 *
 * <p>The complete authored plan fits one chunk. It requires a broad natural
 * support shelf, refuses fluids, block entities and solid obstacles, creates
 * no entity or inventory, and has no repair pass. The ordinary north-to-south
 * route therefore works without breaking blocks, while the two Marshmallow
 * gates expose visible looping shortcuts.</p>
 */
public final class BlackLiquoriceLoopFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("black_liquorice_loop_labyrinth");
	public static final int MIN_Y = 24;
	public static final int MAX_Y = 112;
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MIN_SOLID_SUPPORTS = 33;
	public static final int PLACEMENT_SALT = 2718281;
	public static final BlackLiquoriceLoopFeature FEATURE =
			new BlackLiquoriceLoopFeature();
	private static final ResourceKey<Biome> LABYRINTH_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.BLACK_LIQUORICE_LABYRINTHS.getId());
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};
	private static final String[] MAZE = {
		"####.####",
		"#.......#",
		"#.###.#.#",
		"#.#...#.#",
		"#.#.....#",
		"#...#.#.#",
		"###.#.#.#",
		"#.......#",
		"#######.#"
	};
	private static final int[][] SHORTCUTS = {
		{0, -2},
		{0, 2}
	};
	private static Holder<PlacedFeature> placedFeature;

	private BlackLiquoriceLoopFeature() {
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
								BlackLiquoriceLoopFeature>(
									FEATURE,
									NoneFeatureConfiguration.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						RarityFilter.onAverageOnceEvery(
								AVERAGE_CHUNKS_PER_ATTEMPT),
						InSquarePlacement.spread(),
						HeightRangePlacement.uniform(
								VerticalAnchor.absolute(MIN_Y),
								VerticalAnchor.absolute(MAX_Y)),
						BiomeFilter.biome())));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		ChunkPos chunk = new ChunkPos(context.origin());
		int minY = Math.max(world.getMinBuildHeight() + 2, MIN_Y);
		int maxY = Math.min(world.getMaxBuildHeight() - 5, MAX_Y);
		int[] offsets = {4, 5, 6, 7, 8, 9, 10, 11};
		int startY = Math.max(minY,
				Math.min(maxY, context.origin().getY()));
		for (int offset = 0; offset <= maxY - minY; offset++) {
			for (int direction : offset == 0
					? new int[] {0} : new int[] {-1, 1}) {
				int y = startY + offset * direction;
				if (y < minY || y > maxY) {
					continue;
				}
				for (int offsetX : offsets) {
					for (int offsetZ : offsets) {
						BlockPos centre = new BlockPos(
								chunk.getMinBlockX() + offsetX,
								y,
								chunk.getMinBlockZ() + offsetZ);
						if (!world.getBiome(centre).is(LABYRINTH_KEY)
								|| !canOccupy(world, centre)
								|| !isNaturalSupport(
										world, centre.below())) {
							continue;
						}
						Rotation rotation = orientation(
								world.getSeed(), centre);
						if (fitsWithinChunk(centre, rotation, chunk)
								&& buildAt(world, centre, rotation)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (!hasSafeSite(world, centre, rotation)) {
			return false;
		}
		clearRoutes(world, centre, rotation);
		buildFloor(world, centre, rotation);
		buildWalls(world, centre, rotation);
		buildArch(world, centre, rotation, 0, -4);
		buildArch(world, centre, rotation, 3, 4);
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				set(world, centre, rotation, x, 4, z,
						CakeWorldBlocks.MINT_CRYSTAL.get()
								.defaultBlockState());
			}
		}
		return true;
	}

	public static boolean hasSafeSite(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (centre.getY() < Math.max(
				world.getMinBuildHeight() + 2, MIN_Y)
				|| centre.getY() > Math.min(
						world.getMaxBuildHeight() - 5, MAX_Y)) {
			return false;
		}
		int supports = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 2; y++) {
					if (!canOccupy(world,
							local(centre, rotation, x, y, z))) {
						return false;
					}
				}
				if (isNaturalSupport(world,
						local(centre, rotation, x, -1, z))) {
					supports++;
				}
			}
		}
		for (int[] upper : new int[][] {
			{-1, 3, -4}, {0, 3, -4}, {1, 3, -4},
			{2, 3, 4}, {3, 3, 4}, {4, 3, 4},
			{-4, 4, -4}, {4, 4, -4}, {-4, 4, 4}, {4, 4, 4}
		}) {
			if (!canOccupy(world, local(centre, rotation,
					upper[0], upper[1], upper[2]))) {
				return false;
			}
		}
		return supports >= MIN_SOLID_SUPPORTS;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				BlockPos corner = local(centre, rotation, x, 0, z);
				if (Math.floorDiv(corner.getX(), 16) != chunk.x
						|| Math.floorDiv(corner.getZ(), 16) != chunk.z) {
					return false;
				}
			}
		}
		return true;
	}

	public static Rotation orientation(long seed, BlockPos centre) {
		long mixed = seed ^ centre.asLong() ^ PLACEMENT_SALT;
		mixed = (mixed ^ mixed >>> 30) * -4658895280553007687L;
		mixed = (mixed ^ mixed >>> 27) * -7723592293110705685L;
		mixed ^= mixed >>> 31;
		return ROTATIONS[(int) (mixed & 3L)];
	}

	public static BlockPos local(BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return centre.offset(new BlockPos(x, y, z).rotate(rotation));
	}

	public static String[] mazeRows() {
		return MAZE.clone();
	}

	public static int[][] shortcuts() {
		return SHORTCUTS;
	}

	public static boolean isWall(int x, int z) {
		if (x < -4 || x > 4 || z < -4 || z > 4) {
			return false;
		}
		return MAZE[z + 4].charAt(x + 4) == '#';
	}

	public static boolean isShortcut(int x, int z) {
		for (int[] shortcut : SHORTCUTS) {
			if (shortcut[0] == x && shortcut[1] == z) {
				return true;
			}
		}
		return false;
	}

	private static void clearRoutes(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 1; y <= 2; y++) {
					set(world, centre, rotation, x, y, z,
							Blocks.AIR.defaultBlockState());
				}
			}
		}
	}

	private static void buildFloor(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState stone = CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()
				.defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				set(world, centre, rotation, x, 0, z, stone);
			}
		}
		set(world, centre, rotation, 0, 0, 0,
				CakeWorldBlocks.GILDED_BURNT_TOFFEE.get()
						.defaultBlockState());
	}

	private static void buildWalls(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState root = CakeWorldBlocks.LIQUORICE_ROOT.get()
				.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		for (int z = -4; z <= 4; z++) {
			for (int x = -4; x <= 4; x++) {
				if (!isWall(x, z)) {
					continue;
				}
				if (isShortcut(x, z)) {
					set(world, centre, rotation, x, 1, z,
							CakeWorldBlocks.MARSHMALLOW.get()
									.defaultBlockState());
					continue;
				}
				set(world, centre, rotation, x, 1, z, root);
				BlockState upper = Math.abs(x) == 4
						&& Math.abs(z) == 4
								? CakeWorldBlocks.CANDY_GLASS.get()
										.defaultBlockState()
								: root;
				set(world, centre, rotation, x, 2, z, upper);
			}
		}
	}

	private static void buildArch(WorldGenLevel world,
			BlockPos centre, Rotation rotation, int centreX, int z) {
		for (int x = centreX - 1; x <= centreX + 1; x++) {
			set(world, centre, rotation, x, 3, z,
					CakeWorldBlocks.LIQUORICE_BRICKS.get()
							.defaultBlockState());
		}
	}

	private static boolean canOccupy(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.hasBlockEntity()
				&& (state.isAir() || state.getMaterial().isReplaceable());
	}

	private static boolean isNaturalSupport(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.isAir()
				&& !state.getMaterial().isReplaceable()
				&& !state.hasBlockEntity();
	}

	private static void set(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int x, int y, int z, BlockState state) {
		world.setBlock(local(centre, rotation, x, y, z), state, 2);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
