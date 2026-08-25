package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Copied-world provider merge, player-edit, and tombstone proof for OS-017. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_provider_merge")
public final class ProviderMergeGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation MERGE_PROBE =
			new ResourceLocation("cakeworld", "merge_probe");
	private static final String EDITED_ROCK =
			"cakeworld:merge/edited_rock";
	private static final String ADDED_ROCK =
			"cakeworld:merge/added_rock";
	private static final String EDITED_ORE =
			"cakeworld:merge/edited_ore";
	private static final String DISABLED_ORE =
			"cakeworld:merge/disabled_ore";
	private static final String UNASSIGNED_ORE =
			"cakeworld:merge/unassigned_ore";
	private static final String REMOVED_ORE =
			"cakeworld:merge/removed_ore";
	private static final String ADDED_ORE =
			"cakeworld:merge/added_ore";
	private static final String PLAYER_CUSTOM_ORE =
			"cakeworld:merge/player_custom_ore";

	private ProviderMergeGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void copiedWorldMergePreservesWorldOwnership(
			GameTestHelper helper) {
		String phase = System.getProperty(
				"cakeworld.providerMergePhase", "");
		require(helper, phase.equals("baseline")
					|| phase.equals("upgrade") || phase.equals("reload"),
				"Provider-merge test ran without a supported phase");
		JsonObject packaged = packagedProvider(helper);
		require(helper,
				packaged.get("provider_revision").getAsInt() == 55
						&& packaged.getAsJsonObject("templates").size() == 3,
				"Packaged provider is not the revision-55 control");

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate()
				.filter(MERGE_PROBE::equals).isPresent(),
				"Copied world lost its originally selected merge template");
		JsonObject root = profile.toJson();
		JsonObject manifest = root.getAsJsonObject("providers")
				.getAsJsonObject("cakeworld");
		require(helper, manifest != null,
				"Copied world lost the CakeWorld provider manifest");

		if (phase.equals("baseline")) {
			requireBaseline(helper, root, manifest);
		} else {
			requireUpgrade(helper, root, manifest);
		}
		helper.succeed();
	}

	private static void requireBaseline(GameTestHelper helper,
			JsonObject root, JsonObject manifest) {
		require(helper,
				manifest.get("provider_revision").getAsInt() == 1701,
				"Baseline did not retain provider revision 1701");
		JsonObject rocks = root.getAsJsonObject("rocks");
		JsonObject ores = root.getAsJsonObject("ores");
		require(helper, rocks.has(EDITED_ROCK) && !rocks.has(ADDED_ROCK),
				"Baseline rock set does not match revision 1701");
		require(helper, ores.has(EDITED_ORE) && ores.has(DISABLED_ORE)
					&& ores.has(UNASSIGNED_ORE) && ores.has(REMOVED_ORE)
					&& !ores.has(ADDED_ORE) && !ores.has(PLAYER_CUSTOM_ORE),
				"Baseline ore set does not match revision 1701");
		require(helper, "cakeworld".equals(rocks.getAsJsonObject(
				EDITED_ROCK).get("source_provider").getAsString())
				&& "cakeworld".equals(ores.getAsJsonObject(
				EDITED_ORE).get("source_provider").getAsString()),
				"Baseline rules were not marked as provider-owned");
		requireArray(helper, manifest.getAsJsonArray("known_rocks"),
				List.of(EDITED_ROCK), "baseline known rocks");
		requireArray(helper, manifest.getAsJsonArray("known_ores"),
				List.of(DISABLED_ORE, EDITED_ORE, REMOVED_ORE,
						UNASSIGNED_ORE), "baseline known ores");
	}

	private static void requireUpgrade(GameTestHelper helper,
			JsonObject root, JsonObject manifest) {
		require(helper,
				manifest.get("provider_revision").getAsInt() == 1702,
				"Upgrade did not retain provider revision 1702");
		JsonObject rocks = root.getAsJsonObject("rocks");
		JsonObject ores = root.getAsJsonObject("ores");

		JsonObject editedRock = rocks.getAsJsonObject(EDITED_ROCK);
		require(helper, editedRock != null
					&& "cakeworld:biscuit_stone".equals(
							editedRock.get("block").getAsString())
					&& editedRock.get("weight").getAsDouble() == 7.25D
					&& "keep-rock-edit".equals(
							editedRock.get("player_note").getAsString()),
				"Provider upgrade overwrote the player-edited rock");
		JsonObject addedRock = rocks.getAsJsonObject(ADDED_ROCK);
		require(helper, addedRock != null
					&& "cakeworld:nougat_rock".equals(
							addedRock.get("block").getAsString())
					&& "cakeworld".equals(
							addedRock.get("source_provider").getAsString()),
				"Provider upgrade did not merge the newly introduced rock");

		JsonObject editedOre = ores.getAsJsonObject(EDITED_ORE);
		JsonObject editedDimension = editedOre == null ? null
				: editedOre.getAsJsonObject("dimensions")
						.getAsJsonObject("minecraft:overworld");
		require(helper, editedOre != null && editedDimension != null
					&& "cakeworld:sprinkle_cluster".equals(
							editedOre.get("block").getAsString())
					&& editedDimension.get("frequency").getAsDouble() == 7.5D
					&& "keep-ore-edit".equals(
							editedOre.get("player_note").getAsString()),
				"Provider upgrade overwrote the player-edited ore");
		JsonObject disabledOre = ores.getAsJsonObject(DISABLED_ORE);
		require(helper, disabledOre != null
					&& !disabledOre.get("enabled").getAsBoolean()
					&& "cakeworld:mint_crystal".equals(
							disabledOre.get("block").getAsString()),
				"Provider upgrade re-enabled or overwrote a disabled rule");
		require(helper, !ores.has(UNASSIGNED_ORE),
				"Provider upgrade resurrected an explicitly unassigned rule");

		JsonObject removedOre = ores.getAsJsonObject(REMOVED_ORE);
		require(helper, removedOre != null
					&& removedOre.get("orphaned_provider").getAsBoolean()
					&& "cakeworld:rock_candy_deposit".equals(
							removedOre.get("block").getAsString()),
				"Removed provider rule was not retained as an orphaned snapshot");
		JsonObject addedOre = ores.getAsJsonObject(ADDED_ORE);
		require(helper, addedOre != null
					&& "cakeworld".equals(
							addedOre.get("source_provider").getAsString()),
				"Provider upgrade did not merge the newly introduced ore");
		JsonObject customOre = ores.getAsJsonObject(PLAYER_CUSTOM_ORE);
		require(helper, customOre != null
					&& "keep-custom-rule".equals(
							customOre.get("player_note").getAsString())
					&& customOre.getAsJsonObject("dimensions")
							.getAsJsonObject("minecraft:overworld")
							.get("frequency").getAsDouble() == 0.25D,
				"Provider upgrade discarded the player-owned custom rule");
		JsonObject aliases = root.getAsJsonObject("worldgen_aliases");
		require(helper, aliases != null
					&& "cakeworld:sprinkle_cluster".equals(aliases.get(
							"cakeworld:burnt_sugar_rock").getAsString()),
				"Provider upgrade discarded the world-owned alias");

		requireArray(helper, manifest.getAsJsonArray("known_rocks"),
				List.of(ADDED_ROCK, EDITED_ROCK), "upgraded known rocks");
		requireArray(helper, manifest.getAsJsonArray("known_ores"),
				List.of(ADDED_ORE, DISABLED_ORE, EDITED_ORE, REMOVED_ORE,
						UNASSIGNED_ORE), "upgraded known ores");
		requireArray(helper, manifest.getAsJsonArray("known_templates"),
				List.of(MERGE_PROBE.toString()), "upgraded known templates");
	}

	private static void requireArray(GameTestHelper helper,
			JsonArray actual, List<String> expected, String label) {
		require(helper, actual != null && actual.size() == expected.size(),
				"Unexpected " + label + " count");
		for (int index = 0; index < expected.size(); index++) {
			JsonElement value = actual.get(index);
			require(helper, expected.get(index).equals(value.getAsString()),
					"Unexpected " + label + " entry at " + index);
		}
	}

	private static JsonObject packagedProvider(GameTestHelper helper) {
		try (InputStreamReader reader = new InputStreamReader(
				ProviderMergeGameTests.class.getResourceAsStream(
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
