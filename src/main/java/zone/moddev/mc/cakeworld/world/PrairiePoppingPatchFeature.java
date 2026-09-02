package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.PoppingKernelStalkBlock;
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
 * A renewable kernel field framed by an exceptionally light popcorn arch.
 *
 * <p>The authored eleven-block envelope stays inside its generating chunk,
 * creates no entities or block entities, and has no repair pass. Harvests and
 * player alterations therefore remain ordinary saved-world changes.</p>
 */
public final class PrairiePoppingPatchFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("prairie_popping_patch");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MAX_TERRAIN_RELIEF = 3;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 1978079;
	public static final PrairiePoppingPatchFeature FEATURE =
			new PrairiePoppingPatchFeature();
	private static final ResourceKey<Biome> PRAIRIE_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.POPCORN_PRAIRIE.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] STALKS = {
			{-4, -3}, {-3, -4}, {-3, -2},
			{-2, -3}, {-4, -2}, {-2, -4},
			{4, -3}, {3, -4}, {3, -2},
			{2, -3}, {4, -2}, {2, -4},
			{-4, 3}, {-3, 4}, {-3, 2},
			{-2, 3}, {-4, 2}, {-2, 4},
			{4, 3}, {3, 4}, {3, 2},
			{2, 3}, {4, 2}, {2, 4}
	};
	private static final int[][] PUFF_STACKS = {
			{-4, -4}, {4, -4}, {-4, 4}, {4, 4}
	};
	private static Holder<PlacedFeature> placedFeature;

	private PrairiePoppingPatchFeature() {
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
								PrairiePoppingPatchFeature>(
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
		if (!world.getBiome(origin).is(PRAIRIE_KEY)) {
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
					if (!world.getBiome(around).is(PRAIRIE_KEY)) {
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
		buildFloor(world, centre, rotation);
		buildKernelRows(world, centre, rotation);
		buildPopcornArch(world, centre, rotation);
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

	public static int[][] stalks() {
		return STALKS;
	}

	private static void buildFloor(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState state = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
								.defaultBlockState()
						: CakeWorldBlocks.POPPED_CORN_TURF.get()
								.defaultBlockState();
				BlockPos floor =
						local(centre, rotation, x, 1, z);
				world.setBlock(floor, state, 2);
				supportDown(world, floor.below());
			}
		}
	}

	private static void buildKernelRows(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		PoppingKernelStalkBlock crop =
				(PoppingKernelStalkBlock) CakeWorldBlocks
						.POPPING_KERNEL_STALK.get();
		BlockState mature = crop.getStateForAge(crop.getMaxAge());
		for (int[] stalk : STALKS) {
			set(world, centre, rotation,
					stalk[0], 2, stalk[1], mature);
		}
	}

	private static void buildPopcornArch(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState popcorn = CakeWorldBlocks.POPCORN_BLOCK.get()
				.defaultBlockState();
		for (int[] stack : PUFF_STACKS) {
			set(world, centre, rotation,
					stack[0], 2, stack[1], popcorn);
			set(world, centre, rotation,
					stack[0], 3, stack[1], popcorn);
		}
		for (int x : new int[] {-2, 2}) {
			set(world, centre, rotation, x, 2, 0, popcorn);
			set(world, centre, rotation, x, 3, 0, popcorn);
		}
		for (int x = -2; x <= 2; x++) {
			set(world, centre, rotation, x, 4, 0, popcorn);
		}
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
		return state.is(CakeWorldBlocks.POPPED_CORN_TURF.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())
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
