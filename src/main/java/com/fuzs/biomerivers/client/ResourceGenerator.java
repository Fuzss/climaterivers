package com.fuzs.biomerivers.client;

import com.google.gson.*;
import net.minecraft.client.renderer.model.Variant;
import net.minecraft.client.renderer.model.VariantList;
import net.minecraft.state.Property;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.Optional;

public class ResourceGenerator {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(Variant.class, new Variant.Deserializer()).registerTypeAdapter(VariantList.class, new VariantList.Deserializer()).create();

    private static Optional<ResourceLocation> getPropertyModel(JsonArray jsonArray, Property<?> property) {

        String name = property.getName();
        for (JsonElement jsonElement : jsonArray) {

            if (jsonArray.isJsonObject()) {

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("when")) {

                    JsonObject conditionObject = JSONUtils.getJsonObject(jsonObject, "when");
                    if (conditionObject.has(name) && JSONUtils.getBoolean(conditionObject, name)) {

//                        if ()
//                        JsonObject variantObject = JSONUtils.getJsonObject(jsonObject, "apply");
                    }
                }
            }
        }

        return Optional.empty();
    }

}
