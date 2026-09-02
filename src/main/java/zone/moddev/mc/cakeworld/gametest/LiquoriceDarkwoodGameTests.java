package zone.moddev.mc.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
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
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.GrandGingerbreadManorFeature;
import zone.moddev.mc.cakeworld.world.LiquoriceRootMazeFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
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
 * Contract proof for the first functional Liquorice Darkwood ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class LiquoriceDarkwoodGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("liquorice_darkwood");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private LiquoriceDarkwoodGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow013")
	public static void darkwoodHasSpookyRootsWeaversAndSafeVision(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome darkwood = registry.get(BIOME_ID);
		Biome source = registry.get(new ResourceLocation(
				"minecraft", "dark_forest"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, darkwood != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.FOREST
						&& close(darkwood.getBaseTemperature(), 0.7D)
						&& close(darkwood.getDownfall(), 0.8D),
				"Liquorice Darkwood is not a temperate Dark-Forest-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.FOREST)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.DENSE)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SPOOKY),
				"Liquorice Darkwood dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				darkwood.getAmbientAdditions().orElse(null);
		AmbientParticleSettings motes =
				darkwood.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.LIQUORICE_DARKWOOD_RUSTLE
										.getId())
						&& close(ambience.getTickChance(), 0.001D)
						&& motes != null
						&& motes.getOptions().getType()
								== ParticleTypes.MYCELIUM,
				"Liquorice Darkwood lost its subtitled root rustle or drifting motes");

		assertExactReplacement(helper, source, darkwood,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		assertExactReplacement(helper, source, darkwood,
				EntityType.COW,
				CakeWorldEntities.COCOA_COW.get());
		assertExactReplacement(helper, source, darkwood,
				EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get());
		assertExactReplacement(helper, source, darkwood,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get());
		assertExactReplacement(helper, source, darkwood,
				EntityType.SPIDER,
				CakeWorldEntities.LIQUORICE_WEAVER.get());
		assertExactReplacement(helper, source, darkwood,
				EntityType.BAT,
				CakeWorldEntities.BONBON_BAT.get());
		require(helper, darkwood.getMobSettings()
						.getMobs(MobCategory.CREATURE)
						.unwrap().stream().noneMatch(spawn ->
								Set.of(EntityType.SHEEP,
										EntityType.COW,
										EntityType.PIG,
										EntityType.CHICKEN)
										.contains(spawn.type))
						&& findSpawn(darkwood,
								EntityType.SPIDER) == null
						&& findSpawn(darkwood,
								EntityType.BAT) == null,
				"Liquorice Darkwood leaked literal farm, Spider or Bat roles");

		require(helper,
				CakeWorldBlocks.LIQUORICE_LOAM.get()
						.defaultBlockState()
						.is(BlockTags.ANIMALS_SPAWNABLE_ON)
						&& CakeWorldBlocks.LIQUORICE_ROOT.get()
								.defaultBlockState()
								.is(BlockTags.MINEABLE_WITH_AXE)
						&& hasPlacedFeature(darkwood,
								LiquoriceRootMazeFeature.ID),
				"Darkwood loam, harvestable root or maze ownership is incomplete");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.CUPCAKE_GARDENS.getId(),
				CakeWorldBiomes.WAFFLE_PLATEAUS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							LiquoriceRootMazeFeature.ID),
					"Liquorice Root Maze leaked into " + other);
		}

		Set<ResourceLocation> manorBiomes = registry
				.getTag(GrandGingerbreadManorFeature.GENERATES_IN)
				.map(tag -> tag.stream()
						.map(biome -> biome.unwrapKey()
								.orElseThrow().location())
						.collect(java.util.stream.Collectors.toSet()))
				.orElse(Set.of());
		require(helper,
				manorBiomes.equals(Set.of(
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						BIOME_ID))
						&& !holder.is(
								BiomeTags.HAS_WOODLAND_MANSION),
				"Darkwood did not gain only the custom Gingerbread Manor contract: "
						+ manorBiomes);

		FoodProperties twist =
				CakeWorldItems.LIQUORICE_TWIST.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("liquorice_twist"))
						.orElse(null);
		require(helper, twist != null
						&& twist.getNutrition() == 5
						&& close(twist.getSaturationModifier(), 0.6D)
						&& twist.getEffects().stream()
								.anyMatch(effect ->
										effect.getFirst().getEffect()
												== MobEffects.NIGHT_VISION
										&& effect.getFirst()
												.getDuration() == 240
										&& close(effect.getSecond(),
												1.0D))
						&& recipe != null
						&& recipe.getResultItem().is(
								CakeWorldItems.LIQUORICE_TWIST.get())
						&& recipe.getResultItem().getCount() == 2,
				"Darkwood lost its worthwhile two-serving visibility-aid Liquorice Twist");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 23,
				"Liquorice Darkwood requires provider revision 23");
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
									.getAsInt() == 10
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 4
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 0.9D)
							&& strings(palette.getAsJsonArray(
									"similar_biomes"))
									.equals(Set.of(
											"minecraft:dark_forest"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), 0.4D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 1.0D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.6D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 1.0D)
							&& "cakeworld:liquorice_loam"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:liquorice_root"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5,
					template
							+ " lost the Liquorice Darkwood profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Darkwood profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow013",
			timeoutTicks = 800)
	public static void rootMazeIsBoundedNavigableAndPersistent(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				LiquoriceRootMazeFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== LiquoriceRootMazeFeature.FEATURE
						&& LiquoriceRootMazeFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 12
						&& LiquoriceRootMazeFeature
								.MAX_TERRAIN_RELIEF == 4
						&& LiquoriceRootMazeFeature
								.SAFE_SITE_SEARCH_RADIUS == 8,
				"Liquorice Root Maze registration or bounded placement constants changed");
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
				"Liquorice Root Maze lost its rare surface-biome chain");
		require(helper,
				reachable(0, -4, 0, 0)
						&& reachable(0, 0, 3, 4)
						&& reachable(0, -4, 3, 4),
				"Liquorice Root Maze lost its ordinary entrance-centre-exit route");

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
					LiquoriceRootMazeFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83, 109,
									index * -103)));
		}
		require(helper, orientations.size() == 4,
				"Liquorice Root Maze did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					LiquoriceRootMazeFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Liquorice Root Maze crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					LiquoriceRootMazeFeature.buildAt(
							level, fixture, rotation),
					"Liquorice Root Maze refused a safe fixture for "
							+ rotation + ": "
							+ LiquoriceRootMazeFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertMaze(helper, level, fixture, rotation,
					false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.LIQUORICE_LOAM.get(),
							0) == 81
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIQUORICE_ROOT.get(),
									0) == 92
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get(), 0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get(), 0) == 1,
					"Liquorice Root Maze lost its exact loam, root, window or shortcut palette: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Liquorice Root Maze created an entity or block entity");
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 3, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!LiquoriceRootMazeFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Liquorice Root Maze accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!LiquoriceRootMazeFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Liquorice Root Maze accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow013world",
			timeoutTicks = 24000)
	public static void focusedNaturalLiquoriceRootMazeAudit(
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
		BlockPos darkwood = locateBiome(helper, level, BIOME_ID);
		LocatedMaze maze = locateNaturalMaze(
				helper, level, darkwood, 32);
		ChunkPos chunk = new ChunkPos(maze.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					LiquoriceRootMazeFeature.orientation(
							level.getSeed(),
							maze.centre());
			BlockPos sentinel =
					LiquoriceRootMazeFeature.local(
							maze.centre(), rotation,
							0, 1, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertMaze(helper, level, maze.centre(),
					rotation, brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, maze.centre());
			SurfaceAudit surface = auditSurface(
					level, maze.centre(), 24);
			ResourceLocation biome = level.getBiome(
					maze.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, maze.centre());
			LOGGER.info("Liquorice Root Maze audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, darkwoodColumns={}, surface={}",
					maze.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					maze.scannedChunks(),
					maze.markerCandidates(),
					maze.darkwoodColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIQUORICE_LOAM.get(),
									0)
									== (brickSentinel
											? 80 : 81)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIQUORICE_ROOT.get(),
									0) == 92
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get(), 0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get(), 0) == 1
							&& surface.darkwoodColumns() >= 64
							&& surface.loamTops() >= 64
							&& surface.chocolateSponge() >= 128,
					"Natural Liquorice Darkwood lost its biome, loam surface, sponge body or complete Root Maze: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Darkwood maze");
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

	private static LocatedMaze locateNaturalMaze(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int darkwoodColumns = 0;
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
								darkwoodColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 18);
									y <= Math.min(
											level.getMaxBuildHeight() - 1,
											surfaceY + 5);
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
															0, 2, -2)
															.rotate(
																	rotation));
									if (LiquoriceRootMazeFeature
											.orientation(
													level.getSeed(),
													centre)
											== rotation
											&& matchesMaze(
													level, centre,
													rotation,
													true)) {
										return new LocatedMaze(
												centre,
												scannedChunks,
												markerCandidates,
												darkwoodColumns);
									}
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Liquorice Darkwood survey found no natural Root Maze after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " raspberry-gummy marker candidates near "
						+ anchor + "; darkwoodColumns="
						+ darkwoodColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int darkwoodColumns = 0;
		int loamTops = 0;
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
				darkwoodColumns++;
				boolean foundTop = false;
				for (int y = topY; y >= topY - 16; y--) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (!foundTop
							&& block == CakeWorldBlocks
									.LIQUORICE_LOAM.get()) {
						loamTops++;
						foundTop = true;
					}
					if (block == CakeWorldBlocks
							.CHOCOLATE_SPONGE.get()) {
						chocolateSponge++;
					}
				}
			}
		}
		return new SurfaceAudit(darkwoodColumns,
				loamTops, chocolateSponge);
	}

	private static boolean matchesMaze(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockState floor = level.getBlockState(
						LiquoriceRootMazeFeature.local(
								centre, rotation, x, 1, z));
				if (!floor.is(CakeWorldBlocks
						.LIQUORICE_LOAM.get())
						&& !(allowBrickSentinel
								&& x == 0 && z == 0
								&& floor.is(Blocks.BRICKS))) {
					return false;
				}
				if (!LiquoriceRootMazeFeature.isWall(x, z)) {
					continue;
				}
				BlockState lower = level.getBlockState(
						LiquoriceRootMazeFeature.local(
								centre, rotation, x, 2, z));
				if (LiquoriceRootMazeFeature
						.isShortcut(x, z)) {
					Block expected = z < 0
							? CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK.get()
							: CakeWorldBlocks
									.GRAPE_GUMMY_BLOCK.get();
					if (!lower.is(expected)
							|| !level.getBlockState(
									LiquoriceRootMazeFeature.local(
											centre, rotation,
											x, 3, z))
									.isAir()) {
						return false;
					}
					continue;
				}
				BlockState upper = level.getBlockState(
						LiquoriceRootMazeFeature.local(
								centre, rotation, x, 3, z));
				if (!lower.is(CakeWorldBlocks.LIQUORICE_ROOT.get())
						|| lower.getValue(
								RotatedPillarBlock.AXIS)
								!= net.minecraft.core.Direction.Axis.Y
						|| !(Math.abs(x) == 4
								&& Math.abs(z) == 4
										? upper.is(CakeWorldBlocks
												.CANDY_GLASS.get())
										: upper.is(CakeWorldBlocks
												.LIQUORICE_ROOT.get()))) {
					return false;
				}
			}
		}
		return matchesArch(level, centre, rotation, 0, -5)
				&& matchesArch(level, centre, rotation, 3, 5);
	}

	private static boolean matchesArch(ServerLevel level,
			BlockPos centre, Rotation rotation,
			int centreX, int z) {
		for (int side : new int[] {-1, 1}) {
			for (int y = 2; y <= 4; y++) {
				if (!level.getBlockState(
						LiquoriceRootMazeFeature.local(
								centre, rotation,
								centreX + side, y, z))
						.is(CakeWorldBlocks.LIQUORICE_ROOT.get())) {
					return false;
				}
			}
		}
		return level.getBlockState(
				LiquoriceRootMazeFeature.local(
						centre, rotation, centreX, 4, z))
				.is(CakeWorldBlocks.LIQUORICE_ROOT.get());
	}

	private static void assertMaze(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		require(helper,
				matchesMaze(level, centre, rotation,
						allowBrickSentinel),
				"Liquorice Root Maze lost its floor, ordinary route, two soft shortcuts, corner windows or rotated arches");
	}

	private static boolean reachable(int startX, int startZ,
			int targetX, int targetZ) {
		ArrayDeque<BlockPos> open = new ArrayDeque<>();
		Set<BlockPos> seen = new HashSet<>();
		BlockPos start = new BlockPos(startX, 0, startZ);
		open.add(start);
		seen.add(start);
		while (!open.isEmpty()) {
			BlockPos current = open.removeFirst();
			if (current.getX() == targetX
					&& current.getZ() == targetZ) {
				return true;
			}
			for (int[] step : new int[][] {
					{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
				int x = current.getX() + step[0];
				int z = current.getZ() + step[1];
				if (x < -4 || x > 4 || z < -4 || z > 4
						|| LiquoriceRootMazeFeature
								.isWall(x, z)) {
					continue;
				}
				BlockPos next = new BlockPos(x, 0, z);
				if (seen.add(next)) {
					open.add(next);
				}
			}
		}
		return false;
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
				for (int y = -8;
						centre.getY() + y < level.getMaxBuildHeight();
						y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.LIQUORICE_LOAM.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static void assertExactReplacement(
			GameTestHelper helper, Biome source,
			Biome darkwood, EntityType<?> vanilla,
			EntityType<?> replacement) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(darkwood, replacement);
		require(helper, expected != null
						&& actual != null
						&& actual.getWeight().asInt()
								== expected.getWeight().asInt()
						&& actual.minCount == expected.minCount
						&& actual.maxCount == expected.maxCount
						&& findSpawn(darkwood, vanilla) == null,
				"Liquorice Darkwood lost the exact "
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
				LiquoriceDarkwoodGameTests.class
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

	private record LocatedMaze(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int darkwoodColumns) {
	}

	private record SurfaceAudit(
			int darkwoodColumns,
			int loamTops,
			int chocolateSponge) {
	}
}
