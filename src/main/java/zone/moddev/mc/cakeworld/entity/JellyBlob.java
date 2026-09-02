package zone.moddev.mc.cakeworld.entity;

import java.util.Random;

import zone.moddev.mc.cakeworld.CakeWorld;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.WorldgenRandom;

/**
 * CakeWorld's genuine Slime role.
 *
 * <p>Size scaling, movement, splitting, targets, sounds and size-gated loot
 * remain inherited. The spawn predicate preserves vanilla slime chunks and
 * redirects the surface Swamp branch to future Caramel Bogs.</p>
 */
public class JellyBlob extends Slime {
	private static final String CARAMEL_BOGS =
			"caramel_bogs";

	public JellyBlob(
			EntityType<? extends Slime> type, Level level) {
		super(type, level);
	}

	public static boolean checkJellyBlobSpawnRules(
			EntityType<JellyBlob> type,
			LevelAccessor level, MobSpawnType reason,
			BlockPos pos, Random random) {
		if (!allowsNaturalSpawn(level.getDifficulty())) {
			return false;
		}

		if (isSurfaceJellyBiome(level, pos)
				&& pos.getY() > 50
				&& pos.getY() < 70
				&& random.nextFloat() < 0.5F
				&& random.nextFloat()
						< level.getMoonBrightness()
				&& level.getMaxLocalRawBrightness(pos)
						<= random.nextInt(8)) {
			return Mob.checkMobSpawnRules(
					type, level, reason, pos, random);
		}

		if (!(level instanceof WorldGenLevel world)) {
			return false;
		}
		ChunkPos chunk = new ChunkPos(pos);
		return random.nextInt(10) == 0
				&& isSlimeChunk(world.getSeed(), chunk)
				&& pos.getY() < 40
				&& Mob.checkMobSpawnRules(
						type, level, reason,
						pos, random);
	}

	/**
	 * Matches the vanilla Slime chunk test exactly, including its salt.
	 */
	public static boolean isSlimeChunk(
			long worldSeed, ChunkPos chunk) {
		return WorldgenRandom.seedSlimeChunk(
				chunk.x, chunk.z, worldSeed,
				987234911L).nextInt(10) == 0;
	}

	public static boolean allowsNaturalSpawn(
			Difficulty difficulty) {
		return difficulty != Difficulty.PEACEFUL;
	}

	public static boolean isCakeWorldSurfaceJellyBiome(
			ResourceLocation biome) {
		return biome != null
				&& CakeWorld.MODID.equals(
						biome.getNamespace())
				&& CARAMEL_BOGS.equals(
						biome.getPath());
	}

	private static boolean isSurfaceJellyBiome(
			LevelAccessor level, BlockPos pos) {
		if (level.getBiome(pos).is(Biomes.SWAMP)) {
			return true;
		}
		ResourceLocation biome = level.getBiome(pos)
				.unwrapKey()
				.map(key -> key.location())
				.orElse(null);
		return isCakeWorldSurfaceJellyBiome(biome);
	}
}
