package com.mcmoddev.cakeworld.item;

import java.util.function.Supplier;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public final class CakeBucketItem extends BucketItem {
	private final Supplier<? extends Fluid> fluid;

	public CakeBucketItem(Supplier<? extends Fluid> fluid, Item.Properties properties) {
		super(fluid, properties);
		this.fluid = fluid;
	}

	@Override
	public Fluid getFluid() {
		return fluid.get();
	}
}
