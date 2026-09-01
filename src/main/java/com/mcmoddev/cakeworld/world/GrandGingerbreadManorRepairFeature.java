package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;

/**
 * Replays each Grand Manor chunk slice after biome vegetation.
 *
 * <p>Woodland decoration can run after surface structures and overwrite the
 * manor's ground storey. This final-step feature consults saved structure
 * references, does nothing outside a real Manor, and reuses the same bounded
 * plan so every write remains in the chunk currently being decorated.</p>
 */
public final class GrandGingerbreadManorRepairFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			new ResourceLocation(CakeWorld.MODID,
					"grand_gingerbread_manor_repair");
	public static final GrandGingerbreadManorRepairFeature FEATURE =
			new GrandGingerbreadManorRepairFeature();
	private static Holder<PlacedFeature> placedFeature;

	static {
		FEATURE.setRegistryName(ID);
	}

	private GrandGingerbreadManorRepairFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	public static void registerConfiguredFeature() {
		Holder<ConfiguredFeature<?, ?>> configured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								GrandGingerbreadManorRepairFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				ID,
				new PlacedFeature(configured, List.of()));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		ConfiguredStructureFeature<?, ?> manor =
				world.registryAccess()
						.registryOrThrow(Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.get(GrandGingerbreadManorFeature
								.STRUCTURE_ID);
		if (manor == null) {
			return false;
		}
		StructureFeatureManager manager =
				world.getLevel().structureFeatureManager();
		if (world instanceof WorldGenRegion region) {
			manager = manager.forWorldGenRegion(region);
		}
		List<StructureStart> starts =
				manager.startsForFeature(
						SectionPos.of(context.origin()),
						manor);
		if (starts.isEmpty()) {
			return false;
		}

		ChunkPos chunk = new ChunkPos(context.origin());
		BoundingBox slice = new BoundingBox(
				chunk.getMinBlockX(),
				world.getMinBuildHeight(),
				chunk.getMinBlockZ(),
				chunk.getMaxBlockX(),
				world.getMaxBuildHeight() - 1,
				chunk.getMaxBlockZ());
		boolean repaired = false;
		for (StructureStart start : starts) {
			if (!start.isValid()
					|| !intersectsHorizontally(
							start.getBoundingBox(),
							slice)) {
				continue;
			}
			BoundingBox manorBounds =
					start.getBoundingBox();
			BlockPos centre = new BlockPos(
					manorBounds.minX() + 24,
					manorBounds.minY(),
					manorBounds.minZ() + 24);
			GrandGingerbreadManorFeature
					.rebuildInBounds(
							world, context.random(),
							centre, slice);
			repaired = true;
		}
		return repaired;
	}

	private static boolean intersectsHorizontally(
			BoundingBox first, BoundingBox second) {
		return first.maxX() >= second.minX()
				&& first.minX() <= second.maxX()
				&& first.maxZ() >= second.minZ()
				&& first.minZ() <= second.maxZ();
	}
}
