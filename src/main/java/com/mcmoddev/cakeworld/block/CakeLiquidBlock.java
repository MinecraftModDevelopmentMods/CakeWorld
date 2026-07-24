package com.mcmoddev.cakeworld.block;

import java.util.function.Supplier;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;

public final class CakeLiquidBlock extends LiquidBlock {
	private final Supplier<? extends FlowingFluid> fluid;

	public CakeLiquidBlock(Supplier<? extends FlowingFluid> fluid,
			BlockBehaviour.Properties properties) {
		super(fluid, properties);
		this.fluid = fluid;
	}

	@Override
	public FlowingFluid getFluid() {
		return fluid.get();
	}
}
