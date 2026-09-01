package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;

public final class CocoaCow extends Cow {
	public CocoaCow(EntityType<? extends Cow> type, Level level) {
		super(type, level);
	}

	@Override
	public Cow getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.COCOA_COW.get().create(level);
	}
}
