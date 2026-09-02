package zone.moddev.mc.cakeworld.gametest;

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
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEffects;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.JellybeanCompassPicnicFeature;

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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Rotation;
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
 * Contract proof for BIO-OW-018 and STRUCT-034.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class JellybeanArchipelagoGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("jellybean_archipelago");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private JellybeanArchipelagoGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow018")
	public static void archipelagoHasPeacefulEcologyFizzAndProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome archipelago = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, archipelago != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.MUSHROOM
						&& close(archipelago
								.getBaseTemperature(), 0.9D)
						&& close(archipelago.getDownfall(), 1.0D),
				"Jellybean Archipelago is not a lush Mushroom Fields copy");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.MAGICAL)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.RARE)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.LUSH)
						&& !BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.MUSHROOM),
				"Jellybean Archipelago dictionary roles drifted");

		AmbientAdditionsSettings ambience =
				archipelago.getAmbientAdditions().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.JELLYBEAN_ARCHIPELAGO_CHIMES
										.getId())
						&& close(ambience.getTickChance(),
								0.0014D),
				"Jellybean Archipelago lost its subtitled island chimes");

		assertSpawnReplacement(helper, archipelago,
				MobCategory.CREATURE,
				EntityType.MOOSHROOM,
				CakeWorldEntities.CUPCAKE_COW.get(),
				8, 4, 8);
		assertSpawnReplacement(helper, archipelago,
				MobCategory.WATER_AMBIENT,
				EntityType.TROPICAL_FISH,
				CakeWorldEntities.JELLYBEAN_FISH.get(),
				25, 8, 8);
		assertSpawnReplacement(helper, archipelago,
				MobCategory.WATER_CREATURE,
				EntityType.DOLPHIN,
				CakeWorldEntities.SODA_DOLPHIN.get(),
				1, 1, 2);
		for (EntityType<?> vanilla : List.of(
				EntityType.ZOMBIE,
				EntityType.CREEPER,
				EntityType.BAT)) {
			require(helper, findSpawn(archipelago, vanilla) == null,
					"Jellybean Archipelago retained vanilla spawn "
							+ vanilla.getRegistryName());
		}

		require(helper,
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
						.defaultBlockState().is(
								BlockTags.ANIMALS_SPAWNABLE_ON)
						&& CakeWorldBlocks
								.RASPBERRY_GUMMY_BLOCK.get()
								.defaultBlockState().is(
										BlockTags
												.MOOSHROOMS_SPAWNABLE_ON),
				"Raspberry Gummy no longer supports island animals");
		require(helper,
				hasPlacedFeature(archipelago,
						JellybeanCompassPicnicFeature.ID),
				"Jellybean Archipelago lost its compass picnic");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.GUMMY_JUNGLE.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							JellybeanCompassPicnicFeature.ID),
					"Jellybean Compass Picnic leaked into " + other);
		}

		FoodProperties fizz = CakeWorldItems.ISLAND_HOP_FIZZ.get()
				.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("island_hop_fizz"))
						.orElse(null);
		require(helper, fizz != null
						&& fizz.getNutrition() == 6
						&& close(fizz.getSaturationModifier(), 0.6D)
						&& fizz.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== MobEffects
														.DOLPHINS_GRACE
										&& entry.getFirst()
												.getDuration() == 240)
						&& fizz.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== CakeWorldEffects
														.SUGAR_RUSH.get()
										&& entry.getFirst()
												.getDuration() == 160)
						&& recipe != null
						&& recipe.getIngredients().size() == 5
						&& recipe.getResultItem().is(
								CakeWorldItems.ISLAND_HOP_FIZZ.get())
						&& recipe.getResultItem().getCount() == 2
						&& CakeWorldItems.ISLAND_HOP_FIZZ.get()
								.hasCraftingRemainingItem()
						&& CakeWorldItems.ISLAND_HOP_FIZZ.get()
								.getCraftingRemainingItem()
								== Items.GLASS_BOTTLE,
				"Island-Hop Fizz lost its two-serving island travel recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 28,
				"Jellybean Archipelago requires provider revision 28");
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
									.getAsDouble(), 10.0D)
							&& close(geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsDouble(), 8.0D)
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.0D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:mushroom_fields"))
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
									.getAsDouble(), 0.8D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 1.0D)
							&& "cakeworld:raspberry_gummy_block"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:blueberry_gummy_block"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 4,
					template
							+ " lost the Jellybean Archipelago profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals archipelago profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow018",
			timeoutTicks = 800)
	public static void compassPicnicIsBoundedExactAndNonDestructive(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				JellybeanCompassPicnicFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== JellybeanCompassPicnicFeature.FEATURE
						&& JellybeanCompassPicnicFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& JellybeanCompassPicnicFeature
								.MAX_TERRAIN_RELIEF == 3
						&& JellybeanCompassPicnicFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Compass Picnic registration or bounded constants changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0) instanceof RarityFilter
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3) instanceof BiomeFilter,
				"Compass Picnic lost its bounded surface chain");

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
					JellybeanCompassPicnicFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83, 64,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Compass Picnic did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					JellybeanCompassPicnicFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Compass Picnic crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					JellybeanCompassPicnicFeature.buildAt(
							level, fixture, rotation),
					"Compass Picnic refused a safe fixture for "
							+ rotation + ": "
							+ JellybeanCompassPicnicFeature
									.footprintProblem(
											level, fixture,
											rotation));
			Map<Block, Long> palette = scanPalette(level, fixture);
			require(helper,
					count(palette,
							CakeWorldBlocks.GUMMY_BLOCK.get()) == 17
							&& count(palette,
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get()) == 17
							&& count(palette,
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get()) == 17
							&& count(palette,
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get()) == 17
							&& count(palette,
									CakeWorldBlocks.WAFER_BLOCK
											.get()) == 21
							&& count(palette,
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get()) == 8
							&& count(palette,
									CakeWorldBlocks.CANDY_SPROUT
											.get()) == 16
							&& count(palette,
									CakeWorldBlocks.CANDY_GLASS
											.get()) == 4
							&& count(palette,
									CakeWorldBlocks.MIXING_BOWL
											.get()) == 1,
					"Compass Picnic lost its exact four-flavour palette: "
							+ palette);
			require(helper,
					countMatureSprouts(level, fixture) == 16
							&& level.getEntities((Entity) null,
									new AABB(fixture).inflate(8.0D))
									.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Compass Picnic created immature crops, entities or block entities");
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(5, 0, 5),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!JellybeanCompassPicnicFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Compass Picnic accepted fluid ground");
		prepare(level, fixture);
		level.setBlock(fixture.offset(5, 1, 5),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper,
				!JellybeanCompassPicnicFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Compass Picnic accepted a block entity");
		prepare(level, fixture);
		for (int y = 1; y <= 4; y++) {
			level.setBlock(fixture.offset(5, y, 5),
					CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get()
							.defaultBlockState(),
					2);
		}
		require(helper,
				!JellybeanCompassPicnicFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Compass Picnic accepted more than three blocks of relief");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow018world",
			timeoutTicks = 24000)
	public static void focusedNaturalJellybeanArchipelagoAudit(
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
		BlockPos archipelago =
				locateBiome(helper, level, BIOME_ID);
		LocatedPicnic picnic = locateNaturalPicnic(
				helper, level, archipelago, 16);
		ChunkPos chunk = new ChunkPos(picnic.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			BlockPos sentinel =
					JellybeanCompassPicnicFeature.local(
							picnic.centre(),
							picnic.rotation(),
							-2, 2, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			require(helper,
					matchesPicnic(level, picnic.centre(),
							picnic.rotation(),
							brickSentinel),
					"Natural Compass Picnic lost its complete four-flavour layout");
			Map<Block, Long> palette =
					scanPalette(level, picnic.centre());
			SurfaceAudit surface = auditSurface(
					level, picnic.centre(), 32);
			ResourceLocation biome = level.getBiome(
					picnic.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, picnic.centre());
			LOGGER.info("Jellybean Archipelago audit: centre={}, anchor={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, archipelagoColumns={}, surface={}",
					picnic.centre(), archipelago, biome,
					picnic.rotation(), palette,
					blockEntities, brickSentinel,
					picnic.scannedChunks(),
					picnic.markerCandidates(),
					picnic.archipelagoColumns(),
					surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& count(palette,
									CakeWorldBlocks
											.GUMMY_BLOCK.get()) == 17
							&& count(palette,
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get()) == 17
							&& count(palette,
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get()) == 17
							&& count(palette,
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get()) == 17
							&& count(palette,
									CakeWorldBlocks.WAFER_BLOCK
											.get()) == 21
							&& count(palette,
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get()) == 8
							&& count(palette,
									CakeWorldBlocks.CANDY_SPROUT
											.get()) == 16
							&& count(palette,
									CakeWorldBlocks.CANDY_GLASS
											.get())
									== (brickSentinel ? 3 : 4)
							&& count(palette,
									CakeWorldBlocks.MIXING_BOWL
											.get()) == 1
							&& surface.archipelagoColumns() >= 64
							&& surface.raspberryGummy() >= 64
							&& surface.chocolateSponge() >= 64,
					"Natural Jellybean Archipelago lost its biome, edible island surface or complete Compass Picnic: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Compass Picnic");
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

	private static LocatedPicnic locateNaturalPicnic(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int archipelagoColumns = 0;
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
								archipelagoColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									topY - 12);
									y <= topY; y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								if (!level.getBlockState(marker)
										.is(CakeWorldBlocks
												.MIXING_BOWL.get())) {
									continue;
								}
								markerCandidates++;
								BlockPos centre = marker.below(2);
								Rotation rotation =
										JellybeanCompassPicnicFeature
												.orientation(
														level.getSeed(),
														centre);
								if (matchesPicnic(level,
										centre, rotation, true)) {
									return new LocatedPicnic(
											centre, rotation,
											scannedChunks,
											markerCandidates,
											archipelagoColumns);
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Jellybean Archipelago survey found no natural Compass Picnic after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " mixing-bowl candidates near "
						+ anchor + "; archipelagoColumns="
						+ archipelagoColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int archipelagoColumns = 0;
		int raspberryGummy = 0;
		int chocolateSponge = 0;
		int blueberryGummy = 0;
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
				archipelagoColumns++;
				for (int y = Math.max(
						level.getMinBuildHeight(),
						topY - 8); y <= topY; y++) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (block == CakeWorldBlocks
							.RASPBERRY_GUMMY_BLOCK.get()) {
						raspberryGummy++;
					} else if (block == CakeWorldBlocks
							.CHOCOLATE_SPONGE.get()) {
						chocolateSponge++;
					} else if (block == CakeWorldBlocks
							.BLUEBERRY_GUMMY_BLOCK.get()) {
						blueberryGummy++;
					}
				}
			}
		}
		return new SurfaceAudit(
				archipelagoColumns, raspberryGummy,
				chocolateSponge, blueberryGummy);
	}

	private static boolean matchesPicnic(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected;
				if (x == 0 || z == 0) {
					expected = CakeWorldBlocks.WAFER_BLOCK.get();
				} else if (x < 0 && z < 0) {
					expected = CakeWorldBlocks.GUMMY_BLOCK.get();
				} else if (x > 0 && z < 0) {
					expected = CakeWorldBlocks
							.RASPBERRY_GUMMY_BLOCK.get();
				} else if (x < 0) {
					expected = CakeWorldBlocks
							.BLUEBERRY_GUMMY_BLOCK.get();
				} else {
					expected = CakeWorldBlocks
							.GRAPE_GUMMY_BLOCK.get();
				}
				if (!state(level, centre, rotation, x, 1, z)
						.is(expected)) {
					return false;
				}
			}
		}
		for (int[] sprout :
				JellybeanCompassPicnicFeature.sprouts()) {
			if (!state(level, centre, rotation,
					sprout[0], 2, sprout[1])
					.is(CakeWorldBlocks.CANDY_SPROUT.get())) {
				return false;
			}
		}
		int[][] lanterns = {
			{-3, -3}, {3, -3}, {-3, 3}, {3, 3}
		};
		Block[] caps = {
			CakeWorldBlocks.GUMMY_BLOCK.get(),
			CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get(),
			CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get(),
			CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
		};
		for (int index = 0; index < lanterns.length; index++) {
			int[] lantern = lanterns[index];
			if (!state(level, centre, rotation,
					lantern[0], 2, lantern[1])
					.is(CakeWorldBlocks.CANDY_CANE_PILLAR.get())
					|| !state(level, centre, rotation,
							lantern[0], 3, lantern[1])
							.is(CakeWorldBlocks
									.CANDY_CANE_PILLAR.get())
					|| !state(level, centre, rotation,
							lantern[0], 4, lantern[1])
							.is(caps[index])) {
				return false;
			}
		}
		for (int[] marker : new int[][] {
			{-2, 0}, {2, 0}, {0, -2}, {0, 2}
		}) {
			Block block = state(level, centre, rotation,
					marker[0], 2, marker[1]).getBlock();
			if (block != CakeWorldBlocks.CANDY_GLASS.get()
					&& !(allowBrickSentinel
							&& marker[0] == -2
							&& marker[1] == 0
							&& block == Blocks.BRICKS)) {
				return false;
			}
		}
		for (int[] seat : new int[][] {
			{-1, -1}, {1, -1}, {-1, 1}, {1, 1}
		}) {
			if (!state(level, centre, rotation,
					seat[0], 2, seat[1])
					.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
				return false;
			}
		}
		return state(level, centre, rotation, 0, 2, 0)
				.is(CakeWorldBlocks.MIXING_BOWL.get());
	}

	private static net.minecraft.world.level.block.state.BlockState state(
			ServerLevel level, BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return level.getBlockState(
				JellybeanCompassPicnicFeature.local(
						centre, rotation, x, y, z));
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
						CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK
								.get().defaultBlockState(),
						2);
				for (int y = 1; y <= 6; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
	}

	private static Map<Block, Long> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Long> palette = new LinkedHashMap<>();
		for (BlockPos position : BlockPos.betweenClosed(
				centre.offset(-5, 1, -5),
				centre.offset(5, 4, 5))) {
			Block block = level.getBlockState(position).getBlock();
			if (block != Blocks.AIR) {
				palette.merge(block, 1L, Long::sum);
			}
		}
		return palette;
	}

	private static long count(Map<Block, Long> palette,
			Block block) {
		return palette.getOrDefault(block, 0L);
	}

	private static int countMatureSprouts(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (BlockPos position : BlockPos.betweenClosed(
				centre.offset(-5, 1, -5),
				centre.offset(5, 4, 5))) {
			if (level.getBlockState(position)
					.is(CakeWorldBlocks.CANDY_SPROUT.get())
					&& level.getBlockState(position)
							.getValue(CropBlock.AGE)
							== CropBlock.MAX_AGE) {
				count++;
			}
		}
		return count;
	}

	private static int countBlockEntities(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (BlockPos position : BlockPos.betweenClosed(
				centre.offset(-5, 0, -5),
				centre.offset(5, 4, 5))) {
			if (level.getBlockEntity(position) != null) {
				count++;
			}
		}
		return count;
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
				"Jellybean Archipelago lost its "
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
				JellybeanArchipelagoGameTests.class
						.getResourceAsStream(
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

	private record LocatedPicnic(
			BlockPos centre,
			Rotation rotation,
			int scannedChunks,
			int markerCandidates,
			int archipelagoColumns) {
	}

	private record SurfaceAudit(
			int archipelagoColumns,
			int raspberryGummy,
			int chocolateSponge,
			int blueberryGummy) {
	}
}
