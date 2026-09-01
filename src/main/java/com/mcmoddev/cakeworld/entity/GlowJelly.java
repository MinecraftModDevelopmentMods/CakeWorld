package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

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
		boolean cosmicReef = isCosmicReef(level.getBiome(pos));
		return (cosmicReef || pos.getY() <= level.getSeaLevel() - 33)
				&& level.getRawBrightness(pos, 0) == 0
				&& level.getFluidState(pos).is(FluidTags.WATER);
	}

	public static boolean isCosmicReef(Holder<Biome> biome) {
		return biome.unwrapKey()
				.map(key -> CakeWorld.MODID.equals(
						key.location().getNamespace())
						&& "cosmic_jelly_reefs".equals(
								key.location().getPath()))
				.orElse(false);
	}
}
