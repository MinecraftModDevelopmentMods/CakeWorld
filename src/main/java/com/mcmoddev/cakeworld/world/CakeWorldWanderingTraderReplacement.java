package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.UUID;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.TravellingConfectioner;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Wandering Traders from the vanilla caravan spawner
 * inside CakeWorld terrain. The deferred conversion lets the spawner finish
 * its offers, wander target, despawn delay and both Trader-Llama leads first.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldWanderingTraderReplacement {
	private static final double CARAVAN_LEASH_SCAN = 16.0D;

	private CakeWorldWanderingTraderReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.WANDERING_TRADER
				|| !(event.getEntity()
						instanceof WanderingTrader trader)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!trader.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, trader);
					}
				}));
	}

	public static TravellingConfectioner
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					WanderingTrader trader) {
		if (trader.getType()
				!= EntityType.WANDERING_TRADER) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				trader.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		TravellingConfectioner replacement =
				CakeWorldEntities
						.TRAVELLING_CONFECTIONER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = trader.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.addCakeWorldOffersIfMissing();
		replacement.invulnerableTime =
				trader.invulnerableTime;
		Player tradingPlayer =
				trader.getTradingPlayer();
		LivingEntity lastHurtBy =
				trader.getLastHurtByMob();
		boolean hasRestriction =
				trader.hasRestriction();
		BlockPos restrictionCenter =
				trader.getRestrictCenter();
		int restrictionRadius =
				Math.round(
						trader.getRestrictRadius());
		Entity vehicle = trader.getVehicle();
		Entity leashHolder = trader.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(trader.getPassengers());
		List<Mob> caravanAnimals =
				level.getEntitiesOfClass(
						Mob.class,
						trader.getBoundingBox()
								.inflate(
										CARAVAN_LEASH_SCAN),
						mob -> mob != trader
								&& mob.getLeashHolder()
										== trader);
		UUID sourceUuid = trader.getUUID();
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		// A zero hurt timestamp is indistinguishable from the initial value
		// used by Trader Llama's defence goal.
		replacement.tickCount =
				lastHurtBy == null
						? trader.tickCount
						: Math.max(1,
								trader.tickCount);
		replacement.setTradingPlayer(tradingPlayer);
		replacement.setLastHurtByMob(lastHurtBy);
		if (hasRestriction) {
			replacement.restrictTo(
					restrictionCenter,
					restrictionRadius);
		}

		ServerLevelData overworldData = level
				.getServer().getWorldData()
				.overworldData();
		UUID activeTrader =
				overworldData.getWanderingTraderId();
		if (sourceUuid.equals(activeTrader)) {
			overworldData.setWanderingTraderId(
					replacement.getUUID());
		}
		if (vehicle != null) {
			trader.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			trader.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		for (Mob caravanAnimal : caravanAnimals) {
			caravanAnimal.dropLeash(true, false);
			caravanAnimal.setLeashedTo(
					replacement, true);
		}
		trader.discard();
		return replacement;
	}
}
