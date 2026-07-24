package com.mcmoddev.cakeworld.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public final class CookbookScreen extends Screen {
	private static final int BOOK_WIDTH = 390;
	private static final int BOOK_HEIGHT = 210;
	private static final int TAB_HEIGHT = 18;
	private static final int PAGE_COLOUR = 0xFFF7DCA4;
	private static final int INK_COLOUR = 0xFF4B2A18;
	private static final int TAB_COLOUR = 0xFFE9B96E;
	private static final int ACTIVE_TAB_COLOUR = 0xFFFFD783;

	private DiscoveryType selected = DiscoveryType.VISITING;

	public CookbookScreen() {
		super(new TranslatableComponent("screen.cakeworld.cookbook.title"));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		renderBackground(poseStack);
		int left = (width - BOOK_WIDTH) / 2;
		int top = (height - BOOK_HEIGHT) / 2;
		fill(poseStack, left, top, left + BOOK_WIDTH, top + BOOK_HEIGHT, PAGE_COLOUR);
		fill(poseStack, left + BOOK_WIDTH / 2 - 1, top + 8,
				left + BOOK_WIDTH / 2 + 1, top + BOOK_HEIGHT - 8, 0x55351D10);
		drawCenteredString(poseStack, font, title, width / 2, top + 9, INK_COLOUR);

		DiscoveryType[] tabs = DiscoveryType.values();
		int tabWidth = BOOK_WIDTH / tabs.length;
		for (int i = 0; i < tabs.length; i++) {
			int tabLeft = left + i * tabWidth;
			fill(poseStack, tabLeft, top + 25, tabLeft + tabWidth - 1,
					top + 25 + TAB_HEIGHT,
					tabs[i] == selected ? ACTIVE_TAB_COLOUR : TAB_COLOUR);
			drawCenteredString(poseStack, font,
					new TranslatableComponent(tabs[i].translationKey()),
					tabLeft + tabWidth / 2, top + 30, INK_COLOUR);
		}

		List<ResourceLocation> pages =
				new ArrayList<>(ClientCookbookState.get(selected));
		if (pages.isEmpty()) {
			drawCenteredString(poseStack, font,
					new TranslatableComponent("screen.cakeworld.cookbook.empty"),
					width / 2, top + 79, 0xFF7A5735);
		} else {
			for (int i = 0; i < Math.min(pages.size(), 12); i++) {
				int column = i / 6;
				int row = i % 6;
				int x = left + 14 + column * (BOOK_WIDTH / 2);
				int y = top + 55 + row * 19;
				drawString(poseStack, font, pageName(pages.get(i)), x, y,
						INK_COLOUR);
			}
		}
		super.render(poseStack, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int left = (width - BOOK_WIDTH) / 2;
		int top = (height - BOOK_HEIGHT) / 2;
		if (mouseY >= top + 25 && mouseY < top + 25 + TAB_HEIGHT
				&& mouseX >= left && mouseX < left + BOOK_WIDTH) {
			int tabWidth = BOOK_WIDTH / DiscoveryType.values().length;
			int index = Math.min(DiscoveryType.values().length - 1,
					(int) (mouseX - left) / tabWidth);
			selected = DiscoveryType.values()[index];
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private static String pageName(ResourceLocation id) {
		String name = id.getPath().replace('_', ' ');
		StringBuilder result = new StringBuilder(name.length());
		boolean capitalise = true;
		for (char character : name.toCharArray()) {
			if (capitalise) {
				result.append(Character.toUpperCase(character));
				capitalise = false;
			} else {
				result.append(character);
			}
			if (character == ' ') {
				capitalise = true;
			}
		}
		return result.toString();
	}
}
