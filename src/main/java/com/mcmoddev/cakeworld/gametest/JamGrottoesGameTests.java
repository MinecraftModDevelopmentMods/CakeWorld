package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.CakeLiquidBlock;
import com.mcmoddev.cakeworld.block.JamGlowVineBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.JamLanternWalkFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Contract proof for BIO-OW-020 and STRUCT-036.
 *
 * <p>The integrated fluid evidence targets the covered-deposit feature, and
 * authored landmark Jam is excluded from that count. OS-104 separately proves
 * the repaired OreSpawn 4.0.2 spring path.</p>
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class JamGrottoesGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("jam_grottoes");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final ResourceLocation LUSH_CAVES =
			new ResourceLocation("minecraft", "lush_caves");
	private static final ResourceLocation COOKIE_FOREST =
			id("cookie_forest");

	private JamGrottoesGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow020")
	public static void grottoesHaveRenewableEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome grottoes = registry.get(BIOME_ID);
		Biome lush = registry.get(LUSH_CAVES);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, grottoes != null
						&& lush != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.UNDERGROUND
						&& close(grottoes.getBaseTemperature(), 0.7D)
						&& close(grottoes.getDownfall(), 0.9D),
				"Jam Grottoes are not a Lush-Caves-derived underground biome");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.OVERWORLD,
				BiomeDictionary.Type.UNDERGROUND,
				BiomeDictionary.Type.LUSH,
				BiomeDictionary.Type.WET,
				BiomeDictionary.Type.MAGICAL)) {
			require(helper,
					BiomeDictionary.hasType(BIOME_KEY, type),
					"Jam Grottoes lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				grottoes.getAmbientAdditions().orElse(null);
		AmbientParticleSettings spores =
				grottoes.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.JAM_GROTTOES_DRIP.getId())
						&& close(ambience.getTickChance(), 0.0014D)
						&& spores != null
						&& spores.getOptions().getType()
								== ParticleTypes.SPORE_BLOSSOM_AIR,
				"Jam Grottoes lost their subtitled spore-and-drip ambience");

		assertCopiedReplacement(helper, lush, grottoes,
				EntityType.AXOLOTL,
				CakeWorldEntities.JELLYLOTL.get(),
				MobCategory.AXOLOTLS);
		assertCopiedReplacement(helper, lush, grottoes,
				EntityType.TROPICAL_FISH,
				CakeWorldEntities.JELLYBEAN_FISH.get(),
				MobCategory.WATER_AMBIENT);
		assertCopiedReplacement(helper, lush, grottoes,
				EntityType.GLOW_SQUID,
				CakeWorldEntities.GLOW_JELLY.get(),
				MobCategory.UNDERGROUND_WATER_CREATURE);
		for (EntityType<?> vanilla : List.of(
				EntityType.ZOMBIE,
				EntityType.ZOMBIE_VILLAGER,
				EntityType.SKELETON,
				EntityType.CREEPER,
				EntityType.SPIDER,
				EntityType.WITCH,
				EntityType.SLIME,
				EntityType.ENDERMAN,
				EntityType.BAT)) {
			require(helper, findSpawn(grottoes, vanilla) == null,
					"Jam Grottoes retained vanilla spawn "
							+ vanilla.getRegistryName());
		}
		require(helper,
				hasPlacedFeature(grottoes,
						JamLanternWalkFeature.ID),
				"Jam Grottoes lost their Jam Lantern Walk");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.ROCK_CANDY_CAVERNS.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							JamLanternWalkFeature.ID),
					"Jam Lantern Walk leaked into " + other);
		}

		FoodProperties berry =
				CakeWorldItems.GLOWING_JAM_BERRY.get()
						.getFoodProperties();
		FoodProperties toast =
				CakeWorldItems.GROTTO_JAM_TOAST.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				level.getRecipeManager()
						.byKey(id("grotto_jam_toast"))
						.orElse(null);
		ItemStack biscuit = new ItemStack(
				CakeWorldItems.SIMPLE_BISCUIT.get());
		ItemStack jamBerry = new ItemStack(
				CakeWorldItems.GLOWING_JAM_BERRY.get());
		ItemStack jamBucket = new ItemStack(
				CakeWorldFluids.JAM_BUCKET.get());
		require(helper, berry != null
						&& berry.getNutrition() == 2
						&& close(berry.getSaturationModifier(), 0.2D)
						&& toast != null
						&& toast.getNutrition() == 6
						&& close(toast.getSaturationModifier(), 0.6D)
						&& hasEffect(toast,
								MobEffects.NIGHT_VISION, 240)
						&& hasEffect(toast,
								MobEffects.ABSORPTION, 160)
						&& recipe != null
						&& recipe.getIngredients().size() == 5
						&& matchingIngredients(recipe, biscuit) == 2
						&& matchingIngredients(recipe, jamBerry) == 2
						&& matchingIngredients(recipe, jamBucket) == 1
						&& recipe.getResultItem().is(
								CakeWorldItems.GROTTO_JAM_TOAST.get())
						&& recipe.getResultItem().getCount() == 2
						&& CakeWorldFluids.JAM_BUCKET.get()
								.hasCraftingRemainingItem()
						&& CakeWorldFluids.JAM_BUCKET.get()
								.getCraftingRemainingItem()
								== Items.BUCKET,
				"Grotto Jam Toast lost its useful two-serving recipe, cave effects or bucket return");

		BlockPos dragPos =
				helper.absolutePos(new BlockPos(2, 3, 2));
		ArmorStand stand = EntityType.ARMOR_STAND.create(level);
		require(helper, stand != null,
				"Could not create the Jam drag fixture");
		stand.setDeltaMovement(1.0D, -1.0D, 1.0D);
		((CakeLiquidBlock) CakeWorldFluids.JAM_BLOCK.get())
				.entityInside(
						CakeWorldFluids.JAM_BLOCK.get()
								.defaultBlockState(),
						level, dragPos, stand);
		require(helper,
				close(stand.getDeltaMovement().x, 0.4D)
						&& close(stand.getDeltaMovement().y, -0.7D)
						&& close(stand.getDeltaMovement().z, 0.4D),
				"Strawberry Jam lost its safe horizontal and downward drag");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 30,
				"Jam Grottoes require provider revision 30");
		JsonObject templates =
				provider.getAsJsonObject("templates");
		JsonObject firstPalette = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = templates
					.getAsJsonObject(template)
					.getAsJsonObject("profile");
			JsonObject geomes = profile
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject cavePalette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject(
							"cakeworld:overworld_caves");
			JsonObject placement = cavePalette
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject jamDeposit = profile
					.getAsJsonObject("fluid_deposits")
					.getAsJsonObject(
							"cakeworld:fluid_deposit/jam")
					.getAsJsonObject("dimensions")
					.getAsJsonObject("minecraft:overworld");
			require(helper,
					geomes.size() == 2
							&& close(geomes.get(
									"cakeworld:cocoa_basin")
									.getAsDouble(), 18.0D)
							&& close(geomes.get(
									"cakeworld:wafer_shelf")
									.getAsDouble(), 6.0D)
							&& close(placement.get("weight")
									.getAsDouble(), 1.0D)
							&& strings(placement
									.getAsJsonArray("similar_biomes"))
									.equals(Set.of(
											"minecraft:lush_caves"))
							&& !placement.has("surface")
							&& jamDeposit.get(
									"min_solid_cover")
									.getAsInt() == 3
							&& strings(jamDeposit
									.getAsJsonArray("biome_ids"))
									.equals(Set.of(
											"cakeworld:cookie_forest")),
					template
							+ " lost its 3D Jam Grotto selector or covered-deposit contract");
			if (firstPalette == null) {
				firstPalette = cavePalette;
			} else {
				require(helper, firstPalette.equals(cavePalette),
						"Normal and BaseMetals cave profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow020",
			timeoutTicks = 800)
	public static void lanternWalkIsBoundedRenewableAndNonDestructive(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				JamLanternWalkFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== JamLanternWalkFeature.FEATURE
						&& JamLanternWalkFeature.MIN_Y == -48
						&& JamLanternWalkFeature.MAX_Y == 48,
				"Jam Lantern Walk registration or cave bounds changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 2
						&& modifiers.get(0)
								instanceof InSquarePlacement
						&& modifiers.get(1)
								instanceof HeightRangePlacement,
				"Jam Lantern Walk lost its every-grotto-chunk bounded chain");

		BlockPos helperPos =
				helper.absolutePos(new BlockPos(4, 4, 4));
		ChunkPos chunk = new ChunkPos(helperPos);
		BlockPos fixture = new BlockPos(
				chunk.getMinBlockX() + 7,
				40,
				chunk.getMinBlockZ() + 7);
		Set<Rotation> orientations = new HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					JamLanternWalkFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83,
									index % 64 - 16,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Jam Lantern Walk did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					JamLanternWalkFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Jam Lantern Walk crossed its generating chunk");
			prepare(level, fixture, rotation);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					JamLanternWalkFeature.buildAt(
							level, fixture, rotation),
					"Jam Lantern Walk refused a safe cave fixture for "
							+ rotation);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					matchesWalk(level, fixture,
							rotation, false)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GINGERBREAD_BRICKS.get(),
									0) == 71
							&& palette.getOrDefault(
									CakeWorldFluids.JAM_BLOCK.get(),
									0) == 14
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW.get(),
									0) == 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.JAM_GLOW_VINE.get(),
									0) == 12
							&& palette.getOrDefault(
									CakeWorldBlocks.CANDY_GLASS.get(),
									0) == 4
							&& countBlockEntities(
									level, fixture) == 0
							&& level.getEntities((Entity) null,
									new AABB(fixture).inflate(8.0D))
									.size() == entities,
					"Jam Lantern Walk lost its exact contained plan or created entities/block entities: "
							+ palette);
		}

		prepare(level, fixture, Rotation.NONE);
		require(helper,
				JamLanternWalkFeature.buildAt(
						level, fixture, Rotation.NONE),
				"Could not prepare renewable Jam-vine fixture");
		int[] vineColumn =
				JamLanternWalkFeature.vineColumns()[0];
		BlockPos vinePos =
				JamLanternWalkFeature.local(
						fixture, Rotation.NONE,
						vineColumn[0], 1,
						vineColumn[1]);
		JamGlowVineBlock vine =
				(JamGlowVineBlock) CakeWorldBlocks
						.JAM_GLOW_VINE.get();
		FakePlayer player = FakePlayerFactory.getMinecraft(level);
		BlockState mature = level.getBlockState(vinePos);
		vine.use(mature, level, vinePos, player,
				InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(vinePos),
						net.minecraft.core.Direction.UP,
						vinePos, false));
		require(helper,
				level.getBlockState(vinePos).is(vine)
						&& level.getBlockState(vinePos)
								.getValue(JamGlowVineBlock.AGE)
								== JamGlowVineBlock.PICKED_AGE
						&& level.getEntitiesOfClass(
								ItemEntity.class,
								new AABB(vinePos).inflate(2.0D))
								.stream().anyMatch(item ->
										item.getItem().is(
												CakeWorldItems
														.GLOWING_JAM_BERRY
														.get())
										&& item.getItem()
												.getCount() >= 2),
				"Picking a ripe Jam Glow Vine did not preserve the plant and yield berries");
		for (int attempt = 0; attempt < 3
				&& level.getBlockState(vinePos)
						.getValue(JamGlowVineBlock.AGE) < 3;
				attempt++) {
			BlockState growing =
					level.getBlockState(vinePos);
			vine.performBonemeal(level, level.random,
					vinePos, growing);
		}
		require(helper,
				level.getBlockState(vinePos)
						.getValue(JamGlowVineBlock.AGE) == 3,
				"Bone meal did not renew a picked Jam Glow Vine");

		prepare(level, fixture, Rotation.NONE);
		BlockPos obstacle =
				JamLanternWalkFeature.local(
						fixture, Rotation.NONE,
						-3, 0, -3);
		for (BlockState rejected : List.of(
				Blocks.WATER.defaultBlockState(),
				Blocks.CHEST.defaultBlockState(),
				Blocks.BEDROCK.defaultBlockState())) {
			prepare(level, fixture, Rotation.NONE);
			level.setBlock(obstacle, rejected, 2);
			require(helper,
					!JamLanternWalkFeature.hasSafeSite(
							level, fixture,
							Rotation.NONE),
					"Jam Lantern Walk accepted forbidden obstacle "
							+ rejected);
		}
		prepare(level, fixture, Rotation.NONE);
		require(helper,
				JamLanternWalkFeature.buildAt(
						level, fixture, Rotation.NONE),
				"Could not prepare the edit-persistence fixture");
		level.setBlock(vinePos,
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!JamLanternWalkFeature.buildAt(
						level, fixture, Rotation.NONE)
						&& level.getBlockState(vinePos)
								.is(Blocks.BRICKS),
				"Jam Lantern Walk regenerated over a player edit");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow020world",
			timeoutTicks = 24000)
	public static void focusedNaturalJamGrottoesAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos grottoAnchor =
				locateBiome(helper, level, BIOME_ID, 0);
		LocatedWalk walk = locateNaturalWalk(
				helper, level, grottoAnchor, 8);
		BlockPos cookieAnchor =
				locateBiome(helper, level, COOKIE_FOREST, 64);
		CoveredJamAudit jam = auditCoveredJam(
				level, cookieAnchor, 8);
		ChunkPos chunk = new ChunkPos(walk.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			int[] sentinelColumn =
					JamLanternWalkFeature.vineColumns()[0];
			BlockPos sentinel =
					JamLanternWalkFeature.local(
							walk.centre(),
							walk.rotation(),
							sentinelColumn[0], 1,
							sentinelColumn[1]);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			require(helper,
					matchesWalk(level, walk.centre(),
							walk.rotation(),
							brickSentinel),
					"Natural Jam Lantern Walk lost its complete plan");
			ResourceLocation biome = level.getBiome(
					walk.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			LOGGER.info("Jam Grottoes audit: centre={}, grottoAnchor={}, cookieAnchor={}, biome={}, rotation={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, grottoSamples={}, coveredDeposit={}",
					walk.centre(), grottoAnchor,
					cookieAnchor, biome,
					walk.rotation(), brickSentinel,
					walk.scannedChunks(),
					walk.markerCandidates(),
					walk.grottoSamples(), jam);
			require(helper,
					BIOME_ID.equals(biome)
							&& walk.grottoSamples() >= 16
							&& countBlockEntities(
									level, walk.centre()) == 0
							&& jam.cookieChunks() > 0
							&& jam.jamCells() > 0
							&& jam.coveredJamCells() > 0,
					"Natural Jam Grottoes lost their 3D biome, independent covered deposit or complete landmark: "
							+ jam);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Jam Lantern Walk");
			}
			level.setChunkForced(chunk.x, chunk.z, false);
			helper.succeed();
		});
	}

	private static BlockPos locateBiome(
			GameTestHelper helper, ServerLevel level,
			ResourceLocation biomeId, int y) {
		Pair<BlockPos, Holder<Biome>> match =
				level.findNearestBiome(
						holder -> holder.unwrapKey()
								.map(key -> key.location()
										.equals(biomeId))
								.orElse(false),
						new BlockPos(0, y, 0),
						16384, 8);
		require(helper, match != null,
				"Could not locate " + biomeId
						+ " within 16,384 blocks at Y " + y);
		return match.getFirst();
	}

	private static LocatedWalk locateNaturalWalk(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int grottoSamples = 0;
		for (int radius = 0; radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius; chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius; chunkZ++) {
					if (radius > 0
							&& chunkX != anchorChunk.x - radius
							&& chunkX != anchorChunk.x + radius
							&& chunkZ != anchorChunk.z - radius
							&& chunkZ != anchorChunk.z + radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					int minX = chunkX << 4;
					int minZ = chunkZ << 4;
					int[][] centres = {
						{minX + 7, minZ + 7},
						{minX + 8, minZ + 8},
						{minX + 7, minZ + 8},
						{minX + 8, minZ + 7}
					};
					for (int[] horizontal : centres) {
						for (int y = JamLanternWalkFeature.MIN_Y;
								y <= JamLanternWalkFeature.MAX_Y;
								y++) {
							BlockPos centre = new BlockPos(
									horizontal[0], y,
									horizontal[1]);
							if ((y & 3) == 0
									&& level.getBiome(centre)
											.is(BIOME_KEY)) {
								grottoSamples++;
							}
							if (!level.getBlockState(
									centre.above())
									.is(CakeWorldBlocks
											.MIXING_BOWL.get())) {
								continue;
							}
							markerCandidates++;
							Rotation rotation =
									JamLanternWalkFeature
											.orientation(
													level.getSeed(),
													centre);
							if (matchesWalk(level, centre,
									rotation, true)) {
								return new LocatedWalk(
										centre, rotation,
										scannedChunks,
										markerCandidates,
										grottoSamples);
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Jam Grotto survey found no natural Jam Lantern Walk after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " mixing-bowl candidates near "
						+ anchor + "; grottoSamples="
						+ grottoSamples);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static CoveredJamAudit auditCoveredJam(
			ServerLevel level, BlockPos anchor,
			int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int cookieChunks = 0;
		int jamCells = 0;
		int coveredJamCells = 0;
		for (int chunkX = anchorChunk.x - chunkRadius;
				chunkX <= anchorChunk.x + chunkRadius; chunkX++) {
			for (int chunkZ = anchorChunk.z - chunkRadius;
					chunkZ <= anchorChunk.z + chunkRadius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				BlockPos sample = new BlockPos(
						(chunkX << 4) + 8, 64,
						(chunkZ << 4) + 8);
				if (level.getBiome(sample).unwrapKey()
						.map(ResourceKey::location)
						.filter(COOKIE_FOREST::equals)
						.isPresent()) {
					cookieChunks++;
				}
				for (int x = chunkX << 4;
						x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4;
							z < (chunkZ + 1) << 4; z++) {
						for (int y = -40; y <= 48; y++) {
							BlockPos pos =
									new BlockPos(x, y, z);
							if (!level.getBlockState(pos)
									.is(CakeWorldFluids
											.JAM_BLOCK.get())) {
								continue;
							}
							jamCells++;
							boolean covered = true;
							for (int cover = 1;
									cover <= 3; cover++) {
								BlockState above =
										level.getBlockState(
												pos.above(cover));
								if (above.isAir()
										|| !level.getFluidState(
												pos.above(cover))
												.isEmpty()) {
									covered = false;
									break;
								}
							}
							if (covered) {
								coveredJamCells++;
							}
						}
					}
				}
			}
		}
		return new CoveredJamAudit(cookieChunks,
				jamCells, coveredJamCells);
	}

	private static boolean matchesWalk(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		int[] sentinel =
				JamLanternWalkFeature.vineColumns()[0];
		for (int x = -4; x <= 4; x++) {
			for (int z = -1; z <= 1; z++) {
				Block expected = z == 0
						&& (x == -1 || x == 1)
								? CakeWorldBlocks.MARSHMALLOW.get()
								: CakeWorldBlocks
										.GINGERBREAD_BRICKS.get();
				if (!state(level, centre, rotation,
						x, 0, z).is(expected)) {
					return false;
				}
			}
		}
		for (int side : new int[] {-1, 1}) {
			int channelZ = side * 3;
			for (int x = -3; x <= 3; x++) {
				if (!state(level, centre, rotation,
						x, -1, channelZ).is(
								CakeWorldBlocks
										.GINGERBREAD_BRICKS.get())
						|| !state(level, centre, rotation,
								x, 0, side * 2).is(
										CakeWorldBlocks
												.GINGERBREAD_BRICKS
												.get())
						|| !state(level, centre, rotation,
								x, 0, side * 4).is(
										CakeWorldBlocks
												.GINGERBREAD_BRICKS
												.get())
						|| !state(level, centre, rotation,
								x, 0, channelZ).is(
										CakeWorldFluids
												.JAM_BLOCK.get())) {
					return false;
				}
			}
			for (int x : new int[] {-4, 4}) {
				if (!state(level, centre, rotation,
						x, 0, channelZ).is(
								CakeWorldBlocks
										.GINGERBREAD_BRICKS.get())) {
					return false;
				}
			}
		}
		for (int[] column :
				JamLanternWalkFeature.vineColumns()) {
			if (!state(level, centre, rotation,
					column[0], 4, column[1]).is(
							CakeWorldBlocks.CANDY_GLASS.get())) {
				return false;
			}
			for (int y = 1; y <= 3; y++) {
				BlockState vine = state(level, centre,
						rotation, column[0], y,
						column[1]);
				boolean isSentinel = allowBrickSentinel
						&& column[0] == sentinel[0]
						&& column[1] == sentinel[1]
						&& y == 1
						&& vine.is(Blocks.BRICKS);
				if (!isSentinel
						&& (!vine.is(CakeWorldBlocks
								.JAM_GLOW_VINE.get())
								|| vine.getValue(
										JamGlowVineBlock.AGE)
										!= 3)) {
					return false;
				}
			}
		}
		return state(level, centre, rotation,
				0, 1, 0).is(CakeWorldBlocks.MIXING_BOWL.get())
				&& state(level, centre, rotation,
						0, 1, 1).is(
								CakeWorldBlocks.COOLING_RACK.get());
	}

	private static void prepare(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		for (int x = -7; x <= 7; x++) {
			for (int y = -4; y <= 6; y++) {
				for (int z = -6; z <= 6; z++) {
					level.setBlock(
							JamLanternWalkFeature.local(
									centre, rotation,
									x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
		for (int y = -3; y <= 5; y++) {
			for (int x = -6; x <= 6; x++) {
				for (int z : new int[] {-5, 5}) {
					level.setBlock(
							JamLanternWalkFeature.local(
									centre, rotation,
									x, y, z),
							CakeWorldBlocks.BISCUIT_STONE.get()
									.defaultBlockState(),
							2);
				}
			}
			for (int z = -4; z <= 4; z++) {
				for (int x : new int[] {-6, 6}) {
					level.setBlock(
							JamLanternWalkFeature.local(
									centre, rotation,
									x, y, z),
							CakeWorldBlocks.BISCUIT_STONE.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = -1; y <= 5; y++) {
				for (int z = -5; z <= 5; z++) {
					Block block = level.getBlockState(
							centre.offset(x, y, z))
							.getBlock();
					palette.merge(block, 1, Integer::sum);
				}
			}
		}
		return palette;
	}

	private static int countBlockEntities(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -6; x <= 6; x++) {
			for (int y = -2; y <= 5; y++) {
				for (int z = -6; z <= 6; z++) {
					if (level.getBlockEntity(
							centre.offset(x, y, z)) != null) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private static BlockState state(ServerLevel level,
			BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return level.getBlockState(
				JamLanternWalkFeature.local(
						centre, rotation, x, y, z));
	}

	private static void assertCopiedReplacement(
			GameTestHelper helper, Biome source, Biome target,
			EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category) {
		MobSpawnSettings.SpawnerData original =
				findSpawn(source, vanilla);
		MobSpawnSettings.SpawnerData converted =
				findSpawn(target, replacement);
		require(helper, original != null
						&& converted != null
						&& replacement.getCategory() == category
						&& converted.getWeight().asInt()
								== original.getWeight().asInt()
						&& converted.minCount == original.minCount
						&& converted.maxCount == original.maxCount
						&& findSpawn(target, vanilla) == null,
				"Jam Grottoes lost the exact copied replacement "
						+ replacement.getRegistryName()
						+ " for " + vanilla.getRegistryName());
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type) {
		for (MobCategory category : MobCategory.values()) {
			for (MobSpawnSettings.SpawnerData spawn
					: biome.getMobSettings()
							.getMobs(category)
							.unwrap()) {
				if (spawn.type == type) {
					return spawn;
				}
			}
		}
		return null;
	}

	private static boolean hasPlacedFeature(Biome biome,
			ResourceLocation expected) {
		int step = GenerationStep.Decoration
				.TOP_LAYER_MODIFICATION.ordinal();
		if (biome == null
				|| biome.getGenerationSettings()
						.features().size() <= step) {
			return false;
		}
		for (Holder<PlacedFeature> feature
				: biome.getGenerationSettings()
						.features().get(step)) {
			if (feature.unwrapKey()
					.map(key -> key.location().equals(expected))
					.orElse(false)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasEffect(FoodProperties food,
			net.minecraft.world.effect.MobEffect effect,
			int duration) {
		return food.getEffects().stream()
				.anyMatch(entry ->
						entry.getFirst().getEffect() == effect
								&& entry.getFirst()
										.getDuration() == duration);
	}

	private static long matchingIngredients(
			net.minecraft.world.item.crafting.Recipe<?> recipe,
			ItemStack stack) {
		return recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.test(stack))
				.count();
	}

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element -> values.add(
				element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				JamGrottoesGameTests.class
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

	private record LocatedWalk(
			BlockPos centre,
			Rotation rotation,
			int scannedChunks,
			int markerCandidates,
			int grottoSamples) {
	}

	private record CoveredJamAudit(
			int cookieChunks,
			int jamCells,
			int coveredJamCells) {
	}
}
