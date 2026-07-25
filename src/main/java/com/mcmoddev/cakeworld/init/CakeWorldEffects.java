package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.effect.SugarRushEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldEffects {
	private static final DeferredRegister<MobEffect> EFFECTS =
			DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
					CakeWorld.MODID);

	public static final RegistryObject<MobEffect> SUGAR_RUSH =
			EFFECTS.register("sugar_rush", SugarRushEffect::new);

	private CakeWorldEffects() {
	}

	public static void register(IEventBus modBus) {
		EFFECTS.register(modBus);
	}
}
