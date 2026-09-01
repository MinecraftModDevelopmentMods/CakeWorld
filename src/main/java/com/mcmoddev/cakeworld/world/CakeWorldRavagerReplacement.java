package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.GingerbreadStomper;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Ravagers emitted by vanilla raid waves inside
 * CakeWorld terrain. Loaded, outside-biome and third-party entities remain
 * untouched, and raid/passenger state transfers explicitly.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldRavagerReplacement {
	private CakeWorldRavagerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.RAVAGER
				|| !(event.getEntity()
						instanceof Ravager ravager)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!ravager.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, ravager);
					}
				}));
	}

	public static GingerbreadStomper replaceIfInCakeWorldBiome(
			ServerLevel level, Ravager ravager) {
		ResourceLocation biome = level.getBiome(
				ravager.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		GingerbreadStomper replacement =
				CakeWorldEntities.GINGERBREAD_STOMPER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = ravager.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		saved.remove("RaidId");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Raid raid = ravager.getCurrentRaid();
		int wave = ravager.getWave();
		Entity vehicle = ravager.getVehicle();
		List<Entity> passengers =
				List.copyOf(ravager.getPassengers());
		if (raid != null) {
			raid.joinRaid(wave, replacement,
					null, true);
			raid.removeFromRaid(ravager, true);
		}
		if (vehicle != null) {
			ravager.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		ravager.discard();
		return replacement;
	}
}
