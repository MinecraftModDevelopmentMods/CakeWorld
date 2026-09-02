package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Repairs vanilla Wolf's literal {@code minecraft:rabbit} prey predicate.
 *
 * <p>Fox prey checks use {@code instanceof Rabbit} and already recognise a
 * Gummy Bunny. Untamed vanilla Wolves instead compare the exact entity type,
 * so this narrow bridge gives them the same nearest-prey opportunity without
 * modifying tamed Wolves or third-party Wolf entity types.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GummyBunnyPredatorCompatibility {
	private GummyBunnyPredatorCompatibility() {
	}

	@SubscribeEvent
	public static void onLivingUpdate(
			LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntityLiving() instanceof Wolf wolf)
				|| wolf.level.isClientSide
				|| wolf.getType() != EntityType.WOLF
				|| wolf.isTame()
				|| wolf.getTarget() != null
				|| wolf.getRandom().nextInt(10) != 0) {
			return;
		}
		assignNearestPrey(wolf);
	}

	public static boolean assignNearestPrey(Wolf wolf) {
		if (wolf.getType() != EntityType.WOLF
				|| wolf.isTame()
				|| wolf.getTarget() != null) {
			return false;
		}
		double range = wolf.getAttributeValue(
				Attributes.FOLLOW_RANGE);
		AABB search = wolf.getBoundingBox().inflate(range,
				4.0D, range);
		GummyBunny nearest = null;
		double nearestDistance = Double.MAX_VALUE;
		for (GummyBunny bunny : wolf.level.getEntitiesOfClass(
				GummyBunny.class, search,
				candidate -> candidate.isAlive()
						&& !candidate.isInvulnerable())) {
			double distance = wolf.distanceToSqr(bunny);
			if (distance < nearestDistance
					&& wolf.getSensing().hasLineOfSight(bunny)) {
				nearest = bunny;
				nearestDistance = distance;
			}
		}
		if (nearest == null) {
			return false;
		}
		wolf.setTarget(nearest);
		return true;
	}
}
