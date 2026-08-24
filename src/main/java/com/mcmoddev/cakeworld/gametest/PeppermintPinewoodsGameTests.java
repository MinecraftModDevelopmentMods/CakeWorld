package com.mcmoddev.cakeworld.gametest;

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
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.PeppermintClearingFeature;

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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Contract proof for the first functional Peppermint Pinewoods ecosystem.
 */
@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class PeppermintPinewoodsGameTests {
	private static final String EMPTY = "empty";
	private static final String PROVIDER =
			"/data/cakeworld/orespawn/provider.json";
	private static final ResourceLocation BIOME_ID =
			new ResourceLocation(CakeWorld.MODID,
					"peppermint_pinewoods");
	private static final ResourceLocation SOURCE_BIOME_ID =
			new ResourceLocation("minecraft", "snowy_taiga");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final List<String> TEMPLATES = List.of(
			"cakeworld:edible_world",
			"cakeworld:edible_world_basemetals");

	private PeppermintPinewoodsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow004")
	public static void pinewoodsHasColdChimesAndExactSnowyTaigaRoles(
			GameTestHelper helper) {
		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome biome = biomes.get(BIOME_ID);
		Biome source = biomes.get(SOURCE_BIOME_ID);
		Holder<Biome> holder = biomes.getHolder(BIOME_KEY)
				.orElseThrow(() -> new IllegalStateException(
						"Missing biome holder " + BIOME_ID));

		require(helper, biome != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.TAIGA,
				"Peppermint Pinewoods is not a registered snowy-taiga biome");
		require(helper,
				close(biome.getBaseTemperature(), -0.2D)
						&& close(biome.getDownfall(), 0.7D),
				"Peppermint Pinewoods climate does not match its contract");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.FOREST)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.CONIFEROUS)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.COLD)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.SNOWY)
						&& !BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.OCEAN),
				"Peppermint Pinewoods biome-dictionary roles are wrong");

		AmbientAdditionsSettings additions = biome.getAmbientAdditions()
				.orElse(null);
		require(helper, additions != null
						&& additions.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.PEPPERMINT_PINEWOODS_CHIME
										.getId())
						&& close(additions.getTickChance(),
								0.001D),
				"Peppermint Pinewoods does not use its quiet branch chime");

		requireExactReplacement(helper, source, biome,
				EntityType.COW,
				CakeWorldEntities.COCOA_COW.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get(),
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
				EntityType.RABBIT,
				CakeWorldEntities.GUMMY_BUNNY.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.WOLF,
				CakeWorldEntities.GINGER_SNAP_HOUND.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.FOX,
				CakeWorldEntities.PEPPERMINT_FOX.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, source, biome,
				EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, biome,
				EntityType.CREEPER,
				CakeWorldEntities.POP_ROCK_POPPER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, biome,
				EntityType.BAT,
				CakeWorldEntities.BONBON_BAT.get(),
				MobCategory.AMBIENT);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow004")
	public static void bothAdventureTemplatesSharePinewoodsProfile(
			GameTestHelper helper) {
		JsonObject provider = readObject(PROVIDER);
		require(helper,
				provider.get("provider_revision").getAsInt() >= 14,
				"Peppermint Pinewoods requires OreSpawn provider revision 14 or later");
		JsonObject templates = provider.getAsJsonObject("templates");
		for (String templateId : TEMPLATES) {
			JsonObject profile = templates.getAsJsonObject(templateId)
					.getAsJsonObject("profile");
			JsonObject geomes = profile.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					close(geomes.get("cakeworld:peppermint_fold")
							.getAsDouble(), 12.0D)
							&& close(geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsDouble(), 3.0D),
					templateId
							+ " does not preserve the Pinewoods geome bias");

			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					palette.get("enabled").getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.5D)
							&& stringSet(palette
									.getAsJsonArray(
											"similar_biomes"))
									.equals(Set.of(
											"minecraft:snowy_taiga",
											"minecraft:taiga",
											"minecraft:old_growth_pine_taiga"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.isEmpty()
							&& close(palette.get(
									"min_temperature")
									.getAsDouble(), -2.0D)
							&& close(palette.get(
									"max_temperature")
									.getAsDouble(), 0.3D)
							&& close(palette.get(
									"min_downfall")
									.getAsDouble(), 0.35D)
							&& close(palette.get(
									"max_downfall")
									.getAsDouble(), 1.0D),
					templateId
							+ " does not preserve the Pinewoods selection contract");
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					"cakeworld:icing".equals(
							surface.get("top_block")
									.getAsString())
							&& "cakeworld:peppermint_rock"
									.equals(surface.get(
											"filler_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface.get(
											"underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5,
					templateId
							+ " does not preserve the frosted Peppermint surface");
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow004",
			timeoutTicks = 1200)
	public static void peppermintClearingIsBoundedSafeAndDeterministic(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				PeppermintClearingFeature.placedFeature();
		require(helper, placed != null,
				"Peppermint Clearing placed feature was not registered");
		List<PlacementModifier> modifiers =
				placed.value().placement();
		require(helper,
				placed.unwrapKey()
						.map(key -> key.location().equals(
								PeppermintClearingFeature.ID))
						.orElse(false)
						&& placed.value().feature().value()
								.feature()
								== PeppermintClearingFeature.FEATURE
						&& modifiers.size() == 4
						&& modifiers.get(0)
								instanceof RarityFilter
						&& readPrivateInt(modifiers.get(0),
								"chance")
								== PeppermintClearingFeature
										.AVERAGE_CHUNKS_PER_ATTEMPT
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3)
								instanceof BiomeFilter,
				"Peppermint Clearing lost its one-in-18 spread/surface/biome placement chain");

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
						"Peppermint Clearing crossed into "
								+ entry.getKey().location());
			}
		}
		require(helper, owningBiomes == 1,
				"Peppermint Clearing is not owned by exactly one biome");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 20,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		Set<Rotation> observed =
				new java.util.HashSet<>();
		for (int sample = 0;
				sample < 128 && observed.size() < 4;
				sample++) {
			observed.add(PeppermintClearingFeature
					.orientation(level.getSeed(),
							fixture.offset(sample * 37,
									0, sample * -43)));
		}
		require(helper, observed.size() == 4,
				"Peppermint Clearing did not expose four deterministic cardinal presentations");

		ChunkPos fixtureChunk = new ChunkPos(fixture);
		BlockPos contained = new BlockPos(
				fixtureChunk.getMinBlockX() + 5,
				fixture.getY(),
				fixtureChunk.getMinBlockZ() + 5);
		for (Rotation rotation : Rotation.values()) {
			require(helper,
					PeppermintClearingFeature
							.fitsWithinChunk(
									contained,
									rotation,
									fixtureChunk)
							&& !PeppermintClearingFeature
									.fitsWithinChunk(
											contained.offset(
													-1, 0,
													-1),
											rotation,
											fixtureChunk),
					"Peppermint Clearing lost its complete in-chunk boundary for "
							+ rotation);
		}

		prepareFixture(level, fixture);
		level.setBlock(fixture.offset(2, 0, 2),
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!PeppermintClearingFeature.hasSafeFootprint(
						level, fixture,
						Rotation.CLOCKWISE_90),
				"Peppermint Clearing accepted a flooded footprint");

		prepareFixture(level, fixture);
		level.setBlock(fixture.offset(-2, 1, -2),
				Blocks.CHEST.defaultBlockState(), 2);
		require(helper,
				!PeppermintClearingFeature.hasSafeFootprint(
						level, fixture,
						Rotation.CLOCKWISE_180),
				"Peppermint Clearing accepted an authored block entity");

		prepareFixture(level, fixture);
		level.setBlock(fixture.offset(-3, 2, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!PeppermintClearingFeature.hasSafeFootprint(
						level, fixture,
						Rotation.COUNTERCLOCKWISE_90),
				"Peppermint Clearing accepted a solid authored block");

		prepareFixture(level, fixture);
		BlockPos inherited = fixture.offset(4, 1, 4);
		level.setBlock(inherited,
				Blocks.SPRUCE_LOG.defaultBlockState(), 2);
		level.setBlock(inherited.above(),
				Blocks.SPRUCE_LEAVES.defaultBlockState(),
				2);
		require(helper,
				PeppermintClearingFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE)
						&& PeppermintClearingFeature
								.buildAt(level, fixture,
										Rotation.NONE)
						&& level.getBlockState(inherited)
								.isAir()
						&& level.getBlockState(
								inherited.above()).isAir(),
				"Peppermint Clearing did not safely replace inherited taiga logs and leaves");

		for (Rotation rotation : Rotation.values()) {
			prepareFixture(level, fixture);
			int entitiesBefore = level.getEntities(
					(Entity) null,
					new AABB(fixture).inflate(8.0D))
					.size();
			require(helper,
					PeppermintClearingFeature.buildAt(
							level, fixture, rotation),
					"Peppermint Clearing refused a safe "
							+ rotation + " fixture");
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.ICING_LAYER
									.get(), 0) == 45
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) == 22
							&& palette.getOrDefault(
									CakeWorldBlocks.ICING
											.get(), 0) == 147
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0) == 4
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MINT_CRYSTAL
											.get(), 0) == 1
							&& !hasAnyBlockEntity(
									level, fixture)
							&& level.getEntities(
									(Entity) null,
									new AABB(fixture)
											.inflate(8.0D))
									.size()
									== entitiesBefore,
					"Peppermint Clearing lost its exact three-pine, frosting, chime-post or no-entity contract: "
							+ rotation + " " + palette);
		}
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
				"Peppermint Pinewoods did not exactly replace "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ " with "
						+ Registry.ENTITY_TYPE
								.getKey(replacement));
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
							PeppermintClearingFeature.ID))
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
				for (int y = -1; y <= 10; y++) {
					level.setBlock(
							centre.offset(x, y, z),
							Blocks.AIR
									.defaultBlockState(),
							2);
				}
				level.setBlock(centre.offset(x, -1, z),
						Blocks.STONE.defaultBlockState(),
						2);
				level.setBlock(centre.offset(x, 0, z),
						Blocks.STONE.defaultBlockState(),
						2);
			}
		}
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> result =
				new LinkedHashMap<>();
		for (int x = -7; x <= 7; x++) {
			for (int y = 0; y <= 9; y++) {
				for (int z = -7; z <= 7; z++) {
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
			for (int y = -1; y <= 9; y++) {
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
				PeppermintPinewoodsGameTests.class
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
