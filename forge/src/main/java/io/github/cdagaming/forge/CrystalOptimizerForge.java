package io.github.cdagaming.forge;

//import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import io.github.cdagaming.CrystalOptimizer;

@Mod(CrystalOptimizer.MOD_ID)
public final class CrystalOptimizerForge {
    public CrystalOptimizerForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
//        EventBuses.registerModEventBus(CrystalOptimizer.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        CrystalOptimizer.init();
    }
}
