package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BitterBaker;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Witches from natural spawning, raids and swamp-hut
 * markers inside CakeWorld terrain. Loaded, outside-world and third-party
 * Witch subclasses remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldWitchReplacement {
	private CakeWorldWitchReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.WITCH
				|| !(event.getEntity()
						instanceof Witch witch)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!witch.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, witch);
					}
				}));
	}

	public static BitterBaker replaceIfInCakeWorldBiome(
			ServerLevel level, Witch witch) {
		if (witch.getType() != EntityType.WITCH) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				witch.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		BitterBaker replacement =
				CakeWorldEntities.BITTER_BAKER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = witch.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		saved.remove("RaidId");
		replacement.load(saved);
		replacement.invulnerableTime =
				witch.invulnerableTime;
		if (witch.isDrinkingPotion()) {
			replacement.resumeConvertedDrink();
		}
		LivingEntity target = witch.getTarget();
		LivingEntity lastHurtBy =
				witch.getLastHurtByMob();
		Raid raid = witch.getCurrentRaid();
		int wave = witch.getWave();
		Entity vehicle = witch.getVehicle();
		List<Entity> passengers =
				List.copyOf(witch.getPassengers());
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? witch.tickCount
						: Math.max(1,
								witch.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (raid != null) {
			raid.joinRaid(wave, replacement,
					null, true);
			raid.removeFromRaid(witch, true);
		}
		if (vehicle != null) {
			witch.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		witch.discard();
		return replacement;
	}
}
