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

	private CakeWorldSounds() {
	}

	public static void register(IEventBus modBus) {
		SOUNDS.register(modBus);
	}
}
