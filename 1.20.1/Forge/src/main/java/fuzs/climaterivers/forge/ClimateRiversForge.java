package fuzs.climaterivers.forge;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.data.ModBiomeTagProvider;
import fuzs.climaterivers.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.api.data.v2.core.ForgeDataProviderContext;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;

@Mod(ClimateRivers.MOD_ID)
public class ClimateRiversForge {

    public ClimateRiversForge() {
        ModConstructor.construct(ClimateRivers.MOD_ID, ClimateRivers::new);
        DataProviderHelper.registerDataProviders(ClimateRivers.MOD_ID,
                createRegistriesDatapackGenerator(ModRegistry.REGISTRY_SET_BUILDER),
                ModBiomeTagProvider::new);
    }

    static ForgeDataProviderContext.Factory createRegistriesDatapackGenerator(RegistrySetBuilder registrySetBuilder) {
        return (ForgeDataProviderContext context) -> {
            return new DatapackBuiltinEntriesProvider(context.getPackOutput(),
                    context.getLookupProvider(),
                    registrySetBuilder,
                    Collections.singleton(context.getModId()));
        };
    }
}
