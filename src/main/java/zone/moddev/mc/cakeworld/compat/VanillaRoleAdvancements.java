package zone.moddev.mc.cakeworld.compat;

import zone.moddev.mc.cakeworld.CakeWorld;
import zone.moddev.mc.cakeworld.init.CakeWorldEntities;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityMountEvent;
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
	private static final ResourceLocation RETURN_TO_SENDER =
			new ResourceLocation("minecraft", "nether/return_to_sender");
	private static final ResourceLocation DISTRACT_PIGLIN =
			new ResourceLocation("minecraft",
					"nether/distract_piglin");
	private static final ResourceLocation TWO_BIRDS =
			new ResourceLocation("minecraft",
					"adventure/two_birds_one_arrow");
	private static final ResourceLocation WHOS_THE_PILLAGER_NOW =
			new ResourceLocation("minecraft",
					"adventure/whos_the_pillager_now");
	private static final ResourceLocation SNIPER_DUEL =
			new ResourceLocation("minecraft",
					"adventure/sniper_duel");
	private static final String PHANTOM_ROLE_CROSSBOW_KILLS =
			"CakeWorldPhantomRoleCrossbowKills";
	private static final ResourceLocation RIDE_WITH_GOAT =
			new ResourceLocation("minecraft",
					"husbandry/ride_a_boat_with_a_goat");
	private static final ResourceLocation TACTICAL_FISHING =
			new ResourceLocation("minecraft",
					"husbandry/tactical_fishing");
	private static final ResourceLocation RIDE_STRIDER =
			new ResourceLocation("minecraft",
					"nether/ride_strider");
	private static final ResourceLocation RIDE_STRIDER_OVERWORLD =
			new ResourceLocation("minecraft",
					"nether/ride_strider_in_overworld_lava");
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
			if (event.getEntityLiving().getType()
						== CakeWorldEntities.MALLOW_FLOATER.get()
					&& event.getSource().getDirectEntity()
							instanceof LargeFireball) {
				creditReturnToSenderRole(player);
			}
			if (event.getSource().getDirectEntity()
							instanceof AbstractArrow arrow
					&& event.getEntityLiving().getType()
							== CakeWorldEntities.WAFER_WRAITH.get()) {
				recordPhantomRoleCrossbowKill(player, arrow,
						event.getEntityLiving().getType());
			}
			if (event.getEntityLiving().getType()
							== CakeWorldEntities.BISCUIT_BANDIT.get()
					&& event.getSource().getDirectEntity()
							instanceof AbstractArrow arrow) {
				creditWhosPillagerNowRole(player, arrow);
			}
			if (event.getEntityLiving().getType()
							== CakeWorldEntities.CANDY_CANE_ARCHER.get()
					&& event.getSource().getDirectEntity()
							instanceof AbstractArrow arrow) {
				creditSniperDuelRole(player, arrow,
						event.getEntityLiving());
			}
		}
	}

	@SubscribeEvent
	public static void onMount(EntityMountEvent event) {
		if (event.isMounting()
				&& event.getEntityMounting()
						instanceof ServerPlayer player
				&& event.getEntityBeingMounted() instanceof Boat boat
				&& boat.getPassengers().stream().anyMatch(passenger ->
						passenger.getType()
								== CakeWorldEntities.NOUGAT_GOAT.get())) {
			creditRodeBoatWithGoatRole(player);
		}
	}

	public static void creditBredRole(ServerPlayer player, EntityType<?> childType) {
		String criterion = null;
		if (childType == CakeWorldEntities.SUGAR_BEE.get()) {
			criterion = "minecraft:bee";
		} else if (childType == CakeWorldEntities.CUSTARD_CAT.get()) {
			criterion = "minecraft:cat";
		} else if (childType
				== CakeWorldEntities.SHERBET_OCELOT.get()) {
			criterion = "minecraft:ocelot";
		} else if (childType
				== CakeWorldEntities.CHOCOLATE_PANDA.get()) {
			criterion = "minecraft:panda";
		} else if (childType == CakeWorldEntities.JELLYLOTL.get()) {
			criterion = "minecraft:axolotl";
		} else if (childType == CakeWorldEntities.COCOA_COW.get()) {
			criterion = "minecraft:cow";
		} else if (childType
				== CakeWorldEntities.CUPCAKE_COW.get()) {
			criterion = "minecraft:mooshroom";
		} else if (childType == CakeWorldEntities.MALLOW_CHICK.get()) {
			criterion = "minecraft:chicken";
		} else if (childType == CakeWorldEntities.TRUFFLE_PIG.get()) {
			criterion = "minecraft:pig";
		} else if (childType == CakeWorldEntities.CANDYFLOSS_SHEEP.get()) {
			criterion = "minecraft:sheep";
		} else if (childType == CakeWorldEntities.DOUGH_DONKEY.get()) {
			criterion = "minecraft:donkey";
		} else if (childType == CakeWorldEntities.MARZIPAN_MULE.get()) {
			criterion = "minecraft:mule";
		} else if (childType == CakeWorldEntities.PEPPERMINT_FOX.get()) {
			criterion = "minecraft:fox";
		} else if (childType == CakeWorldEntities.NOUGAT_GOAT.get()) {
			criterion = "minecraft:goat";
		} else if (childType == CakeWorldEntities.FUDGE_BOAR.get()) {
			criterion = "minecraft:hoglin";
		} else if (childType
				== CakeWorldEntities.GINGERBREAD_PONY.get()) {
			criterion = "minecraft:horse";
		} else if (childType
				== CakeWorldEntities.MERINGUE_LLAMA.get()) {
			criterion = "minecraft:llama";
		} else if (childType
				== CakeWorldEntities.SPRINKLE_LLAMA.get()) {
			criterion = "minecraft:llama";
		} else if (childType
				== CakeWorldEntities.GUMMY_BUNNY.get()) {
			criterion = "minecraft:rabbit";
		} else if (childType
				== CakeWorldEntities.FUDGE_SKATER.get()) {
			criterion = "minecraft:strider";
		} else if (childType
				== CakeWorldEntities.WAFER_TURTLE.get()) {
			criterion = "minecraft:turtle";
		} else if (childType
				== CakeWorldEntities.GINGER_SNAP_HOUND.get()) {
			criterion = "minecraft:wolf";
		}
		if (criterion != null) {
			award(player, BRED_ALL, criterion);
		}
	}

	public static void creditKilledZombieRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:zombie");
	}

	public static void creditKilledWitherRole(
			ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:wither");
	}

	public static void creditKilledWitherSkeletonRole(
			ServerPlayer player) {
		award(player, KILL_ALL,
				"minecraft:wither_skeleton");
	}

	public static void creditRodeStriderRole(
			ServerPlayer player) {
		award(player, RIDE_STRIDER,
				"used_warped_fungus_on_a_stick");
	}

	public static void creditRodeStriderDistanceRole(
			ServerPlayer player) {
		award(player, RIDE_STRIDER_OVERWORLD,
				"ride_entity_distance");
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

	public static void creditKilledElderGuardianRole(
			ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:elder_guardian");
	}

	public static void creditKilledGuardianRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:guardian");
	}

	public static void creditKilledHoglinRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:hoglin");
	}

	public static void creditKilledHuskRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:husk");
	}

	public static void creditKilledMagmaCubeRole(
			ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:magma_cube");
	}

	public static void creditKilledEndermanRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:enderman");
	}

	public static void creditKilledEndermiteRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:endermite");
	}

	public static void creditKilledEvokerRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:evoker");
	}

	public static void creditKilledVexRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:vex");
	}

	public static void creditKilledGhastRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:ghast");
	}

	public static void creditKilledPhantomRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:phantom");
	}

	public static void creditKilledPiglinRole(ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:piglin");
	}

	public static void creditKilledPiglinBruteRole(
			ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:piglin_brute");
	}

	public static void creditKilledPillagerRole(
			ServerPlayer player) {
		award(player, KILL_ALL, "minecraft:pillager");
	}

	public static void creditKilledVindicatorRole(
			ServerPlayer player) {
		award(player, KILL_ALL,
				"minecraft:vindicator");
	}

	public static void creditKilledWitchRole(
			ServerPlayer player) {
		award(player, KILL_ALL,
				"minecraft:witch");
	}

	public static void creditDistractedPiglinRole(
			ServerPlayer player, boolean direct) {
		if (player.getItemBySlot(
						net.minecraft.world.entity.EquipmentSlot.HEAD)
						.is(Items.GOLDEN_HELMET)
				|| player.getItemBySlot(
						net.minecraft.world.entity.EquipmentSlot.CHEST)
						.is(Items.GOLDEN_CHESTPLATE)
				|| player.getItemBySlot(
						net.minecraft.world.entity.EquipmentSlot.LEGS)
						.is(Items.GOLDEN_LEGGINGS)
				|| player.getItemBySlot(
						net.minecraft.world.entity.EquipmentSlot.FEET)
						.is(Items.GOLDEN_BOOTS)) {
			return;
		}
		award(player, DISTRACT_PIGLIN,
				direct ? "distract_piglin_directly"
						: "distract_piglin");
	}

	public static void recordPhantomRoleCrossbowKill(
			ServerPlayer player, AbstractArrow arrow,
			EntityType<?> victimType) {
		if (!arrow.shotFromCrossbow()
				|| arrow.getPierceLevel() <= 0
				|| arrow.getOwner() != player
				|| (victimType != EntityType.PHANTOM
						&& victimType
								!= CakeWorldEntities.WAFER_WRAITH
										.get())) {
			return;
		}
		int kills = arrow.getPersistentData().getInt(
				PHANTOM_ROLE_CROSSBOW_KILLS) + 1;
		arrow.getPersistentData().putInt(
				PHANTOM_ROLE_CROSSBOW_KILLS, kills);
		if (kills >= 2) {
			award(player, TWO_BIRDS, "two_birds");
		}
	}

	public static void creditWhosPillagerNowRole(
			ServerPlayer player, AbstractArrow arrow) {
		if (arrow.shotFromCrossbow()
				&& arrow.getOwner() == player) {
			award(player, WHOS_THE_PILLAGER_NOW,
					"kill_pillager");
		}
	}

	public static void creditSniperDuelRole(
			ServerPlayer player, AbstractArrow arrow,
			net.minecraft.world.entity.LivingEntity victim) {
		double x = player.getX() - victim.getX();
		double z = player.getZ() - victim.getZ();
		if (arrow.getOwner() == player
				&& x * x + z * z >= 2500.0D) {
			award(player, SNIPER_DUEL,
					"killed_skeleton");
		}
	}

	public static void creditReturnToSenderRole(ServerPlayer player) {
		award(player, RETURN_TO_SENDER, "killed_ghast");
	}

	public static void creditRodeBoatWithGoatRole(ServerPlayer player) {
		award(player, RIDE_WITH_GOAT,
				"ride_a_boat_with_a_goat");
	}

	public static void creditPufferfishBucketRole(
			ServerPlayer player) {
		award(player, TACTICAL_FISHING, "pufferfish_bucket");
	}

	public static void creditCodBucketRole(
			ServerPlayer player) {
		award(player, TACTICAL_FISHING, "cod_bucket");
	}

	public static void creditSalmonBucketRole(
			ServerPlayer player) {
		award(player, TACTICAL_FISHING, "salmon_bucket");
	}

	public static void creditTropicalFishBucketRole(
			ServerPlayer player) {
		award(player, TACTICAL_FISHING,
				"tropical_fish_bucket");
	}

	private static String killedCriterion(EntityType<?> type) {
		if (type == CakeWorldEntities.STALE_CRUMBLER.get()) {
			return "minecraft:zombie";
		}
		if (type
				== CakeWorldEntities
						.CRUMBLED_GINGERBREAD_FOLK.get()) {
			return "minecraft:zombie_villager";
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
		if (type == CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get()) {
			return "minecraft:elder_guardian";
		}
		if (type == CakeWorldEntities.GUMBALL_GUARDIAN.get()) {
			return "minecraft:guardian";
		}
		if (type == CakeWorldEntities.FUDGE_BOAR.get()) {
			return "minecraft:hoglin";
		}
		if (type == CakeWorldEntities.DRIED_CRUMBLER.get()) {
			return "minecraft:husk";
		}
		if (type == CakeWorldEntities.HOT_FUDGE_BLOB.get()) {
			return "minecraft:magma_cube";
		}
		if (type == CakeWorldEntities.TAFFY_TALLWALKER.get()) {
			return "minecraft:enderman";
		}
		if (type == CakeWorldEntities.MACARON_CLAM.get()) {
			return "minecraft:shulker";
		}
		if (type == CakeWorldEntities.CRUMB_MITE.get()) {
			return "minecraft:silverfish";
		}
		if (type
				== CakeWorldEntities.CANDY_CANE_ARCHER.get()) {
			return "minecraft:skeleton";
		}
		if (type == CakeWorldEntities.FROSTED_ARCHER.get()) {
			return "minecraft:stray";
		}
		if (type == CakeWorldEntities.JELLY_BLOB.get()) {
			return "minecraft:slime";
		}
		if (type
				== CakeWorldEntities.LIQUORICE_WEAVER.get()) {
			return "minecraft:spider";
		}
		if (type == CakeWorldEntities.SUGAR_MITE.get()) {
			return "minecraft:endermite";
		}
		if (type == CakeWorldEntities.SOUR_SORCERER.get()) {
			return "minecraft:evoker";
		}
		if (type == CakeWorldEntities.SOUR_SPRITE.get()) {
			return "minecraft:vex";
		}
		if (type == CakeWorldEntities.MALLOW_FLOATER.get()) {
			return "minecraft:ghast";
		}
		if (type == CakeWorldEntities.WAFER_WRAITH.get()) {
			return "minecraft:phantom";
		}
		if (type == CakeWorldEntities.FUDGE_FOLK.get()) {
			return "minecraft:piglin";
		}
		if (type == CakeWorldEntities.FUDGE_BRUTE.get()) {
			return "minecraft:piglin_brute";
		}
		if (type == CakeWorldEntities.BISCUIT_BANDIT.get()) {
			return "minecraft:pillager";
		}
		if (type
				== CakeWorldEntities.ROLLING_PIN_RAIDER
						.get()) {
			return "minecraft:vindicator";
		}
		if (type == CakeWorldEntities.BITTER_BAKER.get()) {
			return "minecraft:witch";
		}
		if (type
				== CakeWorldEntities.BURNT_SUGAR_TEMPEST
						.get()) {
			return "minecraft:wither";
		}
		if (type
				== CakeWorldEntities.BURNT_CANDY_KNIGHT
						.get()) {
			return "minecraft:wither_skeleton";
		}
		if (type
				== CakeWorldEntities.STALE_FUDGE_BOAR
						.get()) {
			return "minecraft:zoglin";
		}
		if (type
				== CakeWorldEntities.STALE_FUDGE_FOLK
						.get()) {
			return "minecraft:zombified_piglin";
		}
		if (type
				== CakeWorldEntities.GINGERBREAD_STOMPER.get()) {
			return "minecraft:ravager";
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
