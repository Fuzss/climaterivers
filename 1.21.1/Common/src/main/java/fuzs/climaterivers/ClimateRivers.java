package fuzs.climaterivers;

import fuzs.climaterivers.handler.SurfaceRuleBuilder;
import fuzs.climaterivers.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.SurfaceRuleManager;

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
        // must be done in the special entry point on Fabric
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isForgeLike()) {
            // use TerraBlender only for the surface rules, biome placement is handled via mixin
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD,
                    ClimateRivers.MOD_ID,
                    SurfaceRuleBuilder.overworldLike());
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
