package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;

/**
 * A genuine Pig-role farm animal that can snuffle up a confectionery truffle
 * without changing the block beneath it.
 */
public class TrufflePig extends Pig {
	public static final TagKey<Block> FORAGING_BLOCKS =
			TagKey.create(Registry.BLOCK_REGISTRY,
					new ResourceLocation(CakeWorld.MODID,
							"truffle_pig_foraging_blocks"));
	public static final int FORAGE_COOLDOWN_TICKS = 1200;
	private static final String FORAGE_READY_AT =
			"CakeWorldForageReadyAt";

	private long forageReadyAt;

	public TrufflePig(EntityType<? extends Pig> type, Level level) {
		super(type, level);
	}

	@Override
	public InteractionResult mobInteract(
			Player player, InteractionHand hand) {
		if (player.getItemInHand(hand).is(
				CakeWorldItems.SIMPLE_BISCUIT.get())) {
			InteractionResult result =
					tryForage(player, hand);
			if (result != InteractionResult.PASS) {
				return result;
			}
		}
		return super.mobInteract(player, hand);
	}

	public InteractionResult tryForage(
			Player player, InteractionHand hand) {
		if (isBaby()
				|| !level.getBlockState(
						blockPosition().below())
						.is(FORAGING_BLOCKS)) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		if (getForageCooldownTicks() > 0) {
			playSound(SoundEvents.PIG_AMBIENT,
					0.45F, 0.75F);
			return InteractionResult.CONSUME;
		}

		ItemStack treat = player.getItemInHand(hand);
		if (!player.getAbilities().instabuild) {
			treat.shrink(1);
		}
		forageReadyAt = level.getGameTime()
				+ FORAGE_COOLDOWN_TICKS;
		spawnAtLocation(new ItemStack(
				CakeWorldItems.COCOA_TRUFFLE.get()),
				0.4F);
		ServerLevel serverLevel = (ServerLevel)level;
		serverLevel.sendParticles(
				ParticleTypes.HAPPY_VILLAGER,
				getX(), getY() + 0.35D, getZ(),
				7, 0.35D, 0.15D, 0.35D, 0.02D);
		playSound(SoundEvents.ROOTED_DIRT_PLACE,
				0.8F, 1.2F);
		return InteractionResult.CONSUME;
	}

	public int getForageCooldownTicks() {
		return (int)Math.min(Integer.MAX_VALUE,
				Math.max(0L,
						forageReadyAt
								- level.getGameTime()));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (forageReadyAt > level.getGameTime()) {
			tag.putLong(FORAGE_READY_AT,
					forageReadyAt);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		forageReadyAt = tag.getLong(FORAGE_READY_AT);
	}

	@Override
	public Pig getBreedOffspring(ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.TRUFFLE_PIG.get().create(level);
	}
}
