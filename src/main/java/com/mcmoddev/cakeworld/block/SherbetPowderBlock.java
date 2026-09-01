package com.mcmoddev.cakeworld.block;

import com.mcmoddev.cakeworld.init.CakeWorldEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Soft, fizzy dune terrain.
 *
 * <p>The short refresh is deliberately movement-only: walking through the
 * dunes feels buoyant, but never launches, hurts or traps an entity.</p>
 */
public final class SherbetPowderBlock extends Block {
	public static final int FIZZY_FEET_TICKS = 60;

	public SherbetPowderBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void stepOn(Level level, BlockPos pos,
			BlockState state, Entity entity) {
		if (!level.isClientSide && entity instanceof LivingEntity living) {
			MobEffectInstance current =
					living.getEffect(CakeWorldEffects.FIZZY_FEET.get());
			if (current == null
					|| current.getDuration() < FIZZY_FEET_TICKS / 2) {
				living.addEffect(new MobEffectInstance(
						CakeWorldEffects.FIZZY_FEET.get(),
						FIZZY_FEET_TICKS, 0,
						false, true));
			}
		}
		super.stepOn(level, pos, state, entity);
	}
}
