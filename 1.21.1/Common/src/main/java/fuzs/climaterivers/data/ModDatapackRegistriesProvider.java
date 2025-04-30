package fuzs.climaterivers.data;

import fuzs.climaterivers.init.ModBiomes;
import fuzs.climaterivers.init.ModPlacedFeatures;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.Registries;

public class ModDatapackRegistriesProvider extends AbstractDatapackRegistriesProvider {

    public ModDatapackRegistriesProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBootstrap(RegistryBoostrapConsumer registryBoostrapConsumer) {
        registryBoostrapConsumer.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        registryBoostrapConsumer.add(Registries.BIOME, ModBiomes::bootstrap);
    }
}
