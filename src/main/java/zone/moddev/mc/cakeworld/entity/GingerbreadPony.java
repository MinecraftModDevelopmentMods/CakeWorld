package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;

/**
 * CakeWorld's genuine Horse-role mount.
 *
 * <p>Vanilla's Horse offspring method creates a literal
 * {@code EntityType.HORSE}, and its appearance setter is private. Same-family
 * breeding therefore lets vanilla calculate the genetic result, then copies
 * only that appearance and the calculated physical attributes into a
 * Gingerbread Pony. Crossbreeding with a Dough Donkey copies the same
 * calculated physical result into a Marzipan Mule.</p>
 */
public final class GingerbreadPony extends Horse {
	public GingerbreadPony(
			EntityType<? extends Horse> type, Level level) {
		super(type, level);
	}

	@Override
	public AgeableMob getBreedOffspring(
			ServerLevel level, AgeableMob mate) {
		AgeableMob inherited =
				super.getBreedOffspring(level, mate);
		if (mate instanceof DoughDonkey
				&& inherited instanceof Mule mule) {
			MarzipanMule child =
					CakeWorldEntities.MARZIPAN_MULE.get()
							.create(level);
			if (child == null) {
				return null;
			}
			copyBaseAttribute(mule, child,
					Attributes.MAX_HEALTH);
			copyBaseAttribute(mule, child,
					Attributes.JUMP_STRENGTH);
			copyBaseAttribute(mule, child,
					Attributes.MOVEMENT_SPEED);
			child.setHealth(child.getMaxHealth());
			return child;
		}
		if (!(mate instanceof GingerbreadPony)
				|| !(inherited instanceof Horse horse)) {
			return inherited;
		}

		GingerbreadPony child =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(level);
		if (child == null) {
			return null;
		}

		CompoundTag inheritedData = new CompoundTag();
		horse.addAdditionalSaveData(inheritedData);
		CompoundTag appearance = new CompoundTag();
		appearance.putInt("Variant",
				inheritedData.getInt("Variant"));
		child.readAdditionalSaveData(appearance);
		copyBaseAttribute(horse, child,
				Attributes.MAX_HEALTH);
		copyBaseAttribute(horse, child,
				Attributes.JUMP_STRENGTH);
		copyBaseAttribute(horse, child,
				Attributes.MOVEMENT_SPEED);
		child.setHealth(child.getMaxHealth());
		return child;
	}

	public static boolean checkGingerbreadPonySpawnRules(
			EntityType<? extends GingerbreadPony> type,
			ServerLevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		return level.getWorldBorder().isWithinBounds(pos)
				&& Animal.checkAnimalSpawnRules(
						type, level, reason, pos, random)
				&& NaturalSpawner.isValidEmptySpawnBlock(
						level, pos, level.getBlockState(pos),
						level.getFluidState(pos), type)
				&& NaturalSpawner.isValidEmptySpawnBlock(
						level, pos.above(),
						level.getBlockState(pos.above()),
						level.getFluidState(pos.above()), type);
	}

	private static void copyBaseAttribute(
			AbstractHorse source, AbstractHorse target,
			net.minecraft.world.entity.ai.attributes.Attribute attribute) {
		target.getAttribute(attribute).setBaseValue(
				source.getAttributeBaseValue(attribute));
	}
}
