package fuzs.climaterivers.mixin;

import fuzs.climaterivers.init.ModBiomes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeData.class)
abstract class BiomeDataMixin {

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(BootstrapContext<Biome> context, CallbackInfo callback) {
        // all biomes are gathered for validation when built-in data packs are created, so our content must be present then as well,
        // as our biomes are added via mixin directly to vanilla code
        ModBiomes.bootstrap(context);
    }
}
