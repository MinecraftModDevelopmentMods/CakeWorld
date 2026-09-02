package zone.moddev.mc.cakeworld.gametest;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

import org.slf4j.Logger;

/** Black-box release proof for the public weather-material event route. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_weather_materials")
public final class WeatherMaterialGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation ICE_CREAM_TUNDRA =
			new ResourceLocation("cakeworld", "ice_cream_tundra");

	private WeatherMaterialGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void configuredSnowAndIceReplaceVanillaWeatherProducts(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		ChunkAccess chunk = level.getChunkAt(
				helper.absolutePos(new BlockPos(8, 3, 8)));
		ChunkPos chunkPos = chunk.getPos();
		int firstX = chunkPos.getMinBlockX() + 4;
		int z = chunkPos.getMinBlockZ() + 4;
		int targetY = level.getMinBuildHeight() + 8;
		for (int x = firstX; x < firstX + 2; x++) {
			targetY = Math.max(targetY, level.getHeight(
					Heightmap.Types.MOTION_BLOCKING, x, z) + 4);
		}
		require(helper, targetY + 1 < level.getMaxBuildHeight(),
				"Weather fixture has no safe height above the current surface");

		BlockPos snow = new BlockPos(firstX, targetY, z);
		BlockPos ice = snow.east();
		for (BlockPos target : new BlockPos[] { snow, ice }) {
			clearAbove(level, target);
			level.setBlock(target.below(), Blocks.STONE.defaultBlockState(), 3);
		}
		// This is the vanilla weather layout: one-layer Snow occupies the first
		// free cell above its motion-blocking support, while Ice itself is the
		// motion-blocking surface. No artificial cap may pull either target into
		// OreSpawn's scan window.
		level.setBlock(snow, Blocks.SNOW.defaultBlockState(), 3);
		level.setBlock(ice, Blocks.ICE.defaultBlockState(), 3);

		requireConfiguredWeatherOverride(helper);
		assertNothingHigher(helper, snow, "Snow");
		assertNothingHigher(helper, ice, "Ice");
		assertInWeatherScan(helper, chunk, snow, "Snow");
		assertInWeatherScan(helper, chunk, ice, "Ice");
		require(helper, level.getBlockState(snow).is(Blocks.SNOW)
				&& level.getBlockState(ice).is(Blocks.ICE),
				"Weather fixture products changed before ChunkEvent.Load");

		ChunkEvent.Load event = new ChunkEvent.Load(chunk);
		require(helper, event.getWorld() == level,
				"Chunk-load fixture did not retain its ServerLevel");
		MinecraftForge.EVENT_BUS.post(event);

		boolean snowConverted = level.getBlockState(snow)
				.is(CakeWorldBlocks.ICING_LAYER.get());
		boolean iceConverted = level.getBlockState(ice)
				.is(CakeWorldBlocks.FROZEN_LEMONADE.get());
		require(helper, snowConverted && iceConverted,
				"OreSpawn weather conversion failed: snow=" + snowConverted
						+ ", ice=" + iceConverted + ", snowState="
						+ level.getBlockState(snow)
						+ ", iceState="
						+ level.getBlockState(ice));
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void configuredSurfaceIceUsesPublicChunkLoadRoute(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos column = helper.absolutePos(new BlockPos(8, 3, 8));
		ChunkAccess chunk = level.getChunkAt(column);
		int targetY = level.getHeight(Heightmap.Types.MOTION_BLOCKING,
				column.getX(), column.getZ()) + 4;
		require(helper, targetY + 1 < level.getMaxBuildHeight(),
				"Ice fixture has no safe height above the current surface");
		BlockPos ice = new BlockPos(column.getX(), targetY, column.getZ());
		clearAbove(level, ice);
		level.setBlock(ice.below(), Blocks.STONE.defaultBlockState(), 3);
		level.setBlock(ice, Blocks.ICE.defaultBlockState(), 3);

		requireConfiguredWeatherOverride(helper);
		assertNothingHigher(helper, ice, "Ice");
		assertInWeatherScan(helper, chunk, ice, "Ice");
		postChunkLoad(helper, chunk);
		require(helper, level.getBlockState(ice)
				.is(CakeWorldBlocks.FROZEN_LEMONADE.get()),
				"OreSpawn did not convert surface Ice through ChunkEvent.Load: "
						+ level.getBlockState(ice));
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void buriedAuthoredSnowAndIceAreNotBroadlyRewritten(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		ChunkAccess chunk = level.getChunkAt(
				helper.absolutePos(new BlockPos(8, 3, 8)));
		ChunkPos chunkPos = chunk.getPos();
		int firstX = chunkPos.getMinBlockX() + 4;
		int z = chunkPos.getMinBlockZ() + 4;
		int targetY = level.getMinBuildHeight() + 8;
		for (int x = firstX; x < firstX + 2; x++) {
			targetY = Math.max(targetY, level.getHeight(
					Heightmap.Types.MOTION_BLOCKING, x, z) + 4);
		}
		require(helper, targetY + 4 < level.getMaxBuildHeight(),
				"Buried fixture has no safe height above the current surface");

		BlockPos snow = new BlockPos(firstX, targetY, z);
		BlockPos ice = snow.east();
		for (BlockPos target : new BlockPos[] { snow, ice }) {
			clearAbove(level, target);
			level.setBlock(target.below(), Blocks.STONE.defaultBlockState(), 3);
		}
		level.setBlock(snow, Blocks.SNOW.defaultBlockState(), 3);
		level.setBlock(ice, Blocks.ICE.defaultBlockState(), 3);
		for (BlockPos target : new BlockPos[] { snow, ice }) {
			for (int offset = 1; offset <= 3; offset++) {
				level.setBlock(target.above(offset),
						Blocks.STONE.defaultBlockState(), 3);
			}
		}

		requireConfiguredWeatherOverride(helper);
		assertOutsideWeatherScan(helper, chunk, snow, "buried Snow");
		assertOutsideWeatherScan(helper, chunk, ice, "buried Ice");
		postChunkLoad(helper, chunk);
		require(helper, level.getBlockState(snow).is(Blocks.SNOW)
				&& level.getBlockState(ice).is(Blocks.ICE),
				"OreSpawn broadly rewrote authored weather products outside its "
						+ "surface scan: buriedSnow=" + level.getBlockState(snow)
						+ ", buriedIce=" + level.getBlockState(ice));
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 24000)
	public static void naturallyGeneratedWeatherProductsStayConvertedAcrossReload(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel().getServer()
				.getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				holder -> holder.unwrapKey()
						.map(key -> key.location().equals(ICE_CREAM_TUNDRA))
						.orElse(false),
				new BlockPos(0, 64, 0), 16384, 8);
		require(helper, match != null,
				"Could not locate " + ICE_CREAM_TUNDRA
						+ " within 16,384 blocks");

		NaturalWeatherAudit audit = auditNaturalWeather(
				level, match.getFirst(), 48);
		LOGGER.info("Natural weather-material audit: anchor={}, {}",
				match.getFirst(), audit);
		require(helper, audit.tundraColumns() >= 256,
				"Natural weather audit did not sample enough Ice-Cream Tundra columns: "
						+ audit);
		require(helper, audit.surfaceIcingLayers() >= 64,
				"Natural Ice-Cream Tundra did not retain enough converted surface Snow to distinguish it from authored features: "
						+ audit);
		require(helper, audit.surfaceFrozenLemonade() >= 64
				&& audit.frozenLemonadeEdges() >= 16,
				"Natural Ice-Cream Tundra did not retain enough surface Frozen Lemonade adjoining Lemonade to distinguish converted Ice from authored features: "
						+ audit);
		require(helper, audit.vanillaSnow() == 0
				&& audit.vanillaIce() == 0,
				"Natural Ice-Cream Tundra retained vanilla weather products in OreSpawn's surface scan: "
						+ audit);
		helper.succeed();
	}

	private static NaturalWeatherAudit auditNaturalWeather(ServerLevel level,
			BlockPos centre, int radius) {
		int tundraColumns = 0;
		int surfaceIcingLayers = 0;
		int surfaceFrozenLemonade = 0;
		int frozenLemonadeEdges = 0;
		int vanillaSnow = 0;
		int vanillaIce = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				BlockPos column = new BlockPos(x, 64, z);
				ChunkAccess chunk = level.getChunkAt(column);
				int top = chunk.getHeight(Heightmap.Types.MOTION_BLOCKING,
						x & 15, z & 15);
				BlockPos biomePos = new BlockPos(x,
						Math.max(level.getMinBuildHeight(), Math.min(
								level.getMaxBuildHeight() - 1, top)), z);
				if (!level.getBiome(biomePos).unwrapKey()
						.map(key -> key.location().equals(ICE_CREAM_TUNDRA))
						.orElse(false)) {
					continue;
				}
				tundraColumns++;
				for (int y = top + 1; y >= top - 2; y--) {
					BlockPos scanPos = new BlockPos(x, y, z);
					BlockState state = level.getBlockState(
							scanPos);
					if (state.is(Blocks.SNOW)) {
						vanillaSnow++;
					} else if (state.is(Blocks.ICE)) {
						vanillaIce++;
					} else if (state.is(
							CakeWorldBlocks.ICING_LAYER.get())) {
						surfaceIcingLayers++;
					} else if (state.is(
							CakeWorldBlocks.FROZEN_LEMONADE.get())) {
						surfaceFrozenLemonade++;
						if (hasAdjacentLemonade(level, scanPos)) {
							frozenLemonadeEdges++;
						}
					}
				}
			}
		}
		return new NaturalWeatherAudit(tundraColumns,
				surfaceIcingLayers, surfaceFrozenLemonade,
				frozenLemonadeEdges, vanillaSnow, vanillaIce);
	}

	private static boolean hasAdjacentLemonade(ServerLevel level,
			BlockPos pos) {
		return level.getBlockState(pos.north())
				.is(CakeWorldFluids.LEMONADE_BLOCK.get())
				|| level.getBlockState(pos.south())
						.is(CakeWorldFluids.LEMONADE_BLOCK.get())
				|| level.getBlockState(pos.east())
						.is(CakeWorldFluids.LEMONADE_BLOCK.get())
				|| level.getBlockState(pos.west())
						.is(CakeWorldFluids.LEMONADE_BLOCK.get());
	}

	private static void requireConfiguredWeatherOverride(GameTestHelper helper) {
		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElse(null);
		require(helper, profile != null,
				"OreSpawn has no active profile for the weather fixture");
		JsonObject materials = profile.toJson()
				.getAsJsonObject("dimension_materials");
		require(helper, materials != null,
				"Active OreSpawn profile has no dimension materials");
		JsonObject overworld = materials
				.getAsJsonObject("cakeworld:overworld_materials");
		require(helper, overworld != null
				&& overworld.has("enabled")
				&& overworld.has("dimension")
				&& overworld.has("snow_block")
				&& overworld.has("ice_block")
				&& overworld.get("enabled").getAsBoolean()
				&& "minecraft:overworld".equals(
						overworld.get("dimension").getAsString())
				&& "cakeworld:icing_layer".equals(
						overworld.get("snow_block").getAsString())
				&& "cakeworld:frozen_lemonade".equals(
						overworld.get("ice_block").getAsString()),
				"CakeWorld's Overworld snow/ice material override is not active: "
						+ overworld);
	}

	private static void clearAbove(ServerLevel level, BlockPos target) {
		for (int y = target.getY(); y < level.getMaxBuildHeight(); y++) {
			level.setBlock(new BlockPos(target.getX(), y, target.getZ()),
					Blocks.AIR.defaultBlockState(), 3);
		}
	}

	private static void postChunkLoad(GameTestHelper helper, ChunkAccess chunk) {
		ChunkEvent.Load event = new ChunkEvent.Load(chunk);
		require(helper, event.getWorld() == helper.getLevel(),
				"Chunk-load fixture did not retain its ServerLevel");
		MinecraftForge.EVENT_BUS.post(event);
	}

	private static void assertInWeatherScan(GameTestHelper helper,
			ChunkAccess chunk, BlockPos target, String name) {
		int top = motionBlockingTop(chunk, target);
		int offset = top - target.getY();
		require(helper, offset >= -1 && offset <= 2,
				name + " is outside OreSpawn's exact top+1/top/top-1/top-2 scan: "
						+ "targetY=" + target.getY() + ", top=" + top);
	}

	private static void assertOutsideWeatherScan(GameTestHelper helper,
			ChunkAccess chunk, BlockPos target, String name) {
		int top = motionBlockingTop(chunk, target);
		int offset = top - target.getY();
		require(helper, offset < -1 || offset > 2,
				name + " unexpectedly falls inside OreSpawn's surface scan: "
						+ "targetY=" + target.getY() + ", top=" + top);
	}

	private static int motionBlockingTop(ChunkAccess chunk, BlockPos target) {
		return chunk.getHeight(Heightmap.Types.MOTION_BLOCKING,
				target.getX() & 15, target.getZ() & 15);
	}

	private static void assertNothingHigher(GameTestHelper helper,
			BlockPos target, String name) {
		ServerLevel level = helper.getLevel();
		for (int y = target.getY() + 1;
				y < level.getMaxBuildHeight(); y++) {
			BlockState state = level.getBlockState(
					new BlockPos(target.getX(), y, target.getZ()));
			require(helper, state.isAir(),
					name + " column has a block above the fixture at y=" + y
							+ " that can affect the heightmap: " + state);
		}
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private record NaturalWeatherAudit(int tundraColumns,
			int surfaceIcingLayers, int surfaceFrozenLemonade,
			int frozenLemonadeEdges, int vanillaSnow, int vanillaIce) {
	}
}
