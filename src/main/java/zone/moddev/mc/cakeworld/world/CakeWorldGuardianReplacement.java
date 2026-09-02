package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.GumballGuardian;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts literal Guardians emitted by the vanilla monument structure spawn
 * override in fresh CakeWorld terrain. Loaded entities remain untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldGuardianReplacement {
	private CakeWorldGuardianReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType() != EntityType.GUARDIAN
				|| !(event.getEntity() instanceof Guardian guardian)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		// Structure-spawned entities can join before their chunk is FULL.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1, () -> {
					if (guardian.isRemoved()) {
						return;
					}
					GumballGuardian replacement =
							convertIfInCakeWorldBiome(
									level, guardian);
					if (replacement != null) {
						guardian.discard();
						level.addFreshEntity(replacement);
					}
				}));
	}

	public static GumballGuardian convertIfInCakeWorldBiome(
			ServerLevel level, Guardian guardian) {
		ResourceLocation biome = level.getBiome(
				guardian.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return null;
		}

		GumballGuardian replacement =
				CakeWorldEntities.GUMBALL_GUARDIAN.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = guardian.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		return replacement;
	}
}
