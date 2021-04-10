package com.fuzs.biomerivers.element;

import com.fuzs.puzzleslib_br.config.option.OptionsBuilder;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.side.ICommonElement;

public class ByeByeLakesElement extends AbstractElement implements ICommonElement {

    public boolean noSurfaceWater;
    public boolean noSurfaceLava;
    public boolean noWaterLakes;
    public boolean noLavaLakes;

    @Override
    public String[] getDescription() {

        return new String[]{"Prevent water and lava lakes from generating at surface levels."};
    }

    @Override
    public void setupCommonConfig(OptionsBuilder builder) {

        builder.define("Remove Surface Water Lakes", true).comment("Prevent water lakes from generating above sea level.").sync(v -> this.noSurfaceWater = v);
        builder.define("Remove Surface Lava Lakes", true).comment("Prevent lava lakes from generating above sea level.").sync(v -> this.noSurfaceLava = v);
        builder.define("Remove All Water Lakes", false).comment("Remove all water lakes from the world.").sync(v -> this.noWaterLakes = v);
        builder.define("Remove All Lava Lakes", false).comment("Remove all lava lakes from the world.").sync(v -> this.noLavaLakes = v);
    }

}
