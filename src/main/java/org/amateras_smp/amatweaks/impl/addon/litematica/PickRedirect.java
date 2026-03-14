// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.addon.litematica;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BuiltInRegistriesUtil;
import org.amateras_smp.amatweaks.impl.util.IdentifierUtil;
import java.util.Map;
import java.util.HashMap;

//#if MC >= 12104
import net.minecraft.core.Holder.Reference;
//#endif



public class PickRedirect {
    private static Map<Block, Block> redirectCache = null;

    public static void buildCache() {
        Map<Block, Block> newCache = new HashMap<>();
        for (String entry : Configs.Lists.PICK_REDIRECT_MAP.getStrings()) {
            String[] pair = entry.split(",", 2);
            if (pair.length != 2) continue;

            String[] b0Split = pair[0].trim().split(":", 2);
            String[] b1Split = pair[1].trim().split(":", 2);

            Identifier id0 = IdentifierUtil.of(b0Split[0], b0Split[1]);
            Identifier id1 = IdentifierUtil.of(b1Split[0], b1Split[1]);

            //#if MC >= 12104
            Reference<Block> b0 = BuiltInRegistriesUtil.BLOCK.get(id0).orElse(null);
            Reference<Block> b1 = BuiltInRegistriesUtil.BLOCK.get(id1).orElse(null);
            if (b0 != null && b1 != null) {
                newCache.put(b0.value(), b1.value());
            }
            //#else
            //$$ Block b0 = BuiltInRegistriesUtil.BLOCK.get(id0);
            //$$ Block b1 = BuiltInRegistriesUtil.BLOCK.get(id1);
            //$$ newCache.put(b0, b1);
            //#endif
        }
        redirectCache = newCache;
    }

    public static ItemStack getShouldPickItem (BlockState schematicState, ItemStack defaultStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return defaultStack;
        Inventory inventory = mc.player.getInventory();

        // if shouldPickStack is in the player inventory why should I override the pick behavior?
        if (inventory.findSlotMatchingItem(defaultStack) != -1) return defaultStack;

        if (redirectCache == null) buildCache();

        Block targetBlock = redirectCache.get(schematicState.getBlock());
        if (targetBlock == null) {
            return defaultStack;
        }

        int slot = inventory.findSlotMatchingItem(targetBlock.asItem().getDefaultInstance());
        if (slot == -1) return defaultStack;
        return inventory.getItem(slot);
    }
}
