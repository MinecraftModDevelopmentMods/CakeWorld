package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla Bastion Remnant identities reused by the Burnt-Toffee Foundry
 * conversion.
 */
public final class BurntToffeeFoundryFeature {
	public static final ResourceLocation STRUCTURE_ID =
			new ResourceLocation("minecraft", "bastion_remnant");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft", "nether_complexes");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/burnt_toffee_foundry"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("burnt_toffee_foundry"));

	private BurntToffeeFoundryFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
