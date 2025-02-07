// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.addon.tweakermore;

import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.amateras_smp.amatweaks.config.Configs;

public class SelectiveAutoPick {
    public static final ItemRestriction AUTO_PICK_RESTRICTION = new ItemRestriction();

    public static void buildLists() {
        AUTO_PICK_RESTRICTION.setListType((UsageRestriction.ListType) Configs.Lists.SELECTIVE_AUTO_PICK_LIST_TYPE.getOptionListValue());
        AUTO_PICK_RESTRICTION.setListContents(
                Configs.Lists.SELECTIVE_AUTO_PICK_BLACKLIST.getStrings(),
                Configs.Lists.SELECTIVE_AUTO_PICK_WHITELIST.getStrings());
    }

    public static boolean restrict(ClientPlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            return false;
        }
        return !AUTO_PICK_RESTRICTION.isAllowed(stack.getItem());
    }
}
