// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.auto_glide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.AutoGlide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow
    private boolean wasFallFlying;

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Unique
    private int autoGlideTickCount = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!FeatureToggle.TWEAK_AUTO_FIREWORK_GLIDE.getBooleanValue()) return;
        if (wasFallFlying) {
            autoGlideTickCount++;
            if (autoGlideTickCount != 0 && autoGlideTickCount % Configs.Generic.AUTO_GLIDE_USE_ROCKET_INTERVAL.getIntegerValue() == 0) {
                if (minecraft.player == null) return;
                if (minecraft.player.getDeltaMovement().length() <= Configs.Generic.AUTO_EAT_THRESHOLD.getDoubleValue()) {
                    AutoGlide.autoUseRocket(minecraft);
                }
            }
        } else {
            autoGlideTickCount = 0;
        }
    }
}
