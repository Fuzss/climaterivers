package fuzs.climaterivers.fabric.client;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.client.ClimateRiversClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class ClimateRiversFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(ClimateRivers.MOD_ID, ClimateRiversClient::new);
    }
}
