package zone.moddev.mc.cakeworld.gametest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.world.GrandGingerbreadManorFeature;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/** Fixed-world proof for the Manor's declared Liquorice Darkwood home. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_grand_manor_darkwood")
public final class GrandManorDarkwoodGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();

	private GrandManorDarkwoodGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 24000)
	public static void naturalManorUsesItsPrimaryDarkwoodHome(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel().getServer()
				.getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos darkwood = locateBiome(helper, level,
				CakeWorldBiomes.LIQUORICE_DARKWOOD.getId());
		Registry<ConfiguredStructureFeature<?, ?>> structures =
				level.registryAccess().registryOrThrow(
						Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
		ConfiguredStructureFeature<?, ?> configured = structures.get(
				GrandGingerbreadManorFeature.STRUCTURE_ID);
		require(helper, configured != null,
				"Grand Gingerbread Manor was absent from the live registry");
		LocatedManor locatedManor = findNaturalDarkwoodManor(
				helper, level, configured);
		BlockPos located = locatedManor.located();
		StructureStart start = locatedManor.start();
		BoundingBox bounds = start.getBoundingBox();
		require(helper, bounds.getXSpan() == 49
					&& bounds.getYSpan() == 30
					&& bounds.getZSpan() == 49,
				"The Darkwood-located Manor lost its exact bounds: " + bounds);

		int minimumChunkX = Math.floorDiv(bounds.minX(), 16);
		int maximumChunkX = Math.floorDiv(bounds.maxX(), 16);
		int minimumChunkZ = Math.floorDiv(bounds.minZ(), 16);
		int maximumChunkZ = Math.floorDiv(bounds.maxZ(), 16);
		for (int chunkX = minimumChunkX; chunkX <= maximumChunkX;
				chunkX++) {
			for (int chunkZ = minimumChunkZ; chunkZ <= maximumChunkZ;
					chunkZ++) {
				level.setChunkForced(chunkX, chunkZ, true);
			}
		}

		helper.runAfterDelay(80, () -> {
			BlockPos centre = new BlockPos(bounds.minX() + 24,
					bounds.minY(), bounds.minZ() + 24);
			ResourceLocation biome = level.getBiome(centre).unwrapKey()
					.map(key -> key.location()).orElse(null);
			Map<Block, Integer> palette = new LinkedHashMap<>();
			for (int x = -24; x <= 24; x++) {
				for (int y = 0; y <= 29; y++) {
					for (int z = -24; z <= 24; z++) {
						palette.merge(level.getBlockState(
								centre.offset(x, y, z)).getBlock(),
								1, Integer::sum);
					}
				}
			}
			List<Raider> residents = level.getEntitiesOfClass(
					Raider.class, new AABB(centre).inflate(48.0D));
			long bandits = residents.stream().filter(entity ->
					entity.getType()
							== CakeWorldEntities.ROLLING_PIN_RAIDER.get())
					.count();
			long sorcerers = residents.stream().filter(entity ->
					entity.getType()
							== CakeWorldEntities.SOUR_SORCERER.get())
					.count();
			long bakers = residents.stream().filter(entity ->
					entity.getType() == CakeWorldEntities.BITTER_BAKER.get())
					.count();
			LOGGER.info("Natural Darkwood Manor audit: darkwoodAnchor={}, locate={}, centre={}, bounds={}, biome={}, placementCandidates={}, darkwoodCandidates={}, generatedAttempts={}, palette={}, residents={}/{}/{}/{}",
					darkwood, located, centre, bounds, biome,
					locatedManor.placementCandidates(),
					locatedManor.darkwoodCandidates(),
					locatedManor.generatedAttempts(),
					palette,
					residents.size(), bandits, sorcerers, bakers);

			for (int chunkX = minimumChunkX; chunkX <= maximumChunkX;
					chunkX++) {
				for (int chunkZ = minimumChunkZ;
						chunkZ <= maximumChunkZ; chunkZ++) {
					level.setChunkForced(chunkX, chunkZ, false);
				}
			}
			require(helper,
					CakeWorldBiomes.LIQUORICE_DARKWOOD.getId()
							.equals(biome),
					"The nearest natural Manor to Darkwood used " + biome
							+ " instead of its declared primary home");
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.GINGERBREAD_BRICKS.get(), 0)
							>= 2500
							&& palette.getOrDefault(
									CakeWorldBlocks.WAFER_BLOCK.get(), 0)
									>= 3500
							&& palette.getOrDefault(Blocks.CHEST, 0) == 2
							&& residents.size() == 8
							&& bandits == 5 && sorcerers == 2
							&& bakers == 1,
					"The natural Darkwood Manor lost its edible signature or household");
			helper.succeed();
		});
	}

	private static LocatedManor findNaturalDarkwoodManor(
			GameTestHelper helper, ServerLevel level,
			ConfiguredStructureFeature<?, ?> configured) {
		RandomSpreadStructurePlacement placement =
				(RandomSpreadStructurePlacement)
						GrandGingerbreadManorFeature.structureSet()
								.value().placement();
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		List<ChunkPos> candidates = new ArrayList<>();
		int placementCandidates = 0;
		for (int regionX = -48; regionX <= 48; regionX++) {
			for (int regionZ = -48; regionZ <= 48; regionZ++) {
				placementCandidates++;
				ChunkPos candidate = placement.getPotentialFeatureChunk(
						level.getSeed(),
						regionX * placement.spacing(),
						regionZ * placement.spacing());
				int centreX = candidate.getMinBlockX() + 24;
				int centreZ = candidate.getMinBlockZ() + 24;
				Holder<Biome> biome = generator.getNoiseBiome(
						QuartPos.fromBlock(centreX),
						QuartPos.fromBlock(64),
						QuartPos.fromBlock(centreZ));
				if (biome.unwrapKey().map(key -> key.location().equals(
						CakeWorldBiomes.LIQUORICE_DARKWOOD.getId()))
						.orElse(false)) {
					candidates.add(candidate);
				}
			}
		}
		candidates.sort(Comparator.comparingLong(position ->
				(long) position.x * position.x
						+ (long) position.z * position.z));
		int attempts = 0;
		for (ChunkPos candidate : candidates) {
			if (attempts >= 32) {
				break;
			}
			attempts++;
			StructureStart start = level.getChunk(candidate.x, candidate.z)
					.getStartForFeature(configured);
			if (start != null && start.isValid()
					&& start.getFeature() == configured
					&& start.getPieces().size() == 1) {
				return new LocatedManor(new BlockPos(
						candidate.getMinBlockX(), 0,
						candidate.getMinBlockZ()), start,
						placementCandidates, candidates.size(), attempts);
			}
		}
		require(helper, false,
				"No valid natural Darkwood Manor start among "
						+ candidates.size() + " live-biome candidates and "
						+ attempts + " generated attempts");
		throw new IllegalStateException("Unreachable after GameTest failure");
	}

	private static BlockPos locateBiome(GameTestHelper helper,
			ServerLevel level, ResourceLocation biomeId) {
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				holder -> holder.unwrapKey()
						.map(key -> key.location().equals(biomeId))
						.orElse(false),
				new BlockPos(0, 64, 0), 16384, 8);
		require(helper, match != null,
				"Could not locate " + biomeId + " in the fixed world");
		return match.getFirst();
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private record LocatedManor(BlockPos located, StructureStart start,
			int placementCandidates, int darkwoodCandidates,
			int generatedAttempts) {
	}
}
