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
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;

/**
 * Restores Wafer Wreck slices after Soda-Ocean terrain decoration.
 */
public final class WaferWreckRepairFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			new ResourceLocation(CakeWorld.MODID,
					"wafer_wreck_repair");
	public static final WaferWreckRepairFeature FEATURE =
			new WaferWreckRepairFeature();
	private static Holder<PlacedFeature> placedFeature;

	static {
		FEATURE.setRegistryName(ID);
	}

	private WaferWreckRepairFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	public static void registerConfiguredFeature() {
		Holder<ConfiguredFeature<?, ?>> configured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								WaferWreckRepairFeature>(
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
		ConfiguredStructureFeature<?, ?> wreck =
				world.registryAccess()
						.registryOrThrow(Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.get(WaferWreckFeature
								.STRUCTURE_ID);
		if (wreck == null) {
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
						wreck);
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
			BoundingBox wreckBounds =
					start.getBoundingBox();
			if (!start.isValid()
					|| !wreckBounds.intersects(
							slice.minX(), slice.minZ(),
							slice.maxX(), slice.maxZ())) {
				continue;
			}
			BlockPos centre = new BlockPos(
					wreckBounds.minX() + 16,
					wreckBounds.minY()
							+ WaferWreckStructureFeature
									.FLOOR_OFFSET,
					wreckBounds.minZ() + 16);
			WaferWreckFeature.rebuildInBounds(
					world, context.random(),
					centre, slice);
			repaired = true;
		}
		return repaired;
	}
}
