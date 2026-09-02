package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Donkey-role pack animal.
 *
 * <p>Two Dough Donkeys produce their own type. Crossbreeding with a
 * Gingerbread Pony preserves vanilla's inherited physical calculation while
 * producing a Marzipan Mule.</p>
 */
public final class DoughDonkey extends Donkey {
	public DoughDonkey(EntityType<? extends Donkey> type, Level level) {
		super(type, level);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
		if (mate instanceof DoughDonkey) {
			DoughDonkey child = CakeWorldEntities.DOUGH_DONKEY.get()
					.create(level);
			if (child != null) {
				setOffspringAttributes(mate, child);
			}
			return child;
		}
		if (mate instanceof GingerbreadPony) {
			MarzipanMule child =
					CakeWorldEntities.MARZIPAN_MULE.get()
							.create(level);
			if (child != null) {
				setOffspringAttributes(mate, child);
				child.setHealth(child.getMaxHealth());
			}
			return child;
		}
		return super.getBreedOffspring(level, mate);
	}
}
