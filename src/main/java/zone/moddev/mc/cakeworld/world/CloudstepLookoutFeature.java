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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndRodBlock;
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
 * A compact pink-cloud lookout with a bright candy-cane mast, four glide
 * markers and soft launch edges. The seven-by-seven authored plan and its
 * nine-by-nine safety envelope remain inside the generating chunk.
 */
public final class CloudstepLookoutFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID = id("cloudstep_lookout");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MAX_TERRAIN_RELIEF = 6;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 2718281;
	public static final CloudstepLookoutFeature FEATURE =
			new CloudstepLookoutFeature();
	private static final ResourceKey<Biome> CLOUDBANKS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.CANDYFLOSS_CLOUDBANKS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] FLAGS = {
			{-3, 0}, {3, 0}, {0, -3}, {0, 3}
	};
	private static Holder<PlacedFeature> placedFeature;

	private CloudstepLookoutFeature() {
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
								CloudstepLookoutFeature>(
								FEATURE,
								NoneFeatureConfiguration.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						RarityFilter.onAverageOnceEvery(
								AVERAGE_CHUNKS_PER_ATTEMPT),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(
								Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
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
				context.origin().getX(), context.origin().getZ());
		if (!world.getBiome(origin).is(CLOUDBANKS_KEY)) {
			return false;
		}
		ChunkPos placementChunk = new ChunkPos(context.origin());
		for (int radius = 0; radius <= SAFE_SITE_SEARCH_RADIUS; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (radius > 0 && Math.abs(x) != radius
							&& Math.abs(z) != radius) {
						continue;
					}
					BlockPos around = surfaceAt(world,
							origin.getX() + x, origin.getZ() + z);
					if (!world.getBiome(around).is(CLOUDBANKS_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(around.getX(),
							highestSurfaceY(world, around), around.getZ());
					Rotation rotation = orientation(world.getSeed(), centre);
					if (fitsWithinChunk(centre, rotation, placementChunk)
							&& buildAt(world, centre, rotation)) {
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
		clearReplaceableDecoration(world, centre, rotation);
		buildFoundation(world, centre, rotation);
		buildLaunchDeck(world, centre, rotation);
		buildMast(world, centre, rotation);
		set(world, centre, rotation, -2, 2, -1,
				CakeWorldBlocks.COOLING_RACK.get().defaultBlockState());
		set(world, centre, rotation, 2, 2, 1,
				CakeWorldBlocks.MIXING_BOWL.get().defaultBlockState());
		return true;
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		return footprintProblem(world, centre, rotation) == null;
	}

	public static String footprintProblem(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos horizontal = local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceYAtOrBelow(world,
						horizontal.getX(), horizontal.getZ(), centre.getY());
				if (surfaceY > centre.getY()
						|| centre.getY() - surfaceY > MAX_TERRAIN_RELIEF) {
					return "relief at " + horizontal + ": surfaceY="
							+ surfaceY + ", centreY=" + centre.getY();
				}
				BlockPos surface = new BlockPos(horizontal.getX(),
						surfaceY, horizontal.getZ());
				BlockState ground = world.getBlockState(surface);
				if (!world.getFluidState(surface).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return "ground at " + surface + ": state=" + ground
							+ ", fluid=" + world.getFluidState(surface)
							+ ", blockEntity=" + ground.hasBlockEntity();
				}
				for (int y = surfaceY + 1; y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(horizontal.getX(), y,
							horizontal.getZ());
					BlockState state = world.getBlockState(position);
					if (!world.getFluidState(position).isEmpty()
							|| state.hasBlockEntity() || !canClear(state)) {
						return "obstacle at " + position + ": state=" + state
								+ ", fluid=" + world.getFluidState(position)
								+ ", blockEntity=" + state.hasBlockEntity();
					}
				}
			}
		}
		return null;
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

	public static int[][] flags() {
		return FLAGS;
	}

	private static void buildFoundation(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState bricks = CakeWorldBlocks.MERINGUE_BRICKS.get()
				.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				BlockPos foundation = local(centre, rotation, x, 0, z);
				world.setBlock(foundation, bricks, 2);
				supportDown(world, foundation.below(), bricks);
			}
		}
	}

	private static void buildLaunchDeck(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState cloud = CakeWorldBlocks.CANDYFLOSS_CLOUD.get()
				.defaultBlockState();
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK.get()
				.defaultBlockState();
		BlockState foam = CakeWorldBlocks.MERINGUE_FOAM.get()
				.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				BlockState floor;
				if (Math.abs(x) == 3 || Math.abs(z) == 3) {
					floor = cloud;
				} else if (x == 0 || z == 0) {
					floor = wafer;
				} else {
					floor = foam;
				}
				set(world, centre, rotation, x, 1, z, floor);
			}
		}
	}

	private static void buildMast(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState pillar = CakeWorldBlocks.CANDY_CANE_PILLAR.get()
				.defaultBlockState();
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK.get()
				.defaultBlockState();
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		BlockState endRod = Blocks.END_ROD.defaultBlockState()
				.setValue(EndRodBlock.FACING, Direction.UP);
		for (int y = 2; y <= 5; y++) {
			set(world, centre, rotation, 0, y, 0, pillar);
		}
		for (int distance : new int[] {-2, -1, 1, 2}) {
			set(world, centre, rotation, distance, 4, 0, wafer);
		}
		for (int[] flag : FLAGS) {
			set(world, centre, rotation, flag[0], 4, flag[1], glass);
			set(world, centre, rotation, flag[0], 5, flag[1], endRod);
		}
	}

	private static void supportDown(WorldGenLevel world,
			BlockPos start, BlockState support) {
		BlockPos.MutableBlockPos cursor = start.mutable();
		while (cursor.getY() > world.getMinBuildHeight()
				&& canClear(world.getBlockState(cursor))) {
			world.setBlock(cursor, support, 2);
			cursor.move(0, -1, 0);
		}
	}

	private static void clearReplaceableDecoration(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos horizontal = local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceYAtOrBelow(world,
						horizontal.getX(), horizontal.getZ(), centre.getY());
				for (int y = surfaceY + 1; y <= centre.getY() + 5; y++) {
					BlockPos position = new BlockPos(horizontal.getX(), y,
							horizontal.getZ());
					if (canClear(world.getBlockState(position))) {
						world.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}
	}

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.CANDYFLOSS_CLOUD.get())
				|| state.is(CakeWorldBlocks.MERINGUE_FOAM.get())
				|| state.is(CakeWorldBlocks.MARSHMALLOW.get())
				|| state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(CakeWorldBlocks.WAFER_ROCK.get())
				|| state.is(CakeWorldBlocks.NOUGAT_ROCK.get())
				|| state.is(CakeWorldBlocks.ROCK_CANDY.get())
				|| state.is(Blocks.END_STONE)
				|| state.is(BlockTags.BASE_STONE_OVERWORLD);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(CakeWorldBlocks.ICING_LAYER.get());
	}

	private static int highestSurfaceY(WorldGenLevel world, BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				highest = Math.max(highest, terrainSurfaceY(world,
						around.getX() + x, around.getZ() + z));
			}
		}
		return highest;
	}

	private static BlockPos surfaceAt(WorldGenLevel world, int x, int z) {
		return new BlockPos(x, terrainSurfaceY(world, x, z), z);
	}

	private static int terrainSurfaceY(WorldGenLevel world, int x, int z) {
		int y = world.getHeight(
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, y, z);
		while (y > world.getMinBuildHeight()) {
			BlockState state = world.getBlockState(cursor);
			if (!world.getFluidState(cursor).isEmpty() || !canClear(state)) {
				break;
			}
			y--;
			cursor.setY(y);
		}
		return y;
	}

	private static int terrainSurfaceYAtOrBelow(WorldGenLevel world,
			int x, int z, int ceilingY) {
		int y = ceilingY;
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, y, z);
		while (y > world.getMinBuildHeight()) {
			BlockState state = world.getBlockState(cursor);
			if (!world.getFluidState(cursor).isEmpty() || !canClear(state)) {
				break;
			}
			y--;
			cursor.setY(y);
		}
		return y;
	}

	private static void set(WorldGenLevel world, BlockPos centre,
			Rotation rotation, int x, int y, int z, BlockState state) {
		world.setBlock(local(centre, rotation, x, y, z), state, 2);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
