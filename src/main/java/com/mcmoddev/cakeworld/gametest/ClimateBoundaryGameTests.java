package com.mcmoddev.cakeworld.gametest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Inclusive-edge and nearest-outside proof for every production climate range. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_climate_boundaries")
public final class ClimateBoundaryGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation TEMPLATE =
			new ResourceLocation("cakeworld", "climate_boundaries");
	private static final List<Integer> EXPECTED_OUTPUTS_PER_RANGE = List.of(
			18, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1);
	private static final Logger LOGGER = LogUtils.getLogger();

	private ClimateBoundaryGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void everyClimateRangeIncludesEdgesAndRejectsOutside(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.climateBoundaryFixture"),
				"Climate-boundary fixture was not explicitly enabled");
		require(helper, ClimateBoundaryFixture.wasInstalled(),
				"Climate checkerboard was not installed before OreSpawn wrapping");
		require(helper, ClimateBoundaryFixture.RANGES.size() == 17
				&& ClimateBoundaryFixture.cases().size() == 102,
				"Climate fixture must retain 17 ranges and 102 exact cases");

		Map<ClimateBoundaryFixture.RangeSpec, Set<ResourceLocation>> production =
				productionRanges(helper);
		require(helper, production.keySet().equals(
				new HashSet<>(ClimateBoundaryFixture.RANGES)),
				"Production climate range set drifted: " + production.keySet());
		Set<ResourceLocation> allOutputs = new HashSet<>();
		for (int index = 0; index < ClimateBoundaryFixture.RANGES.size(); index++) {
			ClimateBoundaryFixture.RangeSpec range =
					ClimateBoundaryFixture.RANGES.get(index);
			Set<ResourceLocation> outputs = production.get(range);
			require(helper, outputs != null
					&& outputs.size() == EXPECTED_OUTPUTS_PER_RANGE.get(index),
					"Production output count drifted for range " + index
							+ ": " + outputs);
			allOutputs.addAll(outputs);
		}
		require(helper, allOutputs.size() == 35,
				"Expected all 35 production biome outputs; got "
						+ allOutputs.size());

		GeologyProfileView active = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, active.selectedTemplate()
				.filter(TEMPLATE::equals).isPresent(),
				"Climate proof did not select " + TEMPLATE);
		JsonObject palettes = active.toJson().getAsJsonObject("biome_palettes");
		require(helper, palettes != null && palettes.size() == 17,
				"Climate proof must bake exactly 17 isolated palettes");
		List<ResourceLocation> expectedOutputs = new ArrayList<>();
		for (int index = 0; index < ClimateBoundaryFixture.RANGES.size(); index++) {
			expectedOutputs.add(assertActivePalette(helper, palettes, index));
		}

		Registry<Biome> registry = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ChunkGenerator generator = helper.getLevel().getChunkSource()
				.getGenerator();
		Map<Integer, Integer> selectedByRange = new HashMap<>();
		int sourceIndex = 0;
		for (ClimateBoundaryFixture.FixtureCase fixtureCase
				: ClimateBoundaryFixture.cases()) {
			require(helper, registry.containsKey(fixtureCase.id()),
					"Climate source biome is not registered: " + fixtureCase.id());
			Holder<Biome> actualHolder = generator.getNoiseBiome(
					sourceIndex << 2, 16, 0);
			ResourceLocation actual = actualHolder.unwrapKey()
					.map(key -> key.location())
					.orElseGet(() -> registry.getKey(actualHolder.value()));
			ResourceLocation expected = fixtureCase.shouldSelect()
					? expectedOutputs.get(fixtureCase.rangeIndex())
					: fixtureCase.id();
			require(helper, expected.equals(actual),
					"Climate case " + sourceIndex + " expected " + expected
							+ " but got " + actual);
			if (fixtureCase.shouldSelect()) {
				selectedByRange.merge(fixtureCase.rangeIndex(), 1, Integer::sum);
			}
			sourceIndex++;
		}
		require(helper, selectedByRange.size() == 17
				&& selectedByRange.values().stream()
						.allMatch(count -> count == 2),
				"Every range must select both inclusive corners: "
						+ selectedByRange);
		LOGGER.info("Climate boundary audit passed: productionOutputs={}, ranges={}, exactCases={}, inclusiveSelections={}",
				allOutputs.size(), production.size(), sourceIndex,
				selectedByRange.values().stream().mapToInt(Integer::intValue).sum());
		helper.succeed();
	}

	private static ResourceLocation assertActivePalette(GameTestHelper helper,
			JsonObject palettes, int index) {
		ClimateBoundaryFixture.RangeSpec range =
				ClimateBoundaryFixture.RANGES.get(index);
		String paletteId = String.format(
				"cakeworld_fixture:climate_range_%02d", index);
		JsonObject palette = palettes.getAsJsonObject(paletteId);
		require(helper, palette != null
				&& "minecraft:overworld".equals(
						palette.get("dimension").getAsString())
				&& "replace".equals(palette.get("mode").getAsString())
				&& "selected_namespaces".equals(
						palette.get("scope").getAsString())
				&& palette.get("coverage").getAsDouble() == 1.0D
				&& palette.get("fallback_weight").getAsDouble() == 0.0D,
				"Climate palette contract drifted: " + paletteId);
		Set<String> includedNamespaces = strings(
				palette.getAsJsonArray("include_namespaces"));
		require(helper, includedNamespaces.equals(
				Set.of(ClimateBoundaryFixture.NAMESPACE)),
				"Climate palette namespace drifted: " + paletteId);

		JsonObject biomes = palette.getAsJsonObject("biomes");
		require(helper, biomes != null && biomes.size() == 1,
				"Climate palette must have exactly one output: " + paletteId);
		Map.Entry<String, JsonElement> outputEntry =
				biomes.entrySet().iterator().next();
		JsonObject placement = outputEntry.getValue().getAsJsonObject();
		require(helper,
				Float.compare(placement.get("min_temperature").getAsFloat(),
						range.minTemperature()) == 0
				&& Float.compare(placement.get("max_temperature").getAsFloat(),
						range.maxTemperature()) == 0
				&& Float.compare(placement.get("min_downfall").getAsFloat(),
						range.minDownfall()) == 0
				&& Float.compare(placement.get("max_downfall").getAsFloat(),
						range.maxDownfall()) == 0,
				"Climate placement range drifted: " + paletteId);
		Set<String> expectedSources = new HashSet<>();
		for (ClimateBoundaryFixture.FixtureCase fixtureCase
				: ClimateBoundaryFixture.cases()) {
			if (fixtureCase.rangeIndex() == index) {
				expectedSources.add(fixtureCase.id().toString());
			}
		}
		require(helper, strings(placement.getAsJsonArray("similar_biomes"))
				.equals(expectedSources)
				&& strings(placement.getAsJsonArray("required_similar_biomes"))
						.equals(expectedSources),
				"Climate source set drifted: " + paletteId);
		return new ResourceLocation(outputEntry.getKey());
	}

	private static Map<ClimateBoundaryFixture.RangeSpec, Set<ResourceLocation>>
			productionRanges(GameTestHelper helper) {
		JsonObject provider = packagedProvider(helper);
		JsonObject templates = provider.getAsJsonObject("templates");
		JsonObject adventure = templates.getAsJsonObject(
				"cakeworld:edible_world");
		JsonObject palettes = adventure.getAsJsonObject("profile")
				.getAsJsonObject("biome_palettes");
		Map<ClimateBoundaryFixture.RangeSpec, Set<ResourceLocation>> result =
				new HashMap<>();
		for (Map.Entry<String, JsonElement> paletteEntry
				: palettes.entrySet()) {
			JsonObject biomes = paletteEntry.getValue().getAsJsonObject()
					.getAsJsonObject("biomes");
			for (Map.Entry<String, JsonElement> biomeEntry : biomes.entrySet()) {
				JsonObject placement = biomeEntry.getValue().getAsJsonObject();
				ClimateBoundaryFixture.RangeSpec range =
						new ClimateBoundaryFixture.RangeSpec(
								placement.get("min_temperature").getAsFloat(),
								placement.get("max_temperature").getAsFloat(),
								placement.get("min_downfall").getAsFloat(),
								placement.get("max_downfall").getAsFloat());
				result.computeIfAbsent(range, ignored -> new HashSet<>())
						.add(new ResourceLocation(biomeEntry.getKey()));
			}
		}
		return result;
	}

	private static JsonObject packagedProvider(GameTestHelper helper) {
		InputStream stream = ClimateBoundaryGameTests.class.getClassLoader()
				.getResourceAsStream("data/cakeworld/orespawn/provider.json");
		require(helper, stream != null,
				"Packaged CakeWorld provider resource is missing");
		try (InputStream input = stream;
				InputStreamReader reader = new InputStreamReader(input,
						StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception error) {
			throw new IllegalStateException(
					"Could not read packaged CakeWorld provider", error);
		}
	}

	private static Set<String> strings(Iterable<JsonElement> values) {
		Set<String> result = new HashSet<>();
		for (JsonElement value : values) result.add(value.getAsString());
		return result;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
