package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.StaleGingerbreadSteed;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts only fresh literal Zombie Horses inside CakeWorld terrain.
 *
 * <p>The deferred boundary lets command, spawn-egg and spawner initialization
 * finish before state is copied. Loaded entities, outside worlds and unknown
 * third-party subclasses remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldZombieHorseReplacement {
	private CakeWorldZombieHorseReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ZOMBIE_HORSE
				|| !(event.getEntity()
						instanceof ZombieHorse horse)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!horse.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, horse);
					}
				}));
	}

	public static StaleGingerbreadSteed
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					ZombieHorse horse) {
		if (horse.getType()
				!= EntityType.ZOMBIE_HORSE) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				horse.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		StaleGingerbreadSteed replacement =
				CakeWorldEntities
						.STALE_GINGERBREAD_STEED
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = horse.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		float health = horse.getHealth();
		replacement.load(saved);
		replacement.setHealth(Math.min(
				health, replacement.getMaxHealth()));
		replacement.invulnerableTime =
				horse.invulnerableTime;
		LivingEntity target = horse.getTarget();
		LivingEntity lastHurtBy =
				horse.getLastHurtByMob();
		Entity vehicle = horse.getVehicle();
		Entity leashHolder = horse.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(horse.getPassengers());
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? horse.tickCount
						: Math.max(1,
								horse.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (vehicle != null) {
			horse.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			horse.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		horse.discard();
		return replacement;
	}
}
