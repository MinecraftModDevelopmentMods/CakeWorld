package zone.moddev.mc.cakeworld.gametest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import org.slf4j.Logger;
import zone.moddev.mc.orespawn.api.CompiledOrePattern;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OrePatternType;
import zone.moddev.mc.orespawn.api.OrePlacementContext;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.OreSpawnPatternRegistry;

import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldOrePatterns;

/** Public custom-pattern codec, shape, hot-path and integrated Sampler proof. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_layer_cake")
public final class LayerCakePatternGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation SAMPLER_TEMPLATE =
			new ResourceLocation("cakeworld", "sampler_platter");
	private static final ResourceLocation RULE_ID =
			new ResourceLocation("cakeworld", "ore/layer_cake");
	private static final ChunkPos SURVEY_ORIGIN = new ChunkPos(704, 704);
	private static final int SURVEY_SIDE = 4;
	private static final int BENCHMARK_ITERATIONS = 20_000;
	private static final Logger LOGGER = LogUtils.getLogger();

	private LayerCakePatternGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 1200)
	public static void layerCakeCodecShapeAndSamplerPlacement(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.samplerPlatterEvidence"),
				"Layer Cake proof requires the explicitly selected Sampler");
		OrePatternType type = OreSpawnPatternRegistry.registry().getValue(
				CakeWorldOrePatterns.LAYER_CAKE_ID);
		require(helper, type != null,
				"cakeworld:layer_cake was not registered through OreSpawn");

		JsonObject settings = layerCakeSettings(3, 2, 1, 1);
		CompiledOrePattern compiled = type.decode(settings);
		RecordingContext zeroBudget = new RecordingContext(0);
		require(helper, !compiled.place(zeroBudget)
				&& zeroBudget.positions.isEmpty(),
				"Layer Cake ignored a zero placement budget");
		RecordingContext recording = new RecordingContext(39);
		require(helper, compiled.place(recording)
				&& recording.positions.size() == 39
				&& recording.yCounts.size() == 3
				&& recording.yCounts.getOrDefault(-2, 0) == 13
				&& recording.yCounts.getOrDefault(0, 0) == 13
				&& recording.yCounts.getOrDefault(2, 0) == 13
				&& recording.maximumHorizontalDistanceSquared == 4,
				"Decoded Layer Cake shape drifted: positions="
						+ recording.positions.size() + ", y="
						+ recording.yCounts + ", radiusSquared="
						+ recording.maximumHorizontalDistanceSquared);

		boolean usefulInvalidPath = false;
		try {
			type.decode(layerCakeSettings(3, 0, 1, 1));
		} catch (IllegalArgumentException exception) {
			String message = exception.getMessage();
			usefulInvalidPath = message != null
					&& (message.contains("radius")
							|| message.contains("[1:8]"));
		}
		require(helper, usefulInvalidPath,
				"Invalid Layer Cake radius did not fail with a useful codec path");

		OrePatternType precisionType = OreSpawnPatternRegistry.registry()
				.getValue(new ResourceLocation("orespawn", "precision"));
		require(helper, precisionType != null,
				"Built-in precision comparison pattern is unavailable");
		JsonObject precisionSettings = new JsonObject();
		precisionSettings.addProperty("spread", 2);
		precisionSettings.addProperty("vertical_spread", 2);
		precisionSettings.addProperty("node_size", 1);
		precisionSettings.addProperty("length", 1);
		CompiledOrePattern precision = precisionType.decode(
				precisionSettings);
		BenchmarkResult layerBenchmark = benchmark(compiled);
		BenchmarkResult precisionBenchmark = benchmark(precision);
		require(helper,
				layerBenchmark.calls == BENCHMARK_ITERATIONS * 39L
						&& precisionBenchmark.calls
								== BENCHMARK_ITERATIONS * 39L,
				"Compiled-pattern benchmark did not preserve exact budgets");

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate()
				.filter(SAMPLER_TEMPLATE::equals).isPresent(),
				"Layer Cake runtime did not select the Sampler Platter");
		JsonObject rule = profile.toJson().getAsJsonObject("ores")
				.getAsJsonObject(RULE_ID.toString());
		requireLayerCakeRule(helper, rule);

		ServerLevel level = helper.getLevel();
		Block mint = CakeWorldBlocks.MINT_CRYSTAL.get();
		Block fizz = CakeWorldBlocks.FIZZY_PEARL.get();
		Block richSprinkles = CakeWorldBlocks.RICH_SPRINKLE_CLUSTER.get();
		int mintCount = 0;
		int fizzCount = 0;
		int richCount = 0;
		for (int x = 0; x < SURVEY_SIDE; x++) {
			for (int z = 0; z < SURVEY_SIDE; z++) {
				ChunkPos chunk = new ChunkPos(SURVEY_ORIGIN.x + x,
						SURVEY_ORIGIN.z + z);
				mintCount += countBlock(level, chunk, mint, -40, -20);
				fizzCount += countBlock(level, chunk, fizz, -40, -20);
				richCount += countBlock(level, chunk, richSprinkles,
						-40, -20);
			}
		}
		int total = mintCount + fizzCount + richCount;
		require(helper, total > 500 && mintCount > fizzCount
				&& fizzCount > richCount,
				"Integrated Layer Cake/flavour outputs were not visible: mint="
						+ mintCount + ", fizz=" + fizzCount + ", rich="
						+ richCount + ", total=" + total);
		LOGGER.info("Layer Cake audit: shape=39, yLevels={}, survey={}x{} from {}, mint={}, fizz={}, rich={}, total={}, layerNanos={}, precisionNanos={}, timeRatio={}",
				recording.yCounts, SURVEY_SIDE, SURVEY_SIDE, SURVEY_ORIGIN,
				mintCount, fizzCount, richCount, total,
				layerBenchmark.nanos, precisionBenchmark.nanos,
				layerBenchmark.nanos / (double) precisionBenchmark.nanos);
		helper.succeed();
	}

	private static JsonObject layerCakeSettings(int layers, int radius,
			int thickness, int layerGap) {
		JsonObject settings = new JsonObject();
		settings.addProperty("layers", layers);
		settings.addProperty("radius", radius);
		settings.addProperty("thickness", thickness);
		settings.addProperty("layer_gap", layerGap);
		return settings;
	}

	private static BenchmarkResult benchmark(CompiledOrePattern pattern) {
		BenchmarkContext context = new BenchmarkContext(39);
		for (int index = 0; index < 2_000; index++) pattern.place(context);
		context.calls = 0L;
		long start = System.nanoTime();
		for (int index = 0; index < BENCHMARK_ITERATIONS; index++) {
			pattern.place(context);
		}
		return new BenchmarkResult(System.nanoTime() - start, context.calls);
	}

	private static void requireLayerCakeRule(GameTestHelper helper,
			JsonObject rule) {
		require(helper, rule != null && rule.get("enabled").getAsBoolean()
				&& !rule.get("retrogen").getAsBoolean()
				&& rule.get("deep_output_max_y").getAsInt() == -64,
				"Sampler Layer Cake rule is missing or inert");
		JsonArray outputs = rule.getAsJsonArray("outputs");
		JsonObject dimension = rule.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:overworld");
		JsonObject pattern = dimension.getAsJsonObject("pattern");
		JsonObject settings = pattern.getAsJsonObject("settings");
		require(helper, outputs.size() == 3
				&& "cakeworld:mint_crystal".equals(
						outputs.get(0).getAsJsonObject().get("block")
								.getAsString())
				&& outputs.get(0).getAsJsonObject().get("weight")
						.getAsDouble() == 3.0D
				&& outputs.get(1).getAsJsonObject().get("weight")
						.getAsDouble() == 2.0D
				&& outputs.get(2).getAsJsonObject().get("weight")
						.getAsDouble() == 1.0D
				&& "cakeworld:layer_cake".equals(
						pattern.get("type").getAsString())
				&& settings.get("layers").getAsInt() == 3
				&& settings.get("radius").getAsInt() == 2
				&& settings.get("thickness").getAsInt() == 1
				&& settings.get("layer_gap").getAsInt() == 1
				&& dimension.get("quantity").getAsInt() == 39,
				"Layer Cake settings or flavour outputs drifted");
	}

	private static int countBlock(ServerLevel level, ChunkPos chunk,
			Block target, int minY, int maxY) {
		level.getChunk(chunk.x, chunk.z);
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		int count = 0;
		for (int x = chunk.getMinBlockX(); x <= chunk.getMaxBlockX(); x++) {
			for (int z = chunk.getMinBlockZ(); z <= chunk.getMaxBlockZ(); z++) {
				for (int y = minY; y <= maxY; y++) {
					cursor.set(x, y, z);
					if (level.getBlockState(cursor).is(target)) count++;
				}
			}
		}
		return count;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private static class BenchmarkContext implements OrePlacementContext {
		private final Random random = new Random(1L);
		private final int quantity;
		private long calls;

		private BenchmarkContext(int quantity) {
			this.quantity = quantity;
		}

		@Override public Random random() { return random; }
		@Override public int originX() { return 0; }
		@Override public int originY() { return 0; }
		@Override public int originZ() { return 0; }
		@Override public int minY() { return -64; }
		@Override public int maxY() { return 320; }
		@Override public int quantity() { return quantity; }
		@Override public int spread() { return 2; }
		@Override public int verticalSpread() { return 2; }
		@Override public int nodeSize() { return 1; }
		@Override public boolean inside(int x, int y, int z) { return true; }
		@Override public boolean isFluid(int x, int y, int z, Fluid fluid) {
			return false;
		}
		@Override public boolean tryPlace(int x, int y, int z) {
			calls++;
			return true;
		}
	}

	private static final class RecordingContext extends BenchmarkContext {
		private final Set<Long> positions = new HashSet<>();
		private final Map<Integer, Integer> yCounts = new HashMap<>();
		private int maximumHorizontalDistanceSquared;

		private RecordingContext(int quantity) {
			super(quantity);
		}

		@Override
		public boolean tryPlace(int x, int y, int z) {
			long position = BlockPos.asLong(x, y, z);
			if (!positions.add(position)) return false;
			yCounts.merge(y, 1, Integer::sum);
			maximumHorizontalDistanceSquared = Math.max(
					maximumHorizontalDistanceSquared, x * x + z * z);
			return true;
		}
	}

	private static final class BenchmarkResult {
		private final long nanos;
		private final long calls;

		private BenchmarkResult(long nanos, long calls) {
			this.nanos = nanos;
			this.calls = calls;
		}
	}
}
