package zone.moddev.mc.cakeworld.cookbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Read-only, player-specific discovery nudges for the Explorer's Cookbook.
 *
 * <p>Hints are selected only from anchor pages that the player has not
 * discovered. They record no quest state and contain no coordinates.</p>
 */
public final class CookbookHints {
	private static final List<Hint> HINTS = List.of(
			hint(DiscoveryType.VISITING, "candy_plains", "place"),
			hint(DiscoveryType.TASTING, "chocolate_sponge_slice", "taste"),
			hint(DiscoveryType.MEETING, "cocoa_cow", "creature"),
			hint(DiscoveryType.MINING, "rock_candy_deposit", "ingredient"),
			hint(DiscoveryType.CRAFTING, "sponge_batter", "craft"),
			hint(DiscoveryType.FINDING, "cookbook_kiosk", "landmark"));

	private CookbookHints() {
	}

	public static Optional<Hint> nextHint(Player player) {
		Map<DiscoveryType, Set<ResourceLocation>> discoveries =
				CookbookProgress.read(CookbookProgress.snapshot(player));
		List<Hint> available = new ArrayList<>();
		for (Hint hint : HINTS) {
			if (!discoveries.get(hint.type()).contains(hint.target())) {
				available.add(hint);
			}
		}
		if (available.isEmpty()) {
			return Optional.empty();
		}
		int choice = Math.floorMod(player.getUUID().hashCode(),
				available.size());
		return Optional.of(available.get(choice));
	}

	public static void showHint(ServerPlayer player) {
		TranslatableComponent message = nextHint(player)
				.map(hint -> new TranslatableComponent(
						hint.translationKey()))
				.orElseGet(() -> new TranslatableComponent(
						"message.cakeworld.cookbook.hint.complete"));
		player.displayClientMessage(message, false);
	}

	private static Hint hint(DiscoveryType type, String path,
			String translationSuffix) {
		return new Hint(type, new ResourceLocation("cakeworld", path),
				"message.cakeworld.cookbook.hint." + translationSuffix);
	}

	public record Hint(DiscoveryType type, ResourceLocation target,
			String translationKey) {
	}
}
