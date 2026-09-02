package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.WaferWraith;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Redirects the vanilla insomnia spawner's literal fresh Phantoms only after
 * it has applied its vanilla timing, sky, local-difficulty, altitude and group
 * rules. Loaded entities and Phantoms outside CakeWorld biomes are untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldPhantomReplacement {
	private CakeWorldPhantomReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.PHANTOM
				|| !(event.getEntity() instanceof Phantom phantom)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		// Special spawns can join before their chunk is FULL. Delay biome
		// inspection and replacement to avoid worldgen chunk-loading deadlocks.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!phantom.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, phantom);
					}
				}));
	}

	public static WaferWraith replaceIfInCakeWorldBiome(
			ServerLevel level, Phantom phantom) {
		ResourceLocation biome = level.getBiome(
				phantom.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		WaferWraith replacement =
				CakeWorldEntities.WAFER_WRAITH.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = phantom.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		phantom.discard();
		return replacement;
	}
}
