package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.level.Level;

/**
 * Ice-Cream Tundra's genuine Polar Bear role.
 *
 * <p>The superclass retains neutral anger, cub defence, Fox hunting, standing
 * warnings, swimming, panic, family following and all ordinary Polar Bear
 * sounds. Only the literal vanilla offspring factory needs repairing for the
 * CakeWorld entity type.</p>
 */
public class VanillaIceBear extends PolarBear {
	public VanillaIceBear(EntityType<? extends PolarBear> type,
			Level level) {
		super(type, level);
	}

	@Override
	public VanillaIceBear getBreedOffspring(ServerLevel level,
			AgeableMob partner) {
		return CakeWorldEntities.VANILLA_ICE_BEAR.get().create(level);
	}
}
