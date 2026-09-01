package com.mcmoddev.cakeworld.block;

import java.util.Random;

import com.mcmoddev.cakeworld.init.CakeWorldItems;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A renewable hanging fruit vine for Jam Grottoes.
 *
 * <p>Picking a ripe segment leaves the plant in place. Segments may hang from
 * a solid ceiling or another segment, so generated columns remain stable
 * without tile entities, scheduled feature repair, or hidden inventories.</p>
 */
public final class JamGlowVineBlock extends Block
		implements BonemealableBlock {
	public static final IntegerProperty AGE =
			IntegerProperty.create("age", 0, 3);
	public static final int PICKED_AGE = 1;
	private static final VoxelShape SHAPE =
			box(2.0D, 0.0D, 2.0D,
					14.0D, 16.0D, 14.0D);

	public JamGlowVineBlock(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any()
				.setValue(AGE, 0));
	}

	@Override
	public BlockState getStateForPlacement(
			BlockPlaceContext context) {
		BlockState state = defaultBlockState();
		return canSurvive(state, context.getLevel(),
				context.getClickedPos()) ? state : null;
	}

	@Override
	public boolean canSurvive(BlockState state,
			LevelReader level, BlockPos position) {
		BlockPos above = position.above();
		BlockState support = level.getBlockState(above);
		return support.is(this)
				|| support.isFaceSturdy(level, above,
						Direction.DOWN);
	}

	@Override
	public BlockState updateShape(BlockState state,
			Direction direction, BlockState neighbour,
			LevelAccessor level, BlockPos pos,
			BlockPos neighbourPos) {
		if (direction == Direction.UP
				&& !canSurvive(state, level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		return super.updateShape(state, direction, neighbour,
				level, pos, neighbourPos);
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
			int berries = 2 + level.random.nextInt(2);
			popResource(level, pos, new ItemStack(
					CakeWorldItems.GLOWING_JAM_BERRY.get(),
					berries));
			level.setBlock(pos,
					state.setValue(AGE, PICKED_AGE), 2);
			level.playSound(null, pos,
					SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
					SoundSource.BLOCKS, 0.8F, 1.25F);
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(
						ParticleTypes.FALLING_SPORE_BLOSSOM,
						pos.getX() + 0.5D,
						pos.getY() + 0.5D,
						pos.getZ() + 0.5D,
						6, 0.18D, 0.3D, 0.18D,
						0.01D);
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
