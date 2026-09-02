package zone.moddev.mc.cakeworld.client;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

/**
 * Reuses the temporary vanilla Skeleton Horse art without relying on the
 * vanilla renderer's entity-type-only texture map.
 */
public final class BrittleBiscuitSteedRenderer
		extends UndeadHorseRenderer {
	private static final ResourceLocation SKELETON_TEXTURE =
			new ResourceLocation(
					"textures/entity/horse/horse_skeleton.png");

	public BrittleBiscuitSteedRenderer(
			EntityRendererProvider.Context context) {
		super(context, ModelLayers.SKELETON_HORSE);
	}

	@Override
	public ResourceLocation getTextureLocation(
			AbstractHorse horse) {
		return SKELETON_TEXTURE;
	}
}
