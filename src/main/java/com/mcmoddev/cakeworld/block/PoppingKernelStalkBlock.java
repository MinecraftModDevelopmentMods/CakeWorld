package com.mcmoddev.cakeworld.block;

import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A renewable prairie crop whose mature seed pods pop when picked.
 *
 * <p>The plant remains rooted at half growth after harvest. Its tiny upward
 * nudge is deliberately paired with cleared fall distance, so the joke cannot
 * turn a normal harvest into disguised damage.</p>
 */
public final class PoppingKernelStalkBlock extends CropBlock {
	public static final int PICKED_AGE = 3;

	public PoppingKernelStalkBlock(
			BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return CakeWorldItems.POPCORN_KERNEL.get();
	}

	@Override
	protected boolean mayPlaceOn(BlockState state,
			BlockGetter level, BlockPos position) {
		return supportsKernelStalk(state)
				|| super.mayPlaceOn(state, level, position);
	}

	public static boolean supportsKernelStalk(BlockState state) {
		return state.is(CakeWorldBlocks.POPPED_CORN_TURF.get())
				|| state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.CANDIED_SOIL.get());
	}

	@Override
	public InteractionResult use(BlockState state, Level level,
			BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (!isMaxAge(state)) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			int kernels = 3 + level.random.nextInt(3);
			popResource(level, pos, new ItemStack(
					CakeWorldItems.POPCORN_KERNEL.get(),
					kernels));
			level.setBlock(pos, getStateForAge(PICKED_AGE), 2);
			level.playSound(null, pos,
					SoundEvents.FIREWORK_ROCKET_BLAST,
					SoundSource.BLOCKS, 0.7F,
					1.35F + level.random.nextFloat() * 0.2F);
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.POOF,
						pos.getX() + 0.5D,
						pos.getY() + 0.7D,
						pos.getZ() + 0.5D,
						8, 0.25D, 0.35D, 0.25D,
						0.03D);
			}
			player.push(0.0D, 0.12D, 0.0D);
			player.fallDistance = 0.0F;
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
