package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;

public final class CandyflossSheep extends Sheep {
	public CandyflossSheep(EntityType<? extends Sheep> type, Level level) {
		super(type, level);
	}

	@Override
	public Sheep getBreedOffspring(ServerLevel level, AgeableMob partner) {
		CandyflossSheep child = CakeWorldEntities.CANDYFLOSS_SHEEP.get().create(level);
		if (child != null && partner instanceof Sheep otherParent) {
			child.setColor(level.random.nextBoolean() ? getColor() : otherParent.getColor());
		}
		return child;
	}
}
