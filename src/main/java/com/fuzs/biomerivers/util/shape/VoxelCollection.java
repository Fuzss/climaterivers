package com.fuzs.biomerivers.util.shape;

import com.fuzs.biomerivers.mixin.accessor.IVoxelShapeAccessor;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.List;

public class VoxelCollection extends ExtensibleVoxelShape {

    private VoxelShape voxelProvider;
    private final List<NoneVoxelShape> noneVoxels = Lists.newArrayList();

    public VoxelCollection() {

        this(VoxelShapes.empty());
    }

    public VoxelCollection(VoxelShape voxelProvider) {

        super(voxelProvider);
        this.voxelProvider = voxelProvider;
    }

    @Override
    protected DoubleList getValues(Direction.Axis axis) {

        return ((IVoxelShapeAccessor) this.voxelProvider).callGetValues(axis);
    }

    private void setVoxelProvider(VoxelShape voxelShape) {

        this.voxelProvider = voxelShape;
        this.setPart(((IVoxelShapeAccessor) this.voxelProvider).getPart());
    }

    public VoxelCollection addVoxelShape(VoxelShape voxelShape) {

        this.setVoxelProvider(VoxelShapes.or(this.voxelProvider, voxelShape));
        return this;
    }

    public VoxelCollection addNoneVoxelShape(NoneVoxelShape voxelShape) {

        this.noneVoxels.add(voxelShape);
        // combine collision shapes
        return this.addVoxelShape(voxelShape);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void forEachEdge(VoxelShapes.ILineConsumer boxConsumer) {

        this.voxelProvider.forEachEdge(boxConsumer);
        this.noneVoxels.forEach(voxelShape -> voxelShape.forEachEdge(boxConsumer));
    }

}
