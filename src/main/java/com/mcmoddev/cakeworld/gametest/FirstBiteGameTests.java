package com.mcmoddev.cakeworld.gametest;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class FirstBiteGameTests {
	private static final String EMPTY = "empty";

	private FirstBiteGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void softTerrainHasThePromisedPhysicalContracts(GameTestHelper helper) {
		BlockState sponge = CakeWorldBlocks.CHOCOLATE_SPONGE.get().defaultBlockState();
		BlockState icing = CakeWorldBlocks.ICING_LAYER.get().defaultBlockState();
		BlockState crumbs = CakeWorldBlocks.BISCUIT_CRUMBS.get().defaultBlockState();
		BlockState lemonadeIce = CakeWorldBlocks.FROZEN_LEMONADE.get().defaultBlockState();

		require(helper, sponge.getBlock() instanceof ChocolateSpongeBlock,
				"Chocolate sponge is not using its cushioning/nibbling block");
		require(helper, icing.getBlock() instanceof IcingLayerBlock,
				"Icing is not using its accumulating layer block");
		require(helper, icing.getValue(BlockStateProperties.LAYERS) == 1,
				"Icing does not begin as one accumulatable layer");
		require(helper, crumbs.getBlock() instanceof BiscuitCrumbsBlock
						&& crumbs.getBlock() instanceof FallingBlock,
				"Biscuit crumbs are not gravity affected");

		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig frictionTester = helper.spawnWithNoFreeWill(EntityType.PIG, 1.5F, 1.0F, 1.5F);
		float friction = lemonadeIce.getFriction(helper.getLevel(), testPos, frictionTester);
		require(helper, Math.abs(friction - 0.96F) < 0.0001F,
				"Frozen lemonade did not retain its distinctive 0.96 friction");

		Pig spongeTester = helper.spawnWithNoFreeWill(EntityType.PIG, 2.5F, 1.0F, 1.5F);
		spongeTester.setHealth(10.0F);
		sponge.getBlock().fallOn(helper.getLevel(), sponge, testPos, spongeTester, 11.0F);
		require(helper, Math.abs(spongeTester.getHealth() - 8.0F) < 0.001F,
				"Chocolate sponge did not reduce an eight-point fall to two points");

		Pig icingTester = helper.spawnWithNoFreeWill(EntityType.PIG, 3.5F, 1.0F, 1.5F);
		icingTester.setHealth(10.0F);
		icing.getBlock().fallOn(helper.getLevel(), icing, testPos, icingTester, 11.0F);
		require(helper, Math.abs(icingTester.getHealth() - 6.0F) < 0.001F,
				"Icing did not halve an eight-point fall");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void spongeNibblingIsDeliberateVisibleAndFinite(GameTestHelper helper) {
		BlockPos relativePos = new BlockPos(1, 1, 1);
		BlockPos absolutePos = helper.absolutePos(relativePos);
		ChocolateSpongeBlock sponge = (ChocolateSpongeBlock) CakeWorldBlocks.CHOCOLATE_SPONGE.get();
		helper.setBlock(relativePos, sponge.defaultBlockState());

		Player player = helper.makeMockPlayer();
		player.getFoodData().setFoodLevel(10);
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(absolutePos), Direction.UP,
				absolutePos, false);

		InteractionResult first = sponge.use(helper.getBlockState(relativePos), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND, hit);
		require(helper, first.consumesAction(), "Empty-hand nibbling did not consume the action");
		require(helper, player.getFoodData().getFoodLevel() == 11,
				"A raw-terrain nibble did not restore exactly one hunger point");
		require(helper, helper.getBlockState(relativePos).getValue(ChocolateSpongeBlock.BITES) == 1,
				"The first nibble did not expose its visible bite state");

		player.getFoodData().setFoodLevel(20);
		InteractionResult full = sponge.use(helper.getBlockState(relativePos), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND, hit);
		require(helper, full == InteractionResult.PASS,
				"Chocolate sponge could be nibbled at full hunger");
		require(helper, helper.getBlockState(relativePos).getValue(ChocolateSpongeBlock.BITES) == 1,
				"Full-hunger use changed the bite state");

		player.getFoodData().setFoodLevel(10);
		for (int i = 0; i < 3; i++) {
			sponge.use(helper.getBlockState(relativePos), helper.getLevel(), absolutePos, player,
					InteractionHand.MAIN_HAND, hit);
		}
		require(helper, helper.getBlockState(relativePos).isAir(),
				"Four portions did not fully consume the sponge block");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public static void biscuitCrumbsActuallyFall(GameTestHelper helper) {
		BlockPos start = new BlockPos(1, 3, 1);
		BlockPos destination = new BlockPos(1, 1, 1);
		helper.setBlock(new BlockPos(1, 0, 1), Blocks.STONE);
		helper.setBlock(start, CakeWorldBlocks.BISCUIT_CRUMBS.get());
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(CakeWorldBlocks.BISCUIT_CRUMBS.get(), destination);
			helper.assertBlockNotPresent(CakeWorldBlocks.BISCUIT_CRUMBS.get(), start);
		});
	}

	@GameTest(template = EMPTY)
	public static void preparedFoodsBeatRawTerrain(GameTestHelper helper) {
		FoodProperties slice = requireFood(helper,
				CakeWorldItems.CHOCOLATE_SPONGE_SLICE.get().getDefaultInstance());
		FoodProperties icing = requireFood(helper,
				CakeWorldItems.ICING_SPOONFUL.get().getDefaultInstance());
		FoodProperties biscuit = requireFood(helper,
				CakeWorldItems.SIMPLE_BISCUIT.get().getDefaultInstance());
		FoodProperties lemonade = requireFood(helper,
				CakeWorldItems.LEMONADE_BOTTLE.get().getDefaultInstance());
		FoodProperties sundae = requireFood(helper,
				CakeWorldItems.CHOCOLATE_SPONGE_SUNDAE.get().getDefaultInstance());

		require(helper, slice.getNutrition() > ChocolateSpongeBlock.NIBBLE_NUTRITION,
				"Sponge slices are not better than raw terrain");
		require(helper, icing.getNutrition() == 1 && biscuit.getNutrition() == 4
						&& lemonade.getNutrition() == 3,
				"Starter foods do not match their designed nutrition");
		require(helper, sundae.getNutrition() == 8
						&& sundae.getSaturationModifier() > slice.getSaturationModifier(),
				"The prepared starter meal is not a worthwhile upgrade");
		require(helper, CakeWorldItems.LEMONADE_BOTTLE.get().getCraftingRemainingItem()
						== Items.GLASS_BOTTLE,
				"Lemonade bottles do not return their container in crafting");
		helper.succeed();
	}

	private static FoodProperties requireFood(GameTestHelper helper, ItemStack stack) {
		FoodProperties food = stack.getItem().getFoodProperties();
		require(helper, food != null, stack.getItem() + " has no food properties");
		return food;
	}

	private static void require(GameTestHelper helper, boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
