package com.fuzs.biomerivers.element;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.block.IEightWayBlock;
import com.fuzs.puzzleslib_br.config.json.JsonConfigFileUtil;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.IClientElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.data.*;
import net.minecraft.resources.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
//        Map<ResourceLocation, Block> allFenceLocations = ForgeRegistries.BLOCKS.getValues().stream()
//                .filter(block -> block instanceof FenceBlock)
//                .collect(Collectors.toMap(ForgeRegistryEntry::getRegistryName, Function.identity()));
//        this.add(Minecraft.getInstance().getResourceManager(), allFenceLocations);
    }

    private void add(IResourceManager resourceManager, Map<ResourceLocation, Block> allFenceLocations) {

        for (Map.Entry<ResourceLocation, Block> entry : allFenceLocations.entrySet()) {

            ResourceLocation fenceLocation = entry.getKey();
            ResourceLocation jsonPath = this.constructPath(fenceLocation);
            try (InputStream inputstream = resourceManager.getResource(jsonPath).getInputStream()) {

                InputStreamReader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
                JsonElement stateElement = JsonConfigFileUtil.GSON.fromJson(reader, JsonElement.class);
                if (stateElement.isJsonObject()) {

                    JsonObject jsonObject = stateElement.getAsJsonObject();
                    JsonArray multipart = JSONUtils.getJsonArray(jsonObject, "multipart");
                    IFinishedBlockState diagonalState = createExtraFenceState(entry.getValue(), new ResourceLocation(fenceLocation.getNamespace(), "block/" + fenceLocation.getPath() + "_diagonal_side"));
                    JsonObject diagonalStateJsonObject = diagonalState.get().getAsJsonObject();
                    multipart.addAll(JSONUtils.getJsonArray(diagonalStateJsonObject, "multipart"));
                    byte[] bytes = JsonConfigFileUtil.GSON.toJson(stateElement).getBytes(StandardCharsets.UTF_8);
                }
            } catch (IOException e) {

                BiomeRivers.LOGGER.warn("Exception loading blockstate definition: {}: {}", fenceLocation, e);
            }
        }
    }

    private static IFinishedBlockState createExtraFenceState(Block block, ResourceLocation diagonalSide) {

        return FinishedMultiPartBlockState.func_240106_a_(block)
                .func_240108_a_(IMultiPartPredicateBuilder.func_240089_a_().func_240098_a_(IEightWayBlock.NORTH_EAST, true), BlockModelDefinition.getNewModelDefinition().replaceInfoValue(BlockModelFields.field_240202_c_, diagonalSide).replaceInfoValue(BlockModelFields.field_240203_d_, true))
                .func_240108_a_(IMultiPartPredicateBuilder.func_240089_a_().func_240098_a_(IEightWayBlock.SOUTH_EAST, true), BlockModelDefinition.getNewModelDefinition().replaceInfoValue(BlockModelFields.field_240202_c_, diagonalSide).replaceInfoValue(BlockModelFields.field_240201_b_, BlockModelFields.Rotation.R90).replaceInfoValue(BlockModelFields.field_240203_d_, true))
                .func_240108_a_(IMultiPartPredicateBuilder.func_240089_a_().func_240098_a_(IEightWayBlock.SOUTH_WEST, true), BlockModelDefinition.getNewModelDefinition().replaceInfoValue(BlockModelFields.field_240202_c_, diagonalSide).replaceInfoValue(BlockModelFields.field_240201_b_, BlockModelFields.Rotation.R180).replaceInfoValue(BlockModelFields.field_240203_d_, true))
                .func_240108_a_(IMultiPartPredicateBuilder.func_240089_a_().func_240098_a_(IEightWayBlock.NORTH_WEST, true), BlockModelDefinition.getNewModelDefinition().replaceInfoValue(BlockModelFields.field_240202_c_, diagonalSide).replaceInfoValue(BlockModelFields.field_240201_b_, BlockModelFields.Rotation.R270).replaceInfoValue(BlockModelFields.field_240203_d_, true));
    }

    private ResourceLocation constructPath(ResourceLocation blockstateLocation) {

        return new ResourceLocation(blockstateLocation.getNamespace(), "blockstates/" + blockstateLocation.getPath() + ".json");
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
