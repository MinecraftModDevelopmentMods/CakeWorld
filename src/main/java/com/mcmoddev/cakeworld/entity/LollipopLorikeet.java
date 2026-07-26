package com.mcmoddev.cakeworld.entity;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mcmoddev.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * CakeWorld's bright flying companion.
 *
 * Remaining a genuine Parrot preserves variants, flight, taming, sitting,
 * shoulder landing, dancing, vanilla mimicry and the deliberate non-breeding
 * contract. CakeWorld only bridges the literal item, mimic and shoulder seams.
 */
public class LollipopLorikeet extends Parrot {
	public static final TagKey<Item> TAMING_FOODS = TagKey.create(
			Registry.ITEM_REGISTRY,
			new ResourceLocation(CakeWorld.MODID,
					"lollipop_lorikeet_taming_foods"));

	private static final Map<ResourceLocation, SoundEvent>
			CAKEWORLD_MIMIC_SOUNDS = Map.ofEntries(
					mimic("cinnamon_spark",
							SoundEvents.PARROT_IMITATE_BLAZE),
					mimic("deep_liquorice_weaver",
							SoundEvents.PARROT_IMITATE_SPIDER),
					mimic("pop_rock_popper",
							SoundEvents.PARROT_IMITATE_CREEPER),
					mimic("wafer_wraith",
							SoundEvents.PARROT_IMITATE_PHANTOM),
					mimic("soggy_biscuit",
							SoundEvents.PARROT_IMITATE_DROWNED),
					mimic("grand_gumball_guardian",
							SoundEvents.PARROT_IMITATE_ELDER_GUARDIAN),
					mimic("sugar_mite",
							SoundEvents.PARROT_IMITATE_ENDERMITE),
					mimic("sour_sorcerer",
							SoundEvents.PARROT_IMITATE_EVOKER),
					mimic("sour_sprite",
							SoundEvents.PARROT_IMITATE_VEX),
					mimic("mallow_floater",
							SoundEvents.PARROT_IMITATE_GHAST),
					mimic("gumball_guardian",
							SoundEvents.PARROT_IMITATE_GUARDIAN),
					mimic("fudge_boar",
							SoundEvents.PARROT_IMITATE_HOGLIN),
					mimic("fudge_folk",
							SoundEvents.PARROT_IMITATE_PIGLIN),
					mimic("fudge_brute",
							SoundEvents
									.PARROT_IMITATE_PIGLIN_BRUTE),
					mimic("biscuit_bandit",
							SoundEvents
									.PARROT_IMITATE_PILLAGER),
					mimic("rolling_pin_raider",
							SoundEvents
									.PARROT_IMITATE_VINDICATOR),
					mimic("bitter_baker",
							SoundEvents
									.PARROT_IMITATE_WITCH),
					mimic("burnt_sugar_tempest",
							SoundEvents
									.PARROT_IMITATE_WITHER),
					mimic("gingerbread_stomper",
							SoundEvents
									.PARROT_IMITATE_RAVAGER),
					mimic("dried_crumbler",
							SoundEvents.PARROT_IMITATE_HUSK),
					mimic("mirage_confectioner",
							SoundEvents.PARROT_IMITATE_ILLUSIONER),
					mimic("hot_fudge_blob",
							SoundEvents.PARROT_IMITATE_MAGMA_CUBE),
					mimic("macaron_clam",
							SoundEvents.PARROT_IMITATE_SHULKER),
					mimic("crumb_mite",
							SoundEvents.PARROT_IMITATE_SILVERFISH),
					mimic("candy_cane_archer",
							SoundEvents.PARROT_IMITATE_SKELETON),
					mimic("frosted_archer",
							SoundEvents.PARROT_IMITATE_STRAY),
					mimic("jelly_blob",
							SoundEvents.PARROT_IMITATE_SLIME),
					mimic("liquorice_weaver",
							SoundEvents.PARROT_IMITATE_SPIDER),
					mimic("stale_crumbler",
							SoundEvents.PARROT_IMITATE_ZOMBIE));

	public LollipopLorikeet(
			EntityType<? extends Parrot> type, Level level) {
		super(type, level);
	}

	@Override
	public void aiStep() {
		if (level.random.nextInt(400) == 0) {
			imitateNearbyCakeWorldMobs(level, this);
		}
		super.aiStep();
	}

	@Override
	public InteractionResult mobInteract(
			Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!isTame() && stack.is(TAMING_FOODS)) {
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
			if (!isSilent()) {
				level.playSound(null, getX(), getY(), getZ(),
						SoundEvents.PARROT_EAT,
						getSoundSource(), 1.0F,
						1.0F + (random.nextFloat()
								- random.nextFloat())
								* 0.2F);
			}
			if (!level.isClientSide) {
				if (random.nextInt(10) == 0
						&& !ForgeEventFactory.onAnimalTame(
								this, player)) {
					tame(player);
					level.broadcastEntityEvent(this,
							(byte)7);
				} else {
					level.broadcastEntityEvent(this,
							(byte)6);
				}
			}
			return InteractionResult.sidedSuccess(
					level.isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.doHurtTarget(target);
		}
		if (!(target instanceof LivingEntity living)) {
			return false;
		}

		playSound(SoundEvents.PARROT_EAT, 0.6F, getVoicePitch());
		setTarget(null);
		Vec3 offset = living.position().subtract(position());
		Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
		if (horizontal.lengthSqr() > 1.0E-4D) {
			horizontal = horizontal.normalize().scale(0.1D);
		}
		living.push(horizontal.x, 0.12D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 60,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 60,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 60,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 60,
				4, false, false));
		return true;
	}

	public static boolean checkLollipopLorikeetSpawnRules(
			EntityType<LollipopLorikeet> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, java.util.Random random) {
		return level.getBlockState(pos.below())
				.is(BlockTags.PARROTS_SPAWNABLE_ON)
				&& isBrightEnoughToSpawn(level, pos);
	}

	public static boolean imitateNearbyCakeWorldMobs(
			Level level, Entity source) {
		if (!source.isAlive() || source.isSilent()
				|| level.random.nextInt(2) != 0) {
			return false;
		}
		List<Mob> candidates = level.getEntitiesOfClass(
				Mob.class,
				source.getBoundingBox().inflate(20.0D),
				mob -> getCakeWorldImitatedSound(
						mob.getType()) != null);
		if (candidates.isEmpty()) {
			return false;
		}
		Mob target = candidates.get(
				level.random.nextInt(candidates.size()));
		if (target.isSilent()) {
			return false;
		}
		SoundEvent sound = getCakeWorldImitatedSound(
				target.getType());
		if (sound == null) {
			return false;
		}
		level.playSound(null, source.getX(), source.getY(),
				source.getZ(), sound, source.getSoundSource(),
				0.7F, Parrot.getPitch(level.random));
		return true;
	}

	@Nullable
	public static SoundEvent getCakeWorldImitatedSound(
			EntityType<?> type) {
		return CAKEWORLD_MIMIC_SOUNDS.get(
				Registry.ENTITY_TYPE.getKey(type));
	}

	public static boolean isShoulderTag(
			@Nullable CompoundTag tag) {
		return tag != null && (CakeWorld.MODID
				+ ":lollipop_lorikeet")
						.equals(tag.getString("id"));
	}

	public static void playShoulderAmbient(
			Player player, CompoundTag tag) {
		if (!isShoulderTag(tag)
				|| tag.getBoolean("Silent")
				|| player.level.random.nextInt(200) != 0) {
			return;
		}
		if (!Parrot.imitateNearbyMobs(
				player.level, player)
				&& !imitateNearbyCakeWorldMobs(
						player.level, player)) {
			player.level.playSound(null,
					player.getX(), player.getY(),
					player.getZ(),
					Parrot.getAmbient(player.level,
							player.level.random),
					player.getSoundSource(), 1.0F,
					Parrot.getPitch(
							player.level.random));
		}
	}

	private static Map.Entry<ResourceLocation, SoundEvent>
			mimic(String entity, SoundEvent sound) {
		return Map.entry(new ResourceLocation(
				CakeWorld.MODID, entity), sound);
	}
}
