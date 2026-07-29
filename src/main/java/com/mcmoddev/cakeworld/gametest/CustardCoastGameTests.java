package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.WaferTurtle;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CustardCoastKitchenFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Contract proof for the first functional Custard Coast ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class CustardCoastGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("custard_coast");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private CustardCoastGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow017")
	public static void coastHasTurtlesProfileFoodAndSoftPudding(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome coast = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, coast != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.BEACH
						&& close(coast.getBaseTemperature(), 0.8D)
						&& close(coast.getDownfall(), 0.4D),
				"Custard Coast is not a mild Beach-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.BEACH),
				"Custard Coast dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				coast.getAmbientAdditions().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.CUSTARD_COAST_LAP.getId())
						&& close(ambience.getTickChance(), 0.0013D),
				"Custard Coast lost its subtitled pudding lap");

		MobSpawnSettings.SpawnerData turtle = findSpawn(
				coast, CakeWorldEntities.WAFER_TURTLE.get());
		require(helper, turtle != null
						&& coast.getMobSettings()
								.getMobs(MobCategory.CREATURE)
								.unwrap().contains(turtle)
						&& turtle.getWeight().asInt() == 5
						&& turtle.minCount == 2
						&& turtle.maxCount == 5
						&& findSpawn(coast,
								EntityType.TURTLE) == null,
				"Custard Coast lost its exact Wafer Turtle replacement");
		assertSpawnReplacement(helper, coast,
				MobCategory.MONSTER,
				EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER.get(),
				95, 4, 4);
		assertSpawnReplacement(helper, coast,
				MobCategory.MONSTER,
				EntityType.CREEPER,
				CakeWorldEntities.POP_ROCK_POPPER.get(),
				100, 4, 4);
		assertSpawnReplacement(helper, coast,
				MobCategory.AMBIENT,
				EntityType.BAT,
				CakeWorldEntities.BONBON_BAT.get(),
				10, 8, 8);
		require(helper,
				CakeWorldBlocks.CUSTARD_PUDDING.get()
						.defaultBlockState().is(BlockTags.SAND)
						&& CakeWorldBlocks.CUSTARD_PUDDING.get()
								.defaultBlockState().is(
										BlockTags
												.ANIMALS_SPAWNABLE_ON),
				"Custard Pudding no longer supports Wafer Turtle nesting or ordinary animals");
		require(helper,
				hasPlacedFeature(coast,
						CustardCoastKitchenFeature.ID),
				"Custard Coast lost its seaside kitchen");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.CARAMEL_BOGS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							CustardCoastKitchenFeature.ID),
					"Custard Coast Kitchen leaked into " + other);
		}

		FoodProperties tart = CakeWorldItems.CUSTARD_TART.get()
				.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("custard_tart"))
						.orElse(null);
		require(helper, tart != null
						&& tart.getNutrition() == 7
						&& close(tart.getSaturationModifier(), 0.75D)
						&& tart.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== MobEffects.ABSORPTION
										&& entry.getFirst()
												.getDuration() == 200
										&& close(entry.getSecond(),
												1.0D))
						&& recipe != null
						&& recipe.getIngredients().size() == 4
						&& recipe.getResultItem().is(
								CakeWorldItems.CUSTARD_TART.get())
						&& recipe.getResultItem().getCount() == 2
						&& CakeWorldFluids.CUSTARD_BUCKET.get()
								.hasCraftingRemainingItem()
						&& CakeWorldFluids.CUSTARD_BUCKET.get()
								.getCraftingRemainingItem()
								== Items.BUCKET,
				"Custard Coast lost its two-serving, bowl-returning comfort recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 27,
				"Custard Coast ecosystem requires provider revision 27");
		JsonObject templates = provider.getAsJsonObject("templates");
		JsonObject first = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = templates.getAsJsonObject(template)
					.getAsJsonObject("profile");
			JsonObject geomes = profile
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject(
							"cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					geomes.size() == 2
							&& close(geomes.get(
									"cakeworld:wafer_shelf")
									.getAsDouble(), 14.0D)
							&& close(geomes.get(
									"cakeworld:cocoa_basin")
									.getAsDouble(), 4.0D)
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.6D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:beach",
											"minecraft:snowy_beach",
											"minecraft:stony_shore"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), -1.0D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 2.0D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.0D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 1.0D)
							&& "cakeworld:custard_pudding"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:custard_pudding"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:custard_pudding"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 4,
					template + " lost the Custard Coast profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Custard Coast profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow017",
			timeoutTicks = 800)
	public static void kitchenIsBoundedSoftAndPersistent(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CustardCoastKitchenFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== CustardCoastKitchenFeature.FEATURE
						&& CustardCoastKitchenFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& CustardCoastKitchenFeature
								.MAX_TERRAIN_RELIEF == 3
						&& CustardCoastKitchenFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Custard Coast Kitchen registration or bounded constants changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0) instanceof RarityFilter
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3) instanceof BiomeFilter,
				"Custard Coast Kitchen lost its bounded surface chain");

		BlockPos helperPos =
				helper.absolutePos(new BlockPos(4, 4, 4));
		ChunkPos chunk = new ChunkPos(helperPos);
		BlockPos fixture = new BlockPos(
				chunk.getMinBlockX() + 7,
				level.getMaxBuildHeight() - 12,
				chunk.getMinBlockZ() + 7);
		Set<Rotation> orientations = new HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					CustardCoastKitchenFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83, 64,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Custard Coast Kitchen did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					CustardCoastKitchenFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Custard Coast Kitchen crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					CustardCoastKitchenFeature.buildAt(
							level, fixture, rotation),
					"Custard Coast Kitchen refused a safe fixture for "
							+ rotation + ": "
							+ CustardCoastKitchenFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertKitchen(helper, level, fixture,
					rotation, false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.CUSTARD_PUDDING.get(),
							0) == 64
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK.get(),
									0) == 40
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(),
									0) == 16
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0) == 6
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GINGERBREAD_BRICKS
											.get(),
									0) == 8
							&& palette.getOrDefault(
									CakeWorldFluids
											.CUSTARD_BLOCK.get(),
									0) == 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MIXING_BOWL.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks.OVEN.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOLING_RACK.get(),
									0) == 1,
					"Custard Coast Kitchen lost its exact pudding, pier, canopy, basin or workstation palette: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Custard Coast Kitchen created an entity or block entity");
		}

		BlockState pudding = CakeWorldBlocks.CUSTARD_PUDDING
				.get().defaultBlockState();
		Pig fallTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		fallTester.setHealth(10.0F);
		pudding.getBlock().fallOn(level, pudding,
				fixture, fallTester, 11.0F);
		require(helper,
				close(fallTester.getHealth(), 6.0D)
						&& close(pudding.getBlock()
								.getSpeedFactor(), 0.85D)
						&& close(pudding.getBlock()
								.getFriction(), 0.75D),
				"Custard Pudding lost its half-damage, gentle-slow or soft-grip contract");

		prepare(level, fixture);
		level.setBlock(
				CustardCoastKitchenFeature.local(
						fixture, Rotation.NONE, 5, 1, 5),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!CustardCoastKitchenFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Custard Coast Kitchen accepted an authored obstacle");
		prepare(level, fixture);
		level.setBlock(
				CustardCoastKitchenFeature.local(
						fixture, Rotation.NONE, 5, 0, 5),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!CustardCoastKitchenFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Custard Coast Kitchen accepted a waterlogged ground cell");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow017world",
			timeoutTicks = 24000)
	public static void focusedNaturalCustardCoastAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos coast = locateBiome(helper, level, BIOME_ID);
		LocatedKitchen kitchen = locateNaturalKitchen(
				helper, level, coast, 16);
		ChunkPos chunk = new ChunkPos(kitchen.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			BlockPos sentinel =
					CustardCoastKitchenFeature.local(
							kitchen.centre(),
							kitchen.rotation(),
							-2, 6, 1);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertKitchen(helper, level,
					kitchen.centre(),
					kitchen.rotation(),
					brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, kitchen.centre());
			SurfaceAudit surface = auditSurface(
					level, kitchen.centre(), 24);
			ResourceLocation biome = level.getBiome(
					kitchen.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, kitchen.centre());
			int turtles = level.getEntitiesOfClass(
					WaferTurtle.class,
					new AABB(kitchen.centre())
							.inflate(48.0D, 24.0D, 48.0D))
					.size();
			LOGGER.info("Custard Coast audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, coastColumns={}, turtles={}, surface={}",
					kitchen.centre(), biome,
					kitchen.rotation(), palette,
					blockEntities, brickSentinel,
					kitchen.scannedChunks(),
					kitchen.markerCandidates(),
					kitchen.coastColumns(), turtles, surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CUSTARD_PUDDING.get(),
									0) == 64
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK.get(),
									0) == 40
							&& palette.getOrDefault(
									CakeWorldFluids
											.CUSTARD_BLOCK.get(),
									0) == 2
							&& surface.coastColumns() >= 64
							&& surface.custardPudding() >= 64,
					"Natural Custard Coast lost its biome, pudding shore or complete seaside kitchen: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the kitchen roof");
			}
			level.setChunkForced(chunk.x, chunk.z, false);
			helper.succeed();
		});
	}

	private static BlockPos locateBiome(GameTestHelper helper,
			ServerLevel level, ResourceLocation biomeId) {
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				holder -> holder.unwrapKey()
						.map(key -> key.location().equals(biomeId))
						.orElse(false),
				new BlockPos(0, 64, 0), 16384, 8);
		require(helper, match != null,
				"Could not locate " + biomeId
						+ " within 16,384 blocks");
		return match.getFirst();
	}

	private static LocatedKitchen locateNaturalKitchen(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int coastColumns = 0;
		for (int radius = 0; radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius; chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius; chunkZ++) {
					if (radius > 0
							&& chunkX != anchorChunk.x - radius
							&& chunkX != anchorChunk.x + radius
							&& chunkZ != anchorChunk.z - radius
							&& chunkZ != anchorChunk.z + radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					for (int x = chunkX << 4;
							x < (chunkX + 1) << 4; x++) {
						for (int z = chunkZ << 4;
								z < (chunkZ + 1) << 4; z++) {
							int topY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							ResourceLocation biome =
									level.getBiome(new BlockPos(
											x, topY, z))
											.unwrapKey()
											.map(ResourceKey::location)
											.orElse(null);
							if (BIOME_ID.equals(biome)) {
								coastColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									topY - 12);
									y <= topY; y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								if (!level.getBlockState(marker)
										.is(CakeWorldBlocks.OVEN.get())) {
									continue;
								}
								markerCandidates++;
								for (Rotation rotation :
										Rotation.values()) {
									BlockPos offset =
											new BlockPos(0, 2, 3)
													.rotate(rotation);
									BlockPos centre =
											marker.subtract(offset);
									if (matchesKitchen(level,
											centre, rotation,
											true)) {
										return new LocatedKitchen(
												centre, rotation,
												scannedChunks,
												markerCandidates,
												coastColumns);
									}
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Custard Coast survey found no natural seaside kitchen after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " oven candidates near "
						+ anchor + "; coastColumns="
						+ coastColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int coastColumns = 0;
		int custardPudding = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				int topY = level.getHeight(
						Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES,
						x, z) - 1;
				if (!level.getBiome(new BlockPos(
						x, topY, z)).is(BIOME_KEY)) {
					continue;
				}
				coastColumns++;
				for (int y = Math.max(
						level.getMinBuildHeight(),
						topY - 8); y <= topY; y++) {
					if (level.getBlockState(
							new BlockPos(x, y, z))
							.is(CakeWorldBlocks
									.CUSTARD_PUDDING.get())) {
						custardPudding++;
					}
				}
			}
		}
		return new SurfaceAudit(
				coastColumns, custardPudding);
	}

	private static boolean matchesKitchen(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
						: CakeWorldBlocks.CUSTARD_PUDDING.get();
				if (!state(level, centre, rotation, x, 1, z)
						.is(expected)) {
					return false;
				}
			}
		}
		for (int x = -1; x <= 1; x++) {
			if (!state(level, centre, rotation, x, 1, -5)
					.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
				return false;
			}
		}
		for (int x : new int[] {-2, 2}) {
			for (int z : new int[] {1, 4}) {
				for (int y = 2; y <= 4; y++) {
					if (!state(level, centre, rotation,
							x, y, z).is(CakeWorldBlocks
									.CANDY_CANE_PILLAR.get())) {
						return false;
					}
				}
				BlockState cap = state(level, centre,
						rotation, x, 6, z);
				if (!cap.is(CakeWorldBlocks.CANDY_GLASS.get())
						&& !(allowBrickSentinel
								&& x == -2 && z == 1
								&& cap.is(Blocks.BRICKS))) {
					return false;
				}
			}
		}
		for (int x = -2; x <= 2; x++) {
			for (int z = 1; z <= 4; z++) {
				if (!state(level, centre, rotation, x, 5, z)
						.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
					return false;
				}
			}
		}
		if (!state(level, centre, rotation, -1, 2, 3)
				.is(CakeWorldBlocks.MIXING_BOWL.get())
				|| !state(level, centre, rotation, 0, 2, 3)
						.is(CakeWorldBlocks.OVEN.get())
				|| !state(level, centre, rotation, 1, 2, 3)
						.is(CakeWorldBlocks.COOLING_RACK.get())) {
			return false;
		}
		for (int[] basin :
				CustardCoastKitchenFeature.basins()) {
			for (int[] direction : new int[][] {
				{-1, 0}, {1, 0}, {0, -1}, {0, 1}
			}) {
				if (!state(level, centre, rotation,
						basin[0] + direction[0], 2,
						basin[1] + direction[1])
						.is(CakeWorldBlocks
								.GINGERBREAD_BRICKS.get())) {
					return false;
				}
			}
			if (!state(level, centre, rotation,
					basin[0], 2, basin[1])
					.is(CakeWorldFluids
							.CUSTARD_BLOCK.get())) {
				return false;
			}
		}
		for (int x : new int[] {-1, 1}) {
			for (int y = 2; y <= 3; y++) {
				if (!state(level, centre, rotation, x, y, -5)
						.is(CakeWorldBlocks
								.CANDY_CANE_PILLAR.get())) {
					return false;
				}
			}
			if (!state(level, centre, rotation, x, 4, -5)
					.is(CakeWorldBlocks.CANDY_GLASS.get())) {
				return false;
			}
		}
		return true;
	}

	private static BlockState state(ServerLevel level,
			BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return level.getBlockState(
				CustardCoastKitchenFeature.local(
						centre, rotation, x, y, z));
	}

	private static void assertKitchen(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		require(helper,
				matchesKitchen(level, centre, rotation,
						allowBrickSentinel),
				"Custard Coast Kitchen lost its pudding floor, pier, canopy, contained basins or three workstations");
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 6; y++) {
				for (int z = -5; z <= 5; z++) {
					Block block = level.getBlockState(
							centre.offset(x, y, z))
							.getBlock();
					palette.merge(block, 1, Integer::sum);
				}
			}
		}
		return palette;
	}

	private static int countBlockEntities(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -5; x <= 5; x++) {
			for (int y = 0; y <= 6; y++) {
				for (int z = -5; z <= 5; z++) {
					if (level.getBlockEntity(
							centre.offset(x, y, z))
							!= null) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private static void prepare(ServerLevel level,
			BlockPos centre) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				BlockPos floor = centre.offset(x, 0, z);
				level.setBlock(floor.below(),
						CakeWorldBlocks.BISCUIT_STONE.get()
								.defaultBlockState(),
						2);
				level.setBlock(floor,
						CakeWorldBlocks.CUSTARD_PUDDING.get()
								.defaultBlockState(),
						2);
				for (int y = 1; y <= 6; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type) {
		for (MobCategory category : MobCategory.values()) {
			for (MobSpawnSettings.SpawnerData spawn :
					biome.getMobSettings()
							.getMobs(category).unwrap()) {
				if (spawn.type == type) {
					return spawn;
				}
			}
		}
		return null;
	}

	private static void assertSpawnReplacement(
			GameTestHelper helper, Biome biome,
			MobCategory category, EntityType<?> vanilla,
			EntityType<?> replacement, int weight,
			int minimum, int maximum) {
		MobSpawnSettings.SpawnerData spawn =
				findSpawn(biome, replacement);
		require(helper, spawn != null
						&& biome.getMobSettings()
								.getMobs(category)
								.unwrap().contains(spawn)
						&& spawn.getWeight().asInt() == weight
						&& spawn.minCount == minimum
						&& spawn.maxCount == maximum
						&& findSpawn(biome, vanilla) == null,
				"Custard Coast lost its "
						+ replacement.getRegistryName()
						+ " replacement for "
						+ vanilla.getRegistryName());
	}

	private static boolean hasPlacedFeature(Biome biome,
			ResourceLocation expected) {
		int step = GenerationStep.Decoration
				.TOP_LAYER_MODIFICATION.ordinal();
		if (biome == null
				|| biome.getGenerationSettings()
						.features().size() <= step) {
			return false;
		}
		for (Holder<PlacedFeature> feature
				: biome.getGenerationSettings()
						.features().get(step)) {
			if (feature.unwrapKey()
					.map(key -> key.location().equals(expected))
					.orElse(false)) {
				return true;
			}
		}
		return false;
	}

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element -> values.add(
				element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				CustardCoastGameTests.class.getResourceAsStream(
						"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader)
					.getAsJsonObject();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Could not read generated CakeWorld provider",
					exception);
		}
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private static boolean close(double actual,
			double expected) {
		return Math.abs(actual - expected) < 0.00001D;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private record LocatedKitchen(
			BlockPos centre,
			Rotation rotation,
			int scannedChunks,
			int markerCandidates,
			int coastColumns) {
	}

	private record SurfaceAudit(
			int coastColumns,
			int custardPudding) {
	}
}
