package zone.moddev.mc.cakeworld.world;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.CrumbledGingerbreadFolk;
import zone.moddev.mc.cakeworld.entity.GingerbreadFolk;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Zombie Villagers and completes both directions of the
 * Gingerbread-Folk infection/cure cycle.
 *
 * <p>Vanilla remains authoritative for the private cure timer, accelerator
 * scan, equipment handling, advancement and reputation event. CakeWorld
 * replaces only the fully finalized literal conversion outcome. One-tick
 * attachment snapshots restore passengers and leashes detached before Forge's
 * post-conversion event.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldZombieVillagerReplacement {
	private static final Map<UUID, ConversionAttachments>
			PENDING_ATTACHMENTS =
					new ConcurrentHashMap<>();

	private CakeWorldZombieVillagerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ZOMBIE_VILLAGER
				|| !(event.getEntity()
						instanceof ZombieVillager zombieVillager)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!zombieVillager.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level,
								zombieVillager);
					}
				}));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingConversionPre(
			LivingConversionEvent.Pre event) {
		if (event.isCanceled()
				|| !isCakeWorldFamilyConversion(
						event.getEntity().getType(),
						event.getOutcome())
				|| !(event.getEntity() instanceof Mob source)
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
		PENDING_ATTACHMENTS.put(
				sourceId, attachments);
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> PENDING_ATTACHMENTS.remove(
						sourceId, attachments)));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingConversionPost(
			LivingConversionEvent.Post event) {
		ConversionAttachments attachments =
				PENDING_ATTACHMENTS.remove(
						event.getEntity().getUUID());
		if (event.getEntity().getType()
						== CakeWorldEntities
								.GINGERBREAD_FOLK.get()
				&& event.getOutcome().getType()
						== EntityType.ZOMBIE_VILLAGER
				&& event.getOutcome()
						instanceof ZombieVillager outcome
				&& !outcome.isRemoved()
				&& outcome.level
						instanceof ServerLevel level) {
			CrumbledGingerbreadFolk replacement =
					CakeWorldEntities
							.CRUMBLED_GINGERBREAD_FOLK
							.get().create(level);
			if (replacement != null) {
				replace(level, outcome,
						replacement, attachments);
			}
			return;
		}

		if (event.getEntity().getType()
						== CakeWorldEntities
								.CRUMBLED_GINGERBREAD_FOLK
								.get()
				&& event.getOutcome().getType()
						== EntityType.VILLAGER
				&& event.getOutcome()
						instanceof Villager outcome
				&& !outcome.isRemoved()
				&& outcome.level
						instanceof ServerLevel level) {
			GingerbreadFolk replacement =
					CakeWorldEntities
							.GINGERBREAD_FOLK
							.get().create(level);
			if (replacement != null) {
				replace(level, outcome,
						replacement, attachments);
			}
		}
	}

	public static CrumbledGingerbreadFolk
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					ZombieVillager zombieVillager) {
		if (zombieVillager.getType()
				!= EntityType.ZOMBIE_VILLAGER) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				zombieVillager.blockPosition())
				.unwrapKey()
				.map(key -> key.location())
				.orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		CrumbledGingerbreadFolk replacement =
				CakeWorldEntities
						.CRUMBLED_GINGERBREAD_FOLK
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		return replace(level, zombieVillager,
				replacement, null);
	}

	private static boolean isCakeWorldFamilyConversion(
			EntityType<?> source,
			EntityType<?> outcome) {
		return source == CakeWorldEntities
						.GINGERBREAD_FOLK.get()
					&& outcome
							== EntityType.ZOMBIE_VILLAGER
				|| source == CakeWorldEntities
						.CRUMBLED_GINGERBREAD_FOLK
						.get()
					&& outcome == EntityType.VILLAGER;
	}

	private static <T extends Mob> T replace(
			ServerLevel level, Mob source,
			T replacement,
			ConversionAttachments attachments) {
		CompoundTag saved = source.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		float health = source.getHealth();
		replacement.load(saved);
		if (replacement instanceof ZombieVillager zombieVillager
				&& saved.contains("Gossips", 9)) {
			/*
			 * Vanilla 1.18.2 saves Zombie Villager gossip as a list but
			 * mistakenly checks for a compound while loading it. Restore
			 * the saved list through the public API so CakeWorld's second
			 * replacement hop does not erase Villager reputation data.
			 */
			zombieVillager.setGossips(
					saved.getList("Gossips", 10));
		}
		replacement.setHealth(Math.min(
				health, replacement.getMaxHealth()));
		replacement.invulnerableTime =
				source.invulnerableTime;
		LivingEntity target = source.getTarget();
		LivingEntity lastHurtBy =
				source.getLastHurtByMob();
		Entity vehicle = source.getVehicle();
		Entity leashHolder =
				attachments != null
						&& attachments.leashHolder
								!= null
						? attachments.leashHolder
						: source.getLeashHolder();
		Set<Entity> passengers =
				new LinkedHashSet<>(
						source.getPassengers());
		if (attachments != null) {
			passengers.addAll(
					attachments.passengers);
		}
		if (source instanceof Villager sourceVillager
				&& replacement
						instanceof GingerbreadFolk folk) {
			folk.setTradingPlayer(
					sourceVillager.getTradingPlayer());
		}

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
