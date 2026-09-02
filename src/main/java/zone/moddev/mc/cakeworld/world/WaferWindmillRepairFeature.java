package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;

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
 * Restores each windmill slice after surface decoration.
 */
public final class WaferWindmillRepairFeature
		extends Feature<NoneFeatureConfiguration> {
	public static final ResourceLocation ID =
			new ResourceLocation(CakeWorld.MODID,
					"wafer_windmill_repair");
	public static final WaferWindmillRepairFeature FEATURE =
			new WaferWindmillRepairFeature();
	private static Holder<PlacedFeature> placedFeature;

	static {
		FEATURE.setRegistryName(ID);
	}

	private WaferWindmillRepairFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	public static void registerConfiguredFeature() {
		Holder<ConfiguredFeature<?, ?>> configured =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								WaferWindmillRepairFeature>(
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
		ConfiguredStructureFeature<?, ?> windmill =
				world.registryAccess()
						.registryOrThrow(Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.get(WaferWindmillFeature
								.STRUCTURE_ID);
		if (windmill == null) {
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
						windmill);
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
			BoundingBox windmillBounds =
					start.getBoundingBox();
			if (!start.isValid()
					|| !windmillBounds.intersects(
							slice.minX(), slice.minZ(),
							slice.maxX(), slice.maxZ())) {
				continue;
			}
			BlockPos centre = new BlockPos(
					windmillBounds.minX()
							+ WaferWindmillStructureFeature
									.CENTRE_OFFSET,
					windmillBounds.minY(),
					windmillBounds.minZ()
							+ WaferWindmillStructureFeature
									.CENTRE_OFFSET);
			WaferWindmillFeature.rebuildInBounds(
					world, context.random(),
					centre, slice);
			repaired = true;
		}
		return repaired;
	}
}
