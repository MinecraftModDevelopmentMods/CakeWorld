package com.mcmoddev.cakeworld.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldCreatureSpawns {
	private static final Set<String> OVERWORLD_BIOMES = Set.of(
			"candy_plains", "cookie_forest", "marshmallow_peaks", "soda_ocean");

	private CakeWorldCreatureSpawns() {
	}

	@SubscribeEvent
	public static void onBiomeLoading(BiomeLoadingEvent event) {
		ResourceLocation biome = event.getName();
		if (biome == null || !CakeWorld.MODID.equals(biome.getNamespace())
				|| !OVERWORLD_BIOMES.contains(biome.getPath())) {
			return;
		}

		replace(event, MobCategory.MONSTER, EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER, 95, 4, 4);
		if ("candy_plains".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE, EntityType.COW,
					CakeWorldEntities.COCOA_COW, 8, 2, 4);
			replace(event, MobCategory.CREATURE, EntityType.PIG,
					CakeWorldEntities.TRUFFLE_PIG, 8, 2, 4);
			replace(event, MobCategory.CREATURE, EntityType.SHEEP,
					CakeWorldEntities.CANDYFLOSS_SHEEP, 12, 2, 4);
		}
		if ("cookie_forest".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE, EntityType.CHICKEN,
					CakeWorldEntities.MALLOW_CHICK, 10, 2, 4);
		}
	}

	private static <T extends Mob> void replace(BiomeLoadingEvent event,
			MobCategory category, EntityType<?> vanilla,
			Supplier<EntityType<T>> cakeWorld, int fallbackWeight,
			int fallbackMinimum, int fallbackMaximum) {
		List<MobSpawnSettings.SpawnerData> spawns =
				event.getSpawns().getSpawner(category);
		List<MobSpawnSettings.SpawnerData> replacements = new ArrayList<>();
		for (MobSpawnSettings.SpawnerData spawn : spawns) {
			if (spawn.type == vanilla) {
				replacements.add(new MobSpawnSettings.SpawnerData(cakeWorld.get(),
						spawn.getWeight(), spawn.minCount, spawn.maxCount));
			}
		}
		spawns.removeIf(spawn -> spawn.type == vanilla);
		if (replacements.isEmpty()) {
			replacements.add(new MobSpawnSettings.SpawnerData(cakeWorld.get(),
					fallbackWeight, fallbackMinimum, fallbackMaximum));
		}
		spawns.addAll(replacements);
	}
}
