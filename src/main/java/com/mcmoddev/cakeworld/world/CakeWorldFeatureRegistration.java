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
		});
	}
}
