package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.GingerSnapHound;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts only fresh literal Wolves inside CakeWorld terrain.
 *
 * <p>Biome profiles normally emit the custom type directly. This deferred
 * boundary covers commands, spawn eggs, spawners and children of retained
 * literal Wolves after their source has finalized state. Loaded animals,
 * outside worlds and third-party Wolf subclasses remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldWolfReplacement {
	private CakeWorldWolfReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.WOLF
				|| !(event.getEntity()
						instanceof Wolf wolf)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!wolf.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, wolf);
					}
				}));
	}

	public static GingerSnapHound
			replaceIfInCakeWorldBiome(
					ServerLevel level, Wolf wolf) {
		if (wolf.getType() != EntityType.WOLF) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				wolf.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		GingerSnapHound replacement =
				CakeWorldEntities.GINGER_SNAP_HOUND
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = wolf.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		float health = wolf.getHealth();
		replacement.load(saved);
		replacement.setHealth(Math.min(
				health, replacement.getMaxHealth()));
		replacement.invulnerableTime =
				wolf.invulnerableTime;
		LivingEntity target = wolf.getTarget();
		LivingEntity lastHurtBy =
				wolf.getLastHurtByMob();
		Entity vehicle = wolf.getVehicle();
		Entity leashHolder = wolf.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(wolf.getPassengers());
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? wolf.tickCount
						: Math.max(1,
								wolf.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (vehicle != null) {
			wolf.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			wolf.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		wolf.discard();
		return replacement;
	}
}
