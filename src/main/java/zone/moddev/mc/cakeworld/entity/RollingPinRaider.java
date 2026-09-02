package zone.moddev.mc.cakeworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's genuine Vindicator raid, mansion and Ravager-rider role.
 *
 * <p>The superclass retains patrol and captain state, raid membership,
 * village pathing, Johnny targeting, Iron Axe equipment, raid enchantments,
 * alliances, celebration and sounds. CakeWorld only supplies the fixed
 * lower-difficulty safety boundary and repairs Villager awareness of this
 * registered entity type.</p>
 */
public class RollingPinRaider extends Vindicator {
	private static final int INCONVENIENCE_TICKS = 60;
	private static final int RESCUE_TICKS = 120;
	private static final double VILLAGER_ALERT_RANGE = 15.0D;
	private static final double VILLAGER_ALERT_RANGE_SQUARED =
			VILLAGER_ALERT_RANGE * VILLAGER_ALERT_RANGE;

	public RollingPinRaider(
			EntityType<? extends Vindicator> type,
			Level level) {
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
		Vec3 horizontal = new Vec3(
				offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() <= 1.0E-4D) {
			horizontal = getLookAngle()
					.multiply(1.0D, 0.0D, 1.0D);
		}
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize()
					.scale(0.45D);
		}
		living.push(horizontal.x, 0.18D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN,
				INCONVENIENCE_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.GLOWING,
				INCONVENIENCE_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING,
				RESCUE_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE,
				RESCUE_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE,
				RESCUE_TICKS,
				4, false, false));
		playSound(SoundEvents.WOOD_HIT,
				0.8F, 0.75F);
		return true;
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		repairVillagerHostileAwareness();
	}

	protected void repairVillagerHostileAwareness() {
		if (tickCount % 20 != 0) {
			return;
		}
		level.getEntitiesOfClass(
				Villager.class,
				getBoundingBox().inflate(
						VILLAGER_ALERT_RANGE),
				villager -> villager.isAlive()
						&& isWithinVillagerAlertRange(
								villager))
				.forEach(this::alertVisibleVillager);
	}

	protected boolean isWithinVillagerAlertRange(
			LivingEntity entity) {
		return entity.distanceToSqr(this)
				<= VILLAGER_ALERT_RANGE_SQUARED;
	}

	private void alertVisibleVillager(
			Villager villager) {
		Brain<?> brain = villager.getBrain();
		boolean visible = brain.getMemory(
				MemoryModuleType
						.NEAREST_VISIBLE_LIVING_ENTITIES)
				.filter(nearby ->
						nearby.contains(this))
				.isPresent();
		if (!visible) {
			return;
		}

		LivingEntity current = brain.getMemory(
				MemoryModuleType.NEAREST_HOSTILE)
				.orElse(null);
		if (current == null
				|| !current.isAlive()
				|| villager.distanceToSqr(this)
						< villager.distanceToSqr(
								current)) {
			brain.setMemory(
					MemoryModuleType.NEAREST_HOSTILE,
					this);
		}
	}
}
