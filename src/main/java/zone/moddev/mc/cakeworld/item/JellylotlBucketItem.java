package zone.moddev.mc.cakeworld.item;

import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public final class JellylotlBucketItem extends MobBucketItem {
	private static final int FLAVOUR_COUNT = 5;

	public JellylotlBucketItem(Properties properties) {
		super(CakeWorldEntities.JELLYLOTL, CakeWorldFluids.LEMONADE,
				() -> SoundEvents.BUCKET_EMPTY_AXOLOTL, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level,
			List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(new TranslatableComponent(flavourKey(stack))
				.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
	}

	public static String flavourKey(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		int variant = tag == null ? 0
				: Math.floorMod(tag.getInt(Axolotl.VARIANT_TAG),
						FLAVOUR_COUNT);
		return "tooltip.cakeworld.jellylotl.flavour." + variant;
	}
}
