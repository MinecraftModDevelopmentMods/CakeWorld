package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BonbonBat;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.CinnamonPuffProjectile;
import com.mcmoddev.cakeworld.entity.CinnamonSpark;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.entity.DeepLiquoriceWeaver;
import com.mcmoddev.cakeworld.entity.DoughDonkey;
import com.mcmoddev.cakeworld.entity.GrandGumballGuardian;
import com.mcmoddev.cakeworld.entity.GiantStaleCrumbler;
import com.mcmoddev.cakeworld.entity.GlowJelly;
import com.mcmoddev.cakeworld.entity.Jellylotl;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.MallowFloater;
import com.mcmoddev.cakeworld.entity.MallowPuffProjectile;
import com.mcmoddev.cakeworld.entity.PeppermintFox;
import com.mcmoddev.cakeworld.entity.PopRockPopper;
import com.mcmoddev.cakeworld.entity.SodaCod;
import com.mcmoddev.cakeworld.entity.SodaDolphin;
import com.mcmoddev.cakeworld.entity.SoggyBiscuit;
import com.mcmoddev.cakeworld.entity.SoggyTridentProjectile;
import com.mcmoddev.cakeworld.entity.SourSorcerer;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.SugarBee;
import com.mcmoddev.cakeworld.entity.SugarMite;
import com.mcmoddev.cakeworld.entity.TaffyTallwalker;
import com.mcmoddev.cakeworld.entity.TrufflePig;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CakeWorldEntities {
	private static final DeferredRegister<EntityType<?>> ENTITIES =
			DeferredRegister.create(ForgeRegistries.ENTITIES, CakeWorld.MODID);

	public static final RegistryObject<EntityType<CocoaCow>> COCOA_COW =
			entity("cocoa_cow", EntityType.Builder.of(CocoaCow::new, MobCategory.CREATURE)
					.sized(0.9F, 1.4F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<Jellylotl>> JELLYLOTL =
			entity("jellylotl",
					EntityType.Builder.of(Jellylotl::new,
							MobCategory.AXOLOTLS)
							.sized(0.75F, 0.42F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<BonbonBat>> BONBON_BAT =
			entity("bonbon_bat",
					EntityType.Builder.of(BonbonBat::new,
							MobCategory.AMBIENT)
							.sized(0.5F, 0.9F)
							.clientTrackingRange(5));
	public static final RegistryObject<EntityType<SugarBee>> SUGAR_BEE =
			entity("sugar_bee",
					EntityType.Builder.of(SugarBee::new,
							MobCategory.CREATURE)
							.sized(0.7F, 0.6F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<CustardCat>> CUSTARD_CAT =
			entity("custard_cat",
					EntityType.Builder.of(CustardCat::new,
							MobCategory.CREATURE)
							.sized(0.6F, 0.7F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<DeepLiquoriceWeaver>>
			DEEP_LIQUORICE_WEAVER = entity("deep_liquorice_weaver",
					EntityType.Builder.of(DeepLiquoriceWeaver::new,
							MobCategory.MONSTER)
							.sized(0.7F, 0.5F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<SodaCod>> SODA_COD =
			entity("soda_cod",
					EntityType.Builder.of(SodaCod::new,
							MobCategory.WATER_AMBIENT)
							.sized(0.5F, 0.3F)
							.clientTrackingRange(4));
	public static final RegistryObject<EntityType<SodaDolphin>> SODA_DOLPHIN =
			entity("soda_dolphin",
					EntityType.Builder.of(SodaDolphin::new,
							MobCategory.WATER_CREATURE)
							.sized(0.9F, 0.6F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<DoughDonkey>> DOUGH_DONKEY =
			entity("dough_donkey",
					EntityType.Builder.of(DoughDonkey::new,
							MobCategory.CREATURE)
							.sized(1.3964844F, 1.5F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<SoggyBiscuit>> SOGGY_BISCUIT =
			entity("soggy_biscuit",
					EntityType.Builder.of(SoggyBiscuit::new,
							MobCategory.MONSTER)
							.sized(0.6F, 1.95F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<SoggyTridentProjectile>>
			SOGGY_TRIDENT = entity("soggy_trident",
					EntityType.Builder.<SoggyTridentProjectile>of(
							SoggyTridentProjectile::new,
							MobCategory.MISC)
							.sized(0.5F, 0.5F)
							.clientTrackingRange(4)
							.updateInterval(20));
	public static final RegistryObject<EntityType<GrandGumballGuardian>>
			GRAND_GUMBALL_GUARDIAN = entity(
					"grand_gumball_guardian",
					EntityType.Builder.of(
							GrandGumballGuardian::new,
							MobCategory.MONSTER)
							.sized(1.9975F, 1.9975F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<TaffyTallwalker>>
			TAFFY_TALLWALKER = entity("taffy_tallwalker",
					EntityType.Builder.of(TaffyTallwalker::new,
							MobCategory.MONSTER)
							.sized(0.6F, 2.9F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<SugarMite>> SUGAR_MITE =
			entity("sugar_mite",
					EntityType.Builder.of(SugarMite::new,
							MobCategory.MONSTER)
							.sized(0.4F, 0.3F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<SourSorcerer>>
			SOUR_SORCERER = entity("sour_sorcerer",
					EntityType.Builder.of(SourSorcerer::new,
							MobCategory.MONSTER)
							.sized(0.6F, 1.95F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<PeppermintFox>>
			PEPPERMINT_FOX = entity("peppermint_fox",
					EntityType.Builder.of(PeppermintFox::new,
							MobCategory.CREATURE)
							.sized(0.6F, 0.7F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<MallowFloater>>
			MALLOW_FLOATER = entity("mallow_floater",
					EntityType.Builder.of(MallowFloater::new,
							MobCategory.MONSTER)
							.fireImmune()
							.sized(4.0F, 4.0F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<MallowPuffProjectile>>
			MALLOW_PUFF = entity("mallow_puff",
					EntityType.Builder.<MallowPuffProjectile>of(
							MallowPuffProjectile::new,
							MobCategory.MISC)
							.fireImmune()
							.sized(1.0F, 1.0F)
							.clientTrackingRange(4)
							.updateInterval(10));
	public static final RegistryObject<EntityType<GiantStaleCrumbler>>
			GIANT_STALE_CRUMBLER = entity(
					"giant_stale_crumbler",
					EntityType.Builder.of(GiantStaleCrumbler::new,
							MobCategory.MONSTER)
							.sized(3.6F, 12.0F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<GlowJelly>> GLOW_JELLY =
			entity("glow_jelly",
					EntityType.Builder.of(GlowJelly::new,
							MobCategory.UNDERGROUND_WATER_CREATURE)
							.sized(0.8F, 0.8F)
							.clientTrackingRange(10));
	public static final RegistryObject<EntityType<PopRockPopper>>
			POP_ROCK_POPPER = entity("pop_rock_popper",
					EntityType.Builder.of(PopRockPopper::new,
							MobCategory.MONSTER)
							.sized(0.6F, 1.7F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<CinnamonSpark>> CINNAMON_SPARK =
			entity("cinnamon_spark",
					EntityType.Builder.of(CinnamonSpark::new,
							MobCategory.MONSTER)
							.fireImmune()
							.sized(0.6F, 1.8F)
							.clientTrackingRange(8));
	public static final RegistryObject<EntityType<CinnamonPuffProjectile>>
			CINNAMON_PUFF = entity("cinnamon_puff",
					EntityType.Builder.<CinnamonPuffProjectile>of(
							CinnamonPuffProjectile::new,
							MobCategory.MISC)
							.sized(0.25F, 0.25F)
							.clientTrackingRange(4)
							.updateInterval(10));
	public static final RegistryObject<EntityType<MallowChick>> MALLOW_CHICK =
			entity("mallow_chick", EntityType.Builder.of(MallowChick::new, MobCategory.CREATURE)
					.sized(0.4F, 0.7F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<TrufflePig>> TRUFFLE_PIG =
			entity("truffle_pig", EntityType.Builder.of(TrufflePig::new, MobCategory.CREATURE)
					.sized(0.9F, 0.9F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<CandyflossSheep>> CANDYFLOSS_SHEEP =
			entity("candyfloss_sheep",
					EntityType.Builder.of(CandyflossSheep::new, MobCategory.CREATURE)
							.sized(0.9F, 1.3F).clientTrackingRange(10));
	public static final RegistryObject<EntityType<StaleCrumbler>> STALE_CRUMBLER =
			entity("stale_crumbler",
					EntityType.Builder.of(StaleCrumbler::new, MobCategory.MONSTER)
							.sized(0.6F, 1.95F).clientTrackingRange(8));

	private CakeWorldEntities() {
	}

	public static void register(IEventBus modBus) {
		ENTITIES.register(modBus);
		modBus.addListener(CakeWorldEntities::createAttributes);
		modBus.addListener(CakeWorldEntities::commonSetup);
	}

	private static void createAttributes(EntityAttributeCreationEvent event) {
		event.put(JELLYLOTL.get(), Axolotl.createAttributes().build());
		event.put(BONBON_BAT.get(), Bat.createAttributes().build());
		event.put(SUGAR_BEE.get(), Bee.createAttributes().build());
		event.put(CUSTARD_CAT.get(), Cat.createAttributes().build());
		event.put(DEEP_LIQUORICE_WEAVER.get(),
				CaveSpider.createCaveSpider().build());
		event.put(SODA_COD.get(), Cod.createMobAttributes().build());
		event.put(SODA_DOLPHIN.get(), Dolphin.createAttributes().build());
		event.put(DOUGH_DONKEY.get(),
				AbstractChestedHorse.createBaseChestedHorseAttributes()
						.build());
		event.put(SOGGY_BISCUIT.get(),
				Zombie.createAttributes().build());
		event.put(GRAND_GUMBALL_GUARDIAN.get(),
				ElderGuardian.createAttributes().build());
		event.put(TAFFY_TALLWALKER.get(),
				EnderMan.createAttributes().build());
		event.put(SUGAR_MITE.get(),
				Endermite.createAttributes().build());
		event.put(SOUR_SORCERER.get(),
				Evoker.createAttributes().build());
		event.put(PEPPERMINT_FOX.get(),
				Fox.createAttributes().build());
		event.put(MALLOW_FLOATER.get(),
				Ghast.createAttributes().build());
		event.put(GIANT_STALE_CRUMBLER.get(),
				Giant.createAttributes().build());
		event.put(GLOW_JELLY.get(),
				GlowSquid.createAttributes().build());
		event.put(POP_ROCK_POPPER.get(), Creeper.createAttributes().build());
		event.put(CINNAMON_SPARK.get(),
				Blaze.createAttributes().build());
		event.put(COCOA_COW.get(), Cow.createAttributes().build());
		event.put(MALLOW_CHICK.get(), Chicken.createAttributes().build());
		event.put(TRUFFLE_PIG.get(), Pig.createAttributes().build());
		event.put(CANDYFLOSS_SHEEP.get(), Sheep.createAttributes().build());
		event.put(STALE_CRUMBLER.get(), Zombie.createAttributes().build());
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SpawnPlacements.register(JELLYLOTL.get(),
					SpawnPlacements.Type.IN_WATER,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Axolotl::checkAxolotlSpawnRules);
			SpawnPlacements.register(BONBON_BAT.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					BonbonBat::checkBonbonBatSpawnRules);
			SpawnPlacements.register(SUGAR_BEE.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(CUSTARD_CAT.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(DEEP_LIQUORICE_WEAVER.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Monster::checkMonsterSpawnRules);
			SpawnPlacements.register(SODA_COD.get(),
					SpawnPlacements.Type.IN_WATER,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SodaCod::checkSodaCodSpawnRules);
			SpawnPlacements.register(SODA_DOLPHIN.get(),
					SpawnPlacements.Type.IN_WATER,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SodaDolphin::checkSodaDolphinSpawnRules);
			SpawnPlacements.register(DOUGH_DONKEY.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(SOGGY_BISCUIT.get(),
					SpawnPlacements.Type.IN_WATER,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SoggyBiscuit::checkSoggyBiscuitSpawnRules);
			SpawnPlacements.register(TAFFY_TALLWALKER.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Monster::checkMonsterSpawnRules);
			SpawnPlacements.register(SUGAR_MITE.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					SugarMite::checkSugarMiteSpawnRules);
			SpawnPlacements.register(PEPPERMINT_FOX.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					PeppermintFox::checkPeppermintFoxSpawnRules);
			SpawnPlacements.register(MALLOW_FLOATER.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					MallowFloater::checkMallowFloaterSpawnRules);
			SpawnPlacements.register(GLOW_JELLY.get(),
					SpawnPlacements.Type.IN_WATER,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					GlowJelly::checkGlowJellySpawnRules);
			SpawnPlacements.register(POP_ROCK_POPPER.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Monster::checkMonsterSpawnRules);
			SpawnPlacements.register(CINNAMON_SPARK.get(),
					SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Monster::checkAnyLightMonsterSpawnRules);
			SpawnPlacements.register(COCOA_COW.get(), SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(MALLOW_CHICK.get(), SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(TRUFFLE_PIG.get(), SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(CANDYFLOSS_SHEEP.get(), SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Animal::checkAnimalSpawnRules);
			SpawnPlacements.register(STALE_CRUMBLER.get(), SpawnPlacements.Type.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
					Monster::checkMonsterSpawnRules);
		});
	}

	private static <T extends net.minecraft.world.entity.Entity>
			RegistryObject<EntityType<T>> entity(String name, EntityType.Builder<T> builder) {
		return ENTITIES.register(name,
				() -> builder.build(CakeWorld.MODID + ":" + name));
	}
}
