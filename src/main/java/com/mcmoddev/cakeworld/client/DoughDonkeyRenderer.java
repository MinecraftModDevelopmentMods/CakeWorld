package com.mcmoddev.cakeworld.client;

import com.mcmoddev.cakeworld.entity.DoughDonkey;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ChestedHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Reuses the temporary vanilla Donkey art without relying on the vanilla
 * renderer's entity-type-only texture map.
 */
public final class DoughDonkeyRenderer
		extends ChestedHorseRenderer<DoughDonkey> {
	private static final ResourceLocation DONKEY_TEXTURE =
			new ResourceLocation("textures/entity/horse/donkey.png");

	public DoughDonkeyRenderer(EntityRendererProvider.Context context) {
		super(context, 0.87F, ModelLayers.DONKEY);
	}

	@Override
	public ResourceLocation getTextureLocation(DoughDonkey donkey) {
		return DONKEY_TEXTURE;
	}
}
