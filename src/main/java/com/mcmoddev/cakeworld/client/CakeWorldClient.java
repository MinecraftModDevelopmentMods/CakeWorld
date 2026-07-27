package com.mcmoddev.cakeworld.client;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = CakeWorld.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CakeWorldClient {
	private CakeWorldClient() {
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(CakeWorldBlocks.GUMMY_BLOCK.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(
					CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(
					CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(
					CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(
					CakeWorldBlocks.GUMMY_VINE.get(),
					RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(
					CakeWorldBlocks.TREACLE_REED.get(),
					RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(CakeWorldBlocks.CANDY_GLASS.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(CakeWorldFluids.LEMONADE.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(CakeWorldFluids.FLOWING_LEMONADE.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(CakeWorldFluids.HOT_FUDGE.get(),
					RenderType.translucent());
			ItemBlockRenderTypes.setRenderLayer(CakeWorldFluids.FLOWING_HOT_FUDGE.get(),
					RenderType.translucent());
		});
	}
}
