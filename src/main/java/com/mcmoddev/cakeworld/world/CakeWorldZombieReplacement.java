package com.mcmoddev.cakeworld.world;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.SoggyBiscuit;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Zombies inside CakeWorld terrain and completes the
 * Stale-Crumbler-to-Soggy-Biscuit drowning handoff.
 *
 * <p>Biome profiles emit the custom type directly. The deferred literal
 * boundary covers commands, eggs, spawners and village sieges without
 * touching loaded, outside-world or third-party Zombie subclasses. Drowning
 * keeps vanilla's private timers and finalized Drowned state; a one-tick
 * attachment snapshot restores passengers detached before Forge's post
 * event.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldZombieReplacement {
	private static final Map<UUID, ConversionAttachments>
			PENDING_DROWNED_ATTACHMENTS =
					new ConcurrentHashMap<>();

	private CakeWorldZombieReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ZOMBIE
				|| !(event.getEntity()
						instanceof Zombie zombie)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!zombie.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, zombie);
					}
				}));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingConversionPre(
			LivingConversionEvent.Pre event) {
		if (event.isCanceled()
				|| event.getEntity().getType()
						!= CakeWorldEntities
								.STALE_CRUMBLER.get()
				|| !(event.getEntity()
						instanceof StaleCrumbler source)
				|| event.getOutcome()
						!= EntityType.DROWNED
				|| !(source.level
						instanceof ServerLevel level)) {
			return;
		}

		UUID sourceId = source.getUUID();
		ConversionAttachments attachments =
				new ConversionAttachments(
						List.copyOf(
								source.getPassengers()),
						source.getLeashHolder());
		PENDING_DROWNED_ATTACHMENTS.put(
				sourceId, attachments);
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> PENDING_DROWNED_ATTACHMENTS
						.remove(sourceId,
								attachments)));
	}

	@SubscribeEvent
	public static void onLivingConversionPost(
			LivingConversionEvent.Post event) {
		if (event.getEntity().getType()
						!= CakeWorldEntities
								.STALE_CRUMBLER.get()
				|| !(event.getEntity()
						instanceof StaleCrumbler source)
				|| event.getOutcome().getType()
						!= EntityType.DROWNED
				|| !(event.getOutcome()
						instanceof Drowned outcome)
				|| !(outcome.level
						instanceof ServerLevel level)) {
			return;
		}

		ConversionAttachments attachments =
				PENDING_DROWNED_ATTACHMENTS.remove(
						source.getUUID());
		SoggyBiscuit replacement =
				CakeWorldEntities.SOGGY_BISCUIT
						.get().create(level);
		if (replacement != null) {
			replace(level, outcome, replacement,
					attachments == null
							? List.copyOf(
									source.getPassengers())
							: attachments.passengers,
					attachments == null
							? source.getLeashHolder()
							: attachments.leashHolder);
		}
	}

	public static StaleCrumbler
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Zombie zombie) {
		if (zombie.getType()
				!= EntityType.ZOMBIE) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				zombie.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		StaleCrumbler replacement =
				CakeWorldEntities.STALE_CRUMBLER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		return replace(level, zombie, replacement,
				List.of(), null);
	}

	private static <T extends Zombie> T replace(
			ServerLevel level, Zombie source,
			T replacement,
			List<Entity> sourcePassengers,
			Entity sourceLeashHolder) {
		CompoundTag saved = source.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		float health = source.getHealth();
		replacement.load(saved);
		replacement.setHealth(Math.min(
				health, replacement.getMaxHealth()));
		replacement.invulnerableTime =
				source.invulnerableTime;
		LivingEntity target = source.getTarget();
		LivingEntity lastHurtBy =
				source.getLastHurtByMob();
		Entity vehicle = source.getVehicle();
		Entity leashHolder =
				sourceLeashHolder != null
						? sourceLeashHolder
						: source.getLeashHolder();
		Set<Entity> passengers =
				new LinkedHashSet<>(
						source.getPassengers());
		passengers.addAll(sourcePassengers);

		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? source.tickCount
						: Math.max(1,
								source.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (vehicle != null) {
			source.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			source.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		source.discard();
		return replacement;
	}

	private static final class ConversionAttachments {
		private final List<Entity> passengers;
		private final Entity leashHolder;

		private ConversionAttachments(
				List<Entity> passengers,
				Entity leashHolder) {
			this.passengers = passengers;
			this.leashHolder = leashHolder;
		}
	}
}
