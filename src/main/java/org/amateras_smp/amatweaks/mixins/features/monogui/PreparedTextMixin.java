// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.monogui;

import net.minecraft.client.gui.Font.PreparedTextBuilder;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PreparedTextBuilder.class)
public class PreparedTextMixin {
    //#if MC >= 12108
    @ModifyVariable(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z", at = @At("HEAD"), argsOnly = true)
    //#else
    //$$ @ModifyVariable(method = "accept", at = @At("HEAD"), argsOnly = true)
    //#endif
    private Style onAccept(Style style){
        if (FeatureToggle.TWEAK_MONO_GUI.getBooleanValue()) return Style.EMPTY.withColor(ChatFormatting.RESET);
        return style;
    }
}
