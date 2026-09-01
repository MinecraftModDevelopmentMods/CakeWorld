package com.mcmoddev.cakeworld.gametest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/**
 * Fresh-world guard against literal vanilla geology leaking into CakeWorld.
 *
 * <p>The exception list is intentionally explicit. Bedrock remains Minecraft's
 * functional world boundary; air, fluids, structures and vegetation are outside
 * this geology-focused gate and retain their own conversion contracts. Any new
 * natural terrain or mineral source must be added to the unexpected lists, not
 * silently treated as acceptable.</p>
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class EdibleWorldIntegrityGameTests {
	private static final String EMPTY = "empty";
	private static final int COMPONENT_SAMPLE_LIMIT = 4096;
	private static final int COMPONENT_REPORT_LIMIT = 12;
	private static final int POST_LOAD_SETTLE_TICKS = 20;
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Set<Block> KNOWN_VANILLA_EXCEPTIONS =
			Set.of(Blocks.BEDROCK);
	private static final Set<Block> OVERWORLD_UNEXPECTED = Set.of(
			Blocks.STONE, Blocks.DEEPSLATE, Blocks.GRANITE,
			Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF,
			Blocks.CALCITE, Blocks.DRIPSTONE_BLOCK,
			Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT,
			Blocks.PODZOL, Blocks.ROOTED_DIRT, Blocks.GRAVEL,
			Blocks.SAND, Blocks.RED_SAND, Blocks.CLAY,
			Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA,
			Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA,
			Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
			Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA,
			Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA,
			Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA,
			Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
			Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA,
			Blocks.BLACK_TERRACOTTA, Blocks.COAL_ORE,
			Blocks.DEEPSLATE_COAL_ORE, Blocks.IRON_ORE,
			Blocks.DEEPSLATE_IRON_ORE, Blocks.COPPER_ORE,
			Blocks.DEEPSLATE_COPPER_ORE, Blocks.GOLD_ORE,
			Blocks.DEEPSLATE_GOLD_ORE, Blocks.REDSTONE_ORE,
			Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE,
			Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DIAMOND_ORE,
			Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.EMERALD_ORE,
			Blocks.DEEPSLATE_EMERALD_ORE, Blocks.INFESTED_STONE,
			Blocks.INFESTED_DEEPSLATE, Blocks.INFESTED_COBBLESTONE,
			Blocks.INFESTED_STONE_BRICKS, Blocks.AMETHYST_BLOCK,
			Blocks.BUDDING_AMETHYST, Blocks.SMOOTH_BASALT,
			Blocks.MAGMA_BLOCK);
	private static final Set<Block> NETHER_UNEXPECTED = Set.of(
			Blocks.STONE, Blocks.NETHERRACK, Blocks.BASALT, Blocks.SMOOTH_BASALT,
			Blocks.BLACKSTONE, Blocks.GRAVEL, Blocks.SOUL_SAND,
			Blocks.SOUL_SOIL, Blocks.MAGMA_BLOCK,
			Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE,
			Blocks.ANCIENT_DEBRIS);
	private static final Set<Block> END_UNEXPECTED = Set.of(Blocks.END_STONE);
	private static final Set<ResourceKey<PlacedFeature>>
			VANILLA_INEDIBLE_FEATURES = Set.of(
					feature("ore_dirt"), feature("ore_gravel"),
					feature("ore_granite_upper"),
					feature("ore_granite_lower"),
					feature("ore_diorite_upper"),
					feature("ore_diorite_lower"),
					feature("ore_andesite_upper"),
					feature("ore_andesite_lower"),
					feature("ore_tuff"), feature("ore_coal_upper"),
					feature("ore_coal_lower"), feature("ore_iron_upper"),
					feature("ore_iron_middle"), feature("ore_iron_small"),
					feature("ore_gold_extra"), feature("ore_gold"),
					feature("ore_gold_lower"), feature("ore_redstone"),
					feature("ore_redstone_lower"), feature("ore_diamond"),
					feature("ore_diamond_large"),
					feature("ore_diamond_buried"), feature("ore_lapis"),
					feature("ore_lapis_buried"), feature("ore_infested"),
					feature("ore_emerald"), feature("ore_copper"),
					feature("ore_copper_large"), feature("ore_clay"),
					feature("disk_sand"), feature("disk_gravel"),
					feature("disk_clay"), feature("underwater_magma"),
					feature("amethyst_geode"),
					feature("delta"), feature("ore_magma"),
					feature("ore_soul_sand"),
					feature("ore_gold_deltas"),
					feature("ore_quartz_deltas"),
					feature("ore_gold_nether"),
					feature("ore_quartz_nether"),
					feature("ore_gravel_nether"),
					feature("ore_blackstone"),
					feature("ore_ancient_debris_large"),
					feature("ore_debris_small"));

	private EdibleWorldIntegrityGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void cakeWorldBiomesOwnNaturalGeologyFeatures(
			GameTestHelper helper) {
		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Map<ResourceLocation, ResourceLocation> leaks = new LinkedHashMap<>();
		for (Map.Entry<ResourceKey<Biome>, Biome> entry
				: biomes.entrySet()) {
			if (!CakeWorld.MODID.equals(
					entry.getKey().location().getNamespace())) {
				continue;
			}
			for (Holder<PlacedFeature> placed
					: entry.getValue().getGenerationSettings().features()
							.stream().flatMap(features -> features.stream())
							.toList()) {
				for (ResourceKey<PlacedFeature> unexpected
						: VANILLA_INEDIBLE_FEATURES) {
					if (placed.is(unexpected)) {
						leaks.putIfAbsent(entry.getKey().location(),
								unexpected.location());
					}
				}
				placed.unwrapKey().map(ResourceKey::location)
						.filter(id -> "minecraft".equals(id.getNamespace()))
						.map(ResourceLocation::getPath)
						.filter(path -> path.startsWith("trees_"))
						.ifPresent(path -> leaks.putIfAbsent(
								entry.getKey().location(),
								new ResourceLocation("minecraft", path)));
			}
		}
		require(helper, leaks.isEmpty(),
				"CakeWorld biomes retained vanilla geology features: " + leaks);
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "edibleworldintegrity",
			timeoutTicks = 24000)
	public static void fixedWorldContainsNoUnexpectedNaturalBlocks(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel overworld = helper.getLevel().getServer()
				.getLevel(Level.OVERWORLD);
		ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
		ServerLevel end = helper.getLevel().getServer().getLevel(Level.END);
		require(helper, overworld != null && nether != null && end != null,
				"Edible-world integrity scan could not resolve all dimensions");
		preload(overworld, 16, 16, 5);
		preload(nether, 16, 16, 5);
		preload(end, 16, 16, 5);

		// Several CakeWorld structure palettes deliberately run on the live server
		// thread after chunk load. Audit the settled playable state, not the
		// transient structure blocks that exist between load and that bounded pass.
		helper.runAfterDelay(POST_LOAD_SETTLE_TICKS, () -> {
			Audit overworldAudit = scan(overworld, OVERWORLD_UNEXPECTED,
					16, 16, 4, -64, 160);
			Audit netherAudit = scan(nether, NETHER_UNEXPECTED,
					16, 16, 4, 0, 127);
			Audit endAudit = scan(end, END_UNEXPECTED,
					16, 16, 4, 0, 255);
			Map<Block, Integer> overworldResources = count(overworld, Set.of(
					CakeWorldBlocks.COCOA_COAL.get(),
					CakeWorldBlocks.IRON_WAFER.get(),
					CakeWorldBlocks.COPPER_CARAMEL.get(),
					CakeWorldBlocks.HONEYCOMB_GOLD.get(),
					CakeWorldBlocks.RASPBERRY_REDSTONE.get(),
					CakeWorldBlocks.BLUEBERRY_LAPIS.get(),
					CakeWorldBlocks.ROCK_CANDY_DIAMOND.get(),
					CakeWorldBlocks.MINT_EMERALD.get()), 16, 16, 4, -64, 256);
			Map<Block, Integer> netherResources = count(nether, Set.of(
					CakeWorldBlocks.VANILLA_QUARTZ.get(),
					CakeWorldBlocks.FUDGE_GOLD.get(),
					CakeWorldBlocks.ANCIENT_NOUGAT.get()), 16, 16, 4, 0, 127);

			LOGGER.info("Edible-world integrity audit: exceptions={}, overworld={}, nether={}, end={}, overworld_resources={}, nether_resources={}",
					KNOWN_VANILLA_EXCEPTIONS, overworldAudit.summary(),
					netherAudit.summary(), endAudit.summary(),
					describe(overworldResources), describe(netherResources));
			require(helper, overworldAudit.isEmpty(),
					"Overworld contains unexpected natural blocks: "
							+ overworldAudit.failureSummary());
			require(helper, netherAudit.isEmpty(),
					"Nether contains unexpected natural blocks: "
							+ netherAudit.failureSummary());
			require(helper, endAudit.isEmpty(),
					"End contains unexpected natural blocks: "
							+ endAudit.failureSummary());
			require(helper, overworldResources.size() == 8,
					"Themed Overworld resources did not replace every native role: "
							+ describe(overworldResources));
			require(helper, netherResources.size() == 3,
					"Themed Nether resources did not replace every native role: "
							+ describe(netherResources));
			helper.succeed();
		});
	}

	private static Audit scan(ServerLevel level, Set<Block> unexpected,
			int centerChunkX, int centerChunkZ, int radius,
			int requestedMinY, int requestedMaxY) {
		Map<ResourceLocation, Integer> counts = new LinkedHashMap<>();
		Map<ResourceLocation, BlockPos> samples = new LinkedHashMap<>();
		Map<ResourceLocation, Bounds> bounds = new LinkedHashMap<>();
		Map<ResourceLocation, Set<BlockPos>> positions = new LinkedHashMap<>();
		Map<ResourceLocation, Map<ResourceLocation, Integer>> biomeCounts =
				new LinkedHashMap<>();
		Map<ResourceLocation, Integer> vanillaBlocks = new LinkedHashMap<>();
		int minY = Math.max(level.getMinBuildHeight(), requestedMinY);
		int maxY = Math.min(level.getMaxBuildHeight() - 1, requestedMaxY);
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				for (int x = chunkX << 4; x < (chunkX + 1) << 4; x++) {
					for (int z = chunkZ << 4; z < (chunkZ + 1) << 4; z++) {
						for (int y = minY; y <= maxY; y++) {
							BlockPos position = new BlockPos(x, y, z);
							BlockState state = level.getBlockState(position);
							Block block = state.getBlock();
							ResourceLocation blockId = Registry.BLOCK.getKey(block);
							if (!state.isAir() && "minecraft".equals(
									blockId.getNamespace())) {
								vanillaBlocks.merge(blockId, 1, Integer::sum);
							}
							if (unexpected.contains(block)) {
								ResourceLocation id = blockId;
								counts.merge(id, 1, Integer::sum);
								samples.putIfAbsent(id, position);
								bounds.compute(id, (ignored, existing) ->
										existing == null
												? new Bounds(position, position)
												: existing.include(position));
								Set<BlockPos> captured = positions.computeIfAbsent(id,
										ignored -> new HashSet<>());
								if (captured.size() < COMPONENT_SAMPLE_LIMIT) {
									captured.add(position);
								}
								ResourceLocation biome = level.getBiome(position)
										.unwrapKey().map(ResourceKey::location)
										.orElse(new ResourceLocation("minecraft",
												"unknown"));
								biomeCounts.computeIfAbsent(id,
										ignored -> new LinkedHashMap<>())
										.merge(biome, 1, Integer::sum);
							}
						}
					}
				}
			}
		}
		return new Audit(counts, samples, bounds,
				components(positions), neighbors(level, positions), biomeCounts,
				vanillaBlocks);
	}

	private static Map<ResourceLocation, Map<ResourceLocation, Integer>>
			neighbors(ServerLevel level,
					Map<ResourceLocation, Set<BlockPos>> positions) {
		Map<ResourceLocation, Map<ResourceLocation, Integer>> result =
				new LinkedHashMap<>();
		positions.forEach((id, captured) -> {
			Map<ResourceLocation, Integer> adjacent = new LinkedHashMap<>();
			for (BlockPos position : captured) {
				for (BlockPos neighbor : List.of(
						position.offset(1, 0, 0), position.offset(-1, 0, 0),
						position.offset(0, 1, 0), position.offset(0, -1, 0),
						position.offset(0, 0, 1), position.offset(0, 0, -1))) {
					ResourceLocation neighborId = Registry.BLOCK.getKey(
							level.getBlockState(neighbor).getBlock());
					if (!neighborId.equals(id)) {
						adjacent.merge(neighborId, 1, Integer::sum);
					}
				}
			}
			result.put(id, adjacent);
		});
		return result;
	}

	private static Map<ResourceLocation, List<Component>> components(
			Map<ResourceLocation, Set<BlockPos>> positions) {
		Map<ResourceLocation, List<Component>> result = new LinkedHashMap<>();
		positions.forEach((id, captured) -> {
			Set<BlockPos> remaining = new HashSet<>(captured);
			List<Component> found = new ArrayList<>();
			while (!remaining.isEmpty()) {
				BlockPos first = remaining.iterator().next();
				remaining.remove(first);
				ArrayDeque<BlockPos> open = new ArrayDeque<>();
				open.add(first);
				int size = 0;
				Bounds componentBounds = new Bounds(first, first);
				while (!open.isEmpty()) {
					BlockPos current = open.removeFirst();
					size++;
					componentBounds = componentBounds.include(current);
					for (BlockPos neighbor : List.of(
							current.offset(1, 0, 0), current.offset(-1, 0, 0),
							current.offset(0, 1, 0), current.offset(0, -1, 0),
							current.offset(0, 0, 1), current.offset(0, 0, -1))) {
						if (remaining.remove(neighbor)) {
							open.addLast(neighbor);
						}
					}
				}
				found.add(new Component(size, componentBounds));
			}
			found.sort((left, right) -> Integer.compare(
					right.blocks(), left.blocks()));
			result.put(id, List.copyOf(found.subList(0,
					Math.min(COMPONENT_REPORT_LIMIT, found.size()))));
		});
		return result;
	}

	private static void preload(ServerLevel level, int centerChunkX,
			int centerChunkZ, int radius) {
		for (int chunkX = centerChunkX - radius;
				chunkX <= centerChunkX + radius; chunkX++) {
			for (int chunkZ = centerChunkZ - radius;
					chunkZ <= centerChunkZ + radius; chunkZ++) {
				level.getChunk(chunkX, chunkZ);
			}
		}
	}

	private static Map<Block, Integer> count(ServerLevel level,
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

	private static String describe(Map<Block, Integer> counts) {
		Map<ResourceLocation, Integer> described = new LinkedHashMap<>();
		counts.forEach((block, count) -> described.put(
				Registry.BLOCK.getKey(block), count));
		return described.toString();
	}

	private static ResourceKey<PlacedFeature> feature(String path) {
		return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", path));
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private record Audit(Map<ResourceLocation, Integer> counts,
			Map<ResourceLocation, BlockPos> samples,
			Map<ResourceLocation, Bounds> bounds,
			Map<ResourceLocation, List<Component>> components,
			Map<ResourceLocation, Map<ResourceLocation, Integer>> neighbors,
			Map<ResourceLocation, Map<ResourceLocation, Integer>> biomeCounts,
			Map<ResourceLocation, Integer> vanillaBlocks) {
		private boolean isEmpty() {
			return counts.isEmpty();
		}

		private String failureSummary() {
			return "counts=" + counts + ", samples=" + samples
					+ ", bounds=" + bounds + ", biomes=" + biomeCounts;
		}

		private String summary() {
			return failureSummary() + ", largest_components=" + components
					+ ", neighbors=" + neighbors
					+ ", vanilla_blocks=" + vanillaBlocks;
		}
	}

	private record Component(int blocks, Bounds bounds) {
	}

	private record Bounds(BlockPos minimum, BlockPos maximum) {
		private Bounds include(BlockPos position) {
			return new Bounds(
					new BlockPos(Math.min(minimum.getX(), position.getX()),
							Math.min(minimum.getY(), position.getY()),
							Math.min(minimum.getZ(), position.getZ())),
					new BlockPos(Math.max(maximum.getX(), position.getX()),
							Math.max(maximum.getY(), position.getY()),
							Math.max(maximum.getZ(), position.getZ())));
		}
	}
}
