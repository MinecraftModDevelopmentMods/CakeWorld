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
import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.LollipopFruitBlock;
import com.mcmoddev.cakeworld.block.LollipopFruitBlock.Flavour;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.LollipopTastingGroveFeature;

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
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Contract proof for the first functional Lollipop Orchards ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class LollipopOrchardsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("lollipop_orchards");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final BlockPos NATURAL_AUDIT_ANCHOR =
			new BlockPos(8192, 64, 8192);
	private static final TagKey<Biome> COTTAGE_BIOMES =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/confectioners_cottage"));

	private LollipopOrchardsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow014")
	public static void orchardsHaveFlavoursLorikeetsAndCottages(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome orchards = registry.get(BIOME_ID);
		Biome source = registry.get(new ResourceLocation(
				"minecraft", "birch_forest"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, orchards != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.FOREST
						&& close(orchards.getBaseTemperature(), 0.7D)
						&& close(orchards.getDownfall(), 0.7D),
				"Lollipop Orchards is not a mild Birch-Forest-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.FOREST)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.LUSH),
				"Lollipop Orchards dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				orchards.getAmbientAdditions().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.LOLLIPOP_ORCHARDS_CHIME
										.getId())
						&& close(ambience.getTickChance(), 0.0011D),
				"Lollipop Orchards lost its subtitled candy chime");

		assertExactReplacement(helper, source, orchards,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		assertExactReplacement(helper, source, orchards,
				EntityType.COW,
				CakeWorldEntities.COCOA_COW.get());
		assertExactReplacement(helper, source, orchards,
				EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get());
		assertExactReplacement(helper, source, orchards,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get());
		assertSpawn(helper, orchards,
				CakeWorldEntities.SUGAR_BEE.get(),
				8, 1, 3);
		assertSpawn(helper, orchards,
				CakeWorldEntities.GUMMY_BUNNY.get(),
				4, 2, 3);
		assertSpawn(helper, orchards,
				CakeWorldEntities.LOLLIPOP_LORIKEET.get(),
				40, 1, 2);
		require(helper, List.of(
				EntityType.SHEEP, EntityType.COW,
				EntityType.PIG, EntityType.CHICKEN,
				EntityType.BEE, EntityType.RABBIT,
				EntityType.PARROT).stream()
				.noneMatch(type -> findSpawn(orchards, type) != null),
				"Lollipop Orchards leaked a literal vanilla farm, pollinator, bunny or parrot role");

		BlockState soil = CakeWorldBlocks.CANDIED_SOIL.get()
				.defaultBlockState();
		require(helper,
				soil.is(BlockTags.ANIMALS_SPAWNABLE_ON)
						&& soil.is(BlockTags.RABBITS_SPAWNABLE_ON)
						&& soil.is(BlockTags
								.PARROTS_SPAWNABLE_ON),
				"Candied Soil lost its orchard creature support tags");
		require(helper, holder.is(COTTAGE_BIOMES),
				"Lollipop Orchards lost the Confectioner's Cottage host tag");
		require(helper,
				hasPlacedFeature(orchards,
						LollipopTastingGroveFeature.ID),
				"Lollipop Orchards lost its Tasting Grove");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.GINGERBREAD_HEARTHLANDS.getId(),
				CakeWorldBiomes.GUMMY_JUNGLE.getId(),
				CakeWorldBiomes.CUPCAKE_GARDENS.getId(),
				CakeWorldBiomes.LIQUORICE_DARKWOOD.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							LollipopTastingGroveFeature.ID),
					"Lollipop Tasting Grove leaked into " + other);
		}

		FoodProperties lollipop =
				CakeWorldItems.RAINBOW_ORCHARD_LOLLIPOP.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("rainbow_orchard_lollipop"))
						.orElse(null);
		require(helper, lollipop != null
						&& lollipop.getNutrition() == 6
						&& close(lollipop
								.getSaturationModifier(), 0.7D)
						&& lollipop.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst()
												.getEffect()
												== CakeWorldEffects
														.SUGAR_RUSH
														.get()
										&& entry.getFirst()
												.getDuration() == 180
										&& close(entry.getSecond(),
												1.0D))
						&& recipe != null
						&& recipe.getResultItem().is(
								CakeWorldItems
										.RAINBOW_ORCHARD_LOLLIPOP
										.get())
						&& recipe.getResultItem().getCount() == 2,
				"Lollipop Orchards lost its worthwhile two-serving Sugar Rush recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 24,
				"Lollipop Orchards requires provider revision 24");
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
									.getAsInt() == 8
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 10
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.0D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:birch_forest",
											"minecraft:old_growth_birch_forest",
											"minecraft:flower_forest"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), 0.3D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 1.0D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.4D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 0.9D)
							&& "cakeworld:candied_soil"
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
							+ " lost the Lollipop Orchards profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Lollipop profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow014",
			timeoutTicks = 800)
	public static void groveIsBoundedRenewableAndPersistent(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				LollipopTastingGroveFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== LollipopTastingGroveFeature.FEATURE
						&& LollipopTastingGroveFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& LollipopTastingGroveFeature
								.MAX_TERRAIN_RELIEF == 4
						&& LollipopTastingGroveFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Lollipop Tasting Grove registration or bounded placement constants changed");
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
				"Lollipop Tasting Grove lost its bounded surface-biome chain");

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
					LollipopTastingGroveFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 79, 108,
									index * -101)));
		}
		require(helper, orientations.size() == 4,
				"Lollipop Tasting Grove did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					LollipopTastingGroveFeature
							.fitsWithinChunk(fixture,
									rotation, chunk),
					"Lollipop Tasting Grove crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					LollipopTastingGroveFeature.buildAt(
							level, fixture, rotation),
					"Lollipop Tasting Grove refused a safe fixture for "
							+ rotation + ": "
							+ LollipopTastingGroveFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertGrove(helper, level, fixture,
					rotation, false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.CANDIED_SOIL.get(),
							0) == 64
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK.get(),
									0) == 17
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR.get(),
									0) == 12
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LOLLIPOP_FRUIT.get(),
									0) == 25
							&& palette.getOrDefault(
									CakeWorldBlocks.ICING.get(),
									0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0) == 1,
					"Lollipop Tasting Grove lost its exact soil, path, trees, fruit or tasting stand: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Lollipop Tasting Grove created an entity or block entity");
		}

		prepare(level, fixture);
		BlockPos fruitPos = fixture.offset(0, 2, 0);
		LollipopFruitBlock fruit =
				(LollipopFruitBlock) CakeWorldBlocks
						.LOLLIPOP_FRUIT.get();
		BlockState lemon = fruit.defaultBlockState()
				.setValue(LollipopFruitBlock.FLAVOUR,
						Flavour.LEMON);
		level.setBlock(fruitPos, lemon, 2);
		fruit.performBonemeal(level, level.random,
				fruitPos, lemon);
		require(helper, level.getBlockState(fruitPos)
						.getValue(LollipopFruitBlock.RIPE),
				"Bone meal did not ripen Lollipop Fruit");
		fruit.use(level.getBlockState(fruitPos), level,
				fruitPos, FakePlayerFactory.getMinecraft(level),
				InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(fruitPos),
						Direction.UP, fruitPos, false));
		require(helper,
				!level.getBlockState(fruitPos)
						.getValue(LollipopFruitBlock.RIPE)
						&& level.getBlockState(fruitPos)
								.getValue(
										LollipopFruitBlock.FLAVOUR)
								== Flavour.LEMON
						&& level.getEntitiesOfClass(
								ItemEntity.class,
								new AABB(fruitPos).inflate(2.0D))
								.stream().anyMatch(item ->
										item.getItem().is(
												CakeWorldItems
														.BOILED_SWEET
														.get())),
				"Picking Lollipop Fruit did not preserve flavour, reset ripeness and drop a Boiled Sweet");

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 3, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!LollipopTastingGroveFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Lollipop Tasting Grove accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!LollipopTastingGroveFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Lollipop Tasting Grove accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow014world",
			timeoutTicks = 24000)
	public static void focusedNaturalLollipopGroveAudit(
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
		LocatedGrove grove = locateNaturalGrove(
				helper, level, NATURAL_AUDIT_ANCHOR, 12);
		ChunkPos chunk = new ChunkPos(grove.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					LollipopTastingGroveFeature.orientation(
							level.getSeed(),
							grove.centre());
			BlockPos sentinel =
					LollipopTastingGroveFeature.local(
							grove.centre(), rotation,
							0, 2, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertGrove(helper, level,
					grove.centre(), rotation,
					brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, grove.centre());
			SurfaceAudit surface = auditSurface(
					level, grove.centre(), 24);
			ResourceLocation biome = level.getBiome(
					grove.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, grove.centre());
			LOGGER.info("Lollipop Tasting Grove audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, orchardColumns={}, surface={}",
					grove.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					grove.scannedChunks(),
					grove.markerCandidates(),
					grove.orchardColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0)
									== (brickSentinel
											? 0 : 1)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LOLLIPOP_FRUIT.get(),
									0) == 25
							&& surface.orchardColumns() >= 64
							&& surface.candiedSoilTops() >= 64
							&& surface.chocolateSponge() >= 128,
					"Natural Lollipop Orchards lost its biome, candied surface, sponge body or complete Tasting Grove: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Lollipop tasting stand");
			}
			level.setChunkForced(chunk.x, chunk.z, false);
			helper.succeed();
		});
	}

	private static LocatedGrove locateNaturalGrove(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int orchardColumns = 0;
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
								orchardColumns++;
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
								BlockState stand =
										level.getBlockState(
												marker.below());
								if (!level.getBlockState(marker)
										.is(CakeWorldBlocks
												.LOLLIPOP_FRUIT
												.get())
										|| (!stand.is(CakeWorldBlocks
												.CANDY_GLASS
												.get())
												&& !stand.is(
														Blocks.BRICKS))) {
									continue;
								}
								markerCandidates++;
								BlockPos centre =
										marker.below(3);
								Rotation rotation =
										LollipopTastingGroveFeature
												.orientation(
														level.getSeed(),
														centre);
								if (matchesGrove(level, centre,
										rotation, true)) {
									return new LocatedGrove(
											centre,
											scannedChunks,
											markerCandidates,
											orchardColumns);
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Lollipop Orchards survey found no natural Tasting Grove after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " tasting-stand candidates near "
						+ anchor + "; orchardColumns="
						+ orchardColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int orchardColumns = 0;
		int candiedSoilTops = 0;
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
				orchardColumns++;
				boolean foundTop = false;
				for (int y = topY; y >= topY - 16; y--) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (!foundTop
							&& block == CakeWorldBlocks
									.CANDIED_SOIL.get()) {
						candiedSoilTops++;
						foundTop = true;
					}
					if (block == CakeWorldBlocks
							.CHOCOLATE_SPONGE.get()) {
						chocolateSponge++;
					}
				}
			}
		}
		return new SurfaceAudit(orchardColumns,
				candiedSoilTops, chocolateSponge);
	}

	private static boolean matchesGrove(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
						: CakeWorldBlocks.CANDIED_SOIL.get();
				if (!level.getBlockState(
						LollipopTastingGroveFeature.local(
								centre, rotation, x, 1, z))
						.is(expected)) {
					return false;
				}
			}
		}
		Flavour[] flavours = Flavour.values();
		for (int[] tree :
				LollipopTastingGroveFeature.trees()) {
			int x = tree[0];
			int z = tree[1];
			for (int y = 2; y <= 4; y++) {
				if (!level.getBlockState(
						LollipopTastingGroveFeature.local(
								centre, rotation, x, y, z))
						.is(CakeWorldBlocks
								.CANDY_CANE_PILLAR.get())) {
					return false;
				}
			}
			for (int[] fruit : new int[][] {
					{x, 5, z}, {x - 1, 5, z},
					{x + 1, 5, z}, {x, 5, z - 1},
					{x, 5, z + 1}, {x, 6, z}}) {
				BlockState state = level.getBlockState(
						LollipopTastingGroveFeature.local(
								centre, rotation,
								fruit[0], fruit[1], fruit[2]));
				if (!state.is(CakeWorldBlocks
						.LOLLIPOP_FRUIT.get())
						|| state.getValue(
								LollipopFruitBlock.FLAVOUR)
								!= flavours[tree[2]]
						|| !state.getValue(
								LollipopFruitBlock.RIPE)) {
					return false;
				}
			}
		}
		BlockState stand = level.getBlockState(
				LollipopTastingGroveFeature.local(
						centre, rotation, 0, 2, 0));
		return (stand.is(CakeWorldBlocks.CANDY_GLASS.get())
					|| allowBrickSentinel
							&& stand.is(Blocks.BRICKS))
				&& level.getBlockState(
						LollipopTastingGroveFeature.local(
								centre, rotation, 0, 3, 0))
						.is(CakeWorldBlocks
								.LOLLIPOP_FRUIT.get());
	}

	private static void assertGrove(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		require(helper,
				matchesGrove(level, centre, rotation,
						allowBrickSentinel),
				"Lollipop Tasting Grove lost its bright path, four ripe flavour trees or tasting stand");
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 7; y++) {
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
			for (int y = 1; y <= 7; y++) {
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
						CakeWorldBlocks.CANDIED_SOIL.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void assertExactReplacement(
			GameTestHelper helper, Biome source,
			Biome orchards, EntityType<?> vanilla,
			EntityType<?> replacement) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(orchards, replacement);
		require(helper, expected != null
						&& actual != null
						&& actual.getWeight().asInt()
								== expected.getWeight().asInt()
						&& actual.minCount == expected.minCount
						&& actual.maxCount == expected.maxCount
						&& findSpawn(orchards, vanilla) == null,
				"Lollipop Orchards lost the exact "
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
				"Lollipop Orchards spawn mismatch for "
						+ Registry.ENTITY_TYPE.getKey(type)
						+ ": " + spawn);
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
				LollipopOrchardsGameTests.class
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

	private record LocatedGrove(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int orchardColumns) {
	}

	private record SurfaceAudit(
			int orchardColumns,
			int candiedSoilTops,
			int chocolateSponge) {
	}
}
