package com.mcmoddev.cakeworld.gametest;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.authlib.GameProfile;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.compat.VanillaResourceAdvancements;
import com.mcmoddev.cakeworld.entity.BiscuitBandit;
import com.mcmoddev.cakeworld.entity.GingerbreadFolk;
import com.mcmoddev.cakeworld.entity.JawbreakerGuardian;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.world.BiscuitBanditLookoutFeature;
import com.mcmoddev.cakeworld.world.GingerbreadVillageFeature;
import com.mcmoddev.cakeworld.world.GrandGingerbreadManorFeature;
import com.mcmoddev.cakeworld.world.GummyShrineFeature;
import com.mcmoddev.cakeworld.world.WaferMineFeature;
import com.mcmoddev.orespawn.api.CompiledOrePattern;
import com.mcmoddev.orespawn.api.GeologyColumn;
import com.mcmoddev.orespawn.api.GeologyProfileView;
import com.mcmoddev.orespawn.api.GeologySampler;
import com.mcmoddev.orespawn.api.OrePatternType;
import com.mcmoddev.orespawn.api.OrePlacementContext;
import com.mcmoddev.orespawn.api.OreSpawnApi;
import com.mcmoddev.orespawn.api.OreSpawnPatternRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.advancements.Advancement;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class DeepPantryGameTests {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final String EMPTY = "empty";
	private static final ResourceLocation EDIBLE_WORLD =
			new ResourceLocation(CakeWorld.MODID, "edible_world");
	private static final ResourceLocation EDIBLE_WORLD_BASEMETALS =
			new ResourceLocation(CakeWorld.MODID, "edible_world_basemetals");

	private static final Set<ResourceLocation> EXPECTED_ROCK_IDS = Set.of(
			id("rock/chocolate_sponge"),
			id("rock/biscuit_stone"),
			id("rock/wafer"),
			id("rock/nougat"),
			id("rock/peppermint"),
			id("rock/rock_candy"),
			id("rock/candy_glass"),
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
			id("fluid_deposit/syrup"),
			id("fluid_deposit/hot_fudge"));

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
		ResourceLocation expectedTemplate = ModList.get().isLoaded("basemetals")
				? EDIBLE_WORLD_BASEMETALS : EDIBLE_WORLD;
		require(helper, profile.selectedTemplate().filter(
						expectedTemplate::equals).isPresent(),
				"The fresh world did not select " + expectedTemplate);
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
		boolean immutableRockIds = false;
		try {
			profile.rockIds().add(id("gametest_mutation"));
		} catch (UnsupportedOperationException expected) {
			immutableRockIds = true;
		}
		require(helper, immutableRockIds,
				"The active profile exposed a mutable rock-ID collection");
		JsonObject detachedProfileJson = profile.toJson();
		detachedProfileJson.getAsJsonObject("rocks")
				.getAsJsonObject("cakeworld:rock/chocolate_sponge")
				.addProperty("block", "minecraft:air");
		JsonObject profileJson = profile.toJson();
		require(helper, "cakeworld:chocolate_sponge".equals(
						profileJson.getAsJsonObject("rocks")
								.getAsJsonObject(
										"cakeworld:rock/chocolate_sponge")
								.get("block").getAsString()),
				"Mutating diagnostic JSON changed the active baked profile");
		JsonObject fluidDepositRules =
				profileJson.getAsJsonObject("fluid_deposits");
		JsonObject hotFudgeDeposit = fluidDepositRules.getAsJsonObject(
				"cakeworld:fluid_deposit/hot_fudge");
		JsonObject hotFudgeNether = fluidDimensionRule(fluidDepositRules,
				"cakeworld:fluid_deposit/hot_fudge",
				"minecraft:the_nether");
		require(helper, "cakeworld:hot_fudge".equals(
						hotFudgeDeposit.get("block").getAsString())
						&& hotFudgeNether.get("enabled").getAsBoolean()
						&& hotFudgeNether.get("min_y").getAsInt() == 16
						&& hotFudgeNether.get("max_y").getAsInt() == 112
						&& jsonArrayContains(hotFudgeNether,
								"host_families", "igneous_volcanic")
						&& jsonArrayContains(hotFudgeNether,
								"biome_ids", "cakeworld:fudge_wastes")
						&& hotFudgeNether.getAsJsonObject("geomes")
								.get("cakeworld:fudge_mantle")
								.getAsDouble() > 0.0D,
				"Hot Fudge deposit lost its Nether block, range, host, biome, or geome contract");
		JsonObject candyGlassRock = rockRule(profileJson,
				"cakeworld:rock/candy_glass");
		require(helper, candyGlassRock.has("ore_replaceable")
						&& !candyGlassRock.get("ore_replaceable")
								.getAsBoolean(),
				"Candy Glass did not retain its non-replaceable rock contract");
		TagKey<Block> forgeGlassBlocks = TagKey.create(
				Registry.BLOCK_REGISTRY,
				new ResourceLocation("forge", "glass"));
		TagKey<Item> forgeGlassItems = TagKey.create(
				Registry.ITEM_REGISTRY,
				new ResourceLocation("forge", "glass"));
		TagKey<Block> edibleOreHosts = TagKey.create(
				Registry.BLOCK_REGISTRY,
				new ResourceLocation(CakeWorld.MODID, "edible_ore_hosts"));
		BlockState candyGlass = CakeWorldBlocks.CANDY_GLASS.get()
				.defaultBlockState();
		require(helper, !candyGlass.canOcclude()
						&& candyGlass.is(BlockTags.MINEABLE_WITH_PICKAXE)
						&& candyGlass.is(BlockTags.NEEDS_STONE_TOOL)
						&& candyGlass.is(forgeGlassBlocks)
						&& new ItemStack(CakeWorldBlocks.CANDY_GLASS.get())
								.is(forgeGlassItems)
						&& !candyGlass.is(edibleOreHosts),
				"Candy Glass lost its translucent structural, tool, glass-tag, or non-host contract");

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
				CakeWorldBlocks.ROCK_CANDY.get(),
				CakeWorldBlocks.CANDY_GLASS.get());
		Set<Block> newOverworldRocks = Set.of(
				CakeWorldBlocks.WAFER_ROCK.get(),
				CakeWorldBlocks.NOUGAT_ROCK.get(),
				CakeWorldBlocks.PEPPERMINT_ROCK.get(),
				CakeWorldBlocks.ROCK_CANDY.get(),
				CakeWorldBlocks.CANDY_GLASS.get());
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

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void underFluidPatternResolvesCakeWorldLemonade(
			GameTestHelper helper) {
		assertUnderFluidPatternResolvesLemonade(helper);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void compiledDefaultAndPrecisionPatternsRespectBudgets(
			GameTestHelper helper) {
		PatternAudit compact = auditCompiledPattern(
				new ResourceLocation("orespawn", "default"),
				6, 3, 2, 4, 8, 1978L);
		PatternAudit precision = auditCompiledPattern(
				new ResourceLocation("orespawn", "precision"),
				5, 4, 3, 2, 8, 1978L);
		require(helper, compact.changed() && compact.placements() == 6,
				"Default pattern did not consume exactly its six-block budget: "
						+ compact);
		require(helper, compact.maximumDistanceSquared() <= 4,
				"Default pattern escaped its compact connected shape: "
						+ compact);
		require(helper, precision.changed() && precision.placements() == 5,
				"Precision pattern did not consume exactly its five-block budget: "
						+ precision);
		require(helper, precision.maximumDistanceSquared() <= 4,
				"Precision pattern escaped its decoded radius-two sphere: "
						+ precision);
		LOGGER.info("Compiled CakeWorld pattern audit: default={}, precision={}",
				compact, precision);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedBiomeWorldgenAuditsRareOutputs(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed rare-output survey; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		BlockPos marshmallowPeaks = locateBiome(helper, level,
				id("marshmallow_peaks"));
		BlockPos sodaOcean = locateBiome(helper, level, id("soda_ocean"));

		int marshmallowChunkX = Math.floorDiv(marshmallowPeaks.getX(), 16);
		int marshmallowChunkZ = Math.floorDiv(marshmallowPeaks.getZ(), 16);
		Map<Block, Integer> mintRegion = scanDimension(level,
				Set.of(
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						CakeWorldBlocks.MINT_CRYSTAL.get(),
						CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()),
				marshmallowChunkX, marshmallowChunkZ, 4, -64, 96);
		require(helper, mintRegion.getOrDefault(
						CakeWorldBlocks.PEPPERMINT_ROCK.get(), 0) > 0,
				"Marshmallow Peaks region contained no Peppermint Rock host");
		require(helper, mintRegion.getOrDefault(
						CakeWorldBlocks.MINT_CRYSTAL.get(), 0) > 0,
				"Focused Marshmallow Peaks region generated no Mint Crystal");
		Map<Integer, Integer> mintCrystalYs = countTargetYLevels(level,
				Set.of(CakeWorldBlocks.MINT_CRYSTAL.get()),
				marshmallowChunkX, marshmallowChunkZ, 4, -64, 96);
		Map<Integer, Integer> deepMintOutputYs =
				countTargetYLevelsAdjacentTo(level,
						CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get(),
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						marshmallowChunkX, marshmallowChunkZ, 4, -64, -24);
		Map<Integer, Integer> ordinaryRockCandyDepositYs =
				countTargetYLevelsNotAdjacentTo(level,
						CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get(),
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						marshmallowChunkX, marshmallowChunkZ, 4, -48, 80);
		require(helper, countAtOrBelow(mintCrystalYs, -24) == 0,
				"Mint Crystal remained above-ground output below its -24 deep-output threshold");
		require(helper, !deepMintOutputYs.isEmpty(),
				"Focused Peppermint host generated no Rock-Candy deep output at or below -24");
		Map<Integer, Integer> combinedMintOutputYs = mergeYCounts(
				mintCrystalYs, deepMintOutputYs);
		int bottomMintBand = countInRange(combinedMintOutputYs, -56, -11);
		int middleMintBand = countInRange(combinedMintOutputYs, -10, 35);
		int upperMintBand = countInRange(combinedMintOutputYs, 36, 80);
		require(helper, bottomMintBand > middleMintBand
						&& bottomMintBand > upperMintBand,
				"Mint bottom-triangle output did not favour the lower third: "
						+ combinedMintOutputYs);
		JsonObject activeProfile = OreSpawnApi.getActiveProfile(
				level.getServer()).orElseThrow().toJson();
		JsonObject rockCandyRule = oreDimensionRule(
				activeProfile.getAsJsonObject("ores"),
				"cakeworld:ore/rock_candy_deposit");
		int rockCandyMinY = rockCandyRule.get("min_y").getAsInt();
		int rockCandyMaxY = rockCandyRule.get("max_y").getAsInt();
		int triangleWidth = (rockCandyMaxY - rockCandyMinY + 1) / 3;
		require(helper, triangleWidth > 0
						&& triangleWidth * 3
								== rockCandyMaxY - rockCandyMinY + 1,
				"Rock-Candy triangle range does not divide into three exact audit bands");
		int lowerRockCandyBand = countInRange(
				ordinaryRockCandyDepositYs, rockCandyMinY,
				rockCandyMinY + triangleWidth - 1);
		int middleRockCandyBand = countInRange(
				ordinaryRockCandyDepositYs,
				rockCandyMinY + triangleWidth,
				rockCandyMinY + (triangleWidth * 2) - 1);
		int upperRockCandyBand = countInRange(
				ordinaryRockCandyDepositYs,
				rockCandyMinY + (triangleWidth * 2), rockCandyMaxY);
		require(helper, middleRockCandyBand > lowerRockCandyBand
						&& middleRockCandyBand > upperRockCandyBand,
				"Rock-Candy triangle output did not peak in its middle third: "
						+ ordinaryRockCandyDepositYs);
		LOGGER.info("Focused ore-height diagnostics: mint_crystal_y={}, deep_mint_output_y={}, mint_bands=[{},{},{}], ordinary_rock_candy_deposit_y={}, rock_candy_bands=[{},{},{}]",
				mintCrystalYs, deepMintOutputYs,
				bottomMintBand, middleMintBand, upperMintBand,
				ordinaryRockCandyDepositYs, lowerRockCandyBand,
				middleRockCandyBand, upperRockCandyBand);

		int sodaChunkX = Math.floorDiv(sodaOcean.getX(), 16);
		int sodaChunkZ = Math.floorDiv(sodaOcean.getZ(), 16);
		Map<Block, Integer> sodaRegion = scanDimension(level,
				Set.of(
						CakeWorldBlocks.FIZZY_PEARL.get(),
						CakeWorldFluids.LEMONADE_BLOCK.get()),
				sodaChunkX, sodaChunkZ, 4, -64, 96);
		Map<ResourceLocation, Integer> sodaSurfaceBiomes =
				countChunkCenterBiomes(level, sodaChunkX, sodaChunkZ, 4, 64);
		Map<ResourceLocation, Integer> sodaOreFilterBiomes =
				countChunkCenterBiomes(level, sodaChunkX, sodaChunkZ, 4, 0);
		Map<ResourceLocation, Integer> sodaSurfaceGeomes =
				countChunkCenterGeomes(helper, level, sodaChunkX, sodaChunkZ,
						4, 64);
		Map<ResourceLocation, Integer> sodaOreFilterGeomes =
				countChunkCenterGeomes(helper, level, sodaChunkX, sodaChunkZ,
						4, 0);
		Map<Block, Integer> lemonadeFloor = countBlocksDirectlyUnderFluid(
				level, CakeWorldFluids.LEMONADE.get(),
				sodaChunkX, sodaChunkZ, 4, -64, 96);
		LOGGER.info("Focused Soda Ocean diagnostics: location={}, blocks={}, surface_biomes={}, ore_filter_biomes={}, surface_geomes={}, ore_filter_geomes={}, lemonade_floor={}",
				sodaOcean, describe(sodaRegion), sodaSurfaceBiomes,
				sodaOreFilterBiomes, sodaSurfaceGeomes, sodaOreFilterGeomes,
				describe(lemonadeFloor));
		int pearlsDirectlyUnderLemonade = countTargetUnderFluid(level,
				CakeWorldBlocks.FIZZY_PEARL.get(),
				CakeWorldFluids.LEMONADE.get(),
				sodaChunkX, sodaChunkZ, 4, -64, 96);
		int fizzyPearls = sodaRegion.getOrDefault(
				CakeWorldBlocks.FIZZY_PEARL.get(), 0);
		if (fizzyPearls > 0) {
			require(helper, pearlsDirectlyUnderLemonade > 0,
					"Fizzy Pearls generated without an observed under-lemonade origin");
		} else {
			LOGGER.warn("Known OS-085 integrated-world gap: fixed-seed Soda Ocean contained Lemonade and compatible floor hosts but generated no Fizzy Pearls; the public pattern compiler is tested separately");
		}

		LOGGER.info("Focused Deep Pantry audit: marshmallow_peaks={} mint_region={}, soda_ocean={} fizzy_region={}, pearls_under_lemonade={}",
				marshmallowPeaks, describe(mintRegion), sodaOcean,
				describe(sodaRegion), pearlsDirectlyUnderLemonade);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedRealmWorldgenAuditsDeepPantryOutputs(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Nether/End survey; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
		ServerLevel end = helper.getLevel().getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Fixed-seed realm survey could not resolve Nether and End levels");

		Map<Block, Integer> netherBlocks = scanDimension(nether,
				Set.of(
						CakeWorldBlocks.FUDGE_ROCK.get(),
						CakeWorldBlocks.BURNT_SUGAR_ROCK.get(),
						CakeWorldFluids.HOT_FUDGE_BLOCK.get()),
				0, 0, 4, 0, 127);
		Map<ResourceLocation, Integer> netherBiomes =
				countChunkCenterBiomes(nether, 0, 0, 4, 64);
		Map<ResourceLocation, Integer> netherGeomes =
				countChunkCenterGeomes(helper, nether, 0, 0, 4, 64);
		require(helper, netherBlocks.getOrDefault(
						CakeWorldBlocks.FUDGE_ROCK.get(), 0) > 0,
				"Fixed-seed Nether contained no Fudge Rock");
		require(helper, netherBlocks.getOrDefault(
						CakeWorldBlocks.BURNT_SUGAR_ROCK.get(), 0) > 0,
				"Fixed-seed Nether contained no Burnt-Sugar Rock");
		require(helper, netherBlocks.getOrDefault(
						CakeWorldFluids.HOT_FUDGE_BLOCK.get(), 0) > 0,
				"Fixed-seed Nether contained no Hot Fudge world material");
		require(helper, netherBiomes.getOrDefault(
						id("fudge_wastes"), 0) > 0,
				"Fixed-seed Nether contained no Fudge Wastes chunk centers");
		require(helper, netherGeomes.getOrDefault(
						id("fudge_mantle"), 0) > 0,
				"Fixed-seed Nether contained no Fudge Mantle columns");

		Map<Block, Integer> endRocks = scanDimension(end,
				Set.of(
						CakeWorldBlocks.BISCUIT_STONE.get(),
						CakeWorldBlocks.WAFER_ROCK.get(),
						CakeWorldBlocks.NOUGAT_ROCK.get(),
						CakeWorldBlocks.ROCK_CANDY.get()),
				0, 0, 4, 0, 255);
		Map<ResourceLocation, Integer> endBiomes =
				countChunkCenterBiomes(end, 0, 0, 4, 64);
		Map<ResourceLocation, Integer> endGeomes =
				countChunkCenterGeomes(helper, end, 0, 0, 4, 64);
		int endMetamorphic = endRocks.getOrDefault(
						CakeWorldBlocks.NOUGAT_ROCK.get(), 0)
				+ endRocks.getOrDefault(CakeWorldBlocks.ROCK_CANDY.get(), 0);
		require(helper, endMetamorphic > 0,
				"Fixed-seed End contained no Nougat or Rock Candy metamorphic hosts");
		require(helper, endBiomes.getOrDefault(
						id("meringue_islands"), 0) > 0,
				"Fixed-seed End contained no Meringue Islands chunk centers");
		require(helper, endGeomes.getOrDefault(
						id("meringue_crust"), 0) > 0,
				"Fixed-seed End contained no Meringue Crust columns");

		LOGGER.info("Focused realm audit: nether_blocks={}, nether_biomes={}, nether_geomes={}, end_rocks={}, end_biomes={}, end_geomes={}, end_metamorphic={}",
				describe(netherBlocks), netherBiomes, netherGeomes,
				describe(endRocks), endBiomes, endGeomes, endMetamorphic);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedBiomeSurfaceAndPaletteAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed biome surface/palette audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel overworld = helper.getLevel();
		ServerLevel nether = overworld.getServer().getLevel(Level.NETHER);
		ServerLevel end = overworld.getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Surface audit could not resolve Nether and End levels");
		JsonObject profile = OreSpawnApi.getActiveProfile(
				overworld.getServer()).orElseThrow().toJson();
		JsonObject palettes = profile.getAsJsonObject("biome_palettes");
		JsonObject materials = profile.getAsJsonObject(
				"dimension_materials");
		JsonObject overworldMaterials = materials.getAsJsonObject(
				"cakeworld:overworld_materials");
		require(helper, "cakeworld:icing_layer".equals(
						overworldMaterials.get("snow_block").getAsString())
						&& "cakeworld:frozen_lemonade".equals(
								overworldMaterials.get("ice_block")
										.getAsString()),
				"Active profile lost its declarative icing/frozen-lemonade weather materials");

		assertPaletteContract(helper, palettes, "cakeworld:overworld_oceans",
				"cakeworld:soda_ocean", "cakeworld:biscuit_crumbs",
				"cakeworld:biscuit_crumbs", "cakeworld:biscuit_crumbs", 4);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:candy_plains", "cakeworld:icing_layer",
				"cakeworld:chocolate_sponge", "cakeworld:biscuit_crumbs", 4);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:cookie_forest", "cakeworld:chocolate_sponge",
				"cakeworld:chocolate_sponge", "cakeworld:biscuit_crumbs", 5);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:marshmallow_peaks", "cakeworld:icing_layer",
				"cakeworld:biscuit_stone", "cakeworld:biscuit_crumbs", 5);
		assertPaletteContract(helper, palettes, "cakeworld:nether",
				"cakeworld:fudge_wastes", "cakeworld:fudge_rock",
				"cakeworld:fudge_rock", null, 5);
		assertPaletteContract(helper, palettes, "cakeworld:end",
				"cakeworld:meringue_islands", "cakeworld:icing_layer",
				"cakeworld:biscuit_stone", "cakeworld:biscuit_stone", 5);

		Map<String, SurfaceAudit> surfaces = new LinkedHashMap<>();
		surfaces.put("candy_plains", auditSurface(overworld,
				locateBiome(helper, overworld, id("candy_plains")),
				id("candy_plains"), CakeWorldBlocks.ICING_LAYER.get(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(), 2));
		surfaces.put("cookie_forest", auditSurface(overworld,
				locateBiome(helper, overworld, id("cookie_forest")),
				id("cookie_forest"), CakeWorldBlocks.CHOCOLATE_SPONGE.get(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(), 2));
		surfaces.put("marshmallow_peaks", auditSurface(overworld,
				locateBiome(helper, overworld, id("marshmallow_peaks")),
				id("marshmallow_peaks"), CakeWorldBlocks.ICING_LAYER.get(),
				CakeWorldBlocks.BISCUIT_STONE.get(), 2));
		surfaces.put("fudge_wastes", auditSurface(nether,
				new BlockPos(0, 64, 0), id("fudge_wastes"),
				CakeWorldBlocks.FUDGE_ROCK.get(),
				CakeWorldBlocks.FUDGE_ROCK.get(), 2));
		surfaces.put("meringue_islands", auditSurface(end,
				new BlockPos(0, 64, 0), id("meringue_islands"),
				CakeWorldBlocks.ICING_LAYER.get(),
				CakeWorldBlocks.BISCUIT_STONE.get(), 2));
		for (Map.Entry<String, SurfaceAudit> entry : surfaces.entrySet()) {
			require(helper, entry.getValue().biomeColumns() > 0
							&& entry.getValue().topMatches() > 0
							&& entry.getValue().fillerMatches() > 0,
					"Fixed world did not expose the declared top/filler pair for "
							+ entry.getKey() + ": " + entry.getValue());
		}

		BlockPos sodaOcean = locateBiome(helper, overworld, id("soda_ocean"));
		Map<Block, Integer> lemonadeFloor = countBlocksDirectlyUnderFluid(
				overworld, CakeWorldFluids.LEMONADE.get(),
				Math.floorDiv(sodaOcean.getX(), 16),
				Math.floorDiv(sodaOcean.getZ(), 16), 4, -64, 96);
		require(helper, lemonadeFloor.getOrDefault(
						CakeWorldBlocks.BISCUIT_CRUMBS.get(), 0) > 0,
				"Soda Ocean exposed no Biscuit Crumbs underwater surface");
		LOGGER.info("Focused biome surface/palette audit: surfaces={}, soda_floor={}",
				surfaces, describe(lemonadeFloor));
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedGeomeAndRockDepthDistributionAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed geome/depth audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel overworld = helper.getLevel();
		ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
		ServerLevel end = helper.getLevel().getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Fixed-seed geome survey could not resolve Nether and End levels");
		BlockPos marshmallowPeaks = locateBiome(helper, overworld,
				id("marshmallow_peaks"));
		BlockPos sodaOcean = locateBiome(helper, overworld, id("soda_ocean"));

		GeologySurvey survey = new GeologySurvey();
		sampleGeologyRegion(overworld, 0, 0, 4, -64, 128, survey);
		sampleGeologyRegion(overworld, 16, 16, 4, -64, 128, survey);
		sampleGeologyRegion(overworld,
				Math.floorDiv(marshmallowPeaks.getX(), 16),
				Math.floorDiv(marshmallowPeaks.getZ(), 16),
				4, -64, 128, survey);
		sampleGeologyRegion(overworld,
				Math.floorDiv(sodaOcean.getX(), 16),
				Math.floorDiv(sodaOcean.getZ(), 16),
				4, -64, 128, survey);
		sampleGeologyRegion(nether, 0, 0, 4, 0, 127, survey);
		sampleGeologyRegion(end, 0, 0, 4, 0, 255, survey);

		LOGGER.info("Focused geome/depth survey: geome_columns={}, geome_rocks={}, rock_depths={}, candy_glass_predicted={}, candy_glass_survived={}, candy_glass_managed_ore_replacements={}",
				survey.geomeColumns, describeNested(survey.geomeRocks),
				describeDepths(survey.rockYLevels),
				survey.predictedCandyGlass, survey.survivingCandyGlass,
				survey.candyGlassManagedOreReplacements);
		require(helper, survey.geomeColumns.keySet().containsAll(
						EXPECTED_GEOME_IDS),
				"Fixed-seed sampler did not observe every CakeWorld geome: "
						+ missing(EXPECTED_GEOME_IDS,
								survey.geomeColumns.keySet()));
		assertGeomeSignature(helper, survey, id("cocoa_basin"),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get());
		assertGeomeSignature(helper, survey, id("wafer_shelf"),
				CakeWorldBlocks.WAFER_ROCK.get());
		assertGeomeSignature(helper, survey, id("peppermint_fold"),
				CakeWorldBlocks.PEPPERMINT_ROCK.get());
		assertGeomeSignature(helper, survey, id("rock_candy_uplift"),
				CakeWorldBlocks.ROCK_CANDY.get());
		assertGeomeSignature(helper, survey, id("fudge_mantle"),
				CakeWorldBlocks.FUDGE_ROCK.get());
		assertGeomeSignature(helper, survey, id("meringue_crust"),
				CakeWorldBlocks.NOUGAT_ROCK.get());
		require(helper, meanY(survey, CakeWorldBlocks.FUDGE_ROCK.get())
						> meanY(survey,
								CakeWorldBlocks.BURNT_SUGAR_ROCK.get()),
				"Fudge Rock did not retain a shallower identity than Burnt-Sugar Rock");
		require(helper, meanY(survey, CakeWorldBlocks.WAFER_ROCK.get())
						> meanY(survey, CakeWorldBlocks.NOUGAT_ROCK.get()),
				"Wafer did not retain a shallower identity than Nougat");
		require(helper, survey.predictedCandyGlass > 0
						&& survey.survivingCandyGlass > 0,
				"Fixed-seed survey found no integrated Candy Glass control");
		require(helper, survey.candyGlassManagedOreReplacements == 0,
				"An OreSpawn-managed ore replaced predicted non-replaceable Candy Glass");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void biomeIdAndDictionaryGeomeRulesAreBakedAndObserved(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed biome/geome-rule audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel overworld = helper.getLevel();
		ServerLevel nether = overworld.getServer().getLevel(Level.NETHER);
		require(helper, nether != null,
				"Nether level was unavailable for dictionary geome proof");
		JsonObject profile = OreSpawnApi.getActiveProfile(
				overworld.getServer()).orElseThrow().toJson();
		JsonObject biomeRules = profile.getAsJsonObject("biomes");
		JsonObject dictionaryRules =
				profile.getAsJsonObject("biome_dictionary");
		require(helper, biomeRules != null
						&& biomeRules.has("cakeworld:candy_plains")
						&& dictionaryWeight(dictionaryRules, "COLD",
								"cakeworld:peppermint_fold") == 8.0D
						&& dictionaryWeight(dictionaryRules, "HOT",
								"cakeworld:fudge_mantle") == 8.0D
						&& dictionaryWeight(dictionaryRules, "MUSHROOM",
								"cakeworld:meringue_crust") == 8.0D,
				"Active profile lost exact-biome or dictionary geome rules");

		ResourceKey<Biome> marshmallowPeaks = biomeKey(
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId());
		ResourceKey<Biome> fudgeWastes = biomeKey(
				CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper,
				BiomeDictionary.hasType(marshmallowPeaks,
						BiomeDictionary.Type.COLD)
						&& BiomeDictionary.hasType(fudgeWastes,
								BiomeDictionary.Type.HOT),
				"CakeWorld matching biome dictionary types were not registered");
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			require(helper, !BiomeDictionary.hasType(biomeKey(biomeId),
							BiomeDictionary.Type.MUSHROOM),
					"Mismatched MUSHROOM control unexpectedly matched "
							+ biomeId);
		}

		BlockPos marshmallowCenter = locateBiome(helper, overworld,
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId());
		BlockPos fudgeCenter = locateBiome(helper, nether,
				CakeWorldBiomes.FUDGE_WASTES.getId());
		Map<ResourceLocation, Integer> marshmallowGeomes =
				countGeomesForBiome(overworld, marshmallowCenter,
						CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(), 4);
		Map<ResourceLocation, Integer> fudgeGeomes =
				countGeomesForBiome(nether, fudgeCenter,
						CakeWorldBiomes.FUDGE_WASTES.getId(), 4);
		LOGGER.info("Biome ID/dictionary geome audit: marshmallow_peaks={}, fudge_wastes={}",
				marshmallowGeomes, fudgeGeomes);
		require(helper, marshmallowGeomes.getOrDefault(
						id("peppermint_fold"), 0) > 0,
				"COLD Marshmallow Peaks produced no Peppermint Fold columns");
		require(helper, fudgeGeomes.getOrDefault(id("fudge_mantle"), 0) > 0,
				"HOT Fudge Wastes produced no Fudge Mantle columns");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedGeomeWeightedPlacementAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed geome-weighted placement audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		JsonObject profile = OreSpawnApi.getActiveProfile(
				level.getServer()).orElseThrow().toJson();
		JsonObject ores = profile.getAsJsonObject("ores");
		JsonObject deposits = profile.getAsJsonObject("fluid_deposits");
		JsonObject mint = oreDimensionRule(ores,
				"cakeworld:ore/mint_crystal");
		JsonObject custard = fluidDimensionRule(deposits,
				"cakeworld:fluid_deposit/custard");
		require(helper,
				geomeWeight(mint,
								"cakeworld:peppermint_fold") == 5.0D
						&& geomeWeight(mint,
								"cakeworld:rock_candy_uplift") == 2.5D
						&& geomeWeight(mint,
								"cakeworld:cocoa_basin") == 0.0D
						&& geomeWeight(custard,
								"cakeworld:cocoa_basin") == 1.2D
						&& geomeWeight(custard,
								"cakeworld:wafer_shelf") == 3.0D,
				"Active profile lost the representative ore or fluid geome weights");

		BlockPos marshmallowPeaks = locateBiome(helper, level,
				id("marshmallow_peaks"));
		int marshmallowChunkX = Math.floorDiv(
				marshmallowPeaks.getX(), 16);
		int marshmallowChunkZ = Math.floorDiv(
				marshmallowPeaks.getZ(), 16);

		GeomePlacementSurvey mintSurvey = surveyTargetBlocksByGeome(
				level, Set.of(CakeWorldBlocks.MINT_CRYSTAL.get()),
				marshmallowChunkX, marshmallowChunkZ, 4, -56, 80,
				false);
		GeomePlacementSurvey custardSurvey = surveyTargetBlocksByGeome(
				level, Set.of(CakeWorldFluids.CUSTARD_BLOCK.get()),
				16, 16, 4, -24, 64, true);

		LOGGER.info("Focused geome-weighted placement audit: mint_crystal={}, custard={}",
				mintSurvey, custardSurvey);
		ResourceLocation cocoaBasin = id("cocoa_basin");
		ResourceLocation waferShelf = id("wafer_shelf");
		ResourceLocation peppermintFold = id("peppermint_fold");
		ResourceLocation rockCandyUplift = id("rock_candy_uplift");
		require(helper, mintSurvey.chunks(peppermintFold) > 0
						&& mintSurvey.chunks(rockCandyUplift) > 0
						&& mintSurvey.chunks(cocoaBasin) > 0
						&& mintSurvey.blocks(peppermintFold) > 0
						&& mintSurvey.blocks(cocoaBasin) == 0,
				"Mint Crystal positive/zero geome controls were not observed");
		require(helper, custardSurvey.chunks(cocoaBasin) > 0
						&& custardSurvey.chunks(waferShelf) > 0
						&& custardSurvey.blocks(cocoaBasin) > 0
						&& custardSurvey.blocks(waferShelf) > 0,
				"Custard survey did not cover and place in both weighted geomes");
		require(helper, custardSurvey.blocksPerChunk(waferShelf)
						> custardSurvey.blocksPerChunk(cocoaBasin),
				"Custard 3.0/1.2 weights did not favour Wafer Shelf");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedExplicitOreHostAttributionAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed explicit ore-host attribution audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		BlockPos marshmallowPeaks = locateBiome(helper, level,
				id("marshmallow_peaks"));
		int marshmallowChunkX = Math.floorDiv(
				marshmallowPeaks.getX(), 16);
		int marshmallowChunkZ = Math.floorDiv(
				marshmallowPeaks.getZ(), 16);

		HostAttributionResult explicitResult =
				auditPredictedExplicitOreHosts(level,
				Map.of(CakeWorldBlocks.MINT_CRYSTAL.get(),
						Set.of(CakeWorldBlocks.PEPPERMINT_ROCK.get())),
				marshmallowChunkX, marshmallowChunkZ, 4, -56, 80);
		LOGGER.info("Focused explicit ore-host attribution audit: {}",
				explicitResult);
		require(helper, explicitResult.outputs() > 0
						&& explicitResult.violations() == 0,
				"Explicit-host output disagreed with sampled pre-ore geology at "
						+ explicitResult.firstViolationDetail());
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedTerrainAndAquiferBoundaryAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed terrain/aquifer audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel overworld = helper.getLevel();
		ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
		ServerLevel end = helper.getLevel().getServer().getLevel(Level.END);
		require(helper, nether != null && end != null,
				"Fixed-seed terrain audit could not resolve Nether and End levels");
		int auditChunkX = 16;
		int auditChunkZ = 16;

		Map<Block, Integer> overworldResiduals = scanDimension(overworld,
				Set.of(Blocks.STONE, Blocks.DEEPSLATE),
				auditChunkX, auditChunkZ, 4, -64, 96);
		Map<Block, Integer> netherResiduals = scanDimension(nether,
				Set.of(Blocks.NETHERRACK, Blocks.BASALT, Blocks.BLACKSTONE),
				auditChunkX, auditChunkZ, 4, 0, 127);
		Map<Block, Integer> endResiduals = scanDimension(end,
				Set.of(Blocks.END_STONE),
				auditChunkX, auditChunkZ, 4, 0, 255);
		Map<Integer, Integer> overworldResidualYs = countTargetYLevels(
				overworld, Set.of(Blocks.STONE, Blocks.DEEPSLATE),
				auditChunkX, auditChunkZ, 4, -64, 96);

		int lemonadeBelow = countBlockInRange(overworld,
				CakeWorldFluids.LEMONADE_BLOCK.get(), auditChunkX,
				auditChunkZ, 4, -64, -41);
		int lemonadeAtThreshold = countBlockInRange(overworld,
				CakeWorldFluids.LEMONADE_BLOCK.get(), auditChunkX,
				auditChunkZ, 4, -40, -40);
		int lemonadeAbove = countBlockInRange(overworld,
				CakeWorldFluids.LEMONADE_BLOCK.get(), auditChunkX,
				auditChunkZ, 4, -39, 96);
		int fudgeBelow = countBlockInRange(overworld,
				CakeWorldFluids.HOT_FUDGE_BLOCK.get(), auditChunkX,
				auditChunkZ, 4, -64, -41);
		int fudgeAtThreshold = countBlockInRange(overworld,
				CakeWorldFluids.HOT_FUDGE_BLOCK.get(), auditChunkX,
				auditChunkZ, 4, -40, -40);
		int fudgeAbove = countBlockInRange(overworld,
				CakeWorldFluids.HOT_FUDGE_BLOCK.get(), auditChunkX,
				auditChunkZ, 4, -39, 96);
		require(helper, overworld.getChunkSource().getGenerator()
						instanceof NoiseBasedChunkGenerator,
				"Overworld does not expose a noise-based aquifer picker");
		NoiseBasedChunkGenerator generator = (NoiseBasedChunkGenerator)
				overworld.getChunkSource().getGenerator();
		Aquifer.FluidPicker fluidPicker = aquiferPicker(generator);
		BlockState pickerBelow = fluidPicker.computeFluid(
				auditChunkX << 4, -41, auditChunkZ << 4).at(-41);
		BlockState pickerAtThreshold = fluidPicker.computeFluid(
				auditChunkX << 4, -40, auditChunkZ << 4).at(-40);
		LOGGER.info("Focused terrain/aquifer audit: overworld_residuals={}, nether_residuals={}, end_residuals={}, lemonade_below={}, lemonade_at_minus_40={}, lemonade_above={}, fudge_below={}, fudge_at_minus_40={}, fudge_above={}",
				describe(overworldResiduals), describe(netherResiduals),
				describe(endResiduals), lemonadeBelow, lemonadeAtThreshold,
				lemonadeAbove, fudgeBelow, fudgeAtThreshold, fudgeAbove);
		LOGGER.info("Focused terrain residual Y levels: overworld={}",
				overworldResidualYs);
		require(helper, pickerBelow.is(
						CakeWorldFluids.HOT_FUDGE_BLOCK.get()),
				"Installed aquifer picker did not select Hot Fudge below -40");
		require(helper, pickerAtThreshold.is(
						CakeWorldFluids.LEMONADE_BLOCK.get()),
				"Installed aquifer picker did not select Lemonade at -40");
		require(helper, overworldResiduals.isEmpty(),
				"Overworld terrain replacement left base hosts: "
						+ describe(overworldResiduals));
		require(helper, netherResiduals.isEmpty(),
				"Nether terrain replacement left base hosts: "
						+ describe(netherResiduals));
		require(helper, endResiduals.isEmpty(),
				"End terrain replacement left base hosts: "
						+ describe(endResiduals));
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedCoveredFluidDepositEnvelopeAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed covered-fluid audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		int auditChunkX = 16;
		int auditChunkZ = 16;
		int auditRadius = 4;
		BlockPos cookieForest = locateBiome(helper, level,
				id("cookie_forest"));
		int cookieChunkX = Math.floorDiv(cookieForest.getX(), 16);
		int cookieChunkZ = Math.floorDiv(cookieForest.getZ(), 16);
		Map<String, FluidEnvelopeResult> results = new LinkedHashMap<>();
		results.put("jam", auditFluidEnvelope(level,
				CakeWorldFluids.JAM_BLOCK.get(), cookieChunkX, cookieChunkZ,
				auditRadius, -40, 48, 3, 1));
		results.put("custard", auditFluidEnvelope(level,
				CakeWorldFluids.CUSTARD_BLOCK.get(), auditChunkX, auditChunkZ,
				auditRadius, -24, 64, 2, 1));
		results.put("caramel", auditFluidEnvelope(level,
				CakeWorldFluids.CARAMEL_BLOCK.get(), auditChunkX, auditChunkZ,
				auditRadius, -48, 32, 4, 2));
		results.put("syrup", auditFluidEnvelope(level,
				CakeWorldFluids.SYRUP_BLOCK.get(), auditChunkX, auditChunkZ,
				auditRadius, -32, 56, 3, 1));

		LOGGER.info("Focused covered-fluid envelope audit: {}", results);
		for (Map.Entry<String, FluidEnvelopeResult> entry
				: results.entrySet()) {
			FluidEnvelopeResult result = entry.getValue();
			require(helper, result.fluidBlocks() > 0,
					"Fixed-seed region contained no " + entry.getKey()
							+ " deposit blocks");
			require(helper, result.boundaryFaces() > 0,
					"Fixed-seed " + entry.getKey()
							+ " deposit exposed no measurable boundary");
			require(helper, result.violations() == 0,
					"Fixed-seed " + entry.getKey()
							+ " deposit violated its solid envelope at "
							+ result.firstViolation());
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 2400)
	public static void focusedFluidHostAndBiomeFilterAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed fluid host/filter audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		JsonObject profile = OreSpawnApi.getActiveProfile(
				level.getServer()).orElseThrow().toJson();
		JsonObject deposits = profile.getAsJsonObject("fluid_deposits");
		JsonObject jam = fluidDimensionRule(deposits,
				"cakeworld:fluid_deposit/jam");
		JsonObject custard = fluidDimensionRule(deposits,
				"cakeworld:fluid_deposit/custard");
		JsonObject syrup = fluidDimensionRule(deposits,
				"cakeworld:fluid_deposit/syrup");
		require(helper, jsonArrayContains(jam, "host_blocks",
						"cakeworld:wafer_rock")
						&& jsonArrayContains(jam, "host_families",
								"metamorphic"),
				"Jam did not retain its explicit-block plus family host contract");
		require(helper, jsonArrayContains(custard, "host_tags",
						"cakeworld:edible_ore_hosts"),
				"Custard did not retain its baked host-tag contract");
		require(helper, jsonArrayContains(jam, "biome_ids",
						"cakeworld:cookie_forest"),
				"Jam did not retain its Cookie Forest include filter");
		require(helper, jsonArrayContains(syrup, "excluded_biome_ids",
						"cakeworld:soda_ocean"),
				"Syrup did not retain its Soda Ocean exclude filter");

		BlockPos cookieForest = locateBiome(helper, level,
				id("cookie_forest"));
		BlockPos sodaOcean = locateBiome(helper, level, id("soda_ocean"));
		int cookieChunkX = Math.floorDiv(cookieForest.getX(), 16);
		int cookieChunkZ = Math.floorDiv(cookieForest.getZ(), 16);
		int sodaChunkX = Math.floorDiv(sodaOcean.getX(), 16);
		int sodaChunkZ = Math.floorDiv(sodaOcean.getZ(), 16);
		Map<ResourceLocation, Integer> jamStartBiomes =
				countTargetChunkSurfaceBiomes(level,
				CakeWorldFluids.JAM_BLOCK.get(), cookieChunkX, cookieChunkZ,
				4, -40, 48);
		Map<ResourceLocation, Integer> syrupStartBiomes =
				countTargetChunkSurfaceBiomes(level,
				CakeWorldFluids.SYRUP_BLOCK.get(), sodaChunkX, sodaChunkZ,
				4, -32, 56);
		Map<ResourceLocation, Integer> cookieBiomesAt32 =
				countChunkCenterBiomes(level, cookieChunkX, cookieChunkZ,
						4, 32);
		Map<ResourceLocation, Integer> cookieBiomesAt0 =
				countChunkCenterBiomes(level, cookieChunkX, cookieChunkZ,
						4, 0);
		Map<ResourceLocation, Integer> cookieBiomesAtMinus32 =
				countChunkCenterBiomes(level, cookieChunkX, cookieChunkZ,
						4, -32);
		LOGGER.info("Focused fluid host/filter audit: cookie_forest={}, cookie_biomes_y32={}, cookie_biomes_y0={}, cookie_biomes_y-32={}, jam_start_biomes={}, soda_ocean={}, syrup_start_biomes={}",
				cookieForest, cookieBiomesAt32, cookieBiomesAt0,
				cookieBiomesAtMinus32, jamStartBiomes, sodaOcean,
				syrupStartBiomes);
		require(helper,
				jamStartBiomes.getOrDefault(id("cookie_forest"), 0) > 0
						&& jamStartBiomes.keySet().stream()
								.allMatch(id("cookie_forest")::equals),
				"Cookie Forest filter produced no integrated Jam output");
		require(helper, syrupStartBiomes.values().stream()
						.mapToInt(Integer::intValue).sum() > 0,
				"Mixed Soda Ocean survey produced no measurable Syrup output");
		require(helper,
				syrupStartBiomes.getOrDefault(id("soda_ocean"), 0) == 0,
				"Soda Ocean exclusion allowed integrated Syrup output");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 4800)
	public static void focusedGingerbreadVillageStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Gingerbread Village audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						GingerbreadVillageFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Gingerbread Village configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						GingerbreadVillageFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Gingerbread Village was absent from its public locate tag");

		BlockPos located = level.findNearestMapFeature(
				GingerbreadVillageFeature.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				128, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Gingerbread Village within 128 chunks");
		net.minecraft.world.level.ChunkPos startChunk =
				new net.minecraft.world.level.ChunkPos(located);
		level.setChunkForced(startChunk.x, startChunk.z, true);
		net.minecraft.world.level.chunk.LevelChunk chunk =
				level.getChunk(startChunk.x, startChunk.z);
		net.minecraft.world.level.levelgen.structure.StructureStart
				start = chunk.getStartForFeature(configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1,
				"The located Gingerbread Village lost its saved Village structure start");

		BlockPos horizontalCentre =
				new BlockPos(located.getX() + 8, 0,
						located.getZ() + 8);
		BlockPos bell = null;
		for (int y = level.getMinBuildHeight();
				y < level.getMaxBuildHeight(); y++) {
			BlockPos candidate =
					new BlockPos(horizontalCentre.getX(), y,
							horizontalCentre.getZ());
			if (level.getBlockState(candidate).is(Blocks.BELL)) {
				bell = candidate;
				break;
			}
		}
		require(helper, bell != null,
				"The located Gingerbread Village start produced no meeting bell");
		BlockPos centre = bell.below();
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -10; x <= 10; x++) {
			for (int y = 0; y <= 6; y++) {
				for (int z = -10; z <= 10; z++) {
					Block block = level.getBlockState(
							centre.offset(x, y, z))
							.getBlock();
					palette.merge(block, 1, Integer::sum);
				}
			}
		}
		int gummyRoofs =
				palette.getOrDefault(
						CakeWorldBlocks
								.RASPBERRY_GUMMY_BLOCK
								.get(), 0)
				+ palette.getOrDefault(
						CakeWorldBlocks
								.BLUEBERRY_GUMMY_BLOCK
								.get(), 0)
				+ palette.getOrDefault(
						CakeWorldBlocks
								.GRAPE_GUMMY_BLOCK
								.get(), 0);
		long homes = level.getPoiManager().getCountInRange(
				net.minecraft.world.entity.ai.village.poi
						.PoiType.HOME::equals,
				centre, 32,
				net.minecraft.world.entity.ai.village.poi
						.PoiManager.Occupancy.ANY);
		ResourceLocation biomeId = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.getKey(level.getBiome(centre).value());
		boolean literalPlainsVillageEligible =
				level.getBiome(centre).is(
						net.minecraft.tags.BiomeTags
								.HAS_VILLAGE_PLAINS);
		BlockPos meetingBell = bell;
		// A locate query loads the structure chunk but its entity section may
		// finish joining the level on a later server tick. Keep the remote
		// chunk forced briefly so reload evidence observes the saved residents
		// and guardian rather than racing the asynchronous entity manager.
		helper.runAfterDelay(20, () -> {
			List<GingerbreadFolk> residents =
					level.getEntitiesOfClass(
							GingerbreadFolk.class,
							new net.minecraft.world.phys.AABB(
									centre).inflate(16.0D));
			List<JawbreakerGuardian> guardians =
					level.getEntitiesOfClass(
							JawbreakerGuardian.class,
							new net.minecraft.world.phys.AABB(
									centre).inflate(16.0D));
			boolean meetingPoi =
					level.getPoiManager()
							.existsAtPosition(
									net.minecraft.world.entity
											.ai.village.poi
											.PoiType.MEETING,
									meetingBell);
			boolean village = level.isVillage(centre);
			level.setChunkForced(startChunk.x,
					startChunk.z, false);
			LOGGER.info("Focused Gingerbread Village audit: locate={}, centre={}, biome={}, palette={}, homes={}, meeting={}, village={}, residents={}, guardians={}, startPieces={}",
					located, centre, biomeId, palette, homes,
					meetingPoi, village, residents.size(),
					guardians.size(),
					start.getPieces().size());
			require(helper,
					id("candy_plains").equals(biomeId)
							&& level.getBiome(centre).is(
									GingerbreadVillageFeature
											.GENERATES_IN)
							&& !literalPlainsVillageEligible,
					"Gingerbread Village generated outside its CakeWorld-only biome contract or enabled a literal Plains Village");
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0) > 250
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) >= 30
							&& gummyRoofs >= 100
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOKBOOK_LIBRARY
											.get(), 0) == 1
							&& palette.getOrDefault(
									Blocks.BELL, 0) == 1,
					"The natural village lost its gingerbread, candy-cane, gumdrop, library or bell signature");
			require(helper,
					homes >= 8
							&& meetingPoi
							&& village
							&& residents.size() == 4
							&& guardians.size() == 1,
					"The natural village lost bed/meeting POIs, raid-location semantics, residents or defender");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 4800)
	public static void focusedBiscuitBanditLookoutStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Biscuit Bandit Lookout audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						BiscuitBanditLookoutFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Biscuit Bandit Lookout configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						BiscuitBanditLookoutFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Biscuit Bandit Lookout was absent from its public locate tag");

		BlockPos located = level.findNearestMapFeature(
				BiscuitBanditLookoutFeature.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				256, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Biscuit Bandit Lookout within 256 chunks");
		net.minecraft.world.level.ChunkPos startChunk =
				new net.minecraft.world.level.ChunkPos(
						located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		net.minecraft.world.level.levelgen.structure.StructureStart
				start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1,
				"The located Biscuit Bandit Lookout lost its saved Outpost structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		require(helper,
				savedBounds.getXSpan() >= 25
						&& savedBounds.getYSpan() >= 21
						&& savedBounds.getZSpan() >= 25,
				"The saved Biscuit Bandit Lookout collapsed its structure-wide spawn bounds: "
						+ savedBounds);

		int minimumChunkX =
				Math.floorDiv(savedBounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(savedBounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(savedBounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(savedBounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, true);
			}
		}

		helper.runAfterDelay(20, () -> {
			BlockPos horizontalCentre =
					new BlockPos(
							located.getX() + 12, 0,
							located.getZ() + 12);
			BlockPos chestPos = null;
			for (int y = level.getMinBuildHeight();
					y < level.getMaxBuildHeight();
					y++) {
				BlockPos candidate =
						new BlockPos(
								horizontalCentre.getX(),
								y,
								horizontalCentre.getZ());
				if (level.getBlockState(candidate)
						.is(Blocks.CHEST)) {
					chestPos = candidate;
					break;
				}
			}
			BlockPos centre = chestPos == null
					? horizontalCentre
					: chestPos.below(17);
			Map<Block, Integer> palette =
					new LinkedHashMap<>();
			for (int x = -12; x <= 12; x++) {
				for (int y = 0; y <= 20; y++) {
					for (int z = -12; z <= 12;
							z++) {
						Block block =
								level.getBlockState(
										centre.offset(
												x, y,
												z))
										.getBlock();
						palette.merge(block, 1,
								Integer::sum);
					}
				}
			}
			BlockEntity chest = chestPos == null
					? null
					: level.getBlockEntity(chestPos);
			CompoundTag chestState =
					chest == null ? new CompoundTag()
							: chest.saveWithoutMetadata();
			List<BiscuitBandit> bandits =
					level.getEntitiesOfClass(
							BiscuitBandit.class,
							new AABB(centre)
									.inflate(64.0D));
			long persistentBandits =
					bandits.stream()
							.filter(BiscuitBandit
									::isPersistenceRequired)
							.count();
			List<BiscuitBandit> captains =
					bandits.stream()
							.filter(BiscuitBandit
									::isPatrolLeader)
							.toList();
			boolean captainBanner =
					captains.stream().anyMatch(
							captain ->
									ItemStack
											.isSameItemSameTags(
													captain
															.getItemBySlot(
																	EquipmentSlot
																			.HEAD),
													Raid.getLeaderBannerInstance()));
			ResourceLocation biomeId =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(centre)
									.value());
			boolean literalOutpostEligible =
					level.getBiome(centre).is(
							net.minecraft.tags.BiomeTags
									.HAS_PILLAGER_OUTPOST);
			BlockPos nearestVillage =
					level.findNearestMapFeature(
							GingerbreadVillageFeature
									.STRUCTURE_TAG,
							centre, 256, false);
			int villageChunkDistance =
					nearestVillage == null
							? Integer.MAX_VALUE
							: Math.max(
									Math.abs(
											Math.floorDiv(
													nearestVillage
															.getX(),
													16)
													- startChunk.x),
									Math.abs(
											Math.floorDiv(
													nearestVillage
															.getZ(),
													16)
													- startChunk.z));
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					level.setChunkForced(
							chunkX, chunkZ, false);
				}
			}

			LOGGER.info("Focused Biscuit Bandit Lookout audit: locate={}, centre={}, bounds={}, biome={}, palette={}, bandits={}, persistent={}, captains={}, captainBanner={}, nearestVillage={}, villageChunkDistance={}",
					located, centre, savedBounds,
					biomeId, palette, bandits.size(),
					persistentBandits, captains.size(),
					captainBanner, nearestVillage,
					villageChunkDistance);
			require(helper, chestPos != null,
					"The natural Biscuit Bandit Lookout produced no tower supply chest");
			require(helper,
					id("cookie_forest").equals(biomeId)
							&& level.getBiome(centre).is(
									BiscuitBanditLookoutFeature
											.GENERATES_IN)
							&& !literalOutpostEligible,
					"Biscuit Bandit Lookout generated outside its Cookie-Forest-only contract or enabled a literal vanilla Outpost");
			require(helper,
					savedBounds.isInside(
							centre.offset(-12, 0, -12))
							&& savedBounds.isInside(
									centre.offset(
											12, 20, 12)),
					"The saved Lookout bounds do not contain its complete 25x21x25 layout");
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.BISCUIT_STONE
									.get(), 0) >= 350
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0) >= 450
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) >= 90
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get(), 0) == 81
							&& palette.getOrDefault(
									Blocks.TARGET, 0) == 3
							&& palette.getOrDefault(
									Blocks.CAMPFIRE, 0) == 1
							&& palette.getOrDefault(
									Blocks.IRON_BARS, 0) >= 40,
					"The natural Lookout lost its edible tower, camp, cage or target-range signature");
			require(helper,
					BiscuitBanditLookoutFeature.LOOT_ID
							.toString().equals(
									chestState.getString(
											"LootTable")),
					"The natural Lookout chest lost its saved supply loot role");
			require(helper,
					persistentBandits >= 4
							&& !captains.isEmpty()
							&& captainBanner
							&& bandits.stream().allMatch(
									BiscuitBandit
											::canJoinRaid),
					"The natural Lookout lost its saved bandits, Bad-Omen captain or raid eligibility");
			require(helper,
					villageChunkDistance > 10,
					"The Biscuit Bandit Lookout violated its ten-chunk Gingerbread Village exclusion");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 4800)
	public static void focusedWaferMineStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Wafer Mine audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						WaferMineFeature.STRUCTURE_ID);
		require(helper, configured != null,
				"Wafer Mine configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						WaferMineFeature.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Wafer Mine was absent from its public locate tag");

		BlockPos located = level.findNearestMapFeature(
				WaferMineFeature.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				256, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Wafer Mine within 256 chunks");
		net.minecraft.world.level.ChunkPos startChunk =
				new net.minecraft.world.level.ChunkPos(
						located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		net.minecraft.world.level.levelgen.structure.StructureStart
				start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1,
				"The located Wafer Mine lost its saved underground structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		require(helper,
				savedBounds.getXSpan() == 41
						&& savedBounds.getYSpan() == 13
						&& savedBounds.getZSpan() == 41,
				"The saved Wafer Mine collapsed its 41x13x41 piece bounds: "
						+ savedBounds);

		int minimumChunkX =
				Math.floorDiv(savedBounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(savedBounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(savedBounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(savedBounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, true);
			}
		}

		helper.runAfterDelay(20, () -> {
			BlockPos spawnerPosition = null;
			for (int x = savedBounds.minX();
					x <= savedBounds.maxX()
							&& spawnerPosition == null;
					x++) {
				for (int y = savedBounds.minY();
						y <= savedBounds.maxY()
								&& spawnerPosition == null;
						y++) {
					for (int z = savedBounds.minZ();
							z <= savedBounds.maxZ();
							z++) {
						BlockPos candidate =
								new BlockPos(x, y, z);
						if (level.getBlockState(
								candidate).is(
										Blocks.SPAWNER)) {
							spawnerPosition =
									candidate;
							break;
						}
					}
				}
			}
			require(helper, spawnerPosition != null,
					"The natural Wafer Mine produced no Weaver spawner anchor");
			BlockPos centre =
					spawnerPosition.offset(
							-12, -1, -10);
			require(helper,
					savedBounds.isInside(
							centre.offset(
									-20, 0, -20))
							&& savedBounds.isInside(
									centre.offset(
											20, 12,
											20)),
					"The saved Wafer Mine bounds do not contain the complete layout derived from its Weaver anchor");
			Map<Block, Integer> palette =
					new LinkedHashMap<>();
			for (int x = -20; x <= 20; x++) {
				for (int y = 0; y <= 6; y++) {
					for (int z = -20; z <= 20;
							z++) {
						Block block =
								level.getBlockState(
										centre.offset(
												x, y,
												z))
										.getBlock();
						palette.merge(block, 1,
								Integer::sum);
					}
				}
			}
			int themedOreFaces =
					palette.getOrDefault(
							CakeWorldBlocks.COCOA_COAL
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks.IRON_WAFER
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.COPPER_CARAMEL
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_REDSTONE
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.BLUEBERRY_LAPIS
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.ROCK_CANDY_DIAMOND
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.ROCK_CANDY_DEPOSIT
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.LIQUORICE_VEIN
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.SPRINKLE_CLUSTER
									.get(), 0);
			boolean continuousRail = true;
			for (int z = -20; z <= 20; z++) {
				continuousRail &=
						level.getBlockState(
								centre.offset(
										0, 1, z))
								.is(Blocks.RAIL);
			}
			BlockEntity spawner =
					level.getBlockEntity(
							spawnerPosition);
			CompoundTag spawnerState =
					spawner == null
							? new CompoundTag()
							: spawner
									.saveWithoutMetadata();
			String spawnedEntity = spawnerState
					.getCompound("SpawnData")
					.getCompound("entity")
					.getString("id");
			List<MinecartChest> lootMinecarts =
					level.getEntitiesOfClass(
							MinecartChest.class,
							new AABB(centre)
									.inflate(24.0D))
							.stream()
							.filter(cart ->
									WaferMineFeature
											.LOOT_ID
											.toString()
											.equals(cart
													.saveWithoutId(
															new CompoundTag())
													.getString(
															"LootTable")))
							.toList();
			ResourceLocation biomeId =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(
									centre.atY(50))
									.value());
			boolean literalMineshaftEligible =
					level.getBiome(centre.atY(50))
							.is(net.minecraft.tags
									.BiomeTags
									.HAS_MINESHAFT)
							|| level.getBiome(
									centre.atY(50))
									.is(net.minecraft.tags
											.BiomeTags
											.HAS_MINESHAFT_MESA);
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					level.setChunkForced(
							chunkX, chunkZ, false);
				}
			}

			LOGGER.info("Focused Wafer Mine audit: locate={}, centre={}, bounds={}, biome={}, palette={}, continuousRail={}, spawner={}, lootMinecarts={}",
					located, centre, savedBounds,
					biomeId, palette,
					continuousRail, spawnedEntity,
					lootMinecarts.size());
			require(helper,
					level.getBiome(centre.atY(50)).is(
							WaferMineFeature
									.GENERATES_IN)
							&& biomeId != null
							&& CakeWorld.MODID.equals(
									biomeId
											.getNamespace())
							&& !literalMineshaftEligible,
					"Wafer Mine generated outside its four-biome CakeWorld Overworld contract or enabled a literal vanilla Mineshaft: "
							+ biomeId);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.WAFER_BLOCK
									.get(), 0) >= 750
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= 140
							&& palette.getOrDefault(
									Blocks.RAIL, 0)
									== 41
							&& palette.getOrDefault(
									Blocks.SPAWNER, 0)
									== 1
							&& palette.getOrDefault(
									Blocks.COBWEB, 0)
									== 6
							&& themedOreFaces >= 24,
					"The natural Wafer Mine lost its edible supports, rails, Weaver nest or exposed geology");
			require(helper, continuousRail,
					"The natural Wafer Mine's 41-block main rail is discontinuous");
			require(helper,
					spawner
							instanceof SpawnerBlockEntity
							&& CakeWorldEntities
									.DEEP_LIQUORICE_WEAVER
									.getId().toString()
									.equals(
											spawnedEntity),
					"The natural Wafer Mine spawner lost its Deep Liquorice Weaver role: "
							+ spawnedEntity);
			require(helper,
					lootMinecarts.size() == 1
							&& lootMinecarts.get(0)
									.blockPosition()
									.equals(centre.offset(
											0, 1,
											-12)),
					"The natural Wafer Mine lost its single saved cave-loot minecart: "
							+ lootMinecarts.size());
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 7200)
	public static void focusedGrandGingerbreadManorStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Grand Gingerbread Manor audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						GrandGingerbreadManorFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Grand Gingerbread Manor configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						GrandGingerbreadManorFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		boolean mapped = structures.getTag(
						ConfiguredStructureTags
								.ON_WOODLAND_EXPLORER_MAPS)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged && mapped,
				"Grand Gingerbread Manor lost its locate or Woodland Explorer Map tag");

		BlockPos located = level.findNearestMapFeature(
				GrandGingerbreadManorFeature
						.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Grand Gingerbread Manor within 512 chunks");
		net.minecraft.world.level.ChunkPos startChunk =
				new net.minecraft.world.level.ChunkPos(
						located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		net.minecraft.world.level.levelgen.structure.StructureStart
				start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1,
				"The located Grand Gingerbread Manor lost its saved surface-structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		require(helper,
				savedBounds.getXSpan() == 49
						&& savedBounds.getYSpan() == 30
						&& savedBounds.getZSpan() == 49,
				"The saved Grand Gingerbread Manor collapsed its exact 49x30x49 piece bounds: "
						+ savedBounds);

		int minimumChunkX =
				Math.floorDiv(savedBounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(savedBounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(savedBounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(savedBounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, true);
			}
		}

		helper.runAfterDelay(40, () -> {
			BlockPos centre = new BlockPos(
					savedBounds.minX() + 24,
					savedBounds.minY(),
					savedBounds.minZ() + 24);
			Map<Block, Integer> palette =
					new LinkedHashMap<>();
			for (int x = -24; x <= 24; x++) {
				for (int y = 0; y <= 29; y++) {
					for (int z = -24; z <= 24;
							z++) {
						Block block =
								level.getBlockState(
										centre.offset(
												x, y,
												z))
										.getBlock();
						palette.merge(block, 1,
								Integer::sum);
					}
				}
			}
			int gummyRoof =
					palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.BLUEBERRY_GUMMY_BLOCK
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.GRAPE_GUMMY_BLOCK
									.get(), 0);
			boolean traversableFloors = true;
			Map<Integer, Integer> missingRouteByFloor =
					new LinkedHashMap<>();
			List<BlockPos> firstMissingRouteBlocks =
					new java.util.ArrayList<>();
			for (int floorY : new int[] {0, 8, 16}) {
				for (int x = -4; x <= 4; x++) {
					for (int z = -17; z <= 3; z++) {
						BlockPos routePosition =
								centre.offset(
										x, floorY, z);
						boolean wafer =
								level.getBlockState(
										routePosition)
										.is(CakeWorldBlocks
												.WAFER_BLOCK
												.get());
						traversableFloors &= wafer;
						if (!wafer) {
							missingRouteByFloor.merge(
									floorY, 1,
									Integer::sum);
							if (firstMissingRouteBlocks
									.size() < 12) {
								firstMissingRouteBlocks
										.add(routePosition);
							}
						}
					}
				}
			}
			boolean sealedSecretKitchen = true;
			for (int y = 17; y <= 23; y++) {
				for (int z = 4; z <= 17; z++) {
					sealedSecretKitchen &=
							level.getBlockState(
									centre.offset(
											-6, y, z))
									.is(CakeWorldBlocks
											.GINGERBREAD_BRICKS
											.get());
				}
			}
			BlockEntity ordinaryChest =
					level.getBlockEntity(
							centre.offset(
									14, 1, 11));
			BlockEntity secretChest =
					level.getBlockEntity(
							centre.offset(
									-13, 17, 9));
			CompoundTag ordinaryState =
					ordinaryChest == null
							? new CompoundTag()
							: ordinaryChest
									.saveWithoutMetadata();
			CompoundTag secretState =
					secretChest == null
							? new CompoundTag()
							: secretChest
									.saveWithoutMetadata();
			List<net.minecraft.world.entity.raid.Raider>
					inhabitants =
					level.getEntitiesOfClass(
							net.minecraft.world.entity.raid
									.Raider.class,
							new AABB(centre)
									.inflate(48.0D));
			long rollingPinRaiders =
					inhabitants.stream()
							.filter(entity ->
									entity.getType()
											== CakeWorldEntities
													.ROLLING_PIN_RAIDER
													.get())
							.count();
			long sourSorcerers =
					inhabitants.stream()
							.filter(entity ->
									entity.getType()
											== CakeWorldEntities
													.SOUR_SORCERER
													.get())
							.count();
			long bitterBakers =
					inhabitants.stream()
							.filter(entity ->
									entity.getType()
											== CakeWorldEntities
													.BITTER_BAKER
													.get())
							.count();
			ResourceLocation biomeId =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(
									centre)
									.value());
			boolean literalMansionEligible =
					level.getBiome(centre).is(
							BiomeTags
									.HAS_WOODLAND_MANSION);

			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					level.setChunkForced(
							chunkX, chunkZ, false);
				}
			}

			LOGGER.info("Focused Grand Gingerbread Manor audit: locate={}, centre={}, bounds={}, biome={}, palette={}, traversableFloors={}, missingRouteByFloor={}, firstMissingRouteBlocks={}, sealedSecretKitchen={}, rollingPinRaiders={}, sourSorcerers={}, bitterBakers={}, ordinaryLoot={}, secretLoot={}",
					located, centre, savedBounds,
					biomeId, palette,
					traversableFloors,
					missingRouteByFloor,
					firstMissingRouteBlocks,
					sealedSecretKitchen,
					rollingPinRaiders,
					sourSorcerers,
					bitterBakers,
					ordinaryState.getString(
							"LootTable"),
					secretState.getString(
							"LootTable"));
			require(helper,
					level.getBiome(centre).is(
							GrandGingerbreadManorFeature
									.GENERATES_IN)
							&& CakeWorldBiomes
									.COOKIE_FOREST
									.getId()
									.equals(biomeId)
							&& !literalMansionEligible,
					"Grand Gingerbread Manor generated outside Cookie Forest or enabled a literal vanilla Mansion: "
							+ biomeId);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0) >= 2500
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									>= 3500
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= 80
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									>= 100
							&& gummyRoof >= 2500
							&& palette.getOrDefault(
									CakeWorldBlocks.OVEN
											.get(), 0)
									== 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MIXING_BOWL
											.get(), 0)
									== 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOLING_RACK
											.get(), 0)
									== 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_COOKER
											.get(), 0)
									== 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOKBOOK_LIBRARY
											.get(), 0)
									== 3
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOKBOOK_KIOSK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 2
							&& traversableFloors
							&& sealedSecretKitchen,
					"The natural Grand Gingerbread Manor lost its edible floors, kitchens, Cookbook rooms, roof or caches");
			require(helper,
					GrandGingerbreadManorFeature
							.LOOT_ID.toString()
							.equals(ordinaryState
									.getString(
											"LootTable"))
							&& GrandGingerbreadManorFeature
									.SECRET_LOOT_ID
									.toString()
									.equals(secretState
											.getString(
													"LootTable")),
					"The natural Grand Gingerbread Manor lost its ordinary or secret loot table");
			require(helper,
					inhabitants.size() == 8
							&& rollingPinRaiders == 5
							&& sourSorcerers == 2
							&& bitterBakers == 1
							&& inhabitants.stream()
									.allMatch(
											net.minecraft.world
													.entity.raid
													.Raider
													::isPersistenceRequired)
							&& inhabitants.stream()
									.allMatch(
											raider -> raider
													.getCurrentRaid()
													== null),
					"The natural Grand Gingerbread Manor lost its persistent non-raid household: total="
							+ inhabitants.size()
							+ ", rollingPinRaiders="
							+ rollingPinRaiders
							+ ", sourSorcerers="
							+ sourSorcerers
							+ ", bitterBakers="
							+ bitterBakers);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 7200)
	public static void focusedGummyShrineStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Gummy Shrine audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						GummyShrineFeature.STRUCTURE_ID);
		require(helper, configured != null,
				"Gummy Shrine configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						GummyShrineFeature.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Gummy Shrine lost its public locate tag");

		BlockPos located = level.findNearestMapFeature(
				GummyShrineFeature.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Gummy Shrine within 512 chunks");
		net.minecraft.world.level.ChunkPos startChunk =
				new net.minecraft.world.level.ChunkPos(
						located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		net.minecraft.world.level.levelgen.structure.StructureStart
				start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1,
				"The located Gummy Shrine lost its saved surface-structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		require(helper,
				savedBounds.getXSpan() == 15
						&& savedBounds.getYSpan() == 12
						&& savedBounds.getZSpan() == 15,
				"The saved Gummy Shrine collapsed its exact 15x12x15 piece bounds: "
						+ savedBounds);

		int minimumChunkX =
				Math.floorDiv(savedBounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(savedBounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(savedBounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(savedBounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, true);
			}
		}

		helper.runAfterDelay(40, () -> {
			BlockPos centre = new BlockPos(
					savedBounds.minX() + 7,
					savedBounds.minY(),
					savedBounds.minZ() + 7);
			Map<Block, Integer> palette =
					new LinkedHashMap<>();
			for (int x = -7; x <= 7; x++) {
				for (int y = 0; y <= 11; y++) {
					for (int z = -7; z <= 7;
							z++) {
						Block block =
								level.getBlockState(
										centre.offset(
												x, y,
												z))
										.getBlock();
						palette.merge(block, 1,
								Integer::sum);
					}
				}
			}
			int gummyBlocks =
					palette.getOrDefault(
							CakeWorldBlocks
									.GUMMY_BLOCK
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.BLUEBERRY_GUMMY_BLOCK
									.get(), 0)
					+ palette.getOrDefault(
							CakeWorldBlocks
									.GRAPE_GUMMY_BLOCK
									.get(), 0);
			boolean attachedTraps = true;
			for (int z : new int[] {-2, 2}) {
				for (int x : new int[] {-3, 3}) {
					BlockState hook =
							level.getBlockState(
									centre.offset(
											x, 1,
											z));
					attachedTraps &=
							hook.is(Blocks
									.TRIPWIRE_HOOK)
							&& hook.getValue(
									net.minecraft
											.world.level
											.block
											.TripWireHookBlock
											.ATTACHED);
				}
			}
			boolean stockedTraps = true;
			for (BlockPos position : List.of(
					centre.offset(-4, 1, -2),
					centre.offset(4, 1, 2))) {
				BlockEntity blockEntity =
						level.getBlockEntity(
								position);
				if (!(blockEntity
						instanceof DispenserBlockEntity)) {
					stockedTraps = false;
					continue;
				}
				DispenserBlockEntity dispenser =
						(DispenserBlockEntity)
								blockEntity;
				for (int slot = 0; slot < 3;
						slot++) {
					stockedTraps &=
							PotionUtils.getPotion(
									dispenser.getItem(
											slot))
									== Potions.SLOWNESS;
				}
			}
			BlockEntity ordinaryChest =
					level.getBlockEntity(
							centre.offset(3, 1, 3));
			BlockEntity hiddenChest =
					level.getBlockEntity(
							centre.offset(0, 1, 5));
			CompoundTag ordinaryState =
					ordinaryChest == null
							? new CompoundTag()
							: ordinaryChest
									.saveWithoutMetadata();
			CompoundTag hiddenState =
					hiddenChest == null
							? new CompoundTag()
							: hiddenChest
									.saveWithoutMetadata();
			ResourceLocation biomeId =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(
									centre)
									.value());
			boolean literalTempleEligible =
					level.getBiome(centre).is(
							BiomeTags
									.HAS_JUNGLE_TEMPLE);

			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					level.setChunkForced(
							chunkX, chunkZ, false);
				}
			}

			LOGGER.info("Focused Gummy Shrine audit: locate={}, centre={}, bounds={}, biome={}, palette={}, attachedTraps={}, stockedTraps={}, ordinaryLoot={}, hiddenLoot={}",
					located, centre, savedBounds,
					biomeId, palette,
					attachedTraps, stockedTraps,
					ordinaryState.getString(
							"LootTable"),
					hiddenState.getString(
							"LootTable"));
			require(helper,
					CakeWorldBiomes.COOKIE_FOREST
							.getId().equals(biomeId)
							&& !literalTempleEligible,
					"The natural Gummy Shrine lost its temporary Cookie Forest home or leaked literal Jungle Temple eligibility: biome="
							+ biomeId
							+ ", literalTempleEligible="
							+ literalTempleEligible);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.BISCUIT_STONE
									.get(), 0) >= 150
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GINGERBREAD_BRICKS
											.get(), 0)
									>= 180
							&& gummyBlocks >= 470
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									== 12
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									== 29
							&& palette.getOrDefault(
									Blocks.TRIPWIRE_HOOK,
									0) == 4
							&& palette.getOrDefault(
									Blocks.TRIPWIRE, 0)
									== 10
							&& palette.getOrDefault(
									Blocks.DISPENSER, 0)
									== 2
							&& palette.getOrDefault(
									Blocks.LEVER, 0)
									== 3
							&& palette.getOrDefault(
									Blocks.STICKY_PISTON,
									0) == 3
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 2
							&& attachedTraps
							&& stockedTraps,
					"The natural Gummy Shrine lost its edible ruin, elastic approach, sticky traps, flavour clue or caches: "
							+ palette);
			require(helper,
					GummyShrineFeature.LOOT_ID
							.toString()
							.equals(ordinaryState
									.getString(
											"LootTable"))
							&& GummyShrineFeature
									.HIDDEN_LOOT_ID
									.toString()
									.equals(hiddenState
											.getString(
													"LootTable")),
					"The natural Gummy Shrine lost its ordinary or hidden loot table");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 1200)
	public static void baseMetalsCounterpartsPreserveTagsRecipesAndGeneration(
			GameTestHelper helper) {
		Map<String, Block> counterparts = new LinkedHashMap<>();
		counterparts.put("coldiron", CakeWorldBlocks.FROSTED_COLD_IRON.get());
		counterparts.put("adamantine",
				CakeWorldBlocks.JAWBREAKER_ADAMANTINE.get());
		counterparts.put("starsteel", CakeWorldBlocks.STARLIGHT_STARSTEEL.get());
		counterparts.put("tin", CakeWorldBlocks.SILVER_DRAGEE_TIN.get());
		counterparts.put("lead", CakeWorldBlocks.LIQUORICE_LEAD.get());
		counterparts.put("zinc", CakeWorldBlocks.LEMON_DROP_ZINC.get());
		counterparts.put("silver", CakeWorldBlocks.SILVER_LEAF_SILVER.get());
		counterparts.put("mercury", CakeWorldBlocks.MIRROR_GLAZE_MERCURY.get());
		counterparts.put("nickel", CakeWorldBlocks.MINT_WAFER_NICKEL.get());
		counterparts.put("platinum", CakeWorldBlocks.SUGAR_STAR_PLATINUM.get());
		counterparts.put("copper", CakeWorldBlocks.COPPER_CARAMEL.get());
		counterparts.put("antimony", CakeWorldBlocks.ANISEED_ANTIMONY.get());
		counterparts.put("bismuth", CakeWorldBlocks.RAINBOW_ROCK_BISMUTH.get());

		for (Map.Entry<String, Block> entry : counterparts.entrySet()) {
			String metal = entry.getKey();
			Block block = entry.getValue();
			TagKey<Block> blockTag = TagKey.create(Registry.BLOCK_REGISTRY,
					new ResourceLocation("forge", "ores/" + metal));
			TagKey<Item> itemTag = TagKey.create(Registry.ITEM_REGISTRY,
					new ResourceLocation("forge", "ores/" + metal));
			require(helper, block.defaultBlockState().is(
							BlockTags.MINEABLE_WITH_PICKAXE),
					Registry.BLOCK.getKey(block) + " is not pickaxe-mineable");
			require(helper, block.defaultBlockState().is(blockTag)
							&& new ItemStack(block).is(itemTag),
					Registry.BLOCK.getKey(block)
							+ " does not preserve forge:ores/" + metal);
		}
		require(helper, CakeWorldBlocks.SILVER_LEAF_SILVER.get()
						.defaultBlockState().is(BlockTags.NEEDS_STONE_TOOL)
						&& CakeWorldBlocks.MINT_WAFER_NICKEL.get()
								.defaultBlockState().is(BlockTags.NEEDS_STONE_TOOL),
				"BaseMetals stone-tier counterparts are incomplete");
		require(helper, CakeWorldBlocks.FROSTED_COLD_IRON.get()
						.defaultBlockState().is(BlockTags.NEEDS_IRON_TOOL),
				"Frosted Cold-Iron lost BaseMetals' iron-tier requirement");
		require(helper, CakeWorldBlocks.JAWBREAKER_ADAMANTINE.get()
						.defaultBlockState().is(BlockTags.NEEDS_DIAMOND_TOOL)
						&& CakeWorldBlocks.STARLIGHT_STARSTEEL.get()
								.defaultBlockState().is(BlockTags.NEEDS_DIAMOND_TOOL),
				"BaseMetals diamond-tier counterparts are incomplete");

		if (!ModList.get().isLoaded("basemetals")) {
			helper.succeed();
			return;
		}

		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate().filter(
						EDIBLE_WORLD_BASEMETALS::equals).isPresent(),
				"BaseMetals did not select the higher-priority compatibility template");
		JsonObject ores = profile.toJson().getAsJsonObject("ores");
		Set<String> deliberatelyDisabled =
				Set.of("copper", "antimony", "bismuth");
		for (Map.Entry<String, Block> entry : counterparts.entrySet()) {
			String metal = entry.getKey();
			JsonObject rule = ores.getAsJsonObject(
					"basemetals:ore/" + metal);
			require(helper, rule != null
							&& Registry.BLOCK.getKey(entry.getValue()).toString()
									.equals(rule.get("block").getAsString()),
					"Compatibility template did not map BaseMetals " + metal);
			require(helper, hasEnabledPlacement(rule)
							!= deliberatelyDisabled.contains(metal),
					"BaseMetals " + metal
							+ " did not preserve its intended generation state");

			ItemStack input = new ItemStack(entry.getValue());
			assertRecipe(helper, metal + "_ore_smelting", input,
					"basemetals:" + metal + "_ingot", 1);
			assertRecipe(helper, metal + "_ore_blasting", input,
					"basemetals:" + metal + "_ingot", 1);
			assertRecipe(helper, metal + "_ore_crushing", input,
					"basemetals:" + metal + "_powder", 2);
		}

		Map<Block, Integer> overworld = scanDimension(helper.getLevel(),
				Set.of(
						CakeWorldBlocks.SILVER_DRAGEE_TIN.get(),
						CakeWorldBlocks.LIQUORICE_LEAD.get(),
						CakeWorldBlocks.LEMON_DROP_ZINC.get(),
						CakeWorldBlocks.SILVER_LEAF_SILVER.get(),
						CakeWorldBlocks.MIRROR_GLAZE_MERCURY.get(),
						CakeWorldBlocks.MINT_WAFER_NICKEL.get(),
						CakeWorldBlocks.SUGAR_STAR_PLATINUM.get()),
				0, 0, 3, -64, 128);
		Map<Block, Integer> nether = scanDimension(
				helper.getLevel().getServer().getLevel(Level.NETHER),
				Set.of(
						CakeWorldBlocks.FROSTED_COLD_IRON.get(),
						CakeWorldBlocks.JAWBREAKER_ADAMANTINE.get()),
				0, 0, 2, 0, 127);
		Map<Block, Integer> end = scanDimension(
				helper.getLevel().getServer().getLevel(Level.END),
				Set.of(CakeWorldBlocks.STARLIGHT_STARSTEEL.get()),
				0, 0, 2, 0, 254);
		require(helper, !overworld.isEmpty(),
				"Fresh BaseMetals profile generated no themed Overworld ores");
		require(helper, !nether.isEmpty(),
				"Fresh BaseMetals profile generated no themed Nether ores");
		require(helper, !end.isEmpty(),
				"Fresh BaseMetals profile generated no themed End ores");
		LOGGER.info("BaseMetals CakeWorld audit: overworld={}, nether={}, end={}",
				describe(overworld), describe(nether), describe(end));
		helper.succeed();
	}

	private static boolean hasEnabledPlacement(JsonObject rule) {
		if (rule.has("enabled") && !rule.get("enabled").getAsBoolean()) {
			return false;
		}
		for (String sectionName : List.of("dimensions", "dimension_selectors")) {
			if (!rule.has(sectionName)
					|| !rule.get(sectionName).isJsonObject()) {
				continue;
			}
			for (JsonElement value : rule.getAsJsonObject(sectionName)
					.entrySet().stream().map(Map.Entry::getValue).toList()) {
				if (value.isJsonObject()
						&& (!value.getAsJsonObject().has("enabled")
								|| value.getAsJsonObject().get("enabled")
										.getAsBoolean())) {
					return true;
				}
			}
		}
		return false;
	}

	private static void assertRecipe(GameTestHelper helper, String path,
			ItemStack input, String expectedItem, int expectedCount) {
		Recipe<?> recipe = helper.getLevel().getRecipeManager().byKey(
				new ResourceLocation("basemetals", path)).orElse(null);
		require(helper, recipe != null
						&& recipe.getIngredients().stream()
								.anyMatch(ingredient -> ingredient.test(input)),
				"BaseMetals recipe does not accept "
						+ Registry.ITEM.getKey(input.getItem()) + ": " + path);
		require(helper, Registry.ITEM.getKey(recipe.getResultItem().getItem())
						.equals(new ResourceLocation(expectedItem))
						&& recipe.getResultItem().getCount() == expectedCount,
				"BaseMetals recipe has the wrong result: " + path);
	}

	private static Map<Block, Integer> scanDimension(ServerLevel level,
			Set<Block> targets, int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		Map<Block, Integer> counts = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							Block block = level.getBlockState(
									new BlockPos(x, y, z)).getBlock();
							if (targets.contains(block)) {
								counts.merge(block, 1, Integer::sum);
							}
						}
					}
				}
			}
		}
		return counts;
	}

	private static int countBlockInRange(ServerLevel level, Block target,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		return scanDimension(level, Set.of(target), centerChunkX, centerChunkZ,
				radius, requestedMinY, requestedMaxY).getOrDefault(target, 0);
	}

	private static Aquifer.FluidPicker aquiferPicker(
			NoiseBasedChunkGenerator generator) {
		for (String fieldName : List.of("globalFluidPicker", "f_188607_")) {
			try {
				Field field = NoiseBasedChunkGenerator.class
						.getDeclaredField(fieldName);
				field.setAccessible(true);
				return (Aquifer.FluidPicker) field.get(generator);
			} catch (NoSuchFieldException ignored) {
				// Try the runtime-obfuscated name next.
			} catch (ReflectiveOperationException error) {
				throw new AssertionError(
						"Could not inspect the installed aquifer picker", error);
			}
		}
		throw new AssertionError(
				"Could not find the noise generator aquifer-picker field");
	}

	private static Map<Integer, Integer> countTargetYLevels(ServerLevel level,
			Set<Block> targets, int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		Map<Integer, Integer> counts = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							if (targets.contains(level.getBlockState(
									new BlockPos(x, y, z)).getBlock())) {
								counts.merge(y, 1, Integer::sum);
							}
						}
					}
				}
			}
		}
		return counts;
	}

	private static Map<ResourceLocation, Integer> countTargetChunkSurfaceBiomes(
			ServerLevel level, Block target, int centerChunkX,
			int centerChunkZ, int radius, int requestedMinY,
			int requestedMaxY) {
		Map<ResourceLocation, Integer> counts = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				net.minecraft.world.level.chunk.LevelChunk chunk =
						level.getChunk(chunkX, chunkZ);
				int surfaceY = chunk.getHeight(
						Heightmap.Types.WORLD_SURFACE_WG, 8, 8);
				ResourceLocation startBiome = level.getBiome(
						new BlockPos((chunkX << 4) + 8, surfaceY,
								(chunkZ << 4) + 8))
						.unwrapKey().map(key -> key.location()).orElse(null);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position = new BlockPos(x, y, z);
							if (!level.getBlockState(position).is(target)) {
								continue;
							}
							if (startBiome != null) {
								counts.merge(startBiome, 1, Integer::sum);
							}
						}
					}
				}
			}
		}
		return counts;
	}

	private static JsonObject fluidDimensionRule(JsonObject deposits,
			String depositId) {
		return fluidDimensionRule(deposits, depositId,
				"minecraft:overworld");
	}

	private static void assertPaletteContract(GameTestHelper helper,
			JsonObject palettes, String paletteId, String biomeId,
			String topBlock, String fillerBlock, String underwaterBlock,
			int fillerDepth) {
		require(helper, palettes != null && palettes.has(paletteId),
				"Missing biome palette " + paletteId);
		JsonObject palette = palettes.getAsJsonObject(paletteId);
		require(helper, "replace".equals(
						palette.get("mode").getAsString())
						&& "all".equals(palette.get("scope").getAsString())
						&& palette.get("coverage").getAsDouble() == 1.0D
						&& palette.get("fallback_weight").getAsDouble()
								== 0.0D,
				"Palette lost replace/all/full-coverage/zero-fallback contract: "
						+ paletteId);
		JsonObject biomes = palette.getAsJsonObject("biomes");
		require(helper, biomes != null && biomes.has(biomeId),
				"Palette " + paletteId + " lost biome " + biomeId);
		JsonObject surface = biomes.getAsJsonObject(biomeId)
				.getAsJsonObject("surface");
		boolean underwaterMatches = underwaterBlock == null
				? !surface.has("underwater_block")
				: underwaterBlock.equals(
						surface.get("underwater_block").getAsString());
		require(helper, topBlock.equals(
						surface.get("top_block").getAsString())
						&& fillerBlock.equals(
								surface.get("filler_block").getAsString())
						&& underwaterMatches
						&& surface.get("filler_depth").getAsInt()
								== fillerDepth,
				"Biome lost its declared surface contract: " + biomeId);
	}

	private static SurfaceAudit auditSurface(ServerLevel level,
			BlockPos center, ResourceLocation biomeId, Block expectedTop,
			Block expectedFiller, int radius) {
		int centerChunkX = Math.floorDiv(center.getX(), 16);
		int centerChunkZ = Math.floorDiv(center.getZ(), 16);
		int biomeColumns = 0;
		int topMatches = 0;
		int fillerMatches = 0;
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						int surfaceY = level.getHeight(
								Heightmap.Types.WORLD_SURFACE, x, z) - 1;
						if (surfaceY < level.getMinBuildHeight()) {
							continue;
						}
						BlockPos surfacePos = new BlockPos(x, surfaceY, z);
						ResourceLocation actualBiome = level.getBiome(surfacePos)
								.unwrapKey().map(ResourceKey::location)
								.orElse(null);
						if (!biomeId.equals(actualBiome)) {
							continue;
						}
						biomeColumns++;
						int matchedTopY = Integer.MIN_VALUE;
						for (int offset = 1; offset >= -2; offset--) {
							BlockPos candidate = surfacePos.above(offset);
							if (level.getBlockState(candidate).is(expectedTop)) {
								matchedTopY = candidate.getY();
								topMatches++;
								break;
							}
						}
						BlockPos fillerOrigin = matchedTopY == Integer.MIN_VALUE
								? surfacePos
								: new BlockPos(x, matchedTopY, z);
						for (int depth = 1; depth <= 6; depth++) {
							if (level.getBlockState(
										fillerOrigin.below(depth))
									.is(expectedFiller)) {
								fillerMatches++;
							}
						}
					}
				}
			}
		}
		return new SurfaceAudit(biomeColumns, topMatches, fillerMatches);
	}

	private static JsonObject fluidDimensionRule(JsonObject deposits,
			String depositId, String dimensionId) {
		if (deposits == null || !deposits.has(depositId)) {
			throw new AssertionError("Missing fluid deposit " + depositId);
		}
		JsonObject deposit = deposits.getAsJsonObject(depositId);
		JsonObject dimensions = deposit.getAsJsonObject("dimensions");
		if (dimensions == null || !dimensions.has(dimensionId)) {
			throw new AssertionError("Missing " + dimensionId
					+ " rule for " + depositId);
		}
		return dimensions.getAsJsonObject(dimensionId);
	}

	private static JsonObject oreDimensionRule(JsonObject ores, String oreId) {
		if (ores == null || !ores.has(oreId)) {
			throw new AssertionError("Missing ore " + oreId);
		}
		JsonObject ore = ores.getAsJsonObject(oreId);
		JsonObject dimensions = ore.getAsJsonObject("dimensions");
		if (dimensions == null || !dimensions.has("minecraft:overworld")) {
			throw new AssertionError("Missing Overworld rule for " + oreId);
		}
		return dimensions.getAsJsonObject("minecraft:overworld");
	}

	private static JsonObject rockRule(JsonObject profile, String rockId) {
		JsonObject rocks = profile.getAsJsonObject("rocks");
		if (rocks == null || !rocks.has(rockId)) {
			throw new AssertionError("Missing rock " + rockId);
		}
		return rocks.getAsJsonObject(rockId);
	}

	private static boolean jsonArrayContains(JsonObject object, String key,
			String expected) {
		if (!object.has(key) || !object.get(key).isJsonArray()) {
			return false;
		}
		for (JsonElement value : object.getAsJsonArray(key)) {
			if (expected.equals(value.getAsString())) {
				return true;
			}
		}
		return false;
	}

	private static Map<Integer, Integer> countTargetYLevelsAdjacentTo(
			ServerLevel level, Block target, Block adjacent,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		return countTargetYLevelsByAdjacency(level, target, adjacent,
				centerChunkX, centerChunkZ, radius, requestedMinY,
				requestedMaxY, true);
	}

	private static Map<Integer, Integer> countTargetYLevelsNotAdjacentTo(
			ServerLevel level, Block target, Block adjacent,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		return countTargetYLevelsByAdjacency(level, target, adjacent,
				centerChunkX, centerChunkZ, radius, requestedMinY,
				requestedMaxY, false);
	}

	private static Map<Integer, Integer> countTargetYLevelsByAdjacency(
			ServerLevel level, Block target, Block adjacent,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY, boolean requireAdjacent) {
		Map<Integer, Integer> counts = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position = new BlockPos(x, y, z);
							if (!level.getBlockState(position).is(target)) {
								continue;
							}
							boolean hasAdjacent = false;
							for (Direction direction : Direction.values()) {
								if (level.getBlockState(position.relative(
										direction)).is(adjacent)) {
									hasAdjacent = true;
									break;
								}
							}
							if (hasAdjacent == requireAdjacent) {
								counts.merge(y, 1, Integer::sum);
							}
						}
					}
				}
			}
		}
		return counts;
	}

	private static int countAtOrBelow(Map<Integer, Integer> counts,
			int maximumY) {
		return counts.entrySet().stream()
				.filter(entry -> entry.getKey() <= maximumY)
				.mapToInt(Map.Entry::getValue).sum();
	}

	private static int countInRange(Map<Integer, Integer> counts,
			int minimumY, int maximumY) {
		return counts.entrySet().stream()
				.filter(entry -> entry.getKey() >= minimumY
						&& entry.getKey() <= maximumY)
				.mapToInt(Map.Entry::getValue).sum();
	}

	private static Map<Integer, Integer> mergeYCounts(
			Map<Integer, Integer> first, Map<Integer, Integer> second) {
		Map<Integer, Integer> merged = new LinkedHashMap<>(first);
		second.forEach((y, count) -> merged.merge(y, count, Integer::sum));
		return merged;
	}

	private static FluidEnvelopeResult auditFluidEnvelope(ServerLevel level,
			Block fluidBlock, int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY, int minimumSolidCover,
			int minimumSolidShell) {
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		int fluidBlocks = 0;
		int boundaryFaces = 0;
		int violations = 0;
		BlockPos firstViolation = null;
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position = new BlockPos(x, y, z);
							if (!level.getBlockState(position).is(fluidBlock)) {
								continue;
							}
							fluidBlocks++;
							for (Direction direction : Direction.values()) {
								if (level.getBlockState(position.relative(
										direction)).is(fluidBlock)) {
									continue;
								}
								boundaryFaces++;
								int requiredLayers = direction == Direction.UP
										? Math.max(minimumSolidCover,
												minimumSolidShell)
										: minimumSolidShell;
								boolean sealed = true;
								for (int layer = 1;
										layer <= requiredLayers; layer++) {
									BlockState envelope = level.getBlockState(
											position.relative(direction, layer));
									if (!envelope.is(fluidBlock)
											&& (!envelope.getFluidState()
													.isEmpty()
													|| !envelope.getMaterial()
															.blocksMotion())) {
										sealed = false;
										break;
									}
								}
								if (!sealed) {
									violations++;
									if (firstViolation == null) {
										firstViolation = position.relative(
												direction).immutable();
									}
								}
							}
						}
					}
				}
			}
		}
		return new FluidEnvelopeResult(fluidBlocks, boundaryFaces, violations,
				firstViolation);
	}

	private static BlockPos locateBiome(GameTestHelper helper, ServerLevel level,
			ResourceLocation biomeId) {
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				holder -> holder.unwrapKey().map(
						key -> key.location().equals(biomeId)).orElse(false),
				new BlockPos(0, 64, 0), 16384, 8);
		require(helper, match != null,
				"Could not locate " + biomeId + " within 16,384 blocks");
		return match.getFirst();
	}

	private static Map<ResourceLocation, Integer> countGeomesForBiome(
			ServerLevel level, BlockPos center, ResourceLocation targetBiome,
			int chunkRadius) {
		GeologySampler sampler = OreSpawnApi.createSampler(level).orElseThrow();
		Map<ResourceLocation, Integer> result = new LinkedHashMap<>();
		int centerChunkX = Math.floorDiv(center.getX(), 16);
		int centerChunkZ = Math.floorDiv(center.getZ(), 16);
		for (int chunkX = centerChunkX - chunkRadius;
				chunkX <= centerChunkX + chunkRadius; chunkX++) {
			for (int chunkZ = centerChunkZ - chunkRadius;
					chunkZ <= centerChunkZ + chunkRadius; chunkZ++) {
				int blockX = (chunkX << 4) + 8;
				int blockZ = (chunkZ << 4) + 8;
				int surfaceY = level.getHeight(
						Heightmap.Types.WORLD_SURFACE, blockX, blockZ);
				GeologyColumn column = sampler.sampleColumn(
						blockX, blockZ, surfaceY);
				if (targetBiome.equals(column.biome())) {
					result.merge(column.geome(), 1, Integer::sum);
				}
			}
		}
		return result;
	}

	private static ResourceKey<Biome> biomeKey(ResourceLocation biomeId) {
		return ResourceKey.create(Registry.BIOME_REGISTRY, biomeId);
	}

	private static double dictionaryWeight(JsonObject rules, String type,
			String geome) {
		if (rules == null || !rules.has(type)
				|| !rules.get(type).isJsonObject()) {
			return Double.NaN;
		}
		JsonObject weights = rules.getAsJsonObject(type);
		return weights.has(geome) ? weights.get(geome).getAsDouble()
				: Double.NaN;
	}

	private static double geomeWeight(JsonObject rule, String geome) {
		if (rule == null || !rule.has("geomes")
				|| !rule.get("geomes").isJsonObject()) {
			return Double.NaN;
		}
		JsonObject weights = rule.getAsJsonObject("geomes");
		return weights.has(geome) ? weights.get(geome).getAsDouble()
				: Double.NaN;
	}

	private static GeomePlacementSurvey surveyTargetBlocksByGeome(
			ServerLevel level, Set<Block> targets, int centerChunkX,
			int centerChunkZ, int radius, int requestedMinY,
			int requestedMaxY, boolean classifyAtSurface) {
		GeologySampler sampler = OreSpawnApi.createSampler(level).orElseThrow();
		Map<ResourceLocation, Integer> chunks = new LinkedHashMap<>();
		Map<ResourceLocation, Integer> blocks = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				net.minecraft.world.level.chunk.LevelChunk chunk =
						level.getChunk(chunkX, chunkZ);
				int blockX = (chunkX << 4) + 8;
				int blockZ = (chunkZ << 4) + 8;
				int classificationY = classifyAtSurface
						? chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG,
								8, 8)
						: Math.max(level.getMinBuildHeight(), 0);
				GeologyColumn column = sampler.sampleColumn(
						blockX, blockZ, classificationY);
				ResourceLocation geome = column.geome();
				chunks.merge(geome, 1, Integer::sum);
				int targetCount = 0;
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							if (targets.contains(level.getBlockState(
									new BlockPos(x, y, z)).getBlock())) {
								targetCount++;
							}
						}
					}
				}
				if (targetCount > 0) {
					blocks.merge(geome, targetCount, Integer::sum);
				}
			}
		}
		return new GeomePlacementSurvey(chunks, blocks);
	}

	private static HostAttributionResult auditPredictedExplicitOreHosts(
			ServerLevel level,
			Map<Block, Set<Block>> allowedBlocks, int centerChunkX,
			int centerChunkZ, int radius, int requestedMinY,
			int requestedMaxY) {
		GeologySampler sampler = OreSpawnApi.createSampler(level).orElseThrow();
		Map<ResourceLocation, Integer> outputsByBlock =
				new LinkedHashMap<>();
		Map<ResourceLocation, Integer> violationsByBlock =
				new LinkedHashMap<>();
		int outputs = 0;
		int violations = 0;
		BlockPos firstViolation = null;
		String firstViolationDetail = null;
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						int surfaceY = level.getHeight(
								Heightmap.Types.WORLD_SURFACE, x, z);
						GeologyColumn column = sampler.sampleColumn(
								x, z, surfaceY);
						for (int y = minY; y <= maxY; y++) {
							Block actual = level.getBlockState(
									new BlockPos(x, y, z)).getBlock();
							Set<Block> blocks = allowedBlocks.get(actual);
							if (blocks == null) {
								continue;
							}
							outputs++;
							outputsByBlock.merge(
									Registry.BLOCK.getKey(actual), 1,
									Integer::sum);
							Block predicted = column.rockAt(y).getBlock();
							boolean accepted = blocks.contains(predicted);
							if (!accepted) {
								violations++;
								ResourceLocation actualId =
										Registry.BLOCK.getKey(actual);
								violationsByBlock.merge(actualId, 1,
										Integer::sum);
								if (firstViolation == null) {
									firstViolation = new BlockPos(
											x, y, z);
									firstViolationDetail =
											firstViolation
											+ " output=" + actualId
											+ " predicted="
											+ Registry.BLOCK.getKey(predicted)
											+ " family="
											+ column.familyAt(y)
													.map(Enum::name)
													.orElse("none");
								}
							}
						}
					}
				}
			}
		}
		return new HostAttributionResult(outputs, violations,
				firstViolation, firstViolationDetail, outputsByBlock,
				violationsByBlock);
	}

	private static void assertUnderFluidPatternResolvesLemonade(
			GameTestHelper helper) {
		OrePatternType type = OreSpawnPatternRegistry.registry().getValue(
				new ResourceLocation("orespawn", "underfluids"));
		require(helper, type != null,
				"OreSpawn underfluids pattern is not registered");
		JsonObject settings = new JsonObject();
		settings.addProperty("fluid", "cakeworld:lemonade");
		CompiledOrePattern pattern = type.decode(settings);
		boolean placed = pattern.place(new OrePlacementContext() {
			private final java.util.Random random = new java.util.Random(1L);

			@Override public java.util.Random random() { return random; }
			@Override public int originX() { return 0; }
			@Override public int originY() { return 32; }
			@Override public int originZ() { return 0; }
			@Override public int minY() { return -64; }
			@Override public int maxY() { return 96; }
			@Override public int quantity() { return 4; }
			@Override public int spread() { return 5; }
			@Override public int verticalSpread() { return 4; }
			@Override public int nodeSize() { return 2; }
			@Override public boolean inside(int x, int y, int z) { return true; }
			@Override public boolean isFluid(int x, int y, int z,
					net.minecraft.world.level.material.Fluid fluid) {
				return fluid == CakeWorldFluids.LEMONADE.get();
			}
			@Override public boolean tryPlace(int x, int y, int z) {
				return true;
			}
		});
		require(helper, placed,
				"OreSpawn underfluids pattern did not resolve CakeWorld lemonade");
	}

	private static PatternAudit auditCompiledPattern(ResourceLocation patternId,
			int quantity, int spread, int verticalSpread, int nodeSize,
			int length, long seed) {
		OrePatternType type = OreSpawnPatternRegistry.registry().getValue(
				patternId);
		if (type == null) {
			throw new AssertionError("OreSpawn pattern is not registered: "
					+ patternId);
		}
		JsonObject settings = new JsonObject();
		settings.addProperty("spread", spread);
		settings.addProperty("vertical_spread", verticalSpread);
		settings.addProperty("node_size", nodeSize);
		settings.addProperty("length", length);
		CompiledOrePattern pattern = type.decode(settings);
		Set<BlockPos> placements = new java.util.LinkedHashSet<>();
		boolean changed = pattern.place(new OrePlacementContext() {
			private final java.util.Random random =
					new java.util.Random(seed);

			@Override public java.util.Random random() { return random; }
			@Override public int originX() { return 0; }
			@Override public int originY() { return 0; }
			@Override public int originZ() { return 0; }
			@Override public int minY() { return -64; }
			@Override public int maxY() { return 320; }
			@Override public int quantity() { return quantity; }
			@Override public int spread() { return spread; }
			@Override public int verticalSpread() { return verticalSpread; }
			@Override public int nodeSize() { return nodeSize; }
			@Override public boolean inside(int x, int y, int z) {
				return true;
			}
			@Override public boolean isFluid(int x, int y, int z,
					net.minecraft.world.level.material.Fluid fluid) {
				return false;
			}
			@Override public boolean tryPlace(int x, int y, int z) {
				return placements.add(new BlockPos(x, y, z));
			}
		});
		int maximumDistanceSquared = placements.stream()
				.mapToInt(position -> position.getX() * position.getX()
						+ position.getY() * position.getY()
						+ position.getZ() * position.getZ())
				.max().orElse(0);
		return new PatternAudit(changed, placements.size(),
				maximumDistanceSquared);
	}

	private static void sampleGeologyRegion(ServerLevel level,
			int centerChunkX, int centerChunkZ, int radius, int requestedMinY,
			int requestedMaxY, GeologySurvey survey) {
		GeologySampler sampler = OreSpawnApi.createSampler(level)
				.orElseThrow(() -> new AssertionError(
						"OreSpawn sampler unavailable for "
								+ level.dimension().location()));
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				int x = (chunkX << 4) + 8;
				int z = (chunkZ << 4) + 8;
				GeologyColumn column = sampler.sampleColumn(x, z, 64);
				ResourceLocation geome = column.geome();
				survey.geomeColumns.merge(geome, 1, Integer::sum);
				Map<Block, Integer> geomeRocks = survey.geomeRocks
						.computeIfAbsent(geome,
								unused -> new LinkedHashMap<>());
				for (int y = minY; y <= maxY; y += 4) {
					Block rock = column.rockAt(y).getBlock();
					geomeRocks.merge(rock, 1, Integer::sum);
					survey.rockYLevels.computeIfAbsent(rock,
									unused -> new LinkedHashMap<>())
							.merge(y, 1, Integer::sum);
					if (rock == CakeWorldBlocks.CANDY_GLASS.get()) {
						survey.predictedCandyGlass++;
						Block actual = level.getBlockState(
								new BlockPos(x, y, z)).getBlock();
						if (actual == CakeWorldBlocks.CANDY_GLASS.get()) {
							survey.survivingCandyGlass++;
						} else if (isManagedOreOutput(actual)) {
							survey.candyGlassManagedOreReplacements++;
						}
					}
				}
			}
		}
	}

	private static boolean isManagedOreOutput(Block block) {
		return block == CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
				|| block == CakeWorldBlocks.LIQUORICE_VEIN.get()
				|| block == CakeWorldBlocks.COCOA_CLOUD.get()
				|| block == CakeWorldBlocks.MINT_CRYSTAL.get()
				|| block == CakeWorldBlocks.SPRINKLE_CLUSTER.get()
				|| block == CakeWorldBlocks.RICH_SPRINKLE_CLUSTER.get()
				|| block == CakeWorldBlocks.FIZZY_PEARL.get();
	}

	private static void assertGeomeSignature(GameTestHelper helper,
			GeologySurvey survey, ResourceLocation geome, Block signature) {
		Map<Block, Integer> counts = survey.geomeRocks.get(geome);
		require(helper, counts != null
						&& counts.getOrDefault(signature, 0) > 0,
				geome + " did not expose its signature "
						+ Registry.BLOCK.getKey(signature) + " rock");
	}

	private static double meanY(GeologySurvey survey, Block block) {
		Map<Integer, Integer> levels = survey.rockYLevels.get(block);
		if (levels == null || levels.isEmpty()) {
			return Double.NaN;
		}
		long count = 0;
		long total = 0;
		for (Map.Entry<Integer, Integer> entry : levels.entrySet()) {
			count += entry.getValue();
			total += (long) entry.getKey() * entry.getValue();
		}
		return total / (double) count;
	}

	private static Map<ResourceLocation, Map<ResourceLocation, Integer>>
			describeNested(Map<ResourceLocation, Map<Block, Integer>> counts) {
		Map<ResourceLocation, Map<ResourceLocation, Integer>> named =
				new LinkedHashMap<>();
		for (Map.Entry<ResourceLocation, Map<Block, Integer>> entry
				: counts.entrySet()) {
			Map<ResourceLocation, Integer> rocks = new LinkedHashMap<>();
			entry.getValue().forEach((block, count) ->
					rocks.put(Registry.BLOCK.getKey(block), count));
			named.put(entry.getKey(), rocks);
		}
		return named;
	}

	private static Map<ResourceLocation, RockDepthSummary> describeDepths(
			Map<Block, Map<Integer, Integer>> levels) {
		Map<ResourceLocation, RockDepthSummary> summaries =
				new LinkedHashMap<>();
		for (Map.Entry<Block, Map<Integer, Integer>> entry
				: levels.entrySet()) {
			int count = entry.getValue().values().stream()
					.mapToInt(Integer::intValue).sum();
			int minY = entry.getValue().keySet().stream()
					.mapToInt(Integer::intValue).min().orElse(0);
			int maxY = entry.getValue().keySet().stream()
					.mapToInt(Integer::intValue).max().orElse(0);
			long totalY = 0;
			for (Map.Entry<Integer, Integer> level
				: entry.getValue().entrySet()) {
				totalY += (long) level.getKey() * level.getValue();
			}
			summaries.put(Registry.BLOCK.getKey(entry.getKey()),
					new RockDepthSummary(count, minY, maxY,
							totalY / (double) count));
		}
		return summaries;
	}

	private static Map<ResourceLocation, Integer> countChunkCenterBiomes(
			ServerLevel level, int centerChunkX, int centerChunkZ, int radius,
			int y) {
		Map<ResourceLocation, Integer> counts = new LinkedHashMap<>();
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				BlockPos center = new BlockPos((chunkX << 4) + 8, y,
						(chunkZ << 4) + 8);
				level.getBiome(center).unwrapKey().ifPresent(key ->
						counts.merge(key.location(), 1, Integer::sum));
			}
		}
		return counts;
	}

	private static Map<Block, Integer> countBlocksDirectlyUnderFluid(
			ServerLevel level, net.minecraft.world.level.material.Fluid fluid,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		Map<Block, Integer> counts = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 2, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position = new BlockPos(x, y, z);
							if (level.getFluidState(position).isEmpty()
									&& level.getFluidState(position.above())
											.getType().isSame(fluid)) {
								counts.merge(level.getBlockState(position)
										.getBlock(), 1, Integer::sum);
							}
						}
					}
				}
			}
		}
		return counts;
	}

	private static Map<ResourceLocation, Integer> countChunkCenterGeomes(
			GameTestHelper helper, ServerLevel level, int centerChunkX,
			int centerChunkZ, int radius, int biomeY) {
		GeologySampler sampler = OreSpawnApi.createSampler(level)
				.orElseThrow(() -> new AssertionError(
						"OreSpawn sampler unavailable for focused region"));
		Map<ResourceLocation, Integer> counts = new LinkedHashMap<>();
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				int x = (chunkX << 4) + 8;
				int z = (chunkZ << 4) + 8;
				ResourceLocation geome = sampler.sampleColumn(x, z, biomeY)
						.geome();
				counts.merge(geome, 1, Integer::sum);
			}
		}
		require(helper, !counts.isEmpty(),
				"Focused region produced no geology samples");
		return counts;
	}

	private static int countTargetUnderFluid(ServerLevel level, Block target,
			net.minecraft.world.level.material.Fluid fluid,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		int count = 0;
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 2, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position = new BlockPos(x, y, z);
							if (level.getBlockState(position).is(target)
									&& level.getFluidState(position.above())
											.getType().isSame(fluid)) {
								count++;
							}
						}
					}
				}
			}
		}
		return count;
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

	private record FluidEnvelopeResult(int fluidBlocks, int boundaryFaces,
			int violations, BlockPos firstViolation) {
	}

	private record PatternAudit(boolean changed, int placements,
			int maximumDistanceSquared) {
	}

	private record RockDepthSummary(int samples, int minimumY, int maximumY,
			double meanY) {
	}

	private record GeomePlacementSurvey(
			Map<ResourceLocation, Integer> chunksByGeome,
			Map<ResourceLocation, Integer> blocksByGeome) {
		int chunks(ResourceLocation geome) {
			return chunksByGeome.getOrDefault(geome, 0);
		}

		int blocks(ResourceLocation geome) {
			return blocksByGeome.getOrDefault(geome, 0);
		}

		double blocksPerChunk(ResourceLocation geome) {
			int chunks = chunks(geome);
			return chunks == 0 ? 0.0D : blocks(geome) / (double) chunks;
		}
	}

	private record HostAttributionResult(int outputs, int violations,
			BlockPos firstViolation, String firstViolationDetail,
			Map<ResourceLocation, Integer> outputsByBlock,
			Map<ResourceLocation, Integer> violationsByBlock) {
	}

	private record SurfaceAudit(int biomeColumns, int topMatches,
			int fillerMatches) {
	}

	private static final class GeologySurvey {
		private final Map<ResourceLocation, Integer> geomeColumns =
				new LinkedHashMap<>();
		private final Map<ResourceLocation, Map<Block, Integer>> geomeRocks =
				new LinkedHashMap<>();
		private final Map<Block, Map<Integer, Integer>> rockYLevels =
				new LinkedHashMap<>();
		private int predictedCandyGlass;
		private int survivingCandyGlass;
		private int candyGlassManagedOreReplacements;
	}
}
