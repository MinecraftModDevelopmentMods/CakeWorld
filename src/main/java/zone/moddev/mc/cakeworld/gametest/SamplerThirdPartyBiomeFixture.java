package zone.moddev.mc.cakeworld.gametest;

import com.mojang.logging.LogUtils;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.OreSpawnBiomes;

/**
 * Opt-in GameTest-only owner for an unrelated biome namespace. Sampler
 * pass-through and main-adventure replace-mode proofs share this fixture;
 * production CakeWorld never registers or installs it.
 */
public final class SamplerThirdPartyBiomeFixture {
	public static final String NAMESPACE = "cakeworld_fixture";
	public static final ResourceLocation BIOME_ID =
			new ResourceLocation(NAMESPACE, "delegated_meadow");
	private static final ResourceKey<Biome> BIOME_KEY = ResourceKey.create(
			Registry.BIOME_REGISTRY, BIOME_ID);
	private static final DeferredRegister<Biome> BIOMES =
			DeferredRegister.create(ForgeRegistries.BIOMES, NAMESPACE);
	private static final RegistryObject<Biome> DELEGATED_MEADOW =
			OreSpawnBiomes.copyAndRegister(BIOMES, "delegated_meadow",
					() -> ForgeRegistries.BIOMES.getValue(
							new ResourceLocation("minecraft", "plains")),
					builder -> builder.temperature(0.8F).downfall(0.4F));
	private static final Logger LOGGER = LogUtils.getLogger();
	private static boolean registered;
	private static volatile boolean installed;

	private SamplerThirdPartyBiomeFixture() {
	}

	public static synchronized void register(IEventBus modBus) {
		if (registered) return;
		registered = true;
		BIOMES.register(modBus);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST,
				SamplerThirdPartyBiomeFixture::installBeforeOreSpawn);
	}

	private static void installBeforeOreSpawn(WorldEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel)) return;
		ServerLevel overworld = (ServerLevel) event.getWorld();
		if (!Level.OVERWORLD.equals(overworld.dimension())) return;
		Registry<Biome> registry = overworld.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Holder<Biome> fixture = registry.getHolder(BIOME_KEY).orElseThrow(
				() -> new IllegalStateException(
						"Sampler third-party biome was not registered"));
		ChunkGenerator generator = overworld.getChunkSource().getGenerator();
		FixedBiomeSource source = new FixedBiomeSource(fixture);
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class,
				generator, source, "f_62137_");
		ObfuscationReflectionHelper.setPrivateValue(ChunkGenerator.class,
				generator, source, "f_62138_");
		installed = true;
		LOGGER.info("Installed GameTest-only biome source owner for '{}' before OreSpawn wrapping",
				BIOME_ID);
	}

	public static RegistryObject<Biome> delegatedMeadow() {
		return DELEGATED_MEADOW;
	}

	public static boolean wasInstalled() {
		return installed;
	}
}
