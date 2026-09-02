package zone.moddev.mc.cakeworld.gametest;

import java.util.ArrayList;
import java.util.List;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.CheckerboardColumnBiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.OreSpawnBiomes;

/** Opt-in source biomes for exact OreSpawn climate-boundary verification. */
public final class ClimateBoundaryFixture {
	public static final String NAMESPACE = "cakeworld_climate_fixture";
	public static final List<RangeSpec> RANGES = List.of(
			new RangeSpec(-2.0F, 2.0F, 0.0F, 1.0F),
			new RangeSpec(0.25F, 2.0F, 0.0F, 0.65F),
			new RangeSpec(0.4F, 1.2F, 0.25F, 0.8F),
			new RangeSpec(0.15F, 1.5F, 0.55F, 1.0F),
			new RangeSpec(-2.0F, 0.3F, 0.35F, 1.0F),
			new RangeSpec(0.7F, 2.0F, 0.7F, 1.0F),
			new RangeSpec(0.5F, 1.2F, 0.75F, 1.0F),
			new RangeSpec(1.5F, 2.0F, 0.0F, 0.2F),
			new RangeSpec(-2.0F, 0.35F, 0.0F, 1.0F),
			new RangeSpec(-2.0F, 0.2F, 0.0F, 0.8F),
			new RangeSpec(0.5F, 2.0F, 0.0F, 0.35F),
			new RangeSpec(0.3F, 1.2F, 0.35F, 1.0F),
			new RangeSpec(0.4F, 1.0F, 0.6F, 1.0F),
			new RangeSpec(0.3F, 1.0F, 0.4F, 0.9F),
			new RangeSpec(0.5F, 1.2F, 0.2F, 0.6F),
			new RangeSpec(-1.0F, 2.0F, 0.0F, 1.0F),
			new RangeSpec(0.5F, 1.2F, 0.8F, 1.0F));
	private static final DeferredRegister<Biome> BIOMES =
			DeferredRegister.create(ForgeRegistries.BIOMES, NAMESPACE);
	private static final List<FixtureCase> CASES = registerCases();
	public static final ResourceLocation FIRST_BIOME_ID = CASES.get(0).id();
	private static final Logger LOGGER = LogUtils.getLogger();
	private static boolean registered;
	private static volatile boolean installed;

	private ClimateBoundaryFixture() {
	}

	public static synchronized void register(IEventBus modBus) {
		if (registered) return;
		registered = true;
		BIOMES.register(modBus);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST,
				ClimateBoundaryFixture::installBeforeOreSpawn);
	}

	public static List<FixtureCase> cases() {
		return CASES;
	}

	public static boolean wasInstalled() {
		return installed;
	}

	private static List<FixtureCase> registerCases() {
		List<FixtureCase> result = new ArrayList<>();
		for (int rangeIndex = 0; rangeIndex < RANGES.size(); rangeIndex++) {
			RangeSpec range = RANGES.get(rangeIndex);
			float middleTemperature = midpoint(range.minTemperature(),
					range.maxTemperature());
			float middleDownfall = midpoint(range.minDownfall(),
					range.maxDownfall());
			registerCase(result, rangeIndex, "minimum",
					range.minTemperature(), range.minDownfall(), true);
			registerCase(result, rangeIndex, "maximum",
					range.maxTemperature(), range.maxDownfall(), true);
			registerCase(result, rangeIndex, "temperature_below",
					Math.nextDown(range.minTemperature()), middleDownfall, false);
			registerCase(result, rangeIndex, "temperature_above",
					Math.nextUp(range.maxTemperature()), middleDownfall, false);
			registerCase(result, rangeIndex, "downfall_below",
					middleTemperature, Math.nextDown(range.minDownfall()), false);
			registerCase(result, rangeIndex, "downfall_above",
					middleTemperature, Math.nextUp(range.maxDownfall()), false);
		}
		return List.copyOf(result);
	}

	private static void registerCase(List<FixtureCase> result, int rangeIndex,
			String suffix, float temperature, float downfall,
			boolean shouldSelect) {
		String path = String.format("range_%02d_%s", rangeIndex, suffix);
		ResourceLocation id = new ResourceLocation(NAMESPACE, path);
		OreSpawnBiomes.blankAndRegister(BIOMES, path, builder -> builder
				.precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.NONE)
				.temperature(temperature)
				.temperatureAdjustment(Biome.TemperatureModifier.NONE)
				.downfall(downfall)
				.specialEffects(new BiomeSpecialEffects.Builder()
						.fogColor(0xD7C5FF)
						.waterColor(0xFFD166)
						.waterFogColor(0x9C5CFF)
						.skyColor(0xFFB6D9)
						.build())
				.mobSpawnSettings(new MobSpawnSettings.Builder()
						.creatureGenerationProbability(0.0F)
						.build())
				.generationSettings(
						new BiomeGenerationSettings.Builder().build()));
		result.add(new FixtureCase(rangeIndex, id, shouldSelect));
	}

	private static float midpoint(float minimum, float maximum) {
		return minimum + (maximum - minimum) / 2.0F;
	}

	private static void installBeforeOreSpawn(WorldEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel)) return;
		ServerLevel overworld = (ServerLevel) event.getWorld();
		if (!Level.OVERWORLD.equals(overworld.dimension())) return;
		Registry<Biome> registry = overworld.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		List<Holder<Biome>> holders = new ArrayList<>();
		for (FixtureCase fixtureCase : CASES) {
			ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY,
					fixtureCase.id());
			holders.add(registry.getHolder(key).orElseThrow(() ->
					new IllegalStateException("Climate fixture biome was not registered: "
							+ fixtureCase.id())));
		}
		ChunkGenerator generator = overworld.getChunkSource().getGenerator();
		CheckerboardColumnBiomeSource source =
				new CheckerboardColumnBiomeSource(HolderSet.direct(holders), 0);
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class,
				generator, source, "f_62137_");
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class,
				generator, source, "f_62138_");
		installed = true;
		LOGGER.info("Installed GameTest-only {}-biome climate boundary source before OreSpawn wrapping",
				CASES.size());
	}

	public record RangeSpec(float minTemperature, float maxTemperature,
			float minDownfall, float maxDownfall) {
	}

	public record FixtureCase(int rangeIndex, ResourceLocation id,
			boolean shouldSelect) {
	}
}
