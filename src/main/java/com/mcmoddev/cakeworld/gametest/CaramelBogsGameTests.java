package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.CaramelCrustBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.CaramelBogMangroveFeature;
import com.mcmoddev.cakeworld.world.CaramelCottageFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
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
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class CaramelBogsGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation BIOME_ID =
			id("caramel_bogs");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private CaramelBogsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow006")
	public static void bogHasWetAmbienceEcologyAndProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome bog = registry.get(BIOME_ID);
		Biome swamp = registry.get(
				new ResourceLocation("minecraft", "swamp"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, bog != null && swamp != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.SWAMP
						&& close(bog.getBaseTemperature(), 0.8D)
						&& close(bog.getDownfall(), 0.9D),
				"Caramel Bogs is not a warm wet Swamp-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SWAMP)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.WET),
				"Caramel Bogs dictionary roles are incomplete");
		AmbientAdditionsSettings ambience =
				bog.getAmbientAdditions().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.CARAMEL_BOGS_BUBBLE
										.getId())
						&& close(ambience.getTickChance(),
								0.0012D),
				"Caramel Bogs lost its quiet bubbling ambience");
		requireExactReplacement(helper, swamp, bog,
				EntityType.SLIME, CakeWorldEntities.JELLY_BLOB.get(),
				MobCategory.MONSTER);
		requireExactReplacement(helper, swamp, bog,
				EntityType.COW, CakeWorldEntities.COCOA_COW.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, swamp, bog,
				EntityType.PIG, CakeWorldEntities.TRUFFLE_PIG.get(),
				MobCategory.CREATURE);
		requireExactReplacement(helper, swamp, bog,
				EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get(),
				MobCategory.CREATURE);
		require(helper, holder.is(CaramelCottageFeature.GENERATES_IN),
				"Caramel Bogs did not admit the Caramel Cottage");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 16,
				"Caramel Bogs requires provider revision 16");
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
			require(helper,
					geomes.get("cakeworld:cocoa_basin")
								.getAsInt() == 8
							&& geomes.get(
									"cakeworld:wafer_shelf")
									.getAsInt() == 8
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.5D)
							&& "cakeworld:caramel_crust"
									.equals(palette
											.getAsJsonObject(
													"surface")
											.get("top_block")
											.getAsString())
							&& "cakeworld:chocolate_sponge"
									.equals(palette
											.getAsJsonObject(
													"surface")
											.get("filler_block")
											.getAsString()),
					template + " lost the Caramel Bogs profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Caramel profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow006",
			timeoutTicks = 800)
	public static void mangroveRouteIsBoundedStickyAndRecoverable(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				CaramelBogMangroveFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== CaramelBogMangroveFeature.FEATURE,
				"Caramel mangrove feature was not registered");
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
				"Caramel mangrove lost its rare surface-biome chain");
		require(helper, hasPlacedFeature(
						level.registryAccess()
								.registryOrThrow(
										Registry.BIOME_REGISTRY)
								.get(BIOME_ID)),
				"Caramel mangrove is not installed in Caramel Bogs");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 20,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		ChunkPos chunk = new ChunkPos(fixture);
		BlockPos contained = new BlockPos(
				chunk.getMinBlockX() + 4,
				fixture.getY(),
				chunk.getMinBlockZ() + 4);
		for (Rotation rotation : Rotation.values()) {
			require(helper,
					CaramelBogMangroveFeature
							.fitsWithinChunk(contained,
									rotation, chunk),
					"Caramel mangrove crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					CaramelBogMangroveFeature.buildAt(
							level, fixture, rotation),
					"Caramel mangrove refused a safe fixture");
			Map<Block, Integer> palette =
					scan(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.CARAMEL_CRUST
							.get(), 0) == 150
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GINGERBREAD_BRICKS
											.get(), 0) == 25
							&& palette.getOrDefault(
									CakeWorldBlocks.TREACLE_REED
											.get(), 0) == 14
							&& palette.getOrDefault(
									CakeWorldBlocks.WAFER_BLOCK
											.get(), 0) == 9
							&& palette.getOrDefault(
									CakeWorldFluids.CARAMEL_BLOCK
											.get(), 0) == 9
							&& level.getEntities((Entity) null,
									new AABB(fixture)
											.inflate(8.0D))
									.size() == entities,
					"Caramel mangrove lost its exact trees, reeds, route or no-entity contract: "
							+ rotation + " " + palette);
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 2, 2),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!CaramelBogMangroveFeature.hasSafeFootprint(
						level, fixture, Rotation.NONE),
				"Caramel mangrove accepted an authored solid obstacle");

		CaramelCrustBlock crust = (CaramelCrustBlock)
				CakeWorldBlocks.CARAMEL_CRUST.get();
		ArmorStand falling = new ArmorStand(level,
				fixture.getX(), fixture.getY(), fixture.getZ());
		falling.setDeltaMovement(1.0D, -1.0D, 0.5D);
		crust.entityInside(
				CakeWorldBlocks.CARAMEL_CRUST.get()
						.defaultBlockState(),
				level, fixture, falling);
		require(helper,
				close(crust.getSpeedFactor(),
						CaramelCrustBlock
								.SURFACE_SPEED_FACTOR)
						&& close(falling.getDeltaMovement().x,
								0.62D)
						&& close(falling.getDeltaMovement().y,
								-0.8D)
						&& close(falling.getDeltaMovement().z,
								0.31D)
						&& !CakeWorldBlocks.TREACLE_REED.get()
								.defaultBlockState()
								.isRandomlyTicking(),
				"Caramel Crust lost native floor drag/inside drag or Treacle Reed began random ticking");
		helper.succeed();
	}

	private static void prepare(ServerLevel level,
			BlockPos centre) {
		for (int x = -7; x <= 7; x++) {
			for (int z = -7; z <= 7; z++) {
				for (int y = -2; y <= 10; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.CARAMEL_CRUST.get()
								.defaultBlockState(), 2);
				level.setBlock(centre.offset(x, -1, z),
						CakeWorldBlocks.CHOCOLATE_SPONGE.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static Map<Block, Integer> scan(ServerLevel level,
			BlockPos centre) {
		Map<Block, Integer> counts =
				new java.util.LinkedHashMap<>();
		for (int x = -4; x <= 4; x++) {
			for (int y = -1; y <= 9; y++) {
				for (int z = -4; z <= 4; z++) {
					Block block = level.getBlockState(
							centre.offset(x, y, z))
							.getBlock();
					counts.merge(block, 1, Integer::sum);
				}
			}
		}
		return counts;
	}

	private static boolean hasPlacedFeature(Biome biome) {
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
							CaramelBogMangroveFeature.ID))
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
				"Caramel Bogs did not exactly replace "
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
				CaramelBogsGameTests.class.getResourceAsStream(
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
