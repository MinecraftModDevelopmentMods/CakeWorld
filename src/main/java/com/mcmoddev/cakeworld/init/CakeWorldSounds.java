package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldSounds {
	private static final DeferredRegister<SoundEvent> SOUNDS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
					CakeWorld.MODID);

	public static final RegistryObject<SoundEvent> COOKBOOK_DISCOVERY =
			SOUNDS.register("cookbook_discovery",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID, "cookbook_discovery")));
	public static final RegistryObject<SoundEvent> HEARTHLANDS_CHIME =
			SOUNDS.register("hearthlands_chime",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID, "hearthlands_chime")));
	public static final RegistryObject<SoundEvent> COOKIE_FOREST_RUSTLE =
			SOUNDS.register("cookie_forest_rustle",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID, "cookie_forest_rustle")));
	public static final RegistryObject<SoundEvent> PEPPERMINT_PINEWOODS_CHIME =
			SOUNDS.register("peppermint_pinewoods_chime",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"peppermint_pinewoods_chime")));
	public static final RegistryObject<SoundEvent> GUMMY_JUNGLE_WOBBLE =
			SOUNDS.register("gummy_jungle_wobble",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"gummy_jungle_wobble")));
	public static final RegistryObject<SoundEvent> CARAMEL_BOGS_BUBBLE =
			SOUNDS.register("caramel_bogs_bubble",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"caramel_bogs_bubble")));
	public static final RegistryObject<SoundEvent> SHERBET_DUNES_FIZZ =
			SOUNDS.register("sherbet_dunes_fizz",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"sherbet_dunes_fizz")));
	public static final RegistryObject<SoundEvent> CANDY_CANE_BADLANDS_CHIME =
			SOUNDS.register("candy_cane_badlands_chime",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"candy_cane_badlands_chime")));
	public static final RegistryObject<SoundEvent> MARSHMALLOW_PEAKS_BREEZE =
			SOUNDS.register("marshmallow_peaks_breeze",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"marshmallow_peaks_breeze")));
	public static final RegistryObject<SoundEvent> ICE_CREAM_TUNDRA_CHILL =
			SOUNDS.register("ice_cream_tundra_chill",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"ice_cream_tundra_chill")));
	public static final RegistryObject<SoundEvent> WAFFLE_PLATEAUS_BREEZE =
			SOUNDS.register("waffle_plateaus_breeze",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"waffle_plateaus_breeze")));
	public static final RegistryObject<SoundEvent> CUPCAKE_GARDENS_HUM =
			SOUNDS.register("cupcake_gardens_hum",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"cupcake_gardens_hum")));
	public static final RegistryObject<SoundEvent> LIQUORICE_DARKWOOD_RUSTLE =
			SOUNDS.register("liquorice_darkwood_rustle",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"liquorice_darkwood_rustle")));
	public static final RegistryObject<SoundEvent> LOLLIPOP_ORCHARDS_CHIME =
			SOUNDS.register("lollipop_orchards_chime",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"lollipop_orchards_chime")));
	public static final RegistryObject<SoundEvent> POPCORN_PRAIRIE_RUSTLE =
			SOUNDS.register("popcorn_prairie_rustle",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"popcorn_prairie_rustle")));
	public static final RegistryObject<SoundEvent> SODA_OCEAN_FIZZ =
			SOUNDS.register("soda_ocean_fizz",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"soda_ocean_fizz")));
	public static final RegistryObject<SoundEvent> CUSTARD_COAST_LAP =
			SOUNDS.register("custard_coast_lap",
					() -> new SoundEvent(new ResourceLocation(
							CakeWorld.MODID,
							"custard_coast_lap")));
	public static final RegistryObject<SoundEvent>
			JELLYBEAN_ARCHIPELAGO_CHIMES =
					SOUNDS.register(
							"jellybean_archipelago_chimes",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"jellybean_archipelago_chimes")));
	public static final RegistryObject<SoundEvent>
			ROCK_CANDY_CAVERNS_CHIME =
					SOUNDS.register(
							"rock_candy_caverns_chime",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"rock_candy_caverns_chime")));
	public static final RegistryObject<SoundEvent>
			JAM_GROTTOES_DRIP =
					SOUNDS.register(
							"jam_grottoes_drip",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"jam_grottoes_drip")));
	public static final RegistryObject<SoundEvent>
			NOUGAT_DEPTHS_SETTLE =
					SOUNDS.register(
							"nougat_depths_settle",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"nougat_depths_settle")));
	public static final RegistryObject<SoundEvent>
			BURNT_TOFFEE_DELTAS_SNAP =
					SOUNDS.register(
							"burnt_toffee_deltas_snap",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"burnt_toffee_deltas_snap")));
	public static final RegistryObject<SoundEvent>
			FUDGE_WASTES_BUBBLE =
					SOUNDS.register(
							"fudge_wastes_bubble",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"fudge_wastes_bubble")));
	public static final RegistryObject<SoundEvent>
			CINNAMON_EMBER_GROVES_CRACKLE =
					SOUNDS.register(
							"cinnamon_ember_groves_crackle",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"cinnamon_ember_groves_crackle")));
	public static final RegistryObject<SoundEvent>
			BLACK_LIQUORICE_LABYRINTHS_CREAK =
					SOUNDS.register(
							"black_liquorice_labyrinths_creak",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"black_liquorice_labyrinths_creak")));
	public static final RegistryObject<SoundEvent>
			TREACLE_SOUL_VALLEYS_MURMUR =
					SOUNDS.register(
							"treacle_soul_valleys_murmur",
							() -> new SoundEvent(
									new ResourceLocation(
											CakeWorld.MODID,
											"treacle_soul_valleys_murmur")));

	private CakeWorldSounds() {
	}

	public static void register(IEventBus modBus) {
		SOUNDS.register(modBus);
	}
}
