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
 * Soda-Ocean placement contract for CakeWorld's first Wafer Wreck.
 */
public final class WaferWreckStructureFeature
		extends StructureFeature<JigsawConfiguration> {
	public static final int FLOOR_OFFSET = 4;

	public WaferWreckStructureFeature() {
		super(JigsawConfiguration.CODEC,
				WaferWreckStructureFeature::createPieces);
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
				Heightmap.Types.OCEAN_FLOOR_WG)) {
			return Optional.empty();
		}
		int middleX = context.chunkPos()
				.getMiddleBlockX();
		int middleZ = context.chunkPos()
				.getMiddleBlockZ();
		int floorY = context.chunkGenerator()
				.getFirstFreeHeight(
						middleX, middleZ,
						Heightmap.Types
								.OCEAN_FLOOR_WG,
						context.heightAccessor());
		Pools.bootstrap();
		BlockPos start = new BlockPos(
				context.chunkPos().getMinBlockX(),
				floorY - FLOOR_OFFSET,
				context.chunkPos().getMinBlockZ());
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				start, true, false);
	}
}
