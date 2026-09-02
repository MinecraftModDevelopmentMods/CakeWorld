package zone.moddev.mc.cakeworld.cookbook;

import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import zone.moddev.mc.cakeworld.init.CakeWorldItems;
import zone.moddev.mc.cakeworld.init.CakeWorldSounds;
import zone.moddev.mc.cakeworld.network.CakeWorldNetwork;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * Server-owned Cookbook discoveries stored on the player rather than the item.
 */
public final class CookbookProgress {
	private static final String ROOT_TAG = "CakeWorldCookbook";
	private static final String GRANTED_TAG = "StarterBookGranted";

	private CookbookProgress() {
	}

	public static boolean discover(ServerPlayer player, DiscoveryType type,
			ResourceLocation discovery) {
		CompoundTag root = root(player);
		ListTag list = root.getList(type.tabName(), Tag.TAG_STRING);
		String value = discovery.toString();
		for (Tag entry : list) {
			if (value.equals(entry.getAsString())) {
				return false;
			}
		}

		list.add(StringTag.valueOf(value));
		root.put(type.tabName(), list);
		player.displayClientMessage(new TranslatableComponent(
				"message.cakeworld.cookbook.discovery", value), true);
		if (player.connection != null) {
			player.playNotifySound(CakeWorldSounds.COOKBOOK_DISCOVERY.get(),
					SoundSource.PLAYERS, 0.65F, 1.1F);
			CakeWorldNetwork.sync(player);
		}
		return true;
	}

	public static CompoundTag snapshot(Player player) {
		return root(player).copy();
	}

	public static Map<DiscoveryType, Set<ResourceLocation>> read(CompoundTag root) {
		Map<DiscoveryType, Set<ResourceLocation>> result =
				new EnumMap<>(DiscoveryType.class);
		for (DiscoveryType type : DiscoveryType.values()) {
			Set<ResourceLocation> entries = new LinkedHashSet<>();
			for (Tag entry : root.getList(type.tabName(), Tag.TAG_STRING)) {
				ResourceLocation id = ResourceLocation.tryParse(entry.getAsString());
				if (id != null) {
					entries.add(id);
				}
			}
			result.put(type, entries);
		}
		return result;
	}

	public static void copyForRespawn(Player original, Player replacement) {
		CompoundTag originalRoot = root(original);
		persisted(replacement).put(ROOT_TAG, originalRoot.copy());
	}

	public static void grantStarterBook(ServerPlayer player) {
		CompoundTag root = root(player);
		if (root.getBoolean(GRANTED_TAG)) {
			return;
		}
		root.putBoolean(GRANTED_TAG, true);
		recoverBook(player);
	}

	public static boolean recoverBook(ServerPlayer player) {
		ItemStack cookbook = CakeWorldItems.EXPLORERS_COOKBOOK.get().getDefaultInstance();
		if (player.getInventory().contains(cookbook)) {
			return false;
		}
		if (!player.getInventory().add(cookbook)) {
			player.drop(cookbook, false);
		}
		return true;
	}

	private static CompoundTag root(Player player) {
		CompoundTag persisted = persisted(player);
		if (!persisted.contains(ROOT_TAG, Tag.TAG_COMPOUND)) {
			persisted.put(ROOT_TAG, new CompoundTag());
		}
		return persisted.getCompound(ROOT_TAG);
	}

	private static CompoundTag persisted(Player player) {
		CompoundTag playerData = player.getPersistentData();
		if (!playerData.contains(Player.PERSISTED_NBT_TAG, Tag.TAG_COMPOUND)) {
			playerData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}
		return playerData.getCompound(Player.PERSISTED_NBT_TAG);
	}
}
