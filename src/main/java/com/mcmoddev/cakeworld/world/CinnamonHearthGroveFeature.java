package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
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
 * A bounded grove of four glowing spice trees around a safe teaching hearth.
 *
 * <p>The authored plan fits inside one chunk, refuses fluids, block entities
 * and solid obstacles, creates no entity or inventory, and has no repair pass.
 * A player edit therefore remains authoritative after reload.</p>
 */
public final class CinnamonHearthGroveFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID = id("cinnamon_hearth_grove");
	public static final int MIN_Y = 24;
	public static final int MAX_Y = 112;
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MIN_SOLID_SUPPORTS = 25;
	public static final int PLACEMENT_SALT = 3141592;
	public static final CinnamonHearthGroveFeature FEATURE =
			new CinnamonHearthGroveFeature();
	private static final ResourceKey<Biome> GROVE_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.CINNAMON_EMBER_GROVES.getId());
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private CinnamonHearthGroveFeature() {
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
								CinnamonHearthGroveFeature>(
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
		int maxY = Math.min(world.getMaxBuildHeight() - 7, MAX_Y);
		int[][] centres = {
			{chunk.getMinBlockX() + 7, chunk.getMinBlockZ() + 7},
			{chunk.getMinBlockX() + 8, chunk.getMinBlockZ() + 8},
			{chunk.getMinBlockX() + 7, chunk.getMinBlockZ() + 8},
			{chunk.getMinBlockX() + 8, chunk.getMinBlockZ() + 7}
		};
		int startY = Math.max(minY,
				Math.min(maxY, context.origin().getY()));
		for (int offset = 0; offset <= maxY - minY; offset++) {
			for (int direction : offset == 0
					? new int[] {0} : new int[] {-1, 1}) {
				int y = startY + offset * direction;
				if (y < minY || y > maxY) {
					continue;
				}
				for (int[] horizontal : centres) {
					BlockPos centre = new BlockPos(
							horizontal[0], y, horizontal[1]);
					if (!world.getBiome(centre).is(GROVE_KEY)) {
						continue;
					}
					Rotation rotation = orientation(world.getSeed(), centre);
					if (fitsWithinChunk(centre, rotation, chunk)
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
		if (!hasSafeSite(world, centre, rotation)) {
			return false;
		}
		buildCourt(world, centre, rotation);
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-3, 3}) {
				buildTree(world, centre, rotation, x, z);
			}
		}
		return true;
	}

	public static boolean hasSafeSite(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (centre.getY() < Math.max(
				world.getMinBuildHeight() + 2, MIN_Y)
				|| centre.getY() > Math.min(
						world.getMaxBuildHeight() - 7, MAX_Y)) {
			return false;
		}
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				if (!canOccupy(world,
						local(centre, rotation, x, 0, z))) {
					return false;
				}
			}
		}
		for (int[] detail : new int[][] {
			{0, 1, 0}, {-1, 1, -3}, {1, 1, -3}
		}) {
			if (!canOccupy(world, local(centre, rotation,
					detail[0], detail[1], detail[2]))) {
				return false;
			}
		}
		for (int treeX : new int[] {-3, 3}) {
			for (int treeZ : new int[] {-3, 3}) {
				for (int y = 1; y <= 4; y++) {
					if (!canOccupy(world, local(centre, rotation,
							treeX, y, treeZ))) {
						return false;
					}
				}
				for (int x = -1; x <= 1; x++) {
					for (int z = -1; z <= 1; z++) {
						if ((x != 0 || z != 0)
								&& !canOccupy(world,
										local(centre, rotation,
												treeX + x, 4,
												treeZ + z))) {
							return false;
						}
					}
				}
				for (int[] crown : new int[][] {
					{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}
				}) {
					if (!canOccupy(world, local(centre, rotation,
							treeX + crown[0], 5,
							treeZ + crown[1]))) {
						return false;
					}
				}
			}
		}
		int supports = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				if (isNaturalSupport(world,
						local(centre, rotation, x, -1, z))) {
					supports++;
				}
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

	private static void buildCourt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState crust = CakeWorldBlocks.CINNAMON_CRUST.get()
				.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				set(world, centre, rotation, x, 0, z, crust);
			}
		}
		BlockState bricks = CakeWorldBlocks.BURNT_TOFFEE_BRICKS.get()
				.defaultBlockState();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				set(world, centre, rotation, x, 0, z,
						x == 0 && z == 0
								? Blocks.MAGMA_BLOCK.defaultBlockState()
								: bricks);
			}
		}
		for (int[] exit : new int[][] {
			{0, -3}, {3, 0}, {0, 3}, {-3, 0}
		}) {
			set(world, centre, rotation, exit[0], 0, exit[1],
					CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState());
		}
		set(world, centre, rotation, 0, 1, 0,
				CakeWorldBlocks.CANDY_GLASS.get().defaultBlockState());
		set(world, centre, rotation, -1, 1, -3,
				CakeWorldBlocks.COOLING_RACK.get().defaultBlockState());
		set(world, centre, rotation, 1, 1, -3,
				CakeWorldBlocks.MIXING_BOWL.get().defaultBlockState());
	}

	private static void buildTree(WorldGenLevel world,
			BlockPos centre, Rotation rotation, int treeX, int treeZ) {
		BlockState log = CakeWorldBlocks.CINNAMON_LOG.get()
				.defaultBlockState();
		for (int y = 1; y <= 4; y++) {
			set(world, centre, rotation, treeX, y, treeZ, log);
		}
		BlockState leaves = CakeWorldBlocks.EMBER_SPICE_LEAVES.get()
				.defaultBlockState();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x != 0 || z != 0) {
					set(world, centre, rotation, treeX + x, 4,
							treeZ + z, leaves);
				}
			}
		}
		for (int[] crown : new int[][] {
			{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}
		}) {
			set(world, centre, rotation, treeX + crown[0], 5,
					treeZ + crown[1], leaves);
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
