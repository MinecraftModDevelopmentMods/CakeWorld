package zone.moddev.mc.cakeworld.block;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.cookbook.CookbookProgress;
import zone.moddev.mc.cakeworld.cookbook.DiscoveryType;
import zone.moddev.mc.cakeworld.network.CakeWorldNetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class CookbookKioskBlock extends Block {
	private static final ResourceLocation KIOSK =
			new ResourceLocation(CakeWorld.MODID, "cookbook_kiosk");

	public CookbookKioskBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			CookbookProgress.recoverBook(serverPlayer);
			CookbookProgress.discover(serverPlayer, DiscoveryType.FINDING, KIOSK);
			CakeWorldNetwork.openCookbook(serverPlayer);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
