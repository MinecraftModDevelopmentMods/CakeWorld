package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.entity.TrufflePig;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Preserves the vanilla Carrot on a Stick boost for the custom Pig type.
 *
 * FoodOnAStickItem requires exact EntityType.PIG identity before invoking the
 * ItemSteerable contract, even though inherited steering already works.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TrufflePigCarrotOnAStickBridge {
	private static final int BOOST_DURABILITY_COST = 7;

	private TrufflePigCarrotOnAStickBridge() {
	}

	@SubscribeEvent
	public static void onRightClickItem(
			PlayerInteractEvent.RightClickItem event) {
		if (event.getWorld().isClientSide()
				|| !event.getItemStack()
						.is(Items.CARROT_ON_A_STICK)
				|| !(event.getPlayer().getVehicle()
						instanceof TrufflePig pig)
				|| !pig.boost()) {
			return;
		}

		ItemStack stack = event.getItemStack();
		stack.hurtAndBreak(BOOST_DURABILITY_COST,
				event.getPlayer(), player ->
						player.broadcastBreakEvent(
								event.getHand()));
		if (stack.isEmpty()) {
			ItemStack fishingRod =
					new ItemStack(Items.FISHING_ROD);
			fishingRod.setTag(stack.getTag());
			event.getPlayer().setItemInHand(
					event.getHand(), fishingRod);
		}
		event.setCancellationResult(
				InteractionResult.SUCCESS);
		event.setCanceled(true);
	}
}
