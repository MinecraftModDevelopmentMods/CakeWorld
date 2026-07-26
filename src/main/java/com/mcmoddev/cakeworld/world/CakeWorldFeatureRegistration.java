package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.world.level.levelgen.feature.Feature;
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
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			StarterPicnicFeature.registerConfiguredFeature();
			GingerbreadVillageFeature.registerWorldgen();
		});
	}
}
