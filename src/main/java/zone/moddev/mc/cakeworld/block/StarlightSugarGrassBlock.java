package zone.moddev.mc.cakeworld.block;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A luminous crystal-sugar surface whose sparse sparks remain purely visual.
 */
public final class StarlightSugarGrassBlock extends Block {
	public StarlightSugarGrassBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos,
			Random random) {
		if (random.nextInt(6) != 0) {
			return;
		}
		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + 1.02D;
		double z = pos.getZ() + random.nextDouble();
		level.addParticle(ParticleTypes.ELECTRIC_SPARK,
				x, y, z, 0.0D, 0.012D, 0.0D);
	}
}
