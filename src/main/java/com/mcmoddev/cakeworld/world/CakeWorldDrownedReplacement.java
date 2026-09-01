package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.SoggyBiscuit;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Covers literal vanilla Drowned created by ocean-ruin markers and submerged
 * Zombie conversion. Loaded entities are deliberately left untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldDrownedReplacement {
	private CakeWorldDrownedReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType() != EntityType.DROWNED
				|| !(event.getEntity() instanceof Drowned drowned)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		// The event can fire before its chunk is FULL. Delay biome inspection
		// and substitution to avoid worldgen chunk-loading deadlocks.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1, () -> {
					if (drowned.isRemoved()) {
						return;
					}
					SoggyBiscuit replacement =
							convertIfInCakeWorldBiome(level, drowned);
					if (replacement != null) {
						drowned.discard();
						level.addFreshEntity(replacement);
					}
				}));
	}

	public static SoggyBiscuit convertIfInCakeWorldBiome(
			ServerLevel level, Drowned drowned) {
		ResourceLocation biome = level.getBiome(drowned.blockPosition())
				.unwrapKey().map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return null;
		}

		SoggyBiscuit replacement = CakeWorldEntities.SOGGY_BISCUIT.get()
				.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = drowned.saveWithoutId(new CompoundTag());
		// The old entity may remain in the section manager until the end of
		// the tick, so the replacement needs its own UUID.
		saved.remove("UUID");
		replacement.load(saved);
		return replacement;
	}
}
