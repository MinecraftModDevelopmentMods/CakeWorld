package com.mcmoddev.cakeworld.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * A short, wholly beneficial movement boost. It has no delayed crash or
 * negative follow-up state.
 */
public final class SugarRushEffect extends MobEffect {
	private static final String SPEED_MODIFIER =
			"0fd438f6-36f9-4e76-aa04-cf5e12b11e38";

	public SugarRushEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xF7A8D8);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER,
				0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
