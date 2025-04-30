package fuzs.climaterivers.fabric;

import fuzs.climaterivers.ClimateRivers;
import fuzs.climaterivers.handler.SurfaceRuleBuilder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.TerraBlenderApi;

public class ClimateRiversFabric implements ModInitializer, TerraBlenderApi {

    @Override
    public void onInitialize() {
        ModConstructor.construct(ClimateRivers.MOD_ID, ClimateRivers::new);
    }

    @Override
    public void onTerraBlenderInitialized() {
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD,
                ClimateRivers.MOD_ID,
                SurfaceRuleBuilder.overworldLike());
    }
}
