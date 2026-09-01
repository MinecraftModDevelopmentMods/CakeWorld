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
import com.mcmoddev.cakeworld.block.WaferWindmillBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.WaferWindmillFeature;
import com.mcmoddev.cakeworld.world.WaferWindmillRepairFeature;
import com.mcmoddev.cakeworld.world.WaffleSyrupSkywheelFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
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
 * Contract proof for the first functional Waffle Plateaus ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class WafflePlateausGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("waffle_plateaus");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private WafflePlateausGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow011")
	public static void plateausHaveGriddedGeologyHerdsAndWind(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome plateaus = registry.get(BIOME_ID);
		Biome source = registry.get(new ResourceLocation(
				"minecraft", "savanna_plateau"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, plateaus != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.SAVANNA
						&& close(plateaus.getBaseTemperature(), 0.9D)
						&& close(plateaus.getDownfall(), 0.1D),
				"Waffle Plateaus is not a warm dry Savanna-Plateau-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.MOUNTAIN)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.HOT)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.DRY)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SAVANNA),
				"Waffle Plateaus dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				plateaus.getAmbientAdditions().orElse(null);
		AmbientParticleSettings syrupMist =
				plateaus.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.WAFFLE_PLATEAUS_BREEZE
										.getId())
						&& close(ambience.getTickChance(), 0.001D)
						&& syrupMist != null
						&& syrupMist.getOptions().getType()
								== ParticleTypes.FALLING_HONEY,
				"Waffle Plateaus lost its subtitled wooden breeze and syrup-mist ambience");

		assertExactReplacement(helper, source, plateaus,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		assertExactReplacement(helper, source, plateaus,
				EntityType.COW,
				CakeWorldEntities.COCOA_COW.get());
		assertExactReplacement(helper, source, plateaus,
				EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get());
		assertExactReplacement(helper, source, plateaus,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get());
		assertExactReplacement(helper, source, plateaus,
				EntityType.HORSE,
				CakeWorldEntities.GINGERBREAD_PONY.get());
		assertExactReplacement(helper, source, plateaus,
				EntityType.DONKEY,
				CakeWorldEntities.DOUGH_DONKEY.get());
		assertExactReplacement(helper, source, plateaus,
				EntityType.LLAMA,
				CakeWorldEntities.MERINGUE_LLAMA.get());
		require(helper, plateaus.getMobSettings()
						.getMobs(MobCategory.CREATURE)
						.unwrap().stream().noneMatch(spawn ->
								Set.of(EntityType.SHEEP,
										EntityType.COW,
										EntityType.PIG,
										EntityType.CHICKEN,
										EntityType.HORSE,
										EntityType.DONKEY,
										EntityType.LLAMA)
										.contains(spawn.type)),
				"Waffle Plateaus leaked a literal vanilla farm, mount or Llama role");

		require(helper,
				hasPlacedFeature(plateaus,
						WaffleSyrupSkywheelFeature.ID)
						&& hasPlacedFeature(plateaus,
								WaferWindmillRepairFeature.ID)
						&& holder.is(
								WaferWindmillFeature.GENERATES_IN),
				"Waffle Plateaus lost its Skywheel or Wafer-Windmill integration");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.GINGERBREAD_HEARTHLANDS.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.ICE_CREAM_TUNDRA.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							WaffleSyrupSkywheelFeature.ID),
					"Waffle Skywheel leaked into " + other);
		}

		FoodProperties waffle =
				CakeWorldItems.SYRUP_WAFFLE.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("syrup_waffle"))
						.orElse(null);
		require(helper, waffle != null
						&& waffle.getNutrition() == 6
						&& close(waffle
								.getSaturationModifier(), 0.7D)
						&& waffle.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst()
												.getEffect()
												== CakeWorldEffects
														.SUGAR_RUSH
														.get()
										&& entry.getFirst()
												.getDuration() == 160
										&& close(entry.getSecond(),
												1.0D))
						&& recipe != null
						&& recipe.getResultItem().is(
								CakeWorldItems.SYRUP_WAFFLE
										.get())
						&& recipe.getResultItem().getCount() == 2,
				"Waffle Plateaus lost its worthwhile two-serving Syrup-Waffle recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 21,
				"Waffle Plateaus requires provider revision 21");
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
									.getAsInt() == 4
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 16
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 0.75D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:savanna_plateau",
											"minecraft:windswept_savanna",
											"minecraft:savanna"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), 0.5D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 2.0D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.0D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 0.35D)
							&& "cakeworld:wafer_block"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:wafer_rock"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 6,
					template
							+ " lost the Waffle Plateaus profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Waffle profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow011",
			timeoutTicks = 800)
	public static void skywheelIsBoundedPoweredAndSealed(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				WaffleSyrupSkywheelFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== WaffleSyrupSkywheelFeature.FEATURE
						&& WaffleSyrupSkywheelFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 14
						&& WaffleSyrupSkywheelFeature
								.MAX_TERRAIN_RELIEF == 5
						&& WaffleSyrupSkywheelFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Waffle Syrup Skywheel registration or bounded placement constants changed");
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
				"Waffle Syrup Skywheel lost its rare surface-biome chain");

		BlockPos helperPos =
				helper.absolutePos(new BlockPos(4, 4, 4));
		ChunkPos helperChunk = new ChunkPos(helperPos);
		ChunkPos chunk = new ChunkPos(helperChunk.x - 64,
				helperChunk.z + 64);
		level.getChunk(chunk.x, chunk.z);
		BlockPos fixture = new BlockPos(
				chunk.getMinBlockX() + 7,
				level.getMaxBuildHeight() - 24,
				chunk.getMinBlockZ() + 7);
		Set<Rotation> orientations = new HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					WaffleSyrupSkywheelFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83, 112,
									index * -97)));
		}
		require(helper, orientations.size() == 4,
				"Waffle Syrup Skywheel did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					WaffleSyrupSkywheelFeature
							.fitsWithinChunk(fixture,
									rotation, chunk),
					"Waffle Syrup Skywheel crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(9.0D)).size();
			require(helper,
					WaffleSyrupSkywheelFeature.buildAt(
							level, fixture, rotation),
					"Waffle Syrup Skywheel refused a safe fixture for "
							+ rotation + ": "
							+ WaffleSyrupSkywheelFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertSkywheel(helper, level, fixture,
					rotation, false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.WAFER_BLOCK.get(),
							0) == 81
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW.get(),
									0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) == 16
							&& palette.getOrDefault(
									CakeWorldBlocks.CANDY_GLASS
											.get(), 0) == 28
							&& palette.getOrDefault(
									CakeWorldBlocks.SYRUP_PIPE
											.get(), 0) == 12
							&& palette.getOrDefault(
									CakeWorldFluids.SYRUP_BLOCK
											.get(), 0) == 6
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_WINDMILL
											.get(), 0) == 1
							&& palette.getOrDefault(
									Blocks.REDSTONE_BLOCK,
									0) == 1,
					"Waffle Syrup Skywheel lost its exact terrace, wheel, pipes, sealed fall or markers: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(9.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Waffle Syrup Skywheel created an entity or block entity");
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 3, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!WaffleSyrupSkywheelFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Waffle Syrup Skywheel accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!WaffleSyrupSkywheelFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Waffle Syrup Skywheel accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow011world",
			timeoutTicks = 24000)
	public static void focusedNaturalWaffleSkywheelAudit(
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
		BlockPos plateaus = locateBiome(helper, level, BIOME_ID);
		LocatedSkywheel skywheel = locateNaturalSkywheel(
				helper, level, plateaus, 32);
		ChunkPos chunk = new ChunkPos(skywheel.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					WaffleSyrupSkywheelFeature.orientation(
							level.getSeed(),
							skywheel.centre());
			BlockPos sentinel =
					WaffleSyrupSkywheelFeature.local(
							skywheel.centre(), rotation,
							0, 1, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertSkywheel(helper, level,
					skywheel.centre(), rotation,
					brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level,
							skywheel.centre());
			SurfaceAudit surface = auditSurface(
					level, skywheel.centre(), 24);
			ResourceLocation biome = level.getBiome(
					skywheel.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, skywheel.centre());
			LOGGER.info("Waffle Syrup Skywheel audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, waffleColumns={}, surface={}",
					skywheel.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					skywheel.scannedChunks(),
					skywheel.markerCandidates(),
					skywheel.waffleColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK.get(),
									0)
									== (brickSentinel
											? 80 : 81)
							&& palette.getOrDefault(
									CakeWorldFluids
											.SYRUP_BLOCK.get(),
									0) == 6
							&& surface.waffleColumns() >= 64
							&& surface.waferTops() >= 64
							&& surface.waferRock() >= 128,
					"Natural Waffle Plateaus lost its biome, gridded Wafer surface, deep Wafer body or complete Skywheel: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel on the Waffle Skywheel");
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

	private static LocatedSkywheel locateNaturalSkywheel(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int waffleColumns = 0;
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
								waffleColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 16);
									y <= Math.min(
											level.getMaxBuildHeight() - 1,
											surfaceY + 2);
									y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								if (!level.getBlockState(marker)
										.is(CakeWorldFluids
												.SYRUP_BLOCK.get())) {
									continue;
								}
								markerCandidates++;
								for (int localY = 2;
										localY <= 7; localY++) {
									for (Rotation rotation
											: Rotation.values()) {
										BlockPos offset =
												new BlockPos(
														3, localY, 2)
														.rotate(rotation);
										BlockPos centre =
												marker.subtract(offset);
										if (WaffleSyrupSkywheelFeature
												.orientation(
														level.getSeed(),
														centre)
												== rotation
												&& matchesSkywheel(
														level,
														centre,
														rotation,
														true)) {
											return new LocatedSkywheel(
													centre,
													scannedChunks,
													markerCandidates,
													waffleColumns);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Waffle Plateaus survey found no natural Syrup Skywheel after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " Syrup marker candidates near "
						+ anchor + "; waffleColumns="
						+ waffleColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int waffleColumns = 0;
		int waferTops = 0;
		int waferRock = 0;
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
				waffleColumns++;
				boolean foundTop = false;
				for (int y = topY; y >= topY - 14; y--) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (!foundTop
							&& block == CakeWorldBlocks
									.WAFER_BLOCK.get()) {
						waferTops++;
						foundTop = true;
					}
					if (block == CakeWorldBlocks
							.WAFER_ROCK.get()) {
						waferRock++;
					}
				}
			}
		}
		return new SurfaceAudit(waffleColumns,
				waferTops, waferRock);
	}

	private static boolean matchesSkywheel(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected = (x == 0 && Math.abs(z) == 4)
						|| (z == 0 && Math.abs(x) == 4)
								? CakeWorldBlocks.MARSHMALLOW.get()
								: CakeWorldBlocks.WAFER_BLOCK.get();
				BlockPos deck =
						WaffleSyrupSkywheelFeature.local(
								centre, rotation, x, 1, z);
				if (!level.getBlockState(deck).is(expected)
						&& !(allowBrickSentinel
								&& x == 0 && z == 0
								&& level.getBlockState(deck)
										.is(Blocks.BRICKS))) {
					return false;
				}
			}
		}
		BlockPos hub = WaffleSyrupSkywheelFeature.local(
				centre, rotation, -2, 5, -1);
		BlockState hubState = level.getBlockState(hub);
		WaferWindmillBlock windmill =
				(WaferWindmillBlock)
						CakeWorldBlocks.WAFER_WINDMILL.get();
		if (!hubState.is(CakeWorldBlocks.WAFER_WINDMILL.get())
				|| !hubState.getValue(
						WaferWindmillBlock.POWERED)
				|| hubState.getValue(
						WaferWindmillBlock.FACING)
						!= rotation.rotate(Direction.SOUTH)
				|| !level.hasNeighborSignal(hub)
				|| windmill.isSignalSource(hubState)
				|| windmill.getSignal(hubState, level,
						hub, Direction.UP) != 0) {
			return false;
		}
		BlockState horizontal =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(RotatedPillarBlock.AXIS,
								Direction.Axis.X)
						.rotate(rotation);
		BlockState vertical =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(RotatedPillarBlock.AXIS,
								Direction.Axis.Y)
						.rotate(rotation);
		if (!level.getBlockState(
				WaffleSyrupSkywheelFeature.local(
						centre, rotation, -4, 5, -1))
				.equals(horizontal)
				|| !level.getBlockState(
						WaffleSyrupSkywheelFeature.local(
								centre, rotation,
								-2, 7, -1))
						.equals(vertical)) {
			return false;
		}
		for (int y = 2; y <= 7; y++) {
			BlockPos syrup =
					WaffleSyrupSkywheelFeature.local(
							centre, rotation, 3, y, 2);
			if (!level.getBlockState(syrup)
					.is(CakeWorldFluids.SYRUP_BLOCK.get())
					|| level.getFluidState(syrup)
							.getType()
							!= CakeWorldFluids.SYRUP.get()) {
				return false;
			}
			for (BlockPos side : List.of(
					new BlockPos(2, y, 2),
					new BlockPos(4, y, 2),
					new BlockPos(3, y, 1),
					new BlockPos(3, y, 3))) {
				if (!level.getBlockState(
						WaffleSyrupSkywheelFeature.local(
								centre, rotation,
								side.getX(), side.getY(),
								side.getZ()))
						.is(CakeWorldBlocks.CANDY_GLASS.get())) {
					return false;
				}
			}
		}
		if (!level.getBlockState(
				WaffleSyrupSkywheelFeature.local(
						centre, rotation, 3, 8, 2))
				.is(CakeWorldBlocks.SYRUP_PIPE.get())) {
			return false;
		}
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-4, 4}) {
				for (int y = 2; y <= 3; y++) {
					if (!level.getBlockState(
							WaffleSyrupSkywheelFeature.local(
									centre, rotation,
									x, y, z))
							.is(CakeWorldBlocks
									.CANDY_CANE_PILLAR.get())) {
						return false;
					}
				}
				if (!level.getBlockState(
						WaffleSyrupSkywheelFeature.local(
								centre, rotation,
								x, 4, z))
						.is(CakeWorldBlocks.CANDY_GLASS.get())) {
					return false;
				}
			}
		}
		return true;
	}

	private static void assertSkywheel(
			GameTestHelper helper, ServerLevel level,
			BlockPos centre, Rotation rotation,
			boolean allowBrickSentinel) {
		require(helper,
				matchesSkywheel(level, centre, rotation,
						allowBrickSentinel),
				"Waffle Syrup Skywheel lost its gridded terrace, powered wheel, sealed syrup fall, state rotation or readable markers");
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 8; y++) {
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
			for (int y = 1; y <= 8; y++) {
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
				for (int y = -8;
						y < level.getMaxBuildHeight()
								- centre.getY(); y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.WAFER_ROCK.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void assertExactReplacement(
			GameTestHelper helper, Biome source,
			Biome plateaus, EntityType<?> vanilla,
			EntityType<?> replacement) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(plateaus, replacement);
		require(helper, expected != null
						&& actual != null
						&& actual.getWeight().asInt()
								== expected.getWeight().asInt()
						&& actual.minCount == expected.minCount
						&& actual.maxCount == expected.maxCount
						&& findSpawn(plateaus, vanilla) == null,
				"Waffle Plateaus lost the exact "
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
				WafflePlateausGameTests.class
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

	private record LocatedSkywheel(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int waffleColumns) {
	}

	private record SurfaceAudit(
			int waffleColumns,
			int waferTops,
			int waferRock) {
	}
}
