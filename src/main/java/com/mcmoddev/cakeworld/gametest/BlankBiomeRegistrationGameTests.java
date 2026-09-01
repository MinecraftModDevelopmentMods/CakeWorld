package com.mcmoddev.cakeworld.gametest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Complete registry and non-placement proof for blankAndRegister. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_blank_biome")
public final class BlankBiomeRegistrationGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation ADVENTURE_TEMPLATE =
			new ResourceLocation("cakeworld", "edible_world");
	private static final List<String> EXPECTED_INTEGRATION_FEATURES = List.of(
			"2:orespawn:stone_replacer",
			"2:orespawn:biome_surfaces");

	private BlankBiomeRegistrationGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void blankBiomeIsCompleteButNotPlaced(
			GameTestHelper helper) {
		require(helper, Boolean.getBoolean("cakeworld.blankBiomeFixture"),
				"Blank-biome fixture was not explicitly enabled");
		Registry<Biome> registry = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY,
				BlankBiomeRegistrationFixture.BIOME_ID);
		Holder<Biome> holder = registry.getHolder(key).orElseThrow();
		Biome biome = holder.value();

		require(helper, biome.getPrecipitation() == Biome.Precipitation.RAIN,
				"Blank biome precipitation drifted");
		require(helper, Biome.getBiomeCategory(holder)
				== Biome.BiomeCategory.NONE,
				"Blank biome category drifted");
		require(helper, Float.compare(biome.getBaseTemperature(), 0.65F) == 0
				&& Float.compare(biome.getDownfall(), 0.35F) == 0,
				"Blank biome climate drifted");

		BiomeSpecialEffects effects = biome.getSpecialEffects();
		require(helper,
				effects.getFogColor()
						== BlankBiomeRegistrationFixture.FOG_COLOR
				&& effects.getWaterColor()
						== BlankBiomeRegistrationFixture.WATER_COLOR
				&& effects.getWaterFogColor()
						== BlankBiomeRegistrationFixture.WATER_FOG_COLOR
				&& effects.getSkyColor()
						== BlankBiomeRegistrationFixture.SKY_COLOR
				&& effects.getFoliageColorOverride().orElse(-1)
						== BlankBiomeRegistrationFixture.FOLIAGE_COLOR
				&& effects.getGrassColorOverride().orElse(-1)
						== BlankBiomeRegistrationFixture.GRASS_COLOR,
				"Blank biome effects drifted");

		MobSpawnSettings spawns = biome.getMobSettings();
		require(helper, Float.compare(spawns.getCreatureProbability(), 0.0F)
				== 0 && spawns.getEntityTypes().isEmpty(),
				"Blank biome must retain deliberate empty spawn settings");
		for (MobCategory category : MobCategory.values()) {
			require(helper, spawns.getMobs(category).isEmpty(),
					"Blank biome gained a " + category + " spawn");
		}

		BiomeGenerationSettings generation = biome.getGenerationSettings();
		require(helper, generation.getCarvingStages().stream().allMatch(
				stage -> !generation.getCarvers(stage).iterator().hasNext()),
				"Blank biome gained a configured carver");
		List<String> actualFeatures = describeFeatures(generation);
		require(helper, actualFeatures.equals(EXPECTED_INTEGRATION_FEATURES),
				"Blank biome integration features drifted: " + actualFeatures);
		require(helper, generation.getFlowerFeatures().isEmpty(),
				"Blank biome gained a flower feature");

		GeologyProfileView active = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, active.selectedTemplate()
				.filter(ADVENTURE_TEMPLATE::equals).isPresent(),
				"Blank-biome proof did not select the ordinary adventure");
		require(helper, !active.toJson().toString().contains(
				BlankBiomeRegistrationFixture.BIOME_ID.toString()),
				"Registry-only blank biome leaked into the active profile");

		ChunkGenerator generator = helper.getLevel().getChunkSource()
				.getGenerator();
		require(helper, !generator.getBiomeSource().possibleBiomes()
				.contains(holder),
				"Registry-only blank biome leaked into possible worldgen output");
		helper.succeed();
	}

	private static List<String> describeFeatures(
			BiomeGenerationSettings generation) {
		List<String> result = new ArrayList<>();
		for (int step = 0; step < generation.features().size(); step++) {
			for (Holder<PlacedFeature> feature : generation.features().get(step)) {
				result.add(step + ":" + feature.unwrapKey()
						.map(key -> key.location().toString())
						.orElse("<direct>"));
			}
		}
		return result;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
