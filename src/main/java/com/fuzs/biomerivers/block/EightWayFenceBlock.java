package com.fuzs.biomerivers.block;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;

import net.minecraft.block.SixWayBlock;

public class EightWayFenceBlock {

    public static int getIndex(Object2IntMap<BlockState> statePaletteMap, BlockState state) {

        return statePaletteMap.computeIntIfAbsent(state, EightWayFenceBlock::getStateIndex);
    }

    private static int getStateIndex(BlockState stateIn) {

        int index = 0;
        if (stateIn.get(SixWayBlock.NORTH)) {

            index |= EightWayDirection.NORTH.getHorizontalIndex();
        }

        if (stateIn.get(SixWayBlock.EAST)) {

            index |= EightWayDirection.EAST.getHorizontalIndex();
        }

        if (stateIn.get(SixWayBlock.SOUTH)) {

            index |= EightWayDirection.SOUTH.getHorizontalIndex();
        }

        if (stateIn.get(SixWayBlock.WEST)) {

            index |= EightWayDirection.WEST.getHorizontalIndex();
        }

        if (stateIn.get(IEightWayBlock.NORTH_EAST)) {

            index |= EightWayDirection.NORTH_EAST.getHorizontalIndex();
        }

        if (stateIn.get(IEightWayBlock.SOUTH_EAST)) {

            index |= EightWayDirection.SOUTH_EAST.getHorizontalIndex();
        }

        if (stateIn.get(IEightWayBlock.SOUTH_WEST)) {

            index |= EightWayDirection.SOUTH_WEST.getHorizontalIndex();
        }

        if (stateIn.get(IEightWayBlock.NORTH_WEST)) {

            index |= EightWayDirection.NORTH_WEST.getHorizontalIndex();
        }

        return index;
    }

}
