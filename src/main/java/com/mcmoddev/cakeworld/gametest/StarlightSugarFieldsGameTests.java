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
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.SugarStarObservatoryFeature;

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
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/** Contract proof for the complete BIO-END-004 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class StarlightSugarFieldsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID = id(
			"starlight_sugar_fields");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private StarlightSugarFieldsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioend004")
	public static void fieldsHaveLuminousEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome fields = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, fields != null
						&& close(fields.getBaseTemperature(), 0.5D)
						&& close(fields.getDownfall(), 0.0D),
				"Starlight Sugar Fields lost their cool, dry End climate");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.VOID,
				BiomeDictionary.Type.MAGICAL,
				BiomeDictionary.Type.PLAINS)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Starlight Sugar Fields lost dictionary type " + type);
		}

		AmbientAdditionsSettings ambience =
				fields.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				fields.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds.STARLIGHT_SUGAR_FIELDS_TWINKLE
										.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.ELECTRIC_SPARK,
				"Starlight Sugar Fields lost their sparse crystal atmosphere");

		MobSpawnSettings.SpawnerData tallwalker = findSpawn(fields,
				CakeWorldEntities.TAFFY_TALLWALKER.get());
		MobSpawnSettings.SpawnerData sugarMite = findSpawn(fields,
				CakeWorldEntities.SUGAR_MITE.get());
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += fields.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, tallwalker != null
						&& tallwalker.getWeight().asInt() == 10
						&& tallwalker.minCount == 4
						&& tallwalker.maxCount == 4
						&& sugarMite != null
						&& sugarMite.getWeight().asInt() == 8
						&& sugarMite.minCount == 2
						&& sugarMite.maxCount == 4
						&& findSpawn(fields, EntityType.ENDERMAN) == null
						&& totalSpawns == 2,
				"Starlight Sugar Fields lost their exact Tallwalker/Mite ecology: "
						+ totalSpawns);

		for (String tag : List.of(
				"minecraft:has_structure/end_city",
				"cakeworld:has_structure/macaron_citadel")) {
			require(helper, holder.is(TagKey.create(Registry.BIOME_REGISTRY,
					new ResourceLocation(tag))),
					"Starlight Sugar Fields lost structure progression role "
							+ tag);
		}
		assertSurfaceAndFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend004")
	public static void sugarStarObservatoryIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				SugarStarObservatoryFeature.placedFeature();
		Biome fields = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== SugarStarObservatoryFeature.FEATURE
						&& placed.value().placement().size() == 4
						&& placed.value().placement().get(0)
								instanceof RarityFilter
						&& placed.value().placement().get(1)
								instanceof InSquarePlacement
						&& placed.value().placement().get(2)
								instanceof HeightmapPlacement
						&& placed.value().placement().get(3)
								instanceof BiomeFilter
						&& SugarStarObservatoryFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& SugarStarObservatoryFeature.MAX_TERRAIN_RELIEF == 6
						&& hasPlacedFeature(fields, placed),
				"Sugar-Star Observatory lost its bounded placement chain");

		ChunkPos helperChunk = new ChunkPos(helper.absolutePos(
				new BlockPos(8, 5, 8)));
		BlockPos centre = new BlockPos(helperChunk.getMinBlockX() + 8,
				63, helperChunk.getMinBlockZ() + 8);
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					SugarStarObservatoryFeature.hasSafeFootprint(
							level, centre, rotation)
							&& SugarStarObservatoryFeature.buildAt(
									level, centre, rotation),
					"Sugar-Star Observatory rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false),
					"Sugar-Star Observatory plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Sugar-Star Observatory created an entity or block entity");
		}

		prepareSite(level, centre);
		level.setBlock(centre.offset(3, 1, 3),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !SugarStarObservatoryFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Sugar-Star Observatory replaced an existing fluid");
		prepareSite(level, centre);
		level.setBlock(centre.offset(-3, 1, -3),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !SugarStarObservatoryFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Sugar-Star Observatory replaced a block entity");
		prepareSite(level, centre);
		level.setBlock(centre.offset(1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !SugarStarObservatoryFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Sugar-Star Observatory replaced an authored solid");
		require(helper, !SugarStarObservatoryFeature.fitsWithinChunk(
				new BlockPos(helperChunk.getMinBlockX(), centre.getY(),
						helperChunk.getMinBlockZ()), Rotation.NONE, helperChunk),
				"Sugar-Star Observatory crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend004world",
			timeoutTicks = 24000)
	public static void focusedNaturalStarlightSugarFieldsAudit(
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
				"Could not locate Starlight Sugar Fields within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundObservatory found = findObservatory(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Sugar-Star Observatory within 1,089 chunks of "
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
			int[] lamp = SugarStarObservatoryFeature.starLamps()[0];
			BlockPos sentinel = SugarStarObservatoryFeature.local(
					found.centre(), found.rotation(), lamp[0], 4, lamp[1]);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 4, found.centre());
			boolean baseMetals = ModList.get().isLoaded("basemetals");
			LOGGER.info("Starlight Sugar Fields audit: baseMetals={}, anchorChunk={}, centre={}, rotation={}, biomeSamples={}, starlightSugarGrass={}, rockCandy={}, nougatRock={}, starsteel={}, plan={}, brickSentinel={}, sentinel={}",
					baseMetals, anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.starlightSugarGrass(),
					audit.rockCandy(), audit.nougatRock(), audit.starsteel(),
					plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.starlightSugarGrass() > 0
							&& audit.rockCandy() + audit.nougatRock() > 0
							&& (baseMetals
									? audit.starsteel() > 0
									: audit.starsteel() == 0)
							&& plan.complete(brickSentinel),
					"Natural Starlight Sugar Fields lost terrain, geology, optional Starsteel or their complete Observatory: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
						"Could not seed the Observatory reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertSurfaceAndFood(
			GameTestHelper helper, ServerLevel level) {
		BlockState surface = CakeWorldBlocks.STARLIGHT_SUGAR_GRASS.get()
				.defaultBlockState();
		require(helper, surface.getLightEmission() == 7
						&& surface.is(BlockTags.MINEABLE_WITH_PICKAXE),
				"Starlight Sugar Grass lost its luminous harvest contract");

		FoodProperties raw = CakeWorldItems.STAR_SUGAR_CRYSTALS.get()
				.getFoodProperties();
		FoodProperties candy = CakeWorldItems.CONSTELLATION_CANDY.get()
				.getFoodProperties();
		Recipe<?> crystals = level.getRecipeManager()
				.byKey(id("star_sugar_crystals")).orElse(null);
		Recipe<?> constellation = level.getRecipeManager()
				.byKey(id("constellation_candy")).orElse(null);
		require(helper, raw != null
						&& raw.getNutrition() == 1
						&& close(raw.getSaturationModifier(), 0.1D)
						&& candy != null
						&& candy.getNutrition() == 6
						&& close(candy.getSaturationModifier(), 0.7D)
						&& hasEffect(candy, MobEffects.NIGHT_VISION, 400)
						&& hasEffect(candy, MobEffects.LUCK, 200)
						&& crystals != null
						&& crystals.getType() == RecipeType.CRAFTING
						&& crystals.getIngredients().size() == 1
						&& ingredient(crystals, new ItemStack(
								CakeWorldBlocks.STARLIGHT_SUGAR_GRASS.get()))
						&& crystals.getResultItem()
								.is(CakeWorldItems.STAR_SUGAR_CRYSTALS.get())
						&& crystals.getResultItem().getCount() == 4
						&& constellation != null
						&& constellation.getType() == RecipeType.CRAFTING
						&& constellation.getIngredients().size() == 4
						&& ingredientCount(constellation, new ItemStack(
								CakeWorldItems.STAR_SUGAR_CRYSTALS.get())) == 2
						&& ingredient(constellation, new ItemStack(
								CakeWorldItems.GLOWING_JAM_BERRY.get()))
						&& ingredient(constellation, new ItemStack(
								CakeWorldItems.SIMPLE_BISCUIT.get()))
						&& constellation.getResultItem()
								.is(CakeWorldItems.CONSTELLATION_CANDY.get())
						&& constellation.getResultItem().getCount() == 2,
				"Star Sugar or Constellation Candy lost its raw/prepared contract");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 43,
				"Starlight Sugar Fields require provider revision 43");
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
						&& normalEnd.getAsJsonObject("biomes").size() == 4
						&& geomes.size() == 2
						&& close(geomes.get("cakeworld:meringue_crust")
								.getAsDouble(), 8.0D)
						&& close(geomes.get("cakeworld:rock_candy_uplift")
								.getAsDouble(), 12.0D)
						&& close(placement.get("weight").getAsDouble(), 1.25D)
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
						&& "cakeworld:starlight_sugar_grass".equals(
								surface.get("top_block").getAsString())
						&& "cakeworld:rock_candy".equals(
								surface.get("filler_block").getAsString())
						&& "cakeworld:rock_candy".equals(
								surface.get("underwater_block").getAsString())
						&& surface.get("filler_depth").getAsInt() == 4,
				"Adventure profiles lost their Starlight Sugar Fields contract");

		JsonObject normalOres = normal.getAsJsonObject("ores");
		JsonObject baseRule = base.getAsJsonObject("ores")
				.getAsJsonObject("basemetals:ore/starsteel");
		JsonObject end = baseRule.getAsJsonObject("dimensions")
				.getAsJsonObject("minecraft:the_end");
		require(helper, !normalOres.has("basemetals:ore/starsteel")
						&& "cakeworld:starlight_starsteel".equals(
								baseRule.get("block").getAsString())
						&& strings(end.getAsJsonArray("host_families")).equals(
								Set.of("sedimentary", "metamorphic",
										"igneous_intrusive"))
						&& close(end.getAsJsonObject("geomes")
								.get("cakeworld:meringue_crust").getAsDouble(), 4.0D)
						&& close(end.getAsJsonObject("geomes")
								.get("cakeworld:rock_candy_uplift").getAsDouble(), 6.0D),
				"BaseMetals profile lost its optional Starsteel geology contract");
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
						CakeWorldBlocks.STARLIGHT_SUGAR_GRASS.get()
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
		int sugarGrass = 0;
		int biscuitCrumbs = 0;
		int rockCandy = 0;
		int mintCrystals = 0;
		int wafer = 0;
		int glass = 0;
		int endRods = 0;
		int coolingRacks = 0;
		int mixingBowls = 0;
		int sentinelBricks = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 4; y++) {
					BlockState state = level.getBlockState(
							SugarStarObservatoryFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())) {
						meringueBricks++;
					} else if (state.is(
							CakeWorldBlocks.STARLIGHT_SUGAR_GRASS.get())) {
						sugarGrass++;
					} else if (state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())) {
						biscuitCrumbs++;
					} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
						rockCandy++;
					} else if (state.is(CakeWorldBlocks.MINT_CRYSTAL.get())) {
						mintCrystals++;
					} else if (state.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
						wafer++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(Blocks.END_ROD)) {
						endRods++;
					} else if (state.is(CakeWorldBlocks.COOLING_RACK.get())) {
						coolingRacks++;
					} else if (state.is(CakeWorldBlocks.MIXING_BOWL.get())) {
						mixingBowls++;
					} else if (state.is(Blocks.BRICKS)) {
						sentinelBricks++;
					}
				}
			}
		}
		BlockState telescope = level.getBlockState(
				SugarStarObservatoryFeature.local(
						centre, rotation, 0, 2, -3));
		boolean telescopeAligned = telescope.is(Blocks.END_ROD)
				&& telescope.getValue(EndRodBlock.FACING)
						== rotation.rotate(Direction.NORTH);
		return new PlanAudit(meringueBricks, sugarGrass, biscuitCrumbs,
				rockCandy, mintCrystals, wafer, glass, endRods,
				coolingRacks, mixingBowls, sentinelBricks,
				telescopeAligned);
	}

	private static FoundObservatory findObservatory(ServerLevel level,
			ChunkPos anchor, int radius) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
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
							for (int y = 0; y <= 127; y++) {
								cursor.set(x, y, z);
								if (!level.getBlockState(cursor).is(
										CakeWorldBlocks.MIXING_BOWL.get())) {
									continue;
								}
								for (Rotation rotation : ROTATIONS) {
									BlockPos markerOffset = new BlockPos(
											-2, 2, 0).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(markerOffset);
									PlanAudit plan = inspectPlan(level, centre,
											rotation);
									if (plan.identifies(false)) {
										return new FoundObservatory(centre, rotation);
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private static NaturalAudit audit(ServerLevel level, ChunkPos anchor,
			int radius, BlockPos observatory) {
		int biomeSamples = 0;
		int sugarGrass = 0;
		int rockCandy = 0;
		int nougatRock = 0;
		int starsteel = 0;
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
							if (nearObservatory(cursor, observatory)) {
								continue;
							}
							BlockState state = level.getBlockState(cursor);
							if (state.is(
									CakeWorldBlocks.STARLIGHT_SUGAR_GRASS.get())) {
								sugarGrass++;
							} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
								rockCandy++;
							} else if (state.is(CakeWorldBlocks.NOUGAT_ROCK.get())) {
								nougatRock++;
							} else if (state.is(
									CakeWorldBlocks.STARLIGHT_STARSTEEL.get())) {
								starsteel++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, sugarGrass,
				rockCandy, nougatRock, starsteel);
	}

	private static boolean nearObservatory(BlockPos position, BlockPos centre) {
		return Math.abs(position.getX() - centre.getX()) <= 3
				&& position.getY() >= centre.getY()
				&& position.getY() <= centre.getY() + 4
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
				StarlightSugarFieldsGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int meringueBricks, int sugarGrass,
			int biscuitCrumbs, int rockCandy, int mintCrystals,
			int wafer, int glass, int endRods, int coolingRacks,
			int mixingBowls, int sentinelBricks, boolean telescopeAligned) {
		private boolean identifies(boolean exactFoundation) {
			return (exactFoundation ? meringueBricks == 49
					: meringueBricks >= 49)
					&& sugarGrass == 24 && biscuitCrumbs == 9
					&& rockCandy == 16 && mintCrystals == 4
					&& wafer == 2 && glass == 5
					&& endRods + sentinelBricks == 5
					&& coolingRacks == 1 && mixingBowls == 1
					&& telescopeAligned;
		}

		private boolean complete(boolean brickSentinel) {
			return identifies(true)
					&& endRods == (brickSentinel ? 4 : 5)
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundObservatory(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int starlightSugarGrass,
			int rockCandy, int nougatRock, int starsteel) {
	}
}
