package zone.moddev.mc.cakeworld.block;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

/**
 * A deliberately slow environmental hazard: hot enough to hurt, but never to
 * ignite the player or destroy loose possessions. Its glow, smoke, pops and
 * contact sizzle warn players before the next half-heart of damage.
 */
public final class HotFudgeLiquidBlock extends CakeLiquidBlock {
	public static final float CONTACT_DAMAGE = 1.0F;
	public static final int WARNING_LIGHT = 8;

	public HotFudgeLiquidBlock(Supplier<? extends FlowingFluid> fluid,
			BlockBehaviour.Properties properties) {
		super(fluid, properties);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos,
			Entity entity) {
		super.entityInside(state, level, pos, entity);
		if (level.isClientSide || !(entity instanceof LivingEntity living)
				|| living.fireImmune()
				|| living.hasEffect(MobEffects.FIRE_RESISTANCE)
				|| !living.hurt(DamageSource.HOT_FLOOR, CONTACT_DAMAGE)) {
			return;
		}

		level.playSound(null, pos, SoundEvents.GENERIC_BURN,
				SoundSource.BLOCKS, 0.55F,
				0.85F + level.random.nextFloat() * 0.15F);
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ParticleTypes.SMOKE,
					living.getX(), living.getY() + living.getBbHeight() * 0.5D,
					living.getZ(), 4, 0.18D, 0.12D, 0.18D, 0.01D);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos,
			Random random) {
		super.animateTick(state, level, pos, random);
		if (!level.getBlockState(pos.above()).isAir()) {
			return;
		}
		if (random.nextInt(8) == 0) {
			level.addParticle(ParticleTypes.SMOKE,
					pos.getX() + random.nextDouble(), pos.getY() + 1.02D,
					pos.getZ() + random.nextDouble(),
					0.0D, 0.025D, 0.0D);
		}
		if (random.nextInt(100) == 0) {
			level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.0D,
					pos.getZ() + 0.5D, SoundEvents.LAVA_POP,
					SoundSource.BLOCKS, 0.25F,
					0.8F + random.nextFloat() * 0.2F, false);
		}
	}
}
