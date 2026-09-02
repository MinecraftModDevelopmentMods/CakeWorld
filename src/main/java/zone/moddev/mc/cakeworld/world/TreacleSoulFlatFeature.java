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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Small, contained Treacle flats that make the valley floor worth bridging.
 *
 * <p>Every successful patch has a five-by-five solid support shelf, a
 * Treacle-Soul-Crust rim and nine source blocks. It crosses no chunk boundary,
 * rejects fluids, block entities and non-terrain solids, and has no repair or
 * replay path.</p>
 */
public final class TreacleSoulFlatFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID = id("treacle_soul_flat");
	public static final int ATTEMPTS_PER_CHUNK = 4;
	public static final int MIN_Y = 24;
	public static final int MAX_Y = 112;
	public static final TreacleSoulFlatFeature FEATURE =
			new TreacleSoulFlatFeature();
	private static final ResourceKey<Biome> VALLEY_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.TREACLE_SOUL_VALLEYS.getId());
	private static Holder<PlacedFeature> placedFeature;

	private TreacleSoulFlatFeature() {
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
								TreacleSoulFlatFeature>(
									FEATURE,
									NoneFeatureConfiguration.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						CountPlacement.of(ATTEMPTS_PER_CHUNK),
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
		BlockPos origin = context.origin();
		ChunkPos chunk = new ChunkPos(origin);
		int minY = Math.max(world.getMinBuildHeight() + 2, MIN_Y);
		int maxY = Math.min(world.getMaxBuildHeight() - 2, MAX_Y);
		int startY = Math.max(minY, Math.min(maxY, origin.getY()));
		for (int offset = 0; offset <= maxY - minY; offset++) {
			for (int direction : offset == 0
					? new int[] {0} : new int[] {-1, 1}) {
				int y = startY + offset * direction;
				if (y < minY || y > maxY) {
					continue;
				}
				BlockPos centre = new BlockPos(
						origin.getX(), y, origin.getZ());
				if (world.getBiome(centre).is(VALLEY_KEY)
						&& fitsWithinChunk(centre, chunk)
						&& buildAt(world, centre)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world, BlockPos centre) {
		if (!hasSafeSite(world, centre)) {
			return false;
		}
		BlockState rim = CakeWorldBlocks.TREACLE_SOUL_CRUST.get()
				.defaultBlockState();
		BlockState syrup = CakeWorldFluids.SYRUP_BLOCK.get()
				.defaultBlockState();
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				world.setBlock(centre.offset(x, 0, z),
						Math.abs(x) == 2 || Math.abs(z) == 2
								? rim : syrup,
						2);
			}
		}
		return true;
	}

	public static boolean hasSafeSite(
			WorldGenLevel world, BlockPos centre) {
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				BlockPos position = centre.offset(x, 0, z);
				if (!canOccupy(world, position)
						|| !isNaturalSupport(
								world, position.below())) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean fitsWithinChunk(
			BlockPos centre, ChunkPos chunk) {
		for (int x : new int[] {-2, 2}) {
			for (int z : new int[] {-2, 2}) {
				BlockPos corner = centre.offset(x, 0, z);
				if (Math.floorDiv(corner.getX(), 16) != chunk.x
						|| Math.floorDiv(corner.getZ(), 16) != chunk.z) {
					return false;
				}
			}
		}
		return true;
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
				&& !state.hasBlockEntity()
				&& (state.is(CakeWorldBlocks.TREACLE_SOUL_CRUST.get())
						|| state.is(CakeWorldBlocks.FUDGE_ROCK.get()));
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
