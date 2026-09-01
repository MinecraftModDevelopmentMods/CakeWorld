package com.mcmoddev.cakeworld.gametest;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Natural Nether proof for OreSpawn ceiling-surface replacement. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_ceiling_surface")
public final class CeilingSurfaceGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation NETHER_PALETTE =
			new ResourceLocation("cakeworld", "nether");
	private static final ResourceLocation TARGET_BIOME =
			CakeWorldBiomes.BLACK_LIQUORICE_LABYRINTHS.getId();
	private static final Logger LOGGER = LogUtils.getLogger();

	private CeilingSurfaceGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void blackLiquoriceUsesExposedNetherCeilings(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.fixedWorldgenEvidence"),
				"Ceiling-surface proof ran without the fixed-worldgen switch");
		ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
		require(helper, nether != null,
				"Ceiling-surface proof requires the Nether");
		requireSavedDeclaration(helper, nether);

		Pair<BlockPos, Holder<Biome>> located = nether.findNearestBiome(
				holder -> holder.unwrapKey().map(key -> key.location()
						.equals(TARGET_BIOME)).orElse(false),
				new BlockPos(0, 64, 0), 16384, 8);
		require(helper, located != null,
				"Could not locate Black Liquorice Labyrinths within 16,384 blocks");
		int centerChunkX = Math.floorDiv(located.getFirst().getX(), 16);
		int centerChunkZ = Math.floorDiv(located.getFirst().getZ(), 16);
		int targetFaces = 0;
		int targetOutputs = 0;
		int otherFaces = 0;
		int otherOutputs = 0;
		int roofTopOutputs = 0;
		Map<ResourceLocation, Integer> targetMisses = new LinkedHashMap<>();
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int chunkX = centerChunkX - 2; chunkX <= centerChunkX + 2;
				chunkX++) {
			for (int chunkZ = centerChunkZ - 2; chunkZ <= centerChunkZ + 2;
					chunkZ++) {
				ChunkAccess chunk = nether.getChunk(chunkX, chunkZ);
				for (int localX = 0; localX < 16; localX++) {
					for (int localZ = 0; localZ < 16; localZ++) {
						int x = chunk.getPos().getBlockX(localX);
						int z = chunk.getPos().getBlockZ(localZ);
						int y = chunk.getMaxBuildHeight() - 1;
						while (y >= chunk.getMinBuildHeight()
								&& open(chunk.getBlockState(
										cursor.set(x, y, z)))) y--;
						if (y < chunk.getMinBuildHeight()) continue;
						int roofTopY = y;
						while (y >= chunk.getMinBuildHeight()
								&& !open(chunk.getBlockState(
										cursor.set(x, y, z)))) y--;
						int ceilingY = y + 1;
						if (ceilingY <= chunk.getMinBuildHeight()) continue;
						BlockState ceiling = chunk.getBlockState(
								cursor.set(x, ceilingY, z));
						ResourceLocation biome = nether.getBiome(cursor)
								.unwrapKey().map(key -> key.location())
								.orElse(null);
						boolean output = ceiling.is(
								CakeWorldBlocks.BLACK_LIQUORICE_STONE.get());
						if (TARGET_BIOME.equals(biome)) {
							targetFaces++;
							if (output) {
								targetOutputs++;
							} else {
								targetMisses.merge(Registry.BLOCK.getKey(
									ceiling.getBlock()), 1, Integer::sum);
							}
						} else {
							otherFaces++;
							if (output) otherOutputs++;
						}
						if (chunk.getBlockState(cursor.set(x, roofTopY, z))
								.is(CakeWorldBlocks.BLACK_LIQUORICE_STONE.get())) {
							roofTopOutputs++;
						}
					}
				}
			}
		}

		LOGGER.info("Nether ceiling-surface audit: locate={}, targetFaces={}, targetOutputs={}, targetMisses={}, otherFaces={}, otherOutputs={}, roofTopOutputs={}",
				located.getFirst(), targetFaces, targetOutputs, targetMisses,
				otherFaces, otherOutputs, roofTopOutputs);
		require(helper, targetFaces > 0
				&& targetOutputs * 4 >= targetFaces * 3,
				"Black Liquorice did not cover at least three quarters of the final target-biome roof after later decorations: "
						+ targetOutputs + "/" + targetFaces
						+ ", misses=" + targetMisses);
		require(helper, otherFaces > 0 && otherOutputs == 0,
				"Black Liquorice ceiling material leaked into another biome: "
						+ otherOutputs + "/" + otherFaces);
		require(helper, roofTopOutputs == 0,
				"Ceiling material replaced the inaccessible roof top instead of only downward-exposed faces: "
						+ roofTopOutputs);
		helper.succeed();
	}

	private static void requireSavedDeclaration(GameTestHelper helper,
			ServerLevel level) {
		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				level.getServer()).orElseThrow();
		JsonObject palettes = profile.toJson().getAsJsonObject("biome_palettes");
		JsonObject nether = palettes == null ? null
				: palettes.getAsJsonObject(NETHER_PALETTE.toString());
		JsonObject biomes = nether == null ? null
				: nether.getAsJsonObject("biomes");
		JsonObject target = biomes == null ? null
				: biomes.getAsJsonObject(TARGET_BIOME.toString());
		JsonObject surface = target == null ? null
				: target.getAsJsonObject("surface");
		require(helper, surface != null
				&& "cakeworld:black_liquorice_stone".equals(
						surface.get("ceiling_block").getAsString()),
				"Saved profile lost the Black Liquorice ceiling declaration");
	}

	private static boolean open(BlockState state) {
		return state.isAir() || !state.getFluidState().isEmpty();
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
