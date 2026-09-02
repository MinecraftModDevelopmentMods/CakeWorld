package zone.moddev.mc.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBiomes;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEffects;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.world.IceCreamParlourFeature;
import zone.moddev.mc.cakeworld.world.IceCreamParlourRepairFeature;
import zone.moddev.mc.cakeworld.world.IceCreamSundaeRinkFeature;

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
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
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

/**
 * Contract proof for the first functional Ice-Cream Tundra ecosystem.
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class IceCreamTundraGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final ResourceLocation BIOME_ID =
			id("ice_cream_tundra");
	private static final ResourceKey<Biome> BIOME_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY, BIOME_ID);

	private IceCreamTundraGameTests() {
	}

	@GameTest(template = EMPTY, batch = "bioow010")
	public static void tundraHasFrozenEcologyParloursAndProfile(
			GameTestHelper helper) {
		Registry<Biome> registry = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome tundra = registry.get(BIOME_ID);
		Holder<Biome> holder = registry.getHolder(BIOME_KEY)
				.orElseThrow();
		require(helper, tundra != null
						&& Biome.getBiomeCategory(holder)
								== Biome.BiomeCategory.ICY
						&& close(tundra.getBaseTemperature(), -0.5D)
						&& close(tundra.getDownfall(), 0.4D)
						&& tundra.coldEnoughToSnow(
								new BlockPos(0, 80, 0)),
				"Ice-Cream Tundra is not a cold snowy-plains-derived biome");
		require(helper,
				BiomeDictionary.hasType(BIOME_KEY,
						BiomeDictionary.Type.OVERWORLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.PLAINS)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.COLD)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.SNOWY)
						&& BiomeDictionary.hasType(
								BIOME_KEY,
								BiomeDictionary.Type.WASTELAND),
				"Ice-Cream Tundra dictionary roles are incomplete");

		AmbientAdditionsSettings ambience =
				tundra.getAmbientAdditions().orElse(null);
		AmbientParticleSettings snow =
				tundra.getAmbientParticle().orElse(null);
		require(helper, ambience != null
						&& ambience.getSoundEvent().getLocation()
								.equals(CakeWorldSounds
										.ICE_CREAM_TUNDRA_CHILL
										.getId())
						&& close(ambience.getTickChance(), 0.001D)
						&& snow != null
						&& snow.getOptions().getType()
								== ParticleTypes.SNOWFLAKE,
				"Ice-Cream Tundra lost its subtitled glass-chime and snowflake ambience");

		assertSpawn(helper, tundra,
				CakeWorldEntities.VANILLA_ICE_BEAR.get(),
				MobCategory.CREATURE, 1, 1, 2);
		assertSpawn(helper, tundra,
				CakeWorldEntities.SHERBET_SALMON.get(),
				MobCategory.WATER_AMBIENT, 15, 1, 5);
		assertSpawn(helper, tundra,
				CakeWorldEntities.FROSTED_ARCHER.get(),
				MobCategory.MONSTER, 80, 4, 4);
		require(helper,
				findSpawn(tundra, EntityType.POLAR_BEAR,
						MobCategory.CREATURE) == null
						&& findSpawn(tundra, EntityType.SALMON,
								MobCategory.WATER_AMBIENT) == null
						&& findSpawn(tundra, EntityType.STRAY,
								MobCategory.MONSTER) == null,
				"Ice-Cream Tundra leaked a literal Polar Bear, Salmon or Stray");

		require(helper,
				hasPlacedFeature(tundra,
						IceCreamSundaeRinkFeature.ID)
						&& hasPlacedFeature(tundra,
								IceCreamParlourRepairFeature.ID)
						&& holder.is(
								IceCreamParlourFeature.GENERATES_IN),
				"Ice-Cream Tundra lost its Sundae Rink or exclusive Parlour integration");
		Biome peaks = registry.get(
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId());
		require(helper, peaks != null
						&& !hasPlacedFeature(peaks,
								IceCreamSundaeRinkFeature.ID)
						&& !registry.getHolder(
								ResourceKey.create(
										Registry.BIOME_REGISTRY,
										CakeWorldBiomes
												.MARSHMALLOW_PEAKS
												.getId()))
								.orElseThrow()
								.is(IceCreamParlourFeature
										.GENERATES_IN),
				"Ice-Cream Tundra content leaked back into Marshmallow Peaks");

		FoodProperties mint =
				CakeWorldItems.MINT_WAFER.get().getFoodProperties();
		require(helper, mint != null
						&& mint.getEffects().stream().anyMatch(entry ->
								entry.getFirst().getEffect()
										== CakeWorldEffects
												.MINTY_FRESH.get()
										&& entry.getFirst()
												.getDuration() == 200
										&& close(entry.getSecond(),
												1.0D))
						&& helper.getLevel().getRecipeManager()
								.byKey(id("mint_wafer"))
								.isPresent(),
				"Ice-Cream Tundra lost its craftable Mint-Wafer cooling food");

		JsonObject provider = readProvider();
		require(helper,
				provider.get("provider_revision").getAsInt() >= 20,
				"Ice-Cream Tundra requires provider revision 20");
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
								.getAsInt() == 9
							&& geomes.get("cakeworld:wafer_shelf")
									.getAsInt() == 7
							&& palette.get("enabled")
									.getAsBoolean()
							&& close(palette.get("weight")
									.getAsDouble(), 1.0D)
							&& palette.getAsJsonArray(
									"similar_biomes")
									.size() == 3
							&& palette.getAsJsonArray(
									"required_similar_biomes")
									.size() == 0
							&& close(palette
									.get("min_temperature")
									.getAsDouble(), -2.0D)
							&& close(palette
									.get("max_temperature")
									.getAsDouble(), 0.2D)
							&& close(palette
									.get("min_downfall")
									.getAsDouble(), 0.0D)
							&& close(palette
									.get("max_downfall")
									.getAsDouble(), 0.8D)
							&& "cakeworld:icing"
									.equals(surface
											.get("top_block")
											.getAsString())
							&& "cakeworld:icing"
									.equals(surface
											.get("filler_block")
											.getAsString())
							&& "cakeworld:frozen_lemonade"
									.equals(surface
											.get("underwater_block")
											.getAsString())
							&& surface.get("filler_depth")
									.getAsInt() == 5,
					template
							+ " lost the Ice-Cream Tundra profile");
			if (first == null) {
				first = palette;
			} else {
				require(helper, first.equals(palette),
						"Normal and BaseMetals Tundra profiles diverged");
			}
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow010",
			timeoutTicks = 800)
	public static void sundaeRinkIsBoundedSlipperyAndRecoverable(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		Holder<PlacedFeature> placed =
				IceCreamSundaeRinkFeature.placedFeature();
		require(helper, placed != null
						&& placed.value().feature().value()
								.feature()
								== IceCreamSundaeRinkFeature.FEATURE,
				"Ice-Cream Sundae Rink feature was not registered");
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
				"Ice-Cream Sundae Rink lost its rare surface-biome chain");

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
					IceCreamSundaeRinkFeature.orientation(
							level.getSeed(),
							new BlockPos(index * 79, 96,
									index * -89)));
		}
		require(helper, orientations.size() == 4,
				"Ice-Cream Sundae Rink did not expose all rotations");

		for (Rotation rotation : Rotation.values()) {
			require(helper,
					IceCreamSundaeRinkFeature
							.fitsWithinChunk(contained,
									rotation, chunk),
					"Ice-Cream Sundae Rink crossed its generating chunk");
			prepare(level, fixture);
			int entities = level.getEntities((Entity) null,
					new AABB(fixture).inflate(8.0D)).size();
			require(helper,
					IceCreamSundaeRinkFeature.buildAt(
							level, fixture, rotation),
					"Ice-Cream Sundae Rink refused a safe fixture for "
							+ rotation + ": "
							+ IceCreamSundaeRinkFeature
									.footprintProblem(
											level, fixture,
											rotation));
			assertRink(helper, level, fixture, rotation,
					false);
			Map<Block, Integer> palette =
					scanPalette(level, fixture);
			require(helper,
					palette.getOrDefault(
							CakeWorldBlocks.FROZEN_LEMONADE
									.get(), 0) == 49
							&& palette.getOrDefault(
									CakeWorldBlocks.WAFER_BLOCK
											.get(), 0) == 28
							&& palette.getOrDefault(
									CakeWorldBlocks.MARSHMALLOW
											.get(), 0) == 10
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CHOCOLATE_SPONGE
											.get(), 0) == 6
							&& palette.getOrDefault(
									CakeWorldBlocks.ICING.get(),
									0) == 6
							&& palette.getOrDefault(
									CakeWorldBlocks.ICING_LAYER
											.get(), 0) == 3
							&& palette.getOrDefault(
									CakeWorldBlocks
											.CANDY_CANE_PILLAR
											.get(), 0) == 8
							&& palette.getOrDefault(
									CakeWorldBlocks.CANDY_GLASS
											.get(), 0) == 4,
					"Ice-Cream Sundae Rink lost its exact lake, rim, rescue pads, flavour scoops or markers: "
							+ palette);
			require(helper,
					level.getEntities((Entity) null,
							new AABB(fixture).inflate(8.0D))
							.size() == entities,
					"Ice-Cream Sundae Rink created an entity");
			for (int x = -5; x <= 5; x++) {
				for (int y = 1; y <= 4; y++) {
					for (int z = -5; z <= 5; z++) {
						require(helper,
								level.getBlockEntity(
										fixture.offset(x, y, z))
										== null,
								"Ice-Cream Sundae Rink created a block entity");
					}
				}
			}
		}

		prepare(level, fixture);
		level.setBlock(
				IceCreamSundaeRinkFeature.local(
						fixture, Rotation.NONE,
						2, 2, 1),
				Blocks.BRICKS.defaultBlockState(), 2);
		require(helper,
				!IceCreamSundaeRinkFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Ice-Cream Sundae Rink accepted an authored solid obstacle");
		prepare(level, fixture);
		level.setBlock(fixture,
				Blocks.WATER.defaultBlockState(), 2);
		require(helper,
				!IceCreamSundaeRinkFeature
						.hasSafeFootprint(
								level, fixture,
								Rotation.NONE),
				"Ice-Cream Sundae Rink accepted a wet site");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "bioow010world",
			timeoutTicks = 24000)
	public static void focusedNaturalIceCreamSundaeRinkAudit(
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
		BlockPos tundra = locateBiome(helper, level, BIOME_ID);
		LocatedRink rink = locateNaturalRink(
				helper, level, tundra, 32);
		ChunkPos rinkChunk = new ChunkPos(rink.centre());
		level.setChunkForced(rinkChunk.x, rinkChunk.z, true);
		helper.runAfterDelay(40, () -> {
			Rotation rotation =
					IceCreamSundaeRinkFeature.orientation(
							level.getSeed(), rink.centre());
			BlockPos sentinel =
					IceCreamSundaeRinkFeature.local(
							rink.centre(), rotation,
							0, 4, 0);
			boolean brickSentinel =
					level.getBlockState(sentinel)
							.is(Blocks.BRICKS);
			assertRink(helper, level, rink.centre(),
					rotation, brickSentinel);
			Map<Block, Integer> palette =
					scanPalette(level, rink.centre());
			int blockEntities = 0;
			for (int x = -5; x <= 5; x++) {
				for (int y = 1; y <= 4; y++) {
					for (int z = -5; z <= 5; z++) {
						if (level.getBlockEntity(
								rink.centre()
										.offset(x, y, z))
								!= null) {
							blockEntities++;
						}
					}
				}
			}
			SurfaceAudit surface =
					auditSurface(level, rink.centre(), 24);
			ResourceLocation biome = level.getBiome(
					rink.centre()).unwrapKey()
					.map(ResourceKey::location)
					.orElse(null);
			LOGGER.info("Ice-Cream Sundae Rink audit: centre={}, biome={}, rotation={}, palette={}, blockEntities={}, brickSentinel={}, scannedChunks={}, markerCandidates={}, tundraColumns={}, surface={}",
					rink.centre(), biome, rotation,
					palette, blockEntities, brickSentinel,
					rink.scannedChunks(),
					rink.markerCandidates(),
					rink.tundraColumns(), surface);
			require(helper,
					BIOME_ID.equals(biome)
							&& blockEntities == 0
							&& surface.tundraColumns() >= 64
							&& surface.solidIcing() >= 64,
					"Natural Ice-Cream Tundra lost its biome, solid-Icing surface or block-entity-free Sundae Rink: "
							+ surface);
			if (!brickSentinel) {
				level.setBlock(sentinel,
						Blocks.BRICKS.defaultBlockState(),
						2);
				require(helper,
						level.getBlockState(sentinel)
								.is(Blocks.BRICKS),
						"Could not seed the player Brick reload sentinel on the Sundae Rink");
			}
			level.setChunkForced(rinkChunk.x, rinkChunk.z, false);
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

	private static LocatedRink locateNaturalRink(
			GameTestHelper helper, ServerLevel level,
			BlockPos anchor, int chunkRadius) {
		ChunkPos anchorChunk = new ChunkPos(anchor);
		int scannedChunks = 0;
		int markerCandidates = 0;
		int tundraColumns = 0;
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
							ResourceLocation biome =
									level.getBiome(new BlockPos(
											x, surfaceY, z))
											.unwrapKey()
											.map(ResourceKey::location)
											.orElse(null);
							if (BIOME_ID.equals(biome)) {
								tundraColumns++;
							}
							for (int y = Math.max(
									level.getMinBuildHeight(),
									surfaceY - 12);
									y <= Math.min(
											level.getMaxBuildHeight() - 1,
											surfaceY + 4);
									y++) {
								BlockPos marker =
										new BlockPos(x, y, z);
								if (!level.getBlockState(marker)
										.is(CakeWorldBlocks
												.CANDY_GLASS
												.get())) {
									continue;
								}
								markerCandidates++;
								for (int localX
										: new int[] {-4, 4}) {
									for (int localZ
											: new int[] {-3, 3}) {
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
											if (IceCreamSundaeRinkFeature
													.orientation(
															level.getSeed(),
															centre)
													== rotation
													&& matchesRink(
															level,
															centre,
															rotation,
															true)) {
												return new LocatedRink(
														centre,
														scannedChunks,
														markerCandidates,
														tundraColumns);
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
				"The fixed-seed Ice-Cream Tundra survey found no natural Sundae Rink after "
						+ scannedChunks + " generated chunks and "
						+ markerCandidates
						+ " Candy-Glass marker candidates near "
						+ anchor + "; tundraColumns="
						+ tundraColumns);
		throw new IllegalStateException(
				"Unreachable after GameTest failure");
	}

	private static SurfaceAudit auditSurface(
			ServerLevel level, BlockPos centre, int radius) {
		int tundraColumns = 0;
		int icingLayers = 0;
		int solidIcing = 0;
		for (int x = centre.getX() - radius;
				x <= centre.getX() + radius; x++) {
			for (int z = centre.getZ() - radius;
					z <= centre.getZ() + radius; z++) {
				int surfaceY = level.getHeight(
						Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES,
						x, z) - 1;
				BlockPos surface =
						new BlockPos(x, surfaceY, z);
				if (!level.getBiome(surface).is(BIOME_KEY)) {
					continue;
				}
				tundraColumns++;
				for (int y = surfaceY + 2;
						y >= surfaceY - 8; y--) {
					Block block = level.getBlockState(
							new BlockPos(x, y, z))
							.getBlock();
					if (block == CakeWorldBlocks
							.ICING_LAYER.get()) {
						icingLayers++;
					} else if (block == CakeWorldBlocks
							.ICING.get()) {
						solidIcing++;
					}
				}
			}
		}
		return new SurfaceAudit(tundraColumns,
				icingLayers, solidIcing);
	}

	private static Map<Block, Integer> scanPalette(
			ServerLevel level, BlockPos centre) {
		Map<Block, Integer> palette = new LinkedHashMap<>();
		for (int x = -5; x <= 5; x++) {
			for (int y = 1; y <= 4; y++) {
				for (int z = -5; z <= 5; z++) {
					palette.merge(level.getBlockState(
							centre.offset(x, y, z))
							.getBlock(), 1, Integer::sum);
				}
			}
		}
		return palette;
	}

	private static boolean matchesRink(ServerLevel level,
			BlockPos centre, Rotation rotation,
			boolean allowBrickSentinel) {
		for (int x = -5; x <= 5; x++) {
			for (int z = -5; z <= 5; z++) {
				int distance = x * x + z * z;
				if (distance > 25) {
					continue;
				}
				Block expected = distance <= 16
						? CakeWorldBlocks.FROZEN_LEMONADE.get()
						: CakeWorldBlocks.WAFER_BLOCK.get();
				if ((Math.abs(x) == 5 && z == 0)
						|| (Math.abs(z) == 5 && x == 0)) {
					expected = CakeWorldBlocks.MARSHMALLOW.get();
				}
				if (!level.getBlockState(
						IceCreamSundaeRinkFeature.local(
								centre, rotation, x, 1, z))
						.is(expected)) {
					return false;
				}
			}
		}
		Block[] flavours = {
				CakeWorldBlocks.CHOCOLATE_SPONGE.get(),
				CakeWorldBlocks.ICING.get(),
				CakeWorldBlocks.MARSHMALLOW.get()
		};
		for (int index = 0;
				index < IceCreamSundaeRinkFeature
						.scoopCentres().length;
				index++) {
			int x = IceCreamSundaeRinkFeature
					.scoopCentres()[index];
			Block flavour = flavours[index];
			for (int[] offset : new int[][] {
				{x, 2, 0}, {x - 1, 2, 0},
				{x + 1, 2, 0}, {x, 2, -1},
				{x, 2, 1}, {x, 3, 0}
			}) {
				if (!level.getBlockState(
						IceCreamSundaeRinkFeature.local(
								centre, rotation,
								offset[0], offset[1],
								offset[2]))
						.is(flavour)) {
					return false;
				}
			}
			BlockPos cap = IceCreamSundaeRinkFeature.local(
					centre, rotation, x, 4, 0);
			if (!level.getBlockState(cap)
					.is(CakeWorldBlocks.ICING_LAYER.get())
					&& !(allowBrickSentinel && x == 0
							&& level.getBlockState(cap)
									.is(Blocks.BRICKS))) {
				return false;
			}
		}
		for (int x : new int[] {-4, 4}) {
			for (int z : new int[] {-3, 3}) {
				for (int y = 2; y <= 3; y++) {
					if (!level.getBlockState(
							IceCreamSundaeRinkFeature.local(
									centre, rotation,
									x, y, z))
							.is(CakeWorldBlocks
									.CANDY_CANE_PILLAR.get())) {
						return false;
					}
				}
				if (!level.getBlockState(
						IceCreamSundaeRinkFeature.local(
								centre, rotation,
								x, 4, z))
						.is(CakeWorldBlocks.CANDY_GLASS.get())) {
					return false;
				}
			}
		}
		return true;
	}

	private static void assertRink(GameTestHelper helper,
			ServerLevel level, BlockPos centre,
			Rotation rotation, boolean allowBrickSentinel) {
		require(helper,
				matchesRink(level, centre, rotation,
						allowBrickSentinel),
				"Ice-Cream Sundae Rink lost its readable lake, rim, rescue pads, flavour scoops or corner markers");
	}

	private static void prepare(ServerLevel level,
			BlockPos centre) {
		for (int x = -7; x <= 7; x++) {
			for (int z = -7; z <= 7; z++) {
				for (int y = -8; y <= 12; y++) {
					level.setBlock(centre.offset(x, y, z),
							Blocks.AIR.defaultBlockState(), 2);
				}
				level.setBlock(centre.offset(x, 0, z),
						CakeWorldBlocks.ICING.get()
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

	private static void assertSpawn(GameTestHelper helper,
			Biome biome, EntityType<?> type,
			MobCategory category, int weight,
			int minimum, int maximum) {
		MobSpawnSettings.SpawnerData spawn =
				findSpawn(biome, type, category);
		require(helper, spawn != null
						&& spawn.getWeight().asInt() == weight
						&& spawn.minCount == minimum
						&& spawn.maxCount == maximum,
				BIOME_ID + " lost exact "
						+ Registry.ENTITY_TYPE.getKey(type)
						+ " spawning: " + spawn);
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
				IceCreamTundraGameTests.class
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

	private record LocatedRink(
			BlockPos centre,
			int scannedChunks,
			int markerCandidates,
			int tundraColumns) {
	}

	private record SurfaceAudit(
			int tundraColumns,
			int icingLayers,
			int solidIcing) {
	}
}
