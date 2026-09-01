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
import com.mcmoddev.cakeworld.block.PoppingKernelStalkBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.PrairiePoppingPatchFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Contract proof for the first functional Popcorn Prairie ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class PopcornPrairieGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("popcorn_prairie");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final TagKey<Block> SHEEP_GRAZING =
			TagKey.create(Registry.BLOCK_REGISTRY, id(
					"candyfloss_sheep_grazing_surfaces"));
	private static final TagKey<Block> PIG_FORAGING =
			TagKey.create(Registry.BLOCK_REGISTRY, id(
					"truffle_pig_foraging_blocks"));

	private PopcornPrairieGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow015")
	public static void prairieHasOpenEcologyAndPublishedProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome prairie = registry.get(BIOME_ID);
		Biome source = registry.get(new ResourceLocation(
				"minecraft", "plains"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, prairie != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.PLAINS
						&& close(prairie.getBaseTemperature(), 0.8D)
						&& close(prairie.getDownfall(), 0.35D),
				"Popcorn Prairie is not a warm, open Plains-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.PLAINS)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.DRY),
				"Popcorn Prairie dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				prairie.getAmbientAdditions().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.POPCORN_PRAIRIE_RUSTLE
										.getId())
						&& close(ambience.getTickChance(), 0.0014D),
				"Popcorn Prairie lost its subtitled crop rustle");

		assertExactReplacement(helper, source, prairie,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		assertExactReplacement(helper, source, prairie,
				EntityType.COW,
				CakeWorldEntities.COCOA_COW.get());
		assertExactReplacement(helper, source, prairie,
				EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get());
		assertExactReplacement(helper, source, prairie,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get());
		assertExactReplacement(helper, source, prairie,
				EntityType.HORSE,
				CakeWorldEntities.GINGERBREAD_PONY.get());
		assertExactReplacement(helper, source, prairie,
				EntityType.DONKEY,
				CakeWorldEntities.DOUGH_DONKEY.get());
		assertExactReplacement(helper, source, prairie,
				EntityType.CREEPER,
				CakeWorldEntities.POP_ROCK_POPPER.get());
		require(helper, List.of(
				EntityType.SHEEP, EntityType.COW,
				EntityType.PIG, EntityType.CHICKEN,
				EntityType.HORSE, EntityType.DONKEY,
				EntityType.CREEPER).stream()
				.noneMatch(type -> findSpawn(prairie, type) != null),
				"Popcorn Prairie leaked a literal vanilla farm or Creeper role");

		BlockState turf = CakeWorldBlocks.POPPED_CORN_TURF.get()
				.defaultBlockState();
		require(helper,
				turf.is(BlockTags.ANIMALS_SPAWNABLE_ON)
						&& turf.is(SHEEP_GRAZING)
						&& turf.is(PIG_FORAGING)
						&& PoppingKernelStalkBlock
								.supportsKernelStalk(turf),
				"Popped-Corn Turf lost its animal, grazing, foraging or crop support contracts");
		require(helper,
				hasPlacedFeature(prairie,
						PrairiePoppingPatchFeature.ID),
				"Popcorn Prairie lost its Prairie Popping Patch");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.GINGERBREAD_HEARTHLANDS.getId(),
				CakeWorldBiomes.CUPCAKE_GARDENS.getId(),
				CakeWorldBiomes.LOLLIPOP_ORCHARDS.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							PrairiePoppingPatchFeature.ID),
					"Prairie Popping Patch leaked into " + other);
		}

		FoodProperties kernel =
				CakeWorldItems.POPCORN_KERNEL.get()
						.getFoodProperties();
		FoodProperties prepared =
				CakeWorldItems.CARAMEL_POPCORN.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("caramel_popcorn"))
						.orElse(null);
		require(helper, kernel != null
						&& kernel.getNutrition() == 1
						&& close(kernel
								.getSaturationModifier(), 0.1D)
						&& prepared != null
						&& prepared.getNutrition() == 6
						&& close(prepared
								.getSaturationModifier(), 0.65D)
						&& prepared.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst()
												.getEffect()
												== CakeWorldEffects
														.FIZZY_FEET
														.get()
										&& entry.getFirst()
												.getDuration() == 160
										&& close(entry.getSecond(),
												1.0D))
						&& recipe != null
						&& recipe.getResultItem().is(
								CakeWorldItems
										.CARAMEL_POPCORN.get())
						&& recipe.getResultItem().getCount() == 2,
				"Popcorn Prairie lost its emergency kernel and worthwhile two-serving Fizzy Feet recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 25,
				"Popcorn Prairie requires provider revision 25");
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
									.getAsInt() == 6
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 12
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.1D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:plains",
											"minecraft:sunflower_plains",
											"minecraft:meadow"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), 0.5D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 1.2D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.2D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 0.6D)
							&& "cakeworld:popped_corn_turf"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 4,
					template
							+ " lost the Popcorn Prairie profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Popcorn Prairie profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow015",
			timeoutTicks = 800)
	public static void patchIsBoundedRenewableAndPersistent(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				PrairiePoppingPatchFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== PrairiePoppingPatchFeature.FEATURE
						&& PrairiePoppingPatchFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& PrairiePoppingPatchFeature
								.MAX_TERRAIN_RELIEF == 3
						&& PrairiePoppingPatchFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Prairie Popping Patch registration or bounded constants changed");
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
				"Prairie Popping Patch lost its bounded surface-biome chain");

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
					PrairiePoppingPatchFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83, 96,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Prairie Popping Patch did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					PrairiePoppingPatchFeature
							.fitsWithinChunk(fixture,
									rotation, chunk),
					"Prairie Popping Patch crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					PrairiePoppingPatchFeature.buildAt(
							level, fixture, rotation),
					"Prairie Popping Patch refused a safe fixture for "
							+ rotation + ": "
							+ PrairiePoppingPatchFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertPatch(helper, level, fixture,
					rotation, false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.POPPED_CORN_TURF.get(),
							0) == 64
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK.get(),
									0) == 17
							&& palette.getOrDefault(
									CakeWorldBlocks
											.POPPING_KERNEL_STALK
											.get(),
									0) == 24
							&& palette.getOrDefault(
									CakeWorldBlocks
											.POPCORN_BLOCK.get(),
									0) == 17,
					"Prairie Popping Patch lost its exact turf, path, stalk or puff-block palette: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Prairie Popping Patch created an entity or block entity");
		}

		prepare(level, fixture);
		BlockPos stalkPos = fixture.offset(0, 1, 0);
		PoppingKernelStalkBlock stalk =
				(PoppingKernelStalkBlock) CakeWorldBlocks
						.POPPING_KERNEL_STALK.get();
		BlockState mature = stalk.getStateForAge(
				stalk.getMaxAge());
		level.setBlock(stalkPos.below(),
				CakeWorldBlocks.POPPED_CORN_TURF.get()
						.defaultBlockState(), 2);
		level.setBlock(stalkPos, mature, 2);
		FakePlayer player = FakePlayerFactory.getMinecraft(level);
		player.setDeltaMovement(Vec3.ZERO);
		player.fallDistance = 12.0F;
		stalk.use(mature, level, stalkPos, player,
				InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(stalkPos),
						Direction.UP, stalkPos, false));
		BlockState picked = level.getBlockState(stalkPos);
		require(helper,
				picked.is(stalk)
						&& picked.getValue(CropBlock.AGE)
								== PoppingKernelStalkBlock.PICKED_AGE
						&& player.getDeltaMovement().y > 0.0D
						&& player.fallDistance == 0.0F
						&& level.getEntitiesOfClass(
								ItemEntity.class,
								new AABB(stalkPos).inflate(2.0D))
								.stream().anyMatch(item ->
										item.getItem().is(
												CakeWorldItems
														.POPCORN_KERNEL
														.get())
										&& item.getItem()
												.getCount() >= 3),
				"Picking a mature Kernel Stalk did not pop safely, retain the plant and yield kernels");
		for (int attempt = 0; attempt < 8
				&& !stalk.isMaxAge(
						level.getBlockState(stalkPos)); attempt++) {
			BlockState growing =
					level.getBlockState(stalkPos);
			stalk.performBonemeal(level, level.random,
					stalkPos, growing);
		}
		require(helper,
				stalk.isMaxAge(level.getBlockState(stalkPos)),
				"Bone meal did not renew a picked Kernel Stalk");

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 3, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!PrairiePoppingPatchFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Prairie Popping Patch accepted an authored obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!PrairiePoppingPatchFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Prairie Popping Patch accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow015world",
			timeoutTicks = 24000)
	public static void focusedNaturalPrairiePatchAudit(
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
		BlockPos prairie = locateBiome(
				helper, level, BIOME_ID);
		LocatedPatch patch = locateNaturalPatch(
				helper, level, prairie, 16);
		ChunkPos chunk = new ChunkPos(patch.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					PrairiePoppingPatchFeature.orientation(
							level.getSeed(),
							patch.centre());
			BlockPos sentinel =
					PrairiePoppingPatchFeature.local(
							patch.centre(), rotation,
							0, 4, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertPatch(helper, level,
					patch.centre(), rotation,
					brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, patch.centre());
			SurfaceAudit surface = auditSurface(
					level, patch.centre(), 24);
			ResourceLocation biome = level.getBiome(
					patch.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, patch.centre());
			LOGGER.info("Prairie Popping Patch audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, prairieColumns={}, surface={}",
					patch.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					patch.scannedChunks(),
					patch.markerCandidates(),
					patch.prairieColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.POPCORN_BLOCK.get(),
									0)
									== (brickSentinel
											? 16 : 17)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.POPPING_KERNEL_STALK
											.get(),
									0) == 24
							&& surface.prairieColumns() >= 64
							&& surface.poppedCornTurfTops() >= 64
							&& surface.chocolateSponge() >= 128,
					"Natural Popcorn Prairie lost its biome, turf, sponge body or complete Popping Patch: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the popcorn arch");
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

	private static LocatedPatch locateNaturalPatch(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int prairieColumns = 0;
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
								prairieColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 18);
									y <= Math.min(
											level.getMaxBuildHeight() - 1,
											surfaceY + 8);
									y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								BlockState state =
										level.getBlockState(marker);
								if (!state.is(CakeWorldBlocks
										.POPCORN_BLOCK.get())
										&& !state.is(
												Blocks.BRICKS)) {
									continue;
								}
								markerCandidates++;
								BlockPos centre =
										marker.below(4);
								Rotation rotation =
										PrairiePoppingPatchFeature
												.orientation(
														level.getSeed(),
														centre);
								if (matchesPatch(level, centre,
										rotation, true)) {
									return new LocatedPatch(
											centre,
											scannedChunks,
											markerCandidates,
											prairieColumns);
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Popcorn Prairie survey found no natural Popping Patch after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " arch candidates near "
						+ anchor + "; prairieColumns="
						+ prairieColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int prairieColumns = 0;
		int poppedCornTurfTops = 0;
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
				prairieColumns++;
				boolean foundTop = false;
				for (int y = topY; y >= topY - 16; y--) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (!foundTop
							&& block == CakeWorldBlocks
									.POPPED_CORN_TURF.get()) {
						poppedCornTurfTops++;
						foundTop = true;
					}
					if (block == CakeWorldBlocks
							.CHOCOLATE_SPONGE.get()) {
						chocolateSponge++;
					}
				}
			}
		}
		return new SurfaceAudit(prairieColumns,
				poppedCornTurfTops, chocolateSponge);
	}

	private static boolean matchesPatch(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
						: CakeWorldBlocks.POPPED_CORN_TURF.get();
				if (!level.getBlockState(
						PrairiePoppingPatchFeature.local(
								centre, rotation, x, 1, z))
						.is(expected)) {
					return false;
				}
			}
		}
		for (int[] stalk :
				PrairiePoppingPatchFeature.stalks()) {
			BlockState state = level.getBlockState(
					PrairiePoppingPatchFeature.local(
							centre, rotation,
							stalk[0], 2, stalk[1]));
			if (!state.is(CakeWorldBlocks
					.POPPING_KERNEL_STALK.get())
					|| state.getValue(CropBlock.AGE) != 7) {
				return false;
			}
		}
		for (int[] stack : new int[][] {
				{-4, -4}, {4, -4}, {-4, 4}, {4, 4}}) {
			for (int y = 2; y <= 3; y++) {
				if (!level.getBlockState(
						PrairiePoppingPatchFeature.local(
								centre, rotation,
								stack[0], y, stack[1]))
						.is(CakeWorldBlocks
								.POPCORN_BLOCK.get())) {
					return false;
				}
			}
		}
		for (int x : new int[] {-2, 2}) {
			for (int y = 2; y <= 3; y++) {
				if (!level.getBlockState(
						PrairiePoppingPatchFeature.local(
								centre, rotation, x, y, 0))
						.is(CakeWorldBlocks
								.POPCORN_BLOCK.get())) {
					return false;
				}
			}
		}
		for (int x = -2; x <= 2; x++) {
			BlockState state = level.getBlockState(
					PrairiePoppingPatchFeature.local(
							centre, rotation, x, 4, 0));
			if (x == 0 && allowBrickSentinel) {
				if (!state.is(CakeWorldBlocks.POPCORN_BLOCK.get())
						&& !state.is(Blocks.BRICKS)) {
					return false;
				}
			} else if (!state.is(
					CakeWorldBlocks.POPCORN_BLOCK.get())) {
				return false;
			}
		}
		return true;
	}

	private static void assertPatch(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		require(helper,
				matchesPatch(level, centre, rotation,
						allowBrickSentinel),
				"Prairie Popping Patch lost its path, ripe kernel rows or lightweight popcorn arch");
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 6; y++) {
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
			for (int y = 1; y <= 6; y++) {
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
				for (int y = -8; y <= 8; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.POPPED_CORN_TURF.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void assertExactReplacement(
			GameTestHelper helper, Biome source,
			Biome prairie, EntityType<?> vanilla,
			EntityType<?> replacement) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(prairie, replacement);
		require(helper, expected != null
						&& actual != null
						&& actual.getWeight().asInt()
								== expected.getWeight().asInt()
						&& actual.minCount == expected.minCount
						&& actual.maxCount == expected.maxCount
						&& findSpawn(prairie, vanilla) == null,
				"Popcorn Prairie lost the exact "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ " replacement: expected=" + expected
						+ ", actual=" + actual);
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
				PopcornPrairieGameTests.class
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

	private record LocatedPatch(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int prairieColumns) {
	}

	private record SurfaceAudit(
			int prairieColumns,
			int poppedCornTurfTops,
			int chocolateSponge) {
	}
}
