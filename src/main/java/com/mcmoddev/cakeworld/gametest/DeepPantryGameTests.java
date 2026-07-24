package com.mcmoddev.cakeworld.gametest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.compat.VanillaResourceAdvancements;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.orespawn.api.GeologyColumn;
import com.mcmoddev.orespawn.api.GeologyProfileView;
import com.mcmoddev.orespawn.api.GeologySampler;
import com.mcmoddev.orespawn.api.OreSpawnApi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.advancements.Advancement;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class DeepPantryGameTests {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String EMPTY = "empty";
	private static final ResourceLocation EDIBLE_WORLD =
			new ResourceLocation(CakeWorld.MODID, "edible_world");

	private static final Set<ResourceLocation> EXPECTED_ROCK_IDS = Set.of(
			id("rock/chocolate_sponge"),
			id("rock/biscuit_stone"),
			id("rock/wafer"),
			id("rock/nougat"),
			id("rock/peppermint"),
			id("rock/rock_candy"),
			id("rock/fudge_rock"),
			id("rock/burnt_sugar"));

	private static final Set<ResourceLocation> EXPECTED_GEOME_IDS = Set.of(
			id("cocoa_basin"),
			id("wafer_shelf"),
			id("peppermint_fold"),
			id("rock_candy_uplift"),
			id("fudge_mantle"),
			id("meringue_crust"));

	private static final Set<ResourceLocation> EXPECTED_ORE_IDS = Set.of(
			id("ore/rock_candy_deposit"),
			id("ore/liquorice_vein"),
			id("ore/cocoa_cloud"),
			id("ore/mint_crystal"),
			id("ore/sprinkle_cluster"),
			id("ore/fizzy_pearl"));

	private static final Set<ResourceLocation> EXPECTED_FLUID_DEPOSIT_IDS = Set.of(
			id("fluid_deposit/jam"),
			id("fluid_deposit/custard"),
			id("fluid_deposit/caramel"),
			id("fluid_deposit/syrup"));

	private DeepPantryGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void edibleWorldBakesAndGeneratesDeepPantryGeology(
			GameTestHelper helper) {
		Optional<GeologyProfileView> profileResult =
				OreSpawnApi.getActiveProfile(helper.getLevel().getServer());
		require(helper, profileResult.isPresent(),
				"OreSpawn did not expose the active fresh-world profile");
		GeologyProfileView profile = profileResult.orElseThrow();
		require(helper, profile.selectedTemplate().filter(EDIBLE_WORLD::equals).isPresent(),
				"The fresh world did not select cakeworld:edible_world");
		require(helper, profile.rockIds().containsAll(EXPECTED_ROCK_IDS),
				"The active profile is missing CakeWorld rock families: "
						+ missing(EXPECTED_ROCK_IDS, profile.rockIds()));
		require(helper, profile.geomeIds().containsAll(EXPECTED_GEOME_IDS),
				"The active profile is missing CakeWorld flavour geomes: "
						+ missing(EXPECTED_GEOME_IDS, profile.geomeIds()));
		require(helper, profile.oreIds().containsAll(EXPECTED_ORE_IDS),
				"The active profile is missing CakeWorld ore examples: "
						+ missing(EXPECTED_ORE_IDS, profile.oreIds()));
		require(helper, profile.fluidDepositIds().containsAll(
						EXPECTED_FLUID_DEPOSIT_IDS),
				"The active profile is missing CakeWorld fluid-deposit examples: "
						+ missing(EXPECTED_FLUID_DEPOSIT_IDS,
								profile.fluidDepositIds()));

		Optional<GeologySampler> samplerResult =
				OreSpawnApi.createSampler(helper.getLevel());
		require(helper, samplerResult.isPresent(),
				"OreSpawn did not expose a sampler for the active Overworld");
		GeologySampler sampler = samplerResult.orElseThrow();

		Set<Block> overworldRocks = Set.of(
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(),
				CakeWorldBlocks.BISCUIT_STONE.get(),
				CakeWorldBlocks.WAFER_ROCK.get(),
				CakeWorldBlocks.NOUGAT_ROCK.get(),
				CakeWorldBlocks.PEPPERMINT_ROCK.get(),
				CakeWorldBlocks.ROCK_CANDY.get());
		Set<Block> newOverworldRocks = Set.of(
				CakeWorldBlocks.WAFER_ROCK.get(),
				CakeWorldBlocks.NOUGAT_ROCK.get(),
				CakeWorldBlocks.PEPPERMINT_ROCK.get(),
				CakeWorldBlocks.ROCK_CANDY.get());
		Set<Block> oreDeposits = Set.of(
				CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get(),
				CakeWorldBlocks.LIQUORICE_VEIN.get(),
				CakeWorldBlocks.COCOA_CLOUD.get(),
				CakeWorldBlocks.MINT_CRYSTAL.get(),
				CakeWorldBlocks.SPRINKLE_CLUSTER.get(),
				CakeWorldBlocks.RICH_SPRINKLE_CLUSTER.get(),
				CakeWorldBlocks.FIZZY_PEARL.get());
		Set<Block> fluidDeposits = Set.of(
				CakeWorldFluids.JAM_BLOCK.get(),
				CakeWorldFluids.CUSTARD_BLOCK.get(),
				CakeWorldFluids.CARAMEL_BLOCK.get(),
				CakeWorldFluids.SYRUP_BLOCK.get());

		BlockPos origin = helper.absolutePos(BlockPos.ZERO);
		int sampledCakeWorldRocks = 0;
		for (int x = origin.getX() - 48; x < origin.getX() + 48; x += 8) {
			for (int z = origin.getZ() - 48; z < origin.getZ() + 48; z += 8) {
				int surfaceY = helper.getLevel().getHeight(
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
				GeologyColumn column = sampler.sampleColumn(x, z, surfaceY);
				for (int y = -48; y <= Math.min(80, surfaceY - 4); y += 16) {
					if (overworldRocks.contains(column.rockAt(y).getBlock())) {
						sampledCakeWorldRocks++;
					}
				}
			}
		}
		require(helper, sampledCakeWorldRocks > 0,
				"The public sampler did not predict any CakeWorld geology");

		Map<Block, Integer> generatedRocks = new LinkedHashMap<>();
		Map<Block, Integer> generatedDeposits = new LinkedHashMap<>();
		Map<Block, Integer> generatedFluidDeposits = new LinkedHashMap<>();
		int minimumY = helper.getLevel().getMinBuildHeight();
		for (int x = origin.getX() - 48; x < origin.getX() + 48; x++) {
			for (int z = origin.getZ() - 48; z < origin.getZ() + 48; z++) {
				int surfaceY = Math.min(96, helper.getLevel().getHeight(
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 4);
				for (int y = minimumY; y <= surfaceY; y++) {
					Block block = helper.getLevel().getBlockState(
							new BlockPos(x, y, z)).getBlock();
					if (newOverworldRocks.contains(block)) {
						generatedRocks.merge(block, 1, Integer::sum);
					}
					if (oreDeposits.contains(block)) {
						generatedDeposits.merge(block, 1, Integer::sum);
					}
					if (fluidDeposits.contains(block)) {
						generatedFluidDeposits.merge(block, 1, Integer::sum);
					}
				}
			}
		}
		require(helper, !generatedRocks.isEmpty(),
				"Fresh chunks contained no new Deep Pantry rock blocks");
		require(helper, !generatedDeposits.isEmpty(),
				"Fresh chunks contained no Deep Pantry deposit blocks; rocks="
						+ describe(generatedRocks));
		require(helper, !generatedFluidDeposits.isEmpty(),
				"Fresh chunks contained no covered edible fluid deposits");
		LOGGER.info("Deep Pantry generated-world audit: sampler predictions={}, rocks={}, deposits={}, fluid deposits={}",
				sampledCakeWorldRocks, describe(generatedRocks),
				describe(generatedDeposits), describe(generatedFluidDeposits));
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void themedVanillaResourcesPreserveMiningAndProcessingRoles(
			GameTestHelper helper) {
		Map<Block, DropExpectation> resources = new LinkedHashMap<>();
		resources.put(CakeWorldBlocks.COCOA_COAL.get(),
				new DropExpectation(Items.COAL, 1, 1, "coal"));
		resources.put(CakeWorldBlocks.IRON_WAFER.get(),
				new DropExpectation(Items.RAW_IRON, 1, 1, "iron"));
		resources.put(CakeWorldBlocks.COPPER_CARAMEL.get(),
				new DropExpectation(Items.RAW_COPPER, 2, 5, "copper"));
		resources.put(CakeWorldBlocks.HONEYCOMB_GOLD.get(),
				new DropExpectation(Items.RAW_GOLD, 1, 1, "gold"));
		resources.put(CakeWorldBlocks.RASPBERRY_REDSTONE.get(),
				new DropExpectation(Items.REDSTONE, 4, 5, "redstone"));
		resources.put(CakeWorldBlocks.BLUEBERRY_LAPIS.get(),
				new DropExpectation(Items.LAPIS_LAZULI, 4, 9, "lapis"));
		resources.put(CakeWorldBlocks.ROCK_CANDY_DIAMOND.get(),
				new DropExpectation(Items.DIAMOND, 1, 1, "diamond"));
		resources.put(CakeWorldBlocks.MINT_EMERALD.get(),
				new DropExpectation(Items.EMERALD, 1, 1, "emerald"));
		resources.put(CakeWorldBlocks.VANILLA_QUARTZ.get(),
				new DropExpectation(Items.QUARTZ, 1, 1, "quartz"));
		resources.put(CakeWorldBlocks.FUDGE_GOLD.get(),
				new DropExpectation(Items.GOLD_NUGGET, 2, 6, "gold"));
		resources.put(CakeWorldBlocks.ANCIENT_NOUGAT.get(),
				new DropExpectation(CakeWorldBlocks.ANCIENT_NOUGAT.get().asItem(),
						1, 1, "netherite_scrap"));

		ItemStack ordinaryPickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
		ItemStack silkPickaxe = ordinaryPickaxe.copy();
		silkPickaxe.enchant(Enchantments.SILK_TOUCH, 1);
		BlockPos lootPos = helper.absolutePos(new BlockPos(1, 1, 1));

		for (Map.Entry<Block, DropExpectation> entry : resources.entrySet()) {
			Block block = entry.getKey();
			DropExpectation expectation = entry.getValue();
			ResourceLocation blockId = Registry.BLOCK.getKey(block);
			require(helper, block.defaultBlockState().is(
							BlockTags.MINEABLE_WITH_PICKAXE),
					blockId + " is not pickaxe-mineable");
			TagKey<Block> blockTag = TagKey.create(Registry.BLOCK_REGISTRY,
					new ResourceLocation("forge", "ores/" + expectation.oreTag()));
			TagKey<Item> itemTag = TagKey.create(Registry.ITEM_REGISTRY,
					new ResourceLocation("forge", "ores/" + expectation.oreTag()));
			require(helper, block.defaultBlockState().is(blockTag)
							&& new ItemStack(block).is(itemTag),
					blockId + " lost forge:ores/" + expectation.oreTag());

			List<ItemStack> ordinaryDrops =
					drops(helper, block, ordinaryPickaxe, lootPos);
			int ordinaryCount = countOnly(helper, ordinaryDrops,
					expectation.item(), blockId + " ordinary");
			require(helper, ordinaryCount >= expectation.minimum()
							&& ordinaryCount <= expectation.maximum(),
					blockId + " ordinary drop count was " + ordinaryCount);

			List<ItemStack> silkDrops = drops(helper, block, silkPickaxe, lootPos);
			int silkCount = countOnly(helper, silkDrops, block.asItem(),
					blockId + " Silk Touch");
			require(helper, silkCount == 1,
					blockId + " Silk Touch did not return one themed block");
		}

		require(helper, CakeWorldBlocks.RASPBERRY_REDSTONE.get()
						instanceof RedStoneOreBlock,
				"Raspberry Redstone does not retain light-up ore behavior");
		require(helper, CakeWorldBlocks.IRON_WAFER.get().defaultBlockState()
						.is(BlockTags.NEEDS_STONE_TOOL)
						&& CakeWorldBlocks.COPPER_CARAMEL.get().defaultBlockState()
								.is(BlockTags.NEEDS_STONE_TOOL)
						&& CakeWorldBlocks.BLUEBERRY_LAPIS.get().defaultBlockState()
								.is(BlockTags.NEEDS_STONE_TOOL),
				"Stone-tier vanilla resource roles are incomplete");
		require(helper, CakeWorldBlocks.HONEYCOMB_GOLD.get().defaultBlockState()
						.is(BlockTags.NEEDS_IRON_TOOL)
						&& CakeWorldBlocks.RASPBERRY_REDSTONE.get().defaultBlockState()
								.is(BlockTags.NEEDS_IRON_TOOL)
						&& CakeWorldBlocks.ROCK_CANDY_DIAMOND.get().defaultBlockState()
								.is(BlockTags.NEEDS_IRON_TOOL)
						&& CakeWorldBlocks.MINT_EMERALD.get().defaultBlockState()
								.is(BlockTags.NEEDS_IRON_TOOL),
				"Iron-tier vanilla resource roles are incomplete");
		require(helper, CakeWorldBlocks.ANCIENT_NOUGAT.get().defaultBlockState()
						.is(BlockTags.NEEDS_DIAMOND_TOOL),
				"Ancient Nougat does not require diamond-tier mining");
		require(helper, new ItemStack(CakeWorldBlocks.HONEYCOMB_GOLD.get())
						.is(ItemTags.PIGLIN_LOVED)
						&& new ItemStack(CakeWorldBlocks.FUDGE_GOLD.get())
								.is(ItemTags.PIGLIN_LOVED),
				"CakeWorld gold ores lost piglin-recognisable identity");
		require(helper, CakeWorldBlocks.ANCIENT_NOUGAT.get()
						.getExplosionResistance() >= 1200.0F,
				"Ancient Nougat is not blast-resistant enough for netherite progression");

		String[][] recipeOutputs = {
			{"coal_from_smelting_cocoa_coal", "minecraft:coal"},
			{"coal_from_blasting_cocoa_coal", "minecraft:coal"},
			{"iron_ingot_from_smelting_iron_wafer", "minecraft:iron_ingot"},
			{"iron_ingot_from_blasting_iron_wafer", "minecraft:iron_ingot"},
			{"copper_ingot_from_smelting_copper_caramel", "minecraft:copper_ingot"},
			{"copper_ingot_from_blasting_copper_caramel", "minecraft:copper_ingot"},
			{"gold_ingot_from_smelting_honeycomb_gold", "minecraft:gold_ingot"},
			{"gold_ingot_from_blasting_honeycomb_gold", "minecraft:gold_ingot"},
			{"redstone_from_smelting_raspberry_redstone", "minecraft:redstone"},
			{"redstone_from_blasting_raspberry_redstone", "minecraft:redstone"},
			{"lapis_from_smelting_blueberry_lapis", "minecraft:lapis_lazuli"},
			{"lapis_from_blasting_blueberry_lapis", "minecraft:lapis_lazuli"},
			{"diamond_from_smelting_rock_candy_diamond", "minecraft:diamond"},
			{"diamond_from_blasting_rock_candy_diamond", "minecraft:diamond"},
			{"emerald_from_smelting_mint_emerald", "minecraft:emerald"},
			{"emerald_from_blasting_mint_emerald", "minecraft:emerald"},
			{"quartz_from_smelting_vanilla_quartz", "minecraft:quartz"},
			{"quartz_from_blasting_vanilla_quartz", "minecraft:quartz"},
			{"gold_ingot_from_smelting_fudge_gold", "minecraft:gold_ingot"},
			{"gold_ingot_from_blasting_fudge_gold", "minecraft:gold_ingot"},
			{"netherite_scrap_from_smelting_ancient_nougat",
					"minecraft:netherite_scrap"},
			{"netherite_scrap_from_blasting_ancient_nougat",
					"minecraft:netherite_scrap"}
		};
		for (String[] recipeOutput : recipeOutputs) {
			net.minecraft.world.item.crafting.Recipe<?> recipe =
					helper.getLevel().getRecipeManager().byKey(
							id(recipeOutput[0])).orElse(null);
			require(helper, recipe != null && Registry.ITEM.getKey(
							recipe.getResultItem().getItem()).equals(
									new ResourceLocation(recipeOutput[1])),
					"Missing or incorrect processing recipe: " + recipeOutput[0]);
		}

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978beef-beef-4bad-babe-1978beef1978"),
						"CakeWorldResourceTest"));
		VanillaResourceAdvancements.creditAncientNougat(advancementPlayer);
		Advancement ancientDebris = advancementPlayer.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft", "nether/obtain_ancient_debris"));
		require(helper, ancientDebris != null
						&& advancementPlayer.getAdvancements()
								.getOrStartProgress(ancientDebris)
								.getCriterion("ancient_debris").isDone(),
				"Ancient Nougat did not preserve the vanilla advancement role");

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, !profile.toJson().get("manage_vanilla_ores").getAsBoolean(),
				"Unsafe themed-ore takeover was enabled before OreSpawn can map source blocks");
		helper.succeed();
	}

	private static List<ItemStack> drops(GameTestHelper helper, Block block,
			ItemStack tool, BlockPos origin) {
		ResourceLocation blockId = Registry.BLOCK.getKey(block);
		LootTable table = helper.getLevel().getServer().getLootTables()
				.get(new ResourceLocation(CakeWorld.MODID,
						"blocks/" + blockId.getPath()));
		LootContext context = new LootContext.Builder(helper.getLevel())
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(origin))
				.withParameter(LootContextParams.TOOL, tool)
				.withParameter(LootContextParams.BLOCK_STATE,
						block.defaultBlockState())
				.create(LootContextParamSets.BLOCK);
		return table.getRandomItems(context);
	}

	private static int countOnly(GameTestHelper helper, List<ItemStack> drops,
			Item expected, String description) {
		int count = 0;
		for (ItemStack drop : drops) {
			require(helper, drop.is(expected),
					description + " produced unexpected item "
							+ Registry.ITEM.getKey(drop.getItem()));
			count += drop.getCount();
		}
		return count;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private static Set<ResourceLocation> missing(Set<ResourceLocation> expected,
			Set<ResourceLocation> actual) {
		java.util.LinkedHashSet<ResourceLocation> missing =
				new java.util.LinkedHashSet<>(expected);
		missing.removeAll(actual);
		return missing;
	}

	private static String describe(Map<Block, Integer> counts) {
		Map<ResourceLocation, Integer> named = new LinkedHashMap<>();
		counts.forEach((block, count) ->
				named.put(Registry.BLOCK.getKey(block), count));
		return named.toString();
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
		}
	}

	private record DropExpectation(Item item, int minimum, int maximum,
			String oreTag) {
	}
}
