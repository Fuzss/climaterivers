package fuzs.climaterivers.handler;

import fuzs.climaterivers.init.ModRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class SurfaceRuleBuilder {
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
    private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
    private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }

    public static SurfaceRules.RuleSource overworldLike() {
        SurfaceRules.ConditionSource conditionSource8 = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource conditionSource9 = SurfaceRules.waterBlockCheck(0, 0);
        SurfaceRules.ConditionSource conditionSource10 = SurfaceRules.waterStartCheck(-6, -1);
        SurfaceRules.ConditionSource conditionSource14 = SurfaceRules.isBiome(ModRegistry.WARM_RIVER_BIOME);

        SurfaceRules.RuleSource ruleSource = SurfaceRules.sequence(SurfaceRules.ifTrue(conditionSource9, GRASS_BLOCK),
                DIRT);
        SurfaceRules.RuleSource ruleSource2 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING,
                SANDSTONE), SAND);
        SurfaceRules.RuleSource ruleSource3 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE),
                GRAVEL);

        SurfaceRules.RuleSource ruleSource4 = SurfaceRules.sequence(SurfaceRules.ifTrue(conditionSource14, ruleSource2),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(ModRegistry.COLD_RIVER_BIOME), ruleSource3));
        // we need dirt as the below water surface material, otherwise the disk features cannot be placed
        SurfaceRules.RuleSource ruleSource7 = SurfaceRules.sequence(SurfaceRules.ifTrue(conditionSource9, ruleSource4),
                DIRT);
        // warm ocean - top block sand / sandstone with dirt all below
        // lukewarm ocean - top block grass with dirt all below
        // cold ocean - top block gravel / stone with dirt all below
        SurfaceRules.RuleSource ruleSource8 = SurfaceRules.sequence(ruleSource4, ruleSource);

        SurfaceRules.RuleSource ruleSource9 = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.ifTrue(conditionSource8, ruleSource8)),
                SurfaceRules.ifTrue(conditionSource10,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, ruleSource7),
                                SurfaceRules.ifTrue(conditionSource14,
                                        SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)))),
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ModRegistry.WARM_RIVER_BIOME,
                                ModRegistry.LUKEWARM_RIVER_BIOME), ruleSource2), ruleSource3)));

        return SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), ruleSource9);
    }
}
