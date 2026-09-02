package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.RollingPinRaider;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Vindicators from raid waves, Ravager seats and
 * woodland-mansion markers inside CakeWorld terrain. Loaded, outside-world
 * and third-party entities remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldVindicatorReplacement {
	private CakeWorldVindicatorReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.VINDICATOR
				|| !(event.getEntity()
						instanceof Vindicator vindicator)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!vindicator.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, vindicator);
					}
				}));
	}

	public static RollingPinRaider
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Vindicator vindicator) {
		if (vindicator.getType()
				!= EntityType.VINDICATOR) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				vindicator.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		RollingPinRaider replacement =
				CakeWorldEntities.ROLLING_PIN_RAIDER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = vindicator.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		saved.remove("RaidId");
		replacement.load(saved);
		replacement.invulnerableTime =
				vindicator.invulnerableTime;
		LivingEntity target = vindicator.getTarget();
		LivingEntity lastHurtBy =
				vindicator.getLastHurtByMob();
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);

		Raid raid = vindicator.getCurrentRaid();
		int wave = vindicator.getWave();
		boolean leader = raid != null
				&& raid.getLeader(wave) == vindicator;
		Entity vehicle = vindicator.getVehicle();
		List<Entity> passengers =
				List.copyOf(vindicator.getPassengers());
		if (raid != null) {
			raid.joinRaid(wave, replacement,
					null, true);
			if (leader) {
				raid.removeLeader(wave);
			}
			raid.removeFromRaid(vindicator, true);
			if (leader) {
				raid.setLeader(wave, replacement);
			}
		}
		if (vehicle != null) {
			vindicator.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		vindicator.discard();
		return replacement;
	}
}
