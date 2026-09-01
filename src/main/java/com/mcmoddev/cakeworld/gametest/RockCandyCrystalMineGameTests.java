package com.mcmoddev.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Isolated entry point for the expensive fixed-world STRUCT-023 regression.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_crystal_mine")
public final class RockCandyCrystalMineGameTests {
	private static final String EMPTY = "empty";

	private RockCandyCrystalMineGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 12000)
	public static void naturalMineRemainsCompleteAcrossReleasedSurfaceOrdering(
			GameTestHelper helper) {
		DeepPantryGameTests.focusedRockCandyCrystalMineStructureAudit(helper);
	}
}
