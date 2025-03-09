package fuzs.climaterivers.init;

import fuzs.climaterivers.ClimateRivers;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(ClimateRivers.MOD_ID);
    public static final ResourceKey<Biome> COLD_RIVER_BIOME = REGISTRIES.makeResourceKey(Registries.BIOME,
            "cold_river");
    public static final ResourceKey<Biome> LUKEWARM_RIVER_BIOME = REGISTRIES.makeResourceKey(Registries.BIOME,
            "lukewarm_river");
    public static final ResourceKey<Biome> WARM_RIVER_BIOME = REGISTRIES.makeResourceKey(Registries.BIOME,
            "warm_river");

    public static void bootstrap() {
        // NO-OP
    }

    public static void bootstrapBiomes(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> configuredCarverLookup = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(COLD_RIVER_BIOME,
                river(placedFeatureLookup, configuredCarverLookup, true, 0.25F, 0.8F, 4020182, 329011));
        context.register(LUKEWARM_RIVER_BIOME,
                river(placedFeatureLookup, configuredCarverLookup, true, 0.95F, 0.9F, 4566514, 267827));
        context.register(WARM_RIVER_BIOME,
                river(placedFeatureLookup, configuredCarverLookup, false, 2.0F, 0.0F, 4445678, 270131));
    }

    public static Biome river(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers, boolean hasPrecipitation, float temperature, float downfall, int waterColor, int waterFogColor) {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder().addSpawn(MobCategory.WATER_CREATURE,
                        new MobSpawnSettings.SpawnerData(EntityType.SQUID, 2, 1, 4))
                .addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 5, 1, 5));
        BiomeDefaultFeatures.commonSpawns(builder);
        builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 100, 1, 1));
        net.minecraft.world.level.biome.BiomeGenerationSettings.Builder builder2 = new net.minecraft.world.level.biome.BiomeGenerationSettings.Builder(
                placedFeatures,
                worldCarvers);
        OverworldBiomes.globalOverworldGeneration(builder2);
        BiomeDefaultFeatures.addDefaultOres(builder2);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder2);
//        BiomeDefaultFeatures.addWaterTrees(builder2);
        BiomeDefaultFeatures.addDefaultFlowers(builder2);
        BiomeDefaultFeatures.addDefaultGrass(builder2);
        BiomeDefaultFeatures.addDefaultMushrooms(builder2);
//        BiomeDefaultFeatures.addDefaultExtraVegetation(builder2);
        builder2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER);

        return OverworldBiomes.biome(hasPrecipitation,
                temperature,
                downfall,
                waterColor,
                waterFogColor,
                null,
                null,
                builder,
                builder2,
                null);
    }
}
