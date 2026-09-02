package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.MirageConfectioner;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal command-summoned Illusioners in CakeWorld terrain.
 * Loaded entities and summons outside CakeWorld biomes remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldIllusionerReplacement {
	private CakeWorldIllusionerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ILLUSIONER
				|| !(event.getEntity()
						instanceof Illusioner illusioner)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!illusioner.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, illusioner);
					}
				}));
	}

	public static MirageConfectioner
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					Illusioner illusioner) {
		ResourceLocation biome = level.getBiome(
				illusioner.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		MirageConfectioner replacement =
				CakeWorldEntities.MIRAGE_CONFECTIONER
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = illusioner.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		saved.remove("RaidId");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Raid raid = illusioner.getCurrentRaid();
		int wave = illusioner.getWave();
		boolean leader = raid != null
				&& raid.getLeader(wave) == illusioner;
		Entity vehicle = illusioner.getVehicle();
		if (raid != null) {
			raid.joinRaid(wave, replacement,
					null, true);
			if (leader) {
				raid.removeLeader(wave);
			}
			raid.removeFromRaid(illusioner, true);
			if (leader) {
				raid.setLeader(wave, replacement);
			}
		}
		if (vehicle != null) {
			illusioner.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		illusioner.discard();
		return replacement;
	}
}
