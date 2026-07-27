package com.mcmoddev.cakeworld.init;

import java.util.Objects;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.orespawn.api.OreSpawnBiomes;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldBiomes {
	private static final DeferredRegister<Biome> BIOMES =
			DeferredRegister.create(ForgeRegistries.BIOMES, CakeWorld.MODID);

	public static final RegistryObject<Biome> CANDY_PLAINS = copy(
			"candy_plains", "plains", 0.8F, 0.4F);
	public static final RegistryObject<Biome> GINGERBREAD_HEARTHLANDS =
			hearthlands();
	public static final RegistryObject<Biome> COOKIE_FOREST = copy(
			"cookie_forest", "forest", 0.7F, 0.8F);
	public static final RegistryObject<Biome> MARSHMALLOW_PEAKS = copy(
			"marshmallow_peaks", "jagged_peaks", -0.3F, 0.5F);
	public static final RegistryObject<Biome> SODA_OCEAN = copy(
			"soda_ocean", "ocean", 0.5F, 0.5F);
	public static final RegistryObject<Biome> FUDGE_WASTES = copy(
			"fudge_wastes", "nether_wastes", 2.0F, 0.0F);
	public static final RegistryObject<Biome> MERINGUE_ISLANDS = copy(
			"meringue_islands", "end_highlands", 0.5F, 0.0F);

	private CakeWorldBiomes() {
	}

	public static void register(IEventBus modBus) {
		BIOMES.register(modBus);
	}

	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BiomeDictionary.addTypes(key(CANDY_PLAINS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS);
			BiomeDictionary.addTypes(
					key(GINGERBREAD_HEARTHLANDS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS);
			BiomeDictionary.addTypes(key(COOKIE_FOREST),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.DENSE,
					BiomeDictionary.Type.WET);
			BiomeDictionary.addTypes(key(MARSHMALLOW_PEAKS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.MOUNTAIN,
					BiomeDictionary.Type.PEAK,
					BiomeDictionary.Type.COLD,
					BiomeDictionary.Type.SNOWY);
			BiomeDictionary.addTypes(key(SODA_OCEAN),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.OCEAN,
					BiomeDictionary.Type.WATER);
			BiomeDictionary.addTypes(key(FUDGE_WASTES),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(key(MERINGUE_ISLANDS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL);
		});
	}

	private static ResourceKey<Biome> key(RegistryObject<Biome> biome) {
		return ResourceKey.create(Registry.BIOME_REGISTRY, biome.getId());
	}

	private static RegistryObject<Biome> copy(String name, String vanilla,
			float temperature, float downfall) {
		return OreSpawnBiomes.copyAndRegister(BIOMES, name,
				() -> vanilla(vanilla),
				builder -> builder.temperature(temperature).downfall(downfall));
	}

	private static RegistryObject<Biome> hearthlands() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"gingerbread_hearthlands",
				() -> vanilla("plains"),
				builder -> builder
						.temperature(0.85F)
						.downfall(0.55F)
						.specialEffects(
								hearthlandsEffects(
										vanilla("plains")
												.getSpecialEffects())));
	}

	private static BiomeSpecialEffects hearthlandsEffects(
			BiomeSpecialEffects source) {
		BiomeSpecialEffects.Builder builder =
				new BiomeSpecialEffects.Builder()
						.fogColor(source.getFogColor())
						.waterColor(source.getWaterColor())
						.waterFogColor(
								source.getWaterFogColor())
						.skyColor(source.getSkyColor())
						.grassColorModifier(
								source.getGrassColorModifier());
		source.getFoliageColorOverride()
				.ifPresent(builder::foliageColorOverride);
		source.getGrassColorOverride()
				.ifPresent(builder::grassColorOverride);
		source.getAmbientParticleSettings()
				.ifPresent(builder::ambientParticle);
		source.getAmbientLoopSoundEvent()
				.ifPresent(builder::ambientLoopSound);
		source.getAmbientMoodSettings()
				.ifPresent(builder::ambientMoodSound);
		source.getAmbientAdditionsSettings()
				.ifPresent(builder::ambientAdditionsSound);
		source.getBackgroundMusic()
				.ifPresent(builder::backgroundMusic);
		return builder.ambientAdditionsSound(
				new AmbientAdditionsSettings(
						CakeWorldSounds.HEARTHLANDS_CHIME.get(),
						0.001D))
				.build();
	}

	private static Biome vanilla(String name) {
		ResourceLocation sourceId =
				new ResourceLocation("minecraft", name);
		return Objects.requireNonNull(
				ForgeRegistries.BIOMES.getValue(sourceId),
				"Missing vanilla biome " + sourceId);
	}
}
