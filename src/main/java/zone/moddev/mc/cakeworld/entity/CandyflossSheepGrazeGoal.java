package zone.moddev.mc.cakeworld.entity;

import java.util.EnumSet;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Edible-world counterpart to Sheep's inherited grass-eating goal.
 *
 * <p>The timing and random cadence are the vanilla Sheep values. CakeWorld
 * terrain produces a brief crumb cue but is not consumed, so a flock cannot
 * erase the welcoming landscape merely by regrowing fleece.</p>
 */
public final class CandyflossSheepGrazeGoal extends Goal {
	public static final TagKey<Block> GRAZING_SURFACES =
			TagKey.create(Registry.BLOCK_REGISTRY,
					new ResourceLocation(CakeWorld.MODID,
							"candyfloss_sheep_grazing_surfaces"));
	private static final int EAT_ANIMATION_TICKS = 40;

	private final CandyflossSheep sheep;
	private int eatAnimationTick;

	public CandyflossSheepGrazeGoal(CandyflossSheep sheep) {
		this.sheep = sheep;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		int chance = sheep.isBaby() ? 50 : 1000;
		return sheep.getRandom().nextInt(chance) == 0
				&& isEdibleSurface();
	}

	@Override
	public void start() {
		eatAnimationTick =
				adjustedTickDelay(EAT_ANIMATION_TICKS);
		sheep.level.broadcastEntityEvent(sheep, (byte)10);
		sheep.getNavigation().stop();
	}

	@Override
	public void stop() {
		eatAnimationTick = 0;
	}

	@Override
	public boolean canContinueToUse() {
		return eatAnimationTick > 0;
	}

	@Override
	public void tick() {
		eatAnimationTick =
				Math.max(0, eatAnimationTick - 1);
		if (eatAnimationTick != adjustedTickDelay(4)
				|| !isEdibleSurface()) {
			return;
		}

		BlockPos surfacePos =
				sheep.blockPosition().below();
		BlockState surface =
				sheep.level.getBlockState(surfacePos);
		if (ForgeEventFactory.getMobGriefingEvent(
				sheep.level, sheep)) {
			sheep.level.levelEvent(2001, surfacePos,
					Block.getId(surface));
		}
		sheep.ate();
		sheep.gameEvent(GameEvent.EAT,
				sheep.eyeBlockPosition());
	}

	public int getEatAnimationTick() {
		return eatAnimationTick;
	}

	private boolean isEdibleSurface() {
		return sheep.level.getBlockState(
				sheep.blockPosition().below())
						.is(GRAZING_SURFACES);
	}
}
