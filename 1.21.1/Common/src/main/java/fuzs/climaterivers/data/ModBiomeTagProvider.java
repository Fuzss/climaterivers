package fuzs.climaterivers.data;

import fuzs.climaterivers.init.ModBiomes;
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
        this.add(BiomeTags.IS_RIVER)
                .add(ModBiomes.COLD_RIVER_BIOME, ModBiomes.LUKEWARM_RIVER_BIOME, ModBiomes.WARM_RIVER_BIOME);
        this.add(BiomeTags.IS_OVERWORLD)
                .add(ModBiomes.COLD_RIVER_BIOME, ModBiomes.LUKEWARM_RIVER_BIOME, ModBiomes.WARM_RIVER_BIOME);
    }
}
