package com.mcmoddev.cakeworld.compat;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VanillaResourceAdvancements {
	private static final ResourceLocation OBTAIN_ANCIENT_DEBRIS =
			new ResourceLocation("minecraft", "nether/obtain_ancient_debris");

	private VanillaResourceAdvancements() {
	}

	@SubscribeEvent
	public static void onBreak(BlockEvent.BreakEvent event) {
		if (event.getState().is(CakeWorldBlocks.ANCIENT_NOUGAT.get())
				&& event.getPlayer() instanceof ServerPlayer player
				&& player.hasCorrectToolForDrops(event.getState())) {
			creditAncientNougat(player);
		}
	}

	public static void creditAncientNougat(ServerPlayer player) {
		Advancement advancement = player.getServer().getAdvancements()
				.getAdvancement(OBTAIN_ANCIENT_DEBRIS);
		if (advancement != null) {
			player.getAdvancements().award(advancement, "ancient_debris");
		}
	}
}
