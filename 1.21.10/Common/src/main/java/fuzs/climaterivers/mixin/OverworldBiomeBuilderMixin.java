package fuzs.climaterivers.mixin;

import com.mojang.datafixers.util.Pair;
import fuzs.climaterivers.handler.RiverBiomeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
abstract class OverworldBiomeBuilderMixin {
    @Shadow
    @Final
    private Climate.Parameter FULL_RANGE;
    @Shadow
    @Final
    private Climate.Parameter UNFROZEN_RANGE;

    @Inject(method = "addSurfaceBiome", at = @At("HEAD"), cancellable = true)
    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter depth, float weirdness, ResourceKey<Biome> key, CallbackInfo callback) {
        // same approach as https://github.com/jaskarth/RiverRedux/blob/master/src/main/java/supercoder79/riverredux/mixin/MixinOverworldBiomeBuilder.java
        // done via mixin instead of TerraBlender region as we want to apply to all rivers in the whole world, not just in a specific region
        // also if we were to use a region we would have a giant spot in the world with otherwise unchanged vanilla biomes which would clash with a world potentially filled with modded biome generation
        // this approach seems to work fine with TerraBlender's various overworld based biome builders, since the builders will inherit functionality from here
        // Oh The Biomes We've Gone should work fine as it uses those builders, Biomes O' Plenty however has its own builder implementation which requires dedicated integration
        if (key == Biomes.RIVER && this.UNFROZEN_RANGE.equals(temperature) && this.FULL_RANGE.equals(humidity)) {
            RiverBiomeBuilder.addRivers(OverworldBiomeBuilder.class.cast(this),
                    consumer,
                    continentalness,
                    erosion,
                    depth,
                    weirdness);
            callback.cancel();
        }
    }
}
