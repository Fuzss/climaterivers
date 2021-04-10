package com.fuzs.biomerivers.element;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.block.IEightWayBlock;
import com.fuzs.biomerivers.client.BlockResourceGenerator;
import com.fuzs.biomerivers.client.BlockResources;
import com.fuzs.biomerivers.client.RuntimeResourcePack;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.IClientElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

public class DiagonalFencesElement extends AbstractElement implements ICommonElement, IClientElement {

    @Override
    public String[] getDescription() {

        return new String[]{"Prevent water and lava lakes from generating at surface levels."};
    }

    @Override
    public void setupClient() {


        Minecraft mc = Minecraft.getInstance();
        ResourcePackList packList = mc.getResourcePackList();
        IResourceManager resourceManager = mc.getResourceManager();
        BlockResourceGenerator generator = this.buildGenerator(resourceManager);
        RuntimeResourcePack resourcePack = new RuntimeResourcePack(generator, BiomeRivers.NAME, "Custom resources for fences");
        addPackFinder(packList, resourcePack, BiomeRivers.MODID);
    }

    private BlockResourceGenerator buildGenerator(IResourceManager resourceManager) {

        ResourceLocation baseModel = new ResourceLocation(BiomeRivers.MODID, "block/fence_diagonal_side");
        ResourceLocation baseModelName = new ResourceLocation(baseModel.getNamespace(), "models/" + baseModel.getPath() + ".json");

        Property<?>[] properties = IEightWayBlock.DIRECTION_TO_PROPERTY_MAP.values().stream().skip(4).toArray(Property<?>[]::new);
        Property<?>[] parentProperties = IEightWayBlock.DIRECTION_TO_PROPERTY_MAP.values().stream().limit(4).toArray(Property<?>[]::new);
        Set<Block> allFences = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block instanceof FenceBlock)
                .collect(Collectors.toSet());

        return new BlockResourceGenerator()
                .addUnits(allFences, resourceManager, baseModel, "side")
                .setProperties(properties, parentProperties)
                .addResource(baseModelName, BlockResources.getDiagonalModel());
    }

    private static void addPackFinder(ResourcePackList packList, RuntimeResourcePack resourcePack, String owner) {

        packList.addPackFinder((infoConsumer, infoFactory) -> {

            ResourcePackInfo resourcepackinfo = new ResourcePackInfo(owner, true, () -> resourcePack, new StringTextComponent(resourcePack.getName()),
                    resourcePack.getDescription(), PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, true, IPackNameDecorator.BUILTIN, false);

            infoConsumer.accept(resourcepackinfo);
        });
    }

}
