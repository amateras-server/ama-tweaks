// Copyright (c) 2026 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.InventoryUtil;

public class ItemPickupFilter {
    private static final ItemRestriction ITEM_PICKUP_FILTER_RESTRICTION = new ItemRestriction();

    public static void buildLists() {
        ITEM_PICKUP_FILTER_RESTRICTION.setListType((ItemRestriction.ListType) Configs.Lists.ITEM_PICKUP_FILTER_LIST_TYPE.getOptionListValue());
        ITEM_PICKUP_FILTER_RESTRICTION.setListContents(
            Configs.Lists.ITEM_PICKUP_FILTER_BLACK_LIST.getStrings(),
            Configs.Lists.ITEM_PICKUP_FILTER_WHITE_LIST.getStrings());
    }

    public static void acceptOrDrop(Minecraft minecraft, LocalPlayer player, int containerId, int rawSlotId, ItemStack stack) {
        if (!FeatureToggle.TWEAK_ITEM_PICKUP_FILTER.getBooleanValue() || ITEM_PICKUP_FILTER_RESTRICTION.isAllowed(stack.getItem())) {
            return;
        }
        MultiPlayerGameMode gameMode = minecraft.gameMode;
        AbstractContainerMenu containerMenu = player.containerMenu;
        if (gameMode == null) {
            return;
        }

        if (containerId != 0) {
            if (rawSlotId < 0 || rawSlotId >= containerMenu.slots.size()) {
                return;
            }
            Slot slot = containerMenu.getSlot(rawSlotId);
            // Only proceed if the item actually landed in the player's personal inventory slots within the chest GUI.
            if (slot.container != player.getInventory()) {
                return;
            }
        } else {
            // If it's the standard inventory (containerId 0), guard with standard crafting/armor grid offset.
            if (rawSlotId < net.minecraft.world.entity.player.Inventory.getSelectionSize() ||
                rawSlotId >= net.minecraft.world.entity.player.Inventory.INVENTORY_SIZE + net.minecraft.world.entity.player.Inventory.getSelectionSize()) {
                return;
            }
        }
        ChatFormatting color = stack.getRarity().
            //#if MC >= 12006
            color();
            //#else
            //$$ color;
            //#endif
        String itemStr = color + stack.getHoverName().getString() + GuiBase.TXT_RST;

        InventoryUtil.dropSlot(gameMode, containerId, rawSlotId, player);

        String message = GuiBase.TXT_YELLOW +  "[" + Reference.kModName + "] " + FeatureToggle.TWEAK_ITEM_PICKUP_FILTER.getPrettyName() + GuiBase.TXT_GRAY + ": " + GuiBase.TXT_RST + "Dropped " + itemStr;
        InfoUtils.printActionbarMessage(message);
    }
}
