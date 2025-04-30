package fuzs.climaterivers.neoforge.client;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.client.ClimateRiversClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = ClimateRivers.MOD_ID, dist = Dist.CLIENT)
public class ClimateRiversNeoForgeClient {

    public ClimateRiversNeoForgeClient() {
        ClientModConstructor.construct(ClimateRivers.MOD_ID, ClimateRiversClient::new);
    }
}
