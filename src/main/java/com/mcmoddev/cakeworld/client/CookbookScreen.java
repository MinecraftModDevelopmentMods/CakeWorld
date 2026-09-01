package com.mcmoddev.cakeworld.client;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mcmoddev.cakeworld.cookbook.CookbookLayout;
import com.mcmoddev.cakeworld.cookbook.CookbookSummary;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;

import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public final class CookbookScreen extends Screen {
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
		CookbookLayout layout = layout();
		fill(poseStack, layout.left(), layout.top(),
				layout.left() + layout.width(),
				layout.top() + layout.height(), PAGE_COLOUR);
		fill(poseStack, layout.left() + layout.width() / 2,
				layout.pageTop() - 2,
				layout.left() + layout.width() / 2 + 1,
				layout.footerTop() - 4, 0x22351D10);
		drawCrispCentered(poseStack, title, width / 2,
				layout.top() + 9, INK_COLOUR);

		DiscoveryType[] tabs = DiscoveryType.values();
		CookbookSummary summary = ClientCookbookState.summary();
		for (int i = 0; i < tabs.length; i++) {
			int tabLeft = layout.tabLeft(i);
			int tabRight = layout.tabRight(i);
			int tabTop = layout.tabTop(i);
			fill(poseStack, tabLeft, tabTop, tabRight - 1,
					tabTop + CookbookLayout.TAB_HEIGHT,
					tabs[i] == selected ? ACTIVE_TAB_COLOUR : TAB_COLOUR);
			drawCrispCentered(poseStack,
					new TranslatableComponent(tabs[i].translationKey()),
					(tabLeft + tabRight) / 2, tabTop + 5, INK_COLOUR);
			if (summary.hasStamp(tabs[i])) {
				drawCrisp(poseStack, "+", tabRight - 8,
						tabTop + 5, INK_COLOUR);
			}
			if (tabs[i] == selected) {
				fill(poseStack, tabLeft + 2,
						tabTop + CookbookLayout.TAB_HEIGHT - 2,
						tabRight - 3,
						tabTop + CookbookLayout.TAB_HEIGHT,
						INK_COLOUR);
			}
		}

		List<ResourceLocation> pages =
				new ArrayList<>(ClientCookbookState.get(selected));
		drawCrispCentered(poseStack,
				new TranslatableComponent(
						"screen.cakeworld.cookbook.selected_tab",
						new TranslatableComponent(selected.translationKey()),
						pages.size()),
				width / 2, layout.headingTop(), INK_COLOUR);
		if (pages.isEmpty()) {
			drawCrispCentered(poseStack,
					new TranslatableComponent("screen.cakeworld.cookbook.empty"),
					width / 2, layout.pageTop() + 8, 0xFF7A5735);
		} else {
			int rows = layout.pageRows();
			int capacity = layout.visiblePageCapacity();
			for (int i = 0; i < Math.min(pages.size(), capacity); i++) {
				int column = i / rows;
				int row = i % rows;
				int columnWidth = layout.width() / layout.pageColumns();
				int x = layout.left() + 14 + column * columnWidth;
				int y = layout.pageTop() + row * 19;
				drawCrisp(poseStack,
						fitPageName(pageName(pages.get(i)),
								columnWidth - 24),
						x, y,
						INK_COLOUR);
			}
		}
		drawCrispCentered(poseStack,
				new TranslatableComponent(
						"screen.cakeworld.cookbook.summary",
						summary.totalPages(), summary.stamps(),
						summary.stampGoal()),
				width / 2, layout.footerTop(), INK_COLOUR);
		if (summary.firstEditionComplete()) {
			drawCrispCentered(poseStack,
					new TranslatableComponent(
							"screen.cakeworld.cookbook.first_edition_complete"),
					width / 2, layout.footerTop() + 14, 0xFF9A5A00);
		} else {
			drawCrispCentered(poseStack,
					new TranslatableComponent(
							"screen.cakeworld.cookbook.controls"),
					width / 2, layout.footerTop() + 14, 0xFF7A5735);
		}
		super.render(poseStack, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int index = layout().tabIndexAt(mouseX, mouseY,
				DiscoveryType.values().length);
		if (index >= 0) {
			select(DiscoveryType.values()[index]);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_A) {
			cycle(-1);
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_D) {
			cycle(1);
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_HOME) {
			select(DiscoveryType.values()[0]);
			return true;
		}
		if (keyCode == GLFW.GLFW_KEY_END) {
			select(DiscoveryType.values()[
					DiscoveryType.values().length - 1]);
			return true;
		}
		if (keyCode >= GLFW.GLFW_KEY_1
				&& keyCode < GLFW.GLFW_KEY_1
						+ DiscoveryType.values().length) {
			select(DiscoveryType.values()[
					keyCode - GLFW.GLFW_KEY_1]);
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public Component getNarrationMessage() {
		CookbookSummary summary = ClientCookbookState.summary();
		return new TranslatableComponent(
				"screen.cakeworld.cookbook.narration",
				title,
				new TranslatableComponent(selected.translationKey()),
				ClientCookbookState.get(selected).size(),
				summary.totalPages(), summary.stamps(),
				summary.stampGoal());
	}

	@Override
	protected void updateNarrationState(
			NarrationElementOutput narrationOutput) {
		narrationOutput.add(NarratedElementType.TITLE,
				getNarrationMessage());
		narrationOutput.add(NarratedElementType.USAGE,
				new TranslatableComponent(
						"screen.cakeworld.cookbook.narration.usage"));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private CookbookLayout layout() {
		return CookbookLayout.calculate(width, height,
				DiscoveryType.values().length);
	}

	private void cycle(int direction) {
		DiscoveryType[] tabs = DiscoveryType.values();
		int next = Math.floorMod(selected.ordinal() + direction,
				tabs.length);
		select(tabs[next]);
	}

	private void select(DiscoveryType tab) {
		if (selected != tab) {
			selected = tab;
			triggerImmediateNarration(true);
		}
	}

	private String fitPageName(String name, int maximumWidth) {
		if (font.width(name) <= maximumWidth) {
			return name;
		}
		String suffix = "...";
		return font.plainSubstrByWidth(name,
				Math.max(0, maximumWidth - font.width(suffix))) + suffix;
	}

	private void drawCrisp(PoseStack poseStack, String text,
			float x, float y, int colour) {
		font.draw(poseStack, text, x, y, colour);
	}

	private void drawCrispCentered(PoseStack poseStack, Component text,
			int centreX, int y, int colour) {
		font.draw(poseStack, text, centreX - font.width(text) / 2.0F,
				y, colour);
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
