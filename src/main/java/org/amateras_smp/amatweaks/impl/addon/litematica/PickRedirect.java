// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.addon.litematica;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BuiltInRegistriesUtil;
import org.amateras_smp.amatweaks.impl.util.IdentifierUtil;

//#if MC >= 12104
import java.util.Optional;
import net.minecraft.core.Holder.Reference;
//#endif


public class PickRedirect {
    public static ItemStack getShouldPickItem (BlockState schematicState, ItemStack shouldPickItemStack) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return shouldPickItemStack;
        Inventory inventory = mc.player.getInventory();

        // if shouldPickStack is in the player inventory why should I override the pick behavior?
        if (inventory.findSlotMatchingItem(shouldPickItemStack) != -1) return shouldPickItemStack;

        Block shouldPickBlock = null;
        for (String entry : Configs.Lists.PICK_REDIRECT_MAP.getStrings()) {
            String[] split = entry.split("\s*,\s*");
            //#if MC >= 12104
            Optional<Reference<Block>> b0 = BuiltInRegistriesUtil.BLOCK.get(IdentifierUtil.ofVanilla(split[0]));
            Optional<Reference<Block>> b1 = BuiltInRegistriesUtil.BLOCK.get(IdentifierUtil.ofVanilla(split[1]));
            if (b0.isPresent() && b0.get().value() == schematicState.getBlock() && b1.isPresent()) {
                shouldPickBlock = b1.get().value();
            }
            //#else
            //$$ if (BuiltInRegistriesUtil.BLOCK.get(IdentifierUtil.ofVanilla(split[0])) == schematicState.getBlock()) {
            //$$     shouldPickBlock = BuiltInRegistriesUtil.BLOCK.get(IdentifierUtil.ofVanilla(split[1]));
            //$$ }
            //#endif

            if (shouldPickBlock != null) {
                break;
            }
        }
        if (shouldPickBlock == null) return shouldPickItemStack;

        int slot = inventory.findSlotMatchingItem(shouldPickBlock.asItem().getDefaultInstance());
        if (slot == -1) return shouldPickItemStack;
        return inventory.getItem(slot);
    }
}
