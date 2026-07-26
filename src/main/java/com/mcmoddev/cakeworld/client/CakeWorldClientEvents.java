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
import net.minecraft.client.renderer.entity.MagmaCubeRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.client.renderer.entity.OcelotRenderer;
import net.minecraft.client.renderer.entity.PandaRenderer;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.PhantomRenderer;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.client.renderer.entity.PufferfishRenderer;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraft.client.renderer.entity.RavagerRenderer;
import net.minecraft.client.renderer.entity.SalmonRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.ShulkerRenderer;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraft.client.renderer.entity.StriderRenderer;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.client.renderer.entity.TurtleRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
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
				CakeWorldEntities.SHERBET_OCELOT.get(),
				OcelotRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.CHOCOLATE_PANDA.get(),
				PandaRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.LOLLIPOP_LORIKEET.get(),
				ParrotRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.WAFER_WRAITH.get(),
				PhantomRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get(),
				CaveSpiderRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SODA_COD.get(),
				CodRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.SHERBET_SALMON.get(),
				SalmonRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.FIZZBALL_FISH.get(),
				PufferfishRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.JELLYBEAN_FISH.get(),
				TropicalFishRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.WAFER_TURTLE.get(),
				TurtleRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SODA_DOLPHIN.get(),
				DolphinRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.DOUGH_DONKEY.get(),
				DoughDonkeyRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.MARZIPAN_MULE.get(),
				MarzipanMuleRenderer::new);
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
		event.registerEntityRenderer(CakeWorldEntities.FUDGE_FOLK.get(),
				FudgeFolkRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.FUDGE_BRUTE.get(),
				FudgeBruteRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.BISCUIT_BANDIT.get(),
				PillagerRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.VANILLA_ICE_BEAR.get(),
				PolarBearRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.GUMMY_BUNNY.get(),
				RabbitRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.GINGERBREAD_STOMPER.get(),
				RavagerRenderer::new);
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
				CakeWorldEntities.SPRINKLE_LLAMA.get(),
				context -> new LlamaRenderer(
						context,
						ModelLayers.TRADER_LLAMA));
		event.registerEntityRenderer(
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				MagmaCubeRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.TAFFY_TALLWALKER.get(),
				EndermanRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.MACARON_CLAM.get(),
				ShulkerRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.CRUMB_MITE.get(),
				SilverfishRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.CANDY_CANE_ARCHER.get(),
				SkeletonRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.FROSTED_ARCHER.get(),
				StrayRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.FUDGE_SKATER.get(),
				StriderRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.BRITTLE_BISCUIT_STEED
						.get(),
				BrittleBiscuitSteedRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.JELLY_BLOB.get(),
				SlimeRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.ICE_CREAM_GOLEM.get(),
				SnowGolemRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.LIQUORICE_WEAVER.get(),
				SpiderRenderer::new);
		event.registerEntityRenderer(
				CakeWorldEntities.LIQUORICE_SQUID.get(),
				context -> new SquidRenderer(context,
						new SquidModel<>(context.bakeLayer(
								ModelLayers.SQUID))));
		event.registerEntityRenderer(CakeWorldEntities.SUGAR_MITE.get(),
				EndermiteRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SOUR_SORCERER.get(),
				EvokerRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.SOUR_SPRITE.get(),
				VexRenderer::new);
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
		event.registerEntityRenderer(CakeWorldEntities.CUPCAKE_COW.get(),
				MushroomCowRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.MALLOW_CHICK.get(),
				ChickenRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.TRUFFLE_PIG.get(), PigRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.CANDYFLOSS_SHEEP.get(),
				SheepRenderer::new);
		event.registerEntityRenderer(CakeWorldEntities.STALE_CRUMBLER.get(),
				ZombieRenderer::new);
	}

	@SubscribeEvent
	public static void addPlayerLayers(
			EntityRenderersEvent.AddLayers event) {
		for (String skin : event.getSkins()) {
			PlayerRenderer renderer = event.getSkin(skin);
			if (renderer != null) {
				renderer.addLayer(
						new LollipopLorikeetOnShoulderLayer<>(
								renderer,
								event.getEntityModels()));
			}
		}
	}
}
