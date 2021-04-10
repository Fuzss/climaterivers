package com.fuzs.biomerivers.client;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.forgespi.language.IModInfo;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class RuntimeResourcePack extends ResourcePack {

    private final Map<String, byte[]> data = Maps.newHashMap();
    private final IModInfo modInfo;
    private final Map<ResourceLocation, String> blockToTextureMap;

    public RuntimeResourcePack(IModInfo modInfo, Map<ResourceLocation, String> blockToTextureMap) {

        super(new File(modInfo.getModId()));
        this.modInfo = modInfo;
        this.blockToTextureMap = blockToTextureMap;
    }

    @Override
    protected InputStream getInputStream(String resourcePath) throws IOException {
        return null;
    }

    @Override
    protected boolean resourceExists(String resourcePath) {
        return false;
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn) {
        return null;
    }

    @Override
    public Set<String> getResourceNamespaces(ResourcePackType type) {

        return this.blockToTextureMap.keySet().stream()
                .map(ResourceLocation::getNamespace)
                .collect(Collectors.toSet());
    }

    @Override
    public void close() {

        this.data.clear();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) throws IOException {

        if (deserializer == PackMetadataSection.SERIALIZER) {

            return (T) new PackMetadataSection(new StringTextComponent(this.modInfo.getDescription()), SharedConstants.getVersion().getPackVersion());
        }

        return super.getMetadata(deserializer);
    }

    @Override
    public String getName() {

        return this.modInfo.getModId();
    }

    private void generate() {


    }

}
