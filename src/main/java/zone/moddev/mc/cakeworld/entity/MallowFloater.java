package zone.moddev.mc.cakeworld.entity;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * A genuine Ghast-derived Nether floater with a difficulty-aware projectile
 * seam: cushioning puffs below Hard and vanilla fireballs on Hard.
 */
public final class MallowFloater extends Ghast {
	public MallowFloater(
			EntityType<? extends Ghast> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(5, new RandomFloatGoal(this));
		goalSelector.addGoal(7, new LookGoal(this));
		goalSelector.addGoal(7, new ShootGoal(this));
		targetSelector.addGoal(1,
				new NearestAttackableTargetGoal<>(
						this, Player.class, 10, true, false,
						player -> Math.abs(player.getY() - getY())
								<= 4.0D));
	}

	public AbstractHurtingProjectile createShot(
			LivingEntity target) {
		Vec3 view = getViewVector(1.0F);
		double xPower = target.getX()
				- (getX() + view.x * 4.0D);
		double yPower = target.getY(0.5D)
				- (0.5D + getY(0.5D));
		double zPower = target.getZ()
				- (getZ() + view.z * 4.0D);
		AbstractHurtingProjectile projectile;
		if (level.getDifficulty() == Difficulty.HARD) {
			projectile = new LargeFireball(level, this,
					xPower, yPower, zPower,
					getExplosionPower());
		} else {
			projectile = new MallowPuffProjectile(
					level, this, xPower, yPower, zPower);
		}
		projectile.setPos(getX() + view.x * 4.0D,
				getY(0.5D) + 0.5D,
				getZ() + view.z * 4.0D);
		return projectile;
	}

	public static boolean checkMallowFloaterSpawnRules(
			EntityType<MallowFloater> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return level.getDifficulty() != Difficulty.PEACEFUL
				&& random.nextInt(20) == 0
				&& checkMobSpawnRules(
						type, level, reason, pos, random);
	}

	private static final class ShootGoal extends Goal {
		private final MallowFloater floater;
		private int chargeTime;

		private ShootGoal(MallowFloater floater) {
			this.floater = floater;
		}

		@Override
		public boolean canUse() {
			return floater.getTarget() != null;
		}

		@Override
		public void start() {
			chargeTime = 0;
		}

		@Override
		public void stop() {
			floater.setCharging(false);
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity target = floater.getTarget();
			if (target == null) {
				return;
			}
			if (target.distanceToSqr(floater) < 4096.0D
					&& floater.hasLineOfSight(target)) {
				++chargeTime;
				if (chargeTime == 10 && !floater.isSilent()) {
					floater.level.levelEvent(null, 1015,
							floater.blockPosition(), 0);
				}
				if (chargeTime == 20) {
					if (!floater.isSilent()) {
						floater.level.levelEvent(null, 1016,
								floater.blockPosition(), 0);
					}
					floater.level.addFreshEntity(
							floater.createShot(target));
					chargeTime = -40;
				}
			} else if (chargeTime > 0) {
				--chargeTime;
			}
			floater.setCharging(chargeTime > 10);
		}
	}

	private static final class LookGoal extends Goal {
		private final MallowFloater floater;

		private LookGoal(MallowFloater floater) {
			this.floater = floater;
			setFlags(EnumSet.of(Goal.Flag.LOOK));
		}

		@Override
		public boolean canUse() {
			return true;
		}

		@Override
		public boolean requiresUpdateEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			LivingEntity target = floater.getTarget();
			if (target == null) {
				Vec3 movement = floater.getDeltaMovement();
				floater.setYRot(-((float)Math.atan2(
						movement.x, movement.z))
						* (180.0F / (float)Math.PI));
			} else if (target.distanceToSqr(floater) < 4096.0D) {
				double x = target.getX() - floater.getX();
				double z = target.getZ() - floater.getZ();
				floater.setYRot(-((float)Math.atan2(x, z))
						* (180.0F / (float)Math.PI));
			}
			floater.yBodyRot = floater.getYRot();
		}
	}

	private static final class RandomFloatGoal extends Goal {
		private final MallowFloater floater;

		private RandomFloatGoal(MallowFloater floater) {
			this.floater = floater;
			setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			if (!floater.getMoveControl().hasWanted()) {
				return true;
			}
			double x = floater.getMoveControl().getWantedX()
					- floater.getX();
			double y = floater.getMoveControl().getWantedY()
					- floater.getY();
			double z = floater.getMoveControl().getWantedZ()
					- floater.getZ();
			double distance = x * x + y * y + z * z;
			return distance < 1.0D || distance > 3600.0D;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void start() {
			Random random = floater.getRandom();
			double x = floater.getX()
					+ (random.nextFloat() * 2.0F - 1.0F)
							* 16.0F;
			double y = floater.getY()
					+ (random.nextFloat() * 2.0F - 1.0F)
							* 16.0F;
			double z = floater.getZ()
					+ (random.nextFloat() * 2.0F - 1.0F)
							* 16.0F;
			floater.getMoveControl().setWantedPosition(
					x, y, z, 1.0D);
		}
	}
}
