package zone.moddev.mc.cakeworld.entity;

import java.util.List;

import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Trader-Llama-role caravan animal.
 *
 * <p>Trader Llamas inherit the same literal-type caravan and offspring
 * factories as ordinary Llamas. Only those two seams are redirected: all
 * trader leash, defence, despawn, pack, taming and ranged-attack behaviour
 * remains owned by the vanilla {@link TraderLlama} body.</p>
 */
public class SprinkleLlama extends TraderLlama {
	public SprinkleLlama(
			EntityType<? extends TraderLlama> type,
			Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		List<Goal> literalCaravanGoals = goalSelector
				.getAvailableGoals().stream()
				.map(wrapped -> wrapped.getGoal())
				.filter(LlamaFollowCaravanGoal.class::isInstance)
				.toList();
		literalCaravanGoals.forEach(goalSelector::removeGoal);
		goalSelector.addGoal(2,
				new MeringueLlamaFollowCaravanGoal(
						this, (double)2.1F));
	}

	@Override
	protected Llama makeBabyLlama() {
		return CakeWorldEntities.SPRINKLE_LLAMA.get()
				.create(level);
	}
}
