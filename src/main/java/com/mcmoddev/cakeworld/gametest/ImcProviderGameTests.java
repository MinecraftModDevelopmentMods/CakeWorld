package com.mcmoddev.cakeworld.gametest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.ProviderStatus;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Genuine Forge-IMC submission, ownership and immutability proof. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_imc_provider")
public final class ImcProviderGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceKey<Level> SAMPLER_PANTRY =
			ResourceKey.create(Registry.DIMENSION_REGISTRY,
					new ResourceLocation("cakeworld", "sampler_pantry"));
	private static final ChunkPos SURVEY_CHUNK = new ChunkPos(512, 512);
	private static final Logger LOGGER = LogUtils.getLogger();

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
		require(helper, ImcProviderGameTests.class.getResource(
				"/data/cakeworld/dimension/sampler_pantry.json") != null,
				"Test-only selector dimension was not installed");
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
						CakeWorldImcProviderFixture.RULE_ID)
				&& profile.oreIds().contains(
						CakeWorldImcProviderFixture.SELECTOR_RULE_ID),
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
		JsonObject selectorRule = root.getAsJsonObject("ores")
				.getAsJsonObject(
						CakeWorldImcProviderFixture.SELECTOR_RULE_ID
								.toString());
		JsonObject selector = selectorRule
				.getAsJsonObject("dimension_selectors")
				.getAsJsonObject("orespawn:all_except_nether_end");
		JsonObject overworldOverride = selectorRule
				.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:overworld");
		require(helper, selector.get("enabled").getAsBoolean()
				&& selector.get("frequency").getAsDouble() == 64.0D
				&& selector.getAsJsonArray("host_blocks").size() == 1
				&& !overworldOverride.get("enabled").getAsBoolean(),
				"Selector or explicit Overworld override drifted");

		JsonObject manifest = root.getAsJsonObject("providers")
				.getAsJsonObject("cakeworld");
		JsonArray known = manifest.getAsJsonArray("known_ores");
		require(helper, manifest.get("provider_revision").getAsInt()
						== CakeWorldImcProviderFixture.REVISION
				&& known.size() == 2
				&& CakeWorldImcProviderFixture.RULE_ID.toString().equals(
						known.get(0).getAsString())
				&& CakeWorldImcProviderFixture.SELECTOR_RULE_ID.toString()
						.equals(known.get(1).getAsString()),
				"IMC ownership manifest drifted");

		ServerLevel pantry = helper.getLevel().getServer().getLevel(
				SAMPLER_PANTRY);
		require(helper, pantry != null,
				"Test-only ordinary dimension did not load");
		Block output = ForgeRegistries.BLOCKS.getValue(
				CakeWorldImcProviderFixture.OUTPUT_ID);
		require(helper, output != null && output != Blocks.AIR,
				"Selector output block is not registered");
		int pantryOutputs = countBlock(pantry, SURVEY_CHUNK, output);
		int overworldOutputs = countBlock(helper.getLevel(), SURVEY_CHUNK,
				output);
		int netherOutputs = countBlock(helper.getLevel().getServer()
				.getLevel(Level.NETHER), SURVEY_CHUNK, output);
		int endOutputs = countBlock(helper.getLevel().getServer()
				.getLevel(Level.END), SURVEY_CHUNK, output);
		require(helper, pantryOutputs == 1818 && overworldOutputs == 0
				&& netherOutputs == 0 && endOutputs == 0,
				"Selector/explicit-dimension runtime boundary failed: pantry="
						+ pantryOutputs + ", overworld=" + overworldOutputs
						+ ", nether=" + netherOutputs + ", end="
						+ endOutputs);
		LOGGER.info("IMC selector audit: chunk={}, pantry={}, overworld={}, nether={}, end={}",
				SURVEY_CHUNK, pantryOutputs, overworldOutputs, netherOutputs,
				endOutputs);
		helper.succeed();
	}

	private static int countBlock(ServerLevel level, ChunkPos chunk,
			Block target) {
		if (level == null) return -1;
		level.getChunk(chunk.x, chunk.z);
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		int count = 0;
		for (int x = chunk.getMinBlockX(); x <= chunk.getMaxBlockX(); x++) {
			for (int z = chunk.getMinBlockZ(); z <= chunk.getMaxBlockZ(); z++) {
				for (int y = level.getMinBuildHeight();
						y < level.getMaxBuildHeight(); y++) {
					cursor.set(x, y, z);
					if (level.getBlockState(cursor).is(target)) count++;
				}
			}
		}
		return count;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
