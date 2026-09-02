package zone.moddev.mc.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Focused integrated regression for stable-ID biome dictionary weights. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_geome_dictionary")
public final class GeomeDictionaryGameTests {
	private static final String EMPTY = "empty";

	private GeomeDictionaryGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void exactAndDictionaryWeightsRemainAdditive(
			GameTestHelper helper) {
		DeepPantryGameTests
				.biomeIdAndDictionaryGeomeRulesAreBakedAndObserved(helper);
	}
}
