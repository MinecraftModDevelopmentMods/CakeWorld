package zone.moddev.mc.cakeworld.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.TradeWithVillager;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

/**
 * Vanilla Villager social trading with its literal entity-type check pointed
 * at the registered Gingerbread Folk family.
 */
final class GingerbreadFolkTradeWith
		extends TradeWithVillager {
	private final EntityType<? extends Villager> familyType;

	GingerbreadFolkTradeWith(
			EntityType<? extends Villager> familyType) {
		this.familyType = familyType;
	}

	@Override
	protected boolean checkExtraStartConditions(
			ServerLevel level, Villager villager) {
		return BehaviorUtils.targetIsValid(
				villager.getBrain(),
				MemoryModuleType.INTERACTION_TARGET,
				familyType);
	}
}
