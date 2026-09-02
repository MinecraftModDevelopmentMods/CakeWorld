package zone.moddev.mc.cakeworld.block;

import zone.moddev.mc.cakeworld.cookbook.SharedCookbookLibrary;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A community lore shelf with a strict read/progression boundary.
 */
public final class CookbookLibraryBlock extends Block {
	public CookbookLibraryBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(level instanceof ServerLevel serverLevel)
				|| !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		SharedCookbookLibrary library =
				SharedCookbookLibrary.get(serverLevel);
		if (player.isShiftKeyDown()) {
			library.publishNext(serverPlayer).ifPresentOrElse(
					page -> serverPlayer.displayClientMessage(
							new TranslatableComponent(
									"message.cakeworld.library.published",
									page.page().toString()),
							false),
					() -> serverPlayer.displayClientMessage(
							new TranslatableComponent(
									"message.cakeworld.library.nothing_to_publish"),
							false));
		} else {
			library.samplePage().ifPresentOrElse(
					page -> serverPlayer.displayClientMessage(
							new TranslatableComponent(
									"message.cakeworld.library.read",
									page.page().toString(),
									library.pageCount()),
							false),
					() -> serverPlayer.displayClientMessage(
							new TranslatableComponent(
									"message.cakeworld.library.empty"),
							false));
		}
		return InteractionResult.CONSUME;
	}
}
