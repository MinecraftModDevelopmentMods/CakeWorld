package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
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
 * A bounded Sherbet-Dunes discovery scene: striped powder ripples expose a
 * Rock-Candy fossil, Fizzy-Pearl markers and one genuinely buried sweet jar.
 *
 * <p>The feature runs once after OreSpawn's surface pass. It may clear only
 * inherited vegetation, never creates entities, and has no repair or replay
 * path that could overwrite later player work.</p>
 */
public final class SherbetFossilBowlFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("sherbet_fossil_bowl");
	public static final ResourceLocation LOOT_ID =
			id("chests/sherbet_fossil_bowl");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 16;
	public static final int MAX_TERRAIN_RELIEF = 3;
	public static final int SAFE_SITE_SEARCH_RADIUS = 6;
	public static final int PLACEMENT_SALT = 1978070;
	public static final SherbetFossilBowlFeature FEATURE =
			new SherbetFossilBowlFeature();
	private static final ResourceKey<Biome> SHERBET_DUNES_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.SHERBET_DUNES.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static Holder<PlacedFeature> placedFeature;

	private SherbetFossilBowlFeature() {
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
								SherbetFossilBowlFeature>(
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
		if (!world.getBiome(origin).is(SHERBET_DUNES_KEY)) {
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
							.is(SHERBET_DUNES_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							around.getX(),
							highestSurfaceY(world, around),
							around.getZ());
					Rotation rotation = orientation(
							world.getSeed(), centre);
					if (fitsWithinChunk(centre,
							rotation, placementChunk)
							&& buildAt(world,
									context.random(),
									centre, rotation)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			Random random, BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearInheritedVegetation(world, centre, rotation);
		buildStripedBowl(world, centre, rotation);
		buildWaferTrail(world, centre, rotation);
		buildFossil(world, centre, rotation);
		buildPearlMarkers(world, centre, rotation);
		buildBuriedJar(world, random, centre, rotation);
		return true;
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (surfaceY > centre.getY()
						|| centre.getY() - surfaceY
								> MAX_TERRAIN_RELIEF) {
					return false;
				}
				BlockPos surface = new BlockPos(
						horizontal.getX(), surfaceY,
						horizontal.getZ());
				BlockState ground =
						world.getBlockState(surface);
				if (!world.getFluidState(surface).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return false;
				}
				for (int y = surfaceY + 1;
					y <= centre.getY() + 5; y++) {
					BlockState state = world.getBlockState(
							new BlockPos(
									horizontal.getX(),
									y,
									horizontal.getZ()));
					if (state.hasBlockEntity()
							|| !canClear(state)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-5, 5}) {
				BlockPos corner = local(
						centre, rotation, x, 0, z);
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

	public static BlockPos jarPosition(BlockPos centre,
			Rotation rotation) {
		return local(centre, rotation, 3, -1, 3);
	}

	private static void buildStripedBowl(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				BlockPos top = local(
						centre, rotation, x, 0, z);
				world.setBlock(top,
						powderFor(x, z), 2);
				for (int y = -1;
					y >= -MAX_TERRAIN_RELIEF; y--) {
					BlockPos support = top.offset(0, y, 0);
					if (!world.isEmptyBlock(support)) {
						break;
					}
					world.setBlock(support,
							CakeWorldBlocks.BISCUIT_STONE
									.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static BlockState powderFor(int x, int z) {
		return switch (Math.floorMod(x + z, 4)) {
			case 0 -> CakeWorldBlocks.RASPBERRY_SHERBET_POWDER
					.get().defaultBlockState();
			case 1 -> CakeWorldBlocks.ORANGE_SHERBET_POWDER
					.get().defaultBlockState();
			case 2 -> CakeWorldBlocks.LEMON_SHERBET_POWDER
					.get().defaultBlockState();
			default -> CakeWorldBlocks.LIME_SHERBET_POWDER
					.get().defaultBlockState();
		};
	}

	private static void buildWaferTrail(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int z = -5; z <= 5; z++) {
			world.setBlock(local(
					centre, rotation, 0, 0, z),
					CakeWorldBlocks.WAFER_BLOCK.get()
							.defaultBlockState(), 2);
		}
	}

	private static void buildFossil(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState fossil = CakeWorldBlocks.ROCK_CANDY_FOSSIL
				.get().defaultBlockState();
		for (int x = -3; x <= 3; x++) {
			set(world, centre, rotation, x, 1, -1, fossil);
		}
		for (int x : new int[] {-2, 0, 2}) {
			for (int side : new int[] {-1, 1}) {
				set(world, centre, rotation,
						x, 1, -1 + side, fossil);
				set(world, centre, rotation,
						x, 2, -1 + side * 2, fossil);
				set(world, centre, rotation,
						x, 2, -1 + side * 3, fossil);
			}
		}
	}

	private static void buildPearlMarkers(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int[] marker : new int[][] {
				{-4, -4}, {-4, 4}, {4, -4}, {4, 4}, {3, 3}
		}) {
			set(world, centre, rotation,
					marker[0], 1, marker[1],
					CakeWorldBlocks.FIZZY_PEARL.get()
							.defaultBlockState());
		}
	}

	private static void buildBuriedJar(WorldGenLevel world,
			Random random, BlockPos centre, Rotation rotation) {
		BlockPos jar = jarPosition(centre, rotation);
		world.setBlock(jar,
				Blocks.BARREL.defaultBlockState()
						.setValue(BarrelBlock.FACING,
								Direction.UP),
				2);
		RandomizableContainerBlockEntity.setLootTable(
				world, random, jar, LOOT_ID);
		if (world.getBlockEntity(jar)
				instanceof RandomizableContainerBlockEntity container) {
			container.setCustomName(new TranslatableComponent(
					"container.cakeworld.buried_sherbet_jar"));
			container.setChanged();
		}
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int y = 0; y <= 5; y++) {
				for (int z = -5; z <= 5; z++) {
					BlockPos position = local(centre,
							rotation, x, y, z);
					BlockState state =
							world.getBlockState(position);
					if (canClear(state)) {
						world.setBlock(position,
								Blocks.AIR
										.defaultBlockState(),
								2);
					}
				}
			}
		}
	}

	private static boolean isAcceptedGround(BlockState state) {
		return state.is(CakeWorldBlocks.RASPBERRY_SHERBET_POWDER.get())
				|| state.is(CakeWorldBlocks.ORANGE_SHERBET_POWDER.get())
				|| state.is(CakeWorldBlocks.LEMON_SHERBET_POWDER.get())
				|| state.is(CakeWorldBlocks.LIME_SHERBET_POWDER.get())
				|| state.is(CakeWorldBlocks.BISCUIT_SAND.get())
				|| state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(Blocks.SAND)
				|| state.is(Blocks.RED_SAND)
				|| state.is(Blocks.SANDSTONE)
				|| state.is(Blocks.RED_SANDSTONE)
				|| state.is(Blocks.GRASS_BLOCK)
				|| state.is(Blocks.DIRT)
				|| state.is(Blocks.COARSE_DIRT)
				|| state.is(Blocks.STONE);
	}

	private static boolean canClear(BlockState state) {
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS)
				|| state.is(Blocks.CACTUS);
	}

	private static int highestSurfaceY(WorldGenLevel world,
			BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				highest = Math.max(highest,
						terrainSurfaceY(world,
								around.getX() + x,
								around.getZ() + z));
			}
		}
		return highest;
	}

	private static BlockPos surfaceAt(WorldGenLevel world,
			int x, int z) {
		return new BlockPos(x,
				terrainSurfaceY(world, x, z), z);
	}

	private static int terrainSurfaceY(WorldGenLevel world,
			int x, int z) {
		int y = world.getHeight(
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				x, z) - 1;
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos(x, y, z);
		while (y > world.getMinBuildHeight()) {
			BlockState state = world.getBlockState(cursor);
			if (!world.getFluidState(cursor).isEmpty()
					|| !canClear(state)) {
				break;
			}
			y--;
			cursor.setY(y);
		}
		return y;
	}

	private static void set(WorldGenLevel world,
			BlockPos centre, Rotation rotation,
			int x, int y, int z, BlockState state) {
		world.setBlock(local(centre, rotation, x, y, z),
				state, 2);
	}

	private static BlockPos local(BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return centre.offset(new BlockPos(x, y, z)
				.rotate(rotation));
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
