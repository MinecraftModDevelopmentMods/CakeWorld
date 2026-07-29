package com.mcmoddev.cakeworld.init;

import java.util.Objects;

import com.mcmoddev.cakeworld.CakeWorld;
import zone.moddev.mc.orespawn.api.OreSpawnBiomes;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldBiomes {
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
	public static final RegistryObject<Biome> FUDGE_WASTES = copy(
			"fudge_wastes", "nether_wastes", 2.0F, 0.0F);
	public static final RegistryObject<Biome> MERINGUE_ISLANDS = copy(
			"meringue_islands", "end_highlands", 0.5F, 0.0F);

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
			BiomeDictionary.addTypes(key(FUDGE_WASTES),
					BiomeDictionary.Type.NETHER,
					BiomeDictionary.Type.HOT,
					BiomeDictionary.Type.DRY,
					BiomeDictionary.Type.WASTELAND);
			BiomeDictionary.addTypes(key(MERINGUE_ISLANDS),
					BiomeDictionary.Type.END,
					BiomeDictionary.Type.VOID,
					BiomeDictionary.Type.MAGICAL);
		});
	}

	private static ResourceKey<Biome> key(RegistryObject<Biome> biome) {
		return ResourceKey.create(Registry.BIOME_REGISTRY, biome.getId());
	}

	private static RegistryObject<Biome> copy(String name, String vanilla,
			float temperature, float downfall) {
		return OreSpawnBiomes.copyAndRegister(BIOMES, name,
				() -> vanilla(vanilla),
				builder -> builder.temperature(temperature).downfall(downfall));
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
}
