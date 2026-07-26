package com.mcmoddev.cakeworld.world;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.mcmoddev.cakeworld.CakeWorld;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Serializable, correctly bounded pool wrapper for procedural CakeWorld
 * structures.
 *
 * <p>Vanilla's {@code FeaturePoolElement} deliberately reports a zero-sized
 * box. That is suitable for small decoration but not for a structure-wide
 * spawn override: Minecraft would consider only the anchor point part of the
 * structure. This element records an inclusive maximum offset and places its
 * feature exactly once while retaining the full saved piece bounds.</p>
 */
public final class CakeWorldFeaturePoolElement
		extends StructurePoolElement {
	public static final ResourceLocation TYPE_ID =
			new ResourceLocation(CakeWorld.MODID,
					"bounded_feature_pool_element");
	public static final Codec<CakeWorldFeaturePoolElement> CODEC =
			RecordCodecBuilder.create(instance ->
					instance.group(
							PlacedFeature.CODEC.fieldOf("feature")
									.forGetter(element ->
											element.feature),
							Codec.INT.fieldOf("size_x")
									.forGetter(element ->
											element.maximumOffset
													.getX()),
							Codec.INT.fieldOf("size_y")
									.forGetter(element ->
											element.maximumOffset
													.getY()),
							Codec.INT.fieldOf("size_z")
									.forGetter(element ->
											element.maximumOffset
													.getZ()),
							projectionCodec())
							.apply(instance,
									CakeWorldFeaturePoolElement::new));
	private static StructurePoolElementType<CakeWorldFeaturePoolElement>
			type;

	private final Holder<PlacedFeature> feature;
	private final Vec3i maximumOffset;

	private CakeWorldFeaturePoolElement(
			Holder<PlacedFeature> feature,
			int sizeX, int sizeY, int sizeZ,
			StructureTemplatePool.Projection projection) {
		super(projection);
		if (sizeX < 0 || sizeY < 0 || sizeZ < 0) {
			throw new IllegalArgumentException(
					"Structure bounds cannot be negative");
		}
		this.feature = feature;
		this.maximumOffset =
				new Vec3i(sizeX, sizeY, sizeZ);
	}

	public static void registerType() {
		if (type != null) {
			return;
		}
		type = Registry.register(
				Registry.STRUCTURE_POOL_ELEMENT,
				TYPE_ID,
				(StructurePoolElementType<
						CakeWorldFeaturePoolElement>)
						() -> CODEC);
	}

	public static Function<
			StructureTemplatePool.Projection,
			CakeWorldFeaturePoolElement> of(
					Holder<PlacedFeature> feature,
					Vec3i maximumOffset) {
		return projection ->
				new CakeWorldFeaturePoolElement(
						feature,
						maximumOffset.getX(),
						maximumOffset.getY(),
						maximumOffset.getZ(),
						projection);
	}

	@Override
	public Vec3i getSize(
			StructureManager structureManager,
			Rotation rotation) {
		return maximumOffset;
	}

	@Override
	public List<StructureTemplate.StructureBlockInfo>
			getShuffledJigsawBlocks(
					StructureManager structureManager,
					BlockPos position,
					Rotation rotation,
					Random random) {
		return List.of();
	}

	@Override
	public BoundingBox getBoundingBox(
			StructureManager structureManager,
			BlockPos position,
			Rotation rotation) {
		return new BoundingBox(
				position.getX(), position.getY(),
				position.getZ(),
				position.getX()
						+ maximumOffset.getX(),
				position.getY()
						+ maximumOffset.getY(),
				position.getZ()
						+ maximumOffset.getZ());
	}

	@Override
	public boolean place(
			StructureManager structureManager,
			WorldGenLevel world,
			StructureFeatureManager featureManager,
			ChunkGenerator chunkGenerator,
			BlockPos position,
			BlockPos pivot,
			Rotation rotation,
			BoundingBox generationBounds,
			Random random,
			boolean keepJigsaws) {
		ConfiguredFeature<?, ?> configured =
				feature.value().feature().value();
		if (configured.feature()
				instanceof CakeWorldBoundedStructureFeature) {
			return ((CakeWorldBoundedStructureFeature)
					configured.feature()).placeInBounds(
							world, chunkGenerator,
							random, position,
							generationBounds);
		}
		// A multi-chunk piece receives one post-process call per intersecting
		// chunk. Only the chunk containing the saved anchor owns the
		// procedural placement, preventing duplicate blocks and entities.
		if (!generationBounds.isInside(position)) {
			return true;
		}
		return feature.value().place(
				world, chunkGenerator, random, position);
	}

	@Override
	public StructurePoolElementType<?> getType() {
		if (type == null) {
			throw new IllegalStateException(
					"CakeWorld bounded pool element was not registered");
		}
		return type;
	}

	public Vec3i maximumOffset() {
		return maximumOffset;
	}
}
