package com.mcmoddev.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Airy End terrain that combines Marshmallow's safe rebound with visible,
 * deliberately low-value nibble stages. Standing on it also grants a short
 * Slow Falling rescue window for a forgiving first step into the End.
 */
public final class MeringueFoamBlock extends MarshmallowBlock {
	public static final int MAX_BITES = 3;
	public static final int NIBBLE_NUTRITION = 1;
	public static final float NIBBLE_SATURATION = 0.1F;
	public static final int SLOW_FALLING_TICKS = 80;
	public static final IntegerProperty BITES = IntegerProperty.create(
			"bites", 0, MAX_BITES);

	private static final VoxelShape[] SHAPE_BY_BITE = {
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(12.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
	};

	public MeringueFoamBlock(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(BITES, 0));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level,
			BlockPos pos, CollisionContext context) {
		return SHAPE_BY_BITE[state.getValue(BITES)];
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state,
			Entity entity) {
		if (!level.isClientSide && entity instanceof LivingEntity living) {
			living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
					SLOW_FALLING_TICKS, 0, true, false, true));
		}
		super.stepOn(level, pos, state, entity);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (!player.getItemInHand(hand).isEmpty() || !player.canEat(false)) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		player.getFoodData().eat(NIBBLE_NUTRITION, NIBBLE_SATURATION);
		player.awardStat(Stats.EAT_CAKE_SLICE);
		level.playSound(null, pos, SoundEvents.GENERIC_EAT,
				SoundSource.BLOCKS, 0.65F,
				1.1F + level.random.nextFloat() * 0.2F);
		level.levelEvent(2001, pos, Block.getId(state));
		level.gameEvent(player, GameEvent.EAT, pos);

		int bites = state.getValue(BITES);
		if (bites < MAX_BITES) {
			level.setBlock(pos, state.setValue(BITES, bites + 1),
					Block.UPDATE_ALL);
		} else {
			level.removeBlock(pos, false);
			level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
		}
		return InteractionResult.CONSUME;
	}

	@Override
	protected void createBlockStateDefinition(
			StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}
}
