// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class SafeStepProtection {
    public static boolean isPositionAllowedByBreakingRestriction(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player != null) {
            boolean isMovingForward = mc.options.forwardKey.isPressed() || mc.options.rightKey.isPressed() || mc.options.leftKey.isPressed();
            // boolean isMoving = mc.player.getVelocity().x != 0 || mc.player.getVelocity().z != 0;
            // boolean isMovingBack = mc.options.backKey.isPressed();

            boolean isPosBelowPlayer = pos.getY() < player.getY();

            return !isMovingForward || !isPosBelowPlayer;
            // return (!isMoving || isMovingBack) || !isPosBelowPlayer;
        }

        return true;
    }
}
