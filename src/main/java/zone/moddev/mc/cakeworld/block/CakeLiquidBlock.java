package zone.moddev.mc.cakeworld.block;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

public class CakeLiquidBlock extends LiquidBlock {
	private final Supplier<? extends FlowingFluid> fluid;
	private final double horizontalDrag;
	private final double downwardDrag;

	public CakeLiquidBlock(Supplier<? extends FlowingFluid> fluid,
			BlockBehaviour.Properties properties) {
		this(fluid, properties, 1.0D, 1.0D);
	}

	public CakeLiquidBlock(Supplier<? extends FlowingFluid> fluid,
			BlockBehaviour.Properties properties, double horizontalDrag,
			double downwardDrag) {
		super(fluid, properties);
		this.fluid = fluid;
		this.horizontalDrag = boundedDrag(horizontalDrag);
		this.downwardDrag = boundedDrag(downwardDrag);
	}

	@Override
	public FlowingFluid getFluid() {
		return fluid.get();
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos,
			Entity entity) {
		super.entityInside(state, level, pos, entity);
		if (horizontalDrag >= 1.0D && downwardDrag >= 1.0D) {
			return;
		}
		Vec3 movement = entity.getDeltaMovement();
		double vertical = movement.y < 0.0D
				? movement.y * downwardDrag
				: movement.y;
		entity.setDeltaMovement(movement.x * horizontalDrag, vertical,
				movement.z * horizontalDrag);
	}

	private static double boundedDrag(double drag) {
		if (drag <= 0.0D || drag > 1.0D) {
			throw new IllegalArgumentException(
					"Sticky-fluid drag must be greater than 0 and at most 1");
		}
		return drag;
	}
}
