package com.mcmoddev.cakeworld.entity;

import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;

/**
 * Villager's bed-backed family behaviour with literal entity-type checks
 * generalized to the registered Gingerbread Folk type. An already-adjacent
 * HOME POI is also accepted when 1.18's path result stops at the bed
 * collision instead of marking the target reached.
 */
final class GingerbreadFolkMakeLove
		extends Behavior<Villager> {
	private static final float SPEED_MODIFIER = 0.5F;
	private final EntityType<? extends Villager> familyType;
	private Villager mate;
	private long birthTimestamp;

	GingerbreadFolkMakeLove(
			EntityType<? extends Villager> familyType) {
		super(ImmutableMap.of(
				MemoryModuleType.BREED_TARGET,
				MemoryStatus.VALUE_PRESENT,
				MemoryModuleType
						.NEAREST_VISIBLE_LIVING_ENTITIES,
				MemoryStatus.VALUE_PRESENT),
				350, 350);
		this.familyType = familyType;
	}

	@Override
	protected boolean checkExtraStartConditions(
			ServerLevel level, Villager villager) {
		return isBreedingPossible(villager);
	}

	@Override
	protected boolean canStillUse(ServerLevel level,
			Villager villager, long gameTime) {
		return gameTime <= birthTimestamp
				&& canContinueBreeding(villager);
	}

	@Override
	protected void start(ServerLevel level,
			Villager villager, long gameTime) {
		mate = (Villager)villager.getBrain()
				.getMemory(MemoryModuleType.BREED_TARGET)
				.orElseThrow();
		BehaviorUtils.lockGazeAndWalkToEachOther(
				villager, mate, SPEED_MODIFIER);
		level.broadcastEntityEvent(mate, (byte)18);
		level.broadcastEntityEvent(villager, (byte)18);
		birthTimestamp = gameTime + 275L
				+ villager.getRandom().nextInt(50);
	}

	@Override
	protected void tick(ServerLevel level,
			Villager villager, long gameTime) {
		if (mate == null) {
			return;
		}
		if (villager.distanceToSqr(mate) > 5.0D) {
			return;
		}
		BehaviorUtils.lockGazeAndWalkToEachOther(
				villager, mate, SPEED_MODIFIER);
		if (gameTime >= birthTimestamp) {
			villager.eatAndDigestFood();
			mate.eatAndDigestFood();
			tryToGiveBirth(level, villager, mate);
		} else if (villager.getRandom()
				.nextInt(35) == 0) {
			level.broadcastEntityEvent(mate, (byte)12);
			level.broadcastEntityEvent(
					villager, (byte)12);
		}
	}

	@Override
	protected void stop(ServerLevel level,
			Villager villager, long gameTime) {
		villager.getBrain().eraseMemory(
				MemoryModuleType.BREED_TARGET);
		mate = null;
	}

	private boolean isBreedingPossible(
			Villager villager) {
		Brain<Villager> brain = villager.getBrain();
		Optional<AgeableMob> mate = brain
				.getMemory(MemoryModuleType.BREED_TARGET)
				.filter(candidate ->
						candidate.getType()
								== familyType);
		return mate.isPresent()
				&& BehaviorUtils.targetIsValid(
						brain,
						MemoryModuleType.BREED_TARGET,
						familyType)
				&& villager.canBreed()
				&& mate.get().canBreed();
	}

	private boolean canContinueBreeding(
			Villager villager) {
		return mate != null
				&& mate.isAlive()
				&& mate.getType() == familyType
				&& villager.canBreed()
				&& mate.canBreed()
				&& villager.getBrain().getMemory(
								MemoryModuleType
										.NEAREST_VISIBLE_LIVING_ENTITIES)
						.filter(visible ->
								visible.contains(candidate ->
										candidate == mate))
						.isPresent();
	}

	private void tryToGiveBirth(ServerLevel level,
			Villager first, Villager second) {
		Optional<BlockPos> bed =
				takeVacantBed(level, first);
		if (bed.isEmpty()) {
			level.broadcastEntityEvent(second, (byte)13);
			level.broadcastEntityEvent(first, (byte)13);
			return;
		}
		Optional<Villager> child =
				breed(level, first, second);
		if (child.isPresent()) {
			giveBedToChild(level, child.get(),
					bed.get());
		} else {
			level.getPoiManager().release(bed.get());
			DebugPackets.sendPoiTicketCountPacket(
					level, bed.get());
		}
	}

	private Optional<BlockPos> takeVacantBed(
			ServerLevel level, Villager villager) {
		return level.getPoiManager().take(
				PoiType.HOME.getPredicate(),
				position -> canReach(villager,
						position),
				villager.blockPosition(), 48);
	}

	private boolean canReach(Villager villager,
			BlockPos position) {
		Path path = villager.getNavigation()
				.createPath(position,
						PoiType.HOME.getValidRange());
		return (path != null && path.canReach())
				|| villager.blockPosition()
						.distSqr(position) <= 4.0D;
	}

	private Optional<Villager> breed(ServerLevel level,
			Villager first, Villager second) {
		Villager child =
				first.getBreedOffspring(level, second);
		if (child == null) {
			return Optional.empty();
		}
		first.setAge(6000);
		second.setAge(6000);
		child.setAge(-24000);
		child.moveTo(first.getX(), first.getY(),
				first.getZ(), 0.0F, 0.0F);
		level.addFreshEntityWithPassengers(child);
		level.broadcastEntityEvent(child, (byte)12);
		return Optional.of(child);
	}

	private void giveBedToChild(ServerLevel level,
			Villager child, BlockPos bed) {
		child.getBrain().setMemory(
				MemoryModuleType.HOME,
				GlobalPos.of(level.dimension(), bed));
	}
}
