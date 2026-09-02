package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla Buried Treasure identities reused by the Buried Sweet Tin
 * conversion.
 */
public final class BuriedSweetTinFeature {
	public static final ResourceLocation STRUCTURE_ID =
			new ResourceLocation("minecraft",
					"buried_treasure");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft",
					"buried_treasures");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/buried_sweet_tin"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("buried_sweet_tin"));

	private BuriedSweetTinFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
