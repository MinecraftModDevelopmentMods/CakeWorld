package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.FudgeFondueFountainFeature;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/** Contract proof for the first complete BIO-NE-001 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class FudgeWastesGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID = id("fudge_wastes");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private FudgeWastesGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bione001")
	public static void wastesHaveReadableAtmosphereFoodEcologyAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome wastes = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, wastes != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.NETHER
						&& close(wastes.getBaseTemperature(), 2.0D)
						&& close(wastes.getDownfall(), 0.0D),
				"Fudge Wastes are not a Nether-Wastes-derived hot, dry biome");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.HOT,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.WASTELAND)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Fudge Wastes lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				wastes.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				wastes.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.FUDGE_WASTES_BUBBLE.getId())
						&& close(ambience.getTickChance(), 0.0015D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.DRIPPING_LAVA,
				"Fudge Wastes lost their visible bubbling atmosphere");

		assertSpawn(helper, wastes, EntityType.ZOMBIFIED_PIGLIN,
				CakeWorldEntities.STALE_FUDGE_FOLK.get(),
				MobCategory.MONSTER, 100, 4, 4);
		assertSpawn(helper, wastes, EntityType.GHAST,
				CakeWorldEntities.MALLOW_FLOATER.get(),
				MobCategory.MONSTER, 50, 4, 4);
		assertSpawn(helper, wastes, EntityType.MAGMA_CUBE,
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				MobCategory.MONSTER, 2, 4, 4);
		assertSpawn(helper, wastes, EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER.get(),
				MobCategory.MONSTER, 1, 4, 4);
		assertSpawn(helper, wastes, EntityType.PIGLIN,
				CakeWorldEntities.FUDGE_FOLK.get(),
				MobCategory.MONSTER, 15, 4, 4);
		assertSpawn(helper, wastes, EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER.get(),
				MobCategory.CREATURE, 60, 1, 2);
		assertSpawn(helper, wastes, EntityType.BLAZE,
				CakeWorldEntities.CINNAMON_SPARK.get(),
				MobCategory.MONSTER, 10, 1, 3);
		assertSpawn(helper, wastes, EntityType.HOGLIN,
				CakeWorldEntities.FUDGE_BOAR.get(),
				MobCategory.MONSTER, 9, 3, 4);
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += wastes.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, totalSpawns == 8,
				"Fudge Wastes gained an undocumented creature role: "
						+ totalSpawns);

		assertFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione001")
	public static void fondueFountainIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				FudgeFondueFountainFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== FudgeFondueFountainFeature.FEATURE
						&& FudgeFondueFountainFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 6
						&& FudgeFondueFountainFeature
								.MIN_SOLID_SUPPORTS == 25,
				"Fudge Fondue Fountain registration or bounded constants changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0) instanceof RarityFilter
						&& modifiers.get(1) instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightRangePlacement
						&& modifiers.get(3) instanceof BiomeFilter,
				"Fudge Fondue Fountain lost its rare bounded Nether chain");
		require(helper, hasPlacedFeature(level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID),
				placed),
				"Fudge Wastes did not receive the Fondue Fountain feature");

		BlockPos helperPos = helper.absolutePos(new BlockPos(8, 5, 8));
		BlockPos centre = new BlockPos(helperPos.getX(), 64,
				helperPos.getZ());
		require(helper, FudgeFondueFountainFeature.fitsWithinChunk(
				new BlockPos(8, 64, 8), Rotation.NONE,
				new ChunkPos(0, 0)),
				"Fudge Fondue Fountain rejected a centred chunk fixture");
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre, 49);
			require(helper,
					FudgeFondueFountainFeature
									.hasSafeSite(level, centre, rotation)
							&& FudgeFondueFountainFeature
									.buildAt(level, centre, rotation),
					"Fudge Fondue Fountain rejected safe rotation "
							+ rotation);
			assertPlan(helper, level, centre, rotation, false);
		}

		prepareSite(level, centre, 49);
		BlockPos wet = FudgeFondueFountainFeature.local(
				centre, Rotation.NONE, 2, 1, 2);
		level.setBlock(wet, Blocks.WATER.defaultBlockState(), 2);
		require(helper, !FudgeFondueFountainFeature
				.hasSafeSite(level, centre, Rotation.NONE),
				"Fudge Fondue Fountain replaced an existing fluid");

		prepareSite(level, centre, 49);
		BlockPos chest = FudgeFondueFountainFeature.local(
				centre, Rotation.NONE, -2, 1, -2);
		level.setBlock(chest, Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !FudgeFondueFountainFeature
				.hasSafeSite(level, centre, Rotation.NONE),
				"Fudge Fondue Fountain replaced a block entity or inventory");

		prepareSite(level, centre, 24);
		require(helper, !FudgeFondueFountainFeature
				.hasSafeSite(level, centre, Rotation.NONE),
				"Fudge Fondue Fountain ignored its solid-support floor");
		require(helper, !FudgeFondueFountainFeature.fitsWithinChunk(
				new BlockPos(new ChunkPos(centre).getMinBlockX(),
						centre.getY(), new ChunkPos(centre).getMinBlockZ()),
				Rotation.NONE, new ChunkPos(centre)),
				"Fudge Fondue Fountain crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione001world",
			timeoutTicks = 24000)
	public static void focusedNaturalFudgeWastesAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel().getServer()
				.getLevel(Level.NETHER);
		require(helper, level != null,
				"The fixed-seed server did not expose the Nether");
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				biome -> biome.is(BIOME_KEY),
				new BlockPos(0, 64, 0), 32768, 8);
		require(helper, match != null,
				"Could not locate Fudge Wastes within 32,768 Nether blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundFountain found = findFountain(level, anchor, 7);
		require(helper, found != null,
				"Could not find a natural Fudge Fondue Fountain within 225 chunks of "
						+ anchor);
		NaturalAudit audit = audit(level, new ChunkPos(found.centre()),
				2, found.centre());
		PlanAudit plan = inspectPlan(level, found.centre(),
				found.rotation());
		BlockPos sentinel = FudgeFondueFountainFeature.local(
				found.centre(), found.rotation(), -3, 2, -3);
		boolean brickSentinel = level.getBlockState(sentinel)
				.is(Blocks.BRICKS);
		LOGGER.info("Fudge Wastes audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, fudgeRock={}, burntSugar={}, hotFudgeOutsideLandmark={}, literalNetherrack={}, plan={}, brickSentinel={}, sentinel={}",
				anchor, found.centre(), found.rotation(),
				audit.biomeSamples(), audit.fudgeRock(),
				audit.burntSugar(), audit.hotFudgeOutsideLandmark(),
				audit.literalNetherrack(), plan,
				brickSentinel, sentinel);
		require(helper,
				audit.biomeSamples() >= 128
						&& audit.fudgeRock() > 0
						&& audit.hotFudgeOutsideLandmark() > 0
						&& plan.complete(brickSentinel),
				"Natural Fudge Wastes lost their Fudge-Rock geology, Hot Fudge or complete fountain: "
						+ audit + " / " + plan);
		if (!brickSentinel) {
			level.setBlock(sentinel,
					Blocks.BRICKS.defaultBlockState(), 2);
			require(helper, level.getBlockState(sentinel)
					.is(Blocks.BRICKS),
					"Could not seed the Fudge Fountain reload sentinel");
		}
		helper.succeed();
	}

	private static void assertFood(GameTestHelper helper,
			ServerLevel level) {
		FoodProperties food = CakeWorldItems.FUDGE_FONDUE_DUNK.get()
				.getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("fudge_fondue_dunk")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 7
						&& close(food.getSaturationModifier(), 0.75D)
						&& hasEffect(food, MobEffects.FIRE_RESISTANCE, 300)
						&& hasEffect(food,
								CakeWorldEffects.COCOA_COMFORT.get(), 200)
						&& food.getNutrition()
								> CakeWorldItems.FUDGE_SQUARE.get()
										.getFoodProperties().getNutrition()
						&& recipe != null
						&& recipe.getType() == RecipeType.CRAFTING
						&& recipe.getIngredients().size() == 3
						&& ingredient(recipe,
								new ItemStack(CakeWorldItems.FUDGE_SQUARE.get()))
						&& ingredient(recipe,
								new ItemStack(CakeWorldItems.SIMPLE_BISCUIT.get()))
						&& ingredient(recipe,
								new ItemStack(CakeWorldBlocks.MARSHMALLOW.get()))
						&& recipe.getResultItem().is(
								CakeWorldItems.FUDGE_FONDUE_DUNK.get()),
				"Fudge Fondue Dunk lost its worthwhile protective recipe");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 33,
				"Fudge Wastes require provider revision 33");
		JsonObject firstPalette = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = provider.getAsJsonObject("templates")
					.getAsJsonObject(template)
					.getAsJsonObject("profile");
			JsonObject geomes = profile.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject nether = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:nether");
			JsonObject placement = nether.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface = placement.getAsJsonObject("surface");
			List<String> order = nether.getAsJsonObject("biomes")
					.entrySet().stream().map(Map.Entry::getKey).toList();
			require(helper,
					geomes.size() == 1
							&& close(geomes.get("cakeworld:fudge_mantle")
									.getAsDouble(), 12.0D)
							&& close(placement.get("weight")
									.getAsDouble(), 1.0D)
							&& strings(placement.getAsJsonArray(
									"similar_biomes")).equals(Set.of(
											"minecraft:nether_wastes"))
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
							&& "cakeworld:fudge_rock".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:fudge_rock".equals(
									surface.get("filler_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5
							&& order.indexOf("cakeworld:burnt_toffee_deltas")
									< order.indexOf(BIOME_ID.toString()),
					template
							+ " lost its Fudge-Wastes selector, surface or geome boundary");
			if (firstPalette == null) {
				firstPalette = nether;
			} else {
				require(helper, firstPalette.equals(nether),
						"Normal and BaseMetals Nether palettes diverged");
			}
		}
	}

	private static void prepareSite(ServerLevel level,
			BlockPos centre, int supports) {
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-4, -1, -4),
						centre.offset(5, 5, 5)))
				.forEach(Entity::discard);
		int remaining = supports;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 4; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, -1, z),
						Blocks.AIR.defaultBlockState(), 2);
				if (Math.abs(x) <= 3 && Math.abs(z) <= 3
						&& remaining-- > 0) {
					level.setBlock(centre.offset(x, -1, z),
							CakeWorldBlocks.FUDGE_ROCK.get()
									.defaultBlockState(), 2);
				}
			}
		}
	}

	private static void assertPlan(GameTestHelper helper,
			ServerLevel level, BlockPos centre, Rotation rotation,
			boolean brickSentinel) {
		PlanAudit plan = inspectPlan(level, centre, rotation);
		require(helper, plan.complete(brickSentinel),
				"Fudge Fondue Fountain palette changed for "
						+ rotation + ": " + plan);
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 2; y++) {
					require(helper, level.getBlockEntity(
							FudgeFondueFountainFeature.local(
									centre, rotation, x, y, z)) == null,
							"Fudge Fountain created a block entity");
				}
			}
		}
		AABB bounds = new AABB(centre.offset(-3, 0, -3),
				centre.offset(4, 4, 4));
		require(helper, level.getEntitiesOfClass(Entity.class, bounds)
				.isEmpty(), "Fudge Fountain spawned an entity");
	}

	private static FoundFountain findFountain(ServerLevel level,
			ChunkPos anchor, int radius) {
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
					BlockPos.MutableBlockPos cursor =
							new BlockPos.MutableBlockPos();
					for (int x = chunk.getMinBlockX();
							x <= chunk.getMaxBlockX(); x++) {
						for (int z = chunk.getMinBlockZ();
								z <= chunk.getMaxBlockZ(); z++) {
							for (int y = FudgeFondueFountainFeature.MIN_Y;
									y <= FudgeFondueFountainFeature.MAX_Y + 2;
									y++) {
								cursor.set(x, y, z);
								if (!level.getBlockState(cursor).is(
										CakeWorldBlocks.COOLING_RACK.get())) {
									continue;
								}
								for (Rotation rotation : ROTATIONS) {
									BlockPos offset = new BlockPos(-1, 1, -3)
											.rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(offset);
									PlanAudit plan = inspectPlan(
											level, centre, rotation);
									if (plan.complete(false)
											|| plan.complete(true)) {
										return new FoundFountain(
												centre, rotation);
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

	private static PlanAudit inspectPlan(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		int bricks = 0;
		int marshmallow = 0;
		int hotFudge = 0;
		int pillars = 0;
		int glass = 0;
		int wafer = 0;
		int racks = 0;
		int bowls = 0;
		int sentinelBricks = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 2; y++) {
					BlockState state = level.getBlockState(
							FudgeFondueFountainFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks
							.BURNT_TOFFEE_BRICKS.get())) {
						bricks++;
					} else if (state.is(CakeWorldBlocks
							.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldFluids
							.HOT_FUDGE_BLOCK.get())) {
						hotFudge++;
					} else if (state.is(CakeWorldBlocks
							.BURNT_TOFFEE_PILLAR.get())) {
						pillars++;
					} else if (state.is(CakeWorldBlocks
							.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(CakeWorldBlocks
							.WAFER_BLOCK.get())) {
						wafer++;
					} else if (state.is(CakeWorldBlocks
							.COOLING_RACK.get())) {
						racks++;
					} else if (state.is(CakeWorldBlocks
							.MIXING_BOWL.get())) {
						bowls++;
					} else if (state.is(Blocks.BRICKS)) {
						sentinelBricks++;
					}
				}
			}
		}
		return new PlanAudit(bricks, marshmallow, hotFudge,
				pillars, glass, wafer, racks, bowls, sentinelBricks);
	}

	private static NaturalAudit audit(ServerLevel level,
			ChunkPos anchor, int radius, BlockPos landmark) {
		int biomeSamples = 0;
		int fudgeRock = 0;
		int burntSugar = 0;
		int hotFudgeOutsideLandmark = 0;
		int literalNetherrack = 0;
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
							BlockState state = level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks.FUDGE_ROCK.get())) {
								fudgeRock++;
							} else if (state.is(CakeWorldBlocks
									.BURNT_SUGAR_ROCK.get())) {
								burntSugar++;
							} else if (state.is(Blocks.NETHERRACK)) {
								literalNetherrack++;
							}
							if (state.is(CakeWorldFluids.HOT_FUDGE_BLOCK.get())
									&& (Math.abs(x - landmark.getX()) > 4
											|| Math.abs(y - landmark.getY()) > 4
											|| Math.abs(z - landmark.getZ()) > 4)) {
								hotFudgeOutsideLandmark++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, fudgeRock, burntSugar,
				hotFudgeOutsideLandmark, literalNetherrack);
	}

	private static void assertSpawn(GameTestHelper helper, Biome biome,
			EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category, int weight, int minimum, int maximum) {
		MobSpawnSettings.SpawnerData converted = findSpawn(biome, replacement);
		require(helper, converted != null
						&& replacement.getCategory() == category
						&& converted.getWeight().asInt() == weight
						&& converted.minCount == minimum
						&& converted.maxCount == maximum
						&& findSpawn(biome, vanilla) == null,
				"Fudge Wastes lost replacement "
						+ replacement.getRegistryName() + " for "
						+ vanilla.getRegistryName());
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type) {
		for (MobCategory category : MobCategory.values()) {
			for (MobSpawnSettings.SpawnerData spawn : biome
					.getMobSettings().getMobs(category).unwrap()) {
				if (spawn.type == type) {
					return spawn;
				}
			}
		}
		return null;
	}

	private static boolean hasPlacedFeature(Biome biome,
			Holder<PlacedFeature> expected) {
		if (biome == null) {
			return false;
		}
		int step = GenerationStep.Decoration.TOP_LAYER_MODIFICATION
				.ordinal();
		return biome.getGenerationSettings().features().size() > step
				&& biome.getGenerationSettings().features().get(step)
						.stream().anyMatch(feature -> feature.equals(expected));
	}

	private static boolean ingredient(Recipe<?> recipe, ItemStack stack) {
		return recipe.getIngredients().stream()
				.anyMatch(ingredient -> ingredient.test(stack));
	}

	private static boolean hasEffect(FoodProperties food,
			MobEffect effect, int duration) {
		return food.getEffects().stream().anyMatch(entry ->
				entry.getFirst().getEffect() == effect
						&& entry.getFirst().getDuration() == duration);
	}

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element -> values.add(element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				FudgeWastesGameTests.class.getResourceAsStream(
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

	private record FoundFountain(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int fudgeRock,
			int burntSugar, int hotFudgeOutsideLandmark,
			int literalNetherrack) {
	}

	private record PlanAudit(int bricks, int marshmallow, int hotFudge,
			int pillars, int glass, int wafer, int racks, int bowls,
			int sentinelBricks) {
		private boolean complete(boolean brickSentinel) {
			return bricks == 36 && marshmallow == 4 && hotFudge == 8
					&& pillars == 2
					&& glass == (brickSentinel ? 8 : 9)
					&& wafer == 4 && racks == 1 && bowls == 1
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}
}
