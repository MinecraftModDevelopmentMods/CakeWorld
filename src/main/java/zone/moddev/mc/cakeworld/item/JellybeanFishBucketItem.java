package zone.moddev.mc.cakeworld.item;

import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * A Lemonade fish bucket that retains vanilla Tropical Fish tooltip semantics.
 *
 * <p>{@link MobBucketItem} only renders those details for the literal vanilla
 * entity type, so the equivalent public variant API is applied here.</p>
 */
public final class JellybeanFishBucketItem extends MobBucketItem {
	public JellybeanFishBucketItem(Properties properties) {
		super(CakeWorldEntities.JELLYBEAN_FISH,
				CakeWorldFluids.LEMONADE,
				() -> SoundEvents.BUCKET_EMPTY_FISH,
				properties);
	}

	@Override
	public void appendHoverText(ItemStack stack,
			@Nullable Level level, List<Component> tooltip,
			TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains(
				TropicalFish.BUCKET_VARIANT_TAG, 3)) {
			return;
		}

		int variant =
				tag.getInt(TropicalFish.BUCKET_VARIANT_TAG);
		ChatFormatting[] style = {
				ChatFormatting.ITALIC,
				ChatFormatting.GRAY};
		for (int i = 0;
				i < TropicalFish.COMMON_VARIANTS.length;
				i++) {
			if (variant
					== TropicalFish.COMMON_VARIANTS[i]) {
				tooltip.add(new TranslatableComponent(
						TropicalFish
								.getPredefinedName(i))
										.withStyle(style));
				return;
			}
		}

		tooltip.add(new TranslatableComponent(
				TropicalFish.getFishTypeName(variant))
						.withStyle(style));
		String baseKey = "color.minecraft."
				+ TropicalFish.getBaseColor(variant);
		String patternKey = "color.minecraft."
				+ TropicalFish.getPatternColor(variant);
		MutableComponent colours =
				new TranslatableComponent(baseKey);
		if (!baseKey.equals(patternKey)) {
			colours.append(", ").append(
					new TranslatableComponent(patternKey));
		}
		tooltip.add(colours.withStyle(style));
	}
}
