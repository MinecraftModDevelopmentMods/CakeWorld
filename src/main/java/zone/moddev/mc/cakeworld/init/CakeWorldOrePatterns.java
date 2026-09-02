package zone.moddev.mc.cakeworld.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zone.moddev.mc.orespawn.api.CompiledOrePattern;
import zone.moddev.mc.orespawn.api.OrePatternType;
import zone.moddev.mc.orespawn.api.OrePlacementContext;
import zone.moddev.mc.orespawn.api.OreSpawnPatternRegistry;

/** CakeWorld patterns registered through OreSpawn's public codec API. */
public final class CakeWorldOrePatterns {
	public static final ResourceLocation LAYER_CAKE_ID =
			new ResourceLocation("cakeworld", "layer_cake");

	private static final DeferredRegister<OrePatternType> PATTERNS =
			DeferredRegister.create(OreSpawnPatternRegistry.REGISTRY_NAME,
					"cakeworld");

	public static final RegistryObject<OrePatternType> LAYER_CAKE =
			PATTERNS.register(LAYER_CAKE_ID.getPath(), () ->
					OrePatternType.create(LayerCakeSettings.CODEC,
							CakeWorldOrePatterns::compileLayerCake));

	private CakeWorldOrePatterns() {
	}

	public static void register(IEventBus modBus) {
		PATTERNS.register(modBus);
	}

	private static CompiledOrePattern compileLayerCake(
			LayerCakeSettings settings) {
		final int layers = settings.layers;
		final int radius = settings.radius;
		final int thickness = settings.thickness;
		final int layerGap = settings.layerGap;
		final int radiusSquared = radius * radius;
		final int totalHeight = layers * thickness
				+ (layers - 1) * layerGap;
		final int firstYOffset = -((totalHeight - 1) / 2);

		return context -> placeLayerCake(context, layers, radius, thickness,
				layerGap, radiusSquared, firstYOffset);
	}

	private static boolean placeLayerCake(OrePlacementContext context,
			int layers, int radius, int thickness, int layerGap,
			int radiusSquared, int firstYOffset) {
		int placed = 0;
		int target = context.quantity();
		if (target <= 0) return false;
		for (int layer = 0; layer < layers && placed < target; layer++) {
			int layerY = context.originY() + firstYOffset
					+ layer * (thickness + layerGap);
			for (int depth = 0;
					depth < thickness && placed < target; depth++) {
				int y = layerY + depth;
				for (int x = -radius; x <= radius && placed < target; x++) {
					for (int z = -radius; z <= radius && placed < target;
							z++) {
						if (x * x + z * z <= radiusSquared
								&& context.tryPlace(context.originX() + x, y,
										context.originZ() + z)) {
							placed++;
						}
					}
				}
			}
		}
		return placed > 0;
	}

	private static final class LayerCakeSettings {
		private static final Codec<LayerCakeSettings> CODEC =
				RecordCodecBuilder.create(instance -> instance.group(
						Codec.intRange(1, 8).fieldOf("layers")
								.forGetter(settings -> settings.layers),
						Codec.intRange(1, 8).fieldOf("radius")
								.forGetter(settings -> settings.radius),
						Codec.intRange(1, 3).fieldOf("thickness")
								.forGetter(settings -> settings.thickness),
						Codec.intRange(0, 4).fieldOf("layer_gap")
								.forGetter(settings -> settings.layerGap))
						.apply(instance, LayerCakeSettings::new));

		private final int layers;
		private final int radius;
		private final int thickness;
		private final int layerGap;

		private LayerCakeSettings(int layers, int radius, int thickness,
				int layerGap) {
			this.layers = layers;
			this.radius = radius;
			this.thickness = thickness;
			this.layerGap = layerGap;
		}
	}
}
