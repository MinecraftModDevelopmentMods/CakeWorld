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
import zone.moddev.mc.cakeworld.world.CraterKitchenFeature;
import zone.moddev.mc.cakeworld.world.CrumbMoonDialFeature;

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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
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

/** Contract proof for the complete BIO-END-003 ecosystem slice. */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class MooncakeBarrensGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID = id("mooncake_barrens");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final Rotation[] ROTATIONS = {
			Rotation.NONE,
			Rotation.CLOCKWISE_90,
			Rotation.CLOCKWISE_180,
			Rotation.COUNTERCLOCKWISE_90
	};

	private MooncakeBarrensGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioend003")
	public static void barrensHaveQuietEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome barrens = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY).orElseThrow();
		require(helper, barrens != null
						&& close(barrens.getBaseTemperature(), 0.5D)
						&& close(barrens.getDownfall(), 0.0D),
				"Mooncake Barrens lost their cool, dry End climate");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.VOID,
				BiomeDictionary.Type.DRY,
				BiomeDictionary.Type.SPARSE,
				BiomeDictionary.Type.WASTELAND)) {
			require(helper, BiomeDictionary.hasType(BIOME_KEY, type),
					"Mooncake Barrens lost dictionary type " + type);
		}

		AmbientAdditionsSettings ambience =
				barrens.getAmbientAdditions().orElse(null);
		AmbientParticleSettings particle =
				barrens.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation().equals(
								CakeWorldSounds.MOONCAKE_BARRENS_WHISPER.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& particle != null
						&& particle.getOptions().getType()
								== ParticleTypes.FALLING_HONEY,
				"Mooncake Barrens lost their sparse golden atmosphere");

		MobSpawnSettings.SpawnerData tallwalker = findSpawn(barrens,
				CakeWorldEntities.TAFFY_TALLWALKER.get());
		int totalSpawns = 0;
		for (MobCategory category : MobCategory.values()) {
			totalSpawns += barrens.getMobSettings().getMobs(category)
					.unwrap().size();
		}
		require(helper, tallwalker != null
						&& tallwalker.getWeight().asInt() == 10
						&& tallwalker.minCount == 4
						&& tallwalker.maxCount == 4
						&& findSpawn(barrens, EntityType.ENDERMAN) == null
						&& totalSpawns == 1,
				"Mooncake Barrens lost their exact inherited Tallwalker ecology: "
						+ totalSpawns);

		TagKey<Biome> kitchenTag = CraterKitchenFeature.GENERATES_IN;
		Holder<Biome> meringue = registry.getHolder(ResourceKey.create(
				Registry.BIOME_REGISTRY,
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())).orElseThrow();
		require(helper, holder.is(kitchenTag) && !meringue.is(kitchenTag),
				"Crater Kitchen did not migrate exclusively to Mooncake Barrens");
		assertCrust(helper, level);
		assertMooncake(helper, level);
		assertProvider(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend003")
	public static void crumbMoonDialIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed = CrumbMoonDialFeature.placedFeature();
		Biome barrens = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY).get(BIOME_ID);
		require(helper, placed != null
						&& placed.value().feature().value().feature()
								== CrumbMoonDialFeature.FEATURE
						&& placed.value().placement().size() == 4
						&& placed.value().placement().get(0)
								instanceof RarityFilter
						&& placed.value().placement().get(1)
								instanceof InSquarePlacement
						&& placed.value().placement().get(2)
								instanceof HeightmapPlacement
						&& placed.value().placement().get(3)
								instanceof BiomeFilter
						&& CrumbMoonDialFeature.AVERAGE_CHUNKS_PER_ATTEMPT == 2
						&& CrumbMoonDialFeature.MAX_TERRAIN_RELIEF == 6
						&& hasPlacedFeature(barrens, placed),
				"Crumb Moon Dial lost its bounded placement chain");

		ChunkPos helperChunk = new ChunkPos(helper.absolutePos(
				new BlockPos(8, 5, 8)));
		BlockPos centre = new BlockPos(helperChunk.getMinBlockX() + 8,
				63, helperChunk.getMinBlockZ() + 8);
		for (Rotation rotation : ROTATIONS) {
			prepareSite(level, centre);
			Set<Integer> entitiesBefore = entityIds(level, centre);
			require(helper,
					CrumbMoonDialFeature.hasSafeFootprint(
							level, centre, rotation)
							&& CrumbMoonDialFeature.buildAt(
									level, centre, rotation),
					"Crumb Moon Dial rejected safe rotation " + rotation);
			PlanAudit plan = inspectPlan(level, centre, rotation);
			require(helper, plan.complete(false, true),
					"Crumb Moon Dial plan changed for " + rotation
							+ ": " + plan);
			require(helper, entitiesBefore.equals(entityIds(level, centre))
						&& countBlockEntities(level, centre) == 0,
					"Crumb Moon Dial created an entity or block entity");
		}

		prepareSite(level, centre);
		level.setBlock(centre.offset(3, 1, 3),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper, !CrumbMoonDialFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Crumb Moon Dial replaced an existing fluid");
		prepareSite(level, centre);
		level.setBlock(centre.offset(-3, 1, -3),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper, !CrumbMoonDialFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Crumb Moon Dial replaced a block entity");
		prepareSite(level, centre);
		level.setBlock(centre.offset(1, 1, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper, !CrumbMoonDialFeature.hasSafeFootprint(
				level, centre, Rotation.NONE),
				"Crumb Moon Dial replaced an authored solid");
		require(helper, !CrumbMoonDialFeature.fitsWithinChunk(
				new BlockPos(helperChunk.getMinBlockX(), centre.getY(),
						helperChunk.getMinBlockZ()), Rotation.NONE, helperChunk),
				"Crumb Moon Dial crossed its generating chunk");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioend003world",
			timeoutTicks = 24000)
	public static void focusedNaturalMooncakeBarrensAudit(
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
				"Could not locate Mooncake Barrens within 32,768 blocks");
		ChunkPos anchor = new ChunkPos(match.getFirst());
		FoundDial found = findDial(level, anchor, 16);
		require(helper, found != null,
				"Could not find a natural Crumb Moon Dial within 1,089 chunks of "
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
			int[] light = CrumbMoonDialFeature.lights()[0];
			BlockPos sentinel = CrumbMoonDialFeature.local(
					found.centre(), found.rotation(), light[0], 4, light[1]);
			boolean brickSentinel = level.getBlockState(sentinel)
					.is(Blocks.BRICKS);
			NaturalAudit audit = audit(level, foundChunk, 4, found.centre());
			LOGGER.info("Mooncake Barrens audit: anchorChunk={}, centre={}, rotation={}, biomeSamples={}, mooncakeCrust={}, biscuitStone={}, nougatRock={}, rockCandy={}, plan={}, brickSentinel={}, sentinel={}",
					anchor, found.centre(), found.rotation(),
					audit.biomeSamples(), audit.mooncakeCrust(),
					audit.biscuitStone(), audit.nougatRock(),
					audit.rockCandy(), plan, brickSentinel, sentinel);
			require(helper, audit.biomeSamples() >= 128
							&& audit.mooncakeCrust() > 0
							&& audit.biscuitStone() > 0
							&& audit.nougatRock() + audit.rockCandy() > 0
							&& plan.complete(brickSentinel, false),
					"Natural Mooncake Barrens lost terrain, geology or their complete Dial: "
							+ audit + " / " + plan);
			if (!brickSentinel) {
				level.setBlock(sentinel, Blocks.BRICKS.defaultBlockState(), 2);
				require(helper, level.getBlockState(sentinel).is(Blocks.BRICKS),
						"Could not seed the Crumb Moon Dial reload sentinel");
			}
			level.setChunkForced(foundChunk.x, foundChunk.z, false);
			helper.succeed();
		});
	}

	private static void assertCrust(GameTestHelper helper, ServerLevel level) {
		ChocolateSpongeBlock crust = (ChocolateSpongeBlock)
				CakeWorldBlocks.MOONCAKE_CRUST.get();
		Pig falling = EntityType.PIG.create(level);
		require(helper, falling != null,
				"Could not create Mooncake Crust fall fixture");
		falling.setHealth(10.0F);
		crust.fallOn(level, crust.defaultBlockState(), BlockPos.ZERO,
				falling, 11.0F);
		require(helper, close(falling.getHealth(), 8.0D),
				"Mooncake Crust lost its forgiving quarter-fall contract");

		BlockPos relative = new BlockPos(1, 2, 1);
		BlockPos absolute = helper.absolutePos(relative);
		helper.setBlock(relative, crust.defaultBlockState());
		ServerPlayer player = new ServerPlayer(level.getServer(), level,
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed5003"),
						"CakeWorldMooncakeNibbleTest"));
		player.getFoodData().setFoodLevel(10);
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		InteractionResult nibbled = crust.use(helper.getBlockState(relative),
				level, absolute, player, InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP,
						absolute, false));
		require(helper, nibbled.consumesAction()
						&& player.getFoodData().getFoodLevel() == 11
						&& helper.getBlockState(relative)
								.getValue(ChocolateSpongeBlock.BITES) == 1,
				"Mooncake Crust lost its visible emergency nibble contract");
	}

	private static void assertMooncake(GameTestHelper helper,
			ServerLevel level) {
		FoodProperties food = CakeWorldItems.MOONCAKE.get().getFoodProperties();
		Recipe<?> recipe = level.getRecipeManager()
				.byKey(id("mooncake")).orElse(null);
		require(helper, food != null
						&& food.getNutrition() == 8
						&& close(food.getSaturationModifier(), 0.8D)
						&& recipe != null
						&& recipe.getType() == RecipeType.CRAFTING
						&& recipe.getIngredients().size() == 4
						&& ingredient(recipe, new ItemStack(Items.POPPED_CHORUS_FRUIT))
						&& recipe.getResultItem().is(CakeWorldItems.MOONCAKE.get())
						&& recipe.getResultItem().getCount() == 2,
				"Mooncake lost its worthwhile End-gated prepared-food contract");
	}

	private static void assertProvider(GameTestHelper helper) {
		JsonObject provider = readProvider();
		require(helper, provider.get("provider_revision").getAsInt() >= 43,
				"Mooncake Barrens require provider revision 43");
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
									.getAsDouble(), 10.0D)
							&& close(geomes.get("cakeworld:rock_candy_uplift")
									.getAsDouble(), 6.0D)
							&& "minecraft:the_end".equals(
									end.get("dimension").getAsString())
							&& "replace".equals(end.get("mode").getAsString())
							&& end.getAsJsonObject("biomes").size() == 7
							&& close(placement.get("weight").getAsDouble(), 1.5D)
							&& strings(placement.getAsJsonArray("similar_biomes"))
									.equals(Set.of("minecraft:end_barrens"))
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
							&& "cakeworld:mooncake_crust".equals(
									surface.get("top_block").getAsString())
							&& "cakeworld:biscuit_stone".equals(
									surface.get("filler_block").getAsString())
							&& "cakeworld:biscuit_stone".equals(
									surface.get("underwater_block").getAsString())
							&& surface.get("filler_depth").getAsInt() == 5,
					template + " lost its Mooncake Barrens contract");
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
				new AABB(centre.offset(-4, -1, -4), centre.offset(5, 7, 5)))
				.forEach(Entity::discard);
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				level.setBlock(centre.offset(x, -1, z),
						Blocks.END_STONE.defaultBlockState(), 2);
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.MOONCAKE_CRUST.get()
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
		int mooncakeCrust = 0;
		int biscuitCrumbs = 0;
		int macaronBricks = 0;
		int rockCandy = 0;
		int wafer = 0;
		int glass = 0;
		int endRods = 0;
		int coolingRacks = 0;
		int mixingBowls = 0;
		int sentinelBricks = 0;
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				for (int y = 0; y <= 5; y++) {
					BlockState state = level.getBlockState(
							CrumbMoonDialFeature.local(
									centre, rotation, x, y, z));
					if (state.is(CakeWorldBlocks.MERINGUE_BRICKS.get())) {
						meringueBricks++;
					} else if (state.is(CakeWorldBlocks.MOONCAKE_CRUST.get())) {
						mooncakeCrust++;
					} else if (state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())) {
						biscuitCrumbs++;
					} else if (state.is(CakeWorldBlocks.MACARON_BRICKS.get())) {
						macaronBricks++;
					} else if (state.is(CakeWorldBlocks.ROCK_CANDY.get())) {
						rockCandy++;
					} else if (state.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
						wafer++;
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
		return new PlanAudit(meringueBricks, mooncakeCrust,
				biscuitCrumbs, macaronBricks, rockCandy, wafer,
				glass, endRods, coolingRacks, mixingBowls,
				sentinelBricks);
	}

	private static FoundDial findDial(ServerLevel level,
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
											2, 2, 2).rotate(rotation);
									BlockPos centre = cursor.immutable()
											.subtract(markerOffset);
									PlanAudit plan = inspectPlan(level, centre,
											rotation);
									if (plan.identifiesDial(false)) {
										return new FoundDial(centre, rotation);
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
			int radius, BlockPos dial) {
		int biomeSamples = 0;
		int mooncakeCrust = 0;
		int biscuitStone = 0;
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
							if (nearDial(cursor, dial)) {
								continue;
							}
							BlockState state = level.getBlockState(cursor);
							if (state.is(CakeWorldBlocks.MOONCAKE_CRUST.get())) {
								mooncakeCrust++;
							} else if (state.is(CakeWorldBlocks.BISCUIT_STONE.get())) {
								biscuitStone++;
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
		return new NaturalAudit(biomeSamples, mooncakeCrust,
				biscuitStone, nougatRock, rockCandy);
	}

	private static boolean nearDial(BlockPos position, BlockPos centre) {
		return Math.abs(position.getX() - centre.getX()) <= 3
				&& position.getY() >= centre.getY()
				&& position.getY() <= centre.getY() + 5
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

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element -> values.add(element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				MooncakeBarrensGameTests.class.getResourceAsStream(
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

	private record PlanAudit(int meringueBricks, int mooncakeCrust,
			int biscuitCrumbs, int macaronBricks, int rockCandy,
			int wafer, int glass, int endRods, int coolingRacks,
			int mixingBowls, int sentinelBricks) {
		private boolean identifiesDial(boolean exactFoundation) {
			return (exactFoundation ? meringueBricks == 49
					: meringueBricks >= 49)
					&& mooncakeCrust == 24 && biscuitCrumbs == 9
					&& macaronBricks == 16 && rockCandy == 9
					&& wafer == 2 && glass == 4
					&& endRods + sentinelBricks == 4
					&& coolingRacks == 1 && mixingBowls == 1;
		}

		private boolean complete(boolean brickSentinel,
				boolean exactFoundation) {
			return identifiesDial(exactFoundation)
					&& endRods == (brickSentinel ? 3 : 4)
					&& sentinelBricks == (brickSentinel ? 1 : 0);
		}
	}

	private record FoundDial(BlockPos centre, Rotation rotation) {
	}

	private record NaturalAudit(int biomeSamples, int mooncakeCrust,
			int biscuitStone, int nougatRock, int rockCandy) {
	}
}
