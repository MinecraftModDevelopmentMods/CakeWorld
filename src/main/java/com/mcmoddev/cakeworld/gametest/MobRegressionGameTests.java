package com.mcmoddev.cakeworld.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Focused entry points for long mob-role regressions that otherwise share the
 * main CakeWorld namespace with world-generation surveys. Each test has its
 * own batch because difficulty and game rules belong to the whole server, not
 * to an individual GameTest structure.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_mob_regression")
public final class MobRegressionGameTests {
	private static final String EMPTY = "empty";

	private MobRegressionGameTests() {
	}

	@GameTest(template = EMPTY, batch = "mobreg001", timeoutTicks = 500)
	public static void gumballGuardian(GameTestHelper helper) {
		FirstBiteGameTests
				.gumballGuardiansSignalPalaceBeamsGentleUntilHard(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg002", timeoutTicks = 500)
	public static void fizzballFish(GameTestHelper helper) {
		FirstBiteGameTests
				.fizzballFishKeepInflationBucketsAndSafeStings(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg003", timeoutTicks = 500)
	public static void gingerbreadStomper(GameTestHelper helper) {
		FirstBiteGameTests
				.gingerbreadStompersKeepRaidRoarAndSafeObstacles(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg004", timeoutTicks = 500)
	public static void crumbledGingerbreadFolk(GameTestHelper helper) {
		FirstBiteGameTests
				.crumbledGingerbreadFolkKeepTheCompleteZombieVillagerRole(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg005", timeoutTicks = 500)
	public static void iceCreamGolem(GameTestHelper helper) {
		FirstBiteGameTests
				.iceCreamGolemsKeepConstructionScoopsClimateAndIcingTrail(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg006", timeoutTicks = 500)
	public static void fudgeFolk(GameTestHelper helper) {
		FirstBiteGameTests
				.fudgeFolkKeepPiglinSocietyBarterAndSafePeril(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg007", timeoutTicks = 500)
	public static void sourSprite(GameTestHelper helper) {
		FirstBiteGameTests
				.sourSpritesKeepSummoningFlightLifeAndSafeCharges(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg008", timeoutTicks = 500)
	public static void rollingPinRaider(GameTestHelper helper) {
		FirstBiteGameTests
				.rollingPinRaidersKeepVindicatorRaidsAndSafeShoves(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg009", timeoutTicks = 500)
	public static void burntCandyKnight(GameTestHelper helper) {
		FirstBiteGameTests
				.burntCandyKnightsKeepFortressSkullsAndDifficultySafety(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg010", timeoutTicks = 500)
	public static void staleFudgeBoar(GameTestHelper helper) {
		FirstBiteGameTests
				.staleFudgeBoarsKeepZoglinConversionAndSafeUndeadPeril(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg011", timeoutTicks = 500)
	public static void staleCrumbler(GameTestHelper helper) {
		FirstBiteGameTests
				.staleCrumblersKeepTheCompleteZombieRoleSafely(helper);
	}

	@GameTest(template = EMPTY, batch = "mobreg012", timeoutTicks = 500)
	public static void staleFudgeFolk(GameTestHelper helper) {
		FirstBiteGameTests
				.staleFudgeFolkKeepTheCompleteZombifiedPiglinRole(helper);
	}
}
