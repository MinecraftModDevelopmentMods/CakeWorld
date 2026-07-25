package com.mcmoddev.cakeworld.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mcmoddev.cakeworld.entity.LollipopLorikeet;

import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

/**
 * Vanilla's shoulder layer only renders the literal minecraft:parrot type.
 * This companion layer renders CakeWorld's preserved custom shoulder NBT.
 */
public final class LollipopLorikeetOnShoulderLayer<T extends Player>
		extends RenderLayer<T, PlayerModel<T>> {
	private final ParrotModel model;

	public LollipopLorikeetOnShoulderLayer(
			RenderLayerParent<T, PlayerModel<T>> parent,
			EntityModelSet models) {
		super(parent);
		model = new ParrotModel(
				models.bakeLayer(ModelLayers.PARROT));
	}

	@Override
	public void render(PoseStack poses,
			MultiBufferSource buffers, int light,
			T player, float limbSwing,
			float limbSwingAmount, float partialTick,
			float ageInTicks, float netHeadYaw,
			float headPitch) {
		renderShoulder(poses, buffers, light, player,
				limbSwing, limbSwingAmount,
				netHeadYaw, headPitch, true);
		renderShoulder(poses, buffers, light, player,
				limbSwing, limbSwingAmount,
				netHeadYaw, headPitch, false);
	}

	private void renderShoulder(PoseStack poses,
			MultiBufferSource buffers, int light,
			T player, float limbSwing,
			float limbSwingAmount, float netHeadYaw,
			float headPitch, boolean left) {
		CompoundTag tag = left
				? player.getShoulderEntityLeft()
				: player.getShoulderEntityRight();
		if (!LollipopLorikeet.isShoulderTag(tag)) {
			return;
		}
		poses.pushPose();
		poses.translate(left ? (double)0.4F
				: (double)-0.4F,
				player.isCrouching()
						? (double)-1.3F : -1.5D,
				0.0D);
		int variant = Mth.clamp(
				tag.getInt("Variant"), 0, 4);
		VertexConsumer vertices = buffers.getBuffer(
				model.renderType(
						ParrotRenderer.PARROT_LOCATIONS[
								variant]));
		model.renderOnShoulder(poses, vertices, light,
				OverlayTexture.NO_OVERLAY, limbSwing,
				limbSwingAmount, netHeadYaw, headPitch,
				player.tickCount);
		poses.popPose();
	}
}
