package com.mcmoddev.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;
import com.mcmoddev.cakeworld.init.CakeWorldFluids;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

/** Objective registry/provider boundary for CakeWorld's three realm identities. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_realm_identity")
public final class RealmIdentityGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation OVERWORLD_BIOME =
			id("gingerbread_hearthlands");
	private static final ResourceLocation NETHER_BIOME = id("fudge_wastes");
	private static final ResourceLocation END_BIOME = id("meringue_islands");

	private RealmIdentityGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void realmsRetainDistinctBiomesMaterialsAndStructures(
			GameTestHelper helper) {
		require(helper,
				helper.getLevel().getServer().getLevel(Level.OVERWORLD) != null
						&& helper.getLevel().getServer().getLevel(Level.NETHER)
								!= null
						&& helper.getLevel().getServer().getLevel(Level.END) != null,
				"CakeWorld runtime did not expose all three vanilla realms");

		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		assertRealmBiome(helper, biomes, OVERWORLD_BIOME,
				BiomeDictionary.Type.OVERWORLD,
				BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END);
		assertRealmBiome(helper, biomes, NETHER_BIOME,
				BiomeDictionary.Type.NETHER,
				BiomeDictionary.Type.OVERWORLD, BiomeDictionary.Type.END);
		assertRealmBiome(helper, biomes, END_BIOME,
				BiomeDictionary.Type.END,
				BiomeDictionary.Type.OVERWORLD, BiomeDictionary.Type.NETHER);

		require(helper,
				OVERWORLD_BIOME.equals(ForgeRegistries.BIOMES.getKey(
						CakeWorldBiomes.GINGERBREAD_HEARTHLANDS.get()))
						&& NETHER_BIOME.equals(ForgeRegistries.BIOMES.getKey(
								CakeWorldBiomes.FUDGE_WASTES.get()))
						&& END_BIOME.equals(ForgeRegistries.BIOMES.getKey(
								CakeWorldBiomes.MERINGUE_ISLANDS.get())),
				"Signature biome registry objects drifted from their runtime IDs");

		assertRegisteredPalette(helper);
		JsonObject profile = adventureProfile(helper);
		assertTerrainDimensions(helper, profile);
		assertSurface(helper, profile, "cakeworld:overworld_land",
				OVERWORLD_BIOME.toString(), "cakeworld:biscuit_crumbs",
				"cakeworld:chocolate_sponge");
		assertSurface(helper, profile, "cakeworld:nether",
				NETHER_BIOME.toString(), "cakeworld:fudge_rock",
				"cakeworld:fudge_rock");
		assertSurface(helper, profile, "cakeworld:end",
				END_BIOME.toString(), "cakeworld:meringue_foam",
				"cakeworld:marshmallow");
		assertDimensionMaterials(helper, profile);

		assertStructureHome(helper, "gingerbread_village",
				OVERWORLD_BIOME.toString());
		assertStructureHome(helper, "liquorice_fortress",
				NETHER_BIOME.toString());
		assertStructureHome(helper, "macaron_citadel",
				END_BIOME.toString());
		helper.succeed();
	}

	private static void assertRealmBiome(GameTestHelper helper,
			Registry<Biome> biomes, ResourceLocation id,
			BiomeDictionary.Type expected, BiomeDictionary.Type wrongA,
			BiomeDictionary.Type wrongB) {
		ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, id);
		require(helper, biomes.containsKey(id)
					&& BiomeDictionary.hasType(key, expected)
					&& !BiomeDictionary.hasType(key, wrongA)
					&& !BiomeDictionary.hasType(key, wrongB),
				"Signature biome lost its exclusive realm identity: " + id);
	}

	private static void assertRegisteredPalette(GameTestHelper helper) {
		require(helper,
				id("chocolate_sponge").equals(ForgeRegistries.BLOCKS.getKey(
						CakeWorldBlocks.CHOCOLATE_SPONGE.get()))
						&& id("fudge_rock").equals(ForgeRegistries.BLOCKS.getKey(
								CakeWorldBlocks.FUDGE_ROCK.get()))
						&& id("meringue_foam").equals(
								ForgeRegistries.BLOCKS.getKey(
										CakeWorldBlocks.MERINGUE_FOAM.get()))
						&& id("lemonade").equals(ForgeRegistries.FLUIDS.getKey(
								CakeWorldFluids.LEMONADE.get()))
						&& id("hot_fudge").equals(ForgeRegistries.FLUIDS.getKey(
								CakeWorldFluids.HOT_FUDGE.get())),
				"Signature realm blocks or fluids are not registered");
	}

	private static void assertTerrainDimensions(GameTestHelper helper,
			JsonObject profile) {
		JsonObject dimensions = profile.getAsJsonObject("terrain_dimensions");
		for (String id : new String[] { "minecraft:overworld",
				"minecraft:the_nether", "minecraft:the_end" }) {
			JsonObject dimension = dimensions.getAsJsonObject(id);
			require(helper, dimension != null
						&& dimension.get("enabled").getAsBoolean(),
					"Adventure terrain conversion is disabled for " + id);
		}
	}

	private static void assertSurface(GameTestHelper helper, JsonObject profile,
			String paletteId, String biomeId, String top, String filler) {
		JsonObject palette = profile.getAsJsonObject("biome_palettes")
				.getAsJsonObject(paletteId);
		JsonObject surface = palette.getAsJsonObject("biomes")
				.getAsJsonObject(biomeId).getAsJsonObject("surface");
		require(helper, top.equals(surface.get("top_block").getAsString())
					&& filler.equals(
							surface.get("filler_block").getAsString()),
				"Signature surface palette drifted for " + biomeId);
	}

	private static void assertDimensionMaterials(GameTestHelper helper,
			JsonObject profile) {
		JsonObject materials = profile.getAsJsonObject("dimension_materials");
		JsonObject overworld = materials.getAsJsonObject(
				"cakeworld:overworld_materials");
		JsonObject nether = materials.getAsJsonObject(
				"cakeworld:nether_materials");
		JsonObject end = materials.getAsJsonObject("cakeworld:end_materials");
		require(helper,
				"cakeworld:lemonade".equals(
						overworld.get("default_fluid").getAsString())
						&& "cakeworld:hot_fudge".equals(overworld.get(
								"deep_aquifer_fluid").getAsString())
						&& "cakeworld:hot_fudge".equals(
								nether.get("default_fluid").getAsString())
						&& "cakeworld:icing_layer".equals(
								end.get("snow_block").getAsString())
						&& "cakeworld:frozen_lemonade".equals(
								end.get("ice_block").getAsString()),
				"Realm-specific fluid, snow, or ice materials drifted");
	}

	private static void assertStructureHome(GameTestHelper helper, String path,
			String expectedBiome) {
		JsonArray values = resource(helper,
				"/data/cakeworld/tags/worldgen/biome/has_structure/" + path
						+ ".json").getAsJsonArray("values");
		boolean present = false;
		for (int i = 0; i < values.size(); i++) {
			present |= expectedBiome.equals(values.get(i).getAsString());
		}
		require(helper, present,
				"Realm signature structure lost expected home: " + path);
	}

	private static JsonObject adventureProfile(GameTestHelper helper) {
		return resource(helper, "/data/cakeworld/orespawn/provider.json")
				.getAsJsonObject("templates")
				.getAsJsonObject("cakeworld:edible_world")
				.getAsJsonObject("profile");
	}

	private static JsonObject resource(GameTestHelper helper, String path) {
		try (InputStreamReader reader = new InputStreamReader(
				RealmIdentityGameTests.class.getResourceAsStream(path),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			helper.fail("Could not read packaged realm identity resource " + path
					+ ": " + exception.getMessage());
			throw new IllegalStateException(exception);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(CakeWorld.MODID, path);
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
