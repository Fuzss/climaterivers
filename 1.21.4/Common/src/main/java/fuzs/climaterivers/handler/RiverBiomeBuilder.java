package fuzs.climaterivers.handler;

import com.mojang.datafixers.util.Pair;
import fuzs.climaterivers.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils;

import java.util.function.Consumer;

public class RiverBiomeBuilder {
    static final ParameterUtils.Temperature[] TEMPERATURES = {
            ParameterUtils.Temperature.ICY,
            ParameterUtils.Temperature.COOL,
            ParameterUtils.Temperature.NEUTRAL,
            ParameterUtils.Temperature.WARM,
            ParameterUtils.Temperature.HOT
    };
    static final ParameterUtils.Humidity[] HUMIDITIES = {
            ParameterUtils.Humidity.ARID,
            ParameterUtils.Humidity.DRY,
            ParameterUtils.Humidity.NEUTRAL,
            ParameterUtils.Humidity.WET,
            ParameterUtils.Humidity.HUMID,
    };
    static final ResourceKey<Biome>[][] RIVERS = new ResourceKey[][]{
            {null, null, null, null, null}, {
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME
    }, {null, null, null, null, null}, {
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            null,
            ModRegistry.LUKEWARM_RIVER_BIOME,
            ModRegistry.LUKEWARM_RIVER_BIOME
    }, {
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME
    }
    };
    static final ResourceKey<Biome>[][] ALL_RIVERS = new ResourceKey[][]{
            {Biomes.FROZEN_RIVER, Biomes.FROZEN_RIVER, Biomes.FROZEN_RIVER, Biomes.FROZEN_RIVER, Biomes.FROZEN_RIVER}, {
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME,
            ModRegistry.COLD_RIVER_BIOME
    }, {Biomes.RIVER, Biomes.RIVER, Biomes.RIVER, Biomes.RIVER, Biomes.RIVER}, {
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            Biomes.RIVER,
            ModRegistry.LUKEWARM_RIVER_BIOME,
            ModRegistry.LUKEWARM_RIVER_BIOME
    }, {
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME,
            ModRegistry.WARM_RIVER_BIOME
    }
    };

    public static void addValleys(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {

        if (false) {
            addBiome(mapper,
                    ParameterUtils.Temperature.FROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.COAST.parameter(),
                    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_1),
                    Climate.Parameter.span(0.0F, 0.05F),
                    0.0F,
                    Biomes.FROZEN_RIVER);
            addBiome(mapper,
                    ParameterUtils.Temperature.UNFROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.COAST.parameter(),
                    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_1),
                    Climate.Parameter.span(0.0F, 0.05F),
                    0.0F,
                    ModRegistry.WARM_RIVER_BIOME);
            addBiome(mapper,
                    ParameterUtils.Temperature.FROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
                    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_1),
                    ParameterUtils.Weirdness.VALLEY.parameter(),
                    0.0F,
                    Biomes.FROZEN_RIVER);
            addBiome(mapper,
                    ParameterUtils.Temperature.UNFROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
                    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_1),
                    ParameterUtils.Weirdness.VALLEY.parameter(),
                    0.0F,
                    ModRegistry.WARM_RIVER_BIOME);
            addBiome(mapper,
                    ParameterUtils.Temperature.FROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.COAST,
                            ParameterUtils.Continentalness.FAR_INLAND),
                    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_2, ParameterUtils.Erosion.EROSION_5),
                    ParameterUtils.Weirdness.VALLEY.parameter(),
                    0.0F,
                    Biomes.FROZEN_RIVER);
            addBiome(mapper,
                    ParameterUtils.Temperature.UNFROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.COAST,
                            ParameterUtils.Continentalness.FAR_INLAND),
                    ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_2, ParameterUtils.Erosion.EROSION_5),
                    ParameterUtils.Weirdness.VALLEY.parameter(),
                    0.0F,
                    ModRegistry.WARM_RIVER_BIOME);
            addBiome(mapper,
                    ParameterUtils.Temperature.FROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.COAST.parameter(),
                    ParameterUtils.Erosion.EROSION_6.parameter(),
                    ParameterUtils.Weirdness.VALLEY.parameter(),
                    0.0F,
                    Biomes.FROZEN_RIVER);
            addBiome(mapper,
                    ParameterUtils.Temperature.UNFROZEN.parameter(),
                    ParameterUtils.Humidity.FULL_RANGE.parameter(),
                    ParameterUtils.Continentalness.COAST.parameter(),
                    ParameterUtils.Erosion.EROSION_6.parameter(),
                    ParameterUtils.Weirdness.VALLEY.parameter(),
                    0.0F,
                    ModRegistry.WARM_RIVER_BIOME);
        }

        if (!ModLoaderEnvironment.INSTANCE.isDataGeneration()) {
            for (int temperature = 0; temperature < TEMPERATURES.length; temperature++) {
                for (int humidity = 0; humidity < HUMIDITIES.length; humidity++) {
                    ResourceKey<Biome> biome = ALL_RIVERS[temperature][humidity];
                    if (biome != null) {
                        addBiome(mapper,
                                TEMPERATURES[temperature].parameter(),
                                HUMIDITIES[humidity].parameter(),
                                ParameterUtils.Continentalness.COAST.parameter(),
                                ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0, ParameterUtils.Erosion.EROSION_1),
                                weirdness,
                                0.0F,
                                biome);
                        addBiome(mapper,
                                TEMPERATURES[temperature].parameter(),
                                HUMIDITIES[humidity].parameter(),
                                ParameterUtils.Continentalness.NEAR_INLAND.parameter(),
                                ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_0,
                                        ParameterUtils.Erosion.EROSION_1),
                                weirdness,
                                0.0F,
                                biome);
                        addBiome(mapper,
                                TEMPERATURES[temperature].parameter(),
                                HUMIDITIES[humidity].parameter(),
                                ParameterUtils.Continentalness.span(ParameterUtils.Continentalness.COAST,
                                        ParameterUtils.Continentalness.FAR_INLAND),
                                ParameterUtils.Erosion.span(ParameterUtils.Erosion.EROSION_2,
                                        ParameterUtils.Erosion.EROSION_5),
                                weirdness,
                                0.0F,
                                biome);
                        addBiome(mapper,
                                TEMPERATURES[temperature].parameter(),
                                HUMIDITIES[humidity].parameter(),
                                ParameterUtils.Continentalness.COAST.parameter(),
                                ParameterUtils.Erosion.EROSION_6.parameter(),
                                weirdness,
                                0.0F,
                                biome);
                    }
                }
            }
        }
    }

    static void addBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addBiome(mapper,
                temperature,
                humidity,
                continentalness,
                erosion,
                weirdness,
                ParameterUtils.Depth.SURFACE.parameter(),
                offset,
                biome);
        addBiome(mapper,
                temperature,
                humidity,
                continentalness,
                erosion,
                weirdness,
                ParameterUtils.Depth.FLOOR.parameter(),
                offset,
                biome);
    }

    static void addBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, Climate.Parameter depth, float offset, ResourceKey<Biome> biome) {
        mapper.accept(Pair.of(Climate.parameters(temperature,
                humidity,
                continentalness,
                erosion,
                depth,
                weirdness,
                offset), biome));
    }
}
