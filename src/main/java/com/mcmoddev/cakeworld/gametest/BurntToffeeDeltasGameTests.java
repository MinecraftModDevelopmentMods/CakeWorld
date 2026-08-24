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
import com.mcmoddev.cakeworld.block.CrunchyToffeeAshBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.BurntToffeeColumnsFeature;
import com.mcmoddev.cakeworld.world.BurntToffeeDeltasPalette;
import com.mcmoddev.cakeworld.world.BurntToffeeFoundryFeature;

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
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

import org.slf4j.Logger;

/**
 * Contract proof for BIO-NE-002 and its existing STRUCT-018 anchor.
 *
 * <p>Hot Fudge in this historical slice remains terrain/aquifer/deposit
 * material, never spring evidence. OS-104 separately proves the repaired
 * current OreSpawn spring path.</p>
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class BurntToffeeDeltasGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("burnt_toffee_deltas");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private BurntToffeeDeltasGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bione002")
	public static void deltasHaveSafeSnapFoodEcologyAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome deltas = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, deltas != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.NETHER
						&& close(deltas.getBaseTemperature(), 2.0D)
						&& close(deltas.getDownfall(), 0.0D),
				"Burnt-Toffee Deltas are not a Basalt-Deltas-derived Nether biome");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.HOT,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.WASTELAND)) {
			require(helper,
					BiomeDictionary.hasType(BIOME_KEY, type),
					"Burnt-Toffee Deltas lost dictionary type "
							+ type);
		}
		AmbientAdditionsSettings ambience =
				deltas.getAmbientAdditions().orElse(null);
		AmbientParticleSettings ash =
				deltas.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.BURNT_TOFFEE_DELTAS_SNAP
										.getId())
						&& close(ambience.getTickChance(), 0.0015D)
						&& ash != null
						&& ash.getOptions().getType()
								== ParticleTypes.ASH,
				"Burnt-Toffee Deltas lost their snapping ash ambience");

		assertSpawn(helper, deltas, EntityType.MAGMA_CUBE,
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				MobCategory.MONSTER, 100, 2, 5);
		assertSpawn(helper, deltas, EntityType.GHAST,
				CakeWorldEntities.MALLOW_FLOATER.get(),
				MobCategory.MONSTER, 40, 1, 1);
		assertSpawn(helper, deltas, EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER.get(),
				MobCategory.CREATURE, 60, 1, 2);

		assertPalette(helper, deltas, registry);
		assertSafeAsh(helper, level);
		assertFood(helper, level);
		assertFoundryHomes(helper, registry);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bione002world",
			timeoutTicks = 24000)
	public static void focusedNaturalBurntToffeeDeltasAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.NETHER);
		require(helper, level != null,
				"The fixed-seed server did not expose the Nether");
		Pair<BlockPos, Holder<Biome>> match =
				level.findNearestBiome(
						biome -> biome.is(BIOME_KEY),
						new BlockPos(0, 64, 0),
						32768, 8);
		require(helper, match != null,
				"Could not locate Burnt-Toffee Deltas within 32,768 Nether blocks");
		BlockPos anchor = match.getFirst();
		ChunkPos anchorChunk = new ChunkPos(anchor);
		for (int chunkX = anchorChunk.x - 2;
				chunkX <= anchorChunk.x + 2; chunkX++) {
			for (int chunkZ = anchorChunk.z - 2;
					chunkZ <= anchorChunk.z + 2; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
		level.setChunkForced(anchorChunk.x, anchorChunk.z, true);
		NaturalAudit audit = audit(level, anchorChunk, 1);
		BlockPos sentinel = audit.firstAsh() == null
				? null : audit.firstAsh().above();
		boolean brickSentinel = sentinel != null
				&& level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		LOGGER.info("Burnt-Toffee Deltas audit: anchor={}, anchorChunk={}, biomeSamples={}, crunchyAsh={}, burntSugar={}, toffeePillars={}, hotFudge={}, literalSource={}, brickSentinel={}, sentinel={}",
				anchor, anchorChunk, audit.biomeSamples(),
				audit.crunchyAsh(), audit.burntSugar(),
				audit.toffeePillars(), audit.hotFudge(),
				audit.literalSource(), brickSentinel,
				sentinel);
		require(helper,
				audit.biomeSamples() >= 128
						&& audit.crunchyAsh() > 0
						&& audit.burntSugar() > 0
						&& audit.toffeePillars() > 0
						&& audit.hotFudge() > 0
						&& audit.literalSource() == 0
						&& sentinel != null,
				"Natural Burnt-Toffee Deltas lost their edible surface, native geology or Hot Fudge: "
						+ audit);
		if (!brickSentinel) {
			level.setBlock(sentinel,
					Blocks.BRICKS.defaultBlockState(), 2);
			require(helper,
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS),
					"Could not seed the Burnt-Toffee Deltas reload sentinel");
		}
		level.setChunkForced(anchorChunk.x,
				anchorChunk.z, false);
		helper.succeed();
	}

	private static void assertPalette(
			GameTestHelper helper, Biome deltas,
			Registry<Biome> registry) {
		BlockState basaltX = Blocks.BASALT.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS,
						Direction.Axis.X);
		BlockState pillarX =
				BurntToffeeDeltasPalette
						.convertedState(basaltX);
		BlockState smooth =
				Blocks.SMOOTH_BASALT.defaultBlockState();
		BlockState smoothPillar =
				BurntToffeeDeltasPalette
						.convertedState(smooth);
		ResourceKey<PlacedFeature> vanillaSmall = ResourceKey.create(
				Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", "small_basalt_columns"));
		ResourceKey<PlacedFeature> vanillaLarge = ResourceKey.create(
				Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", "large_basalt_columns"));
		ResourceKey<PlacedFeature> vanillaBasaltBlobs = ResourceKey.create(
				Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", "basalt_blobs"));
		ResourceKey<PlacedFeature> vanillaBlackstoneBlobs = ResourceKey.create(
				Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", "blackstone_blobs"));
		ResourceKey<PlacedFeature> vanillaGravelOre = ResourceKey.create(
				Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", "ore_gravel_nether"));
		for (ResourceLocation biomeId : List.of(
				id("fudge_wastes"), id("burnt_toffee_deltas"),
				id("cinnamon_ember_groves"),
				id("black_liquorice_labyrinths"),
				id("treacle_soul_valleys"))) {
			Biome cakeWorldNether = registry.get(biomeId);
			require(helper, cakeWorldNether != null
						&& !hasPlacedFeature(
								cakeWorldNether, vanillaGravelOre),
					"CakeWorld Nether biome leaked the cross-biome Gravel source: "
							+ biomeId);
		}
		require(helper,
				pillarX.is(CakeWorldBlocks
						.BURNT_TOFFEE_PILLAR.get())
						&& pillarX.getValue(
								RotatedPillarBlock.AXIS)
								== Direction.Axis.X
						&& smoothPillar.is(CakeWorldBlocks
								.BURNT_TOFFEE_PILLAR.get())
						&& smoothPillar.getValue(
								RotatedPillarBlock.AXIS)
								== Direction.Axis.Y
						&& BurntToffeeDeltasPalette
								.convertedState(
										Blocks.BLACKSTONE
												.defaultBlockState())
								.is(CakeWorldBlocks
										.BURNT_SUGAR_ROCK.get())
						&& BurntToffeeDeltasPalette
								.convertedState(
										Blocks.GRAVEL
												.defaultBlockState())
								.is(CakeWorldBlocks
										.CRUNCHY_TOFFEE_ASH.get())
						&& BurntToffeeDeltasPalette
								.convertedState(
										Blocks.MAGMA_BLOCK
												.defaultBlockState())
								.is(Blocks.MAGMA_BLOCK)
						&& BurntToffeeDeltasPalette
								.convertedState(pillarX)
								== pillarX
						&& ForgeRegistries.FEATURES.getValue(
								BurntToffeeColumnsFeature.ID)
								== BurntToffeeColumnsFeature.FEATURE
						&& hasPlacedFeature(deltas,
								BurntToffeeColumnsFeature.smallPlacedFeature(),
								GenerationStep.Decoration.SURFACE_STRUCTURES)
						&& hasPlacedFeature(deltas,
								BurntToffeeColumnsFeature.largePlacedFeature(),
								GenerationStep.Decoration.SURFACE_STRUCTURES)
						&& hasPlacedFeature(deltas,
								BurntToffeeColumnsFeature
										.burntToffeeBlobsPlacedFeature(),
								GenerationStep.Decoration.UNDERGROUND_DECORATION)
						&& hasPlacedFeature(deltas,
								BurntToffeeColumnsFeature
										.burntSugarBlobsPlacedFeature(),
								GenerationStep.Decoration.UNDERGROUND_DECORATION)
						&& !hasPlacedFeature(deltas, vanillaSmall)
						&& !hasPlacedFeature(deltas, vanillaLarge)
						&& !hasPlacedFeature(deltas, vanillaBasaltBlobs)
						&& !hasPlacedFeature(deltas, vanillaBlackstoneBlobs)
						&& !hasPlacedFeature(deltas, vanillaGravelOre),
				"Burnt-Toffee Deltas lost native geology, leaked vanilla formations or changed palette boundaries");
	}

	private static void assertSafeAsh(
			GameTestHelper helper, ServerLevel level) {
		require(helper,
				CakeWorldBlocks.CRUNCHY_TOFFEE_ASH.get()
						instanceof CrunchyToffeeAshBlock
						&& CrunchyToffeeAshBlock
								.SNAP_INTERVAL_TICKS == 6,
				"Crunchy Toffee Ash lost its safe snapping block");
		BlockPos pos = helper.absolutePos(
				new BlockPos(2, 2, 2));
		level.setBlock(pos,
				CakeWorldBlocks.CRUNCHY_TOFFEE_ASH.get()
						.defaultBlockState(), 2);
		Pig pig = EntityType.PIG.create(level);
		require(helper, pig != null,
				"Could not create safe-ash test entity");
		pig.tickCount = 6;
		pig.setDeltaMovement(0.2D, 0.0D, 0.0D);
		float health = pig.getHealth();
		Vec3 movement = pig.getDeltaMovement();
		CakeWorldBlocks.CRUNCHY_TOFFEE_ASH.get()
				.stepOn(level, pos,
						level.getBlockState(pos), pig);
		require(helper,
				close(pig.getHealth(), health)
						&& pig.getDeltaMovement().equals(movement)
						&& level.getBlockState(pos).is(
								CakeWorldBlocks
										.CRUNCHY_TOFFEE_ASH.get()),
				"Crunchy Toffee Ash damaged, displaced or destroyed terrain while snapping");
	}

	private static void assertFood(
			GameTestHelper helper, ServerLevel level) {
		FoodProperties food =
				CakeWorldItems.SMOKY_TOFFEE_SNAP.get()
						.getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("smoky_toffee_snap"))
				.orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 5
						&& close(food.getSaturationModifier(), 0.6D)
						&& hasEffect(food,
								MobEffects.FIRE_RESISTANCE, 300)
						&& hasEffect(food,
								CakeWorldEffects.SUGAR_RUSH.get(),
								160)
						&& recipe != null
						&& recipe.getType() == RecipeType.SMELTING
						&& recipe.getIngredients().size() == 1
						&& recipe.getIngredients().get(0)
								.test(new ItemStack(
										CakeWorldItems
												.CARAMEL_CHEW.get()))
						&& recipe.getResultItem().is(
								CakeWorldItems
										.SMOKY_TOFFEE_SNAP.get()),
				"Smoky Toffee Snap lost its prepared-food recipe or protective flavour");
	}

	private static void assertFoundryHomes(
			GameTestHelper helper, Registry<Biome> registry) {
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				BIOME_ID)) {
			Holder<Biome> biome = registry.getHolder(
					ResourceKey.create(
							Registry.BIOME_REGISTRY,
							biomeId)).orElseThrow();
			require(helper,
					biome.is(BurntToffeeFoundryFeature
							.GENERATES_IN)
							&& biome.is(
									BiomeTags
											.HAS_BASTION_REMNANT),
					"Burnt-Toffee Foundry lost biome home "
							+ biomeId);
		}
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 32,
				"Burnt-Toffee Deltas require provider revision 32");
		JsonObject firstPalette = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = provider
					.getAsJsonObject("templates")
					.getAsJsonObject(template)
					.getAsJsonObject("profile");
			JsonObject geomes = profile
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject nether = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:nether");
			JsonObject placement = nether
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface =
					placement.getAsJsonObject("surface");
			JsonObject deposit = profile
					.getAsJsonObject("fluid_deposits")
					.getAsJsonObject(
							"cakeworld:fluid_deposit/hot_fudge")
					.getAsJsonObject("dimensions")
					.getAsJsonObject("minecraft:the_nether");
			List<String> order = nether
					.getAsJsonObject("biomes")
					.entrySet().stream()
					.map(Map.Entry::getKey).toList();
			require(helper,
					geomes.size() == 1
							&& close(geomes.get(
									"cakeworld:fudge_mantle")
									.getAsDouble(), 20.0D)
							&& close(placement.get("weight")
									.getAsDouble(), 1.5D)
							&& strings(placement
									.getAsJsonArray(
											"similar_biomes"))
									.equals(Set.of(
											"minecraft:basalt_deltas"))
							&& strings(placement
									.getAsJsonArray(
											"required_similar_biomes"))
									.isEmpty()
							&& "cakeworld:crunchy_toffee_ash"
									.equals(surface.get(
											"top_block")
											.getAsString())
							&& "cakeworld:burnt_sugar_rock"
									.equals(surface.get(
											"filler_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5
							&& order.indexOf(BIOME_ID.toString())
									< order.indexOf(
											"cakeworld:fudge_wastes")
							&& strings(deposit
									.getAsJsonArray("biome_ids"))
										.equals(Set.of(
												"cakeworld:fudge_wastes",
												BIOME_ID.toString(),
												"cakeworld:cinnamon_ember_groves",
												"cakeworld:black_liquorice_labyrinths",
												"cakeworld:treacle_soul_valleys")),
					template
							+ " lost its Deltas selector, volcanic surface, geome or deposit boundary");
			if (firstPalette == null) {
				firstPalette = nether;
			} else {
				require(helper, firstPalette.equals(nether),
						"Normal and BaseMetals Nether palettes diverged");
			}
		}
	}

	private static NaturalAudit audit(
			ServerLevel level, ChunkPos anchor, int radius) {
		int biomeSamples = 0;
		int crunchyAsh = 0;
		int burntSugar = 0;
		int toffeePillars = 0;
		int hotFudge = 0;
		int basalt = 0;
		int smoothBasalt = 0;
		int blackstone = 0;
		int gravel = 0;
		BlockPos firstAsh = null;
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos();
		for (int chunkX = anchor.x - radius;
				chunkX <= anchor.x + radius; chunkX++) {
			for (int chunkZ = anchor.z - radius;
					chunkZ <= anchor.z + radius; chunkZ++) {
				ChunkPos chunk = new ChunkPos(chunkX, chunkZ);
				for (int x = chunk.getMinBlockX();
						x <= chunk.getMaxBlockX(); x++) {
					for (int z = chunk.getMinBlockZ();
							z <= chunk.getMaxBlockZ(); z++) {
						for (int y = 0; y <= 127; y++) {
							cursor.set(x, y, z);
							if (!level.getBiome(cursor)
									.is(BIOME_KEY)) {
								continue;
							}
							biomeSamples++;
							BlockState state =
									level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks
									.CRUNCHY_TOFFEE_ASH.get())) {
								crunchyAsh++;
								if (firstAsh == null) {
									firstAsh =
											cursor.immutable();
								}
							} else if (state.is(CakeWorldBlocks
									.BURNT_SUGAR_ROCK.get())) {
								burntSugar++;
							} else if (state.is(CakeWorldBlocks
									.BURNT_TOFFEE_PILLAR.get())) {
								toffeePillars++;
							}
							if (state.is(com.mcmoddev.cakeworld
									.init.CakeWorldFluids
									.HOT_FUDGE_BLOCK.get())) {
								hotFudge++;
							}
							if (state.is(Blocks.BASALT)) {
								basalt++;
							} else if (state.is(Blocks.SMOOTH_BASALT)) {
								smoothBasalt++;
							} else if (state.is(Blocks.BLACKSTONE)) {
								blackstone++;
							} else if (state.is(Blocks.GRAVEL)) {
								gravel++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples,
				crunchyAsh, burntSugar, toffeePillars,
				hotFudge, basalt, smoothBasalt,
				blackstone, gravel, firstAsh);
	}

	private static void assertSpawn(
			GameTestHelper helper, Biome biome,
			EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category, int weight,
			int minimum, int maximum) {
		MobSpawnSettings.SpawnerData converted =
				findSpawn(biome, replacement);
		require(helper, converted != null
						&& replacement.getCategory() == category
						&& converted.getWeight().asInt() == weight
						&& converted.minCount == minimum
						&& converted.maxCount == maximum
						&& findSpawn(biome, vanilla) == null,
				"Burnt-Toffee Deltas lost replacement "
						+ replacement.getRegistryName()
						+ " for " + vanilla.getRegistryName());
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
			Holder<PlacedFeature> expected,
			GenerationStep.Decoration decoration) {
		if (biome == null || expected == null) {
			return false;
		}
		int step = decoration.ordinal();
		return biome.getGenerationSettings().features().size() > step
				&& biome.getGenerationSettings().features().get(step)
						.stream().anyMatch(expected::equals);
	}

	private static boolean hasPlacedFeature(Biome biome,
			ResourceKey<PlacedFeature> expected) {
		return biome != null
				&& biome.getGenerationSettings().features().stream()
						.anyMatch(features -> features.stream()
								.anyMatch(feature -> feature.is(expected)));
	}

	private static boolean hasEffect(FoodProperties food,
			MobEffect effect, int duration) {
		return food.getEffects().stream()
				.anyMatch(entry ->
						entry.getFirst().getEffect() == effect
								&& entry.getFirst()
										.getDuration() == duration);
	}

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element ->
				values.add(element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				BurntToffeeDeltasGameTests.class
						.getResourceAsStream(
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

	private record NaturalAudit(
			int biomeSamples,
			int crunchyAsh,
			int burntSugar,
			int toffeePillars,
			int hotFudge,
			int basalt,
			int smoothBasalt,
			int blackstone,
			int gravel,
			BlockPos firstAsh) {
		private int literalSource() {
			return basalt + smoothBasalt + blackstone + gravel;
		}
	}
}
