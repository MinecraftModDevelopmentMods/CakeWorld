package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.CandyCaneArcher;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Skeletons from spawners, spider jockeys and
 * skeleton-horse traps inside CakeWorld biomes. Loaded and outside-CakeWorld
 * entities remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldSkeletonReplacement {
	private CakeWorldSkeletonReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SKELETON
				|| !(event.getEntity()
						instanceof Skeleton skeleton)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!skeleton.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, skeleton);
					}
				}));
	}

	public static CandyCaneArcher replaceIfInCakeWorldBiome(
			ServerLevel level, Skeleton skeleton) {
		ResourceLocation biome = level.getBiome(
				skeleton.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		CandyCaneArcher replacement =
				CakeWorldEntities.CANDY_CANE_ARCHER.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = skeleton.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				skeleton.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = skeleton.getVehicle();
		skeleton.discard();
		if (vehicle != null && !vehicle.isRemoved()) {
			replacement.startRiding(vehicle, true);
		}
		return replacement;
	}
}
