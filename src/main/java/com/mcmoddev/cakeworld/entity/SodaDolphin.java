package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * A Lemonade-compatible Dolphin guide with family-safe retaliation.
 */
public final class SodaDolphin extends Dolphin {
	private static final int BUBBLE_PROTECTION_TICKS = 100;

	public SodaDolphin(EntityType<? extends Dolphin> type, Level level) {
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
			horizontal = horizontal.normalize().scale(0.25D);
		}
		living.push(horizontal.x, 0.15D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE,
				BUBBLE_PROTECTION_TICKS, 0, false, true));
		living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
				BUBBLE_PROTECTION_TICKS, 0, false, false));
		living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
				BUBBLE_PROTECTION_TICKS, 0, false, false));
		living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,
				BUBBLE_PROTECTION_TICKS, 4, false, false));
		playSound(SoundEvents.DOLPHIN_PLAY, 1.0F,
				1.1F + random.nextFloat() * 0.2F);
		return true;
	}

	/**
	 * Vanilla requires a literal water block above a surface-water spawn.
	 * CakeWorld uses the standard water fluid tag so Lemonade is valid.
	 */
	public static boolean checkSodaDolphinSpawnRules(
			EntityType<? extends WaterAnimal> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		int seaLevel = level.getSeaLevel();
		return pos.getY() >= seaLevel - 13
				&& pos.getY() <= seaLevel
				&& level.getFluidState(pos.below()).is(FluidTags.WATER)
				&& level.getFluidState(pos.above()).is(FluidTags.WATER);
	}
}
