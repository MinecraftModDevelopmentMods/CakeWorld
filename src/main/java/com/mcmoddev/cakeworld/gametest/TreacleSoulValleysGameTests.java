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
import com.mcmoddev.cakeworld.world.TreacleSoulFlatFeature;
import com.mcmoddev.cakeworld.world.WispLightCausewayFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

import org.slf4j.Logger;

/** Contract proof for BIO-NE-005, BLK-019, FOOD-010 and STRUCT-041. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class TreacleSoulValleysGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("treacle_soul_valleys");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final ResourceKey<PlacedFeature> ORE_SOUL_SAND =
			ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
					new ResourceLocation("minecraft", "ore_soul_sand"));
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};

	private TreacleSoulValleysGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bione005")
	public static void valleysHaveAtmosphereFoodEcologyAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome biome = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, biome != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.NETHER
						&& close(biome.getBaseTemperature(), 2.0D)
						&& close(biome.getDownfall(), 0.0D),
				"Treacle Soul Valleys are not a hot, dry Soul-Sand-Valley copy");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.HOT,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.SPARSE,
				BiomeDictionary.Type.SPOOKY,
				BiomeDictionary.Type.WASTELAND)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Treacle Soul Valleys lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				biome.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				biome.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds
										.TREACLE_SOUL_VALLEYS_MURMUR.getId())
						&& close(ambience.getTickChance(), 0.0015D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.SOUL_FIRE_FLAME,
				"Treacle Soul Valleys lost their murmuring sugar-wisp atmosphere");

		assertSpawn(helper, biome, EntityType.SKELETON,
				CakeWorldEntities.CANDY_CANE_ARCHER.get(), 20, 5, 5);
		assertSpawn(helper, biome, EntityType.GHAST,
				CakeWorldEntities.MALLOW_FLOATER.get(), 50, 4, 4);
		assertSpawn(helper, biome, EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER.get(), 1, 4, 4);
		assertSpawn(helper, biome, EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER.get(), 60, 1, 2);
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += biome.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, totalSpawns == 4,
				"Treacle Soul Valleys gained an undocumented creature role: "
						+ totalSpawns);

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
					"Treacle Soul Valleys lost progression tag " + tag);
		}

		BlockState crust = CakeWorldBlocks.TREACLE_SOUL_CRUST.get()
				.defaultBlockState();
		require(helper, close(CakeWorldBlocks.TREACLE_SOUL_CRUST.get()
						.getSpeedFactor(), 0.78D)
						&& crust.is(BlockTags.BASE_STONE_NETHER)
						&& crust.is(BlockTags.MINEABLE_WITH_PICKAXE)
						&& crust.is(TagKey.create(Registry.BLOCK_REGISTRY,
								id("edible_ore_hosts"))),
				"Treacle Soul Crust lost its gentle slowdown or geology-host contract");
		for (HolderSet<PlacedFeature> step
				: biome.getGenerationSettings().features()) {
			require(helper, step.stream().noneMatch(
					feature -> feature.is(ORE_SOUL_SAND)),
					"Treacle Soul Valleys retained vanilla Soul-Sand ore terrain");
		}
		assertFood(helper, level);
		assertFeatures(helper, biome);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione005")
	public static void flatsAndCausewayAreContainedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos helperPos = helper.absolutePos(new BlockPos(8, 5, 8));
		BlockPos centre = new BlockPos(helperPos.getX(), 64, helperPos.getZ());
		for (Rotation rotation : ROTATIONS) {
			prepareCauseway(level, centre, 81);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					WispLightCausewayFeature.hasSafeSite(
							level, centre, rotation)
							&& WispLightCausewayFeature.buildAt(
									level, centre, rotation),
					"Wisp-Light Causeway rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false),
					"Wisp-Light Causeway plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Wisp-Light Causeway created an entity or block entity");
		}

		prepareCauseway(level, centre, 81);
		level.setBlock(WispLightCausewayFeature.local(
				centre, Rotation.NONE, 4, 2, 4),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !WispLightCausewayFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Wisp-Light Causeway replaced an existing fluid");
		prepareCauseway(level, centre, 81);
		level.setBlock(WispLightCausewayFeature.local(
				centre, Rotation.NONE, -4, 2, -4),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !WispLightCausewayFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Wisp-Light Causeway replaced a block entity");
		prepareCauseway(level, centre, 81);
		level.setBlock(WispLightCausewayFeature.local(
				centre, Rotation.NONE, 1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !WispLightCausewayFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Wisp-Light Causeway replaced an authored solid");
		prepareCauseway(level, centre, 32);
		require(helper, !WispLightCausewayFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Wisp-Light Causeway ignored its natural support threshold");
		require(helper, !WispLightCausewayFeature.fitsWithinChunk(
				new BlockPos(new ChunkPos(centre).getMinBlockX(),
						centre.getY(), new ChunkPos(centre).getMinBlockZ()),
				Rotation.NONE, new ChunkPos(centre)),
				"Wisp-Light Causeway crossed its generating chunk");

		prepareFlat(level, centre);
		require(helper, TreacleSoulFlatFeature.hasSafeSite(level, centre)
						&& TreacleSoulFlatFeature.buildAt(level, centre),
				"Treacle Soul Flat rejected a safe fixture");
		int rim = 0;
		int syrup = 0;
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				BlockState state = level.getBlockState(centre.offset(x, 0, z));
				if (state.is(CakeWorldBlocks.TREACLE_SOUL_CRUST.get())) {
					rim++;
				} else if (state.is(CakeWorldFluids.SYRUP_BLOCK.get())) {
					syrup++;
				}
			}
		}
		require(helper, rim == 16 && syrup == 9,
				"Treacle Soul Flat lost its exact contained 16/9 rim/source plan");
		prepareFlat(level, centre);
		level.setBlock(centre.offset(2, 0, 2),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !TreacleSoulFlatFeature.hasSafeSite(level, centre),
				"Treacle Soul Flat replaced a block entity");
		prepareFlat(level, centre);
		level.setBlock(centre.offset(0, -1, 0),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !TreacleSoulFlatFeature.hasSafeSite(level, centre),
				"Treacle Soul Flat accepted a non-terrain support");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione005world",
			timeoutTicks = 24000)
	public static void focusedNaturalTreacleSoulValleysAudit(
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
				"Could not locate Treacle Soul Valleys within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundCauseway found = findCauseway(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Wisp-Light Causeway within 1,089 chunks of "
						+ anchor);
		ChunkPos foundChunk = new ChunkPos(found.centre());
		for (int chunkX = foundChunk.x - 2;
				chunkX <= foundChunk.x + 2; chunkX++) {
			for (int chunkZ = foundChunk.z - 2;
					chunkZ <= foundChunk.z + 2; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
		level.setChunkForced(foundChunk.x, foundChunk.z, true);
		helper.runAfterDelay(40, () -> {
			PlanAudit plan = inspectPlan(
					level, found.centre(), found.rotation());
			BlockPos sentinel = WispLightCausewayFeature.local(
					found.centre(), found.rotation(), -3, 2, -3);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 2, found.centre());
			LOGGER.info("Treacle Soul Valleys audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, crust={}, fudgeRock={}, independentSyrup={}, totalSyrup={}, hotFudge={}, literalSoulTerrain={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.crust(), audit.fudgeRock(),
					audit.independentSyrup(), audit.totalSyrup(),
					audit.hotFudge(), audit.literalSoulTerrain(),
					plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.crust() > 0
							&& audit.fudgeRock() > 0
							&& audit.independentSyrup() > 0
							&& audit.literalSoulTerrain() == 0
							&& plan.complete(brickSentinel),
					"Natural Treacle Soul Valleys lost edible terrain, independent flats or their complete causeway: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel)
						.is(Blocks.BRICKS),
						"Could not seed the Wisp-Light Causeway reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertFood(GameTestHelper helper, ServerLevel level) {
		FoodProperties food = CakeWorldItems.WISP_LIGHT_TOFFEE.get()
				.getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("wisp_light_toffee")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 7
						&& close(food.getSaturationModifier(), 0.75D)
						&& hasEffect(food, MobEffects.SLOW_FALLING, 240)
						&& hasEffect(food, MobEffects.FIRE_RESISTANCE, 200)
						&& recipe != null
						&& recipe.getType() == RecipeType.CRAFTING
						&& recipe.getIngredients().size() == 3
						&& ingredient(recipe, new ItemStack(
								CakeWorldFluids.SYRUP_BUCKET.get()))
						&& ingredient(recipe, new ItemStack(
								CakeWorldBlocks.MARSHMALLOW.get()))
						&& ingredient(recipe, new ItemStack(Items.SUGAR))
						&& recipe.getResultItem().is(
								CakeWorldItems.WISP_LIGHT_TOFFEE.get())
						&& recipe.getResultItem().getCount() == 2
						&& CakeWorldFluids.SYRUP_BUCKET.get()
								.getCraftingRemainingItem() == Items.BUCKET,
				"Wisp-Light Toffee lost its nutrition, rescue effects, recipe or bucket return");
	}

	private static void assertFeatures(GameTestHelper helper, Biome biome) {
		Holder<PlacedFeature> flats = TreacleSoulFlatFeature.placedFeature();
		Holder<PlacedFeature> causeway = WispLightCausewayFeature.placedFeature();
		require(helper, flats != null
						&& flats.value().feature().value().feature()
								== TreacleSoulFlatFeature.FEATURE
						&& flats.value().placement().size() == 4
						&& flats.value().placement().get(0)
								instanceof CountPlacement
						&& flats.value().placement().get(1)
								instanceof InSquarePlacement
						&& flats.value().placement().get(2)
								instanceof HeightRangePlacement
						&& flats.value().placement().get(3)
								instanceof BiomeFilter
						&& TreacleSoulFlatFeature.ATTEMPTS_PER_CHUNK == 4
						&& hasPlacedFeature(biome, flats,
								GenerationStep.Decoration.TOP_LAYER_MODIFICATION),
				"Treacle Soul Valleys lost their bounded syrup-flat feature");
		require(helper, causeway != null
						&& causeway.value().feature().value().feature()
								== WispLightCausewayFeature.FEATURE
						&& causeway.value().placement().size() == 4
						&& causeway.value().placement().get(0)
								instanceof RarityFilter
						&& causeway.value().placement().get(1)
								instanceof InSquarePlacement
						&& causeway.value().placement().get(2)
								instanceof HeightRangePlacement
						&& causeway.value().placement().get(3)
								instanceof BiomeFilter
						&& WispLightCausewayFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& WispLightCausewayFeature.MIN_NATURAL_SUPPORTS == 33
						&& hasPlacedFeature(biome, causeway,
								GenerationStep.Decoration.TOP_LAYER_MODIFICATION),
				"Treacle Soul Valleys lost their bounded Wisp-Light Causeway");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 36,
				"Treacle Soul Valleys require provider revision 36");
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
									.getAsDouble(), 14.0D)
							&& close(placement.get("weight").getAsDouble(), 1.2D)
							&& strings(placement.getAsJsonArray("similar_biomes"))
									.equals(Set.of("minecraft:soul_sand_valley"))
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
							&& "cakeworld:treacle_soul_crust".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:fudge_rock".equals(
									surface.get("filler_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5
							&& strings(depositBiomes).equals(Set.of(
									"cakeworld:fudge_wastes",
									"cakeworld:burnt_toffee_deltas",
									"cakeworld:cinnamon_ember_groves",
									"cakeworld:black_liquorice_labyrinths",
									BIOME_ID.toString(),
									"cakeworld:chilli_chocolate_crags",
									"cakeworld:molten_marshmallow_calderas"))
							&& order.indexOf("cakeworld:black_liquorice_labyrinths")
									< order.indexOf(BIOME_ID.toString()),
					template + " lost its Treacle Soul Valley provider contract");
			if (firstPalette == null) {
				firstPalette = nether;
			} else {
				require(helper, firstPalette.equals(nether),
						"Normal and BaseMetals Nether palettes diverged");
			}
		}
	}

	private static void prepareCauseway(
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
							CakeWorldBlocks.FUDGE_ROCK.get()
									.defaultBlockState(), 2);
				}
			}
		}
	}

	private static void prepareFlat(ServerLevel level, BlockPos centre) {
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = -1; y <= 1; y++) {
					level.setBlock(centre.offset(x, y, z),
							y == -1
									? CakeWorldBlocks.FUDGE_ROCK.get()
											.defaultBlockState()
									: Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
	}

	private static PlanAudit inspectPlan(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		int fudgeRock = 0;
		int crust = 0;
		int syrup = 0;
		int wafer = 0;
		int marshmallow = 0;
		int glass = 0;
		int pillars = 0;
		int mint = 0;
		int coolingRacks = 0;
		int mixingBowls = 0;
		int sentinelBricks = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -1; y <= 2; y++) {
					BlockState state = level.getBlockState(
							WispLightCausewayFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.FUDGE_ROCK.get())) {
						fudgeRock++;
					} else if (state.is(CakeWorldBlocks.TREACLE_SOUL_CRUST.get())) {
						crust++;
					} else if (state.is(CakeWorldFluids.SYRUP_BLOCK.get())) {
						syrup++;
					} else if (state.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
						wafer++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(CakeWorldBlocks.CANDY_CANE_PILLAR.get())) {
						pillars++;
					} else if (state.is(CakeWorldBlocks.MINT_CRYSTAL.get())) {
						mint++;
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
		return new PlanAudit(fudgeRock, crust, syrup, wafer,
				marshmallow, glass, pillars, mint,
				coolingRacks, mixingBowls, sentinelBricks);
	}

	private static FoundCauseway findCauseway(ServerLevel level,
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
							for (int y = WispLightCausewayFeature.MIN_Y;
									y <= WispLightCausewayFeature.MAX_Y + 1; y++) {
								cursor.set(x, y, z);
								if (!level.getBlockState(cursor).is(
										CakeWorldBlocks.MIXING_BOWL.get())) {
									continue;
								}
								for (Rotation rotation : ROTATIONS) {
									BlockPos markerOffset =
											new BlockPos(2, 1, -4)
													.rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(markerOffset);
									PlanAudit plan = inspectPlan(
											level, centre, rotation);
									if (plan.identifiesCauseway()) {
										return new FoundCauseway(centre, rotation);
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
			ChunkPos anchor, int radius, BlockPos causeway) {
		int biomeSamples = 0;
		int crust = 0;
		int fudgeRock = 0;
		int independentSyrup = 0;
		int totalSyrup = 0;
		int hotFudge = 0;
		int literalSoulTerrain = 0;
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
							if (state.is(CakeWorldBlocks.TREACLE_SOUL_CRUST.get())) {
								crust++;
							} else if (state.is(CakeWorldBlocks.FUDGE_ROCK.get())) {
								fudgeRock++;
							} else if (state.is(CakeWorldFluids.SYRUP_BLOCK.get())) {
								totalSyrup++;
								if (!nearCauseway(cursor, causeway)) {
									independentSyrup++;
								}
							} else if (state.is(CakeWorldFluids.HOT_FUDGE_BLOCK.get())) {
								hotFudge++;
							} else if (state.is(Blocks.SOUL_SAND)
									|| state.is(Blocks.SOUL_SOIL)) {
								literalSoulTerrain++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, crust, fudgeRock,
				independentSyrup, totalSyrup, hotFudge,
				literalSoulTerrain);
	}

	private static boolean nearCauseway(BlockPos position, BlockPos centre) {
		return Math.abs(position.getX() - centre.getX()) <= 4
				&& position.getY() >= centre.getY() - 1
				&& position.getY() <= centre.getY() + 2
				&& Math.abs(position.getZ() - centre.getZ()) <= 4;
	}

	private static Set<Integer> entityIds(ServerLevel level, BlockPos centre) {
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
				"Treacle Soul Valleys lost replacement "
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
			Holder<PlacedFeature> expected,
			GenerationStep.Decoration decoration) {
		int step = decoration.ordinal();
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
				TreacleSoulValleysGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int fudgeRock, int crust, int syrup,
			int wafer, int marshmallow, int glass, int pillars, int mint,
			int coolingRacks, int mixingBowls, int sentinelBricks) {
		private boolean identifiesCauseway() {
			return fudgeRock >= 60 && crust >= 20 && syrup >= 30
					&& wafer >= 5 && marshmallow >= 1 && glass >= 6
					&& pillars >= 3 && mint >= 3
					&& coolingRacks == 1 && mixingBowls == 1
					&& sentinelBricks <= 1;
		}

		private boolean complete(boolean brickSentinel) {
			return fudgeRock == 81 && crust == 30 && syrup == 42
					&& wafer == 7 && marshmallow == 2 && glass == 8
					&& pillars == 4
					&& mint == (brickSentinel ? 3 : 4)
					&& coolingRacks == 1 && mixingBowls == 1
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundCauseway(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int crust,
			int fudgeRock, int independentSyrup, int totalSyrup,
			int hotFudge, int literalSoulTerrain) {
	}
}
