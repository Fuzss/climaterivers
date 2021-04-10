package com.fuzs.biomerivers.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.Property;
import net.minecraft.util.ResourceLocation;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockResourceGenerator {

    static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Variant.class, new Variant.Deserializer())
            .registerTypeAdapter(VariantList.class, new VariantList.Deserializer())
            .setPrettyPrinting().disableHtmlEscaping().create();

    private Collection<BlockStateModelUnit> blockUnits;

    public BlockResourceGenerator addUnits(Collection<Block> blocks, IResourceManager resourceManager, ResourceLocation baseModel, String modelSuffix) {

        this.blockUnits = blocks.stream()
                .map(block -> new BlockStateModelUnit(block, resourceManager, baseModel, modelSuffix))
                .collect(Collectors.toSet());

        return this;
    }

    public void load(Property<?>[] properties, Property<?>[] parentProperties) {

        this.blockUnits.forEach(unit -> unit.load(properties, parentProperties));
    }

    public void putResources(Map<ResourceLocation, JsonElement> resources) {

        this.blockUnits.forEach(unit -> unit.putResources(resources));
    }

    public Map<String, byte[]> convertResources(Map<ResourceLocation, JsonElement> resources) {

        return resources.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), entry -> jsonToBytes(entry.getValue())));
    }

    private static byte[] jsonToBytes(JsonElement jsonElement) {

        return GSON.toJson(jsonElement).getBytes(StandardCharsets.UTF_8);
    }

}
