package com.mcmoddev.cakeworld.gametest;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.CakeOvenBlock;
import com.mcmoddev.cakeworld.block.CandyCookerBlock;
import com.mcmoddev.cakeworld.block.CandySproutBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.CookbookKioskBlock;
import com.mcmoddev.cakeworld.block.CookbookLibraryBlock;
import com.mcmoddev.cakeworld.block.CoolingRackBlock;
import com.mcmoddev.cakeworld.block.GummyBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.block.MarshmallowBlock;
import com.mcmoddev.cakeworld.block.SodaFountainBlock;
import com.mcmoddev.cakeworld.block.WaferWindmillBlock;
import com.mcmoddev.cakeworld.cookbook.CookbookEvents;
import com.mcmoddev.cakeworld.cookbook.CookbookHints;
import com.mcmoddev.cakeworld.cookbook.CookbookLayout;
import com.mcmoddev.cakeworld.cookbook.CookbookProgress;
import com.mcmoddev.cakeworld.cookbook.CookbookSummary;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;
import com.mcmoddev.cakeworld.cookbook.SharedCookbookLibrary;
import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.BonbonBat;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.CinnamonPuffProjectile;
import com.mcmoddev.cakeworld.entity.CinnamonSpark;
import com.mcmoddev.cakeworld.entity.Jellylotl;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.entity.DeepLiquoriceWeaver;
import com.mcmoddev.cakeworld.entity.DoughDonkey;
import com.mcmoddev.cakeworld.entity.GrandGumballGuardian;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.PeppermintFox;
import com.mcmoddev.cakeworld.entity.PopRockPopper;
import com.mcmoddev.cakeworld.entity.SodaCod;
import com.mcmoddev.cakeworld.entity.SodaDolphin;
import com.mcmoddev.cakeworld.entity.SoggyBiscuit;
import com.mcmoddev.cakeworld.entity.SoggyTridentProjectile;
import com.mcmoddev.cakeworld.entity.SourSorcerer;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.SugarBee;
import com.mcmoddev.cakeworld.entity.SugarMite;
import com.mcmoddev.cakeworld.entity.TaffyTallwalker;
import com.mcmoddev.cakeworld.entity.TrufflePig;
import com.mcmoddev.cakeworld.effect.FizzyFeetEffect;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.item.JellylotlBucketItem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.GameRules;
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
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import com.mcmoddev.cakeworld.world.StarterPicnicFeature;
import com.mcmoddev.cakeworld.world.CakeWorldDrownedReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldElderGuardianReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldEndermiteReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldEvokerReplacement;

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
	public static void pipingBagDecoratesCakeAndReturnsForReuse(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var decoratingRecipe = recipes.byKey(new ResourceLocation(
				CakeWorld.MODID, "piped_celebration_cake"));
		require(helper,
				decoratingRecipe.orElse(null) instanceof ShapelessRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"piping_bag")).orElse(null)
								instanceof ShapedRecipe,
				"Piping Bag is missing its acquisition or decoration recipe");

		ItemStack pipingBag =
				new ItemStack(CakeWorldItems.PIPING_BAG.get());
		require(helper, pipingBag.hasContainerItem()
						&& pipingBag.getContainerItem().is(
								CakeWorldItems.PIPING_BAG.get()),
				"Piping Bag is not a reusable crafting tool");
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 3, 3);
		ingredients.setItem(0, pipingBag);
		ingredients.setItem(2,
				new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
		ingredients.setItem(4,
				new ItemStack(CakeWorldItems.WARM_SPONGE_CAKE.get()));
		ingredients.setItem(8,
				new ItemStack(CakeWorldItems.ICING_SPOONFUL.get()));
		ShapelessRecipe decorating =
				(ShapelessRecipe) decoratingRecipe.orElseThrow();
		ItemStack cake = decorating.assemble(ingredients);
		require(helper,
				decorating.matches(ingredients, helper.getLevel())
						&& cake.is(
								CakeWorldItems.PIPED_CELEBRATION_CAKE.get())
						&& decorating.getRemainingItems(ingredients)
								.get(0).is(CakeWorldItems.PIPING_BAG.get())
						&& cake.getFoodProperties(null).getNutrition()
								> CakeWorldItems.WARM_SPONGE_CAKE.get()
										.getFoodProperties().getNutrition(),
				"Piping did not improve the cake and return the bag");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void coolingRackUsesTaggedRecipesAndReturnsContainers(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var coolingRecipe = recipes.byKey(new ResourceLocation(
				CakeWorld.MODID, "fudge_squares_from_cooling"));
		require(helper, coolingRecipe.orElse(null) instanceof StonecutterRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"cooling_rack")).isPresent(),
				"Cooling Rack is missing its block or standard recipe");
		ItemStack hotFudge = new ItemStack(
				CakeWorldFluids.HOT_FUDGE_BUCKET.get());
		require(helper, hotFudge.is(CoolingRackBlock.INPUTS)
						&& hotFudge.hasContainerItem()
						&& hotFudge.getContainerItem().is(Items.BUCKET),
				"Hot Fudge Bucket lost its tagged container-return contract");

		BlockPos relativeRackPos = new BlockPos(1, 1, 1);
		BlockPos absoluteRackPos = helper.absolutePos(relativeRackPos);
		CoolingRackBlock rack =
				(CoolingRackBlock) CakeWorldBlocks.COOLING_RACK.get();
		helper.setBlock(relativeRackPos, rack.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND, hotFudge);
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteRackPos), Direction.UP,
				absoluteRackPos, false);
		InteractionResult cooled = rack.use(rack.defaultBlockState(),
				helper.getLevel(), absoluteRackPos, player,
				InteractionHand.MAIN_HAND, hit);
		int fudgeSquares = player.getInventory().items.stream()
				.filter(stack -> stack.is(CakeWorldItems.FUDGE_SQUARE.get()))
				.mapToInt(ItemStack::getCount).sum();
		require(helper, cooled.consumesAction()
						&& player.getMainHandItem().is(Items.BUCKET)
						&& fudgeSquares == 8,
				"Cooling did not produce eight Fudge Squares and return the bucket");

		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.SUGAR));
		InteractionResult rejected = rack.use(rack.defaultBlockState(),
				helper.getLevel(), absoluteRackPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, rejected == InteractionResult.PASS
						&& player.getMainHandItem().is(Items.SUGAR),
				"Cooling Rack consumed an untagged unrelated input");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void candyCookerUsesTaggedSmokingRecipes(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var candyRecipe = recipes.byKey(new ResourceLocation(
				CakeWorld.MODID, "caramel_chew_from_candy_cooker"));
		require(helper,
				candyRecipe.orElse(null) instanceof SmokingRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"candy_cooker")).isPresent(),
				"Candy Cooker is missing its block or standard recipe");
		ItemStack sugar = new ItemStack(Items.SUGAR, 2);
		require(helper, sugar.is(CandyCookerBlock.INPUTS),
				"Sugar is missing the public Candy Cooker input tag");

		BlockPos relativeCookerPos = new BlockPos(1, 1, 1);
		BlockPos absoluteCookerPos = helper.absolutePos(relativeCookerPos);
		CandyCookerBlock cooker =
				(CandyCookerBlock) CakeWorldBlocks.CANDY_COOKER.get();
		helper.setBlock(relativeCookerPos, cooker.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND, sugar);
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteCookerPos), Direction.UP,
				absoluteCookerPos, false);
		InteractionResult unfuelled = cooker.use(cooker.defaultBlockState(),
				helper.getLevel(), absoluteCookerPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, unfuelled == InteractionResult.PASS
						&& player.getMainHandItem().getCount() == 2,
				"Unfuelled Candy Cooker consumed its input");

		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		InteractionResult cooked = cooker.use(cooker.defaultBlockState(),
				helper.getLevel(), absoluteCookerPos, player,
				InteractionHand.MAIN_HAND, hit);
		int caramelChews = player.getInventory().items.stream()
				.filter(stack -> stack.is(
						CakeWorldItems.CARAMEL_CHEW.get()))
				.mapToInt(ItemStack::getCount).sum();
		require(helper, cooked.consumesAction()
						&& player.getMainHandItem().is(Items.SUGAR)
						&& player.getMainHandItem().getCount() == 1
						&& player.getOffhandItem().isEmpty()
						&& caramelChews == 1,
				"Candy Cooker did not consume one input/fuel and make one chew");

		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.BEEF));
		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		InteractionResult rejected = cooker.use(cooker.defaultBlockState(),
				helper.getLevel(), absoluteCookerPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, rejected == InteractionResult.PASS
						&& player.getMainHandItem().is(Items.BEEF)
						&& player.getOffhandItem().is(Items.COAL),
				"Candy Cooker consumed an unrelated smoker recipe");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sodaFountainDispensesMeasuredBottleServings(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		require(helper,
				recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"lemonade_bottles")).orElse(null)
						instanceof ShapelessRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"soda_fountain")).isPresent(),
				"Soda Fountain is missing its block or standard drink recipe");
		ItemStack lemonadeBucket = new ItemStack(
				CakeWorldFluids.LEMONADE_BUCKET.get());
		require(helper, lemonadeBucket.is(SodaFountainBlock.INPUTS),
				"Lemonade Bucket is missing the public fountain input tag");

		BlockPos relativeFountainPos = new BlockPos(1, 1, 1);
		BlockPos absoluteFountainPos =
				helper.absolutePos(relativeFountainPos);
		SodaFountainBlock fountain =
				(SodaFountainBlock) CakeWorldBlocks.SODA_FOUNTAIN.get();
		helper.setBlock(relativeFountainPos, fountain.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND, lemonadeBucket);
		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.GLASS_BOTTLE, 2));
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteFountainPos), Direction.UP,
				absoluteFountainPos, false);
		InteractionResult shortServing = fountain.use(
				fountain.defaultBlockState(), helper.getLevel(),
				absoluteFountainPos, player, InteractionHand.MAIN_HAND, hit);
		require(helper, shortServing == InteractionResult.PASS
						&& player.getMainHandItem().is(
								CakeWorldFluids.LEMONADE_BUCKET.get())
						&& player.getOffhandItem().getCount() == 2,
				"Fountain consumed an incomplete three-bottle serving");

		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.GLASS_BOTTLE, 3));
		InteractionResult dispensed = fountain.use(
				fountain.defaultBlockState(), helper.getLevel(),
				absoluteFountainPos, player, InteractionHand.MAIN_HAND, hit);
		int lemonadeBottles = player.getInventory().items.stream()
				.filter(stack -> stack.is(
						CakeWorldItems.LEMONADE_BOTTLE.get()))
				.mapToInt(ItemStack::getCount).sum();
		require(helper, dispensed.consumesAction()
						&& player.getMainHandItem().is(Items.BUCKET)
						&& player.getOffhandItem().isEmpty()
						&& lemonadeBottles == 3,
				"Fountain did not return the bucket and three drinks");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void whimsicalVillageMachineryStaysNonIndustrial(
			GameTestHelper helper) {
		BlockPos relativeWindmillPos = new BlockPos(1, 1, 1);
		BlockPos absoluteWindmillPos =
				helper.absolutePos(relativeWindmillPos);
		WaferWindmillBlock windmill =
				(WaferWindmillBlock) CakeWorldBlocks.WAFER_WINDMILL.get();
		helper.setBlock(relativeWindmillPos,
				windmill.defaultBlockState()
						.setValue(WaferWindmillBlock.FACING,
								Direction.EAST));
		BlockPos relativePowerPos = relativeWindmillPos.relative(
				Direction.WEST);
		BlockPos absolutePowerPos =
				helper.absolutePos(relativePowerPos);
		helper.setBlock(relativePowerPos, Blocks.REDSTONE_BLOCK);
		windmill.neighborChanged(
				helper.getBlockState(relativeWindmillPos),
				helper.getLevel(), absoluteWindmillPos,
				Blocks.REDSTONE_BLOCK, absolutePowerPos, false);
		BlockState powered =
				helper.getBlockState(relativeWindmillPos);
		require(helper, powered.getValue(WaferWindmillBlock.POWERED)
						&& powered.getValue(WaferWindmillBlock.FACING)
								== Direction.EAST
						&& !windmill.isSignalSource(powered)
						&& helper.getLevel().getBlockEntity(
								absoluteWindmillPos) == null,
				"Wafer Windmill lost its powered ambient-only contract");

		helper.setBlock(relativePowerPos, Blocks.AIR);
		windmill.neighborChanged(
				helper.getBlockState(relativeWindmillPos),
				helper.getLevel(), absoluteWindmillPos,
				Blocks.AIR, absolutePowerPos, false);
		require(helper,
				!helper.getBlockState(relativeWindmillPos)
						.getValue(WaferWindmillBlock.POWERED),
				"Wafer Windmill stayed active after power was removed");

		RotatedPillarBlock pipe =
				(RotatedPillarBlock) CakeWorldBlocks.SYRUP_PIPE.get();
		BlockState pipeX = pipe.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
		BlockState pipeZ = pipeX.rotate(Rotation.CLOCKWISE_90);
		BlockPos relativePipePos = new BlockPos(2, 1, 1);
		helper.setBlock(relativePipePos, pipeX);
		require(helper,
				pipeX.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.X
						&& pipeZ.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.Z
						&& helper.getLevel().getBlockEntity(
								helper.absolutePos(relativePipePos)) == null,
				"Syrup Pipe lost its axis-aware decorative contract");
		require(helper,
				helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"wafer_windmill")).isPresent()
						&& helper.getLevel().getRecipeManager().byKey(
								new ResourceLocation(CakeWorld.MODID,
										"syrup_pipe")).isPresent(),
				"Whimsical village machinery is not obtainable");
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
	public static void cookbookHintsAreOptionalPersonalAndReadOnly(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978cafe-cafe-4bad-babe-1978cafe1980"),
						"CakeWorldHintTest"));
		CompoundTag untouched =
				CookbookProgress.snapshot(player).copy();
		var first = CookbookHints.nextHint(player);
		require(helper, first.isPresent()
						&& CookbookProgress.snapshot(player)
								.equals(untouched),
				"Selecting a hint wrote quest-like progress state");

		CookbookHints.Hint firstHint = first.orElseThrow();
		require(helper, CookbookProgress.discover(player,
						firstHint.type(), firstHint.target()),
				"Could not record the hinted discovery");
		var next = CookbookHints.nextHint(player);
		require(helper, next.isPresent()
						&& !next.orElseThrow().target()
								.equals(firstHint.target()),
				"Hint repeated a page the player already discovered");

		CompoundTag beforeSneakUse =
				CookbookProgress.snapshot(player).copy();
		player.setShiftKeyDown(true);
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems.EXPLORERS_COOKBOOK.get()));
		var used = CakeWorldItems.EXPLORERS_COOKBOOK.get().use(
				helper.getLevel(), player, InteractionHand.MAIN_HAND);
		require(helper, used.getResult().consumesAction()
						&& CookbookProgress.snapshot(player)
								.equals(beforeSneakUse),
				"Sneak-use hint changed discoveries or failed to consume use");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookSummaryDerivesStampsAndHonestCompletion(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978cafe-cafe-4bad-babe-1978cafe1981"),
						"CakeWorldSummaryTest"));
		CookbookSummary empty = CookbookSummary.from(
				CookbookProgress.read(CookbookProgress.snapshot(player)));
		require(helper, empty.totalPages() == 0 && empty.stamps() == 0
						&& !empty.firstEditionComplete(),
				"Empty Cookbook received pages, stamps, or completion");

		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, CookbookProgress.discover(player, type,
							new ResourceLocation(CakeWorld.MODID,
									"summary/" + type.name().toLowerCase())),
					"Could not add summary page for " + type);
		}
		CookbookSummary complete = CookbookSummary.from(
				CookbookProgress.read(CookbookProgress.snapshot(player)));
		require(helper, complete.totalPages() == 6
						&& complete.stamps() == 6
						&& complete.firstEditionComplete(),
				"All six discovery methods did not complete First Edition");
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, complete.pages(type) == 1
							&& complete.hasStamp(type),
					type + " has an incorrect page count or stamp");
		}

		require(helper, CookbookProgress.discover(player,
						DiscoveryType.VISITING,
						new ResourceLocation(CakeWorld.MODID,
								"summary/extra_place")),
				"Could not add an extra summary page");
		CookbookSummary expanded = CookbookSummary.from(
				CookbookProgress.read(CookbookProgress.snapshot(player)));
		require(helper, expanded.totalPages() == 7
						&& expanded.pages(DiscoveryType.VISITING) == 2
						&& expanded.stamps() == 6
						&& expanded.firstEditionComplete(),
				"Extra pages incorrectly changed stamp-based completion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookAccessibilityLayoutScalesAndSoundIsRegistered(
			GameTestHelper helper) {
		int tabs = DiscoveryType.values().length;
		CookbookLayout standard =
				CookbookLayout.calculate(854, 480, tabs);
		require(helper, standard.width() == CookbookLayout.MAX_WIDTH
						&& standard.height() == CookbookLayout.MAX_HEIGHT
						&& standard.tabColumns() == 6
						&& standard.tabRows() == 1,
				"Standard Cookbook layout lost its readable six-tab form");

		CookbookLayout compact =
				CookbookLayout.calculate(320, 180, tabs);
		require(helper, compact.left() >= 0 && compact.top() >= 0
						&& compact.left() + compact.width() <= 320
						&& compact.top() + compact.height() <= 180
						&& compact.tabColumns() == 3
						&& compact.tabRows() == 2
						&& compact.visiblePageCapacity() > 0,
				"Compact Cookbook layout escaped the scaled viewport");
		for (int index = 0; index < tabs; index++) {
			int hitX = (compact.tabLeft(index)
					+ compact.tabRight(index)) / 2;
			int hitY = compact.tabTop(index)
					+ CookbookLayout.TAB_HEIGHT / 2;
			require(helper, compact.tabIndexAt(hitX, hitY, tabs)
							== index,
					"Compact tab hit target did not map to tab " + index);
		}
		require(helper, compact.tabIndexAt(0, 0, tabs) == -1,
				"Outside click was incorrectly treated as a Cookbook tab");

		ResourceLocation soundId = new ResourceLocation(
				CakeWorld.MODID, "cookbook_discovery");
		require(helper, CakeWorldSounds.COOKBOOK_DISCOVERY.isPresent()
						&& Registry.SOUND_EVENT.get(soundId)
								== CakeWorldSounds.COOKBOOK_DISCOVERY.get(),
				"Subtitled Cookbook discovery sound is not registered");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sharedLibraryPublishesLoreWithoutGrantingProgress(
			GameTestHelper helper) {
		ServerPlayer contributor = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(),
						"CakeWorldLibraryContributor"));
		ServerPlayer reader = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(),
						"CakeWorldLibraryReader"));
		ResourceLocation privateDiscovery = new ResourceLocation(
				CakeWorld.MODID,
				"gametest/shared_" + UUID.randomUUID());
		require(helper, CookbookProgress.discover(contributor,
						DiscoveryType.FINDING, privateDiscovery),
				"Contributor could not discover publishable lore");
		CompoundTag readerBefore =
				CookbookProgress.snapshot(reader).copy();

		BlockPos relativeLibraryPos = new BlockPos(1, 1, 1);
		BlockPos absoluteLibraryPos =
				helper.absolutePos(relativeLibraryPos);
		CookbookLibraryBlock libraryBlock =
				(CookbookLibraryBlock) CakeWorldBlocks.COOKBOOK_LIBRARY.get();
		helper.setBlock(relativeLibraryPos,
				libraryBlock.defaultBlockState());
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteLibraryPos), Direction.UP,
				absoluteLibraryPos, false);
		contributor.setShiftKeyDown(true);
		InteractionResult published = libraryBlock.use(
				libraryBlock.defaultBlockState(), helper.getLevel(),
				absoluteLibraryPos, contributor,
				InteractionHand.MAIN_HAND, hit);
		SharedCookbookLibrary shared =
				SharedCookbookLibrary.get(helper.getLevel());
		require(helper, published.consumesAction()
						&& shared.contains(DiscoveryType.FINDING,
								privateDiscovery)
						&& shared.pageCount() > 0,
				"Library did not publish contributor-owned lore");
		SharedCookbookLibrary restored = SharedCookbookLibrary.load(
				shared.save(new CompoundTag()));
		require(helper, restored.contains(DiscoveryType.FINDING,
						privateDiscovery)
						&& restored.pageCount() == shared.pageCount(),
				"Shared lore did not survive a save/load round trip");

		reader.setShiftKeyDown(false);
		InteractionResult read = libraryBlock.use(
				libraryBlock.defaultBlockState(), helper.getLevel(),
				absoluteLibraryPos, reader,
				InteractionHand.MAIN_HAND, hit);
		require(helper, read.consumesAction()
						&& CookbookProgress.snapshot(reader)
								.equals(readerBefore),
				"Reading shared lore granted personal progression");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"cookbook_library")).isPresent(),
				"Community Cookbook Library is not obtainable");
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
	public static void jellylotlPreservesAquaticRoleWithoutNormalDamage(
			GameTestHelper helper) {
		Jellylotl jellylotl =
				CakeWorldEntities.JELLYLOTL.get().create(helper.getLevel());
		require(helper, jellylotl != null
						&& jellylotl.getBreedOffspring(helper.getLevel(),
								jellylotl) instanceof Jellylotl,
				"Jellylotls did not breed their own entity type");

		ItemStack flavourSource =
				new ItemStack(CakeWorldItems.JELLYLOTL_BUCKET.get());
		flavourSource.getOrCreateTag().putInt(Axolotl.VARIANT_TAG, 3);
		jellylotl.loadFromBucketTag(flavourSource.getTag());
		ItemStack captured = jellylotl.getBucketItemStack();
		jellylotl.saveToBucketTag(captured);
		Jellylotl restored =
				CakeWorldEntities.JELLYLOTL.get().create(helper.getLevel());
		require(helper, restored != null
						&& captured.is(CakeWorldItems.JELLYLOTL_BUCKET.get())
						&& CakeWorldItems.JELLYLOTL_BUCKET.get()
								instanceof net.minecraft.world.item.MobBucketItem
						&& "tooltip.cakeworld.jellylotl.flavour.3".equals(
								JellylotlBucketItem.flavourKey(captured)),
				"Jellylotl capture lost its dedicated bucket or flavour");
		restored.loadFromBucketTag(captured.getTag());
		require(helper, restored.getVariant() == jellylotl.getVariant(),
				"Jellylotl bucket round trip changed its flavour variant");

		Cod target = EntityType.COD.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Jellylotl helper target");
		float fullTargetHealth = target.getMaxHealth();
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(fullTargetHealth);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				require(helper, jellylotl.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- fullTargetHealth)
										< 0.001F
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.GLOWING),
						safeDifficulty
								+ " Jellylotl helper attack caused damage or lacked its visible slow");
			}
			target.removeAllEffects();
			target.setHealth(fullTargetHealth);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			require(helper, jellylotl.doHurtTarget(target)
							&& target.getHealth() < fullTargetHealth,
					"Hard Jellylotl attack did not restore real damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Jellylotl spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.AXOLOTL,
				CakeWorldEntities.JELLYLOTL.get(),
				MobCategory.AXOLOTLS);
		require(helper, CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState().is(
								BlockTags.AXOLOTLS_SPAWNABLE_ON),
				"Biscuit Crumbs cannot support natural Jellylotl spawning");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2001"),
						"CakeWorldJellylotlRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.JELLYLOTL.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:axolotl");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void bonbonBatIsHarmlessPersistentCakeWorldAmbience(
			GameTestHelper helper) {
		BonbonBat bat =
				CakeWorldEntities.BONBON_BAT.get().create(helper.getLevel());
		require(helper, bat != null
						&& bat.getType().getCategory()
								== MobCategory.AMBIENT
						&& bat.getAttribute(Attributes.ATTACK_DAMAGE) == null
						&& !bat.isPushable()
						&& !bat.causeFallDamage(100.0F, 1.0F,
								net.minecraft.world.damagesource.DamageSource.FALL),
				"Bonbon Bat gained a damage, collision, or fall-hazard role");
		bat.setResting(true);
		CompoundTag saved = new CompoundTag();
		bat.addAdditionalSaveData(saved);
		BonbonBat restored =
				CakeWorldEntities.BONBON_BAT.get().create(helper.getLevel());
		require(helper, restored != null, "Could not restore Bonbon Bat");
		restored.readAdditionalSaveData(saved);
		require(helper, restored.isResting(),
				"Bonbon Bat lost vanilla roost state through save/load");

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect CakeWorld Bonbon Bat biome");
			requireSpawnReplacement(helper, loaded, EntityType.BAT,
					CakeWorldEntities.BONBON_BAT.get(),
					MobCategory.AMBIENT);
		}
		require(helper, CakeWorldItems.BONBON_BAT_SPAWN_EGG.isPresent(),
				"Bonbon Bat has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sugarBeePollinatesWithoutNormalStingDamage(
			GameTestHelper helper) {
		SugarBee bee =
				CakeWorldEntities.SUGAR_BEE.get().create(helper.getLevel());
		require(helper, bee != null
						&& bee.getBreedOffspring(helper.getLevel(), bee)
								instanceof SugarBee,
				"Sugar Bees did not breed their own entity type");
		require(helper, bee.isFood(new ItemStack(
						CakeWorldItems.SPRINKLE_SEEDS.get()))
						&& new ItemStack(
								CakeWorldItems.SPRINKLE_SEEDS.get())
								.is(ItemTags.FLOWERS)
						&& CakeWorldBlocks.CANDY_SPROUT.get()
								.defaultBlockState().is(BlockTags.FLOWERS),
				"Candy Sprouts and Sprinkle Seeds lost their pollinator flower contracts");

		BlockPos flower = helper.absolutePos(new BlockPos(2, 2, 2));
		bee.setSavedFlowerPos(flower);
		CompoundTag saved = new CompoundTag();
		bee.addAdditionalSaveData(saved);
		SugarBee restored =
				CakeWorldEntities.SUGAR_BEE.get().create(helper.getLevel());
		require(helper, restored != null, "Could not restore Sugar Bee");
		restored.readAdditionalSaveData(saved);
		require(helper, restored.hasSavedFlowerPos()
						&& flower.equals(restored.getSavedFlowerPos()),
				"Sugar Bee lost its pollination target through save/load");

		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Sugar Bee sting target");
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				require(helper, bee.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.GLOWING)
								&& !target.hasEffect(
										net.minecraft.world.effect.MobEffects.POISON)
								&& !bee.hasStung(),
						safeDifficulty
								+ " Sugar Bee caused sting damage/poison or lost its visible pollen response");
			}
			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			require(helper, bee.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& bee.hasStung(),
					"Hard Sugar Bee did not restore the real sting");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect Sugar Bee biome");
			requireSpawnReplacement(helper, loaded, EntityType.BEE,
					CakeWorldEntities.SUGAR_BEE.get(),
					MobCategory.CREATURE);
		}

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2003"),
						"CakeWorldSugarBeeRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.SUGAR_BEE.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:bee");
		require(helper, CakeWorldItems.SUGAR_BEE_SPAWN_EGG.isPresent(),
				"Sugar Bee has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cinnamonSparkUsesSafePuffsUntilHard(
			GameTestHelper helper) {
		CinnamonSpark spark =
				CakeWorldEntities.CINNAMON_SPARK.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, spark != null && target != null,
				"Could not create Cinnamon Spark attack fixtures");
		BlockPos centre = helper.absolutePos(new BlockPos(3, 3, 3));
		spark.setPos(centre.getX(), centre.getY(), centre.getZ());
		target.setPos(centre.getX() + 3.0D, centre.getY(),
				centre.getZ());
		helper.getLevel().addFreshEntity(spark);
		helper.getLevel().addFreshEntity(target);
		AABB attackArea = new AABB(centre).inflate(12.0D);

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				spark.performRangedAttack(target, 1.0F);
				java.util.List<CinnamonPuffProjectile> puffs =
						helper.getLevel().getEntitiesOfClass(
								CinnamonPuffProjectile.class,
								attackArea);
				require(helper, !puffs.isEmpty(),
						safeDifficulty
								+ " Cinnamon Spark did not launch a safe puff");
				CinnamonPuffProjectile puff =
						puffs.get(puffs.size() - 1);
				puff.applyHarmlessPuff(target);
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE)
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " Cinnamon Puff caused damage or lacked rescue/knockback behavior");
				puff.discard();
			}

			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			spark.performRangedAttack(target, 1.0F);
			java.util.List<SmallFireball> fireballs =
					helper.getLevel().getEntitiesOfClass(
							SmallFireball.class, attackArea);
			require(helper, !fireballs.isEmpty(),
					"Hard Cinnamon Spark did not launch a real fireball");
			require(helper, spark.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Cinnamon Spark melee did not restore real damage");
			fireballs.forEach(fireball -> fireball.discard());
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome fudgeWastes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes Cinnamon Spark spawning");
		requireSpawnReplacement(helper, fudgeWastes, EntityType.BLAZE,
				CakeWorldEntities.CINNAMON_SPARK.get(),
				MobCategory.MONSTER);
		require(helper, CakeWorldItems.CINNAMON_SPARK_SPAWN_EGG.isPresent(),
				"Cinnamon Spark has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2004"),
						"CakeWorldCinnamonSparkRoleTest"));
		VanillaRoleAdvancements.creditKilledBlazeRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:blaze");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sodaCodSchoolAndReturnFromLemonadeBuckets(
			GameTestHelper helper) {
		SodaCod leader = CakeWorldEntities.SODA_COD.get()
				.create(helper.getLevel());
		SodaCod follower = CakeWorldEntities.SODA_COD.get()
				.create(helper.getLevel());
		require(helper, leader != null && follower != null,
				"Could not create Soda Cod schooling fixtures");
		follower.startFollowing(leader);
		require(helper, follower.isFollower()
						&& follower.inRangeOfLeader()
						&& leader.getMaxSchoolSize() > 1,
				"Soda Cod lost the inherited schooling role");

		BlockPos horizontalAnchor =
				helper.absolutePos(new BlockPos(3, 3, 3));
		BlockPos lemonadePos = new BlockPos(horizontalAnchor.getX(),
				helper.getLevel().getSeaLevel() - 5,
				horizontalAnchor.getZ());
		for (int y = -1; y <= 1; y++) {
			helper.getLevel().setBlock(lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
		}
		boolean vanillaRule =
				WaterAnimal.checkSurfaceWaterAnimalSpawnRules(
						CakeWorldEntities.SODA_COD.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						lemonadePos, new Random(1978L));
		boolean sodaRule = SodaCod.checkSodaCodSpawnRules(
				CakeWorldEntities.SODA_COD.get(), helper.getLevel(),
				MobSpawnType.NATURAL, lemonadePos,
				new Random(1978L));
		require(helper, !vanillaRule && sodaRule,
				"Soda Cod did not replace vanilla's hard-coded water-block spawn check with a Lemonade-compatible water-tag check");

		leader.setCustomName(new TextComponent("Fizz"));
		ItemStack bucket = leader.getBucketItemStack();
		leader.saveToBucketTag(bucket);
		require(helper,
				bucket.is(CakeWorldItems.SODA_COD_BUCKET.get())
						&& bucket.hasCustomHoverName(),
				"Soda Cod did not capture into its dedicated named bucket");
		AABB releaseArea = new AABB(lemonadePos).inflate(2.0D);
		helper.getLevel().getEntitiesOfClass(SodaCod.class, releaseArea)
				.forEach(SodaCod::discard);
		((MobBucketItem) bucket.getItem()).checkExtraContent(null,
				helper.getLevel(), bucket, lemonadePos);
		java.util.List<SodaCod> released =
				helper.getLevel().getEntitiesOfClass(SodaCod.class,
						releaseArea);
		require(helper, released.size() == 1
						&& released.get(0).fromBucket()
						&& released.get(0).hasCustomName()
						&& "Fizz".equals(released.get(0)
								.getCustomName().getString()),
				"Soda Cod bucket released the wrong entity or lost bucket/name data");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Soda Cod spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.COD,
				CakeWorldEntities.SODA_COD.get(),
				MobCategory.WATER_AMBIENT);
		require(helper, CakeWorldItems.SODA_COD_SPAWN_EGG.isPresent(),
				"Soda Cod has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void popRockPoppersStayHarmlessUntilHard(
			GameTestHelper helper) {
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		boolean originalMobGriefing = helper.getLevel().getGameRules()
				.getBoolean(GameRules.RULE_MOBGRIEFING);
		BlockPos centre = helper.absolutePos(new BlockPos(3, 3, 3));
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				PopRockPopper popper =
						CakeWorldEntities.POP_ROCK_POPPER.get()
								.create(helper.getLevel());
				Pig target = EntityType.PIG.create(helper.getLevel());
				require(helper, popper != null && target != null,
						"Could not create Pop-Rock Popper safety fixtures");
				popper.setNoAi(true);
				popper.setPos(centre.getX(), centre.getY(),
						centre.getZ());
				target.setPos(centre.getX() + 2.0D, centre.getY(),
						centre.getZ());
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 20.0F;
				target.setDeltaMovement(Vec3.ZERO);
				BlockPos protectedBlock = centre.offset(0, 0, 1);
				helper.getLevel().setBlock(protectedBlock,
						Blocks.GLASS.defaultBlockState(), 3);
				ItemEntity protectedItem = new ItemEntity(
						helper.getLevel(), centre.getX(),
						centre.getY(), centre.getZ() + 1.5D,
						new ItemStack(Items.DIAMOND, 3));
				protectedItem.setDeltaMovement(Vec3.ZERO);
				helper.getLevel().addFreshEntity(popper);
				helper.getLevel().addFreshEntity(target);
				helper.getLevel().addFreshEntity(protectedItem);

				popper.ignite();
				for (int tick = 0; tick < 31
						&& !popper.isRemoved(); tick++) {
					popper.tick();
				}
				require(helper, popper.isRemoved(),
						safeDifficulty
								+ " Pop-Rock Popper did not complete its safe fuse");
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F,
						safeDifficulty
								+ " safe pop caused health/fire/fall damage");
				require(helper,
						target.hasEffect(MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement().x > 0.0D
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " safe pop lacked controlled knockback and landing protection");
				require(helper,
						helper.getLevel().getBlockState(protectedBlock)
								.is(Blocks.GLASS)
								&& !protectedItem.isRemoved()
								&& protectedItem.getItem()
										.is(Items.DIAMOND)
								&& protectedItem.getItem().getCount() == 3
								&& protectedItem.getDeltaMovement()
										.equals(Vec3.ZERO),
						safeDifficulty
								+ " safe pop altered a block or item entity");
				target.discard();
				protectedItem.discard();
			}

			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(false, helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			PopRockPopper hardPopper =
					CakeWorldEntities.POP_ROCK_POPPER.get()
							.create(helper.getLevel());
			Pig hardTarget = EntityType.PIG.create(helper.getLevel());
			require(helper, hardPopper != null && hardTarget != null,
					"Could not create Hard Pop-Rock Popper fixtures");
			hardPopper.setNoAi(true);
			hardPopper.setPos(centre.getX(), centre.getY(),
					centre.getZ());
			hardTarget.setPos(centre.getX() + 2.0D, centre.getY(),
					centre.getZ());
			hardTarget.setHealth(10.0F);
			helper.getLevel().addFreshEntity(hardPopper);
			helper.getLevel().addFreshEntity(hardTarget);
			hardPopper.ignite();
			for (int tick = 0; tick < 31
					&& !hardPopper.isRemoved(); tick++) {
				hardPopper.tick();
			}
			require(helper,
					hardPopper.isRemoved()
							&& hardTarget.getHealth() < 10.0F,
					"Hard Pop-Rock Popper did not retain a real Creeper explosion");
			hardTarget.discard();
		} finally {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(originalMobGriefing,
							helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect Pop-Rock Popper biome role");
			requireSpawnReplacement(helper, loaded, EntityType.CREEPER,
					CakeWorldEntities.POP_ROCK_POPPER.get(),
					MobCategory.MONSTER);
		}
		require(helper,
				CakeWorldItems.POP_ROCK_POPPER_SPAWN_EGG.isPresent(),
				"Pop-Rock Popper has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2010"),
						"CakeWorldPopRockRoleTest"));
		VanillaRoleAdvancements.creditKilledCreeperRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:creeper");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sodaDolphinsGuideSafelyThroughLemonadeUntilHard(
			GameTestHelper helper) {
		SodaDolphin dolphin = CakeWorldEntities.SODA_DOLPHIN.get()
				.create(helper.getLevel());
		SodaDolphin loaded = CakeWorldEntities.SODA_DOLPHIN.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, dolphin != null && loaded != null
						&& target != null,
				"Could not create Soda Dolphin fixtures");

		Player feeder = helper.makeMockPlayer();
		feeder.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.COD, 2));
		InteractionResult feedResult = dolphin.interact(
				feeder, InteractionHand.MAIN_HAND);
		require(helper,
				feedResult.consumesAction()
						&& dolphin.gotFish()
						&& feeder.getItemInHand(
								InteractionHand.MAIN_HAND)
								.getCount() == 1,
				"Soda Dolphin did not retain the fish-fed treasure-guide trigger");

		BlockPos treasure = new BlockPos(1978, 42, -2011);
		dolphin.setTreasurePos(treasure);
		dolphin.setMoisntessLevel(1234);
		CompoundTag saved = new CompoundTag();
		dolphin.addAdditionalSaveData(saved);
		loaded.readAdditionalSaveData(saved);
		require(helper,
				loaded.gotFish()
						&& loaded.getTreasurePos().equals(treasure)
						&& loaded.getMoistnessLevel() == 1234
						&& loaded.getMaxAirSupply() == 4800
						&& !loaded.canBreatheUnderwater()
						&& loaded.canBeLeashed(feeder)
						&& Math.abs(loaded.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 1.2D)
								< 0.001D,
				"Soda Dolphin lost guide, moisture, air, leash or swimming-speed state");

		BlockPos horizontalAnchor =
				helper.absolutePos(new BlockPos(3, 3, 3));
		BlockPos lemonadePos = new BlockPos(horizontalAnchor.getX(),
				helper.getLevel().getSeaLevel() - 5,
				horizontalAnchor.getZ());
		for (int y = -1; y <= 1; y++) {
			helper.getLevel().setBlock(lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
		}
		boolean vanillaRule =
				WaterAnimal.checkSurfaceWaterAnimalSpawnRules(
						CakeWorldEntities.SODA_DOLPHIN.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						lemonadePos, new Random(1978L));
		boolean sodaRule = SodaDolphin.checkSodaDolphinSpawnRules(
				CakeWorldEntities.SODA_DOLPHIN.get(),
				helper.getLevel(), MobSpawnType.NATURAL,
				lemonadePos, new Random(1978L));
		require(helper, !vanillaRule && sodaRule,
				"Soda Dolphin did not replace vanilla's literal-water-block spawn check with a Lemonade-compatible water-tag check");

		dolphin.setPos(horizontalAnchor.getX(),
				horizontalAnchor.getY(), horizontalAnchor.getZ());
		target.setPos(horizontalAnchor.getX() + 2.0D,
				horizontalAnchor.getY(), horizontalAnchor.getZ());
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						dolphin.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.DOLPHINS_GRACE)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement().x > 0.0D
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " Soda Dolphin bump caused damage or lacked bubble rescue/swim effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper,
					dolphin.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Soda Dolphin did not retain real retaliatory damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Soda Dolphin spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.DOLPHIN,
				CakeWorldEntities.SODA_DOLPHIN.get(),
				MobCategory.WATER_CREATURE);
		require(helper, CakeWorldItems.SODA_DOLPHIN_SPAWN_EGG.isPresent(),
				"Soda Dolphin has no creative/testing spawn egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(new ResourceLocation(
						"minecraft", "adventure/kill_all_mobs"));
		require(helper, killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:dolphin"),
				"Vanilla unexpectedly added a Dolphin kill criterion; reassess compatibility before inventing a bridge");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void doughDonkeysArePersistentRideablePackAnimals(
			GameTestHelper helper) {
		DoughDonkey first = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		DoughDonkey second = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		require(helper, first != null && second != null,
				"Could not create Dough Donkey breeding fixtures");
		first.setTamed(true);
		second.setTamed(true);
		first.setInLove(null);
		second.setInLove(null);
		require(helper, first.canMate(second),
				"Two adult, tame, willing Dough Donkeys could not mate");
		AgeableMob child = first.getBreedOffspring(
				helper.getLevel(), second);
		require(helper, child instanceof DoughDonkey
						&& child.getType()
								== CakeWorldEntities.DOUGH_DONKEY.get(),
				"Dough Donkeys did not breed into their own entity type");

		Horse transitionalHorse = EntityType.HORSE.create(
				helper.getLevel());
		require(helper, transitionalHorse != null,
				"Could not create the staged horse-family fixture");
		AgeableMob transitionalMule = first.getBreedOffspring(
				helper.getLevel(), transitionalHorse);
		require(helper, transitionalMule != null
						&& transitionalMule.getType() == EntityType.MULE,
				"Dough Donkey broke vanilla horse-to-mule breeding before the CakeWorld Pony/Mule replacements exist");

		DoughDonkey packAnimal = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		require(helper, packAnimal != null,
				"Could not create the Dough Donkey pack fixture");
		Player rider = helper.makeMockPlayer();
		packAnimal.setPos(rider.getX(), rider.getY(), rider.getZ());
		packAnimal.setTamed(true);
		packAnimal.setOwnerUUID(rider.getUUID());
		helper.getLevel().addFreshEntity(packAnimal);
		rider.getAbilities().instabuild = false;
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.CHEST));
		InteractionResult chestResult = packAnimal.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, chestResult.consumesAction()
						&& packAnimal.hasChest()
						&& packAnimal.getInventoryColumns() == 5
						&& rider.getMainHandItem().isEmpty(),
				"Dough Donkey did not equip and consume its survival chest");
		require(helper, packAnimal.getSlot(500).set(
						new ItemStack(Items.DIAMOND, 3)),
				"Dough Donkey rejected the first of its 15 pack slots");
		require(helper, packAnimal.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& packAnimal.isSaddled(),
				"Dough Donkey could not equip its saddle");
		rider.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		InteractionResult rideResult = packAnimal.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, rideResult.consumesAction()
						&& rider.getVehicle() == packAnimal
						&& packAnimal.hasPassenger(rider),
				"A tame adult Dough Donkey did not accept an empty-hand rider");
		rider.stopRiding();

		CompoundTag saved = new CompoundTag();
		packAnimal.addAdditionalSaveData(saved);
		DoughDonkey restored = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create the Dough Donkey reload fixture");
		restored.readAdditionalSaveData(saved);
		ItemStack restoredPackItem = restored.getSlot(500).get();
		require(helper, restored.isTamed()
						&& rider.getUUID().equals(restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.isSaddled()
						&& restoredPackItem.is(Items.DIAMOND)
						&& restoredPackItem.getCount() == 3,
				"Dough Donkey lost owner, tame, chest, saddle, or pack contents across save/load");

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Dough Donkey spawning");
		requireSpawnReplacement(helper, candyPlains, EntityType.DONKEY,
				CakeWorldEntities.DOUGH_DONKEY.get(),
				MobCategory.CREATURE);
		require(helper, CakeWorldItems.DOUGH_DONKEY_SPAWN_EGG.isPresent(),
				"Dough Donkey has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2012"),
						"CakeWorldDoughDonkeyRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.DOUGH_DONKEY.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:donkey");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void soggyBiscuitsKeepAquaticRolesGentleUntilHard(
			GameTestHelper helper) {
		SoggyBiscuit biscuit = CakeWorldEntities.SOGGY_BISCUIT.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, biscuit != null && target != null,
				"Could not create Soggy Biscuit attack fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		biscuit.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		biscuit.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.TRIDENT));
		helper.getLevel().addFreshEntity(biscuit);
		helper.getLevel().addFreshEntity(target);

		BlockPos lemonadePos = anchor.offset(0, 0, 4);
		helper.getLevel().setBlock(lemonadePos,
				CakeWorldFluids.LEMONADE_BLOCK.get()
						.defaultBlockState(), 3);
		require(helper, SoggyBiscuit.isTaggedWater(
						helper.getLevel(), lemonadePos),
				"Soggy Biscuit's daylight water goal did not recognize Lemonade through the standard water tag");
		biscuit.setSwimming(true);
		require(helper, !biscuit.isPushedByFluid(),
				"Soggy Biscuit lost Drowned swimming-fluid control");
		biscuit.setSwimming(false);
		require(helper, biscuit.isPushedByFluid(),
				"Soggy Biscuit no longer resumes ordinary fluid pushing on land");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 8.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						biscuit.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.WATER_BREATHING)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Soggy Biscuit melee caused damage or lacked soggy/rescue effects");

				target.removeAllEffects();
				target.setHealth(10.0F);
				biscuit.performRangedAttack(target, 1.0F);
				java.util.List<SoggyTridentProjectile> safeTridents =
						helper.getLevel().getEntitiesOfClass(
								SoggyTridentProjectile.class,
								biscuit.getBoundingBox().inflate(4.0D));
				require(helper, safeTridents.size() == 1
								&& safeTridents.get(0).getType()
										== CakeWorldEntities.SOGGY_TRIDENT.get(),
						safeDifficulty
								+ " Soggy Biscuit did not throw its visible safe trident");
				require(helper,
						safeTridents.get(0).splash(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.WATER_BREATHING)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Soggy Trident caused damage or lacked its splash protection");
				safeTridents.get(0).discard();
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper, biscuit.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Soggy Biscuit did not retain real melee damage");
			biscuit.performRangedAttack(target, 1.0F);
			java.util.List<ThrownTrident> hardTridents =
					helper.getLevel().getEntitiesOfClass(
							ThrownTrident.class,
							biscuit.getBoundingBox().inflate(4.0D),
							trident -> trident.getType()
									== EntityType.TRIDENT);
			require(helper, hardTridents.size() == 1,
					"Hard Soggy Biscuit did not retain the genuine damaging vanilla trident projectile");
			hardTridents.forEach(ThrownTrident::discard);
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Drowned ruinDrowned = EntityType.DROWNED.create(
				helper.getLevel());
		require(helper, ruinDrowned != null,
				"Could not create the ocean-ruin conversion fixture");
		ResourceLocation fixtureBiome = helper.getLevel().getBiome(anchor)
				.unwrapKey().map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Ocean-ruin conversion fixture was not in a CakeWorld biome");
		ruinDrowned.moveTo(anchor.getX(), anchor.getY(), anchor.getZ(),
				37.0F, 0.0F);
		ruinDrowned.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.TRIDENT));
		ruinDrowned.setItemSlot(EquipmentSlot.OFFHAND,
				new ItemStack(Items.NAUTILUS_SHELL));
		ruinDrowned.setCustomName(new TextComponent("Sodden"));
		ruinDrowned.setPersistenceRequired();
		SoggyBiscuit converted =
				CakeWorldDrownedReplacement.convertIfInCakeWorldBiome(
						helper.getLevel(), ruinDrowned);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities.SOGGY_BISCUIT.get()
						&& converted.getMainHandItem().is(Items.TRIDENT)
						&& converted.getOffhandItem().is(
								Items.NAUTILUS_SHELL)
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Sodden")
						&& converted.isPersistenceRequired()
						&& Math.abs(converted.getYRot() - 37.0F)
								< 0.001F,
				"Ocean-ruin/Zombie conversion lost Drowned type, equipment, name, persistence or rotation");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Soggy Biscuit spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.DROWNED,
				CakeWorldEntities.SOGGY_BISCUIT.get(),
				MobCategory.MONSTER);
		TagKey<EntityType<?>> axolotlHostiles = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_always_hostiles"));
		require(helper,
				CakeWorldEntities.SOGGY_BISCUIT.get()
						.is(axolotlHostiles),
				"Soggy Biscuit did not preserve Drowned's axolotl-hostile tag role");
		require(helper,
				CakeWorldItems.SOGGY_BISCUIT_SPAWN_EGG.isPresent(),
				"Soggy Biscuit has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2013"),
						"CakeWorldSoggyBiscuitRoleTest"));
		VanillaRoleAdvancements.creditKilledDrownedRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:drowned");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void grandGumballGuardiansKeepPalaceRolesGentleUntilHard(
			GameTestHelper helper) {
		GrandGumballGuardian guardian =
				CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get()
						.create(helper.getLevel());
		Pig safeTarget = EntityType.PIG.create(helper.getLevel());
		require(helper, guardian != null && safeTarget != null,
				"Could not create Grand Gumball Guardian fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		guardian.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		safeTarget.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(guardian);
		helper.getLevel().addFreshEntity(safeTarget);
		require(helper,
				guardian instanceof ElderGuardian
						&& guardian.isPersistenceRequired()
						&& guardian.canBreatheUnderwater()
						&& guardian.getMobType() == MobType.WATER
						&& guardian.getAttackDuration() == 60
						&& Math.abs(guardian.getMaxHealth() - 80.0F)
								< 0.001F
						&& Math.abs(guardian.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 8.0D)
								< 0.001D
						&& Math.abs(guardian.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.3D)
								< 0.001D,
				"Grand Gumball Guardian lost the persistent Elder Guardian water, beam, fatigue-boss or attribute contract");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 8.0F;
				safeTarget.setDeltaMovement(Vec3.ZERO);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.indirectMagic(
								guardian, guardian), 13.0F);
				require(helper,
						Math.abs(safeTarget.getHealth()
										- 10.0F) < 0.001F
								&& !safeTarget.isOnFire()
								&& safeTarget.fallDistance == 0.0F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.getEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
										.getAmplifier() == 1
								&& safeTarget.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Grand Gumball beam caused health damage or lacked sticky/rescue effects");

				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.thorns(guardian), 2.0F);
				require(helper,
						Math.abs(safeTarget.getHealth()
										- 10.0F) < 0.001F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Grand Gumball thorns caused health damage or lost their safe bounce");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget = EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard guardian target");
			hardTarget.setPos(anchor.getX() + 5.0D,
					anchor.getY(), anchor.getZ());
			hardTarget.setHealth(10.0F);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper,
					hardTarget.hurt(DamageSource.indirectMagic(
									guardian, guardian), 5.0F)
							&& hardTarget.getHealth() < 10.0F,
					"Hard Grand Gumball Guardian did not retain real beam damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		ElderGuardian monumentGuardian =
				EntityType.ELDER_GUARDIAN.create(helper.getLevel());
		require(helper, monumentGuardian != null,
				"Could not create ocean-monument conversion fixture");
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(anchor).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Ocean-monument conversion fixture was not in a CakeWorld biome");
		monumentGuardian.moveTo(anchor.getX(), anchor.getY(),
				anchor.getZ(), 29.0F, 0.0F);
		monumentGuardian.setHealth(37.0F);
		monumentGuardian.setCustomName(
				new TextComponent("Grand Gumdrop"));
		monumentGuardian.setPersistenceRequired();
		GrandGumballGuardian converted =
				CakeWorldElderGuardianReplacement
						.convertIfInCakeWorldBiome(
								helper.getLevel(),
								monumentGuardian);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities
										.GRAND_GUMBALL_GUARDIAN.get()
						&& Math.abs(converted.getHealth() - 37.0F)
								< 0.001F
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Grand Gumdrop")
						&& converted.isPersistenceRequired()
						&& Math.abs(converted.getYRot() - 29.0F)
								< 0.001F,
				"Ocean-monument conversion lost boss type, health, name, persistence or rotation");

		TagKey<EntityType<?>> axolotlHostiles = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_always_hostiles"));
		require(helper,
				CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get()
						.is(axolotlHostiles),
				"Grand Gumball Guardian did not preserve Elder Guardian's axolotl-hostile tag role");
		require(helper,
				CakeWorldItems.GRAND_GUMBALL_GUARDIAN_SPAWN_EGG
						.isPresent(),
				"Grand Gumball Guardian has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2014"),
						"CakeWorldGrandGuardianRoleTest"));
		VanillaRoleAdvancements.creditKilledElderGuardianRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:elder_guardian");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void taffyTallwalkersTeleportAndCarryWithoutSafeGrief(
			GameTestHelper helper) {
		TaffyTallwalker tallwalker =
				CakeWorldEntities.TAFFY_TALLWALKER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, tallwalker != null && target != null,
				"Could not create Taffy Tallwalker fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		tallwalker.setNoAi(true);
		tallwalker.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(tallwalker);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				tallwalker instanceof EnderMan
						&& tallwalker.isSensitiveToWater()
						&& Math.abs(tallwalker.getMaxHealth() - 40.0F)
								< 0.001F
						&& Math.abs(tallwalker.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 7.0D)
								< 0.001D
						&& Math.abs(tallwalker.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 64.0D)
								< 0.001D,
				"Taffy Tallwalker lost the Enderman teleport/water, health, attack or follow-range role");

		UUID angerTarget = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2016");
		tallwalker.setRemainingPersistentAngerTime(321);
		tallwalker.setPersistentAngerTarget(angerTarget);
		tallwalker.setCarriedBlock(CakeWorldBlocks.ICING_LAYER.get()
				.defaultBlockState());
		require(helper, tallwalker.requiresCustomPersistence(),
				"A block-carrying Taffy Tallwalker was not persistent");
		CompoundTag saved = tallwalker.saveWithoutId(new CompoundTag());
		TaffyTallwalker restored =
				CakeWorldEntities.TAFFY_TALLWALKER.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Taffy Tallwalker NBT fixture");
		restored.load(saved);
		require(helper,
				restored.getCarriedBlock() != null
						&& restored.getCarriedBlock().is(
								CakeWorldBlocks.ICING_LAYER.get())
						&& restored.getRemainingPersistentAngerTime()
								== 321
						&& angerTarget.equals(
								restored.getPersistentAngerTarget()),
				"Taffy Tallwalker lost its carried block or persistent anger through NBT");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		boolean originalMobGriefing = helper.getLevel().getGameRules()
				.getBoolean(GameRules.RULE_MOBGRIEFING);
		try {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(true, helper.getLevel().getServer());
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						tallwalker.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.LEVITATION)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Taffy Tallwalker attack caused damage or lacked void-rescue effects");
				require(helper,
						!ForgeEventFactory.getMobGriefingEvent(
								helper.getLevel(), tallwalker),
						safeDifficulty
								+ " Taffy Tallwalker could take or place blocks");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper,
					tallwalker.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Taffy Tallwalker did not retain real Enderman damage");
			require(helper,
					ForgeEventFactory.getMobGriefingEvent(
							helper.getLevel(), tallwalker),
					"Hard Taffy Tallwalker did not honor the enabled mobGriefing rule");
		} finally {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(originalMobGriefing,
							helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect Taffy Tallwalker biome role");
			requireSpawnReplacement(helper, loaded,
					EntityType.ENDERMAN,
					CakeWorldEntities.TAFFY_TALLWALKER.get(),
					MobCategory.MONSTER);
		}
		require(helper,
				CakeWorldItems.TAFFY_TALLWALKER_SPAWN_EGG.isPresent(),
				"Taffy Tallwalker has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed3016"),
						"CakeWorldTaffyTallwalkerRoleTest"));
		VanillaRoleAdvancements.creditKilledEndermanRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:enderman");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sugarMitesKeepPearlLifetimeAndGentleBites(
			GameTestHelper helper) {
		SugarMite mite = CakeWorldEntities.SUGAR_MITE.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, mite != null && target != null,
				"Could not create Sugar Mite fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		mite.setNoAi(true);
		mite.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(mite);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				mite instanceof Endermite
						&& mite.getMobType() == MobType.ARTHROPOD
						&& Math.abs(mite.getMaxHealth() - 8.0F)
								< 0.001F
						&& Math.abs(mite.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 2.0D)
								< 0.001D
						&& Math.abs(mite.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.25D)
								< 0.001D,
				"Sugar Mite lost the Endermite arthropod, health, attack or movement role");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						mite.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Sugar Mite bite caused damage or lacked rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper,
					mite.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Sugar Mite did not retain real Endermite damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Endermite pearlMite = EntityType.ENDERMITE.create(
				helper.getLevel());
		require(helper, pearlMite != null,
				"Could not create Ender Pearl conversion fixture");
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(anchor).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Ender Pearl conversion fixture was not in a CakeWorld biome");
		CompoundTag pearlState = pearlMite.saveWithoutId(
				new CompoundTag());
		pearlState.putInt("Lifetime", 123);
		pearlMite.load(pearlState);
		pearlMite.moveTo(anchor.getX(), anchor.getY(),
				anchor.getZ(), 17.0F, 0.0F);
		pearlMite.setCustomName(new TextComponent("Spark"));
		SugarMite converted =
				CakeWorldEndermiteReplacement
						.convertIfInCakeWorldBiome(
								helper.getLevel(), pearlMite);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities.SUGAR_MITE.get()
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Spark")
						&& Math.abs(converted.getYRot() - 17.0F)
								< 0.001F,
				"Ender Pearl conversion lost Sugar Mite type, name or rotation");
		CompoundTag convertedState = converted.saveWithoutId(
				new CompoundTag());
		require(helper, convertedState.getInt("Lifetime") == 123,
				"Ender Pearl conversion reset the Endermite lifetime");

		SugarMite expiring = CakeWorldEntities.SUGAR_MITE.get()
				.create(helper.getLevel());
		require(helper, expiring != null,
				"Could not create Sugar Mite lifetime fixture");
		CompoundTag expiringState = expiring.saveWithoutId(
				new CompoundTag());
		expiringState.putInt("Lifetime", 2399);
		expiring.load(expiringState);
		expiring.setNoAi(true);
		expiring.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(expiring);
		expiring.aiStep();
		require(helper, expiring.isRemoved(),
				"Sugar Mite did not retain the 2,400-tick Endermite lifetime");

		TagKey<EntityType<?>> powderSnowWalkers = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"powder_snow_walkable_mobs"));
		require(helper, CakeWorldEntities.SUGAR_MITE.get()
						.is(powderSnowWalkers),
				"Sugar Mite did not preserve Endermite's powder-snow walking tag role");
		require(helper, CakeWorldItems.SUGAR_MITE_SPAWN_EGG.isPresent(),
				"Sugar Mite has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2017"),
						"CakeWorldSugarMiteRoleTest"));
		VanillaRoleAdvancements.creditKilledEndermiteRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:endermite");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sourSorcerersKeepRaidsAndGentleSpells(
			GameTestHelper helper) {
		SourSorcerer sorcerer = CakeWorldEntities.SOUR_SORCERER.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		Vex vex = EntityType.VEX.create(helper.getLevel());
		require(helper, sorcerer != null && target != null && vex != null,
				"Could not create Sour Sorcerer spell fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		sorcerer.setNoAi(true);
		sorcerer.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		vex.setNoAi(true);
		vex.setPos(anchor.getX() - 1.0D,
				anchor.getY(), anchor.getZ());
		vex.setOwner(sorcerer);
		helper.getLevel().addFreshEntity(sorcerer);
		helper.getLevel().addFreshEntity(target);
		helper.getLevel().addFreshEntity(vex);
		require(helper,
				sorcerer instanceof Evoker
						&& sorcerer.getMobType() == MobType.ILLAGER
						&& Math.abs(sorcerer.getMaxHealth() - 24.0F)
								< 0.001F
						&& Math.abs(sorcerer.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.5D)
								< 0.001D
						&& Math.abs(sorcerer.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 12.0D)
								< 0.001D
						&& sorcerer.isAlliedTo(vex),
				"Sour Sorcerer lost the Evoker illager, attribute or summoned-Vex alliance role");

		EvokerFangs fangs = new EvokerFangs(helper.getLevel(),
				target.getX(), target.getY(), target.getZ(),
				0.0F, 0, sorcerer);
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(DamageSource.indirectMagic(
						fangs, sorcerer), 6.0F);
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(MobEffects.CONFUSION)
								&& target.hasEffect(MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Sour Sorcerer fangs caused damage or lacked rescue effects");

				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.hurt(DamageSource.mobAttack(vex), 4.0F);
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& target.hasEffect(MobEffects.CONFUSION)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Sour Sorcerer summon caused health damage");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.hurt(DamageSource.indirectMagic(
					fangs, sorcerer), 6.0F);
			require(helper, target.getHealth() < 10.0F,
					"Hard Sour Sorcerer fangs did not retain real damage");
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.hurt(DamageSource.mobAttack(vex), 4.0F);
			require(helper, target.getHealth() < 10.0F,
					"Hard Sour Sorcerer summon did not retain real damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Evoker raidEvoker = EntityType.EVOKER.create(helper.getLevel());
		Ravager ravager = EntityType.RAVAGER.create(helper.getLevel());
		require(helper, raidEvoker != null && ravager != null,
				"Could not create raid conversion fixtures");
		raidEvoker.moveTo(anchor.getX() + 3.0D, anchor.getY(),
				anchor.getZ(), 27.0F, 0.0F);
		raidEvoker.setCustomName(new TextComponent("Tang"));
		raidEvoker.setPersistenceRequired();
		CompoundTag castingState = raidEvoker.saveWithoutId(
				new CompoundTag());
		castingState.putInt("SpellTicks", 25);
		raidEvoker.load(castingState);
		ravager.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(ravager);
		Raid raid = new Raid(197825, helper.getLevel(), anchor);
		raid.joinRaid(3, raidEvoker, null, true);
		raid.setLeader(3, raidEvoker);
		helper.getLevel().addFreshEntity(raidEvoker);
		raidEvoker.startRiding(ravager, true);
		SourSorcerer converted =
				CakeWorldEvokerReplacement.replaceIfInCakeWorldBiome(
						helper.getLevel(), raidEvoker);
		require(helper, converted != null
						&& raidEvoker.isRemoved()
						&& converted.getType()
								== CakeWorldEntities.SOUR_SORCERER.get()
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Tang")
						&& converted.isPersistenceRequired()
						&& converted.isCastingSpell()
						&& Math.abs(converted.getYRot() - 27.0F)
								< 0.001F,
				"Raid conversion lost Sorcerer type, name, persistence, spell or rotation");
		CompoundTag convertedState = converted.saveWithoutId(
				new CompoundTag());
		require(helper, convertedState.getInt("SpellTicks") == 25
						&& converted.getCurrentRaid() == raid
						&& converted.getWave() == 3
						&& raid.getLeader(3) == converted
						&& raid.getTotalRaidersAlive() == 1
						&& converted.getVehicle() == ravager,
				"Raid conversion lost spell ticks, wave, leader, count or Ravager seat");

		TagKey<EntityType<?>> raiders = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft", "raiders"));
		require(helper, CakeWorldEntities.SOUR_SORCERER.get()
						.is(raiders),
				"Sour Sorcerer did not preserve the vanilla raider tag role");
		require(helper, CakeWorldItems.SOUR_SORCERER_SPAWN_EGG.isPresent(),
				"Sour Sorcerer has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2018"),
						"CakeWorldSourSorcererRoleTest"));
		VanillaRoleAdvancements.creditKilledEvokerRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:evoker");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void peppermintFoxesKeepTrustSleepAndMintyPounces(
			GameTestHelper helper) {
		PeppermintFox fox = CakeWorldEntities.PEPPERMINT_FOX.get()
				.create(helper.getLevel());
		PeppermintFox partner = CakeWorldEntities.PEPPERMINT_FOX.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, fox != null && partner != null && target != null,
				"Could not create Peppermint Fox fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		fox.setNoAi(true);
		fox.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		partner.setNoAi(true);
		partner.setPos(anchor.getX() - 1.0D,
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(fox);
		helper.getLevel().addFreshEntity(partner);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				fox instanceof Fox
						&& Math.abs(fox.getMaxHealth() - 10.0F)
								< 0.001F
						&& Math.abs(fox.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.3D)
								< 0.001D
						&& Math.abs(fox.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 32.0D)
								< 0.001D
						&& Math.abs(fox.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 2.0D)
								< 0.001D,
				"Peppermint Fox lost the Fox health, movement, follow or pounce role");

		UUID trusted = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2019");
		for (PeppermintFox fixture :
				new PeppermintFox[] {fox, partner}) {
			fixture.setItemSlot(EquipmentSlot.MAINHAND,
					new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
			CompoundTag state = fixture.saveWithoutId(
					new CompoundTag());
			ListTag trustedList = new ListTag();
			trustedList.add(NbtUtils.createUUID(trusted));
			state.put("Trusted", trustedList);
			state.putBoolean("Sleeping", true);
			state.putBoolean("Sitting", true);
			state.putString("Type", Fox.Type.SNOW.getName());
			fixture.load(state);
		}
		CompoundTag savedFox = fox.saveWithoutId(new CompoundTag());
		ListTag savedTrusted = savedFox.getList("Trusted", 11);
		require(helper,
				fox.getFoxType() == Fox.Type.SNOW
						&& fox.isSleeping()
						&& fox.isSitting()
						&& fox.getItemBySlot(EquipmentSlot.MAINHAND)
								.is(CakeWorldItems.BOILED_SWEET.get())
						&& savedTrusted.size() == 1
						&& NbtUtils.loadUUID(savedTrusted.get(0))
								.equals(trusted),
				"Peppermint Fox lost climate type, sleep, carried sweet or trusted player NBT");
		require(helper,
				fox.isFood(new ItemStack(Items.SWEET_BERRIES))
						&& fox.isFood(new ItemStack(
								CakeWorldItems.BOILED_SWEET.get()))
						&& fox.isFood(new ItemStack(
								CakeWorldItems.MINT_WAFER.get())),
				"Peppermint Fox did not accept vanilla berries and tagged sweets");

		PeppermintFox child = fox.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper, child != null
						&& child.getType()
								== CakeWorldEntities.PEPPERMINT_FOX.get()
						&& child.getFoxType() == Fox.Type.SNOW,
				"Peppermint Fox breeding lost the custom type or parent climate");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						fox.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SPEED)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Peppermint Fox pounce caused damage or lacked mint rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper, fox.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Peppermint Fox did not retain real Fox damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		TagKey<EntityType<?>> powderSnowWalkers = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"powder_snow_walkable_mobs"));
		require(helper, CakeWorldEntities.PEPPERMINT_FOX.get()
						.is(powderSnowWalkers),
				"Peppermint Fox did not preserve Fox's powder-snow walking role");
		require(helper, CakeWorldItems.PEPPERMINT_FOX_SPAWN_EGG.isPresent(),
				"Peppermint Fox has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2119"),
						"CakeWorldPeppermintFoxRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.PEPPERMINT_FOX.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:fox");

		Biome peppermintPinewoods = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(new ResourceLocation(CakeWorld.MODID,
						"peppermint_pinewoods"));
		require(helper, peppermintPinewoods == null,
				"Peppermint Pinewoods now exists; replace this dependency gate with exact Fox spawn-role proof");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void deepLiquoriceWeaversStayStickyUntilHard(
			GameTestHelper helper) {
		DeepLiquoriceWeaver weaver =
				CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, weaver != null && target != null,
				"Could not create Deep Liquorice Weaver attack fixtures");
		helper.getLevel().addFreshEntity(weaver);
		helper.getLevel().addFreshEntity(target);

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				require(helper, weaver.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& !target.hasEffect(MobEffects.POISON)
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.DIG_SLOWDOWN),
						safeDifficulty
								+ " Weaver bite caused damage/poison or lacked sticky effects");
			}

			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			require(helper, weaver.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& target.hasEffect(MobEffects.POISON),
					"Hard Weaver bite did not restore damage and poison");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId()}) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Weaver surface-spawn contract");
			for (MobSpawnSettings.SpawnerData spawn :
					biome.getMobSettings().getMobs(
							MobCategory.MONSTER).unwrap()) {
				require(helper,
						spawn.type != EntityType.CAVE_SPIDER
								&& spawn.type
										!= CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get(),
						"Deep Liquorice Weaver was accidentally made a surface biome spawn");
			}
		}

		require(helper,
				CakeWorldItems.DEEP_LIQUORICE_WEAVER_SPAWN_EGG.isPresent(),
				"Deep Liquorice Weaver has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2006"),
						"CakeWorldWeaverRoleTest"));
		VanillaRoleAdvancements.creditKilledCaveSpiderRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:cave_spider");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void custardCatsKeepTamingAndCatalogueRoles(
			GameTestHelper helper) {
		CustardCat first = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		CustardCat second = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		require(helper, first != null && second != null,
				"Could not create Custard Cat breeding fixtures");
		UUID owner = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2005");
		first.setCatType(Cat.TYPE_BRITISH);
		second.setCatType(Cat.TYPE_JELLIE);
		first.setOwnerUUID(owner);
		second.setOwnerUUID(owner);
		first.setTame(true);
		second.setTame(true);
		first.setCollarColor(DyeColor.PINK);
		second.setCollarColor(DyeColor.YELLOW);

		Cat child = first.getBreedOffspring(helper.getLevel(), second);
		require(helper, child instanceof CustardCat
						&& child.getType()
								== CakeWorldEntities.CUSTARD_CAT.get(),
				"Custard Cats did not breed into their own entity type");
		require(helper,
				(child.getCatType() == Cat.TYPE_BRITISH
						|| child.getCatType() == Cat.TYPE_JELLIE)
						&& child.isTame()
						&& owner.equals(child.getOwnerUUID())
						&& (child.getCollarColor() == DyeColor.PINK
								|| child.getCollarColor()
										== DyeColor.YELLOW),
				"Custard Cat offspring lost variant, owner, tame, or collar inheritance");
		require(helper,
				child.isFood(new ItemStack(Items.COD))
						&& child.isFood(new ItemStack(Items.SALMON))
						&& !child.isFood(new ItemStack(
								CakeWorldItems.SIMPLE_BISCUIT.get())),
				"Custard Cat changed the compatible vanilla cat food role");

		CompoundTag saved = new CompoundTag();
		child.addAdditionalSaveData(saved);
		CustardCat restored = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Custard Cat save fixture");
		restored.readAdditionalSaveData(saved);
		require(helper,
				restored.getCatType() == child.getCatType()
						&& restored.isTame()
						&& owner.equals(restored.getOwnerUUID())
						&& restored.getCollarColor()
								== child.getCollarColor(),
				"Custard Cat did not retain variant/taming data");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(owner, "CakeWorldCustardCatRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.CUSTARD_CAT.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:cat");
		CustardCat catalogueCat = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		require(helper, catalogueCat != null,
				"Could not create Custard Cat catalogue fixture");
		catalogueCat.setCatType(Cat.TYPE_BRITISH);
		CriteriaTriggers.TAME_ANIMAL.trigger(advancementPlayer,
				catalogueCat);
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/complete_catalogue",
				"textures/entity/cat/british_shorthair.png");
		require(helper, CakeWorldItems.CUSTARD_CAT_SPAWN_EGG.isPresent(),
				"Custard Cat has no creative/testing spawn egg");
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
		java.util.List<CustardCat> companions =
				helper.getLevel().getEntitiesOfClass(CustardCat.class,
						new AABB(absoluteCentre).inflate(12.0D));
		require(helper, companions.size() == 1
						&& companions.get(0).isPersistenceRequired()
						&& companions.get(0).hasRestriction()
						&& companions.get(0).getRestrictCenter()
								.equals(absoluteCentre),
				"The First Bite picnic did not receive one persistent, home-restricted Custard Cat");

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
