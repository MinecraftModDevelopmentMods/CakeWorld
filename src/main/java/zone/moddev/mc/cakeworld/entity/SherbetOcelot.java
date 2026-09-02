package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import javax.annotation.Nullable;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's shy Jungle hunter.
 *
 * Remaining a genuine Ocelot preserves fish temptation, trust, player
 * avoidance, stalking poses, chicken and baby-turtle prey, and despawning.
 */
public class SherbetOcelot extends Ocelot {
	public static final TagKey<Block> SPAWNABLE_ON = TagKey.create(
			Registry.BLOCK_REGISTRY,
			new ResourceLocation(CakeWorld.MODID,
					"sherbet_ocelots_spawnable_on"));

	public SherbetOcelot(
			EntityType<? extends Ocelot> type, Level level) {
		super(type, level);
	}

	@Override
	@Nullable
	public SherbetOcelot getBreedOffspring(
			ServerLevel level, AgeableMob partner) {
		return CakeWorldEntities.SHERBET_OCELOT.get().create(level);
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
		living.push(horizontal.x, 0.14D, horizontal.z);
		living.fallDistance = 0.0F;
		living.clearFire();
		living.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SLOWDOWN, 80,
				0, false, true));
		living.addEffect(new MobEffectInstance(
				MobEffects.SLOW_FALLING, 80,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.FIRE_RESISTANCE, 80,
				0, false, false));
		living.addEffect(new MobEffectInstance(
				MobEffects.DAMAGE_RESISTANCE, 80,
				4, false, false));
		return true;
	}

	/**
	 * Vanilla deliberately accepts two out of every three candidate spawns.
	 */
	public static boolean checkSherbetOcelotSpawnRules(
			EntityType<SherbetOcelot> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return random.nextInt(3) != 0;
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader level) {
		if (!level.isUnobstructed(this)
				|| level.containsAnyLiquid(getBoundingBox())) {
			return false;
		}
		BlockPos pos = blockPosition();
		return pos.getY() >= level.getSeaLevel()
				&& level.getBlockState(pos.below())
						.is(SPAWNABLE_ON);
	}
}
