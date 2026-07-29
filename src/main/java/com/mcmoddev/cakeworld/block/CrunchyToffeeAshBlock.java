package com.mcmoddev.cakeworld.block;

import com.mcmoddev.cakeworld.init.CakeWorldSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Dry Burnt-Toffee-Deltas ground that crackles without becoming a trap.
 *
 * <p>Movement produces a quiet snap and a few ash motes. The block never
 * breaks, drops, launches, slows or damages the entity, so the environmental
 * joke remains safe below Hard and cannot destroy player possessions.</p>
 */
public final class CrunchyToffeeAshBlock extends Block {
	public static final int SNAP_INTERVAL_TICKS = 6;

	public CrunchyToffeeAshBlock(
			BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void stepOn(Level level, BlockPos pos,
			BlockState state, Entity entity) {
		if (!level.isClientSide
				&& entity.tickCount % SNAP_INTERVAL_TICKS == 0
				&& entity.getDeltaMovement()
						.horizontalDistanceSqr() > 0.0025D) {
			level.playSound(null, pos,
					CakeWorldSounds
							.BURNT_TOFFEE_DELTAS_SNAP.get(),
					SoundSource.BLOCKS, 0.3F,
					0.9F + level.random.nextFloat() * 0.2F);
			if (level instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(ParticleTypes.ASH,
						pos.getX() + 0.5D,
						pos.getY() + 1.0D,
						pos.getZ() + 0.5D,
						3, 0.25D, 0.05D, 0.25D,
						0.01D);
			}
		}
		super.stepOn(level, pos, state, entity);
	}
}
