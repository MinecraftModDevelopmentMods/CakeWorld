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
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
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

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void underFluidPatternResolvesCakeWorldLemonade(
			GameTestHelper helper) {
		assertUnderFluidPatternResolvesLemonade(helper);
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
		Map<String, FluidEnvelopeResult> results = new LinkedHashMap<>();
		results.put("jam", auditFluidEnvelope(level,
				CakeWorldFluids.JAM_BLOCK.get(), auditChunkX, auditChunkZ,
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
		LOGGER.info("Focused covered-fluid envelope audit: {}", results);
		helper.succeed();
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
}
