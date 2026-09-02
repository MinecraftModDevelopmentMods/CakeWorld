package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.SugarMite;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts literal Endermites produced by Ender Pearls in fresh CakeWorld
 * terrain. Loaded entities are deliberately left untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldEndermiteReplacement {
	private CakeWorldEndermiteReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType() != EntityType.ENDERMITE
				|| !(event.getEntity()
						instanceof Endermite endermite)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1, () -> {
					if (endermite.isRemoved()) {
						return;
					}
					SugarMite replacement =
							convertIfInCakeWorldBiome(
									level, endermite);
					if (replacement != null) {
						endermite.discard();
						level.addFreshEntity(replacement);
					}
				}));
	}

	public static SugarMite convertIfInCakeWorldBiome(
			ServerLevel level, Endermite endermite) {
		ResourceLocation biome = level.getBiome(
				endermite.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(biome.getNamespace())) {
			return null;
		}

		SugarMite replacement = CakeWorldEntities.SUGAR_MITE.get()
				.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = endermite.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		return replacement;
	}
}
