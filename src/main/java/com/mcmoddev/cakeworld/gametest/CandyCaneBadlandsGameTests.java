package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CandyCaneHoodooGardenFeature;
import com.mcmoddev.cakeworld.world.RockCandyCrystalMineFeature;
import com.mcmoddev.cakeworld.world.RockCandyCrystalMineRepairFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class CandyCaneBadlandsGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation BIOME_ID =
			id("candy_cane_badlands");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private CandyCaneBadlandsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow008")
	public static void badlandsHaveStripedGeologyEcologyAndMine(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome badlands = registry.get(BIOME_ID);
		Biome source = registry.get(
				new ResourceLocation("minecraft", "badlands"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, badlands != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.MESA
						&& close(badlands.getBaseTemperature(), 2.0D)
						&& close(badlands.getDownfall(), 0.0D),
				"Candy-Cane Badlands is not a hot dry Badlands-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.HOT)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.DRY)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SANDY)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.MESA)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.WASTELAND),
				"Candy-Cane Badlands dictionary roles are incomplete");
		AmbientAdditionsSettings ambience =
				badlands.getAmbientAdditions().orElse(null);
		AmbientParticleSettings sparkle =
				badlands.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.CANDY_CANE_BADLANDS_CHIME
										.getId())
						&& close(ambience.getTickChance(), 0.001D)
						&& sparkle != null
						&& sparkle.getOptions().getType()
								== ParticleTypes.CRIT,
				"Candy-Cane Badlands lost its subtitled chiming heat haze");

		requireExactReplacement(helper, source, badlands,
				EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, badlands,
				EntityType.SKELETON,
				CakeWorldEntities.CANDY_CANE_ARCHER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, badlands,
				EntityType.CREEPER,
				CakeWorldEntities.POP_ROCK_POPPER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, badlands,
				EntityType.SPIDER,
				CakeWorldEntities.LIQUORICE_WEAVER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, badlands,
				EntityType.WITCH,
				CakeWorldEntities.BITTER_BAKER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, badlands,
				EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, source, badlands,
				EntityType.BAT,
				CakeWorldEntities.BONBON_BAT.get(),
				MobCategory.AMBIENT);

		Set<ResourceLocation> eligible = registry
				.getTag(RockCandyCrystalMineFeature.GENERATES_IN)
				.map(tag -> tag.stream()
						.map(entry -> entry.unwrapKey()
								.orElseThrow().location())
						.collect(Collectors.toSet()))
				.orElse(Set.of());
		Holder<Biome> peaks = registry.getHolder(
				ResourceKey.create(Registry.BIOME_REGISTRY,
						CakeWorldBiomes.MARSHMALLOW_PEAKS.getId()))
				.orElseThrow();
		require(helper, eligible.equals(Set.of(BIOME_ID))
						&& holder.is(
								RockCandyCrystalMineFeature
										.GENERATES_IN)
						&& !peaks.is(
								RockCandyCrystalMineFeature
										.GENERATES_IN)
						&& hasPlacedFeature(badlands,
								RockCandyCrystalMineRepairFeature
										.ID),
				"Rock-Candy Crystal Mine did not migrate exclusively to Candy-Cane Badlands");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 18,
				"Candy-Cane Badlands requires provider revision 18");
		JsonObject templates = provider.getAsJsonObject("templates");
		JsonObject first = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = templates.getAsJsonObject(template)
					.getAsJsonObject("profile");
			JsonObject geomes = profile
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					geomes.get("cakeworld:wafer_shelf")
								.getAsInt() == 6
							&& geomes.get(
									"cakeworld:peppermint_fold")
									.getAsInt() == 10
							&& geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsInt() == 14
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.25D)
							&& palette.getAsJsonArray(
									"similar_biomes")
									.size() == 3
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), 1.5D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 2.0D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.0D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 0.2D)
							&& "cakeworld:wafer_rock"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:candy_cane_pillar"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:peppermint_rock"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 7,
					template
							+ " lost the Candy-Cane Badlands profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Badlands profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow008",
			timeoutTicks = 800)
	public static void hoodooGardenIsBoundedStripedAndRecoverable(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CandyCaneHoodooGardenFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== CandyCaneHoodooGardenFeature
										.FEATURE,
				"Candy-Cane hoodoo-garden feature was not registered");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0)
								instanceof RarityFilter
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3)
								instanceof BiomeFilter,
				"Candy-Cane hoodoo garden lost its rare surface-biome chain");
		Biome badlands = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(BIOME_ID);
		require(helper, hasPlacedFeature(badlands,
						CandyCaneHoodooGardenFeature.ID),
				"Candy-Cane hoodoo garden is not installed in its biome");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 24,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		ChunkPos chunk = new ChunkPos(fixture);
		BlockPos contained = new BlockPos(
				chunk.getMinBlockX() + 5,
				fixture.getY(),
				chunk.getMinBlockZ() + 5);
		Set<Rotation> orientations =
				new java.util.HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					CandyCaneHoodooGardenFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 71, 80,
									index * -79)));
		}
		require(helper, orientations.size() == 4,
				"Candy-Cane hoodoo garden did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					CandyCaneHoodooGardenFeature
							.fitsWithinChunk(contained,
									rotation, chunk),
					"Candy-Cane hoodoo garden crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					CandyCaneHoodooGardenFeature.buildAt(
							level, new Random(1978071L),
							fixture, rotation),
					"Candy-Cane hoodoo garden refused a safe fixture");
			assertCourt(helper, level, fixture, rotation);
			assertHoodoos(helper, level, fixture, rotation);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture)
									.inflate(8.0D))
							.size() == entities,
					"Candy-Cane hoodoo garden created an entity");
			for (int x = -5; x <= 5; x++) {
				for (int y = 0; y <= 9; y++) {
					for (int z = -5; z <= 5; z++) {
						require(helper,
								level.getBlockEntity(
										CandyCaneHoodooGardenFeature
												.local(fixture,
														rotation,
														x, y, z))
										== null,
								"Candy-Cane hoodoo garden created a block entity");
					}
				}
			}
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 2, 2),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!CandyCaneHoodooGardenFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Candy-Cane hoodoo garden accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!CandyCaneHoodooGardenFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Candy-Cane hoodoo garden accepted a wet site");
		helper.succeed();
	}

	private static void assertCourt(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				BlockPos position =
						CandyCaneHoodooGardenFeature.local(
								centre, rotation, x, 0, z);
				if (x == 0) {
					require(helper,
							level.getBlockState(position)
									.is(CakeWorldBlocks
											.WAFER_BLOCK
											.get()),
							"Hoodoo garden lost its walkable Wafer path");
				} else if (z == 0 && Math.abs(x) == 1) {
					require(helper,
							level.getBlockState(position)
									.is(CakeWorldBlocks
											.MARSHMALLOW
											.get()),
							"Hoodoo garden lost a recovery pad");
				} else {
					require(helper,
							level.getBlockState(position)
									.equals(
											CandyCaneHoodooGardenFeature
													.courtState(
															x, z)),
							"Hoodoo garden lost its four-material court at "
									+ x + "," + z);
				}
			}
		}
		for (int z : new int[] {-5, 5}) {
			require(helper,
					level.getBlockState(
							CandyCaneHoodooGardenFeature
									.local(centre, rotation,
											0, 0, z))
							.is(CakeWorldBlocks.WAFER_BLOCK.get()),
					"Hoodoo garden lost a path endpoint");
		}
	}

	private static void assertHoodoos(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int[] hoodoo
				: CandyCaneHoodooGardenFeature.hoodoos()) {
			int x = hoodoo[0];
			int z = hoodoo[1];
			int height = hoodoo[2];
			for (int y = 1; y <= height; y++) {
				require(helper,
						level.getBlockState(
								CandyCaneHoodooGardenFeature
										.local(centre, rotation,
												x, y, z))
								.is(CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get()),
						"Hoodoo garden lost a striped pillar cell");
			}
			for (int capX = -1; capX <= 1; capX++) {
				for (int capZ = -1; capZ <= 1; capZ++) {
					require(helper,
							level.getBlockState(
									CandyCaneHoodooGardenFeature
											.local(centre,
													rotation,
													x + capX,
													height + 1,
													z + capZ))
									.is(CakeWorldBlocks
											.WAFER_ROCK
											.get()),
							"Hoodoo garden lost a Wafer capstone");
				}
			}
			require(helper,
					level.getBlockState(
							CandyCaneHoodooGardenFeature.local(
									centre, rotation, x,
									height + 2, z))
							.is((hoodoo[3] == 0
									? CakeWorldBlocks
											.ROCK_CANDY_DEPOSIT
											.get()
									: CakeWorldBlocks
											.MINT_CRYSTAL
											.get())),
					"Hoodoo garden lost a crystal marker");
		}
	}

	private static void prepare(ServerLevel level,
			BlockPos centre) {
		for (int x = -7; x <= 7; x++) {
			for (int z = -7; z <= 7; z++) {
				for (int y = -5; y <= 12; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.WAFER_ROCK.get()
								.defaultBlockState(), 2);
				level.setBlock(centre.offset(x, -1, z),
						CakeWorldBlocks.BISCUIT_STONE.get()
								.defaultBlockState(), 2);
			}
		}
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

	private static void requireExactReplacement(
			GameTestHelper helper, Biome source, Biome result,
			EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla, category);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(result, replacement, category);
		require(helper, expected != null && actual != null
						&& findSpawn(result, vanilla, category) == null
						&& expected.getWeight().asInt()
								== actual.getWeight().asInt()
						&& expected.minCount == actual.minCount
						&& expected.maxCount == actual.maxCount,
				"Candy-Cane Badlands did not exactly replace "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ ": expected=" + expected
						+ ", actual=" + actual);
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

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				CandyCaneBadlandsGameTests.class
						.getResourceAsStream(
								"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Unable to read packaged provider", exception);
		}
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}

	private static boolean close(double actual,
			double expected) {
		return Math.abs(actual - expected) < 0.0001D;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
