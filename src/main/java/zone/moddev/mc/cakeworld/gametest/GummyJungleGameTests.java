package zone.moddev.mc.cakeworld.gametest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.GummyVineBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.GummyJungleBounceGroveFeature;
import zone.moddev.mc.cakeworld.world.GummyShrineFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class GummyJungleGameTests {
	private static final String EMPTY = "empty";
	private static final String PROVIDER =
			"/data/cakeworld/orespawn/provider.json";
	private static final ResourceLocation BIOME_ID =
			new ResourceLocation(CakeWorld.MODID,
					"gummy_jungle");
	private static final ResourceLocation SOURCE_BIOME_ID =
			new ResourceLocation("minecraft",
					"bamboo_jungle");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					BIOME_ID);
	private static final List<String> TEMPLATES = List.of(
			"cakeworld:edible_world",
			"cakeworld:edible_world_basemetals");

	private GummyJungleGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow005")
	public static void jungleHasWarmWobbleAndExactCreatureRoles(
			GameTestHelper helper) {
		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome biome = biomes.get(BIOME_ID);
		Biome source = biomes.get(SOURCE_BIOME_ID);
		Holder<Biome> holder = biomes.getHolder(BIOME_KEY)
				.orElseThrow(() -> new IllegalStateException(
						"Missing biome holder " + BIOME_ID));

		require(helper, biome != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.JUNGLE,
				"Gummy Jungle is not a registered Bamboo-Jungle biome");
		require(helper,
				close(biome.getBaseTemperature(), 0.95D)
						&& close(biome.getDownfall(), 0.95D),
				"Gummy Jungle climate does not match its warm wet contract");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.FOREST)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.DENSE)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.WET)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.HOT)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.JUNGLE)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.LUSH),
				"Gummy Jungle biome-dictionary roles are incomplete");

		AmbientAdditionsSettings additions =
				biome.getAmbientAdditions().orElse(null);
		require(helper, additions != null
						&& additions.getSoundEvent()
								.getLocation()
								.equals(CakeWorldSounds
										.GUMMY_JUNGLE_WOBBLE
										.getId())
						&& close(additions.getTickChance(),
								0.0014D),
				"Gummy Jungle does not use its quiet jelly wobble");

		requireExactReplacement(helper, source, biome,
				EntityType.COW, CakeWorldEntities.COCOA_COW.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.PIG, CakeWorldEntities.TRUFFLE_PIG.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.PARROT,
				CakeWorldEntities.LOLLIPOP_LORIKEET.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.PANDA,
				CakeWorldEntities.CHOCOLATE_PANDA.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.OCELOT,
				CakeWorldEntities.SHERBET_OCELOT.get(),
				MobCategory.MONSTER);
		requireSpawn(helper, biome,
				CakeWorldEntities.GUMMY_BUNNY.get(),
				MobCategory.CREATURE, 4, 2, 3);
		require(helper,
				holder.is(GummyShrineFeature.GENERATES_IN)
						&& !holder.is(
								BiomeTags.HAS_JUNGLE_TEMPLE),
				"Gummy Jungle did not admit only its edible shrine role");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow005")
	public static void bothAdventureTemplatesShareGummyJungleProfile(
			GameTestHelper helper) {
		JsonObject provider = readObject(PROVIDER);
		require(helper,
				provider.get("provider_revision").getAsInt() >= 15,
				"Gummy Jungle requires OreSpawn provider revision 15 or later");
		JsonObject templates =
				provider.getAsJsonObject("templates");
		for (String templateId : TEMPLATES) {
			JsonObject profile = templates
					.getAsJsonObject(templateId)
					.getAsJsonObject("profile");
			JsonObject geomes = profile
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					close(geomes.get(
							"cakeworld:cocoa_basin")
							.getAsDouble(), 3.0D)
							&& close(geomes.get(
									"cakeworld:wafer_shelf")
									.getAsDouble(), 5.0D)
							&& close(geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsDouble(), 7.0D),
					templateId
							+ " does not preserve the three-flavour Gummy Jungle geome bias");
			JsonObject hotDictionary = profile
					.getAsJsonObject("biome_dictionary")
					.getAsJsonObject("HOT");
			require(helper,
					close(hotDictionary.get(
							"cakeworld:fudge_mantle")
							.getAsDouble(), 8.0D),
					templateId
							+ " does not preserve the HOT/Fudge-Mantle dictionary seam inherited by Gummy Jungle");

			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject(
							"cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					palette.get("enabled").getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.25D)
							&& stringSet(palette
									.getAsJsonArray(
											"similar_biomes"))
									.equals(Set.of(
											"minecraft:bamboo_jungle",
											"minecraft:jungle",
											"minecraft:sparse_jungle"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.isEmpty()
							&& close(palette.get(
									"min_temperature")
									.getAsDouble(), 0.7D)
							&& close(palette.get(
									"max_temperature")
									.getAsDouble(), 2.0D)
							&& close(palette.get(
									"min_downfall")
									.getAsDouble(), 0.7D)
							&& close(palette.get(
									"max_downfall")
									.getAsDouble(), 1.0D),
					templateId
							+ " does not preserve the Gummy Jungle selection contract");
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					"cakeworld:gummy_block".equals(
							surface.get("top_block")
									.getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(surface.get(
											"filler_block")
											.getAsString())
							&& "cakeworld:blueberry_gummy_block"
									.equals(surface.get(
											"underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 4,
					templateId
							+ " does not preserve the elastic Gummy Jungle surface");
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow005",
			timeoutTicks = 1200)
	public static void bounceGroveAndVinesAreBoundedSafeAndElastic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				GummyJungleBounceGroveFeature
						.placedFeature();
		require(helper, placed != null,
				"Gummy Jungle Bounce Grove placed feature was not registered");
		List<PlacementModifier> modifiers =
				placed.value().placement();
		require(helper,
				placed.unwrapKey()
						.map(key -> key.location().equals(
								GummyJungleBounceGroveFeature.ID))
						.orElse(false)
						&& placed.value().feature().value()
								.feature()
								== GummyJungleBounceGroveFeature
										.FEATURE
						&& modifiers.size() == 4
						&& modifiers.get(0)
								instanceof RarityFilter
						&& readPrivateInt(modifiers.get(0),
								"chance")
								== GummyJungleBounceGroveFeature
										.AVERAGE_CHUNKS_PER_ATTEMPT
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3)
								instanceof BiomeFilter,
				"Gummy Jungle Bounce Grove lost its one-in-12 spread/surface/biome chain");

		Registry<Biome> biomes = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		int owningBiomes = 0;
		for (Map.Entry<ResourceKey<Biome>, Biome> entry
				: biomes.entrySet()) {
			if (hasPlacedFeature(entry.getValue())) {
				owningBiomes++;
				require(helper,
						entry.getKey().location()
								.equals(BIOME_ID),
						"Gummy Jungle Bounce Grove crossed into "
								+ entry.getKey().location());
			}
		}
		require(helper, owningBiomes == 1,
				"Gummy Jungle Bounce Grove is not owned by exactly one biome");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 24,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		Set<Rotation> observed =
				new java.util.HashSet<>();
		for (int sample = 0;
				sample < 128 && observed.size() < 4;
				sample++) {
			observed.add(GummyJungleBounceGroveFeature
					.orientation(level.getSeed(),
							fixture.offset(sample * 37,
									0, sample * -43)));
		}
		require(helper, observed.size() == 4,
				"Gummy Jungle Bounce Grove did not expose four deterministic presentations");

		ChunkPos fixtureChunk = new ChunkPos(fixture);
		BlockPos contained = new BlockPos(
				fixtureChunk.getMinBlockX() + 5,
				fixture.getY(),
				fixtureChunk.getMinBlockZ() + 5);
		for (Rotation rotation : Rotation.values()) {
			require(helper,
					GummyJungleBounceGroveFeature
							.fitsWithinChunk(contained,
									rotation,
									fixtureChunk)
							&& !GummyJungleBounceGroveFeature
									.fitsWithinChunk(
											contained.offset(
													-1, 0,
													-1),
											rotation,
											fixtureChunk),
					"Gummy Jungle Bounce Grove lost its complete in-chunk boundary for "
							+ rotation);
		}

		prepareFixture(level, fixture);
		level.setBlock(fixture.offset(2, 0, 2),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!GummyJungleBounceGroveFeature
						.hasSafeFootprint(level, fixture,
								Rotation.CLOCKWISE_90),
				"Gummy Jungle Bounce Grove accepted a flooded footprint");

		prepareFixture(level, fixture);
		level.setBlock(fixture.offset(-2, 1, -2),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper,
				!GummyJungleBounceGroveFeature
						.hasSafeFootprint(level, fixture,
								Rotation.CLOCKWISE_180),
				"Gummy Jungle Bounce Grove accepted an authored block entity");

		prepareFixture(level, fixture);
		level.setBlock(fixture.offset(3, 2, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!GummyJungleBounceGroveFeature
						.hasSafeFootprint(level, fixture,
								Rotation.COUNTERCLOCKWISE_90),
				"Gummy Jungle Bounce Grove accepted a solid authored block");

		prepareFixture(level, fixture);
		BlockPos inherited = fixture.offset(5, 1, 5);
		level.setBlock(inherited,
				Blocks.JUNGLE_LOG.defaultBlockState(), 2);
		level.setBlock(inherited.above(),
				Blocks.JUNGLE_LEAVES.defaultBlockState(), 2);
		require(helper,
				GummyJungleBounceGroveFeature
						.hasSafeFootprint(level, fixture,
								Rotation.NONE)
						&& GummyJungleBounceGroveFeature
								.buildAt(level, fixture,
										Rotation.NONE)
						&& level.getBlockState(inherited)
								.isAir()
						&& level.getBlockState(
								inherited.above()).isAir(),
				"Gummy Jungle Bounce Grove did not safely replace inherited jungle vegetation");

		prepareFixture(level, fixture);
		BlockPos hollow = fixture.offset(4, 0, 0);
		BlockPos inheritedGround =
				fixture.offset(-4, 0, 0);
		level.setBlock(inheritedGround,
				Blocks.PODZOL.defaultBlockState(), 2);
		for (int y = 0; y >= -3; y--) {
			level.setBlock(hollow.offset(0, y, 0),
					Blocks.AIR.defaultBlockState(), 2);
		}
		level.setBlock(hollow.below(4),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(),
				2);
		require(helper,
				GummyJungleBounceGroveFeature
						.MAX_TERRAIN_RELIEF == 4
						&& GummyJungleBounceGroveFeature
								.hasSafeFootprint(
										level, fixture,
										Rotation.NONE)
						&& GummyJungleBounceGroveFeature
								.buildAt(level, fixture,
										Rotation.NONE)
						&& level.getBlockState(
								inheritedGround)
								.is(CakeWorldBlocks
										.GUMMY_BLOCK.get())
						&& level.getBlockState(
								hollow.below())
								.is(CakeWorldBlocks
										.CHOCOLATE_SPONGE
										.get())
						&& level.getBlockState(
								hollow.below(4))
								.is(CakeWorldBlocks
										.CHOCOLATE_SPONGE
										.get()),
				"Gummy Jungle Bounce Grove did not accept inherited Podzol or bridge bounded four-block natural relief with a grounded Chocolate Sponge support");

		for (Rotation rotation : Rotation.values()) {
			prepareFixture(level, fixture);
			int entitiesBefore = level.getEntities(
					(Entity) null,
					new AABB(fixture).inflate(8.0D))
					.size();
			require(helper,
					GummyJungleBounceGroveFeature
							.buildAt(level, fixture,
									rotation),
					"Gummy Jungle Bounce Grove refused a safe "
							+ rotation + " fixture");
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.GUMMY_BLOCK
									.get(), 0) == 155
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get(), 0) == 54
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get(), 0) == 54
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get(), 0) == 54
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) == 23
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GUMMY_VINE.get(),
									0) == 21
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_SPROUT
											.get(), 0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CHOCOLATE_SPONGE
											.get(), 0) == 169
							&& !hasAnyBlockEntity(
									level, fixture)
							&& level.getEntities(
									(Entity) null,
									new AABB(fixture)
											.inflate(8.0D))
									.size()
									== entitiesBefore,
					"Gummy Jungle Bounce Grove lost its exact landing disc, three pools, canopies, vines, sprouts or no-entity contract: "
							+ rotation + " " + palette);
		}

		BlockPos vinePos = fixture.offset(1, 5, 1);
		level.setBlock(vinePos.above(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(), 2);
		level.setBlock(vinePos,
				CakeWorldBlocks.GUMMY_VINE.get()
						.defaultBlockState()
						.setValue(
								net.minecraft.world.level
										.block.VineBlock.UP,
								true),
				2);
		BlockState vine = level.getBlockState(vinePos);
		ArmorStand falling = new ArmorStand(level,
				vinePos.getX() + 0.5D,
				vinePos.getY() + 0.5D,
				vinePos.getZ() + 0.5D);
		falling.fallDistance = 6.0F;
		falling.setDeltaMovement(0.1D, -0.8D, -0.1D);
		((GummyVineBlock) CakeWorldBlocks.GUMMY_VINE.get())
				.entityInside(vine, level, vinePos, falling);
		require(helper,
				vine.is(BlockTags.CLIMBABLE)
						&& vine.canSurvive(level, vinePos)
						&& !vine.isRandomlyTicking()
						&& close(falling.fallDistance, 0.0D)
						&& close(falling.getDeltaMovement().y,
								0.28D)
						&& level.getBlockState(
								fixture.offset(-4, 1, 0))
								.getValue(CropBlock.AGE)
								== 7,
				"Gummy Vine lost its stable climbable elastic rescue or Candy Sprouts lost gummy-ground support");
		helper.succeed();
	}

	private static void requireExactReplacement(
			GameTestHelper helper, Biome source, Biome result,
			EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla, category);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(result, replacement, category);
		require(helper,
				expected != null && actual != null
						&& findSpawn(result, vanilla,
								category) == null
						&& expected.getWeight().asInt()
								== actual.getWeight().asInt()
						&& expected.minCount
								== actual.minCount
						&& expected.maxCount
								== actual.maxCount,
				"Gummy Jungle did not exactly replace "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ " with "
						+ Registry.ENTITY_TYPE
								.getKey(replacement));
	}

	private static void requireSpawn(GameTestHelper helper,
			Biome biome, EntityType<?> type,
			MobCategory category, int weight,
			int minimum, int maximum) {
		MobSpawnSettings.SpawnerData spawn =
				findSpawn(biome, type, category);
		require(helper,
				spawn != null
						&& spawn.getWeight().asInt() == weight
						&& spawn.minCount == minimum
						&& spawn.maxCount == maximum,
				"Gummy Jungle lost the expected "
						+ Registry.ENTITY_TYPE.getKey(type)
						+ " spawn profile");
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type,
			MobCategory category) {
		for (MobSpawnSettings.SpawnerData spawn
				: biome.getMobSettings()
						.getMobs(category).unwrap()) {
			if (spawn.type == type) {
				return spawn;
			}
		}
		return null;
	}

	private static boolean hasPlacedFeature(Biome biome) {
		int step = GenerationStep.Decoration
				.TOP_LAYER_MODIFICATION.ordinal();
		if (biome.getGenerationSettings()
				.features().size() <= step) {
			return false;
		}
		for (Holder<PlacedFeature> feature
				: biome.getGenerationSettings()
						.features().get(step)) {
			if (feature.unwrapKey()
					.map(key -> key.location().equals(
							GummyJungleBounceGroveFeature.ID))
					.orElse(false)) {
				return true;
			}
		}
		return false;
	}

	private static void prepareFixture(ServerLevel level,
			BlockPos centre) {
		for (int x = -8; x <= 8; x++) {
			for (int z = -8; z <= 8; z++) {
				for (int y = -2; y <= 12; y++) {
					level.setBlock(
							centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(),
							2);
				}
				level.setBlock(centre.offset(x, -1, z),
						CakeWorldBlocks.CHOCOLATE_SPONGE
								.get().defaultBlockState(),
						2);
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.GUMMY_BLOCK.get()
								.defaultBlockState(),
						2);
			}
		}
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> result =
				new LinkedHashMap<>();
		for (int x = -6; x <= 6; x++) {
			for (int y = -1; y <= 11; y++) {
				for (int z = -6; z <= 6; z++) {
					result.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(), 1,
							Integer::sum);
				}
			}
		}
		return result;
	}

	private static boolean hasAnyBlockEntity(
			ServerLevel level, BlockPos centre) {
		for (int x = -5; x <= 5; x++) {
			for (int y = -2; y <= 11; y++) {
				for (int z = -5; z <= 5; z++) {
					if (level.getBlockEntity(
							centre.offset(x, y, z))
							!= null) {
						return true;
					}
				}
			}
		}
		return false;
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

	private static Set<String> stringSet(JsonArray array) {
		java.util.LinkedHashSet<String> values =
				new java.util.LinkedHashSet<>();
		for (JsonElement element : array) {
			values.add(element.getAsString());
		}
		return Set.copyOf(values);
	}

	private static JsonObject readObject(String path) {
		try (InputStream stream =
				GummyJungleGameTests.class
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
		return Math.abs(actual - expected) < 0.00001D;
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
