package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.GummyBlock;
import com.mcmoddev.cakeworld.entity.GlowJelly;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CosmicJellyNurseryFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
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

/** Contract proof for the complete BIO-END-006 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class CosmicJellyReefsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID = id(
			"cosmic_jelly_reefs");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private CosmicJellyReefsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioend006")
	public static void reefsHaveBioluminescentEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome archipelago = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, archipelago != null
						&& close(archipelago.getBaseTemperature(), 0.5D)
						&& close(archipelago.getDownfall(), 0.0D),
				"Cosmic Jelly Reefs lost its cool, dry End climate");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.VOID,
				BiomeDictionary.Type.MAGICAL,
				BiomeDictionary.Type.RARE)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Cosmic Jelly Reefs lost dictionary type " + type);
		}

		AmbientAdditionsSettings ambience =
				archipelago.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				archipelago.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds.COSMIC_JELLY_REEF_PULSE
										.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.GLOW,
				"Cosmic Jelly Reefs lost its luminous void atmosphere");

		MobSpawnSettings.SpawnerData tallwalker = findSpawn(archipelago,
				CakeWorldEntities.TAFFY_TALLWALKER.get());
		MobSpawnSettings.SpawnerData mite = findSpawn(archipelago,
				CakeWorldEntities.SUGAR_MITE.get());
		MobSpawnSettings.SpawnerData glowJelly = findSpawn(archipelago,
				CakeWorldEntities.GLOW_JELLY.get());
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += archipelago.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, tallwalker != null
						&& tallwalker.getWeight().asInt() == 10
						&& tallwalker.minCount == 4
						&& tallwalker.maxCount == 4
						&& mite != null
						&& mite.getWeight().asInt() == 5
						&& mite.minCount == 1
						&& mite.maxCount == 3
						&& glowJelly != null
						&& glowJelly.getWeight().asInt() == 4
						&& glowJelly.minCount == 2
						&& glowJelly.maxCount == 4
						&& findSpawn(archipelago, EntityType.ENDERMAN) == null
						&& findSpawn(archipelago, EntityType.ENDERMITE) == null
						&& findSpawn(archipelago, EntityType.GLOW_SQUID) == null
						&& GlowJelly.isCosmicReef(holder)
						&& totalSpawns == 3,
				"Cosmic Jelly Reefs lost its exact Tallwalker/Mite/Glow-Jelly ecology: "
						+ totalSpawns);
		assertJellyAndFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend006")
	public static void nurseryIsBoundedSafeAndSealed(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed = CosmicJellyNurseryFeature.placedFeature();
		Biome archipelago = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== CosmicJellyNurseryFeature.FEATURE
						&& placed.value().placement().size() == 4
						&& placed.value().placement().get(0)
								instanceof RarityFilter
						&& placed.value().placement().get(1)
								instanceof InSquarePlacement
						&& placed.value().placement().get(2)
								instanceof HeightmapPlacement
						&& placed.value().placement().get(3)
								instanceof BiomeFilter
						&& CosmicJellyNurseryFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& CosmicJellyNurseryFeature.MAX_TERRAIN_RELIEF == 6
						&& hasPlacedFeature(archipelago, placed),
				"Cosmic Jelly Nursery lost its bounded placement chain");

		ChunkPos helperChunk = new ChunkPos(helper.absolutePos(
				new BlockPos(8, 5, 8)));
		BlockPos centre = new BlockPos(helperChunk.getMinBlockX() + 8,
				63, helperChunk.getMinBlockZ() + 8);
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					CosmicJellyNurseryFeature.hasSafeFootprint(
							level, centre, rotation)
							&& CosmicJellyNurseryFeature.buildAt(
									level, centre, rotation),
					"Cosmic Jelly Nursery rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false),
					"Cosmic Jelly Nursery plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Cosmic Jelly Nursery created an entity or block entity");
		}

		prepareSite(level, centre);
		level.setBlock(centre.offset(3, 1, 3),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !CosmicJellyNurseryFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Cosmic Jelly Nursery replaced an existing fluid");
		prepareSite(level, centre);
		level.setBlock(centre.offset(-3, 1, -3),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !CosmicJellyNurseryFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Cosmic Jelly Nursery replaced a block entity");
		prepareSite(level, centre);
		level.setBlock(centre.offset(1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !CosmicJellyNurseryFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Cosmic Jelly Nursery replaced an authored solid");
		require(helper, !CosmicJellyNurseryFeature.fitsWithinChunk(
				new BlockPos(helperChunk.getMinBlockX(), centre.getY(),
						helperChunk.getMinBlockZ()), Rotation.NONE, helperChunk),
				"Cosmic Jelly Nursery crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend006world",
			timeoutTicks = 24000)
	public static void focusedNaturalCosmicJellyReefsAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel().getServer().getLevel(Level.END);
		require(helper, level != null,
				"The fixed-seed server did not expose the End");
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				biome -> biome.is(BIOME_KEY), new BlockPos(0, 64, 0),
				32768, 8);
		require(helper, match != null,
				"Could not locate Cosmic Jelly Reefs within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundNursery found = findNursery(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Cosmic Jelly Nursery within 1,089 chunks of "
						+ anchor);
		ChunkPos foundChunk = new ChunkPos(found.centre());
		for (int chunkX = foundChunk.x - 4;
				chunkX <= foundChunk.x + 4; chunkX++) {
			for (int chunkZ = foundChunk.z - 4;
					chunkZ <= foundChunk.z + 4; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
		level.setChunkForced(foundChunk.x, foundChunk.z, true);
		helper.runAfterDelay(40, () -> {
			PlanAudit plan = inspectPlan(level, found.centre(), found.rotation());
			BlockPos sentinel = CosmicJellyNurseryFeature.local(
					found.centre(), found.rotation(), -2, 5, -2);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 4, found.centre());
			LOGGER.info("Cosmic Jelly Reefs audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, cosmicJelly={}, surfaceJelly={}, biscuitStone={}, waferRock={}, rockCandy={}, nougatRock={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.cosmicJelly(),
					audit.surfaceJelly(), audit.biscuitStone(),
					audit.waferRock(), audit.rockCandy(),
					audit.nougatRock(), plan,
					brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.cosmicJelly() > 0
							&& audit.surfaceJelly() > 0
							&& audit.biscuitStone() + audit.waferRock()
									+ audit.rockCandy()
									+ audit.nougatRock() > 0
							&& plan.complete(brickSentinel),
					"Natural Cosmic Jelly Reefs lost layered terrain, geology or its complete Nursery: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
						"Could not seed the Nursery reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertJellyAndFood(
			GameTestHelper helper, ServerLevel level) {
		GummyBlock jelly = (GummyBlock)
				CakeWorldBlocks.COSMIC_JELLY.get();
		Pig falling = EntityType.PIG.create(level);
		require(helper, falling != null,
				"Could not create Cosmic Jelly fall fixture");
		falling.setHealth(10.0F);
		falling.setDeltaMovement(0.0D, -1.0D, 0.0D);
		jelly.fallOn(level, jelly.defaultBlockState(), BlockPos.ZERO,
				falling, 11.0F);
		jelly.updateEntityAfterFallOn(level, falling);
		require(helper, close(falling.getHealth(), 10.0D)
						&& close(falling.getDeltaMovement().y, 0.9D)
						&& jelly.defaultBlockState().getLightEmission() == 10,
				"Cosmic Jelly lost its luminous elastic safety contract");

		FoodProperties raw = CakeWorldItems.STAR_JELLY_DROPLET.get()
				.getFoodProperties();
		FoodProperties prepared = CakeWorldItems.NEBULA_JELLY_CUP.get()
				.getFoodProperties();
		Recipe<?> pieces = level.getRecipeManager()
				.byKey(id("star_jelly_droplet")).orElse(null);
		Recipe<?> rainbow = level.getRecipeManager()
				.byKey(id("nebula_jelly_cup")).orElse(null);
		require(helper, raw != null && raw.getNutrition() == 1
						&& close(raw.getSaturationModifier(), 0.1D)
						&& prepared != null && prepared.getNutrition() == 8
						&& close(prepared.getSaturationModifier(), 0.8D)
						&& hasEffect(prepared, MobEffects.NIGHT_VISION, 400)
						&& hasEffect(prepared, MobEffects.SLOW_FALLING, 300)
						&& pieces != null
						&& pieces.getType() == RecipeType.CRAFTING
						&& pieces.getIngredients().size() == 1
						&& ingredient(pieces, new ItemStack(
								CakeWorldBlocks.COSMIC_JELLY.get()))
						&& pieces.getResultItem().is(
								CakeWorldItems.STAR_JELLY_DROPLET.get())
						&& pieces.getResultItem().getCount() == 4
						&& rainbow != null
						&& rainbow.getType() == RecipeType.CRAFTING
						&& rainbow.getIngredients().size() == 4
						&& ingredientCount(rainbow, new ItemStack(
								CakeWorldItems.STAR_JELLY_DROPLET.get())) == 2
						&& ingredient(rainbow, new ItemStack(
								CakeWorldItems.GLOWING_JAM_BERRY.get()))
						&& ingredient(rainbow, new ItemStack(
								CakeWorldItems.STAR_SUGAR_CRYSTALS.get()))
						&& rainbow.getResultItem().is(
								CakeWorldItems.NEBULA_JELLY_CUP.get())
						&& rainbow.getResultItem().getCount() == 2,
				"Cosmic Jelly or Nebula Jelly Cup lost its raw/prepared contract");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 45,
				"Cosmic Jelly Reefs requires provider revision 45");
		JsonObject templates = provider.getAsJsonObject("templates");
		JsonObject normal = templates.getAsJsonObject("cakeworld:edible_world")
				.getAsJsonObject("profile");
		JsonObject base = templates
				.getAsJsonObject("cakeworld:edible_world_basemetals")
				.getAsJsonObject("profile");
		JsonObject normalEnd = normal.getAsJsonObject("biome_palettes")
				.getAsJsonObject("cakeworld:end");
		JsonObject baseEnd = base.getAsJsonObject("biome_palettes")
				.getAsJsonObject("cakeworld:end");
		JsonObject geomes = normal.getAsJsonObject("biomes")
				.getAsJsonObject(BIOME_ID.toString());
		JsonObject placement = normalEnd.getAsJsonObject("biomes")
				.getAsJsonObject(BIOME_ID.toString());
		JsonObject surface = placement.getAsJsonObject("surface");
		require(helper, normalEnd.equals(baseEnd)
						&& normalEnd.getAsJsonObject("biomes").size() == 7
						&& geomes.size() == 2
						&& close(geomes.get("cakeworld:meringue_crust")
								.getAsDouble(), 4.0D)
						&& close(geomes.get("cakeworld:rock_candy_uplift")
								.getAsDouble(), 16.0D)
						&& close(placement.get("weight").getAsDouble(), 0.6D)
						&& strings(placement.getAsJsonArray("similar_biomes"))
								.equals(Set.of("minecraft:end_midlands"))
						&& strings(placement.getAsJsonArray(
								"required_similar_biomes")).isEmpty()
						&& close(placement.get("min_temperature")
								.getAsDouble(), -2.0D)
						&& close(placement.get("max_temperature")
								.getAsDouble(), 2.0D)
						&& close(placement.get("min_downfall")
								.getAsDouble(), 0.0D)
						&& close(placement.get("max_downfall")
								.getAsDouble(), 1.0D)
						&& "cakeworld:cosmic_jelly".equals(
								surface.get("top_block").getAsString())
						&& "cakeworld:rock_candy".equals(
								surface.get("filler_block").getAsString())
						&& "cakeworld:candy_glass".equals(
								surface.get("underwater_block").getAsString())
						&& surface.get("filler_depth").getAsInt() == 4,
				"Adventure profiles lost their Cosmic Jelly Reefs contract");
	}

	private static void prepareSite(ServerLevel level, BlockPos centre) {
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-4, -1, -4), centre.offset(5, 7, 5)))
				.forEach(Entity::discard);
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				level.setBlock(centre.offset(x, -1, z),
						Blocks.END_STONE.defaultBlockState(), 2);
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.COSMIC_JELLY.get()
								.defaultBlockState(), 2);
				for (int y = 1; y <= 6; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
	}

	private static PlanAudit inspectPlan(ServerLevel level, BlockPos centre,
			Rotation rotation) {
		int meringueBricks = 0;
		int cosmicJelly = 0;
		int lemonade = 0;
		int raspberry = 0;
		int blueberry = 0;
		int grape = 0;
		int lime = 0;
		int glass = 0;
		int endRods = 0;
		int racks = 0;
		int bowls = 0;
		int sentinelBricks = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 5; y++) {
					BlockState state = level.getBlockState(
							CosmicJellyNurseryFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())) {
						meringueBricks++;
					} else if (state.is(CakeWorldBlocks.COSMIC_JELLY.get())) {
						cosmicJelly++;
					} else if (state.is(CakeWorldFluids.LEMONADE_BLOCK.get())) {
						lemonade++;
					} else if (state.is(
							CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get())) {
						raspberry++;
					} else if (state.is(
							CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get())) {
						blueberry++;
					} else if (state.is(CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get())) {
						grape++;
					} else if (state.is(CakeWorldBlocks.GUMMY_BLOCK.get())) {
						lime++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(Blocks.END_ROD)) {
						endRods++;
					} else if (state.is(CakeWorldBlocks.COOLING_RACK.get())) {
						racks++;
					} else if (state.is(CakeWorldBlocks.MIXING_BOWL.get())) {
						bowls++;
					} else if (state.is(Blocks.BRICKS)) {
						sentinelBricks++;
					}
				}
			}
		}
		return new PlanAudit(meringueBricks, cosmicJelly, lemonade,
				raspberry, blueberry, grape, lime, glass, endRods,
				racks, bowls, sentinelBricks);
	}

	private static FoundNursery findNursery(ServerLevel level,
			ChunkPos anchor, int radius) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		int reefColumns = 0;
		int mixingBowls = 0;
		for (int ring = 0; ring <= radius; ring++) {
			for (int chunkX = anchor.x - ring;
					chunkX <= anchor.x + ring; chunkX++) {
				for (int chunkZ = anchor.z - ring;
						chunkZ <= anchor.z + ring; chunkZ++) {
					if (ring > 0
							&& Math.abs(chunkX - anchor.x) != ring
							&& Math.abs(chunkZ - anchor.z) != ring) {
						continue;
					}
					ChunkPos chunk = new ChunkPos(chunkX, chunkZ);
					level.getChunk(chunkX, chunkZ);
					for (int x = chunk.getMinBlockX();
							x <= chunk.getMaxBlockX(); x++) {
						for (int z = chunk.getMinBlockZ();
								z <= chunk.getMaxBlockZ(); z++) {
							cursor.set(x, 64, z);
							if (level.getBiome(cursor).is(BIOME_KEY)) {
								reefColumns++;
							}
							for (int y = 0; y <= 127; y++) {
								cursor.set(x, y, z);
								if (!level.getBlockState(cursor).is(
										CakeWorldBlocks.MIXING_BOWL.get())) {
									continue;
								}
								mixingBowls++;
								for (Rotation rotation : ROTATIONS) {
									BlockPos offset = new BlockPos(
											-1, 1, 3).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(offset);
									PlanAudit plan = inspectPlan(level, centre,
											rotation);
									if (plan.identifies(false)) {
										return new FoundNursery(centre, rotation);
									}
									if (level.getBiome(cursor).is(BIOME_KEY)) {
										LOGGER.info("Rejected Cosmic Jelly Nursery candidate: bowl={}, centre={}, rotation={}, plan={}",
												cursor.immutable(), centre, rotation, plan);
									}
								}
							}
						}
					}
				}
			}
		}
		LOGGER.info("Cosmic Jelly Nursery search found no complete plan: anchorChunk={}, radius={}, reefColumnsAtY64={}, mixingBowls={}",
				anchor, radius, reefColumns, mixingBowls);
		LOGGER.info("Cosmic Jelly Nursery safe-site diagnostic: {}",
				diagnoseSafeSites(level, anchor, radius));
		return null;
	}

	private static SafeSiteAudit diagnoseSafeSites(ServerLevel level,
			ChunkPos anchor, int radius) {
		int reefCentres = 0;
		int safeSites = 0;
		int reliefProblems = 0;
		int groundProblems = 0;
		int obstacleProblems = 0;
		BlockPos firstSafe = null;
		for (int chunkX = anchor.x - radius;
				chunkX <= anchor.x + radius; chunkX++) {
			for (int chunkZ = anchor.z - radius;
					chunkZ <= anchor.z + radius; chunkZ++) {
				ChunkPos chunk = new ChunkPos(chunkX, chunkZ);
				level.getChunk(chunkX, chunkZ);
				for (int x = chunk.getMinBlockX() + 4;
						x <= chunk.getMinBlockX() + 11; x++) {
					for (int z = chunk.getMinBlockZ() + 4;
							z <= chunk.getMinBlockZ() + 11; z++) {
						int highest = Integer.MIN_VALUE;
						for (int offsetX = -4; offsetX <= 4; offsetX++) {
							for (int offsetZ = -4; offsetZ <= 4; offsetZ++) {
								highest = Math.max(highest, level.getHeight(
										Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
										x + offsetX, z + offsetZ) - 1);
							}
						}
						BlockPos centre = new BlockPos(x, highest, z);
						if (!level.getBiome(centre).is(BIOME_KEY)) {
							continue;
						}
						reefCentres++;
						String problem = CosmicJellyNurseryFeature
								.footprintProblem(level, centre, Rotation.NONE);
						if (problem == null) {
							safeSites++;
							if (firstSafe == null) {
								firstSafe = centre;
							}
						} else if (problem.startsWith("relief")) {
							reliefProblems++;
						} else if (problem.startsWith("ground")) {
							groundProblems++;
						} else {
							obstacleProblems++;
						}
					}
				}
			}
		}
		return new SafeSiteAudit(reefCentres, safeSites, firstSafe,
				reliefProblems, groundProblems, obstacleProblems);
	}

	private static NaturalAudit audit(ServerLevel level, ChunkPos anchor,
			int radius, BlockPos nursery) {
		int biomeSamples = 0;
		int cosmicJelly = 0;
		int surfaceJelly = 0;
		int biscuitStone = 0;
		int waferRock = 0;
		int rockCandy = 0;
		int nougatRock = 0;
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int chunkX = anchor.x - radius;
				chunkX <= anchor.x + radius; chunkX++) {
			for (int chunkZ = anchor.z - radius;
					chunkZ <= anchor.z + radius; chunkZ++) {
				ChunkPos chunk = new ChunkPos(chunkX, chunkZ);
				level.getChunk(chunkX, chunkZ);
				for (int x = chunk.getMinBlockX();
						x <= chunk.getMaxBlockX(); x++) {
					for (int z = chunk.getMinBlockZ();
							z <= chunk.getMaxBlockZ(); z++) {
						for (int y = 0; y <= 127; y++) {
							cursor.set(x, y, z);
							if (!level.getBiome(cursor).is(BIOME_KEY)) {
								continue;
							}
							biomeSamples++;
							if (nearNursery(cursor, nursery)) {
								continue;
							}
							BlockState state = level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks.COSMIC_JELLY.get())) {
								cosmicJelly++;
								BlockState below = level.getBlockState(cursor.below());
								if (below.is(CakeWorldBlocks.ROCK_CANDY.get())) {
									surfaceJelly++;
								}
							} else if (state.is(
									CakeWorldBlocks.BISCUIT_STONE.get())) {
								biscuitStone++;
							} else if (state.is(CakeWorldBlocks.WAFER_ROCK.get())) {
								waferRock++;
							} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
								rockCandy++;
							} else if (state.is(CakeWorldBlocks.NOUGAT_ROCK.get())) {
								nougatRock++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, cosmicJelly, surfaceJelly,
				biscuitStone, waferRock, rockCandy, nougatRock);
	}

	private static boolean nearNursery(BlockPos position, BlockPos centre) {
		return Math.abs(position.getX() - centre.getX()) <= 3
				&& position.getY() >= centre.getY()
				&& position.getY() <= centre.getY() + 5
				&& Math.abs(position.getZ() - centre.getZ()) <= 3;
	}

	private static Set<Integer> entityIds(ServerLevel level, BlockPos centre) {
		Set<Integer> ids = new HashSet<>();
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-3, 0, -3), centre.offset(4, 6, 4)))
				.forEach(entity -> ids.add(entity.getId()));
		return ids;
	}

	private static int countBlockEntities(ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 4; y++) {
					if (level.getBlockEntity(centre.offset(x, y, z)) != null) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type) {
		for (MobCategory category : MobCategory.values()) {
			for (MobSpawnSettings.SpawnerData spawn : biome.getMobSettings()
					.getMobs(category).unwrap()) {
				if (spawn.type == type) {
					return spawn;
				}
			}
		}
		return null;
	}

	private static boolean hasPlacedFeature(Biome biome,
			Holder<PlacedFeature> expected) {
		int step = GenerationStep.Decoration.TOP_LAYER_MODIFICATION.ordinal();
		return biome != null
				&& biome.getGenerationSettings().features().size() > step
				&& biome.getGenerationSettings().features().get(step).stream()
						.anyMatch(feature -> feature.equals(expected));
	}

	private static boolean hasEffect(FoodProperties food,
			MobEffect effect, int duration) {
		for (Pair<MobEffectInstance, Float> entry : food.getEffects()) {
			if (entry.getFirst().getEffect() == effect
					&& entry.getFirst().getDuration() == duration
					&& close(entry.getSecond(), 1.0D)) {
				return true;
			}
		}
		return false;
	}

	private static boolean ingredient(Recipe<?> recipe, ItemStack stack) {
		return recipe.getIngredients().stream()
				.anyMatch(ingredient -> ingredient.test(stack));
	}

	private static long ingredientCount(Recipe<?> recipe, ItemStack stack) {
		return recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.test(stack)).count();
	}

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element -> values.add(element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				CosmicJellyReefsGameTests.class.getResourceAsStream(
						"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Could not read generated CakeWorld provider", exception);
		}
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private static boolean close(double actual, double expected) {
		return Math.abs(actual - expected) < 0.00001D;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private record PlanAudit(int meringueBricks, int cosmicJelly,
			int lemonade, int raspberry, int blueberry, int grape, int lime,
			int glass, int endRods, int racks, int bowls,
			int sentinelBricks) {
		private boolean identifies(boolean exactFoundation) {
			return (exactFoundation ? meringueBricks == 49
					: meringueBricks >= 49)
					&& cosmicJelly == 52 && lemonade == 27
					&& raspberry == 1 && blueberry == 1
					&& grape == 1 && lime == 1 && glass == 25
					&& endRods + sentinelBricks == 4
					&& racks == 1 && bowls == 1;
		}

		private boolean complete(boolean brickSentinel) {
			return identifies(true)
					&& endRods == (brickSentinel ? 3 : 4)
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundNursery(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int cosmicJelly,
			int surfaceJelly, int biscuitStone, int waferRock,
			int rockCandy, int nougatRock) {
	}

	private record SafeSiteAudit(int reefCentres, int safeSites,
			BlockPos firstSafe, int reliefProblems, int groundProblems,
			int obstacleProblems) {
	}
}
