package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ScreenUtil {
    public static Screen getScreen(Minecraft mc) {
        //#if MC >= 260200
        return mc.gui.screen();
        //#else
        //$$ return mc.screen;
        //#endif
    }

    public static void setScreen(Minecraft mc, Screen screen) {
        //#if MC >= 260200
        mc.gui.setScreen(screen);
        //#else
        //$$ mc.setScreen(screen);
        //#endif
    }
}
