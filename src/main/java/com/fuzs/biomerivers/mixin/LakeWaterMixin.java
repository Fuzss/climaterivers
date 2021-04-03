package com.fuzs.biomerivers.mixin;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.element.ByeByeLakesElement;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.LakeWater;
import net.minecraft.world.gen.placement.Placement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@Mixin(LakeWater.class)
public abstract class LakeWaterMixin extends Placement<ChanceConfig> {

    public LakeWaterMixin(Codec<ChanceConfig> codec) {

        super(codec);
    }

    @Inject(method = "getPositions", at = @At("HEAD"), cancellable = true)
    public void getPositions(WorldDecoratingHelper helper, Random rand, ChanceConfig config, BlockPos pos, CallbackInfoReturnable<Stream<BlockPos>> callbackInfo) {

        ByeByeLakesElement element = (ByeByeLakesElement) BiomeRivers.BYE_BYE_LAKES;
        if (element.isEnabled()) {

            callbackInfo.setReturnValue(element.noWaterLakes ? Stream.empty() : this.getPositions(helper, rand, config, pos, element.noSurfaceWater));
        }
    }

    public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, ChanceConfig config, BlockPos pos, boolean noSurfaceWater) {

        if (rand.nextInt(config.chance) == 0) {

            int i = rand.nextInt(16) + pos.getX();
            int j = rand.nextInt(16) + pos.getZ();
            int k = rand.nextInt(helper.func_242891_a());
            if (k < helper.func_242895_b() || !noSurfaceWater) {

                return Stream.of(new BlockPos(i, k, j));
            }
        }

        return Stream.empty();
    }

}
