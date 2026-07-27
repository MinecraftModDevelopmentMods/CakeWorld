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
		Holder<PlacedFeature> roadside =
				RoadsideCuriosityFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isCurrentLandBiome(
						biome.getPath())
				&& roadside != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(roadside);
		}
		Holder<PlacedFeature> cookieGrove =
				CookieCrumbGroveFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "cookie_forest".equals(
						biome.getPath())
				&& cookieGrove != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(cookieGrove);
		}
		Holder<PlacedFeature> peppermintClearing =
				PeppermintClearingFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "peppermint_pinewoods".equals(
						biome.getPath())
				&& peppermintClearing != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(peppermintClearing);
		}
		Holder<PlacedFeature> gummyBounceGrove =
				GummyJungleBounceGroveFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "gummy_jungle".equals(
						biome.getPath())
				&& gummyBounceGrove != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(gummyBounceGrove);
		}
		Holder<PlacedFeature> caramelMangrove =
				CaramelBogMangroveFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "caramel_bogs".equals(
						biome.getPath())
				&& caramelMangrove != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(caramelMangrove);
		}
		Holder<PlacedFeature> sherbetFossilBowl =
				SherbetFossilBowlFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "sherbet_dunes".equals(
						biome.getPath())
				&& sherbetFossilBowl != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(sherbetFossilBowl);
		}
		Holder<PlacedFeature> candyCaneHoodooGarden =
				CandyCaneHoodooGardenFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "candy_cane_badlands".equals(
						biome.getPath())
				&& candyCaneHoodooGarden != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(candyCaneHoodooGarden);
		}
		Holder<PlacedFeature> marshmallowCloudBridge =
				MarshmallowCloudBridgeFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "marshmallow_peaks".equals(
						biome.getPath())
				&& marshmallowCloudBridge != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(marshmallowCloudBridge);
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
				&& "caramel_bogs".equals(
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
				&& isSettlementBiome(
						biome.getPath())
				&& confectionersCottageRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(confectionersCottageRepair);
		}
		Holder<PlacedFeature> windmillRepair =
				WaferWindmillRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isSettlementBiome(
						biome.getPath())
				&& windmillRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(windmillRepair);
		}
		Holder<PlacedFeature> bridgeRepair =
				CandyCaneBridgeRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& isSettlementBiome(
						biome.getPath())
				&& bridgeRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(bridgeRepair);
		}
		Holder<PlacedFeature> craterKitchenRepair =
				CraterKitchenRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "meringue_islands".equals(
						biome.getPath())
				&& craterKitchenRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(craterKitchenRepair);
		}
		Holder<PlacedFeature> crystalMineRepair =
				RockCandyCrystalMineRepairFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "candy_cane_badlands".equals(
						biome.getPath())
				&& crystalMineRepair != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(crystalMineRepair);
		}
	}

	private static boolean isCurrentOverworldBiome(
			String path) {
		return "candy_plains".equals(path)
				|| "gingerbread_hearthlands".equals(path)
				|| "cookie_forest".equals(path)
				|| "peppermint_pinewoods".equals(path)
				|| "gummy_jungle".equals(path)
				|| "sherbet_dunes".equals(path)
				|| "candy_cane_badlands".equals(path)
				|| "marshmallow_peaks".equals(path)
				|| "soda_ocean".equals(path);
	}

	private static boolean isCurrentLandBiome(
			String path) {
		return "candy_plains".equals(path)
				|| "cookie_forest".equals(path)
				|| "peppermint_pinewoods".equals(path)
				|| "gummy_jungle".equals(path)
				|| "marshmallow_peaks".equals(path);
	}

	private static boolean isSettlementBiome(String path) {
		return "candy_plains".equals(path)
				|| "gingerbread_hearthlands".equals(path);
	}
}
