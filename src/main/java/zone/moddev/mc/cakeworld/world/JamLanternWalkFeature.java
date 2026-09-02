package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.JamGlowVineBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * A compact, dry cave walk between two contained Jam tasting channels.
 *
 * <p>The fourteen authored Jam cells make the landmark useful and readable,
 * but they are never counted as OreSpawn deposit evidence. The feature does
 * not carve terrain, replace fluids or resources, create block entities,
 * spawn entities, hold inventory, or run a repair pass. Every planned cell is
 * validated at the final decoration step and remains inside its generating
 * chunk.</p>
 */
public final class JamLanternWalkFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("jam_lantern_walk");
	public static final int MIN_Y = -48;
	public static final int MAX_Y = 48;
	public static final int PLACEMENT_SALT = 1978091;
	public static final JamLanternWalkFeature FEATURE =
			new JamLanternWalkFeature();
	private static final ResourceKey<Biome> GROTTO_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.JAM_GROTTOES.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] VINE_COLUMNS = {
			{-3, -1}, {3, -1}, {-3, 1}, {3, 1}
	};
	private static Holder<PlacedFeature> placedFeature;

	private JamLanternWalkFeature() {
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
								JamLanternWalkFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						InSquarePlacement.spread(),
						HeightRangePlacement.uniform(
								VerticalAnchor.aboveBottom(16),
								VerticalAnchor.absolute(MAX_Y)))));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		ChunkPos chunk = new ChunkPos(context.origin());
		int minY = Math.max(world.getMinBuildHeight() + 6, MIN_Y);
		int maxY = Math.min(world.getMaxBuildHeight() - 6, MAX_Y);
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
					if (!world.getBiome(centre).is(GROTTO_KEY)) {
						continue;
					}
					Rotation rotation =
							orientation(world.getSeed(), centre);
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
		buildWalk(world, centre, rotation);
		buildChannels(world, centre, rotation);
		buildLanternVines(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeSite(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (centre.getY() < Math.max(
				world.getMinBuildHeight() + 6, MIN_Y)
				|| centre.getY() > Math.min(
						world.getMaxBuildHeight() - 6, MAX_Y)) {
			return false;
		}
		for (int x = -4; x <= 4; x++) {
			for (int z = -1; z <= 1; z++) {
				if (!canOccupy(world,
						local(centre, rotation, x, 0, z))) {
					return false;
				}
			}
		}
		for (int side : new int[] {-1, 1}) {
			int channelZ = side * 3;
			for (int x = -3; x <= 3; x++) {
				for (int[] point : new int[][] {
					{x, -1, channelZ},
					{x, 0, channelZ},
					{x, 0, side * 2},
					{x, 0, side * 4}
				}) {
					if (!canOccupy(world,
							local(centre, rotation,
									point[0], point[1],
									point[2]))) {
						return false;
					}
				}
			}
			for (int x : new int[] {-4, 4}) {
				if (!canOccupy(world,
						local(centre, rotation,
								x, 0, channelZ))) {
					return false;
				}
			}
		}
		for (int[] column : VINE_COLUMNS) {
			for (int y = 1; y <= 4; y++) {
				if (!canOccupy(world,
						local(centre, rotation,
								column[0], y,
								column[1]))) {
					return false;
				}
			}
		}
		for (int[] appliance : new int[][] {
			{0, 1, 0}, {0, 1, 1}
		}) {
			if (!canOccupy(world,
					local(centre, rotation,
							appliance[0], appliance[1],
							appliance[2]))) {
				return false;
			}
		}
		int openBelow = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -2; y <= -1; y++) {
					if (canOccupy(world,
							local(centre, rotation,
									x, y, z))) {
						openBelow++;
					}
				}
			}
		}
		int solidBoundary = 0;
		for (int y = -2; y <= 5; y++) {
			for (int x = -6; x <= 6; x++) {
				for (int z : new int[] {-5, 5}) {
					if (isNaturalBoundary(world,
							local(centre, rotation,
									x, y, z))) {
						solidBoundary++;
					}
				}
			}
			for (int z = -4; z <= 4; z++) {
				for (int x : new int[] {-6, 6}) {
					if (isNaturalBoundary(world,
							local(centre, rotation,
									x, y, z))) {
						solidBoundary++;
					}
				}
			}
		}
		return openBelow >= 20 && solidBoundary >= 8;
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

	public static int[][] vineColumns() {
		return VINE_COLUMNS;
	}

	private static void buildWalk(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z = -1; z <= 1; z++) {
				BlockState state = z == 0
						&& (x == -1 || x == 1)
								? CakeWorldBlocks.MARSHMALLOW.get()
										.defaultBlockState()
								: bricks;
				set(world, centre, rotation, x, 0, z, state);
			}
		}
		set(world, centre, rotation, 0, 1, 0,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, centre, rotation, 0, 1, 1,
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
	}

	private static void buildChannels(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		BlockState jam = CakeWorldFluids.JAM.get()
				.getSource(false).createLegacyBlock();
		for (int side : new int[] {-1, 1}) {
			int channelZ = side * 3;
			for (int x = -3; x <= 3; x++) {
				set(world, centre, rotation,
						x, -1, channelZ, bricks);
				set(world, centre, rotation,
						x, 0, side * 2, bricks);
				set(world, centre, rotation,
						x, 0, side * 4, bricks);
				set(world, centre, rotation,
						x, 0, channelZ, jam);
			}
			for (int x : new int[] {-4, 4}) {
				set(world, centre, rotation,
						x, 0, channelZ, bricks);
			}
		}
	}

	private static void buildLanternVines(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		BlockState ripe =
				CakeWorldBlocks.JAM_GLOW_VINE.get()
						.defaultBlockState()
						.setValue(JamGlowVineBlock.AGE, 3);
		for (int[] column : VINE_COLUMNS) {
			set(world, centre, rotation,
					column[0], 4, column[1],
					CakeWorldBlocks.CANDY_GLASS.get()
							.defaultBlockState());
			for (int y = 1; y <= 3; y++) {
				set(world, centre, rotation,
						column[0], y, column[1],
						ripe);
			}
		}
	}

	private static boolean canOccupy(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.hasBlockEntity()
				&& (state.isAir()
						|| state.getMaterial().isReplaceable());
	}

	private static boolean isNaturalBoundary(
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
		world.setBlock(local(centre, rotation, x, y, z),
				state, 2);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
