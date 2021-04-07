package com.fuzs.biomerivers.util.shape;

import com.fuzs.biomerivers.mixin.accessor.IVoxelShapeAccessor;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.SplitVoxelShape;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapePart;

public abstract class ExtensibleVoxelShape extends SplitVoxelShape {

    public ExtensibleVoxelShape(VoxelShape voxelProvider) {

        super(voxelProvider, Direction.Axis.X, 0);
        this.setPart(((IVoxelShapeAccessor) voxelProvider).getPart());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected abstract DoubleList getValues(Direction.Axis axis);

    protected final void setPart(VoxelShapePart part) {

        ((IVoxelShapeAccessor) this).setPart(part);
    }

}
