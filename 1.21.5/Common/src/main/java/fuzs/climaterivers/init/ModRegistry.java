package fuzs.climaterivers.init;

import fuzs.climaterivers.ClimateRivers;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(ClimateRivers.MOD_ID);
    public static final ResourceKey<Biome> COLD_RIVER_BIOME = REGISTRIES.makeResourceKey(Registries.BIOME,
            "cold_river");
    public static final ResourceKey<Biome> LUKEWARM_RIVER_BIOME = REGISTRIES.makeResourceKey(Registries.BIOME,
            "lukewarm_river");
    public static final ResourceKey<Biome> WARM_RIVER_BIOME = REGISTRIES.makeResourceKey(Registries.BIOME,
            "warm_river");
    public static final ResourceKey<PlacedFeature> DISK_GRAVEL_PLACED_FEATURE = REGISTRIES.makeResourceKey(Registries.PLACED_FEATURE,
            "disk_gravel");
    public static final ResourceKey<PlacedFeature> DISK_SAND_PLACED_FEATURE = REGISTRIES.makeResourceKey(Registries.PLACED_FEATURE,
            "disk_sand");

    public static void bootstrap() {
        // NO-OP
    }

    public static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> holder9 = holderGetter.getOrThrow(MiscOverworldFeatures.DISK_GRAVEL);
        Holder<ConfiguredFeature<?, ?>> holder10 = holderGetter.getOrThrow(MiscOverworldFeatures.DISK_SAND);
        PlacementUtils.register(context,
                DISK_GRAVEL_PLACED_FEATURE,
                holder9,
                CountPlacement.of(9),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                BiomeFilter.biome());
        PlacementUtils.register(context,
                DISK_SAND_PLACED_FEATURE,
                holder10,
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER)),
                BiomeFilter.biome());
    }

    public static void bootstrapBiomes(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> configuredCarverLookup = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(COLD_RIVER_BIOME, coldRiver(placedFeatureLookup, configuredCarverLookup));
        context.register(LUKEWARM_RIVER_BIOME, lukeWarmRiver(placedFeatureLookup, configuredCarverLookup));
        context.register(WARM_RIVER_BIOME, warmRiver(placedFeatureLookup, configuredCarverLookup));
    }

    public static Biome coldRiver(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder builder = riverSpawns();
        builder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 5, 1, 5));

        BiomeGenerationSettings.Builder builder2 = baseRiverGeneration(placedFeatures, worldCarvers);
        addGravellySoftDisks(builder2);
        BiomeDefaultFeatures.addDefaultFlowers(builder2);
        BiomeDefaultFeatures.addDefaultGrass(builder2);
        BiomeDefaultFeatures.addDefaultMushrooms(builder2);
        BiomeDefaultFeatures.addDefaultExtraVegetation(builder2);
        builder2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER);

        return OverworldBiomes.biome(true, 0.25F, 0.8F, 4020182, 329011, null, null, builder, builder2, null);
    }

    public static Biome lukeWarmRiver(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder builder = riverSpawns();
        builder.addSpawn(MobCategory.WATER_AMBIENT,
                new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 1, 1, 5));
        builder.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 4, 1, 5));

        BiomeGenerationSettings.Builder builder2 = baseRiverGeneration(placedFeatures, worldCarvers);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder2);
        BiomeDefaultFeatures.addLightBambooVegetation(builder2);
        BiomeDefaultFeatures.addDefaultFlowers(builder2);
        BiomeDefaultFeatures.addDefaultGrass(builder2);
        builder2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_WATERLILY);
        BiomeDefaultFeatures.addDefaultMushrooms(builder2);
        builder2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER);

        return OverworldBiomes.biome(true, 0.95F, 0.9F, 4566514, 267827, null, null, builder, builder2, null);
    }

    public static Biome warmRiver(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder builder = riverSpawns();
        builder.addSpawn(MobCategory.WATER_AMBIENT,
                new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 5, 1, 5));

        BiomeGenerationSettings.Builder builder2 = baseRiverGeneration(placedFeatures, worldCarvers);
        BiomeDefaultFeatures.addDefaultSoftDisks(builder2);
        BiomeDefaultFeatures.addDefaultFlowers(builder2);
        BiomeDefaultFeatures.addDefaultGrass(builder2);
        builder2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH);
        BiomeDefaultFeatures.addDefaultMushrooms(builder2);
        builder2.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER);

        return OverworldBiomes.biome(false, 2.0F, 0.0F, 4445678, 270131, null, null, builder, builder2, null);
    }

    public static MobSpawnSettings.Builder riverSpawns() {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder().addSpawn(MobCategory.WATER_CREATURE,
                new MobSpawnSettings.SpawnerData(EntityType.SQUID, 2, 1, 4));
        BiomeDefaultFeatures.commonSpawns(builder);
        builder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 100, 1, 1));
        return builder;
    }

    public static BiomeGenerationSettings.Builder baseRiverGeneration(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        OverworldBiomes.globalOverworldGeneration(builder);
        BiomeDefaultFeatures.addDefaultOres(builder);
        return builder;
    }

    public static void addGravellySoftDisks(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DISK_SAND_PLACED_FEATURE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscOverworldPlacements.DISK_CLAY);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, DISK_GRAVEL_PLACED_FEATURE);
    }
}
