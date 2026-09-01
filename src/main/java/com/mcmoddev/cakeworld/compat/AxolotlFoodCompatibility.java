package com.mcmoddev.cakeworld.compat;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Preserves vanilla Axolotl's filled-bucket remainder for the CakeWorld
 * Tropical Fish role. The item tag supplies the food predicate; this bridge
 * only repairs the literal vanilla-item branch in Axolotl.usePlayerItem.
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID,
		bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class AxolotlFoodCompatibility {
	private AxolotlFoodCompatibility() {
	}

	@SubscribeEvent
	public static void onEntityInteract(
			PlayerInteractEvent.EntityInteract event) {
		Player player = event.getPlayer();
		InteractionHand hand = event.getHand();
		ItemStack original = player.getItemInHand(hand);
		if (!(event.getTarget() instanceof Axolotl axolotl)
				|| !original.is(CakeWorldItems
						.JELLYBEAN_FISH_BUCKET.get())
				|| !(event.getWorld()
						instanceof ServerLevel level)) {
			return;
		}

		int ageBefore = axolotl.getAge();
		boolean loveBefore = axolotl.isInLove();
		level.getServer().tell(new TickTask(
				level.getServer().getTickCount() + 1,
				() -> {
					boolean consumed = original.isEmpty()
							|| (!loveBefore
									&& axolotl
											.isInLove())
							|| axolotl.getAge()
									> ageBefore;
					if (!consumed) {
						return;
					}
					ItemStack current =
							player.getItemInHand(hand);
					if (current.isEmpty()
							|| current.is(
									CakeWorldItems
											.JELLYBEAN_FISH_BUCKET
											.get())) {
						player.setItemInHand(hand,
								new ItemStack(
										CakeWorldFluids
												.LEMONADE_BUCKET
												.get()));
					}
				}));
	}
}
