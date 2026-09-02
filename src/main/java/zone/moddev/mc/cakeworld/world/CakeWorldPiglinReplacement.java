package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.FudgeFolk;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Piglins emitted by vanilla structures inside
 * CakeWorld terrain. Loaded entities, other biomes and third-party types are
 * deliberately untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldPiglinReplacement {
	private CakeWorldPiglinReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.PIGLIN
				|| !(event.getEntity() instanceof Piglin piglin)
				|| !(event.getWorld() instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!piglin.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, piglin);
					}
				}));
	}

	public static FudgeFolk replaceIfInCakeWorldBiome(
			ServerLevel level, Piglin piglin) {
		ResourceLocation biome = level.getBiome(
				piglin.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		FudgeFolk replacement =
				CakeWorldEntities.FUDGE_FOLK.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = piglin.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		piglin.discard();
		return replacement;
	}
}
