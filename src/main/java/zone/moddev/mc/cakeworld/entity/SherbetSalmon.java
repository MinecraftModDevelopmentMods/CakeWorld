package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import zone.moddev.mc.cakeworld.compat.VanillaRoleAdvancements;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * CakeWorld's cool-water Salmon role.
 *
 * <p>Vanilla schooling, swimming, flopping, air, persistence and sounds remain
 * inherited. Only the bucket identity, advancement bridge and literal-water
 * spawn check need CakeWorld-owned equivalents.</p>
 */
public class SherbetSalmon extends Salmon {
	public SherbetSalmon(
			EntityType<? extends Salmon> type, Level level) {
		super(type, level);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(
				CakeWorldItems.SHERBET_SALMON_BUCKET.get());
	}

	@Override
	protected InteractionResult mobInteract(
			Player player, InteractionHand hand) {
		InteractionResult result =
				super.mobInteract(player, hand);
		if (result.consumesAction()
				&& player instanceof ServerPlayer serverPlayer
				&& player.getItemInHand(hand).is(
						CakeWorldItems
								.SHERBET_SALMON_BUCKET
								.get())) {
			VanillaRoleAdvancements
					.creditSalmonBucketRole(serverPlayer);
		}
		return result;
	}

	/**
	 * Vanilla requires a literal water block above surface fish. Lemonade is
	 * deliberately water-tag compatible, so both adjacent checks use the
	 * public fluid role while retaining vanilla's exact depth band.
	 */
	public static boolean checkSherbetSalmonSpawnRules(
			EntityType<? extends WaterAnimal> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		int seaLevel = level.getSeaLevel();
		return pos.getY() >= seaLevel - 13
				&& pos.getY() <= seaLevel
				&& level.getFluidState(pos.below())
						.is(FluidTags.WATER)
				&& level.getFluidState(pos.above())
						.is(FluidTags.WATER);
	}
}
