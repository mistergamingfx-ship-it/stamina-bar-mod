package com.example.staminabar;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = StaminaBar.MODID,
    name = StaminaBar.NAME,
    version = StaminaBar.VERSION,
    acceptedMinecraftVersions = "[1.12.1]"
)
public class StaminaBar {
    public static final String MODID = "staminabar";
    public static final String NAME = "Stamina Bar";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Forge loads the configuration automatically from the @Config class.
    }

    @Config(modid = MODID, name = "StaminaBar")
    public static class Settings {
        @Config.Name("Maximum stamina")
        @Config.Comment("Maximum stamina points. Recommended: 100.")
        @Config.RangeInt(min = 20, max = 500)
        public static int maximumStamina = 100;

        @Config.Name("Sprint drain")
        @Config.Comment("Stamina points consumed per tick while sprinting.")
        @Config.RangeDouble(min = 0.05, max = 5.0)
        public static double sprintDrain = 0.35D;

        @Config.Name("Recovery")
        @Config.Comment("Stamina points restored per tick when not sprinting.")
        @Config.RangeDouble(min = 0.05, max = 5.0)
        public static double recovery = 0.55D;

        @Config.Name("Jump cost")
        @Config.Comment("Stamina points consumed by a normal jump.")
        @Config.RangeDouble(min = 0.0, max = 50.0)
        public static double jumpCost = 7.0D;

        @Config.Name("Show stamina bar")
        public static boolean showBar = true;
    }
}
