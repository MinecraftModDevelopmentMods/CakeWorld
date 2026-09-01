package com.mcmoddev.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Focused entry points for deterministic fixture-isolation and landmark
 * longevity regressions discovered by the complete CakeWorld suite.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_fixture_regression")
public final class FixtureRegressionGameTests {
	private static final String EMPTY = "empty";

	private FixtureRegressionGameTests() {
	}

	@GameTest(template = EMPTY, batch = "fixturereg001",
			timeoutTicks = 1200)
	public static void lollipopGrove(GameTestHelper helper) {
		LollipopOrchardsGameTests
				.groveIsBoundedRenewableAndPersistent(helper);
	}

	@GameTest(template = EMPTY, batch = "fixturereg002",
			timeoutTicks = 1200)
	public static void waffleSkywheel(GameTestHelper helper) {
		WafflePlateausGameTests
				.skywheelIsBoundedPoweredAndSealed(helper);
	}

	@GameTest(template = EMPTY, batch = "fixturereg003",
			timeoutTicks = 1200)
	public static void protectedPicnicCompanion(
			GameTestHelper helper) {
		FirstBiteGameTests
				.starterPicnicBuildsAReadableCookbookLandmark(helper);
	}
}
