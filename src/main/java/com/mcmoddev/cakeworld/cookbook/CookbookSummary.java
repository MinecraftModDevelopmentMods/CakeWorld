package com.mcmoddev.cakeworld.cookbook;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;

/**
 * A read-only summary derived from the player's actual discovery pages.
 *
 * <p>Each non-empty discovery method earns one First Edition stamp. Six
 * stamps mean that the player has experienced every discovery method, not
 * that all future CakeWorld content has been completed.</p>
 */
public final class CookbookSummary {
	private final Map<DiscoveryType, Integer> pagesByType;
	private final int totalPages;
	private final int stamps;

	private CookbookSummary(Map<DiscoveryType, Integer> pagesByType,
			int totalPages, int stamps) {
		this.pagesByType = pagesByType;
		this.totalPages = totalPages;
		this.stamps = stamps;
	}

	public static CookbookSummary from(
			Map<DiscoveryType, Set<ResourceLocation>> discoveries) {
		Map<DiscoveryType, Integer> counts =
				new EnumMap<>(DiscoveryType.class);
		int total = 0;
		int earnedStamps = 0;
		for (DiscoveryType type : DiscoveryType.values()) {
			int count = discoveries.getOrDefault(type, Set.of()).size();
			counts.put(type, count);
			total += count;
			if (count > 0) {
				earnedStamps++;
			}
		}
		return new CookbookSummary(counts, total, earnedStamps);
	}

	public int pages(DiscoveryType type) {
		return pagesByType.getOrDefault(type, 0);
	}

	public boolean hasStamp(DiscoveryType type) {
		return pages(type) > 0;
	}

	public int totalPages() {
		return totalPages;
	}

	public int stamps() {
		return stamps;
	}

	public int stampGoal() {
		return DiscoveryType.values().length;
	}

	public boolean firstEditionComplete() {
		return stamps == stampGoal();
	}
}
