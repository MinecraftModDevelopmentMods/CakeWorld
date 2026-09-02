package zone.moddev.mc.cakeworld.world;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

/**
 * Vanilla Igloo placement contract for CakeWorld's Ice-Cream Parlour.
 */
public final class IceCreamParlourStructureFeature
		extends JigsawFeature {
	public static final int BURIED_DEPTH = 19;

	public IceCreamParlourStructureFeature() {
		super(JigsawConfiguration.CODEC, -BURIED_DEPTH,
				true, true,
				context -> context.validBiomeOnTop(
						Heightmap.Types.WORLD_SURFACE_WG));
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}
}
