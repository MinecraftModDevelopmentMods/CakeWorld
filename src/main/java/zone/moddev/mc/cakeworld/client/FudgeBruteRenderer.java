package zone.moddev.mc.cakeworld.client;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

/**
 * Temporary vanilla-art renderer which bypasses PiglinRenderer's exact
 * entity-type texture map for the genuine custom Piglin Brute subclass.
 */
public final class FudgeBruteRenderer extends PiglinRenderer {
	private static final ResourceLocation PLACEHOLDER_TEXTURE =
			new ResourceLocation(
					"textures/entity/piglin/piglin_brute.png");

	public FudgeBruteRenderer(
			EntityRendererProvider.Context context) {
		super(context, ModelLayers.PIGLIN_BRUTE,
				ModelLayers.PIGLIN_BRUTE_INNER_ARMOR,
				ModelLayers.PIGLIN_BRUTE_OUTER_ARMOR,
				false);
	}

	@Override
	public ResourceLocation getTextureLocation(Mob entity) {
		return PLACEHOLDER_TEXTURE;
	}
}
