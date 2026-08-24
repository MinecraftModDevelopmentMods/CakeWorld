package com.mcmoddev.cakeworld.gametest;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import zone.moddev.mc.orespawn.api.OreDimensionSelector;
import zone.moddev.mc.orespawn.api.OrePattern;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Dormant Forge-IMC provider used only by the explicit OS-006 runtime. */
public final class CakeWorldImcProviderFixture {
	public static final ResourceLocation RULE_ID = new ResourceLocation(
			"cakeworld", "ore/imc_probe");
	public static final ResourceLocation OUTPUT_ID = new ResourceLocation(
			"cakeworld", "sprinkle_cluster");
	public static final ResourceLocation SELECTOR_RULE_ID =
			new ResourceLocation("cakeworld", "ore/imc_selector_probe");
	public static final int REVISION = 6001;
	private static WorldgenProvider provider;
	private static boolean submitted;

	private CakeWorldImcProviderFixture() {
	}

	public static void register(IEventBus modBus) {
		modBus.addListener(CakeWorldImcProviderFixture::enqueue);
	}

	private static void enqueue(InterModEnqueueEvent event) {
		provider = WorldgenProvider.builder("cakeworld", REVISION)
				.ore(RULE_ID, OUTPUT_ID, ore -> ore
						.enabled(false)
						.retrogen(false)
						.dimension(Level.OVERWORLD.location(), placement ->
								placement.enabled(false)
										.attempts(0.0D)
										.quantity(1)))
				.ore(SELECTOR_RULE_ID, OUTPUT_ID, ore -> ore
						.retrogen(false)
						.dimensionSelector(
								OreDimensionSelector.ALL_EXCEPT_NETHER_AND_END,
								placement -> placement.yRange(0, 47)
										.attempts(64.0D)
										.quantity(32)
										.pattern(OrePattern.CLUSTERS)
										.hostBlock(new ResourceLocation(
												"minecraft", "stone")))
						.dimension(Level.OVERWORLD.location(), placement ->
								placement.enabled(false)
										.attempts(0.0D)
										.quantity(1)))
				.build();
		submitted = OreSpawnApi.enqueue(provider);
	}

	public static WorldgenProvider provider() {
		if (provider == null) {
			throw new IllegalStateException("IMC provider was not built");
		}
		return provider;
	}

	public static boolean submitted() {
		return submitted;
	}
}
