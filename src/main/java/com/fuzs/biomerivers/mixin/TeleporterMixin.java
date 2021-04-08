package com.fuzs.biomerivers.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.Teleporter;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings("unused")
@Mixin(Teleporter.class)
public abstract class TeleporterMixin {

    @Shadow
    @Final
    protected ServerWorld world;

    @Redirect(method = "makePortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDefaultState()Lnet/minecraft/block/BlockState;"))
    public BlockState getDefaultState(Block block) {

        return block != Blocks.OBSIDIAN ? block.getDefaultState() : this.world.getRandom().nextInt(4) == 0 ? Blocks.CRYING_OBSIDIAN.getDefaultState() : block.getDefaultState();

    }

}
