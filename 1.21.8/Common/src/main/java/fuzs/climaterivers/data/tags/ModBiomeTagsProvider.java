package fuzs.climaterivers.data.tags;

import fuzs.climaterivers.init.ModBiomes;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTagsProvider extends AbstractTagProvider<Biome> {

    public ModBiomeTagsProvider(DataProviderContext context) {
        super(Registries.BIOME, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BiomeTags.IS_RIVER)
                .addKey(ModBiomes.COLD_RIVER_BIOME, ModBiomes.LUKEWARM_RIVER_BIOME, ModBiomes.WARM_RIVER_BIOME);
        this.tag(BiomeTags.IS_OVERWORLD)
                .addKey(ModBiomes.COLD_RIVER_BIOME, ModBiomes.LUKEWARM_RIVER_BIOME, ModBiomes.WARM_RIVER_BIOME);
    }
}
