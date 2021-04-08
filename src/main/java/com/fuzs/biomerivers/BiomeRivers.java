package com.fuzs.biomerivers;

import com.fuzs.biomerivers.element.ByeByeLakesElement;
import com.fuzs.biomerivers.element.ClimateRiversElement;
import com.fuzs.puzzleslib_br.PuzzlesLib;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.ElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(BiomeRivers.MODID)
public class BiomeRivers extends PuzzlesLib {

    public static final String MODID = "biomerivers";
    public static final String NAME = "Biome Rivers";
    public static final Logger LOGGER = LogManager.getLogger(BiomeRivers.NAME);

    public static final AbstractElement CLIMATE_RIVERS = register("climate_rivers", ClimateRiversElement::new);
    public static final AbstractElement BYE_BYE_LAKES = register("bye_bye_lakes", ByeByeLakesElement::new);

    public BiomeRivers() {

        ElementRegistry.setup(MODID, true);
        this.addResourcePack();
    }

    private void addResourcePack() {

        IModInfo modInfo = getModInfo(MODID);
        if (modInfo != null && modInfo.getOwningFile() instanceof ModFileInfo) {

            addPackFinder(modInfo, ((ModFileInfo) modInfo.getOwningFile()).getFile());
        }
    }

    private static IModInfo getModInfo(String modId) {

        List<ModFileInfo> modFiles = ModList.get().getModFiles();
        for (ModFileInfo modFileInfo : modFiles) {

            for (IModInfo modInfo : modFileInfo.getMods()) {

                if (modInfo.getModId().equals(modId)) {

                    return modInfo;
                }
            }
        }

        return null;
    }

    private static void addPackFinder(IModInfo modInfo, ModFile modFile) {

        ResourcePack internalResourcePack = new ModFileResourcePack(modFile);
        ResourcePackList packList = Minecraft.getInstance().getResourcePackList();
        packList.addPackFinder((infoConsumer, infoFactory) -> {

            ResourcePackInfo resourcepackinfo = new ResourcePackInfo(modInfo.getModId(), true, () -> internalResourcePack, new StringTextComponent(modInfo.getDisplayName()),
                    new StringTextComponent(modInfo.getDescription()), PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, true, IPackNameDecorator.BUILTIN, false);

            infoConsumer.accept(resourcepackinfo);
        });
    }

}
