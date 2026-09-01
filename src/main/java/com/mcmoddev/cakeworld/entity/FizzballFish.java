package com.mcmoddev.cakeworld.entity;

import java.util.Random;

import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * The Pufferfish role for Soda Ocean. Vanilla inflation, water movement,
 * flopping, sounds, poison on Hard and bucket persistence remain inherited.
 */
public class FizzballFish extends Pufferfish {
	public FizzballFish(EntityType<? extends Pufferfish> type,
			Level level) {
		super(type, level);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(CakeWorldItems.FIZZBALL_FISH_BUCKET.get());
	}

	@Override
	protected InteractionResult mobInteract(Player player,
			InteractionHand hand) {
		InteractionResult result = super.mobInteract(player, hand);
		if (result.consumesAction()
				&& player instanceof ServerPlayer serverPlayer
				&& player.getItemInHand(hand)
						.is(CakeWorldItems.FIZZBALL_FISH_BUCKET.get())) {
			VanillaRoleAdvancements.creditPufferfishBucketRole(
					serverPlayer);
		}
		return result;
	}

	/**
	 * Vanilla requires a literal water block above the fish. Lemonade joins
	 * the water fluid tag, so both checks deliberately use that public role.
	 */
	public static boolean checkFizzballFishSpawnRules(
			EntityType<? extends WaterAnimal> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		int seaLevel = level.getSeaLevel();
		return pos.getY() >= seaLevel - 13
				&& pos.getY() <= seaLevel
				&& level.getFluidState(pos.below()).is(FluidTags.WATER)
				&& level.getFluidState(pos.above()).is(FluidTags.WATER);
	}
}
