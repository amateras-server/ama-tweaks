// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.monogui;

import net.minecraft.client.font.TextRenderer.Drawer;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Drawer.class)
public class TextRendererMixin {
    @ModifyVariable(method = "accept(ILnet/minecraft/text/Style;I)Z", at = @At("HEAD"), argsOnly = true)
    private Style onAccept(Style style){
        if (FeatureToggle.TWEAK_MONO_GUI.getBooleanValue()) return Style.EMPTY.withColor(Formatting.RESET);
        return style;
    }
}
