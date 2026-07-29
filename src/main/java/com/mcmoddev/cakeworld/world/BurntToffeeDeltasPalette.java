package com.mcmoddev.cakeworld.world;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mcmoddev.cakeworld.init.CakeWorldBiomes;
import com.mcmoddev.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Applies the final edible palette to newly generated Burnt-Toffee Deltas.
 *
 * <p>Basalt-Deltas decoration can cross a chunk boundary after the receiving
 * chunk's own feature steps finish. This coordinator therefore waits until a
 * new chunk and all eight neighbours are fully loaded, then translates only
 * explicit vanilla source blocks inside Burnt-Toffee Deltas. A persistent
 * per-chunk state prevents reload replay and makes unmarked chunks loaded from
 * an existing world ineligible, so existing worlds and later player edits are
 * never silently rewritten.</p>
 */
@Mod.EventBusSubscriber(modid = CakeWorld.MODID)
public final class BurntToffeeDeltasPalette {
	public static final int MAX_NETHER_TERRAIN_Y = 127;
	private static final String PERSISTENT_KEY =
			"cakeworld_burnt_toffee_deltas_palette";
	private static final String STATE_KEY = "state";
	private static final Queue<ChunkKey> PENDING =
			new ConcurrentLinkedQueue<>();
	private static final Set<ChunkKey> QUEUED =
			ConcurrentHashMap.newKeySet();
	private static final ConcurrentMap<ChunkKey, PaletteState>
			STATES = new ConcurrentHashMap<>();
	private static final ResourceKey<Biome> DELTAS_KEY =
			ResourceKey.create(Registry.BIOME_REGISTRY,
					CakeWorldBiomes.BURNT_TOFFEE_DELTAS.getId());

	private BurntToffeeDeltasPalette() {
	}

	@SubscribeEvent
	public static void onChunkDataLoad(ChunkDataEvent.Load event) {
		if (event.getStatus() != ChunkStatus.ChunkType.LEVELCHUNK
				|| !(event.getWorld() instanceof ServerLevel level)
				|| !level.dimension().equals(Level.NETHER)) {
			return;
		}
		ChunkKey key = key(level, event.getChunk().getPos());
		PaletteState state = PaletteState.EXISTING;
		if (event.getData().contains(PERSISTENT_KEY,
				Tag.TAG_COMPOUND)) {
			state = PaletteState.fromId(event.getData()
					.getCompound(PERSISTENT_KEY)
					.getString(STATE_KEY));
		}
		STATES.put(key, state);
	}

	@SubscribeEvent
	public static void onChunkDataSave(ChunkDataEvent.Save event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !level.dimension().equals(Level.NETHER)) {
			return;
		}
		PaletteState state = STATES.get(
				key(level, event.getChunk().getPos()));
		if (state != PaletteState.PENDING
				&& state != PaletteState.CONVERTED) {
			return;
		}
		CompoundTag persistent = new CompoundTag();
		persistent.putString(STATE_KEY, state.id());
		event.getData().put(PERSISTENT_KEY, persistent);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk() instanceof LevelChunk chunk)
				|| !level.dimension().equals(Level.NETHER)) {
			return;
		}
		ChunkKey key = key(level, chunk.getPos());
		PaletteState state = STATES.computeIfAbsent(key,
				ignored -> PaletteState.PENDING);
		if (state == PaletteState.PENDING) {
			schedulePendingAround(key);
		}
	}

	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event) {
		if (!(event.getWorld() instanceof ServerLevel level)
				|| !(event.getChunk() instanceof LevelChunk chunk)
				|| !level.dimension().equals(Level.NETHER)) {
			return;
		}
		ChunkKey key = key(level, chunk.getPos());
		STATES.remove(key);
		QUEUED.remove(key);
	}

	@SubscribeEvent
	public static void onServerTick(
			TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.START
				|| PENDING.isEmpty()) {
			return;
		}
		MinecraftServer server =
				ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return;
		}
		int pendingAtStart = Math.min(PENDING.size(), 256);
		for (int index = 0; index < pendingAtStart; index++) {
			ChunkKey pending = PENDING.poll();
			if (pending == null) {
				break;
			}
			QUEUED.remove(pending);
			if (STATES.get(pending) != PaletteState.PENDING) {
				continue;
			}
			ServerLevel level = server.getLevel(
					pending.dimension());
			if (level == null
					|| !neighboursReady(level,
							pending.chunk())) {
				continue;
			}
			LevelChunk chunk = level.getChunk(
					pending.chunk().x,
					pending.chunk().z);
			convertChunkSlice(level, chunk.getPos());
			STATES.put(pending, PaletteState.CONVERTED);
			chunk.setUnsaved(true);
		}
	}

	@SubscribeEvent
	public static void onServerStopped(ServerStoppedEvent event) {
		PENDING.clear();
		QUEUED.clear();
		STATES.clear();
	}

	public static int convertChunkSlice(
			WorldGenLevel world, ChunkPos chunk) {
		if (!containsDeltas(world, chunk)) {
			return 0;
		}
		int changed = 0;
		int minY = Math.max(world.getMinBuildHeight(), 0);
		int maxY = Math.min(
				world.getMaxBuildHeight() - 1,
				MAX_NETHER_TERRAIN_Y);
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos();
		for (int x = chunk.getMinBlockX();
				x <= chunk.getMaxBlockX(); x++) {
			for (int z = chunk.getMinBlockZ();
					z <= chunk.getMaxBlockZ(); z++) {
				for (int y = minY; y <= maxY; y++) {
					cursor.set(x, y, z);
					if (!world.getBiome(cursor).is(DELTAS_KEY)) {
						continue;
					}
					BlockState source =
							world.getBlockState(cursor);
					BlockState target =
							convertedState(source);
					if (target != source) {
						world.setBlock(cursor, target, 2);
						changed++;
					}
				}
			}
		}
		return changed;
	}

	/**
	 * Checks every Nether quart cell before entering the blockwise hot path.
	 */
	private static boolean containsDeltas(
			WorldGenLevel world, ChunkPos chunk) {
		int minY = Math.max(world.getMinBuildHeight(), 0);
		int maxY = Math.min(
				world.getMaxBuildHeight() - 1,
				MAX_NETHER_TERRAIN_Y);
		BlockPos.MutableBlockPos cursor =
				new BlockPos.MutableBlockPos();
		for (int x = chunk.getMinBlockX();
				x <= chunk.getMaxBlockX(); x += 4) {
			for (int z = chunk.getMinBlockZ();
					z <= chunk.getMaxBlockZ(); z += 4) {
				for (int y = minY; y <= maxY; y += 4) {
					cursor.set(x, y, z);
					if (world.getBiome(cursor).is(DELTAS_KEY)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static BlockState convertedState(BlockState source) {
		if (source.is(Blocks.BASALT)
				|| source.is(Blocks.SMOOTH_BASALT)) {
			BlockState pillar = CakeWorldBlocks
					.BURNT_TOFFEE_PILLAR.get()
					.defaultBlockState();
			Direction.Axis axis =
					source.hasProperty(
							RotatedPillarBlock.AXIS)
							? source.getValue(
									RotatedPillarBlock.AXIS)
							: Direction.Axis.Y;
			return pillar.setValue(
					RotatedPillarBlock.AXIS, axis);
		}
		if (source.is(Blocks.BLACKSTONE)) {
			return CakeWorldBlocks.BURNT_SUGAR_ROCK.get()
					.defaultBlockState();
		}
		if (source.is(Blocks.GRAVEL)) {
			return CakeWorldBlocks.CRUNCHY_TOFFEE_ASH.get()
					.defaultBlockState();
		}
		return source;
	}

	private static boolean neighboursReady(
			ServerLevel level, ChunkPos chunk) {
		for (int chunkX = chunk.x - 1;
				chunkX <= chunk.x + 1; chunkX++) {
			for (int chunkZ = chunk.z - 1;
					chunkZ <= chunk.z + 1; chunkZ++) {
				if (!level.hasChunk(chunkX, chunkZ)) {
					return false;
				}
			}
		}
		return true;
	}

	private static void schedulePendingAround(ChunkKey centre) {
		for (int chunkX = centre.chunk().x - 1;
				chunkX <= centre.chunk().x + 1; chunkX++) {
			for (int chunkZ = centre.chunk().z - 1;
					chunkZ <= centre.chunk().z + 1; chunkZ++) {
				ChunkKey candidate = new ChunkKey(
						centre.dimension(),
						new ChunkPos(chunkX, chunkZ));
				if (STATES.get(candidate)
						== PaletteState.PENDING
						&& QUEUED.add(candidate)) {
					PENDING.add(candidate);
				}
			}
		}
	}

	private static ChunkKey key(
			ServerLevel level, ChunkPos chunk) {
		return new ChunkKey(level.dimension(), chunk);
	}

	private enum PaletteState {
		PENDING("pending"),
		CONVERTED("converted"),
		EXISTING("existing");

		private final String id;

		PaletteState(String id) {
			this.id = id;
		}

		private String id() {
			return id;
		}

		private static PaletteState fromId(String id) {
			return PENDING.id.equals(id)
					? PENDING
					: CONVERTED.id.equals(id)
							? CONVERTED
							: EXISTING;
		}
	}

	private record ChunkKey(
			ResourceKey<Level> dimension, ChunkPos chunk) {
	}
}
