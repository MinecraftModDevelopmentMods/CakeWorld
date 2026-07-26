package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.SprinkleLlama;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Trader Llamas inside CakeWorld biomes. The deferred
 * conversion lets the wandering-trader spawner attach both caravan leads
 * before their complete state is transferred. Loaded and outside-CakeWorld
 * animals remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldTraderLlamaReplacement {
	private CakeWorldTraderLlamaReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.TRADER_LLAMA
				|| !(event.getEntity()
						instanceof TraderLlama traderLlama)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!traderLlama.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, traderLlama);
					}
				}));
	}

	public static SprinkleLlama
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					TraderLlama traderLlama) {
		if (traderLlama.getType()
				!= EntityType.TRADER_LLAMA) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				traderLlama.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		SprinkleLlama replacement =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = traderLlama.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				traderLlama.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = traderLlama.getVehicle();
		Entity leashHolder =
				traderLlama.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(
						traderLlama.getPassengers());
		if (vehicle != null) {
			traderLlama.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			traderLlama.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		traderLlama.discard();
		return replacement;
	}
}
