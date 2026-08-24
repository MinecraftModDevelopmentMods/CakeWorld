package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Focused contract proof for the explicitly selected Slice 7 sampler. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_sampler")
public final class SamplerPlatterGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation SAMPLER_TEMPLATE =
			new ResourceLocation("cakeworld", "sampler_platter");
	private static final ResourceLocation CANDY_PLAINS =
			new ResourceLocation("cakeworld", "candy_plains");
	private static final Logger LOGGER = LogUtils.getLogger();

	private SamplerPlatterGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void packagedSamplerIsOptionalAndBounded(
			GameTestHelper helper) {
		JsonObject provider = packagedProvider(helper);
		require(helper, provider.get("provider_revision").getAsInt() >= 47,
				"Sampler Platter requires provider revision 47");
		JsonObject templates = provider.getAsJsonObject("templates");
		require(helper, templates.size() == 3,
				"Generated provider must contain two adventures and one sampler");
		JsonObject sampler = templates.getAsJsonObject(
				"cakeworld:sampler_platter");
		require(helper, sampler != null,
				"Generated provider omitted cakeworld:sampler_platter");
		require(helper, !sampler.get("auto_select").getAsBoolean(),
				"Sampler Platter must never be auto-selected");
		require(helper,
				sampler.get("auto_select_priority").getAsInt() == -100,
				"Sampler Platter priority drifted from its inert diagnostic value");
		JsonObject profile = sampler.getAsJsonObject("profile");
		require(helper, !profile.get("manage_vanilla_ores").getAsBoolean()
					&& !profile.get("suppress_all_ore_features")
							.getAsBoolean(),
				"Sampler foundation must not take over vanilla ores");
		JsonObject palette = profile.getAsJsonObject("biome_palettes")
				.getAsJsonObject("cakeworld:sampler_overworld_augment");
		requireSamplerPalette(helper, palette);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void explicitlySelectedSamplerRetainsSourceBiomes(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean(
				"cakeworld.samplerPlatterEvidence"),
				"Sampler test namespace ran without the explicit evidence switch");
		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate()
				.filter(SAMPLER_TEMPLATE::equals).isPresent(),
				"Fresh diagnostic world did not explicitly select "
						+ SAMPLER_TEMPLATE);
		JsonObject palette = profile.toJson()
				.getAsJsonObject("biome_palettes")
				.getAsJsonObject("cakeworld:sampler_overworld_augment");
		requireSamplerPalette(helper, palette);

		ServerLevel level = helper.getLevel();
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Map<ResourceLocation, Integer> counts = new HashMap<>();
		for (int x = -4096; x <= 4096; x += 32) {
			for (int z = -4096; z <= 4096; z += 32) {
				Holder<Biome> biome = generator.getNoiseBiome(
						QuartPos.fromBlock(x), QuartPos.fromBlock(64),
						QuartPos.fromBlock(z));
				ResourceLocation id = biome.unwrapKey()
						.map(key -> key.location())
						.orElseGet(() -> registry.getKey(biome.value()));
				counts.merge(id, 1, Integer::sum);
			}
		}
		int candy = counts.getOrDefault(CANDY_PLAINS, 0);
		int minecraft = counts.entrySet().stream()
				.filter(entry -> "minecraft".equals(
						entry.getKey().getNamespace()))
				.mapToInt(Map.Entry::getValue).sum();
		int total = counts.values().stream().mapToInt(Integer::intValue)
				.sum();
		require(helper, total == 66049,
				"Sampler audit grid drifted from 66,049 positions");
		require(helper, candy > 0,
				"Augment sampler produced no Candy Plains output");
		require(helper, minecraft > 0,
				"Augment sampler did not retain delegated Minecraft biomes");
		require(helper, candy < total && minecraft < total,
				"Sampler collapsed into a single biome source");
		require(helper, candy == 1414 && minecraft == 64635
					&& counts.size() == 50,
				"Fixed-seed augment distribution drifted: candy=" + candy
						+ ", minecraft=" + minecraft + ", distinct="
						+ counts.size());
		LOGGER.info("Sampler Platter fixed-seed biome audit: samples={}, candyPlains={}, minecraft={}, distinct={}",
				total, candy, minecraft, counts.size());
		helper.succeed();
	}

	private static void requireSamplerPalette(GameTestHelper helper,
			JsonObject palette) {
		require(helper, palette != null,
				"Active Sampler profile omitted its Overworld palette");
		require(helper, "augment".equals(
				palette.get("mode").getAsString()),
				"Sampler must retain source biomes through augment mode");
		require(helper, "minecraft_only".equals(
				palette.get("scope").getAsString()),
				"Sampler foundation must not claim third-party biome namespaces");
		require(helper, "tiny".equals(
				palette.get("region_size").getAsString()),
				"Sampler foundation must use compact 128-block regions");
		require(helper, palette.get("coverage").getAsDouble() == 0.75,
				"Sampler partial coverage drifted");
		require(helper,
				palette.get("fallback_weight").getAsDouble() == 3.0,
				"Sampler delegated-source fallback weight drifted");
		JsonObject biomes = palette.getAsJsonObject("biomes");
		require(helper, biomes.size() == 1
					&& biomes.has("cakeworld:candy_plains"),
				"Sampler foundation must expose one readable CakeWorld plot");
		JsonObject candy = biomes.getAsJsonObject("cakeworld:candy_plains");
		require(helper, candy.get("enabled").getAsBoolean()
					&& candy.get("weight").getAsDouble() == 1.0,
				"Sampler Candy Plains enablement or weight drifted");
		require(helper, candy.getAsJsonArray("similar_biomes").size() == 1
					&& "minecraft:plains".equals(candy
							.getAsJsonArray("similar_biomes").get(0)
							.getAsString())
					&& candy.getAsJsonArray("required_similar_biomes")
							.size() == 0,
				"Sampler Candy Plains similarity contract drifted");
		require(helper,
				candy.get("min_temperature").getAsDouble() == -2.0
						&& candy.get("max_temperature").getAsDouble() == 2.0
						&& candy.get("min_downfall").getAsDouble() == 0.0
						&& candy.get("max_downfall").getAsDouble() == 1.0,
				"Sampler Candy Plains climate envelope drifted");
		JsonObject surface = candy.getAsJsonObject("surface");
		require(helper, "cakeworld:chocolate_sponge".equals(
				surface.get("top_block").getAsString())
					&& "cakeworld:wafer_rock".equals(
							surface.get("filler_block").getAsString())
					&& "cakeworld:candy_glass".equals(
							surface.get("underwater_block").getAsString())
					&& surface.get("filler_depth").getAsInt() == 2,
				"Sampler Candy Plains surface contract drifted");
	}

	private static JsonObject packagedProvider(GameTestHelper helper) {
		try (InputStreamReader reader = new InputStreamReader(
				SamplerPlatterGameTests.class.getResourceAsStream(
						"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			helper.fail("Could not read generated CakeWorld provider: "
					+ exception.getMessage());
			throw new IllegalStateException(exception);
		}
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
