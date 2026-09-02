package zone.moddev.mc.cakeworld.block;

import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Fortress-grown cinnamon shoots with the exact Nether-Wart growth contract.
 *
 * <p>The block deliberately keeps Soul Sand as its soil and drops vanilla
 * Nether Wart through its loot table. Brewing, replanting and advancement
 * progression therefore remain native even while generated fortress farms
 * receive a CakeWorld identity.</p>
 */
public final class CinnamonWartBlock extends NetherWartBlock {
	public CinnamonWartBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}
}
