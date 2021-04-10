package com.fuzs.biomerivers.element;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.block.IEightWayBlock;
import com.fuzs.biomerivers.client.BlockResourceGenerator;
import com.fuzs.biomerivers.client.BlockResources;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.IClientElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DiagonalFencesElement extends AbstractElement implements ICommonElement, IClientElement {

    @Override
    public String[] getDescription() {

        return new String[]{"Prevent water and lava lakes from generating at surface levels."};
    }

    @Override
    public void loadClient() {

        this.addResourcePack();
        ResourceLocation baseModel = new ResourceLocation(BiomeRivers.MODID, "block/fence_diagonal_side");
        Set<Block> allFences = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block instanceof FenceBlock)
                .collect(Collectors.toSet());
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        BlockResourceGenerator generator = new BlockResourceGenerator().addUnits(allFences, resourceManager, baseModel, "side");
        Property<?>[] properties = IEightWayBlock.DIRECTION_TO_PROPERTY_MAP.values().stream().skip(4).toArray(Property<?>[]::new);
        Property<?>[] parentProperties = IEightWayBlock.DIRECTION_TO_PROPERTY_MAP.values().stream().limit(4).toArray(Property<?>[]::new);
        generator.load(properties, parentProperties);
        Map<ResourceLocation, JsonElement> resources = Maps.newHashMap();
        resources.put(new ResourceLocation(baseModel.getNamespace(), "model/" + baseModel.getPath() + ".json"), BlockResources.getDiagonalModel());
        generator.putResources(resources);
        generator.convertResources(resources);

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
