package fuzs.climaterivers.init;

import fuzs.climaterivers.ClimateRivers;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.PLACED_FEATURE,
            ModPlacedFeatures::bootstrap).add(Registries.BIOME, ModBiomes::bootstrap);
    static final RegistryManager REGISTRIES = RegistryManager.from(ClimateRivers.MOD_ID);

    public static void bootstrap() {
        // NO-OP
    }
}
