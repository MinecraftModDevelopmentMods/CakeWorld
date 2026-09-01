package com.mcmoddev.cakeworld.block;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A no-fuel cooling station backed by standard stonecutting recipes. A public
 * input tag prevents unrelated stonecutting recipes becoming kitchen recipes.
 */
public final class CoolingRackBlock extends Block {
	public static final TagKey<Item> INPUTS = TagKey.create(
			Registry.ITEM_REGISTRY,
			new ResourceLocation("forge", "cooling_rack_inputs"));

	public CoolingRackBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		ItemStack input = player.getMainHandItem();
		if (input.isEmpty() || !input.is(INPUTS)) {
			return InteractionResult.PASS;
		}
		ItemStack singleInput = input.copy();
		singleInput.setCount(1);
		SimpleContainer container = new SimpleContainer(singleInput);
		Optional<StonecutterRecipe> recipe = level.getRecipeManager()
				.getRecipeFor(RecipeType.STONECUTTING, container, level);
		if (recipe.isEmpty()) {
			return InteractionResult.PASS;
		}
		ItemStack result = recipe.orElseThrow().assemble(container);
		if (result.isEmpty() || !result.isEdible()) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		if (!player.getAbilities().instabuild) {
			ItemStack remainder = input.hasContainerItem()
					? input.getContainerItem() : ItemStack.EMPTY;
			input.shrink(1);
			if (!remainder.isEmpty()) {
				if (input.isEmpty()) {
					player.setItemInHand(hand, remainder);
				} else if (!player.getInventory().add(remainder)) {
					player.drop(remainder, false);
				}
			}
		}
		if (!player.getInventory().add(result.copy())) {
			player.drop(result.copy(), false);
		}
		level.playSound(null, pos, SoundEvents.SNOW_PLACE,
				SoundSource.BLOCKS, 0.8F, 1.2F);
		return InteractionResult.CONSUME;
	}
}
