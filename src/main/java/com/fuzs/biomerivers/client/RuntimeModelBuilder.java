package com.fuzs.biomerivers.client;

import com.google.common.base.Preconditions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.BiFunction;

public class RuntimeModelBuilder extends BlockModelBuilder {

    public RuntimeModelBuilder(ResourceLocation outputLocation) {

        super(outputLocation, null);
    }

    @Override
    public RuntimeModelBuilder texture(String key, ResourceLocation texture) {

        Preconditions.checkNotNull(key, "Key must not be null");
        Preconditions.checkNotNull(texture, "Texture must not be null");
        this.textures.put(key, texture.toString());

        return this;
    }

    @Override
    public <L extends CustomLoaderBuilder<BlockModelBuilder>> L customLoader(BiFunction<BlockModelBuilder, ExistingFileHelper, L> customLoaderFactory) {

        throw new UnsupportedOperationException();
    }

}
