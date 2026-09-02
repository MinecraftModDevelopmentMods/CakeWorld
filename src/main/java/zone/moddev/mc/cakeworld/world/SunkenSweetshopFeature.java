package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla Ocean Ruin identities reused by the Sunken Sweetshop conversion.
 *
 * <p>The native warm/cold configured structures and their shared placement
 * remain authoritative for template selection, integrity, clustering, loot,
 * Drowned markers, dolphin guidance and save format. CakeWorld only themes
 * starts centred in Soda Ocean.</p>
 */
public final class SunkenSweetshopFeature {
	public static final ResourceLocation COLD_STRUCTURE_ID =
			new ResourceLocation("minecraft", "ocean_ruin_cold");
	public static final ResourceLocation WARM_STRUCTURE_ID =
			new ResourceLocation("minecraft", "ocean_ruin_warm");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft", "ocean_ruins");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/sunken_sweetshop"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("sunken_sweetshop"));

	private SunkenSweetshopFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
