package fuzs.climaterivers.neoforge;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.data.tags.ModBiomeTagProvider;
import fuzs.climaterivers.data.ModDatapackRegistriesProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(ClimateRivers.MOD_ID)
public class ClimateRiversNeoForge {

    public ClimateRiversNeoForge() {
        ModConstructor.construct(ClimateRivers.MOD_ID, ClimateRivers::new);
        DataProviderHelper.registerDataProviders(ClimateRivers.MOD_ID,
                ModDatapackRegistriesProvider::new,
                ModBiomeTagProvider::new);
    }
}
