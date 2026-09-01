package com.mcmoddev.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Focused fresh/reload regressions for the optional BaseMetals template. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_basemetals_regression")
public final class BaseMetalsRegressionGameTests {
	private static final String EMPTY = "empty";

	private BaseMetalsRegressionGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 1200)
	public static void counterpartsRetainProcessingContracts(
			GameTestHelper helper) {
		DeepPantryGameTests
				.baseMetalsCounterpartsPreserveTagsRecipesAndGeneration(helper);
	}

	@GameTest(template = EMPTY, batch = "os085world",
			timeoutTicks = 2400)
	public static void underFluidOutputRemainsIndependentlyAttributable(
			GameTestHelper helper) {
		DeepPantryGameTests.focusedFizzyPearlAttributionAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "bioend004world",
			timeoutTicks = 24000)
	public static void starsteelGeneratesInEndFamilyHosts(
			GameTestHelper helper) {
		StarlightSugarFieldsGameTests
				.focusedNaturalStarlightSugarFieldsAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "bioow012world",
			timeoutTicks = 24000)
	public static void overworldLandmarkSurvivesCompatibilityProfile(
			GameTestHelper helper) {
		CupcakeGardensGameTests.focusedNaturalCupcakeBloomAudit(helper);
	}
}
