package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.MacaronClam;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Shulkers placed by End City structure markers inside
 * CakeWorld biomes. Loaded entities and entities outside CakeWorld stay
 * untouched, preserving the fresh-world conversion boundary.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldShulkerReplacement {
	private CakeWorldShulkerReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.SHULKER
				|| !(event.getEntity()
						instanceof Shulker shulker)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		// Structure entities can join before their chunk reaches FULL.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!shulker.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, shulker);
					}
				}));
	}

	public static MacaronClam replaceIfInCakeWorldBiome(
			ServerLevel level, Shulker shulker) {
		ResourceLocation biome = level.getBiome(
				shulker.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		MacaronClam replacement =
				CakeWorldEntities.MACARON_CLAM.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = shulker.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		shulker.discard();
		return replacement;
	}
}
