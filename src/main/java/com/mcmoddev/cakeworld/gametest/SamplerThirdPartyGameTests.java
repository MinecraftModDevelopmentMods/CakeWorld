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

/** Released-scope proof using an opt-in unrelated biome-source namespace. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_sampler_third_party")
public final class SamplerThirdPartyGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation SAMPLER_TEMPLATE =
			new ResourceLocation("cakeworld", "sampler_platter");
	private static final ResourceLocation CANDY_PLAINS =
			new ResourceLocation("cakeworld", "candy_plains");
	private static final Logger LOGGER = LogUtils.getLogger();

	private SamplerThirdPartyGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 400)
	public static void unrelatedNamespaceDelegatesBeforeSelectedPlot(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean(
				"cakeworld.samplerThirdPartyFixture"),
				"Third-party biome fixture was not explicitly enabled");
		JsonObject provider = packagedProvider(helper);
		require(helper, provider.get("provider_revision").getAsInt() == 55,
				"Third-party proof requires provider revision 55");
		JsonObject profile = provider.getAsJsonObject("templates")
				.getAsJsonObject("cakeworld:sampler_platter")
				.getAsJsonObject("profile");
		JsonObject palette = profile.getAsJsonObject("biome_palettes")
				.getAsJsonObject(
						"cakeworld:sampler_third_party_passthrough");
		require(helper, palette != null
				&& "minecraft:overworld".equals(
						palette.get("dimension").getAsString())
				&& "replace".equals(palette.get("mode").getAsString())
				&& "selected_namespaces".equals(
						palette.get("scope").getAsString())
				&& "tiny".equals(palette.get("region_size").getAsString())
				&& palette.get("coverage").getAsDouble() == 0.5
				&& palette.getAsJsonArray("include_namespaces").size() == 1
				&& SamplerThirdPartyBiomeFixture.NAMESPACE.equals(
						palette.getAsJsonArray("include_namespaces")
								.get(0).getAsString()),
				"Packaged third-party compatibility plot drifted");

		GeologyProfileView active = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, active.selectedTemplate()
				.filter(SAMPLER_TEMPLATE::equals).isPresent(),
				"Third-party proof did not select the Sampler");
		JsonObject activePalette = active.toJson()
				.getAsJsonObject("biome_palettes")
				.getAsJsonObject(
						"cakeworld:sampler_third_party_passthrough");
		require(helper, activePalette != null,
				"Saved profile omitted the third-party compatibility plot");

		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		require(helper, registry.containsKey(
				SamplerThirdPartyBiomeFixture.BIOME_ID),
				"Fixture biome is absent from the runtime registry");
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		Map<ResourceLocation, Integer> counts = new HashMap<>();
		for (int x = -1024; x <= 1024; x += 32) {
			for (int z = -1024; z <= 1024; z += 32) {
				Holder<Biome> biome = generator.getNoiseBiome(
						QuartPos.fromBlock(x), QuartPos.fromBlock(64),
						QuartPos.fromBlock(z));
				ResourceLocation id = biome.unwrapKey()
						.map(key -> key.location())
						.orElseGet(() -> registry.getKey(biome.value()));
				counts.merge(id, 1, Integer::sum);
			}
		}
		int delegated = counts.getOrDefault(
				SamplerThirdPartyBiomeFixture.BIOME_ID, 0);
		int selected = counts.getOrDefault(CANDY_PLAINS, 0);
		require(helper, delegated == 1736 && selected == 2489
				&& delegated + selected == 4225 && counts.size() == 2,
				"Unrelated source was captured by an earlier scope or lost: "
						+ counts);
		LOGGER.info("Sampler third-party namespace audit: samples=4225, delegated={}, selected={}, distinct={}, counts={}",
				delegated, selected, counts.size(), counts);
		helper.succeed();
	}

	private static JsonObject packagedProvider(GameTestHelper helper) {
		try (InputStreamReader reader = new InputStreamReader(
				SamplerThirdPartyGameTests.class.getResourceAsStream(
						"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception e) {
			helper.fail("Could not read packaged provider: " + e.getMessage());
			throw new IllegalStateException(e);
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
