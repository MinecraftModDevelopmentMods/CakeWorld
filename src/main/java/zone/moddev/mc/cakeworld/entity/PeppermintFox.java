package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import javax.annotation.Nullable;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

/**
 * A cool-mint Fox retaining vanilla trust, sleep, pounce, prey and carried-item
 * behavior, with a data-pack-extensible confectionery diet.
 */
public final class PeppermintFox extends Fox {
	public static final TagKey<Item> FOODS = ItemTags.create(
			new ResourceLocation(CakeWorld.MODID,
					"peppermint_fox_foods"));
	private static final int MINT_TICKS = 100;

	public PeppermintFox(
			EntityType<? extends Fox> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.is(FOODS) || super.isFood(stack);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		Vec3 offset = living.position().subtract(position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.12D);
		}
		living.push(horizontal.x, 0.16D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SPEED, MINT_TICKS,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, MINT_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, MINT_TICKS,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, MINT_TICKS,
				4, false, false));
		playSound(SoundEvents.FOX_BITE, 0.7F, 1.35F);
		return true;
	}

	@Override
	@Nullable
	public PeppermintFox getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		PeppermintFox child =
				CakeWorldEntities.PEPPERMINT_FOX.get().create(level);
		if (child == null) {
			return null;
		}
		Fox.Type type = getFoxType();
		if (partner instanceof Fox fox && random.nextBoolean()) {
			type = fox.getFoxType();
		}
		CompoundTag state = child.saveWithoutId(new CompoundTag());
		state.putString("Type", type.getName());
		child.load(state);
		return child;
	}

	public static boolean checkPeppermintFoxSpawnRules(
			EntityType<PeppermintFox> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return level.getBlockState(pos.below())
				.is(BlockTags.FOXES_SPAWNABLE_ON)
				&& isBrightEnoughToSpawn(level, pos);
	}
}
