package zone.moddev.mc.cakeworld.gametest;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

/** Wide all-realm proof for the main adventure's replace biome mode. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_replace_mode")
public final class ReplaceModeGameTests {
	private static final String EMPTY = "empty";
	private static final int RADIUS = 4096;
	private static final int STEP = 32;
	private static final int EXPECTED_SAMPLES = 66049;
	private static final String EXPECTED_OVERWORLD_SIGNATURE =
			"671217176651623588";
	private static final String EXPECTED_NETHER_SIGNATURE =
			"14305352832125438259";
	private static final String EXPECTED_END_SIGNATURE =
			"10797345351901494849";
	private static final ResourceLocation ADVENTURE_TEMPLATE =
			new ResourceLocation("cakeworld", "edible_world");
	private static final ResourceLocation GENERIC_LAND_OUTPUT =
			new ResourceLocation("cakeworld", "candy_plains");
	private static final Logger LOGGER = LogUtils.getLogger();

	private ReplaceModeGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 600)
	public static void mainAdventureReplacesUnrelatedSourceInEveryRealm(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.replaceModeFixture"),
				"Replace-mode fixture was not explicitly enabled");
		require(helper, SamplerThirdPartyBiomeFixture.wasInstalled(),
				"Unrelated Overworld source owner was not installed before OreSpawn");

		ServerLevel overworld = helper.getLevel();
		ServerLevel nether = overworld.getServer().getLevel(Level.NETHER);
		ServerLevel end = overworld.getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Replace-mode proof requires all three vanilla dimensions");

		Registry<Biome> registry = overworld.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		require(helper, registry.containsKey(
				SamplerThirdPartyBiomeFixture.BIOME_ID),
				"Unrelated fixture biome is absent from the runtime registry");

		GeologyProfileView active = OreSpawnApi.getActiveProfile(
				overworld.getServer()).orElseThrow();
		require(helper, active.selectedTemplate()
				.filter(ADVENTURE_TEMPLATE::equals).isPresent(),
				"Replace-mode proof did not auto-select " + ADVENTURE_TEMPLATE);
		JsonObject palettes = active.toJson()
				.getAsJsonObject("biome_palettes");
		require(helper, palettes != null && palettes.size() == 5,
				"Main adventure must retain exactly five biome palettes");
		requirePalette(helper, palettes, "cakeworld:overworld_caves",
				"minecraft:overworld");
		requirePalette(helper, palettes, "cakeworld:overworld_oceans",
				"minecraft:overworld");
		requirePalette(helper, palettes, "cakeworld:overworld_land",
				"minecraft:overworld");
		requirePalette(helper, palettes, "cakeworld:nether",
				"minecraft:the_nether");
		requirePalette(helper, palettes, "cakeworld:end",
				"minecraft:the_end");

		Survey overworldSurface = survey(overworld, 64);
		Survey overworldDeep = survey(overworld, -32);
		Survey netherSurvey = survey(nether, 64);
		Survey endSurvey = survey(end, 64);
		LOGGER.info("Main replace-mode audit: samplesPerPlane={}, overworldSurface={}, overworldSurfaceSignature={}, overworldDeep={}, overworldDeepSignature={}, nether={}, netherSignature={}, end={}, endSignature={}",
				EXPECTED_SAMPLES, overworldSurface.counts,
				Long.toUnsignedString(overworldSurface.signature),
				overworldDeep.counts,
				Long.toUnsignedString(overworldDeep.signature),
				netherSurvey.counts,
				Long.toUnsignedString(netherSurvey.signature),
				endSurvey.counts,
				Long.toUnsignedString(endSurvey.signature));

		requireGenericLandReplacement(helper, "Overworld surface",
				overworldSurface);
		requireGenericLandReplacement(helper, "Overworld deep",
				overworldDeep);
		requireCakeWorldOnly(helper, "Nether", netherSurvey, 2);
		requireCakeWorldOnly(helper, "End", endSurvey, 2);
		requireSignature(helper, "Overworld surface", overworldSurface,
				EXPECTED_OVERWORLD_SIGNATURE);
		requireSignature(helper, "Overworld deep", overworldDeep,
				EXPECTED_OVERWORLD_SIGNATURE);
		requireSignature(helper, "Nether", netherSurvey,
				EXPECTED_NETHER_SIGNATURE);
		requireSignature(helper, "End", endSurvey,
				EXPECTED_END_SIGNATURE);
		helper.succeed();
	}

	private static void requirePalette(GameTestHelper helper,
			JsonObject palettes, String id, String dimension) {
		JsonObject palette = palettes.getAsJsonObject(id);
		require(helper, palette != null
				&& dimension.equals(palette.get("dimension").getAsString())
				&& "replace".equals(palette.get("mode").getAsString())
				&& "all".equals(palette.get("scope").getAsString())
				&& palette.get("coverage").getAsDouble() == 1.0D
				&& palette.get("fallback_weight").getAsDouble() == 0.0D
				&& palette.get("enabled").getAsBoolean()
				&& palette.getAsJsonObject("biomes").size() > 0,
				"Replace palette drifted: " + id);
		boolean excludesCakeWorld = false;
		for (JsonElement namespace : palette.getAsJsonArray(
				"exclude_namespaces")) {
			if ("cakeworld".equals(namespace.getAsString())) {
				excludesCakeWorld = true;
			}
		}
		require(helper, excludesCakeWorld,
				"Replace palette must exclude its own output namespace: " + id);
	}

	private static Survey survey(ServerLevel level, int blockY) {
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Map<ResourceLocation, Integer> counts = new HashMap<>();
		long signature = 0xcbf29ce484222325L;
		for (int x = -RADIUS; x <= RADIUS; x += STEP) {
			for (int z = -RADIUS; z <= RADIUS; z += STEP) {
				Holder<Biome> biome = generator.getNoiseBiome(
						QuartPos.fromBlock(x), QuartPos.fromBlock(blockY),
						QuartPos.fromBlock(z));
				ResourceLocation id = biome.unwrapKey()
						.map(key -> key.location())
						.orElseGet(() -> registry.getKey(biome.value()));
				if (id == null) {
					throw new IllegalStateException(
							"Biome source returned an unregistered biome");
				}
				counts.merge(id, 1, Integer::sum);
				signature ^= id.hashCode();
				signature *= 0x100000001b3L;
			}
		}
		return new Survey(counts, signature);
	}

	private static void requireGenericLandReplacement(GameTestHelper helper,
			String label, Survey survey) {
		requireCakeWorldOnly(helper, label, survey, 1);
		require(helper, survey.counts.size() == 1
				&& survey.counts.getOrDefault(GENERIC_LAND_OUTPUT, 0)
						== EXPECTED_SAMPLES,
				label + " did not map the unrelated source to the only generic "
						+ "land output: " + survey.counts);
	}

	private static void requireSignature(GameTestHelper helper, String label,
			Survey survey, String expected) {
		String actual = Long.toUnsignedString(survey.signature);
		require(helper, expected.equals(actual), label
				+ " fixed-seed signature drifted from " + expected + " to "
				+ actual);
	}

	private static void requireCakeWorldOnly(GameTestHelper helper,
			String label, Survey survey, int minimumDistinct) {
		int total = survey.counts.values().stream()
				.mapToInt(Integer::intValue).sum();
		require(helper, total == EXPECTED_SAMPLES,
				label + " sample count drifted from " + EXPECTED_SAMPLES);
		require(helper, survey.counts.size() >= minimumDistinct,
				label + " collapsed to too few outputs: " + survey.counts);
		require(helper, survey.counts.keySet().stream().allMatch(
				id -> "cakeworld".equals(id.getNamespace())),
				label + " delegated a non-CakeWorld source under full replace: "
						+ survey.counts);
		require(helper, !survey.counts.containsKey(
				SamplerThirdPartyBiomeFixture.BIOME_ID),
				label + " leaked the unrelated source biome");
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private static final class Survey {
		private final Map<ResourceLocation, Integer> counts;
		private final long signature;

		private Survey(Map<ResourceLocation, Integer> counts, long signature) {
			this.counts = counts;
			this.signature = signature;
		}
	}
}
