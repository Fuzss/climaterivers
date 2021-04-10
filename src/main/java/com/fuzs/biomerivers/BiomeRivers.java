package com.fuzs.biomerivers;

import com.fuzs.biomerivers.element.ByeByeLakesElement;
import com.fuzs.biomerivers.element.ClimateRiversElement;
import com.fuzs.biomerivers.element.DiagonalFencesElement;
import com.fuzs.puzzleslib_br.PuzzlesLib;
import com.fuzs.puzzleslib_br.element.AbstractElement;
import com.fuzs.puzzleslib_br.element.ElementRegistry;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mod(BiomeRivers.MODID)
public class BiomeRivers extends PuzzlesLib {

    public static final String MODID = "biomerivers";
    public static final String NAME = "Biome Rivers";
    public static final Logger LOGGER = LogManager.getLogger(BiomeRivers.NAME);

    public static final AbstractElement CLIMATE_RIVERS = register("climate_rivers", ClimateRiversElement::new);
    public static final AbstractElement BYE_BYE_LAKES = register("bye_bye_lakes", ByeByeLakesElement::new);
    public static final AbstractElement DIAGONAL_FENCES = register("diagonal_fences", DiagonalFencesElement::new);

    public BiomeRivers() {

        ElementRegistry.setup(MODID, true);
    }

}
