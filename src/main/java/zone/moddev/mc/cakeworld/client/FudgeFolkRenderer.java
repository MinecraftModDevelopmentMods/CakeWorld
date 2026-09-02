package zone.moddev.mc.cakeworld.client;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

/**
 * Temporary vanilla-art renderer which bypasses PiglinRenderer's exact
 * entity-type texture map for the genuine custom Piglin subclass.
 */
public final class FudgeFolkRenderer extends PiglinRenderer {
	private static final ResourceLocation PLACEHOLDER_TEXTURE =
			new ResourceLocation(
					"textures/entity/piglin/piglin.png");

	public FudgeFolkRenderer(
			EntityRendererProvider.Context context) {
		super(context, ModelLayers.PIGLIN,
				ModelLayers.PIGLIN_INNER_ARMOR,
				ModelLayers.PIGLIN_OUTER_ARMOR, false);
	}

	@Override
	public ResourceLocation getTextureLocation(Mob entity) {
		return PLACEHOLDER_TEXTURE;
	}
}
