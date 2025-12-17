plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modCompileOnlyApi(libs.puzzleslib.common)
    modCompileOnlyApi(libs.biolith.common)
}

multiloader {
    mixins {
        mixin("BiomeDataMixin", "OverworldBiomeBuilderMixin", "PlacementUtilsMixin")
    }
}
