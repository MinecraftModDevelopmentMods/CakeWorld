package com.mcmoddev.cakeworld.gametest;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zone.moddev.mc.orespawn.api.OreDimensionSelector;
import zone.moddev.mc.orespawn.api.OrePattern;
import zone.moddev.mc.orespawn.api.OrePatternType;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.OreSpawnPatternRegistry;
import zone.moddev.mc.orespawn.api.WorldgenProvider;

/** Dormant Forge-IMC provider used only by the explicit OS-006 runtime. */
public final class CakeWorldImcProviderFixture {
	public static final ResourceLocation RULE_ID = new ResourceLocation(
			"cakeworld", "ore/imc_probe");
	public static final ResourceLocation OUTPUT_ID = new ResourceLocation(
			"cakeworld", "sprinkle_cluster");
	public static final ResourceLocation SELECTOR_RULE_ID =
			new ResourceLocation("cakeworld", "ore/imc_selector_probe");
	public static final ResourceLocation WEIGHTED_HOST_RULE_ID =
			new ResourceLocation("cakeworld", "ore/imc_weighted_host_probe");
	public static final ResourceLocation WEIGHTED_HOST_OUTPUT_ID =
			new ResourceLocation("cakeworld", "fizzy_pearl");
	public static final ResourceLocation POINT_PATTERN_ID =
			new ResourceLocation("cakeworld", "imc_point_probe");
	public static final int REVISION = 6001;
	private static final DeferredRegister<OrePatternType> PATTERNS =
			DeferredRegister.create(OreSpawnPatternRegistry.REGISTRY_NAME,
					"cakeworld");
	private static final RegistryObject<OrePatternType> POINT_PATTERN =
			PATTERNS.register(POINT_PATTERN_ID.getPath(), () ->
					OrePatternType.create(Codec.PASSTHROUGH, ignored ->
							context -> context.tryPlace(context.originX(),
									context.originY(), context.originZ())));
	private static WorldgenProvider provider;
	private static boolean submitted;

	private CakeWorldImcProviderFixture() {
	}

	public static void register(IEventBus modBus) {
		PATTERNS.register(modBus);
		modBus.addListener(CakeWorldImcProviderFixture::enqueue);
	}

	private static void enqueue(InterModEnqueueEvent event) {
		JsonObject pointSettings = new JsonObject();
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
								placement -> placement.yRange(32, 47)
										.attempts(64.0D)
										.quantity(32)
										.pattern(OrePattern.CLUSTERS)
										.hostBlock(new ResourceLocation(
												"minecraft", "stone")))
						.dimension(Level.OVERWORLD.location(), placement ->
								placement.enabled(false)
										.attempts(0.0D)
										.quantity(1)))
				.ore(WEIGHTED_HOST_RULE_ID, WEIGHTED_HOST_OUTPUT_ID,
						ore -> ore.retrogen(false)
								.dimensionSelector(
										OreDimensionSelector.ALL_EXCEPT_NETHER_AND_END,
										placement -> placement.yRange(-32, 31)
												.attempts(64.0D)
												.quantity(1)
												.pattern(POINT_PATTERN_ID,
														pointSettings)
												.hostBlock(new ResourceLocation(
														"minecraft", "stone"), 0.25D)
												.hostTag(new ResourceLocation(
														"minecraft",
														"deepslate_ore_replaceables"),
														0.75D))
								.dimension(Level.OVERWORLD.location(),
										placement -> placement.enabled(false)
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

	public static boolean pointPatternRegistered() {
		return POINT_PATTERN.isPresent();
	}
}
