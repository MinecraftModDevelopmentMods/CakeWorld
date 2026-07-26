package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's command-only Illusioner role.
 *
 * The inherited private goals retain the mirror and blindness spells. Only
 * the damaging bow payload changes below Hard.
 */
public class MirageConfectioner extends Illusioner {
	public MirageConfectioner(
			EntityType<? extends Illusioner> type, Level level) {
		super(type, level);
	}

	@Override
	public void performRangedAttack(
			LivingEntity target, float power) {
		if (level.getDifficulty() == Difficulty.HARD) {
			super.performRangedAttack(target, power);
			return;
		}

		MirageSweetProjectile sweet =
				new MirageSweetProjectile(level, this);
		double x = target.getX() - getX();
		double y = target.getY(0.3333333333333333D)
				- sweet.getY();
		double z = target.getZ() - getZ();
		double horizontal = Math.sqrt(x * x + z * z);
		sweet.shoot(x, y + horizontal * 0.2D, z,
				1.6F,
				(float) (14
						- level.getDifficulty().getId()
								* 4));
		playSound(SoundEvents.SKELETON_SHOOT, 1.0F,
				1.0F / (getRandom().nextFloat() * 0.4F
						+ 0.8F));
		level.addFreshEntity(sweet);
	}
}
