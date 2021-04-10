package com.fuzs.biomerivers.client;

import com.fuzs.biomerivers.BiomeRivers;
import com.fuzs.puzzleslib_br.config.json.JsonConfigFileUtil;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.resources.IResourceManager;
import net.minecraft.state.Property;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Stream;

public class ResourceGenerator {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Variant.class, new Variant.Deserializer()).registerTypeAdapter(VariantList.class, new VariantList.Deserializer()).create();

    public static void getBlockStateReplacements(IResourceManager resourceManager, Map<ResourceLocation, Block> allBlockLocations) {

        for (Map.Entry<ResourceLocation, Block> entry : allBlockLocations.entrySet()) {

            ResourceLocation location = entry.getKey();
            ResourceLocation jsonPath = new ResourceLocation(location.getNamespace(), "blockstates/" + location.getPath() + ".json");
            JsonElement stateElement = getElementAtPath(resourceManager, jsonPath, location);
            if (stateElement != null && stateElement.isJsonObject()) {

                JsonObject jsonObject = stateElement.getAsJsonObject();

                JsonArray mainMultipart = JSONUtils.getJsonArray(jsonObject, "multipart");
                ResourceLocation blockPath = new ResourceLocation(location.getNamespace(), "block/" + location.getPath() + "_diagonal_side");
                IFinishedBlockState diagonalState = BlockStateResources.getDiagonalState(entry.getValue(), new ResourceLocation[]{blockPath});
                JsonObject additionalMultipart = diagonalState.get().getAsJsonObject();
                mainMultipart.addAll(JSONUtils.getJsonArray(additionalMultipart, "multipart"));
                byte[] bytes = ResourceGenerator.jsonToBytes(stateElement);
            }
        }
    }

    private static JsonElement getElementAtPath(IResourceManager resourceManager, ResourceLocation jsonPath, ResourceLocation location) {

        try (InputStream inputstream = resourceManager.getResource(jsonPath).getInputStream()) {

            InputStreamReader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
            return JsonConfigFileUtil.GSON.fromJson(reader, JsonElement.class);
        } catch (IOException e) {

            BiomeRivers.LOGGER.warn("Exception loading blockstate definition: {}: {}", location, e);
        }

        return null;
    }

    public static JsonElement[] getVariantModels(IResourceManager resourceManager, ResourceLocation parent, Property<?>[] properties, JsonArray multipartArray, Property<?>[] parentProperties) {

        ResourceLocation[] textures = getPropertyTextures(resourceManager, multipartArray, parentProperties);
        JsonElement[] models = new JsonElement[textures.length];
        for (int i = 0; i < textures.length; i++) {

            models[i] = BlockStateResources.getVariantModel(parent, textures[i]);
        }

        return models;
    }

    public static ResourceLocation[] getPropertyTextures(IResourceManager resourceManager, JsonArray multipartArray, Property<?>[] properties) {

        ResourceLocation[] propertyModels = getPropertyModels(multipartArray, properties);
        ResourceLocation[] propertyTextures = new ResourceLocation[propertyModels.length];
        for (int i = 0; i < propertyModels.length; i++) {

            propertyTextures[i] = getModelTexture(resourceManager, propertyModels[i]);
        }

        return propertyTextures;
    }

    private static ResourceLocation getModelTexture(IResourceManager resourceManager, ResourceLocation location) {

        BlockModel blockModel = loadModel(resourceManager, location);
        ResourceLocation texture = MissingTextureSprite.getLocation();
        if (blockModel != null) {

            texture = blockModel.resolveTextureName("texture").getTextureLocation();
            if (MissingTextureSprite.getLocation().equals(texture)) {

                // get any other texture if none was found for 'texture'
                Collection<RenderMaterial> allTextures = blockModel.getTextures(modelLocation -> loadModel(resourceManager, modelLocation), Sets.newHashSet());
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

    private static BlockModel loadModel(IResourceManager resourceManager, ResourceLocation location) {

        ResourceLocation jsonPath = new ResourceLocation(location.getNamespace(), "models/" + location.getPath() + ".json");
        try (InputStream inputstream = resourceManager.getResource(jsonPath).getInputStream()) {

            Reader reader = new InputStreamReader(inputstream, StandardCharsets.UTF_8);
            BlockModel blockmodel = BlockModel.deserialize(reader);
            blockmodel.name = location.toString();

            return blockmodel;
        } catch (IOException e) {

            BiomeRivers.LOGGER.warn("Exception loading block model definition: {}: {}", location, e);
        }

        return null;
    }

    private static ResourceLocation[] getPropertyModels(JsonArray multipartArray, Property<?>[] properties) {

        ResourceLocation[] models = new ResourceLocation[properties.length];
        for (int i = 0; i < properties.length; i++) {

            Property<?> property = properties[i];
            Optional<ResourceLocation> optional = getPropertyModel(multipartArray, property);
            if (optional.isPresent()) {

                models[i] = optional.get();
            }
        }

        return verifyModels(models);
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
                        VariantList variants = GSON.fromJson(multipartObject.get("apply"), VariantList.class);
                        if (!variants.getDependencies().isEmpty()) {

                            return variants.getDependencies().stream().findAny();
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    private static ResourceLocation[] verifyModels(ResourceLocation[] models) {

        ResourceLocation fallback = Stream.of(models)
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);

        return Stream.of(models)
                .map(model -> model != null ? model : fallback)
                .toArray(ResourceLocation[]::new);
    }

    public static byte[] jsonToBytes(JsonElement jsonElement) {

        return JsonConfigFileUtil.GSON.toJson(jsonElement).getBytes(StandardCharsets.UTF_8);
    }

}
