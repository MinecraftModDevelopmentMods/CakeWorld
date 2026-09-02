package zone.moddev.mc.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Focused expected-red integrated regression for OS-085. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_underfluids")
public final class UnderFluidsGameTests {
	private static final String EMPTY = "empty";

	private UnderFluidsGameTests() {
	}

	@GameTest(template = EMPTY, batch = "os085world", timeoutTicks = 2400)
	public static void generatedDepositRetainsAFluidContact(
			GameTestHelper helper) {
		DeepPantryGameTests.focusedFizzyPearlAttributionAudit(helper);
	}
}
