// Copyright (c) 2026 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;

//#if MC >= 260000
import net.minecraft.world.inventory.ContainerInput;
//#else
//$$ import net.minecraft.world.inventory.ClickType;
//#endif


public class InventoryUtil {
    private static final int OUTSIDE_SCREEN_SLOT_ID = -999;

    public static int getSelectedSlot(Inventory inventory) {
        //#if MC >= 12105
        return inventory.getSelectedSlot();
        //#else
        //$$ return inventory.selected;
        //#endif
    }

    public static void setSelectedSlot(Inventory inventory, int slot) {
        //#if MC >= 12105
        inventory.setSelectedSlot(slot);
        //#else
        //$$ inventory.selected = slot;
        //#endif
    }

    public static void dropSlot(MultiPlayerGameMode gameMode, int containerId, int slotId, LocalPlayer player) {
        //#if MC >= 260000
        gameMode.handleContainerInput(containerId, slotId, 0, ContainerInput.PICKUP, player);
        gameMode.handleContainerInput(containerId, OUTSIDE_SCREEN_SLOT_ID, 0, ContainerInput.PICKUP, player);
        //#else
        //$$ gameMode.handleInventoryMouseClick(containerId, slotId, 0, ClickType.PICKUP, player);
        //$$ gameMode.handleInventoryMouseClick(containerId, OUTSIDE_SCREEN_SLOT_ID, 0, ClickType.PICKUP, player);
        //#endif
    }
}
