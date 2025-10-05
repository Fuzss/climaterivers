package fuzs.climaterivers.fabric;

import fuzs.climaterivers.ClimateRivers;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class ClimateRiversFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(ClimateRivers.MOD_ID, ClimateRivers::new);
    }
}
