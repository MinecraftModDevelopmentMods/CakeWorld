package com.mcmoddev.cakeworld.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A non-placeable kitchen tool returned after it participates in crafting.
 */
public final class ReusableKitchenToolItem extends Item {
	public ReusableKitchenToolItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		return new ItemStack(this);
	}
}
