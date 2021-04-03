package com.fuzs.biomerivers.element;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.world.biome.RiverBiomeMaker;
import com.fuzs.puzzleslib_br.PuzzlesLib;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClimateRiversElement extends AbstractElement implements ICommonElement {

    @ObjectHolder(BiomeRivers.MODID + ":" + "dry_river")
    public static final Biome DRY_RIVER_BIOME = null;
    @ObjectHolder(BiomeRivers.MODID + ":" + "barren_river")
    public static final Biome BARREN_RIVER_BIOME = null;

    public static final Int2IntMap BIOME_TO_RIVER_MAP = new Int2IntOpenHashMap();
    private static final Object2IntMap<Biome> RIVER_TO_ID_MAP = new Object2IntOpenHashMap<>();

    @Override
    public String[] getDescription() {

        return new String[]{"Re-adds sword blocking in a very configurable way."};
    }

    @Override
    public void setupCommon() {

        PuzzlesLib.getRegistryManager().register("dry_river", RiverBiomeMaker.makeRiverBiome(-0.5F, 0.0F, 2.0F, 0.0F, 4159204, Biome.RainType.NONE, ConfiguredSurfaceBuilders.field_244172_d));
        PuzzlesLib.getRegistryManager().register("barren_river", RiverBiomeMaker.makeRiverBiome(-0.5F, 0.0F, 2.0F, 0.0F, 4159204, Biome.RainType.NONE, ConfiguredSurfaceBuilders.field_244169_a));
        this.addListener(this::onBiomeLoading);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void loadCommon() {

        Set<Biome> riverBiomes = ImmutableSet.of(DRY_RIVER_BIOME, BARREN_RIVER_BIOME);
        ForgeRegistry<Biome> biomeRegistry = (ForgeRegistry<Biome>) ForgeRegistries.BIOMES;
        RIVER_TO_ID_MAP.putAll(riverBiomes.stream().collect(Collectors.toMap(Function.identity(), biomeRegistry::getID)));
    }

    private void onBiomeLoading(final BiomeLoadingEvent evt) {

        int biome = Optional.ofNullable(evt.getName()).map(((ForgeRegistry<Biome>) ForgeRegistries.BIOMES)::getID).orElse(-1);
        if (biome != -1) {

            if (evt.getCategory() == Biome.Category.MESA) {

                BIOME_TO_RIVER_MAP.put(biome, RIVER_TO_ID_MAP.getInt(BARREN_RIVER_BIOME));
            } else if (evt.getClimate().precipitation == Biome.RainType.NONE) {

                BIOME_TO_RIVER_MAP.put(biome, RIVER_TO_ID_MAP.getInt(DRY_RIVER_BIOME));
            } else if (evt.getClimate().precipitation == Biome.RainType.SNOW) {

                BIOME_TO_RIVER_MAP.put(biome, 11);
            }
        }
    }

}
