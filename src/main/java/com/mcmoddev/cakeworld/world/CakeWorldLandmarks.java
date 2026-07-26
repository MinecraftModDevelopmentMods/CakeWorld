package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldLandmarks {
	private CakeWorldLandmarks() {
	}

	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		Holder<PlacedFeature> picnic = StarterPicnicFeature.placedFeature();
		if (biome != null && CakeWorld.MODID.equals(biome.getNamespace())
				&& "candy_plains".equals(biome.getPath()) && picnic != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.SURFACE_STRUCTURES).add(picnic);
		}
		Holder<PlacedFeature> manorRepair =
				GrandGingerbreadManorRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isCurrentOverworldBiome(
						biome.getPath())
				&& manorRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(manorRepair);
		}
		Holder<PlacedFeature> shrineRepair =
				GummyShrineRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isCurrentOverworldBiome(
						biome.getPath())
				&& shrineRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(shrineRepair);
		}
	}

	private static boolean isCurrentOverworldBiome(
			String path) {
		return "candy_plains".equals(path)
				|| "cookie_forest".equals(path)
				|| "marshmallow_peaks".equals(path)
				|| "soda_ocean".equals(path);
	}
}
