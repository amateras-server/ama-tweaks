// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.autoeat;

import net.minecraft.client.Minecraft;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.AutoEat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    static Minecraft instance;

    //#if MC >= 12110
    @Inject(method = "finishProfilers", at = @At(value = "HEAD"))
    //#else
    //$$ @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;endTick()V"))
    //#endif
    private void onTick(CallbackInfo ci) {
        if (FeatureToggle.TWEAK_AUTO_EAT.getBooleanValue() && instance.player != null && instance.gameMode != null) {
            AutoEat.autoEat(instance, instance.player, instance.player.connection);
        }
    }
}
