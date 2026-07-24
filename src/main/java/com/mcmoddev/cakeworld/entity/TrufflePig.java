package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

public final class TrufflePig extends Pig {
	public TrufflePig(EntityType<? extends Pig> type, Level level) {
		super(type, level);
	}

	@Override
	public Pig getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.TRUFFLE_PIG.get().create(level);
	}
}
