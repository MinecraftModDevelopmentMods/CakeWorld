package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.GiantStaleCrumbler;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Giant;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Giants summoned in CakeWorld terrain while preserving
 * loaded entities and third-party behavior outside CakeWorld biomes.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldGiantReplacement {
	private CakeWorldGiantReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType() != EntityType.GIANT
				|| !(event.getEntity() instanceof Giant giant)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1, () -> {
					if (giant.isRemoved()) {
						return;
					}
					GiantStaleCrumbler replacement =
							convertIfInCakeWorldBiome(level, giant);
					if (replacement != null) {
						giant.discard();
						level.addFreshEntity(replacement);
					}
				}));
	}

	public static GiantStaleCrumbler convertIfInCakeWorldBiome(
			ServerLevel level, Giant giant) {
		ResourceLocation biome = level.getBiome(
				giant.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return null;
		}
		GiantStaleCrumbler replacement =
				CakeWorldEntities.GIANT_STALE_CRUMBLER.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = giant.saveWithoutId(new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		return replacement;
	}
}
