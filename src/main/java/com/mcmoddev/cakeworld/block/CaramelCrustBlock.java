package com.mcmoddev.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * A walkable caramel skin. It makes routes noticeably sticky without stopping
 * steering, pulling upward movement down, or turning ordinary terrain into a
 * disguised damage source.
 */
public final class CaramelCrustBlock extends Block {
	public static final float SURFACE_SPEED_FACTOR = 0.62F;
	public static final double HORIZONTAL_DRAG = 0.62D;
	public static final double DOWNWARD_DRAG = 0.8D;

	public CaramelCrustBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public float getSpeedFactor() {
		// This is the native hook used while an entity walks on a full
		// collision block. entityInside alone cannot supply floor drag.
		return SURFACE_SPEED_FACTOR;
	}

	@Override
	public void entityInside(BlockState state, Level level,
			BlockPos position, Entity entity) {
		super.entityInside(state, level, position, entity);
		Vec3 movement = entity.getDeltaMovement();
		double vertical = movement.y < 0.0D
				? movement.y * DOWNWARD_DRAG
				: movement.y;
		entity.setDeltaMovement(
				movement.x * HORIZONTAL_DRAG,
				vertical,
				movement.z * HORIZONTAL_DRAG);
	}
}
