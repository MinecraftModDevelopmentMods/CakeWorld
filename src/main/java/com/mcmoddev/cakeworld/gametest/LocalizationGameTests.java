package com.mcmoddev.cakeworld.gametest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.cookbook.DiscoveryType;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Guards CakeWorld's English source catalogue against untranslated registry
 * names and accessibility text. Other languages can override this complete
 * source language through ordinary resource packs.
 */
@GameTestHolder(CakeWorld.MODID)
@PrefixGameTestTemplate(false)
public final class LocalizationGameTests {
	private static final String EMPTY = "empty";
	private static final String ENGLISH =
			"/assets/cakeworld/lang/en_us.json";
	private static final String SOUNDS =
			"/assets/cakeworld/sounds.json";

	private static final Set<String> REQUIRED_PRESENTATION_KEYS = Set.of(
			"screen.cakeworld.cookbook.title",
			"screen.cakeworld.cookbook.empty",
			"screen.cakeworld.cookbook.selected_tab",
			"screen.cakeworld.cookbook.summary",
			"screen.cakeworld.cookbook.first_edition_complete",
			"screen.cakeworld.cookbook.controls",
			"screen.cakeworld.cookbook.narration",
			"screen.cakeworld.cookbook.narration.usage",
			"message.cakeworld.cookbook.discovery",
			"message.cakeworld.cookbook.hint.place",
			"message.cakeworld.cookbook.hint.taste",
			"message.cakeworld.cookbook.hint.creature",
			"message.cakeworld.cookbook.hint.ingredient",
			"message.cakeworld.cookbook.hint.craft",
			"message.cakeworld.cookbook.hint.landmark",
			"message.cakeworld.cookbook.hint.complete",
			"message.cakeworld.library.published",
			"message.cakeworld.library.nothing_to_publish",
			"message.cakeworld.library.read",
			"message.cakeworld.library.empty",
			"tooltip.cakeworld.explorers_cookbook.hint",
			"entity.cakeworld.ferocious_gummy_bunny",
			"entity.cakeworld.cottage_confectioner",
			"template.cakeworld.edible_world",
			"template.cakeworld.edible_world.description",
			"template.cakeworld.edible_world_basemetals",
			"template.cakeworld.edible_world_basemetals.description",
			"template.cakeworld.sampler_platter",
			"template.cakeworld.sampler_platter.description",
			"filled_map.cakeworld.wafer_wreck",
			"filled_map.cakeworld.buried_sweet_tin",
			"container.cakeworld.buried_sweet_tin");

	private LocalizationGameTests() {
	}

	@GameTest(template = EMPTY, batch = "sys013")
	public static void englishSourceLanguageCoversCakeWorldPresentation(
			GameTestHelper helper) {
		JsonObject language = readObject(ENGLISH);
		JsonObject sounds = readObject(SOUNDS);
		Set<String> missing = new TreeSet<>();
		Set<String> invalid = new TreeSet<>();

		ForgeRegistries.BLOCKS.getValues().stream()
				.filter(block -> isCakeWorld(
						ForgeRegistries.BLOCKS.getKey(block)))
				.forEach(block -> requireLanguageKey(language,
						block.getDescriptionId(), missing, invalid));
		ForgeRegistries.ITEMS.getValues().stream()
				.filter(item -> isCakeWorld(
						ForgeRegistries.ITEMS.getKey(item)))
				.forEach(item -> requireLanguageKey(language,
						item.getDescriptionId(), missing, invalid));
		ForgeRegistries.ENTITIES.getValues().stream()
				.filter(type -> isCakeWorld(
						ForgeRegistries.ENTITIES.getKey(type)))
				.forEach(type -> requireLanguageKey(language,
						type.getDescriptionId(), missing, invalid));
		ForgeRegistries.MOB_EFFECTS.getValues().stream()
				.filter(effect -> isCakeWorld(
						ForgeRegistries.MOB_EFFECTS.getKey(effect)))
				.forEach(effect -> requireLanguageKey(language,
						effect.getDescriptionId(), missing, invalid));

		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		biomes.keySet().stream()
				.filter(LocalizationGameTests::isCakeWorld)
				.map(id -> "biome." + id.getNamespace()
						+ "." + id.getPath())
				.forEach(key -> requireLanguageKey(language,
						key, missing, invalid));

		for (DiscoveryType type : DiscoveryType.values()) {
			requireLanguageKey(language, type.translationKey(),
					missing, invalid);
		}
		REQUIRED_PRESENTATION_KEYS.forEach(key ->
				requireLanguageKey(language, key, missing, invalid));
		for (int flavour = 0; flavour < 5; flavour++) {
			requireLanguageKey(language,
					"tooltip.cakeworld.jellylotl.flavour."
							+ flavour,
					missing, invalid);
		}

		Set<ResourceLocation> registeredSounds = new TreeSet<>(
				ForgeRegistries.SOUND_EVENTS.getKeys().stream()
						.filter(LocalizationGameTests::isCakeWorld)
						.toList());
		Set<ResourceLocation> declaredSounds = new TreeSet<>();
		for (String path : sounds.keySet()) {
			ResourceLocation id =
					new ResourceLocation(CakeWorld.MODID, path);
			declaredSounds.add(id);
			JsonObject sound = sounds.getAsJsonObject(path);
			if (!sound.has("subtitle")
					|| !sound.get("subtitle").isJsonPrimitive()) {
				missing.add("subtitle for sound " + id);
				continue;
			}
			requireLanguageKey(language,
					sound.get("subtitle").getAsString(),
					missing, invalid);
		}
		for (ResourceLocation id : registeredSounds) {
			if (!declaredSounds.contains(id)) {
				missing.add("sounds.json entry for " + id);
			}
		}
		for (ResourceLocation id : declaredSounds) {
			if (!registeredSounds.contains(id)) {
				invalid.add("unregistered sound " + id);
			}
		}

		for (String key : language.keySet()) {
			JsonElement value = language.get(key);
			if (!value.isJsonPrimitive()
					|| invalidTranslation(value.getAsString())) {
				invalid.add(key);
			}
		}

		require(helper, missing.isEmpty(),
				"Missing English source translations: " + missing);
		require(helper, invalid.isEmpty(),
				"Invalid English source translations: " + invalid);
		require(helper, !registeredSounds.isEmpty(),
				"No CakeWorld sound was available to audit");
		helper.succeed();
	}

	private static boolean isCakeWorld(ResourceLocation id) {
		return id != null
				&& CakeWorld.MODID.equals(id.getNamespace());
	}

	private static void requireLanguageKey(JsonObject language,
			String key, Set<String> missing, Set<String> invalid) {
		if (!language.has(key)) {
			missing.add(key);
			return;
		}
		JsonElement value = language.get(key);
		if (!value.isJsonPrimitive()
				|| invalidTranslation(value.getAsString())) {
			invalid.add(key);
		}
	}

	private static boolean invalidTranslation(String value) {
		if (value == null || value.isBlank()
				|| value.indexOf('\uFFFD') >= 0) {
			return true;
		}
		return value.codePoints().anyMatch(codePoint ->
				Character.isISOControl(codePoint)
						&& codePoint != '\n'
						&& codePoint != '\t');
	}

	private static JsonObject readObject(String path) {
		try (InputStream stream = LocalizationGameTests.class
				.getResourceAsStream(path)) {
			if (stream == null) {
				throw new IllegalStateException(
						"Missing packaged resource " + path);
			}
			try (InputStreamReader reader = new InputStreamReader(
					stream, StandardCharsets.UTF_8)) {
				JsonElement root = JsonParser.parseReader(reader);
				if (!root.isJsonObject()) {
					throw new IllegalStateException(
							"Expected a JSON object in " + path);
				}
				return root.getAsJsonObject();
			}
		} catch (IOException | RuntimeException exception) {
			throw new IllegalStateException(
					"Could not read packaged resource " + path,
					exception);
		}
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
		}
	}
}
