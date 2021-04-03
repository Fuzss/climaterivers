package com.fuzs.biomerivers.mixin;

import com.fuzs.biomerivers.element.ClimateRiversElement;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.MixRiverLayer;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(MixRiverLayer.class)
public abstract class MixRiverLayerMixin implements IDimOffset0Transformer {

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    public void apply(INoiseRandom random, IArea originalArea, IArea riverArea, int posX, int posZ, CallbackInfoReturnable<Integer> callbackInfo) {

        callbackInfo.cancel();
        int originalAreaId = originalArea.getValue(this.getOffsetX(posX), this.getOffsetZ(posZ));
        int riverAreaId = riverArea.getValue(this.getOffsetX(posX), this.getOffsetZ(posZ));

        if (isOcean(originalAreaId)) {

            callbackInfo.setReturnValue(originalAreaId);
        } else if (riverAreaId == 7) {

            int riverBiomeId = ClimateRiversElement.BIOME_TO_RIVER_MAP.get(originalAreaId);
            if (riverBiomeId != 0) {

                callbackInfo.setReturnValue(riverBiomeId & 255);
            } else if (originalAreaId == 12) {

                callbackInfo.setReturnValue(11);
            } else {

                callbackInfo.setReturnValue(originalAreaId != 14 && originalAreaId != 15 ? riverAreaId & 255 : 15);
            }
        } else {

            callbackInfo.setReturnValue(originalAreaId);
        }
    }

    private static boolean isOcean(int biomeId) {

        return biomeId == 44 || biomeId == 45 || biomeId == 0 || biomeId == 46 || biomeId == 10 || biomeId == 47 || biomeId == 48 || biomeId == 24 || biomeId == 49 || biomeId == 50;
    }

}
