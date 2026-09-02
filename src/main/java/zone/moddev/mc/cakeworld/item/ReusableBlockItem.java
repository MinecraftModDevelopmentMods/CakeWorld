package zone.moddev.mc.cakeworld.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Placeable tool whose stack is returned after crafting.
 */
public final class ReusableBlockItem extends BlockItem {
	public ReusableBlockItem(Block block, Properties properties) {
		super(block, properties);
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
