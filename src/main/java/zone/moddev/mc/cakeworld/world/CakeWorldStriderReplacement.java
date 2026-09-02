package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.FudgeSkater;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Strider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Striders inside CakeWorld biomes. Loaded and
 * outside-CakeWorld entities remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldStriderReplacement {
	private CakeWorldStriderReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.STRIDER
				|| !(event.getEntity()
						instanceof Strider strider)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!strider.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, strider);
					}
				}));
	}

	public static FudgeSkater
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Strider strider) {
		ResourceLocation biome = level.getBiome(
				strider.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		FudgeSkater replacement =
				CakeWorldEntities.FUDGE_SKATER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = strider.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				strider.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = strider.getVehicle();
		Entity leashHolder = strider.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(strider.getPassengers());
		if (vehicle != null) {
			strider.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			strider.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		strider.discard();
		return replacement;
	}
}
