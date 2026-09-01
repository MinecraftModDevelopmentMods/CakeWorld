package com.mcmoddev.cakeworld.entity;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * CakeWorld's complete Shulker role and Macaron Citadel defender.
 *
 * <p>The inherited attachment, shell animation, armour, teleportation, goals,
 * homing projectile, colour NBT and sounds stay genuine Shulker behaviour. The
 * single overridden hook repairs vanilla's literal-entity duplication seam so
 * a Macaron Clam counts and creates Macaron Clams.</p>
 */
public class MacaronClam extends Shulker {
	private static final double DUPLICATE_SCAN_RADIUS = 8.0D;
	private static final float DUPLICATE_DENSITY_DIVISOR = 5.0F;

	public MacaronClam(
			EntityType<? extends Shulker> type, Level level) {
		super(type, level);
	}

	@Override
	protected void hitByShulkerBullet() {
		Vec3 origin = position();
		AABB searchArea = getBoundingBox()
				.inflate(DUPLICATE_SCAN_RADIUS);
		if (entityData.get(DATA_PEEK_ID) == 0
				|| !teleportSomewhere()) {
			return;
		}

		int nearbyRoleHolders = level.getEntitiesOfClass(
				Shulker.class, searchArea,
				clam -> clam.isAlive()
						&& (clam.getType()
								== EntityType.SHULKER
							|| clam.getType()
								== CakeWorldEntities
										.MACARON_CLAM.get()))
				.size();
		float crowdingChance =
				(nearbyRoleHolders - 1)
						/ DUPLICATE_DENSITY_DIVISOR;
		if (level.random.nextFloat() < crowdingChance) {
			return;
		}

		MacaronClam duplicate =
				CakeWorldEntities.MACARON_CLAM.get()
						.create(level);
		if (duplicate == null) {
			return;
		}
		DyeColor color = getColor();
		if (color != null) {
			duplicate.entityData.set(
					DATA_COLOR_ID, (byte)color.getId());
		}
		duplicate.moveTo(origin);
		level.addFreshEntity(duplicate);
	}
}
