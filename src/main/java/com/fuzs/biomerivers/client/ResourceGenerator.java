package com.fuzs.biomerivers.client;

import com.google.gson.*;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ResourceGenerator {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Variant.class, new Variant.Deserializer()).registerTypeAdapter(VariantList.class, new VariantList.Deserializer()).create();

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

    public static JsonElement getDiagonalFenceModel(ResourceLocation modelLocation) {

        return new RuntimeModelBuilder(modelLocation)
                .texture("particle", "#texture")
                .element().from(15, 12, 0).to(17, 15, 10)
                .rotation().origin(16, 8, 0).axis(Direction.Axis.Y).angle(-45).end()
                .face(Direction.DOWN).uvs(7, 0, 9, 10).texture("#texture").end()
                .face(Direction.UP).uvs(7, 0, 9, 10).texture("#texture").end()
                .face(Direction.NORTH).uvs(7, 1, 9, 4).texture("#texture").end()
                .face(Direction.WEST).uvs(0, 1, 10, 4).texture("#texture").end()
                .face(Direction.EAST).uvs(0, 1, 10, 4).texture("#texture").end()
                .end()
                .element().from(15, 6, 0).to(17, 9, 10)
                .rotation().origin(16, 8, 0).axis(Direction.Axis.Y).angle(-45).end()
                .face(Direction.DOWN).uvs(7, 0, 9, 10).texture("#texture").end()
                .face(Direction.UP).uvs(7, 0, 9, 10).texture("#texture").end()
                .face(Direction.NORTH).uvs(7, 7, 9, 10).texture("#texture").end()
                .face(Direction.WEST).uvs(0, 7, 10, 10).texture("#texture").end()
                .face(Direction.EAST).uvs(0, 7, 10, 10).texture("#texture").end()
                .end()
                .toJson();
    }

}
