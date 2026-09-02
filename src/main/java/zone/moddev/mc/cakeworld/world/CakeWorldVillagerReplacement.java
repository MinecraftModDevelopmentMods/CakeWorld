package zone.moddev.mc.cakeworld.world;

import java.util.List;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.GingerbreadFolk;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh exact-type Villager sources inside CakeWorld biomes.
 *
 * <p>The one-tick defer lets structure NBT, profession assignment, breeding
 * and Zombie-Villager curing finish before the complete brain, POI, offer,
 * gossip and inventory state is copied. Loaded, outside-world and third-party
 * Villagers remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldVillagerReplacement {
	private CakeWorldVillagerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.VILLAGER
				|| !(event.getEntity()
						instanceof Villager villager)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!villager.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, villager);
					}
				}));
	}

	public static GingerbreadFolk
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Villager villager) {
		if (villager.getType()
				!= EntityType.VILLAGER) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				villager.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		GingerbreadFolk replacement =
				CakeWorldEntities.GINGERBREAD_FOLK
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = villager.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.setTradingPlayer(
				villager.getTradingPlayer());
		replacement.invulnerableTime =
				villager.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = villager.getVehicle();
		Entity leashHolder = villager.getLeashHolder();
		List<Entity> passengers =
				List.copyOf(villager.getPassengers());
		if (vehicle != null) {
			villager.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		if (leashHolder != null) {
			villager.dropLeash(true, false);
			replacement.setLeashedTo(
					leashHolder, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		villager.discard();
		return replacement;
	}
}
