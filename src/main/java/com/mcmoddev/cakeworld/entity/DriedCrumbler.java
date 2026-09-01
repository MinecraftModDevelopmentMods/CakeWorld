package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * Sherbet Dunes' daylight-safe Husk role.
 */
public final class DriedCrumbler extends Husk {
	private static final int DUST_TICKS = 80;
	private static final int RESCUE_TICKS = 120;

	public DriedCrumbler(
			EntityType<? extends Husk> type, Level level) {
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
				DUST_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.CONFUSION, DUST_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS, 0));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS, 4));
		double x = living.getX() - getX();
		double z = living.getZ() - getZ();
		living.push(x * 0.08D, 0.10D, z * 0.08D);
		return true;
	}

	@Override
	public boolean isSunSensitive() {
		return false;
	}

	@Override
	protected void doUnderWaterConversion() {
		convertToZombieType(
				CakeWorldEntities.STALE_CRUMBLER.get());
		if (!isSilent()) {
			level.levelEvent((Player) null, 1041,
					blockPosition(), 0);
		}
	}

	public static boolean checkDriedCrumblerSpawnRules(
			EntityType<? extends DriedCrumbler> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return Monster.checkMonsterSpawnRules(
				type, level, reason, pos, random)
				&& (reason == MobSpawnType.SPAWNER
						|| level.canSeeSky(pos));
	}
}
