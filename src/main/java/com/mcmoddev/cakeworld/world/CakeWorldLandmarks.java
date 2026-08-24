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
		Holder<PlacedFeature> iceCreamSundaeRink =
				IceCreamSundaeRinkFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "ice_cream_tundra".equals(
						biome.getPath())
				&& iceCreamSundaeRink != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(iceCreamSundaeRink);
		}
		Holder<PlacedFeature> waffleSyrupSkywheel =
				WaffleSyrupSkywheelFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "waffle_plateaus".equals(
						biome.getPath())
				&& waffleSyrupSkywheel != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(waffleSyrupSkywheel);
		}
		Holder<PlacedFeature> cupcakeBloomCircle =
				CupcakeBloomCircleFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "cupcake_gardens".equals(
						biome.getPath())
				&& cupcakeBloomCircle != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(cupcakeBloomCircle);
		}
		Holder<PlacedFeature> liquoriceRootMaze =
				LiquoriceRootMazeFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "liquorice_darkwood".equals(
						biome.getPath())
				&& liquoriceRootMaze != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(liquoriceRootMaze);
		}
		Holder<PlacedFeature> lollipopTastingGrove =
				LollipopTastingGroveFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "lollipop_orchards".equals(
						biome.getPath())
				&& lollipopTastingGrove != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(lollipopTastingGrove);
		}
		Holder<PlacedFeature> prairiePoppingPatch =
				PrairiePoppingPatchFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "popcorn_prairie".equals(
						biome.getPath())
				&& prairiePoppingPatch != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(prairiePoppingPatch);
		}
		Holder<PlacedFeature> waferReefNursery =
				WaferReefNurseryFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "soda_ocean".equals(biome.getPath())
				&& waferReefNursery != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(waferReefNursery);
		}
		Holder<PlacedFeature> custardCoastKitchen =
				CustardCoastKitchenFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "custard_coast".equals(
						biome.getPath())
				&& custardCoastKitchen != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(custardCoastKitchen);
		}
		Holder<PlacedFeature> jellybeanCompassPicnic =
				JellybeanCompassPicnicFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "jellybean_archipelago".equals(
						biome.getPath())
				&& jellybeanCompassPicnic != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(jellybeanCompassPicnic);
		}
		Holder<PlacedFeature> rockCandyGeodeBridge =
				RockCandyGeodeBridgeFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "rock_candy_caverns".equals(
						biome.getPath())
				&& rockCandyGeodeBridge != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(rockCandyGeodeBridge);
		}
		Holder<PlacedFeature> jamLanternWalk =
				JamLanternWalkFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "jam_grottoes".equals(
						biome.getPath())
				&& jamLanternWalk != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(jamLanternWalk);
		}
		Holder<PlacedFeature> ancientNougatKitchen =
				AncientNougatKitchenFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "nougat_depths".equals(
						biome.getPath())
				&& ancientNougatKitchen != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(ancientNougatKitchen);
		}
		Holder<PlacedFeature> fudgeFondueFountain =
				FudgeFondueFountainFeature
						.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& "fudge_wastes".equals(
						biome.getPath())
				&& fudgeFondueFountain != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration
							.TOP_LAYER_MODIFICATION)
					.add(fudgeFondueFountain);
		}
		Holder<PlacedFeature> cinnamonHearthGrove =
				CinnamonHearthGroveFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "cinnamon_ember_groves".equals(biome.getPath())
				&& cinnamonHearthGrove != null) {
			event.getGeneration().getFeatures(
							GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(cinnamonHearthGrove);
		}
		Holder<PlacedFeature> smallBurntToffeeColumns =
				BurntToffeeColumnsFeature.smallPlacedFeature();
		Holder<PlacedFeature> largeBurntToffeeColumns =
				BurntToffeeColumnsFeature.largePlacedFeature();
		Holder<PlacedFeature> burntToffeeBlobs =
				BurntToffeeColumnsFeature.burntToffeeBlobsPlacedFeature();
		Holder<PlacedFeature> burntSugarBlobs =
				BurntToffeeColumnsFeature.burntSugarBlobsPlacedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "burnt_toffee_deltas".equals(biome.getPath())
				&& smallBurntToffeeColumns != null
				&& largeBurntToffeeColumns != null
				&& burntToffeeBlobs != null
				&& burntSugarBlobs != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.SURFACE_STRUCTURES)
					.add(smallBurntToffeeColumns);
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.SURFACE_STRUCTURES)
					.add(largeBurntToffeeColumns);
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.UNDERGROUND_DECORATION)
					.add(burntToffeeBlobs);
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.UNDERGROUND_DECORATION)
					.add(burntSugarBlobs);
		}
		Holder<PlacedFeature> blackLiquoriceLoop =
				BlackLiquoriceLoopFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "black_liquorice_labyrinths".equals(biome.getPath())
				&& blackLiquoriceLoop != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(blackLiquoriceLoop);
		}
		Holder<PlacedFeature> blackLiquoriceTangles =
				BlackLiquoriceTanglePatchFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "black_liquorice_labyrinths".equals(biome.getPath())
				&& blackLiquoriceTangles != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.VEGETAL_DECORATION)
					.add(blackLiquoriceTangles);
		}
		Holder<PlacedFeature> treacleSoulFlats =
				TreacleSoulFlatFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "treacle_soul_valleys".equals(biome.getPath())
				&& treacleSoulFlats != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(treacleSoulFlats);
		}
		Holder<PlacedFeature> wispLightCauseway =
				WispLightCausewayFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "treacle_soul_valleys".equals(biome.getPath())
				&& wispLightCauseway != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(wispLightCauseway);
		}
		Holder<PlacedFeature> cragfireProspect =
				CragfireProspectFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "chilli_chocolate_crags".equals(biome.getPath())
				&& cragfireProspect != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(cragfireProspect);
		}
		Holder<PlacedFeature> mallowSteamCaldera =
				MallowSteamCalderaFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "molten_marshmallow_calderas".equals(biome.getPath())
				&& mallowSteamCaldera != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(mallowSteamCaldera);
		}
		Holder<PlacedFeature> meringueStarLanding =
				MeringueStarLandingFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "meringue_islands".equals(biome.getPath())
				&& meringueStarLanding != null) {
			event.getGeneration().getFeatures(
						GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(meringueStarLanding);
		}
		Holder<PlacedFeature> cloudstepLookout =
				CloudstepLookoutFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "candyfloss_cloudbanks".equals(biome.getPath())
				&& cloudstepLookout != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(cloudstepLookout);
		}
		Holder<PlacedFeature> crumbMoonDial =
				CrumbMoonDialFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "mooncake_barrens".equals(biome.getPath())
				&& crumbMoonDial != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(crumbMoonDial);
		}
		Holder<PlacedFeature> sugarStarObservatory =
				SugarStarObservatoryFeature.placedFeature();
		if (biome != null
				&& CakeWorld.MODID.equals(biome.getNamespace())
				&& "starlight_sugar_fields".equals(biome.getPath())
				&& sugarStarObservatory != null) {
			event.getGeneration().getFeatures(
					GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
					.add(sugarStarObservatory);
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
						|| "fudge_wastes".equals(biome.getPath())
						|| "cinnamon_ember_groves"
								.equals(biome.getPath())
						|| "black_liquorice_labyrinths"
								.equals(biome.getPath()))
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
				&& isConfectionersCottageBiome(
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
				&& (isSettlementBiome(
						biome.getPath())
						|| "waffle_plateaus".equals(
								biome.getPath()))
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
				&& "mooncake_barrens".equals(
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
				|| "ice_cream_tundra".equals(path)
				|| "waffle_plateaus".equals(path)
				|| "cupcake_gardens".equals(path)
				|| "liquorice_darkwood".equals(path)
				|| "lollipop_orchards".equals(path)
				|| "popcorn_prairie".equals(path)
				|| "soda_ocean".equals(path)
				|| "custard_coast".equals(path)
				|| "jellybean_archipelago".equals(path);
	}

	private static boolean isCurrentLandBiome(
			String path) {
		return "candy_plains".equals(path)
				|| "cookie_forest".equals(path)
				|| "peppermint_pinewoods".equals(path)
				|| "gummy_jungle".equals(path)
				|| "marshmallow_peaks".equals(path)
				|| "ice_cream_tundra".equals(path)
				|| "waffle_plateaus".equals(path)
				|| "cupcake_gardens".equals(path)
				|| "liquorice_darkwood".equals(path)
				|| "lollipop_orchards".equals(path)
				|| "popcorn_prairie".equals(path)
				|| "custard_coast".equals(path);
	}

	private static boolean isSettlementBiome(String path) {
		return "candy_plains".equals(path)
				|| "gingerbread_hearthlands".equals(path);
	}

	private static boolean isConfectionersCottageBiome(
			String path) {
		return isSettlementBiome(path)
				|| "lollipop_orchards".equals(path);
	}
}
