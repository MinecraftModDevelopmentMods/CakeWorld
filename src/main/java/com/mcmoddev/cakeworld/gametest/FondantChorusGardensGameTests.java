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
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.FondantChorusBloomBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.item.GuidedChorusFoodItem;
import com.mcmoddev.cakeworld.world.FondantChorusCarouselFeature;
import com.mcmoddev.cakeworld.world.FondantChorusSculptureFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ChorusFruitItem;
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
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/** Contract proof for the complete BIO-END-007 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class FondantChorusGardensGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("fondant_chorus_gardens");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
		Rotation.NONE,
		Rotation.CLOCKWISE_90,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90
	};

	private FondantChorusGardensGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioend007")
	public static void gardensHaveRenewableChorusEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome garden = registry.get(BIOME_ID);
		require(helper, garden != null
						&& close(garden.getBaseTemperature(), 0.5D)
						&& close(garden.getDownfall(), 0.0D),
				"Fondant Chorus Gardens lost its cool, dry End climate");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.VOID,
				BiomeDictionary.Type.MAGICAL,
				BiomeDictionary.Type.LUSH)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Fondant Chorus Gardens lost dictionary type " + type);
		}

		AmbientAdditionsSettings ambience =
				garden.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				garden.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds.FONDANT_CHORUS_GARDEN_BELL
										.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.END_ROD,
				"Fondant Chorus Gardens lost its pastel bell atmosphere");

		MobSpawnSettings.SpawnerData tallwalker = findSpawn(garden,
				CakeWorldEntities.TAFFY_TALLWALKER.get());
		MobSpawnSettings.SpawnerData mite = findSpawn(garden,
				CakeWorldEntities.SUGAR_MITE.get());
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += garden.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, tallwalker != null
						&& tallwalker.getWeight().asInt() == 10
						&& tallwalker.minCount == 4
						&& tallwalker.maxCount == 4
						&& mite != null
						&& mite.getWeight().asInt() == 6
						&& mite.minCount == 1
						&& mite.maxCount == 3
						&& findSpawn(garden, EntityType.ENDERMAN) == null
						&& findSpawn(garden, EntityType.ENDERMITE) == null
						&& totalSpawns == 2,
				"Fondant Chorus Gardens lost its exact Tallwalker/Mite ecology: "
						+ totalSpawns);

		assertPlantsAndFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend007")
	public static void carouselAndSculpturesAreBoundedSafeAndNative(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Biome garden = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		Holder<PlacedFeature> sculptures =
				FondantChorusSculptureFeature.placedFeature();
		Holder<PlacedFeature> carousel =
				FondantChorusCarouselFeature.placedFeature();
		require(helper, sculptures != null
						&& sculptures.value().feature().value().feature()
								== FondantChorusSculptureFeature.FEATURE
						&& sculptures.value().placement().size() == 4
						&& sculptures.value().placement().get(0)
								instanceof CountPlacement
						&& sculptures.value().placement().get(1)
								instanceof InSquarePlacement
						&& sculptures.value().placement().get(2)
								instanceof HeightmapPlacement
						&& sculptures.value().placement().get(3)
								instanceof BiomeFilter
						&& hasPlacedFeature(garden,
								GenerationStep.Decoration.VEGETAL_DECORATION,
								sculptures)
						&& !hasPlacedFeature(garden,
								GenerationStep.Decoration.VEGETAL_DECORATION,
								EndPlacements.CHORUS_PLANT),
				"Fondant sculptures did not replace vanilla Chorus placement");
		require(helper, carousel != null
						&& carousel.value().feature().value().feature()
								== FondantChorusCarouselFeature.FEATURE
						&& carousel.value().placement().size() == 4
						&& carousel.value().placement().get(0)
								instanceof RarityFilter
						&& carousel.value().placement().get(1)
								instanceof InSquarePlacement
						&& carousel.value().placement().get(2)
								instanceof HeightmapPlacement
						&& carousel.value().placement().get(3)
								instanceof BiomeFilter
						&& FondantChorusCarouselFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& FondantChorusCarouselFeature.MAX_TERRAIN_RELIEF == 6
						&& hasPlacedFeature(garden,
								GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
								carousel),
				"Fondant Chorus Carousel lost its bounded placement chain");

		ChunkPos helperChunk = new ChunkPos(helper.absolutePos(
				new BlockPos(8, 5, 8)));
		BlockPos centre = new BlockPos(helperChunk.getMinBlockX() + 8,
				63, helperChunk.getMinBlockZ() + 8);
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre);
			Set<Integer> before = entityIds(level, centre);
			require(helper,
					FondantChorusCarouselFeature.hasSafeFootprint(
							level, centre, rotation)
							&& FondantChorusCarouselFeature.buildAt(
									level, centre, rotation),
					"Fondant Carousel rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false)
					&& carouselStemAxesMatch(level, centre, rotation),
					"Fondant Carousel plan changed for " + rotation
							+ ": " + plan);
			require(helper, before.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Fondant Carousel created an entity or block entity");
		}

		prepareSite(level, centre);
		level.setBlock(centre.offset(3, 1, 3),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !FondantChorusCarouselFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Fondant Carousel replaced an existing fluid");
		prepareSite(level, centre);
		level.setBlock(centre.offset(-3, 1, -3),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !FondantChorusCarouselFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Fondant Carousel replaced a block entity");
		prepareSite(level, centre);
		level.setBlock(centre.offset(1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !FondantChorusCarouselFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Fondant Carousel replaced an authored solid");
		require(helper, !FondantChorusCarouselFeature.fitsWithinChunk(
				new BlockPos(helperChunk.getMinBlockX(), centre.getY(),
						helperChunk.getMinBlockZ()),
				Rotation.NONE, helperChunk),
				"Fondant Carousel crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend007world",
			timeoutTicks = 24000)
	public static void focusedNaturalFondantChorusGardensAudit(
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
				"Could not locate Fondant Chorus Gardens within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundCarousel found = findCarousel(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Fondant Chorus Carousel within 1,089 chunks of "
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
			BlockPos sentinel = FondantChorusCarouselFeature.local(
					found.centre(), found.rotation(), -1, 2, -1);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 4, found.centre());
			LOGGER.info("Fondant Chorus Gardens audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, pastelFondant={}, surfaceFondant={}, meringueFoam={}, biscuitStone={}, waferRock={}, rockCandy={}, nougatRock={}, naturalStems={}, naturalBlooms={}, vanillaChorus={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.pastelFondant(),
					audit.surfaceFondant(), audit.meringueFoam(),
					audit.biscuitStone(), audit.waferRock(),
					audit.rockCandy(), audit.nougatRock(),
					audit.naturalStems(), audit.naturalBlooms(),
					audit.vanillaChorus(), plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.pastelFondant() > 0
							&& audit.surfaceFondant() > 0
							&& audit.naturalStems() > 0
							&& audit.naturalBlooms() > 0
							&& audit.vanillaChorus() == 0
							&& audit.meringueFoam() + audit.biscuitStone()
									+ audit.waferRock() + audit.rockCandy()
									+ audit.nougatRock() > 0
							&& plan.complete(brickSentinel),
					"Natural Fondant Chorus Gardens lost layered terrain, native sculptures or its complete Carousel: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
						"Could not seed the Carousel reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertPlantsAndFood(
			GameTestHelper helper, ServerLevel level) {
		ChocolateSpongeBlock fondant = (ChocolateSpongeBlock)
				CakeWorldBlocks.PASTEL_FONDANT.get();
		Pig falling = EntityType.PIG.create(level);
		require(helper, falling != null,
				"Could not create Pastel Fondant fall fixture");
		falling.setHealth(10.0F);
		fondant.fallOn(level, fondant.defaultBlockState(), BlockPos.ZERO,
				falling, 11.0F);
		require(helper, close(falling.getHealth(), 8.0D),
				"Pastel Fondant lost its forgiving quarter-fall contract");

		BlockPos plantBase = helper.absolutePos(new BlockPos(1, 2, 1));
		level.setBlock(plantBase.below(),
				CakeWorldBlocks.PASTEL_FONDANT.get().defaultBlockState(), 2);
		level.setBlock(plantBase,
				CakeWorldBlocks.FONDANT_CHORUS_STEM.get().defaultBlockState(), 2);
		BlockPos bloomPos = plantBase.above();
		level.setBlock(bloomPos,
				FondantChorusSculptureFeature.ripeBloom(), 2);
		Player picker = helper.makeMockPlayer();
		int before = level.getEntitiesOfClass(ItemEntity.class,
				new AABB(bloomPos.offset(-1, -1, -1),
						bloomPos.offset(2, 2, 2))).size();
		((FondantChorusBloomBlock)
				CakeWorldBlocks.FONDANT_CHORUS_BLOOM.get()).use(
						level.getBlockState(bloomPos), level, bloomPos,
						picker, InteractionHand.MAIN_HAND,
						new BlockHitResult(Vec3.atCenterOf(bloomPos),
								Direction.UP, bloomPos, false));
		int after = level.getEntitiesOfClass(ItemEntity.class,
				new AABB(bloomPos.offset(-1, -1, -1),
						bloomPos.offset(2, 2, 2))).size();
		require(helper, level.getBlockState(bloomPos).getValue(
					FondantChorusBloomBlock.AGE) == 0 && after > before,
				"Fondant Chorus Bloom lost its renewable pick-in-place loop");

		BlockPos blinkOrigin = helper.absolutePos(new BlockPos(3, 3, 3));
		for (int x = 0; x <= GuidedChorusFoodItem.MAX_DISTANCE; x++) {
			BlockPos feet = blinkOrigin.east(x);
			level.setBlock(feet.below(), Blocks.BRICKS.defaultBlockState(), 2);
			level.setBlock(feet, Blocks.AIR.defaultBlockState(), 2);
			level.setBlock(feet.above(), Blocks.AIR.defaultBlockState(), 2);
		}
		BlockPos destination = GuidedChorusFoodItem.findSafeDestination(
				level, blinkOrigin, Direction.EAST);
		require(helper, destination != null
						&& destination.equals(blinkOrigin.east(3)),
				"Garden Waybite lost its readable three-block safe target");

		FoodProperties raw = CakeWorldItems.FONDANT_CHORUS_BERRY.get()
				.getFoodProperties();
		FoodProperties prepared = CakeWorldItems.GARDEN_WAYBITE.get()
				.getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("garden_waybite")).orElse(null);
		require(helper,
				CakeWorldItems.FONDANT_CHORUS_BERRY.get()
						instanceof ChorusFruitItem
						&& CakeWorldItems.GARDEN_WAYBITE.get()
								instanceof GuidedChorusFoodItem
						&& raw != null && raw.getNutrition() == 4
						&& close(raw.getSaturationModifier(), 0.3D)
						&& hasEffect(raw, MobEffects.SLOW_FALLING, 100)
						&& prepared != null && prepared.getNutrition() == 7
						&& close(prepared.getSaturationModifier(), 0.75D)
						&& hasEffect(prepared, MobEffects.SLOW_FALLING, 160)
						&& recipe != null
						&& recipe.getType() == RecipeType.CRAFTING
						&& recipe.getIngredients().size() == 4
						&& ingredientCount(recipe, new ItemStack(
								CakeWorldItems.FONDANT_CHORUS_BERRY.get())) == 2
						&& ingredient(recipe, new ItemStack(
								CakeWorldItems.STAR_SUGAR_CRYSTALS.get()))
						&& ingredient(recipe, new ItemStack(
								CakeWorldItems.SIMPLE_BISCUIT.get()))
						&& recipe.getResultItem().is(
								CakeWorldItems.GARDEN_WAYBITE.get())
						&& recipe.getResultItem().getCount() == 2,
				"Fondant berries or Garden Waybite lost their raw/prepared contract");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 46,
				"Fondant Chorus Gardens requires provider revision 46");
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
								.getAsDouble(), 12.0D)
						&& close(geomes.get("cakeworld:rock_candy_uplift")
								.getAsDouble(), 8.0D)
						&& close(placement.get("weight").getAsDouble(), 1.0D)
						&& strings(placement.getAsJsonArray("similar_biomes"))
								.equals(Set.of("minecraft:end_highlands"))
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
						&& "cakeworld:pastel_fondant".equals(
								surface.get("top_block").getAsString())
						&& "cakeworld:meringue_foam".equals(
								surface.get("filler_block").getAsString())
						&& "cakeworld:candy_glass".equals(
								surface.get("underwater_block").getAsString())
						&& surface.get("filler_depth").getAsInt() == 4,
				"Adventure profiles lost their Fondant Chorus Gardens contract");
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
						CakeWorldBlocks.PASTEL_FONDANT.get()
								.defaultBlockState(), 2);
				for (int y = 1; y <= 6; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
	}

	private static PlanAudit inspectPlan(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		int meringueBricks = 0;
		int pastelFondant = 0;
		int marshmallow = 0;
		int candyGlass = 0;
		int macaronBricks = 0;
		int stems = 0;
		int blooms = 0;
		int endRods = 0;
		int racks = 0;
		int bowls = 0;
		int sentinelBricks = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 5; y++) {
					BlockState state = level.getBlockState(
							FondantChorusCarouselFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())) {
						meringueBricks++;
					} else if (state.is(CakeWorldBlocks.PASTEL_FONDANT.get())
							&& y == 1 && Math.abs(x) <= 3
							&& Math.abs(z) <= 3) {
						pastelFondant++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						candyGlass++;
					} else if (state.is(CakeWorldBlocks.MACARON_BRICKS.get())) {
						macaronBricks++;
					} else if (state.is(CakeWorldBlocks.FONDANT_CHORUS_STEM.get())) {
						stems++;
					} else if (state.is(CakeWorldBlocks.FONDANT_CHORUS_BLOOM.get())) {
						blooms++;
					} else if (state.is(Blocks.END_ROD)) {
						endRods++;
					} else if (state.is(CakeWorldBlocks.COOLING_RACK.get())) {
						racks++;
					} else if (state.is(CakeWorldBlocks.MIXING_BOWL.get())) {
						bowls++;
					} else if (state.is(Blocks.BRICKS)
							&& y == 2 && Math.abs(x) == 1
							&& Math.abs(z) == 1) {
						sentinelBricks++;
					}
				}
			}
		}
		return new PlanAudit(meringueBricks, pastelFondant, marshmallow,
				candyGlass, macaronBricks, stems, blooms, endRods,
				racks, bowls, sentinelBricks);
	}

	private static boolean carouselStemAxesMatch(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		for (int x : new int[] {-2, 2}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 2; y <= 4; y++) {
					BlockState vertical = level.getBlockState(
							FondantChorusCarouselFeature.local(
									centre, rotation, x, y, z));
					if (!vertical.is(CakeWorldBlocks.FONDANT_CHORUS_STEM.get())
							|| vertical.getValue(RotatedPillarBlock.AXIS)
									!= Direction.Axis.Y) {
						return false;
					}
				}
				Direction localBranch = x < 0
						? Direction.WEST : Direction.EAST;
				BlockState branch = level.getBlockState(
						FondantChorusCarouselFeature.local(centre, rotation,
								x + localBranch.getStepX(), 3, z));
				if (!branch.is(CakeWorldBlocks.FONDANT_CHORUS_STEM.get())
						|| branch.getValue(RotatedPillarBlock.AXIS)
								!= rotation.rotate(localBranch).getAxis()) {
					return false;
				}
			}
		}
		return true;
	}

	private static FoundCarousel findCarousel(ServerLevel level,
			ChunkPos anchor, int radius) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int ring = 0; ring <= radius; ring++) {
			for (int chunkX = anchor.x - ring;
					chunkX <= anchor.x + ring; chunkX++) {
				for (int chunkZ = anchor.z - ring;
						chunkZ <= anchor.z + ring; chunkZ++) {
					if (ring > 0 && Math.abs(chunkX - anchor.x) != ring
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
									BlockPos offset = new BlockPos(
											-1, 2, 0).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(offset);
									PlanAudit plan = inspectPlan(level, centre,
											rotation);
									if (plan.identifies(false)) {
										return new FoundCarousel(centre, rotation);
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
			int radius, BlockPos carousel) {
		int biomeSamples = 0;
		int pastelFondant = 0;
		int surfaceFondant = 0;
		int meringueFoam = 0;
		int biscuitStone = 0;
		int waferRock = 0;
		int rockCandy = 0;
		int nougatRock = 0;
		int naturalStems = 0;
		int naturalBlooms = 0;
		int vanillaChorus = 0;
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
							boolean near = Math.abs(x - carousel.getX()) <= 4
									&& Math.abs(z - carousel.getZ()) <= 4
									&& y >= carousel.getY()
									&& y <= carousel.getY() + 6;
							BlockState state = level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks.PASTEL_FONDANT.get())) {
								pastelFondant++;
								if (level.getBlockState(cursor.below()).is(
										CakeWorldBlocks.MERINGUE_FOAM.get())) {
									surfaceFondant++;
								}
							} else if (state.is(CakeWorldBlocks.MERINGUE_FOAM.get())) {
								meringueFoam++;
							} else if (state.is(CakeWorldBlocks.BISCUIT_STONE.get())) {
								biscuitStone++;
							} else if (state.is(CakeWorldBlocks.WAFER_ROCK.get())) {
								waferRock++;
							} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
								rockCandy++;
							} else if (state.is(CakeWorldBlocks.NOUGAT_ROCK.get())) {
								nougatRock++;
							} else if (!near && state.is(
									CakeWorldBlocks.FONDANT_CHORUS_STEM.get())) {
								naturalStems++;
							} else if (!near && state.is(
									CakeWorldBlocks.FONDANT_CHORUS_BLOOM.get())) {
								naturalBlooms++;
							} else if (state.is(Blocks.CHORUS_PLANT)
									|| state.is(Blocks.CHORUS_FLOWER)) {
								vanillaChorus++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, pastelFondant,
				surfaceFondant, meringueFoam, biscuitStone, waferRock,
				rockCandy, nougatRock, naturalStems, naturalBlooms,
				vanillaChorus);
	}

	private static Set<Integer> entityIds(ServerLevel level, BlockPos centre) {
		Set<Integer> ids = new HashSet<>();
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-4, 0, -4), centre.offset(5, 7, 5)))
				.forEach(entity -> ids.add(entity.getId()));
		return ids;
	}

	private static int countBlockEntities(ServerLevel level,
			BlockPos centre) {
		int count = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 5; y++) {
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
			GenerationStep.Decoration decoration,
			Holder<PlacedFeature> expected) {
		int step = decoration.ordinal();
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
				FondantChorusGardensGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int meringueBricks, int pastelFondant,
			int marshmallow, int candyGlass, int macaronBricks,
			int stems, int blooms, int endRods, int racks, int bowls,
			int sentinelBricks) {
		private boolean identifies(boolean exactFoundation) {
			return (exactFoundation ? meringueBricks == 49
					: meringueBricks >= 49)
					&& pastelFondant == 20 && marshmallow == 4
					&& candyGlass == 9 && macaronBricks == 16
					&& stems == 16 && blooms == 8
					&& endRods + sentinelBricks == 4
					&& racks == 1 && bowls == 1;
		}

		private boolean complete(boolean brickSentinel) {
			return identifies(true)
					&& endRods == (brickSentinel ? 3 : 4)
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundCarousel(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int pastelFondant,
			int surfaceFondant, int meringueFoam, int biscuitStone,
			int waferRock, int rockCandy, int nougatRock,
			int naturalStems, int naturalBlooms, int vanillaChorus) {
	}
}
