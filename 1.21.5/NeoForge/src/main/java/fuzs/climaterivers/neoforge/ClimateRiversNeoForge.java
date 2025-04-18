package fuzs.climaterivers.neoforge;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.data.ModBiomeTagProvider;
import fuzs.climaterivers.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.neoforged.fml.common.Mod;

@Mod(ClimateRivers.MOD_ID)
public class ClimateRiversNeoForge {

    public ClimateRiversNeoForge() {
        ModConstructor.construct(ClimateRivers.MOD_ID, ClimateRivers::new);
        DataProviderHelper.registerDataProviders(ClimateRivers.MOD_ID,
                new RegistrySetBuilder().add(Registries.PLACED_FEATURE, ModRegistry::bootstrapPlacedFeatures)
                        .add(Registries.BIOME, ModRegistry::bootstrapBiomes),
                ModBiomeTagProvider::new);
    }
}
