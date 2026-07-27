package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CakeWorldFeatureRegistration {
	private CakeWorldFeatureRegistration() {
	}

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(StarterPicnicFeature.FEATURE);
		event.getRegistry().register(GingerbreadVillageFeature.FEATURE);
		event.getRegistry().register(
				BiscuitBanditLookoutFeature.FEATURE);
		event.getRegistry().register(
				WaferMineFeature.FEATURE);
		event.getRegistry().register(
				GrandGingerbreadManorFeature.FEATURE);
		event.getRegistry().register(
				GrandGingerbreadManorRepairFeature.FEATURE);
		event.getRegistry().register(
				GummyShrineFeature.FEATURE);
		event.getRegistry().register(
				GummyShrineRepairFeature.FEATURE);
		event.getRegistry().register(
				SherbetPyramidFeature.FEATURE);
		event.getRegistry().register(
				SherbetPyramidRepairFeature.FEATURE);
		event.getRegistry().register(
				IceCreamParlourFeature.FEATURE);
		event.getRegistry().register(
				IceCreamParlourRepairFeature.FEATURE);
		event.getRegistry().register(
				BurntSugarArchFeature.FEATURE);
		event.getRegistry().register(
				BurntSugarArchRepairFeature.FEATURE);
		event.getRegistry().register(
				WaferWreckFeature.FEATURE);
		event.getRegistry().register(
				WaferWreckRepairFeature.FEATURE);
		event.getRegistry().register(
				CaramelCottageFeature.FEATURE);
		event.getRegistry().register(
				CaramelCottageRepairFeature.FEATURE);
		event.getRegistry().register(
				ConfectionersCottageFeature.FEATURE);
		event.getRegistry().register(
				ConfectionersCottageRepairFeature.FEATURE);
		event.getRegistry().register(
				WaferWindmillFeature.FEATURE);
		event.getRegistry().register(
				WaferWindmillRepairFeature.FEATURE);
		event.getRegistry().register(
				CandyCaneBridgeFeature.FEATURE);
		event.getRegistry().register(
				CandyCaneBridgeRepairFeature.FEATURE);
		event.getRegistry().register(
				CraterKitchenFeature.FEATURE);
		event.getRegistry().register(
				CraterKitchenRepairFeature.FEATURE);
		event.getRegistry().register(
				RockCandyCrystalMineFeature.FEATURE);
		event.getRegistry().register(
				RockCandyCrystalMineRepairFeature.FEATURE);
	}

	@SubscribeEvent
	public static void registerStructureFeatures(
			RegistryEvent.Register<StructureFeature<?>> event) {
		event.getRegistry().register(
				BiscuitBanditLookoutFeature
						.STRUCTURE_FEATURE);
		event.getRegistry().register(
				WaferMineFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				GrandGingerbreadManorFeature
						.STRUCTURE_FEATURE);
		event.getRegistry().register(
				GummyShrineFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				SherbetPyramidFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				IceCreamParlourFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				BurntSugarArchFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				WaferWreckFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				CaramelCottageFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				ConfectionersCottageFeature
						.STRUCTURE_FEATURE);
		event.getRegistry().register(
				WaferWindmillFeature.STRUCTURE_FEATURE);
		event.getRegistry().register(
				CandyCaneBridgeFeature
						.STRUCTURE_FEATURE);
		event.getRegistry().register(
				CraterKitchenFeature
						.STRUCTURE_FEATURE);
		event.getRegistry().register(
				RockCandyCrystalMineFeature
						.STRUCTURE_FEATURE);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			CakeWorldFeaturePoolElement.registerType();
			StarterPicnicFeature.registerConfiguredFeature();
			GingerbreadVillageFeature.registerWorldgen();
			BiscuitBanditLookoutFeature.registerWorldgen();
			WaferMineFeature.registerWorldgen();
			GrandGingerbreadManorFeature.registerWorldgen();
			GrandGingerbreadManorRepairFeature
					.registerConfiguredFeature();
			GummyShrineFeature.registerWorldgen();
			GummyShrineRepairFeature
					.registerConfiguredFeature();
			SherbetPyramidFeature.registerWorldgen();
			SherbetPyramidRepairFeature
					.registerConfiguredFeature();
			IceCreamParlourFeature.registerWorldgen();
			IceCreamParlourRepairFeature
					.registerConfiguredFeature();
			BurntSugarArchFeature.registerWorldgen();
			BurntSugarArchRepairFeature
					.registerConfiguredFeature();
			WaferWreckFeature.registerWorldgen();
			WaferWreckRepairFeature
					.registerConfiguredFeature();
			CaramelCottageFeature.registerWorldgen();
			CaramelCottageRepairFeature
					.registerConfiguredFeature();
			ConfectionersCottageFeature.registerWorldgen();
			ConfectionersCottageRepairFeature
					.registerConfiguredFeature();
			WaferWindmillFeature.registerWorldgen();
			WaferWindmillRepairFeature
					.registerConfiguredFeature();
			CandyCaneBridgeFeature.registerWorldgen();
			CandyCaneBridgeRepairFeature
					.registerConfiguredFeature();
			CraterKitchenFeature.registerWorldgen();
			CraterKitchenRepairFeature
					.registerConfiguredFeature();
			RockCandyCrystalMineFeature
					.registerWorldgen();
			RockCandyCrystalMineRepairFeature
					.registerConfiguredFeature();
		});
	}
}
