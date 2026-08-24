package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
	public static final int MAX_TERRAIN_RELIEF = 4;
	public static final int SAFE_SITE_SEARCH_RADIUS = 7;
	public static final StarterPicnicFeature FEATURE = new StarterPicnicFeature();
	private static final ResourceKey<Biome> CANDY_PLAINS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.CANDY_PLAINS.getId());
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
		BlockPos origin = surfaceAt(world,
				context.origin().getX(), context.origin().getZ());
		if (!world.getBiome(origin).is(CANDY_PLAINS_KEY)) {
			return false;
		}
		ChunkPos placementChunk = new ChunkPos(context.origin());
		for (int radius = 0;
				radius <= SAFE_SITE_SEARCH_RADIUS; radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (radius > 0
							&& Math.abs(x) != radius
							&& Math.abs(z) != radius) {
						continue;
					}
					BlockPos around = surfaceAt(world,
							origin.getX() + x,
							origin.getZ() + z);
					if (!world.getBiome(around)
							.is(CANDY_PLAINS_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							around.getX(),
							highestSurfaceY(world, around),
							around.getZ());
					if (fitsWithinChunk(centre, placementChunk)
							&& buildAt(world, context.random(),
									centre)) {
						return true;
					}
				}
			}
		}
		return false;
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
		clearInheritedVegetation(world, floorCentre);

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
				BlockPos floorBlock = floorCentre.offset(x, 0, z);
				world.setBlock(floorBlock, floor, 2);
				supportDown(world, floorBlock.below());
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
		spawnCompanion(world, random, floorCentre);
		return true;
	}

	private static void spawnCompanion(WorldGenLevel world, Random random,
			BlockPos floorCentre) {
		CustardCat cat = CakeWorldEntities.CUSTARD_CAT.get()
				.create(world.getLevel());
		if (cat == null) {
			return;
		}
		BlockPos shelter = floorCentre.offset(-2, 1, -2);
		cat.setPos(shelter.getX() + 0.5D, shelter.getY(),
				shelter.getZ() + 0.5D);
		cat.setCatType(random.nextInt(10));
		cat.setPersistenceRequired();
		cat.restrictTo(floorCentre, 12);
		world.addFreshEntity(cat);
	}

	private static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos floorCentre) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos horizontal = floorCentre.offset(x, 0, z);
				int surfaceY = terrainSurfaceYAtOrBelow(
						world, horizontal.getX(),
						horizontal.getZ(), floorCentre.getY());
				if (surfaceY > floorCentre.getY()
						|| floorCentre.getY() - surfaceY
								> MAX_TERRAIN_RELIEF) {
					return false;
				}
				BlockPos ground = new BlockPos(
						horizontal.getX(), surfaceY,
						horizontal.getZ());
				BlockState groundState = world.getBlockState(ground);
				if (!groundState.isFaceSturdy(world, ground, Direction.UP)
						|| !world.getFluidState(ground).isEmpty()
						|| groundState.hasBlockEntity()) {
					return false;
				}
				for (int y = surfaceY + 1;
						y <= floorCentre.getY() + 4; y++) {
					BlockState state = world.getBlockState(
							new BlockPos(horizontal.getX(), y,
									horizontal.getZ()));
					if (state.hasBlockEntity() || !canClear(state)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean fitsWithinChunk(
			BlockPos centre, ChunkPos chunk) {
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				BlockPos corner = centre.offset(x, 0, z);
				if (Math.floorDiv(corner.getX(), 16) != chunk.x
						|| Math.floorDiv(corner.getZ(), 16)
								!= chunk.z) {
					return false;
				}
			}
		}
		return true;
	}

	private static void supportDown(
			WorldGenLevel world, BlockPos start) {
		BlockPos.MutableBlockPos cursor = start.mutable();
		int remaining = MAX_TERRAIN_RELIEF;
		while (remaining-- > 0
				&& cursor.getY() > world.getMinBuildHeight()
				&& canClear(world.getBlockState(cursor))) {
			world.setBlock(cursor,
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState(), 2);
			cursor.move(0, -1, 0);
		}
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos horizontal = centre.offset(x, 0, z);
				int surfaceY = terrainSurfaceYAtOrBelow(
						world, horizontal.getX(),
						horizontal.getZ(), centre.getY());
				for (int y = surfaceY + 1;
						y <= centre.getY() + 4; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					if (canClear(world.getBlockState(position))) {
						world.setBlock(position,
								Blocks.AIR.defaultBlockState(), 2);
					}
				}
			}
		}
	}

	private static int highestSurfaceY(
			WorldGenLevel world, BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				highest = Math.max(highest,
						terrainSurfaceY(world,
								around.getX() + x,
								around.getZ() + z));
			}
		}
		return highest;
	}

	private static BlockPos surfaceAt(
			WorldGenLevel world, int x, int z) {
		return new BlockPos(x, terrainSurfaceY(world, x, z), z);
	}

	private static int terrainSurfaceY(
			WorldGenLevel world, int x, int z) {
		int y = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG,
				x, z) - 1;
		return terrainSurfaceYAtOrBelow(world, x, z, y);
	}

	private static int terrainSurfaceYAtOrBelow(
			WorldGenLevel world, int x, int z, int maximumY) {
		int y = maximumY;
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos(x, y, z);
		while (y > world.getMinBuildHeight()
				&& world.getFluidState(cursor).isEmpty()
				&& canClear(world.getBlockState(cursor))) {
			y--;
			cursor.setY(y);
		}
		return y;
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS)
				|| state.is(Blocks.SNOW)
				|| state.is(Blocks.POWDER_SNOW);
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
