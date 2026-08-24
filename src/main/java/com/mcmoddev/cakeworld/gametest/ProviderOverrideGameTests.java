package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Released provider-override precedence and fail-closed integration proof. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_provider_override")
public final class ProviderOverrideGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation OVERRIDE_PROBE =
			new ResourceLocation("cakeworld", "override_probe");
	private static final ResourceLocation LEGACY_SCHEMA_PROBE =
			new ResourceLocation("cakeworld", "legacy_schema_probe");

	private ProviderOverrideGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void activeProviderMatchesIsolatedOverrideScenario(
			GameTestHelper helper) {
		String mode = System.getProperty(
				"cakeworld.providerOverrideMode", "");
		require(helper, mode.equals("valid") || mode.equals("removed")
					|| mode.equals("malformed") || mode.equals("schema3"),
				"Provider-override test ran without a supported scenario");
		JsonObject packaged = packagedProvider(helper);
		require(helper,
				packaged.get("provider_revision").getAsInt() == 55
						&& packaged.getAsJsonObject("templates").size() == 3,
				"Packaged provider is not the revision-55 three-template control");

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		if (mode.equals("valid") || mode.equals("removed")) {
			require(helper, profile.selectedTemplate()
					.filter(OVERRIDE_PROBE::equals).isPresent(),
					"Authoritative override template was not selected or persisted");
			requireNoCakeWorldAdventureRules(helper, profile);
		} else if (mode.equals("schema3")) {
			requireLegacySchemaMigration(helper, profile);
		} else {
			require(helper, profile.selectedTemplate().isEmpty(),
					"Malformed override unexpectedly fell back to a packaged template");
			requireNoCakeWorldAdventureRules(helper, profile);
		}
		helper.succeed();
	}

	private static void requireLegacySchemaMigration(GameTestHelper helper,
			GeologyProfileView profile) {
		require(helper, profile.selectedTemplate()
				.filter(LEGACY_SCHEMA_PROBE::equals).isPresent(),
				"Schema-3 template was not selected or retained");
		require(helper, profile.schemaVersion() == 5
				&& "geome".equals(profile.geologyMode()),
				"Schema-3 provider did not become a current world profile");
		requireNoCakeWorldAdventureRules(helper, profile);
		JsonObject root = profile.toJson();
		JsonObject formations = root.getAsJsonObject("formations");
		require(helper, formations != null
				&& "stable_layers".equals(
						formations.get("algorithm").getAsString())
				&& "average".equals(
						formations.get("horizontal_size").getAsString())
				&& "average".equals(
						formations.get("vertical_thickness").getAsString())
				&& "average".equals(
						formations.get("waviness").getAsString())
				&& "average".equals(
						formations.get("edge_irregularity").getAsString())
				&& "average".equals(
						formations.get("formation_continuity").getAsString()),
				"Schema-3 empty profile lost documented formation defaults");
		require(helper, root.has("place_fluid_deposits")
				&& root.get("place_fluid_deposits").isJsonPrimitive(),
				"Schema-3 profile did not normalize fluid-deposit default");
		JsonObject manifest = root.has("providers")
				? root.getAsJsonObject("providers")
						.getAsJsonObject("cakeworld")
				: null;
		require(helper, manifest != null
				&& manifest.get("provider_revision").getAsInt() == 1101,
				"Schema-3 provider revision was not retained in the snapshot");
	}

	private static void requireNoCakeWorldAdventureRules(
			GameTestHelper helper, GeologyProfileView profile) {
		require(helper, profile.rockIds().stream().noneMatch(
				id -> "cakeworld".equals(id.getNamespace())),
				"Override scenario leaked packaged CakeWorld rocks");
		JsonObject palettes = profile.toJson().getAsJsonObject(
				"biome_palettes");
		require(helper, palettes == null || palettes.entrySet().stream()
				.noneMatch(entry -> entry.getKey().startsWith("cakeworld:")),
				"Override scenario leaked packaged CakeWorld biome palettes");
	}

	private static JsonObject packagedProvider(GameTestHelper helper) {
		try (InputStreamReader reader = new InputStreamReader(
				ProviderOverrideGameTests.class.getResourceAsStream(
						"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			helper.fail("Could not read packaged provider control: "
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
