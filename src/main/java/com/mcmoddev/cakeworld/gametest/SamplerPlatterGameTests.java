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
	private static final ResourceLocation COOKIE_FOREST =
			new ResourceLocation("cakeworld", "cookie_forest");
	private static final ResourceLocation SODA_OCEAN =
			new ResourceLocation("cakeworld", "soda_ocean");
	private static final ResourceLocation GINGERBREAD_HEARTHLANDS =
			new ResourceLocation("cakeworld", "gingerbread_hearthlands");
	private static final ResourceLocation PEPPERMINT_PINEWOODS =
			new ResourceLocation("cakeworld", "peppermint_pinewoods");
	private static final ResourceLocation MARSHMALLOW_PEAKS =
			new ResourceLocation("cakeworld", "marshmallow_peaks");
	private static final ResourceLocation FUDGE_WASTES =
			new ResourceLocation("cakeworld", "fudge_wastes");
	private static final ResourceLocation CHILLI_CHOCOLATE_CRAGS =
			new ResourceLocation("cakeworld", "chilli_chocolate_crags");
	private static final ResourceLocation MERINGUE_ISLANDS =
			new ResourceLocation("cakeworld", "meringue_islands");
	private static final Logger LOGGER = LogUtils.getLogger();

	private SamplerPlatterGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void packagedSamplerIsOptionalAndBounded(
			GameTestHelper helper) {
		JsonObject provider = packagedProvider(helper);
		require(helper, provider.get("provider_revision").getAsInt() >= 51,
				"Sampler formation comparison requires provider revision 51");
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
		requireExtremeFormationProfile(helper,
				profile.getAsJsonObject("formations"));
		requireSharedAdventureGeology(helper,
				templates.getAsJsonObject("cakeworld:edible_world")
						.getAsJsonObject("profile"), profile);
		JsonObject palettes = profile.getAsJsonObject("biome_palettes");
		require(helper, palettes.size() == 8,
				"Sampler region checkpoint must expose eight plots");
		JsonObject palette = palettes
				.getAsJsonObject("cakeworld:sampler_overworld_augment");
		requireSamplerPalette(helper, palette);
		requireSelectedNamespacesPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_nether_selected_namespaces"));
		requireTinyRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_tiny_regions"));
		requireSmallRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_small_regions"));
		requireAverageRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_average_regions"));
		requireLargeRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_large_regions"));
		requireHugeRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_huge_regions"));
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
		requireExtremeFormationProfile(helper,
				profile.toJson().getAsJsonObject("formations"));
		JsonObject palette = palettes
				.getAsJsonObject("cakeworld:sampler_overworld_augment");
		requireSamplerPalette(helper, palette);
		requireSelectedNamespacesPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_nether_selected_namespaces"));
		requireTinyRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_tiny_regions"));
		requireSmallRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_small_regions"));
		requireAverageRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_average_regions"));
		requireLargeRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_large_regions"));
		requireHugeRegionPalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_overworld_huge_regions"));
		requireExcludedNamespacePalette(helper, palettes.getAsJsonObject(
				"cakeworld:sampler_end_excluded_namespace"));

		ServerLevel level = helper.getLevel();
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Map<ResourceLocation, Integer> counts = new HashMap<>();
		Map<Long, Integer> tinyRegionKinds = new HashMap<>();
		Map<Long, Integer> smallRegionKinds = new HashMap<>();
		Map<Long, Integer> averageRegionKinds = new HashMap<>();
		Map<Long, Integer> largeRegionKinds = new HashMap<>();
		Map<Long, Integer> hugeRegionKinds = new HashMap<>();
		for (int x = -4096; x <= 4096; x += 32) {
			for (int z = -4096; z <= 4096; z += 32) {
				Holder<Biome> biome = generator.getNoiseBiome(
						QuartPos.fromBlock(x), QuartPos.fromBlock(64),
						QuartPos.fromBlock(z));
				ResourceLocation id = biome.unwrapKey()
						.map(key -> key.location())
						.orElseGet(() -> registry.getKey(biome.value()));
				counts.merge(id, 1, Integer::sum);
				int kind = CANDY_PLAINS.equals(id) ? 1
						: GINGERBREAD_HEARTHLANDS.equals(id) ? 2
						: PEPPERMINT_PINEWOODS.equals(id) ? 4
						: MARSHMALLOW_PEAKS.equals(id) ? 8
						: COOKIE_FOREST.equals(id) ? 16
						: SODA_OCEAN.equals(id) ? 32
						: "minecraft".equals(id.getNamespace()) ? 64 : 128;
				tinyRegionKinds.merge(regionKey(x, z, 128), kind,
						(left, right) -> left | right);
				smallRegionKinds.merge(regionKey(x, z, 256), kind,
						(left, right) -> left | right);
				averageRegionKinds.merge(regionKey(x, z, 512), kind,
						(left, right) -> left | right);
				largeRegionKinds.merge(regionKey(x, z, 1024), kind,
						(left, right) -> left | right);
				hugeRegionKinds.merge(regionKey(x, z, 2048), kind,
						(left, right) -> left | right);
			}
		}
		int candy = counts.getOrDefault(CANDY_PLAINS, 0);
		int gingerbread = counts.getOrDefault(GINGERBREAD_HEARTHLANDS, 0);
		int peppermint = counts.getOrDefault(PEPPERMINT_PINEWOODS, 0);
		int marshmallow = counts.getOrDefault(MARSHMALLOW_PEAKS, 0);
		int cookie = counts.getOrDefault(COOKIE_FOREST, 0);
		int soda = counts.getOrDefault(SODA_OCEAN, 0);
		int minecraft = counts.entrySet().stream()
				.filter(entry -> "minecraft".equals(
						entry.getKey().getNamespace()))
				.mapToInt(Map.Entry::getValue).sum();
		int total = counts.values().stream().mapToInt(Integer::intValue)
				.sum();
		require(helper, total == 66049,
				"Sampler audit grid drifted from 66,049 positions");
		require(helper, candy > 0 && gingerbread > 0 && peppermint > 0
					&& marshmallow > 0 && cookie > 0 && soda > 0,
				"Five-size sampler omitted a labelled Overworld output: "
						+ counts);
		require(helper, minecraft > 0,
				"Augment sampler did not retain delegated Minecraft biomes");
		require(helper, candy < total && gingerbread < total
					&& peppermint < total && marshmallow < total
					&& minecraft < total && cookie < total && soda < total,
				"Sampler collapsed into a single biome source");
		require(helper, candy == 1414 && gingerbread == 32455
					&& peppermint == 14701 && marshmallow == 7989
					&& cookie == 4256 && soda == 1820
					&& minecraft == 3414 && counts.size() == 47,
				"Fixed-seed five-size distribution drifted: " + counts);
		int tinyConflicts = regionConflicts(tinyRegionKinds, 2,
				4 | 8 | 16 | 32 | 64 | 128);
		int smallConflicts = regionConflicts(smallRegionKinds, 4,
				8 | 16 | 32 | 64 | 128);
		int averageConflicts = regionConflicts(averageRegionKinds, 8,
				16 | 32 | 64 | 128);
		int largeConflicts = regionConflicts(largeRegionKinds, 16,
				32 | 64 | 128);
		int hugeConflicts = regionConflicts(hugeRegionKinds, 32,
				64 | 128);
		int tinyGingerbreadRegions = regionsWith(tinyRegionKinds, 2);
		int smallPeppermintRegions = regionsWith(smallRegionKinds, 4);
		int averageMarshmallowRegions = regionsWith(averageRegionKinds, 8);
		int largeCookieRegions = regionsWith(largeRegionKinds, 16);
		int largeOtherRegions = largeRegionKinds.size() - largeCookieRegions;
		int hugeSodaRegions = regionsWith(hugeRegionKinds, 32);
		require(helper, tinyConflicts == 0 && smallConflicts == 0
					&& averageConflicts == 0 && largeConflicts == 0
					&& hugeConflicts == 0,
				"A labelled output crossed its 128/256/512/1024/2048-block region boundary");
		require(helper, tinyGingerbreadRegions > 0
					&& tinyGingerbreadRegions < tinyRegionKinds.size()
					&& smallPeppermintRegions > 0
					&& smallPeppermintRegions < smallRegionKinds.size()
					&& averageMarshmallowRegions > 0
					&& averageMarshmallowRegions < averageRegionKinds.size()
					&& largeCookieRegions > 0
					&& largeOtherRegions > 0 && hugeSodaRegions > 0
					&& hugeSodaRegions < hugeRegionKinds.size(),
				"Fixed grid did not observe both covered and delegated regions at every size");
		require(helper, tinyRegionKinds.size() == 4225
					&& tinyGingerbreadRegions == 2119
					&& smallRegionKinds.size() == 1089
					&& smallPeppermintRegions == 468
					&& averageRegionKinds.size() == 289
					&& averageMarshmallowRegions == 117
					&& largeRegionKinds.size() == 81
					&& largeCookieRegions == 29
					&& hugeRegionKinds.size() == 25
					&& hugeSodaRegions == 7,
				"Fixed-seed five-size region coverage drifted");
		LOGGER.info("Sampler Platter five-size audit: samples={}, candyPlains={}, gingerbread={}, peppermint={}, marshmallow={}, cookieForest={}, sodaOcean={}, minecraft={}, distinct={}, tinyRegions={}/{}/{}, smallRegions={}/{}/{}, averageRegions={}/{}/{}, largeRegions={}/{}/{}, hugeRegions={}/{}/{}",
				total, candy, gingerbread, peppermint, marshmallow, cookie,
				soda, minecraft, counts.size(), tinyRegionKinds.size(),
				tinyGingerbreadRegions, tinyConflicts, smallRegionKinds.size(),
				smallPeppermintRegions, smallConflicts,
				averageRegionKinds.size(), averageMarshmallowRegions,
				averageConflicts, largeRegionKinds.size(), largeCookieRegions,
				largeConflicts, hugeRegionKinds.size(), hugeSodaRegions,
				hugeConflicts);

		ServerLevel nether = level.getServer().getLevel(Level.NETHER);
		ServerLevel end = level.getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Sampler namespace proof requires Nether and End levels");
		Map<ResourceLocation, Integer> netherCounts = sampleBiomes(nether,
				64, 1024);
		Map<ResourceLocation, Integer> endCounts = sampleBiomes(end, 64, 1024);
		int netherTotal = total(netherCounts);
		int fudge = netherCounts.getOrDefault(FUDGE_WASTES, 0);
		int strict = netherCounts.getOrDefault(CHILLI_CHOCOLATE_CRAGS, 0);
		int netherMinecraft = namespaceTotal(netherCounts, "minecraft");
		int endTotal = total(endCounts);
		int endMinecraft = namespaceTotal(endCounts, "minecraft");
		require(helper, netherTotal == 4225 && fudge == 1511
					&& strict == 0
					&& netherMinecraft == 2714
					&& netherCounts.size() == 5,
				"Optional/required similarity outputs were not isolated in the Nether: "
						+ netherCounts);
		require(helper, endTotal == 4225 && endMinecraft == endTotal
					&& !endCounts.containsKey(MERINGUE_ISLANDS),
				"Excluded minecraft namespace did not delegate unchanged in the End: "
						+ endCounts);
		LOGGER.info("Sampler namespace/similarity audit: netherSamples={}, fudgeWastes={}, strictOutput={}, netherMinecraft={}, netherDistinct={}, endSamples={}, endMinecraft={}, endDistinct={}",
				netherTotal, fudge, strict, netherMinecraft,
				netherCounts.size(), endTotal, endMinecraft,
				endCounts.size());
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

	private static long regionKey(int blockX, int blockZ, int regionSize) {
		int regionX = Math.floorDiv(blockX, regionSize);
		int regionZ = Math.floorDiv(blockZ, regionSize);
		return ((long) regionX << 32) ^ (regionZ & 0xFFFFFFFFL);
	}

	private static int regionsWith(Map<Long, Integer> regions, int bit) {
		return (int) regions.values().stream()
				.filter(kinds -> (kinds & bit) != 0).count();
	}

	private static int regionConflicts(Map<Long, Integer> regions,
			int outputBit, int laterMask) {
		return (int) regions.values().stream()
				.filter(kinds -> (kinds & outputBit) != 0
						&& (kinds & laterMask) != 0).count();
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
		JsonObject biomes = palette.getAsJsonObject("biomes");
		require(helper, biomes.size() == 2
					&& biomes.has("cakeworld:fudge_wastes")
					&& biomes.has("cakeworld:chilli_chocolate_crags"),
				"Sampler selected-namespace similarity pair drifted");
		JsonObject optional = biomes.getAsJsonObject(
				"cakeworld:fudge_wastes");
		JsonObject strict = biomes.getAsJsonObject(
				"cakeworld:chilli_chocolate_crags");
		require(helper, optional.getAsJsonArray("similar_biomes").size() == 2
					&& "minecraft:nether_wastes".equals(
							optional.getAsJsonArray("similar_biomes").get(0)
									.getAsString())
					&& "cakeworld:missing_optional_sampler_source".equals(
							optional.getAsJsonArray("similar_biomes").get(1)
									.getAsString())
					&& optional.getAsJsonArray("required_similar_biomes")
							.size() == 0,
				"Sampler optional similarity fixture drifted");
		require(helper, strict.getAsJsonArray("similar_biomes").size() == 0
					&& strict.getAsJsonArray("required_similar_biomes")
							.size() == 1
					&& "cakeworld:missing_required_sampler_source".equals(
							strict.getAsJsonArray("required_similar_biomes")
									.get(0).getAsString()),
				"Sampler required similarity fixture drifted");
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

	private static void requireLargeRegionPalette(
			GameTestHelper helper, JsonObject palette) {
		requireRegionPalette(helper, palette, "large", 0.5,
				"cakeworld:cookie_forest", 2,
				"Sampler large-region plot drifted");
	}

	private static void requireTinyRegionPalette(
			GameTestHelper helper, JsonObject palette) {
		requireRegionPalette(helper, palette, "tiny", 0.5,
				"cakeworld:gingerbread_hearthlands", 2,
				"Sampler tiny-region boundary plot drifted");
	}

	private static void requireSmallRegionPalette(
			GameTestHelper helper, JsonObject palette) {
		requireRegionPalette(helper, palette, "small", 0.5,
				"cakeworld:peppermint_pinewoods", 2,
				"Sampler small-region boundary plot drifted");
	}

	private static void requireAverageRegionPalette(
			GameTestHelper helper, JsonObject palette) {
		requireRegionPalette(helper, palette, "average", 0.5,
				"cakeworld:marshmallow_peaks", 3,
				"Sampler average-region boundary plot drifted");
	}

	private static void requireHugeRegionPalette(
			GameTestHelper helper, JsonObject palette) {
		requireRegionPalette(helper, palette, "huge", 0.5,
				"cakeworld:soda_ocean", 4,
				"Sampler huge-region plot drifted");
	}

	private static void requireRegionPalette(GameTestHelper helper,
			JsonObject palette, String regionSize, double coverage,
			String biomeId, int fillerDepth, String message) {
		JsonObject biomes = palette == null ? null
				: palette.getAsJsonObject("biomes");
		JsonObject biome = biomes == null ? null
				: biomes.getAsJsonObject(biomeId);
		require(helper, palette != null
					&& "minecraft:overworld".equals(
							palette.get("dimension").getAsString())
					&& "replace".equals(palette.get("mode").getAsString())
					&& "minecraft_only".equals(
							palette.get("scope").getAsString())
					&& regionSize.equals(
							palette.get("region_size").getAsString())
					&& palette.get("coverage").getAsDouble() == coverage
					&& palette.get("fallback_weight").getAsDouble() == 0.0
					&& palette.getAsJsonArray("include_namespaces").size() == 0
					&& palette.getAsJsonArray("exclude_namespaces").size() == 0
					&& biomes.size() == 1 && biome != null
					&& biome.getAsJsonArray("similar_biomes").size() == 0
					&& biome.getAsJsonArray("required_similar_biomes").size() == 0
					&& biome.getAsJsonObject("surface")
							.get("filler_depth").getAsInt() == fillerDepth,
				message);
	}

	private static void requireSharedAdventureGeology(GameTestHelper helper,
			JsonObject adventure, JsonObject sampler) {
		for (String section : new String[] { "rocks", "geomes", "biomes",
				"biome_dictionary", "terrain_dimensions" }) {
			require(helper, adventure.get(section).equals(sampler.get(section)),
					"Sampler did not inherit the canonical adventure "
							+ section + " section");
		}
	}

	private static void requireExtremeFormationProfile(GameTestHelper helper,
			JsonObject formations) {
		require(helper, formations != null
					&& "sky_v1".equals(
							formations.get("algorithm").getAsString()),
				"Sampler alternate formation algorithm drifted");
		for (String key : new String[] { "horizontal_size",
				"vertical_thickness", "waviness", "edge_irregularity",
				"formation_continuity" }) {
			require(helper, "custom".equals(
					formations.get(key).getAsString()),
					"Sampler formation preset drifted for " + key);
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
				"Sampler bounded custom formation values drifted");
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
