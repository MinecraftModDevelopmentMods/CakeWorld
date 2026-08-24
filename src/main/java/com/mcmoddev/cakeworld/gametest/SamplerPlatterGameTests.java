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
import net.minecraft.world.level.Level;
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
	private static final ResourceLocation FUDGE_WASTES =
			new ResourceLocation("cakeworld", "fudge_wastes");
	private static final ResourceLocation MERINGUE_ISLANDS =
			new ResourceLocation("cakeworld", "meringue_islands");
	private static final Logger LOGGER = LogUtils.getLogger();

	private SamplerPlatterGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void packagedSamplerIsOptionalAndBounded(
			GameTestHelper helper) {
		JsonObject provider = packagedProvider(helper);
		require(helper, provider.get("provider_revision").getAsInt() >= 48,
				"Sampler namespace plots require provider revision 48");
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
		JsonObject palettes = profile.getAsJsonObject("biome_palettes");
		require(helper, palettes.size() == 3,
				"Sampler namespace checkpoint must expose three plots");
		JsonObject palette = palettes
				.getAsJsonObject("cakeworld:sampler_overworld_augment");
		requireSamplerPalette(helper, palette);
		requireSelectedNamespacesPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_nether_selected_namespaces"));
		requireExcludedNamespacePalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_end_excluded_namespace"));
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
		JsonObject palettes = profile.toJson()
				.getAsJsonObject("biome_palettes");
		JsonObject palette = palettes
				.getAsJsonObject("cakeworld:sampler_overworld_augment");
		requireSamplerPalette(helper, palette);
		requireSelectedNamespacesPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_nether_selected_namespaces"));
		requireExcludedNamespacePalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_end_excluded_namespace"));

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

		ServerLevel nether = level.getServer().getLevel(Level.NETHER);
		ServerLevel end = level.getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Sampler namespace proof requires Nether and End levels");
		Map<ResourceLocation, Integer> netherCounts = sampleBiomes(nether,
				64, 1024);
		Map<ResourceLocation, Integer> endCounts = sampleBiomes(end, 64, 1024);
		int netherTotal = total(netherCounts);
		int fudge = netherCounts.getOrDefault(FUDGE_WASTES, 0);
		int endTotal = total(endCounts);
		int endMinecraft = namespaceTotal(endCounts, "minecraft");
		require(helper, netherTotal == 4225 && fudge == netherTotal
					&& netherCounts.size() == 1,
				"Selected minecraft namespace was not fully converted in the Nether: "
						+ netherCounts);
		require(helper, endTotal == 4225 && endMinecraft == endTotal
					&& !endCounts.containsKey(MERINGUE_ISLANDS),
				"Excluded minecraft namespace did not delegate unchanged in the End: "
						+ endCounts);
		LOGGER.info("Sampler namespace audit: netherSamples={}, fudgeWastes={}, endSamples={}, endMinecraft={}, endDistinct={}",
				netherTotal, fudge, endTotal, endMinecraft, endCounts.size());
		helper.succeed();
	}

	private static Map<ResourceLocation, Integer> sampleBiomes(
			ServerLevel level, int y, int radius) {
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Map<ResourceLocation, Integer> counts = new HashMap<>();
		for (int x = -radius; x <= radius; x += 32) {
			for (int z = -radius; z <= radius; z += 32) {
				Holder<Biome> biome = generator.getNoiseBiome(
						QuartPos.fromBlock(x), QuartPos.fromBlock(y),
						QuartPos.fromBlock(z));
				ResourceLocation id = biome.unwrapKey()
						.map(key -> key.location())
						.orElseGet(() -> registry.getKey(biome.value()));
				counts.merge(id, 1, Integer::sum);
			}
		}
		return counts;
	}

	private static int total(Map<ResourceLocation, Integer> counts) {
		return counts.values().stream().mapToInt(Integer::intValue).sum();
	}

	private static int namespaceTotal(
			Map<ResourceLocation, Integer> counts, String namespace) {
		return counts.entrySet().stream()
				.filter(entry -> namespace.equals(
						entry.getKey().getNamespace()))
				.mapToInt(Map.Entry::getValue).sum();
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

	private static void requireSelectedNamespacesPalette(
			GameTestHelper helper, JsonObject palette) {
		require(helper, palette != null
					&& "minecraft:the_nether".equals(
							palette.get("dimension").getAsString())
					&& "replace".equals(palette.get("mode").getAsString())
					&& "selected_namespaces".equals(
							palette.get("scope").getAsString())
					&& "small".equals(
							palette.get("region_size").getAsString())
					&& palette.get("coverage").getAsDouble() == 1.0
					&& palette.get("fallback_weight").getAsDouble() == 0.0,
				"Sampler selected-namespace palette controls drifted");
		require(helper, palette.getAsJsonArray("include_namespaces").size() == 1
					&& "minecraft".equals(palette
							.getAsJsonArray("include_namespaces").get(0)
							.getAsString())
					&& palette.getAsJsonArray("exclude_namespaces").size() == 0,
				"Sampler selected-namespace allow-list drifted");
		require(helper, palette.getAsJsonObject("biomes").size() == 1
					&& palette.getAsJsonObject("biomes")
							.has("cakeworld:fudge_wastes"),
				"Sampler selected-namespace output drifted");
	}

	private static void requireExcludedNamespacePalette(
			GameTestHelper helper, JsonObject palette) {
		require(helper, palette != null
					&& "minecraft:the_end".equals(
							palette.get("dimension").getAsString())
					&& "replace".equals(palette.get("mode").getAsString())
					&& "all".equals(palette.get("scope").getAsString())
					&& "average".equals(
							palette.get("region_size").getAsString())
					&& palette.get("coverage").getAsDouble() == 1.0
					&& palette.get("fallback_weight").getAsDouble() == 0.0,
				"Sampler excluded-namespace palette controls drifted");
		require(helper, palette.getAsJsonArray("include_namespaces").size() == 0
					&& palette.getAsJsonArray("exclude_namespaces").size() == 1
					&& "minecraft".equals(palette
							.getAsJsonArray("exclude_namespaces").get(0)
							.getAsString()),
				"Sampler namespace exclusion list drifted");
		require(helper, palette.getAsJsonObject("biomes").size() == 1
					&& palette.getAsJsonObject("biomes")
							.has("cakeworld:meringue_islands"),
				"Sampler excluded-namespace output drifted");
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
