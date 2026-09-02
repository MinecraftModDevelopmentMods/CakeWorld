package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * Harmless wrapped-sweet cave ambience.
 *
 * Bat's roosting, light-sensitive spawning and flight behavior are retained
 * deliberately while CakeWorld supplies a distinct registry identity.
 */
public final class BonbonBat extends Bat {
	public BonbonBat(EntityType<? extends Bat> type, Level level) {
		super(type, level);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static boolean checkBonbonBatSpawnRules(
			EntityType<BonbonBat> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		// Vanilla hard-codes EntityType<Bat> even though the predicate only
		// uses the type through Mob's generic spawn checks.
		return Bat.checkBatSpawnRules((EntityType) type, level, reason,
				pos, random);
	}
}
