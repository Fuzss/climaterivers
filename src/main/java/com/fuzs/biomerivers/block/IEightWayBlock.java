package com.fuzs.biomerivers.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Util;

import java.util.Map;

public interface IEightWayBlock {

    BooleanProperty NORTH_EAST = BooleanProperty.create("north_east");
    BooleanProperty SOUTH_EAST = BooleanProperty.create("south_east");
    BooleanProperty SOUTH_WEST = BooleanProperty.create("south_west");
    BooleanProperty NORTH_WEST = BooleanProperty.create("north_west");
    Map<EightWayDirection, BooleanProperty> DIRECTION_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(EightWayDirection.class), (directions) -> {
        directions.put(EightWayDirection.NORTH, SixWayBlock.NORTH);
        directions.put(EightWayDirection.EAST, SixWayBlock.EAST);
        directions.put(EightWayDirection.SOUTH, SixWayBlock.SOUTH);
        directions.put(EightWayDirection.WEST, SixWayBlock.WEST);
        directions.put(EightWayDirection.NORTH_EAST, NORTH_EAST);
        directions.put(EightWayDirection.SOUTH_EAST, SOUTH_EAST);
        directions.put(EightWayDirection.SOUTH_WEST, SOUTH_WEST);
        directions.put(EightWayDirection.NORTH_WEST, NORTH_WEST);
    });

    default void fillEightWayStateContainer(StateContainer.Builder<Block, BlockState> builder) {

        builder.add(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    }

    default int getEightWayStateIndex(BlockState stateIn) {

        int index = 0;
        for (Map.Entry<EightWayDirection, BooleanProperty> entry : DIRECTION_TO_PROPERTY_MAP.entrySet()) {

            if (stateIn.get(entry.getValue())) {

                index |= entry.getKey().getHorizontalIndex();
            }
        }

        return index;
    }

}
