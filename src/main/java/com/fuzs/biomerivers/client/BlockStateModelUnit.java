package com.fuzs.biomerivers.client;

import com.fuzs.biomerivers.BiomeRivers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.Property;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockStateModelUnit {

    private final Map<ResourceLocation, JsonElement> resources = Maps.newHashMap();
    private final IResourceManager resourceManager;
    private final Block block;
    private final ResourceLocation blockLocation;
    private final ResourceLocation blockStateFile;
    private final ResourceLocation baseModel;
    private final String modelSuffix;

    @SuppressWarnings("ConstantConditions")
    public BlockStateModelUnit(Block block, IResourceManager resourceManager, ResourceLocation baseModel, String modelSuffix) {

        this.resourceManager = resourceManager;
        this.block = block;
        this.blockLocation = block.getRegistryName();
        this.blockStateFile = new ResourceLocation(this.blockLocation.getNamespace(), "blockstates/" + this.blockLocation.getPath() + ".json");
        this.baseModel = baseModel;
        this.modelSuffix = modelSuffix;
    }

    public void load(Property<?>[] properties, Property<?>[] parentProperties) {

        ResourceLocation blockStatePath = this.blockStateFile;
        JsonObject jsonObject = this.getBlockStateResource(blockStatePath);
        if (jsonObject != null && jsonObject.has("multipart")) {

            JsonArray multipart = JSONUtils.getJsonArray(jsonObject, "multipart");
            if (this.addVariantModels(multipart, properties, parentProperties)) {

                ResourceLocation[] modelNames = Stream.of(properties)
                        .map(property -> this.getModelName(property, false))
                        .toArray(ResourceLocation[]::new);
                IFinishedBlockState diagonalState = BlockResources.getDiagonalState(this.block, modelNames);
                JsonObject toBeAdded = diagonalState.get().getAsJsonObject();
                multipart.addAll(JSONUtils.getJsonArray(toBeAdded, "multipart"));
                this.resources.put(blockStatePath, multipart);
            }
        }
    }

    public Collection<ResourceLocation> getAllResourceLocations(Property<?>[] properties) {

        return Stream.concat(Stream.of(properties)
                .map(property -> this.getModelName(property, true)), Stream.of(this.blockStateFile))
                .collect(Collectors.toSet());
    }

    private JsonObject getBlockStateResource(ResourceLocation blockStatePath) {

        JsonElement stateElement = this.loadResource(blockStatePath, reader -> BlockResourceGenerator.GSON.fromJson(reader, JsonElement.class));
        if (stateElement != null && stateElement.isJsonObject()) {

            return stateElement.getAsJsonObject();
        }

        return null;
    }

    private boolean addVariantModels(JsonArray multipartArray, Property<?>[] properties, Property<?>[] parentProperties) {

        ResourceLocation[] textures = Stream.of(parentProperties)
                .map(property -> this.getPropertyTexture(multipartArray, property))
                .toArray(ResourceLocation[]::new);

        applyFallback(textures);
        if (ArrayUtils.isNotEmpty(textures)) {

            for (int i = 0; i < textures.length; i++) {

                ResourceLocation model = this.getModelName(properties[i], true);
                this.resources.put(model, BlockResources.getVariantModel(this.baseModel, textures[i]));
            }

            return true;
        }

        BiomeRivers.LOGGER.warn("Unable to create variant models");
        return false;
    }

    private ResourceLocation getModelName(Property<?> property, boolean fileName) {

        String name = "block/" + this.blockLocation.getPath() + "_" + property.getName() + "_" + this.modelSuffix;
        return new ResourceLocation(this.blockLocation.getNamespace(), fileName ? "models/" + name + ".json" : name);
    }

    private ResourceLocation getPropertyTexture(JsonArray multipartArray, Property<?> property) {

        Optional<ResourceLocation> propertyModel = getPropertyModel(multipartArray, property);
        return propertyModel.map(this::getModelTexture).orElse(null);

    }

    private ResourceLocation getModelTexture(ResourceLocation location) {

        BlockModel blockModel = this.loadModel(location);
        ResourceLocation texture = MissingTextureSprite.getLocation();
        if (blockModel != null) {

            texture = blockModel.resolveTextureName("texture").getTextureLocation();
            if (MissingTextureSprite.getLocation().equals(texture)) {

                // get any other texture if none was found for 'texture'
                Collection<RenderMaterial> allTextures = blockModel.getTextures(this::loadModel, Sets.newHashSet());
                Optional<ResourceLocation> otherTexture = allTextures.stream()
                        .map(RenderMaterial::getTextureLocation)
                        .filter(material -> !MissingTextureSprite.getLocation().equals(material))
                        .findAny();
                if (otherTexture.isPresent()) {

                    texture = otherTexture.get();
                }
            }
        }

        return texture;
    }

    private BlockModel loadModel(ResourceLocation location) {

        ResourceLocation jsonPath = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
        return this.loadResource(jsonPath, reader -> {

            BlockModel blockmodel = BlockModel.deserialize(reader);
            blockmodel.name = location.toString();
            return blockmodel;
        });
    }

    private <T> T loadResource(ResourceLocation jsonPath, Function<Reader, T> transform) {

        try (InputStream inputstream = this.resourceManager.getResource(jsonPath).getInputStream()) {

            InputStreamReader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
            return transform.apply(reader);
        } catch (IOException e) {

            BiomeRivers.LOGGER.warn("Exception loading resource definition: {}: {}", jsonPath, e);
        }

        return null;
    }

    public void putResources(Map<ResourceLocation, JsonElement> resources) {

        resources.putAll(this.resources);
    }

    private static Optional<ResourceLocation> getPropertyModel(JsonArray multipartArray, Property<?> property) {

        String name = property.getName();
        for (JsonElement multipartElement : multipartArray) {

            if (multipartElement.isJsonObject()) {

                JsonObject multipartObject = multipartElement.getAsJsonObject();
                if (multipartObject.has("when")) {

                    JsonObject conditionObject = JSONUtils.getJsonObject(multipartObject, "when");
                    if (conditionObject.has(name) && JSONUtils.getBoolean(conditionObject, name)) {

                        // apply must be present if we've come this far
                        VariantList variants = BlockResourceGenerator.GSON.fromJson(multipartObject.get("apply"), VariantList.class);
                        if (!variants.getDependencies().isEmpty()) {

                            return variants.getDependencies().stream().findAny();
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static <T> void applyFallback(T[] models) {

        final T fallback = Stream.of(models)
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);

        for (int i = 0; i < models.length; i++) {

            if (models[i] == null) {

                models[i] = fallback;
            }
        }
    }

}
