package com.mcmoddev.cakeworld.client;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.AxolotlRenderer;
import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.CaveSpiderRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,
		value = Dist.CLIENT)
public final class CakeWorldClientEvents {
	private CakeWorldClientEvents() {
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(CakeWorldEntities.JELLYLOTL.get(),
				AxolotlRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.BONBON_BAT.get(),
				BatRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SUGAR_BEE.get(),
				BeeRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.CUSTARD_CAT.get(),
				CatRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get(),
				CaveSpiderRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.CINNAMON_SPARK.get(),
				BlazeRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.CINNAMON_PUFF.get(),
				ThrownItemRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.COCOA_COW.get(), CowRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.MALLOW_CHICK.get(),
				ChickenRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.TRUFFLE_PIG.get(), PigRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.CANDYFLOSS_SHEEP.get(),
				SheepRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.STALE_CRUMBLER.get(),
				ZombieRenderer::new);
	}
}
