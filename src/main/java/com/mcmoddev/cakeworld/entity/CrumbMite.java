package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * CakeWorld's Silverfish-role nuisance and edible-stone nest dweller.
 *
 * <p>All movement, targeting, powder-snow climbing, host merging, friend
 * waking, sounds, arthropod identity and spawn rules remain inherited. Only
 * the lower-difficulty bite is replaced with a harmless crumb-scatter cue.</p>
 */
public class CrumbMite extends Silverfish {
	private static final int CRUMB_TICKS = 60;
	private static final int RESCUE_TICKS = 100;

	public CrumbMite(
			EntityType<? extends Silverfish> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		living.clearFire();
		living.fallDistance = 0.0F;
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				CRUMB_TICKS, 0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				CRUMB_TICKS, 0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4, false, false));
		living.push(0.0D, 0.08D, 0.0D);
		playSound(SoundEvents.SILVERFISH_STEP,
				0.3F, 1.25F);
		return true;
	}

	public static boolean checkCrumbMiteSpawnRules(
			EntityType<CrumbMite> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		if (!checkAnyLightMonsterSpawnRules(
				type, level, reason, pos, random)) {
			return false;
		}
		Player nearest = level.getNearestPlayer(
				pos.getX() + 0.5D,
				pos.getY() + 0.5D,
				pos.getZ() + 0.5D,
				5.0D, true);
		return nearest == null;
	}
}
