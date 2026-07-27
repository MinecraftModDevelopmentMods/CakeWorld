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

	private CakeWorldSounds() {
	}

	public static void register(IEventBus modBus) {
		SOUNDS.register(modBus);
	}
}
