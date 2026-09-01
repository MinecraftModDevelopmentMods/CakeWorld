package com.mcmoddev.cakeworld.client;

import com.mcmoddev.cakeworld.entity.MarzipanMule;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ChestedHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Reuses the temporary vanilla Mule art without relying on the vanilla
 * renderer's entity-type-only texture map.
 */
public final class MarzipanMuleRenderer
		extends ChestedHorseRenderer<MarzipanMule> {
	private static final ResourceLocation MULE_TEXTURE =
			new ResourceLocation("textures/entity/horse/mule.png");

	public MarzipanMuleRenderer(
			EntityRendererProvider.Context context) {
		super(context, 0.92F, ModelLayers.MULE);
	}

	@Override
	public ResourceLocation getTextureLocation(
			MarzipanMule mule) {
		return MULE_TEXTURE;
	}
}
