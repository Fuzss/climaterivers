package fuzs.climaterivers.data;

import fuzs.climaterivers.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTagProvider extends AbstractTagProvider<Biome> {

    public ModBiomeTagProvider(DataProviderContext context) {
        super(Registries.BIOME, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BiomeTags.IS_RIVER)
                .add(ModRegistry.COLD_RIVER_BIOME, ModRegistry.LUKEWARM_RIVER_BIOME, ModRegistry.WARM_RIVER_BIOME);
        this.tag(BiomeTags.IS_OVERWORLD)
                .add(ModRegistry.COLD_RIVER_BIOME, ModRegistry.LUKEWARM_RIVER_BIOME, ModRegistry.WARM_RIVER_BIOME);
    }
}
