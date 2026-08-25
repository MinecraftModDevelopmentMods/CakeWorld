package com.mcmoddev.cakeworld.gametest;

import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

/** Black-box release proof for the public weather-material event route. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_weather_materials")
public final class WeatherMaterialGameTests {
	private static final String EMPTY = "empty";

	private WeatherMaterialGameTests() {
	}

	@GameTest(template = EMPTY)
	public static void configuredSnowAndIceReplaceVanillaWeatherProducts(
			GameTestHelper helper) {
		BlockPos snow = helper.absolutePos(new BlockPos(8, 3, 8));
		BlockPos ice = snow.east();
		for (BlockPos top : new BlockPos[] { snow, ice }) {
			helper.getLevel().setBlock(top.below(),
					Blocks.STONE.defaultBlockState(), 3);
			helper.getLevel().setBlock(top.above(),
					Blocks.AIR.defaultBlockState(), 3);
		}
		helper.getLevel().setBlock(snow, Blocks.SNOW.defaultBlockState(), 3);
		helper.getLevel().setBlock(ice, Blocks.ICE.defaultBlockState(), 3);

		ChunkAccess chunk = helper.getLevel().getChunkAt(snow);
		ChunkEvent.Load event = new ChunkEvent.Load(chunk);
		require(helper, event.getWorld() == helper.getLevel(),
				"Chunk-load fixture did not retain its ServerLevel");
		MinecraftForge.EVENT_BUS.post(event);

		boolean snowConverted = helper.getLevel().getBlockState(snow)
				.is(CakeWorldBlocks.ICING_LAYER.get());
		boolean iceConverted = helper.getLevel().getBlockState(ice)
				.is(CakeWorldBlocks.FROZEN_LEMONADE.get());
		require(helper, snowConverted && iceConverted,
				"OreSpawn weather conversion failed: snow=" + snowConverted
						+ ", ice=" + iceConverted + ", snowState="
						+ helper.getLevel().getBlockState(snow)
						+ ", iceState="
						+ helper.getLevel().getBlockState(ice));
		helper.succeed();
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}
}
