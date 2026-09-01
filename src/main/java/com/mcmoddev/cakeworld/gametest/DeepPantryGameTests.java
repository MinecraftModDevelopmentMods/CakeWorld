package com.mcmoddev.cakeworld.gametest;

import java.lang.reflect.Field;
import java.util.Comparator;
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
import com.mcmoddev.cakeworld.block.WaferWindmillBlock;
import com.mcmoddev.cakeworld.compat.VanillaResourceAdvancements;
import com.mcmoddev.cakeworld.entity.BiscuitBandit;
import com.mcmoddev.cakeworld.entity.BitterBaker;
import com.mcmoddev.cakeworld.entity.CrumbledGingerbreadFolk;
import com.mcmoddev.cakeworld.entity.CinnamonSpark;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.entity.FudgeBoar;
import com.mcmoddev.cakeworld.entity.FudgeBrute;
import com.mcmoddev.cakeworld.entity.FudgeFolk;
import com.mcmoddev.cakeworld.entity.GrandGumballGuardian;
import com.mcmoddev.cakeworld.entity.GingerbreadFolk;
import com.mcmoddev.cakeworld.entity.GumballGuardian;
import com.mcmoddev.cakeworld.entity.JawbreakerGuardian;
import com.mcmoddev.cakeworld.entity.MacaronClam;
import com.mcmoddev.cakeworld.entity.SoggyBiscuit;
import com.mcmoddev.cakeworld.entity.TravellingConfectioner;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.world.AncientCakeVaultFeature;
import com.mcmoddev.cakeworld.world.BiscuitBanditLookoutFeature;
import com.mcmoddev.cakeworld.world.BurntSugarArchFeature;
import com.mcmoddev.cakeworld.world.BurntToffeeFoundryFeature;
import com.mcmoddev.cakeworld.world.BuriedSweetTinFeature;
import com.mcmoddev.cakeworld.world.BuriedSweetTinRepair;
import com.mcmoddev.cakeworld.world.CandyCaneBridgeFeature;
import com.mcmoddev.cakeworld.world.CandyCaneBridgeStructureFeature;
import com.mcmoddev.cakeworld.world.CandyCaneHoodooGardenFeature;
import com.mcmoddev.cakeworld.world.CaramelBogMangroveFeature;
import com.mcmoddev.cakeworld.world.CaramelCottageFeature;
import com.mcmoddev.cakeworld.world.ConfectionersCottageFeature;
import com.mcmoddev.cakeworld.world.CookieCrumbGroveFeature;
import com.mcmoddev.cakeworld.world.CraterKitchenFeature;
import com.mcmoddev.cakeworld.world.CraterKitchenStructureFeature;
import com.mcmoddev.cakeworld.world.PeppermintClearingFeature;
import com.mcmoddev.cakeworld.world.RockCandyCrystalMineFeature;
import com.mcmoddev.cakeworld.world.RockCandyCrystalMineStructureFeature;
import com.mcmoddev.cakeworld.world.RoadsideCuriosityFeature;
import com.mcmoddev.cakeworld.world.SherbetFossilBowlFeature;
import com.mcmoddev.cakeworld.world.GingerbreadVillageFeature;
import com.mcmoddev.cakeworld.world.GrandGingerbreadManorFeature;
import com.mcmoddev.cakeworld.world.GummyJungleBounceGroveFeature;
import com.mcmoddev.cakeworld.world.GummyShrineFeature;
import com.mcmoddev.cakeworld.world.IceCreamParlourFeature;
import com.mcmoddev.cakeworld.world.SherbetPyramidFeature;
import com.mcmoddev.cakeworld.world.SodaPalaceFeature;
import com.mcmoddev.cakeworld.world.SodaPalacePalette;
import com.mcmoddev.cakeworld.world.SunkenSweetshopFeature;
import com.mcmoddev.cakeworld.world.LiquoriceFortressFeature;
import com.mcmoddev.cakeworld.world.MacaronCitadelFeature;
import com.mcmoddev.cakeworld.world.MacaronCitadelPalette;
import com.mcmoddev.cakeworld.world.RockCandyFossilFeature;
import com.mcmoddev.cakeworld.world.WaferMineFeature;
import com.mcmoddev.cakeworld.world.WaferWindmillFeature;
import com.mcmoddev.cakeworld.world.WaferWreckFeature;
import zone.moddev.mc.orespawn.api.CompiledOrePattern;
import zone.moddev.mc.orespawn.api.GeologyColumn;
import zone.moddev.mc.orespawn.api.GeologyFamily;
import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.GeologySampler;
import zone.moddev.mc.orespawn.api.OrePatternType;
import zone.moddev.mc.orespawn.api.OrePlacementContext;
import zone.moddev.mc.orespawn.api.OreSpawnApi;
import zone.moddev.mc.orespawn.api.OreSpawnPatternRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.advancements.Advancement;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.BuriedTreasurePieces;
import net.minecraft.world.level.levelgen.structure.EndCityPieces;
import net.minecraft.world.level.levelgen.structure.OceanMonumentPieces;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
import net.minecraft.world.level.levelgen.structure.OceanRuinPieces;
import net.minecraft.world.level.levelgen.structure.NetherBridgePieces;
import net.minecraft.world.level.levelgen.structure.NetherFossilPieces;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StrongholdPieces;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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
	private static final long CLUSTER_PATTERN_SIGNATURE =
			-4459183734629593765L;
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
			id("ore/fizzy_pearl"),
			id("ore/cocoa_coal"),
			id("ore/iron_wafer"),
			id("ore/copper_caramel"),
			id("ore/honeycomb_gold"),
			id("ore/raspberry_redstone"),
			id("ore/blueberry_lapis"),
			id("ore/rock_candy_diamond"),
			id("ore/mint_emerald"),
			id("ore/vanilla_quartz"),
			id("ore/fudge_gold"),
			id("ore/ancient_nougat"));

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
	public static void compiledDefaultPrecisionAndClusterPatternsRespectBudgets(
			GameTestHelper helper) {
		PatternAudit compact = auditCompiledPattern(
				new ResourceLocation("orespawn", "default"),
				6, 3, 2, 4, 8, 1978L);
		PatternAudit precision = auditCompiledPattern(
				new ResourceLocation("orespawn", "precision"),
				5, 4, 3, 2, 8, 1978L);
		PatternAudit clusters = auditCompiledPattern(
				new ResourceLocation("orespawn", "clusters"),
				32, 8, 4, 4, 16, 1978L);
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
		require(helper, clusters.changed() && clusters.placements() == 32,
				"Cluster pattern did not consume its 32-block budget: "
						+ clusters);
		require(helper, clusters.maximumDistanceSquared() <= 187,
				"Cluster pattern escaped its configured spread: " + clusters);
		require(helper, clusters.signature() == CLUSTER_PATTERN_SIGNATURE,
				"Cluster pattern signature drifted: " + clusters);
		LOGGER.info("Compiled CakeWorld pattern audit: default={}, precision={}, clusters={}",
				compact, precision, clusters);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "os085world",
			timeoutTicks = 2400)
	public static void focusedFizzyPearlAttributionAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Fizzy Pearl attribution audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		BlockPos sodaOcean = locateBiome(helper, level,
				id("soda_ocean"));
		int sodaChunkX = Math.floorDiv(
				sodaOcean.getX(), 16);
		int sodaChunkZ = Math.floorDiv(
				sodaOcean.getZ(), 16);
		Map<Block, Integer> sodaRegion = scanDimension(level,
				Set.of(
						CakeWorldBlocks.FIZZY_PEARL.get(),
						CakeWorldFluids.LEMONADE_BLOCK.get()),
				sodaChunkX, sodaChunkZ, 4, -64, 96);
		PearlAttribution pearlAttribution =
				countFizzyPearlAttribution(level,
						sodaChunkX, sodaChunkZ, 4,
						-64, 96);
		int fizzyPearls = sodaRegion.getOrDefault(
				CakeWorldBlocks.FIZZY_PEARL.get(), 0);
		LOGGER.info("Focused Fizzy Pearl attribution audit: soda_ocean={}, fizzy_region={}, pearl_attribution={}",
				sodaOcean, describe(sodaRegion),
				pearlAttribution);
		require(helper,
				pearlAttribution.total() == fizzyPearls,
				"Fizzy Pearl attribution scan disagreed with the focused Soda Ocean block scan");
		require(helper,
				pearlAttribution.unattributed() > 0,
				"OS-085: Soda Ocean contained Lemonade and compatible floor hosts but OreSpawn generated no Fizzy Pearls outside "
						+ pearlAttribution.waferReefTreasures()
						+ " authored Wafer Reef Nursery treasures");
		require(helper,
				pearlAttribution.unattributedUnderLemonade() > 0,
				"OS-085: Fizzy Pearls outside authored Wafer Reef Nurseries appeared without an observed under-Lemonade origin: "
						+ pearlAttribution);
		LOGGER.info("OS-085 integrated-world proof found {} under-Lemonade Fizzy Pearls outside authored Wafer Reef Nurseries",
				pearlAttribution.unattributedUnderLemonade());
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
		BlockPos peppermintPinewoods = locateBiome(helper, level,
				id("peppermint_pinewoods"));
		BlockPos sodaOcean = locateBiome(helper, level, id("soda_ocean"));

		int peppermintChunkX = Math.floorDiv(
				peppermintPinewoods.getX(), 16);
		int peppermintChunkZ = Math.floorDiv(
				peppermintPinewoods.getZ(), 16);
		Map<Block, Integer> mintRegion = scanDimension(level,
				Set.of(
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						CakeWorldBlocks.MINT_CRYSTAL.get(),
						CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()),
				peppermintChunkX, peppermintChunkZ, 6, -64, 96);
		require(helper, mintRegion.getOrDefault(
						CakeWorldBlocks.PEPPERMINT_ROCK.get(), 0) > 0,
				"Peppermint Pinewoods region contained no Peppermint Rock host");
		require(helper, mintRegion.getOrDefault(
						CakeWorldBlocks.MINT_CRYSTAL.get(), 0) > 0,
				"Focused Peppermint Pinewoods region generated no Mint Crystal");
		Map<Integer, Integer> mintCrystalYs = countTargetYLevels(level,
				Set.of(CakeWorldBlocks.MINT_CRYSTAL.get()),
				peppermintChunkX, peppermintChunkZ, 6, -64, 96);
		Map<Integer, Integer> deepMintOutputYs =
				countTargetYLevelsAdjacentTo(level,
						CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get(),
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						peppermintChunkX, peppermintChunkZ, 6, -64, -24);
		Map<Integer, Integer> ordinaryRockCandyDepositYs =
				countTargetYLevelsNotAdjacentTo(level,
						CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get(),
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						peppermintChunkX, peppermintChunkZ, 6, -48, 80);
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
		PearlAttribution pearlAttribution =
				countFizzyPearlAttribution(level,
				sodaChunkX, sodaChunkZ, 4, -64, 96);
		int fizzyPearls = sodaRegion.getOrDefault(
				CakeWorldBlocks.FIZZY_PEARL.get(), 0);
		require(helper,
				pearlAttribution.total() == fizzyPearls,
				"Fizzy Pearl attribution scan disagreed with the focused Soda Ocean block scan");
		LOGGER.info("Broad Soda Ocean attribution observation: authored_reef_treasures={}, under_lemonade_outside_authored_reefs={}",
				pearlAttribution.waferReefTreasures(),
				pearlAttribution.unattributedUnderLemonade());

		LOGGER.info("Focused Deep Pantry audit: peppermint_pinewoods={} mint_region={}, soda_ocean={} fizzy_region={}, pearl_attribution={}",
				peppermintPinewoods, describe(mintRegion), sodaOcean,
				describe(sodaRegion), pearlAttribution);
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
				"cakeworld:candy_plains", "cakeworld:icing",
				"cakeworld:chocolate_sponge", "cakeworld:biscuit_crumbs", 4);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:gingerbread_hearthlands",
				"cakeworld:biscuit_crumbs",
				"cakeworld:chocolate_sponge",
				"cakeworld:biscuit_crumbs", 4);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:cookie_forest", "cakeworld:chocolate_sponge",
				"cakeworld:chocolate_sponge", "cakeworld:biscuit_crumbs", 5);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:peppermint_pinewoods",
				"cakeworld:icing",
				"cakeworld:peppermint_rock",
				"cakeworld:biscuit_crumbs", 5);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:gummy_jungle",
				"cakeworld:gummy_block",
				"cakeworld:chocolate_sponge",
				"cakeworld:blueberry_gummy_block", 4);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:caramel_bogs",
				"cakeworld:caramel_crust",
				"cakeworld:chocolate_sponge",
				"cakeworld:caramel_crust", 4);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:sherbet_dunes",
				"cakeworld:raspberry_sherbet_powder",
				"cakeworld:lemon_sherbet_powder",
				"cakeworld:orange_sherbet_powder", 5);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:candy_cane_badlands",
				"cakeworld:wafer_rock",
				"cakeworld:candy_cane_pillar",
				"cakeworld:peppermint_rock", 7);
		assertPaletteContract(helper, palettes, "cakeworld:overworld_land",
				"cakeworld:marshmallow_peaks", "cakeworld:icing",
				"cakeworld:marshmallow", "cakeworld:biscuit_crumbs", 5);
		assertPaletteContract(helper, palettes, "cakeworld:nether",
				"cakeworld:fudge_wastes", "cakeworld:fudge_rock",
				"cakeworld:fudge_rock", null, 5);
		assertPaletteContract(helper, palettes, "cakeworld:end",
				"cakeworld:meringue_islands", "cakeworld:meringue_foam",
				"cakeworld:marshmallow", "cakeworld:marshmallow", 5);

		Map<String, SurfaceAudit> surfaces = new LinkedHashMap<>();
		BlockPos hearthlands = locateBiome(helper, overworld,
				id("gingerbread_hearthlands"));
		BlockPos peppermintPinewoods = locateBiome(helper,
				overworld, id("peppermint_pinewoods"));
		BlockPos gummyJungle = locateBiome(helper,
				overworld, id("gummy_jungle"));
		BlockPos caramelBogs = locateBiome(helper,
				overworld, id("caramel_bogs"));
		BlockPos sherbetDunes = locateBiome(helper,
				overworld, id("sherbet_dunes"));
		BlockPos candyCaneBadlands = locateBiome(helper,
				overworld, id("candy_cane_badlands"));
		surfaces.put("candy_plains", auditSurface(overworld,
				locateBiome(helper, overworld, id("candy_plains")),
				id("candy_plains"), CakeWorldBlocks.ICING.get(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(), 2));
		surfaces.put("gingerbread_hearthlands",
				auditSurface(overworld, hearthlands,
						id("gingerbread_hearthlands"),
						CakeWorldBlocks.BISCUIT_CRUMBS.get(),
						CakeWorldBlocks.CHOCOLATE_SPONGE.get(),
						2));
		surfaces.put("cookie_forest", auditSurface(overworld,
				locateBiome(helper, overworld, id("cookie_forest")),
				id("cookie_forest"), CakeWorldBlocks.CHOCOLATE_SPONGE.get(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(), 2));
		surfaces.put("peppermint_pinewoods",
				auditSurface(overworld,
						peppermintPinewoods,
						id("peppermint_pinewoods"),
						CakeWorldBlocks.ICING.get(),
						CakeWorldBlocks.PEPPERMINT_ROCK.get(),
						2));
		surfaces.put("gummy_jungle",
				auditSurface(overworld,
						gummyJungle,
						id("gummy_jungle"),
						CakeWorldBlocks.GUMMY_BLOCK.get(),
						CakeWorldBlocks.CHOCOLATE_SPONGE
								.get(),
						2));
		surfaces.put("caramel_bogs",
				auditSurface(overworld,
						caramelBogs,
						id("caramel_bogs"),
						CakeWorldBlocks.CARAMEL_CRUST.get(),
						CakeWorldBlocks.CHOCOLATE_SPONGE
								.get(),
						2));
		surfaces.put("sherbet_dunes",
				auditSurface(overworld,
						sherbetDunes,
						id("sherbet_dunes"),
						CakeWorldBlocks
								.RASPBERRY_SHERBET_POWDER
								.get(),
						CakeWorldBlocks
								.LEMON_SHERBET_POWDER
								.get(),
						2));
		surfaces.put("candy_cane_badlands",
				auditSurface(overworld,
						candyCaneBadlands,
						id("candy_cane_badlands"),
						CakeWorldBlocks.WAFER_ROCK.get(),
						CakeWorldBlocks.CANDY_CANE_PILLAR
								.get(),
						2));
		surfaces.put("marshmallow_peaks", auditSurface(overworld,
				locateBiome(helper, overworld, id("marshmallow_peaks")),
				id("marshmallow_peaks"), CakeWorldBlocks.ICING.get(),
				CakeWorldBlocks.MARSHMALLOW.get(), 2));
		surfaces.put("fudge_wastes", auditSurface(nether,
				new BlockPos(0, 64, 0), id("fudge_wastes"),
				CakeWorldBlocks.FUDGE_ROCK.get(),
				CakeWorldBlocks.FUDGE_ROCK.get(), 2));
		surfaces.put("meringue_islands", auditSurface(end,
				new BlockPos(0, 64, 0), id("meringue_islands"),
				CakeWorldBlocks.MERINGUE_FOAM.get(),
				CakeWorldBlocks.MARSHMALLOW.get(), 2));
		for (Map.Entry<String, SurfaceAudit> entry : surfaces.entrySet()) {
			require(helper, entry.getValue().biomeColumns() > 0
							&& entry.getValue().topMatches() > 0
							&& entry.getValue().fillerMatches() > 0,
					"Fixed world did not expose the declared top/filler pair for "
							+ entry.getKey() + ": " + entry.getValue());
		}

		Map<ResourceLocation, Integer> hearthlandsGeomes =
				countGeomesForBiome(overworld, hearthlands,
						id("gingerbread_hearthlands"), 4);
		Map<ResourceLocation, Integer> peppermintGeomes =
				countGeomesForBiome(overworld,
						peppermintPinewoods,
						id("peppermint_pinewoods"), 4);
		Map<ResourceLocation, Integer> gummyGeomes =
				countGeomesForBiome(overworld,
						gummyJungle,
						id("gummy_jungle"), 4);
		Map<ResourceLocation, Integer> sherbetGeomes =
				countGeomesForBiome(overworld,
						sherbetDunes,
						id("sherbet_dunes"), 4);
		Map<ResourceLocation, Integer> candyCaneBadlandsGeomes =
				countGeomesForBiome(overworld,
						candyCaneBadlands,
						id("candy_cane_badlands"), 6);
		require(helper,
				!hearthlandsGeomes.isEmpty()
						&& hearthlandsGeomes.keySet().stream()
								.allMatch(Set.of(
										id("cocoa_basin"),
										id("wafer_shelf"))
										::contains)
						&& hearthlandsGeomes.getOrDefault(
								id("wafer_shelf"), 0) > 0,
				"Natural Gingerbread Hearthlands did not stay within its Cocoa Basin/Wafer Shelf bias or expose its higher-weight Wafer Shelf: "
						+ hearthlandsGeomes);
		require(helper,
				!peppermintGeomes.isEmpty()
						&& peppermintGeomes.keySet().stream()
								.allMatch(Set.of(
										id("peppermint_fold"),
										id("rock_candy_uplift"))
										::contains)
						&& peppermintGeomes.getOrDefault(
								id("peppermint_fold"), 0) > 0,
				"Natural Peppermint Pinewoods did not stay within its Peppermint Fold/Rock-Candy Uplift bias or expose its higher-weight Peppermint Fold: "
						+ peppermintGeomes);
		require(helper,
				!gummyGeomes.isEmpty()
						&& gummyGeomes.keySet().stream()
								.allMatch(Set.of(
										id("cocoa_basin"),
										id("wafer_shelf"),
										id("rock_candy_uplift"),
										id("fudge_mantle"))
										::contains),
				"Natural Gummy Jungle escaped its three explicit flavour geomes plus inherited HOT/Fudge-Mantle dictionary seam: "
						+ gummyGeomes);
		require(helper,
				!sherbetGeomes.isEmpty()
						&& sherbetGeomes.keySet().stream()
								.allMatch(Set.of(
										id("wafer_shelf"),
										id("rock_candy_uplift"),
										id("fudge_mantle"))
										::contains)
						&& sherbetGeomes.getOrDefault(
								id("rock_candy_uplift"), 0) > 0,
				"Natural Sherbet Dunes escaped its Wafer Shelf/Rock-Candy Uplift profile plus inherited HOT/Fudge-Mantle seam or lost its higher-weight uplift: "
						+ sherbetGeomes);
		require(helper,
				!candyCaneBadlandsGeomes.isEmpty()
						&& candyCaneBadlandsGeomes.keySet().stream()
								.allMatch(Set.of(
										id("wafer_shelf"),
										id("peppermint_fold"),
										id("rock_candy_uplift"),
										id("fudge_mantle"))
										::contains)
						&& candyCaneBadlandsGeomes.getOrDefault(
								id("rock_candy_uplift"), 0) > 0,
				"Natural Candy-Cane Badlands escaped its three explicit flavour geomes plus inherited HOT/Fudge-Mantle seam or lost its higher-weight Rock-Candy Uplift: "
						+ candyCaneBadlandsGeomes);

		BlockPos sodaOcean = locateBiome(helper, overworld, id("soda_ocean"));
		Map<Block, Integer> lemonadeFloor = countBlocksDirectlyUnderFluid(
				overworld, CakeWorldFluids.LEMONADE.get(),
				Math.floorDiv(sodaOcean.getX(), 16),
				Math.floorDiv(sodaOcean.getZ(), 16), 4, -64, 96);
		require(helper, lemonadeFloor.getOrDefault(
						CakeWorldBlocks.BISCUIT_CRUMBS.get(), 0) > 0,
				"Soda Ocean exposed no Biscuit Crumbs underwater surface");
		LOGGER.info("Focused biome surface/palette audit: surfaces={}, hearthlands_geomes={}, peppermint_geomes={}, gummy_geomes={}, sherbet_geomes={}, candy_cane_badlands_geomes={}, soda_floor={}",
				surfaces, hearthlandsGeomes,
				peppermintGeomes, gummyGeomes,
				sherbetGeomes, candyCaneBadlandsGeomes,
				describe(lemonadeFloor));
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
				CakeWorldBiomes.GINGERBREAD_HEARTHLANDS.getId(),
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
				countSampledGeomesForBiome(overworld,
						marshmallowCenter,
						CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
						1024, 16, 64);
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

		BlockPos peppermintPinewoods = locateBiome(helper, level,
				id("peppermint_pinewoods"));
		BlockPos marshmallowPeaks = locateBiome(helper, level,
				id("marshmallow_peaks"));
		BlockPos candyPlains = locateBiome(helper, level,
				id("candy_plains"));

		GeomePlacementSurvey peppermintMintSurvey = surveyTargetBlocksByGeome(
				level, Set.of(CakeWorldBlocks.MINT_CRYSTAL.get()),
				Math.floorDiv(peppermintPinewoods.getX(), 16),
				Math.floorDiv(peppermintPinewoods.getZ(), 16),
				4, -56, 80,
				false);
		GeomePlacementSurvey upliftMintSurvey = surveyTargetBlocksByGeome(
				level, Set.of(CakeWorldBlocks.MINT_CRYSTAL.get()),
				Math.floorDiv(marshmallowPeaks.getX(), 16),
				Math.floorDiv(marshmallowPeaks.getZ(), 16),
				4, -56, 80,
				false);
		GeomePlacementSurvey cocoaMintSurvey = surveyTargetBlocksByGeome(
				level, Set.of(CakeWorldBlocks.MINT_CRYSTAL.get()),
				Math.floorDiv(candyPlains.getX(), 16),
				Math.floorDiv(candyPlains.getZ(), 16),
				4, -56, 80,
				false);
		GeomePlacementSurvey mintSurvey = mergeGeomePlacementSurveys(
				peppermintMintSurvey, upliftMintSurvey,
				cocoaMintSurvey);
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
						&& mintSurvey.blocks(rockCandyUplift) > 0
						&& mintSurvey.blocksPerChunk(cocoaBasin)
								< mintSurvey.blocksPerChunk(
										peppermintFold)
						&& mintSurvey.blocksPerChunk(cocoaBasin)
								< mintSurvey.blocksPerChunk(
										rockCandyUplift),
				"Mint Crystal positive/zero geome controls were not observed; zero-weight controls may contain only boundary spill from formations started in adjacent positive geomes");
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

	public static void focusedFamilyOreHostAttributionAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed family ore-host attribution audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		BlockPos candyPlains = locateBiome(helper, level,
				id("candy_plains"));
		FamilyHostAttributionResult familyResult =
				auditPredictedFamilyOreHosts(level,
						Map.of(
								CakeWorldBlocks.COCOA_CLOUD.get(),
								Set.of(GeologyFamily.SEDIMENTARY),
								CakeWorldBlocks.LIQUORICE_VEIN.get(),
								Set.of(GeologyFamily.SEDIMENTARY,
										GeologyFamily.METAMORPHIC)),
						Math.floorDiv(candyPlains.getX(), 16),
						Math.floorDiv(candyPlains.getZ(), 16),
						4, -48, 32);
		LOGGER.info("Focused family ore-host attribution audit: {}",
				familyResult);
		if (!familyResult.violationDetails().isEmpty()) {
			LOGGER.info("Focused family ore-host violation details: {}",
					familyResult.violationDetails());
		}
		require(helper, familyResult.outputs() > 0
						&& familyResult.violations() == 0
						&& familyResult.nonReplaceableControls() > 0,
				"Family-host output disagreed with sampled pre-ore geology at "
						+ familyResult.firstViolationDetail()
						+ "; nonReplaceableControls="
						+ familyResult.nonReplaceableControls());
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
		for (int chunkX = auditChunkX - 5;
				chunkX <= auditChunkX + 5; chunkX++) {
			for (int chunkZ = auditChunkZ - 5;
					chunkZ <= auditChunkZ + 5; chunkZ++) {
				nether.getChunk(chunkX, chunkZ);
			}
		}

		helper.runAfterDelay(80, () -> {
			Map<Block, Integer> overworldResiduals = scanDimension(overworld,
					Set.of(Blocks.STONE, Blocks.DEEPSLATE),
					auditChunkX, auditChunkZ, 4, -64, 96);
			Map<Block, Integer> netherResiduals = scanDimension(nether,
					Set.of(Blocks.NETHERRACK, Blocks.BASALT,
							Blocks.BLACKSTONE),
					auditChunkX, auditChunkZ, 4, 0, 127);
			Map<Block, Integer> endResiduals = scanDimension(end,
					Set.of(Blocks.END_STONE),
					auditChunkX, auditChunkZ, 4, 0, 255);
			Map<Integer, Integer> overworldResidualYs = countTargetYLevels(
					overworld,
					Set.of(Blocks.STONE, Blocks.DEEPSLATE),
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
		});
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
		net.minecraft.world.level.chunk.LevelChunk chunk =
				level.getChunk(startChunk.x, startChunk.z);
		net.minecraft.world.level.levelgen.structure.StructureStart
				start = chunk.getStartForFeature(configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1,
				"The located Gingerbread Village lost its saved Village structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		int minimumChunkX = Math.floorDiv(
				savedBounds.minX(), 16);
		int maximumChunkX = Math.floorDiv(
				savedBounds.maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				savedBounds.minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				savedBounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ; chunkZ++) {
				level.setChunkForced(chunkX, chunkZ, true);
			}
		}

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
			long homes = level.getPoiManager().getCountInRange(
					net.minecraft.world.entity.ai.village.poi
							.PoiType.HOME::equals,
					centre, 32,
					net.minecraft.world.entity.ai.village.poi
							.PoiManager.Occupancy.ANY);
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
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX; chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ; chunkZ++) {
					level.setChunkForced(
							chunkX, chunkZ, false);
				}
			}
			LOGGER.info("Focused Gingerbread Village audit: locate={}, centre={}, biome={}, palette={}, homes={}, meeting={}, village={}, residents={}, guardians={}, startPieces={}",
					located, centre, biomeId, palette, homes,
					meetingPoi, village, residents.size(),
					guardians.size(),
					start.getPieces().size());
			require(helper,
					Set.of(
							CakeWorldBiomes
									.CANDY_PLAINS
									.getId(),
							CakeWorldBiomes
									.GINGERBREAD_HEARTHLANDS
									.getId(),
							CakeWorldBiomes
									.WAFFLE_PLATEAUS
									.getId())
							.contains(biomeId)
							&& level.getBiome(centre).is(
									GingerbreadVillageFeature
											.GENERATES_IN)
							&& !literalPlainsVillageEligible,
					"Gingerbread Village generated outside its CakeWorld settlement-biome contract or enabled a literal Plains Village");
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0) > 250
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) >= 20
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
									.get(), 0) >= 128
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
							&& Set.of(
									CakeWorldBiomes
											.COOKIE_FOREST
											.getId(),
									CakeWorldBiomes
											.LIQUORICE_DARKWOOD
											.getId())
									.contains(biomeId)
							&& !literalMansionEligible,
					"Grand Gingerbread Manor generated outside Cookie Forest/Liquorice Darkwood or enabled a literal vanilla Mansion: "
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

	@GameTest(template = EMPTY, timeoutTicks = 7200)
	public static void focusedSherbetPyramidStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Sherbet Pyramid audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
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
						SherbetPyramidFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Sherbet Pyramid configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						SherbetPyramidFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Sherbet Pyramid lost its public locate tag");

		BlockPos located = level.findNearestMapFeature(
				SherbetPyramidFeature.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Sherbet Pyramid within 512 chunks");
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
				"The located Sherbet Pyramid lost its saved surface-structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		require(helper,
				savedBounds.getXSpan() == 21
						&& savedBounds.getYSpan() == 25
						&& savedBounds.getZSpan() == 21,
				"The saved Sherbet Pyramid collapsed its exact 21x25x21 above-and-below-ground bounds: "
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
					savedBounds.minX() + 10,
					savedBounds.minY() + 14,
					savedBounds.minZ() + 10);
			Map<Block, Integer> palette =
					new LinkedHashMap<>();
			for (int x = -10; x <= 10; x++) {
				for (int y = -14; y <= 10; y++) {
					for (int z = -10; z <= 10;
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
			boolean protectedLoot = true;
			for (BlockPos position : List.of(
					centre.offset(0, -11, -5),
					centre.offset(0, -11, 5),
					centre.offset(-5, -11, 0),
					centre.offset(5, -11, 0))) {
				BlockEntity blockEntity =
						level.getBlockEntity(
								position);
				CompoundTag state =
						blockEntity == null
								? new CompoundTag()
								: blockEntity
										.saveWithoutMetadata();
				protectedLoot &=
						SherbetPyramidFeature
								.LOOT_ID.toString()
								.equals(state
										.getString(
												"LootTable"));
			}
			ResourceLocation biomeId =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(
									centre)
									.value());
			boolean literalPyramidEligible =
					level.getBiome(centre).is(
							BiomeTags
									.HAS_DESERT_PYRAMID);

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

			LOGGER.info("Focused Sherbet Pyramid audit: locate={}, centre={}, bounds={}, biome={}, palette={}, protectedLoot={}",
					located, centre, savedBounds,
					biomeId, palette,
					protectedLoot);
			require(helper,
					CakeWorldBiomes.SHERBET_DUNES
							.getId().equals(biomeId)
							&& !literalPyramidEligible,
					"The natural Sherbet Pyramid lost its Sherbet Dunes home or leaked literal Desert Pyramid eligibility: biome="
							+ biomeId
							+ ", literalPyramidEligible="
							+ literalPyramidEligible);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.BISCUIT_STONE
									.get(), 0) >= 1300
							&& gummyBlocks >= 40
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									>= 54
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW
											.get(), 0)
									== 16
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									== 9
							&& palette.getOrDefault(
									CakeWorldBlocks
											.FIZZY_PEARL
											.get(), 0)
									== 5
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SPRINKLE_CLUSTER
											.get(), 0)
									== 6
							&& palette.getOrDefault(
									Blocks.BONE_BLOCK,
									0) == 8
							&& palette.getOrDefault(
									Blocks.LADDER, 0)
									>= 25
							&& palette.getOrDefault(
									Blocks.TNT, 0)
									== 1
							&& palette.getOrDefault(
									Blocks
											.STONE_PRESSURE_PLATE,
									0) == 1
							&& palette.getOrDefault(
									Blocks.BARREL, 0)
									== 4,
					"The natural Sherbet Pyramid lost its pressed-sherbet shell, warning palette, fossils, recovery routes, reduced trap or buried jars: "
							+ palette);
			require(helper, protectedLoot,
					"The natural Sherbet Pyramid lost one or more protected buried loot jars");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 7200)
	public static void focusedIceCreamParlourStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Ice-Cream Parlour audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
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
						IceCreamParlourFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Ice-Cream Parlour configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						IceCreamParlourFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Ice-Cream Parlour lost its public locate tag");

		BlockPos located = level.findNearestMapFeature(
				IceCreamParlourFeature.STRUCTURE_TAG,
				helper.absolutePos(new BlockPos(4, 4, 4)),
				512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Ice-Cream Parlour within 512 chunks");
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
				"The located Ice-Cream Parlour lost its saved surface-structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				savedBounds = start.getBoundingBox();
		require(helper,
				savedBounds.getXSpan() == 13
						&& savedBounds.getYSpan() == 27
						&& savedBounds.getZSpan() == 13,
				"The saved Ice-Cream Parlour collapsed its exact 13x27x13 optional-cellar envelope: "
						+ savedBounds);
		BlockPos centre = new BlockPos(
				savedBounds.minX() + 6,
				savedBounds.minY() + 19,
				savedBounds.minZ() + 6);
		boolean expectedBasement =
				IceCreamParlourFeature.hasBasement(
						level.getSeed(), centre);

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
			Map<Block, Integer> palette =
					new LinkedHashMap<>();
			for (int x = -6; x <= 6; x++) {
				for (int y = -19; y <= 7;
						y++) {
					for (int z = -6; z <= 6;
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
			ResourceLocation biomeId =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(
									centre)
									.value());
			boolean literalIglooEligible =
					level.getBiome(centre).is(
							BiomeTags.HAS_IGLOO);

			BlockEntity brewingEntity =
					level.getBlockEntity(
							centre.offset(
									-3, -17, 2));
			boolean stockedWeakness =
					brewingEntity
							instanceof BrewingStandBlockEntity
							&& PotionUtils.getPotion(
									((BrewingStandBlockEntity)
											brewingEntity)
											.getItem(0))
									== Potions.WEAKNESS;
			BlockEntity lootEntity =
					level.getBlockEntity(
							centre.offset(
									3, -17, 2));
			CompoundTag lootState =
					lootEntity == null
							? new CompoundTag()
							: lootEntity
									.saveWithoutMetadata();
			boolean parlourLoot =
					IceCreamParlourFeature.LOOT_ID
							.toString().equals(
									lootState
											.getString(
													"LootTable"));
			// Keep the remote chunk forced through a complete server-tick
			// boundary after generation so the one-shot START-phase resident
			// handoff has joined both entities before this observation.
			helper.runAfterDelay(20, () -> {
			helper.runAfterDelay(1, () -> {
			AABB cellarArea = new AABB(
					centre.offset(-5, -18, -5),
					centre.offset(6, -11, 6));
			List<GingerbreadFolk> folk =
					level.getEntitiesOfClass(
							GingerbreadFolk.class,
							cellarArea);
			List<CrumbledGingerbreadFolk> crumbs =
					level.getEntitiesOfClass(
							CrumbledGingerbreadFolk
									.class,
							cellarArea);
			boolean curingPair =
					folk.size() == 1
							&& crumbs.size() == 1
							&& folk.get(0)
									.isPersistenceRequired()
							&& crumbs.get(0)
									.isPersistenceRequired()
							&& folk.get(0)
									.getVillagerData()
									.getType()
									== VillagerType.SNOW
							&& folk.get(0)
									.getVillagerData()
									.getProfession()
									== VillagerProfession
											.CLERIC
							&& crumbs.get(0)
									.getVillagerData()
									.getType()
									== VillagerType.SNOW
							&& crumbs.get(0)
									.getVillagerData()
									.getProfession()
									== VillagerProfession
											.CLERIC;
			boolean residentMarkerPresent =
					level.getBlockState(
							centre.offset(
									0, -17, 5))
							.is(Blocks.STRUCTURE_VOID);
			boolean pendingHandoff =
					expectedBasement
							&& residentMarkerPresent
							&& folk.isEmpty()
							&& crumbs.isEmpty();

			int cellarLadders = 0;
			for (int y = -17; y <= -1;
					y++) {
				if (level.getBlockState(
						centre.offset(0, y, 2))
						.is(Blocks.LADDER)) {
					cellarLadders++;
				}
			}
			boolean noCellarArtifacts =
					!level.getBlockState(
							centre.offset(0, 0, 2))
							.is(Blocks.OAK_TRAPDOOR)
							&& brewingEntity == null
							&& lootEntity == null
							&& cellarLadders == 0
							&& folk.isEmpty()
							&& crumbs.isEmpty()
							&& !residentMarkerPresent;

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

			LOGGER.info("Focused Ice-Cream Parlour audit: locate={}, centre={}, bounds={}, biome={}, expectedBasement={}, palette={}, weakness={}, loot={}, folk={}, crumbs={}, pendingHandoff={}",
					located, centre, savedBounds,
					biomeId, expectedBasement,
					palette, stockedWeakness,
					parlourLoot, folk.size(),
					crumbs.size(),
					pendingHandoff);
			require(helper,
					CakeWorldBiomes
							.ICE_CREAM_TUNDRA
							.getId().equals(biomeId)
							&& !literalIglooEligible,
					"The natural Ice-Cream Parlour lost its exclusive Ice-Cream Tundra home or leaked literal Igloo eligibility: biome="
							+ biomeId
							+ ", literalIglooEligible="
							+ literalIglooEligible);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.MARSHMALLOW
									.get(), 0) >= 300
							&& palette.getOrDefault(
									CakeWorldBlocks
											.FROZEN_LEMONADE
											.get(), 0)
									>= (expectedBasement
											? 120
											: 121)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING
											.get(), 0)
									>= 89
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									>= 14
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= 12
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									>= 8
							&& palette.getOrDefault(
									CakeWorldBlocks
											.OVEN
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MIXING_BOWL
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOLING_RACK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SODA_FOUNTAIN
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									Blocks.LIGHT_BLUE_BED,
									0) == 2,
					"The natural Ice-Cream Parlour lost its soft shelter, frozen floor, scoop roof or kitchen roles: "
							+ palette);
			if (expectedBasement) {
				require(helper,
						palette.getOrDefault(
								CakeWorldBlocks
										.GINGERBREAD_BRICKS
										.get(), 0)
								>= 250
								&& palette.getOrDefault(
										CakeWorldBlocks
												.BISCUIT_STONE
												.get(), 0)
										>= 81
								&& palette.getOrDefault(
										Blocks.IRON_BARS,
										0) == 22
								&& palette.getOrDefault(
										Blocks
												.OAK_FENCE_GATE,
										0) == 2
								&& level.getBlockState(
										centre.offset(
												0, 0,
												2))
										.is(Blocks
												.OAK_TRAPDOOR)
								&& cellarLadders == 17
								&& stockedWeakness
								&& parlourLoot
								&& (curingPair
										|| pendingHandoff),
						"The natural Ice-Cream Parlour lost its hidden cellar, cure station, loot or persistent Snow-cleric pair: "
								+ palette
								+ ", weakness="
								+ stockedWeakness
								+ ", loot="
								+ parlourLoot
								+ ", folk="
								+ folk.size()
								+ ", crumbs="
								+ crumbs.size()
								+ ", pendingHandoff="
								+ pendingHandoff);
			} else {
				require(helper, noCellarArtifacts,
						"The natural surface-only Ice-Cream Parlour leaked cellar blocks or residents");
			}
			helper.succeed();
			});
			});
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 7200)
	public static void focusedBurntSugarArchStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Burnt-Sugar Arch audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel overworld = helper.getLevel();
		ServerLevel nether = overworld.getServer()
				.getLevel(Level.NETHER);
		require(helper, nether != null,
				"Burnt-Sugar Arch audit could not open the Nether");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				overworld.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						BurntSugarArchFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Burnt-Sugar Arch configured structure was absent from the live registry");
		boolean tagged = structures.getTag(
						BurntSugarArchFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, tagged,
				"Burnt-Sugar Arch lost its public locate tag");

		LocatedArch overworldArch =
				locateBurntSugarArch(
						helper, overworld,
						configured,
						helper.absolutePos(
								new BlockPos(
										4, 4, 4)),
						"Overworld");
		LocatedArch netherArch =
				locateBurntSugarArch(
						helper, nether,
						configured,
						new BlockPos(0, 64, 0),
						"Nether");
		setArchChunksForced(overworld,
				overworldArch, true);
		setArchChunksForced(nether,
				netherArch, true);

		helper.runAfterDelay(60, () -> {
			ArchWorldAudit overworldAudit =
					auditBurntSugarArch(
							overworld,
							overworldArch,
							false);
			ArchWorldAudit netherAudit =
					auditBurntSugarArch(
							nether,
							netherArch,
							true);
			setArchChunksForced(overworld,
					overworldArch, false);
			setArchChunksForced(nether,
					netherArch, false);

			Set<ResourceLocation> expectedOverworld =
					Set.of(
							CakeWorldBiomes
									.CANDY_PLAINS
									.getId(),
							CakeWorldBiomes
									.COOKIE_FOREST
									.getId(),
							CakeWorldBiomes
									.MARSHMALLOW_PEAKS
									.getId(),
							CakeWorldBiomes
									.SODA_OCEAN
									.getId());
			LOGGER.info("Focused Burnt-Sugar Arch audit: overworldLocate={}, overworldCentre={}, overworldBounds={}, overworldBiome={}, overworldPalette={}, overworldLoot={}, overworldRepairable={}; netherLocate={}, netherCentre={}, netherBounds={}, netherBiome={}, netherPalette={}, netherLoot={}, netherRepairable={}",
					overworldArch.located(),
					overworldArch.centre(),
					overworldArch.bounds(),
					overworldAudit.biome(),
					overworldAudit.palette(),
					overworldAudit.loot(),
					overworldAudit.repairable(),
					netherArch.located(),
					netherArch.centre(),
					netherArch.bounds(),
					netherAudit.biome(),
					netherAudit.palette(),
					netherAudit.loot(),
					netherAudit.repairable());
			Set<ResourceLocation> expectedNether =
					Set.of(
							CakeWorldBiomes.FUDGE_WASTES
									.getId(),
							CakeWorldBiomes
									.CINNAMON_EMBER_GROVES
									.getId(),
							CakeWorldBiomes
									.BLACK_LIQUORICE_LABYRINTHS
									.getId(),
							CakeWorldBiomes
									.TREACLE_SOUL_VALLEYS
									.getId(),
							CakeWorldBiomes
									.CHILLI_CHOCOLATE_CRAGS
									.getId(),
							CakeWorldBiomes
									.MOLTEN_MARSHMALLOW_CALDERAS
									.getId());
			require(helper,
					expectedOverworld.contains(
							overworldAudit.biome())
							&& expectedNether.contains(
									netherAudit.biome())
							&& !overworldAudit
									.literalEligible()
							&& !netherAudit
									.literalEligible(),
					"Natural Burnt-Sugar Arches left their tagged CakeWorld Overworld/Nether homes or leaked literal Ruined Portal eligibility: overworld="
							+ overworldAudit.biome()
							+ ", nether="
							+ netherAudit.biome()
							+ ", overworldLiteral="
							+ overworldAudit
									.literalEligible()
							+ ", netherLiteral="
							+ netherAudit
									.literalEligible());
			require(helper,
					overworldArch.bounds()
							.getXSpan() == 17
							&& overworldArch.bounds()
									.getYSpan() == 17
							&& overworldArch.bounds()
									.getZSpan() == 17
							&& netherArch.bounds()
									.getXSpan() == 17
							&& netherArch.bounds()
									.getYSpan() == 17
							&& netherArch.bounds()
									.getZSpan() == 17
							&& netherArch.bounds()
									.minY() >= 32
							&& netherArch.bounds()
									.minY() <= 80,
					"Natural Burnt-Sugar Arch lost its exact saved bounds or deterministic Nether pocket range: overworld="
							+ overworldArch.bounds()
							+ ", nether="
							+ netherArch.bounds());
			assertNaturalArchPalette(
					helper, overworldAudit,
					CakeWorldBlocks.BISCUIT_STONE
							.get(),
					CakeWorldBlocks.HONEYCOMB_GOLD
							.get(),
					"Overworld");
			assertNaturalArchPalette(
					helper, netherAudit,
					CakeWorldBlocks.FUDGE_ROCK.get(),
					CakeWorldBlocks.FUDGE_GOLD.get(),
					"Nether");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 7200)
	public static void focusedWaferWreckStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Wafer Wreck audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						WaferWreckFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Wafer Wreck configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						WaferWreckFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		boolean shipwreckTag = structures.getTag(
						ConfiguredStructureTags
								.SHIPWRECK)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		boolean dolphinTag = structures.getTag(
						ConfiguredStructureTags
								.DOLPHIN_LOCATED)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper,
				locatedTag && shipwreckTag
						&& dolphinTag,
				"Wafer Wreck lost its public locate, vanilla Shipwreck or Dolphin-located tag: own="
						+ locatedTag
						+ ", shipwreck="
						+ shipwreckTag
						+ ", dolphin="
						+ dolphinTag);

		LocatedWreck wreck = locateWaferWreck(
				helper, level, configured,
				new BlockPos(96, 64, 128));
		setWreckChunksForced(level, wreck, true);
		helper.runAfterDelay(60, () -> {
			WreckWorldAudit audit =
					auditWaferWreck(level, wreck);
			setWreckChunksForced(level, wreck,
					false);
			LOGGER.info("Focused Wafer Wreck audit: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, loot={}, literalEligible={}",
					wreck.located(),
					wreck.centre(),
					wreck.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.loot(),
					audit.literalEligible());
			require(helper,
					CakeWorldBiomes.SODA_OCEAN
							.getId().equals(
									audit.biome())
							&& !audit
									.literalEligible(),
					"Natural Wafer Wreck left Soda Ocean or leaked literal vanilla Shipwreck biome eligibility: biome="
							+ audit.biome()
							+ ", literal="
							+ audit.literalEligible());
			require(helper,
					wreck.bounds().getXSpan()
							== 33
							&& wreck.bounds()
									.getYSpan()
									== 17
							&& wreck.bounds()
									.getZSpan()
									== 33
							&& wreck.centre()
									.getY()
									< level
											.getSeaLevel(),
					"Natural Wafer Wreck lost its exact saved rotated envelope or ocean-floor placement: "
							+ wreck.bounds()
							+ ", centre="
							+ wreck.centre()
							+ ", seaLevel="
							+ level.getSeaLevel());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.WAFER_BLOCK
									.get(), 0)
							== 700
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(),
									0) == 94
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING.get(),
									0) == 21
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(),
									0) == 12
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RASPBERRY_GUMMY_BLOCK
											.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_STONE
											.get(),
									0) == 3
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 3
							&& palette.getOrDefault(
									CakeWorldFluids
											.LEMONADE_BLOCK
											.get(),
									0) > 0
							&& audit.loot()
									.equals(Set.of(
											WaferWreckFeature
													.SUPPLY_LOOT_ID,
											WaferWreckFeature
													.MAP_LOOT_ID,
											WaferWreckFeature
													.TREASURE_LOOT_ID)),
					"Natural Wafer Wreck lost its full damaged edible ship, surrounding Lemonade or three cargo identities: palette="
							+ palette
							+ ", loot="
							+ audit.loot());
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct010world",
			timeoutTicks = 7200)
	public static void focusedCaramelCottageStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Caramel Cottage audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						CaramelCottageFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Caramel Cottage configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						CaramelCottageFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Caramel Cottage lost its public configured-structure locate tag");

		LocatedCottage cottage =
				locateCaramelCottage(
						helper, level, configured,
						new BlockPos(96, 64, 128));
		setCottageChunksForced(level, cottage, true);
		// A forced ticket is asynchronous. Touch the saved piece first so
		// its durable resident marker can be consumed on a normal server
		// tick before the evidence callback inspects the inhabitants.
		helper.runAfterDelay(80, () ->
				level.getChunkAt(cottage.centre()));
		helper.runAfterDelay(100, () -> {
			auditCaramelCottage(level, cottage);
		});
		helper.runAfterDelay(102, () -> {
			helper.succeedWhen(() -> {
			CottageWorldAudit audit =
					auditCaramelCottage(
							level, cottage);
			require(helper,
					CakeWorldBiomes.CARAMEL_BOGS
							.getId().equals(
									audit.biome())
							&& !audit
									.literalEligible(),
					"Natural Caramel Cottage did not migrate to Caramel Bogs or leaked literal vanilla Swamp-Hut biome eligibility: biome="
							+ audit.biome()
							+ ", literal="
							+ audit.literalEligible());
			require(helper,
					cottage.bounds().getXSpan()
							== 15
							&& cottage.bounds()
									.getYSpan()
									== 12
							&& cottage.bounds()
									.getZSpan()
									== 15
							&& cottage.centre()
									.getY()
									>= level
											.getSeaLevel()
											- 4,
					"Natural Caramel Cottage lost its exact saved PIECE envelope or average surface placement: "
							+ cottage.bounds()
							+ ", centre="
							+ cottage.centre()
							+ ", seaLevel="
							+ level.getSeaLevel());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0)
							>= 90
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(),
									0) == 160
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING.get(),
									0) == 134
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(),
									0) >= 28
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(),
									0) == 8
							&& palette.getOrDefault(
									CakeWorldBlocks
											.TREACLE_REED
											.get(),
									0) == 3
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CARAMEL_CRUST
											.get(),
									0) >= 6
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SYRUP_PIPE
											.get(),
									0) == 2
							&& palette.getOrDefault(
									CakeWorldBlocks
											.OVEN.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_COOKER
											.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MIXING_BOWL
											.get(),
									0) == 1
							&& palette.getOrDefault(
									Blocks.CAULDRON,
									0) == 3
							&& palette.getOrDefault(
									Blocks.BARREL,
									0) == 2
							&& palette.getOrDefault(
									Blocks.CRAFTING_TABLE,
									0) == 1
							&& palette.getOrDefault(
									Blocks
											.POTTED_RED_MUSHROOM,
									0) == 1
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 0
							&& audit.syrupBuckets()
									== 1
							&& audit
									.caramelBuckets()
									== 2
							&& !audit.randomLoot(),
					"Natural Caramel Cottage lost its complete edible shell, kitchen, garden, caramel/syrup roles or no-invented-loot contract: "
							+ palette
							+ ", syrupBuckets="
							+ audit.syrupBuckets()
							+ ", caramelBuckets="
							+ audit.caramelBuckets()
							+ ", randomLoot="
							+ audit.randomLoot());
			require(helper,
					audit.persistentBakers()
							== 1
							&& audit
									.persistentCats()
									== 1
							&& audit
									.raidCapableBaker()
							&& audit
									.allBlackCat()
							&& audit
									.markerConsumed(),
					"Natural Caramel Cottage is still waiting for exactly one persistent raid-capable Bitter Baker, one persistent all-black Custard Cat and its consumed durable marker: "
							+ audit);
			setCottageChunksForced(
					level, cottage, false);
			LOGGER.info("Focused Caramel Cottage settled: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, persistentBakers={}, persistentCats={}, markerConsumed={}, literalEligible={}",
					cottage.located(),
					cottage.centre(),
					cottage.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.persistentBakers(),
					audit.persistentCats(),
					audit.markerConsumed(),
					audit.literalEligible());
			});
		});
	}

	@GameTest(template = EMPTY, batch = "struct011world",
			timeoutTicks = 7200)
	public static void focusedAncientCakeVaultStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Ancient Cake Vault audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						AncientCakeVaultFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Ancient Cake Vault configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						AncientCakeVaultFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		boolean eyeTag = structures.getTag(
						ConfiguredStructureTags
								.EYE_OF_ENDER_LOCATED)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag && eyeTag,
				"Ancient Cake Vault lost its public locate tag or vanilla Eye-of-Ender locate contract");

		LocatedVault vault = locateAncientCakeVault(
				helper, level, configured,
				new BlockPos(96, 64, 128));
		setVaultChunksForced(level, vault, true);
		helper.runAfterDelay(100, () -> {
			VaultPieceAudit portal =
					auditVaultPiece(
							level,
							vault.portalRoom());
			VaultPieceAudit library =
					vault.library() == null
							? null
							: auditVaultPiece(
									level,
									vault.library());
			VaultPieceAudit corridor =
					vault.corridor() == null
							? null
							: auditVaultPiece(
									level,
									vault.corridor());
			BlockPos vaultBiomePosition =
					new BlockPos(
							(vault.bounds().minX()
									+ vault.bounds()
											.maxX())
									/ 2,
							level.getSeaLevel(),
							(vault.bounds().minZ()
									+ vault.bounds()
											.maxZ())
									/ 2);
			ResourceLocation biome =
					level.registryAccess()
							.registryOrThrow(
									Registry.BIOME_REGISTRY)
							.getKey(level.getBiome(
									vaultBiomePosition)
									.value());
			boolean literalEligible =
					level.getBiome(
							vaultBiomePosition)
							.is(BiomeTags.HAS_STRONGHOLD);
			boolean portalCompletable =
					provesCompletableEndPortal(
							level,
							portal.frames());
			setVaultChunksForced(level, vault,
					false);
			LOGGER.info("Focused Ancient Cake Vault audit: locate={}, eyeLocate={}, bounds={}, biome={}, pieces={}, portals={}, libraries={}, corridors={}, junctions={}, maxDepth={}, portalPalette={}, portalFrames={}, portalSpawner={}, libraryPalette={}, libraryLoot={}, corridorPalette={}, corridorLoot={}, literalEligible={}, portalCompletable={}",
					vault.located(),
					vault.eyeLocated(),
					vault.bounds(),
					biome,
					vault.pieces(),
					vault.portalRooms(),
					vault.libraries(),
					vault.corridors(),
					vault.junctions(),
					vault.maximumDepth(),
					portal.palette(),
					portal.frames().size(),
					portal.spawnerEntity(),
					library == null
							? Map.of()
							: library.palette(),
					library == null
							? Set.of()
							: library.loot(),
					corridor == null
							? Map.of()
							: corridor.palette(),
					corridor == null
							? Set.of()
							: corridor.loot(),
					literalEligible,
					portalCompletable);
			require(helper,
					vault.located().getX()
							== vault.eyeLocated()
									.getX()
							&& vault.located().getZ()
									== vault.eyeLocated()
											.getZ()
							&& !literalEligible
							&& biome != null
							&& CakeWorld.MODID.equals(
									biome.getNamespace())
							&& BiomeDictionary.hasType(
									biomeKey(biome),
									BiomeDictionary.Type
											.OVERWORLD)
							&& !BiomeDictionary.hasType(
									biomeKey(biome),
									BiomeDictionary.Type
											.OCEAN),
					"Natural Ancient Cake Vault did not share the Eye locate result or remain confined to a CakeWorld land biome outside literal vanilla Stronghold eligibility: own="
							+ vault.located()
							+ ", eye="
							+ vault.eyeLocated()
							+ ", biome=" + biome
							+ ", literal="
							+ literalEligible);
			require(helper,
					vault.pieces() >= 12
							&& vault.portalRooms()
									== 1
							&& vault.libraries()
									>= 1
							&& vault.libraries()
									<= 2
							&& vault.junctions()
									>= 1
							&& vault.maximumDepth()
									<= 51
							&& vault.bounds()
									.getXSpan()
									>= 30
							&& vault.bounds()
									.getZSpan()
									>= 30
							&& vault.bounds()
									.getXSpan()
									<= 240
							&& vault.bounds()
									.getZSpan()
									<= 240,
					"Natural Ancient Cake Vault lost the saved sprawling Stronghold graph and guaranteed portal/library progression: "
							+ vault);
			Map<Block, Integer> portalPalette =
					portal.palette();
			require(helper,
					portal.frames().size() == 12
							&& "minecraft:silverfish"
									.equals(portal
											.spawnerEntity())
							&& portalCompletable
							&& portalPalette.getOrDefault(
									CakeWorldBlocks
											.GINGERBREAD_BRICKS
											.get(),
									0) > 0
							&& portalPalette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW
											.get(),
									0) == 8
							&& portalPalette.getOrDefault(
									CakeWorldFluids
											.HOT_FUDGE_BLOCK
											.get(),
									0) == 15
							&& hasNoUnthemedVaultBlocks(
									portalPalette),
					"Natural Ancient Cake Vault portal room lost its edible masonry, rescue stairs, Hot-Fudge basin, Silverfish-to-Crumb-Mite source or genuine End progression: "
							+ portal);
			require(helper,
					library != null
							&& library.palette()
									.getOrDefault(
											CakeWorldBlocks
													.COOKBOOK_LIBRARY
													.get(),
											0) > 0
							&& hasNoUnthemedVaultBlocks(
									library.palette())
							&& library.loot()
									.equals(Set.of(
											BuiltInLootTables
													.STRONGHOLD_LIBRARY)),
					"Natural Ancient Cake Vault library lost its Cookbook shelves, edible masonry or exact vanilla loot role: "
							+ library);
			if (corridor != null) {
				require(helper,
						hasNoUnthemedVaultBlocks(
								corridor.palette())
								&& corridor.loot()
										.equals(Set.of(
												BuiltInLootTables
														.STRONGHOLD_CORRIDOR)),
						"Natural Ancient Cake Vault chest corridor lost its edible masonry or exact vanilla loot role: "
								+ corridor);
			}
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct012world",
			timeoutTicks = 7200)
	public static void focusedSodaPalaceStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Soda Palace audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						SodaPalaceFeature.STRUCTURE_ID);
		require(helper, configured != null,
				"Soda Palace configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						SodaPalaceFeature.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Soda Palace lost its public locate tag");

		LocatedPalace palace = locateSodaPalace(
				helper, level, configured,
				new BlockPos(96, 64, 128));
		setPalaceChunksForced(level, palace, true);
		helper.runAfterDelay(160, () -> {
			PalaceWorldAudit audit =
					auditSodaPalace(level, palace);
			LOGGER.info("Focused Soda Palace audit: locate={}, bounds={}, biome={}, pieces={}, childPieces={}, spongeRooms={}, palette={}, grandGuardians={}, literalElders={}, literalEligible={}",
					palace.located(),
					palace.bounds(),
					audit.biome(),
					palace.pieces(),
					audit.childPieces(),
					audit.spongeRooms(),
					audit.palette(),
					audit.grandGuardians(),
					audit.literalElders(),
					audit.literalEligible());
			require(helper,
					CakeWorldBiomes.SODA_OCEAN
							.getId().equals(
									audit.biome())
							&& audit
									.literalEligible(),
					"Natural Soda Palace left Soda Ocean or lost its explicit native Monument biome-tag bridge: biome="
							+ audit.biome()
							+ ", literal="
							+ audit
									.literalEligible());
			require(helper,
					palace.pieces() == 1
							&& palace.bounds()
									.getXSpan() == 58
							&& palace.bounds()
									.getYSpan() == 23
							&& palace.bounds()
									.getZSpan() == 58
							&& palace.bounds()
									.minY() == 39
							&& palace.bounds()
									.maxY() == 61
							&& audit.childPieces()
									>= 20,
					"Natural Soda Palace lost its one saved MonumentBuilding or regenerated 58x23x58 room graph: "
							+ palace
							+ ", children="
							+ audit.childPieces());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.BLUEBERRY_GUMMY_BLOCK
									.get(), 0) > 1000
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(),
									0) > 1000
							&& palette.getOrDefault(
									CakeWorldBlocks
											.GRAPE_GUMMY_BLOCK
											.get(),
									0) > 100
							&& palette.getOrDefault(
									Blocks.WATER,
									0) > 1000
							&& palette.getOrDefault(
									Blocks.SEA_LANTERN,
									0) > 0
							&& palette.getOrDefault(
									Blocks.GOLD_BLOCK,
									0) == 8
							&& (audit.spongeRooms()
									== 0
									|| palette
											.getOrDefault(
													Blocks.WET_SPONGE,
													0)
											> 0)
							&& hasNoUnthemedPalaceMasonry(
									palette),
					"Natural Soda Palace lost its complete gummy/glass masonry, native water navigation, lights, eight-block gold core or sponge-room reward: "
							+ palette
							+ ", spongeRooms="
							+ audit.spongeRooms());
			require(helper,
					audit.grandGuardians() == 3
							&& audit.literalElders()
									== 0,
					"Natural Soda Palace did not retain exactly three converted Grand Gumball Guardian encounters: custom="
							+ audit
									.grandGuardians()
							+ ", literal="
							+ audit.literalElders());

			List<GumballGuardian> before =
					level.getEntitiesOfClass(
							GumballGuardian.class,
							palace.boundsAabb());
			Set<UUID> beforeIds = before.stream()
					.map(net.minecraft.world.entity.Entity
							::getUUID)
					.collect(java.util.stream.Collectors
							.toSet());
			Guardian probe =
					EntityType.GUARDIAN.create(level);
			require(helper, probe != null,
					"Could not create the native Monument Guardian spawn probe");
			BlockPos probePosition = null;
			for (BlockPos candidate : BlockPos.betweenClosed(
					palace.bounds().minX(),
					palace.bounds().minY(),
					palace.bounds().minZ(),
					palace.bounds().maxX(),
					palace.bounds().maxY() - 1,
					palace.bounds().maxZ())) {
				if (level.getBlockState(candidate)
						.is(Blocks.WATER)
						&& level.getBlockState(candidate.above())
								.is(Blocks.WATER)) {
					probePosition = candidate.immutable();
					break;
				}
			}
			require(helper, probePosition != null,
					"Natural Soda Palace exposed no two-block-deep water cell for its Guardian conversion probe");
			probe.moveTo(
					probePosition.getX() + 0.5D,
					probePosition.getY() + 0.5D,
					probePosition.getZ() + 0.5D,
					0.0F, 0.0F);
			probe.setNoAi(true);
			probe.setNoGravity(true);
			probe.setInvulnerable(true);
			probe.setPersistenceRequired();
			level.addFreshEntity(probe);
			helper.runAfterDelay(3, () -> helper.succeedWhen(() -> {
				List<GumballGuardian> after =
						level.getEntitiesOfClass(
								GumballGuardian.class,
								palace.boundsAabb());
				List<GumballGuardian> converted =
						after.stream()
								.filter(entity ->
										!beforeIds
												.contains(
														entity.getUUID()))
								.toList();
				require(helper,
						probe.isRemoved()
								&& converted.size()
										== 1,
						"Native structure Guardian did not convert exactly once to a Gumball Guardian inside the natural Soda Palace: before="
								+ before.size()
								+ ", after="
								+ after.size()
								+ ", converted="
								+ converted.size());
				converted.forEach(
						GumballGuardian::discard);
				setPalaceChunksForced(
						level, palace, false);
			}));
		});
	}

	@GameTest(template = EMPTY, batch = "struct013world",
			timeoutTicks = 7200)
	public static void focusedSunkenSweetshopStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Sunken Sweetshop audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> cold =
				structures.get(SunkenSweetshopFeature
						.COLD_STRUCTURE_ID);
		ConfiguredStructureFeature<?, ?> warm =
				structures.get(SunkenSweetshopFeature
						.WARM_STRUCTURE_ID);
		require(helper, cold != null && warm != null,
				"Sunken Sweetshop warm/cold native configured structures were absent from the live registry");
		LocatedSweetshop sweetshop =
				locateSunkenSweetshop(helper, level,
						cold, warm,
						new BlockPos(96, 64, 128));
		setSweetshopChunksForced(
				level, sweetshop, true);
		helper.runAfterDelay(160, () -> {
			SweetshopWorldAudit audit =
					auditSunkenSweetshop(
							level, sweetshop);
			LOGGER.info("Focused Sunken Sweetshop audit: locate={}, configured={}, bounds={}, biome={}, pieces={}, templates={}, integrities={}, large={}, palette={}, loot={}, soggyBiscuits={}, literalDrowned={}, literalEligible={}",
					sweetshop.located(),
					sweetshop.configuredId(),
					sweetshop.bounds(),
					audit.biome(),
					sweetshop.start()
							.getPieces().size(),
					audit.templates(),
					audit.integrities(),
					audit.largePieces(),
					audit.palette(),
					audit.loot(),
					audit.soggyBiscuits(),
					audit.literalDrowned(),
					audit.literalEligible());
			require(helper,
					CakeWorldBiomes.SODA_OCEAN
							.getId().equals(
									audit.biome())
							&& audit
									.literalEligible(),
					"Natural Sunken Sweetshop left Soda Ocean or lost native warm/cold biome eligibility: biome="
							+ audit.biome()
							+ ", eligible="
							+ audit
									.literalEligible());
			boolean coldVariant =
					sweetshop.configuredId().equals(
							SunkenSweetshopFeature
									.COLD_STRUCTURE_ID);
			require(helper,
					!sweetshop.start()
							.getPieces().isEmpty()
							&& sweetshop.start()
									.getPieces()
									.stream()
									.allMatch(
											OceanRuinPieces
													.OceanRuinPiece
													.class
													::isInstance)
							&& (!coldVariant
									|| sweetshop
											.start()
											.getPieces()
											.size()
											% 3 == 0)
							&& audit.templates()
									.stream()
									.allMatch(name ->
											coldVariant
													? name.contains(
															"brick")
															|| name
																	.contains(
																			"cracked")
															|| name
																	.contains(
																			"mossy")
													: name.contains(
															"warm")),
					"Natural Sunken Sweetshop lost its saved native warm plan or cold three-layer plan: "
							+ sweetshop
									.configuredId()
							+ " "
							+ audit.templates());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0)
								+ palette.getOrDefault(
										CakeWorldBlocks
												.WAFER_BLOCK
												.get(),
										0)
								+ palette.getOrDefault(
										CakeWorldBlocks
												.BISCUIT_STONE
												.get(),
										0)
								+ palette.getOrDefault(
										CakeWorldBlocks
												.ROCK_CANDY
												.get(),
										0)
								+ palette.getOrDefault(
										CakeWorldBlocks
												.GRAPE_GUMMY_BLOCK
												.get(),
										0)
								+ palette.getOrDefault(
										CakeWorldBlocks
												.BISCUIT_SAND
												.get(),
										0)
								+ palette.getOrDefault(
										CakeWorldBlocks
												.BISCUIT_CRUMBS
												.get(),
										0)
								> 10
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									> 0
							&& hasNoUnthemedSweetshopMasonry(
									palette),
					"Natural Sunken Sweetshop lost its piece-bounded edible warm/cold palette: "
							+ palette);
			require(helper,
					!audit.loot().isEmpty()
							&& audit.loot().stream()
									.allMatch(loot ->
											loot.equals(
													BuiltInLootTables
															.UNDERWATER_RUIN_SMALL)
													|| loot.equals(
															BuiltInLootTables
																	.UNDERWATER_RUIN_BIG))
							&& (audit.largePieces() == 0
									|| audit.loot()
											.contains(
													BuiltInLootTables
															.UNDERWATER_RUIN_BIG)),
					"Natural Sunken Sweetshop lost its native small/big underwater ruin loot roles: large="
							+ audit.largePieces()
							+ ", loot="
							+ audit.loot());
			require(helper,
					audit.literalDrowned()
									== 0,
					"Natural Sunken Sweetshop retained literal Drowned residue instead of using the existing marker conversion boundary: custom="
							+ audit
									.soggyBiscuits()
							+ ", literal="
							+ audit.literalDrowned());
			setSweetshopChunksForced(
					level, sweetshop, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct014world",
			timeoutTicks = 12000)
	public static void focusedLiquoriceFortressStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Liquorice Fortress audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.NETHER);
		require(helper, level != null,
				"The fixed-seed server did not expose the Nether");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						LiquoriceFortressFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Liquorice Fortress native configured structure was absent from the live registry");
		LocatedFortress fortress =
				locateLiquoriceFortress(
						helper, level, configured,
						new BlockPos(0, 64, 0));
		setFortressChunksForced(
				level, fortress, true);
		helper.runAfterDelay(240, () -> {
			FortressWorldAudit audit =
					auditLiquoriceFortress(
							level, fortress);
			boolean sentinelAlreadyNative =
					level.getBlockState(
							audit.sentinel())
							.is(Blocks.NETHER_BRICKS);
			int nativeMasonry =
					audit.palette().getOrDefault(
							Blocks.NETHER_BRICKS, 0)
					+ audit.palette().getOrDefault(
							Blocks.NETHER_BRICK_STAIRS,
							0)
					+ audit.palette().getOrDefault(
							Blocks.NETHER_BRICK_FENCE,
							0)
					+ audit.palette().getOrDefault(
							Blocks.NETHER_WART, 0);
			LOGGER.info("Focused Liquorice Fortress audit: locate={}, bounds={}, biome={}, pieces={}, depth={}, palette={}, loot={}, blazeSpawners={}, cinnamonSparks={}, literalBlazes={}, nativeMasonry={}, sentinel={}, markerPhase={}",
					fortress.located(),
					fortress.bounds(),
					audit.biome(),
					audit.pieceKinds(),
					audit.maximumDepth(),
					audit.palette(),
					audit.loot(),
					audit.blazeSpawners(),
					audit.cinnamonSparks(),
					audit.literalBlazes(),
					nativeMasonry,
					audit.sentinel(),
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					CakeWorldBiomes.FUDGE_WASTES
							.getId().equals(
									audit.biome())
							&& audit
									.literalEligible(),
					"Natural Liquorice Fortress left Fudge Wastes or lost native/CakeWorld biome eligibility: biome="
							+ audit.biome()
							+ ", eligible="
							+ audit
									.literalEligible());
			require(helper,
					fortress.start().isValid()
							&& fortress.start()
									.getFeature()
									== configured
							&& fortress.start()
									.getPieces()
									.size() >= 10
							&& fortress.start()
									.getPieces()
									.stream()
									.allMatch(piece ->
											piece.getClass()
													.getEnclosingClass()
													== NetherBridgePieces
															.class)
							&& audit.maximumDepth()
									<= 30
							&& fortress.bounds().minY()
									>= 48
							&& fortress.bounds().minY()
									<= 70
							&& audit.pieceKinds()
									.size() >= 8,
					"Natural Liquorice Fortress lost its native saved graph, depth-30 or minimum-height-48..70 contract: pieces="
							+ fortress.start()
									.getPieces().size()
							+ ", kinds="
							+ audit.pieceKinds()
							+ ", depth="
							+ audit.maximumDepth()
							+ ", bounds="
							+ fortress.bounds());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.LIQUORICE_BRICKS
									.get(), 0) > 100
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIQUORICE_STAIRS
											.get(), 0)
									> 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIQUORICE_FENCE
											.get(), 0)
									> 0
							&& nativeMasonry
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Liquorice Fortress lost its complete piece-bounded masonry conversion or rewrote more than the explicit reload sentinel: native="
							+ nativeMasonry
							+ ", palette="
							+ palette);
			boolean hasThrone =
					audit.pieceKinds().containsKey(
							"MonsterThrone");
			boolean hasFarm =
					audit.pieceKinds().containsKey(
							"CastleStalkRoom");
			require(helper,
					(!hasThrone
							|| audit.blazeSpawners()
									== audit
											.pieceKinds()
											.get(
													"MonsterThrone"))
							&& (!hasFarm
									|| palette
											.getOrDefault(
													CakeWorldBlocks
															.CINNAMON_WART
															.get(),
													0)
											> 0
											&& palette
													.getOrDefault(
															Blocks.SOUL_SAND,
															0)
													> 0),
					"Natural Liquorice Fortress lost its native throne spawner or Cinnamon-Wart/Soul-Sand farm roles: thrones="
							+ audit.pieceKinds()
									.getOrDefault(
											"MonsterThrone",
											0)
							+ ", spawners="
							+ audit.blazeSpawners()
							+ ", farms="
							+ audit.pieceKinds()
									.getOrDefault(
											"CastleStalkRoom",
											0)
							+ ", palette="
							+ palette);
			require(helper,
					audit.loot().stream()
							.allMatch(loot ->
									loot.equals(
											BuiltInLootTables
													.NETHER_BRIDGE))
							&& audit.literalBlazes()
									== 0,
					"Natural Liquorice Fortress lost native chest loot identity or retained literal Blaze residue: loot="
							+ audit.loot()
							+ ", custom="
							+ audit.cinnamonSparks()
							+ ", literal="
							+ audit.literalBlazes());
			if (!sentinelAlreadyNative) {
				level.setBlock(audit.sentinel(),
						Blocks.NETHER_BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks
										.NETHER_BRICKS),
						"Could not seed the explicit player-placed Nether-Brick reload sentinel");
			}
			setFortressChunksForced(
					level, fortress, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct015world",
			timeoutTicks = 12000)
	public static void focusedMacaronCitadelStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Macaron Citadel audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.END);
		require(helper, level != null,
				"The fixed-seed server did not expose the End");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						MacaronCitadelFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Macaron Citadel native configured structure was absent from the live registry");
		LocatedCitadel citadel =
				locateMacaronCitadel(
						helper, level, configured,
						new BlockPos(1024, 96,
								1024));
		setCitadelChunksForced(
				level, citadel, true);
		helper.runAfterDelay(240, () -> {
			CitadelWorldAudit audit =
					auditMacaronCitadel(
							level, citadel);
			require(helper, audit.sentinel() != null,
					"Natural Macaron Citadel exposed no stable Macaron-Brick reload sentinel");
			boolean sentinelAlreadyNative =
					level.getBlockState(
							audit.sentinel())
							.is(Blocks.PURPUR_BLOCK);
			Map<Block, Integer> palette =
					audit.palette();
			int nativeMasonry =
					palette.getOrDefault(
							Blocks.PURPUR_BLOCK, 0)
					+ palette.getOrDefault(
							Blocks.PURPUR_PILLAR, 0)
					+ palette.getOrDefault(
							Blocks.PURPUR_STAIRS, 0)
					+ palette.getOrDefault(
							Blocks.PURPUR_SLAB, 0)
					+ palette.getOrDefault(
							Blocks.END_STONE_BRICKS,
							0)
					+ palette.getOrDefault(
							Blocks
									.MAGENTA_STAINED_GLASS,
							0);
			LOGGER.info("Focused Macaron Citadel audit: locate={}, bounds={}, biome={}, templates={}, depth={}, shipPieces={}, palette={}, loot={}, lootChests={}, macaronClams={}, literalShulkers={}, elytraFrames={}, dragonHeads={}, enderChests={}, brewingStands={}, healingPotions={}, nativeMasonry={}, sentinel={}, markerPhase={}",
					citadel.located(),
					citadel.bounds(),
					audit.biome(),
					audit.templates(),
					audit.maximumDepth(),
					audit.shipPieces(),
					audit.palette(),
					audit.loot(),
					audit.lootChests(),
					audit.macaronClams(),
					audit.literalShulkers(),
					audit.elytraFrames(),
					audit.dragonHeads(),
					audit.enderChests(),
					audit.brewingStands(),
					audit.healingPotions(),
					nativeMasonry,
					audit.sentinel(),
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					(CakeWorldBiomes.MERINGUE_ISLANDS
							.getId().equals(
									audit.biome())
							|| CakeWorldBiomes.MACARON_ARCHIPELAGO
									.getId().equals(
											audit.biome()))
							&& audit
									.literalEligible(),
					"Natural Macaron Citadel left its Meringue Islands or Macaron Archipelago homes, or lost native/CakeWorld biome eligibility: biome="
							+ audit.biome()
							+ ", eligible="
							+ audit
									.literalEligible());
			require(helper,
					citadel.start().isValid()
							&& citadel.start()
									.getFeature()
									== configured
							&& citadel.start()
									.getPieces()
									.size() >= 4
							&& citadel.start()
									.getPieces()
									.stream()
									.allMatch(
											EndCityPieces
													.EndCityPiece
													.class
													::isInstance)
							&& audit.maximumDepth()
									<= 8
							&& citadel.bounds()
									.minY() >= 60
							&& audit.templates()
									.keySet().stream()
									.anyMatch(name ->
											name.contains(
													"tower"))
							&& audit.shipPieces()
									<= 1,
					"Natural Macaron Citadel lost its native saved tower graph, depth-eight, height gate or at-most-one-airship contract: pieces="
							+ citadel.start()
									.getPieces()
									.size()
							+ ", templates="
							+ audit.templates()
							+ ", depth="
							+ audit.maximumDepth()
							+ ", bounds="
							+ citadel.bounds());
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.MACARON_BRICKS
									.get(), 0) > 25
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MACARON_PILLAR
											.get(),
									0) > 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MACARON_STAIRS
											.get(),
									0) > 0
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MERINGUE_BRICKS
											.get(),
									0) > 0
							&& nativeMasonry
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Macaron Citadel lost its complete piece-bounded tower palette or rewrote more than the explicit reload sentinel: native="
							+ nativeMasonry
							+ ", palette="
							+ palette);
			require(helper,
					audit.loot().stream()
							.allMatch(loot -> loot
									.equals(
											BuiltInLootTables
													.END_CITY_TREASURE))
							&& audit.literalShulkers()
									== 0
							&& audit.macaronClams()
									> 0,
					"Natural Macaron Citadel lost native treasure identity or retained literal Shulker markers: loot="
							+ audit.loot()
							+ ", custom="
							+ audit.macaronClams()
							+ ", literal="
							+ audit.literalShulkers());
			int thirdFloorTwos =
					audit.templates().entrySet()
							.stream()
							.filter(entry -> entry
									.getKey()
									.equals(
											"third_floor_2")
									|| entry.getKey()
											.endsWith(
													"/third_floor_2"))
							.mapToInt(
									Map.Entry::getValue)
							.sum();
			require(helper,
					audit.enderChests()
									== thirdFloorTwos
							&& (audit.lootChests()
									== 0
									|| audit.loot().equals(
											Set.of(
													BuiltInLootTables
															.END_CITY_TREASURE))),
					"Natural Macaron Citadel lost saved third-floor Ender-Chest or treasure-container roles: thirdFloor2="
							+ thirdFloorTwos
							+ ", enderChests="
							+ audit.enderChests()
							+ ", lootChests="
							+ audit.lootChests()
							+ ", loot="
							+ audit.loot());
			if (audit.shipPieces() == 1) {
				require(helper,
						palette.getOrDefault(
								CakeWorldBlocks
										.WAFER_BLOCK
										.get(), 0)
										> 0
								&& palette
										.getOrDefault(
												CakeWorldBlocks
														.WAFER_PILLAR
														.get(),
												0)
										> 0
								&& palette
										.getOrDefault(
												CakeWorldBlocks
														.WAFER_STAIRS
														.get(),
												0)
										> 0
								&& palette
										.getOrDefault(
												CakeWorldBlocks
														.WAFER_SLAB
														.get(),
												0)
										> 0
								&& audit.elytraFrames()
										== 1
								&& audit.dragonHeads()
										== 1
								&& audit.brewingStands()
										== 1
								&& audit.healingPotions()
										== 2,
						"Natural Wafer Airship lost its separate palette, Elytra, Dragon Head or healing supplies: palette="
								+ palette
								+ ", frames="
								+ audit.elytraFrames()
								+ ", heads="
								+ audit.dragonHeads()
								+ ", brewing="
								+ audit.brewingStands()
								+ ", healing="
								+ audit.healingPotions());
			} else {
				require(helper,
						audit.elytraFrames() == 0
								&& audit.dragonHeads()
										== 0
								&& audit.brewingStands()
										== 0
								&& audit.healingPotions()
										== 0,
						"Ship-only End-City rewards appeared in a natural graph with no Wafer Airship");
			}
			if (!sentinelAlreadyNative) {
				level.setBlock(audit.sentinel(),
						Blocks.PURPUR_BLOCK
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks
										.PURPUR_BLOCK),
						"Could not seed the explicit player-placed Purpur reload sentinel");
			}
			setCitadelChunksForced(
					level, citadel, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct016world",
			timeoutTicks = 7200)
	public static void focusedBuriedSweetTinStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Buried Sweet Tin audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
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
						BuriedSweetTinFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Buried Sweet Tin native configured structure was absent from the live registry");
		BlockPos wreck =
				level.findNearestMapFeature(
						WaferWreckFeature
								.STRUCTURE_TAG,
						new BlockPos(-256, 64,
								320),
						32, false);
		require(helper, wreck != null,
				"The fixed-seed world lost its Wafer Wreck map origin");
		LocatedTin tin = locateBuriedSweetTin(
				helper, level, configured, wreck);
		level.setChunkForced(
				tin.startChunk().x,
				tin.startChunk().z, true);
		level.getChunk(tin.startChunk().x,
				tin.startChunk().z);
		helper.runAfterDelay(120, () -> {
			BlockPos chest =
					findBuriedSweetTinChest(
							level, tin.startChunk());
			require(helper, chest != null,
					"Natural Buried Sweet Tin start did not create or repair its native-compatible chest");
			Map<Block, Integer> palette =
					scanBuriedSweetTinPalette(
							level, chest);
			BlockEntity entity =
					level.getBlockEntity(chest);
			CompoundTag chestState =
					entity == null
							? new CompoundTag()
							: entity
									.saveWithoutMetadata();
			BlockPos sentinel = chest.above();
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.SAND);
			boolean cacheComplete =
					level.getBlockState(sentinel)
							.is(sentinelAlreadyNative
									? Blocks.SAND
									: CakeWorldBlocks
											.BISCUIT_CRUMBS
											.get());
			for (Direction direction
					: Direction.Plane.HORIZONTAL) {
				cacheComplete &=
						level.getBlockState(
								chest.relative(
										direction))
								.is(CakeWorldBlocks
										.BISCUIT_SAND
										.get());
			}
			BlockPos anchor = new BlockPos(
					tin.startChunk().getBlockX(9),
					64,
					tin.startChunk().getBlockZ(9));
			ResourceLocation biome =
					level.getBiome(anchor)
							.unwrapKey()
							.map(key -> key
									.location())
							.orElse(null);
			boolean eligible =
					level.getBiome(anchor).is(
							BuriedSweetTinFeature
									.GENERATES_IN)
							&& level.getBiome(anchor)
									.is(BiomeTags
											.HAS_BURIED_TREASURE);

			LootContext mapContext =
					new LootContext.Builder(level)
							.withParameter(
									LootContextParams
											.ORIGIN,
									Vec3.atCenterOf(
											wreck))
							.create(
									LootContextParamSets
											.CHEST);
			List<ItemStack> mapLoot =
					level.getServer()
							.getLootTables()
							.get(WaferWreckFeature
									.MAP_LOOT_ID)
							.getRandomItems(
									mapContext);
			ItemStack treasureMap =
					mapLoot.stream()
							.filter(stack ->
									stack.is(
											Items.FILLED_MAP))
							.findFirst()
							.orElse(
									ItemStack.EMPTY);
			ListTag decorations =
					treasureMap.hasTag()
							? treasureMap.getTag()
									.getList(
											"Decorations",
											Tag.TAG_COMPOUND)
							: new ListTag();
			CompoundTag target =
					decorations.isEmpty()
							? new CompoundTag()
							: decorations
									.getCompound(0);
			boolean namedMap =
					!treasureMap.isEmpty()
							&& treasureMap
									.getHoverName()
									instanceof net.minecraft.network.chat
											.TranslatableComponent
							&& "filled_map.cakeworld.buried_sweet_tin"
									.equals(
											((net.minecraft.network.chat
													.TranslatableComponent)
													treasureMap
															.getHoverName())
																	.getKey());
			LOGGER.info("Focused Buried Sweet Tin audit: wreck={}, locate={}, startChunk={}, startBounds={}, chest={}, biome={}, palette={}, loot={}, customName={}, mapTarget=({},{}), sentinel={}, markerPhase={}",
					wreck, tin.located(),
					tin.startChunk(),
					tin.start().getBoundingBox(),
					chest, biome, palette,
					chestState.getString(
							"LootTable"),
					chestState.getString(
							"CustomName"),
					target.getDouble("x"),
					target.getDouble("z"),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					CakeWorldBiomes.SODA_OCEAN
							.getId().equals(biome)
							&& eligible,
					"Natural Buried Sweet Tin left Soda Ocean or lost native/CakeWorld biome eligibility: biome="
							+ biome + ", eligible="
							+ eligible);
			require(helper,
					tin.start().isValid()
							&& tin.start()
									.getFeature()
									== configured
							&& tin.start()
									.getPieces()
									.size() == 1
							&& tin.start()
									.getPieces()
									.get(0)
									instanceof BuriedTreasurePieces
											.BuriedTreasurePiece
							&& chest.getX()
									== tin.startChunk()
											.getBlockX(
													9)
							&& chest.getZ()
									== tin.startChunk()
											.getBlockZ(
													9)
							&& chest.getY()
									<= level
											.getSeaLevel(),
					"Natural Buried Sweet Tin lost its one native saved piece, +9/+9 anchor or ocean-floor placement: start="
							+ tin.start()
									.getBoundingBox()
							+ ", chest=" + chest);
			require(helper,
					palette.getOrDefault(
							Blocks.CHEST, 0)
									== 1
							&& cacheComplete
							&& BuiltInLootTables
									.BURIED_TREASURE
									.toString()
									.equals(chestState
											.getString(
													"LootTable"))
							&& chestState
									.getString(
											"CustomName")
									.contains(
											"container.cakeworld.buried_sweet_tin"),
					"Natural Buried Sweet Tin lost its compact edible cache, named native chest/loot or durable reload sentinel: palette="
							+ palette
							+ ", state="
							+ chestState);
			require(helper,
					namedMap
							&& decorations.size()
									== 1
							&& target.getDouble(
									"x")
									== tin.located()
											.getX()
							&& target.getDouble(
									"z")
									== tin.located()
											.getZ(),
					"Natural Wafer-Wreck map did not target the locatable Buried Sweet Tin: map="
							+ treasureMap
							+ ", decorations="
							+ decorations
							+ ", locate="
							+ tin.located());
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.SAND
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								sentinel)
								.is(Blocks.SAND),
						"Could not seed the explicit player-placed Sand reload sentinel above the Sweet Tin");
			}
			level.setChunkForced(
					tin.startChunk().x,
					tin.startChunk().z, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct017world",
			timeoutTicks = 7200)
	public static void focusedRockCandyFossilStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Rock-Candy Fossil audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.NETHER);
		require(helper, level != null,
				"The fixed-seed server did not expose the Nether");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						RockCandyFossilFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Rock-Candy Fossil native configured structure was absent from the live registry");
		BlockPos located = level.findNearestMapFeature(
				RockCandyFossilFeature.STRUCTURE_TAG,
				new BlockPos(0, 64, 0), 128,
				false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Rock-Candy Fossil within 128 chunks of the Nether origin");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		if (start == null || !start.isValid()) {
			start = level.structureFeatureManager()
					.startsForFeature(
							net.minecraft.core.SectionPos
									.of(located),
							configured)
					.stream()
					.filter(StructureStart::isValid)
					.findFirst().orElse(null);
		}
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1
						&& start.getPieces()
								.get(0)
								instanceof NetherFossilPieces
										.NetherFossilPiece,
				"The located Rock-Candy Fossil lost its one saved native fossil piece");
		StructureStart fossilStart = start;
		BoundingBox startBounds =
				start.getBoundingBox();
		BoundingBox bounds = start.getPieces()
				.get(0).getBoundingBox();
		int minimumChunkX =
				Math.floorDiv(bounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(bounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(bounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(bounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, true);
				level.getChunk(chunkX, chunkZ);
			}
		}
		helper.runAfterDelay(160, () -> {
			String template;
			try {
				Field templateName =
						TemplateStructurePiece.class
								.getDeclaredField(
										"templateName");
				templateName.setAccessible(true);
				template = templateName.get(
						fossilStart.getPieces()
								.get(0))
						.toString();
			} catch (ReflectiveOperationException
					exception) {
				throw new AssertionError(
						"Could not inspect saved Nether-Fossil template identity",
						exception);
			}
			int templateNumber;
			try {
				templateNumber = Integer.parseInt(
						template.substring(
								template
										.lastIndexOf(
												'_')
										+ 1));
			} catch (RuntimeException exception) {
				throw new AssertionError(
						"Natural Rock-Candy Fossil used an unknown native template: "
								+ template,
						exception);
			}
			int[] nativeBoneCounts = {
				10, 10, 6, 6, 5, 21, 18,
				6, 15, 8, 24, 11, 17, 26
			};
			require(helper,
					templateNumber >= 1
							&& templateNumber
									<= nativeBoneCounts.length,
					"Natural Rock-Candy Fossil escaped the native fourteen-template catalogue: "
							+ template);
			int expectedBones =
					nativeBoneCounts[
							templateNumber - 1];
			Map<Direction.Axis, Integer> axes =
					new LinkedHashMap<>();
			int themedBones = 0;
			int nativeBones = 0;
			BlockPos sentinel = null;
			for (BlockPos position
					: BlockPos.betweenClosed(
							bounds.minX(),
							bounds.minY(),
							bounds.minZ(),
							bounds.maxX(),
							bounds.maxY(),
							bounds.maxZ())) {
				BlockState state =
						level.getBlockState(
								position);
				if (state.is(CakeWorldBlocks
						.ROCK_CANDY_FOSSIL.get())) {
					themedBones++;
					axes.merge(state.getValue(
							RotatedPillarBlock
									.AXIS),
							1, Integer::sum);
					if (sentinel == null) {
						sentinel =
								position.immutable();
					}
				} else if (state.is(
						Blocks.BONE_BLOCK)) {
					nativeBones++;
					axes.merge(state.getValue(
							RotatedPillarBlock
									.AXIS),
							1, Integer::sum);
					if (sentinel == null) {
						sentinel =
								position.immutable();
					}
				}
			}
			require(helper, sentinel != null,
					"Natural Rock-Candy Fossil exposed no stable reload sentinel");
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BONE_BLOCK);
			BlockPos centre = bounds.getCenter();
			ResourceLocation biome =
					level.getBiome(centre)
							.unwrapKey()
							.map(key -> key
									.location())
							.orElse(null);
			boolean eligible =
					level.getBiome(centre).is(
							RockCandyFossilFeature
									.GENERATES_IN)
							&& level.getBiome(centre)
									.is(BiomeTags
											.HAS_NETHER_FOSSIL);
			LOGGER.info("Focused Rock-Candy Fossil audit: locate={}, startChunk={}, startBounds={}, pieceBounds={}, biome={}, template={}, expectedBones={}, themedBones={}, nativeBones={}, axes={}, sentinel={}, markerPhase={}",
					located, fossilStart.getChunkPos(),
					startBounds, bounds, biome, template,
					expectedBones, themedBones,
					nativeBones, axes, sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					Set.of(
							CakeWorldBiomes.FUDGE_WASTES
									.getId(),
							CakeWorldBiomes.TREACLE_SOUL_VALLEYS
									.getId(),
							CakeWorldBiomes.CHILLI_CHOCOLATE_CRAGS
									.getId(),
							CakeWorldBiomes.MOLTEN_MARSHMALLOW_CALDERAS
									.getId())
							.contains(biome)
							&& eligible,
					"Natural Rock-Candy Fossil left its intended Nether biome set or lost native/CakeWorld biome eligibility: biome="
							+ biome + ", eligible="
							+ eligible);
			require(helper,
					bounds.getXSpan() <= 7
							&& bounds.getYSpan()
									<= 7
							&& bounds.getZSpan()
									<= 7
							&& bounds.minY()
									> level
											.getChunkSource()
											.getGenerator()
											.getSeaLevel()
							&& bounds.maxY()
									< level
											.getMaxBuildHeight(),
					"Natural Rock-Candy Fossil lost its compact native envelope or above-sea-level air-pocket placement: "
							+ bounds);
			require(helper,
					themedBones + nativeBones
								== expectedBones
							&& nativeBones
									== (sentinelAlreadyNative
											? 1 : 0)
							&& themedBones
									== expectedBones
											- nativeBones
							&& axes.values()
									.stream()
									.mapToInt(
											Integer::intValue)
									.sum()
									== expectedBones,
					"Natural Rock-Candy Fossil lost its complete axis-aware palette or rewrote more than the explicit reload sentinel: expected="
							+ expectedBones
							+ ", themed="
							+ themedBones
							+ ", native="
							+ nativeBones
							+ ", axes=" + axes);
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BONE_BLOCK
								.defaultBlockState()
								.setValue(
										RotatedPillarBlock
												.AXIS,
										level.getBlockState(
												sentinel)
												.getValue(
														RotatedPillarBlock
																.AXIS)),
						2);
				require(helper,
						level.getBlockState(
								sentinel)
								.is(Blocks
										.BONE_BLOCK),
						"Could not seed the explicit player-placed Bone-Block reload sentinel");
			}
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					level.setChunkForced(
							chunkX, chunkZ,
							false);
				}
			}
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct018world",
			timeoutTicks = 12000)
	public static void focusedBurntToffeeFoundryStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Burnt-Toffee Foundry audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.NETHER);
		require(helper, level != null,
				"The fixed-seed server did not expose the Nether");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess().registryOrThrow(
						Registry
								.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						BurntToffeeFoundryFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Burnt-Toffee Foundry native configured structure was absent from the live registry");
		LocatedFoundry foundry =
				locateBurntToffeeFoundry(
						helper, level, configured,
						new BlockPos(0, 64, 0));
		setFoundryChunksForced(
				level, foundry, true);
		helper.runAfterDelay(360, () -> {
			FoundryWorldAudit audit =
					auditBurntToffeeFoundry(
							level, foundry);
			boolean sentinelAlreadyNative =
					isNativeBastionMasonry(
							level.getBlockState(
									audit.sentinel())
									.getBlock());
			int nativeMasonry =
					audit.palette().entrySet()
							.stream()
							.filter(entry ->
									isNativeBastionMasonry(
											entry.getKey()))
							.mapToInt(Map.Entry::getValue)
							.sum();
			int themedMasonry = List.of(
					CakeWorldBlocks
							.BURNT_SUGAR_ROCK.get(),
					CakeWorldBlocks
							.BURNT_TOFFEE_BRICKS.get(),
					CakeWorldBlocks
							.CRACKED_BURNT_TOFFEE_BRICKS
							.get(),
					CakeWorldBlocks
							.BURNT_TOFFEE_STAIRS.get(),
					CakeWorldBlocks
							.BURNT_TOFFEE_SLAB.get(),
					CakeWorldBlocks
							.BURNT_TOFFEE_WALL.get(),
					CakeWorldBlocks
							.STAMPED_BURNT_TOFFEE.get(),
					CakeWorldBlocks
							.GILDED_BURNT_TOFFEE.get(),
					CakeWorldBlocks
							.BURNT_TOFFEE_PILLAR.get())
							.stream()
							.mapToInt(block ->
									audit.palette()
											.getOrDefault(
													block, 0))
							.sum();
			LOGGER.info("Focused Burnt-Toffee Foundry audit: locate={}, startChunk={}, bounds={}, biome={}, pieces={}, templates={}, startFamily={}, palette={}, themedMasonry={}, nativeMasonry={}, loot={}, chests={}, goldBlocks={}, magmaCubeSpawners={}, fudgeFolk={}, fudgeBrutes={}, fudgeBoars={}, literalPiglins={}, literalBrutes={}, literalHoglins={}, sentinel={}, markerPhase={}",
					foundry.located(),
					foundry.startChunk(),
					foundry.bounds(),
					audit.biome(),
					foundry.start().getPieces()
							.size(),
					audit.templates(),
					audit.startFamily(),
					audit.palette(),
					themedMasonry,
					nativeMasonry,
					audit.loot(),
					audit.chests(),
					audit.palette()
							.getOrDefault(
									Blocks.GOLD_BLOCK,
									0),
					audit.magmaCubeSpawners(),
					audit.fudgeFolk(),
					audit.fudgeBrutes(),
					audit.fudgeBoars(),
					audit.literalPiglins(),
					audit.literalBrutes(),
					audit.literalHoglins(),
					audit.sentinel(),
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					(CakeWorldBiomes.FUDGE_WASTES
							.getId().equals(
									audit.biome())
							|| CakeWorldBiomes
									.BURNT_TOFFEE_DELTAS
									.getId().equals(
											audit.biome()))
							&& audit
									.literalEligible(),
					"Natural Burnt-Toffee Foundry left its two CakeWorld Nether homes or lost native/CakeWorld Bastion eligibility: biome="
							+ audit.biome()
							+ ", eligible="
							+ audit
									.literalEligible());
			require(helper,
					foundry.start().isValid()
							&& foundry.start()
									.getFeature()
									== configured
							&& foundry.start()
									.getPieces()
									.size() >= 8
							&& foundry.start()
									.getPieces()
									.stream()
									.allMatch(
											PoolElementStructurePiece
													.class
													::isInstance)
							&& audit.templates()
									.keySet().stream()
									.allMatch(name ->
											name.contains(
													"bastion/"))
							&& audit.startFamily()
									!= null
							&& foundry.start()
									.getPieces()
									.stream()
									.map(
											PoolElementStructurePiece
													.class
													::cast)
									.anyMatch(piece ->
											piece.getPosition()
													.getX()
													== foundry
															.startChunk()
															.getMinBlockX()
													&& piece
															.getPosition()
															.getZ()
															== foundry
																	.startChunk()
																	.getMinBlockZ()
													&& piece
															.getBoundingBox()
															.minY()
															+ piece
																	.getGroundLevelDelta()
															== 33
													&& isBastionStartTemplate(
															piece.getElement()
																	.toString())),
					"Natural Burnt-Toffee Foundry lost its saved native depth-six Jigsaw graph, start family or Y=33 anchor: pieces="
							+ foundry.start()
									.getPieces().size()
							+ ", family="
							+ audit.startFamily()
							+ ", templates="
							+ audit.templates()
							+ ", bounds="
							+ foundry.bounds());
			require(helper,
					themedMasonry > 100
							&& audit.palette()
									.getOrDefault(
											CakeWorldBlocks
													.BURNT_TOFFEE_BRICKS
													.get(),
											0) > 0
							&& audit.palette()
									.getOrDefault(
											CakeWorldBlocks
													.GILDED_BURNT_TOFFEE
													.get(),
											0) > 0
							&& nativeMasonry
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Burnt-Toffee Foundry lost its complete piece-bounded palette or rewrote more than the explicit reload sentinel: themed="
							+ themedMasonry
							+ ", native="
							+ nativeMasonry
							+ ", palette="
							+ audit.palette());
			Set<ResourceLocation> nativeLoot = Set.of(
					BuiltInLootTables.BASTION_BRIDGE,
					BuiltInLootTables
							.BASTION_HOGLIN_STABLE,
					BuiltInLootTables.BASTION_OTHER,
					BuiltInLootTables
							.BASTION_TREASURE);
			require(helper,
					audit.chests() > 0
							&& !audit.loot().isEmpty()
							&& audit.loot().stream()
									.allMatch(
											nativeLoot::contains)
							&& audit.palette()
									.getOrDefault(
											Blocks.GOLD_BLOCK,
											0) > 0
							&& (!"treasure".equals(
									audit.startFamily())
									|| audit
											.magmaCubeSpawners()
											== 1),
					"Natural Burnt-Toffee Foundry lost native chests/loot, Gold-Block progression or its treasure-room Magma-Cube spawner: loot="
							+ audit.loot()
							+ ", gold="
							+ audit.palette()
									.getOrDefault(
											Blocks.GOLD_BLOCK,
											0)
							+ ", spawners="
							+ audit
									.magmaCubeSpawners());
			require(helper,
					audit.literalPiglins() == 0
							&& audit.literalBrutes()
									== 0
							&& audit.literalHoglins()
									== 0,
					"Natural Burnt-Toffee Foundry retained literal Piglin/Hoglin residue instead of its existing Fudge roles: folk="
							+ audit.fudgeFolk()
							+ ", brutes="
							+ audit.fudgeBrutes()
							+ ", boars="
							+ audit.fudgeBoars()
							+ ", literalPiglins="
							+ audit.literalPiglins()
							+ ", literalBrutes="
							+ audit.literalBrutes()
							+ ", literalHoglins="
							+ audit.literalHoglins());
			if (!sentinelAlreadyNative) {
				level.setBlock(audit.sentinel(),
						Blocks
								.POLISHED_BLACKSTONE_BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks
										.POLISHED_BLACKSTONE_BRICKS),
						"Could not seed the explicit player-placed Polished-Blackstone-Bricks reload sentinel");
			}
			setFoundryChunksForced(
					level, foundry, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct019world",
			timeoutTicks = 7200)
	public static void focusedConfectionersCottageStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Confectioner's Cottage audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						ConfectionersCottageFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Confectioner's Cottage configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						ConfectionersCottageFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Confectioner's Cottage lost its public configured-structure locate tag");

		LocatedCottage cottage =
				locateConfectionersCottage(
						helper, level, configured,
						new BlockPos(96, 64, 128));
		setCottageChunksForced(level, cottage, true);
		helper.runAfterDelay(160, () -> {
			CottageShopWorldAudit audit =
					auditConfectionersCottage(
							level, cottage);
			BlockPos sentinel =
					cottage.centre().offset(
							new BlockPos(0, 9, 0)
									.rotate(
											audit.orientation()));
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			int nativeSentinels =
					audit.palette()
							.getOrDefault(
									Blocks.BRICKS, 0);
			LOGGER.info("Focused Confectioner's Cottage audit: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, residents={}, residentId={}, despawnDelay={}, offers={}, seedUses={}, markerConsumed={}, stockLoot={}, sentinel={}, markerPhase={}",
					cottage.located(),
					cottage.centre(),
					cottage.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.residents(),
					audit.residentId(),
					audit.despawnDelay(),
					audit.offers(),
					audit.seedUses(),
					audit.markerConsumed(),
					audit.stockLoot(),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					Set.of(
							CakeWorldBiomes
									.CANDY_PLAINS
									.getId(),
							CakeWorldBiomes
									.GINGERBREAD_HEARTHLANDS
									.getId())
							.contains(
									audit.biome()),
					"Natural Confectioner's Cottage left its settlement-biome boundary: biome="
							+ audit.biome());
			require(helper,
					cottage.bounds().getXSpan()
							== 17
							&& cottage.bounds()
									.getYSpan()
									== 13
							&& cottage.bounds()
									.getZSpan()
									== 17
							&& cottage.centre()
									.getY()
									>= level
											.getSeaLevel()
											- 4,
					"Natural Confectioner's Cottage lost its exact saved envelope or surface alignment: bounds="
							+ cottage.bounds()
							+ ", centre="
							+ cottage.centre()
							+ ", seaLevel="
							+ level.getSeaLevel());
			Map<Block, Integer> palette =
					audit.palette();
			int gummySign =
					palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK
									.get(), 0)
							+ palette.getOrDefault(
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get(), 0);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0)
							>= 150
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									>= 260
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING.get(),
									0) >= 109
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= 30
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									== 9
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_SPROUT
											.get(), 0)
									== 3
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CHOCOLATE_SPONGE
											.get(), 0)
									>= 3
							&& gummySign >= 9
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MIXING_BOWL
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOLING_RACK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_COOKER
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SODA_FOUNTAIN
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOKBOOK_KIOSK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 1
							&& nativeSentinels
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Confectioner's Cottage lost its complete bright shop, ingredient garden, working gadgets or player-edit reload boundary: "
							+ palette);
			require(helper,
					audit.residents() == 1
							&& audit
									.persistentResident()
							&& audit.despawnDelay()
									== 0
							&& audit.namedResident()
							&& audit.markerConsumed()
							&& audit.offers() == 5
							&& audit
									.earnFirstOffer()
							&& audit
									.starterSaleOffers()
							&& audit.seedUses()
									<= 1,
					"Natural Confectioner's Cottage lost its one permanent named shopkeeper, fixed earn-first starter trades or durable marker: "
							+ audit);
			require(helper,
					ConfectionersCottageFeature
							.LOOT_ID.toString()
							.equals(
									audit.stockLoot()),
					"Natural Confectioner's Cottage chest lost its dedicated starter-stock loot: "
							+ audit.stockLoot());

			net.minecraft.world.entity.Entity residentEntity =
					audit.residentId() == null ? null
							: level.getEntity(
									audit.residentId());
			TravellingConfectioner resident =
					residentEntity instanceof TravellingConfectioner
							? (TravellingConfectioner) residentEntity
							: null;
			require(helper, resident != null,
					"Natural Confectioner's Cottage resident vanished before reload-state proof");
			if (resident.getOffers().get(4)
					.getUses() == 0) {
				resident.getOffers().get(4)
						.increaseUses();
			}
			require(helper,
					resident.getOffers().get(4)
							.getUses() == 1,
					"Confectioner's Cottage seed-offer use did not settle at the reload sentinel value");
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel in the cottage roof");
			}
			setCottageChunksForced(level, cottage,
					false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct020world",
			timeoutTicks = 7200)
	public static void focusedWaferWindmillStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Wafer Windmill audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						WaferWindmillFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Wafer Windmill configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						WaferWindmillFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Wafer Windmill lost its public configured-structure locate tag");

		LocatedCottage windmill =
				locateWaferWindmill(
						helper, level, configured,
						new BlockPos(96, 64, 128));
		setCottageChunksForced(level, windmill, true);
		helper.runAfterDelay(160, () -> {
			WindmillWorldAudit audit =
					auditWaferWindmill(
							level, windmill);
			BlockPos sentinel =
					WaferWindmillFeature
							.reloadSentinelPosition(
									level.getSeed(),
									windmill.centre());
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			int nativeSentinels =
					audit.palette()
							.getOrDefault(
									Blocks.BRICKS, 0);
			LOGGER.info("Focused Wafer Windmill audit: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, hub={}, powered={}, neighborSignal={}, pantryLoot={}, sentinel={}, markerPhase={}",
					windmill.located(),
					windmill.centre(),
					windmill.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.hub(),
					audit.powered(),
					audit.neighborSignal(),
					audit.pantryLoot(),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					Set.of(
							CakeWorldBiomes
									.CANDY_PLAINS
									.getId(),
							CakeWorldBiomes
									.GINGERBREAD_HEARTHLANDS
									.getId(),
							CakeWorldBiomes
									.WAFFLE_PLATEAUS
									.getId())
							.contains(
									audit.biome()),
					"Natural Wafer Windmill left its settlement-biome boundary: biome="
							+ audit.biome());
			require(helper,
					windmill.bounds().getXSpan()
							== 21
							&& windmill.bounds()
									.getYSpan()
									== 20
							&& windmill.bounds()
									.getZSpan()
									== 21
							&& windmill.centre()
									.getY()
									>= level
											.getSeaLevel()
											- 4,
					"Natural Wafer Windmill lost its exact saved envelope or surface alignment: bounds="
							+ windmill.bounds()
							+ ", centre="
							+ windmill.centre()
							+ ", seaLevel="
							+ level.getSeaLevel());
			Map<Block, Integer> palette =
					audit.palette();
			int gummyTips =
					palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK
									.get(), 0)
							+ palette.getOrDefault(
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get(), 0);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get(), 0)
							>= 300
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									>= 300
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING.get(),
									0) >= (sentinelAlreadyNative
											? 99 : 100)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= 70
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									== 16
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SYRUP_PIPE
											.get(), 0)
									>= 18
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_WINDMILL
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									Blocks.REDSTONE_BLOCK,
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.OVEN.get(),
									0) == 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MIXING_BOWL
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_COOKER
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOLING_RACK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SODA_FOUNTAIN
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOKBOOK_KIOSK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW
											.get(), 0)
									== 15
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_SPROUT
											.get(), 0)
									== 14
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 1
							&& gummyTips == 4
							&& nativeSentinels
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Wafer Windmill lost its tower, giant sails, drive line, workshop, landing, crop plot or player-edit reload boundary: "
							+ palette);
			require(helper,
					audit.hub()
							&& audit.powered()
							&& audit.neighborSignal()
							&& audit.facing()
									== audit
											.orientation()
											.rotate(
													Direction.SOUTH)
							&& !audit.signalSource()
							&& audit.signal() == 0,
					"Natural Wafer Windmill lost its live visible hub or gained an automation-output contract: "
							+ audit);
			require(helper,
					WaferWindmillFeature.LOOT_ID
							.toString().equals(
									audit.pantryLoot()),
					"Natural Wafer Windmill pantry lost its dedicated loot: "
							+ audit.pantryLoot());
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel in the Wafer Windmill roof");
			}
			setCottageChunksForced(
					level, windmill, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct021world",
			timeoutTicks = 7200)
	public static void focusedCandyCaneBridgeStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Candy-Cane Bridge audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel();
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						CandyCaneBridgeFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Candy-Cane Bridge configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						CandyCaneBridgeFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Candy-Cane Bridge lost its public configured-structure locate tag");

		LocatedCottage bridge =
				locateCandyCaneBridge(
						helper, level, configured,
						new BlockPos(96, 64, 128));
		setCottageChunksForced(level, bridge, true);
		helper.runAfterDelay(160, () -> {
			BridgeWorldAudit audit =
					auditCandyCaneBridge(
							level, bridge);
			BlockPos sentinel =
					CandyCaneBridgeFeature
							.reloadSentinelPosition(
									level.getSeed(),
									bridge.centre());
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			int nativeSentinels =
					audit.palette()
							.getOrDefault(
									Blocks.BRICKS, 0);
			LOGGER.info("Focused Candy-Cane Bridge audit: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, channel={}, deck={}, clearance={}, axes={}, approaches={}, stairs={}, sentinel={}, markerPhase={}",
					bridge.located(),
					bridge.centre(),
					bridge.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.channel(),
					audit.deck(),
					audit.clearance(),
					audit.axes(),
					audit.approaches(),
					audit.stairs(),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					Set.of(
							CakeWorldBiomes
									.CANDY_PLAINS
									.getId(),
							CakeWorldBiomes
									.GINGERBREAD_HEARTHLANDS
									.getId())
							.contains(
									audit.biome()),
					"Natural Candy-Cane Bridge left its settlement-biome boundary: biome="
							+ audit.biome());
			require(helper,
					bridge.bounds().getXSpan()
							== 33
							&& bridge.bounds()
									.getYSpan()
									== 10
							&& bridge.bounds()
									.getZSpan()
									== 33
							&& bridge.centre()
									.getY()
									>= level
											.getSeaLevel()
											- 8,
					"Natural Candy-Cane Bridge lost its exact saved envelope or surface alignment: bounds="
							+ bridge.bounds()
							+ ", centre="
							+ bridge.centre()
							+ ", seaLevel="
							+ level.getSeaLevel());
			Map<Block, Integer> palette =
					audit.palette();
			int gummyCaps =
					palette.getOrDefault(
							CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK
									.get(), 0)
							+ palette.getOrDefault(
									CakeWorldBlocks
											.BLUEBERRY_GUMMY_BLOCK
											.get(), 0);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.BISCUIT_STONE
									.get(), 0)
							>= 480
							&& palette.getOrDefault(
									CakeWorldFluids
											.LEMONADE_BLOCK
											.get(), 0)
									>= 450
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_CRUMBS
											.get(), 0)
									>= 60
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									>= 205
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_STAIRS
											.get(), 0)
									>= 40
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= (sentinelAlreadyNative
											? 99 : 100)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW
											.get(), 0)
									>= 8
							&& palette.getOrDefault(
									Blocks.LANTERN, 0)
									>= 4
							&& gummyCaps >= 4
							&& nativeSentinels
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Candy-Cane Bridge lost its road, real Lemonade cutting, deck, truss, rescue points or player-edit reload boundary: "
							+ palette);
			require(helper,
					audit.channel()
							&& audit.deck()
							&& audit.clearance()
							&& audit.axes()
							&& audit.approaches()
							&& audit.stairs()
							&& (sentinelAlreadyNative
									|| audit
											.nativeSentinel()),
					"Natural Candy-Cane Bridge stopped being a readable, axis-correct, safely traversable crossing: "
							+ audit);
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel in the Candy-Cane Bridge rail");
			}
			setCottageChunksForced(
					level, bridge, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct022world",
			timeoutTicks = 12000)
	public static void focusedCraterKitchenStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Crater Kitchen audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.END);
		require(helper, level != null,
				"The fixed-seed server did not expose the End");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						CraterKitchenFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Crater Kitchen configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						CraterKitchenFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Crater Kitchen lost its public configured-structure locate tag");

		LocatedCottage kitchen =
				locateCraterKitchen(
						helper, level, configured,
						new BlockPos(1024, 96,
								1024));
		setCottageChunksForced(level, kitchen, true);
		helper.runAfterDelay(200, () -> {
			CraterKitchenWorldAudit audit =
					auditCraterKitchen(
							level, kitchen);
			BlockPos sentinel =
					CraterKitchenFeature
							.reloadSentinelPosition(
									level.getSeed(),
									kitchen.centre());
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			int nativeSentinels =
					audit.palette()
							.getOrDefault(
									Blocks.BRICKS, 0);
			LOGGER.info("Focused Crater Kitchen audit: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, floor={}, bowl={}, entrance={}, stairs={}, safetyPads={}, stations={}, cacheLoot={}, sentinel={}, markerPhase={}",
					kitchen.located(),
					kitchen.centre(),
					kitchen.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.floor(),
					audit.bowl(),
					audit.entrance(),
					audit.stairs(),
					audit.safetyPads(),
					audit.stations(),
					audit.cacheLoot(),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					CakeWorldBiomes
							.MOONCAKE_BARRENS
							.getId().equals(
									audit.biome()),
					"Natural Crater Kitchen left Mooncake Barrens: biome="
							+ audit.biome());
			require(helper,
					kitchen.bounds().getXSpan()
							== 33
							&& kitchen.bounds()
									.getYSpan()
									== 16
							&& kitchen.bounds()
									.getZSpan()
									== 33
							&& kitchen.centre()
									.getY() >= 40,
					"Natural Crater Kitchen lost its exact saved envelope or supported End-island height: bounds="
							+ kitchen.bounds()
							+ ", centre="
							+ kitchen.centre());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.MOONCAKE_CRUST
									.get(), 0)
							>= 900
							&& palette.getOrDefault(
							CakeWorldBlocks
									.MERINGUE_BRICKS
									.get(), 0)
							>= 80
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MACARON_BRICKS
											.get(), 0)
									>= (sentinelAlreadyNative
											? 89 : 90)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MACARON_PILLAR
											.get(), 0)
									== 9
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_CRUMBS
											.get(), 0)
									>= 19
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_STAIRS
											.get(), 0)
									== 18
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(), 0)
									== 9
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_SLAB
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW
											.get(), 0)
									>= 8
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ROCK_CANDY
											.get(), 0)
									>= 7
							&& palette.getOrDefault(
									Blocks.END_ROD, 0)
									== 3
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 1
							&& nativeSentinels
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Crater Kitchen lost its bowl, ancient crumbs, ruin, lunar dial, route, rescue pads or player-edit reload boundary: "
							+ palette);
			require(helper,
					audit.floor()
							&& audit.bowl()
							&& audit.entrance()
							&& audit.stairs()
							&& audit.safetyPads()
							&& audit.stations()
							&& (sentinelAlreadyNative
									|| audit
											.nativeSentinel())
							&& CraterKitchenFeature
									.LOOT_ID.toString()
									.equals(
											audit.cacheLoot()),
					"Natural Crater Kitchen stopped being a safe, readable and functional recipe ruin: "
							+ audit);
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel in the Crater Kitchen rim");
			}
			setCottageChunksForced(
					level, kitchen, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct023world",
			timeoutTicks = 12000)
	public static void focusedRockCandyCrystalMineStructureAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed Rock-Candy Crystal Mine audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		Registry<ConfiguredStructureFeature<?, ?>>
				structures =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured =
				structures.get(
						RockCandyCrystalMineFeature
								.STRUCTURE_ID);
		require(helper, configured != null,
				"Rock-Candy Crystal Mine configured structure was absent from the live registry");
		boolean locatedTag = structures.getTag(
						RockCandyCrystalMineFeature
								.STRUCTURE_TAG)
				.map(tag -> tag.stream().anyMatch(
						holder -> holder.value()
								== configured))
				.orElse(false);
		require(helper, locatedTag,
				"Rock-Candy Crystal Mine lost its public configured-structure locate tag");

		LocatedCottage mine =
				locateRockCandyCrystalMine(
						helper, level, configured,
						new BlockPos(1024, 96,
								1024));
		setCottageChunksForced(level, mine, true);
		helper.runAfterDelay(200, () -> {
			CrystalMineWorldAudit audit =
					auditRockCandyCrystalMine(
							level, mine);
			BlockPos sentinel =
					RockCandyCrystalMineFeature
							.reloadSentinelPosition(
									level.getSeed(),
									mine.centre());
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			int nativeSentinels =
					audit.palette()
							.getOrDefault(
									Blocks.BRICKS, 0);
			LOGGER.info("Focused Rock-Candy Crystal Mine audit: locate={}, centre={}, bounds={}, biome={}, orientation={}, palette={}, surfaceAccess={}, shaft={}, safety={}, hostFamilies={}, patterns={}, headframeTop={}, cacheLoot={}, sentinel={}, markerPhase={}",
					mine.located(),
					mine.centre(),
					mine.bounds(),
					audit.biome(),
					audit.orientation(),
					audit.palette(),
					audit.surfaceAccess(),
					audit.shaft(),
					audit.safety(),
					audit.hostFamilies(),
					audit.patterns(),
					audit.headframeTop(),
					audit.cacheLoot(),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded");
			require(helper,
					CakeWorldBiomes
							.CANDY_CANE_BADLANDS
							.getId().equals(
									audit.biome()),
					"Natural Rock-Candy Crystal Mine left its Candy-Cane-Badlands home: biome="
							+ audit.biome());
			require(helper,
					mine.bounds().getXSpan()
							== 33
							&& mine.bounds()
									.getYSpan()
									== 49
							&& mine.bounds()
									.getZSpan()
									== 33
							&& mine.centre().getY()
									>= level
											.getMinBuildHeight()
											+ 4,
					"Natural Rock-Candy Crystal Mine lost its exact saved envelope or safe build height: bounds="
							+ mine.bounds()
							+ ", centre="
							+ mine.centre());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.WAFER_BLOCK
									.get(), 0)
							>= (sentinelAlreadyNative
									? 549 : 550)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0)
									>= 99
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MARSHMALLOW
											.get(), 0)
									>= 14
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_GLASS
											.get(), 0)
									>= 13
							&& palette.getOrDefault(
									Blocks.LADDER, 0)
									== 35
							&& palette.getOrDefault(
									Blocks.LANTERN, 0)
									== 8
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING.get(),
									0) >= 49
							&& palette.getOrDefault(
									CakeWorldBlocks
											.WAFER_STAIRS
											.get(), 0)
									== 5
							&& palette.getOrDefault(
									Blocks.CHEST, 0)
									== 1
							&& nativeSentinels
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Rock-Candy Crystal Mine lost its gallery, access shaft, headframe, rescue floor, lighting, cache or player-edit reload boundary: "
							+ palette);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.COCOA_CLOUD.get(),
							0) >= 9
							&& palette.getOrDefault(
									CakeWorldBlocks
											.LIQUORICE_VEIN
											.get(), 0)
									>= 6
							&& palette.getOrDefault(
									CakeWorldBlocks
											.MINT_CRYSTAL
											.get(), 0)
									>= 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ROCK_CANDY_DEPOSIT
											.get(), 0)
									>= 12
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ROCK_CANDY_DIAMOND
											.get(), 0)
									>= 1
							&& palette.getOrDefault(
									CakeWorldBlocks
											.SPRINKLE_CLUSTER
											.get(), 0)
									>= 5
							&& palette.getOrDefault(
									CakeWorldBlocks
											.RICH_SPRINKLE_CLUSTER
											.get(), 0)
									>= 1,
					"Natural Rock-Candy Crystal Mine lost an authored pattern exhibit: "
							+ palette);
			require(helper,
					audit.surfaceAccess()
							&& audit.shaft()
							&& audit.safety()
							&& audit.hostFamilies()
							&& audit.patterns()
							&& audit.headframeTop()
									>= mine.centre()
											.getY() + 40
							&& (sentinelAlreadyNative
									|| audit
											.nativeSentinel())
							&& RockCandyCrystalMineFeature
									.LOOT_ID.toString()
									.equals(
											audit.cacheLoot()),
					"Natural Rock-Candy Crystal Mine stopped being a visible, recoverable and truthful geology gallery: "
							+ audit);
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel in the Crystal Mine headframe");
			}
			setCottageChunksForced(
					level, mine, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct024world",
			timeoutTicks = 16000)
	public static void focusedNaturalCookbookKioskAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Cookbook Kiosk audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		LocatedPicnic picnic = locateNaturalPicnic(
				helper, level,
				new BlockPos(280, 96, 440), 12);
		LocatedCottage forcedBounds =
				new LocatedCottage(
						picnic.kiosk(),
						picnic.centre(),
						new BoundingBox(
								picnic.centre()
										.getX() - 4,
								picnic.centre()
										.getY(),
								picnic.centre()
										.getZ() - 4,
								picnic.centre()
										.getX() + 4,
								picnic.centre()
										.getY() + 4,
								picnic.centre()
										.getZ() + 4));
		setCottageChunksForced(
				level, forcedBounds, true);
		helper.runAfterDelay(100, () -> {
			PicnicWorldAudit audit =
					auditNaturalPicnic(
							level,
							picnic.centre());
			BlockPos sentinel =
					picnic.centre()
							.offset(4, 0, 4);
			boolean sentinelAlreadyNative =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			LOGGER.info("Focused natural Cookbook Kiosk audit: centre={}, kiosk={}, biome={}, palette={}, companions={}, homeRestricted={}, sentinel={}, markerPhase={}, scannedChunks={}, kioskCandidates={}",
					picnic.centre(),
					picnic.kiosk(),
					audit.biome(),
					audit.palette(),
					audit.persistentCompanions(),
					audit.homeRestricted(),
					sentinel,
					sentinelAlreadyNative
							? "reloaded"
							: "seeded",
					picnic.scannedChunks(),
					picnic.kioskCandidates());
			require(helper,
					CakeWorldBiomes.CANDY_PLAINS
							.getId().equals(
									audit.biome()),
					"Natural Cookbook Kiosk left the current Candy-Plains starter-biome boundary: biome="
							+ audit.biome());
			Map<Block, Integer> palette =
					audit.palette();
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks
									.CHOCOLATE_SPONGE
									.get(), 0)
							== (sentinelAlreadyNative
									? 34 : 35)
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_STONE
											.get(), 0)
									== 61
							&& palette.getOrDefault(
									CakeWorldBlocks
											.BISCUIT_CRUMBS
											.get(), 0)
									== 5
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING.get(),
									0) == 18
							&& palette.getOrDefault(
									CakeWorldBlocks
											.ICING_LAYER
											.get(), 0)
									== 5
							&& palette.getOrDefault(
									CakeWorldBlocks
											.COOKBOOK_KIOSK
											.get(), 0)
									== 1
							&& palette.getOrDefault(
									Blocks.BRICKS, 0)
									== (sentinelAlreadyNative
											? 1 : 0),
					"Natural Cookbook Kiosk did not belong to the exact sparse Picnic-Hamlet layout or lost the player-edit reload boundary: "
							+ palette);
			require(helper,
					audit.readableLayout()
							&& audit
									.persistentCompanions()
									>= 1,
					"Natural Cookbook Kiosk lost its path, two shelters, four soft seats or persistent companion: "
							+ audit);
			if (!sentinelAlreadyNative) {
				level.setBlock(sentinel,
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel in the Picnic-Hamlet border");
			}
			setCottageChunksForced(
					level, forcedBounds, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "struct025world",
			timeoutTicks = 24000)
	public static void focusedNaturalRoadsideCuriosityAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Roadside Curiosity audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		LocatedRoadside cart =
				locateNaturalRoadside(
						helper, level,
						RoadsideCuriosityFeature.Variant
								.SPILLED_SWEET_CART,
						new BlockPos(
								280, 96, 440),
						new BlockPos(
								378, 69, 534),
						12);
		LocatedRoadside signpost =
				locateNaturalRoadside(
						helper, level,
						RoadsideCuriosityFeature.Variant
								.WRONG_WAY_SIGNPOST,
						new BlockPos(
								148, 96, 20),
						new BlockPos(
								56, 65, 36),
						12);
		LocatedRoadside shelter =
				locateNaturalRoadside(
						helper, level,
						RoadsideCuriosityFeature.Variant
								.MARSHMALLOW_RESCUE_SHELTER,
						new BlockPos(
								-3184, 192,
								-1152),
						new BlockPos(
								-3049, 164,
								-1066),
						12);
		List<LocatedRoadside> scenes =
				List.of(cart, signpost, shelter);
		scenes.forEach(scene ->
				setRoadsideChunksForced(
						level, scene, true));
		helper.runAfterDelay(40, () -> {
			List<RoadsideWorldAudit> audits =
					scenes.stream()
							.map(scene ->
									auditNaturalRoadside(
											level,
											scene))
							.toList();
			long brickSentinels =
					audits.stream()
							.filter(
									RoadsideWorldAudit
											::brickSentinel)
							.count();
			require(helper,
					brickSentinels == 0
							|| brickSentinels
									== audits.size(),
					"Natural Roadside Curiosity world contained a partial fresh/reload sentinel set: "
							+ audits);
			boolean reloaded =
					brickSentinels == audits.size();
			for (int index = 0;
					index < scenes.size(); index++) {
				LocatedRoadside scene =
						scenes.get(index);
				RoadsideWorldAudit audit =
						audits.get(index);
				ResourceLocation expectedBiome =
						switch (scene.variant()) {
						case SPILLED_SWEET_CART ->
							CakeWorldBiomes
									.CANDY_PLAINS
									.getId();
						case WRONG_WAY_SIGNPOST ->
							CakeWorldBiomes
									.COOKIE_FOREST
									.getId();
						case MARSHMALLOW_RESCUE_SHELTER ->
							CakeWorldBiomes
									.MARSHMALLOW_PEAKS
									.getId();
						};
				LOGGER.info("Focused natural Roadside Curiosity audit: variant={}, centre={}, cache={}, biome={}, rotation={}, palette={}, layout={}, sentinel={}, markerPhase={}, scannedChunks={}, cacheCandidates={}",
						scene.variant(),
						scene.centre(),
						scene.cache(),
						audit.biome(),
						audit.rotation(),
						audit.palette(),
						audit.readableLayout(),
						audit.sentinel(),
						reloaded
								? "reloaded"
								: "seeded",
						scene.scannedChunks(),
						scene.cacheCandidates());
				require(helper,
						expectedBiome.equals(
								audit.biome())
								&& audit
										.readableLayout()
								&& RoadsideCuriosityFeature
										.LOOT_ID
										.toString()
										.equals(
												audit.cacheLoot()),
						"Natural Roadside Curiosity lost its exact biome story, readable scene or shared provisions cache: "
								+ audit);
				if (!reloaded) {
					level.setBlock(
							audit.sentinel(),
							Blocks.BRICKS
									.defaultBlockState(),
							2);
					require(helper,
							level.getBlockState(
									audit.sentinel())
									.is(Blocks.BRICKS),
							"Could not seed the explicit player-placed Brick reload sentinel in "
									+ scene.variant());
				}
			}
			scenes.forEach(scene ->
					setRoadsideChunksForced(
							level, scene, false));
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "bioow003world",
			timeoutTicks = 24000)
	public static void focusedNaturalCookieCrumbGroveAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Cookie Crumb Grove audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos cookieForest = locateBiome(helper, level,
				CakeWorldBiomes.COOKIE_FOREST.getId());
		LocatedCookieGrove grove = locateNaturalCookieGrove(
				helper, level, cookieForest, 20);
		setCookieGroveChunksForced(level, grove, true);
		helper.runAfterDelay(40, () -> {
			CookieGroveWorldAudit audit =
					auditNaturalCookieGrove(
							level, grove);
			LOGGER.info("Focused natural Cookie Crumb Grove audit: centre={}, cache={}, biome={}, rotation={}, palette={}, layout={}, sentinel={}, markerPhase={}, scannedChunks={}, cacheCandidates={}",
					grove.centre(),
					grove.cache(),
					audit.biome(),
					audit.rotation(),
					audit.palette(),
					audit.readableLayout(),
					audit.sentinel(),
					audit.brickSentinel()
							? "reloaded"
							: "seeded",
					grove.scannedChunks(),
					grove.cacheCandidates());
			require(helper,
					CakeWorldBiomes.COOKIE_FOREST.getId()
							.equals(audit.biome())
							&& audit.readableLayout()
							&& CookieCrumbGroveFeature
									.LOOT_ID
									.toString()
									.equals(
											audit.cacheLoot()),
					"Natural Cookie Crumb Grove lost its exact biome, four-tree scene, accessible burrow or biscuit cache: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel beside the Cookie Crumb Grove");
			}
			setCookieGroveChunksForced(
					level, grove, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "bioow004world",
			timeoutTicks = 24000)
	public static void focusedNaturalPeppermintClearingAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Peppermint Clearing audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos pinewoods = locateBiome(helper, level,
				CakeWorldBiomes.PEPPERMINT_PINEWOODS.getId());
		LocatedPeppermintClearing clearing =
				locateNaturalPeppermintClearing(
						helper, level, pinewoods, 20);
		setPeppermintClearingChunksForced(
				level, clearing, true);
		helper.runAfterDelay(40, () -> {
			PeppermintClearingWorldAudit audit =
					auditNaturalPeppermintClearing(
							level, clearing);
			LOGGER.info("Focused natural Peppermint Clearing audit: centre={}, biome={}, rotation={}, palette={}, layout={}, sentinel={}, markerPhase={}, scannedChunks={}, crystalCandidates={}",
					clearing.centre(),
					audit.biome(),
					audit.rotation(),
					audit.palette(),
					audit.readableLayout(),
					audit.sentinel(),
					audit.brickSentinel()
							? "reloaded"
							: "seeded",
					clearing.scannedChunks(),
					clearing.crystalCandidates());
			require(helper,
					CakeWorldBiomes
							.PEPPERMINT_PINEWOODS
							.getId()
							.equals(audit.biome())
							&& audit.readableLayout(),
					"Natural Peppermint Clearing lost its exact Pinewoods biome, three striped pines, frosted clearing or chime post: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel beside the Peppermint Clearing");
			}
			setPeppermintClearingChunksForced(
					level, clearing, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "bioow005world",
			timeoutTicks = 24000)
	public static void focusedNaturalGummyJungleBounceGroveAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Gummy Jungle Bounce Grove audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos gummyJungle = locateBiome(helper, level,
				CakeWorldBiomes.GUMMY_JUNGLE.getId());
		LocatedGummyGrove grove =
				locateNaturalGummyGrove(
						helper, level, gummyJungle, 20);
		setGummyGroveChunksForced(level, grove, true);
		helper.runAfterDelay(40, () -> {
			GummyGroveWorldAudit audit =
					auditNaturalGummyGrove(
							level, grove);
			LOGGER.info("Focused natural Gummy Jungle Bounce Grove audit: centre={}, biome={}, rotation={}, palette={}, layout={}, sentinel={}, markerPhase={}, scannedChunks={}, beaconCandidates={}",
					grove.centre(),
					audit.biome(),
					audit.rotation(),
					audit.palette(),
					audit.readableLayout(),
					audit.sentinel(),
					audit.brickSentinel()
							? "reloaded"
							: "seeded",
					grove.scannedChunks(),
					grove.beaconCandidates());
			require(helper,
					CakeWorldBiomes.GUMMY_JUNGLE
							.getId()
							.equals(audit.biome())
							&& audit.readableLayout(),
					"Natural Gummy Jungle Bounce Grove lost its exact biome, three lollipop trees, flavour pools, elastic vines, sprouts or bubble beacon: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS
								.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel beside the Gummy Jungle Bounce Grove");
			}
			setGummyGroveChunksForced(
					level, grove, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "bioow006world",
			timeoutTicks = 24000)
	public static void focusedNaturalCaramelBogMangroveAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Caramel Bog Mangrove audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos caramelBogs = locateBiome(helper, level,
				CakeWorldBiomes.CARAMEL_BOGS.getId());
		LocatedCaramelMangrove mangrove =
				locateNaturalCaramelMangrove(
						helper, level, caramelBogs, 20);
		setCaramelMangroveChunksForced(
				level, mangrove, true);
		helper.runAfterDelay(40, () -> {
			CaramelMangroveWorldAudit audit =
					auditNaturalCaramelMangrove(
							level, mangrove);
			LOGGER.info("Focused natural Caramel Bog Mangrove audit: centre={}, biome={}, rotation={}, palette={}, layout={}, sentinel={}, markerPhase={}, scannedChunks={}, candidateCentres={}",
					mangrove.centre(),
					audit.biome(),
					audit.rotation(),
					audit.palette(),
					audit.readableLayout(),
					audit.sentinel(),
					audit.brickSentinel()
							? "reloaded"
							: "seeded",
					mangrove.scannedChunks(),
					mangrove.candidateCentres());
			require(helper,
					CakeWorldBiomes.CARAMEL_BOGS
							.getId()
							.equals(audit.biome())
							&& audit.readableLayout(),
					"Natural Caramel Bog Mangrove lost its exact biome, three rooted trees, contained pool, reeds or Wafer recovery route: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel beside the Caramel Bog Mangrove");
			}
			setCaramelMangroveChunksForced(
					level, mangrove, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "bioow007world",
			timeoutTicks = 24000)
	public static void focusedNaturalSherbetFossilBowlAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Sherbet Fossil Bowl audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos sherbetDunes = locateBiome(helper, level,
				CakeWorldBiomes.SHERBET_DUNES.getId());
		LocatedSherbetFossilBowl bowl =
				locateNaturalSherbetFossilBowl(
						helper, level, sherbetDunes, 24);
		setSherbetFossilBowlChunksForced(
				level, bowl, true);
		helper.runAfterDelay(40, () -> {
			SherbetFossilBowlWorldAudit audit =
					auditNaturalSherbetFossilBowl(
							level, bowl);
			LOGGER.info("Focused natural Sherbet Fossil Bowl audit: centre={}, jar={}, biome={}, rotation={}, palette={}, layout={}, loot={}, customName={}, sentinel={}, markerPhase={}, scannedChunks={}, jarCandidates={}, sherbetColumns={}",
					bowl.centre(),
					bowl.jar(),
					audit.biome(),
					audit.rotation(),
					audit.palette(),
					audit.readableLayout(),
					audit.jarLoot(),
					audit.customName(),
					audit.sentinel(),
					audit.brickSentinel()
							? "reloaded"
							: "seeded",
					bowl.scannedChunks(),
					bowl.jarCandidates(),
					bowl.sherbetColumns());
			require(helper,
					CakeWorldBiomes.SHERBET_DUNES
							.getId()
							.equals(audit.biome())
							&& audit.readableLayout()
							&& SherbetFossilBowlFeature
									.LOOT_ID.toString()
									.equals(audit.jarLoot())
							&& audit.customName().contains(
									"buried_sherbet_jar"),
					"Natural Sherbet Fossil Bowl lost its exact Dunes biome, stripes, fossil, Fizzy Pearls or buried discovery jar: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel beside the Sherbet Fossil Bowl");
			}
			setSherbetFossilBowlChunksForced(
					level, bowl, false);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, batch = "bioow008world",
			timeoutTicks = 24000)
	public static void focusedNaturalCandyCaneHoodooGardenAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			LOGGER.info("Skipping opt-in fixed-seed natural Candy-Cane Hoodoo Garden audit; run with -PcakeworldFreshWorldgenRuntime=true to execute it");
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos badlands = locateBiome(helper, level,
				CakeWorldBiomes.CANDY_CANE_BADLANDS.getId());
		CandyCaneBadlandsGeologySurvey geology =
				surveyCandyCaneBadlandsGeology(
						level, badlands, 8);
		Set<ResourceLocation> allowedGeomes = Set.of(
				id("wafer_shelf"),
				id("peppermint_fold"),
				id("rock_candy_uplift"),
				id("fudge_mantle"));
		require(helper,
				geology.biomeChunks() > 0
						&& !geology.geomes().isEmpty()
						&& geology.geomes().keySet().stream()
								.allMatch(allowedGeomes::contains)
						&& geology.geomes().getOrDefault(
								id("rock_candy_uplift"), 0) > 0
						&& geology.naturalRocks().size() >= 2,
				"Natural Candy-Cane Badlands did not stay within its allowed geomes while exposing multiple real subsurface rock families: "
						+ geology);
		LocatedCandyCaneHoodooGarden garden =
				locateNaturalCandyCaneHoodooGarden(
						helper, level, badlands, 24);
		setCandyCaneHoodooGardenChunksForced(
				level, garden, true);
		helper.runAfterDelay(40, () -> {
			CandyCaneHoodooGardenWorldAudit audit =
					auditNaturalCandyCaneHoodooGarden(
							level, garden);
			LOGGER.info("Focused natural Candy-Cane Hoodoo Garden audit: centre={}, biome={}, rotation={}, palette={}, layout={}, blockEntities={}, sentinel={}, markerPhase={}, scannedChunks={}, markerCandidates={}, badlandsColumns={}, geomes={}, naturalRocks={}, sampledRockCells={}",
					garden.centre(),
					audit.biome(),
					audit.rotation(),
					describe(audit.palette()),
					audit.readableLayout(),
					audit.blockEntities(),
					audit.sentinel(),
					audit.brickSentinel()
							? "reloaded"
							: "seeded",
					garden.scannedChunks(),
					garden.markerCandidates(),
					garden.badlandsColumns(),
					geology.geomes(),
					describe(geology.naturalRocks()),
					geology.sampledRockCells());
			require(helper,
					CakeWorldBiomes.CANDY_CANE_BADLANDS
							.getId()
							.equals(audit.biome())
							&& audit.readableLayout()
							&& audit.blockEntities() == 0,
					"Natural Candy-Cane Hoodoo Garden lost its exact Badlands biome, four striped hoodoos, geology court, recovery pads or entity-free contract: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(
								audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the explicit player-placed Brick reload sentinel beside the Candy-Cane Hoodoo Garden");
			}
			setCandyCaneHoodooGardenChunksForced(
					level, garden, false);
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
		ServerLevel endLevel = helper.getLevel().getServer().getLevel(Level.END);
		BlockPos starlight = locateBiome(helper, endLevel,
				id("starlight_sugar_fields"));
		ChunkPos starlightChunk = new ChunkPos(starlight);
		Map<Block, Integer> end = scanDimension(
				endLevel,
				Set.of(CakeWorldBlocks.STARLIGHT_STARSTEEL.get()),
				starlightChunk.x, starlightChunk.z, 4, 0, 112);
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

	private static Map<ResourceLocation, Integer>
			countSampledGeomesForBiome(
					ServerLevel level, BlockPos center,
					ResourceLocation targetBiome,
					int chunkRadius, int chunkStep,
					int sampleY) {
		GeologySampler sampler =
				OreSpawnApi.createSampler(level).orElseThrow();
		Map<ResourceLocation, Integer> result =
				new LinkedHashMap<>();
		int centerChunkX = Math.floorDiv(center.getX(), 16);
		int centerChunkZ = Math.floorDiv(center.getZ(), 16);
		for (int chunkX = centerChunkX - chunkRadius;
				chunkX <= centerChunkX + chunkRadius;
				chunkX += chunkStep) {
			for (int chunkZ = centerChunkZ - chunkRadius;
					chunkZ <= centerChunkZ + chunkRadius;
					chunkZ += chunkStep) {
				GeologyColumn column = sampler.sampleColumn(
						(chunkX << 4) + 8,
						(chunkZ << 4) + 8, sampleY);
				if (targetBiome.equals(column.biome())) {
					result.merge(column.geome(), 1,
							Integer::sum);
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

	private static GeomePlacementSurvey mergeGeomePlacementSurveys(
			GeomePlacementSurvey... surveys) {
		Map<ResourceLocation, Integer> chunks =
				new LinkedHashMap<>();
		Map<ResourceLocation, Integer> blocks =
				new LinkedHashMap<>();
		for (GeomePlacementSurvey survey : surveys) {
			survey.chunksByGeome().forEach((geome, count) ->
					chunks.merge(geome, count, Integer::sum));
			survey.blocksByGeome().forEach((geome, count) ->
					blocks.merge(geome, count, Integer::sum));
		}
		return new GeomePlacementSurvey(chunks, blocks);
	}

	private static HostAttributionResult auditPredictedExplicitOreHosts(
			ServerLevel level,
			Map<Block, Set<Block>> allowedBlocks, int centerChunkX,
			int centerChunkZ, int radius, int requestedMinY,
			int requestedMaxY) {
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
		// OreSpawn 4.0.11 learns dynamic biome identities as their chunks load.
		// Build the public sampler only after every audited chunk has supplied its
		// biome identities, so this proof does not compare against a stale snapshot.
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
		GeologySampler sampler = OreSpawnApi.createSampler(level).orElseThrow();
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						int surfaceY = level.getHeight(
								Heightmap.Types.WORLD_SURFACE_WG, x, z);
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
									int finalSurfaceY = level.getHeight(
											Heightmap.Types.WORLD_SURFACE,
											x, z);
									GeologyColumn finalSurfaceColumn =
											sampler.sampleColumn(x, z,
													finalSurfaceY);
									GeologyColumn oreYColumn =
											sampler.sampleColumn(x, z, y);
									Map<Direction, ResourceLocation> neighbors =
											new LinkedHashMap<>();
									for (Direction direction
											: Direction.values()) {
										neighbors.put(direction,
												Registry.BLOCK.getKey(
														level.getBlockState(
																firstViolation
																		.relative(
																				direction))
																.getBlock()));
									}
									firstViolationDetail =
											firstViolation
											+ " output=" + actualId
											+ " predicted="
											+ Registry.BLOCK.getKey(predicted)
											+ " family="
											+ column.familyAt(y)
													.map(Enum::name)
													.orElse("none")
											+ " wgSurface=" + surfaceY
											+ " finalSurface=" + finalSurfaceY
											+ " wgBiome=" + column.biome()
											+ " wgGeome=" + column.geome()
											+ " finalBiome="
											+ finalSurfaceColumn.biome()
											+ " finalGeome="
											+ finalSurfaceColumn.geome()
											+ " finalPredicted="
											+ Registry.BLOCK.getKey(
													finalSurfaceColumn.rockAt(y)
															.getBlock())
											+ " oreYBiome="
											+ oreYColumn.biome()
											+ " oreYGeome="
											+ oreYColumn.geome()
											+ " oreYPredicted="
											+ Registry.BLOCK.getKey(
													oreYColumn.rockAt(y)
															.getBlock())
											+ " neighbors=" + neighbors;
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

	private static FamilyHostAttributionResult auditPredictedFamilyOreHosts(
			ServerLevel level,
			Map<Block, Set<GeologyFamily>> allowedFamilies,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		Map<ResourceLocation, Integer> outputsByBlock =
				new LinkedHashMap<>();
		Map<ResourceLocation, Integer> violationsByBlock =
				new LinkedHashMap<>();
		int outputs = 0;
		int violations = 0;
		int nonReplaceableControls = 0;
		BlockPos firstViolation = null;
		String firstViolationDetail = null;
		List<String> violationDetails = new java.util.ArrayList<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
		GeologySampler sampler = OreSpawnApi.createSampler(level).orElseThrow();
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4;
							z < (chunkZ + 1) << 4; z++) {
						int surfaceY = level.getHeight(
								Heightmap.Types.WORLD_SURFACE_WG, x, z);
						GeologyColumn column = sampler.sampleColumn(
								x, z, surfaceY);
						for (int y = minY; y <= maxY; y++) {
							Block predicted = column.rockAt(y).getBlock();
							if (predicted == CakeWorldBlocks.CANDY_GLASS.get()) {
								nonReplaceableControls++;
							}
							Block actual = level.getBlockState(
									new BlockPos(x, y, z)).getBlock();
							Set<GeologyFamily> families =
									allowedFamilies.get(actual);
							if (families == null) continue;
							outputs++;
							ResourceLocation actualId =
									Registry.BLOCK.getKey(actual);
							outputsByBlock.merge(actualId, 1, Integer::sum);
							Optional<GeologyFamily> family = column.familyAt(y);
							boolean accepted = family.filter(families::contains)
									.isPresent()
									&& predicted
											!= CakeWorldBlocks.CANDY_GLASS.get();
							if (!accepted) {
								violations++;
								violationsByBlock.merge(actualId, 1,
										Integer::sum);
								BlockPos violationPosition =
										new BlockPos(x, y, z);
								Map<String, String> sampleVariants =
										new LinkedHashMap<>();
								for (Heightmap.Types type : List.of(
										Heightmap.Types.WORLD_SURFACE_WG,
										Heightmap.Types.WORLD_SURFACE,
										Heightmap.Types.OCEAN_FLOOR_WG,
										Heightmap.Types.OCEAN_FLOOR,
										Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)) {
									int variantSurface = level.getHeight(type,
											x, z);
									GeologyColumn variant = sampler.sampleColumn(
											x, z, variantSurface);
									sampleVariants.put(type.getSerializationKey(),
											variantSurface + "/"
													+ variant.biome() + "/"
													+ variant.geome() + "/"
													+ Registry.BLOCK.getKey(
															variant.rockAt(y)
																	.getBlock()));
								}
								Map<Direction, ResourceLocation> neighbors =
										new LinkedHashMap<>();
								for (Direction direction : Direction.values()) {
									neighbors.put(direction,
											Registry.BLOCK.getKey(level.getBlockState(
													violationPosition.relative(direction))
													.getBlock()));
								}
								String detail = violationPosition
											+ " output=" + actualId
											+ " predicted="
											+ Registry.BLOCK.getKey(predicted)
											+ " family="
											+ family.map(Enum::name)
													.orElse("none")
											+ " allowed=" + families
											+ " biome=" + column.biome()
											+ " geome=" + column.geome()
											+ " samples=" + sampleVariants
											+ " neighbors=" + neighbors;
								if (violationDetails.size() < 64) {
									violationDetails.add(detail);
								}
								if (firstViolation == null) {
									firstViolation = violationPosition;
									firstViolationDetail = detail;
								}
							}
						}
					}
				}
			}
		}
		return new FamilyHostAttributionResult(outputs, violations,
				nonReplaceableControls, firstViolation,
				firstViolationDetail, outputsByBlock,
				violationsByBlock, List.copyOf(violationDetails));
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
		List<BlockPos> sortedPlacements = placements.stream()
				.sorted(Comparator.comparingInt(
						(BlockPos position) -> position.getX())
						.thenComparingInt(BlockPos::getY)
						.thenComparingInt(BlockPos::getZ))
				.toList();
		int maximumDistanceSquared = sortedPlacements.stream()
				.mapToInt(position -> position.getX() * position.getX()
						+ position.getY() * position.getY()
						+ position.getZ() * position.getZ())
				.max().orElse(0);
		long signature = 1469598103934665603L;
		for (BlockPos position : sortedPlacements) {
			signature ^= position.getX();
			signature *= 1099511628211L;
			signature ^= position.getY();
			signature *= 1099511628211L;
			signature ^= position.getZ();
			signature *= 1099511628211L;
		}
		return new PatternAudit(changed, placements.size(),
				maximumDistanceSquared, signature);
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

	private static PearlAttribution countFizzyPearlAttribution(
			ServerLevel level, int centerChunkX, int centerChunkZ,
			int radius, int requestedMinY, int requestedMaxY) {
		int total = 0;
		int underLemonade = 0;
		int waferReefTreasures = 0;
		int waferReefTreasuresUnderLemonade = 0;
		int minY = Math.max(level.getMinBuildHeight(),
				requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 2,
				requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				for (int x = chunkX << 4;
						x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4;
							z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position =
									new BlockPos(x, y, z);
							if (!level.getBlockState(position).is(
									CakeWorldBlocks
											.FIZZY_PEARL.get())) {
								continue;
							}
							total++;
							boolean submerged = level
									.getFluidState(
											position.above())
									.is(CakeWorldFluids
											.LEMONADE.get());
							if (submerged) {
								underLemonade++;
							}
							if (isWaferReefTreasure(
									level, position)) {
								waferReefTreasures++;
								if (submerged) {
									waferReefTreasuresUnderLemonade++;
								}
							}
						}
					}
				}
			}
		}
		return new PearlAttribution(total, underLemonade,
				waferReefTreasures,
				waferReefTreasuresUnderLemonade);
	}

	private static boolean isWaferReefTreasure(
			ServerLevel level, BlockPos pearl) {
		if (!level.getBlockState(pearl.below())
				.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
			return false;
		}
		for (Direction direction :
				Direction.Plane.HORIZONTAL) {
			if (!level.getBlockState(pearl.relative(
					direction, 2)).is(
							CakeWorldBlocks.CANDY_GLASS.get())
					|| !level.getBlockState(pearl.below()
							.relative(direction))
							.is(CakeWorldBlocks
									.WAFER_BLOCK.get())) {
				return false;
			}
		}
		return true;
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
				"CakeWorld's local biome ownership must not become global vanilla-ore suppression");
		helper.succeed();
	}

	private static LocatedTin locateBuriedSweetTin(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				BuriedSweetTinFeature.STRUCTURE_TAG,
				origin, 50, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Buried Sweet Tin within the native Treasure-Map radius");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		if (start == null || !start.isValid()) {
			List<StructureStart> references =
					level.structureFeatureManager()
							.startsForFeature(
									net.minecraft.core
											.SectionPos
											.of(located),
									configured);
			start = references.stream()
					.filter(StructureStart::isValid)
					.findFirst().orElse(null);
		}
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1,
				"The located Buried Sweet Tin lost its one saved native treasure start");
		return new LocatedTin(
				located, startChunk, start);
	}

	private static BlockPos findBuriedSweetTinChest(
			ServerLevel level, ChunkPos startChunk) {
		int x = startChunk.getBlockX(9);
		int z = startChunk.getBlockZ(9);
		int surface = level.getHeight(
				Heightmap.Types.OCEAN_FLOOR, x, z);
		int maximumY = Math.min(
				level.getMaxBuildHeight() - 1,
				surface + 4);
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos();
		for (int y = maximumY;
				y >= level.getMinBuildHeight(); y--) {
			cursor.set(x, y, z);
			if (level.getBlockState(cursor)
					.is(Blocks.CHEST)) {
				return cursor.immutable();
			}
		}
		return null;
	}

	private static Map<Block, Integer>
			scanBuriedSweetTinPalette(
					ServerLevel level,
					BlockPos chest) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		BoundingBox bounds =
				BuriedSweetTinRepair
						.cacheBounds(chest);
		for (int x = bounds.minX();
				x <= bounds.maxX(); x++) {
			for (int y = bounds.minY();
					y <= bounds.maxY(); y++) {
				for (int z = bounds.minZ();
						z <= bounds.maxZ(); z++) {
					palette.merge(
							level.getBlockState(
									new BlockPos(
											x, y, z))
									.getBlock(),
							1, Integer::sum);
				}
			}
		}
		return palette;
	}

	private static LocatedCitadel locateMacaronCitadel(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				MacaronCitadelFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Macaron Citadel within 512 chunks of the End search anchor");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		if (start == null || !start.isValid()) {
			List<StructureStart> references =
					level.structureFeatureManager()
							.startsForFeature(
									net.minecraft.core
											.SectionPos
											.of(located),
									configured);
			start = references.stream()
					.filter(StructureStart::isValid)
					.findFirst().orElse(null);
		}
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& !start.getPieces()
								.isEmpty(),
				"The located Macaron Citadel lost its saved native End-City start");
		return new LocatedCitadel(
				located, start.getBoundingBox(),
				start.getChunkPos(), start);
	}

	private static void setCitadelChunksForced(
			ServerLevel level,
			LocatedCitadel citadel,
			boolean forced) {
		Set<ChunkPos> chunks =
				new java.util.LinkedHashSet<>();
		for (StructurePiece piece
				: citadel.start().getPieces()) {
			BoundingBox bounds =
					piece.getBoundingBox();
			int minimumChunkX = Math.floorDiv(
					bounds.minX(), 16);
			int maximumChunkX = Math.floorDiv(
					bounds.maxX(), 16);
			int minimumChunkZ = Math.floorDiv(
					bounds.minZ(), 16);
			int maximumChunkZ = Math.floorDiv(
					bounds.maxZ(), 16);
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					chunks.add(new ChunkPos(
							chunkX, chunkZ));
				}
			}
		}
		for (ChunkPos chunk : chunks) {
			level.setChunkForced(
					chunk.x, chunk.z, forced);
			if (forced) {
				level.getChunk(chunk.x, chunk.z);
			}
		}
	}

	private static CitadelWorldAudit
			auditMacaronCitadel(
					ServerLevel level,
					LocatedCitadel citadel) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		Map<String, Integer> templates =
				new LinkedHashMap<>();
		Set<ResourceLocation> loot =
				new java.util.LinkedHashSet<>();
		Set<Long> visited =
				new java.util.HashSet<>();
		int maximumDepth = -1;
		int shipPieces = 0;
		int lootChests = 0;
		int dragonHeads = 0;
		int enderChests = 0;
		int brewingStands = 0;
		int healingPotions = 0;
		BlockPos sentinel = null;
		List<StructurePiece> orderedPieces =
				citadel.start().getPieces().stream()
						.sorted(java.util.Comparator
								.comparingInt(
										(StructurePiece piece) ->
												piece.getBoundingBox()
														.minX())
								.thenComparingInt(piece ->
										piece.getBoundingBox()
												.minY())
								.thenComparingInt(piece ->
										piece.getBoundingBox()
												.minZ())
								.thenComparing(piece ->
										savedEndCityTemplateName(
												piece)))
						.toList();
		for (StructurePiece piece : orderedPieces) {
			String template =
					savedEndCityTemplateName(piece);
			templates.merge(template, 1,
					Integer::sum);
			maximumDepth = Math.max(maximumDepth,
					piece.getGenDepth());
			if (MacaronCitadelPalette
					.isWaferAirship(
							piece.getBoundingBox())) {
				shipPieces++;
			}
			BoundingBox bounds =
					piece.getBoundingBox();
			for (int x = bounds.minX();
					x <= bounds.maxX(); x++) {
				for (int y = bounds.minY();
						y <= bounds.maxY(); y++) {
					for (int z = bounds.minZ();
							z <= bounds.maxZ();
							z++) {
						BlockPos position =
								new BlockPos(
										x, y, z);
						if (!visited.add(
								position.asLong())) {
							continue;
						}
						Block block =
								level.getBlockState(
										position)
										.getBlock();
						palette.merge(block, 1,
								Integer::sum);
						if (sentinel == null
								&& (block
										== CakeWorldBlocks
												.MACARON_BRICKS
												.get()
										|| block
												== Blocks
														.PURPUR_BLOCK)) {
							sentinel =
									position.immutable();
						}
						if (block
								== Blocks
										.DRAGON_WALL_HEAD) {
							dragonHeads++;
						}
						if (block
								== Blocks.ENDER_CHEST) {
							enderChests++;
						}
						BlockEntity entity =
								level.getBlockEntity(
										position);
						if (entity
								instanceof BrewingStandBlockEntity
										stand) {
							brewingStands++;
							for (int slot = 0;
									slot < stand
											.getContainerSize();
									slot++) {
								if (PotionUtils
										.getPotion(
												stand.getItem(
														slot))
										== Potions
												.STRONG_HEALING) {
									healingPotions++;
								}
							}
						}
						if (entity != null) {
							String lootId =
									entity.saveWithoutMetadata()
											.getString(
													"LootTable");
							if (!lootId.isEmpty()) {
								lootChests++;
								loot.add(
										new ResourceLocation(
												lootId));
							}
						}
					}
				}
			}
		}
		BoundingBox bounds = citadel.bounds();
		AABB area = new AABB(
				bounds.minX(), bounds.minY(),
				bounds.minZ(),
				bounds.maxX() + 1,
				bounds.maxY() + 1,
				bounds.maxZ() + 1);
		int macaronClams =
				level.getEntitiesOfClass(
						MacaronClam.class, area)
						.size();
		int literalShulkers =
				(int)level.getEntitiesOfClass(
						Shulker.class, area)
						.stream()
						.filter(shulker ->
								shulker.getType()
										== EntityType
												.SHULKER)
						.count();
		int elytraFrames =
				(int)level.getEntitiesOfClass(
						ItemFrame.class, area)
						.stream()
						.filter(frame -> frame.getItem()
								.is(Items.ELYTRA))
						.count();
		BlockPos centre = bounds.getCenter();
		ResourceLocation biomeId =
				level.getBiome(centre).unwrapKey()
						.map(key -> key.location())
						.orElse(null);
		boolean literalEligible =
				level.getBiome(centre).is(
						BiomeTags.HAS_END_CITY)
						&& level.getBiome(centre).is(
								MacaronCitadelFeature
										.GENERATES_IN);
		return new CitadelWorldAudit(
				palette, biomeId, templates,
				maximumDepth, shipPieces, loot,
				lootChests, macaronClams,
				literalShulkers, elytraFrames,
				dragonHeads, enderChests,
				brewingStands, healingPotions,
				literalEligible, sentinel);
	}

	private static String savedEndCityTemplateName(
			StructurePiece piece) {
		requireEndCityPiece(piece);
		try {
			Field templateName =
					TemplateStructurePiece.class
							.getDeclaredField(
									"templateName");
			templateName.setAccessible(true);
			return templateName.get(piece).toString();
		} catch (ReflectiveOperationException exception) {
			throw new AssertionError(
					"Could not inspect saved End-City template identity",
					exception);
		}
	}

	private static void requireEndCityPiece(
			StructurePiece piece) {
		if (!(piece
				instanceof EndCityPieces.EndCityPiece)) {
			throw new AssertionError(
					"Macaron Citadel saved a non-End-City piece: "
							+ piece.getClass()
									.getName());
		}
	}

	private static LocatedFortress locateLiquoriceFortress(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				LiquoriceFortressFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Liquorice Fortress within 512 chunks of the Nether origin");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		if (start == null || !start.isValid()) {
			List<StructureStart> references =
					level.structureFeatureManager()
							.startsForFeature(
									net.minecraft.core
											.SectionPos
											.of(located),
									configured);
			start = references.stream()
					.filter(StructureStart::isValid)
					.findFirst().orElse(null);
		}
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& !start.getPieces()
								.isEmpty(),
				"The located Liquorice Fortress lost its saved native start");
		return new LocatedFortress(
				located, start.getBoundingBox(),
				start.getChunkPos(), start);
	}

	private static void setFortressChunksForced(
			ServerLevel level,
			LocatedFortress fortress,
			boolean forced) {
		Set<ChunkPos> chunks =
				new java.util.LinkedHashSet<>();
		for (StructurePiece piece
				: fortress.start().getPieces()) {
			BoundingBox bounds =
					piece.getBoundingBox();
			int minimumChunkX = Math.floorDiv(
					bounds.minX(), 16);
			int maximumChunkX = Math.floorDiv(
					bounds.maxX(), 16);
			int minimumChunkZ = Math.floorDiv(
					bounds.minZ(), 16);
			int maximumChunkZ = Math.floorDiv(
					bounds.maxZ(), 16);
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					chunks.add(new ChunkPos(
							chunkX, chunkZ));
				}
			}
		}
		for (ChunkPos chunk : chunks) {
			level.setChunkForced(
					chunk.x, chunk.z, forced);
			if (forced) {
				level.getChunk(chunk.x, chunk.z);
			}
		}
	}

	private static FortressWorldAudit
			auditLiquoriceFortress(
					ServerLevel level,
					LocatedFortress fortress) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		Map<String, Integer> pieceKinds =
				new LinkedHashMap<>();
		Set<ResourceLocation> loot =
				new java.util.LinkedHashSet<>();
		Set<Long> visited =
				new java.util.HashSet<>();
		int maximumDepth = -1;
		int blazeSpawners = 0;
		List<StructurePiece> orderedPieces =
				fortress.start().getPieces().stream()
						.sorted(java.util.Comparator
								.comparingInt(
										(StructurePiece piece) ->
												piece.getBoundingBox()
														.minX())
								.thenComparingInt(piece ->
										piece.getBoundingBox()
												.minY())
								.thenComparingInt(piece ->
										piece.getBoundingBox()
												.minZ())
								.thenComparing(piece ->
										piece.getClass()
												.getSimpleName()))
						.toList();
		BoundingBox sentinelPiece =
				orderedPieces.get(0)
						.getBoundingBox();
		BlockPos sentinel = new BlockPos(
				sentinelPiece.minX(),
				sentinelPiece.minY(),
				sentinelPiece.minZ());
		for (StructurePiece piece : orderedPieces) {
			pieceKinds.merge(
					piece.getClass().getSimpleName(),
					1, Integer::sum);
			maximumDepth = Math.max(maximumDepth,
					piece.getGenDepth());
			BoundingBox bounds =
					piece.getBoundingBox();
			for (int x = bounds.minX();
					x <= bounds.maxX(); x++) {
				for (int y = bounds.minY();
						y <= bounds.maxY(); y++) {
					for (int z = bounds.minZ();
							z <= bounds.maxZ();
							z++) {
						BlockPos position =
								new BlockPos(
										x, y, z);
						if (!visited.add(
								position.asLong())) {
							continue;
						}
						Block block =
								level.getBlockState(
										position)
										.getBlock();
						palette.merge(block, 1,
								Integer::sum);
						BlockEntity entity =
								level.getBlockEntity(
										position);
						if (entity
								instanceof SpawnerBlockEntity) {
							String spawned =
									entity.saveWithoutMetadata()
											.getCompound(
													"SpawnData")
											.getCompound(
													"entity")
											.getString(
													"id");
							if ("minecraft:blaze"
									.equals(spawned)) {
								blazeSpawners++;
							}
						}
						if (entity != null) {
							String lootId =
									entity.saveWithoutMetadata()
											.getString(
													"LootTable");
							if (!lootId.isEmpty()) {
								loot.add(
										new ResourceLocation(
												lootId));
							}
						}
					}
				}
			}
		}
		BoundingBox bounds = fortress.bounds();
		AABB area = new AABB(
				bounds.minX(), bounds.minY(),
				bounds.minZ(),
				bounds.maxX() + 1,
				bounds.maxY() + 1,
				bounds.maxZ() + 1);
		int cinnamonSparks =
				level.getEntitiesOfClass(
						CinnamonSpark.class, area)
						.size();
		int literalBlazes =
				level.getEntitiesOfClass(
						Blaze.class, area,
						entity -> entity.getType()
								== EntityType.BLAZE)
						.size();
		BlockPos centre = bounds.getCenter();
		ResourceLocation biomeId =
				level.getBiome(centre).unwrapKey()
						.map(key -> key.location())
						.orElse(null);
		boolean literalEligible =
				level.getBiome(centre).is(
						BiomeTags
								.HAS_NETHER_FORTRESS)
						&& level.getBiome(centre).is(
								LiquoriceFortressFeature
										.GENERATES_IN);
		return new FortressWorldAudit(
				palette, biomeId, pieceKinds,
				maximumDepth, loot,
				blazeSpawners,
				cinnamonSparks, literalBlazes,
				literalEligible, sentinel);
	}

	private static LocatedFoundry locateBurntToffeeFoundry(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				BurntToffeeFoundryFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Burnt-Toffee Foundry within 512 chunks of the Nether origin");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		if (start == null || !start.isValid()) {
			start = level.structureFeatureManager()
					.startsForFeature(
							net.minecraft.core.SectionPos
									.of(located),
							configured)
					.stream()
					.filter(StructureStart::isValid)
					.findFirst().orElse(null);
		}
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& !start.getPieces()
								.isEmpty(),
				"The located Burnt-Toffee Foundry lost its saved native Bastion start");
		return new LocatedFoundry(
				located, start.getBoundingBox(),
				start.getChunkPos(), start);
	}

	private static void setFoundryChunksForced(
			ServerLevel level,
			LocatedFoundry foundry,
			boolean forced) {
		Set<ChunkPos> chunks =
				new java.util.LinkedHashSet<>();
		for (StructurePiece piece
				: foundry.start().getPieces()) {
			BoundingBox bounds =
					piece.getBoundingBox();
			int minimumChunkX = Math.floorDiv(
					bounds.minX(), 16);
			int maximumChunkX = Math.floorDiv(
					bounds.maxX(), 16);
			int minimumChunkZ = Math.floorDiv(
					bounds.minZ(), 16);
			int maximumChunkZ = Math.floorDiv(
					bounds.maxZ(), 16);
			for (int chunkX = minimumChunkX;
					chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ;
						chunkZ++) {
					chunks.add(new ChunkPos(
							chunkX, chunkZ));
				}
			}
		}
		for (ChunkPos chunk : chunks) {
			level.setChunkForced(
					chunk.x, chunk.z, forced);
			if (forced) {
				level.getChunk(chunk.x, chunk.z);
			}
		}
	}

	private static FoundryWorldAudit
			auditBurntToffeeFoundry(
					ServerLevel level,
					LocatedFoundry foundry) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		Map<String, Integer> templates =
				new LinkedHashMap<>();
		Set<ResourceLocation> loot =
				new java.util.LinkedHashSet<>();
		Set<Long> visited =
				new java.util.HashSet<>();
		int chests = 0;
		int magmaCubeSpawners = 0;
		String startFamily = null;
		List<StructurePiece> orderedPieces =
				foundry.start().getPieces().stream()
						.sorted(java.util.Comparator
								.comparingInt(
										(StructurePiece piece) ->
												piece.getBoundingBox()
														.minX())
								.thenComparingInt(piece ->
										piece.getBoundingBox()
												.minY())
								.thenComparingInt(piece ->
										piece.getBoundingBox()
												.minZ())
								.thenComparing(Object::toString))
						.toList();
		BoundingBox sentinelPiece =
				orderedPieces.get(0)
						.getBoundingBox();
		BlockPos sentinel = new BlockPos(
				sentinelPiece.minX(),
				sentinelPiece.minY(),
				sentinelPiece.minZ());
		for (StructurePiece piece : orderedPieces) {
			if (!(piece
					instanceof PoolElementStructurePiece
							poolPiece)) {
				continue;
			}
			String template =
					poolPiece.getElement().toString();
			templates.merge(template, 1,
					Integer::sum);
			if (template.contains(
					"bastion/units/air_base")) {
				startFamily = "units";
			} else if (template.contains(
					"bastion/hoglin_stable/air_base")) {
				startFamily = "hoglin_stable";
			} else if (template.contains(
					"bastion/treasure/big_air_full")) {
				startFamily = "treasure";
			} else if (template.contains(
					"bastion/bridge/starting_pieces/entrance_base")) {
				startFamily = "bridge";
			}
			BoundingBox bounds =
					piece.getBoundingBox();
			for (int x = bounds.minX();
					x <= bounds.maxX(); x++) {
				for (int y = bounds.minY();
						y <= bounds.maxY(); y++) {
					for (int z = bounds.minZ();
							z <= bounds.maxZ();
							z++) {
						BlockPos position =
								new BlockPos(
										x, y, z);
						if (!visited.add(
								position.asLong())) {
							continue;
						}
						BlockState state =
								level.getBlockState(
										position);
						palette.merge(
								state.getBlock(),
								1, Integer::sum);
						BlockEntity entity =
								level.getBlockEntity(
										position);
						if (entity == null) {
							continue;
						}
						CompoundTag saved =
								entity
										.saveWithoutMetadata();
						String lootId =
								saved.getString(
										"LootTable");
						if (!lootId.isEmpty()) {
							chests++;
							loot.add(
									new ResourceLocation(
											lootId));
						}
						if (entity
								instanceof SpawnerBlockEntity
								&& "minecraft:magma_cube"
										.equals(saved
												.getCompound(
														"SpawnData")
												.getCompound(
														"entity")
												.getString(
														"id"))) {
							magmaCubeSpawners++;
						}
					}
				}
			}
		}
		BoundingBox bounds = foundry.bounds();
		AABB area = new AABB(
				bounds.minX(), bounds.minY(),
				bounds.minZ(),
				bounds.maxX() + 1,
				bounds.maxY() + 1,
				bounds.maxZ() + 1);
		int fudgeFolk =
				level.getEntitiesOfClass(
						FudgeFolk.class, area)
						.size();
		int fudgeBrutes =
				level.getEntitiesOfClass(
						FudgeBrute.class, area)
						.size();
		int fudgeBoars =
				level.getEntitiesOfClass(
						FudgeBoar.class, area)
						.size();
		int literalPiglins =
				level.getEntitiesOfClass(
						Piglin.class, area,
						entity -> entity.getType()
								== EntityType.PIGLIN)
						.size();
		int literalBrutes =
				level.getEntitiesOfClass(
						PiglinBrute.class, area,
						entity -> entity.getType()
								== EntityType
										.PIGLIN_BRUTE)
						.size();
		int literalHoglins =
				level.getEntitiesOfClass(
						Hoglin.class, area,
						entity -> entity.getType()
								== EntityType.HOGLIN)
						.size();
		BlockPos centre = bounds.getCenter();
		ResourceLocation biomeId =
				level.getBiome(centre).unwrapKey()
						.map(key -> key.location())
						.orElse(null);
		boolean literalEligible =
				level.getBiome(centre).is(
						BiomeTags
								.HAS_BASTION_REMNANT)
						&& level.getBiome(centre).is(
								BurntToffeeFoundryFeature
										.GENERATES_IN);
		return new FoundryWorldAudit(
				palette, biomeId, templates,
				startFamily, loot, chests,
				magmaCubeSpawners,
				fudgeFolk, fudgeBrutes,
				fudgeBoars, literalPiglins,
				literalBrutes, literalHoglins,
				literalEligible, sentinel);
	}

	private static boolean isNativeBastionMasonry(
			Block block) {
		return block == Blocks.BLACKSTONE
				|| block == Blocks
						.POLISHED_BLACKSTONE_BRICKS
				|| block == Blocks
						.CRACKED_POLISHED_BLACKSTONE_BRICKS
				|| block == Blocks
						.POLISHED_BLACKSTONE_BRICK_STAIRS
				|| block == Blocks.BLACKSTONE_STAIRS
				|| block == Blocks.BLACKSTONE_SLAB
				|| block == Blocks.BLACKSTONE_WALL
				|| block == Blocks
						.CHISELED_POLISHED_BLACKSTONE
				|| block == Blocks.GILDED_BLACKSTONE
				|| block == Blocks.BASALT
				|| block == Blocks.POLISHED_BASALT;
	}

	private static boolean isBastionStartTemplate(
			String template) {
		return template.contains(
				"bastion/units/air_base")
				|| template.contains(
						"bastion/hoglin_stable/air_base")
				|| template.contains(
						"bastion/treasure/big_air_full")
				|| template.contains(
						"bastion/bridge/starting_pieces/entrance_base");
	}

	private static LocatedSweetshop locateSunkenSweetshop(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> cold,
			ConfiguredStructureFeature<?, ?> warm,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				SunkenSweetshopFeature.STRUCTURE_TAG,
				origin, 128, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Sunken Sweetshop within 128 chunks of Soda Ocean");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(cold);
		ConfiguredStructureFeature<?, ?> configured =
				cold;
		if (start == null || !start.isValid()) {
			start = startLevelChunk
					.getStartForFeature(warm);
			configured = warm;
		}
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& !start.getPieces()
								.isEmpty(),
				"The located Sunken Sweetshop lost its saved native Ocean Ruin start");
		ResourceLocation configuredId =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.getKey(configured);
		return new LocatedSweetshop(
				located, configuredId,
				start.getBoundingBox(),
				startChunk, start);
	}

	private static void setSweetshopChunksForced(
			ServerLevel level,
			LocatedSweetshop sweetshop,
			boolean forced) {
		int minimumChunkX = Math.floorDiv(
				sweetshop.bounds().minX(), 16);
		int maximumChunkX = Math.floorDiv(
				sweetshop.bounds().maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				sweetshop.bounds().minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				sweetshop.bounds().maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
				if (forced) {
					level.getChunk(chunkX, chunkZ);
				}
			}
		}
	}

	private static SweetshopWorldAudit
			auditSunkenSweetshop(
					ServerLevel level,
					LocatedSweetshop sweetshop) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		Set<ResourceLocation> loot =
				new java.util.LinkedHashSet<>();
		Set<String> templates =
				new java.util.LinkedHashSet<>();
		Set<Float> integrities =
				new java.util.LinkedHashSet<>();
		Set<Long> visited =
				new java.util.HashSet<>();
		int largePieces = 0;
		try {
			Field templateName =
					net.minecraft.world.level.levelgen
							.structure.TemplateStructurePiece
							.class.getDeclaredField(
									"templateName");
			Field integrity =
					OceanRuinPieces.OceanRuinPiece
							.class.getDeclaredField(
									"integrity");
			Field isLarge =
					OceanRuinPieces.OceanRuinPiece
							.class.getDeclaredField(
									"isLarge");
			Field biomeType =
					OceanRuinPieces.OceanRuinPiece
							.class.getDeclaredField(
									"biomeType");
			templateName.setAccessible(true);
			integrity.setAccessible(true);
			isLarge.setAccessible(true);
			biomeType.setAccessible(true);
			boolean expectedCold =
					sweetshop.configuredId().equals(
							SunkenSweetshopFeature
									.COLD_STRUCTURE_ID);
			for (StructurePiece piece
					: sweetshop.start()
							.getPieces()) {
				if (!(piece
						instanceof OceanRuinPieces
								.OceanRuinPiece)) {
					continue;
				}
				templates.add(templateName
						.get(piece).toString());
				integrities.add(
						integrity.getFloat(piece));
				if (isLarge.getBoolean(piece)) {
					largePieces++;
				}
				Object actualType =
						biomeType.get(piece);
				if (actualType
						!= (expectedCold
								? OceanRuinFeature.Type
										.COLD
								: OceanRuinFeature.Type
										.WARM)) {
					throw new AssertionError(
							"Loaded Ocean Ruin piece lost its configured biome type");
				}
				BoundingBox bounds =
						piece.getBoundingBox();
				for (int x = bounds.minX();
						x <= bounds.maxX(); x++) {
					for (int y = bounds.minY();
							y <= bounds.maxY();
							y++) {
						for (int z = bounds.minZ();
								z <= bounds
										.maxZ();
								z++) {
							BlockPos position =
									new BlockPos(
											x, y, z);
							if (!visited.add(
									position
											.asLong())) {
								continue;
							}
							Block block =
									level.getBlockState(
											position)
											.getBlock();
							palette.merge(
									block, 1,
									Integer::sum);
							BlockEntity entity =
									level.getBlockEntity(
											position);
							if (entity != null) {
								String lootId =
										entity.saveWithoutMetadata()
												.getString(
														"LootTable");
								if (!lootId
										.isEmpty()) {
									loot.add(
											new ResourceLocation(
													lootId));
								}
							}
						}
					}
				}
			}
		} catch (ReflectiveOperationException exception) {
			throw new AssertionError(
					"Could not inspect loaded native Ocean Ruin piece fields",
					exception);
		}
		BoundingBox bounds = sweetshop.bounds();
		AABB area = new AABB(
				bounds.minX(), bounds.minY(),
				bounds.minZ(),
				bounds.maxX() + 1,
				bounds.maxY() + 1,
				bounds.maxZ() + 1);
		int soggyBiscuits =
				level.getEntitiesOfClass(
						SoggyBiscuit.class, area)
						.size();
		int literalDrowned =
				level.getEntitiesOfClass(
						Drowned.class, area,
						entity -> entity.getType()
								== EntityType.DROWNED)
						.size();
		BlockPos biomePosition = new BlockPos(
				(bounds.minX() + bounds.maxX()) / 2,
				level.getSeaLevel(),
				(bounds.minZ() + bounds.maxZ()) / 2);
		Holder<Biome> biome =
				level.getBiome(biomePosition);
		ResourceLocation biomeId =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(biome.value());
		boolean literalEligible =
				biome.is(BiomeTags
						.HAS_OCEAN_RUIN_COLD)
						&& biome.is(BiomeTags
								.HAS_OCEAN_RUIN_WARM);
		return new SweetshopWorldAudit(
				palette, biomeId, loot,
				templates, integrities,
				largePieces, soggyBiscuits,
				literalDrowned,
				literalEligible);
	}

	private static boolean
			hasNoUnthemedSweetshopMasonry(
					Map<Block, Integer> palette) {
		return palette.getOrDefault(
				Blocks.SANDSTONE, 0) == 0
				&& palette.getOrDefault(
						Blocks.CUT_SANDSTONE,
						0) == 0
				&& palette.getOrDefault(
						Blocks.CHISELED_SANDSTONE,
						0) == 0
				&& palette.getOrDefault(
						Blocks.SMOOTH_SANDSTONE,
						0) == 0
				&& palette.getOrDefault(
						Blocks.SANDSTONE_STAIRS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.SANDSTONE_SLAB,
						0) == 0
				&& palette.getOrDefault(
						Blocks.STONE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.CRACKED_STONE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.MOSSY_STONE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.COBBLESTONE,
						0) == 0
				&& palette.getOrDefault(
						Blocks.MOSSY_COBBLESTONE,
						0) == 0
				&& palette.getOrDefault(
						Blocks.STONE_BRICK_STAIRS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.STONE_BRICK_SLAB,
						0) == 0
				&& palette.getOrDefault(
						Blocks.MOSSY_STONE_BRICK_STAIRS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.MOSSY_STONE_BRICK_SLAB,
						0) == 0
				&& palette.getOrDefault(
						Blocks.SAND, 0) == 0
				&& palette.getOrDefault(
						Blocks.GRAVEL, 0) == 0;
	}

	private static LocatedPalace locateSodaPalace(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				SodaPalaceFeature.STRUCTURE_TAG,
				origin, 128, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Soda Palace within 128 chunks of Soda Ocean");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured
						&& start.getPieces().size() == 1
						&& start.getPieces().get(0)
								instanceof OceanMonumentPieces
										.MonumentBuilding,
				"The located Soda Palace lost its saved native MonumentBuilding start");
		return new LocatedPalace(
				located,
				start.getBoundingBox(),
				start.getPieces().size(),
				startChunk,
				start,
				start.getPieces().get(0));
	}

	private static void setPalaceChunksForced(
			ServerLevel level, LocatedPalace palace,
			boolean forced) {
		int minimumChunkX = Math.floorDiv(
				palace.bounds().minX(), 16);
		int maximumChunkX = Math.floorDiv(
				palace.bounds().maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				palace.bounds().minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				palace.bounds().maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
				if (forced) {
					level.getChunk(
							chunkX, chunkZ);
				}
			}
		}
	}

	private static PalaceWorldAudit auditSodaPalace(
			ServerLevel level, LocatedPalace palace) {
		List<StructurePiece> childPieces = List.of();
		try {
			Field children = OceanMonumentPieces
					.MonumentBuilding.class
					.getDeclaredField("childPieces");
			children.setAccessible(true);
			@SuppressWarnings("unchecked")
			List<StructurePiece> inspected =
					(List<StructurePiece>)
							children.get(palace.building());
			childPieces = inspected;
		} catch (ReflectiveOperationException exception) {
			throw new AssertionError(
					"Could not inspect the loaded Soda Palace child graph",
					exception);
		}
		int spongeRooms = (int) childPieces.stream()
				.filter(OceanMonumentPieces
						.OceanMonumentSimpleTopRoom.class
						::isInstance)
				.count();
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		BoundingBox bounds = palace.bounds();
		for (int x = bounds.minX();
				x <= bounds.maxX(); x++) {
			for (int y = bounds.minY();
					y <= bounds.maxY(); y++) {
				for (int z = bounds.minZ();
						z <= bounds.maxZ(); z++) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					palette.merge(block, 1,
							Integer::sum);
				}
			}
		}
		BlockPos biomePosition = new BlockPos(
				(bounds.minX() + bounds.maxX()) / 2,
				level.getSeaLevel(),
				(bounds.minZ() + bounds.maxZ()) / 2);
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								biomePosition).value());
		boolean literalEligible =
				level.getBiome(biomePosition).is(
						BiomeTags.HAS_OCEAN_MONUMENT);
		int grandGuardians =
				level.getEntitiesOfClass(
						GrandGumballGuardian.class,
						palace.boundsAabb()).size();
		int literalElders =
				level.getEntitiesOfClass(
						ElderGuardian.class,
						palace.boundsAabb(),
						entity -> entity.getType()
								== EntityType
										.ELDER_GUARDIAN)
						.size();
		return new PalaceWorldAudit(
				palette, biome, childPieces.size(),
				spongeRooms, grandGuardians,
				literalElders, literalEligible);
	}

	private static boolean hasNoUnthemedPalaceMasonry(
			Map<Block, Integer> palette) {
		return palette.getOrDefault(
					Blocks.PRISMARINE, 0) == 0
				&& palette.getOrDefault(
						Blocks.PRISMARINE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.DARK_PRISMARINE,
						0) == 0;
	}

	private static LocatedVault locateAncientCakeVault(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				AncientCakeVaultFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Ancient Cake Vault within 512 chunks");
		BlockPos eyeLocated =
				level.findNearestMapFeature(
						ConfiguredStructureTags
								.EYE_OF_ENDER_LOCATED,
						origin, 512, false);
		require(helper, eyeLocated != null,
				"The fixed-seed Ancient Cake Vault was not locatable with a vanilla Eye of Ender");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		String savedStarts =
				startLevelChunk.getAllStarts()
						.entrySet().stream()
						.map(entry ->
								structuresLabel(
										level,
										entry.getKey())
										+ "->"
										+ structuresLabel(
												level,
												entry.getValue()
														.getFeature())
										+ "(valid="
										+ entry.getValue()
												.isValid()
										+ ")")
						.sorted()
						.collect(java.util.stream
								.Collectors
								.joining(", "));
		require(helper,
				start != null && start.isValid()
						&& start.getFeature() == configured,
				"The located Ancient Cake Vault lost its saved structure start: locate="
						+ located + ", eye="
						+ eyeLocated + ", chunk="
						+ startChunk
						+ ", configured="
						+ structuresLabel(
								level, configured)
						+ ", direct="
						+ (start == null
								? "null"
								: structuresLabel(
										level,
										start.getFeature())
										+ "(valid="
										+ start.isValid()
										+ ")")
						+ ", starts={"
						+ savedStarts + "}");
		List<StructurePiece> pieces =
				start.getPieces();
		StructurePiece portal = pieces.stream()
				.filter(StrongholdPieces.PortalRoom.class
						::isInstance)
				.findFirst().orElse(null);
		StructurePiece library = pieces.stream()
				.filter(StrongholdPieces.Library.class
						::isInstance)
				.findFirst().orElse(null);
		StructurePiece corridor = pieces.stream()
				.filter(StrongholdPieces.ChestCorridor.class
						::isInstance)
				.findFirst().orElse(null);
		long portals = pieces.stream()
				.filter(StrongholdPieces.PortalRoom.class
						::isInstance)
				.count();
		long libraries = pieces.stream()
				.filter(StrongholdPieces.Library.class
						::isInstance)
				.count();
		long corridors = pieces.stream()
				.filter(StrongholdPieces.ChestCorridor.class
						::isInstance)
				.count();
		long junctions = pieces.stream()
				.filter(piece ->
						piece instanceof StrongholdPieces
								.FiveCrossing
								|| piece
										instanceof StrongholdPieces
												.RoomCrossing)
				.count();
		int maximumDepth = pieces.stream()
				.mapToInt(StructurePiece::getGenDepth)
				.max().orElse(-1);
		require(helper, portal != null,
				"The natural Ancient Cake Vault graph contained no guaranteed portal room");
		return new LocatedVault(
				located, eyeLocated,
				start.getBoundingBox(),
				pieces.size(),
				(int) portals,
				(int) libraries,
				(int) corridors,
				(int) junctions,
				maximumDepth,
				startChunk,
				portal, library, corridor);
	}

	private static String structuresLabel(
			ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured) {
		ResourceLocation id =
				level.registryAccess()
						.registryOrThrow(
								Registry
										.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
						.getKey(configured);
		return id == null
				? "<unregistered@"
						+ System.identityHashCode(
								configured)
						+ ">"
				: id + "@"
						+ System.identityHashCode(
								configured);
	}

	private static void setVaultChunksForced(
			ServerLevel level, LocatedVault vault,
			boolean forced) {
		Set<ChunkPos> chunks =
				new java.util.LinkedHashSet<>();
		chunks.add(vault.startChunk());
		for (StructurePiece piece : List.of(
				vault.portalRoom())) {
			addVaultPieceChunks(chunks,
					piece.getBoundingBox());
		}
		if (vault.library() != null) {
			addVaultPieceChunks(chunks,
					vault.library()
							.getBoundingBox());
		}
		if (vault.corridor() != null) {
			addVaultPieceChunks(chunks,
					vault.corridor()
							.getBoundingBox());
		}
		for (ChunkPos chunk : chunks) {
			level.setChunkForced(
					chunk.x, chunk.z, forced);
		}
	}

	private static void addVaultPieceChunks(
			Set<ChunkPos> chunks,
			BoundingBox bounds) {
		int minimumChunkX =
				Math.floorDiv(bounds.minX(), 16);
		int maximumChunkX =
				Math.floorDiv(bounds.maxX(), 16);
		int minimumChunkZ =
				Math.floorDiv(bounds.minZ(), 16);
		int maximumChunkZ =
				Math.floorDiv(bounds.maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				chunks.add(new ChunkPos(
						chunkX, chunkZ));
			}
		}
	}

	private static VaultPieceAudit auditVaultPiece(
			ServerLevel level,
			StructurePiece piece) {
		BoundingBox bounds =
				piece.getBoundingBox();
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		Set<ResourceLocation> loot =
				new java.util.LinkedHashSet<>();
		List<BlockPos> frames =
				new java.util.ArrayList<>();
		String spawnerEntity = "";
		for (int x = bounds.minX();
				x <= bounds.maxX(); x++) {
			for (int y = bounds.minY();
					y <= bounds.maxY(); y++) {
				for (int z = bounds.minZ();
						z <= bounds.maxZ(); z++) {
					BlockPos position =
							new BlockPos(x, y, z);
					BlockState state =
							level.getBlockState(
									position);
					palette.merge(
							state.getBlock(), 1,
							Integer::sum);
					if (state.is(
							Blocks.END_PORTAL_FRAME)) {
						frames.add(position);
					}
					BlockEntity entity =
							level.getBlockEntity(
									position);
					if (entity != null) {
						CompoundTag saved =
								entity
										.saveWithoutMetadata();
						String lootTable =
								saved.getString(
										"LootTable");
						if (!lootTable.isEmpty()) {
							loot.add(
									new ResourceLocation(
											lootTable));
						}
					}
					if (entity
							instanceof SpawnerBlockEntity
									spawner) {
						CompoundTag saved =
								spawner.getSpawner()
										.save(
												new CompoundTag());
						spawnerEntity =
								saved.getCompound(
										"SpawnData")
										.getCompound(
												"entity")
										.getString(
												"id");
					}
				}
			}
		}
		return new VaultPieceAudit(
				palette, loot, frames,
				spawnerEntity);
	}

	private static boolean provesCompletableEndPortal(
			ServerLevel level,
			List<BlockPos> frames) {
		if (frames.size() != 12) {
			return false;
		}
		Map<BlockPos, BlockState> originals =
				new LinkedHashMap<>();
		for (BlockPos frame : frames) {
			BlockState original =
					level.getBlockState(frame);
			originals.put(frame.immutable(),
					original);
			level.setBlock(frame,
					original.setValue(
							EndPortalFrameBlock
									.HAS_EYE,
							true),
					2);
		}
		boolean complete = frames.stream()
				.anyMatch(frame ->
						EndPortalFrameBlock
								.getOrCreatePortalShape()
								.find(level, frame)
								!= null);
		originals.forEach((position, state) ->
				level.setBlock(position, state, 2));
		return complete;
	}

	private static boolean hasNoUnthemedVaultBlocks(
			Map<Block, Integer> palette) {
		return palette.getOrDefault(
						Blocks.STONE_BRICKS, 0) == 0
				&& palette.getOrDefault(
						Blocks.CRACKED_STONE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.MOSSY_STONE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.INFESTED_STONE_BRICKS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.STONE_BRICK_SLAB,
						0) == 0
				&& palette.getOrDefault(
						Blocks.SMOOTH_STONE_SLAB,
						0) == 0
				&& palette.getOrDefault(
						Blocks.STONE_BRICK_STAIRS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.COBBLESTONE_STAIRS,
						0) == 0
				&& palette.getOrDefault(
						Blocks.OAK_PLANKS, 0) == 0
				&& palette.getOrDefault(
						Blocks.BOOKSHELF, 0) == 0
				&& palette.getOrDefault(
						Blocks.LAVA, 0) == 0;
	}

	private static LocatedArch locateBurntSugarArch(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin, String dimensionName) {
		BlockPos located = level.findNearestMapFeature(
				BurntSugarArchFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Burnt-Sugar Arch within 512 chunks in the "
						+ dimensionName);
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
				"The located Burnt-Sugar Arch lost its saved "
						+ dimensionName
						+ " structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX() + 8,
				bounds.minY() + 4,
				bounds.minZ() + 8);
		return new LocatedArch(
				located, centre, bounds);
	}

	private static void setArchChunksForced(
			ServerLevel level, LocatedArch arch,
			boolean forced) {
		int minimumChunkX = Math.floorDiv(
				arch.bounds().minX(), 16);
		int maximumChunkX = Math.floorDiv(
				arch.bounds().maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				arch.bounds().minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				arch.bounds().maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
			}
		}
	}

	private static ArchWorldAudit auditBurntSugarArch(
			ServerLevel level, LocatedArch arch,
			boolean nether) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -8; x <= 8; x++) {
			for (int y = -4; y <= 12; y++) {
				for (int z = -8; z <= 8; z++) {
					Block block = level
							.getBlockState(
									arch.centre()
											.offset(
													x,
													y,
													z))
							.getBlock();
					palette.merge(block, 1,
							Integer::sum);
				}
			}
		}
		BlockEntity chestEntity =
				level.getBlockEntity(
						arch.centre().offset(
								5, 1, 3));
		CompoundTag chestState =
				chestEntity == null
						? new CompoundTag()
						: chestEntity
								.saveWithoutMetadata();
		boolean loot =
				BurntSugarArchFeature.LOOT_ID
						.toString().equals(
								chestState.getString(
										"LootTable"));
		boolean gaps = true;
		for (BlockPos gap
				: BurntSugarArchFeature
						.portalGaps(
								arch.centre())) {
			gaps &= level.getBlockState(gap).isAir();
			level.setBlock(gap,
					Blocks.OBSIDIAN
							.defaultBlockState(),
					2);
		}
		boolean repairable =
				PortalShape.findEmptyPortalShape(
						level,
						BurntSugarArchFeature
								.portalInterior(
										arch.centre()),
						Direction.Axis.X)
						.isPresent();
		for (BlockPos gap
				: BurntSugarArchFeature
						.portalGaps(
								arch.centre())) {
			level.setBlock(gap,
					Blocks.AIR.defaultBlockState(),
					2);
		}
		ResourceLocation biomeId =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								arch.centre())
								.value());
		boolean literalEligible =
				isLiteralRuinedPortalBiome(
						level.getBiome(
								arch.centre()));
		return new ArchWorldAudit(
				palette, biomeId, loot,
				gaps && repairable,
				literalEligible);
	}

	private static boolean isLiteralRuinedPortalBiome(
			Holder<Biome> biome) {
		return biome.is(
				BiomeTags.HAS_RUINED_PORTAL_STANDARD)
				|| biome.is(
						BiomeTags
								.HAS_RUINED_PORTAL_DESERT)
				|| biome.is(
						BiomeTags
								.HAS_RUINED_PORTAL_JUNGLE)
				|| biome.is(
						BiomeTags
								.HAS_RUINED_PORTAL_SWAMP)
				|| biome.is(
						BiomeTags
								.HAS_RUINED_PORTAL_MOUNTAIN)
				|| biome.is(
						BiomeTags
								.HAS_RUINED_PORTAL_OCEAN)
				|| biome.is(
						BiomeTags
								.HAS_RUINED_PORTAL_NETHER);
	}

	private static void assertNaturalArchPalette(
			GameTestHelper helper,
			ArchWorldAudit audit,
			Block dimensionRock,
			Block dimensionGold,
			String dimensionName) {
		Map<Block, Integer> palette =
				audit.palette();
		require(helper,
				palette.getOrDefault(
						CakeWorldBlocks
								.BURNT_SUGAR_ROCK
								.get(), 0) >= 140
						&& palette.getOrDefault(
								dimensionRock, 0)
								>= 48
						&& palette.getOrDefault(
								dimensionGold, 0)
								>= 2
						&& palette.getOrDefault(
								Blocks.OBSIDIAN, 0)
								== 18
						&& palette.getOrDefault(
								Blocks
										.CRYING_OBSIDIAN,
								0) == 2
						&& palette.getOrDefault(
								Blocks.MAGMA_BLOCK,
								0) == 2
						&& palette.getOrDefault(
								CakeWorldBlocks
										.MARSHMALLOW
										.get(), 0)
								== 2
						&& palette.getOrDefault(
								Blocks.CHEST, 0)
								== 1
						&& palette.getOrDefault(
								Blocks.NETHER_PORTAL,
								0) == 0
						&& audit.loot()
						&& audit.repairable(),
				"The natural " + dimensionName
						+ " Burnt-Sugar Arch lost its edible ruin, incomplete repairable frame, warning/rescue pair or repair chest: "
						+ palette
						+ ", loot="
						+ audit.loot()
						+ ", repairable="
						+ audit.repairable());
	}

	private static LocatedWreck locateWaferWreck(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				WaferWreckFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Wafer Wreck within 512 chunks of Soda Ocean");
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
				"The located Wafer Wreck lost its saved one-piece structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX() + 16,
				bounds.minY() + 4,
				bounds.minZ() + 16);
		return new LocatedWreck(
				located, centre, bounds);
	}

	private static void setWreckChunksForced(
			ServerLevel level, LocatedWreck wreck,
			boolean forced) {
		int minimumChunkX = Math.floorDiv(
				wreck.bounds().minX(), 16);
		int maximumChunkX = Math.floorDiv(
				wreck.bounds().maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				wreck.bounds().minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				wreck.bounds().maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
			}
		}
	}

	private static WreckWorldAudit auditWaferWreck(
			ServerLevel level, LocatedWreck wreck) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -16; x <= 16; x++) {
			for (int y = -4; y <= 12; y++) {
				for (int z = -16; z <= 16; z++) {
					Block block = level
							.getBlockState(
									wreck.centre()
											.offset(
													x,
													y,
													z))
							.getBlock();
					palette.merge(block, 1,
							Integer::sum);
				}
			}
		}
		Set<ResourceLocation> loot =
				new java.util.HashSet<>();
		for (BlockPos position
				: WaferWreckFeature
						.lootPositions(
								level.getSeed(),
								wreck.centre())) {
			BlockEntity chest =
					level.getBlockEntity(position);
			if (chest != null) {
				CompoundTag saved =
						chest.saveWithoutMetadata();
				String lootId =
						saved.getString("LootTable");
				if (!lootId.isEmpty()) {
					loot.add(new ResourceLocation(
							lootId));
				}
			}
		}
		ResourceLocation biomeId =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								wreck.centre())
								.value());
		Holder<Biome> biome =
				level.getBiome(wreck.centre());
		boolean literalEligible =
				biome.is(BiomeTags.HAS_SHIPWRECK)
						|| biome.is(
								BiomeTags
										.HAS_SHIPWRECK_BEACHED);
		return new WreckWorldAudit(
				palette, biomeId, loot,
				WaferWreckFeature.orientation(
						level.getSeed(),
						wreck.centre()),
				literalEligible);
	}

	private static LocatedCottage locateCaramelCottage(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located = level.findNearestMapFeature(
				CaramelCottageFeature.STRUCTURE_TAG,
				origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Caramel Cottage within 512 chunks of Cookie Forest");
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
				"The located Caramel Cottage lost its saved one-piece structure start");
		net.minecraft.world.level.levelgen.structure.BoundingBox
				bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX() + 7,
				bounds.minY(),
				bounds.minZ() + 7);
		return new LocatedCottage(
				located, centre, bounds);
	}

	private static LocatedCottage
			locateConfectionersCottage(
					GameTestHelper helper,
					ServerLevel level,
					ConfiguredStructureFeature<?, ?>
							configured,
					BlockPos origin) {
		BlockPos located =
				level.findNearestMapFeature(
						ConfectionersCottageFeature
								.STRUCTURE_TAG,
						origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Confectioner's Cottage within 512 chunks of Candy Plains");
		ChunkPos startChunk =
				new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1,
				"The located Confectioner's Cottage lost its saved one-piece structure start");
		BoundingBox bounds =
				start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX() + 8,
				bounds.minY(),
				bounds.minZ() + 8);
		return new LocatedCottage(
				located, centre, bounds);
	}

	private static LocatedCottage locateWaferWindmill(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located =
				level.findNearestMapFeature(
						WaferWindmillFeature
								.STRUCTURE_TAG,
						origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Wafer Windmill within 512 chunks of Candy Plains");
		ChunkPos startChunk =
				new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1,
				"The located Wafer Windmill lost its saved one-piece structure start");
		BoundingBox bounds =
				start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX()
						+ 10,
				bounds.minY(),
				bounds.minZ()
						+ 10);
		return new LocatedCottage(
				located, centre, bounds);
	}

	private static LocatedCottage locateCandyCaneBridge(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located =
				level.findNearestMapFeature(
						CandyCaneBridgeFeature
								.STRUCTURE_TAG,
						origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Candy-Cane Bridge within 512 chunks of Candy Plains");
		ChunkPos startChunk =
				new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1,
				"The located Candy-Cane Bridge lost its saved one-piece structure start");
		BoundingBox bounds =
				start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX()
						+ CandyCaneBridgeStructureFeature
								.CENTRE_OFFSET,
				bounds.minY(),
				bounds.minZ()
						+ CandyCaneBridgeStructureFeature
								.CENTRE_OFFSET);
		return new LocatedCottage(
				located, centre, bounds);
	}

	private static LocatedCottage locateCraterKitchen(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured,
			BlockPos origin) {
		BlockPos located =
				level.findNearestMapFeature(
						CraterKitchenFeature
								.STRUCTURE_TAG,
						origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Crater Kitchen within 512 chunks of Mooncake Barrens");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1,
				"The located Crater Kitchen lost its saved one-piece structure start");
		BoundingBox bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX()
						+ CraterKitchenStructureFeature
								.CENTRE_OFFSET,
				bounds.minY(),
				bounds.minZ()
						+ CraterKitchenStructureFeature
								.CENTRE_OFFSET);
		return new LocatedCottage(
				located, centre, bounds);
	}

	private static LocatedCottage
			locateRockCandyCrystalMine(
					GameTestHelper helper,
					ServerLevel level,
					ConfiguredStructureFeature<?, ?>
							configured,
					BlockPos origin) {
		BlockPos located =
				level.findNearestMapFeature(
						RockCandyCrystalMineFeature
								.STRUCTURE_TAG,
						origin, 512, false);
		require(helper, located != null,
				"The fixed-seed CakeWorld contained no locatable Rock-Candy Crystal Mine within 512 chunks of Candy-Cane Badlands");
		ChunkPos startChunk = new ChunkPos(located);
		net.minecraft.world.level.chunk.LevelChunk
				startLevelChunk =
				level.getChunk(startChunk.x,
						startChunk.z);
		StructureStart start =
				startLevelChunk.getStartForFeature(
						configured);
		require(helper,
				start != null && start.isValid()
						&& start.getFeature()
								== configured
						&& start.getPieces()
								.size() == 1,
				"The located Rock-Candy Crystal Mine lost its saved one-piece structure start");
		BoundingBox bounds = start.getBoundingBox();
		BlockPos centre = new BlockPos(
				bounds.minX()
						+ RockCandyCrystalMineStructureFeature
								.CENTRE_OFFSET,
				bounds.minY(),
				bounds.minZ()
						+ RockCandyCrystalMineStructureFeature
								.CENTRE_OFFSET);
		return new LocatedCottage(
				located, centre, bounds);
	}

	private static LocatedPicnic locateNaturalPicnic(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int kioskCandidates = 0;
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius;
					chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX
									!= anchorChunk.x
											- radius
							&& chunkX
									!= anchorChunk.x
											+ radius
							&& chunkZ
									!= anchorChunk.z
											- radius
							&& chunkZ
									!= anchorChunk.z
											+ radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					ChunkPos chunk =
							new ChunkPos(
									chunkX, chunkZ);
					for (int x = chunk
							.getMinBlockX();
							x <= chunk
									.getMaxBlockX();
							x++) {
						for (int z = chunk
								.getMinBlockZ();
								z <= chunk
										.getMaxBlockZ();
								z++) {
							int y = level.getHeight(
									Heightmap.Types
											.WORLD_SURFACE,
									x, z) - 1;
							BlockPos kiosk =
									new BlockPos(
											x, y, z);
							if (!level
									.getBlockState(
											kiosk)
									.is(CakeWorldBlocks
											.COOKBOOK_KIOSK
											.get())) {
								continue;
							}
							kioskCandidates++;
							BlockPos centre =
									kiosk.below();
							PicnicWorldAudit audit =
									auditNaturalPicnic(
											level,
											centre);
							int sponge =
									audit.palette()
											.getOrDefault(
													CakeWorldBlocks
															.CHOCOLATE_SPONGE
															.get(),
													0);
							if (audit.readableLayout()
									&& (sponge == 35
											|| sponge == 34)
									&& audit.palette()
											.getOrDefault(
													CakeWorldBlocks
															.BISCUIT_STONE
															.get(),
													0)
											== 61
									&& audit.palette()
											.getOrDefault(
													CakeWorldBlocks
															.ICING
															.get(),
													0)
											== 18) {
								return new LocatedPicnic(
										centre, kiosk,
										scannedChunks,
										kioskCandidates);
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Candy-Plains survey found no natural exact Picnic-Hamlet Cookbook Kiosk after "
						+ scannedChunks
						+ " generated chunks and "
						+ kioskCandidates
						+ " surface Kiosk candidates");
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static LocatedRoadside locateNaturalRoadside(
			GameTestHelper helper, ServerLevel level,
			RoadsideCuriosityFeature.Variant expected,
			BlockPos anchor,
			BlockPos fixedSeedCentre,
			int chunkRadius) {
		level.getChunkAt(fixedSeedCentre);
		LocatedRoadside fixedSeedScene =
				new LocatedRoadside(
						fixedSeedCentre,
						RoadsideCuriosityFeature
								.cachePosition(
										fixedSeedCentre),
						expected, 1, 1);
		if (auditNaturalRoadside(
				level, fixedSeedScene)
						.readableLayout()) {
			return fixedSeedScene;
		}
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int cacheCandidates = 0;
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX =
					anchorChunk.x - radius;
					chunkX <= anchorChunk.x
							+ radius;
					chunkX++) {
				for (int chunkZ =
						anchorChunk.z - radius;
						chunkZ <= anchorChunk.z
								+ radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX
									!= anchorChunk.x
											- radius
							&& chunkX
									!= anchorChunk.x
											+ radius
							&& chunkZ
									!= anchorChunk.z
											- radius
							&& chunkZ
									!= anchorChunk.z
											+ radius) {
						continue;
					}
					net.minecraft.world.level.chunk
							.LevelChunk chunk =
							level.getChunk(
									chunkX,
									chunkZ);
					scannedChunks++;
					for (BlockEntity blockEntity
							: chunk
									.getBlockEntities()
									.values()) {
						CompoundTag saved =
								blockEntity
										.saveWithoutMetadata();
						if (!RoadsideCuriosityFeature
								.LOOT_ID
								.toString()
								.equals(
										saved.getString(
												"LootTable"))) {
							continue;
						}
						cacheCandidates++;
						BlockPos cache =
								blockEntity
										.getBlockPos();
						BlockPos centre =
								cache.below();
						ResourceLocation biome =
								level.registryAccess()
										.registryOrThrow(
												Registry
														.BIOME_REGISTRY)
										.getKey(
												level.getBiome(
														centre)
														.value());
						if (RoadsideCuriosityFeature
								.variantForBiome(
										biome)
								!= expected) {
							continue;
						}
						LocatedRoadside scene =
								new LocatedRoadside(
										centre,
										cache,
										expected,
										scannedChunks,
										cacheCandidates);
						if (auditNaturalRoadside(
								level, scene)
										.readableLayout()) {
							return scene;
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed survey found no natural "
						+ expected
						+ " Roadside Curiosity after "
						+ scannedChunks
						+ " generated chunks and "
						+ cacheCandidates
						+ " dedicated cache candidates near "
						+ anchor);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static LocatedCookieGrove locateNaturalCookieGrove(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int cacheCandidates = 0;
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX =
					anchorChunk.x - radius;
					chunkX <= anchorChunk.x
							+ radius;
					chunkX++) {
				for (int chunkZ =
						anchorChunk.z - radius;
						chunkZ <= anchorChunk.z
								+ radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX
									!= anchorChunk.x
											- radius
							&& chunkX
									!= anchorChunk.x
											+ radius
							&& chunkZ
									!= anchorChunk.z
											- radius
							&& chunkZ
									!= anchorChunk.z
											+ radius) {
						continue;
					}
					net.minecraft.world.level.chunk
							.LevelChunk chunk =
							level.getChunk(
									chunkX,
									chunkZ);
					scannedChunks++;
					for (BlockEntity blockEntity
							: chunk
									.getBlockEntities()
									.values()) {
						CompoundTag saved =
								blockEntity
										.saveWithoutMetadata();
						if (!CookieCrumbGroveFeature
								.LOOT_ID
								.toString()
								.equals(
										saved.getString(
												"LootTable"))) {
							continue;
						}
						cacheCandidates++;
						BlockPos cache =
								blockEntity
										.getBlockPos();
						LocatedCookieGrove grove =
								new LocatedCookieGrove(
										cache.above(2),
										cache,
										scannedChunks,
										cacheCandidates);
						CookieGroveWorldAudit audit =
								auditNaturalCookieGrove(
										level, grove);
						LOGGER.info("Cookie Crumb Grove cache candidate: centre={}, cache={}, biome={}, rotation={}, palette={}, layout={}, scannedChunks={}, cacheCandidates={}",
								grove.centre(),
								grove.cache(),
								audit.biome(),
								audit.rotation(),
								audit.palette(),
								audit.readableLayout(),
								scannedChunks,
								cacheCandidates);
						if (audit.readableLayout()) {
							return grove;
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Cookie-Forest survey found no natural Cookie Crumb Grove after "
						+ scannedChunks
						+ " generated chunks and "
						+ cacheCandidates
						+ " dedicated cache candidates near "
						+ anchor);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static CookieGroveWorldAudit
			auditNaturalCookieGrove(
					ServerLevel level,
					LocatedCookieGrove grove) {
		BlockPos centre = grove.centre();
		Rotation rotation =
				CookieCrumbGroveFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel = local(centre, rotation,
				5, 3, 10);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -11; x <= 11; x++) {
			for (int y = -3; y <= 9; y++) {
				for (int z = -11; z <= 11;
						z++) {
					palette.merge(
							level.getBlockState(
									centre.offset(
											x, y, z))
									.getBlock(),
							1, Integer::sum);
				}
			}
		}
		BlockEntity cacheEntity =
				level.getBlockEntity(grove.cache());
		String cacheLoot =
				cacheEntity == null
						? ""
						: cacheEntity
								.saveWithoutMetadata()
								.getString(
										"LootTable");
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				level.getBlockState(grove.cache())
								.is(Blocks.CHEST)
						&& CookieCrumbGroveFeature
								.LOOT_ID.toString()
								.equals(cacheLoot)
						&& palette.getOrDefault(
								CakeWorldBlocks
										.WAFER_BLOCK
										.get(),
								0) >= 60
						&& palette.getOrDefault(
								CakeWorldBlocks
										.BISCUIT_STONE
										.get(),
								0) >= 220
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CHOCOLATE_SPONGE
										.get(),
								0) >= 40
						&& matchesCookieGroveLayout(
								level, centre,
								rotation);
		return new CookieGroveWorldAudit(
				palette, biome, rotation,
				cacheLoot, readable, sentinel,
				brickSentinel);
	}

	private static LocatedPeppermintClearing
			locateNaturalPeppermintClearing(
					GameTestHelper helper,
					ServerLevel level,
					BlockPos anchor,
					int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int crystalCandidates = 0;
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX =
					anchorChunk.x - radius;
					chunkX <= anchorChunk.x
							+ radius;
					chunkX++) {
				for (int chunkZ =
						anchorChunk.z - radius;
						chunkZ <= anchorChunk.z
								+ radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX
									!= anchorChunk.x
											- radius
							&& chunkX
									!= anchorChunk.x
											+ radius
							&& chunkZ
									!= anchorChunk.z
											- radius
							&& chunkZ
									!= anchorChunk.z
											+ radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					for (int x = chunkX << 4;
							x < (chunkX + 1) << 4;
							x++) {
						for (int z = chunkZ << 4;
								z < (chunkZ + 1) << 4;
								z++) {
							int surfaceY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							int minimumY = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 12);
							int maximumY = Math.min(
									level.getMaxBuildHeight()
											- 1,
									surfaceY + 8);
							for (int y = minimumY;
									y <= maximumY;
									y++) {
								BlockPos crystal =
										new BlockPos(
												x, y,
												z);
								if (!level
										.getBlockState(
												crystal)
										.is(CakeWorldBlocks
												.MINT_CRYSTAL
												.get())
										|| !level
												.getBlockState(
														crystal
																.below())
												.is(CakeWorldBlocks
														.CANDY_CANE_PILLAR
														.get())) {
									continue;
								}
								crystalCandidates++;
								LocatedPeppermintClearing
										clearing =
												new LocatedPeppermintClearing(
														crystal.below(
																3),
														scannedChunks,
														crystalCandidates);
								PeppermintClearingWorldAudit
										audit =
												auditNaturalPeppermintClearing(
														level,
														clearing);
								LOGGER.info("Peppermint Clearing crystal candidate: centre={}, biome={}, rotation={}, palette={}, layout={}, scannedChunks={}, crystalCandidates={}",
										clearing.centre(),
										audit.biome(),
										audit.rotation(),
										audit.palette(),
										audit.readableLayout(),
										scannedChunks,
										crystalCandidates);
								if (audit.readableLayout()
										&& CakeWorldBiomes
												.PEPPERMINT_PINEWOODS
												.getId()
												.equals(
														audit.biome())) {
									return clearing;
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Pinewoods survey found no natural Peppermint Clearing after "
						+ scannedChunks
						+ " generated chunks and "
						+ crystalCandidates
						+ " surface crystal candidates near "
						+ anchor);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static PeppermintClearingWorldAudit
			auditNaturalPeppermintClearing(
					ServerLevel level,
					LocatedPeppermintClearing
							clearing) {
		BlockPos centre = clearing.centre();
		Rotation rotation =
				PeppermintClearingFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel = local(centre, rotation,
				5, 2, 5);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 0; y <= 8; y++) {
				for (int z = -5; z <= 5; z++) {
					palette.merge(
							level.getBlockState(
									centre.offset(
											x, y, z))
									.getBlock(),
							1, Integer::sum);
				}
			}
		}
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				matchesPeppermintClearingLayout(
						level, centre, rotation)
						&& palette.getOrDefault(
								CakeWorldBlocks
										.ICING_LAYER
										.get(),
								0) >= 45
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get(),
								0) == 22
						&& palette.getOrDefault(
								CakeWorldBlocks.ICING
										.get(),
								0) >= 147
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_GLASS
										.get(),
								0) == 4
						&& palette.getOrDefault(
								CakeWorldBlocks
										.MINT_CRYSTAL
										.get(),
								0) == 1;
		return new PeppermintClearingWorldAudit(
				palette, biome, rotation,
				readable, sentinel,
				brickSentinel);
	}

	private static boolean
			matchesPeppermintClearingLayout(
					ServerLevel level,
					BlockPos centre,
					Rotation rotation) {
		if (!level.getBlockState(local(
				centre, rotation, 0, 3, 0))
				.is(CakeWorldBlocks.MINT_CRYSTAL.get())) {
			return false;
		}
		for (int y = 0; y <= 2; y++) {
			if (!level.getBlockState(local(
					centre, rotation, 0, y, 0))
					.is(CakeWorldBlocks
							.CANDY_CANE_PILLAR.get())) {
				return false;
			}
		}
		int[][] chimes = {
				{1, 0},
				{-1, 0},
				{0, 1},
				{0, -1}
		};
		for (int[] chime : chimes) {
			if (!level.getBlockState(local(
					centre, rotation,
					chime[0], 2, chime[1]))
					.is(CakeWorldBlocks
							.CANDY_GLASS.get())) {
				return false;
			}
		}
		int[][] trees = {
				{-3, -2, 6},
				{3, -2, 7},
				{0, 3, 6}
		};
		for (int[] tree : trees) {
			BlockPos base = local(centre, rotation,
					tree[0], 0, tree[1]);
			if (!level.getBlockState(base)
					.is(CakeWorldBlocks
							.CANDY_CANE_PILLAR.get())
					|| !level.getBlockState(
							base.above(
									tree[2] - 1))
							.is(CakeWorldBlocks
									.CANDY_CANE_PILLAR
									.get())
					|| !level.getBlockState(
							base.above(tree[2]))
							.is(CakeWorldBlocks.ICING
									.get())) {
				return false;
			}
		}
		return true;
	}

	private static LocatedGummyGrove
			locateNaturalGummyGrove(
					GameTestHelper helper,
					ServerLevel level,
					BlockPos anchor,
					int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int beaconCandidates = 0;
		int gummyColumns = 0;
		Map<Block, Integer> naturalSurfacePalette =
				new LinkedHashMap<>();
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX =
					anchorChunk.x - radius;
					chunkX <= anchorChunk.x
							+ radius;
					chunkX++) {
				for (int chunkZ =
						anchorChunk.z - radius;
						chunkZ <= anchorChunk.z
								+ radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX
									!= anchorChunk.x
											- radius
							&& chunkX
									!= anchorChunk.x
											+ radius
							&& chunkZ
									!= anchorChunk.z
											- radius
							&& chunkZ
									!= anchorChunk.z
											+ radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					for (int x = chunkX << 4;
							x < (chunkX + 1) << 4;
							x++) {
						for (int z = chunkZ << 4;
								z < (chunkZ + 1) << 4;
								z++) {
							int surfaceY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							BlockPos naturalSurface =
									findNaturalTerrainSurface(
											level, x, z,
											surfaceY);
							if (CakeWorldBiomes
									.GUMMY_JUNGLE
									.getId()
									.equals(level.getBiome(
											naturalSurface)
											.unwrapKey()
											.map(ResourceKey
													::location)
											.orElse(null))) {
								gummyColumns++;
								naturalSurfacePalette
										.merge(level
												.getBlockState(
														naturalSurface)
												.getBlock(),
												1,
												Integer::sum);
							}
							int minimumY = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 14);
							int maximumY = Math.min(
									level.getMaxBuildHeight()
											- 1,
									surfaceY + 2);
							for (int y = minimumY;
									y <= maximumY;
									y++) {
								BlockPos glass =
										new BlockPos(
												x, y,
												z);
								if (!level
										.getBlockState(
												glass)
										.is(CakeWorldBlocks
												.CANDY_GLASS
												.get())
										|| !level
												.getBlockState(
														glass
																.below())
												.is(CakeWorldBlocks
														.CANDY_CANE_PILLAR
														.get())
										|| !level
												.getBlockState(
														glass
																.above())
												.is(CakeWorldBlocks
														.GUMMY_BLOCK
														.get())) {
									continue;
								}
								beaconCandidates++;
								LocatedGummyGrove grove =
										new LocatedGummyGrove(
												glass.below(2),
												scannedChunks,
												beaconCandidates);
								GummyGroveWorldAudit
										audit =
												auditNaturalGummyGrove(
														level,
														grove);
								LOGGER.info("Gummy Jungle Bounce Grove beacon candidate: centre={}, biome={}, rotation={}, palette={}, layout={}, scannedChunks={}, beaconCandidates={}",
										grove.centre(),
										audit.biome(),
										audit.rotation(),
										audit.palette(),
										audit.readableLayout(),
										scannedChunks,
										beaconCandidates);
								if (audit.readableLayout()
										&& CakeWorldBiomes
												.GUMMY_JUNGLE
												.getId()
												.equals(
														audit.biome())) {
									return grove;
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Gummy Jungle survey found no natural Bounce Grove after "
						+ scannedChunks
						+ " generated chunks and "
						+ beaconCandidates
						+ " bubble-beacon candidates near "
						+ anchor
						+ "; gummyColumns="
						+ gummyColumns
						+ ", naturalSurfacePalette="
						+ describe(
								naturalSurfacePalette));
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static BlockPos findNaturalTerrainSurface(
			ServerLevel level, int x, int z,
			int startingY) {
		int y = Math.min(startingY,
				level.getMaxBuildHeight() - 1);
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos(x, y, z);
		while (y > level.getMinBuildHeight()) {
			BlockState state =
					level.getBlockState(cursor);
			if (!level.getFluidState(cursor).isEmpty()
					|| !state.getMaterial().isReplaceable()
							&& !state.is(BlockTags.LEAVES)
							&& !state.is(BlockTags.LOGS)) {
				break;
			}
			cursor.setY(--y);
		}
		return cursor.immutable();
	}

	private static LocatedCaramelMangrove
			locateNaturalCaramelMangrove(
					GameTestHelper helper,
					ServerLevel level,
					BlockPos anchor,
					int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int candidateCentres = 0;
		int caramelColumns = 0;
		Map<Block, Integer> naturalSurfacePalette =
				new LinkedHashMap<>();
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius;
					chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX != anchorChunk.x - radius
							&& chunkX != anchorChunk.x + radius
							&& chunkZ != anchorChunk.z - radius
							&& chunkZ != anchorChunk.z + radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					for (int x = chunkX << 4;
							x < (chunkX + 1) << 4; x++) {
						for (int z = chunkZ << 4;
								z < (chunkZ + 1) << 4; z++) {
							int surfaceY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							BlockPos naturalSurface =
									findNaturalTerrainSurface(
											level, x, z,
											surfaceY);
							if (CakeWorldBiomes.CARAMEL_BOGS
									.getId().equals(
											level.getBiome(
													naturalSurface)
													.unwrapKey()
													.map(ResourceKey
															::location)
													.orElse(null))) {
								caramelColumns++;
								naturalSurfacePalette.merge(
										level.getBlockState(
												naturalSurface)
												.getBlock(),
										1, Integer::sum);
							}
							int minimumY = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 10);
							for (int y = minimumY;
									y <= surfaceY; y++) {
								BlockPos centre =
										new BlockPos(x, y, z);
								Rotation rotation =
										CaramelBogMangroveFeature
												.orientation(
														level.getSeed(),
														centre);
								if (!matchesCaramelMangroveLayout(
										level, centre,
										rotation)) {
									continue;
								}
								candidateCentres++;
								LocatedCaramelMangrove located =
										new LocatedCaramelMangrove(
												centre,
												scannedChunks,
												candidateCentres);
								CaramelMangroveWorldAudit audit =
										auditNaturalCaramelMangrove(
												level, located);
								LOGGER.info("Caramel Bog Mangrove candidate: centre={}, biome={}, rotation={}, palette={}, layout={}, scannedChunks={}, candidateCentres={}",
										centre, audit.biome(),
										rotation, audit.palette(),
										audit.readableLayout(),
										scannedChunks,
										candidateCentres);
								if (audit.readableLayout()
										&& CakeWorldBiomes.CARAMEL_BOGS
												.getId().equals(
														audit.biome())) {
									return located;
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Caramel Bogs survey found no natural Mangrove after "
						+ scannedChunks + " generated chunks and "
						+ candidateCentres + " exact candidates near "
						+ anchor + "; caramelColumns="
						+ caramelColumns
						+ ", naturalSurfacePalette="
						+ describe(naturalSurfacePalette));
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static CaramelMangroveWorldAudit
			auditNaturalCaramelMangrove(
					ServerLevel level,
					LocatedCaramelMangrove mangrove) {
		BlockPos centre = mangrove.centre();
		Rotation rotation =
				CaramelBogMangroveFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel = local(
				centre, rotation, 4, 1, 4);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -4; x <= 4; x++) {
			for (int y = 0; y <= 9; y++) {
				for (int z = -4; z <= 4; z++) {
					palette.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(),
							1, Integer::sum);
				}
			}
		}
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				matchesCaramelMangroveLayout(
						level, centre, rotation)
						// The natural scan starts at the
						// surface, so the nine crust supports
						// beneath the contained pool are
						// deliberately outside this palette.
						&& palette.getOrDefault(
								CakeWorldBlocks.CARAMEL_CRUST
										.get(), 0) == 141
						&& palette.getOrDefault(
								CakeWorldBlocks
										.GINGERBREAD_BRICKS
										.get(), 0) == 25
						&& palette.getOrDefault(
								CakeWorldBlocks.TREACLE_REED
										.get(), 0) == 14
						&& palette.getOrDefault(
								CakeWorldBlocks.WAFER_BLOCK
										.get(), 0) == 9
						&& palette.getOrDefault(
								CakeWorldFluids.CARAMEL_BLOCK
										.get(), 0) == 9;
		return new CaramelMangroveWorldAudit(
				palette, biome, rotation,
				readable, sentinel, brickSentinel);
	}

	private static boolean matchesCaramelMangroveLayout(
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			if (!level.getBlockState(local(
					centre, rotation, x, 0, 2))
					.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
				return false;
			}
		}
		for (int x = -3; x <= -1; x++) {
			for (int z = -2; z <= 0; z++) {
				if (!level.getBlockState(local(
						centre, rotation, x, 0, z))
						.is(CakeWorldFluids.CARAMEL_BLOCK
								.get())) {
					return false;
				}
			}
		}
		int[][] trees = {
				{-3, 3, 5},
				{3, 2, 6},
				{2, -3, 5}
		};
		for (int[] tree : trees) {
			BlockPos root = local(centre, rotation,
					tree[0], 0, tree[1]);
			if (!level.getBlockState(root.above())
					.is(CakeWorldBlocks
							.GINGERBREAD_BRICKS.get())
					|| !level.getBlockState(
							root.above(tree[2] - 1))
							.is(CakeWorldBlocks
									.GINGERBREAD_BRICKS
									.get())
					|| !level.getBlockState(
							root.above(tree[2]))
							.is(CakeWorldBlocks
									.CARAMEL_CRUST.get())) {
				return false;
			}
		}
		int[][] reeds = {
				{-4, -2, 2}, {-4, 0, 3},
				{0, -2, 2}, {0, 0, 3},
				{-3, 1, 2}, {-1, 1, 2}
		};
		for (int[] reed : reeds) {
			BlockPos base = local(centre, rotation,
					reed[0], 1, reed[1]);
			for (int y = 0; y < reed[2]; y++) {
				if (!level.getBlockState(base.above(y))
						.is(CakeWorldBlocks
								.TREACLE_REED.get())) {
					return false;
				}
			}
		}
		return true;
	}

	private static LocatedSherbetFossilBowl
			locateNaturalSherbetFossilBowl(
					GameTestHelper helper,
					ServerLevel level,
					BlockPos anchor,
					int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int jarCandidates = 0;
		int sherbetColumns = 0;
		Map<Block, Integer> naturalSurfacePalette =
				new LinkedHashMap<>();
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius;
					chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX != anchorChunk.x - radius
							&& chunkX != anchorChunk.x + radius
							&& chunkZ != anchorChunk.z - radius
							&& chunkZ != anchorChunk.z + radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					for (int x = chunkX << 4;
							x < (chunkX + 1) << 4; x++) {
						for (int z = chunkZ << 4;
								z < (chunkZ + 1) << 4; z++) {
							int surfaceY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							BlockPos naturalSurface =
									findNaturalTerrainSurface(
											level, x, z,
											surfaceY);
							if (CakeWorldBiomes.SHERBET_DUNES
									.getId().equals(
											level.getBiome(
													naturalSurface)
													.unwrapKey()
													.map(ResourceKey
															::location)
													.orElse(null))) {
								sherbetColumns++;
								naturalSurfacePalette.merge(
										level.getBlockState(
												naturalSurface)
												.getBlock(),
										1, Integer::sum);
							}
							int minimumY = Math.max(
									level.getMinBuildHeight(),
									naturalSurface.getY() - 4);
							int maximumY = Math.min(
									level.getMaxBuildHeight() - 1,
									surfaceY + 6);
							for (int y = minimumY;
									y <= maximumY; y++) {
								BlockPos jar =
										new BlockPos(x, y, z);
								if (!level.getBlockState(jar)
										.is(Blocks.BARREL)) {
									continue;
								}
								BlockEntity blockEntity =
										level.getBlockEntity(jar);
								CompoundTag jarState =
										blockEntity == null
												? new CompoundTag()
												: blockEntity
														.saveWithoutMetadata();
								if (!SherbetFossilBowlFeature
										.LOOT_ID.toString()
										.equals(jarState
												.getString(
														"LootTable"))) {
									continue;
								}
								jarCandidates++;
								for (Rotation rotation :
										Rotation.values()) {
									BlockPos offset =
											new BlockPos(
													3, -1, 3)
													.rotate(
															rotation);
									BlockPos centre =
											jar.subtract(offset);
									if (!matchesSherbetFossilBowlLayout(
											level, centre,
											rotation)) {
										continue;
									}
									LocatedSherbetFossilBowl located =
											new LocatedSherbetFossilBowl(
													centre, jar,
													scannedChunks,
													jarCandidates,
													sherbetColumns);
									SherbetFossilBowlWorldAudit audit =
											auditNaturalSherbetFossilBowl(
													level,
													located);
									LOGGER.info("Sherbet Fossil Bowl candidate: centre={}, jar={}, biome={}, rotation={}, palette={}, layout={}, scannedChunks={}, jarCandidates={}, sherbetColumns={}",
											centre, jar,
											audit.biome(),
											rotation,
											audit.palette(),
											audit.readableLayout(),
											scannedChunks,
											jarCandidates,
											sherbetColumns);
									if (audit.readableLayout()
											&& CakeWorldBiomes
													.SHERBET_DUNES
													.getId()
													.equals(audit
															.biome())) {
										return located;
									}
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Sherbet Dunes survey found no natural Fossil Bowl after "
						+ scannedChunks + " generated chunks and "
						+ jarCandidates + " matching buried jars near "
						+ anchor + "; sherbetColumns="
						+ sherbetColumns
						+ ", naturalSurfacePalette="
						+ describe(naturalSurfacePalette));
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SherbetFossilBowlWorldAudit
			auditNaturalSherbetFossilBowl(
					ServerLevel level,
					LocatedSherbetFossilBowl bowl) {
		BlockPos centre = bowl.centre();
		Rotation rotation =
				SherbetFossilBowlFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel = local(
				centre, rotation, 5, 1, 5);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = -1; y <= 5; y++) {
				for (int z = -5; z <= 5; z++) {
					palette.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(),
							1, Integer::sum);
				}
			}
		}
		BlockEntity jarEntity =
				level.getBlockEntity(bowl.jar());
		CompoundTag jarState = jarEntity == null
				? new CompoundTag()
				: jarEntity.saveWithoutMetadata();
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				matchesSherbetFossilBowlLayout(
						level, centre, rotation)
						&& palette.getOrDefault(
								CakeWorldBlocks
										.RASPBERRY_SHERBET_POWDER
										.get(), 0) >= 28
						&& palette.getOrDefault(
								CakeWorldBlocks
										.ORANGE_SHERBET_POWDER
										.get(), 0) >= 27
						&& palette.getOrDefault(
								CakeWorldBlocks
										.LEMON_SHERBET_POWDER
										.get(), 0) >= 28
						&& palette.getOrDefault(
								CakeWorldBlocks
										.LIME_SHERBET_POWDER
										.get(), 0) >= 27
						&& palette.getOrDefault(
								CakeWorldBlocks.WAFER_BLOCK
										.get(), 0) >= 11
						&& palette.getOrDefault(
								CakeWorldBlocks
										.ROCK_CANDY_FOSSIL
										.get(), 0) >= 25
						&& palette.getOrDefault(
								CakeWorldBlocks.FIZZY_PEARL
										.get(), 0) >= 5
						&& palette.getOrDefault(
								Blocks.BARREL, 0) >= 1;
		return new SherbetFossilBowlWorldAudit(
				palette, biome, rotation,
				readable, jarState.getString("LootTable"),
				jarState.getString("CustomName"),
				sentinel, brickSentinel);
	}

	private static boolean matchesSherbetFossilBowlLayout(
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				Block expected = switch (
						Math.floorMod(x + z, 4)) {
					case 0 -> CakeWorldBlocks
							.RASPBERRY_SHERBET_POWDER.get();
					case 1 -> CakeWorldBlocks
							.ORANGE_SHERBET_POWDER.get();
					case 2 -> CakeWorldBlocks
							.LEMON_SHERBET_POWDER.get();
					default -> CakeWorldBlocks
							.LIME_SHERBET_POWDER.get();
				};
				Block actual = level.getBlockState(
						local(centre, rotation,
								x, 0, z))
						.getBlock();
				if (actual != (x == 0
						? CakeWorldBlocks.WAFER_BLOCK.get()
						: expected)) {
					return false;
				}
			}
		}
		for (int x = -3; x <= 3; x++) {
			if (!level.getBlockState(local(
					centre, rotation, x, 1, -1))
					.is(CakeWorldBlocks.ROCK_CANDY_FOSSIL
							.get())) {
				return false;
			}
		}
		for (int x : new int[] {-2, 0, 2}) {
			for (int side : new int[] {-1, 1}) {
				if (!level.getBlockState(local(
						centre, rotation,
						x, 1, -1 + side))
						.is(CakeWorldBlocks
								.ROCK_CANDY_FOSSIL.get())
						|| !level.getBlockState(local(
								centre, rotation,
								x, 2,
								-1 + side * 2))
								.is(CakeWorldBlocks
										.ROCK_CANDY_FOSSIL
										.get())
						|| !level.getBlockState(local(
								centre, rotation,
								x, 2,
								-1 + side * 3))
								.is(CakeWorldBlocks
										.ROCK_CANDY_FOSSIL
										.get())) {
					return false;
				}
			}
		}
		for (int[] marker : new int[][] {
				{-4, -4}, {-4, 4}, {4, -4},
				{4, 4}, {3, 3}
		}) {
			if (!level.getBlockState(local(
					centre, rotation,
					marker[0], 1, marker[1]))
					.is(CakeWorldBlocks.FIZZY_PEARL
							.get())) {
				return false;
			}
		}
		BlockPos jar = SherbetFossilBowlFeature
				.jarPosition(centre, rotation);
		BlockEntity jarEntity = level.getBlockEntity(jar);
		CompoundTag jarState = jarEntity == null
				? new CompoundTag()
				: jarEntity.saveWithoutMetadata();
		return level.getBlockState(jar).is(Blocks.BARREL)
				&& SherbetFossilBowlFeature.LOOT_ID
						.toString().equals(
								jarState.getString(
										"LootTable"))
				&& jarState.getString("CustomName")
						.contains("buried_sherbet_jar");
	}

	private static CandyCaneBadlandsGeologySurvey
			surveyCandyCaneBadlandsGeology(
					ServerLevel level,
					BlockPos anchor,
					int chunkRadius) {
		GeologySampler sampler = OreSpawnApi.createSampler(level)
				.orElseThrow();
		Map<ResourceLocation, Integer> geomes =
				new LinkedHashMap<>();
		Map<Block, Integer> naturalRocks =
				new LinkedHashMap<>();
		Set<Block> rockBlocks = Set.of(
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(),
				CakeWorldBlocks.BISCUIT_STONE.get(),
				CakeWorldBlocks.WAFER_ROCK.get(),
				CakeWorldBlocks.NOUGAT_ROCK.get(),
				CakeWorldBlocks.PEPPERMINT_ROCK.get(),
				CakeWorldBlocks.ROCK_CANDY.get(),
				CakeWorldBlocks.CANDY_GLASS.get());
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int biomeChunks = 0;
		int sampledRockCells = 0;
		for (int chunkX = anchorChunk.x - chunkRadius;
				chunkX <= anchorChunk.x + chunkRadius;
				chunkX++) {
			for (int chunkZ = anchorChunk.z - chunkRadius;
					chunkZ <= anchorChunk.z + chunkRadius;
					chunkZ++) {
				level.getChunk(chunkX, chunkZ);
				int x = (chunkX << 4) + 8;
				int z = (chunkZ << 4) + 8;
				int surfaceY = level.getHeight(
						Heightmap.Types.WORLD_SURFACE,
						x, z) - 1;
				GeologyColumn column = sampler.sampleColumn(
						x, z, surfaceY);
				if (!CakeWorldBiomes.CANDY_CANE_BADLANDS
						.getId().equals(column.biome())) {
					continue;
				}
				biomeChunks++;
				geomes.merge(column.geome(), 1,
						Integer::sum);
				int maximumY = Math.min(128,
						surfaceY - 8);
				for (int y = level.getMinBuildHeight() + 4;
						y <= maximumY; y += 4) {
					sampledRockCells++;
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (rockBlocks.contains(block)) {
						naturalRocks.merge(block, 1,
								Integer::sum);
					}
				}
			}
		}
		return new CandyCaneBadlandsGeologySurvey(
				geomes, naturalRocks,
				biomeChunks, sampledRockCells);
	}

	private static LocatedCandyCaneHoodooGarden
			locateNaturalCandyCaneHoodooGarden(
					GameTestHelper helper,
					ServerLevel level,
					BlockPos anchor,
					int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int badlandsColumns = 0;
		Map<Block, Integer> naturalSurfacePalette =
				new LinkedHashMap<>();
		for (int radius = 0;
				radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius;
					chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius;
						chunkZ++) {
					if (radius > 0
							&& chunkX != anchorChunk.x - radius
							&& chunkX != anchorChunk.x + radius
							&& chunkZ != anchorChunk.z - radius
							&& chunkZ != anchorChunk.z + radius) {
						continue;
					}
					level.getChunk(chunkX, chunkZ);
					scannedChunks++;
					for (int x = chunkX << 4;
							x < (chunkX + 1) << 4; x++) {
						for (int z = chunkZ << 4;
								z < (chunkZ + 1) << 4; z++) {
							int surfaceY = level.getHeight(
									Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
									x, z) - 1;
							BlockPos naturalSurface =
									findNaturalTerrainSurface(
											level, x, z,
											surfaceY);
							if (CakeWorldBiomes.CANDY_CANE_BADLANDS
									.getId().equals(
											level.getBiome(
													naturalSurface)
													.unwrapKey()
													.map(ResourceKey
															::location)
													.orElse(null))) {
								badlandsColumns++;
								naturalSurfacePalette.merge(
										level.getBlockState(
												naturalSurface)
												.getBlock(),
										1, Integer::sum);
							}
							int minimumY = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 16);
							for (int y = minimumY;
									y <= surfaceY; y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								Block markerBlock =
										level.getBlockState(
												marker)
												.getBlock();
								if (markerBlock
										!= CakeWorldBlocks
												.ROCK_CANDY_DEPOSIT
												.get()
										&& markerBlock
												!= CakeWorldBlocks
														.MINT_CRYSTAL
														.get()) {
									continue;
								}
								markerCandidates++;
								for (int[] hoodoo :
										CandyCaneHoodooGardenFeature
												.hoodoos()) {
									Block expected =
											hoodoo[3] == 0
													? CakeWorldBlocks
															.ROCK_CANDY_DEPOSIT
															.get()
													: CakeWorldBlocks
															.MINT_CRYSTAL
															.get();
									if (markerBlock != expected) {
										continue;
									}
									for (Rotation rotation :
											Rotation.values()) {
										BlockPos offset =
												new BlockPos(
														hoodoo[0],
														hoodoo[2] + 2,
														hoodoo[1])
														.rotate(
																rotation);
										BlockPos centre =
												marker.subtract(
														offset);
										if (CandyCaneHoodooGardenFeature
												.orientation(
														level.getSeed(),
														centre)
												!= rotation
												|| !matchesCandyCaneHoodooGardenLayout(
														level,
														centre,
														rotation)) {
											continue;
										}
										LocatedCandyCaneHoodooGarden
												located =
														new LocatedCandyCaneHoodooGarden(
																centre,
																scannedChunks,
																markerCandidates,
																badlandsColumns);
										CandyCaneHoodooGardenWorldAudit
												audit =
														auditNaturalCandyCaneHoodooGarden(
																level,
																located);
										LOGGER.info("Candy-Cane Hoodoo Garden marker candidate: centre={}, biome={}, rotation={}, palette={}, layout={}, scannedChunks={}, markerCandidates={}, badlandsColumns={}",
												centre,
												audit.biome(),
												rotation,
												describe(audit
														.palette()),
												audit.readableLayout(),
												scannedChunks,
												markerCandidates,
												badlandsColumns);
										if (audit.readableLayout()
												&& CakeWorldBiomes
														.CANDY_CANE_BADLANDS
														.getId()
														.equals(audit
																.biome())) {
											return located;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		require(helper, false,
				"The fixed-seed Candy-Cane Badlands survey found no natural Hoodoo Garden after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates + " crystal-marker candidates near "
						+ anchor + "; badlandsColumns="
						+ badlandsColumns
						+ ", naturalSurfacePalette="
						+ describe(naturalSurfacePalette));
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static CandyCaneHoodooGardenWorldAudit
			auditNaturalCandyCaneHoodooGarden(
					ServerLevel level,
					LocatedCandyCaneHoodooGarden garden) {
		BlockPos centre = garden.centre();
		Rotation rotation =
				CandyCaneHoodooGardenFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel = local(
				centre, rotation, 5, 1, 5);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		int blockEntities = 0;
		for (int x = -5; x <= 5; x++) {
			for (int y = 0; y <= 9; y++) {
				for (int z = -5; z <= 5; z++) {
					BlockPos position =
							centre.offset(x, y, z);
					palette.merge(level.getBlockState(
							position).getBlock(),
							1, Integer::sum);
					if (level.getBlockEntity(position) != null) {
						blockEntities++;
					}
				}
			}
		}
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				matchesCandyCaneHoodooGardenLayout(
						level, centre, rotation)
						&& palette.getOrDefault(
								CakeWorldBlocks.WAFER_BLOCK
										.get(), 0) == 11
						&& palette.getOrDefault(
								CakeWorldBlocks.MARSHMALLOW
										.get(), 0) == 2
						&& palette.getOrDefault(
								CakeWorldBlocks
										.ROCK_CANDY_DEPOSIT
										.get(), 0) == 2
						&& palette.getOrDefault(
								CakeWorldBlocks.MINT_CRYSTAL
										.get(), 0) == 2
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get(), 0) >= 22
						&& palette.getOrDefault(
								CakeWorldBlocks.WAFER_ROCK
										.get(), 0) >= 36
						&& palette.getOrDefault(
								CakeWorldBlocks
										.PEPPERMINT_ROCK
										.get(), 0) > 0
						&& palette.getOrDefault(
								CakeWorldBlocks.ROCK_CANDY
										.get(), 0) > 0;
		return new CandyCaneHoodooGardenWorldAudit(
				palette, biome, rotation,
				readable, blockEntities,
				sentinel, brickSentinel);
	}

	private static boolean
			matchesCandyCaneHoodooGardenLayout(
					ServerLevel level,
					BlockPos centre,
					Rotation rotation) {
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				Block expected;
				if (x == 0) {
					expected = CakeWorldBlocks.WAFER_BLOCK.get();
				} else if (z == 0 && Math.abs(x) == 1) {
					expected = CakeWorldBlocks.MARSHMALLOW.get();
				} else {
					expected = CandyCaneHoodooGardenFeature
							.courtState(x, z).getBlock();
				}
				if (!level.getBlockState(
						CandyCaneHoodooGardenFeature.local(
								centre, rotation,
								x, 0, z))
						.is(expected)) {
					return false;
				}
			}
		}
		for (int z : new int[] {-5, 5}) {
			if (!level.getBlockState(
					CandyCaneHoodooGardenFeature.local(
							centre, rotation,
							0, 0, z))
					.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
				return false;
			}
		}
		for (int[] hoodoo :
				CandyCaneHoodooGardenFeature.hoodoos()) {
			int x = hoodoo[0];
			int z = hoodoo[1];
			int height = hoodoo[2];
			for (int y = 1; y <= height; y++) {
				if (!level.getBlockState(
						CandyCaneHoodooGardenFeature.local(
								centre, rotation,
								x, y, z))
						.is(CakeWorldBlocks
								.CANDY_CANE_PILLAR.get())) {
					return false;
				}
			}
			for (int capX = -1; capX <= 1; capX++) {
				for (int capZ = -1; capZ <= 1; capZ++) {
					if (!level.getBlockState(
							CandyCaneHoodooGardenFeature
									.local(centre,
											rotation,
											x + capX,
											height + 1,
											z + capZ))
							.is(CakeWorldBlocks.WAFER_ROCK
									.get())) {
						return false;
					}
				}
			}
			Block marker = hoodoo[3] == 0
					? CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
					: CakeWorldBlocks.MINT_CRYSTAL.get();
			if (!level.getBlockState(
					CandyCaneHoodooGardenFeature.local(
							centre, rotation, x,
							height + 2, z))
					.is(marker)) {
				return false;
			}
		}
		return true;
	}

	private static GummyGroveWorldAudit
			auditNaturalGummyGrove(
					ServerLevel level,
					LocatedGummyGrove grove) {
		BlockPos centre = grove.centre();
		Rotation rotation =
				GummyJungleBounceGroveFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel = local(centre, rotation,
				5, 2, 5);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = -1; y <= 11; y++) {
				for (int z = -5; z <= 5; z++) {
					palette.merge(
							level.getBlockState(
									centre.offset(
											x, y, z))
									.getBlock(),
							1, Integer::sum);
				}
			}
		}
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				matchesGummyGroveLayout(
						level, centre, rotation)
						&& palette.getOrDefault(
								CakeWorldBlocks
										.RASPBERRY_GUMMY_BLOCK
										.get(),
								0) == 54
						&& palette.getOrDefault(
								CakeWorldBlocks
										.BLUEBERRY_GUMMY_BLOCK
										.get(),
								0) == 54
						&& palette.getOrDefault(
								CakeWorldBlocks
										.GRAPE_GUMMY_BLOCK
										.get(),
								0) == 54
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get(),
								0) == 23
						&& palette.getOrDefault(
								CakeWorldBlocks
										.GUMMY_VINE.get(),
								0) == 21
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_SPROUT
										.get(),
								0) == 4
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_GLASS.get(),
								0) == 1
						&& palette.getOrDefault(
								CakeWorldBlocks
										.GUMMY_BLOCK.get(),
								0) >= 38;
		return new GummyGroveWorldAudit(
				palette, biome, rotation,
				readable, sentinel,
				brickSentinel);
	}

	private static boolean matchesGummyGroveLayout(
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		if (!level.getBlockState(local(
				centre, rotation, 0, 1, 0))
				.is(CakeWorldBlocks
						.CANDY_CANE_PILLAR.get())
				|| !level.getBlockState(local(
						centre, rotation, 0, 2, 0))
						.is(CakeWorldBlocks
								.CANDY_GLASS.get())
				|| !level.getBlockState(local(
						centre, rotation, 0, 3, 0))
						.is(CakeWorldBlocks
								.GUMMY_BLOCK.get())
				|| !level.getBlockState(local(
						centre, rotation, 4, 0, 0))
						.is(CakeWorldBlocks
								.GUMMY_BLOCK.get())
				|| !hasSafeNaturalSupport(level,
						local(centre, rotation,
								4, -1, 0))) {
			return false;
		}
		if (!matchesGummyTree(level, centre,
				rotation, -3, -3, 7,
				CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get())
				|| !matchesGummyTree(level, centre,
						rotation, 3, -3, 8,
						CakeWorldBlocks
								.BLUEBERRY_GUMMY_BLOCK
								.get())
				|| !matchesGummyTree(level, centre,
						rotation, 0, 3, 7,
						CakeWorldBlocks
								.GRAPE_GUMMY_BLOCK
								.get())) {
			return false;
		}
		int[][] pools = {
				{-2, 1, 0},
				{2, 1, 1},
				{0, -1, 2}
		};
		Block[] flavours = {
				CakeWorldBlocks
						.RASPBERRY_GUMMY_BLOCK.get(),
				CakeWorldBlocks
						.BLUEBERRY_GUMMY_BLOCK.get(),
				CakeWorldBlocks
						.GRAPE_GUMMY_BLOCK.get()
		};
		int[][] cross = {
				{0, 0},
				{-1, 0},
				{1, 0},
				{0, -1},
				{0, 1}
		};
		for (int[] pool : pools) {
			for (int[] offset : cross) {
				if (!level.getBlockState(local(
						centre, rotation,
						pool[0] + offset[0], 0,
						pool[1] + offset[1]))
						.is(flavours[pool[2]])) {
					return false;
				}
			}
		}
		int[][] sprouts = {
				{-4, 0},
				{4, 0},
				{0, -4},
				{0, 4}
		};
		for (int[] sprout : sprouts) {
			if (!level.getBlockState(local(
					centre, rotation,
					sprout[0], 1, sprout[1]))
					.is(CakeWorldBlocks
							.CANDY_SPROUT.get())) {
				return false;
			}
		}
		return true;
	}

	private static boolean hasSafeNaturalSupport(
			ServerLevel level, BlockPos position) {
		BlockState state = level.getBlockState(position);
		return state.isFaceSturdy(
				level, position, Direction.UP)
				&& level.getFluidState(position).isEmpty();
	}

	private static boolean matchesGummyTree(
			ServerLevel level, BlockPos centre,
			Rotation rotation, int x, int z,
			int height, Block flavour) {
		BlockPos base = local(centre, rotation,
				x, 0, z);
		if (!level.getBlockState(base)
				.is(CakeWorldBlocks
						.CANDY_CANE_PILLAR.get())
				|| !level.getBlockState(
						base.above(height - 1))
						.is(CakeWorldBlocks
								.CANDY_CANE_PILLAR
								.get())
				|| !level.getBlockState(local(
						base, rotation,
						1, height, 0))
						.is(flavour)
				|| !level.getBlockState(
						base.above(height + 1))
						.is(CakeWorldBlocks
								.GUMMY_BLOCK.get())) {
			return false;
		}
		for (int step = 0; step < 3; step++) {
			if (!level.getBlockState(local(
					base, rotation, -2,
					height - 3 - step, 0))
					.is(CakeWorldBlocks
							.GUMMY_VINE.get())) {
				return false;
			}
		}
		for (int step = 0; step < 4; step++) {
			if (!level.getBlockState(local(
					base, rotation, 2,
					height - 3 - step, 0))
					.is(CakeWorldBlocks
							.GUMMY_VINE.get())) {
				return false;
			}
		}
		return true;
	}

	private static boolean matchesCookieGroveLayout(
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int x = -1; x <= 1; x++) {
			for (int z = 6; z <= 10; z++) {
				if (!level.getBlockState(local(
						centre, rotation,
						x, 0, z))
						.is(CakeWorldBlocks
								.BISCUIT_CRUMBS
								.get())) {
					return false;
				}
			}
		}
		if (!level.getBlockState(local(centre,
				rotation, 0, 1, 6)).isAir()
				|| !level.getBlockState(local(
						centre, rotation,
						0, 2, 6)).isAir()) {
			return false;
		}
		int[][] trees = {
				{-3, -2, 5},
				{3, -2, 6},
				{-3, 3, 6},
				{3, 3, 5}
		};
		for (int[] tree : trees) {
			BlockPos base = local(centre, rotation,
					tree[0], 0, tree[1]);
			if (!level.getBlockState(base)
					.is(CakeWorldBlocks
							.WAFER_BLOCK.get())
					|| !level.getBlockState(
							base.above(
									tree[2] - 2))
							.is(CakeWorldBlocks
									.WAFER_BLOCK
									.get())) {
				return false;
			}
		}
		return true;
	}

	private static RoadsideWorldAudit
			auditNaturalRoadside(
					ServerLevel level,
					LocatedRoadside scene) {
		BlockPos centre = scene.centre();
		Rotation rotation =
				RoadsideCuriosityFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel =
				RoadsideCuriosityFeature
						.sentinelPosition(
								centre,
								scene.variant(),
								rotation);
		boolean brickSentinel =
				level.getBlockState(sentinel)
						.is(Blocks.BRICKS);
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -4; x <= 4; x++) {
			for (int y = 0; y <= 5; y++) {
				for (int z = -4; z <= 4;
						z++) {
					palette.merge(
							level.getBlockState(
									centre.offset(
											x, y, z))
									.getBlock(),
							1, Integer::sum);
				}
			}
		}
		BlockEntity cacheEntity =
				level.getBlockEntity(scene.cache());
		String cacheLoot =
				cacheEntity == null
						? ""
						: cacheEntity
								.saveWithoutMetadata()
								.getString(
										"LootTable");
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		boolean readable =
				level.getBlockState(scene.cache())
								.is(Blocks.CHEST)
						&& RoadsideCuriosityFeature
								.LOOT_ID.toString()
								.equals(cacheLoot)
						&& matchesRoadsideLayout(
								level, centre,
								scene.variant(),
								rotation,
								sentinel);
		return new RoadsideWorldAudit(
				palette, biome, rotation,
				cacheLoot, readable, sentinel,
				brickSentinel);
	}

	private static boolean matchesRoadsideLayout(
			ServerLevel level, BlockPos centre,
			RoadsideCuriosityFeature.Variant variant,
			Rotation rotation, BlockPos sentinel) {
		switch (variant) {
		case SPILLED_SWEET_CART:
			for (int x = -2; x <= 2; x++) {
				for (int z = -1; z <= 1;
						z++) {
					if (!level.getBlockState(
							local(centre, rotation,
									x, 0, z))
							.is(CakeWorldBlocks
									.BISCUIT_CRUMBS
									.get())) {
						return false;
					}
				}
			}
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1;
						z++) {
					if ((x != 0 || z != 0)
							&& !matchesOrBrick(
									level,
									local(centre,
											rotation,
											x, 1,
											z),
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(),
									sentinel)) {
						return false;
					}
				}
			}
			for (int x : new int[] {-2, 2}) {
				if (!level.getBlockState(
						local(centre, rotation,
								x, 1, -1))
						.is(CakeWorldBlocks
								.RASPBERRY_GUMMY_BLOCK
								.get())
						|| !level.getBlockState(
								local(centre,
										rotation,
										x, 1,
										1))
								.is(CakeWorldBlocks
										.BLUEBERRY_GUMMY_BLOCK
										.get())) {
					return false;
				}
			}
			Direction.Axis handleAxis =
					rotation.rotate(
							Direction.SOUTH)
							.getAxis();
			for (int z = 2; z <= 3; z++) {
				BlockState handle =
						level.getBlockState(
								local(centre,
										rotation,
										0, 1,
										z));
				if (!handle.is(CakeWorldBlocks
						.CANDY_CANE_PILLAR.get())
						|| handle.getValue(
								RotatedPillarBlock
										.AXIS)
								!= handleAxis) {
					return false;
				}
			}
			return level.getBlockState(
					local(centre, rotation,
							-1, 0, 2))
					.is(CakeWorldBlocks
							.BISCUIT_CRUMBS.get())
					&& level.getBlockState(
							local(centre, rotation,
									1, 0, 2))
							.is(CakeWorldBlocks
									.BISCUIT_CRUMBS
									.get())
					&& level.getBlockState(
							local(centre, rotation,
									2, 0, 3))
							.is(CakeWorldBlocks
									.BISCUIT_CRUMBS
									.get())
					&& level.getBlockState(
							local(centre, rotation,
									2, 1, 2))
							.is(CakeWorldBlocks
									.GUMMY_BLOCK.get())
					&& level.getBlockState(
							local(centre, rotation,
									-1, 1, 3))
							.is(CakeWorldBlocks
									.GRAPE_GUMMY_BLOCK
									.get());
		case WRONG_WAY_SIGNPOST:
			Set<BlockPos> waferBase = Set.of(
					local(centre, rotation,
							-1, 0, -1),
					local(centre, rotation,
							0, 0, -1),
					local(centre, rotation,
							1, 0, -1));
			for (Direction direction
					: Direction.Plane.HORIZONTAL) {
				for (int distance = 0;
						distance <= 3;
						distance++) {
					BlockPos point =
							centre.relative(
									direction,
									distance);
					Block expected =
							waferBase
									.contains(point)
											? CakeWorldBlocks
													.WAFER_BLOCK
													.get()
											: CakeWorldBlocks
													.BISCUIT_CRUMBS
													.get();
					if (!level.getBlockState(
							point).is(expected)) {
						return false;
					}
				}
			}
			for (BlockPos base : waferBase) {
				if (!level.getBlockState(base)
						.is(CakeWorldBlocks
								.WAFER_BLOCK.get())) {
					return false;
				}
			}
			for (int y = 1; y <= 3; y++) {
				BlockState post =
						level.getBlockState(
								local(centre,
										rotation,
										0, y,
										-1));
				if (!post.is(CakeWorldBlocks
						.CANDY_CANE_PILLAR.get())
						|| post.getValue(
								RotatedPillarBlock
										.AXIS)
								!= Direction.Axis.Y) {
					return false;
				}
			}
			Direction.Axis arrowAxis =
					rotation.rotate(Direction.EAST)
							.getAxis();
			for (int x = -2; x <= 2; x++) {
				BlockPos arrow = local(
						centre, rotation,
						x, 4, -1);
				if (arrow.equals(sentinel)
						&& level.getBlockState(
								arrow)
								.is(Blocks.BRICKS)) {
					continue;
				}
				BlockState arrowState =
						level.getBlockState(arrow);
				if (!arrowState.is(
						CakeWorldBlocks
								.CANDY_CANE_PILLAR
								.get())
						|| arrowState.getValue(
								RotatedPillarBlock
										.AXIS)
								!= arrowAxis) {
					return false;
				}
			}
			return level.getBlockState(
					local(centre, rotation,
							-3, 4, -1))
					.is(CakeWorldBlocks
							.BLUEBERRY_GUMMY_BLOCK
							.get())
					&& level.getBlockState(
							local(centre, rotation,
									3, 4, -1))
							.is(CakeWorldBlocks
									.RASPBERRY_GUMMY_BLOCK
									.get())
					&& level.getBlockState(
							local(centre, rotation,
									0, 5, -1))
							.is(Blocks.LANTERN);
		case MARSHMALLOW_RESCUE_SHELTER:
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2;
						z++) {
					if (!level.getBlockState(
							local(centre,
									rotation,
									x, 0, z))
							.is(CakeWorldBlocks
									.MARSHMALLOW
									.get())
							|| !matchesOrBrick(
									level,
									local(centre,
											rotation,
											x, 4,
											z),
									CakeWorldBlocks
											.WAFER_BLOCK
											.get(),
									sentinel)
							|| !level.getBlockState(
									local(centre,
											rotation,
											x, 5,
											z))
									.is(CakeWorldBlocks
											.ICING_LAYER
											.get())) {
						return false;
					}
				}
			}
			for (int x : new int[] {-2, 2}) {
				for (int z
						: new int[] {-2, 2}) {
					for (int y = 1; y <= 3;
							y++) {
						BlockState support =
								level.getBlockState(
										local(
												centre,
												rotation,
												x, y,
												z));
						if (!support.is(
								CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get())
								|| support
										.getValue(
												RotatedPillarBlock
														.AXIS)
										!= Direction.Axis.Y) {
							return false;
						}
					}
				}
			}
			return level.getBlockState(
					local(centre, rotation,
							0, 3, 0))
					.is(Blocks.LANTERN)
					&& level.getBlockState(
							local(centre, rotation,
									0, 0, 3))
							.is(CakeWorldBlocks
									.BISCUIT_CRUMBS
									.get())
					&& level.getBlockState(
							local(centre, rotation,
									0, 0, 4))
							.is(CakeWorldBlocks
									.BISCUIT_CRUMBS
									.get());
		default:
			throw new IllegalStateException(
					"Unhandled roadside variant "
							+ variant);
		}
	}

	private static boolean matchesOrBrick(
			ServerLevel level, BlockPos position,
			Block expected, BlockPos sentinel) {
		return level.getBlockState(position)
						.is(expected)
				|| position.equals(sentinel)
						&& level.getBlockState(
								position)
								.is(Blocks.BRICKS);
	}

	private static void setRoadsideChunksForced(
			ServerLevel level,
			LocatedRoadside scene,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				scene.centre().getX() - 5, 16);
				chunkX <= Math.floorDiv(
						scene.centre().getX() + 5,
						16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					scene.centre().getZ() - 5,
					16);
					chunkZ <= Math.floorDiv(
							scene.centre().getZ()
									+ 5,
							16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ,
						forced);
			}
		}
	}

	private static void setCookieGroveChunksForced(
			ServerLevel level,
			LocatedCookieGrove grove,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				grove.centre().getX() - 11, 16);
				chunkX <= Math.floorDiv(
						grove.centre().getX() + 11,
						16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					grove.centre().getZ() - 11,
					16);
					chunkZ <= Math.floorDiv(
							grove.centre().getZ()
									+ 11,
							16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ,
						forced);
			}
		}
	}

	private static void setPeppermintClearingChunksForced(
			ServerLevel level,
			LocatedPeppermintClearing clearing,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				clearing.centre().getX() - 5, 16);
				chunkX <= Math.floorDiv(
						clearing.centre().getX() + 5,
						16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					clearing.centre().getZ() - 5, 16);
					chunkZ <= Math.floorDiv(
							clearing.centre().getZ()
									+ 5,
							16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ,
						forced);
			}
		}
	}

	private static void setGummyGroveChunksForced(
			ServerLevel level,
			LocatedGummyGrove grove,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				grove.centre().getX() - 5, 16);
				chunkX <= Math.floorDiv(
						grove.centre().getX() + 5,
						16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					grove.centre().getZ() - 5, 16);
					chunkZ <= Math.floorDiv(
							grove.centre().getZ()
									+ 5,
							16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ,
						forced);
			}
		}
	}

	private static void setCaramelMangroveChunksForced(
			ServerLevel level,
			LocatedCaramelMangrove mangrove,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				mangrove.centre().getX() - 5, 16);
				chunkX <= Math.floorDiv(
						mangrove.centre().getX() + 5,
						16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					mangrove.centre().getZ() - 5, 16);
					chunkZ <= Math.floorDiv(
							mangrove.centre().getZ() + 5,
							16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
			}
		}
	}

	private static void setSherbetFossilBowlChunksForced(
			ServerLevel level,
			LocatedSherbetFossilBowl bowl,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				bowl.centre().getX() - 6, 16);
				chunkX <= Math.floorDiv(
						bowl.centre().getX() + 6,
						16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					bowl.centre().getZ() - 6, 16);
					chunkZ <= Math.floorDiv(
							bowl.centre().getZ() + 6,
							16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
			}
		}
	}

	private static void setCandyCaneHoodooGardenChunksForced(
			ServerLevel level,
			LocatedCandyCaneHoodooGarden garden,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				garden.centre().getX() - 5, 16);
				chunkX <= Math.floorDiv(
						garden.centre().getX() + 5, 16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					garden.centre().getZ() - 5, 16);
					chunkZ <= Math.floorDiv(
							garden.centre().getZ() + 5, 16);
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
			}
		}
	}

	private static void setCottageChunksForced(
			ServerLevel level,
			LocatedCottage cottage,
			boolean forced) {
		int minimumChunkX = Math.floorDiv(
				cottage.bounds().minX(), 16);
		int maximumChunkX = Math.floorDiv(
				cottage.bounds().maxX(), 16);
		int minimumChunkZ = Math.floorDiv(
				cottage.bounds().minZ(), 16);
		int maximumChunkZ = Math.floorDiv(
				cottage.bounds().maxZ(), 16);
		for (int chunkX = minimumChunkX;
				chunkX <= maximumChunkX; chunkX++) {
			for (int chunkZ = minimumChunkZ;
					chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(
						chunkX, chunkZ, forced);
			}
		}
	}

	private static CottageWorldAudit auditCaramelCottage(
			ServerLevel level,
			LocatedCottage cottage) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -7; x <= 7; x++) {
			for (int y = -12; y <= 11; y++) {
				for (int z = -7; z <= 7; z++) {
					Block block = level
							.getBlockState(
									cottage.centre()
											.offset(
													x,
													y,
													z))
							.getBlock();
					palette.merge(block, 1,
							Integer::sum);
				}
			}
		}
		AABB residentArea = new AABB(
				cottage.centre().offset(
						-12, -4, -12),
				cottage.centre().offset(
						13, 16, 13));
		List<BitterBaker> bakers =
				level.getEntitiesOfClass(
						BitterBaker.class,
						residentArea).stream()
						.filter(BitterBaker
								::isPersistenceRequired)
						.toList();
		List<CustardCat> cats =
				level.getEntitiesOfClass(
						CustardCat.class,
						residentArea).stream()
						.filter(CustardCat
								::isPersistenceRequired)
						.toList();
		Rotation rotation =
				CaramelCottageFeature.orientation(
						level.getSeed(),
						cottage.centre());
		boolean markerConsumed =
				level.getBlockState(
						CaramelCottageFeature
								.residentMarker(
										cottage.centre(),
										rotation))
						.is(CakeWorldBlocks
								.GINGERBREAD_BRICKS
								.get());
		ResourceLocation biomeId =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								cottage.centre())
								.value());
		boolean literalEligible =
				level.getBiome(cottage.centre())
						.is(BiomeTags.HAS_SWAMP_HUT);
		List<BlockPos> storage =
				CaramelCottageFeature
						.fluidStoragePositions(
								level.getSeed(),
								cottage.centre());
		int syrupBuckets = 0;
		int caramelBuckets = 0;
		boolean randomLoot = false;
		for (int index = 0;
				index < storage.size(); index++) {
			BlockEntity storageEntity =
					level.getBlockEntity(
							storage.get(index));
			if (storageEntity != null) {
				randomLoot |= storageEntity
						.saveWithoutMetadata()
						.contains("LootTable");
			}
			if (!(storageEntity
					instanceof Container container)) {
				continue;
			}
			for (int slot = 0;
					slot < container
							.getContainerSize(); slot++) {
				ItemStack stack =
						container.getItem(slot);
				if (index == 0
						&& stack.is(
								CakeWorldFluids
										.SYRUP_BUCKET
										.get())) {
					syrupBuckets += stack
							.getCount();
				}
				if (index == 1
						&& stack.is(
								CakeWorldFluids
										.CARAMEL_BUCKET
										.get())) {
					caramelBuckets += stack
							.getCount();
				}
			}
		}
		return new CottageWorldAudit(
				palette, biomeId, rotation,
				bakers.size(), cats.size(),
				bakers.size() == 1
						&& bakers.get(0)
								.canJoinRaid(),
				cats.size() == 1
						&& cats.get(0)
								.getCatType()
								== Cat.TYPE_ALL_BLACK,
				markerConsumed,
				literalEligible,
				syrupBuckets,
				caramelBuckets,
				randomLoot);
	}

	private static CottageShopWorldAudit
			auditConfectionersCottage(
					ServerLevel level,
					LocatedCottage cottage) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (BlockPos position
				: BlockPos.betweenClosed(
						cottage.bounds().minX(),
						cottage.centre().getY() - 12,
						cottage.bounds().minZ(),
						cottage.bounds().maxX(),
						cottage.bounds().maxY(),
						cottage.bounds().maxZ())) {
			palette.merge(
					level.getBlockState(position)
							.getBlock(),
					1, Integer::sum);
		}
		Rotation rotation =
				ConfectionersCottageFeature
						.orientation(
								level.getSeed(),
								cottage.centre());
		List<TravellingConfectioner> residents =
				level.getEntitiesOfClass(
						TravellingConfectioner.class,
						new AABB(
								cottage.bounds().minX(),
								cottage.bounds().minY(),
								cottage.bounds().minZ(),
								cottage.bounds().maxX() + 1,
								cottage.bounds().maxY() + 1,
								cottage.bounds().maxZ() + 1))
						.stream()
						.filter(resident -> resident
								.getCustomName()
								instanceof net.minecraft.network.chat
										.TranslatableComponent
								&& "entity.cakeworld.cottage_confectioner"
										.equals(
												((net.minecraft.network.chat
														.TranslatableComponent)
														resident
																.getCustomName())
																.getKey()))
						.toList();
		TravellingConfectioner resident =
				residents.size() == 1
						? residents.get(0) : null;
		boolean namedResident =
				resident != null
						&& resident.getCustomName()
								instanceof net.minecraft.network.chat
										.TranslatableComponent
						&& "entity.cakeworld.cottage_confectioner"
								.equals(
										((net.minecraft.network.chat
												.TranslatableComponent)
												resident
														.getCustomName())
																.getKey());
		boolean earnFirstOffer =
				resident != null
						&& resident.getOffers().size()
								== 5
						&& resident.getOffers().get(0)
								.getBaseCostA().is(
										CakeWorldItems
												.CHOCOLATE_SPONGE_SLICE
												.get())
						&& resident.getOffers().get(0)
								.getBaseCostA()
								.getCount() == 10
						&& resident.getOffers().get(0)
								.getResult().is(
										Items.EMERALD);
		boolean starterSaleOffers =
				resident != null
						&& resident.getOffers().size()
								== 5
						&& resident.getOffers().get(1)
								.getResult().is(
										CakeWorldItems
												.SIMPLE_BISCUIT
												.get())
						&& resident.getOffers().get(2)
								.getResult().is(
										CakeWorldItems
												.BOILED_SWEET
												.get())
						&& resident.getOffers().get(3)
								.getResult().is(
										CakeWorldItems
												.LEMONADE_BOTTLE
												.get())
						&& resident.getOffers().get(4)
								.getResult().is(
										CakeWorldItems
												.SPRINKLE_SEEDS
												.get());
		BlockEntity stock =
				level.getBlockEntity(
						ConfectionersCottageFeature
								.stockChestPosition(
										level.getSeed(),
										cottage.centre()));
		String stockLoot =
				stock == null ? ""
						: stock.saveWithoutMetadata()
								.getString(
										"LootTable");
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								cottage.centre())
								.value());
		boolean markerConsumed =
				level.getBlockState(
						ConfectionersCottageFeature
								.residentMarker(
										cottage.centre(),
										rotation))
						.is(CakeWorldBlocks
								.GINGERBREAD_BRICKS
								.get());
		return new CottageShopWorldAudit(
				palette, biome, rotation,
				residents.size(),
				resident == null
						? null
						: resident.getUUID(),
				resident != null
						&& resident
								.isPersistenceRequired(),
				resident == null
						? -1
						: resident
								.getDespawnDelay(),
				namedResident,
				resident == null
						? 0
						: resident.getOffers().size(),
				resident == null
						|| resident.getOffers()
								.size() < 5
										? -1
										: resident
												.getOffers()
												.get(4)
												.getUses(),
				earnFirstOffer,
				starterSaleOffers,
				markerConsumed,
				stockLoot);
	}

	private static WindmillWorldAudit auditWaferWindmill(
			ServerLevel level,
			LocatedCottage windmill) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (BlockPos position
				: BlockPos.betweenClosed(
						windmill.bounds().minX(),
						windmill.centre().getY() - 12,
						windmill.bounds().minZ(),
						windmill.bounds().maxX(),
						windmill.bounds().maxY(),
						windmill.bounds().maxZ())) {
			palette.merge(
					level.getBlockState(position)
							.getBlock(),
					1, Integer::sum);
		}
		Rotation rotation =
				WaferWindmillFeature.orientation(
						level.getSeed(),
						windmill.centre());
		BlockPos hubPosition =
				WaferWindmillFeature
						.poweredHubPosition(
								level.getSeed(),
								windmill.centre());
		BlockState hub =
				level.getBlockState(hubPosition);
		boolean isHub =
				hub.is(CakeWorldBlocks
						.WAFER_WINDMILL.get());
		WaferWindmillBlock hubBlock =
				(WaferWindmillBlock)
						CakeWorldBlocks
								.WAFER_WINDMILL.get();
		BlockEntity pantry =
				level.getBlockEntity(
						WaferWindmillFeature
								.pantryPosition(
										level.getSeed(),
										windmill.centre()));
		String pantryLoot =
				pantry == null ? ""
						: pantry.saveWithoutMetadata()
								.getString(
										"LootTable");
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								windmill.centre())
								.value());
		return new WindmillWorldAudit(
				palette, biome, rotation,
				isHub,
				isHub && hub.getValue(
						WaferWindmillBlock.POWERED),
				isHub ? hub.getValue(
						WaferWindmillBlock.FACING)
						: Direction.NORTH,
				level.hasNeighborSignal(
						hubPosition),
				isHub && hubBlock
						.isSignalSource(hub),
				isHub ? hubBlock.getSignal(
						hub, level, hubPosition,
						Direction.UP) : -1,
				pantryLoot);
	}

	private static BridgeWorldAudit auditCandyCaneBridge(
			ServerLevel level,
			LocatedCottage bridge) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (BlockPos position
				: BlockPos.betweenClosed(
						bridge.bounds().minX(),
						bridge.centre().getY() - 3,
						bridge.bounds().minZ(),
						bridge.bounds().maxX(),
						bridge.bounds().maxY(),
						bridge.bounds().maxZ())) {
			palette.merge(
					level.getBlockState(position)
							.getBlock(),
					1, Integer::sum);
		}
		Rotation rotation =
				CandyCaneBridgeFeature.orientation(
						level.getSeed(),
						bridge.centre());
		BlockPos centre = bridge.centre();
		BlockPos channel =
				CandyCaneBridgeFeature.channelPosition(
						level.getSeed(), centre);
		BlockPos deck =
				CandyCaneBridgeFeature.deckPosition(
						level.getSeed(), centre);
		boolean channelContract =
				level.getBlockState(channel)
						.is(CakeWorldFluids
								.LEMONADE_BLOCK.get())
						&& level.getBlockState(
								channel.below())
								.is(CakeWorldFluids
										.LEMONADE_BLOCK
										.get())
						&& level.getBlockState(
								channel.below(2))
								.is(CakeWorldBlocks
										.BISCUIT_STONE
										.get());
		boolean deckContract =
				level.getBlockState(deck)
						.is(CakeWorldBlocks
								.WAFER_BLOCK.get());
		boolean clearance =
				level.getBlockState(
						deck.below()).isAir()
						&& level.getBlockState(
								deck.below(2)).isAir()
						&& level.getBlockState(
								deck.below(3)).isAir();

		BlockPos verticalPosition =
				centre.offset(new BlockPos(
						3, 4, 0)
								.rotate(rotation));
		BlockPos roadBeamPosition =
				centre.offset(new BlockPos(
						3, 5, -1)
								.rotate(rotation));
		BlockPos crossBeamPosition =
				centre.offset(new BlockPos(
						0, 2, 6)
								.rotate(rotation));
		Direction.Axis roadAxis =
				rotation.rotate(Direction.SOUTH)
						.getAxis();
		Direction.Axis crossAxis =
				rotation.rotate(Direction.EAST)
						.getAxis();
		boolean axes =
				level.getBlockState(verticalPosition)
								.is(CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get())
						&& level.getBlockState(
								verticalPosition)
								.getValue(
										RotatedPillarBlock
												.AXIS)
								== Direction.Axis.Y
						&& level.getBlockState(
								roadBeamPosition)
								.is(CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get())
						&& level.getBlockState(
								roadBeamPosition)
								.getValue(
										RotatedPillarBlock
												.AXIS)
								== roadAxis
						&& level.getBlockState(
								crossBeamPosition)
								.is(CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get())
						&& level.getBlockState(
								crossBeamPosition)
								.getValue(
										RotatedPillarBlock
												.AXIS)
								== crossAxis;

		BlockPos northApproach =
				centre.offset(new BlockPos(
						0, 0, -16)
								.rotate(rotation));
		BlockPos southApproach =
				centre.offset(new BlockPos(
						0, 0, 16)
								.rotate(rotation));
		boolean approaches =
				level.getBlockState(northApproach)
								.is(CakeWorldBlocks
										.BISCUIT_CRUMBS
										.get())
						&& level.getBlockState(
								southApproach)
								.is(CakeWorldBlocks
										.BISCUIT_CRUMBS
										.get());
		BlockPos northStair =
				centre.offset(new BlockPos(
						0, 0, -10)
								.rotate(rotation));
		BlockPos southStair =
				centre.offset(new BlockPos(
						0, 0, 10)
								.rotate(rotation));
		boolean stairs =
				level.getBlockState(northStair)
								.is(CakeWorldBlocks
										.WAFER_STAIRS
										.get())
						&& level.getBlockState(
								northStair)
								.getValue(
										StairBlock.FACING)
								== rotation.rotate(
										Direction.SOUTH)
						&& level.getBlockState(
								southStair)
								.is(CakeWorldBlocks
										.WAFER_STAIRS
										.get())
						&& level.getBlockState(
								southStair)
								.getValue(
										StairBlock.FACING)
								== rotation.rotate(
										Direction.NORTH);
		BlockState sentinel = level.getBlockState(
				CandyCaneBridgeFeature
						.reloadSentinelPosition(
								level.getSeed(), centre));
		boolean nativeSentinel =
				sentinel.is(CakeWorldBlocks
						.CANDY_CANE_PILLAR.get())
						&& sentinel.getValue(
								RotatedPillarBlock.AXIS)
								== roadAxis;
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(
								bridge.centre())
								.value());
		return new BridgeWorldAudit(
				palette, biome, rotation,
				channelContract, deckContract,
				clearance, axes, approaches,
				stairs, nativeSentinel);
	}

	private static CraterKitchenWorldAudit
			auditCraterKitchen(
					ServerLevel level,
					LocatedCottage kitchen) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (BlockPos position
				: BlockPos.betweenClosed(
						kitchen.bounds().minX(),
						kitchen.bounds().minY(),
						kitchen.bounds().minZ(),
						kitchen.bounds().maxX(),
						kitchen.bounds().maxY(),
						kitchen.bounds().maxZ())) {
			palette.merge(
					level.getBlockState(position)
							.getBlock(),
					1, Integer::sum);
		}
		Rotation rotation =
				CraterKitchenFeature.orientation(
						level.getSeed(),
						kitchen.centre());
		BlockPos centre = kitchen.centre();
		boolean floor =
				level.getBlockState(
						CraterKitchenFeature
								.craterFloorPosition(
										centre))
								.is(CakeWorldBlocks
										.MERINGUE_BRICKS
										.get())
						&& level.getBlockState(
								centre.above())
								.is(CakeWorldBlocks
										.WAFER_SLAB
										.get());
		BlockPos bowlSample =
				centre.offset(
						new BlockPos(6, 2, 0)
								.rotate(rotation));
		boolean bowl =
				level.getBlockState(bowlSample)
								.is(CakeWorldBlocks
										.MOONCAKE_CRUST
										.get())
						&& level.getBlockState(
								bowlSample.above())
								.isAir();
		BlockPos entrance =
				CraterKitchenFeature
						.entrancePosition(
								level.getSeed(),
								centre);
		boolean entranceContract =
				level.getBlockState(entrance)
								.is(CakeWorldBlocks
										.WAFER_STAIRS
										.get())
						&& entrance.getY()
								== centre.getY() + 5;
		boolean stairs = true;
		Direction stairFacing =
				rotation.rotate(Direction.SOUTH);
		for (int index = 0;
				index < 6; index++) {
			for (int x = -1; x <= 1; x++) {
				BlockPos position =
						centre.offset(
								new BlockPos(
										x,
										5 - index,
										-12 + index)
												.rotate(
														rotation));
				BlockState state =
						level.getBlockState(
								position);
				stairs &= state.is(
								CakeWorldBlocks
										.WAFER_STAIRS
										.get())
						&& state.getValue(
								StairBlock.FACING)
								== stairFacing;
			}
		}
		boolean safetyPads = true;
		for (int[] pad : new int[][] {
				{7, 0}, {-7, 0}, {0, 7}, {0, -7},
				{5, 5}, {5, -5}, {-5, 5}, {-5, -5}}) {
			BlockPos position =
					centre.offset(new BlockPos(
							pad[0],
							CraterKitchenFeature
									.craterFloorOffset(
											pad[0],
											pad[1]),
							pad[1]).rotate(rotation));
			safetyPads &= level
					.getBlockState(position)
					.is(CakeWorldBlocks
							.MARSHMALLOW.get());
		}
		int[][] stationOffsets = {
				{-3, 1, 2}, {-1, 1, 2},
				{1, 1, 2}, {3, 1, 2},
				{-3, 1, -1}
		};
		Block[] stationBlocks = {
				CakeWorldBlocks.OVEN.get(),
				CakeWorldBlocks.MIXING_BOWL.get(),
				CakeWorldBlocks.COOLING_RACK.get(),
				CakeWorldBlocks.CANDY_COOKER.get(),
				CakeWorldBlocks.COOKBOOK_LIBRARY.get()
		};
		boolean stations = true;
		for (int index = 0;
				index < stationOffsets.length; index++) {
			int[] offset = stationOffsets[index];
			BlockPos position =
					centre.offset(new BlockPos(
							offset[0], offset[1],
							offset[2]).rotate(
									rotation));
			stations &= level.getBlockState(position)
					.is(stationBlocks[index]);
		}
		BlockPos cache =
				CraterKitchenFeature.cachePosition(
						level.getSeed(), centre);
		BlockEntity cacheEntity =
				level.getBlockEntity(cache);
		String cacheLoot =
				cacheEntity == null ? ""
						: cacheEntity
								.saveWithoutMetadata()
								.getString(
										"LootTable");
		BlockState sentinel =
				level.getBlockState(
						CraterKitchenFeature
								.reloadSentinelPosition(
										level.getSeed(),
										centre));
		boolean nativeSentinel =
				sentinel.is(CakeWorldBlocks
						.MACARON_BRICKS.get());
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		return new CraterKitchenWorldAudit(
				palette, biome, rotation,
				floor, bowl, entranceContract,
				stairs, safetyPads, stations,
				nativeSentinel, cacheLoot);
	}

	private static CrystalMineWorldAudit
			auditRockCandyCrystalMine(
					ServerLevel level,
					LocatedCottage mine) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (BlockPos position
				: BlockPos.betweenClosed(
						mine.bounds().minX(),
						mine.bounds().minY(),
						mine.bounds().minZ(),
						mine.bounds().maxX(),
						mine.bounds().maxY(),
						mine.bounds().maxZ())) {
			palette.merge(
					level.getBlockState(position)
							.getBlock(),
					1, Integer::sum);
		}
		Rotation rotation =
				RockCandyCrystalMineFeature.orientation(
						level.getSeed(),
						mine.centre());
		BlockPos centre = mine.centre();
		BlockPos entrance =
				RockCandyCrystalMineFeature
						.entrancePosition(
								level.getSeed(),
								centre);
		boolean surfaceAccess =
				level.getBlockState(entrance)
								.is(CakeWorldBlocks
										.BISCUIT_STONE
										.get())
						&& entrance.getY()
								== centre.getY()
										+ RockCandyCrystalMineStructureFeature
												.SURFACE_OFFSET;
		Direction stairFacing =
				rotation.rotate(Direction.SOUTH);
		for (int x = -2; x <= 2; x++) {
			BlockPos stair = local(
					centre, rotation, x,
					RockCandyCrystalMineStructureFeature
							.SURFACE_OFFSET,
					-6);
			BlockState state =
					level.getBlockState(stair);
			surfaceAccess &= state.is(
							CakeWorldBlocks.WAFER_STAIRS
									.get())
					&& state.getValue(
							StairBlock.FACING)
							== stairFacing;
		}
		boolean shaft = true;
		Direction ladderFacing =
				rotation.rotate(Direction.SOUTH);
		for (int y = 1; y <= 35; y++) {
			BlockPos ladder = local(
					centre, rotation, 0, y, -13);
			BlockPos support = local(
					centre, rotation, 0, y, -14);
			BlockState ladderState =
					level.getBlockState(ladder);
			BlockState supportState =
					level.getBlockState(support);
			shaft &= ladderState.is(Blocks.LADDER)
					&& ladderState.getValue(
							LadderBlock.FACING)
							== ladderFacing
					&& supportState.is(
							CakeWorldBlocks
									.CANDY_CANE_PILLAR
									.get())
					&& supportState.getValue(
							RotatedPillarBlock.AXIS)
							== Direction.Axis.Y;
		}
		boolean safety = true;
		for (int[] pad : new int[][] {
				{0, 0}, {4, 0}, {-4, 0},
				{0, 4}, {0, -4}}) {
			safety &= level.getBlockState(
					local(centre, rotation,
							pad[0], 0, pad[1]))
					.is(CakeWorldBlocks
							.MARSHMALLOW.get());
		}
		for (int x = -1; x <= 1; x++) {
			for (int z = -13; z <= -11; z++) {
				safety &= level.getBlockState(
						local(centre, rotation,
								x, 0, z))
						.is(CakeWorldBlocks
								.MARSHMALLOW.get());
			}
		}
		boolean hostFamilies = true;
		for (int across = -4;
				across <= 4; across++) {
			for (int y = 1; y <= 6; y++) {
				hostFamilies &=
						level.getBlockState(
								local(centre,
										rotation,
										across, y,
										-16))
								.is(across < 0
										? CakeWorldBlocks
												.BISCUIT_STONE
												.get()
										: CakeWorldBlocks
												.WAFER_ROCK
												.get())
						&& level.getBlockState(
								local(centre,
										rotation,
										16, y,
										across))
								.is(CakeWorldBlocks
										.PEPPERMINT_ROCK
										.get())
						&& level.getBlockState(
								local(centre,
										rotation,
										across, y,
										16))
								.is(across < 0
										? CakeWorldBlocks
												.ROCK_CANDY
												.get()
										: CakeWorldBlocks
												.NOUGAT_ROCK
												.get())
						&& level.getBlockState(
								local(centre,
										rotation,
										-16, y,
										across))
								.is(across < 0
										? CakeWorldBlocks
												.FUDGE_ROCK
												.get()
										: CakeWorldBlocks
												.BURNT_SUGAR_ROCK
												.get());
			}
		}
		boolean patterns = true;
		for (int[] offset : new int[][] {
				{-2, 2, -15}, {-1, 2, -15},
				{0, 2, -15}, {1, 2, -15},
				{2, 2, -15}, {-1, 3, -15},
				{0, 3, -15}, {1, 3, -15},
				{0, 4, -15}}) {
			patterns &= level.getBlockState(
					local(centre, rotation,
							offset[0], offset[1],
							offset[2]))
					.is(CakeWorldBlocks.COCOA_CLOUD
							.get());
		}
		for (int x : new int[] {
				-3, -2, -1, 1, 2, 3}) {
			patterns &= level.getBlockState(
					local(centre, rotation,
							x, 1, -14))
					.is(CakeWorldBlocks.LIQUORICE_VEIN
							.get());
		}
		patterns &= level.getBlockState(
				local(centre, rotation,
						15, 3, 0))
				.is(CakeWorldBlocks.MINT_CRYSTAL.get());
		for (int[] offset : new int[][] {
				{0, 3, 14},
				{-1, 3, 14}, {1, 3, 14},
				{0, 2, 14}, {0, 4, 14},
				{-1, 2, 14}, {1, 2, 14},
				{-1, 4, 14}, {1, 4, 14},
				{0, 3, 15}, {-2, 3, 14},
				{2, 3, 14}}) {
			patterns &= level.getBlockState(
					local(centre, rotation,
							offset[0], offset[1],
							offset[2]))
					.is(CakeWorldBlocks
							.ROCK_CANDY_DEPOSIT.get());
		}
		patterns &= level.getBlockState(
				local(centre, rotation,
						0, 3, 13))
				.is(CakeWorldBlocks
						.ROCK_CANDY_DIAMOND.get());
		for (int[] offset : new int[][] {
				{-15, 2, -1}, {-15, 2, 0},
				{-15, 2, 1}, {-15, 3, 0},
				{-15, 3, 1}}) {
			patterns &= level.getBlockState(
					local(centre, rotation,
							offset[0], offset[1],
							offset[2]))
					.is(CakeWorldBlocks
							.SPRINKLE_CLUSTER.get());
		}
		patterns &= level.getBlockState(
				local(centre, rotation,
						-15, 4, 0))
				.is(CakeWorldBlocks
						.RICH_SPRINKLE_CLUSTER.get());
		BlockPos cache =
				RockCandyCrystalMineFeature
						.cachePosition(
								level.getSeed(),
								centre);
		BlockEntity cacheEntity =
				level.getBlockEntity(cache);
		String cacheLoot =
				cacheEntity == null ? ""
						: cacheEntity
								.saveWithoutMetadata()
								.getString(
										"LootTable");
		BlockState sentinel =
				level.getBlockState(
						RockCandyCrystalMineFeature
								.reloadSentinelPosition(
										level.getSeed(),
										centre));
		boolean nativeSentinel =
				sentinel.is(CakeWorldBlocks
						.WAFER_BLOCK.get());
		int headframeTop = level.getHeight(
				Heightmap.Types
						.MOTION_BLOCKING_NO_LEAVES,
				entrance.getX(),
				entrance.getZ()) - 1;
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		return new CrystalMineWorldAudit(
				palette, biome, rotation,
				surfaceAccess, shaft, safety,
				hostFamilies, patterns,
				nativeSentinel, headframeTop,
				cacheLoot);
	}

	private static BlockPos local(
			BlockPos centre, Rotation rotation,
			int x, int y, int z) {
		return centre.offset(
				new BlockPos(x, y, z)
						.rotate(rotation));
	}

	private static PicnicWorldAudit auditNaturalPicnic(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette =
				new LinkedHashMap<>();
		for (int x = -4; x <= 4; x++) {
			for (int y = 0; y <= 4; y++) {
				for (int z = -4; z <= 4; z++) {
					palette.merge(
							level.getBlockState(
									centre.offset(
											x, y, z))
									.getBlock(),
							1, Integer::sum);
				}
			}
		}
		boolean readableLayout =
				level.getBlockState(centre.above())
						.is(CakeWorldBlocks
								.COOKBOOK_KIOSK.get());
		for (int z = 0; z <= 4; z++) {
			readableLayout &= level.getBlockState(
					centre.offset(0, 0, z))
					.is(CakeWorldBlocks
							.BISCUIT_CRUMBS.get());
		}
		for (int shelterX : new int[] {-2, 2}) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					readableLayout &=
							level.getBlockState(
									centre.offset(
											shelterX
													+ x,
											3,
											-2 + z))
									.is(CakeWorldBlocks
											.ICING.get());
				}
			}
		}
		for (Direction direction
				: Direction.Plane.HORIZONTAL) {
			BlockPos seat = centre
					.relative(direction, 2)
					.above();
			readableLayout &=
					level.getBlockState(seat)
							.is(CakeWorldBlocks
									.CHOCOLATE_SPONGE
									.get())
					&& level.getBlockState(
							seat.above())
							.is(CakeWorldBlocks
									.ICING_LAYER.get());
		}
		List<CustardCat> companions =
				level.getEntitiesOfClass(
						CustardCat.class,
						new AABB(centre)
								.inflate(16.0D))
						.stream()
						.filter(companion ->
								companion.isTame()
										&& companion.isOrderedToSit()
										&& companion.isPersistenceRequired()
										&& companion.isInvulnerable())
						.toList();
		boolean homeRestricted = companions.stream()
				.anyMatch(companion -> companion.hasRestriction()
						&& companion.getRestrictCenter()
								.equals(centre));
		ResourceLocation biome =
				level.registryAccess()
						.registryOrThrow(
								Registry.BIOME_REGISTRY)
						.getKey(level.getBiome(centre)
								.value());
		return new PicnicWorldAudit(
				palette, biome, readableLayout,
				companions.size(), homeRestricted);
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
			int maximumDistanceSquared, long signature) {
	}

	private record RockDepthSummary(int samples, int minimumY, int maximumY,
			double meanY) {
	}

	private record LocatedSweetshop(
			BlockPos located,
			ResourceLocation configuredId,
			BoundingBox bounds,
			ChunkPos startChunk,
			StructureStart start) {
	}

	private record SweetshopWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Set<ResourceLocation> loot,
			Set<String> templates,
			Set<Float> integrities,
			int largePieces,
			int soggyBiscuits,
			int literalDrowned,
			boolean literalEligible) {
	}

	private record LocatedTin(
			BlockPos located,
			ChunkPos startChunk,
			StructureStart start) {
	}

	private record LocatedCitadel(
			BlockPos located,
			BoundingBox bounds,
			ChunkPos startChunk,
			StructureStart start) {
	}

	private record CitadelWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Map<String, Integer> templates,
			int maximumDepth,
			int shipPieces,
			Set<ResourceLocation> loot,
			int lootChests,
			int macaronClams,
			int literalShulkers,
			int elytraFrames,
			int dragonHeads,
			int enderChests,
			int brewingStands,
			int healingPotions,
			boolean literalEligible,
			BlockPos sentinel) {
	}

	private record LocatedFortress(
			BlockPos located,
			BoundingBox bounds,
			ChunkPos startChunk,
			StructureStart start) {
	}

	private record FortressWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Map<String, Integer> pieceKinds,
			int maximumDepth,
			Set<ResourceLocation> loot,
			int blazeSpawners,
			int cinnamonSparks,
			int literalBlazes,
			boolean literalEligible,
			BlockPos sentinel) {
	}

	private record LocatedFoundry(
			BlockPos located,
			BoundingBox bounds,
			ChunkPos startChunk,
			StructureStart start) {
	}

	private record FoundryWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Map<String, Integer> templates,
			String startFamily,
			Set<ResourceLocation> loot,
			int chests,
			int magmaCubeSpawners,
			int fudgeFolk,
			int fudgeBrutes,
			int fudgeBoars,
			int literalPiglins,
			int literalBrutes,
			int literalHoglins,
			boolean literalEligible,
			BlockPos sentinel) {
	}

	private record LocatedPalace(
			BlockPos located,
			BoundingBox bounds,
			int pieces,
			ChunkPos startChunk,
			StructureStart start,
			StructurePiece building) {
		private AABB boundsAabb() {
			return new AABB(
					bounds.minX(), bounds.minY(),
					bounds.minZ(),
					bounds.maxX() + 1,
					bounds.maxY() + 1,
					bounds.maxZ() + 1);
		}
	}

	private record PalaceWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			int childPieces,
			int spongeRooms,
			int grandGuardians,
			int literalElders,
			boolean literalEligible) {
	}

	private record LocatedVault(
			BlockPos located,
			BlockPos eyeLocated,
			BoundingBox bounds,
			int pieces,
			int portalRooms,
			int libraries,
			int corridors,
			int junctions,
			int maximumDepth,
			ChunkPos startChunk,
			StructurePiece portalRoom,
			StructurePiece library,
			StructurePiece corridor) {
	}

	private record VaultPieceAudit(
			Map<Block, Integer> palette,
			Set<ResourceLocation> loot,
			List<BlockPos> frames,
			String spawnerEntity) {
	}

	private record LocatedArch(
			BlockPos located, BlockPos centre,
			net.minecraft.world.level.levelgen.structure.BoundingBox
					bounds) {
	}

	private record ArchWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome, boolean loot,
			boolean repairable,
			boolean literalEligible) {
	}

	private record LocatedWreck(
			BlockPos located, BlockPos centre,
			net.minecraft.world.level.levelgen.structure.BoundingBox
					bounds) {
	}

	private record WreckWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Set<ResourceLocation> loot,
			net.minecraft.world.level.block.Rotation
					orientation,
			boolean literalEligible) {
	}

	private record LocatedCottage(
			BlockPos located, BlockPos centre,
			net.minecraft.world.level.levelgen.structure.BoundingBox
					bounds) {
	}

	private record CottageWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation orientation,
			int persistentBakers,
			int persistentCats,
			boolean raidCapableBaker,
			boolean allBlackCat,
			boolean markerConsumed,
			boolean literalEligible,
			int syrupBuckets,
			int caramelBuckets,
			boolean randomLoot) {
	}

	private record CottageShopWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation orientation,
			int residents,
			UUID residentId,
			boolean persistentResident,
			int despawnDelay,
			boolean namedResident,
			int offers,
			int seedUses,
			boolean earnFirstOffer,
			boolean starterSaleOffers,
			boolean markerConsumed,
			String stockLoot) {
	}

	private record WindmillWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation orientation,
			boolean hub,
			boolean powered,
			Direction facing,
			boolean neighborSignal,
			boolean signalSource,
			int signal,
			String pantryLoot) {
	}

	private record BridgeWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation orientation,
			boolean channel,
			boolean deck,
			boolean clearance,
			boolean axes,
			boolean approaches,
			boolean stairs,
			boolean nativeSentinel) {
	}

	private record CraterKitchenWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation orientation,
			boolean floor,
			boolean bowl,
			boolean entrance,
			boolean stairs,
			boolean safetyPads,
			boolean stations,
			boolean nativeSentinel,
			String cacheLoot) {
	}

	private record CrystalMineWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation orientation,
			boolean surfaceAccess,
			boolean shaft,
			boolean safety,
			boolean hostFamilies,
			boolean patterns,
			boolean nativeSentinel,
			int headframeTop,
			String cacheLoot) {
	}

	private record LocatedPicnic(
			BlockPos centre, BlockPos kiosk,
			int scannedChunks, int kioskCandidates) {
	}

	private record PicnicWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			boolean readableLayout,
			int persistentCompanions,
			boolean homeRestricted) {
	}

	private record LocatedRoadside(
			BlockPos centre, BlockPos cache,
			RoadsideCuriosityFeature.Variant variant,
			int scannedChunks,
			int cacheCandidates) {
	}

	private record RoadsideWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			String cacheLoot,
			boolean readableLayout,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record LocatedCookieGrove(
			BlockPos centre, BlockPos cache,
			int scannedChunks,
			int cacheCandidates) {
	}

	private record CookieGroveWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			String cacheLoot,
			boolean readableLayout,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record LocatedPeppermintClearing(
			BlockPos centre, int scannedChunks,
			int crystalCandidates) {
	}

	private record PeppermintClearingWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			boolean readableLayout,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record LocatedGummyGrove(
			BlockPos centre, int scannedChunks,
			int beaconCandidates) {
	}

	private record GummyGroveWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			boolean readableLayout,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record LocatedCaramelMangrove(
			BlockPos centre, int scannedChunks,
			int candidateCentres) {
	}

	private record CaramelMangroveWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			boolean readableLayout,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record LocatedSherbetFossilBowl(
			BlockPos centre,
			BlockPos jar,
			int scannedChunks,
			int jarCandidates,
			int sherbetColumns) {
	}

	private record SherbetFossilBowlWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			boolean readableLayout,
			String jarLoot,
			String customName,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record LocatedCandyCaneHoodooGarden(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int badlandsColumns) {
	}

	private record CandyCaneHoodooGardenWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			boolean readableLayout,
			int blockEntities,
			BlockPos sentinel,
			boolean brickSentinel) {
	}

	private record CandyCaneBadlandsGeologySurvey(
			Map<ResourceLocation, Integer> geomes,
			Map<Block, Integer> naturalRocks,
			int biomeChunks,
			int sampledRockCells) {
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

	private record FamilyHostAttributionResult(int outputs, int violations,
			int nonReplaceableControls, BlockPos firstViolation,
			String firstViolationDetail,
			Map<ResourceLocation, Integer> outputsByBlock,
			Map<ResourceLocation, Integer> violationsByBlock,
			List<String> violationDetails) {
	}

	private record SurfaceAudit(int biomeColumns, int topMatches,
			int fillerMatches) {
	}

	private record PearlAttribution(
			int total,
			int underLemonade,
			int waferReefTreasures,
			int waferReefTreasuresUnderLemonade) {
		int unattributed() {
			return total - waferReefTreasures;
		}

		int unattributedUnderLemonade() {
			return underLemonade
					- waferReefTreasuresUnderLemonade;
		}
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
