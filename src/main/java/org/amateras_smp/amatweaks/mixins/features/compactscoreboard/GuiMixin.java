// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.compactscoreboard;

import net.minecraft.client.gui.Gui;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.text.NumberFormat;
import java.util.Locale;

//#if MC >= 12004
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import net.minecraft.network.chat.numbers.NumberFormatType;
//#else
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArgs;
//$$ import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//#endif

@Mixin(Gui.class)
public class GuiMixin {
    @Unique
    private static final NumberFormat FORMATTER = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);

    static {
        FORMATTER.setMaximumFractionDigits(1);
    }

    //#if MC >= 12004
    @SuppressWarnings("unchecked")
    @ModifyArg(method = "displayScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/scores/Objective;numberFormatOrDefault(Lnet/minecraft/network/chat/numbers/NumberFormat;)Lnet/minecraft/network/chat/numbers/NumberFormat;"))
    private <T extends net.minecraft.network.chat.numbers.NumberFormat> T replaceWithCompactFormat(T format) {
        if (!FeatureToggle.TWEAK_COMPACT_SCOREBOARD.getBooleanValue())
            return format;
        return (T) new net.minecraft.network.chat.numbers.NumberFormat() {
            @Override
            @NotNull
            public MutableComponent format(int number) {
                return net.minecraft.network.chat.Component.literal(FORMATTER.format(number)).withStyle(net.minecraft.ChatFormatting.RED);
            }

            @Override
            public NumberFormatType<? extends NumberFormat> type() {
                return null;
            }
        };
    }
    //#else
    //$$ @Shadow
    //$$ private int screenWidth;
    //$$ @ModifyArgs(method = "displayScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    //$$ private void replaceWithCompactFormat(Args args) {
    //$$     String string = args.get(1);
    //$$     if (FeatureToggle.TWEAK_COMPACT_SCOREBOARD.getBooleanValue()) {
    //$$         String section;
    //$$         String remaining = string;
    //$$
    //$$         if (string.startsWith("§") && string.length() > 1) {
    //$$             section = string.substring(0, 2);
    //$$             remaining = string.substring(2);
    //$$             string = section + FORMATTER.format(Integer.parseInt(remaining));
    //$$         } else {
    //$$             string = FORMATTER.format(Integer.parseInt(remaining));
    //$$         }
    //$$     }
    //$$     args.set(1, string);
    //$$     Gui instance = (Gui)(Object) this;
    //$$     int u = screenWidth - 3 + 2;
    //$$     //#if MC >= 12000
    //$$     args.set(2, u - instance.getFont().width(string));
    //$$     //#else
    //$$     //$$ args.set(2, (float) (u - instance.getFont().width(string)));
    //$$     //#endif
    //$$ }
    //#endif
}
