package zone.moddev.mc.cakeworld.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * An ambient village windmill prop, deliberately not an automation machine.
 *
 * <p>A redstone signal changes its visible state and enables gentle client-side
 * motion particles and occasional wooden clicks. It has no block entity,
 * inventory, power output, or item/fluid transport contract.</p>
 */
public final class WaferWindmillBlock extends HorizontalDirectionalBlock {
	public static final BooleanProperty POWERED =
			BooleanProperty.create("powered");

	public WaferWindmillBlock(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(POWERED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState()
				.setValue(FACING,
						context.getHorizontalDirection().getOpposite())
				.setValue(POWERED, context.getLevel()
						.hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos,
			Block neighborBlock, BlockPos neighborPos, boolean moving) {
		if (level.isClientSide) {
			return;
		}
		boolean powered = level.hasNeighborSignal(pos);
		if (powered != state.getValue(POWERED)) {
			level.setBlock(pos, state.setValue(POWERED, powered),
					Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos,
			Random random) {
		if (!state.getValue(POWERED)) {
			return;
		}
		Direction facing = state.getValue(FACING);
		double x = pos.getX() + 0.5D + facing.getStepX() * 0.55D;
		double y = pos.getY() + 0.5D;
		double z = pos.getZ() + 0.5D + facing.getStepZ() * 0.55D;
		level.addParticle(ParticleTypes.CLOUD, x, y, z,
				facing.getStepX() * 0.02D, 0.01D,
				facing.getStepZ() * 0.02D);
		if (random.nextInt(12) == 0) {
			level.playLocalSound(x, y, z,
					SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
					SoundSource.BLOCKS, 0.25F, 1.4F, false);
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING,
				rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return rotate(state,
				mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(
			StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return false;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level,
			BlockPos pos, Direction direction) {
		return 0;
	}
}
