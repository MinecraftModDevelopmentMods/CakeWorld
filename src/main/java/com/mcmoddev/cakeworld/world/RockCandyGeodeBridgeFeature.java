package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

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
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * A small prismatic bridge built only into an existing dry cave chamber.
 *
 * <p>The authored crystals are a visual learning display, not evidence that
 * OreSpawn generated a deposit. The feature never carves solid terrain,
 * replaces fluids or block entities, creates inventories or entities, and
 * has no repair pass. Natural OreSpawn geology around it therefore remains
 * independently observable and player edits survive reload.</p>
 *
 * <p>It is attached at the final decoration step to reduce later-decoration
 * conflicts. Site validation runs at that same late step, so the bridge also
 * refuses to replace any natural resource that already occupies its plan.
 * Its load-bearing Cut Rock Candy is deliberately not an edible ore host, so
 * managed deposits cannot reinterpret the authored rails and arches as
 * replaceable geology even if feature ordering changes.</p>
 */
public final class RockCandyGeodeBridgeFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("rock_candy_geode_bridge");
	public static final int MIN_Y = -48;
	public static final int MAX_Y = 48;
	public static final int PLACEMENT_SALT = 1978083;
	public static final RockCandyGeodeBridgeFeature FEATURE =
			new RockCandyGeodeBridgeFeature();
	private static final ResourceKey<Biome> CAVERNS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.ROCK_CANDY_CAVERNS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[][] ARCH = {
			{-3, 0}, {3, 0},
			{-3, 1}, {3, 1},
			{-2, 2}, {2, 2},
			{-1, 3}, {0, 3}, {1, 3}
	};
	private static final int[][] MINT_MARKERS = {
			{-4, -2}, {4, -2}, {-4, 2}, {4, 2}
	};
	private static final int[][] DEPOSIT_MARKERS = {
			{-2, -2}, {2, -2}, {-2, 2}, {2, 2}
	};
	private static Holder<PlacedFeature> placedFeature;

	private RockCandyGeodeBridgeFeature() {
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
								RockCandyGeodeBridgeFeature>(
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
					if (!world.getBiome(centre).is(CAVERNS_KEY)) {
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
		buildBridge(world, centre, rotation);
		buildGeodeArches(world, centre, rotation);
		buildLearningMarkers(world, centre, rotation);
		buildRecoveryPads(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeSite(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (centre.getY() < Math.max(world.getMinBuildHeight() + 6,
				MIN_Y)
				|| centre.getY() > Math.min(
						world.getMaxBuildHeight() - 6, MAX_Y)) {
			return false;
		}
		for (int x = -5; x <= 5; x++) {
			for (int z = -1; z <= 1; z++) {
				if (!canOccupy(world,
						local(centre, rotation, x, 0, z))
						|| !canOccupy(world,
								local(centre, rotation,
										x, 1, z))) {
					return false;
				}
			}
		}
		for (int x = -5; x <= 5; x += 2) {
			if (!canOccupy(world,
					local(centre, rotation, x, 1, -2))
					|| !canOccupy(world,
							local(centre, rotation, x, 1, 2))) {
				return false;
			}
		}
		for (int x : new int[] {-5, 5}) {
			for (int[] point : ARCH) {
				if (!canOccupy(world,
						local(centre, rotation, x,
								point[1], point[0]))) {
					return false;
				}
			}
			for (int z = -1; z <= 1; z++) {
				for (int y = -2; y <= -1; y++) {
					if (!canOccupy(world,
							local(centre, rotation,
									x, y, z))) {
						return false;
					}
				}
			}
		}
		for (int[] marker : MINT_MARKERS) {
			if (!canOccupy(world,
					local(centre, rotation,
							marker[0], 1, marker[1]))) {
				return false;
			}
		}
		for (int[] marker : DEPOSIT_MARKERS) {
			if (!canOccupy(world,
					local(centre, rotation,
							marker[0], 1, marker[1]))) {
				return false;
			}
		}
		for (int z : new int[] {-2, 2}) {
			if (!canOccupy(world,
					local(centre, rotation, 0, 1, z))) {
				return false;
			}
		}
		if (!canOccupy(world,
				local(centre, rotation, 0, 1, 0))) {
			return false;
		}
		int openBelow = 0;
		for (int x = -2; x <= 2; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -2; y <= -1; y++) {
					BlockPos position =
							local(centre, rotation, x, y, z);
					if (canOccupy(world, position)) {
						openBelow++;
					}
				}
			}
		}
		for (int x = -1; x <= 1; x++) {
			if (!canOccupy(world,
					local(centre, rotation, x, -3, 0))) {
				return false;
			}
		}
		int solidBoundary = 0;
		for (int y = -3; y <= 4; y++) {
			for (int x = -6; x <= 6; x++) {
				for (int z : new int[] {-4, 4}) {
					if (isNaturalBoundary(world,
							local(centre, rotation, x, y, z))) {
						solidBoundary++;
					}
				}
			}
			for (int z = -3; z <= 3; z++) {
				for (int x : new int[] {-6, 6}) {
					if (isNaturalBoundary(world,
							local(centre, rotation, x, y, z))) {
						solidBoundary++;
					}
				}
			}
		}
		return openBelow >= 12 && solidBoundary >= 8;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-4, 4}) {
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

	public static int[][] mintMarkers() {
		return MINT_MARKERS;
	}

	public static int[][] depositMarkers() {
		return DEPOSIT_MARKERS;
	}

	private static void buildBridge(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState glass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		for (int x = -5; x <= 5; x++) {
			for (int z = -1; z <= 1; z++) {
				set(world, centre, rotation, x, 0, z, glass);
			}
		}
		BlockState cutRock =
				CakeWorldBlocks.CUT_ROCK_CANDY.get()
						.defaultBlockState();
		for (int x = -5; x <= 5; x += 2) {
			set(world, centre, rotation, x, 1, -2, cutRock);
			set(world, centre, rotation, x, 1, 2, cutRock);
		}
		BlockState pillar =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		for (int x : new int[] {-5, 5}) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -2; y <= -1; y++) {
					set(world, centre, rotation,
							x, y, z, pillar);
				}
			}
		}
	}

	private static void buildGeodeArches(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState rock = CakeWorldBlocks.CUT_ROCK_CANDY.get()
				.defaultBlockState();
		for (int x : new int[] {-5, 5}) {
			for (int[] point : ARCH) {
				set(world, centre, rotation,
						x, point[1], point[0], rock);
			}
		}
	}

	private static void buildLearningMarkers(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int[] marker : MINT_MARKERS) {
			set(world, centre, rotation,
					marker[0], 1, marker[1],
					CakeWorldBlocks.MINT_CRYSTAL.get()
							.defaultBlockState());
		}
		for (int[] marker : DEPOSIT_MARKERS) {
			set(world, centre, rotation,
					marker[0], 1, marker[1],
					CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
							.defaultBlockState());
		}
		for (int z : new int[] {-2, 2}) {
			set(world, centre, rotation, 0, 1, z,
					CakeWorldBlocks.MINT_EMERALD.get()
							.defaultBlockState());
		}
		set(world, centre, rotation, 0, 1, 0,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
	}

	private static void buildRecoveryPads(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState marshmallow =
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState();
		for (int x = -1; x <= 1; x++) {
			set(world, centre, rotation,
					x, -3, 0, marshmallow);
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
