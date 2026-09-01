package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla End City identities reused by the Macaron Citadel conversion.
 */
public final class MacaronCitadelFeature {
	public static final ResourceLocation STRUCTURE_ID =
			new ResourceLocation("minecraft", "end_city");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft", "end_cities");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/macaron_citadel"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("macaron_citadel"));

	private MacaronCitadelFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
