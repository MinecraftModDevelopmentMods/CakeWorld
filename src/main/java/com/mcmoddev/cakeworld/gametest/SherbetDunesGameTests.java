package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.SherbetPowderBlock;
import com.mcmoddev.cakeworld.entity.SherbetOcelot;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.SherbetFossilBowlFeature;
import com.mcmoddev.cakeworld.world.SherbetPyramidFeature;
import com.mcmoddev.cakeworld.world.SherbetPyramidRepairFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class SherbetDunesGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation BIOME_ID =
			id("sherbet_dunes");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private SherbetDunesGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow007")
	public static void dunesHaveDryFizzEcologyAndProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome dunes = registry.get(BIOME_ID);
		Biome desert = registry.get(
				new ResourceLocation("minecraft", "desert"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, dunes != null && desert != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.DESERT
						&& close(dunes.getBaseTemperature(), 2.0D)
						&& close(dunes.getDownfall(), 0.0D),
				"Sherbet Dunes is not a hot dry Desert-derived biome");
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
								BiomeDictionary.Type.WASTELAND),
				"Sherbet Dunes dictionary roles are incomplete");
		AmbientAdditionsSettings ambience =
				dunes.getAmbientAdditions().orElse(null);
		AmbientParticleSettings dust =
				dunes.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.SHERBET_DUNES_FIZZ
										.getId())
						&& close(ambience.getTickChance(),
								0.0012D)
						&& dust != null
						&& dust.getOptions().getType()
								== ParticleTypes.END_ROD,
				"Sherbet Dunes lost its subtitled fizzy dust ambience");

		requireExactReplacement(helper, desert, dunes,
				EntityType.HUSK,
				CakeWorldEntities.DRIED_CRUMBLER.get(),
				MobCategory.MONSTER);
		MobSpawnSettings.SpawnerData ocelot =
				findSpawn(dunes,
						CakeWorldEntities.SHERBET_OCELOT.get(),
						MobCategory.MONSTER);
		require(helper, ocelot != null
						&& ocelot.getWeight().asInt() == 1
						&& ocelot.minCount == 1
						&& ocelot.maxCount == 1
						&& findSpawn(dunes, EntityType.OCELOT,
								MobCategory.MONSTER) == null,
				"Sherbet Dunes lost its rare exact Sherbet Ocelot role");

		Set<ResourceLocation> eligible = registry
				.getTag(SherbetPyramidFeature.GENERATES_IN)
				.map(tag -> tag.stream()
						.map(entry -> entry.unwrapKey()
								.orElseThrow().location())
						.collect(Collectors.toSet()))
				.orElse(Set.of());
		require(helper, eligible.equals(Set.of(BIOME_ID))
						&& holder.is(
								SherbetPyramidFeature.GENERATES_IN)
						&& !registry.getHolder(
								ResourceKey.create(
										Registry.BIOME_REGISTRY,
										CakeWorldBiomes
												.CANDY_PLAINS
												.getId()))
								.orElseThrow()
								.is(SherbetPyramidFeature
										.GENERATES_IN)
						&& hasPlacedFeature(dunes,
								SherbetPyramidRepairFeature.ID),
				"Sherbet Pyramid did not migrate exclusively to Sherbet Dunes");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 17,
				"Sherbet Dunes requires provider revision 17");
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
								.getAsInt() == 4
							&& geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsInt() == 12
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.25D)
							&& palette
									.getAsJsonArray(
											"similar_biomes")
									.size() == 1
							&& "minecraft:desert".equals(
									palette.getAsJsonArray(
											"similar_biomes")
											.get(0)
											.getAsString())
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
							&& "cakeworld:raspberry_sherbet_powder"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:lemon_sherbet_powder"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:orange_sherbet_powder"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5,
					template + " lost the Sherbet Dunes profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Sherbet profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow007",
			timeoutTicks = 800)
	public static void fossilBowlIsBoundedFizzyAndDiscoverable(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				SherbetFossilBowlFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== SherbetFossilBowlFeature.FEATURE,
				"Sherbet fossil-bowl feature was not registered");
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
				"Sherbet fossil bowl lost its rare surface-biome chain");
		Biome dunes = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(BIOME_ID);
		require(helper, hasPlacedFeature(dunes,
						SherbetFossilBowlFeature.ID),
				"Sherbet fossil bowl is not installed in Sherbet Dunes");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 20,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		ChunkPos chunk = new ChunkPos(fixture);
		BlockPos contained = new BlockPos(
				chunk.getMinBlockX() + 5,
				fixture.getY(),
				chunk.getMinBlockZ() + 5);
		for (Rotation rotation : Rotation.values()) {
			require(helper,
					SherbetFossilBowlFeature
							.fitsWithinChunk(contained,
									rotation, chunk),
					"Sherbet fossil bowl crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					SherbetFossilBowlFeature.buildAt(
							level, new Random(1978070L),
							fixture, rotation),
					"Sherbet fossil bowl refused a safe fixture");
			Map<Block, Integer> palette =
					scan(level, fixture);
			BlockPos jar =
					SherbetFossilBowlFeature.jarPosition(
							fixture, rotation);
			BlockEntity jarEntity =
					level.getBlockEntity(jar);
			CompoundTag jarState = jarEntity == null
					? new CompoundTag()
					: jarEntity.saveWithoutMetadata();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_SHERBET_POWDER
									.get(), 0) == 28
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ORANGE_SHERBET_POWDER
											.get(), 0) == 27
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LEMON_SHERBET_POWDER
											.get(), 0) == 28
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIME_SHERBET_POWDER
											.get(), 0) == 27
							&& palette.getOrDefault(
									CakeWorldBlocks.WAFER_BLOCK
											.get(), 0) == 11
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ROCK_CANDY_FOSSIL
											.get(), 0) == 25
							&& palette.getOrDefault(
									CakeWorldBlocks.FIZZY_PEARL
											.get(), 0) == 5
							&& palette.getOrDefault(
									Blocks.BARREL, 0) == 1
							&& SherbetFossilBowlFeature
									.LOOT_ID.toString()
									.equals(jarState
											.getString("LootTable"))
							&& jarState.getString("CustomName")
									.contains(
											"buried_sherbet_jar")
							&& level.getEntities((Entity) null,
									new AABB(fixture)
											.inflate(8.0D))
									.size() == entities,
					"Sherbet fossil bowl lost its exact stripes, fossil, route, buried jar or no-entity contract: "
							+ rotation + " " + palette
							+ " jar=" + jarState);
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 2, 2),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!SherbetFossilBowlFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Sherbet fossil bowl accepted an authored solid obstacle");

		prepare(level, fixture);
		ArmorStand walker = new ArmorStand(level,
				fixture.getX(), fixture.getY() + 1.0D,
				fixture.getZ());
		Vec3 movement = new Vec3(0.3D, -0.4D, -0.2D);
		walker.setDeltaMovement(movement);
		SherbetPowderBlock powder = (SherbetPowderBlock)
				CakeWorldBlocks.RASPBERRY_SHERBET_POWDER.get();
		powder.stepOn(level, fixture,
				powder.defaultBlockState(), walker);
		require(helper,
				walker.hasEffect(CakeWorldEffects.FIZZY_FEET.get())
						&& walker.getEffect(
								CakeWorldEffects.FIZZY_FEET.get())
								.getDuration()
								== SherbetPowderBlock
										.FIZZY_FEET_TICKS
						&& walker.getDeltaMovement()
								.equals(movement)
						&& powder.defaultBlockState()
								.is(SherbetOcelot.SPAWNABLE_ON),
				"Sherbet powder lost its brief non-launching Fizzy Feet or Ocelot surface contract");
		helper.succeed();
	}

	private static void prepare(ServerLevel level,
			BlockPos centre) {
		for (int x = -7; x <= 7; x++) {
			for (int z = -7; z <= 7; z++) {
				for (int y = -3; y <= 7; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.RASPBERRY_SHERBET_POWDER
								.get().defaultBlockState(),
						2);
				level.setBlock(centre.offset(x, -1, z),
						CakeWorldBlocks.BISCUIT_STONE.get()
								.defaultBlockState(),
						2);
			}
		}
	}

	private static Map<Block, Integer> scan(ServerLevel level,
			BlockPos centre) {
		Map<Block, Integer> counts = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = -1; y <= 5; y++) {
				for (int z = -5; z <= 5; z++) {
					counts.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(), 1, Integer::sum);
				}
			}
		}
		return counts;
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
				"Sherbet Dunes did not exactly replace "
						+ Registry.ENTITY_TYPE.getKey(vanilla));
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
				SherbetDunesGameTests.class.getResourceAsStream(
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
