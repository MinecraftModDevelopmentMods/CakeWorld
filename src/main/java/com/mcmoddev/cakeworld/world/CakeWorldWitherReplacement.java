package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BurntSugarTempest;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts only freshly summoned literal Withers inside CakeWorld terrain.
 *
 * <p>The one-tick deferral is deliberate. Vanilla first constructs its
 * literal Wither and fires the Summoned Entity advancement trigger; conversion
 * happens afterwards so the authoritative Soul Sand/skull pattern and
 * Withering Heights continue to work unchanged. Loaded bosses, bosses outside
 * CakeWorld and third-party subclasses remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldWitherReplacement {
	private CakeWorldWitherReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.WITHER
				|| !(event.getEntity()
						instanceof WitherBoss wither)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!wither.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, wither);
					}
				}));
	}

	public static BurntSugarTempest
			replaceIfInCakeWorldBiome(
					ServerLevel level,
					WitherBoss wither) {
		if (wither.getType() != EntityType.WITHER) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				wither.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		BurntSugarTempest replacement =
				CakeWorldEntities.BURNT_SUGAR_TEMPEST
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = wither.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				wither.invulnerableTime;
		replacement.setInvulnerableTicks(
				wither.getInvulnerableTicks());
		LivingEntity target = wither.getTarget();
		LivingEntity lastHurtBy =
				wither.getLastHurtByMob();
		int[] alternativeTargets = {
				wither.getAlternativeTarget(0),
				wither.getAlternativeTarget(1),
				wither.getAlternativeTarget(2)
		};
		List<Entity> passengers =
				List.copyOf(wither.getPassengers());
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		replacement.tickCount =
				lastHurtBy == null
						? wither.tickCount
						: Math.max(1,
								wither.tickCount);
		replacement.setTarget(target);
		replacement.setLastHurtByMob(lastHurtBy);
		for (int head = 0;
				head < alternativeTargets.length;
				head++) {
			replacement.setAlternativeTarget(
					head,
					alternativeTargets[head]);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		wither.discard();
		return replacement;
	}
}
