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
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A fuelled candy station backed by ordinary smoking recipes.
 *
 * <p>The public input tag keeps meat and other unrelated smoker recipes out of
 * the cooker while allowing data packs and integrated mods to add sweets
 * without a CakeWorld-specific recipe serializer.</p>
 */
public final class CandyCookerBlock extends Block {
	public static final TagKey<Item> INPUTS = TagKey.create(
			Registry.ITEM_REGISTRY,
			new ResourceLocation("forge", "candy_cooker_inputs"));

	public CandyCookerBlock(BlockBehaviour.Properties properties) {
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
		if (input.isEmpty() || !input.is(INPUTS)) {
			return InteractionResult.PASS;
		}

		ItemStack singleInput = input.copy();
		singleInput.setCount(1);
		SimpleContainer container = new SimpleContainer(singleInput);
		Optional<SmokingRecipe> recipe = level.getRecipeManager()
				.getRecipeFor(RecipeType.SMOKING, container, level);
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
			ItemStack remainder = input.hasContainerItem()
					? input.getContainerItem() : ItemStack.EMPTY;
			input.shrink(1);
			returnInputContainer(player, hand, input, remainder);
			consumeOneFuel(player, fuel);
		}
		giveResult(player, hand, input, result.copy());
		level.playSound(null, pos, SoundEvents.LAVA_POP,
				SoundSource.BLOCKS, 0.8F, 1.3F);
		return InteractionResult.CONSUME;
	}

	private static boolean isFuel(ItemStack fuel) {
		int forgeBurnTime = fuel.getBurnTime(RecipeType.SMOKING);
		return forgeBurnTime > 0
				|| (forgeBurnTime < 0
						&& AbstractFurnaceBlockEntity.isFuel(fuel));
	}

	private static void returnInputContainer(Player player,
			InteractionHand hand, ItemStack input, ItemStack remainder) {
		if (remainder.isEmpty()) {
			return;
		}
		if (input.isEmpty()) {
			player.setItemInHand(hand, remainder);
		} else if (!player.getInventory().add(remainder)) {
			player.drop(remainder, false);
		}
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
		if (input.isEmpty() && player.getItemInHand(hand).isEmpty()) {
			player.setItemInHand(hand, result);
		} else if (!player.getInventory().add(result)) {
			player.drop(result, false);
		}
	}
}
