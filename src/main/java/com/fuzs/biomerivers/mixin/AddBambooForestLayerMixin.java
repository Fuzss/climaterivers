package com.fuzs.biomerivers.mixin;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.AddBambooForestLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(AddBambooForestLayer.class)
public abstract class AddBambooForestLayerMixin {

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    public void apply(INoiseRandom context, int value, CallbackInfoReturnable<Integer> callbackInfo) {

        callbackInfo.setReturnValue(context.random(10) == 0 && value == 2 ? 168 : value);
    }

}
