package com.mcmoddev.cakeworld.gametest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.ProviderStatus;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Genuine Forge-IMC submission, ownership and immutability proof. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_imc_provider")
public final class ImcProviderGameTests {
	private static final String EMPTY = "empty";

	private ImcProviderGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void isolatedImcProviderFreezesWithOwnedRule(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.imcProviderFixture")
				&& CakeWorldImcProviderFixture.submitted(),
				"Conditional IMC provider was not submitted");
		require(helper, ImcProviderGameTests.class.getResource(
				"/data/cakeworld/orespawn/provider.json") == null,
				"Packaged provider was not isolated from the IMC proof");
		require(helper, OreSpawnApi.getProviderStatus("cakeworld")
				== ProviderStatus.ACTIVE,
				"Released OreSpawn did not freeze the IMC provider as active");

		WorldgenProvider provider = CakeWorldImcProviderFixture.provider();
		JsonObject first = provider.toJson();
		require(helper, first.get("schema_version").getAsInt() == 4
				&& first.get("provider_revision").getAsInt()
						== CakeWorldImcProviderFixture.REVISION,
				"IMC builder did not emit the current provider schema/revision");
		first.getAsJsonObject("ores")
				.getAsJsonObject(
						CakeWorldImcProviderFixture.RULE_ID.toString())
				.addProperty("enabled", true);
		JsonObject detached = provider.toJson();
		require(helper, !detached.getAsJsonObject("ores")
				.getAsJsonObject(
						CakeWorldImcProviderFixture.RULE_ID.toString())
				.get("enabled").getAsBoolean(),
				"Built IMC definition was mutable through diagnostic JSON");

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate().isEmpty()
				&& profile.oreIds().contains(
						CakeWorldImcProviderFixture.RULE_ID),
				"IMC rule did not enter the world-owned profile");
		JsonObject root = profile.toJson();
		JsonObject rule = root.getAsJsonObject("ores").getAsJsonObject(
				CakeWorldImcProviderFixture.RULE_ID.toString());
		JsonObject dimension = rule.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:overworld");
		require(helper, "cakeworld".equals(
				rule.get("source_provider").getAsString())
				&& !rule.get("enabled").getAsBoolean()
				&& !rule.get("retrogen").getAsBoolean()
				&& !dimension.get("enabled").getAsBoolean()
				&& dimension.get("frequency").getAsDouble() == 0.0D,
				"IMC rule lost ownership or inert settings");
		JsonObject manifest = root.getAsJsonObject("providers")
				.getAsJsonObject("cakeworld");
		JsonArray known = manifest.getAsJsonArray("known_ores");
		require(helper, manifest.get("provider_revision").getAsInt()
						== CakeWorldImcProviderFixture.REVISION
				&& known.size() == 1
				&& CakeWorldImcProviderFixture.RULE_ID.toString().equals(
						known.get(0).getAsString()),
				"IMC ownership manifest drifted");
		helper.succeed();
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
