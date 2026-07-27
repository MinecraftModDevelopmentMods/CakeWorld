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

	private CakeWorldSounds() {
	}

	public static void register(IEventBus modBus) {
		SOUNDS.register(modBus);
	}
}
