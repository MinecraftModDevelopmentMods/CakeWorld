package zone.moddev.mc.cakeworld.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Villager family.
 *
 * <p>The inherited brain, schedules, professions, trades, POIs, inventory,
 * gossip, reputation, raids and golem logic remain authoritative. Vanilla
 * 1.18.2 selects social partners and parents by the literal Villager entity
 * type, so three small additive brain behaviours restore those same-family
 * seams for this registered subtype.</p>
 */
public class GingerbreadFolk extends Villager {
	private static final float SOCIAL_SPEED = 0.5F;
	private static final int SOCIAL_RANGE = 8;

	public GingerbreadFolk(
			EntityType<? extends Villager> type,
			Level level) {
		super(type, level);
	}

	private GingerbreadFolk(
			EntityType<? extends Villager> type,
			Level level, VillagerType villagerType) {
		super(type, level, villagerType);
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		Brain<?> brain = super.makeBrain(dynamic);
		installFamilyBehaviors(castBrain(brain));
		return brain;
	}

	@Override
	public void refreshBrain(ServerLevel level) {
		super.refreshBrain(level);
		installFamilyBehaviors(getBrain());
	}

	@Override
	public Villager getBreedOffspring(
			ServerLevel level, AgeableMob mate) {
		if (!(mate instanceof Villager villagerMate)) {
			return null;
		}
		double choice = random.nextDouble();
		VillagerType childType;
		if (choice < 0.5D) {
			childType = VillagerType.byBiome(
					level.getBiome(blockPosition()));
		} else if (choice < 0.75D) {
			childType = getVillagerData().getType();
		} else {
			childType = villagerMate
					.getVillagerData().getType();
		}

		GingerbreadFolk child =
				new GingerbreadFolk(familyType(),
						level, childType);
		child.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						child.blockPosition()),
				MobSpawnType.BREEDING,
				(SpawnGroupData)null, null);
		return child;
	}

	private void installFamilyBehaviors(
			Brain<Villager> brain) {
		EntityType<? extends Villager> family =
				familyType();
		brain.addActivity(
				net.minecraft.world.entity.schedule.Activity.CORE,
				ImmutableList.of(Pair.of(5,
						new SetEntityLookTarget(
								family, 8.0F))));
		brain.addActivity(
				net.minecraft.world.entity.schedule.Activity.PLAY,
				ImmutableList.of(Pair.of(1,
						InteractWith.of(family,
								SOCIAL_RANGE,
								MemoryModuleType
										.INTERACTION_TARGET,
								SOCIAL_SPEED, 2))));
		brain.addActivity(
				net.minecraft.world.entity.schedule.Activity.MEET,
				ImmutableList.of(
						Pair.of(1,
								InteractWith.of(family,
										SOCIAL_RANGE,
										MemoryModuleType
												.INTERACTION_TARGET,
										SOCIAL_SPEED,
										2)),
						Pair.of(2,
								new GingerbreadFolkTradeWith(
										family))));
		brain.addActivity(
				net.minecraft.world.entity.schedule.Activity.IDLE,
				ImmutableList.of(
						Pair.of(1,
								new RunOne<>(
										ImmutableList.of(
												Pair.of(
														InteractWith
																.of(family,
																		SOCIAL_RANGE,
																		MemoryModuleType
																				.INTERACTION_TARGET,
																		SOCIAL_SPEED,
																		2),
														2),
												Pair.of(
														new InteractWith<>(
																family,
																SOCIAL_RANGE,
																AgeableMob::canBreed,
																AgeableMob::canBreed,
																MemoryModuleType
																		.BREED_TARGET,
																SOCIAL_SPEED,
																2),
														1)))),
						Pair.of(2,
								new GingerbreadFolkTradeWith(
										family)),
						Pair.of(2,
								new GingerbreadFolkMakeLove(
										family))));
	}

	@SuppressWarnings("unchecked")
	private EntityType<? extends Villager> familyType() {
		return (EntityType<? extends Villager>)getType();
	}

	@SuppressWarnings("unchecked")
	private static Brain<Villager> castBrain(
			Brain<?> brain) {
		return (Brain<Villager>)brain;
	}
}
