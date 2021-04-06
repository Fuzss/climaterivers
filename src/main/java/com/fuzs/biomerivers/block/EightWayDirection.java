package com.fuzs.biomerivers.block;

public enum EightWayDirection {

    SOUTH, WEST, NORTH, EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST, SOUTH_EAST;

    private final int horizontalIndex = 1 << this.ordinal();

    public int getHorizontalIndex() {

        return this.horizontalIndex;
    }

}
