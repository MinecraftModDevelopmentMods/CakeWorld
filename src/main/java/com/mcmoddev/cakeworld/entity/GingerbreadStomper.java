package com.mcmoddev.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Ravager raid mount and obstacle spectacle.
 *
 * <p>The superclass retains raid membership, Illager riders, shield stun,
 * delayed roar, navigation, targets, alliances, sounds and Hard destruction.
 * CakeWorld replaces direct lower-difficulty damage and supplies a safe hop
 * when its mob-griefing guard deliberately leaves an obstacle intact.</p>
 */
public class GingerbreadStomper extends Ravager {
	public GingerbreadStomper(
			EntityType<? extends Ravager> type, Level level) {
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

		handleEntityEvent((byte)4);
		level.broadcastEntityEvent(this, (byte)4);
		GingerbreadStomperDamageSafety.applySafeImpact(
				this, living, true);
		return true;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (isAlive()
				&& level.getDifficulty() != Difficulty.HARD
				&& horizontalCollision && onGround) {
			jumpFromGround();
			if (tickCount % 10 == 0) {
				playSound(SoundEvents.WOOD_HIT,
						0.65F, 0.8F);
			}
		}
	}
}
