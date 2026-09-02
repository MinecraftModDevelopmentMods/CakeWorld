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
 * A lit, forgiving example crossing over a contained Treacle flat.
 *
 * <p>The complete plan fits one chunk, uses a sealed support shelf, creates no
 * entity, block entity or inventory, and has no repair pass. Two Marshmallow
 * ends make the bridge easy to spot and safe to reach.</p>
 */
public final class WispLightCausewayFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID = id("wisp_light_causeway");
	public static final int MIN_Y = 24;
	public static final int MAX_Y = 112;
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 2;
	public static final int MIN_NATURAL_SUPPORTS = 33;
	public static final int PLACEMENT_SALT = 1618033;
	public static final WispLightCausewayFeature FEATURE =
			new WispLightCausewayFeature();
	private static final ResourceKey<Biome> VALLEY_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.TREACLE_SOUL_VALLEYS.getId());
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private WispLightCausewayFeature() {
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
								WispLightCausewayFeature>(
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
					if (!world.getBiome(centre).is(VALLEY_KEY)) {
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
		buildSupport(world, centre, rotation);
		buildFlatAndBridge(world, centre, rotation);
		buildWispLights(world, centre, rotation);
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
		int naturalSupports = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 2; y++) {
					if (!canOccupy(world,
							local(centre, rotation, x, y, z))) {
						return false;
					}
				}
				BlockPos support = local(
						centre, rotation, x, -1, z);
				if (!canSupportOrFill(world, support)) {
					return false;
				}
				if (isNaturalSupport(world, support)) {
					naturalSupports++;
				}
			}
		}
		return naturalSupports >= MIN_NATURAL_SUPPORTS;
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

	private static void buildSupport(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState support = CakeWorldBlocks.FUDGE_ROCK.get()
				.defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				set(world, centre, rotation, x, -1, z, support);
			}
		}
	}

	private static void buildFlatAndBridge(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState crust = CakeWorldBlocks.TREACLE_SOUL_CRUST.get()
				.defaultBlockState();
		BlockState syrup = CakeWorldFluids.SYRUP_BLOCK.get()
				.defaultBlockState();
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState state = Math.abs(x) == 4 || Math.abs(z) == 4
						? crust : syrup;
				set(world, centre, rotation, x, 0, z, state);
			}
		}
		for (int z = -4; z <= 4; z++) {
			BlockState bridge = Math.abs(z) == 4
					? CakeWorldBlocks.MARSHMALLOW.get()
							.defaultBlockState()
					: CakeWorldBlocks.WAFER_BLOCK.get()
							.defaultBlockState();
			set(world, centre, rotation, 0, 0, z, bridge);
		}
	}

	private static void buildWispLights(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		for (int z : new int[] {-3, -1, 1, 3}) {
			for (int x : new int[] {-1, 1}) {
				set(world, centre, rotation, x, 1, z, glass);
			}
		}
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-3, 3}) {
				set(world, centre, rotation, x, 1, z,
						CakeWorldBlocks.CANDY_CANE_PILLAR.get()
								.defaultBlockState());
				set(world, centre, rotation, x, 2, z,
						CakeWorldBlocks.MINT_CRYSTAL.get()
								.defaultBlockState());
			}
		}
		set(world, centre, rotation, -2, 1, -4,
				CakeWorldBlocks.COOLING_RACK.get().defaultBlockState());
		set(world, centre, rotation, 2, 1, -4,
				CakeWorldBlocks.MIXING_BOWL.get().defaultBlockState());
	}

	private static boolean canOccupy(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.hasBlockEntity()
				&& (state.isAir() || state.getMaterial().isReplaceable());
	}

	private static boolean canSupportOrFill(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.hasBlockEntity()
				&& (state.isAir() || state.getMaterial().isReplaceable()
						|| state.is(CakeWorldBlocks.FUDGE_ROCK.get())
						|| state.is(CakeWorldBlocks.TREACLE_SOUL_CRUST.get()));
	}

	private static boolean isNaturalSupport(
			WorldGenLevel world, BlockPos position) {
		BlockState state = world.getBlockState(position);
		return world.getFluidState(position).isEmpty()
				&& !state.hasBlockEntity()
				&& (state.is(CakeWorldBlocks.FUDGE_ROCK.get())
						|| state.is(CakeWorldBlocks.TREACLE_SOUL_CRUST.get()));
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
