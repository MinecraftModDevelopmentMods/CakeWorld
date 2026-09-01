package com.mcmoddev.cakeworld.client;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.UndeadHorseRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

/**
 * Reuses the temporary vanilla Zombie Horse presentation while bespoke
 * gingerbread art remains future work.
 */
public final class StaleGingerbreadSteedRenderer
		extends UndeadHorseRenderer {
	private static final ResourceLocation ZOMBIE_TEXTURE =
			new ResourceLocation(
					"textures/entity/horse/horse_zombie.png");

	public StaleGingerbreadSteedRenderer(
			EntityRendererProvider.Context context) {
		super(context, ModelLayers.ZOMBIE_HORSE);
	}

	@Override
	public ResourceLocation getTextureLocation(
			AbstractHorse horse) {
		return ZOMBIE_TEXTURE;
	}
}
