package zone.moddev.mc.cakeworld.world;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.FudgeBrute;
import zone.moddev.mc.cakeworld.entity.FudgeFolk;
import zone.moddev.mc.cakeworld.entity.StaleFudgeFolk;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Completes Fudge Folk and Fudge Brute zombification and converts only fresh
 * literal Zombified Piglins inside CakeWorld terrain.
 *
 * <p>Forge's post-conversion event runs after vanilla has finalized Piglin
 * baby, equipment, vehicle and Nausea state. A one-tick pre-conversion
 * snapshot carries source passengers that vanilla detaches before publishing
 * the outcome. Fresh literal command, egg, spawner and Truffle-Pig lightning
 * outcomes use the same deferred world boundary. Loaded, outside-world and
 * third-party Zombified-Piglin types remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldZombifiedPiglinReplacement {
	private static final Map<UUID, ConversionAttachments>
			PENDING_ATTACHMENTS =
					new ConcurrentHashMap<>();

	private CakeWorldZombifiedPiglinReplacement() {
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingConversionPre(
			LivingConversionEvent.Pre event) {
		if (event.isCanceled()
				|| !isCakeWorldPiglin(
						event.getEntity())
				|| event.getOutcome()
						!= EntityType.ZOMBIFIED_PIGLIN
				|| !(event.getEntity().level
						instanceof ServerLevel level)) {
			return;
		}

		UUID sourceId =
				event.getEntity().getUUID();
		ConversionAttachments attachments =
				new ConversionAttachments(
						List.copyOf(event.getEntity()
								.getPassengers()),
						event.getEntity()
								instanceof Mob mob
										? mob.getLeashHolder()
										: null);
		PENDING_ATTACHMENTS.put(
				sourceId, attachments);
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> PENDING_ATTACHMENTS.remove(
						sourceId, attachments)));
	}

	@SubscribeEvent
	public static void onLivingConversionPost(
			LivingConversionEvent.Post event) {
		if (!isCakeWorldPiglin(
						event.getEntity())
				|| event.getOutcome().getType()
						!= EntityType.ZOMBIFIED_PIGLIN
				|| !(event.getOutcome()
						instanceof ZombifiedPiglin outcome)
				|| !(outcome.level
						instanceof ServerLevel level)) {
			return;
		}

		ConversionAttachments attachments =
				PENDING_ATTACHMENTS.remove(
						event.getEntity()
								.getUUID());
		replace(level, outcome,
				attachments == null
						? List.copyOf(event.getEntity()
								.getPassengers())
						: attachments.passengers,
				attachments == null
						? null
						: attachments.leashHolder);
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ZOMBIFIED_PIGLIN
				|| !(event.getEntity()
						instanceof ZombifiedPiglin piglin)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!piglin.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, piglin);
					}
				}));
	}

	public static StaleFudgeFolk
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					ZombifiedPiglin piglin) {
		if (piglin.getType()
				!= EntityType.ZOMBIFIED_PIGLIN) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				piglin.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}
		return replace(level, piglin,
				List.of(), null);
	}

	private static StaleFudgeFolk replace(
			ServerLevel level,
			ZombifiedPiglin source,
			List<Entity> sourcePassengers,
			Entity sourceLeashHolder) {
		StaleFudgeFolk replacement =
				CakeWorldEntities.STALE_FUDGE_FOLK
						.get().create(level);
		if (replacement == null) {
			return null;
		}

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

	private static boolean isCakeWorldPiglin(
			Entity entity) {
		return entity instanceof FudgeFolk
				|| entity instanceof FudgeBrute;
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
