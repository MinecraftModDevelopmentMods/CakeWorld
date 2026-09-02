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
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;

/**
 * CakeWorld's complete Tropical Fish role.
 *
 * <p>The vanilla packed pattern and colour variant, common variants, school
 * generation, movement, sounds, bucket NBT and persistence all remain
 * inherited. Only CakeWorld's bucket identity, advancement bridge and
 * Lemonade-compatible spawn seam differ.</p>
 */
public class JellybeanFish extends TropicalFish {
	public JellybeanFish(EntityType<? extends TropicalFish> type,
			Level level) {
		super(type, level);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(
				CakeWorldItems.JELLYBEAN_FISH_BUCKET.get());
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
								.JELLYBEAN_FISH_BUCKET
								.get())) {
			VanillaRoleAdvancements
					.creditTropicalFishBucketRole(
							serverPlayer);
		}
		return result;
	}

	/**
	 * Vanilla accepts Lush Caves at any height and otherwise restricts the
	 * fish to the thirteen blocks below sea level. Both adjacent fluid checks
	 * deliberately use the public water role so Lemonade works without
	 * changing either positional rule.
	 */
	public static boolean checkJellybeanFishSpawnRules(
			EntityType<? extends TropicalFish> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		boolean waterColumn =
				level.getFluidState(pos.below())
						.is(FluidTags.WATER)
				&& level.getFluidState(pos.above())
						.is(FluidTags.WATER);
		if (!waterColumn) {
			return false;
		}
		if (level.getBiome(pos).is(Biomes.LUSH_CAVES)) {
			return true;
		}
		int seaLevel = level.getSeaLevel();
		return pos.getY() >= seaLevel - 13
				&& pos.getY() <= seaLevel;
	}
}
