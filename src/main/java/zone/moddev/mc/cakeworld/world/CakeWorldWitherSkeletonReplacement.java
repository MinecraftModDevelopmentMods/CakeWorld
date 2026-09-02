package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.BurntCandyKnight;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts only fresh literal Wither Skeletons inside CakeWorld terrain.
 *
 * <p>Nether Fortress remains the authoritative spawn source and therefore
 * continues to emit the vanilla type. A one-tick deferral lets structure
 * spawning and equipment finalization finish before the exact literal entity
 * is replaced. Loaded entities, outside worlds and third-party subclasses are
 * intentionally untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldWitherSkeletonReplacement {
	private CakeWorldWitherSkeletonReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.WITHER_SKELETON
				|| !(event.getEntity()
						instanceof WitherSkeleton skeleton)
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

	public static BurntCandyKnight
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					WitherSkeleton skeleton) {
		if (skeleton.getType()
				!= EntityType.WITHER_SKELETON) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				skeleton.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		BurntCandyKnight replacement =
				CakeWorldEntities.BURNT_CANDY_KNIGHT
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = skeleton.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				skeleton.invulnerableTime;
		LivingEntity target = skeleton.getTarget();
		LivingEntity lastHurtBy =
				skeleton.getLastHurtByMob();
		Entity vehicle = skeleton.getVehicle();
		List<Entity> passengers =
				List.copyOf(skeleton.getPassengers());
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? skeleton.tickCount
						: Math.max(1,
								skeleton.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		if (vehicle != null) {
			skeleton.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		skeleton.discard();
		return replacement;
	}
}
