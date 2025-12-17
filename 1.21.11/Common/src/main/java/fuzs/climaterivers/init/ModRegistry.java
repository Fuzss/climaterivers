package fuzs.climaterivers.init;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.PLACED_FEATURE,
            ModPlacedFeatures::bootstrap).add(Registries.BIOME, ModBiomes::bootstrap);

    public static void bootstrap() {
        // NO-OP
    }
}
