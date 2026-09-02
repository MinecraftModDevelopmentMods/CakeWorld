package zone.moddev.mc.cakeworld.block;

import java.util.Random;

import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Renewable Soda Ocean foliage which preserves the Lemonade source occupying
 * its block. Mature fronds can be picked without draining the sea or uprooting
 * the plant.
 */
public final class FizzyKelpBlock extends BushBlock
		implements BonemealableBlock, LiquidBlockContainer {
	public static final IntegerProperty AGE =
			IntegerProperty.create("age", 0, 3);
	public static final int PICKED_AGE = 1;
	private static final VoxelShape SHAPE =
			box(2.0D, 0.0D, 2.0D,
					14.0D, 16.0D, 14.0D);

	public FizzyKelpBlock(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any()
				.setValue(AGE, 0));
	}

	@Override
	protected boolean mayPlaceOn(BlockState state,
			BlockGetter level, BlockPos position) {
		return supportsFizzyKelp(state)
				|| state.is(this);
	}

	public static boolean supportsFizzyKelp(BlockState state) {
		return state.is(CakeWorldBlocks.BISCUIT_CRUMBS.get())
				|| state.is(CakeWorldBlocks.BISCUIT_STONE.get())
				|| state.is(CakeWorldBlocks.WAFER_BLOCK.get())
				|| state.is(CakeWorldBlocks.WAFER_ROCK.get());
	}

	@Override
	public BlockState getStateForPlacement(
			BlockPlaceContext context) {
		FluidState fluid = context.getLevel().getFluidState(
				context.getClickedPos());
		return fluid.is(CakeWorldFluids.LEMONADE.get())
				? defaultBlockState() : null;
	}

	@Override
	public boolean canSurvive(BlockState state,
			LevelReader level, BlockPos position) {
		return mayPlaceOn(level.getBlockState(position.below()),
				level, position.below())
				&& level.getFluidState(position)
						.is(CakeWorldFluids.LEMONADE.get());
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < 3;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level,
			BlockPos pos, Random random) {
		int age = state.getValue(AGE);
		if (age < 3 && random.nextInt(5) == 0) {
			level.setBlock(pos,
					state.setValue(AGE, age + 1), 2);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level,
			BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (state.getValue(AGE) < 3) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			int fronds = 2 + level.random.nextInt(3);
			popResource(level, pos, new ItemStack(
					CakeWorldItems.FIZZY_KELP_FROND.get(),
					fronds));
			level.setBlock(pos,
					state.setValue(AGE, PICKED_AGE), 2);
			level.playSound(null, pos,
					SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,
					SoundSource.BLOCKS, 0.8F,
					1.15F + level.random.nextFloat() * 0.2F);
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(
						ParticleTypes.BUBBLE_COLUMN_UP,
						pos.getX() + 0.5D,
						pos.getY() + 0.6D,
						pos.getZ() + 0.5D,
						10, 0.2D, 0.35D, 0.2D,
						0.02D);
			}
		}
		return InteractionResult.sidedSuccess(
				level.isClientSide);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level,
			BlockPos pos, BlockState state, boolean isClient) {
		return state.getValue(AGE) < 3;
	}

	@Override
	public boolean isBonemealSuccess(Level level,
			Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level,
			Random random, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(AGE,
				Math.min(3, state.getValue(AGE) + 1)), 2);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return CakeWorldFluids.LEMONADE.get()
				.getSource(false);
	}

	@Override
	public BlockState updateShape(BlockState state,
			Direction direction, BlockState neighbour,
			LevelAccessor level, BlockPos pos,
			BlockPos neighbourPos) {
		level.scheduleTick(pos,
				CakeWorldFluids.LEMONADE.get(),
				CakeWorldFluids.LEMONADE.get()
						.getTickDelay(level));
		return super.updateShape(state, direction, neighbour,
				level, pos, neighbourPos);
	}

	@Override
	public boolean canPlaceLiquid(BlockGetter level,
			BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor level,
			BlockPos pos, BlockState state,
			FluidState fluidState) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state,
			BlockGetter level, BlockPos position,
			CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(
			StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
