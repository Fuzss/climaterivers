package com.fuzs.biomerivers.util.shape;

import net.minecraft.util.math.vector.Vector3d;

import java.util.stream.Stream;

public class VoxelUtils {

    public static Vector3d[] scaleDown(Vector3d[] edges) {

        return Stream.of(edges).map(edge -> edge.scale(0.0625)).toArray(Vector3d[]::new);
    }

    public static Vector3d[] flipX(Vector3d[] edges) {

        return Stream.of(edges).map(edge -> new Vector3d(1.0 - edge.x, edge.y, 1.0 - edge.z)).toArray(Vector3d[]::new);
    }

    public static Vector3d[] flipZ(Vector3d[] edges) {

        return Stream.of(edges).map(edge -> new Vector3d(edge.z, edge.y, edge.x)).toArray(Vector3d[]::new);
    }

    public static Vector3d[] flipBoth(Vector3d[] edges) {

        return flipZ(flipX(edges));
    }

    /**
     * @param corners provided edges as top left, top right, bottom left and bottom right
     * @return vectors as pairs representing the edges
     */
    public static Vector3d[] create12Edges(Vector3d[] corners) {

        assert corners.length == 12 : "Amount of corners must be 12";

        return new Vector3d[]{

                // skew side
                corners[0], corners[1],
                corners[1], corners[3],
                corners[3], corners[2],
                corners[2], corners[0],

                // connections between skew sides
                corners[0], corners[4],
                corners[1], corners[5],
                corners[2], corners[6],
                corners[3], corners[7],

                // other skew side
                corners[4], corners[5],
                corners[5], corners[7],
                corners[7], corners[6],
                corners[6], corners[4]
        };
    }

}
