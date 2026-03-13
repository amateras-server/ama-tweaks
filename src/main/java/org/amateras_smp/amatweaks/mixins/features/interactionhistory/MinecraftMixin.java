// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.interactionhistory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.amateras_smp.amatweaks.impl.features.InteractionHistory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void onJoinWorld(CallbackInfo ci) {
        InteractionHistory.clear();
    }

    //#if MC >= 12002
    @Inject(method = "clearClientLevel", at = @At("HEAD"))
    //#else
    //$$ @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
    //#endif
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        InteractionHistory.clear();
    }
}
