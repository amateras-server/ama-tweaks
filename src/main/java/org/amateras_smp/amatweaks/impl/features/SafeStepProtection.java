// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public class SafeStepProtection {
    public static boolean isPositionAllowedByBreakingRestriction(BlockPos pos) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player != null) {
            boolean isMovingForward = mc.options.keyUp.isDown() || mc.options.keyRight.isDown() || mc.options.keyLeft.isDown();
            // boolean isMoving = mc.player.getVelocity().x != 0 || mc.player.getVelocity().z != 0;
            // boolean isMovingBack = mc.options.backKey.isPressed();

            boolean isPosBelowPlayer = pos.getY() < player.getY();

            return !isMovingForward || !isPosBelowPlayer;
            // return (!isMoving || isMovingBack) || !isPosBelowPlayer;
        }

        return true;
    }
}
