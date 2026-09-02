package zone.moddev.mc.cakeworld;

import zone.moddev.mc.cakeworld.command.CakeWorldCommands;
import zone.moddev.mc.cakeworld.gametest.BlankBiomeRegistrationFixture;
import zone.moddev.mc.cakeworld.gametest.CakeWorldImcProviderFixture;
import zone.moddev.mc.cakeworld.gametest.ClimateBoundaryFixture;
import zone.moddev.mc.cakeworld.gametest.SamplerThirdPartyBiomeFixture;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEffects;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldOrePatterns;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.network.CakeWorldNetwork;

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
		if (Boolean.getBoolean("cakeworld.blankBiomeFixture")) {
			BlankBiomeRegistrationFixture.register(modBus);
		}
		if (Boolean.getBoolean("cakeworld.climateBoundaryFixture")) {
			ClimateBoundaryFixture.register(modBus);
		}
		if (Boolean.getBoolean("cakeworld.imcProviderFixture")) {
			CakeWorldImcProviderFixture.register(modBus);
		}
		modBus.addListener(CakeWorldBiomes::commonSetup);
		CakeWorldNetwork.register();
		MinecraftForge.EVENT_BUS.addListener(CakeWorldCommands::register);
	}
}
