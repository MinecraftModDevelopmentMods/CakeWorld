package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.LiquoriceWeaver;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal surface Spiders in CakeWorld biomes while preserving
 * command, passenger and Spider-jockey state. Loaded and outside entities are
 * deliberately untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldSpiderReplacement {
	private CakeWorldSpiderReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SPIDER
				|| !(event.getEntity()
						instanceof Spider spider)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!spider.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, spider);
					}
				}));
	}

	public static LiquoriceWeaver
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Spider spider) {
		ResourceLocation biome = level.getBiome(
				spider.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		LiquoriceWeaver replacement =
				CakeWorldEntities.LIQUORICE_WEAVER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = spider.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				spider.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = spider.getVehicle();
		List<Entity> passengers =
				List.copyOf(spider.getPassengers());
		if (vehicle != null) {
			spider.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		spider.discard();
		return replacement;
	}
}
