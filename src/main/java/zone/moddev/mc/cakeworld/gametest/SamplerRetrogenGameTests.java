package zone.moddev.mc.cakeworld.gametest;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import org.slf4j.Logger;

import zone.moddev.mc.orespawn.api.GeologyProfileView;
import zone.moddev.mc.orespawn.api.OreSpawnApi;

/** Three-phase copied-world proof for bounded OreSpawn retrogen. */
@PrefixGameTestTemplate(false)
@GameTestHolder("cakeworld_sampler_retrogen")
public final class SamplerRetrogenGameTests {
	private static final String EMPTY = "empty";
	private static final ResourceLocation SAMPLER_TEMPLATE =
			new ResourceLocation("cakeworld", "sampler_platter");
	private static final ResourceLocation SPRINKLE_RULE =
			new ResourceLocation("cakeworld", "ore/retrogen_sprinkles");
	private static final ResourceLocation CONTROL_RULE =
			new ResourceLocation("cakeworld", "ore/retrogen_control");
	private static final ChunkPos ELIGIBLE_CHUNK = new ChunkPos(560, 560);
	private static final ChunkPos INELIGIBLE_CHUNK = new ChunkPos(561, 560);
	private static final int EVIDENCE_Y = 20;
	private static final int RETROGEN_WAIT_TICKS = 200;
	private static final Logger LOGGER = LogUtils.getLogger();

	private SamplerRetrogenGameTests() {
	}

	@GameTest(template = EMPTY, timeoutTicks = 400)
	public static void copiedWorldRetrogenIsBounded(GameTestHelper helper) {
		String phase = System.getProperty(
				"cakeworld.samplerRetrogenPhase", "").trim();
		require(helper, "baseline".equals(phase) || "apply".equals(phase)
				|| "reload".equals(phase),
				"Retrogen evidence requires baseline, apply, or reload phase");
		GeologyProfileView view = OreSpawnApi.getActiveProfile(
				helper.getLevel().getServer()).orElseThrow();
		require(helper, view.selectedTemplate()
				.filter(SAMPLER_TEMPLATE::equals).isPresent(),
				"Retrogen evidence did not use the copied Sampler world");
		JsonObject profile = view.toJson();
		JsonObject retrogen = profile.getAsJsonObject("retrogen");
		JsonObject ores = profile.getAsJsonObject("ores");
		JsonObject sprinkles = ores.getAsJsonObject(SPRINKLE_RULE.toString());
		JsonObject control = ores.getAsJsonObject(CONTROL_RULE.toString());
		ServerLevel level = helper.getLevel();

		if ("baseline".equals(phase)) {
			require(helper, !retrogen.get("enabled").getAsBoolean()
					&& retrogen.get("revision").getAsInt() == 0
					&& !sprinkles.get("enabled").getAsBoolean()
					&& sprinkles.get("retrogen").getAsBoolean()
					&& !control.get("enabled").getAsBoolean()
					&& !control.get("retrogen").getAsBoolean(),
					"Baseline retrogen fixtures were not inert");
			LevelChunk eligible = level.getChunk(ELIGIBLE_CHUNK.x,
					ELIGIBLE_CHUNK.z);
			LevelChunk ineligible = level.getChunk(INELIGIBLE_CHUNK.x,
					INELIGIBLE_CHUNK.z);
			fillLayer(eligible, CakeWorldBlocks.BISCUIT_STONE.get());
			fillLayer(ineligible, Blocks.BRICKS);
			BlockPos eligibleSentinel = sentinel(ELIGIBLE_CHUNK);
			BlockPos ineligibleSentinel = sentinel(INELIGIBLE_CHUNK);
			level.setBlockAndUpdate(eligibleSentinel, Blocks.BRICKS.defaultBlockState());
			level.setBlockAndUpdate(ineligibleSentinel, Blocks.BRICKS.defaultBlockState());
			LayerSurvey eligibleSurvey = survey(eligible);
			LayerSurvey ineligibleSurvey = survey(ineligible);
			require(helper, eligibleSurvey.biscuit == 256
					&& eligibleSurvey.sprinkles == 0
					&& eligibleSurvey.control == 0
					&& ineligibleSurvey.bricks == 256
					&& ineligibleSurvey.sprinkles == 0
					&& ineligibleSurvey.control == 0,
					"Baseline slabs were not exact: eligible="
							+ eligibleSurvey + ", ineligible="
							+ ineligibleSurvey);
			LOGGER.info("Sampler retrogen baseline: eligibleChunk={}, ineligibleChunk={}, eligible={}, ineligible={}, sentinels=true",
					ELIGIBLE_CHUNK, INELIGIBLE_CHUNK, eligibleSurvey,
					ineligibleSurvey);
			helper.succeed();
			return;
		}

		require(helper, retrogen.get("enabled").getAsBoolean()
				&& !retrogen.get("force").getAsBoolean()
				&& retrogen.get("revision").getAsInt() == 5301
				&& retrogen.get("chunks_per_tick").getAsInt() == 4
				&& sprinkles.get("enabled").getAsBoolean()
				&& sprinkles.get("retrogen").getAsBoolean()
				&& control.get("enabled").getAsBoolean()
				&& !control.get("retrogen").getAsBoolean(),
				"Copied profile did not activate only the intended retrogen rule");
		level.setChunkForced(ELIGIBLE_CHUNK.x, ELIGIBLE_CHUNK.z, true);
		level.setChunkForced(INELIGIBLE_CHUNK.x, INELIGIBLE_CHUNK.z, true);
		LevelChunk eligible = level.getChunk(ELIGIBLE_CHUNK.x,
				ELIGIBLE_CHUNK.z);
		LevelChunk ineligible = level.getChunk(INELIGIBLE_CHUNK.x,
				INELIGIBLE_CHUNK.z);
		if ("apply".equals(phase)) {
			int eligibleCommandQueue = queueLoadedChunk(level, ELIGIBLE_CHUNK);
			int ineligibleCommandQueue = queueLoadedChunk(level, INELIGIBLE_CHUNK);
			LOGGER.info("Sampler retrogen queue diagnostic: eligibleCommandQueue={}, ineligibleCommandQueue={}",
					eligibleCommandQueue, ineligibleCommandQueue);
			require(helper, eligibleCommandQueue == 0 && ineligibleCommandQueue == 0,
					"Copied chunks were not automatically queued on load");
		}
		int delay = "apply".equals(phase) ? RETROGEN_WAIT_TICKS : 80;
		helper.runAfterDelay(delay, () -> {
			try {
				LayerSurvey eligibleSurvey = survey(eligible);
				LayerSurvey ineligibleSurvey = survey(ineligible);
				require(helper, eligibleSurvey.sprinkles == 64
						&& eligibleSurvey.biscuit == 192
						&& eligibleSurvey.control == 0,
						"Retrogen did not retain the exact eligible Sprinkle result: "
								+ eligibleSurvey);
				require(helper, ineligibleSurvey.bricks == 256
						&& ineligibleSurvey.sprinkles == 0
						&& ineligibleSurvey.control == 0,
						"Retrogen changed the ineligible Brick control: "
								+ ineligibleSurvey);
				require(helper,
						level.getBlockState(sentinel(ELIGIBLE_CHUNK)).is(Blocks.BRICKS)
						&& level.getBlockState(sentinel(INELIGIBLE_CHUNK))
								.is(Blocks.BRICKS),
						"Copied-world Brick sentinels did not survive retrogen");
				LOGGER.info("Sampler retrogen {}: revision=5301 eligibleChunk={}, ineligibleChunk={}, eligible={}, ineligible={}, sentinels=true",
						phase, ELIGIBLE_CHUNK, INELIGIBLE_CHUNK, eligibleSurvey,
						ineligibleSurvey);
				helper.succeed();
			} finally {
				level.setChunkForced(ELIGIBLE_CHUNK.x,
						ELIGIBLE_CHUNK.z, false);
				level.setChunkForced(INELIGIBLE_CHUNK.x,
						INELIGIBLE_CHUNK.z, false);
			}
		});
	}

	private static int queueLoadedChunk(ServerLevel level, ChunkPos chunk) {
		BlockPos center = new BlockPos(chunk.getMiddleBlockX(), EVIDENCE_Y,
				chunk.getMiddleBlockZ());
		return level.getServer().getCommands().performCommand(
				level.getServer().createCommandSourceStack()
						.withLevel(level)
						.withPosition(Vec3.atCenterOf(center))
						.withPermission(4),
				"orespawn retrogen 0");
	}

	private static void fillLayer(LevelChunk chunk, Block block) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				cursor.set(chunk.getPos().getBlockX(x), EVIDENCE_Y,
						chunk.getPos().getBlockZ(z));
				chunk.setBlockState(cursor, block.defaultBlockState(), false);
			}
		}
		chunk.setUnsaved(true);
	}

	private static LayerSurvey survey(LevelChunk chunk) {
		LayerSurvey result = new LayerSurvey();
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				cursor.set(chunk.getPos().getBlockX(x), EVIDENCE_Y,
						chunk.getPos().getBlockZ(z));
				Block block = chunk.getBlockState(cursor).getBlock();
				if (block == CakeWorldBlocks.BISCUIT_STONE.get()) result.biscuit++;
				if (block == Blocks.BRICKS) result.bricks++;
				if (block == CakeWorldBlocks.SPRINKLE_CLUSTER.get()) result.sprinkles++;
				if (block == CakeWorldBlocks.FIZZY_PEARL.get()) result.control++;
			}
		}
		return result;
	}

	private static BlockPos sentinel(ChunkPos chunk) {
		return new BlockPos(chunk.getMinBlockX(), EVIDENCE_Y + 1,
				chunk.getMinBlockZ());
	}

	private static void require(GameTestHelper helper, boolean condition,
			String message) {
		if (!condition) {
			helper.fail(message);
			throw new IllegalStateException(message);
		}
	}

	private static final class LayerSurvey {
		private int biscuit;
		private int bricks;
		private int sprinkles;
		private int control;

		@Override
		public String toString() {
			return "LayerSurvey[biscuit=" + biscuit + ", bricks=" + bricks
					+ ", sprinkles=" + sprinkles + ", control=" + control
					+ "]";
		}
	}
}
