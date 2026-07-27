package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla Nether Fossil identities reused by the Rock-Candy Fossil
 * conversion.
 */
public final class RockCandyFossilFeature {
	public static final ResourceLocation STRUCTURE_ID =
			new ResourceLocation("minecraft", "nether_fossil");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft", "nether_fossils");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/rock_candy_fossil"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("rock_candy_fossil"));

	private RockCandyFossilFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
