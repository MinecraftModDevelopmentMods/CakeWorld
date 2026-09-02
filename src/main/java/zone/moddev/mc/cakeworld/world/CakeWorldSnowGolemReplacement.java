package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.IceCreamGolem;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Snow Golems built or commanded into CakeWorld
 * terrain. Loaded and outside entities are deliberately left untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldSnowGolemReplacement {
	private CakeWorldSnowGolemReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SNOW_GOLEM
				|| !(event.getEntity()
						instanceof SnowGolem snowGolem)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		// Let CarvedPumpkinBlock finish consuming and updating its pattern
		// before replacing the freshly built literal entity.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!snowGolem.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, snowGolem);
					}
				}));
	}

	public static IceCreamGolem
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					SnowGolem snowGolem) {
		ResourceLocation biome = level.getBiome(
				snowGolem.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		IceCreamGolem replacement =
				CakeWorldEntities.ICE_CREAM_GOLEM
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = snowGolem.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				snowGolem.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = snowGolem.getVehicle();
		Entity leashHolder = snowGolem.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(snowGolem.getPassengers());
		if (vehicle != null) {
			snowGolem.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			snowGolem.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		snowGolem.discard();
		return replacement;
	}
}
