package zone.moddev.mc.cakeworld.gametest;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.ChocolateSpongeBlock;
import zone.moddev.mc.cakeworld.block.HotFudgeLiquidBlock;
import zone.moddev.mc.cakeworld.block.MarshmallowBlock;
import zone.moddev.mc.cakeworld.block.MoltenMallowLiquidBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Integrated functional boundary for the forgiving-hazards contract. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_environmental_safety")
public final class EnvironmentalSafetyGameTests {
	private static final String EMPTY = "empty";

	private EnvironmentalSafetyGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void hazardsWarnHurtSlowlyAndKeepRescuesAccessible(
			GameTestHelper helper) {
		HotFudgeLiquidBlock hotFudge = (HotFudgeLiquidBlock)
				CakeWorldFluids.HOT_FUDGE_BLOCK.get();
		BlockPos contact = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig exposed = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		exposed.setHealth(10.0F);
		hotFudge.entityInside(hotFudge.defaultBlockState(),
				helper.getLevel(), contact, exposed);
		require(helper,
				close(exposed.getHealth(), 9.0D)
						&& !exposed.isOnFire()
						&& hotFudge.defaultBlockState().getLightEmission()
								== HotFudgeLiquidBlock.WARNING_LIGHT,
				"Hot Fudge did not deal one clearly lit, non-burning half-heart");

		float afterFirstContact = exposed.getHealth();
		hotFudge.entityInside(hotFudge.defaultBlockState(),
				helper.getLevel(), contact, exposed);
		require(helper, close(exposed.getHealth(), afterFirstContact),
				"Hot Fudge bypassed the normal damage cooldown");

		Pig protectedPig = helper.spawnWithNoFreeWill(
				EntityType.PIG, 2.5F, 1.0F, 1.5F);
		protectedPig.setHealth(10.0F);
		protectedPig.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 200));
		hotFudge.entityInside(hotFudge.defaultBlockState(),
				helper.getLevel(), contact, protectedPig);
		require(helper, close(protectedPig.getHealth(), 10.0D),
				"Fire Resistance did not provide an accessible Hot Fudge defence");

		ItemEntity dropped = new ItemEntity(helper.getLevel(),
				contact.getX() + 0.5D, contact.getY() + 0.5D,
				contact.getZ() + 0.5D, new ItemStack(Items.DIAMOND));
		hotFudge.entityInside(hotFudge.defaultBlockState(),
				helper.getLevel(), contact, dropped);
		require(helper, dropped.isAlive() && !dropped.isOnFire()
					&& dropped.getItem().is(Items.DIAMOND),
				"Hot Fudge destroyed or ignited a dropped possession");

		MoltenMallowLiquidBlock moltenMallow = (MoltenMallowLiquidBlock)
				CakeWorldFluids.MOLTEN_MALLOW_BLOCK.get();
		ArmorStand falling = new ArmorStand(helper.getLevel(),
				3.5D, 64.0D, 3.5D);
		falling.setDeltaMovement(0.4D, -1.0D, -0.4D);
		falling.fallDistance = 12.0F;
		moltenMallow.entityInside(moltenMallow.defaultBlockState(),
				helper.getLevel(), contact, falling);
		require(helper,
				close(falling.getDeltaMovement().y,
						MoltenMallowLiquidBlock.UPDRAFT)
						&& close(falling.fallDistance, 0.0D),
				"Molten Mallow did not provide its visible rescue updraft");

		Pig marshmallowFall = helper.spawnWithNoFreeWill(
				EntityType.PIG, 3.5F, 1.0F, 1.5F);
		marshmallowFall.setHealth(10.0F);
		MarshmallowBlock marshmallow = (MarshmallowBlock)
				CakeWorldBlocks.MARSHMALLOW.get();
		marshmallow.fallOn(helper.getLevel(), marshmallow.defaultBlockState(),
				contact, marshmallowFall, 20.0F);
		require(helper, close(marshmallowFall.getHealth(), 10.0D),
				"Marshmallow rescue terrain allowed fall damage");

		Pig spongeFall = helper.spawnWithNoFreeWill(
				EntityType.PIG, 4.5F, 1.0F, 1.5F);
		spongeFall.setHealth(10.0F);
		ChocolateSpongeBlock sponge = (ChocolateSpongeBlock)
				CakeWorldBlocks.CHOCOLATE_SPONGE.get();
		sponge.fallOn(helper.getLevel(), sponge.defaultBlockState(), contact,
				spongeFall, 11.0F);
		require(helper, close(spongeFall.getHealth(), 8.0D),
				"Chocolate Sponge did not cushion the reference fall");

		require(helper,
				helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "marshmallow"))
						.isPresent()
						&& helper.getLevel().getRecipeManager().byKey(
								new ResourceLocation(CakeWorld.MODID,
										"cragfire_truffle"))
								.isPresent(),
				"Cushioning or Fire Resistance recovery recipes were unavailable");
		helper.succeed();
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
