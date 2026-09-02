package zone.moddev.mc.cakeworld.gametest;

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
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.ChocolateSpongeBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.MacaronWayfinderFeature;

import net.minecraft.core.BlockPos;
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

/** Contract proof for the complete BIO-END-005 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class MacaronArchipelagoGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID = id(
			"macaron_archipelago");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private MacaronArchipelagoGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioend005")
	public static void archipelagoHasLayeredEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome archipelago = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, archipelago != null
						&& close(archipelago.getBaseTemperature(), 0.5D)
						&& close(archipelago.getDownfall(), 0.0D),
				"Macaron Archipelago lost its cool, dry End climate");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.VOID,
				BiomeDictionary.Type.MAGICAL,
				BiomeDictionary.Type.RARE)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Macaron Archipelago lost dictionary type " + type);
		}

		AmbientAdditionsSettings ambience =
				archipelago.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				archipelago.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds.MACARON_ARCHIPELAGO_CHIME
										.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.WAX_ON,
				"Macaron Archipelago lost its pastel shell atmosphere");

		MobSpawnSettings.SpawnerData tallwalker = findSpawn(archipelago,
				CakeWorldEntities.TAFFY_TALLWALKER.get());
		MobSpawnSettings.SpawnerData llama = findSpawn(archipelago,
				CakeWorldEntities.MERINGUE_LLAMA.get());
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += archipelago.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, tallwalker != null
						&& tallwalker.getWeight().asInt() == 10
						&& tallwalker.minCount == 4
						&& tallwalker.maxCount == 4
						&& llama != null
						&& llama.getWeight().asInt() == 4
						&& llama.minCount == 2
						&& llama.maxCount == 4
						&& findSpawn(archipelago, EntityType.ENDERMAN) == null
						&& totalSpawns == 2,
				"Macaron Archipelago lost its exact Tallwalker/Llama ecology: "
						+ totalSpawns);

		for (String tag : List.of(
				"minecraft:has_structure/end_city",
				"cakeworld:has_structure/macaron_citadel")) {
			require(helper, holder.is(TagKey.create(Registry.BIOME_REGISTRY,
					new ResourceLocation(tag))),
					"Macaron Archipelago lost structure progression role "
							+ tag);
		}
		assertShellAndFood(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend005")
	public static void wayfinderIsBoundedSafeAndCardinal(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed = MacaronWayfinderFeature.placedFeature();
		Biome archipelago = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== MacaronWayfinderFeature.FEATURE
						&& placed.value().placement().size() == 4
						&& placed.value().placement().get(0)
								instanceof RarityFilter
						&& placed.value().placement().get(1)
								instanceof InSquarePlacement
						&& placed.value().placement().get(2)
								instanceof HeightmapPlacement
						&& placed.value().placement().get(3)
								instanceof BiomeFilter
						&& MacaronWayfinderFeature
								.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& MacaronWayfinderFeature.MAX_TERRAIN_RELIEF == 6
						&& hasPlacedFeature(archipelago, placed),
				"Macaron Wayfinder lost its bounded placement chain");

		ChunkPos helperChunk = new ChunkPos(helper.absolutePos(
				new BlockPos(8, 5, 8)));
		BlockPos centre = new BlockPos(helperChunk.getMinBlockX() + 8,
				63, helperChunk.getMinBlockZ() + 8);
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					MacaronWayfinderFeature.hasSafeFootprint(
							level, centre, rotation)
							&& MacaronWayfinderFeature.buildAt(
									level, centre, rotation),
					"Macaron Wayfinder rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false),
					"Macaron Wayfinder plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Macaron Wayfinder created an entity or block entity");
		}

		prepareSite(level, centre);
		level.setBlock(centre.offset(3, 1, 3),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !MacaronWayfinderFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Macaron Wayfinder replaced an existing fluid");
		prepareSite(level, centre);
		level.setBlock(centre.offset(-3, 1, -3),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !MacaronWayfinderFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Macaron Wayfinder replaced a block entity");
		prepareSite(level, centre);
		level.setBlock(centre.offset(1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !MacaronWayfinderFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Macaron Wayfinder replaced an authored solid");
		require(helper, !MacaronWayfinderFeature.fitsWithinChunk(
				new BlockPos(helperChunk.getMinBlockX(), centre.getY(),
						helperChunk.getMinBlockZ()), Rotation.NONE, helperChunk),
				"Macaron Wayfinder crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend005world",
			timeoutTicks = 24000)
	public static void focusedNaturalMacaronArchipelagoAudit(
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
				"Could not locate Macaron Archipelago within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundWayfinder found = findWayfinder(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Macaron Wayfinder within 1,089 chunks of "
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
			int[] lamp = MacaronWayfinderFeature.cornerLamps()[0];
			BlockPos sentinel = MacaronWayfinderFeature.local(
					found.centre(), found.rotation(), lamp[0], 4, lamp[1]);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 4, found.centre());
			LOGGER.info("Macaron Archipelago audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, macaronShell={}, marshmallow={}, rockCandy={}, nougatRock={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.macaronShell(),
					audit.marshmallow(), audit.rockCandy(),
					audit.nougatRock(), plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.macaronShell() > 0
							&& audit.marshmallow() > 0
							&& audit.rockCandy() + audit.nougatRock() > 0
							&& plan.complete(brickSentinel),
					"Natural Macaron Archipelago lost layered terrain, geology or its complete Wayfinder: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
						"Could not seed the Wayfinder reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertShellAndFood(
			GameTestHelper helper, ServerLevel level) {
		ChocolateSpongeBlock shell = (ChocolateSpongeBlock)
				CakeWorldBlocks.MACARON_SHELL.get();
		Pig falling = EntityType.PIG.create(level);
		require(helper, falling != null,
				"Could not create Macaron Shell fall fixture");
		falling.setHealth(10.0F);
		shell.fallOn(level, shell.defaultBlockState(), BlockPos.ZERO,
				falling, 11.0F);
		require(helper, close(falling.getHealth(), 8.0D),
				"Macaron Shell lost its forgiving quarter-fall contract");

		BlockPos relative = new BlockPos(1, 2, 1);
		BlockPos absolute = helper.absolutePos(relative);
		helper.setBlock(relative, shell.defaultBlockState());
		ServerPlayer player = new ServerPlayer(level.getServer(), level,
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed5005"),
						"CakeWorldMacaronNibbleTest"));
		player.getFoodData().setFoodLevel(10);
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		InteractionResult nibbled = shell.use(helper.getBlockState(relative),
				level, absolute, player, InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(absolute),
						net.minecraft.core.Direction.UP, absolute, false));
		require(helper, nibbled.consumesAction()
						&& player.getFoodData().getFoodLevel() == 11
						&& helper.getBlockState(relative)
								.getValue(ChocolateSpongeBlock.BITES) == 1,
				"Macaron Shell lost its visible emergency nibble contract");

		FoodProperties raw = CakeWorldItems.MACARON_SHELL_PIECE.get()
				.getFoodProperties();
		FoodProperties prepared = CakeWorldItems.RAINBOW_SKY_MACARON.get()
				.getFoodProperties();
		Recipe<?> pieces = level.getRecipeManager()
				.byKey(id("macaron_shell_piece")).orElse(null);
		Recipe<?> rainbow = level.getRecipeManager()
				.byKey(id("rainbow_sky_macaron")).orElse(null);
		require(helper, raw != null && raw.getNutrition() == 1
						&& close(raw.getSaturationModifier(), 0.1D)
						&& prepared != null && prepared.getNutrition() == 7
						&& close(prepared.getSaturationModifier(), 0.75D)
						&& hasEffect(prepared, MobEffects.SLOW_FALLING, 300)
						&& hasEffect(prepared, MobEffects.ABSORPTION, 160)
						&& pieces != null
						&& pieces.getType() == RecipeType.CRAFTING
						&& pieces.getIngredients().size() == 1
						&& ingredient(pieces, new ItemStack(
								CakeWorldBlocks.MACARON_SHELL.get()))
						&& pieces.getResultItem().is(
								CakeWorldItems.MACARON_SHELL_PIECE.get())
						&& pieces.getResultItem().getCount() == 4
						&& rainbow != null
						&& rainbow.getType() == RecipeType.CRAFTING
						&& rainbow.getIngredients().size() == 4
						&& ingredientCount(rainbow, new ItemStack(
								CakeWorldItems.MACARON_SHELL_PIECE.get())) == 2
						&& ingredient(rainbow, new ItemStack(
								CakeWorldItems.GLOWING_JAM_BERRY.get()))
						&& ingredient(rainbow, new ItemStack(
								CakeWorldItems.ICING_SPOONFUL.get()))
						&& rainbow.getResultItem().is(
								CakeWorldItems.RAINBOW_SKY_MACARON.get())
						&& rainbow.getResultItem().getCount() == 2,
				"Macaron Shell or Rainbow Sky Macaron lost its raw/prepared contract");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 44,
				"Macaron Archipelago requires provider revision 44");
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
								.getAsDouble(), 6.0D)
						&& close(geomes.get("cakeworld:rock_candy_uplift")
								.getAsDouble(), 14.0D)
						&& close(placement.get("weight").getAsDouble(), 0.75D)
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
						&& "cakeworld:macaron_shell".equals(
								surface.get("top_block").getAsString())
						&& "cakeworld:marshmallow".equals(
								surface.get("filler_block").getAsString())
						&& "cakeworld:rock_candy".equals(
								surface.get("underwater_block").getAsString())
						&& surface.get("filler_depth").getAsInt() == 3,
				"Adventure profiles lost their Macaron Archipelago contract");
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
						CakeWorldBlocks.MACARON_SHELL.get()
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
		int shell = 0;
		int raspberry = 0;
		int blueberry = 0;
		int grape = 0;
		int lime = 0;
		int rockCandy = 0;
		int marshmallow = 0;
		int wafer = 0;
		int macaronBricks = 0;
		int glass = 0;
		int endRods = 0;
		int racks = 0;
		int bowls = 0;
		int sentinelBricks = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 4; y++) {
					BlockState state = level.getBlockState(
							MacaronWayfinderFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())) {
						meringueBricks++;
					} else if (state.is(CakeWorldBlocks.MACARON_SHELL.get())) {
						shell++;
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
					} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
						rockCandy++;
					} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
						marshmallow++;
					} else if (state.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
						wafer++;
					} else if (state.is(CakeWorldBlocks.MACARON_BRICKS.get())) {
						macaronBricks++;
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
		boolean cardinal = true;
		for (int distance = 1; distance <= 3; distance++) {
			cardinal &= level.getBlockState(
					centre.offset(0, 1, -distance)).is(
							CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get());
			cardinal &= level.getBlockState(
					centre.offset(distance, 1, 0)).is(
							CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get());
			cardinal &= level.getBlockState(
					centre.offset(0, 1, distance)).is(
							CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get());
			cardinal &= level.getBlockState(
					centre.offset(-distance, 1, 0)).is(
							CakeWorldBlocks.GUMMY_BLOCK.get());
		}
		return new PlanAudit(meringueBricks, shell, raspberry, blueberry,
				grape, lime, rockCandy, marshmallow, wafer, macaronBricks,
				glass, endRods, racks, bowls, sentinelBricks, cardinal);
	}

	private static FoundWayfinder findWayfinder(ServerLevel level,
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
									BlockPos offset = new BlockPos(
											-1, 2, 2).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(offset);
									PlanAudit plan = inspectPlan(level, centre,
											rotation);
									if (plan.identifies(false)) {
										return new FoundWayfinder(centre, rotation);
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
			int radius, BlockPos wayfinder) {
		int biomeSamples = 0;
		int shell = 0;
		int marshmallow = 0;
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
							if (nearWayfinder(cursor, wayfinder)) {
								continue;
							}
							BlockState state = level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks.MACARON_SHELL.get())) {
								shell++;
							} else if (state.is(CakeWorldBlocks.MARSHMALLOW.get())) {
								marshmallow++;
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
		return new NaturalAudit(biomeSamples, shell, marshmallow,
				rockCandy, nougatRock);
	}

	private static boolean nearWayfinder(BlockPos position, BlockPos centre) {
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
				MacaronArchipelagoGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int meringueBricks, int shell,
			int raspberry, int blueberry, int grape, int lime,
			int rockCandy, int marshmallow, int wafer, int macaronBricks,
			int glass, int endRods, int racks, int bowls,
			int sentinelBricks, boolean cardinal) {
		private boolean identifies(boolean exactFoundation) {
			return (exactFoundation ? meringueBricks == 49
					: meringueBricks >= 49)
					&& shell == 36 && raspberry == 3 && blueberry == 3
					&& grape == 3 && lime == 3 && rockCandy == 1
					&& marshmallow == 4 && wafer == 4
					&& macaronBricks == 4 && glass == 4
					&& endRods + sentinelBricks == 4
					&& racks == 1 && bowls == 1 && cardinal;
		}

		private boolean complete(boolean brickSentinel) {
			return identifies(true)
					&& endRods == (brickSentinel ? 3 : 4)
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundWayfinder(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int macaronShell,
			int marshmallow, int rockCandy, int nougatRock) {
	}
}
