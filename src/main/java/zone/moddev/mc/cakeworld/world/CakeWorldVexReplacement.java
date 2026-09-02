package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.SourSprite;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Vex;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh exact-type Vex sources inside CakeWorld biomes.
 *
 * <p>Vanilla sets an Evoker-summoned Vex's owner, bound origin and limited
 * life immediately before adding it to the level. A one-tick defer therefore
 * captures the complete summon state without touching loaded or third-party
 * entities. Owner, target and charging are runtime-only seams and are copied
 * explicitly alongside ordinary saved state.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldVexReplacement {
	private CakeWorldVexReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType() != EntityType.VEX
				|| !(event.getEntity() instanceof Vex vex)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!vex.isRemoved()) {
						replaceIfInCakeWorldBiome(level, vex);
					}
				}));
	}

	public static SourSprite replaceIfInCakeWorldBiome(
			ServerLevel level, Vex vex) {
		if (vex.getType() != EntityType.VEX) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				vex.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		SourSprite replacement =
				CakeWorldEntities.SOUR_SPRITE.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = vex.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		Mob owner = vex.getOwner();
		if (owner != null) {
			replacement.setOwner(owner);
		}
		replacement.setTarget(vex.getTarget());
		replacement.setIsCharging(vex.isCharging());
		replacement.invulnerableTime =
				vex.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = vex.getVehicle();
		List<Entity> passengers =
				List.copyOf(vex.getPassengers());
		if (vehicle != null) {
			vex.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(replacement, true);
		}
		vex.discard();
		return replacement;
	}
}
