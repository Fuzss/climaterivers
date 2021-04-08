package com.fuzs.biomerivers.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings("unused")
@Mixin(IForgeBlock.class)
public interface IForgeBlockMixin extends IForgeBlock {

    @Override
    default boolean isPortalFrame(BlockState state, IBlockReader world, BlockPos pos) {

        return state.isIn(Tags.Blocks.OBSIDIAN);
    }

}
