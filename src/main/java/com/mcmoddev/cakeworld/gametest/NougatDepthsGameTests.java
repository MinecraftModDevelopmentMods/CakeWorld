package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.AncientNougatKitchenFeature;

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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Contract proof for BIO-OW-021 and STRUCT-037.
 *
 * <p>OreSpawn 4.0.2 cannot restrict a biome-palette output by Y. These tests
 * therefore prove a weighted Dripstone-Caves replacement plus genuinely deep
 * geology and landmark behaviour; they do not claim a depth-only selector.
 * OS-104 separately proves the repaired 4.0.2 spring path.</p>
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class NougatDepthsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("nougat_depths");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final BlockPos FIXED_WORLD_SEARCH_ORIGIN =
			new BlockPos(12288, -40, -12288);

	private NougatDepthsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow021")
	public static void depthsHaveDenseEcologyFoodAndProfile(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Registry<Biome> registry = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome depths = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, depths != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.UNDERGROUND
						&& close(depths.getBaseTemperature(), 0.6D)
						&& close(depths.getDownfall(), 0.5D),
				"Nougat Depths are not a Dripstone-Caves-derived underground biome");
		for (BiomeDictionary.Type type : List.of(
				BiomeDictionary.Type.OVERWORLD,
				BiomeDictionary.Type.UNDERGROUND,
				BiomeDictionary.Type.DENSE,
				BiomeDictionary.Type.MAGICAL)) {
			require(helper,
					BiomeDictionary.hasType(BIOME_KEY, type),
					"Nougat Depths lost dictionary type " + type);
		}
		AmbientAdditionsSettings ambience =
				depths.getAmbientAdditions().orElse(null);
		AmbientParticleSettings dust =
				depths.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.NOUGAT_DEPTHS_SETTLE.getId())
						&& close(ambience.getTickChance(), 0.0011D)
						&& dust != null
						&& dust.getOptions().getType()
								== ParticleTypes.WHITE_ASH,
				"Nougat Depths lost their subtitled settling ambience");

		assertSpawn(helper, depths, EntityType.CAVE_SPIDER,
				CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get(),
				MobCategory.MONSTER, 6, 1, 2);
		assertSpawn(helper, depths, EntityType.SILVERFISH,
				CakeWorldEntities.CRUMB_MITE.get(),
				MobCategory.MONSTER, 8, 2, 4);
		for (EntityType<?> vanilla : List.of(
				EntityType.ZOMBIE,
				EntityType.ZOMBIE_VILLAGER,
				EntityType.SKELETON,
				EntityType.CREEPER,
				EntityType.SPIDER,
				EntityType.CAVE_SPIDER,
				EntityType.SILVERFISH,
				EntityType.WITCH,
				EntityType.SLIME,
				EntityType.ENDERMAN,
				EntityType.BAT)) {
			require(helper, findSpawn(depths, vanilla) == null,
					"Nougat Depths retained vanilla spawn "
							+ vanilla.getRegistryName());
		}
		require(helper,
				hasPlacedFeature(depths,
						AncientNougatKitchenFeature.ID),
				"Nougat Depths lost their Ancient Nougat Kitchen");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.ROCK_CANDY_CAVERNS.getId(),
				CakeWorldBiomes.JAM_GROTTOES.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							AncientNougatKitchenFeature.ID),
					"Ancient Nougat Kitchen leaked into " + other);
		}

		TagKey<Block> edibleOreHosts = TagKey.create(
				Registry.BLOCK_REGISTRY,
				id("edible_ore_hosts"));
		net.minecraft.world.item.crafting.Recipe<?> tilesRecipe =
				level.getRecipeManager()
						.byKey(id("toasted_nougat_tiles"))
						.orElse(null);
		require(helper,
				close(CakeWorldBlocks.NOUGAT_ROCK.get()
						.getSpeedFactor(), 0.88D)
						&& CakeWorldBlocks.NOUGAT_ROCK.get()
								.defaultBlockState()
								.is(edibleOreHosts)
						&& !CakeWorldBlocks.TOASTED_NOUGAT_TILES
								.get().defaultBlockState()
								.is(edibleOreHosts)
						&& tilesRecipe != null
						&& tilesRecipe.getIngredients().size() == 1
						&& tilesRecipe.getIngredients().get(0)
								.test(new ItemStack(
										CakeWorldBlocks
												.NOUGAT_ROCK.get()))
						&& tilesRecipe.getResultItem().is(
								CakeWorldBlocks
										.TOASTED_NOUGAT_TILES.get()
										.asItem()),
				"Nougat terrain or structural-tile contract drifted");

		FoodProperties food =
				CakeWorldItems.MINERS_NOUGAT.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				level.getRecipeManager()
						.byKey(id("miners_nougat"))
						.orElse(null);
		ItemStack rollingPin =
				new ItemStack(CakeWorldItems.ROLLING_PIN.get());
		ItemStack nougatRock =
				new ItemStack(CakeWorldBlocks.NOUGAT_ROCK.get());
		require(helper, food != null
						&& food.getNutrition() == 7
						&& close(food.getSaturationModifier(), 0.8D)
						&& hasEffect(food,
								CakeWorldEffects.COCOA_COMFORT.get(),
								240)
						&& hasEffect(food,
								MobEffects.DIG_SPEED, 200)
						&& recipe != null
						&& recipe.getIngredients().size() == 5
						&& matchingIngredients(recipe,
								nougatRock) == 2
						&& matchingIngredients(recipe,
								rollingPin) == 1
						&& recipe.getResultItem().is(
								CakeWorldItems.MINERS_NOUGAT.get())
						&& recipe.getResultItem().getCount() == 3
						&& rollingPin.getItem()
								.hasContainerItem(rollingPin)
						&& rollingPin.getItem()
								.getContainerItem(rollingPin)
								.is(CakeWorldItems.ROLLING_PIN.get()),
				"Miner's Nougat lost its worthwhile reusable-tool recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 31,
				"Nougat Depths require provider revision 31");
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
			JsonObject palettes = profile
					.getAsJsonObject("biome_palettes");
			JsonObject cavePalette = palettes
					.getAsJsonObject(
							"cakeworld:overworld_caves");
			JsonObject placement = cavePalette
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject mintRule = profile
					.getAsJsonObject("ores")
					.getAsJsonObject(
							"cakeworld:ore/mint_crystal");
			require(helper,
					geomes.size() == 2
							&& close(geomes.get(
									"cakeworld:peppermint_fold")
									.getAsDouble(), 8.0D)
							&& close(geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsDouble(), 20.0D)
							&& close(placement.get("weight")
									.getAsDouble(), 0.75D)
							&& strings(placement
									.getAsJsonArray("similar_biomes"))
									.equals(Set.of(
											"minecraft:dripstone_caves"))
							&& !placement.has("surface")
							&& !placement.has("min_y")
							&& !placement.has("max_y")
							&& "cakeworld:rock_candy_deposit"
									.equals(mintRule
											.get("deep_output")
											.getAsString())
							&& mintRule.get(
									"deep_output_max_y")
									.getAsInt() == -24,
					template
							+ " lost its weighted cave, geology or deep-output contract");
			List<String> paletteOrder = palettes.entrySet()
					.stream().map(Map.Entry::getKey)
					.toList();
			require(helper,
					paletteOrder.indexOf(
							"cakeworld:overworld_caves")
							< paletteOrder.indexOf(
									"cakeworld:overworld_land"),
					"Cave replacement must run before the broad land palette");
			if (firstPalette == null) {
				firstPalette = cavePalette;
			} else {
				require(helper, firstPalette.equals(cavePalette),
						"Normal and BaseMetals cave profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow021",
			timeoutTicks = 800)
	public static void kitchenIsDeepBoundedExactAndNonDestructive(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				AncientNougatKitchenFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== AncientNougatKitchenFeature.FEATURE
						&& AncientNougatKitchenFeature.MIN_Y == -56
						&& AncientNougatKitchenFeature.MAX_Y == -24,
				"Ancient Nougat Kitchen registration or deep bounds changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 2
						&& modifiers.get(0)
								instanceof InSquarePlacement
						&& modifiers.get(1)
								instanceof HeightRangePlacement,
				"Ancient Nougat Kitchen lost its every-depths-chunk bounded chain");

		BlockPos helperPos =
				helper.absolutePos(new BlockPos(4, 4, 4));
		ChunkPos chunk = new ChunkPos(helperPos);
		BlockPos fixture = new BlockPos(
				chunk.getMinBlockX() + 7,
				-40,
				chunk.getMinBlockZ() + 7);
		Set<Rotation> orientations = new HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					AncientNougatKitchenFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83,
									-56 + index % 33,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Ancient Nougat Kitchen did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					AncientNougatKitchenFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Ancient Nougat Kitchen crossed its generating chunk");
			prepare(level, fixture, rotation);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(6.0D)).size();
			require(helper,
					AncientNougatKitchenFeature.buildAt(
							level, fixture, rotation),
					"Ancient Nougat Kitchen refused a safe deep fixture for "
							+ rotation);
			Map<Block, Integer> palette =
					scanPalette(level, fixture, rotation);
			require(helper,
					matchesKitchen(level, fixture,
							rotation, false)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.TOASTED_NOUGAT_TILES.get(),
									0) == 30
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GINGERBREAD_BRICKS.get(),
									0) == 7
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR.get(),
									0) == 12
							&& palette.getOrDefault(
									CakeWorldBlocks.CANDY_GLASS.get(),
									0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks.MINT_CRYSTAL.get(),
									0) == 3
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ROCK_CANDY_DEPOSIT.get(),
									0) == 3
							&& palette.getOrDefault(
									CakeWorldBlocks.NOUGAT_ROCK.get(),
									0) == 1
							&& countBlockEntities(
									level, fixture) == 0
							&& level.getEntities((Entity) null,
									new AABB(fixture).inflate(6.0D))
									.size() == entities,
					"Ancient Nougat Kitchen lost its exact plan or created entities/block entities: "
							+ palette);
		}

		BlockPos obstacle =
				AncientNougatKitchenFeature.local(
						fixture, Rotation.NONE,
						0, 1, 1);
		for (BlockState rejected : List.of(
				Blocks.WATER.defaultBlockState(),
				Blocks.CHEST.defaultBlockState(),
				Blocks.BEDROCK.defaultBlockState())) {
			prepare(level, fixture, Rotation.NONE);
			level.setBlock(obstacle, rejected, 2);
			require(helper,
					!AncientNougatKitchenFeature.hasSafeSite(
							level, fixture,
							Rotation.NONE),
					"Ancient Nougat Kitchen accepted forbidden obstacle "
							+ rejected);
		}
		prepare(level, fixture, Rotation.NONE);
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				level.setBlock(
						AncientNougatKitchenFeature.local(
								fixture, Rotation.NONE,
								x, -1, z),
						Blocks.AIR.defaultBlockState(), 2);
			}
		}
		require(helper,
				!AncientNougatKitchenFeature.hasSafeSite(
						level, fixture, Rotation.NONE),
				"Ancient Nougat Kitchen accepted an unsupported chamber");

		prepare(level, fixture, Rotation.NONE);
		require(helper,
				AncientNougatKitchenFeature.buildAt(
						level, fixture, Rotation.NONE),
				"Could not prepare the kitchen edit-persistence fixture");
		BlockPos sentinel =
				AncientNougatKitchenFeature.local(
						fixture, Rotation.NONE,
						-2, 1, 1);
		level.setBlock(sentinel,
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!AncientNougatKitchenFeature.buildAt(
						level, fixture, Rotation.NONE)
						&& level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
				"Ancient Nougat Kitchen regenerated over a player edit");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow021world",
			timeoutTicks = 24000)
	public static void focusedNaturalNougatDepthsAudit(
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
		BlockPos depthsAnchor =
				locateBiome(helper, level);
		LocatedKitchen kitchen = locateNaturalKitchen(
				helper, level, depthsAnchor, 8);
		ChunkPos chunk = new ChunkPos(kitchen.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			BlockPos sentinel =
					AncientNougatKitchenFeature.local(
							kitchen.centre(),
							kitchen.rotation(),
							-2, 1, 1);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			require(helper,
					matchesKitchen(level,
							kitchen.centre(),
							kitchen.rotation(),
							brickSentinel),
					"Natural Ancient Nougat Kitchen lost its complete plan");
			GeologyAudit geology =
					auditNaturalGeology(
							level, kitchen.centre(), 64);
			ResourceLocation biome = level.getBiome(
					kitchen.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			LOGGER.info("Nougat Depths audit: centre={}, anchor={}, biome={}, rotation={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, depthsSamples={}, geology={}",
					kitchen.centre(), depthsAnchor, biome,
					kitchen.rotation(), brickSentinel,
					kitchen.scannedChunks(),
					kitchen.markerCandidates(),
					kitchen.depthsSamples(), geology);
			require(helper,
					BIOME_ID.equals(biome)
							&& kitchen.centre().getY()
									>= AncientNougatKitchenFeature.MIN_Y
							&& kitchen.centre().getY()
									<= AncientNougatKitchenFeature.MAX_Y
							&& kitchen.depthsSamples() >= 16
							&& geology.nougatRock() > 0
							&& geology.deepRockCandyDeposits() > 0
							&& countBlockEntities(
									level, kitchen.centre()) == 0,
					"Natural Nougat Depths lost their 3D biome, independent deep geology or complete kitchen: "
							+ geology);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Ancient Nougat Kitchen");
			}
			level.setChunkForced(chunk.x, chunk.z, false);
			helper.succeed();
		});
	}

	private static BlockPos locateBiome(
			GameTestHelper helper, ServerLevel level) {
		Pair<BlockPos, Holder<Biome>> match =
				level.findNearestBiome(
						holder -> holder.unwrapKey()
								.map(key -> key.location()
										.equals(BIOME_ID))
								.orElse(false),
						FIXED_WORLD_SEARCH_ORIGIN,
						16384, 8);
		require(helper, match != null,
				"Could not locate Nougat Depths within 16,384 blocks of the isolated fixed-world origin at Y -40");
		return match.getFirst();
	}

	private static LocatedKitchen locateNaturalKitchen(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int depthsSamples = 0;
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
						for (int y =
								AncientNougatKitchenFeature.MIN_Y;
								y <= AncientNougatKitchenFeature
										.MAX_Y; y++) {
							BlockPos centre = new BlockPos(
									horizontal[0], y,
									horizontal[1]);
							if ((y & 3) == 0
									&& level.getBiome(centre)
											.is(BIOME_KEY)) {
								depthsSamples++;
							}
							Rotation rotation =
									AncientNougatKitchenFeature
											.orientation(
													level.getSeed(),
													centre);
							BlockPos marker =
									AncientNougatKitchenFeature
											.local(centre,
													rotation,
													0, 1, -1);
							if (!level.getBlockState(marker)
									.is(CakeWorldBlocks
											.MIXING_BOWL.get())) {
								continue;
							}
							markerCandidates++;
							if (matchesKitchen(level, centre,
									rotation, true)) {
								return new LocatedKitchen(
										centre, rotation,
										scannedChunks,
										markerCandidates,
										depthsSamples);
							}
							LOGGER.warn(
									"Rejected Ancient Nougat Kitchen marker candidate: centre={}, rotation={}, marker={}, mismatch={}",
									centre, rotation, marker,
									firstKitchenMismatch(
											level, centre,
											rotation, true));
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Nougat Depths survey found no natural Ancient Nougat Kitchen after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " mixing-bowl candidates near "
						+ anchor + "; depthsSamples="
						+ depthsSamples);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static GeologyAudit auditNaturalGeology(
			ServerLevel level, BlockPos centre, int radius) {
		int minChunkX = Math.floorDiv(
				centre.getX() - radius, 16);
		int maxChunkX = Math.floorDiv(
				centre.getX() + radius, 16);
		int minChunkZ = Math.floorDiv(
				centre.getZ() - radius, 16);
		int maxChunkZ = Math.floorDiv(
				centre.getZ() + radius, 16);
		for (int chunkX = minChunkX;
				chunkX <= maxChunkX; chunkX++) {
			for (int chunkZ = minChunkZ;
					chunkZ <= maxChunkZ; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
		List<BlockPos> authoredKitchens = new ArrayList<>();
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				for (int y =
						AncientNougatKitchenFeature.MIN_Y;
						y <= AncientNougatKitchenFeature.MAX_Y + 1;
						y++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (level.getBlockState(pos)
							.is(CakeWorldBlocks.MIXING_BOWL.get())) {
						authoredKitchens.add(pos);
					}
				}
			}
		}
		int nougatRock = 0;
		int deepDeposits = 0;
		int excludedCells = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				for (int y = -56; y <= -24; y++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (nearAuthoredKitchen(
							pos, authoredKitchens)) {
						excludedCells++;
						continue;
					}
					BlockState state =
							level.getBlockState(pos);
					if (state.is(
							CakeWorldBlocks.NOUGAT_ROCK.get())) {
						nougatRock++;
					}
					if (state.is(CakeWorldBlocks
							.ROCK_CANDY_DEPOSIT.get())) {
						deepDeposits++;
					}
				}
			}
		}
		return new GeologyAudit(nougatRock,
				deepDeposits, authoredKitchens.size(),
				excludedCells);
	}

	private static boolean nearAuthoredKitchen(
			BlockPos position, List<BlockPos> bowls) {
		for (BlockPos bowl : bowls) {
			if (Math.abs(position.getX() - bowl.getX()) <= 6
					&& Math.abs(position.getY() - bowl.getY())
							<= 6
					&& Math.abs(position.getZ() - bowl.getZ())
							<= 6) {
				return true;
			}
		}
		return false;
	}

	private static boolean matchesKitchen(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		return firstKitchenMismatch(level, centre, rotation,
				allowBrickSentinel) == null;
	}

	private static String firstKitchenMismatch(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -3; x <= 3; x++) {
			for (int z = -2; z <= 2; z++) {
				Block expected = z == 0
						? CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						: CakeWorldBlocks
								.TOASTED_NOUGAT_TILES.get();
				if (!state(level, centre, rotation,
						x, 0, z).is(expected)) {
					return mismatch(level, centre, rotation,
							x, 0, z, expected);
				}
			}
		}
		for (int x : new int[] {-3, 3}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					if (!state(level, centre, rotation,
							x, y, z).is(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR.get())) {
						return mismatch(level, centre,
								rotation, x, y, z,
								CakeWorldBlocks
										.CANDY_CANE_PILLAR.get());
					}
				}
				if (!state(level, centre, rotation,
						x, 4, z).is(
								CakeWorldBlocks.CANDY_GLASS.get())) {
					return mismatch(level, centre, rotation,
							x, 4, z,
							CakeWorldBlocks.CANDY_GLASS.get());
				}
			}
		}
		for (Object[] furnishing : new Object[][] {
			{-2, -1, CakeWorldBlocks.TOASTED_NOUGAT_TILES.get()},
			{2, -1, CakeWorldBlocks.TOASTED_NOUGAT_TILES.get()},
			{-1, -1, CakeWorldBlocks.OVEN.get()},
			{0, -1, CakeWorldBlocks.MIXING_BOWL.get()},
			{1, -1, CakeWorldBlocks.COOLING_RACK.get()}
		}) {
			int x = (Integer) furnishing[0];
			int z = (Integer) furnishing[1];
			Block expected = (Block) furnishing[2];
			if (!state(level, centre, rotation,
					x, 1, z).is(expected)) {
				return mismatch(level, centre, rotation,
						x, 1, z, expected);
			}
		}
		int[][] mint = {
			{-2, 1}, {0, 1}, {2, 1}
		};
		for (int index = 0; index < mint.length; index++) {
			BlockState marker = state(level, centre,
					rotation, mint[index][0], 1,
					mint[index][1]);
			boolean sentinel = allowBrickSentinel
					&& index == 0
					&& marker.is(Blocks.BRICKS);
			if (!sentinel
					&& !marker.is(
							CakeWorldBlocks.MINT_CRYSTAL.get())) {
				return mismatch(level, centre, rotation,
						mint[index][0], 1,
						mint[index][1],
						CakeWorldBlocks.MINT_CRYSTAL.get());
			}
		}
		for (int[] deposit : new int[][] {
			{-1, 1}, {1, 1}, {-2, 0}
		}) {
			if (!state(level, centre, rotation,
					deposit[0], 1, deposit[1])
					.is(CakeWorldBlocks
							.ROCK_CANDY_DEPOSIT.get())) {
				return mismatch(level, centre, rotation,
						deposit[0], 1, deposit[1],
						CakeWorldBlocks
								.ROCK_CANDY_DEPOSIT.get());
			}
		}
		if (!state(level, centre, rotation,
				2, 1, 0).is(CakeWorldBlocks.NOUGAT_ROCK.get())) {
			return mismatch(level, centre, rotation,
					2, 1, 0,
					CakeWorldBlocks.NOUGAT_ROCK.get());
		}
		return null;
	}

	private static String mismatch(ServerLevel level,
			BlockPos centre, Rotation rotation,
			int x, int y, int z, Block expected) {
		BlockPos position =
				AncientNougatKitchenFeature.local(
						centre, rotation, x, y, z);
		return "local=(" + x + "," + y + "," + z
				+ "), world=" + position
				+ ", expected=" + expected
				+ ", actual="
				+ level.getBlockState(position);
	}

	private static void prepare(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int y = -1; y <= 5; y++) {
				for (int z = -3; z <= 3; z++) {
					BlockState state = y == -1
							? CakeWorldBlocks.NOUGAT_ROCK.get()
									.defaultBlockState()
							: Blocks.AIR.defaultBlockState();
					level.setBlock(
							AncientNougatKitchenFeature.local(
									centre, rotation,
									x, y, z),
							state, 2);
				}
			}
		}
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -3; x <= 3; x++) {
			for (int y = 0; y <= 4; y++) {
				for (int z = -2; z <= 2; z++) {
					Block block = state(level, centre,
							rotation, x, y, z)
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
		for (int x = -4; x <= 4; x++) {
			for (int y = -1; y <= 5; y++) {
				for (int z = -3; z <= 3; z++) {
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
				AncientNougatKitchenFeature.local(
						centre, rotation, x, y, z));
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
				"Nougat Depths lost replacement "
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
		array.forEach(element ->
				values.add(element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				NougatDepthsGameTests.class
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

	private record LocatedKitchen(
			BlockPos centre,
			Rotation rotation,
			int scannedChunks,
			int markerCandidates,
			int depthsSamples) {
	}

	private record GeologyAudit(
			int nougatRock,
			int deepRockCandyDeposits,
			int authoredKitchenMarkers,
			int excludedCells) {
	}
}
