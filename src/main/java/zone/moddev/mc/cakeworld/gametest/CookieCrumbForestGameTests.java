package zone.moddev.mc.cakeworld.gametest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.CookieCrumbGroveFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Contract proof for the first functional Cookie Crumb Forest ecosystem.
 */
@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class CookieCrumbForestGameTests {
	private static final String EMPTY = "empty";
	private static final String PROVIDER =
			"/data/cakeworld/orespawn/provider.json";
	private static final ResourceLocation BIOME_ID =
			new ResourceLocation(CakeWorld.MODID,
					"cookie_forest");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final List<String> TEMPLATES = List.of(
			"cakeworld:edible_world",
			"cakeworld:edible_world_basemetals");

	private CookieCrumbForestGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow003")
	public static void cookieForestHasDistinctClimateSoundAndEcology(
			GameTestHelper helper) {
		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome biome = biomes.get(BIOME_ID);
		Holder<Biome> holder = biomes.getHolder(BIOME_KEY)
				.orElseThrow(() -> new IllegalStateException(
						"Missing biome holder " + BIOME_ID));

		require(helper, biome != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.FOREST,
				"Cookie Crumb Forest is not a registered forest-category biome");
		require(helper,
				close(biome.getBaseTemperature(), 0.7D)
						&& close(biome.getDownfall(), 0.8D),
				"Cookie Crumb Forest climate does not match its contract");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.FOREST)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.DENSE)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.WET)
						&& !BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.OCEAN),
				"Cookie Crumb Forest biome-dictionary roles are wrong");

		AmbientAdditionsSettings additions = biome.getAmbientAdditions()
				.orElse(null);
		require(helper, additions != null
						&& additions.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.COOKIE_FOREST_RUSTLE
										.getId())
						&& close(additions.getTickChance(),
								0.0012D),
				"Cookie Crumb Forest does not use its quiet canopy rustle");

		requireSpawnReplacement(helper, biome, EntityType.COW,
				CakeWorldEntities.COCOA_COW.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.WOLF,
				CakeWorldEntities.GINGER_SNAP_HOUND.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.BEE,
				CakeWorldEntities.SUGAR_BEE.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER.get(),
				MobCategory.MONSTER);
		requireSpawnReplacement(helper, biome, EntityType.CREEPER,
				CakeWorldEntities.POP_ROCK_POPPER.get(),
				MobCategory.MONSTER);
		requireSpawnReplacement(helper, biome, EntityType.BAT,
				CakeWorldEntities.BONBON_BAT.get(),
				MobCategory.AMBIENT);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow003")
	public static void bothAdventureTemplatesShareCookieForestProfile(
			GameTestHelper helper) {
		JsonObject provider = readObject(PROVIDER);
		require(helper,
				provider.get("provider_revision").getAsInt() >= 13,
				"Cookie Crumb Forest requires OreSpawn provider revision 13 or later");
		JsonObject templates = provider.getAsJsonObject("templates");
		for (String templateId : TEMPLATES) {
			JsonObject profile = templates.getAsJsonObject(templateId)
					.getAsJsonObject("profile");
			JsonObject geomes = profile.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					close(geomes.get("cakeworld:cocoa_basin")
							.getAsDouble(), 6.0D)
							&& close(geomes.get(
									"cakeworld:wafer_shelf")
									.getAsDouble(), 8.0D),
					templateId
							+ " does not preserve the Cookie Forest geome bias");

			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					palette.get("enabled").getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 2.0D)
							&& stringSet(palette
									.getAsJsonArray(
											"similar_biomes"))
									.equals(Set.of(
											"minecraft:forest",
											"minecraft:flower_forest",
											"minecraft:birch_forest"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.isEmpty(),
					templateId
							+ " does not preserve the Cookie Forest weight/similarity contract");
			JsonObject surface = palette.getAsJsonObject("surface");
			require(helper,
					"cakeworld:chocolate_sponge".equals(
							surface.get("top_block").getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface.get(
											"underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5,
					templateId
							+ " does not preserve the Cookie Forest edible surface");
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow003",
			timeoutTicks = 1200)
	public static void cookieGroveBuildsTreesBurrowAndBiscuitCache(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CookieCrumbGroveFeature.placedFeature();
		require(helper, placed != null,
				"Cookie Crumb Grove placed feature was not registered");
		List<PlacementModifier> modifiers =
				placed.value().placement();
		require(helper,
				placed.unwrapKey()
						.map(key -> key.location()
								.equals(CookieCrumbGroveFeature.ID))
						.orElse(false)
						&& placed.value().feature()
								.value().feature()
								== CookieCrumbGroveFeature.FEATURE
						&& modifiers.size() == 4
						&& modifiers.get(0)
								instanceof RarityFilter
						&& readPrivateInt(modifiers.get(0),
								"chance")
								== CookieCrumbGroveFeature
										.AVERAGE_CHUNKS_PER_ATTEMPT
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3)
								instanceof BiomeFilter,
				"Cookie Crumb Grove lost its one-in-24 spread/surface/biome placement chain");
		require(helper,
				hasPlacedFeature(level,
						CakeWorldBiomes.COOKIE_FOREST.getId())
						&& !hasPlacedFeature(level,
								CakeWorldBiomes
										.CANDY_PLAINS
										.getId())
						&& !hasPlacedFeature(level,
								CakeWorldBiomes
										.GINGERBREAD_HEARTHLANDS
										.getId())
						&& !hasPlacedFeature(level,
								CakeWorldBiomes
										.MARSHMALLOW_PEAKS
										.getId())
						&& !hasPlacedFeature(level,
								CakeWorldBiomes
										.SODA_OCEAN
										.getId()),
				"Cookie Crumb Grove crossed its explicit Cookie-Forest-only boundary");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 20,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		Set<Rotation> observed = new java.util.HashSet<>();
		for (int sample = 0;
				sample < 128 && observed.size() < 4; sample++) {
			observed.add(CookieCrumbGroveFeature.orientation(
					level.getSeed(),
					fixture.offset(sample * 37, 0,
							sample * -43)));
		}
		require(helper, observed.size() == 4,
				"Cookie Crumb Grove did not expose four deterministic cardinal presentations");

		ChunkPos fixtureChunk = new ChunkPos(fixture);
		int minimumX = fixtureChunk.getMinBlockX();
		int minimumZ = fixtureChunk.getMinBlockZ();
		BlockPos[] containedCentres = {
				new BlockPos(minimumX + 5,
						fixture.getY(),
						minimumZ + 4),
				new BlockPos(minimumX + 10,
						fixture.getY(),
						minimumZ + 5),
				new BlockPos(minimumX + 5,
						fixture.getY(),
						minimumZ + 10),
				new BlockPos(minimumX + 4,
						fixture.getY(),
						minimumZ + 5)
		};
		Rotation[] containedRotations = {
				Rotation.NONE,
				Rotation.CLOCKWISE_90,
				Rotation.CLOCKWISE_180,
				Rotation.COUNTERCLOCKWISE_90
		};
		for (int index = 0;
				index < containedRotations.length;
				index++) {
			require(helper,
					CookieCrumbGroveFeature
							.fitsWithinChunk(
									containedCentres[index],
									containedRotations[index],
									fixtureChunk)
							&& !CookieCrumbGroveFeature
									.fitsWithinChunk(
											containedCentres[index]
													.offset(
															-1,
															0,
															-1),
											containedRotations[index],
											fixtureChunk),
					"Cookie Crumb Grove lost its complete in-chunk boundary for "
							+ containedRotations[index]);
		}

		prepareFixture(level, fixture);
		Rotation unsafeRotation = Rotation.CLOCKWISE_90;
		level.setBlock(local(fixture, unsafeRotation,
				1, 0, 10),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!CookieCrumbGroveFeature.hasSafeFootprint(
						level, fixture,
						unsafeRotation),
				"Cookie Crumb Grove accepted a flooded rotated path footprint");

		prepareFixture(level, fixture);
		BlockPos protectedCache = local(fixture,
				Rotation.CLOCKWISE_180,
				4, -1, 9);
		level.setBlock(protectedCache,
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper,
				!CookieCrumbGroveFeature.hasSafeFootprint(
						level, fixture,
						Rotation.CLOCKWISE_180),
				"Cookie Crumb Grove accepted a block-entity-bearing authored footprint");

		prepareFixture(level, fixture);
		BlockPos inheritedTrunk = local(fixture,
				Rotation.NONE, 5, 1, 9);
		level.setBlock(inheritedTrunk,
				Blocks.OAK_LOG.defaultBlockState(), 2);
		level.setBlock(inheritedTrunk.above(),
				Blocks.OAK_LEAVES.defaultBlockState(), 2);
		require(helper,
				CookieCrumbGroveFeature.hasSafeFootprint(
						level, fixture,
						Rotation.NONE)
						&& CookieCrumbGroveFeature.buildAt(
								level,
								new Random(
										CookieCrumbGroveFeature
												.PLACEMENT_SALT),
								fixture,
								Rotation.NONE)
						&& level.getBlockState(
								inheritedTrunk).isAir()
						&& level.getBlockState(
								inheritedTrunk.above())
								.isAir(),
				"Cookie Crumb Grove did not safely replace inherited Forest logs and leaves");

		for (Rotation rotation : Rotation.values()) {
			prepareFixture(level, fixture);
			int entitiesBefore = level.getEntities(
					(Entity) null,
					new AABB(fixture).inflate(14.0D))
					.size();
			require(helper,
					CookieCrumbGroveFeature.buildAt(
							level,
							new Random(
									CookieCrumbGroveFeature
											.PLACEMENT_SALT),
							fixture, rotation),
					"Cookie Crumb Grove refused a safe "
							+ rotation + " fixture");
			Map<Block, Integer> palette =
					scanPalette(level, fixture, 12,
							4, 10);
			BlockPos cache =
					CookieCrumbGroveFeature
							.cachePosition(fixture);
			BlockEntity cacheEntity =
					level.getBlockEntity(cache);
			String loot = cacheEntity == null ? ""
					: cacheEntity.saveWithoutMetadata()
							.getString("LootTable");
			BlockPos entrance = local(fixture,
					rotation, 0, 1, 6);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.WAFER_BLOCK.get(),
							0) >= 60
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_STONE
											.get(),
									0) >= 220
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CHOCOLATE_SPONGE
											.get(),
									0) >= 40
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_CRUMBS
											.get(),
									0) == 15
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 1
							&& CookieCrumbGroveFeature
									.LOOT_ID.toString()
									.equals(loot)
							&& level.getBlockState(
									entrance).isAir()
							&& level.getBlockState(
									entrance.above())
									.isAir()
							&& level.getEntities(
									(Entity) null,
									new AABB(fixture)
											.inflate(14.0D))
									.size()
									== entitiesBefore,
					"Cookie Crumb Grove lost its four trees, biscuit canopies, accessible burrow, exact path, single cache or no-entity boundary: "
							+ rotation + " "
							+ palette);
		}

		LootTable loot = level.getServer().getLootTables()
				.get(CookieCrumbGroveFeature.LOOT_ID);
		LootContext context =
				new LootContext.Builder(level)
						.withParameter(
								LootContextParams.ORIGIN,
								Vec3.atCenterOf(fixture))
						.create(LootContextParamSets.CHEST);
		List<ItemStack> supplies =
				loot.getRandomItems(context);
		int biscuits = supplies.stream()
				.filter(stack -> stack.is(
						CakeWorldItems.SIMPLE_BISCUIT.get()))
				.mapToInt(ItemStack::getCount)
				.sum();
		require(helper, biscuits >= 3 && biscuits <= 6
						&& supplies.stream().anyMatch(
								stack -> stack.is(
										Blocks.TORCH
												.asItem()))
						&& supplies.stream().anyMatch(
								stack -> stack.is(
										CakeWorldBlocks
												.WAFER_BLOCK
												.get()
												.asItem())),
				"Cookie Crumb Burrow lost its guaranteed signature biscuits or recovery supplies");
		helper.succeed();
	}

	private static void prepareFixture(ServerLevel level,
			BlockPos centre) {
		for (int x = -13; x <= 13; x++) {
			for (int z = -13; z <= 13; z++) {
				for (int y = -4; y <= 11; y++) {
					level.setBlock(
							centre.offset(x, y, z),
							Blocks.AIR
									.defaultBlockState(),
							2);
				}
				level.setBlock(centre.offset(x, 0, z),
						Blocks.STONE.defaultBlockState(),
						2);
			}
		}
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre,
			int horizontalRadius, int buriedDepth,
			int topHeight) {
		Map<Block, Integer> result = new LinkedHashMap<>();
		for (int x = -horizontalRadius;
				x <= horizontalRadius; x++) {
			for (int y = -buriedDepth;
					y <= topHeight; y++) {
				for (int z = -horizontalRadius;
						z <= horizontalRadius; z++) {
					result.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(), 1,
							Integer::sum);
				}
			}
		}
		return result;
	}

	private static boolean hasPlacedFeature(ServerLevel level,
			ResourceLocation biomeId) {
		Biome biome = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(biomeId);
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
					.map(key -> key.location().equals(
							CookieCrumbGroveFeature.ID))
					.orElse(false)) {
				return true;
			}
		}
		return false;
	}

	private static void requireSpawnReplacement(
			GameTestHelper helper, Biome biome,
			EntityType<?> vanilla,
			EntityType<?> replacement,
			MobCategory category) {
		boolean foundVanilla = false;
		boolean foundReplacement = false;
		for (MobSpawnSettings.SpawnerData spawn
				: biome.getMobSettings()
						.getMobs(category).unwrap()) {
			foundVanilla |= spawn.type == vanilla;
			foundReplacement |= spawn.type == replacement;
		}
		require(helper, !foundVanilla && foundReplacement,
				"Cookie Crumb Forest did not replace "
						+ Registry.ENTITY_TYPE
								.getKey(vanilla)
						+ " with "
						+ Registry.ENTITY_TYPE
								.getKey(replacement));
	}

	private static int readPrivateInt(
			Object instance, String name) {
		try {
			Field field = instance.getClass()
					.getDeclaredField(name);
			field.setAccessible(true);
			return field.getInt(instance);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not read " + name
							+ " from "
							+ instance.getClass(),
					exception);
		}
	}

	private static BlockPos local(BlockPos centre,
			Rotation rotation, int x, int y, int z) {
		return switch (rotation) {
		case NONE -> centre.offset(x, y, z);
		case CLOCKWISE_90 -> centre.offset(-z, y, x);
		case CLOCKWISE_180 -> centre.offset(-x, y, -z);
		case COUNTERCLOCKWISE_90 ->
			centre.offset(z, y, -x);
		};
	}

	private static Set<String> stringSet(JsonArray array) {
		java.util.LinkedHashSet<String> values =
				new java.util.LinkedHashSet<>();
		for (JsonElement element : array) {
			values.add(element.getAsString());
		}
		return Set.copyOf(values);
	}

	private static JsonObject readObject(String path) {
		try (InputStream stream = CookieCrumbForestGameTests.class
				.getResourceAsStream(path)) {
			if (stream == null) {
				throw new IllegalStateException(
						"Missing packaged resource "
								+ path);
			}
			try (InputStreamReader reader =
					new InputStreamReader(stream,
							StandardCharsets.UTF_8)) {
				JsonElement root =
						JsonParser.parseReader(reader);
				if (!root.isJsonObject()) {
					throw new IllegalStateException(
							"Expected a JSON object in "
									+ path);
				}
				return root.getAsJsonObject();
			}
		} catch (IOException | RuntimeException exception) {
			throw new IllegalStateException(
					"Could not read packaged resource "
							+ path,
					exception);
		}
	}

	private static boolean close(double actual,
			double expected) {
		return Math.abs(actual - expected)
				< 0.00001D;
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
