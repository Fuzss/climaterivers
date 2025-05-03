package fuzs.climaterivers.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> DISK_GRAVEL_PLACED_FEATURE = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.PLACED_FEATURE,
            "disk_gravel");
    public static final ResourceKey<PlacedFeature> DISK_SAND_PLACED_FEATURE = ModRegistry.REGISTRIES.makeResourceKey(
            Registries.PLACED_FEATURE,
            "disk_sand");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
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
}
