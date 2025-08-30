package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.entity.player.PlayerInventory;

public class InventoryUtil {
    public static int getSelectedSlot(PlayerInventory inventory) {
        //#if MC < 12105
        return inventory.selectedSlot;
        //#else
        //$$ return inventory.getSelectedSlot();
        //#endif
    }

    public static void setSelectedSlot(PlayerInventory inventory, int slot) {
        //#if MC < 12105
        inventory.selectedSlot = slot;
        //#else
        //$$ inventory.setSelectedSlot(slot);
        //#endif
    }
}
