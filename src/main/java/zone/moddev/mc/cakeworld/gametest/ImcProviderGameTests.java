package zone.moddev.mc.cakeworld.gametest;

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
	private static final int SELECTOR_OUTPUT_BASELINE = 57;
	private static final int WEIGHTED_SURVEY_SIDE = 8;
	private static final int MIN_DEEPSLATE_SELECTIONS = 1000;
	private static final int MIN_STONE_SELECTIONS = 250;
	private static final double MIN_WEIGHTED_RATIO = 2.5D;
	private static final double MAX_WEIGHTED_RATIO = 3.75D;
	private static final Logger LOGGER = LogUtils.getLogger();

	private ImcProviderGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void isolatedImcProviderFreezesWithOwnedRule(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.imcProviderFixture")
				&& CakeWorldImcProviderFixture.submitted(),
				"Conditional IMC provider was not submitted");
		require(helper, CakeWorldImcProviderFixture.pointPatternRegistered(),
				"Conditional one-candidate pattern was not registered");
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
						CakeWorldImcProviderFixture.SELECTOR_RULE_ID)
				&& profile.oreIds().contains(
						CakeWorldImcProviderFixture.WEIGHTED_HOST_RULE_ID),
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
		JsonObject selectorPattern = selector.getAsJsonObject("pattern");
		JsonObject overworldOverride = selectorRule
				.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:overworld");
		require(helper, selector.get("enabled").getAsBoolean()
				&& selector.get("frequency").getAsDouble() == 64.0D
				&& selector.get("min_y").getAsInt() == 32
				&& selector.get("max_y").getAsInt() == 47
				&& selector.get("quantity").getAsInt() == 1
				&& "cakeworld:imc_point_probe".equals(
						selectorPattern.get("type").getAsString())
				&& selector.getAsJsonArray("host_blocks").size() == 1
				&& !overworldOverride.get("enabled").getAsBoolean(),
				"Selector or explicit Overworld override drifted");
		JsonObject weightedRule = root.getAsJsonObject("ores")
				.getAsJsonObject(
						CakeWorldImcProviderFixture.WEIGHTED_HOST_RULE_ID
								.toString());
		JsonObject weightedSelector = weightedRule
				.getAsJsonObject("dimension_selectors")
				.getAsJsonObject("orespawn:all_except_nether_end");
		JsonObject weightedBlock = weightedSelector
				.getAsJsonArray("host_blocks").get(0).getAsJsonObject();
		JsonObject weightedTag = weightedSelector
				.getAsJsonArray("host_tags").get(0).getAsJsonObject();
		require(helper, weightedSelector.get("min_y").getAsInt() == -32
				&& weightedSelector.get("max_y").getAsInt() == 31
				&& "cakeworld:imc_point_probe".equals(weightedSelector
						.getAsJsonObject("pattern").get("type").getAsString())
				&& "minecraft:stone".equals(
						weightedBlock.get("block").getAsString())
				&& weightedBlock.get("weight").getAsDouble() == 0.25D
				&& "minecraft:deepslate_ore_replaceables".equals(
						weightedTag.get("tag").getAsString())
				&& weightedTag.get("weight").getAsDouble() == 0.75D
				&& !weightedRule.getAsJsonObject("dimensions")
						.getAsJsonObject("minecraft:overworld")
						.get("enabled").getAsBoolean(),
				"Weighted block/tag host declaration drifted");

		JsonObject manifest = root.getAsJsonObject("providers")
				.getAsJsonObject("cakeworld");
		JsonArray known = manifest.getAsJsonArray("known_ores");
		require(helper, manifest.get("provider_revision").getAsInt()
						== CakeWorldImcProviderFixture.REVISION
				&& known.size() == 3
				&& CakeWorldImcProviderFixture.RULE_ID.toString().equals(
						known.get(0).getAsString())
				&& CakeWorldImcProviderFixture.SELECTOR_RULE_ID.toString()
						.equals(known.get(1).getAsString())
				&& CakeWorldImcProviderFixture.WEIGHTED_HOST_RULE_ID
						.toString().equals(known.get(2).getAsString()),
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
		// The selector uses the fixture's single-candidate point pattern so its
		// exact result is independent of neighbouring chunk generation order.
		require(helper, pantryOutputs == SELECTOR_OUTPUT_BASELINE
				&& overworldOutputs == 0
				&& netherOutputs == 0 && endOutputs == 0,
				"Selector/explicit-dimension runtime boundary failed: pantry="
						+ pantryOutputs + ", overworld=" + overworldOutputs
						+ ", nether=" + netherOutputs + ", end="
						+ endOutputs);
		LOGGER.info("IMC selector audit: chunk={}, pantry={}, overworld={}, nether={}, end={}",
				SURVEY_CHUNK, pantryOutputs, overworldOutputs, netherOutputs,
				endOutputs);

		Block weightedOutput = ForgeRegistries.BLOCKS.getValue(
				CakeWorldImcProviderFixture.WEIGHTED_HOST_OUTPUT_ID);
		require(helper, weightedOutput != null && weightedOutput != Blocks.AIR,
				"Weighted-host output block is not registered");
		int deepslateSelections = 0;
		int stoneSelections = 0;
		for (int x = 0; x < WEIGHTED_SURVEY_SIDE; x++) {
			for (int z = 0; z < WEIGHTED_SURVEY_SIDE; z++) {
				ChunkPos chunk = new ChunkPos(SURVEY_CHUNK.x + x,
						SURVEY_CHUNK.z + z);
				deepslateSelections += countBlock(pantry, chunk,
						weightedOutput, -32, -1);
				stoneSelections += countBlock(pantry, chunk,
						weightedOutput, 0, 31);
			}
		}
		double weightedRatio = stoneSelections == 0
				? Double.POSITIVE_INFINITY
				: deepslateSelections / (double) stoneSelections;
		int weightedOverworld = countBlock(helper.getLevel(), SURVEY_CHUNK,
				weightedOutput);
		int weightedNether = countBlock(helper.getLevel().getServer()
				.getLevel(Level.NETHER), SURVEY_CHUNK, weightedOutput);
		int weightedEnd = countBlock(helper.getLevel().getServer()
				.getLevel(Level.END), SURVEY_CHUNK, weightedOutput);
		// Host weights are acceptance probabilities. Prove their distribution
		// over a large sample instead of freezing one incidental RNG outcome.
		require(helper, deepslateSelections >= MIN_DEEPSLATE_SELECTIONS
				&& stoneSelections >= MIN_STONE_SELECTIONS
				&& weightedRatio >= MIN_WEIGHTED_RATIO
				&& weightedRatio <= MAX_WEIGHTED_RATIO
				&& weightedOverworld == 0 && weightedNether == 0
				&& weightedEnd == 0,
				"Weighted host runtime boundary failed: deepslate="
						+ deepslateSelections + ", stone=" + stoneSelections
						+ ", ratio=" + weightedRatio + ", overworld="
						+ weightedOverworld + ", nether=" + weightedNether
						+ ", end=" + weightedEnd);
		LOGGER.info("IMC weighted-host audit: chunks={}x{} from {}, deepslate_tag_0.75={}, stone_block_0.25={}, ratio={}, overworld={}, nether={}, end={}",
				WEIGHTED_SURVEY_SIDE, WEIGHTED_SURVEY_SIDE, SURVEY_CHUNK,
				deepslateSelections, stoneSelections,
				weightedRatio, weightedOverworld, weightedNether, weightedEnd);
		helper.succeed();
	}

	private static int countBlock(ServerLevel level, ChunkPos chunk,
			Block target) {
		return countBlock(level, chunk, target,
				level == null ? 0 : level.getMinBuildHeight(),
				level == null ? -1 : level.getMaxBuildHeight() - 1);
	}

	private static int countBlock(ServerLevel level, ChunkPos chunk,
			Block target, int minY, int maxY) {
		if (level == null) return -1;
		level.getChunk(chunk.x, chunk.z);
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		int count = 0;
		for (int x = chunk.getMinBlockX(); x <= chunk.getMaxBlockX(); x++) {
			for (int z = chunk.getMinBlockZ(); z <= chunk.getMaxBlockZ(); z++) {
				for (int y = Math.max(minY, level.getMinBuildHeight());
						y <= Math.min(maxY,
								level.getMaxBuildHeight() - 1); y++) {
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
