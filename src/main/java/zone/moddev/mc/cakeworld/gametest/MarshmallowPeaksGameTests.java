package zone.moddev.mc.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.block.IcingLayerBlock;
import zone.moddev.mc.cakeworld.block.MarshmallowBlock;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.MarshmallowCloudBridgeFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class MarshmallowPeaksGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("marshmallow_peaks");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private MarshmallowPeaksGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow009")
	public static void peaksHaveSoftGeologyFlocksAndAmbience(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome peaks = registry.get(BIOME_ID);
		Biome source = registry.get(
				new ResourceLocation("minecraft", "jagged_peaks"));
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, peaks != null && source != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.MOUNTAIN
						&& close(peaks.getBaseTemperature(), -0.3D)
						&& close(peaks.getDownfall(), 0.5D)
						&& peaks.coldEnoughToSnow(
								new BlockPos(0, 160, 0)),
				"Marshmallow Peaks is not a cold snowy Jagged-Peaks-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.MOUNTAIN)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.PEAK)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.COLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SNOWY),
				"Marshmallow Peaks dictionary roles are incomplete");
		AmbientAdditionsSettings ambience =
				peaks.getAmbientAdditions().orElse(null);
		AmbientParticleSettings cloud =
				peaks.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.MARSHMALLOW_PEAKS_BREEZE
										.getId())
						&& close(ambience.getTickChance(), 0.001D)
						&& cloud != null
						&& cloud.getOptions().getType()
								== ParticleTypes.CLOUD,
				"Marshmallow Peaks lost its subtitled soft-cloud ambience");

		requireExactReplacement(helper, source, peaks,
				EntityType.GOAT,
				CakeWorldEntities.NOUGAT_GOAT.get(),
				MobCategory.CREATURE);
		MobSpawnSettings.SpawnerData sheep = findSpawn(
				peaks, CakeWorldEntities.CANDYFLOSS_SHEEP.get(),
				MobCategory.CREATURE);
		require(helper, sheep != null
						&& sheep.getWeight().asInt() == 6
						&& sheep.minCount == 2
						&& sheep.maxCount == 4
						&& findSpawn(peaks, EntityType.SHEEP,
								MobCategory.CREATURE) == null,
				"Marshmallow Peaks lost its gentle Candyfloss flock or leaked literal Sheep: "
						+ sheep);
		require(helper,
				MarshmallowBlock.BOUNCE_MULTIPLIER == 0.45D
						&& MarshmallowBlock.MAXIMUM_BOUNCE == 0.8D
						&& IcingLayerBlock.FALL_DAMAGE_MULTIPLIER
								== 0.5F,
				"Marshmallow Peaks lost its zero-damage bounce or Icing landing-softness constants");

		require(helper, hasPlacedFeature(peaks,
						MarshmallowCloudBridgeFeature.ID),
				"Marshmallow Peaks did not install its cloud bridge");
		for (ResourceLocation other : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.PEPPERMINT_PINEWOODS.getId(),
				CakeWorldBiomes.CANDY_CANE_BADLANDS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			require(helper,
					!hasPlacedFeature(registry.get(other),
							MarshmallowCloudBridgeFeature.ID),
					"Marshmallow cloud bridge leaked into " + other);
		}

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 19,
				"Marshmallow Peaks requires provider revision 19");
		JsonObject templates = provider.getAsJsonObject("templates");
		JsonObject first = null;
		for (String template : List.of(
				"cakeworld:edible_world",
				"cakeworld:edible_world_basemetals")) {
			JsonObject profile = templates.getAsJsonObject(template)
					.getAsJsonObject("profile");
			JsonObject geomes = profile
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject palette = profile
					.getAsJsonObject("biome_palettes")
					.getAsJsonObject("cakeworld:overworld_land")
					.getAsJsonObject("biomes")
					.getAsJsonObject(BIOME_ID.toString());
			JsonObject surface =
					palette.getAsJsonObject("surface");
			require(helper,
					geomes.get("cakeworld:peppermint_fold")
								.getAsInt() == 6
							&& geomes.get(
									"cakeworld:rock_candy_uplift")
									.getAsInt() == 14
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.0D)
							&& palette.getAsJsonArray(
									"similar_biomes")
									.size() == 5
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), -2.0D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 0.35D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.0D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 1.0D)
							&& "cakeworld:icing"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:marshmallow"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:biscuit_crumbs"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5,
					template
							+ " lost the Marshmallow Peaks profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Peaks profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow009",
			timeoutTicks = 800)
	public static void cloudBridgeIsBoundedWalkableAndRecoverable(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				MarshmallowCloudBridgeFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== MarshmallowCloudBridgeFeature.FEATURE,
				"Marshmallow cloud-bridge feature was not registered");
		List<?> modifiers = placed.value().placement();
		require(helper, modifiers.size() == 4
						&& modifiers.get(0)
								instanceof RarityFilter
						&& modifiers.get(1)
								instanceof InSquarePlacement
						&& modifiers.get(2)
								instanceof HeightmapPlacement
						&& modifiers.get(3)
								instanceof BiomeFilter,
				"Marshmallow cloud bridge lost its rare surface-biome chain");

		BlockPos fixture = new BlockPos(
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getX(),
				level.getMaxBuildHeight() - 24,
				helper.absolutePos(new BlockPos(4, 4, 4))
						.getZ());
		ChunkPos chunk = new ChunkPos(fixture);
		BlockPos contained = new BlockPos(
				chunk.getMinBlockX() + 7,
				fixture.getY(),
				chunk.getMinBlockZ() + 7);
		Set<Rotation> orientations = new HashSet<>();
		for (int index = 0; index < 128
				&& orientations.size() < 4; index++) {
			orientations.add(
					MarshmallowCloudBridgeFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 73, 160,
									index * -83)));
		}
		require(helper, orientations.size() == 4,
				"Marshmallow cloud bridge did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					MarshmallowCloudBridgeFeature
							.fitsWithinChunk(contained,
									rotation, chunk),
					"Marshmallow cloud bridge crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(9.0D)).size();
			require(helper,
					MarshmallowCloudBridgeFeature.buildAt(
							level, new Random(1978072L),
							fixture, rotation),
					"Marshmallow cloud bridge refused a safe fixture");
			assertBridge(helper, level, fixture, rotation);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(9.0D))
							.size() == entities,
					"Marshmallow cloud bridge created an entity");
			for (int x = -6; x <= 6; x++) {
				for (int y = 0; y <= 5; y++) {
					for (int z = -2; z <= 2; z++) {
						require(helper,
								level.getBlockEntity(
										MarshmallowCloudBridgeFeature
												.local(fixture,
														rotation,
														x, y, z))
										== null,
								"Marshmallow cloud bridge created a block entity");
					}
				}
			}
		}

		prepare(level, fixture);
		level.setBlock(fixture.offset(2, 2, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!MarshmallowCloudBridgeFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Marshmallow cloud bridge accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!MarshmallowCloudBridgeFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Marshmallow cloud bridge accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow009world",
			timeoutTicks = 24000)
	public static void focusedNaturalMarshmallowCloudBridgeAudit(
			GameTestHelper helper) {
		if (!Boolean.getBoolean(
				"cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel()
				.getServer().getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		BlockPos peaks = locateBiome(helper, level, BIOME_ID);
		LocatedCloudBridge bridge = locateNaturalCloudBridge(
				helper, level, peaks, 32);
		setBridgeChunksForced(level, bridge, true);
		helper.runAfterDelay(40, () -> {
			CloudBridgeWorldAudit audit =
					auditNaturalCloudBridge(level, bridge);
			LOGGER.info("Marshmallow Cloud Bridge audit: centre={}, biome={}, rotation={}, palette={}, layout={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, peaksColumns={}",
					bridge.centre(), audit.biome(),
					audit.rotation(), audit.palette(),
					audit.readableLayout(),
					audit.blockEntities(),
					audit.brickSentinel(),
					bridge.scannedChunks(),
					bridge.markerCandidates(),
					bridge.peaksColumns());
			require(helper,
					BIOME_ID.equals(audit.biome())
							&& audit.readableLayout()
							&& audit.blockEntities() == 0,
					"Natural Marshmallow Cloud Bridge lost its Peaks biome, continuous Wafer walk, cloud puffs, landings, cushions, Icing caps or chime posts: "
							+ audit);
			if (!audit.brickSentinel()) {
				level.setBlock(audit.sentinel(),
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(audit.sentinel())
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel on the Marshmallow Cloud Bridge");
			}
			setBridgeChunksForced(level, bridge, false);
			helper.succeed();
		});
	}

	private static BlockPos locateBiome(GameTestHelper helper,
			ServerLevel level, ResourceLocation biomeId) {
		Pair<BlockPos, Holder<Biome>> match = level.findNearestBiome(
				holder -> holder.unwrapKey()
						.map(key -> key.location().equals(biomeId))
						.orElse(false),
				new BlockPos(0, 64, 0), 16384, 8);
		require(helper, match != null,
				"Could not locate " + biomeId
						+ " within 16,384 blocks");
		return match.getFirst();
	}

	private static LocatedCloudBridge locateNaturalCloudBridge(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int peaksColumns = 0;
		for (int radius = 0; radius <= chunkRadius; radius++) {
			for (int chunkX = anchorChunk.x - radius;
					chunkX <= anchorChunk.x + radius; chunkX++) {
				for (int chunkZ = anchorChunk.z - radius;
						chunkZ <= anchorChunk.z + radius; chunkZ++) {
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
							ResourceLocation biome = level.getBiome(
									new BlockPos(x, surfaceY, z))
									.unwrapKey()
									.map(ResourceKey::location)
									.orElse(null);
							if (BIOME_ID.equals(biome)) {
								peaksColumns++;
							}
							int minimumY = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 16);
							int maximumY = Math.min(
									level.getMaxBuildHeight() - 1,
									surfaceY + 16);
							for (int y = minimumY;
									y <= maximumY; y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								if (!level.getBlockState(marker)
										.is(CakeWorldBlocks
												.CANDY_GLASS.get())) {
									continue;
								}
								markerCandidates++;
								for (int localX
										: new int[] {-6, 6}) {
									for (int localZ
											: new int[] {-2, 2}) {
										for (Rotation rotation
												: Rotation.values()) {
											BlockPos offset =
													new BlockPos(
															localX,
															4,
															localZ)
															.rotate(
																	rotation);
											BlockPos centre =
													marker.subtract(
															offset);
											if (MarshmallowCloudBridgeFeature
													.orientation(
															level.getSeed(),
															centre)
													== rotation
													&& matchesCloudBridgeLayout(
															level,
															centre,
															rotation)) {
												return new LocatedCloudBridge(
														centre,
														scannedChunks,
														markerCandidates,
														peaksColumns);
											}
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
				"The fixed-seed Marshmallow Peaks survey found no natural Cloud Bridge after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " Candy-Glass marker candidates near "
						+ anchor + "; peaksColumns=" + peaksColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static CloudBridgeWorldAudit auditNaturalCloudBridge(
			ServerLevel level, LocatedCloudBridge bridge) {
		BlockPos centre = bridge.centre();
		Rotation rotation =
				MarshmallowCloudBridgeFeature.orientation(
						level.getSeed(), centre);
		BlockPos sentinel =
				MarshmallowCloudBridgeFeature.local(
						centre, rotation, 0, 4, 2);
		boolean brickSentinel =
				level.getBlockState(sentinel).is(Blocks.BRICKS);
		Map<Block, Integer> palette = new LinkedHashMap<>();
		int blockEntities = 0;
		for (int x = -7; x <= 7; x++) {
			for (int y = 0; y <= 5; y++) {
				for (int z = -3; z <= 3; z++) {
					BlockPos position = centre.offset(x, y, z);
					palette.merge(level.getBlockState(position)
							.getBlock(), 1, Integer::sum);
					if (level.getBlockEntity(position) != null) {
						blockEntities++;
					}
				}
			}
		}
		ResourceLocation biome = level.getBiome(centre)
				.unwrapKey()
				.map(ResourceKey::location)
				.orElse(null);
		boolean readable =
				matchesCloudBridgeLayout(level, centre, rotation)
						&& palette.getOrDefault(
								CakeWorldBlocks.WAFER_BLOCK.get(),
								0) == 11
						&& palette.getOrDefault(
								CakeWorldBlocks
										.CANDY_CANE_PILLAR.get(),
								0) == 12
						&& palette.getOrDefault(
								CakeWorldBlocks.CANDY_GLASS.get(),
								0) == 4
						&& palette.getOrDefault(
								CakeWorldBlocks.MARSHMALLOW.get(),
								0) >= 33
						&& palette.getOrDefault(
								CakeWorldBlocks.ICING_LAYER.get(),
								0) >= (brickSentinel ? 11 : 12);
		return new CloudBridgeWorldAudit(
				palette, biome, rotation, readable,
				blockEntities, sentinel, brickSentinel);
	}

	private static boolean matchesCloudBridgeLayout(
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int x : new int[] {-6, 6}) {
			for (int z = -1; z <= 1; z++) {
				if (!level.getBlockState(
						MarshmallowCloudBridgeFeature.local(
								centre, rotation, x, 1, z))
						.is(CakeWorldBlocks.MARSHMALLOW.get())) {
					return false;
				}
			}
		}
		for (int x : new int[] {-5, 5}) {
			if (!level.getBlockState(
					MarshmallowCloudBridgeFeature.local(
							centre, rotation, x, 2, 0))
					.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
				return false;
			}
		}
		for (int x = -4; x <= 4; x++) {
			if (!level.getBlockState(
					MarshmallowCloudBridgeFeature.local(
							centre, rotation, x,
							MarshmallowCloudBridgeFeature.BRIDGE_Y,
							0))
					.is(CakeWorldBlocks.WAFER_BLOCK.get())) {
				return false;
			}
			for (int z : new int[] {-1, 1}) {
				if (!level.getBlockState(
						MarshmallowCloudBridgeFeature.local(
								centre, rotation, x,
								MarshmallowCloudBridgeFeature
										.BRIDGE_Y,
								z))
						.is(CakeWorldBlocks.MARSHMALLOW.get())) {
					return false;
				}
			}
		}
		for (int x : MarshmallowCloudBridgeFeature.cloudCentres()) {
			if (!level.getBlockState(
					MarshmallowCloudBridgeFeature.local(
							centre, rotation, x, 2, 0))
					.is(CakeWorldBlocks.MARSHMALLOW.get())) {
				return false;
			}
			for (int z : new int[] {-2, 2}) {
				if (!level.getBlockState(
						MarshmallowCloudBridgeFeature.local(
								centre, rotation, x, 3, z))
						.is(CakeWorldBlocks.MARSHMALLOW.get())) {
					return false;
				}
			}
			for (int z : new int[] {-2, -1, 1, 2}) {
				BlockPos cap = MarshmallowCloudBridgeFeature
						.local(centre, rotation, x, 4, z);
				if (!level.getBlockState(cap)
						.is(CakeWorldBlocks.ICING_LAYER.get())
						&& !(x == 0 && z == 2
								&& level.getBlockState(cap)
										.is(Blocks.BRICKS))) {
					return false;
				}
			}
		}
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					if (!level.getBlockState(
							MarshmallowCloudBridgeFeature.local(
									centre, rotation, x, y, z))
							.is(CakeWorldBlocks
									.CANDY_CANE_PILLAR.get())) {
						return false;
					}
				}
				if (!level.getBlockState(
						MarshmallowCloudBridgeFeature.local(
								centre, rotation, x, 4, z))
						.is(CakeWorldBlocks.CANDY_GLASS.get())) {
					return false;
				}
			}
		}
		return true;
	}

	private static void setBridgeChunksForced(
			ServerLevel level, LocatedCloudBridge bridge,
			boolean forced) {
		for (int chunkX = Math.floorDiv(
				bridge.centre().getX() - 6, 16);
				chunkX <= Math.floorDiv(
						bridge.centre().getX() + 6, 16);
				chunkX++) {
			for (int chunkZ = Math.floorDiv(
					bridge.centre().getZ() - 6, 16);
					chunkZ <= Math.floorDiv(
							bridge.centre().getZ() + 6, 16);
					chunkZ++) {
				level.setChunkForced(chunkX, chunkZ, forced);
			}
		}
	}

	private static void assertBridge(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation) {
		for (int x : new int[] {-6, 6}) {
			for (int z = -1; z <= 1; z++) {
				require(helper,
						level.getBlockState(
								MarshmallowCloudBridgeFeature
										.local(centre, rotation,
												x, 1, z))
								.is(CakeWorldBlocks
										.MARSHMALLOW.get()),
						"Cloud bridge lost a Marshmallow landing");
			}
		}
		for (int x : new int[] {-5, 5}) {
			require(helper,
					level.getBlockState(
							MarshmallowCloudBridgeFeature.local(
									centre, rotation, x, 2, 0))
							.is(CakeWorldBlocks.WAFER_BLOCK.get()),
					"Cloud bridge lost an access step");
		}
		for (int x = -4; x <= 4; x++) {
			require(helper,
					level.getBlockState(
							MarshmallowCloudBridgeFeature.local(
									centre, rotation, x,
									MarshmallowCloudBridgeFeature
											.BRIDGE_Y,
									0))
							.is(CakeWorldBlocks.WAFER_BLOCK.get()),
					"Cloud bridge lost its continuous Wafer walk");
			for (int z : new int[] {-1, 1}) {
				require(helper,
						level.getBlockState(
								MarshmallowCloudBridgeFeature
										.local(centre, rotation,
												x,
												MarshmallowCloudBridgeFeature
														.BRIDGE_Y,
												z))
								.is(CakeWorldBlocks
										.MARSHMALLOW.get()),
						"Cloud bridge lost a side cloud");
			}
		}
		for (int x
				: MarshmallowCloudBridgeFeature.cloudCentres()) {
			require(helper,
					level.getBlockState(
							MarshmallowCloudBridgeFeature.local(
									centre, rotation, x, 2, 0))
							.is(CakeWorldBlocks.MARSHMALLOW.get()),
					"Cloud bridge lost an underside rescue cushion");
			for (int z : new int[] {-2, 2}) {
				require(helper,
						level.getBlockState(
								MarshmallowCloudBridgeFeature
										.local(centre, rotation,
												x, 3, z))
								.is(CakeWorldBlocks
										.MARSHMALLOW.get()),
						"Cloud bridge lost a wide cloud puff");
			}
			for (int z : new int[] {-2, -1, 1, 2}) {
				require(helper,
						level.getBlockState(
								MarshmallowCloudBridgeFeature
										.local(centre, rotation,
												x, 4, z))
								.is(CakeWorldBlocks
										.ICING_LAYER.get()),
						"Cloud bridge lost an Icing cap");
			}
		}
		for (int x : new int[] {-6, 6}) {
			for (int z : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					require(helper,
							level.getBlockState(
									MarshmallowCloudBridgeFeature
											.local(centre,
													rotation,
													x, y, z))
									.is(CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get()),
							"Cloud bridge lost a chime post");
				}
				require(helper,
						level.getBlockState(
								MarshmallowCloudBridgeFeature
										.local(centre, rotation,
												x, 4, z))
								.is(CakeWorldBlocks.CANDY_GLASS.get()),
						"Cloud bridge lost a Candy-Glass chime");
			}
		}
	}

	private static void prepare(ServerLevel level,
			BlockPos centre) {
		for (int x = -8; x <= 8; x++) {
			for (int z = -8; z <= 8; z++) {
				for (int y = -10; y <= 8; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.MARSHMALLOW.get()
								.defaultBlockState(), 2);
				level.setBlock(centre.offset(x, -1, z),
						CakeWorldBlocks.BISCUIT_STONE.get()
								.defaultBlockState(), 2);
			}
		}
	}

	private static boolean hasPlacedFeature(Biome biome,
			ResourceLocation expected) {
		int step = GenerationStep.Decoration
				.TOP_LAYER_MODIFICATION.ordinal();
		if (biome == null
				|| biome.getGenerationSettings()
						.features().size() <= step) {
			return false;
		}
		for (Holder<PlacedFeature> feature
				: biome.getGenerationSettings()
						.features().get(step)) {
			if (feature.unwrapKey()
					.map(key -> key.location().equals(expected))
					.orElse(false)) {
				return true;
			}
		}
		return false;
	}

	private static void requireExactReplacement(
			GameTestHelper helper, Biome source, Biome result,
			EntityType<?> vanilla, EntityType<?> replacement,
			MobCategory category) {
		MobSpawnSettings.SpawnerData expected =
				findSpawn(source, vanilla, category);
		MobSpawnSettings.SpawnerData actual =
				findSpawn(result, replacement, category);
		require(helper, expected != null && actual != null
						&& findSpawn(result, vanilla, category) == null
						&& expected.getWeight().asInt()
								== actual.getWeight().asInt()
						&& expected.minCount == actual.minCount
						&& expected.maxCount == actual.maxCount,
				"Marshmallow Peaks did not exactly replace "
						+ Registry.ENTITY_TYPE.getKey(vanilla)
						+ ": expected=" + expected
						+ ", actual=" + actual);
	}

	private static MobSpawnSettings.SpawnerData findSpawn(
			Biome biome, EntityType<?> type,
			MobCategory category) {
		for (MobSpawnSettings.SpawnerData spawn
				: biome.getMobSettings()
						.getMobs(category).unwrap()) {
			if (spawn.type == type) {
				return spawn;
			}
		}
		return null;
	}

	private static JsonObject readProvider() {
		try (InputStreamReader reader = new InputStreamReader(
				MarshmallowPeaksGameTests.class
						.getResourceAsStream(
								"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Unable to read packaged provider", exception);
		}
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}

	private static boolean close(double actual,
			double expected) {
		return Math.abs(actual - expected) < 0.0001D;
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private record LocatedCloudBridge(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int peaksColumns) {
	}

	private record CloudBridgeWorldAudit(
			Map<Block, Integer> palette,
			ResourceLocation biome,
			Rotation rotation,
			boolean readableLayout,
			int blockEntities,
			BlockPos sentinel,
			boolean brickSentinel) {
	}
}
