package zone.moddev.mc.cakeworld.item;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A short, direction-readable alternative to Chorus Fruit's random teleport.
 *
 * <p>The food checks a three-block line in the direction the eater faces and
 * chooses the furthest safe standing space. It never loads a new chunk, enters
 * a solid block or teleports over a missing floor.</p>
 */
public final class GuidedChorusFoodItem extends Item {
	public static final int MAX_DISTANCE = 3;

	public GuidedChorusFoodItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack,
			Level level, LivingEntity consumer) {
		ItemStack result = super.finishUsingItem(stack, level, consumer);
		if (!level.isClientSide && !consumer.isPassenger()) {
			Direction direction = consumer.getDirection();
			BlockPos destination = findSafeDestination(level,
					consumer.blockPosition(), direction);
			if (destination != null) {
				double oldX = consumer.getX();
				double oldY = consumer.getY();
				double oldZ = consumer.getZ();
				consumer.teleportTo(destination.getX() + 0.5D,
						destination.getY(),
						destination.getZ() + 0.5D);
				consumer.fallDistance = 0.0F;
				level.playSound(null, oldX, oldY, oldZ,
						SoundEvents.CHORUS_FRUIT_TELEPORT,
						SoundSource.PLAYERS, 1.0F, 1.15F);
				consumer.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT,
						1.0F, 1.15F);
			}
			if (consumer instanceof Player player) {
				player.getCooldowns().addCooldown(this, 20);
			}
		}
		return result;
	}

	@Nullable
	public static BlockPos findSafeDestination(LevelReader level,
			BlockPos origin, Direction direction) {
		Direction horizontal = direction.getAxis().isHorizontal()
				? direction : Direction.NORTH;
		for (int distance = MAX_DISTANCE; distance >= 1; distance--) {
			BlockPos column = origin.relative(horizontal, distance);
			for (int yOffset : new int[] {0, 1, -1, 2, -2}) {
				BlockPos feet = column.offset(0, yOffset, 0);
				if (!level.hasChunkAt(feet)) {
					continue;
				}
				BlockPos head = feet.above();
				BlockPos floor = feet.below();
				BlockState feetState = level.getBlockState(feet);
				BlockState headState = level.getBlockState(head);
				BlockState floorState = level.getBlockState(floor);
				if (feetState.getCollisionShape(level, feet).isEmpty()
						&& headState.getCollisionShape(level, head).isEmpty()
						&& floorState.isFaceSturdy(level, floor,
								Direction.UP)) {
					return feet.immutable();
				}
			}
		}
		return null;
	}
}
