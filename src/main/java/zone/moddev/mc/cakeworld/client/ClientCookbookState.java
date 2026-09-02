package zone.moddev.mc.cakeworld.client;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import zone.moddev.mc.cakeworld.cookbook.CookbookProgress;
import zone.moddev.mc.cakeworld.cookbook.CookbookSummary;
import zone.moddev.mc.cakeworld.cookbook.DiscoveryType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public final class ClientCookbookState {
	private static Map<DiscoveryType, Set<ResourceLocation>> discoveries =
			emptyState();

	private ClientCookbookState() {
	}

	public static void load(CompoundTag progress) {
		discoveries = CookbookProgress.read(progress);
	}

	public static Set<ResourceLocation> get(DiscoveryType type) {
		return discoveries.getOrDefault(type, Collections.emptySet());
	}

	public static CookbookSummary summary() {
		return CookbookSummary.from(discoveries);
	}

	private static Map<DiscoveryType, Set<ResourceLocation>> emptyState() {
		Map<DiscoveryType, Set<ResourceLocation>> result =
				new EnumMap<>(DiscoveryType.class);
		for (DiscoveryType type : DiscoveryType.values()) {
			result.put(type, Collections.emptySet());
		}
		return result;
	}
}
