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
 * Restores Crater-Kitchen slices after End surface decoration.
 */
public final class CraterKitchenRepairFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			new ResourceLocation(CakeWorld.MODID,
					"crater_kitchen_repair");
	public static final CraterKitchenRepairFeature FEATURE =
			new CraterKitchenRepairFeature();
	private static Holder<PlacedFeature> placedFeature;

	static {
		FEATURE.setRegistryName(ID);
	}

	private CraterKitchenRepairFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	public static void registerConfiguredFeature() {
		Holder<ConfiguredFeature<?, ?>> configured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								CraterKitchenRepairFeature>(
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
		ConfiguredStructureFeature<?, ?> kitchen =
				world.registryAccess()
						.registryOrThrow(Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.get(CraterKitchenFeature
								.STRUCTURE_ID);
		if (kitchen == null) {
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
						kitchen);
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
			BoundingBox kitchenBounds =
					start.getBoundingBox();
			if (!start.isValid()
					|| !kitchenBounds.intersects(
							slice.minX(), slice.minZ(),
							slice.maxX(), slice.maxZ())) {
				continue;
			}
			BlockPos centre = new BlockPos(
					kitchenBounds.minX()
							+ CraterKitchenStructureFeature
									.CENTRE_OFFSET,
					kitchenBounds.minY(),
					kitchenBounds.minZ()
							+ CraterKitchenStructureFeature
									.CENTRE_OFFSET);
			CraterKitchenFeature.rebuildInBounds(
					world, context.random(),
					centre, slice);
			repaired = true;
		}
		return repaired;
	}
}
