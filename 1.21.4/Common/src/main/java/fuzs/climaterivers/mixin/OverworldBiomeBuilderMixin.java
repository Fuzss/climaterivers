package fuzs.climaterivers.mixin;

import com.mojang.datafixers.util.Pair;
import fuzs.climaterivers.handler.RiverBiomeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
abstract class OverworldBiomeBuilderMixin {

    @Inject(method = "addValleys", at = @At("HEAD"), cancellable = true)
    private void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter param, CallbackInfo callback) {
        RiverBiomeBuilder.addValleys(consumer, param);
    }

    @Inject(method = "addSurfaceBiome", at = @At("HEAD"), cancellable = true)
    private void addSurfaceBiome(
            Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer,
            Climate.Parameter temperature,
            Climate.Parameter humidity,
            Climate.Parameter continentalness,
            Climate.Parameter erosion,
            Climate.Parameter depth,
            float weirdness,
            ResourceKey<Biome> key, CallbackInfo callback
    ) {
        if (key == Biomes.RIVER) callback.cancel();
    }
}
