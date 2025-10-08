package fuzs.climaterivers.mixin;

import fuzs.climaterivers.init.ModPlacedFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlacementUtils.class)
abstract class PlacementUtilsMixin {

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(BootstrapContext<PlacedFeature> context, CallbackInfo callback) {
        // all biomes are gathered for validation when built-in data packs are created, so our content must be present then as well,
        // as our biomes are added via mixin directly to vanilla code
        ModPlacedFeatures.bootstrap(context);
    }
}
