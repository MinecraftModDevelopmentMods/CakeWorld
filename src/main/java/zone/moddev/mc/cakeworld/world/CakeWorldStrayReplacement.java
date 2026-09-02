package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.FrostedArcher;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Stray;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Strays inside CakeWorld biomes, including the
 * intermediate result of Candy-Cane Archer freezing. Loaded and
 * outside-CakeWorld entities remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldStrayReplacement {
	private CakeWorldStrayReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.STRAY
				|| !(event.getEntity()
						instanceof Stray stray)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!stray.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, stray);
					}
				}));
	}

	public static FrostedArcher
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Stray stray) {
		ResourceLocation biome = level.getBiome(
				stray.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		FrostedArcher replacement =
				CakeWorldEntities.FROSTED_ARCHER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = stray.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				stray.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = stray.getVehicle();
		Entity leashHolder = stray.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(stray.getPassengers());
		if (vehicle != null) {
			stray.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			stray.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		stray.discard();
		return replacement;
	}
}
