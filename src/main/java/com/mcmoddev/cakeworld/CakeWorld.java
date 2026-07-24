package com.mcmoddev.cakeworld;

import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;

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
		CakeWorldBiomes.register(modBus);
	}
}
