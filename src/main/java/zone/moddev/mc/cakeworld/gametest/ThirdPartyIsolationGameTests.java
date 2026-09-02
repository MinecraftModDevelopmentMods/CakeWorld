package zone.moddev.mc.cakeworld.gametest;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.WaferWraith;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

/** Runtime proof that a modded subtype is not captured by vanilla conversion. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_third_party_isolation")
public final class ThirdPartyIsolationGameTests {
	private static final String EMPTY = "empty";

	private ThirdPartyIsolationGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public static void nonVanillaPhantomSubtypeSurvivesTheCompleteJoinEventBus(
			GameTestHelper helper) {
		WaferWraith external = CakeWorldEntities.WAFER_WRAITH.get()
				.create(helper.getLevel());
		ResourceLocation externalKey = external == null ? null
				: ForgeRegistries.ENTITIES.getKey(external.getType());
		require(helper, external != null
					&& external instanceof Phantom
					&& externalKey != null
					&& CakeWorld.MODID.equals(externalKey.getNamespace())
					&& "wafer_wraith".equals(externalKey.getPath()),
				"SYS-012 fixture was not a registered non-vanilla Phantom subtype");

		var position = helper.absolutePos(new net.minecraft.core.BlockPos(1, 2, 1));
		external.moveTo(position.getX() + 0.5D, position.getY(),
				position.getZ() + 0.5D);
		external.setHealth(13.0F);
		external.setCustomName(new TextComponent("External Phantom Fixture"));
		ResourceLocation biome = helper.getLevel().getBiome(position)
				.unwrapKey().map(key -> key.location()).orElse(null);
		require(helper, biome != null
					&& CakeWorld.MODID.equals(biome.getNamespace()),
				"External-entity fixture was not inside the active CakeWorld world");

		EntityJoinWorldEvent event = new EntityJoinWorldEvent(
				external, helper.getLevel(), false);
		MinecraftForge.EVENT_BUS.post(event);
		require(helper, !event.isCanceled(),
				"CakeWorld or another handler cancelled the external join event");

		helper.runAfterDelay(3, () -> {
			require(helper,
					!external.isRemoved()
							&& external.getType()
									== CakeWorldEntities.WAFER_WRAITH.get()
							&& close(external.getHealth(), 13.0D)
							&& external.hasCustomName()
							&& "External Phantom Fixture".equals(
									external.getCustomName().getString()),
					"Unknown modded subtype was converted or mutated after joining");
			helper.succeed();
		});
	}

	private static boolean close(double left, double right) {
		return Math.abs(left - right) < 0.001D;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
