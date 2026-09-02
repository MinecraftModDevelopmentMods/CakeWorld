package zone.moddev.mc.cakeworld.block;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A measured drink dispenser backed by ordinary crafting recipes.
 *
 * <p>One tagged fountain input in the main hand and three glass bottles in the
 * offhand form a normal 2x2 crafting grid. This keeps drink additions
 * data-driven while the tag prevents unrelated four-ingredient recipes being
 * exposed through the fountain.</p>
 */
public final class SodaFountainBlock extends Block {
	public static final TagKey<Item> INPUTS = TagKey.create(
			Registry.ITEM_REGISTRY,
			new ResourceLocation("forge", "soda_fountain_inputs"));

	public SodaFountainBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		ItemStack input = player.getMainHandItem();
		ItemStack bottles = player.getOffhandItem();
		if (input.isEmpty() || !input.is(INPUTS)
				|| !bottles.is(Items.GLASS_BOTTLE)
				|| bottles.getCount() < 3) {
			return InteractionResult.PASS;
		}

		CraftingContainer grid = createGrid();
		ItemStack singleInput = input.copy();
		singleInput.setCount(1);
		grid.setItem(0, singleInput);
		for (int slot = 1; slot < 4; slot++) {
			grid.setItem(slot, new ItemStack(Items.GLASS_BOTTLE));
		}
		Optional<CraftingRecipe> recipe = level.getRecipeManager()
				.getRecipeFor(RecipeType.CRAFTING, grid, level);
		if (recipe.isEmpty()) {
			return InteractionResult.PASS;
		}
		ItemStack result = recipe.orElseThrow().assemble(grid);
		ItemStack remainder = recipe.orElseThrow()
				.getRemainingItems(grid).get(0);
		if (result.isEmpty() || !result.isEdible()
				|| result.getCount() != 3 || remainder.isEmpty()) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		if (!player.getAbilities().instabuild) {
			input.shrink(1);
			bottles.shrink(3);
			returnContainer(player, hand, input, remainder);
		}
		if (!player.getInventory().add(result.copy())) {
			player.drop(result.copy(), false);
		}
		level.playSound(null, pos, SoundEvents.BOTTLE_FILL,
				SoundSource.BLOCKS, 0.8F, 1.25F);
		return InteractionResult.CONSUME;
	}

	private static CraftingContainer createGrid() {
		AbstractContainerMenu menu = new AbstractContainerMenu(null, -1) {
			@Override
			public boolean stillValid(Player player) {
				return true;
			}
		};
		return new CraftingContainer(menu, 2, 2);
	}

	private static void returnContainer(Player player, InteractionHand hand,
			ItemStack input, ItemStack remainder) {
		if (input.isEmpty()) {
			player.setItemInHand(hand, remainder.copy());
		} else if (!player.getInventory().add(remainder.copy())) {
			player.drop(remainder.copy(), false);
		}
	}
}
