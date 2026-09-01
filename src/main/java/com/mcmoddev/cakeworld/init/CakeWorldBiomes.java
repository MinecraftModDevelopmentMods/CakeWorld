package com.mcmoddev.cakeworld.init;

import java.util.Objects;
import java.util.Set;

import com.mcmoddev.cakeworld.CakeWorld;
import zone.moddev.mc.orespawn.api.OreSpawnBiomes;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldBiomes {
	private static final Set<ResourceKey<PlacedFeature>>
			VANILLA_OVERWORLD_INEDIBLE_FEATURES = Set.of(
					placedFeatureKey("ore_dirt"),
					placedFeatureKey("ore_gravel"),
					placedFeatureKey("ore_granite_upper"),
					placedFeatureKey("ore_granite_lower"),
					placedFeatureKey("ore_diorite_upper"),
					placedFeatureKey("ore_diorite_lower"),
					placedFeatureKey("ore_andesite_upper"),
					placedFeatureKey("ore_andesite_lower"),
					placedFeatureKey("ore_tuff"),
					placedFeatureKey("ore_coal_upper"),
					placedFeatureKey("ore_coal_lower"),
					placedFeatureKey("ore_iron_upper"),
					placedFeatureKey("ore_iron_middle"),
					placedFeatureKey("ore_iron_small"),
					placedFeatureKey("ore_gold_extra"),
					placedFeatureKey("ore_gold"),
					placedFeatureKey("ore_gold_lower"),
					placedFeatureKey("ore_redstone"),
					placedFeatureKey("ore_redstone_lower"),
					placedFeatureKey("ore_diamond"),
					placedFeatureKey("ore_diamond_large"),
					placedFeatureKey("ore_diamond_buried"),
					placedFeatureKey("ore_lapis"),
					placedFeatureKey("ore_lapis_buried"),
					placedFeatureKey("ore_infested"),
					placedFeatureKey("ore_emerald"),
					placedFeatureKey("ore_copper"),
					placedFeatureKey("ore_copper_large"),
					placedFeatureKey("ore_clay"),
					placedFeatureKey("disk_sand"),
					placedFeatureKey("disk_gravel"),
					placedFeatureKey("disk_clay"),
					placedFeatureKey("underwater_magma"),
					placedFeatureKey("amethyst_geode"));
	private static final Set<ResourceKey<PlacedFeature>>
			VANILLA_NETHER_INEDIBLE_FEATURES = Set.of(
					placedFeatureKey("delta"),
					placedFeatureKey("ore_magma"),
					placedFeatureKey("ore_soul_sand"),
					placedFeatureKey("ore_gold_deltas"),
					placedFeatureKey("ore_quartz_deltas"),
					placedFeatureKey("ore_gold_nether"),
					placedFeatureKey("ore_quartz_nether"),
					placedFeatureKey("ore_gravel_nether"),
					placedFeatureKey("ore_blackstone"),
					placedFeatureKey("ore_ancient_debris_large"),
					placedFeatureKey("ore_debris_small"));
	private static final ResourceKey<PlacedFeature>
			SMALL_BASALT_COLUMNS = placedFeatureKey(
					"small_basalt_columns");
	private static final ResourceKey<PlacedFeature>
			LARGE_BASALT_COLUMNS = placedFeatureKey(
					"large_basalt_columns");
	private static final ResourceKey<PlacedFeature>
			BASALT_BLOBS = placedFeatureKey("basalt_blobs");
	private static final ResourceKey<PlacedFeature>
			BLACKSTONE_BLOBS = placedFeatureKey("blackstone_blobs");
	private static final DeferredRegister<Biome> BIOMES =
			DeferredRegister.create(ForgeRegistries.BIOMES, CakeWorld.MODID);

	public static final RegistryObject<Biome> CANDY_PLAINS = copy(
			"candy_plains", "plains", 0.8F, 0.4F);
	public static final RegistryObject<Biome> GINGERBREAD_HEARTHLANDS =
			hearthlands();
	public static final RegistryObject<Biome> COOKIE_FOREST =
			cookieForest();
	public static final RegistryObject<Biome> PEPPERMINT_PINEWOODS =
			peppermintPinewoods();
	public static final RegistryObject<Biome> GUMMY_JUNGLE =
			gummyJungle();
	public static final RegistryObject<Biome> CARAMEL_BOGS =
			caramelBogs();
	public static final RegistryObject<Biome> SHERBET_DUNES =
			sherbetDunes();
	public static final RegistryObject<Biome> CANDY_CANE_BADLANDS =
			candyCaneBadlands();
	public static final RegistryObject<Biome> MARSHMALLOW_PEAKS =
			marshmallowPeaks();
	public static final RegistryObject<Biome> ICE_CREAM_TUNDRA =
			iceCreamTundra();
	public static final RegistryObject<Biome> WAFFLE_PLATEAUS =
			wafflePlateaus();
	public static final RegistryObject<Biome> CUPCAKE_GARDENS =
			cupcakeGardens();
	public static final RegistryObject<Biome> LIQUORICE_DARKWOOD =
			liquoriceDarkwood();
	public static final RegistryObject<Biome> LOLLIPOP_ORCHARDS =
			lollipopOrchards();
	public static final RegistryObject<Biome> POPCORN_PRAIRIE =
			popcornPrairie();
	public static final RegistryObject<Biome> SODA_OCEAN =
			sodaOcean();
	public static final RegistryObject<Biome> CUSTARD_COAST =
			custardCoast();
	public static final RegistryObject<Biome> JELLYBEAN_ARCHIPELAGO =
			jellybeanArchipelago();
	public static final RegistryObject<Biome> ROCK_CANDY_CAVERNS =
			rockCandyCaverns();
	public static final RegistryObject<Biome> JAM_GROTTOES =
			jamGrottoes();
	public static final RegistryObject<Biome> NOUGAT_DEPTHS =
			nougatDepths();
	public static final RegistryObject<Biome> BURNT_TOFFEE_DELTAS =
			burntToffeeDeltas();
	public static final RegistryObject<Biome> FUDGE_WASTES =
			fudgeWastes();
	public static final RegistryObject<Biome> CINNAMON_EMBER_GROVES =
			cinnamonEmberGroves();
	public static final RegistryObject<Biome> BLACK_LIQUORICE_LABYRINTHS =
			blackLiquoriceLabyrinths();
	public static final RegistryObject<Biome> TREACLE_SOUL_VALLEYS =
			treacleSoulValleys();
	public static final RegistryObject<Biome> CHILLI_CHOCOLATE_CRAGS =
			chilliChocolateCrags();
	public static final RegistryObject<Biome> MOLTEN_MARSHMALLOW_CALDERAS =
			moltenMarshmallowCalderas();
	public static final RegistryObject<Biome> MERINGUE_ISLANDS =
			meringueIslands();
	public static final RegistryObject<Biome> CANDYFLOSS_CLOUDBANKS =
			candyflossCloudbanks();
	public static final RegistryObject<Biome> MOONCAKE_BARRENS =
			mooncakeBarrens();
	public static final RegistryObject<Biome> STARLIGHT_SUGAR_FIELDS =
			starlightSugarFields();
	public static final RegistryObject<Biome> MACARON_ARCHIPELAGO =
			macaronArchipelago();
	public static final RegistryObject<Biome> COSMIC_JELLY_REEFS =
			cosmicJellyReefs();
	public static final RegistryObject<Biome> FONDANT_CHORUS_GARDENS =
			fondantChorusGardens();

	private CakeWorldBiomes() {
	}

	public static void register(IEventBus modBus) {
		BIOMES.register(modBus);
	}

	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BiomeDictionary.addTypes(key(CANDY_PLAINS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS);
			BiomeDictionary.addTypes(
					key(GINGERBREAD_HEARTHLANDS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS);
			BiomeDictionary.addTypes(key(COOKIE_FOREST),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.DENSE,
					BiomeDictionary.Type.WET);
			BiomeDictionary.addTypes(
					key(PEPPERMINT_PINEWOODS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.CONIFEROUS,
					BiomeDictionary.Type.COLD,
					BiomeDictionary.Type.SNOWY);
			BiomeDictionary.addTypes(key(GUMMY_JUNGLE),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.DENSE,
					BiomeDictionary.Type.WET,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.JUNGLE,
					BiomeDictionary.Type.LUSH);
			BiomeDictionary.addTypes(key(CARAMEL_BOGS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.SWAMP,
					BiomeDictionary.Type.WET);
			BiomeDictionary.addTypes(key(SHERBET_DUNES),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.SANDY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(
					key(CANDY_CANE_BADLANDS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.SANDY,
					BiomeDictionary.Type.MESA,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(key(MARSHMALLOW_PEAKS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.MOUNTAIN,
					BiomeDictionary.Type.PEAK,
					BiomeDictionary.Type.COLD,
					BiomeDictionary.Type.SNOWY);
			BiomeDictionary.addTypes(key(ICE_CREAM_TUNDRA),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS,
					BiomeDictionary.Type.COLD,
					BiomeDictionary.Type.SNOWY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(key(WAFFLE_PLATEAUS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.MOUNTAIN,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.SAVANNA);
			BiomeDictionary.addTypes(key(CUPCAKE_GARDENS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS,
					BiomeDictionary.Type.LUSH);
			BiomeDictionary.addTypes(key(LIQUORICE_DARKWOOD),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.DENSE,
					BiomeDictionary.Type.SPOOKY);
			BiomeDictionary.addTypes(key(LOLLIPOP_ORCHARDS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.LUSH);
			BiomeDictionary.addTypes(key(POPCORN_PRAIRIE),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.PLAINS,
					BiomeDictionary.Type.DRY);
			BiomeDictionary.addTypes(key(SODA_OCEAN),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.OCEAN,
					BiomeDictionary.Type.WATER);
			BiomeDictionary.addTypes(key(CUSTARD_COAST),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.BEACH);
			BiomeDictionary.addTypes(
					key(JELLYBEAN_ARCHIPELAGO),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.RARE,
					BiomeDictionary.Type.LUSH);
			BiomeDictionary.addTypes(
					key(ROCK_CANDY_CAVERNS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.UNDERGROUND,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.SPARSE);
			BiomeDictionary.addTypes(
					key(JAM_GROTTOES),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.UNDERGROUND,
					BiomeDictionary.Type.LUSH,
					BiomeDictionary.Type.WET,
					BiomeDictionary.Type.MAGICAL);
			BiomeDictionary.addTypes(
					key(NOUGAT_DEPTHS),
					BiomeDictionary.Type.OVERWORLD,
					BiomeDictionary.Type.UNDERGROUND,
					BiomeDictionary.Type.DENSE,
					BiomeDictionary.Type.MAGICAL);
			BiomeDictionary.addTypes(key(FUDGE_WASTES),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(
					key(BURNT_TOFFEE_DELTAS),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(
					key(CINNAMON_EMBER_GROVES),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.FOREST);
			BiomeDictionary.addTypes(
					key(BLACK_LIQUORICE_LABYRINTHS),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.FOREST,
					BiomeDictionary.Type.DENSE,
					BiomeDictionary.Type.SPOOKY);
			BiomeDictionary.addTypes(
					key(TREACLE_SOUL_VALLEYS),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.SPARSE,
					BiomeDictionary.Type.SPOOKY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(
					key(CHILLI_CHOCOLATE_CRAGS),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.MOUNTAIN,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(
					key(MOLTEN_MARSHMALLOW_CALDERAS),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.MOUNTAIN,
					BiomeDictionary.Type.MAGICAL);
			BiomeDictionary.addTypes(key(MERINGUE_ISLANDS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL);
			BiomeDictionary.addTypes(key(CANDYFLOSS_CLOUDBANKS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.RARE);
			BiomeDictionary.addTypes(key(MOONCAKE_BARRENS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.SPARSE,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(key(STARLIGHT_SUGAR_FIELDS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.PLAINS);
			BiomeDictionary.addTypes(key(MACARON_ARCHIPELAGO),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.RARE);
			BiomeDictionary.addTypes(key(COSMIC_JELLY_REEFS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.RARE);
			BiomeDictionary.addTypes(key(FONDANT_CHORUS_GARDENS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL,
					BiomeDictionary.Type.LUSH);
		});
	}

	private static ResourceKey<Biome> key(RegistryObject<Biome> biome) {
		return ResourceKey.create(Registry.BIOME_REGISTRY, biome.getId());
	}

	private static RegistryObject<Biome> copy(String name, String vanilla,
			float temperature, float downfall) {
		return OreSpawnBiomes.copyAndRegister(BIOMES, name,
				() -> vanilla(vanilla),
				builder -> builder.temperature(temperature).downfall(downfall)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla(vanilla))));
	}

	private static RegistryObject<Biome> hearthlands() {
		return copyWithAmbient("gingerbread_hearthlands",
				"plains", 0.85F, 0.55F,
				CakeWorldSounds.HEARTHLANDS_CHIME,
				0.001D);
	}

	private static RegistryObject<Biome> cookieForest() {
		return copyWithAmbient("cookie_forest",
				"forest", 0.7F, 0.8F,
				CakeWorldSounds.COOKIE_FOREST_RUSTLE,
				0.0012D);
	}

	private static RegistryObject<Biome> peppermintPinewoods() {
		return copyWithAmbient("peppermint_pinewoods",
				"snowy_taiga", -0.2F, 0.7F,
				CakeWorldSounds.PEPPERMINT_PINEWOODS_CHIME,
				0.001D);
	}

	private static RegistryObject<Biome> gummyJungle() {
		return copyWithAmbient("gummy_jungle",
				"bamboo_jungle", 0.95F, 0.95F,
				CakeWorldSounds.GUMMY_JUNGLE_WOBBLE,
				0.0014D);
	}

	private static RegistryObject<Biome> caramelBogs() {
		return copyWithAmbient("caramel_bogs",
				"swamp", 0.8F, 0.9F,
				CakeWorldSounds.CARAMEL_BOGS_BUBBLE,
				0.0012D);
	}

	private static RegistryObject<Biome> sherbetDunes() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"sherbet_dunes",
				() -> vanilla("desert"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("desert")))
						.specialEffects(
								withSherbetEffects(
										vanilla("desert")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> candyCaneBadlands() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"candy_cane_badlands",
				() -> vanilla("badlands"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("badlands")))
						.specialEffects(
								withCandyCaneBadlandsEffects(
										vanilla("badlands")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> marshmallowPeaks() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"marshmallow_peaks",
				() -> vanilla("jagged_peaks"),
				builder -> builder
						.temperature(-0.3F)
						.downfall(0.5F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("jagged_peaks")))
						.specialEffects(
								withMarshmallowPeaksEffects(
										vanilla("jagged_peaks")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> iceCreamTundra() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"ice_cream_tundra",
				() -> vanilla("snowy_plains"),
				builder -> builder
						.temperature(-0.5F)
						.downfall(0.4F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("snowy_plains")))
						.specialEffects(
								withIceCreamTundraEffects(
										vanilla("snowy_plains")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> wafflePlateaus() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"waffle_plateaus",
				() -> vanilla("savanna_plateau"),
				builder -> builder
						.temperature(0.9F)
						.downfall(0.1F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("savanna_plateau")))
						.specialEffects(
								withWafflePlateauEffects(
										vanilla("savanna_plateau")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> cupcakeGardens() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"cupcake_gardens",
				() -> vanilla("sunflower_plains"),
				builder -> builder
						.temperature(0.75F)
						.downfall(0.8F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("sunflower_plains")))
						.specialEffects(
								withCupcakeGardenEffects(
										vanilla("sunflower_plains")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> liquoriceDarkwood() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"liquorice_darkwood",
				() -> vanilla("dark_forest"),
				builder -> builder
						.temperature(0.7F)
						.downfall(0.8F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("dark_forest")))
						.specialEffects(
								withLiquoriceDarkwoodEffects(
										vanilla("dark_forest")
												.getSpecialEffects())));
	}

	private static RegistryObject<Biome> lollipopOrchards() {
		return copyWithAmbient("lollipop_orchards",
				"birch_forest", 0.7F, 0.7F,
				CakeWorldSounds.LOLLIPOP_ORCHARDS_CHIME,
				0.0011D);
	}

	private static RegistryObject<Biome> popcornPrairie() {
		return copyWithAmbient("popcorn_prairie",
				"plains", 0.8F, 0.35F,
				CakeWorldSounds.POPCORN_PRAIRIE_RUSTLE,
				0.0014D);
	}

	private static RegistryObject<Biome> sodaOcean() {
		return copyWithAmbient("soda_ocean",
				"ocean", 0.5F, 0.5F,
				CakeWorldSounds.SODA_OCEAN_FIZZ,
				0.0015D);
	}

	private static RegistryObject<Biome> custardCoast() {
		return copyWithAmbient("custard_coast",
				"beach", 0.8F, 0.4F,
				CakeWorldSounds.CUSTARD_COAST_LAP,
				0.0013D);
	}

	private static RegistryObject<Biome> jellybeanArchipelago() {
		return copyWithAmbient("jellybean_archipelago",
				"mushroom_fields", 0.9F, 1.0F,
				CakeWorldSounds
						.JELLYBEAN_ARCHIPELAGO_CHIMES,
				0.0014D);
	}

	private static RegistryObject<Biome> rockCandyCaverns() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"rock_candy_caverns",
				() -> vanilla("dripstone_caves"),
				builder -> builder
						.temperature(0.8F)
						.downfall(0.4F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("dripstone_caves")))
						.specialEffects(
								effectsBuilder(
										vanilla("dripstone_caves")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.END_ROD,
														0.0012F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.ROCK_CANDY_CAVERNS_CHIME
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> jamGrottoes() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"jam_grottoes",
				() -> vanilla("lush_caves"),
				builder -> builder
						.temperature(0.7F)
						.downfall(0.9F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("lush_caves")))
						.specialEffects(
								effectsBuilder(
										vanilla("lush_caves")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes
																.SPORE_BLOSSOM_AIR,
														0.0015F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.JAM_GROTTOES_DRIP
																.get(),
														0.0014D))
										.build()));
	}

	private static RegistryObject<Biome> nougatDepths() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"nougat_depths",
				() -> vanilla("dripstone_caves"),
				builder -> builder
						.temperature(0.6F)
						.downfall(0.5F)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla("dripstone_caves")))
						.specialEffects(
								effectsBuilder(
										vanilla("dripstone_caves")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes
																.WHITE_ASH,
														0.0009F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.NOUGAT_DEPTHS_SETTLE
																.get(),
														0.0011D))
										.build()));
	}

	private static RegistryObject<Biome> burntToffeeDeltas() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"burnt_toffee_deltas",
				() -> vanilla("basalt_deltas"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaBasaltDeltaFeatures(
										vanilla("basalt_deltas")))
						.specialEffects(
								effectsBuilder(
										vanilla("basalt_deltas")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.ASH,
														0.006F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.BURNT_TOFFEE_DELTAS_SNAP
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> fudgeWastes() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"fudge_wastes",
				() -> vanilla("nether_wastes"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaNetherGravel(
										vanilla("nether_wastes")))
						.specialEffects(
								effectsBuilder(
										vanilla("nether_wastes")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes
																.DRIPPING_LAVA,
														0.003F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.FUDGE_WASTES_BUBBLE
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> cinnamonEmberGroves() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"cinnamon_ember_groves",
				() -> vanilla("crimson_forest"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaNetherGravel(
										vanilla("crimson_forest")))
						.specialEffects(
								effectsBuilder(
										vanilla("crimson_forest")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.FLAME,
														0.0025F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.CINNAMON_EMBER_GROVES_CRACKLE
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> blackLiquoriceLabyrinths() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"black_liquorice_labyrinths",
				() -> vanilla("warped_forest"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaNetherGravel(
										vanilla("warped_forest")))
						.specialEffects(
								effectsBuilder(
										vanilla("warped_forest")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.ASH,
														0.0015F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.BLACK_LIQUORICE_LABYRINTHS_CREAK
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> treacleSoulValleys() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"treacle_soul_valleys",
				() -> vanilla("soul_sand_valley"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaSoulValleyTerrain(
										vanilla("soul_sand_valley")))
						.specialEffects(
								effectsBuilder(
										vanilla("soul_sand_valley")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes
																.SOUL_FIRE_FLAME,
														0.002F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.TREACLE_SOUL_VALLEYS_MURMUR
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> chilliChocolateCrags() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"chilli_chocolate_crags",
				() -> vanilla("nether_wastes"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaNetherGravel(
										vanilla("nether_wastes")))
						.specialEffects(
								effectsBuilder(
										vanilla("nether_wastes")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes
																.DRIPPING_LAVA,
														0.006F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.CHILLI_CHOCOLATE_CRAGS_RUMBLE
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> moltenMarshmallowCalderas() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"molten_marshmallow_calderas",
				() -> vanilla("basalt_deltas"),
				builder -> builder
						.temperature(2.0F)
						.downfall(0.0F)
						.generationSettings(
								withoutVanillaBasaltDeltaFeatures(
										vanilla("basalt_deltas")))
						.specialEffects(
								effectsBuilder(
										vanilla("basalt_deltas")
												.getSpecialEffects())
									.ambientParticle(
											new AmbientParticleSettings(
													ParticleTypes.CLOUD,
													0.004F))
									.ambientAdditionsSound(
											new AmbientAdditionsSettings(
													CakeWorldSounds
															.MOLTEN_MARSHMALLOW_CALDERAS_HISS
															.get(),
													0.0015D))
									.build()));
	}

	private static RegistryObject<Biome> meringueIslands() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"meringue_islands",
				() -> vanilla("end_highlands"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("end_highlands")
												.getSpecialEffects())
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.END_ROD,
														0.0012F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.MERINGUE_ISLANDS_CHIME
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> candyflossCloudbanks() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"candyfloss_cloudbanks",
				() -> vanilla("small_end_islands"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("small_end_islands")
												.getSpecialEffects())
										.fogColor(0xF3B6D5)
										.skyColor(0xC58BDE)
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.CLOUD,
														0.003F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.CANDYFLOSS_CLOUDBANKS_FLUTTER
																.get(),
														0.0015D))
										.build()));
	}

	private static RegistryObject<Biome> mooncakeBarrens() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"mooncake_barrens",
				() -> vanilla("end_barrens"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("end_barrens")
												.getSpecialEffects())
										.fogColor(0xD8B66A)
										.skyColor(0x8C74A8)
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.FALLING_HONEY,
														0.0012F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.MOONCAKE_BARRENS_WHISPER
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> starlightSugarFields() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"starlight_sugar_fields",
				() -> vanilla("end_midlands"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("end_midlands")
												.getSpecialEffects())
										.fogColor(0x3B386F)
										.skyColor(0x17122E)
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.ELECTRIC_SPARK,
														0.0015F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.STARLIGHT_SUGAR_FIELDS_TWINKLE
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> macaronArchipelago() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"macaron_archipelago",
				() -> vanilla("end_highlands"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("end_highlands")
												.getSpecialEffects())
										.fogColor(0xE8A4C8)
										.skyColor(0x77518E)
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.WAX_ON,
														0.0015F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.MACARON_ARCHIPELAGO_CHIME
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> cosmicJellyReefs() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"cosmic_jelly_reefs",
				() -> vanilla("end_midlands"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("end_midlands")
												.getSpecialEffects())
										.fogColor(0x4B2B78)
										.skyColor(0x25153F)
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.GLOW,
														0.002F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.COSMIC_JELLY_REEF_PULSE
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> fondantChorusGardens() {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				"fondant_chorus_gardens",
				() -> vanilla("end_highlands"),
				builder -> builder
						.temperature(0.5F)
						.downfall(0.0F)
						.specialEffects(
								effectsBuilder(
										vanilla("end_highlands")
												.getSpecialEffects())
										.fogColor(0xD68BC4)
										.skyColor(0x8A5B9E)
										.ambientParticle(
												new AmbientParticleSettings(
														ParticleTypes.END_ROD,
														0.0015F))
										.ambientAdditionsSound(
												new AmbientAdditionsSettings(
														CakeWorldSounds
																.FONDANT_CHORUS_GARDEN_BELL
																.get(),
														0.0012D))
										.build()));
	}

	private static RegistryObject<Biome> copyWithAmbient(
			String name, String vanilla,
			float temperature, float downfall,
			RegistryObject<net.minecraft.sounds.SoundEvent> sound,
			double chance) {
		return OreSpawnBiomes.copyAndRegister(BIOMES,
				name,
				() -> vanilla(vanilla),
				builder -> builder
						.temperature(temperature)
						.downfall(downfall)
						.generationSettings(
								withoutVanillaOverworldInedibleFeatures(
										vanilla(vanilla)))
						.specialEffects(
								withAmbientAdditions(
										vanilla(vanilla)
												.getSpecialEffects(),
										sound.get(),
										chance)));
	}

	private static BiomeSpecialEffects withAmbientAdditions(
			BiomeSpecialEffects source,
			net.minecraft.sounds.SoundEvent sound,
			double chance) {
		return effectsBuilder(source)
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								sound, chance))
				.build();
	}

	private static BiomeSpecialEffects withSherbetEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.END_ROD, 0.0015F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.SHERBET_DUNES_FIZZ
										.get(),
								0.0012D))
				.build();
	}

	private static BiomeSpecialEffects withCandyCaneBadlandsEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.CRIT, 0.0008F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.CANDY_CANE_BADLANDS_CHIME
										.get(),
								0.001D))
				.build();
	}

	private static BiomeSpecialEffects withMarshmallowPeaksEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.CLOUD, 0.0008F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.MARSHMALLOW_PEAKS_BREEZE
										.get(),
								0.001D))
				.build();
	}

	private static BiomeSpecialEffects withIceCreamTundraEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.SNOWFLAKE, 0.001F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.ICE_CREAM_TUNDRA_CHILL
										.get(),
								0.001D))
				.build();
	}

	private static BiomeSpecialEffects withWafflePlateauEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.FALLING_HONEY, 0.0008F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.WAFFLE_PLATEAUS_BREEZE
										.get(),
								0.001D))
				.build();
	}

	private static BiomeSpecialEffects withCupcakeGardenEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.FALLING_NECTAR, 0.0012F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.CUPCAKE_GARDENS_HUM
										.get(),
								0.0012D))
				.build();
	}

	private static BiomeSpecialEffects withLiquoriceDarkwoodEffects(
			BiomeSpecialEffects source) {
		return effectsBuilder(source)
				.ambientParticle(new AmbientParticleSettings(
						ParticleTypes.MYCELIUM, 0.0009F))
				.ambientAdditionsSound(
						new AmbientAdditionsSettings(
								CakeWorldSounds
										.LIQUORICE_DARKWOOD_RUSTLE
										.get(),
								0.001D))
				.build();
	}

	private static BiomeSpecialEffects.Builder effectsBuilder(
			BiomeSpecialEffects source) {
		BiomeSpecialEffects.Builder builder =
				new BiomeSpecialEffects.Builder()
						.fogColor(source.getFogColor())
						.waterColor(source.getWaterColor())
						.waterFogColor(
								source.getWaterFogColor())
						.skyColor(source.getSkyColor())
						.grassColorModifier(
								source.getGrassColorModifier());
		source.getFoliageColorOverride()
				.ifPresent(builder::foliageColorOverride);
		source.getGrassColorOverride()
				.ifPresent(builder::grassColorOverride);
		source.getAmbientParticleSettings()
				.ifPresent(builder::ambientParticle);
		source.getAmbientLoopSoundEvent()
				.ifPresent(builder::ambientLoopSound);
		source.getAmbientMoodSettings()
				.ifPresent(builder::ambientMoodSound);
		source.getAmbientAdditionsSettings()
				.ifPresent(builder::ambientAdditionsSound);
		source.getBackgroundMusic()
				.ifPresent(builder::backgroundMusic);
		return builder;
	}

	private static Biome vanilla(String name) {
		ResourceLocation sourceId =
				new ResourceLocation("minecraft", name);
		return Objects.requireNonNull(
				ForgeRegistries.BIOMES.getValue(sourceId),
				"Missing vanilla biome " + sourceId);
	}

	private static BiomeGenerationSettings withoutVanillaBasaltDeltaFeatures(
			Biome source) {
		return withoutVanillaNetherFeatures(source, true);
	}

	private static BiomeGenerationSettings
			withoutVanillaOverworldInedibleFeatures(Biome source) {
		return withoutPlacedFeatures(source,
				VANILLA_OVERWORLD_INEDIBLE_FEATURES, true);
	}

	private static BiomeGenerationSettings withoutVanillaNetherGravel(
			Biome source) {
		return withoutVanillaNetherFeatures(source, false);
	}

	private static BiomeGenerationSettings withoutVanillaSoulValleyTerrain(
			Biome source) {
		return withoutVanillaNetherFeatures(source, false);
	}

	private static BiomeGenerationSettings withoutVanillaNetherFeatures(
			Biome source, boolean removeBasaltFeatures) {
		Set<ResourceKey<PlacedFeature>> removals =
				new java.util.HashSet<>(VANILLA_NETHER_INEDIBLE_FEATURES);
		if (removeBasaltFeatures) {
			removals.addAll(Set.of(SMALL_BASALT_COLUMNS,
					LARGE_BASALT_COLUMNS, BASALT_BLOBS,
					BLACKSTONE_BLOBS));
		}
		return withoutPlacedFeatures(source, removals, false);
	}

	private static BiomeGenerationSettings withoutPlacedFeatures(
			Biome source, Set<ResourceKey<PlacedFeature>> removals,
			boolean removeVanillaTrees) {
		BiomeGenerationSettings sourceSettings =
				source.getGenerationSettings();
		BiomeGenerationSettings.Builder builder =
				new BiomeGenerationSettings.Builder();
		for (GenerationStep.Carving stage
				: sourceSettings.getCarvingStages()) {
			for (Holder<ConfiguredWorldCarver<?>> carver
					: sourceSettings.getCarvers(stage)) {
				builder.addCarver(stage, carver);
			}
		}
		for (int step = 0;
				step < sourceSettings.features().size(); step++) {
			HolderSet<PlacedFeature> features =
					sourceSettings.features().get(step);
			for (Holder<PlacedFeature> feature : features) {
				if (removals.stream().noneMatch(feature::is)
						&& !(removeVanillaTrees
								&& isVanillaTreeFeature(feature))) {
					builder.addFeature(step, feature);
				}
			}
		}
		return builder.build();
	}

	private static boolean isVanillaTreeFeature(
			Holder<PlacedFeature> feature) {
		return feature.unwrapKey().map(ResourceKey::location)
				.filter(id -> "minecraft".equals(id.getNamespace()))
				.map(ResourceLocation::getPath)
				.filter(path -> path.startsWith("trees_"))
				.isPresent();
	}

	private static ResourceKey<PlacedFeature> placedFeatureKey(
			String path) {
		return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
				new ResourceLocation("minecraft", path));
	}
}
