package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * A Lemonade-compatible Cod role for Soda Ocean.
 */
public final class SodaCod extends Cod {
	public SodaCod(EntityType<? extends Cod> type, Level level) {
		super(type, level);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(CakeWorldItems.SODA_COD_BUCKET.get());
	}

	/**
	 * Vanilla's equivalent predicate hard-codes a vanilla water block above
	 * the spawn. CakeWorld deliberately uses the standard water fluid tag so
	 * Lemonade and other compatible water-like fluids remain valid.
	 */
	public static boolean checkSodaCodSpawnRules(
			EntityType<? extends WaterAnimal> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		int seaLevel = level.getSeaLevel();
		return pos.getY() >= seaLevel - 13
				&& pos.getY() <= seaLevel
				&& level.getFluidState(pos.below()).is(FluidTags.WATER)
				&& level.getFluidState(pos.above()).is(FluidTags.WATER);
	}
}
