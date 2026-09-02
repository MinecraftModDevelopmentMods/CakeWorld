package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
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

/** Small natural root tangles that can grow on CakeWorld Nether terrain. */
public final class BlackLiquoriceTanglePatchFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("black_liquorice_tangle_patch");
	public static final int ATTEMPTS_PER_CHUNK = 6;
	public static final int MIN_Y = 24;
	public static final int MAX_Y = 112;
	public static final BlackLiquoriceTanglePatchFeature FEATURE =
			new BlackLiquoriceTanglePatchFeature();
	private static final ResourceKey<Biome> LABYRINTH_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.BLACK_LIQUORICE_LABYRINTHS.getId());
	private static Holder<PlacedFeature> placedFeature;

	private BlackLiquoriceTanglePatchFeature() {
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
								BlackLiquoriceTanglePatchFeature>(
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
		if (!world.getBiome(origin).is(LABYRINTH_KEY)) {
			return false;
		}
		BlockPos centre = findFloor(world, origin);
		if (centre == null) {
			return false;
		}
		Random random = context.random();
		int radius = 1 + random.nextInt(2);
		int placed = 0;
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (x * x + z * z > radius * radius + 1
						|| random.nextFloat() > 0.8F) {
					continue;
				}
				BlockPos patch = findNearbyFloor(
						world, centre.offset(x, 0, z));
				if (patch != null
						&& world.getBiome(patch).is(LABYRINTH_KEY)) {
					world.setBlock(patch,
							CakeWorldBlocks.BLACK_LIQUORICE_TANGLE.get()
									.defaultBlockState(), 2);
					placed++;
				}
			}
		}
		return placed > 0;
	}

	private static BlockPos findFloor(
			WorldGenLevel world, BlockPos origin) {
		int minY = Math.max(world.getMinBuildHeight() + 1, MIN_Y);
		int maxY = Math.min(world.getMaxBuildHeight() - 1, MAX_Y);
		int startY = Math.max(minY, Math.min(maxY, origin.getY()));
		for (int offset = 0; offset <= maxY - minY; offset++) {
			for (int direction : offset == 0
					? new int[] {0} : new int[] {-1, 1}) {
				int y = startY + offset * direction;
				if (y < minY || y > maxY) {
					continue;
				}
				BlockPos candidate = new BlockPos(
						origin.getX(), y, origin.getZ());
				if (canGrowAt(world, candidate)) {
					return candidate;
				}
			}
		}
		return null;
	}

	private static BlockPos findNearbyFloor(
			WorldGenLevel world, BlockPos origin) {
		for (int offset : new int[] {0, -1, 1, -2, 2}) {
			BlockPos candidate = origin.offset(0, offset, 0);
			if (canGrowAt(world, candidate)) {
				return candidate;
			}
		}
		return null;
	}

	private static boolean canGrowAt(
			WorldGenLevel world, BlockPos position) {
		return world.getBlockState(position).isAir()
				&& world.getFluidState(position).isEmpty()
				&& (world.getBlockState(position.below())
						.is(CakeWorldBlocks.BLACK_LIQUORICE_STONE.get())
						|| world.getBlockState(position.below())
								.is(CakeWorldBlocks.FUDGE_ROCK.get()));
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
