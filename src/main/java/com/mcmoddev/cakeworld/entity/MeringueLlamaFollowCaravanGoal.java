package com.mcmoddev.cakeworld.entity;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.phys.Vec3;

/**
 * Vanilla-equivalent caravan following without literal entity-type checks.
 *
 * <p>The 1.18.2 goal searches only {@code minecraft:llama} and
 * {@code minecraft:trader_llama}. Using the shared Llama class contract lets
 * CakeWorld Llamas form their own caravans and follow compatible Llama
 * subclasses without replacing or rewriting those other entities.</p>
 */
public final class MeringueLlamaFollowCaravanGoal extends Goal {
	private static final int CARAVAN_LIMIT = 8;

	private final Llama llama;
	private double speedModifier;
	private int distCheckCounter;

	public MeringueLlamaFollowCaravanGoal(Llama llama,
			double speedModifier) {
		this.llama = llama;
		this.speedModifier = speedModifier;
		setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (llama.isLeashed() || llama.inCaravan()) {
			return false;
		}

		List<Llama> nearby = llama.level.getEntitiesOfClass(
				Llama.class,
				llama.getBoundingBox().inflate(9.0D, 4.0D, 9.0D),
				candidate -> candidate != llama);
		Llama head = null;
		double nearestDistance = Double.MAX_VALUE;

		for (Llama candidate : nearby) {
			if (candidate.inCaravan()
					&& !candidate.hasCaravanTail()) {
				double distance =
						llama.distanceToSqr(candidate);
				if (distance <= nearestDistance) {
					nearestDistance = distance;
					head = candidate;
				}
			}
		}

		if (head == null) {
			for (Llama candidate : nearby) {
				if (candidate.isLeashed()
						&& !candidate.hasCaravanTail()) {
					double distance =
							llama.distanceToSqr(candidate);
					if (distance <= nearestDistance) {
						nearestDistance = distance;
						head = candidate;
					}
				}
			}
		}

		if (head == null || nearestDistance < 4.0D
				|| (!head.isLeashed()
						&& !firstIsLeashed(head, 1))) {
			return false;
		}
		llama.joinCaravan(head);
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		Llama head = llama.getCaravanHead();
		if (!llama.inCaravan() || head == null
				|| !head.isAlive()
				|| !firstIsLeashed(llama, 0)) {
			return false;
		}

		double distance = llama.distanceToSqr(head);
		if (distance > 676.0D) {
			if (speedModifier <= 3.0D) {
				speedModifier *= 1.2D;
				distCheckCounter = reducedTickDelay(40);
				return true;
			}
			if (distCheckCounter == 0) {
				return false;
			}
		}
		if (distCheckCounter > 0) {
			--distCheckCounter;
		}
		return true;
	}

	@Override
	public void stop() {
		llama.leaveCaravan();
		speedModifier = (double) 2.1F;
	}

	@Override
	public void tick() {
		if (!llama.inCaravan()
				|| llama.getLeashHolder()
						instanceof LeashFenceKnotEntity) {
			return;
		}
		Llama head = llama.getCaravanHead();
		if (head == null) {
			return;
		}
		double distance = llama.distanceTo(head);
		Vec3 direction = new Vec3(
				head.getX() - llama.getX(),
				head.getY() - llama.getY(),
				head.getZ() - llama.getZ())
						.normalize()
						.scale(Math.max(distance - 2.0D, 0.0D));
		llama.getNavigation().moveTo(
				llama.getX() + direction.x,
				llama.getY() + direction.y,
				llama.getZ() + direction.z,
				speedModifier);
	}

	private boolean firstIsLeashed(Llama candidate,
			int depth) {
		if (depth > CARAVAN_LIMIT) {
			return false;
		}
		if (!candidate.inCaravan()) {
			return false;
		}
		Llama head = candidate.getCaravanHead();
		if (head == null) {
			return false;
		}
		if (head.isLeashed()) {
			return true;
		}
		return firstIsLeashed(head, depth + 1);
	}
}
