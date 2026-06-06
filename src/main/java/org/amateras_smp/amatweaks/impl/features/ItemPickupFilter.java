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
import net.minecraft.world.item.ItemStack;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.config.Configs;

//#if MC >= 260000
import net.minecraft.world.inventory.ContainerInput;
//#else
//$$ import net.minecraft.world.inventory.ClickType;
//#endif

public class ItemPickupFilter {
    private static final ItemRestriction ITEM_PICKUP_FILTER_RESTRICTION = new ItemRestriction();

    private static final int OUTSIDE_SCREEN_SLOT_ID = -999;

    public static void buildLists() {
        ITEM_PICKUP_FILTER_RESTRICTION.setListType((ItemRestriction.ListType) Configs.Lists.ITEM_PICKUP_FILTER_LIST_TYPE.getOptionListValue());
        ITEM_PICKUP_FILTER_RESTRICTION.setListContents(
            Configs.Lists.ITEM_PICKUP_FILTER_BLACK_LIST.getStrings(),
            Configs.Lists.ITEM_PICKUP_FILTER_WHITE_LIST.getStrings());
    }

    public static void acceptOrDrop(Minecraft minecraft, LocalPlayer player, int slotId, ItemStack stack) {
        if (!FeatureToggle.TWEAK_ITEM_PICKUP_FILTER.getBooleanValue() || ITEM_PICKUP_FILTER_RESTRICTION.isAllowed(stack.getItem())) {
            return;
        }
        MultiPlayerGameMode gameMode = minecraft.gameMode;
        if (gameMode == null) {
            return;
        }

        ChatFormatting color = stack.getRarity().
            //#if MC >= 12006
            color();
            //#else
            //$$ color;
            //#endif
        String itemStr = color + stack.getHoverName().getString() + GuiBase.TXT_RST;

        //#if MC >= 260000
        gameMode.handleContainerInput(0, slotId, 0, ContainerInput.PICKUP, player);
        gameMode.handleContainerInput(0, OUTSIDE_SCREEN_SLOT_ID, 0, ContainerInput.PICKUP, player);
        //#else
        //$$ gameMode.handleInventoryMouseClick(0, slotId, 0, ClickType.PICKUP, player);
        //$$ gameMode.handleInventoryMouseClick(0, OUTSIDE_SCREEN_SLOT_ID, 0, ClickType.PICKUP, player);
        //#endif

        String message = GuiBase.TXT_YELLOW +  "[" + Reference.kModName + "] " + FeatureToggle.TWEAK_ITEM_PICKUP_FILTER.getPrettyName() + GuiBase.TXT_GRAY + ": " + GuiBase.TXT_RST + "Dropped " + itemStr;
        InfoUtils.printActionbarMessage(message);
    }
}
