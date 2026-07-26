package com.mcmoddev.cakeworld.entity;

import java.util.function.Supplier;

import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine roaming Wandering-Trader role.
 *
 * <p>The superclass remains authoritative for vanilla and Forge-extensible
 * trade pools, trading, invisibility and milk scheduling, wandering,
 * avoidance, sounds and despawn. CakeWorld adds one rotating snack and a
 * deliberately scarce Sprinkle Seed offer without replacing any compatible
 * offer supplied by vanilla or another mod.</p>
 */
public class TravellingConfectioner extends WanderingTrader {
	private static final int TRADE_XP = 1;
	private static final float PRICE_MULTIPLIER = 0.05F;

	public TravellingConfectioner(
			EntityType<? extends WanderingTrader> type,
			Level level) {
		super(type, level);
	}

	@Override
	protected void updateTrades() {
		super.updateTrades();
		addCakeWorldOffersIfMissing();
	}

	public void addCakeWorldOffersIfMissing() {
		MerchantOffers offers = getOffers();
		boolean hasSnack = offers.stream()
				.anyMatch(offer -> isCakeWorldSnack(
						offer.getResult()));
		if (!hasSnack) {
			VillagerTrades.ItemListing[] snacks = {
					selling(CakeWorldItems
							.CHOCOLATE_SPONGE_SLICE,
							1, 3, 12),
					selling(CakeWorldItems
							.SIMPLE_BISCUIT,
							1, 4, 12),
					selling(CakeWorldItems
							.LEMONADE_BOTTLE,
							2, 1, 8),
					selling(CakeWorldItems
							.SHERBET_FIZZ,
							3, 1, 6),
					selling(CakeWorldItems
							.COMFORT_COCOA,
							2, 1, 8),
					selling(CakeWorldItems
							.MINT_WAFER,
							2, 2, 8)
			};
			addOffersFromItemListings(
					offers, snacks, 1);
		}
		boolean hasSeeds = offers.stream()
				.anyMatch(offer -> offer.getResult()
						.is(CakeWorldItems
								.SPRINKLE_SEEDS
								.get()));
		if (!hasSeeds) {
			MerchantOffer seeds = selling(
					CakeWorldItems.SPRINKLE_SEEDS,
					5, 1, 2)
							.getOffer(this, random);
			if (seeds != null) {
				offers.add(seeds);
			}
		}
	}

	private static boolean isCakeWorldSnack(
			ItemStack stack) {
		return stack.is(CakeWorldItems
				.CHOCOLATE_SPONGE_SLICE.get())
				|| stack.is(CakeWorldItems
						.SIMPLE_BISCUIT.get())
				|| stack.is(CakeWorldItems
						.LEMONADE_BOTTLE.get())
				|| stack.is(CakeWorldItems
						.SHERBET_FIZZ.get())
				|| stack.is(CakeWorldItems
						.COMFORT_COCOA.get())
				|| stack.is(CakeWorldItems
						.MINT_WAFER.get());
	}

	private static VillagerTrades.ItemListing selling(
			Supplier<? extends ItemLike> result,
			int emeraldCost,
			int resultCount,
			int maxUses) {
		return (Entity trader, java.util.Random random) ->
				new MerchantOffer(
						new ItemStack(Items.EMERALD,
								emeraldCost),
						new ItemStack(
								result.get()
										.asItem(),
								resultCount),
						maxUses, TRADE_XP,
						PRICE_MULTIPLIER);
	}
}
