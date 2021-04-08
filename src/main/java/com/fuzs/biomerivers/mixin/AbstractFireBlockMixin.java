package com.fuzs.biomerivers.mixin;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(AbstractFireBlock.class)
public abstract class AbstractFireBlockMixin extends Block {

    public AbstractFireBlockMixin(Properties properties) {

        super(properties);
    }

    @Redirect(method = "shouldLightPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/block/Block;)Z"))
    private static boolean isIn(BlockState state, Block tagIn) {

        return state.isIn(Tags.Blocks.OBSIDIAN);
    }

}
