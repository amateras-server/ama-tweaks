// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

//#if MC < 11900
//$$ import net.minecraft.text.LiteralText;
//#endif

public class TextUtil {
    public static Text withFormat(String message, Formatting formatting) {
        //#if MC < 11900
        //$$ return new LiteralText(message).formatted(formatting);
        //#else
        return Text.literal(message).formatted(formatting);
        //#endif
    }
}
