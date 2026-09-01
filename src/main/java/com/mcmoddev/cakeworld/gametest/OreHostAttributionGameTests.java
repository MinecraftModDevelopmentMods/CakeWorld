package com.mcmoddev.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Focused public-sampler proof for explicit and family ore hosts. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_ore_host_attribution")
public final class OreHostAttributionGameTests {
	private static final String EMPTY = "empty";

	private OreHostAttributionGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void sampledPreOreHostsMatchGeneratedOutputs(
			GameTestHelper helper) {
		DeepPantryGameTests.focusedFamilyOreHostAttributionAudit(helper);
	}
}
