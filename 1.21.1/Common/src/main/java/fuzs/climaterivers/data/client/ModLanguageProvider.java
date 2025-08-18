package fuzs.climaterivers.data.client;

import fuzs.climaterivers.init.ModBiomes;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.Registries;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(Registries.elementsDirPath(Registries.BIOME), ModBiomes.COLD_RIVER_BIOME, "Cold River");
        translationBuilder.add(Registries.elementsDirPath(Registries.BIOME),
                ModBiomes.LUKEWARM_RIVER_BIOME,
                "Lukewarm River");
        translationBuilder.add(Registries.elementsDirPath(Registries.BIOME), ModBiomes.WARM_RIVER_BIOME, "Warm River");
    }
}
