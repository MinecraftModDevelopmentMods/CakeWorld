package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;
import zone.moddev.mc.cakeworld.init.CakeWorldFluids;
import zone.moddev.mc.cakeworld.init.CakeWorldItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A friendly CakeWorld aquatic helper.
 *
 * Below Hard its attack is visible crowd control rather than health damage.
 * Hard deliberately restores the complete axolotl combat role.
 */
public final class Jellylotl extends Axolotl {
	public Jellylotl(EntityType<? extends Axolotl> type, Level level) {
		super(type, level);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level,
			AgeableMob partner) {
		Jellylotl child = CakeWorldEntities.JELLYLOTL.get().create(level);
		if (child != null) {
			Variant variant;
			if (random.nextInt(RARE_VARIANT_CHANCE) == 0) {
				variant = Variant.getRareSpawnVariant(random);
			} else {
				variant = random.nextBoolean() ? getVariant()
						: ((Axolotl) partner).getVariant();
			}
			CompoundTag flavour = new CompoundTag();
			flavour.putInt(VARIANT_TAG, variant.getId());
			child.loadFromBucketTag(flavour);
			child.setPersistenceRequired();
		}
		return child;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (target instanceof LivingEntity livingTarget) {
			livingTarget.addEffect(new MobEffectInstance(
					MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
			livingTarget.addEffect(new MobEffectInstance(
					MobEffects.GLOWING, 80));
			playSound(SoundEvents.AXOLOTL_ATTACK, 1.0F, 1.2F);
			return true;
		}
		return false;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(CakeWorldItems.JELLYLOTL_BUCKET.get());
	}

	@Override
	protected void usePlayerItem(Player player,
			InteractionHand hand, ItemStack stack) {
		if (stack.is(CakeWorldItems
				.JELLYBEAN_FISH_BUCKET.get())) {
			player.setItemInHand(hand,
					new ItemStack(CakeWorldFluids
							.LEMONADE_BUCKET.get()));
			return;
		}
		super.usePlayerItem(player, hand, stack);
	}
}
