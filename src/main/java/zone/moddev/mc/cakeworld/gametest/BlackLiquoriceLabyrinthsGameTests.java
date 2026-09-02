package zone.moddev.mc.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.HashSet;
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
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.BlackLiquoriceLoopFeature;
import zone.moddev.mc.cakeworld.world.BlackLiquoriceTanglePatchFeature;
import zone.moddev.mc.cakeworld.world.BurntToffeeDeltasPalette;

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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/** Contract proof for the first complete BIO-NE-004 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class BlackLiquoriceLabyrinthsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("black_liquorice_labyrinths");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};

	private BlackLiquoriceLabyrinthsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bione004")
	public static void labyrinthsHaveAtmosphereFoodEcologyAndProfile(
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
				"Black-Liquorice Labyrinths are not a hot, dry Warped-Forest copy");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.HOT,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.FOREST,
				BiomeDictionary.Type.DENSE,
				BiomeDictionary.Type.SPOOKY)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Black-Liquorice Labyrinths lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				biome.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				biome.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds
										.BLACK_LIQUORICE_LABYRINTHS_CREAK.getId())
						&& close(ambience.getTickChance(), 0.0015D)
						&& particle != null
						&& particle.getOptions().getType() == ParticleTypes.ASH,
				"Black-Liquorice Labyrinths lost their creaking ash atmosphere");

		assertSpawn(helper, biome, EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER.get(), 1, 4, 4);
		assertSpawn(helper, biome, EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER.get(), 60, 1, 2);
		MobSpawnSettings.SpawnerData weaver = findSpawn(
				biome, CakeWorldEntities.LIQUORICE_WEAVER.get());
		require(helper, weaver != null
						&& weaver.getWeight().asInt() == 12
						&& weaver.minCount == 2
						&& weaver.maxCount == 4
						&& findSpawn(biome,
								CakeWorldEntities.BURNT_CANDY_KNIGHT.get()) == null,
				"Labyrinth ecology lost its deliberate Weaver or gained an open-biome Knight");
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += biome.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, totalSpawns == 3,
				"Black-Liquorice Labyrinths gained an undocumented creature role: "
						+ totalSpawns);

		for (String tag : List.of(
				"minecraft:has_structure/bastion_remnant",
				"minecraft:has_structure/nether_fortress",
				"cakeworld:has_structure/burnt_toffee_foundry",
				"cakeworld:has_structure/liquorice_fortress",
				"cakeworld:has_structure/burnt_sugar_arch")) {
			require(helper, holder.is(TagKey.create(
					Registry.BIOME_REGISTRY, new ResourceLocation(tag))),
					"Black-Liquorice Labyrinths lost progression tag " + tag);
		}

		BlockState stone = CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()
				.defaultBlockState();
		require(helper, stone.is(BlockTags.BASE_STONE_NETHER)
						&& stone.is(BlockTags.MINEABLE_WITH_PICKAXE)
						&& stone.is(TagKey.create(Registry.BLOCK_REGISTRY,
								id("edible_ore_hosts")))
						&& CakeWorldBlocks.BLACK_LIQUORICE_TANGLE.get()
								.defaultBlockState()
								.getCollisionShape(level, BlockPos.ZERO).isEmpty(),
				"Labyrinth terrain lost its Nether host, mining or soft-tangle contract");
		assertFoodAndRecipes(helper, level);
		assertPalette(helper);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione004")
	public static void loopLabyrinthIsReadableBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				BlackLiquoriceLoopFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== BlackLiquoriceLoopFeature.FEATURE
						&& BlackLiquoriceLoopFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& BlackLiquoriceLoopFeature.MIN_SOLID_SUPPORTS == 33,
				"Black-Liquorice Loop registration or bounds changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0) instanceof RarityFilter
						&& modifiers.get(1) instanceof InSquarePlacement
						&& modifiers.get(2) instanceof HeightRangePlacement
						&& modifiers.get(3) instanceof BiomeFilter,
				"Black-Liquorice Loop lost its rare bounded Nether chain");
		Biome biome = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, hasPlacedFeature(biome, placed,
				GenerationStep.Decoration.TOP_LAYER_MODIFICATION),
				"Black-Liquorice Labyrinths did not receive their loop feature");
		Holder<PlacedFeature> tangles =
				BlackLiquoriceTanglePatchFeature.placedFeature();
		require(helper, tangles != null
						&& tangles.value().feature().value().feature()
								== BlackLiquoriceTanglePatchFeature.FEATURE
						&& tangles.value().placement().size() == 4
						&& tangles.value().placement().get(0)
								instanceof CountPlacement
						&& hasPlacedFeature(biome, tangles,
								GenerationStep.Decoration.VEGETAL_DECORATION),
				"Black-Liquorice Labyrinths lost their natural tangle patches");
		require(helper, pathExists(false)
						&& pathExists(true)
						&& BlackLiquoriceLoopFeature.shortcuts().length == 2,
				"The ordinary maze route or its two visible shortcuts became unreadable");

		BlockPos helperPos = helper.absolutePos(new BlockPos(8, 5, 8));
		BlockPos centre = new BlockPos(helperPos.getX(), 64, helperPos.getZ());
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre, 81);
			Set<Integer> entityIdsBefore = entityIds(level, centre);
			require(helper,
					BlackLiquoriceLoopFeature.hasSafeSite(
							level, centre, rotation)
							&& BlackLiquoriceLoopFeature.buildAt(
									level, centre, rotation),
					"Black-Liquorice Loop rejected safe rotation " + rotation);
			assertPlan(helper, level, centre, rotation, false);
			require(helper, entityIds(level, centre).equals(entityIdsBefore),
					"Black-Liquorice Loop created or removed an entity");
		}

		prepareSite(level, centre, 81);
		level.setBlock(BlackLiquoriceLoopFeature.local(
				centre, Rotation.NONE, 4, 2, 4),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !BlackLiquoriceLoopFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Black-Liquorice Loop replaced an existing fluid");
		prepareSite(level, centre, 81);
		level.setBlock(BlackLiquoriceLoopFeature.local(
				centre, Rotation.NONE, -4, 2, -4),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !BlackLiquoriceLoopFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Black-Liquorice Loop replaced a block entity or inventory");
		prepareSite(level, centre, 81);
		level.setBlock(BlackLiquoriceLoopFeature.local(
				centre, Rotation.NONE, 1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !BlackLiquoriceLoopFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Black-Liquorice Loop replaced a solid obstacle");
		prepareSite(level, centre, 32);
		require(helper, !BlackLiquoriceLoopFeature.hasSafeSite(
				level, centre, Rotation.NONE),
				"Black-Liquorice Loop ignored its support threshold");
		require(helper, !BlackLiquoriceLoopFeature.fitsWithinChunk(
				new BlockPos(new ChunkPos(centre).getMinBlockX(),
						centre.getY(), new ChunkPos(centre).getMinBlockZ()),
				Rotation.NONE, new ChunkPos(centre)),
				"Black-Liquorice Loop crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione004world",
			timeoutTicks = 24000)
	public static void focusedNaturalBlackLiquoriceLabyrinthsAudit(
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
				"Could not locate Black-Liquorice Labyrinths within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundLoop found = findLoop(level, anchor, 12);
		require(helper, found != null,
				"Could not find a natural Black-Liquorice Loop within 625 chunks of "
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
			BlockPos sentinel = found.centre();
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 2);
			LOGGER.info("Black-Liquorice Labyrinths audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, blackStone={}, fudgeRock={}, liquoriceRoots={}, tangles={}, mintCrystals={}, hotFudge={}, literalWarpedSource={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.blackStone(),
					audit.fudgeRock(), audit.liquoriceRoots(),
					audit.tangles(), audit.mintCrystals(),
					audit.hotFudge(), audit.literalWarpedSource(),
					plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.blackStone() > 0
							&& audit.fudgeRock() > 0
							&& audit.liquoriceRoots() > 0
							&& audit.tangles() > 0
							&& audit.mintCrystals() > 0
							&& audit.literalWarpedSource() == 0
							&& plan.complete(brickSentinel),
					"Natural Black-Liquorice Labyrinths lost edible terrain, converted vegetation or their complete loop: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel)
						.is(Blocks.BRICKS),
						"Could not seed the Black-Liquorice Loop reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertFoodAndRecipes(GameTestHelper helper,
			ServerLevel level) {
		FoodProperties food = CakeWorldItems.LABYRINTH_LIQUORICE_COIL.get()
				.getFoodProperties();
		Recipe<?> coil = level.getRecipeManager()
				.byKey(id("labyrinth_liquorice_coil")).orElse(null);
		Recipe<?> bricks = level.getRecipeManager()
				.byKey(id("liquorice_bricks_from_black_liquorice_stone"))
				.orElse(null);
		Recipe<?> cutting = level.getRecipeManager()
				.byKey(id("liquorice_bricks_stonecutting")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 7
						&& close(food.getSaturationModifier(), 0.75D)
						&& hasEffect(food, MobEffects.NIGHT_VISION, 300)
						&& hasEffect(food, MobEffects.FIRE_RESISTANCE, 200)
						&& coil != null
						&& coil.getType() == RecipeType.CRAFTING
						&& coil.getIngredients().size() == 3
						&& ingredient(coil,
								new ItemStack(CakeWorldItems.LIQUORICE_TWIST.get()))
						&& ingredient(coil,
								new ItemStack(CakeWorldItems.CINNAMON_STICK.get()))
						&& ingredient(coil,
								new ItemStack(CakeWorldItems.FUDGE_SQUARE.get()))
						&& coil.getResultItem().is(
								CakeWorldItems.LABYRINTH_LIQUORICE_COIL.get())
						&& coil.getResultItem().getCount() == 2
						&& bricks != null
						&& ingredient(bricks, new ItemStack(
								CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()))
						&& bricks.getResultItem().is(
								CakeWorldBlocks.LIQUORICE_BRICKS.get().asItem())
						&& bricks.getResultItem().getCount() == 4
						&& cutting != null
						&& cutting.getType() == RecipeType.STONECUTTING
						&& ingredient(cutting, new ItemStack(
								CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()))
						&& cutting.getResultItem().is(
								CakeWorldBlocks.LIQUORICE_BRICKS.get().asItem()),
				"Labyrinth Liquorice Coil or renewable structural recipes changed");
	}

	private static void assertPalette(GameTestHelper helper) {
		BlockState horizontalStem = Blocks.WARPED_STEM.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
		BlockState convertedStem = BurntToffeeDeltasPalette
				.blackLiquoriceConvertedState(horizontalStem);
		require(helper,
				BurntToffeeDeltasPalette.blackLiquoriceConvertedState(
						Blocks.WARPED_NYLIUM.defaultBlockState())
								.is(CakeWorldBlocks.BLACK_LIQUORICE_STONE.get())
						&& convertedStem.is(CakeWorldBlocks.LIQUORICE_ROOT.get())
						&& convertedStem.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.X
						&& BurntToffeeDeltasPalette.blackLiquoriceConvertedState(
								Blocks.WARPED_WART_BLOCK.defaultBlockState())
								.is(CakeWorldBlocks.LIQUORICE_ROOT.get())
						&& BurntToffeeDeltasPalette.blackLiquoriceConvertedState(
								Blocks.WARPED_ROOTS.defaultBlockState())
								.is(CakeWorldBlocks.BLACK_LIQUORICE_TANGLE.get())
						&& BurntToffeeDeltasPalette.blackLiquoriceConvertedState(
								Blocks.TWISTING_VINES.defaultBlockState())
								.is(CakeWorldBlocks.BLACK_LIQUORICE_TANGLE.get())
						&& BurntToffeeDeltasPalette.blackLiquoriceConvertedState(
								Blocks.SHROOMLIGHT.defaultBlockState())
								.is(CakeWorldBlocks.MINT_CRYSTAL.get())
						&& BurntToffeeDeltasPalette.blackLiquoriceConvertedState(
								CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()
										.defaultBlockState())
								.is(CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()),
				"Black-Liquorice copied-feature palette lost identity, axis or idempotence");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 35,
				"Black-Liquorice Labyrinths require provider revision 35");
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
									.getAsDouble(), 18.0D)
							&& close(placement.get("weight").getAsDouble(), 1.1D)
							&& strings(placement.getAsJsonArray("similar_biomes"))
									.equals(Set.of("minecraft:warped_forest"))
							&& strings(placement.getAsJsonArray(
									"required_similar_biomes")).isEmpty()
							&& close(placement.get("min_temperature").getAsDouble(), -2.0D)
							&& close(placement.get("max_temperature").getAsDouble(), 2.0D)
							&& close(placement.get("min_downfall").getAsDouble(), 0.0D)
							&& close(placement.get("max_downfall").getAsDouble(), 1.0D)
							&& "cakeworld:black_liquorice_stone".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:fudge_rock".equals(
									surface.get("filler_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5
							&& strings(depositBiomes).equals(Set.of(
									"cakeworld:fudge_wastes",
									"cakeworld:burnt_toffee_deltas",
									"cakeworld:cinnamon_ember_groves",
									BIOME_ID.toString(),
									"cakeworld:treacle_soul_valleys",
									"cakeworld:chilli_chocolate_crags",
									"cakeworld:molten_marshmallow_calderas"))
							&& order.indexOf("cakeworld:cinnamon_ember_groves")
									< order.indexOf(BIOME_ID.toString()),
					template + " lost its Labyrinth provider contract");
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
				new AABB(centre.offset(-5, -1, -5), centre.offset(6, 5, 6)))
				.forEach(Entity::discard);
		int remaining = supports;
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				for (int y = 0; y <= 4; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, -1, z),
						Blocks.AIR.defaultBlockState(), 2);
				if (Math.abs(x) <= 4 && Math.abs(z) <= 4
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
				"Black-Liquorice Loop palette changed for "
						+ rotation + ": " + plan);
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 4; y++) {
					require(helper, level.getBlockEntity(
							BlackLiquoriceLoopFeature.local(
									centre, rotation, x, y, z)) == null,
							"Black-Liquorice Loop created a block entity");
				}
			}
		}
	}

	private static PlanAudit inspectPlan(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		int stone = 0;
		int gilded = 0;
		int roots = 0;
		int marshmallow = 0;
		int glass = 0;
		int bricks = 0;
		int mint = 0;
		int sentinelBricks = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 4; y++) {
					BlockState state = level.getBlockState(
							BlackLiquoriceLoopFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.BLACK_LIQUORICE_STONE.get())) {
						stone++;
					} else if (state.is(CakeWorldBlocks.GILDED_BURNT_TOFFEE.get())) {
						gilded++;
					} else if (state.is(CakeWorldBlocks.LIQUORICE_ROOT.get())) {
						roots++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(CakeWorldBlocks.LIQUORICE_BRICKS.get())) {
						bricks++;
					} else if (state.is(CakeWorldBlocks.MINT_CRYSTAL.get())) {
						mint++;
					} else if (state.is(Blocks.BRICKS)) {
						sentinelBricks++;
					}
				}
			}
		}
		return new PlanAudit(stone, gilded, roots, marshmallow,
				glass, bricks, mint, sentinelBricks);
	}

	private static FoundLoop findLoop(ServerLevel level,
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
							for (int y = BlackLiquoriceLoopFeature.MIN_Y;
									y <= BlackLiquoriceLoopFeature.MAX_Y; y++) {
								cursor.set(x, y, z);
								BlockState state = level.getBlockState(cursor);
								if (!state.is(CakeWorldBlocks
										.GILDED_BURNT_TOFFEE.get())
										&& !state.is(Blocks.BRICKS)) {
									continue;
								}
								BlockPos centre = cursor.immutable();
								for (Rotation rotation : ROTATIONS) {
									PlanAudit plan = inspectPlan(
											level, centre, rotation);
									if (plan.identifiesLoop(
											state.is(Blocks.BRICKS))) {
										return new FoundLoop(centre, rotation);
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
			ChunkPos anchor, int radius) {
		int biomeSamples = 0;
		int blackStone = 0;
		int fudgeRock = 0;
		int liquoriceRoots = 0;
		int tangles = 0;
		int mintCrystals = 0;
		int hotFudge = 0;
		int literalWarpedSource = 0;
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
							if (state.is(CakeWorldBlocks.BLACK_LIQUORICE_STONE.get())) {
								blackStone++;
							} else if (state.is(CakeWorldBlocks.FUDGE_ROCK.get())) {
								fudgeRock++;
							} else if (state.is(CakeWorldBlocks.LIQUORICE_ROOT.get())) {
								liquoriceRoots++;
							} else if (state.is(CakeWorldBlocks.BLACK_LIQUORICE_TANGLE.get())) {
								tangles++;
							} else if (state.is(CakeWorldBlocks.MINT_CRYSTAL.get())) {
								mintCrystals++;
							} else if (state.is(CakeWorldFluids.HOT_FUDGE_BLOCK.get())) {
								hotFudge++;
							} else if (isLiteralWarpedSource(state)) {
								literalWarpedSource++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, blackStone, fudgeRock,
				liquoriceRoots, tangles, mintCrystals,
				hotFudge, literalWarpedSource);
	}

	private static boolean isLiteralWarpedSource(BlockState state) {
		return state.is(Blocks.WARPED_NYLIUM)
				|| state.is(Blocks.WARPED_STEM)
				|| state.is(Blocks.WARPED_HYPHAE)
				|| state.is(Blocks.STRIPPED_WARPED_STEM)
				|| state.is(Blocks.STRIPPED_WARPED_HYPHAE)
				|| state.is(Blocks.WARPED_WART_BLOCK)
				|| state.is(Blocks.WARPED_ROOTS)
				|| state.is(Blocks.CRIMSON_ROOTS)
				|| state.is(Blocks.NETHER_SPROUTS)
				|| state.is(Blocks.WARPED_FUNGUS)
				|| state.is(Blocks.TWISTING_VINES)
				|| state.is(Blocks.TWISTING_VINES_PLANT)
				|| state.is(Blocks.SHROOMLIGHT);
	}

	private static boolean pathExists(boolean openShortcuts) {
		record Cell(int x, int z) {
		}
		Cell start = new Cell(0, -4);
		Cell goal = new Cell(3, 4);
		ArrayDeque<Cell> pending = new ArrayDeque<>();
		Set<Cell> visited = new HashSet<>();
		pending.add(start);
		visited.add(start);
		while (!pending.isEmpty()) {
			Cell cell = pending.removeFirst();
			if (cell.equals(goal)) {
				return true;
			}
			for (int[] direction : new int[][] {
				{1, 0}, {-1, 0}, {0, 1}, {0, -1}
			}) {
				Cell next = new Cell(cell.x() + direction[0],
						cell.z() + direction[1]);
				if (next.x() < -4 || next.x() > 4
						|| next.z() < -4 || next.z() > 4) {
					continue;
				}
				boolean wall = BlackLiquoriceLoopFeature
						.isWall(next.x(), next.z());
				if (wall && !(openShortcuts
						&& BlackLiquoriceLoopFeature
								.isShortcut(next.x(), next.z()))) {
					continue;
				}
				if (visited.add(next)) {
					pending.add(next);
				}
			}
		}
		return false;
	}

	private static Set<Integer> entityIds(
			ServerLevel level, BlockPos centre) {
		Set<Integer> ids = new HashSet<>();
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-4, 0, -4), centre.offset(5, 4, 5)))
				.forEach(entity -> ids.add(entity.getId()));
		return ids;
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
				"Black-Liquorice Labyrinths lost replacement "
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
		if (biome == null) {
			return false;
		}
		int step = decoration.ordinal();
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
				BlackLiquoriceLabyrinthsGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int stone, int gilded, int roots,
			int marshmallow, int glass, int bricks, int mint,
			int sentinelBricks) {
		private boolean identifiesLoop(boolean brickSentinel) {
			return stone >= 60
					&& gilded == (brickSentinel ? 0 : 1)
					&& roots >= 60 && marshmallow >= 1
					&& glass >= 2 && bricks >= 4 && mint >= 2
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}

		private boolean complete(boolean brickSentinel) {
			return stone == 80
					&& gilded == (brickSentinel ? 0 : 1)
					&& roots == 78 && marshmallow == 2
					&& glass == 4 && bricks == 6 && mint == 4
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundLoop(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int blackStone,
			int fudgeRock, int liquoriceRoots, int tangles,
			int mintCrystals, int hotFudge, int literalWarpedSource) {
	}
}
