package com.fuzs.biomerivers.util.shape;

import com.fuzs.biomerivers.mixin.accessor.IVoxelShapeAccessor;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.SplitVoxelShape;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class NoneVoxelShape extends SplitVoxelShape {

    private final IVoxelShapeAccessor valuesAccessor;
    private final List<Vector3d> edges;

    public NoneVoxelShape(VoxelShape voxelShape, List<Vector3d> edges) {

        super(voxelShape, Direction.Axis.X, 0);
        this.valuesAccessor = (IVoxelShapeAccessor) voxelShape;
        ((IVoxelShapeAccessor) this).setPart(this.valuesAccessor.getPart());

        assert edges.size() % 2 == 0 : "Edges must be in groups of two points";
        this.edges = edges;
    }

    @Override
    public DoubleList getValues(Direction.Axis axis) {

        return this.valuesAccessor.callGetValues(axis);
    }

    @Override
    public void forEachEdge(VoxelShapes.ILineConsumer boxConsumer) {

        Iterator<Vector3d> iter = this.edges.iterator();
        while (iter.hasNext()) {

            Vector3d p1 = iter.next();
            Vector3d p2 = iter.next();
            boxConsumer.consume(
                    p1.x, p1.y, p1.z,
                    p2.x, p2.y, p2.z
            );
        }
    }

}
