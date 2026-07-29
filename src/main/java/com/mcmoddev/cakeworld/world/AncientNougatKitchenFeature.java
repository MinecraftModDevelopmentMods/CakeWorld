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
 * A compact deep-cave kitchen built from structural Nougat tiles.
 *
 * <p>The three Mint Crystals and three Rock-Candy Deposits are authored
 * teaching samples and must never be counted as OreSpawn output evidence.
 * The feature stays inside one chunk, only occupies a dry open chamber,
 * creates no entity, block entity or inventory, and has no repair pass, so a
 * later player edit is preserved.</p>
 */
public final class AncientNougatKitchenFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("ancient_nougat_kitchen");
	public static final int MIN_Y = -56;
	public static final int MAX_Y = -24;
	public static final int PLACEMENT_SALT = 21102131;
	public static final AncientNougatKitchenFeature FEATURE =
			new AncientNougatKitchenFeature();
	private static final ResourceKey<Biome> DEPTHS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.NOUGAT_DEPTHS.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private AncientNougatKitchenFeature() {
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
								AncientNougatKitchenFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of(
						InSquarePlacement.spread(),
						HeightRangePlacement.uniform(
								VerticalAnchor.absolute(MIN_Y),
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
		int minY = Math.max(world.getMinBuildHeight() + 2, MIN_Y);
		int maxY = Math.min(world.getMaxBuildHeight() - 5, MAX_Y);
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
					if (!world.getBiome(centre).is(DEPTHS_KEY)) {
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
		buildFloor(world, centre, rotation);
		buildFrame(world, centre, rotation);
		buildKitchen(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeSite(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (centre.getY() < Math.max(
				world.getMinBuildHeight() + 2, MIN_Y)
				|| centre.getY() > Math.min(
						world.getMaxBuildHeight() - 5, MAX_Y)) {
			return false;
		}
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				for (int y = 0; y <= 4; y++) {
					if (!canOccupy(world,
							local(centre, rotation, x, y, z))) {
						return false;
					}
				}
			}
		}
		int solidSupports = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				if (isNaturalSupport(world,
						local(centre, rotation, x, -1, z))) {
					solidSupports++;
				}
			}
		}
		return solidSupports >= 8;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-2, 2}) {
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

	private static void buildFloor(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState tiles =
				CakeWorldBlocks.TOASTED_NOUGAT_TILES.get()
						.defaultBlockState();
		BlockState path =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				set(world, centre, rotation, x, 0, z,
						z == 0 ? path : tiles);
			}
		}
	}

	private static void buildFrame(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState pillar =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState();
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					set(world, centre, rotation,
							x, y, z, pillar);
				}
				set(world, centre, rotation,
						x, 4, z,
						CakeWorldBlocks.CANDY_GLASS.get()
								.defaultBlockState());
			}
		}
	}

	private static void buildKitchen(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState tiles =
				CakeWorldBlocks.TOASTED_NOUGAT_TILES.get()
						.defaultBlockState();
		set(world, centre, rotation, -2, 1, -1, tiles);
		set(world, centre, rotation, 2, 1, -1, tiles);
		set(world, centre, rotation, -1, 1, -1,
				CakeWorldBlocks.OVEN.get().defaultBlockState());
		set(world, centre, rotation, 0, 1, -1,
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, centre, rotation, 1, 1, -1,
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());

		set(world, centre, rotation, -2, 1, 1,
				CakeWorldBlocks.MINT_CRYSTAL.get()
						.defaultBlockState());
		set(world, centre, rotation, -1, 1, 1,
				CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
						.defaultBlockState());
		set(world, centre, rotation, 0, 1, 1,
				CakeWorldBlocks.MINT_CRYSTAL.get()
						.defaultBlockState());
		set(world, centre, rotation, 1, 1, 1,
				CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
						.defaultBlockState());
		set(world, centre, rotation, 2, 1, 1,
				CakeWorldBlocks.MINT_CRYSTAL.get()
						.defaultBlockState());
		set(world, centre, rotation, -2, 1, 0,
				CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
						.defaultBlockState());
		set(world, centre, rotation, 2, 1, 0,
				CakeWorldBlocks.NOUGAT_ROCK.get()
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
		world.setBlock(local(centre, rotation, x, y, z),
				state, 2);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
