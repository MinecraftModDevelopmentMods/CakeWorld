package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.LiquoriceSquid;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Squid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Squid sources inside CakeWorld biomes while
 * preserving saved, leash and riding state. Loaded and outside entities are
 * deliberately untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldSquidReplacement {
	private CakeWorldSquidReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SQUID
				|| !(event.getEntity()
						instanceof Squid squid)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!squid.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, squid);
					}
				}));
	}

	public static LiquoriceSquid
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Squid squid) {
		ResourceLocation biome = level.getBiome(
				squid.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		LiquoriceSquid replacement =
				CakeWorldEntities.LIQUORICE_SQUID
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = squid.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				squid.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = squid.getVehicle();
		Entity leashHolder = squid.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(squid.getPassengers());
		if (vehicle != null) {
			squid.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			squid.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		squid.discard();
		return replacement;
	}
}
