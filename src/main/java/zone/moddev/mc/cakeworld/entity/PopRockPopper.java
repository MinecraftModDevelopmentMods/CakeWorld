package zone.moddev.mc.cakeworld.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;

/**
 * A Creeper-role creature that pops harmlessly below Hard difficulty.
 *
 * <p>The safe fuse is deliberately separate from Creeper's private explosion
 * fuse. Easy and Normal therefore never call {@link Level#explode}; Hard uses
 * the unmodified Creeper tick path.</p>
 */
public final class PopRockPopper extends Creeper {
	private static final int DEFAULT_SAFE_FUSE = 30;
	private static final int LANDING_PROTECTION_TICKS = 120;
	private static final String SAFE_SWELL_TAG = "CakeWorldSafeSwell";
	private static final String SAFE_IGNITED_TAG = "CakeWorldSafeIgnited";

	private int oldSafeSwell;
	private int safeSwell;
	private int safeMaxSwell = DEFAULT_SAFE_FUSE;
	private boolean safeIgnited;
	private boolean suppressVanillaIgnition;

	public PopRockPopper(EntityType<? extends Creeper> type, Level level) {
		super(type, level);
	}

	@Override
	public void tick() {
		if (level.getDifficulty() == Difficulty.HARD) {
			super.tick();
			return;
		}

		if (isAlive()) {
			oldSafeSwell = safeSwell;
			/*
			 * isIgnited also reads Creeper's synced ignition bit, so a client
			 * animates a player-lit Popper even though safeIgnited itself is
			 * server-owned state.
			 */
			int direction = isIgnited() ? 1 : getSwellDir();
			if (direction > 0 && safeSwell == 0) {
				playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.7F);
				gameEvent(GameEvent.PRIME_FUSE);
			}
			safeSwell = Mth.clamp(safeSwell + direction, 0, safeMaxSwell);
			if (safeSwell >= safeMaxSwell) {
				if (!level.isClientSide) {
					safePop();
				}
				return;
			}
		}

		/*
		 * Creeper's explosion method and fuse fields are private. Holding its
		 * own fuse at zero is the hard boundary which guarantees that the
		 * lower-difficulty path cannot reach Level.explode.
		 */
		setSwellDir(-1);
		suppressVanillaIgnition = true;
		try {
			super.tick();
		} finally {
			suppressVanillaIgnition = false;
		}
	}

	@Override
	public float getSwelling(float partialTick) {
		if (level.getDifficulty() == Difficulty.HARD) {
			return super.getSwelling(partialTick);
		}
		return Mth.lerp(partialTick, (float) oldSafeSwell,
				(float) safeSwell) / (float) (safeMaxSwell - 2);
	}

	@Override
	public boolean causeFallDamage(float distance, float multiplier,
			DamageSource source) {
		boolean result = super.causeFallDamage(distance, multiplier, source);
		if (level.getDifficulty() != Difficulty.HARD) {
			safeSwell = Math.min(safeMaxSwell - 5,
					safeSwell + (int) (distance * 1.5F));
		}
		return result;
	}

	@Override
	public boolean isIgnited() {
		if (suppressVanillaIgnition) {
			return false;
		}
		return safeIgnited || super.isIgnited();
	}

	@Override
	public void ignite() {
		safeIgnited = true;
		/*
		 * Keep vanilla ignition too, so changing the world to Hard before the
		 * pop correctly opts into genuine Creeper peril.
		 */
		super.ignite();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt(SAFE_SWELL_TAG, safeSwell);
		tag.putBoolean(SAFE_IGNITED_TAG, safeIgnited);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Fuse", 99)) {
			safeMaxSwell = Math.max(1, tag.getShort("Fuse"));
		}
		safeSwell = Mth.clamp(tag.getInt(SAFE_SWELL_TAG), 0, safeMaxSwell);
		safeIgnited = tag.getBoolean(SAFE_IGNITED_TAG)
				|| tag.getBoolean("ignited");
	}

	private void safePop() {
		double radius = isPowered() ? 6.0D : 4.0D;
		double horizontalPush = isPowered() ? 0.65D : 0.45D;
		AABB area = getBoundingBox().inflate(radius);
		for (LivingEntity target : level.getEntitiesOfClass(
				LivingEntity.class, area, entity -> entity != this)) {
			Vec3 offset = target.position().subtract(position());
			Vec3 horizontal = new Vec3(offset.x, 0.0D, offset.z);
			if (horizontal.lengthSqr() > 1.0E-4D) {
				horizontal = horizontal.normalize().scale(horizontalPush);
			}
			target.push(horizontal.x, 0.25D, horizontal.z);
			target.fallDistance = 0.0F;
			target.clearFire();
			target.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
					LANDING_PROTECTION_TICKS, 0, false, true));
			target.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,
					LANDING_PROTECTION_TICKS, 0, false, false));
			target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,
					LANDING_PROTECTION_TICKS, 4, false, false));
		}

		playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 1.0F,
				1.1F + random.nextFloat() * 0.3F);
		gameEvent(GameEvent.EXPLODE);
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.FIREWORK,
					getX(), getY() + 0.8D, getZ(), isPowered() ? 48 : 28,
					1.0D, 1.0D, 1.0D, 0.08D);
			serverLevel.sendParticles(ParticleTypes.NOTE,
					getX(), getY() + 0.8D, getZ(), isPowered() ? 30 : 18,
					1.2D, 1.2D, 1.2D, 0.0D);
		}
		discard();
	}
}
