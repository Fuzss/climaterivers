package fuzs.climaterivers.data.client;

import fuzs.climaterivers.init.ModBiomes;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("biome", ModBiomes.COLD_RIVER_BIOME.location(), "Cold River");
        translationBuilder.add("biome", ModBiomes.LUKEWARM_RIVER_BIOME.location(), "Lukewarm River");
        translationBuilder.add("biome", ModBiomes.WARM_RIVER_BIOME.location(), "Warm River");
    }
}
