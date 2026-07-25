package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine sterile Mule-role pack mount.
 */
public final class MarzipanMule extends Mule {
	public MarzipanMule(
			EntityType<? extends Mule> type, Level level) {
		super(type, level);
	}

	/**
	 * Vanilla exposes an offspring factory even though Mule mating is disabled.
	 * Preserve that unreachable API seam without leaking a literal Mule.
	 */
	@Override
	public AgeableMob getBreedOffspring(
			ServerLevel level, AgeableMob mate) {
		return CakeWorldEntities.MARZIPAN_MULE.get()
				.create(level);
	}
}
