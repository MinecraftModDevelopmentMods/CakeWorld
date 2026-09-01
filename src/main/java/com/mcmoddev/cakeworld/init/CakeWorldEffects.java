package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.effect.CocoaComfortEffect;
import com.mcmoddev.cakeworld.effect.FizzyFeetEffect;
import com.mcmoddev.cakeworld.effect.MintyFreshEffect;
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
	public static final RegistryObject<MobEffect> COCOA_COMFORT =
			EFFECTS.register("cocoa_comfort", CocoaComfortEffect::new);
	public static final RegistryObject<MobEffect> MINTY_FRESH =
			EFFECTS.register("minty_fresh", MintyFreshEffect::new);
	public static final RegistryObject<MobEffect> FIZZY_FEET =
			EFFECTS.register("fizzy_feet", FizzyFeetEffect::new);

	private CakeWorldEffects() {
	}

	public static void register(IEventBus modBus) {
		EFFECTS.register(modBus);
	}
}
