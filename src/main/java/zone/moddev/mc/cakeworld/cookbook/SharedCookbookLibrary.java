package zone.moddev.mc.cakeworld.cookbook;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Server-saved community lore that never writes personal progression.
 */
public final class SharedCookbookLibrary extends SavedData {
	private static final String DATA_NAME = "cakeworld_shared_cookbook";
	private static final String LORE_TAG = "Lore";

	private final CompoundTag lore;

	public SharedCookbookLibrary() {
		this(new CompoundTag());
	}

	private SharedCookbookLibrary(CompoundTag lore) {
		this.lore = lore;
	}

	public static SharedCookbookLibrary get(ServerLevel level) {
		return level.getServer().overworld().getDataStorage()
				.computeIfAbsent(SharedCookbookLibrary::load,
						SharedCookbookLibrary::new, DATA_NAME);
	}

	public Optional<SharedPage> publishNext(ServerPlayer contributor) {
		Map<DiscoveryType, Set<ResourceLocation>> personal =
				CookbookProgress.read(CookbookProgress.snapshot(contributor));
		Map<DiscoveryType, Set<ResourceLocation>> shared =
				CookbookProgress.read(lore);
		for (DiscoveryType type : DiscoveryType.values()) {
			for (ResourceLocation page : personal.get(type)) {
				if (!shared.get(type).contains(page)) {
					ListTag pages = lore.getList(type.tabName(),
							Tag.TAG_STRING);
					pages.add(StringTag.valueOf(page.toString()));
					lore.put(type.tabName(), pages);
					setDirty();
					return Optional.of(new SharedPage(type, page));
				}
			}
		}
		return Optional.empty();
	}

	public boolean contains(DiscoveryType type, ResourceLocation page) {
		return CookbookProgress.read(lore).get(type).contains(page);
	}

	public int pageCount() {
		return CookbookProgress.read(lore).values().stream()
				.mapToInt(Set::size).sum();
	}

	public Optional<SharedPage> samplePage() {
		Map<DiscoveryType, Set<ResourceLocation>> shared =
				CookbookProgress.read(lore);
		for (DiscoveryType type : DiscoveryType.values()) {
			for (ResourceLocation page : shared.get(type)) {
				return Optional.of(new SharedPage(type, page));
			}
		}
		return Optional.empty();
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		tag.put(LORE_TAG, lore.copy());
		return tag;
	}

	public static SharedCookbookLibrary load(CompoundTag tag) {
		return new SharedCookbookLibrary(
				tag.contains(LORE_TAG, Tag.TAG_COMPOUND)
						? tag.getCompound(LORE_TAG).copy()
						: new CompoundTag());
	}

	public record SharedPage(DiscoveryType type, ResourceLocation page) {
	}
}
