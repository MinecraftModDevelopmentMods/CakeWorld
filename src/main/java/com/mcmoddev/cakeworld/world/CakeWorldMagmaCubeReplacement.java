package com.mcmoddev.cakeworld.world;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.HotFudgeBlob;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts literal Magma Cubes emitted by the vanilla Nether Fortress spawn
 * override in fresh CakeWorld terrain. Loaded entities and other worlds remain
 * untouched.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldMagmaCubeReplacement {
	private CakeWorldMagmaCubeReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.MAGMA_CUBE
				|| !(event.getEntity()
						instanceof MagmaCube magmaCube)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		// Structure-spawned entities can join before their chunk is FULL.
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!magmaCube.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, magmaCube);
					}
				}));
	}

	public static HotFudgeBlob replaceIfInCakeWorldBiome(
			ServerLevel level, MagmaCube magmaCube) {
		ResourceLocation biome = level.getBiome(
				magmaCube.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		HotFudgeBlob replacement =
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = magmaCube.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}
		magmaCube.discard();
		return replacement;
	}
}
