package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Donkey-role pack animal.
 *
 * <p>The future Marzipan Mule will complete the mixed horse-family breeding
 * contract. Until then, vanilla's cross-species Mule result remains intact
 * while two Dough Donkeys always produce a Dough Donkey.</p>
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
		return super.getBreedOffspring(level, mate);
	}
}
