package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;

/**
 * A spice-bright Nether guardian with difficulty-aware projectiles.
 */
public final class CinnamonSpark extends Blaze implements RangedAttackMob {
	public CinnamonSpark(EntityType<? extends Blaze> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(4,
				new RangedAttackGoal(this, 1.0D, 40, 48.0F));
		goalSelector.addGoal(5,
				new MoveTowardsRestrictionGoal(this, 1.0D));
		goalSelector.addGoal(7,
				new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
		goalSelector.addGoal(8,
				new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1,
				new HurtByTargetGoal(this).setAlertOthers());
		targetSelector.addGoal(2,
				new NearestAttackableTargetGoal<>(this,
						Player.class, true));
	}

	@Override
	public void performRangedAttack(LivingEntity target,
			float distanceFactor) {
		double x = target.getX() - getX();
		double y = target.getY(0.5D) - getY(0.5D);
		double z = target.getZ() - getZ();
		if (!isSilent()) {
			level.levelEvent((Player) null, 1018, blockPosition(), 0);
		}
		if (level.getDifficulty() == Difficulty.HARD) {
			SmallFireball fireball =
					new SmallFireball(level, this, x, y, z);
			fireball.setPos(fireball.getX(), getY(0.5D) + 0.5D,
					fireball.getZ());
			level.addFreshEntity(fireball);
		} else {
			CinnamonPuffProjectile puff =
					new CinnamonPuffProjectile(level, this);
			puff.shoot(x, y, z, 1.0F, 4.0F);
			level.addFreshEntity(puff);
		}
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (target instanceof LivingEntity livingTarget) {
			CinnamonPuffProjectile puff =
					new CinnamonPuffProjectile(level, this);
			puff.setPos(position());
			puff.applyHarmlessPuff(livingTarget);
			puff.discard();
			playSound(SoundEvents.BLAZE_SHOOT, 0.8F, 1.2F);
			return true;
		}
		return false;
	}
}
