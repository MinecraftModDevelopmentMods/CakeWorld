package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.FudgeBrute;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh literal Piglin Brutes emitted by vanilla Bastion structures
 * inside CakeWorld terrain. Loaded entities, other biomes and third-party
 * types are deliberately untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldPiglinBruteReplacement {
	private CakeWorldPiglinBruteReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.PIGLIN_BRUTE
				|| !(event.getEntity()
						instanceof PiglinBrute brute)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!brute.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, brute);
					}
				}));
	}

	public static FudgeBrute replaceIfInCakeWorldBiome(
			ServerLevel level, PiglinBrute brute) {
		ResourceLocation biome = level.getBiome(
				brute.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		FudgeBrute replacement =
				CakeWorldEntities.FUDGE_BRUTE.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = brute.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		Entity vehicle = brute.getVehicle();
		if (vehicle != null) {
			brute.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		brute.discard();
		return replacement;
	}
}
