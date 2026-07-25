package com.mcmoddev.cakeworld.entity;

import java.util.HashSet;
import java.util.Set;

import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

/**
 * CakeWorld's genuine Snow Golem role.
 *
 * <p>The inherited helper AI, pumpkin/shearing state, climate sensitivity and
 * vanilla Snowball remain the source of truth. Only fresh snow-trail cells
 * created during the inherited step are translated to CakeWorld icing.</p>
 */
public class IceCreamGolem extends SnowGolem {
	private static final int TRAIL_SNAPSHOT_RADIUS = 2;

	public IceCreamGolem(
			EntityType<? extends SnowGolem> type, Level level) {
		super(type, level);
	}

	@Override
	public void aiStep() {
		Set<BlockPos> initiallyEmpty = level.isClientSide
				? Set.of()
				: snapshotNearbyEmptyTrailCells();
		super.aiStep();
		if (!level.isClientSide) {
			translateFreshSnowTrail(initiallyEmpty);
		}
	}

	private Set<BlockPos>
			snapshotNearbyEmptyTrailCells() {
		Set<BlockPos> empty = new HashSet<>();
		int centerX = Mth.floor(getX());
		int y = Mth.floor(getY());
		int centerZ = Mth.floor(getZ());
		for (int x = centerX - TRAIL_SNAPSHOT_RADIUS;
				x <= centerX + TRAIL_SNAPSHOT_RADIUS;
				x++) {
			for (int z =
					centerZ - TRAIL_SNAPSHOT_RADIUS;
					z <= centerZ
							+ TRAIL_SNAPSHOT_RADIUS;
					z++) {
				BlockPos pos = new BlockPos(x, y, z);
				if (level.isEmptyBlock(pos)) {
					empty.add(pos);
				}
			}
		}
		return empty;
	}

	private void translateFreshSnowTrail(
			Set<BlockPos> initiallyEmpty) {
		int y = Mth.floor(getY());
		for (int corner = 0; corner < 4;
				corner++) {
			int trailX = Mth.floor(getX()
					+ (corner % 2 * 2 - 1)
							* 0.25F);
			int trailZ = Mth.floor(getZ()
					+ (corner / 2 % 2 * 2 - 1)
							* 0.25F);
			BlockPos pos = new BlockPos(
					trailX, y, trailZ);
			if (initiallyEmpty.contains(pos)
					&& level.getBlockState(pos)
							.is(Blocks.SNOW)) {
				level.setBlockAndUpdate(pos,
						CakeWorldBlocks.ICING_LAYER
								.get()
								.defaultBlockState());
			}
		}
	}
}
