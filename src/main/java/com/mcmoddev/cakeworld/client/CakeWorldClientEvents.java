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
import net.minecraft.client.renderer.entity.CodRenderer;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.DolphinRenderer;
import net.minecraft.client.renderer.entity.DrownedRenderer;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EndermiteRenderer;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.entity.GiantMobRenderer;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.client.renderer.entity.GoatRenderer;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.HoglinRenderer;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.entity.HuskRenderer;
import net.minecraft.client.renderer.entity.IllusionerRenderer;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
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
		event.registerEntityRenderer(CakeWorldEntities.SODA_COD.get(),
				CodRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SODA_DOLPHIN.get(),
				DolphinRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.DOUGH_DONKEY.get(),
				DoughDonkeyRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SOGGY_BISCUIT.get(),
				DrownedRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SOGGY_TRIDENT.get(),
				ThrownTridentRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get(),
				ElderGuardianRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.GUMBALL_GUARDIAN.get(),
				GuardianRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.FUDGE_BOAR.get(),
				HoglinRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.GINGERBREAD_PONY.get(),
				HorseRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.DRIED_CRUMBLER.get(),
				HuskRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.MIRAGE_CONFECTIONER.get(),
				IllusionerRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.MIRAGE_SWEET.get(),
				ThrownItemRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.JAWBREAKER_GUARDIAN.get(),
				IronGolemRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.MERINGUE_LLAMA.get(),
				context -> new LlamaRenderer(
						context, ModelLayers.LLAMA));
		event.registerEntityRenderer(
				CakeWorldEntities.TAFFY_TALLWALKER.get(),
				EndermanRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SUGAR_MITE.get(),
				EndermiteRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SOUR_SORCERER.get(),
				EvokerRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.PEPPERMINT_FOX.get(),
				FoxRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.MALLOW_FLOATER.get(),
				GhastRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.MALLOW_PUFF.get(),
				context -> new ThrownItemRenderer<>(
						context, 3.0F, true));
		event.registerEntityRenderer(
				CakeWorldEntities.GIANT_STALE_CRUMBLER.get(),
				context -> new GiantMobRenderer(context, 6.0F));
		event.registerEntityRenderer(CakeWorldEntities.GLOW_JELLY.get(),
				context -> new GlowSquidRenderer(context,
						new SquidModel<>(context.bakeLayer(
								ModelLayers.GLOW_SQUID))));
		event.registerEntityRenderer(CakeWorldEntities.NOUGAT_GOAT.get(),
				GoatRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.POP_ROCK_POPPER.get(),
				CreeperRenderer::new);
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
