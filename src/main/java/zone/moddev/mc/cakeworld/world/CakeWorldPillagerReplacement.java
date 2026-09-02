package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.BiscuitBandit;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Pillagers from patrol, Outpost and raid paths inside
 * CakeWorld terrain. Loaded, outside-biome and third-party entities remain
 * untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldPillagerReplacement {
	private CakeWorldPillagerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.PILLAGER
				|| !(event.getEntity()
						instanceof Pillager pillager)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!pillager.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, pillager);
					}
				}));
	}

	public static BiscuitBandit replaceIfInCakeWorldBiome(
			ServerLevel level, Pillager pillager) {
		ResourceLocation biome = level.getBiome(
				pillager.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		BiscuitBandit replacement =
				CakeWorldEntities.BISCUIT_BANDIT.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = pillager.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		saved.remove("RaidId");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Raid raid = pillager.getCurrentRaid();
		int wave = pillager.getWave();
		boolean leader = raid != null
				&& raid.getLeader(wave) == pillager;
		Entity vehicle = pillager.getVehicle();
		if (raid != null) {
			raid.joinRaid(wave, replacement,
					null, true);
			if (leader) {
				raid.removeLeader(wave);
			}
			raid.removeFromRaid(pillager, true);
			if (leader) {
				raid.setLeader(wave, replacement);
			}
		}
		if (vehicle != null) {
			pillager.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		pillager.discard();
		return replacement;
	}
}
