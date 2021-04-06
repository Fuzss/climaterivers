package com.fuzs.biomerivers.block;

import net.minecraft.util.math.vector.Vector3i;

import java.util.Arrays;
import java.util.stream.IntStream;

public enum EightWayDirection {

    SOUTH(0, -1),
    WEST(-1, 0),
    NORTH(0, 1),
    EAST(1, 0),
    SOUTH_WEST(-1, -1),
    NORTH_WEST(-1, 1),
    NORTH_EAST(1, 1),
    SOUTH_EAST(1, -1);

    private final Vector3i directionVec;
    private final int horizontalIndex = 1 << this.ordinal();

    EightWayDirection(int directionX, int directionZ) {

        this.directionVec = new Vector3i(directionX, 0, directionZ);
    }

    public Vector3i getDirectionVec() {

        return this.directionVec;
    }

    public int getHorizontalIndex() {

        return this.horizontalIndex;
    }

    public EightWayDirection getOpposite() {

        if (this.ordinal() >= 4) {

            return EightWayDirection.values()[(this.ordinal() + 2) % 4 + 4];
        }

        return EightWayDirection.values()[(this.ordinal() + 2) % 4];
    }

    public static EightWayDirection[] getCardinalDirections() {

        return Arrays.copyOf(EightWayDirection.values(), 4);
    }

    public static EightWayDirection[] getIntercardinalDirections() {

        return Arrays.copyOfRange(EightWayDirection.values(), 4, 8);
    }

}
