package com.mcmoddev.cakeworld.init;

import java.util.Objects;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.orespawn.api.OreSpawnBiomes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldBiomes {
	private static final DeferredRegister<Biome> BIOMES =
			DeferredRegister.create(ForgeRegistries.BIOMES, CakeWorld.MODID);

	public static final RegistryObject<Biome> CANDY_PLAINS = copy(
			"candy_plains", "plains", 0.8F, 0.4F);
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

	private static RegistryObject<Biome> copy(String name, String vanilla,
			float temperature, float downfall) {
		ResourceLocation sourceId = new ResourceLocation("minecraft", vanilla);
		return OreSpawnBiomes.copyAndRegister(BIOMES, name,
				() -> Objects.requireNonNull(ForgeRegistries.BIOMES.getValue(sourceId),
						"Missing vanilla biome " + sourceId),
				builder -> builder.temperature(temperature).downfall(downfall));
	}
}
