package zone.moddev.mc.cakeworld.gametest;

import zone.moddev.mc.cakeworld.command.CakeWorldCommands;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Public active-profile query and read-only command proof for OS-008. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_orespawn_status")
public final class OreSpawnStatusCommandGameTests {
	private static final String EMPTY = "empty";

	private OreSpawnStatusCommandGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void statusCommandReportsActiveProfile(GameTestHelper helper) {
		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		Component component = CakeWorldCommands.statusComponent(profile);
		require(helper, component instanceof TranslatableComponent,
				"CakeWorld OreSpawn status is not translatable");
		TranslatableComponent status = (TranslatableComponent) component;
		Object[] arguments = status.getArgs();
		require(helper, CakeWorldCommands.STATUS_KEY.equals(status.getKey())
				&& arguments.length == 8
				&& arguments[0].equals(profile.selectedTemplate()
						.orElseThrow().toString())
				&& arguments[1].equals(profile.geologyMode())
				&& arguments[2].equals(profile.rockIds().size())
				&& arguments[3].equals(profile.geomeIds().size())
				&& arguments[4].equals(profile.oreIds().size())
				&& arguments[5].equals(profile.fluidDepositIds().size())
				&& arguments[6].equals(profile.biomePaletteIds().size())
				&& arguments[7].equals(profile.terrainDimensions().size()),
				"CakeWorld OreSpawn status lost an active-profile field");
		int result = helper.getLevel().getServer().getCommands()
				.performCommand(helper.getLevel().getServer()
						.createCommandSourceStack()
						.withPermission(0)
						.withSuppressedOutput(),
						"cakeworld orespawn");
		require(helper, result == 1,
				"Read-only CakeWorld OreSpawn status command was unavailable");
		helper.succeed();
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
