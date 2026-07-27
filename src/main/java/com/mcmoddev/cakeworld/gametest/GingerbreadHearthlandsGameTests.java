package com.mcmoddev.cakeworld.gametest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.world.AncientCakeVaultFeature;
import com.mcmoddev.cakeworld.world.BurntSugarArchFeature;
import com.mcmoddev.cakeworld.world.BurntSugarArchRepairFeature;
import com.mcmoddev.cakeworld.world.CandyCaneBridgeFeature;
import com.mcmoddev.cakeworld.world.CandyCaneBridgeRepairFeature;
import com.mcmoddev.cakeworld.world.ConfectionersCottageFeature;
import com.mcmoddev.cakeworld.world.ConfectionersCottageRepairFeature;
import com.mcmoddev.cakeworld.world.GingerbreadVillageFeature;
import com.mcmoddev.cakeworld.world.WaferMineFeature;
import com.mcmoddev.cakeworld.world.WaferWindmillFeature;
import com.mcmoddev.cakeworld.world.WaferWindmillRepairFeature;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Contract proof for the first functional Gingerbread Hearthlands slice.
 *
 * <p>The initial slice deliberately reuses the existing settlement plans, but
 * it is already a distinct OreSpawn-selected biome with its own climate,
 * surface, geome bias, soundscape and complete current Overworld spawn-role
 * conversion.</p>
 */
@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class GingerbreadHearthlandsGameTests {
	private static final String EMPTY = "empty";
	private static final String PROVIDER =
			"/data/cakeworld/orespawn/provider.json";
	private static final ResourceLocation BIOME_ID =
			new ResourceLocation(CakeWorld.MODID,
					"gingerbread_hearthlands");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);
	private static final List<String> TEMPLATES = List.of(
			"cakeworld:edible_world",
			"cakeworld:edible_world_basemetals");

	private GingerbreadHearthlandsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow002")
	public static void hearthlandsHasDistinctClimateSoundAndRoles(
			GameTestHelper helper) {
		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome biome = biomes.get(BIOME_ID);
		Holder<Biome> holder = biomes.getHolder(BIOME_KEY)
				.orElseThrow(() -> new IllegalStateException(
						"Missing biome holder " + BIOME_ID));

		require(helper, biome != null,
				"Missing registered Gingerbread Hearthlands biome");
		require(helper,
				Biome.getBiomeCategory(holder)
						== Biome.BiomeCategory.PLAINS,
				"Gingerbread Hearthlands is not a plains-category biome");
		require(helper,
				close(biome.getBaseTemperature(), 0.85D)
						&& close(biome.getDownfall(), 0.55D),
				"Gingerbread Hearthlands climate does not match its contract");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.PLAINS)
						&& !BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.OCEAN)
						&& !BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.NETHER)
						&& !BiomeDictionary.hasType(BIOME_KEY,
								BiomeDictionary.Type.END),
				"Gingerbread Hearthlands biome-dictionary roles are wrong");

		AmbientAdditionsSettings additions = biome.getAmbientAdditions()
				.orElse(null);
		require(helper, additions != null
						&& additions.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.HEARTHLANDS_CHIME.getId())
						&& close(additions.getTickChance(), 0.001D),
				"Gingerbread Hearthlands does not use its gentle chime ambience");

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
		requireSpawnReplacement(helper, biome, EntityType.HORSE,
				CakeWorldEntities.GINGERBREAD_PONY.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.DONKEY,
				CakeWorldEntities.DOUGH_DONKEY.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.BEE,
				CakeWorldEntities.SUGAR_BEE.get(),
				MobCategory.CREATURE);
		requireSpawnReplacement(helper, biome, EntityType.RABBIT,
				CakeWorldEntities.GUMMY_BUNNY.get(),
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

	@GameTest(template = EMPTY, batch = "bioow002")
	public static void hearthlandsOwnsSettlementAndAdventureBoundaries(
			GameTestHelper helper) {
		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Holder<Biome> holder = biomes.getHolder(BIOME_KEY)
				.orElseThrow(() -> new IllegalStateException(
						"Missing biome holder " + BIOME_ID));

		require(helper,
				holder.is(GingerbreadVillageFeature.GENERATES_IN)
						&& holder.is(ConfectionersCottageFeature
								.GENERATES_IN)
						&& holder.is(WaferWindmillFeature
								.GENERATES_IN)
						&& holder.is(CandyCaneBridgeFeature
								.GENERATES_IN),
				"Gingerbread Hearthlands is missing a settlement structure role");
		require(helper,
				holder.is(WaferMineFeature.GENERATES_IN)
						&& holder.is(AncientCakeVaultFeature
								.GENERATES_IN)
						&& holder.is(BurntSugarArchFeature
								.GENERATES_IN),
				"Gingerbread Hearthlands is missing an adventure structure role");
		require(helper, !holder.is(BiomeTags.HAS_VILLAGE_PLAINS),
				"Gingerbread Hearthlands accidentally enables vanilla plains villages");

		require(helper,
				hasPlacedFeature(helper,
						ConfectionersCottageRepairFeature.ID)
						&& hasPlacedFeature(helper,
								WaferWindmillRepairFeature.ID)
						&& hasPlacedFeature(helper,
								CandyCaneBridgeRepairFeature.ID)
						&& hasPlacedFeature(helper,
								BurntSugarArchRepairFeature.ID),
				"Gingerbread Hearthlands is missing a landmark repair pass");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow002")
	public static void bothAdventureTemplatesShareHearthlandsProfile(
			GameTestHelper helper) {
		JsonObject provider = readObject(PROVIDER);
		require(helper,
				provider.get("provider_revision").getAsInt() == 12,
				"Gingerbread Hearthlands requires OreSpawn provider revision 12");

		JsonObject templates = provider.getAsJsonObject("templates");
		for (String templateId : TEMPLATES) {
			JsonObject profile = templates.getAsJsonObject(templateId)
					.getAsJsonObject("profile");
			JsonObject geomes = profile.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					close(geomes.get("cakeworld:cocoa_basin")
							.getAsDouble(), 5.0D)
							&& close(geomes.get(
									"cakeworld:wafer_shelf")
									.getAsDouble(), 9.0D),
					templateId
							+ " does not preserve the Hearthlands geome bias");

			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			require(helper,
					palette.get("enabled").getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 2.5D),
					templateId
							+ " does not enable the weighted Hearthlands palette");
			require(helper,
					stringSet(palette.getAsJsonArray(
							"similar_biomes")).equals(Set.of(
									"minecraft:plains",
									"minecraft:sunflower_plains",
									"minecraft:meadow"))
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.isEmpty(),
					templateId
							+ " does not preserve the Hearthlands similarity contract");
			require(helper,
					close(palette.get("min_temperature")
							.getAsDouble(), 0.4D)
							&& close(palette.get(
									"max_temperature")
									.getAsDouble(), 1.2D)
							&& close(palette.get("min_downfall")
									.getAsDouble(), 0.25D)
							&& close(palette.get("max_downfall")
									.getAsDouble(), 0.8D),
					templateId
							+ " does not preserve the Hearthlands climate selectors");

			JsonObject surface = palette.getAsJsonObject("surface");
			require(helper,
					"cakeworld:biscuit_crumbs".equals(
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
									.getAsInt() == 4,
					templateId
							+ " does not preserve the warm crumb-and-sponge surface");
		}
		helper.succeed();
	}

	private static void requireSpawnReplacement(GameTestHelper helper,
			Biome biome, EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category) {
		boolean foundVanilla = false;
		boolean foundReplacement = false;
		for (MobSpawnSettings.SpawnerData spawn
				: biome.getMobSettings().getMobs(category).unwrap()) {
			foundVanilla |= spawn.type == vanilla;
			foundReplacement |= spawn.type == replacement;
		}
		require(helper, !foundVanilla && foundReplacement,
				"Gingerbread Hearthlands did not replace "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ " with "
						+ Registry.ENTITY_TYPE.getKey(replacement));
	}

	private static boolean hasPlacedFeature(GameTestHelper helper,
			ResourceLocation featureId) {
		Biome biome = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(BIOME_ID);
		if (biome == null
				|| biome.getGenerationSettings().features().size()
						<= GenerationStep.Decoration
								.TOP_LAYER_MODIFICATION.ordinal()) {
			return false;
		}
		for (Holder<PlacedFeature> feature
				: biome.getGenerationSettings().features().get(
						GenerationStep.Decoration
								.TOP_LAYER_MODIFICATION.ordinal())) {
			if (feature.unwrapKey()
					.map(key -> key.location().equals(featureId))
					.orElse(false)) {
				return true;
			}
		}
		return false;
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
		try (InputStream stream = GingerbreadHearthlandsGameTests.class
				.getResourceAsStream(path)) {
			if (stream == null) {
				throw new IllegalStateException(
						"Missing packaged resource " + path);
			}
			try (InputStreamReader reader = new InputStreamReader(
					stream, StandardCharsets.UTF_8)) {
				JsonElement root = JsonParser.parseReader(reader);
				if (!root.isJsonObject()) {
					throw new IllegalStateException(
							"Expected a JSON object in " + path);
				}
				return root.getAsJsonObject();
			}
		} catch (IOException | RuntimeException exception) {
			throw new IllegalStateException(
					"Could not read packaged resource " + path,
					exception);
		}
	}

	private static boolean close(double actual, double expected) {
		return Math.abs(actual - expected) < 0.00001D;
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
