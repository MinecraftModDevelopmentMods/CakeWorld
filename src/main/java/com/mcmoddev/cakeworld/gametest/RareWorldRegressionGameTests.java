package com.mcmoddev.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Focused entry points for expensive natural-landmark regressions. Keeping
 * these in a dedicated namespace makes rarity and safe-site changes testable
 * without rerunning the complete CakeWorld suite.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_rare_regression")
public final class RareWorldRegressionGameTests {
	private static final String EMPTY = "empty";

	private RareWorldRegressionGameTests() {
	}

	@GameTest(template = EMPTY, batch = "rarereg001",
			timeoutTicks = 24000)
	public static void blackLiquoriceLoop(GameTestHelper helper) {
		BlackLiquoriceLabyrinthsGameTests
				.focusedNaturalBlackLiquoriceLabyrinthsAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "rarereg002",
			timeoutTicks = 24000)
	public static void roadsideCuriosities(GameTestHelper helper) {
		DeepPantryGameTests
				.focusedNaturalRoadsideCuriosityAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "rarereg003",
			timeoutTicks = 24000)
	public static void candyCaneHoodooGarden(
			GameTestHelper helper) {
		DeepPantryGameTests
				.focusedNaturalCandyCaneHoodooGardenAudit(helper);
	}
}
