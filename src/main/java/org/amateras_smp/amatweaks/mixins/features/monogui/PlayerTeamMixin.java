// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.monogui;

import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.ChatFormatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTeam.class)
public class PlayerTeamMixin {
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void onGetColor(CallbackInfoReturnable<ChatFormatting> cir) {
        if (FeatureToggle.TWEAK_MONO_TEAM_COLOR.getBooleanValue()) {
            cir.setReturnValue(ChatFormatting.RESET);
        }
    }
}
