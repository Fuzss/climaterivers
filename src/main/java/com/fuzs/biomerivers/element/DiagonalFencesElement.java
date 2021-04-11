package com.fuzs.biomerivers.element;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.biomerivers.block.IEightWayBlock;
import com.fuzs.biomerivers.client.renderer.model.AssetLocations;
import com.fuzs.biomerivers.client.renderer.model.BlockAssetGenerator;
import com.fuzs.biomerivers.client.renderer.model.BlockStateModelUnit;
import com.fuzs.biomerivers.data.BlockAssetProvider;
import com.fuzs.biomerivers.resources.IResourceInfoFactory;
import com.fuzs.biomerivers.resources.RuntimeResourcePack;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.IClientElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

public class DiagonalFencesElement extends AbstractElement implements ICommonElement, IClientElement {

    private BlockAssetGenerator generator;

    @Override
    public String[] getDescription() {

        return new String[]{"Prevent water and lava lakes from generating at surface levels."};
    }

    @Override
    public void setupClient() {

        // we just need an event which is called before ResourceManager is loaded for the first time
        // (which is during construction of Minecraft.class), but after registries have been populated
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onParticleFactoryRegister);

        Minecraft mc = Minecraft.getInstance();
        ResourcePackList packList = mc.getResourcePackList();
        this.makeGenerator(mc.getResourceManager());
        RuntimeResourcePack resourcePack = new RuntimeResourcePack(this.generator, "Diagonal Fences", "Diagonal fences?! Is this even allowed?");
        this.addPackFinder(packList, resourcePack, BiomeRivers.MODID);
    }

    private void onParticleFactoryRegister(final ParticleFactoryRegisterEvent evt) {

        this.addUnits();
    }

    private void makeGenerator(IResourceManager resourceManager) {

        ResourceLocation baseModel = new ResourceLocation(BiomeRivers.MODID, "fence_diagonal_side");
        this.generator = new BlockAssetGenerator(resourceManager)
                .addResource(AssetLocations.getBlockModelPath(baseModel), BlockAssetProvider.getDiagonalModel());
    }

    private void addPackFinder(ResourcePackList packList, IResourceInfoFactory resourcePack, String owner) {

        packList.addPackFinder((infoConsumer, infoFactory) -> {

            ResourcePackInfo resourcepackinfo = resourcePack.createResourcePack(owner, true, ResourcePackInfo.Priority.TOP, true, true);
            infoConsumer.accept(resourcepackinfo);
        });
    }

    private void addUnits() {

        Set<Block> allFences = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block instanceof FenceBlock)
                .collect(Collectors.toSet());
        ResourceLocation baseModel = new ResourceLocation(BiomeRivers.MODID, "fence_diagonal_side");
        Property<?>[] newProperties = IEightWayBlock.DIRECTION_TO_PROPERTY_MAP.values().stream()
                .skip(4)
                .toArray(Property<?>[]::new);
        Property<?>[] referenceProperties = IEightWayBlock.DIRECTION_TO_PROPERTY_MAP.values().stream()
                .limit(4)
                .toArray(Property<?>[]::new);
        BlockStateModelUnit.ModelData modelData = new BlockStateModelUnit.ModelData(
                AssetLocations.getBlockModelName(baseModel), "side", newProperties, referenceProperties);

        this.generator.addUnits(allFences, modelData);
    }

}
