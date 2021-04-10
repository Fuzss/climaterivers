package com.fuzs.biomerivers.client;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockResourceGenerator {

    static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Variant.class, new Variant.Deserializer())
            .registerTypeAdapter(VariantList.class, new VariantList.Deserializer())
            .setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, Supplier<Map<ResourceLocation, JsonElement>>> resources = Maps.newHashMap();
    private Pair<Property<?>[], Property<?>[]> properties;

    public BlockResourceGenerator addUnits(Collection<Block> blocks, IResourceManager resourceManager, ResourceLocation baseModel, String modelSuffix) {

        for (Block block : blocks) {

            this.addUnit(block, resourceManager, baseModel, modelSuffix);
        }

        return this;
    }

    public BlockResourceGenerator addUnit(Block block, IResourceManager resourceManager, ResourceLocation baseModel, String modelSuffix) {

        BlockStateModelUnit unit = new BlockStateModelUnit(block, resourceManager, baseModel, modelSuffix);
        this.resources.put(unit.getResourceLocation(), () -> this.loadUnit(unit));

        return this;
    }

    private Map<ResourceLocation, JsonElement> loadUnit(BlockStateModelUnit unit) {

        assert this.properties != null : "Properties not set for generator";
        return unit.load(this.properties.getLeft(), this.properties.getRight());
    }

    public BlockResourceGenerator setProperties(Property<?>[] properties, Property<?>[] parentProperties) {

        this.properties = Pair.of(properties, parentProperties);
        return this;
    }

    public BlockResourceGenerator addResource(ResourceLocation path, JsonElement resourceElement) {

        this.resources.put(path, () -> Collections.singletonMap(path, resourceElement));
        return this;
    }

    public boolean hasResourceLocation(ResourceLocation resource) {

        return this.resources.containsKey(resource);
    }

    public Collection<ResourceLocation> getResourceLocations() {

        return ImmutableSet.copyOf(this.resources.keySet());
    }

    public Map<ResourceLocation, byte[]> getResource(ResourceLocation resource) {

        return this.convert(this.resources.get(resource).get());
    }

    public Map<ResourceLocation, byte[]> getResources() {

        return this.resources.values().stream()
                .map(Supplier::get)
                .map(this::convert)
                .flatMap(resources -> resources.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<ResourceLocation, byte[]> convert(Map<ResourceLocation, JsonElement> resources) {

        return resources.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> toBytes(entry.getValue())));
    }

    private static byte[] toBytes(JsonElement jsonElement) {

        return GSON.toJson(jsonElement).getBytes(StandardCharsets.UTF_8);
    }

}
