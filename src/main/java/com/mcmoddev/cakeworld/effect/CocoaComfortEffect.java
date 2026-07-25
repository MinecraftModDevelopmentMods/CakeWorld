package com.mcmoddev.cakeworld.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * A modest defensive effect: less knockback and a little armour, with no
 * hidden healing, hunger, or penalty cycle.
 */
public final class CocoaComfortEffect extends MobEffect {
	private static final String KNOCKBACK_MODIFIER =
			"848b4503-7013-46a8-a180-84493ccaf00e";
	private static final String ARMOUR_MODIFIER =
			"ad2ca515-b10d-4486-adc6-f9ca75e0fdcc";

	public CocoaComfortEffect() {
		super(MobEffectCategory.BENEFICIAL, 0x7A4935);
		addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE,
				KNOCKBACK_MODIFIER, 0.25D,
				AttributeModifier.Operation.ADDITION);
		addAttributeModifier(Attributes.ARMOR, ARMOUR_MODIFIER, 2.0D,
				AttributeModifier.Operation.ADDITION);
	}
}
