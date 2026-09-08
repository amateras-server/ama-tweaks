// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

//#if MC < 11900
//$$ import net.minecraft.network.chat.TextComponent;
//#endif

public class TextUtil {
    public static Component withFormat(String message, ChatFormatting formatting) {
        //#if MC >= 11900
        return Component.literal(message).withStyle(formatting);
        //#else
        //$$ return new TextComponent(message).withStyle(formatting);
        //#endif
    }

    public static MutableComponent createEmpty() {
        //#if MC >= 11900
        return Component.literal("");
        //#else
        //$$ return new TextComponent("");
        //#endif
    }
}
