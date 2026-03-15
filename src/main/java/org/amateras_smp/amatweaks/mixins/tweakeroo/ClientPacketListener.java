// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.tweakeroo;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {@Condition(Reference.ModIds.tweakeroo)})
@Mixin(net.minecraft.client.multiplayer.ClientPacketListener.class)
public class ClientPacketListener {
    @Unique
    private static final boolean isTweakerooLoaded = checkTweakeroo();

    @Unique
    private static boolean checkTweakeroo() {
        try {
            Class.forName("fi.dy.masa.tweakeroo.config.FeatureToggle");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void handleLogin(CallbackInfo ci) {
        if (Configs.Generic.PERSISTENT_GAMMA_OVERRIDE.getBooleanValue()) {
            if (isTweakerooLoaded) {
                fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
            }
        }
    }
}
