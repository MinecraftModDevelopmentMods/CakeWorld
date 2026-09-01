package com.mcmoddev.cakeworld.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;
import com.mcmoddev.cakeworld.entity.FudgeSkater;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Repairs vanilla's two exact-EntityType Strider gates for CakeWorld's custom
 * mount while retaining the vanilla item, durability and advancement roles.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FudgeSkaterRideCompatibility {
	private static final double REQUIRED_DISTANCE_SQUARED =
			50.0D * 50.0D;
	private static final Map<UUID, Vec3> HOT_FLUID_RIDE_STARTS =
			new HashMap<>();

	private FudgeSkaterRideCompatibility() {
	}

	@SubscribeEvent
	public static void onRightClickItem(
			PlayerInteractEvent.RightClickItem event) {
		if (event.getWorld().isClientSide()
				|| !(event.getPlayer()
						instanceof ServerPlayer player)
				|| !boostWithVanillaStick(
						player, event.getHand())) {
			return;
		}
		event.setCancellationResult(
				InteractionResult.SUCCESS);
		event.setCanceled(true);
	}

	public static boolean boostWithVanillaStick(
			ServerPlayer player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(Items.WARPED_FUNGUS_ON_A_STICK)
				|| !(player.getVehicle()
						instanceof FudgeSkater skater)
				|| !skater.boost()) {
			return false;
		}

		stack.hurtAndBreak(1, player,
				broken -> broken.broadcastBreakEvent(hand));
		if (stack.isEmpty()) {
			ItemStack fishingRod =
					new ItemStack(Items.FISHING_ROD);
			fishingRod.setTag(stack.getTag());
			player.setItemInHand(hand, fishingRod);
		}
		VanillaRoleAdvancements
				.creditRodeStriderRole(player);
		return true;
	}

	@SubscribeEvent
	public static void onPlayerTick(
			TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END
				&& !event.player.level.isClientSide
				&& event.player
						instanceof ServerPlayer player) {
			trackHotFluidRide(player);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(
			PlayerEvent.PlayerLoggedOutEvent event) {
		HOT_FLUID_RIDE_STARTS.remove(
				event.getPlayer().getUUID());
	}

	public static void trackHotFluidRide(
			ServerPlayer player) {
		UUID playerId = player.getUUID();
		if (player.level.dimension() != Level.OVERWORLD
				|| !(player.getVehicle()
						instanceof FudgeSkater skater)
				|| !skater.isInLava()) {
			HOT_FLUID_RIDE_STARTS.remove(playerId);
			return;
		}

		Vec3 start = HOT_FLUID_RIDE_STARTS
				.computeIfAbsent(playerId,
						ignored -> player.position());
		double deltaX = player.getX() - start.x;
		double deltaZ = player.getZ() - start.z;
		if (deltaX * deltaX + deltaZ * deltaZ
				>= REQUIRED_DISTANCE_SQUARED) {
			VanillaRoleAdvancements
					.creditRodeStriderDistanceRole(
							player);
			HOT_FLUID_RIDE_STARTS.put(
					playerId, player.position());
		}
	}
}
