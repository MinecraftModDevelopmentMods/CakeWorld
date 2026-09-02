package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

/**
 * CakeWorld's Stray-role chilled ranged creature.
 *
 * <p>The complete Skeleton body and AI, sunlight response, equipment, exact
 * Slowness arrows, sounds and powder-snow ecology remain inherited. Only
 * lower-difficulty contact is made harmless.</p>
 */
public class FrostedArcher extends Stray {
	public FrostedArcher(
			EntityType<? extends Stray> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}
		FrostedArcherDamageSafety.applyChilledShot(
				this, living);
		return true;
	}

	/**
	 * The exact Stray powder-snow-column and sky rule, parameterized for the
	 * CakeWorld entity type.
	 */
	public static boolean checkFrostedArcherSpawnRules(
			EntityType<FrostedArcher> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		BlockPos.MutableBlockPos probe = pos.mutable();
		do {
			probe.move(Direction.UP);
		} while (level.getBlockState(probe)
				.is(Blocks.POWDER_SNOW));
		return Monster.checkMonsterSpawnRules(
				type, level, reason, pos, random)
				&& (reason == MobSpawnType.SPAWNER
						|| level.canSeeSky(probe.below()));
	}
}
