package zone.moddev.mc.cakeworld.gametest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import zone.moddev.mc.orespawn.api.OreSpawnBiomes;

/**
 * Opt-in registry-only example for OreSpawn's advanced blank-biome helper.
 * Every required builder field is explicit; the empty spawn and generation
 * settings are a deliberate laboratory contract, not missing configuration.
 */
public final class BlankBiomeRegistrationFixture {
	public static final String NAMESPACE = "cakeworld_fixture";
	public static final String PATH = "blank_biome_laboratory";
	public static final ResourceLocation BIOME_ID =
			new ResourceLocation(NAMESPACE, PATH);
	public static final int FOG_COLOR = 0xF7D6FF;
	public static final int WATER_COLOR = 0xFFD166;
	public static final int WATER_FOG_COLOR = 0x9C5CFF;
	public static final int SKY_COLOR = 0xFFB6D9;
	public static final int FOLIAGE_COLOR = 0xB6F36B;
	public static final int GRASS_COLOR = 0xFF8FD8;

	private static final DeferredRegister<Biome> BIOMES =
			DeferredRegister.create(ForgeRegistries.BIOMES, NAMESPACE);
	public static final RegistryObject<Biome> BLANK_BIOME_LABORATORY =
			OreSpawnBiomes.blankAndRegister(BIOMES, PATH, builder -> builder
					.precipitation(Biome.Precipitation.RAIN)
					.biomeCategory(Biome.BiomeCategory.NONE)
					.temperature(0.65F)
					.temperatureAdjustment(Biome.TemperatureModifier.NONE)
					.downfall(0.35F)
					.specialEffects(new BiomeSpecialEffects.Builder()
							.fogColor(FOG_COLOR)
							.waterColor(WATER_COLOR)
							.waterFogColor(WATER_FOG_COLOR)
							.skyColor(SKY_COLOR)
							.foliageColorOverride(FOLIAGE_COLOR)
							.grassColorOverride(GRASS_COLOR)
							.build())
					.mobSpawnSettings(new MobSpawnSettings.Builder()
							.creatureGenerationProbability(0.0F)
							.build())
					.generationSettings(
							new BiomeGenerationSettings.Builder().build()));

	private BlankBiomeRegistrationFixture() {
	}

	public static void register(IEventBus modBus) {
		BIOMES.register(modBus);
	}
}
