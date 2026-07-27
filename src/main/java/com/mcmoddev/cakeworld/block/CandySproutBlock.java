package com.mcmoddev.cakeworld.block;

import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
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
 * A gentle crop that can be harvested in place rather than destroyed and
 * replanted. Picking a mature sprout leaves a partly grown plant behind.
 */
public final class CandySproutBlock extends CropBlock {
	public static final int PICKED_AGE = 2;

	public CandySproutBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected ItemLike getBaseSeedId() {
		return CakeWorldItems.SPRINKLE_SEEDS.get();
	}

	@Override
	protected boolean mayPlaceOn(BlockState state,
			BlockGetter level, BlockPos position) {
		return supportsGardenPlant(state)
				|| super.mayPlaceOn(state, level, position);
	}

	public static boolean supportsGardenPlant(BlockState state) {
		return state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())
				|| state.is(CakeWorldBlocks.ICING.get())
				|| state.is(CakeWorldBlocks.GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get())
				|| state.is(CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (!isMaxAge(state)) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			int sweets = 2 + level.random.nextInt(2);
			popResource(level, pos,
					new ItemStack(CakeWorldItems.BOILED_SWEET.get(), sweets));
			level.setBlock(pos, getStateForAge(PICKED_AGE), 2);
			level.playSound(null, pos,
					SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
					SoundSource.BLOCKS, 1.0F, 1.15F);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
