package zone.moddev.mc.cakeworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Accumulates like snow but keeps every visible layer collidable so even a
 * single layer can soften a landing.
 */
public final class IcingLayerBlock extends SnowLayerBlock {
	public static final float FALL_DAMAGE_MULTIPLIER = 0.5F;

	public IcingLayerBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float distance) {
		entity.causeFallDamage(distance, FALL_DAMAGE_MULTIPLIER, DamageSource.FALL);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
			CollisionContext context) {
		return SHAPE_BY_LAYER[state.getValue(LAYERS)];
	}
}
