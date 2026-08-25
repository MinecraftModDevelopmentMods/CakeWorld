package com.mcmoddev.cakeworld;

import com.mcmoddev.cakeworld.command.CakeWorldCommands;
import com.mcmoddev.cakeworld.gametest.CakeWorldImcProviderFixture;
import com.mcmoddev.cakeworld.gametest.SamplerThirdPartyBiomeFixture;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldOrePatterns;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.network.CakeWorldNetwork;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CakeWorld.MODID)
public final class CakeWorld {
	public static final String MODID = "cakeworld";

	public CakeWorld() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		CakeWorldBlocks.register(modBus);
		CakeWorldFluids.register(modBus);
		CakeWorldEntities.register(modBus);
		CakeWorldEffects.register(modBus);
		CakeWorldSounds.register(modBus);
		CakeWorldItems.register(modBus);
		CakeWorldBiomes.register(modBus);
		CakeWorldOrePatterns.register(modBus);
		if (Boolean.getBoolean("cakeworld.samplerThirdPartyFixture")
				|| Boolean.getBoolean("cakeworld.replaceModeFixture")) {
			SamplerThirdPartyBiomeFixture.register(modBus);
		}
		if (Boolean.getBoolean("cakeworld.imcProviderFixture")) {
			CakeWorldImcProviderFixture.register(modBus);
		}
		modBus.addListener(CakeWorldBiomes::commonSetup);
		CakeWorldNetwork.register();
		MinecraftForge.EVENT_BUS.addListener(CakeWorldCommands::register);
	}
}
