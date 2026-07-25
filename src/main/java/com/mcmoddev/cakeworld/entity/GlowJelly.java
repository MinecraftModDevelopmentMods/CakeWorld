package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * The luminous Glow Squid role for Soda Ocean.
 *
 * Glow Squid already use the water fluid tag for swimming and fleeing. Their
 * natural-spawn predicate is the one exception: it requires the literal
 * vanilla water block. Glow-Jelly retains the depth and darkness rules while
 * accepting CakeWorld's water-tagged Lemonade.
 */
public final class GlowJelly extends GlowSquid {
	public GlowJelly(EntityType<? extends GlowJelly> type, Level level) {
		super(type, level);
	}

	public static boolean checkGlowJellySpawnRules(
			EntityType<? extends GlowJelly> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return pos.getY() <= level.getSeaLevel() - 33
				&& level.getRawBrightness(pos, 0) == 0
				&& level.getFluidState(pos).is(FluidTags.WATER);
	}
}
