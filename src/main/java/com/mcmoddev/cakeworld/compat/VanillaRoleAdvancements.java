package com.mcmoddev.cakeworld.compat;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CakeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VanillaRoleAdvancements {
	private static final ResourceLocation BRED_ALL =
			new ResourceLocation("minecraft", "husbandry/bred_all_animals");
	private static final ResourceLocation KILL_ALL =
			new ResourceLocation("minecraft", "adventure/kill_all_mobs");
	private VanillaRoleAdvancements() {
	}

	@SubscribeEvent
	public static void onBaby(BabyEntitySpawnEvent event) {
		if (event.getCausedByPlayer() instanceof ServerPlayer player
				&& event.getChild() != null) {
			creditBredRole(player, event.getChild().getType());
		}
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		if (event.getSource().getEntity() instanceof ServerPlayer player) {
			String criterion = killedCriterion(
					event.getEntityLiving().getType());
			if (criterion != null) {
				award(player, KILL_ALL, criterion);
			}
		}
	}

	public static void creditBredRole(ServerPlayer player, EntityType<?> childType) {
		String criterion = null;
		if (childType == CakeWorldEntities.SUGAR_BEE.get()) {
			criterion = "minecraft:bee";
		} else if (childType == CakeWorldEntities.CUSTARD_CAT.get()) {
			criterion = "minecraft:cat";
		} else if (childType == CakeWorldEntities.JELLYLOTL.get()) {
			criterion = "minecraft:axolotl";
		} else if (childType == CakeWorldEntities.COCOA_COW.get()) {
			criterion = "minecraft:cow";
		} else if (childType == CakeWorldEntities.MALLOW_CHICK.get()) {
			criterion = "minecraft:chicken";
		} else if (childType == CakeWorldEntities.TRUFFLE_PIG.get()) {
			criterion = "minecraft:pig";
		} else if (childType == CakeWorldEntities.CANDYFLOSS_SHEEP.get()) {
			criterion = "minecraft:sheep";
		} else if (childType == CakeWorldEntities.DOUGH_DONKEY.get()) {
			criterion = "minecraft:donkey";
		}
		if (criterion != null) {
			award(player, BRED_ALL, criterion);
		}
	}

	public static void creditKilledZombieRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:zombie");
	}

	public static void creditKilledBlazeRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:blaze");
	}

	public static void creditKilledCaveSpiderRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:cave_spider");
	}

	public static void creditKilledCreeperRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:creeper");
	}

	public static void creditKilledDrownedRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:drowned");
	}

	private static String killedCriterion(EntityType<?> type) {
		if (type == CakeWorldEntities.STALE_CRUMBLER.get()) {
			return "minecraft:zombie";
		}
		if (type == CakeWorldEntities.CINNAMON_SPARK.get()) {
			return "minecraft:blaze";
		}
		if (type == CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get()) {
			return "minecraft:cave_spider";
		}
		if (type == CakeWorldEntities.POP_ROCK_POPPER.get()) {
			return "minecraft:creeper";
		}
		if (type == CakeWorldEntities.SOGGY_BISCUIT.get()) {
			return "minecraft:drowned";
		}
		return null;
	}

	private static void award(ServerPlayer player, ResourceLocation advancementId,
			String criterion) {
		Advancement advancement = player.getServer().getAdvancements()
				.getAdvancement(advancementId);
		if (advancement != null) {
			player.getAdvancements().award(advancement, criterion);
		}
	}
}
