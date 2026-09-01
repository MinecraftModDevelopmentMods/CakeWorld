package com.mcmoddev.cakeworld.gametest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/**
 * Integrated positive, excluded, and neutral controls for OreSpawn biome
 * filters. The fixture rules exist only in the explicitly selected Sampler
 * Platter profile.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_sampler_biome_filters")
public final class SamplerBiomeFilterGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation SAMPLER =
			new ResourceLocation("cakeworld", "sampler_platter");
	private static final ResourceLocation CANDY_PLAINS =
			new ResourceLocation("cakeworld", "candy_plains");
	private static final ResourceLocation COOKIE_FOREST =
			new ResourceLocation("cakeworld", "cookie_forest");
	private static final ResourceLocation PEPPERMINT_PINEWOODS =
			new ResourceLocation("cakeworld", "peppermint_pinewoods");
	private static final ResourceLocation VANILLA_FOREST =
			new ResourceLocation("minecraft", "forest");
	private static final ResourceLocation EXACT_RULE =
			new ResourceLocation("cakeworld", "ore/exact_biome_filter_probe");
	private static final ResourceLocation DICTIONARY_RULE =
			new ResourceLocation("cakeworld", "ore/dictionary_biome_filter_probe");
	private static final Set<ResourceLocation> TARGET_BIOMES = Set.of(
			CANDY_PLAINS, COOKIE_FOREST, PEPPERMINT_PINEWOODS,
			VANILLA_FOREST);
	private static final int CHUNKS_PER_BIOME = 3;
	private static final Logger LOGGER = LogUtils.getLogger();

	private SamplerBiomeFilterGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 1200)
	public static void exactAndDictionaryFiltersUseStableBiomeKeys(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.samplerPlatterEvidence"),
				"Biome-filter fixture ran without the explicit Sampler switch");
		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate().filter(SAMPLER::equals)
				.isPresent(),
				"Biome-filter fixture did not select the Sampler Platter");
		requireSavedRules(helper, profile.toJson());

		ServerLevel level = helper.getLevel();
		Map<ResourceLocation, List<ChunkPos>> chunks = locateChunks(level);
		Map<ResourceLocation, Counts> counts = new LinkedHashMap<>();
		for (ResourceLocation biome : TARGET_BIOMES) {
			int exact = 0;
			int dictionary = 0;
			for (ChunkPos chunkPos : chunks.get(biome)) {
				ChunkAccess chunk = level.getChunk(chunkPos.x, chunkPos.z);
				exact += countAtY(chunk,
						CakeWorldBlocks.COCOA_COAL.get(), -30);
				dictionary += countAtY(chunk,
						CakeWorldBlocks.IRON_WAFER.get(), -18);
			}
			counts.put(biome, new Counts(exact, dictionary));
		}

		require(helper, counts.get(CANDY_PLAINS).exact() > 0,
				"Exact include produced no Cocoa Coal in Candy Plains: " + counts);
		require(helper, counts.get(COOKIE_FOREST).exact() == 0,
				"Exact exclusion allowed Cocoa Coal in Cookie Forest: " + counts);
		require(helper, counts.get(PEPPERMINT_PINEWOODS).exact() == 0,
				"Exact include leaked Cocoa Coal into a neutral biome: " + counts);

		require(helper,
				counts.get(PEPPERMINT_PINEWOODS).dictionary() > 0
						&& counts.get(VANILLA_FOREST).dictionary() > 0,
				"FOREST dictionary include did not place in both CakeWorld and vanilla forests: "
						+ counts);
		require(helper, counts.get(COOKIE_FOREST).dictionary() == 0,
				"DENSE dictionary exclusion allowed Iron Wafer in Cookie Forest: "
						+ counts);
		require(helper, counts.get(CANDY_PLAINS).dictionary() == 0,
				"FOREST dictionary include leaked Iron Wafer into neutral Candy Plains: "
						+ counts);

		LOGGER.info("Sampler biome-filter audit: chunks={}, counts={}",
				chunks, counts);
		helper.succeed();
	}

	private static Map<ResourceLocation, List<ChunkPos>> locateChunks(
			ServerLevel level) {
		Map<ResourceLocation, List<ChunkPos>> result = new LinkedHashMap<>();
		for (ResourceLocation biome : TARGET_BIOMES) {
			result.put(biome, new ArrayList<>());
		}
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (int chunkX = -256; chunkX <= 256 && !complete(result); chunkX++) {
			for (int chunkZ = -256; chunkZ <= 256 && !complete(result); chunkZ++) {
				int blockX = (chunkX << 4) + 8;
				int blockZ = (chunkZ << 4) + 8;
				Holder<Biome> holder = generator.getNoiseBiome(
						QuartPos.fromBlock(blockX), QuartPos.fromBlock(0),
						QuartPos.fromBlock(blockZ));
				ResourceLocation id = holder.unwrapKey()
						.map(key -> key.location())
						.orElseGet(() -> registry.getKey(holder.value()));
				List<ChunkPos> selected = result.get(id);
				if (selected != null && selected.size() < CHUNKS_PER_BIOME) {
					selected.add(new ChunkPos(chunkX, chunkZ));
				}
			}
		}
		if (!complete(result)) {
			throw new AssertionError("Could not locate three sampler chunks for every filter control: "
					+ result);
		}
		return result;
	}

	private static boolean complete(
			Map<ResourceLocation, List<ChunkPos>> chunks) {
		return chunks.values().stream()
				.allMatch(value -> value.size() == CHUNKS_PER_BIOME);
	}

	private static int countAtY(ChunkAccess chunk, Block target, int y) {
		int count = 0;
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				cursor.set(chunk.getPos().getBlockX(x), y,
						chunk.getPos().getBlockZ(z));
				if (chunk.getBlockState(cursor).is(target)) count++;
			}
		}
		return count;
	}

	private static void requireSavedRules(GameTestHelper helper,
			JsonObject profile) {
		JsonObject ores = profile.getAsJsonObject("ores");
		JsonObject exact = ores == null ? null
				: ores.getAsJsonObject(EXACT_RULE.toString());
		JsonObject dictionary = ores == null ? null
				: ores.getAsJsonObject(DICTIONARY_RULE.toString());
		require(helper, exact != null && dictionary != null,
				"Saved Sampler profile omitted its biome-filter rules");
		JsonObject exactDimension = exact.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:overworld");
		JsonObject dictionaryDimension = dictionary.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:overworld");
		require(helper, strings(exactDimension, "biome_ids").equals(
				Set.of(CANDY_PLAINS.toString(), COOKIE_FOREST.toString()))
				&& strings(exactDimension, "excluded_biome_ids").equals(
						Set.of(COOKIE_FOREST.toString())),
				"Saved exact biome-filter declaration drifted");
		require(helper, strings(dictionaryDimension, "biome_dictionary")
				.equals(Set.of("FOREST"))
				&& strings(dictionaryDimension,
						"excluded_biome_dictionary").equals(Set.of("DENSE")),
				"Saved dictionary biome-filter declaration drifted");
	}

	private static Set<String> strings(JsonObject parent, String key) {
		JsonArray array = parent.getAsJsonArray(key);
		java.util.LinkedHashSet<String> result = new java.util.LinkedHashSet<>();
		for (int index = 0; index < array.size(); index++) {
			result.add(array.get(index).getAsString());
		}
		return result;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private record Counts(int exact, int dictionary) {
	}
}
