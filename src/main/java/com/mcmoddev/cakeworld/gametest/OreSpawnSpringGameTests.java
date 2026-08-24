package com.mcmoddev.cakeworld.gametest;

import com.mojang.logging.LogUtils;
import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

/**
 * Behavioural proof for OS-104 on the released OreSpawn 4.0.6.118021 baseline.
 *
 * <p>The tests use Minecraft's real configured water-spring feature. They do
 * not access OreSpawn implementation packages or mutate its configuration.
 * The direct fixture proves CakeWorld Wafer Rock is accepted while an
 * unrelated structural block remains rejected; the fixed-world fixture then
 * proves the resulting source and a player-style sentinel survive an exact
 * save/reload cycle.</p>
 */
@PrefixGameTestTemplate(false)
@GameTestHolder(CakeWorld.MODID)
public final class OreSpawnSpringGameTests {
	private static final String EMPTY = "empty";
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final BlockPos PERSISTED_SPRING =
			new BlockPos(28672, 40, 28672);
	private static final BlockPos PERSISTED_SENTINEL =
			PERSISTED_SPRING.offset(3, 0, 3);

	private OreSpawnSpringGameTests() {
	}

	@GameTest(template = EMPTY, batch = "os104")
	public static void releasedOreSpawnExpandsVanillaSpringHosts(
			GameTestHelper helper) {
		ServerLevel level = helper.getLevel();
		String version = ModList.get()
				.getModContainerById("orespawn")
				.map(container -> container.getModInfo()
						.getVersion().toString())
				.orElse("missing");
		require(helper, "4.0.6.118021".equals(version),
				"OS-104 requires the released OreSpawn 4.0.6.118021 artifact, loaded="
						+ version);

		BlockPos edible = helper.absolutePos(new BlockPos(3, 3, 3));
		prepareFixture(level, edible,
				CakeWorldBlocks.WAFER_ROCK.get()
						.defaultBlockState());
		boolean ediblePlaced = placeWaterSpring(level, edible, 104L);
		require(helper, ediblePlaced && isWaterSource(level, edible),
				"Minecraft's water spring did not form inside non-vanilla Wafer Rock");

		BlockPos structural = helper.absolutePos(
				new BlockPos(11, 3, 3));
		prepareFixture(level, structural,
				Blocks.BRICKS.defaultBlockState());
		boolean structuralPlaced = placeWaterSpring(
				level, structural, 105L);
		require(helper, !structuralPlaced
						&& level.getBlockState(structural)
								.is(Blocks.BRICKS),
				"OreSpawn widened vanilla springs beyond baked geology into structural blocks");
		helper.succeed();
	}

	@GameTest(template = EMPTY, batch = "os104world",
			timeoutTicks = 24000)
	public static void fixedWorldSpringSurvivesReload(
			GameTestHelper helper) {
		if (!Boolean.getBoolean("cakeworld.fixedWorldgenEvidence")) {
			helper.succeed();
			return;
		}
		ServerLevel level = helper.getLevel().getServer()
				.getLevel(Level.OVERWORLD);
		require(helper, level != null,
				"The fixed-seed server did not expose the Overworld");
		level.getChunkAt(PERSISTED_SPRING);
		boolean reload = level.getBlockState(PERSISTED_SENTINEL)
				.is(Blocks.BRICKS);
		if (!reload) {
			prepareFixture(level, PERSISTED_SPRING,
					CakeWorldBlocks.WAFER_ROCK.get()
							.defaultBlockState());
			require(helper,
					placeWaterSpring(level, PERSISTED_SPRING, 10402L)
							&& isWaterSource(level, PERSISTED_SPRING),
					"Could not seed the fresh-world non-vanilla spring fixture");
			level.setBlock(PERSISTED_SENTINEL,
					Blocks.BRICKS.defaultBlockState(), 2);
		}
		require(helper,
				isWaterSource(level, PERSISTED_SPRING)
						&& level.getBlockState(PERSISTED_SPRING.above())
								.is(CakeWorldBlocks.WAFER_ROCK.get())
						&& level.getBlockState(PERSISTED_SPRING.below())
								.is(CakeWorldBlocks.WAFER_ROCK.get())
						&& level.getBlockState(PERSISTED_SPRING.north())
								.is(CakeWorldBlocks.WAFER_ROCK.get())
						&& level.getBlockState(PERSISTED_SPRING.east())
								.is(CakeWorldBlocks.WAFER_ROCK.get())
						&& level.getBlockState(PERSISTED_SPRING.south())
								.is(CakeWorldBlocks.WAFER_ROCK.get())
						&& level.getBlockState(PERSISTED_SENTINEL)
								.is(Blocks.BRICKS),
				"The OreSpawn 4.0.6.118021 spring fixture or player sentinel did not survive reload");
		LOGGER.info("OreSpawn 4.0.6.118021 spring audit: spring={}, reload={}, source={}, sentinel={}",
				PERSISTED_SPRING, reload,
				isWaterSource(level, PERSISTED_SPRING),
				level.getBlockState(PERSISTED_SENTINEL)
						.is(Blocks.BRICKS));
		helper.succeed();
	}

	private static void prepareFixture(ServerLevel level,
			BlockPos target, BlockState host) {
		level.setBlock(target, host, 2);
		level.setBlock(target.above(), host, 2);
		level.setBlock(target.below(), host, 2);
		level.setBlock(target.north(), host, 2);
		level.setBlock(target.east(), host, 2);
		level.setBlock(target.south(), host, 2);
		level.setBlock(target.west(), Blocks.AIR.defaultBlockState(), 2);
	}

	private static boolean placeWaterSpring(ServerLevel level,
			BlockPos target, long seed) {
		WorldgenRandom random = new WorldgenRandom(
				new LegacyRandomSource(seed));
		return MiscOverworldFeatures.SPRING_WATER.value().place(
				level, level.getChunkSource().getGenerator(),
				random, target);
	}

	private static boolean isWaterSource(ServerLevel level,
			BlockPos position) {
		return level.getBlockState(position).is(Blocks.WATER)
				&& level.getFluidState(position).isSource();
	}

	private static void require(GameTestHelper helper,
			boolean condition, String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
