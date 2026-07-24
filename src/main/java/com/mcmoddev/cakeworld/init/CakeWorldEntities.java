package com.mcmoddev.cakeworld.init;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.TrufflePig;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Monster;
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
		event.put(COCOA_COW.get(), Cow.createAttributes().build());
		event.put(MALLOW_CHICK.get(), Chicken.createAttributes().build());
		event.put(TRUFFLE_PIG.get(), Pig.createAttributes().build());
		event.put(CANDYFLOSS_SHEEP.get(), Sheep.createAttributes().build());
		event.put(STALE_CRUMBLER.get(), Zombie.createAttributes().build());
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
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
