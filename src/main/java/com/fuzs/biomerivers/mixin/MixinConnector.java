package com.fuzs.biomerivers.mixin;

import com.fuzs.biomerivers.BiomeRivers;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@SuppressWarnings("unused")
public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {

        Mixins.addConfiguration("META-INF/" + BiomeRivers.MODID + ".mixins.json");
    }

}
