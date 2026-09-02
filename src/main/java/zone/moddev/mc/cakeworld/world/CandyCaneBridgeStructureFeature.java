package zone.moddev.mc.cakeworld.world;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

/**
 * Broad surface sampling for a route that may rotate through any cardinal
 * direction.
 */
public final class CandyCaneBridgeStructureFeature
		extends StructureFeature<JigsawConfiguration> {
	public static final int CENTRE_OFFSET = 16;

	public CandyCaneBridgeStructureFeature() {
		super(JigsawConfiguration.CODEC,
				CandyCaneBridgeStructureFeature::createPieces);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	private static Optional<PieceGenerator<JigsawConfiguration>>
			createPieces(
					PieceGeneratorSupplier.Context<
							JigsawConfiguration> context) {
		if (!context.validBiomeOnTop(
				Heightmap.Types.WORLD_SURFACE_WG)) {
			return Optional.empty();
		}
		int centreX = context.chunkPos().getMinBlockX()
				+ CENTRE_OFFSET;
		int centreZ = context.chunkPos().getMinBlockZ()
				+ CENTRE_OFFSET;
		int totalHeight = 0;
		int samples = 0;
		for (int x : new int[] {-12, 0, 12}) {
			for (int z : new int[] {-12, 0, 12}) {
				if (x != 0 || z != 0) {
					totalHeight += context.chunkGenerator()
							.getBaseHeight(
									centreX + x,
									centreZ + z,
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									context.heightAccessor());
					samples++;
				}
			}
		}
		int floorY = totalHeight / samples;
		Pools.bootstrap();
		BlockPos start = new BlockPos(
				context.chunkPos().getMinBlockX(),
				floorY,
				context.chunkPos().getMinBlockZ());
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				start, true, false);
	}
}
