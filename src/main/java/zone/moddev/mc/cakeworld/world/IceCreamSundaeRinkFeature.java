package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.Rotation;
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
 * A safe Frozen-Lemonade rink ringed with Wafer, rescue cushions and three
 * little flavour scoops.
 *
 * <p>The whole scene is contained in its generating chunk. It creates no
 * entities or block entities and has no repair pass, so later player changes
 * are never mistaken for damaged world generation.</p>
 */
public final class IceCreamSundaeRinkFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			id("ice_cream_sundae_rink");
	public static final int AVERAGE_CHUNKS_PER_ATTEMPT = 12;
	public static final int MAX_TERRAIN_RELIEF = 4;
	public static final int SAFE_SITE_SEARCH_RADIUS = 8;
	public static final int PLACEMENT_SALT = 1978073;
	public static final IceCreamSundaeRinkFeature FEATURE =
			new IceCreamSundaeRinkFeature();
	private static final ResourceKey<Biome> TUNDRA_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.ICE_CREAM_TUNDRA.getId());
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};
	private static final int[] SCOOP_CENTRES = {-3, 0, 3};
	private static Holder<PlacedFeature> placedFeature;

	private IceCreamSundaeRinkFeature() {
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
								IceCreamSundaeRinkFeature>(
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
		if (!world.getBiome(origin).is(TUNDRA_KEY)) {
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
					if (!world.getBiome(around).is(TUNDRA_KEY)) {
						continue;
					}
					BlockPos centre = new BlockPos(
							around.getX(),
							highestSurfaceY(world, around),
							around.getZ());
					Rotation rotation = orientation(
							world.getSeed(), centre);
					if (fitsWithinChunk(centre, rotation,
							placementChunk)
							&& buildAt(world, centre,
									rotation)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean buildAt(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		if (!hasSafeFootprint(world, centre, rotation)) {
			return false;
		}
		clearInheritedVegetation(world, centre, rotation);
		buildRink(world, centre, rotation);
		buildScoops(world, centre, rotation);
		buildCornerMarkers(world, centre, rotation);
		return true;
	}

	public static boolean hasSafeFootprint(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		return footprintProblem(world, centre, rotation) == null;
	}

	public static String footprintProblem(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				if (x * x + z * z > 25) {
					continue;
				}
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				if (surfaceY > centre.getY()
						|| centre.getY() - surfaceY
								> MAX_TERRAIN_RELIEF) {
					return "relief at " + horizontal
							+ ": surfaceY=" + surfaceY
							+ ", centreY=" + centre.getY();
				}
				BlockPos surface = new BlockPos(
						horizontal.getX(), surfaceY,
						horizontal.getZ());
				BlockState ground = world.getBlockState(surface);
				if (!world.getFluidState(surface).isEmpty()
						|| ground.hasBlockEntity()
						|| !isAcceptedGround(ground)) {
					return "ground at " + surface
							+ ": state=" + ground
							+ ", fluid="
							+ world.getFluidState(surface)
							+ ", blockEntity="
							+ ground.hasBlockEntity();
				}
				for (int y = surfaceY + 1;
						y <= centre.getY() + 4; y++) {
					BlockState state = world.getBlockState(
							new BlockPos(horizontal.getX(),
									y, horizontal.getZ()));
					if (state.hasBlockEntity()
							|| !canClear(state)) {
						return "obstacle at "
								+ new BlockPos(
										horizontal.getX(),
										y,
										horizontal.getZ())
								+ ": state=" + state
								+ ", blockEntity="
								+ state.hasBlockEntity();
					}
				}
			}
		}
		return null;
	}

	public static boolean fitsWithinChunk(BlockPos centre,
			Rotation rotation, ChunkPos chunk) {
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-5, 5}) {
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

	public static int[] scoopCentres() {
		return SCOOP_CENTRES;
	}

	private static void buildRink(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState frozen = CakeWorldBlocks.FROZEN_LEMONADE
				.get().defaultBlockState();
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK
				.get().defaultBlockState();
		BlockState rescue = CakeWorldBlocks.MARSHMALLOW
				.get().defaultBlockState();
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				int distance = x * x + z * z;
				if (distance > 25) {
					continue;
				}
				BlockState state = distance <= 16
						? frozen : wafer;
				if ((Math.abs(x) == 5 && z == 0)
						|| (Math.abs(z) == 5 && x == 0)) {
					state = rescue;
				}
				BlockPos platform =
						local(centre, rotation, x, 1, z);
				world.setBlock(platform, state, 2);
				supportDown(world, platform.below());
			}
		}
	}

	private static void buildScoops(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState[] flavours = {
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(),
				CakeWorldBlocks.ICING.get()
						.defaultBlockState(),
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState()
		};
		BlockState cap = CakeWorldBlocks.ICING_LAYER
				.get().defaultBlockState();
		for (int index = 0; index < SCOOP_CENTRES.length;
				index++) {
			int x = SCOOP_CENTRES[index];
			BlockState flavour = flavours[index];
			set(world, centre, rotation, x, 2, 0, flavour);
			set(world, centre, rotation, x - 1, 2, 0,
					flavour);
			set(world, centre, rotation, x + 1, 2, 0,
					flavour);
			set(world, centre, rotation, x, 2, -1,
					flavour);
			set(world, centre, rotation, x, 2, 1,
					flavour);
			set(world, centre, rotation, x, 3, 0,
					flavour);
			set(world, centre, rotation, x, 4, 0, cap);
		}
	}

	private static void buildCornerMarkers(WorldGenLevel world,
			BlockPos centre, Rotation rotation) {
		BlockState pillar = CakeWorldBlocks.CANDY_CANE_PILLAR
				.get().defaultBlockState();
		BlockState glass = CakeWorldBlocks.CANDY_GLASS
				.get().defaultBlockState();
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-3, 3}) {
				set(world, centre, rotation, x, 2, z, pillar);
				set(world, centre, rotation, x, 3, z, pillar);
				set(world, centre, rotation, x, 4, z, glass);
			}
		}
	}

	private static void supportDown(WorldGenLevel world,
			BlockPos start) {
		BlockPos.MutableBlockPos cursor = start.mutable();
		while (cursor.getY() > world.getMinBuildHeight()
				&& canClear(world.getBlockState(cursor))) {
			world.setBlock(cursor,
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState(),
					2);
			cursor.move(0, -1, 0);
		}
	}

	private static void clearInheritedVegetation(
			WorldGenLevel world, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				if (x * x + z * z > 25) {
					continue;
				}
				BlockPos horizontal =
						local(centre, rotation, x, 0, z);
				int surfaceY = terrainSurfaceY(world,
						horizontal.getX(),
						horizontal.getZ());
				for (int y = surfaceY + 1;
						y <= centre.getY() + 4; y++) {
					BlockPos position = new BlockPos(
							horizontal.getX(), y,
							horizontal.getZ());
					if (canClear(
							world.getBlockState(position))) {
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
		return state.is(CakeWorldBlocks.ICING.get())
				|| state.is(CakeWorldBlocks.ICING_LAYER.get())
				|| state.is(CakeWorldBlocks.FROZEN_LEMONADE.get())
				|| state.is(CakeWorldBlocks.MARSHMALLOW.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(CakeWorldBlocks.PEPPERMINT_ROCK.get())
				|| state.is(CakeWorldBlocks.ROCK_CANDY.get())
				|| state.is(CakeWorldBlocks.WAFER_ROCK.get())
				|| state.is(BlockTags.BASE_STONE_OVERWORLD)
				|| state.is(BlockTags.DIRT)
				|| state.is(Blocks.SNOW_BLOCK)
				|| state.is(Blocks.POWDER_SNOW)
				|| state.is(Blocks.GRAVEL);
	}

	private static boolean canClear(BlockState state) {
		if (state.is(CakeWorldBlocks.ICING.get())) {
			return false;
		}
		return state.isAir()
				|| state.getMaterial().isReplaceable()
				|| state.is(CakeWorldBlocks.ICING_LAYER.get())
				|| state.is(BlockTags.LEAVES)
				|| state.is(BlockTags.LOGS)
				|| state.is(Blocks.SNOW)
				|| state.is(Blocks.POWDER_SNOW);
	}

	private static int highestSurfaceY(WorldGenLevel world,
			BlockPos around) {
		int highest = Integer.MIN_VALUE;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				if (x * x + z * z <= 25) {
					highest = Math.max(highest,
							terrainSurfaceY(world,
									around.getX() + x,
									around.getZ() + z));
				}
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
		int solidSurface = y;
		cursor.set(x, solidSurface + 1, z);
		if (world.getBlockState(cursor)
				.is(CakeWorldBlocks.ICING_LAYER.get())) {
			y = solidSurface + 1;
		}
		return y;
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
