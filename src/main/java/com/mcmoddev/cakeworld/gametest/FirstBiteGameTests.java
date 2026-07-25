package com.mcmoddev.cakeworld.gametest;

import java.util.List;
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
import com.mcmoddev.cakeworld.entity.DriedCrumbler;
import com.mcmoddev.cakeworld.entity.GrandGumballGuardian;
import com.mcmoddev.cakeworld.entity.GiantStaleCrumbler;
import com.mcmoddev.cakeworld.entity.GlowJelly;
import com.mcmoddev.cakeworld.entity.GumballGuardian;
import com.mcmoddev.cakeworld.entity.FudgeBoar;
import com.mcmoddev.cakeworld.entity.GingerbreadPony;
import com.mcmoddev.cakeworld.entity.HotFudgeBlob;
import com.mcmoddev.cakeworld.entity.HotFudgeBlobDamageSafety;
import com.mcmoddev.cakeworld.entity.JawbreakerGuardian;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.MallowFloater;
import com.mcmoddev.cakeworld.entity.MallowPuffProjectile;
import com.mcmoddev.cakeworld.entity.MarzipanMule;
import com.mcmoddev.cakeworld.entity.MeringueLlama;
import com.mcmoddev.cakeworld.entity.MeringueLlamaFollowCaravanGoal;
import com.mcmoddev.cakeworld.entity.MirageConfectioner;
import com.mcmoddev.cakeworld.entity.MirageSweetProjectile;
import com.mcmoddev.cakeworld.entity.NougatGoat;
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
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.sensing.HoglinSpecificSensor;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
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
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.NetherFortressFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import com.mcmoddev.cakeworld.world.StarterPicnicFeature;
import com.mcmoddev.cakeworld.world.CakeWorldDrownedReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldElderGuardianReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldEndermiteReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldEvokerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldGiantReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldGuardianReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldIllusionerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldIronGolemReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldMagmaCubeReplacement;

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

		GingerbreadPony transitionalHorse =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, transitionalHorse != null,
				"Could not create the staged horse-family fixture");
		AgeableMob transitionalMule = first.getBreedOffspring(
				helper.getLevel(), transitionalHorse);
		require(helper,
				transitionalMule instanceof MarzipanMule
						&& transitionalMule.getType()
								== CakeWorldEntities.MARZIPAN_MULE.get(),
				"Dough Donkey and Gingerbread Pony did not produce Marzipan Mule");

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
	public static void gumballGuardiansSignalPalaceBeamsGentleUntilHard(
			GameTestHelper helper) {
		GumballGuardian guardian =
				CakeWorldEntities.GUMBALL_GUARDIAN.get()
						.create(helper.getLevel());
		Pig safeTarget = EntityType.PIG.create(helper.getLevel());
		require(helper, guardian != null && safeTarget != null,
				"Could not create Gumball Guardian fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		guardian.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		safeTarget.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		guardian.setNoGravity(true);
		safeTarget.setNoGravity(true);
		helper.getLevel().addFreshEntity(guardian);
		helper.getLevel().addFreshEntity(safeTarget);
		require(helper,
				guardian instanceof Guardian
						&& !ElderGuardian.class
								.isInstance(guardian)
						&& guardian.canBreatheUnderwater()
						&& guardian.getMobType() == MobType.WATER
						&& guardian.getAttackDuration() == 80
						&& close(guardian.getDimensions(Pose.STANDING)
								.width, 0.85D)
						&& close(guardian.getDimensions(Pose.STANDING)
								.height, 0.85D)
						&& close(guardian.getMaxHealth(), 30.0D)
						&& close(guardian.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 6.0D)
						&& close(guardian.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.5D)
						&& close(guardian.getAttributeValue(
								Attributes.FOLLOW_RANGE), 16.0D),
				"Gumball Guardian lost the ordinary Guardian water, size, beam, or attribute contract");

		guardian.setTarget(safeTarget);
		for (int tick = 0;
				tick < 30 && !guardian.hasActiveAttackTarget();
				++tick) {
			guardian.tick();
		}
		require(helper,
				guardian.hasActiveAttackTarget()
						&& guardian.getActiveAttackTarget()
								== safeTarget,
				"Gumball Guardian did not expose the inherited visible beam target before impact");
		guardian.setTarget(null);
		guardian.setNoAi(true);

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 8.0F;
				safeTarget.setDeltaMovement(Vec3.ZERO);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(DamageSource.indirectMagic(
						guardian, guardian), 3.0F);
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& !safeTarget.isOnFire()
								&& safeTarget.fallDistance == 0.0F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
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
								+ " Gumball beam contract failed: health="
								+ safeTarget.getHealth()
								+ ", fire=" + safeTarget.isOnFire()
								+ ", fall=" + safeTarget.fallDistance
								+ ", slow=" + safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								+ ", miningSlow=" + safeTarget.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								+ ", slowFall=" + safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								+ ", fireResist=" + safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								+ ", resistance=" + safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE));

				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.mobAttack(guardian), 6.0F);
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Gumball beam contact caused health damage or lost its safe impact");

				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.thorns(guardian), 2.0F);
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Gumball thorns caused health damage or lost their safe impact");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Gumball target");
			hardTarget.setPos(anchor.getX() + 5.0D,
					anchor.getY(), anchor.getZ());
			hardTarget.setHealth(10.0F);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper,
					hardTarget.hurt(
							DamageSource.mobAttack(guardian),
							6.0F)
							&& hardTarget.getHealth() < 10.0F,
					"Hard Gumball Guardian did not retain real beam-contact damage");

			BlockPos lemonadePos = anchor.offset(0, 0, 6);
			helper.getLevel().setBlock(lemonadePos.below(),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
			helper.getLevel().setBlock(lemonadePos,
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
			helper.getLevel().setBlock(lemonadePos.above(3),
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState(), 3);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			require(helper,
					Guardian.checkGuardianSpawnRules(
							CakeWorldEntities.GUMBALL_GUARDIAN.get(),
							helper.getLevel(),
							MobSpawnType.SPAWNER,
							lemonadePos,
							new Random(24L))
							&& SpawnPlacements.getPlacementType(
									CakeWorldEntities
											.GUMBALL_GUARDIAN.get())
									== SpawnPlacements.Type.IN_WATER,
					"Gumball Guardian lost the covered tagged-water spawn contract for Lemonade");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Guardian monumentGuardian =
				EntityType.GUARDIAN.create(helper.getLevel());
		require(helper, monumentGuardian != null,
				"Could not create monument Guardian conversion fixture");
		var nearestCakeWorldBiome =
				helper.getLevel().findNearestBiome(
						holder -> holder.unwrapKey()
								.map(key -> CakeWorld.MODID
										.equals(key.location()
												.getNamespace()))
								.orElse(false),
						anchor, 512, 4);
		require(helper, nearestCakeWorldBiome != null,
				"Could not locate a generated CakeWorld biome for monument Guardian conversion");
		BlockPos monumentAnchor =
				nearestCakeWorldBiome.getFirst();
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(monumentAnchor).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Monument Guardian conversion fixture was not in a CakeWorld biome");
		monumentGuardian.moveTo(monumentAnchor.getX(),
				monumentAnchor.getY(),
				monumentAnchor.getZ(), 31.0F, 0.0F);
		monumentGuardian.setHealth(17.0F);
		monumentGuardian.setCustomName(
				new TextComponent("Bubble Sentinel"));
		monumentGuardian.setPersistenceRequired();
		GumballGuardian converted =
				CakeWorldGuardianReplacement
						.convertIfInCakeWorldBiome(
								helper.getLevel(),
								monumentGuardian);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities
										.GUMBALL_GUARDIAN.get()
						&& close(converted.getHealth(), 17.0D)
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Bubble Sentinel")
						&& converted.isPersistenceRequired()
						&& close(converted.getYRot(), 31.0D),
				"Monument conversion lost Guardian type, health, name, persistence or rotation");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Guardian spawning");
		boolean openOceanGuardian = false;
		boolean openOceanGumball = false;
		for (MobSpawnSettings.SpawnerData spawn :
				sodaOcean.getMobSettings()
						.getMobs(MobCategory.MONSTER).unwrap()) {
			openOceanGuardian |= spawn.type == EntityType.GUARDIAN;
			openOceanGumball |= spawn.type
					== CakeWorldEntities.GUMBALL_GUARDIAN.get();
		}
		require(helper,
				!openOceanGuardian && !openOceanGumball,
				"Structure-only Gumball defenders leaked into normal open-ocean spawning before Soda Palace exists");

		TagKey<EntityType<?>> axolotlHostiles = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_always_hostiles"));
		require(helper,
				CakeWorldEntities.GUMBALL_GUARDIAN.get()
						.is(axolotlHostiles),
				"Gumball Guardian did not preserve Guardian's axolotl-hostile role");
		require(helper,
				CakeWorldItems.GUMBALL_GUARDIAN_SPAWN_EGG
						.isPresent(),
				"Gumball Guardian has no creative/testing spawn egg");
		require(helper,
				guardian.getLootTable().equals(
						new ResourceLocation(CakeWorld.MODID,
								"entities/gumball_guardian")),
				"Gumball Guardian did not resolve its dedicated Guardian-equivalent loot");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2024"),
						"CakeWorldGumballGuardianRoleTest"));
		VanillaRoleAdvancements.creditKilledGuardianRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:guardian");
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
	public static void mallowFloatersUseSafePuffsUntilHard(
			GameTestHelper helper) {
		MallowFloater floater = CakeWorldEntities.MALLOW_FLOATER.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, floater != null && target != null,
				"Could not create Mallow Floater fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 4, 2));
		floater.setNoAi(true);
		floater.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(floater);
		helper.getLevel().addFreshEntity(target);
		CompoundTag floaterState = floater.saveWithoutId(
				new CompoundTag());
		floaterState.putByte("ExplosionPower", (byte)3);
		floater.load(floaterState);
		require(helper,
				floater instanceof Ghast
						&& floater.fireImmune()
						&& floater.getExplosionPower() == 3
						&& Math.abs(floater.getMaxHealth() - 10.0F)
								< 0.001F
						&& Math.abs(floater.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 100.0D)
								< 0.001D
						&& floater.getMaxSpawnClusterSize() == 1,
				"Mallow Floater lost the Ghast fire, explosion, health, range or solitary-spawn role");

		BlockPos protectedBlock = anchor.offset(4, 0, 1);
		helper.getLevel().setBlockAndUpdate(
				protectedBlock, Blocks.GLASS.defaultBlockState());
		ItemEntity protectedItem = new ItemEntity(helper.getLevel(),
				target.getX(), target.getY(), target.getZ() + 1.0D,
				new ItemStack(Items.DIAMOND, 3));
		protectedItem.setDeltaMovement(Vec3.ZERO);
		helper.getLevel().addFreshEntity(protectedItem);

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
				protectedItem.setDeltaMovement(Vec3.ZERO);
				AbstractHurtingProjectile shot =
						floater.createShot(target);
				require(helper, shot instanceof MallowPuffProjectile,
						safeDifficulty
								+ " Mallow Floater did not create a safe visible puff");
				shot.setPos(target.getX(), target.getY(),
						target.getZ());
				helper.getLevel().addFreshEntity(shot);
				((MallowPuffProjectile)shot).burst();
				require(helper,
						shot.isRemoved()
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
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& helper.getLevel().getBlockState(
										protectedBlock).is(Blocks.GLASS)
								&& protectedItem.isAlive()
								&& protectedItem.getItem().getCount() == 3
								&& protectedItem.getDeltaMovement()
										.equals(Vec3.ZERO),
						safeDifficulty
								+ " Mallow Puff caused damage/destruction or lacked rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			AbstractHurtingProjectile hardShot =
					floater.createShot(target);
			LargeFireball hardFireball =
					(LargeFireball)hardShot;
			CompoundTag hardShotState = hardShot.saveWithoutId(
					new CompoundTag());
			target.hurt(DamageSource.fireball(
					hardFireball, floater), 6.0F);
			require(helper,
					hardShot.getClass() == LargeFireball.class
							&& hardShotState.getByte(
									"ExplosionPower") == 3
							&& target.getHealth() < 10.0F,
					"Hard Mallow Floater did not restore a real power-three Ghast fireball");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome fudgeWastes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect the Mallow Floater's Fudge Wastes biome");
		requireSpawnReplacement(helper, fudgeWastes,
				EntityType.GHAST,
				CakeWorldEntities.MALLOW_FLOATER.get(),
				MobCategory.MONSTER);
		require(helper, CakeWorldItems.MALLOW_FLOATER_SPAWN_EGG.isPresent(),
				"Mallow Floater has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2020"),
						"CakeWorldMallowFloaterRoleTest"));
		MallowFloater returned = CakeWorldEntities.MALLOW_FLOATER.get()
				.create(helper.getLevel());
		require(helper, returned != null,
				"Could not create Return to Sender fixture");
		returned.setPos(anchor.getX() - 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(returned);
		MallowPuffProjectile reflected =
				new MallowPuffProjectile(helper.getLevel(),
						returned, 1.0D, 0.0D, 0.0D);
		reflected.setOwner(advancementPlayer);
		require(helper,
				returned.hurt(DamageSource.fireball(
						reflected, advancementPlayer), 1.0F)
						&& !returned.isAlive(),
				"Reflected Mallow Puff did not retain Ghast return-fire defeat");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:ghast");
		requireCriterion(helper, advancementPlayer,
				"minecraft:nether/return_to_sender",
				"killed_ghast");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void giantStaleCrumblersStaySummonOnly(
			GameTestHelper helper) {
		GiantStaleCrumbler giant =
				CakeWorldEntities.GIANT_STALE_CRUMBLER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, giant != null && target != null,
				"Could not create Giant Stale Crumbler fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		giant.setNoAi(true);
		giant.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(giant);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				giant instanceof Giant
						&& Math.abs(giant.getBbWidth() - 3.6F)
								< 0.001F
						&& Math.abs(giant.getBbHeight() - 12.0F)
								< 0.001F
						&& Math.abs(giant.getEyeHeight() - 10.440001F)
								< 0.001F
						&& Math.abs(giant.getMaxHealth() - 100.0F)
								< 0.001F
						&& Math.abs(giant.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.5D)
								< 0.001D
						&& Math.abs(giant.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 50.0D)
								< 0.001D,
				"Giant Stale Crumbler lost the Giant dimensions, eye height or attributes");

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
						giant.doHurtTarget(target)
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
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Giant crumb stomp caused damage or lacked rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget = EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Giant target");
			hardTarget.setPos(anchor.getX() + 2.0D,
					anchor.getY(), anchor.getZ() + 2.0D);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper, giant.doHurtTarget(hardTarget)
							&& !hardTarget.isAlive(),
					"Hard Giant Stale Crumbler did not retain real fifty-point Giant damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Giant vanillaGiant = EntityType.GIANT.create(helper.getLevel());
		require(helper, vanillaGiant != null,
				"Could not create command-summon conversion fixture");
		vanillaGiant.moveTo(anchor.getX() + 5.0D,
				anchor.getY(), anchor.getZ(), 31.0F, 0.0F);
		vanillaGiant.setHealth(42.0F);
		vanillaGiant.setCustomName(new TextComponent("Old Crumb"));
		vanillaGiant.setPersistenceRequired();
		vanillaGiant.setNoAi(true);
		GiantStaleCrumbler converted =
				CakeWorldGiantReplacement.convertIfInCakeWorldBiome(
						helper.getLevel(), vanillaGiant);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities.GIANT_STALE_CRUMBLER.get()
						&& Math.abs(converted.getHealth() - 42.0F)
								< 0.001F
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Old Crumb")
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& Math.abs(converted.getYRot() - 31.0F)
								< 0.001F,
				"Fresh command-Giant conversion lost type, health, name, persistence, NoAI or rotation");

		ResourceLocation[] cakeWorldBiomes = {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId()
		};
		for (ResourceLocation biomeId : cakeWorldBiomes) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Missing CakeWorld biome " + biomeId);
			boolean spawned = biome.getMobSettings()
					.getMobs(MobCategory.MONSTER).unwrap()
					.stream().anyMatch(spawn ->
							spawn.type == EntityType.GIANT
									|| spawn.type
											== CakeWorldEntities
													.GIANT_STALE_CRUMBLER
													.get());
			require(helper, !spawned,
					"Giant role was added to normal spawning in "
							+ biomeId);
		}
		require(helper,
				Registry.ITEM.get(new ResourceLocation(
						CakeWorld.MODID,
						"giant_stale_crumbler_spawn_egg"))
						== Items.AIR,
				"Command-only Giant unexpectedly received a spawn egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		require(helper, killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:giant"),
				"Vanilla unexpectedly requires a Giant kill criterion");
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

	@GameTest(template = EMPTY)
	public static void glowJelliesKeepTheLuminousCaveWaterRoleInLemonade(
			GameTestHelper helper) {
		GlowJelly jelly = CakeWorldEntities.GLOW_JELLY.get()
				.create(helper.getLevel());
		GlowJelly restored = CakeWorldEntities.GLOW_JELLY.get()
				.create(helper.getLevel());
		require(helper, jelly != null && restored != null,
				"Could not create Glow-Jelly fixtures");
		require(helper,
				jelly instanceof GlowSquid
						&& close(jelly.getAttributeValue(
								Attributes.MAX_HEALTH), 10.0D)
						&& jelly.getDarkTicksRemaining() == 0,
				"Glow-Jelly lost the Glow Squid base, health, or lit state");

		BlockPos horizontalAnchor =
				helper.absolutePos(new BlockPos(3, 3, 3));
		BlockPos lemonadePos = new BlockPos(horizontalAnchor.getX(),
				helper.getLevel().getSeaLevel() - 34,
				horizontalAnchor.getZ());
		for (Direction direction : Direction.values()) {
			helper.getLevel().setBlock(lemonadePos.relative(direction),
					Blocks.STONE.defaultBlockState(), 3);
		}
		helper.getLevel().setBlock(lemonadePos,
				CakeWorldFluids.LEMONADE_BLOCK.get()
						.defaultBlockState(), 3);
		require(helper,
				helper.getLevel().getRawBrightness(lemonadePos, 0) == 0,
				"Glow-Jelly darkness fixture was not actually dark");
		boolean vanillaRule = GlowSquid.checkGlowSquideSpawnRules(
				EntityType.GLOW_SQUID, helper.getLevel(),
				MobSpawnType.NATURAL, lemonadePos,
				new Random(1978L));
		boolean jellyRule = GlowJelly.checkGlowJellySpawnRules(
				CakeWorldEntities.GLOW_JELLY.get(), helper.getLevel(),
				MobSpawnType.NATURAL, lemonadePos,
				new Random(1978L));
		require(helper, !vanillaRule && jellyRule,
				"Glow-Jelly did not retain the deep-dark spawn rule while adapting literal water to water-tagged Lemonade");
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.GLOW_JELLY.get())
						== SpawnPlacements.Type.IN_WATER,
				"Glow-Jelly lost its in-water spawn placement");

		jelly.setPos(horizontalAnchor.getX(), horizontalAnchor.getY(),
				horizontalAnchor.getZ());
		helper.getLevel().addFreshEntity(jelly);
		jelly.setHealth(10.0F);
		Player attacker = helper.makeMockPlayer();
		require(helper,
				jelly.hurt(DamageSource.playerAttack(attacker), 1.0F)
						&& jelly.getDarkTicksRemaining() == 100
						&& close(jelly.getHealth(), 9.0D),
				"Glow-Jelly did not squirt luminous ink and darken for 100 ticks after a living attacker injured it");
		CompoundTag saved = new CompoundTag();
		jelly.addAdditionalSaveData(saved);
		restored.readAdditionalSaveData(saved);
		require(helper,
				saved.getInt("DarkTicksRemaining") == 100
						&& restored.getDarkTicksRemaining() == 100,
				"Glow-Jelly did not retain its dark-tick state across save/reload");
		require(helper,
				jelly.getLootTable().equals(new ResourceLocation(
						CakeWorld.MODID, "entities/glow_jelly")),
				"Glow-Jelly did not resolve its dedicated glow-ink loot table");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Glow-Jelly spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.GLOW_SQUID,
				CakeWorldEntities.GLOW_JELLY.get(),
				MobCategory.UNDERGROUND_WATER_CREATURE);
		TagKey<EntityType<?>> axolotlPrey = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_hunt_targets"));
		require(helper, CakeWorldEntities.GLOW_JELLY.get()
						.is(axolotlPrey),
				"Glow-Jelly did not preserve the Glow Squid axolotl-prey role");
		require(helper, CakeWorldItems.GLOW_JELLY_SPAWN_EGG.isPresent(),
				"Glow-Jelly has no creative/testing spawn egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(new ResourceLocation(
						"minecraft", "adventure/kill_all_mobs"));
		require(helper, killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:glow_squid"),
				"Vanilla unexpectedly added a Glow Squid kill criterion; reassess compatibility before inventing a bridge");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void nougatGoatsKeepMountainRamsMilkAndFamilyRoles(
			GameTestHelper helper) {
		NougatGoat goat = CakeWorldEntities.NOUGAT_GOAT.get()
				.create(helper.getLevel());
		NougatGoat partner = CakeWorldEntities.NOUGAT_GOAT.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, goat != null && partner != null && target != null,
				"Could not create Nougat Goat fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		goat.setNoAi(true);
		partner.setNoAi(true);
		goat.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		partner.setPos(anchor.getX() - 2.0D,
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(goat);
		helper.getLevel().addFreshEntity(partner);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				goat instanceof Goat
						&& close(goat.getAttributeValue(
								Attributes.MAX_HEALTH), 10.0D)
						&& close(goat.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.2D)
						&& close(goat.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 2.0D)
						&& close(goat.getDimensions(Pose.LONG_JUMPING)
								.width, 0.63D)
						&& close(goat.getDimensions(Pose.LONG_JUMPING)
								.height, 0.91D),
				"Nougat Goat lost Goat health, movement, ram strength, or long-jump dimensions");

		goat.handleEntityEvent((byte)58);
		goat.aiStep();
		require(helper, goat.getRammingXHeadRot() > 0.0F,
				"Nougat Goat did not retain the visible horned head-lowering ram cue");
		goat.handleEntityEvent((byte)59);
		goat.getBrain().setMemory(MemoryModuleType.RAM_TARGET,
				target.position());
		require(helper, goat.getBrain().hasMemoryValue(
						MemoryModuleType.RAM_TARGET),
				"Nougat Goat brain lost its ram-target memory");
		goat.getBrain().eraseMemory(MemoryModuleType.RAM_TARGET);

		goat.setInLove(null);
		partner.setInLove(null);
		partner.setScreamingGoat(true);
		require(helper, goat.canMate(partner)
						&& !goat.canAttack(partner),
				"Nougat Goats could not mate or would ram their own custom type");
		NougatGoat child = goat.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities.NOUGAT_GOAT.get()
						&& child.isScreamingGoat()
						&& child.getBrain().hasMemoryValue(
								MemoryModuleType
										.LONG_JUMP_COOLDOWN_TICKS)
						&& child.getBrain().hasMemoryValue(
								MemoryModuleType.RAM_COOLDOWN_TICKS),
				"Nougat Goat offspring lost its custom type, screaming inheritance, or exact inherited cooldown roles");
		require(helper,
				goat.isFood(new ItemStack(Items.WHEAT)),
				"Nougat Goat lost the vanilla wheat breeding role");

		float preFallHealth = goat.getHealth();
		goat.causeFallDamage(12.0F, 1.0F, DamageSource.FALL);
		require(helper, close(goat.getHealth(), preFallHealth),
				"Nougat Goat lost the inherited ten-block fall-damage reduction");

		Player milker = helper.makeMockPlayer();
		milker.getAbilities().instabuild = false;
		milker.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.BUCKET));
		InteractionResult milked = goat.mobInteract(
				milker, InteractionHand.MAIN_HAND);
		require(helper, milked.consumesAction()
						&& milker.getMainHandItem()
								.is(Items.MILK_BUCKET),
				"Nougat Goat lost its adult milk-bucket role");

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
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(DamageSource.mobAttack(goat)
								.setNoAggro(),
						(float) goat.getAttributeValue(
								Attributes.ATTACK_DAMAGE));
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement().x > 0.0D
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " Nougat Goat ram caused damage or lacked its cushioned bounce/rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.hurt(DamageSource.mobAttack(goat).setNoAggro(),
					(float) goat.getAttributeValue(
							Attributes.ATTACK_DAMAGE));
			require(helper, target.getHealth() < 10.0F,
					"Hard Nougat Goat did not retain real ram damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		BlockPos icing = helper.absolutePos(new BlockPos(6, 2, 6));
		helper.getLevel().setBlock(icing.below(),
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(2),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.offset(2, 1, 0),
				Blocks.GLOWSTONE.defaultBlockState(), 3);
		BlockPos spawnPos = icing.above();
		boolean vanillaRule = Goat.checkGoatSpawnRules(
				CakeWorldEntities.NOUGAT_GOAT.get(),
				helper.getLevel(), MobSpawnType.NATURAL,
				spawnPos, new Random(1978L));
		boolean vanillaBody = SpawnPlacements.Type.ON_GROUND.canSpawnAt(
				helper.getLevel(), spawnPos,
				CakeWorldEntities.NOUGAT_GOAT.get());
		boolean nougatRule = NougatGoat.checkNougatGoatSpawnRules(
				CakeWorldEntities.NOUGAT_GOAT.get(),
				helper.getLevel(), MobSpawnType.NATURAL,
				spawnPos, new Random(1978L));
		require(helper,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState()
						.is(BlockTags.GOATS_SPAWNABLE_ON)
						&& vanillaRule
						&& !vanillaBody
						&& nougatRule
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities.NOUGAT_GOAT.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& NaturalSpawner.isValidEmptySpawnBlock(
								helper.getLevel(), spawnPos,
								helper.getLevel().getBlockState(
										spawnPos),
								helper.getLevel().getFluidState(
										spawnPos),
								CakeWorldEntities.NOUGAT_GOAT.get()),
				"Nougat Goat did not adapt thin edible icing into a safe, bright Goat spawn surface");

		Biome marshmallowPeaks = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.MARSHMALLOW_PEAKS.getId());
		require(helper, marshmallowPeaks != null,
				"Could not inspect Marshmallow Peaks Nougat Goat spawning");
		requireSpawnReplacement(helper, marshmallowPeaks,
				EntityType.GOAT, CakeWorldEntities.NOUGAT_GOAT.get(),
				MobCategory.CREATURE);
		require(helper, CakeWorldItems.NOUGAT_GOAT_SPAWN_EGG.isPresent(),
				"Nougat Goat has no creative/testing spawn egg");
		require(helper, goat.getLootTable().equals(
						new ResourceLocation(CakeWorld.MODID,
								"entities/nougat_goat")),
				"Nougat Goat did not resolve its dedicated empty Goat loot table");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2023"),
						"CakeWorldNougatGoatRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.NOUGAT_GOAT.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:goat");
		Boat boat = EntityType.BOAT.create(helper.getLevel());
		require(helper, boat != null,
				"Could not create Nougat Goat boat fixture");
		boat.setPos(anchor.getX() - 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(boat);
		require(helper, goat.startRiding(boat, true),
				"Nougat Goat could not retain the boat-passenger role");
		VanillaRoleAdvancements.onMount(new EntityMountEvent(
				advancementPlayer, boat, helper.getLevel(), true));
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/ride_a_boat_with_a_goat",
				"ride_a_boat_with_a_goat");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void fudgeBoarsKeepHuntsBreedingThrowsAndZoglinTransition(
			GameTestHelper helper) {
		FudgeBoar boar = CakeWorldEntities.FUDGE_BOAR.get()
				.create(helper.getLevel());
		FudgeBoar partner = CakeWorldEntities.FUDGE_BOAR.get()
				.create(helper.getLevel());
		Pig safeTarget = EntityType.PIG.create(helper.getLevel());
		require(helper,
				boar != null && partner != null && safeTarget != null,
				"Could not create Fudge Boar fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		boar.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		partner.setPos(anchor.getX() - 2.0D,
				anchor.getY(), anchor.getZ());
		safeTarget.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		boar.setImmuneToZombification(true);
		partner.setImmuneToZombification(true);
		boar.setNoAi(true);
		partner.setNoAi(true);
		helper.getLevel().addFreshEntity(boar);
		helper.getLevel().addFreshEntity(partner);
		helper.getLevel().addFreshEntity(safeTarget);
		require(helper,
				boar instanceof Hoglin
						&& close(boar.getMaxHealth(), 40.0D)
						&& close(boar.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.3D)
						&& close(boar.getAttributeValue(
								Attributes.KNOCKBACK_RESISTANCE),
								0.6D)
						&& close(boar.getAttributeValue(
								Attributes.ATTACK_KNOCKBACK),
								1.0D)
						&& close(boar.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 6.0D)
						&& close(boar.getDimensions(Pose.STANDING)
								.width, 1.3964844D)
						&& close(boar.getDimensions(Pose.STANDING)
								.height, 1.4D),
				"Fudge Boar lost the Hoglin health, movement, resistance, charge, or size contract");
		require(helper,
				boar.canBeHunted()
						&& boar.isFood(new ItemStack(
								Items.CRIMSON_FUNGUS)),
				"Adult Fudge Boar lost its Piglin-hunt or crimson-fungus breeding role");

		boar.setInLove(null);
		partner.setInLove(null);
		require(helper, boar.canMate(partner),
				"Fudge Boars could not recognize their own custom family");
		FudgeBoar child = boar.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities.FUDGE_BOAR.get()
						&& child.isPersistenceRequired(),
				"Fudge Boar mating produced the hard-coded vanilla Hoglin type or lost offspring persistence");
		partner.setBaby(true);
		require(helper, !partner.canBeHunted(),
				"Baby Fudge Boar incorrectly remained huntable");
		boar.resetLove();
		boar.getBrain().setMemory(
				MemoryModuleType.PACIFIED, true);
		require(helper, !boar.canFallInLove(),
				"Fudge Boar ignored inherited Hoglin pacification");
		boar.getBrain().eraseMemory(MemoryModuleType.PACIFIED);

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 12.0F;
				safeTarget.setDeltaMovement(Vec3.ZERO);
				safeTarget.invulnerableTime = 0;
				require(helper, boar.doHurtTarget(safeTarget),
						safeDifficulty
								+ " Fudge Boar did not complete its protected charge");
				Vec3 protectedThrow =
						safeTarget.getDeltaMovement();
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& boar
										.getAttackAnimationRemainingTicks()
										== 10
								&& !safeTarget.isOnFire()
								&& safeTarget.fallDistance == 0.0F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& protectedThrow
										.multiply(1.0D, 0.0D, 1.0D)
										.lengthSqr() > 0.0D,
						safeDifficulty
								+ " Fudge Boar charge caused health damage or lost its telegraph, throw, or rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Fudge Boar target");
			hardTarget.setPos(anchor.getX() + 3.0D,
					anchor.getY(), anchor.getZ());
			hardTarget.setHealth(10.0F);
			hardTarget.setDeltaMovement(Vec3.ZERO);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper,
					boar.doHurtTarget(hardTarget)
							&& hardTarget.getHealth() < 10.0F
							&& hardTarget.getDeltaMovement()
									.multiply(1.0D, 0.0D, 1.0D)
									.lengthSqr() > 0.0D,
					"Hard Fudge Boar did not retain real Hoglin damage and knockback");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Piglin piglin =
				EntityType.PIGLIN.create(helper.getLevel());
		require(helper, piglin != null,
				"Could not create Piglin hunt-sensor fixture");
		piglin.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		piglin.setNoAi(true);
		helper.getLevel().addFreshEntity(piglin);
		NearestLivingEntitySensor nearestSensor =
				new NearestLivingEntitySensor();
		PiglinSpecificSensor piglinSensor =
				new PiglinSpecificSensor();
		for (int scan = 0; scan < 21; ++scan) {
			nearestSensor.tick(helper.getLevel(), piglin);
		}
		for (int scan = 0; scan < 21; ++scan) {
			piglinSensor.tick(helper.getLevel(), piglin);
		}
		require(helper,
				piglin.getBrain().getMemory(
						MemoryModuleType
								.NEAREST_VISIBLE_HUNTABLE_HOGLIN)
						.orElse(null) == boar,
				"Piglin hunt sensor did not classify the Fudge Boar subclass as a huntable Hoglin");

		BlockPos repellent = anchor.offset(0, 0, 3);
		helper.getLevel().setBlock(repellent.below(),
				Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
		helper.getLevel().setBlock(repellent,
				Blocks.WARPED_FUNGUS.defaultBlockState(), 3);
		HoglinSpecificSensor hoglinSensor =
				new HoglinSpecificSensor();
		for (int scan = 0; scan < 21; ++scan) {
			hoglinSensor.tick(helper.getLevel(), boar);
		}
		require(helper,
				boar.getBrain().getMemory(
						MemoryModuleType.NEAREST_REPELLENT)
						.orElse(null).equals(repellent),
				"Fudge Boar lost the inherited Hoglin-repellent sensor");

		BlockPos spawnPos = anchor.offset(5, 0, 5);
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				FudgeBoar.checkFudgeBoarSpawnRules(
						CakeWorldEntities.FUDGE_BOAR.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						spawnPos, new Random(1978L))
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities.FUDGE_BOAR.get())
								== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements.Type.ON_GROUND
								.canSpawnAt(helper.getLevel(),
										spawnPos,
										CakeWorldEntities
												.FUDGE_BOAR.get()),
				"Fudge Boar lost its exact Hoglin ground-spawn contract on Fudge Rock");
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.NETHER_WART_BLOCK.defaultBlockState(), 3);
		require(helper,
				!FudgeBoar.checkFudgeBoarSpawnRules(
						CakeWorldEntities.FUDGE_BOAR.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						spawnPos, new Random(1978L)),
				"Fudge Boar spawned on vanilla Hoglin-excluded Nether Wart Block");
		Biome fudgeWastes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes Fudge Boar spawning");
		requireSpawnReplacement(helper, fudgeWastes,
				EntityType.HOGLIN,
				CakeWorldEntities.FUDGE_BOAR.get(),
				MobCategory.MONSTER);

		CompoundTag saved = new CompoundTag();
		saved.putBoolean("IsImmuneToZombification", true);
		saved.putInt("TimeInOverworld", 123);
		saved.putBoolean("CannotBeHunted", true);
		FudgeBoar restored = CakeWorldEntities.FUDGE_BOAR.get()
				.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create restored Fudge Boar fixture");
		restored.readAdditionalSaveData(saved);
		CompoundTag roundTrip = new CompoundTag();
		restored.addAdditionalSaveData(roundTrip);
		require(helper,
				roundTrip.getBoolean(
						"IsImmuneToZombification")
						&& roundTrip.getInt(
								"TimeInOverworld") == 123
						&& roundTrip.getBoolean(
								"CannotBeHunted")
						&& !restored.canBeHunted()
						&& !restored.isConverting(),
				"Fudge Boar did not preserve immunity, conversion time, or huntability across NBT");

		FudgeBoar converting =
				CakeWorldEntities.FUDGE_BOAR.get()
						.create(helper.getLevel());
		require(helper, converting != null,
				"Could not create Fudge Boar conversion fixture");
		CompoundTag conversion = new CompoundTag();
		conversion.putInt("TimeInOverworld", 300);
		converting.readAdditionalSaveData(conversion);
		BlockPos conversionPos = anchor.offset(0, 0, 6);
		converting.setPos(conversionPos.getX(),
				conversionPos.getY(), conversionPos.getZ());
		helper.getLevel().addFreshEntity(converting);
		require(helper, converting.isConverting(),
				"Unprotected Overworld Fudge Boar did not enter the inherited zombification countdown");
		converting.tick();
		Zoglin converted = helper.getLevel()
				.getEntitiesOfClass(Zoglin.class,
						new AABB(conversionPos).inflate(2.0D))
				.stream().findFirst().orElse(null);
		require(helper,
				converting.isRemoved()
						&& converted != null
						&& converted.getType() == EntityType.ZOGLIN
						&& converted.hasEffect(
								MobEffects.CONFUSION),
				"Fudge Boar did not preserve the documented transitional conversion to vanilla Zoglin");

		require(helper,
				CakeWorldItems.FUDGE_BOAR_SPAWN_EGG.isPresent()
						&& boar.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/fudge_boar")),
				"Fudge Boar lost its spawn egg or dedicated Hoglin-equivalent loot table");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2025"),
						"CakeWorldFudgeBoarRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.FUDGE_BOAR.get());
		VanillaRoleAdvancements.creditKilledHoglinRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:hoglin");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:hoglin");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gingerbreadPoniesKeepGeneticsArmorJumpAndTaming(
			GameTestHelper helper) {
		GingerbreadPony first =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		GingerbreadPony second =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, first != null && second != null,
				"Could not create Gingerbread Pony breeding fixtures");

		CompoundTag firstAppearance = new CompoundTag();
		firstAppearance.putInt("Variant",
				Variant.BLACK.getId()
						| Markings.WHITE_DOTS.getId() << 8);
		first.readAdditionalSaveData(firstAppearance);
		CompoundTag secondAppearance = new CompoundTag();
		secondAppearance.putInt("Variant",
				Variant.CREAMY.getId()
						| Markings.NONE.getId() << 8);
		second.readAdditionalSaveData(secondAppearance);
		for (GingerbreadPony parent :
				new GingerbreadPony[] {first, second}) {
			parent.getAttribute(Attributes.MAX_HEALTH)
					.setBaseValue(24.0D);
			parent.getAttribute(Attributes.JUMP_STRENGTH)
					.setBaseValue(0.8D);
			parent.getAttribute(Attributes.MOVEMENT_SPEED)
					.setBaseValue(0.25D);
			parent.setHealth(parent.getMaxHealth());
			parent.setTamed(true);
			parent.setInLove(null);
		}
		require(helper, first.canMate(second),
				"Two adult, healthy, tame Gingerbread Ponies could not mate");
		AgeableMob offspring = first.getBreedOffspring(
				helper.getLevel(), second);
		require(helper,
				offspring instanceof GingerbreadPony child
						&& child.getType()
								== CakeWorldEntities
										.GINGERBREAD_PONY.get()
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH) >= 21.0D
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH) <= 26.0D
						&& child.getAttributeBaseValue(
								Attributes.JUMP_STRENGTH)
								>= 0.666D
						&& child.getAttributeBaseValue(
								Attributes.JUMP_STRENGTH)
								<= 0.867D
						&& child.getAttributeBaseValue(
								Attributes.MOVEMENT_SPEED)
								>= 0.204D
						&& child.getAttributeBaseValue(
								Attributes.MOVEMENT_SPEED)
								<= 0.280D
						&& child.getVariant() != null
						&& child.getMarkings() != null,
				"Gingerbread Pony offspring lost its custom type or vanilla Horse appearance/attribute inheritance");

		DoughDonkey donkey =
				CakeWorldEntities.DOUGH_DONKEY.get()
						.create(helper.getLevel());
		require(helper, donkey != null,
				"Could not create staged Marzipan Mule parent");
		donkey.setTamed(true);
		donkey.setHealth(donkey.getMaxHealth());
		donkey.setInLove(null);
		require(helper,
				first.canMate(donkey)
						&& donkey.canMate(first),
				"Gingerbread Pony and Dough Donkey lost Horse-family mate compatibility");
		AgeableMob ponySideMule = first.getBreedOffspring(
				helper.getLevel(), donkey);
		AgeableMob donkeySideMule = donkey.getBreedOffspring(
				helper.getLevel(), first);
		require(helper,
				ponySideMule instanceof MarzipanMule
						&& ponySideMule.getType()
								== CakeWorldEntities.MARZIPAN_MULE.get()
						&& donkeySideMule instanceof MarzipanMule
						&& donkeySideMule.getType()
								== CakeWorldEntities.MARZIPAN_MULE.get(),
				"Pony/Donkey crossbreeding did not produce Marzipan Mule from both parent directions");

		GingerbreadPony mount =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, mount != null,
				"Could not create Gingerbread Pony mount fixture");
		require(helper,
				mount instanceof Horse
						&& close(mount.getMaxHealth(), 53.0D)
						&& close(mount.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.225D)
						&& close(mount.getDimensions(Pose.STANDING)
								.width, 1.3964844D)
						&& close(mount.getDimensions(Pose.STANDING)
								.height, 1.6D)
						&& mount.canWearArmor()
						&& mount.getMaxTemper() == 100,
				"Gingerbread Pony lost the registered Horse attributes, size, armour, or taming contract");
		require(helper,
				mount.isFood(new ItemStack(Items.WHEAT))
						&& mount.isFood(new ItemStack(
								Items.SUGAR))
						&& mount.isFood(new ItemStack(
								Items.HAY_BLOCK))
						&& mount.isFood(new ItemStack(
								Items.APPLE))
						&& mount.isFood(new ItemStack(
								Items.GOLDEN_CARROT))
						&& mount.isFood(new ItemStack(
								Items.GOLDEN_APPLE))
						&& mount.isFood(new ItemStack(
								Items.ENCHANTED_GOLDEN_APPLE)),
				"Gingerbread Pony lost the full vanilla Horse food and breeding set");

		CompoundTag mountAppearance = new CompoundTag();
		mountAppearance.putInt("Variant",
				Variant.BROWN.getId()
						| Markings.WHITE_FIELD.getId() << 8);
		mount.readAdditionalSaveData(mountAppearance);
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		mount.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		helper.getLevel().addFreshEntity(mount);
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2026"),
						"CakeWorldGingerbreadPonyRoleTest"));
		require(helper, mount.tameWithName(advancementPlayer)
						&& mount.isTamed()
						&& advancementPlayer.getUUID().equals(
								mount.getOwnerUUID()),
				"Gingerbread Pony did not retain actual Horse taming and ownership");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/tame_an_animal",
				"tamed_animal");
		require(helper,
				mount.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& mount.isSaddled()
						&& mount.getSlot(401).set(
								new ItemStack(
										Items
												.DIAMOND_HORSE_ARMOR))
						&& mount.getArmor().is(
								Items.DIAMOND_HORSE_ARMOR)
						&& close(mount.getAttributeValue(
								Attributes.ARMOR), 11.0D),
				"Gingerbread Pony could not equip its vanilla saddle and Diamond Horse Armour");

		Player rider = helper.makeMockPlayer();
		rider.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		InteractionResult rideResult = mount.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper,
				rideResult.consumesAction()
						&& rider.getVehicle() == mount
						&& mount.hasPassenger(rider)
						&& mount.canBeControlledByRider()
						&& mount.canJump(),
				"A tame, saddled Gingerbread Pony did not accept or expose control to its rider");
		mount.handleStartJump(90);
		require(helper, mount.isStanding(),
				"Gingerbread Pony lost its visible charged-jump cue");
		rider.stopRiding();

		CompoundTag saved = new CompoundTag();
		mount.addAdditionalSaveData(saved);
		GingerbreadPony restored =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Gingerbread Pony reload fixture");
		restored.readAdditionalSaveData(saved);
		require(helper,
				restored.isTamed()
						&& advancementPlayer.getUUID().equals(
								restored.getOwnerUUID())
						&& restored.isSaddled()
						&& restored.getArmor().is(
								Items.DIAMOND_HORSE_ARMOR)
						&& restored.getVariant()
								== Variant.BROWN
						&& restored.getMarkings()
								== Markings.WHITE_FIELD,
				"Gingerbread Pony lost owner, tame, saddle, armour, variant, or markings across NBT");

		BlockPos icing = anchor.offset(5, 0, 5);
		helper.getLevel().setBlock(icing.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(2),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				icing.offset(2, 1, 0),
				Blocks.GLOWSTONE.defaultBlockState(), 3);
		BlockPos spawnPos = icing.above();
		boolean vanillaRule = Animal.checkAnimalSpawnRules(
				CakeWorldEntities.GINGERBREAD_PONY.get(),
				helper.getLevel(), MobSpawnType.NATURAL,
				spawnPos, new Random(1978L));
		boolean vanillaBody =
				SpawnPlacements.Type.ON_GROUND.canSpawnAt(
						helper.getLevel(), spawnPos,
						CakeWorldEntities
								.GINGERBREAD_PONY.get());
		boolean ponyRule =
				GingerbreadPony
						.checkGingerbreadPonySpawnRules(
								CakeWorldEntities
										.GINGERBREAD_PONY.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								spawnPos,
								new Random(1978L));
		require(helper,
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState()
						.is(BlockTags.ANIMALS_SPAWNABLE_ON)
						&& CakeWorldBlocks.ICING_LAYER.get()
								.defaultBlockState()
								.is(BlockTags
										.ANIMALS_SPAWNABLE_ON)
						&& vanillaRule
						&& !vanillaBody
						&& ponyRule
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.GINGERBREAD_PONY.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& NaturalSpawner
								.isValidEmptySpawnBlock(
										helper.getLevel(),
										spawnPos,
										helper.getLevel()
												.getBlockState(
														spawnPos),
										helper.getLevel()
												.getFluidState(
														spawnPos),
										CakeWorldEntities
												.GINGERBREAD_PONY
												.get()),
				"Gingerbread Pony did not adapt thin edible icing into a safe, bright Horse spawn surface");

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Gingerbread Pony spawning");
		requireSpawnReplacement(helper, candyPlains,
				EntityType.HORSE,
				CakeWorldEntities.GINGERBREAD_PONY.get(),
				MobCategory.CREATURE);
		require(helper,
				CakeWorldItems.GINGERBREAD_PONY_SPAWN_EGG
						.isPresent()
						&& mount.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/gingerbread_pony")),
				"Gingerbread Pony lost its spawn egg or dedicated Horse-equivalent loot table");
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.GINGERBREAD_PONY.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:horse");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void marzipanMulesKeepHybridPacksMountsAndSterility(
			GameTestHelper helper) {
		GingerbreadPony pony =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		DoughDonkey donkey =
				CakeWorldEntities.DOUGH_DONKEY.get()
						.create(helper.getLevel());
		MarzipanMule role =
				CakeWorldEntities.MARZIPAN_MULE.get()
						.create(helper.getLevel());
		require(helper, pony != null && donkey != null
						&& role != null,
				"Could not create Marzipan Mule role fixtures");

		pony.getAttribute(Attributes.MAX_HEALTH)
				.setBaseValue(24.0D);
		pony.getAttribute(Attributes.JUMP_STRENGTH)
				.setBaseValue(0.8D);
		pony.getAttribute(Attributes.MOVEMENT_SPEED)
				.setBaseValue(0.25D);
		donkey.getAttribute(Attributes.MAX_HEALTH)
				.setBaseValue(30.0D);
		donkey.getAttribute(Attributes.JUMP_STRENGTH)
				.setBaseValue(0.6D);
		donkey.getAttribute(Attributes.MOVEMENT_SPEED)
				.setBaseValue(0.18D);
		for (net.minecraft.world.entity.animal.horse.AbstractHorse parent :
				new net.minecraft.world.entity.animal.horse.AbstractHorse[] {
						pony, donkey}) {
			parent.setHealth(parent.getMaxHealth());
			parent.setTamed(true);
			parent.setInLove(null);
		}
		require(helper, pony.canMate(donkey)
						&& donkey.canMate(pony),
				"Marzipan Mule parents lost bidirectional Horse-family compatibility");
		AgeableMob ponyResult = pony.getBreedOffspring(
				helper.getLevel(), donkey);
		AgeableMob donkeyResult = donkey.getBreedOffspring(
				helper.getLevel(), pony);
		require(helper,
				hasInheritedMuleAttributes(ponyResult)
						&& hasInheritedMuleAttributes(
								donkeyResult),
				"Marzipan Mule offspring lost custom type or vanilla hybrid physical inheritance");

		require(helper,
				role instanceof Mule
						&& role.getType()
								== CakeWorldEntities
										.MARZIPAN_MULE.get()
						&& close(role.getMaxHealth(), 53.0D)
						&& close(role.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.175D)
						&& close(role.getAttributeValue(
								Attributes.JUMP_STRENGTH),
								0.5D)
						&& close(role.getDimensions(Pose.STANDING)
								.width, 1.3964844D)
						&& close(role.getDimensions(Pose.STANDING)
								.height, 1.6D)
						&& role.getMaxTemper() == 100
						&& role.getMaxSpawnClusterSize() == 6
						&& role.getInventoryColumns() == 5
						&& !role.canWearArmor(),
				"Marzipan Mule lost exact Mule attributes, body, temper, cluster, pack, or armour roles");
		role.setTamed(true);
		role.setHealth(role.getMaxHealth());
		role.setInLove(null);
		require(helper,
				!role.canMate(pony)
						&& !pony.canMate(role)
						&& role.getBreedOffspring(
								helper.getLevel(), pony)
								instanceof MarzipanMule,
				"Marzipan Mule lost sterility or leaked a literal Mule through its unreachable offspring factory");
		require(helper,
				role.isFood(new ItemStack(Items.WHEAT))
						&& role.isFood(new ItemStack(Items.SUGAR))
						&& role.isFood(new ItemStack(
								Items.HAY_BLOCK))
						&& role.isFood(new ItemStack(Items.APPLE))
						&& role.isFood(new ItemStack(
								Items.GOLDEN_CARROT))
						&& role.isFood(new ItemStack(
								Items.GOLDEN_APPLE))
						&& role.isFood(new ItemStack(
								Items.ENCHANTED_GOLDEN_APPLE)),
				"Marzipan Mule lost the full vanilla Horse-family food set");

		Player rider = helper.makeMockPlayer();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		role.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		role.setOwnerUUID(rider.getUUID());
		role.setTamed(true);
		role.setHealth(role.getMaxHealth());
		helper.getLevel().addFreshEntity(role);
		rider.getAbilities().instabuild = false;
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.CHEST));
		InteractionResult chestResult = role.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, chestResult.consumesAction()
						&& role.hasChest()
						&& rider.getMainHandItem().isEmpty()
						&& role.getSlot(500).set(
								new ItemStack(Items.DIAMOND, 3))
						&& role.getSlot(514).set(
								new ItemStack(Items.GOLD_INGOT, 2)),
				"Marzipan Mule did not equip a chest or expose all fifteen pack slots");
		require(helper, role.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& role.isSaddled(),
				"Marzipan Mule could not equip its saddle");
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		InteractionResult rideResult = role.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, rideResult.consumesAction()
						&& rider.getVehicle() == role
						&& role.hasPassenger(rider)
						&& role.canBeControlledByRider()
						&& role.canJump(),
				"Marzipan Mule lost tame saddle riding, rider control, or charged-jump support");
		role.onPlayerJump(90);
		role.handleStartJump(90);
		require(helper, role.isStanding(),
				"Marzipan Mule lost its visible full-charge jump cue");
		rider.stopRiding();

		role.setHealth(role.getMaxHealth());
		float healthBeforeFall = role.getHealth();
		require(helper,
				role.causeFallDamage(8.0F, 1.0F,
						DamageSource.FALL)
						&& role.getHealth()
								< healthBeforeFall,
				"Marzipan Mule incorrectly removed the real environmental fall hazard");
		CompoundTag saved = role.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		MarzipanMule restored =
				CakeWorldEntities.MARZIPAN_MULE.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Marzipan Mule reload fixture");
		restored.load(saved);
		require(helper,
				restored.isTamed()
						&& rider.getUUID().equals(
								restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.isSaddled()
						&& restored.getSlot(500).get()
								.is(Items.DIAMOND)
						&& restored.getSlot(500).get()
								.getCount() == 3
						&& restored.getSlot(514).get()
								.is(Items.GOLD_INGOT)
						&& restored.getSlot(514).get()
								.getCount() == 2,
				"Marzipan Mule lost owner, tame, chest, saddle, or edge pack slots across save/load");

		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Marzipan Mule spawn boundary for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.CREATURE)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type == EntityType.MULE
									|| spawn.type
											== CakeWorldEntities
													.MARZIPAN_MULE
													.get());
			require(helper, !leaked,
					"Marzipan Mule leaked into open-biome spawning at "
							+ biomeId);
		}
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.MARZIPAN_MULE.get())
								== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.MARZIPAN_MULE.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.MARZIPAN_MULE_SPAWN_EGG
								.isPresent()
						&& role.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/marzipan_mule")),
				"Marzipan Mule lost exact placement, egg, or dedicated Mule-equivalent loot");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2032"),
						"CakeWorldMarzipanMuleRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.MARZIPAN_MULE.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:mule");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void driedCrumblersKeepDaylightDustAndThemedWaterConversion(
			GameTestHelper helper) {
		DriedCrumbler crumbler =
				CakeWorldEntities.DRIED_CRUMBLER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, crumbler != null && target != null,
				"Could not create Dried Crumbler test fixtures");
		require(helper,
				crumbler instanceof Husk
						&& close(crumbler.getMaxHealth(), 20.0D)
						&& close(crumbler.getAttributeValue(
								Attributes.FOLLOW_RANGE), 35.0D)
						&& close(crumbler.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.23D)
						&& close(crumbler.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 3.0D)
						&& close(crumbler.getAttributeValue(
								Attributes.ARMOR), 2.0D)
						&& crumbler.getAttribute(
								Attributes
										.SPAWN_REINFORCEMENTS_CHANCE)
								!= null
						&& crumbler.getMobType()
								== MobType.UNDEAD
						&& close(crumbler
								.getDimensions(Pose.STANDING)
								.width, 0.6D)
						&& close(crumbler
								.getDimensions(Pose.STANDING)
								.height, 1.95D),
				"Dried Crumbler lost its exact Husk size, type, or Zombie attributes");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						crumbler.doHurtTarget(target),
						safeDifficulty
								+ " Dried Crumbler contact did not register");
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.CONFUSION)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& !target.hasEffect(
										MobEffects.HUNGER)
								&& target.getDeltaMovement().y
										> 0.0D,
						safeDifficulty
								+ " Dried Crumbler caused damage, starvation risk, or lacked its dust/rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			require(helper,
					crumbler.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& target.hasEffect(
									MobEffects.HUNGER),
					"Hard Dried Crumbler did not retain real Husk damage and Hunger");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			DriedCrumbler peaceful =
					CakeWorldEntities.DRIED_CRUMBLER.get()
							.create(helper.getLevel());
			require(helper, peaceful != null,
					"Could not create Peaceful Dried Crumbler fixture");
			peaceful.checkDespawn();
			require(helper, peaceful.isRemoved(),
					"Dried Crumbler did not retain Peaceful monster despawning");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		require(helper, !crumbler.isSunSensitive(),
				"Dried Crumbler lost Husk's explicit daylight-safe contract");

		BlockPos darkSpawn = helper.absolutePos(
				new BlockPos(12, 4, 12));
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				helper.getLevel().setBlock(
						darkSpawn.offset(x, -1, z),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(x, 2, z),
						Blocks.STONE.defaultBlockState(), 3);
			}
		}
		for (int y = 0; y <= 1; y++) {
			for (int edge = -2; edge <= 2; edge++) {
				helper.getLevel().setBlock(
						darkSpawn.offset(-2, y, edge),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(2, y, edge),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(edge, y, -2),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(edge, y, 2),
						Blocks.STONE.defaultBlockState(), 3);
			}
		}
		helper.getLevel().setBlock(darkSpawn,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(darkSpawn.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.DRIED_CRUMBLER.get())
						== SpawnPlacements.Type.ON_GROUND
						&& DriedCrumbler
								.checkDriedCrumblerSpawnRules(
										CakeWorldEntities
												.DRIED_CRUMBLER
												.get(),
										helper.getLevel(),
										MobSpawnType.SPAWNER,
										darkSpawn,
										new Random(1978L))
						&& !DriedCrumbler
								.checkDriedCrumblerSpawnRules(
										CakeWorldEntities
												.DRIED_CRUMBLER
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										darkSpawn,
										new Random(1978L)),
				"Dried Crumbler lost its ground placement or exact Husk sky-visible natural-spawn boundary");

		DriedCrumbler converting =
				CakeWorldEntities.DRIED_CRUMBLER.get()
						.create(helper.getLevel());
		require(helper, converting != null,
				"Could not create Dried Crumbler conversion fixture");
		converting.setCustomName(new TextComponent(
				"Dry as a Biscuit"));
		converting.setPersistenceRequired();
		converting.setBaby(true);
		CompoundTag conversion = new CompoundTag();
		conversion.putBoolean("IsBaby", true);
		conversion.putBoolean("CanBreakDoors", true);
		conversion.putBoolean("PersistenceRequired", true);
		conversion.putInt("InWaterTime", 600);
		conversion.putInt("DrownedConversionTime", 0);
		converting.readAdditionalSaveData(conversion);
		CompoundTag conversionRoundTrip = new CompoundTag();
		converting.addAdditionalSaveData(
				conversionRoundTrip);
		require(helper,
				converting.isUnderWaterConverting()
						&& conversionRoundTrip.getInt(
								"InWaterTime") == -1
						&& conversionRoundTrip.getInt(
								"DrownedConversionTime") == 0,
				"Dried Crumbler did not preserve its active water-conversion countdown");
		BlockPos conversionPos = helper.absolutePos(
				new BlockPos(10, 4, 4));
		converting.setPos(conversionPos.getX(),
				conversionPos.getY(), conversionPos.getZ());
		helper.getLevel().addFreshEntity(converting);
		converting.tick();
		StaleCrumbler converted = helper.getLevel()
				.getEntitiesOfClass(StaleCrumbler.class,
						new AABB(conversionPos).inflate(2.0D))
				.stream().findFirst().orElse(null);
		require(helper,
				converting.isRemoved()
						&& converted != null
						&& converted.getType()
								== CakeWorldEntities
										.STALE_CRUMBLER.get()
						&& converted.isBaby()
						&& converted.isPersistenceRequired()
						&& converted.hasCustomName()
						&& "Dry as a Biscuit".equals(
								converted.getCustomName()
										.getString())
						&& converted.canBreakDoors(),
				"Dried Crumbler leaked a vanilla Zombie or lost its baby, name, persistence, or door state during water conversion");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ResourceLocation sherbetDunes = new ResourceLocation(
				CakeWorld.MODID, "sherbet_dunes");
		require(helper, biomes.get(sherbetDunes) == null,
				"Sherbet Dunes unexpectedly exists; MOB-027's staged spawn gate must be revisited");
		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId()}) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect staged Dried Crumbler biome gate for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.MONSTER)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type == EntityType.HUSK
									|| spawn.type
											== CakeWorldEntities
													.DRIED_CRUMBLER
													.get());
			require(helper, !leaked,
					"Dried Crumbler or vanilla Husk leaked into existing biome "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.DRIED_CRUMBLER_SPAWN_EGG
						.isPresent()
						&& crumbler.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/dried_crumbler")),
				"Dried Crumbler lost its spawn egg or dedicated Husk-equivalent loot table");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2027"),
						"CakeWorldDriedCrumblerRoleTest"));
		VanillaRoleAdvancements.creditKilledHuskRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:husk");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void mirageConfectionersKeepCommandSpellsAndSafeSweets(
			GameTestHelper helper) {
		MirageConfectioner confectioner =
				CakeWorldEntities.MIRAGE_CONFECTIONER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		Evoker illagerColleague =
				EntityType.EVOKER.create(helper.getLevel());
		require(helper,
				confectioner != null && target != null
						&& illagerColleague != null,
				"Could not create Mirage Confectioner fixtures");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		confectioner.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		target.setPos(anchor.getX() + 7.0D,
				anchor.getY(), anchor.getZ());
		confectioner.finalizeSpawn(helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						anchor),
				MobSpawnType.COMMAND, null, null);
		AABB culling = confectioner
				.getBoundingBoxForCulling();
		require(helper,
				confectioner instanceof Illusioner
						&& confectioner.getMobType()
								== MobType.ILLAGER
						&& close(confectioner
								.getMaxHealth(), 32.0D)
						&& close(confectioner
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.5D)
						&& close(confectioner
								.getAttribute(
										Attributes.FOLLOW_RANGE)
								.getBaseValue(),
								18.0D)
						&& close(confectioner
								.getDimensions(Pose.STANDING)
								.width, 0.6D)
						&& close(confectioner
								.getDimensions(Pose.STANDING)
								.height, 1.95D),
				"Mirage Confectioner lost Illusioner type, attributes, or dimensions: "
						+ "instanceof=" + (confectioner instanceof Illusioner)
						+ ", mobType=" + confectioner.getMobType()
						+ ", health=" + confectioner.getMaxHealth()
						+ ", speed=" + confectioner.getAttributeValue(
								Attributes.MOVEMENT_SPEED)
						+ ", follow=" + confectioner.getAttributeValue(
								Attributes.FOLLOW_RANGE)
						+ ", followBase=" + confectioner.getAttribute(
								Attributes.FOLLOW_RANGE).getBaseValue()
						+ ", width=" + confectioner
								.getDimensions(Pose.STANDING).width
						+ ", height=" + confectioner
								.getDimensions(Pose.STANDING).height);
		require(helper,
				close(culling.getXsize(), 6.6D)
						&& close(culling.getZsize(), 6.6D),
				"Mirage Confectioner lost Illusioner's expanded four-copy culling box: "
						+ culling);
		require(helper,
				confectioner.getItemBySlot(
						EquipmentSlot.MAINHAND)
						.is(Items.BOW),
				"Mirage Confectioner command initialization did not equip the Illusioner bow");
		require(helper, confectioner.canJoinRaid(),
				"Mirage Confectioner command initialization lost the Raider join flag");
		require(helper,
				confectioner.isAlliedTo(illagerColleague),
				"Mirage Confectioner lost unteamed Illager alliance");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				int arrowsBefore = helper.getLevel()
						.getEntitiesOfClass(
								AbstractArrow.class,
								new AABB(anchor)
										.inflate(32.0D))
						.size();
				confectioner.performRangedAttack(
						target, 1.0F);
				MirageSweetProjectile sweet =
						helper.getLevel()
								.getEntitiesOfClass(
										MirageSweetProjectile.class,
										new AABB(anchor)
												.inflate(32.0D),
										projectile -> projectile
												.getOwner()
												== confectioner)
								.stream().findFirst()
								.orElse(null);
				require(helper,
						sweet != null
								&& helper.getLevel()
										.getEntitiesOfClass(
												AbstractArrow.class,
												new AABB(
														anchor)
														.inflate(
																32.0D))
										.size()
										== arrowsBefore,
						safeDifficulty
								+ " Mirage Confectioner did not substitute its real arrow with a safe visible sweet");
				sweet.applyHarmlessMirage(target);
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.BLINDNESS)
								&& target.hasEffect(
										MobEffects.CONFUSION)
								&& target.hasEffect(
										MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Mirage Sweet caused damage or lacked obscuring/rescue effects");
				sweet.discard();
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			confectioner.performRangedAttack(target, 1.0F);
			AbstractArrow arrow = helper.getLevel()
					.getEntitiesOfClass(AbstractArrow.class,
							new AABB(anchor).inflate(32.0D),
							projectile -> projectile
									.getOwner()
									== confectioner)
					.stream().findFirst().orElse(null);
			require(helper, arrow != null,
					"Hard Mirage Confectioner did not retain the genuine Illusioner arrow path");
			target.hurt(DamageSource.arrow(
					arrow, confectioner), 4.0F);
			require(helper, target.getHealth() < 10.0F,
					"Hard Mirage Confectioner arrow did not retain real damage");
			arrow.discard();

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			MirageConfectioner peaceful =
					CakeWorldEntities
							.MIRAGE_CONFECTIONER.get()
							.create(helper.getLevel());
			require(helper, peaceful != null,
					"Could not create Peaceful Mirage Confectioner fixture");
			peaceful.checkDespawn();
			require(helper, peaceful.isRemoved(),
					"Mirage Confectioner did not retain Peaceful monster despawning");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		target.setInvulnerable(true);
		confectioner.setTarget(target);
		helper.getLevel().addFreshEntity(confectioner);
		helper.getLevel().addFreshEntity(target);
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			for (int i = 0; i < 50; i++) {
				confectioner.tick();
			}
			require(helper,
					confectioner.hasEffect(
							MobEffects.INVISIBILITY),
					"Mirage Confectioner did not retain the inherited mirror/invisibility spell");
			for (int i = 0; i < 30; i++) {
				confectioner.tick();
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeEffect(MobEffects.BLINDNESS);
			for (int i = 0; i < 50; i++) {
				confectioner.tick();
			}
			require(helper,
					target.hasEffect(MobEffects.BLINDNESS),
					"Mirage Confectioner did not retain Illusioner's Hard-only blindness spell");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
			target.setInvulnerable(false);
		}

		var nearestCakeWorldBiome =
				helper.getLevel().findNearestBiome(
						holder -> holder.unwrapKey()
								.map(key -> CakeWorld.MODID
										.equals(key.location()
												.getNamespace()))
								.orElse(false),
						anchor, 512, 4);
		require(helper, nearestCakeWorldBiome != null,
				"Could not locate CakeWorld terrain for Illusioner command conversion");
		BlockPos conversionPos =
				nearestCakeWorldBiome.getFirst();
		Illusioner literal =
				EntityType.ILLUSIONER.create(
						helper.getLevel());
		Ravager ravager =
				EntityType.RAVAGER.create(helper.getLevel());
		require(helper, literal != null && ravager != null,
				"Could not create Illusioner raid-conversion fixtures");
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		literal.setHealth(23.0F);
		literal.setCustomName(new TextComponent(
				"Four of a Kind"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.BOW));
		CompoundTag spellState = literal.saveWithoutId(
				new CompoundTag());
		spellState.putInt("SpellTicks", 25);
		spellState.putBoolean("CanJoinRaid", true);
		literal.load(spellState);
		ravager.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		helper.getLevel().addFreshEntity(ravager);
		Raid raid = new Raid(197828, helper.getLevel(),
				conversionPos);
		raid.joinRaid(2, literal, null, true);
		raid.setLeader(2, literal);
		helper.getLevel().addFreshEntity(literal);
		literal.startRiding(ravager, true);
		MirageConfectioner converted =
				CakeWorldIllusionerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.MIRAGE_CONFECTIONER
										.get()
						&& close(converted.getHealth(), 23.0D)
						&& converted.hasCustomName()
						&& "Four of a Kind".equals(
								converted.getCustomName()
										.getString())
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isCastingSpell()
						&& close(converted.getYRot(), 29.0D)
						&& converted.getItemBySlot(
								EquipmentSlot.MAINHAND)
								.is(Items.BOW)
						&& converted.getCurrentRaid() == raid
						&& converted.getWave() == 2
						&& raid.getLeader(2) == converted
						&& raid.getTotalRaidersAlive() == 1
						&& converted.getVehicle() == ravager,
				"Literal Illusioner conversion lost type, NBT, spell, command bow, raid, leader, or Ravager state");

		TagKey<EntityType<?>> raiders = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"raiders"));
		require(helper,
				CakeWorldEntities.MIRAGE_CONFECTIONER
						.get().is(raiders),
				"Mirage Confectioner did not preserve the vanilla raider tag role");
		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect command-only Mirage Confectioner boundary for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.MONSTER)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type
									== EntityType.ILLUSIONER
									|| spawn.type
											== CakeWorldEntities
													.MIRAGE_CONFECTIONER
													.get());
			require(helper, !leaked,
					"Command-only Illusioner role leaked into biome spawning at "
							+ biomeId);
		}
		ResourceLocation eggId = new ResourceLocation(
				CakeWorld.MODID,
				"mirage_confectioner_spawn_egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities
								.MIRAGE_CONFECTIONER.get())
						== SpawnPlacements.Type
								.NO_RESTRICTIONS
						&& Registry.ITEM.getOptional(eggId)
								.isEmpty()
						&& killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:illusioner")
						&& converted.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/mirage_confectioner")),
				"Mirage Confectioner lost empty loot or fabricated a spawn placement, egg, or nonexistent advancement criterion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void jawbreakerGuardiansKeepSettlementDefenceAndSafeLaunches(
			GameTestHelper helper) {
		JawbreakerGuardian guardian =
				CakeWorldEntities.JAWBREAKER_GUARDIAN
						.get().create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, guardian != null && target != null,
				"Could not create Jawbreaker Guardian fixtures");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		guardian.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		require(helper,
				guardian instanceof IronGolem
						&& guardian.getType()
								== CakeWorldEntities
										.JAWBREAKER_GUARDIAN
										.get()
						&& guardian.getType().getCategory()
								== MobCategory.MISC
						&& close(guardian.getMaxHealth(),
								100.0D)
						&& close(guardian.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.25D)
						&& close(guardian.getAttributeValue(
								Attributes
										.KNOCKBACK_RESISTANCE),
								1.0D)
						&& close(guardian.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								15.0D)
						&& close(guardian
								.getDimensions(Pose.STANDING)
								.width, 1.4D)
						&& close(guardian
								.getDimensions(Pose.STANDING)
								.height, 2.7D)
						&& close(guardian.maxUpStep, 1.0D)
						&& !guardian.causeFallDamage(
								30.0F, 1.0F,
								DamageSource.FALL)
						&& !guardian.removeWhenFarAway(
								10000.0D),
				"Jawbreaker Guardian lost Iron Golem type, attributes, body, step, fall, or persistence roles");
		guardian.setPlayerCreated(true);
		require(helper,
				guardian.isPlayerCreated()
						&& !guardian.canAttackType(
								EntityType.PLAYER)
						&& !guardian.canAttackType(
								EntityType.CREEPER)
						&& guardian.canAttackType(
								EntityType.ZOMBIE),
				"Jawbreaker Guardian lost player-created loyalty or hostile/Creeper targeting rules");

		guardian.setHealth(74.0F);
		require(helper,
				guardian.getCrackiness()
						== IronGolem.Crackiness.LOW,
				"Jawbreaker Guardian lost low crack state");
		guardian.setHealth(49.0F);
		require(helper,
				guardian.getCrackiness()
						== IronGolem.Crackiness.MEDIUM,
				"Jawbreaker Guardian lost medium crack state");
		guardian.setHealth(24.0F);
		require(helper,
				guardian.getCrackiness()
						== IronGolem.Crackiness.HIGH,
				"Jawbreaker Guardian lost high crack state");
		ServerPlayer player = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2029"),
						"CakeWorldJawbreakerGuardianRoleTest"));
		guardian.setHealth(74.0F);
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.IRON_INGOT));
		InteractionResult repair = player.interactOn(
				guardian, InteractionHand.MAIN_HAND);
		require(helper,
				repair.consumesAction()
						&& close(guardian.getHealth(), 99.0D)
						&& guardian.getCrackiness()
								== IronGolem.Crackiness.NONE
						&& player.getMainHandItem().isEmpty(),
				"Jawbreaker Guardian lost one-ingot, twenty-five-health crack repair");
		guardian.offerFlower(true);
		require(helper, guardian.getOfferFlowerTick() == 400,
				"Jawbreaker Guardian lost its Villager flower-offering animation");
		guardian.offerFlower(false);
		require(helper, guardian.getOfferFlowerTick() == 0,
				"Jawbreaker Guardian could not end its flower offering");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setDeltaMovement(Vec3.ZERO);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				boolean attacked =
						guardian.doHurtTarget(target);
				require(helper,
						attacked
								&& close(target.getHealth(),
										10.0D)
								&& guardian
										.getAttackAnimationTick()
										== 10
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement()
										.y > 0.5D,
						safeDifficulty
								+ " Jawbreaker swing caused damage or lost its visible protected defence bounce");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setDeltaMovement(Vec3.ZERO);
			require(helper,
					guardian.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& target.getDeltaMovement().y
									>= 0.4D,
					"Hard Jawbreaker Guardian did not retain genuine Iron Golem damage and launch");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Villager villager =
				EntityType.VILLAGER.create(helper.getLevel());
		Pillager raider =
				EntityType.PILLAGER.create(helper.getLevel());
		require(helper, villager != null && raider != null,
				"Could not create Jawbreaker village-integration fixtures");
		villager.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		raider.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(guardian);
		helper.getLevel().addFreshEntity(villager);
		helper.getLevel().addFreshEntity(raider);
		villager.getBrain().eraseMemory(
				MemoryModuleType.GOLEM_DETECTED_RECENTLY);
		villager.getBrain().setMemory(
				MemoryModuleType.NEAREST_LIVING_ENTITIES,
				List.of(guardian));
		GolemSensor.checkForNearbyGolem(villager);
		require(helper,
				!villager.getBrain().hasMemoryValue(
						MemoryModuleType
								.GOLEM_DETECTED_RECENTLY),
				"Vanilla GolemSensor unexpectedly stopped using its literal-type boundary");
		guardian.refreshVillagerAwareness();
		require(helper,
				villager.getBrain().hasMemoryValue(
						MemoryModuleType
								.GOLEM_DETECTED_RECENTLY),
				"Jawbreaker Guardian did not refresh nearby Villager golem awareness");
		raider.setTarget(guardian);
		raider.setNoActionTime(999);
		guardian.refreshRaiderCombatActivity();
		require(helper, raider.getNoActionTime() == 0,
				"Jawbreaker Guardian did not preserve active Raider-versus-golem combat");

		BlockPos conversionPos =
				findCakeWorldBiomePosition(
						helper, anchor, 512);
		require(helper, conversionPos != null,
				"Could not locate CakeWorld terrain for Iron Golem conversion");
		IronGolem literal =
				EntityType.IRON_GOLEM.create(
						helper.getLevel());
		Ravager ravager =
				EntityType.RAVAGER.create(helper.getLevel());
		require(helper, literal != null && ravager != null,
				"Could not create Iron Golem conversion fixtures");
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		literal.setHealth(49.0F);
		literal.setCustomName(new TextComponent(
				"Jawbreaker Jane"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setPlayerCreated(true);
		literal.setRemainingPersistentAngerTime(321);
		literal.setPersistentAngerTarget(
				player.getUUID());
		ravager.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		helper.getLevel().addFreshEntity(ravager);
		helper.getLevel().addFreshEntity(literal);
		CriteriaTriggers.SUMMONED_ENTITY.trigger(
				player, literal);
		requireCriterion(helper, player,
				"minecraft:adventure/summon_iron_golem",
				"summoned_golem");
		literal.startRiding(ravager, true);
		// Riding can reposition the passenger across a vertical 3D-biome
		// boundary; keep this conversion fixture at the already matched
		// CakeWorld coordinate while retaining its vehicle relationship.
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		JawbreakerGuardian converted =
				CakeWorldIronGolemReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		String conversionState = converted == null
				? "converted=null, literalRemoved="
						+ literal.isRemoved()
				: "type=" + converted.getType()
						+ ", literalRemoved="
						+ literal.isRemoved()
						+ ", health="
						+ converted.getHealth()
						+ ", crack="
						+ converted.getCrackiness()
						+ ", name="
						+ converted.getCustomName()
						+ ", persistent="
						+ converted.isPersistenceRequired()
						+ ", noAi="
						+ converted.isNoAi()
						+ ", playerCreated="
						+ converted.isPlayerCreated()
						+ ", angerTime="
						+ converted
								.getRemainingPersistentAngerTime()
						+ ", angerTarget="
						+ converted.getPersistentAngerTarget()
						+ ", expectedTarget="
						+ player.getUUID()
						+ ", yRot="
						+ converted.getYRot()
						+ ", vehicle="
						+ converted.getVehicle()
						+ ", expectedVehicle="
						+ ravager;
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.JAWBREAKER_GUARDIAN
										.get()
						&& close(converted.getHealth(), 49.0D)
						&& converted.getCrackiness()
								== IronGolem.Crackiness.MEDIUM
						&& converted.hasCustomName()
						&& "Jawbreaker Jane".equals(
								converted.getCustomName()
										.getString())
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isPlayerCreated()
						&& converted
								.getRemainingPersistentAngerTime()
								== 321
						&& player.getUUID().equals(converted
								.getPersistentAngerTarget())
						&& close(converted.getYRot(), 29.0D)
						&& converted.getVehicle() == ravager,
				"Literal Iron Golem conversion lost type, NBT, crack, loyalty, anger, rotation, or vehicle state: "
						+ conversionState);

		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Jawbreaker structure-only spawn boundary for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.MISC)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type
									== EntityType.IRON_GOLEM
									|| spawn.type
											== CakeWorldEntities
													.JAWBREAKER_GUARDIAN
													.get());
			require(helper, !leaked,
					"Jawbreaker Guardian leaked into open-biome spawning at "
							+ biomeId);
		}
		ResourceLocation eggId = new ResourceLocation(
				CakeWorld.MODID,
				"jawbreaker_guardian_spawn_egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities
								.JAWBREAKER_GUARDIAN
								.get())
						== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.JAWBREAKER_GUARDIAN
										.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& Registry.ITEM.getOptional(eggId)
								.isEmpty()
						&& killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:iron_golem")
						&& converted.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/jawbreaker_guardian")),
				"Jawbreaker Guardian lost Iron Golem placement/loot or fabricated an egg or Monsters Hunted criterion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void meringueLlamasKeepPacksCaravansDecorBreedingAndSafeSpit(
			GameTestHelper helper) {
		MeringueLlama first =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		MeringueLlama second =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		TraderLlama trader =
				EntityType.TRADER_LLAMA.create(
						helper.getLevel());
		require(helper, first != null && second != null
						&& target != null && trader != null,
				"Could not create Meringue Llama fixtures");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		first.setNoAi(true);
		second.setNoAi(true);
		first.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		second.setPos(anchor.getX() - 2.0D,
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(first);
		helper.getLevel().addFreshEntity(second);
		helper.getLevel().addFreshEntity(target);

		require(helper,
				first instanceof Llama
						&& first.getType()
								== CakeWorldEntities
										.MERINGUE_LLAMA.get()
						&& first.getType().getCategory()
								== MobCategory.CREATURE
						&& close(first.getMaxHealth(), 53.0D)
						&& close(first.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.175D)
						&& close(first.getAttributeValue(
								Attributes.JUMP_STRENGTH),
								0.5D)
						&& close(first.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								40.0D)
						&& close(first
								.getDimensions(Pose.STANDING)
								.width, 0.9D)
						&& close(first
								.getDimensions(Pose.STANDING)
								.height, 1.87D)
						&& close(first.maxUpStep, 1.0D)
						&& first.getMaxSpawnClusterSize() == 6
						&& first.getMaxTemper() == 30
						&& !first.isTraderLlama()
						&& trader.isTraderLlama()
						&& trader.getType()
								== EntityType.TRADER_LLAMA
						&& !first.isSaddleable()
						&& !first.canBeControlledByRider()
						&& first.canWearArmor()
						&& first.isFood(new ItemStack(
								Items.WHEAT))
						&& first.isFood(new ItemStack(
								Items.HAY_BLOCK)),
				"Meringue Llama lost its exact Llama type, body, attributes, taming, food, pack, or Trader separation");

		CompoundTag firstGenes = new CompoundTag();
		firstGenes.putInt("Strength", 5);
		firstGenes.putInt("Variant", 3);
		first.readAdditionalSaveData(firstGenes);
		CompoundTag secondGenes = new CompoundTag();
		secondGenes.putInt("Strength", 2);
		secondGenes.putInt("Variant", 1);
		second.readAdditionalSaveData(secondGenes);
		UUID owner = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2030");
		ServerPlayer player = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(owner,
						"CakeWorldMeringueLlamaRoleTest"));
		first.setTamed(true);
		first.setOwnerUUID(owner);
		second.setTamed(true);
		second.setOwnerUUID(owner);
		require(helper,
				first.getSlot(499).set(
						new ItemStack(Items.CHEST))
						&& first.getSlot(401).set(
								new ItemStack(
										Items.PINK_CARPET))
						&& first.getSlot(500).set(
								new ItemStack(
										CakeWorldItems
												.BOILED_SWEET
												.get(), 3))
						&& first.getSlot(514).set(
								new ItemStack(
										CakeWorldItems
												.MINT_WAFER
												.get(), 2))
						&& !first.getSlot(515).set(
								new ItemStack(
										Items.DIAMOND))
						&& first.hasChest()
						&& first.getInventoryColumns() == 5
						&& first.getStrength() == 5
						&& first.getVariant() == 3
						&& first.getSwag() == DyeColor.PINK
						&& first.isWearingArmor(),
				"Meringue Llama lost strength-scaled 15-slot storage or carpet decor");

		CompoundTag saved =
				first.saveWithoutId(new CompoundTag());
		MeringueLlama restored =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Meringue Llama reload fixture");
		restored.load(saved);
		require(helper,
				restored.isTamed()
						&& owner.equals(
								restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.getStrength() == 5
						&& restored.getInventoryColumns() == 5
						&& restored.getVariant() == 3
						&& restored.getSwag()
								== DyeColor.PINK
						&& restored.getSlot(401).get()
								.is(Items.PINK_CARPET)
						&& restored.getSlot(500).get()
								.is(CakeWorldItems
										.BOILED_SWEET.get())
						&& restored.getSlot(500).get()
								.getCount() == 3
						&& restored.getSlot(514).get()
								.is(CakeWorldItems
										.MINT_WAFER.get())
						&& restored.getSlot(514).get()
								.getCount() == 2,
				"Meringue Llama reload lost owner, strength, variant, chest, carpet, or edge pack slot");

		player.setItemInHand(InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		InteractionResult mounted = player.interactOn(
				first, InteractionHand.MAIN_HAND);
		require(helper,
				mounted.consumesAction()
						&& player.getVehicle() == first
						&& !first.canBeControlledByRider(),
				"Tame Meringue Llama lost its mountable but deliberately uncontrollable passenger role");
		player.stopRiding();

		first.setHealth(first.getMaxHealth());
		second.setHealth(second.getMaxHealth());
		first.setAge(0);
		second.setAge(0);
		first.setInLove(player);
		second.setInLove(player);
		require(helper, first.canMate(second),
				"Two healthy tame Meringue Llamas could not mate");
		Llama child = first.getBreedOffspring(
				helper.getLevel(), second);
		require(helper,
				child instanceof MeringueLlama
						&& child.getType()
								== CakeWorldEntities
										.MERINGUE_LLAMA.get()
						&& child.getStrength() >= 1
						&& child.getStrength() <= 5
						&& (child.getVariant() == 3
								|| child.getVariant() == 1)
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH)
								>= 40.0D
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH)
								<= 46.0D,
				"Meringue Llama breeding leaked a literal Llama or lost strength, variant, or inherited physical attributes");

		MeringueLlama caravanLeader =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		MeringueLlama caravanFollower =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		require(helper, caravanLeader != null
						&& caravanFollower != null,
				"Could not create Meringue Llama caravan fixtures");
		caravanLeader.setNoAi(true);
		caravanFollower.setNoAi(true);
		caravanLeader.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ() + 6.0D);
		caravanFollower.setPos(anchor.getX() + 5.0D,
				anchor.getY(), anchor.getZ() + 6.0D);
		helper.getLevel().addFreshEntity(caravanLeader);
		helper.getLevel().addFreshEntity(caravanFollower);
		caravanLeader.setLeashedTo(player, false);
		long literalGoals = caravanFollower.goalSelector
				.getAvailableGoals().stream()
				.filter(wrapped -> wrapped.getGoal()
						instanceof LlamaFollowCaravanGoal)
				.count();
		MeringueLlamaFollowCaravanGoal caravanGoal =
				caravanFollower.goalSelector
						.getAvailableGoals().stream()
						.map(wrapped -> wrapped.getGoal())
						.filter(MeringueLlamaFollowCaravanGoal.class
								::isInstance)
						.map(MeringueLlamaFollowCaravanGoal.class
								::cast)
						.findFirst().orElse(null);
		require(helper,
				literalGoals == 0
						&& caravanGoal != null
						&& caravanGoal.canUse()
						&& caravanFollower.inCaravan()
						&& caravanFollower.getCaravanHead()
								== caravanLeader
						&& caravanLeader.hasCaravanTail(),
				"Meringue Llama did not replace the literal-type vanilla goal or form a genuine custom caravan");
		caravanFollower.leaveCaravan();

		first.performRangedAttack(target, 1.0F);
		LlamaSpit spit = helper.getLevel()
				.getEntitiesOfClass(LlamaSpit.class,
						new AABB(anchor).inflate(10.0D))
				.stream()
				.filter(projectile ->
						projectile.getOwner() == first)
				.findFirst().orElse(null);
		require(helper, spit != null,
				"Meringue Llama did not retain its visible vanilla Llama spit projectile");
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(
						DamageSource.indirectMobAttack(
								spit, first)
								.setProjectile(),
						1.0F);
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.getEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
										.getAmplifier() == 1
								&& target.hasEffect(
										MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement()
										.x > 0.0D
								&& target.getDeltaMovement()
										.y > 0.0D,
						safeDifficulty
								+ " Meringue spit caused health damage or lost its sticky visible nudge and rescue envelope");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setDeltaMovement(Vec3.ZERO);
			target.hurt(
					DamageSource.indirectMobAttack(
							spit, first).setProjectile(),
					1.0F);
			require(helper,
					close(target.getHealth(), 9.0D)
							&& target.getActiveEffects().isEmpty()
							&& target.getDeltaMovement()
									.lengthSqr() > 0.0D,
					"Hard Meringue Llama did not retain exact one-point vanilla spit damage and hit motion");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		float beforeFall = restored.getHealth();
		restored.causeFallDamage(
				8.0F, 1.0F, DamageSource.FALL);
		require(helper, restored.getHealth() < beforeFall,
				"Meringue Llama incorrectly erased genuine environmental fall damage");

		BlockPos spawnPos = anchor.offset(8, 0, 8);
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.GRASS_BLOCK.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.TORCH.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		boolean chocolateTag =
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState()
						.is(BlockTags.ANIMALS_SPAWNABLE_ON);
		boolean grassTag = Blocks.GRASS_BLOCK
				.defaultBlockState()
				.is(BlockTags.ANIMALS_SPAWNABLE_ON);
		boolean groundBody =
				SpawnPlacements.Type.ON_GROUND.canSpawnAt(
						helper.getLevel(), spawnPos,
						CakeWorldEntities.MERINGUE_LLAMA.get());
		boolean placementType =
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.MERINGUE_LLAMA.get())
						== SpawnPlacements.Type.ON_GROUND;
		boolean heightmapType =
				SpawnPlacements.getHeightmapType(
						CakeWorldEntities.MERINGUE_LLAMA.get())
						== Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES;
		require(helper,
				chocolateTag && grassTag
						&& groundBody && placementType
						&& heightmapType,
				"Meringue Llama lost the exact vanilla-valid animal ground metadata contract: chocolateTag="
						+ chocolateTag + ", grassTag="
						+ grassTag + ", groundBody="
						+ groundBody + ", placementType="
						+ placementType + ", heightmapType="
						+ heightmapType);

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ResourceLocation cloudbanks = new ResourceLocation(
				CakeWorld.MODID,
				"candyfloss_cloudbanks");
		require(helper, biomes.get(cloudbanks) == null,
				"Candyfloss Cloudbanks unexpectedly exists; MOB-030's staged spawn gate must be revisited");
		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect staged Meringue Llama biome gate for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.CREATURE)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type == EntityType.LLAMA
									|| spawn.type
											== CakeWorldEntities
													.MERINGUE_LLAMA
													.get());
			require(helper, !leaked,
					"Meringue Llama or vanilla Llama leaked into existing biome "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.MERINGUE_LLAMA_SPAWN_EGG
						.isPresent()
						&& first.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/meringue_llama")),
				"Meringue Llama lost its spawn egg or dedicated Llama-equivalent loot table");
		VanillaRoleAdvancements.creditBredRole(
				player,
				CakeWorldEntities.MERINGUE_LLAMA.get());
		requireCriterion(helper, player,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:llama");
		helper.runAfterDelay(5, () -> {
			int rawBrightness = helper.getLevel()
					.getMaxLocalRawBrightness(spawnPos);
			require(helper,
					rawBrightness > 8
							&& Animal.checkAnimalSpawnRules(
									CakeWorldEntities
											.MERINGUE_LLAMA
											.get(),
									helper.getLevel(),
									MobSpawnType.NATURAL,
									spawnPos,
									new Random(1978L)),
					"Meringue Llama lost the inherited bright animal predicate after block-light propagation: rawBrightness="
							+ rawBrightness);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void hotFudgeBlobsKeepSizesJumpsSplitsCreamAndSafeContact(
			GameTestHelper helper) {
		var blob = new HotFudgeBlob(
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				helper.getLevel()) {
			public int sampleJumpDelay() {
				return getJumpDelay();
			}

			public float sampleAttackDamage() {
				return getAttackDamage();
			}

			public void performGroundJump() {
				jumpFromGround();
			}

			public void performLavaJump() {
				jumpInLiquid(FluidTags.LAVA);
			}
		};
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Hot-Fudge Blob contact target");
		CompoundTag largeState = new CompoundTag();
		largeState.putInt("Size", 3);
		blob.readAdditionalSaveData(largeState);
		blob.setHealth(blob.getMaxHealth());
		BlockPos anchor = helper.absolutePos(
				new BlockPos(1, 2, 1));
		blob.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		blob.setNoAi(true);
		target.setPos(anchor.getX() + 0.25D,
				anchor.getY(), anchor.getZ());
		target.setNoAi(true);
		helper.getLevel().addFreshEntity(blob);
		helper.getLevel().addFreshEntity(target);

		int jumpDelay = blob.sampleJumpDelay();
		blob.setDeltaMovement(Vec3.ZERO);
		blob.performGroundJump();
		double groundJump = blob.getDeltaMovement().y;
		blob.setDeltaMovement(Vec3.ZERO);
		blob.performLavaJump();
		double lavaJump = blob.getDeltaMovement().y;
		require(helper,
				blob instanceof MagmaCube
						&& blob.getType()
								== CakeWorldEntities
										.HOT_FUDGE_BLOB.get()
						&& blob.getType().getCategory()
								== MobCategory.MONSTER
						&& blob.getSize() == 4
						&& close(blob.getMaxHealth(), 16.0D)
						&& close(blob.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.6D)
						&& close(blob.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								4.0D)
						&& close(blob.getAttributeValue(
								Attributes.ARMOR), 12.0D)
						&& close(blob.sampleAttackDamage(),
								6.0D)
						&& close(blob.getDimensions(
								Pose.STANDING).width,
								2.0808D)
						&& close(blob.getDimensions(
								Pose.STANDING).height,
								2.0808D)
						&& jumpDelay >= 40
						&& jumpDelay <= 116
						&& close(groundJump, 0.82D)
						&& close(lavaJump, 0.42D),
				"Hot-Fudge Blob lost Magma Cube size-four health, speed, damage, armour, body or jump scaling");
		require(helper,
				blob.fireImmune()
						&& !blob.isOnFire()
						&& close(blob.getBrightness(), 1.0D)
						&& !blob.causeFallDamage(
								100.0F, 1.0F,
								DamageSource.FALL),
				"Hot-Fudge Blob lost its fire immunity, full brightness or fall immunity");

		for (Difficulty safeDifficulty :
				new Difficulty[] {
						Difficulty.PEACEFUL,
						Difficulty.EASY,
						Difficulty.NORMAL}) {
			target.setPos(anchor.getX() + 0.25D,
					anchor.getY(), anchor.getZ());
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setSecondsOnFire(5);
			target.fallDistance = 12.0F;
			target.setDeltaMovement(Vec3.ZERO);
			LivingHurtEvent protectedHit =
					new LivingHurtEvent(target,
							DamageSource.mobAttack(blob),
							blob.sampleAttackDamage());
			HotFudgeBlobDamageSafety
					.applyForDifficulty(
							protectedHit,
							safeDifficulty);
			require(helper,
					protectedHit.isCanceled()
							&& close(target.getHealth(), 10.0D)
							&& !target.isOnFire()
							&& target.fallDistance == 0.0F
							&& target.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& target.getEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
									.getAmplifier() == 1
							&& target.hasEffect(
									MobEffects.GLOWING)
							&& target.hasEffect(
									MobEffects.SLOW_FALLING)
							&& target.hasEffect(
									MobEffects.FIRE_RESISTANCE)
							&& target.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4
							&& target.getDeltaMovement()
									.lengthSqr() > 0.0D,
					safeDifficulty
							+ " Hot-Fudge Blob did not cancel contact or lost its sticky protected bounce");
		}
		target.removeAllEffects();
		target.setDeltaMovement(Vec3.ZERO);
		LivingHurtEvent hardHit = new LivingHurtEvent(
				target, DamageSource.mobAttack(blob),
				blob.sampleAttackDamage());
		HotFudgeBlobDamageSafety.applyForDifficulty(
				hardHit, Difficulty.HARD);
		require(helper,
				!hardHit.isCanceled()
						&& close(hardHit.getAmount(), 6.0D)
						&& target.getActiveEffects().isEmpty()
						&& target.getDeltaMovement()
								.equals(Vec3.ZERO),
				"Hard size-four Hot-Fudge Blob did not retain an unmodified six-point Magma Cube contact");
		target.discard();
		blob.discard();

		HotFudgeBlob tiny =
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.create(helper.getLevel());
		require(helper, tiny != null,
				"Could not create tiny Hot-Fudge Blob fixture");
		CompoundTag tinyState = new CompoundTag();
		tinyState.putInt("Size", 0);
		tiny.readAdditionalSaveData(tinyState);
		require(helper,
				tiny.isTiny()
						&& close(tiny.getMaxHealth(), 1.0D)
						&& close(tiny.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.3D)
						&& close(tiny.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								1.0D)
						&& close(tiny.getAttributeValue(
								Attributes.ARMOR), 3.0D)
						&& close(tiny.getDimensions(
								Pose.STANDING).width,
								0.5202D)
						&& tiny.getLootTable().equals(
								BuiltInLootTables.EMPTY)
						&& blob.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/hot_fudge_blob")),
				"Hot-Fudge Blob lost tiny size scaling or size-gated magma-cream loot");

		HotFudgeBlob parent =
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.create(helper.getLevel());
		require(helper, parent != null,
				"Could not create Hot-Fudge Blob split fixture");
		parent.readAdditionalSaveData(largeState);
		BlockPos splitPos = anchor;
		parent.setPos(splitPos.getX(), splitPos.getY(),
				splitPos.getZ());
		parent.setCustomName(new TextComponent(
				"Family Fudge"));
		parent.setPersistenceRequired();
		parent.setNoAi(true);
		parent.setInvulnerable(true);
		helper.getLevel().addFreshEntity(parent);
		parent.setHealth(0.0F);
		parent.remove(Entity.RemovalReason.KILLED);
		List<HotFudgeBlob> children = helper.getLevel()
				.getEntitiesOfClass(HotFudgeBlob.class,
						new AABB(splitPos).inflate(3.0D));
		require(helper,
				parent.isRemoved()
						&& children.size() >= 2
						&& children.size() <= 4
						&& children.stream().allMatch(child ->
								child.getType()
										== CakeWorldEntities
												.HOT_FUDGE_BLOB
												.get()
										&& child.getSize() == 2
										&& close(child.getHealth(),
												4.0D)
										&& child.isPersistenceRequired()
										&& child.isNoAi()
										&& child.isInvulnerable()
										&& child.hasCustomName()
										&& "Family Fudge".equals(
												child.getName()
														.getString())),
				"Hot-Fudge Blob split leaked literal Magma Cubes or lost child size, health, name, persistence, AI or invulnerability");
		children.forEach(Entity::discard);

		BlockPos spawnPos = anchor;
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				!HotFudgeBlob.allowsNaturalSpawn(
						Difficulty.PEACEFUL)
						&& HotFudgeBlob.allowsNaturalSpawn(
								Difficulty.EASY)
						&& HotFudgeBlob.allowsNaturalSpawn(
								Difficulty.NORMAL)
						&& HotFudgeBlob.allowsNaturalSpawn(
								Difficulty.HARD)
						&& SpawnPlacements
									.getPlacementType(
											CakeWorldEntities
													.HOT_FUDGE_BLOB
													.get())
									== SpawnPlacements.Type.ON_GROUND
							&& SpawnPlacements
									.getHeightmapType(
											CakeWorldEntities
													.HOT_FUDGE_BLOB
													.get())
									== Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES
							&& SpawnPlacements.Type.ON_GROUND
									.canSpawnAt(
											helper.getLevel(),
											spawnPos,
											CakeWorldEntities
													.HOT_FUDGE_BLOB
													.get()),
				"Hot-Fudge Blob lost Peaceful suppression or exact Magma Cube ground placement on Fudge Rock");

		Biome fudgeWastes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes Hot-Fudge Blob spawning");
		requireSpawnReplacement(helper, fudgeWastes,
				EntityType.MAGMA_CUBE,
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				MobCategory.MONSTER);
		MobSpawnSettings.SpawnerData biomeSpawn =
				fudgeWastes.getMobSettings()
						.getMobs(MobCategory.MONSTER)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.HOT_FUDGE_BLOB.get())
						.findFirst().orElse(null);
		MobSpawnSettings.SpawnerData fortressSpawn =
				NetherFortressFeature.FORTRESS_ENEMIES
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== EntityType.MAGMA_CUBE)
						.findFirst().orElse(null);
		require(helper,
				biomeSpawn != null
						&& biomeSpawn.getWeight().asInt() == 2
						&& biomeSpawn.minCount == 4
						&& biomeSpawn.maxCount == 4
						&& fortressSpawn != null
						&& fortressSpawn.getWeight().asInt() == 3
						&& fortressSpawn.minCount == 4
						&& fortressSpawn.maxCount == 4,
				"Hot-Fudge Blob lost the Nether Wastes 2/4-4 profile or the audited Fortress 3/4-4 conversion seam");

		MagmaCube literal =
				EntityType.MAGMA_CUBE.create(
						helper.getLevel());
		require(helper, literal != null,
				"Could not create literal Fortress Magma Cube fixture");
		literal.readAdditionalSaveData(largeState);
		literal.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());
		literal.setHealth(13.0F);
		literal.setCustomName(new TextComponent(
				"Fortress Fudge"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		helper.getLevel().addFreshEntity(literal);
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(literal.blockPosition())
				.unwrapKey().map(key -> key.location())
				.orElse(null);
		require(helper,
				fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Hot-Fudge Blob conversion fixture is not in a CakeWorld biome: "
						+ fixtureBiome);
		HotFudgeBlob converted =
				CakeWorldMagmaCubeReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.HOT_FUDGE_BLOB.get()
						&& converted.getSize() == 4
						&& close(converted.getHealth(), 13.0D)
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.hasCustomName()
						&& "Fortress Fudge".equals(
								converted.getName()
										.getString()),
				"Fresh CakeWorld Fortress conversion lost Magma Cube size, health, name, persistence, AI or invulnerability");

		TagKey<EntityType<?>> freezeHurtsExtra =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"freeze_hurts_extra_types"));
		require(helper,
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.is(freezeHurtsExtra)
						&& blob.canFreeze()
						&& CakeWorldItems
								.HOT_FUDGE_BLOB_SPAWN_EGG
								.isPresent(),
				"Hot-Fudge Blob lost extra freeze vulnerability or its creative/testing egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2031"),
						"CakeWorldHotFudgeRoleTest"));
		VanillaRoleAdvancements
				.creditKilledMagmaCubeRole(
						advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:magma_cube");
		converted.discard();
		helper.succeed();
	}

	private static FoodProperties requireFood(GameTestHelper helper, ItemStack stack) {
		FoodProperties food = stack.getItem().getFoodProperties();
		require(helper, food != null, stack.getItem() + " has no food properties");
		return food;
	}

	private static BlockPos findCakeWorldBiomePosition(
			GameTestHelper helper, BlockPos origin,
			int maximumRadius) {
		for (int radius = 0; radius <= maximumRadius;
				radius += 4) {
			for (int x = -radius; x <= radius; x += 4) {
				for (int z = -radius; z <= radius;
						z += 4) {
					if (Math.max(Math.abs(x),
							Math.abs(z)) != radius) {
						continue;
					}
					BlockPos candidate =
							origin.offset(x, 0, z);
					boolean cakeWorld = helper.getLevel()
							.getBiome(candidate).unwrapKey()
							.map(key -> CakeWorld.MODID
									.equals(key.location()
											.getNamespace()))
							.orElse(false);
					if (cakeWorld) {
						return candidate;
					}
				}
			}
		}
		return null;
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

	private static boolean hasInheritedMuleAttributes(
			AgeableMob child) {
		return child instanceof MarzipanMule
				&& child.getType()
						== CakeWorldEntities.MARZIPAN_MULE.get()
				&& child.getAttributeBaseValue(
						Attributes.MAX_HEALTH) >= 23.0D
				&& child.getAttributeBaseValue(
						Attributes.MAX_HEALTH) <= 28.0D
				&& child.getAttributeBaseValue(
						Attributes.JUMP_STRENGTH) >= 0.6D
				&& child.getAttributeBaseValue(
						Attributes.JUMP_STRENGTH) <= 0.8D
				&& child.getAttributeBaseValue(
						Attributes.MOVEMENT_SPEED)
								>= 0.180833D
				&& child.getAttributeBaseValue(
						Attributes.MOVEMENT_SPEED)
								<= 0.255834D;
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
