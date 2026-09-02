package zone.moddev.mc.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class BiscuitCrumbsBlock extends FallingBlock {
	private static final int CRUMB_DUST_COLOUR = 0xB88752;

	public BiscuitCrumbsBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
		return CRUMB_DUST_COLOUR;
	}
}
