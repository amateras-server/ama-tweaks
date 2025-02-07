// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.holdmove;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onProcessKeybindsPre(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        //#if MC >= 11802
        if (mc.player == null || mc.player.getWorld() == null) return;
        //#else
        //$$ if (mc.player == null || mc.player.world == null) return;
        //#endif

        if (mc.currentScreen == null)
        {
            if (FeatureToggle.TWEAK_HOLD_FORWARD.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.forwardKey.getBoundKeyTranslationKey()), true);
            }
            if (FeatureToggle.TWEAK_HOLD_BACK.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.backKey.getBoundKeyTranslationKey()), true);
            }
            if (FeatureToggle.TWEAK_HOLD_LEFT.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.leftKey.getBoundKeyTranslationKey()), true);
            }
            if (FeatureToggle.TWEAK_HOLD_RIGHT.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.rightKey.getBoundKeyTranslationKey()), true);
            }
        }
    }
}