package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import javax.annotation.Nullable;

import zone.moddev.mc.cakeworld.compat.VanillaRoleAdvancements;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.TurtleEggBlock;

/**
 * CakeWorld's complete Turtle role.
 *
 * <p>The homeward nesting cycle, long-distance travel, amphibious movement,
 * Seagrass diet, egg state, sounds and Scute growth reward remain inherited.
 * CakeWorld adds only its entity identity, same-family factory, edible nesting
 * surfaces through the public sand tag and the vanilla advancement bridge.</p>
 */
public class WaferTurtle extends Turtle {
	@Nullable
	private ServerPlayer recentLoveCause;

	public WaferTurtle(EntityType<? extends Turtle> type,
			Level level) {
		super(type, level);
	}

	@Override
	@Nullable
	public AgeableMob getBreedOffspring(ServerLevel level,
			AgeableMob partner) {
		return CakeWorldEntities.WAFER_TURTLE.get()
				.create(level);
	}

	/**
	 * Turtle breeding lays eggs rather than emitting a
	 * {@code BabyEntitySpawnEvent}. The inherited goal sets {@code HasEgg}
	 * immediately before resetting love, so this is the one lossless seam at
	 * which the player and vanilla Turtle criterion are still available.
	 */
	@Override
	public void resetLove() {
		ServerPlayer player = hasEgg()
				? recentLoveCause : null;
		if (player == null && hasEgg()) {
			player = getLoveCause();
		}
		super.resetLove();
		recentLoveCause = null;
		if (player != null) {
			VanillaRoleAdvancements.creditBredRole(
					player, getType());
		}
	}

	@Override
	public void setInLove(@Nullable Player player) {
		super.setInLove(player);
		recentLoveCause = player
				instanceof ServerPlayer serverPlayer
						? serverPlayer : null;
	}

	public static boolean checkWaferTurtleSpawnRules(
			EntityType<WaferTurtle> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return pos.getY() < level.getSeaLevel() + 4
				&& TurtleEggBlock.onSand(level, pos)
				&& isBrightEnoughToSpawn(level, pos);
	}
}
