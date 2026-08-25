package com.mcmoddev.cakeworld.gametest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

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
	private static final int VISUAL_MIN_XZ = -512;
	private static final int VISUAL_MAX_XZ = 512;
	private static final int VISUAL_MIN_Y = -48;
	private static final int VISUAL_MAX_Y = 80;
	private static final int CROSS_SCALE = 2;
	private static final int MAP_STEP = 8;
	private static final int MAP_SCALE = 4;

	private FormationProfileGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 400)
	public static void fixedSeedFormationProfileIsLive(GameTestHelper helper) {
		String expected = System.getProperty(
				"cakeworld.expectedFormationAlgorithm", "").trim();
		String formationCase = System.getProperty(
				"cakeworld.samplerFormationCase",
				"sky_v1".equals(expected) ? "extreme" : "baseline").trim();
		require(helper, "stable_layers".equals(expected)
					|| "sky_v1".equals(expected),
				"Formation evidence requires an explicit expected algorithm");
		require(helper, "stable_layers".equals(expected)
					? "baseline".equals(formationCase)
					: "extreme".equals(formationCase)
							|| "minimum".equals(formationCase),
				"Formation evidence received an incompatible profile case");

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

		if ("minimum".equals(formationCase)) {
			requireMinimumSettings(helper, formations);
		} else if ("sky_v1".equals(expected)) {
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
		if ("minimum".equals(formationCase)) {
			require(helper, survey.verticalTransitions == 32350
						&& survey.horizontalTransitions == 1627
						&& survey.rockCounts.size() == 8
						&& survey.geomeCounts.size() == 14
						&& survey.signature == -5967564796242673837L,
					"Fixed-seed minimum-preset sky_v1 signature drifted: "
							+ survey);
		} else if ("sky_v1".equals(expected)) {
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
		LOGGER.info("CakeWorld formation survey algorithm={} case={} template={} columns={} samples={} verticalTransitions={} horizontalTransitions={} distinctRocks={} distinctGeomes={} rockCounts={} geomeCounts={} familyCounts={} signature={} elapsedMs={}",
				expected, formationCase, expectedTemplate, survey.columns, survey.samplePoints,
				survey.verticalTransitions, survey.horizontalTransitions,
				survey.rockCounts.size(), survey.geomeCounts.size(),
				survey.rockCounts, survey.geomeCounts, survey.familyCounts,
				Long.toUnsignedString(survey.signature), elapsedMillis);
		if (Boolean.getBoolean("cakeworld.formationVisualEvidence")) {
			try {
				writeVisualEvidence(helper, sampler, expected, formationCase,
						expectedTemplate, survey);
			} catch (IOException exception) {
				String message = "Could not write formation visual evidence: "
						+ exception.getMessage();
				helper.fail(message);
				throw new IllegalStateException(message, exception);
			}
		}
		helper.succeed();
	}

	private static void writeVisualEvidence(GameTestHelper helper,
			GeologySampler sampler, String algorithm, String formationCase,
			ResourceLocation template, FormationSurvey survey)
			throws IOException {
		int crossWidth = (VISUAL_MAX_XZ - VISUAL_MIN_XZ + 1)
				* CROSS_SCALE;
		int crossHeight = (VISUAL_MAX_Y - VISUAL_MIN_Y + 1)
				* CROSS_SCALE;
		int mapSamples = (VISUAL_MAX_XZ - VISUAL_MIN_XZ) / MAP_STEP + 1;
		int mapSize = mapSamples * MAP_SCALE;
		int margin = 24;
		int titleHeight = 58;
		int crossCaptionHeight = 50;
		int gap = 24;
		int footerHeight = 32;
		int width = margin + crossWidth + margin;
		int mapY = margin + titleHeight + crossHeight
				+ crossCaptionHeight + gap;
		int height = mapY + mapSize + footerHeight + margin;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		Set<ResourceLocation> seenRocks = new TreeSet<>(
				Comparator.comparing(ResourceLocation::toString));
		try {
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			graphics.setColor(new Color(0xfffaf2));
			graphics.fillRect(0, 0, width, height);
			graphics.setColor(new Color(0x30231f));
			graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
			graphics.drawString("CakeWorld OreSpawn formation comparison",
					margin, margin + 25);
			graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
			graphics.drawString("algorithm=" + algorithm + "  case="
					+ formationCase + "  seed="
					+ helper.getLevel().getSeed(), margin, margin + 49);

			int crossY = margin + titleHeight;
			for (int x = VISUAL_MIN_XZ; x <= VISUAL_MAX_XZ; x++) {
				GeologyColumn column = sampler.sampleColumn(x, 0, 96);
				for (int y = VISUAL_MIN_Y; y <= VISUAL_MAX_Y; y++) {
					ResourceLocation rockId = Registry.BLOCK.getKey(
							column.rockAt(y).getBlock());
					seenRocks.add(rockId);
					graphics.setColor(new Color(colorFor(rockId)));
					graphics.fillRect(margin
							+ (x - VISUAL_MIN_XZ) * CROSS_SCALE,
							crossY + (VISUAL_MAX_Y - y) * CROSS_SCALE,
							CROSS_SCALE, CROSS_SCALE);
				}
			}
			graphics.setColor(new Color(0x30231f));
			graphics.drawRect(margin, crossY, crossWidth, crossHeight);
			graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
			graphics.drawString(
					"Vertical cross-section: X -512..512, Z 0, Y 80..-48",
					margin, crossY + crossHeight + 22);
			graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
			graphics.drawString("-512", margin,
					crossY + crossHeight + 43);
			graphics.drawString("0", margin + crossWidth / 2,
					crossY + crossHeight + 43);
			graphics.drawString("512", margin + crossWidth - 28,
					crossY + crossHeight + 43);

			for (int x = VISUAL_MIN_XZ, mapXIndex = 0;
					x <= VISUAL_MAX_XZ; x += MAP_STEP, mapXIndex++) {
				for (int z = VISUAL_MIN_XZ, mapZIndex = 0;
						z <= VISUAL_MAX_XZ; z += MAP_STEP, mapZIndex++) {
					GeologyColumn column = sampler.sampleColumn(x, z, 96);
					ResourceLocation rockId = Registry.BLOCK.getKey(
							column.rockAt(0).getBlock());
					seenRocks.add(rockId);
					graphics.setColor(new Color(colorFor(rockId)));
					graphics.fillRect(margin + mapXIndex * MAP_SCALE,
							mapY + mapZIndex * MAP_SCALE,
							MAP_SCALE, MAP_SCALE);
				}
			}
			graphics.setColor(new Color(0x30231f));
			graphics.drawRect(margin, mapY, mapSize, mapSize);
			graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
			graphics.drawString(
					"Top-down slice: Y 0, X/Z -512..512, sample step 8",
					margin, mapY - 8);

			int legendX = margin + mapSize + 32;
			int legendY = mapY + 20;
			graphics.drawString("Rock legend", legendX, legendY);
			graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
			for (ResourceLocation rockId : seenRocks) {
				legendY += 28;
				graphics.setColor(new Color(colorFor(rockId)));
				graphics.fillRect(legendX, legendY - 16, 20, 20);
				graphics.setColor(new Color(0x30231f));
				graphics.drawRect(legendX, legendY - 16, 20, 20);
				graphics.drawString(rockId.toString(), legendX + 30,
						legendY);
			}
			legendY += 40;
			graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
			graphics.drawString("Fixed survey metrics", legendX, legendY);
			graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
			String[] metrics = {
					"vertical transitions: " + survey.verticalTransitions,
					"horizontal transitions: " + survey.horizontalTransitions,
					"distinct rocks: " + survey.rockCounts.size(),
					"distinct geomes: " + survey.geomeCounts.size(),
					"signature: "
							+ Long.toUnsignedString(survey.signature) };
			for (String metric : metrics) {
				legendY += 23;
				graphics.drawString(metric, legendX, legendY);
			}
		} finally {
			graphics.dispose();
		}

		Path outputDirectory = Path.of("formation-visual")
				.toAbsolutePath().normalize();
		Files.createDirectories(outputDirectory);
		String baseName = algorithm + "-" + formationCase;
		Path imagePath = outputDirectory.resolve(baseName + ".png");
		if (!ImageIO.write(image, "png", imagePath.toFile())) {
			throw new IOException("No PNG writer is available");
		}
		StringBuilder manifest = new StringBuilder();
		manifest.append("CakeWorld OreSpawn formation visual evidence\n")
				.append("algorithm=").append(algorithm).append('\n')
				.append("case=").append(formationCase).append('\n')
				.append("template=").append(template).append('\n')
				.append("world_seed=").append(helper.getLevel().getSeed())
				.append('\n')
				.append("cross_section=x:-512..512,z:0,y:-48..80,step:1\n")
				.append("top_down=y:0,xz:-512..512,step:8\n")
				.append("survey_vertical_transitions=")
				.append(survey.verticalTransitions).append('\n')
				.append("survey_horizontal_transitions=")
				.append(survey.horizontalTransitions).append('\n')
				.append("survey_distinct_rocks=")
				.append(survey.rockCounts.size()).append('\n')
				.append("survey_distinct_geomes=")
				.append(survey.geomeCounts.size()).append('\n')
				.append("survey_signature=")
				.append(Long.toUnsignedString(survey.signature)).append('\n');
		for (ResourceLocation rockId : seenRocks) {
			manifest.append("color.").append(rockId).append("=#")
					.append(String.format("%06X", colorFor(rockId)))
					.append('\n');
		}
		Path manifestPath = outputDirectory.resolve(baseName + ".txt");
		Files.writeString(manifestPath, manifest, StandardCharsets.UTF_8);
		LOGGER.info("CakeWorld formation visual evidence image={} manifest={}",
				imagePath, manifestPath);
	}

	private static int colorFor(ResourceLocation rockId) {
		return switch (rockId.toString()) {
		case "cakeworld:chocolate_sponge" -> 0x8b4a2f;
		case "cakeworld:biscuit_stone" -> 0xc58d52;
		case "cakeworld:wafer_rock" -> 0xe6c77d;
		case "cakeworld:nougat_rock" -> 0xc98f78;
		case "cakeworld:peppermint_rock" -> 0xe7f3ed;
		case "cakeworld:rock_candy" -> 0x62cbdc;
		case "cakeworld:candy_glass" -> 0xc86fe8;
		case "cakeworld:fudge_rock" -> 0x4b2519;
		case "cakeworld:burnt_sugar_rock" -> 0x211720;
		case "minecraft:stone" -> 0x7f7f7f;
		default -> 0xff00ff;
		};
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

	private static void requireMinimumSettings(GameTestHelper helper,
			JsonObject formations) {
		for (String key : new String[] { "horizontal_size",
				"vertical_thickness", "waviness", "edge_irregularity",
				"formation_continuity" }) {
			require(helper, "tiny".equals(formations.get(key).getAsString()),
					"Minimum formation profile lost tiny preset " + key);
		}
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
