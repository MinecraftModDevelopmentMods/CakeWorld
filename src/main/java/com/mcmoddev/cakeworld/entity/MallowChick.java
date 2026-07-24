package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public final class MallowChick extends Chicken {
	public MallowChick(EntityType<? extends Chicken> type, Level level) {
		super(type, level);
	}

	@Override
	public Chicken getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.MALLOW_CHICK.get().create(level);
	}
}
