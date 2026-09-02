package zone.moddev.mc.cakeworld.world;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.FudgeBoar;
import zone.moddev.mc.cakeworld.entity.StaleFudgeBoar;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Completes Fudge Boar zombification and converts only fresh literal Zoglins
 * inside CakeWorld terrain.
 *
 * <p>Forge's post-conversion event runs after vanilla has finalized baby
 * state, equipment, vehicle and Nausea, allowing CakeWorld to preserve that
 * exact result without copying Hoglin's private timer. A one-tick
 * pre-conversion snapshot carries source leashes and passengers because
 * vanilla detaches them before Forge publishes the finalized outcome.
 * Loaded, outside-world and third-party Zoglins remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldZoglinReplacement {
	private static final Map<UUID, ConversionAttachments>
			PENDING_ATTACHMENTS =
					new ConcurrentHashMap<>();

	private CakeWorldZoglinReplacement() {
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingConversionPre(
			LivingConversionEvent.Pre event) {
		if (event.isCanceled()
				|| event.getEntity().getType()
						!= CakeWorldEntities
								.FUDGE_BOAR.get()
				|| !(event.getEntity()
						instanceof FudgeBoar source)
				|| event.getOutcome()
						!= EntityType.ZOGLIN
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

	@SubscribeEvent
	public static void onLivingConversion(
			LivingConversionEvent.Post event) {
		if (event.getEntity().getType()
						!= CakeWorldEntities
								.FUDGE_BOAR.get()
				|| !(event.getEntity()
						instanceof FudgeBoar source)
				|| event.getOutcome().getType()
						!= EntityType.ZOGLIN
				|| !(event.getOutcome()
						instanceof Zoglin outcome)
				|| !(outcome.level
						instanceof ServerLevel level)) {
			return;
		}

		ConversionAttachments attachments =
				PENDING_ATTACHMENTS.remove(
						source.getUUID());
		replace(level, outcome,
				attachments == null
						? List.copyOf(
								source.getPassengers())
						: attachments.passengers,
				attachments == null
						? source.getLeashHolder()
						: attachments.leashHolder);
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ZOGLIN
				|| !(event.getEntity()
						instanceof Zoglin zoglin)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!zoglin.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, zoglin);
					}
				}));
	}

	public static StaleFudgeBoar
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Zoglin zoglin) {
		if (zoglin.getType()
				!= EntityType.ZOGLIN) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				zoglin.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}
		return replace(level, zoglin,
				List.of(), null);
	}

	private static StaleFudgeBoar replace(
			ServerLevel level, Zoglin zoglin,
			List<Entity> sourcePassengers,
			Entity sourceLeashHolder) {
		StaleFudgeBoar replacement =
				CakeWorldEntities.STALE_FUDGE_BOAR
						.get().create(level);
		if (replacement == null) {
			return null;
		}

		CompoundTag saved = zoglin.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		float health = zoglin.getHealth();
		replacement.load(saved);
		replacement.setHealth(Math.min(
				health, replacement.getMaxHealth()));
		replacement.invulnerableTime =
				zoglin.invulnerableTime;
		LivingEntity target = zoglin.getTarget();
		LivingEntity lastHurtBy =
				zoglin.getLastHurtByMob();
		Entity vehicle = zoglin.getVehicle();
		Entity leashHolder =
				sourceLeashHolder != null
						? sourceLeashHolder
						: zoglin.getLeashHolder();
		Set<Entity> passengers =
				new LinkedHashSet<>(
						zoglin.getPassengers());
		passengers.addAll(sourcePassengers);

		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? zoglin.tickCount
						: Math.max(1,
								zoglin.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (vehicle != null) {
			zoglin.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			zoglin.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		zoglin.discard();
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
