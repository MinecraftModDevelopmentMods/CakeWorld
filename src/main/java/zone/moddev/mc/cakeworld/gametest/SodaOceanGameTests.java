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
import zone.moddev.mc.cakeworld.block.FizzyKelpBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEffects;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.WaferReefNurseryFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Contract proof for the first functional Soda Ocean ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class SodaOceanGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("soda_ocean");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private SodaOceanGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow016")
	public static void oceanHasFizzyEcologyAndPublishedProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome ocean = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, ocean != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.OCEAN
						&& close(ocean.getBaseTemperature(), 0.5D)
						&& close(ocean.getDownfall(), 0.5D),
				"Soda Ocean is not a mild Ocean-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.OCEAN)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.WATER),
				"Soda Ocean dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				ocean.getAmbientAdditions().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.SODA_OCEAN_FIZZ.getId())
						&& close(ambience.getTickChance(), 0.0015D),
				"Soda Ocean lost its subtitled Lemonade fizz");

		assertSpawn(helper, ocean, MobCategory.MONSTER,
				CakeWorldEntities.SOGGY_BISCUIT.get(),
				5, 1, 1);
		assertSpawn(helper, ocean, MobCategory.AXOLOTLS,
				CakeWorldEntities.JELLYLOTL.get(),
				6, 1, 3);
		assertSpawn(helper, ocean, MobCategory.WATER_AMBIENT,
				CakeWorldEntities.SODA_COD.get(),
				10, 3, 6);
		assertSpawn(helper, ocean, MobCategory.WATER_AMBIENT,
				CakeWorldEntities.SHERBET_SALMON.get(),
				15, 1, 5);
		assertSpawn(helper, ocean, MobCategory.WATER_AMBIENT,
				CakeWorldEntities.FIZZBALL_FISH.get(),
				5, 1, 3);
		assertSpawn(helper, ocean, MobCategory.WATER_AMBIENT,
				CakeWorldEntities.JELLYBEAN_FISH.get(),
				25, 8, 8);
		assertSpawn(helper, ocean, MobCategory.WATER_CREATURE,
				CakeWorldEntities.SODA_DOLPHIN.get(),
				1, 1, 2);
		assertSpawn(helper, ocean,
				MobCategory.UNDERGROUND_WATER_CREATURE,
				CakeWorldEntities.GLOW_JELLY.get(),
				10, 4, 6);
		require(helper, List.of(
				EntityType.DROWNED, EntityType.AXOLOTL,
				EntityType.COD, EntityType.SALMON,
				EntityType.PUFFERFISH,
				EntityType.TROPICAL_FISH,
				EntityType.DOLPHIN,
				EntityType.GLOW_SQUID).stream()
				.noneMatch(type -> findSpawn(ocean, type) != null),
				"Soda Ocean leaked a literal vanilla aquatic role");

		require(helper,
				FizzyKelpBlock.supportsFizzyKelp(
						CakeWorldBlocks.BISCUIT_CRUMBS.get()
								.defaultBlockState())
						&& FizzyKelpBlock.supportsFizzyKelp(
								CakeWorldBlocks.WAFER_BLOCK.get()
										.defaultBlockState())
						&& CakeWorldBlocks.FIZZY_KELP.get()
								.defaultBlockState()
								.getFluidState()
								.is(CakeWorldFluids
										.LEMONADE.get()),
				"Fizzy Kelp lost its reef floor or Lemonade-preservation contract");
		require(helper,
				hasPlacedFeature(ocean,
						WaferReefNurseryFeature.ID),
				"Soda Ocean lost its Wafer Reef Nursery");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.GUMMY_JUNGLE.getId(),
				CakeWorldBiomes.CARAMEL_BOGS.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							WaferReefNurseryFeature.ID),
					"Wafer Reef Nursery leaked into " + other);
		}

		FoodProperties raw =
				CakeWorldItems.FIZZY_KELP_FROND.get()
						.getFoodProperties();
		FoodProperties fizz =
				CakeWorldItems.BUBBLE_REEF_FIZZ.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("bubble_reef_fizz"))
						.orElse(null);
		require(helper, raw != null
						&& raw.getNutrition() == 1
						&& close(raw.getSaturationModifier(), 0.1D)
						&& fizz != null
						&& fizz.getNutrition() == 4
						&& close(fizz.getSaturationModifier(), 0.4D)
						&& fizz.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== net.minecraft.world.effect
														.MobEffects
														.WATER_BREATHING
										&& entry.getFirst()
												.getDuration() == 300)
						&& fizz.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== CakeWorldEffects
														.FIZZY_FEET
														.get()
										&& entry.getFirst()
												.getDuration() == 160)
						&& recipe != null
						&& recipe.getResultItem().is(
								CakeWorldItems
										.BUBBLE_REEF_FIZZ.get())
						&& recipe.getResultItem().getCount() == 2,
				"Soda Ocean lost its emergency frond and two-serving exploration drink");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 26,
				"Soda Ocean ecosystem requires provider revision 26");
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
							"cakeworld:overworld_oceans")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					geomes.size() == 2
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 12
							&& geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsInt() == 6
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.0D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:ocean",
											"minecraft:deep_ocean",
											"minecraft:cold_ocean",
											"minecraft:deep_cold_ocean",
											"minecraft:frozen_ocean",
											"minecraft:deep_frozen_ocean",
											"minecraft:lukewarm_ocean",
											"minecraft:deep_lukewarm_ocean",
											"minecraft:warm_ocean"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), -2.0D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 2.0D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.0D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 1.0D)
							&& "cakeworld:biscuit_crumbs"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 4,
					template + " lost the Soda Ocean profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Soda Ocean profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow016",
			timeoutTicks = 800)
	public static void reefIsBoundedRenewableAndPersistent(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				WaferReefNurseryFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== WaferReefNurseryFeature.FEATURE
						&& WaferReefNurseryFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& WaferReefNurseryFeature
								.MAX_FLOOR_RELIEF == 3
						&& WaferReefNurseryFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Wafer Reef Nursery registration or bounded constants changed");
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
				"Wafer Reef Nursery lost its bounded ocean-floor chain");

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
					WaferReefNurseryFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83, 48,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Wafer Reef Nursery did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					WaferReefNurseryFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Wafer Reef Nursery crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					WaferReefNurseryFeature.buildAt(
							level, fixture, rotation),
					"Wafer Reef Nursery refused a safe fixture for "
							+ rotation + ": "
							+ WaferReefNurseryFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertReef(helper, level, fixture,
					rotation, false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.BISCUIT_CRUMBS.get(),
							0) == 64
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK.get(),
									0) == 17
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_ROCK.get(),
									0) == 8
							&& palette.getOrDefault(
									CakeWorldBlocks
											.FIZZY_KELP.get(),
									0) == 16
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.FIZZY_PEARL.get(),
									0) == 1,
					"Wafer Reef Nursery lost its exact floor, reef, kelp or treasure palette: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Wafer Reef Nursery created an entity or block entity");
		}

		prepare(level, fixture);
		BlockPos kelpPos = fixture.offset(2, 2, 2);
		FizzyKelpBlock kelp =
				(FizzyKelpBlock) CakeWorldBlocks
						.FIZZY_KELP.get();
		BlockState mature = kelp.defaultBlockState()
				.setValue(FizzyKelpBlock.AGE, 3);
		level.setBlock(kelpPos.below(),
				CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState(), 2);
		level.setBlock(kelpPos, mature, 2);
		FakePlayer player = FakePlayerFactory.getMinecraft(level);
		kelp.use(mature, level, kelpPos, player,
				InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(kelpPos),
						Direction.UP, kelpPos, false));
		BlockState picked = level.getBlockState(kelpPos);
		require(helper,
				picked.is(kelp)
						&& picked.getValue(FizzyKelpBlock.AGE)
								== FizzyKelpBlock.PICKED_AGE
						&& picked.getFluidState().is(
								CakeWorldFluids.LEMONADE.get())
						&& level.getEntitiesOfClass(
								ItemEntity.class,
								new AABB(kelpPos).inflate(2.0D))
								.stream().anyMatch(item ->
										item.getItem().is(
												CakeWorldItems
														.FIZZY_KELP_FROND
														.get())
										&& item.getItem()
												.getCount() >= 2),
				"Picking mature Fizzy Kelp did not preserve Lemonade, retain the plant and yield fronds");
		for (int attempt = 0; attempt < 4
				&& level.getBlockState(kelpPos)
						.getValue(FizzyKelpBlock.AGE) < 3;
				attempt++) {
			BlockState growing =
					level.getBlockState(kelpPos);
			kelp.performBonemeal(level, level.random,
					kelpPos, growing);
		}
		require(helper,
				level.getBlockState(kelpPos)
						.getValue(FizzyKelpBlock.AGE) == 3,
				"Bone meal did not renew picked Fizzy Kelp");

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 3, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!WaferReefNurseryFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Wafer Reef Nursery accepted an authored obstacle");
		prepare(level, fixture);
		level.setBlock(fixture.offset(1, 2, 1),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!WaferReefNurseryFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Wafer Reef Nursery accepted a non-Lemonade water cell");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow016world",
			timeoutTicks = 24000)
	public static void focusedNaturalWaferReefAudit(
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
		BlockPos ocean = locateBiome(helper, level, BIOME_ID);
		LocatedReef reef = locateNaturalReef(
				helper, level, ocean, 16);
		ChunkPos chunk = new ChunkPos(reef.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					WaferReefNurseryFeature.orientation(
							level.getSeed(), reef.centre());
			BlockPos sentinel =
					WaferReefNurseryFeature.local(
							reef.centre(), rotation,
							0, 2, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertReef(helper, level,
					reef.centre(), rotation,
					brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, reef.centre());
			SurfaceAudit surface = auditSurface(
					level, reef.centre(), 24);
			ResourceLocation biome = level.getBiome(
					reef.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, reef.centre());
			LOGGER.info("Wafer Reef Nursery audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, sodaColumns={}, surface={}",
					reef.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					reef.scannedChunks(),
					reef.markerCandidates(),
					reef.sodaColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.FIZZY_PEARL.get(),
									0)
									== (brickSentinel
											? 0 : 1)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.FIZZY_KELP.get(),
									0) == 16
							&& surface.sodaColumns() >= 64
							&& surface.lemonade() >= 512
							&& surface.biscuitCrumbs() >= 64,
					"Natural Soda Ocean lost its biome, Lemonade, crumb floor or complete Wafer Reef Nursery: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the reef treasure");
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
				new BlockPos(0, 48, 0), 16384, 8);
		require(helper, match != null,
				"Could not locate " + biomeId
						+ " within 16,384 blocks");
		return match.getFirst();
	}

	private static LocatedReef locateNaturalReef(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int sodaColumns = 0;
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
											x, Math.min(48, topY),
											z))
											.unwrapKey()
											.map(ResourceKey::location)
											.orElse(null);
							if (BIOME_ID.equals(biome)) {
								sodaColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									topY - 48);
									y <= topY; y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								BlockState state =
										level.getBlockState(marker);
								if (!state.is(CakeWorldBlocks
										.FIZZY_PEARL.get())
										&& !state.is(
												Blocks.BRICKS)) {
									continue;
								}
								markerCandidates++;
								BlockPos centre =
										marker.below(2);
								Rotation rotation =
										WaferReefNurseryFeature
												.orientation(
														level.getSeed(),
														centre);
								if (matchesReef(level, centre,
										rotation, true)) {
									return new LocatedReef(
											centre,
											scannedChunks,
											markerCandidates,
											sodaColumns);
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Soda Ocean survey found no natural Wafer Reef Nursery after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " treasure candidates near "
						+ anchor + "; sodaColumns="
						+ sodaColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int sodaColumns = 0;
		int lemonade = 0;
		int biscuitCrumbs = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				if (!level.getBiome(new BlockPos(
						x, 48, z)).is(BIOME_KEY)) {
					continue;
				}
				sodaColumns++;
				for (int y = level.getMinBuildHeight();
						y <= 64; y++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (level.getFluidState(pos)
							.is(CakeWorldFluids.LEMONADE.get())) {
						lemonade++;
					}
					if (level.getBlockState(pos).is(
							CakeWorldBlocks.BISCUIT_CRUMBS
									.get())) {
						biscuitCrumbs++;
					}
				}
			}
		}
		return new SurfaceAudit(sodaColumns,
				lemonade, biscuitCrumbs);
	}

	private static boolean matchesReef(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected = x == 0 || z == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
						: CakeWorldBlocks.BISCUIT_CRUMBS.get();
				if (!level.getBlockState(
						WaferReefNurseryFeature.local(
								centre, rotation, x, 1, z))
						.is(expected)) {
					return false;
				}
			}
		}
		for (int[] kelp :
				WaferReefNurseryFeature.kelp()) {
			BlockState state = level.getBlockState(
					WaferReefNurseryFeature.local(
							centre, rotation,
							kelp[0], 2, kelp[1]));
			if (!state.is(CakeWorldBlocks.FIZZY_KELP.get())
					|| state.getValue(FizzyKelpBlock.AGE) != 3
					|| !state.getFluidState().is(
							CakeWorldFluids.LEMONADE.get())) {
				return false;
			}
		}
		int index = 0;
		Block[] caps = {
			CakeWorldBlocks.GUMMY_BLOCK.get(),
			CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get(),
			CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get(),
			CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get()
		};
		for (int[] tower : new int[][] {
				{-3, -3}, {3, -3}, {-3, 3}, {3, 3}
		}) {
			for (int y = 2; y <= 3; y++) {
				if (!level.getBlockState(
						WaferReefNurseryFeature.local(
								centre, rotation,
								tower[0], y, tower[1]))
						.is(CakeWorldBlocks.WAFER_ROCK.get())) {
					return false;
				}
			}
			if (!level.getBlockState(
					WaferReefNurseryFeature.local(
							centre, rotation,
							tower[0], 4, tower[1]))
					.is(caps[index++])) {
				return false;
			}
		}
		for (int[] marker : new int[][] {
				{-2, 0}, {2, 0}, {0, -2}, {0, 2}
		}) {
			if (!level.getBlockState(
					WaferReefNurseryFeature.local(
							centre, rotation,
							marker[0], 2, marker[1]))
					.is(CakeWorldBlocks.CANDY_GLASS.get())) {
				return false;
			}
		}
		BlockState treasure = level.getBlockState(
				WaferReefNurseryFeature.local(
						centre, rotation, 0, 2, 0));
		return treasure.is(CakeWorldBlocks.FIZZY_PEARL.get())
				|| allowBrickSentinel
						&& treasure.is(Blocks.BRICKS);
	}

	private static void assertReef(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		require(helper,
				matchesReef(level, centre, rotation,
						allowBrickSentinel),
				"Wafer Reef Nursery lost its floor, ripe fronds, four-colour reef or visible treasure");
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 5; y++) {
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
						CakeWorldBlocks.BISCUIT_CRUMBS.get()
								.defaultBlockState(),
						2);
				for (int y = 1; y <= 6; y++) {
					level.setBlock(centre.offset(x, y, z),
							CakeWorldFluids.LEMONADE_BLOCK.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static void assertSpawn(GameTestHelper helper,
			Biome biome, MobCategory category,
			EntityType<?> expected, int weight,
			int minimum, int maximum) {
		MobSpawnSettings.SpawnerData spawn =
				findSpawn(biome, expected);
		require(helper, spawn != null
						&& biome.getMobSettings()
								.getMobs(category)
								.unwrap().contains(spawn)
						&& spawn.getWeight().asInt() == weight
						&& spawn.minCount == minimum
						&& spawn.maxCount == maximum,
				"Missing exact Soda Ocean spawn role for "
						+ expected + ": " + spawn);
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
				SodaOceanGameTests.class.getResourceAsStream(
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

	private record LocatedReef(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int sodaColumns) {
	}

	private record SurfaceAudit(
			int sodaColumns,
			int lemonade,
			int biscuitCrumbs) {
	}
}
