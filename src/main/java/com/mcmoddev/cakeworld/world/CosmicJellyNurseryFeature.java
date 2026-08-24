package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;

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
 * A sealed, luminous Lemonade nursery surrounded by elastic Cosmic Jelly.
 *
 * <p>The seven-by-seven plan and nine-by-nine safety envelope stay inside one
 * generating chunk. It creates a dark, water-tagged habitat for Glow Jellies
 * without directly spawning an entity, owning inventory, persisting a marker,
 * repairing an existing world or replaying on reload.</p>
 */
public final class CosmicJellyNurseryFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID = id("cosmic_jelly_nursery");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MAX_TERRAIN_RELIEF = 6;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 2718281;
	public static final CosmicJellyNurseryFeature FEATURE =
			new CosmicJellyNurseryFeature();
	private static final ResourceKey<Biome> REEFS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.COSMIC_JELLY_REEFS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private CosmicJellyNurseryFeature() {
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
								CosmicJellyNurseryFeature>(
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
		if (!world.getBiome(origin).is(REEFS_KEY)) {
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
					if (!world.getBiome(around).is(REEFS_KEY)) {
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
		buildNursery(world, centre, rotation);
		buildOuterPads(world, centre, rotation);
		set(world, centre, rotation, -1, 1, 3,
				CakeWorldBlocks.MIXING_BOWL.get().defaultBlockState());
		set(world, centre, rotation, 1, 1, 3,
				CakeWorldBlocks.COOLING_RACK.get().defaultBlockState());
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
				for (int y = surfaceY + 1; y <= centre.getY() + 6; y++) {
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

	private static void buildNursery(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState jelly = CakeWorldBlocks.COSMIC_JELLY.get()
				.defaultBlockState();
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		BlockState lemonade = CakeWorldFluids.LEMONADE_BLOCK.get()
				.defaultBlockState();
		for (int y = 1; y <= 3; y++) {
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					if (Math.abs(x) == 2 || Math.abs(z) == 2) {
						set(world, centre, rotation, x, y, z, jelly);
					}
				}
			}
		}
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				set(world, centre, rotation, x, 4, z, glass);
			}
		}
		for (int y = 1; y <= 3; y++) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					set(world, centre, rotation, x, y, z, lemonade);
				}
			}
		}
		BlockState rod = Blocks.END_ROD.defaultBlockState()
				.setValue(EndRodBlock.FACING, net.minecraft.core.Direction.UP);
		for (int x : new int[] {-2, 2}) {
			for (int z : new int[] {-2, 2}) {
				set(world, centre, rotation, x, 5, z, rod);
			}
		}
	}

	private static void buildOuterPads(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState jelly = CakeWorldBlocks.COSMIC_JELLY.get()
				.defaultBlockState();
		for (int[] pad : new int[][] {
				{0, -3}, {3, 0}, {0, 3}, {-3, 0}}) {
			set(world, centre, rotation, pad[0], 1, pad[1], jelly);
		}
		set(world, centre, rotation, -3, 1, -3,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
						.defaultBlockState());
		set(world, centre, rotation, 3, 1, -3,
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get()
						.defaultBlockState());
		set(world, centre, rotation, 3, 1, 3,
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
						.defaultBlockState());
		set(world, centre, rotation, -3, 1, 3,
				CakeWorldBlocks.GUMMY_BLOCK.get().defaultBlockState());
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
				for (int y = surfaceY + 1; y <= centre.getY() + 6; y++) {
					BlockPos position = new BlockPos(horizontal.getX(), y,
							horizontal.getZ());
					if (canClear(world.getBlockState(position))) {
						world.setBlock(position,
								Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}
	}

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.COSMIC_JELLY.get())
				|| state.is(CakeWorldBlocks.MACARON_SHELL.get())
				|| state.is(CakeWorldBlocks.MERINGUE_FOAM.get())
				|| state.is(CakeWorldBlocks.CANDYFLOSS_CLOUD.get())
				|| state.is(CakeWorldBlocks.MOONCAKE_CRUST.get())
				|| state.is(CakeWorldBlocks.STARLIGHT_SUGAR_GRASS.get())
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
