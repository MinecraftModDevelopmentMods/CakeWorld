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
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CragfireProspectFeature;

import net.minecraft.core.BlockPos;
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

/** Contract proof for the first complete BIO-NE-006 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class ChilliChocolateCragsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("chilli_chocolate_crags");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};

	private ChilliChocolateCragsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bione006")
	public static void cragsHaveReadableMiningEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome crags = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, crags != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.NETHER
						&& close(crags.getBaseTemperature(), 2.0D)
						&& close(crags.getDownfall(), 0.0D),
				"Chilli-Chocolate Crags are not a hot, dry Nether biome");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.HOT,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.MOUNTAIN,
				BiomeDictionary.Type.WASTELAND)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Chilli-Chocolate Crags lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				crags.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				crags.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.CHILLI_CHOCOLATE_CRAGS_RUMBLE
										.getId())
						&& close(ambience.getTickChance(), 0.0015D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.DRIPPING_LAVA,
				"Chilli-Chocolate Crags lost their visible heat atmosphere");

		assertSpawn(helper, crags, EntityType.ZOMBIFIED_PIGLIN,
				CakeWorldEntities.STALE_FUDGE_FOLK.get(), 100, 4, 4);
		assertSpawn(helper, crags, EntityType.GHAST,
				CakeWorldEntities.MALLOW_FLOATER.get(), 50, 4, 4);
		assertSpawn(helper, crags, EntityType.MAGMA_CUBE,
				CakeWorldEntities.HOT_FUDGE_BLOB.get(), 2, 4, 4);
		assertSpawn(helper, crags, EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER.get(), 1, 4, 4);
		assertSpawn(helper, crags, EntityType.PIGLIN,
				CakeWorldEntities.FUDGE_FOLK.get(), 15, 4, 4);
		assertSpawn(helper, crags, EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER.get(), 60, 1, 2);
		assertSpawn(helper, crags, EntityType.HOGLIN,
				CakeWorldEntities.FUDGE_BOAR.get(), 12, 2, 3);
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += crags.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, totalSpawns == 7
						&& findSpawn(crags,
								CakeWorldEntities.FUDGE_BRUTE.get()) == null,
				"Crags gained an undocumented open role or leaked Fudge Brutes: "
						+ totalSpawns);

		BlockState rock = CakeWorldBlocks.CHILLI_CHOCOLATE_ROCK.get()
				.defaultBlockState();
		TagKey<Block> edibleHosts = TagKey.create(
				Registry.BLOCK_REGISTRY, id("edible_ore_hosts"));
		require(helper,
				CakeWorldBlocks.CHILLI_CHOCOLATE_ROCK.get().getClass()
						== Block.class
						&& rock.is(BlockTags.MINEABLE_WITH_PICKAXE)
						&& rock.is(BlockTags.BASE_STONE_NETHER)
						&& rock.is(edibleHosts)
						&& !rock.is(Blocks.MAGMA_BLOCK),
				"Chilli Chocolate Rock lost its ordinary solid host contract");

		for (String tag : List.of(
				"minecraft:has_structure/bastion_remnant",
				"minecraft:has_structure/nether_fortress",
				"minecraft:has_structure/nether_fossil",
				"cakeworld:has_structure/burnt_toffee_foundry",
				"cakeworld:has_structure/liquorice_fortress",
				"cakeworld:has_structure/burnt_sugar_arch",
				"cakeworld:has_structure/rock_candy_fossil")) {
			require(helper, holder.is(TagKey.create(
					Registry.BIOME_REGISTRY, new ResourceLocation(tag))),
					"Crags lost structure-bound progression role " + tag);
		}
		assertFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione006")
	public static void cragfireProspectIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CragfireProspectFeature.placedFeature();
		Biome crags = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== CragfireProspectFeature.FEATURE
						&& placed.value().placement().size() == 4
						&& placed.value().placement().get(0)
								instanceof RarityFilter
						&& placed.value().placement().get(1)
								instanceof InSquarePlacement
						&& placed.value().placement().get(2)
								instanceof HeightRangePlacement
						&& placed.value().placement().get(3)
								instanceof BiomeFilter
						&& CragfireProspectFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& CragfireProspectFeature.MIN_NATURAL_SUPPORTS == 33
						&& hasPlacedFeature(crags, placed),
				"Cragfire Prospect lost its bounded Crags placement chain");

		BlockPos helperPos = helper.absolutePos(new BlockPos(8, 5, 8));
		BlockPos centre = new BlockPos(helperPos.getX(), 64,
				helperPos.getZ());
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre, 81);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					CragfireProspectFeature.hasSafeSite(
							level, centre, rotation)
							&& CragfireProspectFeature.buildAt(
									level, centre, rotation),
					"Cragfire Prospect rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false),
					"Cragfire Prospect plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Cragfire Prospect created an entity or block entity");
		}

		prepareSite(level, centre, 81);
		level.setBlock(CragfireProspectFeature.local(
				centre, Rotation.NONE, 4, 2, 4),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !CragfireProspectFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cragfire Prospect replaced an existing fluid");
		prepareSite(level, centre, 81);
		level.setBlock(CragfireProspectFeature.local(
				centre, Rotation.NONE, -4, 2, -4),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !CragfireProspectFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cragfire Prospect replaced a block entity");
		prepareSite(level, centre, 81);
		level.setBlock(CragfireProspectFeature.local(
				centre, Rotation.NONE, 1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !CragfireProspectFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cragfire Prospect replaced an authored solid");
		prepareSite(level, centre, 32);
		require(helper, !CragfireProspectFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Cragfire Prospect ignored its natural support threshold");
		require(helper, !CragfireProspectFeature.fitsWithinChunk(
				new BlockPos(new ChunkPos(centre).getMinBlockX(),
						centre.getY(), new ChunkPos(centre).getMinBlockZ()),
				Rotation.NONE, new ChunkPos(centre)),
				"Cragfire Prospect crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione006world",
			timeoutTicks = 24000)
	public static void focusedNaturalChilliChocolateCragsAudit(
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
				"Could not locate Chilli-Chocolate Crags within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundProspect found = findProspect(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Cragfire Prospect within 1,089 chunks of "
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
			PlanAudit plan = inspectPlan(
					level, found.centre(), found.rotation());
			BlockPos sentinel = CragfireProspectFeature.local(
					found.centre(), found.rotation(), -3, 1, -2);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(
					level, foundChunk, 4, found.centre());
			LOGGER.info("Chilli-Chocolate Crags audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, chilliRock={}, fudgeRock={}, hotFudge={}, netherQuartz={}, netherGold={}, ancientDebris={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.chilliRock(),
					audit.fudgeRock(), audit.hotFudge(),
					audit.netherQuartz(), audit.netherGold(),
					audit.ancientDebris(), plan,
					brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.chilliRock() > 0
							&& audit.fudgeRock() > 0
							&& audit.netherQuartz() + audit.netherGold() > 0
							&& plan.complete(brickSentinel),
					"Natural Crags lost terrain, progression ores or their complete Prospect: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel)
						.is(Blocks.BRICKS),
						"Could not seed the Cragfire Prospect reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertFood(
			GameTestHelper helper, ServerLevel level) {
		FoodProperties food = CakeWorldItems.CRAGFIRE_TRUFFLE.get()
				.getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("cragfire_truffle")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 7
						&& close(food.getSaturationModifier(), 0.8D)
						&& hasEffect(food, MobEffects.FIRE_RESISTANCE, 300)
						&& hasEffect(food, MobEffects.DIG_SPEED, 200)
						&& recipe != null
						&& recipe.getType() == RecipeType.CRAFTING
						&& recipe.getIngredients().size() == 3
						&& ingredient(recipe, new ItemStack(
								CakeWorldItems.COCOA_TRUFFLE.get()))
						&& ingredient(recipe, new ItemStack(
								CakeWorldItems.FUDGE_SQUARE.get()))
						&& ingredient(recipe, new ItemStack(
								CakeWorldItems.CINNAMON_STICK.get()))
						&& recipe.getResultItem().is(
								CakeWorldItems.CRAGFIRE_TRUFFLE.get())
						&& recipe.getResultItem().getCount() == 2,
				"Cragfire Truffle lost its nutrition, effects or recipe");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 38,
				"Chilli-Chocolate Crags require provider revision 38");
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
			JsonArray depositBiomes = profile
					.getAsJsonObject("fluid_deposits")
					.getAsJsonObject("cakeworld:fluid_deposit/hot_fudge")
					.getAsJsonObject("dimensions")
					.getAsJsonObject("minecraft:the_nether")
					.getAsJsonArray("biome_ids");
			List<String> order = nether.getAsJsonObject("biomes")
					.entrySet().stream().map(Map.Entry::getKey).toList();
			require(helper, geomes.size() == 1
							&& close(geomes.get("cakeworld:fudge_mantle")
									.getAsDouble(), 28.0D)
							&& close(placement.get("weight").getAsDouble(), 0.65D)
							&& strings(placement.getAsJsonArray("similar_biomes"))
									.equals(Set.of("minecraft:nether_wastes"))
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
							&& "cakeworld:chilli_chocolate_rock".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:fudge_rock".equals(
									surface.get("filler_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5
							&& strings(depositBiomes).equals(Set.of(
									"cakeworld:fudge_wastes",
									"cakeworld:burnt_toffee_deltas",
									"cakeworld:cinnamon_ember_groves",
									"cakeworld:black_liquorice_labyrinths",
									"cakeworld:treacle_soul_valleys",
									BIOME_ID.toString()))
							&& order.indexOf("cakeworld:fudge_wastes")
									< order.indexOf(BIOME_ID.toString())
							&& order.indexOf("cakeworld:treacle_soul_valleys")
									< order.indexOf(BIOME_ID.toString()),
					template + " lost its Crags provider contract");
			if (firstPalette == null) {
				firstPalette = nether;
			} else {
				require(helper, firstPalette.equals(nether),
						"Normal and BaseMetals Nether palettes diverged");
			}
		}
	}

	private static void prepareSite(
			ServerLevel level, BlockPos centre, int supports) {
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-5, -1, -5), centre.offset(6, 4, 6)))
				.forEach(Entity::discard);
		int remaining = supports;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				for (int y = -1; y <= 3; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				if (Math.abs(x) <= 4 && Math.abs(z) <= 4
						&& remaining-- > 0) {
					level.setBlock(centre.offset(x, -1, z),
							CakeWorldBlocks.CHILLI_CHOCOLATE_ROCK.get()
									.defaultBlockState(), 2);
				}
			}
		}
	}

	private static PlanAudit inspectPlan(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		int chilli = 0;
		int wafer = 0;
		int marshmallow = 0;
		int magma = 0;
		int glass = 0;
		int pillars = 0;
		int fudgeGold = 0;
		int vanillaQuartz = 0;
		int ancientNougat = 0;
		int coolingRacks = 0;
		int mixingBowls = 0;
		int sentinelBricks = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -1; y <= 2; y++) {
					BlockState state = level.getBlockState(
							CragfireProspectFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.CHILLI_CHOCOLATE_ROCK.get())) {
						chilli++;
					} else if (state.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
						wafer++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(Blocks.MAGMA_BLOCK)) {
						magma++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(CakeWorldBlocks.CANDY_CANE_PILLAR.get())) {
						pillars++;
					} else if (state.is(CakeWorldBlocks.FUDGE_GOLD.get())) {
						fudgeGold++;
					} else if (state.is(CakeWorldBlocks.VANILLA_QUARTZ.get())) {
						vanillaQuartz++;
					} else if (state.is(CakeWorldBlocks.ANCIENT_NOUGAT.get())) {
						ancientNougat++;
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
		return new PlanAudit(chilli, wafer, marshmallow, magma,
				glass, pillars, fudgeGold, vanillaQuartz, ancientNougat,
				coolingRacks, mixingBowls, sentinelBricks);
	}

	private static FoundProspect findProspect(
			ServerLevel level, ChunkPos anchor, int radius) {
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
							for (int y = CragfireProspectFeature.MIN_Y;
									y <= CragfireProspectFeature.MAX_Y + 2; y++) {
								cursor.set(x, y, z);
								if (!level.getBlockState(cursor).is(
										CakeWorldBlocks.MIXING_BOWL.get())) {
									continue;
								}
								for (Rotation rotation : ROTATIONS) {
									BlockPos markerOffset = new BlockPos(
											-2, 1, 3).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(markerOffset);
									PlanAudit plan = inspectPlan(
											level, centre, rotation);
									if (plan.identifiesProspect()) {
										return new FoundProspect(centre, rotation);
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

	private static NaturalAudit audit(ServerLevel level,
			ChunkPos anchor, int radius, BlockPos prospect) {
		int biomeSamples = 0;
		int chilliRock = 0;
		int fudgeRock = 0;
		int hotFudge = 0;
		int netherQuartz = 0;
		int netherGold = 0;
		int ancientDebris = 0;
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
							if (state.is(CakeWorldBlocks
									.CHILLI_CHOCOLATE_ROCK.get())) {
								if (!nearProspect(cursor, prospect)) {
									chilliRock++;
								}
							} else if (state.is(CakeWorldBlocks.FUDGE_ROCK.get())) {
								fudgeRock++;
							} else if (state.is(CakeWorldFluids.HOT_FUDGE_BLOCK.get())) {
								hotFudge++;
							} else if (state.is(Blocks.NETHER_QUARTZ_ORE)) {
								netherQuartz++;
							} else if (state.is(Blocks.NETHER_GOLD_ORE)) {
								netherGold++;
							} else if (state.is(Blocks.ANCIENT_DEBRIS)) {
								ancientDebris++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, chilliRock, fudgeRock,
				hotFudge, netherQuartz, netherGold, ancientDebris);
	}

	private static boolean nearProspect(BlockPos position, BlockPos centre) {
		return Math.abs(position.getX() - centre.getX()) <= 4
				&& position.getY() >= centre.getY() - 1
				&& position.getY() <= centre.getY() + 2
				&& Math.abs(position.getZ() - centre.getZ()) <= 4;
	}

	private static Set<Integer> entityIds(
			ServerLevel level, BlockPos centre) {
		Set<Integer> ids = new HashSet<>();
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-4, -1, -4), centre.offset(5, 3, 5)))
				.forEach(entity -> ids.add(entity.getId()));
		return ids;
	}

	private static int countBlockEntities(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -1; y <= 2; y++) {
					if (level.getBlockEntity(centre.offset(x, y, z)) != null) {
						count++;
					}
				}
			}
		}
		return count;
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
				"Crags lost replacement " + replacement.getRegistryName()
						+ " for " + vanilla.getRegistryName());
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

	private static boolean hasPlacedFeature(
			Biome biome, Holder<PlacedFeature> expected) {
		int step = GenerationStep.Decoration.TOP_LAYER_MODIFICATION.ordinal();
		return biome != null
				&& biome.getGenerationSettings().features().size() > step
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
				ChilliChocolateCragsGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int chilli, int wafer, int marshmallow,
			int magma, int glass, int pillars, int fudgeGold,
			int vanillaQuartz, int ancientNougat, int coolingRacks,
			int mixingBowls, int sentinelBricks) {
		private boolean identifiesProspect() {
			return chilli >= 120 && wafer >= 10 && marshmallow >= 3
					&& magma == 1 && glass >= 6 && pillars >= 3
					&& vanillaQuartz == 1 && ancientNougat == 1
					&& coolingRacks == 1 && mixingBowls == 1
					&& fudgeGold + sentinelBricks == 1;
		}

		private boolean complete(boolean brickSentinel) {
			return chilli == 145 && wafer == 12 && marshmallow == 4
					&& magma == 1 && glass == 8 && pillars == 4
					&& fudgeGold == (brickSentinel ? 0 : 1)
					&& vanillaQuartz == 1 && ancientNougat == 1
					&& coolingRacks == 1 && mixingBowls == 1
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundProspect(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int chilliRock,
			int fudgeRock, int hotFudge, int netherQuartz,
			int netherGold, int ancientDebris) {
	}
}
