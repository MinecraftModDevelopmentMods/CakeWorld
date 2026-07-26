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
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return;
		}

		replace(event, MobCategory.MONSTER, EntityType.ENDERMAN,
				CakeWorldEntities.TAFFY_TALLWALKER, 10, 1, 4);
		replaceExisting(event, MobCategory.CREATURE,
				EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP);
		replaceExisting(event, MobCategory.CREATURE,
				EntityType.WOLF,
				CakeWorldEntities.GINGER_SNAP_HOUND);
		replaceExisting(event, MobCategory.MONSTER,
				EntityType.ZOGLIN,
				CakeWorldEntities.STALE_FUDGE_BOAR);
		replaceExisting(event, MobCategory.MONSTER,
				EntityType.SKELETON,
				CakeWorldEntities.CANDY_CANE_ARCHER);
		replaceExisting(event, MobCategory.MONSTER,
				EntityType.STRAY,
				CakeWorldEntities.FROSTED_ARCHER);
		replaceExisting(event, MobCategory.CREATURE,
				EntityType.STRIDER,
				CakeWorldEntities.FUDGE_SKATER);
		replaceExisting(event, MobCategory.MONSTER,
				EntityType.SLIME,
				CakeWorldEntities.JELLY_BLOB);
		replaceExisting(event, MobCategory.MONSTER,
				EntityType.SPIDER,
				CakeWorldEntities.LIQUORICE_WEAVER);
		replaceExisting(event, MobCategory.MONSTER,
				EntityType.WITCH,
				CakeWorldEntities.BITTER_BAKER);
		replaceExisting(event, MobCategory.WATER_CREATURE,
				EntityType.SQUID,
				CakeWorldEntities.LIQUORICE_SQUID);

		if (OVERWORLD_BIOMES.contains(biome.getPath())) {
			replace(event, MobCategory.MONSTER, EntityType.ZOMBIE,
					CakeWorldEntities.STALE_CRUMBLER, 95, 4, 4);
			replace(event, MobCategory.AMBIENT, EntityType.BAT,
					CakeWorldEntities.BONBON_BAT, 10, 4, 8);
			replace(event, MobCategory.MONSTER, EntityType.CREEPER,
					CakeWorldEntities.POP_ROCK_POPPER, 100, 4, 4);
		}
		if ("candy_plains".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE, EntityType.BEE,
					CakeWorldEntities.SUGAR_BEE, 8, 1, 3);
			replace(event, MobCategory.CREATURE, EntityType.COW,
					CakeWorldEntities.COCOA_COW, 8, 2, 4);
			replace(event, MobCategory.CREATURE, EntityType.PIG,
					CakeWorldEntities.TRUFFLE_PIG, 8, 2, 4);
			replace(event, MobCategory.CREATURE, EntityType.DONKEY,
					CakeWorldEntities.DOUGH_DONKEY, 1, 1, 3);
			replace(event, MobCategory.CREATURE, EntityType.HORSE,
					CakeWorldEntities.GINGERBREAD_PONY, 5, 2, 6);
			replace(event, MobCategory.CREATURE, EntityType.RABBIT,
					CakeWorldEntities.GUMMY_BUNNY, 2, 2, 6);
		}
		if ("chocolate_sponge_meadows".equals(
				biome.getPath())) {
			replace(event, MobCategory.CREATURE, EntityType.RABBIT,
					CakeWorldEntities.GUMMY_BUNNY, 2, 2, 6);
			replace(event, MobCategory.CREATURE,
					EntityType.SHEEP,
					CakeWorldEntities.CANDYFLOSS_SHEEP,
					12, 4, 4);
		}
		if ("cupcake_gardens".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE,
					EntityType.MOOSHROOM,
					CakeWorldEntities.CUPCAKE_COW,
					8, 4, 8);
		}
		if ("gummy_jungle".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE,
					EntityType.RABBIT,
					CakeWorldEntities.GUMMY_BUNNY,
					4, 2, 3);
			replace(event, MobCategory.CREATURE,
					EntityType.PARROT,
					CakeWorldEntities.LOLLIPOP_LORIKEET,
					40, 1, 2);
			// Ocelots are CREATURE entities, but 1.18.2 deliberately
			// places Jungle Ocelots in the MONSTER spawn-cap list.
			replace(event, MobCategory.MONSTER,
					EntityType.OCELOT,
					CakeWorldEntities.SHERBET_OCELOT,
					2, 1, 3);
			replace(event, MobCategory.CREATURE,
					EntityType.PANDA,
					CakeWorldEntities.CHOCOLATE_PANDA,
					80, 1, 2);
		}
		if ("lollipop_orchards".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE,
					EntityType.PARROT,
					CakeWorldEntities.LOLLIPOP_LORIKEET,
					40, 1, 2);
		}
		if ("sherbet_dunes".equals(biome.getPath())) {
			replace(event, MobCategory.MONSTER,
					EntityType.OCELOT,
					CakeWorldEntities.SHERBET_OCELOT,
					1, 1, 1);
			replace(event, MobCategory.MONSTER, EntityType.HUSK,
					CakeWorldEntities.DRIED_CRUMBLER, 80, 4, 4);
		}
		if ("cookie_forest".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE, EntityType.BEE,
					CakeWorldEntities.SUGAR_BEE, 6, 1, 2);
			replace(event, MobCategory.CREATURE, EntityType.CHICKEN,
					CakeWorldEntities.MALLOW_CHICK, 10, 2, 4);
		}
		if ("marshmallow_peaks".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE, EntityType.GOAT,
					CakeWorldEntities.NOUGAT_GOAT, 5, 1, 3);
		}
		if ("ice_cream_tundra".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE,
					EntityType.POLAR_BEAR,
					CakeWorldEntities.VANILLA_ICE_BEAR,
					1, 1, 2);
			replace(event, MobCategory.WATER_AMBIENT,
					EntityType.SALMON,
					CakeWorldEntities.SHERBET_SALMON,
					15, 1, 5);
		}
		if ("custard_coast".equals(biome.getPath())) {
			replace(event, MobCategory.CREATURE,
					EntityType.TURTLE,
					CakeWorldEntities.WAFER_TURTLE,
					5, 2, 5);
		}
		if ("candyfloss_cloudbanks".equals(
				biome.getPath())) {
			replace(event, MobCategory.CREATURE,
					EntityType.LLAMA,
					CakeWorldEntities.MERINGUE_LLAMA,
					5, 4, 6);
		}
		if ("soda_ocean".equals(biome.getPath())) {
			replace(event, MobCategory.MONSTER, EntityType.DROWNED,
					CakeWorldEntities.SOGGY_BISCUIT, 5, 1, 1);
			replace(event, MobCategory.AXOLOTLS, EntityType.AXOLOTL,
					CakeWorldEntities.JELLYLOTL, 6, 1, 3);
			replace(event, MobCategory.WATER_AMBIENT, EntityType.COD,
					CakeWorldEntities.SODA_COD, 15, 3, 6);
			replace(event, MobCategory.WATER_AMBIENT,
					EntityType.SALMON,
					CakeWorldEntities.SHERBET_SALMON,
					15, 1, 5);
			replace(event, MobCategory.WATER_AMBIENT,
					EntityType.PUFFERFISH,
					CakeWorldEntities.FIZZBALL_FISH, 5, 1, 3);
			replace(event, MobCategory.WATER_AMBIENT,
					EntityType.TROPICAL_FISH,
					CakeWorldEntities.JELLYBEAN_FISH,
					25, 8, 8);
			replace(event, MobCategory.WATER_CREATURE,
					EntityType.DOLPHIN,
					CakeWorldEntities.SODA_DOLPHIN, 1, 1, 2);
			replace(event, MobCategory.UNDERGROUND_WATER_CREATURE,
					EntityType.GLOW_SQUID,
					CakeWorldEntities.GLOW_JELLY, 10, 4, 6);
		}
		if ("fudge_wastes".equals(biome.getPath())) {
			replace(event, MobCategory.MONSTER, EntityType.PIGLIN,
					CakeWorldEntities.FUDGE_FOLK, 15, 4, 4);
			replace(event, MobCategory.MONSTER,
					EntityType.MAGMA_CUBE,
					CakeWorldEntities.HOT_FUDGE_BLOB,
					2, 4, 4);
			replace(event, MobCategory.MONSTER, EntityType.BLAZE,
					CakeWorldEntities.CINNAMON_SPARK, 10, 1, 3);
			replace(event, MobCategory.MONSTER, EntityType.GHAST,
					CakeWorldEntities.MALLOW_FLOATER, 50, 4, 4);
			replace(event, MobCategory.MONSTER, EntityType.HOGLIN,
					CakeWorldEntities.FUDGE_BOAR, 9, 3, 4);
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

	private static <T extends Mob> void replaceExisting(
			BiomeLoadingEvent event, MobCategory category,
			EntityType<?> vanilla,
			Supplier<EntityType<T>> cakeWorld) {
		List<MobSpawnSettings.SpawnerData> spawns =
				event.getSpawns().getSpawner(category);
		List<MobSpawnSettings.SpawnerData> replacements =
				new ArrayList<>();
		for (MobSpawnSettings.SpawnerData spawn : spawns) {
			if (spawn.type == vanilla) {
				replacements.add(
						new MobSpawnSettings.SpawnerData(
								cakeWorld.get(),
								spawn.getWeight(),
								spawn.minCount,
								spawn.maxCount));
			}
		}
		spawns.removeIf(spawn -> spawn.type == vanilla);
		spawns.addAll(replacements);
	}
}
