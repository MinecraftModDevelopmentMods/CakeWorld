package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

/**
 * Vanilla Stronghold identities reused by the Ancient Cake Vault conversion.
 *
 * <p>A second {@code (32,3,128)} concentric-ring placement is unsafe in
 * Minecraft 1.18: record-equal placements collide in the chunk generator's
 * ring-position cache, and the winning biome search can change across JVM
 * launches. CakeWorld therefore themes the one native Stronghold start in
 * CakeWorld land biomes instead of registering a competing structure set.</p>
 */
public final class AncientCakeVaultFeature {
	public static final ResourceLocation STRUCTURE_ID =
			new ResourceLocation("minecraft", "stronghold");
	public static final ResourceLocation STRUCTURE_SET_ID =
			new ResourceLocation("minecraft", "strongholds");
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/ancient_cake_vault"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					id("ancient_cake_vault"));
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			EYE_LOCATED =
			ConfiguredStructureTags.EYE_OF_ENDER_LOCATED;

	private AncientCakeVaultFeature() {
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
