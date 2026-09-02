package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.phys.AABB;

/**
 * First edible mineshaft counterpart.
 *
 * <p>The saved structure owns a correctly bounded 41x13x41 underground piece.
 * Its compact first layout supplies the gameplay contracts that identify a
 * mineshaft: branching tunnels, continuous rail, frequent support frames,
 * minecart loot, a cave-spider-role spawner and exposed mining faces. Later
 * variants can add a wider random piece graph without changing the stable
 * configured-structure identity.</p>
 */
public final class WaferMineFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("wafer_mine_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("wafer_mine_structure");
	public static final ResourceLocation POOL_ID =
			id("wafer_mine/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("wafer_mine");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("wafer_mines");
	public static final ResourceLocation LOOT_ID =
			id("chests/wafer_mine");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(40, 12, 40);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/wafer_mine"));
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_KEY =
			ResourceKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					STRUCTURE_ID);
	public static final TagKey<ConfiguredStructureFeature<?, ?>>
			STRUCTURE_TAG =
			TagKey.create(
					Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY,
					STRUCTURE_ID);
	public static final WaferMineFeature FEATURE =
			new WaferMineFeature();
	public static final WaferMineStructureFeature
			STRUCTURE_FEATURE =
			new WaferMineStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private WaferMineFeature() {
		super(NoneFeatureConfiguration.CODEC);
		setRegistryName(PIECE_ID);
	}

	public static void registerWorldgen() {
		Holder<ConfiguredFeature<?, ?>> configuredPiece =
				BuiltinRegistries.register(
						BuiltinRegistries.CONFIGURED_FEATURE,
						PIECE_ID,
						new ConfiguredFeature<
								NoneFeatureConfiguration,
								WaferMineFeature>(
										FEATURE,
										NoneFeatureConfiguration
												.INSTANCE));
		placedFeature = BuiltinRegistries.register(
				BuiltinRegistries.PLACED_FEATURE,
				PIECE_ID,
				new PlacedFeature(configuredPiece, List.of()));
		pool = BuiltinRegistries.register(
				BuiltinRegistries.TEMPLATE_POOL,
				POOL_ID,
				new StructureTemplatePool(
						POOL_ID,
						Pools.EMPTY.location(),
						List.of(Pair.of(
								CakeWorldFeaturePoolElement
										.of(placedFeature,
												MAXIMUM_OFFSET),
								1)),
						StructureTemplatePool
								.Projection.RIGID));
		configuredStructure = BuiltinRegistries.register(
				BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE,
				STRUCTURE_KEY,
				STRUCTURE_FEATURE.configured(
						new JigsawConfiguration(pool, 1),
						GENERATES_IN));
		structureSet = BuiltinRegistries.register(
				BuiltinRegistries.STRUCTURE_SETS,
				STRUCTURE_SET_ID,
				new StructureSet(
						configuredStructure,
						new RandomSpreadStructurePlacement(
								1, 0,
								RandomSpreadType.LINEAR,
								1978003)));
	}

	public static Holder<PlacedFeature> placedFeature() {
		return placedFeature;
	}

	public static Holder<StructureTemplatePool> pool() {
		return pool;
	}

	public static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure() {
		return configuredStructure;
	}

	public static Holder<StructureSet> structureSet() {
		return structureSet;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		return buildAt(context.level(), context.random(),
				context.origin().offset(20, 0, 20));
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		return buildAt(world, random,
				origin.offset(20, 0, 20),
				generationBounds);
	}

	/**
	 * Builds the deterministic first mine plan around its floor-level centre.
	 */
	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre) {
		return buildAt(world, random, centre,
				new BoundingBox(
						centre.getX() - 20,
						centre.getY(),
						centre.getZ() - 20,
						centre.getX() + 20,
						centre.getY() + 12,
						centre.getZ() + 20));
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre,
			BoundingBox generationBounds) {
		clearOldLootMinecarts(world, centre,
				generationBounds);

		digRoom(world, generationBounds,
				centre, 4, 5);
		digCorridor(world, generationBounds, centre,
				Direction.Axis.Z, -20, 20);
		digCorridor(world, generationBounds, centre,
				Direction.Axis.X, -20, 20);
		digCorridor(world, generationBounds,
				centre.offset(-12, 0, 0),
				Direction.Axis.Z, -15, 15);
		digCorridor(world, generationBounds,
				centre.offset(12, 0, 0),
				Direction.Axis.Z, -15, 15);
		digRoom(world, generationBounds,
				centre.offset(12, 0, 10),
				4, 4);
		digRoom(world, generationBounds,
				centre.offset(-12, 0, -10),
				4, 4);

		addSupportFrames(world, generationBounds, centre,
				Direction.Axis.Z, -20, 20);
		addSupportFrames(world, generationBounds, centre,
				Direction.Axis.X, -20, 20);
		addSupportFrames(world, generationBounds,
				centre.offset(-12, 0, 0),
				Direction.Axis.Z, -15, 15);
		addSupportFrames(world, generationBounds,
				centre.offset(12, 0, 0),
				Direction.Axis.Z, -15, 15);

		addRails(world, generationBounds,
				centre, -20, 20);
		addLootMinecart(world, generationBounds, random,
				centre.offset(0, 1, -12));
		addWeaverNest(world, generationBounds,
				centre.offset(12, 0, 10));
		addMiningFaces(world, generationBounds, centre);
		addWayfinding(world, generationBounds, centre);
		return true;
	}

	private static void clearOldLootMinecarts(
			WorldGenLevel world, BlockPos centre,
			BoundingBox generationBounds) {
		BlockPos anchor = centre.offset(0, 1, -12);
		if (!generationBounds.isInside(anchor)) {
			return;
		}
		world.getLevel().getEntitiesOfClass(
				MinecartChest.class,
				new AABB(centre).inflate(24.0D))
				.stream()
				.filter(cart -> LOOT_ID.toString().equals(
						cart.saveWithoutId(
								new CompoundTag())
								.getString(
										"LootTable")))
				.forEach(MinecartChest::discard);
	}

	private static void digCorridor(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre,
			Direction.Axis axis,
			int minimum, int maximum) {
		BlockState floor =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int along = minimum;
				along <= maximum; along++) {
			for (int across = -2;
				across <= 2; across++) {
				BlockPos base = axis
						== Direction.Axis.Z
								? centre.offset(
										across, 0,
										along)
								: centre.offset(
										along, 0,
										across);
				setBlock(world, generationBounds,
						base, floor);
				for (int y = 1; y <= 4; y++) {
					setBlock(world, generationBounds,
							base.above(y),
							Blocks.CAVE_AIR
									.defaultBlockState());
				}
			}
		}
	}

	private static void digRoom(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre,
			int radius, int height) {
		BlockState floor =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				BlockPos base = centre.offset(x, 0, z);
				setBlock(world, generationBounds,
						base, floor);
				for (int y = 1; y <= height; y++) {
					setBlock(world, generationBounds,
							base.above(y),
							Blocks.CAVE_AIR
									.defaultBlockState());
				}
			}
		}
	}

	private static void addSupportFrames(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre,
			Direction.Axis axis,
			int minimum, int maximum) {
		BlockState upright =
				CakeWorldBlocks.CANDY_CANE_PILLAR.get()
						.defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								Direction.Axis.Y);
		BlockState beam =
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState();
		for (int along = firstMultipleOfFive(minimum);
				along <= maximum; along += 5) {
			for (int side : new int[] {-2, 2}) {
				for (int y = 1; y <= 3; y++) {
					BlockPos position = axis
							== Direction.Axis.Z
									? centre.offset(
											side, y,
											along)
									: centre.offset(
											along, y,
											side);
					setBlock(world, generationBounds,
							position, upright);
				}
			}
			for (int across = -2;
					across <= 2; across++) {
				BlockPos position = axis
						== Direction.Axis.Z
								? centre.offset(
										across, 4,
										along)
								: centre.offset(
										along, 4,
										across);
				setBlock(world, generationBounds,
						position, beam);
			}
		}
	}

	private static int firstMultipleOfFive(int minimum) {
		return Math.floorDiv(minimum + 4, 5) * 5;
	}

	private static void addRails(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre,
			int minimum, int maximum) {
		BlockState rail =
				Blocks.RAIL.defaultBlockState()
						.setValue(RailBlock.SHAPE,
								RailShape.NORTH_SOUTH);
		for (int z = minimum; z <= maximum; z++) {
			setBlock(world, generationBounds,
					centre.offset(0, 1, z),
					rail);
		}
	}

	private static void addLootMinecart(
			WorldGenLevel world,
			BoundingBox generationBounds,
			Random random,
			BlockPos position) {
		if (!generationBounds.isInside(position)) {
			return;
		}
		MinecartChest minecart =
				new MinecartChest(world.getLevel(),
						position.getX() + 0.5D,
						position.getY() + 0.5D,
						position.getZ() + 0.5D);
		minecart.setLootTable(LOOT_ID,
				random.nextLong());
		world.addFreshEntity(minecart);
	}

	private static void addWeaverNest(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre) {
		BlockPos spawnerPosition = centre.above();
		if (generationBounds.isInside(
				spawnerPosition)) {
			world.setBlock(spawnerPosition,
					Blocks.SPAWNER
							.defaultBlockState(), 2);
			BlockEntity blockEntity =
					world.getBlockEntity(
							spawnerPosition);
			if (blockEntity
					instanceof SpawnerBlockEntity) {
				((SpawnerBlockEntity)blockEntity)
						.getSpawner().setEntityId(
								CakeWorldEntities
										.DEEP_LIQUORICE_WEAVER
										.get());
				blockEntity.setChanged();
			}
		}
		int[][] webs = {
				{-3, 2, -3}, {-3, 3, 2},
				{3, 2, -2}, {2, 4, 3},
				{-1, 4, -3}, {3, 3, 3}
		};
		for (int[] web : webs) {
			setBlock(world, generationBounds,
					centre.offset(
							web[0], web[1], web[2]),
					Blocks.COBWEB.defaultBlockState(),
					2);
		}
	}

	private static void addMiningFaces(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre) {
		placeOreFace(world, generationBounds,
				centre.offset(
				-14, 2, -15),
				CakeWorldBlocks.COCOA_COAL.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				-12, 2, -15),
				CakeWorldBlocks.IRON_WAFER.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				-10, 2, -15),
				CakeWorldBlocks.COPPER_CARAMEL.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				10, 2, 15),
				CakeWorldBlocks.RASPBERRY_REDSTONE.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				12, 2, 15),
				CakeWorldBlocks.BLUEBERRY_LAPIS.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				14, 2, 15),
				CakeWorldBlocks.ROCK_CANDY_DIAMOND.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				-20, 2, -2),
				CakeWorldBlocks.ROCK_CANDY_DEPOSIT.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				20, 2, 0),
				CakeWorldBlocks.LIQUORICE_VEIN.get()
						.defaultBlockState());
		placeOreFace(world, generationBounds,
				centre.offset(
				-20, 2, 2),
				CakeWorldBlocks.SPRINKLE_CLUSTER.get()
						.defaultBlockState());
	}

	private static void placeOreFace(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre,
			BlockState ore) {
		setBlock(world, generationBounds,
				centre, ore);
		setBlock(world, generationBounds,
				centre.above(), ore);
		setBlock(world, generationBounds,
				centre.east(), ore);
	}

	private static void addWayfinding(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos centre) {
		for (int z : new int[] {-15, -5, 5, 15}) {
			setBlock(world, generationBounds,
					centre.offset(1, 1, z),
					Blocks.TORCH.defaultBlockState());
		}
		setBlock(world, generationBounds,
				centre.offset(-2, 1, 0),
				CakeWorldBlocks.ROCK_CANDY.get()
						.defaultBlockState());
		setBlock(world, generationBounds,
				centre.offset(2, 1, 0),
				CakeWorldBlocks.MINT_CRYSTAL.get()
						.defaultBlockState());
	}

	private static void setBlock(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos position,
			BlockState state) {
		setBlock(world, generationBounds,
				position, state, 2);
	}

	private static void setBlock(
			WorldGenLevel world,
			BoundingBox generationBounds,
			BlockPos position,
			BlockState state,
			int flags) {
		if (generationBounds.isInside(position)) {
			world.setBlock(position, state, flags);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
