package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.block.FondantChorusBloomBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Replaces vanilla Chorus plants with small renewable pastel sculptures. */
public final class FondantChorusSculptureFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			new ResourceLocation(CakeWorld.MODID,
					"fondant_chorus_sculptures");
	public static final FondantChorusSculptureFeature FEATURE =
			new FondantChorusSculptureFeature();
	private static Holder<PlacedFeature> placedFeature;

	private FondantChorusSculptureFeature() {
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
								FondantChorusSculptureFeature>(
								FEATURE,
								NoneFeatureConfiguration.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						CountPlacement.of(UniformInt.of(1, 4)),
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
		BlockPos origin = context.origin();
		WorldGenLevel world = context.level();
		if (!world.isEmptyBlock(origin)
				|| !world.getFluidState(origin).isEmpty()
				|| !world.getBlockState(origin.below()).is(
						CakeWorldBlocks.PASTEL_FONDANT.get())) {
			return false;
		}
		return generateSculpture(world, origin, context.random());
	}

	public static boolean generateSculpture(WorldGenLevel world,
			BlockPos origin, Random random) {
		int height = 3 + random.nextInt(3);
		for (int y = 0; y <= height; y++) {
			if (!world.isEmptyBlock(origin.above(y))
					|| !world.getFluidState(origin.above(y)).isEmpty()) {
				return false;
			}
		}
		BlockState uprightStem = CakeWorldBlocks.FONDANT_CHORUS_STEM.get()
				.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
		for (int y = 0; y < height; y++) {
			world.setBlock(origin.above(y), uprightStem, 2);
		}
		world.setBlock(origin.above(height), ripeBloom(), 2);

		int branches = 1 + random.nextInt(2);
		for (int index = 0; index < branches; index++) {
			int y = 1 + random.nextInt(Math.max(1, height - 1));
			Direction direction = Direction.Plane.HORIZONTAL
					.getRandomDirection(random);
			BlockPos stemPos = origin.above(y).relative(direction);
			BlockPos bloomPos = stemPos.relative(direction);
			if (!world.isEmptyBlock(stemPos)
					|| !world.isEmptyBlock(bloomPos)
					|| !world.getFluidState(stemPos).isEmpty()
					|| !world.getFluidState(bloomPos).isEmpty()) {
				continue;
			}
			world.setBlock(stemPos,
					uprightStem.setValue(RotatedPillarBlock.AXIS,
							direction.getAxis()), 2);
			world.setBlock(bloomPos, ripeBloom(), 2);
		}
		return true;
	}

	public static BlockState ripeBloom() {
		return CakeWorldBlocks.FONDANT_CHORUS_BLOOM.get()
				.defaultBlockState()
				.setValue(FondantChorusBloomBlock.AGE,
						FondantChorusBloomBlock.RIPE_AGE);
	}
}
