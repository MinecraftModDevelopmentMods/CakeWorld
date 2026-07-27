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
import com.mcmoddev.cakeworld.block.CandySproutBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CupcakeBloomCircleFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
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
 * Contract proof for the first functional Cupcake Gardens ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class CupcakeGardensGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("cupcake_gardens");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private CupcakeGardensGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow012")
	public static void gardensHaveFrostingPollinatorsAndCupcakes(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome gardens = registry.get(BIOME_ID);
		Biome source = registry.get(new ResourceLocation(
				"minecraft", "sunflower_plains"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, gardens != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.PLAINS
						&& close(gardens.getBaseTemperature(), 0.75D)
						&& close(gardens.getDownfall(), 0.8D),
				"Cupcake Gardens is not a mild lush Sunflower-Plains-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.PLAINS)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.LUSH),
				"Cupcake Gardens dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				gardens.getAmbientAdditions().orElse(null);
		AmbientParticleSettings nectar =
				gardens.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.CUPCAKE_GARDENS_HUM
										.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& nectar != null
						&& nectar.getOptions().getType()
								== ParticleTypes.FALLING_NECTAR,
				"Cupcake Gardens lost its subtitled pollinator hum or nectar ambience");

		assertExactReplacement(helper, source, gardens,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		assertExactReplacement(helper, source, gardens,
				EntityType.COW,
				CakeWorldEntities.COCOA_COW.get());
		assertExactReplacement(helper, source, gardens,
				EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get());
		assertExactReplacement(helper, source, gardens,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get());
		assertExactReplacement(helper, source, gardens,
				EntityType.HORSE,
				CakeWorldEntities.GINGERBREAD_PONY.get());
		assertExactReplacement(helper, source, gardens,
				EntityType.DONKEY,
				CakeWorldEntities.DOUGH_DONKEY.get());
		assertSpawn(helper, gardens,
				CakeWorldEntities.SUGAR_BEE.get(),
				12, 2, 4);
		assertSpawn(helper, gardens,
				CakeWorldEntities.CUPCAKE_COW.get(),
				8, 4, 8);
		require(helper, gardens.getMobSettings()
						.getMobs(MobCategory.CREATURE)
						.unwrap().stream().noneMatch(spawn ->
								Set.of(EntityType.SHEEP,
										EntityType.COW,
										EntityType.PIG,
										EntityType.CHICKEN,
										EntityType.HORSE,
										EntityType.DONKEY,
										EntityType.BEE,
										EntityType.MOOSHROOM)
										.contains(spawn.type)),
				"Cupcake Gardens leaked a literal vanilla farm, mount, Bee or Mooshroom role");

		boolean animalSupport = CakeWorldBlocks.ICING.get()
				.defaultBlockState()
				.is(BlockTags.ANIMALS_SPAWNABLE_ON);
		boolean cupcakeCowSupport = CakeWorldBlocks.ICING.get()
				.defaultBlockState()
				.is(BlockTags.MOOSHROOMS_SPAWNABLE_ON);
		boolean sproutSupportValid =
				CandySproutBlock.supportsGardenPlant(
						CakeWorldBlocks.ICING.get()
								.defaultBlockState());
		require(helper,
				animalSupport && cupcakeCowSupport
						&& sproutSupportValid,
				"Solid Icing support mismatch: animals="
						+ animalSupport + ", cupcakeCows="
						+ cupcakeCowSupport + ", candySprouts="
						+ sproutSupportValid);

		require(helper,
				hasPlacedFeature(gardens,
						CupcakeBloomCircleFeature.ID),
				"Cupcake Gardens lost its Bloom Circle");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.GINGERBREAD_HEARTHLANDS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.WAFFLE_PLATEAUS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							CupcakeBloomCircleFeature.ID),
					"Cupcake Bloom Circle leaked into " + other);
		}

		FoodProperties cupcake =
				CakeWorldItems.HONEY_SPRINKLE_CUPCAKE.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("honey_sprinkle_cupcake"))
						.orElse(null);
		require(helper, cupcake != null
						&& cupcake.getNutrition() == 7
						&& close(cupcake
								.getSaturationModifier(), 0.8D)
						&& cupcake.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst()
												.getEffect()
												== CakeWorldEffects
														.SUGAR_RUSH
														.get()
										&& entry.getFirst()
												.getDuration() == 120
										&& close(entry.getSecond(),
												1.0D))
						&& recipe != null
						&& recipe.getResultItem().is(
								CakeWorldItems
										.HONEY_SPRINKLE_CUPCAKE
										.get())
						&& recipe.getResultItem().getCount() == 2,
				"Cupcake Gardens lost its worthwhile two-serving Honey Sprinkle Cupcake");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 22,
				"Cupcake Gardens requires provider revision 22");
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
					.getAsJsonObject("cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					geomes.size() == 2
							&& geomes.get(
									"cakeworld:cocoa_basin")
									.getAsInt() == 12
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 8
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.25D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:sunflower_plains",
											"minecraft:flower_forest",
											"minecraft:meadow"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), 0.3D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 1.2D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.35D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 1.0D)
							&& "cakeworld:icing"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:raspberry_gummy_block"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 4,
					template
							+ " lost the Cupcake Gardens profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Cupcake profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow012",
			timeoutTicks = 800)
	public static void bloomCircleIsBoundedHarvestableAndPersistent(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CupcakeBloomCircleFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== CupcakeBloomCircleFeature.FEATURE
						&& CupcakeBloomCircleFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 10
						&& CupcakeBloomCircleFeature
								.MAX_TERRAIN_RELIEF == 4
						&& CupcakeBloomCircleFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Cupcake Bloom Circle registration or bounded placement constants changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0)
								instanceof RarityFilter
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3)
								instanceof BiomeFilter,
				"Cupcake Bloom Circle lost its rare surface-biome chain");

		BlockPos helperPos =
				helper.absolutePos(new BlockPos(4, 4, 4));
		ChunkPos chunk = new ChunkPos(helperPos);
		BlockPos fixture = new BlockPos(
				chunk.getMinBlockX() + 7,
				level.getMaxBuildHeight() - 24,
				chunk.getMinBlockZ() + 7);
		Set<Rotation> orientations = new HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					CupcakeBloomCircleFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 79, 108,
									index * -101)));
		}
		require(helper, orientations.size() == 4,
				"Cupcake Bloom Circle did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					CupcakeBloomCircleFeature
							.fitsWithinChunk(fixture,
									rotation, chunk),
					"Cupcake Bloom Circle crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					CupcakeBloomCircleFeature.buildAt(
							level, fixture, rotation),
					"Cupcake Bloom Circle refused a safe fixture for "
							+ rotation + ": "
							+ CupcakeBloomCircleFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertBloomCircle(helper, level, fixture,
					rotation, false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.CHOCOLATE_SPONGE.get(),
							0) == 60
							&& palette.getOrDefault(
									CakeWorldBlocks.ICING.get(),
									0) == 33
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW.get(),
									0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GUMMY_BLOCK.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get(), 0) == 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get(), 0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get(), 0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_SPROUT.get(),
									0) == 12
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0) == 3,
					"Cupcake Bloom Circle lost its exact frosting garden, flavour blooms, mature sprouts or nectar bell: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Cupcake Bloom Circle created an entity or block entity");
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 3, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!CupcakeBloomCircleFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Cupcake Bloom Circle accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!CupcakeBloomCircleFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Cupcake Bloom Circle accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow012world",
			timeoutTicks = 24000)
	public static void focusedNaturalCupcakeBloomAudit(
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
		BlockPos gardens = locateBiome(helper, level, BIOME_ID);
		LocatedBloom bloom = locateNaturalBloom(
				helper, level, gardens, 32);
		ChunkPos chunk = new ChunkPos(bloom.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					CupcakeBloomCircleFeature.orientation(
							level.getSeed(),
							bloom.centre());
			BlockPos sentinel =
					CupcakeBloomCircleFeature.local(
							bloom.centre(), rotation,
							0, 2, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertBloomCircle(helper, level,
					bloom.centre(), rotation,
					brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, bloom.centre());
			SurfaceAudit surface = auditSurface(
					level, bloom.centre(), 24);
			ResourceLocation biome = level.getBiome(
					bloom.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, bloom.centre());
			LOGGER.info("Cupcake Bloom Circle audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, cupcakeColumns={}, surface={}",
					bloom.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					bloom.scannedChunks(),
					bloom.markerCandidates(),
					bloom.cupcakeColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0)
									== (brickSentinel
											? 2 : 3)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_SPROUT.get(),
									0) == 12
							&& surface.cupcakeColumns() >= 64
							&& surface.icingTops() >= 64
							&& surface.chocolateSponge() >= 128,
					"Natural Cupcake Gardens lost its biome, frosting surface, sponge body or complete Bloom Circle: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Cupcake nectar bell");
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

	private static LocatedBloom locateNaturalBloom(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int cupcakeColumns = 0;
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
							int surfaceY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							ResourceLocation biome =
									level.getBiome(new BlockPos(
											x, surfaceY, z))
											.unwrapKey()
											.map(ResourceKey::location)
											.orElse(null);
							if (BIOME_ID.equals(biome)) {
								cupcakeColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 16);
									y <= Math.min(
											level.getMaxBuildHeight() - 1,
											surfaceY + 3);
									y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								if (!level.getBlockState(marker)
										.is(CakeWorldBlocks
												.RASPBERRY_GUMMY_BLOCK
												.get())) {
									continue;
								}
								markerCandidates++;
								for (Rotation rotation
										: Rotation.values()) {
									BlockPos centre =
											marker.subtract(
													new BlockPos(
															0, 5, 0)
															.rotate(
																	rotation));
									if (CupcakeBloomCircleFeature
											.orientation(
													level.getSeed(),
													centre)
											== rotation
											&& matchesBloomCircle(
													level, centre,
													rotation,
													true)) {
										return new LocatedBloom(
												centre,
												scannedChunks,
												markerCandidates,
												cupcakeColumns);
									}
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Cupcake Gardens survey found no natural Bloom Circle after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " raspberry-gummy marker candidates near "
						+ anchor + "; cupcakeColumns="
						+ cupcakeColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int cupcakeColumns = 0;
		int icingTops = 0;
		int chocolateSponge = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				int topY = level.getHeight(
						Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES,
						x, z) - 1;
				BlockPos top = new BlockPos(x, topY, z);
				if (!level.getBiome(top).is(BIOME_KEY)) {
					continue;
				}
				cupcakeColumns++;
				boolean foundTop = false;
				for (int y = topY; y >= topY - 12; y--) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (!foundTop
							&& block == CakeWorldBlocks
									.ICING.get()) {
						icingTops++;
						foundTop = true;
					}
					if (block == CakeWorldBlocks
							.CHOCOLATE_SPONGE.get()) {
						chocolateSponge++;
					}
				}
			}
		}
		return new SurfaceAudit(cupcakeColumns,
				icingTops, chocolateSponge);
	}

	private static boolean matchesBloomCircle(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected;
				if (Math.abs(x) == 4 && Math.abs(z) == 4) {
					expected = CakeWorldBlocks.MARSHMALLOW.get();
				} else if (x == 0 || z == 0) {
					expected = CakeWorldBlocks.ICING.get();
				} else {
					expected = CakeWorldBlocks.CHOCOLATE_SPONGE
							.get();
				}
				if (!level.getBlockState(
						CupcakeBloomCircleFeature.local(
								centre, rotation, x, 1, z))
						.is(expected)) {
					return false;
				}
			}
		}
		Block[] flavours = {
				CakeWorldBlocks.GUMMY_BLOCK.get(),
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get(),
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get(),
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
		};
		for (int[] bloom : CupcakeBloomCircleFeature.blooms()) {
			int x = bloom[0];
			int z = bloom[1];
			if (!level.getBlockState(
					CupcakeBloomCircleFeature.local(
							centre, rotation, x, 2, z))
					.is(flavours[bloom[2]])
					|| !isMatureSprout(level,
							CupcakeBloomCircleFeature.local(
									centre, rotation,
									x, 3, z))) {
				return false;
			}
			for (int[] petal : new int[][] {
					{x - 1, z}, {x + 1, z},
					{x, z - 1}, {x, z + 1}}) {
				if (!level.getBlockState(
						CupcakeBloomCircleFeature.local(
								centre, rotation,
								petal[0], 2,
								petal[1]))
						.is(CakeWorldBlocks.ICING.get())) {
					return false;
				}
			}
		}
		for (int[] outer
				: CupcakeBloomCircleFeature.outerSprouts()) {
			if (!isMatureSprout(level,
					CupcakeBloomCircleFeature.local(
							centre, rotation,
							outer[0], 2, outer[1]))) {
				return false;
			}
		}
		for (int y = 2; y <= 4; y++) {
			BlockState state = level.getBlockState(
					CupcakeBloomCircleFeature.local(
							centre, rotation, 0, y, 0));
			if (!state.is(CakeWorldBlocks.CANDY_GLASS.get())
					&& !(allowBrickSentinel && y == 2
							&& state.is(Blocks.BRICKS))) {
				return false;
			}
		}
		return level.getBlockState(
				CupcakeBloomCircleFeature.local(
						centre, rotation, 0, 5, 0))
				.is(CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get());
	}

	private static boolean isMatureSprout(ServerLevel level,
			BlockPos position) {
		BlockState state = level.getBlockState(position);
		return state.is(CakeWorldBlocks.CANDY_SPROUT.get())
				&& state.getValue(CropBlock.AGE)
						== CropBlock.MAX_AGE;
	}

	private static void assertBloomCircle(
			GameTestHelper helper, ServerLevel level,
			BlockPos centre, Rotation rotation,
			boolean allowBrickSentinel) {
		require(helper,
				matchesBloomCircle(level, centre, rotation,
						allowBrickSentinel),
				"Cupcake Bloom Circle lost its frosting garden, rotated flavour blooms, harvest-ready sprouts or nectar bell");
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 5; y++) {
				for (int z = -5; z <= 5; z++) {
					palette.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(), 1, Integer::sum);
				}
			}
		}
		return palette;
	}

	private static int countBlockEntities(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 5; y++) {
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
		for (int x = -7; x <= 7; x++) {
			for (int z = -7; z <= 7; z++) {
				for (int y = -8; y <= 10; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.ICING.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void assertExactReplacement(
			GameTestHelper helper, Biome source,
			Biome gardens, EntityType<?> vanilla,
			EntityType<?> replacement) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(gardens, replacement);
		require(helper, expected != null
						&& actual != null
						&& actual.getWeight().asInt()
								== expected.getWeight().asInt()
						&& actual.minCount == expected.minCount
						&& actual.maxCount == expected.maxCount
						&& findSpawn(gardens, vanilla) == null,
				"Cupcake Gardens lost the exact "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ " replacement: expected=" + expected
						+ ", actual=" + actual);
	}

	private static void assertSpawn(GameTestHelper helper,
			Biome biome, EntityType<?> type,
			int weight, int minimum, int maximum) {
		MobSpawnSettings.SpawnerData spawn =
				findSpawn(biome, type);
		require(helper, spawn != null
						&& spawn.getWeight().asInt() == weight
						&& spawn.minCount == minimum
						&& spawn.maxCount == maximum,
				"Cupcake Gardens lost the exact "
						+ Registry.ENTITY_TYPE.getKey(type)
						+ " profile: " + spawn);
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type) {
		for (MobCategory category : MobCategory.values()) {
			for (MobSpawnSettings.SpawnerData spawn
					: biome.getMobSettings()
							.getMobs(category).unwrap()) {
				if (spawn.type == type) {
					return spawn;
				}
			}
		}
		return null;
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
		array.forEach(value -> values.add(value.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				CupcakeGardensGameTests.class
						.getResourceAsStream(
								"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Unable to read packaged provider", exception);
		}
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}

	private static boolean close(double actual,
			double expected) {
		return Math.abs(actual - expected) < 0.0001D;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private record LocatedBloom(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int cupcakeColumns) {
	}

	private record SurfaceAudit(
			int cupcakeColumns,
			int icingTops,
			int chocolateSponge) {
	}
}
