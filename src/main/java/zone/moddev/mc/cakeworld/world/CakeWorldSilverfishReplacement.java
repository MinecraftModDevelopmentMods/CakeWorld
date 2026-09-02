package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.CrumbMite;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Silverfish from Stronghold spawners and infested
 * blocks inside CakeWorld biomes. Loaded and outside-CakeWorld entities remain
 * untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldSilverfishReplacement {
	private CakeWorldSilverfishReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SILVERFISH
				|| !(event.getEntity()
						instanceof Silverfish silverfish)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!silverfish.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, silverfish);
					}
				}));
	}

	public static CrumbMite replaceIfInCakeWorldBiome(
			ServerLevel level, Silverfish silverfish) {
		ResourceLocation biome = level.getBiome(
				silverfish.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		CrumbMite replacement =
				CakeWorldEntities.CRUMB_MITE.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = silverfish.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		silverfish.discard();
		return replacement;
	}
}
