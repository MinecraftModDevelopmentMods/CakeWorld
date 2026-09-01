package com.mcmoddev.cakeworld.world;

import java.util.List;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.WaferTurtle;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Converts fresh exact-type Turtle sources inside CakeWorld biomes.
 *
 * <p>The one-tick defer lets vanilla Turtle Eggs finish assigning baby age,
 * home position and placement before the source state is copied. Loaded,
 * outside-world and third-party Turtle subclasses remain untouched.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CakeWorldTurtleReplacement {
	private CakeWorldTurtleReplacement() {
	}

	@SubscribeEvent
	public static void onEntityJoin(
			EntityJoinWorldEvent event) {
		if (event.loadedFromDisk()
				|| event.getEntity().getType()
						!= EntityType.TURTLE
				|| !(event.getEntity()
						instanceof Turtle turtle)
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					if (!turtle.isRemoved()) {
						replaceIfInCakeWorldBiome(
								level, turtle);
					}
				}));
	}

	public static WaferTurtle replaceIfInCakeWorldBiome(
			ServerLevel level, Turtle turtle) {
		if (turtle.getType() != EntityType.TURTLE) {
			return null;
		}
		ResourceLocation biome = level.getBiome(
				turtle.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		if (biome == null
				|| !CakeWorld.MODID.equals(
						biome.getNamespace())) {
			return null;
		}

		WaferTurtle replacement =
				CakeWorldEntities.WAFER_TURTLE
						.get().create(level);
		if (replacement == null) {
			return null;
		}
		CompoundTag saved = turtle.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		replacement.load(saved);
		replacement.invulnerableTime =
				turtle.invulnerableTime;
		if (!level.addFreshEntity(replacement)) {
			replacement.discard();
			return null;
		}

		Entity vehicle = turtle.getVehicle();
		List<Entity> passengers =
				List.copyOf(turtle.getPassengers());
		if (vehicle != null) {
			turtle.stopRiding();
			replacement.startRiding(vehicle, true);
		}
		for (Entity passenger : passengers) {
			passenger.stopRiding();
			passenger.startRiding(
					replacement, true);
		}
		turtle.discard();
		return replacement;
	}
}
