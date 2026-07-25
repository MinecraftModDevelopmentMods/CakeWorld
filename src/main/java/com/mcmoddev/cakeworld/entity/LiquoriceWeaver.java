package com.mcmoddev.cakeworld.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine surface Spider role.
 *
 * <p>Wall navigation, climbing, daylight neutrality, cobweb and poison
 * immunity, spawn effects and jockey construction remain inherited. Below
 * Hard, the bite becomes a visible but harmless string splat.</p>
 */
public class LiquoriceWeaver extends Spider {
	private static final int WEB_TICKS = 100;
	private static final int RESCUE_TICKS = 120;

	public LiquoriceWeaver(
			EntityType<? extends Spider> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity livingTarget)) {
			return false;
		}
		applyHarmlessWebSplat(livingTarget);
		return true;
	}

	public void applyHarmlessWebSplat(
			LivingEntity target) {
		target.clearFire();
		target.fallDistance = 0.0F;
		target.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				WEB_TICKS, 1, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.DIG_SLOWDOWN,
				WEB_TICKS, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				60, 0, false, true));
		target.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0, false, false));
		target.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				60, 4, false, false));
		if (level instanceof ServerLevel server) {
			server.sendParticles(
					new ItemParticleOption(
							ParticleTypes.ITEM,
							new ItemStack(
									Items.STRING)),
					target.getX(),
					target.getY()
							+ target.getBbHeight()
									* 0.5D,
					target.getZ(),
					12, 0.25D, 0.35D,
					0.25D, 0.02D);
		}
		playSound(SoundEvents.SLIME_SQUISH,
				0.8F, 0.7F);
	}
}
