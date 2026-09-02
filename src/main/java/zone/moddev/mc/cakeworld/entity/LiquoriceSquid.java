package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * CakeWorld's genuine surface Squid role.
 *
 * <p>Swimming, air supply, leashability, fleeing and ink defence remain
 * inherited. The natural-spawn predicate keeps vanilla's surface band while
 * accepting CakeWorld's water-tagged Lemonade.</p>
 */
public class LiquoriceSquid extends Squid {
	public LiquoriceSquid(
			EntityType<? extends Squid> type, Level level) {
		super(type, level);
	}

	public static boolean checkLiquoriceSquidSpawnRules(
			EntityType<? extends LiquoriceSquid> type,
			ServerLevelAccessor level,
			MobSpawnType reason, BlockPos pos,
			Random random) {
		int seaLevel = level.getSeaLevel();
		return pos.getY() >= seaLevel - 13
				&& pos.getY() <= seaLevel
				&& level.getFluidState(pos.below())
						.is(FluidTags.WATER)
				&& level.getFluidState(pos.above())
						.is(FluidTags.WATER);
	}
}
