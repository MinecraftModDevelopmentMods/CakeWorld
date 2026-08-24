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
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CinnamonHearthGroveFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
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

/** Contract proof for the first complete BIO-NE-003 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class CinnamonEmberGrovesGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("cinnamon_ember_groves");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};

	private CinnamonEmberGrovesGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bione003")
	public static void grovesHaveAtmosphereFoodEcologyAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome grove = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, grove != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.NETHER
						&& close(grove.getBaseTemperature(), 2.0D)
						&& close(grove.getDownfall(), 0.0D),
				"Cinnamon Ember Groves are not a hot, dry Crimson-Forest copy");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.HOT,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.FOREST)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Cinnamon Ember Groves lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				grove.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				grove.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds
										.CINNAMON_EMBER_GROVES_CRACKLE.getId())
						&& close(ambience.getTickChance(), 0.0015D)
						&& particle != null
						&& particle.getOptions().getType() == ParticleTypes.FLAME,
				"Cinnamon Ember Groves lost their visible ember atmosphere");

		assertSpawn(helper, grove, EntityType.ZOMBIFIED_PIGLIN,
				CakeWorldEntities.STALE_FUDGE_FOLK.get(), 1, 2, 4);
		assertSpawn(helper, grove, EntityType.HOGLIN,
				CakeWorldEntities.FUDGE_BOAR.get(), 9, 3, 4);
		assertSpawn(helper, grove, EntityType.PIGLIN,
				CakeWorldEntities.FUDGE_FOLK.get(), 5, 3, 4);
		assertSpawn(helper, grove, EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER.get(), 60, 1, 2);
		assertSpawn(helper, grove, EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER.get(), 10, 1, 4);
		assertSpawn(helper, grove, EntityType.BLAZE,
				CakeWorldEntities.CINNAMON_SPARK.get(), 20, 1, 3);
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += grove.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, totalSpawns == 6,
				"Cinnamon Ember Groves gained an undocumented creature role: "
						+ totalSpawns);

		for (String tag : List.of(
				"minecraft:has_structure/bastion_remnant",
				"minecraft:has_structure/nether_fortress",
				"cakeworld:has_structure/burnt_toffee_foundry",
				"cakeworld:has_structure/liquorice_fortress",
				"cakeworld:has_structure/burnt_sugar_arch")) {
			ResourceLocation location = new ResourceLocation(tag);
			require(helper, holder.is(TagKey.create(
					Registry.BIOME_REGISTRY, location)),
					"Cinnamon Ember Groves lost progression tag " + tag);
		}

		assertFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione003")
	public static void hearthGroveIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CinnamonHearthGroveFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== CinnamonHearthGroveFeature.FEATURE
						&& CinnamonHearthGroveFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& CinnamonHearthGroveFeature
								.MIN_SOLID_SUPPORTS == 25,
				"Cinnamon Hearth Grove registration or bounds changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0) instanceof RarityFilter
						&& modifiers.get(1) instanceof InSquarePlacement
						&& modifiers.get(2) instanceof HeightRangePlacement
						&& modifiers.get(3) instanceof BiomeFilter,
				"Cinnamon Hearth Grove lost its rare bounded Nether chain");
		require(helper, hasPlacedFeature(level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID), placed),
				"Cinnamon Ember Groves did not receive their hearth feature");

		BlockPos helperPos = helper.absolutePos(new BlockPos(8, 5, 8));
		BlockPos centre = new BlockPos(helperPos.getX(), 64,
				helperPos.getZ());
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre, 49);
			Set<Integer> entityIdsBefore = entityIds(level, centre);
			require(helper,
					CinnamonHearthGroveFeature.hasSafeSite(
							level, centre, rotation)
							&& CinnamonHearthGroveFeature.buildAt(
									level, centre, rotation),
					"Cinnamon Hearth Grove rejected safe rotation " + rotation);
			assertPlan(helper, level, centre, rotation, false);
			require(helper, entityIds(level, centre).equals(entityIdsBefore),
					"Cinnamon Hearth Grove created or removed an entity");
		}

		prepareSite(level, centre, 49);
		BlockPos wet = CinnamonHearthGroveFeature.local(
				centre, Rotation.NONE, 4, 4, 4);
		level.setBlock(wet, Blocks.WATER.defaultBlockState(), 2);
		require(helper, !CinnamonHearthGroveFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cinnamon Hearth Grove replaced an existing fluid");
		prepareSite(level, centre, 49);
		BlockPos chest = CinnamonHearthGroveFeature.local(
				centre, Rotation.NONE, -4, 4, -4);
		level.setBlock(chest, Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !CinnamonHearthGroveFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cinnamon Hearth Grove replaced an inventory");
		prepareSite(level, centre, 24);
		require(helper, !CinnamonHearthGroveFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cinnamon Hearth Grove ignored its support threshold");
		require(helper, !CinnamonHearthGroveFeature.fitsWithinChunk(
				new BlockPos(new ChunkPos(centre).getMinBlockX(),
						centre.getY(), new ChunkPos(centre).getMinBlockZ()),
				Rotation.NONE, new ChunkPos(centre)),
				"Cinnamon Hearth Grove crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione003world",
			timeoutTicks = 24000)
	public static void focusedNaturalCinnamonEmberGrovesAudit(
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
				"Could not locate Cinnamon Ember Groves within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundGrove found = findGrove(level, anchor, 20);
		require(helper, found != null,
				"Could not find a natural Cinnamon Hearth Grove within 1,681 chunks of "
						+ anchor);
		NaturalAudit audit = audit(level, new ChunkPos(found.centre()), 2);
		PlanAudit plan = inspectPlan(level, found.centre(), found.rotation());
		BlockPos sentinel = CinnamonHearthGroveFeature.local(
				found.centre(), found.rotation(), -4, 5, -3);
		boolean brickSentinel = level.getBlockState(sentinel).is(Blocks.BRICKS);
		LOGGER.info("Cinnamon Ember Groves audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, cinnamonCrust={}, fudgeRock={}, cinnamonLogs={}, emberLeaves={}, plan={}, brickSentinel={}, sentinel={}",
				anchor, found.centre(), found.rotation(),
				audit.biomeSamples(), audit.cinnamonCrust(),
				audit.fudgeRock(), audit.cinnamonLogs(),
				audit.emberLeaves(), plan, brickSentinel, sentinel);
		require(helper, audit.biomeSamples() >= 128
						&& audit.cinnamonCrust() > 0
						&& audit.fudgeRock() > 0
						&& plan.complete(brickSentinel),
				"Natural Cinnamon Ember Groves lost their terrain or hearth: "
						+ audit + " / " + plan);
		if (!brickSentinel) {
			level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
			require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
					"Could not seed the Cinnamon Grove reload sentinel");
		}
		helper.succeed();
	}

	private static void assertFood(GameTestHelper helper, ServerLevel level) {
		FoodProperties food = CakeWorldItems.EMBER_CINNAMON_SWIRL.get()
				.getFoodProperties();
		Recipe<?> sticks = level.getRecipeManager()
				.byKey(id("cinnamon_stick")).orElse(null);
		Recipe<?> swirl = level.getRecipeManager()
				.byKey(id("ember_cinnamon_swirl")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 6
						&& close(food.getSaturationModifier(), 0.7D)
						&& hasEffect(food, MobEffects.FIRE_RESISTANCE, 400)
						&& hasEffect(food,
								CakeWorldEffects.SUGAR_RUSH.get(), 160)
						&& sticks != null
						&& sticks.getType() == RecipeType.CRAFTING
						&& sticks.getResultItem().getCount() == 4
						&& sticks.getResultItem().is(
								CakeWorldItems.CINNAMON_STICK.get())
						&& ingredient(sticks,
								new ItemStack(CakeWorldBlocks.CINNAMON_LOG.get()))
						&& swirl != null
						&& swirl.getType() == RecipeType.CRAFTING
						&& swirl.getIngredients().size() == 3
						&& ingredient(swirl,
								new ItemStack(CakeWorldItems.CINNAMON_STICK.get()))
						&& ingredient(swirl, new ItemStack(net.minecraft.world.item.Items.WHEAT))
						&& ingredient(swirl, new ItemStack(net.minecraft.world.item.Items.SUGAR))
						&& swirl.getResultItem().getCount() == 2
						&& swirl.getResultItem().is(
								CakeWorldItems.EMBER_CINNAMON_SWIRL.get()),
				"Ember Cinnamon Swirl lost its renewable warming recipe");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 34,
				"Cinnamon Ember Groves require provider revision 34");
		JsonObject firstPalette = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = provider.getAsJsonObject("templates")
					.getAsJsonObject(template).getAsJsonObject("profile");
			JsonObject geomes = profile.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject nether = profile.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:nether");
			JsonObject placement = nether.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface = placement.getAsJsonObject("surface");
			JsonArray depositBiomes = profile.getAsJsonObject("fluid_deposits")
					.getAsJsonObject("cakeworld:fluid_deposit/hot_fudge")
					.getAsJsonObject("dimensions")
					.getAsJsonObject("minecraft:the_nether")
					.getAsJsonArray("biome_ids");
			List<String> order = nether.getAsJsonObject("biomes")
					.entrySet().stream().map(Map.Entry::getKey).toList();
			require(helper, geomes.size() == 1
							&& close(geomes.get("cakeworld:fudge_mantle")
									.getAsDouble(), 16.0D)
							&& close(placement.get("weight").getAsDouble(), 1.25D)
							&& strings(placement.getAsJsonArray("similar_biomes"))
									.equals(Set.of("minecraft:crimson_forest"))
							&& strings(placement.getAsJsonArray(
									"required_similar_biomes")).isEmpty()
							&& close(placement.get("min_temperature").getAsDouble(), -2.0D)
							&& close(placement.get("max_temperature").getAsDouble(), 2.0D)
							&& close(placement.get("min_downfall").getAsDouble(), 0.0D)
							&& close(placement.get("max_downfall").getAsDouble(), 1.0D)
							&& "cakeworld:cinnamon_crust".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:fudge_rock".equals(
									surface.get("filler_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5
							&& strings(depositBiomes).equals(Set.of(
									"cakeworld:fudge_wastes",
									"cakeworld:burnt_toffee_deltas",
									BIOME_ID.toString(),
									"cakeworld:black_liquorice_labyrinths",
									"cakeworld:treacle_soul_valleys",
									"cakeworld:chilli_chocolate_crags",
									"cakeworld:molten_marshmallow_calderas"))
							&& order.indexOf("cakeworld:fudge_wastes")
									< order.indexOf(BIOME_ID.toString()),
					template + " lost its Cinnamon-Grove provider contract");
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
				new AABB(centre.offset(-5, -1, -5),
						centre.offset(6, 7, 6)))
				.forEach(Entity::discard);
		int remaining = supports;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				for (int y = 0; y <= 6; y++) {
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
				"Cinnamon Hearth Grove palette changed for "
						+ rotation + ": " + plan);
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 5; y++) {
					BlockPos position = CinnamonHearthGroveFeature.local(
							centre, rotation, x, y, z);
					require(helper, level.getBlockEntity(position) == null,
							"Cinnamon Hearth Grove created a block entity");
				}
			}
		}
	}

	private static Set<Integer> entityIds(
			ServerLevel level, BlockPos centre) {
		AABB bounds = new AABB(centre.offset(-4, 0, -4),
				centre.offset(5, 6, 5));
		Set<Integer> ids = new HashSet<>();
		level.getEntitiesOfClass(Entity.class, bounds)
				.forEach(entity -> ids.add(entity.getId()));
		return ids;
	}

	private static FoundGrove findGrove(ServerLevel level,
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
					BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
					for (int x = chunk.getMinBlockX(); x <= chunk.getMaxBlockX(); x++) {
						for (int z = chunk.getMinBlockZ(); z <= chunk.getMaxBlockZ(); z++) {
							for (int y = CinnamonHearthGroveFeature.MIN_Y;
									y <= CinnamonHearthGroveFeature.MAX_Y + 5; y++) {
								cursor.set(x, y, z);
								if (!level.getBlockState(cursor).is(
										CakeWorldBlocks.COOLING_RACK.get())) {
									continue;
								}
								for (Rotation rotation : ROTATIONS) {
									BlockPos offset = new BlockPos(-1, 1, -3)
											.rotate(rotation);
									BlockPos centre = cursor.immutable().subtract(offset);
									PlanAudit plan = inspectPlan(level, centre, rotation);
									if (plan.complete(false) || plan.complete(true)) {
										return new FoundGrove(centre, rotation);
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
		int crust = 0;
		int bricks = 0;
		int magma = 0;
		int marshmallow = 0;
		int glass = 0;
		int logs = 0;
		int leaves = 0;
		int racks = 0;
		int bowls = 0;
		int sentinelBricks = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 5; y++) {
					BlockState state = level.getBlockState(
							CinnamonHearthGroveFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.CINNAMON_CRUST.get())) {
						crust++;
					} else if (state.is(CakeWorldBlocks.BURNT_TOFFEE_BRICKS.get())) {
						bricks++;
					} else if (state.is(Blocks.MAGMA_BLOCK)) {
						magma++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(CakeWorldBlocks.CINNAMON_LOG.get())) {
						logs++;
					} else if (state.is(CakeWorldBlocks.EMBER_SPICE_LEAVES.get())) {
						leaves++;
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
		return new PlanAudit(crust, bricks, magma, marshmallow,
				glass, logs, leaves, racks, bowls, sentinelBricks);
	}

	private static NaturalAudit audit(ServerLevel level,
			ChunkPos anchor, int radius) {
		int biomeSamples = 0;
		int cinnamonCrust = 0;
		int fudgeRock = 0;
		int cinnamonLogs = 0;
		int emberLeaves = 0;
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int chunkX = anchor.x - radius;
				chunkX <= anchor.x + radius; chunkX++) {
			for (int chunkZ = anchor.z - radius;
					chunkZ <= anchor.z + radius; chunkZ++) {
				ChunkPos chunk = new ChunkPos(chunkX, chunkZ);
				level.getChunk(chunkX, chunkZ);
				for (int x = chunk.getMinBlockX(); x <= chunk.getMaxBlockX(); x++) {
					for (int z = chunk.getMinBlockZ(); z <= chunk.getMaxBlockZ(); z++) {
						for (int y = 0; y <= 127; y++) {
							cursor.set(x, y, z);
							if (!level.getBiome(cursor).is(BIOME_KEY)) {
								continue;
							}
							biomeSamples++;
							BlockState state = level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks.CINNAMON_CRUST.get())) {
								cinnamonCrust++;
							} else if (state.is(CakeWorldBlocks.FUDGE_ROCK.get())) {
								fudgeRock++;
							} else if (state.is(CakeWorldBlocks.CINNAMON_LOG.get())) {
								cinnamonLogs++;
							} else if (state.is(CakeWorldBlocks.EMBER_SPICE_LEAVES.get())) {
								emberLeaves++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, cinnamonCrust,
				fudgeRock, cinnamonLogs, emberLeaves);
	}

	private static void assertSpawn(GameTestHelper helper, Biome biome,
			EntityType<?> vanilla, EntityType<?> replacement,
			int weight, int minimum, int maximum) {
		MobSpawnSettings.SpawnerData converted = findSpawn(biome, replacement);
		require(helper, converted != null
						&& converted.getWeight().asInt() == weight
						&& converted.minCount == minimum
						&& converted.maxCount == maximum
						&& findSpawn(biome, vanilla) == null,
				"Cinnamon Ember Groves lost replacement "
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
		int step = GenerationStep.Decoration.TOP_LAYER_MODIFICATION.ordinal();
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
				CinnamonEmberGrovesGameTests.class.getResourceAsStream(
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

	private record FoundGrove(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int cinnamonCrust,
			int fudgeRock, int cinnamonLogs, int emberLeaves) {
	}

	private record PlanAudit(int crust, int bricks, int magma,
			int marshmallow, int glass, int logs, int leaves,
			int racks, int bowls, int sentinelBricks) {
		private boolean complete(boolean brickSentinel) {
			return crust == 36 && bricks == 8 && magma == 1
					&& marshmallow == 4 && glass == 1 && logs == 16
					&& leaves == (brickSentinel ? 51 : 52)
					&& racks == 1 && bowls == 1
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}
}
