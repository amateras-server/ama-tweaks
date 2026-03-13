// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.holdmove;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "handleKeybinds", at = @At("HEAD"))
    private void onProcessKeybindsPre(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        if (mc.screen == null) {
            if (FeatureToggle.TWEAK_HOLD_FORWARD.getBooleanValue()) {
                KeyMapping.set(InputConstants.getKey(mc.options.keyUp.saveString()), true);
            }
            if (FeatureToggle.TWEAK_HOLD_BACK.getBooleanValue()) {
                KeyMapping.set(InputConstants.getKey(mc.options.keyDown.saveString()), true);
            }
            if (FeatureToggle.TWEAK_HOLD_LEFT.getBooleanValue()) {
                KeyMapping.set(InputConstants.getKey(mc.options.keyLeft.saveString()), true);
            }
            if (FeatureToggle.TWEAK_HOLD_RIGHT.getBooleanValue()) {
                KeyMapping.set(InputConstants.getKey(mc.options.keyRight.saveString()), true);
            }
        }
    }
}
