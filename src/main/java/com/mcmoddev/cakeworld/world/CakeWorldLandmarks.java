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
		Holder<PlacedFeature> pyramidRepair =
				SherbetPyramidRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isCurrentOverworldBiome(
						biome.getPath())
				&& pyramidRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(pyramidRepair);
		}
		Holder<PlacedFeature> parlourRepair =
				IceCreamParlourRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isCurrentOverworldBiome(
						biome.getPath())
				&& parlourRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(parlourRepair);
		}
		Holder<PlacedFeature> archRepair =
				BurntSugarArchRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& (isCurrentOverworldBiome(
						biome.getPath())
						|| "fudge_wastes"
								.equals(biome
										.getPath()))
				&& archRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(archRepair);
		}
		Holder<PlacedFeature> wreckRepair =
				WaferWreckRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "soda_ocean".equals(
						biome.getPath())
				&& wreckRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(wreckRepair);
		}
		Holder<PlacedFeature> cottageRepair =
				CaramelCottageRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "cookie_forest".equals(
						biome.getPath())
				&& cottageRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(cottageRepair);
		}
		Holder<PlacedFeature> confectionersCottageRepair =
				ConfectionersCottageRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "candy_plains".equals(
						biome.getPath())
				&& confectionersCottageRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(confectionersCottageRepair);
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
