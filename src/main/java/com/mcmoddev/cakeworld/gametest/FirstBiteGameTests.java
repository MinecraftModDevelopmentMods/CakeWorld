package com.mcmoddev.cakeworld.gametest;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.cookbook.CookbookProgress;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;
import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.TrufflePig;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.common.util.FakePlayerFactory;
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

	private static void require(GameTestHelper helper, boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
