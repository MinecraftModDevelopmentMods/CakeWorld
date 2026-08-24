package com.mcmoddev.cakeworld.block;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

/**
 * A warm rescue fluid that puffs falling entities back toward its surface.
 *
 * <p>It deliberately is not tagged as water or lava: it neither hides lava
 * damage behind a confectionery name nor grants ordinary swimming behaviour.
 * Crouching suppresses the updraft so players can descend deliberately.</p>
 */
public final class MoltenMallowLiquidBlock extends CakeLiquidBlock {
	public static final double HORIZONTAL_DRAG = 0.82D;
	public static final double UPDRAFT = 0.16D;
	public static final double CROUCH_DESCENT_DRAG = 0.25D;

	public MoltenMallowLiquidBlock(
			Supplier<? extends FlowingFluid> fluid,
			BlockBehaviour.Properties properties) {
		super(fluid, properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos,
			Entity entity) {
		super.entityInside(state, level, pos, entity);
		Vec3 movement = entity.getDeltaMovement();
		entity.fallDistance = 0.0F;
		double vertical = entity.isSuppressingBounce()
				? Math.min(0.0D, movement.y) * CROUCH_DESCENT_DRAG
				: Math.max(movement.y, UPDRAFT);
		entity.setDeltaMovement(movement.x * HORIZONTAL_DRAG, vertical,
				movement.z * HORIZONTAL_DRAG);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos,
			Random random) {
		super.animateTick(state, level, pos, random);
		if (level.getBlockState(pos.above()).isAir()
				&& random.nextInt(5) == 0) {
			level.addParticle(ParticleTypes.CLOUD,
					pos.getX() + random.nextDouble(), pos.getY() + 1.02D,
					pos.getZ() + random.nextDouble(),
					0.0D, 0.035D, 0.0D);
		}
	}
}
