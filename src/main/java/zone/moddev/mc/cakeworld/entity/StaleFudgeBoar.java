package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Zoglin-derived stale Fudge Boar role.
 *
 * <p>The superclass remains authoritative for the complete adult and baby
 * brain, broad hostility, undead behavior, attack animation, sounds, NBT and
 * leashing. Vanilla excludes only its literal Zoglin type from target
 * selection, so the custom family is added to that same boundary. Below Hard,
 * the inherited attack still drives animation and AI cooldowns while the Forge
 * safety boundary turns the charge into a protected shove.</p>
 */
public class StaleFudgeBoar extends Zoglin {
	public StaleFudgeBoar(
			EntityType<? extends Zoglin> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean canAttack(LivingEntity target) {
		return target.getType()
						!= CakeWorldEntities
								.STALE_FUDGE_BOAR.get()
				&& super.canAttack(target);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity)) {
			return false;
		}

		// Retain Zoglin animation, sound and brain cooldown. The early Forge
		// boundary cancels health damage and supplies the safe physical cue.
		super.doHurtTarget(target);
		return true;
	}

	@Override
	protected void blockedByShield(LivingEntity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			super.blockedByShield(target);
		} else {
			StaleFudgeBoarDamageSafety
					.protectAndThrow(this, target);
		}
	}
}
