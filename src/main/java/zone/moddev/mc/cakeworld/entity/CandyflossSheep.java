package zone.moddev.mc.cakeworld.entity;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's complete Sheep role.
 *
 * <p>Vanilla colour, dye, shearing, fleece, food, movement, sound and save
 * behavior remain inherited. The two intentional extensions preserve the
 * recipe-derived offspring colour on the CakeWorld entity type and allow
 * fleece to regrow while grazing on edible terrain.</p>
 */
public class CandyflossSheep extends Sheep {
	public CandyflossSheep(EntityType<? extends Sheep> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		goalSelector.addGoal(5,
				new CandyflossSheepGrazeGoal(this));
	}

	@Override
	public Sheep getBreedOffspring(ServerLevel level, AgeableMob partner) {
		Sheep vanillaColorResult =
				super.getBreedOffspring(level, partner);
		CandyflossSheep child =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get()
						.create(level);
		if (child != null && vanillaColorResult != null) {
			child.setColor(vanillaColorResult.getColor());
		}
		return child;
	}
}
