package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BrittleBiscuitSteed;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Skeleton Horses created by vanilla thunder and the
 * inherited Skeleton Trap goal inside CakeWorld biomes. Loaded entities,
 * outside biomes and unknown third-party mounts remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldSkeletonHorseReplacement {
	private CakeWorldSkeletonHorseReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SKELETON_HORSE
				|| !(event.getEntity()
						instanceof SkeletonHorse horse)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!horse.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, horse);
					}
				}));
	}

	public static BrittleBiscuitSteed
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					SkeletonHorse horse) {
		ResourceLocation biome = level.getBiome(
				horse.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		BrittleBiscuitSteed replacement =
				CakeWorldEntities.BRITTLE_BISCUIT_STEED
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = horse.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				horse.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = horse.getVehicle();
		List<Entity> passengers =
				List.copyOf(horse.getPassengers());
		if (vehicle != null) {
			horse.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		horse.discard();
		return replacement;
	}
}
