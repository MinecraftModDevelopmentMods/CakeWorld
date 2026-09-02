package zone.moddev.mc.cakeworld.gametest;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Focused proof that the optional Sampler never wins normal auto-selection. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_selection")
public final class AutomaticTemplateSelectionGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation EDIBLE_WORLD =
			new ResourceLocation("cakeworld", "edible_world");

	private AutomaticTemplateSelectionGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 200)
	public static void ordinaryFreshWorldDoesNotSelectSampler(
			GameTestHelper helper) {
		GeologyProfileView profile = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, profile.selectedTemplate()
				.filter(EDIBLE_WORLD::equals).isPresent(),
				"Ordinary CakeWorld-only fresh world did not select "
						+ EDIBLE_WORLD);
		Registry<Biome> biomes = helper.getLevel().registryAccess()
				.registryOrThrow(Registry.BIOME_REGISTRY);
		require(helper, !biomes.containsKey(
				SamplerThirdPartyBiomeFixture.BIOME_ID),
				"GameTest-only unrelated biome leaked into an ordinary runtime");
		require(helper, !biomes.containsKey(
				BlankBiomeRegistrationFixture.BIOME_ID),
				"GameTest-only blank biome leaked into an ordinary runtime");
		require(helper, !biomes.containsKey(
				ClimateBoundaryFixture.FIRST_BIOME_ID),
				"GameTest-only climate biome leaked into an ordinary runtime");

		JsonObject templates = packagedProvider(helper)
				.getAsJsonObject("templates");
		JsonObject sampler = templates.getAsJsonObject(
				"cakeworld:sampler_platter");
		require(helper, sampler != null
					&& !sampler.get("auto_select").getAsBoolean(),
				"Sampler Platter became eligible for automatic selection");
		require(helper, templates.getAsJsonObject(
				"cakeworld:edible_world").get("auto_select")
				.getAsBoolean(),
				"Normal adventure lost automatic selection");
		helper.succeed();
	}

	private static JsonObject packagedProvider(GameTestHelper helper) {
		try (InputStreamReader reader = new InputStreamReader(
				AutomaticTemplateSelectionGameTests.class.getResourceAsStream(
						"/data/cakeworld/orespawn/provider.json"),
				StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} catch (Exception exception) {
			helper.fail("Could not read generated CakeWorld provider: "
					+ exception.getMessage());
			throw new IllegalStateException(exception);
		}
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
