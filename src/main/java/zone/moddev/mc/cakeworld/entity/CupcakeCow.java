package zone.moddev.mc.cakeworld.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * CakeWorld's genuine Mooshroom-role garden cow.
 *
 * <p>Most of the unusually rich role remains owned by vanilla's
 * {@link MushroomCow}: red and brown variants, bowls, suspicious stew,
 * flower memory, lightning conversion, and breeding mutation. The only
 * behavior duplicated here is the private vanilla shearing helper, because
 * its otherwise hard-coded destination is a literal vanilla cow.</p>
 */
public final class CupcakeCow extends MushroomCow {
	public CupcakeCow(
			EntityType<? extends MushroomCow> type, Level level) {
		super(type, level);
	}

	public static boolean checkCupcakeCowSpawnRules(
			EntityType<CupcakeCow> type, LevelAccessor level,
			MobSpawnType reason, BlockPos pos, Random random) {
		return level.getBlockState(pos.below())
				.is(BlockTags.MOOSHROOMS_SPAWNABLE_ON)
				&& isBrightEnoughToSpawn(level, pos);
	}

	@Override
	public float getWalkTargetValue(
			BlockPos pos, LevelReader level) {
		if (level.getBlockState(pos.below())
				.is(BlockTags.MOOSHROOMS_SPAWNABLE_ON)) {
			return 10.0F;
		}
		return super.getWalkTargetValue(pos, level);
	}

	@Override
	public CupcakeCow getBreedOffspring(
			ServerLevel level, AgeableMob mate) {
		MushroomCow vanillaChild =
				super.getBreedOffspring(level, mate);
		CupcakeCow child =
				CakeWorldEntities.CUPCAKE_COW.get()
						.create(level);
		if (child != null && vanillaChild != null) {
			CompoundTag variant = new CompoundTag();
			vanillaChild.addAdditionalSaveData(variant);
			child.readAdditionalSaveData(variant);
		}
		return child;
	}

	@Override
	public List<ItemStack> onSheared(
			Player player, ItemStack item, Level level,
			BlockPos pos, int fortune) {
		gameEvent(GameEvent.SHEAR, player);
		return shearToCocoaCow(player == null
				? SoundSource.BLOCKS : SoundSource.PLAYERS);
	}

	@Override
	public void shear(SoundSource source) {
		shearToCocoaCow(source).forEach(stack ->
				spawnAtLocation(stack, getBbHeight()));
	}

	private List<ItemStack> shearToCocoaCow(
			SoundSource source) {
		level.playSound(null, this,
				SoundEvents.MOOSHROOM_SHEAR,
				source, 1.0F, 1.0F);
		if (level.isClientSide()) {
			return Collections.emptyList();
		}

		((ServerLevel) level).sendParticles(
				ParticleTypes.EXPLOSION,
				getX(), getY(0.5D), getZ(),
				1, 0.0D, 0.0D, 0.0D, 0.0D);
		MushroomType mushroomType = getMushroomType();
		discard();

		CocoaCow cow = CakeWorldEntities.COCOA_COW.get()
				.create(level);
		if (cow != null) {
			cow.moveTo(getX(), getY(), getZ(),
					getYRot(), getXRot());
			cow.setHealth(getHealth());
			cow.yBodyRot = yBodyRot;
			if (hasCustomName()) {
				cow.setCustomName(getCustomName());
				cow.setCustomNameVisible(
						isCustomNameVisible());
			}
			if (isPersistenceRequired()) {
				cow.setPersistenceRequired();
			}
			cow.setInvulnerable(isInvulnerable());
			level.addFreshEntity(cow);
		}

		List<ItemStack> drops = new ArrayList<>();
		for (int index = 0; index < 5; index++) {
			drops.add(new ItemStack(
					mushroomType.getBlockState()
							.getBlock()));
		}
		return drops;
	}
}
