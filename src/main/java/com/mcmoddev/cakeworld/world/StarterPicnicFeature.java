package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
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
 * A tiny, self-contained starter hamlet rather than a full village replacement.
 * It gives the Cookbook Kiosk a readable home while STRUCT-001 remains a later,
 * considerably larger settlement system.
 */
public final class StarterPicnicFeature extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			new ResourceLocation(CakeWorld.MODID, "first_bite_picnic");
	public static final StarterPicnicFeature FEATURE = new StarterPicnicFeature();
	private static Holder<PlacedFeature> placedFeature;

	private StarterPicnicFeature() {
		super(NoneFeatureConfiguration.CODEC);
		setRegistryName(ID);
	}

	public static void registerConfiguredFeature() {
		Holder<ConfiguredFeature<?, ?>> configured = BuiltinRegistries.register(
				BuiltinRegistries.CONFIGURED_FEATURE, ID,
				new ConfiguredFeature<NoneFeatureConfiguration, StarterPicnicFeature>(
						FEATURE, NoneFeatureConfiguration.INSTANCE));
		placedFeature = BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE,
				ID, new PlacedFeature(configured, List.of(
						RarityFilter.onAverageOnceEvery(24),
						InSquarePlacement.spread(),
						HeightmapPlacement.onHeightmap(
								Heightmap.Types.WORLD_SURFACE_WG),
						BiomeFilter.biome())));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos origin = context.origin();
		int surfaceY = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG,
				origin.getX(), origin.getZ()) - 1;
		return buildAt(world, context.random(),
				new BlockPos(origin.getX(), surfaceY, origin.getZ()));
	}

	/**
	 * Deterministic construction seam used by world generation and GameTests.
	 * The supplied position is the centre floor block, not the air above it.
	 */
	public static boolean buildAt(WorldGenLevel world, Random random,
			BlockPos floorCentre) {
		if (!hasSafeFootprint(world, floorCentre)) {
			return false;
		}

		BlockState biscuit = CakeWorldBlocks.BISCUIT_STONE.get().defaultBlockState();
		BlockState crumbs = CakeWorldBlocks.BISCUIT_CRUMBS.get().defaultBlockState();
		BlockState sponge = CakeWorldBlocks.CHOCOLATE_SPONGE.get().defaultBlockState();
		BlockState icing = CakeWorldBlocks.ICING.get().defaultBlockState();
		BlockState icingLayer = CakeWorldBlocks.ICING_LAYER.get().defaultBlockState();
		BlockState kiosk = CakeWorldBlocks.COOKBOOK_KIOSK.get().defaultBlockState();

		// A nine-block picnic square with a clear biscuit path.
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState floor = Math.abs(x) == 4 || Math.abs(z) == 4
						? sponge : biscuit;
				if (x == 0 && z >= 0) {
					floor = crumbs;
				}
				world.setBlock(floorCentre.offset(x, 0, z), floor, 2);
			}
		}

		// The guide kiosk and two recognisable, cupcake-like picnic shelters.
		world.setBlock(floorCentre.above(), kiosk, 2);
		buildShelter(world, floorCentre.offset(-2, 1, -2), biscuit, icing);
		buildShelter(world, floorCentre.offset(2, 1, -2), biscuit, icing);

		// Four soft seats and a dusting of icing make the landmark safe and
		// readable from the approach without requiring signs or quest markers.
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos seat = floorCentre.relative(direction, 2).above();
			world.setBlock(seat, sponge, 2);
			world.setBlock(seat.above(), icingLayer, 2);
		}
		if (random.nextBoolean()) {
			world.setBlock(floorCentre.offset(-3, 1, 3), icingLayer, 2);
		} else {
			world.setBlock(floorCentre.offset(3, 1, 3), icingLayer, 2);
		}
		return true;
	}

	private static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos floorCentre) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos ground = floorCentre.offset(x, 0, z);
				BlockState groundState = world.getBlockState(ground);
				if (!groundState.isFaceSturdy(world, ground, Direction.UP)
						|| !world.getFluidState(ground).isEmpty()) {
					return false;
				}
				for (int y = 1; y <= 4; y++) {
					BlockState state = world.getBlockState(ground.above(y));
					if (!state.isAir() && !state.getMaterial().isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static void buildShelter(WorldGenLevel world, BlockPos centre,
			BlockState post, BlockState roof) {
		for (int x = -1; x <= 1; x += 2) {
			for (int z = -1; z <= 1; z += 2) {
				world.setBlock(centre.offset(x, 0, z), post, 2);
				world.setBlock(centre.offset(x, 1, z), post, 2);
			}
		}
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				world.setBlock(centre.offset(x, 2, z), roof, 2);
			}
		}
		world.setBlock(centre, Blocks.AIR.defaultBlockState(), 2);
	}
}
