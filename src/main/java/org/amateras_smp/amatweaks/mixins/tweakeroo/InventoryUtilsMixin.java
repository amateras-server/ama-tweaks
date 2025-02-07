// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.tweakeroo;

import fi.dy.masa.tweakeroo.util.InventoryUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.addon.tweakeroo.SelectiveToolSwitch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(Reference.ModIds.tweakeroo))
@Mixin(InventoryUtils.class)
public class InventoryUtilsMixin {
    @Inject(method = "trySwitchToEffectiveTool", at = @At("HEAD"), cancellable = true)
    private static void onSwitchTool(BlockPos pos, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        if (player != null && mc.world != null) {
            BlockState state = mc.world.getBlockState(pos);
            if (SelectiveToolSwitch.restrict(state)) ci.cancel();
        }
    }
}
