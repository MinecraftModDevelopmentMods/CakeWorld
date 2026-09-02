package zone.moddev.mc.cakeworld.block;

import java.util.Random;

import zone.moddev.mc.cakeworld.init.CakeWorldItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A four-flavour orchard fruit which can be picked without felling the tree.
 * Harvested fruit remains in place and regrows through random ticks or bone
 * meal, making generated orchards a renewable source of Boiled Sweets.
 */
public final class LollipopFruitBlock extends Block
		implements BonemealableBlock {
	public static final EnumProperty<Flavour> FLAVOUR =
			EnumProperty.create("flavour", Flavour.class);
	public static final BooleanProperty RIPE =
			BooleanProperty.create("ripe");

	public LollipopFruitBlock(BlockBehaviour.Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any()
				.setValue(FLAVOUR, Flavour.RASPBERRY)
				.setValue(RIPE, false));
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return !state.getValue(RIPE);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level,
			BlockPos pos, Random random) {
		if (!state.getValue(RIPE) && random.nextInt(5) == 0) {
			level.setBlock(pos, state.setValue(RIPE, true), 2);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level,
			BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult hit) {
		if (!state.getValue(RIPE)) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			popResource(level, pos,
					new ItemStack(CakeWorldItems.BOILED_SWEET.get()));
			level.setBlock(pos, state.setValue(RIPE, false), 2);
			level.playSound(null, pos,
					SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
					SoundSource.BLOCKS, 1.0F, 1.2F);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level,
			BlockPos pos, BlockState state, boolean isClient) {
		return !state.getValue(RIPE);
	}

	@Override
	public boolean isBonemealSuccess(Level level, Random random,
			BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, Random random,
			BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(RIPE, true), 2);
	}

	@Override
	protected void createBlockStateDefinition(
			StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FLAVOUR, RIPE);
	}

	public enum Flavour implements StringRepresentable {
		RASPBERRY("raspberry"),
		LEMON("lemon"),
		LIME("lime"),
		BLUEBERRY("blueberry");

		private final String name;

		Flavour(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
