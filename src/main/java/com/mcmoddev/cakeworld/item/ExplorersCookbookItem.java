package com.mcmoddev.cakeworld.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.cakeworld.cookbook.CookbookHints;
import com.mcmoddev.cakeworld.network.CakeWorldNetwork;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public final class ExplorersCookbookItem extends Item {
	public ExplorersCookbookItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player,
			InteractionHand hand) {
		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			if (player.isShiftKeyDown()) {
				CookbookHints.showHint(serverPlayer);
			} else {
				CakeWorldNetwork.openCookbook(serverPlayer);
			}
		}
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),
				level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level,
			List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(new TranslatableComponent(
				"tooltip.cakeworld.explorers_cookbook.hint")
						.withStyle(ChatFormatting.GRAY));
	}
}
