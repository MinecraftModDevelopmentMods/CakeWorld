package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's tameable settlement companion.
 *
 * Remaining a genuine Cat preserves taming, morning gifts, creeper/phantom
 * reactions, sleeping behavior, and all eleven Complete Catalogue variants.
 */
public final class CustardCat extends Cat {
	public CustardCat(EntityType<? extends Cat> type, Level level) {
		super(type, level);
	}

	@Override
	public Cat getBreedOffspring(ServerLevel level, AgeableMob partner) {
		CustardCat child = CakeWorldEntities.CUSTARD_CAT.get().create(level);
		if (child == null || !(partner instanceof Cat otherParent)) {
			return child;
		}

		child.setCatType(random.nextBoolean()
				? getCatType() : otherParent.getCatType());
		if (isTame()) {
			child.setOwnerUUID(getOwnerUUID());
			child.setTame(true);
			DyeColor collar = random.nextBoolean()
					? getCollarColor() : otherParent.getCollarColor();
			child.setCollarColor(collar);
		}
		return child;
	}
}
