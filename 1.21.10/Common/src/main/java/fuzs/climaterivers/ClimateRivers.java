package fuzs.climaterivers;

import com.terraformersmc.biolith.api.surface.SurfaceGeneration;
import fuzs.climaterivers.handler.SurfaceRuleBuilder;
import fuzs.climaterivers.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClimateRivers implements ModConstructor {
    public static final String MOD_ID = "climaterivers";
    public static final String MOD_NAME = "Climate Rivers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
    }

    @Override
    public void onCommonSetup() {
        SurfaceGeneration.addOverworldSurfaceRules(id("rules"), SurfaceRuleBuilder.overworldLike());
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
