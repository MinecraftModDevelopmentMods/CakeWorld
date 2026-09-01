package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.JawbreakerGuardian;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Iron Golems created by villagers, player block
 * patterns or commands in CakeWorld terrain. Loaded and outside entities are
 * deliberately left untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldIronGolemReplacement {
	private CakeWorldIronGolemReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.IRON_GOLEM
				|| !(event.getEntity()
						instanceof IronGolem ironGolem)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		// Deferred replacement lets CarvedPumpkinBlock fire the vanilla
		// summoned_entity criterion against its literal Iron Golem first.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!ironGolem.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, ironGolem);
					}
				}));
	}

	public static JawbreakerGuardian
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					IronGolem ironGolem) {
		ResourceLocation biome = level.getBiome(
				ironGolem.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		JawbreakerGuardian replacement =
				CakeWorldEntities.JAWBREAKER_GUARDIAN
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = ironGolem.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = ironGolem.getVehicle();
		Entity leashHolder = ironGolem.getLeashHolder();
		if (vehicle != null) {
			ironGolem.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			ironGolem.dropLeash(true, false);
			replacement.setLeashedTo(leashHolder, true);
		}
		ironGolem.discard();
		return replacement;
	}
}
