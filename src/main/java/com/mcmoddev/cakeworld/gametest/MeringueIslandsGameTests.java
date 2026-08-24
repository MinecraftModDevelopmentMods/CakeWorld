package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.MeringueFoamBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.MeringueStarLandingFeature;

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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/** Contract proof for the first complete BIO-END-001 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class MeringueIslandsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID = id("meringue_islands");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private MeringueIslandsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioend001")
	public static void islandsHaveAiryEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome islands = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, islands != null
						&& close(islands.getBaseTemperature(), 0.5D)
						&& close(islands.getDownfall(), 0.0D),
				"Meringue Islands lost their cool, dry End climate");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.VOID,
				BiomeDictionary.Type.MAGICAL)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Meringue Islands lost dictionary type " + type);
		}

		AmbientAdditionsSettings ambience =
				islands.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				islands.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds.MERINGUE_ISLANDS_CHIME.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& particle != null
						&& particle.getOptions().getType() == ParticleTypes.END_ROD,
				"Meringue Islands lost their starlight atmosphere");

		MobSpawnSettings.SpawnerData tallwalker = findSpawn(islands,
				CakeWorldEntities.TAFFY_TALLWALKER.get());
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += islands.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, tallwalker != null
						&& tallwalker.getWeight().asInt() == 10
						&& tallwalker.minCount == 4
						&& tallwalker.maxCount == 4
						&& findSpawn(islands, EntityType.ENDERMAN) == null
						&& totalSpawns == 1,
				"Meringue Islands lost the exact inherited Tallwalker ecology: "
						+ totalSpawns);

		for (String tag : List.of(
				"minecraft:has_structure/end_city",
				"cakeworld:has_structure/macaron_citadel")) {
			require(helper, holder.is(TagKey.create(Registry.BIOME_REGISTRY,
					new ResourceLocation(tag))),
					"Meringue Islands lost structure progression role " + tag);
		}
		require(helper, !holder.is(TagKey.create(Registry.BIOME_REGISTRY,
				new ResourceLocation(
						"cakeworld:has_structure/crater_kitchen"))),
				"Meringue Islands retained the migrated Crater Kitchen role");
		assertFoam(helper, level);
		assertFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend001")
	public static void starLandingIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed = MeringueStarLandingFeature
				.placedFeature();
		Biome islands = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== MeringueStarLandingFeature.FEATURE
						&& placed.value().placement().size() == 4
						&& placed.value().placement().get(0)
								instanceof RarityFilter
						&& placed.value().placement().get(1)
								instanceof InSquarePlacement
						&& placed.value().placement().get(2)
								instanceof HeightmapPlacement
						&& placed.value().placement().get(3)
								instanceof BiomeFilter
						&& MeringueStarLandingFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& MeringueStarLandingFeature.MAX_TERRAIN_RELIEF == 6
						&& hasPlacedFeature(islands, placed),
				"Meringue Star Landing lost its bounded placement chain");

		ChunkPos helperChunk = new ChunkPos(helper.absolutePos(
				new BlockPos(8, 5, 8)));
		BlockPos centre = new BlockPos(helperChunk.getMinBlockX() + 8,
				63, helperChunk.getMinBlockZ() + 8);
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					MeringueStarLandingFeature.hasSafeFootprint(
							level, centre, rotation)
							&& MeringueStarLandingFeature.buildAt(
									level, centre, rotation),
					"Meringue Star Landing rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false),
					"Meringue Star Landing plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Meringue Star Landing created an entity or block entity");
		}

		prepareSite(level, centre);
		level.setBlock(centre.offset(4, 1, 4),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !MeringueStarLandingFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Meringue Star Landing replaced an existing fluid");
		prepareSite(level, centre);
		level.setBlock(centre.offset(-4, 1, -4),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !MeringueStarLandingFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Meringue Star Landing replaced a block entity");
		prepareSite(level, centre);
		level.setBlock(centre.offset(1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !MeringueStarLandingFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Meringue Star Landing replaced an authored solid");
		require(helper, !MeringueStarLandingFeature.fitsWithinChunk(
				new BlockPos(helperChunk.getMinBlockX(), centre.getY(),
						helperChunk.getMinBlockZ()), Rotation.NONE, helperChunk),
				"Meringue Star Landing crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend001world",
			timeoutTicks = 24000)
	public static void focusedNaturalMeringueIslandsAudit(
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
				"Could not locate Meringue Islands within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundLanding found = findLanding(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Meringue Star Landing within 1,089 chunks of "
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
			int[] beacon = MeringueStarLandingFeature.beacons()[0];
			BlockPos sentinel = MeringueStarLandingFeature.local(
					found.centre(), found.rotation(), beacon[0], 4, beacon[1]);
			boolean brickSentinel = level.getBlockState(sentinel).is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 4, found.centre());
			LOGGER.info("Meringue Islands audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, meringueFoam={}, marshmallow={}, nougatRock={}, rockCandy={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.meringueFoam(),
					audit.marshmallow(), audit.nougatRock(),
					audit.rockCandy(), plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.meringueFoam() > 0
							&& audit.marshmallow() > 0
							&& audit.nougatRock() + audit.rockCandy() > 0
							&& plan.complete(brickSentinel),
					"Natural Meringue Islands lost terrain, geology or their complete Landing: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
						"Could not seed the Star Landing reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertFoam(GameTestHelper helper, ServerLevel level) {
		MeringueFoamBlock foam = (MeringueFoamBlock)
				CakeWorldBlocks.MERINGUE_FOAM.get();
		ArmorStand falling = new ArmorStand(level, 0.5D, 64.5D, 0.5D);
		falling.setDeltaMovement(0.5D, -4.0D, -0.5D);
		float health = falling.getHealth();
		foam.fallOn(level, foam.defaultBlockState(), BlockPos.ZERO,
				falling, 20.0F);
		foam.updateEntityAfterFallOn(level, falling);
		foam.stepOn(level, BlockPos.ZERO, foam.defaultBlockState(), falling);
		require(helper, close(falling.getHealth(), health)
						&& close(falling.getDeltaMovement().x, 0.5D)
						&& close(falling.getDeltaMovement().y,
								MeringueFoamBlock.MAXIMUM_BOUNCE)
						&& close(falling.getDeltaMovement().z, -0.5D)
						&& falling.hasEffect(MobEffects.SLOW_FALLING)
						&& falling.getEffect(MobEffects.SLOW_FALLING)
								.getDuration()
								== MeringueFoamBlock.SLOW_FALLING_TICKS,
				"Meringue Foam lost its no-damage bounce or rescue effect");

		BlockPos relative = new BlockPos(1, 2, 1);
		BlockPos absolute = helper.absolutePos(relative);
		helper.setBlock(relative, foam.defaultBlockState());
		ServerPlayer player = new ServerPlayer(level.getServer(), level,
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed5001"),
						"CakeWorldMeringueNibbleTest"));
		player.getFoodData().setFoodLevel(10);
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		InteractionResult nibbled = foam.use(helper.getBlockState(relative),
				level, absolute, player, InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP,
						absolute, false));
		require(helper, nibbled.consumesAction()
						&& player.getFoodData().getFoodLevel() == 11
						&& helper.getBlockState(relative)
								.getValue(MeringueFoamBlock.BITES) == 1,
				"Meringue Foam lost its low-value visible nibble contract");
		player.getFoodData().setFoodLevel(20);
		InteractionResult full = foam.use(helper.getBlockState(relative), level,
				absolute, player, InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP,
						absolute, false));
		require(helper, full == InteractionResult.PASS
						&& helper.getBlockState(relative)
								.getValue(MeringueFoamBlock.BITES) == 1,
				"Meringue Foam could be nibbled at full hunger");
	}

	private static void assertFood(GameTestHelper helper, ServerLevel level) {
		FoodProperties food = CakeWorldItems.STARLIGHT_PAVLOVA.get()
				.getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("starlight_pavlova")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 8
						&& close(food.getSaturationModifier(), 0.8D)
						&& hasEffect(food, MobEffects.SLOW_FALLING, 300)
						&& hasEffect(food, MobEffects.NIGHT_VISION, 300)
						&& recipe != null
						&& recipe.getType() == RecipeType.CRAFTING
						&& recipe.getIngredients().size() == 4
						&& ingredient(recipe, new ItemStack(
								CakeWorldBlocks.MARSHMALLOW.get()))
						&& ingredient(recipe, new ItemStack(
								CakeWorldItems.GLOWING_JAM_BERRY.get()))
						&& ingredient(recipe, new ItemStack(Items.SUGAR))
						&& ingredient(recipe, new ItemStack(Items.EGG))
						&& recipe.getResultItem().is(
								CakeWorldItems.STARLIGHT_PAVLOVA.get())
						&& recipe.getResultItem().getCount() == 2,
				"Starlight Pavlova lost its nutrition, effects or recipe");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 42,
				"Meringue Islands require provider revision 42");
		JsonObject firstPalette = null;
		for (String template : List.of("cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = provider.getAsJsonObject("templates")
					.getAsJsonObject(template).getAsJsonObject("profile");
			JsonObject geomes = profile.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject end = profile.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:end");
			JsonObject placement = end.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface = placement.getAsJsonObject("surface");
			require(helper, geomes.size() == 2
							&& close(geomes.get("cakeworld:meringue_crust")
									.getAsDouble(), 12.0D)
							&& close(geomes.get("cakeworld:rock_candy_uplift")
									.getAsDouble(), 4.0D)
							&& "minecraft:the_end".equals(
									end.get("dimension").getAsString())
							&& "replace".equals(end.get("mode").getAsString())
							&& "all".equals(end.get("scope").getAsString())
							&& close(end.get("coverage").getAsDouble(), 1.0D)
							&& close(end.get("fallback_weight").getAsDouble(), 0.0D)
							&& end.getAsJsonObject("biomes").size() == 3
							&& close(placement.get("weight").getAsDouble(), 1.0D)
							&& strings(placement.getAsJsonArray("similar_biomes"))
									.isEmpty()
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
							&& "cakeworld:meringue_foam".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:marshmallow".equals(
									surface.get("filler_block").getAsString())
							&& "cakeworld:marshmallow".equals(
									surface.get("underwater_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5,
					template + " lost its Meringue Islands provider contract");
			if (firstPalette == null) {
				firstPalette = end;
			} else {
				require(helper, firstPalette.equals(end),
						"Normal and BaseMetals End palettes diverged");
			}
		}
	}

	private static void prepareSite(ServerLevel level, BlockPos centre) {
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-5, -1, -5), centre.offset(6, 5, 6)))
				.forEach(Entity::discard);
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				level.setBlock(centre.offset(x, -1, z),
						Blocks.END_STONE.defaultBlockState(), 2);
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.MERINGUE_FOAM.get()
								.defaultBlockState(), 2);
				for (int y = 1; y <= 5; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
	}

	private static PlanAudit inspectPlan(ServerLevel level, BlockPos centre,
			Rotation rotation) {
		int meringueBricks = 0;
		int meringueFoam = 0;
		int wafer = 0;
		int marshmallow = 0;
		int glass = 0;
		int endRods = 0;
		int coolingRacks = 0;
		int mixingBowls = 0;
		int sentinelBricks = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 4; y++) {
					BlockState state = level.getBlockState(
							MeringueStarLandingFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())) {
						meringueBricks++;
					} else if (state.is(CakeWorldBlocks.MERINGUE_FOAM.get())) {
						meringueFoam++;
					} else if (state.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
						wafer++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldBlocks.CANDY_GLASS.get())) {
						glass++;
					} else if (state.is(Blocks.END_ROD)
							&& state.getValue(EndRodBlock.FACING) == Direction.UP) {
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
		return new PlanAudit(meringueBricks, meringueFoam, wafer,
				marshmallow, glass, endRods, coolingRacks, mixingBowls,
				sentinelBricks);
	}

	private static FoundLanding findLanding(ServerLevel level,
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
											1, 2, 0).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(markerOffset);
									PlanAudit plan = inspectPlan(level, centre,
											rotation);
									if (plan.identifiesLanding()) {
										return new FoundLanding(centre, rotation);
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
			int radius, BlockPos landing) {
		int biomeSamples = 0;
		int meringueFoam = 0;
		int marshmallow = 0;
		int nougatRock = 0;
		int rockCandy = 0;
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
							if (state.is(CakeWorldBlocks.MERINGUE_FOAM.get())) {
								if (!nearLanding(cursor, landing)) {
									meringueFoam++;
								}
							} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
								if (!nearLanding(cursor, landing)) {
									marshmallow++;
								}
							} else if (state.is(CakeWorldBlocks.NOUGAT_ROCK.get())) {
								nougatRock++;
							} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
								rockCandy++;
							}
						}
					}
				}
			}
		}
		return new NaturalAudit(biomeSamples, meringueFoam, marshmallow,
				nougatRock, rockCandy);
	}

	private static boolean nearLanding(BlockPos position, BlockPos centre) {
		return Math.abs(position.getX() - centre.getX()) <= 4
				&& position.getY() >= centre.getY()
				&& position.getY() <= centre.getY() + 4
				&& Math.abs(position.getZ() - centre.getZ()) <= 4;
	}

	private static Set<Integer> entityIds(ServerLevel level, BlockPos centre) {
		Set<Integer> ids = new HashSet<>();
		level.getEntitiesOfClass(Entity.class,
				new AABB(centre.offset(-4, 0, -4), centre.offset(5, 5, 5)))
				.forEach(entity -> ids.add(entity.getId()));
		return ids;
	}

	private static int countBlockEntities(ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
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
				MeringueIslandsGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int meringueBricks, int meringueFoam,
			int wafer, int marshmallow, int glass, int endRods,
			int coolingRacks, int mixingBowls, int sentinelBricks) {
		private boolean identifiesLanding() {
			return meringueBricks == 85 && meringueFoam == 32
					&& wafer == 13 && marshmallow == 36 && glass == 4
					&& endRods + sentinelBricks == 4
					&& coolingRacks == 1 && mixingBowls == 1;
		}

		private boolean complete(boolean brickSentinel) {
			return identifiesLanding()
					&& endRods == (brickSentinel ? 3 : 4)
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundLanding(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int meringueFoam,
			int marshmallow, int nougatRock, int rockCandy) {
	}
}
