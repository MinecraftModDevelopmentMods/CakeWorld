package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.BonbonBat;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.CinnamonPuffProjectile;
import com.mcmoddev.cakeworld.entity.CinnamonSpark;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.entity.Jellylotl;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.SugarBee;
import com.mcmoddev.cakeworld.entity.TrufflePig;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Blaze;
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
