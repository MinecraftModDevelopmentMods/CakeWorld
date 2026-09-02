package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Native Burnt-Toffee columns that never leak vanilla basalt across biomes. */
public final class BurntToffeeColumnsFeature
		extends Feature<ColumnFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("burnt_toffee_columns");
	public static final ResourceLocation SMALL_ID =
			id("small_burnt_toffee_columns");
	public static final ResourceLocation LARGE_ID =
			id("large_burnt_toffee_columns");
	public static final ResourceLocation BURNT_TOFFEE_BLOBS_ID =
			id("burnt_toffee_blobs");
	public static final ResourceLocation BURNT_SUGAR_BLOBS_ID =
			id("burnt_sugar_blobs");
	public static final BurntToffeeColumnsFeature FEATURE =
			new BurntToffeeColumnsFeature();
	private static final ImmutableList<Block> CANNOT_PLACE_ON =
			ImmutableList.of(
					Blocks.LAVA,
					Blocks.BEDROCK,
					Blocks.MAGMA_BLOCK,
					Blocks.SOUL_SAND,
					Blocks.NETHER_BRICKS,
					Blocks.NETHER_BRICK_FENCE,
					Blocks.NETHER_BRICK_STAIRS,
					Blocks.NETHER_WART,
					Blocks.CHEST,
					Blocks.SPAWNER);
	private static Holder<PlacedFeature> smallPlacedFeature;
	private static Holder<PlacedFeature> largePlacedFeature;
	private static Holder<PlacedFeature> burntToffeeBlobsPlacedFeature;
	private static Holder<PlacedFeature> burntSugarBlobsPlacedFeature;

	private BurntToffeeColumnsFeature() {
		super(ColumnFeatureConfiguration.CODEC);
		setRegistryName(ID);
	}

	public static void registerConfiguredFeatures() {
		Holder<ConfiguredFeature<?, ?>> smallConfigured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						SMALL_ID,
						new ConfiguredFeature<>(FEATURE,
								new ColumnFeatureConfiguration(
										ConstantInt.of(1),
										UniformInt.of(1, 4))));
		Holder<ConfiguredFeature<?, ?>> largeConfigured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						LARGE_ID,
						new ConfiguredFeature<>(FEATURE,
								new ColumnFeatureConfiguration(
										UniformInt.of(2, 3),
										UniformInt.of(5, 10))));
		smallPlacedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				SMALL_ID,
				new PlacedFeature(smallConfigured, List.of(
						CountOnEveryLayerPlacement.of(4),
						BiomeFilter.biome())));
		largePlacedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				LARGE_ID,
				new PlacedFeature(largeConfigured, List.of(
						CountOnEveryLayerPlacement.of(2),
						BiomeFilter.biome())));
		burntToffeeBlobsPlacedFeature = registerBlob(
				BURNT_TOFFEE_BLOBS_ID,
				CakeWorldBlocks.BURNT_TOFFEE_PILLAR.get(), 75);
		burntSugarBlobsPlacedFeature = registerBlob(
				BURNT_SUGAR_BLOBS_ID,
				CakeWorldBlocks.BURNT_SUGAR_ROCK.get(), 25);
	}

	private static Holder<PlacedFeature> registerBlob(
			ResourceLocation id, Block output, int attempts) {
		Holder<ConfiguredFeature<?, ?>> configured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						id,
						new ConfiguredFeature<>(Feature.REPLACE_BLOBS,
								new ReplaceSphereConfiguration(
										CakeWorldBlocks.FUDGE_ROCK.get()
												.defaultBlockState(),
										output.defaultBlockState(),
										UniformInt.of(3, 7))));
		return BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				id,
				new PlacedFeature(configured, List.of(
						CountPlacement.of(attempts),
						InSquarePlacement.spread(),
						HeightRangePlacement.uniform(
								VerticalAnchor.bottom(),
								VerticalAnchor.top()),
						BiomeFilter.biome())));
	}

	public static Holder<PlacedFeature> smallPlacedFeature() {
		return smallPlacedFeature;
	}

	public static Holder<PlacedFeature> largePlacedFeature() {
		return largePlacedFeature;
	}

	public static Holder<PlacedFeature> burntToffeeBlobsPlacedFeature() {
		return burntToffeeBlobsPlacedFeature;
	}

	public static Holder<PlacedFeature> burntSugarBlobsPlacedFeature() {
		return burntSugarBlobsPlacedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<ColumnFeatureConfiguration> context) {
		int seaLevel = context.chunkGenerator().getSeaLevel();
		BlockPos origin = context.origin();
		WorldGenLevel world = context.level();
		Random random = context.random();
		ColumnFeatureConfiguration config = context.config();
		if (!canPlaceAt(world, seaLevel, origin.mutable())) {
			return false;
		}
		int height = config.height().sample(random);
		boolean clustered = random.nextFloat() < 0.9F;
		int reach = Math.min(height, clustered ? 5 : 8);
		int attempts = clustered ? 50 : 15;
		boolean placed = false;
		for (BlockPos candidate : BlockPos.randomBetweenClosed(
				random, attempts,
				origin.getX() - reach, origin.getY(),
				origin.getZ() - reach,
				origin.getX() + reach, origin.getY(),
				origin.getZ() + reach)) {
			int candidateHeight = height
					- candidate.distManhattan(origin);
			if (candidateHeight >= 0) {
				placed |= placeColumn(world, seaLevel, candidate,
						candidateHeight, config.reach().sample(random));
			}
		}
		return placed;
	}

	private static boolean placeColumn(LevelAccessor world,
			int seaLevel, BlockPos origin, int height, int reach) {
		boolean placed = false;
		for (BlockPos candidate : BlockPos.betweenClosed(
				origin.getX() - reach, origin.getY(),
				origin.getZ() - reach,
				origin.getX() + reach, origin.getY(),
				origin.getZ() + reach)) {
			int distance = candidate.distManhattan(origin);
			BlockPos start = isOpen(world, seaLevel, candidate)
					? findSurface(world, seaLevel,
							candidate.mutable(), distance)
					: findAir(world, candidate.mutable(), distance);
			if (start == null) {
				continue;
			}
			int remaining = height - distance / 2;
			for (BlockPos.MutableBlockPos cursor = start.mutable();
					remaining >= 0; remaining--) {
				if (isOpen(world, seaLevel, cursor)) {
					world.setBlock(cursor,
							CakeWorldBlocks.BURNT_TOFFEE_PILLAR.get()
									.defaultBlockState(), 2);
					cursor.move(Direction.UP);
					placed = true;
				} else {
					if (!world.getBlockState(cursor).is(
							CakeWorldBlocks.BURNT_TOFFEE_PILLAR.get())) {
						break;
					}
					cursor.move(Direction.UP);
				}
			}
		}
		return placed;
	}

	@Nullable
	private static BlockPos findSurface(LevelAccessor world,
			int seaLevel, BlockPos.MutableBlockPos cursor, int distance) {
		while (cursor.getY() > world.getMinBuildHeight() + 1
				&& distance > 0) {
			distance--;
			if (canPlaceAt(world, seaLevel, cursor)) {
				return cursor;
			}
			cursor.move(Direction.DOWN);
		}
		return null;
	}

	private static boolean canPlaceAt(LevelAccessor world,
			int seaLevel, BlockPos.MutableBlockPos cursor) {
		if (!isOpen(world, seaLevel, cursor)) {
			return false;
		}
		BlockState stateBelow = world.getBlockState(
				cursor.move(Direction.DOWN));
		cursor.move(Direction.UP);
		return !stateBelow.isAir()
				&& !CANNOT_PLACE_ON.contains(stateBelow.getBlock());
	}

	@Nullable
	private static BlockPos findAir(LevelAccessor world,
			BlockPos.MutableBlockPos cursor, int distance) {
		while (cursor.getY() < world.getMaxBuildHeight()
				&& distance > 0) {
			distance--;
			Block block = world.getBlockState(cursor).getBlock();
			if (CANNOT_PLACE_ON.contains(block)) {
				return null;
			}
			if (world.getBlockState(cursor).isAir()) {
				return cursor;
			}
			cursor.move(Direction.UP);
		}
		return null;
	}

	private static boolean isOpen(LevelAccessor world,
			int seaLevel, BlockPos position) {
		return world.getBlockState(position).isAir()
				|| !world.getFluidState(position).isEmpty()
						&& position.getY() <= seaLevel;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
