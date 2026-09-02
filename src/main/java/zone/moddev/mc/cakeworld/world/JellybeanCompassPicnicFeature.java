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
 * A four-flavour picnic compass that makes each Jellybean island readable
 * from the air and supplies renewable Candy Sprouts for Island-Hop Fizz.
 *
 * <p>The complete eleven-block envelope remains in its generating chunk. It
 * creates no entities, block entities or inventories and has no repair pass,
 * so harvested sprouts and player edits remain ordinary saved-world
 * changes.</p>
 */
public final class JellybeanCompassPicnicFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("jellybean_compass_picnic");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MAX_TERRAIN_RELIEF = 3;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 1978082;
	public static final JellybeanCompassPicnicFeature FEATURE =
			new JellybeanCompassPicnicFeature();
	private static final ResourceKey<Biome> ARCHIPELAGO_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.JELLYBEAN_ARCHIPELAGO.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] SPROUTS = {
			{-4, -3}, {-4, -1}, {-4, 1}, {-4, 3},
			{4, -3}, {4, -1}, {4, 1}, {4, 3},
			{-3, -4}, {-1, -4}, {1, -4}, {3, -4},
			{-3, 4}, {-1, 4}, {1, 4}, {3, 4}
	};
	private static final int[][] LANTERNS = {
			{-3, -3}, {3, -3}, {-3, 3}, {3, 3}
	};
	private static Holder<PlacedFeature> placedFeature;

	private JellybeanCompassPicnicFeature() {
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
								JellybeanCompassPicnicFeature>(
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
		if (!world.getBiome(origin).is(ARCHIPELAGO_KEY)) {
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
					if (!world.getBiome(around)
							.is(ARCHIPELAGO_KEY)) {
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
		buildCompassFloor(world, centre, rotation);
		buildSprouts(world, centre, rotation);
		buildLanterns(world, centre, rotation);
		buildPicnicCentre(world, centre, rotation);
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
						y <= centre.getY() + 4; y++) {
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

	public static int[][] sprouts() {
		return SPROUTS;
	}

	private static void buildCompassFloor(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState state = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
								.defaultBlockState()
						: flavourFloor(x, z);
				BlockPos floor =
						local(centre, rotation, x, 1, z);
				world.setBlock(floor, state, 2);
				supportDown(world, floor.below());
			}
		}
	}

	private static BlockState flavourFloor(int x, int z) {
		if (x < 0 && z < 0) {
			return CakeWorldBlocks.GUMMY_BLOCK.get()
					.defaultBlockState();
		}
		if (x > 0 && z < 0) {
			return CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
					.defaultBlockState();
		}
		if (x < 0) {
			return CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get()
					.defaultBlockState();
		}
		return CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
				.defaultBlockState();
	}

	private static void buildSprouts(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState mature = CakeWorldBlocks.CANDY_SPROUT.get()
				.defaultBlockState()
				.setValue(CropBlock.AGE, CropBlock.MAX_AGE);
		for (int[] sprout : SPROUTS) {
			set(world, centre, rotation,
					sprout[0], 2, sprout[1], mature);
		}
	}

	private static void buildLanterns(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState pillar = CakeWorldBlocks.CANDY_CANE_PILLAR
				.get().defaultBlockState();
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
		for (int i = 0; i < LANTERNS.length; i++) {
			int[] lantern = LANTERNS[i];
			set(world, centre, rotation,
					lantern[0], 2, lantern[1], pillar);
			set(world, centre, rotation,
					lantern[0], 3, lantern[1], pillar);
			set(world, centre, rotation,
					lantern[0], 4, lantern[1], caps[i]);
		}
	}

	private static void buildPicnicCentre(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		for (int[] marker : new int[][] {
			{-2, 0}, {2, 0}, {0, -2}, {0, 2}
		}) {
			set(world, centre, rotation,
					marker[0], 2, marker[1], glass);
		}
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK.get()
				.defaultBlockState();
		for (int[] seat : new int[][] {
			{-1, -1}, {1, -1}, {-1, 1}, {1, 1}
		}) {
			set(world, centre, rotation,
					seat[0], 2, seat[1], wafer);
		}
		set(world, centre, rotation, 0, 2, 0,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
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
						y <= centre.getY() + 4; y++) {
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
		return state.is(CakeWorldBlocks.GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(BlockTags.BASE_STONE_OVERWORLD)
				|| state.is(BlockTags.DIRT)
				|| state.is(BlockTags.SAND)
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
