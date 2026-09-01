package com.mcmoddev.cakeworld.gametest;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import com.mcmoddev.cakeworld.cookbook.CookbookProgress;
import com.mcmoddev.cakeworld.cookbook.CookbookSummary;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldItems;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

/** Regression proof that the Cookbook is a personal guide, not a quest gate. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_cookbook_guide")
public final class CookbookGuideGameTests {
	private static final String EMPTY = "empty";

	private CookbookGuideGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void discoveriesArePersonalReadOnlyPagesWithoutRewards(
			GameTestHelper helper) {
		ServerPlayer explorer = player(helper, "CakeWorldGuideExplorer");
		ServerPlayer neighbour = player(helper, "CakeWorldGuideNeighbour");
		ItemStack cookbook = CakeWorldItems.EXPLORERS_COOKBOOK.get()
				.getDefaultInstance();
		ResourceLocation page = Objects.requireNonNull(
				ForgeRegistries.BIOMES.getKey(
						CakeWorldBiomes.CANDY_PLAINS.get()));

		CompoundTag before = CookbookProgress.snapshot(explorer);
		String inventoryBefore = inventory(explorer);
		float healthBefore = explorer.getHealth();
		int foodBefore = explorer.getFoodData().getFoodLevel();
		float saturationBefore = explorer.getFoodData().getSaturationLevel();
		int experienceLevelBefore = explorer.experienceLevel;
		int experienceTotalBefore = explorer.totalExperience;
		float experienceProgressBefore = explorer.experienceProgress;

		require(helper, !explorer.getInventory().contains(cookbook)
					&& before.getAllKeys().isEmpty(),
				"SYS-003 fixture started with a book or hidden progress state");
		require(helper,
				CookbookProgress.discover(explorer,
						DiscoveryType.VISITING, page),
				"A bookless player could not record an optional discovery page");
		require(helper,
				!CookbookProgress.discover(explorer,
						DiscoveryType.VISITING, page),
				"Repeating a discovery created quest-like duplicate progress");

		CompoundTag after = CookbookProgress.snapshot(explorer);
		Map<DiscoveryType, Set<ResourceLocation>> pages =
				CookbookProgress.read(after);
		Map<DiscoveryType, Set<ResourceLocation>> neighbourPages =
				CookbookProgress.read(CookbookProgress.snapshot(neighbour));
		CookbookSummary summary = CookbookSummary.from(pages);
		require(helper,
				after.getAllKeys().equals(Set.of("places"))
						&& pages.get(DiscoveryType.VISITING)
								.equals(Set.of(page))
						&& pages.entrySet().stream()
								.filter(entry -> entry.getKey()
										!= DiscoveryType.VISITING)
								.allMatch(entry -> entry.getValue().isEmpty())
						&& neighbourPages.values().stream()
								.allMatch(Set::isEmpty)
						&& summary.totalPages() == 1
						&& !summary.firstEditionComplete(),
				"Discovery storage was not one personal, optional guide page");

		require(helper,
				inventory(explorer).equals(inventoryBefore)
						&& !explorer.getInventory().contains(cookbook)
						&& close(explorer.getHealth(), healthBefore)
						&& explorer.getFoodData().getFoodLevel() == foodBefore
						&& close(explorer.getFoodData().getSaturationLevel(),
								saturationBefore)
						&& explorer.experienceLevel == experienceLevelBefore
						&& explorer.totalExperience == experienceTotalBefore
						&& close(explorer.experienceProgress,
								experienceProgressBefore),
				"A discovery granted an item, stat reward, or mandatory progression");
		helper.succeed();
	}

	private static ServerPlayer player(GameTestHelper helper, String name) {
		return FakePlayerFactory.get(helper.getLevel(),
				new GameProfile(UUID.randomUUID(), name));
	}

	private static String inventory(ServerPlayer player) {
		return player.getInventory().save(new ListTag()).toString();
	}

	private static boolean close(double left, double right) {
		return Math.abs(left - right) < 0.001D;
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
