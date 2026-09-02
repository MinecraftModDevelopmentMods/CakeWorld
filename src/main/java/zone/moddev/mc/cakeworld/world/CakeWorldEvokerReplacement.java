package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.SourSorcerer;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts the literal Evokers created by raid waves and woodland-mansion
 * markers in fresh CakeWorld terrain. Loaded entities are left untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldEvokerReplacement {
	private CakeWorldEvokerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType() != EntityType.EVOKER
				|| !(event.getEntity() instanceof Evoker evoker)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1, () -> {
					if (!evoker.isRemoved()) {
						replaceIfInCakeWorldBiome(level, evoker);
					}
				}));
	}

	public static SourSorcerer replaceIfInCakeWorldBiome(
			ServerLevel level, Evoker evoker) {
		ResourceLocation biome = level.getBiome(
				evoker.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return null;
		}

		SourSorcerer replacement =
				CakeWorldEntities.SOUR_SORCERER.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = evoker.saveWithoutId(new CompoundTag());
		saved.remove("UUID");
		// Raid membership is transferred explicitly so the wave set and its
		// total-health accounting remain coherent.
		saved.remove("RaidId");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Raid raid = evoker.getCurrentRaid();
		int wave = evoker.getWave();
		boolean leader = raid != null
				&& raid.getLeader(wave) == evoker;
		Entity vehicle = evoker.getVehicle();
		if (raid != null) {
			raid.joinRaid(wave, replacement, null, true);
			if (leader) {
				raid.removeLeader(wave);
			}
			raid.removeFromRaid(evoker, true);
			if (leader) {
				raid.setLeader(wave, replacement);
			}
		}
		if (vehicle != null) {
			evoker.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		evoker.discard();
		return replacement;
	}
}
