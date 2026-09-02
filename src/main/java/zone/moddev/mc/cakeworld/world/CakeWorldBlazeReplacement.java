package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.CinnamonSpark;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Fortress and spawner Blazes in CakeWorld terrain.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldBlazeReplacement {
	private CakeWorldBlazeReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.BLAZE
				|| !(event.getEntity() instanceof Blaze blaze)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!blaze.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, blaze);
					}
				}));
	}

	public static CinnamonSpark replaceIfInCakeWorldBiome(
			ServerLevel level, Blaze blaze) {
		if (blaze.getType() != EntityType.BLAZE) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				blaze.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}
		CinnamonSpark replacement =
				CakeWorldEntities.CINNAMON_SPARK
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = blaze.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				blaze.invulnerableTime;
		LivingEntity target = blaze.getTarget();
		LivingEntity lastHurtBy =
				blaze.getLastHurtByMob();
		Entity vehicle = blaze.getVehicle();
		List<Entity> passengers =
				List.copyOf(blaze.getPassengers());
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? blaze.tickCount
						: Math.max(1,
								blaze.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (vehicle != null) {
			blaze.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		blaze.discard();
		return replacement;
	}
}
