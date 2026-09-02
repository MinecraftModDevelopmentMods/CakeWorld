package zone.moddev.mc.cakeworld.world;

import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.CrumbledGingerbreadFolk;
import zone.moddev.mc.cakeworld.entity.GingerbreadFolk;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.phys.AABB;

/**
 * First edible Igloo counterpart.
 *
 * <p>Half of parlours conceal a curing cellar containing a Weakness splash,
 * guaranteed Golden Apple, and CakeWorld's genuine Villager/Zombie-Villager
 * pair. The stable world-seed decision survives bounded late repair and
 * save/reload without storing mutable generation callbacks.</p>
 */
public final class IceCreamParlourFeature
		extends Feature<NoneFeatureConfiguration>
		implements CakeWorldBoundedStructureFeature {
	public static final ResourceLocation PIECE_ID =
			id("ice_cream_parlour_piece");
	public static final ResourceLocation STRUCTURE_FEATURE_ID =
			id("ice_cream_parlour_structure");
	public static final ResourceLocation POOL_ID =
			id("ice_cream_parlour/start");
	public static final ResourceLocation STRUCTURE_ID =
			id("ice_cream_parlour");
	public static final ResourceLocation STRUCTURE_SET_ID =
			id("ice_cream_parlours");
	public static final ResourceLocation LOOT_ID =
			id("chests/ice_cream_parlour");
	public static final Vec3i MAXIMUM_OFFSET =
			new Vec3i(12, 26, 12);
	public static final TagKey<net.minecraft.world.level.biome.Biome>
			GENERATES_IN =
			TagKey.create(Registry.BIOME_REGISTRY,
					id("has_structure/ice_cream_parlour"));
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
	public static final IceCreamParlourFeature FEATURE =
			new IceCreamParlourFeature();
	public static final IceCreamParlourStructureFeature
			STRUCTURE_FEATURE =
			new IceCreamParlourStructureFeature();
	private static Holder<PlacedFeature> placedFeature;
	private static Holder<StructureTemplatePool> pool;
	private static Holder<ConfiguredStructureFeature<?, ?>>
			configuredStructure;
	private static Holder<StructureSet> structureSet;

	static {
		STRUCTURE_FEATURE.setRegistryName(
				STRUCTURE_FEATURE_ID);
	}

	private IceCreamParlourFeature() {
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
								IceCreamParlourFeature>(
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
								32, 8,
								RandomSpreadType.LINEAR,
								14357618)));
	}

	public static Holder<StructureTemplatePool> pool() {
		return pool;
	}

	public static Holder<StructureSet> structureSet() {
		return structureSet;
	}

	@Override
	public boolean place(
			FeaturePlaceContext<NoneFeatureConfiguration> context) {
		BlockPos centre = context.origin().offset(
				6,
				IceCreamParlourStructureFeature
						.BURIED_DEPTH,
				6);
		return buildAt(context.level(), context.random(),
				centre);
	}

	@Override
	public boolean placeInBounds(
			WorldGenLevel world,
			ChunkGenerator chunkGenerator,
			Random random,
			BlockPos origin,
			BoundingBox generationBounds) {
		BlockPos centre = origin.offset(
				6,
				IceCreamParlourStructureFeature
						.BURIED_DEPTH,
				6);
		boolean basement =
				hasBasement(world.getSeed(), centre);
		boolean built = buildAt(world, random, centre,
				basement, generationBounds, false);
		if (built && basement
				&& generationBounds.isInside(
						residentMarker(centre))) {
			IceCreamParlourResidents.queue(
					world, centre,
					fullBounds(centre));
		}
		return built;
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos surfaceCentre) {
		return buildAt(world, random, surfaceCentre,
				hasBasement(world.getSeed(),
						surfaceCentre));
	}

	public static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos surfaceCentre,
			boolean basement) {
		return buildAt(world, random, surfaceCentre,
				basement,
				fullBounds(surfaceCentre), true);
	}

	private static boolean buildAt(
			WorldGenLevel world, Random random,
			BlockPos centre, boolean basement,
			BoundingBox generationBounds,
			boolean spawnResidents) {
		buildSurface(world, centre, basement,
				generationBounds);
		if (basement) {
			buildBasement(world, random, centre,
					generationBounds);
			if (spawnResidents) {
				spawnCuringPair(world, centre,
						generationBounds);
			}
		}
		return true;
	}

	static boolean rebuildInBounds(
			WorldGenLevel world, Random random,
			BlockPos surfaceCentre,
			BoundingBox generationBounds) {
		return buildAt(world, random, surfaceCentre,
				hasBasement(world.getSeed(),
						surfaceCentre),
				generationBounds, false);
	}

	public static boolean hasBasement(
			long worldSeed, BlockPos surfaceCentre) {
		long mixed = worldSeed
				^ ((long)surfaceCentre.getX()
						* 341873128712L)
				^ ((long)surfaceCentre.getZ()
						* 132897987541L)
				^ 14357618L;
		return new Random(mixed).nextBoolean();
	}

	private static BoundingBox fullBounds(
			BlockPos centre) {
		return new BoundingBox(
				centre.getX() - 6,
				centre.getY()
						- IceCreamParlourStructureFeature
								.BURIED_DEPTH,
				centre.getZ() - 6,
				centre.getX() + 6,
				centre.getY() + 7,
				centre.getZ() + 6);
	}

	private static void buildSurface(
			WorldGenLevel world, BlockPos centre,
			boolean basement, BoundingBox bounds) {
		fill(world, bounds, centre,
				-6, 0, -6, 6, 0, 6,
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-5, 0, -5, 5, 0, 5,
				CakeWorldBlocks.FROZEN_LEMONADE.get()
						.defaultBlockState());
		for (int y = 1; y <= 3; y++) {
			for (int x = -5; x <= 5; x++) {
				for (int z = -5; z <= 5; z++) {
					boolean wall =
							Math.abs(x) == 5
									|| Math.abs(z) == 5;
					set(world, bounds,
							centre.offset(x, y, z),
							wall
									? CakeWorldBlocks
											.MARSHMALLOW
											.get()
											.defaultBlockState()
									: Blocks.AIR
											.defaultBlockState());
				}
			}
		}
		for (int x : new int[] {-5, 5}) {
			for (int z : new int[] {-5, 5}) {
				fill(world, bounds, centre,
						x, 1, z, x, 3, z,
						CakeWorldBlocks
								.CANDY_CANE_PILLAR
								.get()
								.defaultBlockState());
			}
		}
		for (int offset : new int[] {-3, -1, 1, 3}) {
			set(world, bounds,
					centre.offset(offset, 2, -5),
					CakeWorldBlocks.CANDY_GLASS.get()
							.defaultBlockState());
			set(world, bounds,
					centre.offset(offset, 2, 5),
					CakeWorldBlocks.CANDY_GLASS.get()
							.defaultBlockState());
			set(world, bounds,
					centre.offset(-5, 2, offset),
					CakeWorldBlocks.CANDY_GLASS.get()
							.defaultBlockState());
			set(world, bounds,
					centre.offset(5, 2, offset),
					CakeWorldBlocks.CANDY_GLASS.get()
							.defaultBlockState());
		}
		fill(world, bounds, centre,
				-1, 1, -5, 1, 3, -5,
				Blocks.AIR.defaultBlockState());
		fill(world, bounds, centre,
				-5, 4, -5, 5, 4, 5,
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-4, 5, -4, 4, 5, 4,
				CakeWorldBlocks.ICING.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-3, 6, -3, 3, 6, 3,
				CakeWorldBlocks.MARSHMALLOW.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-1, 7, -1, 1, 7, 1,
				CakeWorldBlocks.ICING.get()
						.defaultBlockState());
		set(world, bounds, centre.offset(0, 7, 0),
				CakeWorldBlocks.FIZZY_PEARL.get()
						.defaultBlockState());

		fill(world, bounds, centre,
				-4, 1, 1, 4, 1, 1,
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState());
		set(world, bounds, centre.offset(0, 1, 1),
				Blocks.AIR.defaultBlockState());
		set(world, bounds, centre.offset(-3, 1, 3),
				CakeWorldBlocks.OVEN.get()
						.defaultBlockState());
		set(world, bounds, centre.offset(-1, 1, 3),
				CakeWorldBlocks.MIXING_BOWL.get()
						.defaultBlockState());
		set(world, bounds, centre.offset(1, 1, 3),
				CakeWorldBlocks.COOLING_RACK.get()
						.defaultBlockState());
		set(world, bounds, centre.offset(3, 1, 3),
				CakeWorldBlocks.SODA_FOUNTAIN.get()
						.defaultBlockState());
		set(world, bounds, centre.offset(-4, 1, -2),
				Blocks.CRAFTING_TABLE.defaultBlockState());
		placeBed(world, bounds,
				centre.offset(4, 1, -2));

		if (basement) {
			set(world, bounds, centre.offset(0, 0, 2),
					Blocks.OAK_TRAPDOOR
							.defaultBlockState());
		}
	}

	private static void buildBasement(
			WorldGenLevel world, Random random,
			BlockPos centre, BoundingBox bounds) {
		fill(world, bounds, centre,
				-5, -18, -5, 5, -12, 5,
				CakeWorldBlocks.GINGERBREAD_BRICKS.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-4, -17, -4, 4, -13, 4,
				Blocks.AIR.defaultBlockState());
		fill(world, bounds, centre,
				-4, -18, -4, 4, -18, 4,
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState());
		fill(world, bounds, centre,
				-4, -12, -4, 4, -12, 4,
				CakeWorldBlocks.WAFER_BLOCK.get()
						.defaultBlockState());

		BlockState ladder = Blocks.LADDER
				.defaultBlockState()
				.setValue(LadderBlock.FACING,
						Direction.NORTH);
		for (int y = -17; y <= -1; y++) {
			set(world, bounds,
					centre.offset(0, y, 3),
					CakeWorldBlocks.GINGERBREAD_BRICKS
							.get().defaultBlockState());
			set(world, bounds,
					centre.offset(0, y, 2),
					ladder);
		}
		for (int z = -4; z <= 0; z++) {
			set(world, bounds,
					centre.offset(0, -16, z),
					Blocks.IRON_BARS.defaultBlockState());
			set(world, bounds,
					centre.offset(0, -15, z),
					Blocks.IRON_BARS.defaultBlockState());
		}
		for (int x = -4; x <= 4; x++) {
			if (x == -2 || x == 2 || x == 0) {
				continue;
			}
			set(world, bounds,
					centre.offset(x, -16, 0),
					Blocks.IRON_BARS.defaultBlockState());
			set(world, bounds,
					centre.offset(x, -15, 0),
					Blocks.IRON_BARS.defaultBlockState());
		}
		BlockState gate = Blocks.OAK_FENCE_GATE
				.defaultBlockState()
				.setValue(FenceGateBlock.FACING,
						Direction.SOUTH);
		for (int x : new int[] {-2, 2}) {
			set(world, bounds,
					centre.offset(x, -16, 0),
					gate);
			set(world, bounds,
					centre.offset(x, -15, 0),
					Blocks.AIR.defaultBlockState());
		}

		BlockPos brewing = centre.offset(-3, -17, 2);
		set(world, bounds, brewing,
				Blocks.BREWING_STAND.defaultBlockState());
		if (bounds.isInside(brewing)
				&& world.getBlockEntity(brewing)
						instanceof BrewingStandBlockEntity stand) {
			ItemStack weakness =
					new ItemStack(Items.SPLASH_POTION);
			PotionUtils.setPotion(weakness,
					Potions.WEAKNESS);
			stand.setItem(0, weakness);
		}
		BlockPos loot = centre.offset(3, -17, 2);
		if (bounds.isInside(loot)) {
			world.setBlock(loot,
					Blocks.CHEST.defaultBlockState(),
					2);
			RandomizableContainerBlockEntity
					.setLootTable(
							world, random, loot,
							LOOT_ID);
		}
		set(world, bounds, centre.offset(0, -17, 0),
				CakeWorldBlocks.COOKBOOK_LIBRARY.get()
						.defaultBlockState());
		set(world, bounds, residentMarker(centre),
				Blocks.STRUCTURE_VOID
						.defaultBlockState());
		for (BlockPos light : List.of(
				centre.offset(-4, -17, 3),
				centre.offset(4, -17, 3))) {
			set(world, bounds, light,
					Blocks.TORCH
							.defaultBlockState());
		}
	}

	private static void placeBed(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos foot) {
		BlockState state = Blocks.LIGHT_BLUE_BED
				.defaultBlockState()
				.setValue(BedBlock.FACING,
						Direction.SOUTH);
		set(world, bounds, foot,
				state.setValue(BedBlock.PART,
						BedPart.FOOT));
		set(world, bounds, foot.south(),
				state.setValue(BedBlock.PART,
						BedPart.HEAD));
	}

	static boolean spawnCuringPair(
			WorldGenLevel world, BlockPos centre,
			BoundingBox bounds) {
		BlockPos folkPosition =
				centre.offset(-2, -17, -3);
		if (bounds.isInside(folkPosition)) {
			world.getLevel().getEntitiesOfClass(
					GingerbreadFolk.class,
					new AABB(folkPosition)
							.inflate(1.0D))
					.forEach(GingerbreadFolk::discard);
			GingerbreadFolk folk =
					CakeWorldEntities.GINGERBREAD_FOLK
							.get().create(
									world.getLevel());
			if (folk != null) {
				folk.moveTo(folkPosition,
						0.0F, 0.0F);
				folk.finalizeSpawn(world,
						world.getCurrentDifficultyAt(
								folkPosition),
						MobSpawnType.STRUCTURE,
						null, null);
				folk.setVillagerData(
						folk.getVillagerData()
								.setType(
										VillagerType.SNOW)
								.setProfession(
										VillagerProfession
												.CLERIC));
				BlockPos job =
						centre.offset(-3, -17, 2);
				world.getLevel().getPoiManager()
						.add(job, PoiType.CLERIC);
				world.getLevel().getPoiManager()
						.take(PoiType.CLERIC::equals,
								job::equals,
								job, 1);
				folk.getBrain().setMemory(
						MemoryModuleType.JOB_SITE,
						GlobalPos.of(
								world.getLevel()
										.dimension(),
								job));
				folk.setPersistenceRequired();
				folk.restrictTo(folkPosition, 6);
				world.addFreshEntity(folk);
			}
		}

		BlockPos crumbPosition =
				centre.offset(2, -17, -3);
		if (bounds.isInside(crumbPosition)) {
			world.getLevel().getEntitiesOfClass(
					CrumbledGingerbreadFolk.class,
					new AABB(crumbPosition)
							.inflate(1.0D))
					.forEach(
							CrumbledGingerbreadFolk
									::discard);
			CrumbledGingerbreadFolk crumb =
					CakeWorldEntities
							.CRUMBLED_GINGERBREAD_FOLK
							.get().create(
									world.getLevel());
			if (crumb != null) {
				crumb.moveTo(crumbPosition,
						180.0F, 0.0F);
				crumb.finalizeSpawn(world,
						world.getCurrentDifficultyAt(
								crumbPosition),
						MobSpawnType.STRUCTURE,
						null, null);
				crumb.setVillagerData(
						crumb.getVillagerData()
								.setType(
										VillagerType.SNOW)
								.setProfession(
										VillagerProfession
												.CLERIC));
				crumb.setPersistenceRequired();
				crumb.restrictTo(crumbPosition, 6);
				world.addFreshEntity(crumb);
			}
		}

		AABB cellar = new AABB(
				centre.offset(-5, -18, -5),
				centre.offset(6, -11, 6));
		boolean complete =
				world.getLevel().getEntitiesOfClass(
						GingerbreadFolk.class,
						cellar).size() == 1
						&& world.getLevel()
								.getEntitiesOfClass(
										CrumbledGingerbreadFolk
												.class,
										cellar)
								.size() == 1;
		if (complete) {
			set(world, bounds, residentMarker(centre),
					CakeWorldBlocks
							.GINGERBREAD_BRICKS.get()
							.defaultBlockState());
		}
		return complete;
	}

	static BlockPos residentMarker(BlockPos centre) {
		return centre.offset(0, -17, 5);
	}

	private static void fill(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos centre,
			int minimumX, int minimumY, int minimumZ,
			int maximumX, int maximumY, int maximumZ,
			BlockState state) {
		for (int x = minimumX; x <= maximumX; x++) {
			for (int y = minimumY;
					y <= maximumY; y++) {
				for (int z = minimumZ;
						z <= maximumZ; z++) {
					set(world, bounds,
							centre.offset(x, y, z),
							state);
				}
			}
		}
	}

	private static void set(
			WorldGenLevel world, BoundingBox bounds,
			BlockPos position, BlockState state) {
		if (bounds.isInside(position)) {
			world.setBlock(position, state, 2);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}
}
