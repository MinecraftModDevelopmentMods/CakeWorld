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
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * A tiny teleport-associated nuisance retaining the Endermite lifetime and
 * arthropod roles.
 */
public final class SugarMite extends Endermite {
	private static final int FIZZY_TICKS = 100;

	public SugarMite(
			EntityType<? extends Endermite> type, Level level) {
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

		Vec3 offset = living.position().subtract(position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.1D);
		}
		living.push(horizontal.x, 0.08D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, FIZZY_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, FIZZY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, FIZZY_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, FIZZY_TICKS,
				4, false, false));
		playSound(SoundEvents.ENDERMITE_STEP, 0.7F, 1.4F);
		return true;
	}

	public static boolean checkSugarMiteSpawnRules(
			EntityType<SugarMite> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return checkAnyLightMonsterSpawnRules(
				type, level, reason, pos, random)
				&& level.getNearestPlayer(
						pos.getX() + 0.5D,
						pos.getY() + 0.5D,
						pos.getZ() + 0.5D,
						5.0D, true) == null;
	}
}
