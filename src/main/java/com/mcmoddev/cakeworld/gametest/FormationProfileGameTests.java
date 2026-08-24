package com.mcmoddev.cakeworld.gametest;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.GeologyColumn;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.GeologySampler;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Same-seed evidence for the adventure and diagnostic formation profiles. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_formation")
public final class FormationProfileGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation ADVENTURE_TEMPLATE =
			new ResourceLocation("cakeworld", "edible_world");
	private static final ResourceLocation SAMPLER_TEMPLATE =
			new ResourceLocation("cakeworld", "sampler_platter");
	private static final Logger LOGGER = LogUtils.getLogger();

	private FormationProfileGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 400)
	public static void fixedSeedFormationProfileIsLive(GameTestHelper helper) {
		String expected = System.getProperty(
				"cakeworld.expectedFormationAlgorithm", "").trim();
		require(helper, "stable_layers".equals(expected)
					|| "sky_v1".equals(expected),
				"Formation evidence requires an explicit expected algorithm");

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		JsonObject root = profile.toJson();
		JsonObject formations = root.getAsJsonObject("formations");
		require(helper, expected.equals(
				formations.get("algorithm").getAsString()),
				"Active formation algorithm did not match the evidence run");
		ResourceLocation expectedTemplate = "sky_v1".equals(expected)
				? SAMPLER_TEMPLATE : ADVENTURE_TEMPLATE;
		require(helper, profile.selectedTemplate()
				.filter(expectedTemplate::equals).isPresent(),
				"Formation evidence selected the wrong world template");

		if ("sky_v1".equals(expected)) {
			requireExtremeSettings(helper, formations);
		} else {
			require(helper,
					"average".equals(formations.get("horizontal_size")
							.getAsString())
							&& "average".equals(formations
									.get("vertical_thickness").getAsString())
							&& "average".equals(formations.get("waviness")
									.getAsString())
							&& "average".equals(formations
									.get("edge_irregularity").getAsString())
							&& "average".equals(formations
									.get("formation_continuity").getAsString()),
					"Adventure formation baseline drifted from average presets");
		}

		GeologySampler sampler = OreSpawnApi.createSampler(helper.getLevel())
				.orElseThrow(() -> new AssertionError(
						"OreSpawn did not expose its active geology sampler"));
		long started = System.nanoTime();
		FormationSurvey survey = survey(sampler);
		long elapsedMillis = (System.nanoTime() - started) / 1_000_000L;
		require(helper, survey.columns == 1089
					&& survey.samplePoints == 70785,
				"Formation survey dimensions drifted");
		require(helper, survey.rockCounts.size() >= 3
					&& survey.geomeCounts.size() >= 2
					&& survey.verticalTransitions > 0
					&& survey.horizontalTransitions > 0,
				"Active formation profile did not produce varied live geology: "
						+ survey);
		if ("sky_v1".equals(expected)) {
			require(helper, survey.verticalTransitions == 36515
						&& survey.horizontalTransitions == 1176
						&& survey.rockCounts.size() == 7
						&& survey.geomeCounts.size() == 14
						&& survey.signature == 2804912711855593311L,
					"Fixed-seed extreme sky_v1 signature drifted: " + survey);
		} else {
			require(helper, survey.verticalTransitions == 5393
						&& survey.horizontalTransitions == 634
						&& survey.rockCounts.size() == 7
						&& survey.geomeCounts.size() == 5
						&& survey.signature == 6720209891956171365L,
					"Fixed-seed stable_layers signature drifted: " + survey);
		}
		LOGGER.info("CakeWorld formation survey algorithm={} template={} columns={} samples={} verticalTransitions={} horizontalTransitions={} distinctRocks={} distinctGeomes={} rockCounts={} geomeCounts={} familyCounts={} signature={} elapsedMs={}",
				expected, expectedTemplate, survey.columns, survey.samplePoints,
				survey.verticalTransitions, survey.horizontalTransitions,
				survey.rockCounts.size(), survey.geomeCounts.size(),
				survey.rockCounts, survey.geomeCounts, survey.familyCounts,
				Long.toUnsignedString(survey.signature), elapsedMillis);
		helper.succeed();
	}

	private static FormationSurvey survey(GeologySampler sampler) {
		FormationSurvey result = new FormationSurvey();
		ResourceLocation[][] zeroLevel = new ResourceLocation[33][33];
		int gridX = 0;
		for (int x = -512; x <= 512; x += 32) {
			int gridZ = 0;
			for (int z = -512; z <= 512; z += 32) {
				GeologyColumn column = sampler.sampleColumn(x, z, 96);
				result.columns++;
				result.geomeCounts.merge(column.geome(), 1, Integer::sum);
				ResourceLocation previous = null;
				for (int y = -48; y <= 80; y += 2) {
					Block rock = column.rockAt(y).getBlock();
					ResourceLocation rockId = Registry.BLOCK.getKey(rock);
					result.samplePoints++;
					result.rockCounts.merge(rockId, 1, Integer::sum);
					column.familyAt(y).ifPresent(family -> result.familyCounts
							.merge(family.name(), 1, Integer::sum));
					if (previous != null && !previous.equals(rockId)) {
						result.verticalTransitions++;
					}
					previous = rockId;
					result.signature ^= rockId.toString().hashCode();
					result.signature *= 1099511628211L;
					if (y == 0) {
						zeroLevel[gridX][gridZ] = rockId;
					}
				}
				gridZ++;
			}
			gridX++;
		}
		for (int x = 0; x < zeroLevel.length; x++) {
			for (int z = 0; z < zeroLevel[x].length; z++) {
				if (x > 0 && !zeroLevel[x][z]
						.equals(zeroLevel[x - 1][z])) {
					result.horizontalTransitions++;
				}
				if (z > 0 && !zeroLevel[x][z]
						.equals(zeroLevel[x][z - 1])) {
					result.horizontalTransitions++;
				}
			}
		}
		return result;
	}

	private static void requireExtremeSettings(GameTestHelper helper,
			JsonObject formations) {
		for (String key : new String[] { "horizontal_size",
				"vertical_thickness", "waviness", "edge_irregularity",
				"formation_continuity" }) {
			require(helper, "custom".equals(
					formations.get(key).getAsString()),
					"Extreme formation profile lost custom preset " + key);
		}
		JsonObject custom = formations.getAsJsonObject("custom");
		require(helper,
				custom.get("stratum_wavelength").getAsDouble() == 32.0
						&& custom.get("family_region_wavelength")
								.getAsDouble() == 8192.0
						&& custom.get("vertical_thickness").getAsInt() == 1
						&& custom.get("waviness_wavelength")
								.getAsDouble() == 32.0
						&& custom.get("waviness_amplitude")
								.getAsDouble() == 512.0
						&& custom.get("edge_wavelength").getAsDouble() == 8.0
						&& custom.get("edge_amplitude").getAsDouble() == 256.0
						&& custom.get("edge_octaves").getAsInt() == 8
						&& custom.get("continuity").getAsDouble() == 0.0,
				"Extreme formation values did not survive profile serialization");
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private static final class FormationSurvey {
		private int columns;
		private int samplePoints;
		private int verticalTransitions;
		private int horizontalTransitions;
		private long signature = 1469598103934665603L;
		private final Map<ResourceLocation, Integer> rockCounts =
				new LinkedHashMap<>();
		private final Map<ResourceLocation, Integer> geomeCounts =
				new LinkedHashMap<>();
		private final Map<String, Integer> familyCounts =
				new LinkedHashMap<>();

		@Override
		public String toString() {
			return "FormationSurvey[columns=" + columns + ", samples="
					+ samplePoints + ", verticalTransitions="
					+ verticalTransitions + ", horizontalTransitions="
					+ horizontalTransitions + ", rocks=" + rockCounts
					+ ", geomes=" + geomeCounts + "]";
		}
	}
}
