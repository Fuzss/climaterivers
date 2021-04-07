package com.fuzs.biomerivers.mixin.accessor;

import net.minecraft.util.math.shapes.DoubleRangeList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@SuppressWarnings("unused")
@Mixin(DoubleRangeList.class)
public interface IDoubleRangeListAccessor {

    @Invoker("<init>")
    static DoubleRangeList newDoubleRangeList(int capacity) {

        throw new IllegalStateException();
    }

}
