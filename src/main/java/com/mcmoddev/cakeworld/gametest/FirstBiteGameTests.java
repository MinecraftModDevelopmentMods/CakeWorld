package com.mcmoddev.cakeworld.gametest;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.block.BiscuitCrumbsBlock;
import com.mcmoddev.cakeworld.block.CakeOvenBlock;
import com.mcmoddev.cakeworld.block.CandyCookerBlock;
import com.mcmoddev.cakeworld.block.CandySproutBlock;
import com.mcmoddev.cakeworld.block.ChocolateSpongeBlock;
import com.mcmoddev.cakeworld.block.CookbookKioskBlock;
import com.mcmoddev.cakeworld.block.CookbookLibraryBlock;
import com.mcmoddev.cakeworld.block.CoolingRackBlock;
import com.mcmoddev.cakeworld.block.GummyBlock;
import com.mcmoddev.cakeworld.block.IcingLayerBlock;
import com.mcmoddev.cakeworld.block.MarshmallowBlock;
import com.mcmoddev.cakeworld.block.SodaFountainBlock;
import com.mcmoddev.cakeworld.block.WaferWindmillBlock;
import com.mcmoddev.cakeworld.cookbook.CookbookEvents;
import com.mcmoddev.cakeworld.cookbook.CookbookHints;
import com.mcmoddev.cakeworld.cookbook.CookbookLayout;
import com.mcmoddev.cakeworld.cookbook.CookbookProgress;
import com.mcmoddev.cakeworld.cookbook.CookbookSummary;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;
import com.mcmoddev.cakeworld.cookbook.SharedCookbookLibrary;
import com.mcmoddev.cakeworld.compat.VanillaRoleAdvancements;
import com.mcmoddev.cakeworld.entity.BiscuitBandit;
import com.mcmoddev.cakeworld.entity.BitterBaker;
import com.mcmoddev.cakeworld.entity.BrittleBiscuitSteed;
import com.mcmoddev.cakeworld.entity.BurntCandyKnight;
import com.mcmoddev.cakeworld.entity.BurntCandyKnightSafety;
import com.mcmoddev.cakeworld.entity.BurntSugarTempest;
import com.mcmoddev.cakeworld.entity.BurntSugarTempestSafety;
import com.mcmoddev.cakeworld.entity.CandyflossSheep;
import com.mcmoddev.cakeworld.entity.CandyflossSheepGrazeGoal;
import com.mcmoddev.cakeworld.entity.CandyCaneArcher;
import com.mcmoddev.cakeworld.entity.BonbonBat;
import com.mcmoddev.cakeworld.entity.ChocolatePanda;
import com.mcmoddev.cakeworld.entity.CocoaCow;
import com.mcmoddev.cakeworld.entity.CupcakeCow;
import com.mcmoddev.cakeworld.entity.CrumbMite;
import com.mcmoddev.cakeworld.entity.CrumbMiteGriefSafety;
import com.mcmoddev.cakeworld.entity.CinnamonPuffProjectile;
import com.mcmoddev.cakeworld.entity.CinnamonSpark;
import com.mcmoddev.cakeworld.entity.Jellylotl;
import com.mcmoddev.cakeworld.entity.JellyBlob;
import com.mcmoddev.cakeworld.entity.JellybeanFish;
import com.mcmoddev.cakeworld.entity.JellyBlobDamageSafety;
import com.mcmoddev.cakeworld.entity.LollipopLorikeet;
import com.mcmoddev.cakeworld.entity.LiquoriceSquid;
import com.mcmoddev.cakeworld.entity.LiquoriceWeaver;
import com.mcmoddev.cakeworld.entity.MacaronClam;
import com.mcmoddev.cakeworld.entity.MacaronClamProjectileSafety;
import com.mcmoddev.cakeworld.entity.CustardCat;
import com.mcmoddev.cakeworld.entity.DeepLiquoriceWeaver;
import com.mcmoddev.cakeworld.entity.DoughDonkey;
import com.mcmoddev.cakeworld.entity.DriedCrumbler;
import com.mcmoddev.cakeworld.entity.GrandGumballGuardian;
import com.mcmoddev.cakeworld.entity.GiantStaleCrumbler;
import com.mcmoddev.cakeworld.entity.GlowJelly;
import com.mcmoddev.cakeworld.entity.GummyBunny;
import com.mcmoddev.cakeworld.entity.GummyBunnyPredatorCompatibility;
import com.mcmoddev.cakeworld.entity.GumballGuardian;
import com.mcmoddev.cakeworld.entity.FudgeBoar;
import com.mcmoddev.cakeworld.entity.FudgeBrute;
import com.mcmoddev.cakeworld.entity.FudgeFolk;
import com.mcmoddev.cakeworld.entity.FizzballFish;
import com.mcmoddev.cakeworld.entity.FizzballFishDamageSafety;
import com.mcmoddev.cakeworld.entity.FrostedArcher;
import com.mcmoddev.cakeworld.entity.FudgeSkater;
import com.mcmoddev.cakeworld.entity.GingerbreadFolk;
import com.mcmoddev.cakeworld.entity.GingerbreadPony;
import com.mcmoddev.cakeworld.entity.GingerbreadStomper;
import com.mcmoddev.cakeworld.entity.GingerbreadStomperDamageSafety;
import com.mcmoddev.cakeworld.entity.GingerbreadStomperGriefSafety;
import com.mcmoddev.cakeworld.entity.HotFudgeBlob;
import com.mcmoddev.cakeworld.entity.HotFudgeBlobDamageSafety;
import com.mcmoddev.cakeworld.entity.IceCreamGolem;
import com.mcmoddev.cakeworld.entity.IceCreamGolemProjectileSafety;
import com.mcmoddev.cakeworld.entity.JawbreakerGuardian;
import com.mcmoddev.cakeworld.entity.MallowChick;
import com.mcmoddev.cakeworld.entity.MallowFloater;
import com.mcmoddev.cakeworld.entity.MallowPuffProjectile;
import com.mcmoddev.cakeworld.entity.MarzipanMule;
import com.mcmoddev.cakeworld.entity.MeringueLlama;
import com.mcmoddev.cakeworld.entity.MeringueLlamaFollowCaravanGoal;
import com.mcmoddev.cakeworld.entity.MirageConfectioner;
import com.mcmoddev.cakeworld.entity.MirageSweetProjectile;
import com.mcmoddev.cakeworld.entity.NougatGoat;
import com.mcmoddev.cakeworld.entity.PeppermintFox;
import com.mcmoddev.cakeworld.entity.PopRockPopper;
import com.mcmoddev.cakeworld.entity.RollingPinRaider;
import com.mcmoddev.cakeworld.entity.RollingPinRaiderGriefSafety;
import com.mcmoddev.cakeworld.entity.SodaCod;
import com.mcmoddev.cakeworld.entity.SodaDolphin;
import com.mcmoddev.cakeworld.entity.SoggyBiscuit;
import com.mcmoddev.cakeworld.entity.SoggyTridentProjectile;
import com.mcmoddev.cakeworld.entity.SherbetOcelot;
import com.mcmoddev.cakeworld.entity.SherbetSalmon;
import com.mcmoddev.cakeworld.entity.SourSorcerer;
import com.mcmoddev.cakeworld.entity.SourSprite;
import com.mcmoddev.cakeworld.entity.SprinkleLlama;
import com.mcmoddev.cakeworld.entity.StaleCrumbler;
import com.mcmoddev.cakeworld.entity.SugarBee;
import com.mcmoddev.cakeworld.entity.SugarMite;
import com.mcmoddev.cakeworld.entity.TaffyTallwalker;
import com.mcmoddev.cakeworld.entity.TrufflePig;
import com.mcmoddev.cakeworld.entity.TravellingConfectioner;
import com.mcmoddev.cakeworld.entity.VanillaIceBear;
import com.mcmoddev.cakeworld.entity.VanillaIceBearDamageSafety;
import com.mcmoddev.cakeworld.entity.WaferTurtle;
import com.mcmoddev.cakeworld.entity.WaferWraith;
import com.mcmoddev.cakeworld.effect.FizzyFeetEffect;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldEffects;
import com.mcmoddev.cakeworld.init.CakeWorldEntities;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;
import com.mcmoddev.cakeworld.init.CakeWorldItems;
import com.mcmoddev.cakeworld.init.CakeWorldSounds;
import com.mcmoddev.cakeworld.item.JellylotlBucketItem;
import com.mcmoddev.cakeworld.world.CakeWorldPiglinReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldPiglinBruteReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldTurtleReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldVexReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldVillagerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldVindicatorReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldWanderingTraderReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldWitchReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldWitherReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldWitherSkeletonReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldPillagerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldRavagerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldShulkerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldSilverfishReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldSkeletonReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldSkeletonHorseReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldSnowGolemReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldSpiderReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldSquidReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldStrayReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldStriderReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldTraderLlamaReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldTropicalFishReplacement;
import com.mcmoddev.cakeworld.world.FudgeSkaterRideCompatibility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.sensing.HoglinSpecificSensor;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.SkeletonTrapGoal;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.NetherFortressFeature;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import com.mcmoddev.cakeworld.world.StarterPicnicFeature;
import com.mcmoddev.cakeworld.world.CakeWorldDrownedReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldElderGuardianReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldEndermiteReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldEvokerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldGiantReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldGuardianReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldIllusionerReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldIronGolemReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldMagmaCubeReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldPhantomReplacement;
import com.mcmoddev.cakeworld.world.CakeWorldCreatureSpawns;
import com.mcmoddev.cakeworld.world.TrufflePigCarrotOnAStickBridge;

@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class FirstBiteGameTests {
	private static final String EMPTY = "empty";

	private FirstBiteGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void softTerrainHasThePromisedPhysicalContracts(GameTestHelper helper) {
		BlockState sponge = CakeWorldBlocks.CHOCOLATE_SPONGE.get().defaultBlockState();
		BlockState icing = CakeWorldBlocks.ICING_LAYER.get().defaultBlockState();
		BlockState crumbs = CakeWorldBlocks.BISCUIT_CRUMBS.get().defaultBlockState();
		BlockState lemonadeIce = CakeWorldBlocks.FROZEN_LEMONADE.get().defaultBlockState();

		require(helper, sponge.getBlock() instanceof ChocolateSpongeBlock,
				"Chocolate sponge is not using its cushioning/nibbling block");
		require(helper, icing.getBlock() instanceof IcingLayerBlock,
				"Icing is not using its accumulating layer block");
		require(helper, icing.getValue(BlockStateProperties.LAYERS) == 1,
				"Icing does not begin as one accumulatable layer");
		require(helper, crumbs.getBlock() instanceof BiscuitCrumbsBlock
						&& crumbs.getBlock() instanceof FallingBlock,
				"Biscuit crumbs are not gravity affected");

		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig frictionTester = helper.spawnWithNoFreeWill(EntityType.PIG, 1.5F, 1.0F, 1.5F);
		float friction = lemonadeIce.getFriction(helper.getLevel(), testPos, frictionTester);
		require(helper, Math.abs(friction - 0.96F) < 0.0001F,
				"Frozen lemonade did not retain its distinctive 0.96 friction");

		Pig spongeTester = helper.spawnWithNoFreeWill(EntityType.PIG, 2.5F, 1.0F, 1.5F);
		spongeTester.setHealth(10.0F);
		sponge.getBlock().fallOn(helper.getLevel(), sponge, testPos, spongeTester, 11.0F);
		require(helper, Math.abs(spongeTester.getHealth() - 8.0F) < 0.001F,
				"Chocolate sponge did not reduce an eight-point fall to two points");

		Pig icingTester = helper.spawnWithNoFreeWill(EntityType.PIG, 3.5F, 1.0F, 1.5F);
		icingTester.setHealth(10.0F);
		icing.getBlock().fallOn(helper.getLevel(), icing, testPos, icingTester, 11.0F);
		require(helper, Math.abs(icingTester.getHealth() - 6.0F) < 0.001F,
				"Icing did not halve an eight-point fall");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public static void icingStacksAndMeltsAtTheVanillaLightThreshold(
			GameTestHelper helper) {
		BlockPos relativeIcingPos = new BlockPos(1, 1, 1);
		BlockPos absoluteIcingPos = helper.absolutePos(relativeIcingPos);
		IcingLayerBlock icing =
				(IcingLayerBlock) CakeWorldBlocks.ICING_LAYER.get();
		helper.setBlock(new BlockPos(1, 0, 1), Blocks.STONE);
		helper.setBlock(relativeIcingPos, icing.defaultBlockState());

		Player player = helper.makeMockPlayer();
		player.setPos(absoluteIcingPos.getX() + 0.5D,
				absoluteIcingPos.getY() + 1.0D,
				absoluteIcingPos.getZ() - 1.0D);
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldBlocks.ICING_LAYER.get()));
		BlockHitResult hit = new BlockHitResult(
				Vec3.atBottomCenterOf(absoluteIcingPos).add(0.0D, 1.0D, 0.0D),
				Direction.UP,
				absoluteIcingPos, false);
		InteractionResult stacked = icing.asItem().useOn(
				new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
		require(helper, stacked.consumesAction()
						&& helper.getBlockState(relativeIcingPos).getValue(
								BlockStateProperties.LAYERS) == 2,
				"Placing icing on icing did not accumulate a second layer");

		helper.setBlock(new BlockPos(2, 1, 1), Blocks.GLOWSTONE);
		helper.runAfterDelay(2, () -> {
			int blockLight = helper.getLevel().getBrightness(
					LightLayer.BLOCK, absoluteIcingPos);
			require(helper, blockLight > 11,
					"Icing melting fixture did not reach the vanilla light threshold");
			BlockState stackedIcing =
					helper.getBlockState(relativeIcingPos);
			icing.randomTick(stackedIcing, helper.getLevel(),
					absoluteIcingPos, helper.getLevel().random);
			require(helper, helper.getBlockState(relativeIcingPos).isAir(),
					"Bright icing did not inherit snow-layer melting");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void frozenLemonadePreservesMomentumWithoutRemovingControl(
			GameTestHelper helper) {
		helper.setBlock(new BlockPos(1, 0, 1),
				CakeWorldBlocks.FROZEN_LEMONADE.get());
		helper.setBlock(new BlockPos(3, 0, 1), Blocks.STONE);
		Pig lemonadeTester =
				helper.spawnWithNoFreeWill(EntityType.PIG, 1.5F, 1.0F, 1.5F);
		Pig stoneTester =
				helper.spawnWithNoFreeWill(EntityType.PIG, 3.5F, 1.0F, 1.5F);
		Vec3 initialMovement = new Vec3(0.2D, 0.0D, 0.0D);
		lemonadeTester.setOnGround(true);
		stoneTester.setOnGround(true);
		lemonadeTester.setDeltaMovement(initialMovement);
		stoneTester.setDeltaMovement(initialMovement);

		lemonadeTester.travel(Vec3.ZERO);
		stoneTester.travel(Vec3.ZERO);
		double lemonadeMomentum = lemonadeTester.getDeltaMovement().x;
		double stoneMomentum = stoneTester.getDeltaMovement().x;
		require(helper, lemonadeMomentum > stoneMomentum * 1.4D,
				"Frozen lemonade did not preserve recognisably more momentum than stone");
		require(helper, lemonadeMomentum > 0.0D
						&& lemonadeMomentum < initialMovement.x,
				"Frozen lemonade stopped movement or removed natural deceleration");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void marshmallowCancelsFallsAndCapsItsGentleBounce(
			GameTestHelper helper) {
		MarshmallowBlock marshmallow =
				(MarshmallowBlock) CakeWorldBlocks.MARSHMALLOW.get();
		BlockState state = marshmallow.defaultBlockState();
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig tester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		tester.setHealth(10.0F);
		marshmallow.fallOn(helper.getLevel(), state, testPos,
				tester, 30.0F);
		require(helper, Math.abs(tester.getHealth() - 10.0F) < 0.001F,
				"Marshmallow allowed health damage from a long fall");

		Vec3 falling = new Vec3(0.3D, -4.0D, -0.2D);
		tester.setDeltaMovement(falling);
		marshmallow.updateEntityAfterFallOn(helper.getLevel(), tester);
		Vec3 rebound = tester.getDeltaMovement();
		require(helper, close(rebound.x, falling.x)
						&& close(rebound.z, falling.z)
						&& close(rebound.y,
								MarshmallowBlock.MAXIMUM_BOUNCE),
				"Marshmallow lost steering or exceeded its gentle-bounce cap");

		Player crouching = helper.makeMockPlayer();
		crouching.setShiftKeyDown(true);
		crouching.setDeltaMovement(0.2D, -1.0D, 0.1D);
		marshmallow.updateEntityAfterFallOn(helper.getLevel(), crouching);
		require(helper, close(crouching.getDeltaMovement().y, 0.0D),
				"Crouching did not suppress the marshmallow bounce");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "marshmallow"))
						.isPresent(),
				"Marshmallow is not obtainable through its starter recipe");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gummyBlockProvidesAHighButBoundedElasticBounce(
			GameTestHelper helper) {
		GummyBlock gummy = (GummyBlock) CakeWorldBlocks.GUMMY_BLOCK.get();
		BlockState state = gummy.defaultBlockState();
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		Pig tester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		tester.setHealth(10.0F);
		gummy.fallOn(helper.getLevel(), state, testPos, tester, 30.0F);
		require(helper, Math.abs(tester.getHealth() - 10.0F) < 0.001F,
				"Gummy allowed health damage from a long fall");

		Vec3 ordinaryFall = new Vec3(0.3D, -1.0D, -0.2D);
		tester.setDeltaMovement(ordinaryFall);
		gummy.updateEntityAfterFallOn(helper.getLevel(), tester);
		Vec3 ordinaryRebound = tester.getDeltaMovement();
		require(helper, close(ordinaryRebound.x, ordinaryFall.x)
						&& close(ordinaryRebound.z, ordinaryFall.z)
						&& close(ordinaryRebound.y,
								GummyBlock.BOUNCE_MULTIPLIER)
						&& ordinaryRebound.y
								> MarshmallowBlock.BOUNCE_MULTIPLIER,
				"Gummy did not provide a stronger controlled rebound than marshmallow");

		tester.setDeltaMovement(0.1D, -4.0D, 0.2D);
		gummy.updateEntityAfterFallOn(helper.getLevel(), tester);
		require(helper, close(tester.getDeltaMovement().y,
						GummyBlock.MAXIMUM_BOUNCE),
				"Gummy did not cap an extreme rebound");

		Player crouching = helper.makeMockPlayer();
		crouching.setShiftKeyDown(true);
		crouching.setDeltaMovement(0.2D, -1.0D, 0.1D);
		gummy.updateEntityAfterFallOn(helper.getLevel(), crouching);
		require(helper, close(crouching.getDeltaMovement().y, 0.0D),
				"Crouching did not suppress the gummy bounce");
		ShapedRecipe recipe = (ShapedRecipe) helper.getLevel()
				.getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "gummy_block"))
				.orElseThrow();
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 3, 3);
		for (int slot = 0; slot < 9; slot++) {
			ingredients.setItem(slot, new ItemStack(
					slot == 4 ? CakeWorldItems.LEMONADE_BOTTLE.get()
							: Items.SUGAR));
		}
		ItemStack gummyResult = recipe.assemble(ingredients);
		ItemStack returnedBottle = recipe.getRemainingItems(ingredients).get(4);
		require(helper, recipe.matches(ingredients, helper.getLevel())
						&& gummyResult.is(CakeWorldBlocks.GUMMY_BLOCK.get().asItem())
						&& returnedBottle.is(Items.GLASS_BOTTLE),
				"Gummy recipe did not assemble the block and return its bottle");
		Map<String, BlockState> flavours = Map.of(
				"gummy_block",
				CakeWorldBlocks.GUMMY_BLOCK.get().defaultBlockState(),
				"raspberry_gummy_block",
				CakeWorldBlocks.RASPBERRY_GUMMY_BLOCK.get().defaultBlockState(),
				"blueberry_gummy_block",
				CakeWorldBlocks.BLUEBERRY_GUMMY_BLOCK.get().defaultBlockState(),
				"grape_gummy_block",
				CakeWorldBlocks.GRAPE_GUMMY_BLOCK.get().defaultBlockState());
		for (Map.Entry<String, BlockState> flavour : flavours.entrySet()) {
			require(helper, flavour.getValue().getBlock() instanceof GummyBlock
							&& helper.getLevel().getRecipeManager().byKey(
									new ResourceLocation(CakeWorld.MODID,
											flavour.getKey())).isPresent(),
					"Missing elastic block or recipe for gummy flavour "
							+ flavour.getKey());
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void waferBlocksAreCheapSolidAndDeliberatelyFragile(
			GameTestHelper helper) {
		BlockState wafer = CakeWorldBlocks.WAFER_BLOCK.get().defaultBlockState();
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		require(helper, !wafer.requiresCorrectToolForDrops()
						&& close(wafer.getDestroySpeed(
								helper.getLevel(), testPos), 0.2D)
						&& close(CakeWorldBlocks.WAFER_BLOCK.get()
								.getExplosionResistance(), 0.2D),
				"Wafer Block lost its quick-break fragile building contract");
		require(helper, !wafer.getCollisionShape(
						helper.getLevel(), testPos).isEmpty(),
				"Wafer Block cannot support a temporary bridge");

		ShapedRecipe recipe = (ShapedRecipe) helper.getLevel()
				.getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "wafer_block"))
				.orElseThrow();
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 2, 2);
		for (int slot = 0; slot < 4; slot++) {
			ingredients.setItem(slot,
					new ItemStack(CakeWorldItems.SIMPLE_BISCUIT.get()));
		}
		ItemStack result = recipe.assemble(ingredients);
		require(helper, recipe.matches(ingredients, helper.getLevel())
						&& result.is(CakeWorldBlocks.WAFER_BLOCK.get().asItem())
						&& result.getCount() == 8,
				"Wafer Block recipe is not a high-yield bridge supply");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void candySproutsCanBePickedWithoutDestroyingThePlant(
			GameTestHelper helper) {
		CandySproutBlock sprout =
				(CandySproutBlock) CakeWorldBlocks.CANDY_SPROUT.get();
		BlockPos relativePos = new BlockPos(1, 1, 1);
		BlockPos absolutePos = helper.absolutePos(relativePos);
		helper.setBlock(new BlockPos(1, 0, 1), Blocks.FARMLAND);
		helper.setBlock(relativePos,
				sprout.getStateForAge(sprout.getMaxAge()));
		Player player = helper.makeMockPlayer();
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absolutePos), Direction.UP,
				absolutePos, false);
		InteractionResult picked = sprout.use(
				helper.getBlockState(relativePos), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND, hit);

		int sweets = helper.getLevel().getEntitiesOfClass(
				ItemEntity.class, new AABB(absolutePos).inflate(1.0D),
				entity -> entity.getItem().is(
						CakeWorldItems.BOILED_SWEET.get()))
				.stream().mapToInt(entity -> entity.getItem().getCount()).sum();
		require(helper, picked.consumesAction()
						&& helper.getBlockState(relativePos).is(
								CakeWorldBlocks.CANDY_SPROUT.get())
						&& helper.getBlockState(relativePos).getValue(
								BlockStateProperties.AGE_7)
								== CandySproutBlock.PICKED_AGE
						&& sweets >= 2 && sweets <= 3,
				"Picking a ripe Candy Sprout did not leave regrowth and sweets");
		require(helper, new ItemStack(CakeWorldItems.SPRINKLE_SEEDS.get())
						.getItem() instanceof ItemNameBlockItem,
				"Sprinkle Seeds are not plantable as the Candy Sprout crop");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sugarRushIsBeneficialTemporaryAndHasNoCrash(
			GameTestHelper helper) {
		var sugarRush = CakeWorldEffects.SUGAR_RUSH.get();
		require(helper, sugarRush.getCategory()
						== MobEffectCategory.BENEFICIAL,
				"Sugar Rush is not categorised as a beneficial effect");
		Pig tester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		double ordinarySpeed =
				tester.getAttributeValue(Attributes.MOVEMENT_SPEED);
		tester.addEffect(new MobEffectInstance(sugarRush, 200));
		double rushedSpeed =
				tester.getAttributeValue(Attributes.MOVEMENT_SPEED);
		tester.removeEffect(sugarRush);
		double restoredSpeed =
				tester.getAttributeValue(Attributes.MOVEMENT_SPEED);
		require(helper, rushedSpeed > ordinarySpeed * 1.14D
						&& close(restoredSpeed, ordinarySpeed),
				"Sugar Rush did not boost speed cleanly without a crash state");

		FoodProperties fizz =
				CakeWorldItems.SHERBET_FIZZ.get().getFoodProperties();
		require(helper, fizz != null && fizz.getEffects().stream()
						.anyMatch(effect -> {
							MobEffectInstance instance =
									effect.getFirst();
							return instance.getEffect() == sugarRush
									&& instance.getDuration() == 200
									&& close(effect.getSecond(), 1.0D);
						}),
				"Sherbet Fizz does not always grant the intended Sugar Rush");

		ShapelessRecipe recipe = (ShapelessRecipe) helper.getLevel()
				.getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID, "sherbet_fizz"))
				.orElseThrow();
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 2, 2);
		ingredients.setItem(0,
				new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
		ingredients.setItem(1, new ItemStack(Items.SUGAR));
		ingredients.setItem(2,
				new ItemStack(CakeWorldItems.LEMONADE_BOTTLE.get()));
		ItemStack result = recipe.assemble(ingredients);
		require(helper, recipe.matches(ingredients, helper.getLevel())
						&& result.is(CakeWorldItems.SHERBET_FIZZ.get())
						&& result.getCount() == 2
						&& recipe.getRemainingItems(ingredients).get(2)
								.is(Items.GLASS_BOTTLE),
				"Sherbet Fizz recipe did not yield two and return its bottle");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void comfortMintAndFizzStayGentleAndDistinct(
			GameTestHelper helper) {
		var comfort = CakeWorldEffects.COCOA_COMFORT.get();
		var mint = CakeWorldEffects.MINTY_FRESH.get();
		var fizz = CakeWorldEffects.FIZZY_FEET.get();
		require(helper, comfort.getCategory() == MobEffectCategory.BENEFICIAL
						&& mint.getCategory() == MobEffectCategory.BENEFICIAL
						&& fizz.getCategory() == MobEffectCategory.BENEFICIAL,
				"A playful food effect was not categorised as beneficial");

		Pig comfortTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 1.5F, 1.0F, 1.5F);
		double ordinaryKnockback = comfortTester.getAttributeValue(
				Attributes.KNOCKBACK_RESISTANCE);
		double ordinaryArmour =
				comfortTester.getAttributeValue(Attributes.ARMOR);
		comfortTester.addEffect(new MobEffectInstance(comfort, 300));
		require(helper, comfortTester.getAttributeValue(
						Attributes.KNOCKBACK_RESISTANCE)
								>= ordinaryKnockback + 0.25D
						&& comfortTester.getAttributeValue(Attributes.ARMOR)
								>= ordinaryArmour + 2.0D,
				"Cocoa Comfort did not provide its modest defensive cushion");

		Pig mintTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 2.5F, 1.0F, 1.5F);
		mintTester.setSecondsOnFire(5);
		mint.applyEffectTick(mintTester, 0);
		require(helper, !mintTester.isOnFire(),
				"Minty Fresh did not extinguish its affected entity");

		Pig fizzTester = helper.spawnWithNoFreeWill(
				EntityType.PIG, 3.5F, 1.0F, 1.5F);
		Vec3 falling = new Vec3(0.2D, -1.0D, -0.1D);
		fizzTester.setDeltaMovement(falling);
		fizz.applyEffectTick(fizzTester, 0);
		Vec3 softened = fizzTester.getDeltaMovement();
		require(helper, close(softened.x, falling.x)
						&& close(softened.z, falling.z)
						&& close(softened.y,
								-FizzyFeetEffect.DESCENT_MULTIPLIER)
						&& softened.y < 0.0D,
				"Fizzy Feet lost steering or forced an upward camera bounce");

		require(helper, grantsEffect(CakeWorldItems.COMFORT_COCOA.get(),
							comfort, 300)
						&& grantsEffect(CakeWorldItems.MINT_WAFER.get(),
								mint, 200)
						&& grantsEffect(CakeWorldItems.FIZZY_POPPERS.get(),
								fizz, 200),
				"A named food does not guarantee its intended effect");
		for (String recipe : Set.of("comfort_cocoa", "mint_wafer",
				"fizzy_poppers")) {
			require(helper, helper.getLevel().getRecipeManager().byKey(
							new ResourceLocation(CakeWorld.MODID, recipe))
							.isPresent(),
					"Missing playable recipe " + recipe);
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void rollingPinPreparesReusableOvenReadyDough(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var rollingRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID,
						"rolled_biscuit_dough"));
		var bakingRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID,
						"simple_biscuit_from_rolled_dough"));
		require(helper, rollingRecipe.orElse(null) instanceof ShapelessRecipe
						&& bakingRecipe.orElse(null) instanceof SmeltingRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"rolling_pin")).isPresent(),
				"Rolling Pin is missing its acquisition or standard recipe chain");

		ItemStack rollingPin =
				new ItemStack(CakeWorldItems.ROLLING_PIN.get());
		require(helper, rollingPin.hasContainerItem()
						&& rollingPin.getContainerItem().is(
								CakeWorldItems.ROLLING_PIN.get()),
				"Rolling Pin is not a reusable crafting tool");
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 2, 2);
		ingredients.setItem(0, rollingPin);
		ingredients.setItem(3,
				new ItemStack(CakeWorldItems.SPONGE_BATTER.get()));
		ShapelessRecipe rolling =
				(ShapelessRecipe) rollingRecipe.orElseThrow();
		ItemStack dough = rolling.assemble(ingredients);
		require(helper, rolling.matches(ingredients, helper.getLevel())
						&& dough.is(CakeWorldItems.ROLLED_BISCUIT_DOUGH.get())
						&& dough.getCount() == 2
						&& rolling.getRemainingItems(ingredients).get(0)
								.is(CakeWorldItems.ROLLING_PIN.get()),
				"Rolling did not create two dough portions and return the pin");

		TagKey<Item> ovenBatters = TagKey.create(Registry.ITEM_REGISTRY,
				new ResourceLocation("forge", "oven_batters"));
		SmeltingRecipe baking =
				(SmeltingRecipe) bakingRecipe.orElseThrow();
		SimpleContainer ovenInput = new SimpleContainer(
				new ItemStack(CakeWorldItems.ROLLED_BISCUIT_DOUGH.get()));
		require(helper, ovenInput.getItem(0).is(ovenBatters)
						&& baking.matches(ovenInput, helper.getLevel())
						&& baking.assemble(ovenInput).is(
								CakeWorldItems.SIMPLE_BISCUIT.get()),
				"Rolled dough is not compatible with the standard food-only oven");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void pipingBagDecoratesCakeAndReturnsForReuse(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var decoratingRecipe = recipes.byKey(new ResourceLocation(
				CakeWorld.MODID, "piped_celebration_cake"));
		require(helper,
				decoratingRecipe.orElse(null) instanceof ShapelessRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"piping_bag")).orElse(null)
								instanceof ShapedRecipe,
				"Piping Bag is missing its acquisition or decoration recipe");

		ItemStack pipingBag =
				new ItemStack(CakeWorldItems.PIPING_BAG.get());
		require(helper, pipingBag.hasContainerItem()
						&& pipingBag.getContainerItem().is(
								CakeWorldItems.PIPING_BAG.get()),
				"Piping Bag is not a reusable crafting tool");
		AbstractContainerMenu recipeMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer ingredients =
				new CraftingContainer(recipeMenu, 3, 3);
		ingredients.setItem(0, pipingBag);
		ingredients.setItem(2,
				new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
		ingredients.setItem(4,
				new ItemStack(CakeWorldItems.WARM_SPONGE_CAKE.get()));
		ingredients.setItem(8,
				new ItemStack(CakeWorldItems.ICING_SPOONFUL.get()));
		ShapelessRecipe decorating =
				(ShapelessRecipe) decoratingRecipe.orElseThrow();
		ItemStack cake = decorating.assemble(ingredients);
		require(helper,
				decorating.matches(ingredients, helper.getLevel())
						&& cake.is(
								CakeWorldItems.PIPED_CELEBRATION_CAKE.get())
						&& decorating.getRemainingItems(ingredients)
								.get(0).is(CakeWorldItems.PIPING_BAG.get())
						&& cake.getFoodProperties(null).getNutrition()
								> CakeWorldItems.WARM_SPONGE_CAKE.get()
										.getFoodProperties().getNutrition(),
				"Piping did not improve the cake and return the bag");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void coolingRackUsesTaggedRecipesAndReturnsContainers(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var coolingRecipe = recipes.byKey(new ResourceLocation(
				CakeWorld.MODID, "fudge_squares_from_cooling"));
		require(helper, coolingRecipe.orElse(null) instanceof StonecutterRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"cooling_rack")).isPresent(),
				"Cooling Rack is missing its block or standard recipe");
		ItemStack hotFudge = new ItemStack(
				CakeWorldFluids.HOT_FUDGE_BUCKET.get());
		require(helper, hotFudge.is(CoolingRackBlock.INPUTS)
						&& hotFudge.hasContainerItem()
						&& hotFudge.getContainerItem().is(Items.BUCKET),
				"Hot Fudge Bucket lost its tagged container-return contract");

		BlockPos relativeRackPos = new BlockPos(1, 1, 1);
		BlockPos absoluteRackPos = helper.absolutePos(relativeRackPos);
		CoolingRackBlock rack =
				(CoolingRackBlock) CakeWorldBlocks.COOLING_RACK.get();
		helper.setBlock(relativeRackPos, rack.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND, hotFudge);
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteRackPos), Direction.UP,
				absoluteRackPos, false);
		InteractionResult cooled = rack.use(rack.defaultBlockState(),
				helper.getLevel(), absoluteRackPos, player,
				InteractionHand.MAIN_HAND, hit);
		int fudgeSquares = player.getInventory().items.stream()
				.filter(stack -> stack.is(CakeWorldItems.FUDGE_SQUARE.get()))
				.mapToInt(ItemStack::getCount).sum();
		require(helper, cooled.consumesAction()
						&& player.getMainHandItem().is(Items.BUCKET)
						&& fudgeSquares == 8,
				"Cooling did not produce eight Fudge Squares and return the bucket");

		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.SUGAR));
		InteractionResult rejected = rack.use(rack.defaultBlockState(),
				helper.getLevel(), absoluteRackPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, rejected == InteractionResult.PASS
						&& player.getMainHandItem().is(Items.SUGAR),
				"Cooling Rack consumed an untagged unrelated input");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void candyCookerUsesTaggedSmokingRecipes(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var candyRecipe = recipes.byKey(new ResourceLocation(
				CakeWorld.MODID, "caramel_chew_from_candy_cooker"));
		require(helper,
				candyRecipe.orElse(null) instanceof SmokingRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"candy_cooker")).isPresent(),
				"Candy Cooker is missing its block or standard recipe");
		ItemStack sugar = new ItemStack(Items.SUGAR, 2);
		require(helper, sugar.is(CandyCookerBlock.INPUTS),
				"Sugar is missing the public Candy Cooker input tag");

		BlockPos relativeCookerPos = new BlockPos(1, 1, 1);
		BlockPos absoluteCookerPos = helper.absolutePos(relativeCookerPos);
		CandyCookerBlock cooker =
				(CandyCookerBlock) CakeWorldBlocks.CANDY_COOKER.get();
		helper.setBlock(relativeCookerPos, cooker.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND, sugar);
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteCookerPos), Direction.UP,
				absoluteCookerPos, false);
		InteractionResult unfuelled = cooker.use(cooker.defaultBlockState(),
				helper.getLevel(), absoluteCookerPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, unfuelled == InteractionResult.PASS
						&& player.getMainHandItem().getCount() == 2,
				"Unfuelled Candy Cooker consumed its input");

		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		InteractionResult cooked = cooker.use(cooker.defaultBlockState(),
				helper.getLevel(), absoluteCookerPos, player,
				InteractionHand.MAIN_HAND, hit);
		int caramelChews = player.getInventory().items.stream()
				.filter(stack -> stack.is(
						CakeWorldItems.CARAMEL_CHEW.get()))
				.mapToInt(ItemStack::getCount).sum();
		require(helper, cooked.consumesAction()
						&& player.getMainHandItem().is(Items.SUGAR)
						&& player.getMainHandItem().getCount() == 1
						&& player.getOffhandItem().isEmpty()
						&& caramelChews == 1,
				"Candy Cooker did not consume one input/fuel and make one chew");

		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.BEEF));
		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		InteractionResult rejected = cooker.use(cooker.defaultBlockState(),
				helper.getLevel(), absoluteCookerPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, rejected == InteractionResult.PASS
						&& player.getMainHandItem().is(Items.BEEF)
						&& player.getOffhandItem().is(Items.COAL),
				"Candy Cooker consumed an unrelated smoker recipe");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sodaFountainDispensesMeasuredBottleServings(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		require(helper,
				recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"lemonade_bottles")).orElse(null)
						instanceof ShapelessRecipe
						&& recipes.byKey(new ResourceLocation(CakeWorld.MODID,
								"soda_fountain")).isPresent(),
				"Soda Fountain is missing its block or standard drink recipe");
		ItemStack lemonadeBucket = new ItemStack(
				CakeWorldFluids.LEMONADE_BUCKET.get());
		require(helper, lemonadeBucket.is(SodaFountainBlock.INPUTS),
				"Lemonade Bucket is missing the public fountain input tag");

		BlockPos relativeFountainPos = new BlockPos(1, 1, 1);
		BlockPos absoluteFountainPos =
				helper.absolutePos(relativeFountainPos);
		SodaFountainBlock fountain =
				(SodaFountainBlock) CakeWorldBlocks.SODA_FOUNTAIN.get();
		helper.setBlock(relativeFountainPos, fountain.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND, lemonadeBucket);
		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.GLASS_BOTTLE, 2));
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteFountainPos), Direction.UP,
				absoluteFountainPos, false);
		InteractionResult shortServing = fountain.use(
				fountain.defaultBlockState(), helper.getLevel(),
				absoluteFountainPos, player, InteractionHand.MAIN_HAND, hit);
		require(helper, shortServing == InteractionResult.PASS
						&& player.getMainHandItem().is(
								CakeWorldFluids.LEMONADE_BUCKET.get())
						&& player.getOffhandItem().getCount() == 2,
				"Fountain consumed an incomplete three-bottle serving");

		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.GLASS_BOTTLE, 3));
		InteractionResult dispensed = fountain.use(
				fountain.defaultBlockState(), helper.getLevel(),
				absoluteFountainPos, player, InteractionHand.MAIN_HAND, hit);
		int lemonadeBottles = player.getInventory().items.stream()
				.filter(stack -> stack.is(
						CakeWorldItems.LEMONADE_BOTTLE.get()))
				.mapToInt(ItemStack::getCount).sum();
		require(helper, dispensed.consumesAction()
						&& player.getMainHandItem().is(Items.BUCKET)
						&& player.getOffhandItem().isEmpty()
						&& lemonadeBottles == 3,
				"Fountain did not return the bucket and three drinks");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void whimsicalVillageMachineryStaysNonIndustrial(
			GameTestHelper helper) {
		BlockPos relativeWindmillPos = new BlockPos(1, 1, 1);
		BlockPos absoluteWindmillPos =
				helper.absolutePos(relativeWindmillPos);
		WaferWindmillBlock windmill =
				(WaferWindmillBlock) CakeWorldBlocks.WAFER_WINDMILL.get();
		helper.setBlock(relativeWindmillPos,
				windmill.defaultBlockState()
						.setValue(WaferWindmillBlock.FACING,
								Direction.EAST));
		BlockPos relativePowerPos = relativeWindmillPos.relative(
				Direction.WEST);
		BlockPos absolutePowerPos =
				helper.absolutePos(relativePowerPos);
		helper.setBlock(relativePowerPos, Blocks.REDSTONE_BLOCK);
		windmill.neighborChanged(
				helper.getBlockState(relativeWindmillPos),
				helper.getLevel(), absoluteWindmillPos,
				Blocks.REDSTONE_BLOCK, absolutePowerPos, false);
		BlockState powered =
				helper.getBlockState(relativeWindmillPos);
		require(helper, powered.getValue(WaferWindmillBlock.POWERED)
						&& powered.getValue(WaferWindmillBlock.FACING)
								== Direction.EAST
						&& !windmill.isSignalSource(powered)
						&& helper.getLevel().getBlockEntity(
								absoluteWindmillPos) == null,
				"Wafer Windmill lost its powered ambient-only contract");

		helper.setBlock(relativePowerPos, Blocks.AIR);
		windmill.neighborChanged(
				helper.getBlockState(relativeWindmillPos),
				helper.getLevel(), absoluteWindmillPos,
				Blocks.AIR, absolutePowerPos, false);
		require(helper,
				!helper.getBlockState(relativeWindmillPos)
						.getValue(WaferWindmillBlock.POWERED),
				"Wafer Windmill stayed active after power was removed");

		RotatedPillarBlock pipe =
				(RotatedPillarBlock) CakeWorldBlocks.SYRUP_PIPE.get();
		BlockState pipeX = pipe.defaultBlockState()
				.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
		BlockState pipeZ = pipeX.rotate(Rotation.CLOCKWISE_90);
		BlockPos relativePipePos = new BlockPos(2, 1, 1);
		helper.setBlock(relativePipePos, pipeX);
		require(helper,
				pipeX.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.X
						&& pipeZ.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.Z
						&& helper.getLevel().getBlockEntity(
								helper.absolutePos(relativePipePos)) == null,
				"Syrup Pipe lost its axis-aware decorative contract");
		require(helper,
				helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"wafer_windmill")).isPresent()
						&& helper.getLevel().getRecipeManager().byKey(
								new ResourceLocation(CakeWorld.MODID,
										"syrup_pipe")).isPresent(),
				"Whimsical village machinery is not obtainable");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void candyCanePillarsKeepAllThreeStructuralAxes(
			GameTestHelper helper) {
		RotatedPillarBlock pillar =
				(RotatedPillarBlock) CakeWorldBlocks.CANDY_CANE_PILLAR.get();
		BlockState vertical = pillar.defaultBlockState();
		BlockState eastWest = vertical.setValue(
				RotatedPillarBlock.AXIS, Direction.Axis.X);
		BlockState northSouth = eastWest.rotate(
				helper.getLevel(), helper.absolutePos(new BlockPos(1, 1, 1)),
				Rotation.CLOCKWISE_90);
		require(helper, vertical.getValue(RotatedPillarBlock.AXIS)
						== Direction.Axis.Y,
				"Candy-Cane Pillar does not default to a vertical axis");
		require(helper, eastWest.getValue(RotatedPillarBlock.AXIS)
						== Direction.Axis.X
						&& northSouth.getValue(RotatedPillarBlock.AXIS)
								== Direction.Axis.Z,
				"Candy-Cane Pillar did not rotate between horizontal axes");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"candy_cane_pillar")).isPresent(),
				"Candy-Cane Pillar is not obtainable through its recipe");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gingerbreadMasonryRequiresPreparedMortarAndTools(
			GameTestHelper helper) {
		BlockState bricks =
				CakeWorldBlocks.GINGERBREAD_BRICKS.get().defaultBlockState();
		require(helper, bricks.requiresCorrectToolForDrops()
						&& bricks.is(BlockTags.MINEABLE_WITH_PICKAXE),
				"Gingerbread Bricks lost their hard structural mining contract");
		require(helper, !new ItemStack(CakeWorldItems.FROSTING_MORTAR.get())
						.isEdible(),
				"Structural Frosting Mortar became a raw snack");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"frosting_mortar")).isPresent()
						&& helper.getLevel().getRecipeManager().byKey(
								new ResourceLocation(CakeWorld.MODID,
										"gingerbread_bricks")).isPresent(),
				"Gingerbread masonry is missing its prepared mortar recipe chain");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void stickyFluidsSlowWithoutTrapping(GameTestHelper helper) {
		BlockPos testPos = helper.absolutePos(new BlockPos(1, 1, 1));
		var caramelEntity = EntityType.ARMOR_STAND.create(helper.getLevel());
		var treacleEntity = EntityType.ARMOR_STAND.create(helper.getLevel());
		require(helper, caramelEntity != null && treacleEntity != null,
				"Could not create sticky-fluid test entities");

		Vec3 fallingMovement = new Vec3(1.0D, -0.8D, -1.0D);
		caramelEntity.setDeltaMovement(fallingMovement);
		CakeWorldFluids.CARAMEL_BLOCK.get().entityInside(
				CakeWorldFluids.CARAMEL_BLOCK.get().defaultBlockState(),
				helper.getLevel(), testPos, caramelEntity);
		Vec3 caramelMovement = caramelEntity.getDeltaMovement();
		require(helper, close(caramelMovement.x, 0.35D)
						&& close(caramelMovement.y, -0.4D)
						&& close(caramelMovement.z, -0.35D),
				"Caramel did not apply its strong horizontal/downward drag");

		treacleEntity.setDeltaMovement(fallingMovement);
		CakeWorldFluids.SYRUP_BLOCK.get().entityInside(
				CakeWorldFluids.SYRUP_BLOCK.get().defaultBlockState(),
				helper.getLevel(), testPos, treacleEntity);
		Vec3 treacleMovement = treacleEntity.getDeltaMovement();
		require(helper, close(treacleMovement.x, 0.55D)
						&& close(treacleMovement.y, -0.56D)
						&& close(treacleMovement.z, -0.55D),
				"Treacle Syrup did not apply its gentler drag");

		Vec3 escapingMovement = new Vec3(0.6D, 0.42D, 0.4D);
		caramelEntity.setDeltaMovement(escapingMovement);
		CakeWorldFluids.CARAMEL_BLOCK.get().entityInside(
				CakeWorldFluids.CARAMEL_BLOCK.get().defaultBlockState(),
				helper.getLevel(), testPos, caramelEntity);
		Vec3 escapingCaramel = caramelEntity.getDeltaMovement();
		require(helper, close(escapingCaramel.y, escapingMovement.y)
						&& escapingCaramel.x > 0.0D
						&& escapingCaramel.z > 0.0D,
				"Caramel removed upward escape or all player control");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void spongeNibblingIsDeliberateVisibleAndFinite(GameTestHelper helper) {
		BlockPos relativePos = new BlockPos(1, 1, 1);
		BlockPos absolutePos = helper.absolutePos(relativePos);
		ChocolateSpongeBlock sponge = (ChocolateSpongeBlock) CakeWorldBlocks.CHOCOLATE_SPONGE.get();
		helper.setBlock(relativePos, sponge.defaultBlockState());

		Player player = helper.makeMockPlayer();
		player.getFoodData().setFoodLevel(10);
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(absolutePos), Direction.UP,
				absolutePos, false);

		InteractionResult first = sponge.use(helper.getBlockState(relativePos), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND, hit);
		require(helper, first.consumesAction(), "Empty-hand nibbling did not consume the action");
		require(helper, player.getFoodData().getFoodLevel() == 11,
				"A raw-terrain nibble did not restore exactly one hunger point");
		require(helper, helper.getBlockState(relativePos).getValue(ChocolateSpongeBlock.BITES) == 1,
				"The first nibble did not expose its visible bite state");

		player.getFoodData().setFoodLevel(20);
		InteractionResult full = sponge.use(helper.getBlockState(relativePos), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND, hit);
		require(helper, full == InteractionResult.PASS,
				"Chocolate sponge could be nibbled at full hunger");
		require(helper, helper.getBlockState(relativePos).getValue(ChocolateSpongeBlock.BITES) == 1,
				"Full-hunger use changed the bite state");

		player.getFoodData().setFoodLevel(10);
		for (int i = 0; i < 3; i++) {
			sponge.use(helper.getBlockState(relativePos), helper.getLevel(), absolutePos, player,
					InteractionHand.MAIN_HAND, hit);
		}
		require(helper, helper.getBlockState(relativePos).isAir(),
				"Four portions did not fully consume the sponge block");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 40)
	public static void biscuitCrumbsActuallyFall(GameTestHelper helper) {
		BlockPos start = new BlockPos(1, 3, 1);
		BlockPos destination = new BlockPos(1, 1, 1);
		helper.setBlock(new BlockPos(1, 0, 1), Blocks.STONE);
		helper.setBlock(start, CakeWorldBlocks.BISCUIT_CRUMBS.get());
		helper.succeedWhen(() -> {
			helper.assertBlockPresent(CakeWorldBlocks.BISCUIT_CRUMBS.get(), destination);
			helper.assertBlockNotPresent(CakeWorldBlocks.BISCUIT_CRUMBS.get(), start);
		});
	}

	@GameTest(template = EMPTY)
	public static void preparedFoodsBeatRawTerrain(GameTestHelper helper) {
		FoodProperties slice = requireFood(helper,
				CakeWorldItems.CHOCOLATE_SPONGE_SLICE.get().getDefaultInstance());
		FoodProperties icing = requireFood(helper,
				CakeWorldItems.ICING_SPOONFUL.get().getDefaultInstance());
		FoodProperties biscuit = requireFood(helper,
				CakeWorldItems.SIMPLE_BISCUIT.get().getDefaultInstance());
		FoodProperties lemonade = requireFood(helper,
				CakeWorldItems.LEMONADE_BOTTLE.get().getDefaultInstance());
		FoodProperties sundae = requireFood(helper,
				CakeWorldItems.CHOCOLATE_SPONGE_SUNDAE.get().getDefaultInstance());

		require(helper, slice.getNutrition() > ChocolateSpongeBlock.NIBBLE_NUTRITION,
				"Sponge slices are not better than raw terrain");
		require(helper, icing.getNutrition() == 1 && biscuit.getNutrition() == 4
						&& lemonade.getNutrition() == 3,
				"Starter foods do not match their designed nutrition");
		require(helper, sundae.getNutrition() == 8
						&& sundae.getSaturationModifier() > slice.getSaturationModifier(),
				"The prepared starter meal is not a worthwhile upgrade");
		require(helper, CakeWorldItems.LEMONADE_BOTTLE.get().getCraftingRemainingItem()
						== Items.GLASS_BOTTLE,
				"Lemonade bottles do not return their container in crafting");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void starterKitchenUsesReusableMixingAndStandardCooking(
			GameTestHelper helper) {
		var recipes = helper.getLevel().getRecipeManager();
		var batterRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID, "sponge_batter"));
		var ovenRecipe = recipes.byKey(
				new ResourceLocation(CakeWorld.MODID,
						"warm_sponge_cake_from_oven"));
		require(helper, batterRecipe.orElse(null) instanceof ShapelessRecipe,
				"Sponge batter is not a data-driven shapeless recipe");
		require(helper, ovenRecipe.orElse(null) instanceof SmeltingRecipe,
				"Warm Sponge Cake is not a standard smelting recipe");

		ItemStack mixingBowl =
				new ItemStack(CakeWorldBlocks.MIXING_BOWL.get());
		require(helper, mixingBowl.hasContainerItem()
						&& mixingBowl.getContainerItem().is(
								CakeWorldBlocks.MIXING_BOWL.get().asItem()),
				"The Mixing Bowl is not returned after shapeless preparation");
		AbstractContainerMenu testMenu =
				new AbstractContainerMenu(null, 0) {
					@Override
					public boolean stillValid(Player player) {
						return true;
					}
				};
		CraftingContainer craftingGrid =
				new CraftingContainer(testMenu, 2, 2);
		craftingGrid.setItem(0, mixingBowl);
		craftingGrid.setItem(1,
				new ItemStack(CakeWorldItems.ICING_SPOONFUL.get()));
		craftingGrid.setItem(3,
				new ItemStack(CakeWorldItems.CHOCOLATE_SPONGE_SLICE.get()));
		ShapelessRecipe shapelessBatter =
				(ShapelessRecipe) batterRecipe.orElseThrow();
		require(helper, shapelessBatter.matches(
						craftingGrid, helper.getLevel())
						&& shapelessBatter.getRemainingItems(craftingGrid)
								.get(0).is(
										CakeWorldBlocks.MIXING_BOWL.get()
												.asItem()),
				"Mixed ingredient order did not match or return the Mixing Bowl");
		TagKey<Item> ovenBatters = TagKey.create(Registry.ITEM_REGISTRY,
				new ResourceLocation("forge", "oven_batters"));
		require(helper, new ItemStack(CakeWorldItems.SPONGE_BATTER.get())
						.is(ovenBatters),
				"Sponge Batter is missing the public oven-batter tag");

		BlockPos relativeOvenPos = new BlockPos(1, 1, 1);
		BlockPos absoluteOvenPos = helper.absolutePos(relativeOvenPos);
		CakeOvenBlock oven = (CakeOvenBlock) CakeWorldBlocks.OVEN.get();
		helper.setBlock(relativeOvenPos, oven.defaultBlockState());
		Player player = helper.makeMockPlayer();
		player.getAbilities().instabuild = false;
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems.SPONGE_BATTER.get()));
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteOvenPos), Direction.UP,
				absoluteOvenPos, false);
		require(helper, oven.use(oven.defaultBlockState(),
						helper.getLevel(), absoluteOvenPos, player,
						InteractionHand.MAIN_HAND, hit)
						== InteractionResult.PASS
						&& player.getMainHandItem().is(
								CakeWorldItems.SPONGE_BATTER.get()),
				"The Oven cooked without fuel or consumed its input");

		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		InteractionResult cooked = oven.use(oven.defaultBlockState(),
				helper.getLevel(), absoluteOvenPos, player,
				InteractionHand.MAIN_HAND, hit);
		require(helper, cooked.consumesAction(),
				"The fuelled Oven rejected its standard food recipe: "
						+ cooked);
		require(helper, player.getMainHandItem().is(
						CakeWorldItems.WARM_SPONGE_CAKE.get()),
				"The Oven did not return Warm Sponge Cake; main hand is "
						+ player.getMainHandItem());
		require(helper, player.getOffhandItem().isEmpty(),
				"The Oven did not consume one standard fuel; offhand is "
						+ player.getOffhandItem());

		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldBlocks.COCOA_COAL.get()));
		player.setItemInHand(InteractionHand.OFF_HAND,
				new ItemStack(Items.COAL));
		require(helper, oven.use(oven.defaultBlockState(),
						helper.getLevel(), absoluteOvenPos, player,
						InteractionHand.MAIN_HAND, hit)
						== InteractionResult.PASS
						&& player.getMainHandItem().is(
								CakeWorldBlocks.COCOA_COAL.get().asItem())
						&& player.getOffhandItem().is(Items.COAL),
				"The starter Oven accepted a non-food resource recipe");
		require(helper, CakeWorldItems.WARM_SPONGE_CAKE.get()
						.getFoodProperties().getNutrition()
						> CakeWorldItems.CHOCOLATE_SPONGE_SLICE.get()
								.getFoodProperties().getNutrition(),
				"Cooked Sponge Cake is not a worthwhile prepared food");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookProgressIsPlayerOwnedAndHasSixEventTypes(GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.getMinecraft(helper.getLevel());
		for (DiscoveryType type : DiscoveryType.values()) {
			ResourceLocation page = new ResourceLocation(CakeWorld.MODID,
					"gametest/" + type.name().toLowerCase());
			require(helper, CookbookProgress.discover(player, type, page),
					type + " did not add its first page");
			require(helper, !CookbookProgress.discover(player, type, page),
					type + " accepted a duplicate page");
		}

		CookbookProgress.grantStarterBook(player);
		player.getInventory().clearContent();
		CompoundTag snapshot = CookbookProgress.snapshot(player);
		Map<DiscoveryType, Set<ResourceLocation>> discoveries =
				CookbookProgress.read(snapshot);
		require(helper, discoveries.size() == DiscoveryType.values().length,
				"The Cookbook does not expose all six discovery tabs");
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, discoveries.get(type).size() == 1,
					type + " progress was lost with the physical book");
		}
		require(helper, CookbookProgress.recoverBook(player),
				"The kiosk recovery path did not replace a lost Cookbook");
		require(helper, player.getInventory().contains(
				CakeWorldItems.EXPLORERS_COOKBOOK.get().getDefaultInstance()),
				"The recovered Cookbook did not reach the player");

		ServerPlayer replacement = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.fromString("1978cafe-cafe-4bad-babe-1978cafe1978"),
						"CakeWorldRespawnTest"));
		Map<DiscoveryType, Set<ResourceLocation>> isolated =
				CookbookProgress.read(CookbookProgress.snapshot(replacement));
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, isolated.get(type).isEmpty(),
					type + " leaked between players");
		}
		CookbookProgress.copyForRespawn(player, replacement);
		Map<DiscoveryType, Set<ResourceLocation>> copied =
				CookbookProgress.read(CookbookProgress.snapshot(replacement));
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, copied.get(type).size() == 1,
					type + " did not survive the respawn copy");
		}

		String[] expectedRecipes = {
			"chocolate_sponge_from_slices",
			"chocolate_sponge_slices",
			"chocolate_sponge_sundae",
			"cookbook_kiosk",
			"explorers_cookbook",
			"icing_from_spoonfuls",
			"icing_spoonful",
			"lemonade_bottles",
			"simple_biscuit"
		};
		for (String recipe : expectedRecipes) {
			require(helper, helper.getLevel().getRecipeManager().byKey(
					new ResourceLocation(CakeWorld.MODID, recipe)).isPresent(),
					"Recipe did not load: " + recipe);
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookHintsAreOptionalPersonalAndReadOnly(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978cafe-cafe-4bad-babe-1978cafe1980"),
						"CakeWorldHintTest"));
		CompoundTag untouched =
				CookbookProgress.snapshot(player).copy();
		var first = CookbookHints.nextHint(player);
		require(helper, first.isPresent()
						&& CookbookProgress.snapshot(player)
								.equals(untouched),
				"Selecting a hint wrote quest-like progress state");

		CookbookHints.Hint firstHint = first.orElseThrow();
		require(helper, CookbookProgress.discover(player,
						firstHint.type(), firstHint.target()),
				"Could not record the hinted discovery");
		var next = CookbookHints.nextHint(player);
		require(helper, next.isPresent()
						&& !next.orElseThrow().target()
								.equals(firstHint.target()),
				"Hint repeated a page the player already discovered");

		CompoundTag beforeSneakUse =
				CookbookProgress.snapshot(player).copy();
		player.setShiftKeyDown(true);
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems.EXPLORERS_COOKBOOK.get()));
		var used = CakeWorldItems.EXPLORERS_COOKBOOK.get().use(
				helper.getLevel(), player, InteractionHand.MAIN_HAND);
		require(helper, used.getResult().consumesAction()
						&& CookbookProgress.snapshot(player)
								.equals(beforeSneakUse),
				"Sneak-use hint changed discoveries or failed to consume use");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookSummaryDerivesStampsAndHonestCompletion(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978cafe-cafe-4bad-babe-1978cafe1981"),
						"CakeWorldSummaryTest"));
		CookbookSummary empty = CookbookSummary.from(
				CookbookProgress.read(CookbookProgress.snapshot(player)));
		require(helper, empty.totalPages() == 0 && empty.stamps() == 0
						&& !empty.firstEditionComplete(),
				"Empty Cookbook received pages, stamps, or completion");

		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, CookbookProgress.discover(player, type,
							new ResourceLocation(CakeWorld.MODID,
									"summary/" + type.name().toLowerCase())),
					"Could not add summary page for " + type);
		}
		CookbookSummary complete = CookbookSummary.from(
				CookbookProgress.read(CookbookProgress.snapshot(player)));
		require(helper, complete.totalPages() == 6
						&& complete.stamps() == 6
						&& complete.firstEditionComplete(),
				"All six discovery methods did not complete First Edition");
		for (DiscoveryType type : DiscoveryType.values()) {
			require(helper, complete.pages(type) == 1
							&& complete.hasStamp(type),
					type + " has an incorrect page count or stamp");
		}

		require(helper, CookbookProgress.discover(player,
						DiscoveryType.VISITING,
						new ResourceLocation(CakeWorld.MODID,
								"summary/extra_place")),
				"Could not add an extra summary page");
		CookbookSummary expanded = CookbookSummary.from(
				CookbookProgress.read(CookbookProgress.snapshot(player)));
		require(helper, expanded.totalPages() == 7
						&& expanded.pages(DiscoveryType.VISITING) == 2
						&& expanded.stamps() == 6
						&& expanded.firstEditionComplete(),
				"Extra pages incorrectly changed stamp-based completion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookAccessibilityLayoutScalesAndSoundIsRegistered(
			GameTestHelper helper) {
		int tabs = DiscoveryType.values().length;
		CookbookLayout standard =
				CookbookLayout.calculate(854, 480, tabs);
		require(helper, standard.width() == CookbookLayout.MAX_WIDTH
						&& standard.height() == CookbookLayout.MAX_HEIGHT
						&& standard.tabColumns() == 6
						&& standard.tabRows() == 1,
				"Standard Cookbook layout lost its readable six-tab form");

		CookbookLayout compact =
				CookbookLayout.calculate(320, 180, tabs);
		require(helper, compact.left() >= 0 && compact.top() >= 0
						&& compact.left() + compact.width() <= 320
						&& compact.top() + compact.height() <= 180
						&& compact.tabColumns() == 3
						&& compact.tabRows() == 2
						&& compact.visiblePageCapacity() > 0,
				"Compact Cookbook layout escaped the scaled viewport");
		for (int index = 0; index < tabs; index++) {
			int hitX = (compact.tabLeft(index)
					+ compact.tabRight(index)) / 2;
			int hitY = compact.tabTop(index)
					+ CookbookLayout.TAB_HEIGHT / 2;
			require(helper, compact.tabIndexAt(hitX, hitY, tabs)
							== index,
					"Compact tab hit target did not map to tab " + index);
		}
		require(helper, compact.tabIndexAt(0, 0, tabs) == -1,
				"Outside click was incorrectly treated as a Cookbook tab");

		ResourceLocation soundId = new ResourceLocation(
				CakeWorld.MODID, "cookbook_discovery");
		require(helper, CakeWorldSounds.COOKBOOK_DISCOVERY.isPresent()
						&& Registry.SOUND_EVENT.get(soundId)
								== CakeWorldSounds.COOKBOOK_DISCOVERY.get(),
				"Subtitled Cookbook discovery sound is not registered");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sharedLibraryPublishesLoreWithoutGrantingProgress(
			GameTestHelper helper) {
		ServerPlayer contributor = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(),
						"CakeWorldLibraryContributor"));
		ServerPlayer reader = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(),
						"CakeWorldLibraryReader"));
		ResourceLocation privateDiscovery = new ResourceLocation(
				CakeWorld.MODID,
				"gametest/shared_" + UUID.randomUUID());
		require(helper, CookbookProgress.discover(contributor,
						DiscoveryType.FINDING, privateDiscovery),
				"Contributor could not discover publishable lore");
		CompoundTag readerBefore =
				CookbookProgress.snapshot(reader).copy();

		BlockPos relativeLibraryPos = new BlockPos(1, 1, 1);
		BlockPos absoluteLibraryPos =
				helper.absolutePos(relativeLibraryPos);
		CookbookLibraryBlock libraryBlock =
				(CookbookLibraryBlock) CakeWorldBlocks.COOKBOOK_LIBRARY.get();
		helper.setBlock(relativeLibraryPos,
				libraryBlock.defaultBlockState());
		BlockHitResult hit = new BlockHitResult(
				Vec3.atCenterOf(absoluteLibraryPos), Direction.UP,
				absoluteLibraryPos, false);
		contributor.setShiftKeyDown(true);
		InteractionResult published = libraryBlock.use(
				libraryBlock.defaultBlockState(), helper.getLevel(),
				absoluteLibraryPos, contributor,
				InteractionHand.MAIN_HAND, hit);
		SharedCookbookLibrary shared =
				SharedCookbookLibrary.get(helper.getLevel());
		require(helper, published.consumesAction()
						&& shared.contains(DiscoveryType.FINDING,
								privateDiscovery)
						&& shared.pageCount() > 0,
				"Library did not publish contributor-owned lore");
		SharedCookbookLibrary restored = SharedCookbookLibrary.load(
				shared.save(new CompoundTag()));
		require(helper, restored.contains(DiscoveryType.FINDING,
						privateDiscovery)
						&& restored.pageCount() == shared.pageCount(),
				"Shared lore did not survive a save/load round trip");

		reader.setShiftKeyDown(false);
		InteractionResult read = libraryBlock.use(
				libraryBlock.defaultBlockState(), helper.getLevel(),
				absoluteLibraryPos, reader,
				InteractionHand.MAIN_HAND, hit);
		require(helper, read.consumesAction()
						&& CookbookProgress.snapshot(reader)
								.equals(readerBefore),
				"Reading shared lore granted personal progression");
		require(helper, helper.getLevel().getRecipeManager().byKey(
						new ResourceLocation(CakeWorld.MODID,
								"cookbook_library")).isPresent(),
				"Community Cookbook Library is not obtainable");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cookbookEventHooksUnlockAllSixDiscoveryPaths(
			GameTestHelper helper) {
		ServerPlayer player = FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(
						UUID.fromString(
								"1978cafe-cafe-4bad-babe-1978cafe1979"),
						"CakeWorldEventHookTest"));
		BlockPos relativePos = new BlockPos(1, 1, 1);
		BlockPos absolutePos = helper.absolutePos(relativePos);
		player.setPos(absolutePos.getX() + 0.5D,
				absolutePos.getY(), absolutePos.getZ() + 0.5D);
		player.tickCount = 40;

		ResourceLocation visitedBiome = player.level
				.getBiome(player.blockPosition()).unwrapKey().orElseThrow()
				.location();
		require(helper, CakeWorld.MODID.equals(visitedBiome.getNamespace()),
				"Event-hook fixture is not inside a CakeWorld biome");
		CookbookEvents.onPlayerTick(new TickEvent.PlayerTickEvent(
				TickEvent.Phase.END, player));
		CookbookEvents.onFinishFood(
				new LivingEntityUseItemEvent.Finish(player,
						new ItemStack(CakeWorldItems.WARM_SPONGE_CAKE.get()),
						0, ItemStack.EMPTY));
		CookbookEvents.onCraft(new PlayerEvent.ItemCraftedEvent(player,
				new ItemStack(CakeWorldItems.SPONGE_BATTER.get()),
				new SimpleContainer(0)));
		CookbookEvents.onBreak(new BlockEvent.BreakEvent(helper.getLevel(),
				absolutePos,
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(),
				player));
		CocoaCow cow =
				CakeWorldEntities.COCOA_COW.get().create(helper.getLevel());
		require(helper, cow != null,
				"Could not create the meeting-discovery fixture");
		CookbookEvents.onTrack(
				new PlayerEvent.StartTracking(player, cow));

		CookbookKioskBlock kiosk =
				(CookbookKioskBlock) CakeWorldBlocks.COOKBOOK_KIOSK.get();
		helper.setBlock(relativePos, kiosk.defaultBlockState());
		kiosk.use(kiosk.defaultBlockState(), helper.getLevel(),
				absolutePos, player, InteractionHand.MAIN_HAND,
				new BlockHitResult(Vec3.atCenterOf(absolutePos),
						Direction.UP, absolutePos, false));

		Map<DiscoveryType, Set<ResourceLocation>> discoveries =
				CookbookProgress.read(CookbookProgress.snapshot(player));
		require(helper, discoveries.get(DiscoveryType.VISITING)
						.contains(visitedBiome),
				"Visiting hook did not record the current CakeWorld biome"
						+ " tick=" + player.tickCount
						+ " pos=" + player.blockPosition()
						+ " biome=" + visitedBiome
						+ " visits="
						+ discoveries.get(DiscoveryType.VISITING));
		require(helper, discoveries.get(DiscoveryType.TASTING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"warm_sponge_cake")),
				"Tasting hook did not record the eaten CakeWorld food");
		require(helper, discoveries.get(DiscoveryType.CRAFTING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"sponge_batter")),
				"Crafting hook did not record the crafted CakeWorld item");
		require(helper, discoveries.get(DiscoveryType.MINING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"chocolate_sponge")),
				"Mining hook did not record the broken CakeWorld block");
		require(helper, discoveries.get(DiscoveryType.MEETING).contains(
						new ResourceLocation(CakeWorld.MODID, "cocoa_cow")),
				"Meeting hook did not record the tracked CakeWorld entity");
		require(helper, discoveries.get(DiscoveryType.FINDING).contains(
						new ResourceLocation(CakeWorld.MODID,
								"cookbook_kiosk")),
				"Finding hook did not record the used landmark");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void firstCreaturesReplaceRolesAndRespectDifficulty(GameTestHelper helper) {
		CocoaCow cocoaCow = CakeWorldEntities.COCOA_COW.get().create(helper.getLevel());
		MallowChick mallowChick =
				CakeWorldEntities.MALLOW_CHICK.get().create(helper.getLevel());
		TrufflePig trufflePig =
				CakeWorldEntities.TRUFFLE_PIG.get().create(helper.getLevel());
		CandyflossSheep candyflossSheep =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get().create(helper.getLevel());
		require(helper, cocoaCow != null && cocoaCow.getBreedOffspring(
				helper.getLevel(), cocoaCow) instanceof CocoaCow,
				"Cocoa Cows did not breed their own entity type");
		require(helper, mallowChick != null && mallowChick.getBreedOffspring(
				helper.getLevel(), mallowChick) instanceof MallowChick,
				"Mallow Chicks did not breed their own entity type");
		require(helper, trufflePig != null && trufflePig.getBreedOffspring(
				helper.getLevel(), trufflePig) instanceof TrufflePig,
				"Truffle Pigs did not breed their own entity type");
		require(helper, candyflossSheep != null && candyflossSheep.getBreedOffspring(
				helper.getLevel(), candyflossSheep) instanceof CandyflossSheep,
				"Candyfloss Sheep did not breed their own entity type");

		StaleCrumbler crumbler =
				CakeWorldEntities.STALE_CRUMBLER.get().create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, crumbler != null && target != null,
				"Could not create the difficulty-contract test entities");
		target.setHealth(10.0F);
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				helper.getLevel().getServer().setDifficulty(safeDifficulty, true);
				require(helper, crumbler.doHurtTarget(target),
						safeDifficulty + " Stale Crumbler attack did not register");
				require(helper, Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& target.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(net.minecraft.world.effect.MobEffects.SLOW_FALLING),
						safeDifficulty
								+ " attack caused damage or lacked safe inconvenience/rescue");
			}

			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(Difficulty.HARD, true);
			require(helper, crumbler.doHurtTarget(target) && target.getHealth() < 10.0F,
					"Hard Stale Crumbler attack did not cause real damage");

			helper.getLevel().getServer().setDifficulty(Difficulty.PEACEFUL, true);
			StaleCrumbler peacefulCrumbler =
					CakeWorldEntities.STALE_CRUMBLER.get().create(helper.getLevel());
			require(helper, peacefulCrumbler != null,
					"Could not create the Peaceful behavior test creature");
			peacefulCrumbler.checkDespawn();
			require(helper, peacefulCrumbler.isRemoved(),
					"Stale Crumbler did not retain Peaceful despawning");
		} finally {
			helper.getLevel().getServer().setDifficulty(originalDifficulty, true);
		}

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		Biome cookieForest = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.COOKIE_FOREST.getId());
		require(helper, candyPlains != null && cookieForest != null,
				"Could not inspect CakeWorld biome spawn roles");
		requireSpawnReplacement(helper, candyPlains, EntityType.COW,
				CakeWorldEntities.COCOA_COW.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, candyPlains, EntityType.PIG,
				CakeWorldEntities.TRUFFLE_PIG.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, candyPlains, EntityType.SHEEP,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, cookieForest, EntityType.CHICKEN,
				CakeWorldEntities.MALLOW_CHICK.get(), MobCategory.CREATURE);
		requireSpawnReplacement(helper, candyPlains, EntityType.ZOMBIE,
				CakeWorldEntities.STALE_CRUMBLER.get(), MobCategory.MONSTER);

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString("1978feed-feed-4bad-babe-1978feed1978"),
						"CakeWorldRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.COCOA_COW.get());
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.MALLOW_CHICK.get());
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.TRUFFLE_PIG.get());
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		VanillaRoleAdvancements.creditKilledZombieRole(advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:cow");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:chicken");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:pig");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals", "minecraft:sheep");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs", "minecraft:zombie");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void jellylotlPreservesAquaticRoleWithoutNormalDamage(
			GameTestHelper helper) {
		Jellylotl jellylotl =
				CakeWorldEntities.JELLYLOTL.get().create(helper.getLevel());
		require(helper, jellylotl != null
						&& jellylotl.getBreedOffspring(helper.getLevel(),
								jellylotl) instanceof Jellylotl,
				"Jellylotls did not breed their own entity type");

		ItemStack flavourSource =
				new ItemStack(CakeWorldItems.JELLYLOTL_BUCKET.get());
		flavourSource.getOrCreateTag().putInt(Axolotl.VARIANT_TAG, 3);
		jellylotl.loadFromBucketTag(flavourSource.getTag());
		ItemStack captured = jellylotl.getBucketItemStack();
		jellylotl.saveToBucketTag(captured);
		Jellylotl restored =
				CakeWorldEntities.JELLYLOTL.get().create(helper.getLevel());
		require(helper, restored != null
						&& captured.is(CakeWorldItems.JELLYLOTL_BUCKET.get())
						&& CakeWorldItems.JELLYLOTL_BUCKET.get()
								instanceof net.minecraft.world.item.MobBucketItem
						&& "tooltip.cakeworld.jellylotl.flavour.3".equals(
								JellylotlBucketItem.flavourKey(captured)),
				"Jellylotl capture lost its dedicated bucket or flavour");
		restored.loadFromBucketTag(captured.getTag());
		require(helper, restored.getVariant() == jellylotl.getVariant(),
				"Jellylotl bucket round trip changed its flavour variant");

		Cod target = EntityType.COD.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Jellylotl helper target");
		float fullTargetHealth = target.getMaxHealth();
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(fullTargetHealth);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				require(helper, jellylotl.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- fullTargetHealth)
										< 0.001F
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.GLOWING),
						safeDifficulty
								+ " Jellylotl helper attack caused damage or lacked its visible slow");
			}
			target.removeAllEffects();
			target.setHealth(fullTargetHealth);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			require(helper, jellylotl.doHurtTarget(target)
							&& target.getHealth() < fullTargetHealth,
					"Hard Jellylotl attack did not restore real damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Jellylotl spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.AXOLOTL,
				CakeWorldEntities.JELLYLOTL.get(),
				MobCategory.AXOLOTLS);
		require(helper, CakeWorldBlocks.BISCUIT_CRUMBS.get()
						.defaultBlockState().is(
								BlockTags.AXOLOTLS_SPAWNABLE_ON),
				"Biscuit Crumbs cannot support natural Jellylotl spawning");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2001"),
						"CakeWorldJellylotlRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.JELLYLOTL.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:axolotl");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void bonbonBatIsHarmlessPersistentCakeWorldAmbience(
			GameTestHelper helper) {
		BonbonBat bat =
				CakeWorldEntities.BONBON_BAT.get().create(helper.getLevel());
		require(helper, bat != null
						&& bat.getType().getCategory()
								== MobCategory.AMBIENT
						&& bat.getAttribute(Attributes.ATTACK_DAMAGE) == null
						&& !bat.isPushable()
						&& !bat.causeFallDamage(100.0F, 1.0F,
								net.minecraft.world.damagesource.DamageSource.FALL),
				"Bonbon Bat gained a damage, collision, or fall-hazard role");
		bat.setResting(true);
		CompoundTag saved = new CompoundTag();
		bat.addAdditionalSaveData(saved);
		BonbonBat restored =
				CakeWorldEntities.BONBON_BAT.get().create(helper.getLevel());
		require(helper, restored != null, "Could not restore Bonbon Bat");
		restored.readAdditionalSaveData(saved);
		require(helper, restored.isResting(),
				"Bonbon Bat lost vanilla roost state through save/load");

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect CakeWorld Bonbon Bat biome");
			requireSpawnReplacement(helper, loaded, EntityType.BAT,
					CakeWorldEntities.BONBON_BAT.get(),
					MobCategory.AMBIENT);
		}
		require(helper, CakeWorldItems.BONBON_BAT_SPAWN_EGG.isPresent(),
				"Bonbon Bat has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sugarBeePollinatesWithoutNormalStingDamage(
			GameTestHelper helper) {
		SugarBee bee =
				CakeWorldEntities.SUGAR_BEE.get().create(helper.getLevel());
		require(helper, bee != null
						&& bee.getBreedOffspring(helper.getLevel(), bee)
								instanceof SugarBee,
				"Sugar Bees did not breed their own entity type");
		require(helper, bee.isFood(new ItemStack(
						CakeWorldItems.SPRINKLE_SEEDS.get()))
						&& new ItemStack(
								CakeWorldItems.SPRINKLE_SEEDS.get())
								.is(ItemTags.FLOWERS)
						&& CakeWorldBlocks.CANDY_SPROUT.get()
								.defaultBlockState().is(BlockTags.FLOWERS),
				"Candy Sprouts and Sprinkle Seeds lost their pollinator flower contracts");

		BlockPos flower = helper.absolutePos(new BlockPos(2, 2, 2));
		bee.setSavedFlowerPos(flower);
		CompoundTag saved = new CompoundTag();
		bee.addAdditionalSaveData(saved);
		SugarBee restored =
				CakeWorldEntities.SUGAR_BEE.get().create(helper.getLevel());
		require(helper, restored != null, "Could not restore Sugar Bee");
		restored.readAdditionalSaveData(saved);
		require(helper, restored.hasSavedFlowerPos()
						&& flower.equals(restored.getSavedFlowerPos()),
				"Sugar Bee lost its pollination target through save/load");

		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Sugar Bee sting target");
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				require(helper, bee.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.GLOWING)
								&& !target.hasEffect(
										net.minecraft.world.effect.MobEffects.POISON)
								&& !bee.hasStung(),
						safeDifficulty
								+ " Sugar Bee caused sting damage/poison or lost its visible pollen response");
			}
			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			require(helper, bee.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& bee.hasStung(),
					"Hard Sugar Bee did not restore the real sting");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect Sugar Bee biome");
			requireSpawnReplacement(helper, loaded, EntityType.BEE,
					CakeWorldEntities.SUGAR_BEE.get(),
					MobCategory.CREATURE);
		}

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2003"),
						"CakeWorldSugarBeeRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.SUGAR_BEE.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:bee");
		require(helper, CakeWorldItems.SUGAR_BEE_SPAWN_EGG.isPresent(),
				"Sugar Bee has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cinnamonSparkUsesSafePuffsUntilHard(
			GameTestHelper helper) {
		CinnamonSpark spark =
				CakeWorldEntities.CINNAMON_SPARK.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, spark != null && target != null,
				"Could not create Cinnamon Spark attack fixtures");
		BlockPos centre = helper.absolutePos(new BlockPos(3, 3, 3));
		spark.setPos(centre.getX(), centre.getY(), centre.getZ());
		target.setPos(centre.getX() + 3.0D, centre.getY(),
				centre.getZ());
		helper.getLevel().addFreshEntity(spark);
		helper.getLevel().addFreshEntity(target);
		AABB attackArea = new AABB(centre).inflate(12.0D);

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				spark.performRangedAttack(target, 1.0F);
				java.util.List<CinnamonPuffProjectile> puffs =
						helper.getLevel().getEntitiesOfClass(
								CinnamonPuffProjectile.class,
								attackArea);
				require(helper, !puffs.isEmpty(),
						safeDifficulty
								+ " Cinnamon Spark did not launch a safe puff");
				CinnamonPuffProjectile puff =
						puffs.get(puffs.size() - 1);
				puff.applyHarmlessPuff(target);
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE)
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " Cinnamon Puff caused damage or lacked rescue/knockback behavior");
				puff.discard();
			}

			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			spark.performRangedAttack(target, 1.0F);
			java.util.List<SmallFireball> fireballs =
					helper.getLevel().getEntitiesOfClass(
							SmallFireball.class, attackArea);
			require(helper, !fireballs.isEmpty(),
					"Hard Cinnamon Spark did not launch a real fireball");
			require(helper, spark.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Cinnamon Spark melee did not restore real damage");
			fireballs.forEach(fireball -> fireball.discard());
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome fudgeWastes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes Cinnamon Spark spawning");
		requireSpawnReplacement(helper, fudgeWastes, EntityType.BLAZE,
				CakeWorldEntities.CINNAMON_SPARK.get(),
				MobCategory.MONSTER);
		require(helper, CakeWorldItems.CINNAMON_SPARK_SPAWN_EGG.isPresent(),
				"Cinnamon Spark has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2004"),
						"CakeWorldCinnamonSparkRoleTest"));
		VanillaRoleAdvancements.creditKilledBlazeRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:blaze");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sodaCodSchoolAndReturnFromLemonadeBuckets(
			GameTestHelper helper) {
		SodaCod leader = CakeWorldEntities.SODA_COD.get()
				.create(helper.getLevel());
		SodaCod follower = CakeWorldEntities.SODA_COD.get()
				.create(helper.getLevel());
		require(helper, leader != null && follower != null,
				"Could not create Soda Cod schooling fixtures");
		follower.startFollowing(leader);
		require(helper, follower.isFollower()
						&& follower.inRangeOfLeader()
						&& leader.getMaxSchoolSize() > 1,
				"Soda Cod lost the inherited schooling role");

		BlockPos horizontalAnchor =
				helper.absolutePos(new BlockPos(3, 3, 3));
		BlockPos lemonadePos = new BlockPos(horizontalAnchor.getX(),
				helper.getLevel().getSeaLevel() - 5,
				horizontalAnchor.getZ());
		for (int y = -1; y <= 1; y++) {
			helper.getLevel().setBlock(lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
		}
		boolean vanillaRule =
				WaterAnimal.checkSurfaceWaterAnimalSpawnRules(
						CakeWorldEntities.SODA_COD.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						lemonadePos, new Random(1978L));
		boolean sodaRule = SodaCod.checkSodaCodSpawnRules(
				CakeWorldEntities.SODA_COD.get(), helper.getLevel(),
				MobSpawnType.NATURAL, lemonadePos,
				new Random(1978L));
		require(helper, !vanillaRule && sodaRule,
				"Soda Cod did not replace vanilla's hard-coded water-block spawn check with a Lemonade-compatible water-tag check");

		leader.setCustomName(new TextComponent("Fizz"));
		ItemStack bucket = leader.getBucketItemStack();
		leader.saveToBucketTag(bucket);
		require(helper,
				bucket.is(CakeWorldItems.SODA_COD_BUCKET.get())
						&& bucket.hasCustomHoverName(),
				"Soda Cod did not capture into its dedicated named bucket");
		AABB releaseArea = new AABB(lemonadePos).inflate(2.0D);
		helper.getLevel().getEntitiesOfClass(SodaCod.class, releaseArea)
				.forEach(SodaCod::discard);
		((MobBucketItem) bucket.getItem()).checkExtraContent(null,
				helper.getLevel(), bucket, lemonadePos);
		java.util.List<SodaCod> released =
				helper.getLevel().getEntitiesOfClass(SodaCod.class,
						releaseArea);
		require(helper, released.size() == 1
						&& released.get(0).fromBucket()
						&& released.get(0).hasCustomName()
						&& "Fizz".equals(released.get(0)
								.getCustomName().getString()),
				"Soda Cod bucket released the wrong entity or lost bucket/name data");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Soda Cod spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.COD,
				CakeWorldEntities.SODA_COD.get(),
				MobCategory.WATER_AMBIENT);
		require(helper, CakeWorldItems.SODA_COD_SPAWN_EGG.isPresent(),
				"Soda Cod has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void popRockPoppersStayHarmlessUntilHard(
			GameTestHelper helper) {
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		boolean originalMobGriefing = helper.getLevel().getGameRules()
				.getBoolean(GameRules.RULE_MOBGRIEFING);
		BlockPos centre = helper.absolutePos(new BlockPos(3, 3, 3));
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				PopRockPopper popper =
						CakeWorldEntities.POP_ROCK_POPPER.get()
								.create(helper.getLevel());
				Pig target = EntityType.PIG.create(helper.getLevel());
				require(helper, popper != null && target != null,
						"Could not create Pop-Rock Popper safety fixtures");
				popper.setNoAi(true);
				popper.setPos(centre.getX(), centre.getY(),
						centre.getZ());
				target.setPos(centre.getX() + 2.0D, centre.getY(),
						centre.getZ());
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 20.0F;
				target.setDeltaMovement(Vec3.ZERO);
				BlockPos protectedBlock = centre.offset(0, 0, 1);
				helper.getLevel().setBlock(protectedBlock,
						Blocks.GLASS.defaultBlockState(), 3);
				ItemEntity protectedItem = new ItemEntity(
						helper.getLevel(), centre.getX(),
						centre.getY(), centre.getZ() + 1.5D,
						new ItemStack(Items.DIAMOND, 3));
				protectedItem.setDeltaMovement(Vec3.ZERO);
				helper.getLevel().addFreshEntity(popper);
				helper.getLevel().addFreshEntity(target);
				helper.getLevel().addFreshEntity(protectedItem);

				popper.ignite();
				for (int tick = 0; tick < 31
						&& !popper.isRemoved(); tick++) {
					popper.tick();
				}
				require(helper, popper.isRemoved(),
						safeDifficulty
								+ " Pop-Rock Popper did not complete its safe fuse");
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F,
						safeDifficulty
								+ " safe pop caused health/fire/fall damage");
				require(helper,
						target.hasEffect(MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement().x > 0.0D
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " safe pop lacked controlled knockback and landing protection");
				require(helper,
						helper.getLevel().getBlockState(protectedBlock)
								.is(Blocks.GLASS)
								&& !protectedItem.isRemoved()
								&& protectedItem.getItem()
										.is(Items.DIAMOND)
								&& protectedItem.getItem().getCount() == 3
								&& protectedItem.getDeltaMovement()
										.equals(Vec3.ZERO),
						safeDifficulty
								+ " safe pop altered a block or item entity");
				target.discard();
				protectedItem.discard();
			}

			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(false, helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			PopRockPopper hardPopper =
					CakeWorldEntities.POP_ROCK_POPPER.get()
							.create(helper.getLevel());
			Pig hardTarget = EntityType.PIG.create(helper.getLevel());
			require(helper, hardPopper != null && hardTarget != null,
					"Could not create Hard Pop-Rock Popper fixtures");
			hardPopper.setNoAi(true);
			hardPopper.setPos(centre.getX(), centre.getY(),
					centre.getZ());
			hardTarget.setPos(centre.getX() + 2.0D, centre.getY(),
					centre.getZ());
			hardTarget.setHealth(10.0F);
			helper.getLevel().addFreshEntity(hardPopper);
			helper.getLevel().addFreshEntity(hardTarget);
			hardPopper.ignite();
			for (int tick = 0; tick < 31
					&& !hardPopper.isRemoved(); tick++) {
				hardPopper.tick();
			}
			require(helper,
					hardPopper.isRemoved()
							&& hardTarget.getHealth() < 10.0F,
					"Hard Pop-Rock Popper did not retain a real Creeper explosion");
			hardTarget.discard();
		} finally {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(originalMobGriefing,
							helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect Pop-Rock Popper biome role");
			requireSpawnReplacement(helper, loaded, EntityType.CREEPER,
					CakeWorldEntities.POP_ROCK_POPPER.get(),
					MobCategory.MONSTER);
		}
		require(helper,
				CakeWorldItems.POP_ROCK_POPPER_SPAWN_EGG.isPresent(),
				"Pop-Rock Popper has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2010"),
						"CakeWorldPopRockRoleTest"));
		VanillaRoleAdvancements.creditKilledCreeperRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:creeper");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sodaDolphinsGuideSafelyThroughLemonadeUntilHard(
			GameTestHelper helper) {
		SodaDolphin dolphin = CakeWorldEntities.SODA_DOLPHIN.get()
				.create(helper.getLevel());
		SodaDolphin loaded = CakeWorldEntities.SODA_DOLPHIN.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, dolphin != null && loaded != null
						&& target != null,
				"Could not create Soda Dolphin fixtures");

		Player feeder = helper.makeMockPlayer();
		feeder.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.COD, 2));
		InteractionResult feedResult = dolphin.interact(
				feeder, InteractionHand.MAIN_HAND);
		require(helper,
				feedResult.consumesAction()
						&& dolphin.gotFish()
						&& feeder.getItemInHand(
								InteractionHand.MAIN_HAND)
								.getCount() == 1,
				"Soda Dolphin did not retain the fish-fed treasure-guide trigger");

		BlockPos treasure = new BlockPos(1978, 42, -2011);
		dolphin.setTreasurePos(treasure);
		dolphin.setMoisntessLevel(1234);
		CompoundTag saved = new CompoundTag();
		dolphin.addAdditionalSaveData(saved);
		loaded.readAdditionalSaveData(saved);
		require(helper,
				loaded.gotFish()
						&& loaded.getTreasurePos().equals(treasure)
						&& loaded.getMoistnessLevel() == 1234
						&& loaded.getMaxAirSupply() == 4800
						&& !loaded.canBreatheUnderwater()
						&& loaded.canBeLeashed(feeder)
						&& Math.abs(loaded.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 1.2D)
								< 0.001D,
				"Soda Dolphin lost guide, moisture, air, leash or swimming-speed state");

		BlockPos horizontalAnchor =
				helper.absolutePos(new BlockPos(3, 3, 3));
		BlockPos lemonadePos = new BlockPos(horizontalAnchor.getX(),
				helper.getLevel().getSeaLevel() - 5,
				horizontalAnchor.getZ());
		for (int y = -1; y <= 1; y++) {
			helper.getLevel().setBlock(lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
		}
		boolean vanillaRule =
				WaterAnimal.checkSurfaceWaterAnimalSpawnRules(
						CakeWorldEntities.SODA_DOLPHIN.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						lemonadePos, new Random(1978L));
		boolean sodaRule = SodaDolphin.checkSodaDolphinSpawnRules(
				CakeWorldEntities.SODA_DOLPHIN.get(),
				helper.getLevel(), MobSpawnType.NATURAL,
				lemonadePos, new Random(1978L));
		require(helper, !vanillaRule && sodaRule,
				"Soda Dolphin did not replace vanilla's literal-water-block spawn check with a Lemonade-compatible water-tag check");

		dolphin.setPos(horizontalAnchor.getX(),
				horizontalAnchor.getY(), horizontalAnchor.getZ());
		target.setPos(horizontalAnchor.getX() + 2.0D,
				horizontalAnchor.getY(), horizontalAnchor.getZ());
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						dolphin.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.DOLPHINS_GRACE)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement().x > 0.0D
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " Soda Dolphin bump caused damage or lacked bubble rescue/swim effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper,
					dolphin.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Soda Dolphin did not retain real retaliatory damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Soda Dolphin spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.DOLPHIN,
				CakeWorldEntities.SODA_DOLPHIN.get(),
				MobCategory.WATER_CREATURE);
		require(helper, CakeWorldItems.SODA_DOLPHIN_SPAWN_EGG.isPresent(),
				"Soda Dolphin has no creative/testing spawn egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(new ResourceLocation(
						"minecraft", "adventure/kill_all_mobs"));
		require(helper, killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:dolphin"),
				"Vanilla unexpectedly added a Dolphin kill criterion; reassess compatibility before inventing a bridge");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void doughDonkeysArePersistentRideablePackAnimals(
			GameTestHelper helper) {
		DoughDonkey first = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		DoughDonkey second = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		require(helper, first != null && second != null,
				"Could not create Dough Donkey breeding fixtures");
		first.setTamed(true);
		second.setTamed(true);
		first.setInLove(null);
		second.setInLove(null);
		require(helper, first.canMate(second),
				"Two adult, tame, willing Dough Donkeys could not mate");
		AgeableMob child = first.getBreedOffspring(
				helper.getLevel(), second);
		require(helper, child instanceof DoughDonkey
						&& child.getType()
								== CakeWorldEntities.DOUGH_DONKEY.get(),
				"Dough Donkeys did not breed into their own entity type");

		GingerbreadPony transitionalHorse =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, transitionalHorse != null,
				"Could not create the staged horse-family fixture");
		AgeableMob transitionalMule = first.getBreedOffspring(
				helper.getLevel(), transitionalHorse);
		require(helper,
				transitionalMule instanceof MarzipanMule
						&& transitionalMule.getType()
								== CakeWorldEntities.MARZIPAN_MULE.get(),
				"Dough Donkey and Gingerbread Pony did not produce Marzipan Mule");

		DoughDonkey packAnimal = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		require(helper, packAnimal != null,
				"Could not create the Dough Donkey pack fixture");
		Player rider = helper.makeMockPlayer();
		packAnimal.setPos(rider.getX(), rider.getY(), rider.getZ());
		packAnimal.setTamed(true);
		packAnimal.setOwnerUUID(rider.getUUID());
		helper.getLevel().addFreshEntity(packAnimal);
		rider.getAbilities().instabuild = false;
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.CHEST));
		InteractionResult chestResult = packAnimal.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, chestResult.consumesAction()
						&& packAnimal.hasChest()
						&& packAnimal.getInventoryColumns() == 5
						&& rider.getMainHandItem().isEmpty(),
				"Dough Donkey did not equip and consume its survival chest");
		require(helper, packAnimal.getSlot(500).set(
						new ItemStack(Items.DIAMOND, 3)),
				"Dough Donkey rejected the first of its 15 pack slots");
		require(helper, packAnimal.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& packAnimal.isSaddled(),
				"Dough Donkey could not equip its saddle");
		rider.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		InteractionResult rideResult = packAnimal.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, rideResult.consumesAction()
						&& rider.getVehicle() == packAnimal
						&& packAnimal.hasPassenger(rider),
				"A tame adult Dough Donkey did not accept an empty-hand rider");
		rider.stopRiding();

		CompoundTag saved = new CompoundTag();
		packAnimal.addAdditionalSaveData(saved);
		DoughDonkey restored = CakeWorldEntities.DOUGH_DONKEY.get()
				.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create the Dough Donkey reload fixture");
		restored.readAdditionalSaveData(saved);
		ItemStack restoredPackItem = restored.getSlot(500).get();
		require(helper, restored.isTamed()
						&& rider.getUUID().equals(restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.isSaddled()
						&& restoredPackItem.is(Items.DIAMOND)
						&& restoredPackItem.getCount() == 3,
				"Dough Donkey lost owner, tame, chest, saddle, or pack contents across save/load");

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Dough Donkey spawning");
		requireSpawnReplacement(helper, candyPlains, EntityType.DONKEY,
				CakeWorldEntities.DOUGH_DONKEY.get(),
				MobCategory.CREATURE);
		require(helper, CakeWorldItems.DOUGH_DONKEY_SPAWN_EGG.isPresent(),
				"Dough Donkey has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2012"),
						"CakeWorldDoughDonkeyRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.DOUGH_DONKEY.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:donkey");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void soggyBiscuitsKeepAquaticRolesGentleUntilHard(
			GameTestHelper helper) {
		SoggyBiscuit biscuit = CakeWorldEntities.SOGGY_BISCUIT.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, biscuit != null && target != null,
				"Could not create Soggy Biscuit attack fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		biscuit.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		biscuit.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.TRIDENT));
		helper.getLevel().addFreshEntity(biscuit);
		helper.getLevel().addFreshEntity(target);

		BlockPos lemonadePos = anchor.offset(0, 0, 4);
		helper.getLevel().setBlock(lemonadePos,
				CakeWorldFluids.LEMONADE_BLOCK.get()
						.defaultBlockState(), 3);
		require(helper, SoggyBiscuit.isTaggedWater(
						helper.getLevel(), lemonadePos),
				"Soggy Biscuit's daylight water goal did not recognize Lemonade through the standard water tag");
		biscuit.setSwimming(true);
		require(helper, !biscuit.isPushedByFluid(),
				"Soggy Biscuit lost Drowned swimming-fluid control");
		biscuit.setSwimming(false);
		require(helper, biscuit.isPushedByFluid(),
				"Soggy Biscuit no longer resumes ordinary fluid pushing on land");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 8.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						biscuit.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.WATER_BREATHING)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Soggy Biscuit melee caused damage or lacked soggy/rescue effects");

				target.removeAllEffects();
				target.setHealth(10.0F);
				biscuit.performRangedAttack(target, 1.0F);
				java.util.List<SoggyTridentProjectile> safeTridents =
						helper.getLevel().getEntitiesOfClass(
								SoggyTridentProjectile.class,
								biscuit.getBoundingBox().inflate(4.0D));
				require(helper, safeTridents.size() == 1
								&& safeTridents.get(0).getType()
										== CakeWorldEntities.SOGGY_TRIDENT.get(),
						safeDifficulty
								+ " Soggy Biscuit did not throw its visible safe trident");
				require(helper,
						safeTridents.get(0).splash(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.WATER_BREATHING)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Soggy Trident caused damage or lacked its splash protection");
				safeTridents.get(0).discard();
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper, biscuit.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Soggy Biscuit did not retain real melee damage");
			biscuit.performRangedAttack(target, 1.0F);
			java.util.List<ThrownTrident> hardTridents =
					helper.getLevel().getEntitiesOfClass(
							ThrownTrident.class,
							biscuit.getBoundingBox().inflate(4.0D),
							trident -> trident.getType()
									== EntityType.TRIDENT);
			require(helper, hardTridents.size() == 1,
					"Hard Soggy Biscuit did not retain the genuine damaging vanilla trident projectile");
			hardTridents.forEach(ThrownTrident::discard);
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Drowned ruinDrowned = EntityType.DROWNED.create(
				helper.getLevel());
		require(helper, ruinDrowned != null,
				"Could not create the ocean-ruin conversion fixture");
		ResourceLocation fixtureBiome = helper.getLevel().getBiome(anchor)
				.unwrapKey().map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Ocean-ruin conversion fixture was not in a CakeWorld biome");
		ruinDrowned.moveTo(anchor.getX(), anchor.getY(), anchor.getZ(),
				37.0F, 0.0F);
		ruinDrowned.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.TRIDENT));
		ruinDrowned.setItemSlot(EquipmentSlot.OFFHAND,
				new ItemStack(Items.NAUTILUS_SHELL));
		ruinDrowned.setCustomName(new TextComponent("Sodden"));
		ruinDrowned.setPersistenceRequired();
		SoggyBiscuit converted =
				CakeWorldDrownedReplacement.convertIfInCakeWorldBiome(
						helper.getLevel(), ruinDrowned);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities.SOGGY_BISCUIT.get()
						&& converted.getMainHandItem().is(Items.TRIDENT)
						&& converted.getOffhandItem().is(
								Items.NAUTILUS_SHELL)
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Sodden")
						&& converted.isPersistenceRequired()
						&& Math.abs(converted.getYRot() - 37.0F)
								< 0.001F,
				"Ocean-ruin/Zombie conversion lost Drowned type, equipment, name, persistence or rotation");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Soggy Biscuit spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.DROWNED,
				CakeWorldEntities.SOGGY_BISCUIT.get(),
				MobCategory.MONSTER);
		TagKey<EntityType<?>> axolotlHostiles = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_always_hostiles"));
		require(helper,
				CakeWorldEntities.SOGGY_BISCUIT.get()
						.is(axolotlHostiles),
				"Soggy Biscuit did not preserve Drowned's axolotl-hostile tag role");
		require(helper,
				CakeWorldItems.SOGGY_BISCUIT_SPAWN_EGG.isPresent(),
				"Soggy Biscuit has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2013"),
						"CakeWorldSoggyBiscuitRoleTest"));
		VanillaRoleAdvancements.creditKilledDrownedRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:drowned");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void grandGumballGuardiansKeepPalaceRolesGentleUntilHard(
			GameTestHelper helper) {
		GrandGumballGuardian guardian =
				CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get()
						.create(helper.getLevel());
		Pig safeTarget = EntityType.PIG.create(helper.getLevel());
		require(helper, guardian != null && safeTarget != null,
				"Could not create Grand Gumball Guardian fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		guardian.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		safeTarget.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(guardian);
		helper.getLevel().addFreshEntity(safeTarget);
		require(helper,
				guardian instanceof ElderGuardian
						&& guardian.isPersistenceRequired()
						&& guardian.canBreatheUnderwater()
						&& guardian.getMobType() == MobType.WATER
						&& guardian.getAttackDuration() == 60
						&& Math.abs(guardian.getMaxHealth() - 80.0F)
								< 0.001F
						&& Math.abs(guardian.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 8.0D)
								< 0.001D
						&& Math.abs(guardian.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.3D)
								< 0.001D,
				"Grand Gumball Guardian lost the persistent Elder Guardian water, beam, fatigue-boss or attribute contract");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 8.0F;
				safeTarget.setDeltaMovement(Vec3.ZERO);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.indirectMagic(
								guardian, guardian), 13.0F);
				require(helper,
						Math.abs(safeTarget.getHealth()
										- 10.0F) < 0.001F
								&& !safeTarget.isOnFire()
								&& safeTarget.fallDistance == 0.0F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.getEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
										.getAmplifier() == 1
								&& safeTarget.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Grand Gumball beam caused health damage or lacked sticky/rescue effects");

				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.thorns(guardian), 2.0F);
				require(helper,
						Math.abs(safeTarget.getHealth()
										- 10.0F) < 0.001F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Grand Gumball thorns caused health damage or lost their safe bounce");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget = EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard guardian target");
			hardTarget.setPos(anchor.getX() + 5.0D,
					anchor.getY(), anchor.getZ());
			hardTarget.setHealth(10.0F);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper,
					hardTarget.hurt(DamageSource.indirectMagic(
									guardian, guardian), 5.0F)
							&& hardTarget.getHealth() < 10.0F,
					"Hard Grand Gumball Guardian did not retain real beam damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		ElderGuardian monumentGuardian =
				EntityType.ELDER_GUARDIAN.create(helper.getLevel());
		require(helper, monumentGuardian != null,
				"Could not create ocean-monument conversion fixture");
		BlockPos conversionAnchor =
				findCakeWorldBiomePosition(helper,
						anchor, 64);
		require(helper, conversionAnchor != null,
				"Could not locate a runtime CakeWorld biome for the ocean-monument conversion fixture");
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(conversionAnchor).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Ocean-monument conversion fixture was not in a CakeWorld biome");
		monumentGuardian.moveTo(conversionAnchor.getX(),
				conversionAnchor.getY(),
				conversionAnchor.getZ(), 29.0F, 0.0F);
		monumentGuardian.setHealth(37.0F);
		monumentGuardian.setCustomName(
				new TextComponent("Grand Gumdrop"));
		monumentGuardian.setPersistenceRequired();
		GrandGumballGuardian converted =
				CakeWorldElderGuardianReplacement
						.convertIfInCakeWorldBiome(
								helper.getLevel(),
								monumentGuardian);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities
										.GRAND_GUMBALL_GUARDIAN.get()
						&& Math.abs(converted.getHealth() - 37.0F)
								< 0.001F
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Grand Gumdrop")
						&& converted.isPersistenceRequired()
						&& Math.abs(converted.getYRot() - 29.0F)
								< 0.001F,
				"Ocean-monument conversion lost boss type, health, name, persistence or rotation");

		TagKey<EntityType<?>> axolotlHostiles = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_always_hostiles"));
		require(helper,
				CakeWorldEntities.GRAND_GUMBALL_GUARDIAN.get()
						.is(axolotlHostiles),
				"Grand Gumball Guardian did not preserve Elder Guardian's axolotl-hostile tag role");
		require(helper,
				CakeWorldItems.GRAND_GUMBALL_GUARDIAN_SPAWN_EGG
						.isPresent(),
				"Grand Gumball Guardian has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2014"),
						"CakeWorldGrandGuardianRoleTest"));
		VanillaRoleAdvancements.creditKilledElderGuardianRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:elder_guardian");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gumballGuardiansSignalPalaceBeamsGentleUntilHard(
			GameTestHelper helper) {
		GumballGuardian guardian =
				CakeWorldEntities.GUMBALL_GUARDIAN.get()
						.create(helper.getLevel());
		Pig safeTarget = EntityType.PIG.create(helper.getLevel());
		require(helper, guardian != null && safeTarget != null,
				"Could not create Gumball Guardian fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		guardian.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		safeTarget.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		guardian.setNoGravity(true);
		safeTarget.setNoGravity(true);
		helper.getLevel().addFreshEntity(guardian);
		helper.getLevel().addFreshEntity(safeTarget);
		require(helper,
				guardian instanceof Guardian
						&& !ElderGuardian.class
								.isInstance(guardian)
						&& guardian.canBreatheUnderwater()
						&& guardian.getMobType() == MobType.WATER
						&& guardian.getAttackDuration() == 80
						&& close(guardian.getDimensions(Pose.STANDING)
								.width, 0.85D)
						&& close(guardian.getDimensions(Pose.STANDING)
								.height, 0.85D)
						&& close(guardian.getMaxHealth(), 30.0D)
						&& close(guardian.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 6.0D)
						&& close(guardian.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.5D)
						&& close(guardian.getAttributeValue(
								Attributes.FOLLOW_RANGE), 16.0D),
				"Gumball Guardian lost the ordinary Guardian water, size, beam, or attribute contract");

		guardian.setTarget(safeTarget);
		for (int tick = 0;
				tick < 30 && !guardian.hasActiveAttackTarget();
				++tick) {
			guardian.tick();
		}
		require(helper,
				guardian.hasActiveAttackTarget()
						&& guardian.getActiveAttackTarget()
								== safeTarget,
				"Gumball Guardian did not expose the inherited visible beam target before impact");
		guardian.setTarget(null);
		guardian.setNoAi(true);

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 8.0F;
				safeTarget.setDeltaMovement(Vec3.ZERO);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(DamageSource.indirectMagic(
						guardian, guardian), 3.0F);
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& !safeTarget.isOnFire()
								&& safeTarget.fallDistance == 0.0F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Gumball beam contract failed: health="
								+ safeTarget.getHealth()
								+ ", fire=" + safeTarget.isOnFire()
								+ ", fall=" + safeTarget.fallDistance
								+ ", slow=" + safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								+ ", miningSlow=" + safeTarget.hasEffect(
										MobEffects.DIG_SLOWDOWN)
								+ ", slowFall=" + safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								+ ", fireResist=" + safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								+ ", resistance=" + safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE));

				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.mobAttack(guardian), 6.0F);
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Gumball beam contact caused health damage or lost its safe impact");

				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.invulnerableTime = 0;
				safeTarget.hurt(
						DamageSource.thorns(guardian), 2.0F);
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Gumball thorns caused health damage or lost their safe impact");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Gumball target");
			hardTarget.setPos(anchor.getX() + 5.0D,
					anchor.getY(), anchor.getZ());
			hardTarget.setHealth(10.0F);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper,
					hardTarget.hurt(
							DamageSource.mobAttack(guardian),
							6.0F)
							&& hardTarget.getHealth() < 10.0F,
					"Hard Gumball Guardian did not retain real beam-contact damage");

			BlockPos lemonadePos = anchor.offset(0, 0, 6);
			helper.getLevel().setBlock(lemonadePos.below(),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
			helper.getLevel().setBlock(lemonadePos,
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
			helper.getLevel().setBlock(lemonadePos.above(3),
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState(), 3);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			require(helper,
					Guardian.checkGuardianSpawnRules(
							CakeWorldEntities.GUMBALL_GUARDIAN.get(),
							helper.getLevel(),
							MobSpawnType.SPAWNER,
							lemonadePos,
							new Random(24L))
							&& SpawnPlacements.getPlacementType(
									CakeWorldEntities
											.GUMBALL_GUARDIAN.get())
									== SpawnPlacements.Type.IN_WATER,
					"Gumball Guardian lost the covered tagged-water spawn contract for Lemonade");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Guardian monumentGuardian =
				EntityType.GUARDIAN.create(helper.getLevel());
		require(helper, monumentGuardian != null,
				"Could not create monument Guardian conversion fixture");
		BlockPos monumentAnchor =
				findCakeWorldBiomePosition(
						helper, anchor, 512);
		require(helper, monumentAnchor != null,
				"Could not locate a generated CakeWorld biome for monument Guardian conversion");
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(monumentAnchor).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Monument Guardian conversion fixture was not in a CakeWorld biome");
		monumentGuardian.moveTo(monumentAnchor.getX(),
				monumentAnchor.getY(),
				monumentAnchor.getZ(), 31.0F, 0.0F);
		monumentGuardian.setHealth(17.0F);
		monumentGuardian.setCustomName(
				new TextComponent("Bubble Sentinel"));
		monumentGuardian.setPersistenceRequired();
		GumballGuardian converted =
				CakeWorldGuardianReplacement
						.convertIfInCakeWorldBiome(
								helper.getLevel(),
								monumentGuardian);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities
										.GUMBALL_GUARDIAN.get()
						&& close(converted.getHealth(), 17.0D)
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Bubble Sentinel")
						&& converted.isPersistenceRequired()
						&& close(converted.getYRot(), 31.0D),
				"Monument conversion lost Guardian type, health, name, persistence or rotation");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Guardian spawning");
		boolean openOceanGuardian = false;
		boolean openOceanGumball = false;
		for (MobSpawnSettings.SpawnerData spawn :
				sodaOcean.getMobSettings()
						.getMobs(MobCategory.MONSTER).unwrap()) {
			openOceanGuardian |= spawn.type == EntityType.GUARDIAN;
			openOceanGumball |= spawn.type
					== CakeWorldEntities.GUMBALL_GUARDIAN.get();
		}
		require(helper,
				!openOceanGuardian && !openOceanGumball,
				"Structure-only Gumball defenders leaked into normal open-ocean spawning before Soda Palace exists");

		TagKey<EntityType<?>> axolotlHostiles = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_always_hostiles"));
		require(helper,
				CakeWorldEntities.GUMBALL_GUARDIAN.get()
						.is(axolotlHostiles),
				"Gumball Guardian did not preserve Guardian's axolotl-hostile role");
		require(helper,
				CakeWorldItems.GUMBALL_GUARDIAN_SPAWN_EGG
						.isPresent(),
				"Gumball Guardian has no creative/testing spawn egg");
		require(helper,
				guardian.getLootTable().equals(
						new ResourceLocation(CakeWorld.MODID,
								"entities/gumball_guardian")),
				"Gumball Guardian did not resolve its dedicated Guardian-equivalent loot");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2024"),
						"CakeWorldGumballGuardianRoleTest"));
		VanillaRoleAdvancements.creditKilledGuardianRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:guardian");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void taffyTallwalkersTeleportAndCarryWithoutSafeGrief(
			GameTestHelper helper) {
		TaffyTallwalker tallwalker =
				CakeWorldEntities.TAFFY_TALLWALKER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, tallwalker != null && target != null,
				"Could not create Taffy Tallwalker fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		tallwalker.setNoAi(true);
		tallwalker.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(tallwalker);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				tallwalker instanceof EnderMan
						&& tallwalker.isSensitiveToWater()
						&& Math.abs(tallwalker.getMaxHealth() - 40.0F)
								< 0.001F
						&& Math.abs(tallwalker.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 7.0D)
								< 0.001D
						&& Math.abs(tallwalker.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 64.0D)
								< 0.001D,
				"Taffy Tallwalker lost the Enderman teleport/water, health, attack or follow-range role");

		UUID angerTarget = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2016");
		tallwalker.setRemainingPersistentAngerTime(321);
		tallwalker.setPersistentAngerTarget(angerTarget);
		tallwalker.setCarriedBlock(CakeWorldBlocks.ICING_LAYER.get()
				.defaultBlockState());
		require(helper, tallwalker.requiresCustomPersistence(),
				"A block-carrying Taffy Tallwalker was not persistent");
		CompoundTag saved = tallwalker.saveWithoutId(new CompoundTag());
		TaffyTallwalker restored =
				CakeWorldEntities.TAFFY_TALLWALKER.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Taffy Tallwalker NBT fixture");
		restored.load(saved);
		require(helper,
				restored.getCarriedBlock() != null
						&& restored.getCarriedBlock().is(
								CakeWorldBlocks.ICING_LAYER.get())
						&& restored.getRemainingPersistentAngerTime()
								== 321
						&& angerTarget.equals(
								restored.getPersistentAngerTarget()),
				"Taffy Tallwalker lost its carried block or persistent anger through NBT");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		boolean originalMobGriefing = helper.getLevel().getGameRules()
				.getBoolean(GameRules.RULE_MOBGRIEFING);
		try {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(true, helper.getLevel().getServer());
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						tallwalker.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.LEVITATION)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Taffy Tallwalker attack caused damage or lacked void-rescue effects");
				require(helper,
						!ForgeEventFactory.getMobGriefingEvent(
								helper.getLevel(), tallwalker),
						safeDifficulty
								+ " Taffy Tallwalker could take or place blocks");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper,
					tallwalker.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Taffy Tallwalker did not retain real Enderman damage");
			require(helper,
					ForgeEventFactory.getMobGriefingEvent(
							helper.getLevel(), tallwalker),
					"Hard Taffy Tallwalker did not honor the enabled mobGriefing rule");
		} finally {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(originalMobGriefing,
							helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId()}) {
			Biome loaded = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, loaded != null,
					"Could not inspect Taffy Tallwalker biome role");
			requireSpawnReplacement(helper, loaded,
					EntityType.ENDERMAN,
					CakeWorldEntities.TAFFY_TALLWALKER.get(),
					MobCategory.MONSTER);
		}
		require(helper,
				CakeWorldItems.TAFFY_TALLWALKER_SPAWN_EGG.isPresent(),
				"Taffy Tallwalker has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed3016"),
						"CakeWorldTaffyTallwalkerRoleTest"));
		VanillaRoleAdvancements.creditKilledEndermanRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:enderman");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sugarMitesKeepPearlLifetimeAndGentleBites(
			GameTestHelper helper) {
		SugarMite mite = CakeWorldEntities.SUGAR_MITE.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, mite != null && target != null,
				"Could not create Sugar Mite fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		mite.setNoAi(true);
		mite.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(mite);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				mite instanceof Endermite
						&& mite.getMobType() == MobType.ARTHROPOD
						&& Math.abs(mite.getMaxHealth() - 8.0F)
								< 0.001F
						&& Math.abs(mite.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 2.0D)
								< 0.001D
						&& Math.abs(mite.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.25D)
								< 0.001D,
				"Sugar Mite lost the Endermite arthropod, health, attack or movement role");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						mite.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Sugar Mite bite caused damage or lacked rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper,
					mite.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Sugar Mite did not retain real Endermite damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Endermite pearlMite = EntityType.ENDERMITE.create(
				helper.getLevel());
		require(helper, pearlMite != null,
				"Could not create Ender Pearl conversion fixture");
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(anchor).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Ender Pearl conversion fixture was not in a CakeWorld biome");
		CompoundTag pearlState = pearlMite.saveWithoutId(
				new CompoundTag());
		pearlState.putInt("Lifetime", 123);
		pearlMite.load(pearlState);
		pearlMite.moveTo(anchor.getX(), anchor.getY(),
				anchor.getZ(), 17.0F, 0.0F);
		pearlMite.setCustomName(new TextComponent("Spark"));
		SugarMite converted =
				CakeWorldEndermiteReplacement
						.convertIfInCakeWorldBiome(
								helper.getLevel(), pearlMite);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities.SUGAR_MITE.get()
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Spark")
						&& Math.abs(converted.getYRot() - 17.0F)
								< 0.001F,
				"Ender Pearl conversion lost Sugar Mite type, name or rotation");
		CompoundTag convertedState = converted.saveWithoutId(
				new CompoundTag());
		require(helper, convertedState.getInt("Lifetime") == 123,
				"Ender Pearl conversion reset the Endermite lifetime");

		SugarMite expiring = CakeWorldEntities.SUGAR_MITE.get()
				.create(helper.getLevel());
		require(helper, expiring != null,
				"Could not create Sugar Mite lifetime fixture");
		CompoundTag expiringState = expiring.saveWithoutId(
				new CompoundTag());
		expiringState.putInt("Lifetime", 2399);
		expiring.load(expiringState);
		expiring.setNoAi(true);
		expiring.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(expiring);
		expiring.aiStep();
		require(helper, expiring.isRemoved(),
				"Sugar Mite did not retain the 2,400-tick Endermite lifetime");

		TagKey<EntityType<?>> powderSnowWalkers = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"powder_snow_walkable_mobs"));
		require(helper, CakeWorldEntities.SUGAR_MITE.get()
						.is(powderSnowWalkers),
				"Sugar Mite did not preserve Endermite's powder-snow walking tag role");
		require(helper, CakeWorldItems.SUGAR_MITE_SPAWN_EGG.isPresent(),
				"Sugar Mite has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2017"),
						"CakeWorldSugarMiteRoleTest"));
		VanillaRoleAdvancements.creditKilledEndermiteRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:endermite");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void sourSorcerersKeepRaidsAndGentleSpells(
			GameTestHelper helper) {
		SourSorcerer sorcerer = CakeWorldEntities.SOUR_SORCERER.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		Vex vex = EntityType.VEX.create(helper.getLevel());
		require(helper, sorcerer != null && target != null && vex != null,
				"Could not create Sour Sorcerer spell fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		sorcerer.setNoAi(true);
		sorcerer.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		vex.setNoAi(true);
		vex.setPos(anchor.getX() - 1.0D,
				anchor.getY(), anchor.getZ());
		vex.setOwner(sorcerer);
		helper.getLevel().addFreshEntity(sorcerer);
		helper.getLevel().addFreshEntity(target);
		helper.getLevel().addFreshEntity(vex);
		require(helper,
				sorcerer instanceof Evoker
						&& sorcerer.getMobType() == MobType.ILLAGER
						&& Math.abs(sorcerer.getMaxHealth() - 24.0F)
								< 0.001F
						&& Math.abs(sorcerer.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.5D)
								< 0.001D
						&& Math.abs(sorcerer.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 12.0D)
								< 0.001D
						&& sorcerer.isAlliedTo(vex),
				"Sour Sorcerer lost the Evoker illager, attribute or summoned-Vex alliance role");

		EvokerFangs fangs = new EvokerFangs(helper.getLevel(),
				target.getX(), target.getY(), target.getZ(),
				0.0F, 0, sorcerer);
		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(DamageSource.indirectMagic(
						fangs, sorcerer), 6.0F);
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(MobEffects.CONFUSION)
								&& target.hasEffect(MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Sour Sorcerer fangs caused damage or lacked rescue effects");

				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.hurt(DamageSource.mobAttack(vex), 4.0F);
				require(helper,
						Math.abs(target.getHealth() - 10.0F) < 0.001F
								&& target.hasEffect(MobEffects.CONFUSION)
								&& target.hasEffect(
										MobEffects.DAMAGE_RESISTANCE),
						safeDifficulty
								+ " Sour Sorcerer summon caused health damage");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.hurt(DamageSource.indirectMagic(
					fangs, sorcerer), 6.0F);
			require(helper, target.getHealth() < 10.0F,
					"Hard Sour Sorcerer fangs did not retain real damage");
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.hurt(DamageSource.mobAttack(vex), 4.0F);
			require(helper, target.getHealth() < 10.0F,
					"Hard Sour Sorcerer summon did not retain real damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Evoker raidEvoker = EntityType.EVOKER.create(helper.getLevel());
		Ravager ravager = EntityType.RAVAGER.create(helper.getLevel());
		require(helper, raidEvoker != null && ravager != null,
				"Could not create raid conversion fixtures");
		BlockPos conversionAnchor =
				findCakeWorldBiomePosition(helper,
						anchor, 64);
		require(helper, conversionAnchor != null,
				"Could not find a runtime CakeWorld biome for Sour Sorcerer conversion");
		raidEvoker.moveTo(conversionAnchor.getX(),
				conversionAnchor.getY(),
				conversionAnchor.getZ(), 27.0F, 0.0F);
		raidEvoker.setCustomName(new TextComponent("Tang"));
		raidEvoker.setPersistenceRequired();
		CompoundTag castingState = raidEvoker.saveWithoutId(
				new CompoundTag());
		castingState.putInt("SpellTicks", 25);
		raidEvoker.load(castingState);
		ravager.setPos(conversionAnchor.getX(),
				conversionAnchor.getY(),
				conversionAnchor.getZ());
		helper.getLevel().addFreshEntity(ravager);
		Raid raid = new Raid(197825, helper.getLevel(),
				conversionAnchor);
		raid.joinRaid(3, raidEvoker, null, true);
		raid.setLeader(3, raidEvoker);
		helper.getLevel().addFreshEntity(raidEvoker);
		raidEvoker.startRiding(ravager, true);
		SourSorcerer converted =
				CakeWorldEvokerReplacement.replaceIfInCakeWorldBiome(
						helper.getLevel(), raidEvoker);
		require(helper, converted != null
						&& raidEvoker.isRemoved()
						&& converted.getType()
								== CakeWorldEntities.SOUR_SORCERER.get()
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Tang")
						&& converted.isPersistenceRequired()
						&& converted.isCastingSpell()
						&& Math.abs(converted.getYRot() - 27.0F)
								< 0.001F,
				"Raid conversion lost Sorcerer type, name, persistence, spell or rotation: converted="
						+ converted
						+ ", sourceBiome="
						+ helper.getLevel()
								.getBiome(conversionAnchor)
								.unwrapKey()
								.map(key -> key.location())
								.orElse(null));
		CompoundTag convertedState = converted.saveWithoutId(
				new CompoundTag());
		require(helper, convertedState.getInt("SpellTicks") == 25
						&& converted.getCurrentRaid() == raid
						&& converted.getWave() == 3
						&& raid.getLeader(3) == converted
						&& raid.getTotalRaidersAlive() == 1
						&& converted.getVehicle() == ravager,
				"Raid conversion lost spell ticks, wave, leader, count or Ravager seat");

		TagKey<EntityType<?>> raiders = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft", "raiders"));
		require(helper, CakeWorldEntities.SOUR_SORCERER.get()
						.is(raiders),
				"Sour Sorcerer did not preserve the vanilla raider tag role");
		require(helper, CakeWorldItems.SOUR_SORCERER_SPAWN_EGG.isPresent(),
				"Sour Sorcerer has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2018"),
						"CakeWorldSourSorcererRoleTest"));
		VanillaRoleAdvancements.creditKilledEvokerRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:evoker");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void peppermintFoxesKeepTrustSleepAndMintyPounces(
			GameTestHelper helper) {
		PeppermintFox fox = CakeWorldEntities.PEPPERMINT_FOX.get()
				.create(helper.getLevel());
		PeppermintFox partner = CakeWorldEntities.PEPPERMINT_FOX.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, fox != null && partner != null && target != null,
				"Could not create Peppermint Fox fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		fox.setNoAi(true);
		fox.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		partner.setNoAi(true);
		partner.setPos(anchor.getX() - 1.0D,
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(fox);
		helper.getLevel().addFreshEntity(partner);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				fox instanceof Fox
						&& Math.abs(fox.getMaxHealth() - 10.0F)
								< 0.001F
						&& Math.abs(fox.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.3D)
								< 0.001D
						&& Math.abs(fox.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 32.0D)
								< 0.001D
						&& Math.abs(fox.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 2.0D)
								< 0.001D,
				"Peppermint Fox lost the Fox health, movement, follow or pounce role");

		UUID trusted = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2019");
		for (PeppermintFox fixture :
				new PeppermintFox[] {fox, partner}) {
			fixture.setItemSlot(EquipmentSlot.MAINHAND,
					new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
			CompoundTag state = fixture.saveWithoutId(
					new CompoundTag());
			ListTag trustedList = new ListTag();
			trustedList.add(NbtUtils.createUUID(trusted));
			state.put("Trusted", trustedList);
			state.putBoolean("Sleeping", true);
			state.putBoolean("Sitting", true);
			state.putString("Type", Fox.Type.SNOW.getName());
			fixture.load(state);
		}
		CompoundTag savedFox = fox.saveWithoutId(new CompoundTag());
		ListTag savedTrusted = savedFox.getList("Trusted", 11);
		require(helper,
				fox.getFoxType() == Fox.Type.SNOW
						&& fox.isSleeping()
						&& fox.isSitting()
						&& fox.getItemBySlot(EquipmentSlot.MAINHAND)
								.is(CakeWorldItems.BOILED_SWEET.get())
						&& savedTrusted.size() == 1
						&& NbtUtils.loadUUID(savedTrusted.get(0))
								.equals(trusted),
				"Peppermint Fox lost climate type, sleep, carried sweet or trusted player NBT");
		require(helper,
				fox.isFood(new ItemStack(Items.SWEET_BERRIES))
						&& fox.isFood(new ItemStack(
								CakeWorldItems.BOILED_SWEET.get()))
						&& fox.isFood(new ItemStack(
								CakeWorldItems.MINT_WAFER.get())),
				"Peppermint Fox did not accept vanilla berries and tagged sweets");

		PeppermintFox child = fox.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper, child != null
						&& child.getType()
								== CakeWorldEntities.PEPPERMINT_FOX.get()
						&& child.getFoxType() == Fox.Type.SNOW,
				"Peppermint Fox breeding lost the custom type or parent climate");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						fox.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SPEED)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Peppermint Fox pounce caused damage or lacked mint rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			require(helper, fox.doHurtTarget(target)
							&& target.getHealth() < 10.0F,
					"Hard Peppermint Fox did not retain real Fox damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		TagKey<EntityType<?>> powderSnowWalkers = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"powder_snow_walkable_mobs"));
		require(helper, CakeWorldEntities.PEPPERMINT_FOX.get()
						.is(powderSnowWalkers),
				"Peppermint Fox did not preserve Fox's powder-snow walking role");
		require(helper, CakeWorldItems.PEPPERMINT_FOX_SPAWN_EGG.isPresent(),
				"Peppermint Fox has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2119"),
						"CakeWorldPeppermintFoxRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.PEPPERMINT_FOX.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:fox");

		Biome peppermintPinewoods = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(new ResourceLocation(CakeWorld.MODID,
						"peppermint_pinewoods"));
		require(helper, peppermintPinewoods == null,
				"Peppermint Pinewoods now exists; replace this dependency gate with exact Fox spawn-role proof");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void mallowFloatersUseSafePuffsUntilHard(
			GameTestHelper helper) {
		MallowFloater floater = CakeWorldEntities.MALLOW_FLOATER.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, floater != null && target != null,
				"Could not create Mallow Floater fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 4, 2));
		floater.setNoAi(true);
		floater.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(floater);
		helper.getLevel().addFreshEntity(target);
		CompoundTag floaterState = floater.saveWithoutId(
				new CompoundTag());
		floaterState.putByte("ExplosionPower", (byte)3);
		floater.load(floaterState);
		require(helper,
				floater instanceof Ghast
						&& floater.fireImmune()
						&& floater.getExplosionPower() == 3
						&& Math.abs(floater.getMaxHealth() - 10.0F)
								< 0.001F
						&& Math.abs(floater.getAttributeValue(
								Attributes.FOLLOW_RANGE) - 100.0D)
								< 0.001D
						&& floater.getMaxSpawnClusterSize() == 1,
				"Mallow Floater lost the Ghast fire, explosion, health, range or solitary-spawn role");

		BlockPos protectedBlock = anchor.offset(4, 0, 1);
		helper.getLevel().setBlockAndUpdate(
				protectedBlock, Blocks.GLASS.defaultBlockState());
		ItemEntity protectedItem = new ItemEntity(helper.getLevel(),
				target.getX(), target.getY(), target.getZ() + 1.0D,
				new ItemStack(Items.DIAMOND, 3));
		protectedItem.setDeltaMovement(Vec3.ZERO);
		helper.getLevel().addFreshEntity(protectedItem);

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				protectedItem.setDeltaMovement(Vec3.ZERO);
				AbstractHurtingProjectile shot =
						floater.createShot(target);
				require(helper, shot instanceof MallowPuffProjectile,
						safeDifficulty
								+ " Mallow Floater did not create a safe visible puff");
				shot.setPos(target.getX(), target.getY(),
						target.getZ());
				helper.getLevel().addFreshEntity(shot);
				((MallowPuffProjectile)shot).burst();
				require(helper,
						shot.isRemoved()
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& helper.getLevel().getBlockState(
										protectedBlock).is(Blocks.GLASS)
								&& protectedItem.isAlive()
								&& protectedItem.getItem().getCount() == 3
								&& protectedItem.getDeltaMovement()
										.equals(Vec3.ZERO),
						safeDifficulty
								+ " Mallow Puff caused damage/destruction or lacked rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			AbstractHurtingProjectile hardShot =
					floater.createShot(target);
			LargeFireball hardFireball =
					(LargeFireball)hardShot;
			CompoundTag hardShotState = hardShot.saveWithoutId(
					new CompoundTag());
			target.hurt(DamageSource.fireball(
					hardFireball, floater), 6.0F);
			require(helper,
					hardShot.getClass() == LargeFireball.class
							&& hardShotState.getByte(
									"ExplosionPower") == 3
							&& target.getHealth() < 10.0F,
					"Hard Mallow Floater did not restore a real power-three Ghast fireball");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Biome fudgeWastes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect the Mallow Floater's Fudge Wastes biome");
		requireSpawnReplacement(helper, fudgeWastes,
				EntityType.GHAST,
				CakeWorldEntities.MALLOW_FLOATER.get(),
				MobCategory.MONSTER);
		require(helper, CakeWorldItems.MALLOW_FLOATER_SPAWN_EGG.isPresent(),
				"Mallow Floater has no creative/testing spawn egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2020"),
						"CakeWorldMallowFloaterRoleTest"));
		MallowFloater returned = CakeWorldEntities.MALLOW_FLOATER.get()
				.create(helper.getLevel());
		require(helper, returned != null,
				"Could not create Return to Sender fixture");
		returned.setPos(anchor.getX() - 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(returned);
		MallowPuffProjectile reflected =
				new MallowPuffProjectile(helper.getLevel(),
						returned, 1.0D, 0.0D, 0.0D);
		reflected.setOwner(advancementPlayer);
		require(helper,
				returned.hurt(DamageSource.fireball(
						reflected, advancementPlayer), 1.0F)
						&& !returned.isAlive(),
				"Reflected Mallow Puff did not retain Ghast return-fire defeat");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:ghast");
		requireCriterion(helper, advancementPlayer,
				"minecraft:nether/return_to_sender",
				"killed_ghast");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void giantStaleCrumblersStaySummonOnly(
			GameTestHelper helper) {
		GiantStaleCrumbler giant =
				CakeWorldEntities.GIANT_STALE_CRUMBLER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, giant != null && target != null,
				"Could not create Giant Stale Crumbler fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		giant.setNoAi(true);
		giant.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(giant);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				giant instanceof Giant
						&& Math.abs(giant.getBbWidth() - 3.6F)
								< 0.001F
						&& Math.abs(giant.getBbHeight() - 12.0F)
								< 0.001F
						&& Math.abs(giant.getEyeHeight() - 10.440001F)
								< 0.001F
						&& Math.abs(giant.getMaxHealth() - 100.0F)
								< 0.001F
						&& Math.abs(giant.getAttributeValue(
								Attributes.MOVEMENT_SPEED) - 0.5D)
								< 0.001D
						&& Math.abs(giant.getAttributeValue(
								Attributes.ATTACK_DAMAGE) - 50.0D)
								< 0.001D,
				"Giant Stale Crumbler lost the Giant dimensions, eye height or attributes");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.setSecondsOnFire(5);
				target.fallDistance = 6.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						giant.doHurtTarget(target)
								&& Math.abs(target.getHealth()
										- 10.0F) < 0.001F
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Giant crumb stomp caused damage or lacked rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget = EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Giant target");
			hardTarget.setPos(anchor.getX() + 2.0D,
					anchor.getY(), anchor.getZ() + 2.0D);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper, giant.doHurtTarget(hardTarget)
							&& !hardTarget.isAlive(),
					"Hard Giant Stale Crumbler did not retain real fifty-point Giant damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Giant vanillaGiant = EntityType.GIANT.create(helper.getLevel());
		require(helper, vanillaGiant != null,
				"Could not create command-summon conversion fixture");
		vanillaGiant.moveTo(anchor.getX() + 5.0D,
				anchor.getY(), anchor.getZ(), 31.0F, 0.0F);
		vanillaGiant.setHealth(42.0F);
		vanillaGiant.setCustomName(new TextComponent("Old Crumb"));
		vanillaGiant.setPersistenceRequired();
		vanillaGiant.setNoAi(true);
		GiantStaleCrumbler converted =
				CakeWorldGiantReplacement.convertIfInCakeWorldBiome(
						helper.getLevel(), vanillaGiant);
		require(helper, converted != null
						&& converted.getType()
								== CakeWorldEntities.GIANT_STALE_CRUMBLER.get()
						&& Math.abs(converted.getHealth() - 42.0F)
								< 0.001F
						&& converted.hasCustomName()
						&& converted.getCustomName().getString()
								.equals("Old Crumb")
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& Math.abs(converted.getYRot() - 31.0F)
								< 0.001F,
				"Fresh command-Giant conversion lost type, health, name, persistence, NoAI or rotation");

		ResourceLocation[] cakeWorldBiomes = {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId()
		};
		for (ResourceLocation biomeId : cakeWorldBiomes) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Missing CakeWorld biome " + biomeId);
			boolean spawned = biome.getMobSettings()
					.getMobs(MobCategory.MONSTER).unwrap()
					.stream().anyMatch(spawn ->
							spawn.type == EntityType.GIANT
									|| spawn.type
											== CakeWorldEntities
													.GIANT_STALE_CRUMBLER
													.get());
			require(helper, !spawned,
					"Giant role was added to normal spawning in "
							+ biomeId);
		}
		require(helper,
				Registry.ITEM.get(new ResourceLocation(
						CakeWorld.MODID,
						"giant_stale_crumbler_spawn_egg"))
						== Items.AIR,
				"Command-only Giant unexpectedly received a spawn egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		require(helper, killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:giant"),
				"Vanilla unexpectedly requires a Giant kill criterion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void deepLiquoriceWeaversStayStickyUntilHard(
			GameTestHelper helper) {
		DeepLiquoriceWeaver weaver =
				CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, weaver != null && target != null,
				"Could not create Deep Liquorice Weaver attack fixtures");
		helper.getLevel().addFreshEntity(weaver);
		helper.getLevel().addFreshEntity(target);

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				target.removeAllEffects();
				target.setHealth(10.0F);
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				require(helper, weaver.doHurtTarget(target)
								&& Math.abs(target.getHealth() - 10.0F)
										< 0.001F
								&& !target.hasEffect(MobEffects.POISON)
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.DIG_SLOWDOWN),
						safeDifficulty
								+ " Weaver bite caused damage/poison or lacked sticky effects");
			}

			target.removeAllEffects();
			target.setHealth(10.0F);
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			require(helper, weaver.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& target.hasEffect(MobEffects.POISON),
					"Hard Weaver bite did not restore damage and poison");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId()}) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Weaver surface-spawn contract");
			for (MobSpawnSettings.SpawnerData spawn :
					biome.getMobSettings().getMobs(
							MobCategory.MONSTER).unwrap()) {
				require(helper,
						spawn.type != EntityType.CAVE_SPIDER
								&& spawn.type
										!= CakeWorldEntities.DEEP_LIQUORICE_WEAVER.get(),
						"Deep Liquorice Weaver was accidentally made a surface biome spawn");
			}
		}

		require(helper,
				CakeWorldItems.DEEP_LIQUORICE_WEAVER_SPAWN_EGG.isPresent(),
				"Deep Liquorice Weaver has no creative/testing spawn egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2006"),
						"CakeWorldWeaverRoleTest"));
		VanillaRoleAdvancements.creditKilledCaveSpiderRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:cave_spider");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void custardCatsKeepTamingAndCatalogueRoles(
			GameTestHelper helper) {
		CustardCat first = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		CustardCat second = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		require(helper, first != null && second != null,
				"Could not create Custard Cat breeding fixtures");
		UUID owner = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2005");
		first.setCatType(Cat.TYPE_BRITISH);
		second.setCatType(Cat.TYPE_JELLIE);
		first.setOwnerUUID(owner);
		second.setOwnerUUID(owner);
		first.setTame(true);
		second.setTame(true);
		first.setCollarColor(DyeColor.PINK);
		second.setCollarColor(DyeColor.YELLOW);

		Cat child = first.getBreedOffspring(helper.getLevel(), second);
		require(helper, child instanceof CustardCat
						&& child.getType()
								== CakeWorldEntities.CUSTARD_CAT.get(),
				"Custard Cats did not breed into their own entity type");
		require(helper,
				(child.getCatType() == Cat.TYPE_BRITISH
						|| child.getCatType() == Cat.TYPE_JELLIE)
						&& child.isTame()
						&& owner.equals(child.getOwnerUUID())
						&& (child.getCollarColor() == DyeColor.PINK
								|| child.getCollarColor()
										== DyeColor.YELLOW),
				"Custard Cat offspring lost variant, owner, tame, or collar inheritance");
		require(helper,
				child.isFood(new ItemStack(Items.COD))
						&& child.isFood(new ItemStack(Items.SALMON))
						&& !child.isFood(new ItemStack(
								CakeWorldItems.SIMPLE_BISCUIT.get())),
				"Custard Cat changed the compatible vanilla cat food role");

		CompoundTag saved = new CompoundTag();
		child.addAdditionalSaveData(saved);
		CustardCat restored = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Custard Cat save fixture");
		restored.readAdditionalSaveData(saved);
		require(helper,
				restored.getCatType() == child.getCatType()
						&& restored.isTame()
						&& owner.equals(restored.getOwnerUUID())
						&& restored.getCollarColor()
								== child.getCollarColor(),
				"Custard Cat did not retain variant/taming data");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(owner, "CakeWorldCustardCatRoleTest"));
		VanillaRoleAdvancements.creditBredRole(advancementPlayer,
				CakeWorldEntities.CUSTARD_CAT.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:cat");
		CustardCat catalogueCat = CakeWorldEntities.CUSTARD_CAT.get()
				.create(helper.getLevel());
		require(helper, catalogueCat != null,
				"Could not create Custard Cat catalogue fixture");
		catalogueCat.setCatType(Cat.TYPE_BRITISH);
		CriteriaTriggers.TAME_ANIMAL.trigger(advancementPlayer,
				catalogueCat);
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/complete_catalogue",
				"textures/entity/cat/british_shorthair.png");
		require(helper, CakeWorldItems.CUSTARD_CAT_SPAWN_EGG.isPresent(),
				"Custard Cat has no creative/testing spawn egg");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void starterPicnicBuildsAReadableCookbookLandmark(
			GameTestHelper helper) {
		BlockPos relativeCentre = new BlockPos(5, 20, 5);
		BlockPos absoluteCentre =
				helper.absolutePos(relativeCentre);
		helper.getLevel()
				.getEntitiesOfClass(
						CustardCat.class,
						new AABB(absoluteCentre)
								.inflate(12.0D))
				.forEach(CustardCat::discard);
		for (int x = 1; x <= 9; x++) {
			for (int z = 1; z <= 9; z++) {
				helper.setBlock(new BlockPos(x, 19, z), Blocks.STONE);
				helper.setBlock(new BlockPos(x, 20, z),
						CakeWorldBlocks.CHOCOLATE_SPONGE.get());
				for (int y = 21; y <= 25; y++) {
					helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
				}
			}
		}
		require(helper, StarterPicnicFeature.buildAt(helper.getLevel(),
						new Random(1978L), absoluteCentre),
				"The First Bite picnic refused a safe flat site");
		helper.assertBlockPresent(CakeWorldBlocks.COOKBOOK_KIOSK.get(),
				relativeCentre.above());

		int spongeSeatsAndBorder = 0;
		int icingRoof = 0;
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 3; y++) {
					BlockState state = helper.getLevel().getBlockState(
							absoluteCentre.offset(x, y, z));
					if (state.is(CakeWorldBlocks.CHOCOLATE_SPONGE.get())) {
						spongeSeatsAndBorder++;
					}
					if (state.is(CakeWorldBlocks.ICING.get())) {
						icingRoof++;
					}
				}
			}
		}
		// The biscuit approach deliberately replaces one of the 32 border
		// blocks; the remaining 31 plus four seats total 35 sponge blocks.
		require(helper, spongeSeatsAndBorder == 35 && icingRoof == 18,
				"The picnic lost its cushioned border, seats, or two icing roofs: "
						+ spongeSeatsAndBorder + " sponge, " + icingRoof + " icing");
		java.util.List<CustardCat> companions =
				helper.getLevel().getEntitiesOfClass(CustardCat.class,
						new AABB(absoluteCentre).inflate(12.0D));
		require(helper, companions.size() == 1
						&& companions.get(0).isPersistenceRequired()
						&& companions.get(0).hasRestriction()
						&& companions.get(0).getRestrictCenter()
								.equals(absoluteCentre),
				"The First Bite picnic did not receive one persistent, home-restricted Custard Cat");

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect the picnic's target biome");
		boolean installed = false;
		for (Holder<PlacedFeature> feature : candyPlains.getGenerationSettings()
				.features().get(GenerationStep.Decoration.SURFACE_STRUCTURES.ordinal())) {
			if (feature.unwrapKey().map(key -> key.location().equals(
					StarterPicnicFeature.ID)).orElse(false)) {
				installed = true;
				break;
			}
		}
		require(helper, installed,
				"The First Bite picnic was not installed in Candy Plains worldgen");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void glowJelliesKeepTheLuminousCaveWaterRoleInLemonade(
			GameTestHelper helper) {
		GlowJelly jelly = CakeWorldEntities.GLOW_JELLY.get()
				.create(helper.getLevel());
		GlowJelly restored = CakeWorldEntities.GLOW_JELLY.get()
				.create(helper.getLevel());
		require(helper, jelly != null && restored != null,
				"Could not create Glow-Jelly fixtures");
		require(helper,
				jelly instanceof GlowSquid
						&& close(jelly.getAttributeValue(
								Attributes.MAX_HEALTH), 10.0D)
						&& jelly.getDarkTicksRemaining() == 0,
				"Glow-Jelly lost the Glow Squid base, health, or lit state");

		BlockPos horizontalAnchor =
				helper.absolutePos(new BlockPos(3, 3, 3));
		BlockPos lemonadePos = new BlockPos(horizontalAnchor.getX(),
				helper.getLevel().getSeaLevel() - 34,
				horizontalAnchor.getZ());
		for (Direction direction : Direction.values()) {
			helper.getLevel().setBlock(lemonadePos.relative(direction),
					Blocks.STONE.defaultBlockState(), 3);
		}
		helper.getLevel().setBlock(lemonadePos,
				CakeWorldFluids.LEMONADE_BLOCK.get()
						.defaultBlockState(), 3);
		require(helper,
				helper.getLevel().getRawBrightness(lemonadePos, 0) == 0,
				"Glow-Jelly darkness fixture was not actually dark");
		boolean vanillaRule = GlowSquid.checkGlowSquideSpawnRules(
				EntityType.GLOW_SQUID, helper.getLevel(),
				MobSpawnType.NATURAL, lemonadePos,
				new Random(1978L));
		boolean jellyRule = GlowJelly.checkGlowJellySpawnRules(
				CakeWorldEntities.GLOW_JELLY.get(), helper.getLevel(),
				MobSpawnType.NATURAL, lemonadePos,
				new Random(1978L));
		require(helper, !vanillaRule && jellyRule,
				"Glow-Jelly did not retain the deep-dark spawn rule while adapting literal water to water-tagged Lemonade");
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.GLOW_JELLY.get())
						== SpawnPlacements.Type.IN_WATER,
				"Glow-Jelly lost its in-water spawn placement");

		jelly.setPos(horizontalAnchor.getX(), horizontalAnchor.getY(),
				horizontalAnchor.getZ());
		helper.getLevel().addFreshEntity(jelly);
		jelly.setHealth(10.0F);
		Player attacker = helper.makeMockPlayer();
		require(helper,
				jelly.hurt(DamageSource.playerAttack(attacker), 1.0F)
						&& jelly.getDarkTicksRemaining() == 100
						&& close(jelly.getHealth(), 9.0D),
				"Glow-Jelly did not squirt luminous ink and darken for 100 ticks after a living attacker injured it");
		CompoundTag saved = new CompoundTag();
		jelly.addAdditionalSaveData(saved);
		restored.readAdditionalSaveData(saved);
		require(helper,
				saved.getInt("DarkTicksRemaining") == 100
						&& restored.getDarkTicksRemaining() == 100,
				"Glow-Jelly did not retain its dark-tick state across save/reload");
		require(helper,
				jelly.getLootTable().equals(new ResourceLocation(
						CakeWorld.MODID, "entities/glow_jelly")),
				"Glow-Jelly did not resolve its dedicated glow-ink loot table");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Glow-Jelly spawning");
		requireSpawnReplacement(helper, sodaOcean, EntityType.GLOW_SQUID,
				CakeWorldEntities.GLOW_JELLY.get(),
				MobCategory.UNDERGROUND_WATER_CREATURE);
		TagKey<EntityType<?>> axolotlPrey = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_hunt_targets"));
		require(helper, CakeWorldEntities.GLOW_JELLY.get()
						.is(axolotlPrey),
				"Glow-Jelly did not preserve the Glow Squid axolotl-prey role");
		require(helper, CakeWorldItems.GLOW_JELLY_SPAWN_EGG.isPresent(),
				"Glow-Jelly has no creative/testing spawn egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(new ResourceLocation(
						"minecraft", "adventure/kill_all_mobs"));
		require(helper, killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:glow_squid"),
				"Vanilla unexpectedly added a Glow Squid kill criterion; reassess compatibility before inventing a bridge");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void nougatGoatsKeepMountainRamsMilkAndFamilyRoles(
			GameTestHelper helper) {
		NougatGoat goat = CakeWorldEntities.NOUGAT_GOAT.get()
				.create(helper.getLevel());
		NougatGoat partner = CakeWorldEntities.NOUGAT_GOAT.get()
				.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, goat != null && partner != null && target != null,
				"Could not create Nougat Goat fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		goat.setNoAi(true);
		partner.setNoAi(true);
		goat.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		partner.setPos(anchor.getX() - 2.0D,
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(goat);
		helper.getLevel().addFreshEntity(partner);
		helper.getLevel().addFreshEntity(target);
		require(helper,
				goat instanceof Goat
						&& close(goat.getAttributeValue(
								Attributes.MAX_HEALTH), 10.0D)
						&& close(goat.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.2D)
						&& close(goat.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 2.0D)
						&& close(goat.getDimensions(Pose.LONG_JUMPING)
								.width, 0.63D)
						&& close(goat.getDimensions(Pose.LONG_JUMPING)
								.height, 0.91D),
				"Nougat Goat lost Goat health, movement, ram strength, or long-jump dimensions");

		goat.handleEntityEvent((byte)58);
		goat.aiStep();
		require(helper, goat.getRammingXHeadRot() > 0.0F,
				"Nougat Goat did not retain the visible horned head-lowering ram cue");
		goat.handleEntityEvent((byte)59);
		goat.getBrain().setMemory(MemoryModuleType.RAM_TARGET,
				target.position());
		require(helper, goat.getBrain().hasMemoryValue(
						MemoryModuleType.RAM_TARGET),
				"Nougat Goat brain lost its ram-target memory");
		goat.getBrain().eraseMemory(MemoryModuleType.RAM_TARGET);

		goat.setInLove(null);
		partner.setInLove(null);
		partner.setScreamingGoat(true);
		require(helper, goat.canMate(partner)
						&& !goat.canAttack(partner),
				"Nougat Goats could not mate or would ram their own custom type");
		NougatGoat child = goat.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities.NOUGAT_GOAT.get()
						&& child.isScreamingGoat()
						&& child.getBrain().hasMemoryValue(
								MemoryModuleType
										.LONG_JUMP_COOLDOWN_TICKS)
						&& child.getBrain().hasMemoryValue(
								MemoryModuleType.RAM_COOLDOWN_TICKS),
				"Nougat Goat offspring lost its custom type, screaming inheritance, or exact inherited cooldown roles");
		require(helper,
				goat.isFood(new ItemStack(Items.WHEAT)),
				"Nougat Goat lost the vanilla wheat breeding role");

		float preFallHealth = goat.getHealth();
		goat.causeFallDamage(12.0F, 1.0F, DamageSource.FALL);
		require(helper, close(goat.getHealth(), preFallHealth),
				"Nougat Goat lost the inherited ten-block fall-damage reduction");

		Player milker = helper.makeMockPlayer();
		milker.getAbilities().instabuild = false;
		milker.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.BUCKET));
		InteractionResult milked = goat.mobInteract(
				milker, InteractionHand.MAIN_HAND);
		require(helper, milked.consumesAction()
						&& milker.getMainHandItem()
								.is(Items.MILK_BUCKET),
				"Nougat Goat lost its adult milk-bucket role");

		Difficulty originalDifficulty = helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY, Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(DamageSource.mobAttack(goat)
								.setNoAggro(),
						(float) goat.getAttributeValue(
								Attributes.ATTACK_DAMAGE));
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement().x > 0.0D
								&& target.getDeltaMovement().y > 0.0D,
						safeDifficulty
								+ " Nougat Goat ram caused damage or lacked its cushioned bounce/rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.hurt(DamageSource.mobAttack(goat).setNoAggro(),
					(float) goat.getAttributeValue(
							Attributes.ATTACK_DAMAGE));
			require(helper, target.getHealth() < 10.0F,
					"Hard Nougat Goat did not retain real ram damage");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		BlockPos icing = helper.absolutePos(new BlockPos(6, 2, 6));
		helper.getLevel().setBlock(icing.below(),
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(),
				Blocks.LIGHT.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(2),
				Blocks.AIR.defaultBlockState(), 3);
		BlockPos spawnPos = icing.above();
		boolean vanillaBody = SpawnPlacements.Type.ON_GROUND.canSpawnAt(
				helper.getLevel(), spawnPos,
				CakeWorldEntities.NOUGAT_GOAT.get());
		require(helper,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState()
						.is(BlockTags.GOATS_SPAWNABLE_ON)
						&& !vanillaBody
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities.NOUGAT_GOAT.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& NaturalSpawner.isValidEmptySpawnBlock(
								helper.getLevel(), spawnPos,
								helper.getLevel().getBlockState(
										spawnPos),
								helper.getLevel().getFluidState(
										spawnPos),
								CakeWorldEntities.NOUGAT_GOAT.get()),
				"Nougat Goat did not adapt thin edible icing into a safe Goat spawn surface with a collisionless light fixture");

		Biome marshmallowPeaks = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.MARSHMALLOW_PEAKS.getId());
		require(helper, marshmallowPeaks != null,
				"Could not inspect Marshmallow Peaks Nougat Goat spawning");
		requireSpawnReplacement(helper, marshmallowPeaks,
				EntityType.GOAT, CakeWorldEntities.NOUGAT_GOAT.get(),
				MobCategory.CREATURE);
		require(helper, CakeWorldItems.NOUGAT_GOAT_SPAWN_EGG.isPresent(),
				"Nougat Goat has no creative/testing spawn egg");
		require(helper, goat.getLootTable().equals(
						new ResourceLocation(CakeWorld.MODID,
								"entities/nougat_goat")),
				"Nougat Goat did not resolve its dedicated empty Goat loot table");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2023"),
						"CakeWorldNougatGoatRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.NOUGAT_GOAT.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:goat");
		Boat boat = EntityType.BOAT.create(helper.getLevel());
		require(helper, boat != null,
				"Could not create Nougat Goat boat fixture");
		boat.setPos(anchor.getX() - 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(boat);
		require(helper, goat.startRiding(boat, true),
				"Nougat Goat could not retain the boat-passenger role");
		VanillaRoleAdvancements.onMount(new EntityMountEvent(
				advancementPlayer, boat, helper.getLevel(), true));
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/ride_a_boat_with_a_goat",
				"ride_a_boat_with_a_goat");
		helper.runAfterDelay(5, () -> {
			int rawBrightness = helper.getLevel()
					.getMaxLocalRawBrightness(spawnPos);
			boolean vanillaRule = Goat.checkGoatSpawnRules(
					CakeWorldEntities.NOUGAT_GOAT.get(),
					helper.getLevel(),
					MobSpawnType.NATURAL, spawnPos,
					new Random(1978L));
			boolean nougatRule =
					NougatGoat.checkNougatGoatSpawnRules(
							CakeWorldEntities.NOUGAT_GOAT.get(),
							helper.getLevel(),
							MobSpawnType.NATURAL,
							spawnPos,
							new Random(1978L));
			require(helper,
					rawBrightness > 8
							&& vanillaRule
							&& nougatRule,
					"Nougat Goat did not retain its bright Goat spawn predicate after block-light propagation: rawBrightness="
							+ rawBrightness);
			helper.getLevel().setBlock(spawnPos,
					Blocks.AIR.defaultBlockState(), 3);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void fudgeBoarsKeepHuntsBreedingThrowsAndZoglinTransition(
			GameTestHelper helper) {
		FudgeBoar boar = CakeWorldEntities.FUDGE_BOAR.get()
				.create(helper.getLevel());
		FudgeBoar partner = CakeWorldEntities.FUDGE_BOAR.get()
				.create(helper.getLevel());
		Pig safeTarget = EntityType.PIG.create(helper.getLevel());
		require(helper,
				boar != null && partner != null && safeTarget != null,
				"Could not create Fudge Boar fixtures");
		BlockPos anchor = helper.absolutePos(new BlockPos(2, 3, 2));
		boar.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
		partner.setPos(anchor.getX() - 2.0D,
				anchor.getY(), anchor.getZ());
		safeTarget.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		boar.setImmuneToZombification(true);
		partner.setImmuneToZombification(true);
		boar.setNoAi(true);
		partner.setNoAi(true);
		helper.getLevel().addFreshEntity(boar);
		helper.getLevel().addFreshEntity(partner);
		helper.getLevel().addFreshEntity(safeTarget);
		require(helper,
				boar instanceof Hoglin
						&& close(boar.getMaxHealth(), 40.0D)
						&& close(boar.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.3D)
						&& close(boar.getAttributeValue(
								Attributes.KNOCKBACK_RESISTANCE),
								0.6D)
						&& close(boar.getAttributeValue(
								Attributes.ATTACK_KNOCKBACK),
								1.0D)
						&& close(boar.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 6.0D)
						&& close(boar.getDimensions(Pose.STANDING)
								.width, 1.3964844D)
						&& close(boar.getDimensions(Pose.STANDING)
								.height, 1.4D),
				"Fudge Boar lost the Hoglin health, movement, resistance, charge, or size contract");
		require(helper,
				boar.canBeHunted()
						&& boar.isFood(new ItemStack(
								Items.CRIMSON_FUNGUS)),
				"Adult Fudge Boar lost its Piglin-hunt or crimson-fungus breeding role");

		boar.setInLove(null);
		partner.setInLove(null);
		require(helper, boar.canMate(partner),
				"Fudge Boars could not recognize their own custom family");
		FudgeBoar child = boar.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities.FUDGE_BOAR.get()
						&& child.isPersistenceRequired(),
				"Fudge Boar mating produced the hard-coded vanilla Hoglin type or lost offspring persistence");
		partner.setBaby(true);
		require(helper, !partner.canBeHunted(),
				"Baby Fudge Boar incorrectly remained huntable");
		boar.resetLove();
		boar.getBrain().setMemory(
				MemoryModuleType.PACIFIED, true);
		require(helper, !boar.canFallInLove(),
				"Fudge Boar ignored inherited Hoglin pacification");
		boar.getBrain().eraseMemory(MemoryModuleType.PACIFIED);

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				safeTarget.removeAllEffects();
				safeTarget.setHealth(10.0F);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 12.0F;
				safeTarget.setDeltaMovement(Vec3.ZERO);
				safeTarget.invulnerableTime = 0;
				require(helper, boar.doHurtTarget(safeTarget),
						safeDifficulty
								+ " Fudge Boar did not complete its protected charge");
				Vec3 protectedThrow =
						safeTarget.getDeltaMovement();
				require(helper,
						close(safeTarget.getHealth(), 10.0D)
								&& boar
										.getAttackAnimationRemainingTicks()
										== 10
								&& !safeTarget.isOnFire()
								&& safeTarget.fallDistance == 0.0F
								&& safeTarget.hasEffect(
										MobEffects.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects.FIRE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& protectedThrow
										.multiply(1.0D, 0.0D, 1.0D)
										.lengthSqr() > 0.0D,
						safeDifficulty
								+ " Fudge Boar charge caused health damage or lost its telegraph, throw, or rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Fudge Boar target");
			hardTarget.setPos(anchor.getX() + 3.0D,
					anchor.getY(), anchor.getZ());
			hardTarget.setHealth(10.0F);
			hardTarget.setDeltaMovement(Vec3.ZERO);
			helper.getLevel().addFreshEntity(hardTarget);
			require(helper,
					boar.doHurtTarget(hardTarget)
							&& hardTarget.getHealth() < 10.0F
							&& hardTarget.getDeltaMovement()
									.multiply(1.0D, 0.0D, 1.0D)
									.lengthSqr() > 0.0D,
					"Hard Fudge Boar did not retain real Hoglin damage and knockback");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Piglin piglin =
				EntityType.PIGLIN.create(helper.getLevel());
		require(helper, piglin != null,
				"Could not create Piglin hunt-sensor fixture");
		piglin.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		piglin.setNoAi(true);
		helper.getLevel().addFreshEntity(piglin);
		NearestLivingEntitySensor nearestSensor =
				new NearestLivingEntitySensor();
		PiglinSpecificSensor piglinSensor =
				new PiglinSpecificSensor();
		for (int scan = 0; scan < 21; ++scan) {
			nearestSensor.tick(helper.getLevel(), piglin);
		}
		for (int scan = 0; scan < 21; ++scan) {
			piglinSensor.tick(helper.getLevel(), piglin);
		}
		require(helper,
				piglin.getBrain().getMemory(
						MemoryModuleType
								.NEAREST_VISIBLE_HUNTABLE_HOGLIN)
						.orElse(null) == boar,
				"Piglin hunt sensor did not classify the Fudge Boar subclass as a huntable Hoglin");

		BlockPos repellent = anchor.offset(0, 0, 3);
		helper.getLevel().setBlock(repellent.below(),
				Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
		helper.getLevel().setBlock(repellent,
				Blocks.WARPED_FUNGUS.defaultBlockState(), 3);
		HoglinSpecificSensor hoglinSensor =
				new HoglinSpecificSensor();
		for (int scan = 0; scan < 21; ++scan) {
			hoglinSensor.tick(helper.getLevel(), boar);
		}
		require(helper,
				boar.getBrain().getMemory(
						MemoryModuleType.NEAREST_REPELLENT)
						.orElse(null).equals(repellent),
				"Fudge Boar lost the inherited Hoglin-repellent sensor");

		BlockPos spawnPos = anchor.offset(5, 0, 5);
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				FudgeBoar.checkFudgeBoarSpawnRules(
						CakeWorldEntities.FUDGE_BOAR.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						spawnPos, new Random(1978L))
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities.FUDGE_BOAR.get())
								== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements.Type.ON_GROUND
								.canSpawnAt(helper.getLevel(),
										spawnPos,
										CakeWorldEntities
												.FUDGE_BOAR.get()),
				"Fudge Boar lost its exact Hoglin ground-spawn contract on Fudge Rock");
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.NETHER_WART_BLOCK.defaultBlockState(), 3);
		require(helper,
				!FudgeBoar.checkFudgeBoarSpawnRules(
						CakeWorldEntities.FUDGE_BOAR.get(),
						helper.getLevel(), MobSpawnType.NATURAL,
						spawnPos, new Random(1978L)),
				"Fudge Boar spawned on vanilla Hoglin-excluded Nether Wart Block");
		Biome fudgeWastes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes Fudge Boar spawning");
		requireSpawnReplacement(helper, fudgeWastes,
				EntityType.HOGLIN,
				CakeWorldEntities.FUDGE_BOAR.get(),
				MobCategory.MONSTER);

		CompoundTag saved = new CompoundTag();
		saved.putBoolean("IsImmuneToZombification", true);
		saved.putInt("TimeInOverworld", 123);
		saved.putBoolean("CannotBeHunted", true);
		FudgeBoar restored = CakeWorldEntities.FUDGE_BOAR.get()
				.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create restored Fudge Boar fixture");
		restored.readAdditionalSaveData(saved);
		CompoundTag roundTrip = new CompoundTag();
		restored.addAdditionalSaveData(roundTrip);
		require(helper,
				roundTrip.getBoolean(
						"IsImmuneToZombification")
						&& roundTrip.getInt(
								"TimeInOverworld") == 123
						&& roundTrip.getBoolean(
								"CannotBeHunted")
						&& !restored.canBeHunted()
						&& !restored.isConverting(),
				"Fudge Boar did not preserve immunity, conversion time, or huntability across NBT");

		FudgeBoar converting =
				CakeWorldEntities.FUDGE_BOAR.get()
						.create(helper.getLevel());
		require(helper, converting != null,
				"Could not create Fudge Boar conversion fixture");
		CompoundTag conversion = new CompoundTag();
		conversion.putInt("TimeInOverworld", 300);
		converting.readAdditionalSaveData(conversion);
		BlockPos conversionPos = anchor.offset(0, 0, 6);
		converting.setPos(conversionPos.getX(),
				conversionPos.getY(), conversionPos.getZ());
		helper.getLevel().addFreshEntity(converting);
		require(helper, converting.isConverting(),
				"Unprotected Overworld Fudge Boar did not enter the inherited zombification countdown");
		converting.tick();
		Zoglin converted = helper.getLevel()
				.getEntitiesOfClass(Zoglin.class,
						new AABB(conversionPos).inflate(2.0D))
				.stream().findFirst().orElse(null);
		require(helper,
				converting.isRemoved()
						&& converted != null
						&& converted.getType() == EntityType.ZOGLIN
						&& converted.hasEffect(
								MobEffects.CONFUSION),
				"Fudge Boar did not preserve the documented transitional conversion to vanilla Zoglin");

		require(helper,
				CakeWorldItems.FUDGE_BOAR_SPAWN_EGG.isPresent()
						&& boar.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/fudge_boar")),
				"Fudge Boar lost its spawn egg or dedicated Hoglin-equivalent loot table");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2025"),
						"CakeWorldFudgeBoarRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.FUDGE_BOAR.get());
		VanillaRoleAdvancements.creditKilledHoglinRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:hoglin");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:hoglin");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void gingerbreadPoniesKeepGeneticsArmorJumpAndTaming(
			GameTestHelper helper) {
		GingerbreadPony first =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		GingerbreadPony second =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, first != null && second != null,
				"Could not create Gingerbread Pony breeding fixtures");

		CompoundTag firstAppearance = new CompoundTag();
		firstAppearance.putInt("Variant",
				Variant.BLACK.getId()
						| Markings.WHITE_DOTS.getId() << 8);
		first.readAdditionalSaveData(firstAppearance);
		CompoundTag secondAppearance = new CompoundTag();
		secondAppearance.putInt("Variant",
				Variant.CREAMY.getId()
						| Markings.NONE.getId() << 8);
		second.readAdditionalSaveData(secondAppearance);
		for (GingerbreadPony parent :
				new GingerbreadPony[] {first, second}) {
			parent.getAttribute(Attributes.MAX_HEALTH)
					.setBaseValue(24.0D);
			parent.getAttribute(Attributes.JUMP_STRENGTH)
					.setBaseValue(0.8D);
			parent.getAttribute(Attributes.MOVEMENT_SPEED)
					.setBaseValue(0.25D);
			parent.setHealth(parent.getMaxHealth());
			parent.setTamed(true);
			parent.setInLove(null);
		}
		require(helper, first.canMate(second),
				"Two adult, healthy, tame Gingerbread Ponies could not mate");
		AgeableMob offspring = first.getBreedOffspring(
				helper.getLevel(), second);
		require(helper,
				offspring instanceof GingerbreadPony child
						&& child.getType()
								== CakeWorldEntities
										.GINGERBREAD_PONY.get()
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH) >= 21.0D
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH) <= 26.0D
						&& child.getAttributeBaseValue(
								Attributes.JUMP_STRENGTH)
								>= 0.666D
						&& child.getAttributeBaseValue(
								Attributes.JUMP_STRENGTH)
								<= 0.867D
						&& child.getAttributeBaseValue(
								Attributes.MOVEMENT_SPEED)
								>= 0.204D
						&& child.getAttributeBaseValue(
								Attributes.MOVEMENT_SPEED)
								<= 0.280D
						&& child.getVariant() != null
						&& child.getMarkings() != null,
				"Gingerbread Pony offspring lost its custom type or vanilla Horse appearance/attribute inheritance");

		DoughDonkey donkey =
				CakeWorldEntities.DOUGH_DONKEY.get()
						.create(helper.getLevel());
		require(helper, donkey != null,
				"Could not create staged Marzipan Mule parent");
		donkey.setTamed(true);
		donkey.setHealth(donkey.getMaxHealth());
		donkey.setInLove(null);
		require(helper,
				first.canMate(donkey)
						&& donkey.canMate(first),
				"Gingerbread Pony and Dough Donkey lost Horse-family mate compatibility");
		AgeableMob ponySideMule = first.getBreedOffspring(
				helper.getLevel(), donkey);
		AgeableMob donkeySideMule = donkey.getBreedOffspring(
				helper.getLevel(), first);
		require(helper,
				ponySideMule instanceof MarzipanMule
						&& ponySideMule.getType()
								== CakeWorldEntities.MARZIPAN_MULE.get()
						&& donkeySideMule instanceof MarzipanMule
						&& donkeySideMule.getType()
								== CakeWorldEntities.MARZIPAN_MULE.get(),
				"Pony/Donkey crossbreeding did not produce Marzipan Mule from both parent directions");

		GingerbreadPony mount =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, mount != null,
				"Could not create Gingerbread Pony mount fixture");
		require(helper,
				mount instanceof Horse
						&& close(mount.getMaxHealth(), 53.0D)
						&& close(mount.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.225D)
						&& close(mount.getDimensions(Pose.STANDING)
								.width, 1.3964844D)
						&& close(mount.getDimensions(Pose.STANDING)
								.height, 1.6D)
						&& mount.canWearArmor()
						&& mount.getMaxTemper() == 100,
				"Gingerbread Pony lost the registered Horse attributes, size, armour, or taming contract");
		require(helper,
				mount.isFood(new ItemStack(Items.WHEAT))
						&& mount.isFood(new ItemStack(
								Items.SUGAR))
						&& mount.isFood(new ItemStack(
								Items.HAY_BLOCK))
						&& mount.isFood(new ItemStack(
								Items.APPLE))
						&& mount.isFood(new ItemStack(
								Items.GOLDEN_CARROT))
						&& mount.isFood(new ItemStack(
								Items.GOLDEN_APPLE))
						&& mount.isFood(new ItemStack(
								Items.ENCHANTED_GOLDEN_APPLE)),
				"Gingerbread Pony lost the full vanilla Horse food and breeding set");

		CompoundTag mountAppearance = new CompoundTag();
		mountAppearance.putInt("Variant",
				Variant.BROWN.getId()
						| Markings.WHITE_FIELD.getId() << 8);
		mount.readAdditionalSaveData(mountAppearance);
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		mount.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		helper.getLevel().addFreshEntity(mount);
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2026"),
						"CakeWorldGingerbreadPonyRoleTest"));
		require(helper, mount.tameWithName(advancementPlayer)
						&& mount.isTamed()
						&& advancementPlayer.getUUID().equals(
								mount.getOwnerUUID()),
				"Gingerbread Pony did not retain actual Horse taming and ownership");
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/tame_an_animal",
				"tamed_animal");
		require(helper,
				mount.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& mount.isSaddled()
						&& mount.getSlot(401).set(
								new ItemStack(
										Items
												.DIAMOND_HORSE_ARMOR))
						&& mount.getArmor().is(
								Items.DIAMOND_HORSE_ARMOR)
						&& close(mount.getAttributeValue(
								Attributes.ARMOR), 11.0D),
				"Gingerbread Pony could not equip its vanilla saddle and Diamond Horse Armour");

		Player rider = helper.makeMockPlayer();
		rider.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		InteractionResult rideResult = mount.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper,
				rideResult.consumesAction()
						&& rider.getVehicle() == mount
						&& mount.hasPassenger(rider)
						&& mount.canBeControlledByRider()
						&& mount.canJump(),
				"A tame, saddled Gingerbread Pony did not accept or expose control to its rider");
		mount.handleStartJump(90);
		require(helper, mount.isStanding(),
				"Gingerbread Pony lost its visible charged-jump cue");
		rider.stopRiding();

		CompoundTag saved = new CompoundTag();
		mount.addAdditionalSaveData(saved);
		GingerbreadPony restored =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Gingerbread Pony reload fixture");
		restored.readAdditionalSaveData(saved);
		require(helper,
				restored.isTamed()
						&& advancementPlayer.getUUID().equals(
								restored.getOwnerUUID())
						&& restored.isSaddled()
						&& restored.getArmor().is(
								Items.DIAMOND_HORSE_ARMOR)
						&& restored.getVariant()
								== Variant.BROWN
						&& restored.getMarkings()
								== Markings.WHITE_FIELD,
				"Gingerbread Pony lost owner, tame, saddle, armour, variant, or markings across NBT");

		BlockPos icing = new BlockPos(
				anchor.getX() + 5,
				helper.getLevel().getMaxBuildHeight() - 3,
				anchor.getZ() + 5);
		helper.getLevel().setBlock(icing.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing,
				CakeWorldBlocks.ICING_LAYER.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(icing.above(2),
				Blocks.AIR.defaultBlockState(), 3);
		BlockPos spawnPos = icing.above();
		boolean vanillaRule = Animal.checkAnimalSpawnRules(
				CakeWorldEntities.GINGERBREAD_PONY.get(),
				helper.getLevel(), MobSpawnType.NATURAL,
				spawnPos, new Random(1978L));
		boolean vanillaBody =
				SpawnPlacements.Type.ON_GROUND.canSpawnAt(
						helper.getLevel(), spawnPos,
						CakeWorldEntities
								.GINGERBREAD_PONY.get());
		boolean ponyRule =
				GingerbreadPony
						.checkGingerbreadPonySpawnRules(
								CakeWorldEntities
										.GINGERBREAD_PONY.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								spawnPos,
								new Random(1978L));
		require(helper,
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState()
						.is(BlockTags.ANIMALS_SPAWNABLE_ON)
						&& CakeWorldBlocks.ICING_LAYER.get()
								.defaultBlockState()
								.is(BlockTags
										.ANIMALS_SPAWNABLE_ON)
						&& vanillaRule
						&& !vanillaBody
						&& ponyRule
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.GINGERBREAD_PONY.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& NaturalSpawner
								.isValidEmptySpawnBlock(
										helper.getLevel(),
										spawnPos,
										helper.getLevel()
												.getBlockState(
														spawnPos),
										helper.getLevel()
												.getFluidState(
														spawnPos),
										CakeWorldEntities
												.GINGERBREAD_PONY
												.get()),
				"Gingerbread Pony did not adapt thin edible icing into a safe, bright Horse spawn surface");

		Biome candyPlains = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Gingerbread Pony spawning");
		requireSpawnReplacement(helper, candyPlains,
				EntityType.HORSE,
				CakeWorldEntities.GINGERBREAD_PONY.get(),
				MobCategory.CREATURE);
		require(helper,
				CakeWorldItems.GINGERBREAD_PONY_SPAWN_EGG
						.isPresent()
						&& mount.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/gingerbread_pony")),
				"Gingerbread Pony lost its spawn egg or dedicated Horse-equivalent loot table");
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.GINGERBREAD_PONY.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:horse");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void marzipanMulesKeepHybridPacksMountsAndSterility(
			GameTestHelper helper) {
		GingerbreadPony pony =
				CakeWorldEntities.GINGERBREAD_PONY.get()
						.create(helper.getLevel());
		DoughDonkey donkey =
				CakeWorldEntities.DOUGH_DONKEY.get()
						.create(helper.getLevel());
		MarzipanMule role =
				CakeWorldEntities.MARZIPAN_MULE.get()
						.create(helper.getLevel());
		require(helper, pony != null && donkey != null
						&& role != null,
				"Could not create Marzipan Mule role fixtures");

		pony.getAttribute(Attributes.MAX_HEALTH)
				.setBaseValue(24.0D);
		pony.getAttribute(Attributes.JUMP_STRENGTH)
				.setBaseValue(0.8D);
		pony.getAttribute(Attributes.MOVEMENT_SPEED)
				.setBaseValue(0.25D);
		donkey.getAttribute(Attributes.MAX_HEALTH)
				.setBaseValue(30.0D);
		donkey.getAttribute(Attributes.JUMP_STRENGTH)
				.setBaseValue(0.6D);
		donkey.getAttribute(Attributes.MOVEMENT_SPEED)
				.setBaseValue(0.18D);
		for (net.minecraft.world.entity.animal.horse.AbstractHorse parent :
				new net.minecraft.world.entity.animal.horse.AbstractHorse[] {
						pony, donkey}) {
			parent.setHealth(parent.getMaxHealth());
			parent.setTamed(true);
			parent.setInLove(null);
		}
		require(helper, pony.canMate(donkey)
						&& donkey.canMate(pony),
				"Marzipan Mule parents lost bidirectional Horse-family compatibility");
		AgeableMob ponyResult = pony.getBreedOffspring(
				helper.getLevel(), donkey);
		AgeableMob donkeyResult = donkey.getBreedOffspring(
				helper.getLevel(), pony);
		require(helper,
				hasInheritedMuleAttributes(ponyResult)
						&& hasInheritedMuleAttributes(
								donkeyResult),
				"Marzipan Mule offspring lost custom type or vanilla hybrid physical inheritance");

		require(helper,
				role instanceof Mule
						&& role.getType()
								== CakeWorldEntities
										.MARZIPAN_MULE.get()
						&& close(role.getMaxHealth(), 53.0D)
						&& close(role.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.175D)
						&& close(role.getAttributeValue(
								Attributes.JUMP_STRENGTH),
								0.5D)
						&& close(role.getDimensions(Pose.STANDING)
								.width, 1.3964844D)
						&& close(role.getDimensions(Pose.STANDING)
								.height, 1.6D)
						&& role.getMaxTemper() == 100
						&& role.getMaxSpawnClusterSize() == 6
						&& role.getInventoryColumns() == 5
						&& !role.canWearArmor(),
				"Marzipan Mule lost exact Mule attributes, body, temper, cluster, pack, or armour roles");
		role.setTamed(true);
		role.setHealth(role.getMaxHealth());
		role.setInLove(null);
		require(helper,
				!role.canMate(pony)
						&& !pony.canMate(role)
						&& role.getBreedOffspring(
								helper.getLevel(), pony)
								instanceof MarzipanMule,
				"Marzipan Mule lost sterility or leaked a literal Mule through its unreachable offspring factory");
		require(helper,
				role.isFood(new ItemStack(Items.WHEAT))
						&& role.isFood(new ItemStack(Items.SUGAR))
						&& role.isFood(new ItemStack(
								Items.HAY_BLOCK))
						&& role.isFood(new ItemStack(Items.APPLE))
						&& role.isFood(new ItemStack(
								Items.GOLDEN_CARROT))
						&& role.isFood(new ItemStack(
								Items.GOLDEN_APPLE))
						&& role.isFood(new ItemStack(
								Items.ENCHANTED_GOLDEN_APPLE)),
				"Marzipan Mule lost the full vanilla Horse-family food set");

		Player rider = helper.makeMockPlayer();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		role.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		role.setOwnerUUID(rider.getUUID());
		role.setTamed(true);
		role.setHealth(role.getMaxHealth());
		helper.getLevel().addFreshEntity(role);
		rider.getAbilities().instabuild = false;
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.CHEST));
		InteractionResult chestResult = role.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, chestResult.consumesAction()
						&& role.hasChest()
						&& rider.getMainHandItem().isEmpty()
						&& role.getSlot(500).set(
								new ItemStack(Items.DIAMOND, 3))
						&& role.getSlot(514).set(
								new ItemStack(Items.GOLD_INGOT, 2)),
				"Marzipan Mule did not equip a chest or expose all fifteen pack slots");
		require(helper, role.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& role.isSaddled(),
				"Marzipan Mule could not equip its saddle");
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		InteractionResult rideResult = role.mobInteract(
				rider, InteractionHand.MAIN_HAND);
		require(helper, rideResult.consumesAction()
						&& rider.getVehicle() == role
						&& role.hasPassenger(rider)
						&& role.canBeControlledByRider()
						&& role.canJump(),
				"Marzipan Mule lost tame saddle riding, rider control, or charged-jump support");
		role.onPlayerJump(90);
		role.handleStartJump(90);
		require(helper, role.isStanding(),
				"Marzipan Mule lost its visible full-charge jump cue");
		rider.stopRiding();

		role.setHealth(role.getMaxHealth());
		float healthBeforeFall = role.getHealth();
		require(helper,
				role.causeFallDamage(8.0F, 1.0F,
						DamageSource.FALL)
						&& role.getHealth()
								< healthBeforeFall,
				"Marzipan Mule incorrectly removed the real environmental fall hazard");
		CompoundTag saved = role.saveWithoutId(
				new CompoundTag());
		saved.remove("UUID");
		MarzipanMule restored =
				CakeWorldEntities.MARZIPAN_MULE.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Marzipan Mule reload fixture");
		restored.load(saved);
		require(helper,
				restored.isTamed()
						&& rider.getUUID().equals(
								restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.isSaddled()
						&& restored.getSlot(500).get()
								.is(Items.DIAMOND)
						&& restored.getSlot(500).get()
								.getCount() == 3
						&& restored.getSlot(514).get()
								.is(Items.GOLD_INGOT)
						&& restored.getSlot(514).get()
								.getCount() == 2,
				"Marzipan Mule lost owner, tame, chest, saddle, or edge pack slots across save/load");

		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Marzipan Mule spawn boundary for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.CREATURE)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type == EntityType.MULE
									|| spawn.type
											== CakeWorldEntities
													.MARZIPAN_MULE
													.get());
			require(helper, !leaked,
					"Marzipan Mule leaked into open-biome spawning at "
							+ biomeId);
		}
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.MARZIPAN_MULE.get())
								== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.MARZIPAN_MULE.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.MARZIPAN_MULE_SPAWN_EGG
								.isPresent()
						&& role.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/marzipan_mule")),
				"Marzipan Mule lost exact placement, egg, or dedicated Mule-equivalent loot");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(), helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2032"),
						"CakeWorldMarzipanMuleRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.MARZIPAN_MULE.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:mule");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void driedCrumblersKeepDaylightDustAndThemedWaterConversion(
			GameTestHelper helper) {
		DriedCrumbler crumbler =
				CakeWorldEntities.DRIED_CRUMBLER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, crumbler != null && target != null,
				"Could not create Dried Crumbler test fixtures");
		require(helper,
				crumbler instanceof Husk
						&& close(crumbler.getMaxHealth(), 20.0D)
						&& close(crumbler.getAttributeValue(
								Attributes.FOLLOW_RANGE), 35.0D)
						&& close(crumbler.getAttributeValue(
								Attributes.MOVEMENT_SPEED), 0.23D)
						&& close(crumbler.getAttributeValue(
								Attributes.ATTACK_DAMAGE), 3.0D)
						&& close(crumbler.getAttributeValue(
								Attributes.ARMOR), 2.0D)
						&& crumbler.getAttribute(
								Attributes
										.SPAWN_REINFORCEMENTS_CHANCE)
								!= null
						&& crumbler.getMobType()
								== MobType.UNDEAD
						&& close(crumbler
								.getDimensions(Pose.STANDING)
								.width, 0.6D)
						&& close(crumbler
								.getDimensions(Pose.STANDING)
								.height, 1.95D),
				"Dried Crumbler lost its exact Husk size, type, or Zombie attributes");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				require(helper,
						crumbler.doHurtTarget(target),
						safeDifficulty
								+ " Dried Crumbler contact did not register");
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.CONFUSION)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& !target.hasEffect(
										MobEffects.HUNGER)
								&& target.getDeltaMovement().y
										> 0.0D,
						safeDifficulty
								+ " Dried Crumbler caused damage, starvation risk, or lacked its dust/rescue effects");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			require(helper,
					crumbler.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& target.hasEffect(
									MobEffects.HUNGER),
					"Hard Dried Crumbler did not retain real Husk damage and Hunger");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			DriedCrumbler peaceful =
					CakeWorldEntities.DRIED_CRUMBLER.get()
							.create(helper.getLevel());
			require(helper, peaceful != null,
					"Could not create Peaceful Dried Crumbler fixture");
			peaceful.checkDespawn();
			require(helper, peaceful.isRemoved(),
					"Dried Crumbler did not retain Peaceful monster despawning");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		require(helper, !crumbler.isSunSensitive(),
				"Dried Crumbler lost Husk's explicit daylight-safe contract");

		BlockPos darkSpawn = helper.absolutePos(
				new BlockPos(0, 24, 0));
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				helper.getLevel().setBlock(
						darkSpawn.offset(x, -1, z),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(x, 2, z),
						Blocks.STONE.defaultBlockState(), 3);
			}
		}
		for (int y = 0; y <= 1; y++) {
			for (int edge = -2; edge <= 2; edge++) {
				helper.getLevel().setBlock(
						darkSpawn.offset(-2, y, edge),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(2, y, edge),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(edge, y, -2),
						Blocks.STONE.defaultBlockState(), 3);
				helper.getLevel().setBlock(
						darkSpawn.offset(edge, y, 2),
						Blocks.STONE.defaultBlockState(), 3);
			}
		}
		helper.getLevel().setBlock(darkSpawn,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(darkSpawn.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.DRIED_CRUMBLER.get())
						== SpawnPlacements.Type.ON_GROUND,
				"Dried Crumbler lost its ON_GROUND placement");

		DriedCrumbler converting =
				CakeWorldEntities.DRIED_CRUMBLER.get()
						.create(helper.getLevel());
		require(helper, converting != null,
				"Could not create Dried Crumbler conversion fixture");
		converting.setCustomName(new TextComponent(
				"Dry as a Biscuit"));
		converting.setPersistenceRequired();
		converting.setBaby(true);
		CompoundTag conversion = new CompoundTag();
		conversion.putBoolean("IsBaby", true);
		conversion.putBoolean("CanBreakDoors", true);
		conversion.putBoolean("PersistenceRequired", true);
		conversion.putInt("InWaterTime", 600);
		conversion.putInt("DrownedConversionTime", 0);
		converting.readAdditionalSaveData(conversion);
		CompoundTag conversionRoundTrip = new CompoundTag();
		converting.addAdditionalSaveData(
				conversionRoundTrip);
		require(helper,
				converting.isUnderWaterConverting()
						&& conversionRoundTrip.getInt(
								"InWaterTime") == -1
						&& conversionRoundTrip.getInt(
								"DrownedConversionTime") == 0,
				"Dried Crumbler did not preserve its active water-conversion countdown");
		BlockPos conversionPos = helper.absolutePos(
				new BlockPos(10, 4, 4));
		converting.setPos(conversionPos.getX(),
				conversionPos.getY(), conversionPos.getZ());
		helper.getLevel().addFreshEntity(converting);
		converting.tick();
		StaleCrumbler converted = helper.getLevel()
				.getEntitiesOfClass(StaleCrumbler.class,
						new AABB(conversionPos).inflate(2.0D))
				.stream().findFirst().orElse(null);
		require(helper,
				converting.isRemoved()
						&& converted != null
						&& converted.getType()
								== CakeWorldEntities
										.STALE_CRUMBLER.get()
						&& converted.isBaby()
						&& converted.isPersistenceRequired()
						&& converted.hasCustomName()
						&& "Dry as a Biscuit".equals(
								converted.getCustomName()
										.getString())
						&& converted.canBreakDoors(),
				"Dried Crumbler leaked a vanilla Zombie or lost its baby, name, persistence, or door state during water conversion");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ResourceLocation sherbetDunes = new ResourceLocation(
				CakeWorld.MODID, "sherbet_dunes");
		require(helper, biomes.get(sherbetDunes) == null,
				"Sherbet Dunes unexpectedly exists; MOB-027's staged spawn gate must be revisited");
		for (ResourceLocation biomeId : new ResourceLocation[] {
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId()}) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect staged Dried Crumbler biome gate for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.MONSTER)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type == EntityType.HUSK
									|| spawn.type
											== CakeWorldEntities
													.DRIED_CRUMBLER
													.get());
			require(helper, !leaked,
					"Dried Crumbler or vanilla Husk leaked into existing biome "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.DRIED_CRUMBLER_SPAWN_EGG
						.isPresent()
						&& crumbler.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/dried_crumbler")),
				"Dried Crumbler lost its spawn egg or dedicated Husk-equivalent loot table");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2027"),
						"CakeWorldDriedCrumblerRoleTest"));
		VanillaRoleAdvancements.creditKilledHuskRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:husk");
		helper.runAfterDelay(5, () -> {
			Difficulty predicateDifficulty =
					helper.getLevel()
							.getDifficulty();
			boolean spawnerAllowed;
			boolean naturalAllowed;
			try {
				helper.getLevel().getServer()
						.setDifficulty(
								Difficulty.NORMAL,
								true);
				spawnerAllowed =
						DriedCrumbler
								.checkDriedCrumblerSpawnRules(
										CakeWorldEntities
												.DRIED_CRUMBLER
												.get(),
										helper.getLevel(),
										MobSpawnType
												.SPAWNER,
										darkSpawn,
										new Random(
												1978L));
				naturalAllowed =
						DriedCrumbler
								.checkDriedCrumblerSpawnRules(
										CakeWorldEntities
												.DRIED_CRUMBLER
												.get(),
										helper.getLevel(),
										MobSpawnType
												.NATURAL,
										darkSpawn,
										new Random(
												1978L));
			} finally {
				helper.getLevel().getServer()
						.setDifficulty(
								predicateDifficulty,
								true);
			}
			require(helper,
					spawnerAllowed
							&& !naturalAllowed,
					"Dried Crumbler lost its lit spawn boundary after light propagation: spawner="
							+ spawnerAllowed
							+ ", natural="
							+ naturalAllowed
							+ ", sky="
							+ helper.getLevel()
									.getBrightness(
											LightLayer
													.SKY,
											darkSpawn)
							+ ", block="
							+ helper.getLevel()
									.getBrightness(
											LightLayer
													.BLOCK,
											darkSpawn)
							+ ", raw="
							+ helper.getLevel()
									.getMaxLocalRawBrightness(
											darkSpawn)
							+ ", canSeeSky="
							+ helper.getLevel()
									.canSeeSky(
											darkSpawn));
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void mirageConfectionersKeepCommandSpellsAndSafeSweets(
			GameTestHelper helper) {
		MirageConfectioner confectioner =
				CakeWorldEntities.MIRAGE_CONFECTIONER.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		Evoker illagerColleague =
				EntityType.EVOKER.create(helper.getLevel());
		require(helper,
				confectioner != null && target != null
						&& illagerColleague != null,
				"Could not create Mirage Confectioner fixtures");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		confectioner.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		target.setPos(anchor.getX() + 7.0D,
				anchor.getY(), anchor.getZ());
		confectioner.finalizeSpawn(helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						anchor),
				MobSpawnType.COMMAND, null, null);
		AABB culling = confectioner
				.getBoundingBoxForCulling();
		require(helper,
				confectioner instanceof Illusioner
						&& confectioner.getMobType()
								== MobType.ILLAGER
						&& close(confectioner
								.getMaxHealth(), 32.0D)
						&& close(confectioner
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.5D)
						&& close(confectioner
								.getAttribute(
										Attributes.FOLLOW_RANGE)
								.getBaseValue(),
								18.0D)
						&& close(confectioner
								.getDimensions(Pose.STANDING)
								.width, 0.6D)
						&& close(confectioner
								.getDimensions(Pose.STANDING)
								.height, 1.95D),
				"Mirage Confectioner lost Illusioner type, attributes, or dimensions: "
						+ "instanceof=" + (confectioner instanceof Illusioner)
						+ ", mobType=" + confectioner.getMobType()
						+ ", health=" + confectioner.getMaxHealth()
						+ ", speed=" + confectioner.getAttributeValue(
								Attributes.MOVEMENT_SPEED)
						+ ", follow=" + confectioner.getAttributeValue(
								Attributes.FOLLOW_RANGE)
						+ ", followBase=" + confectioner.getAttribute(
								Attributes.FOLLOW_RANGE).getBaseValue()
						+ ", width=" + confectioner
								.getDimensions(Pose.STANDING).width
						+ ", height=" + confectioner
								.getDimensions(Pose.STANDING).height);
		require(helper,
				close(culling.getXsize(), 6.6D)
						&& close(culling.getZsize(), 6.6D),
				"Mirage Confectioner lost Illusioner's expanded four-copy culling box: "
						+ culling);
		require(helper,
				confectioner.getItemBySlot(
						EquipmentSlot.MAINHAND)
						.is(Items.BOW),
				"Mirage Confectioner command initialization did not equip the Illusioner bow");
		require(helper, confectioner.canJoinRaid(),
				"Mirage Confectioner command initialization lost the Raider join flag");
		require(helper,
				confectioner.isAlliedTo(illagerColleague),
				"Mirage Confectioner lost unteamed Illager alliance");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				int arrowsBefore = helper.getLevel()
						.getEntitiesOfClass(
								AbstractArrow.class,
								new AABB(anchor)
										.inflate(32.0D))
						.size();
				confectioner.performRangedAttack(
						target, 1.0F);
				MirageSweetProjectile sweet =
						helper.getLevel()
								.getEntitiesOfClass(
										MirageSweetProjectile.class,
										new AABB(anchor)
												.inflate(32.0D),
										projectile -> projectile
												.getOwner()
												== confectioner)
								.stream().findFirst()
								.orElse(null);
				require(helper,
						sweet != null
								&& helper.getLevel()
										.getEntitiesOfClass(
												AbstractArrow.class,
												new AABB(
														anchor)
														.inflate(
																32.0D))
										.size()
										== arrowsBefore,
						safeDifficulty
								+ " Mirage Confectioner did not substitute its real arrow with a safe visible sweet");
				sweet.applyHarmlessMirage(target);
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects.BLINDNESS)
								&& target.hasEffect(
										MobEffects.CONFUSION)
								&& target.hasEffect(
										MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Mirage Sweet caused damage or lacked obscuring/rescue effects");
				sweet.discard();
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			confectioner.performRangedAttack(target, 1.0F);
			AbstractArrow arrow = helper.getLevel()
					.getEntitiesOfClass(AbstractArrow.class,
							new AABB(anchor).inflate(32.0D),
							projectile -> projectile
									.getOwner()
									== confectioner)
					.stream().findFirst().orElse(null);
			require(helper, arrow != null,
					"Hard Mirage Confectioner did not retain the genuine Illusioner arrow path");
			target.hurt(DamageSource.arrow(
					arrow, confectioner), 4.0F);
			require(helper, target.getHealth() < 10.0F,
					"Hard Mirage Confectioner arrow did not retain real damage");
			arrow.discard();

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			MirageConfectioner peaceful =
					CakeWorldEntities
							.MIRAGE_CONFECTIONER.get()
							.create(helper.getLevel());
			require(helper, peaceful != null,
					"Could not create Peaceful Mirage Confectioner fixture");
			peaceful.checkDespawn();
			require(helper, peaceful.isRemoved(),
					"Mirage Confectioner did not retain Peaceful monster despawning");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		target.setInvulnerable(true);
		confectioner.setTarget(target);
		helper.getLevel().addFreshEntity(confectioner);
		helper.getLevel().addFreshEntity(target);
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			for (int i = 0; i < 50; i++) {
				confectioner.tick();
			}
			require(helper,
					confectioner.hasEffect(
							MobEffects.INVISIBILITY),
					"Mirage Confectioner did not retain the inherited mirror/invisibility spell");
			for (int i = 0; i < 30; i++) {
				confectioner.tick();
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeEffect(MobEffects.BLINDNESS);
			for (int i = 0; i < 50; i++) {
				confectioner.tick();
			}
			require(helper,
					target.hasEffect(MobEffects.BLINDNESS),
					"Mirage Confectioner did not retain Illusioner's Hard-only blindness spell");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
			target.setInvulnerable(false);
		}

		BlockPos conversionPos =
				findCakeWorldBiomeColumnPosition(
						helper, anchor, 512, 4);
		require(helper, conversionPos != null,
				"Could not locate CakeWorld terrain for Illusioner command conversion");
		Illusioner literal =
				EntityType.ILLUSIONER.create(
						helper.getLevel());
		Ravager ravager =
				EntityType.RAVAGER.create(helper.getLevel());
		require(helper, literal != null && ravager != null,
				"Could not create Illusioner raid-conversion fixtures");
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		literal.setHealth(23.0F);
		literal.setCustomName(new TextComponent(
				"Four of a Kind"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.BOW));
		CompoundTag spellState = literal.saveWithoutId(
				new CompoundTag());
		spellState.putInt("SpellTicks", 25);
		spellState.putBoolean("CanJoinRaid", true);
		literal.load(spellState);
		ravager.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		helper.getLevel().addFreshEntity(ravager);
		Raid raid = new Raid(197828, helper.getLevel(),
				conversionPos);
		raid.joinRaid(2, literal, null, true);
		raid.setLeader(2, literal);
		helper.getLevel().addFreshEntity(literal);
		literal.startRiding(ravager, true);
		// Riding raises the passenger and can cross a vertical 3D-biome
		// boundary. Keep the conversion fixture at the coordinate which
		// was explicitly resolved as CakeWorld while preserving its mount.
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		ResourceLocation literalBiome = helper.getLevel()
				.getBiome(literal.blockPosition()).unwrapKey()
				.map(key -> key.location()).orElse(null);
		require(helper, literalBiome != null
						&& CakeWorld.MODID.equals(
								literalBiome.getNamespace()),
				"Mounted Illusioner conversion fixture left CakeWorld terrain at "
						+ literal.blockPosition() + " in "
						+ literalBiome);
		MirageConfectioner converted =
				CakeWorldIllusionerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper, converted != null,
				"Literal Illusioner conversion did not create a Mirage Confectioner");
		require(helper,
				literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.MIRAGE_CONFECTIONER
										.get(),
				"Literal Illusioner conversion lost its replacement type or source removal");
		require(helper,
				close(converted.getHealth(), 23.0D)
						&& converted.hasCustomName()
						&& "Four of a Kind".equals(
								converted.getCustomName()
										.getString())
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& close(converted.getYRot(), 29.0D),
				"Literal Illusioner conversion lost health, name, persistence, AI, or rotation NBT");
		require(helper,
				converted.isCastingSpell()
						&& converted.getItemBySlot(
								EquipmentSlot.MAINHAND)
								.is(Items.BOW),
				"Literal Illusioner conversion lost its active spell or command bow");
		require(helper,
				converted.getCurrentRaid() == raid
						&& converted.getWave() == 2
						&& raid.getLeader(2) == converted
						&& raid.getTotalRaidersAlive() == 1,
				"Literal Illusioner conversion lost its raid membership, wave, leader, or count");
		require(helper,
				converted.getVehicle() == ravager,
				"Literal Illusioner conversion lost its Ravager mount");

		TagKey<EntityType<?>> raiders = TagKey.create(
				Registry.ENTITY_TYPE_REGISTRY,
				new ResourceLocation("minecraft",
						"raiders"));
		require(helper,
				CakeWorldEntities.MIRAGE_CONFECTIONER
						.get().is(raiders),
				"Mirage Confectioner did not preserve the vanilla raider tag role");
		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect command-only Mirage Confectioner boundary for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.MONSTER)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type
									== EntityType.ILLUSIONER
									|| spawn.type
											== CakeWorldEntities
													.MIRAGE_CONFECTIONER
													.get());
			require(helper, !leaked,
					"Command-only Illusioner role leaked into biome spawning at "
							+ biomeId);
		}
		ResourceLocation eggId = new ResourceLocation(
				CakeWorld.MODID,
				"mirage_confectioner_spawn_egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities
								.MIRAGE_CONFECTIONER.get())
						== SpawnPlacements.Type
								.NO_RESTRICTIONS
						&& Registry.ITEM.getOptional(eggId)
								.isEmpty()
						&& killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:illusioner")
						&& converted.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/mirage_confectioner")),
				"Mirage Confectioner lost empty loot or fabricated a spawn placement, egg, or nonexistent advancement criterion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void jawbreakerGuardiansKeepSettlementDefenceAndSafeLaunches(
			GameTestHelper helper) {
		JawbreakerGuardian guardian =
				CakeWorldEntities.JAWBREAKER_GUARDIAN
						.get().create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, guardian != null && target != null,
				"Could not create Jawbreaker Guardian fixtures");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		guardian.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		target.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ());
		require(helper,
				guardian instanceof IronGolem
						&& guardian.getType()
								== CakeWorldEntities
										.JAWBREAKER_GUARDIAN
										.get()
						&& guardian.getType().getCategory()
								== MobCategory.MISC
						&& close(guardian.getMaxHealth(),
								100.0D)
						&& close(guardian.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.25D)
						&& close(guardian.getAttributeValue(
								Attributes
										.KNOCKBACK_RESISTANCE),
								1.0D)
						&& close(guardian.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								15.0D)
						&& close(guardian
								.getDimensions(Pose.STANDING)
								.width, 1.4D)
						&& close(guardian
								.getDimensions(Pose.STANDING)
								.height, 2.7D)
						&& close(guardian.maxUpStep, 1.0D)
						&& !guardian.causeFallDamage(
								30.0F, 1.0F,
								DamageSource.FALL)
						&& !guardian.removeWhenFarAway(
								10000.0D),
				"Jawbreaker Guardian lost Iron Golem type, attributes, body, step, fall, or persistence roles");
		guardian.setPlayerCreated(true);
		require(helper,
				guardian.isPlayerCreated()
						&& !guardian.canAttackType(
								EntityType.PLAYER)
						&& !guardian.canAttackType(
								EntityType.CREEPER)
						&& guardian.canAttackType(
								EntityType.ZOMBIE),
				"Jawbreaker Guardian lost player-created loyalty or hostile/Creeper targeting rules");

		guardian.setHealth(74.0F);
		require(helper,
				guardian.getCrackiness()
						== IronGolem.Crackiness.LOW,
				"Jawbreaker Guardian lost low crack state");
		guardian.setHealth(49.0F);
		require(helper,
				guardian.getCrackiness()
						== IronGolem.Crackiness.MEDIUM,
				"Jawbreaker Guardian lost medium crack state");
		guardian.setHealth(24.0F);
		require(helper,
				guardian.getCrackiness()
						== IronGolem.Crackiness.HIGH,
				"Jawbreaker Guardian lost high crack state");
		ServerPlayer player = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2029"),
						"CakeWorldJawbreakerGuardianRoleTest"));
		guardian.setHealth(74.0F);
		player.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.IRON_INGOT));
		InteractionResult repair = player.interactOn(
				guardian, InteractionHand.MAIN_HAND);
		require(helper,
				repair.consumesAction()
						&& close(guardian.getHealth(), 99.0D)
						&& guardian.getCrackiness()
								== IronGolem.Crackiness.NONE
						&& player.getMainHandItem().isEmpty(),
				"Jawbreaker Guardian lost one-ingot, twenty-five-health crack repair");
		guardian.offerFlower(true);
		require(helper, guardian.getOfferFlowerTick() == 400,
				"Jawbreaker Guardian lost its Villager flower-offering animation");
		guardian.offerFlower(false);
		require(helper, guardian.getOfferFlowerTick() == 0,
				"Jawbreaker Guardian could not end its flower offering");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setDeltaMovement(Vec3.ZERO);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				boolean attacked =
						guardian.doHurtTarget(target);
				require(helper,
						attacked
								&& close(target.getHealth(),
										10.0D)
								&& guardian
										.getAttackAnimationTick()
										== 10
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement()
										.y > 0.5D,
						safeDifficulty
								+ " Jawbreaker swing caused damage or lost its visible protected defence bounce");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setDeltaMovement(Vec3.ZERO);
			require(helper,
					guardian.doHurtTarget(target)
							&& target.getHealth() < 10.0F
							&& target.getDeltaMovement().y
									>= 0.4D,
					"Hard Jawbreaker Guardian did not retain genuine Iron Golem damage and launch");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Villager villager =
				EntityType.VILLAGER.create(helper.getLevel());
		Pillager raider =
				EntityType.PILLAGER.create(helper.getLevel());
		require(helper, villager != null && raider != null,
				"Could not create Jawbreaker village-integration fixtures");
		villager.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		raider.setPos(anchor.getX() + 4.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(guardian);
		helper.getLevel().addFreshEntity(villager);
		helper.getLevel().addFreshEntity(raider);
		villager.getBrain().eraseMemory(
				MemoryModuleType.GOLEM_DETECTED_RECENTLY);
		villager.getBrain().setMemory(
				MemoryModuleType.NEAREST_LIVING_ENTITIES,
				List.of(guardian));
		GolemSensor.checkForNearbyGolem(villager);
		require(helper,
				!villager.getBrain().hasMemoryValue(
						MemoryModuleType
								.GOLEM_DETECTED_RECENTLY),
				"Vanilla GolemSensor unexpectedly stopped using its literal-type boundary");
		guardian.refreshVillagerAwareness();
		require(helper,
				villager.getBrain().hasMemoryValue(
						MemoryModuleType
								.GOLEM_DETECTED_RECENTLY),
				"Jawbreaker Guardian did not refresh nearby Villager golem awareness");
		raider.setTarget(guardian);
		raider.setNoActionTime(999);
		guardian.refreshRaiderCombatActivity();
		require(helper, raider.getNoActionTime() == 0,
				"Jawbreaker Guardian did not preserve active Raider-versus-golem combat");

		BlockPos conversionPos =
				findCakeWorldBiomePosition(
						helper, anchor, 512);
		require(helper, conversionPos != null,
				"Could not locate CakeWorld terrain for Iron Golem conversion");
		IronGolem literal =
				EntityType.IRON_GOLEM.create(
						helper.getLevel());
		Ravager ravager =
				EntityType.RAVAGER.create(helper.getLevel());
		require(helper, literal != null && ravager != null,
				"Could not create Iron Golem conversion fixtures");
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		literal.setHealth(49.0F);
		literal.setCustomName(new TextComponent(
				"Jawbreaker Jane"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setPlayerCreated(true);
		literal.setRemainingPersistentAngerTime(321);
		literal.setPersistentAngerTarget(
				player.getUUID());
		ravager.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		helper.getLevel().addFreshEntity(ravager);
		helper.getLevel().addFreshEntity(literal);
		CriteriaTriggers.SUMMONED_ENTITY.trigger(
				player, literal);
		requireCriterion(helper, player,
				"minecraft:adventure/summon_iron_golem",
				"summoned_golem");
		literal.startRiding(ravager, true);
		// Riding can reposition the passenger across a vertical 3D-biome
		// boundary; keep this conversion fixture at the already matched
		// CakeWorld coordinate while retaining its vehicle relationship.
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 29.0F, 0.0F);
		JawbreakerGuardian converted =
				CakeWorldIronGolemReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		String conversionState = converted == null
				? "converted=null, literalRemoved="
						+ literal.isRemoved()
				: "type=" + converted.getType()
						+ ", literalRemoved="
						+ literal.isRemoved()
						+ ", health="
						+ converted.getHealth()
						+ ", crack="
						+ converted.getCrackiness()
						+ ", name="
						+ converted.getCustomName()
						+ ", persistent="
						+ converted.isPersistenceRequired()
						+ ", noAi="
						+ converted.isNoAi()
						+ ", playerCreated="
						+ converted.isPlayerCreated()
						+ ", angerTime="
						+ converted
								.getRemainingPersistentAngerTime()
						+ ", angerTarget="
						+ converted.getPersistentAngerTarget()
						+ ", expectedTarget="
						+ player.getUUID()
						+ ", yRot="
						+ converted.getYRot()
						+ ", vehicle="
						+ converted.getVehicle()
						+ ", expectedVehicle="
						+ ravager;
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.JAWBREAKER_GUARDIAN
										.get()
						&& close(converted.getHealth(), 49.0D)
						&& converted.getCrackiness()
								== IronGolem.Crackiness.MEDIUM
						&& converted.hasCustomName()
						&& "Jawbreaker Jane".equals(
								converted.getCustomName()
										.getString())
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isPlayerCreated()
						&& converted
								.getRemainingPersistentAngerTime()
								== 321
						&& player.getUUID().equals(converted
								.getPersistentAngerTarget())
						&& close(converted.getYRot(), 29.0D)
						&& converted.getVehicle() == ravager,
				"Literal Iron Golem conversion lost type, NBT, crack, loyalty, anger, rotation, or vehicle state: "
						+ conversionState);

		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Jawbreaker structure-only spawn boundary for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.MISC)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type
									== EntityType.IRON_GOLEM
									|| spawn.type
											== CakeWorldEntities
													.JAWBREAKER_GUARDIAN
													.get());
			require(helper, !leaked,
					"Jawbreaker Guardian leaked into open-biome spawning at "
							+ biomeId);
		}
		ResourceLocation eggId = new ResourceLocation(
				CakeWorld.MODID,
				"jawbreaker_guardian_spawn_egg");
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities
								.JAWBREAKER_GUARDIAN
								.get())
						== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.JAWBREAKER_GUARDIAN
										.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& Registry.ITEM.getOptional(eggId)
								.isEmpty()
						&& killAll != null
						&& !killAll.getCriteria().containsKey(
								"minecraft:iron_golem")
						&& converted.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/jawbreaker_guardian")),
				"Jawbreaker Guardian lost Iron Golem placement/loot or fabricated an egg or Monsters Hunted criterion");
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void meringueLlamasKeepPacksCaravansDecorBreedingAndSafeSpit(
			GameTestHelper helper) {
		MeringueLlama first =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		MeringueLlama second =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		Pig target = EntityType.PIG.create(helper.getLevel());
		TraderLlama trader =
				EntityType.TRADER_LLAMA.create(
						helper.getLevel());
		require(helper, first != null && second != null
						&& target != null && trader != null,
				"Could not create Meringue Llama fixtures");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		first.setNoAi(true);
		second.setNoAi(true);
		first.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		second.setPos(anchor.getX() - 2.0D,
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(first);
		helper.getLevel().addFreshEntity(second);
		helper.getLevel().addFreshEntity(target);

		require(helper,
				first instanceof Llama
						&& first.getType()
								== CakeWorldEntities
										.MERINGUE_LLAMA.get()
						&& first.getType().getCategory()
								== MobCategory.CREATURE
						&& close(first.getMaxHealth(), 53.0D)
						&& close(first.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.175D)
						&& close(first.getAttributeValue(
								Attributes.JUMP_STRENGTH),
								0.5D)
						&& close(first.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								40.0D)
						&& close(first
								.getDimensions(Pose.STANDING)
								.width, 0.9D)
						&& close(first
								.getDimensions(Pose.STANDING)
								.height, 1.87D)
						&& close(first.maxUpStep, 1.0D)
						&& first.getMaxSpawnClusterSize() == 6
						&& first.getMaxTemper() == 30
						&& !first.isTraderLlama()
						&& trader.isTraderLlama()
						&& trader.getType()
								== EntityType.TRADER_LLAMA
						&& !first.isSaddleable()
						&& !first.canBeControlledByRider()
						&& first.canWearArmor()
						&& first.isFood(new ItemStack(
								Items.WHEAT))
						&& first.isFood(new ItemStack(
								Items.HAY_BLOCK)),
				"Meringue Llama lost its exact Llama type, body, attributes, taming, food, pack, or Trader separation");

		CompoundTag firstGenes = new CompoundTag();
		firstGenes.putInt("Strength", 5);
		firstGenes.putInt("Variant", 3);
		first.readAdditionalSaveData(firstGenes);
		CompoundTag secondGenes = new CompoundTag();
		secondGenes.putInt("Strength", 2);
		secondGenes.putInt("Variant", 1);
		second.readAdditionalSaveData(secondGenes);
		UUID owner = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2030");
		ServerPlayer player = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(owner,
						"CakeWorldMeringueLlamaRoleTest"));
		first.setTamed(true);
		first.setOwnerUUID(owner);
		second.setTamed(true);
		second.setOwnerUUID(owner);
		require(helper,
				first.getSlot(499).set(
						new ItemStack(Items.CHEST))
						&& first.getSlot(401).set(
								new ItemStack(
										Items.PINK_CARPET))
						&& first.getSlot(500).set(
								new ItemStack(
										CakeWorldItems
												.BOILED_SWEET
												.get(), 3))
						&& first.getSlot(514).set(
								new ItemStack(
										CakeWorldItems
												.MINT_WAFER
												.get(), 2))
						&& !first.getSlot(515).set(
								new ItemStack(
										Items.DIAMOND))
						&& first.hasChest()
						&& first.getInventoryColumns() == 5
						&& first.getStrength() == 5
						&& first.getVariant() == 3
						&& first.getSwag() == DyeColor.PINK
						&& first.isWearingArmor(),
				"Meringue Llama lost strength-scaled 15-slot storage or carpet decor");

		CompoundTag saved =
				first.saveWithoutId(new CompoundTag());
		MeringueLlama restored =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Meringue Llama reload fixture");
		restored.load(saved);
		require(helper,
				restored.isTamed()
						&& owner.equals(
								restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.getStrength() == 5
						&& restored.getInventoryColumns() == 5
						&& restored.getVariant() == 3
						&& restored.getSwag()
								== DyeColor.PINK
						&& restored.getSlot(401).get()
								.is(Items.PINK_CARPET)
						&& restored.getSlot(500).get()
								.is(CakeWorldItems
										.BOILED_SWEET.get())
						&& restored.getSlot(500).get()
								.getCount() == 3
						&& restored.getSlot(514).get()
								.is(CakeWorldItems
										.MINT_WAFER.get())
						&& restored.getSlot(514).get()
								.getCount() == 2,
				"Meringue Llama reload lost owner, strength, variant, chest, carpet, or edge pack slot");

		player.setItemInHand(InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		InteractionResult mounted = player.interactOn(
				first, InteractionHand.MAIN_HAND);
		require(helper,
				mounted.consumesAction()
						&& player.getVehicle() == first
						&& !first.canBeControlledByRider(),
				"Tame Meringue Llama lost its mountable but deliberately uncontrollable passenger role");
		player.stopRiding();

		first.setHealth(first.getMaxHealth());
		second.setHealth(second.getMaxHealth());
		first.setAge(0);
		second.setAge(0);
		first.setInLove(player);
		second.setInLove(player);
		require(helper, first.canMate(second),
				"Two healthy tame Meringue Llamas could not mate");
		Llama child = first.getBreedOffspring(
				helper.getLevel(), second);
		require(helper,
				child instanceof MeringueLlama
						&& child.getType()
								== CakeWorldEntities
										.MERINGUE_LLAMA.get()
						&& child.getStrength() >= 1
						&& child.getStrength() <= 5
						&& (child.getVariant() == 3
								|| child.getVariant() == 1)
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH)
								>= 40.0D
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH)
								<= 46.0D,
				"Meringue Llama breeding leaked a literal Llama or lost strength, variant, or inherited physical attributes");

		MeringueLlama caravanLeader =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		MeringueLlama caravanFollower =
				CakeWorldEntities.MERINGUE_LLAMA.get()
						.create(helper.getLevel());
		require(helper, caravanLeader != null
						&& caravanFollower != null,
				"Could not create Meringue Llama caravan fixtures");
		caravanLeader.setNoAi(true);
		caravanFollower.setNoAi(true);
		caravanLeader.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ() + 6.0D);
		caravanFollower.setPos(anchor.getX() + 5.0D,
				anchor.getY(), anchor.getZ() + 6.0D);
		helper.getLevel().addFreshEntity(caravanLeader);
		helper.getLevel().addFreshEntity(caravanFollower);
		caravanLeader.setLeashedTo(player, false);
		long literalGoals = caravanFollower.goalSelector
				.getAvailableGoals().stream()
				.filter(wrapped -> wrapped.getGoal()
						instanceof LlamaFollowCaravanGoal)
				.count();
		MeringueLlamaFollowCaravanGoal caravanGoal =
				caravanFollower.goalSelector
						.getAvailableGoals().stream()
						.map(wrapped -> wrapped.getGoal())
						.filter(MeringueLlamaFollowCaravanGoal.class
								::isInstance)
						.map(MeringueLlamaFollowCaravanGoal.class
								::cast)
						.findFirst().orElse(null);
		require(helper,
				literalGoals == 0
						&& caravanGoal != null
						&& caravanGoal.canUse()
						&& caravanFollower.inCaravan()
						&& caravanFollower.getCaravanHead()
								== caravanLeader
						&& caravanLeader.hasCaravanTail(),
				"Meringue Llama did not replace the literal-type vanilla goal or form a genuine custom caravan");
		caravanFollower.leaveCaravan();

		first.performRangedAttack(target, 1.0F);
		LlamaSpit spit = helper.getLevel()
				.getEntitiesOfClass(LlamaSpit.class,
						new AABB(anchor).inflate(10.0D))
				.stream()
				.filter(projectile ->
						projectile.getOwner() == first)
				.findFirst().orElse(null);
		require(helper, spit != null,
				"Meringue Llama did not retain its visible vanilla Llama spit projectile");
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(
						DamageSource.indirectMobAttack(
								spit, first)
								.setProjectile(),
						1.0F);
				require(helper,
						close(target.getHealth(), 10.0D)
								&& !target.isOnFire()
								&& target.fallDistance == 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.getEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
										.getAmplifier() == 1
								&& target.hasEffect(
										MobEffects.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& target.getDeltaMovement()
										.x > 0.0D
								&& target.getDeltaMovement()
										.y > 0.0D,
						safeDifficulty
								+ " Meringue spit caused health damage or lost its sticky visible nudge and rescue envelope");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setDeltaMovement(Vec3.ZERO);
			target.hurt(
					DamageSource.indirectMobAttack(
							spit, first).setProjectile(),
					1.0F);
			require(helper,
					close(target.getHealth(), 9.0D)
							&& target.getActiveEffects().isEmpty()
							&& target.getDeltaMovement()
									.lengthSqr() > 0.0D,
					"Hard Meringue Llama did not retain exact one-point vanilla spit damage and hit motion");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		float beforeFall = restored.getHealth();
		restored.causeFallDamage(
				8.0F, 1.0F, DamageSource.FALL);
		require(helper, restored.getHealth() < beforeFall,
				"Meringue Llama incorrectly erased genuine environmental fall damage");

		BlockPos spawnPos = anchor.offset(8, 0, 8);
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.GRASS_BLOCK.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.TORCH.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		boolean chocolateTag =
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState()
						.is(BlockTags.ANIMALS_SPAWNABLE_ON);
		boolean grassTag = Blocks.GRASS_BLOCK
				.defaultBlockState()
				.is(BlockTags.ANIMALS_SPAWNABLE_ON);
		boolean groundBody =
				SpawnPlacements.Type.ON_GROUND.canSpawnAt(
						helper.getLevel(), spawnPos,
						CakeWorldEntities.MERINGUE_LLAMA.get());
		boolean placementType =
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.MERINGUE_LLAMA.get())
						== SpawnPlacements.Type.ON_GROUND;
		boolean heightmapType =
				SpawnPlacements.getHeightmapType(
						CakeWorldEntities.MERINGUE_LLAMA.get())
						== Heightmap.Types
								.MOTION_BLOCKING_NO_LEAVES;
		require(helper,
				chocolateTag && grassTag
						&& groundBody && placementType
						&& heightmapType,
				"Meringue Llama lost the exact vanilla-valid animal ground metadata contract: chocolateTag="
						+ chocolateTag + ", grassTag="
						+ grassTag + ", groundBody="
						+ groundBody + ", placementType="
						+ placementType + ", heightmapType="
						+ heightmapType);

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ResourceLocation cloudbanks = new ResourceLocation(
				CakeWorld.MODID,
				"candyfloss_cloudbanks");
		require(helper, biomes.get(cloudbanks) == null,
				"Candyfloss Cloudbanks unexpectedly exists; MOB-030's staged spawn gate must be revisited");
		for (ResourceLocation biomeId :
				new ResourceLocation[] {
						CakeWorldBiomes.CANDY_PLAINS.getId(),
						CakeWorldBiomes.COOKIE_FOREST.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN.getId(),
						CakeWorldBiomes.FUDGE_WASTES.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId()}) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect staged Meringue Llama biome gate for "
							+ biomeId);
			boolean leaked = biome.getMobSettings()
					.getMobs(MobCategory.CREATURE)
					.unwrap().stream()
					.anyMatch(spawn ->
							spawn.type == EntityType.LLAMA
									|| spawn.type
											== CakeWorldEntities
													.MERINGUE_LLAMA
													.get());
			require(helper, !leaked,
					"Meringue Llama or vanilla Llama leaked into existing biome "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.MERINGUE_LLAMA_SPAWN_EGG
						.isPresent()
						&& first.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/meringue_llama")),
				"Meringue Llama lost its spawn egg or dedicated Llama-equivalent loot table");
		VanillaRoleAdvancements.creditBredRole(
				player,
				CakeWorldEntities.MERINGUE_LLAMA.get());
		requireCriterion(helper, player,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:llama");
		helper.runAfterDelay(5, () -> {
			int rawBrightness = helper.getLevel()
					.getMaxLocalRawBrightness(spawnPos);
			require(helper,
					rawBrightness > 8
							&& Animal.checkAnimalSpawnRules(
									CakeWorldEntities
											.MERINGUE_LLAMA
											.get(),
									helper.getLevel(),
									MobSpawnType.NATURAL,
									spawnPos,
									new Random(1978L)),
					"Meringue Llama lost the inherited bright animal predicate after block-light propagation: rawBrightness="
							+ rawBrightness);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void hotFudgeBlobsKeepSizesJumpsSplitsCreamAndSafeContact(
			GameTestHelper helper) {
		var blob = new HotFudgeBlob(
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				helper.getLevel()) {
			public int sampleJumpDelay() {
				return getJumpDelay();
			}

			public float sampleAttackDamage() {
				return getAttackDamage();
			}

			public void performGroundJump() {
				jumpFromGround();
			}

			public void performLavaJump() {
				jumpInLiquid(FluidTags.LAVA);
			}
		};
		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Hot-Fudge Blob contact target");
		CompoundTag largeState = new CompoundTag();
		largeState.putInt("Size", 3);
		blob.readAdditionalSaveData(largeState);
		blob.setHealth(blob.getMaxHealth());
		BlockPos anchor = helper.absolutePos(
				new BlockPos(1, 2, 1));
		blob.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		blob.setNoAi(true);
		target.setPos(anchor.getX() + 0.25D,
				anchor.getY(), anchor.getZ());
		target.setNoAi(true);
		helper.getLevel().addFreshEntity(blob);
		helper.getLevel().addFreshEntity(target);

		int jumpDelay = blob.sampleJumpDelay();
		blob.setDeltaMovement(Vec3.ZERO);
		blob.performGroundJump();
		double groundJump = blob.getDeltaMovement().y;
		blob.setDeltaMovement(Vec3.ZERO);
		blob.performLavaJump();
		double lavaJump = blob.getDeltaMovement().y;
		require(helper,
				blob instanceof MagmaCube
						&& blob.getType()
								== CakeWorldEntities
										.HOT_FUDGE_BLOB.get()
						&& blob.getType().getCategory()
								== MobCategory.MONSTER
						&& blob.getSize() == 4
						&& close(blob.getMaxHealth(), 16.0D)
						&& close(blob.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.6D)
						&& close(blob.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								4.0D)
						&& close(blob.getAttributeValue(
								Attributes.ARMOR), 12.0D)
						&& close(blob.sampleAttackDamage(),
								6.0D)
						&& close(blob.getDimensions(
								Pose.STANDING).width,
								2.0808D)
						&& close(blob.getDimensions(
								Pose.STANDING).height,
								2.0808D)
						&& jumpDelay >= 40
						&& jumpDelay <= 116
						&& close(groundJump, 0.82D)
						&& close(lavaJump, 0.42D),
				"Hot-Fudge Blob lost Magma Cube size-four health, speed, damage, armour, body or jump scaling");
		require(helper,
				blob.fireImmune()
						&& !blob.isOnFire()
						&& close(blob.getBrightness(), 1.0D)
						&& !blob.causeFallDamage(
								100.0F, 1.0F,
								DamageSource.FALL),
				"Hot-Fudge Blob lost its fire immunity, full brightness or fall immunity");

		for (Difficulty safeDifficulty :
				new Difficulty[] {
						Difficulty.PEACEFUL,
						Difficulty.EASY,
						Difficulty.NORMAL}) {
			target.setPos(anchor.getX() + 0.25D,
					anchor.getY(), anchor.getZ());
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setSecondsOnFire(5);
			target.fallDistance = 12.0F;
			target.setDeltaMovement(Vec3.ZERO);
			LivingHurtEvent protectedHit =
					new LivingHurtEvent(target,
							DamageSource.mobAttack(blob),
							blob.sampleAttackDamage());
			HotFudgeBlobDamageSafety
					.applyForDifficulty(
							protectedHit,
							safeDifficulty);
			require(helper,
					protectedHit.isCanceled()
							&& close(target.getHealth(), 10.0D)
							&& !target.isOnFire()
							&& target.fallDistance == 0.0F
							&& target.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& target.getEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
									.getAmplifier() == 1
							&& target.hasEffect(
									MobEffects.GLOWING)
							&& target.hasEffect(
									MobEffects.SLOW_FALLING)
							&& target.hasEffect(
									MobEffects.FIRE_RESISTANCE)
							&& target.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4
							&& target.getDeltaMovement()
									.lengthSqr() > 0.0D,
					safeDifficulty
							+ " Hot-Fudge Blob did not cancel contact or lost its sticky protected bounce");
		}
		target.removeAllEffects();
		target.setDeltaMovement(Vec3.ZERO);
		LivingHurtEvent hardHit = new LivingHurtEvent(
				target, DamageSource.mobAttack(blob),
				blob.sampleAttackDamage());
		HotFudgeBlobDamageSafety.applyForDifficulty(
				hardHit, Difficulty.HARD);
		require(helper,
				!hardHit.isCanceled()
						&& close(hardHit.getAmount(), 6.0D)
						&& target.getActiveEffects().isEmpty()
						&& target.getDeltaMovement()
								.equals(Vec3.ZERO),
				"Hard size-four Hot-Fudge Blob did not retain an unmodified six-point Magma Cube contact");
		target.discard();
		blob.discard();

		HotFudgeBlob tiny =
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.create(helper.getLevel());
		require(helper, tiny != null,
				"Could not create tiny Hot-Fudge Blob fixture");
		CompoundTag tinyState = new CompoundTag();
		tinyState.putInt("Size", 0);
		tiny.readAdditionalSaveData(tinyState);
		require(helper,
				tiny.isTiny()
						&& close(tiny.getMaxHealth(), 1.0D)
						&& close(tiny.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.3D)
						&& close(tiny.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								1.0D)
						&& close(tiny.getAttributeValue(
								Attributes.ARMOR), 3.0D)
						&& close(tiny.getDimensions(
								Pose.STANDING).width,
								0.5202D)
						&& tiny.getLootTable().equals(
								BuiltInLootTables.EMPTY)
						&& blob.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/hot_fudge_blob")),
				"Hot-Fudge Blob lost tiny size scaling or size-gated magma-cream loot");

		HotFudgeBlob parent =
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.create(helper.getLevel());
		require(helper, parent != null,
				"Could not create Hot-Fudge Blob split fixture");
		parent.readAdditionalSaveData(largeState);
		BlockPos splitPos = anchor;
		parent.setPos(splitPos.getX(), splitPos.getY(),
				splitPos.getZ());
		parent.setCustomName(new TextComponent(
				"Family Fudge"));
		parent.setPersistenceRequired();
		parent.setNoAi(true);
		parent.setInvulnerable(true);
		helper.getLevel().addFreshEntity(parent);
		parent.setHealth(0.0F);
		parent.remove(Entity.RemovalReason.KILLED);
		List<HotFudgeBlob> children = helper.getLevel()
				.getEntitiesOfClass(HotFudgeBlob.class,
						new AABB(splitPos).inflate(3.0D));
		require(helper,
				parent.isRemoved()
						&& children.size() >= 2
						&& children.size() <= 4
						&& children.stream().allMatch(child ->
								child.getType()
										== CakeWorldEntities
												.HOT_FUDGE_BLOB
												.get()
										&& child.getSize() == 2
										&& close(child.getHealth(),
												4.0D)
										&& child.isPersistenceRequired()
										&& child.isNoAi()
										&& child.isInvulnerable()
										&& child.hasCustomName()
										&& "Family Fudge".equals(
												child.getName()
														.getString())),
				"Hot-Fudge Blob split leaked literal Magma Cubes or lost child size, health, name, persistence, AI or invulnerability");
		children.forEach(Entity::discard);

		BlockPos spawnPos = anchor;
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				!HotFudgeBlob.allowsNaturalSpawn(
						Difficulty.PEACEFUL)
						&& HotFudgeBlob.allowsNaturalSpawn(
								Difficulty.EASY)
						&& HotFudgeBlob.allowsNaturalSpawn(
								Difficulty.NORMAL)
						&& HotFudgeBlob.allowsNaturalSpawn(
								Difficulty.HARD)
						&& SpawnPlacements
									.getPlacementType(
											CakeWorldEntities
													.HOT_FUDGE_BLOB
													.get())
									== SpawnPlacements.Type.ON_GROUND
							&& SpawnPlacements
									.getHeightmapType(
											CakeWorldEntities
													.HOT_FUDGE_BLOB
													.get())
									== Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES
							&& SpawnPlacements.Type.ON_GROUND
									.canSpawnAt(
											helper.getLevel(),
											spawnPos,
											CakeWorldEntities
													.HOT_FUDGE_BLOB
													.get()),
				"Hot-Fudge Blob lost Peaceful suppression or exact Magma Cube ground placement on Fudge Rock");

		Biome fudgeWastes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes Hot-Fudge Blob spawning");
		requireSpawnReplacement(helper, fudgeWastes,
				EntityType.MAGMA_CUBE,
				CakeWorldEntities.HOT_FUDGE_BLOB.get(),
				MobCategory.MONSTER);
		MobSpawnSettings.SpawnerData biomeSpawn =
				fudgeWastes.getMobSettings()
						.getMobs(MobCategory.MONSTER)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.HOT_FUDGE_BLOB.get())
						.findFirst().orElse(null);
		MobSpawnSettings.SpawnerData fortressSpawn =
				NetherFortressFeature.FORTRESS_ENEMIES
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== EntityType.MAGMA_CUBE)
						.findFirst().orElse(null);
		require(helper,
				biomeSpawn != null
						&& biomeSpawn.getWeight().asInt() == 2
						&& biomeSpawn.minCount == 4
						&& biomeSpawn.maxCount == 4
						&& fortressSpawn != null
						&& fortressSpawn.getWeight().asInt() == 3
						&& fortressSpawn.minCount == 4
						&& fortressSpawn.maxCount == 4,
				"Hot-Fudge Blob lost the Nether Wastes 2/4-4 profile or the audited Fortress 3/4-4 conversion seam");

		MagmaCube literal =
				EntityType.MAGMA_CUBE.create(
						helper.getLevel());
		require(helper, literal != null,
				"Could not create literal Fortress Magma Cube fixture");
		literal.readAdditionalSaveData(largeState);
		literal.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());
		literal.setHealth(13.0F);
		literal.setCustomName(new TextComponent(
				"Fortress Fudge"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		helper.getLevel().addFreshEntity(literal);
		ResourceLocation fixtureBiome = helper.getLevel()
				.getBiome(literal.blockPosition())
				.unwrapKey().map(key -> key.location())
				.orElse(null);
		require(helper,
				fixtureBiome != null
						&& CakeWorld.MODID.equals(
								fixtureBiome.getNamespace()),
				"Hot-Fudge Blob conversion fixture is not in a CakeWorld biome: "
						+ fixtureBiome);
		HotFudgeBlob converted =
				CakeWorldMagmaCubeReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.HOT_FUDGE_BLOB.get()
						&& converted.getSize() == 4
						&& close(converted.getHealth(), 13.0D)
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.hasCustomName()
						&& "Fortress Fudge".equals(
								converted.getName()
										.getString()),
				"Fresh CakeWorld Fortress conversion lost Magma Cube size, health, name, persistence, AI or invulnerability");

		TagKey<EntityType<?>> freezeHurtsExtra =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"freeze_hurts_extra_types"));
		require(helper,
				CakeWorldEntities.HOT_FUDGE_BLOB.get()
						.is(freezeHurtsExtra)
						&& blob.canFreeze()
						&& CakeWorldItems
								.HOT_FUDGE_BLOB_SPAWN_EGG
								.isPresent(),
				"Hot-Fudge Blob lost extra freeze vulnerability or its creative/testing egg");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2031"),
						"CakeWorldHotFudgeRoleTest"));
		VanillaRoleAdvancements
				.creditKilledMagmaCubeRole(
						advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:magma_cube");
		converted.discard();
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void cupcakeCowsKeepVariantsStewsShearingAndGardenRole(
			GameTestHelper helper) {
		CupcakeCow red =
				CakeWorldEntities.CUPCAKE_COW.get()
						.create(helper.getLevel());
		require(helper, red != null,
				"Could not create Cupcake Cow fixture");
		require(helper,
				red instanceof MushroomCow
						&& red.getType()
								== CakeWorldEntities
										.CUPCAKE_COW.get()
						&& red.getType().getCategory()
								== MobCategory.CREATURE
						&& red.getMushroomType()
								== MushroomCow.MushroomType.RED
						&& close(red.getMaxHealth(), 10.0D)
						&& close(red.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.2D)
						&& close(red.getDimensions(
								Pose.STANDING).width,
								0.9D)
						&& close(red.getDimensions(
								Pose.STANDING).height,
								1.4D)
						&& red.getMaxSpawnClusterSize() == 4
						&& red.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/cupcake_cow")),
				"Cupcake Cow lost its genuine Mooshroom type, red default, cow attributes/body/cluster or custom loot");

		CompoundTag brownState = new CompoundTag();
		brownState.putString("Type", "brown");
		CupcakeCow brown =
				CakeWorldEntities.CUPCAKE_COW.get()
						.create(helper.getLevel());
		CupcakeCow reloaded =
				CakeWorldEntities.CUPCAKE_COW.get()
						.create(helper.getLevel());
		require(helper, brown != null && reloaded != null,
				"Could not create brown/reload Cupcake Cow fixtures");
		brown.readAdditionalSaveData(brownState);
		CompoundTag savedVariant = new CompoundTag();
		brown.addAdditionalSaveData(savedVariant);
		reloaded.readAdditionalSaveData(savedVariant);
		require(helper,
				brown.getMushroomType()
								== MushroomCow.MushroomType.BROWN
						&& "brown".equals(
								savedVariant.getString("Type"))
						&& reloaded.getMushroomType()
								== MushroomCow.MushroomType.BROWN,
				"Cupcake Cow lost red/brown variant save and reload");

		Player bowlPlayer = helper.makeMockPlayer();
		bowlPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.BOWL));
		InteractionResult ordinaryStew =
				red.mobInteract(bowlPlayer,
						InteractionHand.MAIN_HAND);
		require(helper,
				ordinaryStew.consumesAction()
						&& bowlPlayer.getItemInHand(
								InteractionHand.MAIN_HAND)
								.is(Items.MUSHROOM_STEW),
				"Adult red Cupcake Cow did not exchange a bowl for mushroom stew");

		Player flowerPlayer = helper.makeMockPlayer();
		flowerPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.DANDELION, 2));
		InteractionResult flowerFed =
				brown.mobInteract(flowerPlayer,
						InteractionHand.MAIN_HAND);
		require(helper,
				flowerFed.consumesAction()
						&& flowerPlayer.getItemInHand(
								InteractionHand.MAIN_HAND)
								.getCount() == 1,
				"Brown Cupcake Cow did not consume one valid flower");
		flowerPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.POPPY));
		InteractionResult refusedSecondFlower =
				brown.mobInteract(flowerPlayer,
						InteractionHand.MAIN_HAND);
		require(helper,
				refusedSecondFlower.consumesAction()
						&& flowerPlayer.getItemInHand(
								InteractionHand.MAIN_HAND)
								.getCount() == 1,
				"Brown Cupcake Cow replaced a pending flower effect instead of refusing the second flower");
		flowerPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.BOWL));
		InteractionResult suspiciousStew =
				brown.mobInteract(flowerPlayer,
						InteractionHand.MAIN_HAND);
		ItemStack suspicious = flowerPlayer.getItemInHand(
				InteractionHand.MAIN_HAND);
		ListTag stewEffects = suspicious.hasTag()
				? suspicious.getTag().getList(
						SuspiciousStewItem.EFFECTS_TAG, 10)
				: new ListTag();
		require(helper,
				suspiciousStew.consumesAction()
						&& suspicious.is(
								Items.SUSPICIOUS_STEW)
						&& stewEffects.size() == 1
						&& stewEffects.getCompound(0)
								.getInt(
										SuspiciousStewItem
												.EFFECT_ID_TAG)
								> 0
						&& stewEffects.getCompound(0)
								.getInt(
										SuspiciousStewItem
												.EFFECT_DURATION_TAG)
								> 0,
				"Cupcake Cow lost brown-flower suspicious-stew state or effect NBT");

		LightningBolt firstBolt =
				EntityType.LIGHTNING_BOLT.create(
						helper.getLevel());
		LightningBolt secondBolt =
				EntityType.LIGHTNING_BOLT.create(
						helper.getLevel());
		require(helper, firstBolt != null && secondBolt != null,
				"Could not create lightning fixtures");
		red.thunderHit(helper.getLevel(), firstBolt);
		MushroomCow.MushroomType afterFirstBolt =
				red.getMushroomType();
		red.thunderHit(helper.getLevel(), firstBolt);
		MushroomCow.MushroomType afterRepeatedBolt =
				red.getMushroomType();
		red.thunderHit(helper.getLevel(), secondBolt);
		require(helper,
				afterFirstBolt
								== MushroomCow.MushroomType.BROWN
						&& afterRepeatedBolt == afterFirstBolt
						&& red.getMushroomType()
								== MushroomCow.MushroomType.RED,
				"Cupcake Cow lost lightning switching or the repeated-bolt UUID guard");

		boolean bredRed = false;
		boolean bredBrown = false;
		for (int index = 0; index < 128; index++) {
			CupcakeCow child = red.getBreedOffspring(
					helper.getLevel(), reloaded);
			require(helper,
					child != null
							&& child.getType()
									== CakeWorldEntities
											.CUPCAKE_COW
											.get(),
					"Cupcake Cow breeding leaked a literal Mooshroom");
			bredRed |= child.getMushroomType()
					== MushroomCow.MushroomType.RED;
			bredBrown |= child.getMushroomType()
					== MushroomCow.MushroomType.BROWN;
		}
		require(helper, bredRed && bredBrown,
				"Cupcake Cow did not preserve vanilla mixed-parent variant inheritance");

		BlockPos vanillaShearPos = helper.absolutePos(
				new BlockPos(2, 2, 2));
		helper.getLevel().getEntitiesOfClass(
				CocoaCow.class,
				new AABB(vanillaShearPos).inflate(2.0D))
				.forEach(Entity::discard);
		helper.getLevel().getEntitiesOfClass(
				ItemEntity.class,
				new AABB(vanillaShearPos).inflate(2.0D))
				.forEach(Entity::discard);
		CupcakeCow vanillaShear =
				CakeWorldEntities.CUPCAKE_COW.get()
						.create(helper.getLevel());
		require(helper, vanillaShear != null,
				"Could not create vanilla shear fixture");
		vanillaShear.setPos(vanillaShearPos.getX(),
				vanillaShearPos.getY(),
				vanillaShearPos.getZ());
		vanillaShear.setHealth(7.0F);
		vanillaShear.setCustomName(
				new TextComponent("Cherry Cupcake"));
		vanillaShear.setCustomNameVisible(true);
		vanillaShear.setPersistenceRequired();
		vanillaShear.setInvulnerable(true);
		vanillaShear.setNoAi(true);
		helper.getLevel().addFreshEntity(vanillaShear);
		require(helper,
				vanillaShear.readyForShearing()
						&& vanillaShear.isShearable(
								new ItemStack(Items.SHEARS),
								helper.getLevel(),
								vanillaShearPos),
				"Adult Cupcake Cow did not expose vanilla and Forge shearing readiness");
		vanillaShear.shear(SoundSource.PLAYERS);
		List<CocoaCow> vanillaShearCows =
				helper.getLevel().getEntitiesOfClass(
						CocoaCow.class,
						new AABB(vanillaShearPos)
								.inflate(2.0D))
				.stream()
				.filter(cow -> cow.distanceToSqr(
						vanillaShearPos.getX(),
						vanillaShearPos.getY(),
						vanillaShearPos.getZ())
						< 0.01D)
				.toList();
		int redMushrooms = helper.getLevel()
				.getEntitiesOfClass(ItemEntity.class,
						new AABB(vanillaShearPos)
								.inflate(2.0D))
				.stream()
				.filter(drop -> drop.getItem()
						.is(Items.RED_MUSHROOM))
				.mapToInt(drop -> drop.getItem().getCount())
				.sum();
		require(helper,
				vanillaShear.isRemoved()
						&& vanillaShearCows.size() == 1
						&& vanillaShearCows.get(0).getType()
								== CakeWorldEntities.COCOA_COW
										.get()
						&& close(vanillaShearCows.get(0)
								.getHealth(), 7.0D)
						&& vanillaShearCows.get(0)
								.isPersistenceRequired()
						&& vanillaShearCows.get(0)
								.isInvulnerable()
						&& vanillaShearCows.get(0)
								.hasCustomName()
						&& vanillaShearCows.get(0)
								.isCustomNameVisible()
						&& "Cherry Cupcake".equals(
								vanillaShearCows.get(0)
										.getName()
										.getString())
						&& redMushrooms == 5,
				"Vanilla Cupcake Cow shearing did not produce one state-preserving Cocoa Cow and five red mushrooms");

		BlockPos forgeShearPos = helper.absolutePos(
				new BlockPos(8, 2, 2));
		helper.getLevel().getEntitiesOfClass(
				CocoaCow.class,
				new AABB(forgeShearPos).inflate(2.0D))
				.forEach(Entity::discard);
		CupcakeCow forgeShear =
				CakeWorldEntities.CUPCAKE_COW.get()
						.create(helper.getLevel());
		require(helper, forgeShear != null,
				"Could not create Forge shear fixture");
		forgeShear.readAdditionalSaveData(brownState);
		forgeShear.setPos(forgeShearPos.getX(),
				forgeShearPos.getY(), forgeShearPos.getZ());
		helper.getLevel().addFreshEntity(forgeShear);
		List<ItemStack> forgeDrops = forgeShear.onSheared(
				helper.makeMockPlayer(),
				new ItemStack(Items.SHEARS),
				helper.getLevel(), forgeShearPos, 3);
		List<CocoaCow> forgeShearCows =
				helper.getLevel().getEntitiesOfClass(
						CocoaCow.class,
						new AABB(forgeShearPos)
								.inflate(2.0D))
				.stream()
				.filter(cow -> cow.distanceToSqr(
						forgeShearPos.getX(),
						forgeShearPos.getY(),
						forgeShearPos.getZ())
						< 0.01D)
				.toList();
		require(helper,
				forgeShear.isRemoved()
						&& forgeShearCows.size() == 1
						&& forgeDrops.size() == 5
						&& forgeDrops.stream().allMatch(
								stack -> stack.is(
										Items.BROWN_MUSHROOM)
										&& stack.getCount() == 1),
				"Forge Cupcake Cow shearing did not return five brown mushrooms or convert to Cocoa Cow");

		BlockPos spawnPos = helper.absolutePos(
				new BlockPos(5, 2, 5));
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.LIGHT.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.runAfterDelay(5, () -> {
			require(helper,
				helper.getLevel()
						.getBlockState(spawnPos.below())
						.is(BlockTags
								.MOOSHROOMS_SPAWNABLE_ON)
						&& helper.getLevel()
								.getRawBrightness(spawnPos, 0)
								> 8
						&& CupcakeCow
								.checkCupcakeCowSpawnRules(
										CakeWorldEntities
												.CUPCAKE_COW
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										spawnPos,
										new Random(33L))
						&& SpawnPlacements.checkSpawnRules(
								CakeWorldEntities
										.CUPCAKE_COW.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								spawnPos,
								new Random(34L))
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.CUPCAKE_COW
												.get())
								== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.CUPCAKE_COW
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& close(red.getWalkTargetValue(
								spawnPos,
								helper.getLevel()), 10.0D),
				"Cupcake Cow lost bright ground placement, edible spawn surface or surface preference");
			vanillaShearCows.forEach(Entity::discard);
			forgeShearCows.forEach(Entity::discard);
			helper.getLevel().getEntitiesOfClass(
					ItemEntity.class,
					new AABB(vanillaShearPos).inflate(2.0D))
					.forEach(Entity::discard);
			helper.getLevel().setBlock(spawnPos.below(),
					Blocks.AIR.defaultBlockState(), 3);
			helper.getLevel().setBlock(spawnPos,
					Blocks.AIR.defaultBlockState(), 3);
			helper.succeed();
		});

		MobSpawnSettingsBuilder futureSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureGarden =
				new BiomeLoadingEvent(
						new ResourceLocation(
								CakeWorld.MODID,
								"cupcake_gardens"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings
										.EMPTY),
						futureSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(
				futureGarden);
		MobSpawnSettings.SpawnerData futureSpawn =
				futureSpawns
						.getSpawner(MobCategory.CREATURE)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.CUPCAKE_COW.get())
						.findFirst().orElse(null);
		require(helper,
				futureSpawn != null
						&& futureSpawn.getWeight().asInt() == 8
						&& futureSpawn.minCount == 4
						&& futureSpawn.maxCount == 8
						&& futureSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType.MOOSHROOM),
				"Future Cupcake Gardens hook lost the exact Mushroom Fields 8/4-8 replacement profile");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = helper.getLevel()
					.registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.MOOSHROOM
											|| spawn.type
													== CakeWorldEntities
															.CUPCAKE_COW
															.get()),
					"Current biome leaked Mooshroom/Cupcake Cow spawning before Cupcake Gardens exists: "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.CUPCAKE_COW_SPAWN_EGG
						.isPresent(),
				"Cupcake Cow lost its creative/testing egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2033"),
						"CakeWorldCupcakeCowRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.CUPCAKE_COW.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:mooshroom");
	}

	@GameTest(template = EMPTY)
	public static void sherbetOcelotsKeepTrustPreyAndJungleRole(
			GameTestHelper helper) {
		SherbetOcelotProbe ocelot =
				new SherbetOcelotProbe(helper.getLevel());
		CompoundTag initialState = new CompoundTag();
		ocelot.addAdditionalSaveData(initialState);
		float healthBeforeFall = ocelot.getHealth();
		boolean causedFallDamage = ocelot.causeFallDamage(
				20.0F, 1.0F, DamageSource.FALL);
		require(helper,
				ocelot instanceof Ocelot
						&& ocelot.getType()
								== CakeWorldEntities
										.SHERBET_OCELOT.get()
						&& ocelot.getType().getCategory()
								== MobCategory.CREATURE
						&& close(ocelot.getMaxHealth(), 10.0D)
						&& close(ocelot.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.3D)
						&& close(ocelot.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								3.0D)
						&& close(ocelot.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(ocelot.getDimensions(
								Pose.STANDING).height,
								0.7D)
						&& ocelot.getMaxSpawnClusterSize() == 4
						&& ocelot.getAmbientSoundInterval()
								== 900
						&& !causedFallDamage
						&& close(ocelot.getHealth(),
								healthBeforeFall)
						&& !initialState.getBoolean(
								"Trusting")
						&& ocelot.countGoalsNamed(
								"OcelotAvoidEntityGoal")
								== 1
						&& ocelot.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 2
						&& ocelot.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/sherbet_ocelot")),
				"Sherbet Ocelot lost its genuine type, attributes, body, zero-fall, shy-goal, prey-goal, sound interval or loot contract");

		ocelot.getMoveControl().setWantedPosition(
				ocelot.getX() + 2.0D, ocelot.getY(),
				ocelot.getZ(), 0.6D);
		ocelot.customServerAiStep();
		require(helper,
				ocelot.getPose() == Pose.CROUCHING
						&& ocelot.isSteppingCarefully()
						&& !ocelot.isSprinting(),
				"Sherbet Ocelot lost its scared/tempted crouching pose");
		ocelot.getMoveControl().setWantedPosition(
				ocelot.getX() + 4.0D, ocelot.getY(),
				ocelot.getZ(), 1.33D);
		ocelot.customServerAiStep();
		require(helper,
				ocelot.getPose() == Pose.STANDING
						&& ocelot.isSprinting(),
				"Sherbet Ocelot lost its attack/flee sprint pose");

		require(helper,
				ocelot.isFood(new ItemStack(Items.COD))
						&& ocelot.isFood(
								new ItemStack(Items.SALMON))
						&& !ocelot.isFood(new ItemStack(
								Items.TROPICAL_FISH)),
				"Sherbet Ocelot lost its exact Cod/Salmon diet");

		ServerPlayer trustingPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2034"),
						"CakeWorldSherbetOcelotTrustTest"));
		BlockPos trustPos = helper.absolutePos(
				new BlockPos(4, 2, 4));
		ocelot.setPos(trustPos.getX() + 0.5D,
				trustPos.getY(), trustPos.getZ() + 0.5D);
		trustingPlayer.setPos(ocelot.getX() + 1.0D,
				ocelot.getY(), ocelot.getZ());
		trustingPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.COD, 2));
		helper.getLevel().players().add(trustingPlayer);
		try {
			require(helper, ocelot.startFishTemptation(),
					"Sherbet Ocelot did not enter its real fish TemptGoal for a nearby player");
			ocelot.seedRandom(0L);
			InteractionResult trustResult = ocelot.mobInteract(
					trustingPlayer, InteractionHand.MAIN_HAND);
			CompoundTag trustedState = new CompoundTag();
			ocelot.addAdditionalSaveData(trustedState);
			require(helper,
					trustResult.consumesAction()
							&& trustingPlayer
									.getItemInHand(
											InteractionHand
													.MAIN_HAND)
									.getCount() == 1
							&& trustedState.getBoolean(
									"Trusting")
							&& ocelot.countGoalsNamed(
									"OcelotAvoidEntityGoal")
									== 0,
					"Sherbet Ocelot did not consume one fish, pass the deterministic one-in-three trust roll and remove player avoidance");

			SherbetOcelotProbe restored =
					new SherbetOcelotProbe(
							helper.getLevel());
			restored.readAdditionalSaveData(trustedState);
			CompoundTag reloadedState = new CompoundTag();
			restored.addAdditionalSaveData(reloadedState);
			restored.setTestTickCount(2401);
			require(helper,
					reloadedState.getBoolean("Trusting")
							&& restored.countGoalsNamed(
									"OcelotAvoidEntityGoal")
									== 0
							&& !restored.removeWhenFarAway(
									256.0D),
					"Sherbet Ocelot lost trust NBT, trusted approach or trusted despawn immunity");
		} finally {
			helper.getLevel().players().remove(
					trustingPlayer);
		}

		SherbetOcelotProbe oldUntrusted =
				new SherbetOcelotProbe(helper.getLevel());
		oldUntrusted.setTestTickCount(2401);
		require(helper,
				oldUntrusted.countGoalsNamed(
						"OcelotAvoidEntityGoal") == 1
						&& oldUntrusted.removeWhenFarAway(
								256.0D),
				"Old untrusted Sherbet Ocelot no longer uses vanilla conditional despawning");

		SherbetOcelot partner =
				CakeWorldEntities.SHERBET_OCELOT.get()
						.create(helper.getLevel());
		SherbetOcelot child = ocelot.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper,
				partner != null && child != null
						&& child.getType()
								== CakeWorldEntities
										.SHERBET_OCELOT.get(),
				"Sherbet Ocelot breeding leaked a literal Ocelot");

		BlockPos preyPos = helper.absolutePos(
				new BlockPos(7, 2, 7));
		for (int offset = 0; offset <= 11; offset++) {
			BlockPos dryPreyPos = preyPos.offset(offset, 0, 0);
			helper.getLevel().setBlock(
					dryPreyPos.below(),
					CakeWorldBlocks.CHOCOLATE_SPONGE.get()
							.defaultBlockState(), 3);
			helper.getLevel().setBlock(dryPreyPos,
					Blocks.AIR.defaultBlockState(), 3);
			helper.getLevel().setBlock(
					dryPreyPos.above(),
					Blocks.AIR.defaultBlockState(), 3);
		}
		Chicken chicken = EntityType.CHICKEN.create(
				helper.getLevel());
		require(helper, chicken != null,
				"Could not create Chicken prey fixture");
		chicken.setPos(preyPos.getX() + 0.5D,
				preyPos.getY(), preyPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(chicken);
		SherbetOcelotProbe chickenHunter =
				new SherbetOcelotProbe(helper.getLevel());
		chickenHunter.setPos(chicken.getX() + 2.0D,
				chicken.getY(), chicken.getZ());
		require(helper,
				chickenHunter.acquirePreyTarget(200)
						&& chickenHunter.getTarget()
								== chicken,
				"Sherbet Ocelot did not select Chicken through its inherited prey goal");
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			float chickenHealth = chicken.getHealth();
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				chicken.removeAllEffects();
				chicken.setHealth(chickenHealth);
				chicken.setSecondsOnFire(5);
				chicken.fallDistance = 7.0F;
				chicken.setDeltaMovement(Vec3.ZERO);
				require(helper,
						chickenHunter
								.doHurtTarget(chicken)
								&& close(
										chicken.getHealth(),
										chickenHealth)
								&& !chicken.isOnFire()
								&& chicken.fallDistance
										== 0.0F
								&& chicken.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& chicken.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& chicken.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& chicken.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4,
						safeDifficulty
								+ " Sherbet Ocelot pounce caused health damage or lacked sticky rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			chicken.removeAllEffects();
			chicken.invulnerableTime = 0;
			require(helper,
					chickenHunter.doHurtTarget(chicken)
							&& close(chicken.getHealth(),
									chickenHealth - 3.0D),
					"Hard Sherbet Ocelot lost the exact three-point Ocelot prey attack");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}
		chicken.discard();

		Turtle babyTurtle = EntityType.TURTLE.create(
				helper.getLevel());
		require(helper, babyTurtle != null,
				"Could not create baby Turtle prey fixture");
		babyTurtle.setBaby(true);
		babyTurtle.setPos(preyPos.getX() + 4.5D,
				preyPos.getY(), preyPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(babyTurtle);
		SherbetOcelotProbe turtleHunter =
				new SherbetOcelotProbe(helper.getLevel());
		turtleHunter.setPos(babyTurtle.getX() + 2.0D,
				babyTurtle.getY(), babyTurtle.getZ());
		require(helper,
				!babyTurtle.isInWater()
						&& turtleHunter.acquirePreyTarget(
								300)
						&& turtleHunter.getTarget()
								== babyTurtle,
				"Sherbet Ocelot did not select a baby Turtle on land through its inherited prey goal");
		babyTurtle.discard();

		Turtle adultTurtle = EntityType.TURTLE.create(
				helper.getLevel());
		Rabbit rabbit = EntityType.RABBIT.create(
				helper.getLevel());
		require(helper, adultTurtle != null && rabbit != null,
				"Could not create non-prey fixtures");
		adultTurtle.setPos(preyPos.getX() + 8.5D,
				preyPos.getY(), preyPos.getZ() + 0.5D);
		rabbit.setPos(preyPos.getX() + 9.5D,
				preyPos.getY(), preyPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(adultTurtle);
		helper.getLevel().addFreshEntity(rabbit);
		SherbetOcelotProbe selectiveHunter =
				new SherbetOcelotProbe(helper.getLevel());
		selectiveHunter.setPos(preyPos.getX() + 7.0D,
				preyPos.getY(), preyPos.getZ() + 0.5D);
		require(helper,
				!selectiveHunter.acquirePreyTarget(400)
						&& selectiveHunter.getTarget()
								== null,
				"Sherbet Ocelot incorrectly targets adult Turtles or Rabbits in 1.18.2");

		int seaLevel = helper.getLevel().getSeaLevel();
		BlockPos localSpawn = helper.absolutePos(
				new BlockPos(5, 2, 5));
		BlockPos spawnPos = new BlockPos(localSpawn.getX(),
				seaLevel + 1, localSpawn.getZ());
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		SherbetOcelot spawnProbe =
				CakeWorldEntities.SHERBET_OCELOT.get()
						.create(helper.getLevel());
		require(helper, spawnProbe != null,
				"Could not create Sherbet Ocelot spawn fixture");
		spawnProbe.setPos(spawnPos.getX() + 0.5D,
				spawnPos.getY(), spawnPos.getZ() + 0.5D);
		Random rejectCandidate = new Random() {
			@Override
			public int nextInt(int bound) {
				return 0;
			}
		};
		Random acceptCandidate = new Random() {
			@Override
			public int nextInt(int bound) {
				return 1;
			}
		};
		require(helper,
				helper.getLevel()
						.getBlockState(spawnPos.below())
						.is(SherbetOcelot.SPAWNABLE_ON)
						&& spawnProbe.checkSpawnObstruction(
								helper.getLevel())
						&& !SherbetOcelot
								.checkSherbetOcelotSpawnRules(
										CakeWorldEntities
												.SHERBET_OCELOT
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										spawnPos,
										rejectCandidate)
						&& SherbetOcelot
								.checkSherbetOcelotSpawnRules(
										CakeWorldEntities
												.SHERBET_OCELOT
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										spawnPos,
										acceptCandidate)
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.SHERBET_OCELOT
												.get())
								== SpawnPlacements.Type.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.SHERBET_OCELOT
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Sherbet Ocelot lost edible obstruction, exact two-thirds probability or registered ground placement");
		BlockPos lowPos = new BlockPos(spawnPos.getX(),
				seaLevel - 1, spawnPos.getZ());
		helper.getLevel().setBlock(lowPos.below(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(lowPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(lowPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		spawnProbe.setPos(lowPos.getX() + 0.5D,
				lowPos.getY(), lowPos.getZ() + 0.5D);
		require(helper,
				helper.getLevel().getBlockState(
						lowPos.below())
						.is(SherbetOcelot.SPAWNABLE_ON)
						&& !spawnProbe.checkSpawnObstruction(
								helper.getLevel()),
				"Sherbet Ocelot ignored the vanilla sea-level obstruction boundary");

		MobSpawnSettingsBuilder futureSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureJungle =
				new BiomeLoadingEvent(
						new ResourceLocation(
								CakeWorld.MODID,
								"gummy_jungle"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings
										.EMPTY),
						futureSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(
				futureJungle);
		MobSpawnSettings.SpawnerData futureSpawn =
				futureSpawns
						.getSpawner(MobCategory.MONSTER)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.SHERBET_OCELOT
										.get())
						.findFirst().orElse(null);
		require(helper,
				futureSpawn != null
						&& futureSpawn.getWeight().asInt() == 2
						&& futureSpawn.minCount == 1
						&& futureSpawn.maxCount == 3
						&& futureSpawns
								.getSpawner(
										MobCategory.MONSTER)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType.OCELOT)
						&& futureSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== CakeWorldEntities
														.SHERBET_OCELOT
														.get()),
				"Future Gummy Jungle hook lost vanilla Jungle's deliberate MONSTER-list 2/1-3 Ocelot profile");

		MobSpawnSettingsBuilder futureDunesSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureDunes =
				new BiomeLoadingEvent(
						new ResourceLocation(
								CakeWorld.MODID,
								"sherbet_dunes"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings
										.EMPTY),
						futureDunesSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(
				futureDunes);
		MobSpawnSettings.SpawnerData dunesSpawn =
				futureDunesSpawns
						.getSpawner(MobCategory.MONSTER)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.SHERBET_OCELOT
										.get())
						.findFirst().orElse(null);
		require(helper,
				dunesSpawn != null
						&& dunesSpawn.getWeight().asInt() == 1
						&& dunesSpawn.minCount == 1
						&& dunesSpawn.maxCount == 1
						&& futureDunesSpawns
								.getSpawner(
										MobCategory.MONSTER)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType.OCELOT)
						&& futureDunesSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== CakeWorldEntities
														.SHERBET_OCELOT
														.get()),
				"Future Sherbet Dunes hook lost its rare MONSTER-list 1/1-1 desert-edge profile");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = helper.getLevel()
					.registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper,
					biome != null
							&& List.of(
									MobCategory.MONSTER,
									MobCategory.CREATURE)
									.stream().allMatch(
											category -> biome
													.getMobSettings()
													.getMobs(category)
													.unwrap().stream()
													.noneMatch(spawn ->
															spawn.type
																	== EntityType
																			.OCELOT
															|| spawn.type
																	== CakeWorldEntities
																			.SHERBET_OCELOT
																			.get())),
					"Current biome leaked Ocelot/Sherbet Ocelot spawning before Gummy Jungle exists: "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.SHERBET_OCELOT_SPAWN_EGG
						.isPresent(),
				"Sherbet Ocelot lost its creative/testing egg");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed3034"),
						"CakeWorldSherbetOcelotRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.SHERBET_OCELOT.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:ocelot");

		chicken.discard();
		babyTurtle.discard();
		adultTurtle.discard();
		rabbit.discard();
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(lowPos.below(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.succeed();
	}

	private static final class SherbetOcelotProbe
			extends SherbetOcelot {
		private SherbetOcelotProbe(Level level) {
			super(CakeWorldEntities.SHERBET_OCELOT.get(),
					level);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void setTestTickCount(int ticks) {
			tickCount = ticks;
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private boolean startFishTemptation() {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if ("OcelotTemptGoal".equals(wrapped
						.getGoal().getClass()
						.getSimpleName())
						&& wrapped.canUse()) {
					wrapped.start();
					return wrapped.isRunning();
				}
			}
			return false;
		}

		private boolean acquirePreyTarget(int attempts) {
			for (int attempt = 0; attempt < attempts;
					attempt++) {
				for (WrappedGoal wrapped :
						targetSelector.getAvailableGoals()) {
					if ("NearestAttackableTargetGoal"
							.equals(wrapped.getGoal()
									.getClass()
									.getSimpleName())
							&& wrapped.canUse()) {
						wrapped.start();
						return getTarget() != null;
					}
				}
			}
			return false;
		}
	}

	@GameTest(template = EMPTY)
	public static void chocolatePandasKeepGenesMoodsCaneAndSafeBites(
			GameTestHelper helper) {
		ChocolatePandaProbe panda =
				new ChocolatePandaProbe(helper.getLevel());
		require(helper,
				panda instanceof Panda
						&& panda.getType()
								== CakeWorldEntities
										.CHOCOLATE_PANDA.get()
						&& panda.getType().getCategory()
								== MobCategory.CREATURE
						&& close(panda.getMaxHealth(), 20.0D)
						&& close(panda.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.15D)
						&& close(panda.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								6.0D)
						&& close(panda.getDimensions(
								Pose.STANDING).width,
								1.3D)
						&& close(panda.getDimensions(
								Pose.STANDING).height,
								1.25D)
						&& panda.getMaxSpawnClusterSize() == 4
						&& panda.getAmbientSoundInterval()
								== 120
						&& !panda.canBeLeashed(null)
						&& panda.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/chocolate_panda"))
						&& panda.countGoalsNamed(
								"PandaRollGoal") == 1
						&& panda.countGoalsNamed(
								"PandaSneezeGoal") == 1
						&& panda.countGoalsNamed(
								"PandaSitGoal") == 1
						&& panda.countGoalsNamed(
								"CakeWorldPandaBreedGoal")
								== 1
						&& panda.countGoalsNamed(
								"CakeWorldPandaFoodGoal")
								== 1
						&& panda.countGoalsNamed(
								"TemptGoal") == 2
						&& panda.countTargetGoalsNamed(
								"PandaHurtByTargetGoal")
								== 1,
				"Chocolate Panda lost its genuine type, exact physical, loot, no-leash or inherited/custom AI contract");

		for (Panda.Gene gene : List.of(
				Panda.Gene.NORMAL,
				Panda.Gene.LAZY,
				Panda.Gene.WORRIED,
				Panda.Gene.PLAYFUL,
				Panda.Gene.AGGRESSIVE)) {
			panda.setMainGene(gene);
			panda.setHiddenGene(Panda.Gene.NORMAL);
			require(helper, panda.getVariant() == gene,
					"Chocolate Panda lost dominant gene phenotype "
							+ gene);
		}
		panda.setMainGene(Panda.Gene.BROWN);
		panda.setHiddenGene(Panda.Gene.NORMAL);
		require(helper,
				panda.getVariant() == Panda.Gene.NORMAL,
				"Chocolate Panda lost recessive brown masking");
		panda.setHiddenGene(Panda.Gene.BROWN);
		require(helper,
				panda.getVariant() == Panda.Gene.BROWN
						&& panda.isBrown(),
				"Chocolate Panda lost homozygous brown phenotype");
		panda.setMainGene(Panda.Gene.WEAK);
		panda.setHiddenGene(Panda.Gene.WEAK);
		panda.setAttributes();
		require(helper,
				panda.getVariant() == Panda.Gene.WEAK
						&& panda.isWeak()
						&& close(panda.getMaxHealth(),
								10.0D),
				"Chocolate Panda lost weak recessive health");
		ChocolatePandaProbe lazy =
				new ChocolatePandaProbe(helper.getLevel());
		lazy.setMainGene(Panda.Gene.LAZY);
		lazy.setHiddenGene(Panda.Gene.NORMAL);
		lazy.setAttributes();
		require(helper,
				lazy.isLazy()
						&& close(lazy.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.07D),
				"Chocolate Panda lost lazy movement speed");

		panda.setMainGene(Panda.Gene.PLAYFUL);
		panda.setHiddenGene(Panda.Gene.AGGRESSIVE);
		CompoundTag genes = new CompoundTag();
		panda.addAdditionalSaveData(genes);
		ChocolatePandaProbe restored =
				new ChocolatePandaProbe(helper.getLevel());
		restored.readAdditionalSaveData(genes);
		require(helper,
				"playful".equals(
						genes.getString("MainGene"))
						&& "aggressive".equals(
								genes.getString(
										"HiddenGene"))
						&& restored.getMainGene()
								== Panda.Gene.PLAYFUL
						&& restored.getHiddenGene()
								== Panda.Gene.AGGRESSIVE,
				"Chocolate Panda lost main/hidden gene NBT");

		ChocolatePandaProbe brownParent =
				new ChocolatePandaProbe(helper.getLevel());
		brownParent.setMainGene(Panda.Gene.BROWN);
		brownParent.setHiddenGene(Panda.Gene.BROWN);
		ChocolatePandaProbe weakParent =
				new ChocolatePandaProbe(helper.getLevel());
		weakParent.setMainGene(Panda.Gene.WEAK);
		weakParent.setHiddenGene(Panda.Gene.WEAK);
		ChocolatePanda child = brownParent
				.getBreedOffspring(helper.getLevel(),
						weakParent);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities
										.CHOCOLATE_PANDA.get()
						&& List.of(Panda.Gene.values())
								.contains(child.getMainGene())
						&& List.of(Panda.Gene.values())
								.contains(child.getHiddenGene()),
				"Chocolate Panda offspring leaked a literal Panda or produced invalid genes");
		ChocolatePandaProbe inheritedChild =
				new ChocolatePandaProbe(helper.getLevel());
		inheritedChild.seedRandom(1978L);
		inheritedChild.setGeneFromParents(
				brownParent, weakParent);
		inheritedChild.setAttributes();
		require(helper,
				Set.of(inheritedChild.getMainGene(),
						inheritedChild.getHiddenGene())
						.equals(Set.of(
								Panda.Gene.BROWN,
								Panda.Gene.WEAK))
						&& inheritedChild.getVariant()
								== Panda.Gene.NORMAL,
				"Chocolate Panda lost fixed-seed parent-gene inheritance outside vanilla's deliberate mutation cases");

		ChocolatePandaProbe animation =
				new ChocolatePandaProbe(helper.getLevel());
		animation.setNoAi(true);
		animation.roll(true);
		for (int tick = 0; tick < 33; tick++) {
			animation.tick();
		}
		require(helper, !animation.isRolling(),
				"Chocolate Panda roll did not complete after its exact 32-step sequence");
		animation.sneeze(true);
		for (int tick = 0; tick < 21; tick++) {
			animation.tick();
		}
		animation.sit(true);
		animation.eat(true);
		animation.setOnBack(true);
		require(helper,
				!animation.isSneezing()
						&& animation.getSneezeCounter() == 0
						&& animation.isSitting()
						&& animation.isEating()
						&& animation.isOnBack()
						&& !animation.canPerformAction(),
				"Chocolate Panda lost sneeze completion, sitting, eating or on-back state");

		ChocolatePandaProbe worried =
				new ChocolatePandaProbe(helper.getLevel());
		worried.setMainGene(Panda.Gene.WORRIED);
		worried.setHiddenGene(Panda.Gene.NORMAL);
		try {
			helper.getLevel().setRainLevel(1.0F);
			helper.getLevel().setThunderLevel(1.0F);
			worried.tick();
			require(helper,
					worried.isWorried()
							&& worried.isScared()
							&& worried.isSitting()
							&& !worried.isEating(),
					"Worried Chocolate Panda lost its thunder fear and sitting response");
		} finally {
			helper.getLevel().setRainLevel(0.0F);
			helper.getLevel().setThunderLevel(0.0F);
			helper.getLevel().setWeatherParameters(
					6000, 0, false, false);
		}

		require(helper,
				panda.isFood(new ItemStack(
						Blocks.BAMBOO))
						&& panda.isFood(new ItemStack(
								CakeWorldItems
										.SPRINKLE_SEEDS
										.get()))
						&& panda.isFood(new ItemStack(
								CakeWorldBlocks
										.CANDY_CANE_PILLAR
										.get()))
						&& !panda.isFood(new ItemStack(
								CakeWorldItems
										.SIMPLE_BISCUIT
										.get()))
						&& new ItemStack(CakeWorldItems
								.SPRINKLE_SEEDS.get())
								.is(ChocolatePanda.FOODS)
						&& new ItemStack(CakeWorldBlocks
								.CANDY_CANE_PILLAR.get())
								.is(ChocolatePanda.FOODS)
						&& CakeWorldBlocks.CANDY_SPROUT
								.get().defaultBlockState()
								.is(ChocolatePanda
										.BREEDING_PLANTS)
						&& CakeWorldBlocks
								.CANDY_CANE_PILLAR.get()
								.defaultBlockState()
								.is(ChocolatePanda
										.BREEDING_PLANTS),
				"Chocolate Panda lost Bamboo compatibility or tagged Candy Sprout/Candy-Cane food roles");

		BlockPos anchor = helper.absolutePos(
				new BlockPos(4, 2, 4));
		ServerPlayer feeder = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed3035"),
						"CakeWorldChocolatePandaFoodTest"));
		ChocolatePandaProbe handFed =
				new ChocolatePandaProbe(helper.getLevel());
		handFed.setPos(anchor.getX() + 0.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		feeder.setPos(handFed.getX() + 1.0D,
				handFed.getY(), handFed.getZ());
		feeder.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems
						.SPRINKLE_SEEDS.get(), 2));
		helper.getLevel().players().add(feeder);
		try {
			require(helper,
					handFed.startGoalNamed(
							"TemptGoal")
							&& handFed.mobInteract(
									feeder,
									InteractionHand
											.MAIN_HAND)
									.consumesAction()
							&& handFed.isInLove()
							&& feeder.getItemInHand(
									InteractionHand
											.MAIN_HAND)
									.getCount() == 1,
					"Chocolate Panda did not follow and consume a tagged Candy Sprout food");
		} finally {
			helper.getLevel().players().remove(feeder);
		}

		ChocolatePandaProbe droppedFoodPanda =
				new ChocolatePandaProbe(helper.getLevel());
		droppedFoodPanda.setPos(anchor.getX() + 3.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		ItemEntity droppedFood = new ItemEntity(
				helper.getLevel(),
				droppedFoodPanda.getX() + 0.5D,
				droppedFoodPanda.getY(),
				droppedFoodPanda.getZ(),
				new ItemStack(CakeWorldItems
						.SPRINKLE_SEEDS.get(), 2));
		helper.getLevel().addFreshEntity(droppedFood);
		require(helper,
				droppedFoodPanda.startGoalNamed(
						"CakeWorldPandaFoodGoal")
						&& droppedFood.isRemoved()
						&& droppedFoodPanda
								.getMainHandItem()
								.is(ChocolatePanda.FOODS)
						&& droppedFoodPanda
								.getMainHandItem()
								.getCount() == 2
						&& droppedFoodPanda.isSitting()
						&& droppedFoodPanda.isEating(),
				"Chocolate Panda did not collect, sit with and eat dropped tagged food");

		ChocolatePandaProbe breedingFirst =
				new ChocolatePandaProbe(helper.getLevel());
		ChocolatePandaProbe breedingSecond =
				new ChocolatePandaProbe(helper.getLevel());
		breedingFirst.setPos(anchor.getX() + 6.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		breedingSecond.setPos(anchor.getX() + 7.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		breedingFirst.setInLove(null);
		breedingSecond.setInLove(null);
		helper.getLevel().addFreshEntity(breedingFirst);
		helper.getLevel().addFreshEntity(breedingSecond);
		BlockPos breedingPlant = anchor.offset(6, 0, 2);
		helper.getLevel().setBlock(breedingPlant,
				CakeWorldBlocks.CANDY_SPROUT.get()
						.defaultBlockState(), 3);
		require(helper,
				breedingFirst.startGoalNamed(
						"CakeWorldPandaBreedGoal")
						&& breedingFirst
								.getUnhappyCounter() == 0,
				"Chocolate Panda did not accept nearby tagged Candy Sprout vegetation for breeding");

		ChocolatePandaProbe attacked =
				new ChocolatePandaProbe(helper.getLevel());
		ChocolatePandaProbe aggressiveAlly =
				new ChocolatePandaProbe(helper.getLevel());
		Pig familyAttacker = EntityType.PIG.create(
				helper.getLevel());
		require(helper, familyAttacker != null,
				"Could not create Chocolate Panda family-response attacker");
		attacked.setMainGene(Panda.Gene.NORMAL);
		attacked.setHiddenGene(Panda.Gene.NORMAL);
		aggressiveAlly.setMainGene(
				Panda.Gene.AGGRESSIVE);
		aggressiveAlly.setHiddenGene(
				Panda.Gene.NORMAL);
		attacked.setPos(anchor.getX() + 2.5D,
				anchor.getY(), anchor.getZ() + 4.5D);
		aggressiveAlly.setPos(anchor.getX() + 3.5D,
				anchor.getY(), anchor.getZ() + 4.5D);
		familyAttacker.setPos(anchor.getX() + 4.5D,
				anchor.getY(), anchor.getZ() + 4.5D);
		helper.getLevel().addFreshEntity(attacked);
		helper.getLevel().addFreshEntity(aggressiveAlly);
		helper.getLevel().addFreshEntity(familyAttacker);
		attacked.setTestTickCount(1);
		attacked.hurt(DamageSource.mobAttack(
				familyAttacker), 1.0F);
		require(helper,
				attacked.startTargetGoalNamed(
						"PandaHurtByTargetGoal")
						&& attacked.getTarget()
								== familyAttacker
						&& aggressiveAlly.getTarget()
								== familyAttacker,
				"Chocolate Panda lost inherited hurt response or aggressive-family alerting");

		Pig biteTarget = EntityType.PIG.create(
				helper.getLevel());
		require(helper, biteTarget != null,
				"Could not create Chocolate Panda bite target");
		ChocolatePandaProbe biter =
				new ChocolatePandaProbe(helper.getLevel());
		float biteHealth = biteTarget.getHealth();
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer()
						.setDifficulty(safeDifficulty,
								true);
				biteTarget.setHealth(biteHealth);
				biteTarget.invulnerableTime = 0;
				biteTarget.removeAllEffects();
				biteTarget.setSecondsOnFire(5);
				biteTarget.fallDistance = 8.0F;
				biteTarget.setDeltaMovement(Vec3.ZERO);
				biter.setTarget(biteTarget);
				require(helper,
						biter.doHurtTarget(
								biteTarget)
								&& close(
										biteTarget
												.getHealth(),
										biteHealth)
								&& biter.getTarget()
										== null
								&& !biteTarget.isOnFire()
								&& biteTarget.fallDistance
										== 0.0F
								&& biteTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& biteTarget.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& biteTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& biteTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4,
						safeDifficulty
								+ " Chocolate Panda bite caused health damage, retained a normal target or lacked sticky rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			biteTarget.setHealth(biteHealth);
			biteTarget.invulnerableTime = 0;
			biteTarget.removeAllEffects();
			require(helper,
					biter.doHurtTarget(biteTarget)
							&& close(
									biteTarget.getHealth(),
									biteHealth - 6.0D),
					"Hard Chocolate Panda lost the exact six-point Panda bite");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		ResourceLocation gummyJungle = new ResourceLocation(
				CakeWorld.MODID, "gummy_jungle");
		require(helper, biomes.get(gummyJungle) == null,
				"Gummy Jungle unexpectedly exists; MOB-035's staged spawn gate must be revisited");
		MobSpawnSettingsBuilder futureSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureJungle =
				new BiomeLoadingEvent(gummyJungle,
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings
										.EMPTY),
						futureSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(
				futureJungle);
		MobSpawnSettings.SpawnerData futureSpawn =
				futureSpawns
						.getSpawner(MobCategory.CREATURE)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.CHOCOLATE_PANDA
										.get())
						.findFirst().orElse(null);
		require(helper,
				futureSpawn != null
						&& futureSpawn.getWeight().asInt()
								== 80
						&& futureSpawn.minCount == 1
						&& futureSpawn.maxCount == 2
						&& futureSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType.PANDA),
				"Future Gummy Jungle hook lost the exact Bamboo Jungle 80/1-2 Panda replacement");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType.PANDA
											|| spawn.type
													== CakeWorldEntities
															.CHOCOLATE_PANDA
															.get()),
					"Current biome leaked Panda/Chocolate Panda spawning before Gummy Jungle exists: "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.CHOCOLATE_PANDA_SPAWN_EGG
						.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.CHOCOLATE_PANDA
												.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.CHOCOLATE_PANDA
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Chocolate Panda lost its egg or exact Panda spawn placement metadata");
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed4035"),
						"CakeWorldChocolatePandaRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.CHOCOLATE_PANDA.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:panda");

		BlockPos spawnPos = anchor.offset(8, 0, 8);
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.TORCH.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				helper.getLevel()
						.getBlockState(spawnPos.below())
						.is(BlockTags
								.ANIMALS_SPAWNABLE_ON)
						&& SpawnPlacements.Type
								.NO_RESTRICTIONS.canSpawnAt(
										helper.getLevel(),
										spawnPos,
										CakeWorldEntities
												.CHOCOLATE_PANDA
												.get()),
				"Chocolate Panda lost its edible animal surface or body-valid unrestricted placement");

		breedingFirst.discard();
		breedingSecond.discard();
		attacked.discard();
		aggressiveAlly.discard();
		familyAttacker.discard();
		droppedFoodPanda.discard();
		helper.getLevel().setBlock(breedingPlant,
				Blocks.AIR.defaultBlockState(), 3);
		helper.runAfterDelay(5, () -> {
			int rawBrightness = helper.getLevel()
					.getMaxLocalRawBrightness(spawnPos);
			require(helper,
					rawBrightness > 8
							&& Animal.checkAnimalSpawnRules(
									CakeWorldEntities
											.CHOCOLATE_PANDA
											.get(),
									helper.getLevel(),
									MobSpawnType.NATURAL,
									spawnPos,
									new Random(1978L)),
					"Chocolate Panda lost the inherited bright animal predicate after block-light propagation: rawBrightness="
							+ rawBrightness);
			helper.getLevel().setBlock(spawnPos,
					Blocks.AIR.defaultBlockState(), 3);
			helper.getLevel().setBlock(spawnPos.below(),
					Blocks.AIR.defaultBlockState(), 3);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void lollipopLorikeetsKeepTamingShouldersAndMimicry(
			GameTestHelper helper) {
		LollipopLorikeetProbe lorikeet =
				new LollipopLorikeetProbe(helper.getLevel());
		require(helper,
				lorikeet instanceof Parrot
						&& lorikeet instanceof FlyingAnimal
						&& lorikeet.getType()
								== CakeWorldEntities
										.LOLLIPOP_LORIKEET
										.get()
						&& close(lorikeet.getMaxHealth(),
								6.0D)
						&& close(lorikeet.getAttributeValue(
								Attributes.FLYING_SPEED),
								0.4D)
						&& close(lorikeet.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.2D)
						&& close(lorikeet.getBbWidth(),
								0.5D)
						&& close(lorikeet.getBbHeight(),
								0.9D)
						&& lorikeet.getType()
								.clientTrackingRange() == 8
						&& lorikeet
								.getMaxSpawnClusterSize() == 4
						&& lorikeet.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/lollipop_lorikeet")),
				"Lollipop Lorikeet lost its genuine Parrot type, flight role, attributes, body, tracking or cluster size");

		for (int variant = 0; variant < 5; variant++) {
			lorikeet.setVariant(variant);
			require(helper,
					lorikeet.getVariant() == variant,
					"Lollipop Lorikeet lost variant "
							+ variant);
		}
		lorikeet.setVariant(4);
		CompoundTag variantData = new CompoundTag();
		lorikeet.addAdditionalSaveData(variantData);
		LollipopLorikeetProbe restored =
				new LollipopLorikeetProbe(helper.getLevel());
		restored.readAdditionalSaveData(variantData);
		require(helper,
				variantData.getInt("Variant") == 4
						&& restored.getVariant() == 4,
				"Lollipop Lorikeet lost variant NBT");

		require(helper,
				!lorikeet.isBaby()
						&& !lorikeet.canMate(restored)
						&& lorikeet.getBreedOffspring(
								helper.getLevel(),
								restored) == null
						&& lorikeet.getNavigation()
								instanceof FlyingPathNavigation
						&& !lorikeet.causeFallDamage(
								20.0F, 1.0F,
								DamageSource.FALL)
						&& lorikeet.countGoalsNamed(
								"SitWhenOrderedToGoal") == 1
						&& lorikeet.countGoalsNamed(
								"FollowOwnerGoal") == 1
						&& lorikeet.countGoalsNamed(
								"ParrotWanderGoal") == 1
						&& lorikeet.countGoalsNamed(
								"LandOnOwnersShoulderGoal") == 1
						&& lorikeet.countGoalsNamed(
								"FollowMobGoal") == 1,
				"Lollipop Lorikeet lost non-breeding, flying, fall-safe, owner, wandering or shoulder goals");

		for (Item tamingFood : List.of(
				Items.WHEAT_SEEDS,
				Items.MELON_SEEDS,
				Items.PUMPKIN_SEEDS,
				Items.BEETROOT_SEEDS,
				CakeWorldItems.SPRINKLE_SEEDS.get())) {
			require(helper,
					new ItemStack(tamingFood).is(
							LollipopLorikeet
									.TAMING_FOODS),
					"Lollipop Lorikeet taming tag lost "
							+ Registry.ITEM.getKey(
									tamingFood));
		}
		require(helper,
				!new ItemStack(Items.COOKIE).is(
						LollipopLorikeet.TAMING_FOODS)
						&& !lorikeet.isFood(
								new ItemStack(
										Items.WHEAT_SEEDS)),
				"Lollipop Lorikeet confused taming food with breeding food or cookie danger");

		BlockPos anchor = helper.absolutePos(
				new BlockPos(4, 2, 4));
		ServerPlayer owner = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed3036"),
						"CakeWorldLorikeetTameTest"));
		owner.setPos(anchor.getX() + 1.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		owner.setOnGround(true);
		helper.getLevel().players().add(owner);
		try {
			long successfulSeed = -1L;
			for (long candidate = 0L;
					candidate < 1000L; candidate++) {
				Random prediction = new Random(candidate);
				prediction.nextFloat();
				prediction.nextFloat();
				if (prediction.nextInt(10) == 0) {
					successfulSeed = candidate;
					break;
				}
			}
			require(helper, successfulSeed >= 0L,
					"Could not prepare deterministic Lorikeet taming seed");
			lorikeet.seedRandom(successfulSeed);
			lorikeet.setPos(anchor.getX() + 0.5D,
					anchor.getY(), anchor.getZ() + 0.5D);
			owner.setItemInHand(
					InteractionHand.MAIN_HAND,
					new ItemStack(CakeWorldItems
							.SPRINKLE_SEEDS.get(), 2));
			InteractionResult tameResult =
					lorikeet.mobInteract(owner,
							InteractionHand.MAIN_HAND);
			require(helper,
					tameResult.consumesAction()
							&& lorikeet.isTame()
							&& lorikeet.isOwnedBy(owner)
							&& owner.getItemInHand(
									InteractionHand
											.MAIN_HAND)
									.getCount() == 1,
					"Lollipop Lorikeet did not consume tagged Sprinkle Seeds and tame at the exact one-in-ten gate");
			requireCriterion(helper, owner,
					"minecraft:husbandry/tame_an_animal",
					"tamed_animal");

			owner.setItemInHand(
					InteractionHand.MAIN_HAND,
					ItemStack.EMPTY);
			lorikeet.setOnGround(true);
			require(helper,
					lorikeet.mobInteract(owner,
							InteractionHand.MAIN_HAND)
									.consumesAction()
							&& lorikeet
									.isOrderedToSit(),
					"Tamed grounded Lollipop Lorikeet lost owner sit toggling");

			LollipopLorikeetProbe shoulderBird =
					new LollipopLorikeetProbe(
							helper.getLevel());
			shoulderBird.setOwnerUUID(owner.getUUID());
			shoulderBird.setTame(true);
			shoulderBird.setVariant(3);
			shoulderBird.setPos(owner.getX(),
					owner.getY(), owner.getZ());
			require(helper,
					shoulderBird.setEntityOnShoulder(
							owner),
					"Lollipop Lorikeet could not enter an empty owner shoulder");
			CompoundTag shoulder =
					owner.getShoulderEntityLeft();
			require(helper,
					LollipopLorikeet
							.isShoulderTag(shoulder)
							&& (CakeWorld.MODID
									+ ":lollipop_lorikeet")
											.equals(
													shoulder
															.getString(
																	"id"))
							&& shoulder.getInt(
									"Variant") == 3,
					"Lollipop Lorikeet shoulder NBT lost its custom identity or variant");
		} finally {
			helper.getLevel().players().remove(owner);
		}

		LollipopLorikeetProbe cookieBird =
				new LollipopLorikeetProbe(helper.getLevel());
		ServerPlayer cookieFeeder = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed4036"),
						"CakeWorldLorikeetCookieTest"));
		cookieFeeder.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.COOKIE, 2));
		require(helper,
				cookieBird.mobInteract(cookieFeeder,
						InteractionHand.MAIN_HAND)
								.consumesAction()
						&& cookieFeeder.getItemInHand(
								InteractionHand
										.MAIN_HAND)
								.getCount() == 1
						&& cookieBird.hasEffect(
								MobEffects.POISON)
						&& cookieBird.getEffect(
								MobEffects.POISON)
								.getDuration() == 900
						&& cookieBird
								.isDeadOrDying(),
				"Lollipop Lorikeet lost the exact poisonous-cookie interaction");

		BlockPos jukebox = anchor.offset(3, 0, 0);
		helper.getLevel().setBlock(jukebox,
				Blocks.JUKEBOX.defaultBlockState(), 3);
		lorikeet.setPos(jukebox.getX() + 0.5D,
				jukebox.getY(), jukebox.getZ() + 0.5D);
		lorikeet.setRecordPlayingNearby(jukebox, true);
		lorikeet.aiStep();
		require(helper, lorikeet.isPartyParrot(),
				"Lollipop Lorikeet did not dance beside a jukebox");
		helper.getLevel().setBlock(jukebox,
				Blocks.AIR.defaultBlockState(), 3);
		lorikeet.aiStep();
		require(helper, !lorikeet.isPartyParrot(),
				"Lollipop Lorikeet kept dancing after its jukebox disappeared");

		PopRockPopper mimicTarget =
				CakeWorldEntities.POP_ROCK_POPPER.get()
						.create(helper.getLevel());
		require(helper, mimicTarget != null,
				"Could not create Lorikeet mimic target");
		mimicTarget.setPos(lorikeet.getX() + 1.0D,
				lorikeet.getY(), lorikeet.getZ());
		helper.getLevel().addFreshEntity(mimicTarget);
		boolean mimicked = false;
		for (int attempt = 0;
				attempt < 16 && !mimicked; attempt++) {
			mimicked = LollipopLorikeet
					.imitateNearbyCakeWorldMobs(
							helper.getLevel(),
							lorikeet);
		}
		require(helper,
				LollipopLorikeet
						.getCakeWorldImitatedSound(
								CakeWorldEntities
										.POP_ROCK_POPPER
										.get())
								== SoundEvents
										.PARROT_IMITATE_CREEPER
						&& mimicked,
				"Lollipop Lorikeet lost CakeWorld replacement-mob mimicry");
		mimicTarget.discard();

		Pig peckTarget = EntityType.PIG.create(
				helper.getLevel());
		require(helper, peckTarget != null,
				"Could not create Lollipop Lorikeet peck target");
		LollipopLorikeetProbe pecker =
				new LollipopLorikeetProbe(helper.getLevel());
		float peckHealth = peckTarget.getHealth();
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer()
						.setDifficulty(safeDifficulty,
								true);
				peckTarget.setHealth(peckHealth);
				peckTarget.invulnerableTime = 0;
				peckTarget.removeAllEffects();
				peckTarget.setSecondsOnFire(5);
				peckTarget.fallDistance = 8.0F;
				pecker.setTarget(peckTarget);
				require(helper,
						pecker.doHurtTarget(
								peckTarget)
								&& close(
										peckTarget
												.getHealth(),
										peckHealth)
								&& pecker.getTarget()
										== null
								&& !peckTarget.isOnFire()
								&& peckTarget.fallDistance
										== 0.0F
								&& peckTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& peckTarget.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& peckTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& peckTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4,
						safeDifficulty
								+ " Lollipop Lorikeet peck caused health damage or lacked sticky rescue effects");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			peckTarget.setHealth(peckHealth);
			peckTarget.invulnerableTime = 0;
			peckTarget.removeAllEffects();
			require(helper,
					pecker.doHurtTarget(peckTarget)
							&& close(
									peckTarget.getHealth(),
									peckHealth - 3.0D),
					"Hard Lollipop Lorikeet lost the exact three-point Parrot attack");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (String futureBiome :
				List.of("lollipop_orchards",
						"gummy_jungle")) {
			ResourceLocation biomeId =
					new ResourceLocation(
							CakeWorld.MODID,
							futureBiome);
			require(helper, biomes.get(biomeId) == null,
					futureBiome
							+ " unexpectedly exists; MOB-036's staged spawn gate must be revisited");
			MobSpawnSettingsBuilder futureSpawns =
					new MobSpawnSettingsBuilder(
							MobSpawnSettings.EMPTY);
			BiomeLoadingEvent futureEvent =
					new BiomeLoadingEvent(
							biomeId, null, null, null,
							new BiomeGenerationSettingsBuilder(
									BiomeGenerationSettings
											.EMPTY),
							futureSpawns);
			CakeWorldCreatureSpawns.onBiomeLoading(
					futureEvent);
			MobSpawnSettings.SpawnerData futureSpawn =
					futureSpawns.getSpawner(
							MobCategory.CREATURE)
							.stream()
							.filter(spawn -> spawn.type
									== CakeWorldEntities
											.LOLLIPOP_LORIKEET
											.get())
							.findFirst().orElse(null);
			require(helper,
					futureSpawn != null
							&& futureSpawn.getWeight()
									.asInt() == 40
							&& futureSpawn.minCount == 1
							&& futureSpawn.maxCount == 2
							&& futureSpawns
									.getSpawner(
											MobCategory
													.CREATURE)
									.stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.PARROT),
					"Future " + futureBiome
							+ " hook lost the exact Jungle 40/1-2 Parrot replacement");
		}

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType.PARROT
											|| spawn.type
													== CakeWorldEntities
															.LOLLIPOP_LORIKEET
															.get()),
					"Current biome leaked Parrot/Lollipop Lorikeet spawning before its biomes exist: "
							+ biomeId);
		}

		BlockPos spawnPos = anchor.offset(8, 0, 8);
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.GUMMY_BLOCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.TORCH.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				CakeWorldItems
						.LOLLIPOP_LORIKEET_SPAWN_EGG
						.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.LOLLIPOP_LORIKEET
												.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.LOLLIPOP_LORIKEET
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING
						&& helper.getLevel()
								.getBlockState(
										spawnPos.below())
								.is(BlockTags
										.PARROTS_SPAWNABLE_ON)
						&& SpawnPlacements.Type.ON_GROUND
								.canSpawnAt(
										helper.getLevel(),
										spawnPos,
										CakeWorldEntities
												.LOLLIPOP_LORIKEET
												.get()),
				"Lollipop Lorikeet lost its egg, exact Parrot placement metadata or edible perch surface");

		helper.runAfterDelay(5, () -> {
			int rawBrightness = helper.getLevel()
					.getMaxLocalRawBrightness(spawnPos);
			require(helper,
					rawBrightness > 8
							&& LollipopLorikeet
									.checkLollipopLorikeetSpawnRules(
											CakeWorldEntities
													.LOLLIPOP_LORIKEET
													.get(),
											helper.getLevel(),
											MobSpawnType.NATURAL,
											spawnPos,
											new Random(1978L)),
					"Lollipop Lorikeet lost the exact bright tagged-surface spawn predicate: rawBrightness="
							+ rawBrightness);
			helper.getLevel().setBlock(spawnPos,
					Blocks.AIR.defaultBlockState(), 3);
			helper.getLevel().setBlock(spawnPos.below(),
					Blocks.AIR.defaultBlockState(), 3);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY)
	public static void waferWraithsKeepInsomniaSwoopsAndProgression(
			GameTestHelper helper) {
		WaferWraithProbe wraith =
				new WaferWraithProbe(helper.getLevel());
		wraith.setPhantomSize(4);
		require(helper,
				wraith instanceof Phantom
						&& wraith.getType()
								== CakeWorldEntities
										.WAFER_WRAITH.get()
						&& wraith.getType().getCategory()
								== MobCategory.MONSTER
						&& close(wraith.getMaxHealth(), 20.0D)
						&& close(wraith.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								10.0D)
						&& close(wraith.getDimensions(
								Pose.STANDING).width,
								1.7D)
						&& close(wraith.getDimensions(
								Pose.STANDING).height,
								0.9444444D)
						&& wraith.getMobType() == MobType.UNDEAD
						&& wraith.despawnsInPeaceful()
						&& wraith.canAttackType(EntityType.PIG)
						&& wraith.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/wafer_wraith"))
						&& wraith.countGoalsNamed(
								"PhantomAttackStrategyGoal")
								== 1
						&& wraith.countGoalsNamed(
								"PhantomSweepAttackGoal")
								== 1
						&& wraith.countGoalsNamed(
								"PhantomCircleAroundAnchorGoal")
								== 1
						&& wraith.countTargetGoalsNamed(
								"PhantomAttackPlayerTargetGoal")
								== 1,
				"Wafer Wraith lost exact Phantom type, size, damage, undead, loot or circling/swoop goals");

		Pig target = EntityType.PIG.create(helper.getLevel());
		require(helper, target != null,
				"Could not create Wafer Wraith swoop target");
		float fullHealth = target.getHealth();
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer()
						.setDifficulty(safeDifficulty,
								true);
				target.setHealth(fullHealth);
				target.invulnerableTime = 0;
				target.removeAllEffects();
				target.setDeltaMovement(Vec3.ZERO);
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				require(helper,
						wraith.doHurtTarget(target)
								&& close(target.getHealth(),
										fullHealth)
								&& !target.isOnFire()
								&& target.fallDistance
										== 0.0F
								&& target.getDeltaMovement().y
										> 0.0D
								&& target.hasEffect(
										MobEffects.BLINDNESS)
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4,
						safeDifficulty
								+ " Wafer Wraith swoop caused health damage or lacked its obscuring rescue envelope");
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			target.setHealth(fullHealth);
			target.invulnerableTime = 0;
			target.removeAllEffects();
			require(helper,
					wraith.doHurtTarget(target)
							&& close(target.getHealth(),
									fullHealth - 10.0D),
					"Hard size-four Wafer Wraith lost its exact ten-point Phantom attack");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		BlockPos runtimeCakeWorld =
				findCakeWorldBiomePosition(helper,
						helper.absolutePos(
								new BlockPos(2, 2, 2)),
						64);
		require(helper, runtimeCakeWorld != null,
				"Could not find runtime CakeWorld biome for insomnia conversion");
		Phantom literal = EntityType.PHANTOM.create(
				helper.getLevel());
		require(helper, literal != null,
				"Could not create literal insomnia Phantom fixture");
		CompoundTag literalState =
				literal.saveWithoutId(new CompoundTag());
		literalState.putInt("AX", 1978);
		literalState.putInt("AY", 111);
		literalState.putInt("AZ", -37);
		literalState.putInt("Size", 3);
		literal.load(literalState);
		literal.setPos(runtimeCakeWorld.getX(),
				runtimeCakeWorld.getY(),
				runtimeCakeWorld.getZ());
		literal.setHealth(13.0F);
		literal.setCustomName(new TextComponent(
				"Late-Night Wafer"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		helper.getLevel().addFreshEntity(literal);
		WaferWraith converted =
				CakeWorldPhantomReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		CompoundTag convertedState = converted == null
				? new CompoundTag()
				: converted.saveWithoutId(new CompoundTag());
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.WAFER_WRAITH.get()
						&& converted.getPhantomSize() == 3
						&& close(converted.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								9.0D)
						&& close(converted.getHealth(), 13.0D)
						&& converted.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.hasCustomName()
						&& "Late-Night Wafer".equals(
								converted.getName()
										.getString())
						&& convertedState.getInt("AX") == 1978
						&& convertedState.getInt("AY") == 111
						&& convertedState.getInt("AZ") == -37,
				"Fresh CakeWorld insomnia conversion lost Phantom size, anchor, health, name, persistence, AI or invulnerability");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType.PHANTOM
											|| spawn.type
													== CakeWorldEntities
															.WAFER_WRAITH
															.get()),
					"Wafer Wraith leaked into ordinary biome spawning instead of the insomnia path: "
							+ biomeId);
		}
		require(helper,
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.WAFER_WRAITH.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.WAFER_WRAITH.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems.WAFER_WRAITH_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.WAFER_WRAITH
												.get())
								== SoundEvents
										.PARROT_IMITATE_PHANTOM,
				"Wafer Wraith lost exact Phantom placement metadata, testing egg or Lorikeet mimic role");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2037"),
						"CakeWorldWaferWraithRoleTest"));
		VanillaRoleAdvancements.creditKilledPhantomRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:phantom");
		Advancement twoBirds = advancementPlayer.getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/two_birds_one_arrow"));
		AbstractArrow piercingArrow =
				EntityType.ARROW.create(helper.getLevel());
		require(helper,
				twoBirds != null && piercingArrow != null,
				"Could not create Two Birds, One Arrow fixtures");
		piercingArrow.setOwner(advancementPlayer);
		piercingArrow.setShotFromCrossbow(true);
		piercingArrow.setPierceLevel((byte)1);
		VanillaRoleAdvancements
				.recordPhantomRoleCrossbowKill(
						advancementPlayer,
						piercingArrow,
						CakeWorldEntities.WAFER_WRAITH.get());
		require(helper,
				!advancementPlayer.getAdvancements()
						.getOrStartProgress(twoBirds)
						.getCriterion("two_birds").isDone(),
				"One Wafer Wraith incorrectly completed Two Birds, One Arrow");
		VanillaRoleAdvancements
				.recordPhantomRoleCrossbowKill(
						advancementPlayer,
						piercingArrow,
						CakeWorldEntities.WAFER_WRAITH.get());
		require(helper,
				advancementPlayer.getAdvancements()
						.getOrStartProgress(twoBirds)
						.getCriterion("two_birds").isDone(),
				"Two Wafer Wraiths killed by one piercing crossbow arrow did not preserve Two Birds, One Arrow");
		converted.discard();
		helper.succeed();
	}

	@GameTest(template = EMPTY)
	public static void trufflePigsKeepRidingLightningAndIngredientForaging(
			GameTestHelper helper) {
		TrufflePigProbe pig =
				new TrufflePigProbe(helper.getLevel());
		require(helper,
				pig instanceof Pig
						&& pig.getType()
								== CakeWorldEntities
										.TRUFFLE_PIG.get()
						&& pig.getType().getCategory()
								== MobCategory.CREATURE
						&& close(pig.getMaxHealth(), 10.0D)
						&& close(pig.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.25D)
						&& close(pig.getDimensions(
								Pose.STANDING).width,
								0.9D)
						&& close(pig.getDimensions(
								Pose.STANDING).height,
								0.9D)
						&& pig.getMaxSpawnClusterSize() == 4
						&& pig.getAmbientSoundInterval()
								== 120
						&& pig.countGoalsNamed(
								"FloatGoal") == 1
						&& pig.countGoalsNamed(
								"PanicGoal") == 1
						&& pig.countGoalsNamed(
								"BreedGoal") == 1
						&& pig.countGoalsNamed(
								"TemptGoal") == 2
						&& pig.countGoalsNamed(
								"FollowParentGoal") == 1
						&& pig.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/truffle_pig")),
				"Truffle Pig lost exact Pig type, attributes, body, goals or loot table");
		for (Item food : List.of(
				Items.CARROT, Items.POTATO,
				Items.BEETROOT)) {
			require(helper,
					pig.isFood(new ItemStack(food)),
					"Truffle Pig rejected vanilla Pig food "
							+ food);
		}
		require(helper,
				!pig.isFood(new ItemStack(
						CakeWorldItems.SIMPLE_BISCUIT.get()))
						&& pig.getBreedOffspring(
								helper.getLevel(), pig)
								instanceof TrufflePig,
				"Truffle Pig changed its exact breeding diet or leaked a literal Pig offspring");

		BlockPos foragePos = helper.absolutePos(
				new BlockPos(2, 3, 2));
		helper.getLevel().setBlock(
				foragePos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		pig.setPos(foragePos.getX() + 0.5D,
				foragePos.getY(),
				foragePos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(pig);
		Player forager = helper.makeMockPlayer();
		forager.getAbilities().instabuild = false;
		forager.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(
						CakeWorldItems.SIMPLE_BISCUIT.get(),
						2));
		BlockState forageSurface = helper.getLevel()
				.getBlockState(foragePos.below());
		InteractionResult forageResult = pig.mobInteract(
				forager, InteractionHand.MAIN_HAND);
		List<ItemEntity> firstDrops = helper.getLevel()
				.getEntitiesOfClass(ItemEntity.class,
						pig.getBoundingBox().inflate(2.0D),
						drop -> drop.getItem().is(
								CakeWorldItems.COCOA_TRUFFLE
										.get()));
		require(helper,
				forageResult.consumesAction()
						&& forager.getMainHandItem()
								.getCount() == 1
						&& firstDrops.size() == 1
						&& firstDrops.get(0).getItem()
								.getCount() == 1
						&& pig.getForageCooldownTicks()
								== TrufflePig
										.FORAGE_COOLDOWN_TICKS
						&& helper.getLevel().getBlockState(
								foragePos.below())
								== forageSurface,
				"Truffle Pig forage did not exchange one biscuit for one truffle without changing terrain");
		InteractionResult cooldownResult = pig.mobInteract(
				forager, InteractionHand.MAIN_HAND);
		require(helper,
				cooldownResult.consumesAction()
						&& forager.getMainHandItem()
								.getCount() == 1
						&& helper.getLevel()
								.getEntitiesOfClass(
										ItemEntity.class,
										pig.getBoundingBox()
												.inflate(2.0D),
										drop -> drop.getItem()
												.is(CakeWorldItems
														.COCOA_TRUFFLE
														.get()))
								.size() == 1,
				"Truffle Pig cooldown consumed another biscuit or duplicated a truffle");
		CompoundTag forageState = pig.saveWithoutId(
				new CompoundTag());
		TrufflePig restored =
				CakeWorldEntities.TRUFFLE_PIG.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Truffle Pig cooldown reload fixture");
		restored.load(forageState);
		require(helper,
				restored.getForageCooldownTicks() > 1100,
				"Truffle Pig lost its foraging cooldown across save/load");
		TrufflePig baby =
				CakeWorldEntities.TRUFFLE_PIG.get()
						.create(helper.getLevel());
		require(helper, baby != null,
				"Could not create baby Truffle Pig forage fixture");
		baby.setAge(-24000);
		baby.setPos(pig.getX(), pig.getY(), pig.getZ());
		ItemStack babyTreat = new ItemStack(
				CakeWorldItems.SIMPLE_BISCUIT.get());
		forager.setItemInHand(
				InteractionHand.MAIN_HAND, babyTreat);
		require(helper,
				baby.tryForage(forager,
						InteractionHand.MAIN_HAND)
								== InteractionResult.PASS
						&& babyTreat.getCount() == 1,
				"Baby Truffle Pig foraged or consumed a treat");

		TrufflePig ridden =
				CakeWorldEntities.TRUFFLE_PIG.get()
						.create(helper.getLevel());
		require(helper, ridden != null
						&& ridden.isSaddleable(),
				"Could not create adult saddleable Truffle Pig");
		ridden.setPos(foragePos.getX() + 3.0D,
				foragePos.getY(), foragePos.getZ());
		ridden.equipSaddle(null);
		helper.getLevel().addFreshEntity(ridden);
		Player rider = helper.makeMockPlayer();
		rider.getAbilities().instabuild = false;
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.CARROT_ON_A_STICK));
		require(helper,
				rider.startRiding(ridden, true)
						&& ridden.isSaddled()
						&& ridden.getControllingPassenger()
								== rider
						&& ridden.canBeControlledByRider()
						&& close(ridden.getSteeringSpeed(),
								0.05625D),
				"Truffle Pig lost saddle, rider, Carrot-on-a-Stick control or exact steering speed");
		PlayerInteractEvent.RightClickItem boostEvent =
				new PlayerInteractEvent.RightClickItem(
						rider,
						InteractionHand.MAIN_HAND);
		TrufflePigCarrotOnAStickBridge
				.onRightClickItem(boostEvent);
		require(helper,
				boostEvent.isCanceled()
						&& boostEvent.getCancellationResult()
								== InteractionResult.SUCCESS
						&& rider.getMainHandItem()
								.getDamageValue() == 7
						&& !ridden.boost(),
				"Truffle Pig did not bridge the literal Pig boost gate or exact seven durability cost");
		CompoundTag saddleState =
				ridden.saveWithoutId(new CompoundTag());
		TrufflePig saddleReload =
				CakeWorldEntities.TRUFFLE_PIG.get()
						.create(helper.getLevel());
		require(helper, saddleReload != null,
				"Could not create Truffle Pig saddle reload fixture");
		saddleReload.load(saddleState);
		require(helper, saddleReload.isSaddled(),
				"Truffle Pig lost saddle NBT");
		rider.stopRiding();

		TrufflePig breakingRide =
				CakeWorldEntities.TRUFFLE_PIG.get()
						.create(helper.getLevel());
		require(helper, breakingRide != null,
				"Could not create breaking boost fixture");
		breakingRide.setPos(ridden.getX() + 2.0D,
				ridden.getY(), ridden.getZ());
		breakingRide.equipSaddle(null);
		helper.getLevel().addFreshEntity(breakingRide);
		ItemStack nearlyBroken =
				new ItemStack(Items.CARROT_ON_A_STICK);
		nearlyBroken.setDamageValue(
				nearlyBroken.getMaxDamage() - 6);
		CompoundTag stickTag = nearlyBroken
				.getOrCreateTag();
		stickTag.putBoolean("CakeWorldTest", true);
		rider.setItemInHand(InteractionHand.MAIN_HAND,
				nearlyBroken);
		require(helper,
				rider.startRiding(breakingRide, true),
				"Rider could not mount second Truffle Pig");
		PlayerInteractEvent.RightClickItem breakEvent =
				new PlayerInteractEvent.RightClickItem(
						rider,
						InteractionHand.MAIN_HAND);
		TrufflePigCarrotOnAStickBridge
				.onRightClickItem(breakEvent);
		require(helper,
				breakEvent.isCanceled()
						&& rider.getMainHandItem()
								.is(Items.FISHING_ROD)
						&& rider.getMainHandItem().hasTag()
						&& rider.getMainHandItem().getTag()
								.getBoolean(
										"CakeWorldTest"),
				"Breaking Truffle Pig boost did not preserve vanilla Fishing Rod replacement and NBT");
		rider.stopRiding();

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		ZombifiedPiglin lightningResult = null;
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			TrufflePig struck =
					CakeWorldEntities.TRUFFLE_PIG.get()
							.create(helper.getLevel());
			LightningBolt bolt =
					EntityType.LIGHTNING_BOLT.create(
							helper.getLevel());
			require(helper, struck != null && bolt != null,
					"Could not create Truffle Pig lightning fixtures");
			struck.setPos(foragePos.getX() + 6.0D,
					foragePos.getY(), foragePos.getZ());
			struck.setAge(-24000);
			struck.setNoAi(true);
			struck.setCustomName(new TextComponent(
					"Thunder Truffle"));
			helper.getLevel().addFreshEntity(struck);
			struck.thunderHit(helper.getLevel(), bolt);
			lightningResult = helper.getLevel()
					.getEntitiesOfClass(
							ZombifiedPiglin.class,
							new AABB(struck.getX() - 2.0D,
									struck.getY() - 2.0D,
									struck.getZ() - 2.0D,
									struck.getX() + 2.0D,
									struck.getY() + 2.0D,
									struck.getZ() + 2.0D))
					.stream().findFirst().orElse(null);
			require(helper,
					struck.isRemoved()
							&& lightningResult != null
							&& lightningResult.isBaby()
							&& lightningResult.isNoAi()
							&& lightningResult
									.isPersistenceRequired()
							&& lightningResult
									.getMainHandItem()
									.is(Items.GOLDEN_SWORD)
							&& "Thunder Truffle".equals(
									lightningResult.getName()
											.getString()),
					"Truffle Pig lost the staged vanilla lightning-conversion state for MOB-073");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			TrufflePig peacefulStruck =
					CakeWorldEntities.TRUFFLE_PIG.get()
							.create(helper.getLevel());
			LightningBolt peacefulBolt =
					EntityType.LIGHTNING_BOLT.create(
							helper.getLevel());
			require(helper,
					peacefulStruck != null
							&& peacefulBolt != null,
					"Could not create Peaceful lightning fixtures");
			peacefulStruck.setPos(foragePos.getX() + 9.0D,
					foragePos.getY(), foragePos.getZ());
			peacefulStruck.thunderHit(helper.getLevel(),
					peacefulBolt);
			require(helper,
					!peacefulStruck.isRemoved(),
					"Peaceful Truffle Pig incorrectly transformed into the future Stale Fudge Folk role");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
			if (lightningResult != null) {
				lightningResult.discard();
			}
		}

		Biome candyPlains = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Truffle Pig spawning");
		MobSpawnSettings.SpawnerData pigSpawn =
				candyPlains.getMobSettings()
						.getMobs(MobCategory.CREATURE)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.TRUFFLE_PIG.get())
						.findFirst().orElse(null);
		require(helper,
				pigSpawn != null
						&& pigSpawn.getWeight().asInt() == 10
						&& pigSpawn.minCount == 4
						&& pigSpawn.maxCount == 4
						&& candyPlains.getMobSettings()
								.getMobs(
										MobCategory.CREATURE)
								.unwrap().stream()
								.noneMatch(spawn ->
										spawn.type
												== EntityType.PIG)
						&& CakeWorldItems
								.TRUFFLE_PIG_SPAWN_EGG
								.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.TRUFFLE_PIG
												.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.TRUFFLE_PIG
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Truffle Pig lost exact Plains 10/4-4 replacement, egg or Pig placement metadata");

		BlockPos spawnPos = foragePos.offset(12, 0, 0);
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.LIGHT.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.runAfterDelay(5, () -> {
			require(helper,
					helper.getLevel().getBlockState(
							spawnPos.below())
							.is(BlockTags
									.ANIMALS_SPAWNABLE_ON)
							&& Animal
									.checkAnimalSpawnRules(
											CakeWorldEntities
													.TRUFFLE_PIG
													.get(),
											helper.getLevel(),
											MobSpawnType.NATURAL,
											spawnPos,
											new Random(1978L))
							&& SpawnPlacements.Type.ON_GROUND
									.canSpawnAt(
											helper.getLevel(),
											spawnPos,
											CakeWorldEntities
													.TRUFFLE_PIG
													.get()),
					"Truffle Pig lost exact bright tagged-surface spawn predicate");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void fudgeBrutesGuardFoundriesWithoutBreakingFamilySafety(
			GameTestHelper helper) {
		FudgeBruteProbe brute =
				new FudgeBruteProbe(helper.getLevel());
		require(helper,
				brute instanceof PiglinBrute
						&& brute instanceof AbstractPiglin
						&& brute.getType()
								== CakeWorldEntities
										.FUDGE_BRUTE.get()
						&& brute.getType().getCategory()
								== MobCategory.MONSTER
						&& close(brute.getMaxHealth(), 50.0D)
						&& close(brute.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.35D)
						&& close(brute.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								7.0D)
						&& close(brute.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(brute.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& brute.getExperienceValue() == 20
						&& !brute.canHuntRole()
						&& brute.despawnsInPeaceful()
						&& brute.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/fudge_brute"))
						&& brute.ambientSound()
								== SoundEvents
										.PIGLIN_BRUTE_AMBIENT
						&& brute.hurtSound()
								== SoundEvents
										.PIGLIN_BRUTE_HURT
						&& brute.deathSound()
								== SoundEvents
										.PIGLIN_BRUTE_DEATH,
				"Fudge Brute lost exact Piglin Brute type, body, attributes, XP, hunt, Peaceful, loot or sound roles");

		BlockPos guardPos = helper.absolutePos(
				new BlockPos(2, 3, 2));
		brute.setPos(guardPos.getX() + 0.5D,
				guardPos.getY(), guardPos.getZ() + 0.5D);
		brute.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						guardPos),
				MobSpawnType.STRUCTURE, null, null);
		helper.getLevel().addFreshEntity(brute);
		GlobalPos home = brute.getBrain().getMemory(
				MemoryModuleType.HOME).orElse(null);
		require(helper,
				home != null
						&& home.dimension().equals(
								helper.getLevel()
										.dimension())
						&& home.pos().equals(
								brute.blockPosition())
						&& brute.getMainHandItem()
								.is(Items.GOLDEN_AXE)
						&& brute.wantsToPickUp(
								new ItemStack(
										Items.GOLDEN_AXE))
						&& !brute.wantsToPickUp(
								new ItemStack(
										Items.GOLD_INGOT)),
				"Fudge Brute lost its Foundry home, guaranteed Golden Axe or exact pickup role");

		ServerPlayer goldPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2040"),
						"CakeWorldFudgeBruteTest"));
		goldPlayer.setPos(brute.getX() + 2.0D,
				brute.getY(), brute.getZ());
		goldPlayer.setItemSlot(EquipmentSlot.HEAD,
				new ItemStack(Items.GOLDEN_HELMET));
		goldPlayer.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.GOLD_INGOT));
		InteractionResult barter = brute.runMobInteract(
				goldPlayer, InteractionHand.MAIN_HAND);
		require(helper,
				!barter.consumesAction()
						&& goldPlayer.getMainHandItem()
								.is(Items.GOLD_INGOT)
						&& !brute.getBrain()
								.hasMemoryValue(
										MemoryModuleType
												.ADMIRING_ITEM),
				"Fudge Brute incorrectly inherited the ordinary Piglin barter role");
		Difficulty targetingDifficulty =
				helper.getLevel().getDifficulty();
		FudgeBruteProbe nemesisBrute =
				new FudgeBruteProbe(helper.getLevel());
		WitherSkeleton nemesis =
				EntityType.WITHER_SKELETON.create(
						helper.getLevel());
		require(helper, nemesis != null,
				"Could not create Fudge Brute nemesis fixture");
		try {
			// Player targeting is disabled globally in Peaceful. Pin this
			// synchronous assertion so concurrently scheduled mob tests
			// cannot make gold armour look like a Brute exemption.
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			for (int attempt = 0; attempt < 25
					&& !brute.getBrain().hasMemoryValue(
							MemoryModuleType.ATTACK_TARGET);
					attempt++) {
				// The vanilla PlayerSensor may run during this manual
				// brain tick. Re-seed across one complete scan cadence
				// so the test is independent of its random phase.
				brute.getBrain().setMemory(
						MemoryModuleType
								.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
						goldPlayer);
				brute.runServerAiStep();
			}
			require(helper,
					brute.getBrain().getMemory(
							MemoryModuleType.ATTACK_TARGET)
							.filter(target ->
									target == goldPlayer)
							.isPresent()
							&& brute.isAggressive(),
					"Fudge Brute stopped targeting a gold-armoured player");

			nemesisBrute.setPos(brute.getX(),
					brute.getY(), brute.getZ() + 3.0D);
			nemesis.setPos(nemesisBrute.getX() + 2.0D,
					nemesisBrute.getY(),
					nemesisBrute.getZ());
			for (int attempt = 0; attempt < 25
					&& !nemesisBrute.getBrain()
							.hasMemoryValue(
									MemoryModuleType
											.ATTACK_TARGET);
					attempt++) {
				nemesisBrute.getBrain().setMemory(
						MemoryModuleType
								.NEAREST_VISIBLE_NEMESIS,
						nemesis);
				nemesisBrute.runServerAiStep();
			}
			require(helper,
					nemesisBrute.getBrain().getMemory(
							MemoryModuleType.ATTACK_TARGET)
							.filter(target ->
									target == nemesis)
							.isPresent(),
					"Fudge Brute lost inherited nemesis targeting");
			VanillaRoleAdvancements
					.creditKilledPiglinBruteRole(
							goldPlayer);
			requireCriterion(helper, goldPlayer,
					"minecraft:adventure/kill_all_mobs",
					"minecraft:piglin_brute");
		} finally {
			nemesisBrute.discard();
			nemesis.discard();
			helper.getLevel().getServer().setDifficulty(
					targetingDifficulty, true);
		}

		FudgeBruteProbe familyBrute =
				new FudgeBruteProbe(helper.getLevel());
		FudgeFolkProbe familyFolk =
				new FudgeFolkProbe(helper.getLevel());
		BlockPos familyPos = helper.absolutePos(
				new BlockPos(4, 7, 4));
		helper.getLevel().setBlock(
				familyPos, Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				familyPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				familyPos.east(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				familyPos.east().above(),
				Blocks.AIR.defaultBlockState(), 3);
		familyBrute.setPos(familyPos.getX() + 0.5D,
				familyPos.getY(),
				familyPos.getZ() + 0.5D);
		familyFolk.setPos(familyBrute.getX() + 1.0D,
				familyBrute.getY(), familyBrute.getZ());
		helper.getLevel().addFreshEntity(familyBrute);
		helper.getLevel().addFreshEntity(familyFolk);
		familyBrute.getBrain().eraseMemory(
				MemoryModuleType.ATTACK_TARGET);
		familyBrute.getBrain().eraseMemory(
				MemoryModuleType.WALK_TARGET);
		familyBrute.getBrain().eraseMemory(
				MemoryModuleType.INTERACTION_TARGET);
		familyBrute.getBrain()
				.setActiveActivityIfPossible(Activity.IDLE);
		familyFolk.getBrain().eraseMemory(
				MemoryModuleType.ATTACK_TARGET);
		familyFolk.getBrain().eraseMemory(
				MemoryModuleType.WALK_TARGET);
		familyFolk.getBrain().eraseMemory(
				MemoryModuleType.INTERACTION_TARGET);
		familyFolk.getBrain()
				.setActiveActivityIfPossible(Activity.IDLE);
		familyBrute.getBrain().setMemory(
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
				new NearestVisibleLivingEntities(
						familyBrute,
						List.of(familyFolk)));
		familyFolk.getBrain().setMemory(
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
				new NearestVisibleLivingEntities(
						familyFolk,
						List.of(familyBrute)));
		familyBrute.tickCount = 20;
		familyFolk.tickCount = 20;
		familyBrute.runFamilyInteractionRepair();
		familyFolk.runFamilyInteractionRepair();
		require(helper,
				familyBrute.getBrain().getMemory(
						MemoryModuleType.INTERACTION_TARGET)
						.filter(target ->
								target == familyFolk)
						.isPresent()
						&& familyFolk.getBrain()
								.getMemory(
										MemoryModuleType
												.INTERACTION_TARGET)
								.filter(target ->
										target
												== familyBrute)
								.isPresent(),
				"Custom Piglin family did not repair literal-type idle interaction");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		ZombifiedPiglin conversion = null;
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			float beforeFamilyHit = familyBrute.getHealth();
			require(helper,
					familyBrute.hurt(
							DamageSource.mobAttack(
									familyFolk),
							1.0F)
							&& close(
									familyBrute
											.getHealth(),
									beforeFamilyHit
											- 1.0D)
							&& !familyBrute.getBrain()
									.hasMemoryValue(
											MemoryModuleType
													.ANGRY_AT)
							&& !familyBrute.getBrain()
									.hasMemoryValue(
											MemoryModuleType
													.ATTACK_TARGET),
					"Fudge Brute retaliated against its AbstractPiglin family");

			for (Difficulty safeDifficulty :
					List.of(Difficulty.EASY,
							Difficulty.NORMAL)) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				Pig safeTarget = EntityType.PIG.create(
						helper.getLevel());
				require(helper, safeTarget != null,
						"Could not create safe Fudge Brute target");
				safeTarget.setPos(brute.getX() + 8.0D,
						brute.getY(),
						brute.getZ()
								+ safeDifficulty.getId()
										* 2.0D);
				helper.getLevel().addFreshEntity(
						safeTarget);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 8.0F;
				brute.doHurtTarget(safeTarget);
				require(helper,
						close(safeTarget.getHealth(),
								safeTarget
										.getMaxHealth())
								&& !safeTarget.isOnFire()
								&& close(safeTarget
										.fallDistance,
										0.0D)
								&& safeTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Fudge Brute axe caused damage or lacked rescue");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget = EntityType.PIG.create(
					helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Fudge Brute target");
			hardTarget.setPos(brute.getX() + 8.0D,
					brute.getY(), brute.getZ() + 6.0D);
			helper.getLevel().addFreshEntity(hardTarget);
			brute.doHurtTarget(hardTarget);
			require(helper,
					hardTarget.getHealth()
							< hardTarget.getMaxHealth(),
					"Hard Fudge Brute axe did not cause real damage");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			FudgeBruteProbe peaceful =
					new FudgeBruteProbe(helper.getLevel());
			peaceful.setPos(brute.getX() + 10.0D,
					brute.getY(), brute.getZ());
			helper.getLevel().addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Fudge Brute did not retain vanilla Monster removal");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			FudgeBruteProbe converting =
					new FudgeBruteProbe(helper.getLevel());
			converting.setPos(brute.getX() + 12.0D,
					brute.getY(), brute.getZ());
			helper.getLevel().getEntitiesOfClass(
					ZombifiedPiglin.class,
					converting.getBoundingBox()
							.inflate(3.0D))
					.forEach(ZombifiedPiglin::discard);
			converting.setCustomName(new TextComponent(
					"Staged Burnt Fudge Brute"));
			converting.setPersistenceRequired();
			converting.setItemSlot(EquipmentSlot.MAINHAND,
					new ItemStack(Items.GOLDEN_AXE));
			CompoundTag convertingState =
					converting.saveWithoutId(
							new CompoundTag());
			convertingState.putInt(
					"TimeInOverworld", 301);
			converting.load(convertingState);
			helper.getLevel().addFreshEntity(converting);
			converting.runServerAiStep();
			conversion = helper.getLevel()
					.getEntitiesOfClass(
							ZombifiedPiglin.class,
							converting.getBoundingBox()
									.inflate(3.0D))
					.stream().findFirst().orElse(null);
			require(helper,
					converting.isRemoved()
							&& conversion != null
							&& conversion
									.isPersistenceRequired()
							&& conversion.getMainHandItem()
									.is(Items.GOLDEN_AXE)
							&& "Staged Burnt Fudge Brute"
									.equals(conversion.getName()
											.getString())
							&& conversion.hasEffect(
									MobEffects.CONFUSION),
					"Fudge Brute lost staged vanilla zombification for MOB-073");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
			if (conversion != null) {
				conversion.discard();
			}
		}

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						helper.absolutePos(
								new BlockPos(8, 3, 8)),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Piglin Brute conversion");
		PiglinBrute literal =
				EntityType.PIGLIN_BRUTE.create(
						helper.getLevel());
		require(helper, literal != null,
				"Could not create literal Bastion Piglin Brute fixture");
		literal.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literal.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.STRUCTURE, null, null);
		literal.setNoAi(true);
		literal.setHealth(37.0F);
		literal.setCustomName(new TextComponent(
				"Foundry Captain"));
		literal.setPersistenceRequired();
		FudgeBrute structureReplacement =
				CakeWorldPiglinBruteReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				literal.isRemoved()
						&& structureReplacement != null
						&& structureReplacement.isNoAi()
						&& structureReplacement
								.isPersistenceRequired()
						&& close(structureReplacement
								.getHealth(), 37.0D)
						&& structureReplacement
								.getMainHandItem()
								.is(Items.GOLDEN_AXE)
						&& structureReplacement.getBrain()
								.hasMemoryValue(
										MemoryModuleType.HOME)
						&& "Foundry Captain".equals(
								structureReplacement
										.getName()
										.getString()),
				"Fresh literal Piglin Brute conversion lost Foundry state");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = helper.getLevel().registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.PIGLIN_BRUTE
													|| spawn.type
															== CakeWorldEntities
																	.FUDGE_BRUTE
																	.get()),
					"Fudge Brute leaked into open spawning in "
							+ biomeId);
		}

		require(helper,
				CakeWorldItems.FUDGE_BRUTE_SPAWN_EGG
						.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.FUDGE_BRUTE
												.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.FUDGE_BRUTE
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.PIGLIN_BRUTE)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.FUDGE_BRUTE
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.PIGLIN_BRUTE)
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.FUDGE_BRUTE
												.get())
								== SoundEvents
										.PARROT_IMITATE_PIGLIN_BRUTE,
				"Fudge Brute lost its egg, structure-only placement or mimic role");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void biscuitBanditsKeepPatrolOutpostAndRaidRoles(
			GameTestHelper helper) {
		BiscuitBanditProbe bandit =
				new BiscuitBanditProbe(helper.getLevel());
		require(helper,
				bandit instanceof Pillager
						&& bandit instanceof Raider
						&& bandit instanceof PatrollingMonster
						&& bandit instanceof CrossbowAttackMob
						&& bandit.getType()
								== CakeWorldEntities
										.BISCUIT_BANDIT.get()
						&& bandit.getType().getCategory()
								== MobCategory.MONSTER
						&& close(bandit.getMaxHealth(), 24.0D)
						&& close(bandit.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.35D)
						&& close(bandit.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								32.0D)
						&& close(bandit.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								5.0D)
						&& close(bandit.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(bandit.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& bandit.getMaxSpawnClusterSize() == 1
						&& bandit.getExperienceValue() == 5
						&& bandit.getInventory()
								.getContainerSize() == 5
						&& bandit.canFireProjectileWeapon(
								(net.minecraft.world.item
										.ProjectileWeaponItem)
										Items.CROSSBOW)
						&& bandit.canBeLeader()
						&& bandit.canJoinPatrol()
						&& bandit.despawnsInPeaceful()
						&& close(bandit.getWalkTargetValue(
								helper.absolutePos(
										new BlockPos(
												2, 3, 2)),
								helper.getLevel()),
								0.0D)
						&& bandit.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/biscuit_bandit"))
						&& bandit.ambientSound()
								== SoundEvents
										.PILLAGER_AMBIENT
						&& bandit.hurtSound()
								== SoundEvents
										.PILLAGER_HURT
						&& bandit.deathSound()
								== SoundEvents
										.PILLAGER_DEATH
						&& bandit.getCelebrateSound()
								== SoundEvents
										.PILLAGER_CELEBRATE
						&& bandit.countGoalsNamed(
								"RangedCrossbowAttackGoal")
								== 1
						&& bandit.countGoalsNamed(
								"LongDistancePatrolGoal")
								== 1
						&& bandit.countGoalsNamed(
								"HoldGroundAttackGoal")
								== 1
						&& bandit.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 3,
				"Biscuit Bandit lost exact Pillager body, inventory, crossbow, patrol, goals, XP, loot or sound roles");

		BlockPos patrolPos = helper.absolutePos(
				new BlockPos(2, 3, 2));
		BlockPos patrolTarget = patrolPos.offset(
				64, 0, 64);
		bandit.setPos(patrolPos.getX() + 0.5D,
				patrolPos.getY(),
				patrolPos.getZ() + 0.5D);
		bandit.setPatrolLeader(true);
		bandit.setPatrolTarget(patrolTarget);
		bandit.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						patrolPos),
				MobSpawnType.PATROL, null, null);
		helper.getLevel().addFreshEntity(bandit);
		require(helper,
				bandit.isPatrolLeader()
						&& bandit.isPatrollingRole()
						&& bandit.hasPatrolTarget()
						&& patrolTarget.equals(
								bandit.getPatrolTarget())
						&& ItemStack.isSameItemSameTags(
								bandit.getItemBySlot(
										EquipmentSlot.HEAD),
								Raid.getLeaderBannerInstance())
						&& bandit.getMainHandItem()
								.is(Items.CROSSBOW)
						&& bandit.canJoinRaid(),
				"Biscuit Bandit lost patrol target, captain banner, crossbow or raid eligibility");
		bandit.setChargingCrossbow(true);
		require(helper,
				bandit.isChargingCrossbow()
						&& bandit.getArmPose()
								== AbstractIllager
										.IllagerArmPose
										.CROSSBOW_CHARGE,
				"Biscuit Bandit lost its crossbow charging state");
		bandit.setChargingCrossbow(false);
		require(helper,
				bandit.getArmPose()
						== AbstractIllager.IllagerArmPose
								.CROSSBOW_HOLD,
				"Biscuit Bandit lost its charged crossbow hold pose");

		Pig rangedCueTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, rangedCueTarget != null,
				"Could not create Biscuit Bandit ranged cue target");
		rangedCueTarget.setPos(bandit.getX() + 8.0D,
				bandit.getY(), bandit.getZ());
		rangedCueTarget.setNoAi(true);
		helper.getLevel().addFreshEntity(rangedCueTarget);
		ItemStack chargedCrossbow =
				new ItemStack(Items.CROSSBOW);
		ListTag chargedProjectiles = new ListTag();
		chargedProjectiles.add(
				new ItemStack(Items.ARROW).save(
						new CompoundTag()));
		chargedCrossbow.getOrCreateTag().put(
				"ChargedProjectiles", chargedProjectiles);
		CrossbowItem.setCharged(chargedCrossbow, true);
		bandit.setItemSlot(EquipmentSlot.MAINHAND,
				chargedCrossbow);
		bandit.setTarget(rangedCueTarget);
		bandit.performRangedAttack(
				rangedCueTarget, 1.0F);
		AbstractArrow visibleBolt = helper.getLevel()
				.getEntitiesOfClass(
						AbstractArrow.class,
						bandit.getBoundingBox()
								.inflate(12.0D))
				.stream()
				.filter(arrow -> arrow.getOwner()
						== bandit)
				.findFirst().orElse(null);
		require(helper,
				visibleBolt != null
						&& visibleBolt.shotFromCrossbow(),
				"Biscuit Bandit did not fire a visible crossbow bolt");
		bandit.setTarget(null);
		visibleBolt.discard();
		rangedCueTarget.discard();

		Villager villager =
				EntityType.VILLAGER.create(helper.getLevel());
		require(helper, villager != null,
				"Could not create Biscuit Bandit Villager-awareness fixture");
		villager.setPos(bandit.getX() + 15.0D,
				bandit.getY(), bandit.getZ());
		boolean inclusiveBoundary =
				bandit.checksVillagerAlertRange(
						villager);
		villager.setPos(bandit.getX() + 15.01D,
				bandit.getY(), bandit.getZ());
		require(helper,
				inclusiveBoundary
						&& !bandit
								.checksVillagerAlertRange(
										villager),
				"Biscuit Bandit lost its exact inclusive 15-block Villager alert boundary");
		villager.setPos(bandit.getX(),
				bandit.getY(), bandit.getZ());
		villager.setNoAi(true);
		helper.getLevel().addFreshEntity(villager);
		villager.getBrain().eraseMemory(
				MemoryModuleType.NEAREST_HOSTILE);
		villager.getBrain().setMemory(
				MemoryModuleType
						.NEAREST_VISIBLE_LIVING_ENTITIES,
				new NearestVisibleLivingEntities(
						villager, List.of(bandit)));
		bandit.setTestTickCount(20);
		bandit.runVillagerHostileRepair();
		require(helper,
				villager.getBrain().getMemory(
						MemoryModuleType.NEAREST_HOSTILE)
						.filter(hostile -> hostile
								== bandit)
						.isPresent(),
				"Biscuit Bandit did not repair Pillager's exact 15-block Villager fear role");
		villager.discard();

		Evoker illagerColleague =
				EntityType.EVOKER.create(helper.getLevel());
		require(helper,
				illagerColleague != null
						&& bandit.getMobType()
								== MobType.ILLAGER
						&& bandit.isAlliedTo(
								illagerColleague),
				"Biscuit Bandit lost its Illager alliance");
		illagerColleague.discard();

		BiscuitBanditProbe raidBandit =
				new BiscuitBanditProbe(helper.getLevel());
		raidBandit.setPos(bandit.getX() + 4.0D,
				bandit.getY(), bandit.getZ());
		raidBandit.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						raidBandit.blockPosition()),
				MobSpawnType.EVENT, null, null);
		Raid buffRaid = new Raid(197841,
				helper.getLevel(),
				raidBandit.blockPosition());
		buffRaid.setBadOmenLevel(5);
		buffRaid.joinRaid(
				7, raidBandit, null, true);
		buffRaid.setLeader(7, raidBandit);
		raidBandit.seedRandom(0L);
		raidBandit.applyRaidBuffs(7, false);
		ItemStack raidCrossbow =
				raidBandit.getMainHandItem();
		require(helper,
				raidBandit.getCurrentRaid() == buffRaid
						&& raidBandit.getWave() == 7
						&& buffRaid.getLeader(7)
								== raidBandit
						&& ItemStack.isSameItemSameTags(
								raidBandit.getItemBySlot(
										EquipmentSlot.HEAD),
								Raid.getLeaderBannerInstance())
						&& raidCrossbow.is(Items.CROSSBOW)
						&& EnchantmentHelper
								.getItemEnchantmentLevel(
										Enchantments
												.QUICK_CHARGE,
										raidCrossbow)
								== 2
						&& EnchantmentHelper
								.getItemEnchantmentLevel(
										Enchantments
												.MULTISHOT,
										raidCrossbow)
								== 1,
				"Biscuit Bandit lost raid membership, leadership, banner or wave-seven crossbow buffs");
		buffRaid.removeFromRaid(raidBandit, true);
		raidBandit.discard();

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					List.of(Difficulty.EASY,
							Difficulty.NORMAL)) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				Pig meleeTarget =
						EntityType.PIG.create(
								helper.getLevel());
				require(helper, meleeTarget != null,
						"Could not create safe Biscuit Bandit melee target");
				meleeTarget.setPos(bandit.getX() + 6.0D,
						bandit.getY(),
						bandit.getZ()
								+ safeDifficulty.getId()
										* 2.0D);
				helper.getLevel().addFreshEntity(
						meleeTarget);
				meleeTarget.setSecondsOnFire(5);
				meleeTarget.fallDistance = 8.0F;
				bandit.doHurtTarget(meleeTarget);
				require(helper,
						close(meleeTarget.getHealth(),
								meleeTarget
										.getMaxHealth())
								&& !meleeTarget.isOnFire()
								&& close(meleeTarget
										.fallDistance,
										0.0D)
								&& meleeTarget.hasEffect(
										MobEffects
												.BLINDNESS)
								&& meleeTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& meleeTarget.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& meleeTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& meleeTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Biscuit Bandit melee caused damage or lacked crumb rescue");
				meleeTarget.discard();

				Pig rangedTarget =
						EntityType.PIG.create(
								helper.getLevel());
				require(helper, rangedTarget != null,
						"Could not create safe Biscuit Bandit bolt target");
				rangedTarget.setPos(bandit.getX() + 7.0D,
						bandit.getY(),
						bandit.getZ()
								+ safeDifficulty.getId()
										* 2.0D);
				helper.getLevel().addFreshEntity(
						rangedTarget);
				Arrow safeArrow = new Arrow(
						helper.getLevel(), bandit);
				rangedTarget.hurt(DamageSource.arrow(
						safeArrow, bandit), 5.0F);
				require(helper,
						close(rangedTarget.getHealth(),
								rangedTarget
										.getMaxHealth())
								&& rangedTarget.hasEffect(
										MobEffects
												.BLINDNESS)
								&& rangedTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE),
						safeDifficulty
								+ " Biscuit Bandit bolt caused damage or lacked crumb rescue");
				rangedTarget.discard();
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardMelee =
					EntityType.PIG.create(helper.getLevel());
			Pig hardRanged =
					EntityType.PIG.create(helper.getLevel());
			require(helper,
					hardMelee != null && hardRanged != null,
					"Could not create Hard Biscuit Bandit targets");
			hardMelee.setPos(bandit.getX() + 6.0D,
					bandit.getY(), bandit.getZ() + 6.0D);
			hardRanged.setPos(bandit.getX() + 7.0D,
					bandit.getY(), bandit.getZ() + 7.0D);
			helper.getLevel().addFreshEntity(hardMelee);
			helper.getLevel().addFreshEntity(hardRanged);
			bandit.doHurtTarget(hardMelee);
			Arrow hardArrow = new Arrow(
					helper.getLevel(), bandit);
			hardRanged.hurt(DamageSource.arrow(
					hardArrow, bandit), 5.0F);
			require(helper,
					hardMelee.getHealth()
							< hardMelee.getMaxHealth()
							&& hardRanged.getHealth()
									< hardRanged
											.getMaxHealth(),
					"Hard Biscuit Bandit melee or bolt did not cause real damage");
			hardMelee.discard();
			hardRanged.discard();

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			BiscuitBanditProbe peaceful =
					new BiscuitBanditProbe(
							helper.getLevel());
			peaceful.setPos(bandit.getX() + 9.0D,
					bandit.getY(), bandit.getZ());
			helper.getLevel().addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Biscuit Bandit did not retain vanilla Monster removal");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						helper.absolutePos(
								new BlockPos(8, 3, 8)),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Pillager conversion");
		Pillager literalPatrol =
				EntityType.PILLAGER.create(
						helper.getLevel());
		require(helper, literalPatrol != null,
				"Could not create literal patrol Pillager fixture");
		literalPatrol.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literalPatrol.setPatrolLeader(true);
		BlockPos convertedPatrolTarget =
				cakeWorldPos.offset(96, 0, -64);
		literalPatrol.setPatrolTarget(
				convertedPatrolTarget);
		literalPatrol.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.PATROL, null, null);
		literalPatrol.setNoAi(true);
		literalPatrol.setHealth(17.0F);
		literalPatrol.setCustomName(new TextComponent(
				"Crumb Patrol Captain"));
		literalPatrol.setPersistenceRequired();
		literalPatrol.getInventory().setItem(0,
				new ItemStack(Items.ARROW, 2));
		BiscuitBandit patrolReplacement =
				CakeWorldPillagerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literalPatrol);
		require(helper,
				literalPatrol.isRemoved()
						&& patrolReplacement != null
						&& patrolReplacement.isNoAi()
						&& patrolReplacement
								.isPersistenceRequired()
						&& close(patrolReplacement
								.getHealth(), 17.0D)
						&& patrolReplacement
								.isPatrolLeader()
						&& patrolReplacement
								.hasPatrolTarget()
						&& convertedPatrolTarget.equals(
								patrolReplacement
										.getPatrolTarget())
						&& ItemStack.isSameItemSameTags(
								patrolReplacement
										.getItemBySlot(
												EquipmentSlot
														.HEAD),
								Raid.getLeaderBannerInstance())
						&& patrolReplacement
								.getInventory()
								.countItem(Items.ARROW)
								== 2
						&& "Crumb Patrol Captain".equals(
								patrolReplacement
										.getName()
										.getString()),
				"Fresh patrol Pillager conversion lost captain, target, banner, inventory or NBT state");

		Ravager ravager =
				EntityType.RAVAGER.create(helper.getLevel());
		Pillager literalRaider =
				EntityType.PILLAGER.create(
						helper.getLevel());
		require(helper,
				ravager != null && literalRaider != null,
				"Could not create mounted raid Pillager fixtures");
		ravager.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		ravager.setNoAi(true);
		helper.getLevel().addFreshEntity(ravager);
		literalRaider.setPos(ravager.getX(),
				ravager.getY(), ravager.getZ());
		literalRaider.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.EVENT, null, null);
		literalRaider.setNoAi(true);
		literalRaider.setCustomName(new TextComponent(
				"Mounted Biscuit Captain"));
		literalRaider.getInventory().setItem(0,
				new ItemStack(Items.ARROW, 4));
		Raid transferRaid = new Raid(197842,
				helper.getLevel(), cakeWorldPos);
		transferRaid.joinRaid(
				3, literalRaider, null, true);
		transferRaid.setLeader(3, literalRaider);
		literalRaider.startRiding(ravager, true);
		literalRaider.moveTo(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D,
				31.0F, 0.0F);
		BiscuitBandit raidReplacement =
				CakeWorldPillagerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literalRaider);
		require(helper,
				literalRaider.isRemoved()
						&& raidReplacement != null
						&& raidReplacement
								.getCurrentRaid()
								== transferRaid
						&& raidReplacement.getWave() == 3
						&& transferRaid.getLeader(3)
								== raidReplacement
						&& transferRaid
								.getTotalRaidersAlive()
								== 1
						&& raidReplacement.getVehicle()
								== ravager
						&& ravager.getPassengers()
								.contains(
										raidReplacement)
						&& raidReplacement
								.getInventory()
								.countItem(Items.ARROW)
								== 4
						&& "Mounted Biscuit Captain"
								.equals(raidReplacement
										.getName()
										.getString()),
				"Fresh raid Pillager conversion lost wave, leader, mount, inventory or NBT state");

		ConfiguredStructureFeature<?, ?> outpost =
				StructureFeatures.PILLAGER_OUTPOST.value();
		StructureSpawnOverride outpostOverride =
				outpost.spawnOverrides.get(
						MobCategory.MONSTER);
		MobSpawnSettings.SpawnerData outpostSpawn =
				outpostOverride == null ? null
						: outpostOverride.spawns()
								.unwrap().stream()
								.filter(spawn -> spawn.type
										== EntityType
												.PILLAGER)
								.findFirst()
								.orElse(null);
		require(helper,
				outpostOverride != null
						&& outpostOverride.boundingBox()
								== StructureSpawnOverride
										.BoundingBoxType
										.STRUCTURE
						&& outpostSpawn != null
						&& outpostSpawn.getWeight()
								.asInt() == 1
						&& outpostSpawn.minCount == 1
						&& outpostSpawn.maxCount == 1,
				"Biscuit Bandit lost the exact dormant Outpost 1/1-1 literal source contract");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = helper.getLevel()
					.registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.PILLAGER
											|| spawn.type
													== CakeWorldEntities
															.BISCUIT_BANDIT
															.get()),
					"Open biome leaked Pillager/Biscuit Bandit spawning before Lookouts: "
							+ biomeId);
		}

		TagKey<EntityType<?>> raiders =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft", "raiders"));
		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2041"),
						"CakeWorldBiscuitBanditRoleTest"));
		Arrow roleArrow = new Arrow(
				helper.getLevel(), advancementPlayer);
		roleArrow.setShotFromCrossbow(true);
		VanillaRoleAdvancements
				.creditKilledPillagerRole(
						advancementPlayer);
		VanillaRoleAdvancements
				.creditWhosPillagerNowRole(
						advancementPlayer, roleArrow);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:pillager");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/whos_the_pillager_now",
				"kill_pillager");
		require(helper,
				CakeWorldItems
						.BISCUIT_BANDIT_SPAWN_EGG
						.isPresent()
						&& CakeWorldEntities.BISCUIT_BANDIT
								.get().is(raiders)
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.BISCUIT_BANDIT
												.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.BISCUIT_BANDIT
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.PILLAGER)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.BISCUIT_BANDIT
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.BISCUIT_BANDIT
												.get())
								== SoundEvents
										.PARROT_IMITATE_PILLAGER,
				"Biscuit Bandit lost its raider tag, egg, placement or mimic role");

		bandit.discard();
		patrolReplacement.discard();
		transferRaid.removeFromRaid(
				raidReplacement, true);
		raidReplacement.discard();
		ravager.discard();
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void vanillaIceBearsKeepCubDefenceAndSafeWarnings(
			GameTestHelper helper) {
		VanillaIceBearProbe bear =
				new VanillaIceBearProbe(helper.getLevel());
		VanillaIceBearProbe restored =
				new VanillaIceBearProbe(helper.getLevel());
		require(helper,
				bear instanceof PolarBear
						&& bear instanceof NeutralMob
						&& bear.getType()
								== CakeWorldEntities
										.VANILLA_ICE_BEAR.get()
						&& bear.getType().getCategory()
								== MobCategory.CREATURE
						&& close(bear.getMaxHealth(), 30.0D)
						&& close(bear.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								20.0D)
						&& close(bear.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.25D)
						&& close(bear.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								6.0D)
						&& close(bear.getDimensions(
								Pose.STANDING).width,
								1.4D)
						&& close(bear.getDimensions(
								Pose.STANDING).height,
								1.4D)
						&& bear.getType()
								.clientTrackingRange() == 10
						&& bear.getMaxSpawnClusterSize() == 4
						&& bear.getAmbientSoundInterval() == 120
						&& close(bear.waterSlowDown(), 0.98D)
						&& bear.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/vanilla_ice_bear")),
				"Vanilla-Ice Bear lost exact Polar Bear type, body, attributes, tracking, group, ambient, swimming or loot roles");

		bear.seedRandom(1978L);
		int experience = bear.getExperienceValue();
		require(helper,
				experience >= 1 && experience <= 3
						&& bear.countGoalsNamed(
								"FloatGoal") == 1
						&& bear.countGoalsNamed(
								"PolarBearMeleeAttackGoal")
								== 1
						&& bear.countGoalsNamed(
								"PolarBearPanicGoal") == 1
						&& bear.countGoalsNamed(
								"FollowParentGoal") == 1
						&& bear.countGoalsNamed(
								"RandomStrollGoal") == 1
						&& bear.countGoalsNamed(
								"LookAtPlayerGoal") == 1
						&& bear.countGoalsNamed(
								"RandomLookAroundGoal") == 1
						&& bear.countTargetGoalsNamed(
								"PolarBearHurtByTargetGoal")
								== 1
						&& bear.countTargetGoalsNamed(
								"PolarBearAttackPlayersGoal")
								== 1
						&& bear.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 2
						&& bear.countTargetGoalsNamed(
								"ResetUniversalAngerTargetGoal")
								== 1,
				"Vanilla-Ice Bear lost Polar Bear XP or exact movement, combat, cub-defence, anger or Fox-hunting goals");

		require(helper,
				!bear.isFood(new ItemStack(Items.COD))
						&& !bear.isFood(
								new ItemStack(Items.SALMON))
						&& !bear.isFood(
								new ItemStack(Items.CAKE)),
				"Vanilla-Ice Bear invented a player breeding food that vanilla Polar Bears do not have");
		VanillaIceBear child = bear.getBreedOffspring(
				helper.getLevel(), restored);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities
										.VANILLA_ICE_BEAR.get(),
				"Vanilla-Ice Bear command/mod family creation returned a literal vanilla Polar Bear");

		UUID angerTarget = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2042");
		bear.startPersistentAngerTimer();
		require(helper,
				bear.getRemainingPersistentAngerTime() >= 400
						&& bear.getRemainingPersistentAngerTime()
								<= 780,
				"Vanilla-Ice Bear lost the exact 20-39 second persistent anger range");
		bear.setRemainingPersistentAngerTime(500);
		bear.setPersistentAngerTarget(angerTarget);
		CompoundTag angerData = new CompoundTag();
		bear.addAdditionalSaveData(angerData);
		restored.readAdditionalSaveData(angerData);
		require(helper,
				restored.getRemainingPersistentAngerTime()
								== 500
						&& angerTarget.equals(
								restored
										.getPersistentAngerTarget()),
				"Vanilla-Ice Bear did not retain persistent anger across save/reload");

		require(helper,
				bear.ambientSound()
								== SoundEvents.POLAR_BEAR_AMBIENT
						&& bear.hurtSound()
								== SoundEvents.POLAR_BEAR_HURT
						&& bear.deathSound()
								== SoundEvents.POLAR_BEAR_DEATH,
				"Adult Vanilla-Ice Bear lost exact Polar Bear sounds");
		bear.setBaby(true);
		require(helper,
				bear.ambientSound()
						== SoundEvents.POLAR_BEAR_AMBIENT_BABY,
				"Baby Vanilla-Ice Bear lost its exact cub sound");
		bear.setBaby(false);
		bear.setStanding(true);
		require(helper, bear.isStanding(),
				"Vanilla-Ice Bear lost its visible standing warning state");
		bear.runWarningSound();
		require(helper,
				bear.lastSound()
						== SoundEvents.POLAR_BEAR_WARNING,
				"Vanilla-Ice Bear lost its warning growl");
		bear.runStepSound();
		require(helper,
				bear.lastSound()
						== SoundEvents.POLAR_BEAR_STEP,
				"Vanilla-Ice Bear lost its Polar Bear step sound");

		BlockPos familyAnchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		VanillaIceBearProbe adult =
				new VanillaIceBearProbe(helper.getLevel());
		VanillaIceBearProbe cub =
				new VanillaIceBearProbe(helper.getLevel());
		Pig familyAttacker =
				EntityType.PIG.create(helper.getLevel());
		require(helper, familyAttacker != null,
				"Could not create Vanilla-Ice Bear family alert fixture");
		cub.setBaby(true);
		adult.setPos(familyAnchor.getX() + 0.5D,
				familyAnchor.getY(),
				familyAnchor.getZ() + 0.5D);
		cub.setPos(familyAnchor.getX() + 2.5D,
				familyAnchor.getY(),
				familyAnchor.getZ() + 0.5D);
		familyAttacker.setPos(familyAnchor.getX() + 4.5D,
				familyAnchor.getY(),
				familyAnchor.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(adult);
		helper.getLevel().addFreshEntity(cub);
		helper.getLevel().addFreshEntity(familyAttacker);
		// HurtByTargetGoal consumes LivingEntity's standard last-attacker
		// state and ignores an initial timestamp of zero. Seed that vanilla
		// input directly so this test isolates the inherited family alert
		// rather than duplicating Minecraft's global damage bookkeeping.
		cub.setTestTickCount(1);
		cub.setLastHurtByMob(familyAttacker);
		require(helper,
				cub.getLastHurtByMob()
								== familyAttacker
						&& cub.getLastHurtByMobTimestamp()
								> 0,
				"Vanilla-Ice cub fixture did not record its living attacker");
		require(helper,
				cub.startTargetGoalNamed(
						"PolarBearHurtByTargetGoal"),
				"Injured Vanilla-Ice cub could not start the inherited hurt-by-target goal");
		require(helper,
				adult.getTarget() == familyAttacker,
				"Injured Vanilla-Ice cub did not alert a nearby adult through the inherited Polar Bear family role");
		require(helper, cub.getTarget() == null,
				"Baby Vanilla-Ice Bear kept pursuing its attacker after alerting an adult");

		Pig swipeTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, swipeTarget != null,
				"Could not create Vanilla-Ice Bear swipe target");
		swipeTarget.setPos(familyAnchor.getX() + 2.0D,
				familyAnchor.getY(),
				familyAnchor.getZ() + 4.0D);
		adult.setPos(familyAnchor.getX(),
				familyAnchor.getY(),
				familyAnchor.getZ() + 4.0D);
		helper.getLevel().addFreshEntity(swipeTarget);
		for (Difficulty safeDifficulty :
				new Difficulty[] {
						Difficulty.PEACEFUL,
						Difficulty.EASY,
						Difficulty.NORMAL}) {
			swipeTarget.removeAllEffects();
			swipeTarget.setHealth(
					swipeTarget.getMaxHealth());
			swipeTarget.invulnerableTime = 0;
			swipeTarget.setSecondsOnFire(5);
			swipeTarget.fallDistance = 12.0F;
			swipeTarget.setDeltaMovement(Vec3.ZERO);
			LivingHurtEvent protectedSwipe =
					new LivingHurtEvent(swipeTarget,
							DamageSource.mobAttack(adult),
							6.0F);
			VanillaIceBearDamageSafety
					.applyForDifficulty(
							protectedSwipe,
							safeDifficulty);
			require(helper,
					protectedSwipe.isCanceled()
							&& close(
									swipeTarget.getHealth(),
									swipeTarget
											.getMaxHealth())
							&& !swipeTarget.isOnFire()
							&& swipeTarget.fallDistance
									== 0.0F
							&& swipeTarget.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& swipeTarget.hasEffect(
									MobEffects
											.SLOW_FALLING)
							&& swipeTarget.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& swipeTarget.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4
							&& swipeTarget
									.getDeltaMovement().x
									> 0.0D
							&& swipeTarget
									.getDeltaMovement().y
									> 0.0D,
					safeDifficulty
							+ " Vanilla-Ice Bear swipe caused health or indirect peril, or lacked its cream-cushion rescue");
		}
		swipeTarget.removeAllEffects();
		swipeTarget.setDeltaMovement(Vec3.ZERO);
		LivingHurtEvent hardSwipe =
				new LivingHurtEvent(swipeTarget,
						DamageSource.mobAttack(adult),
						6.0F);
		VanillaIceBearDamageSafety.applyForDifficulty(
				hardSwipe, Difficulty.HARD);
		require(helper,
				!hardSwipe.isCanceled()
						&& close(hardSwipe.getAmount(), 6.0D)
						&& swipeTarget.getActiveEffects()
								.isEmpty()
						&& swipeTarget.getDeltaMovement()
								.equals(Vec3.ZERO),
				"Hard Vanilla-Ice Bear did not retain an unmodified six-point Polar Bear swipe");

		TagKey<EntityType<?>> freezeImmune =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"freeze_immune_entity_types"));
		require(helper,
				CakeWorldEntities.VANILLA_ICE_BEAR.get()
								.is(freezeImmune)
						&& !bear.canFreeze()
						&& !CakeWorldEntities
								.VANILLA_ICE_BEAR.get()
								.isBlockDangerous(
										Blocks.POWDER_SNOW
												.defaultBlockState())
						&& CakeWorldItems
								.VANILLA_ICE_BEAR_SPAWN_EGG
								.isPresent(),
				"Vanilla-Ice Bear lost freeze, powder-snow or creative/testing-egg contracts");

		MobSpawnSettingsBuilder futureSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		futureSpawns.getSpawner(MobCategory.CREATURE)
				.add(new MobSpawnSettings.SpawnerData(
						EntityType.POLAR_BEAR, 1, 1, 2));
		BiomeLoadingEvent futureTundra =
				new BiomeLoadingEvent(
						new ResourceLocation(
								CakeWorld.MODID,
								"ice_cream_tundra"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings
										.EMPTY),
						futureSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(futureTundra);
		MobSpawnSettings.SpawnerData futureSpawn =
				futureSpawns
						.getSpawner(MobCategory.CREATURE)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.VANILLA_ICE_BEAR
										.get())
						.findFirst().orElse(null);
		require(helper,
				futureSpawn != null
						&& futureSpawn.getWeight().asInt() == 1
						&& futureSpawn.minCount == 1
						&& futureSpawn.maxCount == 2
						&& futureSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType
														.POLAR_BEAR),
				"Future Ice-Cream Tundra hook lost the exact snowy 1/1-2 Polar Bear replacement");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = helper.getLevel()
					.registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.POLAR_BEAR
											|| spawn.type
													== CakeWorldEntities
															.VANILLA_ICE_BEAR
															.get()),
					"Current biome leaked Polar/Vanilla-Ice Bear spawning before Ice-Cream Tundra exists: "
							+ biomeId);
		}

		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation(
								"minecraft",
								"adventure/kill_all_mobs"));
		Advancement bredAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation(
								"minecraft",
								"husbandry/bred_all_animals"));
		require(helper,
				killAll != null
						&& bredAll != null
						&& !killAll.getCriteria()
								.containsKey(
										"minecraft:polar_bear")
						&& !bredAll.getCriteria()
								.containsKey(
										"minecraft:polar_bear")
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.VANILLA_ICE_BEAR
												.get())
								== null,
				"Vanilla unexpectedly assigned a Polar Bear kill, breeding or Parrot-mimic role; reassess before inventing a compatibility bridge");

		BlockPos spawnPos = familyAnchor.offset(8, 0, 0);
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.FROZEN_LEMONADE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.LIGHT.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.runAfterDelay(5, () -> {
			require(helper,
					helper.getLevel().getBlockState(
							spawnPos.below())
							.is(BlockTags
									.ANIMALS_SPAWNABLE_ON)
							&& Animal
									.checkAnimalSpawnRules(
											CakeWorldEntities
													.VANILLA_ICE_BEAR
													.get(),
											helper.getLevel(),
											MobSpawnType.NATURAL,
											spawnPos,
											new Random(1978L))
							&& SpawnPlacements
									.checkSpawnRules(
											CakeWorldEntities
													.VANILLA_ICE_BEAR
													.get(),
											helper.getLevel(),
											MobSpawnType.NATURAL,
											spawnPos,
											new Random(1979L))
							&& SpawnPlacements.Type
									.ON_GROUND.canSpawnAt(
											helper.getLevel(),
											spawnPos,
											CakeWorldEntities
													.VANILLA_ICE_BEAR
													.get())
							&& SpawnPlacements
									.getPlacementType(
											CakeWorldEntities
													.VANILLA_ICE_BEAR
													.get())
									== SpawnPlacements.Type
											.ON_GROUND
							&& SpawnPlacements
									.getHeightmapType(
											CakeWorldEntities
													.VANILLA_ICE_BEAR
													.get())
									== Heightmap.Types
											.MOTION_BLOCKING_NO_LEAVES,
					"Vanilla-Ice Bear lost bright frozen-lemonade placement, edible surface or exact metadata");
			adult.discard();
			cub.discard();
			familyAttacker.discard();
			swipeTarget.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void fizzballFishKeepInflationBucketsAndSafeStings(
			GameTestHelper helper) {
		FizzballFishProbe fish =
				new FizzballFishProbe(helper.getLevel());
		FizzballFishProbe restored =
				new FizzballFishProbe(helper.getLevel());
		require(helper,
				fish instanceof Pufferfish
						&& fish.getType()
								== CakeWorldEntities
										.FIZZBALL_FISH.get()
						&& fish.getType().getCategory()
								== MobCategory.WATER_AMBIENT
						&& close(fish.getMaxHealth(), 3.0D)
						&& close(fish.getDimensions(
								Pose.STANDING).width,
								0.35D)
						&& close(fish.getDimensions(
								Pose.STANDING).height,
								0.35D)
						&& fish.getType()
								.clientTrackingRange() == 4
						&& fish.getMaxSpawnClusterSize() == 8
						&& fish.getAmbientSoundInterval() == 120
						&& fish.canBreatheUnderwater()
						&& fish.getMobType()
								== MobType.WATER
						&& !fish.isPushedByFluid()
						&& fish.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/fizzball_fish")),
				"Fizzball Fish lost exact Pufferfish type, small body, health, tracking, group, water or loot roles");

		fish.seedRandom(1978L);
		int experience = fish.getExperienceValue();
		require(helper,
				experience >= 1 && experience <= 3
						&& fish.countGoalsNamed(
								"PanicGoal") == 1
						&& fish.countGoalsNamed(
								"AvoidEntityGoal") == 1
						&& fish.countGoalsNamed(
								"FishSwimGoal") == 1
						&& fish.countGoalsNamed(
								"PufferfishPuffGoal") == 1,
				"Fizzball Fish lost exact Pufferfish XP, panic, player avoidance, swimming or inflation goals");

		fish.setPuffState(1);
		require(helper,
				close(fish.getDimensions(
						Pose.STANDING).width, 0.49D),
				"Mid-inflated Fizzball Fish lost its exact 0.7 scale");
		fish.setPuffState(2);
		require(helper,
				close(fish.getDimensions(
						Pose.STANDING).width, 0.7D),
				"Fully inflated Fizzball Fish lost its exact full scale");
		CompoundTag puffData = new CompoundTag();
		fish.addAdditionalSaveData(puffData);
		restored.readAdditionalSaveData(puffData);
		require(helper,
				restored.getPuffState() == 2,
				"Fizzball Fish lost PuffState across save/reload");
		CompoundTag oversizedPuff = new CompoundTag();
		oversizedPuff.putInt("PuffState", 9);
		restored.readAdditionalSaveData(oversizedPuff);
		require(helper,
				restored.getPuffState() == 2,
				"Fizzball Fish lost vanilla's maximum saved puff-state clamp");

		BlockPos anchor = helper.absolutePos(
				new BlockPos(3, 3, 3));
		Pig scaryMob = EntityType.PIG.create(
				helper.getLevel());
		require(helper, scaryMob != null,
				"Could not create Fizzball inflation threat");
		fish.setPuffState(0);
		fish.setNoGravity(true);
		fish.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		scaryMob.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(scaryMob);
		require(helper,
				fish.startGoalNamed(
						"PufferfishPuffGoal"),
				"Fizzball Fish could not start the genuine proximity inflation goal");
		for (int tick = 0; tick < 42; tick++) {
			fish.tick();
		}
		require(helper,
				fish.getPuffState() == 2
						&& fish.blowUpSoundCount() == 2,
				"Fizzball Fish did not progress from small to fully inflated after the exact forty-tick warning: state="
						+ fish.getPuffState()
						+ ", blow_up_sounds="
						+ fish.blowUpSoundCount());
		scaryMob.discard();
		fish.stopGoalNamed("PufferfishPuffGoal");
		for (int tick = 0; tick < 102; tick++) {
			fish.tick();
		}
		require(helper,
				fish.getPuffState() == 0
						&& fish.blowOutSoundCount() == 2,
				"Fizzball Fish did not deflate through vanilla's sixty/one-hundred-tick sequence");
		require(helper,
				fish.ambientSound()
								== SoundEvents.PUFFER_FISH_AMBIENT
						&& fish.hurtSound()
								== SoundEvents.PUFFER_FISH_HURT
						&& fish.deathSound()
								== SoundEvents.PUFFER_FISH_DEATH
						&& fish.flopSound()
								== SoundEvents.PUFFER_FISH_FLOP
						&& fish.getPickupSound()
								== SoundEvents.BUCKET_FILL_FISH,
				"Fizzball Fish lost exact Pufferfish ambient, hurt, death, flop or pickup sounds");

		FizzballFishProbe safetyFish =
				new FizzballFishProbe(helper.getLevel());
		Pig target = EntityType.PIG.create(
				helper.getLevel());
		require(helper, target != null,
				"Could not create Fizzball safety target");
		safetyFish.setPuffState(2);
		safetyFish.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ() + 4.0D);
		target.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ() + 4.0D);
		for (Difficulty safeDifficulty :
				new Difficulty[] {
						Difficulty.PEACEFUL,
						Difficulty.EASY,
						Difficulty.NORMAL}) {
			target.removeAllEffects();
			target.setHealth(target.getMaxHealth());
			target.setSecondsOnFire(5);
			target.fallDistance = 12.0F;
			target.setDeltaMovement(Vec3.ZERO);
			LivingAttackEvent protectedSting =
					new LivingAttackEvent(target,
							DamageSource.mobAttack(
									safetyFish),
							3.0F);
			FizzballFishDamageSafety
					.applyForDifficulty(
							protectedSting,
							safeDifficulty);
			require(helper,
					protectedSting.isCanceled()
							&& close(target.getHealth(),
									target.getMaxHealth())
							&& !target.isOnFire()
							&& target.fallDistance
									== 0.0F
							&& target.hasEffect(
									MobEffects
											.MOVEMENT_SPEED)
							&& target.hasEffect(
									MobEffects
											.SLOW_FALLING)
							&& target.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& target.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4
							&& !target.hasEffect(
									MobEffects.POISON)
							&& target.getDeltaMovement().x
									> 0.0D
							&& target.getDeltaMovement().y
									> 0.0D,
					safeDifficulty
							+ " Fizzball sting caused health, poison or indirect peril, or lacked its fizzy rescue");
		}

		target.removeAllEffects();
		target.setDeltaMovement(Vec3.ZERO);
		LivingAttackEvent hardSting =
				new LivingAttackEvent(target,
						DamageSource.mobAttack(safetyFish),
						3.0F);
		FizzballFishDamageSafety.applyForDifficulty(
				hardSting, Difficulty.HARD);
		require(helper,
				!hardSting.isCanceled()
						&& close(hardSting.getAmount(), 3.0D)
						&& target.getActiveEffects().isEmpty()
						&& target.getDeltaMovement()
								.equals(Vec3.ZERO),
				"Hard Fizzball Fish did not retain an unmodified full-puff three-point sting for vanilla poison handling");

		ServerPlayer contactPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2043"),
						"CakeWorldFizzballContactTest"));
		clearServerPlayerSpawnInvulnerability(
				contactPlayer);
		contactPlayer.connection =
				new ServerGamePacketListenerImpl(
						helper.getLevel().getServer(),
						new Connection(
								PacketFlow.CLIENTBOUND),
						contactPlayer);
		contactPlayer.getAbilities().invulnerable = false;
		contactPlayer.setInvulnerable(false);
		contactPlayer.invulnerableTime = 0;
		contactPlayer.removeAllEffects();
		contactPlayer.setHealth(contactPlayer.getMaxHealth());
		contactPlayer.setPos(anchor.getX() + 1.0D,
				anchor.getY(), anchor.getZ() + 4.0D);
		contactPlayer.setSecondsOnFire(5);
		contactPlayer.fallDistance = 12.0F;
		contactPlayer.setDeltaMovement(Vec3.ZERO);
		safetyFish.playerTouch(contactPlayer);
		require(helper,
				close(contactPlayer.getHealth(),
						contactPlayer.getMaxHealth())
						&& !contactPlayer
								.hasEffect(MobEffects.POISON)
						&& contactPlayer.hasEffect(
								MobEffects.MOVEMENT_SPEED)
						&& contactPlayer.getDeltaMovement().y
								> 0.0D
						&& safetyFish.lastSound()
								== SoundEvents
										.PUFFER_FISH_STING,
				"Real Normal player contact bypassed the early attack cancellation, added poison, or lost sting feedback: health="
						+ contactPlayer.getHealth()
						+ "/"
						+ contactPlayer.getMaxHealth()
						+ ", poison="
						+ contactPlayer.hasEffect(
								MobEffects.POISON)
						+ ", speed="
						+ contactPlayer.hasEffect(
								MobEffects.MOVEMENT_SPEED)
						+ ", motion="
						+ contactPlayer.getDeltaMovement()
						+ ", sound="
						+ safetyFish.lastSound());

		Axolotl axolotl = EntityType.AXOLOTL.create(
				helper.getLevel());
		require(helper, axolotl != null,
				"Could not create Fizzball Axolotl-contact target");
		axolotl.setPos(safetyFish.getX(),
				safetyFish.getY(), safetyFish.getZ());
		axolotl.setHealth(axolotl.getMaxHealth());
		helper.getLevel().addFreshEntity(safetyFish);
		helper.getLevel().addFreshEntity(axolotl);
		safetyFish.aiStep();
		require(helper,
				close(axolotl.getHealth(),
						axolotl.getMaxHealth())
						&& !axolotl.hasEffect(
								MobEffects.POISON)
						&& axolotl.hasEffect(
								MobEffects.MOVEMENT_SPEED),
				"Real Normal mob contact bypassed the early attack cancellation or Axolotl-specific Pufferfish threat role");

		BlockPos lemonadePos = new BlockPos(anchor.getX() + 8,
				helper.getLevel().getSeaLevel() - 5,
				anchor.getZ());
		for (int y = -1; y <= 1; y++) {
			helper.getLevel().setBlock(
					lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
		}
		boolean vanillaRule =
				WaterAnimal.checkSurfaceWaterAnimalSpawnRules(
						CakeWorldEntities.FIZZBALL_FISH.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						lemonadePos,
						new Random(1978L));
		boolean fizzballRule =
				FizzballFish.checkFizzballFishSpawnRules(
						CakeWorldEntities.FIZZBALL_FISH.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						lemonadePos,
						new Random(1978L));
		require(helper,
				!vanillaRule
						&& fizzballRule
						&& SpawnPlacements
								.checkSpawnRules(
										CakeWorldEntities
												.FIZZBALL_FISH
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										lemonadePos,
										new Random(1979L))
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.FIZZBALL_FISH
												.get())
								== SpawnPlacements.Type
										.IN_WATER
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.FIZZBALL_FISH
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Fizzball Fish lost Lemonade-compatible surface spawning or exact placement metadata");

		FizzballFish captureFish =
				CakeWorldEntities.FIZZBALL_FISH.get()
						.create(helper.getLevel());
		require(helper, captureFish != null,
				"Could not create Fizzball bucket fixture");
		captureFish.setCustomName(
				new TextComponent("Pop"));
		captureFish.setPos(lemonadePos.getX() + 0.5D,
				lemonadePos.getY(),
				lemonadePos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(captureFish);
		ServerPlayer bucketPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed3043"),
						"CakeWorldFizzballBucketTest"));
		bucketPlayer.setItemInHand(InteractionHand.MAIN_HAND,
				new ItemStack(Items.WATER_BUCKET));
		InteractionResult captureResult =
				bucketPlayer.interactOn(captureFish,
						InteractionHand.MAIN_HAND);
		ItemStack bucket = bucketPlayer.getItemInHand(
				InteractionHand.MAIN_HAND);
		require(helper,
				captureResult.consumesAction()
						&& bucket.is(
								CakeWorldItems
										.FIZZBALL_FISH_BUCKET
										.get())
						&& bucket.hasCustomHoverName()
						&& captureFish.isRemoved(),
				"Fizzball Fish did not capture into its dedicated named Lemonade bucket");
		AABB releaseArea = new AABB(lemonadePos).inflate(2.0D);
		helper.getLevel().getEntitiesOfClass(
				FizzballFish.class, releaseArea)
				.forEach(FizzballFish::discard);
		((MobBucketItem)bucket.getItem()).checkExtraContent(
				null, helper.getLevel(), bucket, lemonadePos);
		List<FizzballFish> released =
				helper.getLevel().getEntitiesOfClass(
						FizzballFish.class, releaseArea);
		require(helper,
				released.size() == 1
						&& released.get(0).fromBucket()
						&& released.get(0).hasCustomName()
						&& "Pop".equals(released.get(0)
								.getCustomName().getString()),
				"Fizzball bucket released the wrong entity or lost from-bucket/name data");
		requireCriterion(helper, bucketPlayer,
				"minecraft:husbandry/tactical_fishing",
				"pufferfish_bucket");

		Biome sodaOcean = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Fizzball spawning");
		MobSpawnSettings.SpawnerData sodaSpawn =
				sodaOcean.getMobSettings()
						.getMobs(MobCategory.WATER_AMBIENT)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.FIZZBALL_FISH.get())
						.findFirst().orElse(null);
		require(helper,
				sodaSpawn != null
						&& sodaSpawn.getWeight().asInt() == 5
						&& sodaSpawn.minCount == 1
						&& sodaSpawn.maxCount == 3
						&& sodaOcean.getMobSettings()
								.getMobs(
										MobCategory
												.WATER_AMBIENT)
								.unwrap().stream()
								.noneMatch(spawn ->
										spawn.type
												== EntityType
														.PUFFERFISH),
				"Soda Ocean lost the exact 5/1-3 Fizzball replacement or leaked vanilla Pufferfish");

		TagKey<EntityType<?>> axolotlPrey =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"axolotl_hunt_targets"));
		Advancement killAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"adventure/kill_all_mobs"));
		Advancement bredAll = helper.getLevel().getServer()
				.getAdvancements().getAdvancement(
						new ResourceLocation("minecraft",
								"husbandry/bred_all_animals"));
		require(helper,
				CakeWorldEntities.FIZZBALL_FISH.get()
								.is(axolotlPrey)
						&& CakeWorldItems
								.FIZZBALL_FISH_SPAWN_EGG
								.isPresent()
						&& killAll != null
						&& bredAll != null
						&& !killAll.getCriteria()
								.containsKey(
										"minecraft:pufferfish")
						&& !bredAll.getCriteria()
								.containsKey(
										"minecraft:pufferfish")
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.FIZZBALL_FISH
												.get())
								== null,
				"Fizzball Fish lost its Axolotl-prey/egg roles or fabricated a kill, breeding or Parrot-mimic contract");

		safetyFish.discard();
		axolotl.discard();
		released.forEach(FizzballFish::discard);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void gummyBunniesKeepVariantsGardensAndSafeBites(
			GameTestHelper helper) {
		GummyBunnyProbe bunny =
				new GummyBunnyProbe(helper.getLevel());
		GummyBunnyProbe restored =
				new GummyBunnyProbe(helper.getLevel());
		bunny.seedRandom(1978L);
		int experience = bunny.getExperienceValue();
		require(helper,
				bunny instanceof Rabbit
						&& bunny.getType()
								== CakeWorldEntities
										.GUMMY_BUNNY.get()
						&& bunny.getType().getCategory()
								== MobCategory.CREATURE
						&& close(bunny.getMaxHealth(), 3.0D)
						&& close(bunny.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.3D)
						&& close(bunny.getAttributeValue(
								Attributes.ARMOR), 0.0D)
						&& close(bunny.getDimensions(
								Pose.STANDING).width,
								0.4D)
						&& close(bunny.getDimensions(
								Pose.STANDING).height,
								0.5D)
						&& bunny.getType()
								.clientTrackingRange() == 8
						&& bunny.getMaxSpawnClusterSize() == 4
						&& experience >= 1
						&& experience <= 3
						&& bunny.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/gummy_bunny")),
				"Gummy Bunny lost the exact Rabbit body, attributes, tracking, cluster, XP or loot roles");

		require(helper,
				bunny.countGoalsNamed("FloatGoal") == 1
						&& bunny.countGoalsNamed(
								"ClimbOnTopOfPowderSnowGoal")
								== 1
						&& bunny.countGoalsNamed(
								"RabbitPanicGoal") == 1
						&& bunny.countGoalsNamed(
								"BreedGoal") == 1
						&& bunny.countGoalsNamed(
								"TemptGoal") == 2
						&& bunny.countGoalsNamed(
								"RabbitAvoidEntityGoal") == 3
						&& bunny.countGoalsNamed(
								"RaidGardenGoal") == 1
						&& bunny.countGoalsNamed(
								"WaterAvoidingRandomStrollGoal")
								== 1
						&& bunny.countGoalsNamed(
								"LookAtPlayerGoal") == 1
						&& bunny.countGoalsNamed(
								"EvilRabbitAttackGoal") == 0
						&& bunny.countTargetGoals() == 0,
				"Gummy Bunny lost Rabbit hopping, panic, breeding, temptation, avoidance, garden or ordinary non-hostile goals");
		require(helper,
				bunny.isFood(new ItemStack(Items.CARROT))
						&& bunny.isFood(new ItemStack(
								Items.GOLDEN_CARROT))
						&& bunny.isFood(new ItemStack(
								Items.DANDELION))
						&& bunny.isFood(new ItemStack(
								CakeWorldItems
										.SPRINKLE_SEEDS
										.get()))
						&& bunny.ambientSound()
								== SoundEvents.RABBIT_AMBIENT
						&& bunny.hurtSound()
								== SoundEvents.RABBIT_HURT
						&& bunny.deathSound()
								== SoundEvents.RABBIT_DEATH
						&& bunny.jumpSound()
								== SoundEvents.RABBIT_JUMP
						&& bunny.getSoundSource()
								== SoundSource.NEUTRAL
						&& close(bunny.jumpPower(), 0.2D),
				"Gummy Bunny lost vanilla foods, tagged Sprinkle Seeds, sounds, neutral role or resting jump power");
		bunny.startJumping();
		require(helper,
				bunny.lastSound() == SoundEvents.RABBIT_JUMP
						&& close(bunny.getJumpCompletion(
								0.0F), 0.0D),
				"Gummy Bunny lost its ten-tick jump start and audible hop");

		for (int variant = Rabbit.TYPE_BROWN;
				variant <= Rabbit.TYPE_SALT; variant++) {
			bunny.setRabbitType(variant);
			CompoundTag variantData = new CompoundTag();
			bunny.addAdditionalSaveData(variantData);
			restored.readAdditionalSaveData(variantData);
			require(helper,
					variantData.getInt("RabbitType")
									== variant
							&& restored.getRabbitType()
									== variant,
					"Gummy Bunny lost colour variant "
							+ variant
							+ " across RabbitType save/reload");
		}

		BlockPos anchor = helper.absolutePos(
				new BlockPos(3, 3, 3));
		GummyBunny grouped =
				CakeWorldEntities.GUMMY_BUNNY.get()
						.create(helper.getLevel());
		require(helper, grouped != null,
				"Could not create Gummy Bunny group fixture");
		grouped.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ());
		grouped.finalizeSpawn(helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(anchor),
				MobSpawnType.NATURAL,
				new Rabbit.RabbitGroupData(Rabbit.TYPE_GOLD),
				null);
		require(helper,
				grouped.getRabbitType() == Rabbit.TYPE_GOLD,
				"Gummy Bunny lost vanilla same-group climate variant propagation");

		GummyBunnyProbe partner =
				new GummyBunnyProbe(helper.getLevel());
		bunny.setRabbitType(Rabbit.TYPE_WHITE_SPLOTCHED);
		partner.setRabbitType(Rabbit.TYPE_WHITE_SPLOTCHED);
		bunny.seedRandom(1978L);
		GummyBunny child = bunny.getBreedOffspring(
				helper.getLevel(), partner);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities
										.GUMMY_BUNNY.get()
						&& child.getRabbitType()
								== Rabbit.TYPE_WHITE_SPLOTCHED,
				"Gummy Bunny breeding leaked a literal Rabbit or lost same-flavour inheritance");

		BlockPos farmland = anchor.offset(4, 0, 0);
		BlockPos carrots = farmland.above();
		helper.getLevel().setBlock(farmland,
				Blocks.FARMLAND.defaultBlockState(), 3);
		helper.getLevel().setBlock(carrots,
				Blocks.CARROTS.defaultBlockState()
						.setValue(CarrotBlock.AGE, 7), 3);
		GummyBunnyProbe raider =
				new GummyBunnyProbe(helper.getLevel());
		raider.seedRandom(1978L);
		raider.setPos(carrots.getX() + 0.5D,
				carrots.getY(), carrots.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(raider);
		require(helper,
				helper.getLevel().getGameRules()
						.getBoolean(GameRules.RULE_MOBGRIEFING)
						&& raider.runGardenRaidAt(carrots)
						&& helper.getLevel()
								.getBlockState(carrots)
								.is(Blocks.CARROTS)
						&& helper.getLevel()
								.getBlockState(carrots)
								.getValue(CarrotBlock.AGE)
								== 6,
				"Gummy Bunny did not use the genuine mob-griefing-controlled mature-carrot raid");
		CompoundTag raiderData = new CompoundTag();
		raider.addAdditionalSaveData(raiderData);
		GummyBunnyProbe reloadedRaider =
				new GummyBunnyProbe(helper.getLevel());
		reloadedRaider.readAdditionalSaveData(raiderData);
		CompoundTag reloadedRaiderData = new CompoundTag();
		reloadedRaider.addAdditionalSaveData(
				reloadedRaiderData);
		require(helper,
				raiderData.getInt("MoreCarrotTicks") > 0
						&& reloadedRaiderData.getInt(
								"MoreCarrotTicks")
								== raiderData.getInt(
										"MoreCarrotTicks"),
				"Gummy Bunny lost the inherited garden-rest timer across save/reload");

		GummyBunnyProbe ferocious =
				new GummyBunnyProbe(helper.getLevel());
		ferocious.setRabbitType(Rabbit.TYPE_EVIL);
		require(helper,
				close(ferocious.getAttributeValue(
								Attributes.ARMOR), 8.0D)
						&& ferocious.getSoundSource()
								== SoundSource.HOSTILE
						&& ferocious.countGoalsNamed(
								"EvilRabbitAttackGoal") == 1
						&& ferocious.countTargetGoalsNamed(
								"HurtByTargetGoal") == 1
						&& ferocious.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 2
						&& ferocious.getCustomName()
								instanceof TranslatableComponent
										name
						&& "entity.cakeworld.ferocious_gummy_bunny"
								.equals(name.getKey()),
				"Ferocious Gummy Bunny lost exact armour, hostile goals/sound or its original CakeWorld name");
		Pig biteTarget = EntityType.PIG.create(
				helper.getLevel());
		require(helper, biteTarget != null,
				"Could not create Gummy Bunny bite target");
		float biteHealth = biteTarget.getHealth();
		for (Difficulty safeDifficulty :
				new Difficulty[] {
						Difficulty.PEACEFUL,
						Difficulty.EASY,
						Difficulty.NORMAL}) {
			biteTarget.setHealth(biteHealth);
			biteTarget.invulnerableTime = 0;
			biteTarget.removeAllEffects();
			biteTarget.setSecondsOnFire(5);
			biteTarget.fallDistance = 12.0F;
			biteTarget.setDeltaMovement(Vec3.ZERO);
			require(helper,
					ferocious.doHurtTargetForDifficulty(
								biteTarget,
								safeDifficulty)
							&& close(
									biteTarget.getHealth(),
									biteHealth)
							&& !biteTarget.isOnFire()
							&& biteTarget.fallDistance
									== 0.0F
							&& biteTarget.hasEffect(
									CakeWorldEffects
											.FIZZY_FEET.get())
							&& biteTarget.hasEffect(
									MobEffects
											.SLOW_FALLING)
							&& biteTarget.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& biteTarget.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4
							&& biteTarget
									.getDeltaMovement().y
									> 0.0D,
					safeDifficulty
							+ " Ferocious Gummy Bunny bite caused health or indirect peril, or lost elastic rescue");
		}
		biteTarget.setHealth(biteHealth);
		biteTarget.invulnerableTime = 0;
		biteTarget.removeAllEffects();
		biteTarget.setDeltaMovement(Vec3.ZERO);
		boolean hardBite =
				ferocious.doHurtTargetForDifficulty(
						biteTarget, Difficulty.HARD);
		require(helper,
				hardBite
						&& close(biteTarget.getHealth(),
								biteHealth - 8.0D)
						&& biteTarget.getActiveEffects()
								.isEmpty()
						&& biteTarget.getDeltaMovement()
								.horizontalDistanceSqr()
								> 0.0D,
				"Hard Ferocious Gummy Bunny lost the exact unmodified eight-point Rabbit attack: result="
						+ hardBite
						+ ", health="
						+ biteTarget.getHealth()
						+ "/"
						+ biteHealth
						+ ", effects="
						+ biteTarget.getActiveEffects()
						+ ", motion="
						+ biteTarget.getDeltaMovement()
						+ ", invulnerableTime="
						+ biteTarget.invulnerableTime);

		GummyBunny wolfPrey =
				CakeWorldEntities.GUMMY_BUNNY.get()
						.create(helper.getLevel());
		Wolf wolf = EntityType.WOLF.create(
				helper.getLevel());
		require(helper, wolfPrey != null && wolf != null,
				"Could not create Gummy Bunny predator fixtures");
		wolf.setPos(anchor.getX(), anchor.getY(),
				anchor.getZ() + 5.0D);
		wolfPrey.setPos(anchor.getX() + 2.0D,
				anchor.getY(), anchor.getZ() + 5.0D);
		helper.getLevel().addFreshEntity(wolf);
		helper.getLevel().addFreshEntity(wolfPrey);
		require(helper,
				wolfPrey instanceof Rabbit
						&& GummyBunnyPredatorCompatibility
								.assignNearestPrey(wolf)
						&& wolf.getTarget() == wolfPrey,
				"Gummy Bunny lost inherited Fox prey compatibility or the narrow untamed-vanilla-Wolf literal-type repair");

		BlockPos spawnPos = new BlockPos(
				anchor.getX() + 8,
				helper.getLevel().getMaxBuildHeight() - 2,
				anchor.getZ());
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		TagKey<EntityType<?>> powderSnowWalkable =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"powder_snow_walkable_mobs"));
		boolean directSpawnRule =
				GummyBunny.checkGummyBunnySpawnRules(
						CakeWorldEntities.GUMMY_BUNNY.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						spawnPos,
						new Random(1978L));
		boolean registeredSpawnRule =
				SpawnPlacements.checkSpawnRules(
						CakeWorldEntities.GUMMY_BUNNY.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						spawnPos,
						new Random(1979L));
		SpawnPlacements.Type placementType =
				SpawnPlacements.getPlacementType(
						CakeWorldEntities.GUMMY_BUNNY.get());
		Heightmap.Types heightmapType =
				SpawnPlacements.getHeightmapType(
						CakeWorldEntities.GUMMY_BUNNY.get());
		boolean powderSnowRole =
				CakeWorldEntities.GUMMY_BUNNY.get()
						.is(powderSnowWalkable);
		boolean eggPresent =
				CakeWorldItems.GUMMY_BUNNY_SPAWN_EGG
						.isPresent();
		require(helper,
				directSpawnRule
						&& registeredSpawnRule
						&& placementType
								== SpawnPlacements.Type.ON_GROUND
						&& heightmapType
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& powderSnowRole
						&& eggPresent,
				"Gummy Bunny lost edible bright-surface spawning, exact placement, powder-snow or egg roles: direct="
						+ directSpawnRule
						+ ", registered="
						+ registeredSpawnRule
						+ ", below="
						+ helper.getLevel()
								.getBlockState(
										spawnPos.below())
						+ ", rawBrightness="
						+ helper.getLevel().getRawBrightness(
								spawnPos, 0)
						+ ", placement="
						+ placementType
						+ ", heightmap="
						+ heightmapType
						+ ", powderTag="
						+ powderSnowRole
						+ ", egg="
						+ eggPresent);

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome candyPlains = biomes.get(
				CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Gummy Bunny spawning");
		MobSpawnSettings.SpawnerData candySpawn =
				candyPlains.getMobSettings()
						.getMobs(MobCategory.CREATURE)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.GUMMY_BUNNY.get())
						.findFirst().orElse(null);
		require(helper,
				candySpawn != null
						&& candySpawn.getWeight().asInt() == 2
						&& candySpawn.minCount == 2
						&& candySpawn.maxCount == 6
						&& candyPlains.getMobSettings()
								.getMobs(
										MobCategory.CREATURE)
								.unwrap().stream()
								.noneMatch(spawn ->
										spawn.type
												== EntityType
														.RABBIT),
				"Candy Plains lost the exact Meadow 2/2-6 Gummy Bunny population or leaked literal Rabbits");

		MobSpawnSettingsBuilder futureMeadowSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureMeadow =
				new BiomeLoadingEvent(
						new ResourceLocation(CakeWorld.MODID,
								"chocolate_sponge_meadows"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings.EMPTY),
						futureMeadowSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(futureMeadow);
		MobSpawnSettings.SpawnerData futureMeadowSpawn =
				futureMeadowSpawns
						.getSpawner(MobCategory.CREATURE)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.GUMMY_BUNNY.get())
						.findFirst().orElse(null);
		MobSpawnSettingsBuilder futureJungleSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureJungle =
				new BiomeLoadingEvent(
						new ResourceLocation(CakeWorld.MODID,
								"gummy_jungle"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings.EMPTY),
						futureJungleSpawns);
		CakeWorldCreatureSpawns.onBiomeLoading(futureJungle);
		MobSpawnSettings.SpawnerData futureJungleSpawn =
				futureJungleSpawns
						.getSpawner(MobCategory.CREATURE)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.GUMMY_BUNNY.get())
						.findFirst().orElse(null);
		require(helper,
				futureMeadowSpawn != null
						&& futureMeadowSpawn.getWeight()
								.asInt() == 2
						&& futureMeadowSpawn.minCount == 2
						&& futureMeadowSpawn.maxCount == 6
						&& futureJungleSpawn != null
						&& futureJungleSpawn.getWeight()
								.asInt() == 4
						&& futureJungleSpawn.minCount == 2
						&& futureJungleSpawn.maxCount == 3
						&& futureMeadowSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType
														.RABBIT)
						&& futureJungleSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType
														.RABBIT),
				"Future Chocolate Sponge Meadows or Gummy Jungle lost its staged Gummy Bunny profile");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType.RABBIT
											|| spawn.type
													== CakeWorldEntities
															.GUMMY_BUNNY
															.get()),
					"Non-meadow current biome leaked Rabbit/Gummy Bunny spawning: "
							+ biomeId);
		}

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2044"),
						"CakeWorldGummyBunnyRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.GUMMY_BUNNY.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:rabbit");

		raider.discard();
		wolf.discard();
		wolfPrey.discard();
		helper.getLevel().setBlock(farmland,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(carrots,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void gingerbreadStompersKeepRaidRoarAndSafeObstacles(
			GameTestHelper helper) {
		GingerbreadStomperProbe stomper =
				new GingerbreadStomperProbe(
						helper.getLevel());
		require(helper,
				stomper instanceof Ravager
						&& stomper.getType()
								== CakeWorldEntities
										.GINGERBREAD_STOMPER
										.get()
						&& stomper.getType().getCategory()
								== MobCategory.MONSTER
						&& close(stomper.getMaxHealth(),
								100.0D)
						&& close(stomper
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.3D)
						&& close(stomper
								.getAttributeValue(
										Attributes
												.KNOCKBACK_RESISTANCE),
								0.75D)
						&& close(stomper
								.getAttributeValue(
										Attributes
												.ATTACK_DAMAGE),
								12.0D)
						&& close(stomper
								.getAttributeValue(
										Attributes
												.ATTACK_KNOCKBACK),
								1.5D)
						&& close(stomper
								.getAttributeValue(
										Attributes
												.FOLLOW_RANGE),
								32.0D)
						&& close(stomper.getDimensions(
								Pose.STANDING).width,
								1.95D)
						&& close(stomper.getDimensions(
								Pose.STANDING).height,
								2.2D)
						&& stomper.getType()
								.clientTrackingRange()
								== 10
						&& close(stomper.maxUpStep, 1.0D)
						&& stomper.getExperienceValue()
								== 20
						&& close(stomper
								.getPassengersRidingOffset(),
								2.1D)
						&& stomper.getMaxHeadYRot() == 45
						&& stomper.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/gingerbread_stomper")),
				"Gingerbread Stomper lost the exact Ravager body, attributes, tracking, rider offset, XP or loot roles");

		require(helper,
				stomper.countGoalsNamed("FloatGoal") == 1
						&& stomper.countGoalsNamed(
								"RavagerMeleeAttackGoal")
								== 1
						&& stomper.countGoalsNamed(
								"WaterAvoidingRandomStrollGoal")
								== 1
						&& stomper.countGoalsNamed(
								"LookAtPlayerGoal") == 2
						&& stomper.countTargetGoalsNamed(
								"HurtByTargetGoal") == 1
						&& stomper.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 3
						&& "RavagerNavigation".equals(
								stomper.getNavigation()
										.getClass()
										.getSimpleName())
						&& !stomper.canBeLeader(),
				"Gingerbread Stomper lost Ravager movement, melee, look, player, adult-Villager, Golem or no-leader behavior");
		stomper.runStepSound();
		require(helper,
				stomper.ambientSound()
								== SoundEvents.RAVAGER_AMBIENT
						&& stomper.hurtSound()
								== SoundEvents.RAVAGER_HURT
						&& stomper.deathSound()
								== SoundEvents.RAVAGER_DEATH
						&& stomper.getCelebrateSound()
								== SoundEvents.RAVAGER_CELEBRATE
						&& stomper.lastSound()
								== SoundEvents.RAVAGER_STEP,
				"Gingerbread Stomper lost Ravager ambient, hurt, death, celebration or step cues");

		GingerbreadStomperProbe animation =
				new GingerbreadStomperProbe(
						helper.getLevel());
		CompoundTag animationState =
				new CompoundTag();
		animationState.putInt("AttackTick", 7);
		animationState.putInt("StunTick", 13);
		animationState.putInt("RoarTick", 17);
		animation.readAdditionalSaveData(animationState);
		require(helper,
				animation.getAttackTick() == 7
						&& animation.getStunnedTick() == 13
						&& animation.getRoarTick() == 17,
				"Gingerbread Stomper did not restore persisted attack, stun and roar animation state");
		animation.handleEntityEvent((byte)4);
		require(helper,
				animation.getAttackTick() == 10
						&& animation.lastSound()
								== SoundEvents.RAVAGER_ATTACK,
				"Gingerbread Stomper lost its visible ten-tick attack cue");

		IronGolem shieldBearer =
				EntityType.IRON_GOLEM.create(
						helper.getLevel());
		require(helper, shieldBearer != null,
				"Could not create Gingerbread Stomper shield fixture");
		shieldBearer.setPos(2.0D, 3.0D, 2.0D);
		stomper.setPos(0.0D, 3.0D, 2.0D);
		stomper.seedRandom(1978L);
		for (int attempt = 0;
				attempt < 32
						&& stomper.getStunnedTick() == 0;
				attempt++) {
			stomper.runBlockedByShield(shieldBearer);
		}
		require(helper,
				stomper.getStunnedTick() == 40
						&& shieldBearer.hurtMarked
						&& stomper.lastSound()
								== SoundEvents.RAVAGER_STUNNED,
				"Gingerbread Stomper lost the Ravager shield-stun/knockback contract");

		BlockPos anchor = helper.absolutePos(
				new BlockPos(4, 3, 4));
		GingerbreadStomperProbe safeRoarer =
				new GingerbreadStomperProbe(
						helper.getLevel());
		Pig safeRoarTarget =
				EntityType.PIG.create(helper.getLevel());
		BiscuitBandit safeIllager =
				CakeWorldEntities.BISCUIT_BANDIT.get()
						.create(helper.getLevel());
		require(helper,
				safeRoarTarget != null
						&& safeIllager != null,
				"Could not create Gingerbread Stomper safe-roar fixtures");
		safeRoarer.setPos(anchor.getX() + 0.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		safeRoarer.setNoAi(true);
		safeRoarer.setNoGravity(true);
		safeRoarTarget.setPos(
				safeRoarer.getX() + 2.0D,
				safeRoarer.getY(), safeRoarer.getZ());
		safeRoarTarget.setNoAi(true);
		safeRoarTarget.setNoGravity(true);
		safeIllager.setPos(safeRoarer.getX(),
				safeRoarer.getY(),
				safeRoarer.getZ() + 2.0D);
		safeIllager.setNoAi(true);
		safeIllager.setNoGravity(true);
		helper.getLevel().addFreshEntity(safeRoarer);
		helper.getLevel().addFreshEntity(safeRoarTarget);
		helper.getLevel().addFreshEntity(safeIllager);
		safeRoarer.handleEntityEvent((byte)39);
		for (int tick = 0; tick < 50; tick++) {
			safeRoarer.aiStep();
		}
		require(helper,
				safeRoarer.getRoarTick() == 10
						&& close(safeRoarTarget
								.getHealth(),
								safeRoarTarget
										.getMaxHealth())
						&& safeRoarTarget.hasEffect(
								MobEffects
										.MOVEMENT_SLOWDOWN)
						&& safeRoarTarget.hasEffect(
								MobEffects.SLOW_FALLING)
						&& safeRoarTarget.hasEffect(
								MobEffects
										.FIRE_RESISTANCE)
						&& safeRoarTarget.getEffect(
								MobEffects
										.DAMAGE_RESISTANCE)
								.getAmplifier() == 4
						&& safeRoarTarget
								.getDeltaMovement()
								.lengthSqr() > 0.01D
						&& close(safeIllager.getHealth(),
								safeIllager
										.getMaxHealth())
						&& !safeIllager.hasEffect(
								MobEffects
										.MOVEMENT_SLOWDOWN)
						&& safeIllager
								.getDeltaMovement()
								.lengthSqr() > 0.01D,
				"Normal Gingerbread Stomper roar caused health damage, lost its rescue envelope/strong displacement, or treated its Illager rider as prey");

		for (Difficulty difficulty : List.of(
				Difficulty.PEACEFUL,
				Difficulty.EASY,
				Difficulty.NORMAL,
				Difficulty.HARD)) {
			LivingHurtEvent policy =
					new LivingHurtEvent(
							safeRoarTarget,
							DamageSource.mobAttack(
									stomper),
							12.0F);
			GingerbreadStomperDamageSafety
					.applyForDifficulty(
							policy, difficulty);
			require(helper,
					policy.isCanceled()
							== (difficulty
									!= Difficulty.HARD),
					difficulty
							+ " Gingerbread Stomper hurt policy crossed the Hard-only health-damage boundary");

			EntityMobGriefingEvent grief =
					new EntityMobGriefingEvent(
							stomper);
			GingerbreadStomperGriefSafety
					.applyForDifficulty(
							grief, difficulty);
			require(helper,
					grief.getResult()
							== (difficulty
									== Difficulty.HARD
											? Event.Result.DEFAULT
											: Event.Result.DENY),
					difficulty
							+ " Gingerbread Stomper grief policy crossed the Hard-only gamerule boundary");
		}

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		boolean originalMobGriefing =
				helper.getLevel().getGameRules()
						.getBoolean(
								GameRules
										.RULE_MOBGRIEFING);
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			GingerbreadStomperProbe safeMelee =
					new GingerbreadStomperProbe(
							helper.getLevel());
			Pig safeMeleeTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, safeMeleeTarget != null,
					"Could not create safe Gingerbread Stomper melee target");
			safeMelee.setPos(anchor.getX() + 8.0D,
					anchor.getY(), anchor.getZ());
			safeMeleeTarget.setPos(
					safeMelee.getX() + 1.5D,
					safeMelee.getY(),
					safeMelee.getZ());
			helper.getLevel().addFreshEntity(
					safeMelee);
			helper.getLevel().addFreshEntity(
					safeMeleeTarget);
			safeMeleeTarget.setSecondsOnFire(5);
			safeMeleeTarget.fallDistance = 8.0F;
			safeMelee.doHurtTarget(
					safeMeleeTarget);
			require(helper,
					close(safeMeleeTarget.getHealth(),
							safeMeleeTarget
									.getMaxHealth())
							&& safeMelee
									.getAttackTick()
									== 10
							&& !safeMeleeTarget
									.isOnFire()
							&& close(safeMeleeTarget
									.fallDistance,
									0.0D)
							&& safeMeleeTarget.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& safeMeleeTarget.hasEffect(
									MobEffects
											.SLOW_FALLING)
							&& safeMeleeTarget.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& safeMeleeTarget.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4,
					"Normal Gingerbread Stomper melee caused damage or lost its attack cue and rescue envelope");

			BlockPos safeLeaves = anchor.offset(
					12, 0, 0);
			GingerbreadStomperProbe safeObstacle =
					new GingerbreadStomperProbe(
							helper.getLevel());
			safeObstacle.setPos(
					safeLeaves.getX() + 0.5D,
					safeLeaves.getY(),
					safeLeaves.getZ() + 0.5D);
			safeObstacle.setNoAi(true);
			safeObstacle.horizontalCollision = true;
			safeObstacle.setOnGround(true);
			safeObstacle.setDeltaMovement(
					Vec3.ZERO);
			helper.getLevel().setBlock(safeLeaves,
					Blocks.OAK_LEAVES
							.defaultBlockState(),
					3);
			helper.getLevel().addFreshEntity(
					safeObstacle);
			safeObstacle.aiStep();
			require(helper,
					helper.getLevel().getBlockState(
							safeLeaves).is(
									Blocks.OAK_LEAVES)
							&& safeObstacle
									.getDeltaMovement().y
									> 0.0D,
					"Normal Gingerbread Stomper destroyed a possession or failed its safe obstacle hop");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(true,
							helper.getLevel()
									.getServer());
			GingerbreadStomperProbe hardMelee =
					new GingerbreadStomperProbe(
							helper.getLevel());
			IronGolem hardMeleeTarget =
					EntityType.IRON_GOLEM.create(
							helper.getLevel());
			require(helper, hardMeleeTarget != null,
					"Could not create Hard Gingerbread Stomper melee target");
			hardMelee.setPos(anchor.getX() + 18.0D,
					anchor.getY(), anchor.getZ());
			hardMeleeTarget.setPos(
					hardMelee.getX() + 1.5D,
					hardMelee.getY(),
					hardMelee.getZ());
			helper.getLevel().addFreshEntity(
					hardMelee);
			helper.getLevel().addFreshEntity(
					hardMeleeTarget);
			hardMelee.doHurtTarget(
					hardMeleeTarget);
			require(helper,
					close(hardMeleeTarget.getHealth(),
							88.0D),
					"Hard Gingerbread Stomper melee did not retain exact 12-point damage: "
							+ hardMeleeTarget
									.getHealth());

			GingerbreadStomperProbe hardRoarer =
					new GingerbreadStomperProbe(
							helper.getLevel());
			IronGolem hardRoarTarget =
					EntityType.IRON_GOLEM.create(
							helper.getLevel());
			require(helper, hardRoarTarget != null,
					"Could not create Hard Gingerbread Stomper roar target");
			hardRoarer.setPos(
					anchor.getX() + 24.0D,
					anchor.getY(), anchor.getZ());
			hardRoarer.setNoAi(true);
			hardRoarer.setNoGravity(true);
			hardRoarTarget.setPos(
					hardRoarer.getX() + 2.0D,
					hardRoarer.getY(),
					hardRoarer.getZ());
			hardRoarTarget.setNoAi(true);
			hardRoarTarget.setNoGravity(true);
			helper.getLevel().addFreshEntity(
					hardRoarer);
			helper.getLevel().addFreshEntity(
					hardRoarTarget);
			hardRoarer.handleEntityEvent((byte)39);
			for (int tick = 0; tick < 50;
					tick++) {
				hardRoarer.aiStep();
			}
			require(helper,
					close(hardRoarTarget.getHealth(),
							94.0D)
							&& hardRoarTarget
									.getDeltaMovement()
									.lengthSqr()
									> 0.01D,
					"Hard Gingerbread Stomper roar did not retain exact six-point damage and strong displacement: "
							+ hardRoarTarget
									.getHealth());

			BlockPos hardLeaves = anchor.offset(
					30, 0, 0);
			GingerbreadStomperProbe hardObstacle =
					new GingerbreadStomperProbe(
							helper.getLevel());
			hardObstacle.setPos(
					hardLeaves.getX() + 0.5D,
					hardLeaves.getY(),
					hardLeaves.getZ() + 0.5D);
			hardObstacle.setNoAi(true);
			hardObstacle.horizontalCollision = true;
			hardObstacle.setOnGround(true);
			helper.getLevel().setBlock(hardLeaves,
					Blocks.OAK_LEAVES
							.defaultBlockState(),
					3);
			helper.getLevel().addFreshEntity(
					hardObstacle);
			hardObstacle.aiStep();
			require(helper,
					helper.getLevel().getBlockState(
							hardLeaves).isAir(),
					"Hard Gingerbread Stomper did not retain mobGriefing-controlled leaf destruction");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			GingerbreadStomperProbe peaceful =
					new GingerbreadStomperProbe(
							helper.getLevel());
			peaceful.setPos(anchor.getX() + 36.0D,
					anchor.getY(), anchor.getZ());
			helper.getLevel().addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Gingerbread Stomper did not retain vanilla Monster removal");
		} finally {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(originalMobGriefing,
							helper.getLevel()
									.getServer());
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		SpawnPlacements.Type placementType =
				SpawnPlacements.getPlacementType(
						CakeWorldEntities
								.GINGERBREAD_STOMPER
								.get());
		Heightmap.Types heightmapType =
				SpawnPlacements.getHeightmapType(
						CakeWorldEntities
								.GINGERBREAD_STOMPER
								.get());
		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.RAVAGER
											|| spawn.type
													== CakeWorldEntities
															.GINGERBREAD_STOMPER
															.get()),
					"Open-biome Ravager/Gingerbread Stomper spawning leaked into "
							+ biomeId);
		}
		require(helper,
				placementType
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& heightmapType
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.GINGERBREAD_STOMPER_SPAWN_EGG
								.isPresent()
						&& CakeWorldEntities
								.GINGERBREAD_STOMPER
								.get().is(
										net.minecraft.tags
												.EntityTypeTags
												.RAIDERS)
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.GINGERBREAD_STOMPER
												.get())
								== SoundEvents
										.PARROT_IMITATE_RAVAGER,
				"Gingerbread Stomper lost exact placement metadata, Raiders tag, egg or Lollipop Lorikeet mimic role");

		try {
			Field raidTypeField =
					Raid.RaiderType.class.getDeclaredField(
							"entityType");
			Field wavesField =
					Raid.RaiderType.class.getDeclaredField(
							"spawnsPerWaveBeforeBonus");
			raidTypeField.setAccessible(true);
			wavesField.setAccessible(true);
			require(helper,
					raidTypeField.get(
							Raid.RaiderType.RAVAGER)
								== EntityType.RAVAGER
							&& Arrays.equals(
									(int[])wavesField.get(
											Raid.RaiderType
													.RAVAGER),
									new int[] {0, 0, 0,
											1, 0, 1,
											0, 2}),
					"Gingerbread Stomper lost the literal vanilla Ravager raid source or exact wave counts");
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not inspect the vanilla Ravager raid source",
					exception);
		}

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(
						helper, anchor, 256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Ravager conversion");
		Ravager literalRavager =
				EntityType.RAVAGER.create(
						helper.getLevel());
		Pillager literalRider =
				EntityType.PILLAGER.create(
						helper.getLevel());
		require(helper,
				literalRavager != null
						&& literalRider != null,
				"Could not create literal Ravager raid-mount fixtures");
		literalRavager.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literalRavager.setNoAi(true);
		literalRavager.setHealth(83.0F);
		literalRavager.setCustomName(
				new TextComponent(
						"Grand Gingerbread Stomper"));
		literalRavager.setPersistenceRequired();
		CompoundTag literalAnimation =
				new CompoundTag();
		literalAnimation.putInt("AttackTick", 7);
		literalAnimation.putInt("StunTick", 13);
		literalAnimation.putInt("RoarTick", 17);
		literalRavager.readAdditionalSaveData(
				literalAnimation);
		// The animation-only read intentionally exercises Ravager's
		// persisted counters; restore Mob flags that an absent NBT key
		// correctly resets to its vanilla default.
		literalRavager.setNoAi(true);
		literalRavager.setPersistenceRequired();
		helper.getLevel().addFreshEntity(
				literalRavager);
		literalRider.setPos(
				literalRavager.getX(),
				literalRavager.getY(),
				literalRavager.getZ());
		literalRider.setNoAi(true);
		literalRider.setCustomName(
				new TextComponent(
						"Stomper Biscuit Rider"));
		helper.getLevel().addFreshEntity(
				literalRider);
		Raid transferRaid = new Raid(197845,
				helper.getLevel(), cakeWorldPos);
		transferRaid.joinRaid(5, literalRavager,
				null, true);
		transferRaid.joinRaid(5, literalRider,
				null, true);
		literalRider.startRiding(
				literalRavager, true);
		GingerbreadStomper replacement =
				CakeWorldRavagerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literalRavager);
		require(helper,
				literalRavager.isRemoved()
						&& replacement != null
						&& replacement.isNoAi()
						&& replacement
								.isPersistenceRequired()
						&& close(replacement.getHealth(),
								83.0D)
						&& "Grand Gingerbread Stomper"
								.equals(replacement
										.getName()
										.getString())
						&& replacement
								.getAttackTick() == 7
						&& replacement
								.getStunnedTick() == 13
						&& replacement
								.getRoarTick() == 17
						&& replacement
								.getCurrentRaid()
								== transferRaid
						&& replacement.getWave() == 5
						&& literalRider.getVehicle()
								== replacement
						&& replacement.getPassengers()
								.contains(
										literalRider)
						&& transferRaid
								.getTotalRaidersAlive()
								== 2,
				"Fresh literal Ravager conversion lost NBT, animation, raid, wave or rider state: oldRemoved="
						+ literalRavager.isRemoved()
						+ ", replacement="
						+ replacement
						+ ", noAi="
						+ (replacement != null
								&& replacement.isNoAi())
						+ ", persistence="
						+ (replacement != null
								&& replacement
										.isPersistenceRequired())
						+ ", health="
						+ (replacement == null ? -1.0F
								: replacement
										.getHealth())
						+ ", name="
						+ (replacement == null ? "<null>"
								: replacement.getName()
										.getString())
						+ ", animation="
						+ (replacement == null ? "<null>"
								: replacement
										.getAttackTick()
										+ "/"
										+ replacement
												.getStunnedTick()
										+ "/"
										+ replacement
												.getRoarTick())
						+ ", raid="
						+ (replacement == null ? "<null>"
								: replacement
										.getCurrentRaid())
						+ ", wave="
						+ (replacement == null ? -1
								: replacement
										.getWave())
						+ ", riderVehicle="
						+ literalRider.getVehicle()
						+ ", passenger="
						+ (replacement != null
								&& replacement
										.getPassengers()
										.contains(
												literalRider))
						+ ", raidAlive="
						+ transferRaid
								.getTotalRaidersAlive());
		BiscuitBandit riderReplacement =
				CakeWorldPillagerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literalRider);
		require(helper,
				literalRider.isRemoved()
						&& riderReplacement != null
						&& riderReplacement
								.getCurrentRaid()
								== transferRaid
						&& riderReplacement
								.getWave() == 5
						&& riderReplacement
								.getVehicle()
								== replacement
						&& replacement.getPassengers()
								.contains(
										riderReplacement)
						&& transferRaid
								.getTotalRaidersAlive()
								== 2
						&& "Stomper Biscuit Rider"
								.equals(riderReplacement
										.getName()
										.getString()),
				"Mounted raid conversion lost the Biscuit rider's raid, wave, mount or NBT state");

		ServerPlayer advancementPlayer =
				new ServerPlayer(
						helper.getLevel()
								.getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2045"),
								"CakeWorldStomperRoleTest"));
		GingerbreadStomper roleVictim =
				CakeWorldEntities.GINGERBREAD_STOMPER
						.get().create(
								helper.getLevel());
		require(helper, roleVictim != null,
				"Could not create Gingerbread Stomper advancement fixture");
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(roleVictim,
						DamageSource.playerAttack(
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:ravager");

		safeRoarer.discard();
		safeRoarTarget.discard();
		safeIllager.discard();
		shieldBearer.discard();
		replacement.discard();
		riderReplacement.discard();
		roleVictim.discard();
		helper.getLevel().setBlock(
				anchor.offset(12, 0, 0),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				anchor.offset(30, 0, 0),
				Blocks.AIR.defaultBlockState(), 3);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void sherbetSalmonKeepSchoolsBucketsAndCoolWater(
			GameTestHelper helper) {
		SherbetSalmonProbe leader =
				new SherbetSalmonProbe(
						helper.getLevel());
		SherbetSalmonProbe restored =
				new SherbetSalmonProbe(
						helper.getLevel());
		int experience = leader.getExperienceValue();
		require(helper,
				leader instanceof Salmon
						&& leader.getType()
								== CakeWorldEntities
										.SHERBET_SALMON
										.get()
						&& leader.getType().getCategory()
								== MobCategory.WATER_AMBIENT
						&& close(leader.getMaxHealth(), 3.0D)
						&& close(leader.getDimensions(
								Pose.STANDING).width,
								0.7D)
						&& close(leader.getDimensions(
								Pose.STANDING).height,
								0.4D)
						&& leader.getType()
								.clientTrackingRange() == 4
						&& leader.getMaxSchoolSize() == 5
						&& leader
								.getMaxSpawnClusterSize() == 5
						&& experience >= 1
						&& experience <= 3
						&& leader.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/sherbet_salmon")),
				"Sherbet Salmon lost the exact Salmon body, school/cluster, tracking, XP or loot roles");
		require(helper,
				leader.countGoalsNamed("PanicGoal") == 1
						&& leader.countGoalsNamed(
								"AvoidEntityGoal") == 1
						&& leader.countGoalsNamed(
								"FishSwimGoal") == 1
						&& leader.countGoalsNamed(
								"FollowFlockLeaderGoal")
								== 1
						&& "WaterBoundPathNavigation"
								.equals(leader.getNavigation()
										.getClass()
										.getSimpleName())
						&& leader.canBreatheUnderwater()
						&& leader.getMobType()
								== MobType.WATER
						&& !leader.isPushedByFluid()
						&& !leader.canBeLeashedRole()
						&& leader.getAmbientSoundInterval()
								== 120,
				"Sherbet Salmon lost panic, avoidance, random/follower swimming, water navigation, air or no-leash roles");
		require(helper,
				leader.ambientSound()
								== SoundEvents.SALMON_AMBIENT
						&& leader.hurtSound()
								== SoundEvents.SALMON_HURT
						&& leader.deathSound()
								== SoundEvents.SALMON_DEATH
						&& leader.flopSound()
								== SoundEvents.SALMON_FLOP
						&& leader.swimSound()
								== SoundEvents.FISH_SWIM
						&& leader.getPickupSound()
								== SoundEvents.BUCKET_FILL_FISH,
				"Sherbet Salmon lost exact ambient, hurt, death, flop, swim or pickup sounds");

		SherbetSalmonProbe followerOne =
				new SherbetSalmonProbe(helper.getLevel());
		SherbetSalmonProbe followerTwo =
				new SherbetSalmonProbe(helper.getLevel());
		SherbetSalmonProbe followerThree =
				new SherbetSalmonProbe(helper.getLevel());
		SherbetSalmonProbe followerFour =
				new SherbetSalmonProbe(helper.getLevel());
		SherbetSalmonProbe overflow =
				new SherbetSalmonProbe(helper.getLevel());
		leader.setPos(0.0D, 3.0D, 0.0D);
		followerOne.setPos(1.0D, 3.0D, 0.0D);
		followerTwo.setPos(2.0D, 3.0D, 0.0D);
		followerThree.setPos(3.0D, 3.0D, 0.0D);
		followerFour.setPos(4.0D, 3.0D, 0.0D);
		overflow.setPos(5.0D, 3.0D, 0.0D);
		leader.addFollowers(java.util.stream.Stream.of(
				followerOne, followerTwo,
				followerThree, followerFour, overflow));
		require(helper,
				followerOne.isFollower()
						&& followerTwo.isFollower()
						&& followerThree.isFollower()
						&& followerFour.isFollower()
						&& !overflow.isFollower()
						&& followerOne.inRangeOfLeader()
						&& !followerOne
								.canRandomSwimRole()
						&& leader.canRandomSwimRole()
						&& !leader.canBeFollowed(),
				"Sherbet Salmon lost exact five-fish school capacity, following range or leader-only random swimming");
		followerOne.stopFollowing();
		require(helper,
				!followerOne.isFollower()
						&& leader.canBeFollowed(),
				"Sherbet Salmon did not reopen a school place after a follower left");
		followerOne.startFollowing(leader);

		leader.setFromBucket(true);
		leader.setCustomName(
				new TextComponent("Raspberry Ripple"));
		CompoundTag saved =
				leader.saveWithoutId(new CompoundTag());
		restored.load(saved);
		require(helper,
				restored.fromBucket()
						&& restored
								.requiresCustomPersistence()
						&& !restored.removeWhenFarAway(
								4096.0D)
						&& restored.hasCustomName()
						&& "Raspberry Ripple".equals(
								restored.getName()
										.getString()),
				"Sherbet Salmon lost from-bucket persistence or custom name across NBT");

		BlockPos anchor = helper.absolutePos(
				new BlockPos(3, 3, 3));
		BlockPos flopPos = new BlockPos(anchor.getX(),
				helper.getLevel().getMaxBuildHeight() - 2,
				anchor.getZ());
		helper.getLevel().setBlock(flopPos,
				Blocks.AIR.defaultBlockState(), 3);
		SherbetSalmonProbe flopping =
				new SherbetSalmonProbe(helper.getLevel());
		flopping.setPos(flopPos.getX() + 0.5D,
				flopPos.getY(), flopPos.getZ() + 0.5D);
		flopping.setOnGround(true);
		flopping.verticalCollision = true;
		flopping.setDeltaMovement(Vec3.ZERO);
		flopping.aiStep();
		require(helper,
				flopping.getDeltaMovement().y > 0.3D
						&& flopping.lastSound()
								== SoundEvents.SALMON_FLOP,
				"Sherbet Salmon lost its inherited land flop and cue"
						+ " (motion="
						+ flopping.getDeltaMovement()
						+ ", sound="
						+ flopping.lastSound() + ")");

		BlockPos lemonadePos =
				new BlockPos(anchor.getX() + 8,
						helper.getLevel()
								.getSeaLevel() - 5,
						anchor.getZ());
		for (int y = -1; y <= 1; y++) {
			helper.getLevel().setBlock(
					lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK.get()
							.defaultBlockState(), 3);
		}
		boolean vanillaRule =
				WaterAnimal
						.checkSurfaceWaterAnimalSpawnRules(
								CakeWorldEntities
										.SHERBET_SALMON
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								lemonadePos,
								new Random(1978L));
		boolean sherbetRule =
				SherbetSalmon
						.checkSherbetSalmonSpawnRules(
								CakeWorldEntities
										.SHERBET_SALMON
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								lemonadePos,
								new Random(1978L));
		require(helper,
				!vanillaRule
						&& sherbetRule
						&& SpawnPlacements
								.checkSpawnRules(
										CakeWorldEntities
												.SHERBET_SALMON
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										lemonadePos,
										new Random(1979L))
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.SHERBET_SALMON
												.get())
								== SpawnPlacements.Type
										.IN_WATER
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.SHERBET_SALMON
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Sherbet Salmon lost Lemonade-compatible surface spawning or exact placement metadata");

		SherbetSalmon captureFish =
				CakeWorldEntities.SHERBET_SALMON.get()
						.create(helper.getLevel());
		require(helper, captureFish != null,
				"Could not create Sherbet Salmon bucket fixture");
		captureFish.setCustomName(
				new TextComponent("Lemon Swirl"));
		captureFish.setPos(
				lemonadePos.getX() + 0.5D,
				lemonadePos.getY(),
				lemonadePos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(captureFish);
		ServerPlayer bucketPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2046"),
						"CakeWorldSherbetSalmonBucketTest"));
		bucketPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.WATER_BUCKET));
		InteractionResult captureResult =
				bucketPlayer.interactOn(captureFish,
						InteractionHand.MAIN_HAND);
		ItemStack bucket = bucketPlayer.getItemInHand(
				InteractionHand.MAIN_HAND);
		require(helper,
				captureResult.consumesAction()
						&& bucket.is(
								CakeWorldItems
										.SHERBET_SALMON_BUCKET
										.get())
						&& bucket.hasCustomHoverName()
						&& captureFish.isRemoved(),
				"Sherbet Salmon did not capture into its dedicated named Lemonade bucket");
		AABB releaseArea =
				new AABB(lemonadePos).inflate(2.0D);
		helper.getLevel().getEntitiesOfClass(
				SherbetSalmon.class, releaseArea)
				.forEach(SherbetSalmon::discard);
		((MobBucketItem)bucket.getItem())
				.checkExtraContent(null,
						helper.getLevel(), bucket,
						lemonadePos);
		List<SherbetSalmon> released =
				helper.getLevel().getEntitiesOfClass(
						SherbetSalmon.class,
						releaseArea);
		require(helper,
				released.size() == 1
						&& released.get(0).fromBucket()
						&& released.get(0).hasCustomName()
						&& "Lemon Swirl".equals(
								released.get(0)
										.getName()
										.getString()),
				"Sherbet Salmon bucket released the wrong entity or lost from-bucket/name data");
		requireCriterion(helper, bucketPlayer,
				"minecraft:husbandry/tactical_fishing",
				"salmon_bucket");

		SodaCod cod =
				CakeWorldEntities.SODA_COD.get()
						.create(helper.getLevel());
		require(helper, cod != null,
				"Could not create Soda Cod bucket-role maintenance fixture");
		cod.setPos(lemonadePos.getX() + 0.5D,
				lemonadePos.getY(),
				lemonadePos.getZ() + 1.5D);
		helper.getLevel().addFreshEntity(cod);
		ServerPlayer codPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2008"),
						"CakeWorldSodaCodBucketRoleTest"));
		codPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.WATER_BUCKET));
		require(helper,
				codPlayer.interactOn(cod,
						InteractionHand.MAIN_HAND)
								.consumesAction()
						&& codPlayer.getItemInHand(
								InteractionHand.MAIN_HAND)
								.is(CakeWorldItems
										.SODA_COD_BUCKET
										.get()),
				"Soda Cod bucket-role maintenance did not traverse the real capture path");
		requireCriterion(helper, codPlayer,
				"minecraft:husbandry/tactical_fishing",
				"cod_bucket");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome sodaOcean = biomes.get(
				CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Soda Ocean Sherbet Salmon spawning");
		MobSpawnSettings.SpawnerData sodaSpawn =
				sodaOcean.getMobSettings()
						.getMobs(
								MobCategory.WATER_AMBIENT)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.SHERBET_SALMON
										.get())
						.findFirst().orElse(null);
		require(helper,
				sodaSpawn != null
						&& sodaSpawn.getWeight()
								.asInt() == 15
						&& sodaSpawn.minCount == 1
						&& sodaSpawn.maxCount == 5
						&& sodaOcean.getMobSettings()
								.getMobs(
										MobCategory
												.WATER_AMBIENT)
								.unwrap().stream()
								.noneMatch(spawn ->
										spawn.type
												== EntityType
														.SALMON),
				"Soda Ocean lost the exact cold-ocean 15/1-5 Sherbet Salmon population or leaked literal Salmon");

		MobSpawnSettingsBuilder futureTundraSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureTundra =
				new BiomeLoadingEvent(
						new ResourceLocation(CakeWorld.MODID,
								"ice_cream_tundra"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings.EMPTY),
						futureTundraSpawns);
		CakeWorldCreatureSpawns
				.onBiomeLoading(futureTundra);
		MobSpawnSettings.SpawnerData futureSpawn =
				futureTundraSpawns
						.getSpawner(
								MobCategory.WATER_AMBIENT)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.SHERBET_SALMON
										.get())
						.findFirst().orElse(null);
		require(helper,
				futureSpawn != null
						&& futureSpawn.getWeight()
								.asInt() == 15
						&& futureSpawn.minCount == 1
						&& futureSpawn.maxCount == 5
						&& futureTundraSpawns
								.getSpawner(
										MobCategory
												.WATER_AMBIENT)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType
														.SALMON),
				"Future Ice-Cream Tundra lost its exact frozen-lake Sherbet Salmon profile");

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.WATER_AMBIENT)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SALMON
											|| spawn.type
													== CakeWorldEntities
															.SHERBET_SALMON
															.get()),
					"Non-water/cold current biome leaked Salmon spawning: "
							+ biomeId);
		}

		TagKey<EntityType<?>> axolotlPrey =
				TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"axolotl_hunt_targets"));
		Advancement killAll = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"adventure/kill_all_mobs"));
		Advancement bredAll = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"husbandry/bred_all_animals"));
		require(helper,
				CakeWorldEntities.SHERBET_SALMON.get()
								.is(axolotlPrey)
						&& CakeWorldEntities.SODA_COD.get()
								.is(axolotlPrey)
						&& CakeWorldEntities.FIZZBALL_FISH
								.get().is(axolotlPrey)
						&& CakeWorldEntities.GLOW_JELLY.get()
								.is(axolotlPrey)
						&& CakeWorldItems
								.SHERBET_SALMON_SPAWN_EGG
								.isPresent()
						&& killAll != null
						&& bredAll != null
						&& !killAll.getCriteria()
								.containsKey(
										"minecraft:salmon")
						&& !bredAll.getCriteria()
								.containsKey(
										"minecraft:salmon")
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.SHERBET_SALMON
												.get())
								== null,
				"Sherbet Salmon lost Axolotl-prey/egg roles, dropped an existing CakeWorld prey role, or fabricated combat, breeding or mimic progression");

		released.forEach(SherbetSalmon::discard);
		helper.getLevel().setBlock(
				lemonadePos.below(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				lemonadePos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				lemonadePos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void candyflossSheepKeepDyesFleeceAndEdibleGrazing(
			GameTestHelper helper) {
		CandyflossSheepProbe sheep =
				new CandyflossSheepProbe(helper.getLevel());
		int experience = sheep.getExperienceValue();
		require(helper,
				sheep instanceof Sheep
						&& sheep.getType()
								== CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get()
						&& sheep.getType().getCategory()
								== MobCategory.CREATURE
						&& close(sheep.getMaxHealth(), 8.0D)
						&& close(sheep.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.23D)
						&& sheep.getAttribute(
								Attributes.ATTACK_DAMAGE)
								== null
						&& close(sheep.getDimensions(
								Pose.STANDING).width,
								0.9D)
						&& close(sheep.getDimensions(
								Pose.STANDING).height,
								1.3D)
						&& close(sheep.standingEyeHeight(),
								1.235D)
						&& sheep.getType()
								.clientTrackingRange() == 10
						&& sheep
								.getMaxSpawnClusterSize() == 4
						&& experience >= 1
						&& experience <= 3
						&& sheep.isFood(
								new ItemStack(Items.WHEAT))
						&& sheep.canBeLeashedRole(),
				"Candyfloss Sheep lost the exact passive Sheep body, movement, eye height, cluster, XP, Wheat or leash roles");
		require(helper,
				sheep.countGoalsNamed("FloatGoal") == 1
						&& sheep.goalPriority(
								"FloatGoal") == 0
						&& sheep.countGoalsNamed(
								"PanicGoal") == 1
						&& sheep.goalPriority(
								"PanicGoal") == 1
						&& sheep.countGoalsNamed(
								"BreedGoal") == 1
						&& sheep.goalPriority(
								"BreedGoal") == 2
						&& sheep.countGoalsNamed(
								"TemptGoal") == 1
						&& sheep.goalPriority(
								"TemptGoal") == 3
						&& sheep.countGoalsNamed(
								"FollowParentGoal") == 1
						&& sheep.goalPriority(
								"FollowParentGoal") == 4
						&& sheep.countGoalsNamed(
								"EatBlockGoal") == 1
						&& sheep.goalPriority(
								"EatBlockGoal") == 5
						&& sheep.countGoalsNamed(
								"CandyflossSheepGrazeGoal")
								== 1
						&& sheep.goalPriority(
								"CandyflossSheepGrazeGoal")
								== 5
						&& sheep.countGoalsNamed(
								"WaterAvoidingRandomStrollGoal")
								== 1
						&& sheep.goalPriority(
								"WaterAvoidingRandomStrollGoal")
								== 6
						&& sheep.countGoalsNamed(
								"LookAtPlayerGoal") == 1
						&& sheep.goalPriority(
								"LookAtPlayerGoal") == 7
						&& sheep.countGoalsNamed(
								"RandomLookAroundGoal")
								== 1
						&& sheep.goalPriority(
								"RandomLookAroundGoal")
								== 8
						&& sheep.countTargetGoals() == 0,
				"Candyfloss Sheep lost the exact vanilla goal order, gained combat targeting, or lost edible grazing");
		require(helper,
				sheep.ambientSound()
								== SoundEvents.SHEEP_AMBIENT
						&& sheep.hurtSound()
								== SoundEvents.SHEEP_HURT
						&& sheep.deathSound()
								== SoundEvents.SHEEP_DEATH
						&& sheep.stepSound()
								== SoundEvents.SHEEP_STEP,
				"Candyfloss Sheep lost exact ambient, hurt, death or step sounds");

		CandyflossSheep dyeSheep =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get()
						.create(helper.getLevel());
		require(helper, dyeSheep != null,
				"Could not create Candyfloss Sheep dye fixture");
		dyeSheep.setColor(DyeColor.WHITE);
		ItemStack magentaDye =
				new ItemStack(Items.MAGENTA_DYE, 2);
		InteractionResult dyeResult =
				Items.MAGENTA_DYE.interactLivingEntity(
						magentaDye,
						helper.makeMockPlayer(),
						dyeSheep,
						InteractionHand.MAIN_HAND);
		require(helper,
				dyeResult.consumesAction()
						&& dyeSheep.getColor()
								== DyeColor.MAGENTA
						&& magentaDye.getCount() == 1,
				"Candyfloss Sheep did not inherit live dye interaction");
		dyeSheep.setSheared(true);
		ItemStack blueDye =
				new ItemStack(Items.BLUE_DYE);
		require(helper,
				Items.BLUE_DYE.interactLivingEntity(
						blueDye,
						helper.makeMockPlayer(),
						dyeSheep,
						InteractionHand.MAIN_HAND)
								== InteractionResult.PASS
						&& dyeSheep.getColor()
								== DyeColor.MAGENTA
						&& blueDye.getCount() == 1,
				"Sheared Candyfloss Sheep incorrectly accepted dye");

		dyeSheep.setCustomName(
				new TextComponent("Cherry Cloud"));
		CompoundTag dyedState =
				dyeSheep.saveWithoutId(
						new CompoundTag());
		CandyflossSheep restored =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Candyfloss Sheep NBT fixture");
		restored.load(dyedState);
		require(helper,
				restored.getColor() == DyeColor.MAGENTA
						&& restored.isSheared()
						&& restored.hasCustomName()
						&& "Cherry Cloud".equals(
								restored.getName()
										.getString()),
				"Candyfloss Sheep lost colour, sheared state or name across NBT");

		CandyflossSheep red =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get()
						.create(helper.getLevel());
		CandyflossSheep yellow =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get()
						.create(helper.getLevel());
		require(helper, red != null && yellow != null,
				"Could not create Candyfloss Sheep breeding fixtures");
		red.setColor(DyeColor.RED);
		yellow.setColor(DyeColor.YELLOW);
		Sheep orangeChild = red.getBreedOffspring(
				helper.getLevel(), yellow);
		require(helper,
				orangeChild instanceof CandyflossSheep
						&& orangeChild.getType()
								== CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get()
						&& orangeChild.getColor()
								== DyeColor.ORANGE,
				"Candyfloss Sheep did not preserve vanilla dye-recipe offspring mixing on its own entity type");
		require(helper,
				Sheep.getRandomSheepColor(
						new FixedRandom(0))
								== DyeColor.BLACK
						&& Sheep.getRandomSheepColor(
								new FixedRandom(5))
								== DyeColor.GRAY
						&& Sheep.getRandomSheepColor(
								new FixedRandom(10))
								== DyeColor.LIGHT_GRAY
						&& Sheep.getRandomSheepColor(
								new FixedRandom(15))
								== DyeColor.BROWN
						&& Sheep.getRandomSheepColor(
								new FixedRandom(19))
								== DyeColor.WHITE
						&& Sheep.getRandomSheepColor(
								new SequenceRandom(18, 0))
								== DyeColor.PINK,
				"Candyfloss Sheep lost the exact black/gray/light-gray/brown/white/rare-pink natural colour branches");

		CandyflossSheep shearSheep =
				CakeWorldEntities.CANDYFLOSS_SHEEP.get()
						.create(helper.getLevel());
		require(helper, shearSheep != null,
				"Could not create Candyfloss Sheep shearing fixture");
		shearSheep.setColor(DyeColor.CYAN);
		shearSheep.setAge(0);
		shearSheep.setSheared(false);
		BlockPos shearPos = helper.absolutePos(
				new BlockPos(4, 3, 4));
		require(helper,
				shearSheep.readyForShearing()
						&& shearSheep.isShearable(
								new ItemStack(Items.SHEARS),
								helper.getLevel(),
								shearPos),
				"Adult Candyfloss Sheep did not expose vanilla and Forge shearing readiness");
		List<ItemStack> fleece =
				shearSheep.onSheared(
						helper.makeMockPlayer(),
						new ItemStack(Items.SHEARS),
						helper.getLevel(),
						shearPos, 0);
		require(helper,
				shearSheep.isSheared()
						&& fleece.size() >= 1
						&& fleece.size() <= 3
						&& fleece.stream().allMatch(
								stack -> stack.is(
										Items.CYAN_WOOL)
										&& stack
												.getCount()
												== 1),
				"Candyfloss Sheep did not return exact 1-3 colour-matched compatible wool");
		shearSheep.setAge(-24000);
		require(helper,
				!shearSheep.readyForShearing(),
				"Baby Candyfloss Sheep incorrectly became shearable");
		shearSheep.ate();
		require(helper,
				!shearSheep.isSheared()
						&& shearSheep.getAge() == -22800,
				"Candyfloss Sheep did not regrow fleece and advance a baby by the exact 60 seconds");

		sheep.setColor(DyeColor.PINK);
		sheep.setSheared(false);
		require(helper,
				sheep.getDefaultLootTable().equals(
						BuiltInLootTables.SHEEP_PINK),
				"Unsheared Candyfloss Sheep lost colour-matched wool death loot");
		sheep.setSheared(true);
		require(helper,
				sheep.getDefaultLootTable().equals(
						new ResourceLocation(
								CakeWorld.MODID,
								"entities/candyfloss_sheep")),
				"Candyfloss Sheep lost colour-wool death loot or its sheared Mutton table");

		BlockPos grazingSurface = new BlockPos(
				shearPos.getX() + 5,
				helper.getLevel().getMaxBuildHeight() - 3,
				shearPos.getZ());
		helper.getLevel().setBlock(grazingSurface,
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(grazingSurface.above(),
				Blocks.AIR.defaultBlockState(), 3);
		CandyflossSheepProbe grazing =
				new CandyflossSheepProbe(helper.getLevel());
		grazing.setPos(grazingSurface.getX() + 0.5D,
				grazingSurface.getY() + 1.0D,
				grazingSurface.getZ() + 0.5D);
		grazing.setAge(-24000);
		grazing.setSheared(true);
		grazing.seedRandom(2047L);
		CandyflossSheepGrazeGoal graze =
				new CandyflossSheepGrazeGoal(grazing);
		boolean canGraze = false;
		for (int attempt = 0;
				attempt < 10000 && !canGraze;
				attempt++) {
			canGraze = graze.canUse();
		}
		require(helper,
				canGraze
						&& helper.getLevel()
								.getBlockState(
										grazingSurface)
								.is(CandyflossSheepGrazeGoal
										.GRAZING_SURFACES),
				"Edible grazing surface or vanilla-cadence goal never became available");
		graze.start();
		require(helper,
				graze.getEatAnimationTick() > 0,
				"Edible grazing did not start the Sheep eating animation");
		while (graze.canContinueToUse()) {
			graze.tick();
		}
		require(helper,
				!grazing.isSheared()
						&& grazing.getAge() == -22800
						&& helper.getLevel()
								.getBlockState(
										grazingSurface)
								.is(CakeWorldBlocks
										.CHOCOLATE_SPONGE
										.get()),
				"Edible grazing did not regrow fleece/grow the lamb or consumed the welcoming terrain");

		BlockPos spawnSurface =
				grazingSurface.offset(3, 0, 0);
		BlockPos spawnPos = spawnSurface.above();
		helper.getLevel().setBlock(spawnSurface,
				CakeWorldBlocks.CHOCOLATE_SPONGE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				helper.getLevel()
								.getBlockState(spawnSurface)
								.is(BlockTags
										.ANIMALS_SPAWNABLE_ON)
						&& Animal.checkAnimalSpawnRules(
								CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								spawnPos,
								new Random(2047L))
						&& SpawnPlacements.checkSpawnRules(
								CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								spawnPos,
								new Random(2048L))
						&& SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Candyfloss Sheep lost edible-surface spawning or exact placement metadata");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome candyPlains = biomes.get(
				CakeWorldBiomes.CANDY_PLAINS.getId());
		require(helper, candyPlains != null,
				"Could not inspect Candy Plains Candyfloss Sheep spawning");
		MobSpawnSettings.SpawnerData candySpawn =
				candyPlains.getMobSettings()
						.getMobs(MobCategory.CREATURE)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get())
						.findFirst().orElse(null);
		require(helper,
				candySpawn != null
						&& candySpawn.getWeight()
								.asInt() == 12
						&& candySpawn.minCount == 4
						&& candySpawn.maxCount == 4,
				"Candy Plains lost the exact Plains 12/4-4 Candyfloss Sheep flock");
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SHEEP),
					"Current CakeWorld biome leaked literal Sheep: "
							+ biomeId);
		}

		MobSpawnSettingsBuilder futureMeadowSpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		BiomeLoadingEvent futureMeadow =
				new BiomeLoadingEvent(
						new ResourceLocation(CakeWorld.MODID,
								"chocolate_sponge_meadows"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings.EMPTY),
						futureMeadowSpawns);
		CakeWorldCreatureSpawns
				.onBiomeLoading(futureMeadow);
		MobSpawnSettings.SpawnerData futureSpawn =
				futureMeadowSpawns
						.getSpawner(MobCategory.CREATURE)
						.stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.CANDYFLOSS_SHEEP
										.get())
						.findFirst().orElse(null);
		require(helper,
				futureSpawn != null
						&& futureSpawn.getWeight()
								.asInt() == 12
						&& futureSpawn.minCount == 4
						&& futureSpawn.maxCount == 4
						&& futureMeadowSpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== EntityType
														.SHEEP),
				"Future Chocolate Sponge Meadows lost its exact 12/4-4 flock");

		MobSpawnSettingsBuilder thirdPartySpawns =
				new MobSpawnSettingsBuilder(
						MobSpawnSettings.EMPTY);
		thirdPartySpawns.getSpawner(
				MobCategory.CREATURE).add(
						new MobSpawnSettings.SpawnerData(
								EntityType.SHEEP,
								12, 4, 4));
		BiomeLoadingEvent thirdPartyBiome =
				new BiomeLoadingEvent(
						new ResourceLocation("examplemod",
								"pasture"),
						null, null, null,
						new BiomeGenerationSettingsBuilder(
								BiomeGenerationSettings.EMPTY),
						thirdPartySpawns);
		CakeWorldCreatureSpawns
				.onBiomeLoading(thirdPartyBiome);
		require(helper,
				thirdPartySpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().anyMatch(
										spawn -> spawn.type
												== EntityType
														.SHEEP)
						&& thirdPartySpawns
								.getSpawner(
										MobCategory.CREATURE)
								.stream().noneMatch(
										spawn -> spawn.type
												== CakeWorldEntities
														.CANDYFLOSS_SHEEP
														.get()),
				"CakeWorld rewrote an unknown third-party biome's Sheep role");

		ServerPlayer advancementPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(UUID.fromString(
								"1978feed-feed-4bad-babe-1978feed2047"),
								"CakeWorldCandyflossSheepRoleTest"));
		VanillaRoleAdvancements.creditBredRole(
				advancementPlayer,
				CakeWorldEntities.CANDYFLOSS_SHEEP.get());
		requireCriterion(helper, advancementPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:sheep");
		require(helper,
				CakeWorldItems
								.CANDYFLOSS_SHEEP_SPAWN_EGG
								.isPresent()
						&& CakeWorldEntities
								.CANDYFLOSS_SHEEP.get()
								.is(TagKey.create(
										Registry.ENTITY_TYPE_REGISTRY,
										new ResourceLocation(
												CakeWorld.MODID,
												"cakeworld_mobs"))),
				"Candyfloss Sheep lost its spawn egg or CakeWorld entity tag");

		helper.getLevel().setBlock(grazingSurface,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnSurface,
				Blocks.AIR.defaultBlockState(), 3);
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void macaronClamsKeepShulkerStorageAndSafePeril(
			GameTestHelper helper) {
		MacaronClamProbe clam =
				new MacaronClamProbe(helper.getLevel());
		require(helper,
				clam instanceof Shulker
						&& clam.getType()
								== CakeWorldEntities
										.MACARON_CLAM.get()
						&& clam.getType().getCategory()
								== MobCategory.MONSTER
						&& close(clam.getMaxHealth(), 30.0D)
						&& close(clam.getDimensions(
								Pose.STANDING).width,
								1.0D)
						&& close(clam.getDimensions(
								Pose.STANDING).height,
								1.0D)
						&& clam.getType()
								.clientTrackingRange() == 10
						&& clam.fireImmune()
						&& clam.getExperienceValue() == 5
						&& clam.getSoundSource()
								== SoundSource.HOSTILE
						&& clam.getAttachFace()
								== Direction.DOWN
						&& clam.rawPeek() == 0
						&& clam.getColor() == null
						&& close(clam.standingEyeHeight(),
								0.5D)
						&& clam.getDeltaMovement()
								.equals(Vec3.ZERO)
						&& close(clam.getPickRadius(), 0.0D)
						&& clam.canBeCollidedWith(),
				"Macaron Clam lost its genuine Shulker body, attachment, shell or tracking contract");
		require(helper,
				clam.ambientSound()
								== SoundEvents.SHULKER_AMBIENT
						&& clam.hurtSound()
								== SoundEvents
										.SHULKER_HURT_CLOSED
						&& clam.deathSound()
								== SoundEvents.SHULKER_DEATH
						&& clam.countGoalsNamed(
								"LookAtPlayerGoal") == 1
						&& clam.goalPriority(
								"LookAtPlayerGoal") == 1
						&& clam.countGoalsNamed(
								"ShulkerAttackGoal") == 1
						&& clam.goalPriority(
								"ShulkerAttackGoal") == 4
						&& clam.countGoalsNamed(
								"ShulkerPeekGoal") == 1
						&& clam.goalPriority(
								"ShulkerPeekGoal") == 7
						&& clam.countGoalsNamed(
								"RandomLookAroundGoal") == 1
						&& clam.goalPriority(
								"RandomLookAroundGoal") == 8
						&& clam.countTargetGoalsNamed(
								"HurtByTargetGoal") == 1
						&& clam.countTargetGoalsNamed(
								"ShulkerNearestAttackGoal") == 1
						&& clam.countTargetGoalsNamed(
								"ShulkerDefenseAttackGoal") == 1,
				"Macaron Clam lost exact Shulker sounds or goal priorities");

		clam.setRawPeek(30);
		clam.setRoleColor(DyeColor.MAGENTA);
		require(helper,
				clam.hurtSound()
								== SoundEvents.SHULKER_HURT
						&& clam.rawPeek() == 30
						&& clam.getColor()
								== DyeColor.MAGENTA,
				"Open Macaron Clam lost its open hurt sound, peek or colour");
		CompoundTag saved = clam.saveWithoutId(
				new CompoundTag());
		saved.putByte("AttachFace",
				(byte)Direction.NORTH.get3DDataValue());
		MacaronClamProbe restored =
				new MacaronClamProbe(helper.getLevel());
		restored.load(saved);
		require(helper,
				restored.getAttachFace()
								== Direction.NORTH
						&& restored.rawPeek() == 30
						&& restored.getColor()
								== DyeColor.MAGENTA,
				"Macaron Clam lost AttachFace, Peek or Color NBT");

		MacaronClamProbe closed =
				new MacaronClamProbe(helper.getLevel());
		Arrow arrow = new Arrow(helper.getLevel(), clam);
		float closedHealth = closed.getHealth();
		require(helper,
				!closed.hurt(DamageSource.arrow(
						arrow, clam), 5.0F)
						&& close(closed.getHealth(),
								closedHealth),
				"Closed Macaron shell lost Shulker arrow immunity");

		ShulkerBullet safeBullet = new ShulkerBullet(
				EntityType.SHULKER_BULLET,
				helper.getLevel());
		safeBullet.setOwner(clam);
		Pig safeTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, safeTarget != null,
				"Could not create Macaron Dust target");
		safeTarget.setSecondsOnFire(5);
		safeTarget.fallDistance = 20.0F;
		LivingAttackEvent safeHit =
				new LivingAttackEvent(safeTarget,
						DamageSource.indirectMobAttack(
								safeBullet, clam)
								.setProjectile(),
						4.0F);
		MacaronClamProjectileSafety.applyForDifficulty(
				safeHit, Difficulty.NORMAL);
		require(helper,
				safeHit.isCanceled()
						&& !safeTarget.isOnFire()
						&& close(safeTarget.fallDistance,
								0.0D)
						&& safeTarget.hasEffect(
								MobEffects
										.MOVEMENT_SLOWDOWN)
						&& safeTarget.hasEffect(
								MobEffects.GLOWING)
						&& safeTarget.hasEffect(
								MobEffects.SLOW_FALLING)
						&& safeTarget.hasEffect(
								MobEffects
										.FIRE_RESISTANCE)
						&& safeTarget.hasEffect(
								MobEffects
										.DAMAGE_RESISTANCE),
				"Normal Macaron projectile was damaging or lacked the complete dust rescue envelope");
		Pig hardPolicyTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, hardPolicyTarget != null,
				"Could not create Hard Macaron policy target");
		LivingAttackEvent hardHit =
				new LivingAttackEvent(hardPolicyTarget,
						DamageSource.indirectMobAttack(
								safeBullet, clam)
								.setProjectile(),
						4.0F);
		MacaronClamProjectileSafety.applyForDifficulty(
				hardHit, Difficulty.HARD);
		require(helper,
				!hardHit.isCanceled()
						&& hardPolicyTarget
								.getActiveEffects()
								.isEmpty(),
				"Hard Macaron projectile did not retain vanilla damage and Levitation handling");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		BlockPos duplicateOrigin =
				helper.absolutePos(new BlockPos(4, 4, 4));
		MacaronClamProbe duplicateTarget =
				new MacaronClamProbe(helper.getLevel());
		MacaronClam projectileOwner =
				CakeWorldEntities.MACARON_CLAM.get()
						.create(helper.getLevel());
		require(helper, projectileOwner != null,
				"Could not create Macaron duplication owner");
		duplicateTarget.moveTo(duplicateOrigin.getX() + 0.5D,
				duplicateOrigin.getY(),
				duplicateOrigin.getZ() + 0.5D);
		duplicateTarget.setRawPeek(30);
		duplicateTarget.setRoleColor(DyeColor.LIME);
		helper.getLevel().addFreshEntity(duplicateTarget);
		ShulkerBullet duplicateBullet = new ShulkerBullet(
				EntityType.SHULKER_BULLET,
				helper.getLevel());
		duplicateBullet.setOwner(projectileOwner);
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			int before = helper.getLevel()
					.getEntitiesOfClass(
							MacaronClam.class,
							new AABB(duplicateOrigin)
									.inflate(12.0D),
							Entity::isAlive)
					.size();
			require(helper,
					duplicateTarget.hurt(
							DamageSource
									.indirectMobAttack(
											duplicateBullet,
											projectileOwner)
									.setProjectile(),
							1.0F),
					"Hard Shulker Bullet did not enter the inherited Macaron damage hook");
			List<MacaronClam> duplicated = helper.getLevel()
					.getEntitiesOfClass(
							MacaronClam.class,
							new AABB(duplicateOrigin)
									.inflate(12.0D),
							Entity::isAlive);
			MacaronClam child = duplicated.stream()
					.filter(candidate ->
							candidate != duplicateTarget
									&& candidate.distanceToSqr(
											Vec3.atBottomCenterOf(
													duplicateOrigin))
											< 1.0D)
					.findFirst().orElse(null);
			require(helper,
					duplicated.size() == before + 1
							&& child != null
							&& child.getType()
									== CakeWorldEntities
											.MACARON_CLAM
											.get()
							&& child.getColor()
									== DyeColor.LIME,
					"Open projectile hit did not create one colour-preserving Macaron Clam at the pre-teleport position");

			BlockPos crowdedOrigin =
					duplicateOrigin.offset(40, 0, 0);
			MacaronClamProbe crowded =
					new MacaronClamProbe(
							helper.getLevel());
			crowded.moveTo(crowdedOrigin.getX() + 0.5D,
					crowdedOrigin.getY(),
					crowdedOrigin.getZ() + 0.5D);
			crowded.setRawPeek(30);
			helper.getLevel().addFreshEntity(crowded);
			for (int i = 0; i < 5; i++) {
				MacaronClam neighbour =
						CakeWorldEntities
								.MACARON_CLAM.get()
								.create(
										helper.getLevel());
				require(helper, neighbour != null,
						"Could not create crowded Macaron fixture");
				neighbour.moveTo(
						crowdedOrigin.getX()
								+ 1.5D + i,
						crowdedOrigin.getY(),
						crowdedOrigin.getZ() + 0.5D);
				helper.getLevel()
						.addFreshEntity(neighbour);
			}
			AABB crowdedArea = new AABB(crowdedOrigin)
					.inflate(12.0D);
			int crowdedBefore = helper.getLevel()
					.getEntitiesOfClass(
							MacaronClam.class,
							crowdedArea,
							Entity::isAlive)
					.size();
			crowded.triggerDuplicationHook();
			int crowdedAfter = helper.getLevel()
					.getEntitiesOfClass(
							MacaronClam.class,
							crowdedArea,
							Entity::isAlive)
					.size();
			require(helper,
					crowdedBefore >= 6
							&& crowdedAfter
									== crowdedBefore,
					"Macaron duplication lost the exact five-neighbour Shulker crowding cap: before="
							+ crowdedBefore
							+ ", after="
							+ crowdedAfter);

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			MacaronClamProbe peaceful =
					new MacaronClamProbe(
							helper.getLevel());
			peaceful.moveTo(
					duplicateOrigin.getX() + 70.5D,
					duplicateOrigin.getY(),
					duplicateOrigin.getZ() + 0.5D);
			helper.getLevel().addFreshEntity(peaceful);
			peaceful.checkDespawn();
			ShulkerBullet peacefulBullet =
					new ShulkerBullet(
							EntityType.SHULKER_BULLET,
							helper.getLevel());
			peacefulBullet.checkDespawn();
			require(helper,
					!peaceful.isRemoved()
							&& !peaceful
									.despawnsInPeaceful()
							&& peacefulBullet
									.isRemoved(),
					"Peaceful Macaron Clam lost vanilla's non-despawning shell or bullet-removal contract");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		BlockPos conversionAnchor =
				findCakeWorldBiomePosition(
						helper, duplicateOrigin, 512);
		require(helper, conversionAnchor != null,
				"Could not locate CakeWorld biome for End City Shulker conversion");
		Shulker vanilla =
				EntityType.SHULKER.create(helper.getLevel());
		require(helper, vanilla != null,
				"Could not create End City Shulker fixture");
		vanilla.moveTo(conversionAnchor.getX() + 0.5D,
				conversionAnchor.getY(),
				conversionAnchor.getZ() + 0.5D,
				37.0F, 0.0F);
		vanilla.setHealth(17.0F);
		vanilla.setCustomName(
				new TextComponent("Macaron Archive Guard"));
		vanilla.setPersistenceRequired();
		CompoundTag vanillaState = vanilla.saveWithoutId(
				new CompoundTag());
		vanillaState.putByte("Peek", (byte)30);
		vanillaState.putByte("Color",
				(byte)DyeColor.CYAN.getId());
		vanilla.load(vanillaState);
		MacaronClam converted =
				CakeWorldShulkerReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								vanilla);
		CompoundTag convertedState =
				converted == null ? new CompoundTag()
						: converted.saveWithoutId(
								new CompoundTag());
		require(helper,
				converted != null
						&& converted.getType()
								== CakeWorldEntities
										.MACARON_CLAM.get()
						&& close(converted.getHealth(),
								17.0D)
						&& converted.hasCustomName()
						&& "Macaron Archive Guard"
								.equals(converted
										.getCustomName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.getColor()
								== DyeColor.CYAN
						&& convertedState
								.getByte("Peek") == 30
						&& vanilla.isRemoved(),
				"End City conversion lost Macaron type, health, name, persistence, colour or shell state");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SHULKER
											|| spawn.type
													== CakeWorldEntities
															.MACARON_CLAM
															.get()),
					"Structure-only Shulker/Macaron Clam leaked into open-biome spawning in "
							+ biomeId);
		}
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.MACARON_CLAM.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.MACARON_CLAM
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.MACARON_CLAM_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.MACARON_CLAM
												.get())
								== SoundEvents
										.PARROT_IMITATE_SHULKER
						&& clam.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/macaron_clam")),
				"Macaron Clam lost placement metadata, egg, Shulker mimic or shell loot table");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2048"),
						"CakeWorldMacaronClamRoleTest"));
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(clam,
						DamageSource.playerAttack(
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:shulker");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void crumbMitesKeepSilverfishNestsAndSafeMischief(
			GameTestHelper helper) {
		CrumbMiteProbe mite =
				new CrumbMiteProbe(helper.getLevel());
		require(helper,
				mite instanceof Silverfish
						&& mite.getType()
								== CakeWorldEntities
										.CRUMB_MITE.get()
						&& mite.getType().getCategory()
								== MobCategory.MONSTER
						&& close(mite.getMaxHealth(), 8.0D)
						&& close(mite.getAttributeValue(
								Attributes
										.MOVEMENT_SPEED),
								0.25D)
						&& close(mite.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								1.0D)
						&& close(mite.getDimensions(
								Pose.STANDING).width,
								0.4D)
						&& close(mite.getDimensions(
								Pose.STANDING).height,
								0.3D)
						&& mite.getType()
								.clientTrackingRange() == 8
						&& mite.getExperienceValue() == 5
						&& mite.getMobType()
								== MobType.ARTHROPOD
						&& close(mite.getMyRidingOffset(),
								0.1D)
						&& close(mite.standingEyeHeight(),
								0.13D),
				"Crumb Mite lost its genuine Silverfish body, attributes, XP or arthropod role");
		require(helper,
				mite.ambientSound()
								== SoundEvents
										.SILVERFISH_AMBIENT
						&& mite.hurtSound()
								== SoundEvents
										.SILVERFISH_HURT
						&& mite.deathSound()
								== SoundEvents
										.SILVERFISH_DEATH
						&& mite.stepSound()
								== SoundEvents
										.SILVERFISH_STEP
						&& mite.countGoalsNamed(
								"FloatGoal") == 1
						&& mite.goalPriority(
								"FloatGoal") == 1
						&& mite.countGoalsNamed(
								"ClimbOnTopOfPowderSnowGoal")
								== 1
						&& mite.goalPriority(
								"ClimbOnTopOfPowderSnowGoal")
								== 1
						&& mite.countGoalsNamed(
								"SilverfishWakeUpFriendsGoal")
								== 1
						&& mite.goalPriority(
								"SilverfishWakeUpFriendsGoal")
								== 3
						&& mite.countGoalsNamed(
								"MeleeAttackGoal") == 1
						&& mite.goalPriority(
								"MeleeAttackGoal") == 4
						&& mite.countGoalsNamed(
								"SilverfishMergeWithStoneGoal")
								== 1
						&& mite.goalPriority(
								"SilverfishMergeWithStoneGoal")
								== 5
						&& mite.targetGoalPriority(
								"HurtByTargetGoal") == 1
						&& mite.targetGoalPriority(
								"NearestAttackableTargetGoal")
								== 2,
				"Crumb Mite lost exact Silverfish sounds or goal priorities");

		BlockState biscuit =
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState();
		BlockState nest =
				CakeWorldBlocks.CRUMB_MITE_NEST.get()
						.defaultBlockState();
		require(helper,
				nest.getBlock() instanceof InfestedBlock
						&& ((InfestedBlock)nest.getBlock())
								.getHostBlock()
								== CakeWorldBlocks
										.BISCUIT_STONE.get()
						&& InfestedBlock
								.isCompatibleHostBlock(
										biscuit)
						&& InfestedBlock
								.infestedStateByHost(
										biscuit)
								.is(CakeWorldBlocks
										.CRUMB_MITE_NEST
										.get())
						&& ((InfestedBlock)nest.getBlock())
								.hostStateByInfested(nest)
								.is(CakeWorldBlocks
										.BISCUIT_STONE
										.get())
						&& CakeWorldBlocks.CRUMB_MITE_NEST
								.get().getLootTable()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"blocks/crumb_mite_nest")),
				"Crumb-Mite Biscuit Stone lost its true InfestedBlock host or Silk-Touch loot route");
		BlockPos center =
				helper.absolutePos(new BlockPos(1, 1, 1));
		helper.getLevel().setBlock(center.below(),
				biscuit, 3);
		require(helper,
				close(mite.getWalkTargetValue(
						center, helper.getLevel()),
						10.0D),
				"Crumb Mite no longer prefers its compatible edible host");
		helper.getLevel().setBlock(center.below(),
				Blocks.AIR.defaultBlockState(), 3);

		for (Difficulty difficulty : List.of(
				Difficulty.PEACEFUL,
				Difficulty.EASY,
				Difficulty.NORMAL,
				Difficulty.HARD)) {
			EntityMobGriefingEvent grief =
					new EntityMobGriefingEvent(mite);
			CrumbMiteGriefSafety.applyForDifficulty(
					grief, difficulty);
			require(helper,
					grief.getResult()
							== (difficulty
									== Difficulty.HARD
											? Event.Result
													.DEFAULT
											: Event.Result
													.DENY),
					difficulty
							+ " Crumb Mite crossed the Hard-only nest-grief boundary");
		}

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		boolean originalMobGriefing =
				helper.getLevel().getGameRules()
						.getBoolean(
								GameRules
										.RULE_MOBGRIEFING);
		CrumbMite converted = null;
		try {
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(true,
							helper.getLevel().getServer());
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			Pig safeTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, safeTarget != null,
					"Could not create safe Crumb Mite target");
			safeTarget.setHealth(10.0F);
			safeTarget.setSecondsOnFire(5);
			safeTarget.fallDistance = 20.0F;
			require(helper,
					mite.doHurtTarget(safeTarget)
							&& close(safeTarget
									.getHealth(),
									10.0D)
							&& !safeTarget.isOnFire()
							&& close(safeTarget
									.fallDistance,
									0.0D)
							&& safeTarget.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& safeTarget.hasEffect(
									MobEffects.GLOWING)
							&& safeTarget.hasEffect(
									MobEffects
											.SLOW_FALLING)
							&& safeTarget.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& safeTarget.hasEffect(
									MobEffects
											.DAMAGE_RESISTANCE),
					"Normal Crumb Mite bite caused health damage or lacked complete rescue");

			CrumbMiteProbe safeMerge =
					new CrumbMiteProbe(
							helper.getLevel());
			safeMerge.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			safeMerge.setNoGravity(true);
			for (Direction direction :
					Direction.values()) {
				helper.getLevel().setBlock(
						center.relative(direction),
						biscuit, 3);
			}
			helper.getLevel().addFreshEntity(safeMerge);
			safeMerge.seedRandom(0L);
			safeMerge.startGoal(
					"SilverfishMergeWithStoneGoal");
			require(helper,
					Direction.values().length
									== Arrays.stream(
											Direction
													.values())
											.filter(direction ->
													!helper
															.getLevel()
															.getBlockState(
																	center
																			.relative(
																					direction))
															.is(CakeWorldBlocks
																	.CRUMB_MITE_NEST
																	.get()))
											.count()
							&& !safeMerge.isRemoved(),
					"Normal Crumb Mite changed a possession into a nest");
			safeMerge.discard();

			for (Direction direction :
					Direction.values()) {
				helper.getLevel().setBlock(
						center.relative(direction),
						Blocks.AIR.defaultBlockState(),
						3);
			}
			CrumbMiteProbe safeWake =
					new CrumbMiteProbe(
							helper.getLevel());
			CrumbMiteProbe attacker =
					new CrumbMiteProbe(
							helper.getLevel());
			safeWake.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			safeWake.setNoGravity(true);
			helper.getLevel().addFreshEntity(safeWake);
			helper.getLevel().setBlock(center, nest, 3);
			safeWake.hurt(
					DamageSource.mobAttack(attacker),
					1.0F);
			safeWake.tickGoal(
					"SilverfishWakeUpFriendsGoal", 25);
			require(helper,
					helper.getLevel()
								.getBlockState(center)
								.is(CakeWorldBlocks
										.BISCUIT_STONE
										.get())
							&& helper.getLevel()
									.getEntitiesOfClass(
											Silverfish.class,
											new AABB(center)
													.inflate(
															1.0D),
											entity ->
													entity
															.getType()
															== EntityType
																	.SILVERFISH)
									.isEmpty(),
					"Normal hurt Crumb Mite woke a damaging friend instead of safely clearing the nest");
			safeWake.discard();

			helper.getLevel().setBlock(center,
					Blocks.AIR.defaultBlockState(), 3);
			for (Direction direction :
					Direction.values()) {
				helper.getLevel().setBlock(
						center.relative(direction),
						biscuit, 3);
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			CrumbMiteProbe hardMerge =
					new CrumbMiteProbe(
							helper.getLevel());
			hardMerge.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			hardMerge.setNoGravity(true);
			helper.getLevel().addFreshEntity(hardMerge);
			hardMerge.seedRandom(0L);
			boolean merged = hardMerge.startGoal(
					"SilverfishMergeWithStoneGoal");
			long nestCount = Arrays.stream(
					Direction.values())
					.filter(direction -> helper
							.getLevel()
							.getBlockState(
									center.relative(direction))
							.is(CakeWorldBlocks
									.CRUMB_MITE_NEST.get()))
					.count();
			require(helper,
					merged && hardMerge.isRemoved()
							&& nestCount == 1,
					"Hard Crumb Mite did not use the inherited merge goal to create exactly one edible nest: "
							+ nestCount);

			for (Direction direction :
					Direction.values()) {
				helper.getLevel().setBlock(
						center.relative(direction),
						Blocks.AIR.defaultBlockState(),
						3);
			}
			CrumbMiteProbe hardWake =
					new CrumbMiteProbe(
							helper.getLevel());
			hardWake.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			hardWake.setNoGravity(true);
			helper.getLevel().addFreshEntity(hardWake);
			helper.getLevel().setBlock(center, nest, 3);
			hardWake.hurt(
					DamageSource.mobAttack(attacker),
					1.0F);
			hardWake.tickGoal(
					"SilverfishWakeUpFriendsGoal", 25);
			Silverfish literal = helper.getLevel()
					.getEntitiesOfClass(
							Silverfish.class,
							new AABB(center)
									.inflate(1.0D),
							entity -> entity.getType()
									== EntityType.SILVERFISH)
					.stream().findFirst().orElse(null);
			require(helper,
					helper.getLevel()
								.getBlockState(center)
								.isAir()
							&& literal != null,
					"Hard friend-wake did not break the edible nest and spawn its literal Stronghold role");
			literal.setHealth(5.0F);
			literal.setCustomName(
					new TextComponent(
							"Vault Crumb Mite"));
			literal.setPersistenceRequired();
			converted = CakeWorldSilverfishReplacement
					.replaceIfInCakeWorldBiome(
							helper.getLevel(),
							literal);
			require(helper,
					converted != null
							&& converted.getType()
									== CakeWorldEntities
											.CRUMB_MITE.get()
							&& close(converted.getHealth(),
									5.0D)
							&& converted.hasCustomName()
							&& "Vault Crumb Mite"
									.equals(converted
											.getCustomName()
											.getString())
							&& converted
									.isPersistenceRequired()
							&& literal.isRemoved(),
					"Fresh infested-block/Stronghold conversion lost Crumb Mite type or state");

			Pig hardTarget = EntityType.PIG.create(
					helper.getLevel());
			require(helper, hardTarget != null,
					"Could not create Hard Crumb Mite target");
			hardTarget.setHealth(10.0F);
			hardTarget.invulnerableTime = 0;
			require(helper,
					mite.doHurtTarget(hardTarget)
							&& close(hardTarget
									.getHealth(),
									9.0D),
					"Hard Crumb Mite lost the exact one-point Silverfish bite");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			CrumbMiteProbe peaceful =
					new CrumbMiteProbe(
							helper.getLevel());
			peaceful.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			helper.getLevel().addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Crumb Mite lost vanilla Monster removal");
		} finally {
			if (converted != null) {
				converted.discard();
			}
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					for (int z = -1; z <= 1; z++) {
						helper.getLevel().setBlock(
								center.offset(x, y, z),
								Blocks.AIR
										.defaultBlockState(),
								3);
					}
				}
			}
			helper.getLevel().getGameRules()
					.getRule(GameRules.RULE_MOBGRIEFING)
					.set(originalMobGriefing,
							helper.getLevel()
									.getServer());
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SILVERFISH
											|| spawn.type
													== CakeWorldEntities
															.CRUMB_MITE
															.get()),
					"Structure-only Silverfish/Crumb Mite leaked into open-biome spawning in "
							+ biomeId);
		}
		TagKey<EntityType<?>> powderWalkers =
				TagKey.create(
						Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft",
								"powder_snow_walkable_mobs"));
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.CRUMB_MITE.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.CRUMB_MITE
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldEntities.CRUMB_MITE
								.get().is(powderWalkers)
						&& CakeWorldItems
								.CRUMB_MITE_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.CRUMB_MITE
												.get())
								== SoundEvents
										.PARROT_IMITATE_SILVERFISH
						&& mite.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/crumb_mite")),
				"Crumb Mite lost placement, powder walking, egg, mimic or empty loot");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2049"),
						"CakeWorldCrumbMiteRoleTest"));
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(mite,
						DamageSource.playerAttack(
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:silverfish");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 240)
	public static void candyCaneArchersKeepSkeletonBowsAndSafeStickyShots(
			GameTestHelper helper) {
		CandyCaneArcherProbe archer =
				new CandyCaneArcherProbe(
						helper.getLevel());
		require(helper,
				archer instanceof Skeleton
						&& archer
								instanceof AbstractSkeleton
						&& archer.getType()
								== CakeWorldEntities
										.CANDY_CANE_ARCHER
										.get()
						&& archer.getType().getCategory()
								== MobCategory.MONSTER
						&& close(archer.getMaxHealth(),
								20.0D)
						&& close(archer.getAttributeValue(
								Attributes
										.MOVEMENT_SPEED),
								0.25D)
						&& close(archer.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								2.0D)
						&& close(archer.getAttributeValue(
								Attributes
										.FOLLOW_RANGE),
								16.0D)
						&& close(archer.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(archer.getDimensions(
								Pose.STANDING).height,
								1.99D)
						&& archer.getType()
								.clientTrackingRange() == 8
						&& archer.getExperienceValue() == 5
						&& archer.getMobType()
								== MobType.UNDEAD
						&& close(archer.getMyRidingOffset(),
								-0.6D)
						&& close(archer
								.standingEyeHeight(),
								1.74D)
						&& !archer.canFreeze(),
				"Candy-Cane Archer lost its genuine Skeleton body, attributes, undead or freeze role");
		require(helper,
				archer.ambientSound()
								== SoundEvents
										.SKELETON_AMBIENT
						&& archer.hurtSound()
								== SoundEvents
										.SKELETON_HURT
						&& archer.deathSound()
								== SoundEvents
										.SKELETON_DEATH
						&& archer.stepSound()
								== SoundEvents
										.SKELETON_STEP
						&& archer.goalPriority(
								"RestrictSunGoal") == 2
						&& archer.goalPriority(
								"FleeSunGoal") == 3
						&& archer.goalPriority(
								"AvoidEntityGoal") == 3
						&& archer.goalPriority(
								"WaterAvoidingRandomStrollGoal")
								== 5
						&& archer.goalPriority(
								"LookAtPlayerGoal") == 6
						&& archer.goalPriority(
								"RandomLookAroundGoal") == 6
						&& archer.targetGoalPriority(
								"HurtByTargetGoal") == 1
						&& archer.targetGoalPriority(
								"NearestAttackableTargetGoal")
								== 2
						&& archer
								.countTargetGoalsAtPriority(3)
								== 2,
				"Candy-Cane Archer lost exact Skeleton sounds, sun/wolf movement or target priorities");

		BlockPos center =
				helper.absolutePos(new BlockPos(1, 1, 1));
		archer.moveTo(center.getX() + 0.5D,
				center.getY(),
				center.getZ() + 0.5D);
		archer.equipDefault(
				helper.getLevel()
						.getCurrentDifficultyAt(center));
		require(helper,
				archer.getMainHandItem().is(Items.BOW)
						&& archer.canFireBow()
						&& archer.goalPriority(
								"RangedBowAttackGoal")
								== 4
						&& archer
								.countGoalsAssignableTo(
										net.minecraft
												.world
												.entity
												.ai
												.goal
												.MeleeAttackGoal
												.class)
								== 0,
				"Candy-Cane Archer lost default Bow equipment or ranged-goal selection");

		archer.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.IRON_SWORD));
		require(helper,
				archer.countGoalsAssignableTo(
								net.minecraft.world.entity
										.ai.goal
										.MeleeAttackGoal
										.class)
								== 1
						&& archer.goalPriorityAssignableTo(
								net.minecraft.world.entity
										.ai.goal
										.MeleeAttackGoal
										.class)
								== 4
						&& archer.goalPriority(
								"RangedBowAttackGoal")
								== -1,
				"Candy-Cane Archer no longer switches to inherited melee AI without a Bow");
		archer.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.BOW));

		archer.forceSunBurn(true);
		archer.setItemSlot(EquipmentSlot.HEAD,
				ItemStack.EMPTY);
		archer.clearFire();
		archer.aiStep();
		require(helper, archer.isOnFire(),
				"Candy-Cane Archer lost Skeleton sunlight burning");
		archer.clearFire();
		archer.setItemSlot(EquipmentSlot.HEAD,
				new ItemStack(Items.IRON_HELMET));
		archer.aiStep();
		require(helper,
				!archer.isOnFire()
						&& !archer.getItemBySlot(
								EquipmentSlot.HEAD)
								.isEmpty(),
				"Candy-Cane Archer helmet no longer shields sunlight");
		archer.forceSunBurn(false);

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		CandyCaneArcher converted = null;
		Spider vehicle = null;
		Stray convertedStray = null;
		FrostedArcher convertedFrosted = null;
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			archer.reassessWeaponGoal();
			require(helper,
					archer.bowAttackInterval() == 40,
					"Normal Candy-Cane Archer lost the exact 40-tick Skeleton bow interval");
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			archer.reassessWeaponGoal();
			require(helper,
					archer.bowAttackInterval() == 20,
					"Hard Candy-Cane Archer lost the exact 20-tick Skeleton bow interval");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			archer.reassessWeaponGoal();
			helper.getLevel().addFreshEntity(archer);
			Pig aimingTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, aimingTarget != null,
					"Could not create Candy-Cane Archer aiming target");
			aimingTarget.moveTo(center.getX() + 8.5D,
					center.getY(),
					center.getZ() + 0.5D);
			archer.clearLastSound();
			archer.performRangedAttack(
					aimingTarget, 1.0F);
			AbstractArrow fired = helper.getLevel()
					.getEntitiesOfClass(
							AbstractArrow.class,
							new AABB(center)
									.inflate(16.0D),
							arrow -> arrow.getOwner()
									== archer)
					.stream().findFirst().orElse(null);
			require(helper,
					fired instanceof Arrow
							&& fired.getOwner() == archer
							&& archer.lastSound()
									== SoundEvents
											.SKELETON_SHOOT
							&& fired.getDeltaMovement()
									.length() > 1.0D,
					"Candy-Cane Archer did not fire the inherited aimed Arrow with Skeleton cue");

			Pig safeTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, safeTarget != null,
					"Could not create sticky-shot target");
			safeTarget.moveTo(center.getX() + 4.5D,
					center.getY(),
					center.getZ() + 0.5D);
			safeTarget.setHealth(10.0F);
			safeTarget.setSecondsOnFire(5);
			safeTarget.fallDistance = 20.0F;
			boolean safeHit = safeTarget.hurt(
					DamageSource.arrow(
							(AbstractArrow)fired,
							archer),
					4.0F);
			require(helper,
					!safeHit
							&& close(safeTarget
									.getHealth(),
									10.0D)
							&& !safeTarget.isOnFire()
							&& close(safeTarget
									.fallDistance,
									0.0D)
							&& safeTarget.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& safeTarget.getEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
									.getAmplifier() == 1
							&& safeTarget.hasEffect(
									MobEffects.GLOWING)
							&& safeTarget.hasEffect(
									MobEffects
											.SLOW_FALLING)
							&& safeTarget.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& safeTarget.hasEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
							&& safeTarget
									.getDeltaMovement().y > 0.0D,
					"Normal Candy-Cane Arrow caused health damage or lacked sticky rescue");

			CandyCaneArcherProbe melee =
					new CandyCaneArcherProbe(
							helper.getLevel());
			melee.moveTo(center.getX() - 2.5D,
					center.getY(),
					center.getZ() + 0.5D);
			Pig safeMelee =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, safeMelee != null,
					"Could not create safe melee target");
			safeMelee.setHealth(10.0F);
			require(helper,
					melee.doHurtTarget(safeMelee)
							&& close(safeMelee
									.getHealth(),
									10.0D)
							&& safeMelee.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN),
					"Normal Bow-less Candy-Cane Archer bypassed the no-damage contract");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardArrowTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, hardArrowTarget != null,
					"Could not create Hard arrow target");
			hardArrowTarget.setHealth(10.0F);
			hardArrowTarget.invulnerableTime = 0;
			require(helper,
					hardArrowTarget.hurt(
							DamageSource.arrow(
									(AbstractArrow)fired,
									archer),
							4.0F)
							&& close(hardArrowTarget
									.getHealth(),
									6.0D),
					"Hard Candy-Cane Arrow no longer permits exact incoming damage");
			Pig hardMeleeTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, hardMeleeTarget != null,
					"Could not create Hard melee target");
			hardMeleeTarget.setHealth(10.0F);
			hardMeleeTarget.invulnerableTime = 0;
			require(helper,
					melee.doHurtTarget(
							hardMeleeTarget)
							&& close(hardMeleeTarget
									.getHealth(),
									8.0D),
					"Hard Bow-less Candy-Cane Archer lost the exact two-point Skeleton melee attack");

			BlockPos cakeWorldPos =
					findCakeWorldBiomePosition(
							helper, center, 256);
			require(helper, cakeWorldPos != null,
					"Could not locate CakeWorld terrain for literal Skeleton conversion");
			Skeleton literal =
					EntityType.SKELETON.create(
							helper.getLevel());
			vehicle = EntityType.SPIDER.create(
					helper.getLevel());
			require(helper,
					literal != null && vehicle != null,
					"Could not create literal Skeleton jockey fixture");
			literal.moveTo(
					cakeWorldPos.getX() + 0.5D,
					cakeWorldPos.getY() + 2.0D,
					cakeWorldPos.getZ() + 0.5D);
			vehicle.moveTo(literal.getX(),
					literal.getY(), literal.getZ());
			literal.setHealth(13.0F);
			literal.setCustomName(
					new TextComponent(
							"Striped Sharpshooter"));
			literal.setPersistenceRequired();
			literal.setNoAi(true);
			literal.setItemSlot(
					EquipmentSlot.MAINHAND,
					new ItemStack(Items.BOW));
			literal.setItemSlot(
					EquipmentSlot.HEAD,
					new ItemStack(
							Items.GOLDEN_HELMET));
			helper.getLevel()
					.addFreshEntity(vehicle);
			helper.getLevel()
					.addFreshEntity(literal);
			literal.startRiding(vehicle, true);
			converted = CakeWorldSkeletonReplacement
					.replaceIfInCakeWorldBiome(
							helper.getLevel(),
							literal);
			require(helper,
					converted != null
							&& converted.getType()
									== CakeWorldEntities
											.CANDY_CANE_ARCHER
											.get()
							&& close(converted.getHealth(),
									13.0D)
							&& converted.hasCustomName()
							&& "Striped Sharpshooter"
									.equals(converted
											.getCustomName()
											.getString())
							&& converted
									.isPersistenceRequired()
							&& converted.isNoAi()
							&& converted
									.getMainHandItem()
									.is(Items.BOW)
							&& converted.getItemBySlot(
									EquipmentSlot.HEAD)
									.is(Items
											.GOLDEN_HELMET)
							&& converted.getVehicle()
									== vehicle
							&& vehicle.getPassengers()
									.contains(converted)
							&& literal.isRemoved(),
					"Fresh spawner/jockey/trap conversion lost Archer state, equipment or mount");

			CandyCaneArcherProbe freezing =
					new CandyCaneArcherProbe(
							helper.getLevel());
			freezing.moveTo(
					cakeWorldPos.getX() + 0.5D,
					cakeWorldPos.getY() + 2.0D,
					cakeWorldPos.getZ() + 3.5D);
			freezing.startFreezeConversionFromNbt(0);
			require(helper,
					freezing.isFreezeConverting()
							&& freezing.isShaking(),
					"Candy-Cane Archer lost Skeleton freeze-conversion NBT or shaking");
			helper.getLevel()
					.addFreshEntity(freezing);
			freezing.tick();
			convertedStray = helper.getLevel()
					.getEntitiesOfClass(
							Stray.class,
							freezing.getBoundingBox()
									.inflate(2.0D),
							stray -> stray.getType()
									== EntityType.STRAY)
					.stream().findFirst().orElse(null);
			require(helper,
					freezing.isRemoved()
							&& convertedStray != null,
					"Candy-Cane Archer lost its inherited intermediate Stray conversion");
			convertedFrosted =
					CakeWorldStrayReplacement
							.replaceIfInCakeWorldBiome(
									helper.getLevel(),
									convertedStray);
			require(helper,
					convertedFrosted != null
							&& convertedFrosted.getType()
									== CakeWorldEntities
											.FROSTED_ARCHER
											.get()
							&& convertedStray.isRemoved(),
					"Candy-Cane Archer freeze output did not hand off to Frosted Archer");

			Creeper charged =
					EntityType.CREEPER.create(
							helper.getLevel());
			require(helper, charged != null,
					"Could not create charged Creeper fixture");
			CompoundTag chargedTag =
					new CompoundTag();
			chargedTag.putBoolean("powered", true);
			charged.readAdditionalSaveData(
					chargedTag);
			archer.emitCustomDeathLoot(
					DamageSource.mobAttack(charged));
			ItemEntity skull = helper.getLevel()
					.getEntitiesOfClass(
							ItemEntity.class,
							archer.getBoundingBox()
									.inflate(2.0D),
							item -> item.getItem()
									.is(Items
											.SKELETON_SKULL))
					.stream().findFirst().orElse(null);
			require(helper,
					skull != null
							&& !charged
									.canDropMobsSkull(),
					"Candy-Cane Archer lost the charged-Creeper Skeleton Skull role");
			skull.discard();

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			CandyCaneArcherProbe peaceful =
					new CandyCaneArcherProbe(
							helper.getLevel());
			peaceful.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			helper.getLevel()
					.addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Candy-Cane Archer lost vanilla Monster removal");
		} finally {
			if (!archer.isRemoved()) {
				archer.discard();
			}
			if (converted != null) {
				converted.discard();
			}
			if (vehicle != null
					&& !vehicle.isRemoved()) {
				vehicle.discard();
			}
			if (convertedStray != null
					&& !convertedStray.isRemoved()) {
				convertedStray.discard();
			}
			if (convertedFrosted != null
					&& !convertedFrosted.isRemoved()) {
				convertedFrosted.discard();
			}
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SKELETON),
					"Literal Skeleton leaked into CakeWorld spawning in "
							+ biomeId);
			if (biomeId.equals(
							CakeWorldBiomes
									.CANDY_PLAINS.getId())
					|| biomeId.equals(
							CakeWorldBiomes
									.COOKIE_FOREST.getId())
					|| biomeId.equals(
							CakeWorldBiomes
									.MARSHMALLOW_PEAKS
									.getId())
					|| biomeId.equals(
							CakeWorldBiomes
									.SODA_OCEAN.getId())) {
				List<MobSpawnSettings.SpawnerData>
						archerSpawns = biome
								.getMobSettings()
								.getMobs(
										MobCategory
												.MONSTER)
								.unwrap().stream()
								.filter(spawn ->
										spawn.type
												== CakeWorldEntities
														.CANDY_CANE_ARCHER
														.get())
								.toList();
				require(helper,
						archerSpawns.size() == 1,
						"Expected one inherited Candy-Cane Archer profile in "
								+ biomeId + ": "
								+ archerSpawns);
			}
		}

		TagKey<EntityType<?>> skeletons =
				TagKey.create(
						Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft",
								"skeletons"));
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.CANDY_CANE_ARCHER
										.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.CANDY_CANE_ARCHER
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldEntities
								.CANDY_CANE_ARCHER
								.get().is(skeletons)
						&& CakeWorldItems
								.CANDY_CANE_ARCHER_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.CANDY_CANE_ARCHER
												.get())
								== SoundEvents
										.PARROT_IMITATE_SKELETON
						&& archer.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/candy_cane_archer")),
				"Candy-Cane Archer lost placement, Skeleton-tag music-disc, egg, mimic or exact loot route");

		ServerPlayer advancementPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2050"),
						"CakeWorldCandyArcherRoleTest"));
		advancementPlayer.setPos(center.getX() - 60.0D,
				center.getY(), center.getZ());
		archer.setPos(center.getX(),
				center.getY(), center.getZ());
		Arrow playerArrow = new Arrow(
				helper.getLevel(),
				advancementPlayer);
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(archer,
						DamageSource.arrow(
								playerArrow,
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:skeleton");
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/sniper_duel",
				"killed_skeleton");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 260)
	public static void frostedArchersKeepStrayArrowsSnowEcologyAndSafeChills(
			GameTestHelper helper) {
		FrostedArcherProbe archer =
				new FrostedArcherProbe(helper.getLevel());
		require(helper,
				archer instanceof Stray
						&& archer
								instanceof AbstractSkeleton
						&& archer.getType()
								== CakeWorldEntities
										.FROSTED_ARCHER.get()
						&& archer.getType().getCategory()
								== MobCategory.MONSTER
						&& close(archer.getMaxHealth(),
								20.0D)
						&& close(archer.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.25D)
						&& close(archer.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								2.0D)
						&& close(archer.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								16.0D)
						&& close(archer.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(archer.getDimensions(
								Pose.STANDING).height,
								1.99D)
						&& archer.getType()
								.clientTrackingRange() == 8
						&& archer.getExperienceValue() == 5
						&& archer.getMobType()
								== MobType.UNDEAD
						&& close(archer.getMyRidingOffset(),
								-0.6D)
						&& close(archer
								.standingEyeHeight(),
								1.74D)
						&& !archer.canFreeze()
						&& !CakeWorldEntities
								.FROSTED_ARCHER.get()
								.isBlockDangerous(
										Blocks.POWDER_SNOW
												.defaultBlockState()),
				"Frosted Archer lost its genuine Stray body, attributes, undead or powder-snow immunity");
		require(helper,
				archer.ambientSound()
								== SoundEvents.STRAY_AMBIENT
						&& archer.hurtSound()
								== SoundEvents.STRAY_HURT
						&& archer.deathSound()
								== SoundEvents.STRAY_DEATH
						&& archer.stepSound()
								== SoundEvents.STRAY_STEP
						&& archer.goalPriority(
								"RestrictSunGoal") == 2
						&& archer.goalPriority(
								"FleeSunGoal") == 3
						&& archer.goalPriority(
								"AvoidEntityGoal") == 3
						&& archer.goalPriority(
								"WaterAvoidingRandomStrollGoal")
								== 5
						&& archer.goalPriority(
								"LookAtPlayerGoal") == 6
						&& archer.goalPriority(
								"RandomLookAroundGoal") == 6
						&& archer.targetGoalPriority(
								"HurtByTargetGoal") == 1
						&& archer.targetGoalPriority(
								"NearestAttackableTargetGoal")
								== 2
						&& archer
								.countTargetGoalsAtPriority(3)
								== 2,
				"Frosted Archer lost exact Stray sounds or inherited Skeleton AI priorities");

		BlockPos center =
				helper.absolutePos(new BlockPos(1, 2, 1));
		helper.getLevel().setBlock(center.below(),
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState(), 3);
		for (int y = 0; y <= 4; y++) {
			helper.getLevel().setBlock(center.above(y),
					Blocks.AIR.defaultBlockState(), 3);
		}
		archer.moveTo(center.getX() + 0.5D,
				center.getY(),
				center.getZ() + 0.5D);
		archer.equipDefault(
				helper.getLevel()
						.getCurrentDifficultyAt(center));
		CompoundTag chilledArrowData =
				archer.createChilledArrow()
						.saveWithoutId(new CompoundTag());
		require(helper,
				archer.getMainHandItem().is(Items.BOW)
						&& archer.canFireBow()
						&& archer.goalPriority(
								"RangedBowAttackGoal")
								== 4
						&& archer
								.countGoalsAssignableTo(
										net.minecraft.world
												.entity.ai.goal
												.MeleeAttackGoal
												.class)
								== 0
						&& hasExactCustomEffect(
								chilledArrowData,
								MobEffects
										.MOVEMENT_SLOWDOWN,
								600, 0),
				"Frosted Archer lost default Bow AI or the exact Slowness-tipped arrow");

		archer.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.IRON_SWORD));
		require(helper,
				archer.countGoalsAssignableTo(
								net.minecraft.world.entity.ai.goal
										.MeleeAttackGoal.class)
								== 1
						&& archer.goalPriorityAssignableTo(
								net.minecraft.world.entity.ai.goal
										.MeleeAttackGoal.class)
								== 4
						&& archer.goalPriority(
								"RangedBowAttackGoal") == -1,
				"Frosted Archer no longer switches to inherited melee AI without a Bow");
		archer.setItemSlot(EquipmentSlot.MAINHAND,
				new ItemStack(Items.BOW));

		archer.forceSunBurn(true);
		archer.setItemSlot(EquipmentSlot.HEAD,
				ItemStack.EMPTY);
		archer.clearFire();
		archer.aiStep();
		require(helper, archer.isOnFire(),
				"Frosted Archer lost inherited sunlight burning");
		archer.clearFire();
		archer.setItemSlot(EquipmentSlot.HEAD,
				new ItemStack(Items.IRON_HELMET));
		archer.aiStep();
		require(helper,
				!archer.isOnFire()
						&& !archer.getItemBySlot(
								EquipmentSlot.HEAD)
								.isEmpty(),
				"Frosted Archer helmet no longer shields sunlight");
		archer.forceSunBurn(false);

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		FrostedArcher converted = null;
		Spider vehicle = null;
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			archer.reassessWeaponGoal();
			require(helper,
					archer.bowAttackInterval() == 40,
					"Normal Frosted Archer lost the exact 40-tick Skeleton bow interval");
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			archer.reassessWeaponGoal();
			require(helper,
					archer.bowAttackInterval() == 20,
					"Hard Frosted Archer lost the exact 20-tick Skeleton bow interval");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			archer.reassessWeaponGoal();
			helper.getLevel().addFreshEntity(archer);
			Pig aimingTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, aimingTarget != null,
					"Could not create Frosted Archer aiming target");
			aimingTarget.moveTo(center.getX() + 8.5D,
					center.getY(),
					center.getZ() + 0.5D);
			archer.clearLastSound();
			archer.performRangedAttack(
					aimingTarget, 1.0F);
			AbstractArrow fired = helper.getLevel()
					.getEntitiesOfClass(
							AbstractArrow.class,
							new AABB(center)
									.inflate(16.0D),
							arrow -> arrow.getOwner()
									== archer)
					.stream().findFirst().orElse(null);
			CompoundTag firedData = fired == null
					? new CompoundTag()
					: fired.saveWithoutId(
							new CompoundTag());
			require(helper,
					fired instanceof Arrow
							&& fired.getOwner() == archer
							&& hasExactCustomEffect(
									firedData,
									MobEffects
											.MOVEMENT_SLOWDOWN,
									600, 0)
							&& archer.lastSound()
									== SoundEvents
											.SKELETON_SHOOT
							&& fired.getDeltaMovement()
									.length() > 1.0D,
					"Frosted Archer did not fire the inherited aimed Slowness Arrow with Skeleton cue");

			Pig safeTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, safeTarget != null,
					"Could not create safe Frosted Archer arrow target");
			safeTarget.moveTo(center.getX() + 2.5D,
					center.getY(),
					center.getZ() + 0.5D);
			safeTarget.setHealth(10.0F);
			safeTarget.setSecondsOnFire(5);
			safeTarget.fallDistance = 20.0F;
			boolean safeHit = safeTarget.hurt(
					DamageSource.arrow(fired, archer),
					4.0F);
			require(helper,
					!safeHit
							&& close(safeTarget
									.getHealth(), 10.0D)
							&& !safeTarget.isOnFire()
							&& close(safeTarget
									.fallDistance, 0.0D)
							&& safeTarget.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& safeTarget.getEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
									.getAmplifier() == 1
							&& safeTarget.hasEffect(
									MobEffects.GLOWING)
							&& safeTarget.hasEffect(
									MobEffects.SLOW_FALLING)
							&& safeTarget.hasEffect(
									MobEffects.FIRE_RESISTANCE)
							&& safeTarget.hasEffect(
									MobEffects.DAMAGE_RESISTANCE)
							&& safeTarget
									.getDeltaMovement().y
									> 0.0D,
					"Normal Frosted Arrow caused health damage or lacked visible chill rescue");

			FrostedArcherProbe melee =
					new FrostedArcherProbe(
							helper.getLevel());
			melee.moveTo(center.getX() - 2.5D,
					center.getY(),
					center.getZ() + 0.5D);
			Pig safeMelee =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, safeMelee != null,
					"Could not create safe Frosted Archer melee target");
			safeMelee.setHealth(10.0F);
			require(helper,
					melee.doHurtTarget(safeMelee)
							&& close(safeMelee
									.getHealth(), 10.0D)
							&& safeMelee.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN),
					"Normal Bow-less Frosted Archer bypassed the no-damage contract");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardArrowTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, hardArrowTarget != null,
					"Could not create Hard Frosted Archer arrow target");
			hardArrowTarget.setHealth(10.0F);
			hardArrowTarget.invulnerableTime = 0;
			require(helper,
					hardArrowTarget.hurt(
							DamageSource.arrow(
									fired, archer),
							4.0F)
							&& close(hardArrowTarget
									.getHealth(), 6.0D),
					"Hard Frosted Arrow no longer permits exact incoming damage");
			Pig hardMeleeTarget =
					EntityType.PIG.create(
							helper.getLevel());
			require(helper, hardMeleeTarget != null,
					"Could not create Hard Frosted Archer melee target");
			hardMeleeTarget.setHealth(10.0F);
			hardMeleeTarget.invulnerableTime = 0;
			require(helper,
					melee.doHurtTarget(
							hardMeleeTarget)
							&& close(hardMeleeTarget
									.getHealth(), 8.0D),
					"Hard Frosted Archer lost the exact two-point Skeleton melee attack");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			ServerLevelAccessor openSnow =
					controlledFrostedArcherAccessor(
							helper.getLevel(), center,
							true);
			ServerLevelAccessor coveredSnow =
					controlledFrostedArcherAccessor(
							helper.getLevel(), center,
							false);
			require(helper,
					FrostedArcher
							.checkFrostedArcherSpawnRules(
									CakeWorldEntities
											.FROSTED_ARCHER
											.get(),
									coveredSnow,
									MobSpawnType.SPAWNER,
									center,
									new Random(1978L))
							&& FrostedArcher
									.checkFrostedArcherSpawnRules(
											CakeWorldEntities
													.FROSTED_ARCHER
													.get(),
											openSnow,
											MobSpawnType.NATURAL,
											center,
											new Random(1978L))
							&& !FrostedArcher
									.checkFrostedArcherSpawnRules(
											CakeWorldEntities
													.FROSTED_ARCHER
													.get(),
											coveredSnow,
											MobSpawnType.NATURAL,
											center,
											new Random(1978L)),
					"Frosted Archer lost Stray's spawner bypass or exact top-of-powder-snow sky rule");

			BlockPos cakeWorldPos =
					findCakeWorldBiomePosition(
							helper, center, 256);
			require(helper, cakeWorldPos != null,
					"Could not locate CakeWorld terrain for literal Stray conversion");
			Stray literal =
					EntityType.STRAY.create(
							helper.getLevel());
			vehicle = EntityType.SPIDER.create(
					helper.getLevel());
			require(helper,
					literal != null && vehicle != null,
					"Could not create literal Stray rider fixture");
			literal.moveTo(
					cakeWorldPos.getX() + 0.5D,
					cakeWorldPos.getY() + 2.0D,
					cakeWorldPos.getZ() + 0.5D);
			vehicle.moveTo(literal.getX(),
					literal.getY(), literal.getZ());
			literal.setHealth(13.0F);
			literal.setCustomName(
					new TextComponent(
							"Iced Sharpshooter"));
			literal.setPersistenceRequired();
			literal.setNoAi(true);
			literal.setItemSlot(
					EquipmentSlot.MAINHAND,
					new ItemStack(Items.BOW));
			literal.setItemSlot(
					EquipmentSlot.HEAD,
					new ItemStack(
							Items.CHAINMAIL_HELMET));
			helper.getLevel()
					.addFreshEntity(vehicle);
			helper.getLevel()
					.addFreshEntity(literal);
			literal.startRiding(vehicle, true);
			converted = CakeWorldStrayReplacement
					.replaceIfInCakeWorldBiome(
							helper.getLevel(), literal);
			require(helper,
					converted != null
							&& converted.getType()
									== CakeWorldEntities
											.FROSTED_ARCHER
											.get()
							&& close(converted
									.getHealth(), 13.0D)
							&& converted.hasCustomName()
							&& "Iced Sharpshooter"
									.equals(converted
											.getCustomName()
											.getString())
							&& converted
									.isPersistenceRequired()
							&& converted.isNoAi()
							&& converted
									.getMainHandItem()
									.is(Items.BOW)
							&& converted.getItemBySlot(
									EquipmentSlot.HEAD)
									.is(Items
											.CHAINMAIL_HELMET)
							&& converted.getVehicle()
									== vehicle
							&& vehicle.getPassengers()
									.contains(converted)
							&& literal.isRemoved(),
					"Fresh Stray conversion lost Frosted Archer state, equipment or mount");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			FrostedArcherProbe peaceful =
					new FrostedArcherProbe(
							helper.getLevel());
			peaceful.moveTo(center.getX() + 0.5D,
					center.getY(),
					center.getZ() + 0.5D);
			helper.getLevel()
					.addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Frosted Archer lost vanilla Monster removal");
		} finally {
			if (!archer.isRemoved()) {
				archer.discard();
			}
			if (converted != null
					&& !converted.isRemoved()) {
				converted.discard();
			}
			if (vehicle != null
					&& !vehicle.isRemoved()) {
				vehicle.discard();
			}
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(
						Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.STRAY
											|| spawn.type
													== CakeWorldEntities
															.FROSTED_ARCHER
															.get()),
					"Stray ecology was invented before Ice-Cream Tundra in "
							+ biomeId);
		}

		TagKey<EntityType<?>> skeletons =
				TagKey.create(
						Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft",
								"skeletons"));
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.FROSTED_ARCHER
										.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.FROSTED_ARCHER
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldEntities
								.FROSTED_ARCHER.get()
								.is(skeletons)
						&& CakeWorldItems
								.FROSTED_ARCHER_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.FROSTED_ARCHER
												.get())
								== SoundEvents
										.PARROT_IMITATE_STRAY
						&& archer.getLootTable().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/frosted_archer")),
				"Frosted Archer lost placement, Skeleton tag, egg, Stray mimic or loot route");

		ServerPlayer advancementPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2055"),
								"CakeWorldFrostedArcherRoleTest"));
		advancementPlayer.setPos(center.getX() - 60.0D,
				center.getY(), center.getZ());
		archer.setPos(center.getX(),
				center.getY(), center.getZ());
		Arrow playerArrow = new Arrow(
				helper.getLevel(),
				advancementPlayer);
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(
						archer,
						DamageSource.arrow(
								playerArrow,
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:stray");
		Advancement sniper = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft:adventure/sniper_duel"));
		require(helper,
				sniper != null
						&& !advancementPlayer
								.getAdvancements()
								.getOrStartProgress(sniper)
								.getCriterion(
										"killed_skeleton")
								.isDone(),
				"Frosted Archer incorrectly inherited Skeleton-only Sniper Duel credit");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void fudgeSkatersKeepCompleteStriderRidingAndHotFudgeTraversal(
			GameTestHelper helper) {
		FudgeSkaterProbe skater =
				new FudgeSkaterProbe(helper.getLevel());
		skater.seedRandom(1978L);
		int experience = skater.getExperienceValue();
		require(helper,
				skater instanceof Strider
						&& skater instanceof Animal
						&& skater.getType()
								== CakeWorldEntities
										.FUDGE_SKATER.get()
						&& skater.getType().getCategory()
								== MobCategory.CREATURE
						&& skater.getType()
								.fireImmune()
						&& !skater.isOnFire()
						&& skater.isSensitiveToWater()
						&& skater.blocksBuilding()
						&& close(skater.getMaxHealth(),
								20.0D)
						&& close(skater.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.175D)
						&& close(skater.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								16.0D)
						&& skater.getAttribute(
								Attributes.ATTACK_DAMAGE)
								== null
						&& close(skater.getDimensions(
								Pose.STANDING).width,
								0.9D)
						&& close(skater.getDimensions(
								Pose.STANDING).height,
								1.7D)
						&& skater.getType()
								.clientTrackingRange() == 10
						&& experience >= 1
						&& experience <= 3
						&& close(skater.getPathfindingMalus(
								net.minecraft.world.level
										.pathfinder
										.BlockPathTypes.WATER),
								-1.0D)
						&& close(skater.getPathfindingMalus(
								net.minecraft.world.level
										.pathfinder
										.BlockPathTypes.LAVA),
								0.0D),
				"Fudge Skater lost the genuine passive fire-immune Strider body, attributes, tracking or path roles");
		require(helper,
				skater.goalPriority("PanicGoal") == 1
						&& skater.goalPriority(
								"BreedGoal") == 2
						&& skater.goalPriority(
								"TemptGoal") == 3
						&& skater.goalPriority(
								"FudgeSkaterGoToHotFluidGoal")
								== 4
						&& skater.countGoalsNamed(
								"StriderGoToLavaGoal")
								== 0
						&& skater.goalPriority(
								"FollowParentGoal") == 5
						&& skater.goalPriority(
								"RandomStrollGoal") == 7
						&& skater.countGoalsNamed(
								"LookAtPlayerGoal") == 2
						&& skater.goalPriority(
								"RandomLookAroundGoal")
								== 8
						&& skater.targetGoalCount() == 0
						&& skater.ambientSound()
								== SoundEvents
										.STRIDER_AMBIENT
						&& skater.hurtSound()
								== SoundEvents
										.STRIDER_HURT
						&& skater.deathSound()
								== SoundEvents
										.STRIDER_DEATH,
				"Fudge Skater lost exact Strider goals, passive targeting or sounds");

		BlockPos hotPos = helper.absolutePos(
				new BlockPos(1, 2, 1));
		BlockPos coldPos = hotPos.offset(4, 3, 0);
		helper.getLevel().setBlock(hotPos,
				CakeWorldFluids.HOT_FUDGE_BLOCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(hotPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(coldPos.below(),
				CakeWorldBlocks.BISCUIT_STONE.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(coldPos,
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				helper.getLevel().getFluidState(hotPos)
								.is(FluidTags.LAVA)
						&& helper.getLevel()
								.getBlockState(hotPos)
								.is(BlockTags
										.STRIDER_WARM_BLOCKS)
						&& skater.canStandOnFluid(
								helper.getLevel()
										.getFluidState(
												hotPos))
						&& close(skater
								.getWalkTargetValue(
										hotPos,
										helper.getLevel()),
								10.0D)
						&& skater.getNavigation()
								.isStableDestination(
										hotPos)
						&& skater.hotFluidGoalAccepts(
								helper.getLevel(),
								hotPos)
						&& FudgeSkater
								.checkFudgeSkaterSpawnRules(
										CakeWorldEntities
												.FUDGE_SKATER
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										hotPos,
										new Random(1978L)),
				"Hot Fudge did not participate in every Strider lava movement, seek, stable-destination, warmth or spawn seam");

		skater.moveTo(hotPos.getX() + 0.5D,
				hotPos.getY() + 0.1D,
				hotPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(skater);
		skater.tick();
		require(helper,
				!skater.isSuffocating()
						&& skater.isInLava()
						&& close(skater.getMoveSpeed(),
								0.175D)
						&& close(skater
								.getSteeringSpeed(),
								0.09625D)
						&& skater.stepSound()
								== SoundEvents
										.STRIDER_STEP_LAVA,
				"Fudge Skater did not become warm, float-state aware or use exact warm speeds and lava steps in Hot Fudge");
		FudgeSkaterProbe coldSkater =
				new FudgeSkaterProbe(helper.getLevel());
		coldSkater.moveTo(coldPos.getX() + 0.5D,
				coldPos.getY(),
				coldPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(coldSkater);
		coldSkater.tick();
		require(helper,
				coldSkater.isSuffocating()
						&& !coldSkater.isInLava()
						&& close(coldSkater.getMoveSpeed(),
								0.1155D)
						&& close(coldSkater
								.getSteeringSpeed(),
								0.04025D)
						&& coldSkater.stepSound()
								== SoundEvents.STRIDER_STEP,
				"Fudge Skater lost exact cold-state speed, steering or dry step cue: suffocating="
						+ coldSkater.isSuffocating()
						+ ", in_lava="
						+ coldSkater.isInLava()
						+ ", move="
						+ coldSkater.getMoveSpeed()
						+ ", steering="
						+ coldSkater.getSteeringSpeed()
						+ ", step="
						+ coldSkater.stepSound());

		FudgeSkater child = skater.getBreedOffspring(
				helper.getLevel(), skater);
		require(helper,
				child != null
						&& child.getType()
								== CakeWorldEntities
										.FUDGE_SKATER.get()
						&& skater.isFood(new ItemStack(
								Items.WARPED_FUNGUS))
						&& !skater.isFood(new ItemStack(
								Items.WARPED_FUNGUS_ON_A_STICK)),
				"Fudge Skater lost same-family offspring or exact Warped Fungus food role");

		skater.setAge(0);
		skater.equipSaddle(SoundSource.NEUTRAL);
		require(helper,
				skater.isSaddled()
						&& skater.isSaddleable(),
				"Fudge Skater lost adult saddle state");
		CompoundTag saddleState =
				skater.saveWithoutId(new CompoundTag());
		FudgeSkater saddleReload =
				CakeWorldEntities.FUDGE_SKATER
						.get().create(helper.getLevel());
		require(helper, saddleReload != null,
				"Could not create Fudge Skater saddle reload fixture");
		saddleReload.load(saddleState);
		require(helper, saddleReload.isSaddled(),
				"Fudge Skater lost exact Saddle NBT");

		ServerPlayer rider = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2057"),
						"CakeWorldFudgeSkaterRoleTest"));
		rider.connection =
				new ServerGamePacketListenerImpl(
						helper.getLevel().getServer(),
						new Connection(
								PacketFlow.CLIENTBOUND),
						rider);
		rider.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(
						Items.WARPED_FUNGUS_ON_A_STICK));
		require(helper,
				rider.startRiding(skater, true)
						&& skater
								.getControllingPassenger()
								== rider
						&& skater.canBeControlledByRider(),
				"Fudge Skater lost vanilla rider or Warped-Fungus-on-a-Stick directional control");
		PlayerInteractEvent.RightClickItem boostEvent =
				new PlayerInteractEvent.RightClickItem(
						rider,
						InteractionHand.MAIN_HAND);
		FudgeSkaterRideCompatibility
				.onRightClickItem(boostEvent);
		require(helper,
				boostEvent.isCanceled()
						&& boostEvent
								.getCancellationResult()
								== InteractionResult.SUCCESS
						&& rider.getMainHandItem()
								.getDamageValue() == 1
						&& !skater.boost(),
				"Fudge Skater did not bridge vanilla's literal-Strider boost gate or exact durability cost");
		requireCriterion(helper, rider,
				"minecraft:nether/ride_strider",
				"used_warped_fungus_on_a_stick");

		skater.moveTo(hotPos.getX() + 0.5D,
				hotPos.getY() + 0.1D,
				hotPos.getZ() + 0.5D);
		skater.tick();
		rider.setPos(skater.getX(),
				skater.getY(), skater.getZ());
		FudgeSkaterRideCompatibility
				.trackHotFluidRide(rider);
		rider.setPos(skater.getX() + 50.0D,
				skater.getY(), skater.getZ());
		FudgeSkaterRideCompatibility
				.trackHotFluidRide(rider);
		requireCriterion(helper, rider,
				"minecraft:nether/ride_strider_in_overworld_lava",
				"ride_entity_distance");
		VanillaRoleAdvancements.creditBredRole(
				rider, child.getType());
		requireCriterion(helper, rider,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:strider");
		rider.stopRiding();

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(
						helper, hotPos, 256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Strider conversion");
		Strider literal =
				EntityType.STRIDER.create(
						helper.getLevel());
		Boat vehicle =
				EntityType.BOAT.create(
						helper.getLevel());
		Pig passenger =
				EntityType.PIG.create(
						helper.getLevel());
		require(helper,
				literal != null
						&& vehicle != null
						&& passenger != null,
				"Could not create literal Strider relationship fixtures");
		literal.moveTo(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		vehicle.moveTo(literal.getX(),
				literal.getY(), literal.getZ());
		passenger.moveTo(literal.getX(),
				literal.getY(), literal.getZ());
		literal.setHealth(13.0F);
		literal.setCustomName(
				new TextComponent("Fudge Ferryman"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.equipSaddle(null);
		helper.getLevel().addFreshEntity(vehicle);
		helper.getLevel().addFreshEntity(literal);
		helper.getLevel().addFreshEntity(passenger);
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		FudgeSkater converted =
				CakeWorldStriderReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& converted.getType()
								== CakeWorldEntities
										.FUDGE_SKATER.get()
						&& close(converted.getHealth(),
								13.0D)
						&& converted.hasCustomName()
						&& "Fudge Ferryman".equals(
								converted
										.getCustomName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isSaddled()
						&& converted.getVehicle()
								== vehicle
						&& converted.getPassengers()
								.contains(passenger)
						&& passenger.getVehicle()
								== converted
						&& literal.isRemoved(),
				"Fresh literal Strider conversion lost Fudge Skater state, saddle, vehicle or passenger");

		FudgeSkaterProbe piglinJockey =
				new FudgeSkaterProbe(helper.getLevel());
		piglinJockey.moveTo(
				cakeWorldPos.getX() + 4.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		piglinJockey.seedRandom(0L);
		piglinJockey.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						piglinJockey.blockPosition()),
				MobSpawnType.NATURAL, null, null);
		require(helper,
				piglinJockey.isSaddled()
						&& piglinJockey
								.getFirstPassenger()
								instanceof ZombifiedPiglin
						&& ((ZombifiedPiglin)piglinJockey
								.getFirstPassenger())
								.getMainHandItem()
								.is(Items
										.WARPED_FUNGUS_ON_A_STICK),
				"Fudge Skater lost exact seeded one-in-thirty Zombified-Piglin jockey, saddle or control item");

		FudgeSkaterProbe babyJockey =
				new FudgeSkaterProbe(helper.getLevel());
		babyJockey.moveTo(
				cakeWorldPos.getX() + 8.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		babyJockey.seedRandom(3L);
		babyJockey.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						babyJockey.blockPosition()),
				MobSpawnType.NATURAL, null, null);
		require(helper,
				babyJockey.getFirstPassenger()
								instanceof Strider
						&& babyJockey
								.getFirstPassenger()
								.getType()
								== EntityType.STRIDER
						&& ((Strider)babyJockey
								.getFirstPassenger())
								.isBaby(),
				"Fudge Skater lost exact seeded subsequent one-in-ten baby-Strider jockey branch");
		Strider literalBaby =
				(Strider)babyJockey
						.getFirstPassenger();
		FudgeSkater convertedBaby =
				CakeWorldStriderReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literalBaby);
		require(helper,
				convertedBaby != null
						&& convertedBaby.isBaby()
						&& convertedBaby.getVehicle()
								== babyJockey
						&& babyJockey
								.getPassengers()
								.contains(
										convertedBaby),
				"Inherited baby-Strider jockey did not hand off to a baby Fudge Skater");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(
						Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Missing CakeWorld biome "
							+ biomeId);
			List<MobSpawnSettings.SpawnerData>
					skaterSpawns = biome
							.getMobSettings()
							.getMobs(
									MobCategory.CREATURE)
							.unwrap().stream()
							.filter(spawn ->
									spawn.type
											== CakeWorldEntities
													.FUDGE_SKATER
													.get())
							.toList();
			require(helper,
					biome.getMobSettings()
							.getMobs(
									MobCategory.CREATURE)
							.unwrap().stream()
							.noneMatch(spawn ->
									spawn.type
											== EntityType.STRIDER),
					"Literal Strider leaked into CakeWorld spawning in "
							+ biomeId);
			if (biomeId.equals(
					CakeWorldBiomes.FUDGE_WASTES
							.getId())) {
				require(helper,
						skaterSpawns.size() == 1
								&& skaterSpawns
										.get(0).getWeight()
										.asInt() == 60
								&& skaterSpawns
										.get(0).minCount
										== 1
								&& skaterSpawns
										.get(0).maxCount
										== 2,
						"Fudge Wastes lost exact inherited 60/1-2 Strider ecology: "
								+ skaterSpawns);
			} else {
				require(helper,
						skaterSpawns.isEmpty(),
						"Fudge Skater ecology was invented outside Fudge Wastes in "
								+ biomeId);
			}
		}

		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.FUDGE_SKATER
										.get())
								== SpawnPlacements.Type
										.IN_LAVA
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.FUDGE_SKATER
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.FUDGE_SKATER_SPAWN_EGG
								.isPresent()
						&& skater.getLootTableId()
								.equals(
										new ResourceLocation(
												CakeWorld.MODID,
												"entities/fudge_skater"))
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.FUDGE_SKATER
												.get())
								== null,
				"Fudge Skater lost exact placement, egg, String-loot route or deliberate no-mimic role");

		if (child != null) {
			child.discard();
		}
		if (!skater.isRemoved()) {
			skater.discard();
		}
		if (!coldSkater.isRemoved()) {
			coldSkater.discard();
		}
		saddleReload.discard();
		if (!converted.isRemoved()) {
			converted.discard();
		}
		if (!vehicle.isRemoved()) {
			vehicle.discard();
		}
		if (!passenger.isRemoved()) {
			passenger.discard();
		}
		piglinJockey.discard();
		babyJockey.discard();
		if (convertedBaby != null
				&& !convertedBaby.isRemoved()) {
			convertedBaby.discard();
		}
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void sprinkleLlamasKeepTraderCaravansPacksDefenceAndDespawn(
			GameTestHelper helper) {
		SprinkleLlamaProbe llama =
				new SprinkleLlamaProbe(
						helper.getLevel());
		llama.seedRandom(1978L);
		int experience = llama.getExperienceValue();
		require(helper,
				llama instanceof TraderLlama
						&& llama instanceof Llama
						&& llama.getType()
								== CakeWorldEntities
										.SPRINKLE_LLAMA
										.get()
						&& llama.getType().getCategory()
								== MobCategory.CREATURE
						&& !llama.getType().fireImmune()
						&& close(llama.getMaxHealth(),
								53.0D)
						&& close(llama
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.175D)
						&& close(llama
								.getAttributeValue(
										Attributes
												.JUMP_STRENGTH),
								0.5D)
						&& close(llama
								.getAttributeValue(
										Attributes
												.FOLLOW_RANGE),
								40.0D)
						&& llama.getAttribute(
								Attributes.ATTACK_DAMAGE)
								== null
						&& close(llama
								.getDimensions(
										Pose.STANDING)
								.width, 0.9D)
						&& close(llama
								.getDimensions(
										Pose.STANDING)
								.height, 1.87D)
						&& llama.getType()
								.clientTrackingRange()
								== 10
						&& close(llama.maxUpStep, 1.0D)
						&& llama
								.getMaxSpawnClusterSize()
								== 6
						&& llama.getMaxTemper() == 30
						&& llama.isTraderLlama()
						&& !llama.isSaddleable()
						&& !llama
								.canBeControlledByRider()
						&& llama.canWearArmor()
						&& experience >= 1
						&& experience <= 3,
				"Sprinkle Llama lost the genuine Trader Llama body, attributes, pack, tracking or passive role");
		require(helper,
				llama.hasGoalAt("FloatGoal", 0)
						&& llama.hasGoalAt(
								"RunAroundLikeCrazyGoal",
								1)
						&& llama.hasGoalAt(
								"MeringueLlamaFollowCaravanGoal",
								2)
						&& llama.countGoalsNamed(
								"LlamaFollowCaravanGoal")
								== 0
						&& llama.hasGoalAt(
								"RangedAttackGoal", 3)
						&& llama
								.countGoalsNamed(
										"PanicGoal")
								== 2
						&& llama.hasGoalAt(
								"PanicGoal", 1)
						&& llama.hasGoalAt(
								"PanicGoal", 3)
						&& llama.hasGoalAt(
								"BreedGoal", 4)
						&& llama.hasGoalAt(
								"TemptGoal", 5)
						&& llama.hasGoalAt(
								"FollowParentGoal", 6)
						&& llama.hasGoalAt(
								"WaterAvoidingRandomStrollGoal",
								7)
						&& llama.hasGoalAt(
								"LookAtPlayerGoal", 8)
						&& llama.hasGoalAt(
								"RandomLookAroundGoal",
								9)
						&& llama.hasTargetGoalAt(
								"LlamaHurtByTargetGoal",
								1)
						&& llama.hasTargetGoalAt(
								"TraderLlamaDefendWanderingTraderGoal",
								1)
						&& llama.hasTargetGoalAt(
								"LlamaAttackWolfGoal",
								2)
						&& llama.ambientSound()
								== SoundEvents
										.LLAMA_AMBIENT
						&& llama.angrySound()
								== SoundEvents
										.LLAMA_ANGRY
						&& llama.hurtSound()
								== SoundEvents.LLAMA_HURT
						&& llama.deathSound()
								== SoundEvents
										.LLAMA_DEATH
						&& llama.eatingSound()
								== SoundEvents.LLAMA_EAT,
				"Sprinkle Llama lost exact Llama goals, Trader panic/defence additions or sounds");
		require(helper,
				llama.isFood(new ItemStack(
								Items.WHEAT))
						&& llama.isFood(new ItemStack(
								Items.HAY_BLOCK))
						&& !llama.isFood(new ItemStack(
								CakeWorldItems
										.BOILED_SWEET
										.get())),
				"Sprinkle Llama changed the exact Wheat and Hay Block diet");

		BlockPos anchor = helper.absolutePos(
				new BlockPos(2, 3, 2));
		llama.setNoAi(true);
		llama.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(llama);
		Pig target = EntityType.PIG.create(
				helper.getLevel());
		require(helper, target != null,
				"Could not create Sprinkle Llama spit target");
		target.setPos(anchor.getX() + 3.0D,
				anchor.getY(), anchor.getZ());
		helper.getLevel().addFreshEntity(target);

		CompoundTag genes = new CompoundTag();
		genes.putInt("Strength", 5);
		genes.putInt("Variant", 3);
		llama.readAdditionalSaveData(genes);
		UUID owner = UUID.fromString(
				"1978feed-feed-4bad-babe-1978feed2058");
		ServerPlayer player = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(owner,
						"CakeWorldSprinkleLlamaRoleTest"));
		llama.setTamed(true);
		llama.setOwnerUUID(owner);
		llama.setDespawnDelay(1234);
		require(helper,
				llama.getSlot(499).set(
						new ItemStack(Items.CHEST))
						&& llama.getSlot(401).set(
								new ItemStack(
										Items.CYAN_CARPET))
						&& llama.getSlot(500).set(
								new ItemStack(
										CakeWorldItems
												.BOILED_SWEET
												.get(), 3))
						&& llama.getSlot(514).set(
								new ItemStack(
										CakeWorldItems
												.MINT_WAFER
												.get(), 2))
						&& !llama.getSlot(515).set(
								new ItemStack(
										Items.DIAMOND))
						&& llama.hasChest()
						&& llama.getStrength() == 5
						&& llama
								.getInventoryColumns()
								== 5
						&& llama.getVariant() == 3
						&& llama.getSwag()
								== DyeColor.CYAN
						&& llama.isWearingArmor(),
				"Sprinkle Llama lost strength-five storage, edge pack slot or carpet decor");
		CompoundTag saved = llama.saveWithoutId(
				new CompoundTag());
		SprinkleLlama restored =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(
								helper.getLevel());
		require(helper, restored != null,
				"Could not create Sprinkle Llama reload fixture");
		restored.load(saved);
		require(helper,
				restored.isTraderLlama()
						&& restored.isTamed()
						&& owner.equals(
								restored.getOwnerUUID())
						&& restored.hasChest()
						&& restored.getStrength() == 5
						&& restored
								.getInventoryColumns()
								== 5
						&& restored.getVariant() == 3
						&& restored.getSwag()
								== DyeColor.CYAN
						&& restored.getSlot(401).get()
								.is(Items.CYAN_CARPET)
						&& restored.getSlot(500).get()
								.is(CakeWorldItems
										.BOILED_SWEET
										.get())
						&& restored.getSlot(500).get()
								.getCount() == 3
						&& restored.getSlot(514).get()
								.is(CakeWorldItems
										.MINT_WAFER.get())
						&& restored.getSlot(514).get()
								.getCount() == 2
						&& restored
								.saveWithoutId(
										new CompoundTag())
								.getInt("DespawnDelay")
								== 1234,
				"Sprinkle Llama reload lost Trader delay, owner, genes, chest, carpet or pack inventory");

		SprinkleLlamaProbe eventLlama =
				new SprinkleLlamaProbe(
						helper.getLevel());
		eventLlama.setAge(-24000);
		SpawnGroupData eventGroup =
				eventLlama.finalizeSpawn(
						helper.getLevel(),
						helper.getLevel()
								.getCurrentDifficultyAt(
										anchor),
						MobSpawnType.EVENT,
						null, null);
		require(helper,
				eventGroup != null
						&& !eventLlama.isBaby()
						&& eventLlama.getStrength()
								>= 1
						&& eventLlama.getStrength()
								<= 5
						&& eventLlama.getVariant()
								>= 0
						&& eventLlama.getVariant()
								<= 3,
				"Sprinkle Llama lost the adult EVENT-spawn and randomized Llama group contract");

		WanderingTrader trader =
				EntityType.WANDERING_TRADER
						.create(helper.getLevel());
		Pig attacker =
				EntityType.PIG.create(
						helper.getLevel());
		SprinkleLlamaProbe traderBound =
				new SprinkleLlamaProbe(
						helper.getLevel());
		require(helper,
				trader != null && attacker != null,
				"Could not create trader-defence fixtures");
		trader.setPos(anchor.getX() + 8.0D,
				anchor.getY(), anchor.getZ());
		attacker.setPos(trader.getX() + 2.0D,
				trader.getY(), trader.getZ());
		traderBound.setPos(trader.getX() + 1.0D,
				trader.getY(), trader.getZ());
		traderBound.setNoAi(true);
		helper.getLevel().addFreshEntity(trader);
		helper.getLevel().addFreshEntity(attacker);
		helper.getLevel().addFreshEntity(
				traderBound);
		traderBound.setLeashedTo(trader, false);
		trader.setDespawnDelay(50);
		traderBound.setDespawnDelay(999);
		traderBound.aiStep();
		require(helper,
				traderBound.despawnDelay() == 49,
				"Trader-bound Sprinkle Llama did not synchronize to trader delay minus one");
		trader.tickCount = 20;
		trader.setLastHurtByMob(attacker);
		var defendGoal = traderBound
				.targetSelector.getAvailableGoals()
				.stream()
				.map(WrappedGoal::getGoal)
				.filter(goal -> goal.getClass()
						.getSimpleName().equals(
								"TraderLlamaDefendWanderingTraderGoal"))
				.findFirst().orElse(null);
		require(helper,
				defendGoal != null
						&& defendGoal.canUse(),
				"Sprinkle Llama did not notice its leashed trader's attacker");
		defendGoal.start();
		require(helper,
				traderBound.getTarget() == attacker,
				"Sprinkle Llama defence goal did not target the trader's attacker");

		SprinkleLlama mountLlama =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(
								helper.getLevel());
		require(helper, mountLlama != null,
				"Could not create trader-leash mount fixture");
		mountLlama.setPos(trader.getX() + 3.0D,
				trader.getY(), trader.getZ());
		mountLlama.setTamed(true);
		mountLlama.setOwnerUUID(owner);
		helper.getLevel().addFreshEntity(mountLlama);
		mountLlama.setLeashedTo(trader, false);
		player.setItemInHand(
				InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		player.interactOn(
				mountLlama,
				InteractionHand.MAIN_HAND);
		require(helper,
				player.getVehicle() == null,
				"Trader-leashed Sprinkle Llama incorrectly allowed a rider");
		mountLlama.dropLeash(true, false);
		InteractionResult mounted = player.interactOn(
				mountLlama,
				InteractionHand.MAIN_HAND);
		require(helper,
				mounted.consumesAction()
						&& player.getVehicle()
								== mountLlama
						&& !mountLlama
								.canBeControlledByRider(),
				"Unleashed tame Sprinkle Llama lost its mountable but uncontrollable role");
		player.stopRiding();

		SprinkleLlamaProbe tamedProtected =
				new SprinkleLlamaProbe(
						helper.getLevel());
		tamedProtected.setNoAi(true);
		tamedProtected.setTamed(true);
		tamedProtected.setDespawnDelay(1);
		helper.getLevel().addFreshEntity(
				tamedProtected);
		tamedProtected.aiStep();
		require(helper,
				!tamedProtected.isRemoved()
						&& tamedProtected
								.despawnDelay() == 1,
				"Tamed Sprinkle Llama did not retain the exact despawn exemption");

		SprinkleLlamaProbe leashedProtected =
				new SprinkleLlamaProbe(
						helper.getLevel());
		leashedProtected.setNoAi(true);
		leashedProtected.setDespawnDelay(1);
		helper.getLevel().addFreshEntity(
				leashedProtected);
		leashedProtected.setLeashedTo(
				player, false);
		leashedProtected.aiStep();
		require(helper,
				!leashedProtected.isRemoved()
						&& leashedProtected
								.despawnDelay() == 1,
				"Non-trader leash did not retain the exact Sprinkle Llama despawn exemption");

		SprinkleLlamaProbe passengerProtected =
				new SprinkleLlamaProbe(
						helper.getLevel());
		ServerPlayer passengerPlayer =
				new ServerPlayer(
						helper.getLevel()
								.getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed3058"),
								"CakeWorldSprinklePassengerTest"));
		passengerProtected.setNoAi(true);
		passengerProtected.setDespawnDelay(1);
		helper.getLevel().addFreshEntity(
				passengerProtected);
		require(helper,
				passengerPlayer.startRiding(
						passengerProtected, true),
				"Could not create one-player Sprinkle Llama passenger exemption");
		passengerProtected.aiStep();
		require(helper,
				!passengerProtected.isRemoved()
						&& passengerProtected
								.despawnDelay() == 1,
				"Exactly one player passenger did not prevent Sprinkle Llama despawn");
		passengerPlayer.stopRiding();

		SprinkleLlamaProbe expiring =
				new SprinkleLlamaProbe(
						helper.getLevel());
		expiring.setNoAi(true);
		expiring.setDespawnDelay(1);
		helper.getLevel().addFreshEntity(expiring);
		expiring.aiStep();
		require(helper, expiring.isRemoved(),
				"Unprotected zero-delay Sprinkle Llama did not discard exactly like a Trader Llama");

		SprinkleLlama mate =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(
								helper.getLevel());
		require(helper, mate != null,
				"Could not create Sprinkle Llama mate");
		CompoundTag mateGenes =
				new CompoundTag();
		mateGenes.putInt("Strength", 2);
		mateGenes.putInt("Variant", 1);
		mate.readAdditionalSaveData(mateGenes);
		mate.setTamed(true);
		mate.setOwnerUUID(owner);
		mate.setAge(0);
		mate.setHealth(mate.getMaxHealth());
		llama.setAge(0);
		llama.setHealth(llama.getMaxHealth());
		llama.setInLove(player);
		mate.setInLove(player);
		require(helper, llama.canMate(mate),
				"Two tame adult Sprinkle Llamas could not mate");
		Llama child = llama.getBreedOffspring(
				helper.getLevel(), mate);
		require(helper,
				child instanceof SprinkleLlama
						&& child.getType()
								== CakeWorldEntities
										.SPRINKLE_LLAMA
										.get()
						&& child.isTraderLlama()
						&& child.getStrength() >= 1
						&& child.getStrength() <= 5
						&& (child.getVariant() == 3
								|| child.getVariant()
										== 1)
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH)
								>= 40.0D
						&& child.getAttributeBaseValue(
								Attributes.MAX_HEALTH)
								<= 46.0D,
				"Sprinkle Llama breeding leaked a literal Trader Llama or lost physical, strength or variant inheritance");
		VanillaRoleAdvancements.creditBredRole(
				player, child.getType());
		requireCriterion(helper, player,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:llama");

		SprinkleLlama caravanLeader =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(
								helper.getLevel());
		SprinkleLlama caravanFollower =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(
								helper.getLevel());
		require(helper, caravanLeader != null
						&& caravanFollower != null,
				"Could not create Sprinkle Llama caravan fixtures");
		caravanLeader.setNoAi(true);
		caravanFollower.setNoAi(true);
		caravanLeader.setPos(anchor.getX(),
				anchor.getY(),
				anchor.getZ() + 30.0D);
		caravanFollower.setPos(
				anchor.getX() + 5.0D,
				anchor.getY(),
				anchor.getZ() + 30.0D);
		helper.getLevel().addFreshEntity(
				caravanLeader);
		helper.getLevel().addFreshEntity(
				caravanFollower);
		caravanLeader.setLeashedTo(
				player, false);
		long literalGoals = caravanFollower
				.goalSelector.getAvailableGoals()
				.stream()
				.filter(wrapped -> wrapped.getGoal()
						instanceof LlamaFollowCaravanGoal)
				.count();
		MeringueLlamaFollowCaravanGoal caravanGoal =
				caravanFollower.goalSelector
						.getAvailableGoals().stream()
						.map(WrappedGoal::getGoal)
						.filter(MeringueLlamaFollowCaravanGoal
								.class::isInstance)
						.map(MeringueLlamaFollowCaravanGoal
								.class::cast)
						.findFirst().orElse(null);
		require(helper,
				literalGoals == 0
						&& caravanGoal != null
						&& caravanGoal.canUse()
						&& caravanFollower.inCaravan()
						&& caravanFollower
								.getCaravanHead()
								== caravanLeader
						&& caravanLeader
								.hasCaravanTail(),
				"Sprinkle Llama retained the literal-type caravan goal or failed to form a custom caravan");
		caravanFollower.leaveCaravan();

		llama.performRangedAttack(target, 1.0F);
		LlamaSpit spit = helper.getLevel()
				.getEntitiesOfClass(
						LlamaSpit.class,
						new AABB(anchor).inflate(10.0D))
				.stream()
				.filter(projectile ->
						projectile.getOwner() == llama)
				.findFirst().orElse(null);
		require(helper, spit != null,
				"Sprinkle Llama did not retain a visible vanilla Llama spit projectile");
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer()
						.setDifficulty(
								safeDifficulty,
								true);
				target.removeAllEffects();
				target.setHealth(10.0F);
				target.invulnerableTime = 0;
				target.setSecondsOnFire(5);
				target.fallDistance = 12.0F;
				target.setDeltaMovement(Vec3.ZERO);
				target.hurt(
						DamageSource.indirectMobAttack(
								spit, llama)
								.setProjectile(),
						1.0F);
				require(helper,
						close(target.getHealth(),
								10.0D)
								&& !target.isOnFire()
								&& target.fallDistance
										== 0.0F
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.getEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
										.getAmplifier()
										== 1
								&& target.hasEffect(
										MobEffects
												.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4
								&& target
										.getDeltaMovement().y
										> 0.0D,
						safeDifficulty
								+ " Sprinkle spit caused health damage or lost its sticky rescue envelope");
			}
			helper.getLevel().getServer()
					.setDifficulty(
							Difficulty.HARD, true);
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.invulnerableTime = 0;
			target.setDeltaMovement(Vec3.ZERO);
			target.hurt(
					DamageSource.indirectMobAttack(
							spit, llama)
							.setProjectile(),
					1.0F);
			require(helper,
					close(target.getHealth(), 9.0D)
							&& target
									.getActiveEffects()
									.isEmpty()
							&& target
									.getDeltaMovement()
									.lengthSqr() > 0.0D,
					"Hard Sprinkle Llama lost exact one-point vanilla spit damage and hit motion");
		} finally {
			helper.getLevel().getServer()
					.setDifficulty(
							originalDifficulty, true);
		}
		restored.setHealth(restored.getMaxHealth());
		float beforeFall = restored.getHealth();
		restored.causeFallDamage(
				8.0F, 1.0F, DamageSource.FALL);
		require(helper,
				restored.getHealth() < beforeFall,
				"Sprinkle Llama incorrectly erased genuine environmental fall damage");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(
						helper, anchor, 256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Trader Llama conversion");
		TraderLlama literal =
				EntityType.TRADER_LLAMA
						.create(helper.getLevel());
		Boat vehicle =
				EntityType.BOAT.create(
						helper.getLevel());
		Pig passenger =
				EntityType.PIG.create(
						helper.getLevel());
		require(helper,
				literal != null
						&& vehicle != null
						&& passenger != null,
				"Could not create literal Trader Llama relationship fixtures");
		literal.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		vehicle.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		passenger.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		CompoundTag literalGenes =
				new CompoundTag();
		literalGenes.putInt("Strength", 5);
		literalGenes.putInt("Variant", 2);
		literal.readAdditionalSaveData(
				literalGenes);
		literal.setTamed(true);
		literal.setOwnerUUID(owner);
		literal.setHealth(13.0F);
		literal.setCustomName(
				new TextComponent(
						"Sprinkle Porter"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		literal.setDespawnDelay(321);
		require(helper,
				literal.getSlot(499).set(
						new ItemStack(Items.CHEST))
						&& literal.getSlot(401).set(
								new ItemStack(
										Items.YELLOW_CARPET))
						&& literal.getSlot(500).set(
								new ItemStack(
										CakeWorldItems
												.SIMPLE_BISCUIT
												.get(), 4)),
				"Could not prepare literal Trader Llama pack state");
		helper.getLevel().addFreshEntity(vehicle);
		helper.getLevel().addFreshEntity(literal);
		helper.getLevel().addFreshEntity(passenger);
		require(helper,
				literal.startRiding(vehicle, true)
						&& passenger.startRiding(
								literal, true)
						&& literal.getVehicle()
								== vehicle
						&& passenger.getVehicle()
								== literal,
				"Could not prepare nested literal Trader Llama ride");
		literal.setLeashedTo(trader, false);
		require(helper,
				literal.getVehicle() == null
						&& passenger.getVehicle()
								== literal
						&& literal.getLeashHolder()
								== trader,
				"Literal Trader Llama did not preserve vanilla's leash-dismount invariant before conversion");
		SprinkleLlama converted =
				CakeWorldTraderLlamaReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper, converted != null,
				"Fresh literal Trader Llama was not converted in CakeWorld terrain");
		CompoundTag convertedState =
				converted.saveWithoutId(
						new CompoundTag());
		require(helper,
				converted.getType()
								== CakeWorldEntities
										.SPRINKLE_LLAMA
										.get()
						&& converted
								.isTraderLlama()
						&& close(converted.getHealth(),
								13.0D)
						&& converted.hasCustomName()
						&& "Sprinkle Porter".equals(
								converted
										.getCustomName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted
								.isInvulnerable()
						&& converted.isTamed()
						&& owner.equals(
								converted
										.getOwnerUUID())
						&& converted.getStrength()
								== 5
						&& converted.getVariant()
								== 2
						&& converted.hasChest()
						&& converted.getSwag()
								== DyeColor.YELLOW
						&& converted.getSlot(500)
								.get().is(
										CakeWorldItems
												.SIMPLE_BISCUIT
												.get())
						&& converted.getSlot(500)
								.get().getCount() == 4
						&& convertedState
								.getInt("DespawnDelay")
								== 321,
				"Fresh literal Trader Llama conversion lost saved state or pack: type="
						+ converted.getType()
						+ ", trader="
						+ converted.isTraderLlama()
						+ ", health="
						+ converted.getHealth()
						+ ", name="
						+ converted.getName()
								.getString()
						+ ", persistence="
						+ converted.isPersistenceRequired()
						+ ", noAi="
						+ converted.isNoAi()
						+ ", invulnerable="
						+ converted.isInvulnerable()
						+ ", tamed="
						+ converted.isTamed()
						+ ", owner="
						+ converted.getOwnerUUID()
						+ ", strength="
						+ converted.getStrength()
						+ ", variant="
						+ converted.getVariant()
						+ ", chest="
						+ converted.hasChest()
						+ ", decor="
						+ converted.getSwag()
						+ ", pack="
						+ converted.getSlot(500).get()
						+ ", delay="
						+ convertedState.getInt(
								"DespawnDelay"));
		require(helper,
				converted.getVehicle() == null
						&& converted.getPassengers()
								.contains(passenger)
						&& passenger.getVehicle()
								== converted
						&& converted
								.getLeashHolder()
								== trader
						&& literal.isRemoved(),
				"Fresh literal Trader Llama conversion lost relationships: vehicle="
						+ converted.getVehicle()
						+ ", passengers="
						+ converted.getPassengers()
						+ ", passengerVehicle="
						+ passenger.getVehicle()
						+ ", leash="
						+ converted.getLeashHolder()
						+ ", expectedLeash="
						+ trader
						+ ", literalRemoved="
						+ literal.isRemoved());

		TraderLlama ridingLiteral =
				EntityType.TRADER_LLAMA
						.create(helper.getLevel());
		require(helper, ridingLiteral != null,
				"Could not create separate riding Trader Llama fixture");
		ridingLiteral.setPos(
				cakeWorldPos.getX() + 2.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(
				ridingLiteral);
		require(helper,
				ridingLiteral.startRiding(
						vehicle, true)
						&& ridingLiteral
								.getVehicle()
								== vehicle,
				"Literal Trader Llama could not enter its separate vehicle-transfer fixture");
		SprinkleLlama convertedRider =
				CakeWorldTraderLlamaReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								ridingLiteral);
		require(helper,
				convertedRider != null
						&& convertedRider
								.getVehicle()
								== vehicle
						&& vehicle.getPassengers()
								.contains(
										convertedRider)
						&& ridingLiteral.isRemoved(),
				"Fresh literal Trader Llama conversion lost a valid vehicle relationship");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(
						Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId :
				List.of(
						CakeWorldBiomes.CANDY_PLAINS
								.getId(),
						CakeWorldBiomes.COOKIE_FOREST
								.getId(),
						CakeWorldBiomes.MARSHMALLOW_PEAKS
								.getId(),
						CakeWorldBiomes.SODA_OCEAN
								.getId(),
						CakeWorldBiomes.FUDGE_WASTES
								.getId(),
						CakeWorldBiomes.MERINGUE_ISLANDS
								.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Missing CakeWorld biome "
							+ biomeId);
			require(helper,
					biome.getMobSettings()
							.getMobs(
									MobCategory.CREATURE)
							.unwrap().stream()
							.noneMatch(spawn ->
									spawn.type
											== EntityType
													.TRADER_LLAMA
											|| spawn.type
													== CakeWorldEntities
															.SPRINKLE_LLAMA
															.get()),
					"Event-only Trader/Sprinkle Llama leaked into biome ecology in "
							+ biomeId);
		}

		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.SPRINKLE_LLAMA
										.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.SPRINKLE_LLAMA
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.SPRINKLE_LLAMA_SPAWN_EGG
								.isPresent()
						&& llama.getLootTableId()
								.equals(
										new ResourceLocation(
												CakeWorld.MODID,
												"entities/sprinkle_llama"))
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.SPRINKLE_LLAMA
												.get())
								== null,
				"Sprinkle Llama lost exact dormant placement, egg, Trader-Llama loot route or deliberate no-mimic role");

		BlockPos spawnPos =
				cakeWorldPos.offset(12, 3, 0);
		helper.getLevel().setBlock(
				spawnPos.below(),
				Blocks.GRASS_BLOCK
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.LIGHT.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);

		WanderingTrader sourceTrader =
				EntityType.WANDERING_TRADER
						.create(helper.getLevel());
		TraderLlama sourceLiteral =
				EntityType.TRADER_LLAMA
						.create(helper.getLevel());
		require(helper,
				sourceTrader != null
						&& sourceLiteral != null,
				"Could not create deferred wandering-caravan conversion fixtures");
		BlockPos sourcePos =
				cakeWorldPos.offset(18, 2, 0);
		sourceTrader.setPos(
				sourcePos.getX() + 0.5D,
				sourcePos.getY(),
				sourcePos.getZ() + 0.5D);
		sourceTrader.setDespawnDelay(1200);
		helper.getLevel().addFreshEntity(
				sourceTrader);
		sourceLiteral.setPos(
				sourceTrader.getX() + 1.0D,
				sourceTrader.getY(),
				sourceTrader.getZ());
		sourceLiteral.setAge(-24000);
		sourceLiteral.finalizeSpawn(
				helper.getLevel(),
				helper.getLevel()
						.getCurrentDifficultyAt(
								sourcePos),
				MobSpawnType.EVENT, null, null);
		sourceLiteral.setCustomName(
				new TextComponent(
						"Fresh Sprinkle Caravan"));
		sourceLiteral.setNoAi(true);
		sourceLiteral.setDespawnDelay(600);
		helper.getLevel().addFreshEntity(
				sourceLiteral);
		sourceLiteral.setLeashedTo(
				sourceTrader, false);

		helper.runAfterDelay(5, () -> {
			List<SprinkleLlama> automatic =
					helper.getLevel()
							.getEntitiesOfClass(
									SprinkleLlama.class,
									new AABB(sourcePos)
											.inflate(
													5.0D),
									candidate ->
											candidate
													.hasCustomName()
													&& "Fresh Sprinkle Caravan"
															.equals(
																	candidate
																			.getCustomName()
																			.getString()));
			boolean literalRemains =
					helper.getLevel()
							.getEntitiesOfClass(
									TraderLlama.class,
									new AABB(sourcePos)
											.inflate(
													5.0D),
									candidate ->
											candidate.getType()
													== EntityType
															.TRADER_LLAMA
													&& candidate
															.hasCustomName()
													&& "Fresh Sprinkle Caravan"
															.equals(
																	candidate
																			.getCustomName()
																			.getString()))
							.size() > 0;
			SprinkleLlama automaticLlama =
					automatic.size() == 1
							? automatic.get(0)
							: null;
			List<TravellingConfectioner>
					automaticTraders =
							helper.getLevel()
									.getEntitiesOfClass(
											TravellingConfectioner.class,
											new AABB(
													sourcePos)
													.inflate(
															5.0D));
			TravellingConfectioner
					automaticTrader =
							automaticTraders
											.size()
									== 1
											? automaticTraders
													.get(0)
											: null;
			require(helper,
					automaticLlama != null
							&& automaticTrader != null
							&& sourceTrader.isRemoved()
							&& !automaticLlama
									.isBaby()
							&& automaticLlama
									.getLeashHolder()
									== automaticTrader
							&& automaticLlama
									.saveWithoutId(
											new CompoundTag())
									.getInt(
											"DespawnDelay")
									> 0
							&& !literalRemains,
					"Deferred fresh EVENT caravan did not convert both the trader and its attached llama");
			require(helper,
					helper.getLevel()
							.getMaxLocalRawBrightness(
									spawnPos) > 8
							&& Animal
									.checkAnimalSpawnRules(
											CakeWorldEntities
													.SPRINKLE_LLAMA
													.get(),
											helper.getLevel(),
											MobSpawnType
													.NATURAL,
											spawnPos,
											new Random(
													1978L))
							&& SpawnPlacements
									.checkSpawnRules(
											CakeWorldEntities
													.SPRINKLE_LLAMA
													.get(),
											helper.getLevel(),
											MobSpawnType
													.NATURAL,
											spawnPos,
											new Random(
													2058L))
							&& SpawnPlacements.Type
									.NO_RESTRICTIONS
									.canSpawnAt(
											helper.getLevel(),
											spawnPos,
											CakeWorldEntities
													.SPRINKLE_LLAMA
													.get()),
					"Sprinkle Llama lost vanilla Trader Llama's dormant NO_RESTRICTIONS placement plus animal predicate");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void brittleBiscuitSteedsKeepTheCompleteSkeletonTrap(
			GameTestHelper helper) {
		BrittleBiscuitSteedProbe steed =
				new BrittleBiscuitSteedProbe(
						helper.getLevel());
		require(helper,
				steed instanceof SkeletonHorse
						&& steed.getType()
								== CakeWorldEntities
										.BRITTLE_BISCUIT_STEED
										.get()
						&& steed.getType().getCategory()
								== MobCategory.CREATURE
						&& close(steed.getMaxHealth(),
								15.0D)
						&& close(steed.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.2D)
						&& close(steed.getAttributeValue(
								Attributes.JUMP_STRENGTH),
								0.7D)
						&& steed.getAttribute(
								Attributes.ATTACK_DAMAGE)
								== null
						&& close(steed.getDimensions(
								Pose.STANDING).width,
								1.3964844D)
						&& close(steed.getDimensions(
								Pose.STANDING).height,
								1.6D)
						&& steed.getType()
								.clientTrackingRange() == 10
						&& steed.getMaxSpawnClusterSize()
								== 6
						&& steed.getMobType()
								== MobType.UNDEAD
						&& steed.getMaxTemper() == 100,
				"Brittle Biscuit Steed lost the exact Skeleton Horse body, attributes, size, tracking, group, undead or temper contract");
		steed.seedRandom(1978L);
		int experience = steed.getExperienceValue();
		require(helper,
				experience >= 1 && experience <= 3
						&& steed.goalPriority(
								"PanicGoal") == 1
						&& steed.goalPriority(
								"RunAroundLikeCrazyGoal")
								== 1
						&& steed.goalPriority(
								"BreedGoal") == 2
						&& steed.goalPriority(
								"FollowParentGoal") == 4
						&& steed.goalPriority(
								"WaterAvoidingRandomStrollGoal")
								== 6
						&& steed.goalPriority(
								"LookAtPlayerGoal") == 7
						&& steed.goalPriority(
								"RandomLookAroundGoal")
								== 8
						&& steed.goalPriority(
								"FloatGoal") == -1
						&& steed.goalPriority(
								"TemptGoal") == -1
						&& steed.targetGoalCount() == 0,
				"Brittle Biscuit Steed lost exact Skeleton Horse experience, dry-land goals or no-target role");
		require(helper,
				steed.ambientSound()
								== SoundEvents
										.SKELETON_HORSE_AMBIENT
						&& steed.hurtSound()
								== SoundEvents
										.SKELETON_HORSE_HURT
						&& steed.deathSound()
								== SoundEvents
										.SKELETON_HORSE_DEATH
						&& steed.swimSound()
								== SoundEvents
										.SKELETON_HORSE_SWIM
						&& steed.rideableUnderWater()
						&& close(steed.waterSlowDown(),
								0.96D),
				"Brittle Biscuit Steed lost exact Skeleton Horse sounds or underwater-riding contract");

		BrittleBiscuitSteed mate =
				CakeWorldEntities.BRITTLE_BISCUIT_STEED
						.get().create(helper.getLevel());
		require(helper, mate != null,
				"Could not create Brittle Biscuit Steed mate fixture");
		steed.setTamed(true);
		mate.setTamed(true);
		steed.setInLove(null);
		mate.setInLove(null);
		AgeableMob commandOffspring =
				steed.getBreedOffspring(
						helper.getLevel(), mate);
		require(helper,
				!steed.canMate(mate)
						&& commandOffspring
								instanceof BrittleBiscuitSteed
						&& commandOffspring.getType()
								== CakeWorldEntities
										.BRITTLE_BISCUIT_STEED
										.get()
						&& !steed.canWearArmor()
						&& !steed.isArmor(new ItemStack(
								Items.DIAMOND_HORSE_ARMOR))
						&& !steed.getSlot(401).set(
								new ItemStack(Items
										.DIAMOND_HORSE_ARMOR)),
				"Brittle Biscuit Steed lost vanilla sterility/no-armour or custom command-offspring identity");

		Player untamedRider = helper.makeMockPlayer();
		untamedRider.setItemInHand(
				InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		steed.setTamed(false);
		require(helper,
				steed.mobInteract(untamedRider,
						InteractionHand.MAIN_HAND)
								== InteractionResult.PASS
						&& untamedRider.getVehicle()
								== null,
				"Untamed Brittle Biscuit Steed no longer refuses ordinary riding");
		steed.setTamed(true);
		require(helper,
				steed.getSlot(400).set(
						new ItemStack(Items.SADDLE))
						&& steed.isSaddled(),
				"Tamed Brittle Biscuit Steed could not equip the exact saddle slot");
		InteractionResult rideResult = steed.mobInteract(
				untamedRider, InteractionHand.MAIN_HAND);
		require(helper,
				rideResult.consumesAction()
						&& untamedRider.getVehicle()
								== steed
						&& steed.hasPassenger(
								untamedRider)
						&& steed.canBeControlledByRider()
						&& steed.canJump(),
				"Tamed, saddled Brittle Biscuit Steed lost inherited player riding and jumping");
		untamedRider.stopRiding();

		BrittleBiscuitSteedProbe expiring =
				new BrittleBiscuitSteedProbe(
						helper.getLevel());
		CompoundTag expiringState =
				new CompoundTag();
		expiringState.putBoolean("SkeletonTrap", true);
		expiringState.putInt(
				"SkeletonTrapTime", 18000);
		expiring.readAdditionalSaveData(
				expiringState);
		require(helper,
				expiring.isTrap()
						&& expiring.goalPriority(
								"SkeletonTrapGoal")
								== 1,
				"Skeleton Trap NBT did not install the inherited priority-one trigger");
		expiring.aiStep();
		require(helper, expiring.isRemoved(),
				"Brittle Biscuit Steed no longer expires after the exact 18,000 trap ticks");

		BlockPos center =
				helper.absolutePos(new BlockPos(2, 3, 2));
		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(
						helper, center, 256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for Skeleton Horse conversion");

		SkeletonHorse literal =
				EntityType.SKELETON_HORSE.create(
						helper.getLevel());
		Boat boat = EntityType.BOAT.create(
				helper.getLevel());
		CandyCaneArcher passenger =
				CakeWorldEntities.CANDY_CANE_ARCHER
						.get().create(helper.getLevel());
		require(helper,
				literal != null && boat != null
						&& passenger != null,
				"Could not create literal Skeleton Horse conversion fixtures");
		literal.moveTo(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		boat.moveTo(literal.getX(), literal.getY(),
				literal.getZ());
		passenger.moveTo(literal.getX(),
				literal.getY(), literal.getZ());
		CompoundTag literalTrapState =
				new CompoundTag();
		literalTrapState.putBoolean(
				"SkeletonTrap", true);
		literalTrapState.putInt(
				"SkeletonTrapTime", 123);
		literal.readAdditionalSaveData(
				literalTrapState);
		literal.setTamed(true);
		literal.getSlot(400).set(
				new ItemStack(Items.SADDLE));
		literal.setHealth(9.0F);
		literal.setCustomName(
				new TextComponent("Crumbly Charger"));
		literal.setPersistenceRequired();
		literal.invulnerableTime = 41;
		passenger.invulnerableTime = 39;
		helper.getLevel().addFreshEntity(boat);
		helper.getLevel().addFreshEntity(literal);
		helper.getLevel().addFreshEntity(passenger);
		literal.startRiding(boat, true);
		passenger.startRiding(literal, true);
		BrittleBiscuitSteed converted =
				CakeWorldSkeletonHorseReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		CompoundTag convertedState =
				new CompoundTag();
		if (converted != null) {
			converted.addAdditionalSaveData(
					convertedState);
		}
		require(helper,
				converted != null
						&& converted.getType()
								== CakeWorldEntities
										.BRITTLE_BISCUIT_STEED
										.get()
						&& close(converted.getHealth(),
								9.0D)
						&& "Crumbly Charger".equals(
								converted.getName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isTamed()
						&& converted.isSaddled()
						&& converted.isTrap()
						&& convertedState.getInt(
								"SkeletonTrapTime")
								== 123
						&& converted.invulnerableTime
								== 41
						&& converted.getVehicle() == boat
						&& boat.getPassengers()
								.contains(converted)
						&& passenger.getVehicle()
								== converted
						&& converted.getPassengers()
								.contains(passenger)
						&& passenger.invulnerableTime
								== 39
						&& literal.isRemoved(),
				"Fresh literal Skeleton Horse conversion lost trap timer, tame/saddle, state, invulnerability, vehicle or rider");
		passenger.discard();
		converted.discard();
		boat.discard();

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SKELETON_HORSE
											|| spawn.type
													== CakeWorldEntities
															.BRITTLE_BISCUIT_STEED
															.get()),
					"Skeleton Horse role leaked into ordinary biome spawning in "
							+ biomeId);
		}

		Advancement monstersHunted = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"adventure/kill_all_mobs"));
		Advancement bredAll = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"husbandry/bred_all_animals"));
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.BRITTLE_BISCUIT_STEED
										.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.SKELETON_HORSE)
						&& SpawnPlacements.getHeightmapType(
								CakeWorldEntities
										.BRITTLE_BISCUIT_STEED
										.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.SKELETON_HORSE)
						&& CakeWorldItems
								.BRITTLE_BISCUIT_STEED_SPAWN_EGG
								.isPresent()
						&& steed.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/brittle_biscuit_steed"))
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.BRITTLE_BISCUIT_STEED
												.get())
								== null
						&& monstersHunted != null
						&& !monstersHunted.getCriteria()
								.containsKey(
										"minecraft:skeleton_horse")
						&& bredAll != null
						&& !bredAll.getCriteria()
								.containsKey(
										"minecraft:skeleton_horse"),
				"Brittle Biscuit Steed lost matching non-spawn placement, egg, loot, no-mimic or no-invented-advancement contract");

		BrittleBiscuitSteedProbe trap =
				new BrittleBiscuitSteedProbe(
						helper.getLevel());
		trap.moveTo(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 2.0D,
				cakeWorldPos.getZ() + 0.5D);
		trap.setTrap(true);
		helper.getLevel().addFreshEntity(trap);
		ServerPlayer nearbyPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2051"),
								"CakeWorldSkeletonTrapTest"));
		nearbyPlayer.setPos(trap.getX() + 5.0D,
				trap.getY(), trap.getZ());
		SkeletonTrapGoal inheritedTrigger =
				new SkeletonTrapGoal(trap);
		List<ServerPlayer> levelPlayers =
				testLevelPlayers(helper.getLevel());
		levelPlayers.add(nearbyPlayer);
		try {
			require(helper,
					inheritedTrigger.canUse(),
					"Inherited Skeleton Trap no longer detects a living player within ten blocks");
		} finally {
			levelPlayers.remove(nearbyPlayer);
		}
		nearbyPlayer.setPos(trap.getX() + 11.0D,
				trap.getY(), trap.getZ());
		levelPlayers.add(nearbyPlayer);
		try {
			require(helper,
					!inheritedTrigger.canUse(),
					"Inherited Skeleton Trap activated beyond the exact ten-block radius");
		} finally {
			levelPlayers.remove(nearbyPlayer);
		}
		inheritedTrigger.tick();

		AABB trapArea = trap.getBoundingBox()
				.inflate(12.0D);
		helper.runAfterDelay(1, () -> {
			LightningBolt cue = helper.getLevel()
					.getEntitiesOfClass(
							LightningBolt.class,
							trapArea)
					.stream().findFirst()
					.orElse(null);
			require(helper,
					cue != null
							&& isVisualOnly(cue)
							&& !trap.isTrap()
							&& trap.isTamed()
							&& !trap.isBaby(),
					"Inherited Skeleton Trap lost its visual-only lightning cue or did not tame/adult the original steed");
		});
		helper.runAfterDelay(6, () -> {
			List<BrittleBiscuitSteed> mounts =
					helper.getLevel()
							.getEntitiesOfClass(
									BrittleBiscuitSteed
											.class,
									trapArea);
			List<CandyCaneArcher> riders =
					helper.getLevel()
							.getEntitiesOfClass(
									CandyCaneArcher.class,
									trapArea,
									archer -> archer
											.isPassenger());
			long persistentMounts = mounts.stream()
					.filter(mount -> mount
							.isPersistenceRequired())
					.count();
			long protectedChildMounts = mounts.stream()
					.filter(mount ->
							mount.invulnerableTime > 0)
					.count();
			require(helper,
					mounts.size() == 4
							&& mounts.contains(trap)
							&& mounts.stream().allMatch(
									mount -> mount
											.isTamed()
											&& !mount
													.isBaby()
											&& !mount
													.isTrap())
							&& persistentMounts == 3
							&& protectedChildMounts == 3
							&& riders.size() == 4
							&& riders.stream().allMatch(
									rider -> rider
											.getType()
													== CakeWorldEntities
															.CANDY_CANE_ARCHER
															.get()
											&& rider
													.isPersistenceRequired()
											&& rider
													.invulnerableTime
													> 0
											&& rider
													.getMainHandItem()
													.is(Items.BOW)
											&& !rider
													.getItemBySlot(
															EquipmentSlot
																	.HEAD)
													.isEmpty()
											&& rider
													.getVehicle()
													instanceof BrittleBiscuitSteed)
							&& mounts.stream().allMatch(
									mount -> mount
											.getPassengers()
											.size() == 1
											&& mount
													.getFirstPassenger()
													instanceof CandyCaneArcher)
							&& helper.getLevel()
									.getEntitiesOfClass(
											SkeletonHorse.class,
											trapArea,
											horse -> horse
													.getType()
													== EntityType
															.SKELETON_HORSE)
									.isEmpty()
							&& helper.getLevel()
									.getEntitiesOfClass(
											Skeleton.class,
											trapArea,
											rider -> rider
													.getType()
													== EntityType
															.SKELETON)
									.isEmpty(),
					"Inherited trap did not produce exactly four custom tamed adult mounts and four protected equipped Archer riders without literal leakage");
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void jellyBlobsKeepSlimeEcologySplitsAndSafeElasticContact(
			GameTestHelper helper) {
		JellyBlobProbe large =
				new JellyBlobProbe(helper.getLevel());
		large.setTestSize(4);
		large.setHealth(large.getMaxHealth());
		large.seedRandom(1978L);
		int jumpDelay = large.sampleJumpDelay();
		large.setDeltaMovement(Vec3.ZERO);
		large.performGroundJump();
		require(helper,
				large instanceof Slime
						&& large.getType()
								== CakeWorldEntities
										.JELLY_BLOB.get()
						&& large.getType().getCategory()
								== MobCategory.MONSTER
						&& large.getSize() == 4
						&& close(large.getMaxHealth(),
								16.0D)
						&& close(large.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.6D)
						&& close(large.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								4.0D)
						&& close(large.getAttributeValue(
								Attributes.ARMOR), 0.0D)
						&& close(large.getAttributeValue(
								Attributes.ARMOR_TOUGHNESS),
								0.0D)
						&& close(large.sampleAttackDamage(),
								4.0D)
						&& large.getExperienceValue() == 4
						&& close(large.getDimensions(
								Pose.STANDING).width,
								2.0808D)
						&& close(large.getDimensions(
								Pose.STANDING).height,
								2.0808D)
						&& close(large.standingEyeHeight(),
								1.3005D)
						&& large.getType()
								.clientTrackingRange() == 10
						&& large.getMaxSpawnClusterSize()
								== 4
						&& jumpDelay >= 10
						&& jumpDelay <= 29
						&& close(large
								.getDeltaMovement().y,
								0.42D)
						&& large.canDealContactDamage()
						&& large.despawnsInPeaceful(),
				"Jelly Blob lost exact size-four Slime body, scaling, experience, jump, tracking, group or hostility");
		require(helper,
				large.goalPriority(
						"SlimeFloatGoal") == 1
						&& large.goalPriority(
								"SlimeAttackGoal") == 2
						&& large.goalPriority(
								"SlimeRandomDirectionGoal")
								== 3
						&& large.goalPriority(
								"SlimeKeepOnJumpingGoal")
								== 5
						&& large.targetGoalCountAtPriority(
								1) == 1
						&& large.targetGoalCountAtPriority(
								3) == 1
						&& large.targetGoalCount() == 2,
				"Jelly Blob lost Slime float, attack, direction, hopping, Player or Iron Golem goals");
		require(helper,
				large.hurtSound()
								== SoundEvents.SLIME_HURT
						&& large.deathSound()
								== SoundEvents.SLIME_DEATH
						&& large.squishSound()
								== SoundEvents.SLIME_SQUISH
						&& large.jumpSound()
								== SoundEvents.SLIME_JUMP
						&& close(large.soundVolume(),
								1.6D),
				"Large Jelly Blob lost exact Slime sound roles or size-scaled volume");

		CompoundTag saved = new CompoundTag();
		large.addAdditionalSaveData(saved);
		JellyBlobProbe restored =
				new JellyBlobProbe(helper.getLevel());
		restored.readAdditionalSaveData(saved);
		CompoundTag belowMinimum =
				new CompoundTag();
		belowMinimum.putInt("Size", -100);
		JellyBlobProbe minimum =
				new JellyBlobProbe(helper.getLevel());
		minimum.readAdditionalSaveData(
				belowMinimum);
		CompoundTag atMaximum =
				new CompoundTag();
		atMaximum.putInt("Size", 126);
		JellyBlobProbe maximum =
				new JellyBlobProbe(helper.getLevel());
		maximum.readAdditionalSaveData(atMaximum);
		require(helper,
				saved.getInt("Size") == 3
						&& saved.contains(
								"wasOnGround")
						&& restored.getSize() == 4
						&& close(restored
								.getMaxHealth(),
								16.0D)
						&& minimum.getSize() == 1
						&& maximum.getSize() == 127
						&& close(maximum.getAttribute(
								Attributes.MAX_HEALTH)
								.getBaseValue(),
								16129.0D)
						&& close(maximum
								.getMaxHealth(),
								1024.0D),
				"Jelly Blob lost Slime size NBT, ground-state field or 1..127 clamping");

		Pig target = EntityType.PIG.create(
				helper.getLevel());
		require(helper, target != null,
				"Could not create Jelly Blob contact target");
		BlockPos anchor = helper.absolutePos(
				new BlockPos(1, 2, 1));
		large.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());
		target.setPos(anchor.getX() + 0.25D,
				anchor.getY(), anchor.getZ());
		for (Difficulty safeDifficulty :
				new Difficulty[] {
						Difficulty.PEACEFUL,
						Difficulty.EASY,
						Difficulty.NORMAL}) {
			target.removeAllEffects();
			target.setHealth(10.0F);
			target.setSecondsOnFire(5);
			target.fallDistance = 12.0F;
			target.setDeltaMovement(Vec3.ZERO);
			large.clearLastSound();
			LivingHurtEvent protectedHit =
					new LivingHurtEvent(target,
							DamageSource.mobAttack(
									large),
							large.sampleAttackDamage());
			JellyBlobDamageSafety
					.applyForDifficulty(
							protectedHit,
							safeDifficulty);
			require(helper,
					protectedHit.isCanceled()
							&& close(target.getHealth(),
									10.0D)
							&& !target.isOnFire()
							&& target.fallDistance == 0.0F
							&& target.hasEffect(
									MobEffects
											.MOVEMENT_SLOWDOWN)
							&& target.hasEffect(
									MobEffects.GLOWING)
							&& target.hasEffect(
									MobEffects.SLOW_FALLING)
							&& target.hasEffect(
									MobEffects
											.FIRE_RESISTANCE)
							&& target.getEffect(
									MobEffects
											.DAMAGE_RESISTANCE)
									.getAmplifier() == 4
							&& target
									.getDeltaMovement().y
									>= 0.6D
							&& large.lastSound()
									== SoundEvents
											.SLIME_SQUISH,
					safeDifficulty
							+ " Jelly Blob did not replace contact damage with the protected elastic rescue");
		}
		target.removeAllEffects();
		target.setDeltaMovement(Vec3.ZERO);
		large.clearLastSound();
		LivingHurtEvent hardHit =
				new LivingHurtEvent(target,
						DamageSource.mobAttack(large),
						large.sampleAttackDamage());
		JellyBlobDamageSafety.applyForDifficulty(
				hardHit, Difficulty.HARD);
		require(helper,
				!hardHit.isCanceled()
						&& close(hardHit.getAmount(),
								4.0D)
						&& target.getActiveEffects()
								.isEmpty()
						&& target.getDeltaMovement()
								.equals(Vec3.ZERO)
						&& large.lastSound() == null,
				"Hard size-four Jelly Blob did not retain an unmodified four-point Slime contact");

		JellyBlobProbe tiny =
				new JellyBlobProbe(helper.getLevel());
		tiny.setTestSize(1);
		require(helper,
				tiny.isTiny()
						&& close(tiny.getMaxHealth(),
								1.0D)
						&& close(tiny.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.3D)
						&& close(tiny.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								1.0D)
						&& tiny.getExperienceValue() == 1
						&& close(tiny.getDimensions(
								Pose.STANDING).width,
								0.5202D)
						&& !tiny.canDealContactDamage()
						&& tiny.hurtSound()
								== SoundEvents
										.SLIME_HURT_SMALL
						&& tiny.deathSound()
								== SoundEvents
										.SLIME_DEATH_SMALL
						&& tiny.squishSound()
								== SoundEvents
										.SLIME_SQUISH_SMALL
						&& tiny.jumpSound()
								== SoundEvents
										.SLIME_JUMP_SMALL
						&& tiny.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/jelly_blob"))
						&& large.getLootTableId().equals(
								BuiltInLootTables.EMPTY),
				"Tiny Jelly Blob lost harmless contact, exact scaling, small sounds or Slime-Ball loot gate");

		JellyBlobProbe parent =
				new JellyBlobProbe(helper.getLevel());
		parent.setTestSize(4);
		parent.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());
		parent.setCustomName(new TextComponent(
				"Jiggly Family"));
		parent.setPersistenceRequired();
		parent.setNoAi(true);
		parent.setInvulnerable(true);
		helper.getLevel().addFreshEntity(parent);
		parent.setHealth(0.0F);
		parent.remove(Entity.RemovalReason.KILLED);
		List<JellyBlob> children = helper.getLevel()
				.getEntitiesOfClass(JellyBlob.class,
						new AABB(anchor).inflate(3.0D));
		require(helper,
				parent.isRemoved()
						&& children.size() >= 2
						&& children.size() <= 4
						&& children.stream()
								.allMatch(child ->
										child.getType()
												== CakeWorldEntities
														.JELLY_BLOB
														.get()
										&& child
												.getSize() == 2
										&& close(child
												.getHealth(),
												4.0D)
										&& child
												.isPersistenceRequired()
										&& child.isNoAi()
										&& child
												.isInvulnerable()
										&& "Jiggly Family"
												.equals(child
														.getName()
														.getString()))
						&& helper.getLevel()
								.getEntitiesOfClass(
										Slime.class,
										new AABB(anchor)
												.inflate(3.0D),
										slime -> slime
												.getType()
												== EntityType
														.SLIME)
								.isEmpty(),
				"Jelly Blob split leaked literal Slimes or lost child size, health, name, persistence, AI or invulnerability");
		children.forEach(Entity::discard);

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Could not inspect Jelly Blob spawning in "
							+ biomeId);
			List<MobSpawnSettings.SpawnerData>
					roleSpawns = biome.getMobSettings()
							.getMobs(
									MobCategory.MONSTER)
							.unwrap().stream()
							.filter(spawn ->
									spawn.type
											== EntityType.SLIME
									|| spawn.type
											== CakeWorldEntities
													.JELLY_BLOB
													.get())
							.toList();
			require(helper,
					roleSpawns.size() == 1
							&& roleSpawns.get(0).type
									== CakeWorldEntities
											.JELLY_BLOB.get()
							&& roleSpawns.get(0)
									.getWeight().asInt()
									== 100
							&& roleSpawns.get(0)
									.minCount == 4
							&& roleSpawns.get(0)
									.maxCount == 4,
					"Jelly Blob did not exactly replace the common Overworld Slime 100/4-4 profile in "
							+ biomeId);
		}
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SLIME
											|| spawn.type
													== CakeWorldEntities
															.JELLY_BLOB
															.get()),
					"Jelly Blob role leaked outside the Overworld ecology in "
							+ biomeId);
		}

		ChunkPos slimeChunk = null;
		ChunkPos ordinaryChunk = null;
		long worldSeed = helper.getLevel().getSeed();
		for (int x = -20; x <= 20
				&& (slimeChunk == null
						|| ordinaryChunk == null); x++) {
			for (int z = -20; z <= 20
					&& (slimeChunk == null
							|| ordinaryChunk == null);
					z++) {
				ChunkPos candidate =
						new ChunkPos(x, z);
				boolean expected =
						WorldgenRandom.seedSlimeChunk(
								x, z, worldSeed,
								987234911L)
								.nextInt(10) == 0;
				if (expected
						&& JellyBlob.isSlimeChunk(
								worldSeed,
								candidate)) {
					slimeChunk = candidate;
				} else if (!expected
						&& !JellyBlob.isSlimeChunk(
								worldSeed,
								candidate)) {
					ordinaryChunk = candidate;
				}
			}
		}
		require(helper,
				slimeChunk != null
						&& ordinaryChunk != null
						&& !JellyBlob.allowsNaturalSpawn(
								Difficulty.PEACEFUL)
						&& JellyBlob.allowsNaturalSpawn(
								Difficulty.EASY)
						&& JellyBlob.allowsNaturalSpawn(
								Difficulty.NORMAL)
						&& JellyBlob.allowsNaturalSpawn(
								Difficulty.HARD)
						&& JellyBlob
								.isCakeWorldSurfaceJellyBiome(
										new ResourceLocation(
												CakeWorld.MODID,
												"caramel_bogs"))
						&& !JellyBlob
								.isCakeWorldSurfaceJellyBiome(
										CakeWorldBiomes
												.CANDY_PLAINS
												.getId())
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.JELLY_BLOB
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.SLIME)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.JELLY_BLOB
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.SLIME),
				"Jelly Blob lost Peaceful suppression, exact slime-chunk salt, future Caramel Bogs routing or Slime placement metadata");

		ServerPlayer advancementPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2052"),
								"CakeWorldJellyBlobRoleTest"));
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(tiny,
						DamageSource.playerAttack(
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:slime");
		require(helper,
				CakeWorldItems
								.JELLY_BLOB_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.JELLY_BLOB
												.get())
								== SoundEvents
										.PARROT_IMITATE_SLIME,
				"Jelly Blob lost its testing egg, Slime advancement role or Lorikeet mimic");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void iceCreamGolemsKeepConstructionScoopsClimateAndIcingTrail(
			GameTestHelper helper) {
		IceCreamGolemProbe golem =
				new IceCreamGolemProbe(
						helper.getLevel());
		require(helper,
				golem instanceof SnowGolem
						&& golem.getType()
								== CakeWorldEntities
										.ICE_CREAM_GOLEM
										.get()
						&& golem.getType().getCategory()
								== MobCategory.MISC
						&& close(golem.getMaxHealth(),
								4.0D)
						&& close(golem.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.2D)
						&& close(golem.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								16.0D)
						&& golem.getAttribute(
								Attributes.ATTACK_DAMAGE)
								== null
						&& close(golem.getDimensions(
								Pose.STANDING).width,
								0.7D)
						&& close(golem.getDimensions(
								Pose.STANDING).height,
								1.9D)
						&& close(golem.standingEyeHeight(),
								1.7D)
						&& golem.getType()
								.clientTrackingRange() == 8
						&& golem.getMaxSpawnClusterSize()
								== 4
						&& golem.getExperienceValue() == 0
						&& !golem.despawnsInPeaceful()
						&& golem.isSensitiveToWater()
						&& !golem.causeFallDamage(
								100.0F, 1.0F,
								DamageSource.FALL)
						&& !golem.removeWhenFarAway(
								10000.0D)
						&& golem
								.getAmbientSoundInterval()
								== 120
						&& golem.getType()
								.isBlockDangerous(
										Blocks
												.POWDER_SNOW
												.defaultBlockState())
								== EntityType.SNOW_GOLEM
										.isBlockDangerous(
												Blocks
														.POWDER_SNOW
														.defaultBlockState()),
				"Ice-Cream Golem lost exact Snow Golem body, attributes, persistence, climate, fall or powder-snow contract");
		require(helper,
				golem.goalPriority(
						"RangedAttackGoal") == 1
						&& golem.goalPriority(
								"WaterAvoidingRandomStrollGoal")
								== 2
						&& golem.goalPriority(
								"LookAtPlayerGoal") == 3
						&& golem.goalPriority(
								"RandomLookAroundGoal") == 4
						&& golem.targetGoalPriority(
								"NearestAttackableTargetGoal")
								== 1
						&& golem.targetGoalCount() == 1,
				"Ice-Cream Golem lost exact ranged-helper movement or Enemy-target goals");
		require(helper,
				golem.hasPumpkin()
						&& golem.readyForShearing()
						&& golem.ambientSound()
								== SoundEvents
										.SNOW_GOLEM_AMBIENT
						&& golem.hurtSound()
								== SoundEvents
										.SNOW_GOLEM_HURT
						&& golem.deathSound()
								== SoundEvents
										.SNOW_GOLEM_DEATH
						&& close(golem.getLeashOffset().y,
								1.275D)
						&& close(golem.getLeashOffset().z,
								0.28D),
				"Ice-Cream Golem lost pumpkin, shearing, sound or leash presentation");

		List<ItemStack> sheared = golem.onSheared(
				null, new ItemStack(Items.SHEARS),
				helper.getLevel(),
				golem.blockPosition(), 0);
		CompoundTag pumpkinState =
				new CompoundTag();
		golem.addAdditionalSaveData(pumpkinState);
		IceCreamGolemProbe restored =
				new IceCreamGolemProbe(
						helper.getLevel());
		restored.readAdditionalSaveData(
				pumpkinState);
		require(helper,
				sheared.size() == 1
						&& sheared.get(0)
								.is(Items.CARVED_PUMPKIN)
						&& !golem.hasPumpkin()
						&& !golem.readyForShearing()
						&& pumpkinState.contains(
								"Pumpkin")
						&& !pumpkinState.getBoolean(
								"Pumpkin")
						&& !restored.hasPumpkin(),
				"Ice-Cream Golem lost Forge shearing, carved-pumpkin drop or Pumpkin NBT");

		BlockPos trailCenter = helper.absolutePos(
				new BlockPos(1, 3, 1));
		List<BlockPos> trailCells = List.of(
				trailCenter.offset(-1, 0, -1),
				trailCenter.offset(0, 0, -1),
				trailCenter.offset(-1, 0, 0),
				trailCenter);
		for (BlockPos trailCell : trailCells) {
			helper.getLevel().setBlock(
					trailCell.below(),
					CakeWorldBlocks.BISCUIT_STONE
							.get().defaultBlockState(),
					3);
			helper.getLevel().setBlock(
					trailCell,
					Blocks.AIR.defaultBlockState(),
					3);
		}
		BlockPos preservedSnow = trailCells.get(0);
		helper.getLevel().setBlock(preservedSnow,
				Blocks.SNOW.defaultBlockState(), 3);
		golem.setNoAi(true);
		golem.setPos(trailCenter.getX(),
				trailCenter.getY(),
				trailCenter.getZ());
		require(helper,
				helper.getLevel().getGameRules()
								.getBoolean(GameRules
										.RULE_MOBGRIEFING)
						&& ForgeEventFactory
								.getMobGriefingEvent(
										helper.getLevel(),
										golem),
				"Ice-Cream Golem trail fixture requires the accepted mobGriefing gate");
		golem.aiStep();
		long icingCells = trailCells.stream()
				.filter(pos -> helper.getLevel()
						.getBlockState(pos)
						.is(CakeWorldBlocks
								.ICING_LAYER.get()))
				.count();
		require(helper,
				icingCells == 3
						&& helper.getLevel()
								.getBlockState(
										preservedSnow)
								.is(Blocks.SNOW)
						&& trailCells.stream()
								.noneMatch(pos ->
										!pos.equals(
												preservedSnow)
										&& helper
												.getLevel()
												.getBlockState(
														pos)
												.is(Blocks
														.SNOW)),
				"Ice-Cream Golem did not translate only its three fresh Snow trail cells to Icing");
		trailCells.forEach(pos ->
				helper.getLevel().setBlock(pos,
						Blocks.AIR.defaultBlockState(),
						3));

		ServerLevel nether = helper.getLevel()
				.getServer().getLevel(Level.NETHER);
		require(helper, nether != null,
				"Could not inspect Ice-Cream Golem hot-biome melting");
		BlockPos hotPos = null;
		for (int x = -128; x <= 128
				&& hotPos == null; x += 16) {
			for (int z = -128; z <= 128
					&& hotPos == null; z += 16) {
				BlockPos candidate =
						new BlockPos(x, 64, z);
				if (nether.getBiome(candidate)
						.value()
						.shouldSnowGolemBurn(
								candidate)) {
					hotPos = candidate;
				}
			}
		}
		require(helper, hotPos != null,
				"Could not find a hot CakeWorld Nether biome for Ice-Cream Golem melting");
		IceCreamGolemProbe melting =
				new IceCreamGolemProbe(nether);
		melting.setNoAi(true);
		melting.setPos(hotPos.getX(),
				hotPos.getY(), hotPos.getZ());
		melting.setHealth(4.0F);
		melting.aiStep();
		require(helper,
				close(melting.getHealth(), 3.0D),
				"Ice-Cream Golem did not retain exact one-point-per-step hot-biome melting");

		BlockPos attackCenter = trailCenter;
		golem.setPos(attackCenter.getX(),
				attackCenter.getY(),
				attackCenter.getZ());
		golem.clearLastSound();
		CinnamonSpark cinnamon =
				CakeWorldEntities.CINNAMON_SPARK.get()
						.create(helper.getLevel());
		require(helper, cinnamon != null,
				"Could not create Ice-Cream Golem Blaze-role target");
		cinnamon.setPos(attackCenter.getX() + 5.0D,
				attackCenter.getY(),
				attackCenter.getZ());
		golem.performRangedAttack(cinnamon, 1.0F);
		Snowball fired = helper.getLevel()
				.getEntitiesOfClass(Snowball.class,
						new AABB(attackCenter)
								.inflate(8.0D),
						projectile -> projectile
								.getOwner() == golem)
				.stream().findFirst().orElse(null);
		Vec3 firedVelocity = fired == null
				? Vec3.ZERO
				: fired.getDeltaMovement();
		double firedSpeed = firedVelocity.length();
		require(helper,
				fired != null
						&& fired.getType()
								== EntityType.SNOWBALL
						&& firedVelocity.x > 0.5D
						&& firedSpeed > 0.5D
						&& firedSpeed < 2.7D
						&& golem.lastSound()
								== SoundEvents
										.SNOW_GOLEM_SHOOT,
				"Ice-Cream Golem lost its vanilla Snowball, target-directed trajectory or firing sound"
						+ " (present=" + (fired != null)
						+ ", velocity=" + firedVelocity
						+ ", speed=" + firedSpeed
						+ ", sound=" + golem.lastSound()
						+ ")");
		fired.discard();

		cinnamon.setHealth(20.0F);
		cinnamon.invulnerableTime = 0;
		cinnamon.removeAllEffects();
		SnowballProbe safeScoop =
				new SnowballProbe(
						helper.getLevel(), golem);
		safeScoop.hit(cinnamon);
		require(helper,
				close(cinnamon.getHealth(), 20.0D)
						&& cinnamon.hasEffect(
								MobEffects
										.MOVEMENT_SLOWDOWN)
						&& cinnamon.hasEffect(
								MobEffects.GLOWING)
						&& cinnamon.hasEffect(
								MobEffects.WEAKNESS),
				"Normal Ice-Cream Golem scoop did not cancel Blaze-family damage and apply visible inconvenience");

		Pig ordinaryOwner =
				EntityType.PIG.create(helper.getLevel());
		require(helper, ordinaryOwner != null,
				"Could not create ordinary Snowball owner");
		cinnamon.removeAllEffects();
		cinnamon.setHealth(20.0F);
		cinnamon.invulnerableTime = 0;
		SnowballProbe vanillaControl =
				new SnowballProbe(
						helper.getLevel(),
						ordinaryOwner);
		vanillaControl.hit(cinnamon);
		require(helper,
				close(cinnamon.getHealth(), 17.0D),
				"Vanilla Snowball control no longer proves the exact three-point Blaze-family damage");

		cinnamon.removeAllEffects();
		LivingAttackEvent hardHit =
				new LivingAttackEvent(cinnamon,
						DamageSource.thrown(
								safeScoop, golem),
						3.0F);
		IceCreamGolemProjectileSafety
				.applyForDifficulty(
						hardHit, Difficulty.HARD);
		require(helper,
				!hardHit.isCanceled()
						&& close(hardHit.getAmount(),
								3.0D)
						&& cinnamon.getActiveEffects()
								.isEmpty(),
				"Hard Ice-Cream Golem scoop did not remain an unmodified three-point vanilla Blaze-family hit");

		BlockPos conversionPos = trailCenter;
		ResourceLocation conversionBiome =
				helper.getLevel()
						.getBiome(conversionPos)
						.unwrapKey()
						.map(key -> key.location())
						.orElse(null);
		require(helper,
				conversionBiome != null
						&& CakeWorld.MODID.equals(
								conversionBiome
										.getNamespace()),
				"Ice-Cream Golem fixture is not in local CakeWorld terrain: "
						+ conversionBiome);
		SnowGolem literal =
				EntityType.SNOW_GOLEM.create(
						helper.getLevel());
		Boat boat = EntityType.BOAT.create(
				helper.getLevel());
		Pig passenger =
				EntityType.PIG.create(helper.getLevel());
		Pig leashHolder =
				EntityType.PIG.create(helper.getLevel());
		require(helper,
				literal != null && boat != null
						&& passenger != null
						&& leashHolder != null,
				"Could not create Snow Golem conversion fixtures");
		literal.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		literal.setHealth(3.0F);
		literal.setPumpkin(false);
		literal.setCustomName(new TextComponent(
				"Vanilla Swirl"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		literal.invulnerableTime = 37;
		boat.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		passenger.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		leashHolder.setPos(conversionPos.getX() + 1.0D,
				conversionPos.getY(),
				conversionPos.getZ());
		helper.getLevel().addFreshEntity(boat);
		helper.getLevel().addFreshEntity(passenger);
		helper.getLevel().addFreshEntity(leashHolder);
		helper.getLevel().addFreshEntity(literal);
		literal.startRiding(boat, true);
		passenger.startRiding(literal, true);
		literal.moveTo(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ(), 31.0F, 0.0F);
		IceCreamGolem converted =
				CakeWorldSnowGolemReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		String conversionState = converted == null
				? "converted=null, biome="
						+ helper.getLevel()
								.getBiome(
										literal
												.blockPosition())
								.unwrapKey()
								.map(key -> key
										.location())
								.orElse(null)
				: "health=" + converted.getHealth()
						+ ", pumpkin="
						+ converted.hasPumpkin()
						+ ", name="
						+ converted.getName()
								.getString()
						+ ", persistent="
						+ converted
								.isPersistenceRequired()
						+ ", noAi="
						+ converted.isNoAi()
						+ ", invulnerable="
						+ converted.isInvulnerable()
						+ ", invulnerableTime="
						+ converted.invulnerableTime
						+ ", vehicle="
						+ converted.getVehicle()
						+ ", passengerVehicle="
						+ passenger.getVehicle()
						+ ", passengers="
						+ converted.getPassengers()
						+ ", leash="
						+ converted.getLeashHolder();
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.ICE_CREAM_GOLEM
										.get()
						&& close(converted.getHealth(),
								3.0D)
						&& !converted.hasPumpkin()
						&& "Vanilla Swirl".equals(
								converted.getName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.invulnerableTime
								== 37
						&& converted.getVehicle()
								== boat
						&& passenger.getVehicle()
								== converted
						&& converted.getPassengers()
								.contains(passenger),
				"Fresh literal Snow Golem conversion lost health, pumpkin, state, invulnerability, vehicle or passenger: "
						+ conversionState);
		passenger.discard();
		converted.discard();
		boat.discard();

		SnowGolem leashedLiteral =
				EntityType.SNOW_GOLEM.create(
						helper.getLevel());
		require(helper, leashedLiteral != null,
				"Could not create leashed Snow Golem conversion fixture");
		leashedLiteral.setPos(conversionPos.getX(),
				conversionPos.getY(),
				conversionPos.getZ());
		helper.getLevel().addFreshEntity(
				leashedLiteral);
		leashedLiteral.setLeashedTo(
				leashHolder, true);
		IceCreamGolem leashedConverted =
				CakeWorldSnowGolemReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								leashedLiteral);
		require(helper,
				leashedConverted != null
						&& leashedLiteral.isRemoved()
						&& leashedConverted
								.getLeashHolder()
								== leashHolder,
				"Fresh literal Snow Golem conversion lost a valid leash relationship");
		leashedConverted.discard();
		leashHolder.discard();

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MISC)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SNOW_GOLEM
											|| spawn.type
													== CakeWorldEntities
															.ICE_CREAM_GOLEM
															.get()),
					"Snow Golem role leaked into ordinary biome spawning in "
							+ biomeId);
		}
		Advancement summonIronGolem =
				helper.getLevel().getServer()
						.getAdvancements()
						.getAdvancement(
								new ResourceLocation(
										"minecraft",
										"adventure/summon_iron_golem"));
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.ICE_CREAM_GOLEM
										.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.SNOW_GOLEM)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.ICE_CREAM_GOLEM
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.SNOW_GOLEM)
						&& golem.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/ice_cream_golem"))
						&& CakeWorldItems
								.ICE_CREAM_GOLEM_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.ICE_CREAM_GOLEM
												.get())
								== null
						&& summonIronGolem != null
						&& summonIronGolem.getCriteria()
								.size() == 1
						&& summonIronGolem.getCriteria()
								.containsKey(
										"summoned_golem"),
				"Ice-Cream Golem lost exact dormant placement, loot, egg, no-mimic or no-invented-advancement contract");

		BlockPos buildBottom =
				conversionPos.above(5);
		helper.getLevel().setBlock(buildBottom,
				Blocks.SNOW_BLOCK
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				buildBottom.above(),
				Blocks.SNOW_BLOCK
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				buildBottom.above(2),
				Blocks.CARVED_PUMPKIN
						.defaultBlockState(), 3);
		AABB buildArea = new AABB(buildBottom)
				.inflate(3.0D);
		require(helper,
				helper.getLevel()
								.getEntitiesOfClass(
										SnowGolem.class,
										buildArea,
										entity -> entity
												.getType()
												== EntityType
														.SNOW_GOLEM)
								.size() == 1
						&& helper.getLevel()
								.isEmptyBlock(buildBottom)
						&& helper.getLevel()
								.isEmptyBlock(
										buildBottom
												.above())
						&& helper.getLevel()
								.isEmptyBlock(
										buildBottom
												.above(2)),
				"Vanilla two-Snow-Block and carved-pumpkin construction did not create and consume the literal Snow Golem pattern");
		helper.runAfterDelay(4, () -> {
			List<IceCreamGolem> built =
					helper.getLevel()
							.getEntitiesOfClass(
									IceCreamGolem.class,
									buildArea);
			require(helper,
					built.size() == 1
							&& built.get(0).hasPumpkin()
							&& helper.getLevel()
									.getEntitiesOfClass(
											SnowGolem.class,
											buildArea,
											entity -> entity
													.getType()
													== EntityType
															.SNOW_GOLEM)
									.isEmpty(),
					"Fresh player-built Snow Golem did not defer-convert to exactly one pumpkin-wearing Ice-Cream Golem");
			built.forEach(Entity::discard);
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void liquoriceWeaversKeepClimbingJockeysNightTemperamentAndSafeWebs(
			GameTestHelper helper) {
		LiquoriceWeaverProbe weaver =
				new LiquoriceWeaverProbe(
						helper.getLevel());
		require(helper,
				weaver instanceof Spider
						&& weaver.getType()
								== CakeWorldEntities
										.LIQUORICE_WEAVER
										.get()
						&& weaver.getType().getCategory()
								== MobCategory.MONSTER
						&& close(weaver.getMaxHealth(),
								16.0D)
						&& close(weaver.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.3D)
						&& close(weaver.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								2.0D)
						&& close(weaver.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								16.0D)
						&& close(weaver.getDimensions(
								Pose.STANDING).width,
								1.4D)
						&& close(weaver.getDimensions(
								Pose.STANDING).height,
								0.9D)
						&& close(weaver.standingEyeHeight(),
								0.65D)
						&& close(weaver
								.getPassengersRidingOffset(),
								0.45D)
						&& weaver.getType()
								.clientTrackingRange() == 8
						&& weaver.getMaxSpawnClusterSize()
								== 4
						&& weaver.experienceReward() == 5
						&& weaver.despawnsInPeaceful()
						&& weaver.getMobType()
								== MobType.ARTHROPOD
						&& weaver.getNavigation()
								instanceof WallClimberNavigation,
				"Liquorice Weaver lost exact Spider body, attributes, XP, passenger, Peaceful, arthropod or wall-navigation role");
		require(helper,
				weaver.goalPriority("FloatGoal") == 1
						&& weaver.goalPriority(
								"LeapAtTargetGoal")
								== 3
						&& weaver.goalPriority(
								"SpiderAttackGoal")
								== 4
						&& weaver.goalPriority(
								"WaterAvoidingRandomStrollGoal")
								== 5
						&& weaver.goalPriority(
								"LookAtPlayerGoal")
								== 6
						&& weaver.goalPriority(
								"RandomLookAroundGoal")
								== 6
						&& weaver.targetGoalPriority(
								"HurtByTargetGoal")
								== 1
						&& weaver.countTargetGoalsNamed(
								"SpiderTargetGoal")
								== 2,
				"Liquorice Weaver lost exact Spider movement, melee or Player/Iron-Golem target goals");
		require(helper,
				weaver.ambientSound()
								== SoundEvents
										.SPIDER_AMBIENT
						&& weaver.hurtSound()
								== SoundEvents
										.SPIDER_HURT
						&& weaver.deathSound()
								== SoundEvents
										.SPIDER_DEATH,
				"Liquorice Weaver lost exact Spider sounds");
		weaver.clearLastSound();
		weaver.playStep();
		require(helper,
				weaver.lastSound()
								== SoundEvents
										.SPIDER_STEP
						&& close(weaver.lastVolume(),
								0.15D)
						&& close(weaver.lastPitch(),
								1.0D),
				"Liquorice Weaver lost exact Spider step cue");

		weaver.setClimbing(true);
		weaver.makeStuckInBlock(
				Blocks.COBWEB.defaultBlockState(),
				new Vec3(0.25D, 0.05D, 0.25D));
		Vec3 cobwebMultiplier =
				weaver.stuckMultiplier();
		weaver.makeStuckInBlock(
				Blocks.HONEY_BLOCK
						.defaultBlockState(),
				new Vec3(0.25D, 0.05D, 0.25D));
		require(helper,
				weaver.onClimbable()
						&& weaver.isClimbing()
						&& cobwebMultiplier
								.equals(Vec3.ZERO)
						&& weaver.stuckMultiplier()
								.equals(new Vec3(
										0.25D,
										0.05D,
										0.25D))
						&& !weaver.canBeAffected(
								new MobEffectInstance(
										MobEffects
												.POISON,
										100))
						&& weaver.canBeAffected(
								new MobEffectInstance(
										MobEffects
												.MOVEMENT_SPEED,
										100)),
				"Liquorice Weaver lost climbing, cobweb immunity, ordinary stuck handling or poison immunity");

		Pig brightTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, brightTarget != null,
				"Could not create Liquorice Weaver daylight target");
		brightTarget.setPos(weaver.getX() + 1.0D,
				weaver.getY(), weaver.getZ());
		weaver.setTestBrightness(1.0F);
		weaver.setTestRandom(new FixedRandom(0));
		weaver.setTarget(brightTarget);
		require(helper,
				!weaver.attackGoalContinues()
						&& weaver.getTarget() == null
						&& !weaver
								.anySpiderTargetCanUse(),
				"Liquorice Weaver did not retain bright-light neutrality for attack and target goals");
		weaver.clearTestEnvironment();

		DifficultyInstance saturatedHard =
				new DifficultyInstance(
						Difficulty.HARD,
						1440000L, 3600000L,
						1.0F);
		LiquoriceWeaver hardEffectWeaver =
				CakeWorldEntities.LIQUORICE_WEAVER
						.get().create(helper.getLevel());
		LiquoriceWeaver normalEffectWeaver =
				CakeWorldEntities.LIQUORICE_WEAVER
						.get().create(helper.getLevel());
		require(helper,
				hardEffectWeaver != null
						&& normalEffectWeaver != null,
				"Could not create Liquorice Weaver spawn-effect fixtures");
		SpawnGroupData hardGroup =
				hardEffectWeaver.finalizeSpawn(
						controlledSpiderAccessor(
								helper.getLevel(),
								Difficulty.HARD,
								new SpiderSpawnRandom(
										1, 0.0F,
										4)),
						saturatedHard,
						MobSpawnType.NATURAL,
						null, null);
		SpawnGroupData normalGroup =
				normalEffectWeaver.finalizeSpawn(
						controlledSpiderAccessor(
								helper.getLevel(),
								Difficulty.NORMAL,
								new SpiderSpawnRandom(
										1, 0.0F,
										4)),
						new DifficultyInstance(
								Difficulty.NORMAL,
								1440000L,
								3600000L,
								1.0F),
						MobSpawnType.NATURAL,
						null, null);
		MobEffectInstance hardInvisibility =
				hardEffectWeaver.getEffect(
						MobEffects.INVISIBILITY);
		require(helper,
				hardGroup instanceof
								Spider
										.SpiderEffectsGroupData
						&& ((Spider
								.SpiderEffectsGroupData)
								hardGroup).effect
								== MobEffects
										.INVISIBILITY
						&& hardInvisibility != null
						&& hardInvisibility
								.getDuration()
								== Integer.MAX_VALUE
						&& normalGroup instanceof
								Spider
										.SpiderEffectsGroupData
						&& ((Spider
								.SpiderEffectsGroupData)
								normalGroup).effect
								== null
						&& normalEffectWeaver
								.getActiveEffects()
								.isEmpty(),
				"Liquorice Weaver lost Hard local-difficulty random effects or incorrectly applied them on Normal");

		BlockPos localPos = helper.absolutePos(
				new BlockPos(1, 3, 1));
		weaver.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		brightTarget.setPos(localPos.getX() + 1.0D,
				localPos.getY(), localPos.getZ());
		helper.getLevel().addFreshEntity(weaver);
		helper.getLevel().addFreshEntity(
				brightTarget);
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					List.of(Difficulty.EASY,
							Difficulty.NORMAL)) {
				helper.getLevel().getServer()
						.setDifficulty(
								safeDifficulty,
								true);
				brightTarget.removeAllEffects();
				brightTarget.setHealth(20.0F);
				brightTarget.invulnerableTime = 0;
				brightTarget.fallDistance = 40.0F;
				brightTarget.setSecondsOnFire(5);
				weaver.clearLastSound();
				float safeStartingHealth =
						brightTarget.getHealth();
				boolean safeHit =
						weaver.doHurtTarget(
								brightTarget);
				String safeState =
						"hit=" + safeHit
						+ ", health="
						+ brightTarget.getHealth()
						+ "/"
						+ safeStartingHealth
						+ ", fire="
						+ brightTarget.isOnFire()
						+ ", fall="
						+ brightTarget.fallDistance
						+ ", slow="
						+ brightTarget.getEffect(
								MobEffects
										.MOVEMENT_SLOWDOWN)
						+ ", fatigue="
						+ brightTarget.getEffect(
								MobEffects
										.DIG_SLOWDOWN)
						+ ", glow="
						+ brightTarget.getEffect(
								MobEffects.GLOWING)
						+ ", slowFall="
						+ brightTarget.getEffect(
								MobEffects
										.SLOW_FALLING)
						+ ", fireResist="
						+ brightTarget.getEffect(
								MobEffects
										.FIRE_RESISTANCE)
						+ ", resist="
						+ brightTarget.getEffect(
								MobEffects
										.DAMAGE_RESISTANCE)
						+ ", sound="
						+ weaver.lastSound();
				require(helper,
						safeHit
								&& close(
										brightTarget
												.getHealth(),
										safeStartingHealth)
								&& !brightTarget
										.isOnFire()
								&& close(
										brightTarget
												.fallDistance,
										0.0D)
								&& brightTarget
										.hasEffect(
												MobEffects
														.MOVEMENT_SLOWDOWN)
								&& brightTarget
										.getEffect(
												MobEffects
														.MOVEMENT_SLOWDOWN)
										.getAmplifier()
										== 1
								&& brightTarget
										.hasEffect(
												MobEffects
														.DIG_SLOWDOWN)
								&& brightTarget
										.hasEffect(
												MobEffects
														.GLOWING)
								&& brightTarget
										.hasEffect(
												MobEffects
														.SLOW_FALLING)
								&& brightTarget
										.hasEffect(
												MobEffects
														.FIRE_RESISTANCE)
								&& brightTarget
										.hasEffect(
												MobEffects
														.DAMAGE_RESISTANCE)
								&& weaver
										.lastSound()
										== SoundEvents
												.SLIME_SQUISH,
						safeDifficulty
								+ " Liquorice Weaver bite was not a harmless visible web splat with complete rescue: "
								+ safeState);
			}
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			brightTarget.removeAllEffects();
			brightTarget.setHealth(20.0F);
			brightTarget.invulnerableTime = 0;
			float hardStartingHealth =
					brightTarget.getHealth();
			require(helper,
					weaver.doHurtTarget(
									brightTarget)
							&& close(
									brightTarget
											.getHealth(),
									hardStartingHealth
											- 2.0D)
							&& brightTarget
									.getActiveEffects()
									.isEmpty(),
					"Hard Liquorice Weaver did not retain the exact unmodified two-point Spider bite");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}
		brightTarget.discard();
		weaver.discard();

		Spider literal =
				EntityType.SPIDER.create(
						helper.getLevel());
		Boat vehicle = EntityType.BOAT.create(
				helper.getLevel());
		Pig passenger =
				EntityType.PIG.create(helper.getLevel());
		require(helper,
				literal != null && vehicle != null
						&& passenger != null,
				"Could not create Liquorice Weaver literal-conversion fixtures");
		literal.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		literal.setHealth(12.0F);
		literal.setCustomName(new TextComponent(
				"Twisty Lace"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		literal.invulnerableTime = 31;
		literal.addEffect(new MobEffectInstance(
				MobEffects.MOVEMENT_SPEED,
				800, 1));
		vehicle.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		passenger.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		helper.getLevel().addFreshEntity(vehicle);
		helper.getLevel().addFreshEntity(passenger);
		helper.getLevel().addFreshEntity(literal);
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		LiquoriceWeaver converted =
				CakeWorldSpiderReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& close(converted.getHealth(),
								12.0D)
						&& "Twisty Lace".equals(
								converted.getName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.invulnerableTime
								== 31
						&& converted.getEffect(
								MobEffects
										.MOVEMENT_SPEED)
								.getAmplifier() == 1
						&& converted.getVehicle()
								== vehicle
						&& passenger.getVehicle()
								== converted,
				"Fresh literal Spider conversion lost health, name, state, effects, vehicle or passenger");
		passenger.discard();
		converted.discard();
		vehicle.discard();

		LiquoriceWeaver jockey =
				CakeWorldEntities.LIQUORICE_WEAVER
						.get().create(helper.getLevel());
		require(helper, jockey != null,
				"Could not create Liquorice Weaver jockey fixture");
		jockey.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		jockey.finalizeSpawn(
				controlledSpiderAccessor(
						helper.getLevel(),
						Difficulty.NORMAL,
						new SpiderSpawnRandom(
								0, 0.99F, 0)),
				new DifficultyInstance(
						Difficulty.NORMAL,
						0L, 0L, 0.0F),
				MobSpawnType.NATURAL,
				null, null);
		Skeleton literalJockey =
				jockey.getFirstPassenger()
						instanceof Skeleton skeleton
								? skeleton : null;
		require(helper,
				literalJockey != null
						&& literalJockey.getType()
								== EntityType.SKELETON
						&& literalJockey.getVehicle()
								== jockey,
				"Liquorice Weaver lost the exact inherited one-percent literal Skeleton-jockey construction");
		helper.getLevel().addFreshEntity(jockey);
		helper.getLevel().addFreshEntity(
				literalJockey);
		CandyCaneArcher candyJockey =
				CakeWorldSkeletonReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literalJockey);
		require(helper,
				candyJockey != null
						&& literalJockey.isRemoved()
						&& candyJockey.getVehicle()
								== jockey
						&& jockey.getPassengers()
								.contains(candyJockey),
				"Liquorice Weaver jockey did not hand the literal Skeleton rider to Candy-Cane Archer without losing the mount");
		candyJockey.discard();
		jockey.discard();

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId())) {
			Biome biome = biomes.get(biomeId);
			List<MobSpawnSettings.SpawnerData>
					spiderProfiles = biome
							.getMobSettings()
							.getMobs(
									MobCategory
											.MONSTER)
							.unwrap().stream()
							.filter(spawn ->
									spawn.type
											== EntityType
													.SPIDER
									|| spawn.type
											== CakeWorldEntities
													.LIQUORICE_WEAVER
													.get())
							.toList();
			require(helper,
					spiderProfiles.size() == 1
							&& spiderProfiles.get(0).type
									== CakeWorldEntities
											.LIQUORICE_WEAVER
											.get()
							&& spiderProfiles.get(0)
									.getWeight()
									.asInt() == 100
							&& spiderProfiles.get(0)
									.minCount == 4
							&& spiderProfiles.get(0)
									.maxCount == 4,
					"Liquorice Weaver did not exactly replace the inherited 100/4-4 Spider profile in "
							+ biomeId + ": "
							+ spiderProfiles);
		}
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SPIDER
											|| spawn.type
													== CakeWorldEntities
															.LIQUORICE_WEAVER
															.get()),
					"Liquorice Weaver invented a Spider profile in "
							+ biomeId);
		}
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.LIQUORICE_WEAVER
										.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.SPIDER)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.LIQUORICE_WEAVER
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.SPIDER)
						&& weaver
								.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/liquorice_weaver"))
						&& CakeWorldItems
								.LIQUORICE_WEAVER_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.LIQUORICE_WEAVER
												.get())
								== SoundEvents
										.PARROT_IMITATE_SPIDER,
				"Liquorice Weaver lost exact placement, loot, egg or Lorikeet mimic");

		ServerPlayer advancementPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2054"),
								"CakeWorldLiquoriceWeaverRoleTest"));
		VanillaRoleAdvancements.onDeath(
				new LivingDeathEvent(
						hardEffectWeaver,
						DamageSource.playerAttack(
								advancementPlayer)));
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:spider");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void liquoriceSquidKeepSurfaceSchoolsSwimmingInkAndPassiveRole(
			GameTestHelper helper) {
		LiquoriceSquidProbe squid =
				new LiquoriceSquidProbe(
						helper.getLevel());
		require(helper,
				squid instanceof Squid
						&& squid instanceof WaterAnimal
						&& squid.getType()
								== CakeWorldEntities
										.LIQUORICE_SQUID
										.get()
						&& squid.getType().getCategory()
								== MobCategory.WATER_CREATURE
						&& close(squid.getMaxHealth(),
								10.0D)
						&& close(squid.getDimensions(
								Pose.STANDING).width,
								0.8D)
						&& close(squid.getDimensions(
								Pose.STANDING).height,
								0.8D)
						&& close(squid.standingEyeHeight(),
								0.4D)
						&& squid.getType()
								.clientTrackingRange() == 8
						&& squid.getType()
								.clientTrackingRange()
								== EntityType.SQUID
										.clientTrackingRange()
						&& squid.getMaxSpawnClusterSize()
								== 4
						&& squid.getMobType()
								== MobType.WATER
						&& squid.canBreatheUnderwater()
						&& !squid.isPushedByFluid()
						&& squid.getAmbientSoundInterval()
								== 120
						&& !squid.despawnsInPeaceful()
						&& squid.movementEmission()
								== Entity
										.MovementEmission
										.EVENTS,
				"Liquorice Squid lost exact Squid body, water, eye, tracking, cluster, Peaceful or movement-emission role");
		require(helper,
				squid.goalPriority(
						"SquidRandomMovementGoal")
								== 0
						&& squid.goalPriority(
								"SquidFleeGoal") == 1
						&& squid.goalCount() == 2
						&& squid.targetGoalCount() == 0,
				"Liquorice Squid lost exact passive random-swim/flee goals or invented a target");
		require(helper,
				squid.ambientSound()
								== SoundEvents
										.SQUID_AMBIENT
						&& squid.hurtSound()
								== SoundEvents
										.SQUID_HURT
						&& squid.deathSound()
								== SoundEvents
										.SQUID_DEATH
						&& squid.squirtSound()
								== SoundEvents
										.SQUID_SQUIRT
						&& close(squid.soundVolume(),
								0.4D),
				"Liquorice Squid lost exact ambient, hurt, death, squirt or volume contract");

		Player leashTester = helper.makeMockPlayer();
		require(helper, squid.canBeLeashed(leashTester),
				"Liquorice Squid lost Squid leashability");
		for (int i = 0; i < 32; i++) {
			int experience = squid.experienceReward();
			require(helper,
					experience >= 1
							&& experience <= 3,
					"Liquorice Squid XP escaped the exact one-to-three range: "
							+ experience);
		}

		BlockPos localPos = helper.absolutePos(
				new BlockPos(1, 3, 1));
		squid.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		squid.noPhysics = true;
		squid.setMovementVector(
				0.2F, -0.05F, 0.1F);
		squid.tentacleMovement = 2.0F;
		squid.handleEntityEvent((byte)19);
		Vec3 movementStart = squid.position();
		squid.setDeltaMovement(
				0.25D, 0.1D, -0.2D);
		squid.travel(Vec3.ZERO);
		Vec3 travelled = squid.position()
				.subtract(movementStart);
		require(helper,
				squid.hasMovementVector()
						&& close(
								squid.tentacleMovement,
								0.0D)
						&& close(travelled.x, 0.25D)
						&& close(travelled.y, 0.1D)
						&& close(travelled.z, -0.2D),
				"Liquorice Squid lost movement-vector, tentacle reset or direct aquatic travel");
		squid.noPhysics = false;

		LiquoriceSquidProbe dry =
				new LiquoriceSquidProbe(
						helper.getLevel());
		dry.setPos(localPos.getX(),
				localPos.getY() + 10.0D,
				localPos.getZ());
		dry.setHealth(10.0F);
		dry.invulnerableTime = 0;
		dry.testHandleAir(-19);
		require(helper,
				dry.getAirSupply() == 0
						&& close(dry.getHealth(), 8.0D),
				"Liquorice Squid lost exact dry-air countdown damage");

		squid.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		squid.setHealth(10.0F);
		squid.invulnerableTime = 0;
		squid.clearInkEvidence();
		helper.getLevel().addFreshEntity(squid);
		Player attacker = helper.makeMockPlayer();
		attacker.setPos(localPos.getX() + 1.0D,
				localPos.getY(), localPos.getZ());
		require(helper,
				squid.hurt(
						DamageSource.playerAttack(
								attacker),
						1.0F)
						&& close(squid.getHealth(),
								9.0D)
						&& squid.inkParticleRequests()
								== 30
						&& squid.lastSound()
								== SoundEvents
										.SQUID_SQUIRT
						&& close(squid.lastVolume(),
								0.4D),
				"Liquorice Squid lost its exact thirty-particle ink defence or squirt cue");
		squid.discard();
		attacker.discard();
		leashTester.discard();

		int seaLevel = helper.getLevel()
				.getSeaLevel();
		BlockPos surfacePos = new BlockPos(
				localPos.getX(), seaLevel - 5,
				localPos.getZ());
		List<BlockPos> spawnCells = List.of(
				surfacePos.below(), surfacePos,
				surfacePos.above());
		Map<BlockPos, BlockState> originalStates =
				spawnCells.stream().collect(
						java.util.stream.Collectors
								.toMap(pos -> pos,
										pos -> helper
												.getLevel()
												.getBlockState(
														pos)));
		for (BlockPos pos : spawnCells) {
			helper.getLevel().setBlock(pos,
					Blocks.WATER
							.defaultBlockState(),
					3);
		}
		boolean vanillaWater =
				WaterAnimal
						.checkSurfaceWaterAnimalSpawnRules(
								EntityType.SQUID,
								helper.getLevel(),
								MobSpawnType.NATURAL,
								surfacePos,
								new Random(1978L));
		boolean customWater =
				LiquoriceSquid
						.checkLiquoriceSquidSpawnRules(
								CakeWorldEntities
										.LIQUORICE_SQUID
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								surfacePos,
								new Random(1978L));
		for (BlockPos pos : spawnCells) {
			helper.getLevel().setBlock(pos,
					CakeWorldFluids.LEMONADE_BLOCK
							.get().defaultBlockState(),
					3);
		}
		boolean vanillaLemonade =
				WaterAnimal
						.checkSurfaceWaterAnimalSpawnRules(
								EntityType.SQUID,
								helper.getLevel(),
								MobSpawnType.NATURAL,
								surfacePos,
								new Random(1978L));
		boolean customLemonade =
				LiquoriceSquid
						.checkLiquoriceSquidSpawnRules(
								CakeWorldEntities
										.LIQUORICE_SQUID
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								surfacePos,
								new Random(1978L));
		originalStates.forEach((pos, state) ->
				helper.getLevel().setBlock(
						pos, state, 3));
		require(helper,
				vanillaWater && customWater
						&& !vanillaLemonade
						&& customLemonade
						&& !LiquoriceSquid
								.checkLiquoriceSquidSpawnRules(
										CakeWorldEntities
												.LIQUORICE_SQUID
												.get(),
										helper.getLevel(),
										MobSpawnType
												.NATURAL,
										new BlockPos(
												surfacePos
														.getX(),
												seaLevel
														- 14,
												surfacePos
														.getZ()),
										new Random(
												1978L)),
				"Liquorice Squid did not preserve the exact surface band while adapting literal Water to water-tagged Lemonade");

		Squid literal = EntityType.SQUID.create(
				helper.getLevel());
		Boat vehicle = EntityType.BOAT.create(
				helper.getLevel());
		Pig passenger = EntityType.PIG.create(
				helper.getLevel());
		require(helper,
				literal != null && vehicle != null
						&& passenger != null,
				"Could not create Liquorice Squid state-conversion fixtures");
		literal.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		literal.setHealth(7.0F);
		literal.setAirSupply(155);
		literal.setCustomName(new TextComponent(
				"Inky Twist"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		literal.invulnerableTime = 27;
		vehicle.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		passenger.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		helper.getLevel().addFreshEntity(vehicle);
		helper.getLevel().addFreshEntity(passenger);
		helper.getLevel().addFreshEntity(literal);
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		LiquoriceSquid converted =
				CakeWorldSquidReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& close(converted.getHealth(),
								7.0D)
						&& converted.getAirSupply()
								== 155
						&& "Inky Twist".equals(
								converted.getName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.invulnerableTime
								== 27
						&& converted.getVehicle()
								== vehicle
						&& passenger.getVehicle()
								== converted,
				"Fresh literal Squid conversion lost health, air, name, state, vehicle or passenger");
		passenger.discard();
		converted.discard();
		vehicle.discard();

		Squid leashedLiteral =
				EntityType.SQUID.create(
						helper.getLevel());
		Boat leashHolder = EntityType.BOAT.create(
				helper.getLevel());
		require(helper,
				leashedLiteral != null
						&& leashHolder != null,
				"Could not create Liquorice Squid leash-conversion fixtures");
		leashedLiteral.setPos(localPos.getX(),
				localPos.getY(), localPos.getZ());
		leashHolder.setPos(localPos.getX() + 1.0D,
				localPos.getY(), localPos.getZ());
		helper.getLevel().addFreshEntity(leashHolder);
		helper.getLevel().addFreshEntity(
				leashedLiteral);
		leashedLiteral.setLeashedTo(
				leashHolder, true);
		LiquoriceSquid leashedConverted =
				CakeWorldSquidReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								leashedLiteral);
		require(helper,
				leashedConverted != null
						&& leashedLiteral.isRemoved()
						&& leashedConverted
								.getLeashHolder()
								== leashHolder,
				"Fresh literal Squid conversion lost its leash holder");
		leashedConverted.discard();
		leashHolder.discard();

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome sodaOcean = biomes.get(
				CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Liquorice Squid Soda Ocean ecology");
		List<MobSpawnSettings.SpawnerData>
				squidProfiles = sodaOcean
						.getMobSettings()
						.getMobs(
								MobCategory
										.WATER_CREATURE)
						.unwrap().stream()
						.filter(spawn ->
								spawn.type
										== EntityType
												.SQUID
								|| spawn.type
										== CakeWorldEntities
												.LIQUORICE_SQUID
												.get())
						.toList();
		require(helper,
				squidProfiles.size() == 1
						&& squidProfiles.get(0).type
								== CakeWorldEntities
										.LIQUORICE_SQUID
										.get()
						&& squidProfiles.get(0)
								.getWeight()
								.asInt() == 1
						&& squidProfiles.get(0)
								.minCount == 1
						&& squidProfiles.get(0)
								.maxCount == 4,
				"Liquorice Squid did not exactly replace Soda Ocean's inherited 1/1-4 Squid school: "
						+ squidProfiles);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.WATER_CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.SQUID
											|| spawn.type
													== CakeWorldEntities
															.LIQUORICE_SQUID
															.get()),
					"Liquorice Squid invented an ecology profile in "
							+ biomeId);
		}

		TagKey<EntityType<?>> axolotlPrey =
				TagKey.create(
						Registry
								.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft",
								"axolotl_hunt_targets"));
		Advancement killAll = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"adventure/kill_all_mobs"));
		require(helper,
				SpawnPlacements.getPlacementType(
								CakeWorldEntities
										.LIQUORICE_SQUID
										.get())
								== SpawnPlacements
										.Type.IN_WATER
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.LIQUORICE_SQUID
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.SQUID)
						&& dry.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/liquorice_squid"))
						&& CakeWorldEntities
								.LIQUORICE_SQUID.get()
								.is(axolotlPrey)
						&& CakeWorldItems
								.LIQUORICE_SQUID_SPAWN_EGG
								.isPresent()
						&& killAll != null
						&& !killAll.getCriteria()
								.containsKey(
										"minecraft:squid")
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.LIQUORICE_SQUID
												.get())
								== null,
				"Liquorice Squid lost placement, loot, Axolotl prey or egg role, or invented advancement/mimic progress");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void fudgeFolkKeepPiglinSocietyBarterAndSafePeril(
			GameTestHelper helper) {
		FudgeFolkProbe folk =
				new FudgeFolkProbe(helper.getLevel());
		require(helper,
				folk instanceof Piglin
						&& folk instanceof CrossbowAttackMob
						&& folk.getType()
								== CakeWorldEntities
										.FUDGE_FOLK.get()
						&& folk.getType().getCategory()
								== MobCategory.MONSTER
						&& close(folk.getMaxHealth(), 16.0D)
						&& close(folk.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.35D)
						&& close(folk.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								5.0D)
						&& close(folk.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(folk.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& folk.getInventory()
								.getContainerSize() == 8
						&& folk.getExperienceValue() == 5
						&& folk.canHuntRole()
						&& !folk.despawnsInPeaceful()
						&& folk.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/fudge_folk")),
				"Fudge Folk lost exact Piglin type, body, attributes, brain inventory, hunt, Peaceful or loot roles");

		BlockPos societyPos = helper.absolutePos(
				new BlockPos(2, 3, 2));
		folk.setPos(societyPos.getX() + 0.5D,
				societyPos.getY(),
				societyPos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(folk);
		ServerPlayer barterPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2039"),
						"CakeWorldFudgeFolkBarterTest"));
		barterPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.GOLD_INGOT));
		InteractionResult barter = folk.mobInteract(
				barterPlayer, InteractionHand.MAIN_HAND);
		require(helper,
				barter == InteractionResult.CONSUME
						&& barterPlayer.getMainHandItem()
								.isEmpty()
						&& folk.getOffhandItem()
								.is(Items.GOLD_INGOT)
						&& folk.getBrain().hasMemoryValue(
								MemoryModuleType
										.ADMIRING_ITEM),
				"Fudge Folk lost direct gold admiration");
		requireCriterion(helper, barterPlayer,
				"minecraft:nether/distract_piglin",
				"distract_piglin_directly");
		VanillaRoleAdvancements.creditDistractedPiglinRole(
				barterPlayer, false);
		requireCriterion(helper, barterPlayer,
				"minecraft:nether/distract_piglin",
				"distract_piglin");

		folk.getBrain().eraseMemory(
				MemoryModuleType.ADMIRING_ITEM);
		folk.runServerAiStep();
		List<ItemEntity> barterDrops = helper.getLevel()
				.getEntitiesOfClass(
						ItemEntity.class,
						folk.getBoundingBox()
								.inflate(8.0D));
		require(helper,
				folk.getOffhandItem().isEmpty()
						&& !barterDrops.isEmpty()
						&& barterDrops.stream().allMatch(
								drop -> !drop.getItem()
										.isEmpty()),
				"Fudge Folk did not complete the inherited Piglin barter table after admiration");

		FudgeBoar hunted =
				CakeWorldEntities.FUDGE_BOAR.get()
						.create(helper.getLevel());
		FudgeFolk peer =
				CakeWorldEntities.FUDGE_FOLK.get()
						.create(helper.getLevel());
		require(helper, hunted != null && peer != null,
				"Could not create Fudge Folk society sensor fixtures");
		hunted.setPos(folk.getX() + 2.0D,
				folk.getY(), folk.getZ());
		peer.setPos(folk.getX() + 1.0D,
				folk.getY(), folk.getZ() + 1.0D);
		hunted.setNoAi(true);
		peer.setNoAi(true);
		helper.getLevel().addFreshEntity(hunted);
		helper.getLevel().addFreshEntity(peer);
		NearestLivingEntitySensor nearest =
				new NearestLivingEntitySensor();
		PiglinSpecificSensor piglinSensor =
				new PiglinSpecificSensor();
		for (int scan = 0; scan < 21; ++scan) {
			nearest.tick(helper.getLevel(), folk);
		}
		for (int scan = 0; scan < 21; ++scan) {
			piglinSensor.tick(helper.getLevel(), folk);
		}
		require(helper,
				folk.getBrain().getMemory(
						MemoryModuleType
								.NEAREST_VISIBLE_HUNTABLE_HOGLIN)
						.filter(found -> found == hunted)
						.isPresent()
						&& folk.getBrain().getMemory(
								MemoryModuleType
										.NEAREST_VISIBLE_ADULT_PIGLINS)
								.orElse(List.of())
								.contains(peer),
				"Piglin sensors did not recognise Fudge Folk society or the Fudge Boar hunt role");
		folk.rememberFudgeBoarHunt();
		require(helper,
				folk.getBrain().hasMemoryValue(
						MemoryModuleType.HUNTED_RECENTLY)
						&& peer.getBrain().hasMemoryValue(
								MemoryModuleType
										.HUNTED_RECENTLY),
				"Fudge Boar kill bookkeeping did not reach visible Fudge Folk");

		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		ZombifiedPiglin conversion = null;
		try {
			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			folk.getBrain().setMemory(
					MemoryModuleType
							.VISIBLE_ADULT_PIGLIN_COUNT,
					0);
			folk.getBrain().setMemory(
					MemoryModuleType
							.VISIBLE_ADULT_HOGLIN_COUNT,
					3);
			require(helper,
					folk.hurt(
							DamageSource.mobAttack(hunted),
							1.0F)
							&& folk.getBrain()
									.getMemory(
											MemoryModuleType
													.AVOID_TARGET)
									.filter(target ->
											target == hunted)
									.isPresent()
							&& !folk.getBrain()
									.hasMemoryValue(
											MemoryModuleType
													.ATTACK_TARGET),
					"Outnumbered Fudge Folk did not repair the literal-Hoglin retreat seam");

			FudgeBoar retreating =
					CakeWorldEntities.FUDGE_BOAR.get()
							.create(helper.getLevel());
			FudgeFolk pressure =
					CakeWorldEntities.FUDGE_FOLK.get()
							.create(helper.getLevel());
			require(helper,
					retreating != null && pressure != null,
					"Could not create reciprocal Fudge Folk retreat fixtures");
			retreating.setPos(folk.getX() + 4.0D,
					folk.getY(), folk.getZ());
			pressure.setPos(retreating.getX() + 1.0D,
					retreating.getY(), retreating.getZ());
			helper.getLevel().addFreshEntity(retreating);
			helper.getLevel().addFreshEntity(pressure);
			retreating.getBrain().setMemory(
					MemoryModuleType
							.VISIBLE_ADULT_PIGLIN_COUNT,
					3);
			retreating.getBrain().setMemory(
					MemoryModuleType
							.VISIBLE_ADULT_HOGLIN_COUNT,
					0);
			retreating.doHurtTarget(pressure);
			require(helper,
					retreating.getBrain().getMemory(
							MemoryModuleType.AVOID_TARGET)
							.filter(target ->
									target == pressure)
							.isPresent()
							&& !retreating.getBrain()
									.hasMemoryValue(
											MemoryModuleType
													.ATTACK_TARGET),
					"Fudge Boar did not recognise the custom Piglin family when outnumbered");

			for (Difficulty safeDifficulty :
					List.of(Difficulty.EASY,
							Difficulty.NORMAL)) {
				helper.getLevel().getServer().setDifficulty(
						safeDifficulty, true);
				Pig meleeTarget = EntityType.PIG.create(
						helper.getLevel());
				require(helper, meleeTarget != null,
						"Could not create safe melee Fudge Folk target");
				meleeTarget.setPos(folk.getX() + 7.0D,
						folk.getY(),
						folk.getZ()
								+ safeDifficulty.getId()
										* 2.0D);
				helper.getLevel().addFreshEntity(
						meleeTarget);
				meleeTarget.setSecondsOnFire(5);
				meleeTarget.fallDistance = 8.0F;
				folk.doHurtTarget(meleeTarget);
				require(helper,
						close(meleeTarget.getHealth(),
								meleeTarget
										.getMaxHealth())
								&& !meleeTarget.isOnFire()
								&& close(meleeTarget
										.fallDistance,
										0.0D)
								&& meleeTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& meleeTarget.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& meleeTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4,
						safeDifficulty
								+ " Fudge Folk sword splat caused damage or lacked rescue");

				Pig rangedTarget = EntityType.PIG.create(
						helper.getLevel());
				require(helper, rangedTarget != null,
						"Could not create safe ranged Fudge Folk target");
				rangedTarget.setPos(meleeTarget.getX(),
						meleeTarget.getY(),
						meleeTarget.getZ() + 1.0D);
				helper.getLevel().addFreshEntity(
						rangedTarget);
				Arrow arrow = new Arrow(
						helper.getLevel(), folk);
				rangedTarget.hurt(DamageSource.arrow(
						arrow, folk), 5.0F);
				require(helper,
						close(rangedTarget.getHealth(),
								rangedTarget.getMaxHealth())
								&& rangedTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& rangedTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE),
						safeDifficulty
								+ " Fudge Folk crossbow source caused damage or lacked rescue");
			}

			helper.getLevel().getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardMeleeTarget =
					EntityType.PIG.create(helper.getLevel());
			require(helper, hardMeleeTarget != null,
					"Could not create Hard melee Fudge Folk target");
			hardMeleeTarget.setPos(folk.getX() + 7.0D,
					folk.getY(), folk.getZ() + 6.0D);
			helper.getLevel().addFreshEntity(
					hardMeleeTarget);
			folk.doHurtTarget(hardMeleeTarget);
			require(helper,
					hardMeleeTarget.getHealth()
							< hardMeleeTarget.getMaxHealth(),
					"Hard Fudge Folk sword attack did not cause real damage");
			Pig hardRangedTarget =
					EntityType.PIG.create(helper.getLevel());
			require(helper, hardRangedTarget != null,
					"Could not create Hard ranged Fudge Folk target");
			hardRangedTarget.setPos(folk.getX() + 7.0D,
					folk.getY(), folk.getZ() + 7.0D);
			helper.getLevel().addFreshEntity(
					hardRangedTarget);
			Arrow hardArrow = new Arrow(
					helper.getLevel(), folk);
			hardRangedTarget.hurt(DamageSource.arrow(
					hardArrow, folk), 5.0F);
			require(helper,
					hardRangedTarget.getHealth()
							< hardRangedTarget.getMaxHealth(),
					"Hard Fudge Folk crossbow source did not cause real damage");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			FudgeFolkProbe peaceful =
					new FudgeFolkProbe(helper.getLevel());
			peaceful.setPos(folk.getX() + 9.0D,
					folk.getY(), folk.getZ());
			helper.getLevel().addFreshEntity(peaceful);
			peaceful.checkDespawn();
			require(helper,
					!peaceful.isRemoved()
							&& !peaceful
									.despawnsInPeaceful(),
					"Peaceful Fudge Folk did not retain vanilla Piglin persistence");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.NORMAL, true);
			FudgeFolkProbe converting =
					new FudgeFolkProbe(helper.getLevel());
			converting.setPos(folk.getX() + 11.0D,
					folk.getY(), folk.getZ());
			helper.getLevel().getEntitiesOfClass(
					ZombifiedPiglin.class,
					converting.getBoundingBox()
							.inflate(3.0D))
					.forEach(ZombifiedPiglin::discard);
			converting.setBaby(true);
			converting.setCustomName(new TextComponent(
					"Staged Stale Fudge Folk"));
			converting.setPersistenceRequired();
			converting.setItemSlot(EquipmentSlot.MAINHAND,
					new ItemStack(Items.CROSSBOW));
			converting.getInventory().setItem(0,
					new ItemStack(Items.GOLD_NUGGET, 3));
			CompoundTag convertingState =
					converting.saveWithoutId(
							new CompoundTag());
			convertingState.putInt(
					"TimeInOverworld", 300);
			converting.load(convertingState);
			helper.getLevel().addFreshEntity(converting);
			converting.runServerAiStep();
			conversion = helper.getLevel()
					.getEntitiesOfClass(
							ZombifiedPiglin.class,
							converting.getBoundingBox()
									.inflate(3.0D),
							candidate ->
									"Staged Stale Fudge Folk"
											.equals(candidate
													.getName()
													.getString()))
					.stream().findFirst().orElse(null);
			require(helper,
					converting.isRemoved()
							&& conversion != null
							&& conversion.isBaby()
							&& conversion
									.isPersistenceRequired()
							&& conversion.getMainHandItem()
									.is(Items.CROSSBOW)
							&& "Staged Stale Fudge Folk"
									.equals(conversion.getName()
											.getString())
							&& conversion.hasEffect(
									MobEffects.CONFUSION),
					"Fudge Folk lost the staged vanilla zombification state for MOB-073");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
			if (conversion != null) {
				conversion.discard();
			}
		}

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						helper.absolutePos(
								new BlockPos(8, 3, 8)),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Piglin conversion");
		Piglin literal =
				EntityType.PIGLIN.create(helper.getLevel());
		require(helper, literal != null,
				"Could not create literal structure Piglin fixture");
		literal.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literal.setBaby(true);
		literal.setNoAi(true);
		literal.setCustomName(new TextComponent(
				"Foundry Visitor"));
		literal.setPersistenceRequired();
		literal.getInventory().setItem(0,
				new ItemStack(Items.GOLD_NUGGET, 2));
		FudgeFolk structureReplacement =
				CakeWorldPiglinReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				literal.isRemoved()
						&& structureReplacement != null
						&& structureReplacement.isBaby()
						&& structureReplacement.isNoAi()
						&& structureReplacement
								.isPersistenceRequired()
						&& "Foundry Visitor".equals(
								structureReplacement
										.getName()
										.getString())
						&& structureReplacement
								.getInventory()
								.countItem(
										Items.GOLD_NUGGET)
								== 2,
				"Fresh literal Piglin conversion lost structure state");

		Biome fudgeWastes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY)
				.get(CakeWorldBiomes.FUDGE_WASTES.getId());
		require(helper, fudgeWastes != null,
				"Could not inspect Fudge Wastes spawning");
		MobSpawnSettings.SpawnerData folkSpawn =
				fudgeWastes.getMobSettings()
						.getMobs(MobCategory.MONSTER)
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== CakeWorldEntities
										.FUDGE_FOLK.get())
						.findFirst().orElse(null);
		require(helper,
				folkSpawn != null
						&& folkSpawn.getWeight().asInt() == 15
						&& folkSpawn.minCount == 4
						&& folkSpawn.maxCount == 4
						&& fudgeWastes.getMobSettings()
								.getMobs(
										MobCategory.MONSTER)
								.unwrap().stream()
								.noneMatch(spawn ->
										spawn.type
												== EntityType.PIGLIN)
						&& CakeWorldItems
								.FUDGE_FOLK_SPAWN_EGG
								.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.FUDGE_FOLK
												.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.FUDGE_FOLK
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.FUDGE_FOLK
												.get())
								== SoundEvents
										.PARROT_IMITATE_PIGLIN,
				"Fudge Folk lost exact Nether Wastes 15/4-4 replacement, egg, placement or mimic role");

		BlockPos spawnPos = helper.absolutePos(
				new BlockPos(14, 3, 2));
		helper.getLevel().setBlock(spawnPos.below(),
				CakeWorldBlocks.FUDGE_ROCK.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos,
				Blocks.AIR.defaultBlockState(), 3);
		helper.getLevel().setBlock(spawnPos.above(),
				Blocks.AIR.defaultBlockState(), 3);
		require(helper,
				FudgeFolk.checkFudgeFolkSpawnRules(
						CakeWorldEntities.FUDGE_FOLK.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						spawnPos, new Random(1978L))
						&& SpawnPlacements.Type.ON_GROUND
								.canSpawnAt(
										helper.getLevel(),
										spawnPos,
										CakeWorldEntities
												.FUDGE_FOLK
												.get()),
				"Fudge Folk rejected a valid edible Nether surface");
		helper.getLevel().setBlock(spawnPos.below(),
				Blocks.NETHER_WART_BLOCK
						.defaultBlockState(), 3);
		require(helper,
				!FudgeFolk.checkFudgeFolkSpawnRules(
						CakeWorldEntities.FUDGE_FOLK.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						spawnPos, new Random(1978L)),
				"Fudge Folk lost the exact Piglin Nether-wart spawn exclusion");

		VanillaRoleAdvancements.creditKilledPiglinRole(
				barterPlayer);
		requireCriterion(helper, barterPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:piglin");
		helper.succeed();
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void jellybeanFishKeepEveryVariantSchoolAndBucketRole(
			GameTestHelper helper) {
		JellybeanFishProbe fish =
				new JellybeanFishProbe(helper.getLevel());
		fish.seedRandom(1978L);
		int experience = fish.getExperienceValue();
		require(helper,
				fish instanceof TropicalFish
						&& fish.getType()
								== CakeWorldEntities
										.JELLYBEAN_FISH
										.get()
						&& fish.getType().getCategory()
								== MobCategory.WATER_AMBIENT
						&& close(fish.getMaxHealth(), 3.0D)
						&& close(fish.getDimensions(
								Pose.STANDING).width,
								0.5D)
						&& close(fish.getDimensions(
								Pose.STANDING).height,
								0.4D)
						&& fish.getType()
								.clientTrackingRange() == 4
						&& fish.getMaxSchoolSize() == 8
						&& fish.getMaxSpawnClusterSize() == 8
						&& experience >= 1
						&& experience <= 3
						&& fish.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/jellybean_fish")),
				"Jellybean Fish lost the exact Tropical Fish type, body, school, XP or loot roles");
		require(helper,
				fish.countGoalsNamed("PanicGoal") == 1
						&& fish.countGoalsNamed(
								"AvoidEntityGoal") == 1
						&& fish.countGoalsNamed(
								"FishSwimGoal") == 1
						&& fish.countGoalsNamed(
								"FollowFlockLeaderGoal")
								== 1
						&& fish.countTargetGoals() == 0
						&& "WaterBoundPathNavigation"
								.equals(fish.getNavigation()
										.getClass()
										.getSimpleName())
						&& fish.canBreatheUnderwater()
						&& fish.getMobType()
								== MobType.WATER
						&& !fish.isPushedByFluid()
						&& !fish.canBeLeashedRole()
						&& fish.getAmbientSoundInterval()
								== 120,
				"Jellybean Fish lost panic, avoidance, school/random swimming, passive targets or water roles");
		require(helper,
				fish.ambientSound()
								== SoundEvents
										.TROPICAL_FISH_AMBIENT
						&& fish.hurtSound()
								== SoundEvents
										.TROPICAL_FISH_HURT
						&& fish.deathSound()
								== SoundEvents
										.TROPICAL_FISH_DEATH
						&& fish.flopSound()
								== SoundEvents
										.TROPICAL_FISH_FLOP
						&& fish.swimSound()
								== SoundEvents.FISH_SWIM
						&& fish.getPickupSound()
								== SoundEvents
										.BUCKET_FILL_FISH,
				"Jellybean Fish lost exact Tropical Fish ambient, hurt, death, flop, swim or pickup sounds");

		Set<Integer> packedVariants =
				new java.util.HashSet<>();
		Set<String> patternNames =
				new java.util.HashSet<>();
		Set<DyeColor> generatedColours =
				new java.util.HashSet<>();
		for (int base = 0; base < 2; ++base) {
			for (int pattern = 0;
					pattern < 6; ++pattern) {
				for (int baseColour = 0;
						baseColour < 15;
						++baseColour) {
					for (int patternColour = 0;
							patternColour < 15;
							++patternColour) {
						int packed = base
								| pattern << 8
								| baseColour << 16
								| patternColour
										<< 24;
						fish.setVariant(packed);
						packedVariants.add(
								fish.getVariant());
						patternNames.add(
								TropicalFish
										.getFishTypeName(
												packed));
						generatedColours.add(
								TropicalFish
										.getBaseColor(
												packed));
						generatedColours.add(
								TropicalFish
										.getPatternColor(
												packed));
						require(helper,
								fish.getVariant()
										== packed
										&& fish.getBaseVariant()
												== base
										&& TropicalFish
												.getBaseColor(
														packed)
												.getId()
												== baseColour
										&& TropicalFish
												.getPatternColor(
														packed)
												.getId()
												== patternColour
										&& fish.getBaseTextureLocation()
												.getPath()
												.equals(
														"textures/entity/fish/tropical_"
																+ (base == 0
																		? "a"
																		: "b")
																+ ".png")
										&& fish.getPatternTextureLocation()
												.getPath()
												.equals(
														"textures/entity/fish/tropical_"
																+ (base == 0
																		? "a"
																		: "b")
																+ "_pattern_"
																+ (pattern
																		+ 1)
																+ ".png"),
								"Jellybean Fish changed packed variant "
										+ Integer.toUnsignedString(
												packed));
					}
				}
			}
		}
		require(helper,
				packedVariants.size() == 2700
						&& patternNames.size() == 12
						&& generatedColours.size() == 15
						&& !generatedColours
								.contains(DyeColor.BLACK)
						&& TropicalFish.COMMON_VARIANTS.length
								== 22
						&& Arrays.stream(
								TropicalFish
										.COMMON_VARIANTS)
								.boxed().distinct().count()
								== 22,
				"Jellybean Fish lost the exact 2,700 generated variants, twelve patterns, fifteen generated colours or twenty-two common forms");

		BlockPos localPos = helper.absolutePos(
				new BlockPos(3, 3, 3));
		DifficultyInstance localDifficulty =
				helper.getLevel().getCurrentDifficultyAt(
						localPos);
		JellybeanFishProbe commonLeader =
				new JellybeanFishProbe(
						helper.getLevel());
		commonLeader.seedRandom(12L);
		SpawnGroupData schoolData =
				commonLeader.finalizeSpawn(
						helper.getLevel(),
						localDifficulty,
						MobSpawnType.NATURAL,
						null, null);
		JellybeanFishProbe commonFollower =
				new JellybeanFishProbe(
						helper.getLevel());
		commonFollower.finalizeSpawn(
				helper.getLevel(), localDifficulty,
				MobSpawnType.NATURAL,
				schoolData, null);
		require(helper,
				"TropicalFishGroupData".equals(
						schoolData.getClass()
								.getSimpleName())
						&& commonLeader.getVariant()
								== TropicalFish
										.COMMON_VARIANTS[0]
						&& commonFollower.getVariant()
								== commonLeader.getVariant()
						&& commonFollower.isFollower()
						&& commonLeader.hasFollowers()
						&& !commonLeader
								.isMaxGroupSizeReached(2),
				"Common Jellybean Fish did not form a same-variant school");
		commonFollower.stopFollowing();
		require(helper,
				!commonFollower.isFollower()
						&& !commonLeader.hasFollowers(),
				"Jellybean Fish did not leave its school cleanly");

		JellybeanFishProbe rare =
				new JellybeanFishProbe(
						helper.getLevel());
		rare.seedRandom(5L);
		rare.finalizeSpawn(helper.getLevel(),
				localDifficulty, MobSpawnType.NATURAL,
				null, null);
		require(helper,
				rare.getVariant()
								== (0 | 5 << 8
										| 2 << 16
										| 6 << 24)
						&& rare.isMaxGroupSizeReached(1),
				"Rare solitary Jellybean Fish lost the exact free-variant branch");

		int savedVariant =
				1 | 4 << 8 | 7 << 16 | 12 << 24;
		fish.setVariant(savedVariant);
		fish.setFromBucket(true);
		CompoundTag entityState = new CompoundTag();
		fish.addAdditionalSaveData(entityState);
		JellybeanFishProbe restored =
				new JellybeanFishProbe(
						helper.getLevel());
		restored.readAdditionalSaveData(entityState);
		require(helper,
				entityState.getInt("Variant")
								== savedVariant
						&& entityState.getBoolean(
								"FromBucket")
						&& restored.getVariant()
								== savedVariant
						&& restored.fromBucket(),
				"Jellybean Fish lost Variant or FromBucket entity NBT");

		int seaLevel = helper.getLevel().getSeaLevel();
		BlockPos lemonadePos = new BlockPos(
				localPos.getX() + 8,
				seaLevel - 5, localPos.getZ());
		for (int y = -1; y <= 1; ++y) {
			helper.getLevel().setBlock(
					lemonadePos.offset(0, y, 0),
					CakeWorldFluids.LEMONADE_BLOCK
							.get().defaultBlockState(),
					3);
		}
		require(helper,
				!TropicalFish
						.checkTropicalFishSpawnRules(
								EntityType
										.TROPICAL_FISH,
								helper.getLevel(),
								MobSpawnType.NATURAL,
								lemonadePos,
								new Random(1978L))
						&& JellybeanFish
								.checkJellybeanFishSpawnRules(
										CakeWorldEntities
												.JELLYBEAN_FISH
												.get(),
										helper.getLevel(),
										MobSpawnType
												.NATURAL,
										lemonadePos,
										new Random(
												1978L))
						&& SpawnPlacements
								.checkSpawnRules(
										CakeWorldEntities
												.JELLYBEAN_FISH
												.get(),
										helper.getLevel(),
										MobSpawnType
												.NATURAL,
										lemonadePos,
										new Random(
												1979L))
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.JELLYBEAN_FISH
												.get())
								== SpawnPlacements.Type
										.IN_WATER
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.JELLYBEAN_FISH
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Jellybean Fish lost Lemonade-compatible surface spawning or exact placement metadata");

		JellybeanFish captureFish =
				CakeWorldEntities.JELLYBEAN_FISH.get()
						.create(helper.getLevel());
		require(helper, captureFish != null,
				"Could not create Jellybean Fish bucket fixture");
		captureFish.setVariant(
				TropicalFish.COMMON_VARIANTS[6]);
		captureFish.setCustomName(
				new TextComponent("Raspberry Drop"));
		captureFish.setPos(
				lemonadePos.getX() + 0.5D,
				lemonadePos.getY(),
				lemonadePos.getZ() + 0.5D);
		helper.getLevel().addFreshEntity(captureFish);
		ServerPlayer bucketPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2059"),
						"CakeWorldJellybeanBucketTest"));
		bucketPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Items.WATER_BUCKET));
		InteractionResult captureResult =
				bucketPlayer.interactOn(captureFish,
						InteractionHand.MAIN_HAND);
		ItemStack bucket = bucketPlayer.getItemInHand(
				InteractionHand.MAIN_HAND);
		require(helper,
				captureResult.consumesAction()
						&& bucket.is(CakeWorldItems
								.JELLYBEAN_FISH_BUCKET
								.get())
						&& bucket.hasCustomHoverName()
						&& bucket.getOrCreateTag().getInt(
								TropicalFish
										.BUCKET_VARIANT_TAG)
								== TropicalFish
										.COMMON_VARIANTS[6]
						&& captureFish.isRemoved(),
				"Jellybean Fish did not capture its name and packed common variant");
		List<net.minecraft.network.chat.Component>
				commonTooltip =
						new java.util.ArrayList<>();
		bucket.getItem().appendHoverText(
				bucket, null, commonTooltip,
				TooltipFlag.Default.NORMAL);
		require(helper,
				commonTooltip.size() == 1
						&& commonTooltip.get(0)
								instanceof TranslatableComponent
										commonName
						&& commonName.getKey().equals(
								TropicalFish
										.getPredefinedName(
												6)),
				"Jellybean Fish common bucket lost its predefined-name tooltip");

		ItemStack uncommonBucket = new ItemStack(
				CakeWorldItems.JELLYBEAN_FISH_BUCKET
						.get());
		uncommonBucket.getOrCreateTag().putInt(
				TropicalFish.BUCKET_VARIANT_TAG,
				savedVariant);
		List<net.minecraft.network.chat.Component>
				uncommonTooltip =
						new java.util.ArrayList<>();
		uncommonBucket.getItem().appendHoverText(
				uncommonBucket, null, uncommonTooltip,
				TooltipFlag.Default.NORMAL);
		require(helper,
				Arrays.stream(
						TropicalFish.COMMON_VARIANTS)
						.noneMatch(value ->
								value == savedVariant)
						&& uncommonTooltip.size() == 2
						&& uncommonTooltip.get(0)
								instanceof TranslatableComponent
										typeName
						&& typeName.getKey().equals(
								TropicalFish
										.getFishTypeName(
												savedVariant))
						&& uncommonTooltip.get(1)
								instanceof TranslatableComponent
										baseColourName
						&& baseColourName.getKey().equals(
								"color.minecraft."
										+ TropicalFish
												.getBaseColor(
														savedVariant)),
				"Jellybean Fish uncommon bucket lost type and colour tooltip semantics");

		AABB releaseArea =
				new AABB(lemonadePos).inflate(2.0D);
		((MobBucketItem)bucket.getItem())
				.checkExtraContent(null,
						helper.getLevel(), bucket,
						lemonadePos);
		List<JellybeanFish> released =
				helper.getLevel().getEntitiesOfClass(
						JellybeanFish.class,
						releaseArea);
		require(helper,
				released.size() == 1
						&& released.get(0).fromBucket()
						&& released.get(0).getVariant()
								== TropicalFish
										.COMMON_VARIANTS[6]
						&& "Raspberry Drop".equals(
								released.get(0).getName()
										.getString()),
				"Jellybean Fish bucket released the wrong entity or lost variant/from-bucket/name data");
		requireCriterion(helper, bucketPlayer,
				"minecraft:husbandry/tactical_fishing",
				"tropical_fish_bucket");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						localPos.offset(20, 0, 0),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Tropical Fish conversion");
		TropicalFish literal = EntityType.TROPICAL_FISH
				.create(helper.getLevel());
		Boat vehicle = EntityType.BOAT.create(
				helper.getLevel());
		Pig passenger = EntityType.PIG.create(
				helper.getLevel());
		require(helper,
				literal != null && vehicle != null
						&& passenger != null,
				"Could not create literal Tropical Fish state fixtures");
		literal.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literal.setVariant(savedVariant);
		literal.setFromBucket(true);
		literal.setHealth(2.0F);
		literal.setAirSupply(177);
		literal.setCustomName(
				new TextComponent("Buried Sweet"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setInvulnerable(true);
		literal.invulnerableTime = 19;
		vehicle.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		passenger.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		helper.getLevel().addFreshEntity(vehicle);
		helper.getLevel().addFreshEntity(passenger);
		helper.getLevel().addFreshEntity(literal);
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		JellybeanFish converted =
				CakeWorldTropicalFishReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& converted.getVariant()
								== savedVariant
						&& converted.fromBucket()
						&& close(converted.getHealth(),
								2.0D)
						&& converted.getAirSupply()
								== 177
						&& "Buried Sweet".equals(
								converted.getName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.isInvulnerable()
						&& converted.invulnerableTime
								== 19
						&& converted.getVehicle()
								== vehicle
						&& passenger.getVehicle()
								== converted,
				"Fresh literal Tropical Fish conversion lost variant, bucket, health, air, name, state, vehicle or passenger");
		JellybeanFish exactTypeGuard =
				CakeWorldEntities.JELLYBEAN_FISH.get()
						.create(helper.getLevel());
		require(helper,
				exactTypeGuard != null
						&& CakeWorldTropicalFishReplacement
								.replaceIfInCakeWorldBiome(
										helper.getLevel(),
										exactTypeGuard)
								== null
						&& !exactTypeGuard.isRemoved(),
				"Tropical Fish source conversion touched a non-literal entity type");
		exactTypeGuard.discard();
		passenger.discard();
		converted.discard();
		vehicle.discard();

		TropicalFish eventLiteral =
				EntityType.TROPICAL_FISH.create(
						helper.getLevel());
		require(helper, eventLiteral != null,
				"Could not create deferred Tropical Fish source fixture");
		eventLiteral.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		eventLiteral.setVariant(savedVariant);
		eventLiteral.setFromBucket(true);
		eventLiteral.setNoAi(true);
		eventLiteral.setCustomName(
				new TextComponent(
						"Deferred Jellybean"));
		helper.getLevel().addFreshEntity(
				eventLiteral);

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		Biome sodaOcean = biomes.get(
				CakeWorldBiomes.SODA_OCEAN.getId());
		require(helper, sodaOcean != null,
				"Could not inspect Jellybean Fish Soda Ocean ecology");
		List<MobSpawnSettings.SpawnerData>
				sodaProfiles = sodaOcean.getMobSettings()
						.getMobs(
								MobCategory.WATER_AMBIENT)
						.unwrap().stream()
						.filter(spawn ->
								spawn.type
										== EntityType
												.TROPICAL_FISH
								|| spawn.type
										== CakeWorldEntities
												.JELLYBEAN_FISH
												.get())
						.toList();
		require(helper,
				sodaProfiles.size() == 1
						&& sodaProfiles.get(0).type
								== CakeWorldEntities
										.JELLYBEAN_FISH
										.get()
						&& sodaProfiles.get(0)
								.getWeight().asInt() == 25
						&& sodaProfiles.get(0).minCount
								== 8
						&& sodaProfiles.get(0).maxCount
								== 8,
				"Soda Ocean lost the exact warm-ocean 25/8-8 Jellybean Fish school: "
						+ sodaProfiles);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.WATER_AMBIENT)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.TROPICAL_FISH
											|| spawn.type
													== CakeWorldEntities
															.JELLYBEAN_FISH
															.get()),
					"Non-ocean current biome leaked Tropical/Jellybean Fish spawning: "
							+ biomeId);
		}

		TagKey<EntityType<?>> axolotlPrey =
				TagKey.create(
						Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation("minecraft",
								"axolotl_hunt_targets"));
		TagKey<Item> axolotlFood = TagKey.create(
				Registry.ITEM_REGISTRY,
				new ResourceLocation("minecraft",
						"axolotl_tempt_items"));
		Advancement killAll = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"adventure/kill_all_mobs"));
		Advancement bredAll = helper.getLevel()
				.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(
						"minecraft",
						"husbandry/bred_all_animals"));
		require(helper,
				CakeWorldEntities.JELLYBEAN_FISH
								.get().is(axolotlPrey)
						&& CakeWorldItems
								.JELLYBEAN_FISH_BUCKET
								.isPresent()
						&& new ItemStack(CakeWorldItems
								.JELLYBEAN_FISH_BUCKET
								.get()).is(axolotlFood)
						&& CakeWorldItems
								.JELLYBEAN_FISH_SPAWN_EGG
								.isPresent()
						&& killAll != null
						&& bredAll != null
						&& !killAll.getCriteria()
								.containsKey(
										"minecraft:tropical_fish")
						&& !bredAll.getCriteria()
								.containsKey(
										"minecraft:tropical_fish")
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.JELLYBEAN_FISH
												.get())
								== null,
				"Jellybean Fish lost Axolotl prey/food or egg roles, or invented combat, breeding or mimic progress");

		Jellylotl jellylotl =
				CakeWorldEntities.JELLYLOTL.get()
						.create(helper.getLevel());
		require(helper, jellylotl != null,
				"Could not create Jellylotl food-role fixture");
		jellylotl.setPos(localPos.getX() + 12.0D,
				localPos.getY(), localPos.getZ());
		helper.getLevel().addFreshEntity(jellylotl);
		ServerPlayer jellylotlPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2060"),
						"CakeWorldJellylotlFoodTest"));
		jellylotlPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems
						.JELLYBEAN_FISH_BUCKET.get()));
		require(helper,
				jellylotl.isFood(jellylotlPlayer
						.getMainHandItem())
						&& jellylotlPlayer.interactOn(
								jellylotl,
								InteractionHand.MAIN_HAND)
								.consumesAction()
						&& jellylotl.isInLove()
						&& jellylotlPlayer
								.getMainHandItem()
								.is(CakeWorldFluids
										.LEMONADE_BUCKET
										.get()),
				"Jellylotl did not consume the Jellybean role and retain its Lemonade");

		Axolotl vanillaAxolotl =
				EntityType.AXOLOTL.create(
						helper.getLevel());
		require(helper, vanillaAxolotl != null,
				"Could not create vanilla Axolotl compatibility fixture");
		vanillaAxolotl.setPos(
				localPos.getX() + 14.0D,
				localPos.getY(), localPos.getZ());
		helper.getLevel().addFreshEntity(
				vanillaAxolotl);
		ServerPlayer vanillaPlayer = new ServerPlayer(
				helper.getLevel().getServer(),
				helper.getLevel(),
				new GameProfile(UUID.fromString(
						"1978feed-feed-4bad-babe-1978feed2061"),
						"CakeWorldVanillaAxolotlFoodTest"));
		vanillaPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(CakeWorldItems
						.JELLYBEAN_FISH_BUCKET.get()));
		InteractionResult vanillaFeeding =
				vanillaPlayer.interactOn(
						vanillaAxolotl,
						InteractionHand.MAIN_HAND);
		require(helper,
				vanillaFeeding.consumesAction()
						&& vanillaAxolotl.isInLove(),
				"Vanilla Axolotl rejected the tagged Jellybean Fish bucket");

		released.forEach(JellybeanFish::discard);
		for (int y = -1; y <= 1; ++y) {
			helper.getLevel().setBlock(
					lemonadePos.offset(0, y, 0),
					Blocks.AIR.defaultBlockState(), 3);
		}
		helper.runAfterDelay(3, () -> {
			require(helper,
					vanillaPlayer.getMainHandItem()
							.is(CakeWorldFluids
									.LEMONADE_BUCKET
									.get()),
					"Vanilla Axolotl consumed the custom filled bucket without the Lemonade remainder");
			List<JellybeanFish> deferred =
					helper.getLevel()
							.getEntitiesOfClass(
									JellybeanFish.class,
									new AABB(
											cakeWorldPos)
													.inflate(
															2.0D),
									candidate ->
											"Deferred Jellybean"
													.equals(
															candidate
																	.getName()
																	.getString()));
			require(helper,
					eventLiteral.isRemoved()
							&& deferred.size() == 1
							&& deferred.get(0)
									.getVariant()
									== savedVariant
							&& deferred.get(0)
									.fromBucket()
							&& deferred.get(0)
									.isNoAi(),
					"Fresh literal EntityJoin source did not defer-convert with variant, bucket and AI state");
			deferred.forEach(
					JellybeanFish::discard);
			jellylotl.discard();
			vanillaAxolotl.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 700)
	public static void waferTurtlesKeepNestingHatchingAndShellProgression(
			GameTestHelper helper) {
		WaferTurtleProbe turtle =
				new WaferTurtleProbe(helper.getLevel());
		turtle.seedRandom(1978L);
		int experience = turtle.getExperienceValue();
		require(helper,
				turtle instanceof Turtle
						&& turtle.getType()
								== CakeWorldEntities
										.WAFER_TURTLE.get()
						&& turtle.getType().getCategory()
								== MobCategory.CREATURE
						&& close(turtle.getMaxHealth(), 30.0D)
						&& close(turtle.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.25D)
						&& close(turtle.getDimensions(
								Pose.STANDING).width,
								1.2D)
						&& close(turtle.getDimensions(
								Pose.STANDING).height,
								0.4D)
						&& turtle.getType()
								.clientTrackingRange() == 10
						&& turtle.getMaxSpawnClusterSize() == 4
						&& close(turtle.maxUpStep, 1.0D)
						&& close(turtle.getPathfindingMalus(
								net.minecraft.world.level
										.pathfinder
										.BlockPathTypes.WATER),
								0.0D)
						&& turtle.canBreatheUnderwater()
						&& !turtle.isPushedByFluid()
						&& turtle.getMobType() == MobType.WATER
						&& turtle.getAmbientSoundInterval() == 200
						&& !turtle.canBeLeashedRole()
						&& experience >= 1
						&& experience <= 3
						&& turtle.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/wafer_turtle")),
				"Wafer Turtle lost the exact Turtle body, movement, pathing, water, tracking, cluster, XP, no-leash or loot roles");
		require(helper,
				turtle.hasGoalAt("TurtlePanicGoal", 0)
						&& turtle.hasGoalAt(
								"TurtleBreedGoal", 1)
						&& turtle.hasGoalAt(
								"TurtleLayEggGoal", 1)
						&& turtle.hasGoalAt(
								"TemptGoal", 2)
						&& turtle.hasGoalAt(
								"TurtleGoToWaterGoal", 3)
						&& turtle.hasGoalAt(
								"TurtleGoHomeGoal", 4)
						&& turtle.hasGoalAt(
								"TurtleTravelGoal", 7)
						&& turtle.hasGoalAt(
								"LookAtPlayerGoal", 8)
						&& turtle.hasGoalAt(
								"TurtleRandomStrollGoal", 9)
						&& turtle.countGoalsNamed(
								"TurtlePanicGoal") == 1
						&& turtle.countGoalsNamed(
								"TurtleBreedGoal") == 1
						&& turtle.countGoalsNamed(
								"TurtleLayEggGoal") == 1
						&& turtle.countGoalsNamed(
								"TurtleGoToWaterGoal") == 1
						&& turtle.countGoalsNamed(
								"TurtleGoHomeGoal") == 1
						&& turtle.countGoalsNamed(
								"TurtleTravelGoal") == 1
						&& turtle.countGoalsNamed(
								"TurtleRandomStrollGoal") == 1
						&& turtle.countTargetGoals() == 0
						&& "TurtlePathNavigation".equals(
								turtle.getNavigation()
										.getClass()
										.getSimpleName())
						&& "TurtleMoveControl".equals(
								turtle.getMoveControl()
										.getClass()
										.getSimpleName()),
				"Wafer Turtle lost panic, breeding, nesting, water, home, travel, look, stroll, navigation or passive-goal priorities");
		require(helper,
				turtle.isFood(new ItemStack(
								Blocks.SEAGRASS))
						&& !turtle.isFood(new ItemStack(
								Items.WHEAT))
						&& turtle.adultLandAmbient()
								== SoundEvents
										.TURTLE_AMBIENT_LAND
						&& turtle.adultHurtSound()
								== SoundEvents.TURTLE_HURT
						&& turtle.babyHurtSound()
								== SoundEvents
										.TURTLE_HURT_BABY
						&& turtle.adultDeathSound()
								== SoundEvents.TURTLE_DEATH
						&& turtle.babyDeathSound()
								== SoundEvents
										.TURTLE_DEATH_BABY
						&& turtle.swimSound()
								== SoundEvents.TURTLE_SWIM
						&& turtle.adultStepSound()
								== SoundEvents.TURTLE_SHAMBLE
						&& turtle.babyStepSound()
								== SoundEvents
										.TURTLE_SHAMBLE_BABY,
				"Wafer Turtle lost Seagrass food or exact adult/baby land, swim, hurt, death and shamble sounds");
		turtle.setAge(0);
		require(helper, close(turtle.getScale(), 1.0D),
				"Adult Wafer Turtle lost its full render scale");
		turtle.setAge(-24000);
		require(helper, close(turtle.getScale(), 0.3D)
						&& Turtle.BABY_ON_LAND_SELECTOR
								.test(turtle),
				"Baby Wafer Turtle lost its scale or inherited land-predator role");
		turtle.setAge(0);

		BlockPos stateHome = helper.absolutePos(
				new BlockPos(2, 3, 2));
		BlockPos stateTravel = stateHome.offset(
				137, -3, -211);
		turtle.setHomePos(stateHome);
		CompoundTag turtleState = new CompoundTag();
		turtle.addAdditionalSaveData(turtleState);
		turtleState.putBoolean("HasEgg", true);
		turtleState.putInt("TravelPosX",
				stateTravel.getX());
		turtleState.putInt("TravelPosY",
				stateTravel.getY());
		turtleState.putInt("TravelPosZ",
				stateTravel.getZ());
		WaferTurtleProbe restored =
				new WaferTurtleProbe(helper.getLevel());
		restored.readAdditionalSaveData(turtleState);
		CompoundTag restoredState = new CompoundTag();
		restored.addAdditionalSaveData(restoredState);
		require(helper,
				restored.hasEgg()
						&& restoredState.getInt("HomePosX")
								== stateHome.getX()
						&& restoredState.getInt("HomePosY")
								== stateHome.getY()
						&& restoredState.getInt("HomePosZ")
								== stateHome.getZ()
						&& restoredState.getInt("TravelPosX")
								== stateTravel.getX()
						&& restoredState.getInt("TravelPosY")
								== stateTravel.getY()
						&& restoredState.getInt("TravelPosZ")
								== stateTravel.getZ(),
				"Wafer Turtle lost home, travel or carried-egg state across NBT");

		WaferTurtle offspring =
				(WaferTurtle)turtle.getBreedOffspring(
						helper.getLevel(), restored);
		require(helper,
				offspring != null
						&& offspring.getType()
								== CakeWorldEntities
										.WAFER_TURTLE.get(),
				"Wafer Turtle same-family factory leaked a literal Turtle");

		ServerPlayer breedingPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(UUID.fromString(
								"1978feed-feed-4bad-babe-1978feed2062"),
								"CakeWorldWaferTurtleBreedTest"));
		WaferTurtle tempted =
				CakeWorldEntities.WAFER_TURTLE.get()
						.create(helper.getLevel());
		require(helper, tempted != null,
				"Could not create Wafer Turtle interaction fixture");
		breedingPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				new ItemStack(Blocks.SEAGRASS, 2));
		InteractionResult temptedResult =
				breedingPlayer.interactOn(
						tempted,
						InteractionHand.MAIN_HAND);
		require(helper,
				temptedResult.consumesAction()
						&& tempted.isInLove()
						&& breedingPlayer
								.getMainHandItem()
								.getCount() == 1,
				"Wafer Turtle did not consume Seagrass and enter its inherited breeding state");
		CompoundTag eggBearerState =
				new CompoundTag();
		tempted.addAdditionalSaveData(
				eggBearerState);
		eggBearerState.putBoolean("HasEgg", true);
		tempted.readAdditionalSaveData(
				eggBearerState);
		tempted.resetLove();
		requireCriterion(helper, breedingPlayer,
				"minecraft:husbandry/bred_all_animals",
				"minecraft:turtle");

		BlockPos spawnPos = new BlockPos(
				helper.absolutePos(new BlockPos(3, 3, 3))
						.getX(),
				helper.getLevel().getSeaLevel() + 1,
				helper.absolutePos(new BlockPos(3, 3, 3))
						.getZ());
		helper.getLevel().setBlock(
				spawnPos.below(),
				CakeWorldBlocks.BISCUIT_SAND.get()
						.defaultBlockState(), 3);
		for (int y = 0; y <= 3; y++) {
			helper.getLevel().setBlock(
					spawnPos.above(y),
					Blocks.AIR.defaultBlockState(), 3);
		}
		helper.getLevel().setBlock(
				spawnPos.offset(1, 0, 0),
				Blocks.SEA_LANTERN
						.defaultBlockState(), 3);
		require(helper,
				helper.getLevel().getBlockState(
						spawnPos.below())
						.is(BlockTags.SAND)
						&& CakeWorldBlocks.BISCUIT_CRUMBS
								.get()
								.defaultBlockState()
								.is(BlockTags.SAND),
				"Biscuit Sand or Biscuit Crumbs lost the public nesting-surface tag");
		boolean directSpawn =
				WaferTurtle.checkWaferTurtleSpawnRules(
						CakeWorldEntities.WAFER_TURTLE
								.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						spawnPos,
						new Random(1978L));
		boolean registeredSpawn =
				SpawnPlacements.checkSpawnRules(
						CakeWorldEntities.WAFER_TURTLE
								.get(),
						helper.getLevel(),
						MobSpawnType.NATURAL,
						spawnPos,
						new Random(1979L));
		require(helper,
				directSpawn
						&& registeredSpawn
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.WAFER_TURTLE
												.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.WAFER_TURTLE
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES
						&& CakeWorldItems
								.WAFER_TURTLE_SPAWN_EGG
								.isPresent(),
				"Wafer Turtle lost edible Beach-height bright spawning, registered placement or egg: direct="
						+ directSpawn
						+ ", registered="
						+ registeredSpawn
						+ ", rawBrightness="
						+ helper.getLevel()
								.getRawBrightness(
										spawnPos, 0));
		helper.getLevel().setBlock(
				spawnPos.below(),
				CakeWorldBlocks.CHOCOLATE_SPONGE
						.get().defaultBlockState(), 3);
		require(helper,
				!WaferTurtle
						.checkWaferTurtleSpawnRules(
								CakeWorldEntities
										.WAFER_TURTLE
										.get(),
								helper.getLevel(),
								MobSpawnType.NATURAL,
								spawnPos,
								new Random(1980L))
						&& !WaferTurtle
								.checkWaferTurtleSpawnRules(
										CakeWorldEntities
												.WAFER_TURTLE
												.get(),
										helper.getLevel(),
										MobSpawnType.NATURAL,
										new BlockPos(
												spawnPos
														.getX(),
												helper.getLevel()
														.getSeaLevel()
														+ 4,
												spawnPos
														.getZ()),
										new Random(
												1981L)),
				"Wafer Turtle spawn rule accepted a non-sand surface or a position outside vanilla's sea-level-plus-four ceiling");

		WaferTurtle growing =
				CakeWorldEntities.WAFER_TURTLE.get()
						.create(helper.getLevel());
		require(helper, growing != null,
				"Could not create Wafer Turtle growth fixture");
		BlockPos growthPos = helper.absolutePos(
				new BlockPos(7, 3, 3));
		growing.setPos(growthPos.getX() + 0.5D,
				growthPos.getY(),
				growthPos.getZ() + 0.5D);
		growing.setAge(-20);
		helper.getLevel().addFreshEntity(growing);
		growing.ageUp(1, true);
		List<ItemEntity> scutes = helper.getLevel()
				.getEntitiesOfClass(ItemEntity.class,
						new AABB(growthPos).inflate(2.0D),
						drop -> drop.getItem()
								.is(Items.SCUTE));
		require(helper,
				!growing.isBaby()
						&& scutes.size() == 1
						&& helper.getLevel()
								.getRecipeManager()
								.byKey(new ResourceLocation(
										"minecraft",
										"turtle_helmet"))
								.isPresent(),
				"Wafer Turtle lost adult-growth Scute or vanilla Turtle-Shell recipe progression");

		BlockPos protectedEggPos =
				helper.absolutePos(
						new BlockPos(10, 3, 3));
		helper.getLevel().setBlock(
				protectedEggPos.below(),
				CakeWorldBlocks.BISCUIT_CRUMBS
						.get().defaultBlockState(), 3);
		helper.getLevel().setBlock(
				protectedEggPos,
				Blocks.TURTLE_EGG
						.defaultBlockState()
						.setValue(
								TurtleEggBlock.EGGS, 4),
				3);
		for (int i = 0; i < 1000; i++) {
			Blocks.TURTLE_EGG.stepOn(
					helper.getLevel(),
					protectedEggPos,
					helper.getLevel()
							.getBlockState(
									protectedEggPos),
					turtle);
		}
		require(helper,
				helper.getLevel()
						.getBlockState(
								protectedEggPos)
						.is(Blocks.TURTLE_EGG)
						&& helper.getLevel()
								.getBlockState(
										protectedEggPos)
								.getValue(
										TurtleEggBlock
												.EGGS)
								== 4,
				"Wafer Turtle failed the inherited Turtle/Turtle-Egg trampling exemption");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(
						helper,
						new BlockPos(0,
								helper.getLevel()
										.getSeaLevel(),
								0),
						64);
		require(helper, cakeWorldPos != null,
				"Could not locate a CakeWorld biome for Wafer Turtle source and hatch conversion");
		BlockPos sourcePos =
				cakeWorldPos.offset(0, 2, 0);
		Turtle literal =
				EntityType.TURTLE.create(
						helper.getLevel());
		Entity passenger =
				EntityType.ARMOR_STAND.create(
						helper.getLevel());
		require(helper,
				literal != null && passenger != null,
				"Could not create Wafer Turtle source fixtures");
		literal.setPos(sourcePos.getX() + 0.5D,
				sourcePos.getY(),
				sourcePos.getZ() + 0.5D);
		literal.setAge(-1234);
		literal.setHealth(17.0F);
		literal.setNoAi(true);
		literal.setCustomName(
				new TextComponent("Homeward Wafer"));
		literal.setHomePos(
				sourcePos.offset(-4, -1, 7));
		literal.invulnerableTime = 9;
		CompoundTag literalState =
				new CompoundTag();
		literal.addAdditionalSaveData(
				literalState);
		literalState.putBoolean("HasEgg", true);
		literalState.putInt("TravelPosX",
				sourcePos.getX() + 71);
		literalState.putInt("TravelPosY",
				sourcePos.getY() - 3);
		literalState.putInt("TravelPosZ",
				sourcePos.getZ() - 43);
		literal.readAdditionalSaveData(
				literalState);
		helper.getLevel().addFreshEntity(literal);
		passenger.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		helper.getLevel().addFreshEntity(
				passenger);
		passenger.startRiding(literal, true);
		WaferTurtle converted =
				CakeWorldTurtleReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		CompoundTag convertedState =
				new CompoundTag();
		require(helper, converted != null,
				"Fresh literal Turtle did not convert in a CakeWorld biome");
		converted.addAdditionalSaveData(
				convertedState);
		require(helper,
				literal.isRemoved()
						&& converted.getType()
								== CakeWorldEntities
										.WAFER_TURTLE.get()
						&& converted.getAge() == -1234
						&& close(converted.getHealth(), 17.0D)
						&& converted.isNoAi()
						&& converted.hasEgg()
						&& converted.invulnerableTime == 9
						&& "Homeward Wafer".equals(
								converted.getName()
										.getString())
						&& convertedState.getInt(
								"HomePosX")
								== sourcePos.getX() - 4
						&& convertedState.getInt(
								"TravelPosX")
								== sourcePos.getX() + 71
						&& passenger.getVehicle()
								== converted
						&& converted.getPassengers()
								.contains(passenger),
				"Fresh literal Turtle conversion lost age, health, AI, egg, home, travel, name, invulnerability or passenger state");
		WaferTurtle exactTypeGuard =
				CakeWorldEntities.WAFER_TURTLE.get()
						.create(helper.getLevel());
		require(helper,
				exactTypeGuard != null
						&& CakeWorldTurtleReplacement
								.replaceIfInCakeWorldBiome(
										helper.getLevel(),
										exactTypeGuard)
								== null
						&& !exactTypeGuard.isRemoved(),
				"Turtle source conversion touched a non-literal entity type");
		exactTypeGuard.discard();

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS
						.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS
						.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.TURTLE
											|| spawn.type
													== CakeWorldEntities
															.WAFER_TURTLE
															.get()),
					"Current pre-Custard-Coast biome leaked Turtle/Wafer Turtle ecology: "
							+ biomeId);
		}

		WaferTurtle lightningTurtle =
				CakeWorldEntities.WAFER_TURTLE.get()
						.create(helper.getLevel());
		LightningBolt lightning =
				EntityType.LIGHTNING_BOLT.create(
						helper.getLevel());
		require(helper,
				lightningTurtle != null
						&& lightning != null,
				"Could not create Wafer Turtle lightning fixture");
		lightningTurtle.thunderHit(
				helper.getLevel(), lightning);
		require(helper,
				!lightningTurtle.isAlive(),
				"Wafer Turtle lost the inherited lethal lightning/Bowl-loot trigger");

		BlockPos hatchPos =
				sourcePos.offset(8, 0, 0);
		helper.getLevel()
				.getEntitiesOfClass(
						Turtle.class,
						new AABB(hatchPos)
								.inflate(96.0D),
						candidate ->
								hasTurtleHome(
										candidate,
										hatchPos))
				.forEach(Turtle::discard);
		helper.getLevel().setBlock(
				hatchPos.below(),
				CakeWorldBlocks.BISCUIT_SAND.get()
						.defaultBlockState(), 3);
		helper.getLevel().setBlock(
				hatchPos,
				Blocks.TURTLE_EGG
						.defaultBlockState()
						.setValue(
								TurtleEggBlock.HATCH, 2)
						.setValue(
								TurtleEggBlock.EGGS, 3),
				3);
		for (int attempt = 0;
				attempt < 10000
						&& helper.getLevel()
								.getBlockState(hatchPos)
								.is(Blocks.TURTLE_EGG);
				attempt++) {
			Blocks.TURTLE_EGG.randomTick(
					helper.getLevel()
							.getBlockState(hatchPos),
					helper.getLevel(), hatchPos,
					new Random(1982L + attempt));
		}
		require(helper,
				helper.getLevel().isEmptyBlock(
						hatchPos),
				"Three fully cracked Turtle Eggs did not pass the vanilla random/moonlit hatch gate on tagged Biscuit Sand");
		helper.runAfterDelay(5, () ->
				helper.getLevel()
						.getEntitiesOfClass(
								WaferTurtle.class,
								new AABB(hatchPos)
										.inflate(16.0D),
								candidate ->
										candidate
												.isBaby()
										&& hasTurtleHome(
												candidate,
												hatchPos))
						.forEach(candidate ->
								candidate
										.setNoAi(true)));

		BlockPos laySurface =
				helper.absolutePos(
						new BlockPos(14, 3, 3));
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				BlockPos surface =
						laySurface.offset(x, 0, z);
				helper.getLevel().setBlock(
						surface,
						CakeWorldBlocks.BISCUIT_SAND
								.get()
								.defaultBlockState(),
						3);
				helper.getLevel().setBlock(
						surface.above(),
						Blocks.AIR
								.defaultBlockState(),
						3);
			}
		}
		WaferTurtle eggBearer =
				CakeWorldEntities.WAFER_TURTLE.get()
						.create(helper.getLevel());
		require(helper, eggBearer != null,
				"Could not create Wafer Turtle egg-laying fixture");
		eggBearer.setPos(
				laySurface.getX() + 0.5D,
				laySurface.getY() + 1.0D,
				laySurface.getZ() + 0.5D);
		eggBearer.setHomePos(
				laySurface.above());
		CompoundTag layingState =
				new CompoundTag();
		eggBearer.addAdditionalSaveData(
				layingState);
		layingState.putBoolean("HasEgg", true);
		eggBearer.readAdditionalSaveData(
				layingState);
		helper.getLevel().addFreshEntity(
				eggBearer);

		helper.runAfterDelay(520, () -> {
			List<WaferTurtle> hatchlings =
					helper.getLevel()
							.getEntitiesOfClass(
									WaferTurtle.class,
									new AABB(hatchPos)
											.inflate(64.0D),
									candidate ->
											candidate
													.isBaby()
											&& hasTurtleHome(
													candidate,
													hatchPos));
			List<Turtle> literalHatchlings =
					helper.getLevel()
							.getEntitiesOfClass(
									Turtle.class,
									new AABB(hatchPos)
											.inflate(64.0D),
									candidate ->
											candidate
													.getType()
													== EntityType
															.TURTLE
											&& hasTurtleHome(
													candidate,
													hatchPos));
			require(helper,
					hatchlings.size() == 3
							&& literalHatchlings.isEmpty(),
					"Moonlit three-egg hatch did not defer-convert to exactly three Wafer Turtle babies: custom="
							+ hatchlings.size()
							+ " "
							+ hatchlings.stream()
									.map(Entity::blockPosition)
									.toList()
							+ ", literal="
							+ literalHatchlings.size()
							+ " "
							+ literalHatchlings.stream()
									.map(Entity::blockPosition)
									.toList());
			for (WaferTurtle hatchling :
					hatchlings) {
				CompoundTag hatchState =
						new CompoundTag();
				hatchling.addAdditionalSaveData(
						hatchState);
				require(helper,
						hatchState.getInt(
								"HomePosX")
									== hatchPos
											.getX()
								&& hatchState.getInt(
										"HomePosY")
										== hatchPos
												.getY()
								&& hatchState.getInt(
										"HomePosZ")
										== hatchPos
												.getZ(),
						"Wafer Turtle hatchling lost its egg-site home position");
			}
			BlockState laidEgg = null;
			BlockPos laidEggPos = null;
			for (int x = -16;
					x <= 16 && laidEgg == null; x++) {
				for (int z = -16;
						z <= 16; z++) {
					BlockPos candidate =
							laySurface.offset(
									x, 1, z);
					BlockState candidateState =
							helper.getLevel()
									.getBlockState(
											candidate);
					if (candidateState.is(
							Blocks.TURTLE_EGG)) {
						laidEgg = candidateState;
						laidEggPos = candidate;
						break;
					}
				}
			}
			require(helper,
					laidEgg != null
							&& laidEgg.getValue(
									TurtleEggBlock
											.EGGS) >= 1
							&& laidEgg.getValue(
									TurtleEggBlock
											.EGGS) <= 4
							&& !eggBearer.hasEgg(),
					"Wafer Turtle did not complete the inherited homeward one-to-four-egg laying cycle: egg="
							+ laidEggPos
							+ ", hasEgg="
							+ eggBearer.hasEgg());
			hatchlings.forEach(
					WaferTurtle::discard);
			scutes.forEach(ItemEntity::discard);
			growing.discard();
			turtle.discard();
			restored.discard();
			offspring.discard();
			tempted.discard();
			passenger.discard();
			converted.discard();
			eggBearer.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 300)
	public static void sourSpritesKeepSummoningFlightLifeAndSafeCharges(
			GameTestHelper helper) {
		SourSpriteProbe sprite =
				new SourSpriteProbe(helper.getLevel());
		int experience = sprite.getExperienceValue();
		require(helper,
				sprite instanceof Vex
						&& sprite.getType()
								== CakeWorldEntities
										.SOUR_SPRITE.get()
						&& sprite.getType().getCategory()
								== MobCategory.MONSTER
						&& sprite.getType().fireImmune()
						&& close(sprite.getMaxHealth(), 14.0D)
						&& close(sprite.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								4.0D)
						&& close(sprite.getDimensions(
								Pose.STANDING).width,
								0.4D)
						&& close(sprite.getDimensions(
								Pose.STANDING).height,
								0.8D)
						&& sprite.getType()
								.clientTrackingRange() == 8
						&& experience == 3,
				"Sour Sprite lost the exact Vex body, fire immunity, tracking, attributes or XP");
		require(helper,
				"VexMoveControl".equals(
						sprite.getMoveControl()
								.getClass()
								.getSimpleName())
						&& sprite.hasGoalAt(
								"FloatGoal", 0)
						&& sprite.hasGoalAt(
								"VexChargeAttackGoal", 4)
						&& sprite.hasGoalAt(
								"VexRandomMoveGoal", 8)
						&& sprite.hasGoalAt(
								"LookAtPlayerGoal", 9)
						&& sprite.hasGoalAt(
								"LookAtPlayerGoal", 10)
						&& sprite.countGoalsNamed(
								"LookAtPlayerGoal") == 2
						&& sprite.hasTargetGoalAt(
								"HurtByTargetGoal", 1)
						&& sprite.hasTargetGoalAt(
								"VexCopyOwnerTargetGoal",
								2)
						&& sprite.hasTargetGoalAt(
								"NearestAttackableTargetGoal",
								3)
						&& sprite.countTargetGoals() == 3,
				"Sour Sprite lost Vex flight, charge, wandering, look or targeting goals");
		require(helper,
				sprite.ambientSound()
								== SoundEvents.VEX_AMBIENT
						&& sprite.hurtSound()
								== SoundEvents.VEX_HURT
						&& sprite.deathSound()
								== SoundEvents.VEX_DEATH
						&& close(sprite.getBrightness(), 1.0D),
				"Sour Sprite lost Vex sounds or full brightness");

		BlockPos localAnchor =
				helper.absolutePos(new BlockPos(4, 3, 4));
		sprite.setPos(localAnchor.getX(),
				localAnchor.getY(), localAnchor.getZ());
		sprite.finalizeSpawn(helper.getLevel(),
				helper.getLevel()
						.getCurrentDifficultyAt(localAnchor),
				MobSpawnType.MOB_SUMMONED, null, null);
		require(helper,
				sprite.getMainHandItem().is(Items.IRON_SWORD)
						&& close(sprite.mainHandDropChance(),
								0.0D),
				"Sour Sprite lost the summoned Iron Sword or zero drop chance");

		BlockPos bound = localAnchor.offset(5, 2, -3);
		sprite.setBoundOrigin(bound);
		sprite.setLimitedLife(417);
		sprite.setIsCharging(true);
		CompoundTag saved =
				sprite.saveWithoutId(new CompoundTag());
		SourSprite restored =
				CakeWorldEntities.SOUR_SPRITE.get()
						.create(helper.getLevel());
		require(helper, restored != null,
				"Could not create Sour Sprite reload fixture");
		restored.load(saved);
		CompoundTag restoredState =
				restored.saveWithoutId(new CompoundTag());
		require(helper,
				bound.equals(restored.getBoundOrigin())
						&& restoredState.getInt(
								"LifeTicks") == 417
						&& !restored.isCharging(),
				"Sour Sprite lost Vex bound origin or limited life, or fabricated charging persistence");

		SourSorcerer owner =
				CakeWorldEntities.SOUR_SORCERER.get()
						.create(helper.getLevel());
		Pig ownerTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, owner != null && ownerTarget != null,
				"Could not create Sour Sprite owner-target fixtures");
		owner.setTarget(ownerTarget);
		sprite.setOwner(owner);
		sprite.setTarget(null);
		sprite.runTargetGoals();
		require(helper,
				sprite.getOwner() == owner
						&& sprite.getTarget()
								== ownerTarget
						&& owner.isAlliedTo(sprite),
				"Sour Sprite lost owner-target copying or Sour Sorcerer alliance");

		sprite.setPos(0.0D, 4.0D, 0.0D);
		ownerTarget.setPos(8.0D, 4.0D, 0.0D);
		sprite.setTarget(ownerTarget);
		WrappedGoal charge =
				sprite.goalNamed("VexChargeAttackGoal");
		require(helper, charge != null,
				"Could not inspect Sour Sprite charge goal");
		sprite.clearRecordedSound();
		charge.getGoal().start();
		require(helper,
				sprite.isCharging()
						&& sprite.getMoveControl()
								.hasWanted()
						&& sprite.recordedSound()
								== SoundEvents.VEX_CHARGE,
				"Sour Sprite charge did not target the victim's eyes, set its flag or play its cue");
		charge.getGoal().stop();
		require(helper, !sprite.isCharging(),
				"Sour Sprite charge flag did not clear");

		SourSpriteProbe decaying =
				new SourSpriteProbe(helper.getLevel());
		decaying.setNoAi(true);
		decaying.setHealth(14.0F);
		decaying.setLimitedLife(1);
		decaying.tick();
		CompoundTag decayedState =
				decaying.saveWithoutId(new CompoundTag());
		require(helper,
				close(decaying.getHealth(), 13.0D)
						&& decayedState.getInt(
								"LifeTicks") == 20
						&& decaying.isNoGravity()
						&& decaying
								.sawNoPhysicsDuringBaseTick()
						&& !decaying.noPhysics,
				"Sour Sprite lost no-physics flight, no gravity or twenty-tick limited-life starvation");

		Pig contactTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper, contactTarget != null,
				"Could not create Sour Sprite contact target");
		contactTarget.setPos(1.0D, 4.0D, 0.0D);
		Difficulty originalDifficulty =
				helper.getLevel().getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					new Difficulty[] {
							Difficulty.EASY,
							Difficulty.NORMAL}) {
				helper.getLevel().getServer()
						.setDifficulty(
								safeDifficulty, true);
				contactTarget.setHealth(10.0F);
				contactTarget.invulnerableTime = 0;
				contactTarget.removeAllEffects();
				contactTarget.setSecondsOnFire(5);
				contactTarget.fallDistance = 12.0F;
				contactTarget.setDeltaMovement(
						Vec3.ZERO);
				require(helper,
						sprite.doHurtTarget(
								contactTarget)
								&& close(
										contactTarget
												.getHealth(),
										10.0D)
								&& contactTarget
										.getEffect(
												MobEffects
														.CONFUSION)
										.getDuration() == 100
								&& contactTarget.hasEffect(
										MobEffects.GLOWING)
								&& contactTarget.hasEffect(
										MobEffects.SLOW_FALLING)
								&& contactTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& contactTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier() == 4
								&& contactTarget
										.getRemainingFireTicks()
										<= 0
								&& close(contactTarget
										.fallDistance, 0.0D)
								&& contactTarget
										.getDeltaMovement().y
										> 0.0D,
						safeDifficulty
								+ " Sour Sprite charge caused damage or lacked sour rescue");
			}

			helper.getLevel().getServer()
					.setDifficulty(Difficulty.HARD, true);
			contactTarget.setHealth(10.0F);
			contactTarget.invulnerableTime = 0;
			contactTarget.removeAllEffects();
			require(helper,
					sprite.doHurtTarget(contactTarget)
							&& close(
									contactTarget.getHealth(),
									6.0D)
							&& contactTarget
									.getActiveEffects()
									.isEmpty(),
					"Hard Sour Sprite did not retain exact four-point Vex contact damage");

			helper.getLevel().getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			SourSpriteProbe peaceful =
					new SourSpriteProbe(
							helper.getLevel());
			peaceful.checkDespawnRole();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Sour Sprite lost vanilla Monster removal");
		} finally {
			helper.getLevel().getServer().setDifficulty(
					originalDifficulty, true);
		}

		require(helper,
				sprite.getLootTableId().equals(
						new ResourceLocation(
								CakeWorld.MODID,
								"entities/sour_sprite"))
						&& CakeWorldItems
								.SOUR_SPRITE_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.SOUR_SPRITE
												.get())
								== SoundEvents
										.PARROT_IMITATE_VEX,
				"Sour Sprite lost empty-loot, testing-egg or Vex-mimic roles");
		ServerPlayer advancementPlayer =
				new ServerPlayer(
						helper.getLevel().getServer(),
						helper.getLevel(),
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2061"),
								"CakeWorldSourSpriteRoleTest"));
		VanillaRoleAdvancements.creditKilledVexRole(
				advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:vex");

		Registry<Biome> biomes = helper.getLevel()
				.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.VEX
											|| spawn.type
													== CakeWorldEntities
															.SOUR_SPRITE
															.get()),
					"Sour Sprite fabricated natural ecology in "
							+ biomeId);
		}

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						helper.absolutePos(
								new BlockPos(8, 3, 8)),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for Sour Sprite source conversion");
		Vex literal =
				EntityType.VEX.create(helper.getLevel());
		SourSorcerer conversionOwner =
				CakeWorldEntities.SOUR_SORCERER.get()
						.create(helper.getLevel());
		Pig conversionTarget =
				EntityType.PIG.create(helper.getLevel());
		Chicken passenger =
				EntityType.CHICKEN.create(helper.getLevel());
		Boat vehicle = new Boat(
				helper.getLevel(),
				cakeWorldPos.getX() + 2.0D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 2.0D);
		require(helper,
				literal != null
						&& conversionOwner != null
						&& conversionTarget != null
						&& passenger != null,
				"Could not create Sour Sprite direct-conversion fixtures");
		literal.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 0.5D);
		literal.finalizeSpawn(helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.MOB_SUMMONED, null, null);
		literal.setHealth(11.0F);
		literal.setCustomName(
				new TextComponent("Deferred Tang"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.setBoundOrigin(
				cakeWorldPos.offset(3, 2, -4));
		literal.setLimitedLife(509);
		literal.setOwner(conversionOwner);
		literal.setTarget(conversionTarget);
		literal.setIsCharging(true);
		literal.invulnerableTime = 37;
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		SourSprite converted =
				CakeWorldVexReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								literal);
		CompoundTag convertedState =
				converted == null
						? new CompoundTag()
						: converted.saveWithoutId(
								new CompoundTag());
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& close(converted.getHealth(),
								11.0D)
						&& converted.hasCustomName()
						&& "Deferred Tang".equals(
								converted.getName()
										.getString())
						&& converted
								.isPersistenceRequired()
						&& converted.isNoAi()
						&& converted.getMainHandItem()
								.is(Items.IRON_SWORD)
						&& cakeWorldPos.offset(3, 2, -4)
								.equals(converted
										.getBoundOrigin())
						&& convertedState.getInt(
								"LifeTicks") == 509
						&& converted.getOwner()
								== conversionOwner
						&& converted.getTarget()
								== conversionTarget
						&& converted.isCharging()
						&& converted.invulnerableTime == 37
						&& converted.getVehicle()
								== vehicle
						&& converted.getPassengers()
								.contains(passenger),
				"Fresh literal Vex conversion lost life, bound, owner, target, charging, state, equipment or relationships");
		require(helper,
				CakeWorldVexReplacement
						.replaceIfInCakeWorldBiome(
								helper.getLevel(),
								sprite) == null
						&& !sprite.isRemoved(),
				"Vex source conversion touched a non-literal entity type");
		passenger.discard();
		converted.discard();
		vehicle.discard();

		AABB eventArea =
				new AABB(cakeWorldPos).inflate(5.0D);
		helper.getLevel().getEntitiesOfClass(
				Vex.class, eventArea)
				.forEach(Vex::discard);
		Vex eventLiteral =
				EntityType.VEX.create(helper.getLevel());
		SourSorcerer eventOwner =
				CakeWorldEntities.SOUR_SORCERER.get()
						.create(helper.getLevel());
		Pig eventTarget =
				EntityType.PIG.create(helper.getLevel());
		require(helper,
				eventLiteral != null
						&& eventOwner != null
						&& eventTarget != null,
				"Could not create Sour Sprite entity-join fixtures");
		eventLiteral.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 0.5D);
		eventLiteral.finalizeSpawn(helper.getLevel(),
				helper.getLevel().getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.MOB_SUMMONED, null, null);
		eventLiteral.setCustomName(
				new TextComponent(
						"Entity Join Sour Sprite"));
		eventLiteral.setNoAi(true);
		eventLiteral.setBoundOrigin(
				cakeWorldPos.offset(-2, 1, 3));
		eventLiteral.setLimitedLife(611);
		eventLiteral.setOwner(eventOwner);
		eventLiteral.setTarget(eventTarget);
		eventLiteral.setIsCharging(true);
		require(helper,
				helper.getLevel()
						.addFreshEntity(eventLiteral),
				"Could not add literal Vex entity-join source");

		helper.runAfterDelay(4, () -> {
			List<SourSprite> emitted =
					helper.getLevel()
							.getEntitiesOfClass(
									SourSprite.class,
									eventArea,
									candidate ->
											candidate
													.hasCustomName()
													&& "Entity Join Sour Sprite"
															.equals(candidate
																	.getName()
																	.getString()));
			SourSprite eventSprite =
					emitted.size() == 1
							? emitted.get(0) : null;
			CompoundTag eventState =
					eventSprite != null
							? eventSprite
									.saveWithoutId(
											new CompoundTag())
							: new CompoundTag();
			require(helper,
					eventLiteral.isRemoved()
							&& eventSprite != null
							&& eventSprite
									.getOwner()
									== eventOwner
							&& eventSprite
									.getTarget()
									== eventTarget
							&& eventSprite
									.isCharging()
							&& cakeWorldPos.offset(
									-2, 1, 3)
									.equals(eventSprite
											.getBoundOrigin())
							&& eventState.getInt(
									"LifeTicks") >= 605
							&& eventState.getInt(
									"LifeTicks") <= 611
							&& eventSprite
									.getMainHandItem()
									.is(Items.IRON_SWORD),
					"Actual deferred Vex entity-join source lost summon state: literalRemoved="
							+ eventLiteral.isRemoved()
							+ ", emitted=" + emitted.size()
							+ ", owner="
							+ (eventSprite != null
									&& eventSprite.getOwner()
											== eventOwner)
							+ ", target="
							+ (eventSprite != null
									&& eventSprite.getTarget()
											== eventTarget)
							+ ", charging="
							+ (eventSprite != null
									&& eventSprite.isCharging())
							+ ", bound="
							+ (eventSprite == null
									? null
									: eventSprite
											.getBoundOrigin())
							+ ", life="
							+ eventState.getInt("LifeTicks")
							+ ", sword="
							+ (eventSprite != null
									&& eventSprite
											.getMainHandItem()
											.is(Items.IRON_SWORD)));
			emitted.forEach(SourSprite::discard);
			sprite.discard();
			restored.discard();
			decaying.discard();
			owner.discard();
			ownerTarget.discard();
			contactTarget.discard();
			conversionOwner.discard();
			conversionTarget.discard();
			eventOwner.discard();
			eventTarget.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 420)
	public static void gingerbreadFolkKeepVillageLifeAndFamilyBridges(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(5, 3, 5));
		AABB localArea = new AABB(anchor).inflate(12.0D);
		level.getEntitiesOfClass(
				Villager.class, localArea)
				.forEach(Villager::discard);
		level.getEntitiesOfClass(Witch.class, localArea)
				.forEach(Witch::discard);

		GingerbreadFolkProbe folk =
				new GingerbreadFolkProbe(level);
		require(helper,
				folk instanceof Villager
						&& folk.getType()
								== CakeWorldEntities
										.GINGERBREAD_FOLK.get()
						&& folk.getType().getCategory()
								== MobCategory.MISC
						&& close(folk.getMaxHealth(),
								20.0D)
						&& close(folk.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.5D)
						&& close(folk.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								48.0D)
						&& close(folk.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(folk.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& folk.getType()
								.clientTrackingRange() == 10
						&& folk.getInventory()
								.getContainerSize() == 8
						&& folk.getNavigation()
								instanceof GroundPathNavigation
										navigation
						&& navigation.canOpenDoors()
						&& !folk.removeWhenFarAway(
								Double.MAX_VALUE),
				"Gingerbread Folk lost the exact persistent Villager body, navigation, inventory or attributes");
		require(helper,
				folk.getBrain().getSchedule()
								== Schedule.VILLAGER_DEFAULT
						&& folk.ambientSound()
								== SoundEvents
										.VILLAGER_AMBIENT
						&& folk.hurtSound()
								== SoundEvents.VILLAGER_HURT
						&& folk.deathSound()
								== SoundEvents
										.VILLAGER_DEATH,
				"Gingerbread Folk lost the adult schedule or Villager sounds");

		folk.setVillagerData(new VillagerData(
				VillagerType.PLAINS,
				VillagerProfession.FARMER, 1));
		folk.refreshBrain(level);
		MerchantOffers farmerOffers = folk.getOffers();
		require(helper,
				farmerOffers.size() == 2
						&& farmerOffers.stream()
								.allMatch(offer ->
										!offer.getCostA()
												.isEmpty()
												&& !offer
														.getResult()
														.isEmpty())
						&& "entity.cakeworld.gingerbread_folk.farmer"
								.equals(folk
										.typeNameKey()),
				"Gingerbread Farmer lost vanilla/Forge profession trades or its translated profession identity");

		ServerPlayer tradingPlayer =
				new ServerPlayer(level.getServer(), level,
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2062"),
								"CakeWorldGingerbreadTradeTest"));
		tradingPlayer.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());
		MerchantOffer testOffer =
				new MerchantOffer(
						new ItemStack(Items.EMERALD),
						new ItemStack(Items.BREAD, 3),
						12, 5, 0.05F);
		MerchantOffers testOffers =
				new MerchantOffers();
		testOffers.add(testOffer);
		folk.setOffers(testOffers);
		folk.setTradingPlayer(tradingPlayer);
		int previousXp = folk.getVillagerXp();
		folk.notifyTrade(testOffer);
		require(helper,
				testOffer.getUses() == 1
						&& folk.getVillagerXp()
								== previousXp + 5,
				"Gingerbread Folk lost trade uses or Villager XP");
		requireCriterion(helper, tradingPlayer,
				"minecraft:adventure/trade", "traded");
		tradingPlayer.setPos(anchor.getX(),
				319.0D, anchor.getZ());
		CriteriaTriggers.TRADE.trigger(tradingPlayer,
				folk, testOffer.getResult());
		requireCriterion(helper, tradingPlayer,
				"minecraft:adventure/trade_at_world_height",
				"trade_at_world_height");
		tradingPlayer.setPos(anchor.getX(),
				anchor.getY(), anchor.getZ());

		Zombie cureSource =
				EntityType.ZOMBIE.create(level);
		require(helper, cureSource != null,
				"Could not create Gingerbread Folk cure criterion source");
		CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger(
				tradingPlayer, cureSource, folk);
		requireCriterion(helper, tradingPlayer,
				"minecraft:story/cure_zombie_villager",
				"cured_zombie");
		folk.onReputationEventFrom(
				ReputationEventType.TRADE,
				tradingPlayer);
		folk.onReputationEventFrom(
				ReputationEventType.ZOMBIE_VILLAGER_CURED,
				tradingPlayer);
		require(helper,
				folk.getPlayerReputation(
						tradingPlayer) > 0,
				"Gingerbread Folk lost positive Villager gossip");
		GingerbreadFolkProbe gossipPartner =
				new GingerbreadFolkProbe(level);
		gossipPartner.getRandom().setSeed(1978L);
		gossipPartner.gossip(level, folk,
				Math.max(1200L, level.getGameTime()));
		require(helper,
				gossipPartner.getPlayerReputation(
						tradingPlayer) > 0,
				"Gingerbread Folk did not transfer player reputation through Villager gossip");

		Difficulty originalDifficulty =
				level.getDifficulty();
		try {
			level.getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			GingerbreadFolkProbe peaceful =
					new GingerbreadFolkProbe(level);
			peaceful.checkDespawnRole();
			require(helper,
					!peaceful.isRemoved()
							&& !peaceful
									.despawnsInPeaceful(),
					"Peaceful Gingerbread Folk lost passive Villager persistence");
			peaceful.discard();

			level.getServer().setDifficulty(
					Difficulty.HARD, true);
			GingerbreadFolkProbe lightningFolk =
					new GingerbreadFolkProbe(level);
			lightningFolk.setPos(anchor.getX() + 7.0D,
					anchor.getY(), anchor.getZ());
			lightningFolk.setCustomName(
					new TextComponent(
							"Spiced by Lightning"));
			lightningFolk.setNoAi(true);
			level.addFreshEntity(lightningFolk);
			LightningBolt lightning =
					EntityType.LIGHTNING_BOLT
							.create(level);
			require(helper, lightning != null,
					"Could not create Gingerbread Folk lightning fixture");
			lightning.setPos(lightningFolk.getX(),
					lightningFolk.getY(),
					lightningFolk.getZ());
			lightningFolk.thunderHit(
					level, lightning);
			Witch witch = level.getEntitiesOfClass(
					Witch.class, localArea,
					candidate ->
							candidate.hasCustomName()
									&& "Spiced by Lightning"
											.equals(candidate
													.getName()
													.getString()))
					.stream().findFirst().orElse(null);
			require(helper,
					lightningFolk.isRemoved()
							&& witch != null
							&& witch.isNoAi()
							&& witch
									.isPersistenceRequired(),
					"Gingerbread Folk lost exact non-Peaceful Villager lightning conversion");
			if (witch != null) {
				witch.discard();
			}
			lightning.discard();
		} finally {
			level.getServer().setDifficulty(
					originalDifficulty, true);
		}

		require(helper,
				folk.getLootTableId().equals(
						new ResourceLocation(
								CakeWorld.MODID,
								"entities/gingerbread_folk"))
						&& CakeWorldItems
								.GINGERBREAD_FOLK_SPAWN_EGG
								.isPresent()
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.GINGERBREAD_FOLK
												.get())
								== null
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.GINGERBREAD_FOLK
												.get())
								== SpawnPlacements.Type
										.ON_GROUND
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.GINGERBREAD_FOLK
												.get())
								== Heightmap.Types
										.MOTION_BLOCKING_NO_LEAVES,
				"Gingerbread Folk lost empty loot, test egg, dormant Villager placement or deliberate no-mimic role");

		Registry<Biome> biomes = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory.MISC)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.VILLAGER
											|| spawn.type
													== CakeWorldEntities
															.GINGERBREAD_FOLK
															.get()),
					"Gingerbread Folk fabricated natural ecology in "
							+ biomeId);
		}

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						anchor.offset(16, 0, 16),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for Gingerbread Folk conversion");
		AABB eventArea =
				new AABB(cakeWorldPos).inflate(6.0D);
		level.getEntitiesOfClass(
				Villager.class, eventArea)
				.forEach(Villager::discard);

		Villager literal =
				EntityType.VILLAGER.create(level);
		Chicken passenger =
				EntityType.CHICKEN.create(level);
		Boat vehicle = new Boat(level,
				cakeWorldPos.getX() + 2.0D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 2.0D);
		require(helper,
				literal != null && passenger != null,
				"Could not create Gingerbread Folk direct-conversion fixtures");
		literal.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 0.5D);
		literal.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.STRUCTURE, null, null);
		literal.setVillagerData(new VillagerData(
				VillagerType.DESERT,
				VillagerProfession.LIBRARIAN, 3));
		MerchantOffers preservedOffers =
				new MerchantOffers();
		preservedOffers.add(new MerchantOffer(
				new ItemStack(Items.EMERALD, 2),
				new ItemStack(Items.BOOK),
				8, 11, 0.2F));
		literal.setOffers(preservedOffers);
		literal.setVillagerXp(77);
		literal.setCustomName(
				new TextComponent(
						"Professor Biscotti"));
		literal.setPersistenceRequired();
		literal.setNoAi(true);
		literal.onReputationEventFrom(
				ReputationEventType
						.ZOMBIE_VILLAGER_CURED,
				tradingPlayer);
		GlobalPos home = GlobalPos.of(level.dimension(),
				cakeWorldPos.offset(4, 0, -3));
		literal.getBrain().setMemory(
				MemoryModuleType.HOME, home);
		CompoundTag literalState =
				literal.saveWithoutId(
						new CompoundTag());
		literalState.putByte("FoodLevel", (byte)9);
		literalState.putLong("LastRestock", 12345L);
		literalState.putInt("RestocksToday", 1);
		literal.load(literalState);
		literal.getInventory().addItem(
				new ItemStack(Items.BREAD, 7));
		literal.setTradingPlayer(tradingPlayer);
		literal.invulnerableTime = 31;
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		GingerbreadFolk converted =
				CakeWorldVillagerReplacement
						.replaceIfInCakeWorldBiome(
								level, literal);
		CompoundTag convertedState =
				converted == null
						? new CompoundTag()
						: converted.saveWithoutId(
								new CompoundTag());
		require(helper, converted != null,
				"Literal Villager conversion did not create Gingerbread Folk");
		require(helper, literal.isRemoved(),
				"Literal Villager conversion did not remove its source");
		require(helper,
				converted.getVillagerData().getType()
								== VillagerType.DESERT
						&& converted.getVillagerData()
								.getProfession()
								== VillagerProfession
										.LIBRARIAN
						&& converted.getVillagerData()
								.getLevel() == 3,
				"Literal Villager conversion lost type, profession or career level: "
						+ converted.getVillagerData());
		require(helper,
				converted.getOffers().size() == 1
						&& converted.getOffers().get(0)
								.getResult().is(Items.BOOK),
				"Literal Villager conversion lost its trade offers");
		require(helper,
				converted.getInventory()
								.countItem(Items.BREAD) == 7
						&& converted.getVillagerXp() == 77,
				"Literal Villager conversion lost inventory or Villager XP: bread="
						+ converted.getInventory()
								.countItem(Items.BREAD)
						+ ", xp="
						+ converted.getVillagerXp());
		require(helper,
				convertedState.getByte("FoodLevel") == 9
						&& convertedState.getLong(
								"LastRestock") == 12345L
						&& convertedState.getInt(
								"RestocksToday") == 1,
				"Literal Villager conversion lost food or restock NBT: food="
						+ convertedState.getByte(
								"FoodLevel")
						+ ", last="
						+ convertedState.getLong(
								"LastRestock")
						+ ", count="
						+ convertedState.getInt(
								"RestocksToday"));
		require(helper,
				converted.assignProfessionWhenSpawned(),
				"Literal Villager conversion lost deferred structure profession assignment");
		require(helper,
				converted.getBrain()
						.getMemory(MemoryModuleType.HOME)
						.filter(home::equals).isPresent(),
				"Literal Villager conversion lost HOME brain/POI memory: "
						+ converted.getBrain().getMemory(
								MemoryModuleType.HOME));
		require(helper,
				converted.getPlayerReputation(
								tradingPlayer) > 0
						&& converted.getTradingPlayer()
								== tradingPlayer,
				"Literal Villager conversion lost gossip or its active trading player");
		require(helper,
				converted.invulnerableTime == 31
						&& converted.getVehicle()
								== vehicle
						&& converted.getPassengers()
								.contains(passenger),
				"Literal Villager conversion lost invulnerability, vehicle or passenger state");
		require(helper,
				converted.hasCustomName()
						&& "Professor Biscotti".equals(
								converted.getName()
										.getString())
						&& converted.isPersistenceRequired()
						&& converted.isNoAi(),
				"Literal Villager conversion lost name, persistence or AI state");
		require(helper,
				CakeWorldVillagerReplacement
						.replaceIfInCakeWorldBiome(
								level, converted) == null
						&& !converted.isRemoved(),
				"Villager source conversion touched a non-literal entity type");
		passenger.discard();
		converted.discard();
		vehicle.discard();

		Villager leashedLiteral =
				EntityType.VILLAGER.create(level);
		Pig leashHolder =
				EntityType.PIG.create(level);
		require(helper,
				leashedLiteral != null
						&& leashHolder != null,
				"Could not create Gingerbread Folk leash-conversion fixtures");
		leashedLiteral.setPos(
				cakeWorldPos.getX() + 3.5D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 0.5D);
		leashedLiteral.setLeashedTo(
				leashHolder, false);
		GingerbreadFolk leashedConverted =
				CakeWorldVillagerReplacement
						.replaceIfInCakeWorldBiome(
								level, leashedLiteral);
		require(helper,
				leashedConverted != null
						&& leashedLiteral.isRemoved()
						&& leashedConverted
								.getLeashHolder()
								== leashHolder,
				"Gingerbread Folk conversion lost a valid leash");
		leashedConverted.discard();
		leashHolder.discard();

		Villager eventLiteral =
				EntityType.VILLAGER.create(level);
		require(helper, eventLiteral != null,
				"Could not create Gingerbread Folk entity-join source");
		eventLiteral.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY() + 1.0D,
				cakeWorldPos.getZ() + 0.5D);
		eventLiteral.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.STRUCTURE, null, null);
		eventLiteral.setVillagerData(
				new VillagerData(
						VillagerType.SNOW,
						VillagerProfession.CLERIC,
						2));
		eventLiteral.setCustomName(
				new TextComponent(
						"Entity Join Gingerbread Folk"));
		eventLiteral.setNoAi(true);
		require(helper,
				level.addFreshEntity(eventLiteral),
				"Could not add literal Villager entity-join source");

		Map<BlockPos, BlockState> originalBlocks =
				new java.util.HashMap<>();
		for (int x = -2; x <= 3; ++x) {
			for (int z = -2; z <= 2; ++z) {
				BlockPos ground =
						anchor.offset(x, -1, z);
				originalBlocks.put(ground,
						level.getBlockState(ground));
				level.setBlock(ground,
						Blocks.STONE.defaultBlockState(),
						3);
				for (int y = 0; y <= 2; ++y) {
					BlockPos space =
							anchor.offset(x, y, z);
					originalBlocks.put(space,
							level.getBlockState(space));
					level.setBlock(space,
							Blocks.AIR.defaultBlockState(),
							3);
				}
			}
		}
		BlockPos bedFoot = anchor.offset(2, 0, 0);
		BlockPos bedHead =
				bedFoot.relative(Direction.NORTH);
		BlockPos secondBedFoot =
				anchor.offset(-1, 0, 2);
		BlockPos secondBedHead =
				secondBedFoot.relative(Direction.NORTH);
		BlockPos thirdBedFoot =
				anchor.offset(1, 0, 2);
		BlockPos thirdBedHead =
				thirdBedFoot.relative(Direction.NORTH);
		placeTestBed(level, bedFoot, Direction.NORTH);
		placeTestBed(level, secondBedFoot,
				Direction.NORTH);
		placeTestBed(level, thirdBedFoot,
				Direction.NORTH);
		List<BlockPos> familyBedPositions =
				List.of(bedFoot, bedHead,
						secondBedFoot, secondBedHead,
						thirdBedFoot, thirdBedHead);
		require(helper,
				familyBedPositions.stream().allMatch(
						position -> level.getBlockState(
										position)
								.is(Blocks.RED_BED)),
				"Gingerbread Folk family fixture did not retain all six bed halves");

		GingerbreadFolkProbe firstParent =
				new GingerbreadFolkProbe(level);
		GingerbreadFolkProbe secondParent =
				new GingerbreadFolkProbe(level);
		firstParent.setPos(anchor.getX() + 1.5D,
				anchor.getY(),
				anchor.getZ() - 0.5D);
		secondParent.setPos(anchor.getX() + 0.5D,
				anchor.getY(),
				anchor.getZ() - 0.5D);
		firstParent.setVillagerData(
				new VillagerData(
						VillagerType.DESERT,
						VillagerProfession.NONE,
						1));
		secondParent.setVillagerData(
				new VillagerData(
						VillagerType.SNOW,
						VillagerProfession.NONE,
						1));
		firstParent.refreshBrain(level);
		secondParent.refreshBrain(level);
		firstParent.getInventory().addItem(
				new ItemStack(Items.BREAD, 3));
		secondParent.getInventory().addItem(
				new ItemStack(Items.BREAD, 3));
		level.addFreshEntity(firstParent);
		level.addFreshEntity(secondParent);
		Villager directChild =
				firstParent.getBreedOffspring(
						level, secondParent);
		require(helper,
				directChild instanceof GingerbreadFolk
						&& directChild.getType()
								== CakeWorldEntities
										.GINGERBREAD_FOLK.get()
						&& directChild.getVillagerData()
								.getProfession()
								== VillagerProfession.NONE,
				"Gingerbread Folk offspring factory returned a literal Villager or retained a profession");
		directChild.discard();
		firstParent.getBrain().setMemory(
				MemoryModuleType
						.NEAREST_VISIBLE_LIVING_ENTITIES,
				new NearestVisibleLivingEntities(
						firstParent,
						List.of(secondParent)));
		firstParent.getBrain()
				.setActiveActivityIfPossible(
						Activity.IDLE);
		boolean familyVisibleBeforeBrain =
				firstParent.getBrain().getMemory(
								MemoryModuleType
										.NEAREST_VISIBLE_LIVING_ENTITIES)
						.filter(visible ->
								visible.contains(candidate ->
										candidate
												== secondParent))
						.isPresent();
		boolean idleBeforeBrain =
				firstParent.getBrain()
						.isActive(Activity.IDLE);
		boolean parentsCanBreedBeforeBrain =
				firstParent.canBreed()
						&& secondParent.canBreed();
		boolean foundFamilyInteraction = false;
		boolean foundFamilyBreedTarget = false;
		for (int attempt = 0; attempt < 96
				&& (!foundFamilyInteraction
						|| !foundFamilyBreedTarget);
				attempt++) {
			firstParent.runBrainOnce();
			foundFamilyInteraction |=
					firstParent.getBrain()
							.getRunningBehaviors()
							.stream()
							.anyMatch(behavior ->
									"GingerbreadFolkTradeWith"
											.equals(behavior
													.getClass()
													.getSimpleName()));
			foundFamilyBreedTarget |=
					firstParent.getBrain()
							.getRunningBehaviors()
							.stream()
							.anyMatch(behavior ->
									"GingerbreadFolkMakeLove"
											.equals(behavior
													.getClass()
													.getSimpleName()));
			firstParent.getBrain().stopAll(
					level, firstParent);
			firstParent.getBrain().eraseMemory(
					MemoryModuleType.WALK_TARGET);
			firstParent.getBrain().eraseMemory(
					MemoryModuleType.LOOK_TARGET);
			firstParent.getBrain().eraseMemory(
					MemoryModuleType.INTERACTION_TARGET);
			firstParent.getBrain().eraseMemory(
					MemoryModuleType.BREED_TARGET);
			firstParent.getBrain().setMemory(
					MemoryModuleType
							.NEAREST_VISIBLE_LIVING_ENTITIES,
					new NearestVisibleLivingEntities(
							firstParent,
							List.of(secondParent)));
			firstParent.getBrain()
					.setActiveActivityIfPossible(
							Activity.IDLE);
		}
		require(helper,
				foundFamilyInteraction
						&& foundFamilyBreedTarget,
				"Gingerbread Folk additive brain did not recognize its own family for social and breeding targets: social="
						+ foundFamilyInteraction
						+ ", breed="
						+ foundFamilyBreedTarget
						+ ", visible="
						+ familyVisibleBeforeBrain
						+ ", idle="
						+ idleBeforeBrain
						+ ", canBreed="
						+ parentsCanBreedBeforeBrain
						+ ", types="
						+ firstParent.getType()
						+ "/"
						+ secondParent.getType());
		firstParent.getBrain().setSchedule(
				Schedule.EMPTY);
		secondParent.getBrain().setSchedule(
				Schedule.EMPTY);
		firstParent.getBrain()
				.setActiveActivityIfPossible(
						Activity.IDLE);
		secondParent.getBrain()
				.setActiveActivityIfPossible(
						Activity.IDLE);
		BlockPos registeredBed =
				familyBedPositions.stream()
						.filter(position ->
								level.getPoiManager()
										.existsAtPosition(
												PoiType.HOME,
												position))
						.findFirst().orElse(bedFoot);
		boolean familyBedPoi =
				level.getPoiManager().existsAtPosition(
						PoiType.HOME, registeredBed);
		var familyPath =
				firstParent.getNavigation().createPath(
						registeredBed,
						PoiType.HOME.getValidRange());
		require(helper, familyBedPoi,
				"Gingerbread Folk family fixture did not expose a HOME bed POI; immediatePath="
						+ (familyPath != null
								&& familyPath.canReach()));

		helper.runAfterDelay(360, () -> {
			List<GingerbreadFolk> babies =
					level.getEntitiesOfClass(
							GingerbreadFolk.class,
							localArea,
							GingerbreadFolk::isBaby);
			GingerbreadFolk baby =
					babies.size() == 1
							? babies.get(0) : null;
			GlobalPos childHome = baby == null
					? null
					: baby.getBrain().getMemory(
							MemoryModuleType.HOME)
							.orElse(null);
			List<GingerbreadFolk> emitted =
					level.getEntitiesOfClass(
							GingerbreadFolk.class,
							eventArea,
							candidate ->
									candidate
											.hasCustomName()
											&& "Entity Join Gingerbread Folk"
													.equals(candidate
															.getName()
															.getString()));
			GingerbreadFolk eventFolk =
					emitted.size() == 1
							? emitted.get(0) : null;
			require(helper,
					baby != null
							&& firstParent.getAge() > 0
							&& firstParent.getAge() <= 6000
							&& secondParent.getAge() > 0
							&& secondParent.getAge() <= 6000
							&& childHome != null
							&& childHome.dimension()
									.equals(
											level.dimension())
							&& familyBedPositions
									.contains(
											childHome.pos()),
					"Gingerbread Folk did not complete real food-and-bed-backed family breeding: babies="
							+ babies.size()
							+ ", firstAge="
							+ firstParent.getAge()
							+ ", secondAge="
							+ secondParent.getAge()
							+ ", home=" + childHome
							+ ", firstBread="
							+ firstParent.getInventory()
									.countItem(
											Items.BREAD)
							+ ", secondBread="
							+ secondParent.getInventory()
									.countItem(
											Items.BREAD));
			require(helper,
					eventLiteral.isRemoved()
							&& eventFolk != null
							&& eventFolk
									.getVillagerData()
									.getType()
									== VillagerType.SNOW
							&& eventFolk
									.getVillagerData()
									.getProfession()
									== VillagerProfession
											.CLERIC
							&& eventFolk
									.getVillagerData()
									.getLevel() == 2,
					"Actual deferred Villager entity-join source lost structure identity: literalRemoved="
							+ eventLiteral.isRemoved()
							+ ", emitted="
							+ emitted.size());

			babies.forEach(
					GingerbreadFolk::discard);
			emitted.forEach(
					GingerbreadFolk::discard);
			firstParent.discard();
			secondParent.discard();
			folk.discard();
			gossipPartner.discard();
			cureSource.discard();
			originalBlocks.forEach((position, state) ->
					level.setBlock(position,
							state, 3));
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 160)
	public static void rollingPinRaidersKeepVindicatorRaidsAndSafeShoves(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(5, 3, 5));
		AABB localArea = new AABB(anchor).inflate(16.0D);
		level.getEntitiesOfClass(
				Vindicator.class, localArea)
				.forEach(Vindicator::discard);
		level.getEntitiesOfClass(Pig.class, localArea)
				.forEach(Pig::discard);

		RollingPinRaiderProbe bareRaider =
				new RollingPinRaiderProbe(level);
		require(helper,
				close(bareRaider.getMaxHealth(),
								24.0D)
						&& close(bareRaider
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.35D)
						&& close(bareRaider
								.getAttributeValue(
										Attributes
												.FOLLOW_RANGE),
								12.0D)
						&& close(bareRaider
								.getAttributeValue(
										Attributes
												.ATTACK_DAMAGE),
								5.0D)
						&& bareRaider
								.getExperienceValue()
								== 5,
				"Unequipped Rolling-Pin Raider lost the base Vindicator attributes or XP reward");
		bareRaider.discard();

		RollingPinRaiderProbe raider =
				new RollingPinRaiderProbe(level);
		raider.setPos(anchor.getX() + 0.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		raider.finalizeSpawn(level,
				level.getCurrentDifficultyAt(anchor),
				MobSpawnType.STRUCTURE, null, null);
		level.addFreshEntity(raider);
		int equippedExperience =
				raider.getExperienceValue();
		boolean groundNavigation =
				raider.getNavigation()
						instanceof GroundPathNavigation;
		boolean opensDoors = groundNavigation
				&& ((GroundPathNavigation)raider
						.getNavigation()).canOpenDoors();
		require(helper,
				raider instanceof Vindicator
						&& raider instanceof Raider
						&& raider
								instanceof PatrollingMonster
						&& raider.getType()
								== CakeWorldEntities
										.ROLLING_PIN_RAIDER
										.get()
						&& raider.getType().getCategory()
								== MobCategory.MONSTER
						&& close(raider.getMaxHealth(),
								24.0D)
						&& close(raider.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.35D)
						&& close(raider.getAttribute(
								Attributes.FOLLOW_RANGE)
										.getBaseValue(),
								12.0D)
						&& raider.getAttribute(
								Attributes.FOLLOW_RANGE)
								.getModifiers().stream()
								.anyMatch(modifier ->
										"Random spawn bonus"
												.equals(modifier
														.getName()))
						&& close(raider.getAttributeValue(
								Attributes.ATTACK_DAMAGE),
								5.0D)
						&& close(raider.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(raider.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& raider.getType()
								.clientTrackingRange() == 8
						&& raider.getMaxSpawnClusterSize()
								== 4
						&& equippedExperience >= 6
						&& equippedExperience <= 8
						&& raider.getMobType()
								== MobType.ILLAGER
						&& raider.canBeLeader()
						&& raider.canJoinPatrol()
						&& raider.canJoinRaid()
						&& raider.despawnsInPeaceful()
						&& groundNavigation
						&& opensDoors,
				"Rolling-Pin Raider lost the exact Vindicator body, attributes, equipped XP, navigation, raid or patrol roles"
						+ ": equippedExperience="
						+ equippedExperience
						+ ", maxHealth="
						+ raider.getMaxHealth()
						+ ", speed="
						+ raider.getAttributeValue(
								Attributes.MOVEMENT_SPEED)
						+ ", followBase="
						+ raider.getAttribute(
								Attributes.FOLLOW_RANGE)
								.getBaseValue()
						+ ", followEffective="
						+ raider.getAttributeValue(
								Attributes.FOLLOW_RANGE)
						+ ", attack="
						+ raider.getAttributeValue(
								Attributes.ATTACK_DAMAGE)
						+ ", dimensions="
						+ raider.getDimensions(
								Pose.STANDING)
						+ ", tracking="
						+ raider.getType()
								.clientTrackingRange()
						+ ", cluster="
						+ raider.getMaxSpawnClusterSize()
						+ ", mobType="
						+ raider.getMobType()
						+ ", leader="
						+ raider.canBeLeader()
						+ ", joinPatrol="
						+ raider.canJoinPatrol()
						+ ", joinRaid="
						+ raider.canJoinRaid()
						+ ", peaceful="
						+ raider.despawnsInPeaceful()
						+ ", navigation="
						+ raider.getNavigation()
								.getClass()
								.getSimpleName()
						+ ", opensDoors="
						+ opensDoors);
		require(helper,
				raider.getMainHandItem()
								.is(Items.IRON_AXE)
						&& raider.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/rolling_pin_raider"))
						&& raider.ambientSound()
								== SoundEvents
										.VINDICATOR_AMBIENT
						&& raider.hurtSound()
								== SoundEvents
										.VINDICATOR_HURT
						&& raider.deathSound()
								== SoundEvents
										.VINDICATOR_DEATH
						&& raider.getCelebrateSound()
								== SoundEvents
										.VINDICATOR_CELEBRATE,
				"Rolling-Pin Raider lost Vindicator equipment, loot or sounds");
		require(helper,
				raider.countGoalsNamed("FloatGoal") == 1
						&& raider.countGoalsNamed(
								"VindicatorBreakDoorGoal")
								== 1
						&& raider.countGoalsNamed(
								"RaiderOpenDoorGoal")
								== 1
						&& raider.countGoalsNamed(
								"HoldGroundAttackGoal")
								== 1
						&& raider.countGoalsNamed(
								"VindicatorMeleeAttackGoal")
								== 1
						&& raider.countGoalsNamed(
								"LongDistancePatrolGoal")
								== 1
						&& raider.countGoalsNamed(
								"RandomStrollGoal") == 1
						&& raider.countGoalsNamed(
								"LookAtPlayerGoal") == 2
						&& raider.countTargetGoalsNamed(
								"HurtByTargetGoal") == 1
						&& raider.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 3
						&& raider.countTargetGoalsNamed(
								"VindicatorJohnnyAttackGoal")
								== 1,
				"Rolling-Pin Raider lost Vindicator raid, door, melee, patrol, target or Johnny goals");
		require(helper,
				raider.getArmPose()
								== AbstractIllager
										.IllagerArmPose
										.CROSSED,
				"Idle Rolling-Pin Raider lost the crossed-arm pose");
		raider.setAggressive(true);
		require(helper,
				raider.getArmPose()
								== AbstractIllager
										.IllagerArmPose
										.ATTACKING,
				"Aggressive Rolling-Pin Raider lost the attacking pose");
		raider.setAggressive(false);
		raider.setCelebrating(true);
		require(helper,
				raider.getArmPose()
								== AbstractIllager
										.IllagerArmPose
										.CELEBRATING,
				"Victorious Rolling-Pin Raider lost the celebration pose");
		raider.setCelebrating(false);

		BlockPos patrolTarget =
				anchor.offset(80, 0, -48);
		RollingPinRaiderProbe captain =
				new RollingPinRaiderProbe(level);
		captain.setPos(anchor.getX() + 2.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		captain.setPatrolLeader(true);
		captain.setPatrolTarget(patrolTarget);
		captain.setCustomName(
				new TextComponent("Johnny"));
		captain.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						captain.blockPosition()),
				MobSpawnType.PATROL, null, null);
		require(helper,
				ItemStack.isSameItemSameTags(
						captain.getItemBySlot(
								EquipmentSlot.HEAD),
						Raid.getLeaderBannerInstance()),
				"Patrol-finalized Rolling-Pin Raider captain did not receive the leader banner");
		CompoundTag raiderState =
				captain.saveWithoutId(
						new CompoundTag());
		RollingPinRaiderProbe restored =
				new RollingPinRaiderProbe(level);
		restored.load(raiderState);
		CompoundTag restoredState =
				restored.saveWithoutId(
						new CompoundTag());
		require(helper,
				restoredState.getBoolean("Johnny")
						&& restored.isPatrolLeader()
						&& restored.hasPatrolTarget()
						&& patrolTarget.equals(
								restored.getPatrolTarget())
						&& ItemStack.isSameItemSameTags(
								restored.getItemBySlot(
										EquipmentSlot.HEAD),
								Raid.getLeaderBannerInstance())
						&& "Johnny".equals(
								restored.getName()
										.getString()),
				"Rolling-Pin Raider lost Johnny, patrol target, captain banner or name NBT");
		restored.setPos(anchor.getX() + 3.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		level.addFreshEntity(restored);
		Pig johnnyTarget =
				EntityType.PIG.create(level);
		require(helper, johnnyTarget != null,
				"Could not create Rolling-Pin Raider Johnny target");
		johnnyTarget.setPos(restored.getX() + 2.0D,
				restored.getY(), restored.getZ());
		johnnyTarget.setNoAi(true);
		level.addFreshEntity(johnnyTarget);
		require(helper,
				restored.startJohnnyTargetGoal()
						&& restored.getTarget()
								== johnnyTarget,
				"Johnny Rolling-Pin Raider did not target an otherwise neutral living entity");

		GingerbreadFolk villager =
				CakeWorldEntities.GINGERBREAD_FOLK
						.get().create(level);
		require(helper, villager != null,
				"Could not create Rolling-Pin Raider Villager-awareness fixture");
		villager.setPos(raider.getX() + 4.0D,
				raider.getY(), raider.getZ());
		villager.setNoAi(true);
		level.addFreshEntity(villager);
		villager.getBrain().eraseMemory(
				MemoryModuleType.NEAREST_HOSTILE);
		villager.getBrain().setMemory(
				MemoryModuleType
						.NEAREST_VISIBLE_LIVING_ENTITIES,
				new NearestVisibleLivingEntities(
						villager, List.of(raider)));
		raider.setTestTickCount(20);
		raider.runVillagerHostileRepair();
		require(helper,
				villager.getBrain().getMemory(
						MemoryModuleType.NEAREST_HOSTILE)
						.filter(hostile ->
								hostile == raider)
						.isPresent(),
				"Rolling-Pin Raider did not repair Vindicator fear for Gingerbread Folk");
		Evoker colleague =
				EntityType.EVOKER.create(level);
		require(helper,
				colleague != null
						&& raider.isAlliedTo(colleague),
				"Rolling-Pin Raider lost unteamed Illager alliance");
		colleague.discard();

		RollingPinRaiderProbe buffed =
				new RollingPinRaiderProbe(level);
		buffed.setPos(anchor.getX() + 6.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		buffed.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						buffed.blockPosition()),
				MobSpawnType.EVENT, null, null);
		Raid buffRaid = new Raid(197863, level,
				buffed.blockPosition());
		buffRaid.setBadOmenLevel(5);
		buffRaid.joinRaid(7, buffed, null, true);
		buffRaid.setLeader(7, buffed);
		buffed.seedRandom(0L);
		buffed.applyRaidBuffs(7, false);
		require(helper,
				buffed.getCurrentRaid() == buffRaid
						&& buffed.getWave() == 7
						&& buffRaid.getLeader(7)
								== buffed
						&& buffed.getMainHandItem()
								.is(Items.IRON_AXE)
						&& EnchantmentHelper
								.getItemEnchantmentLevel(
										Enchantments
												.SHARPNESS,
										buffed
												.getMainHandItem())
								== 2,
				"Rolling-Pin Raider lost wave-seven raid membership, leadership or Sharpness-II axe buff");
		buffRaid.removeFromRaid(buffed, true);
		buffed.discard();

		for (Difficulty difficulty :
				Difficulty.values()) {
			EntityMobGriefingEvent grief =
					new EntityMobGriefingEvent(raider);
			RollingPinRaiderGriefSafety
					.applyForDifficulty(
							grief, difficulty);
			require(helper,
					grief.getResult()
							== (difficulty
									== Difficulty.HARD
											? Event.Result.DEFAULT
											: Event.Result.DENY),
					difficulty
							+ " Rolling-Pin Raider door-grief policy crossed the Hard-only possession boundary");
		}

		Difficulty originalDifficulty =
				level.getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					List.of(Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL)) {
				level.getServer().setDifficulty(
						safeDifficulty, true);
				Pig safeTarget =
						EntityType.PIG.create(level);
				require(helper, safeTarget != null,
						"Could not create protected Rolling-Pin Raider target");
				safeTarget.setPos(
						raider.getX() + 1.0D,
						raider.getY(),
						raider.getZ());
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 9.0F;
				level.addFreshEntity(safeTarget);
				raider.doHurtTarget(safeTarget);
				require(helper,
						close(safeTarget.getHealth(),
								safeTarget
										.getMaxHealth())
								&& safeTarget
										.getDeltaMovement()
										.lengthSqr()
										> 0.01D
								&& !safeTarget.isOnFire()
								&& close(safeTarget
										.fallDistance,
										0.0D)
								&& safeTarget.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& safeTarget.hasEffect(
										MobEffects.GLOWING)
								&& safeTarget.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& safeTarget.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& safeTarget.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4,
						safeDifficulty
								+ " Rolling-Pin Raider shove caused health/fall/fire damage or lacked visible rescue");
				safeTarget.discard();
			}

			level.getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(level);
			require(helper, hardTarget != null,
					"Could not create Hard Rolling-Pin Raider target");
			hardTarget.setPos(raider.getX() + 1.0D,
					raider.getY(), raider.getZ());
			level.addFreshEntity(hardTarget);
			raider.doHurtTarget(hardTarget);
			require(helper,
					hardTarget.getHealth()
							< hardTarget.getMaxHealth()
							&& !hardTarget.hasEffect(
									MobEffects
											.DAMAGE_RESISTANCE),
					"Hard Rolling-Pin Raider did not retain real Vindicator axe damage");
			hardTarget.discard();

			level.getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			RollingPinRaiderProbe peaceful =
					new RollingPinRaiderProbe(level);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Rolling-Pin Raider lost vanilla Monster removal");
		} finally {
			level.getServer().setDifficulty(
					originalDifficulty, true);
		}

		try {
			Field raidTypeField =
					Raid.RaiderType.class
							.getDeclaredField(
									"entityType");
			Field wavesField =
					Raid.RaiderType.class
							.getDeclaredField(
									"spawnsPerWaveBeforeBonus");
			raidTypeField.setAccessible(true);
			wavesField.setAccessible(true);
			require(helper,
					raidTypeField.get(
							Raid.RaiderType.VINDICATOR)
								== EntityType.VINDICATOR
							&& Arrays.equals(
									(int[])wavesField.get(
											Raid.RaiderType
													.VINDICATOR),
									new int[] {0, 0, 2,
											0, 1, 4,
											2, 5}),
					"Rolling-Pin Raider lost the literal vanilla Vindicator raid source or exact wave counts");
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not inspect the vanilla Vindicator raid source",
					exception);
		}

		Registry<Biome> biomes = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.VINDICATOR
											|| spawn.type
													== CakeWorldEntities
															.ROLLING_PIN_RAIDER
															.get()),
					"Rolling-Pin Raider fabricated open-biome ecology in "
							+ biomeId);
		}
		TagKey<EntityType<?>> raiders =
				TagKey.create(
						Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft", "raiders"));
		require(helper,
				CakeWorldItems
						.ROLLING_PIN_RAIDER_SPAWN_EGG
						.isPresent()
						&& CakeWorldEntities
								.ROLLING_PIN_RAIDER
								.get().is(raiders)
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.ROLLING_PIN_RAIDER
												.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.ROLLING_PIN_RAIDER
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.VINDICATOR)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.ROLLING_PIN_RAIDER
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.VINDICATOR)
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.ROLLING_PIN_RAIDER
												.get())
								== SoundEvents
										.PARROT_IMITATE_VINDICATOR,
				"Rolling-Pin Raider lost raider tag, egg, exact placement or Lorikeet mimic");
		ServerPlayer advancementPlayer =
				new ServerPlayer(level.getServer(), level,
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2063"),
								"CakeWorldRollingPinRaiderRoleTest"));
		VanillaRoleAdvancements
				.creditKilledVindicatorRole(
						advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:vindicator");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						anchor.offset(16, 0, 16),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Vindicator conversion");
		AABB eventArea =
				new AABB(cakeWorldPos).inflate(6.0D);
		level.getEntitiesOfClass(
				Vindicator.class, eventArea)
				.forEach(Vindicator::discard);
		Vindicator literal =
				EntityType.VINDICATOR.create(level);
		Ravager mount =
				EntityType.RAVAGER.create(level);
		Chicken passenger =
				EntityType.CHICKEN.create(level);
		Pig conversionTarget =
				EntityType.PIG.create(level);
		require(helper,
				literal != null
						&& mount != null
						&& passenger != null
						&& conversionTarget != null,
				"Could not create Rolling-Pin Raider direct-conversion fixtures");
		mount.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		mount.setNoAi(true);
		level.addFreshEntity(mount);
		conversionTarget.setPos(
				cakeWorldPos.getX() + 3.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		conversionTarget.setNoAi(true);
		level.addFreshEntity(conversionTarget);
		literal.setPos(mount.getX(), mount.getY(),
				mount.getZ());
		literal.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.EVENT, null, null);
		literal.setHealth(17.0F);
		literal.setCustomName(
				new TextComponent("Johnny"));
		literal.setNoAi(true);
		literal.setPersistenceRequired();
		literal.setPatrolLeader(true);
		BlockPos transferredPatrolTarget =
				cakeWorldPos.offset(48, 0, -48);
		literal.setPatrolTarget(
				transferredPatrolTarget);
		literal.invulnerableTime = 29;
		literal.setTarget(conversionTarget);
		literal.setLastHurtByMob(
				conversionTarget);
		Raid transferRaid = new Raid(
				197864, level, cakeWorldPos);
		transferRaid.joinRaid(
				7, literal, null, true);
		transferRaid.setLeader(7, literal);
		level.addFreshEntity(literal);
		literal.startRiding(mount, true);
		passenger.startRiding(literal, true);
		RollingPinRaider converted =
				CakeWorldVindicatorReplacement
						.replaceIfInCakeWorldBiome(
								level, literal);
		CompoundTag convertedState =
				converted == null
						? new CompoundTag()
						: converted.saveWithoutId(
								new CompoundTag());
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& convertedState.getBoolean(
								"Johnny")
						&& close(converted.getHealth(),
								17.0D)
						&& converted.isNoAi()
						&& converted
								.isPersistenceRequired()
						&& converted.isPatrolLeader()
						&& transferredPatrolTarget
								.equals(converted
										.getPatrolTarget())
						&& converted.invulnerableTime
								== 29
						&& converted.getTarget()
								== conversionTarget
						&& converted.getLastHurtByMob()
								== conversionTarget
						&& converted.getCurrentRaid()
								== transferRaid
						&& converted.getWave() == 7
						&& transferRaid.getLeader(7)
								== converted
						&& transferRaid
								.getTotalRaidersAlive()
								== 1
						&& converted.getVehicle() == mount
						&& converted.getPassengers()
								.contains(passenger)
						&& converted.getMainHandItem()
								.is(Items.IRON_AXE),
				"Fresh literal Vindicator conversion lost Johnny, state, patrol, target, raid, leader, axe or riding relationships");
		require(helper,
				CakeWorldVindicatorReplacement
						.replaceIfInCakeWorldBiome(
								level, raider) == null
						&& !raider.isRemoved(),
				"Vindicator source conversion touched a non-literal entity type");
		passenger.discard();
		if (converted != null) {
			transferRaid.removeFromRaid(
					converted, true);
			converted.discard();
		}
		mount.discard();
		conversionTarget.discard();

		Vindicator eventLiteral =
				EntityType.VINDICATOR.create(level);
		require(helper, eventLiteral != null,
				"Could not create Rolling-Pin Raider entity-join source");
		eventLiteral.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		eventLiteral.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.STRUCTURE, null, null);
		eventLiteral.setCustomName(
				new TextComponent(
						"Entity Join Rolling-Pin Raider"));
		eventLiteral.setNoAi(true);
		eventLiteral.setPatrolTarget(
				cakeWorldPos.offset(-32, 0, 32));
		require(helper,
				level.addFreshEntity(eventLiteral),
				"Could not add literal Vindicator entity-join source");

		helper.runAfterDelay(4, () -> {
			List<RollingPinRaider> emitted =
					level.getEntitiesOfClass(
							RollingPinRaider.class,
							eventArea,
							candidate ->
									candidate.hasCustomName()
											&& "Entity Join Rolling-Pin Raider"
													.equals(candidate
															.getName()
															.getString()));
			RollingPinRaider eventRaider =
					emitted.size() == 1
							? emitted.get(0) : null;
			require(helper,
					eventLiteral.isRemoved()
							&& eventRaider != null
							&& eventRaider.isNoAi()
							&& eventRaider
									.hasPatrolTarget()
							&& eventRaider
									.getMainHandItem()
									.is(Items.IRON_AXE),
					"Actual deferred Vindicator entity-join source lost structure state: literalRemoved="
							+ eventLiteral.isRemoved()
							+ ", emitted="
							+ emitted.size());
			emitted.forEach(
					RollingPinRaider::discard);
			raider.discard();
			captain.discard();
			restored.discard();
			johnnyTarget.discard();
			villager.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 240)
	public static void travellingConfectionersKeepTradesCaravansAndDespawn(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(5, 3, 5));
		AABB localArea = new AABB(anchor).inflate(16.0D);
		level.getEntitiesOfClass(
				WanderingTrader.class, localArea)
				.forEach(WanderingTrader::discard);
		level.getEntitiesOfClass(
				TraderLlama.class, localArea)
				.forEach(TraderLlama::discard);

		TravellingConfectionerProbe trader =
				new TravellingConfectionerProbe(level);
		trader.setPos(anchor.getX() + 0.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		trader.seedRandom(1978L);
		level.addFreshEntity(trader);
		require(helper,
				trader instanceof WanderingTrader
						&& trader.getType()
								== CakeWorldEntities
										.TRAVELLING_CONFECTIONER
										.get()
						&& trader.getType().getCategory()
								== MobCategory.CREATURE
						&& close(trader.getMaxHealth(),
								20.0D)
						&& close(trader.getAttributeValue(
								Attributes.MOVEMENT_SPEED),
								0.7D)
						&& close(trader.getAttributeValue(
								Attributes.FOLLOW_RANGE),
								16.0D)
						&& close(trader.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(trader.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& close(trader
								.standingEyeHeight(),
								1.62D)
						&& trader.getType()
								.clientTrackingRange()
								== 10
						&& trader
								.getMaxSpawnClusterSize()
								== 4
						&& trader.experienceReward() == 0
						&& trader.getNavigation()
								instanceof GroundPathNavigation
						&& !trader.showProgressBar()
						&& !trader.removeWhenFarAway(
								1000000.0D)
						&& !trader.canBeLeashed(
								helper.makeMockPlayer())
						&& trader.getBreedOffspring(
								level, trader) == null,
				"Travelling Confectioner lost the exact Wandering Trader body, attributes, navigation or lifecycle");
		require(helper,
				trader.goalCount() == 18
						&& trader.goalCount(
								"FloatGoal") == 1
						&& trader.goalCount(
								"UseItemGoal") == 2
						&& trader.goalCount(
								"TradeWithPlayerGoal")
								== 1
						&& trader.goalCount(
								"AvoidEntityGoal") == 7
						&& trader.goalCount(
								"PanicGoal") == 1
						&& trader.goalCount(
								"LookAtTradingPlayerGoal")
								== 1
						&& trader.goalCount(
								"WanderToPositionGoal")
								== 1
						&& trader.goalCount(
								"MoveTowardsRestrictionGoal")
								== 1
						&& trader.goalCount(
								"WaterAvoidingRandomStrollGoal")
								== 1
						&& trader.goalCount(
								"InteractGoal") == 1
						&& trader.goalCount(
								"LookAtPlayerGoal") == 1
						&& trader.targetGoalCount() == 0,
				"Travelling Confectioner lost vanilla trade, avoidance, potion, wander or look goals");
		require(helper,
				trader.ambientSound()
								== SoundEvents
										.WANDERING_TRADER_AMBIENT
						&& trader.hurtSound()
								== SoundEvents
										.WANDERING_TRADER_HURT
						&& trader.deathSound()
								== SoundEvents
										.WANDERING_TRADER_DEATH
						&& trader.drinkingSound(
								new ItemStack(
										Items.MILK_BUCKET))
								== SoundEvents
										.WANDERING_TRADER_DRINK_MILK
						&& trader.drinkingSound(
								PotionUtils.setPotion(
										new ItemStack(
												Items.POTION),
										Potions
												.INVISIBILITY))
								== SoundEvents
										.WANDERING_TRADER_DRINK_POTION
						&& trader.tradeUpdatedSound(true)
								== SoundEvents
										.WANDERING_TRADER_YES
						&& trader.tradeUpdatedSound(false)
								== SoundEvents
										.WANDERING_TRADER_NO
						&& trader.getNotifyTradeSound()
								== SoundEvents
										.WANDERING_TRADER_YES,
				"Travelling Confectioner lost Wandering Trader voice, trade, potion or milk sounds");

		long originalDayTime = level.getDayTime();
		try {
			level.setDayTime(13000L);
			level.updateSkyBrightness();
			trader.setInvisible(false);
			ItemStack nightDrink =
					trader.startOnlyUsableItemGoal();
			require(helper,
					nightDrink.is(Items.POTION)
							&& PotionUtils.getPotion(
									nightDrink)
									== Potions
											.INVISIBILITY,
					"Night Travelling Confectioner did not select its Invisibility Potion");
			trader.setInvisible(true);
			level.setDayTime(1000L);
			level.updateSkyBrightness();
			ItemStack dayDrink =
					trader.startOnlyUsableItemGoal();
			require(helper,
					dayDrink.is(Items.MILK_BUCKET),
					"Daytime invisible Travelling Confectioner did not select its Milk Bucket");
		} finally {
			trader.setInvisible(false);
			trader.setItemSlot(EquipmentSlot.MAINHAND,
					ItemStack.EMPTY);
			level.setDayTime(originalDayTime);
			level.updateSkyBrightness();
		}

		MerchantOffers offers = trader.getOffers();
		Set<Item> snackItems = Set.of(
				CakeWorldItems.CHOCOLATE_SPONGE_SLICE
						.get(),
				CakeWorldItems.SIMPLE_BISCUIT.get(),
				CakeWorldItems.LEMONADE_BOTTLE.get(),
				CakeWorldItems.SHERBET_FIZZ.get(),
				CakeWorldItems.COMFORT_COCOA.get(),
				CakeWorldItems.MINT_WAFER.get());
		List<MerchantOffer> snackOffers =
				offers.stream()
						.filter(offer -> snackItems
								.contains(offer
										.getResult()
										.getItem()))
						.toList();
		List<MerchantOffer> seedOffers =
				offers.stream()
						.filter(offer -> offer
								.getResult().is(
										CakeWorldItems
												.SPRINKLE_SEEDS
												.get()))
						.toList();
		MerchantOffer seedOffer =
				seedOffers.size() == 1
						? seedOffers.get(0) : null;
		require(helper,
				offers.size() == 8
						&& snackOffers.size() == 1
						&& seedOffer != null
						&& seedOffer.getBaseCostA()
								.is(Items.EMERALD)
						&& seedOffer.getBaseCostA()
								.getCount() == 5
						&& seedOffer.getResult()
								.getCount() == 1
						&& seedOffer.getMaxUses() == 2
						&& seedOffer.getXp() == 1
						&& offers.stream()
								.filter(offer ->
										!snackOffers
												.contains(offer)
										&& offer
												!= seedOffer)
								.count() == 6,
				"Travelling Confectioner did not preserve six vanilla/Forge offers plus one snack and scarce Sprinkle Seeds");

		ServerPlayer tradingPlayer =
				new ServerPlayer(level.getServer(), level,
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2064"),
								"CakeWorldTravellingConfectionerTradeTest"));
		tradingPlayer.connection =
				new ServerGamePacketListenerImpl(
						level.getServer(),
						new Connection(
								PacketFlow.CLIENTBOUND),
						tradingPlayer);
		tradingPlayer.setPos(trader.getX(),
				trader.getY(), trader.getZ());
		tradingPlayer.setItemInHand(
				InteractionHand.MAIN_HAND,
				ItemStack.EMPTY);
		int talkedBefore = tradingPlayer.getStats()
				.getValue(Stats.CUSTOM.get(
						Stats.TALKED_TO_VILLAGER));
		InteractionResult interaction =
				trader.mobInteract(tradingPlayer,
						InteractionHand.MAIN_HAND);
		require(helper,
				interaction.consumesAction()
						&& trader.getTradingPlayer()
								== tradingPlayer
						&& tradingPlayer.getStats()
								.getValue(
										Stats.CUSTOM.get(
												Stats
														.TALKED_TO_VILLAGER))
								== talkedBefore + 1,
				"Travelling Confectioner did not open trade or award the villager-talk statistic");
		tradingPlayer.closeContainer();
		trader.setTradingPlayer(tradingPlayer);
		level.getEntitiesOfClass(
				ExperienceOrb.class,
				trader.getBoundingBox().inflate(3.0D))
				.forEach(ExperienceOrb::discard);
		require(helper, seedOffer != null,
				"Missing Sprinkle Seed offer fixture");
		trader.notifyTrade(seedOffer);
		List<ExperienceOrb> tradeExperience =
				level.getEntitiesOfClass(
						ExperienceOrb.class,
						trader.getBoundingBox()
								.inflate(3.0D));
		int experienceTotal = tradeExperience.stream()
				.mapToInt(ExperienceOrb::getValue)
				.sum();
		require(helper,
				seedOffer.getUses() == 1
						&& experienceTotal >= 3
						&& experienceTotal <= 6,
				"Travelling Confectioner lost trade uses or the three-to-six XP reward");
		requireCriterion(helper, tradingPlayer,
				"minecraft:adventure/trade", "traded");
		tradingPlayer.setPos(trader.getX(),
				319.0D, trader.getZ());
		CriteriaTriggers.TRADE.trigger(
				tradingPlayer, trader,
				seedOffer.getResult());
		requireCriterion(helper, tradingPlayer,
				"minecraft:adventure/trade_at_world_height",
				"trade_at_world_height");
		tradingPlayer.setPos(trader.getX(),
				trader.getY(), trader.getZ());
		CookbookEvents.onTrack(
				new PlayerEvent.StartTracking(
						tradingPlayer, trader));
		require(helper,
				CookbookProgress
						.read(CookbookProgress.snapshot(
								tradingPlayer))
						.get(DiscoveryType.MEETING)
						.contains(new ResourceLocation(
								CakeWorld.MODID,
								"travelling_confectioner")),
				"Meeting a Travelling Confectioner did not unlock its player-specific Cookbook discovery");

		BlockPos wanderTarget =
				anchor.offset(40, 0, -24);
		BlockPos restriction =
				anchor.offset(-8, 0, 8);
		trader.setDespawnDelay(4321);
		trader.setWanderTarget(wanderTarget);
		trader.restrictTo(restriction, 16);
		trader.setAge(-24000);
		trader.getInventory().setItem(0,
				new ItemStack(
						CakeWorldItems.CARAMEL_CHEW.get(),
						3));
		CompoundTag traderState =
				trader.saveWithoutId(
						new CompoundTag());
		TravellingConfectioner restored =
				CakeWorldEntities
						.TRAVELLING_CONFECTIONER
						.get().create(level);
		require(helper, restored != null,
				"Could not create Travelling Confectioner reload fixture");
		restored.load(traderState);
		CompoundTag restoredState =
				restored.saveWithoutId(
						new CompoundTag());
		MerchantOffer restoredSeed =
				restored.getOffers().stream()
						.filter(offer -> offer
								.getResult().is(
										CakeWorldItems
												.SPRINKLE_SEEDS
												.get()))
						.findFirst().orElse(null);
		require(helper,
				restored.getDespawnDelay() == 4321
						&& restored.getAge() == 0
						&& restoredState.contains(
								"WanderTarget")
						&& wanderTarget.equals(
								NbtUtils.readBlockPos(
										restoredState
												.getCompound(
														"WanderTarget")))
						&& restored.getInventory()
								.getItem(0).is(
										CakeWorldItems
												.CARAMEL_CHEW
												.get())
						&& restored.getInventory()
								.getItem(0)
								.getCount() == 3
						&& restored.getOffers().size()
								== 8
						&& restoredSeed != null
						&& restoredSeed.getUses() == 1,
				"Travelling Confectioner reload lost state: delay="
						+ restored.getDespawnDelay()
						+ ", age=" + restored.getAge()
						+ ", wander="
						+ (restoredState.contains(
								"WanderTarget")
										? NbtUtils.readBlockPos(
												restoredState
														.getCompound(
																"WanderTarget"))
										: null)
						+ ", inventory="
						+ restored.getInventory()
								.getItem(0)
						+ ", offers="
						+ restored.getOffers().size()
						+ ", seedUses="
						+ (restoredSeed == null
								? null
								: restoredSeed.getUses()));

		TravellingConfectionerProbe despawning =
				new TravellingConfectionerProbe(level);
		despawning.setPos(anchor.getX() + 5.5D,
				anchor.getY(), anchor.getZ() + 0.5D);
		despawning.setNoAi(true);
		despawning.setDespawnDelay(2);
		despawning.setTradingPlayer(tradingPlayer);
		level.addFreshEntity(despawning);
		despawning.aiStep();
		despawning.aiStep();
		require(helper,
				despawning.getDespawnDelay() == 2
						&& !despawning.isRemoved(),
				"Trading did not pause the Travelling Confectioner's despawn delay");
		despawning.setTradingPlayer(null);
		despawning.aiStep();
		require(helper,
				despawning.getDespawnDelay() == 1
						&& !despawning.isRemoved(),
				"Idle Travelling Confectioner did not resume its despawn countdown");
		despawning.aiStep();
		require(helper, despawning.isRemoved(),
				"Travelling Confectioner did not leave when its despawn delay expired");

		Registry<Biome> biomes = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.CREATURE)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.WANDERING_TRADER
											|| spawn.type
													== CakeWorldEntities
															.TRAVELLING_CONFECTIONER
															.get()),
					"Travelling Confectioner fabricated open-biome ecology in "
							+ biomeId);
		}
		require(helper,
				CakeWorldItems
						.TRAVELLING_CONFECTIONER_SPAWN_EGG
						.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.TRAVELLING_CONFECTIONER
												.get())
								== SpawnPlacements.Type
										.NO_RESTRICTIONS
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.TRAVELLING_CONFECTIONER
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.WANDERING_TRADER)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.TRAVELLING_CONFECTIONER
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.WANDERING_TRADER)
						&& trader.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/travelling_confectioner"))
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.TRAVELLING_CONFECTIONER
												.get())
								== null,
				"Travelling Confectioner lost its egg, exact default placement, empty loot or deliberate no-mimic role");
		require(helper,
				CakeWorldWanderingTraderReplacement
						.replaceIfInCakeWorldBiome(
								level, trader) == null
						&& !trader.isRemoved(),
				"Wandering Trader source conversion touched a non-literal entity type");
		SprinkleLlama llamaGuard =
				CakeWorldEntities.SPRINKLE_LLAMA
						.get().create(level);
		require(helper,
				llamaGuard != null
						&& CakeWorldTraderLlamaReplacement
								.replaceIfInCakeWorldBiome(
										level,
										llamaGuard)
								== null
						&& !llamaGuard.isRemoved(),
				"Trader-Llama source conversion touched a non-literal entity type");
		llamaGuard.discard();

		BlockPos cakeWorldBiomePos =
				findCakeWorldBiomePosition(helper,
						anchor.offset(24, 0, 24),
						256);
		require(helper, cakeWorldBiomePos != null,
				"Could not locate CakeWorld terrain for caravan conversion");
		BlockPos cakeWorldPos = level.getHeightmapPos(
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				cakeWorldBiomePos).above();
		AABB eventArea =
				new AABB(cakeWorldPos).inflate(8.0D);
		level.getEntitiesOfClass(
				WanderingTrader.class, eventArea)
				.forEach(WanderingTrader::discard);
		level.getEntitiesOfClass(
				TraderLlama.class, eventArea)
				.forEach(TraderLlama::discard);
		WanderingTrader literal =
				EntityType.WANDERING_TRADER
						.create(level);
		TraderLlama firstLiteralLlama =
				EntityType.TRADER_LLAMA.create(level);
		TraderLlama secondLiteralLlama =
				EntityType.TRADER_LLAMA.create(level);
		Boat caravanBoat =
				EntityType.BOAT.create(level);
		Chicken caravanPassenger =
				EntityType.CHICKEN.create(level);
		Pig caravanAttacker =
				EntityType.PIG.create(level);
		require(helper,
				literal != null
						&& firstLiteralLlama != null
						&& secondLiteralLlama != null
						&& caravanBoat != null
						&& caravanPassenger != null
						&& caravanAttacker != null,
				"Could not create Travelling Confectioner caravan fixtures");
		caravanBoat.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		level.addFreshEntity(caravanBoat);
		caravanAttacker.setPos(
				cakeWorldPos.getX() + 5.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		caravanAttacker.setNoAi(true);
		level.addFreshEntity(caravanAttacker);
		literal.setPos(caravanBoat.getX(),
				caravanBoat.getY(),
				caravanBoat.getZ());
		literal.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.EVENT, null, null);
		literal.getOffers();
		literal.setCustomName(new TextComponent(
				"Entity Join Travelling Confectioner"));
		literal.setNoAi(true);
		literal.setHealth(15.0F);
		literal.setDespawnDelay(48000);
		BlockPos caravanTarget =
				cakeWorldPos.offset(24, 0, -24);
		literal.setWanderTarget(caravanTarget);
		literal.restrictTo(caravanTarget, 16);
		literal.setTradingPlayer(tradingPlayer);
		literal.setLastHurtByMob(
				caravanAttacker);
		literal.invulnerableTime = 17;
		require(helper, level.addFreshEntity(literal),
				"Could not add literal Wandering Trader caravan source");
		literal.startRiding(caravanBoat, true);
		caravanPassenger.setPos(
				literal.getX(), literal.getY(),
				literal.getZ());
		level.addFreshEntity(caravanPassenger);
		caravanPassenger.startRiding(literal, true);
		firstLiteralLlama.setPos(
				literal.getX() + 2.0D,
				literal.getY(), literal.getZ());
		firstLiteralLlama.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						firstLiteralLlama
								.blockPosition()),
				MobSpawnType.EVENT, null, null);
		firstLiteralLlama.setCustomName(
				new TextComponent(
						"First Sprinkle Caravan"));
		firstLiteralLlama.setNoAi(true);
		level.addFreshEntity(firstLiteralLlama);
		firstLiteralLlama.setLeashedTo(
				literal, true);
		secondLiteralLlama.setPos(
				literal.getX() - 2.0D,
				literal.getY(), literal.getZ());
		secondLiteralLlama.finalizeSpawn(level,
				level.getCurrentDifficultyAt(
						secondLiteralLlama
								.blockPosition()),
				MobSpawnType.EVENT, null, null);
		secondLiteralLlama.setCustomName(
				new TextComponent(
						"Second Sprinkle Caravan"));
		secondLiteralLlama.setNoAi(true);
		level.addFreshEntity(secondLiteralLlama);
		secondLiteralLlama.setLeashedTo(
				literal, true);
		UUID originalActiveTrader = level
				.getServer().getWorldData()
				.overworldData()
				.getWanderingTraderId();
		level.getServer().getWorldData()
				.overworldData()
				.setWanderingTraderId(
						literal.getUUID());

		helper.runAfterDelay(4, () -> {
			List<TravellingConfectioner>
					emittedTraders =
					level.getEntitiesOfClass(
							TravellingConfectioner.class,
							eventArea,
							candidate ->
									candidate.hasCustomName()
											&& "Entity Join Travelling Confectioner"
													.equals(candidate
															.getName()
															.getString()));
			TravellingConfectioner emitted =
					emittedTraders.size() == 1
							? emittedTraders.get(0)
							: null;
			List<SprinkleLlama> emittedLlamas =
					level.getEntitiesOfClass(
							SprinkleLlama.class,
							eventArea,
							candidate ->
									candidate.hasCustomName()
											&& candidate
													.getName()
													.getString()
													.endsWith(
															"Sprinkle Caravan"));
			require(helper,
					literal.isRemoved()
							&& firstLiteralLlama
									.isRemoved()
							&& secondLiteralLlama
									.isRemoved()
							&& emitted != null
							&& close(emitted.getHealth(),
									15.0D)
							&& emitted.isNoAi()
							&& emitted
									.getDespawnDelay()
									== 48000
							&& emitted
									.getTradingPlayer()
									== tradingPlayer
							&& emitted
									.getLastHurtByMob()
									== caravanAttacker
							&& emitted.hasRestriction()
							&& caravanTarget.equals(
									emitted
											.getRestrictCenter())
							&& emitted
									.getRestrictRadius()
									== 16.0F
							&& emitted.invulnerableTime
									> 0
							&& emitted.invulnerableTime
									<= 17
							&& emitted.getVehicle()
									== caravanBoat
							&& emitted.getPassengers()
									.contains(
											caravanPassenger)
							&& emitted.getOffers()
									.size() == 8
							&& level.getServer()
									.getWorldData()
									.overworldData()
									.getWanderingTraderId()
									.equals(emitted
											.getUUID())
							&& emittedLlamas.size()
									== 2
							&& emittedLlamas.stream()
									.allMatch(llama ->
											llama
													.getLeashHolder()
													== emitted),
					"Actual deferred caravan conversion lost trader state, world UUID, riding or both Sprinkle-Llama leads: traders="
							+ emittedTraders.size()
							+ ", llamas="
							+ emittedLlamas.size());
			SprinkleLlamaProbe defended =
					new SprinkleLlamaProbe(level);
			if (emitted != null) {
				caravanAttacker.setPos(
						emitted.getX() + 1.0D,
						emitted.getY() + 8.0D,
						emitted.getZ());
				defended.setPos(
						emitted.getX() + 0.25D,
						emitted.getY() + 8.0D,
						emitted.getZ());
			} else {
				defended.setPos(
						cakeWorldPos.getX()
								+ 3.5D,
						cakeWorldPos.getY()
								+ 2.0D,
						cakeWorldPos.getZ()
								+ 0.5D);
			}
			defended.setNoAi(true);
			level.addFreshEntity(defended);
			if (emitted != null) {
				defended.setLeashedTo(
						emitted, true);
			}
			net.minecraft.world.entity.ai.goal.Goal
					defenceGoal =
							defended.targetSelector
									.getAvailableGoals()
									.stream()
									.map(WrappedGoal::getGoal)
									.filter(goal -> goal
											.getClass()
											.getSimpleName()
											.equals(
													"TraderLlamaDefendWanderingTraderGoal"))
									.findFirst()
									.orElse(null);
			boolean canDefend =
					defenceGoal != null
							&& defenceGoal.canUse();
			if (canDefend) {
				defenceGoal.start();
			}
			require(helper,
					emitted != null
							&& canDefend
							&& defended.getTarget()
									== caravanAttacker,
					"Sprinkle Llama did not defend its custom Travelling Confectioner: goal="
							+ (defenceGoal != null)
							+ ", canUse="
							+ canDefend
							+ ", holder="
							+ defended.getLeashHolder()
							+ ", lastHurt="
							+ (emitted == null
									? null
									: emitted
											.getLastHurtByMob())
							+ ", hurtTimestamp="
							+ (emitted == null
									? null
									: emitted
											.getLastHurtByMobTimestamp())
							+ ", canAttack="
							+ defended.canAttack(
									caravanAttacker)
							+ ", lineOfSight="
							+ defended.getSensing()
									.hasLineOfSight(
											caravanAttacker)
							+ ", distanceSquared="
							+ defended.distanceToSqr(
									caravanAttacker));

			level.getServer().getWorldData()
					.overworldData()
					.setWanderingTraderId(
							originalActiveTrader);
			emittedLlamas.forEach(
					SprinkleLlama::discard);
			defended.discard();
			emittedTraders.forEach(
					TravellingConfectioner::discard);
			tradeExperience.forEach(
					ExperienceOrb::discard);
			caravanBoat.discard();
			caravanPassenger.discard();
			caravanAttacker.discard();
			trader.discard();
			restored.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void bitterBakersKeepWitchPotionsRaidsAndSafeMixtures(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(5, 3, 5));
		AABB localArea = new AABB(anchor).inflate(20.0D);
		level.getEntitiesOfClass(
				Witch.class, localArea)
				.forEach(Witch::discard);
		level.getEntitiesOfClass(
				ThrownPotion.class, localArea)
				.forEach(ThrownPotion::discard);

		BitterBakerProbe baker =
				new BitterBakerProbe(level);
		VanillaWitchProbe vanilla =
				new VanillaWitchProbe(level);
		baker.setPos(anchor.getX() + 0.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		level.addFreshEntity(baker);
		require(helper,
				baker instanceof Witch
						&& baker instanceof Raider
						&& baker.getType()
								== CakeWorldEntities
										.BITTER_BAKER
										.get()
						&& baker.getType().getCategory()
								== MobCategory.MONSTER
						&& close(baker.getMaxHealth(),
								26.0D)
						&& close(baker
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.25D)
						&& close(baker.getDimensions(
								Pose.STANDING).width,
								0.6D)
						&& close(baker.getDimensions(
								Pose.STANDING).height,
								1.95D)
						&& close(baker
								.standingEyeHeight(),
								1.62D)
						&& baker.getType()
								.clientTrackingRange() == 8
						&& baker.getMaxSpawnClusterSize()
								== vanilla
										.getMaxSpawnClusterSize()
						&& baker.experienceReward()
								== vanilla
										.experienceReward()
						&& baker.canJoinRaid()
								== vanilla
										.canJoinRaid()
						&& !baker.canBeLeader()
						&& baker.despawnsInPeaceful()
						&& baker.getNavigation()
								instanceof GroundPathNavigation,
				"Bitter Baker lost the exact Witch body, attributes, navigation, XP or raid role");
		BitterBakerProbe naturalBaker =
				new BitterBakerProbe(level);
		BitterBakerProbe eventSpawnBaker =
				new BitterBakerProbe(level);
		naturalBaker.finalizeSpawn(level,
				level.getCurrentDifficultyAt(anchor),
				MobSpawnType.NATURAL, null, null);
		eventSpawnBaker.finalizeSpawn(level,
				level.getCurrentDifficultyAt(anchor),
				MobSpawnType.EVENT, null, null);
		require(helper,
				!naturalBaker.canJoinRaid()
						&& eventSpawnBaker.canJoinRaid(),
				"Bitter Baker crossed Witch's literal-type natural-versus-event raid-eligibility boundary");
		naturalBaker.discard();
		eventSpawnBaker.discard();
		require(helper,
				baker.goalSignatures().equals(
								vanilla
										.goalSignatures())
						&& baker.targetGoalSignatures()
								.equals(vanilla
										.targetGoalSignatures())
						&& baker.countTargetGoalsNamed(
								"NearestHealableRaiderTargetGoal")
								== 1,
				"Bitter Baker lost or duplicated a Witch goal while repairing the literal healing predicate: bakerGoals="
						+ baker.goalSignatures()
						+ ", vanillaGoals="
						+ vanilla.goalSignatures()
						+ ", bakerTargets="
						+ baker.targetGoalSignatures()
						+ ", vanillaTargets="
						+ vanilla.targetGoalSignatures());
		require(helper,
				baker.getLootTableId().equals(
								new ResourceLocation(
										CakeWorld.MODID,
										"entities/bitter_baker"))
						&& baker.ambientSound()
								== SoundEvents
										.WITCH_AMBIENT
						&& baker.hurtSound()
								== SoundEvents.WITCH_HURT
						&& baker.deathSound()
								== SoundEvents.WITCH_DEATH
						&& baker.getCelebrateSound()
								== SoundEvents
										.WITCH_CELEBRATE
						&& close(baker
								.magicDamageAfterAbsorb(
										DamageSource.MAGIC,
										10.0F),
								vanilla
										.magicDamageAfterAbsorb(
												DamageSource.MAGIC,
												10.0F)),
				"Bitter Baker lost Witch loot, sounds, celebration or magic resistance");

		BitterBakerProbe drinker =
				new BitterBakerProbe(level);
		drinker.setSecondsOnFire(200);
		drinker.seedRandom(1978L);
		int selectionTicks = 0;
		while (!drinker.isDrinkingPotion()
				&& selectionTicks++ < 200) {
			drinker.runAiStep();
		}
		require(helper,
				drinker.isDrinkingPotion()
						&& drinker.getMainHandItem()
								.is(Items.POTION)
						&& PotionUtils.getPotion(
								drinker
										.getMainHandItem())
								== Potions.FIRE_RESISTANCE
						&& close(drinker
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.0D),
				"Bitter Baker did not select Fire Resistance or apply the exact Witch drinking speed penalty in "
						+ selectionTicks + " ticks");
		int drinkingTicks = 0;
		while (drinker.isDrinkingPotion()
				&& drinkingTicks++ < 40) {
			drinker.runAiStep();
		}
		require(helper,
				!drinker.isDrinkingPotion()
						&& drinker.getMainHandItem()
								.isEmpty()
						&& drinker.hasEffect(
								MobEffects
										.FIRE_RESISTANCE)
						&& close(drinker
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.25D),
				"Bitter Baker did not consume its self-selected potion or restore movement speed");
		drinker.discard();

		Difficulty originalDifficulty =
				level.getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					List.of(Difficulty.PEACEFUL,
							Difficulty.EASY,
							Difficulty.NORMAL)) {
				level.getServer().setDifficulty(
						safeDifficulty, true);
				Pig target =
						EntityType.PIG.create(level);
				require(helper, target != null,
						"Could not create protected Bitter Baker target");
				target.setPos(baker.getX() + 4.0D,
						baker.getY(), baker.getZ());
				target.setNoAi(true);
				target.setSecondsOnFire(5);
				target.fallDistance = 9.0F;
				level.addFreshEntity(target);
				level.getEntitiesOfClass(
						ThrownPotion.class,
						localArea)
						.forEach(
								ThrownPotion::discard);
				baker.performRangedAttack(
						target, 1.0F);
				List<ThrownPotion> mixtures =
						level.getEntitiesOfClass(
								ThrownPotion.class,
								localArea);
				require(helper,
						mixtures.size() == 1
								&& PotionUtils
										.getPotion(
												mixtures
														.get(0)
														.getItem())
										== Potions
												.SLOWNESS
								&& close(target
										.getHealth(),
										target
												.getMaxHealth())
								&& !target.isOnFire()
								&& close(target
										.fallDistance,
										0.0D)
								&& target.hasEffect(
										MobEffects
												.MOVEMENT_SLOWDOWN)
								&& target.hasEffect(
										MobEffects
												.GLOWING)
								&& target.hasEffect(
										MobEffects
												.SLOW_FALLING)
								&& target.hasEffect(
										MobEffects
												.FIRE_RESISTANCE)
								&& target.getEffect(
										MobEffects
												.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4
								&& !target.hasEffect(
										MobEffects
												.POISON),
						safeDifficulty
								+ " Bitter Baker mixture caused health/fall/fire danger or lost its visible rescue envelope");
				mixtures.forEach(
						ThrownPotion::discard);
				target.discard();
			}

			level.getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(level);
			require(helper, hardTarget != null,
					"Could not create Hard Bitter Baker target");
			hardTarget.setPos(baker.getX() + 5.0D,
					baker.getY(), baker.getZ());
			hardTarget.setHealth(6.0F);
			hardTarget.setNoAi(true);
			level.addFreshEntity(hardTarget);
			baker.performRangedAttack(
					hardTarget, 1.0F);
			List<ThrownPotion> hardPotions =
					level.getEntitiesOfClass(
							ThrownPotion.class,
							localArea);
			require(helper,
					hardPotions.size() == 1
							&& PotionUtils.getPotion(
									hardPotions.get(0)
											.getItem())
									== Potions.HARMING
							&& !hardTarget.hasEffect(
									MobEffects
											.DAMAGE_RESISTANCE),
					"Hard Bitter Baker did not retain the real context-sensitive Witch harming potion");
			hardPotions.forEach(
					ThrownPotion::discard);
			hardTarget.discard();

			level.getServer().setDifficulty(
					Difficulty.NORMAL, true);
			RollingPinRaider healingTarget =
					CakeWorldEntities
							.ROLLING_PIN_RAIDER
							.get().create(level);
			require(helper, healingTarget != null,
					"Could not create Bitter Baker raid-healing target");
			healingTarget.setPos(
					baker.getX() + 3.0D,
					baker.getY(), baker.getZ());
			healingTarget.setHealth(3.0F);
			healingTarget.setNoAi(true);
			level.addFreshEntity(healingTarget);
			baker.setTarget(healingTarget);
			baker.performRangedAttack(
					healingTarget, 1.0F);
			List<ThrownPotion> healingPotions =
					level.getEntitiesOfClass(
							ThrownPotion.class,
							localArea);
			require(helper,
					healingPotions.size() == 1
							&& PotionUtils.getPotion(
									healingPotions
											.get(0)
											.getItem())
									== Potions.HEALING
							&& baker.getTarget() == null,
					"Bitter Baker lost the Witch low-health Raider healing branch");
			healingPotions.forEach(
					ThrownPotion::discard);
			healingTarget.setHealth(10.0F);
			baker.setTarget(healingTarget);
			baker.performRangedAttack(
					healingTarget, 1.0F);
			List<ThrownPotion> regenerationPotions =
					level.getEntitiesOfClass(
							ThrownPotion.class,
							localArea);
			require(helper,
					regenerationPotions.size() == 1
							&& PotionUtils.getPotion(
									regenerationPotions
											.get(0)
											.getItem())
									== Potions.REGENERATION
							&& baker.getTarget() == null,
					"Bitter Baker lost the Witch higher-health Raider regeneration branch");
			regenerationPotions.forEach(
					ThrownPotion::discard);

			BitterBaker otherBaker =
					CakeWorldEntities.BITTER_BAKER
							.get().create(level);
			require(helper, otherBaker != null,
					"Could not create same-family Bitter Baker healing boundary");
			otherBaker.setPos(
					baker.getX() + 1.0D,
					baker.getY(), baker.getZ());
			otherBaker.setHealth(3.0F);
			otherBaker.setNoAi(true);
			level.addFreshEntity(otherBaker);
			baker.setTarget(otherBaker);
			baker.performRangedAttack(
					otherBaker, 1.0F);
			require(helper,
					baker.getTarget() == null
							&& level.getEntitiesOfClass(
									ThrownPotion.class,
									localArea)
									.isEmpty(),
					"Bitter Baker selected another Witch-family entity through vanilla's literal-type healing seam");
			otherBaker.discard();
			healingTarget.discard();

			level.getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			BitterBakerProbe peaceful =
					new BitterBakerProbe(level);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Bitter Baker lost vanilla Monster removal");
		} finally {
			level.getServer().setDifficulty(
					originalDifficulty, true);
		}

		try {
			Field raidTypeField =
					Raid.RaiderType.class
							.getDeclaredField(
									"entityType");
			Field wavesField =
					Raid.RaiderType.class
							.getDeclaredField(
									"spawnsPerWaveBeforeBonus");
			raidTypeField.setAccessible(true);
			wavesField.setAccessible(true);
			require(helper,
					raidTypeField.get(
							Raid.RaiderType.WITCH)
								== EntityType.WITCH
							&& Arrays.equals(
									(int[])wavesField.get(
											Raid.RaiderType
													.WITCH),
									new int[] {0, 0, 0,
											0, 3, 0,
											0, 1}),
					"Bitter Baker lost the literal vanilla Witch raid source or exact wave counts");
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not inspect the vanilla Witch raid source",
					exception);
		}

		Registry<Biome> biomes = level.registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		int activeBakerProfiles = 0;
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = biomes.get(biomeId);
			require(helper, biome != null,
					"Missing Bitter Baker ecology-audit biome "
							+ biomeId);
			for (MobSpawnSettings.SpawnerData spawn :
					biome.getMobSettings().getMobs(
							MobCategory.MONSTER)
							.unwrap()) {
				require(helper,
						spawn.type
								!= EntityType.WITCH,
						"Literal Witch leaked through CakeWorld ecology in "
								+ biomeId);
				if (spawn.type
						== CakeWorldEntities
								.BITTER_BAKER
								.get()) {
					activeBakerProfiles++;
					require(helper,
							spawn.getWeight()
									.asInt() == 5
									&& spawn.minCount
											== 1
									&& spawn.maxCount
											== 1,
							"Bitter Baker changed the inherited Witch spawn profile in "
									+ biomeId);
				}
			}
		}
		require(helper, activeBakerProfiles > 0,
				"Bitter Baker did not replace any inherited Witch profile in the current CakeWorld biomes");

		TagKey<EntityType<?>> raiders =
				TagKey.create(
						Registry.ENTITY_TYPE_REGISTRY,
						new ResourceLocation(
								"minecraft", "raiders"));
		require(helper,
				CakeWorldItems.BITTER_BAKER_SPAWN_EGG
						.isPresent()
						&& CakeWorldEntities
								.BITTER_BAKER
								.get().is(raiders)
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.BITTER_BAKER
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.WITCH)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.BITTER_BAKER
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.WITCH)
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.BITTER_BAKER
												.get())
								== SoundEvents
										.PARROT_IMITATE_WITCH,
				"Bitter Baker lost raider tag, egg, exact placement or Lorikeet mimic");
		ServerPlayer advancementPlayer =
				new ServerPlayer(level.getServer(), level,
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2065"),
								"CakeWorldBitterBakerRoleTest"));
		VanillaRoleAdvancements
				.creditKilledWitchRole(
						advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:witch");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						anchor.offset(16, 0, 16),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for literal Witch conversion");
		AABB eventArea =
				new AABB(cakeWorldPos).inflate(7.0D);
		level.getEntitiesOfClass(
				Witch.class, eventArea)
				.forEach(Witch::discard);
		Witch literal =
				EntityType.WITCH.create(level);
		Ravager mount =
				EntityType.RAVAGER.create(level);
		Chicken passenger =
				EntityType.CHICKEN.create(level);
		Pig conversionTarget =
				EntityType.PIG.create(level);
		require(helper,
				literal != null
						&& mount != null
						&& passenger != null
						&& conversionTarget != null,
				"Could not create Bitter Baker direct-conversion fixtures");
		mount.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		mount.setNoAi(true);
		level.addFreshEntity(mount);
		conversionTarget.setPos(
				cakeWorldPos.getX() + 3.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		conversionTarget.setNoAi(true);
		level.addFreshEntity(conversionTarget);
		literal.setPos(mount.getX(), mount.getY(),
				mount.getZ());
		literal.setHealth(17.0F);
		literal.setCustomName(
				new TextComponent("Bitter Brew"));
		literal.setNoAi(true);
		literal.setPersistenceRequired();
		literal.invulnerableTime = 31;
		literal.setItemSlot(EquipmentSlot.MAINHAND,
				PotionUtils.setPotion(
						new ItemStack(Items.POTION),
						Potions.SWIFTNESS));
		literal.setUsingItem(true);
		literal.setTarget(conversionTarget);
		literal.setLastHurtByMob(
				conversionTarget);
		Raid transferRaid = new Raid(
				197865, level, cakeWorldPos);
		transferRaid.joinRaid(
				5, literal, null, true);
		level.addFreshEntity(literal);
		literal.startRiding(mount, true);
		passenger.startRiding(literal, true);
		BitterBaker converted =
				CakeWorldWitchReplacement
						.replaceIfInCakeWorldBiome(
								level, literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& close(converted.getHealth(),
								17.0D)
						&& "Bitter Brew".equals(
								converted.getName()
										.getString())
						&& converted.isNoAi()
						&& converted
								.isPersistenceRequired()
						&& converted.invulnerableTime
								== 31
						&& converted
								.isDrinkingPotion()
						&& PotionUtils.getPotion(
								converted
										.getMainHandItem())
								== Potions.SWIFTNESS
						&& converted.getTarget()
								== conversionTarget
						&& converted.getLastHurtByMob()
								== conversionTarget
						&& converted.getCurrentRaid()
								== transferRaid
						&& converted.getWave() == 5
						&& transferRaid
								.getTotalRaidersAlive()
								== 1
						&& converted.getVehicle() == mount
						&& converted.getPassengers()
								.contains(passenger),
				"Fresh literal Witch conversion lost drinking, NBT, target, raid or riding state");
		require(helper,
				CakeWorldWitchReplacement
						.replaceIfInCakeWorldBiome(
								level, baker) == null
						&& !baker.isRemoved(),
				"Witch source conversion touched a non-literal entity type");
		passenger.discard();
		if (converted != null) {
			transferRaid.removeFromRaid(
					converted, true);
			converted.discard();
		}
		mount.discard();
		conversionTarget.discard();

		Witch eventLiteral =
				EntityType.WITCH.create(level);
		require(helper, eventLiteral != null,
				"Could not create Bitter Baker entity-join source");
		eventLiteral.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		eventLiteral.setCustomName(
				new TextComponent(
						"Entity Join Bitter Baker"));
		eventLiteral.setNoAi(true);
		eventLiteral.setPersistenceRequired();
		eventLiteral.setItemSlot(
				EquipmentSlot.MAINHAND,
				PotionUtils.setPotion(
						new ItemStack(Items.POTION),
						Potions.HEALING));
		eventLiteral.setUsingItem(true);
		require(helper,
				level.addFreshEntity(eventLiteral),
				"Could not add literal Witch entity-join source");

		helper.runAfterDelay(4, () -> {
			List<BitterBaker> emitted =
					level.getEntitiesOfClass(
							BitterBaker.class,
							eventArea,
							candidate ->
									candidate
											.hasCustomName()
											&& "Entity Join Bitter Baker"
													.equals(candidate
															.getName()
															.getString()));
			BitterBaker eventBaker =
					emitted.size() == 1
							? emitted.get(0) : null;
			require(helper,
					eventLiteral.isRemoved()
							&& eventBaker != null
							&& eventBaker.isNoAi()
							&& eventBaker
									.isPersistenceRequired()
							&& PotionUtils.getPotion(
									eventBaker
											.getMainHandItem())
									== Potions.HEALING,
					"Actual deferred Witch entity-join source lost structure state: literalRemoved="
							+ eventLiteral.isRemoved()
							+ ", emitted="
							+ emitted.size());
			emitted.forEach(
					BitterBaker::discard);
			baker.discard();
			vanilla.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 240)
	public static void burntSugarTempestKeepsWitherProgressionAndSafeStorms(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(5, 4, 5));
		AABB localArea = new AABB(anchor)
				.inflate(24.0D);
		level.getEntitiesOfClass(
				WitherBoss.class, localArea)
				.forEach(WitherBoss::discard);
		level.getEntitiesOfClass(
				WitherSkull.class, localArea)
				.forEach(WitherSkull::discard);

		BurntSugarTempestProbe tempest =
				new BurntSugarTempestProbe(level);
		VanillaWitherProbe vanilla =
				new VanillaWitherProbe(level);
		tempest.setPos(anchor.getX() + 0.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		tempest.setNoAi(true);
		tempest.setPersistenceRequired();
		level.addFreshEntity(tempest);
		require(helper,
				tempest instanceof WitherBoss
						&& tempest.getType()
								== CakeWorldEntities
										.BURNT_SUGAR_TEMPEST
										.get()
						&& tempest.getType()
								.getCategory()
								== MobCategory.MONSTER
						&& close(tempest.getMaxHealth(),
								300.0D)
						&& close(tempest
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.6D)
						&& close(tempest
								.getAttributeValue(
										Attributes
												.FLYING_SPEED),
								0.6D)
						&& close(tempest
								.getAttributeValue(
										Attributes
												.FOLLOW_RANGE),
								40.0D)
						&& close(tempest
								.getAttributeValue(
										Attributes.ARMOR),
								4.0D)
						&& close(tempest.getDimensions(
								Pose.STANDING).width,
								0.9D)
						&& close(tempest.getDimensions(
								Pose.STANDING).height,
								3.5D)
						&& tempest.getType()
								.clientTrackingRange()
								== 10
						&& tempest.getType()
								.fireImmune()
						&& !tempest.getType()
								.isBlockDangerous(
										Blocks
												.WITHER_ROSE
												.defaultBlockState())
						&& tempest.getNavigation()
								instanceof FlyingPathNavigation
						&& tempest.getMoveControl()
								.getClass()
								== vanilla
										.getMoveControl()
										.getClass()
						&& tempest.experienceReward()
								== 50,
				"Burnt-Sugar Tempest lost the exact Wither body, attributes, movement, fire/rose immunity or XP");
		require(helper,
				tempest.goalSignatures().equals(
								vanilla
										.goalSignatures())
						&& tempest.targetGoalSignatures()
								.equals(vanilla
										.targetGoalSignatures())
						&& tempest.getMobType()
								== MobType.UNDEAD
						&& tempest.ambientSound()
								== SoundEvents
										.WITHER_AMBIENT
						&& tempest.hurtSound()
								== SoundEvents
										.WITHER_HURT
						&& tempest.deathSound()
								== SoundEvents
										.WITHER_DEATH
						&& tempest.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/burnt_sugar_tempest")),
				"Burnt-Sugar Tempest lost Wither goals, undead role, sounds or loot identity");

		tempest.makeInvulnerable();
		require(helper,
				tempest.getInvulnerableTicks()
								== 220
						&& close(tempest.getHealth(),
								100.0D),
				"Burnt-Sugar Tempest lost the exact Wither summoning charge");
		tempest.tickCount = 10;
		tempest.runServerAiStep();
		require(helper,
				tempest.getInvulnerableTicks()
								== 219
						&& close(tempest.getHealth(),
								110.0D),
				"Burnt-Sugar Tempest lost ten-tick charge healing or countdown");

		Difficulty originalDifficulty =
				level.getDifficulty();
		BlockPos protectedBlock =
				anchor.offset(2, 0, 0);
		ItemEntity protectedItem = null;
		try {
			level.getServer().setDifficulty(
					Difficulty.NORMAL, true);
			Pig protectedTarget =
					EntityType.PIG.create(level);
			require(helper, protectedTarget != null,
					"Could not create protected Tempest target");
			protectedTarget.setPos(
					tempest.getX() + 4.0D,
					tempest.getY(),
					tempest.getZ());
			protectedTarget.setNoAi(true);
			protectedTarget.setSecondsOnFire(5);
			protectedTarget.fallDistance = 12.0F;
			level.addFreshEntity(protectedTarget);
			LivingHurtEvent protectedHit =
					new LivingHurtEvent(
							protectedTarget,
							DamageSource
									.mobAttack(tempest),
							8.0F);
			BurntSugarTempestSafety
					.applyDamagePolicy(
							protectedHit,
							Difficulty.NORMAL);
			require(helper,
					protectedHit.isCanceled()
							&& close(protectedTarget
									.getHealth(),
									protectedTarget
											.getMaxHealth())
							&& !protectedTarget.isOnFire()
							&& close(protectedTarget
									.fallDistance,
									0.0D)
							&& protectedTarget
									.hasEffect(
											MobEffects
													.MOVEMENT_SLOWDOWN)
							&& protectedTarget
									.hasEffect(
											MobEffects
													.GLOWING)
							&& protectedTarget
									.hasEffect(
											MobEffects
													.SLOW_FALLING)
							&& protectedTarget
									.hasEffect(
											MobEffects
													.FIRE_RESISTANCE)
							&& protectedTarget
									.getEffect(
											MobEffects
													.DAMAGE_RESISTANCE)
									.getAmplifier()
									== 4,
					"Normal Tempest contact lost its harmless visible gust and rescue envelope");
			LivingHurtEvent hardHit =
					new LivingHurtEvent(
							protectedTarget,
							DamageSource
									.mobAttack(tempest),
							8.0F);
			BurntSugarTempestSafety
					.applyDamagePolicy(
							hardHit,
							Difficulty.HARD);
			require(helper,
					!hardHit.isCanceled(),
					"Hard Tempest contact was incorrectly softened");

			Explosion tempestExplosion =
					new Explosion(level, tempest,
							tempest.getX(),
							tempest.getY(),
							tempest.getZ(),
							7.0F);
			ExplosionEvent.Start safeExplosion =
					new ExplosionEvent.Start(
							level,
							tempestExplosion);
			BurntSugarTempestSafety
					.applyExplosionPolicy(
							safeExplosion,
							Difficulty.NORMAL);
			ExplosionEvent.Start hardExplosion =
					new ExplosionEvent.Start(
							level,
							tempestExplosion);
			BurntSugarTempestSafety
					.applyExplosionPolicy(
							hardExplosion,
							Difficulty.HARD);
			EntityMobGriefingEvent safeGrief =
					new EntityMobGriefingEvent(
							tempest);
			BurntSugarTempestSafety
					.applyGriefingPolicy(
							safeGrief,
							Difficulty.NORMAL);
			EntityMobGriefingEvent hardGrief =
					new EntityMobGriefingEvent(
							tempest);
			BurntSugarTempestSafety
					.applyGriefingPolicy(
							hardGrief,
							Difficulty.HARD);
			require(helper,
					safeExplosion.isCanceled()
							&& !hardExplosion
									.isCanceled()
							&& safeGrief
									.getResult()
									== Event.Result.DENY
							&& hardGrief
									.getResult()
									== Event.Result.DEFAULT,
					"Tempest explosion/grief safety did not stop below Hard and release on Hard");

			level.setBlock(protectedBlock,
					Blocks.BRICKS
							.defaultBlockState(),
					3);
			protectedItem = new ItemEntity(
					level,
					protectedBlock.getX() + 0.5D,
					protectedBlock.getY() + 0.5D,
					protectedBlock.getZ() + 0.5D,
					new ItemStack(
							Items.DIAMOND));
			level.addFreshEntity(protectedItem);
			tempest.setInvulnerableTicks(1);
			tempest.tickCount = 11;
			tempest.runServerAiStep();
			require(helper,
					tempest.getInvulnerableTicks()
								== 0
							&& level.getBlockState(
									protectedBlock)
									.is(Blocks.BRICKS)
							&& protectedItem.isAlive(),
					"Actual Normal charge-completion explosion destroyed a block or possession");

			WitherSkullProbe safeSkull =
					new WitherSkullProbe(level);
			safeSkull.setOwner(tempest);
			safeSkull.setPos(
					protectedTarget.getX() - 1.0D,
					protectedTarget.getEyeY(),
					protectedTarget.getZ());
			level.addFreshEntity(safeSkull);
			protectedTarget.setHealth(
					protectedTarget.getMaxHealth());
			protectedTarget.removeAllEffects();
			safeSkull.finishHit(
					protectedTarget);
			require(helper,
					close(protectedTarget.getHealth(),
							protectedTarget
									.getMaxHealth())
							&& !protectedTarget
									.hasEffect(
											MobEffects
													.WITHER)
							&& protectedTarget
									.hasEffect(
											MobEffects
													.SLOW_FALLING)
							&& safeSkull.isRemoved()
							&& level.getBlockState(
									protectedBlock)
									.is(Blocks.BRICKS)
							&& protectedItem.isAlive(),
					"Actual Normal owned skull caused health, Wither, explosion or possession damage: health="
							+ protectedTarget.getHealth()
							+ ", wither="
							+ protectedTarget.hasEffect(
									MobEffects.WITHER)
							+ ", slowFalling="
							+ protectedTarget.hasEffect(
									MobEffects.SLOW_FALLING)
							+ ", skullRemoved="
							+ safeSkull.isRemoved()
							+ ", block="
							+ level.getBlockState(
									protectedBlock)
							+ ", itemAlive="
							+ protectedItem.isAlive());

			level.getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(level);
			require(helper, hardTarget != null,
					"Could not create Hard Tempest target");
			hardTarget.setPos(
					tempest.getX() + 4.0D,
					tempest.getY(),
					tempest.getZ() + 2.0D);
			hardTarget.setNoAi(true);
			level.addFreshEntity(hardTarget);
			WitherSkullProbe hardSkull =
					new WitherSkullProbe(level);
			hardSkull.setOwner(tempest);
			hardSkull.hitEntity(hardTarget);
			require(helper,
					close(hardTarget.getHealth(),
							2.0D)
							&& hardTarget.hasEffect(
									MobEffects.WITHER)
							&& hardTarget.getEffect(
									MobEffects.WITHER)
									.getDuration()
									== 800,
					"Hard owned skull lost vanilla eight-damage and forty-second Wither peril");
			hardSkull.discard();
			hardTarget.discard();
			protectedTarget.discard();
		} finally {
			level.getServer().setDifficulty(
					originalDifficulty, true);
			level.setBlock(protectedBlock,
					Blocks.AIR.defaultBlockState(),
					3);
			if (protectedItem != null) {
				protectedItem.discard();
			}
		}

		tempest.setInvulnerableTicks(0);
		tempest.setHealth(150.0F);
		Arrow arrow = EntityType.ARROW.create(level);
		require(helper, arrow != null,
				"Could not create Tempest phase arrow");
		boolean powered = tempest.isPowered();
		boolean poweredArrowAccepted =
				tempest.hurt(
						DamageSource.arrow(
								arrow, null),
						5.0F);
		require(helper,
				powered
						&& !poweredArrowAccepted,
				"Powered Tempest lost half-health phase or arrow immunity");
		tempest.setHealth(151.0F);
		boolean unpowered = !tempest.isPowered();
		boolean unpoweredArrowAccepted =
				tempest.hurt(
						DamageSource.arrow(
								arrow, null),
						5.0F);
		float healthAfterArrow =
				tempest.getHealth();
		boolean drownAccepted =
				tempest.hurt(
						DamageSource.DROWN,
						5.0F);
		boolean witherAccepted =
				tempest.hurt(
						DamageSource
								.mobAttack(vanilla),
						5.0F);
		boolean effectAccepted =
				tempest.addEffect(
						new MobEffectInstance(
								MobEffects.POISON,
								100));
		boolean fallAccepted =
				tempest.causeFallDamage(
						20.0F, 1.0F,
						DamageSource.FALL);
		boolean dimensionChange =
				tempest.canChangeDimensions();
		vanilla.setInvulnerableTicks(0);
		vanilla.setHealth(151.0F);
		boolean vanillaArrowAccepted =
				vanilla.hurt(
						DamageSource.arrow(
								arrow, null),
						5.0F);
		float vanillaHealthAfterArrow =
				vanilla.getHealth();
		require(helper,
				unpowered
						&& unpoweredArrowAccepted
						&& vanillaArrowAccepted
						&& close(healthAfterArrow,
								vanillaHealthAfterArrow)
						&& close(tempest.getHealth(),
								healthAfterArrow)
						&& !drownAccepted
						&& !witherAccepted
						&& !effectAccepted
						&& !fallAccepted
						&& !dimensionChange,
				"Tempest lost Wither powered transition, damage/effect/fall immunity or dimension lock: unpowered="
						+ unpowered
						+ ", arrowAccepted="
						+ unpoweredArrowAccepted
						+ ", healthAfterArrow="
						+ healthAfterArrow
						+ ", finalHealth="
						+ tempest.getHealth()
						+ ", vanillaArrowAccepted="
						+ vanillaArrowAccepted
						+ ", vanillaHealthAfterArrow="
						+ vanillaHealthAfterArrow
						+ ", drownAccepted="
						+ drownAccepted
						+ ", witherAccepted="
						+ witherAccepted
						+ ", effectAccepted="
						+ effectAccepted
						+ ", fallAccepted="
						+ fallAccepted
						+ ", dimensionChange="
						+ dimensionChange);
		Boat forbiddenVehicle =
				EntityType.BOAT.create(level);
		require(helper,
				forbiddenVehicle != null
						&& !tempest.startRiding(
								forbiddenVehicle),
				"Tempest incorrectly gained vehicle riding");
		arrow.discard();
		forbiddenVehicle.discard();

		level.getEntitiesOfClass(ItemEntity.class,
				localArea,
				item -> item.getItem()
						.is(Items.NETHER_STAR))
				.forEach(ItemEntity::discard);
		tempest.dropBossLoot();
		List<ItemEntity> stars =
				level.getEntitiesOfClass(
						ItemEntity.class,
						localArea,
						item -> item.getItem()
								.is(Items.NETHER_STAR));
		CompoundTag starData = stars.size() == 1
				? stars.get(0).saveWithoutId(
						new CompoundTag())
				: new CompoundTag();
		require(helper,
				stars.size() == 1
						&& starData.getShort("Age")
								== -6000
						&& level.getRecipeManager()
								.getAllRecipesFor(
										net.minecraft.world.item.crafting.RecipeType
												.CRAFTING)
								.stream()
								.filter(recipe -> recipe
										.getId()
										.equals(new ResourceLocation(
												"minecraft",
												"beacon")))
								.anyMatch(recipe -> {
									for (net.minecraft.world.item.crafting.Ingredient ingredient :
											recipe
													.getIngredients()) {
										for (ItemStack stack :
												ingredient
														.getItems()) {
											if (stack.is(
													Items.NETHER_STAR)) {
												return true;
											}
										}
									}
									return false;
								}),
				"Tempest lost its exact extended-life Nether Star or vanilla Beacon recipe progression");
		stars.forEach(ItemEntity::discard);

		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = level.registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper, biome != null,
					"Missing Tempest ecology-audit biome "
							+ biomeId);
			for (MobSpawnSettings.SpawnerData spawn :
					biome.getMobSettings()
							.getMobs(
									MobCategory
											.MONSTER)
							.unwrap()) {
				require(helper,
						spawn.type
								!= CakeWorldEntities
										.BURNT_SUGAR_TEMPEST
										.get()
								&& spawn.type
										!= EntityType.WITHER,
						"Boss leaked into natural ecology in "
								+ biomeId);
			}
		}
		require(helper,
				CakeWorldItems
						.BURNT_SUGAR_TEMPEST_SPAWN_EGG
						.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.BURNT_SUGAR_TEMPEST
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.WITHER)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.BURNT_SUGAR_TEMPEST
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.WITHER)
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.BURNT_SUGAR_TEMPEST
												.get())
								== SoundEvents
										.PARROT_IMITATE_WITHER,
				"Tempest lost testing egg, exact Wither placement or Lorikeet mimic");
		ServerPlayer advancementPlayer =
				new ServerPlayer(level.getServer(), level,
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2066"),
								"CakeWorldTempestRoleTest"));
		VanillaRoleAdvancements
				.creditKilledWitherRole(
						advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:wither");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						anchor.offset(16, 0, 16),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for Wither summon/conversion");
		WitherBoss literal =
				EntityType.WITHER.create(level);
		Pig conversionTarget =
				EntityType.PIG.create(level);
		Chicken passenger =
				EntityType.CHICKEN.create(level);
		require(helper,
				literal != null
						&& conversionTarget != null
						&& passenger != null,
				"Could not create Tempest direct-conversion fixtures");
		conversionTarget.setPos(
				cakeWorldPos.getX() + 4.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		conversionTarget.setNoAi(true);
		level.addFreshEntity(conversionTarget);
		literal.setPos(
				cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literal.setCustomName(
				new TextComponent(
						"Burnt Crown"));
		literal.setNoAi(true);
		literal.setPersistenceRequired();
		literal.setHealth(123.0F);
		literal.invulnerableTime = 29;
		literal.setInvulnerableTicks(47);
		literal.setTarget(conversionTarget);
		literal.setLastHurtByMob(
				conversionTarget);
		level.addFreshEntity(literal);
		literal.setAlternativeTarget(
				0, conversionTarget.getId());
		literal.setAlternativeTarget(
				1, conversionTarget.getId());
		literal.setAlternativeTarget(
				2, conversionTarget.getId());
		passenger.startRiding(literal, true);
		BurntSugarTempest converted =
				CakeWorldWitherReplacement
						.replaceIfInCakeWorldBiome(
								level, literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& close(converted.getHealth(),
								123.0D)
						&& "Burnt Crown".equals(
								converted.getName()
										.getString())
						&& converted.isNoAi()
						&& converted
								.isPersistenceRequired()
						&& converted.invulnerableTime
								== 29
						&& converted
								.getInvulnerableTicks()
								== 47
						&& converted.getTarget()
								== conversionTarget
						&& converted.getLastHurtByMob()
								== conversionTarget
						&& converted
								.getAlternativeTarget(0)
								== conversionTarget
										.getId()
						&& converted
								.getAlternativeTarget(1)
								== conversionTarget
										.getId()
						&& converted
								.getAlternativeTarget(2)
								== conversionTarget
										.getId()
						&& converted.getPassengers()
								.contains(passenger),
				"Fresh literal Wither conversion lost charge, NBT, target heads or passenger state");
		require(helper,
				CakeWorldWitherReplacement
						.replaceIfInCakeWorldBiome(
								level, tempest)
						== null
						&& !tempest.isRemoved(),
				"Wither source conversion touched a non-literal entity type");
		passenger.discard();
		converted.discard();
		conversionTarget.discard();

		BlockPos patternBase =
				cakeWorldPos.offset(8, 0, 0);
		for (int x = -2; x <= 2; x++) {
			for (int y = 0; y <= 3; y++) {
				for (int z = -1; z <= 1; z++) {
					level.setBlock(
							patternBase.offset(
									x, y, z),
							Blocks.AIR
									.defaultBlockState(),
							3);
				}
			}
		}
		level.setBlock(patternBase,
				Blocks.SOUL_SAND
						.defaultBlockState(), 3);
		for (int x = -1; x <= 1; x++) {
			level.setBlock(
					patternBase.offset(x, 1, 0),
					Blocks.SOUL_SAND
							.defaultBlockState(),
					3);
			level.setBlock(
					patternBase.offset(x, 2, 0),
					Blocks.WITHER_SKELETON_SKULL
							.defaultBlockState(),
					3);
		}
		BlockPos middleSkull =
				patternBase.offset(0, 2, 0);
		BlockEntity skullEntity =
				level.getBlockEntity(middleSkull);
		require(helper,
				skullEntity
						instanceof SkullBlockEntity,
				"Actual Wither pattern did not create a skull block entity");
		WitherSkullBlock.checkSpawn(
				level, middleSkull,
				(SkullBlockEntity)skullEntity);
		AABB patternArea =
				new AABB(patternBase)
						.inflate(8.0D);
		List<WitherBoss> literalSummons =
				level.getEntitiesOfClass(
						WitherBoss.class,
						patternArea,
						boss -> boss.getType()
								== EntityType.WITHER);
		require(helper,
				literalSummons.size() == 1
						&& literalSummons.get(0)
								.getInvulnerableTicks()
								== 220
						&& close(literalSummons
								.get(0).getHealth(),
								100.0D)
						&& level.getBlockState(
								patternBase)
								.isAir(),
				"Vanilla Soul Sand/skull pattern did not remain the authoritative literal Wither source");
		CriteriaTriggers.SUMMONED_ENTITY
				.trigger(advancementPlayer,
						literalSummons.get(0));
		requireCriterion(helper, advancementPlayer,
				"minecraft:nether/summon_wither",
				"summoned");

		helper.runAfterDelay(5, () -> {
			List<BurntSugarTempest> emitted =
					level.getEntitiesOfClass(
							BurntSugarTempest.class,
							patternArea);
			BurntSugarTempest eventTempest =
					emitted.size() == 1
							? emitted.get(0)
							: null;
			require(helper,
					literalSummons.get(0)
								.isRemoved()
							&& eventTempest != null
							&& eventTempest
									.getInvulnerableTicks()
									> 210
							&& eventTempest
									.getInvulnerableTicks()
									< 220
							&& close(eventTempest
									.getHealth(),
									100.0D),
					"Actual summoned literal Wither did not defer-convert with its boss charge intact: emitted="
							+ emitted.size()
							+ ", invul="
							+ (eventTempest == null
									? -1
									: eventTempest
											.getInvulnerableTicks()));
			emitted.forEach(
					BurntSugarTempest::discard);

			Difficulty beforePeaceful =
					level.getDifficulty();
			level.getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			BurntSugarTempestProbe peaceful =
					new BurntSugarTempestProbe(level);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Tempest lost vanilla Monster removal");
			level.getServer().setDifficulty(
					beforePeaceful, true);
			tempest.discard();
			vanilla.discard();
			helper.succeed();
		});
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void burntCandyKnightsKeepFortressSkullsAndDifficultySafety(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		BlockPos anchor = helper.absolutePos(
				new BlockPos(5, 4, 5));
		AABB localArea = new AABB(anchor)
				.inflate(32.0D);
		level.getEntitiesOfClass(
				WitherSkeleton.class, localArea)
				.forEach(WitherSkeleton::discard);
		level.getEntitiesOfClass(
				ItemEntity.class, localArea,
				item -> item.getItem().is(
						Items.WITHER_SKELETON_SKULL))
				.forEach(ItemEntity::discard);

		BurntCandyKnightProbe knight =
				new BurntCandyKnightProbe(level);
		VanillaWitherSkeletonProbe vanilla =
				new VanillaWitherSkeletonProbe(level);
		knight.setPos(anchor.getX() + 0.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		vanilla.setPos(anchor.getX() + 3.5D,
				anchor.getY(),
				anchor.getZ() + 0.5D);
		knight.finalizeSpawn(
				level,
				level.getCurrentDifficultyAt(anchor),
				MobSpawnType.STRUCTURE, null, null);
		vanilla.finalizeSpawn(
				level,
				level.getCurrentDifficultyAt(anchor),
				MobSpawnType.STRUCTURE, null, null);
		knight.setNoAi(true);
		vanilla.setNoAi(true);
		level.addFreshEntity(knight);
		level.addFreshEntity(vanilla);

		require(helper,
				knight instanceof WitherSkeleton
						&& knight instanceof AbstractSkeleton
						&& knight.getType()
								== CakeWorldEntities
										.BURNT_CANDY_KNIGHT
										.get()
						&& knight.getType()
								.getCategory()
								== MobCategory.MONSTER
						&& close(knight.getMaxHealth(),
								20.0D)
						&& close(knight
								.getAttributeValue(
										Attributes
												.MOVEMENT_SPEED),
								0.25D)
						&& close(knight
								.getAttributeValue(
										Attributes
												.ATTACK_DAMAGE),
								4.0D)
						&& close(knight
								.getAttributeBaseValue(
										Attributes
												.FOLLOW_RANGE),
								16.0D)
						&& close(knight
								.getAttributeValue(
										Attributes.ARMOR),
								0.0D)
						&& close(knight.getDimensions(
								Pose.STANDING).width,
								0.7D)
						&& close(knight.getDimensions(
								Pose.STANDING).height,
								2.4D)
						&& close(knight
								.standingEyeHeight(),
								2.1D)
						&& knight.getType()
								.clientTrackingRange()
								== 8
						&& knight.getType()
								.fireImmune()
						&& !knight.getType()
								.isBlockDangerous(
										Blocks
												.WITHER_ROSE
												.defaultBlockState())
						&& knight.getNavigation()
								instanceof GroundPathNavigation
						&& close(knight
								.getPathfindingMalus(
										net.minecraft.world.level.pathfinder.BlockPathTypes
												.LAVA),
								8.0D)
						&& knight.getMobType()
								== MobType.UNDEAD
						&& knight.baseExperienceReward()
								== 5,
				"Burnt-Candy Knight lost exact Wither-Skeleton type, body, attributes, movement, immunity or XP:"
						+ " type="
						+ Registry.ENTITY_TYPE.getKey(
								knight.getType())
						+ ", category="
						+ knight.getType().getCategory()
						+ ", maxHealth="
						+ knight.getMaxHealth()
						+ ", speed="
						+ knight.getAttributeValue(
								Attributes.MOVEMENT_SPEED)
						+ ", attack="
						+ knight.getAttributeValue(
								Attributes.ATTACK_DAMAGE)
						+ ", follow="
						+ knight.getAttributeValue(
								Attributes.FOLLOW_RANGE)
						+ ", followBase="
						+ knight.getAttributeBaseValue(
								Attributes.FOLLOW_RANGE)
						+ ", armor="
						+ knight.getAttributeValue(
								Attributes.ARMOR)
						+ ", dimensions="
						+ knight.getDimensions(
								Pose.STANDING)
						+ ", eye="
						+ knight.standingEyeHeight()
						+ ", tracking="
						+ knight.getType()
								.clientTrackingRange()
						+ ", fireImmune="
						+ knight.getType().fireImmune()
						+ ", roseDanger="
						+ knight.getType()
								.isBlockDangerous(
										Blocks.WITHER_ROSE
												.defaultBlockState())
						+ ", navigation="
						+ knight.getNavigation()
								.getClass()
								.getSimpleName()
						+ ", lavaMalus="
						+ knight.getPathfindingMalus(
								net.minecraft.world.level.pathfinder.BlockPathTypes
										.LAVA)
						+ ", mobType="
						+ knight.getMobType()
						+ ", baseXp="
						+ knight.baseExperienceReward());
		require(helper,
				knight.goalSignatures().equals(
								vanilla.goalSignatures())
						&& knight.targetGoalSignatures()
								.equals(vanilla
										.targetGoalSignatures())
						&& knight.countTargetGoalsNamed(
								"NearestAttackableTargetGoal")
								== 4
						&& knight.getMainHandItem()
								.is(Items.STONE_SWORD)
						&& EnchantmentHelper
								.getEnchantments(
										knight
												.getMainHandItem())
								.isEmpty()
						&& knight.ambientSound()
								== SoundEvents
										.WITHER_SKELETON_AMBIENT
						&& knight.hurtSound()
								== SoundEvents
										.WITHER_SKELETON_HURT
						&& knight.deathSound()
								== SoundEvents
										.WITHER_SKELETON_DEATH
						&& knight.stepSound()
								== SoundEvents
										.WITHER_SKELETON_STEP
						&& knight.getLootTableId()
								.equals(new ResourceLocation(
										CakeWorld.MODID,
										"entities/burnt_candy_knight")),
				"Burnt-Candy Knight lost exact goals, Piglin target, sword, sounds or loot identity");

		knight.setItemSlot(
				EquipmentSlot.MAINHAND,
				new ItemStack(Items.BOW));
		vanilla.setItemSlot(
				EquipmentSlot.MAINHAND,
				new ItemStack(Items.BOW));
		AbstractArrow burningArrow =
				knight.makeArrow();
		require(helper,
				knight.goalSignatures().equals(
								vanilla.goalSignatures())
						&& burningArrow.isOnFire()
						&& burningArrow.getOwner()
								== knight,
				"Burnt-Candy Knight lost exact bow-goal reassessment or flaming-arrow ownership");
		burningArrow.discard();
		knight.setItemSlot(
				EquipmentSlot.MAINHAND,
				new ItemStack(Items.STONE_SWORD));

		require(helper,
				!knight.canBeAffected(
						new MobEffectInstance(
								MobEffects.WITHER,
								200))
						&& knight.canBeAffected(
								new MobEffectInstance(
										MobEffects.MOVEMENT_SPEED,
										200)),
				"Burnt-Candy Knight lost exact Wither-only effect immunity");

		Difficulty originalDifficulty =
				level.getDifficulty();
		try {
			for (Difficulty safeDifficulty :
					List.of(Difficulty.EASY,
							Difficulty.NORMAL)) {
				level.getServer().setDifficulty(
						safeDifficulty, true);
				Pig safeTarget =
						EntityType.PIG.create(level);
				require(helper,
						safeTarget != null,
						"Could not create safe Knight target");
				safeTarget.setPos(
						knight.getX() + 4.0D,
						knight.getY(),
						knight.getZ()
								+ safeDifficulty
										.getId());
				safeTarget.setNoAi(true);
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 9.0F;
				level.addFreshEntity(safeTarget);
				boolean accepted =
						knight.doHurtTarget(
								safeTarget);
				require(helper,
						!accepted
								&& close(safeTarget
										.getHealth(),
										safeTarget
												.getMaxHealth())
								&& !safeTarget
										.hasEffect(
												MobEffects
														.WITHER)
								&& !safeTarget.isOnFire()
								&& close(safeTarget
										.fallDistance,
										0.0D)
								&& safeTarget
										.hasEffect(
												MobEffects
														.MOVEMENT_SLOWDOWN)
								&& safeTarget
										.hasEffect(
												MobEffects
														.GLOWING)
								&& safeTarget
										.hasEffect(
												MobEffects
														.SLOW_FALLING)
								&& safeTarget
										.hasEffect(
												MobEffects
														.FIRE_RESISTANCE)
								&& safeTarget
										.getEffect(
												MobEffects
														.DAMAGE_RESISTANCE)
										.getAmplifier()
										== 4,
						safeDifficulty
								+ " Knight melee caused health, Wither, fire, fall or follow-on damage");

				AbstractArrow safeArrow =
						knight.makeArrow();
				safeTarget.removeAllEffects();
				safeTarget.setSecondsOnFire(5);
				safeTarget.fallDistance = 7.0F;
				LivingAttackEvent arrowHit =
						new LivingAttackEvent(
								safeTarget,
								DamageSource.arrow(
										safeArrow,
										knight),
								4.0F);
				BurntCandyKnightSafety
						.applyAttackPolicy(
								arrowHit,
								safeDifficulty);
				require(helper,
						arrowHit.isCanceled()
								&& BurntCandyKnightSafety
										.isOwnedByKnight(
												knight,
												safeArrow)
								&& !safeTarget.isOnFire()
								&& close(safeTarget
										.fallDistance,
										0.0D)
								&& safeTarget
										.hasEffect(
												MobEffects
														.SLOW_FALLING),
						safeDifficulty
								+ " owned flaming arrow escaped Knight safety");
				safeArrow.discard();
				safeTarget.discard();
			}

			level.getServer().setDifficulty(
					Difficulty.HARD, true);
			Pig hardTarget =
					EntityType.PIG.create(level);
			require(helper, hardTarget != null,
					"Could not create Hard Knight target");
			hardTarget.setPos(knight.getX() + 4.0D,
					knight.getY(),
					knight.getZ() + 6.0D);
			hardTarget.setNoAi(true);
			level.addFreshEntity(hardTarget);
			boolean hardAccepted =
					knight.doHurtTarget(hardTarget);
			require(helper,
					hardAccepted
							&& close(hardTarget
									.getHealth(),
									6.0D)
							&& hardTarget.hasEffect(
									MobEffects.WITHER)
							&& hardTarget.getEffect(
									MobEffects.WITHER)
									.getDuration()
									== 200,
					"Hard Knight lost exact four-damage and ten-second Wither peril");
			hardTarget.discard();

			level.getServer().setDifficulty(
					Difficulty.PEACEFUL, true);
			BurntCandyKnightProbe peaceful =
					new BurntCandyKnightProbe(level);
			peaceful.checkDespawn();
			require(helper,
					peaceful.isRemoved()
							&& peaceful
									.despawnsInPeaceful(),
					"Peaceful Knight lost vanilla Monster removal");
		} finally {
			level.getServer().setDifficulty(
					originalDifficulty, true);
		}

		Creeper charged =
				EntityType.CREEPER.create(level);
		LightningBolt lightning =
				EntityType.LIGHTNING_BOLT.create(level);
		require(helper,
				charged != null
						&& lightning != null,
				"Could not create charged-Creeper skull fixtures");
		charged.setPos(knight.getX() + 2.0D,
				knight.getY(), knight.getZ());
		lightning.setPos(charged.getX(),
				charged.getY(), charged.getZ());
		level.addFreshEntity(charged);
		level.addFreshEntity(lightning);
		charged.thunderHit(level, lightning);
		require(helper, charged.canDropMobsSkull(),
				"Lightning did not arm the charged-Creeper skull route");
		knight.dropChargedCreeperLoot(
				DamageSource.mobAttack(charged));
		List<ItemEntity> skulls =
				level.getEntitiesOfClass(
						ItemEntity.class,
						localArea,
						item -> item.getItem()
								.is(Items
										.WITHER_SKELETON_SKULL));
		require(helper,
				skulls.size() == 1
						&& skulls.get(0).getItem()
								.getCount() == 1
						&& !charged
								.canDropMobsSkull(),
				"Knight lost exact charged-Creeper Wither-Skeleton-Skull drop or one-skull gate");

		ServerPlayer advancementPlayer =
				new ServerPlayer(level.getServer(), level,
						new GameProfile(
								UUID.fromString(
										"1978feed-feed-4bad-babe-1978feed2067"),
								"CakeWorldKnightRoleTest"));
		advancementPlayer.connection =
				new ServerGamePacketListenerImpl(
						level.getServer(),
						new Connection(
								PacketFlow.CLIENTBOUND),
						advancementPlayer);
		ItemStack progressionSkull =
				skulls.get(0).getItem().copy();
		advancementPlayer.getInventory()
				.add(progressionSkull.copy());
		CriteriaTriggers.INVENTORY_CHANGED
				.trigger(advancementPlayer,
						advancementPlayer
								.getInventory(),
						progressionSkull);
		requireCriterion(helper, advancementPlayer,
				"minecraft:nether/get_wither_skull",
				"wither_skull");
		VanillaRoleAdvancements
				.creditKilledWitherSkeletonRole(
						advancementPlayer);
		requireCriterion(helper, advancementPlayer,
				"minecraft:adventure/kill_all_mobs",
				"minecraft:wither_skeleton");
		skulls.forEach(ItemEntity::discard);
		charged.discard();
		lightning.discard();

		MobSpawnSettings.SpawnerData fortressSpawn =
				NetherFortressFeature.FORTRESS_ENEMIES
						.unwrap().stream()
						.filter(spawn -> spawn.type
								== EntityType
										.WITHER_SKELETON)
						.findFirst().orElse(null);
		require(helper,
				fortressSpawn != null
						&& fortressSpawn
								.getWeight().asInt()
								== 8
						&& fortressSpawn.minCount
								== 5
						&& fortressSpawn.maxCount
								== 5
						&& NetherFortressFeature
								.FORTRESS_ENEMIES
								.unwrap().stream()
								.noneMatch(spawn ->
										spawn.type
												== CakeWorldEntities
														.BURNT_CANDY_KNIGHT
														.get()),
				"Knight replaced the authoritative literal Fortress 8/5-5 source instead of converting it");
		for (ResourceLocation biomeId : List.of(
				CakeWorldBiomes.CANDY_PLAINS.getId(),
				CakeWorldBiomes.COOKIE_FOREST.getId(),
				CakeWorldBiomes.MARSHMALLOW_PEAKS.getId(),
				CakeWorldBiomes.SODA_OCEAN.getId(),
				CakeWorldBiomes.FUDGE_WASTES.getId(),
				CakeWorldBiomes.MERINGUE_ISLANDS.getId())) {
			Biome biome = level.registryAccess()
					.registryOrThrow(
							Registry.BIOME_REGISTRY)
					.get(biomeId);
			require(helper,
					biome != null
							&& biome.getMobSettings()
									.getMobs(
											MobCategory
													.MONSTER)
									.unwrap().stream()
									.noneMatch(spawn ->
											spawn.type
													== EntityType
															.WITHER_SKELETON
													|| spawn.type
															== CakeWorldEntities
																	.BURNT_CANDY_KNIGHT
																	.get()),
					"Knight leaked into open-biome ecology in "
							+ biomeId);
		}
		require(helper,
				CakeWorldItems
						.BURNT_CANDY_KNIGHT_SPAWN_EGG
						.isPresent()
						&& SpawnPlacements
								.getPlacementType(
										CakeWorldEntities
												.BURNT_CANDY_KNIGHT
												.get())
								== SpawnPlacements
										.getPlacementType(
												EntityType
														.WITHER_SKELETON)
						&& SpawnPlacements
								.getHeightmapType(
										CakeWorldEntities
												.BURNT_CANDY_KNIGHT
												.get())
								== SpawnPlacements
										.getHeightmapType(
												EntityType
														.WITHER_SKELETON)
						&& CakeWorldEntities
								.BURNT_CANDY_KNIGHT
								.get().is(
										net.minecraft.tags.EntityTypeTags
												.SKELETONS)
						&& LollipopLorikeet
								.getCakeWorldImitatedSound(
										CakeWorldEntities
												.BURNT_CANDY_KNIGHT
												.get())
								== SoundEvents
										.PARROT_IMITATE_WITHER_SKELETON,
				"Knight lost testing egg, exact placement, skeleton role tag or Lorikeet mimic");

		BlockPos cakeWorldPos =
				findCakeWorldBiomePosition(helper,
						anchor.offset(16, 0, 16),
						256);
		require(helper, cakeWorldPos != null,
				"Could not locate CakeWorld terrain for Fortress Knight conversion");
		WitherSkeleton literal =
				EntityType.WITHER_SKELETON
						.create(level);
		Pig conversionTarget =
				EntityType.PIG.create(level);
		Boat vehicle =
				EntityType.BOAT.create(level);
		Chicken passenger =
				EntityType.CHICKEN.create(level);
		require(helper,
				literal != null
						&& conversionTarget != null
						&& vehicle != null
						&& passenger != null,
				"Could not create Knight direct-conversion fixtures");
		literal.setPos(cakeWorldPos.getX() + 0.5D,
				cakeWorldPos.getY(),
				cakeWorldPos.getZ() + 0.5D);
		literal.finalizeSpawn(
				level,
				level.getCurrentDifficultyAt(
						cakeWorldPos),
				MobSpawnType.STRUCTURE, null, null);
		literal.setCustomName(
				new TextComponent(
						"Charred Gatekeeper"));
		literal.setNoAi(true);
		literal.setPersistenceRequired();
		literal.setHealth(13.0F);
		literal.invulnerableTime = 19;
		conversionTarget.setPos(
				literal.getX() + 4.0D,
				literal.getY(), literal.getZ());
		conversionTarget.setNoAi(true);
		vehicle.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		passenger.setPos(literal.getX(),
				literal.getY(), literal.getZ());
		level.addFreshEntity(conversionTarget);
		level.addFreshEntity(vehicle);
		level.addFreshEntity(literal);
		level.addFreshEntity(passenger);
		literal.setTarget(conversionTarget);
		literal.setLastHurtByMob(
				conversionTarget);
		literal.startRiding(vehicle, true);
		passenger.startRiding(literal, true);
		BurntCandyKnight converted =
				CakeWorldWitherSkeletonReplacement
						.replaceIfInCakeWorldBiome(
								level, literal);
		require(helper,
				converted != null
						&& literal.isRemoved()
						&& close(converted.getHealth(),
								13.0D)
						&& "Charred Gatekeeper".equals(
								converted.getName()
										.getString())
						&& converted.isNoAi()
						&& converted
								.isPersistenceRequired()
						&& converted.invulnerableTime
								== 19
						&& converted.getMainHandItem()
								.is(Items.STONE_SWORD)
						&& close(converted
								.getAttributeValue(
										Attributes
												.ATTACK_DAMAGE),
								4.0D)
						&& converted.getTarget()
								== conversionTarget
						&& converted.getLastHurtByMob()
								== conversionTarget
						&& converted.getVehicle()
								== vehicle
						&& converted.getPassengers()
								.contains(passenger),
				"Fresh literal Fortress conversion lost NBT, equipment, target, vehicle or passenger state");
		require(helper,
				CakeWorldWitherSkeletonReplacement
						.replaceIfInCakeWorldBiome(
								level, converted)
						== null
						&& !converted.isRemoved(),
				"Wither-Skeleton source conversion touched a non-literal entity type");
		passenger.discard();
		vehicle.discard();
		conversionTarget.discard();
		converted.discard();

		BlockPos eventPos =
				findCakeWorldBiomePosition(helper,
						cakeWorldPos.offset(
								12, 0, 0),
						64);
		require(helper, eventPos != null,
				"Could not locate CakeWorld terrain for deferred Fortress Knight conversion");
		WitherSkeleton eventLiteral =
				EntityType.WITHER_SKELETON
						.create(level);
		require(helper, eventLiteral != null,
				"Could not create deferred Fortress Knight fixture");
		eventLiteral.setPos(
				eventPos.getX() + 0.5D,
				eventPos.getY(),
				eventPos.getZ() + 0.5D);
		eventLiteral.finalizeSpawn(
				level,
				level.getCurrentDifficultyAt(
						eventPos),
				MobSpawnType.STRUCTURE,
				null, null);
		eventLiteral.setCustomName(
				new TextComponent(
						"Deferred Knight"));
		eventLiteral.setNoAi(true);
		level.addFreshEntity(eventLiteral);
		AABB eventArea =
				new AABB(eventPos).inflate(3.0D);
		helper.runAfterDelay(5, () -> {
			List<BurntCandyKnight> emitted =
					level.getEntitiesOfClass(
							BurntCandyKnight.class,
							eventArea);
			require(helper,
					eventLiteral.isRemoved()
							&& emitted.size() == 1
							&& emitted.get(0)
									.isNoAi()
							&& emitted.get(0)
									.getMainHandItem()
									.is(Items.STONE_SWORD)
							&& "Deferred Knight".equals(
									emitted.get(0)
											.getName()
											.getString()),
					"Fresh literal Fortress entity did not defer-convert with finalized Knight state");
			emitted.forEach(
					BurntCandyKnight::discard);
			knight.discard();
			vanilla.discard();
			helper.succeed();
		});
	}

	private static class BurntCandyKnightProbe
			extends BurntCandyKnight {
		private BurntCandyKnightProbe(Level level) {
			super(CakeWorldEntities
					.BURNT_CANDY_KNIGHT
					.get(), level);
		}

		private int baseExperienceReward() {
			return xpReward;
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(
					Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				stepSound() {
			return getStepSound();
		}

		private AbstractArrow makeArrow() {
			return getArrow(
					new ItemStack(Items.ARROW),
					1.0F);
		}

		private void dropChargedCreeperLoot(
				DamageSource source) {
			dropCustomDeathLoot(source, 0, true);
		}

		private List<String> goalSignatures() {
			return goalSelector.getAvailableGoals()
					.stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private List<String>
				targetGoalSignatures() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private int countTargetGoalsNamed(
				String name) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}
	}

	private static final class VanillaWitherSkeletonProbe
			extends WitherSkeleton {
		private VanillaWitherSkeletonProbe(
				Level level) {
			super(EntityType.WITHER_SKELETON,
					level);
		}

		private List<String> goalSignatures() {
			return goalSelector.getAvailableGoals()
					.stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private List<String>
				targetGoalSignatures() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}
	}

	private static class BurntSugarTempestProbe
			extends BurntSugarTempest {
		private BurntSugarTempestProbe(Level level) {
			super(CakeWorldEntities
					.BURNT_SUGAR_TEMPEST
					.get(), level);
		}

		private int experienceReward() {
			return getExperienceReward(null);
		}

		private void runServerAiStep() {
			customServerAiStep();
		}

		private void dropBossLoot() {
			dropCustomDeathLoot(
					DamageSource.GENERIC,
					0, true);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private List<String> goalSignatures() {
			return goalSelector.getAvailableGoals()
					.stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private List<String>
				targetGoalSignatures() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}
	}

	private static final class VanillaWitherProbe
			extends WitherBoss {
		private VanillaWitherProbe(Level level) {
			super(EntityType.WITHER, level);
		}

		private List<String> goalSignatures() {
			return goalSelector.getAvailableGoals()
					.stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private List<String>
				targetGoalSignatures() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}
	}

	private static final class WitherSkullProbe
			extends WitherSkull {
		private WitherSkullProbe(Level level) {
			super(EntityType.WITHER_SKULL,
					level);
		}

		private void hitEntity(
				Entity target) {
			onHitEntity(
					new EntityHitResult(target));
		}

		private void finishHit(
				Entity target) {
			onHit(new EntityHitResult(target));
		}
	}

	private static final class BitterBakerProbe
			extends BitterBaker {
		private BitterBakerProbe(Level level) {
			super(CakeWorldEntities.BITTER_BAKER
					.get(), level);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void runAiStep() {
			aiStep();
		}

		private int experienceReward() {
			return getExperienceReward(null);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(
					Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private float magicDamageAfterAbsorb(
				DamageSource source, float damage) {
			return getDamageAfterMagicAbsorb(
					source, damage);
		}

		private List<String> goalSignatures() {
			return goalSelector.getAvailableGoals()
					.stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private List<String>
				targetGoalSignatures() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private int countTargetGoalsNamed(
				String name) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}
	}

	private static final class VanillaWitchProbe
			extends Witch {
		private VanillaWitchProbe(Level level) {
			super(EntityType.WITCH, level);
		}

		private int experienceReward() {
			return getExperienceReward(null);
		}

		private float magicDamageAfterAbsorb(
				DamageSource source, float damage) {
			return getDamageAfterMagicAbsorb(
					source, damage);
		}

		private List<String> goalSignatures() {
			return goalSelector.getAvailableGoals()
					.stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}

		private List<String>
				targetGoalSignatures() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(wrapped ->
							wrapped.getPriority() + ":"
									+ wrapped.getGoal()
											.getClass()
											.getSimpleName())
					.sorted().toList();
		}
	}

	private static final class TravellingConfectionerProbe
			extends TravellingConfectioner {
		private TravellingConfectionerProbe(
				Level level) {
			super(CakeWorldEntities
					.TRAVELLING_CONFECTIONER
					.get(), level);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int experienceReward() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(
					Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				drinkingSound(ItemStack stack) {
			return getDrinkingSound(stack);
		}

		private net.minecraft.sounds.SoundEvent
				tradeUpdatedSound(boolean accepted) {
			return getTradeUpdatedSound(accepted);
		}

		private int goalCount() {
			return goalSelector.getAvailableGoals()
					.size();
		}

		private int goalCount(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int targetGoalCount() {
			return targetSelector
					.getAvailableGoals().size();
		}

		private ItemStack startOnlyUsableItemGoal() {
			List<net.minecraft.world.entity.ai.goal.Goal>
					usable = goalSelector
							.getAvailableGoals()
							.stream()
							.map(WrappedGoal::getGoal)
							.filter(UseItemGoal.class
									::isInstance)
							.filter(net.minecraft.world.entity.ai.goal.Goal
									::canUse)
							.toList();
			if (usable.size() != 1) {
				return ItemStack.EMPTY;
			}
			net.minecraft.world.entity.ai.goal.Goal goal =
					usable.get(0);
			goal.start();
			ItemStack selected =
					getMainHandItem().copy();
			goal.stop();
			return selected;
		}
	}

	private static final class RollingPinRaiderProbe
			extends RollingPinRaider {
		private RollingPinRaiderProbe(Level level) {
			super(CakeWorldEntities.ROLLING_PIN_RAIDER
					.get(), level);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void setTestTickCount(int ticks) {
			tickCount = ticks;
		}

		private void runVillagerHostileRepair() {
			repairVillagerHostileAwareness();
		}

		private boolean startJohnnyTargetGoal() {
			for (WrappedGoal wrapped :
					targetSelector.getAvailableGoals()) {
				if ("VindicatorJohnnyAttackGoal"
						.equals(wrapped.getGoal()
								.getClass()
								.getSimpleName())
						&& wrapped.getGoal().canUse()) {
					wrapped.getGoal().start();
					return true;
				}
			}
			return false;
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(
				String name) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}
	}

	private static final class LollipopLorikeetProbe
			extends LollipopLorikeet {
		private LollipopLorikeetProbe(Level level) {
			super(CakeWorldEntities.LOLLIPOP_LORIKEET.get(),
					level);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void setTestTickCount(int ticks) {
			tickCount = ticks;
		}
	}

	private static final class WaferWraithProbe
			extends WaferWraith {
		private WaferWraithProbe(Level level) {
			super(CakeWorldEntities.WAFER_WRAITH.get(),
					level);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}
	}

	private static final class TrufflePigProbe
			extends TrufflePig {
		private TrufflePigProbe(Level level) {
			super(CakeWorldEntities.TRUFFLE_PIG.get(),
					level);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}
	}

	private static final class FudgeFolkProbe
			extends FudgeFolk {
		private FudgeFolkProbe(Level level) {
			super(CakeWorldEntities.FUDGE_FOLK.get(),
					level);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private boolean canHuntRole() {
			return canHunt();
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private void runServerAiStep() {
			customServerAiStep();
		}

		private void runFamilyInteractionRepair() {
			repairCakeWorldFamilyInteraction();
		}
	}

	private static final class FudgeBruteProbe
			extends FudgeBrute {
		private FudgeBruteProbe(Level level) {
			super(CakeWorldEntities.FUDGE_BRUTE.get(),
					level);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private boolean canHuntRole() {
			return canHunt();
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private InteractionResult runMobInteract(
				Player player, InteractionHand hand) {
			return mobInteract(player, hand);
		}

		private void runServerAiStep() {
			customServerAiStep();
		}

		private void runFamilyInteractionRepair() {
			repairCakeWorldFamilyInteraction();
		}
	}

	private static final class BiscuitBanditProbe
			extends BiscuitBandit {
		private BiscuitBanditProbe(Level level) {
			super(CakeWorldEntities.BISCUIT_BANDIT.get(),
					level);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private boolean isPatrollingRole() {
			return isPatrolling();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void setTestTickCount(int ticks) {
			tickCount = ticks;
		}

		private void runVillagerHostileRepair() {
			repairVillagerHostileAwareness();
		}

		private boolean checksVillagerAlertRange(
				LivingEntity entity) {
			return super.isWithinVillagerAlertRange(
					entity);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}
	}

	private static final class VanillaIceBearProbe
			extends VanillaIceBear {
		private net.minecraft.sounds.SoundEvent lastSound;

		private VanillaIceBearProbe(Level level) {
			super(CakeWorldEntities.VANILLA_ICE_BEAR.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private float waterSlowDown() {
			return getWaterSlowDown();
		}

		private void runWarningSound() {
			playWarningSound();
		}

		private void runStepSound() {
			playStepSound(BlockPos.ZERO,
					Blocks.SNOW_BLOCK.defaultBlockState());
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void setTestTickCount(int ticks) {
			tickCount = ticks;
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private boolean startTargetGoalNamed(String name) {
			for (WrappedGoal wrapped :
					targetSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())
						&& wrapped.canUse()) {
					wrapped.start();
					return true;
				}
			}
			return false;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class FizzballFishProbe
			extends FizzballFish {
		private net.minecraft.sounds.SoundEvent lastSound;
		private int blowUpSoundCount;
		private int blowOutSoundCount;

		private FizzballFishProbe(Level level) {
			super(CakeWorldEntities.FIZZBALL_FISH.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent flopSound() {
			return getFlopSound();
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private int blowUpSoundCount() {
			return blowUpSoundCount;
		}

		private int blowOutSoundCount() {
			return blowOutSoundCount;
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private boolean startGoalNamed(String name) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())
						&& wrapped.canUse()) {
					wrapped.start();
					return true;
				}
			}
			return false;
		}

		private void stopGoalNamed(String name) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())) {
					wrapped.stop();
				}
			}
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
			if (sound == SoundEvents.PUFFER_FISH_BLOW_UP) {
				blowUpSoundCount++;
			} else if (sound
					== SoundEvents.PUFFER_FISH_BLOW_OUT) {
				blowOutSoundCount++;
			}
		}
	}

	private static final class JellybeanFishProbe
			extends JellybeanFish {
		private JellybeanFishProbe(Level level) {
			super(CakeWorldEntities.JELLYBEAN_FISH.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				flopSound() {
			return getFlopSound();
		}

		private net.minecraft.sounds.SoundEvent
				swimSound() {
			return getSwimSound();
		}

		private boolean canBeLeashedRole() {
			return canBeLeashed(null);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoals() {
			return targetSelector
					.getAvailableGoals().size();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

	}

	private static final class WaferTurtleProbe
			extends WaferTurtle {
		private net.minecraft.sounds.SoundEvent lastSound;

		private WaferTurtleProbe(Level level) {
			super(CakeWorldEntities.WAFER_TURTLE.get(),
					level);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private boolean canBeLeashedRole() {
			return canBeLeashed(null);
		}

		private boolean hasGoalAt(
				String name, int priority) {
			return goalSelector.getAvailableGoals()
					.stream().anyMatch(wrapped ->
							wrapped.getPriority()
									== priority
									&& name.equals(
											wrapped
													.getGoal()
													.getClass()
													.getSimpleName()));
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoals() {
			return targetSelector
					.getAvailableGoals().size();
		}

		private net.minecraft.sounds.SoundEvent
				adultLandAmbient() {
			setAge(0);
			onGround = true;
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				adultHurtSound() {
			setAge(0);
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				babyHurtSound() {
			setAge(-24000);
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				adultDeathSound() {
			setAge(0);
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				babyDeathSound() {
			setAge(-24000);
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				swimSound() {
			return getSwimSound();
		}

		private net.minecraft.sounds.SoundEvent
				adultStepSound() {
			setAge(0);
			lastSound = null;
			playStepSound(blockPosition(),
					Blocks.SAND.defaultBlockState());
			return lastSound;
		}

		private net.minecraft.sounds.SoundEvent
				babyStepSound() {
			setAge(-24000);
			lastSound = null;
			playStepSound(blockPosition(),
					Blocks.SAND.defaultBlockState());
			return lastSound;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class SourSpriteProbe
			extends SourSprite {
		private net.minecraft.sounds.SoundEvent
				recordedSound;
		private boolean noPhysicsDuringBaseTick;

		private SourSpriteProbe(Level level) {
			super(CakeWorldEntities.SOUR_SPRITE.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private float mainHandDropChance() {
			return getEquipmentDropChance(
					EquipmentSlot.MAINHAND);
		}

		private boolean hasGoalAt(
				String name, int priority) {
			return goalSelector.getAvailableGoals()
					.stream().anyMatch(wrapped ->
							wrapped.getPriority()
									== priority
									&& name.equals(
											wrapped
													.getGoal()
													.getClass()
													.getSimpleName()));
		}

		private boolean hasTargetGoalAt(
				String name, int priority) {
			return targetSelector.getAvailableGoals()
					.stream().anyMatch(wrapped ->
							wrapped.getPriority()
									== priority
									&& name.equals(
											wrapped
													.getGoal()
													.getClass()
													.getSimpleName()));
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoals() {
			return targetSelector
					.getAvailableGoals().size();
		}

		private WrappedGoal goalNamed(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.findFirst().orElse(null);
		}

		private void runTargetGoals() {
			targetSelector.tick();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private void clearRecordedSound() {
			recordedSound = null;
		}

		private net.minecraft.sounds.SoundEvent
				recordedSound() {
			return recordedSound;
		}

		private boolean sawNoPhysicsDuringBaseTick() {
			return noPhysicsDuringBaseTick;
		}

		private void checkDespawnRole() {
			checkDespawn();
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		@Override
		public void baseTick() {
			noPhysicsDuringBaseTick = noPhysics;
			super.baseTick();
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			recordedSound = sound;
		}
	}

	private static final class GingerbreadFolkProbe
			extends GingerbreadFolk {
		private GingerbreadFolkProbe(Level level) {
			super(CakeWorldEntities
					.GINGERBREAD_FOLK.get(), level);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private String typeNameKey() {
			return ((TranslatableComponent)
					getTypeName()).getKey();
		}

		private void checkDespawnRole() {
			checkDespawn();
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private void runBrainOnce() {
			getBrain().tick(
					(ServerLevel)level, this);
		}
	}

	private static final class GummyBunnyProbe
			extends GummyBunny {
		private net.minecraft.sounds.SoundEvent lastSound;

		private GummyBunnyProbe(Level level) {
			super(CakeWorldEntities.GUMMY_BUNNY.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent jumpSound() {
			return getJumpSound();
		}

		private float jumpPower() {
			return getJumpPower();
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoals() {
			return targetSelector.getAvailableGoals().size();
		}

		private boolean runGardenRaidAt(BlockPos cropPos) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if (!"RaidGardenGoal".equals(
						wrapped.getGoal().getClass()
								.getSimpleName())) {
					continue;
				}
				for (int attempt = 0; attempt < 450;
						attempt++) {
					if (wrapped.canUse()) {
						wrapped.start();
						setPos(cropPos.getX() + 0.5D,
								cropPos.getY(),
								cropPos.getZ() + 0.5D);
						wrapped.tick();
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class GingerbreadStomperProbe
			extends GingerbreadStomper {
		private net.minecraft.sounds.SoundEvent lastSound;

		private GingerbreadStomperProbe(Level level) {
			super(CakeWorldEntities
					.GINGERBREAD_STOMPER.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private void runStepSound() {
			playStepSound(BlockPos.ZERO,
					CakeWorldBlocks.GINGERBREAD_BRICKS
							.get().defaultBlockState());
		}

		private void runBlockedByShield(
				LivingEntity blocker) {
			blockedByShield(blocker);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int countTargetGoalsNamed(
				String name) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class SherbetSalmonProbe
			extends SherbetSalmon {
		private net.minecraft.sounds.SoundEvent lastSound;

		private SherbetSalmonProbe(Level level) {
			super(CakeWorldEntities
					.SHERBET_SALMON.get(), level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent flopSound() {
			return getFlopSound();
		}

		private net.minecraft.sounds.SoundEvent swimSound() {
			return getSwimSound();
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private boolean canRandomSwimRole() {
			return canRandomSwim();
		}

		private boolean canBeLeashedRole() {
			return canBeLeashed(null);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class CandyflossSheepProbe
			extends CandyflossSheep {
		private net.minecraft.sounds.SoundEvent lastSound;

		private CandyflossSheepProbe(Level level) {
			super(CakeWorldEntities
					.CANDYFLOSS_SHEEP.get(), level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private boolean canBeLeashedRole() {
			return canBeLeashed(null);
		}

		private net.minecraft.sounds.SoundEvent ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent stepSound() {
			lastSound = null;
			playStepSound(BlockPos.ZERO,
					Blocks.GRASS_BLOCK
							.defaultBlockState());
			return lastSound;
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int countTargetGoals() {
			return targetSelector.getAvailableGoals().size();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class MacaronClamProbe
			extends MacaronClam {
		private MacaronClamProbe(Level level) {
			super(CakeWorldEntities.MACARON_CLAM.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private int rawPeek() {
			return entityData.get(DATA_PEEK_ID);
		}

		private void setRawPeek(int amount) {
			entityData.set(DATA_PEEK_ID, (byte)amount);
		}

		private void setRoleColor(DyeColor color) {
			entityData.set(DATA_COLOR_ID,
					(byte)color.getId());
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private void triggerDuplicationHook() {
			hitByShulkerBullet();
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		@Override
		protected boolean teleportSomewhere() {
			setPos(getX() + 3.0D, getY(), getZ());
			return true;
		}
	}

	private static final class CrumbMiteProbe
			extends CrumbMite {
		private net.minecraft.sounds.SoundEvent lastSound;

		private CrumbMiteProbe(Level level) {
			super(CakeWorldEntities.CRUMB_MITE.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent stepSound() {
			lastSound = null;
			playStepSound(BlockPos.ZERO,
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState());
			return lastSound;
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalPriority(String name) {
			return targetSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private boolean startGoal(String name) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())) {
					if (!wrapped.getGoal().canUse()) {
						return false;
					}
					wrapped.getGoal().start();
					return true;
				}
			}
			return false;
		}

		private void tickGoal(String name, int times) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())) {
					for (int tick = 0;
							tick < times; tick++) {
						wrapped.getGoal().tick();
					}
					return;
				}
			}
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class CandyCaneArcherProbe
			extends CandyCaneArcher {
		private net.minecraft.sounds.SoundEvent lastSound;
		private boolean forceSunBurn;

		private CandyCaneArcherProbe(Level level) {
			super(CakeWorldEntities.CANDY_CANE_ARCHER.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent stepSound() {
			lastSound = null;
			playStepSound(BlockPos.ZERO,
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState());
			return lastSound;
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalPriority(String name) {
			return targetSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int countTargetGoalsAtPriority(int priority) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.filter(wrapped ->
							wrapped.getPriority()
									== priority)
					.count();
		}

		private int countGoalsAssignableTo(
				Class<?> goalClass) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> goalClass
							.isInstance(
									wrapped.getGoal()))
					.count();
		}

		private int goalPriorityAssignableTo(
				Class<?> goalClass) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> goalClass
							.isInstance(
									wrapped.getGoal()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private void equipDefault(
				net.minecraft.world.DifficultyInstance
						difficulty) {
			populateDefaultEquipmentSlots(difficulty);
		}

		private boolean canFireBow() {
			return Items.BOW
							instanceof net.minecraft.world.item
									.ProjectileWeaponItem weapon
					&& canFireProjectileWeapon(weapon);
		}

		private int bowAttackInterval() {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if ("RangedBowAttackGoal".equals(
						wrapped.getGoal().getClass()
								.getSimpleName())) {
					try {
						Field field = wrapped.getGoal()
								.getClass()
								.getDeclaredField(
										"attackIntervalMin");
						field.setAccessible(true);
						return field.getInt(
								wrapped.getGoal());
					} catch (ReflectiveOperationException
							exception) {
						throw new IllegalStateException(
								"Could not inspect Skeleton bow interval",
								exception);
					}
				}
			}
			return -1;
		}

		private void emitCustomDeathLoot(
				DamageSource source) {
			dropCustomDeathLoot(source, 0, true);
		}

		private void startFreezeConversionFromNbt(
				int ticks) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("StrayConversionTime", ticks);
			readAdditionalSaveData(tag);
		}

		private void forceSunBurn(boolean force) {
			forceSunBurn = force;
		}

		private void clearLastSound() {
			lastSound = null;
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		@Override
		protected boolean isSunBurnTick() {
			return forceSunBurn || super.isSunBurnTick();
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class FrostedArcherProbe
			extends FrostedArcher {
		private net.minecraft.sounds.SoundEvent lastSound;
		private boolean forceSunBurn;

		private FrostedArcherProbe(Level level) {
			super(CakeWorldEntities.FROSTED_ARCHER.get(),
					level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent stepSound() {
			lastSound = null;
			playStepSound(BlockPos.ZERO,
					CakeWorldBlocks.BISCUIT_STONE.get()
							.defaultBlockState());
			return lastSound;
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalPriority(String name) {
			return targetSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int countTargetGoalsAtPriority(int priority) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.filter(wrapped ->
							wrapped.getPriority()
									== priority)
					.count();
		}

		private int countGoalsAssignableTo(
				Class<?> goalClass) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> goalClass
							.isInstance(
									wrapped.getGoal()))
					.count();
		}

		private int goalPriorityAssignableTo(
				Class<?> goalClass) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> goalClass
							.isInstance(
									wrapped.getGoal()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private void equipDefault(
				DifficultyInstance difficulty) {
			populateDefaultEquipmentSlots(difficulty);
		}

		private boolean canFireBow() {
			return Items.BOW
							instanceof net.minecraft.world.item
									.ProjectileWeaponItem weapon
					&& canFireProjectileWeapon(weapon);
		}

		private int bowAttackInterval() {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if ("RangedBowAttackGoal".equals(
						wrapped.getGoal().getClass()
								.getSimpleName())) {
					try {
						Field field = wrapped.getGoal()
								.getClass()
								.getDeclaredField(
										"attackIntervalMin");
						field.setAccessible(true);
						return field.getInt(
								wrapped.getGoal());
					} catch (ReflectiveOperationException
							exception) {
						throw new IllegalStateException(
								"Could not inspect Frosted Archer bow interval",
								exception);
					}
				}
			}
			return -1;
		}

		private AbstractArrow createChilledArrow() {
			return getArrow(
					new ItemStack(Items.ARROW), 1.0F);
		}

		private void forceSunBurn(boolean force) {
			forceSunBurn = force;
		}

		private void clearLastSound() {
			lastSound = null;
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		@Override
		protected boolean isSunBurnTick() {
			return forceSunBurn || super.isSunBurnTick();
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class FudgeSkaterProbe
			extends FudgeSkater {
		private net.minecraft.sounds.SoundEvent lastSound;

		private FudgeSkaterProbe(Level level) {
			super(CakeWorldEntities.FUDGE_SKATER.get(),
					level);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private boolean blocksBuilding() {
			return blocksBuilding;
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int targetGoalCount() {
			return targetSelector
					.getAvailableGoals().size();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent stepSound() {
			lastSound = null;
			playStepSound(blockPosition(),
					getBlockStateOn());
			return lastSound;
		}

		private net.minecraft.sounds.SoundEvent lastSound() {
			return lastSound;
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private boolean hotFluidGoalAccepts(
				net.minecraft.world.level.LevelReader level,
				BlockPos pos) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if ("FudgeSkaterGoToHotFluidGoal"
						.equals(wrapped.getGoal()
								.getClass()
								.getSimpleName())) {
					try {
						java.lang.reflect.Method method =
								wrapped.getGoal()
										.getClass()
										.getDeclaredMethod(
												"isValidTarget",
												net.minecraft.world.level
														.LevelReader
														.class,
												BlockPos.class);
						method.setAccessible(true);
						return (boolean)method.invoke(
								wrapped.getGoal(),
								level, pos);
					} catch (ReflectiveOperationException
							exception) {
						throw new IllegalStateException(
								"Could not inspect Fudge Skater hot-fluid goal",
								exception);
					}
				}
			}
			return false;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class SprinkleLlamaProbe
			extends SprinkleLlama {
		private SprinkleLlamaProbe(Level level) {
			super(CakeWorldEntities.SPRINKLE_LLAMA
					.get(), level);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private boolean hasGoalAt(
				String name, int priority) {
			return goalSelector.getAvailableGoals()
					.stream().anyMatch(wrapped ->
							wrapped.getPriority()
									== priority
									&& name.equals(
											wrapped
													.getGoal()
													.getClass()
													.getSimpleName()));
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private boolean hasTargetGoalAt(
				String name, int priority) {
			return targetSelector
					.getAvailableGoals().stream()
					.anyMatch(wrapped ->
							wrapped.getPriority()
									== priority
									&& name.equals(
											wrapped
													.getGoal()
													.getClass()
													.getSimpleName()));
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				angrySound() {
			return getAngrySound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				eatingSound() {
			return getEatingSound();
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private int despawnDelay() {
			return saveWithoutId(
					new CompoundTag())
							.getInt("DespawnDelay");
		}
	}

	private static final class BrittleBiscuitSteedProbe
			extends BrittleBiscuitSteed {
		private BrittleBiscuitSteedProbe(Level level) {
			super(CakeWorldEntities.BRITTLE_BISCUIT_STEED
					.get(), level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent swimSound() {
			return getSwimSound();
		}

		private float waterSlowDown() {
			return getWaterSlowDown();
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalCount() {
			return targetSelector.getAvailableGoals()
					.size();
		}
	}

	private static final class JellyBlobProbe
			extends JellyBlob {
		private net.minecraft.sounds.SoundEvent lastSound;

		private JellyBlobProbe(Level level) {
			super(CakeWorldEntities.JELLY_BLOB.get(),
					level);
		}

		private void setTestSize(int size) {
			setSize(size, true);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private int sampleJumpDelay() {
			return getJumpDelay();
		}

		private float sampleAttackDamage() {
			return getAttackDamage();
		}

		private void performGroundJump() {
			jumpFromGround();
		}

		private boolean canDealContactDamage() {
			return isDealsDamage();
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalCountAtPriority(
				int priority) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.filter(wrapped ->
							wrapped.getPriority()
									== priority)
					.count();
		}

		private int targetGoalCount() {
			return targetSelector.getAvailableGoals()
					.size();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				squishSound() {
			return getSquishSound();
		}

		private net.minecraft.sounds.SoundEvent
				jumpSound() {
			return getJumpSound();
		}

		private float soundVolume() {
			return getSoundVolume();
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private void clearLastSound() {
			lastSound = null;
		}

		private net.minecraft.sounds.SoundEvent
				lastSound() {
			return lastSound;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class IceCreamGolemProbe
			extends IceCreamGolem {
		private net.minecraft.sounds.SoundEvent lastSound;

		private IceCreamGolemProbe(Level level) {
			super(CakeWorldEntities.ICE_CREAM_GOLEM
					.get(), level);
		}

		private int getExperienceValue() {
			return getExperienceReward(null);
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalPriority(String name) {
			return targetSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalCount() {
			return targetSelector.getAvailableGoals()
					.size();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private void clearLastSound() {
			lastSound = null;
		}

		private net.minecraft.sounds.SoundEvent
				lastSound() {
			return lastSound;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
		}
	}

	private static final class LiquoriceWeaverProbe
			extends LiquoriceWeaver {
		private Float testBrightness;
		private Random testRandom;
		private net.minecraft.sounds.SoundEvent
				lastSound;
		private float lastVolume;
		private float lastPitch;

		private LiquoriceWeaverProbe(Level level) {
			super(CakeWorldEntities
					.LIQUORICE_WEAVER.get(),
					level);
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(
							WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int targetGoalPriority(
				String name) {
			return targetSelector
					.getAvailableGoals().stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(
							WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int countTargetGoalsNamed(
				String name) {
			return (int)targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private int experienceReward() {
			return getExperienceReward(null);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(
					Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private void playStep() {
			playStepSound(blockPosition(),
					CakeWorldBlocks.BISCUIT_STONE
							.get()
							.defaultBlockState());
		}

		private Vec3 stuckMultiplier() {
			return stuckSpeedMultiplier;
		}

		private void setTestBrightness(
				float brightness) {
			testBrightness = brightness;
		}

		private void setTestRandom(Random random) {
			testRandom = random;
		}

		private void clearTestEnvironment() {
			testBrightness = null;
			testRandom = null;
		}

		private boolean attackGoalContinues() {
			return goalSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal ->
							"SpiderAttackGoal"
									.equals(goal
											.getClass()
											.getSimpleName()))
					.findFirst()
					.map(goal ->
							goal.canContinueToUse())
					.orElse(false);
		}

		private boolean anySpiderTargetCanUse() {
			return targetSelector
					.getAvailableGoals().stream()
					.map(WrappedGoal::getGoal)
					.filter(goal ->
							"SpiderTargetGoal"
									.equals(goal
											.getClass()
											.getSimpleName()))
					.anyMatch(goal ->
							goal.canUse());
		}

		private void clearLastSound() {
			lastSound = null;
			lastVolume = 0.0F;
			lastPitch = 0.0F;
		}

		private net.minecraft.sounds.SoundEvent
				lastSound() {
			return lastSound;
		}

		private float lastVolume() {
			return lastVolume;
		}

		private float lastPitch() {
			return lastPitch;
		}

		@Override
		public float getBrightness() {
			return testBrightness == null
					? super.getBrightness()
					: testBrightness;
		}

		@Override
		public Random getRandom() {
			return testRandom == null
					? super.getRandom()
					: testRandom;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
			lastVolume = volume;
			lastPitch = pitch;
		}
	}

	private static final class LiquoriceSquidProbe
			extends LiquoriceSquid {
		private int inkParticleRequests;
		private net.minecraft.sounds.SoundEvent
				lastSound;
		private float lastVolume;

		private LiquoriceSquidProbe(Level level) {
			super(CakeWorldEntities
					.LIQUORICE_SQUID.get(),
					level);
		}

		private int goalPriority(String name) {
			return goalSelector.getAvailableGoals()
					.stream()
					.filter(wrapped -> name.equals(
							wrapped.getGoal()
									.getClass()
									.getSimpleName()))
					.mapToInt(
							WrappedGoal::getPriority)
					.findFirst().orElse(-1);
		}

		private int goalCount() {
			return goalSelector.getAvailableGoals()
					.size();
		}

		private int targetGoalCount() {
			return targetSelector
					.getAvailableGoals().size();
		}

		private int experienceReward() {
			return getExperienceReward(null);
		}

		private boolean despawnsInPeaceful() {
			return shouldDespawnInPeaceful();
		}

		private float standingEyeHeight() {
			return getStandingEyeHeight(
					Pose.STANDING,
					getDimensions(Pose.STANDING));
		}

		private Entity.MovementEmission
				movementEmission() {
			return getMovementEmission();
		}

		private net.minecraft.sounds.SoundEvent
				ambientSound() {
			return getAmbientSound();
		}

		private net.minecraft.sounds.SoundEvent
				hurtSound() {
			return getHurtSound(
					DamageSource.GENERIC);
		}

		private net.minecraft.sounds.SoundEvent
				deathSound() {
			return getDeathSound();
		}

		private net.minecraft.sounds.SoundEvent
				squirtSound() {
			return getSquirtSound();
		}

		private float soundVolume() {
			return getSoundVolume();
		}

		private void testHandleAir(int air) {
			handleAirSupply(air);
		}

		private ResourceLocation getLootTableId() {
			return getLootTable();
		}

		private void clearInkEvidence() {
			inkParticleRequests = 0;
			lastSound = null;
			lastVolume = 0.0F;
		}

		private int inkParticleRequests() {
			return inkParticleRequests;
		}

		private net.minecraft.sounds.SoundEvent
				lastSound() {
			return lastSound;
		}

		private float lastVolume() {
			return lastVolume;
		}

		@Override
		protected ParticleOptions getInkParticle() {
			inkParticleRequests++;
			return ParticleTypes.SQUID_INK;
		}

		@Override
		public void playSound(
				net.minecraft.sounds.SoundEvent sound,
				float volume, float pitch) {
			lastSound = sound;
			lastVolume = volume;
		}
	}

	private static final class SnowballProbe
			extends Snowball {
		private SnowballProbe(
				Level level, LivingEntity owner) {
			super(level, owner);
		}

		private void hit(Entity target) {
			onHitEntity(new EntityHitResult(target));
		}
	}

	private static final class FixedRandom extends Random {
		private static final long serialVersionUID = 1L;
		private final int value;

		private FixedRandom(int value) {
			this.value = value;
		}

		@Override
		public int nextInt(int bound) {
			return Math.min(value, bound - 1);
		}
	}

	private static final class SequenceRandom extends Random {
		private static final long serialVersionUID = 1L;
		private final int[] values;
		private int index;

		private SequenceRandom(int... values) {
			this.values = values;
		}

		@Override
		public int nextInt(int bound) {
			int value = values[Math.min(index,
					values.length - 1)];
			index++;
			return Math.min(value, bound - 1);
		}
	}

	private static final class SpiderSpawnRandom
			extends Random {
		private static final long serialVersionUID = 1L;
		private final int jockeyRoll;
		private final float effectRoll;
		private final int effectIndex;

		private SpiderSpawnRandom(
				int jockeyRoll, float effectRoll,
				int effectIndex) {
			this.jockeyRoll = jockeyRoll;
			this.effectRoll = effectRoll;
			this.effectIndex = effectIndex;
		}

		@Override
		public int nextInt(int bound) {
			if (bound == 100) {
				return jockeyRoll;
			}
			if (bound == 5) {
				return effectIndex;
			}
			return 0;
		}

		@Override
		public float nextFloat() {
			return effectRoll;
		}
	}

	private static final class ChocolatePandaProbe
			extends ChocolatePanda {
		private ChocolatePandaProbe(Level level) {
			super(CakeWorldEntities.CHOCOLATE_PANDA.get(),
					level);
		}

		private int countGoalsNamed(String name) {
			return (int)goalSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private void seedRandom(long seed) {
			random.setSeed(seed);
		}

		private int countTargetGoalsNamed(String name) {
			return (int)targetSelector.getAvailableGoals()
					.stream()
					.map(WrappedGoal::getGoal)
					.filter(goal -> name.equals(
							goal.getClass()
									.getSimpleName()))
					.count();
		}

		private void setTestTickCount(int ticks) {
			tickCount = ticks;
		}

		private boolean startGoalNamed(String name) {
			for (WrappedGoal wrapped :
					goalSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())
						&& wrapped.canUse()) {
					wrapped.start();
					wrapped.tick();
					return true;
				}
			}
			return false;
		}

		private boolean startTargetGoalNamed(String name) {
			for (WrappedGoal wrapped :
					targetSelector.getAvailableGoals()) {
				if (name.equals(wrapped.getGoal()
						.getClass().getSimpleName())
						&& wrapped.canUse()) {
					wrapped.start();
					return true;
				}
			}
			return false;
		}
	}

	private static FoodProperties requireFood(GameTestHelper helper, ItemStack stack) {
		FoodProperties food = stack.getItem().getFoodProperties();
		require(helper, food != null, stack.getItem() + " has no food properties");
		return food;
	}

	private static void placeTestBed(
			ServerLevel level, BlockPos foot,
			Direction facing) {
		BlockPos head = foot.relative(facing);
		level.setBlock(foot,
				Blocks.RED_BED.defaultBlockState()
						.setValue(
								BlockStateProperties
										.HORIZONTAL_FACING,
								facing)
						.setValue(
								BlockStateProperties
										.BED_PART,
								BedPart.FOOT),
				2);
		level.setBlock(head,
				Blocks.RED_BED.defaultBlockState()
						.setValue(
								BlockStateProperties
										.HORIZONTAL_FACING,
								facing)
						.setValue(
								BlockStateProperties
										.BED_PART,
								BedPart.HEAD),
				2);
	}

	private static BlockPos findCakeWorldBiomePosition(
			GameTestHelper helper, BlockPos origin,
			int maximumRadius) {
		for (int radius = 0; radius <= maximumRadius;
				radius += 4) {
			for (int x = -radius; x <= radius; x += 4) {
				for (int z = -radius; z <= radius;
						z += 4) {
					if (Math.max(Math.abs(x),
							Math.abs(z)) != radius) {
						continue;
					}
					BlockPos candidate =
							origin.offset(x, 0, z);
					boolean cakeWorld = helper.getLevel()
							.getBiome(candidate).unwrapKey()
							.map(key -> CakeWorld.MODID
									.equals(key.location()
											.getNamespace()))
							.orElse(false);
					if (cakeWorld) {
						return candidate;
					}
				}
			}
		}
		return null;
	}

	private static BlockPos findCakeWorldBiomeColumnPosition(
			GameTestHelper helper, BlockPos origin,
			int maximumRadius, int height) {
		for (int radius = 0; radius <= maximumRadius;
				radius += 4) {
			for (int x = -radius; x <= radius; x += 4) {
				for (int z = -radius; z <= radius;
						z += 4) {
					if (Math.max(Math.abs(x),
							Math.abs(z)) != radius) {
						continue;
					}
					BlockPos candidate =
							origin.offset(x, 0, z);
					boolean cakeWorld = true;
					for (int y = 0; y <= height; y++) {
						ResourceLocation biome =
								helper.getLevel()
										.getBiome(
												candidate
														.above(y))
										.unwrapKey()
										.map(key ->
												key.location())
										.orElse(null);
						if (biome == null
								|| !CakeWorld.MODID.equals(
										biome
												.getNamespace())) {
							cakeWorld = false;
							break;
						}
					}
					if (cakeWorld) {
						return candidate;
					}
				}
			}
		}
		return null;
	}

	private static boolean hasTurtleHome(
			Turtle turtle, BlockPos expected) {
		CompoundTag state = new CompoundTag();
		turtle.addAdditionalSaveData(state);
		return state.getInt("HomePosX")
						== expected.getX()
				&& state.getInt("HomePosY")
						== expected.getY()
				&& state.getInt("HomePosZ")
						== expected.getZ();
	}

	private static void requireSpawnReplacement(GameTestHelper helper, Biome biome,
			EntityType<?> vanilla, EntityType<?> replacement, MobCategory category) {
		boolean foundVanilla = false;
		boolean foundReplacement = false;
		for (MobSpawnSettings.SpawnerData spawn :
				biome.getMobSettings().getMobs(category).unwrap()) {
			foundVanilla |= spawn.type == vanilla;
			foundReplacement |= spawn.type == replacement;
		}
		require(helper, !foundVanilla && foundReplacement,
				"Biome did not replace " + Registry.ENTITY_TYPE.getKey(vanilla)
						+ " with " + Registry.ENTITY_TYPE.getKey(replacement));
	}

	private static boolean hasInheritedMuleAttributes(
			AgeableMob child) {
		return child instanceof MarzipanMule
				&& child.getType()
						== CakeWorldEntities.MARZIPAN_MULE.get()
				&& child.getAttributeBaseValue(
						Attributes.MAX_HEALTH) >= 23.0D
				&& child.getAttributeBaseValue(
						Attributes.MAX_HEALTH) <= 28.0D
				&& child.getAttributeBaseValue(
						Attributes.JUMP_STRENGTH) >= 0.6D
				&& child.getAttributeBaseValue(
						Attributes.JUMP_STRENGTH) <= 0.8D
				&& child.getAttributeBaseValue(
						Attributes.MOVEMENT_SPEED)
								>= 0.180833D
				&& child.getAttributeBaseValue(
						Attributes.MOVEMENT_SPEED)
								<= 0.255834D;
	}

	private static void requireCriterion(GameTestHelper helper, ServerPlayer player,
			String advancementId, String criterion) {
		Advancement advancement = player.getServer().getAdvancements()
				.getAdvancement(new ResourceLocation(advancementId));
		require(helper, advancement != null
						&& player.getAdvancements().getOrStartProgress(advancement)
								.getCriterion(criterion).isDone(),
				"Vanilla role criterion was not credited: " + criterion);
	}

	@SuppressWarnings("unchecked")
	private static List<ServerPlayer> testLevelPlayers(
			ServerLevel level) {
		try {
			Field field = ServerLevel.class
					.getDeclaredField("players");
			field.setAccessible(true);
			return (List<ServerPlayer>)field.get(level);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not access the test level player list",
					exception);
		}
	}

	private static boolean isVisualOnly(
			LightningBolt lightning) {
		try {
			Field field = LightningBolt.class
					.getDeclaredField("visualOnly");
			field.setAccessible(true);
			return field.getBoolean(lightning);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not inspect the Skeleton Trap lightning cue",
					exception);
		}
	}

	private static ServerLevelAccessor
			controlledSpiderAccessor(
					ServerLevel level,
					Difficulty difficulty,
					Random random) {
		return (ServerLevelAccessor)
				Proxy.newProxyInstance(
						ServerLevelAccessor.class
								.getClassLoader(),
						new Class<?>[] {
								ServerLevelAccessor.class },
						(proxy, method, arguments) -> {
							if ("getDifficulty".equals(
									method.getName())) {
								return difficulty;
							}
							if ("getRandom".equals(
									method.getName())) {
								return random;
							}
							return method.invoke(level,
									arguments);
						});
	}

	private static ServerLevelAccessor
			controlledFrostedArcherAccessor(
					ServerLevel level,
					BlockPos base,
					boolean skyVisible) {
		return (ServerLevelAccessor)
				Proxy.newProxyInstance(
						ServerLevelAccessor.class
								.getClassLoader(),
						new Class<?>[] {
								ServerLevelAccessor.class },
						(proxy, method, arguments) -> {
							String name = method.getName();
							if ("getBlockState".equals(name)
									&& arguments != null
									&& arguments.length == 1
									&& arguments[0]
											instanceof BlockPos pos
									&& (pos.equals(
											base.above())
											|| pos.equals(
													base.above(2)))) {
								return Blocks.POWDER_SNOW
										.defaultBlockState();
							}
							if ("canSeeSky".equals(name)
									&& arguments != null
									&& arguments.length == 1) {
								return skyVisible
										&& base.above(2)
												.equals(
														arguments[0]);
							}
							if ("getDifficulty".equals(name)) {
								return Difficulty.NORMAL;
							}
							if ("getRandom".equals(name)) {
								return new Random(1978L);
							}
							if ("getBrightness".equals(name)
									|| "getRawBrightness"
											.equals(name)
									|| "getMaxLocalRawBrightness"
											.equals(name)) {
								return 0;
							}
							return method.invoke(level,
									arguments);
						});
	}

	private static boolean close(double actual, double expected) {
		return Math.abs(actual - expected) < 0.000001D;
	}

	private static boolean grantsEffect(Item item,
			net.minecraft.world.effect.MobEffect effect, int duration) {
		FoodProperties food = item.getFoodProperties();
		return food != null && food.getEffects().stream().anyMatch(entry ->
				entry.getFirst().getEffect() == effect
						&& entry.getFirst().getDuration() == duration
						&& close(entry.getSecond(), 1.0D));
	}

	private static boolean hasExactCustomEffect(
			CompoundTag projectile,
			net.minecraft.world.effect.MobEffect effect,
			int duration, int amplifier) {
		ListTag effects = projectile.getList(
				"CustomPotionEffects", 10);
		for (int index = 0; index < effects.size();
				index++) {
			MobEffectInstance instance =
					MobEffectInstance.load(
							effects.getCompound(index));
			if (instance != null
					&& instance.getEffect() == effect
					&& instance.getDuration() == duration
					&& instance.getAmplifier()
							== amplifier) {
				return true;
			}
		}
		return false;
	}

	private static void clearServerPlayerSpawnInvulnerability(
			ServerPlayer player) {
		try {
			Field field = ServerPlayer.class.getDeclaredField(
					"spawnInvulnerableTime");
			field.setAccessible(true);
			field.setInt(player, 0);
		} catch (ReflectiveOperationException exception) {
			throw new IllegalStateException(
					"Could not clear the test player's vanilla spawn invulnerability",
					exception);
		}
	}

	private static void require(GameTestHelper helper, boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
