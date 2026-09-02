package zone.moddev.mc.cakeworld.world;

import zone.moddev.mc.cakeworld.init.CakeWorldBlocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Pure palette mappings retained for focused compatibility tests.
 *
 * <p>Natural Burnt-Toffee columns are generated directly by
 * {@link BurntToffeeColumnsFeature}; they are no longer repaired after chunk
 * generation. Black-Liquorice Labyrinths likewise use native terrain and
 * vegetation features. Keeping these mappings side-effect free documents the
 * intended substitutions without risking reload replay or asynchronous chunk
 * storage races.</p>
 */
public final class BurntToffeeDeltasPalette {
	public static final int MAX_NETHER_TERRAIN_Y = 127;

	private BurntToffeeDeltasPalette() {
	}

	public static BlockState convertedState(BlockState source) {
		if (source.is(Blocks.BASALT)
				|| source.is(Blocks.SMOOTH_BASALT)) {
			BlockState pillar = CakeWorldBlocks
					.BURNT_TOFFEE_PILLAR.get()
					.defaultBlockState();
			Direction.Axis axis = source.hasProperty(
					RotatedPillarBlock.AXIS)
							? source.getValue(RotatedPillarBlock.AXIS)
							: Direction.Axis.Y;
			return pillar.setValue(RotatedPillarBlock.AXIS, axis);
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

	public static BlockState blackLiquoriceConvertedState(
			BlockState source) {
		if (source.is(Blocks.WARPED_NYLIUM)) {
			return CakeWorldBlocks.BLACK_LIQUORICE_STONE.get()
					.defaultBlockState();
		}
		if (source.is(Blocks.WARPED_STEM)
				|| source.is(Blocks.WARPED_HYPHAE)
				|| source.is(Blocks.STRIPPED_WARPED_STEM)
				|| source.is(Blocks.STRIPPED_WARPED_HYPHAE)) {
			BlockState root = CakeWorldBlocks.LIQUORICE_ROOT.get()
					.defaultBlockState();
			Direction.Axis axis = source.hasProperty(
					RotatedPillarBlock.AXIS)
							? source.getValue(RotatedPillarBlock.AXIS)
							: Direction.Axis.Y;
			return root.setValue(RotatedPillarBlock.AXIS, axis);
		}
		if (source.is(Blocks.WARPED_WART_BLOCK)) {
			return CakeWorldBlocks.LIQUORICE_ROOT.get()
					.defaultBlockState();
		}
		if (source.is(Blocks.WARPED_ROOTS)
				|| source.is(Blocks.CRIMSON_ROOTS)
				|| source.is(Blocks.NETHER_SPROUTS)
				|| source.is(Blocks.WARPED_FUNGUS)
				|| source.is(Blocks.TWISTING_VINES)
				|| source.is(Blocks.TWISTING_VINES_PLANT)) {
			return CakeWorldBlocks.BLACK_LIQUORICE_TANGLE.get()
					.defaultBlockState();
		}
		if (source.is(Blocks.SHROOMLIGHT)) {
			return CakeWorldBlocks.MINT_CRYSTAL.get()
					.defaultBlockState();
		}
		return source;
	}
}
