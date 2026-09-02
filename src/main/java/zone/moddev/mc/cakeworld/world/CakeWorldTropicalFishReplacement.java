package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.JellybeanFish;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh exact-type Tropical Fish sources inside CakeWorld biomes.
 * Loaded, outside-world and third-party subclasses remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldTropicalFishReplacement {
	private CakeWorldTropicalFishReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.TROPICAL_FISH
				|| !(event.getEntity()
						instanceof TropicalFish fish)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		// Bucket extra data is applied after EntityType.spawn adds the fish.
		// Deferring also avoids biome reads from a pre-FULL worldgen chunk.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!fish.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, fish);
					}
				}));
	}

	public static JellybeanFish
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					TropicalFish fish) {
		if (fish.getType() != EntityType.TROPICAL_FISH) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				fish.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		JellybeanFish replacement =
				CakeWorldEntities.JELLYBEAN_FISH
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = fish.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				fish.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = fish.getVehicle();
		List<Entity> passengers =
				List.copyOf(fish.getPassengers());
		if (vehicle != null) {
			fish.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		fish.discard();
		return replacement;
	}
}
