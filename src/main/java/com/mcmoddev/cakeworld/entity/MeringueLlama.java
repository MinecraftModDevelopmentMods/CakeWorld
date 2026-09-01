package com.mcmoddev.cakeworld.entity;

import java.util.List;

import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;

/**
 * CakeWorld's genuine Llama-role pack animal.
 *
 * <p>Vanilla hard-codes both Llama offspring and the two entity types that
 * caravan followers may notice. The offspring factory is redirected to the
 * CakeWorld type, and only the literal-type caravan goal is exchanged for an
 * otherwise equivalent subclass-friendly implementation.</p>
 */
public final class MeringueLlama extends Llama {
	public MeringueLlama(EntityType<? extends Llama> type,
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
						this, (double) 2.1F));
	}

	@Override
	protected Llama makeBabyLlama() {
		return CakeWorldEntities.MERINGUE_LLAMA.get()
				.create(level);
	}
}
