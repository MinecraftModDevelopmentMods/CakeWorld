package zone.moddev.mc.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Focused entry points for the integrated world-generation regressions found
 * by the broad CakeWorld suite. Each test receives its own batch so forced
 * chunks, delayed assertions and shared world state cannot overlap.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_world_regression")
public final class WorldRegressionGameTests {
	private static final String EMPTY = "empty";

	private WorldRegressionGameTests() {
	}

	@GameTest(template = EMPTY, batch = "worldreg001",
			timeoutTicks = 7200)
	public static void confectionersCottage(GameTestHelper helper) {
		DeepPantryGameTests
				.focusedConfectionersCottageStructureAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg002",
			timeoutTicks = 7200)
	public static void moltenMarshmallowCalderas(
			GameTestHelper helper) {
		MoltenMarshmallowCalderasGameTests
				.focusedNaturalMoltenMarshmallowCalderasAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg003",
			timeoutTicks = 7200)
	public static void chilliChocolateCrags(GameTestHelper helper) {
		ChilliChocolateCragsGameTests
				.focusedNaturalChilliChocolateCragsAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg004",
			timeoutTicks = 7200)
	public static void sodaPalace(GameTestHelper helper) {
		DeepPantryGameTests.focusedSodaPalaceStructureAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg005",
			timeoutTicks = 2400)
	public static void weightedGeomePlacement(GameTestHelper helper) {
		DeepPantryGameTests.focusedGeomeWeightedPlacementAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg006",
			timeoutTicks = 2400)
	public static void rareOreOutputs(GameTestHelper helper) {
		DeepPantryGameTests.focusedBiomeWorldgenAuditsRareOutputs(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg007",
			timeoutTicks = 2400)
	public static void biomeAndDictionaryGeomes(GameTestHelper helper) {
		DeepPantryGameTests
				.biomeIdAndDictionaryGeomeRulesAreBakedAndObserved(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg008",
			timeoutTicks = 4800)
	public static void biscuitBanditLookout(GameTestHelper helper) {
		DeepPantryGameTests
				.focusedBiscuitBanditLookoutStructureAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg009",
			timeoutTicks = 7200)
	public static void burntSugarArch(GameTestHelper helper) {
		DeepPantryGameTests.focusedBurntSugarArchStructureAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg010",
			timeoutTicks = 4800)
	public static void gingerbreadVillage(GameTestHelper helper) {
		DeepPantryGameTests
				.focusedGingerbreadVillageStructureAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg011",
			timeoutTicks = 2400)
	public static void buriedSweetTin(GameTestHelper helper) {
		FirstBiteGameTests
				.buriedSweetTinKeepsNativeMapPlacementAndHeartLoot(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg012",
			timeoutTicks = 7200)
	public static void candyCaneBridge(GameTestHelper helper) {
		DeepPantryGameTests
				.focusedCandyCaneBridgeStructureAudit(helper);
	}

	@GameTest(template = EMPTY, batch = "worldreg013",
			timeoutTicks = 16000)
	public static void cookbookKiosk(GameTestHelper helper) {
		DeepPantryGameTests
				.focusedNaturalCookbookKioskAudit(helper);
	}

}
