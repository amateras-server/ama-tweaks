package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.world.entity.player.Inventory;

public class InventoryUtil {
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
}
