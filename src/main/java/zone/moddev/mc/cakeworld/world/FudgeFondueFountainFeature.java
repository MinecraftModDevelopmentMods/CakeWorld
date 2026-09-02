package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
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
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

/**
 * A small Fudge-Wastes rest stop around a contained Hot-Fudge fondue bowl.
 *
 * <p>The four Marshmallow pads give a visibly soft escape from the real lava
 * hazard, while a Cooling Rack and Mixing Bowl teach the prepared-food loop.
 * The complete plan stays inside its generating chunk, creates no entities,
 * block entities or inventory, and has no repair pass, so later player edits
 * survive reload.</p>
 */
public final class FudgeFondueFountainFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("fudge_fondue_fountain");
	public static final int MIN_Y = 24;
	public static final int MAX_Y = 112;
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 6;
	public static final int MIN_SOLID_SUPPORTS = 25;
	public static final int PLACEMENT_SALT = 1978097;
	public static final FudgeFondueFountainFeature FEATURE =
			new FudgeFondueFountainFeature();
	private static final ResourceKey<Biome> WASTES_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.FUDGE_WASTES.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private FudgeFondueFountainFeature() {
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
								FudgeFondueFountainFeature>(
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
		int maxY = Math.min(world.getMaxBuildHeight() - 4, MAX_Y);
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
					if (!world.getBiome(centre).is(WASTES_KEY)) {
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
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (!hasSafeSite(world, centre, rotation)) {
			return false;
		}
		buildBowl(world, centre, rotation);
		buildMarkers(world, centre, rotation);
		buildKitchen(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeSite(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (centre.getY() < Math.max(
				world.getMinBuildHeight() + 2, MIN_Y)
				|| centre.getY() > Math.min(
						world.getMaxBuildHeight() - 4, MAX_Y)) {
			return false;
		}
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 3; y++) {
					if (!canOccupy(world,
							local(centre, rotation, x, y, z))) {
						return false;
					}
				}
			}
		}
		int solidSupports = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				if (isNaturalSupport(world,
						local(centre, rotation, x, -1, z))) {
					solidSupports++;
				}
			}
		}
		return solidSupports >= MIN_SOLID_SUPPORTS;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-3, 3}) {
				BlockPos corner = local(centre, rotation, x, 0, z);
				if (Math.floorDiv(corner.getX(), 16) != chunk.x
						|| Math.floorDiv(corner.getZ(), 16)
								!= chunk.z) {
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

	private static void buildBowl(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState bricks = CakeWorldBlocks.BURNT_TOFFEE_BRICKS.get()
				.defaultBlockState();
		BlockState marshmallow = CakeWorldBlocks.MARSHMALLOW.get()
				.defaultBlockState();
		BlockState fudge = CakeWorldFluids.HOT_FUDGE_BLOCK.get()
				.defaultBlockState();
		BlockState pillar = CakeWorldBlocks.BURNT_TOFFEE_PILLAR.get()
				.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				BlockState state;
				if (Math.abs(x) <= 1 && Math.abs(z) <= 1) {
					state = x == 0 && z == 0 ? pillar : fudge;
				} else if ((x == 0 && Math.abs(z) == 3)
						|| (z == 0 && Math.abs(x) == 3)) {
					state = marshmallow;
				} else {
					state = bricks;
				}
				set(world, centre, rotation, x, 0, z, state);
			}
		}
		set(world, centre, rotation, 0, 1, 0, pillar);
		set(world, centre, rotation, 0, 2, 0,
				CakeWorldBlocks.CANDY_GLASS.get()
						.defaultBlockState());
	}

	private static void buildMarkers(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-3, 3}) {
				set(world, centre, rotation, x, 1, z, glass);
				set(world, centre, rotation, x, 2, z, glass);
			}
		}
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK.get()
				.defaultBlockState();
		for (int x : new int[] {-2, 2}) {
			for (int z : new int[] {-2, 2}) {
				set(world, centre, rotation, x, 1, z, wafer);
			}
		}
	}

	private static void buildKitchen(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		set(world, centre, rotation, -1, 1, -3,
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		set(world, centre, rotation, 1, 1, -3,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
	}

	private static boolean canOccupy(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.hasBlockEntity()
				&& (state.isAir()
						|| state.getMaterial().isReplaceable());
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
