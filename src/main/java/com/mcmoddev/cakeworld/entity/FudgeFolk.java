package com.mcmoddev.cakeworld.entity;

import java.util.List;
import java.util.Random;

import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

/**
 * Fudge Wastes' ordinary Piglin society role.
 *
 * <p>The genuine Piglin superclass retains its complete brain, inventory,
 * equipment, admiration, bartering, hunting, riding and zombification
 * contracts. The only behavioural repair here is for vanilla Piglin AI
 * branches which compare the literal Hoglin entity type instead of accepting
 * the already-recognised Hoglin subclass.</p>
 */
public class FudgeFolk extends Piglin {
	private static final UniformInt HUNT_COOLDOWN =
			TimeUtil.rangeOfSeconds(30, 120);
	private static final UniformInt RETREAT_DURATION =
			TimeUtil.rangeOfSeconds(5, 20);
	private LivingEntity deadFudgeBoarTarget;

	public FudgeFolk(
			EntityType<? extends AbstractPiglin> type, Level level) {
		super(type, level);
	}

	@Override
	public InteractionResult mobInteract(
			net.minecraft.world.entity.player.Player player,
			InteractionHand hand) {
		ItemStack offered = player.getItemInHand(hand);
		boolean directDistraction = isAdult()
				&& offered.is(Items.GOLD_INGOT)
				&& player instanceof ServerPlayer;
		InteractionResult result = super.mobInteract(player, hand);
		if (!level.isClientSide && result.consumesAction()
				&& directDistraction) {
			VanillaRoleAdvancements
					.creditDistractedPiglinRole(
							(ServerPlayer) player, true);
		}
		return result;
	}

	@Override
	public void onItemPickup(ItemEntity item) {
		ServerPlayer thrower = null;
		if (!level.isClientSide && isAdult()
				&& item.getItem().is(
						net.minecraft.tags.ItemTags.PIGLIN_LOVED)
				&& item.getThrower() != null) {
			net.minecraft.world.entity.player.Player player =
					level.getPlayerByUUID(item.getThrower());
			if (player instanceof ServerPlayer) {
				thrower = (ServerPlayer) player;
			}
		}
		super.onItemPickup(item);
		if (thrower != null) {
			VanillaRoleAdvancements
					.creditDistractedPiglinRole(
							thrower, false);
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		boolean hurt = super.hurt(source, amount);
		if (hurt && source.getEntity() instanceof FudgeBoar boar
				&& isAdult() && hoglinsOutnumberFudgeFolk()) {
			retreatFamilyFrom(boar);
		}
		return hurt;
	}

	@Override
	protected void customServerAiStep() {
		LivingEntity target = getBrain().getMemory(
				MemoryModuleType.ATTACK_TARGET).orElse(null);
		deadFudgeBoarTarget = target instanceof FudgeBoar
				&& target.isDeadOrDying() ? target : null;
		super.customServerAiStep();
		if (deadFudgeBoarTarget != null) {
			rememberFudgeBoarHunt();
			if (getBrain().hasMemoryValue(
					MemoryModuleType.CELEBRATE_LOCATION)
					&& new Random(level.getGameTime())
							.nextFloat() < 0.1F) {
				getBrain().setMemoryWithExpiry(
						MemoryModuleType.DANCING,
						true, 300L);
			}
			deadFudgeBoarTarget = null;
		}
	}

	public void rememberFudgeBoarHunt() {
		rememberHunt(this);
		for (AbstractPiglin piglin : visibleAdultPiglins()) {
			rememberHunt(piglin);
		}
	}

	private boolean hoglinsOutnumberFudgeFolk() {
		int piglins = getBrain().getMemory(
				MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT)
				.orElse(0) + 1;
		int hoglins = getBrain().getMemory(
				MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT)
				.orElse(0);
		return hoglins > piglins;
	}

	private void retreatFamilyFrom(LivingEntity danger) {
		retreatFrom(this, danger);
		for (AbstractPiglin piglin : visibleAdultPiglins()) {
			if (piglin instanceof Piglin) {
				LivingEntity nearest = BehaviorUtils.getNearestTarget(
						piglin,
						piglin.getBrain().getMemory(
								MemoryModuleType.AVOID_TARGET),
						danger);
				nearest = BehaviorUtils.getNearestTarget(
						piglin,
						piglin.getBrain().getMemory(
								MemoryModuleType.ATTACK_TARGET),
						nearest);
				retreatFrom(piglin, nearest);
			}
		}
	}

	private List<AbstractPiglin> visibleAdultPiglins() {
		return getBrain().getMemory(
				MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS)
				.orElse(List.of());
	}

	private static void retreatFrom(
			AbstractPiglin piglin, LivingEntity danger) {
		Brain<?> brain = piglin.getBrain();
		brain.eraseMemory(MemoryModuleType.ANGRY_AT);
		brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
		brain.eraseMemory(MemoryModuleType.WALK_TARGET);
		brain.setMemoryWithExpiry(
				MemoryModuleType.AVOID_TARGET, danger,
				RETREAT_DURATION.sample(piglin.level.random));
		rememberHunt(piglin);
	}

	private static void rememberHunt(AbstractPiglin piglin) {
		piglin.getBrain().setMemoryWithExpiry(
				MemoryModuleType.HUNTED_RECENTLY, true,
				HUNT_COOLDOWN.sample(piglin.level.random));
	}

	public static boolean checkFudgeFolkSpawnRules(
			EntityType<FudgeFolk> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, java.util.Random random) {
		return !level.getBlockState(pos.below())
				.is(Blocks.NETHER_WART_BLOCK);
	}
}
