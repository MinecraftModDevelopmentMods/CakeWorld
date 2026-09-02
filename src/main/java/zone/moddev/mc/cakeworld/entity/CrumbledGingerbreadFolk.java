package zone.moddev.mc.cakeworld.entity;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine curable Zombie Villager role.
 *
 * <p>The inherited entity keeps Zombie body, AI, equipment and sunlight
 * behavior together with Villager data, offers, gossip, XP, the private cure
 * timer, bed/iron-bar acceleration and Forge conversion events. CakeWorld
 * changes only the registered family output and lower-difficulty contact
 * policy.</p>
 */
public class CrumbledGingerbreadFolk
		extends ZombieVillager {
	public CrumbledGingerbreadFolk(
			EntityType<? extends ZombieVillager> type,
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

		StaleCrumblerSafety.applyProtectedContact(
				this, living);
		return true;
	}
}
