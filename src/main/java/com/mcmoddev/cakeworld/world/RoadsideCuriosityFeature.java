package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
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
 * Rare, non-locatable micro-scenes that make travelling feel authored without
 * turning every joke or shelter into a map target.
 *
 * <p>The first set deliberately covers only CakeWorld's three current land
 * biomes. Each attempt performs one bounded search for safe ground and then
 * builds a small biome-specific plan. Nothing is replayed after chunk
 * generation, so later player edits remain authoritative.</p>
 */
public final class RoadsideCuriosityFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("roadside_curiosity");
	public static final ResourceLocation LOOT_ID =
			id("chests/roadside_curiosity");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 48;
	public static final int SAFE_SITE_SEARCH_RADIUS = 6;
	public static final int PLACEMENT_SALT = 1978025;
	public static final RoadsideCuriosityFeature FEATURE =
			new RoadsideCuriosityFeature();
	private static Holder<PlacedFeature> placedFeature;

	private RoadsideCuriosityFeature() {
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
								RoadsideCuriosityFeature>(
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
						HeightmapPlacement.onHeightmap(
								Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES),
						BiomeFilter.biome())));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos origin = surfaceAt(world,
				context.origin().getX(),
				context.origin().getZ());
		Variant variant = variantAt(world, origin);
		if (variant == null) {
			return false;
		}
		BlockPos centre = findSafeSite(world, origin,
				variant);
		return centre != null
				&& buildAt(world, context.random(),
						centre, variant,
						orientation(world.getSeed(),
								centre));
	}

	/**
	 * Deterministic construction seam used by GameTests. The supplied centre is
	 * the central ground block and the cache is always directly above it.
	 */
	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, Variant variant,
			Rotation rotation) {
		if (!hasSafeFootprint(world, centre,
				variant)) {
			return false;
		}
		switch (variant) {
		case SPILLED_SWEET_CART:
			buildSpilledSweetCart(world, centre,
					rotation);
			break;
		case WRONG_WAY_SIGNPOST:
			buildWrongWaySignpost(world, centre,
					rotation);
			break;
		case MARSHMALLOW_RESCUE_SHELTER:
			buildRescueShelter(world, centre,
					rotation);
			break;
		default:
			throw new IllegalStateException(
					"Unhandled roadside curiosity "
							+ variant);
		}
		BlockPos cache = cachePosition(centre);
		world.setBlock(cache,
				Blocks.CHEST.defaultBlockState()
						.rotate(rotation),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, cache, LOOT_ID);
		return true;
	}

	public static Rotation orientation(
			long worldSeed, BlockPos centre) {
		Random random = new Random(worldSeed
				^ centre.asLong() ^ PLACEMENT_SALT);
		return Rotation.getRandom(random);
	}

	public static BlockPos cachePosition(
			BlockPos centre) {
		return centre.above();
	}

	public static BlockPos sentinelPosition(
			BlockPos centre, Variant variant,
			Rotation rotation) {
		return switch (variant) {
		case SPILLED_SWEET_CART ->
			local(centre, rotation, -1, 1, -1);
		case WRONG_WAY_SIGNPOST ->
			local(centre, rotation, 0, 4, -1);
		case MARSHMALLOW_RESCUE_SHELTER ->
			local(centre, rotation, 0, 4, 0);
		};
	}

	public static Variant variantForBiome(
			ResourceLocation biome) {
		if (CakeWorldBiomes.CANDY_PLAINS.getId()
				.equals(biome)) {
			return Variant.SPILLED_SWEET_CART;
		}
		if (CakeWorldBiomes.COOKIE_FOREST.getId()
				.equals(biome)) {
			return Variant.WRONG_WAY_SIGNPOST;
		}
		if (CakeWorldBiomes.MARSHMALLOW_PEAKS.getId()
				.equals(biome)) {
			return Variant.MARSHMALLOW_RESCUE_SHELTER;
		}
		return null;
	}

	public static boolean hasSafeFootprint(
			WorldGenLevel world, BlockPos centre,
			Variant variant) {
		int radius = variant.footprintRadius();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				int surfaceY = world.getHeight(
						Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES,
						centre.getX() + x,
						centre.getZ() + z) - 1;
				if (surfaceY > centre.getY()
						|| centre.getY() - surfaceY
								> variant
										.maximumDrop()) {
					return false;
				}
				BlockPos ground = new BlockPos(
						centre.getX() + x,
						surfaceY,
						centre.getZ() + z);
				BlockState groundState =
						world.getBlockState(ground);
				if (!groundState.isFaceSturdy(
						world, ground, Direction.UP)
						|| !world.getFluidState(ground)
								.isEmpty()) {
					return false;
				}
				for (int y = surfaceY + 1;
						y <= centre.getY()
								+ variant.clearance();
						y++) {
					BlockState state =
							world.getBlockState(
									new BlockPos(
											ground.getX(),
											y,
											ground.getZ()));
					if (!state.isAir()
							&& !state.getMaterial()
									.isReplaceable()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static BlockPos findSafeSite(
			WorldGenLevel world, BlockPos origin,
			Variant variant) {
		for (int radius = 0;
				radius <= SAFE_SITE_SEARCH_RADIUS;
				radius++) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					if (radius > 0
							&& Math.abs(x) != radius
							&& Math.abs(z) != radius) {
						continue;
					}
					BlockPos candidate = raiseToHighestSurface(
							world,
							origin.getX() + x,
							origin.getZ() + z,
							variant);
					if (variantAt(world, candidate)
								== variant
							&& hasSafeFootprint(
									world,
									candidate,
									variant)) {
						return candidate;
					}
				}
			}
		}
		return null;
	}

	private static Variant variantAt(
			WorldGenLevel world, BlockPos position) {
		ResourceLocation biome = world.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.getKey(world.getBiome(position).value());
		return variantForBiome(biome);
	}

	private static BlockPos surfaceAt(
			WorldGenLevel world, int x, int z) {
		return new BlockPos(x,
				world.getHeight(
						Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES,
						x, z) - 1,
				z);
	}

	private static BlockPos raiseToHighestSurface(
			WorldGenLevel world, int x, int z,
			Variant variant) {
		int surfaceY = Integer.MIN_VALUE;
		for (int offsetX =
				-variant.footprintRadius();
				offsetX <= variant
						.footprintRadius();
				offsetX++) {
			for (int offsetZ =
					-variant.footprintRadius();
					offsetZ <= variant
							.footprintRadius();
					offsetZ++) {
				surfaceY = Math.max(
						surfaceY,
						world.getHeight(
								Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
								x + offsetX,
								z + offsetZ)
								- 1);
			}
		}
		return new BlockPos(x, surfaceY, z);
	}

	private static void buildSpilledSweetCart(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK
				.get().defaultBlockState();
		BlockState crumbs =
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState();
		BlockState handle =
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Z);
		for (int x = -2; x <= 2; x++) {
			for (int z = -1; z <= 1; z++) {
				set(world, centre, rotation,
						x, 0, z, crumbs);
			}
		}
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x != 0 || z != 0) {
					set(world, centre, rotation,
							x, 1, z, wafer);
				}
			}
		}
		for (int x : new int[] {-2, 2}) {
			set(world, centre, rotation,
					x, 1, -1,
					CakeWorldBlocks
							.RASPBERRY_GUMMY_BLOCK
							.get().defaultBlockState());
			set(world, centre, rotation,
					x, 1, 1,
					CakeWorldBlocks
							.BLUEBERRY_GUMMY_BLOCK
							.get().defaultBlockState());
		}
		for (int z = 2; z <= 3; z++) {
			set(world, centre, rotation,
					0, 1, z, handle);
		}
		set(world, centre, rotation,
				-1, 0, 2, crumbs);
		set(world, centre, rotation,
				1, 0, 2, crumbs);
		set(world, centre, rotation,
				2, 0, 3, crumbs);
		set(world, centre, rotation,
				2, 1, 2,
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState());
		set(world, centre, rotation,
				-1, 1, 3,
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK
						.get().defaultBlockState());
	}

	private static void buildWrongWaySignpost(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		BlockState crumbs =
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState();
		BlockState vertical =
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState();
		BlockState horizontal =
				vertical.setValue(
						RotatedPillarBlock.AXIS,
						Direction.Axis.X);
		for (Direction direction
				: Direction.Plane.HORIZONTAL) {
			for (int distance = 0;
					distance <= 3; distance++) {
				BlockPos point = centre
						.relative(direction, distance);
				world.setBlock(point, crumbs, 2);
			}
		}
		for (int x = -1; x <= 1; x++) {
			set(world, centre, rotation,
					x, 0, -1,
					CakeWorldBlocks.WAFER_BLOCK
							.get().defaultBlockState());
		}
		for (int y = 1; y <= 4; y++) {
			set(world, centre, rotation,
					0, y, -1, vertical);
		}
		for (int x = -2; x <= 2; x++) {
			set(world, centre, rotation,
					x, 4, -1, horizontal);
		}
		set(world, centre, rotation,
				-3, 4, -1,
				CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get()
						.defaultBlockState());
		set(world, centre, rotation,
				3, 4, -1,
				CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get()
						.defaultBlockState());
		set(world, centre, rotation,
				0, 5, -1,
				Blocks.LANTERN.defaultBlockState());
	}

	private static void buildRescueShelter(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		BlockState marshmallow =
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState();
		BlockState support =
				CakeWorldBlocks.CANDY_CANE_PILLAR
						.get().defaultBlockState();
		BlockState wafer =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		BlockState icingLayer =
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState();
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				set(world, centre, rotation,
						x, 0, z,
						marshmallow);
				fillDown(world, centre, rotation,
						x, z, marshmallow, 3);
				set(world, centre, rotation,
						x, 4, z, wafer);
				set(world, centre, rotation,
						x, 5, z,
						icingLayer);
			}
		}
		for (int x : new int[] {-2, 2}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					set(world, centre,
							rotation,
							x, y, z,
							support);
				}
			}
		}
		set(world, centre, rotation,
				0, 3, 0,
				Blocks.LANTERN.defaultBlockState()
						.setValue(
								LanternBlock.HANGING,
								true));
		for (int z = 3; z <= 4; z++) {
			set(world, centre, rotation,
					0, 0, z,
					CakeWorldBlocks.BISCUIT_CRUMBS
							.get().defaultBlockState());
			fillDown(world, centre, rotation,
					0, z,
					CakeWorldBlocks.BISCUIT_STONE
							.get().defaultBlockState(),
					3);
		}
	}

	private static void fillDown(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, int x, int z,
			BlockState state, int maximumDepth) {
		for (int depth = 1;
				depth <= maximumDepth; depth++) {
			BlockPos position = local(centre,
					rotation, x, -depth, z);
			BlockState existing =
					world.getBlockState(position);
			if (!existing.isAir()
					&& !existing.getMaterial()
							.isReplaceable()) {
				return;
			}
			world.setBlock(position,
					state.rotate(rotation), 2);
		}
	}

	private static void set(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation, int x, int y,
			int z, BlockState state) {
		world.setBlock(local(centre, rotation,
				x, y, z), state.rotate(rotation), 2);
	}

	private static BlockPos local(
			BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return centre.offset(
				new BlockPos(x, y, z)
						.rotate(rotation));
	}

	private static ResourceLocation id(
			String path) {
		return new ResourceLocation(
				CakeWorld.MODID, path);
	}

	public enum Variant {
		SPILLED_SWEET_CART(3, 3, 1),
		WRONG_WAY_SIGNPOST(3, 5, 1),
		MARSHMALLOW_RESCUE_SHELTER(4, 5, 3);

		private final int footprintRadius;
		private final int clearance;
		private final int maximumDrop;

		Variant(int footprintRadius,
				int clearance,
				int maximumDrop) {
			this.footprintRadius =
					footprintRadius;
			this.clearance = clearance;
			this.maximumDrop = maximumDrop;
		}

		public int footprintRadius() {
			return footprintRadius;
		}

		public int clearance() {
			return clearance;
		}

		public int maximumDrop() {
			return maximumDrop;
		}
	}
}
