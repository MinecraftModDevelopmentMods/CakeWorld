package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.GrandGumballGuardian;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts literal Elder Guardians created by ocean-monument pieces in fresh
 * CakeWorld terrain. Loaded entities are deliberately left untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldElderGuardianReplacement {
	private CakeWorldElderGuardianReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.ELDER_GUARDIAN
				|| !(event.getEntity()
						instanceof ElderGuardian guardian)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		// Monument pieces can emit the entity before their chunk is FULL.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1, () -> {
					if (guardian.isRemoved()) {
						return;
					}
					GrandGumballGuardian replacement =
							convertIfInCakeWorldBiome(
									level, guardian);
					if (replacement != null) {
						guardian.discard();
						level.addFreshEntity(replacement);
					}
				}));
	}

	public static GrandGumballGuardian convertIfInCakeWorldBiome(
			ServerLevel level, ElderGuardian guardian) {
		ResourceLocation biome = level.getBiome(
				guardian.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return null;
		}

		GrandGumballGuardian replacement =
				CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get()
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
