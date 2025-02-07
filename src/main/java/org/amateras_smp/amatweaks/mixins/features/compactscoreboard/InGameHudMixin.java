// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.compactscoreboard;

import net.minecraft.client.gui.hud.InGameHud;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.text.NumberFormat;
import java.util.Locale;

//#if MC >= 12004
//$$ import net.minecraft.scoreboard.number.NumberFormatType;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//$$ import net.minecraft.text.MutableText;
//$$ import net.minecraft.text.Text;
//$$ import net.minecraft.util.Formatting;
//#endif

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private static final NumberFormat FORMATTER = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);

    static {
        FORMATTER.setMaximumFractionDigits(1);
    }

    //#if MC < 12004
    @Shadow
    private int scaledWidth;
    //#if MC < 12000
    //$$ @ModifyArgs(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"))
    //#else
    @ModifyArgs(method = "renderScoreboardSidebar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"))
    //#endif
    private void replaceWithCompactFormat(Args args) {
        String string = args.get(1);
        if (FeatureToggle.TWEAK_COMPACT_SCOREBOARD.getBooleanValue()) {
            String section;
            String remaining = string;

            if (string.startsWith("§") && string.length() > 1) {
                section = string.substring(0, 2);
                remaining = string.substring(2);
                string = section + FORMATTER.format(Integer.parseInt(remaining));
            } else {
                string = FORMATTER.format(Integer.parseInt(remaining));
            }
        }
        args.set(1, string);
        InGameHud instance = (InGameHud)(Object) this;
        int u = scaledWidth - 3 + 2;
        //#if MC >= 12000
        args.set(2, u - instance.getTextRenderer().getWidth(string));
        //#else
        //$$ args.set(2, (float) (u - instance.getTextRenderer().getWidth(string)));
        //#endif
    }
    //#else
    //$$
    //$$ @SuppressWarnings("unchecked")
    //$$ @ModifyArg(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/ScoreboardObjective;getNumberFormatOr(Lnet/minecraft/scoreboard/number/NumberFormat;)Lnet/minecraft/scoreboard/number/NumberFormat;"))
    //$$ private <T extends net.minecraft.scoreboard.number.NumberFormat> T replaceWithCompactFormat(T format) {
    //$$     if (!FeatureToggle.TWEAK_COMPACT_SCOREBOARD.getBooleanValue())
    //$$         return format;
    //$$
    //$$     return (T) new net.minecraft.scoreboard.number.NumberFormat() {
    //$$         @Override
    //$$         public MutableText format(int number) {
    //$$             return Text.literal(FORMATTER.format(number)).formatted(Formatting.RED);
    //$$         }
    //$$
    //$$          @Override
    //$$         public NumberFormatType<T> getType() {
    //$$             return null;
    //$$         }
    //$$     };
    //$$ }
    //#endif
}
