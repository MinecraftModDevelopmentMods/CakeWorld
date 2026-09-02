package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla Ocean Monument identities reused by the Soda Palace conversion.
 *
 * <p>The native structure remains authoritative for placement, its internal
 * room graph, Guardian spawn overrides, elder encounters, sponge rooms, gold
 * core and save format. CakeWorld only themes starts centred in Soda Ocean.</p>
 */
public final class SodaPalaceFeature {
	public static final ResourceLocation STRUCTURE_ID =
			new ResourceLocation("minecraft", "monument");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft", "ocean_monuments");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/soda_palace"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("soda_palace"));

	private SodaPalaceFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
