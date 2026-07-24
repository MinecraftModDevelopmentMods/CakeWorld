package com.mcmoddev.cakeworld;

import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.network.CakeWorldNetwork;

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
		CakeWorldItems.register(modBus);
		CakeWorldBiomes.register(modBus);
		modBus.addListener(CakeWorldBiomes::commonSetup);
		CakeWorldNetwork.register();
	}
}
