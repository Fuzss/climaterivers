package com.fuzs.biomerivers.element;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.client.ResourceGenerator;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.IClientElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DiagonalFencesElement extends AbstractElement implements ICommonElement, IClientElement {

    @Override
    public String[] getDescription() {

        return new String[]{"Prevent water and lava lakes from generating at surface levels."};
    }

    @Override
    public void loadClient() {

        this.addResourcePack();
        Map<ResourceLocation, Block> allFenceLocations = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block instanceof FenceBlock)
                .collect(Collectors.toMap(ForgeRegistryEntry::getRegistryName, Function.identity()));
        ResourceGenerator.getBlockStateReplacements(Minecraft.getInstance().getResourceManager(), allFenceLocations);
    }

    private void addResourcePack() {

        IModInfo modInfo = getModInfo(BiomeRivers.MODID);
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
