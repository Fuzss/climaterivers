package com.fuzs.biomerivers.client;

import com.google.common.collect.Maps;
import net.minecraft.resources.IResourcePack;
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
public class RuntimeResourcePack implements IResourcePack {

    private final String name;
    private final StringTextComponent description;
    private boolean locked;
    private final Map<String, byte[]> data = Maps.newHashMap();
    private final Map<ResourceLocation, String> blockToTextureMap = Maps.newHashMap();

    public RuntimeResourcePack(String name, String description) {

        this.name = name;
        this.description = new StringTextComponent(description);
    }

    @Override
    public InputStream getRootResourceStream(String fileName) throws IOException {

        return null;
    }

    @Override
    public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {

        return null;
    }

    @Override
    public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String namespaceIn, String pathIn, int maxDepthIn, Predicate<String> filterIn) {

        return null;
    }

    @Override
    public boolean resourceExists(ResourcePackType type, ResourceLocation location) {

        return false;
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
    public <T> T getMetadata(IMetadataSectionSerializer<T> deserializer) {

        if (deserializer == PackMetadataSection.SERIALIZER) {

            return (T) new PackMetadataSection(this.description, SharedConstants.getVersion().getPackVersion());
        }

        return null;
    }

    @Override
    public String getName() {

        return this.name;
    }

}
