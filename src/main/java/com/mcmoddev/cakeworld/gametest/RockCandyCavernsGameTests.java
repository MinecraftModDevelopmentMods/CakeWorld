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
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.RockCandyGeodeBridgeFeature;

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
 * Contract proof for BIO-OW-019 and STRUCT-035.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class RockCandyCavernsGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("rock_candy_caverns");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private RockCandyCavernsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow019")
	public static void cavernsHavePrismaticEcologyFoodAndProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome caverns = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, caverns != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.UNDERGROUND
						&& close(caverns.getBaseTemperature(), 0.8D)
						&& close(caverns.getDownfall(), 0.4D),
				"Rock-Candy Caverns are not a Dripstone-Caves-derived underground biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.UNDERGROUND)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.MAGICAL)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SPARSE),
				"Rock-Candy Caverns dictionary roles drifted");

		AmbientAdditionsSettings ambience =
				caverns.getAmbientAdditions().orElse(null);
		AmbientParticleSettings sparkle =
				caverns.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.ROCK_CANDY_CAVERNS_CHIME
										.getId())
						&& close(ambience.getTickChance(), 0.0012D)
						&& sparkle != null
						&& sparkle.getOptions().getType()
								== ParticleTypes.END_ROD,
				"Rock-Candy Caverns lost their subtitled prismatic ambience");

		assertSpawn(helper, caverns, EntityType.SILVERFISH,
				CakeWorldEntities.CRUMB_MITE.get(),
				MobCategory.MONSTER, 8, 2, 4);
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
			require(helper, findSpawn(caverns, vanilla) == null,
					"Rock-Candy Caverns retained vanilla spawn "
							+ vanilla.getRegistryName());
		}
		for (EntityType<?> replacement : List.of(
				CakeWorldEntities.STALE_CRUMBLER.get(),
				CakeWorldEntities.CANDY_CANE_ARCHER.get(),
				CakeWorldEntities.POP_ROCK_POPPER.get(),
				CakeWorldEntities.LIQUORICE_WEAVER.get(),
				CakeWorldEntities.TAFFY_TALLWALKER.get(),
				CakeWorldEntities.BONBON_BAT.get())) {
			require(helper, findSpawn(caverns, replacement) != null,
					"Rock-Candy Caverns lost CakeWorld spawn "
							+ replacement.getRegistryName());
		}

		require(helper,
				CakeWorldBlocks.MINT_CRYSTAL.get()
						.defaultBlockState()
						.getLightEmission() == 7,
				"Mint Crystal no longer provides gentle cave light");
		TagKey<Block> edibleOreHosts = TagKey.create(
				Registry.BLOCK_REGISTRY,
				id("edible_ore_hosts"));
		net.minecraft.world.item.crafting.Recipe<?> cutRecipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("cut_rock_candy"))
						.orElse(null);
		require(helper,
				!CakeWorldBlocks.CUT_ROCK_CANDY.get()
						.defaultBlockState().is(edibleOreHosts)
						&& cutRecipe != null
						&& cutRecipe.getIngredients().size() == 1
						&& cutRecipe.getResultItem().is(
								CakeWorldBlocks.CUT_ROCK_CANDY.get()
										.asItem())
						&& cutRecipe.getResultItem().getCount() == 1,
				"Cut Rock Candy lost its structural non-host or stonecutting contract");
		require(helper,
				hasPlacedFeature(caverns,
						RockCandyGeodeBridgeFeature.ID),
				"Rock-Candy Caverns lost their Geode Bridge");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.CANDY_CANE_BADLANDS.getId(),
				CakeWorldBiomes.JELLYBEAN_ARCHIPELAGO.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							RockCandyGeodeBridgeFeature.ID),
					"Rock-Candy Geode Bridge leaked into " + other);
		}

		FoodProperties food =
				CakeWorldItems.PRISMATIC_ROCK_CANDY.get()
						.getFoodProperties();
		net.minecraft.world.item.crafting.Recipe<?> recipe =
				helper.getLevel().getRecipeManager()
						.byKey(id("prismatic_rock_candy"))
						.orElse(null);
		ItemStack rockCandyDeposit = new ItemStack(
				CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get());
		ItemStack mintCrystal = new ItemStack(
				CakeWorldBlocks.MINT_CRYSTAL.get());
		require(helper, food != null
						&& food.getNutrition() == 4
						&& close(food.getSaturationModifier(), 0.4D)
						&& food.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== MobEffects.NIGHT_VISION
										&& entry.getFirst()
												.getDuration() == 300)
						&& food.getEffects().stream()
								.anyMatch(entry ->
										entry.getFirst().getEffect()
												== CakeWorldEffects
														.MINTY_FRESH.get()
										&& entry.getFirst()
												.getDuration() == 160)
						&& recipe != null
						&& recipe.getIngredients().size() == 8
						&& recipe.getIngredients().stream()
								.filter(ingredient ->
										ingredient.test(
												rockCandyDeposit))
								.count() == 4
						&& recipe.getIngredients().stream()
								.filter(ingredient ->
										ingredient.test(
												mintCrystal))
								.count() == 4
						&& recipe.getResultItem().is(
								CakeWorldItems
										.PRISMATIC_ROCK_CANDY.get())
						&& recipe.getResultItem().getCount() == 4,
				"Prismatic Rock Candy lost its useful four-serving cave recipe");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 29,
				"Rock-Candy Caverns require provider revision 29");
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
			require(helper,
					geomes.size() == 2
							&& close(geomes.get(
									"cakeworld:peppermint_fold")
									.getAsDouble(), 4.0D)
							&& close(geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsDouble(), 20.0D)
							&& "minecraft:overworld".equals(
									cavePalette.get("dimension")
											.getAsString())
							&& "replace".equals(cavePalette
									.get("mode").getAsString())
							&& close(cavePalette.get("coverage")
									.getAsDouble(), 1.0D)
							&& close(cavePalette
									.get("fallback_weight")
									.getAsDouble(), 0.0D)
							&& close(placement.get("weight")
									.getAsDouble(), 1.0D)
							&& strings(placement
									.getAsJsonArray("similar_biomes"))
									.equals(Set.of(
											"minecraft:dripstone_caves"))
							&& placement
									.getAsJsonArray(
											"required_similar_biomes")
									.size() == 0
							&& !placement.has("surface"),
					template
							+ " lost its exact 3D cavern selector or geology");
			List<String> paletteOrder = palettes.entrySet()
					.stream().map(java.util.Map.Entry::getKey)
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

	@GameTest(template = EMPTY, batch = "bioow019",
			timeoutTicks = 800)
	public static void geodeBridgeIsBoundedExactAndNonDestructive(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				RockCandyGeodeBridgeFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== RockCandyGeodeBridgeFeature.FEATURE
						&& RockCandyGeodeBridgeFeature.MIN_Y == -48
						&& RockCandyGeodeBridgeFeature.MAX_Y == 48,
				"Geode Bridge registration or bounds changed");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 2
						&& modifiers.get(0)
								instanceof InSquarePlacement
						&& modifiers.get(1)
								instanceof HeightRangePlacement,
				"Geode Bridge lost its every-cavern-chunk bounded chain or regained a redundant random rejection");

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
					RockCandyGeodeBridgeFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 83,
									index % 64 - 16,
									index * -107)));
		}
		require(helper, orientations.size() == 4,
				"Geode Bridge did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					RockCandyGeodeBridgeFeature.fitsWithinChunk(
							fixture, rotation, chunk),
					"Geode Bridge crossed its generating chunk");
			prepare(level, fixture, rotation);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					RockCandyGeodeBridgeFeature.buildAt(
							level, fixture, rotation),
					"Geode Bridge refused a safe cave fixture for "
							+ rotation);
			require(helper,
					matchesBridge(level, fixture, rotation, false)
							&& level.getEntities((Entity) null,
									new AABB(fixture).inflate(8.0D))
									.size() == entities
							&& countBlockEntities(
									level, fixture) == 0,
					"Geode Bridge lost its exact plan or created entities/block entities");
		}

		prepare(level, fixture, Rotation.NONE);
		BlockPos marker =
				RockCandyGeodeBridgeFeature.local(
						fixture, Rotation.NONE, -4, 1, -2);
		level.setBlock(marker,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!RockCandyGeodeBridgeFeature.hasSafeSite(
						level, fixture, Rotation.NONE),
				"Geode Bridge accepted fluid in its plan");
		prepare(level, fixture, Rotation.NONE);
		level.setBlock(marker,
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper,
				!RockCandyGeodeBridgeFeature.hasSafeSite(
						level, fixture, Rotation.NONE),
				"Geode Bridge accepted a block entity");
		prepare(level, fixture, Rotation.NONE);
		level.setBlock(marker,
				Blocks.BEDROCK.defaultBlockState(), 2);
		require(helper,
				!RockCandyGeodeBridgeFeature.hasSafeSite(
						level, fixture, Rotation.NONE),
				"Geode Bridge carved an authored solid obstacle");
		prepare(level, fixture, Rotation.NONE);
		for (int x = -2; x <= 2; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -2; y <= -1; y++) {
					level.setBlock(
							RockCandyGeodeBridgeFeature.local(
									fixture, Rotation.NONE,
									x, y, z),
							CakeWorldBlocks.BISCUIT_STONE.get()
									.defaultBlockState(),
							2);
				}
			}
		}
		require(helper,
				!RockCandyGeodeBridgeFeature.hasSafeSite(
						level, fixture, Rotation.NONE),
				"Geode Bridge accepted a floor with no open span");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow019world",
			timeoutTicks = 24000)
	public static void focusedNaturalRockCandyCavernsAudit(
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
		BlockPos cavernAnchor =
				locateCavern(helper, level);
		LocatedBridge bridge = locateNaturalBridge(
				helper, level, cavernAnchor, 8);
		ChunkPos chunk = new ChunkPos(bridge.centre());
		level.setChunkForced(chunk.x, chunk.z, true);
		helper.runAfterDelay(40, () -> {
			BlockPos sentinel =
					RockCandyGeodeBridgeFeature.local(
							bridge.centre(),
							bridge.rotation(),
							-4, 1, -2);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			require(helper,
					matchesBridge(level, bridge.centre(),
							bridge.rotation(),
							brickSentinel),
					"Natural Geode Bridge lost its complete plan");
			GeologyAudit geology =
					auditNaturalGeology(
							level, bridge.centre(), 64);
			ResourceLocation biome = level.getBiome(
					bridge.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			int blockEntities = countBlockEntities(
					level, bridge.centre());
			LOGGER.info("Rock-Candy Caverns audit: centre={}, anchor={}, biome={}, rotation={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, cavernSamples={}, geology={}",
					bridge.centre(), cavernAnchor, biome,
					bridge.rotation(), blockEntities,
					brickSentinel, bridge.scannedChunks(),
					bridge.markerCandidates(),
					bridge.cavernSamples(), geology);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& bridge.cavernSamples() >= 16
							&& geology.rockCandy() > 0
							&& geology.rockCandyDeposits() > 0
							&& geology.mintCrystals() > 0,
					"Natural Caverns lost their 3D biome, independent geology or complete bridge: "
							+ geology);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel in the Geode Bridge");
			}
			level.setChunkForced(chunk.x, chunk.z, false);
			helper.succeed();
		});
	}

	private static BlockPos locateCavern(
			GameTestHelper helper, ServerLevel level) {
		Pair<BlockPos, Holder<Biome>> match =
				level.findNearestBiome(
						holder -> holder.unwrapKey()
								.map(key -> key.location()
										.equals(BIOME_ID))
								.orElse(false),
						new BlockPos(0, 0, 0),
						16384, 8);
		require(helper, match != null,
				"Could not locate Rock-Candy Caverns within 16,384 blocks at cave height");
		return match.getFirst();
	}

	private static LocatedBridge locateNaturalBridge(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int cavernSamples = 0;
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
								RockCandyGeodeBridgeFeature.MIN_Y;
								y <= RockCandyGeodeBridgeFeature
										.MAX_Y; y++) {
							BlockPos centre = new BlockPos(
									horizontal[0], y,
									horizontal[1]);
							if ((y & 3) == 0
									&& level.getBiome(centre)
											.is(BIOME_KEY)) {
								cavernSamples++;
							}
							BlockPos marker = centre.above();
							if (!level.getBlockState(marker)
									.is(CakeWorldBlocks
											.MIXING_BOWL.get())) {
								continue;
							}
							markerCandidates++;
							Rotation rotation =
									RockCandyGeodeBridgeFeature
											.orientation(
													level.getSeed(),
													centre);
							if (matchesBridge(level,
									centre, rotation, true)) {
								return new LocatedBridge(
										centre, rotation,
										scannedChunks,
										markerCandidates,
										cavernSamples);
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Rock-Candy Caverns survey found no natural Geode Bridge after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " mixing-bowl candidates near "
						+ anchor + "; cavernSamples="
						+ cavernSamples);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static GeologyAudit auditNaturalGeology(
			ServerLevel level, BlockPos centre, int radius) {
		int rockCandy = 0;
		int deposits = 0;
		int mintCrystals = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				for (int y = RockCandyGeodeBridgeFeature.MIN_Y;
						y <= RockCandyGeodeBridgeFeature.MAX_Y; y++) {
					if (Math.abs(x - centre.getX()) <= 7
							&& Math.abs(z - centre.getZ()) <= 7
							&& Math.abs(y - centre.getY()) <= 5) {
						continue;
					}
					Block block = level.getBlockState(
							new BlockPos(x, y, z)).getBlock();
					if (block == CakeWorldBlocks.ROCK_CANDY.get()) {
						rockCandy++;
					} else if (block == CakeWorldBlocks
							.ROCK_CANDY_DEPOSIT.get()) {
						deposits++;
					} else if (block == CakeWorldBlocks
							.MINT_CRYSTAL.get()) {
						mintCrystals++;
					}
				}
			}
		}
		return new GeologyAudit(rockCandy, deposits,
				mintCrystals);
	}

	private static boolean matchesBridge(
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -1; z <= 1; z++) {
				if (!state(level, centre, rotation,
						x, 0, z).is(
								CakeWorldBlocks.CANDY_GLASS.get())) {
					return false;
				}
			}
		}
		for (int x = -5; x <= 5; x += 2) {
			if (!state(level, centre, rotation,
					x, 1, -2).is(
							CakeWorldBlocks.CUT_ROCK_CANDY.get())
					|| !state(level, centre, rotation,
							x, 1, 2).is(
									CakeWorldBlocks.CUT_ROCK_CANDY.get())) {
				return false;
			}
		}
		int[][] arch = {
			{-3, 0}, {3, 0}, {-3, 1}, {3, 1},
			{-2, 2}, {2, 2}, {-1, 3}, {0, 3}, {1, 3}
		};
		for (int x : new int[] {-5, 5}) {
			for (int[] point : arch) {
				if (!state(level, centre, rotation,
						x, point[1], point[0]).is(
								CakeWorldBlocks.CUT_ROCK_CANDY.get())) {
					return false;
				}
			}
			for (int z = -1; z <= 1; z++) {
				for (int y = -2; y <= -1; y++) {
					if (!state(level, centre, rotation,
							x, y, z).is(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR.get())) {
						return false;
					}
				}
			}
		}
		for (int[] marker :
				RockCandyGeodeBridgeFeature.mintMarkers()) {
			Block block = state(level, centre, rotation,
					marker[0], 1, marker[1]).getBlock();
			if (block != CakeWorldBlocks.MINT_CRYSTAL.get()
					&& !(allowBrickSentinel
							&& marker[0] == -4
							&& marker[1] == -2
							&& block == Blocks.BRICKS)) {
				return false;
			}
		}
		for (int[] marker :
				RockCandyGeodeBridgeFeature.depositMarkers()) {
			if (!state(level, centre, rotation,
					marker[0], 1, marker[1]).is(
							CakeWorldBlocks
									.ROCK_CANDY_DEPOSIT.get())) {
				return false;
			}
		}
		for (int z : new int[] {-2, 2}) {
			if (!state(level, centre, rotation,
					0, 1, z).is(
							CakeWorldBlocks.MINT_EMERALD.get())) {
				return false;
			}
		}
		if (!state(level, centre, rotation,
				0, 1, 0).is(
						CakeWorldBlocks.MIXING_BOWL.get())) {
			return false;
		}
		for (int x = -1; x <= 1; x++) {
			if (!state(level, centre, rotation,
					x, -3, 0).is(
							CakeWorldBlocks.MARSHMALLOW.get())) {
				return false;
			}
		}
		return true;
	}

	private static void prepare(ServerLevel level,
			BlockPos centre, Rotation rotation) {
		for (int x = -7; x <= 7; x++) {
			for (int y = -5; y <= 6; y++) {
				for (int z = -5; z <= 5; z++) {
					level.setBlock(
							RockCandyGeodeBridgeFeature.local(
									centre, rotation,
									x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
			}
		}
		for (int y = -4; y <= 5; y++) {
			for (int x = -6; x <= 6; x++) {
				for (int z : new int[] {-4, 4}) {
					level.setBlock(
							RockCandyGeodeBridgeFeature.local(
									centre, rotation,
									x, y, z),
							CakeWorldBlocks.BISCUIT_STONE.get()
									.defaultBlockState(),
							2);
				}
			}
			for (int z = -3; z <= 3; z++) {
				for (int x : new int[] {-6, 6}) {
					level.setBlock(
							RockCandyGeodeBridgeFeature.local(
									centre, rotation,
									x, y, z),
							CakeWorldBlocks.BISCUIT_STONE.get()
									.defaultBlockState(),
							2);
				}
			}
		}
	}

	private static int countBlockEntities(
			ServerLevel level, BlockPos centre) {
		int count = 0;
		for (int x = -7; x <= 7; x++) {
			for (int y = -5; y <= 6; y++) {
				for (int z = -7; z <= 7; z++) {
					if (level.getBlockEntity(
							centre.offset(x, y, z)) != null) {
						count++;
					}
				}
			}
		}
		return count;
	}

	private static net.minecraft.world.level.block.state.BlockState state(
			ServerLevel level, BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return level.getBlockState(
				RockCandyGeodeBridgeFeature.local(
						centre, rotation, x, y, z));
	}

	private static void assertSpawn(GameTestHelper helper,
			Biome biome, EntityType<?> vanilla,
			EntityType<?> replacement, MobCategory category,
			int weight, int minimum, int maximum) {
		MobSpawnSettings.SpawnerData spawn =
				findSpawn(biome, replacement);
		require(helper, spawn != null
						&& replacement.getCategory() == category
						&& spawn.getWeight().asInt() == weight
						&& spawn.minCount == minimum
						&& spawn.maxCount == maximum
						&& findSpawn(biome, vanilla) == null,
				"Rock-Candy Caverns lost the "
						+ replacement.getRegistryName()
						+ " replacement for "
						+ vanilla.getRegistryName());
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

	private static Set<String> strings(JsonArray array) {
		Set<String> values = new HashSet<>();
		array.forEach(element -> values.add(
				element.getAsString()));
		return values;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				RockCandyCavernsGameTests.class
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

	private record LocatedBridge(
			BlockPos centre,
			Rotation rotation,
			int scannedChunks,
			int markerCandidates,
			int cavernSamples) {
	}

	private record GeologyAudit(
			int rockCandy,
			int rockCandyDeposits,
			int mintCrystals) {
	}
}
