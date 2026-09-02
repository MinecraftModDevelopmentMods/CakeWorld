package zone.moddev.mc.cakeworld.block;

import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A deliberately stable decorative reed for Caramel Bogs. World generation
 * controls its height, so it performs no random-tick growth.
 */
public final class TreacleReedBlock extends BushBlock {
	private static final VoxelShape SHAPE =
			box(3.0D, 0.0D, 3.0D,
					13.0D, 16.0D, 13.0D);

	public TreacleReedBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public boolean canSurvive(BlockState state,
			LevelReader level, BlockPos position) {
		BlockState below = level.getBlockState(position.below());
		return below.is(this)
				|| below.is(CakeWorldBlocks.CARAMEL_CRUST.get());
	}

	@Override
	public VoxelShape getShape(BlockState state,
			BlockGetter level, BlockPos position,
			CollisionContext context) {
		return SHAPE;
	}
}
