package com.mcmoddev.cakeworld.cookbook;

/**
 * The six ways CakeWorld can add a page to the Explorer's Cookbook.
 */
public enum DiscoveryType {
	VISITING("places"),
	TASTING("recipes"),
	MEETING("creatures"),
	MINING("ingredients"),
	CRAFTING("curiosities"),
	FINDING("landmarks");

	private final String tabName;

	DiscoveryType(String tabName) {
		this.tabName = tabName;
	}

	public String tabName() {
		return tabName;
	}

	public String translationKey() {
		return "screen.cakeworld.cookbook.tab." + tabName;
	}
}
