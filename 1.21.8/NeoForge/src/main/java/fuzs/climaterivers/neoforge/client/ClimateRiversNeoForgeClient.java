package fuzs.climaterivers.neoforge.client;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.client.ClimateRiversClient;
import fuzs.climaterivers.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ClimateRivers.MOD_ID, dist = Dist.CLIENT)
public class ClimateRiversNeoForgeClient {

    public ClimateRiversNeoForgeClient() {
        ClientModConstructor.construct(ClimateRivers.MOD_ID, ClimateRiversClient::new);
        DataProviderHelper.registerDataProviders(ClimateRivers.MOD_ID, ModLanguageProvider::new);
    }
}
