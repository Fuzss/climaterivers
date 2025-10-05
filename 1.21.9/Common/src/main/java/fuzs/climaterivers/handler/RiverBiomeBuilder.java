package fuzs.climaterivers.handler;

import com.mojang.datafixers.util.Pair;
import fuzs.climaterivers.init.ModBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;

import java.util.function.Consumer;

public class RiverBiomeBuilder {
    static final ResourceKey<Biome>[][] UNFROZEN_RIVERS = new ResourceKey[][]{
            {null, null, null, null, null}, {
            ModBiomes.COLD_RIVER_BIOME,
            ModBiomes.COLD_RIVER_BIOME,
            ModBiomes.COLD_RIVER_BIOME,
            ModBiomes.COLD_RIVER_BIOME,
            ModBiomes.COLD_RIVER_BIOME
    }, {Biomes.RIVER, Biomes.RIVER, Biomes.RIVER, Biomes.RIVER, Biomes.RIVER}, {
            ModBiomes.WARM_RIVER_BIOME,
            ModBiomes.WARM_RIVER_BIOME,
            Biomes.RIVER,
            ModBiomes.LUKEWARM_RIVER_BIOME,
            ModBiomes.LUKEWARM_RIVER_BIOME
    }, {
            ModBiomes.WARM_RIVER_BIOME,
            ModBiomes.WARM_RIVER_BIOME,
            ModBiomes.WARM_RIVER_BIOME,
            ModBiomes.WARM_RIVER_BIOME,
            ModBiomes.WARM_RIVER_BIOME
    }
    };

    public static void addRivers(OverworldBiomeBuilder biomeBuilder, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset) {
        for (int temperature = 0; temperature < biomeBuilder.temperatures.length; temperature++) {
            for (int humidity = 0; humidity < biomeBuilder.humidities.length; humidity++) {
                ResourceKey<Biome> biome = UNFROZEN_RIVERS[temperature][humidity];
                if (biome != null) {
                    addSurfaceBiome(mapper,
                            biomeBuilder.temperatures[temperature],
                            biomeBuilder.humidities[humidity],
                            continentalness,
                            erosion,
                            weirdness,
                            offset,
                            biome);
                }
            }
        }
    }

    /**
     * Copy of
     * {@link net.minecraft.world.level.biome.OverworldBiomeBuilder#addSurfaceBiome(Consumer, Climate.Parameter,
     * Climate.Parameter, Climate.Parameter, Climate.Parameter, Climate.Parameter, float, ResourceKey)} as we have a
     * mixin going into the vanilla one.
     */
    static void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        mapper.accept(Pair.of(Climate.parameters(temperature,
                humidity,
                continentalness,
                erosion,
                Climate.Parameter.point(0.0F),
                weirdness,
                offset), biome));
        mapper.accept(Pair.of(Climate.parameters(temperature,
                humidity,
                continentalness,
                erosion,
                Climate.Parameter.point(1.0F),
                weirdness,
                offset), biome));
    }
}
