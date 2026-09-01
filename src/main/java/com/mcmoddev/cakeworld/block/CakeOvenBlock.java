package com.mcmoddev.cakeworld.block;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A deliberately simple, single-serving starter oven.
 *
 * <p>It accepts standard smelting recipes whose result is food, vanilla
 * furnace fuels, and positive Forge per-stack burn overrides. This gives
 * datapacks and integrated mods a normal recipe/tag seam without introducing
 * an early-game machine system.</p>
 */
public final class CakeOvenBlock extends Block {
	public CakeOvenBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		ItemStack input = player.getMainHandItem();
		ItemStack fuel = player.getOffhandItem();
		if (input.isEmpty()) {
			return InteractionResult.PASS;
		}

		ItemStack singleInput = input.copy();
		singleInput.setCount(1);
		SimpleContainer container = new SimpleContainer(singleInput);
		Optional<SmeltingRecipe> recipe = level.getRecipeManager()
				.getRecipeFor(RecipeType.SMELTING, container, level);
		if (recipe.isEmpty()) {
			return InteractionResult.PASS;
		}
		ItemStack result = recipe.orElseThrow().assemble(container);
		if (result.isEmpty() || !result.isEdible()
				|| (!player.getAbilities().instabuild
						&& !isFuel(fuel))) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		if (!player.getAbilities().instabuild) {
			input.shrink(1);
			consumeOneFuel(player, fuel);
		}
		giveResult(player, hand, input, result.copy());
		level.playSound(null, pos, SoundEvents.FURNACE_FIRE_CRACKLE,
				SoundSource.BLOCKS, 0.8F, 1.1F);
		return InteractionResult.CONSUME;
	}

	private static boolean isFuel(ItemStack fuel) {
		int forgeBurnTime = fuel.getBurnTime(RecipeType.SMELTING);
		return forgeBurnTime > 0
				|| (forgeBurnTime < 0
						&& AbstractFurnaceBlockEntity.isFuel(fuel));
	}

	private static void consumeOneFuel(Player player, ItemStack fuel) {
		ItemStack remainder = fuel.hasContainerItem()
				? fuel.getContainerItem() : ItemStack.EMPTY;
		fuel.shrink(1);
		if (remainder.isEmpty()) {
			return;
		}
		if (fuel.isEmpty()) {
			player.setItemInHand(InteractionHand.OFF_HAND, remainder);
		} else if (!player.getInventory().add(remainder)) {
			player.drop(remainder, false);
		}
	}

	private static void giveResult(Player player, InteractionHand hand,
			ItemStack input, ItemStack result) {
		if (input.isEmpty()) {
			player.setItemInHand(hand, result);
		} else if (!player.getInventory().add(result)) {
			player.drop(result, false);
		}
	}
}
