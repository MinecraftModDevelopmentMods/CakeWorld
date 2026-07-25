package com.mcmoddev.cakeworld.gametest;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.CakeOvenBlock;
import com.mcmoddev.cakeworld.block.CandySproutBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.CookbookKioskBlock;
import com.mcmoddev.cakeworld.block.GummyBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.block.MarshmallowBlock;
import com.mcmoddev.cakeworld.cookbook.CookbookEvents;
import com.mcmoddev.cakeworld.cookbook.CookbookProgress;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;
import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.TrufflePig;
import com.mcmoddev.cakeworld.effect.FizzyFeetEffect;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.advancements.Advancement;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import com.mcmoddev.cakeworld.world.StarterPicnicFeature;

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

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public static void icingStacksAndMeltsAtTheVanillaLightThreshold(
			GameTestHelper helper) {
		BlockPos relativeIcingPos = new BlockPos(1, 1, 1);
		BlockPos absoluteIcingPos = helper.absolutePos(relativeIcingPos);
		IcingLayerBlock icing =
				(IcingLayerBlock) CakeWorldBlocks.ICING_LAYER.get();
		helper.setBlock(new BlockPos(1, 0, 1), Blocks.STONE);
		helper.setBlock(relativeIcingPos, icing.defaultBlockState());

		Player player = helper.makeMockPlayer();
		player.setPos(absoluteIcingPos.getX() + 0.5D,
				absoluteIcingPos.getY() + 1.0D,
				absoluteIcingPos.getZ() - 1.0D);
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldBlocks.ICING_LAYER.get()));
		BlockHitResult hit = new BlockHitResult(
				Vec3.atBottomCenterOf(absoluteIcingPos).add(0.0D, 1.0D, 0.0D),
				Direction.UP,
				absoluteIcingPos, false);
		InteractionResult stacked = icing.asItem().useOn(
				new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
		require(helper, stacked.consumesAction()
						&& helper.getBlockState(relativeIcingPos).getValue(
								BlockStateProperties.LAYERS) == 2,
				"Placing icing on icing did not accumulate a second layer");

		helper.setBlock(new BlockPos(2, 1, 1), Blocks.GLOWSTONE);
		helper.runAfterDelay(2, () -> {
			int blockLight = helper.getLevel().getBrightness(
					LightLayer.BLOCK, absoluteIcingPos);
			require(helper, blockLight > 11,
					"Icing melting fixture did not reach the vanilla light threshold");
			BlockState stackedIcing =
					helper.getBlockState(relativeIcingPos);
			icing.randomTick(stackedIcing, helper.getLevel(),
					absoluteIcingPos, helper.getLevel().random);
			require(helper, helper.getBlockState(relativeIcingPos).isAir(),
					"Bright icing did not inherit snow-layer melting");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void frozenLemonadePreservesMomentumWithoutRemovingControl(
			GameTestHelper helper) {
		helper.setBlock(new BlockPos(1, 0, 1),
				CakeWorldBlocks.FROZEN_LEMONADE.get());
		helper.setBlock(new BlockPos(3, 0, 1), Blocks.STONE);
		Pig lemonadeTester =
				helper.spawnWithNoFreeWill(EntityType.PIG, 1.5F, 1.0F, 1.5F);
		Pig stoneTester =
				helper.spawnWithNoFreeWill(EntityType.PIG, 3.5F, 1.0F, 1.5F);
		Vec3 initialMovement = new Vec3(0.2D, 0.0D, 0.0D);
		lemonadeTester.setOnGround(true);
		stoneTester.setOnGround(true);
		lemonadeTester.setDeltaMovement(initialMovement);
		stoneTester.setDeltaMovement(initialMovement);

		lemonadeTester.travel(Vec3.ZERO);
		stoneTester.travel(Vec3.ZERO);
		double lemonadeMomentum = lemonadeTester.getDeltaMovement().x;
		double stoneMomentum = stoneTester.getDeltaMovement().x;
		require(helper, lemonadeMomentum > stoneMomentum * 1.4D,
				"Frozen lemonade did not preserve recognisably more momentum than stone");
		require(helper, lemonadeMomentum > 0.0D
						&& lemonadeMomentum < initialMovement.x,
				"Frozen lemonade stopped movement or removed natural deceleration");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void marshmallowCancelsFallsAndCapsItsGentleBounce(
			GameTestHelper helper) {
		MarshmallowBlock marshmallow =
				(MarshmallowBlock) CakeWorldBlocks.MARSHMALLOW.get();
		BlockState state = marshmallow.defaultBlockState();
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig tester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		tester.setHealth(10.0F);
		marshmallow.fallOn(helper.getLevel(), state, testPos,
				tester, 30.0F);
		require(helper, Math.abs(tester.getHealth() - 10.0F) < 0.001F,
				"Marshmallow allowed health damage from a long fall");

		Vec3 falling = new Vec3(0.3D, -4.0D, -0.2D);
		tester.setDeltaMovement(falling);
		marshmallow.updateEntityAfterFallOn(helper.getLevel(), tester);
		Vec3 rebound = tester.getDeltaMovement();
		require(helper, close(rebound.x, falling.x)
						&& close(rebound.z, falling.z)
						&& close(rebound.y,
								MarshmallowBlock.MAXIMUM_BOUNCE),
				"Marshmallow lost steering or exceeded its gentle-bounce cap");

		Player crouching = helper.makeMockPlayer();
		crouching.setShiftKeyDown(true);
		crouching.setDeltaMovement(0.2D, -1.0D, 0.1D);
		marshmallow.updateEntityAfterFallOn(helper.getLevel(), crouching);
		require(helper, close(crouching.getDeltaMovement().y, 0.0D),
				"Crouching did not suppress the marshmallow bounce");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "marshmallow"))
						.isPresent(),
				"Marshmallow is not obtainable through its starter recipe");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gummyBlockProvidesAHighButBoundedElasticBounce(
			GameTestHelper helper) {
		GummyBlock gummy = (GummyBlock) CakeWorldBlocks.GUMMY_BLOCK.get();
		BlockState state = gummy.defaultBlockState();
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig tester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		tester.setHealth(10.0F);
		gummy.fallOn(helper.getLevel(), state, testPos, tester, 30.0F);
		require(helper, Math.abs(tester.getHealth() - 10.0F) < 0.001F,
				"Gummy allowed health damage from a long fall");

		Vec3 ordinaryFall = new Vec3(0.3D, -1.0D, -0.2D);
		tester.setDeltaMovement(ordinaryFall);
		gummy.updateEntityAfterFallOn(helper.getLevel(), tester);
		Vec3 ordinaryRebound = tester.getDeltaMovement();
		require(helper, close(ordinaryRebound.x, ordinaryFall.x)
						&& close(ordinaryRebound.z, ordinaryFall.z)
						&& close(ordinaryRebound.y,
								GummyBlock.BOUNCE_MULTIPLIER)
						&& ordinaryRebound.y
								> MarshmallowBlock.BOUNCE_MULTIPLIER,
				"Gummy did not provide a stronger controlled rebound than marshmallow");

		tester.setDeltaMovement(0.1D, -4.0D, 0.2D);
		gummy.updateEntityAfterFallOn(helper.getLevel(), tester);
		require(helper, close(tester.getDeltaMovement().y,
						GummyBlock.MAXIMUM_BOUNCE),
				"Gummy did not cap an extreme rebound");

		Player crouching = helper.makeMockPlayer();
		crouching.setShiftKeyDown(true);
		crouching.setDeltaMovement(0.2D, -1.0D, 0.1D);
		gummy.updateEntityAfterFallOn(helper.getLevel(), crouching);
		require(helper, close(crouching.getDeltaMovement().y, 0.0D),
				"Crouching did not suppress the gummy bounce");
		ShapedRecipe recipe = (ShapedRecipe) helper.getLevel()
				.getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "gummy_block"))
				.orElseThrow();
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 3, 3);
		for (int slot = 0; slot < 9; slot++) {
			ingredients.setItem(slot, new ItemStack(
					slot == 4 ? CakeWorldItems.LEMONADE_BOTTLE.get()
							: Items.SUGAR));
		}
		ItemStack gummyResult = recipe.assemble(ingredients);
		ItemStack returnedBottle = recipe.getRemainingItems(ingredients).get(4);
		require(helper, recipe.matches(ingredients, helper.getLevel())
						&& gummyResult.is(CakeWorldBlocks.GUMMY_BLOCK.get().asItem())
						&& returnedBottle.is(Items.GLASS_BOTTLE),
				"Gummy recipe did not assemble the block and return its bottle");
		Map<String, BlockState> flavours = Map.of(
				"gummy_block",
				CakeWorldBlocks.GUMMY_BLOCK.get().defaultBlockState(),
				"raspberry_gummy_block",
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get().defaultBlockState(),
				"blueberry_gummy_block",
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get().defaultBlockState(),
				"grape_gummy_block",
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get().defaultBlockState());
		for (Map.Entry<String, BlockState> flavour : flavours.entrySet()) {
			require(helper, flavour.getValue().getBlock() instanceof GummyBlock
							&& helper.getLevel().getRecipeManager().byKey(
									new ResourceLocation(CakeWorld.MODID,
											flavour.getKey())).isPresent(),
					"Missing elastic block or recipe for gummy flavour "
							+ flavour.getKey());
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void waferBlocksAreCheapSolidAndDeliberatelyFragile(
			GameTestHelper helper) {
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK.get().defaultBlockState();
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		require(helper, !wafer.requiresCorrectToolForDrops()
						&& close(wafer.getDestroySpeed(
								helper.getLevel(), testPos), 0.2D)
						&& close(CakeWorldBlocks.WAFER_BLOCK.get()
								.getExplosionResistance(), 0.2D),
				"Wafer Block lost its quick-break fragile building contract");
		require(helper, !wafer.getCollisionShape(
						helper.getLevel(), testPos).isEmpty(),
				"Wafer Block cannot support a temporary bridge");

		ShapedRecipe recipe = (ShapedRecipe) helper.getLevel()
				.getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "wafer_block"))
				.orElseThrow();
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 2, 2);
		for (int slot = 0; slot < 4; slot++) {
			ingredients.setItem(slot,
					new ItemStack(CakeWorldItems.SIMPLE_BISCUIT.get()));
		}
		ItemStack result = recipe.assemble(ingredients);
		require(helper, recipe.matches(ingredients, helper.getLevel())
						&& result.is(CakeWorldBlocks.WAFER_BLOCK.get().asItem())
						&& result.getCount() == 8,
				"Wafer Block recipe is not a high-yield bridge supply");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void candySproutsCanBePickedWithoutDestroyingThePlant(
			GameTestHelper helper) {
		CandySproutBlock sprout =
				(CandySproutBlock) CakeWorldBlocks.CANDY_SPROUT.get();
		BlockPos relativePos = new BlockPos(1, 1, 1);
		BlockPos absolutePos = helper.absolutePos(relativePos);
		helper.setBlock(new BlockPos(1, 0, 1), Blocks.FARMLAND);
		helper.setBlock(relativePos,
				sprout.getStateForAge(sprout.getMaxAge()));
		Player player = helper.makeMockPlayer();
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absolutePos), Direction.UP,
				absolutePos, false);
		InteractionResult picked = sprout.use(
				helper.getBlockState(relativePos), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND, hit);

		int sweets = helper.getLevel().getEntitiesOfClass(
				ItemEntity.class, new AABB(absolutePos).inflate(1.0D),
				entity -> entity.getItem().is(
						CakeWorldItems.BOILED_SWEET.get()))
				.stream().mapToInt(entity -> entity.getItem().getCount()).sum();
		require(helper, picked.consumesAction()
						&& helper.getBlockState(relativePos).is(
								CakeWorldBlocks.CANDY_SPROUT.get())
						&& helper.getBlockState(relativePos).getValue(
								BlockStateProperties.AGE_7)
								== CandySproutBlock.PICKED_AGE
						&& sweets >= 2 && sweets <= 3,
				"Picking a ripe Candy Sprout did not leave regrowth and sweets");
		require(helper, new ItemStack(CakeWorldItems.SPRINKLE_SEEDS.get())
						.getItem() instanceof ItemNameBlockItem,
				"Sprinkle Seeds are not plantable as the Candy Sprout crop");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sugarRushIsBeneficialTemporaryAndHasNoCrash(
			GameTestHelper helper) {
		var sugarRush = CakeWorldEffects.SUGAR_RUSH.get();
		require(helper, sugarRush.getCategory()
						== MobEffectCategory.BENEFICIAL,
				"Sugar Rush is not categorised as a beneficial effect");
		Pig tester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		double ordinarySpeed =
				tester.getAttributeValue(Attributes.MOVEMENT_SPEED);
		tester.addEffect(new MobEffectInstance(sugarRush, 200));
		double rushedSpeed =
				tester.getAttributeValue(Attributes.MOVEMENT_SPEED);
		tester.removeEffect(sugarRush);
		double restoredSpeed =
				tester.getAttributeValue(Attributes.MOVEMENT_SPEED);
		require(helper, rushedSpeed > ordinarySpeed * 1.14D
						&& close(restoredSpeed, ordinarySpeed),
				"Sugar Rush did not boost speed cleanly without a crash state");

		FoodProperties fizz =
				CakeWorldItems.SHERBET_FIZZ.get().getFoodProperties();
		require(helper, fizz != null && fizz.getEffects().stream()
						.anyMatch(effect -> {
							MobEffectInstance instance =
									effect.getFirst();
							return instance.getEffect() == sugarRush
									&& instance.getDuration() == 200
									&& close(effect.getSecond(), 1.0D);
						}),
				"Sherbet Fizz does not always grant the intended Sugar Rush");

		ShapelessRecipe recipe = (ShapelessRecipe) helper.getLevel()
				.getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "sherbet_fizz"))
				.orElseThrow();
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 2, 2);
		ingredients.setItem(0,
				new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
		ingredients.setItem(1, new ItemStack(Items.SUGAR));
		ingredients.setItem(2,
				new ItemStack(CakeWorldItems.LEMONADE_BOTTLE.get()));
		ItemStack result = recipe.assemble(ingredients);
		require(helper, recipe.matches(ingredients, helper.getLevel())
						&& result.is(CakeWorldItems.SHERBET_FIZZ.get())
						&& result.getCount() == 2
						&& recipe.getRemainingItems(ingredients).get(2)
								.is(Items.GLASS_BOTTLE),
				"Sherbet Fizz recipe did not yield two and return its bottle");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void comfortMintAndFizzStayGentleAndDistinct(
			GameTestHelper helper) {
		var comfort = CakeWorldEffects.COCOA_COMFORT.get();
		var mint = CakeWorldEffects.MINTY_FRESH.get();
		var fizz = CakeWorldEffects.FIZZY_FEET.get();
		require(helper, comfort.getCategory() == MobEffectCategory.BENEFICIAL
						&& mint.getCategory() == MobEffectCategory.BENEFICIAL
						&& fizz.getCategory() == MobEffectCategory.BENEFICIAL,
				"A playful food effect was not categorised as beneficial");

		Pig comfortTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		double ordinaryKnockback = comfortTester.getAttributeValue(
				Attributes.KNOCKBACK_RESISTANCE);
		double ordinaryArmour =
				comfortTester.getAttributeValue(Attributes.ARMOR);
		comfortTester.addEffect(new MobEffectInstance(comfort, 300));
		require(helper, comfortTester.getAttributeValue(
						Attributes.KNOCKBACK_RESISTANCE)
								>= ordinaryKnockback + 0.25D
						&& comfortTester.getAttributeValue(Attributes.ARMOR)
								>= ordinaryArmour + 2.0D,
				"Cocoa Comfort did not provide its modest defensive cushion");

		Pig mintTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 2.5F, 1.0F, 1.5F);
		mintTester.setSecondsOnFire(5);
		mint.applyEffectTick(mintTester, 0);
		require(helper, !mintTester.isOnFire(),
				"Minty Fresh did not extinguish its affected entity");

		Pig fizzTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 3.5F, 1.0F, 1.5F);
		Vec3 falling = new Vec3(0.2D, -1.0D, -0.1D);
		fizzTester.setDeltaMovement(falling);
		fizz.applyEffectTick(fizzTester, 0);
		Vec3 softened = fizzTester.getDeltaMovement();
		require(helper, close(softened.x, falling.x)
						&& close(softened.z, falling.z)
						&& close(softened.y,
								-FizzyFeetEffect.DESCENT_MULTIPLIER)
						&& softened.y < 0.0D,
				"Fizzy Feet lost steering or forced an upward camera bounce");

		require(helper, grantsEffect(CakeWorldItems.COMFORT_COCOA.get(),
							comfort, 300)
						&& grantsEffect(CakeWorldItems.MINT_WAFER.get(),
								mint, 200)
						&& grantsEffect(CakeWorldItems.FIZZY_POPPERS.get(),
								fizz, 200),
				"A named food does not guarantee its intended effect");
		for (String recipe : Set.of("comfort_cocoa", "mint_wafer",
				"fizzy_poppers")) {
			require(helper, helper.getLevel().getRecipeManager().byKey(
							new ResourceLocation(CakeWorld.MODID, recipe))
							.isPresent(),
					"Missing playable recipe " + recipe);
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void rollingPinPreparesReusableOvenReadyDough(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var rollingRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID,
						"rolled_biscuit_dough"));
		var bakingRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID,
						"simple_biscuit_from_rolled_dough"));
		require(helper, rollingRecipe.orElse(null) instanceof ShapelessRecipe
						&& bakingRecipe.orElse(null) instanceof SmeltingRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"rolling_pin")).isPresent(),
				"Rolling Pin is missing its acquisition or standard recipe chain");

		ItemStack rollingPin =
				new ItemStack(CakeWorldItems.ROLLING_PIN.get());
		require(helper, rollingPin.hasContainerItem()
						&& rollingPin.getContainerItem().is(
								CakeWorldItems.ROLLING_PIN.get()),
				"Rolling Pin is not a reusable crafting tool");
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 2, 2);
		ingredients.setItem(0, rollingPin);
		ingredients.setItem(3,
				new ItemStack(CakeWorldItems.SPONGE_BATTER.get()));
		ShapelessRecipe rolling =
				(ShapelessRecipe) rollingRecipe.orElseThrow();
		ItemStack dough = rolling.assemble(ingredients);
		require(helper, rolling.matches(ingredients, helper.getLevel())
						&& dough.is(CakeWorldItems.ROLLED_BISCUIT_DOUGH.get())
						&& dough.getCount() == 2
						&& rolling.getRemainingItems(ingredients).get(0)
								.is(CakeWorldItems.ROLLING_PIN.get()),
				"Rolling did not create two dough portions and return the pin");

		TagKey<Item> ovenBatters = TagKey.create(Registry.ITEM_REGISTRY,
				new ResourceLocation("forge", "oven_batters"));
		SmeltingRecipe baking =
				(SmeltingRecipe) bakingRecipe.orElseThrow();
		SimpleContainer ovenInput = new SimpleContainer(
				new ItemStack(CakeWorldItems.ROLLED_BISCUIT_DOUGH.get()));
		require(helper, ovenInput.getItem(0).is(ovenBatters)
						&& baking.matches(ovenInput, helper.getLevel())
						&& baking.assemble(ovenInput).is(
								CakeWorldItems.SIMPLE_BISCUIT.get()),
				"Rolled dough is not compatible with the standard food-only oven");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void candyCanePillarsKeepAllThreeStructuralAxes(
			GameTestHelper helper) {
		RotatedPillarBlock pillar =
				(RotatedPillarBlock) CakeWorldBlocks.CANDY_CANE_PILLAR.get();
		BlockState vertical = pillar.defaultBlockState();
		BlockState eastWest = vertical.setValue(
				RotatedPillarBlock.AXIS, Direction.Axis.X);
		BlockState northSouth = eastWest.rotate(
				helper.getLevel(), helper.absolutePos(new BlockPos(1, 1, 1)),
				Rotation.CLOCKWISE_90);
		require(helper, vertical.getValue(RotatedPillarBlock.AXIS)
						== Direction.Axis.Y,
				"Candy-Cane Pillar does not default to a vertical axis");
		require(helper, eastWest.getValue(RotatedPillarBlock.AXIS)
						== Direction.Axis.X
						&& northSouth.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.Z,
				"Candy-Cane Pillar did not rotate between horizontal axes");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"candy_cane_pillar")).isPresent(),
				"Candy-Cane Pillar is not obtainable through its recipe");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gingerbreadMasonryRequiresPreparedMortarAndTools(
			GameTestHelper helper) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get().defaultBlockState();
		require(helper, bricks.requiresCorrectToolForDrops()
						&& bricks.is(BlockTags.MINEABLE_WITH_PICKAXE),
				"Gingerbread Bricks lost their hard structural mining contract");
		require(helper, !new ItemStack(CakeWorldItems.FROSTING_MORTAR.get())
						.isEdible(),
				"Structural Frosting Mortar became a raw snack");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"frosting_mortar")).isPresent()
						&& helper.getLevel().getRecipeManager().byKey(
								new ResourceLocation(CakeWorld.MODID,
										"gingerbread_bricks")).isPresent(),
				"Gingerbread masonry is missing its prepared mortar recipe chain");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void stickyFluidsSlowWithoutTrapping(GameTestHelper helper) {
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		var caramelEntity = EntityType.ARMOR_STAND.create(helper.getLevel());
		var treacleEntity = EntityType.ARMOR_STAND.create(helper.getLevel());
		require(helper, caramelEntity != null && treacleEntity != null,
				"Could not create sticky-fluid test entities");

		Vec3 fallingMovement = new Vec3(1.0D, -0.8D, -1.0D);
		caramelEntity.setDeltaMovement(fallingMovement);
		CakeWorldFluids.CARAMEL_BLOCK.get().entityInside(
				CakeWorldFluids.CARAMEL_BLOCK.get().defaultBlockState(),
				helper.getLevel(), testPos, caramelEntity);
		Vec3 caramelMovement = caramelEntity.getDeltaMovement();
		require(helper, close(caramelMovement.x, 0.35D)
						&& close(caramelMovement.y, -0.4D)
						&& close(caramelMovement.z, -0.35D),
				"Caramel did not apply its strong horizontal/downward drag");

		treacleEntity.setDeltaMovement(fallingMovement);
		CakeWorldFluids.SYRUP_BLOCK.get().entityInside(
				CakeWorldFluids.SYRUP_BLOCK.get().defaultBlockState(),
				helper.getLevel(), testPos, treacleEntity);
		Vec3 treacleMovement = treacleEntity.getDeltaMovement();
		require(helper, close(treacleMovement.x, 0.55D)
						&& close(treacleMovement.y, -0.56D)
						&& close(treacleMovement.z, -0.55D),
				"Treacle Syrup did not apply its gentler drag");

		Vec3 escapingMovement = new Vec3(0.6D, 0.42D, 0.4D);
		caramelEntity.setDeltaMovement(escapingMovement);
		CakeWorldFluids.CARAMEL_BLOCK.get().entityInside(
				CakeWorldFluids.CARAMEL_BLOCK.get().defaultBlockState(),
				helper.getLevel(), testPos, caramelEntity);
		Vec3 escapingCaramel = caramelEntity.getDeltaMovement();
		require(helper, close(escapingCaramel.y, escapingMovement.y)
						&& escapingCaramel.x > 0.0D
						&& escapingCaramel.z > 0.0D,
				"Caramel removed upward escape or all player control");
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

	@GameTest(template = EMPTY)
	public static void starterKitchenUsesReusableMixingAndStandardCooking(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var batterRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID, "sponge_batter"));
		var ovenRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID,
						"warm_sponge_cake_from_oven"));
		require(helper, batterRecipe.orElse(null) instanceof ShapelessRecipe,
				"Sponge batter is not a data-driven shapeless recipe");
		require(helper, ovenRecipe.orElse(null) instanceof SmeltingRecipe,
				"Warm Sponge Cake is not a standard smelting recipe");

		ItemStack mixingBowl =
				new ItemStack(CakeWorldBlocks.MIXING_BOWL.get());
		require(helper, mixingBowl.hasContainerItem()
						&& mixingBowl.getContainerItem().is(
								CakeWorldBlocks.MIXING_BOWL.get().asItem()),
				"The Mixing Bowl is not returned after shapeless preparation");
		AbstractContainerMenu testMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer craftingGrid =
				new CraftingContainer(testMenu, 2, 2);
		craftingGrid.setItem(0, mixingBowl);
		craftingGrid.setItem(1,
				new ItemStack(CakeWorldItems.ICING_SPOONFUL.get()));
		craftingGrid.setItem(3,
				new ItemStack(CakeWorldItems.CHOCOLATE_SPONGE_SLICE.get()));
		ShapelessRecipe shapelessBatter =
				(ShapelessRecipe) batterRecipe.orElseThrow();
		require(helper, shapelessBatter.matches(
						craftingGrid, helper.getLevel())
						&& shapelessBatter.getRemainingItems(craftingGrid)
								.get(0).is(
										CakeWorldBlocks.MIXING_BOWL.get()
												.asItem()),
				"Mixed ingredient order did not match or return the Mixing Bowl");
		TagKey<Item> ovenBatters = TagKey.create(Registry.ITEM_REGISTRY,
				new ResourceLocation("forge", "oven_batters"));
		require(helper, new ItemStack(CakeWorldItems.SPONGE_BATTER.get())
						.is(ovenBatters),
				"Sponge Batter is missing the public oven-batter tag");

		BlockPos relativeOvenPos = new BlockPos(1, 1, 1);
		BlockPos absoluteOvenPos = helper.absolutePos(relativeOvenPos);
		CakeOvenBlock oven = (CakeOvenBlock) CakeWorldBlocks.OVEN.get();
		helper.setBlock(relativeOvenPos, oven.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems.SPONGE_BATTER.get()));
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteOvenPos), Direction.UP,
				absoluteOvenPos, false);
		require(helper, oven.use(oven.defaultBlockState(),
						helper.getLevel(), absoluteOvenPos, player,
						InteractionHand.MAIN_HAND, hit)
						== InteractionResult.PASS
						&& player.getMainHandItem().is(
								CakeWorldItems.SPONGE_BATTER.get()),
				"The Oven cooked without fuel or consumed its input");

		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		InteractionResult cooked = oven.use(oven.defaultBlockState(),
				helper.getLevel(), absoluteOvenPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, cooked.consumesAction(),
				"The fuelled Oven rejected its standard food recipe: "
						+ cooked);
		require(helper, player.getMainHandItem().is(
						CakeWorldItems.WARM_SPONGE_CAKE.get()),
				"The Oven did not return Warm Sponge Cake; main hand is "
						+ player.getMainHandItem());
		require(helper, player.getOffhandItem().isEmpty(),
				"The Oven did not consume one standard fuel; offhand is "
						+ player.getOffhandItem());

		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldBlocks.COCOA_COAL.get()));
		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		require(helper, oven.use(oven.defaultBlockState(),
						helper.getLevel(), absoluteOvenPos, player,
						InteractionHand.MAIN_HAND, hit)
						== InteractionResult.PASS
						&& player.getMainHandItem().is(
								CakeWorldBlocks.COCOA_COAL.get().asItem())
						&& player.getOffhandItem().is(Items.COAL),
				"The starter Oven accepted a non-food resource recipe");
		require(helper, CakeWorldItems.WARM_SPONGE_CAKE.get()
						.getFoodProperties().getNutrition()
						> CakeWorldItems.CHOCOLATE_SPONGE_SLICE.get()
								.getFoodProperties().getNutrition(),
				"Cooked Sponge Cake is not a worthwhile prepared food");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookProgressIsPlayerOwnedAndHasSixEventTypes(GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.getMinecraft(helper.getLevel());
		for (DiscoveryType type : DiscoveryType.values()) {
			ResourceLocation page = new ResourceLocation(CakeWorld.MODID,
					"gametest/" + type.name().toLowerCase());
			require(helper, CookbookProgress.discover(player, type, page),
					type + " did not add its first page");
			require(helper, !CookbookProgress.discover(player, type, page),
					type + " accepted a duplicate page");
		}

		CookbookProgress.grantStarterBook(player);
		player.getInventory().clearContent();
		CompoundTag snapshot = CookbookProgress.snapshot(player);
		Map<DiscoveryType, Set<ResourceLocation>> discoveries =
				CookbookProgress.read(snapshot);
		require(helper, discoveries.size() == DiscoveryType.values().length,
				"The Cookbook does not expose all six discovery tabs");
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, discoveries.get(type).size() == 1,
					type + " progress was lost with the physical book");
		}
		require(helper, CookbookProgress.recoverBook(player),
				"The kiosk recovery path did not replace a lost Cookbook");
		require(helper, player.getInventory().contains(
				CakeWorldItems.EXPLORERS_COOKBOOK.get().getDefaultInstance()),
				"The recovered Cookbook did not reach the player");

		ServerPlayer replacement = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.fromString("1978cafe-cafe-4bad-babe-1978cafe1978"),
						"CakeWorldRespawnTest"));
		Map<DiscoveryType, Set<ResourceLocation>> isolated =
				CookbookProgress.read(CookbookProgress.snapshot(replacement));
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, isolated.get(type).isEmpty(),
					type + " leaked between players");
		}
		CookbookProgress.copyForRespawn(player, replacement);
		Map<DiscoveryType, Set<ResourceLocation>> copied =
				CookbookProgress.read(CookbookProgress.snapshot(replacement));
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, copied.get(type).size() == 1,
					type + " did not survive the respawn copy");
		}

		String[] expectedRecipes = {
			"chocolate_sponge_from_slices",
			"chocolate_sponge_slices",
			"chocolate_sponge_sundae",
			"cookbook_kiosk",
			"explorers_cookbook",
			"icing_from_spoonfuls",
			"icing_spoonful",
			"lemonade_bottles",
			"simple_biscuit"
		};
		for (String recipe : expectedRecipes) {
			require(helper, helper.getLevel().getRecipeManager().byKey(
					new ResourceLocation(CakeWorld.MODID, recipe)).isPresent(),
					"Recipe did not load: " + recipe);
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookEventHooksUnlockAllSixDiscoveryPaths(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(
						UUID.fromString(
								"1978cafe-cafe-4bad-babe-1978cafe1979"),
						"CakeWorldEventHookTest"));
		BlockPos relativePos = new BlockPos(1, 1, 1);
		BlockPos absolutePos = helper.absolutePos(relativePos);
		player.setPos(absolutePos.getX() + 0.5D,
				absolutePos.getY(), absolutePos.getZ() + 0.5D);
		player.tickCount = 40;

		ResourceLocation visitedBiome = player.level
				.getBiome(player.blockPosition()).unwrapKey().orElseThrow()
				.location();
		require(helper, CakeWorld.MODID.equals(visitedBiome.getNamespace()),
				"Event-hook fixture is not inside a CakeWorld biome");
		CookbookEvents.onPlayerTick(new TickEvent.PlayerTickEvent(
				TickEvent.Phase.END, player));
		CookbookEvents.onFinishFood(
				new LivingEntityUseItemEvent.Finish(player,
						new ItemStack(CakeWorldItems.WARM_SPONGE_CAKE.get()),
						0, ItemStack.EMPTY));
		CookbookEvents.onCraft(new PlayerEvent.ItemCraftedEvent(player,
				new ItemStack(CakeWorldItems.SPONGE_BATTER.get()),
				new SimpleContainer(0)));
		CookbookEvents.onBreak(new BlockEvent.BreakEvent(helper.getLevel(),
				absolutePos,
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(),
				player));
		CocoaCow cow =
				CakeWorldEntities.COCOA_COW.get().create(helper.getLevel());
		require(helper, cow != null,
				"Could not create the meeting-discovery fixture");
		CookbookEvents.onTrack(
				new PlayerEvent.StartTracking(player, cow));

		CookbookKioskBlock kiosk =
				(CookbookKioskBlock) CakeWorldBlocks.COOKBOOK_KIOSK.get();
		helper.setBlock(relativePos, kiosk.defaultBlockState());
		kiosk.use(kiosk.defaultBlockState(), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(absolutePos),
						Direction.UP, absolutePos, false));

		Map<DiscoveryType, Set<ResourceLocation>> discoveries =
				CookbookProgress.read(CookbookProgress.snapshot(player));
		require(helper, discoveries.get(DiscoveryType.VISITING)
						.contains(visitedBiome),
				"Visiting hook did not record the current CakeWorld biome"
						+ " tick=" + player.tickCount
						+ " pos=" + player.blockPosition()
						+ " biome=" + visitedBiome
						+ " visits="
						+ discoveries.get(DiscoveryType.VISITING));
		require(helper, discoveries.get(DiscoveryType.TASTING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"warm_sponge_cake")),
				"Tasting hook did not record the eaten CakeWorld food");
		require(helper, discoveries.get(DiscoveryType.CRAFTING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"sponge_batter")),
				"Crafting hook did not record the crafted CakeWorld item");
		require(helper, discoveries.get(DiscoveryType.MINING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"chocolate_sponge")),
				"Mining hook did not record the broken CakeWorld block");
		require(helper, discoveries.get(DiscoveryType.MEETING).contains(
						new ResourceLocation(CakeWorld.MODID, "cocoa_cow")),
				"Meeting hook did not record the tracked CakeWorld entity");
		require(helper, discoveries.get(DiscoveryType.FINDING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"cookbook_kiosk")),
				"Finding hook did not record the used landmark");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void firstCreaturesReplaceRolesAndRespectDifficulty(GameTestHelper helper) {
		CocoaCow cocoaCow = CakeWorldEntities.COCOA_COW.get().create(helper.getLevel());
		MallowChick mallowChick =
				CakeWorldEntities.MALLOW_CHICK.get().create(helper.getLevel());
		TrufflePig trufflePig =
				CakeWorldEntities.TRUFFLE_PIG.get().create(helper.getLevel());
		CandyflossSheep candyflossSheep =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get().create(helper.getLevel());
		require(helper, cocoaCow != null && cocoaCow.getBreedOffspring(
				helper.getLevel(), cocoaCow) instanceof CocoaCow,
				"Cocoa Cows did not breed their own entity type");
		require(helper, mallowChick != null && mallowChick.getBreedOffspring(
				helper.getLevel(), mallowChick) instanceof MallowChick,
				"Mallow Chicks did not breed their own entity type");
		require(helper, trufflePig != null && trufflePig.getBreedOffspring(
				helper.getLevel(), trufflePig) instanceof TrufflePig,
				"Truffle Pigs did not breed their own entity type");
		require(helper, candyflossSheep != null && candyflossSheep.getBreedOffspring(
				helper.getLevel(), candyflossSheep) instanceof CandyflossSheep,
				"Candyfloss Sheep did not breed their own entity type");

		StaleCrumbler crumbler =
				CakeWorldEntities.STALE_CRUMBLER.get().create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, crumbler != null && target != null,
				"Could not create the difficulty-contract test entities");
		target.setHealth(10.0F);
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				helper.getLevel().getServer().setDifficulty(safeDifficulty, true);
				require(helper, crumbler.doHurtTarget(target),
						safeDifficulty + " Stale Crumbler attack did not register");
				require(helper, Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& target.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(net.minecraft.world.effect.MobEffects.SLOW_FALLING),
						safeDifficulty
								+ " attack caused damage or lacked safe inconvenience/rescue");
			}

			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(Difficulty.HARD, true);
			require(helper, crumbler.doHurtTarget(target) && target.getHealth() < 10.0F,
					"Hard Stale Crumbler attack did not cause real damage");

			helper.getLevel().getServer().setDifficulty(Difficulty.PEACEFUL, true);
			StaleCrumbler peacefulCrumbler =
					CakeWorldEntities.STALE_CRUMBLER.get().create(helper.getLevel());
			require(helper, peacefulCrumbler != null,
					"Could not create the Peaceful behavior test creature");
			peacefulCrumbler.checkDespawn();
			require(helper, peacefulCrumbler.isRemoved(),
					"Stale Crumbler did not retain Peaceful despawning");
		} finally {
			helper.getLevel().getServer().setDifficulty(originalDifficulty, true);
		}

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		Biome cookieForest = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.COOKIE_FOREST.getId());
		require(helper, candyPlains != null && cookieForest != null,
				"Could not inspect CakeWorld biome spawn roles");
		requireSpawnReplacement(helper, candyPlains, EntityType.COW,
				CakeWorldEntities.COCOA_COW.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, candyPlains, EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, candyPlains, EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, cookieForest, EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, candyPlains, EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER.get(), MobCategory.MONSTER);

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString("1978feed-feed-4bad-babe-1978feed1978"),
						"CakeWorldRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.COCOA_COW.get());
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.MALLOW_CHICK.get());
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.TRUFFLE_PIG.get());
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		VanillaRoleAdvancements.creditKilledZombieRole(advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:cow");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:chicken");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:pig");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:sheep");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs", "minecraft:zombie");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void starterPicnicBuildsAReadableCookbookLandmark(
			GameTestHelper helper) {
		BlockPos relativeCentre = new BlockPos(5, 20, 5);
		for (int x = 1; x <= 9; x++) {
			for (int z = 1; z <= 9; z++) {
				helper.setBlock(new BlockPos(x, 19, z), Blocks.STONE);
				helper.setBlock(new BlockPos(x, 20, z),
						CakeWorldBlocks.CHOCOLATE_SPONGE.get());
				for (int y = 21; y <= 25; y++) {
					helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
				}
			}
		}
		BlockPos absoluteCentre = helper.absolutePos(relativeCentre);
		require(helper, StarterPicnicFeature.buildAt(helper.getLevel(),
						new Random(1978L), absoluteCentre),
				"The First Bite picnic refused a safe flat site");
		helper.assertBlockPresent(CakeWorldBlocks.COOKBOOK_KIOSK.get(),
				relativeCentre.above());

		int spongeSeatsAndBorder = 0;
		int icingRoof = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 3; y++) {
					BlockState state = helper.getLevel().getBlockState(
							absoluteCentre.offset(x, y, z));
					if (state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())) {
						spongeSeatsAndBorder++;
					}
					if (state.is(CakeWorldBlocks.ICING.get())) {
						icingRoof++;
					}
				}
			}
		}
		// The biscuit approach deliberately replaces one of the 32 border
		// blocks; the remaining 31 plus four seats total 35 sponge blocks.
		require(helper, spongeSeatsAndBorder == 35 && icingRoof == 18,
				"The picnic lost its cushioned border, seats, or two icing roofs: "
						+ spongeSeatsAndBorder + " sponge, " + icingRoof + " icing");

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect the picnic's target biome");
		boolean installed = false;
		for (Holder<PlacedFeature> feature : candyPlains.getGenerationSettings()
				.features().get(GenerationStep.Decoration.SURFACE_STRUCTURES.ordinal())) {
			if (feature.unwrapKey().map(key -> key.location().equals(
					StarterPicnicFeature.ID)).orElse(false)) {
				installed = true;
				break;
			}
		}
		require(helper, installed,
				"The First Bite picnic was not installed in Candy Plains worldgen");
		helper.succeed();
	}

	private static FoodProperties requireFood(GameTestHelper helper, ItemStack stack) {
		FoodProperties food = stack.getItem().getFoodProperties();
		require(helper, food != null, stack.getItem() + " has no food properties");
		return food;
	}

	private static void requireSpawnReplacement(GameTestHelper helper, Biome biome,
			EntityType<?> vanilla, EntityType<?> replacement, MobCategory category) {
		boolean foundVanilla = false;
		boolean foundReplacement = false;
		for (MobSpawnSettings.SpawnerData spawn :
				biome.getMobSettings().getMobs(category).unwrap()) {
			foundVanilla |= spawn.type == vanilla;
			foundReplacement |= spawn.type == replacement;
		}
		require(helper, !foundVanilla && foundReplacement,
				"Biome did not replace " + Registry.ENTITY_TYPE.getKey(vanilla)
						+ " with " + Registry.ENTITY_TYPE.getKey(replacement));
	}

	private static void requireCriterion(GameTestHelper helper, ServerPlayer player,
			String advancementId, String criterion) {
		Advancement advancement = player.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(advancementId));
		require(helper, advancement != null
						&& player.getAdvancements().getOrStartProgress(advancement)
								.getCriterion(criterion).isDone(),
				"Vanilla role criterion was not credited: " + criterion);
	}

	private static boolean close(double actual, double expected) {
		return Math.abs(actual - expected) < 0.000001D;
	}

	private static boolean grantsEffect(Item item,
			net.minecraft.world.effect.MobEffect effect, int duration) {
		FoodProperties food = item.getFoodProperties();
		return food != null && food.getEffects().stream().anyMatch(entry ->
				entry.getFirst().getEffect() == effect
						&& entry.getFirst().getDuration() == duration
						&& close(entry.getSecond(), 1.0D));
	}

	private static void require(GameTestHelper helper, boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
